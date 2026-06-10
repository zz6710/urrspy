package com.kayak.schedule;

import com.kayak.schedule.biz.MyJob;
import com.kayak.server.ServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * com.kayak.schedule
 * function:定时更新
 */
@Slf4j
@Component
public class UpdateDisclosureNticeData extends MyJob {

    /**
     * 功能：每天更新前三日信披公告数据
     *
     * @throws Exception
     */
    @Override
    public void execute(JobExecutionContext context) {
        try {
            if (checkIp()) {
                Map<String, Object> param = new HashMap<>();
                super.execute(context);
                ServerUtil.requestPost("PmsApp", "com.kayak.pms.basePublish.model.DisclosureDataSet", "updDisclosurenNoticeData", param);
            }
        } catch (Exception e) {
            log.error("日志备份 【{}】", e.toString());
        }
    }

}