package com.kayak.schedule;

import java.util.HashMap;
import java.util.Map;
import com.kayak.server.ServerUtil;

import org.springframework.stereotype.Component;

@Component
public class ExecuteTaskScheduled {
	public void run() {
		try {
			generateNoticeAndData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 功能：每天定时跑批，生成公告
	 *
	 * @throws Exception
	 */
	public void generateNoticeAndData() throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		// 添加到任务表
		ServerUtil.requestPost("PmsApp", "com.kayak.pms.disclosureControl.model.ScheduleNotice", "autoGenerateNotice", param);

	}











}
