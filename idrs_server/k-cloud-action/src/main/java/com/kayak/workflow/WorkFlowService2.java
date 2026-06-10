package com.kayak.workflow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.server.ServerUtil;
import com.kayak.workflow.constants.WfFieldConstants;
import com.kayak.workflow.exception.WorkflowException;
import com.kayak.workflow.exception.WorkflowExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 工作流发起
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WorkFlowService2 {

    @Value("${spring.application.name}")
    public String appName;

    /**
     * 开启工作流
     *
     * @param wfBusiConfig
     * @param businessParams
     * @throws Exception
     */
    public JSONObject start(SqlRow wfBusiConfig , Map<String, Object> businessParams) throws Exception {
        businessParams.put(WfFieldConstants.PROCESS_NAME, wfBusiConfig.getString("process_name"));
        businessParams.put(WfFieldConstants.BUSI_UN_KEY, wfBusiConfig.getString("bus_keys"));

        // 初始化参数
        businessParams.put(WfFieldConstants.SERVER, appName);
        businessParams.put(WfFieldConstants.URL, businessParams.remove("url"));

        // 非请求参数
//        businessParams.remove("action");
//        businessParams.remove("modelClassName");
//        businessParams.remove("oldData");

        // 发起工作流
        JSONObject params = (JSONObject) JSON.toJSON(businessParams);
        String strResult = ServerUtil.requestPostForm("WorkflowServer",
                "/flow/startFlow.json", params, SysUtil.getLoginUserid());
        JSONObject result = JSON.parseObject(strResult);
        // 返回结果校验
        if (isFail(result)) {
            throw new WorkflowException("流程发起失败:" + result.get("returnmsg"));
        }
        return (JSONObject) JSON.toJSON(result);
    }


    /**
     * 流程返回结果校验
     * @param result
     * @return
     */
    protected boolean isFail(Map<String, Object> result) {
        return result == null || !StringUtils.equals("200", (String) result.get("status"));
    }


}
