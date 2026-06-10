package com.kayak.dps.ods.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class DealDwdPortDao extends ComnDao {

    @Resource(name = "comnDao")
    private ComnDao comnDao;



    public List<SqlRow> selectInfo(String sql,Map<String, Object> params) throws Exception {
        List<SqlRow> sqlRows = comnDao.findRows(sql, params);
        return sqlRows;
    }


    public void insertToTable(String sql,Map<String, Object> sqlRow) throws Exception {
        comnDao.update(sql, sqlRow);
    }

    public void updateDPPLIF(Map<String, Object> tparam) throws Exception {
        //todo
        comnDao.update("update DWD_PRD_PRD_LMT_INF_F set" +
                "IDV_SNG_SSCR_MIN_AMT = $S{IDV_SNG_SSCR_MIN_AMT}" +
                "IDV_SNG_SSCR_INCR_AMT = $S{IDV_SNG_SSCR_INCR_AMT}" +
                "IDV_SNG_SSCR_HI_AMT = $S{IDV_SNG_SSCR_HI_AMT}" +
                "IDV_ACM_BUY_CEIL = $S{IDV_ACM_BUY_CEIL}" +
                "IDV_SNG_PCH_MIN_AMT = $S{IDV_SNG_PCH_MIN_AMT}" +
                "IDV_SNG_PCH_INCR_AMT = $S{IDV_SNG_PCH_INCR_AMT}" +
                "IDV_SNG_PCH_HI_AMT = $S{IDV_SNG_PCH_HI_AMT}", tparam);
    }




    public void updateDPPSI(SqlRow sqlRow) {
    }


    public int deleteInfo(String sql,String srl_nbr) throws Exception {
        return super.update(sql, srl_nbr).getEffect();
    }


    public void updateDPPBI(String sql,Map<String, Object> sqlRow) throws Exception {
        comnDao.update(sql, sqlRow);
    }

    /**
     * 处理主体评级转化成数据字典
     * @param main_rat
     */
    public SqlRow dealMainRat(String main_rat) throws Exception {
        return comnDao.findRow("select itemkey  from   sys_dict_item where dict = 'mainRating'  and  itemval = $S{main_rat} ", main_rat);
    }
}
