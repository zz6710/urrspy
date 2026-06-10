package com.kayak.rpt.zz.historyInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.historyInfo.dao.ProdIssuanceRegistInfohDao;
import com.kayak.rpt.zz.historyInfo.model.ProdIssuanceRegistInfoh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "产品发行登记历史信息服务", model = ProdIssuanceRegistInfoh.class)
public class ProdIssuanceRegistInfohService {

	@Autowired
	private ProdIssuanceRegistInfohDao prodIssuanceRegistInfohDao;

	@API(desc = "查询产品发行登记历史信息信息", auth = APIAuth.YES)
	public SqlResult<ProdIssuanceRegistInfoh> findProdIssuanceRegistInfos(SqlParam<ProdIssuanceRegistInfoh> params) throws Exception {
		params.setMakeSql(true);
		return prodIssuanceRegistInfohDao.findProdIssuanceRegistInfohs(params);
	}

	@API(desc = "添加产品发行登记历史信息", params = "prod_code,bank_code,prod_ident_code,subscription_start_date,subscription_end_date,prod_value_date,prod_maturity_date,management_method,structured_prod,details_per_rate,opening_mode,register_serno,imp_date,register_date,register_status,up_limit_per_rate,low_limit_per_rate,regular_open_period,other_open_period,disorder_open_period,first_open_day,holiday_open_type,average_open_no,busi_open_period,details_busi_op_period,custody_acct_no,custody_acct_name", auth = APIAuth.NO)
	public int addProdIssuanceRegistInfo(SqlParam<ProdIssuanceRegistInfoh> params) throws Exception {
		return prodIssuanceRegistInfohDao.addProdIssuanceRegistInfoh(params).getEffect();
	}
	
	@API(desc = "修改产品发行登记历史信息", params = "prod_code,bank_code,prod_ident_code,subscription_start_date,subscription_end_date,prod_value_date,prod_maturity_date,management_method,structured_prod,details_per_rate,opening_mode,register_serno,imp_date,register_date,register_status,up_limit_per_rate,low_limit_per_rate,regular_open_period,other_open_period,disorder_open_period,first_open_day,holiday_open_type,average_open_no,busi_open_period,details_busi_op_period,custody_acct_no,custody_acct_name", auth = APIAuth.NO)
	public int updateProdIssuanceRegistInfo(SqlParam<ProdIssuanceRegistInfoh> params) throws Exception {
		return prodIssuanceRegistInfohDao.updateProdIssuanceRegistInfoh(params).getEffect();
	}
	
	@API(desc = "删除产品发行登记历史信息", params = "prod_code,bank_code,prod_ident_code,subscription_start_date,subscription_end_date,prod_value_date,prod_maturity_date,management_method,structured_prod,details_per_rate,opening_mode,register_serno,imp_date,register_date,register_status,up_limit_per_rate,low_limit_per_rate,regular_open_period,other_open_period,disorder_open_period,first_open_day,holiday_open_type,average_open_no,busi_open_period,details_busi_op_period,custody_acct_no,custody_acct_name", auth = APIAuth.NO)
	public int deleteProdIssuanceRegistInfo(SqlParam<ProdIssuanceRegistInfoh> params) throws Exception {
		return prodIssuanceRegistInfohDao.deleteProdIssuanceRegistInfoh(params).getEffect();
	}

}
