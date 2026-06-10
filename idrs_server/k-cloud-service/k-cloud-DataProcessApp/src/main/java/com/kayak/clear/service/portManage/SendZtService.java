package com.kayak.clear.service.portManage;


import com.kayak.base.dao.ComnDao;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.business.BusinessBaseTaskService;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.clear.utils.TaskUtil;
import com.kayak.config.constants.STGConstants;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.dps.ods.service.DealPortFileService;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
import com.kayakwise.kcloud.batch.service.BaseTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Component
@Scope("prototype")
public class SendZtService extends BaseTaskService<PubReq, PubResp> {
    @Autowired
    public CreateTaskService createTaskService;

    @Autowired
    private DealPortFileService dealPortFileService;

    @Autowired
    private ComnDao comnDao;

    String workDate = "";

    @Override
    protected void doCheckParams(PubReq request) throws Exception {
        beforeClear(request);
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

    @StepNo(stepNo = 1)
    protected void process(PubReq request) throws Exception {
        log.info("--------发送文件到中台开始------");
        log.info(request.getTaskId() + " Request: {}", request);
        Map<String, Object> params =new HashMap<>();
        params.put("portType", STGConstants.STG_PORT_TYPE_DC);//接口为中台
        params.put("portDir", STGConstants.STG_DATA_DIR_SEND);//方向为发送
        params.put("deal_date", request.getTaskDate());//处理日期
        params.put("dealType", STGConstants.STG_DATA_HANDLE_DAYS);//按天处理
        String sqlCol = ExeQuery.queryExeId("SENDEU01");
        List<SqlRow> sqlRows = comnDao.findRows(sqlCol,params);
        try {
            for (SqlRow sqlRow : sqlRows) {
                params.put("dealDate", sqlRow.getString("Dates"));//处理日期
                dealPortFileService.dealAllPortInfo(params);
            }
        }catch (Exception e){
            log.error("[URRS-ERROR-H]{}日任务{}执行失败：{}",request.getTaskDate(),request.getTaskId(),e.getMessage());
            throw e;
        }
        log.info("--------发送文件到中台结束------");
    }

    /**
     * 清算流程启动之前 调用
     * 验证清算检查前 的 准备工作是否完成
     * 包含数据初始化工作 （axin）
     * @throws Exception
     */
    public void beforeClear(PubReq request) throws Exception{

        //参数初始化
        workDate=request.getTaskDate();

        if("".equals(workDate)||workDate==null){
            throw new Exception("报送工作日不能为空。");
        }

    }
}
