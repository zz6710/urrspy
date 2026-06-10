<template>
	<div class="py-page">
		<div>
			<k-form-search-customize
				ref="searchRef"
				data-model-name="DwsCounterPartyInfo"
				v-model="searchParam"
				data-target="tableGrid"
				data-label-width="150px"
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
					<k-field-text v-model="searchParam.prodCd" @data-on-change="prodCdChange" />
				</k-form-item>
				<k-form-item label="内部产品代码">
					<k-field-text v-model="searchParam.prodIntrCd" @data-on-change="prodIntrCdChange" />
				</k-form-item>
				<k-form-item label="资产负债项目">
					<k-field-select
						v-model="searchParam.bredCd"
						data-dict="pbc_balance_proj_prt"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="资产代码">
					<k-field-text v-model="searchParam.assetCd" @data-on-change="assetCdChange" />
				</k-form-item>
				<k-form-item label="交易对手产品种类">
					<k-field-select v-model="searchParam.cntrProdType" data-dict="pbc_prd_typ" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="交易对手机构编码">
					<k-field-text v-model="searchParam.cntrOrgCd" @data-on-change="cntrOrgCdChange" />
				</k-form-item>
				<k-form-item label="交易对手机构名称">
					<k-field-text
						v-model="searchParam.cntrOrgNm"
						@data-on-change="changeParam(searchParam.cntrOrgNm, 'cntrOrgNm')"
					/>
				</k-form-item>
				<k-form-item label="交易对手产品代码">
					<k-field-text
						v-model="searchParam.cntrProdCd"
						@data-on-change="changeParam(searchParam.cntrProdCd, 'cntrProdCd')"
					/>
				</k-form-item>
				<k-form-item label="交易对手产品名称">
					<k-field-text
						v-model="searchParam.cntrProdNm"
						@data-on-change="changeParam(searchParam.cntrProdNm, 'cntrProdNm')"
					/>
				</k-form-item>
				<k-form-item label="校验状态">
					<k-field-select v-model="searchParam.status" data-dict="srb_report_task_status" />
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
						data-autoload="false"
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
						<md-icon>cloud_upload</md-icon>导入
					</k-btn>
					<k-btn
						slot="button"
						class="btn-custom-plain"
						data-functype="EXPORT"
						data-export-dict="true"
						data-target="tableGrid"
						data-export-name="月度交易对手中间表"
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
				data-action="DwsCounterPartyInfo.findDwsCounterPartyInfos"
				:dataAutoload="false"
				data-operate-width="120px"
				data-fixed="right"
			>
				<k-grid-column data-header="id" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
				<k-grid-column data-header="数据日期" data-name="actDt" data-export="false"></k-grid-column>
				<k-grid-column data-header="产品代码" data-name="prodCd"></k-grid-column>
				<k-grid-column data-header="内部产品代码" data-name="prodIntrCd"></k-grid-column>
				<k-grid-column
					data-header="资产负债项目"
					data-name="bredCd"
					data-dict="pbc_balance_proj_prt"
					data-dict-type="1"
				></k-grid-column>
				<k-grid-column data-header="资产代码" data-name="assetCd"></k-grid-column>
				<k-grid-column
					data-header="交易对手产品种类"
					data-name="cntrProdType"
					data-dict="pbc_prd_typ"
					data-dict-type="1"
				></k-grid-column>
				<k-grid-column data-header="交易对手机构编码" data-name="cntrOrgCd"></k-grid-column>
				<k-grid-column data-header="交易对手机构名称" data-name="cntrOrgNm"></k-grid-column>
				<k-grid-column data-header="交易对手产品代码" data-name="cntrProdCd"></k-grid-column>
				<k-grid-column data-header="交易对手产品名称" data-name="cntrProdNm"></k-grid-column>
				<k-grid-column data-header="币种" data-name="ccyCd" data-dict="pbc_cur" data-dict-type="1"></k-grid-column>
				<k-grid-column data-header="期末金额" data-name="amtBal"></k-grid-column>
				<k-grid-column data-header="期末金额折人民币" data-name="amtBalCny"></k-grid-column>
				<k-grid-column data-header="校验状态" data-name="status" data-dict="srb_report_task_status"></k-grid-column>
				<k-grid-column data-header="异常描述" data-name="exception"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-descript="修改月度交易对手中间表"
						data-functype="POPUP"
						data-size="mini"
						data-target="editPopup"
					>修改</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="DwsCounterPartyInfo.deleteDwsCounterPartyInfo"
						data-size="mini"
						data-type="danger"
						data-target="tableGrid"
						:data-confirm="true"
						data-descript="删除月度交易对手中间表"
					>删除</k-btn>
				</template>
			</k-grid>
		</div>

		<!--    添加月度交易对手中间表弹出框   -->
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
				<k-form-item label="内部产品代码">
					<k-field-text v-model="formData.prodIntrCd" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="资产负债项目">
					<k-field-select v-model="formData.bredCd" data-dict="pbc_balance_proj_prt" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="资产代码">
					<k-field-text v-model="formData.assetCd" />
				</k-form-item>
				<k-form-item label="交易对手产品种类">
					<k-field-select v-model="formData.cntrProdType" data-dict="pbc_prd_typ" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="交易对手机构编码">
					<k-field-text v-model="formData.cntrOrgCd" />
				</k-form-item>
				<k-form-item label="交易对手机构名称">
					<k-field-text v-model="formData.cntrOrgNm" />
				</k-form-item>
				<k-form-item label="交易对手产品代码">
					<k-field-text v-model="formData.cntrProdCd" />
				</k-form-item>
				<k-form-item label="交易对手产品名称">
					<k-field-text v-model="formData.cntrProdNm" />
				</k-form-item>
				<k-form-item label="币种">
					<k-field-select v-model="formData.ccyCd" data-dict="pbc_cur" />
				</k-form-item>
				<k-form-item label="期末金额">
					<k-field-text v-model="formData.amtBal" />
				</k-form-item>
				<k-form-item label="期末金额折人民币">
					<k-field-text v-model="formData.amtBalCny" />
				</k-form-item>

				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwsCounterPartyInfo.addDwsCounterPartyInfo"
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
						data-upload-url="upload/server/RptApp/uploadDwsCounterPartyInfo.json"
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

		<!--    修改月度交易对手中间表弹出框   -->
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
				<k-form-item label="资产负债项目" :class="[handleItemDiff('bredCd')]">
					<k-field-select v-model="formData.bredCd" data-dict="pbc_balance_proj_prt" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="资产代码" :class="[handleItemDiff('assetCd')]">
					<k-field-text v-model="formData.assetCd" />
				</k-form-item>
				<k-form-item label="交易对手产品种类" :class="[handleItemDiff('cntrProdType')]">
					<k-field-select v-model="formData.cntrProdType" data-dict="pbc_prd_typ" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="交易对手机构编码" :class="[handleItemDiff('cntrOrgCd')]">
					<k-field-text v-model="formData.cntrOrgCd" />
				</k-form-item>
				<k-form-item label="交易对手机构名称" :class="[handleItemDiff('cntrOrgNm')]">
					<k-field-text v-model="formData.cntrOrgNm" />
				</k-form-item>
				<k-form-item label="交易对手产品代码" :class="[handleItemDiff('cntrProdCd')]">
					<k-field-text v-model="formData.cntrProdCd" />
				</k-form-item>
				<k-form-item label="交易对手产品名称" :class="[handleItemDiff('cntrProdNm')]">
					<k-field-text v-model="formData.cntrProdNm" />
				</k-form-item>
				<k-form-item label="币种" :class="[handleItemDiff('ccyCd')]">
					<k-field-select v-model="formData.ccyCd" data-dict="pbc_cur" />
				</k-form-item>
				<k-form-item label="期末金额" :class="[handleItemDiff('amtBal')]">
					<k-field-text v-model="formData.amtBal" />
				</k-form-item>
				<k-form-item label="期末金额折人民币" :class="[handleItemDiff('amtBalCny')]">
					<k-field-text v-model="formData.amtBalCny" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwsCounterPartyInfo.updateDwsCounterPartyInfo"
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
		<!-- <GenerateFormAgainDialog ref="formAgainRef" paraid="90000052004" menuId="M061803" buttonName="重新生成报表" /> -->
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
import AssetMixin from "@/pages/pms/subject/mixins/assetMixin.js";
import GenerateFormAgainMixin from "@/pages/pms/subject/mixins/generateFormAgainMixin.js";
import moment from "moment";
import ReReport from "@/utils/ReReport.vue";
export default {
	name: "DwsCounterPartyInfo",
	mixins: [AssetMixin, GenerateFormAgainMixin],
	components: {
      ReReport
   	},
	data() {
		return {
			searchParam: {
				actDt: Tools.getPreviousMonth(),
                formData: { reportDate: "" },
		    },
			menuId: "M061803",
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
		// 				params: { menuId: "M061803", buttonName: "重新生成报表", reportDate: this.lastDay, paraid: "90000052004" },
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
		prodCdChange() {
			if (this.searchParam.prodCd) {
				this.searchParam.prodCd = this.searchParam.prodCd.trim();
			}
		},
		prodIntrCdChange() {
			if (this.searchParam.prodIntrCd) {
				this.searchParam.prodIntrCd = this.searchParam.prodIntrCd.trim();
			}
		},
		assetCdChange() {
			if (this.searchParam.assetCd) {
				this.searchParam.assetCd = this.searchParam.assetCd.trim();
			}
		},
		cntrOrgCdChange() {
			if (this.searchParam.cntrOrgCd) {
				this.searchParam.cntrOrgCd = this.searchParam.cntrOrgCd.trim();
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
