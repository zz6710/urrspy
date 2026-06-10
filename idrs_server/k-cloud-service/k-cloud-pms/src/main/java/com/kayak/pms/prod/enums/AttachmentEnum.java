package com.kayak.pms.prod.enums;

public enum AttachmentEnum {

	ATTACHMENT_10001("10001", "产品发行审批文件"),

	ATTACHMENT_10002("10002", "产品终版参数表文件"),
	
	ATTACHMENT_10003("10003", "产品创意会文件"),
	
	ATTACHMENT_10004("10004", "产品创意附件文件");
	
	
	
	AttachmentEnum(String val, String desc) {
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

	public static AttachmentEnum getEiamFlagEnum(String value) {
		AttachmentEnum[] enums = AttachmentEnum.values();
		for (AttachmentEnum attachEnum : enums) {
			if (attachEnum.getVal().equals(value)) {
				return attachEnum;
			}
		}
		return null;
	}
}
