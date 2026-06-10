<template>
	<div class="py-page">
		<div>
			<k-form-search-customize
				ref="searchRef"
				data-model-name="DwsProdTTRDBef"
				data-label-width="80px"
				v-model="searchParam"
				data-target="dwsProdTTRDBefGrid"
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
					<k-field-select
						v-model="searchParam.productCode"
						data-action="ProdInfoOds.findProdInfoOds"
						ref="prodCodeId"
						data-display-field="prodCode,prodName"
						data-value-field="prodCode"
						:data-remote="true"
						:data-remote-paging="true"
					/>
				</k-form-item>
				<k-form-item label="资产代码">
					<k-field-text v-model="searchParam.icode" />
				</k-form-item>
				<k-form-item label="穿透前分类">
					<k-field-select v-model="searchParam.orgClassific" data-dict="g06_type" />
				</k-form-item>
				<k-form-item label="穿透后分类">
					<k-field-select v-model="searchParam.newClassific" data-dict="g06_type" />
				</k-form-item>
				<k-form-item label="持仓类型">
					<k-field-select v-model="searchParam.assettype" data-dict="position_type" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn
						slot="button"
						ref="uploadBtnRef"
						data-functype="POPUP"
						class="btn-custom-plain"
						data-target="uploadPopup"
						:handle-before="uploadHandleBefore"
						:load-disabled="false"
					>
						<md-icon>cloud_upload</md-icon>导入
					</k-btn>
					<k-btn
						slot="button"
						class="btn-custom-plain"
						data-functype="EXPORT"
						data-export-dict="true"
						data-target="dwsProdTTRDBefGrid"
						data-export-name="G06穿透前报表（调整后）"
						data-export-form="searchRef"
					>
						<md-icon>cloud_download</md-icon>导出
					</k-btn>
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
				ref="dwsProdTTRDBefGrid"
				@data-row-select="selectRow"
				data-action="DwsProdTTRDBef.findDwsProdTTRDBefs"
				data-operate-column="false"
				:data-autoload="false"
			>
				<k-grid-column data-header="id" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
				<k-grid-column data-header="数据日期" data-name="reportDate"></k-grid-column>
				<k-grid-column data-header="产品代码" data-name="productCode"></k-grid-column>
				<k-grid-column data-header="是否现管产品" data-name="cshMngF" data-dict="g06_yes_no" data-width="95"></k-grid-column>
				<k-grid-column data-header="资产代码" data-name="icode"></k-grid-column>
				<k-grid-column data-header="资产三类" data-name="assetThirdType"></k-grid-column>
				<k-grid-column data-header="资产名称" data-name="iname"></k-grid-column>
				<k-grid-column data-header="金额" data-name="amount"></k-grid-column>
				<k-grid-column data-header="汇率" data-name="changerate"></k-grid-column>
				<k-grid-column data-header="折算人民币金额" data-name="investedamountcny" data-width="110"></k-grid-column>
				<k-grid-column
					data-header="穿透前分类"
					data-name="orgClassific"
					data-dict="g06_type"
					data-width="90"
				></k-grid-column>
				<k-grid-column
					data-header="委托/自主管理"
					data-name="orderfreemanage"
					data-dict="g06_manager_type"
					data-width="98"
				></k-grid-column>
				<k-grid-column
					data-header="穿透后分类"
					data-name="newClassific"
					data-dict="g06_type"
					data-width="90"
				></k-grid-column>
				<k-grid-column data-header="是否逾期" data-name="isoverdue" data-dict="g06_yes_no"></k-grid-column>
				<k-grid-column
					data-header="是否投向房地产业"
					data-name="moneyofproperty"
					data-dict="g06_yes_no"
					data-width="80"
				></k-grid-column>
				<k-grid-column
					data-header="资金投向具体领域"
					data-name="cashtodomain"
					data-dict="g06_fields"
					data-width="80"
				></k-grid-column>
				<k-grid-column data-header="创业投资基金" data-name="vcintfund" data-dict="g06_yes_no"></k-grid-column>
				<k-grid-column
					data-header="政府出资产业投资基金"
					data-name="govintfund"
					data-dict="g06_yes_no"
					data-width="96"
				></k-grid-column>
				<k-grid-column data-header="非标行业" data-name="isnostandard" data-dict="g06_no_standard"></k-grid-column>
				<k-grid-column
					data-header="如填列1.4.6-11债券，请分信用登记"
					data-name="bondrating"
					data-dict="g06_bond_credit"
					data-width="134"
				></k-grid-column>
				<k-grid-column
					data-header="如填列1.4.2，请补充是否“1.4.2.a 专项债券”"
					data-name="specialbond"
					data-dict="g06_yes_no"
					data-width="148"
				></k-grid-column>
				<k-grid-column
					data-header="如填列1.5.16 非标，对股质类业务区分“1.5.16.a 场内”和“1.5.16.b 场外”"
					data-name="inmarketornot"
					data-dict="g06_on_off_site"
					data-width="235"
				></k-grid-column>
				<k-grid-column
					data-header="如填列1.5 非标，需补充是否“1.5.a 投向地方政府融资平台的部分”"
					data-name="cashtogovernment"
					data-dict="g06_yes_no"
					data-width="199"
				></k-grid-column>
				<k-grid-column
					data-header="1.15.a 投向“公共私营合作项目”（PPP）的部分（金额）"
					data-name="cashtopublic"
					data-width="170"
				></k-grid-column>
				<k-grid-column
					data-header="1.15.b 投向市场化债转股相关产品的部分（金额）"
					data-name="cashtorelateproduct"
					data-width="180"
				></k-grid-column>
				<k-grid-column data-header="1.15.c 逾期资产（金额）" data-name="moneyofoverdueasset" data-width="100"></k-grid-column>
				<k-grid-column
					data-header="3.2.3 二级资本债"
					data-name="secondlevelcaptialbond"
					data-dict="g06_yes_no"
				></k-grid-column>
				<k-grid-column data-header="3.2.2 永续债" data-name="continuebondforever" data-dict="g06_yes_no"></k-grid-column>
				<k-grid-column data-header="3.2.1 优先股" data-name="seniorbond" data-dict="g06_yes_no"></k-grid-column>
				<k-grid-column data-header="3.2.4 可转债" data-name="convertbond" data-dict="g06_yes_no"></k-grid-column>
				<k-grid-column
					data-header="3.2.5 其他银行资本补充工具"
					data-name="otherbanksupplementtools"
					data-dict="g06_yes_no"
					data-width="108"
				></k-grid-column>
				<k-grid-column data-header="持仓类型" data-name="assettype" data-dict="position_type"></k-grid-column>
				<k-grid-column data-header="持仓数量" data-name="hldnQntt" data-type="money"></k-grid-column>
				<k-grid-column data-header="处理日期" data-name="dealDate" data-hidden="true" data-export="false"></k-grid-column>
			</k-grid>
		</div>
		<k-popup ref="uploadPopup" data-title="导入">
			<k-form ref="addDwsProdTTRDBefForm" data-ui="element">
				<k-form-item label="导入日期" data-ui="element" data-input-width="500px">
					<k-field-date
						v-model="formData.reportDate"
						data-type="date"
						data-date-format="yyyy-MM-dd"
						data-value-format="yyyyMMdd"
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
						:data-success="onTTRDSubmitSuccess"
						:data-auto-upload="false"
						data-upload-url="upload/server/RptApp/uploadDwsProdTTRDBef.json"
					></k-field-excel-upload>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						ref="submitBtn"
						:data-auto-upload="false"
						data-from="addDwsProdTTRDBefForm"
						:data-handler="submitUploadTTRDParam"
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
		<!--    添加资产负载情况穿透前修改后弹出框   -->
		<k-popup ref="addDwsProdTTRDBefPopup" data-title="新增">
			<k-form ref="addDwsProdTTRDBefForm" :data-col="2">
				<k-form-item label="id">
					<k-field-text v-model="formData.id" />
				</k-form-item>
				<k-form-item label="产品代码">
					<k-field-text v-model="formData.productCode" />
				</k-form-item>
				<k-form-item label="资产代码">
					<k-field-text v-model="formData.icode" />
				</k-form-item>
				<k-form-item label="资产三类">
					<k-field-text v-model="formData.assetThirdType" />
				</k-form-item>
				<k-form-item label="资产名称">
					<k-field-text v-model="formData.iname" />
				</k-form-item>
				<k-form-item label="金额">
					<k-field-text v-model="formData.amount" />
				</k-form-item>
				<k-form-item label="汇率">
					<k-field-text v-model="formData.changerate" />
				</k-form-item>
				<k-form-item label="折算人民币金额">
					<k-field-text v-model="formData.investedamountcny" />
				</k-form-item>
				<k-form-item label="穿透前分类">
					<k-field-text v-model="formData.orgClassific" />
				</k-form-item>
				<k-form-item label="委托/自主管理">
					<k-field-text v-model="formData.orderfreemanage" />
				</k-form-item>
				<k-form-item label="穿透后分类">
					<k-field-text v-model="formData.newClassific" />
				</k-form-item>
				<k-form-item label="是否逾期">
					<k-field-text v-model="formData.isoverdue" />
				</k-form-item>
				<k-form-item label="资金投向具体领域">
					<k-field-text v-model="formData.cashtodomain" />
				</k-form-item>
				<k-form-item label="创业投资基金">
					<k-field-text v-model="formData.vcintfund" />
				</k-form-item>
				<k-form-item label="政府出资产业投资基金">
					<k-field-text v-model="formData.govintfund" />
				</k-form-item>
				<k-form-item label="非标行业">
					<k-field-text v-model="formData.isnostandard" />
				</k-form-item>
				<k-form-item label="如填列1.4.6-11债券，请分信用登记">
					<k-field-text v-model="formData.bondrating" />
				</k-form-item>
				<k-form-item label="如填列1.4.2，请补充是否“1.4.2.a 专项债券”">
					<k-field-text v-model="formData.specialbond" />
				</k-form-item>
				<k-form-item label="如填列1.5.16 非标，对股质类业务区分“1.5.16.a 场内”和“1.5.16.b 场外”">
					<k-field-text v-model="formData.inmarketornot" />
				</k-form-item>
				<k-form-item label="如填列1.5 非标，需补充是否“1.5.a 投向地方政府融资平台的部分”">
					<k-field-text v-model="formData.cashtogovernment" />
				</k-form-item>
				<k-form-item label="1.15.a 投向“公共私营合作项目”（PPP）的部分（金额）">
					<k-field-text v-model="formData.cashtopublic" />
				</k-form-item>
				<k-form-item label="1.15.b 投向市场化债转股相关产品的部分（金额）">
					<k-field-text v-model="formData.cashtorelateproduct" />
				</k-form-item>
				<k-form-item label="1.15.c 逾期资产（金额）">
					<k-field-text v-model="formData.moneyofoverdueasset" />
				</k-form-item>
				<k-form-item label="1.15.d 投向房地产业的部分">
					<k-field-text v-model="formData.moneyofproperty" />
				</k-form-item>
				<k-form-item label="3.2.3 二级资本债">
					<k-field-text v-model="formData.secondlevelcaptialbond" />
				</k-form-item>
				<k-form-item label="3.2.2 永续债">
					<k-field-text v-model="formData.continuebondforever" />
				</k-form-item>
				<k-form-item label="3.2.1 优先股">
					<k-field-text v-model="formData.seniorbond" />
				</k-form-item>
				<k-form-item label="3.2.4 可转债">
					<k-field-text v-model="formData.convertbond" />
				</k-form-item>
				<k-form-item label="3.2.5 其他银行资本补充工具">
					<k-field-text v-model="formData.otherbanksupplementtools" />
				</k-form-item>
				<k-form-item label="持仓类型">
					<k-field-text v-model="formData.assettype" />
				</k-form-item>
				<k-form-item label="处理日期">
					<k-field-text v-model="formData.dealDate" />
				</k-form-item>
				<k-form-item label="报告日期">
					<k-field-text v-model="formData.reportDate" />
				</k-form-item>

				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwsProdTTRDBef.addDwsProdTTRDBef"
						data-from="addDwsProdTTRDBefForm"
						:data-model="formData"
						data-target="dwsProdTTRDBefGrid"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
					</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<!--    修改资产负载情况穿透前修改后弹出框   -->
		<k-popup ref="editDwsProdTTRDBefPopup" data-title="修改">
			<k-form ref="editDwsProdTTRDBefForm" :data-col="2">
				<k-form-item label="id">
					<k-field-text v-model="formData.id" />
				</k-form-item>
				<k-form-item label="产品代码">
					<k-field-text v-model="formData.productCode" />
				</k-form-item>
				<k-form-item label="资产代码">
					<k-field-text v-model="formData.icode" />
				</k-form-item>
				<k-form-item label="资产三类">
					<k-field-text v-model="formData.assetThirdType" />
				</k-form-item>
				<k-form-item label="资产名称">
					<k-field-text v-model="formData.iname" />
				</k-form-item>
				<k-form-item label="金额">
					<k-field-text v-model="formData.amount" />
				</k-form-item>
				<k-form-item label="汇率">
					<k-field-text v-model="formData.changerate" />
				</k-form-item>
				<k-form-item label="折算人民币金额">
					<k-field-text v-model="formData.investedamountcny" />
				</k-form-item>
				<k-form-item label="穿透前分类">
					<k-field-text v-model="formData.orgClassific" />
				</k-form-item>
				<k-form-item label="委托/自主管理">
					<k-field-text v-model="formData.orderfreemanage" />
				</k-form-item>
				<k-form-item label="穿透后分类">
					<k-field-text v-model="formData.newClassific" />
				</k-form-item>
				<k-form-item label="是否逾期">
					<k-field-text v-model="formData.isoverdue" />
				</k-form-item>
				<k-form-item label="资金投向具体领域">
					<k-field-text v-model="formData.cashtodomain" />
				</k-form-item>
				<k-form-item label="创业投资基金">
					<k-field-text v-model="formData.vcintfund" />
				</k-form-item>
				<k-form-item label="政府出资产业投资基金">
					<k-field-text v-model="formData.govintfund" />
				</k-form-item>
				<k-form-item label="非标行业">
					<k-field-text v-model="formData.isnostandard" />
				</k-form-item>
				<k-form-item label="如填列1.4.6-11债券，请分信用登记">
					<k-field-text v-model="formData.bondrating" />
				</k-form-item>
				<k-form-item label="如填列1.4.2，请补充是否“1.4.2.a 专项债券”">
					<k-field-text v-model="formData.specialbond" />
				</k-form-item>
				<k-form-item label="如填列1.5.16 非标，对股质类业务区分“1.5.16.a 场内”和“1.5.16.b 场外”">
					<k-field-text v-model="formData.inmarketornot" />
				</k-form-item>
				<k-form-item label="如填列1.5 非标，需补充是否“1.5.a 投向地方政府融资平台的部分”">
					<k-field-text v-model="formData.cashtogovernment" />
				</k-form-item>
				<k-form-item label="1.15.a 投向“公共私营合作项目”（PPP）的部分（金额）">
					<k-field-text v-model="formData.cashtopublic" />
				</k-form-item>
				<k-form-item label="1.15.b 投向市场化债转股相关产品的部分（金额）">
					<k-field-text v-model="formData.cashtorelateproduct" />
				</k-form-item>
				<k-form-item label="1.15.c 逾期资产（金额）">
					<k-field-text v-model="formData.moneyofoverdueasset" />
				</k-form-item>
				<k-form-item label="1.15.d 投向房地产业的部分">
					<k-field-text v-model="formData.moneyofproperty" />
				</k-form-item>
				<k-form-item label="3.2.3 二级资本债">
					<k-field-text v-model="formData.secondlevelcaptialbond" />
				</k-form-item>
				<k-form-item label="3.2.2 永续债">
					<k-field-text v-model="formData.continuebondforever" />
				</k-form-item>
				<k-form-item label="3.2.1 优先股">
					<k-field-text v-model="formData.seniorbond" />
				</k-form-item>
				<k-form-item label="3.2.4 可转债">
					<k-field-text v-model="formData.convertbond" />
				</k-form-item>
				<k-form-item label="3.2.5 其他银行资本补充工具">
					<k-field-text v-model="formData.otherbanksupplementtools" />
				</k-form-item>
				<k-form-item label="持仓类型">
					<k-field-text v-model="formData.assettype" />
				</k-form-item>
				<k-form-item label="处理日期">
					<k-field-text v-model="formData.dealDate" />
				</k-form-item>
				<k-form-item label="报告日期">
					<k-field-text v-model="formData.reportDate" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="DwsProdTTRDBef.updateDwsProdTTRDBef"
						data-from="editDwsProdTTRDBefForm"
						:data-model="formData"
						data-target="dwsProdTTRDBefGrid"
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
import moment from "moment";
import Tools from "@/utils/tools.js";
import ReReport from "@/utils/ReReport.vue";
export default {
	name: "DwsProdTTRDBef",
	components: {
      ReReport
   	},
	data() {
		return {
			formData: {
				reportDate: "",
			},
			selectRowData: {},
			searchParam: {},
			loading: true,
			reloading: true,
			menuId: "M061812",
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
		// 		this.$refs.reloadBtnRef.setIconStyle(0);
		// 		this.httpUtil
		// 			.comnUpdate({
		// 				action: "DwsProdTTRDBef.updateTaskAppQuery",
		// 				async: true,
		// 				params: { menuId: "M061812", buttonName: "重新生成报表", reportDate: this.formData.reportDate },
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
		submitUploadTTRDParam() {
			//文件上传校验
			let validate = this.$refs.addDwsProdTTRDBefForm.validate();
			if (validate) {
				let temp = document.getElementsByClassName("upload-demo");
				let lis = temp[0].childNodes[1].childNodes.length;
				if (lis > 0) {
					this.$confirm("请确认是否包含所有存续产品/持有资管计划的估值数据，导入后该日期数据不会再用上游数据覆盖！！！", "提示", {
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "warning",
					})
						.then(() => {
							this.$refs.uploadBtnRef.setIconStyle(0);
							this.$refs.uploadRef.upload(this.formData);
							// 清除数据，关闭弹框
							setTimeout(() => {
								this.$refs.uploadPopup.close();
							}, 500);
						})
						.catch(() => {});
				} else {
					this.$message.error("上传文件不能为空!");
				}
			}
			return false;
		},
		uploadHandleBefore() {
			this.formData.reportDate = "";
			return true;
		},
		onTTRDSubmitSuccess() {
			if (this.searchParam.reportDate) {
				this.$refs.dwsProdTTRDBefGrid.load(this.searchParam);
			}
			this.$refs.uploadBtnRef.setIconStyle(1);
		},
		onSubmitError() {
			this.$refs.uploadBtnRef.setIconStyle(1);
		},
	},
};
</script>
