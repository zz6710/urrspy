package com.kayak.dps.ods.service;

import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.app.model.T8ProdWorth;
import com.kayak.dps.ods.dao.T8ProdWorthDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;

@Service
@APIDefine(desc = "T8ProdWorth服务", model = T8ProdWorth.class)
public class T8ProdWorthService {

	@Autowired
	private T8ProdWorthDao t8ProdWorthDao;

	@API(desc = "查询产品净值信息", auth = APIAuth.YES ,operation = APIOperation.SELECT)
	public SqlResult<T8ProdWorth> findT8ProdWorths(SqlParam<T8ProdWorth> params) throws Exception {
		params.setMakeSql(false);
		return t8ProdWorthDao.findT8ProdWorths(params);
	}
	/*@API(desc = "新增下拉回显产品代码", auth = APIAuth.NO ,operation = APIOperation.SELECT)
	public SqlResult<T8ProdWorth> findT8ProdProd(SqlParam<T8ProdWorth> params) throws Exception {
		return t8ProdWorthDao.findT8ProdProd(params);
	}*/
	@API(desc = "新增下拉根据代码回显产品名称与代码", auth = APIAuth.NO ,operation = APIOperation.SELECT)
	public SqlResult<T8ProdWorth> findProdWorthCdAndNm(SqlParam<T8ProdWorth> params) throws Exception {
		return t8ProdWorthDao.findProdWorthCdAndNm(params);
	}

	@API(desc = "新增下拉根据代码回显产品名称", auth = APIAuth.NO ,operation = APIOperation.SELECT)
	public SqlResult<T8ProdWorth> findT8ProdProdName(SqlParam<T8ProdWorth> params) throws Exception {
		return t8ProdWorthDao.findT8ProdProdName(params);
	}

	@API(desc = "添加", params = "id,prod_code,prod_name,valuation_date,disclosure_date,net_value,add_net_date,total_assets,gross_liability,prod_add_value,add_share", auth = APIAuth.YES)
	public int addT8ProdWorth(SqlParam<T8ProdWorth> params) throws Exception {
		return t8ProdWorthDao.addT8ProdWorth(params).getEffect();
	}
	
	@API(desc = "修改", params = "id,prod_code,prod_name,valuation_date,disclosure_date,net_value,add_net_date,total_assets,gross_liability,prod_add_value,add_share", auth = APIAuth.YES)
	public int updateT8ProdWorth(SqlParam<T8ProdWorth> params) throws Exception {
		return t8ProdWorthDao.updateT8ProdWorth(params).getEffect();
	}
	
	@API(desc = "删除", params = "prod_code,prod_name,valuation_date,disclosure_date,net_value,add_net_date,total_assets,gross_liability,prod_add_value,add_share", auth = APIAuth.YES)
	public int deleteT8ProdWorth(SqlParam<T8ProdWorth> params) throws Exception {
		return t8ProdWorthDao.deleteT8ProdWorth(params).getEffect();
	}

	@API(desc = "根据产品代码和净值日期获取信息", auth = APIAuth.NO ,operation = APIOperation.SELECT)
	public SqlResult<T8ProdWorth> findDataByProdCode(SqlParam<T8ProdWorth> params) throws Exception {
		return t8ProdWorthDao.findDataByProdCode(params);
	}

}
