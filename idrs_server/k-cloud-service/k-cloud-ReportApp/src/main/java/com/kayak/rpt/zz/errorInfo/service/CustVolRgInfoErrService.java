package com.kayak.rpt.zz.errorInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.errorInfo.dao.CustVolRgInfoErrDao;
import com.kayak.rpt.zz.errorInfo.model.CustVolRgInfoErr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "投资者持有错误信息服务", model = CustVolRgInfoErr.class)
public class CustVolRgInfoErrService {

	@Autowired
	private CustVolRgInfoErrDao custVolRgInfoErrDao;

	@API(desc = "查询投资者持有错误信息信息", auth = APIAuth.YES)
	public SqlResult<CustVolRgInfoErr> findCustVolRgInfos(SqlParam<CustVolRgInfoErr> params) throws Exception {
		params.setMakeSql(true);
		return custVolRgInfoErrDao.findCustVolRgInfos(params);
	}

	@API(desc = "添加投资者持有错误信息", params = "bank_code_desc,prod_code_desc,cust_no_desc,hold_date_desc,cur_desc,hold_vol_desc,hold_amt_desc,convert_rmb_desc,imp_date,register_serno,id", auth = APIAuth.NO)
	public int addCustVolRgInfo(SqlParam<CustVolRgInfoErr> params) throws Exception {
		return custVolRgInfoErrDao.addCustVolRgInfo(params).getEffect();
	}
	
	@API(desc = "修改投资者持有错误信息", params = "bank_code_desc,prod_code_desc,cust_no_desc,hold_date_desc,cur_desc,hold_vol_desc,hold_amt_desc,convert_rmb_desc,imp_date,register_serno,id", auth = APIAuth.NO)
	public int updateCustVolRgInfo(SqlParam<CustVolRgInfoErr> params) throws Exception {
		return custVolRgInfoErrDao.updateCustVolRgInfo(params).getEffect();
	}
	
	@API(desc = "删除投资者持有错误信息", params = "bank_code_desc,prod_code_desc,cust_no_desc,hold_date_desc,cur_desc,hold_vol_desc,hold_amt_desc,convert_rmb_desc,imp_date,register_serno,id", auth = APIAuth.NO)
	public int deleteCustVolRgInfo(SqlParam<CustVolRgInfoErr> params) throws Exception {
		return custVolRgInfoErrDao.deleteCustVolRgInfo(params).getEffect();
	}

}
