package com.kayak.dps.ods.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.ods.model.BaseEtlSet;
import org.springframework.stereotype.Repository;

@Repository
public class BaseEtlSetDao extends ComnDao {

    public SqlResult<BaseEtlSet> findT8OdsSyncSets(SqlParam<BaseEtlSet> params) throws Exception {
        return super.findRows("SELECT id,`database`,sysdesc,sharding,tablename,table_cloumns,sql_order,select_condition,exec_time,increment_flag,date_condition,date_end_condition,limits,status,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,remark FROM base_etl_set", params);
    }

    public UpdateResult addT8OdsSyncSet(SqlParam<BaseEtlSet> params) throws Exception {
        return super.update("INSERT INTO base_etl_set(`database`,sysdesc,sharding," +
                        "tablename,table_cloumns,sql_order,select_condition,exec_time,increment_flag,date_condition,date_end_condition,limits,status,crt_date,crt_time," +
                        "crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,remark)" +
                        " VALUES($S{database},$S{sysdesc},$S{sharding},$S{tablename}," +
                        "$S{tableCloumns},$S{sqlOrder},$S{selectCondition},$S{execTime},$S{incrementFlag},$S{dateCondition},$S{dateEndCondition},$S{limits},$S{status},$S{crtDate},$S{crtTime}," +
                        "$S{crtUserId},$S{crtUserName},$S{updDate},$S{updTime},$S{updUserId},$S{updUserName},$S{remark})",
                params.getModel());
    }

    public UpdateResult updateT8OdsSyncSet(SqlParam<BaseEtlSet> params) throws Exception {
        return super.update("UPDATE base_etl_set SET `database`=$S{database} ,sysdesc=$S{sysdesc} ,sharding=$S{sharding} ,tablename=$S{tablename} ,table_cloumns=$S{tableCloumns} ,sql_order=$S{sqlOrder} ,select_condition=$S{selectCondition},exec_time=$S{execTime},increment_flag=$S{incrementFlag},date_condition=$S{dateCondition},date_end_condition=$S{dateEndCondition},limits=$S{limits} ,status=$S{status} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,crt_user_name=$S{crtUserName} ,remark=$S{remark}  WHERE  id=$S{id} ",
                params.getModel());
    }

    public UpdateResult deleteT8OdsSyncSet(SqlParam<BaseEtlSet> params) throws Exception {
        return super.update("DELETE FROM base_etl_set WHERE  id=$S{id} ",
                params.getModel());
    }

    public int stopStatus(SqlParam<BaseEtlSet> params) throws Exception {
        return super.update(
                "UPDATE base_etl_set SET status='P' WHERE id=$S{id}",
                params.getModel()).getEffect();
    }

    public int recoverStatus(SqlParam<BaseEtlSet> params) throws Exception {
        return super.update(
                "UPDATE base_etl_set SET status='N' WHERE id=$S{id}",
                params.getModel()).getEffect();
    }

    public SqlResult<BaseEtlSet> getDataBase(SqlParam<BaseEtlSet> params) throws Exception {
        return super.findRows("select distinct `database` from base_etl_set ",params);
    }
}
