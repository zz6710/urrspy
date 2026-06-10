package com.kayak.schedule.action;
import com.alibaba.fastjson.JSON;
import com.kayak.schedule.biz.MyJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Component
public class ExecSynchUserAction extends MyJob {
	@Autowired
	private RestTemplate restTemplate;
	@Override
	public void execute(JobExecutionContext context) {

		try {
			if (checkIp()) {
				ResponseEntity<String> body = restTemplate.exchange("http://PmsApp/synch/user.json",HttpMethod.POST,null, String.class);
				log.info("调用PmsApp同步用户返回",JSON.toJSONString(body));
			}

		} catch (Exception e) {
			log.error("调用PmsApp同步用户异常【{}】",e.toString());
		}
	}

}
