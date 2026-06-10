package com.kayak.dps.valtabimp.model;

public class T8prodbase {

	public int id;
	public String start_fee_date;
	public String end_date;
	public String real_pay_date; //实际兑付日期
	public String pay_date;//兑付日
	public String subs_date;//申购确认N值
	public String redeem_date;//赎回确认N值
	public Integer is_capital;//保本非保本
	public String basedays;//产品计息基数
	public String issu_ccy;//发行币种
	public String prod_mod;//产品模式
	public String prod_code;//产品代码
	public String prod_name;//产品名称
	public Double expe_rate;//产品收益率
	public String acct_mod;//核算类型
	
	public String valuation_method;
	
	public String getValuation_method() {
		return valuation_method;
	}
	public void setValuation_method(String valuation_method) {
		this.valuation_method = valuation_method;
	}

	public String getProd_mod() {
		return prod_mod;
	}
	public void setProd_mod(String prod_mod) {
		this.prod_mod = prod_mod;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStart_fee_date() {
		return start_fee_date;
	}
	public void setStart_fee_date(String start_fee_date) {
		this.start_fee_date = start_fee_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getReal_pay_date() {
		return real_pay_date;
	}
	public void setReal_pay_date(String real_pay_date) {
		this.real_pay_date = real_pay_date;
	}
	public String getPay_date() {
		return pay_date;
	}
	public void setPay_date(String pay_date) {
		this.pay_date = pay_date;
	}
	public String getSubs_date() {
		return subs_date;
	}
	public void setSubs_date(String subs_date) {
		this.subs_date = subs_date;
	}
	public String getRedeem_date() {
		return redeem_date;
	}
	public void setRedeem_date(String redeem_date) {
		this.redeem_date = redeem_date;
	}
	public Integer getIs_capital() {
		return is_capital;
	}
	public void setIs_capital(Integer is_capital) {
		this.is_capital = is_capital;
	}
	public String getIssu_ccy() {
		return issu_ccy;
	}
	public void setIssu_ccy(String issu_ccy) {
		this.issu_ccy = issu_ccy;
	}
	public String getProd_code() {
		return prod_code;
	}
	public void setProd_code(String prod_code) {
		this.prod_code = prod_code;
	}
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
	public String getBasedays() {
		return basedays;
	}
	public void setBasedays(String basedays) {
		this.basedays = basedays;
	}
	public Double getExpe_rate() {
		return expe_rate;
	}
	public void setExpe_rate(Double expe_rate) {
		this.expe_rate = expe_rate;
	}
	public String getAcct_mod() {
		return acct_mod;
	}
	public void setAcct_mod(String acct_mod) {
		this.acct_mod = acct_mod;
	}


}
