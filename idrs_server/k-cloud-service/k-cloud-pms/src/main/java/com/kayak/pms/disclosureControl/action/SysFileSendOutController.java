package com.kayak.pms.disclosureControl.action;

import com.kayak.pms.disclosureControl.service.DisclosureNoticeDocOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysFileSendOutController {

	@Autowired
	private DisclosureNoticeDocOperateService disclosureNoticeDocOperateService;

	@RequestMapping("/notice/contentReplace.json")
	public void docContextReplaceTest() throws Exception {
		String temPath = "D:/xpModTemp/";
		String fileModName = "半年度报告_20220406.docx";
		String fileSavePath = "D:/xpModTemp/savePath/";
		String noticeVersionId = "4";
		//获取文件临时地址参数
		disclosureNoticeDocOperateService.replaceNoticeDocGridContent(temPath, fileModName, fileSavePath, noticeVersionId);
	}
}
