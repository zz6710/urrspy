package com.kayak.rpt.zz.manage.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kayak.rpt.zz.manage.model.ProdTransRegistInfo;
import com.kayak.rpt.zz.manage.util.CheckDataParams;
import org.apache.commons.lang3.StringUtils;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.zz.manage.dao.ProdRegistFilingCompareDao;
import com.kayak.rpt.zz.manage.dao.ProdRegistFilingInfoDao;
import com.kayak.rpt.zz.manage.enums.OperatorEnum;
import com.kayak.rpt.zz.manage.model.ProdRegistFilingInfo;
import com.kayak.rpt.zz.operate.service.ProdRegistFilingService;
import com.kayak.server.ServerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "产品申报登记服务", model = ProdRegistFilingInfo.class)
public class ProdRegistFilingInfoService {

	@Autowired
	private ProdRegistFilingInfoDao prodRegistFilingInfoDao;

	@Autowired
	private ProdRegistFilingService prodRegistFilingService;

	@Autowired
	private ProdRegistFilingCompareDao prodRegistFilingCompareDao;

	@Autowired
	protected DaoService daoService;

	@Autowired
	CheckDataForVueService checkDataForVueService;

	CheckDataParams checkDataParams = new CheckDataParams();

	private final Logger log = LoggerFactory.getLogger(ProdRegistFilingInfoService.class);

	@API(desc = "查询产品申报登记信息", auth = APIAuth.YES)
	public SqlResult<ProdRegistFilingInfo> findProdRegistFilingInfos1(SqlParam<ProdRegistFilingInfo> params) throws Exception {
//		params.setMakeSql(true);
		return prodRegistFilingInfoDao.findProdRegistFilingInfos(params);
	}

	@API(desc = "查询发行登记数据及字段变更标识", auth = APIAuth.YES)
	public SqlResult<ProdRegistFilingInfo> findProdRegistFilingInfos(SqlParam<ProdRegistFilingInfo> params) throws Exception {
		SqlResult<ProdRegistFilingInfo> r1=  prodRegistFilingInfoDao.findProdRegistFilingInfos(params);
		List<ProdRegistFilingInfo> returnList = new ArrayList<>();
		if(r1 != null && r1.getRows() != null &&r1.getRows().size() > 0){
			List<ProdRegistFilingInfo> list0 = new ArrayList<>(),list1 = new ArrayList<>();
			list0 = r1.getRows();//原始数据
			StringBuffer prods = new StringBuffer();
			for(int i = 0;i<list0.size();i++){
				ProdRegistFilingInfo ProdRegistFilingInfo = list0.get(i);
				if(i == list0.size()-1){
					prods.append("'"+ProdRegistFilingInfo.getIdentCode()+"'");
				}else{
					prods.append("'"+ProdRegistFilingInfo.getIdentCode()+"',");
				}
			}

			list1 = prodRegistFilingInfoDao.findProdIssuanceRegistInfosByprod(String.valueOf(prods),params).getRows();//指定产品的数据集

			for(int i = 0;i<list0.size();i++){
				ProdRegistFilingInfo ProdRegistFilingInfo = list0.get(i);
				ProdRegistFilingInfo prodIssuance2 = prodRegistFilingCompareDao.compareFlag(ProdRegistFilingInfo,list1);
				returnList.add(prodIssuance2);
			}
		}
		r1.setRowsList1(returnList);
		return r1;
	}


	@API(desc = "添加产品申报登记", params = "prod_name,ident_code,prod_brand,prod_term_no,bank_code,approver_id_code,designer_id_code,manager_id_code,contact_name,contact_telphone,contact_mobile,contact_email,type_collect,prod_retrun_type,prod_term,fiancial_exclusive,invert_region,invert_country,service_mode,operation_mode,min_hold_period,min_hold_day,option_redempt_period,cash_manager,asset_ac_method,prod_mana_mode,ac_mana_name,price_method,invest_type,cooperate_mode,cooperator,invest_type_ratio,prod_benchmark,risk_level,prod_sales_region,fund_cur,principal_cur,income_cur,invest_threshold,plan_fund_amt,start_date_earliest,start_date_latest,principal_due_date,income_due_date,sales_commission_rate,manage_fee_rate,dc_cd_name,dc_cd_ident_code,seas_cd_nation,seas_cd_name,cd_fee_rate,risk_rate,early_tn_option,invest_rdm_option,prod_crt_enhance,crt_ins_type,prod_crt_method,details,register_serno,imp_date,register_date,register_status,main_doc,feasy_ass_report,inter_audit_doc,due_diligencr_doc,legal_doc_sifned,prod_sale_doc,prod_specifi,prod_mark_doc,other_doc,new_prod", auth = APIAuth.YES)
	public String addProdRegistFilingInfo(SqlParam<ProdRegistFilingInfo> params) throws Exception {
		try {//只有修改、删除添加操作记录,且为修改前内容的记录
//			prodRegistFilingService.addProdRegistFiling(params, OperatorEnum.CREATE.getVal());
			prodRegistFilingInfoDao.addProdRegistFilingInfo(params);
			return RequestSupport.updateReturnJson(true,  "添加成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "添加失败，有相同产品申报信息", null).toString();
		}
	}
	
	@API(desc = "修改产品申报登记", params = "prod_name,ident_code,prod_brand,prod_term_no,bank_code,approver_id_code,designer_id_code,manager_id_code,contact_name,contact_telphone,contact_mobile,contact_email,type_collect,prod_retrun_type,prod_term,fiancial_exclusive,invert_region,invert_country,service_mode,operation_mode,min_hold_period,min_hold_day,option_redempt_period,cash_manager,asset_ac_method,prod_mana_mode,ac_mana_name,price_method,invest_type,cooperate_mode,cooperator,invest_type_ratio,prod_benchmark,risk_level,prod_sales_region,fund_cur,principal_cur,income_cur,invest_threshold,plan_fund_amt,start_date_earliest,start_date_latest,principal_due_date,income_due_date,sales_commission_rate,manage_fee_rate,dc_cd_name,dc_cd_ident_code,seas_cd_nation,seas_cd_name,cd_fee_rate,risk_rate,early_tn_option,invest_rdm_option,prod_crt_enhance,crt_ins_type,prod_crt_method,details,register_serno,imp_date,register_date,register_status,main_doc,feasy_ass_report,inter_audit_doc,due_diligencr_doc,legal_doc_sifned,prod_sale_doc,prod_specifi,prod_mark_doc,other_doc,new_prod", auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public String updateProdRegistFilingInfo(SqlParam<ProdRegistFilingInfo> params) throws Exception {
		try {
			checkDataParams.initDataNoDict();
			String whiteregex = CheckDataParams.whiteregex;
			String whitereForCode = CheckDataParams.whitereForCode;
			String checkErr = checkDataForVueService.prodRegistFilingInfoCheckForVue(whiteregex,whitereForCode,params.getModel());
			if (StringUtils.isNotBlank(checkErr)) {
				return RequestSupport.updateReturnJson(false,  "修改失败！错误信息为：\n"+checkErr, null).toString();
			}
			//只有修改、删除添加操作记录,且为修改前内容的记录 确认申报表的唯一主键
			SqlResult<ProdRegistFilingInfo> sqlResult = prodRegistFilingInfoDao.findProdRegistFilingInfoSingleById(params);
			prodRegistFilingService.addOldProdRegistFiling(sqlResult.getRows().get(0), OperatorEnum.UPDATE.getVal());
//			prodRegistFilingService.addProdRegistFiling(params, OperatorEnum.UPDATE.getVal());
			prodRegistFilingInfoDao.updateProdRegistFilingInfo(params);
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "修改失败，数据库错误信息为："+e.getMessage(), null).toString();
		}
	}

	@API(desc = "查询报送状态为0,1的数据", auth = APIAuth.NO)
	public String getAbnormalData(SqlParam<ProdRegistFilingInfo> params) throws Exception {
		try {
			int  recordCnt = prodRegistFilingInfoDao.findProdRegistFilingInfosCount(params);
			if (recordCnt == 0) {
				return RequestSupport.updateReturnJson(false,  "没有需要确认并导出的数据，请检查！", null).toString();
			}
			int unreadyCnt= prodRegistFilingInfoDao.findProdRegistFilingInfoFailStatus(params);
			if (unreadyCnt > 0) {
				return RequestSupport.updateReturnJson(false,  "存在报送状态异常(0 初始化 或 1 校验失败)的数据，请处理后导出！", null).toString();
			}
			return RequestSupport.updateReturnJson(true,  "", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "查询失败，请检查", null).toString();
		}
	}

	@API(desc = "确认并导出申报", auth = APIAuth.YES)
	public String updateProdRegistFilingInfoStatus(SqlParam<ProdRegistFilingInfo> params) throws Exception {
		try {
			String prod_code = prodRegistFilingInfoDao.getProdCode(params);

			if(StringUtils.isNotBlank(prod_code)) {
				daoService.doTrans(() -> {
					prodRegistFilingInfoDao.updateProdDataFlag(params,prod_code);
					prodRegistFilingInfoDao.updateProdRegistFilingInfoStatus(params,prod_code);
					prodRegistFilingInfoDao.updateProdStat(params,prod_code);
				});
			}else{
				return RequestSupport.updateReturnJson(false,  "操作失败，产品代码集合为空!", null).toString();
			}
			return RequestSupport.updateReturnJson(true,  "操作成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "操作失败，请检查!", null).toString();
		}
	}
	
	@API(desc = "删除产品申报登记", params = "prod_name,ident_code,prod_brand,prod_term_no,bank_code,approver_id_code,designer_id_code,manager_id_code,contact_name,contact_telphone,contact_mobile,contact_email,type_collect,prod_retrun_type,prod_term,fiancial_exclusive,invert_region,invert_country,service_mode,operation_mode,min_hold_period,min_hold_day,option_redempt_period,cash_manager,asset_ac_method,prod_mana_mode,ac_mana_name,price_method,invest_type,cooperate_mode,cooperator,invest_type_ratio,prod_benchmark,risk_level,prod_sales_region,fund_cur,principal_cur,income_cur,invest_threshold,plan_fund_amt,start_date_earliest,start_date_latest,principal_due_date,income_due_date,sales_commission_rate,manage_fee_rate,dc_cd_name,dc_cd_ident_code,seas_cd_nation,seas_cd_name,cd_fee_rate,risk_rate,early_tn_option,invest_rdm_option,prod_crt_enhance,crt_ins_type,prod_crt_method,details,register_serno,imp_date,register_date,register_status,main_doc,feasy_ass_report,inter_audit_doc,due_diligencr_doc,legal_doc_sifned,prod_sale_doc,prod_specifi,prod_mark_doc,other_doc,new_prod", auth = APIAuth.YES)
	public String deleteProdRegistFilingInfo(SqlParam<ProdRegistFilingInfo> params) throws Exception {
		//删除操作，正常添加操作记录
		prodRegistFilingService.addProdRegistFiling(params, OperatorEnum.DELETE.getVal());
		prodRegistFilingInfoDao.deleteProdRegistFilingInfo(params).getEffect();
		return RequestSupport.updateReturnJson(true,  "操作成功！", null).toString();
	}
	public int findProdRegistFilingInfoEffective(ProdRegistFilingInfo params) throws Exception {
		//删除前先校验产品是否已发行登记（有过发行登记的记录），若是，不允许删除
		int checkInt = prodRegistFilingInfoDao.findProdRegistFilingInfoEffective(params);
		return checkInt;
	}

	@API(desc = "根据产品id查询", auth = APIAuth.NO)
	public SqlResult<ProdRegistFilingInfo> findProdRegistFilingInfosById(SqlParam<ProdRegistFilingInfo> params) throws Exception {
		params.setMakeSql(false);
		return prodRegistFilingInfoDao.findProdRegistFilingInfosById(params);
	}

	@API(desc = "根据产品id查询", auth = APIAuth.NO)
	public SqlResult<ProdRegistFilingInfo> findProdInfos(SqlParam<ProdRegistFilingInfo> params) throws Exception {
		params.setMakeSql(false);
		return prodRegistFilingInfoDao.findProdInfos(params);
	}
	@API(desc = "导入产品申报登记信息", auth = APIAuth.YES, operation = APIOperation.INSTER)
	public void importProdRegistFilingInfo(List<ProdRegistFilingInfo> prodRegistFilingInfos,Map<String, Object> params) throws Exception {
		try {
			for (ProdRegistFilingInfo prodRegistFilingInfo : prodRegistFilingInfos) {
				Map<String, Object> map = BeanUtil.beanToMap(prodRegistFilingInfo);
				map.put("theoryReportStartDate", DateUtil.getLastSysWordDay(DateUtil.getNowDate()));
				map.put("registerStatus","0");
				/*根据产品公私募 推算理论报送截止日期*/
				if("01".equals(map.get("typeCollect"))){
					/*公募 募集起始日期前10个工作日*/
					map.put("theoryReportEndDate", DateUtil.getSysWordDayByNum(map.get("startDateEarliest").toString(),-10));
				}else {
					/*私募 募集起始日期前2个工作日*/
					map.put("theoryReportEndDate", DateUtil.getSysWordDayByNum(map.get("startDateEarliest").toString(),-2));
				}
				//只有修改、删除添加操作记录,且为修改前内容的记录
//				prodRegistFilingService.addImportProdRegistFiling(prodRegistFilingInfo, OperatorEnum.IMPORT.getVal());
				prodRegistFilingInfoDao.addImportProdRegistFilingInfo(map);
				prodRegistFilingInfoDao.addImportProdBaseInfo(map);
				prodRegistFilingInfoDao.addImportProdSpvsInfo(map);
			}
			params.put("reportType", "02");//报表大类
			params.put("reportTable", "app_prod_regist_filing_info");//报表名称
			params.put("settleDate", DateUtil.getLastSysWordDay(DateUtil.getNowDate()));//数据日期
			Object strResult = ServerUtil.requestPostJson("DpsApp", "/handleReportDataTask.action", params);
			JSONObject result = JSON.parseObject(strResult.toString());
			if (result == null ) {
				throw new Exception("指标校验发起失败:" + result.get("returnmsg"));
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
