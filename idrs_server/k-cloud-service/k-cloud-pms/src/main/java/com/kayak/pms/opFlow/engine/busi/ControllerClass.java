package com.kayak.pms.opFlow.engine.busi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * Created by daniel on 06/06/2017.
 */
@Controller
public class ControllerClass {
    private static Logger logger = LoggerFactory.getLogger(ControllerClass.class);

    /**
     * 自定义流程不做业务数据处理，只简单显示流程完成信息即可
     */
    public void controllerMethod() {
        // 业务回调
        //CommonWorkflowCallback commonWorkflowCallback = (CommonWorkflowCallback) ClassHelper.newInstance("com.kayak.opFlow.busi.CommonWorkflowCallback");
    }

}
