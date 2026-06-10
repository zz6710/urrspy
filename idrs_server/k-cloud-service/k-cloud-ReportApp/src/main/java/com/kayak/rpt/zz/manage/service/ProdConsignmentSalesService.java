package com.kayak.rpt.zz.manage.service;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.system.RequestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.ProdConsignmentSalesDao;
import com.kayak.rpt.zz.manage.model.ProdConsignmentSales;

import java.util.HashMap;
import java.util.Map;

@Service
@APIDefine(desc = "委托销售产品编码导入服务", model = ProdConsignmentSales.class)
public class ProdConsignmentSalesService {

	@Autowired
	private ProdConsignmentSalesDao prodConsignmentSalesDao;
	@Autowired
	protected ComnDao comnDao;

	@API(desc = "查询委托销售产品编码导入信息", auth = APIAuth.YES)
	public SqlResult<ProdConsignmentSales> findProdConsignmentSales(SqlParam<ProdConsignmentSales> params) throws Exception {
		return prodConsignmentSalesDao.findProdConsignmentSales(params);
	}

	@API(desc = "查询委托销售产品编码导入机构代码", auth = APIAuth.YES)
	public String getParaValueForFXJGDM(SqlParam<ProdConsignmentSales> params) throws Exception {
		Map<String, Object> param=new HashMap<>();
		Map<String, Object> rtnParam=new HashMap<>();
		String queryStr="select paravalue from sys_param where paraid = '80000047'";
		String cnt = comnDao.findRow(queryStr, param).getString("paravalue");
		if (String.valueOf(cnt) == null) {
			rtnParam.put("paravalue","");
		} else {
			rtnParam.put("paravalue",cnt);
		}
		return RequestSupport.updateReturnJson(true, "查询成功", rtnParam).toString();
	}

	@API(desc = "添加委托销售产品编码导入", params = "id,establish_date,bank_code,prod_reg_enc,create_date,theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status,sys_data_version,sys_data_source,register_serno,report_date,register_date", auth = APIAuth.NO)
	public int addProdConsignmentSales(SqlParam<ProdConsignmentSales> params) throws Exception {
		return prodConsignmentSalesDao.addProdConsignmentSales(params).getEffect();
	}
	
	@API(desc = "修改委托销售产品编码导入", params = "id,establish_date,bank_code,prod_reg_enc,create_date,theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status,sys_data_version,sys_data_source,register_serno,report_date,register_date", auth = APIAuth.NO)
	public int updateProdConsignmentSales(SqlParam<ProdConsignmentSales> params) throws Exception {
		return prodConsignmentSalesDao.updateProdConsignmentSales(params).getEffect();
	}
	
	@API(desc = "删除委托销售产品编码导入", params = "id,establish_date,bank_code,prod_reg_enc,create_date,theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status,sys_data_version,sys_data_source,register_serno,report_date,register_date", auth = APIAuth.NO)
	public int deleteProdConsignmentSales(SqlParam<ProdConsignmentSales> params) throws Exception {
		return prodConsignmentSalesDao.deleteProdConsignmentSales(params).getEffect();
	}

}
