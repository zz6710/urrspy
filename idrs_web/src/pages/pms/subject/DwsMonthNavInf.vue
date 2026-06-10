<template>
	<div class="py-page">
		<div>
			<k-form-search-customize
				data-model-name="DwsMonthNavInf"
				v-model="searchParam"
				data-target="tableGrid"
				:handleConfirm="handleConfirm"
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
					<k-field-text v-model="searchParam.prdcCdPbc" @data-on-change="pdbChange" />
				</k-form-item>
				<k-form-item label="内部产品代码">
					<k-field-text v-model="searchParam.prdcCd" @data-on-change="prdcCdChange" />
				</k-form-item>
				<k-form-item label="剩余期限">
					<k-field-select
						v-model="searchParam.remainingTerm"
						data-dict="t8_prod_remaining_trm"
						data-dict-type="1"
					></k-field-select>
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
						data-export-name="月度净值信息中间表"
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
				data-action="DwsMonthNavInf.findDwsMonthNavInfs"
				data-autoload="false"
				data-dict-type="1"
			>
				<k-grid-column data-header="ID" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
				<k-grid-column data-header="数据日期" data-name="dealDate" data-export="false"></k-grid-column>
				<k-grid-column data-header="产品代码" data-name="prdcCdPbc"></k-grid-column>
				<k-grid-column data-header="内部产品代码" data-name="prdcCd"></k-grid-column>
				<k-grid-column data-header="期末净值" data-name="untNav" data-digits="8"></k-grid-column>
				<k-grid-column data-header="期末累计净值" data-name="acmNav"></k-grid-column>
				<k-grid-column data-header="当月年化收益率(%)" data-name="rct1mGrwRat"></k-grid-column>
				<k-grid-column data-header="剩余天数" data-name="remainingDays"></k-grid-column>
				<k-grid-column data-header="剩余期限" data-name="remainingTerm" data-dict="t8_prod_remaining_trm"></k-grid-column>
				<k-grid-column data-header="创建日期" data-name="crtDt" data-hidden="true" data-export="false"></k-grid-column>
				<k-grid-column data-header="创建时间" data-name="crtTm" data-hidden="true" data-export="false"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-descript="修改月度净值信息中间表"
						data-functype="POPUP"
						data-size="mini"
						data-target="editPopup"
					>修改</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="DwsMonthNavInf.deleteDwsMonthNavInf"
						data-size="mini"
						data-type="danger"
						data-target="tableGrid"
						:data-confirm="true"
						data-descript="删除月度净值信息中间表"
					>删除</k-btn>
				</template>
			</k-grid>
		</div>

		<!--    添加月度净值信息中间表弹出框   -->
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
				<k-form-item label="期末净值">
					<k-field-text
						v-model="formData.untNav"
						data-validate-type="money"
						data-max-value="9999.99999999"
					/>
				</k-form-item>
				<k-form-item label="期末累计净值">
					<k-field-text
						v-model="formData.acmNav"
						data-validate-type="money"
						data-max-value="9999.99999999"
					/>
				</k-form-item>
				<k-form-item label="当月年化收益率(%)">
					<k-field-text
						v-model="formData.rct1mGrwRat"
						data-validate-type="money"
						data-max-value="99999.99999"
					/>
				</k-form-item>
				<k-form-item label="剩余天数">
					<k-field-text
						v-model="formData.remainingDays"
						data-validate-type="int"
						data-type="int"
						:data-max-lenght="5"
					/>
				</k-form-item>
				<k-form-item label="剩余期限">
					<k-field-select
						v-model="formData.remainingTerm"
						data-dict="t8_prod_remaining_trm"
						data-dict-type="1"
					/>
				</k-form-item>

				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwsMonthNavInf.addDwsMonthNavInf"
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
						data-upload-url="upload/server/RptApp/uploadDwsMonthNavInf.json"
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

		<!--    修改月度净值信息中间表弹出框   -->
		<k-popup ref="editPopup" data-title="修改">
			<k-form ref="editForm" :data-col="2">
				<k-form-item label="ID" v-show="false">
					<k-field-text v-model="formData.id" />
				</k-form-item>
				<k-form-item label="数据日期">
					<k-field-date v-model="formData.dealDate" :data-allowblank="false" data-disabled="true" />
				</k-form-item>
				<k-form-item label="产品代码">
					<k-field-text v-model="formData.prdcCdPbc" :data-allowblank="false" data-disabled="true" />
				</k-form-item>
				<k-form-item label="内部产品代码">
					<k-field-text v-model="formData.prdcCd" :data-allowblank="false" data-disabled="true" />
				</k-form-item>
				<k-form-item label="期末净值" :class="[handleItemDiff('untNav')]">
					<k-field-text
						v-model="formData.untNav"
						data-validate-type="money"
						data-max-value="9999.99999999"
					/>
				</k-form-item>
				<k-form-item label="期末累计净值" :class="[handleItemDiff('acmNav')]">
					<k-field-text
						v-model="formData.acmNav"
						data-validate-type="money"
						data-max-value="9999.99999999"
					/>
				</k-form-item>
				<k-form-item label="当月年化收益率(%)" :class="[handleItemDiff('rct1mGrwRat')]">
					<k-field-text
						v-model="formData.rct1mGrwRat"
						data-validate-type="money"
						data-max-value="99999.99999"
					/>
				</k-form-item>
				<k-form-item label="剩余天数" :class="[handleItemDiff('remainingDays')]">
					<k-field-text
						v-model="formData.remainingDays"
						data-validate-type="int"
						data-type="int"
						:data-max-lenght="5"
					/>
				</k-form-item>
				<k-form-item label="剩余期限" :class="[handleItemDiff('remainingTerm')]">
					<k-field-select
						v-model="formData.remainingTerm"
						data-dict="t8_prod_remaining_trm"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwsMonthNavInf.updateDwsMonthNavInf"
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
		<!-- <GenerateFormAgainDialog ref="formAgainRef" paraid="90000052002" menuId="M061701" buttonName="重新生成报表" /> -->
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
	name: "DwsMonthNavInf",
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
			menuId: "M061701",
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
		// 				params: { menuId: "M061701", buttonName: "重新生成报表", reportDate: this.lastDay, paraid: "90000052002" },
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
		handleConfirm() {
			console.log("handle");
			if (this.searchParam.prdcCdPbc) {
				this.searchParam.prdcCdPbc = this.searchParam.prdcCdPbc.trim();
			}
			if (this.searchParam.prdcCd) {
				this.searchParam.prdcCd = this.searchParam.prdcCd.trim();
			}
			return true;
		},
		pdbChange() {
			if (this.searchParam.prdcCdPbc) {
				this.searchParam.prdcCdPbc = this.searchParam.prdcCdPbc.trim();
			}
		},
		prdcCdChange() {
			if (this.searchParam.prdcCd) {
				this.searchParam.prdcCd = this.searchParam.prdcCd.trim();
			}
		},
	},
};
</script>
