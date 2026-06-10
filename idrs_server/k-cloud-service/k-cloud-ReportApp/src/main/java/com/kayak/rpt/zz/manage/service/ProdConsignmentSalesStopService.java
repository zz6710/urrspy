package com.kayak.rpt.zz.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.ProdConsignmentSalesStopDao;
import com.kayak.rpt.zz.manage.model.ProdConsignmentSalesStop;

@Service
@APIDefine(desc = "委托销售产品编码停用导入服务", model = ProdConsignmentSalesStop.class)
public class ProdConsignmentSalesStopService {

	@Autowired
	private ProdConsignmentSalesStopDao prodConsignmentSalesStopDao;

	@API(desc = "查询委托销售产品编码停用导入信息", auth = APIAuth.YES)
	public SqlResult<ProdConsignmentSalesStop> findProdConsignmentSalesStops(SqlParam<ProdConsignmentSalesStop> params) throws Exception {
		return prodConsignmentSalesStopDao.findProdConsignmentSalesStops(params);
	}

	@API(desc = "添加委托销售产品编码停用导入", params = "id,end_date,bank_code,prod_reg_enc,create_date,theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status,sys_data_version,sys_data_source,register_serno,report_date,register_date", auth = APIAuth.NO)
	public int addProdConsignmentSalesStop(SqlParam<ProdConsignmentSalesStop> params) throws Exception {
		return prodConsignmentSalesStopDao.addProdConsignmentSalesStop(params).getEffect();
	}
	
	@API(desc = "修改委托销售产品编码停用导入", params = "id,end_date,bank_code,prod_reg_enc,create_date,theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status,sys_data_version,sys_data_source,register_serno,report_date,register_date", auth = APIAuth.NO)
	public int updateProdConsignmentSalesStop(SqlParam<ProdConsignmentSalesStop> params) throws Exception {
		return prodConsignmentSalesStopDao.updateProdConsignmentSalesStop(params).getEffect();
	}
	
	@API(desc = "删除委托销售产品编码停用导入", params = "id,end_date,bank_code,prod_reg_enc,create_date,theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status,sys_data_version,sys_data_source,register_serno,report_date,register_date", auth = APIAuth.NO)
	public int deleteProdConsignmentSalesStop(SqlParam<ProdConsignmentSalesStop> params) throws Exception {
		return prodConsignmentSalesStopDao.deleteProdConsignmentSalesStop(params).getEffect();
	}

}
