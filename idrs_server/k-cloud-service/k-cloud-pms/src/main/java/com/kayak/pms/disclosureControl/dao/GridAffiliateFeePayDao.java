package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.disclosureControl.model.GridAffiliateFeePay;
import org.springframework.stereotype.Repository;

@Repository
public class GridAffiliateFeePayDao extends ComnDao {


    public SqlResult<GridAffiliateFeePay> findGridAffiliateFeePay(SqlParam<GridAffiliateFeePay> params) throws Exception {
        return super.findRows("select  id, prod_cd, affiliate_name, fee_type, deal_amount, pos_dt, deal_dt, crt_dt, crt_time from  app_grid_affiliate_fee_pay_base  order by deal_dt",DataSourceProperty.IDB, params);
    }

    public int updateGridAffiliateFeePay(SqlParam<GridAffiliateFeePay> params) throws Exception {
        String sql = "UPDATE app_grid_affiliate_fee_pay_base \n" +
                "SET `prod_cd` = $S{prodCd},\n" +
                "`affiliate_name` = $S{affiliateName},\n" +
                "`fee_type` = $S{feeType},\n" +
                "`deal_amount` = $S{dealAmount},\n" +
                "`pos_dt` = $S{posDt}\n" +
                "WHERE\n" +
                "\t`id` = $S{id}";
        return super.update(sql,DataSourceProperty.IDB,params.getModel()).getEffect();
    }

    public int deleteGridAffiliateFeePay(SqlParam<GridAffiliateFeePay> params) throws Exception {
        return super.update("delete  from  app_grid_affiliate_fee_pay_base where  id = $S{id}",DataSourceProperty.IDB,params.getModel()).getEffect();
    }
}
