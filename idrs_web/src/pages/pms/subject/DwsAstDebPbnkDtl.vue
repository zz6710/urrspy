<template>
	<div class="py-page">
		<div>
			<k-form-search-customize
				ref="searchRef"
				data-model-name="DwsAstDebPbnkDtl"
				data-label-width="80px"
				v-model="searchParam"
				data-target="dwsAstDebPbnkDtlGrid"
			>
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
					<k-field-text v-model="searchParam.prodCd" />
				</k-form-item>
				<k-form-item label="资产代码">
					<k-field-text v-model="searchParam.scrCd" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn slot="button" ref="uploadBtnRef" data-functype="POPUP" class="btn-custom-plain" data-target="uploadNewPopup" :load-disabled="false">
						<md-icon>cloud_upload</md-icon>导入
					</k-btn>
					<k-btn
						slot="button"
						class="btn-custom-plain"
						data-functype="EXPORT"
						data-export-dict="true"
						data-target="dwsAstDebPbnkDtlGrid"
						data-export-name="资产负债剩余期限明细表"
						data-export-form="searchRef"
					>
						<md-icon>cloud_download</md-icon>导出
					</k-btn>
					<!-- <k-btn slot="button" class="btn-custom-plain" @click="handleTaskApp">
						重新生成报表
					</k-btn> -->
					<k-btn
						slot="button"
						ref="reloadBtnRef"
						class="btn-custom-plain"
						data-functype="POPUP"
						data-target="handleTaskPopup"
						data-action="DwsProdTTRDBef.updateTaskAppQuery"
						loading-tip="正在重新生成报表，请稍后重试！"
					>
						<md-icon>cloud_download</md-icon>
						重新生成报表
					</k-btn>
				</div>
				<ReReport ref="reReportRef" :formData="formData" :menuId="menuId" :buttonName="buttonName" />
			</div>
			<k-grid
				ref="dwsAstDebPbnkDtlGrid"
				@data-row-select="selectRow"
				data-action="DwsAstDebPbnkDtl.findDwsAstDebPbnkDtls"
				:data-autoload="false"
				data-dict-type="1"
			>
				<k-grid-column data-header="数据日期" data-name="reportDate" data-export="false"></k-grid-column>
				<k-grid-column data-header="产品代码" data-name="prodCd"></k-grid-column>
				<k-grid-column data-header="产品名称" data-name="prodNm"></k-grid-column>
				<k-grid-column data-header="资产代码" data-name="scrCd"></k-grid-column>
				<k-grid-column data-header="资产名称" data-name="scrNm"></k-grid-column>
				<k-grid-column data-header="资产三类" data-name="asstThrKnd"></k-grid-column>
				<k-grid-column data-header="资产方/负债方" data-name="asstType" data-dict="zcffzf"></k-grid-column>
				<k-grid-column data-header="资产类型" data-name="asstClss" data-dict="zclx"></k-grid-column>
				<k-grid-column data-header="到期日" data-name="mtuDt"></k-grid-column>
				<k-grid-column data-header="剩余期限" data-name="prodTrmPbnk" data-dict="t8_prod_remaining_trm"></k-grid-column>
				<k-grid-column data-header="市值" data-name="mktVol"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-descript="修改资产负债剩余期限明细表"
						data-functype="POPUP"
						data-size="mini"
						data-target="editDwsAstDebPbnkDtlPopup"
					>
						修改
					</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="DwsAstDebPbnkDtl.deleteDwsAstDebPbnkDtl"
						data-size="mini"
						data-type="danger"
						data-target="dwsAstDebPbnkDtlGrid"
						:data-confirm="true"
						data-descript="删除资产负债剩余期限明细表"
					>
						删除
					</k-btn>
				</template>
			</k-grid>
		</div>

		<!--    修改资产负债剩余期限明细表弹出框   -->
		<k-popup ref="editDwsAstDebPbnkDtlPopup" data-title="修改">
			<k-form ref="editDwsAstDebPbnkDtlForm" :data-col="2">
				<k-form-item label="数据日期">
					<k-field-text v-model="formData.reportDate" :data-allowblank="false" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="产品代码" :class="[handleItemDiff('prodCd')]">
					<k-field-text v-model="formData.prodCd" />
				</k-form-item>
				<k-form-item label="产品名称" :class="[handleItemDiff('prodNm')]">
					<k-field-text v-model="formData.prodNm" />
				</k-form-item>
				<k-form-item label="资产代码" :class="[handleItemDiff('scrCd')]">
					<k-field-text v-model="formData.scrCd" />
				</k-form-item>
				<k-form-item label="资产名称" :class="[handleItemDiff('scrNm')]">
					<k-field-text v-model="formData.scrNm" />
				</k-form-item>
				<k-form-item label="资产三类" :class="[handleItemDiff('asstThrKnd')]">
					<k-field-text v-model="formData.asstThrKnd" />
				</k-form-item>
				<k-form-item label="资产方/负债方" :class="[handleItemDiff('asstType')]">
					<k-field-select v-model="formData.asstType" data-dict="zcffzf" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="资产类型" :class="[handleItemDiff('asstClss')]">
					<k-field-select v-model="formData.asstClss" data-dict="zclx" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="到期日" :class="[handleItemDiff('mtuDt')]">
					<k-field-date v-model="formData.mtuDt" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" />
				</k-form-item>
				<k-form-item label="剩余期限" :class="[handleItemDiff('prodTrmPbnk')]">
					<k-field-select v-model="formData.prodTrmPbnk" data-dict="t8_prod_remaining_trm" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="市值" :class="[handleItemDiff('mktVol')]">
					<k-field-text v-model="formData.mktVol" data-validate-type="number" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwsAstDebPbnkDtl.updateDwsAstDebPbnkDtl"
						data-from="editDwsAstDebPbnkDtlForm"
						:data-model="formData"
						data-target="dwsAstDebPbnkDtlGrid"
						:handle-before="handleBefore"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
		<k-popup ref="uploadNewPopup" data-title="导入">
			<k-form ref="addNewForm" data-ui="element">
				<k-form-item label="数据日期" data-ui="element">
					<k-field-date
						v-model="formData.dealDate"
						data-type="month"
						data-date-format="yyyy-MM"
						data-value-format="yyyy-MM"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-form-item label="附件" data-ui="element" data-input-width="500px">
					<k-field-excel-upload
						data-type="file"
						ref="uploadNewRef"
						:data-multiple="false"
						:data-limit="1"
						data-accept=".xlsx,.xls"
						:data-error="onSubmitNewError"
						:data-success="onSubmitNewSuccess"
						:data-auto-upload="false"
						data-upload-url="upload/server/RptApp/uploadDwsAstDebPbnkDtl.json"
						data-tip-succ="导入成功"
					>
					</k-field-excel-upload>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-target="dwsAstDebPbnkDtlGrid"
						ref="submitNewBtn"
						:data-auto-upload="false"
						data-from="addNewForm"
						:data-handler="submitUploadNewParam"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
		<!-- <GenerateFormAgainDialog ref="formAgainRef" paraid="90000052007" menuId="M061817" buttonName="重新生成报表" /> -->
		<k-popup ref="handleTaskPopup" data-title="重新生成报表">
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
import Tools from "@/utils/tools.js";
import AssetMixin from "@/pages/pms/subject/mixins/assetMixin.js";
import GenerateFormAgainMixin from "@/pages/pms/subject/mixins/generateFormAgainMixin.js";
import moment from "moment";
import ReReport from "@/utils/ReReport.vue";
export default {
	mixins: [AssetMixin, GenerateFormAgainMixin],
	components: {
      ReReport
   	},
	data() {
		return {
			formData: {},
			formDataCopy: {},
			selectRowData: {},
			searchParam: {
				reportDate: Tools.getPreviousMonth(),
			},
			formData: { reportDate: "" },
			menuId: "M061817",
            buttonName: "重新生成报表",
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
		handleTaskApp() {
			this.$refs.reReportRef.handleReports(this.lastDay);
		},
		// handleTaskApp() {
		// 	if (this.$refs.handleTaskAppForm.validate()) {
		// 		this.$refs.reloadBtnRef.setIconStyle(0);
		// 		this.httpUtil
		// 			.comnUpdate({
		// 				action: "DwsProdTTRDBef.updateTaskAppQuery",
		// 				async: true,
		// 				params: { menuId: "M061817", buttonName: "重新生成报表", reportDate: this.lastDay, paraid: "90000052007" },
		// 				successAlert: false,
		// 				dataAfterSuccess: (reData) => {
		// 					Tools.alertTime(reData.returnmsg, "success", 0);
		// 				},
		// 			})
		// 			.then((data) => {
		// 				this.$refs.reloadBtnRef.setIconStyle(1);
		// 			})
		// 			.catch((err) => {
		// 				console.log(err, "err");
		// 				this.$refs.reloadBtnRef.setIconStyle(1);
		// 			});
		// 		setTimeout(() => {
		// 			this.$refs.handleTaskPopup.close();
		// 		}, 300);
		// 	}
		// },
		onSubmitNewSuccess() {
			if (this.searchParam.reportDate) {
				this.$refs.dwsAstDebPbnkDtlGrid.load(this.searchParam);
			}
			this.$refs.uploadBtnRef.setIconStyle(1);
		},
		onSubmitNewError() {
			this.$refs.uploadBtnRef.setIconStyle(1);
		},
		submitUploadNewParam() {
			//文件上传校验
			let validate = this.$refs.addNewForm.validate();
			if (validate) {
				let temp = document.getElementsByClassName("upload-demo");
				let lis = temp[0].childNodes[1].childNodes.length;
				if (lis > 0) {
					let formData = { dealDate: this.lastDayDeal };
					this.$refs.uploadNewRef.upload(formData);
					this.$refs.uploadBtnRef.setIconStyle(0);
					setTimeout(() => {
						this.$refs.uploadNewPopup.close();
					}, 300);
				} else {
					this.$message.error("上传文件不能为空!");
					return false;
				}
				return false;
			}
		},
		handleBefore() {
			if (this.formNoChangeCb()) {
				this.$refs.editDwsAstDebPbnkDtlPopup.close();
				return false;
			}
			return true;
		},
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
			this.formDataCopy = Object.assign({}, row);
		},
	},
};
</script>
