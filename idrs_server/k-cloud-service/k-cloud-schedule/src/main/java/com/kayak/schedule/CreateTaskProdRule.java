package com.kayak.schedule;

import com.kayak.server.ServerUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CreateTaskProdRule {
	

	public void run() {
		try {
			generateTask();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 功能：系统自动跑批生成产品信披规则，每天
	 *
	 * @throws Exception
	 */
	public void generateTask() throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		// 添加到任务表
		ServerUtil.requestPost("PmsApp", "com.kayak.pms.basePublish.model.DisclosureRule", "getProdDisRule", param);
	}
}
