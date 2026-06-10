package com.kayak.schedule;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kayak.core.sql.SqlRow;
import com.kayak.schedule.biz.MyJob;
import com.kayak.schedule.dao.HealthyDao;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Component
public class CheckServerScheduled extends MyJob {
	 
	
	  @Autowired
	  private HealthyDao healthyDao;
	  
	  @Autowired
	  private RestTemplate restTemplate;
	  /**
	     * 功能：检查服务信息
	     *
	     * @throws Exception
	     */
	@Value("${kayak.server.shell.path}")
	private String shellPath;
	  
    
		@Override
	    public void execute(JobExecutionContext context) {
    	try {
    		 super.execute(context);
    		 List<SqlRow> appList = healthyDao.findAll();
    		 appList.forEach((item)->{
    			 String server = item.getString("app_name");
    			 final String excShell = shellPath+server+File.separator+"start.sh";


                 try {
                     Object obj = restTemplate.postForObject("http://" + server + "/heathy.json",null,Object.class);
                     HashMap<String,Object> result = JSON.parseObject(JSONObject.toJSONString(obj), HashMap.class);
                     if(!(Boolean)result.get("success")) {
                    	log.info("服务异常,{},执行脚本，{}",server,excShell);
                    	Runtime.getRuntime().exec(excShell);
                     }

                 }catch (Exception e){
                	
                	 try {
                		log.info("未找到该服务,{}执行脚本，{}",server,excShell);
						Runtime.getRuntime().exec(excShell);
					} catch (IOException e1) {
						 log.error("执行脚本异常,{}",e1);
					}
                     
                 }
              });
        } catch (Exception e) {
           log.error("检查服务信息异常【{}】",e.toString());
        }
    }

}