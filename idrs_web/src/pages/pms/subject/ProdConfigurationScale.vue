<template>
	<div class="py-page">
		<div>
			<k-form-search-customize
				data-model-name="ProdConfigurationScale"
				data-label-width="80px"
				v-model="searchParam"
				data-target="prodConfigurationScaleGrid"
				ref="searchRef"
			>
				<k-form-item label="数据日期">
					<k-field-date
						v-model="searchParam.actDt"
						data-type="month"
						data-date-format="yyyy-MM"
						data-value-format="yyyyMM"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-form-item label="资产代码">
					<k-field-text v-model="searchParam.icode" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<k-btn
					slot="button"
					class="btn-custom-plain"
					data-functype="EXPORT"
					data-target="prodConfigurationScaleGrid"
					data-export-name="理财产品专户配置比例表"
					data-export-dict="true"
					data-export-form="searchRef"
				>
					<md-icon>cloud_download</md-icon>导出
				</k-btn>
			</div>
			<k-grid
				ref="prodConfigurationScaleGrid"
				@data-row-select="selectRow"
				data-action="ProdConfigurationScale.findProdConfigurationScales"
				:data-operate-column="false"
				:dataAutoload="false"
				:custom-row-class="handleRowClass"
			>
				<k-grid-column data-header="数据日期" data-name="actDt"></k-grid-column>
				<k-grid-column data-header="资产代码" data-name="icode"></k-grid-column>
				<k-grid-column data-header="母行划转产品投资市值" data-name="investmonamount"></k-grid-column>
				<k-grid-column data-header="自主发行产品投资市值" data-name="investownamount"></k-grid-column>
				<k-grid-column data-header="产品投资市值合计" data-name="investcountamount"></k-grid-column>
				<k-grid-column data-header="母行划转产品投资占比" data-name="investmonrate"></k-grid-column>
				<k-grid-column data-header="自主发行产品投资占比" data-name="investownrate"></k-grid-column>
				<k-grid-column data-header="产品投资占比合计" data-name="investcountrate"></k-grid-column>
			</k-grid>
		</div>
	</div>
</template>

<script>
export default {
	name: "ProdConfigurationScale",
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
		handleRowClass(row) {
			if (row.investcountrate != 1) {
				return `row-less1`
			}
			return ''
		}
	},
};
</script>
<style lang="scss" scoped>
/deep/ .row-less1 {
	color: #d5bb06;
}
</style>
