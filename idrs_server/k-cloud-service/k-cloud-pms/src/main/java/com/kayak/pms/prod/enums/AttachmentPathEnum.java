package com.kayak.pms.prod.enums;

public enum AttachmentPathEnum {

	PATH_01_报告主文件("01", "报告主文件"),//报告主文件 ： 在其他报备材料下载最新版本的报告主文件

	PATH_02_理财产品可行性评估报告("02", "理财产品可行性评估报告"),//可行性报告： 在其他报备材料下载最新版本的可行性报告文件
	
	PATH_03_内部审核文件("03", "内部审核文件"),//发行审批表、申请发行说明  ：申请发行说明 下载备案审批表，与当前最新发行说明文件
	
	PATH_04_相关人员尽职调查文件("04", "相关人员尽职调查文件"),//尽调文件 ：产品创设 文档管理下载尽调文件（唯一一条），
	
	PATH_05_相关方签署的法律文件("05", "相关方签署的法律文件"),//托管协议 ：下载境内、境外 托管行最新一条数据
	
	PATH_06_理财产品销售文件("06", "理财产品销售文件"),//产品说明书 ： 产品说明最新一版下载 ，与07 为同一文件
	
	PATH_07_理财产品说明书("07", "理财产品说明书"),//产品说明书  ： 产品说明最新一版下载 
	
	PATH_08_理财产品宣传材料("08", "理财产品宣传材料"),//宣传材料  ：产品创设 文档管理下载宣传材料 ，
	
	PATH_09_其他材料("09", "其他材料");//报告材料联络人及联系人方式  ：产品创设 文档管理下载其他材料 ，
	
	
	
	AttachmentPathEnum(String val, String desc) {
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

	public static AttachmentPathEnum getEiamFlagEnum(String value) {
		AttachmentPathEnum[] enums = AttachmentPathEnum.values();
		for (AttachmentPathEnum attachEnum : enums) {
			if (attachEnum.getVal().equals(value)) {
				return attachEnum;
			}
		}
		return null;
	}
}
