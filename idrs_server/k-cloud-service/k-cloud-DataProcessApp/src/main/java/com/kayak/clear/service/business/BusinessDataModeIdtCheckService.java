package com.kayak.clear.service.business;


import com.kayak.clear.req.PubReq;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * 处理投资者三期数据生僻字
 */
@Slf4j
@Component
@Scope("prototype")
public class BusinessDataModeIdtCheckService extends BusinessBaseTaskService{

    @StepNo(stepNo = 1)
    protected void stepProcess(PubReq request) throws Exception{
        try {
            List<Map<String, String>> newInvList = super.doRareCharacterHandle();//获取投资者身份信息
            super.doRareCharacterUpdate(newInvList);//更新投资者生僻字数据
        }catch (Exception e){
            log.error("[URRS-ERROR-H]{}日任务{}执行失败：{}",request.getTaskDate(),request.getTaskId(),e.getMessage());
            throw e;
        }

    }


}
