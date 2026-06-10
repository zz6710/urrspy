package com.kayak.pms.chinaBondSubmit.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.chinaBondSubmit.model.TrClearLog;
import org.springframework.stereotype.Repository;

@Repository
public class TrClearLogDao extends ComnDao {

    public SqlResult<TrClearLog> findTrClearLogs(SqlParam<TrClearLog> params) throws Exception {
        return super.findRows("SELECT trans_serno,step_no,step_sub_no,workdate,busi_code,exec_date,start_time,end_time,exec_status,rtn_code,upd_date,upd_time,file_name,file_serno,recordnum,tano,rtn_desc FROM tr_clear_log", params);
    }

    public UpdateResult addTrClearLog(SqlParam<TrClearLog> params) throws Exception {
        return super.update("INSERT INTO tr_clear_log(trans_serno,step_no,step_sub_no,workdate,busi_code,exec_date,start_time,end_time,exec_status,rtn_code,upd_date,upd_time,file_name,file_serno,recordnum,tano,rtn_desc) VALUES($S{transSerno},$S{stepNo},$S{stepSubNo},$S{workdate},$S{busiCode},$S{execDate},$S{startTime},$S{endTime},$S{execStatus},$S{rtnCode},$S{updDate},$S{updTime},$S{fileName},$S{fileSerno},$S{recordnum},$S{tano},$S{rtnDesc})",
                params.getModel());
    }

    public UpdateResult updateTrClearLog(SqlParam<TrClearLog> params) throws Exception {
        return super.update("UPDATE tr_clear_log SET trans_serno=$S{transSerno} ,step_no=$S{stepNo} ,step_sub_no=$S{stepSubNo} ,workdate=$S{workdate} ,busi_code=$S{busiCode} ,exec_date=$S{execDate} ,start_time=$S{startTime} ,end_time=$S{endTime} ,exec_status=$S{execStatus} ,rtn_code=$S{rtnCode} ,upd_date=$S{updDate} ,upd_time=$S{updTime} ,file_name=$S{fileName} ,file_serno=$S{fileSerno} ,recordnum=$S{recordnum} ,tano=$S{tano} ,rtn_desc=$S{rtnDesc}  WHERE ",
                params.getModel());
    }

    public UpdateResult deleteTrClearLog(SqlParam<TrClearLog> params) throws Exception {
        return super.update("DELETE FROM tr_clear_log WHERE ",
                params.getModel());
    }

}
