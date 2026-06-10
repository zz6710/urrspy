<template>
	<div class="py-page">
		<div>
			<k-form-search-customize ref="searchRef" data-model-name="ProdInvestDetails" v-model="searchParam" data-target="prodInvestDetailsGrid">
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
					<k-field-text v-model="searchParam.productCode" />
				</k-form-item>
				<k-form-item label="理财投资资产代码">
					<k-field-text v-model="searchParam.icode" />
				</k-form-item>
				<k-form-item label="底层代码">
					<k-field-text v-model="searchParam.bottomCode" />
				</k-form-item>
				<k-form-item label="穿透前分类">
					<k-field-select v-model="searchParam.orgClassific" data-dict="g06_type" />
				</k-form-item>
				<k-form-item label="G06穿透底层分类">
					<k-field-select v-model="searchParam.g06Type" data-dict="g06_scd_type" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="转型表分类-大类">
					<k-field-select v-model="searchParam.zxbFirstType" data-dict="first_level" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="转型表分类-二类">
					<k-field-select v-model="searchParam.zxbSecondType" data-dict="second_level" data-dict-type="1" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn
						slot="button"
						class="btn-custom-plain"
						data-functype="EXPORT"
						data-target="prodInvestDetailsGrid"
						data-export-name="理财产品投资情况明细表"
						data-export-dict="true"
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
				ref="prodInvestDetailsGrid"
				@data-row-select="selectRow"
				data-action="ProdInvestDetails.findProdInvestDetailss"
				data-operate-width="100"
				data-fixed="right"
				:dataAutoload="false"
			>
				<k-grid-column data-header="数据日期" data-name="actDt"></k-grid-column>
				<k-grid-column data-header="产品代码" data-name="productCode" data-width="120"></k-grid-column>
				<k-grid-column data-header="理财投资资产代码" data-name="icode" data-width="120"></k-grid-column>
				<k-grid-column data-header="资产三类" data-name="assetThirdType"></k-grid-column>
				<k-grid-column data-header="资管产品名称(第一层)" data-name="inamec1" data-width="120"></k-grid-column>
				<!-- <k-grid-column data-header="资管产品名称(第二层)" data-name="inamec2" data-width="120"></k-grid-column> -->
				<k-grid-column data-header="底层代码" data-name="bottomCode"></k-grid-column>
				<k-grid-column data-header="科目名称" data-name="itemName"></k-grid-column>
				<!-- <k-grid-column data-header="组合代码" data-name="comcode"></k-grid-column> -->
				<!-- <k-grid-column data-header="成本" data-name="cost"></k-grid-column> -->
				<k-grid-column data-header="市值" data-name="amount"></k-grid-column>
				<k-grid-column data-header="汇率" data-name="invValRateCsh"></k-grid-column>
				<!-- <k-grid-column data-header="币种" data-name="currency"></k-grid-column> -->
				<!-- <k-grid-column data-header="委托/自主管理" data-name="orderfreemanage" data-dict="g06_manager_type"></k-grid-column> -->
				<k-grid-column data-header="穿透前分类" data-name="orgClassific" data-dict="g06_type"></k-grid-column>
				<k-grid-column data-header="G06穿透底层分类" data-name="g06Type" data-dict="g06_scd_type" data-dict-type="1"></k-grid-column>
				<k-grid-column data-header="转型表分类-二类" data-name="zxbSecondType" data-dict="second_level" data-dict-type="1"></k-grid-column>
				<k-grid-column data-header="转型表分类-大类" data-name="zxbFirstType" data-dict="first_level" data-dict-type="1"></k-grid-column>
				<k-grid-column data-header="转型表分类-三类" data-name="zxbThirdType" data-dict="third_level" data-dict-type="1"></k-grid-column>
				<k-grid-column data-header="从母行划转的产品（比例）" data-name="prodmonrate" data-width="120"></k-grid-column>
				<k-grid-column data-header="自主发行产品（比例）" data-name="pordownrate" data-width="120"></k-grid-column>
				<k-grid-column data-header="我司持仓市值" data-name="mycompnyamount"></k-grid-column>
				<k-grid-column data-header="母行划转产品持仓市值" data-name="investmonamount" data-width="120"></k-grid-column>
				<k-grid-column data-header="自主发行产品持仓市值" data-name="investownamount" data-width="120"></k-grid-column>
				<!-- <k-grid-column data-header="信用等级" data-name="ratLevel"></k-grid-column> -->
				<!-- <k-grid-column data-header="是否投向公共私营合作项目（PPP）的部分" data-name="isPppPart" data-width="180"></k-grid-column> -->
				<!-- <k-grid-column data-header="是否投向市场化债转股相关" data-name="isMktBtsRlt" data-width="120"></k-grid-column> -->
				<!-- <k-grid-column data-header="是否投向地方政府融资平台的部分" data-name="isGovFncPart" data-width="120"></k-grid-column> -->
				<k-grid-column data-header="商业银行优先股" data-name="isFncStk" data-dict="1yes2no" data-dict-type="1"></k-grid-column>
				<k-grid-column data-header="商业银行永续债" data-name="isFncBnd" data-dict="1yes2no" data-dict-type="1"></k-grid-column>
				<k-grid-column
					data-header="商业银行二级资本债"
					data-name="isFncScdBnd"
					data-width="120"
					data-dict="1yes2no"
					data-dict-type="1"
				></k-grid-column>
				<k-grid-column data-header="商业银行可转债" data-name="isFncTsfBnd" data-dict="1yes2no" data-dict-type="1"></k-grid-column>
				<k-grid-column
					data-header="其他银行资本补充工具"
					data-name="isOthBnkTls"
					data-width="120"
					data-dict="1yes2no"
					data-dict-type="1"
				></k-grid-column>
				<!-- <k-grid-column data-header="是否地方政府专项债" data-name="isGovSpcBnd" data-width="120" data-dict="1yes2no" data-dict-type="1"></k-grid-column> -->
				<k-grid-column data-header="是否创业投资基金" data-name="vcintfund" data-width="120" data-dict="1yes2no" data-dict-type="1"></k-grid-column>
				<k-grid-column
					data-header="是否政府出资产业投资基金"
					data-name="govintfund"
					data-width="120"
					data-dict="1yes2no"
					data-dict-type="1"
				></k-grid-column>
				<k-grid-column data-header="投资估值表比例" data-name="invValRate"></k-grid-column>
				<k-grid-column data-header="非保本比例" data-name="nonGrtRate"></k-grid-column>
				<k-grid-column data-header="投向非保本金额" data-name="nonGrtAmt"></k-grid-column>
				<k-grid-column data-header="备注" data-name="mark"></k-grid-column>
				<k-grid-column data-header="资产到期日" data-name="assetEndDate"></k-grid-column>
				<!-- <k-grid-column data-header="资产期限特殊说明" data-name="assetTermPj"></k-grid-column> -->
				<k-grid-column data-header="报告日" data-name="bgDate"></k-grid-column>
				<k-grid-column data-header="剩余期限" data-name="overDay"></k-grid-column>
				<k-grid-column data-header="母行划转产品投资权重" data-name="investmonrate" data-width="120"></k-grid-column>
				<k-grid-column data-header="自主发行产品投资权重" data-name="investownrate" data-width="120"></k-grid-column>
				<k-grid-column data-header="产品分类" data-name="prodType" data-dict="prod_type" data-dict-type="1"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn class="btn-custom-text" data-functype="POPUP" data-size="mini" data-target="editProdInvestDetailsPopup"> 修改 </k-btn>
				</template>
			</k-grid>
		</div>
		<k-popup ref="editProdInvestDetailsPopup" data-title="修改">
			<k-form ref="editProdInvestDetailsForm" :data-col="2">
				<k-form-item label="数据日期">
					<k-field-text v-model="formData.actDt" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="产品代码">
					<k-field-text v-model="formData.productCode" />
				</k-form-item>
				<k-form-item label="理财投资资产代码">
					<k-field-text v-model="formData.icode" />
				</k-form-item>
				<k-form-item label="资产三类">
					<k-field-text v-model="formData.assetThirdType" />
				</k-form-item>
				<k-form-item label="资管产品名称(第一层)">
					<k-field-text v-model="formData.inamec1" />
				</k-form-item>
				<!-- <k-form-item label="资管产品名称(第二层)">
					<k-field-text v-model="formData.inamec2" />
				</k-form-item> -->
				<k-form-item label="底层代码">
					<k-field-text v-model="formData.bottomCode" />
				</k-form-item>
				<k-form-item label="科目名称">
					<k-field-text v-model="formData.itemName" />
				</k-form-item>
				<!-- <k-form-item label="组合代码">
					<k-field-text v-model="formData.comcode" />
				</k-form-item> -->
				<!-- <k-form-item label="成本">
					<k-field-text v-model="formData.cost" />
				</k-form-item> -->
				<k-form-item label="市值">
					<k-field-text v-model="formData.amount" @data-on-change="changeValue" />
				</k-form-item>
				<k-form-item label="汇率">
					<k-field-text v-model="formData.invValRateCsh" />
				</k-form-item>
				<!-- <k-form-item label="币种">
					<k-field-text v-model="formData.currency" />
				</k-form-item> -->
				<!-- <k-form-item label="委托/自主管理">
					<k-field-select v-model="formData.orderfreemanage" data-dict="g06_manager_type"/>
				</k-form-item> -->
				<k-form-item label="穿透前分类">
					<k-field-select v-model="formData.orgClassific" data-dict="g06_type" />
				</k-form-item>
				<k-form-item label="G06穿透底层分类">
					<k-field-select v-model="formData.g06Type" data-dict="g06_scd_type" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="转型表分类-二类">
					<k-field-select v-model="formData.zxbSecondType" data-dict="second_level" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="转型表分类-大类">
					<k-field-select v-model="formData.zxbFirstType" data-dict="first_level" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="转型表分类-三类">
					<k-field-select v-model="formData.zxbThirdType" data-dict="third_level" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="从母行划转的产品（比例）">
					<k-field-text v-model="formData.prodmonrate" @data-on-change="changeValue" />
				</k-form-item>
				<k-form-item label="自主发行产品（比例）">
					<k-field-text v-model="formData.pordownrate" @data-on-change="changeValue" />
				</k-form-item>
				<k-form-item label="我司持仓市值">
					<k-field-text v-model="formData.mycompnyamount" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="母行划转产品持仓市值">
					<k-field-text v-model="formData.investmonamount" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="自主发行产品持仓市值">
					<k-field-text v-model="formData.investownamount" :data-disabled="true" />
				</k-form-item>
				<!-- <k-form-item label="信用等级">
					<k-field-text v-model="formData.ratLevel" />
				</k-form-item> -->
				<!-- <k-form-item label="是否投向公共私营合作项目（PPP）的部分">
					<k-field-text v-model="formData.isPppPart" />
				</k-form-item> -->
				<!-- <k-form-item label="是否投向市场化债转股相关">
					<k-field-text v-model="formData.isMktBtsRlt" />
				</k-form-item> -->
				<!-- <k-form-item label="是否投向地方政府融资平台的部分">
					<k-field-text v-model="formData.isGovFncPart" />
				</k-form-item> -->
				<k-form-item label="商业银行优先股:01-是 02-否">
					<k-field-select v-model="formData.isFncStk" data-dict="1yes2no" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="商业银行永续债:01-是 02-否">
					<k-field-select v-model="formData.isFncBnd" data-dict="1yes2no" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="商业银行二级资本债:01-是 02-否">
					<k-field-select v-model="formData.isFncScdBnd" data-dict="1yes2no" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="商业银行可转债:01-是 02-否">
					<k-field-select v-model="formData.isFncTsfBnd" data-dict="1yes2no" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="其他银行资本补充工具:01-是 02-否">
					<k-field-select v-model="formData.isOthBnkTls" data-dict="1yes2no" data-dict-type="1" />
				</k-form-item>
				<!-- <k-form-item label="是否地方政府专项债:01-是 02-否">
					<k-field-select v-model="formData.isGovSpcBnd" data-dict="1yes2no" data-dict-type="1"/>
				</k-form-item> -->
				<k-form-item label="是否创业投资基金:01-是 02-否">
					<k-field-select v-model="formData.vcintfund" data-dict="1yes2no" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="是否政府出资产业投资基金:01-是 02-否">
					<k-field-select v-model="formData.govintfund" data-dict="1yes2no" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="投资估值表比例">
					<k-field-text v-model="formData.invValRate" @data-on-change="changeValue" />
				</k-form-item>
				<k-form-item label="非保本比例">
					<k-field-text v-model="formData.nonGrtRate" />
				</k-form-item>
				<k-form-item label="投向非保本金额">
					<k-field-text v-model="formData.nonGrtAmt" />
				</k-form-item>
				<k-form-item label="备注">
					<k-field-text v-model="formData.mark" />
				</k-form-item>
				<k-form-item label="资产到期日">
					<k-field-date
						v-model="formData.assetEndDate"
						data-type="date"
						data-date-format="yyyy-MM-dd"
						data-value-format="yyyyMMdd"
						@data-on-change="changeValue"
					/>
				</k-form-item>
				<!-- <k-form-item label="资产期限特殊说明">
					<k-field-text v-model="formData.assetTermPj" />
				</k-form-item> -->
				<k-form-item label="报告日">
					<k-field-date v-model="formData.bgDate" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" />
				</k-form-item>
				<k-form-item label="剩余期限">
					<k-field-text v-model="formData.overDay" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="母行划转产品投资权重">
					<k-field-text v-model="formData.investmonrate" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="自主发行产品投资权重">
					<k-field-text v-model="formData.investownrate" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="产品分类">
					<k-field-select v-model="formData.prodType" data-dict="prod_type" data-dict-type="1" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="ProdInvestDetails.updateProdInvestDetails"
						data-from="editProdInvestDetailsForm"
						:data-model="formData"
						data-target="prodInvestDetailsGrid"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
		<!-- <GenerateFormAgainDialog ref="formAgainRef" paraid="90000061003" menuId="M061815" buttonName="重新生成报表" /> -->
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
	name: "ProdInvestDetails",
	mixins: [AssetMixin, GenerateFormAgainMixin],
	components: {
      ReReport
   	},
	data() {
		return {
			formData: {},
			selectRowData: {},
			searchParam: {},
			formData: { reportDate: "" },
			menuId: "M061815",
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
		// 				params: { menuId: "M061815", buttonName: "重新生成报表", reportDate: this.lastDay, paraid: "90000061003" },
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
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},
		/*变更值后对各项数据进行重新计算 程晓鹏 2025.03.27 modify */
		changeValue(val) {
			//变更后的数据
			let v_amount = this.formData.amount; //市值
			let v_invValRate = this.formData.invValRate; //投资估值表比例
			let v_prodmonrate = this.formData.prodmonrate; //从母行划转的产品（比例）
			let v_pordownrate = this.formData.pordownrate; //自主发行产品（比例）
			let v_assetEndDate = this.formData.assetEndDate; //资产到期日
			let v_bgDate = this.formData.bgDate; //报告日

			//进行计算
			let exec_mycompnyamount = v_amount * v_invValRate; //我司持仓市值 = 市值*投资估值表比例
			let exec_investmonamount = exec_mycompnyamount * v_prodmonrate; //母行划转产品持仓市值 = 我司持仓市值*从母行划转的产品（比例）
			let exec_investownamount = exec_mycompnyamount * v_pordownrate; //自主发行产品持仓市值 = 我司持仓市值*自主发行产品（比例）
			let exec_overDay = this.dateDiff(v_assetEndDate, v_bgDate); //剩余期限 = 资产到期日-报告日
			let exec_investmonrate = (exec_investmonamount * exec_overDay) / 365; //母行划转产品投资权重 = 母行划转产品持仓市值*剩余期限/365 保留8位小数
			let exec_investownrate = (exec_investownamount * exec_overDay) / 365; //自主发行产品投资权重 = 自主发行产品持仓市值*剩余期限/365 保留8位小数
			let exec_investmonrate_value = exec_investmonrate.toFixed(8); //母行划转产品投资权重, 保留8位小数
			let exec_investownrate_value = exec_investownrate.toFixed(8); //自主发行产品投资权重 保留8位小数

			//进行赋值
			this.formData.mycompnyamount = exec_mycompnyamount; //我司持仓市值
			this.formData.investmonamount = exec_investmonamount; //母行划转产品持仓市值
			this.formData.investownamount = exec_investownamount; //自主发行产品持仓市值
			this.formData.overDay = exec_overDay; //剩余期限
			this.formData.investmonrate = exec_investmonrate_value; //母行划转产品投资权重
			this.formData.investownrate = exec_investownrate_value; //自主发行产品投资权重
		},
		dateDiff(date1, date2) {
			if (date1 === null || date2 === null || date1 == "" || date2 == "") {
				return 0;
			}
			let str_date1 = this.formatDate(date1); //格式化字符串
			let str_date2 = this.formatDate(date2); //格式化字符串
			let time1 = new Date(str_date1).getTime();
			let time2 = new Date(str_date2).getTime();
			let diff = time1 - time2;
			let day = Math.floor(diff / (1000 * 60 * 60 * 24)); //剩余天数
			return day;
		},
		formatDate(date) {
			if (date == "") {
				return "";
			} else {
				return date.substr(0, 4) + "-" + date.substr(4, 2) + "-" + date.substr(6, 2);
			}
		},
	},
};
</script>
