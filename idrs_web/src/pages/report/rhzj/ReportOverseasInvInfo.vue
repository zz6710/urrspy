<template>
	<div class="py-page">
		<div>
			<k-form-search-customize
				data-model-name="ReportOverseasInvInfo"
				data-label-width="80px"
				v-model="searchParam"
				data-target="reportOverseasInvInfoGrid"
			>
				<k-form-item label="数据日期">
					<k-field-date
						v-model="searchParam.reportDate"
						data-type="date"
						data-date-format="yyyy-MM-dd"
						data-value-format="yyyyMMdd"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-form-item label="产品代码">
					<k-field-text v-model="searchParam.prodCd" />
				</k-form-item>
				<k-form-item label="产品登记编码">
					<k-field-text v-model="searchParam.prodRegEnc" />
				</k-form-item>
				<k-form-item label="首层资产代码">
					<k-field-text v-model="searchParam.fasstCd" />
				</k-form-item>
				<k-form-item label="底层科目代码">
					<k-field-text v-model="searchParam.itmCd" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<!-- <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addReportOverseasInvInfoPopup">
					<md-icon md-src="/static/svg/add.svg" />新增</k-btn>-->
					<k-btn
						slot="button"
						class="btn-custom-plain"
						data-functype="EXPORT"
						data-export-dict="true"
						data-target="reportOverseasInvInfoGrid"
						data-export-name="境外投资情况明细表"
					>
						<md-icon>cloud_download</md-icon>导出
					</k-btn>

					<k-btn
						slot="button"
						ref="reloadBtnRef"
						class="btn-custom-plain"
						data-functype="POPUP"
						data-target="handleTaskPopup"
						data-action="DwsProdTTRDBef.updateTaskApp"
						loading-tip="正在重新生成报表，请稍后重试！"
					>
						<md-icon>cloud_download</md-icon>重新生成报表
					</k-btn>
				</div>
				<ReReport ref="reReportRef" :formData="formData" :menuId="menuId" :buttonName="buttonName" />
			</div>
			<k-grid
				ref="reportOverseasInvInfoGrid"
				@data-row-select="selectRow"
				data-action="ReportOverseasInvInfo.findReportOverseasInvInfos"
				:dataAutoload="false"
			>
				<k-grid-column data-header data-name="id" :data-hidden="true"></k-grid-column>
				<k-grid-column data-header="数据日期" data-name="reportDate"></k-grid-column>
				<k-grid-column data-header="产品代码" data-name="prodCd"></k-grid-column>
				<k-grid-column data-header="产品登记编码" data-name="prodRegEnc"></k-grid-column>
				<k-grid-column data-header="持仓类型" data-name="holdType" data-dict="position_type"></k-grid-column>
				<k-grid-column data-header="首层资产代码" data-name="fasstCd"></k-grid-column>
				<k-grid-column data-header="资产三类" data-name="assetThirdType"></k-grid-column>
				<k-grid-column data-header="首层资产名称" data-name="fasstNm"></k-grid-column>
				<k-grid-column data-header="首层折算人民币金额（万元）" data-name="famount"></k-grid-column>
				<k-grid-column data-header="穿透前分类" data-name="orgClassific" data-dict="g06_type"></k-grid-column>
				<k-grid-column data-header="底层科目代码" data-name="itmCd"></k-grid-column>
				<k-grid-column data-header="底层科目名称" data-name="itmNm"></k-grid-column>
				<k-grid-column data-header="底层折算人民币金额（万元）" data-name="damount"></k-grid-column>
				<k-grid-column data-header="穿透后分类" data-name="newClassific" data-dict="g06_type"></k-grid-column>
				<k-grid-column data-header="初始投资时间" data-name="finvTm"></k-grid-column>
				<k-grid-column
					data-header="投资资产分类"
					data-name="asstType"
					data-dict="invs_asst_type"
					data-dict-type="1"
				></k-grid-column>
				<k-grid-column data-header="投资资产本金（万元）" data-name="asstAmount"></k-grid-column>
				<k-grid-column data-header="投资地区" data-name="asstZon"></k-grid-column>
				<k-grid-column data-header="管理人注册地" data-name="mangZon"></k-grid-column>
				<k-grid-column data-header="计提减值准备金额" data-name="deprRdyAmt"></k-grid-column>
				<k-grid-column data-header="已计提减值" data-name="deprAmt"></k-grid-column>
				<k-grid-column
					data-header="外汇额度类型"
					data-name="fxType"
					data-dict="forex_exe_type"
					data-dict-type="1"
				></k-grid-column>
				<k-grid-column
					data-header="债务期限结构"
					data-name="bdRmaiType"
					data-dict="debt_term_type"
					data-dict-type="1"
				></k-grid-column>
				<k-grid-column data-header="剩余天数" data-name="rmaiDay"></k-grid-column>
				<k-grid-column data-header="风险事件" data-name="riskEnvn"></k-grid-column>
				<k-grid-column data-header="风险项目余额" data-name="riskPjAmt"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-descript="修改境外投资情况明细表"
						data-functype="POPUP"
						data-size="mini"
						data-target="editReportOverseasInvInfoPopup"
					>修改</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="ReportOverseasInvInfo.deleteReportOverseasInvInfo"
						data-size="mini"
						data-type="danger"
						data-target="reportOverseasInvInfoGrid"
						:data-confirm="true"
						data-descript="删除境外投资情况明细表"
					>删除</k-btn>
				</template>
			</k-grid>
		</div>

		<!--    添加境外投资情况明细表弹出框   -->
		<k-popup ref="addReportOverseasInvInfoPopup" data-title="添加">
			<k-form ref="addReportOverseasInvInfoForm" :data-col="2">
				<k-form-item label="id" v-show="false">
					<k-field-text v-model="formData.id" />
				</k-form-item>
				<k-form-item label="数据日期">
					<k-field-text v-model="formData.reportDate" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="产品代码">
					<k-field-text v-model="formData.prodCd" />
				</k-form-item>
				<k-form-item label="产品登记编码">
					<k-field-text v-model="formData.prodRegEnc" />
				</k-form-item>
				<k-form-item label="持仓类型">
					<k-field-select v-model="formData.holdType" data-dict="position_type" />
				</k-form-item>
				<k-form-item label="首层资产代码">
					<k-field-text v-model="formData.fasstCd" />
				</k-form-item>
				<k-form-item label="资产三类">
					<k-field-text v-model="formData.assetThirdType" />
				</k-form-item>
				<k-form-item label="首层资产名称">
					<k-field-text v-model="formData.fasstNm" />
				</k-form-item>
				<k-form-item label="首层折算人民币金额（万元）">
					<k-field-text v-model="formData.famount" data-validate-type="money" data-digits="2" />
				</k-form-item>
				<k-form-item label="穿透前分类">
					<k-field-select v-model="formData.orgClassific" data-dict="g06_type" />
				</k-form-item>
				<k-form-item label="底层科目代码">
					<k-field-text v-model="formData.itmCd" />
				</k-form-item>
				<k-form-item label="底层科目名称">
					<k-field-text v-model="formData.itmNm" />
				</k-form-item>
				<k-form-item label="底层折算人民币金额（万元）">
					<k-field-text v-model="formData.damount" data-validate-type="money" data-digits="2" />
				</k-form-item>
				<k-form-item label="穿透后分类">
					<k-field-select v-model="formData.newClassific" data-dict="g06_type" />
				</k-form-item>
				<k-form-item label="初始投资时间">
					<k-field-text v-model="formData.finvTm" />
				</k-form-item>
				<k-form-item label="投资资产分类">
					<k-field-select v-model="formData.asstType" data-dict="invs_asst_type" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="投资资产本金（万元）">
					<k-field-text v-model="formData.asstAmount" data-validate-type="money" data-digits="2" />
				</k-form-item>
				<k-form-item label="投资地区">
					<k-field-text v-model="formData.asstZon" :data-max-length="20" />
				</k-form-item>
				<k-form-item label="管理人注册地">
					<k-field-text v-model="formData.mangZon" />
				</k-form-item>
				<k-form-item label="计提减值准备金额">
					<k-field-text v-model="formData.deprRdyAmt" />
				</k-form-item>
				<k-form-item label="已计提减值">
					<k-field-text v-model="formData.deprAmt" />
				</k-form-item>
				<k-form-item label="外汇额度类型">
					<k-field-select v-model="formData.fxType" data-dict="forex_exe_type" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="债务期限结构">
					<k-field-select v-model="formData.bdRmaiType" data-dict="debt_term_type" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="剩余天数">
					<k-field-text v-model="formData.rmaiDay" data-validate-type="number" />
				</k-form-item>
				<k-form-item label="风险事件">
					<k-field-text v-model="formData.riskEnvn" />
				</k-form-item>
				<k-form-item label="风险项目余额">
					<k-field-text v-model="formData.riskPjAmt" data-validate-type="money" />
				</k-form-item>

				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="ReportOverseasInvInfo.addReportOverseasInvInfo"
						data-from="addReportOverseasInvInfoForm"
						:data-model="formData"
						data-target="reportOverseasInvInfoGrid"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
					</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<!--    修改境外投资情况明细表弹出框   -->
		<k-popup ref="editReportOverseasInvInfoPopup" data-title="编辑">
			<k-form ref="editReportOverseasInvInfoForm" :data-col="2">
				<k-form-item label="id" v-show="false">
					<k-field-text v-model="formData.id" />
				</k-form-item>
				<k-form-item label="数据日期">
					<k-field-text v-model="formData.reportDate" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="产品代码">
					<k-field-text v-model="formData.prodCd" />
				</k-form-item>
				<k-form-item label="产品登记编码">
					<k-field-text v-model="formData.prodRegEnc" />
				</k-form-item>
				<k-form-item label="持仓类型">
					<k-field-select v-model="formData.holdType" data-dict="position_type" />
				</k-form-item>
				<k-form-item label="首层资产代码">
					<k-field-text v-model="formData.fasstCd" />
				</k-form-item>
				<k-form-item label="资产三类">
					<k-field-text v-model="formData.assetThirdType" />
				</k-form-item>
				<k-form-item label="首层资产名称">
					<k-field-text v-model="formData.fasstNm" />
				</k-form-item>
				<k-form-item label="首层折算人民币金额（万元）">
					<k-field-text v-model="formData.famount" data-validate-type="money" data-digits="2" />
				</k-form-item>
				<k-form-item label="穿透前分类">
					<k-field-select v-model="formData.orgClassific" data-dict="g06_type" />
				</k-form-item>
				<k-form-item label="底层科目代码">
					<k-field-text v-model="formData.itmCd" />
				</k-form-item>
				<k-form-item label="底层科目名称">
					<k-field-text v-model="formData.itmNm" />
				</k-form-item>
				<k-form-item label="底层折算人民币金额（万元）">
					<k-field-text v-model="formData.damount" data-validate-type="money" data-digits="2" />
				</k-form-item>
				<k-form-item label="穿透后分类">
					<k-field-select v-model="formData.newClassific" data-dict="g06_type" />
				</k-form-item>
				<k-form-item label="初始投资时间">
					<k-field-text v-model="formData.finvTm" />
				</k-form-item>
				<k-form-item label="投资资产分类">
					<k-field-select v-model="formData.asstType" data-dict="invs_asst_type" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="投资资产本金（万元）">
					<k-field-text v-model="formData.asstAmount" data-validate-type="money" data-digits="2" />
				</k-form-item>
				<k-form-item label="投资地区">
					<k-field-text v-model="formData.asstZon" :data-max-length="20" />
				</k-form-item>
				<k-form-item label="管理人注册地">
					<k-field-text v-model="formData.mangZon" />
				</k-form-item>
				<k-form-item label="计提减值准备金额">
					<k-field-text v-model="formData.deprRdyAmt" />
				</k-form-item>
				<k-form-item label="已计提减值">
					<k-field-text v-model="formData.deprAmt" />
				</k-form-item>
				<k-form-item label="外汇额度类型">
					<k-field-select v-model="formData.fxType" data-dict="forex_exe_type" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="债务期限结构">
					<k-field-select v-model="formData.bdRmaiType" data-dict="debt_term_type" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="剩余天数">
					<k-field-text v-model="formData.rmaiDay" data-validate-type="number" />
				</k-form-item>
				<k-form-item label="风险事件">
					<k-field-text v-model="formData.riskEnvn" />
				</k-form-item>
				<k-form-item label="风险项目余额">
					<k-field-text v-model="formData.riskPjAmt" data-validate-type="money" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="ReportOverseasInvInfo.updateReportOverseasInvInfo"
						data-from="editReportOverseasInvInfoForm"
						:data-model="formData"
						data-target="reportOverseasInvInfoGrid"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
					</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
		<k-popup ref="handleTaskPopup" data-title="重新生成报表">
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
import ReReport from "@/utils/ReReport.vue";
export default {
	components: {
      ReReport
   	},
	data() {
		return {
			formData: {},
			selectRowData: {},
			searchParam: {},
			loading: true,
			reloading: true,
			menuId: "M061835",
            buttonName: "重新生成报表",
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
		// handleTaskApp() {
		// 	if (this.$refs.handleTaskAppForm.validate()) {
		// 		this.$refs.createFormBtnRef.setIconStyle(0);

		// 		this.httpUtil
		// 			.comnUpdate({
		// 				action: "DwsProdTTRDBef.updateTaskAppQuery",
		// 				async: true,
		// 				params: { reportDate: this.formData.reportDate, menuId: "M061835", buttonName: "重新生成报表" },
		// 				successAlert: false,
		// 				dataAfterSuccess: (reData) => {
		// 					Tools.alertTime(reData.returnmsg, "success", 0);
		// 				},
		// 			})
		// 			.then((returndata) => {
		// 				this.$refs.createFormBtnRef.setIconStyle(1);
		// 				this.$refs.reportOverseasInvInfoGrid.load();
		// 			})
		// 			.catch((err) => {
		// 				console.log(err, "err");
		// 				this.$refs.createFormBtnRef.setIconStyle(1);
		// 			});
		// 		setTimeout(() => {
		// 			this.$refs.handleTaskPopup.close();
		// 		}, 300);
		// 	}
		// },
	},
};
</script>
