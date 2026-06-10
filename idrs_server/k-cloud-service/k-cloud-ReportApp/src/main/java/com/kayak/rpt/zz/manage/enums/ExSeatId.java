package com.kayak.rpt.zz.manage.enums;

/**
 * ExSeatId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ExSeatId implements java.io.Serializable {

	// Fields

	private String fcode;
	private String extpid;
	private String tcode;

	// Constructors

	/** default constructor */
	public ExSeatId() {
	}

	/** full constructor */
	public ExSeatId(String fcode,String tcode, String extpid) {
		this.fcode = fcode;
		this.extpid = extpid;
		this.tcode=tcode;
	}

	// Property accessors

	public String getFcode() {
		return this.fcode;
	}

	public void setFcode(String fcode) {
		this.fcode = fcode;
	}
	public String getExtpid() {
		return this.extpid;
	}

	public void setExtpid(String extpid) {
		this.extpid = extpid;
	}

	public String getTcode() {
		return tcode;
	}

	public void setTcode(String tcode) {
		this.tcode = tcode;
	}

}