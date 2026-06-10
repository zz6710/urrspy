package com.kayak.clear.service.business;


import com.kayak.clear.req.PubReq;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


//数据加工公共类  --axin 20220719


@Slf4j
@Component
@Scope("prototype")
public class BusinessDataModeLoopService extends BusinessBaseTaskLoopService{

    @StepNo(stepNo = 1)
    protected void stepProcess(PubReq request) throws Exception{
        try {
            super.dataModeLoopConvert(request);
        }catch (Exception e){
            log.error("[URRS-ERROR-H]{}日任务{}执行失败：{}",request.getTaskDate(),request.getTaskId(),e.getMessage());
            throw e;
        }

    }


}
