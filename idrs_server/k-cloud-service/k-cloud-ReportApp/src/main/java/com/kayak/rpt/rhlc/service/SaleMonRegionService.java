package com.kayak.rpt.rhlc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.rhlc.dao.SaleMonRegionDao;
import com.kayak.rpt.rhlc.model.SaleMonRegion;

@Service
@APIDefine(desc = "销售月度统计-分投地区类型服务", model = SaleMonRegion.class)
public class SaleMonRegionService {

	@Autowired
	private SaleMonRegionDao saleMonRegionDao;

	@API(desc = "查询销售月度统计-分投地区类型信息", auth = APIAuth.YES)
	public SqlResult<SaleMonRegion> findSaleMonRegions(SqlParam<SaleMonRegion> params) throws Exception {
		return saleMonRegionDao.findSaleMonRegions(params);
	}

	@API(desc = "添加销售月度统计-分投地区类型", params = "id,prod_reg_enc,region_type,investor_num,sale_total_money,sale_net_money,hold_babance,remark,trade_date,prod_esp_sale_channel,theory_report_start_date,theory_report_end_date,register_status,register_date,imp_date,sys_data_status,sys_data_version,sys_data_source,report_date", auth = APIAuth.NO)
	public int addSaleMonRegion(SqlParam<SaleMonRegion> params) throws Exception {
		return saleMonRegionDao.addSaleMonRegion(params).getEffect();
	}
	
	@API(desc = "修改销售月度统计-分投地区类型", params = "id,prod_reg_enc,region_type,investor_num,sale_total_money,sale_net_money,hold_babance,remark,trade_date,prod_esp_sale_channel,theory_report_start_date,theory_report_end_date,register_status,register_date,imp_date,sys_data_status,sys_data_version,sys_data_source,report_date", auth = APIAuth.NO)
	public int updateSaleMonRegion(SqlParam<SaleMonRegion> params) throws Exception {
		return saleMonRegionDao.updateSaleMonRegion(params).getEffect();
	}
	
	@API(desc = "删除销售月度统计-分投地区类型", params = "id,prod_reg_enc,region_type,investor_num,sale_total_money,sale_net_money,hold_babance,remark,trade_date,prod_esp_sale_channel,theory_report_start_date,theory_report_end_date,register_status,register_date,imp_date,sys_data_status,sys_data_version,sys_data_source,report_date", auth = APIAuth.NO)
	public int deleteSaleMonRegion(SqlParam<SaleMonRegion> params) throws Exception {
		return saleMonRegionDao.deleteSaleMonRegion(params).getEffect();
	}

}
