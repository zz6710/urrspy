package com.kayak.rpt.rhlc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.rhlc.dao.SaleMonInvestDao;
import com.kayak.rpt.rhlc.model.SaleMonInvest;

@Service
@APIDefine(desc = "销售月度统计-分投资者类型服务", model = SaleMonInvest.class)
public class SaleMonInvestService {

	@Autowired
	private SaleMonInvestDao saleMonInvestDao;

	@API(desc = "查询销售月度统计-分投资者类型信息", auth = APIAuth.YES)
	public SqlResult<SaleMonInvest> findSaleMonInvests(SqlParam<SaleMonInvest> params) throws Exception {
		return saleMonInvestDao.findSaleMonInvests(params);
	}

	@API(desc = "添加销售月度统计-分投资者类型", params = "id,prod_reg_enc,investor_type,investor_num,sale_total_money,sale_net_money,hold_babance,remark,trade_date,prod_esp_sale_channel,theory_report_start_date,theory_report_end_date,register_status,register_date,imp_date,sys_data_status,sys_data_version,sys_data_source,register_serno,report_date", auth = APIAuth.NO)
	public int addSaleMonInvest(SqlParam<SaleMonInvest> params) throws Exception {
		return saleMonInvestDao.addSaleMonInvest(params).getEffect();
	}
	
	@API(desc = "修改销售月度统计-分投资者类型", params = "id,prod_reg_enc,investor_type,investor_num,sale_total_money,sale_net_money,hold_babance,remark,trade_date,prod_esp_sale_channel,theory_report_start_date,theory_report_end_date,register_status,register_date,imp_date,sys_data_status,sys_data_version,sys_data_source,register_serno,report_date", auth = APIAuth.NO)
	public int updateSaleMonInvest(SqlParam<SaleMonInvest> params) throws Exception {
		return saleMonInvestDao.updateSaleMonInvest(params).getEffect();
	}
	
	@API(desc = "删除销售月度统计-分投资者类型", params = "id,prod_reg_enc,investor_type,investor_num,sale_total_money,sale_net_money,hold_babance,remark,trade_date,prod_esp_sale_channel,theory_report_start_date,theory_report_end_date,register_status,register_date,imp_date,sys_data_status,sys_data_version,sys_data_source,register_serno,report_date", auth = APIAuth.NO)
	public int deleteSaleMonInvest(SqlParam<SaleMonInvest> params) throws Exception {
		return saleMonInvestDao.deleteSaleMonInvest(params).getEffect();
	}

}
