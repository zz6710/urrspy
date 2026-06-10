package com.kayak.dps.valtabimp.model;

public class T8_BANK_FY {
	private String t8_prod_base_id   ; //产品id
	private String prod_code        ;//理财产品代码
	private String prod_name        ;//理财产品名称
	private String buybackdeal_money1 ="0"       ;//回购交易(1日)  直融中心交易费
	private String buybackdeal_money2 ="0"      ;//回购交易(2日以上)
	private String bond_deal ="0"  ;//债券买卖
	private String Bill_maintenance ="0";//账户维护费
	private String Pledge_style_Repo1 ="0";//质押式回购(单券种)
	private String Pledge_style_Repo2 ="0";//质押式回购(多券种)
	private String Outright_Repo ="0";//买断式回购
	private String bond_deal2 ="0";//债券买卖
	private String account_maintenance_fee ="0";//账户维护费
	private String total_cost ="0"      ;//应缴费总金额
	private String account_code   ;//付款账号
	private String account_name   ;//付款户名
	private String income_account_code   ;//收款账号
	private String income_account_name   ;//收款户名
	private String remark;//备注

	private String is_cross_parent;
	private String tradedate;
	private String startdate;
	private String enddate;
	private String bottonid;
	private String button_id;
	private String dates;
	private String unique_code;
	private String feepay_type;
	private String prod_codes;



	public T8_BANK_FY() {
	}

	public String getButton_id() {
		return button_id;
	}

	public void setButton_id(String button_id) {
		this.button_id = button_id;
	}

	public String getProd_codes() {
		return prod_codes;
	}

	public void setProd_codes(String prod_codes) {
		this.prod_codes = prod_codes;
	}

	public String getProd_name() {
		return prod_name;
	}

	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}

	public String getT8_prod_base_id() {
		return t8_prod_base_id;
	}

	public void setT8_prod_base_id(String t8_prod_base_id) {
		this.t8_prod_base_id = t8_prod_base_id;
	}

	public String getProd_code() {
		return prod_code;
	}

	public void setProd_code(String prod_code) {
		this.prod_code = prod_code;
	}

	public String getBuybackdeal_money1() {
		return buybackdeal_money1;
	}

	public void setBuybackdeal_money1(String buybackdeal_money1) {
		this.buybackdeal_money1 = buybackdeal_money1;
	}

	public String getBuybackdeal_money2() {
		return buybackdeal_money2;
	}

	public void setBuybackdeal_money2(String buybackdeal_money2) {
		this.buybackdeal_money2 = buybackdeal_money2;
	}

	public String getBond_deal() {
		return bond_deal;
	}

	public void setBond_deal(String bond_deal) {
		this.bond_deal = bond_deal;
	}

	public String getBill_maintenance() {
		return Bill_maintenance;
	}

	public void setBill_maintenance(String bill_maintenance) {
		Bill_maintenance = bill_maintenance;
	}

	public String getPledge_style_Repo1() {
		return Pledge_style_Repo1;
	}

	public void setPledge_style_Repo1(String pledge_style_Repo1) {
		Pledge_style_Repo1 = pledge_style_Repo1;
	}

	public String getPledge_style_Repo2() {
		return Pledge_style_Repo2;
	}

	public void setPledge_style_Repo2(String pledge_style_Repo2) {
		Pledge_style_Repo2 = pledge_style_Repo2;
	}

	public String getOutright_Repo() {
		return Outright_Repo;
	}

	public void setOutright_Repo(String outright_Repo) {
		Outright_Repo = outright_Repo;
	}

	public String getBond_deal2() {
		return bond_deal2;
	}

	public void setBond_deal2(String bond_deal2) {
		this.bond_deal2 = bond_deal2;
	}

	public String getAccount_maintenance_fee() {
		return account_maintenance_fee;
	}

	public void setAccount_maintenance_fee(String account_maintenance_fee) {
		this.account_maintenance_fee = account_maintenance_fee;
	}

	public String getTotal_cost() {
		return total_cost;
	}

	public void setTotal_cost(String total_cost) {
		this.total_cost = total_cost;
	}

	public String getAccount_code() {
		return account_code;
	}

	public void setAccount_code(String account_code) {
		this.account_code = account_code;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getIncome_account_code() {
		return income_account_code;
	}

	public void setIncome_account_code(String income_account_code) {
		this.income_account_code = income_account_code;
	}

	public String getIncome_account_name() {
		return income_account_name;
	}

	public void setIncome_account_name(String income_account_name) {
		this.income_account_name = income_account_name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIs_cross_parent() {
		return is_cross_parent;
	}

	public void setIs_cross_parent(String is_cross_parent) {
		this.is_cross_parent = is_cross_parent;
	}

	public String getTradedate() {
		return tradedate;
	}

	public void setTradedate(String tradedate) {
		this.tradedate = tradedate;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getBottonid() {
		return bottonid;
	}

	public void setBottonid(String bottonid) {
		this.bottonid = bottonid;
	}

	public String getDates() {
		return dates;
	}

	public void setDates(String dates) {
		this.dates = dates;
	}

	public String getUnique_code() {
		return unique_code;
	}

	public void setUnique_code(String unique_code) {
		this.unique_code = unique_code;
	}

	public String getFeepay_type() {
		return feepay_type;
	}

	public void setFeepay_type(String feepay_type) {
		this.feepay_type = feepay_type;
	}

	@Override
	public String toString() {
		return "T8_BANK_FY{" +
				"t8_prod_base_id='" + t8_prod_base_id + '\'' +
				", prod_code='" + prod_code + '\'' +
				", prod_name='" + prod_name + '\'' +
				", buybackdeal_money1='" + buybackdeal_money1 + '\'' +
				", buybackdeal_money2='" + buybackdeal_money2 + '\'' +
				", bond_deal='" + bond_deal + '\'' +
				", Bill_maintenance='" + Bill_maintenance + '\'' +
				", Pledge_style_Repo1='" + Pledge_style_Repo1 + '\'' +
				", Pledge_style_Repo2='" + Pledge_style_Repo2 + '\'' +
				", Outright_Repo='" + Outright_Repo + '\'' +
				", bond_deal2='" + bond_deal2 + '\'' +
				", account_maintenance_fee='" + account_maintenance_fee + '\'' +
				", total_cost='" + total_cost + '\'' +
				", account_code='" + account_code + '\'' +
				", account_name='" + account_name + '\'' +
				", income_account_code='" + income_account_code + '\'' +
				", income_account_name='" + income_account_name + '\'' +
				", remark='" + remark + '\'' +
				", is_cross_parent='" + is_cross_parent + '\'' +
				", tradedate='" + tradedate + '\'' +
				", startdate='" + startdate + '\'' +
				", enddate='" + enddate + '\'' +
				", bottonid='" + bottonid + '\'' +
				", button_id='" + button_id + '\'' +
				", dates='" + dates + '\'' +
				", unique_code='" + unique_code + '\'' +
				", feepay_type='" + feepay_type + '\'' +
				", prod_codes='" + prod_codes + '\'' +
				'}';
	}
}
