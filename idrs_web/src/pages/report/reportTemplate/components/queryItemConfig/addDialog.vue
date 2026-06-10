<template>
	<div>
		<div class="panel-body">
				<k-form ref="refForm">
					<k-form-item label="查询名称">
						<k-field-text v-model="formData.queryName" :data-allowblank="false" />
					</k-form-item>
					<k-form-item label="地址映射id">
						<k-field-text v-model="formData.menuid" :data-allowblank="false" />
					</k-form-item>
					<k-form-item label="SQL语句" data-input-width="577px">
						<k-field-text input-type="textarea" :rows="5" :data-allowblank="false"></k-field-text>
					</k-form-item>
				</k-form>
			</div>
			<k-form-footer data-align="center">
				<k-btn
					class="md-success"
					data-target="tableGrid"
					data-descript="保存SQL信息"
					data-functype="SUBMIT"
					data-from="refForm"
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