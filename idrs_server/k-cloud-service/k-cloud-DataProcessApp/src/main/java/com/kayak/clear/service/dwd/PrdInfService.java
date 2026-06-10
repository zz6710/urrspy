//package com.kayak.clear.service.dwd;
//
//
//import com.kayak.base.dao.ComnDao;
//import com.kayak.clear.req.PubReq;
//import com.kayak.clear.resp.PubResp;
//import com.kayak.clear.service.pub.CreateTaskService;
//import com.kayak.core.sql.SqlRow;
//import com.kayak.core.util.ExeQuery;
//import com.kayak.core.util.DateUtil;
//import com.kayak.dps.ods.dao.DealDwdPortDao;
//import com.kayakwise.kcloud.batch.annotation.StepNo;
//import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
//import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
//import com.kayakwise.kcloud.batch.service.BaseTaskService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 批处理-产品信息
// */
//@Component
//@Scope("prototype")
//public class PrdInfService extends BaseTaskService<PubReq, PubResp> {
//
//    private static Logger log = LoggerFactory.getLogger(com.kayak.clear.service.dwd.PrdInfService.class);
//
//    @Resource(name = "dealDwdPortDao")
//    private DealDwdPortDao dealDwdPortDao;
//    @Autowired
//    public CreateTaskService createTaskService;
//    @Autowired
//    public ComnDao comnDao;
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
//    protected void demo2(PubReq request) throws Exception{
//        String sysDate = DateUtil.getSysWordDay();
//        log.info("----------产品信息模型生成Start -----------");
//        String insertString= ExeQuery.queryExeId("IT09PRODBASE");
//        String deleteString=ExeQuery.queryExeId("DE09PRODBASE");
//        Map<String, Object> params=new HashMap<String, Object>();
//        params.put("deal_date",sysDate);
//        // params.put("deal_date","20220621");
//        params.put("CRT_DT", new SimpleDateFormat("yyyyMMdd").format(new Date()));//创建日期
//        params.put("UPD_DT", new SimpleDateFormat("yyyyMMdd").format(new Date()));//更新时间
//        comnDao.update(deleteString,params);
//        comnDao.update(insertString,params);
//        log.info("----------产品信息模型生成End -----------");
//    }
//
//
//}
