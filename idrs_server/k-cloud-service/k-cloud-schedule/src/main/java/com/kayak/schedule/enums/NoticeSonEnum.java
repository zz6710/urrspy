package com.kayak.schedule.enums;

public enum NoticeSonEnum {
	
	


	doc_1_产品季报("1", "季报"),
	doc_2_产品半年报("2", "半年报"),
	doc_3_产品年报("3", "年报"),
	doc_4_产品月报("4", "月报"),
	
	doc_6_公司半年报("6", "公司半年报"),
	doc_7_公司年报("7", "公司年报");
	
	
	
	NoticeSonEnum(String val, String desc) {
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
	public static NoticeSonEnum getNoticeType (String val) {
		for (NoticeSonEnum type :NoticeSonEnum.values()) {
			if (val.equals(type.getVal()))
				return type;
		}
		return null;
	}
}