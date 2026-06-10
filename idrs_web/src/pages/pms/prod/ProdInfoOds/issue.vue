<template>
	<k-form :data-col="3" dataLabelWidth="200px">
		<k-form-item label="中债登记编码" :class="[handleItemDiff('checkInon')]">
			<k-field-text v-model="formData.checkInon" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="管理方式" :class="[handleItemDiff('operMod')]">
			<k-field-select v-model="formData.operMod" data-dict="managementMethod" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="募集起始日期" :class="[handleItemDiff('subsBdate')]">
			<k-field-date v-model="formData.subsBdate" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="募集结束日期" :class="[handleItemDiff('subsEdate')]">
			<k-field-date v-model="formData.subsEdate" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品起始日期" :class="[handleItemDiff('establishDate')]">
			<k-field-date v-model="formData.establishDate" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品实际终止日期" :class="[handleItemDiff('realEndDate')]">
			<k-field-date v-model="formData.realEndDate" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="产品预计终止日期" :class="[handleItemDiff('endDate')]">
			<k-field-date v-model="formData.endDate" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="是否为结构化(分级)产品" :class="[handleItemDiff('isStructprod')]">
			<k-field-select v-model="formData.isStructprod" data-dict="1yes2no" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="分级比例" :class="[handleItemDiff('clsfSto')]">
			<k-field-text v-model="formData.clsfSto" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="业绩比较基准上限%" :class="[handleItemDiff('perfmBenchmUpper')]">
			<k-field-text v-model="formData.perfmBenchmUpper" data-validate-type="number" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="业绩比较基准下限%" :class="[handleItemDiff('perfmBenchmLower')]">
			<k-field-text v-model="formData.perfmBenchmLower" data-validate-type="number" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="业绩比较基准说明" style="width:1220px" :class="[handleItemDiff('yjbjjzsmPj')]">
			<k-field-text v-model="formData.yjbjjzsmPj" input-type="textarea" :data-disabled="isDetail" :dataAutosize="true"/>
		</k-form-item>
		<k-form-item label="定期开放周期(天)" :class="[handleItemDiff('rglrPrdOpnCyc')]">
			<k-field-text v-model="formData.rglrPrdOpnCyc" data-validate-type="int" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="其他规律开放周期(天)" :class="[handleItemDiff('othRegulOpenCyc')]">
			<k-field-text v-model="formData.othRegulOpenCyc" data-validate-type="int" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="开放模式" :class="[handleItemDiff('openMod')]">
			<k-field-select v-model="formData.openMod" data-dict="open_mod" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="规律开放周期" :class="[handleItemDiff('regularOpenCycle')]">
			<k-field-select v-model="formData.regularOpenCycle" data-dict="regular_open_cycle" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="无规律开放说明" :class="[handleItemDiff('irregularOpenDesc')]">
			<k-field-text v-model="formData.irregularOpenDesc" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="首次开放周期起始日" :class="[handleItemDiff('firstOpenDate')]">
			<k-field-date v-model="formData.firstOpenDate" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="节假日是否开放" :class="[handleItemDiff('isOpenInHoliday')]">
			<k-field-select v-model="formData.isOpenInHoliday" data-dict="1yes2no" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="平均开放次数(年化)" :class="[handleItemDiff('openTimes')]">
			<k-field-text v-model="formData.openTimes" data-validate-type="number" :data-digits="2" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="开放期业务" :class="[handleItemDiff('openPeriodBusiness')]">
			<k-field-select v-model="formData.openPeriodBusiness" data-dict="t8_open_control" data-dict-type="1" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="开放期业务说明" :class="[handleItemDiff('openPeriBusiDesc')]">
			<k-field-text v-model="formData.openPeriBusiDesc" :data-disabled="isDetail" />
		</k-form-item>
	</k-form>
</template>
<script>
export default {
	props: {
		formData: {
			type: Object,
			default:() => {
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
		isDetail() {
			return this.type == "detail";
		}
	},
	watch: {
		"formData.rglrPrdOpnCyc": {
			handler(v) {
				if (this.formData.cashType != "01") {
                    this.$set(this.formData, "minPrdOpnCyc", v);
				}
			},
	    }
	},
};
</script>
