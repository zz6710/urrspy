package com.kayak.schedule;

import com.kayak.schedule.biz.MyJob;
import com.kayak.server.ServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class SendRegularEmailsSchedule extends MyJob {
    /**
     * 功能：每天跑  发送公告邮箱
     *
     * @throws Exception
     */
    @Override
    public void execute(JobExecutionContext context) {
        try {
            if (checkIp()) {
                Map<String, Object> param = new HashMap<>();
                super.execute(context);
                ServerUtil.requestPost("PmsApp", "com.kayak.pms.disclosureControl.model.`DisclosureNoticeChannel`", "autoSendChannelsEmails", param);
            }
        } catch (Exception e) {
            log.error("每天定时跑批，发送公告邮箱【{}】", e.toString());
        }
    }

}
