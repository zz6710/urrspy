package com.kayak.schedule.model;

import lombok.Data;

@Data
public class NoticeModel  {

	//公告信息
	private String t8DisclosureNoticeId;
	//产品代码
	private String prodCode;
	//文档版本
	private String version;
	//文件名称
	private String fileName;
	//文件路径
	private String filePath ;
	//信披类型
	private String disclosureType ;
	//信批子类型
	private String disclosureSonType;
	//注册登记编码
	private String registCode;
}
