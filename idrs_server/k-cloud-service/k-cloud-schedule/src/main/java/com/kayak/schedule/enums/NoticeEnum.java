package com.kayak.schedule.enums;

public enum NoticeEnum {
	
	


	doc_1_产品销售文件("1", "产品销售文件"),
	doc_2_产品发行公告("2", "产品发行公告"),
	doc_3_产品到期公告("3", "产品到期公告"),
	doc_4_运作公告("4", "运作公告"),
	doc_5_定期报告("5", "定期报告"),
	doc_6_整体报告("6", "整体报告"),
	doc_7_重大报告("7", "重大报告"),
	doc_8_临时公告("8", "临时公告"),
	doc_9_净值报告("9", "净值报告"),
	doc_11_手工报告("11", "手工报告");
		
	NoticeEnum(String val, String desc) {
		this.val = val;
		this.desc = desc;
	}

	private String val;
	private String desc;

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static NoticeEnum getNoticeType (String val) {
		for (NoticeEnum type :NoticeEnum.values()) {
			if (val.equals(type.getVal()))
				return type;
		}
		return null;
	}
}