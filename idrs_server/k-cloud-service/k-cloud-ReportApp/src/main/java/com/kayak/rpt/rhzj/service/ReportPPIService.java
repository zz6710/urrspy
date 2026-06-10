package com.kayak.rpt.rhzj.service;

import com.kayak.rpt.rhzj.model.ReportPPI;
import com.kayak.rpt.rhzj.util.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.rhzj.dao.ReportPPIDao;

import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "人行资金池及产品信息服务", model = ReportPPI.class)
public class ReportPPIService {

	private static final Logger log = LoggerFactory.getLogger(ReportPPIService.class);

	@Autowired
	private ReportPPIDao reportPPIDao;

	@API(desc = "查询人行资金池及产品信息信息", auth = APIAuth.YES)
	public SqlResult<ReportPPI> findReportPPIs(SqlParam<ReportPPI> params) throws Exception {
		params.setMakeSql(true);
		return reportPPIDao.findReportPPIs(params);
	}

	@API(desc = "添加人行资金池及产品信息", params = "prod_code,orgno,peoplebank_submitcode,prod_variety,prod_name,prod_brand,prod_times,coll_mod,oper_mod,run_mod,prod_type,busi_mod,safe_rate,safe_capit,max_rate,min_rate,subs_bdate,subs_edate,term_flag,redeem_flag,prod_credit_flag,bord_trusti_code,overs_trusti_nation,overs_trusti_name,establish_date,end_date,pbc_assetscode,issu_ccy,return_ccy,income_ccy,invest_object,prod_credit_org,prod_credit_mod,end_date_real,rate_real,income_type,cooperation_mode,grading_flag,entrested_obligation,transfer_flag,orgno_flag,cash_type,cross_border_finan", auth = APIAuth.NO)
	public int addReportPPI(SqlParam<ReportPPI> params) throws Exception {
		return reportPPIDao.addReportPPI(params).getEffect();
	}
	
	@API(desc = "修改人行资金池及产品信息", params = "prod_code,orgno,peoplebank_submitcode,prod_variety,prod_name,prod_brand,prod_times,coll_mod,oper_mod,run_mod,prod_type,busi_mod,safe_rate,safe_capit,max_rate,min_rate,subs_bdate,subs_edate,term_flag,redeem_flag,prod_credit_flag,bord_trusti_code,overs_trusti_nation,overs_trusti_name,establish_date,end_date,pbc_assetscode,issu_ccy,return_ccy,income_ccy,invest_object,prod_credit_org,prod_credit_mod,end_date_real,rate_real,income_type,cooperation_mode,grading_flag,entrested_obligation,transfer_flag,orgno_flag,cash_type,cross_border_finan", auth = APIAuth.NO)
	public int updateReportPPI(SqlParam<ReportPPI> params) throws Exception {
		return reportPPIDao.updateReportPPI(params).getEffect();
	}
	
	@API(desc = "删除人行资金池及产品信息", params = "prod_code,orgno,peoplebank_submitcode,prod_variety,prod_name,prod_brand,prod_times,coll_mod,oper_mod,run_mod,prod_type,busi_mod,safe_rate,safe_capit,max_rate,min_rate,subs_bdate,subs_edate,term_flag,redeem_flag,prod_credit_flag,bord_trusti_code,overs_trusti_nation,overs_trusti_name,establish_date,end_date,pbc_assetscode,issu_ccy,return_ccy,income_ccy,invest_object,prod_credit_org,prod_credit_mod,end_date_real,rate_real,income_type,cooperation_mode,grading_flag,entrested_obligation,transfer_flag,orgno_flag,cash_type,cross_border_finan", auth = APIAuth.NO)
	public int deleteReportPPI(SqlParam<ReportPPI> params) throws Exception {
		return reportPPIDao.deleteReportPPI(params).getEffect();
	}

	public void importReportPPIData(List<ReportPPI> reportPPIS, Map<String, Object> params) {
		try {
			reportPPIDao.deleteReportPPIForReportDate(params);
			for (ReportPPI info : reportPPIS) {
				Map<String, Object> map = MapUtil.toMap(info);
				//获取key
				map.put("prodVariety", StringUtils.isNotBlank(info.getProdVariety())?info.getProdVariety().split("-")[0]: StringUtils.EMPTY);//产品品种
				map.put("operMod", StringUtils.isNotBlank(info.getOperMod())?info.getOperMod().split("-")[0]: StringUtils.EMPTY);
				map.put("runMod",  StringUtils.isNotBlank(info.getRunMod())?info.getRunMod().split("-")[0]: StringUtils.EMPTY);
				map.put("prodType", StringUtils.isNotBlank(info.getProdType())?info.getProdType().split("-")[0]: StringUtils.EMPTY);
				map.put("busiMod", StringUtils.isNotBlank(info.getBusiMod())?info.getBusiMod().split("-")[0]: StringUtils.EMPTY);
				map.put("safeRate", StringUtils.isNotBlank(info.getSafeRate())?info.getSafeRate().split("-")[0]: StringUtils.EMPTY);
				map.put("safeCapit", StringUtils.isNotBlank(info.getSafeCapit())?info.getSafeCapit().split("-")[0]: StringUtils.EMPTY);
				map.put("termFlag", StringUtils.isNotBlank(info.getTermFlag())?info.getTermFlag().split("-")[0]: StringUtils.EMPTY);
				map.put("redeemFlag", StringUtils.isNotBlank(info.getRedeemFlag())?info.getRedeemFlag().split("-")[0]: StringUtils.EMPTY);
				map.put("prodCreditFlag", StringUtils.isNotBlank(info.getProdCreditFlag())?info.getProdCreditFlag().split("-")[0]: StringUtils.EMPTY);
				map.put("investObject", StringUtils.isNotBlank(info.getInvestObject())?info.getInvestObject().split("-")[0]: StringUtils.EMPTY);
				map.put("collMod",StringUtils.isNotBlank(info.getCollMod())?info.getCollMod().split("-")[0]: StringUtils.EMPTY);
				map.put("entrestedObligation", StringUtils.isNotBlank(info.getEntrestedObligation())?info.getEntrestedObligation().split("-")[0]: StringUtils.EMPTY);
				map.put("cooperationMode", StringUtils.isNotBlank(info.getCooperationMode())?info.getCooperationMode().split("-")[0]: StringUtils.EMPTY);
				map.put("gradingFlag", StringUtils.isNotBlank(info.getGradingFlag())?info.getGradingFlag().split("-")[0]: StringUtils.EMPTY);
				map.put("transferFlag", StringUtils.isNotBlank(info.getTransferFlag())?info.getTransferFlag().split("-")[0]: StringUtils.EMPTY);
				map.put("orgnoFlag", StringUtils.isNotBlank(info.getOrgnoFlag())?info.getOrgnoFlag().split("-")[0]: StringUtils.EMPTY);
				map.put("prodCreditOrg", StringUtils.isNotBlank(info.getProdCreditOrg())?info.getProdCreditOrg().split("-")[0]: StringUtils.EMPTY);
				map.put("prodCreditMod", StringUtils.isNotBlank(info.getProdCreditMod())?info.getProdCreditMod().split("-")[0]: StringUtils.EMPTY);
				//设置时间格式为yyyyMMdd
				map.put("subsBdate", info.getSubsBdate().replace("-", "")); //募集起始日期
				map.put("subsEdate", info.getSubsEdate().replace("-", ""));//募集结束日期
				map.put("establishDate", info.getEstablishDate().replace("-", ""));//产品起始日期
				map.put("endDate", info.getEndDate().replace("-", ""));//产品预计终止日期
				reportPPIDao.addReportPPI(map);
			}
		} catch (Exception e) {
			log.error("导入产品基本信息异常!", e);
			throw new RuntimeException();
		}
	}

}
