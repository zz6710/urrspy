<template>
	<div class="py-page">
		<div>
			<k-form-search-customize
				data-model-name="DwsMonthInvRaise"
				v-model="searchParam"
				data-target="tableGrid"
				data-label-width="140px"
			>
				<k-form-item label="数据日期">
					<k-field-date
						v-model="searchParam.dealDate"
						data-type="month"
						data-date-format="yyyy-MM"
						data-value-format="yyyyMM"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-form-item label="产品代码">
					<k-field-text v-model="searchParam.prdcCdPbc" />
				</k-form-item>
				<k-form-item label="内部产品代码">
					<k-field-text v-model="searchParam.prdcCd" />
				</k-form-item>
				<k-form-item label="所属地区">
					<k-field-select v-model="searchParam.zonCd" data-dict="pbc_province_area" />
				</k-form-item>
				<k-form-item label="客户类型">
					<k-field-select v-model="searchParam.invTyp" data-dict="pbc_org_typ" />
				</k-form-item>
				<k-form-item label="客户类型原始码值">
					<k-field-text v-model="searchParam.orgnInvType" data-dict="orgnInvType" />
				</k-form-item>
				<k-form-item label="业务种类">
					<k-field-select v-model="searchParam.busiType" data-dict="pbc_busi_type" />
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
						v-if="false"
						slot="button"
						ref="uploadBtnRef"
						data-functype="POPUP"
						class="btn-custom-plain"
						data-target="uploadPopup"
						:load-disabled="false"
					>
						<md-icon>cloud_upload</md-icon>导入
					</k-btn>
					<k-btn
						slot="button"
						class="btn-custom-plain"
						data-functype="EXPORT"
						data-export-dict="true"
						data-target="tableGrid"
						data-export-name="月度募集信息表"
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
				data-action="DwsMonthInvRaise.findDwsMonthInvRaises"
				data-autoload="false"
			>
				<k-grid-column data-header="ID" data-name="id" :data-hidden="true" data-export="false"></k-grid-column>
				<k-grid-column data-header="数据日期" data-name="dealDate" data-export="false"></k-grid-column>
				<k-grid-column data-header="产品代码" data-name="prdcCdPbc"></k-grid-column>
				<k-grid-column data-header="内部产品代码" data-name="prdcCd"></k-grid-column>
				<k-grid-column
					data-header="所属地区"
					data-name="zonCd"
					data-dict="pbc_province_area"
					data-dict-type="1"
				></k-grid-column>
				<k-grid-column data-header="客户类型" data-name="invTyp" data-dict="pbc_org_typ" data-dict-type="1"></k-grid-column>
				<k-grid-column
					data-header="客户类型原始码值"
					data-name="orgnInvType"
					data-dict="orgnInvType"
					data-dict-type="1"
				></k-grid-column>
				<k-grid-column
					data-header="业务种类"
					data-name="busiType"
					data-dict="pbc_busi_type"
					data-dict-type="1"
				></k-grid-column>
				<k-grid-column data-header="金额" data-name="holdAmt" data-type="money"></k-grid-column>
				<k-grid-column data-header="份额" data-name="holdVol" data-type="money"></k-grid-column>
				<!--		<k-grid-column data-header="创建日期" data-name="crtDt" data-export="false"></k-grid-column>-->
				<!--		<k-grid-column data-header="创建时间" data-name="crtTm" data-export="false"></k-grid-column>-->
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-descript="修改月度募集信息"
						data-functype="POPUP"
						data-size="mini"
						data-target="editPopup"
					>修改</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="DwsMonthInvRaise.deleteDwsMonthInvRaise"
						data-size="mini"
						data-type="danger"
						data-target="tableGrid"
						:data-confirm="true"
						data-descript="删除月度募集信息"
					>删除</k-btn>
				</template>
			</k-grid>
		</div>

		<!--    添加月度募集信息中间表弹出框   -->
		<k-popup ref="addPopup" data-title="新增">
			<k-form ref="addForm" :data-col="2">
				<k-form-item label="ID" v-show="false">
					<k-field-text v-model="formData.id" />
				</k-form-item>
				<k-form-item label="数据日期">
					<k-field-date
						v-model="formData.dealDate"
						:data-allowblank="false"
						data-type="month"
						data-date-format="yyyy-MM"
						data-value-format="yyyy-MM"
					/>
				</k-form-item>
				<!--			<k-form-item label="产品代码">-->
				<!--	        	<k-field-text v-model="formData.prdcCdPbc" :data-allowblank="false"/>-->
				<!--	     	</k-form-item>-->
				<k-form-item label="内部产品代码">
					<k-field-text v-model="formData.prdcCd" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="所属地区">
					<k-field-select v-model="formData.zonCd" data-dict="pbc_province_area" />
				</k-form-item>
				<k-form-item label="客户类型">
					<k-field-select v-model="formData.invTyp" data-dict="pbc_org_typ" />
				</k-form-item>
				<k-form-item label="客户类型原始码值">
					<k-field-select v-model="formData.orgnInvType" data-dict="orgnInvType" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="业务种类">
					<k-field-select v-model="formData.busiType" data-dict="pbc_busi_type" />
				</k-form-item>
				<k-form-item label="金额">
					<k-field-text v-model="formData.holdAmt" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="份额">
					<k-field-text v-model="formData.holdVol" data-validate-type="money" />
				</k-form-item>

				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwsMonthInvRaise.addDwsMonthInvRaise"
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

		<k-popup ref="uploadPopup" data-title="导入">
			<k-form ref="addForm" data-ui="element">
				<k-form-item label="数据日期" data-ui="element" data-input-width="500px">
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
						data-upload-url="upload/server/RptApp/uploadDwsMonthInvRaise.json"
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

		<!--    修改月度募集信息中间表弹出框   -->
		<k-popup ref="editPopup" data-title="修改">
			<k-form ref="editForm" :data-col="2">
				<k-form-item label="ID" v-show="false">
					<k-field-text v-model="formData.id" />
				</k-form-item>
				<k-form-item label="数据日期" :class="[handleItemDiff('dealDate')]">
					<k-field-date v-model="formData.dealDate" :data-allowblank="false" data-disabled="true" />
				</k-form-item>
				<k-form-item label="产品代码" :class="[handleItemDiff('prdcCdPbc')]">
					<k-field-text v-model="formData.prdcCdPbc" :data-allowblank="false" data-disabled="true" />
				</k-form-item>
				<k-form-item label="内部产品代码" :class="[handleItemDiff('prdcCd')]">
					<k-field-text v-model="formData.prdcCd" :data-allowblank="false" data-disabled="true" />
				</k-form-item>
				<k-form-item label="所属地区" :class="[handleItemDiff('zonCd')]">
					<k-field-select v-model="formData.zonCd" data-dict="pbc_province_area" />
				</k-form-item>
				<k-form-item label="客户类型" :class="[handleItemDiff('invTyp')]">
					<k-field-select v-model="formData.invTyp" data-dict="pbc_org_typ" />
				</k-form-item>
				<k-form-item label="客户类型原始码值" :class="[handleItemDiff('orgnInvType')]">
					<k-field-select v-model="formData.orgnInvType" data-dict="orgnInvType" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="业务种类" :class="[handleItemDiff('busiType')]">
					<k-field-select v-model="formData.busiType" data-dict="pbc_busi_type" />
				</k-form-item>
				<k-form-item label="金额" :class="[handleItemDiff('holdAmt')]">
					<k-field-text v-model="formData.holdAmt" data-validate-type="money" />
				</k-form-item>
				<k-form-item label="份额" :class="[handleItemDiff('holdVol')]">
					<k-field-text v-model="formData.holdVol" data-validate-type="money" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwsMonthInvRaise.updateDwsMonthInvRaise"
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
		<!-- <GenerateFormAgainDialog ref="formAgainRef" paraid="90000052001" menuId="M061705" buttonName="重新生成报表" /> -->
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
import Tools from "@/utils/tools.js";
import ProdMixin from "@/pages/pms/subject/mixins/prodMixin.js";
import GenerateFormAgainMixin from "@/pages/pms/subject/mixins/generateFormAgainMixin.js";
import moment from "moment";
import ReReport from "@/utils/ReReport.vue";
export default {
	name: "DwsMonthInvRaise",
	mixins: [ProdMixin, GenerateFormAgainMixin],
	components: {
      ReReport
   	},
	data() {
		return {
			searchParam: {
				dealDate: Tools.getPreviousMonth(),
			},
			formData: { reportDate: "" },
			menuId: "M061705",
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
		// 				params: { menuId: "M061705", buttonName: "重新生成报表", reportDate: this.lastDay, paraid: "90000052001" },
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
	},
};
</script>
