package com.kayak.rpt.zz.manage.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.PublicUtils;
import com.kayak.graphql.model.FetcherData;
import com.kayak.rpt.zz.manage.dao.AssetDebtRegisterInfoDao;
import com.kayak.rpt.zz.manage.enums.OperatorEnum;
import com.kayak.rpt.zz.manage.model.AssetDebtRegisterInfo;
import com.kayak.rpt.zz.manage.model.AssetDebtRegisterInfo;
import com.kayak.rpt.zz.manage.model.AssetDebtRegisterInfo;
import com.kayak.rpt.zz.manage.util.CheckDataParams;
import com.kayak.rpt.zz.operate.service.AssetDebtRegistService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@APIDefine(desc = "资产要素登记管理服务", model = AssetDebtRegisterInfo.class)
public class AssetDebtRegisterInfoService {

	@Value("${zg.query.reflect.upd}")
	private String reflectUpdSql;
	@Value("${zg.query.org_info.upd}")
	private String orgInfoUpdSql;
	@Value("${zg.query.asset_info.upd}")
	private String assetInfoUpdSql;
	@Value("${zg.query.memo.upd1}")
	private String detailsUpdSql1;
	@Value("${zg.query.memo.upd2}")
	private String detailsUpdSql2;
	@Autowired
	protected DaoService daoService;
	@Autowired
	private AssetDebtRegisterInfoDao assetDebtRegisterInfoDao;

	@Autowired
	CheckDataForVueService checkDataForVueService;

	CheckDataParams checkDataParams = new CheckDataParams();

	@Autowired
	private AssetDebtRegistService assetDebtRegistService;
	@API(desc = "查询资产要素登记管理信息", auth = APIAuth.YES)
	public SqlResult<AssetDebtRegisterInfo> findAssetDebtRegisterInfos(SqlParam<AssetDebtRegisterInfo> params) throws Exception {
		/*String isUpdate = SysUtil.getSystemParamsByParaid("is_zg_upd");//是否更新交易信息登记
		if("1".equals(isUpdate)){
			updateTradeInfoReportData();//更新交易信息登记
		}*/
//		params.setMakeSql(true);
		return assetDebtRegisterInfoDao.findAssetDebtRegisterInfos(params);
	}

	@API(desc = "添加资产要素登记管理",  auth = APIAuth.YES)
	public int addAssetDebtRegisterInfo(SqlParam<AssetDebtRegisterInfo> params) throws Exception {
		// 操作记录
//		assetDebtRegistService.addAssetDebtRegist(params, OperatorEnum.CREATE.getVal());
		if(assetDebtRegisterInfoDao.isExist(params)){
			return assetDebtRegisterInfoDao.updateAssetDebtRegister(params).getEffect();
		}else{
			return assetDebtRegisterInfoDao.addAssetDebtRegisterInfo(params).getEffect();
		}
	}
	
	@API(desc = "修改资产要素登记管理", params = "register_serno,imp_date,register_date,register_status,bank_code,asset_code,ass_debt_type,cur,trade_venue,details,bb_deposit_bank,bb_account_no,bb_deposit_amt,bb_value_date,bb_maturity_date,bb_annual_rate,bb_interest_basis,bb_deposit_type,bb_struct_deposit_type,bb_struct_deposit,cc_ident_code,cc_name,cc_specific_bond_type,cc_iss_mode_bond,cc_iss_rate_part,cc_institute_type_scale,cc_institute_type_tech,cc_institute_type_economic,cc_industry_issuer,cc_regist_deposit,cc_details_regist_deposit,dd_value_date,dd_maturity_date,dd_counterparty,dd_counterparty_type,dd_annal_interest_rate,dd_interest_basis,dd_collateral_type,dd_collateral_value,ee_name,ee_amt,ee_unit_par_value,ee_ownership_type,ee_buyback,ee_value_date,ee_maturity_date,ee_statutory_maturity_date,ee_expected_return,ee_project_annaul_return,ee_coupon_type,ee_regualr_interest_pay,ee_interest_pay_frequency,ee_coupon_allocation_type,ee_detail_princ_interest,ee_interest_basis,ee_bench_rate_type,ee_float_factor,ee_float_rate,ee_yield_spread_bp,ee_struct_grade,ee_princ_payment_type,ee_install_repay_type,ee_base_asset_type,ee_percent_exc_in_allot,ee_debtor,ee_deptor_rate,ee_rate_agency_iss,ee_debtor_type_scale,ee_debtor_type_tech,ee_debtor_type_economic,ee_project,ee_industry_debtor,ee_industry_project,ee_monitor_indus_type,ee_monitor_industry_type,ee_details_monitory_type,ee_guarantee_method,ee_detail_guarantee_status,ee_pledge_type,ee_pledge_value,ee_guarantee_type,ee_guarantor_type,ee_debtor_rate,ee_inter_asset_rate,ee_out_asset_rate,ee_option_type,ee_exercise_date_type,ee_fixed_exercise_date,ee_first_exercise_date,ee_exercise_period,ee_exercise_price,ee_perpetual_type,ee_deferre_interest_type,ee_interest_deferred,ee_first_reprice_date,ee_reprice_period,ee_partial_redemption,ee_partial_redemption_rate,ee_option_right,ee_details_exercise_term,ee_region_debtor,ee_enhance_institute_code,ee_enhance_institute_name,ee_total_fee_rate,ee_organization_code,ff_ownership,ff_buyback,ff_type,ff_quantity,ff_aggregate_amt,ff_weight_remain_day,ff_max_remain_day,ff_min_remain_fay,ff_maturity_date,ff_value_date,ff_discount_rate,ff_industry,gg_stock_code,gg_name,gg_stock_type,gg_industry,gg_invest_stage,gg_equity_out_date,gg_enter_type_scale,gg_enter_type_tech,gg_enter_type_economic,gg_pledged_finace,gg_debt_equity_swap,hh_name,hh_nominal_principal,hh_under_asset_type,hh_hold_objective,ii_county_region,ii_bond_name,ii_bond_ident_code,ii_issuer,ii_industry_issuer,ii_value_date,ii_maturity_date,ii_term_maturity,ii_issuer_rate_bond,ii_bond_rate,ii_coup_rate,ii_interest_pay_quency,ii_details_assure_status,ii_details_special_terms,jj_country,jj_value_date,jj_maturity_date,jj_counterparty,jj_interest_rate,jj_interest_basis,kk_country,kk_ident_code,kk_name,kk_issuer,kk_industry,ll_country,ll_contract_name,ll_value_date,ll_maturity_date,ll_coupon_rate,ll_interest_frequency,ll_percent_fix,ll_percent_derivate,ll_derivate_invet_type,ll_under_asset,ll_details_proceeds,ll_details_option,ll_max_note_return,ll_min_note_return,ll_strike_under_asset,ll_under_rg_price,ll_trans_costs,mm_manage_plan_name,mm_issued_asset_company,mm_plan_issuer_code,mm_asset_plan_rg_code,mm_manager,mm_custodian,mm_amt,mm_actual_direct,mm_details_invest,mm_industry_invest,mm_plan_start_date,mm_plan_maturity_date,mm_plan_type,mm_expected_return,mm_max_expected_return,mm_min_expected_return,mm_invest_structure,mm_manager_type,mm_manager_fee_rate,mm_custodian_fee_rate,mm_trans_cost_rate,mm_inter_fee_rate,mm_other_expense_rate,nn_country,nn_name,nn_term_days,nn_asset_value,nn_asset_return,oo_country,oo_name,oo_value_date,oo_maturity_date,oo_asset_value,oo_asset_return,pp_fund_code,pp_fund_name,pp_issued_asset_company,pp_industry,pp_regist_agency,pp_govern_invest_fund,pp_direct_govern_fund,pp_ta_name,pp_manager_fund_name,pp_custodian_fund_name,pp_invest_stage,pp_enter_type_scale,pp_enter_type_tech,pp_enter_type_economic,pp_invest_assets,qq_out_agreement_name,qq_out_agreement_code,qq_trustee,qq_actual_manager,qq_custodian,qq_out_amt,qq_actual_direction,qq_details_invest,qq_industry_invest,qq_value_date,qq_maturity_date,qq_out_type,qq_expected_return,qq_max_expected_return,qq_min_expected_return,qq_manager_fee_rate,qq_custodian_fee_rate,qq_trans_cost_rate,qq_inter_fee_rate,qq_other_expenses_rate,rr_country,rr_name,rr_term_maturity,rr_liability_amt,rr_interest_rate,ss_name,ss_asset_type,ss_details_asset_type,ss_amt,ss_value_date,ss_maturity_date,ss_country,ss_expected_return,ss_annual_return,ss_interest_frequency,ss_debtor,ss_organ_code,ss_rate_agency_iss,ss_debtor_type_scale,ss_debtor_type_tech,ss_debtor_type_economic,ss_project,ss_industry_debtor,ss_industry_project,ss_monitory_industry,ss_monitory_industry_type,ss_details_monitory_type,ss_internal_asset_rate,ss_guarantee_method,ss_details_guarantee,ss_pledge_type,ss_pledge_value,ss_guarantee_type,ss_guarantor_type,ss_debt_equity_swap", auth = APIAuth.YES)
	public String updateAssetDebtRegisterInfo(SqlParam<AssetDebtRegisterInfo> params) throws Exception {
		try {
			checkDataParams.initDataNoDict();
			String whiteregex = CheckDataParams.whiteregex;
			String whitereForCode = CheckDataParams.whitereForCode;
			String checkErr = checkDataForVueService.assetDebtRegisterInfoCheckForVue(whiteregex,whitereForCode,params.getModel());
			if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
				return RequestSupport.updateReturnJson(false,  "修改失败！错误信息为：\n"+checkErr, null).toString();
			}
			// 操作记录
			Map paramMap = new HashMap<>();
			paramMap.put("registerSerno",params.getModel().getRegisterSerno());
			SqlParam<AssetDebtRegisterInfo> oldParams =  new FetcherData<>(paramMap,AssetDebtRegisterInfo.class);
			SqlResult<AssetDebtRegisterInfo> originParams =  assetDebtRegisterInfoDao.findAssetDebtRegisterInfos(oldParams);
			if(originParams.getRows().size()>0){
				AssetDebtRegisterInfo param = 	originParams.getRows().get(0);
				paramMap = BeanUtil.beanToMap(param);
				oldParams =  new FetcherData<>(paramMap,AssetDebtRegisterInfo.class);
			}
			assetDebtRegistService.addAssetDebtRegist(oldParams, OperatorEnum.UPDATE.getVal());
			assetDebtRegisterInfoDao.updateAssetDebtRegisterInfo(params);
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "修改失败，数据库错误信息为："+e.getMessage(), null).toString();
		}

	}

	@API(desc = "删除资产要素登记管理", params = "register_serno,imp_date,register_date,register_status,bank_code,asset_code,ass_debt_type,cur,trade_venue,details,bb_deposit_bank,bb_deposit_amt,bb_value_date,bb_maturity_date,bb_annual_rate,bb_interest_basis,bb_deposit_type,bb_struct_deposit_type,bb_struct_deposit,cc_ident_code,cc_name,cc_specific_bond_type,cc_iss_mode_bond,cc_iss_rate_part,cc_institute_type_scale,cc_institute_type_tech,cc_institute_type_economic,cc_industry_issuer,cc_regist_deposit,cc_details_regist_deposit,dd_value_date,dd_maturity_date,dd_counterparty,dd_counterparty_type,dd_annal_interest_rate,dd_interest_basis,dd_collateral_type,dd_collateral_value,ee_name,ee_amt,ee_unit_par_value,ee_ownership_type,ee_buyback,ee_value_date,ee_maturity_date,ee_statutory_maturity_date,ee_expected_return,ee_project_annaul_return,ee_coupon_type,ee_regualr_interest_pay,ee_interest_pay_frequency,ee_coupon_allocation_type,ee_detail_princ_interest,ee_interest_basis,ee_bench_rate_type,ee_float_factor,ee_float_rate,ee_yield_spread_bp,ee_struct_grade,ee_princ_payment_type,ee_install_repay_type,ee_base_asset_type,ee_percent_exc_in_allot,ee_debtor,ee_deptor_rate,ee_rate_agency_iss,ee_debtor_type_scale,ee_debtor_type_tech,ee_debtor_type_economic,ee_project,ee_industry_debtor,ee_industry_project,ee_monitor_indus_type,ee_monitor_industry_type,ee_details_monitory_type,ee_guarantee_method,ee_detail_guarantee_status,ee_pledge_type,ee_pledge_value,ee_guarantee_type,ee_guarantor_type,ee_debtor_rate,ee_inter_asset_rate,ee_out_asset_rate,ee_option_type,ee_exercise_date_type,ee_fixed_exercise_date,ee_first_exercise_date,ee_exercise_period,ee_exercise_price,ee_perpetual_type,ee_deferre_interest_type,ee_interest_deferred,ee_first_reprice_date,ee_reprice_period,ee_partial_redemption,ee_partial_redemption_rate,ee_option_right,ee_details_exercise_term,ee_region_debtor,ee_enhance_institute_code,ee_enhance_institute_name,ee_total_fee_rate,ee_organization_code,ff_ownership,ff_buyback,ff_type,ff_quantity,ff_aggregate_amt,ff_weight_remain_day,ff_max_remain_day,ff_min_remain_fay,ff_maturity_date,ff_value_date,ff_discount_rate,ff_industry,gg_stock_code,gg_name,gg_stock_type,gg_industry,gg_invest_stage,gg_equity_out_date,gg_enter_type_scale,gg_enter_type_tech,gg_enter_type_economic,gg_pledged_finace,gg_debt_equity_swap,hh_name,hh_nominal_principal,hh_under_asset_type,hh_hold_objective,ii_county_region,ii_bond_name,ii_bond_ident_code,ii_issuer,ii_industry_issuer,ii_value_date,ii_maturity_date,ii_term_maturity,ii_issuer_rate_bond,ii_bond_rate,ii_coup_rate,ii_interest_pay_quency,ii_details_assure_status,ii_details_special_terms,jj_country,jj_value_date,jj_maturity_date,jj_counterparty,jj_interest_rate,jj_interest_basis,kk_country,kk_ident_code,kk_name,kk_issuer,kk_industry,ll_country,ll_contract_name,ll_value_date,ll_maturity_date,ll_coupon_rate,ll_interest_frequency,ll_percent_fix,ll_percent_derivate,ll_derivate_invet_type,ll_under_asset,ll_details_proceeds,ll_details_option,ll_max_note_return,ll_min_note_return,ll_strike_under_asset,ll_under_rg_price,ll_trans_costs,mm_manage_plan_name,mm_issued_asset_company,mm_plan_issuer_code,mm_asset_plan_rg_code,mm_manager,mm_custodian,mm_amt,mm_actual_direct,mm_details_invest,mm_industry_invest,mm_plan_start_date,mm_plan_maturity_date,mm_plan_type,mm_expected_return,mm_max_expected_return,mm_min_expected_return,mm_invest_structure,mm_manager_type,mm_manager_fee_rate,mm_custodian_fee_rate,mm_trans_cost_rate,mm_inter_fee_rate,mm_other_expense_rate,nn_country,nn_name,nn_term_days,nn_asset_value,nn_asset_return,oo_country,oo_name,oo_value_date,oo_maturity_date,oo_asset_value,oo_asset_return,pp_fund_code,pp_fund_name,pp_issued_asset_company,pp_industry,pp_regist_agency,pp_govern_invest_fund,pp_direct_govern_fund,pp_ta_name,pp_manager_fund_name,pp_custodian_fund_name,pp_invest_stage,pp_enter_type_scale,pp_enter_type_tech,pp_enter_type_economic,pp_invest_assets,qq_out_agreement_name,qq_out_agreement_code,qq_trustee,qq_actual_manager,qq_custodian,qq_out_amt,qq_actual_direction,qq_details_invest,qq_industry_invest,qq_value_date,qq_maturity_date,qq_out_type,qq_expected_return,qq_max_expected_return,qq_min_expected_return,qq_manager_fee_rate,qq_custodian_fee_rate,qq_trans_cost_rate,qq_inter_fee_rate,qq_other_expenses_rate,rr_country,rr_name,rr_term_maturity,rr_liability_amt,rr_interest_rate,ss_name,ss_asset_type,ss_details_asset_type,ss_amt,ss_value_date,ss_maturity_date,ss_country,ss_expected_return,ss_annual_return,ss_interest_frequency,ss_debtor,ss_organ_code,ss_rate_agency_iss,ss_debtor_type_scale,ss_debtor_type_tech,ss_debtor_type_economic,ss_project,ss_industry_debtor,ss_industry_project,ss_monitory_industry,ss_monitory_industry_type,ss_details_monitory_type,ss_internal_asset_rate,ss_guarantee_method,ss_details_guarantee,ss_pledge_type,ss_pledge_value,ss_guarantee_type,ss_guarantor_type,ss_debt_equity_swap", auth = APIAuth.YES)
	public String deleteAssetDebtRegisterInfo(SqlParam<AssetDebtRegisterInfo> params) throws Exception {
		// 操作记录
		assetDebtRegistService.addAssetDebtRegist(params, OperatorEnum.DELETE.getVal());
		assetDebtRegisterInfoDao.deleteAssetDebtRegisterInfo(params).getEffect();
		return RequestSupport.updateReturnJson(true,  "操作成功！", null).toString();
	}
	/**是否有 交易信息登记记录**/
	public int findProdTransRegistInfosEffective(AssetDebtRegisterInfo params) throws Exception {
		int transEffevtive = assetDebtRegisterInfoDao.findProdTransRegistInfosEffective(params);
		return transEffevtive;
	}
	/**是否有 资产持仓登记记录**/
	public int findAssetRegistInfosEffective(AssetDebtRegisterInfo params) throws Exception {
		int assetEffevtive = assetDebtRegisterInfoDao.findAssetRegistInfosEffective(params);
		return assetEffevtive;
	}
	/**是否有 底层资产持仓登记记录**/
	public int findUnderAssetRegistInfosEffective(AssetDebtRegisterInfo params) throws Exception {
		int underEffevtive = assetDebtRegisterInfoDao.findUnderAssetRegistInfosEffective(params);
		return underEffevtive;
	}


	@API(desc = "资产负债类别字典查询",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> findAssDebtTypeDict(SqlParam<AssetDebtRegisterInfo> param) throws Exception {
		//Map<String, Object> params = new HashMap<>();
		Map<String, Object> params = RequestSupport.getParameters();
		//params.put("assetDebtRegisterType",param.getModel().getAssetDebtRegisterType());
		List<SqlRow> tempTypeByDocType = assetDebtRegisterInfoDao.findAssDebtTypeDict(params);
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(tempTypeByDocType.size());
		sqlRowSqlResult.setRows(tempTypeByDocType);
		sqlRowSqlResult.setDesensitized(false);;
		return sqlRowSqlResult;
	}

	public void importAssetDebtRegisterInfo(List<AssetDebtRegisterInfo> assetDebtRegisterInfos,Map<String, Object> params) throws Exception {
		// 添加至操作记录
		assetDebtRegisterInfoDao.deleteImportAssetDebtRegistInfo(params);
		for (AssetDebtRegisterInfo assetDebtRegisterInfo : assetDebtRegisterInfos) {
			Map<String, Object> map = BeanUtil.beanToMap(assetDebtRegisterInfo);
//			assetDebtRegistService.addImportAssetDebtRegist(assetDebtRegisterInfo,OperatorEnum.IMPORT.getVal());
			assetDebtRegisterInfoDao.addImportAssetDebtRegistInfo(map);
		}
	}

	/**
	 * 更新交易登记报表
	 * @throws Exception
	 */
	public void updateTradeInfoReportData () throws Exception {
		String workday = PublicUtils.getSysWordDay();
		List<String> updSqlList = new ArrayList<>(Arrays.asList(reflectUpdSql,orgInfoUpdSql,assetInfoUpdSql));
		Map<String, Object> params = new HashMap<>();
		params.put("workday", workday);
		assetDebtRegisterInfoDao.executeUpdSql(updSqlList, params);
	}
	@API(desc = "查询报送状态为0,1的数据", auth = APIAuth.NO)
	public String getAbnormalData(SqlParam<AssetDebtRegisterInfo> params) throws Exception {
		try {
			int  recordCnt = assetDebtRegisterInfoDao.findAssetDebtRegisterInfosCount(params);
//			if (updateResult.getRows().size() == 0) {
			if (recordCnt == 0) {
				return RequestSupport.updateReturnJson(false,  "没有需要确认并导出的数据，请检查！", null).toString();
			}
//			SqlResult<AssetDebtRegisterInfo> updateResult1 = assetDebtRegisterInfoDao.findAssetDebtRegisterInfoFailStatus(params);
			int unreadyCnt= assetDebtRegisterInfoDao.findAssetDebtRegisterInfoFailStatus(params);
//			if (updateResult1.getRows().size() > 0) {
			if (unreadyCnt > 0) {
				return RequestSupport.updateReturnJson(false,  "存在报送状态异常(0 初始化 或 1 校验失败)的数据，请处理后导出！", null).toString();
			}
			return RequestSupport.updateReturnJson(true,  "", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "查询失败，请检查", null).toString();
		}
	}

	@API(desc = "确认并导出资产要素登记",  auth = APIAuth.YES)
	public String updateAssetDebtRegisterInfoStatus(SqlParam<AssetDebtRegisterInfo> params) throws Exception {
		try {
			daoService.doTrans(() -> {
				assetDebtRegisterInfoDao.updateAssetDebtRegisterInfoStatus(params);
				assetDebtRegisterInfoDao.updateBaseReportResultInfo(params);
			});
			return RequestSupport.updateReturnJson(true,  "操作成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "操作失败，请检查!", null).toString();
		}
	}
}
