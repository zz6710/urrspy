package com.kayak.schedule.enums;

public enum ChannelEnum {
	
	


	channel_1_中国光大银行官网("1", "中国光大银行官网"),
	channel_2_光大理财官网("2", "光大理财官网"),
	channel_3_光大理财机构直销渠道("3", "光大理财机构直销渠道"),
	channel_4_光大理财销售渠道("4", "光大理财销售渠道"),
	channel_5_行外代销机构("5", "行外代销机构");
	
	
	ChannelEnum(String val, String desc) {
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

}