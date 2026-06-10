package com.kayak.schedule;

import com.kayak.schedule.biz.MyJob;
import com.kayak.server.ServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description : 运作公告定期发送邮件
 * @Author: wangchenglin
 * @Date: 2022/2/10 9:22
 */
@Slf4j
@Component
public class SendWithNoticeSchedule extends MyJob {
    @Override
    public void execute(JobExecutionContext context) {
        try {
            if (checkIp()) {
                Map<String, Object> param = new HashMap<>();
                super.execute(context);
                ServerUtil.requestPost("PmsApp", "com.kayak.pms.netWorthOperation.model.T8ProdNavOperation", "sendNetWorthOperation", param);
            }
        } catch (Exception e) {
            log.error("每天定时扫描 【{}】", e.toString());
        }
    }
}
