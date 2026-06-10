package com.kayak.schedule.action;

import com.kayak.core.system.SysBeans;
import com.kayak.schedule.CreateTaskScheduled;
import com.kayak.schedule.biz.MyJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

/**
 * com.kayak.schedule.action user:rennannan date:2021/5/31 19:59 function:生成信披任务
 */
@Slf4j
@Component
public class ExecGeneTaskAction extends MyJob {
	@Override
	public void execute(JobExecutionContext context){
		
		try {
			if(checkIp()) {
				super.execute(context);
				CreateTaskScheduled date = SysBeans.getBean("createTaskScheduled");
				date.run();
			}
		} catch (Exception e) {
			log.error("生成信披任务异常【{}】",e);
		}
	}
}
