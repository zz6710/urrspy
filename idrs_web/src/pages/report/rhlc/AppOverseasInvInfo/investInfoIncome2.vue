<template>
	<div class="py-page">
		<!-- <div>
			<k-form-search-customize
				data-model-name="AppOverseasInvInfo2"
				data-label-width="80px"
				v-model="searchParam"
				data-target="appOverseasInvInfo2Grid"
			>
				<k-form-item label="数据日期">
					<k-field-date v-model="searchParam.dataDate" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" />
				</k-form-item>
			</k-form-search-customize>
		</div> -->
		<div class="py-page-container">
			<k-grid
				ref="appOverseasInvInfo2Grid"
				@data-row-select="selectRow"
				data-action="AppOverseasInvInfo2.findAppOverseasInvInfo2s"
				:dataOperateColumn="false"
				:dataAutoload="false"
			>
				<k-grid-column data-header="数据日期" data-name="reportDate"></k-grid-column>
				<k-grid-column data-header="公司名称" data-name="compNm"></k-grid-column>
				<k-grid-column data-header="产品类型" data-name="prodType"></k-grid-column>
				<k-grid-column data-header="投资资产余额（万元）" data-name="invAsstMkt"></k-grid-column>
				<k-grid-column data-header="加权累计净值增长率（%）" data-name="groRate"></k-grid-column>
			</k-grid>
		</div>

		<!--    添加境外投资情况明细表总体收益情况2弹出框   -->
		<k-popup ref="addAppOverseasInvInfo2Popup" data-title="添加">
			<k-form ref="addAppOverseasInvInfo2Form" :data-col="2">
				<k-form-item label="数据日期">
					<k-field-text v-model="formData.reportDate" />
				</k-form-item>
				<k-form-item label="公司名称">
					<k-field-text v-model="formData.compNm" />
				</k-form-item>
				<k-form-item label="产品类型">
					<k-field-text v-model="formData.prodType" />
				</k-form-item>
				<k-form-item label="投资资产余额">
					<k-field-text v-model="formData.invAsstMkt" />
				</k-form-item>
				<k-form-item label="加权累计净值增长率">
					<k-field-text v-model="formData.groRate" />
				</k-form-item>

				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="AppOverseasInvInfo2.addAppOverseasInvInfo2"
						data-from="addAppOverseasInvInfo2Form"
						:data-model="formData"
						data-target="appOverseasInvInfo2Grid"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<!--    修改境外投资情况明细表总体收益情况2弹出框   -->
		<k-popup ref="editAppOverseasInvInfo2Popup" data-title="编辑">
			<k-form ref="editAppOverseasInvInfo2Form" :data-col="2">
				<k-form-item label="数据日期">
					<k-field-text v-model="formData.reportDate" />
				</k-form-item>
				<k-form-item label="公司名称">
					<k-field-text v-model="formData.compNm" />
				</k-form-item>
				<k-form-item label="产品类型">
					<k-field-text v-model="formData.prodType" />
				</k-form-item>
				<k-form-item label="投资资产余额">
					<k-field-text v-model="formData.invAsstMkt" />
				</k-form-item>
				<k-form-item label="加权累计净值增长率">
					<k-field-text v-model="formData.groRate" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="AppOverseasInvInfo2.updateAppOverseasInvInfo2"
						data-from="editAppOverseasInvInfo2Form"
						:data-model="formData"
						data-target="appOverseasInvInfo2Grid"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
	</div>
</template>

<script>
export default {
	data() {
		return {
			formData: {},
			selectRowData: {},
			searchParam: {},
		};
	},
	methods: {
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},
	},
};
</script>
