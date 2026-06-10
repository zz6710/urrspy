package com.kayak.pms.prod.dao;


import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.prod.model.ProdOriginality;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProdOriginalityDao extends ComnDao {

    public int addOriginality (ProdOriginality prodOriginality) throws Exception {
        return super.update(
                "INSERT INTO t8_prod_creative_project (id, seminar_id, originality_name, originality_type, inventor, status, update_date, update_time, inputuser)\n" +
                     "VALUES ($AUTOIDS{originalityId}, $S{seminarId}, $S{originalityName}, $S{originalityType}, $S{inventor}, $S{status}, $S{updateDate}, $S{updateTime}, $S{inputuser})",
                prodOriginality).getEffect();
    }
}
