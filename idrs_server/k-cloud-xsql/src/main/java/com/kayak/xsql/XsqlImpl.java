package com.kayak.xsql;

import com.alibaba.fastjson.JSON;
import com.kayak.core.sql.UpdateResult;
import com.kayak.xsql.parameter.FieldGetterAutoId;
import com.kayak.xsql.parameter.ParameterHandler;
import com.kayak.xsql.parameter.ParameterPlanner;
import com.kayak.xsql.result.ResultHandler;
import com.kayak.xsql.result.ResultPlanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Esql的基础实现
 * 
 * @author zuojie
 * 
 */
public abstract class XsqlImpl implements Xsql {
	private static final Logger log = LoggerFactory.getLogger(XsqlImpl.class);

	private DataSource defaultDataSource; // 默认数据源

	// 事务上下文，线程本地数据
	private ThreadLocal<LinkedList<Context>> localContext = new ThreadLocal<>(); // 因为多数据源路由支持嵌套数据源，所以，这了用了一个栈

	protected ParameterPlanner planer1 = new ParameterPlanner();
	protected ResultPlanner planner2 = new ResultPlanner();

	protected Map<String, ParameterHandler> cache1 = new ConcurrentHashMap<>(); // 参数处理器缓存
	protected Map<String, ResultHandler<?>> cache2 = new ConcurrentHashMap<>(); // 结果处理器缓存

	public static final Map<Connection, String> dbKeepMap = new ConcurrentHashMap<Connection, String>();

//	private XsqlHelper helper = new XsqlHelper();

	public XsqlImpl() {
//		helper.setEsql(this);
	}

	public void setDefaultDataSource(DataSource source) {
		this.defaultDataSource = source;
	}

	@Override
	public AutoCloseable selectDataSource(final DataSource source, int sharding) {
		LinkedList<Context> stack = localContext.get();
		if (stack == null) {
			stack = new LinkedList<>();
			localContext.set(stack);
		}

		if (!stack.isEmpty()) {
			Context context = stack.peek();
			if (context.source == source) {
				return null;
			}
		}

		Context context = new Context();
		stack.push(context);
		context.source = source;
		context.sharding = sharding;

		return new AutoCloseable() {
			@Override
			public void close() throws Exception {
				popDataSource();
			}
		};
	}

	private void popDataSource() {
		LinkedList<Context> stack = localContext.get();
		Context context = stack.pop();
		XsqlUtils.close(context.connection);
	}

	@Override
	public int getDataSourceIndex() {
		Context context = getContext();
		return context.sharding;
	}

	private Context getContext() {
		LinkedList<Context> stack = localContext.get();
		if (stack == null) {
			stack = new LinkedList<>();
			localContext.set(stack);
		}
		// 当前无数据源时，使用默认数据源
		if (stack.isEmpty()) {
			Context context = new Context();
			stack.push(context);
			context.source = defaultDataSource;
			context.sharding = -1;
		}

		return stack.peek();
	}

	/** 获取底层数据库连接。注意：不要自行调用Connection的close方法，在事务结束时，xsql会自动调用Connection的close方法。 */
	@Override
	public Connection getConnection() throws SQLException {
		// 取得事务上下文
		Context context = getContext();

		// 如果已经获取数据连接，就直接使用，否则获取一个新的连接
		Connection db = context.connection;
		if (db == null) {
			db = context.source.getConnection();

			// 存储当前使用链接的栈信息
			StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
			StringBuilder stackTraceStringBuilder = new StringBuilder();
			for (int i = 0; i < stackTraces.length; i++) {
				if (i >= 10) {
					break;
				}
				stackTraceStringBuilder.append(stackTraces[i].getClassName() + "." + stackTraces[i].getMethodName());
				stackTraceStringBuilder.append("\n");
			}
			dbKeepMap.put(db, stackTraceStringBuilder.toString());

			db.setAutoCommit(context.depth == 0); // 当depth为0时表示在自动提交模式
			context.connection = db;
		}

		return db;
	}

	@Override
	public void releaseConnection() {
		Context context = getContext();
		if (context.depth > 0) {
			return;
		}

		XsqlUtils.close(context.connection);
		context.connection = null;
	}

	// -----------------------------

	@Override
	public String databaseName() throws SQLException {
		try {
			// 获取数据库连接
			Connection db = getConnection();
			if (db == null) {
				throw new SQLException("Current database not connected. Data source index is " + getDataSourceIndex());
			}
			return db.getMetaData().getDatabaseProductName();
		} finally {
			releaseConnection();
		}
	}

	// -----------------------------

	/** 开始一个事务，采用指定的并发隔离度，从指定连接池取一个连接 */
	@Override
	public void begin() throws Exception {
		Context context = getContext();
		context.depth++;

		Connection db = context.connection;
		if (db != null)
			db.setAutoCommit(false);
	}

	/** 提交事务 */
	@Override
	public boolean commit() throws Exception {
		Context context = getContext();

		Connection db = context.connection;
		if (db == null)
			return false;

		if (context.depth == 1) { // 处于顶层事务中，才执行真正的提交动作
			if (log.isTraceEnabled())
				log.trace("提交事务");

			db.commit();
			XsqlUtils.close(context.connection);
			context.connection = null;
		} else {
			context.depth--;
		}

		return true;
	}

	/** 结束事务，尚未提交的事务被回滚 */
	@Override
	public boolean end() {
		Context context = getContext();

		// 如果嵌套事务未回到顶层，不做处理
		context.depth--;
		if (context.depth > 0)
			return false;

		// 回滚未提交的事务
		Connection db = context.connection;
		if (db == null)
			return true;

		try {
			if (log.isTraceEnabled())
				log.trace("回滚事务");
			db.rollback();
		} catch (Throwable e) {
			log.error("回滚异常");
		} finally {
			releaseConnection();
		}

		return true;
	}

	// ================================================================================
	/** 执行返回一行结果的查询，将结果封装成指定类型的对象。 */
	@Override
	public <T> T query(Class<T> clazz, String sql, Object... params) throws Exception {
		Object param = normalize(params);

		if (log.isDebugEnabled()) {
			log.debug("单行查询: " + sql);
			logParams(param);
		}

		try {
			Connection db = getConnection();
			ParameterHandler ph = planParameterHandler(sql, param);
			try (PreparedStatement ps = db.prepareStatement(ph.getSql(param))) {
				ph.setParameters(ps, param);
				try (ResultSet rs = ps.executeQuery()) {
					ResultHandler<T> rh = planResultHandler(clazz, sql, ps);
					if (rs.next())
						return rh.build(rs);

					return null;
				}
			}
		} finally {
			releaseConnection();
		}
	}

	@Override
	public <T> List<T> listNoSqlLog(Class<T> clazz, String sql, Object... params) throws Exception {

		Object param = normalize(params);

		if (log.isDebugEnabled()) {
			log.debug("多行查询: " + sql);
			logParams(param);
		}

		try {
			Connection db = getConnection();

			ParameterHandler ph = planParameterHandler(sql, param);
			try (PreparedStatement ps = db.prepareStatement(ph.getSql(param))) {
				ph.setParameters(ps, param, true);
				try (ResultSet rs = ps.executeQuery()) {
					ResultHandler<T> handler = planResultHandler(clazz, sql, ps);

					List<T> list = new ArrayList<>();
					while (rs.next()) {
						list.add(handler.build(rs));
					}
					if(sql.indexOf("SYS_PARAM")>= 0||sql.indexOf("sys_param")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VN_BM_BAL")>= 0||sql.indexOf("i_fv_vn_bm_bal")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VN_BM_BAL_COPY")>= 0||sql.indexOf("i_fv_vn_bm_bal_copy")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VF_REPORT_VAL")>= 0||sql.indexOf("i_fv_vf_report_val")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VF_REPORT_VAL_COPY")>= 0||sql.indexOf("i_fv_vf_report_val_copy")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VN_FINANCIAL_VAL")>= 0||sql.indexOf("i_fv_vn_financial_val")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VN_FINANCIAL_VAL_COPY")>= 0||sql.indexOf("i_fv_vn_financial_val_copy")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VN_REPORTDATA_CWZH")>= 0||sql.indexOf("i_fv_vn_reportdata_cwzh")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VN_REPORTDATA_CWZH_COPY")>= 0||sql.indexOf("i_fv_vn_reportdata_cwzh_copy")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_FP_P_PRODUCT")>= 0||sql.indexOf("a_fp_p_product")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_FP_P_PARENT_PRODUCT_DAY_NETVALUE")>= 0||sql.indexOf("a_fp_p_parent_product_day_netvalue")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FP_R_PRODUCT_CONFIRM_SUM")>= 0||sql.indexOf("i_fp_r_product_confirm_sum")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FP_R_PRODUCT_CONFIRM_SUM_COPY")>= 0||sql.indexOf("i_fp_r_product_confirm_sum_copy")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FP_R_PRODUCT_IPO_SUM")>= 0||sql.indexOf("i_fp_r_product_ipo_sum")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FP_R_PRODUCT_IPO_SUM_COPY")>= 0||sql.indexOf("i_fp_r_product_ipo_sum_copy")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_FP_P_PRODUCT_DAY_NETVALUE")>= 0||sql.indexOf("a_fp_p_product_day_netvalue")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_FP_P_PRODUCT_CAPITAL_ACCOUNT")>= 0||sql.indexOf("a_fp_p_product_capital_account")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_TA_ACCOUNTDEPOSIT")>= 0||sql.indexOf("a_ta_accountdeposit")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_TA_TTPLANINFO")>= 0||sql.indexOf("i_ta_ttplaninfo")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_TA_TTPLANINFO_COPY")>= 0||sql.indexOf("i_ta_ttplaninfo_copy")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_FP_P_PARENT_PRODUCT")>= 0||sql.indexOf("a_fp_p_parent_product")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_FP_P_PRODUCT_LIMIT")>= 0||sql.indexOf("a_fp_p_product_limit")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_FP_P_PRODUCT_OPEN_CYCLE")>= 0||sql.indexOf("a_fp_p_product_open_cycle")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_FP_P_PRODUCT_OPEN_DAY")>= 0||sql.indexOf("a_fp_p_product_open_day")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_SA_PRD_VALUATION")>= 0||sql.indexOf("a_sa_prd_valuation")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_SA_PRD_INFO")>= 0||sql.indexOf("a_sa_prd_info")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("CIHDQUOTE")>= 0||sql.indexOf("cihdquote")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("IBBONDINDEX")>= 0||sql.indexOf("ibbondindex")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("INTERESTRATE")>= 0||sql.indexOf("interestrate")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FP_R_PRODUCT_ESTABLISH_SUM")>= 0||sql.indexOf("i_fp_r_product_establish_sum")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FP_R_PRODUCT_ESTABLISH_SUM_COPY")>= 0||sql.indexOf("i_fp_r_product_establish_sum_copy")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VF_REPORTDATA_CWZH")>= 0||sql.indexOf("i_fv_vf_reportdata_cwzh")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VF_REPORTDATA_CWZH_COPY")>= 0||sql.indexOf("i_fv_vf_reportdata_cwzh_copy")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FP_R_PRODUCT_BALANCE")>= 0||sql.indexOf("i_fp_r_product_balance")>= 0){
						removePlanParameterHandler(sql, param);
					}
					return list;
				}
			}
		} finally {
			releaseConnection();
		}
	}

	/** 执行返回多行结果的查询，将结果封装成指定类型的对象列表。 */
	@Override
	public <T> List<T> list(Class<T> clazz, String sql, Object... params) throws Exception {

		Object param = normalize(params);

		if (log.isDebugEnabled()) {
			log.debug("多行查询: " + sql);
			logParams(param);
		}

		try {
			Connection db = getConnection();

			ParameterHandler ph = planParameterHandler(sql, param);
			try (PreparedStatement ps = db.prepareStatement(ph.getSql(param))) {
				ph.setParameters(ps, param);
				try (ResultSet rs = ps.executeQuery()) {
					ResultHandler<T> handler = planResultHandler(clazz, sql, ps);

					List<T> list = new ArrayList<>();
					while (rs.next()) {
						list.add(handler.build(rs));
					}
					if(sql.indexOf("SYS_PARAM")>= 0||sql.indexOf("sys_param")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VN_BM_BAL")>= 0||sql.indexOf("i_fv_vn_bm_bal")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VN_BM_BAL_COPY")>= 0||sql.indexOf("i_fv_vn_bm_bal_copy")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VF_REPORT_VAL")>= 0||sql.indexOf("i_fv_vf_report_val")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VF_REPORT_VAL_COPY")>= 0||sql.indexOf("i_fv_vf_report_val_copy")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VN_FINANCIAL_VAL")>= 0||sql.indexOf("i_fv_vn_financial_val")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VN_FINANCIAL_VAL_COPY")>= 0||sql.indexOf("i_fv_vn_financial_val_copy")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VN_REPORTDATA_CWZH")>= 0||sql.indexOf("i_fv_vn_reportdata_cwzh")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VN_REPORTDATA_CWZH_COPY")>= 0||sql.indexOf("i_fv_vn_reportdata_cwzh_copy")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_FP_P_PRODUCT")>= 0||sql.indexOf("a_fp_p_product")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_FP_P_PARENT_PRODUCT_DAY_NETVALUE")>= 0||sql.indexOf("a_fp_p_parent_product_day_netvalue")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FP_R_PRODUCT_CONFIRM_SUM")>= 0||sql.indexOf("i_fp_r_product_confirm_sum")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FP_R_PRODUCT_CONFIRM_SUM_COPY")>= 0||sql.indexOf("i_fp_r_product_confirm_sum_copy")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FP_R_PRODUCT_IPO_SUM")>= 0||sql.indexOf("i_fp_r_product_ipo_sum")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FP_R_PRODUCT_IPO_SUM_COPY")>= 0||sql.indexOf("i_fp_r_product_ipo_sum_copy")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_FP_P_PRODUCT_DAY_NETVALUE")>= 0||sql.indexOf("a_fp_p_product_day_netvalue")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_FP_P_PRODUCT_CAPITAL_ACCOUNT")>= 0||sql.indexOf("a_fp_p_product_capital_account")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_TA_ACCOUNTDEPOSIT")>= 0||sql.indexOf("a_ta_accountdeposit")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_TA_TTPLANINFO")>= 0||sql.indexOf("i_ta_ttplaninfo")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_TA_TTPLANINFO_COPY")>= 0||sql.indexOf("i_ta_ttplaninfo_copy")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_FP_P_PARENT_PRODUCT")>= 0||sql.indexOf("a_fp_p_parent_product")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_FP_P_PRODUCT_LIMIT")>= 0||sql.indexOf("a_fp_p_product_limit")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_FP_P_PRODUCT_OPEN_CYCLE")>= 0||sql.indexOf("a_fp_p_product_open_cycle")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_FP_P_PRODUCT_OPEN_DAY")>= 0||sql.indexOf("a_fp_p_product_open_day")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_SA_PRD_VALUATION")>= 0||sql.indexOf("a_sa_prd_valuation")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("A_SA_PRD_INFO")>= 0||sql.indexOf("a_sa_prd_info")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("CIHDQUOTE")>= 0||sql.indexOf("cihdquote")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("IBBONDINDEX")>= 0||sql.indexOf("ibbondindex")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("INTERESTRATE")>= 0||sql.indexOf("interestrate")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FP_R_PRODUCT_ESTABLISH_SUM")>= 0||sql.indexOf("i_fp_r_product_establish_sum")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FP_R_PRODUCT_ESTABLISH_SUM_COPY")>= 0||sql.indexOf("i_fp_r_product_establish_sum_copy")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VF_REPORTDATA_CWZH")>= 0||sql.indexOf("i_fv_vf_reportdata_cwzh")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FV_VF_REPORTDATA_CWZH_COPY")>= 0||sql.indexOf("i_fv_vf_reportdata_cwzh_copy")>= 0){
						removePlanParameterHandler(sql, param);
					}
					if(sql.indexOf("I_FP_R_PRODUCT_BALANCE")>= 0||sql.indexOf("i_fp_r_product_balance")>= 0){
						removePlanParameterHandler(sql, param);
					}
					return list;
				}
			}
		} finally {
			releaseConnection();
		}
	}

	/** 执行更新查询，返回更新行数。 */
	@Override
	public UpdateResult update(String sql, Object... params) throws Exception {
		Object param = normalize(params);

		if (log.isDebugEnabled()) {
			log.debug("更新查询: " + sql);
			logParams(param);
		}

		try {
			// 如果有自增ID，先预先生成自增ID
			ParameterHandler ph = planParameterHandler(sql, param);
			FieldGetterAutoId fieldGetterAutoId = ph.getFieldGetterAutoId();
			if (fieldGetterAutoId != null) {
				fieldGetterAutoId.prepare();
			}

			Connection db = getConnection();

			try (PreparedStatement ps = db.prepareStatement(ph.getSql(param))) {
				ph.setParameters(ps, param);
				int effect = ps.executeUpdate();

				UpdateResult updateResult = ph.getUpdateResult();
				if (updateResult == null) {
					updateResult = new UpdateResult();
				}

				updateResult.setEffect(effect);
				if(sql.indexOf("SYS_PARAM")>= 0||sql.indexOf("sys_param")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VN_BM_BAL")>= 0||sql.indexOf("i_fv_vn_bm_bal")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VN_BM_BAL_COPY")>= 0||sql.indexOf("i_fv_vn_bm_bal_copy")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VF_REPORT_VAL")>= 0||sql.indexOf("I_FV_VF_REPORT_VAL")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VF_REPORT_VAL_COPY")>= 0||sql.indexOf("i_fv_vf_report_val_copy")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VN_FINANCIAL_VAL")>= 0||sql.indexOf("i_fv_vn_financial_val")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VN_FINANCIAL_VAL_COPY")>= 0||sql.indexOf("i_fv_vn_financial_val_copy")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VN_REPORTDATA_CWZH")>= 0||sql.indexOf("i_fv_vn_reportdata_cwzh")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VN_REPORTDATA_CWZH_COPY")>= 0||sql.indexOf("i_fv_vn_reportdata_cwzh_copy")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_FP_P_PRODUCT")>= 0||sql.indexOf("a_fp_p_product")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_FP_P_PARENT_PRODUCT_DAY_NETVALUE")>= 0||sql.indexOf("a_fp_p_parent_product_day_netvalue")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FP_R_PRODUCT_CONFIRM_SUM")>= 0||sql.indexOf("i_fp_r_product_confirm_sum")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FP_R_PRODUCT_CONFIRM_SUM_COPY")>= 0||sql.indexOf("i_fp_r_product_confirm_sum_copy")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FP_R_PRODUCT_IPO_SUM")>= 0||sql.indexOf("i_fp_r_product_ipo_sum")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FP_R_PRODUCT_IPO_SUM_COPY")>= 0||sql.indexOf("i_fp_r_product_ipo_sum_copy")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_FP_P_PRODUCT_DAY_NETVALUE")>= 0||sql.indexOf("a_fp_p_product_day_netvalue")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_FP_P_PRODUCT_CAPITAL_ACCOUNT")>= 0||sql.indexOf("a_fp_p_product_capital_account")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_TA_ACCOUNTDEPOSIT")>= 0||sql.indexOf("a_ta_accountdeposit")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_TA_TTPLANINFO")>= 0||sql.indexOf("i_ta_ttplaninfo")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_TA_TTPLANINFO_COPY")>= 0||sql.indexOf("i_ta_ttplaninfo_copy")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_FP_P_PARENT_PRODUCT")>= 0||sql.indexOf("a_fp_p_parent_product")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_FP_P_PRODUCT_LIMIT")>= 0||sql.indexOf("a_fp_p_product_limit")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_FP_P_PRODUCT_OPEN_CYCLE")>= 0||sql.indexOf("a_fp_p_product_open_cycle")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_FP_P_PRODUCT_OPEN_DAY")>= 0||sql.indexOf("a_fp_p_product_open_day")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_SA_PRD_VALUATION")>= 0||sql.indexOf("a_sa_prd_valuation")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_SA_PRD_INFO")>= 0||sql.indexOf("a_sa_prd_info")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("CIHDQUOTE")>= 0||sql.indexOf("cihdquote")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("IBBONDINDEX")>= 0||sql.indexOf("ibbondindex")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("INTERESTRATE")>= 0||sql.indexOf("interestrate")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FP_R_PRODUCT_ESTABLISH_SUM")>= 0||sql.indexOf("i_fp_r_product_establish_sum")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FP_R_PRODUCT_ESTABLISH_SUM_COPY")>= 0||sql.indexOf("i_fp_r_product_establish_sum_copy")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VF_REPORTDATA_CWZH")>= 0||sql.indexOf("i_fv_vf_reportdata_cwzh")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VF_REPORTDATA_CWZH_COPY")>= 0||sql.indexOf("i_fv_vf_reportdata_cwzh_copy")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FP_R_PRODUCT_BALANCE")>= 0||sql.indexOf("i_fp_r_product_balance")>= 0){
					removePlanParameterHandler(sql, param);
				}
				return updateResult;
			}
		}catch (Exception e){
			throw e;
		}finally {
			releaseConnection();
		}
	}

	/** 执行更新查询，返回更新行数。 */
	@Override
	public UpdateResult updateNoLog(String sql, Object... params) throws Exception {
		Object param = normalize(params);

		if (log.isDebugEnabled()) {
			log.debug("更新查询: " + sql);
			logParams(param);
		}

		try {
			// 如果有自增ID，先预先生成自增ID
			ParameterHandler ph = planParameterHandler(sql, param);
			FieldGetterAutoId fieldGetterAutoId = ph.getFieldGetterAutoId();
			if (fieldGetterAutoId != null) {
				fieldGetterAutoId.prepare();
			}

			Connection db = getConnection();

			try (PreparedStatement ps = db.prepareStatement(ph.getSql(param))) {
				ph.setParametersNoLog(ps, param);
				int effect = ps.executeUpdate();

				UpdateResult updateResult = ph.getUpdateResult();
				if (updateResult == null) {
					updateResult = new UpdateResult();
				}

				//删除缓存
				updateResult.setEffect(effect);
				if(sql.indexOf("SYS_PARAM")>= 0||sql.indexOf("sys_param")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VN_BM_BAL")>= 0||sql.indexOf("i_fv_vn_bm_bal")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VN_BM_BAL_COPY")>= 0||sql.indexOf("i_fv_vn_bm_bal_copy")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VF_REPORT_VAL")>= 0||sql.indexOf("I_FV_VF_REPORT_VAL")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VF_REPORT_VAL_COPY")>= 0||sql.indexOf("i_fv_vf_report_val_copy")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VN_FINANCIAL_VAL")>= 0||sql.indexOf("i_fv_vn_financial_val")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VN_FINANCIAL_VAL_COPY")>= 0||sql.indexOf("i_fv_vn_financial_val_copy")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VN_REPORTDATA_CWZH")>= 0||sql.indexOf("i_fv_vn_reportdata_cwzh")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VN_REPORTDATA_CWZH_COPY")>= 0||sql.indexOf("i_fv_vn_reportdata_cwzh_copy")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_FP_P_PRODUCT")>= 0||sql.indexOf("a_fp_p_product")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_FP_P_PARENT_PRODUCT_DAY_NETVALUE")>= 0||sql.indexOf("a_fp_p_parent_product_day_netvalue")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FP_R_PRODUCT_CONFIRM_SUM")>= 0||sql.indexOf("i_fp_r_product_confirm_sum")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FP_R_PRODUCT_CONFIRM_SUM_COPY")>= 0||sql.indexOf("i_fp_r_product_confirm_sum_copy")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FP_R_PRODUCT_IPO_SUM")>= 0||sql.indexOf("i_fp_r_product_ipo_sum")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FP_R_PRODUCT_IPO_SUM_COPY")>= 0||sql.indexOf("i_fp_r_product_ipo_sum_copy")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_FP_P_PRODUCT_DAY_NETVALUE")>= 0||sql.indexOf("a_fp_p_product_day_netvalue")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_FP_P_PRODUCT_CAPITAL_ACCOUNT")>= 0||sql.indexOf("a_fp_p_product_capital_account")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_TA_ACCOUNTDEPOSIT")>= 0||sql.indexOf("a_ta_accountdeposit")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_TA_TTPLANINFO")>= 0||sql.indexOf("i_ta_ttplaninfo")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_TA_TTPLANINFO_COPY")>= 0||sql.indexOf("i_ta_ttplaninfo_copy")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_FP_P_PARENT_PRODUCT")>= 0||sql.indexOf("a_fp_p_parent_product")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_FP_P_PRODUCT_LIMIT")>= 0||sql.indexOf("a_fp_p_product_limit")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_FP_P_PRODUCT_OPEN_CYCLE")>= 0||sql.indexOf("a_fp_p_product_open_cycle")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_FP_P_PRODUCT_OPEN_DAY")>= 0||sql.indexOf("a_fp_p_product_open_day")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_SA_PRD_VALUATION")>= 0||sql.indexOf("a_sa_prd_valuation")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("A_SA_PRD_INFO")>= 0||sql.indexOf("a_sa_prd_info")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("CIHDQUOTE")>= 0||sql.indexOf("cihdquote")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("IBBONDINDEX")>= 0||sql.indexOf("ibbondindex")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("INTERESTRATE")>= 0||sql.indexOf("interestrate")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FP_R_PRODUCT_ESTABLISH_SUM")>= 0||sql.indexOf("i_fp_r_product_establish_sum")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FP_R_PRODUCT_ESTABLISH_SUM_COPY")>= 0||sql.indexOf("i_fp_r_product_establish_sum_copy")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VF_REPORTDATA_CWZH")>= 0||sql.indexOf("i_fv_vf_reportdata_cwzh")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FV_VF_REPORTDATA_CWZH_COPY")>= 0||sql.indexOf("i_fv_vf_reportdata_cwzh_copy")>= 0){
					removePlanParameterHandler(sql, param);
				}
				if(sql.indexOf("I_FP_R_PRODUCT_BALANCE")>= 0||sql.indexOf("i_fp_r_product_balance")>= 0){
					removePlanParameterHandler(sql, param);
				}
				return updateResult;
			}
		} finally {
			releaseConnection();
		}
	}
	/**
	 * 开始一个批量更新操作。 1. 用完后，务必关闭BatchedXsql，请使用try-with-resouce的方式编写 2.
	 * 批量操作中的每次操作的参数形式必须完全一致
	 */
	public BatchedXsql batch(String sql) throws Exception {
		log.debug("批量更新: {}", sql);

		return new BatchedXsql(this, sql);
	}

	@Override
	public <T> CursoredXsql<T> iterate(Class<T> clazz, String sql, Object... params) throws Exception {
		Object param = normalize(params);

		if (log.isDebugEnabled()) {
			log.debug("多行查询: " + sql);
			logParams(param);
		}

		return new CursoredXsql<T>(this, clazz, sql, param);
	}

	// =================================

	/** 创建(规划)参数处理器 */
	public ParameterHandler planParameterHandler(String sql, Object params) throws Exception {
		return planer1.plan(sql, params);
	/* 修复缓存
		String key = (params == null ? "() -> " : "(" + params.getClass().getName() + ") -> ") + sql;
		ParameterHandler ph = cache1.get(key);
		if (ph == null) {
			ph = planer1.plan(sql, params);
			cache1.put(key, ph);
		}

		return ph;*/
	}

	/** 删除缓存数据 参数处理器 */
	protected void removePlanParameterHandler(String sql, Object params) throws Exception {
		String key = (params == null ? "() -> " : "(" + params.getClass().getName() + ") -> ") + sql;
		cache1.remove(key);
	}

	/** 创建(规划)结果处理器 */
	protected <T> ResultHandler<T> planResultHandler(Class<T> clazz, String sql, PreparedStatement ps)
			throws Exception {
		return planner2.plan(clazz, ps.getMetaData());

		/*去掉缓存
		String key = clazz.getName() + " = " + sql;
		@SuppressWarnings("unchecked")
		ResultHandler<T> handler = (ResultHandler<T>) cache2.get(key);
		//log.info("是否有系统参数缓存?", key.contains("sys_param"));
		if (handler == null || key.contains("sys_param") || key.contains("SYS_PARAM")) {
			//log.info("删除系统参数缓存!!!!");
			handler = planner2.plan(clazz, ps.getMetaData());
			cache2.put(key, handler);
		}

		return handler;*/
	}

	/** 删除缓存 结果处理器 */
	protected <T> void removePlanResultHandler(Class<T> clazz, String sql, PreparedStatement ps)
			throws Exception {
		String key = clazz.getName() + " = " + sql;
		cache2.remove(key);

	}

	// ---------------------------

	protected void logParams(Object params) {
		if (params == null) {
			log.debug("查询参数: []");
			return;
		}

		if (params.getClass().equals(Object[].class)) {
			log.debug("查询参数: " + XsqlUtils.toString((Object[]) params));
			return;
		}

		log.debug("查询参数: " + JSON.toJSONString(params));
	}

	/** 将输入参数标准化：1. 直接参数，2. Map传参，3. JavaBean传参 */
	Object normalize(Object... params) {
		if (params == null) { // 如果为null，没有参数
			return new Object[0];
		}
		if (params.length == 0 || params.length > 1) { // 如果参数数量超过1个，一定是以直接参数给出的形式
			return params;
		}

		// 只有1个参数，
		Object o = params[0];
		if (o == null) {
			return params;
		}

		// 根据参数类型来猜测
		Class<?> c = o.getClass();
		if (Map.class.isAssignableFrom(c)) { // Map的派生类，认为是Map传参
			return o;
		}
		if (c.getClassLoader() == null || Enum.class.isAssignableFrom(c)) { // Java本身的类型，或枚举，认为是直接传参
			return params;
		}

		// 其他都认为是JavaBean传参
		return o;
	}

	// ================================================================================

	private static class Context {
		DataSource source; // 当前数据源
		int sharding; // 分区编号
		Connection connection = null; // 数据库连接
		int depth = 0; // 事务嵌套深度
	}

	@Override
	public ResultSetMetaData getMetaData(String sql, Object... params) throws Exception {

		Object param = normalize(params);

		if (log.isDebugEnabled()) {
			log.debug("多行查询: " + sql);
			logParams(param);
		}

		try {
			Connection db = getConnection();

			ParameterHandler ph = planParameterHandler(sql, param);
			try (PreparedStatement ps = db.prepareStatement(ph.getSql(param))) {
				ph.setParameters(ps, param);
				try (ResultSet rs = ps.executeQuery()) {
					return rs.getMetaData();
				}
			}
		} finally {
			releaseConnection();
		}
	}

}
