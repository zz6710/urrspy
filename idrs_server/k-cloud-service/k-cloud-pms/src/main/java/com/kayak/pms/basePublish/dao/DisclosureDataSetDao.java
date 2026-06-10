package com.kayak.pms.basePublish.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.DateUtil;
import com.kayak.pms.basePublish.model.DisclosureDataSet;
import com.kayak.pms.disclosureControl.dao.SaveLogDao;
import com.kayak.pms.disclosureControl.model.ScheduleNotice;
import com.kayak.pms.disclosureControl.util.SynDataUtil;
import com.kayak.pms.global.constants.DisclosureType;
import com.kayak.pms.global.constants.MoneyFormat;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@Repository
public class DisclosureDataSetDao extends ComnDao {

	public SqlResult<DisclosureDataSet> findDisclosureDataSets(SqlParam<DisclosureDataSet> params) throws Exception {
		return super.findRows("SELECT id,disclosure_type,disclosure_son_type,select_columns,select_sqls,delete_sqls,insert_sqls,update_sqls,status,conditions,exec_order,remark,crt_user_name,crt_date,crt_time,crt_user_id,upd_user_id,upd_user_name,upd_date,upd_time FROM idb_disclosure_data_set", params);
	}

	public UpdateResult addDisclosureDataSet(SqlParam<DisclosureDataSet> params) throws Exception {
		return super.update("INSERT INTO idb_disclosure_data_set(id,disclosure_type,disclosure_son_type," +
						" select_sqls,delete_sqls,insert_sqls,update_sqls,status,conditions," +
						" exec_order,remark,crt_user_name,crt_date,crt_time,crt_user_id," +
						" upd_user_id,upd_user_name,upd_date,upd_time,select_columns) " +
						" VALUES($AUTOIDS{id},$S{disclosureType},$S{disclosureSonType}," +
						" $S{selectSqls},$S{deleteSqls},$S{insertSqls},$S{updateSqls},$S{status},$S{conditions}," +
						" $S{execOrder},$S{remark},$S{crtUserName},$S{crtDate},$S{crtTime},$S{crtUserId}," +
						" $S{updUserId},$S{updUserName},$S{updDate},$S{updTime},$S{selectColumns})",
				params.getModel());
	}

	public UpdateResult updateDisclosureDataSet(SqlParam<DisclosureDataSet> params) throws Exception {
		return super.update("UPDATE idb_disclosure_data_set " +
						" SET disclosure_type=$S{disclosureType} ," +
						" disclosure_son_type=$S{disclosureSonType} ," +
						" select_columns=$S{selectColumns}," +
						" select_sqls=$S{selectSqls}  ," +
						"   delete_sqls=$S{deleteSqls} ," +
						"   insert_sqls=$S{insertSqls} ," +
						"   update_sqls=$S{updateSqls} ," +
						"   status=$S{status} ," +
						"   conditions=$S{conditions} ," +
						"   exec_order=$S{execOrder} ," +
						"   remark=$S{remark} ," +
						"   upd_user_id=$S{updUserId} ," +
						"   upd_user_name=$S{updUserName} ," +
						"   upd_date=$S{updDate} ," +
						"   upd_time=$S{updTime}  " +
						" WHERE  id=$S{id} ",
				params.getModel());
	}

	public UpdateResult deleteDisclosureDataSet(SqlParam<DisclosureDataSet> params) throws Exception {
		return super.update("DELETE FROM idb_disclosure_data_set WHERE  id=$S{id} ",
				params.getModel());
	}

	/**
	 * 功能：修改数据集视状态
	 * 作者：rennannan
	 * 日期：20210528
	 *
	 * @param param
	 * @return
	 */
	public int updateDisclosureDataStatus(DisclosureDataSet param) throws Exception {
		String sql = " update idb_disclosure_data_set" +
				" set status=$S{status}" +
				" where id= $S{id}";
		return super.update(sql, param).getEffect();
	}

	@Autowired
	private SaveLogDao saveLogDao;

	/**
	 * @param param [定期报告：prod_code,disclosure_type,disclosure_son_type,report_date,regular_start_date,regular_end_date]
	 * @throws Exception
	 */
	public String dataIntegrate(Map<String, Object> param) throws Exception {
		if (param.get("value_sql")!=null) {
			if (Strings.isNotBlank((String) param.get("value_sql"))){

				/**获取执行sql、执行数据库数据源*/
				String sql = param.get("value_sql")!=null?param.get("value_sql").toString():"";
				int dataSource = param.get("data_source")!=null?Integer.parseInt(param.get("data_source").toString()):0;
				String keyName =param.get("column_key")!=null?param.get("column_key").toString():"";
				String parameterName =param.get("sql_parameter")!=null?param.get("sql_parameter").toString():"";
				String moneyFormat =param.get("money_format")!=null?param.get("money_format").toString():"";
				int dataLength =param.get("data_length")!=null?Integer.parseInt(param.get("data_length").toString()):0;
				String dictName =param.get("dict")!=null?param.get("dict").toString():"";
				String columnLabel =param.get("column_label")!=null?param.get("column_label").toString():"";
				StringBuffer findDictSql = new StringBuffer();

				try {
					/**执行sql并获取结果集中优先与设置的sql参数名匹配的字段值其次为与key匹配的字段值，忽略大小写*/
					SqlRow resultsUObj = super.findRow(SynDataUtil.sToS(sql), dataSource, param);
					//优先匹配参数名
					String resultV = resultsUObj != null?resultsUObj.getString(parameterName.toUpperCase()):"";
					resultV = Strings.isBlank(resultV)? resultsUObj != null?resultsUObj.getString(parameterName.toLowerCase()):"":resultV;
					//匹配key
					if (Strings.isBlank(resultV)){
						resultV = resultsUObj != null?resultsUObj.getString(keyName.toUpperCase()):"";
						resultV = Strings.isBlank(resultV)? resultsUObj != null?resultsUObj.getString(keyName.toLowerCase()):"":resultV;
					}


					/**字典值，码值转换*/
					String resultI = "";
					if (Strings.isNotBlank(dictName)){
						SqlRow resultsIObj = super.findRow(findDictSql.append(" SELECT i.itemval FROM sys_dict_item i WHERE 1=1 AND i.dict='").append(dictName).append("' AND i.itemkey='").append(resultV).append("'").toString(),DataSourceProperty.PUB, null);
						resultI = resultsIObj !=null ? resultsIObj.getString("itemval") : resultV;
						findDictSql.delete(0,findDictSql.length());
					}else {
						resultI = resultV;
					}

					/**金额格式 数据库函数capMoney执行转换*/
					String resultF = "";
					if (moneyFormat.equals(MoneyFormat.big.getItemKey())){
						SqlRow resultsFObj =super.findRow(findDictSql.append(" SELECT capMoney(").append("".equals(resultI)?0:resultI).append(") capMoney from DUAL").toString(),DataSourceProperty.PUB,null);
						resultF = resultsFObj !=null ? resultsFObj.getString("capMoney") : resultI;
						findDictSql.delete(0,findDictSql.length());
					}else if (moneyFormat.equals(MoneyFormat.small.getItemKey())) {
						SqlRow resultsFObj = super.findRow(findDictSql.append(" SELECT FORMAT(").append("".equals(resultI) ? 0 : resultI).append(",2) capMoney from DUAL").toString(), DataSourceProperty.PUB, null);
						resultF = resultsFObj != null ? resultsFObj.getString("capMoney") : resultI;
						findDictSql.delete(0, findDictSql.length());
					}else{
						resultF = resultI;
					}

					/**默认长度*/
					 String resultL = dataLength>0 ? (resultF.length()>dataLength? resultF.substring(0,dataLength) :resultF) : resultF;

					 return resultL;

				} catch (Exception e) {
					e.printStackTrace();
					log.error("字段:" + columnLabel + "[" + keyName + "]取值sql有误");
				}
			}
		}
		return  null;
	}

	public List<SqlRow> findT8DisclosureAssetBottom (Map<String, Object> param) throws Exception{
		return super.findRows("select 1 from idb_disclosure_asset_bottom where prod_code = '"+param.get("prod_code")+"' and data_date = '"+param.get("report_date")+"' ");

	}

	public int updateDisclosureAssetBottomChange(String totalAmount,String prodCode,String dataData) throws Exception {
		String sql = " UPDATE idb_disclosure_asset_bottom_change set asset_ratio = convert((invest_amount/"+totalAmount+")*100,decimal(15,2)) where prod_code = '"+prodCode+"' and data_date = '"+dataData+"' ";
		return super.update(sql).getEffect();
	}

	/**
	 * 更新指定字段
	 * @param param [定期报告：prod_code,disclosure_type,disclosure_son_type,report_date,regular_start_date,regular_end_date,select_columns]
	 * @throws Exception
	 */
	public void dataIntegrateColumns(Map<String, Object> param) throws Exception {
		String sql = "select id,disclosure_type,disclosure_son_type,select_sqls,select_columns," +
				"delete_sqls,insert_sqls,update_sqls,conditions,status,exec_type,crt_user_id,crt_date,crt_time,remark from idb_disclosure_data_set " +
				" where disclosure_type = $S{disclosure_type} ";
		if (!SynDataUtil.objToStr(param.get("disclosure_son_type")).isEmpty()) {
			sql = sql + " and disclosure_son_type = $S{disclosure_son_type} ";
		}

		if (!SynDataUtil.objToStr(param.get("select_columns")).isEmpty()) {
			sql = sql + " and select_columns = $S{select_columns} ";
		}

		sql = sql + " and status = 'N' order by exec_order+0";
		try {
			List<SqlRow> results = super.findRows(SynDataUtil.sToS(sql), param);
			for (SqlRow sqlRow : results) {
				dataExcute(sqlRow,param,"4");//datatype-3 数据集视转换
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据查询的结果，判断条件，执行语句
	 * @param sqlRow sqlRow.get("conditions");//执行条件
	 *               sqlRow.getString("select_sqls");//查询sql
	 * 				 sqlRow.getString("select_columns");//sql中字段
	 * 				 sqlRow.get("delete_sqls");//目标删除语句
	 * 				 sqlRow.get("insert_sqls");//目标插入语句
	 * 				 sqlRow.get("update_sqls");//目标更新语句
	 * 				 sqlRow.get("exec_type");//执行类型
	 * @param param
	 * @param datatype
	 * @throws Exception
	 */
	public void dataExcute(SqlRow sqlRow,Map<String, Object> param,String datatype) throws Exception {
		StringBuffer tbLogs = new StringBuffer();
		String startDate = DateUtil.getNowDate();
		String startTime = DateUtil.getNowTime();
		try {
			//1、判断参数是否满足执行条件
			String conditions = SynDataUtil.objToStr(sqlRow.get("conditions"));
			boolean isOk = SynDataUtil.compareUtil(conditions, param);
			if (conditions.isEmpty()) {
				tbLogs.append("条件:为空; 判断结果:").append(isOk).append("; /n");
			} else {
				tbLogs.append("条件:").append(conditions).append("; 判断结果:").append(isOk).append("; /n");
			}
			if (isOk) {//满足执行条件之后
				String select_sqls = SynDataUtil.sToS(sqlRow.getString("select_sqls"));//查询sql
				String select_columns = sqlRow.getString("select_columns");//sql中字段
				String delete_sqls = SynDataUtil.sToS(SynDataUtil.objToStr(sqlRow.get("delete_sqls")));//目标删除语句
				String insert_sqls = SynDataUtil.sToS(SynDataUtil.objToStr(sqlRow.get("insert_sqls")));//目标插入语句
				String update_sqls = SynDataUtil.sToS(SynDataUtil.objToStr(sqlRow.get("update_sqls")));//目标更新语句

				//2、执行查询的sql
				tbLogs.append("QUERY:").append(select_sqls).append("; \n");
				List<SqlRow> selectList = super.findRows(select_sqls, param);
				if (!delete_sqls.isEmpty()) {//执行删除语句
					//非份额分类 selectList.size() == 0  不做操作
					if(selectList.size() != 0) {
						Map<String, Object> resultMap2 = SynDataUtil.rowMap(selectList.get(0), select_columns);
						resultMap2.putAll(param);
						super.update(delete_sqls, resultMap2);
						tbLogs.append("DELETE:").append(delete_sqls).append("; \n");
					}
				}
				for (SqlRow sqlRow1 : selectList) {
					Map<String, Object> resultMap = SynDataUtil.rowMap(sqlRow1, select_columns);
					if (SynDataUtil.objToStr(sqlRow.get("exec_type")).compareTo("0") == 0) {//平铺执行
						resultMap.putAll(param);
						if (!insert_sqls.isEmpty()) {//执行插入语句
							super.update(insert_sqls, resultMap);
							tbLogs.append("INSERT:").append(insert_sqls).append("; \n");
						}
						if (delete_sqls.isEmpty() & insert_sqls.isEmpty() & !update_sqls.isEmpty()) {
							super.update(update_sqls, resultMap);
							tbLogs.append("UPDATE:").append(update_sqls).append("; \n");
						}
					} else if (SynDataUtil.objToStr(sqlRow.get("exec_type")).compareTo("1") == 0) {//字段循环执行，针对一个字段执行一次语句的情况

						String[] columns = select_columns.split(",");
						for (String column : columns) {
							Map<String, Object> reMap = new HashMap<String, Object>();
							reMap.put("column_value", sqlRow1.get(column));
							reMap.put("column_key", column);
							reMap.putAll(param);
							if (!update_sqls.isEmpty()) {
								super.update(update_sqls, reMap);
								tbLogs.append("UPDATE:").append(update_sqls).append("; \n");
							}
						}
					}
				}
			}
			String endDate = DateUtil.getNowDate();
			String endTime = DateUtil.getNowTime();
			Map<String, Object> logParam = new HashMap<>();
			logParam.put("logs", tbLogs);
			logParam.put("datatype", datatype);//集视
			logParam.put("start_date", startDate);
			logParam.put("start_time", startTime);
			logParam.put("end_date", endDate);
			logParam.put("end_time", endTime);
			logParam.put("remark", "");
			saveLogDao.saveLog(logParam);

		} catch (Exception e) {
			log.error("data set 更新异常{}",e);
		}
	}

	/**
	 * 查询公告表数据
	 * @throws Exception
	 */
	public List<ScheduleNotice> findDisclosureNoticeByCrtDate(String minDate, String maxDate) throws Exception {
		String sql = " select id,t8_disclosure_rule_id,prod_code,disclosure_type,disclosure_son_type,prod_base_date,t8_prod_info_id  from idb_disclosure_notice where '"+minDate+"' <= crt_date  and crt_date <= '"+maxDate+"' ";
		return findRows(ScheduleNotice.class,sql,0,null);
	}
}
