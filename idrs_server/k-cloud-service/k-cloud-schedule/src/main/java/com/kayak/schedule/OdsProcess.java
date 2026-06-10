package com.kayak.schedule;
import com.kayak.schedule.biz.MyJob;
import com.kayak.server.ServerUtil;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class OdsProcess extends MyJob {
	 /**
     * 功能：每天定时跑批，同步数仓数据
     *
     * @throws Exception
     */
    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
    	try {
    		if(checkIp()) {
    			super.execute(context);
            	ServerUtil.requestPost("PmsApp", "com.kayak.pms.schedule.model.T8OdsSyncSet", "odsAutoSynData", new HashMap());
    		}
        } catch (Exception e) {
           log.error("每天定时跑批，同步数仓数据异常【{}】",e.toString());
        }
    }

}
