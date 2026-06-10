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
 * function:发送当日披露产品净值文件到净值对应邮箱
 */
@Controller
public class SendNetEmailSchedule extends MyJob {
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
            sendNetEmails();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 功能：每日跑  发送当日披露产品净值文件到净值对应邮箱
     * 作者：rennannan
     * 日期：20210628
     *
     * @throws Exception
     */
    public void sendNetEmails() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        // 添加到任务表
        ServerUtil.requestPost("PmsApp", "com.kayak.pms.netValue.model.T8ProdNetValueNotice", "sendEmails", param);
    }
}
