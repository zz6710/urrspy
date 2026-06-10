package com.kayak.subject.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.rpt.rhzg.listener.ExcelImportListener;
import com.kayak.rpt.rhzg.service.ExcelImportService;
import com.kayak.subject.dao.SimsValuationDataBInfoDao;
import com.kayak.subject.model.PubReq;
import com.kayak.subject.model.SimsValuationDataBInfo;
import com.kayak.subject.model.SimsValuationDataBInfoExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@APIDefine(desc = "SIMS底层估值明细表（调整后）", model = SimsValuationDataBInfo.class)
public class SimsValuationDataBInfoService implements ExcelImportService<SimsValuationDataBInfoExcel> {

	@Autowired
	private SimsValuationDataBInfoDao simsValuationDataBInfoDao;

	@Autowired
	private RptBusinessBaseTaskService rptBusinessBaseTaskService;

	@Autowired
	private ComnDao comnDao;

	@API(desc = "查询SIMS底层估值明细表（调整后）", auth = APIAuth.YES)
	public SqlResult<SimsValuationDataBInfo> findSimsValuationDataBInfos(SqlParam<SimsValuationDataBInfo> params) throws Exception {
		return simsValuationDataBInfoDao.findSimsValuationDataBInfos(params);
	}


	public String importSimsValuationDataBInfo(MultipartFile file, Map<String, Object> params) throws Exception {
		long startTime = System.currentTimeMillis();
		log.info("导入SIMS底层估值明细表（调整后）【{}】开始",file.getOriginalFilename());
		final SimsValuationDataBInfoService simsValuationDataBInfoService = this;
		ExcelImportListener<SimsValuationDataBInfoExcel> excelImportListener = new ExcelImportListener<SimsValuationDataBInfoExcel>(params) {
			@Override
			protected ExcelImportService<SimsValuationDataBInfoExcel> getImportService() { return simsValuationDataBInfoService; }
		};

		String inputDate = (String) params.get("inputDate");
		SimsValuationDataBInfo param = new SimsValuationDataBInfo();
		param.setInputDate(inputDate);
		//先删后插
		simsValuationDataBInfoDao.deleteSimsValuationData(param);
		log.info("删除 ods_sims_valuation_data 表数据，日期为：{}", inputDate);

		try {
			EasyExcel.read(file.getInputStream())
					.head(SimsValuationDataBInfoExcel.class)
					.registerReadListener(excelImportListener)
					.sheet()
					.doRead();
		} catch (Exception e) {
			throw new Exception(excelImportListener.getStopMsg());
		}

		log.info("导入SIMS底层估值明细表（调整后）【{}】结束，耗时：{} ms", file.getOriginalFilename(), System.currentTimeMillis() - startTime);
		return excelImportListener.getStopMsg();
	}

	@Override
	public void importFile(List<SimsValuationDataBInfoExcel> list, Map map) throws Exception {
		long startTime = System.currentTimeMillis();

		comnDao.doTrans(() -> {
			PreparedStatement ps = getPreparedStatement(list);
			try {
				String reportDate = (String) map.get("inputDate");
				for (SimsValuationDataBInfoExcel simsValuationDataBInfoExcel : list) {
					resolveSimsValuationDataBInfo(simsValuationDataBInfoExcel);
					simsValuationDataBInfoExcel.setInputDate(reportDate);
					ps.setString(1, simsValuationDataBInfoExcel.getComcode());
					ps.setString(2, simsValuationDataBInfoExcel.getBottomCode());
					ps.setString(3, simsValuationDataBInfoExcel.getAssetType());
					ps.setString(4, simsValuationDataBInfoExcel.getAmount());
					ps.setString(5, simsValuationDataBInfoExcel.getCost());
					ps.setString(6, simsValuationDataBInfoExcel.getCurrency());
					ps.setString(7, simsValuationDataBInfoExcel.getInputDate());
					ps.setString(8, simsValuationDataBInfoExcel.getItemId());
					ps.setString(9, simsValuationDataBInfoExcel.getItemName());
					ps.setString(10, simsValuationDataBInfoExcel.getImportDate());
					ps.setString(11, simsValuationDataBInfoExcel.getIcode());
					ps.setString(12, simsValuationDataBInfoExcel.getAtype());
					ps.setString(13, simsValuationDataBInfoExcel.getMtype());
					ps.setString(14, simsValuationDataBInfoExcel.getAssetCode());
					ps.setString(15, simsValuationDataBInfoExcel.getOrgLevel());
					ps.setString(16, simsValuationDataBInfoExcel.getZzReportType());
					ps.setString(17, simsValuationDataBInfoExcel.getTradePlace());
					ps.setString(18, simsValuationDataBInfoExcel.getIsPublic());
					ps.setString(19, simsValuationDataBInfoExcel.getNetValue());
					ps.setString(20, simsValuationDataBInfoExcel.getDataInsrDt());
					ps.addBatch();
				}
				ps.executeBatch();
				log.info(" ##### 批量入库{}耗时: {} ms", list.size(), System.currentTimeMillis() - startTime);
			} catch (Exception e) {
				log.error("导入 ods_sims_valuation_data 异常!", e);
				throw new Exception(e.getMessage());
			} finally {
				ps.close();
			}
		});
	}

	private PreparedStatement getPreparedStatement(List<SimsValuationDataBInfoExcel> list) throws Exception {
		String batchSql = "insert into ods_sims_valuation_data (comcode,bottom_code,asset_type,amount,cost,currency,input_date,item_id,item_name,import_date,i_code,a_type,m_type,asset_code,org_level,zz_report_type,trade_place,is_public,net_value,data_insr_dt,deal_date) " +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,date_format(CURDATE(),'%Y%m%d')) ";

		if (list == null || list.isEmpty()) {
			throw new Exception("Excl无数据");
		}

		Connection connection = comnDao.getConnection();
		PreparedStatement ps = connection.prepareStatement(batchSql);
		return ps;
	}

	private void resolveSimsValuationDataBInfo(SimsValuationDataBInfoExcel simsValuationDataBInfoExcel) throws Exception {
		simsValuationDataBInfoExcel.setAssetType(CacheUtil.getDictItemKey("sims_asset_type", simsValuationDataBInfoExcel.getAssetType()));
		simsValuationDataBInfoExcel.setTradePlace(CacheUtil.getDictItemKey("trade_market", simsValuationDataBInfoExcel.getTradePlace()));
		simsValuationDataBInfoExcel.setZzReportType(StringUtils.isEmpty(simsValuationDataBInfoExcel.getZzReportType())?null:getOutkey(simsValuationDataBInfoExcel.getZzReportType()));
	}

	@API(desc = "重新生成底层持仓中间表（调整前）", auth = APIAuth.NO)
	public String reloadTask(SqlParam<SimsValuationDataBInfo> params) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("paravalue","1");
		map.put("oldParavalue","0");
		map.put("paraid","90000061001");

		try {// 调用清算M086
			if (rptBusinessBaseTaskService.upTaskStatus(map) > 0) {
				List<SqlRow> sqlStrs = ExeQuery.queryPortSqlByTaskId("M086");
				for (SqlRow sqlStr : sqlStrs) {
					// $LIST列表循环执行
					String sqlStrString = sqlStr.getString("sqlstr");
					String sqlStrString1 = sqlStrString.replaceAll("\\$LIST\\{G06_DT_DT\\}", params.getModel().getInputDate());
					comnDao.update(sqlStrString1,params);
				}
				// 执行完成后更新数据分布式锁
				map.put("paravalue","0");
				map.put("oldParavalue","1");
				rptBusinessBaseTaskService.upTaskStatus(map);
			} else {
				return RequestSupport.updateReturnJson(true, "正在重新生成底层估值明细表（调整前），请稍后重试", null).toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 执行失败后更新数据分布式锁
			map.put("paravalue","0");
			map.put("oldParavalue","1");
			rptBusinessBaseTaskService.upTaskStatus(map);
			return RequestSupport.updateReturnJson(false, "重新生成底层估值明细表（调整前）失败", null).toString();
		}
		return RequestSupport.updateReturnJson(true, "重新生成底层估值明细表（调整前）成功", null).toString();
	}

	@API(desc = "重新生成底层持仓中间表（调整前）查询", auth = APIAuth.NO)
	public String reloadTaskQuery(SqlParam<SimsValuationDataBInfo> params) throws Exception {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("paraid","90000061001");
			List<SqlRow> sqlRowList = rptBusinessBaseTaskService.quTaskStatus(map);
			if (CollectionUtil.isEmpty(sqlRowList) || (CollectionUtil.isNotEmpty(sqlRowList) && "1".equals(sqlRowList.get(0).getString("paravalue")))) {
				return RequestSupport.updateReturnJson(true, "正在重新生成底层估值明细表（调整前），请稍后重试", null).toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false, "", null).toString();
		}
		return RequestSupport.updateReturnJson(true, "", null).toString();
	}

	//外部字典
	private String getOutkey(String val) throws Exception {
		String area = "";
		String querySql = "select out_value from base_ex_map bem where dict ='zz_report_type' and dictname = '"+val+"' " ;
		SqlRow sqlRow = comnDao.findRow(querySql,null);
		return sqlRow.getString("out_value");
	}


	public void createImportLog(String reportDate) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("create_date", DateUtil.getNowDate());
		params.put("summit_user", SysUtil.getSysUserParamValue("sys_user_userid"));
		params.put("create_time", DateUtil.getNowTime());
		params.put("input_dt", reportDate);
		params.put("table_nm", "ods_sims_valuation_data");
		String sql = "insert into G06_mezzanine_remark(create_date,summit_user,create_time,input_dt,table_nm)values($S{create_date},$S{summit_user},$S{create_time},$S{input_dt},$S{table_nm})";
		comnDao.update(sql,params);
	}
}
