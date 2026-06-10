package com.kayak.dps.valtabimp.model;

public class Vwsysadtype {

	private int id;
	private String ad_name;
	private String calc_type;//核算类型
	private String subj_type;//科目类型
	private int    cbc_type;//中债分类1——标准资产  2——非标资产  3——其他类金融资产  4——其他
	private int    ad_level;//级别
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAd_name() {
		return ad_name;
	}
	public void setAd_name(String ad_name) {
		this.ad_name = ad_name;
	}
	public String getCalc_type() {
		return calc_type;
	}
	public void setCalc_type(String calc_type) {
		this.calc_type = calc_type;
	}
	public String getSubj_type() {
		return subj_type;
	}
	public void setSubj_type(String subj_type) {
		this.subj_type = subj_type;
	}
	public int getCbc_type() {
		return cbc_type;
	}
	public void setCbc_type(int cbc_type) {
		this.cbc_type = cbc_type;
	}
	public int getAd_level() {
		return ad_level;
	}
	public void setAd_level(int ad_level) {
		this.ad_level = ad_level;
	}
	public Vwsysadtype(int id, String ad_name, String calc_type,
                       String subj_type, int cbc_type, int ad_level) {
		super();
		this.id = id;
		this.ad_name = ad_name;
		this.calc_type = calc_type;
		this.subj_type = subj_type;
		this.cbc_type = cbc_type;
		this.ad_level = ad_level;
	}
	
	public Vwsysadtype(){};
}
