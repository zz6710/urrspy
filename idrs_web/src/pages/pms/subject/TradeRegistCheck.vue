<template>
	<div class="py-page">
		<div>
			<k-form-search-customize ref="searchRef" data-model-name="TradeRegistCheck" data-label-width="80px" v-model="searchParam" data-target="tradeRegistCheckGrid">
				<k-form-item label="数据日期">
					<k-field-date
						v-model="searchParam.reportDate"
						data-type="date"
						data-date-format="yyyyMMdd"
						data-value-format="yyyyMMdd"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-form-item label="产品代码">
					<k-field-text v-model="searchParam.prodCode" />
				</k-form-item>
				<k-form-item label="底层资产/负债编码" data-label-width="140px">
					<k-field-text v-model="searchParam.bottomCode" />
				</k-form-item>
				<k-form-item label="偏离度(%)">
                    <k-field-select v-model="searchParam.plRate" data-dict="pl_rate"/>
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
					data-target="tradeRegistCheckGrid"
					data-export-name="交易登记校验表"
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
				ref="tradeRegistCheckGrid"
				@data-row-select="selectRow"
				data-action="TradeRegistCheck.findTradeRegistChecks"
				:data-operate-column="false"
				:dataAutoload="false"
				data-dict-type="1"
			>
				<k-grid-column data-header="数据日期" data-name="reportDate"></k-grid-column>
				<k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
				<k-grid-column data-header="产品登记编码" data-name="prodRegEnc"></k-grid-column>
				<k-grid-column data-header="直接或间接投资" data-name="investType" data-dict="invest_type_tradecheck"></k-grid-column>
				<k-grid-column data-header="底层资产/负债编码" data-name="bottomCode"></k-grid-column>
				<k-grid-column data-header="交易登记计算数量" data-name="tradeInvamount"></k-grid-column>
				<k-grid-column data-header="估值系统持仓" data-name="faAmount"></k-grid-column>
				<k-grid-column data-header="偏离数量" data-name="plAmount"></k-grid-column>
				<k-grid-column data-header="偏离度（%）" data-name="plRate"></k-grid-column>
			</k-grid>
		</div>

		<k-popup ref="handleTaskPopup" data-title="重新生成校验表">
			<k-form ref="handleTaskAppForm" data-ui="element">
				<k-form-item label="数据日期" data-ui="element" data-input-width="500px">
					<k-field-date
						v-model="formData.reportDate"
						data-type="date"
						data-date-format="yyyy-MM-dd"
						data-value-format="yyyyMMdd"
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
import ReReport from "@/utils/ReReport.vue";
export default {
	name: "TradeRegistCheck",
	components: {
      ReReport
   	},
	data() {
		return {
			formData: {},
			selectRowData: {},
			searchParam: {},
			menuId: "M042002",
            buttonName: "重新生成校验表",
		};
	},
	methods: {
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},

		handleTaskApp() {
          this.$refs.reReportRef.handleReports(this.formData.reportDate);
        },
	},
};
</script>
