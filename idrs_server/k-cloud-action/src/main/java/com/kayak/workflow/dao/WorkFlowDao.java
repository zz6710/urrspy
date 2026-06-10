package com.kayak.workflow.dao;

import com.alibaba.fastjson.JSON;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;
import com.kayak.workflow.constants.WfBusinessStatus;
import com.kayak.workflow.constants.WfProcessInstanceStatusConstant;
import com.kayak.workflow.model.WfBusinessConfig;
import com.kayak.workflow.model.WfBusinessExtend;
import com.kayak.workflow.model.WfTransConfig;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WorkFlowDao extends ComnDao {

    public List<SqlRow> findAllConfig() throws Exception {
        Map<String, Object> params = new HashMap<>(1);
        return super.findRows("SELECT * FROM flow_busi_config", params);
    }

    public SqlResult<WfBusinessExtend> findExtends(SqlParam<WfBusinessExtend> params) throws Exception {
        return super.findRows("SELECT * FROM wf_busi_extend", 0, params);
    }

    public WfTransConfig getTransConfigByTransCode(String transCode) throws Exception {
        return super.findRow(WfTransConfig.class, "SELECT * FROM wf_trans_config a WHERE trans_code = $S{transCode}",0, transCode);
    }

    public List<WfBusinessExtend> findNotFinishData(WfBusinessExtend model) throws Exception {
        return super.findRows(WfBusinessExtend.class, "SELECT * FROM wf_busi_extend " +
                " WHERE server = $S{server} " +
                " AND keys_value = $S{keysValue} " + " AND not" +
                " (" +
                "   (process_status = " + WfProcessInstanceStatusConstant.FINISH + " AND bus_status = " + WfBusinessStatus.FINISH + ")" +
                "   OR process_status = " + WfProcessInstanceStatusConstant.REFUSE +
                "   OR (process_status = " + WfProcessInstanceStatusConstant.FINISH + " AND bus_status = " + WfBusinessStatus.ERROR_CONFIRMED + ")" +
                " ) ORDER BY start_date,start_time", 0, model);
    }

    public List<WfBusinessExtend> findExtends(WfBusinessExtend wfBusinessExtend) throws Exception {
        SqlParam<WfBusinessExtend> params = new FetcherData<>((Map) JSON.toJSON(wfBusinessExtend), WfBusinessExtend.class);
        return this.findExtends(params).getRows();
    }

    public String addWfBusinessExtend(WfBusinessExtend model) throws Exception {
        setUpdateDateTime(model);
        return super.update("INSERT INTO wf_busi_extend (extend_id, server, keys_value, process_id, process_instance_id, process_status, bus_status, userid, start_date, start_time, update_date, update_time, app_name, url) " +
                        " VALUES " +
                        " ($AUTOIDS{extendId}, $S{server}, $S{keysValue}, $S{processId}, $S{processInstanceId}, $S{processStatus}, $S{busStatus}, $S{userid}, $S{startDate}, $S{startTime}, $S{updateDate}, $S{updateTime}, $S{appName}, $S{url})",
                model).getAutoId();
    }

    public int delWfBusinessExtend(String extendId) throws Exception {
        return super.update("DELETE FROM wf_busi_extend WHERE extend_id=$S{extendId}", extendId).getEffect();
    }

    public int updateWfBusinessExtend(String extendId, String processId, String processInstanceId, String processStatus) throws Exception {
        WfBusinessExtend model = new WfBusinessExtend();
        model.setExtendId(extendId);
        model.setProcessId(processId);
        model.setProcessStatus(processStatus);
        model.setProcessInstanceId(processInstanceId);
        setUpdateDateTime(model);
        return super.update("UPDATE wf_busi_extend SET process_instance_id=$S{processInstanceId}, " +
                " process_status=$S{processStatus}, " +
                " process_id=$S{processId}, " +
                " update_date =$S{updateDate}, " +
                " update_time =$S{updateTime} " +
                " WHERE extend_id=$S{extendId}", model).getEffect();
    }

    private void setUpdateDateTime(WfBusinessExtend model) {
        Date now = new Date();
        model.setUpdateDate(Tools.dt2Date1(now));
        model.setUpdateTime(Tools.dt2Time1(now));
    }

    public int updateWfBusinessExtend(String processInstanceId, String newBusStatus,String errMesage) throws Exception {
        WfBusinessExtend model = new WfBusinessExtend();
        model.setBusStatus(newBusStatus);
        model.setBusErr(errMesage);
        model.setProcessInstanceId(processInstanceId);
        setUpdateDateTime(model);
        return super.update("UPDATE wf_busi_extend SET bus_status=$S{busStatus}, " +
                " bus_err =$S{busErr}, " +
                " update_date =$S{updateDate}, " +
                " update_time =$S{updateTime} " +
                " WHERE process_instance_id=$S{processInstanceId}", model).getEffect();
    }

    public int updateWfBusiToProcessingIfReady(String processInstanceId) throws Exception {
        WfBusinessExtend model = new WfBusinessExtend();
        model.setProcessInstanceId(processInstanceId);
        setUpdateDateTime(model);
        return super.update("UPDATE wf_busi_extend SET bus_status=" + WfBusinessStatus.PROCESSING +
                " ,update_date =$S{updateDate}, " +
                " update_time =$S{updateTime} " +
                " WHERE process_instance_id=$S{processInstanceId} AND bus_status = " + WfBusinessStatus.READY, model)
                .getEffect();
    }

    public int updateWfExtendBusinessStatusByProcessId(String processInstanceId, String businessStatus) throws Exception {
        WfBusinessExtend model = new WfBusinessExtend();
        model.setProcessInstanceId(processInstanceId);
        model.setBusStatus(businessStatus);
        setUpdateDateTime(model);
        return super.update("UPDATE wf_busi_extend SET bus_status=$S{busStatus}," +
                " update_date =$S{updateDate}, " +
                " update_time =$S{updateTime} " +
                " WHERE process_instance_id=$S{processInstanceId}", model).getEffect();
    }


    public List<SqlRow> getActor(String processInstanceId) throws Exception {
        HashMap<String, Object> params =  new HashMap<>();
        params.put("processInstanceId", processInstanceId);
        return super.findRows("select a.actor_id,a.actor_type  from wf_task_actor a join wf_task b on b.id = a.task_id where b.process_instance_id = $S{processInstanceId}", params);
    }

    public List<SqlRow> getLoginNameByroleId(String actorId) throws Exception {
        HashMap<String, Object> params =  new HashMap<>();
        params.put("actorId", actorId);
        return super.findRows("select b.loginname from sys_user_role a join sys_user b on a.userid = b.userid where a.roleid = $S{actorId}", params);

    }

    public SqlRow getLoginNameByuserId(String actorId) throws Exception {
        HashMap<String, Object> params =  new HashMap<>();
        params.put("actorId", actorId);
        return super.findRow("select loginname from sys_user where userid = $S{actorId}", params);
    }

    public SqlRow getProcessById(String processId) throws Exception {
        HashMap<String, Object> params =  new HashMap<>();
        params.put("processId", processId);
        return super.findRow("SELECT id, name, display_name, create_time, update_time, creator, json, version, context_id, process_type,\n" +
                " wf_env_id AS env_id, validate_type, validate_sql,is_judge_prod_user, type FROM wf_process wp WHERE id=$S{processId}", params);
    }

    public List<SqlRow> getSubmitParamsByProcessInstanceId(String processInstanceId) throws Exception {
        HashMap<String, Object> params =  new HashMap<>();
        params.put("processInstanceId", processInstanceId);
       return super.findRows("SELECT process_instance_id,submit_params FROM wf_submit_params  WHERE process_instance_id=$S{processInstanceId} ORDER BY create_date DESC, create_time DESC", params);
    }

    public List<SqlRow> findProdUserInfo(HashMap<String, Object> queryCriteria) throws Exception {
         return super.findRows("select a.loginname,a.userid from sys_user a JOIN t8_prod_user b on a.userid = b.userid_a join t8_prod_info c on b.t8_prod_info_id = c.id\n" +
                 " where c.id = $S{t8ProdInfoId} or c.prod_code = $S{prodCode} or c.prod_name = $S{prodName}", queryCriteria);
    }

    public SqlRow getAppDisplay(String processInstanceId) throws Exception {
        HashMap<String, Object> params =  new HashMap<>();
        params.put("processInstanceId", processInstanceId);
        return super.findRow("select app_display from wf_process_instance WHERE id=$S{processInstanceId}", params);
    }
}
