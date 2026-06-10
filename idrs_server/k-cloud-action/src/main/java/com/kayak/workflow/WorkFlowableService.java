package com.kayak.workflow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kayak.core.sql.SqlRow;
import com.kayak.server.ServerUtil;
import com.kayak.workflow.constants.WfFieldConstants;
import com.kayak.workflow.exception.WorkflowException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author yuanjinqiao
 * @description
 * @create 2022-09-14 18:26
 **/
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WorkFlowableService {
    @Value("${spring.application.name}")
    public String appName;

    /**
     * 开启工作流
     *
     * @param wfBusiConfig
     * @param businessParams
     * @throws Exception
     */
    public JSONObject start(SqlRow wfBusiConfig, Map<String, Object> businessParams) throws Exception {
        businessParams.put(WfFieldConstants.PROCESS_KEY, wfBusiConfig.getString("process_key"));
        businessParams.put(WfFieldConstants.BUSI_UN_KEY, wfBusiConfig.getString("bus_keys"));

        // 初始化参数
        businessParams.put(WfFieldConstants.SERVER, appName);
        businessParams.put(WfFieldConstants.URL, businessParams.remove("url"));

        // 发起工作流
        JSONObject params = (JSONObject) JSON.toJSON(businessParams);

        Object strResult = ServerUtil.requestPostJson("WorkflowServer", "/process/start.json", params);

        JSONObject result = JSON.parseObject(strResult.toString());

        if (result == null || 200 != ((Integer) result.get("code"))) {
            throw new WorkflowException("流程发起失败:" + result.get("returnmsg"));
        }
        return (JSONObject) JSON.toJSON(result);
    }
}
