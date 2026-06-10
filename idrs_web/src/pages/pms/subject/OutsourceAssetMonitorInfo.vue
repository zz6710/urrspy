<template>
	<div class="py-page">
		<div>
			<k-form-search-customize
				data-model-name="OutsourceAssetMonitorInfo"
				data-label-width="120px"
				v-model="searchParam"
				data-target="outsourceAssetMonitorInfoGrid"
			>
				<k-form-item label="资产代码">
					<k-field-text v-model="searchParam.assetCode" />
				</k-form-item>
				<k-form-item label="是否接入数据">
					<k-field-select v-model="searchParam.isJoinData" data-dict="yesnoText"></k-field-select>
				</k-form-item>
				<k-form-item label="专户类型">
					<k-field-select v-model="searchParam.atype" data-dict="zhuanhuType"></k-field-select>
				</k-form-item>
				<k-form-item label="持仓日期">
					<k-field-date v-model="searchParam.inputDate" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" />
				</k-form-item>
				<k-form-item label="计算日期">
					<k-field-date v-model="searchParam.calcDate" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" />
				</k-form-item>
				<k-form-item label="导入日期">
					<k-field-date v-model="searchParam.dealDate" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<k-btn
					slot="button"
					class="btn-custom-plain"
					data-functype="EXPORT"
					data-export-dict="true"
					data-target="outsourceAssetMonitorInfoGrid"
					data-export-name="委外资产监测表"
				>
					<md-icon>cloud_download</md-icon>
					导出
				</k-btn>
			</div>
			<k-grid ref="outsourceAssetMonitorInfoGrid" data-action="OutsourceAssetMonitorInfo.findOutsourceAssetMonitorInfos" :data-operate-column="false" :dataAutoload="false">
				<k-grid-column data-header="资产代码" data-name="assetCode"></k-grid-column>
				<k-grid-column data-header="资产名称" data-name="assetName"></k-grid-column>
				<k-grid-column data-header="组合代码/非我行托管组合代码" data-name="combinedCode"></k-grid-column>
				<k-grid-column data-header="专户类型" data-name="atype" data-dict="zhuanhuType"></k-grid-column>
				<k-grid-column data-header="资产联系人" data-name="sponsorLinkman"></k-grid-column>
				<k-grid-column data-header="是否接入数据" data-name="isJoinData"></k-grid-column>
				<k-grid-column data-header="未接入数据原因" data-name="noJoinReason"></k-grid-column>
				<k-grid-column data-header="数据初步校验(%)" data-name="moniResult"></k-grid-column>
				<k-grid-column data-header="偏离金额(元)" data-name="deviateAmt"></k-grid-column>
				<k-grid-column data-header="持仓日期" data-name="inputDate"></k-grid-column>
				<k-grid-column data-header="计算日期" data-name="calcDate"></k-grid-column>
				<k-grid-column data-header="导入日期" data-name="dealDate"></k-grid-column>
			</k-grid>
		</div>
	</div>
</template>

<script>
export default {
	name: "OutsourceAssetMonitorInfo",
	data() {
		return {
			searchParam: {},
		};
	},
	methods: {},
};
</script>
