package com.kayak.schedule;

import com.kayak.schedule.biz.MyJob;
import com.kayak.server.ServerUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * com.kayak.schedule
 * user:rennannan
 * date:2021/6/26
 * function:每日批量生成净值披露数据和任务
 */
@Controller
public class NetValTaskNoticeSchedule extends MyJob {
    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        super.execute(context);
        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            generateNetValDays();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 功能：每日跑  生成净值披露任务和数据
     * 作者：rennannan
     * 日期：20210626
     *
     * @throws Exception
     */
    public void generateNetValDays() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        // 添加到任务表
        ServerUtil.requestPost("PmsApp", "com.kayak.pms.netValue.model.T8ProdNetValueNotice", "autoGenerateNetByDay", param);
    }
}
