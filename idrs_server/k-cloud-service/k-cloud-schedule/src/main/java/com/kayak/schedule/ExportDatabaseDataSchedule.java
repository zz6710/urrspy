package com.kayak.schedule;

import com.kayak.schedule.biz.MyJob;
import com.kayak.server.ServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ExportDatabaseDataSchedule extends MyJob {

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        try {
            if (checkIp()) {
                Map<String, Object> param = new HashMap<>();
                super.execute(context);
                ServerUtil.requestPost("PmsApp", "com.kayak.pms.T81.model.T8ProdInfo", "exportProdDataSchedule", param);
            }
        } catch (Exception e) {
            log.error("导出产品数据定时任务异常【{}】", e.toString());
        }
    }
}
