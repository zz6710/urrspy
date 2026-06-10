package com.kayak.rpt.rhlc.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhlc.model.SaleMonInvest;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class SaleMonInvestDao extends ComnDao {

	public SqlResult<SaleMonInvest> findSaleMonInvests(SqlParam<SaleMonInvest> params) throws Exception {
		String sql = "select distinct \n" +
				"\ta.id,\n" +
				"\ta.prod_reg_enc,\n" +
				"\tcase when b.itemval is null then a.investor_type \n" +
				"\telse concat(concat(a.investor_type, ' '), b.itemval) end investor_type,\n" +
				"\ta.investor_num,\n" +
				"\treplace(cast(format(a.sale_total_money/10000, 9) as char),',','') as sale_total_money,\n" +
				"\treplace(cast(format(a.sale_net_money/10000, 9) as char),',','') as sale_net_money,\n" +
				"\treplace(cast(format(a.hold_babance/10000, 9) as char),',','') as hold_babance,\n" +
				"\ta.remark,\n" +
				"\ta.trade_date,\n" +
				"\tcase when c.itemval is null then a.prod_esp_sale_channel \n" +
				"\telse concat(concat(a.prod_esp_sale_channel, ' '), c.itemval) end prod_esp_sale_channel,\n" +
				"\ta.theory_report_start_date,\n" +
				"\ta.theory_report_end_date,\n" +
				"\ta.register_status,\n" +
				"\ta.register_date,\n" +
				"\ta.imp_date, \n" +
				"\ta.sys_data_status, \n" +
				"\ta.sys_data_version, \n" +
				"\ta.sys_data_source, \n" +
				"\ta.register_serno, \n" +
				"\ta.report_date \n" +
				"\tfrom \n" +
				"\tapp_prod_sale_investor a\n" +
				"\tleft join sys_dict_item b on b.dict='investor_type' and a.investor_type=b.itemkey \n" +
				"\tleft join sys_dict_item c on c.dict='prod_esp_sale_channel' and a.prod_esp_sale_channel=c.itemkey \n" +
				"\twhere 1 = 1 and a.sys_data_status = '1' \n";
		if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
			sql = sql + " and a.report_date like '%" + params.getModel().getReportDate() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdRegEnc())) {
			sql = sql + " and a.prod_reg_enc like '%" + params.getModel().getProdRegEnc() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getInvestorType())) {
			sql = sql + " and a.investor_type like '%" + params.getModel().getInvestorType() + "%'";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addSaleMonInvest(SqlParam<SaleMonInvest> params) throws Exception {
		return super.update("INSERT INTO app_prod_sale_investor(id,prod_reg_enc,investor_type,investor_num,sale_total_money,sale_net_money,hold_babance,remark,trade_date,prod_esp_sale_channel,theory_report_start_date,theory_report_end_date,register_status,register_date,imp_date,sys_data_status,sys_data_version,sys_data_source,register_serno,report_date) VALUES($AUTOIDS{id},$S{prodRegEnc},$S{investorType},$S{investorNum},$D{saleTotalMoney},$D{saleNetMoney},$D{holdBabance},$S{remark},$S{tradeDate},$S{prodEspSaleChannel},$S{theoryReportStartDate},$S{theoryReportEndDate},$S{registerStatus},$S{registerDate},$S{impDate},$S{sysDataStatus},$S{sysDataVersion},$S{sysDataSource},$S{registerSerno},$S{reportDate})",
				params.getModel());
	}
	
	public UpdateResult updateSaleMonInvest(SqlParam<SaleMonInvest> params) throws Exception {
		return super.update("UPDATE app_prod_sale_investor SET prod_reg_enc=$S{prodRegEnc} ,investor_type=$S{investorType} ,investor_num=$S{investorNum} ,sale_total_money=$D{saleTotalMoney} ,sale_net_money=$D{saleNetMoney} ,hold_babance=$D{holdBabance} ,remark=$S{remark} ,trade_date=$S{tradeDate} ,prod_esp_sale_channel=$S{prodEspSaleChannel} ,theory_report_start_date=$S{theoryReportStartDate} ,theory_report_end_date=$S{theoryReportEndDate} ,register_status=$S{registerStatus} ,register_date=$S{registerDate} ,imp_date=$S{impDate} ,sys_data_status=$S{sysDataStatus} ,sys_data_version=$S{sysDataVersion} ,sys_data_source=$S{sysDataSource} ,register_serno=$S{registerSerno} ,report_date=$S{reportDate}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteSaleMonInvest(SqlParam<SaleMonInvest> params) throws Exception {
		return super.update("DELETE FROM app_prod_sale_investor WHERE  id=$S{id} ",
				params.getModel());
	}

}
