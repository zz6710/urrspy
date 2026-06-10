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
 * date:2021/6/28
 * function:更新当日披露产品净值数据
 */
@Controller
public class UpdateNetSchedule extends MyJob {
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
            updateValData();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 功能：每日跑  更新当日产品披露净值数据
     * 作者：rennannan
     * 日期：20210626
     *
     * @throws Exception
     */
    public void updateValData() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        // 添加到任务表
        ServerUtil.requestPost("PmsApp", "com.kayak.pms.netValue.model.T8ProdNetValueNotice", "sysNoticeNetValue", param);
    }
}
