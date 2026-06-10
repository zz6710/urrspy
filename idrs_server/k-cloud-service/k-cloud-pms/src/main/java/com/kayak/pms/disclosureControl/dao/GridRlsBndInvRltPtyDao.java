package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.disclosureControl.model.GridFbassetHoldAnalysis;
import com.kayak.pms.disclosureControl.model.GridRlsBndInvRltPty;
import org.springframework.stereotype.Repository;

@Repository
public class GridRlsBndInvRltPtyDao extends ComnDao {


    public SqlResult<GridRlsBndInvRltPty> findGridRlsBndInvRltPty(SqlParam<GridRlsBndInvRltPty> params) throws Exception {
        return super.findRows("select  id, notice_version_id, deal_dt, order_no, affiliate_name, securities_code, securities_name, deal_amount, party_relation from  app_grid_rls_bnd_inv_rlt_pty_data  order by deal_dt,order_no+0",DataSourceProperty.IDB, params);
    }

    public int updateGridRlsBndInvRltPty(SqlParam<GridRlsBndInvRltPty> params) throws Exception {
        String sql = "update app_grid_rls_bnd_inv_rlt_pty_data " +
                "      set notice_version_id = $S{noticeVersionId}," +
                "  deal_dt=$S{dealDt}," +
                "  order_no=$S{orderNo}," +
                "  affiliate_name=$S{affiliateName}," +
                "  securities_code = $S{securitiesCode}," +
                "  securities_name = $S{securitiesName}," +
                "  deal_amount = $S{dealAmount}," +
                "  party_relation = $S{partyRelation}" +
                "    where id=$S{id}";
        return super.update(sql,DataSourceProperty.IDB,params.getModel()).getEffect();
    }

    public int deleteGridRlsBndInvRltPty(SqlParam<GridRlsBndInvRltPty> params)  throws Exception {
        return super.update("delete from  app_grid_rls_bnd_inv_rlt_pty_data where id=$S{id}",DataSourceProperty.IDB,params.getModel()).getEffect();
    }
}
