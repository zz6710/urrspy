<template>
	<k-form :data-col="3" dataLabelWidth="200px">
		<k-form-item label="人行登记编码" :class="[handleItemDiff('pbcRegcode')]">
			<k-field-text v-model="formData.pbcRegcode" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品全称" :class="[handleItemDiff('prodNmFu')]">
			<k-field-text v-model="formData.prodNmFu" :data-disabled="isDisabled || isDetail" />
		</k-form-item>
		<k-form-item label="中债登记编码" :class="[handleItemDiff('checkInon')]">
			<k-field-text v-model="formData.checkInon" :data-disabled="isDisabled || isDetail" />
		</k-form-item>
		<k-form-item label="产品投资性质" :class="[handleItemDiff('t8InvestPropType')]">
			<k-field-select v-model="formData.t8InvestPropType" :data-disabled="isDisabled || isDetail" data-dict="prod_invest_nature" data-dict-type="1" />
		</k-form-item>
		<k-form-item label="产品运作模式" :class="[handleItemDiff('prodMod')]">
			<k-field-select v-model="formData.prodMod" :data-disabled="isDisabled || isDetail" data-dict="prod_mod_sys" data-dict-type="1" />
		</k-form-item>
		<k-form-item label="产品募集方式" :class="[handleItemDiff('collMod')]">
			<k-field-select v-model="formData.collMod" :data-disabled="isDisabled || isDetail" data-dict="t8_raise_type" data-dict-type="1" />
		</k-form-item>
		<k-form-item label="管理方式" :class="[handleItemDiff('operMod')]">
			<k-field-select v-model="formData.operMod" :data-disabled="isDisabled || isDetail" data-dict="managementMethod" data-dict-type="1" />
		</k-form-item>
		<k-form-item label="产品品牌" :class="[handleItemDiff('prodBrand')]">
			<k-field-text v-model="formData.prodBrand" :data-disabled="isDisabled || isDetail" />
		</k-form-item>
		<k-form-item label="产品期次" :class="[handleItemDiff('prodTimes')]">
			<k-field-text v-model="formData.prodTimes" :data-disabled="isDisabled || isDetail" />
		</k-form-item>
		<k-form-item label="募集币种" :class="[handleItemDiff('issuCcy')]">
			<k-field-text v-model="formData.issuCcy" :data-disabled="isDisabled || isDetail" />
		</k-form-item>
		<k-form-item label="兑付本金币种" :class="[handleItemDiff('returnCcy')]">
			<k-field-text v-model="formData.returnCcy" :data-disabled="isDisabled || isDetail" />
		</k-form-item>
		<k-form-item label="兑付收益币种" :class="[handleItemDiff('incomeCcy')]">
			<k-field-text v-model="formData.incomeCcy" :data-disabled="isDisabled || isDetail" />
		</k-form-item>
		<k-form-item label="募集起始日期" :class="[handleItemDiff('subsBdate')]">
			<k-field-date v-model="formData.subsBdate" :data-disabled="isDisabled || isDetail" />
		</k-form-item>
		<k-form-item label="募集结束日期" :class="[handleItemDiff('subsEdate')]">
			<k-field-date v-model="formData.subsEdate" :data-disabled="isDisabled || isDetail" />
		</k-form-item>
		<k-form-item label="发行机构提前终止权标识" :class="[handleItemDiff('termFlag')]">
			<k-field-select v-model="formData.termFlag" :data-disabled="isDisabled || isDetail" data-dict="t8_redeem_flag" data-dict-type="1" />
		</k-form-item>
		<k-form-item label="产品增信标识" :class="[handleItemDiff('prodCreditFlag')]">
			<k-field-select v-model="formData.prodCreditFlag" :data-disabled="isDisabled || isDetail" data-dict="t8_redeem_flag" data-dict-type="1" />
		</k-form-item>
		<k-form-item label="产品增信机构类型" :class="[handleItemDiff('prodCreditOrg')]">
			<k-field-select v-model="formData.prodCreditOrg" :data-disabled="isDisabled || isDetail" data-dict="out_cpzxjglx" data-dict-type="1" />
		</k-form-item>
		<k-form-item label="产品增信形式" :class="[handleItemDiff('prodCreditMod')]">
			<k-field-text v-model="formData.prodCreditMod" :data-disabled="isDisabled || isDetail" data-dcit="out_cpzxxs" data-dict-type="1" />
		</k-form-item>
		<k-form-item label="境内托管机构代码(人行)" :class="[handleItemDiff('bordTrustiCodeP')]">
			<k-field-text v-model="formData.bordTrustiCodeP" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="境外托管机构国别" :class="[handleItemDiff('oversTrustiNation')]">
			<k-field-select v-model="formData.oversTrustiNation" :data-disabled="isDisabled || isDetail" data-dict="worldCountCode" data-dict-type="1" />
		</k-form-item>
		<k-form-item label="境内托管机构名称" :class="[handleItemDiff('bordTrustiName')]">
			<k-field-select v-model="formData.bordTrustiName" :data-disabled="isDisabled || isDetail" data-dict="domestic_custodian_name" data-dict-type="1" />
		</k-form-item>
		<k-form-item label="境外托管机构名称" :class="[handleItemDiff('oversTrustiName')]">
			<k-field-text v-model="formData.oversTrustiName" :data-disabled="isDisabled || isDetail" />
		</k-form-item>
		<k-form-item label="产品起始日期" :class="[handleItemDiff('establishDate')]">
			<k-field-date v-model="formData.establishDate" :data-disabled="isDisabled || isDetail" />
		</k-form-item>
		<k-form-item label="产品预计终止日期" :class="[handleItemDiff('endDate')]">
			<k-field-date v-model="formData.endDate" :data-disabled="isDisabled || isDetail" />
		</k-form-item>
		<k-form-item label="是否现金管理类(人行)" :class="[handleItemDiff('cashType')]">
			<k-field-select v-model="formData.cashType" :data-disabled="isDetail" data-dict="1yes2no" data-dict-type="1" @data-on-change="changeCashType"/>
		</k-form-item>
		<k-form-item label="客户赎回权标识(人行)" :class="[handleItemDiff('redeemFlagPb')]">
			<k-field-select v-model="formData.redeemFlagPb" data-dict="t8_redeem_flag" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品变更日期" :class="[handleItemDiff('prodChangeDate')]">
			<k-field-date v-model="formData.prodChangeDate" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="受托机构管理职责" :class="[handleItemDiff('custodyOrgMngDuty')]">
			<k-field-select v-model="formData.custodyOrgMngDuty" data-dict="mngDuty" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="收益权转让产品标识" :class="[handleItemDiff('incomeTransProdMark')]">
			<k-field-select v-model="formData.incomeTransProdMark" data-dict="1yes2no" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="跨境理财通" :class="[handleItemDiff('crossBorderWealth')]">
			<k-field-select v-model="formData.crossBorderWealth" data-dict="1yes2no" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="基本信息公开标识" :class="[handleItemDiff('baseInfoOpenMark')]">
			<k-field-select v-model="formData.baseInfoOpenMark" data-dict="1yes2no" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="变更原因" :class="[handleItemDiff('changeReason')]">
			<k-field-text v-model="formData.changeReason" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品展期标识" :class="[handleItemDiff('prodRenewalMark')]">
			<k-field-select v-model="formData.prodRenewalMark" data-dict="1yes2no" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="是否处于清算中" :class="[handleItemDiff('liquidateMark')]">
			<k-field-select v-model="formData.liquidateMark" data-dict="1yes2no" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="最短开放周期" :class="[handleItemDiff('minPrdOpnCyc')]">
			<k-field-text v-model="formData.minPrdOpnCyc" :data-disabled="isDetail" />
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
		}
	},
	methods: {
		changeCashType() {
			const v = this.formData.cashType
			if (v == "01") {
					this.$set(this.formData, "rglrPrdOpnCyc", "0");
					this.$set(this.formData, "minPrdOpnCyc", "1");
				} else {
					this.$set(this.formData, "rglrPrdOpnCyc", "");
					this.$set(this.formData, "minPrdOpnCyc", "");
				}
		}
	}
};
</script>
