<template>
	<div class="py-page">
		<div>
			<k-form-search-customize
				ref="searchFormRef"
				data-model-name="AppSonShareInfoReg"
				v-model="queryParam"
				data-target="appSonShareInfoRegGrid"
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
				<k-form-item label="子份额业务类型">
					<k-field-select v-model="searchParam.sonShareTaskType" data-dict="sonShareTaskType" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="业务日期">
					<k-field-date v-model="searchParam.taskDate" data-type="date" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" />
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
						data-target="appSonShareInfoRegGrid"
						data-export-dict="true"
						data-export-form="searchFormRef"
						:data-export-name="'子份额信息登记'"
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
					<k-btn
						slot="button"
						ref="uploadBtnRef"
						:load-disabled="false"
						data-functype="POPUP"
						class="btn-custom-plain"
						data-target="uploadProdRegistFilingInfoPopup"
					>
						<md-icon>cloud_upload</md-icon>
						子份额登记编码导入
					</k-btn>
				</div>
			</div>
			<k-grid
				ref="appSonShareInfoRegGrid"
				@data-row-select="selectRow"
				data-action="AppSonShareInfoReg.findAppSonShareInfoRegs"
				data-fixed="right"
				data-operate-width="120px"
				:data-autoload="false"
			>
				<k-grid-column data-header="报送日期" data-name="reportDate" data-export="false"></k-grid-column>
				<k-grid-column data-header="*发行机构代码" data-name="bankCode" data-width="120"></k-grid-column>
				<k-grid-column data-header="*产品登记编码" data-name="prodRegEnc" data-width="130"></k-grid-column>
				<k-grid-column data-header="*产品子份额代码" data-name="sonShareCode" data-width="160"></k-grid-column>
				<k-grid-column data-header="*产品子份额名称" data-name="sonShareName" data-width="200"></k-grid-column>
				<k-grid-column
					data-header="*子份额业务类型"
					data-name="sonShareTaskType"
					data-width="120"
					data-dict="sonShareTaskType"
					data-dict-type="1"
				></k-grid-column>
				<k-grid-column data-header="*子份额销售对象" data-name="sonShareSaleObj" data-width="160"></k-grid-column>
				<k-grid-column data-header="*业务日期" data-name="taskDate"></k-grid-column>
				<k-grid-column data-header="备注" data-name="details" data-width="160"></k-grid-column>
				<k-grid-column data-header="子份额登记编码" data-name="sonShareRegEnc" data-width="160" data-export="false"></k-grid-column>
				<k-grid-column
					data-header="报送状态"
					data-name="registerStatus"
					data-dict="subm_report_status"
					data-export="false"
					data-width="100"
				></k-grid-column>
				<k-grid-column data-header="登记日期" data-name="registerDate" data-export="false"></k-grid-column>
				<k-grid-column data-header="登记流水号" data-name="registerSerno" data-width="250" data-export="false"></k-grid-column>
				<k-grid-column data-align="left" data-header="新增日期" data-name="createDate" data-export="false" data-width="80"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-descript="修改子份额信息登记"
						data-functype="POPUP"
						data-size="mini"
						data-target="editAppSonShareInfoRegPopup"
					>
						修改
					</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="AppSonShareInfoReg.deleteAppSonShareInfoReg"
						data-size="mini"
						data-type="danger"
						data-target="appSonShareInfoRegGrid"
						:data-confirm="true"
						data-descript="删除子份额信息登记"
					>
						删除
					</k-btn>
				</template>
			</k-grid>
		</div>

		<!--    修改子份额信息登记弹出框   -->
		<k-popup ref="editAppSonShareInfoRegPopup" data-title="编辑">
			<k-form ref="editAppSonShareInfoRegForm" :data-col="2" data-label-width="130px">
				<k-form-item label="*发行机构代码">
					<k-field-text v-model="formData.bankCode" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="*产品登记编码">
					<k-field-text v-model="formData.prodRegEnc" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="*产品子份额代码">
					<k-field-text v-model="formData.sonShareCode" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="*产品子份额名称">
					<k-field-text v-model="formData.sonShareName" />
				</k-form-item>
				<k-form-item label="*子份额业务类型">
					<k-field-select v-model="formData.sonShareTaskType" data-dict="sonShareTaskType" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="子份额登记编码">
					<k-field-text v-model="formData.sonShareRegEnc" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="*子份额销售对象">
					<k-field-text v-model="formData.sonShareSaleObj" />
				</k-form-item>
				<k-form-item label="*业务日期">
					<k-field-date v-model="formData.taskDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" />
				</k-form-item>
				<k-form-item label="备注">
					<k-field-text v-model="formData.details" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="AppSonShareInfoReg.updateAppSonShareInfoReg"
						data-from="editAppSonShareInfoRegForm"
						:data-model="formData"
						data-target="appSonShareInfoRegGrid"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
		<k-popup ref="uploadProdRegistFilingInfoPopup" title="报送数据导入" @data-opened="uploadOpened()">
			<k-form ref="addForm" data-ui="element">
				<k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
					<k-field-excel-upload
						data-type="file"
						ref="uploadRef"
						:data-multiple="false"
						:data-limit="1"
						data-accept=".xlsx,.xls"
						:data-error="onSubmitError"
						:data-success="onSubmitSuccess"
						:data-auto-upload="false"
						data-upload-url="upload/server/RptApp/reportManage/sonShareInfoImport.json"
					>
					</k-field-excel-upload>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-target="tableGrid"
						ref="submitBtn"
						:data-auto-upload="false"
						data-from="addForm"
						:data-handler="submitUploadParam"
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
	name: "AppSonShareInfoReg",
	mixins: [ProdMixin],
	data() {
		return {
			formData: {},
			selectRowData: {},
			searchParam: {},
			queryParamDateRange: [],
			abnormalAction: "AppSonShareInfoReg.getAbnormalData",
			updateStatusAction: "AppSonShareInfoReg.updateAppSonShareInfoRegStatus",
		};
	},
	computed: {
		queryParam() {
			return {
				reportBeginDate: this.queryParamDateRange ? this.queryParamDateRange[0] : null,
				reportEndDate: this.queryParamDateRange ? this.queryParamDateRange[1] : null,
				taskDate: this.searchParam.taskDate,
				prodRegEnc: this.searchParam.prodRegEnc,
				registerStatus: this.searchParam.registerStatus,
				sonShareCode: this.searchParam.sonShareCode,
				sonShareTaskType: this.searchParam.sonShareTaskType,
			};
		},
	},
	methods: {
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},
		uploadOpened() {
			this.formData.reportDate = "";
		},
		submitUploadParam() {
			//文件上传校验
			let validate = this.$refs.addForm.validate();
			if (validate) {
				let formData = { reportDate: this.formData.reportDate };
				let temp = document.getElementsByClassName("upload-demo");
				let lis = temp[0].childNodes[1].childNodes.length;
				if (lis > 0) {
					this.$refs.uploadBtnRef.setIconStyle(0);
					this.$refs.uploadRef.upload(formData);
					setTimeout(() => {
						this.$refs.uploadProdRegistFilingInfoPopup.close();
					}, 300);
				} else {
					this.$message.error("上传文件不能为空!");
					return false;
				}
			}
			return false;
		},
		onSubmitSuccess() {
			this.$refs.appSonShareInfoRegGrid.load(this.queryParam);
			this.$refs.uploadBtnRef.setIconStyle(1);
		},
		onSubmitError() {
			this.$refs.uploadBtnRef.setIconStyle(1);
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
