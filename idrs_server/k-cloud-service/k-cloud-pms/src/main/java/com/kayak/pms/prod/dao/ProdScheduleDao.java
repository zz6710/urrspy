package com.kayak.pms.prod.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.prod.model.ProdSchedule;
import org.springframework.stereotype.Repository;

/**
 * com.kayak.pms.prod.dao
 * user:rennannan
 * date:2021/3/15 14:18
 * function:产品进度表dao  记录关键节点日期
 */
@Repository
public class ProdScheduleDao extends ComnDao {
    /**
     *功能：根据产品id与节点查询进度信息是否存在
     * 作者：rennannan
     * 日期：20210315
     */
    public int findProdScheduleCount(ProdSchedule param) throws Exception {
        String sql="select count(1) cou" +
                "     from t8_prod_schedule_info " +
                "    where t8_prod_info_id = $S{t8ProdInfoId} " +
                "      and node_id=$S{nodeId}";
        return super.findRow(sql,param).getInteger("cou");
    }

    /**
     *功能：根据产品代码与节点查询进度信息是否存在
     * 作者：rennannan
     * 日期：20210315
     */
    public int findScheduleCountByCode(ProdSchedule param) throws Exception {
        String sql="select count(1) cou" +
                "     from t8_prod_schedule_info " +
                "    where prod_code = $S{prodCode} " +
                "      and node_id=$S{nodeId}";
        return super.findRow(sql,param).getInteger("cou");
    }
    /**
     * 功能：插入产品进度操作信息
     * 作者：rennannan
     * 日期：20210315
     * @param param
     * @return
     * @throws Exception
     */
    public int insertProdSchedule(ProdSchedule param) throws Exception {
        String sql="insert into t8_prod_schedule_info(id,t8_prod_info_id,prod_code,node_id,business_date," +
                                                    " crt_date,crt_time,crt_user,upd_date,upd_time,upd_user)" +
                " values($AUTOIDS{t8_prod_schedule_info},$S{t8ProdInfoId},$S{prodCode},$S{nodeId},$S{businessDate}," +
                " $S{crtDate},$S{crtTime},$S{crtUser},$S{updDate},$S{updTime},$S{updUser})";
        return super.update(sql,param).getEffect();
    }

    /**
     * 功能：根据产品代码与节点修改产品进度信息
     * 作者：rennannan
     * 日期：20210315
     * @return
     */
    public int updateProdScheduleByCode(ProdSchedule param)throws Exception {
        String sql="update t8_prod_schedule_info " +
                     " set business_date = $S{businessDate}," +
                     "     upd_date=$S{updDate}," +
                     "     upd_time=$S{updTime}," +
                     "     upd_user=$S{updUser}" +
                   " where prod_code=$S{prodCode}" +
                   "   and node_id=$S{nodeId}";
        return super.update(sql,param).getEffect();
    }

    /**
     * 功能：根据产品id与节点修改进度信息
     * 作者：rennannan
     * 日期：20210315
     * @param param
     * @return
     * @throws Exception
     */
    public int updateProdScheduleByInfoId(ProdSchedule param)throws Exception {
        String sql="update t8_prod_schedule_info " +
                " set business_date = $S{businessDate}," +
                "     upd_date=$S{updDate}," +
                "     upd_time=$S{updTime}," +
                "     upd_user=$S{updUser}" +
                " where t8_prod_info_id=$S{t8ProdInfoId}" +
                "   and node_id=$S{nodeId}";
        return super.update(sql,param).getEffect();
    }
}
