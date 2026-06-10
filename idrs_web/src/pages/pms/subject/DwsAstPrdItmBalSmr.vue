<template>
	<div class="py-page">
		<div>
			<k-form-search-customize
				ref="searchRef"
				data-model-name="DwsAstPrdItmBalSmr"
				v-model="searchParam"
				data-target="tableGrid"
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
				<k-form-item label="产品代码">
					<k-field-text
						v-model="searchParam.prodCd"
						@data-on-change="changeParam(searchParam.prodCd, 'prodCd')"
					/>
				</k-form-item>
				<k-form-item label="内部产品代码">
					<k-field-text
						v-model="searchParam.prodIntrCd"
						@data-on-change="changeParam(searchParam.prodIntrCd, 'prodIntrCd')"
					/>
				</k-form-item>
				<k-form-item label="币种">
					<k-field-text
						v-model="searchParam.ccyCd"
						@data-on-change="changeParam(searchParam.ccyCd, 'ccyCd')"
					/>
				</k-form-item>
				<k-form-item label="数据种类">
					<k-field-select v-model="searchParam.ctgCd" data-dict="ctg_cd" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn
						class="btn-custom-primary"
						data-functype="POPUP"
						:data-handler="() => (this.formData = {})"
						data-target="addPopup"
					>
						<md-icon md-src="/static/svg/add.svg" />新增
					</k-btn>
					<k-btn
						slot="button"
						ref="uploadBtnRef"
						data-functype="POPUP"
						class="btn-custom-plain"
						data-target="uploadPopup"
						:load-disabled="false"
					>
						<md-icon>cloud_upload</md-icon>覆盖导入
					</k-btn>
					<k-btn
						slot="button"
						ref="uploadBtnRef1"
						data-functype="POPUP"
						class="btn-custom-plain"
						data-target="uploadNewPopup"
						:load-disabled="false"
					>
						<md-icon>cloud_upload</md-icon>增量导入
					</k-btn>
					<k-btn
						slot="button"
						class="btn-custom-plain"
						data-functype="EXPORT"
						data-export-dict="false"
						data-target="tableGrid"
						data-export-name="月度资产负债信息中间表"
						data-export-form="searchRef"
					>
						<md-icon>cloud_download</md-icon>导出
					</k-btn>
					<!-- <k-btn slot="button" class="btn-custom-plain" @click="handleTaskApp">
						重新生成报表
					</k-btn>-->
					<k-btn
						slot="button"
						ref="reloadBtnRef"
						class="btn-custom-plain"
						data-functype="POPUP"
						data-target="handleTaskPopup"
						data-action="DwsProdTTRDBef.updateTaskAppQuery"
						loading-tip="正在重新生成报表，请稍后重试！"
					>
						<md-icon>cloud_download</md-icon>重新生成报表
					</k-btn>
				</div>
				<ReReport ref="reReportRef" :formData="formData" :menuId="menuId" :buttonName="buttonName" />
			</div>
			<k-grid
				ref="tableGrid"
				@data-row-select="selectRow"
				data-action="DwsAstPrdItmBalSmr.findDwsAstPrdItmBalSmrs"
				data-autoload="false"
				data-operate-width="120px"
				data-fixed="right"
			>
				<k-grid-column data-header="数据日期" data-name="actDt" data-export="false"></k-grid-column>
				<k-grid-column data-header="id" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
				<k-grid-column data-header="产品代码" data-name="prodCd"></k-grid-column>
				<k-grid-column data-header="内部产品代码" data-name="prodIntrCd"></k-grid-column>
				<k-grid-column data-header="币种" data-name="ccyCd"></k-grid-column>
				<k-grid-column data-header="数据种类" data-name="ctgCd" data-dict="ctg_cd"></k-grid-column>
				<k-grid-column data-header="金额" data-name="amtBal"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-functype="POPUP"
						data-size="mini"
						data-target="editPopup"
					>编辑</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="DwsAstPrdItmBalSmr.deleteDwsAstPrdItmBalSmr"
						data-size="mini"
						data-descript="删除"
						data-type="danger"
						data-target="tableGrid"
						:data-confirm="true"
					>删除</k-btn>
				</template>
			</k-grid>
		</div>

		<!--    添加月度资产负债信息中间表弹出框   -->
		<k-popup ref="addPopup" data-title="新增">
			<k-form ref="addForm" :data-col="2">
				<k-form-item label="id" v-show="false">
					<k-field-text v-model="formData.id" />
				</k-form-item>
				<k-form-item label="数据日期">
					<k-field-date
						v-model="formData.actDt"
						data-type="month"
						data-date-format="yyyy-MM"
						data-value-format="yyyy-MM"
						:data-allowblank="false"
					/>
				</k-form-item>
				<!--			<k-form-item label="产品代码">-->
				<!--	        	<k-field-text v-model="formData.prodCd" :data-allowblank="false"/>-->
				<!--	     	</k-form-item>-->
				<k-form-item label="内部产品代码">
					<k-field-text v-model="formData.prodIntrCd" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="币种">
					<k-field-text v-model="formData.ccyCd" />
				</k-form-item>
				<k-form-item label="数据种类">
					<k-field-select v-model="formData.ctgCd" data-dict="ctg_cd" />
				</k-form-item>
				<k-form-item label="金额">
					<k-field-text v-model="formData.amtBal" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwsAstPrdItmBalSmr.addDwsAstPrdItmBalSmr"
						data-from="addForm"
						:data-model="formDataTransfer"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
					</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<k-popup ref="uploadPopup" data-title="覆盖导入">
			<k-form ref="addForm" data-ui="element">
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
						ref="uploadRef"
						:data-multiple="false"
						:data-limit="1"
						data-accept=".xlsx,.xls"
						:data-error="onSubmitError"
						:data-success="onSubmitSuccess"
						:data-auto-upload="false"
						data-upload-url="upload/server/RptApp/uploadDwsAstPrdItmBalSmr.json"
						data-tip-succ="覆盖导入成功"
					></k-field-excel-upload>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						ref="submitBtn"
						:data-auto-upload="false"
						data-from="addForm"
						:data-handler="submitUploadParam"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
					</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<k-popup ref="uploadNewPopup" data-title="增量导入">
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
						data-upload-url="upload/server/RptApp/uploadDwsAstPrdItmBalSmrNew.json"
						data-tip-succ="增量导入成功"
					></k-field-excel-upload>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-target="tableGrid"
						ref="submitNewBtn"
						:data-auto-upload="false"
						data-from="addNewForm"
						:data-handler="submitUploadNewParam"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
					</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<!--    修改月度资产负债信息中间表弹出框   -->
		<k-popup ref="editPopup" data-title="修改">
			<k-form ref="editForm" :data-col="2">
				<k-form-item label="id" v-show="false">
					<k-field-text v-model="formData.id" />
				</k-form-item>
				<k-form-item label="数据日期">
					<k-field-date v-model="formData.actDt" :data-allowblank="false" data-disabled="true" />
				</k-form-item>
				<k-form-item label="产品代码">
					<k-field-text v-model="formData.prodCd" :data-allowblank="false" data-disabled="true" />
				</k-form-item>
				<k-form-item label="内部产品代码">
					<k-field-text v-model="formData.prodIntrCd" :data-allowblank="false" data-disabled="true" />
				</k-form-item>
				<k-form-item label="币种" :class="[handleItemDiff('ccyCd')]">
					<k-field-text v-model="formData.ccyCd" />
				</k-form-item>
				<k-form-item label="数据种类" :class="[handleItemDiff('ctgCd')]">
					<k-field-select v-model="formData.ctgCd" data-dict="ctg_cd" />
				</k-form-item>
				<k-form-item label="金额" :class="[handleItemDiff('amtBal')]">
					<k-field-text v-model="formData.amtBal" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwsAstPrdItmBalSmr.updateDwsAstPrdItmBalSmr"
						data-from="editForm"
						:data-model="formData"
						data-target="tableGrid"
						:handle-before="handleBefore"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
					</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
		<!-- <GenerateFormAgainDialog ref="formAgainRef" paraid="90000052003" menuId="M061801" buttonName="重新生成报表" /> -->
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
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
					</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
	</div>
</template>

<script>
import Tools from "@/utils/tools";
import AssetMixin from "@/pages/pms/subject/mixins/assetMixin.js";
import GenerateFormAgainMixin from "@/pages/pms/subject/mixins/generateFormAgainMixin.js";
import moment from "moment";
import ReReport from "@/utils/ReReport.vue";
export default {
	name: "DwsAstPrdItmBalSmr",
	mixins: [AssetMixin, GenerateFormAgainMixin],
	components: {
      ReReport
   	},
	data() {
		return {
			formData: { reportDate: "" },
			menuId: "M061801",
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
		// 				params: { menuId: "M061801", buttonName: "重新生成报表", reportDate: this.lastDay, paraid: "90000052003" },
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
		handleBefore() {
			if (this.formNoChangeCb()) {
				this.$refs.editPopup.close();
				return false;
			}
			return true;
		},
		onSubmitNewSuccess() {
			if (this.searchParam.actDt) {
				this.$refs.tableGrid.load(this.searchParam);
			}
			this.$refs.uploadBtnRef1.setIconStyle(1);
		},
		onSubmitNewError() {
			this.$refs.uploadBtnRef1.setIconStyle(1);
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
					this.$refs.uploadBtnRef1.setIconStyle(0);
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
		changeParam(paramValue, paramKey) {
			if (paramValue) {
				this.searchParam[paramKey] = paramValue.trim();
			}
		},
	},
};
</script>
