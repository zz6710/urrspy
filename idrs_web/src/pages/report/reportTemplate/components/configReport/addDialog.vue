<template>
	<div>
		<div class="panel-body">
			<k-form ref="refForm">
				<k-form-item label="报表菜单ID">
					<k-field-text v-model="formData.id" :data-allowblank="false" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="积木报表ID">
					<k-field-text v-model="formData.jimuReportId" :data-allowblank="false" />
				</k-form-item>
			</k-form>
		</div>
		<k-form-footer data-align="center">
			<k-btn
				class="btn-custom-primary"
				data-from="refForm"
				:data-model="formData"
				data-target="tableGrid"
				data-descript="保存报表信息"
				data-functype="SUBMIT"
			>
				<md-icon md-src="/static/svg/confirm.svg" />保存
			</k-btn>
			<k-btn class="btn-custom-plain" data-functype="CLOSE"><md-icon md-src="/static/svg/cancel.svg" />取消</k-btn>
		</k-form-footer>
	</div>
</template>
<script>
export default {
	props: {
		dataData: {
			type: Object,
			default: () => {
				return {};
			},
		},
		dataType: String,
	},
	watch: {
		dataData: {
			handler(v) {
				if (v) {
					this.formData = v;
				}
			},
			immediate: true,
		},
	},
	data() {
		return {
			formData: {}
		}
	}
};
</script>
