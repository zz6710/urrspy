<template>
	<div class="py-page">
		<k-form-search-customize v-model="searchParam" data-target="accountMergeInfoGrid" data-label-width="130px">
			<k-form-item label="客户识别标识(从)">
				<k-field-text v-model="searchParam.cstmAccF"></k-field-text>
			</k-form-item>
			<k-form-item label="客户识别标识(到)">
				<k-field-text v-model="searchParam.cstmAccT"></k-field-text>
			</k-form-item>
		</k-form-search-customize>

		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn
						slot="button"
						data-functype="POPUP"
						class="btn-custom-primary"
						data-target="addMergeOrderPopup"
						:data-handler="() => (this.formData = {})"
					>
						<md-icon md-src="/static/svg/add.svg" />
						新增
					</k-btn>
				</div>
			</div>

			<k-grid ref="accountMergeInfoGrid" data-action="CustomerDataMergeModel.findCustomerAccountMergeOrderInfo" @data-row-select="selectRow" :dataAutoload="false" data-operate-width="350px">
				<k-grid-column data-header="ID" data-name="id" data-hidden="true"/>
				<k-grid-column data-header="数据日期(起始)" data-name="cstmDtF" />
				<k-grid-column data-header="数据日期(结束)" data-name="cstmDtE" />
				<k-grid-column data-header="客户识别标识(从)" data-name="cstmAccF" />
				<k-grid-column data-header="客户识别标识(到)" data-name="cstmAccT" />
				<k-grid-column data-header="操作日期" data-name="optDt" />
				<k-grid-column data-header="操作时间" data-name="optTm" />
				<k-grid-column data-header="操作员编号" data-name="optUserId" />
				<k-grid-column data-header="操作员名称" data-name="optUserNm" />
				<k-grid-column data-header="合并状态" data-name="mrgSts" data-dict="merge_status"/>
				<template slot="operate" slot-scope="{row}">
					<k-btn
						data-functype="SUBMIT"
						data-action="CustomerDataMergeModel.doAccountMergeOperation"
						data-target="accountMergeInfoGrid"
						class="btn-custom-text"
						:data-confirm="true"
						data-descript="合并"
						:data-disabled="isMerge(row.row)"
					>合并</k-btn>

					<k-btn class="btn-custom-text" data-functype="SUBMIT"
					  data-action="CustomerDataMergeModel.removeMergeOrderInfo"
            :data-confirm="true" data-type="danger"
            data-target="accountMergeInfoGrid"
            data-descript="删除"
            :data-disabled="isMerge(row.row)"
          >删除</k-btn>

          <k-btn data-functype="SUBMIT"
            data-target="accountMergeInfoGrid" class="btn-custom-text" data-action="CustomerDataMergeModel.customerHisDataDownload" :data-params="{'mrg_typ':'0'}"
            :data-confirm="true" data-descript="下载合并前数据" :data-disabled="isComplete(row.row)" :data-export-name="'投资者明细信息登记历史数据管理'">
          下载合并前</k-btn>

          <k-btn data-functype="SUBMIT"
            data-target="accountMergeInfoGrid" class="btn-custom-text" data-action="CustomerDataMergeModel.customerHisDataDownload" :data-params="{'mrg_typ':'1'}"
            :data-confirm="true" data-descript="下载合并后数据" :data-disabled="isComplete(row.row)" >
          下载合并后</k-btn>
				</template>
			</k-grid>
		</div>

		<k-popup ref="addMergeOrderPopup" data-title="新增">
			<k-form ref="addMergeOrderForm" :data-col="1">
				<k-form-item label="数据日期(起始)">
          <k-field-date v-model="formData.cstmDtF" :data-allowblank="false"
                        data-date-format="yyyyMMdd"
                        data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="数据日期(结束)">
          <k-field-date v-model="formData.cstmDtE" :data-allowblank="false"
                        data-date-format="yyyyMMdd"
                        data-value-format="yyyyMMdd"/>
        </k-form-item>
				<k-form-item label="客户识别标识(从)">
          <k-field-text v-model="formData.cstmAccF" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="客户识别标识(到)">
          <k-field-text v-model="formData.cstmAccT" :data-allowblank="false" />
        </k-form-item>

				<k-form-footer data-align="center">
          <k-btn
            class="btn-custom-primary"
            data-functype="SUBMIT"
            data-action="CustomerDataMergeModel.putCustomerAccountMergeOrderInfo"
            data-from="addReportConditionForm"
            :data-model="formData"
            data-target="accountMergeInfoGrid"
          >
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
					</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
	</div>
</template>

<script>
import { assign } from "lodash";
export default {
	name: "AccountMerge",
	data() {
		return {
			selectRowData: {},
			formData: {},
			RegisterDate:[],
			cstmDtFRange:[],
			cstmDtERange:[],
			searchParam: {},
		};
	},
	methods: {
		selectRow(row, column, event) {
			this.formData = Object.assign({}, row);
			searchParam: {}
		},
		isMerge(row) {
			return row.mrgSts === '02' || row.mrgSts === '03';
		},
		isComplete(row) {
            return row.mrgSts != '03';
        }
	},
	watch: {

  }
};
</script>

<style scoped>

</style>
