<template>
	<k-form :data-col="3" dataLabelWidth="200px">
		<k-form-item label="中债登记编码" :class="[handleItemDiff('checkInon')]">
			<k-field-text v-model="formData.checkInon" :data-disabled="isDisabled || isDetail" />
		</k-form-item>
		<k-form-item label="资金托管账号" :class="[handleItemDiff('accountCode')]">
			<k-field-text v-model="formData.accountCode" :data-disabled="isDetail" />
		</k-form-item>
		<k-form-item label="资金托管账户" :class="[handleItemDiff('accountName')]">
			<k-field-text v-model="formData.accountName" :data-disabled="isDetail" />
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
};
</script>
