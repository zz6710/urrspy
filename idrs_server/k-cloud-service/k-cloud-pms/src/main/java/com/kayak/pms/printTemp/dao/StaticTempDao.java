package com.kayak.pms.printTemp.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.pms.printTemp.model.PrintTempVersion;
import com.kayak.pms.printTemp.model.StaticTemp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @program: k-cloud
 * @description: 静态文档Dao
 * @author: WangZhenXin
 * @create: 2021-01-02 10:17
 * @memo 备注信息
 */
@Repository
public class StaticTempDao extends ComnDao {
    private static final Logger logger = LoggerFactory.getLogger(StaticTempDao.class);

    public SqlResult<StaticTemp> find(SqlParam<StaticTemp> params) throws Exception {
        return super.findRows("select "
        		+ "  IFNULL(e.TRUTEE_NAME,d.DISTRIBUTOR_NAME) distributor_trutee_name," +
                "    t.id, " +
                "    t.temp_type, " +
                "    t.temp_name, " +
                "    t.doc_type, " +
                "    t.remark, "+
                "    t.distributor_code, " +
                "    t.t8_trutee_info_id, " +
                "    t.create_date, " +
                "    t.create_time, " +
                "    t.update_date, " +
                "    t.update_time," +
                "    t.create_user_name " +
                " from t8_static_temp t "
                + " left join t8_trutee_info e on t.t8_trutee_info_id = e.id "
                + " left join t8_distributor_info d on t.distributor_code = d.distributor_code" +
                " order by t.temp_type", params);
    }

    //根据销售商代码查询数据
    public List<SqlRow> findByDistributorCode(String distributorCode) throws Exception {

        Map<String, Object> params = new HashMap<>(1);
        params.put("distributorCode", distributorCode);
        return super.findRows("select t.id,t.distributor_code from t8_static_temp t where t.distributor_code=$S{distributorCode}", params);
    }

    public Integer checkStaticTemp(StaticTemp staticTemp){
        Integer cont = 0;
        try {
            List<SqlRow> rows = super.findRows("select count(t.id) cont " +
                            "from t8_static_temp t " +
                            "where t.doc_type = $S{docType} " +
                            "  and t.temp_type = $S{tempType} " +
                            "  and t.temp_name = $S{tempName} "
                    , staticTemp);
            if (rows != null && rows.size()>0){
                cont = rows.get(0).getInteger("cont");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return cont;
    }


    public String saveStaticTemp(StaticTemp staticTemp) throws Exception {
       return super.update("insert into t8_static_temp(id, temp_type, temp_name, doc_type ,remark, distributor_code, t8_trutee_info_id, create_date, create_time, create_user_id, create_user_name) " +
                        "VALUES($AUTOIDS{t8_static_temp},$S{tempType},$S{tempName},$S{docType},$S{remark},$S{distributorCode}, $S{t8TruteeInfoId}, $S{createDate},$S{createTime}, $S{createUserId}, $S{createUserName})", staticTemp).getAutoId();
    }

    public StaticTemp getStaticTempByTempId(String staticTempId) throws Exception {
        SqlRow sqlRow = super.findRow("select t.id, " +
                "       t.temp_type, " +
                "       t.temp_name, " +
                "       t.doc_type, " +
                "       t.remark, " +
                "       t.distributor_code, " +
                "       t.t8_trutee_info_id, " +
                "       t.update_date, " +
                "       t.update_time, " +
                "       t.create_date, " +
                "       t.create_time, " +
                "       t.create_user_id, " +
                "       t.create_user_name " +
                "from t8_static_temp t " +
                "where t.id=$S{staticTempId}", staticTempId);
        StaticTemp staticTemp = new StaticTemp();
        staticTemp.setId(sqlRow.getString("id"));
        staticTemp.setDocType(sqlRow.getString("doc_type"));
        staticTemp.setTempType(sqlRow.getString("temp_type"));
        staticTemp.setTempName(sqlRow.getString("temp_name"));
        staticTemp.setRemark(sqlRow.getString("remark"));
        staticTemp.setDistributorCode(sqlRow.getString("distributor_code"));
        staticTemp.setT8TruteeInfoId(sqlRow.getString("t8_trutee_info_id"));
        staticTemp.setCreateDate(sqlRow.getString("create_date"));
        staticTemp.setCreateTime(sqlRow.getString("create_time"));
        staticTemp.setUpdateDate(sqlRow.getString("update_date"));
        staticTemp.setUpdateTime(sqlRow.getString("update_time"));
        staticTemp.setCreateUserId(sqlRow.getString("create_user_id"));
        staticTemp.setCreateUserName(sqlRow.getString("create_user_name"));
        return staticTemp;
    }

    public Integer updateStaticTemp(StaticTemp staticTemp) throws Exception {

        String sql = " UPDATE t8_static_temp SET distributor_code = $S{distributorCode} WHERE id = $S{id}";
        return super.update(sql, staticTemp).getEffect();
    }

}
