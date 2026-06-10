package com.kayak.pms.prod.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.prod.model.ProdStatusChange;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * com.kayak.pms.prod.dao
 * user:rennannan
 * date:2021/2/19 14:10
 * function:产品状态调整信息记录
 */
@Repository
public class ProdStatusChangeDao extends ComnDao {
    /**
     * 功能：根据产品id查询所有该产品调整记录
     * 日期：20210219
     * 作者：rennannan
     */
     public SqlResult<ProdStatusChange> findProdStatusChange(SqlParam<ProdStatusChange> param) throws Exception {
         String sql = "select t1.id,t1.t8_prod_info_id,t1.prod_code,t1.adjust_type,t1.adjust_cause,t1.crt_date,t1.crt_time,su.username crt_user,t2.assembly_desc" +
                 "       from t8_prod_adjust t1 left join t8_prod_assembly_info t2 on t1.adjust_type = t2.assembly_id  " +
                 "       left join sys_user su on t1.crt_user = su.userid or t1.crt_user = su.username" +
                 "      where t8_prod_info_id = $S{t8ProdInfoId}" +
                 "    order by crt_date desc, crt_time desc";
         return super.findRows(sql,param);
     }
    /**
     * 功能：插入调整信息
     * 日期：20210219
     * 作者：rennannan
     */
    public String insertProdStatusChange(ProdStatusChange param) throws Exception {
        String sql = "insert into t8_prod_adjust(id,t8_prod_info_id,prod_code,adjust_type,adjust_cause,crt_date,crt_time,crt_user)" +
                "           values($AUTOIDS{t8_prod_adjust},$S{t8ProdInfoId},$S{prodCode},$S{adjustType},$S{adjustCause},$S{crtDate},$S{crtTime},$S{crtUser})";
        return super.update(sql,param).getAutoId();
    }

    public SqlResult<ProdStatusChange> findDataParams(SqlParam<ProdStatusChange> param) throws Exception {
        String sql = "SELECT\n" +
                "  COLUMN_NAME \"prod_code\",\n" +
                "  COLUMN_TYPE \"adjust_type\",\n" +
                "  COLUMN_COMMENT \"id\"\n" +
                "FROM\n" +
                "    information_schema. COLUMNS\n" +
                "WHERE TABLE_SCHEMA = 'pms_disclosure' AND TABLE_NAME = 't8_prod_info' ";
        return super.findRows(sql,param);
    }

    public void updateProdCode(List<String> list) throws Exception {
        for(int i=0;i<list.size();i++){
            super.update(list.get(i),null);
        }

    }
}
