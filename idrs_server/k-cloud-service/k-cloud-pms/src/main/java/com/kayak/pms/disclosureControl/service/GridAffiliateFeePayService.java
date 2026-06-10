package com.kayak.pms.disclosureControl.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.disclosureControl.dao.GridAffiliateFeePayDao;
import com.kayak.pms.disclosureControl.model.GridAffiliateFeePay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "支付关联方费用数据", model = GridAffiliateFeePay.class)
public class GridAffiliateFeePayService {

	@Autowired
	private GridAffiliateFeePayDao gridAffiliateFeePayDao;


	@API(desc = "支付关联方费用数据查询", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<GridAffiliateFeePay> findGridAffiliateFeePay(SqlParam<GridAffiliateFeePay> params) throws Exception {
		params.setMakeSql(true);
		return gridAffiliateFeePayDao.findGridAffiliateFeePay(params);
	}

	@API(desc = "支付关联方费用数据修改",  auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public int updateGridAffiliateFeePay(SqlParam<GridAffiliateFeePay> params) throws Exception {
		return gridAffiliateFeePayDao.updateGridAffiliateFeePay(params);
	}

	@API(desc = "支付关联方费用数据删除",  auth = APIAuth.YES, operation = APIOperation.DELETE)
	public int deleteGridAffiliateFeePay(SqlParam<GridAffiliateFeePay> params) throws Exception {
		return gridAffiliateFeePayDao.deleteGridAffiliateFeePay(params);
	}

}
