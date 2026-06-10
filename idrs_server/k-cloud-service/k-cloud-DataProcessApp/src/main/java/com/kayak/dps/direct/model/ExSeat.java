package com.kayak.dps.direct.model;

import java.util.List;

/**
 * ExSeat entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ExSeat implements java.io.Serializable {

	// Fields

	private ExSeatId id;
	private String exmode;
	private String fnmfmt;
	private String oheader;
	private String oitmnm;
	private String oitmnmfl;
	private String oreccnt;
	private String osymbol;
	private String oflddef;
	private String extab;
	private String exfmtid;
	private String ftaname;
	private String ttaname;
	private String extpname;
	private String indexfile;


	/* 2007-12-08 Qc add 需要处理的包查询条件,格式为：packid1,packid2,packid3, */
	private String packIds;
	/* 2007-12-08 Qc add 通过解析fnmfmt后的文件名称 */
	private String filename;
	/* 2007-12-08 Qc add 读取配置的字段数据信息 */
	private List fieldList;

	/**
	 * 查询数据起始量
	 */
	private Integer left;
	/**
	 * 查询数据结束量
	 */
	private Integer right;

	// Constructors

	/** default constructor */
	public ExSeat() {
	}

	/**
	 * 2007-12-08  按关键字一次初始化
	 * @param fcode
	 * @param tcode
	 * @param extpid
	 */
	public ExSeat(String fcode,String tcode,String extpid) {
		this.id = new ExSeatId(fcode,tcode, extpid);
	}

	/** minimal constructor */
	public ExSeat(ExSeatId id, String exmode) {
		this.id = id;
		this.exmode = exmode;
	}

	/** full constructor */
	public ExSeat(ExSeatId id, String exmode, String fnmfmt, String oheader,
			String oitmnm, String oitmnmfl, String oreccnt, String osymbol,
			String oflddef, String extab, String exfmtid) {
		this.id = id;
		this.exmode = exmode;
		this.fnmfmt = fnmfmt;
		this.oheader = oheader;
		this.oitmnm = oitmnm;
		this.oitmnmfl = oitmnmfl;
		this.oreccnt = oreccnt;
		this.osymbol = osymbol;
		this.oflddef = oflddef;
		this.extab = extab;
		this.exfmtid = exfmtid;
	}

	// Property accessors

	public ExSeatId getId() {
		return this.id;
	}

	public void setId(ExSeatId id) {
		this.id = id;
	}

	public String getExmode() {
		return this.exmode;
	}

	public void setExmode(String exmode) {
		this.exmode = exmode;
	}

	public String getFnmfmt() {
		return this.fnmfmt;
	}

	public void setFnmfmt(String fnmfmt) {
		this.fnmfmt = fnmfmt;
	}

	public String getOheader() {
		return this.oheader;
	}

	public void setOheader(String oheader) {
		this.oheader = oheader;
	}

	public String getOitmnm() {
		return this.oitmnm;
	}

	public void setOitmnm(String oitmnm) {
		this.oitmnm = oitmnm;
	}

	public String getOitmnmfl() {
		return this.oitmnmfl;
	}

	public void setOitmnmfl(String oitmnmfl) {
		this.oitmnmfl = oitmnmfl;
	}

	public String getOreccnt() {
		return this.oreccnt;
	}

	public void setOreccnt(String oreccnt) {
		this.oreccnt = oreccnt;
	}

	public String getOsymbol() {
		return this.osymbol;
	}

	public void setOsymbol(String osymbol) {
		this.osymbol = osymbol;
	}

	public String getOflddef() {
		return this.oflddef;
	}

	public void setOflddef(String oflddef) {
		this.oflddef = oflddef;
	}

	public String getExtab() {
		return this.extab;
	}

	public void setExtab(String extab) {
		this.extab = extab;
	}

	public String getExfmtid() {
		return this.exfmtid;
	}

	public void setExfmtid(String exfmtid) {
		this.exfmtid = exfmtid;
	}



	public String getExtpname() {
		return extpname;
	}

	public void setExtpname(String extpname) {
		this.extpname = extpname;
	}


	public String getFtaname() {
		return ftaname;
	}

	public void setFtaname(String ftaname) {
		this.ftaname = ftaname;
	}

	public String getTtaname() {
		return ttaname;
	}

	public void setTtaname(String ttaname) {
		this.ttaname = ttaname;
	}


	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public List getFieldList() {
		return fieldList;
	}

	public void setFieldList(List fieldList) {
		this.fieldList = fieldList;
	}

	public String getIndexfile() {
		return indexfile;
	}

	public void setIndexfile(String indexfile) {
		this.indexfile = indexfile;
	}

	public String getPackIds() {
		return packIds;
	}

	public void setPackIds(String packIds) {
		this.packIds = packIds;
	}

	public Integer getLeft() {
		return left;
}

	public void setLeft(Integer left) {
		this.left = left;
	}

	public Integer getRight() {
		return right;
	}

	public void setRight(Integer right) {
		this.right = right;
	}

}
