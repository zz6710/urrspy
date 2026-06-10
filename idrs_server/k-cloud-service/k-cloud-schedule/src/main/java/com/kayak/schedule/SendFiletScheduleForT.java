package com.kayak.schedule;

import com.alibaba.fastjson.JSON;
import com.kayak.schedule.biz.MyJob;
import com.kayak.server.ServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;



/**
 * 205向外部发送邮件
 */
@Slf4j
@Component
public class SendFiletScheduleForT extends MyJob {

	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		try {

			if (checkIp()) {
				ResponseEntity<String> body = restTemplate.exchange("http://PmsApp/email/sendFile.json",HttpMethod.POST,null, String.class);
				log.info("调用PmsApp 流程处理任务process返回【{}】",JSON.toJSONString(body));
		
			}
		} catch (Exception e) {
			log.error("发送文件任务异常【{}】", e.toString());
		}
	}

}
