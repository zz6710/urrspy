package com.kayak.pms.prod.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.prod.model.CreativitySeminar;
import com.kayak.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Map;


/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/1/6 18:26
 *
 */
@Repository
public class CreativitySeminarDao extends ComnDao {

    public String addSeminarInfo(SqlParam<CreativitySeminar> params) throws Exception {
        return super.update("INSERT INTO t8_creativity_seminar (ID, SEMINAR_NAME, SEMINAR_DATE, SEMINAR_TIME, SEMINAR_ADDR, PARTICIPANT,seminar_status, INPUTUSER, UPDATEUSER, CRT_DATE, CRT_TIME, UPD_DATE, UPD_TIME)\n" +
                "VALUES ($AUTOIDS{seminarId}, $S{seminarName}, $S{seminarDate}, $S{seminarTime}, $S{seminarAddr}, $S{participant}, '1',$S{inputuser}, null, $S{crtDate}, $S{crtTime}, null, null)", params.getModel()).getAutoId();

    }

    public UpdateResult updateSeminarInfo(SqlParam<CreativitySeminar> params) throws Exception {
        return super.update("UPDATE t8_creativity_seminar SET  SEMINAR_NAME=$S{seminarName}, SEMINAR_DATE=$S{seminarDate}, SEMINAR_TIME=$S{seminarTime}, SEMINAR_ADDR=$S{seminarAddr}, PARTICIPANT=$S{participant}, UPDATEUSER=$S{updateuser}, UPD_DATE=$S{updDate}, UPD_TIME=$S{updTime} where id=$S{id}", params.getModel());

    }
    
    public UpdateResult delSeminarInfo(SqlParam<CreativitySeminar> params) throws Exception {
        return super.update("delete from t8_creativity_seminar where id=$S{id}", params.getModel());

    }

    public SqlResult<Map<String, Object>> findSeminarInfo(Map<String, Object> parameters) throws Exception {
        String sql = "select t.id,t.seminar_name,t.seminar_date,t.seminar_time,t.seminar_addr,t.participant," +
                     " t.seminar_status,t.crt_Date,t.crt_time,su.username inputuser from t8_creativity_seminar t" +
                     " left join sys_user su on t.inputuser = su.userid or t.inputuser = su.username where 1=1 ";
                     if(StringUtils.isNotBlank((String) parameters.get("seminarName"))){
                         sql = sql +" and t.SEMINAR_NAME like '%" + parameters.get("seminarName")+ "%'";
                     }
                     if(StringUtils.isNotBlank((String)parameters.get("seminarDate"))){
                         sql = sql +" and t.SEMINAR_DATE = '"+parameters.get("seminarDate")+"'";
                     }
                     if(StringUtils.isNotBlank((String) parameters.get("originalityName"))){
                         sql = sql + " AND t.id IN ( SELECT DISTINCT seminar_id id FROM t8_prod_creative_project WHERE originality_name LIKE '%" + parameters.get("originalityName")+ "%'  )";


                     }
        SqlResult<Map<String, Object>> sqlRowSqlResult = SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, parameters, this);
        return sqlRowSqlResult;

    }
}
