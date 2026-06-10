<template>
	<div class="py-page">
		<div>
			<k-form-search-customize ref="searchRef" data-model-name="AssetHoldRegistCheck" v-model="searchParam" data-target="assetHoldRegistCheckGrid">
				<k-form-item label="数据日期">
					<k-field-date
						v-model="searchParam.reportDate"
						data-type="month"
						data-date-format="yyyy-MM"
						data-value-format="yyyyMM"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-form-item label="产品代码">
					<k-field-text v-model="searchParam.prodCode" />
				</k-form-item>
				<k-form-item label="产品登记编码">
					<k-field-text v-model="searchParam.prodRegEnc" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn
					slot="button"
					class="btn-custom-plain"
					data-functype="EXPORT"
					data-target="assetHoldRegistCheckGrid"
					data-export-name="资产持仓登记校验表"
					data-export-dict="true"
					data-export-form="searchRef"
				>
					<md-icon>cloud_download</md-icon>导出
				</k-btn>
				    <k-btn slot="button" ref="reloadBtnRef" class="btn-custom-plain" data-functype="POPUP" data-target="handleTaskPopup" loading-tip="正在重新生成校验表，请稍后重试！">
                      <md-icon>cloud_download</md-icon>重新生成校验表
				    </k-btn>
				</div>
				<ReReport ref="reReportRef" :formData="formData" :menuId="menuId" :buttonName="buttonName" />
			</div>
			<k-grid
				ref="assetHoldRegistCheckGrid"
				@data-row-select="selectRow"
				data-action="AssetHoldRegistCheck.findAssetHoldRegistChecks"
				:data-operate-column="false"
				:dataAutoload="false"
			>
				<k-grid-column data-header="数据日期" data-name="reportDate"></k-grid-column>
				<k-grid-column data-header="产品代码" data-name="prodCode" data-width="140"></k-grid-column>
				<k-grid-column data-header="产品登记编码" data-name="prodRegEnc" data-width="140"></k-grid-column>
				<k-grid-column data-header="理财产品总资产金额(元)" data-name="totAssets" data-width="100"></k-grid-column>
				<k-grid-column data-header="净资产" data-name="assetsJn" data-width="100"></k-grid-column>
				<k-grid-column data-header="穿透前资产" data-name="orgAssets" data-width="100"></k-grid-column>
				<k-grid-column data-header="穿透前负债" data-name="orgDebt" data-width="100"></k-grid-column>
				<k-grid-column data-header="穿透前资产净值" data-name="orgAssetsJn" data-width="120"></k-grid-column>
				<k-grid-column data-header="穿透前资产差额" data-name="orgAssetsCe" data-width="120"></k-grid-column>
				<k-grid-column data-header="穿透前资产差异比例（%）" data-name="orgAssetsCerate" data-width="100"></k-grid-column>
				<k-grid-column data-header="穿透前净资产差额" data-name="orgAssetsJnce" data-width="120"></k-grid-column>
				<k-grid-column data-header="穿透前净资产差异比例（%）" data-name="orgAssetsJncerate" data-width="120"></k-grid-column>
				<k-grid-column data-header="穿透后总资产" data-name="otcAllAssets" data-width="120"></k-grid-column>
				<k-grid-column data-header="底层负债" data-name="buttomDebt" data-width="100"></k-grid-column>
				<k-grid-column data-header="穿透后总负债" data-name="otcDebt" data-width="100"></k-grid-column>
				<k-grid-column data-header="穿透后资产" data-name="otcAssets" data-width="100"></k-grid-column>
				<k-grid-column data-header="穿透后资产净值" data-name="otcAssetsJn" data-width="120"></k-grid-column>
				<k-grid-column data-header="穿透后资产差额" data-name="otcAssetsCe" data-width="120"></k-grid-column>
				<k-grid-column data-header="穿透后资产差额比例（%）" data-name="otcAssetsCerate" data-width="100"></k-grid-column>
				<k-grid-column data-header="穿透后资产净值差额" data-name="otcAssetsJnce" data-width="100"></k-grid-column>
				<k-grid-column data-header="穿透后资产净值差额比例（%）" data-name="otcAssetsJncerate" data-width="120"></k-grid-column>
			</k-grid>
		</div>

		<k-popup ref="handleTaskPopup" data-title="重新生成校验表">
			<k-form ref="handleTaskAppForm" data-ui="element">
				<k-form-item label="数据日期" data-ui="element" data-input-width="500px">
					<k-field-date
						v-model="formData.reportDate"
						data-type="month"
						data-date-format="yyyy-MM"
						data-value-format="yyyy-MM"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn class="btn-custom-primary" data-from="editForm" :data-handler="handleTaskApp">
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
	    </k-popup>
	</div>
</template>

<script>
import moment from "moment";
import ReReport from "@/utils/ReReport.vue";
export default {
	name: "AssetHoldRegistCheck",
	components: {
      ReReport
   	},
	data() {
		return {
			formData: {},
			selectRowData: {},
			searchParam: {},
			menuId: "M042001",
            buttonName: "重新生成校验表",
		};
	},
	computed: {
		lastDay() {
			if (this.formData.reportDate) {
				return moment([this.formData.reportDate.split("-")[0], this.formData.reportDate.split("-")[1] - 1])
					.endOf("month")
					.format("YYYYMMDD");
			}
			return "";
		},
	},
	methods: {
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},

		handleTaskApp() {
          this.$refs.reReportRef.handleReports(this.lastDay);
        },
	},
};
</script>
