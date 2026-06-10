package com.kayak.workflow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateFormatEnum;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.server.ServerUtil;
import com.kayak.workflow.constants.WfBusinessStatus;
import com.kayak.workflow.constants.WfFieldConstants;
import com.kayak.workflow.constants.WfProcessInstanceStatusConstant;
import com.kayak.workflow.dao.WorkFlowDao;
import com.kayak.workflow.exception.WorkflowExceptionEnum;
import com.kayak.workflow.model.WfBusinessConfig;
import com.kayak.workflow.model.WfBusinessExtend;
import com.kayak.workflow.model.WfTransConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class WorkFlowService {

    @Autowired
    private WorkFlowDao workFlowDao;

    /**
     * 开启工作流
     *
     * @param wfBusinessConfig
     * @param businessParams
     * @throws Exception
     */
    public JSONObject start(WfBusinessConfig wfBusinessConfig, Map<String, Object> businessParams) throws Exception {

        businessParams.put(WfFieldConstants.PROCESS_NAME, wfBusinessConfig.getProcessName());
        businessParams.put(WfFieldConstants.BUSI_TABLE_NAME, wfBusinessConfig.getTableName());
        businessParams.put(WfFieldConstants.BUSI_TABLE_PrimaryKey, wfBusinessConfig.getBusKeys());

        String values = getKeyValues(wfBusinessConfig.getBusKeys(), businessParams);
        if (Tools.isNotEmpty(values) && valueRepeat(wfBusinessConfig.getServer(), values)) {
            WorkflowExceptionEnum.DUPLICATE_LAUNCH.throwException();
        }
        WfBusinessExtend wfBusinessExtend = addProcessRecord(wfBusinessConfig.getServer(), "", "", values);
        JSONObject startResult;
        try {
            startResult = doStart(businessParams);
        } catch (Exception e) {
            workFlowDao.delWfBusinessExtend(wfBusinessExtend.getExtendId());
            throw e;
        }

        if (isFail(startResult)) {
            workFlowDao.delWfBusinessExtend(wfBusinessExtend.getExtendId());
            WorkflowExceptionEnum.FAIL_LAUNCH.throwException();
        }

        JSONObject resultData = startResult.getObject("data", JSONObject.class);
        workFlowDao.updateWfBusinessExtend(wfBusinessExtend.getExtendId(),
                resultData.getString("processId"),
                resultData.getString("processInstanceId"),
                WfProcessInstanceStatusConstant.RUNNING);
        return startResult;
    }

    /**
     * 开启工作流
     *
     * @param wfTransConfig
     * @param params
     * @throws Exception
     */
    public JSONObject start(WfTransConfig wfTransConfig, Map<String, Object> params) throws Exception {

        params.put(WfFieldConstants.PROCESS_NAME, wfTransConfig.getProcessName());
        params.put(WfFieldConstants.BUSI_TABLE_NAME, wfTransConfig.getTableName());
        params.put(WfFieldConstants.BUSI_TABLE_PrimaryKey, wfTransConfig.getBusKeys());

        String values = getKeyValues(wfTransConfig.getBusKeys(), params);
        if (Tools.isNotEmpty(values) && valueRepeat(wfTransConfig.getTransCode(), values)) {
            WorkflowExceptionEnum.DUPLICATE_LAUNCH.throwException();
        }
        WfBusinessExtend wfBusinessExtend = addProcessRecord(Tools.obj2Str(params.get("transCode")), Tools.obj2Str(params.get("serverName")), Tools.obj2Str(params.get("url")), "");
        JSONObject startResult;
        try {
            startResult = doStart(params);
        } catch (Exception e) {
            workFlowDao.delWfBusinessExtend(wfBusinessExtend.getExtendId());
            throw e;
        }

        if (isFail(startResult)) {
            workFlowDao.delWfBusinessExtend(wfBusinessExtend.getExtendId());
            WorkflowExceptionEnum.FAIL_LAUNCH.throwException();
        }

        JSONObject resultData = startResult.getObject("data", JSONObject.class);
        workFlowDao.updateWfBusinessExtend(wfBusinessExtend.getExtendId(),
                resultData.getString("processId"),
                resultData.getString("processInstanceId"),
                WfProcessInstanceStatusConstant.RUNNING);
        return startResult;
    }

    private WfBusinessExtend addProcessRecord(String server, String appName, String url, String values)
            throws Exception {
        Date now = new Date();
        WfBusinessExtend model = WfBusinessExtend.builder()
                .busStatus(WfBusinessStatus.READY)
                .keysValue(values)
                .processStatus(WfProcessInstanceStatusConstant.PREPARATION)
                .server(server)
                .appName(appName)
                .url(url)
                .userid(SysUtil.getLoginUserid())
                .startDate(DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT))
                .startTime(DateUtil.formatDate(now, DateFormatEnum.TIME_FORMAT)).build();
        String id = workFlowDao.addWfBusinessExtend(model);
        model.setExtendId(id);
        return model;
    }

    /**
     * 调用工作流服务，开启工作流
     *
     * @param businessParams
     * @return
     */
    protected JSONObject doStart(Map<String, Object> businessParams) {
        JSONObject params = (JSONObject) JSON.toJSON(businessParams);
        params.remove("action");
        params.remove("modelClassName");
        params.remove("oldData");
//        Map<String, Object> result = (Map<String, Object>) ServerUtil.requestPostForm("WorkflowServer",
//                "/processInstance/startAndExecute.json", params, SysUtil.getLoginUserid());
        String result = ServerUtil.requestPostForm("WorkflowServer",
                "/processInstance/startAndExecute.json", params, SysUtil.getLoginUserid());
        return JSONObject.parseObject(result);
//        return JSON.parseObject(result, Map.class);
    }

    protected boolean isFail(JSONObject result) {
        return result == null || !"200".equals(result.getString("status"));
    }

    private String getKeyValues(String busKeys, Map<String, Object> businessParams) {
        if (Tools.isBlank(busKeys)) {
            return null;
        }
        String[] keys = busKeys.split(",");
        List<String> values = new ArrayList<>(keys.length);
        for (String key : keys) {
            if (Tools.isBlank(key)) {
                continue;
            }
            String value = (String) businessParams.get(key);
            if (Tools.isNotBlank(value)) {
                values.add(value);
            }
        }
        return values.stream().collect(Collectors.joining(","));
    }

    private boolean valueRepeat(String server, String values) throws Exception {
        List<WfBusinessExtend> workFlowDaoExtends = workFlowDao.findNotFinishData(
                WfBusinessExtend.builder()
                        .server(server)
                        .keysValue(values).build());
        return !CollectionUtils.isEmpty(workFlowDaoExtends);
    }

    public List<SqlRow> getActor(String processInstanceId) throws Exception {
       return StringUtils.isBlank(processInstanceId)? null:workFlowDao.getActor(processInstanceId);
    }


    /**
     * 通过角色id获取登录名称
     * @param actorId
     */
    public List<SqlRow>  getLoginNameByroleId(String actorId)  {
        try {
           return  workFlowDao.getLoginNameByroleId(actorId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通过用户id获取登录名称
     * @param actorId
     */
    public SqlRow getLoginNameByuserId(String actorId) {
        try {
            return workFlowDao.getLoginNameByuserId(actorId);
        } catch (Exception e) {
            return null;
        }
    }

    public SqlRow getProcessInfo(String processId) throws Exception {
        return workFlowDao.getProcessById(processId);
    }

    public List<SqlRow> getSubmitParamsByProcessInstanceId(String processInstanceId) throws Exception {
        return workFlowDao.getSubmitParamsByProcessInstanceId(processInstanceId);
    }

    public List<SqlRow> findProdUserInfo(HashMap<String, Object> queryCriteria) throws Exception {
        return workFlowDao.findProdUserInfo(queryCriteria);
    }

    public SqlRow getAppDisplay(String processInstanceId) throws Exception {
        return workFlowDao.getAppDisplay(processInstanceId);
    }
}
