package com.kayak.pms.T8ProdDeal.dao;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.T8ProdDeal.model.T8ProdSalesInfoDistributor;
import com.kayak.pms.printTemp.model.StaticTemp;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class T8ProdSalesInfoDistributorDao extends ComnDao {

	public SqlResult<T8ProdSalesInfoDistributor> findT8ProdSalesInfoDistributors(SqlParam<T8ProdSalesInfoDistributor> params) throws Exception {
		return super.findRows("SELECT id,ods_amng_prod_impinfo_id,prod_code,prod_code_sub,distributor_code,change_date,subs_vol,subs_amt,redeem_vol,redeem_amt,fee_type,fee_money,remark,sale_by_proxy,proxy_sum_money,buy_ccy,prod_deal_type,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user FROM ods_amng_prod_impinfo_distributor", params);
	}

	public UpdateResult addT8ProdSalesInfoDistributor(SqlParam<T8ProdSalesInfoDistributor> params) throws Exception {
		return super.update("INSERT INTO ods_amng_prod_impinfo_distributor(id,ods_amng_prod_impinfo_id,prod_code,prod_code_sub,distributor_code,change_date,subs_vol,subs_amt,redeem_vol,redeem_amt,fee_type,fee_money,remark,sale_by_proxy,proxy_sum_money,buy_ccy,prod_deal_type,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user) VALUES($AUTOIDS{id},$S{t8ProdImpinfoId},$S{prodCode},$S{prodCodeSub},$S{distributorCode},$S{changeDate},$D{subsVol},$D{subsAmt},$D{redeemVol},$D{redeemAmt},$S{feeType},$D{feeMoney},$S{remark},$S{saleByProxy},$D{proxySumMoney},$S{buyCcy},$S{prodDealType},$S{crtDate},$S{crtTime},$S{crtUser},$S{updDate},$S{updTime},$S{updUser})",
				params.getModel());
	}
	
	public UpdateResult updateT8ProdSalesInfoDistributor(SqlParam<T8ProdSalesInfoDistributor> params) throws Exception {
		return super.update("UPDATE ods_amng_prod_impinfo_distributor SET ods_amng_prod_impinfo_id=$S{t8ProdImpinfoId} ,prod_code=$S{prodCode} ,prod_code_sub=$S{prodCodeSub} ,distributor_code=$S{distributorCode} ,change_date=$S{changeDate} ,subs_vol=$D{subsVol} ,subs_amt=$D{subsAmt} ,redeem_vol=$D{redeemVol} ,redeem_amt=$D{redeemAmt} ,fee_type=$S{feeType} ,fee_money=$D{feeMoney} ,remark=$S{remark} ,sale_by_proxy=$S{saleByProxy} ,proxy_sum_money=$D{proxySumMoney} ,buy_ccy=$S{buyCcy} ,prod_deal_type=$S{prodDealType} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,crt_user=$S{crtUser} ,upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user=$S{updUser}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteT8ProdSalesInfoDistributor(SqlParam<T8ProdSalesInfoDistributor> params) throws Exception {
		return super.update("DELETE FROM ods_amng_prod_impinfo_distributor WHERE  id=$S{id} ",
				params.getModel());
	}
	//根据销售商代码查询数据
	public List<SqlRow> findByDistributorCode(String distributorCode) throws Exception {

		Map<String, Object> params = new HashMap<>(1);
		params.put("distributorCode", distributorCode);
		return super.findRows("select t.id,t.distributor_code from ods_amng_prod_impinfo_distributor t where t.distributor_code=$S{distributorCode}", params);
	}
	public Integer updateDistributorCode(T8ProdSalesInfoDistributor t8ProdSalesInfoDistributor) throws Exception {

		String sql = " UPDATE ods_amng_prod_impinfo_distributor SET distributor_code = $S{distributorCode} WHERE id = $S{id}";
		return super.update(sql, t8ProdSalesInfoDistributor).getEffect();
	}
}
