package com.kayak.rpt.zz.manage.enums;

import java.util.List;

/**
 * ExFmt entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ExFmt implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ExFmtId id;
	private Long itmprc;
	private Long itmscl;
	private String fld;
	private String fldpk;
	private Long sn;
	private String itmdsc;
	/**
	 * 分级数据项父节点
	 */
	private String fmtItmUp;

	/* 2007-12-08 Qc add 从ex_dict表中关联取出的数据类型相关参数 */
	/**
	 * 接口数据项类型（A-定长字符型, C-变长字符型, N-数字型）
	 */
	private String dictItmtp;
	private Long dictItmprc;
	private Long dictItmscl;
	private String dictflag;
	private String dictItmdic;
	private String dictItmmem;

	private List<ExFmt> exFmtList;

	/** default constructor */
	public ExFmt() {
	}

	public ExFmt(String exfmtid) {
		this.id = new ExFmtId(exfmtid, "");
	}

	/** minimal constructor */
	public ExFmt(ExFmtId id) {
		this.id = id;
	}

	/** full constructor */
	public ExFmt(ExFmtId id, Long itmprc, Long itmscl, String fld,
                 String fldpk, Long sn) {
		this.id = id;
		this.itmprc = itmprc;
		this.itmscl = itmscl;
		this.fld = fld;
		this.fldpk = fldpk;
		this.sn = sn;
	}

	// Property accessors

	public ExFmtId getId() {
		return this.id;
	}

	public void setId(ExFmtId id) {
		this.id = id;
	}

	public Long getItmprc() {
		/* 2007-12-08 Qc add 支持"若本长度>0则按本长度，否则按数据字典中的定义" */ 
		if (this.itmprc == null || this.itmprc.longValue() <= 0L)
			return this.getDictItmprc();

		return this.itmprc;
	}

	public void setItmprc(Long itmprc) {
		this.itmprc = itmprc;
	}

	public Long getItmscl() {
		/* 2007-12-08 Qc add 支持"若本长度>0则按本长度，否则按数据字典中的定义" */
		if (this.itmprc == null || this.itmprc.longValue() <= 0L)
			return this.getDictItmscl();

		if (this.itmscl == null) return new Long(0);
		return this.itmscl;
	}

	public void setItmscl(Long itmscl) {
		this.itmscl = itmscl;
	}

	public String getFld() {
		return this.fld;
	}

	public void setFld(String fld) {
		this.fld = fld;
	}

	public String getFldpk() {
		return this.fldpk;
	}

	public void setFldpk(String fldpk) {
		this.fldpk = fldpk;
	}

	public Long getSn() {
		return this.sn;
	}

	public void setSn(Long sn) {
		this.sn = sn;
	}

	public String getItmdsc() {
		return itmdsc;
	}

	public void setItmdsc(String itmdsc) {
		this.itmdsc = itmdsc;
	}


	public Long getDictItmprc() {
		return dictItmprc;
	}

	public void setDictItmprc(Long dictItmprc) {
		this.dictItmprc = dictItmprc;
	}

	public Long getDictItmscl() {
		if (dictItmscl == null) return new Long(0);
		return dictItmscl;
	}

	public void setDictItmscl(Long dictItmscl) {
		this.dictItmscl = dictItmscl;
	}

	public String getDictItmtp() {
		return dictItmtp;
	}

	public void setDictItmtp(String dictItmtp) {
		this.dictItmtp = dictItmtp;
	}

	public String getDictflag() {
		return dictflag;
	}

	public void setDictflag(String dictflag) {
		this.dictflag = dictflag;
	}

	/**
	 * @return the dictItmdic
	 */
	public String getDictItmdic() {
		return dictItmdic;
	}

	/**
	 * @param dictItmdic the dictItmdic to set
	 */
	public void setDictItmdic(String dictItmdic) {
		this.dictItmdic = dictItmdic;
	}

	public String getDictItmmem() {
		return dictItmmem;
	}

	public void setDictItmmem(String dictItmmem) {
		this.dictItmmem = dictItmmem;
	}

	public void setExFmtList(List<ExFmt> exFmtList) {
		this.exFmtList = exFmtList;
	}

	public List<ExFmt> getExFmtList() {
		return exFmtList;
	}

	public String getFmtItmUp() {
		return fmtItmUp;
}

	public void setFmtItmup(String fmtItmUp) {
		this.fmtItmUp = fmtItmUp;
	}
}
