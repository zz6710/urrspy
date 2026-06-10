package com.kayak.report.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.report.dao.ReportDataLockRecordDao;
import com.kayak.report.model.ReportDataLockRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@APIDefine(desc = "报送数据锁表管理记录", model = ReportDataLockRecord.class)
public class ReportDataLockRecordService {
	private static Logger logger = LogManager.getLogger(ReportDataLockRecordService.class);

	@Autowired
	private ReportDataLockRecordDao reportDataLockRecordDao;

	@API(desc = "查询报送数据锁表管理记录信息", auth = APIAuth.YES)
	public SqlResult<ReportDataLockRecord> findReportDataLockRecordInfo(SqlParam<ReportDataLockRecord> params) throws Exception {
		return reportDataLockRecordDao.findReportDataLockRecordInfo(params);
	}

	@API(desc = "解除报送数据锁定", auth = APIAuth.YES)
	public String releaseReportData(SqlParam<ReportDataLockRecord> params) throws Exception {
		params.getModel().setLockStatus("02");
		String username = String.valueOf(SysUtil.getSysUserParams().get("loginname"));
		String return_message = reportDataLockRecordDao.checkLowerGradeValidate(params);
		if (return_message.length() > 0) {
			return RequestSupport.updateReturnJson(false,"解锁："+params.getModel().getTableName()+params.getModel().getReportDate() + "日报表失败，需先解锁：" + return_message + "！", null).toString();
		}
		reportDataLockRecordDao.operateReportData(params, username);
		return RequestSupport.updateReturnJson(true, "报表" + params.getModel().getTableName()+params.getModel().getReportDate() + "日解锁成功！", null).toString();
	}

	@API(desc = "锁定报送数据", auth = APIAuth.YES)
	public String lockReportData(SqlParam<ReportDataLockRecord> params) throws Exception {
		params.getModel().setLockStatus("01");
		String username = String.valueOf(SysUtil.getSysUserParams().get("loginname"));
		String return_message = reportDataLockRecordDao.checkUpperGradeValidate(params);
		if (return_message.length() > 0) {
			return RequestSupport.updateReturnJson(false,  "锁定："+params.getModel().getTableName()+params.getModel().getReportDate() + "日报表失败，需先锁定：" + return_message + "！", null).toString();
		}
		reportDataLockRecordDao.operateReportData(params, username);
		logger.info("reportDate:"+params.getModel().getReportDate()+"; reportTable:"+params.getModel().getReportTable()+"; tableName:"+params.getModel().getTableName()+";paramsModel:"+params.getModel().toString());
		if(StringUtils.isNotEmpty(params.getModel().getReportDate())
		   && ("dwd_sum_buttom_asset".equals(params.getModel().getReportTable())||"dws_prod_ttrd_bef_g06a2".equals(params.getModel().getReportTable()))){
          //锁定 G06穿透前（调整后） 和 底层估值明细（调整后） 时，需要触发 新增业务报表app_prod_bond_property_info数据  及 数据清理（若有对应date的历史数据）
		reportDataLockRecordDao.generateDataAndTaskRecord(params.getModel().getReportTable(),params.getModel().getReportDate());

		}
		return RequestSupport.updateReturnJson(true, "报表" + params.getModel().getReportDate() + "日锁定成功！", null).toString();
	}

	@API(desc = "批量操作报送数据锁定", auth = APIAuth.YES)
	public String batchLockReportData(SqlParam<ReportDataLockRecord> params) throws Exception {
		String username = String.valueOf(SysUtil.getSysUserParams().get("loginname"));
		List<String> tableList = Arrays.asList(params.getModel().getReportTable().split(","));
		params.getModel().setReportDate(DateUtil.getMonthEndDate(params.getModel().getReportDate()));
		Set<String> msg_set = new HashSet<>();
		/**检查是否满足锁表or解锁条件*/
		for (String reportTable : tableList) {
			params.getModel().setReportTable(reportTable);
			if ("01".equals(params.getModel().getLockStatus())) {//锁定表，检查对应依赖中间表是否已锁
				String lock_message = reportDataLockRecordDao.checkUpperGradeValidate(params);
				if (lock_message.length() > 0) {
					return RequestSupport.updateReturnJson(false, "批量锁定：" + params.getModel().getTableName()+params.getModel().getReportDate() + "日报表失败，需先锁定：" + lock_message + "！", null).toString();
				}
			} else {//解锁表，检查中间表下游报表是否未锁
				String unlock_message = reportDataLockRecordDao.checkLowerGradeValidate(params);
				if (unlock_message.length() > 0) {
					return RequestSupport.updateReturnJson(false, "批量解锁：" + params.getModel().getTableName()+params.getModel().getReportDate() + "日报表失败，需先解锁：" + unlock_message + "！", null).toString();
				}
			}
		}

		/** 执行锁表任务 */
		for (String reportTable : tableList) {
			params.getModel().setReportTable(reportTable);
			reportDataLockRecordDao.operateReportData(params, username);
			if(StringUtils.isNotEmpty(params.getModel().getReportDate())
					&&("dwd_sum_buttom_asset".equals(params.getModel().getReportTable())
					   ||"dws_prod_ttrd_bef_g06a2".equals(params.getModel().getReportTable()))){
				reportDataLockRecordDao.generateDataAndTaskRecord(params.getModel().getReportTable(),params.getModel().getReportDate());
			}
		}
		if ("01".equals(params.getModel().getLockStatus())) {
			return RequestSupport.updateReturnJson(true, "批量锁定" + params.getModel().getReportDate() + "日报表成功！", null).toString();
		} else {
			return RequestSupport.updateReturnJson(true, "批量解锁" + params.getModel().getReportDate() + "日报表成功！", null).toString();
		}

	}


}
