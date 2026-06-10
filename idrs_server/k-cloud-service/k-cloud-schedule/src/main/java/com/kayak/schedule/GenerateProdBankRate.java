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
public class GenerateProdBankRate extends MyJob {
    /**
     * 功能：跑一次历史数据，然后每天跑  生成银行间利率
     *
     * @throws Exception
     */
    @Override
    public void execute(JobExecutionContext context) {
        try {
            if (checkIp()) {
                Map<String, Object> param = new HashMap<>();
                super.execute(context);
                ServerUtil.requestPost("PmsApp", "com.kayak.pms.schedule.model.BankRate", "syncBankRate", param);
            }
        } catch (Exception e) {
            log.error("每天定时同步 【{}】", e.toString());
        }
    }

}
