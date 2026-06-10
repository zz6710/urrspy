package com.kayak.dps.direct.model;

public class ExCheck {
	private String fld;
	private String exfmtid;
	private String itmcn;
	private String itmxv;
	private String itmnv;
	private String itmnl;

	private String itmrx;
	private String itmrt;
	private String itmcv;
	private String itmcf;

	public ExCheck(String fld, String exfmtid, String itmcn, String itmxv, String itmnv, String itmnl, String itmrx,
                   String itmrt, String itmcv, String itmcf) {
		this.fld = fld;
		this.exfmtid = exfmtid;
		this.itmcn = itmcn;
		this.itmxv = itmxv;
		this.itmnv = itmnv;
		this.itmnl = itmnl;
		this.itmrx = itmrx;
		this.itmrt = itmrt;
		this.itmcv = itmcv;
		this.itmcf = itmcf;
	}

	public String getFld() {
		return fld;
	}

	public void setFld(String fld) {
		this.fld = fld;
	}

	public String getExfmtid() {
		return exfmtid;
	}

	public void setExfmtid(String exfmtid) {
		this.exfmtid = exfmtid;
	}

	public String getItmcn() {
		return itmcn;
	}

	public void setItmcn(String itmcn) {
		this.itmcn = itmcn;
	}

	public String getItmxv() {
		return itmxv;
	}

	public void setItmxv(String itmxv) {
		this.itmxv = itmxv;
	}

	public String getItmnv() {
		return itmnv;
	}

	public void setItmnv(String itmnv) {
		this.itmnv = itmnv;
	}

	public String getItmnl() {
		return itmnl;
	}

	public void setItmnl(String itmnl) {
		this.itmnl = itmnl;
	}

	public String getItmrx() {
		return itmrx;
	}

	public void setItmrx(String itmrx) {
		this.itmrx = itmrx;
	}

	public String getItmrt() {
		return itmrt;
	}

	public void setItmrt(String itmrt) {
		this.itmrt = itmrt;
	}

	public String getItmcv() {
		return itmcv;
	}

	public void setItmcv(String itmcv) {
		this.itmcv = itmcv;
	}

	public Object getItmcf() {
		// TODO Auto-generated method stub
		return itmcf;
	}

	public void setItmcf(String itmcf) {
		this.itmcf = itmcf;
	}

}
