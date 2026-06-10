package com.kayak.dps.app.service;

import com.alibaba.fastjson.JSONObject;
import com.kayak.base.dao.ComnDao;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.clear.utils.DateUtils;
import com.kayak.config.constants.STGConstants;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.ods.service.DealPortFileService;
import com.kayak.server.ServerUtil;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
import com.kayakwise.kcloud.batch.service.BaseTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
@Scope("prototype")
public class AutoGeneratePerfmTasksService extends BaseTaskService<PubReq, PubResp> {

    @Autowired
    public CreateTaskService createTaskService;
    @Autowired
    public ComnDao comnDao;
    @Autowired
    private DealPortFileService dealPortFileService;


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
    * @功能描述:业绩比较基准上下限文件自动发送→cisp
    */
    @StepNo(stepNo = 1)
    protected void autoGenerateExpireTasks(PubReq request) throws Exception{
        log.info("---------- 任务: " + request.getTaskId() +" 业绩比较基准上下限文件→cisp Start -----------");
        Map<String, Object> params =new HashMap<>();
        params.put("portType", STGConstants.STG_PORT_TYPE_CSP);
        params.put("portDir", STGConstants.STG_DATA_DIR_SEND);
        params.put("dealDate", DateUtil.getNowDate());//处理日期
        params.put("dealType", STGConstants.STG_DATA_HANDLE_DAYS);//按天处理
        params.put("pId", request.getTaskId());//接口配置的任务ID
        try {
            dealPortFileService.dealAllPortInfo(params);
            //处理ok文件
        }catch (Exception e){
            log.error("业绩比较基准上下限文件→cisp出错！错误信息:[{}]", e.getMessage(), e);
            throw e;
        }
        log.info("---------- 任务: " + request.getTaskId() +" 业绩比较基准上下限文件→cisp End-----------");
    }

}