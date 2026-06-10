//package com.kayak.clear.service.dwd;
//
//
//import com.kayak.base.dao.ComnDao;
//import com.kayak.clear.exception.ModelDataHandleException;
//import com.kayak.clear.req.PubReq;
//import com.kayak.clear.resp.PubResp;
//import com.kayak.clear.service.pub.CreateTaskService;
//import com.kayak.core.util.ExeQuery;
//import com.kayak.core.util.DateUtil;
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
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 批处理-货币市场基金收益评级表
// */
//@Component
//@Scope("prototype")
//public class FundIncomeService extends BaseTaskService<PubReq, PubResp> {
//
//    private static Logger log = LoggerFactory.getLogger(FundIncomeService.class);
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
//    /**
//     * 贴源层-->模型层加工
//     * @param request
//     * @throws Exception
//     */
//    @StepNo(stepNo = 1)
//    protected void step1Process(PubReq request) throws Exception{
//        log.info("---------- 货币市场基金收益评级数据:贴源层-->模型层:任务 Start -----------");
//        try {
//            Map<String, Object> params=new HashMap<>();
//            params.put("deal_date", DateUtil.getSysWordDay());
//            //执行语句先删后插
//            comnDao.update(ExeQuery.queryExeId("DELMDLFUNDINCOMEEQ01"), params);
//            comnDao.update(ExeQuery.queryExeId("ITMDLFUNDINCOMEEQ01"), params);
//            log.info("---------- 货币市场基金收益评级数据:贴源层-->模型层:任务 End -----------");
//        } catch (Exception e) {
//            throw new ModelDataHandleException("贴源层加工货币市场基金收益评级数据异常:" + e.getMessage());
//        }
//
//    }
//
//
//
//}
