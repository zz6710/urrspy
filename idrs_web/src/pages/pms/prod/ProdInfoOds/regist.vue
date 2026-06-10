<template>
	<k-form :data-col="3" dataLabelWidth="200px" ref="formRef">
		<k-form-item label="产品代码" :class="[handleItemDiff('prodCode')]">
			<k-field-text v-model="formData.prodCode" :data-disabled="isDisabled || isDetail" />
		</k-form-item>
		<!--<k-form-item label="产品名称">
			<k-field-text v-model="formData.prodName" :data-disabled="isDetail" />
		</k-form-item>-->
		<k-form-item label="产品全称" :class="[handleItemDiff('prodNmFu')]">
			<k-field-text v-model="formData.prodNmFu" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品投资性质" :class="[handleItemDiff('t8InvestPropType')]">
			<k-field-select v-model="formData.t8InvestPropType" data-dict="prod_invest_nature" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品运作模式" :class="[handleItemDiff('prodMod')]">
			<k-field-select v-model="formData.prodMod" data-dict="prod_mod_sys" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品募集方式" :class="[handleItemDiff('collMod')]">
			<k-field-select v-model="formData.collMod" data-dict="t8_raise_type" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品期限" :class="[handleItemDiff('prodCycle')]">
			<k-field-select v-model="formData.prodCycle" data-dict="prod_term" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品品牌" :class="[handleItemDiff('prodBrand')]">
			<k-field-text v-model="formData.prodBrand" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品期次" :class="[handleItemDiff('prodTimes')]">
			<k-field-text v-model="formData.prodTimes" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="募集币种" :class="[handleItemDiff('issuCcy')]">
			<k-field-text v-model="formData.issuCcy" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="兑付本金币种" :class="[handleItemDiff('returnCcy')]">
			<k-field-text v-model="formData.returnCcy" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="兑付收益币种" :class="[handleItemDiff('incomeCcy')]">
			<k-field-text v-model="formData.incomeCcy" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="发行机构提前终止权标识" :class="[handleItemDiff('termFlag')]">
			<k-field-select v-model="formData.termFlag" data-dict="t8_redeem_flag" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="客户赎回权标识(中债)" :class="[handleItemDiff('redeemFlag')]">
			<k-field-select v-model="formData.redeemFlag" data-dict="t8_redeem_flag" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品增信标识" :class="[handleItemDiff('prodCreditFlag')]">
			<k-field-select v-model="formData.prodCreditFlag" data-dict="t8_redeem_flag" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品增信机构类型" :class="[handleItemDiff('prodCreditOrg')]">
			<k-field-select v-model="formData.prodCreditOrg" data-dict="out_cpzxjglx" data-dict-type="1" :data-disabled="isDetail || creditDisabled" />
		</k-form-item>
		<k-form-item label="产品增信形式" :class="[handleItemDiff('prodCreditMod')]">
			<k-field-select v-model="formData.prodCreditMod" data-dict="out_cpzxxs" data-dict-type="1" :data-disabled="isDetail || creditDisabled" />
		</k-form-item>
		<k-form-item label="境内托管机构代码(中债)" :class="[handleItemDiff('bordTrustiCode')]">
			<k-field-text v-model="formData.bordTrustiCode" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="境外托管机构国别" :class="[handleItemDiff('oversTrustiNation')]">
			<k-field-select v-model="formData.oversTrustiNation" :data-disabled="isDetail" data-dict="worldCountCode" data-dict-type="1" />
		</k-form-item>
		<k-form-item label="境内托管机构名称" :class="[handleItemDiff('bordTrustiName')]">
			<k-field-select v-model="formData.bordTrustiName" data-dict="domestic_custodian_name" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="境外托管机构名称" :class="[handleItemDiff('oversTrustiName')]">
			<k-field-text v-model="formData.oversTrustiName" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="是否现金管理类(中债)" :class="[handleItemDiff('cashTypeZ')]">
			<k-field-select v-model="formData.cashTypeZ" data-dict="1yes2no" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品收益类型" :class="[handleItemDiff('incomeType')]">
			<k-field-select v-model="formData.incomeType" data-dict="prod_revenue_type" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="新老产品标记" :class="[handleItemDiff('newOldProdF')]">
			<k-field-select v-model="formData.newOldProdF" data-dict="newProd" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="是否金融同业专属" :class="[handleItemDiff('blgFinSamBusF')]">
			<k-field-select v-model="formData.blgFinSamBusF" data-dict="1yes0no" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="资金投向地区" :class="[handleItemDiff('salePlace')]">
			<k-field-select v-model="formData.salePlace" data-dict="out_zjtxdq" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品投资国家或地区(境外)" :class="[handleItemDiff('speciCountryRegion')]">
			<k-field-select v-model="formData.speciCountryRegion" data-dict="worldCountCode" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="理财业务服务模式" :class="[handleItemDiff('srvMode')]">
			<k-field-select v-model="formData.srvMode" data-dict="t8_srv_mode" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品资产配置方式" :class="[handleItemDiff('assetMaping')]">
			<k-field-select v-model="formData.assetMaping" data-dict="t8_asset_maping" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品管理模式" :class="[handleItemDiff('manageMode')]">
			<k-field-select v-model="formData.manageMode" data-dict="t8_prod_mng_mod" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="实际管理人名称" :class="[handleItemDiff('adminName')]">
			<k-field-text v-model="formData.adminName" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品定价方式" :class="[handleItemDiff('pricingType')]">
			<k-field-select v-model="formData.pricingType" data-dict="prod_price_way" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="业绩比较基准%" :class="[handleItemDiff('perfmBenchmRate')]">
			<k-field-text v-model="formData.perfmBenchmRate" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="是否设置最短持有期限" :class="[handleItemDiff('isMinHoldTerm')]">
			<k-field-select v-model="formData.isMinHoldTerm" data-dict="1yes2no" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="最短持有期限(天)" :class="[handleItemDiff('minHoldTerm')]">
			<k-field-text v-model="formData.minHoldTerm" data-validate-type="int" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="最短持有期后是否自由赎回" :class="[handleItemDiff('redeemAfterHold')]">
			<k-field-select v-model="formData.redeemAfterHold" data-dict="optionRedemptPeriod" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="起点销售金额" :class="[handleItemDiff('investThresh')]">
			<k-field-text v-model="formData.investThresh" data-validate-type="money" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品销售区域" style="width:1220px" :class="[handleItemDiff('prodSalZon')]">
			<k-field-select style="width:100%" v-model="formData.prodSalZon" data-dict="subm_prod_sale_area" :data-multiple="true" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="销售手续费率%" :class="[handleItemDiff('saleCommisRate')]">
			<k-field-text v-model="formData.saleCommisRate" data-validate-type="number" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="托管费率%" :class="[handleItemDiff('custodyFeeRate')]">
			<k-field-text v-model="formData.custodyFeeRate" data-validate-type="number" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="募集起始日期(从)" :class="[handleItemDiff('subscrSdEarliest')]">
			<k-field-date v-model="formData.subscrSdEarliest" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="募集起始日期(到)" :class="[handleItemDiff('subscrEdLatest')]">
			<k-field-date v-model="formData.subscrEdLatest" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="计划募集金额(元)" :class="[handleItemDiff('planFundAmount')]">
			<k-field-text v-model="formData.planFundAmount" data-validate-type="money" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="投资者风险偏好" :class="[handleItemDiff('investorTrend')]">
			<k-field-select v-model="formData.investorTrend" data-dict="investor_risk_preference" :data-multiple="true" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品风险等级" :class="[handleItemDiff('riskLev')]">
			<k-field-select v-model="formData.riskLev" data-dict="risk_rate" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品特殊属性" :class="[handleItemDiff('prodEspPrpt')]">
			<k-field-select v-model="formData.prodEspPrpt" data-dict="prodSpecialProp" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="投资管理费率%" :class="[handleItemDiff('investMngFeeRate')]">
			<k-field-text v-model="formData.investMngFeeRate" data-validate-type="number" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="合作模式" :class="[handleItemDiff('cooperationMode')]">
			<k-field-select v-model="formData.cooperationMode" data-dict="coorpMode" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="合作机构名称" :class="[handleItemDiff('cooperationOrgName')]">
			<k-field-text v-model="formData.cooperationOrgName" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="投资本金到账日" :class="[handleItemDiff('returnCost')]">
			<k-field-select v-model="formData.returnCost" data-dict="invest_income_arrive_date" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="投资收益到账日" :class="[handleItemDiff('returnIncome')]">
			<k-field-select v-model="formData.returnIncome" data-dict="invest_income_arrive_date" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="投资资产种类及比例" :class="[handleItemDiff('prodPrecent')]">
			<k-field-text v-model="formData.prodPrecent" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品审批人姓名" :class="[handleItemDiff('authorName')]">
			<k-field-text v-model="formData.authorName" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品审批人身份证号" :class="[handleItemDiff('authorIdentif')]">
			<k-field-text v-model="formData.authorIdentif" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品设计人姓名" :class="[handleItemDiff('designName')]">
			<k-field-text v-model="formData.designName" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品设计人身份证号" :class="[handleItemDiff('designIdentif')]">
			<k-field-text v-model="formData.designIdentif" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="投资经理姓名" :class="[handleItemDiff('manageName')]">
			<k-field-text v-model="formData.manageName" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="投资经理身份证号" :class="[handleItemDiff('manageIdentif')]">
			<k-field-text v-model="formData.manageIdentif" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="业务联络人姓名" :class="[handleItemDiff('salemanName')]">
			<k-field-text v-model="formData.salemanName" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="业务联络人座机" :class="[handleItemDiff('salemanPhoneno')]">
			<k-field-text v-model="formData.salemanPhoneno" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="业务联络人手机" :class="[handleItemDiff('salemanTelno')]">
			<k-field-text v-model="formData.salemanTelno" date-validate-type="telephone" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="业务联络人邮箱" :class="[handleItemDiff('salemanEmail')]">
			<k-field-text v-model="formData.salemanEmail" date-validate-type="email" :data-disabled="isDetail" />
		</k-form-item>
	</k-form>
</template>
<script>
export default {
	props: {
		formData: {
			type: Object,
			default: () => {
				return {};
			},
		},
		formDataCopy: {
			type: Object,
			default: () => {
				return {};
			},
		},
		type: String,
	},
	computed: {
		isDisabled() {
			return this.type == "edit";
		},
		isDetail() {
			return this.type == "detail";
		},
		creditDisabled() {
			return this.formData.prodCreditFlag == "02";
		}
	},
	watch: {
		"formData.prodCreditFlag": {
			handler(v) {
				if (v == "02") {
					this.$set(this.formData, "prodCreditOrg", "");
					this.$set(this.formData, "prodCreditMod", "");
				} else {

				}
			},
		},
	},
};
</script>
