package com.kayak.clear.dataSync.utils;

import com.kayak.clear.dataSync.service.TtrdInstitutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 任务注册定时器
 *
 */
@Configuration
public class TaskRegistMonitorUtil {

    private final Logger log = LoggerFactory.getLogger(TaskRegistMonitorUtil.class);
    @Autowired
    private ApplicationContext ac;

    /* dps定时任务,资管数据在交易工作时间段每半小时同步一次 */
    @Scheduled(cron = "${pms.scheduled.do_task.interval}")
    public void doRegistProcess() {

        // 获取任务注册业务处理 service，内部成员变量多，service不通过@Autowired注入
        TtrdInstitutionService ttrdInstitutionService = ac.getBean(TtrdInstitutionService.class);
        try {
            //同步资管映射和机构数据
            ttrdInstitutionService.syncZGTradeRelationInfo();
        } catch (Exception e) {
            log.error("资管同步定时任务出错！错误信息:[{}]", e.getMessage(), e);
        }
    }
}