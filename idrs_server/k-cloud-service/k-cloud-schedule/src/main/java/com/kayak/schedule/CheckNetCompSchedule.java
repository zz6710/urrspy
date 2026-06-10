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
 * function:检验当日产品披露净值数据是否完整
 */
@Controller
public class CheckNetCompSchedule extends MyJob {
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
            checkNetValComplete();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 功能：每日跑  检验当日产品披露净值数据是否完整
     * 作者：rennannan
     * 日期：20210626
     *
     * @throws Exception
     */
    public void checkNetValComplete() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        // 添加到任务表
        ServerUtil.requestPost("PmsApp", "com.kayak.pms.netValue.model.T8ProdNetValueNotice", "autoCheckNetComplete", param);
    }
}
