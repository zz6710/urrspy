package com.kayak.dps.direct.model;
// default package

/**
 * ClearStepSub entity. @author MyEclipse Persistence Tools
 */

public class ClearStepSub implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer stepNo;
	private Integer stepSubNo;
	private String workdate;
	private String busiCode;
	private String execStatus;
	private String stepSubName;
	private String execType;
	private String className;
	private String methodName;
	private String serviceCode;
	private String businessCode;
	private String remark;
	private String reStep;
	
	// Constructors
	/** default constructor */
	public ClearStepSub() {
	}

	/** full constructor */
	public ClearStepSub(Integer stepNo,Integer stepSubNo, String workdate, String busiCode,
			String execStatus, String stepSubName, String execType,
			String className, String methodName, String serviceCode,
			String businessCode, String reStep) {
		setStepNo(stepNo);
		setStepSubNo(stepSubNo);
		this.workdate = workdate;
		this.busiCode = busiCode;
		this.execStatus = execStatus;
		this.stepSubName = stepSubName;
		this.execType = execType;
		this.className = className;
		this.methodName = methodName;
		this.serviceCode = serviceCode;
		this.businessCode = businessCode;
		this.reStep = reStep;
	}

	// Property accessors
	public Integer getStepNo() {
		return stepNo;
	}

	public void setStepNo(Integer stepNo) {
		this.stepNo = stepNo;
	}

	public Integer getStepSubNo() {
		return stepSubNo;
	}

	public void setStepSubNo(Integer stepSubNo) {
		this.stepSubNo = stepSubNo;
	}
	
	public String getWorkdate() {
		return this.workdate;
	}

	public void setWorkdate(String workdate) {
		this.workdate = workdate;
	}

	public String getBusiCode() {
		return this.busiCode;
	}

	public void setBusiCode(String busiCode) {
		this.busiCode = busiCode;
	}

	public String getExecStatus() {
		return this.execStatus;
	}

	public void setExecStatus(String execStatus) {
		this.execStatus = execStatus;
	}

	public String getStepSubName() {
		return this.stepSubName;
	}

	public void setStepSubName(String stepSubName) {
		this.stepSubName = stepSubName;
	}

	public String getExecType() {
		return this.execType;
	}

	public void setExecType(String execType) {
		this.execType = execType;
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getServiceCode() {
		return this.serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getBusinessCode() {
		return this.businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReStep() {
		return reStep;
	}

	public void setReStep(String reStep) {
		this.reStep = reStep;
	}


}