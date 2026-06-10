package com.kayak.clear.service.business;

import com.kayak.base.dao.ComnDao;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
import com.kayakwise.kcloud.batch.service.BaseTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 批处理-中债切日
 * axin
 * 20220713
 */
@Slf4j
@Component
@Scope("prototype")
public class DebtCutWorkDate extends BaseTaskService<PubReq, PubResp> {
    @Autowired
    public ComnDao comnDao;

    @Autowired
    public CreateTaskService createTaskService;

    //中债当前日期
    String workDate = "";
    String nextWorkDate = "";

    @Override
    protected void doCheckParams(PubReq request) throws Exception {
//        workDate = SysUtil.getSystemParamsByParaid("app_workDate");
//        nextWorkDate = SysUtil.getSystemParamsByParaid("app_next_workdate"); // 系统下一工作日
//        if ("".equals(workDate))
//            throw new Exception("中债工作日不可以为空");
//        if ("".equals(nextWorkDate))
//            throw new Exception("中债工作日不可以为空");
    }

    @Override
    protected void doCheckBusiness(PubReq request) throws Exception {
        doCheckBusiness();
        log.info(" ###### 业务校验开始 ");
    }

    @Override
    protected RegistClearTaskPojo taskRegist(TaskRegeditReq taskRegeditReq) throws Exception {
        log.info(" ###### 任务注册");
        return createTaskService.createSystemTask(taskRegeditReq);
    }

    //切日
    @StepNo(stepNo = 1)
    protected void debtCutWorkDateStep(PubReq request) throws Exception{
            System.err.println(request.getTaskDate());
//        comnDao.doTrans(() -> {
//
//            //下下一工作日取值为next_workdate+1
//            String sNextNextWorkdate = DateUtil.add(nextWorkDate, "yyyyMMdd", 1);
//
//            //(1)更新上一系统工作日为当前系统工作日
//            String sql = "UPDATE sys_param a SET paravalue=$S{workDate} WHERE moduleid='R' AND paraid='app_pre_workDate'";
//            comnDao.update(sql,workDate);
//            //(2)更新当前系统工作日为下一系统工作日
//            sql = "UPDATE sys_param a SET paravalue=$S{nextWorkdate} WHERE moduleid='R' AND paraid='app_workDate'";
//            comnDao.update(sql,nextWorkDate);
//            //(3)更新下一系统工作日为下下一系统工作日
//            sql = "UPDATE sys_param a SET paravalue=$S{sNextNextWorkdate} WHERE moduleid='R' AND paraid='app_next_workDate'";
//            comnDao.update(sql,sNextNextWorkdate);
//
//        });
    }

    //验证如果中债 报送没有完成，不让切日
    //一二期做了在搞这个验证
    public void doCheckBusiness(){


    }

}
