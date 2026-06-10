//package com.kayak.clear.service.dwd;
//
//
//import com.kayak.base.dao.ComnDao;
//import com.kayak.clear.req.PubReq;
//import com.kayak.clear.resp.PubResp;
//import com.kayak.clear.service.pub.CreateTaskService;
//import com.kayak.core.util.ExeQuery;
//import com.kayak.core.util.DateUtil;
//import com.kayakwise.kcloud.batch.annotation.StepNo;
//import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
//import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
//import com.kayakwise.kcloud.batch.service.BaseTaskService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 批处理-产品限制信息
// */
//@Slf4j
//@Component
//@Scope("prototype")
//public class PrdLmtInfService extends BaseTaskService<PubReq, PubResp> {
//
//    @Autowired
//    public CreateTaskService createTaskService;
//    @Autowired
//    public ComnDao comnDao;
//
//    @Override
//    protected void doCheckParams(PubReq request) throws Exception {
//        log.info(" ###### 参数校验 ");
//    }
//
//    @Override
//    protected void doCheckBusiness(PubReq request) throws Exception {
//        log.info(" ###### 业务校验 ");
//    }
//
//    @Override
//    protected RegistClearTaskPojo taskRegist(TaskRegeditReq taskRegeditReq) throws Exception {
//        log.info(" ###### 任务注册");
//        return createTaskService.createSystemTask(taskRegeditReq);
//    }
//
//    @StepNo(stepNo = 1)
//    protected void dealPordLimitInfo(PubReq request) throws Exception{
//        //直接使用insertselect 写法
//        String sysDate = DateUtil.getSysWordDay();
//        log.info("----------产品限制信息模型生成Start -----------");
//        String insertString= ExeQuery.queryExeId("IT02PRODLIM");
//        String deleteString=ExeQuery.queryExeId("DE02PRODLIM");
//        Map<String, Object> params=new HashMap<String, Object>();
//        params.put("deal_date",sysDate);
//        // params.put("deal_date","20220621");
//        params.put("CRT_DT", new SimpleDateFormat("yyyyMMdd").format(new Date()));//创建日期
//        params.put("UPD_DT", new SimpleDateFormat("yyyyMMdd").format(new Date()));//更新时间
//        comnDao.update(deleteString,params);
//        comnDao.update(insertString,params);
//        log.info("----------产品限制信息模型生成End -----------");
//    }
//}
