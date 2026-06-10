package com.kayak.dps.app.service;

import com.alibaba.fastjson.JSONObject;
import com.kayak.base.dao.ComnDao;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.server.ServerUtil;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
import com.kayakwise.kcloud.batch.service.BaseTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
@Scope("prototype")
public class AutoGenerateProdTasksService extends BaseTaskService<PubReq, PubResp> {

    @Autowired
    public CreateTaskService createTaskService;
    @Autowired
    public ComnDao comnDao;

    String workDate = "";

    @Override
    protected void doCheckParams(PubReq request) throws Exception {
        log.info(" ###### 参数校验 ");
    }

    @Override
    protected void doCheckBusiness(PubReq request) throws Exception {
        log.info(" ###### 业务校验 ");
    }

    @Override
    protected RegistClearTaskPojo taskRegist(TaskRegeditReq taskRegeditReq) throws Exception {
        log.info(" ###### 任务注册");
        return createTaskService.createSystemTask(taskRegeditReq);
    }

    /**
     * 数据验证
     */
    protected void dataModeCheck(PubReq request) throws Exception{
        log.info("---------- 任务: " + request.getTaskId() +" 数据验证开始 Start -----------");


        log.info("---------- 任务: " + request.getTaskId() +" 数据验证结束 End-----------");
    }
    /**
    * @功能描述:产品系统文件自动发送
    */
    @StepNo(stepNo = 1)
    protected void autoGenerateExpireTasks(PubReq request) throws Exception{
        log.info("---------- 任务: " + request.getTaskId() +" 产品系统文件自动发送 Start -----------");

        Map<String, Object> param = new HashMap<String, Object>();
        Object body=ServerUtil.requestPost("DpsApp", "com.kayak.dps.app.model.ProdInfoOds", "autoGenerateProdFileTask", param);
        Map map = JSONObject.parseObject(JSONObject.toJSONString(body),Map.class) ;
        if (map.get("success").toString().equals("false"))
            throw new Exception(map.get("returnmsg").toString());

        log.info("---------- 任务: " + request.getTaskId() +" 产品系统文件自动发送 End-----------");
    }

}