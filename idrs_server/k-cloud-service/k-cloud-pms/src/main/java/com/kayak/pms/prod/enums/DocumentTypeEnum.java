package com.kayak.pms.prod.enums;

public enum DocumentTypeEnum {

	DOCUMENT_报告主体文件("报告主体文件", "'10005','20005','30005','40005','50005','60005','70005'"),
	
	DOCUMENT_可行性报告("可行性报告", "'10004','20004','30004','40004','50004','60004','70004'"),
	
	DOCUMENT_托管协议("托管协议", "'10002','20002','30002','40002','50002','60002','70002'"),
	
	DOCUMENT_产品说明书("产品说明书 ","'10001','20001','30001','40001','50001','60001','70001'");

	
	
	
	
	DocumentTypeEnum(String type, String desc) {
		this.type = type;
		this.val = desc;
	}

	private String type;
	private String val;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public static String getEnum(String type) {
		DocumentTypeEnum[] enums = DocumentTypeEnum.values();
		for (DocumentTypeEnum attachEnum : enums) {
			if (attachEnum.getType().equals(type)) {
				return attachEnum.val;
			}
		}
		return null;
	}
}
