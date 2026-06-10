package com.kayak.context;

import cn.hutool.core.collection.CollectionUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.rpt.email.dao.EmailBizTaskDao;
import com.kayak.rpt.email.model.EmailBizTask;
import com.kayak.rpt.email.service.EmailBizTaskService;
import com.kayak.subject.service.RptBusinessBaseTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tools.ant.taskdefs.email.EmailTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

/**
 * 邮件监测定时任务
 */
@Slf4j
@Component
//@Configuration
public class EmailScheduled {
    @Autowired
    private EmailBizTaskService emailBizTaskService;

    @Autowired
    private RptBusinessBaseTaskService rptBusinessBaseTaskService;

    @Autowired
    private EmailBizTaskDao emailBizTaskDao;

    /**
     * 在配置中心配置对应的参数
     * 定时扫描业务表，获取要执行任务的记录。 每隔5分钟扫描一次，仅扫描标记为 需要临时执行的
     *
     * @throws Exception
     */
//    @Scheduled(cron = "${email.scheduled.cron:0 */2 * * * * }")
    @Scheduled(cron = "${email.scheduled.cron}")
//    @Scheduled(cron = "${email.scheduled.cron}")
    public void exec() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("paravalue", "1");
        map.put("oldParavalue", "0");
        map.put("paraid", "90000151810");

        if ("1".equals(SysUtil.getSystemParamsByParaid("90000151810"))) {
            log.info("发邮件任务，锁被占用，不处理");
            return;
        }
        if (rptBusinessBaseTaskService.upTaskStatus(map) > 0) {
            map.put("paravalue", "0");
            map.put("oldParavalue", "1");
        } else {
            log.info("发邮件任务，锁被占用，不处理");
            return;
        }

        //查询业务表，获取记录 需要立即执行任务的记录
        EmailBizTask params = new EmailBizTask();
        //查询 需要立即执行任务的 业务记录
        params.setTaskFlag("1");
        List<EmailBizTask> bizInfoList = emailBizTaskDao.findEmailBizTaskList(params);
        if (CollectionUtil.isEmpty(bizInfoList)) {
            log.info("没有需要调用的业务流程");
            rptBusinessBaseTaskService.upTaskStatus(map);
            return;
        }
        /**
         * 循环每笔发起业务回调
         */
        for (EmailBizTask emailBizTask : bizInfoList) {
            try {
                EmailThreadPoolUtils.execute(() -> {
                    try {
                        emailBizTaskService.execute(emailBizTask);
                    } catch (Exception e) {
                        // 业务处理报错
                        log.error("业务方法执行失败: ", e.getMessage());
                    }
                });
            } catch (RejectedExecutionException e) {
                // 阻塞队列已满后恢复为准备状态
                log.error(" 执行失败: ", e);
            }
        }
        //释放
        rptBusinessBaseTaskService.upTaskStatus(map);
    }

}
