<template>
	<div class="py-page">
		<div>
			<k-form-search-customize ref="searchRef" data-model-name="AssetEndDate" data-label-width="80px" v-model="searchParam" data-target="assetEndDateGrid">
				<k-form-item label="统计日期">
					<k-field-date
						v-model="searchParam.statisticDate"
						data-type="date"
						data-date-format="yyyy-MM-dd"
						data-value-format="yyyyMMdd"
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
					data-target="assetEndDateGrid"
					data-export-name="资产到期日期清单"
					data-export-dict="true"
					data-export-form="searchRef"
				>
					<md-icon>cloud_download</md-icon>导出
				</k-btn>
			</div>
			<k-grid
				ref="assetEndDateGrid"
				@data-row-select="selectRow"
				data-action="AssetEndDate.findAssetEndDates"
				data-operate-width="100"
				:dataAutoload="false"
			>
				<k-grid-column data-header="资产代码" data-name="icode"></k-grid-column>
				<k-grid-column data-header="资产名称" data-name="iname"></k-grid-column>
				<k-grid-column data-header="资产三类" data-name="assetThirdType"></k-grid-column>
				<k-grid-column data-header="资产到期日" data-name="assetEndDate"></k-grid-column>
				<!-- <k-grid-column data-header="资产期限特殊说明" data-name="assetTermPj"></k-grid-column> -->
				<k-grid-column data-header="统计日期" data-name="statisticDate"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn class="btn-custom-text" data-functype="POPUP" data-size="mini" data-target="editAssetEndDatePopup"> 修改 </k-btn>
				</template>
			</k-grid>
		</div>
		<k-popup ref="editAssetEndDatePopup" data-title="修改">
			<k-form ref="editAssetEndDateForm" :data-col="2">
				<k-form-item label="资产代码">
					<k-field-text v-model="formData.icode" />
				</k-form-item>
				<k-form-item label="资产名称">
					<k-field-text v-model="formData.iname" />
				</k-form-item>
				<k-form-item label="资产三类">
					<k-field-text v-model="formData.assetThirdType" />
				</k-form-item>
				<k-form-item label="资产到期日">
					<k-field-date v-model="formData.assetEndDate" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" />
				</k-form-item>
				<!-- <k-form-item label="资产期限特殊说明">
					<k-field-text v-model="formData.assetTermPj" />
				</k-form-item> -->
				<k-form-item label="统计日期">
					<k-field-date v-model="formData.statisticDate" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="AssetEndDate.updateAssetEndDate"
						data-from="editAssetEndDateForm"
						:data-model="formData"
						data-target="assetEndDateGrid"
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
	name: "AssetEndDate",
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
