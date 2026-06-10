package com.kayak.dps.ods.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class InvestorBaseHandlerDao extends ComnDao {

    /**
     * 每日变更投资者信息查询
     * @param params
     * @return
     * @throws Exception
     */
    public List<SqlRow> getInvestorChangeList(Map<String,Object> params) throws Exception {
        String sql = "select bank_code,is_belong,iss_bank_name,iss_bank_code,in_out_sign,iss_country,data_type,ta_id,in_cust_no,cust_type,\n" +
                     "       personal_id_type,organization_id_type,other_id_name,id_code,spv_open_bank,other_open_bank,cust_name,sex,risk_level,moble,\n" +
                     "       tel_phone,email,remark,sllr_cd,deal_dt " +
                     "  from stg_cust_register_info " +
                     " where deal_date = $S{deal_date}";
        return super.findRows(sql,DataSourceProperty.PUB, params);
    }


}
