package com.kayak.rpt.rhlc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.rhlc.dao.SaleMonChannelDao;
import com.kayak.rpt.rhlc.model.SaleMonChannel;

@Service
@APIDefine(desc = "销售月度统计-分销售渠道服务", model = SaleMonChannel.class)
public class SaleMonChannelService {

	@Autowired
	private SaleMonChannelDao saleMonChannelDao;

	@API(desc = "查询销售月度统计-分销售渠道信息", auth = APIAuth.YES)
	public SqlResult<SaleMonChannel> findSaleMonChannels(SqlParam<SaleMonChannel> params) throws Exception {
		return saleMonChannelDao.findSaleMonChannels(params);
	}

	@API(desc = "添加销售月度统计-分销售渠道", params = "id,prod_reg_enc,sale_proxy,sale_org_code,investor_num,sale_total_money,sale_net_money,hold_babance,remark,trade_date,prod_esp_sale_channel,theory_report_start_date,theory_report_end_date,register_status,register_date,imp_date,sys_data_status,sys_data_version,sys_data_source,register_serno,report_date", auth = APIAuth.NO)
	public int addSaleMonChannel(SqlParam<SaleMonChannel> params) throws Exception {
		return saleMonChannelDao.addSaleMonChannel(params).getEffect();
	}
	
	@API(desc = "修改销售月度统计-分销售渠道", params = "id,prod_reg_enc,sale_proxy,sale_org_code,investor_num,sale_total_money,sale_net_money,hold_babance,remark,trade_date,prod_esp_sale_channel,theory_report_start_date,theory_report_end_date,register_status,register_date,imp_date,sys_data_status,sys_data_version,sys_data_source,register_serno,report_date", auth = APIAuth.NO)
	public int updateSaleMonChannel(SqlParam<SaleMonChannel> params) throws Exception {
		return saleMonChannelDao.updateSaleMonChannel(params).getEffect();
	}
	
	@API(desc = "删除销售月度统计-分销售渠道", params = "id,prod_reg_enc,sale_proxy,sale_org_code,investor_num,sale_total_money,sale_net_money,hold_babance,remark,trade_date,prod_esp_sale_channel,theory_report_start_date,theory_report_end_date,register_status,register_date,imp_date,sys_data_status,sys_data_version,sys_data_source,register_serno,report_date", auth = APIAuth.NO)
	public int deleteSaleMonChannel(SqlParam<SaleMonChannel> params) throws Exception {
		return saleMonChannelDao.deleteSaleMonChannel(params).getEffect();
	}

}
