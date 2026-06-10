package com.kayak.login.action;

public enum INITIALTYPENUM {

	IS_INITIAL_PWD("0", "初始密码"),

	NO_INITIAL_PWD("1", "已修改密码");
	
	
	
	INITIALTYPENUM(String val, String desc) {
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
