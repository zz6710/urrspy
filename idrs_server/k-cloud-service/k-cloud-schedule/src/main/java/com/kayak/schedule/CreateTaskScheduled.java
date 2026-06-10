package com.kayak.schedule;

import java.util.*;
import com.kayak.server.ServerUtil;
import org.springframework.stereotype.Component;
@Component
public class CreateTaskScheduled {
	

	public void run() {
		try {
			generateTask();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 功能：系统自动跑批，每个月25号（可配）跑下个月批量
	 *
	 * @throws Exception
	 */
	public void generateTask() throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		// 添加到任务表
		ServerUtil.requestPost("PmsApp", "com.kayak.pms.disclosureControl.model.DisclosureProdTask", "autoAddDisclosureTasks", param);
	}
}
