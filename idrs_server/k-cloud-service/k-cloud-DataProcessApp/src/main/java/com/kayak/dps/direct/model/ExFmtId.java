package com.kayak.dps.direct.model;

/**
 * ExFmtId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ExFmtId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String exfmtid;
	private String itmnm;

	// Constructors

	/** default constructor */
	public ExFmtId() {
	}

	/** full constructor */
	public ExFmtId(String exfmtid, String itmnm) {
		this.exfmtid = exfmtid;
		this.itmnm = itmnm;
	}

	// Property accessors

	public String getExfmtid() {
		return this.exfmtid;
	}

	public void setExfmtid(String exfmtid) {
		this.exfmtid = exfmtid;
	}

	public String getItmnm() {
		return this.itmnm;
	}

	public void setItmnm(String itmnm) {
		this.itmnm = itmnm;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ExFmtId))
			return false;
		ExFmtId castOther = (ExFmtId) other;

		return ((this.getExfmtid() == castOther.getExfmtid()) || (this
				.getExfmtid() != null
				&& castOther.getExfmtid() != null && this.getExfmtid().equals(
				castOther.getExfmtid())))
				&& ((this.getItmnm() == castOther.getItmnm()) || (this
						.getItmnm() != null
						&& castOther.getItmnm() != null && this.getItmnm()
						.equals(castOther.getItmnm())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getExfmtid() == null ? 0 : this.getExfmtid().hashCode());
		result = 37 * result
				+ (getItmnm() == null ? 0 : this.getItmnm().hashCode());
		return result;
	}

}