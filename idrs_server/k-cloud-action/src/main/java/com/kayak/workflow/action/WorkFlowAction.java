package com.kayak.workflow.action;

import com.kayak.core.action.BaseController;
import com.kayak.core.util.Tools;
import com.kayak.workflow.WorkFlowService;
import com.kayak.workflow.cache.WfBusinessConfigCache;
import com.kayak.workflow.dao.WorkFlowDao;
import com.kayak.workflow.model.WfTransConfig;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * 开启工作流
 * @author xiamh
 * @date 2021/1/6
 */
//@RestController
public class WorkFlowAction extends BaseController {

    Logger logger = LoggerFactory.getLogger(WorkFlowAction.class);

    @Autowired
    private WorkFlowService workFlowService;

    @Autowired
    private WorkFlowDao workFlowDao;

    @Autowired
    private WfBusinessConfigCache wfBusinessConfigCache;


    @RequestMapping(value = "/flow/reload.json")
    public String dictReload() {
        try {
            wfBusinessConfigCache.initCache();
            log.info("工作流刷新缓存成功");
            return updateSuccess();
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
    }

    /**
     * 检查是否需要开启工作流
     * @return
     */
    @PostMapping(value = "/flow/checkAndStart.json")
    public Object checkAndStart(@RequestBody String body) throws Exception {
        Map<String, Object> params = Tools.json2map(new JSONObject(body));
        WfTransConfig wfTransConfig = workFlowDao.getTransConfigByTransCode(Tools.obj2Str(params.get("transCode")));

        // 判断是否走工作流，从缓存中
        Map<String,Object> returndata = new HashMap();
        if (wfTransConfig != null) {
            try {
                workFlowService.start(wfTransConfig, params);
                returndata.put("isStartFlow", true);
                return updateSuccess("流程开启成功", returndata);
            } catch (Exception e) {
                e.printStackTrace();
                return updateFailure("流程开启失败");
            }
        }
        returndata.put("isStartFlow", false);
        return updateSuccess("交易不用审批", returndata);
    }


}
