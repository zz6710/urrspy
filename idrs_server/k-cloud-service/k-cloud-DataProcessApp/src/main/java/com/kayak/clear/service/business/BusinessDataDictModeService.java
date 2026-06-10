package com.kayak.clear.service.business;


import com.kayak.clear.req.PubReq;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;


//数据加工公共类  --axin 20220719


@Slf4j
@Component
@Scope("prototype")
public class BusinessDataDictModeService extends BusinessBaseTaskService{

    @Autowired
    private JedisPool jedisPool;

    @StepNo(stepNo = 1)
    protected void stepProcess(PubReq request) throws Exception{
        try {
            super.dataModeConvert(request);
        } catch (Exception e) {
            log.error("[URRS-ERROR-H]{}日任务{}执行失败：{}",request.getTaskDate(),request.getTaskId(),e.getMessage());
            throw e;
        }


    }

}
