<template>
	<div class="py-page">
		<div>
			<k-form-search-customize
				ref="searchFormRef"
				data-model-name="AppSonShareDelReg"
				v-model="queryParam"
				data-target="appSonShareDelRegGrid"
				data-label-width="130px"
			>
				<k-form-item label="报送日期">
					<k-field-date v-model="queryParamDateRange" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" />
				</k-form-item>
				<k-form-item label="产品登记编码">
					<k-field-text v-model="searchParam.prodRegEnc" />
				</k-form-item>
				<k-form-item label="产品子份额代码">
					<k-field-text v-model="searchParam.sonShareCode" />
				</k-form-item>
				<k-form-item label="报送状态">
					<k-field-select v-model="searchParam.registerStatus" data-dict="subm_report_status" :dataDictExcludeFilter="['9']" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn
						slot="button"
						ref="exportRef"
						class="btn-custom-plain"
						data-functype="EXPORT"
						data-target="appSonShareDelRegGrid"
						data-export-dict="true"
						data-export-form="searchFormRef"
						:data-export-name="'子份额信息登记删除'"
						@downSuccess="downSuccess"
						:data-handler="handleExport"
					>
						<md-icon>cloud_download</md-icon>
						导出
					</k-btn>
					<k-btn slot="button" class="btn-custom-plain" :handleBefore="handleBefore" :data-handler="handleConfirmExport">
						<md-icon>cloud_download</md-icon>
						确认并导出
					</k-btn>
				</div>
			</div>
			<k-grid ref="appSonShareDelRegGrid" @data-row-select="selectRow" data-action="AppSonShareDelReg.findAppSonShareDelRegs" :data-autoload="false">
				<k-grid-column data-header="报送日期" data-name="reportDate" data-export="false"></k-grid-column>
				<k-grid-column data-header="*发行机构代码" data-name="bankCode"></k-grid-column>
				<k-grid-column data-header="*产品登记编码" data-name="prodRegEnc"></k-grid-column>
				<k-grid-column data-header="*产品子份额代码" data-name="sonShareCode"></k-grid-column>
				<k-grid-column data-header="*子份额登记模式" data-name="sonShareTaskType" data-dict="sonShareTaskType" data-dict-type="1"></k-grid-column>
				<k-grid-column data-header="*业务日期" data-name="taskDate"></k-grid-column>
				<k-grid-column data-header="*删除原因" data-name="details"></k-grid-column>
				<k-grid-column
					data-header="报送状态"
					data-name="registerStatus"
					data-dict="subm_report_status"
					data-export="false"
					data-width="100"
				></k-grid-column>
				<k-grid-column data-header="登记日期" data-name="registerDate" data-export="false"></k-grid-column>
				<k-grid-column data-header="登记流水号" data-name="registerSerno" data-export="false"></k-grid-column>
				<k-grid-column data-header="新增日期" data-name="createDate" data-export="false"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-descript="修改子份额登记删除"
						data-functype="POPUP"
						data-size="mini"
						data-target="editAppSonShareDelRegPopup"
					>
						修改
					</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="AppSonShareDelReg.deleteAppSonShareDelReg"
						data-size="mini"
						data-type="danger"
						data-target="appSonShareDelRegGrid"
						:data-confirm="true"
						data-descript="删除子份额登记删除"
					>
						删除
					</k-btn>
				</template>
			</k-grid>
		</div>

		<!--    修改子份额登记删除弹出框   -->
		<k-popup ref="editAppSonShareDelRegPopup" data-title="编辑">
			<k-form ref="editAppSonShareDelRegForm" :data-col="2" data-label-width="190px">
				<k-form-item label="*发行机构代码">
					<k-field-text v-model="formData.bankCode" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="*产品登记编码">
					<k-field-text v-model="formData.prodRegEnc" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="*产品子份额代码">
					<k-field-text v-model="formData.sonShareCode" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="*子份额登记模式">
					<k-field-select v-model="formData.sonShareTaskType" data-dict="sonShareTaskType" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="*业务日期">
					<k-field-text v-model="formData.taskDate" />
				</k-form-item>
				<k-form-item label="*删除原因">
					<k-field-text v-model="formData.details" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="AppSonShareDelReg.updateAppSonShareDelReg"
						data-from="editAppSonShareDelRegForm"
						:data-model="formData"
						data-target="appSonShareDelRegGrid"
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
import ProdMixin from "@/pages/zz/manage/mixins/prodMixin.js";
export default {
	name: "AppSonShareDelReg",
	mixins: [ProdMixin],
	data() {
		return {
			formData: {},
			selectRowData: {},
			searchParam: {},
			abnormalAction: "AppSonShareDelReg.getAbnormalData",
			updateStatusAction: "AppSonShareDelReg.updateAppSonShareDelRegStatus",
			queryParamDateRange: [],
		};
	},
	computed: {
		queryParam() {
			return {
				reportBeginDate: this.queryParamDateRange ? this.queryParamDateRange[0] : null,
				reportEndDate: this.queryParamDateRange ? this.queryParamDateRange[1] : null,
				prodRegEnc: this.searchParam.prodRegEnc,
				registerStatus: this.searchParam.registerStatus,
				sonShareCode: this.searchParam.sonShareCode,
			};
		},
	},
	methods: {
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},
		setConfirmExportParam() {
			this.comfirmExportParam = this.queryParam;
		},
	},
	created() {
    let now = new Date();
    let year = now.getFullYear(); //获取年
    let month = now.getMonth(); //获取月
    let date = now.getDate(); //得到日期
    month = month + 1;
    month = month.toString().padStart(2, "0");
    date = date.toString().padStart(2, "0");
    let  defaultDate = `${year}${month}${date}`;
    this.queryParamDateRange[0] = defaultDate;
    this.queryParamDateRange[1] = defaultDate;
    this.$set(this.queryParam, "queryParamDateRange", defaultDate);
  },
};
</script>
