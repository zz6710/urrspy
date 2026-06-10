package com.kayak.dps.valtabimp.model;

public class T8prodbasesub {

	public int id;
	public String is_last;
	public String end_date;
	public Double expe_rate; //预期收益率
	public String prod_code_sub;//子产品代码
	public String prod_name_syb;//子产品名称
	public String prod_code;//产品代码

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIs_last() {
		return is_last;
	}
	public void setIs_last(String is_last) {
		this.is_last = is_last;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public Double getExpe_rate() {
		return expe_rate;
	}
	public void setExpe_rate(Double expe_rate) {
		this.expe_rate = expe_rate;
	}
	public String getProd_code_sub() {
		return prod_code_sub;
	}
	public void setProd_code_sub(String prod_code_sub) {
		this.prod_code_sub = prod_code_sub;
	}
	public String getProd_name_syb() {
		return prod_name_syb;
	}
	public void setProd_name_syb(String prod_name_syb) {
		this.prod_name_syb = prod_name_syb;
	}
	public String getProd_code() {
		return prod_code;
	}
	public void setProd_code(String prod_code) {
		this.prod_code = prod_code;
	}
	
}
