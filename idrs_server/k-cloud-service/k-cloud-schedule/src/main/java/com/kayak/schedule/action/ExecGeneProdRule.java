package com.kayak.schedule.action;

import com.kayak.core.system.SysBeans;
import com.kayak.schedule.CreateTaskScheduled;
import com.kayak.schedule.biz.MyJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

/**
 * com.kayak.schedule.action user:ouyifan date:2022/6/15  function:生成产品信披任务
 */
@Slf4j
@Component
public class ExecGeneProdRule extends MyJob {
	@Override
	public void execute(JobExecutionContext context){
		
		try {
			if(checkIp()) {
				super.execute(context);
				CreateTaskScheduled date = SysBeans.getBean("createTaskProdRule");
				date.run();
			}
		} catch (Exception e) {
			log.error("生成产品信披任务异常【{}】",e);
		}
	}
}
