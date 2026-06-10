package com.kayak.rpt.zz.feedback.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.feedback.dao.TrClearLogDao;
import com.kayak.rpt.zz.feedback.model.TrClearLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "清算流水服务", model = TrClearLog.class)
public class TrClearLogService {

    @Autowired
    private TrClearLogDao trClearLogDao;

    @API(desc = "查询清算流水信息", auth = APIAuth.YES)
    public SqlResult<TrClearLog> findTrClearLogs(SqlParam<TrClearLog> params) throws Exception {
        params.setMakeSql(true);
        return trClearLogDao.findTrClearLogs(params);
    }

    @API(desc = "添加清算流水", params = "trans_serno,step_no,step_sub_no,workdate,busi_code,exec_date,start_time,end_time,exec_status,rtn_code,upd_date,upd_time,file_name,file_serno,recordnum,tano,rtn_desc", auth = APIAuth.NO)
    public int addTrClearLog(SqlParam<TrClearLog> params) throws Exception {
        return trClearLogDao.addTrClearLog(params).getEffect();
    }

    @API(desc = "修改清算流水", params = "trans_serno,step_no,step_sub_no,workdate,busi_code,exec_date,start_time,end_time,exec_status,rtn_code,upd_date,upd_time,file_name,file_serno,recordnum,tano,rtn_desc", auth = APIAuth.NO)
    public int updateTrClearLog(SqlParam<TrClearLog> params) throws Exception {
        return trClearLogDao.updateTrClearLog(params).getEffect();
    }

    @API(desc = "删除清算流水", params = "trans_serno,step_no,step_sub_no,workdate,busi_code,exec_date,start_time,end_time,exec_status,rtn_code,upd_date,upd_time,file_name,file_serno,recordnum,tano,rtn_desc", auth = APIAuth.NO)
    public int deleteTrClearLog(SqlParam<TrClearLog> params) throws Exception {
        return trClearLogDao.deleteTrClearLog(params).getEffect();
    }

}
