package com.kayak.rpt.rhzj.service;

import com.kayak.core.system.RequestSupport;
import com.kayak.subject.model.DwdAsstBondComprat;
import com.kayak.subject.model.PubReq;
import com.kayak.subject.service.RptBusinessBaseTaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.rhzj.dao.ReportOverseasInvInfoDao;
import com.kayak.rpt.rhzj.model.ReportOverseasInvInfo;

@Service
@APIDefine(desc = "境外投资情况明细表服务", model = ReportOverseasInvInfo.class)
public class ReportOverseasInvInfoService extends RptBusinessBaseTaskService {

	@Autowired
	private ReportOverseasInvInfoDao reportOverseasInvInfoDao;

	@API(desc = "查询境外投资情况明细表信息", auth = APIAuth.YES)
	public SqlResult<ReportOverseasInvInfo> findReportOverseasInvInfos(SqlParam<ReportOverseasInvInfo> params) throws Exception {
		params.setMakeSql(true);
		return reportOverseasInvInfoDao.findReportOverseasInvInfos(params);
	}

	@API(desc = "添加境外投资情况明细表", params = "id,report_date,prod_cd,prod_reg_enc,hold_type,f_asst_cd,asset_third_type,f_asst_nm,f_amount,org_classific,itm_cd,itm_nm,d_amount,new_classific,f_inv_tm,asst_type,asst_amount,asst_zon,mang_zon,depr_rdy_amt,depr_amt,fx_type,bd_rmai_type,rmai_day,risk_envn,risk_pj_amt", auth = APIAuth.NO)
	public int addReportOverseasInvInfo(SqlParam<ReportOverseasInvInfo> params) throws Exception {
		return reportOverseasInvInfoDao.addReportOverseasInvInfo(params).getEffect();
	}

	@API(desc = "修改境外投资情况明细表", params = "id,report_date,prod_cd,prod_reg_enc,hold_type,f_asst_cd,asset_third_type,f_asst_nm,f_amount,org_classific,itm_cd,itm_nm,d_amount,new_classific,f_inv_tm,asst_type,asst_amount,asst_zon,mang_zon,depr_rdy_amt,depr_amt,fx_type,bd_rmai_type,rmai_day,risk_envn,risk_pj_amt", auth = APIAuth.YES)
	public int updateReportOverseasInvInfo(SqlParam<ReportOverseasInvInfo> params) throws Exception {
		return reportOverseasInvInfoDao.updateReportOverseasInvInfo(params).getEffect();
	}

	@API(desc = "删除境外投资情况明细表", params = "id,report_date,prod_cd,prod_reg_enc,hold_type,f_asst_cd,asset_third_type,f_asst_nm,f_amount,org_classific,itm_cd,itm_nm,d_amount,new_classific,f_inv_tm,asst_type,asst_amount,asst_zon,mang_zon,depr_rdy_amt,depr_amt,fx_type,bd_rmai_type,rmai_day,risk_envn,risk_pj_amt", auth = APIAuth.YES)
	public int deleteReportOverseasInvInfo(SqlParam<ReportOverseasInvInfo> params) throws Exception {
		return reportOverseasInvInfoDao.deleteReportOverseasInvInfo(params).getEffect();
	}

	@API(desc = "重新生成报表", params = "id,report_date,prod_cd,prod_reg_enc,hold_type,f_asst_cd,asset_third_type,f_asst_nm,f_amount,org_classific,itm_cd,itm_nm,d_amount,new_classific,f_inv_tm,asst_type,asst_amount,asst_zon,mang_zon,depr_rdy_amt,depr_amt,fx_type,bd_rmai_type,rmai_day,risk_envn,risk_pj_amt", auth = APIAuth.YES)
	public String buildReportOverseasInvInfo(SqlParam<ReportOverseasInvInfo> params) throws Exception {
		try {
			String reportDate = params.getModel().getReportDate();
			String taskIds = "R120";

			if (StringUtils.isNotEmpty(taskIds)) {
				String[] taskId = taskIds.split(",");

				for (String task : taskId) {
					PubReq request = new PubReq();
					request.setTaskId(task);
					request.setTaskDate(reportDate);
					super.beforeClear(request);
					super.dataModeExConvert(request);
				}
			}

			return RequestSupport.updateReturnJson(true,"重新生成报表成功",null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,"重新生成报表失败！" + e.getMessage(),null).toString();
		}
	}

}
