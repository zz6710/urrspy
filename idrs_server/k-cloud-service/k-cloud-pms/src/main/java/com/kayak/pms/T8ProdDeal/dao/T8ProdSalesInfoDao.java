package com.kayak.pms.T8ProdDeal.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.T8ProdDeal.model.T8ProdSalesInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * @program: k-cloud
 * @description: 产品销售数据Dao
 * @author: axin
 * @create: 2021-03-16 16:23
 * @memo 备注信息
 */
@Repository
public class T8ProdSalesInfoDao extends ComnDao {
    private static final Logger logger = LoggerFactory.getLogger(T8ProdSalesInfoDao.class);

    public SqlResult<T8ProdSalesInfo> findT8ProdSalesInfos(SqlParam<T8ProdSalesInfo> params) throws Exception {
        String sql = "SELECT tpi.id,tpi.prod_code,tp.prod_name,tpi.prod_code_sub,tpi.change_date,tpi.subs_vol,tpi.subs_amt,tpi.redeem_vol,tpi.redeem_amt," +
                "tpi.fee_type,tpi.fee_money,tpi.remark,tpi.sale_by_proxy,tpi.proxy_sum_money,tpi.buy_ccy,tpi.prod_deal_type,tpi.crt_date,tpi.crt_time,tpi.crt_user," +
                "tpi.upd_date,tpi.upd_time,tpi.upd_user FROM ods_amng_prod_impinfo tpi left join t8_prod_info tp on  tp.prod_code=tpi.prod_code where 1=1";
        if (StringUtils.isNotBlank(params.getModel().getProdName())) {
            sql = sql + " and tp.prod_name like '%" + params.getModel().getProdName() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
            sql = sql + " and tpi.prod_code =  + '"+params.getModel().getProdCode()+"' ";
        }
        return super.findRows(sql, params);
    }

    public UpdateResult addT8ProdSalesInfo(SqlParam<T8ProdSalesInfo> params) throws Exception {
        return super.update("INSERT INTO ods_amng_prod_impinfo(id,prod_code,prod_code_sub,change_date,subs_vol,subs_amt,redeem_vol,redeem_amt,fee_type,fee_money,remark,sale_by_proxy,proxy_sum_money,buy_ccy,prod_deal_type,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user) VALUES($AUTOIDS{id},$S{prodCode},$S{prodCodeSub},$S{changeDate},$D{subsVol},$D{subsAmt},$D{redeemVol},$D{redeemAmt},$S{feeType},$D{feeMoney},$S{remark},$S{saleByProxy},$D{proxySumMoney},$S{buyCcy},$S{prodDealType},$S{crtDate},$S{crtTime},$S{crtUser},$S{updDate},$S{updTime},$S{updUser})",
                params.getModel());
    }

    public UpdateResult updateT8ProdSalesInfo(SqlParam<T8ProdSalesInfo> params) throws Exception {
        return super.update("UPDATE ods_amng_prod_impinfo SET change_date=$S{changeDate} ,subs_vol=$D{subsVol} ,subs_amt=$D{subsAmt} ,redeem_vol=$D{redeemVol} ,redeem_amt=$D{redeemAmt} ,fee_type=$S{feeType} ,fee_money=$D{feeMoney} ,remark=$S{remark} ,sale_by_proxy=$S{saleByProxy} ,proxy_sum_money=$D{proxySumMoney} ,buy_ccy=$S{buyCcy} ,prod_deal_type=$S{prodDealType} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,crt_user=$S{crtUser} ,upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user=$S{updUser}  WHERE  id=$S{id} ",
                params.getModel());
    }

    public UpdateResult deleteT8ProdSalesInfo(SqlParam<T8ProdSalesInfo> params) throws Exception {
        return super.update("DELETE FROM ods_amng_prod_impinfo WHERE  id=$S{id} ",
                params.getModel());
    }

	public UpdateResult addT8ProdSalesInfoDistributor(SqlParam<T8ProdSalesInfo> params)throws Exception  {
		return super.update("INSERT INTO ods_amng_prod_impinfo_distributor(id,ods_amng_prod_impinfo_id,prod_code,prod_code_sub,distributor_code,change_date,subs_vol,subs_amt,redeem_vol,redeem_amt,fee_type,fee_money,remark,sale_by_proxy,proxy_sum_money,buy_ccy,prod_deal_type,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user) VALUES($AUTOIDS{id},$S{t8ProdImpinfoId},$S{prodCode},$S{prodCodeSub},$S{distributorCode},$S{changeDate},$D{subsVol},$D{subsAmt},$D{redeemVol},$D{redeemAmt},$S{feeType},$D{feeMoney},$S{remark},$S{saleByProxy},$D{proxySumMoney},$S{buyCcy},$S{prodDealType},$S{crtDate},$S{crtTime},$S{crtUser},$S{updDate},$S{updTime},$S{updUser})",
				params.getModel());
	}
	
	public UpdateResult updateT8ProdSalesInfoDistributor(SqlParam<T8ProdSalesInfo> params) throws Exception {
		return super.update("UPDATE ods_amng_prod_impinfo_distributor SET ods_amng_prod_impinfo_id=$S{t8ProdImpinfoId} ,prod_code=$S{prodCode} ,prod_code_sub=$S{prodCodeSub} ,distributor_code=$S{distributorCode} ,change_date=$S{changeDate} ,subs_vol=$D{subsVol} ,subs_amt=$D{subsAmt} ,redeem_vol=$D{redeemVol} ,redeem_amt=$D{redeemAmt} ,fee_type=$S{feeType} ,fee_money=$D{feeMoney} ,remark=$S{remark} ,sale_by_proxy=$S{saleByProxy} ,proxy_sum_money=$D{proxySumMoney} ,buy_ccy=$S{buyCcy} ,prod_deal_type=$S{prodDealType} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,crt_user=$S{crtUser} ,upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user=$S{updUser}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteT8ProdSalesInfoDistributor(SqlParam<T8ProdSalesInfo> params) throws Exception {
		return super.update("DELETE FROM ods_amng_prod_impinfo_distributor WHERE  id=$S{id} ",
				params.getModel());
	}
}
