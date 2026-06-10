package com.kayak.dps.valtabimp.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 变动集
 * @author zzl
 *
 */
public class VwchangeData implements Cloneable{
	public Integer getAddcurrentdayinterest() {
		return addcurrentdayinterest;
	}

	public void setAddcurrentdayinterest(Integer addcurrentdayinterest) {
		this.addcurrentdayinterest = addcurrentdayinterest;
	}

	@Override
	public String toString() {
		return "VwchangeData{" +
				"maketype='" + maketype + '\'' +
				", event_name='" + event_name + '\'' +
				", t8_prod_base_id=" + t8_prod_base_id +
				", probaseSmid=" + probaseSmid +
				", dealid=" + dealid +
				", fullprice=" + fullprice +
				", cleanprice=" + cleanprice +
				", faceamount=" + faceamount +
				", interestcost=" + interestcost +
				", interest=" + interest +
				", beginamount=" + beginamount +
				", endamount=" + endamount +
				", beginfaceamount=" + beginfaceamount +
				", endfaceamount=" + endfaceamount +
				", transyield=" + transyield +
				", translevel=" + translevel +
				", isexercise='" + isexercise + '\'' +
				", pdyield='" + pdyield + '\'' +
				", feeyield=" + feeyield +
				", optionday='" + optionday + '\'' +
				", fixbondid='" + fixbondid + '\'' +
				", ftool_name='" + ftool_name + '\'' +
				", ftool_code='" + ftool_code + '\'' +
				", bond_market='" + bond_market + '\'' +
				", t8_sys_portfol_id=" + t8_sys_portfol_id +
				", addcurrentdayinterest=" + addcurrentdayinterest +
				", openpdid='" + openpdid + '\'' +
				", pdid='" + pdid + '\'' +
				", t8_sys_adtype_id=" + t8_sys_adtype_id +
				", producttypeid='" + producttypeid + '\'' +
				", relation_no='" + relation_no + '\'' +
				", dealtype='" + dealtype + '\'' +
				", fixdealid='" + fixdealid + '\'' +
				", t8_sys_acceven_id=" + t8_sys_acceven_id +
				", counterpartyid='" + counterpartyid + '\'' +
				", settle_date=" + settle_date +
				", account_date=" + account_date +
				", lastsharedate='" + lastsharedate + '\'' +
				", dealtax='" + dealtax + '\'' +
				", fairprice=" + fairprice +
				", shareinterval='" + shareinterval + '\'' +
				", coupon='" + coupon + '\'' +
				", unitprincipal=" + unitprincipal +
				", dealno='" + dealno + '\'' +
				", repodealno='" + repodealno + '\'' +
				", transamount=" + transamount +
				", npamount=" + npamount +
				", amount=" + amount +
				", netprice=" + netprice +
				", t8_channel_bas_id=" + t8_channel_bas_id +
				", direction=" + direction +
				", repotrans_id='" + repotrans_id + '\'' +
				", maturedealrate=" + maturedealrate +
				", account_type='" + account_type + '\'' +
				", t8_deal_info_id='" + t8_deal_info_id + '\'' +
				", realrate=" + realrate +
				", repotrans_type=" + repotrans_type +
				", t8_prod_umbrei_id=" + t8_prod_umbrei_id +
				", feepaybln=" + feepaybln +
				", profitloss_id=" + profitloss_id +
				", fairpricedate=" + fairpricedate +
				", rate_base=" + rate_base +
				", profitchg=" + profitchg +
				", profitbln=" + profitbln +
				", payfitbln=" + payfitbln +
				", payfitchg=" + payfitchg +
				", undsfitchg=" + undsfitchg +
				", undsfitbln=" + undsfitbln +
				", real_cost=" + real_cost +
				", fair_date='" + fair_date + '\'' +
				", fee_money=" + fee_money +
				", prod_famount=" + prod_famount +
				", thisnetprice=" + thisnetprice +
				", thisfullprice=" + thisfullprice +
				", invest_object='" + invest_object + '\'' +
				", jxdays=" + jxdays +
				", is_confirm=" + is_confirm +
				", costchg=" + costchg +
				", costbln=" + costbln +
				", interestincomechg=" + interestincomechg +
				", interestincomebln=" + interestincomebln +
				", dztaxchg=" + dztaxchg +
				", dztaxbln=" + dztaxbln +
				", dzjcchg=" + dzjcchg +
				", dzjcbln=" + dzjcbln +
				", jcfeechg=" + jcfeechg +
				", jcfeebln=" + jcfeebln +
				", onway_amountchg=" + onway_amountchg +
				", onway_amountbln=" + onway_amountbln +
				", is_last=" + is_last +
				", is_change=" + is_change +
				", fee_adtype='" + fee_adtype + '\'' +
				", settlementbegindate=" + settlementbegindate +
				", settlementenddate=" + settlementenddate +
				", tenor='" + tenor + '\'' +
				", accrual_code='" + accrual_code + '\'' +
				", duration=" + duration +
				", cuy='" + cuy + '\'' +
				", end_date=" + end_date +
				", issu_ccy='" + issu_ccy + '\'' +
				", fcu_interestincome=" + fcu_interestincome +
				", asset_type='" + asset_type + '\'' +
				", tax=" + tax +
				", income_type='" + income_type + '\'' +
				", receivableschg=" + receivableschg +
				", receivablesbln=" + receivablesbln +
				", allamount=" + allamount +
				", pay_mod=" + pay_mod +
				", prod_mod='" + prod_mod + '\'' +
				", myriad_income=" + myriad_income +
				", tax_accruincomechg=" + tax_accruincomechg +
				", tax_interestchg=" + tax_interestchg +
				", tax_positionchg=" + tax_positionchg +
				", tax_positionbln=" + tax_positionbln +
				", tax_rpl=" + tax_rpl +
				", tax_child_adtype=" + tax_child_adtype +
				", tax_ftool_code='" + tax_ftool_code + '\'' +
				", tax_rate=" + tax_rate +
				", tax_rplrate=" + tax_rplrate +
				", tax_yztaxchg=" + tax_yztaxchg +
				", tax_type='" + tax_type + '\'' +
				", endjz=" + endjz +
				", endjz_net=" + endjz_net +
				", beginjz=" + beginjz +
				", beginjz_net=" + beginjz_net +
				", ir_rate=" + ir_rate +
				", divisor=" + divisor +
				", ir_rate_balance=" + ir_rate_balance +
				", old_ftool_code='" + old_ftool_code + '\'' +
				", ast_positionbln=" + ast_positionbln +
				", is_fee_trade=" + is_fee_trade +
				", algorithmforfee='" + algorithmforfee + '\'' +
				", fee_mod='" + fee_mod + '\'' +
				", ori_settle_date=" + ori_settle_date +
				", cfets_tax=" + cfets_tax +
				", fund_type=" + fund_type +
				", is_repo=" + is_repo +
				", Is_cashflow=" + Is_cashflow +
				", is_personage=" + is_personage +
				", tax_rpl2=" + tax_rpl2 +
				", gachabln=" + gachabln +
				", prodtaxbln=" + prodtaxbln +
				", prodyztaxbln=" + prodyztaxbln +
				", prodtaxjzbln=" + prodtaxjzbln +
				", tax_pay_static=" + tax_pay_static +
				", account_code='" + account_code + '\'' +
				", account_name='" + account_name + '\'' +
				", valuedays='" + valuedays + '\'' +
				", settlement_way='" + settlement_way + '\'' +
				", is_assosiate_assets='" + is_assosiate_assets + '\'' +
				", endpay_date=" + endpay_date +
				", accountcodetype='" + accountcodetype + '\'' +
				", cfets_trans=" + cfets_trans +
				'}';
	}
	
	/**
	 * 收款类型
	 */
	public String maketype;//收款类型（1：交易对手；2：券商账户）
	public String tenor;//占款天数
	public Date settlementenddate;
	public Date settlementbegindate;
	public String accrual_code;//现金划拨发生科目
	/**
	 * 资产本期利息调整变动
	 */
	public BigDecimal tax_interestchg;
	
	/**
	 * 税费支付状态
	 */
	public Integer tax_pay_static;
	
	/**
	 * 是否是现金型产品
	 * */
	public Integer Is_cashflow;
	
	/**
	 * 是否底层资产
	 */
	public String is_assosiate_assets;
	
	/**
	 * 到期兑付日
	 */
	public Date endpay_date;
	
	/**
	 * 是否个人
	 * */
	public Integer is_personage;
	
	/**
	 * 账户类型
	 */
	public String accountcodetype;
	
	public String getMaketype() {
		return maketype;
	}

	public void setMaketype(String maketype) {
		this.maketype = maketype;
	}

	public String getTenor() {
		return tenor;
	}

	public void setTenor(String tenor) {
		this.tenor = tenor;
	}

	public Date getSettlementenddate() {
		return settlementenddate;
	}

	public void setSettlementenddate(Date settlementenddate) {
		this.settlementenddate = settlementenddate;
	}

	public Date getSettlementbegindate() {
		return settlementbegindate;
	}

	public void setSettlementbegindate(Date settlementbegindate) {
		this.settlementbegindate = settlementbegindate;
	}

	public String getAccrual_code() {
		return accrual_code;
	}

	public void setAccrual_code(String accrual_code) {
		this.accrual_code = accrual_code;
	}

	public BigDecimal getTax_interestchg() {
		return tax_interestchg;
	}

	public void setTax_interestchg(BigDecimal tax_interestchg) {
		this.tax_interestchg = tax_interestchg;
	}

	public Integer getTax_pay_static() {
		return tax_pay_static;
	}

	public void setTax_pay_static(Integer tax_pay_static) {
		this.tax_pay_static = tax_pay_static;
	}

	public Integer getIs_cashflow() {
		return Is_cashflow;
	}

	public void setIs_cashflow(Integer is_cashflow) {
		Is_cashflow = is_cashflow;
	}

	public String getIs_assosiate_assets() {
		return is_assosiate_assets;
	}

	public void setIs_assosiate_assets(String is_assosiate_assets) {
		this.is_assosiate_assets = is_assosiate_assets;
	}

	public Date getEndpay_date() {
		return endpay_date;
	}

	public void setEndpay_date(Date endpay_date) {
		this.endpay_date = endpay_date;
	}

	public Integer getIs_personage() {
		return is_personage;
	}

	public void setIs_personage(Integer is_personage) {
		this.is_personage = is_personage;
	}

	public String getAccountcodetype() {
		return accountcodetype;
	}

	public void setAccountcodetype(String accountcodetype) {
		this.accountcodetype = accountcodetype;
	}

	public Double getOnway_amountchg() {
		return onway_amountchg;
	}

	public void setOnway_amountchg(Double onway_amountchg) {
		this.onway_amountchg = onway_amountchg;
	}

	public Double getOnway_amountbln() {
		return onway_amountbln;
	}

	public void setOnway_amountbln(Double onway_amountbln) {
		this.onway_amountbln = onway_amountbln;
	}

	/**
	 * 变动类型名称
	 */
	public String event_name;//变动类型名称

	/**
	 * 理财产品ID
	 */
	public Integer t8_prod_base_id;//理财产品ID
	/**
	 * 子理财ID
	 */
	public Integer probaseSmid;//子理财ID
	/**
	 * 交易ID
	 */
	public Integer dealid;//交易ID
	/***
	 * 交易全价
	 */
	public BigDecimal fullprice;

	/***
	 * 交易净价
	 */
	public BigDecimal cleanprice;
	/***
	 * 交易面额
	 */
	public BigDecimal faceamount;

	/***
	 * 付息利息
	 */
	public BigDecimal interest;

	/**
	 * 首期金额
	 */
	public BigDecimal beginamount;

	/**
	 * 到期金额
	 */
	public BigDecimal endamount;

	/**
	 * 首期面额
	 */
	public BigDecimal beginfaceamount;

	/**
	 * 到期面额
	 */
	public BigDecimal endfaceamount;

	/**
	 * 回购利率
	 */
	public BigDecimal transyield;

	/**
	 * 变动类型优先级
	 */
	public Integer translevel;

	/**
	 * 行权日
	 */
	public String isexercise;


//	/**
//	 * 交易利率
//	 */
//	public String transyield;
	/**
	 * 理财收益率
	 */
	public String pdyield;
	/**
	 * 费率
	 */
	public Double feeyield;
	/**
	 * 行权日
	 */
	public String optionday;

	/**
	 * 债券id
	 */
	public String fixbondid;
	/**
	 * 产品名称
	 */
	public String ftool_name;
	/**
	 * 产品代码
	 */
	public String ftool_code;
	/**
	 * 市场
	 */
	public String bond_market;
	/**
	 * 投组id
	 */
	public Integer t8_sys_portfol_id;
	/**
	 * 期数id
	 */
	public Integer addcurrentdayinterest=0;//是否包含当日息
	public String openpdid;
	/**
	 * 理财产品id
	 */
	public String pdid;
	/**
	 * 产品类型id
	 */
	public Integer t8_sys_adtype_id;
	/**
	 * 金融工具
	 */
	public String producttypeid;
	/**
	 * 关联单号
	 */
	public String relation_no;
	/**
	 * 交易类型
	 */
	public String dealtype;
	/**
	 * 交易id
	 */
	public String fixdealid;
	/**
	 * 变动类型id
	 */
	public Integer t8_sys_acceven_id;
	/**
	 * 交易对手id
	 */
	public String counterpartyid;
	/**;
	 * 变动日期
	 */
	public Date settle_date;
	/**
	 * 记账日期
	 */
	public Date account_date;
	/**
	 * 上次摊销日期
	 */
	public String lastsharedate;
	/**
	 * 实际交易税
	 */
	public String dealtax;
	/**
	 * 公允净价
	 */
	public BigDecimal fairprice;
	/**
	 * 摊销间隔天数
	 */
	public String shareinterval;
	/**
	 * 票面利息
	 */
	public String coupon;

	/**
	 * 单位还本金额
	 */
	public BigDecimal unitprincipal;
	/**
	 * 交易单号
	 */
	public String dealno;
	/**
	 * 回购交易单号
	 */
	public String repodealno;
	/**
	 * 交易金额
	 */
	public BigDecimal transamount;

	/**
	 * 净价金额
	 * @return
	 */
	public BigDecimal npamount;

	/**
	 * 全价金额
	 * @return
	 */
	public BigDecimal amount;

	/**
	 * 百元净价
	 * @return
	 */
	public BigDecimal netprice;

	/**
	 * 通道ID
	 */
	public Integer t8_channel_bas_id;

	/**
	 * 交易方向
	 * @return
	 */
	public Integer direction;

	/**
	 * 回购交易号
	 * @return
	 */
	public String repotrans_id;

	/**
	 * 到期收益率
	 */
	public BigDecimal maturedealrate;

	/**
	 * 会计分类
	 */
	public String account_type;

	/**
	 * 交易ID
	 * @return
	 */
	public String t8_deal_info_id;

	/**
	 * 实际利率
	 */
	public BigDecimal  realrate;


	/**
	 * 回购交易类型 1：主交易跑批类型  2：抵押券跑批类型
	 */
	public Integer repotrans_type;

	/**
	 * 子理财ID
	 */
	public Integer t8_prod_umbrei_id;

	/**
	 * 应付费用变动
	 */
	public BigDecimal  feepaybln;

	/**
	 * 现金损益ID
	 */
	public Integer profitloss_id;
	
	/**
	 * 公允价调整时间
	 */
	public Date fairpricedate;
	
	/**
	 * 票据计息基数
	 */
	public Integer rate_base;
	
	/**
	 * 本期利润变动
	 */
	public Double profitchg;
	
	/**
	 * 本期利润余额
	 */
	public Double profitbln;
	
	/**
	 * 应付利润余额
	 */
	public Double payfitbln;
	
	/**
	 * 应付利润变动
	 */
	public Double payfitchg;
	
	/**
	 * 未分配利润变动
	 */
	public Double undsfitchg;
	
	/**
	 * 未分配利润余额
	 */
	public Double undsfitbln;
	
	/**
	 * 非标资金流表的提前还本 实际发生金额
	 */
	public double real_cost;
	
	/**
	 * 公允价调整计划取最新调整时间
	 */
	public String fair_date;
	
	/**
	 * 费用金额
	 */
	public BigDecimal fee_money;
	
	/**
	 * 产品份额
	 */
	public BigDecimal prod_famount;
	
	
	/**
	 * 本次百元净价
	 * @return
	 */
	public BigDecimal thisnetprice;
	
	/**
	 * 本次百元全价
	 * @return
	 */
	public BigDecimal thisfullprice;
	
	/**
	 * 客户类型
	 */
	public String invest_object;
	
	/**
	 * 计息基数
	 */
	public Integer jxdays;
	
	/**
	 * 现金确认
	 */
	public Integer is_confirm;

	/**
	 * 成本变动
	 */
	public Double costchg;
	/**
	 * 成本余额
	 * 
	 */
	public Double costbln;
	
	/**
	 * 利息调整收入变动
	 */
	public Double interestincomechg;
	
	/**
	 * 利息调整收入余额
	 */
	public Double interestincomebln;
	/**
	 * 待转销项税变动
	 */
	public Double dztaxchg;
	
	/**
	 * 待转销项税余额
	 */
	public Double dztaxbln;
	/**
	 * 待转价差税变动
	 */
	public Double dzjcchg;
	
	/**
	 * 待转价差税余额
	 */
	public Double dzjcbln;
	/**
	 * 价差税费变动
	 */
	public Double jcfeechg;
	
	/**
	 * 价差税费余额
	 */
	public Double jcfeebln;
	
	/**
	 * 在途资金变动
	 */
	public Double onway_amountchg;

	/**
	 * 在途资金余额
	 */
	public Double onway_amountbln;
	/**
	 * 是否最后一期
	 */
	public Integer is_last;
	
	/**
	 * 票面利息是否发生变化
	 */
	public Integer is_change;
	/**
	 * 久期
	 * 
	 */
	public BigDecimal duration;
	/**
	 * 币种
	 */
	public String cuy;
	
	/**
	 * 到期日期
	 */
	public Date end_date;
	
	/**
	 * 募集币种
	 * @return
	 */
	public String issu_ccy;
	
	/**
	 * 外币投资利息收入
	 */
	public BigDecimal fcu_interestincome;
	
	/**
	 * 资产类型
	 */
	public String asset_type;
	/**
	 * 手续费
	 */
	public  BigDecimal tax;

	/**
	 * 产品收益类型
	 */
	public  String income_type;
	
	/**
	 * 应收未收利息变动
	 */
	public Double receivableschg;
	
	/**
	 * 应收未收利息余额
	 */
	public Double receivablesbln;
	
	/**
	 * 全部付息金额
	 * @return
	 */
	public BigDecimal allamount;
	
	/**
	 * 付息方式
	 * @return
	 */
	public Integer pay_mod;
	
	/**
	 * 产品模式
	 */
	public String prod_mod;
	
	
	/**
	  * 产品万份收益
	  */
	public BigDecimal myriad_income;
	
	
	public Double endjz; //期末净值

	public Double endjz_net; //期末累计净值

	public Double beginjz;//期初净值

	public Double beginjz_net; //期初累计净值

	public Double ir_rate;//业绩比较基准

	public int divisor;//计提相差天数

	public double ir_rate_balance; //期初业绩报酬余额
	
	public String old_ftool_code;
	
	
	/**
	 * 资产持仓余额
	 */
	public Double ast_positionbln;
	
	
	public Integer is_fee_trade;
	
	
	public String algorithmforfee;
	
	
	/**
	 * 收费模式
	 */
	public String fee_mod;
	
	
	/**;
	 * 变动日期
	 */
	public Date ori_settle_date;

	/**
	 * 交易中心手续费
	 */
	public Double cfets_tax;

	/**
	 * 基金类型
	 */
	public int  fund_type;

	/**
	 * 是否为正回购
	 */
	public Integer is_repo;

	/**
	 * 回扣费率
	 */
	public Double feeyieldrebate;
	
	/**
	 * 税前利息收入余额
	 */
	public Double tax_interestbln;
	
	/**
	 * 税前价差收入变动
	 */
	public Double tax_rplchg;
	
	/**
	 * 税前价差收入余额
	 */
	public Double tax_rplbln;
	
	/**
	 * 应付贷款税费变动
	 */
	public Double loanvolchg;
	
	/**
	 * 应付贷款税费余额
	 */
	public Double loanvolbln;
	
	/**
	 * 应付金融税费变动
	 */
	public Double finavolchg;
	
	/**
	 * 应付金融税费余额
	 */
	public Double finavolbln;
	
	/**
	 * 应交增值税变动
	 */
	public Double tax_addedchg;
	/**
	 * 应交增值税余额
	 */
	public Double tax_addedbln;
	
	public String account_code;//账号代码
		
	public String account_name;//账号名称	
	
	
	/* 价税分离专用 */
	/**
	 * 资产本期应收利息变动
	 */
	public BigDecimal tax_accruincomechg;
	/**
	 * 资产本期利息调整变动
	 */
	public BigDecimal tax_accruincomeadjustchg;
	/**
	 * 资产本期持仓面额变动
	 */
	public BigDecimal tax_positionchg;
	/**
	 * 资产本期持仓面额余额
	 */
	public BigDecimal tax_positionbln;
	/**
	 * 资产本期价差收入
	 */
	public BigDecimal tax_rpl;
	/**
	 * 价税分离所属资产负债品种id
	 */
	public Integer tax_child_adtype;
	/**
	 * 价税分离所属资产负债代码
	 */
	public String tax_ftool_code;
	/**
	 * 票息收入税率
	 */
	public Double tax_rate;
	/**
	 * 投资收益税率
	 */
	public Double tax_rplrate;
	/**
	 * 增值税本期销项税变动
	 */
	public BigDecimal tax_yztaxchg;
	/**
	 * 税费类型
	 */
	public String tax_type;
	/**
	 * 轧差票息金额
	 */
	public BigDecimal gachabln;
	
	/**
	 * 产品本期计提增值税金额
	 */
	public BigDecimal prodtaxbln;
	/**
	 * 产品本期结转增值税
	 */
	public BigDecimal prodtaxjzbln;
	
	/**
	 * 产品本期结转增值税
	 */
	public BigDecimal gachataxbln;
	
	/**
	 * 产品本期销项税
	 */
	public BigDecimal prodyztaxbln;
	
	/**
	 * 资产本期负价差收入
	 */
	public BigDecimal tax_rpl2;


	/**
	 * 本期利润未实现变动
	 */
	public BigDecimal profitunchg;
	/**
	 * 本期利润未实现余额
	 */
	public BigDecimal profitunbln;
	/**
	 * 未分配利润未实现变动
	 */
	public BigDecimal undsfitunchg;
	/**
	 * 未分配利润未实现余额
	 */
	public BigDecimal undsfitunbln;

	/**
	 * 损益平准金实现变动
	 */
	public BigDecimal paladjunchg;
	/**
	 * 损益平准金未实现余额
	 */
	public BigDecimal paladjunbln;
	
	/**
	 * 应收利息总额
	 */
	public BigDecimal interestcost;
	
	/**
	 * 计提累计天数
	 */
	public int jtdaystol;
	
	/**
	 * 交易费用所属交易品种
	 */
	public String fee_adtype;
	/**
	 * 清算速度
	 */
	public String valuedays;
	/**
	 * 交收方式
	 */
	public String settlement_way;
	
	/**
	 * 理论日期
	 */
	public String theory_date;
	
	/**
	 * 计费截止日
	 */
	public String expiry_date;

	/**
	 * 摊销截至日
	 */
	public String amoretize_enddate;
	
	public Double double_one;
	public Double double_two;
	public Double double_three;
	public Double double_four;
	public Double double_fif;
	public String char_one;
	public String char_two;
	public String char_three;
	public String char_four;
	public String char_fif;
	
	public String getAmoretize_enddate() {
		return amoretize_enddate;
	}

	public void setAmoretize_enddate(String latest_exdate) {
		this.amoretize_enddate = latest_exdate;
	}


	
	public String getExpiry_date() {
		return expiry_date;
	}

	public void setExpiry_date(String expiry_date) {
		this.expiry_date = expiry_date;
	}
	
	public String getTheory_date() {
		return theory_date;
	}

	public void setTheory_date(String theory_date) {
		this.theory_date = theory_date;
	}

	public BigDecimal getTax_accruincomeadjustchg() {
		return tax_accruincomeadjustchg;
	}

	public void setTax_accruincomeadjustchg(BigDecimal tax_accruincomeadjustchg) {
		this.tax_accruincomeadjustchg = tax_accruincomeadjustchg;
	}

	public String getValuedays() {
		return valuedays;
	}

	public void setValuedays(String valuedays) {
		this.valuedays = valuedays;
	}

	public String getSettlement_way() {
		return settlement_way;
	}

	public void setSettlement_way(String settlement_way) {
		this.settlement_way = settlement_way;
	}

	public String getFee_adtype() {
		return fee_adtype;
	}

	public void setFee_adtype(String fee_adtype) {
		this.fee_adtype = fee_adtype;
	}

	public int getJtdaystol() {
		return jtdaystol;
	}

	public void setJtdaystol(int jtdaystol) {
		this.jtdaystol = jtdaystol;
	}

	public BigDecimal getPaladjunchg () {
		return paladjunchg;
	}

	public void setPaladjunchg (BigDecimal paladjunchg) {
		this.paladjunchg=paladjunchg;
	}

	public BigDecimal getPaladjunbln () {
		return paladjunbln;
	}

	public void setPaladjunbln (BigDecimal paladjunbln) {
		this.paladjunbln=paladjunbln;
	}

	public BigDecimal getProfitunchg () {
		return profitunchg;
	}

	public void setProfitunchg (BigDecimal profitunchg) {
		this.profitunchg=profitunchg;
	}

	public BigDecimal getProfitunbln () {
		return profitunbln;
	}

	public void setProfitunbln (BigDecimal profitunbln) {
		this.profitunbln=profitunbln;
	}

	public BigDecimal getUndsfitunchg () {
		return undsfitunchg;
	}

	public void setUndsfitunchg (BigDecimal undsfitunchg) {
		this.undsfitunchg=undsfitunchg;
	}

	public BigDecimal getUndsfitunbln () {
		return undsfitunbln;
	}

	public void setUndsfitunbln (BigDecimal undsfitunbln) {
		this.undsfitunbln=undsfitunbln;
	}

	public BigDecimal getGachabln() {
		return gachabln;
	}

	public void setGachabln(BigDecimal gachabln) {
		this.gachabln = gachabln;
	}

	public BigDecimal getProdtaxbln() {
		return prodtaxbln;
	}

	public void setProdtaxbln(BigDecimal prodtaxbln) {
		this.prodtaxbln = prodtaxbln;
	}

	public BigDecimal getProdtaxjzbln() {
		return prodtaxjzbln;
	}

	public void setProdtaxjzbln(BigDecimal prodtaxjzbln) {
		this.prodtaxjzbln = prodtaxjzbln;
	}

	public BigDecimal getGachataxbln() {
		return gachataxbln;
	}

	public void setGachataxbln(BigDecimal gachataxbln) {
		this.gachataxbln = gachataxbln;
	}

	public BigDecimal getProdyztaxbln() {
		return prodyztaxbln;
	}

	public void setProdyztaxbln(BigDecimal prodyztaxbln) {
		this.prodyztaxbln = prodyztaxbln;
	}

	public BigDecimal getTax_rpl2() {
		return tax_rpl2;
	}

	public void setTax_rpl2(BigDecimal tax_rpl2) {
		this.tax_rpl2 = tax_rpl2;
	}

	public Double getLoanvolbln() {
		return loanvolbln;
	}

	public void setLoanvolbln(Double loanvolbln) {
		this.loanvolbln = loanvolbln;
	}

	public Double getFinavolchg() {
		return finavolchg;
	}

	public void setFinavolchg(Double finavolchg) {
		this.finavolchg = finavolchg;
	}

	public Double getFinavolbln() {
		return finavolbln;
	}

	public void setFinavolbln(Double finavolbln) {
		this.finavolbln = finavolbln;
	}

	public Double getTax_addedchg() {
		return tax_addedchg;
	}

	public void setTax_addedchg(Double tax_addedchg) {
		this.tax_addedchg = tax_addedchg;
	}

	public Double getTax_addedbln() {
		return tax_addedbln;
	}

	public void setTax_addedbln(Double tax_addedbln) {
		this.tax_addedbln = tax_addedbln;
	}
	
	public Double getTax_rplbln() {
		return tax_rplbln;
	}

	public void setTax_rplbln(Double tax_rplbln) {
		this.tax_rplbln = tax_rplbln;
	}

	public Double getLoanvolchg() {
		return loanvolchg;
	}

	public void setLoanvolchg(Double loanvolchg) {
		this.loanvolchg = loanvolchg;
	}

	public Double getTax_interestbln() {
		return tax_interestbln;
	}

	public void setTax_interestbln(Double tax_interestbln) {
		this.tax_interestbln = tax_interestbln;
	}

	public Double getTax_rplchg() {
		return tax_rplchg;
	}

	public void setTax_rplchg(Double tax_rplchg) {
		this.tax_rplchg = tax_rplchg;
	}
	
	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getAccount_code() {
		return account_code;
	}

	public void setAccount_code(String account_code) {
		this.account_code = account_code;
	}

	public Double getFeeyieldrebate () {
		return feeyieldrebate;
	}

	public void setFeeyieldrebate (Double feeyieldrebate) {
		this.feeyieldrebate=feeyieldrebate;
	}

	public Integer getIs_repo () {
		return is_repo;
	}

	public void setIs_repo (Integer is_repo) {
		this.is_repo=is_repo;
	}

	public void setCfets_tax (Double cfets_tax) {
		this.cfets_tax=cfets_tax;
	}

	public int getFund_type () {
		return fund_type;
	}

	public void setFund_type (int fund_type) {
		this.fund_type=fund_type;
	}

	public void setCfets_trans (Double cfets_trans) {
		this.cfets_trans=cfets_trans;
	}

	public Double getCfets_tax () {
		return cfets_tax;
	}

	public Double getCfets_trans () {
		return cfets_trans;
	}

	/**
	 * 交易中心过户费
	 */
	public Double cfets_trans;

	public String getAlgorithmforfee() {
		return algorithmforfee;
	}

	public void setAlgorithmforfee(String algorithmforfee) {
		this.algorithmforfee = algorithmforfee;
	}

	public BigDecimal getTax_accruincomechg() {
		return tax_accruincomechg;
	}

	public void setTax_accruincomechg(BigDecimal tax_accruincomechg) {
		this.tax_accruincomechg = tax_accruincomechg;
	}

	public BigDecimal getTax_positionchg() {
		return tax_positionchg;
	}

	public void setTax_positionchg(BigDecimal tax_positionchg) {
		this.tax_positionchg = tax_positionchg;
	}

	public BigDecimal getTax_positionbln() {
		return tax_positionbln;
	}

	public void setTax_positionbln(BigDecimal tax_positionbln) {
		this.tax_positionbln = tax_positionbln;
	}

	public BigDecimal getTax_rpl() {
		return tax_rpl;
	}

	public void setTax_rpl(BigDecimal tax_rpl) {
		this.tax_rpl = tax_rpl;
	}

	public Integer getTax_child_adtype() {
		return tax_child_adtype;
	}

	public void setTax_child_adtype(Integer tax_child_adtype) {
		this.tax_child_adtype = tax_child_adtype;
	}

	public String getTax_ftool_code() {
		return tax_ftool_code;
	}

	public void setTax_ftool_code(String tax_ftool_code) {
		this.tax_ftool_code = tax_ftool_code;
	}

	public Double getTax_rate() {
		return tax_rate;
	}

	public void setTax_rate(Double tax_rate) {
		this.tax_rate = tax_rate;
	}

	public Double getTax_rplrate() {
		return tax_rplrate;
	}

	public void setTax_rplrate(Double tax_rplrate) {
		this.tax_rplrate = tax_rplrate;
	}

	public BigDecimal getTax_yztaxchg() {
		return tax_yztaxchg;
	}

	public void setTax_yztaxchg(BigDecimal tax_yztaxchg) {
		this.tax_yztaxchg = tax_yztaxchg;
	}

	public String getTax_type() {
		return tax_type;
	}

	public void setTax_type(String tax_type) {
		this.tax_type = tax_type;
	}

	public Double getEndjz() {
		return endjz;
	}

	public void setEndjz(Double endjz) {
		this.endjz = endjz;
	}

	public Double getEndjz_net() {
		return endjz_net;
	}

	public void setEndjz_net(Double endjz_net) {
		this.endjz_net = endjz_net;
	}

	public Double getBeginjz() {
		return beginjz;
	}

	public void setBeginjz(Double beginjz) {
		this.beginjz = beginjz;
	}

	public Double getBeginjz_net() {
		return beginjz_net;
	}

	public void setBeginjz_net(Double beginjz_net) {
		this.beginjz_net = beginjz_net;
	}

	public Double getIr_rate() {
		return ir_rate;
	}

	public void setIr_rate(Double ir_rate) {
		this.ir_rate = ir_rate;
	}

	public int getDivisor() {
		return divisor;
	}

	public void setDivisor(int divisor) {
		this.divisor = divisor;
	}

	public double getIr_rate_balance() {
		return ir_rate_balance;
	}

	public void setIr_rate_balance(double ir_rate_balance) {
		this.ir_rate_balance = ir_rate_balance;
	}

	public String getOld_ftool_code() {
		return old_ftool_code;
	}

	public void setOld_ftool_code(String old_ftool_code) {
		this.old_ftool_code = old_ftool_code;
	}

	public Double getAst_positionbln() {
		return ast_positionbln;
	}

	public void setAst_positionbln(Double ast_positionbln) {
		this.ast_positionbln = ast_positionbln;
	}

	public Integer getIs_fee_trade() {
		return is_fee_trade;
	}

	public void setIs_fee_trade(Integer is_fee_trade) {
		this.is_fee_trade = is_fee_trade;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public String getAsset_type() {
		return asset_type;
	}

	public void setAsset_type(String asset_type) {
		this.asset_type = asset_type;
	}

	public String getEvent_name() {
		return event_name;
	}

	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}
	public Integer getProbaseSmid() {
		return probaseSmid;
	}
	public void setProbaseSmid(Integer probaseSmid) {
		this.probaseSmid = probaseSmid;
	}
	public Integer getDealid() {
		return dealid;
	}
	public void setDealid(Integer dealid) {
		this.dealid = dealid;
	}
	public BigDecimal getFullprice() {
		return fullprice;
	}
	public void setFullprice(BigDecimal fullprice) {
		this.fullprice = fullprice;
	}
	public BigDecimal getCleanprice() {
		return cleanprice;
	}
	public void setCleanprice(BigDecimal cleanprice) {
		this.cleanprice = cleanprice;
	}
	public BigDecimal getFaceamount() {
		return faceamount;
	}
	public void setFaceamount(BigDecimal faceamount) {
		this.faceamount = faceamount;
	}
	public BigDecimal getInterestcost() {
		return interestcost;
	}
	public void setInterestcost(BigDecimal interestcost) {
		this.interestcost = interestcost;
	}
	public BigDecimal getBeginamount() {
		return beginamount;
	}
	public void setBeginamount(BigDecimal beginamount) {
		this.beginamount = beginamount;
	}
	public BigDecimal getEndamount() {
		return endamount;
	}
	public void setEndamount(BigDecimal endamount) {
		this.endamount = endamount;
	}
	public BigDecimal getBeginfaceamount() {
		return beginfaceamount;
	}
	public void setBeginfaceamount(BigDecimal beginfaceamount) {
		this.beginfaceamount = beginfaceamount;
	}
	public BigDecimal getEndfaceamount() {
		return endfaceamount;
	}
	public void setEndfaceamount(BigDecimal endfaceamount) {
		this.endfaceamount = endfaceamount;
	}
	public BigDecimal getTransyield() {
		return transyield;
	}
	public void setTransyield(BigDecimal transyield) {
		this.transyield = transyield;
	}
	public Integer getTranslevel() {
		return translevel;
	}
	public void setTranslevel(Integer translevel) {
		this.translevel = translevel;
	}
	public String getIsexercise() {
		return isexercise;
	}
	public void setIsexercise(String isexercise) {
		this.isexercise = isexercise;
	}
	public String getPdyield() {
		return pdyield;
	}
	public void setPdyield(String pdyield) {
		this.pdyield = pdyield;
	}
	public Double getFeeyield() {
		return feeyield;
	}
	public void setFeeyield(Double feeyield) {
		this.feeyield = feeyield;
	}
	public String getOptionday() {
		return optionday;
	}
	public void setOptionday(String optionday) {
		this.optionday = optionday;
	}
	public String getFixbondid() {
		return fixbondid;
	}
	public void setFixbondid(String fixbondid) {
		this.fixbondid = fixbondid;
	}
	public String getFtool_name() {
		return ftool_name;
	}
	public void setFtool_name(String ftool_name) {
		this.ftool_name = ftool_name;
	}
	public String getFtool_code() {
		return ftool_code;
	}
	public void setFtool_code(String ftool_code) {
		this.ftool_code = ftool_code;
	}
	public String getBond_market() {
		return bond_market;
	}
	public void setBond_market(String bond_market) {
		this.bond_market = bond_market;
	}

	public String getOpenpdid() {
		return openpdid;
	}
	public void setOpenpdid(String openpdid) {
		this.openpdid = openpdid;
	}
	public String getPdid() {
		return pdid;
	}
	public void setPdid(String pdid) {
		this.pdid = pdid;
	}
	public String getProducttypeid() {
		return producttypeid;
	}
	public void setProducttypeid(String producttypeid) {
		this.producttypeid = producttypeid;
	}
	public String getRelation_no() {
		return relation_no;
	}
	public void setRelation_no(String relation_no) {
		this.relation_no = relation_no;
	}
	public String getDealtype() {
		return dealtype;
	}
	public void setDealtype(String dealtype) {
		this.dealtype = dealtype;
	}
	public String getFixdealid() {
		return fixdealid;
	}
	public void setFixdealid(String fixdealid) {
		this.fixdealid = fixdealid;
	}

	public Integer getT8_sys_acceven_id() {
		return t8_sys_acceven_id;
	}
	public void setT8_sys_acceven_id(Integer t8_sys_acceven_id) {
		this.t8_sys_acceven_id = t8_sys_acceven_id;
	}
	public String getCounterpartyid() {
		return counterpartyid;
	}
	public void setCounterpartyid(String counterpartyid) {
		this.counterpartyid = counterpartyid;
	}
	public Date getSettle_date() {
		return settle_date;
	}
	public void setSettle_date(Date settle_date) {
		this.settle_date = settle_date;
	}
	public Date getAccount_date() {
		return account_date;
	}
	public void setAccount_date(Date account_date) {
		this.account_date = account_date;
	}
	public String getLastsharedate() {
		return lastsharedate;
	}
	public void setLastsharedate(String lastsharedate) {
		this.lastsharedate = lastsharedate;
	}
	public String getDealtax() {
		return dealtax;
	}
	public void setDealtax(String dealtax) {
		this.dealtax = dealtax;
	}
	public BigDecimal getFairprice() {
		return fairprice;
	}
	public void setFairprice(BigDecimal fairprice) {
		this.fairprice = fairprice;
	}
	public String getShareinterval() {
		return shareinterval;
	}
	public void setShareinterval(String shareinterval) {
		this.shareinterval = shareinterval;
	}
	public String getCoupon() {
		return coupon;
	}
	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}
	public BigDecimal getUnitprincipal() {
		return unitprincipal;
	}
	public void setUnitprincipal(BigDecimal unitprincipal) {
		this.unitprincipal = unitprincipal;
	}
	public String getDealno() {
		return dealno;
	}
	public void setDealno(String dealno) {
		this.dealno = dealno;
	}
	public String getRepodealno() {
		return repodealno;
	}
	public void setRepodealno(String repodealno) {
		this.repodealno = repodealno;
	}
	public BigDecimal getTransamount() {
		return transamount;
	}
	public void setTransamount(BigDecimal transamount) {
		this.transamount = transamount;
	}
	public BigDecimal getNpamount() {
		return npamount;
	}
	public void setNpamount(BigDecimal npamount) {
		this.npamount = npamount;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getNetprice() {
		return netprice;
	}
	public void setNetprice(BigDecimal netprice) {
		this.netprice = netprice;
	}


	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public String getRepotrans_id() {
		return repotrans_id;
	}

	public void setRepotrans_id(String repotrans_id) {
		this.repotrans_id = repotrans_id;
	}

	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
	}

	public BigDecimal getMaturedealrate() {
		return maturedealrate;
	}

	public void setMaturedealrate(BigDecimal maturedealrate) {
		this.maturedealrate = maturedealrate;
	}



	public Integer getT8_sys_adtype_id() {
		return t8_sys_adtype_id;
	}

	public void setT8_sys_adtype_id(Integer t8_sys_adtype_id) {
		this.t8_sys_adtype_id = t8_sys_adtype_id;
	}

	public Integer getT8_prod_base_id() {
		return t8_prod_base_id;
	}

	public void setT8_prod_base_id(Integer t8_prod_base_id) {
		this.t8_prod_base_id = t8_prod_base_id;
	}

	public Integer getT8_sys_portfol_id() {
		return t8_sys_portfol_id;
	}

	public void setT8_sys_portfol_id(Integer t8_sys_portfol_id) {
		this.t8_sys_portfol_id = t8_sys_portfol_id;
	}

	public Integer getT8_channel_bas_id() {
		return t8_channel_bas_id;
	}

	public void setT8_channel_bas_id(Integer t8_channel_bas_id) {
		this.t8_channel_bas_id = t8_channel_bas_id;
	}

	public String getAccount_type() {
		return account_type;
	}

	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}

	public String getT8_deal_info_id() {
		return t8_deal_info_id;
	}

	public void setT8_deal_info_id(String t8_deal_info_id) {
		this.t8_deal_info_id = t8_deal_info_id;
	}

	public BigDecimal getRealrate() {
		return realrate;
	}

	public void setRealrate(BigDecimal realrate) {
		this.realrate = realrate;
	}

	public Integer getRepotrans_type() {
		return repotrans_type;
	}

	public void setRepotrans_type(Integer repotrans_type) {
		this.repotrans_type = repotrans_type;
	}

	public Integer getT8_prod_umbrei_id() {
		return t8_prod_umbrei_id;
	}

	public void setT8_prod_umbrei_id(Integer t8_prod_umbrei_id) {
		this.t8_prod_umbrei_id = t8_prod_umbrei_id;
	}

	public BigDecimal getFeepaybln() {
		return feepaybln;
	}

	public void setFeepaybln(BigDecimal feepaybln) {
		this.feepaybln = feepaybln;
	}

	public Integer getProfitloss_id() {
		return profitloss_id;
	}

	public void setProfitloss_id(Integer profitloss_id) {
		this.profitloss_id = profitloss_id;
	}

	public Date getFairpricedate() {
		return fairpricedate;
	}

	public void setFairpricedate(Date fairpricedate) {
		this.fairpricedate = fairpricedate;
	}

	public Integer getRate_base() {
		return rate_base;
	}

	public void setRate_base(Integer rate_base) {
		this.rate_base = rate_base;
	}

	public Double getProfitchg() {
		return profitchg;
	}

	public void setProfitchg(Double profitchg) {
		this.profitchg = profitchg;
	}

	public Double getProfitbln() {
		return profitbln;
	}

	public void setProfitbln(Double profitbln) {
		this.profitbln = profitbln;
	}

	public Double getPayfitbln() {
		return payfitbln;
	}

	public void setPayfitbln(Double payfitbln) {
		this.payfitbln = payfitbln;
	}

	public Double getPayfitchg() {
		return payfitchg;
	}

	public void setPayfitchg(Double payfitchg) {
		this.payfitchg = payfitchg;
	}

	public Double getUndsfitchg() {
		return undsfitchg;
	}

	public void setUndsfitchg(Double undsfitchg) {
		this.undsfitchg = undsfitchg;
	}

	public Double getUndsfitbln() {
		return undsfitbln;
	}

	public void setUndsfitbln(Double undsfitbln) {
		this.undsfitbln = undsfitbln;
	}
	
	public Double getDztaxchg() {
		return dztaxchg;
	}

	public String getFee_mod() {
		return fee_mod;
	}

	public void setFee_mod(String fee_mod) {
		this.fee_mod = fee_mod;
	}

	public Date getOri_settle_date() {
		return ori_settle_date;
	}

	public void setOri_settle_date(Date ori_settle_date) {
		this.ori_settle_date = ori_settle_date;
	}

	public void setDztaxchg(Double dztaxchg) {
		this.dztaxchg = dztaxchg;
	}

	public Double getDztaxbln() {
		return dztaxbln;
	}

	public void setDztaxbln(Double dztaxbln) {
		this.dztaxbln = dztaxbln;
	}
	
	public Double getDzjcchg() {
		return dzjcchg;
	}

	public void setDzjcchg(Double dzjcchg) {
		this.dzjcchg = dzjcchg;
	}

	public Double getDzjcbln() {
		return dzjcbln;
	}

	public void setDzjcbln(Double dzjcbln) {
		this.dzjcbln = dzjcbln;
	}
	
	public Double getJcfeechg() {
		return jcfeechg;
	}

	public void setJcfeechg(Double jcfeechg) {
		this.jcfeechg = jcfeechg;
	}

	public Double getJcfeebln() {
		return jcfeebln;
	}

	public void setJcfeebln(Double jcfeecbln) {
		this.jcfeebln = jcfeecbln;
	}

	public double getReal_cost() {
		return real_cost;
	}

	public void setReal_cost(double real_cost) {
		this.real_cost = real_cost;
	}

	public String getFair_date() {
		return fair_date;
	}

	public void setFair_date(String fair_date) {
		this.fair_date = fair_date;
	}

	public BigDecimal getFee_money() {
		return fee_money;
	}

	public void setFee_money(BigDecimal fee_money) {
		this.fee_money = fee_money;
	}

	public BigDecimal getProd_famount() {
		return prod_famount;
	}

	public void setProd_famount(BigDecimal prod_famount) {
		this.prod_famount = prod_famount;
	}

	public BigDecimal getThisnetprice() {
		return thisnetprice;
	}

	public void setThisnetprice(BigDecimal thisnetprice) {
		this.thisnetprice = thisnetprice;
	}

	public String getInvest_object() {
		return invest_object;
	}

	public void setInvest_object(String invest_object) {
		this.invest_object = invest_object;
	}

	public BigDecimal getThisfullprice() {
		return thisfullprice;
	}

	public void setThisfullprice(BigDecimal thisfullprice) {
		this.thisfullprice = thisfullprice;
	}

	public Integer getJxdays() {
		return jxdays;
	}

	public void setJxdays(Integer jxdays) {
		this.jxdays = jxdays;
	}


	public Double getCostchg() {
		return costchg;
	}

	public void setCostchg(Double costchg) {
		this.costchg = costchg;
	}

	public Integer getIs_confirm() {
		return is_confirm;
	}

	public void setIs_confirm(Integer is_confirm) {
		this.is_confirm = is_confirm;
	}

	public Double getCostbln() {
		return costbln;
	}

	public void setCostbln(Double costbln) {
		this.costbln = costbln;
	}
	
	public void setInterestincomechg(Double interestincomechg) {
		this.interestincomechg = interestincomechg;
	}
	
	public Double getInterestincomechg() {
		return interestincomechg;
	}

	public Double getInterestincomebln() {
		return interestincomebln;
	}
	
	public void setInterestincomebln(Double interestincomebln) {
		this.interestincomebln = interestincomebln;
	}

	public Integer getIs_last() {
		return is_last;
	}

	public void setIs_last(Integer is_last) {
		this.is_last = is_last;
	}

	public Integer getIs_change() {
		return is_change;
	}

	public void setIs_change(Integer is_change) {
		this.is_change = is_change;
	}

	public BigDecimal getDuration() {
		return duration;
	}

	public void setDuration(BigDecimal duration) {
		this.duration = duration;
	}

	public String getCuy() {
		return cuy;
	}

	public void setCuy(String cuy) {
		this.cuy = cuy;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getIssu_ccy() {
		return issu_ccy;
	}

	public void setIssu_ccy(String issu_ccy) {
		this.issu_ccy = issu_ccy;
	}

	public BigDecimal getFcu_interestincome() {
		return fcu_interestincome;
	}

	public void setFcu_interestincome(BigDecimal fcu_interestincome) {
		this.fcu_interestincome = fcu_interestincome;
	}
	
	

	public Double getReceivableschg() {
		return receivableschg;
	}

	public void setReceivableschg(Double receivableschg) {
		this.receivableschg = receivableschg;
	}

	public Double getReceivablesbln() {
		return receivablesbln;
	}

	public void setReceivablesbln(Double receivablesbln) {
		this.receivablesbln = receivablesbln;
	}
	
	

	public BigDecimal getAllamount() {
		return allamount;
	}

	public void setAllamount(BigDecimal allamount) {
		this.allamount = allamount;
	}

	public Integer getPay_mod() {
		return pay_mod;
	}

	public void setPay_mod(Integer pay_mod) {
		this.pay_mod = pay_mod;
	}	

	public String getIncome_type() {
		return income_type;
	}

	public void setIncome_type(String income_type) {
		this.income_type = income_type;
	}
	

	public String getProd_mod() {
		return prod_mod;
	}

	public void setProd_mod(String prod_mod) {
		this.prod_mod = prod_mod;
	}
	
	

	public BigDecimal getMyriad_income() {
		return myriad_income;
	}

	public void setMyriad_income(BigDecimal myriad_income) {
		this.myriad_income = myriad_income;
	}
	
	

	public Double getDouble_one() {
		return double_one;
	}

	public void setDouble_one(Double double_one) {
		this.double_one = double_one;
	}

	public Double getDouble_two() {
		return double_two;
	}

	public void setDouble_two(Double double_two) {
		this.double_two = double_two;
	}

	public Double getDouble_three() {
		return double_three;
	}

	public void setDouble_three(Double double_three) {
		this.double_three = double_three;
	}

	public Double getDouble_four() {
		return double_four;
	}

	public void setDouble_four(Double double_four) {
		this.double_four = double_four;
	}

	public Double getDouble_fif() {
		return double_fif;
	}

	public void setDouble_fif(Double double_fif) {
		this.double_fif = double_fif;
	}

	public String getChar_one() {
		return char_one;
	}

	public void setChar_one(String char_one) {
		this.char_one = char_one;
	}

	public String getChar_two() {
		return char_two;
	}

	public void setChar_two(String char_two) {
		this.char_two = char_two;
	}

	public String getChar_three() {
		return char_three;
	}

	public void setChar_three(String char_three) {
		this.char_three = char_three;
	}

	public String getChar_four() {
		return char_four;
	}

	public void setChar_four(String char_four) {
		this.char_four = char_four;
	}

	public String getChar_fif() {
		return char_fif;
	}

	public void setChar_fif(String char_fif) {
		this.char_fif = char_fif;
	}

	public Object clone() {
		Object object = null;
		try{
			object = super.clone();
		} catch (CloneNotSupportedException e) {
			object = null;
		}
		return object;
	}

	
}
