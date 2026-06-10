package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.ExeQuery;
import com.kayak.rpt.zz.manage.model.ProdTransRegistInfo;
import com.kayak.rpt.zz.manage.model.UnderAssetRegistInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

@Repository
public class ProdTransRegistInfoDao extends ComnDao {

	public SqlResult<ProdTransRegistInfo> findProdTransRegistInfos(SqlParam<ProdTransRegistInfo> params) throws Exception {
		String sql = "SELECT T1.ID,BANK_CODE,T1.PROD_CODE,T1.TRANS_CODE,T1.ASSET_CODE,T1.CUR,T1.AMT,T1.CONVERT_RMB,T1.QUANTITY,T1.METHOD_ASSET_MEASURE,T1.CASH_TYPE,T1.DETAIL_CASH_TYPE,T1.TRADE_DATE,T1.TRADE_COUNTER,T1.COUNTER_TYPE,T1.UNIT_PRICE_FULL,T1.UNIT_PRICE_NET,T1.RATE_ANNUAL_RETURN,T1.TRANS_IDENT_CODE,T1.DETAILS,T1.register_serno,T1.IMP_DATE,T1.REGISTER_DATE,T1.REGISTER_STATUS,T1.create_date,T1.theory_report_start_date,T1.theory_report_end_date,T1.related_party_trans,T1.trans_origin_time,T1.trans_approve_id,T1.trans_approve_name,T1.trader_id,T1.trader_name,T1.TRX_TM,T1.sys_data_source,T1.sys_data_status,T1.sys_data_version,T1.report_date,ifnull(ARS.audit_status,0) audit_status,T1.is_cover FROM app_prod_trans_regist_info T1 LEFT JOIN base_report_data_audit_results ARS ON T1.theory_report_start_date = ARS.report_date AND ARS.table_id = 'app_prod_trans_regist_info' where t1.sys_data_status ='1'";
		/*if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(register_date) >= DATE($S{startDate}) and DATE(register_date) <= DATE($S{endDate})";
		}*/
		if (params.getModel().getStartDate()!=null && Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(TRADE_DATE) >= "+params.getModel().getStartDate() + "  and DATE(TRADE_DATE) <= "+params.getModel().getEndDate();
		}
		/*if (Strings.isNotBlank(params.getModel().getQueryStartDate())) {
			sql += " and DATE(TRADE_DATE) >= DATE($S{queryStartDate}) and DATE(TRADE_DATE) <= DATE($S{queryEndDate})";
		}*/
		if (params.getModel().getRegisterSerno()!=null && StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  register_serno = '" + params.getModel().getRegisterSerno() + "'";
		}
		if (params.getModel().getProdCode()!=null && StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql = sql + " and  prod_code like '%" + params.getModel().getProdCode().trim() + "%'";
		}
		if (params.getModel().getCashType()!=null && StringUtils.isNotBlank(params.getModel().getCashType())) {
			sql = sql + " and  cash_type = '" + params.getModel().getCashType() + "'";
		}
		if (params.getModel().getRegisterStatus()!=null && StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  register_status = '" + params.getModel().getRegisterStatus() + "'";
		}
		if (params.getModel().getAssetCode()!=null && StringUtils.isNotBlank(params.getModel().getAssetCode())) {
			sql = sql + " and  asset_code like '%" + params.getModel().getAssetCode().trim() + "%'";
		}

		return super.findRows(sql, DataSourceProperty.PUB, params);
	}

	public int findProdTransRegistInfosCount(SqlParam<ProdTransRegistInfo> params) throws Exception {
		String sql = "SELECT count(1) FROM app_prod_trans_regist_info T1 LEFT JOIN base_report_data_audit_results ARS ON T1.theory_report_start_date = ARS.report_date AND ARS.table_id = 'app_prod_trans_regist_info' where t1.sys_data_status ='1'";
		/*if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(register_date) >= DATE($S{startDate}) and DATE(register_date) <= DATE($S{endDate})";
		}*/
		if (params.getModel().getStartDate()!=null && Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(TRADE_DATE) >= "+params.getModel().getStartDate() + "  and DATE(TRADE_DATE) <= "+params.getModel().getEndDate();
		}
		/*if (Strings.isNotBlank(params.getModel().getQueryStartDate())) {
			sql += " and DATE(TRADE_DATE) >= DATE($S{queryStartDate}) and DATE(TRADE_DATE) <= DATE($S{queryEndDate})";
		}*/
		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  register_serno = '" + params.getModel().getRegisterSerno() + "'";
		}
		if (params.getModel().getProdCode()!=null && StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql = sql + " and  prod_code like '%" + params.getModel().getProdCode().trim() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getCashType())) {
			sql = sql + " and  cash_type = '" + params.getModel().getCashType() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  register_status = '" + params.getModel().getRegisterStatus() + "'";
		}
		if (params.getModel().getAssetCode()!=null && StringUtils.isNotBlank(params.getModel().getAssetCode())) {
			sql = sql + " and  asset_code like '%" + params.getModel().getAssetCode().trim() + "%'";
		}

		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
	}

	public UpdateResult addProdTransRegistInfo(SqlParam<ProdTransRegistInfo> params) throws Exception {
		return super.update("INSERT INTO app_prod_trans_regist_info(trans_approve_id,trans_approve_name,trader_id,trader_name,related_party_trans,trans_origin_time,bank_code,prod_code,trans_code,asset_code,cur,amt,convert_rmb,quantity,method_asset_measure,cash_type,detail_cash_type,trade_date,trade_counter,counter_type,unit_price_full,unit_price_net,rate_annual_return,trans_ident_code,details,register_serno,imp_date,register_date,register_status,create_date,theory_report_start_date,trx_tm) VALUES($S{transApproveId},$S{transApproveName},$S{traderId},$S{traderName},$S{relatedPartyTrans},$S{transOriginTime},$S{bankCode},$S{prodCode},$S{transCode},$S{assetCode},$S{cur},$D{amt},$D{convertRmb},$D{quantity},$S{methodAssetMeasure},$S{cashType},$S{detailCashType},$S{tradeDate},$S{tradeCounter},$S{counterType},$D{unitPriceFull},$D{unitPriceNet},$D{rateAnnualReturn},$S{transIdentCode},$S{details},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),$S{impDate},$S{registerDate},'0',date_format(CURDATE(),'%Y%m%d'),DATE_FORMAT($S{tradeDate},'%Y%m%d'),$S{trxTm})",
				DataSourceProperty.PUB,params.getModel());
	}
    //修改交易信息登记管理
	//页面不可修改的字段  prodCode  bankCode  assetCode   transCode cashType
	//后台控制的字段 registerSerno  impDate  registerDate  registerStatus
	public UpdateResult updateProdTransRegistInfo(SqlParam<ProdTransRegistInfo> params) throws Exception {
		String sql = "UPDATE app_prod_trans_regist_info SET trans_approve_id=$S{transApproveId},trans_approve_name=$S{transApproveName}," +
				"trader_id=$S{traderId},trader_name=$S{traderName},related_party_trans=$S{relatedPartyTrans},trx_tm=$S{trxTm}," +
				"cur=$S{cur} ,amt=$D{amt} ,convert_rmb=$D{convertRmb} ,quantity=$D{quantity} ,method_asset_measure=$S{methodAssetMeasure}," +
				"detail_cash_type=$S{detailCashType} ,trade_date=$S{tradeDate},theory_report_start_date=$S{tradeDate},trade_counter=$S{tradeCounter} ,counter_type=$S{counterType}," +
				"unit_price_full=$D{unitPriceFull} ,unit_price_net=$D{unitPriceNet} ,rate_annual_return=$D{rateAnnualReturn} ,trans_ident_code=$S{transIdentCode} ,details=$S{details} ,";
		if(Strings.isNotBlank(params.getModel().getInitTraderId()) && !params.getModel().getInitTraderId().equals(params.getModel().getTraderId())){
			sql += "trader_id=$S{traderId} ,";
		}
		if(Strings.isNotBlank(params.getModel().getInitTransApproveId()) && !params.getModel().getInitTransApproveId().equals(params.getModel().getTransApproveId())){
			sql += "trans_approve_id=$S{transApproveId} ,";
		}
//		sql += "register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ";
		sql += 	" is_cover=$S{isCover} WHERE id=$I{id} ";
		return super.update(sql,DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult deleteProdTransRegistInfo(SqlParam<ProdTransRegistInfo> params) throws Exception {
		return super.update("DELETE FROM app_prod_trans_regist_info WHERE  id=$I{id} ",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult addimportProdTransRegistInfo(Object param) throws Exception {
		return super.update("INSERT INTO app_prod_trans_regist_info(trans_approve_id,trans_approve_name,trader_id,trader_name,related_party_trans,trans_origin_time,bank_code,prod_code,trans_code,asset_code,cur,amt,convert_rmb,quantity,method_asset_measure,cash_type,detail_cash_type,trade_date,trade_counter,counter_type,unit_price_full,unit_price_net,rate_annual_return,trans_ident_code,details,register_serno,imp_date,register_date,register_status,create_date,theory_report_start_date,TRX_TM) VALUES($S{transApproveId},$S{transApproveName},$S{traderId},$S{traderName},$S{relatedPartyTrans},$S{transOriginTime},$S{bankCode},$S{prodCode},$S{transCode},$S{assetCode},$S{cur},$D{amt},$D{convertRmb},$D{quantity},$S{methodAssetMeasure},$S{cashType},$S{detailCashType},$S{tradeDate},$S{tradeCounter},$S{counterType},$D{unitPriceFull},$D{unitPriceNet},$D{rateAnnualReturn},$S{transIdentCode},$S{details},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),date_format(CURDATE(),'%Y%m%d'),$S{registerDate},'0',date_format(CURDATE(),'%Y%m%d'),$S{theoryReportStartDate},$S{trxTm})",
				DataSourceProperty.PUB,param);
	}
    //批量修改导入
	public UpdateResult updateimportProdTransRegistInfo(Map<String, Object> params) throws Exception {
		String sql = "UPDATE app_prod_trans_regist_info SET trans_approve_id=$S{transApproveId},trans_approve_name=$S{transApproveName}," +
				"trader_id=$S{traderId},trader_name=$S{traderName},related_party_trans=$S{relatedPartyTrans},trx_tm=$S{trxTm}," +
				"cur=$S{cur} ,amt=$S{amt} ,convert_rmb=$S{convertRmb} ,quantity=$S{quantity} ,method_asset_measure=$S{methodAssetMeasure}," +
				"detail_cash_type=$S{detailCashType} ,trade_date=$S{tradeDate},theory_report_start_date=$S{tradeDate},trade_counter=$S{tradeCounter} ,counter_type=$S{counterType}," +
				"unit_price_full=$S{unitPriceFull} ,unit_price_net=$S{unitPriceNet} ,rate_annual_return=$S{rateAnnualReturn} ,trans_ident_code=$S{transIdentCode} ,details=$S{details}," +
				"is_cover=$S{isCover} WHERE trans_code=$S{transCode} ";
		if(params.get("prodCode")!=null && Strings.isNotBlank((String)params.get("prodCode"))){
			sql = sql+" and prod_code=$S{prodCode}";
		}
		if(params.get("assetCode")!=null && Strings.isNotBlank((String)params.get("assetCode"))){
			sql = sql+" and asset_code=$S{assetCode}";
		}
		if(params.get("cashType")!=null && Strings.isNotBlank((String)params.get("cashType"))){
			sql = sql+" and cash_type=$S{cashType}";
		}
		//页面不可修改的字段  prodCode  bankCode  assetCode   transCode  cashType
		//后台控制的字段 registerSerno  impDate  registerDate  registerStatus
		return super.update(sql,DataSourceProperty.PUB,params);
	}

    public UpdateResult deleteimportProdTransRegistInfo(Map<String, Object> params) throws Exception {
		return super.update("DELETE FROM app_prod_trans_regist_info where theory_report_start_date between $S{beginDate} and $S{queryDate} ", params);
    }

	public List<SqlRow> findImportMenuFileManageId(Map<String, Object> params) throws Exception {
		String sql = "SELECT CONCAT(IFNULL(max(id), 0)) AS id FROM import_menu_file_manage";
		return super.findRows(sql);
	}

	public List<SqlRow> findImportMenuFileManage(Map<String, Object> params) throws Exception {
		String sql = "SELECT menu_id, file_name, local_file_path, status FROM import_menu_file_manage WHERE 1 = 1 ";
		if(!ObjectUtils.isEmpty(params.get("id"))){
			sql += "and id = " + params.get("id");
		}
		if(!ObjectUtils.isEmpty(params.get("status"))){
			sql += "and status = " + params.get("status");
		}
		return super.findRows(sql);
	}

	public UpdateResult addImportMenuFileManage(Map<String, Object> params) throws Exception {
		return super.update("INSERT INTO import_menu_file_manage (id, menu_id, file_name, local_file_path, status) values ($I{id}, $S{menuId}, $S{fileName}, $S{localFilePath}, $S{status})",
				DataSourceProperty.PUB,params);
	}

	public UpdateResult updImportMenuFileManage(Map<String, Object> params) throws Exception {
		return super.update("UPDATE import_menu_file_manage SET status = $S{status} WHERE id = $S{id}",
				DataSourceProperty.PUB,params);
	}

	/**
	 * 执行交易信息登记更新数据
	 * @param sqlIdList
	 */
	public void executeUpdSql(List<String> sqlIdList, Map<String, Object> params) throws Exception {
		for (String exeId : sqlIdList) {
			super.update(ExeQuery.queryExeId(exeId), DataSourceProperty.PUB, params);
		}
	}
	public int findProdTransRegistInfoFailStatus(SqlParam<ProdTransRegistInfo> params) throws Exception {
		String startDate = params.getModel().getStartDate();
		String sql = "SELECT  count(1) FROM ";
		String reportDate = startDate;
		sql += " app_prod_trans_regist_info";
		sql += " T1 LEFT JOIN base_report_data_audit_results ARS ON T1.theory_report_start_date = ARS.report_date AND ARS.table_id = 'app_prod_trans_regist_info' where t1.sys_data_status ='1' and t1.register_status in (0,1) ";
		if (params.getModel().getStartDate()!=null && Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(TRADE_DATE) >= "+params.getModel().getStartDate() + "  and DATE(TRADE_DATE) <= "+params.getModel().getEndDate();
		}
		if (params.getModel().getRegisterSerno()!=null && StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  register_serno = '" + params.getModel().getRegisterSerno() + "'";
		}
		if (params.getModel().getProdCode()!=null && StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql = sql + " and  prod_code like '%" + params.getModel().getProdCode().trim() + "%'";
		}
		if (params.getModel().getCashType()!=null && StringUtils.isNotBlank(params.getModel().getCashType())) {
			sql = sql + " and  cash_type = '" + params.getModel().getCashType() + "'";
		}
		if (params.getModel().getRegisterStatus()!=null && StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  register_status = '" + params.getModel().getRegisterStatus() + "'";
		}
		if (params.getModel().getAssetCode()!=null && StringUtils.isNotBlank(params.getModel().getAssetCode())) {
			sql = sql + " and  asset_code like '%" + params.getModel().getAssetCode().trim() + "%'";
		}
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
	}

	public UpdateResult updateProdTransRegistInfoStatus(SqlParam<ProdTransRegistInfo> params) throws Exception {
		String sql = "update app_prod_trans_regist_info SET register_status='3' WHERE sys_data_status='1' ";
		if (params.getModel().getStartDate()!=null && Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(TRADE_DATE) >= "+params.getModel().getStartDate() + "  and DATE(TRADE_DATE) <= "+params.getModel().getEndDate();
		}
		if (params.getModel().getProdCode()!=null && StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql = sql + " and  prod_code like '%" + params.getModel().getProdCode().trim() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getCashType())) {
			sql = sql + " and  cash_type = '" + params.getModel().getCashType() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  register_serno = '" + params.getModel().getRegisterSerno() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  register_status = '" + params.getModel().getRegisterStatus() + "'";
		}
		if (params.getModel().getAssetCode()!=null && StringUtils.isNotBlank(params.getModel().getAssetCode())) {
			sql = sql + " and  asset_code like '%" + params.getModel().getAssetCode().trim() + "%'";
		}
		return super.update(sql, DataSourceProperty.PUB,params.getModel());
	}
	public void updateBaseReportResultInfo(SqlParam<ProdTransRegistInfo> params) throws Exception {
		String queryEndDate=params.getModel().getEndDate();
		String queryStartDate=params.getModel().getStartDate();
		String sql="update base_report_result set register_date = theory_report_start_date,report_success_number=total,status= '1',register_status= '1',update_date=date_format(now(),'%Y%m%d'),update_time=date_format(now(),'%H%i%s') where report_table = 'app_prod_trans_regist_info' and theory_report_start_date in (select theory_report_start_date from app_prod_trans_regist_info where trade_date>='"+queryStartDate+"' and trade_date<= '"+queryEndDate+"' and sys_data_status='1') ";
		super.update(sql, DataSourceProperty.PUB, params.getModel());
	}

    //导入
	public UpdateResult addImportProdTransRegistInfo(Object param) throws Exception {
		return super.update("INSERT INTO app_prod_trans_regist_info(trans_approve_id,trans_approve_name,trader_id,trader_name,related_party_trans,trans_origin_time,bank_code,prod_code,trans_code,asset_code,cur,amt,convert_rmb,quantity,method_asset_measure,cash_type,detail_cash_type,trade_date,trade_counter,counter_type,unit_price_full,unit_price_net,rate_annual_return,trans_ident_code,details,register_serno,imp_date,register_date,register_status,create_date,theory_report_start_date,TRX_TM,report_date,sys_data_status) VALUES($S{transApproveId},$S{transApproveName},$S{traderId},$S{traderName},$S{relatedPartyTrans},$S{trxTm},$S{bankCode},$S{prodCode},$S{transCode},$S{assetCode},$S{cur},$D{amt},$D{convertRmb},$D{quantity},$S{methodAssetMeasure},$S{cashType},$S{detailCashType},$S{tradeDate},$S{tradeCounter},$S{counterType},$D{unitPriceFull},$D{unitPriceNet},$D{rateAnnualReturn},$S{transIdentCode},$S{details},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),date_format(CURDATE(),'%Y%m%d'),'','0',date_format(CURDATE(),'%Y%m%d'),$S{theoryReportStartDate},$S{trxTm},$S{reportDate},'1')",
				DataSourceProperty.PUB,param);
	}
}
