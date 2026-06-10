package com.kayak.pms.T8ProdDeal.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;
import com.kayak.pms.T81.model.T8ProdInfo;
import com.kayak.pms.T8ProdDeal.model.T8ProdDeal;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @program: k-cloud
 * @description: 产品流水Dao
 * @author: WangZhenXin
 * @create: 2021-01-06 09:22
 * @memo 备注信息
 */
@Repository
public class T8ProdDealDao extends ComnDao {
    private static final Logger logger = LoggerFactory.getLogger(T8ProdDealDao.class);

    public List<SqlRow> findProdDealByProdCodeOrProdName(SqlParam<T8ProdDeal> params) throws Exception {
        Map<String, Object> param = params.getParams();
        String sql = "select t2.prod_code, " +
                "       t2.prod_name, " +
                "       t.change_date, " +
                "       (case " +
                "            when t.prod_deal_type in ('1', '2') " +
                "                then t.subs_vol " +
                "            when t.prod_deal_type = '3' " +
                "                then t.redeem_vol end) vol, " +
                "       (case " +
                "            when t.prod_deal_type in ('1', '2') " +
                "                then t.subs_amt " +
                "            when t.prod_deal_type = '3' " +
                "                then t.redeem_amt end) amt, " +
                "       t.prod_deal_type, " +
                "       t.fee_type, " +
                "       t.fee_money, " +
                "       t.trans_stat " +
                "from ods_amng_prod_deal t " +
                "         left join t8_prod_info t2 on t.prod_code = t2.prod_code " +
                "where t.prod_deal_type in ('1', '2', '3', '6') " +
                "  and t.trans_stat = '1' " +
                "  and t2.prod_status in ('4', '5') ";
        if (StringUtils.isNotEmpty(params.getModel().getProdCode())) {
            sql = sql + " and t.prod_code like '%" + params.getModel().getProdCode() + "%' ";
        }
        if (StringUtils.isNotEmpty(params.getModel().getChangeDate())) {
            sql = sql + " and t.change_date = '" + params.getModel().getChangeDate() + "' ";
        }
        return super.findRows(sql,param);
    }
}
