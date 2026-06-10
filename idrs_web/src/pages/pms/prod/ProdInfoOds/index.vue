<template>
	<div class="py-page">
		<k-form-search-customize data-target="prodInfoOdsGrid" v-model="queryParam1" data-label-width="150px">
			<k-form-item label="产品代码/名称">
				<k-field-select
					v-model="SearchParam.prodCode"
					data-action="ProdInfoOds.findProdInfoOds"
					ref="prodCodeId"
					data-display-field="prodCode,prodName"
					data-value-field="prodCode"
					:data-remote="true"
					:data-remote-paging="true"
				/>
			</k-form-item>
			<k-form-item label="中债登记编码">
				<k-field-text v-model="SearchParam.checkInon" @data-on-change="changeParam(SearchParam.checkInon,'checkInon')"/>
			</k-form-item>
			<k-form-item label="人行登记编码">
				<k-field-text v-model="SearchParam.pbcRegcode" @data-on-change="changeParam(SearchParam.pbcRegcode,'pbcRegcode')"/>
			</k-form-item>
			<k-form-item label="产品状态">
				<k-field-select v-model="SearchParam.prodStatus" data-dict="prodStatus" data-dict-type="1" />
			</k-form-item>
            <k-form-item label="募集起始日期">
			    <k-field-date v-model="querySubsBdateDateRange" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" />
			</k-form-item>
			<k-form-item label="产品起始日期">
				<k-field-date v-model="queryEstablishDateDateRange" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" />
			</k-form-item>
			<k-form-item label="产品实际终止日期">
				<k-field-date v-model="queryRealEndDateDateRange" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" />
			</k-form-item>
			<k-form-item label="产品运作模式">
				<k-field-select v-model="SearchParam.prodMod" data-dict="prod_mod_sys" data-dict-type="1" />
			</k-form-item>
			<k-form-item label="是否现金管理类(人行)">
				<k-field-select v-model="SearchParam.cashType" data-dict="1yes2no" data-dict-type="1" />
			</k-form-item>
		</k-form-search-customize>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn
						class="btn-custom-plain"
						data-target="prodInfoOdsGrid"
						data-export-name="产品基本信息"
						data-export-dict="true"
						data-functype="EXPORT"
						data-url="ProdInfoOds.findProdInfoOds"
						><md-icon>cloud_download</md-icon>导出</k-btn
					>
					<k-btn class="btn-custom-plain" data-target="prodInfoOdsGrid" @click="handleProdForm">
						<i v-show="loading"><md-icon md-src="/static/svg/confirm.svg" /></i>
						<i v-show="!loading" class="el-icon-loading" />
						生成产品端报表</k-btn>
					<k-btn v-if="false" class="btn-custom-plain" data-target="prodInfoOdsGrid" :data-handler="handleUpdateProdPbc" ref="checkButton">
			            <md-icon md-src="/static/svg/confirm.svg"></md-icon>更新人行登记编码
					</k-btn>
					<div v-if="false" style="width: 120px">
						执行状态: <strong>{{ !loading ? "运行中" : "已完成" }}</strong>
					</div>
					<k-btn v-if="false" class="btn-custom-icon" @click="getRunStatus"> <md-icon md-src="/static/svg/reset.svg"></md-icon></k-btn>
				</div>
				<div class="right" v-show="vShow">
				&nbsp;&nbsp;生成开始时间：<span class="detail">{{ startTime }}</span>
				&nbsp;&nbsp;生成结束时间：<span class="detail">{{ endTime }}</span>
				&nbsp;&nbsp;生成数据日期：<span class="detail">{{ reportDate }}</span>
				&nbsp;&nbsp;生成结果：<a href="javascript:void(0)" class="detail1" @click="handleClick">{{ resultStatus }}</a>
				<k-btn class="btn-custom-icon" @click="queryRelust"> <md-icon md-src="/static/svg/reset.svg"></md-icon></k-btn>
				</div>
			</div>
			<k-grid
				ref="prodInfoOdsGrid"
				@data-row-select="selectRow"
				data-action="ProdInfoOds.findProdInfoOds"
				data-fixed="right"
				data-operate-width="160px"
				data-dict-type="1"
				@init="
					(grid) => {
						this.$kgrid = grid;
					}
				"
				:data-display="false"
			>
				<k-grid-column data-header="产品全称" data-name="prodNmFu" data-width="130" />
				<k-grid-column data-header="产品代码" data-name="prodCode" data-width="130" />
				<k-grid-column data-header="产品状态" data-name="prodStatus" data-dict="prodStatus" data-width="90" />
				<k-grid-column data-header="中债登记编码" data-name="checkInon" data-width="130" />
				<k-grid-column data-header="人行登记编码" data-name="pbcRegcode" data-width="130" />
				<k-grid-column data-header="产品投资性质" data-name="t8InvestPropType" data-dict="prod_invest_nature" data-width="110" />
				<k-grid-column data-header="产品运作模式" data-name="prodMod" data-dict="prod_mod_sys" data-width="120" />
				<k-grid-column data-header="产品募集方式" data-name="collMod" data-dict="t8_raise_type" />
				<k-grid-column data-header="管理方式" data-name="operMod" data-dict="managementMethod" />
				<k-grid-column data-header="产品期限" data-name="prodCycle" data-dict="prod_term" />
				<k-grid-column data-header="产品品牌" data-name="prodBrand" />
				<k-grid-column data-header="产品期次" data-name="prodTimes" />
				<k-grid-column data-header="募集币种" data-name="issuCcy" />
				<k-grid-column data-header="兑付本金币种" data-name="returnCcy" />
				<k-grid-column data-header="兑付收益币种" data-name="incomeCcy" />
				<k-grid-column data-header="募集起始日期" data-name="subsBdate" />
				<k-grid-column data-header="募集结束日期" data-name="subsEdate" />
				<k-grid-column data-header="发行机构提前终止权标识" data-name="termFlag" data-dict="t8_redeem_flag" data-width="100" />
				<k-grid-column data-header="客户赎回权标识(中债)" data-name="redeemFlag" data-dict="t8_redeem_flag" data-width="100" />
				<k-grid-column data-header="产品增信标识" data-name="prodCreditFlag" data-dict="prodZxStatus" />
				<k-grid-column data-header="产品增信机构类型" data-name="prodCreditOrg" data-dict="out_cpzxjglx" />
				<k-grid-column data-header="产品增信形式" data-name="prodCreditMod" data-dcit="out_cpzxxs" />
				<k-grid-column data-header="境内托管机构代码(中债)" data-name="bordTrustiCode" />
				<k-grid-column data-header="境外托管机构国别" data-name="oversTrustiNation" data-dict="worldCountCode" />
				<k-grid-column data-header="境内托管机构名称" data-name="bordTrustiName" data-dict="domestic_custodian_name" />
				<k-grid-column data-header="境外托管机构名称" data-name="oversTrustiName" />
				<k-grid-column data-header="产品起始日期" data-name="establishDate" />
				<k-grid-column data-header="产品实际终止日期" data-name="realEndDate" />
				<k-grid-column data-header="产品预计终止日期" data-name="endDate" />
				<k-grid-column data-header="是否为结构化(分级)产品" data-name="isStructprod" data-dict="1yes2no" data-width="100" />
				<k-grid-column data-header="是否现金管理类(中债)" data-name="cashTypeZ" data-dict="1yes2no" />
				<k-grid-column data-header="定期开放周期(天)" data-name="rglrPrdOpnCyc" />
				<k-grid-column data-header="其他规律开放周期(天)" data-name="othRegulOpenCyc" data-width="100" />
				<k-grid-column data-header="产品收益类型" data-name="incomeType" data-dict="prod_revenue_type" />
				<k-grid-column data-header="新老产品标记" data-name="newOldProdF" data-dict="newProd" />
				<k-grid-column data-header="是否金融同业专属" data-name="blgFinSamBusF" data-dict="1yes0no" />
				<k-grid-column data-header="资金投向地区" data-name="salePlace" data-dict="out_zjtxdq" />
				<k-grid-column data-header="产品投资国家或地区(境外)" data-name="speciCountryRegion" data-dict="worldCountCode" data-width="100" />
				<k-grid-column data-header="理财业务服务模式" data-name="srvMode" data-dict="t8_srv_mode" />
				<k-grid-column data-header="产品资产配置方式" data-name="assetMaping" data-dict="t8_asset_maping" />
				<k-grid-column data-header="产品管理模式" data-name="manageMode" data-dict="t8_prod_mng_mod" />
				<k-grid-column data-header="实际管理人名称" data-name="adminName" />
				<k-grid-column data-header="产品定价方式" data-name="pricingType" data-dict="prod_price_way" />
				<k-grid-column data-header="业绩比较基准" dataDigits="5" data-name="perfmBenchmRate" />
				<k-grid-column data-header="是否设置最短持有期限" data-name="isMinHoldTerm" data-dict="1yes2no" data-width="100" />
				<k-grid-column data-header="最短持有期限(天)" data-name="minHoldTerm" />
				<k-grid-column data-header="最短持有期后是否自由赎回" data-name="redeemAfterHold" data-dict="optionRedemptPeriod" data-width="100" />
				<k-grid-column data-header="产品销售区域" data-name="prodSalZon" data-dict="subm_prod_sale_area" data-dict-type="0" data-width="130" />
				<k-grid-column data-header="起点销售金额" dataDigits="2" data-name="investThresh" />
				<k-grid-column data-header="销售手续费率%" dataDigits="5" data-name="saleCommisRate" />
				<k-grid-column data-header="托管费率%" dataDigits="5" data-name="custodyFeeRate" />
				<k-grid-column data-header="募集起始日期(从)" data-name="subscrSdEarliest" />
				<k-grid-column data-header="募集起始日期(到)" data-name="subscrEdLatest" />
				<k-grid-column data-header="计划募集金额(元)" dataDigits="2" data-name="planFundAmount" />
				<k-grid-column data-header="投资者风险偏好" data-name="investorTrend" data-dict="investor_risk_preference" />y
				<k-grid-column data-header="产品风险等级" data-name="riskLev" data-dict="risk_rate" />
				<k-grid-column data-header="产品特殊属性" data-name="prodEspPrpt" data-dict="prodSpecialProp" />
				<k-grid-column data-header="投资管理费率%" dataDigits="5" data-name="investMngFeeRate" />
				<k-grid-column data-header="合作模式" data-name="cooperationMode" data-dict="coorpMode" />
				<k-grid-column data-header="合作机构名称" data-name="cooperationOrgName" />
				<k-grid-column data-header="投资本金到账日" data-name="returnCost" data-dict="invest_income_arrive_date" />
				<k-grid-column data-header="投资收益到账日" data-name="returnIncome" data-dict="invest_income_arrive_date" />
				<k-grid-column data-header="投资资产种类及比例" data-name="prodPrecent" data-width="100" />
				<k-grid-column data-header="产品审批人姓名" data-name="authorName" />
				<k-grid-column data-header="产品审批人身份证号" data-name="authorIdentif" data-width="100" />
				<k-grid-column data-header="产品设计人姓名" data-name="designName" />
				<k-grid-column data-header="产品设计人身份证号" data-name="designIdentif" data-width="100" />
				<k-grid-column data-header="投资经理姓名" data-name="manageName" />
				<k-grid-column data-header="投资经理身份证号" data-name="manageIdentif" />
				<k-grid-column data-header="业务联络人姓名" data-name="salemanName" />
				<k-grid-column data-header="业务联络人座机" data-name="salemanPhoneno" />
				<k-grid-column data-header="业务联络人手机" data-name="salemanTelno" />
				<k-grid-column data-header="业务联络人邮箱" data-name="salemanEmail" />
				<k-grid-column data-header="分级比例" data-name="clsfSto" />
				<k-grid-column data-header="业绩比较基准上限%" dataDigits="5" data-name="perfmBenchmUpper" />
				<k-grid-column data-header="业绩比较基准下限%" dataDigits="5" data-name="perfmBenchmLower" />
				<k-grid-column data-header="业绩比较基准说明" data-name="yjbjjzsmPj" />
				<k-grid-column data-header="开放模式" data-name="openMod" data-dict="open_mod" />
				<k-grid-column data-header="规律开放周期" data-name="regularOpenCycle" data-dict="regular_open_cycle" />
				<k-grid-column data-header="无规律开放说明" data-name="irregularOpenDesc" />
				<k-grid-column data-header="首次开放周期起始日" data-name="firstOpenDate" data-width="100" />
				<k-grid-column data-header="节假日是否开放" data-name="isOpenInHoliday" data-dict="1yes2no" />
				<k-grid-column data-header="平均开放次数(年化)" data-name="openTimes" data-width="100" />
				<k-grid-column data-header="开放期业务" data-name="openPeriodBusiness" data-dict="t8_open_control" />
				<k-grid-column data-header="开放期业务说明" data-name="openPeriBusiDesc" />
				<k-grid-column data-header="资金托管账号" data-name="accountCode" />
				<k-grid-column data-header="资金托管账户" data-name="accountName" />
				<k-grid-column data-header="是否现金管理类(人行)" data-name="cashType" data-dict="1yes2no" />
        <k-grid-column data-header="境内托管机构代码(人行)" data-name="bordTrustiCodeP" />
				<k-grid-column data-header="客户赎回权标识(人行)" data-name="redeemFlagPb" data-dict="t8_redeem_flag" data-width="100" />
				<k-grid-column data-header="产品变更日期" data-name="prodChangeDate" />
				<k-grid-column data-header="受托机构管理职责" data-name="custodyOrgMngDuty" data-dict="mngDuty" data-width="100" />
				<k-grid-column data-header="收益权转让产品标识" data-name="incomeTransProdMark" data-dict="1yes2no" data-width="100" />
				<k-grid-column data-header="跨境理财通" data-name="crossBorderWealth" data-dict="1yes2no" />
				<k-grid-column data-header="基本信息公开标识" data-name="baseInfoOpenMark" data-dict="1yes2no" />
				<k-grid-column data-header="变更原因" data-name="changeReason" />
				<k-grid-column data-header="产品展期标识" data-name="prodRenewalMark" data-dict="1yes2no" />
				<k-grid-column data-header="是否处于清算中" data-name="liquidateMark" data-dict="1yes2no" />
				<k-grid-column data-header="最短开放周期" data-name="minPrdOpnCyc" />
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text specialClass"
						data-functype="POPUP"
						data-size="mini"
						data-target="prodInfoOdsPopup"
						v-if="global.isShowAuthorityButton('ProdInfoOds.updateProdInfo')"
						:data-handler="handleEdit"
					>
						修改
					</k-btn>
					<!-- <k-btn
						class="btn-custom-text specialClass"
						data-functype="SUBMIT"
						data-action="ProdInfoOds.deleteProdInfoOds"
						data-size="mini"
						data-target="prodInfoOdsPopup"
						:data-confirm="true"
						v-if="global.isShowAuthorityButton('ProdInfoOds.deleteProdInfoOds')"
					>
						删除
					</k-btn> -->
					<!-- <k-btn
						class="btn-custom-text specialClass"
						data-descript="修改"
						data-functype="POPUP"
						data-size="mini"
						data-target="prodInfoOdsPopupReport"
						style="width: 110px"
						:data-disabled="scope.row.row.fileStatus === '1' || scope.row.row.motherFundFlag === '0'"
						v-if="global.isShowAuthorityButton('ProdInfoOds.updateProdInfoOds')"
					>
						申报状态
					</k-btn> -->
					<k-btn
						class="btn-custom-text specialClass"
						data-descript="详情"
						data-functype="POPUP"
						data-size="mini"
						data-target="prodInfoOdsPopup"
						:data-handler="handleDetail"
					>
						详情
					</k-btn>
				</template>
			</k-grid>
		</div>

		<k-popup
			ref="prodInfoOdsPopup"
			:class="['edit-popup', 'type-' + dialogType, 'h-dialog']"
			:data-title="dialogType=='edit' ? '修改' : '详情'"
			:dataDialogDrag="true"
		>
			<div class="dialog-content">
				<div class="dialog-main">
					<k-form :data-col="3" dataLabelWidth="200px">
						<k-form-item label="产品状态" :class="[handleItemDiff('prodStatus')]">
							<k-field-select data-dict="prodStatus" data-dict-type="1" v-model="formData.prodStatus" :data-disabled="dialogType=='detail'"></k-field-select>
						</k-form-item>
					</k-form>
					<div class="title-block">申报要素</div>
					<ProdRegist ref="ref1" :formData="formData" :formDataCopy="formDataCopy" :type="dialogType" />
					<div class="title-block">发行要素</div>
					<ProdIssue :formData="formData" :formDataCopy="formDataCopy" :type="dialogType" />
					<div class="title-block">募集要素</div>
					<ProdRaise :formData="formData" :formDataCopy="formDataCopy" :type="dialogType" />
					<div class="title-block">人行要素</div>
					<ProdPeopleBank :formData="formData" :formDataCopy="formDataCopy" :type="dialogType" />
				</div>
				<div class="dialog-footer">
					<k-form-footer data-align="center">
						<template v-if="dialogType !='detail'">
							<k-btn
								class="btn-custom-primary"
								data-functype="SUBMIT"
								data-target="prodInfoOdsGrid"
								ref="submitBtn"
								data-from="prodInfoOdsForm"
								:data-model="formData"
								data-action="ProdInfoOds.updateProdInfo"
								:handle-before="handleBefore"
							>
								确定
							</k-btn>
							<k-btn class="btn-custom-plain" data-functype="CLOSE">关闭</k-btn>
						</template>
						<k-btn v-else class="btn-custom-plain" data-functype="CLOSE">关闭</k-btn>
					</k-form-footer>
				</div>
			</div>
		</k-popup>

		<k-popup ref="prodInfoOdsPopupReport" data-title="登记申报结果" :dataDialogDrag="true">
			<k-form ref="prodInfoOdsForm" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px" isFormBodyScreen>
				<k-form-item label="序号" v-show="false">
					<k-field-text v-model="formData.id" />
				</k-form-item>
				<k-form-item label="产品代码">
					<k-field-text v-model="formData.prodCode" :data-disabled="true" :data-max-length="32" />
				</k-form-item>
				<k-form-item label="产品名称">
					<k-field-text v-model="formData.prodName" :data-disabled="true" :data-max-length="128" />
				</k-form-item>
				<k-form-item label="申报结果">
					<k-field-select v-model="formData.prodReportStatus" data-dict="prod_report_status" @data-on-change="resetCheckInon" />
				</k-form-item>
				<k-form-item label="中债登记编码">
					<k-field-text
						v-model="formData.checkInon"
						:data-max-length="32"
						:data-disabled="formData.prodReportStatus !== '3'"
						:data-allowblank="formData.prodReportStatus !== '3'"
					/>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-target="prodInfoOdsGrid"
						ref="submitBtn"
						data-from="prodInfoOdsForm"
						:data-model="formData"
						data-action="ProdInfoOds.updateProdInfoReport"
					>
						确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
	</div>
</template>

<script>
import Tools from "@/utils/tools";
import ProdRegist from "./regist.vue";
import ProdIssue from "./issue.vue";
import ProdRaise from "./raise.vue";
import ProdPeopleBank from "./peopleBank.vue";

export default {
	name: "ProdInfoOds",
	components: {
		ProdRegist,
		ProdIssue,
		ProdRaise,
		ProdPeopleBank,
	},
	data() {
		return {
			formData: {
				prodCreditMod: ""
			},
			formDataCopy: {},
			selectRowData: {},
			SearchParam: {}, //查询参数
			showSubmitBtn: true,
			prodReportStatus: "",
			fileStatus: "",
			motherFundFlag: "",
			checkInon: "",
			dialogType: "edit",
			loading: true,
			reloading: true,
			timer: null,
			querySubsBdateDateRange: [],
		    queryEstablishDateDateRange: [],
			queryRealEndDateDateRange: [],
			menuId: "M0605",
			buttonName: "生成产品端报表",
			vShow: false,
			startTime: "",
			endTime: "",
			reportDate: "",
			resultStatus: "",
			resultInfo: "",
		};
	},
	created() {
		this.queryRelust();
    },
	computed: {
		queryParam() {
			return {
				prodCode: this.SearchParam.prodCode,
				establishDate1: this.SearchParam.establishDate1,
				establishDate2: this.SearchParam.establishDate2,
				realEndDate1: this.SearchParam.realEndDate1,
				realEndDate2: this.SearchParam.realEndDate2,
			};
		},
		queryParam1() {
			return {
				...this.SearchParam,
				subsBdate1: this.querySubsBdateDateRange ? this.querySubsBdateDateRange[0] : null,
				subsBdate2: this.querySubsBdateDateRange ? this.querySubsBdateDateRange[1] : null,
				establishDate1: this.queryEstablishDateDateRange ? this.queryEstablishDateDateRange[0] : null,
				establishDate2: this.queryEstablishDateDateRange ? this.queryEstablishDateDateRange[1] : null,
				realEndDate1: this.queryRealEndDateDateRange ? this.queryRealEndDateDateRange[0] : null,
				realEndDate2: this.queryRealEndDateDateRange ? this.queryRealEndDateDateRange[1] : null,
			};
		},
	},
	activated() {
		this.$nextTick(()=>{
			this.$refs.prodInfoOdsGrid._load(this.queryParam1)
		})
	},
	methods: {
	  changeParam(paramValue,paramKey){
	    if(paramValue) {
	      this.SearchParam[paramKey] = this.SearchParam[paramKey].trim();
	    }
	  },
		handleBefore() {
			if (!this.$refs.ref1.$refs.formRef.validate()) {
				return false
			}
			if (this.formData.prodStatus < this.selectRowData.prodStatus) {
				Tools.alert("产品状态不能逆向修改！", "danger");
				return false
			}
			if (this.formNoChangeCb()) {
				this.$refs.prodInfoOdsPopup.close();
				return false
			}
			return true
		},
		handleDetail() {
			this.dialogType = "detail";
		},
		handleEdit() {
			this.dialogType = "edit";
		},
		handleProdForm() {
			if (!this.reloading) {
				Tools.alert("正在重新生成产品端报表，稍后重试！", "danger");
				return;
			}
			this.startTime = Tools.getCurrentTime("/", ":");
			this.endTime = "";
			this.reportDate = "";
			this.resultStatus = "正在"+this.buttonName+"中";
			this.resultInfo = "";

			this.loading = false;
			this.reloading = false;
			this.setTimer();
			this.httpUtil
				.comnUpdate({
					action: "ProdInfoOds.updateTask",
					params: {},
					successAlert: true,
				})
				.then((data) => {
					this.queryRelust();
					this.loading = true;
					this.reloading = true;
					if (!data.success) {
						this.removeTimer();
					}
				});
		},
		handleUpdateProdPbc() {
			this.$refs.checkButton.setIconStyle(0)
			this.httpUtil
				.comnUpdate({
					action: "ProdInfoOds.updatePbcTask",
					params: {},
					successAlert: true,
				})
				.then((data) => {
					this.$refs.checkButton.setIconStyle(1, []);
					if (data.success == true) {

					} else if (data.success == false) {

					}
				});
        return false;
		},
		// 定时查询任务状态
		setTimer() {
			const that = this;
			if (this.timer) {
				return;
			}
			this.timer = setInterval(() => {
				that.getRunStatus();
			}, 5000);
		},
		getRunStatus() {
			this.httpUtil
				.comnQuery({
					action: "ProdInfoOds.findTask",
					params: {},
					successAlert: false ,
				})
				.then((data) => {
					if (data.returndata.loading=="false") {
						//未已完成
						this.loading = false;
						this.removeTimer();
					} else {
						this.loading = true;
						this.reloading = true;

					}
				});
		},
		removeTimer() {
			clearInterval(this.timer);
		},
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
			this.formDataCopy = Object.assign({}, row);
		},
		//检查选中数据是否满足可发送状态与中债登记编码不为空的条件
		checkBatchPublishData() {
			let pass = true;
			let prodCodes = "";
			let prodCodea = "";
			const _this = this;
			const list = _this.$kgrid.getSelected();
			if (list.length === 0) {
				Tools.alertTime("请先勾选产品信息复选框！", "danger", 5000);
				return false;
			}
			//当没有选中时不会进入
			for (let i = 0; i < list.length; i++) {
				//报备失败和成功才可以发送
				if (list[i].prodReportStatus === "0") {
					pass = false;
					if (!pass) {
						Tools.alert("产品申报状态必须为[报备成功]或[报备失败]！", "danger");
						this.$refs.prodInfoOdsGrid.setSelected([]);
						return false;
					}
				}
				if (list[i].fileStatus === "1") {
					//已发送文件的产品
					pass = false;
					prodCodes = list[i].prodCode;
					if (!pass) {
						Tools.alert("产品 [" + prodCodes + "] 已发送文件，请勿重复操作", "danger");
						this.$refs.prodInfoOdsGrid.setSelected([]);
						return false;
					}
				}
				if (list[i].prodReportStatus === "3") {
					//报备成功的产品，中债登记编码要有值
					pass = true;
					if (list[i].checkInon === "") {
						pass = false;
					}
					prodCodea = list[i].prodCode;
					if (!pass) {
						Tools.alert("产品 [" + prodCodea + "] 的中债登记编码为空，请先维护", "danger");
						this.$refs.prodInfoOdsGrid.setSelected([]);
						return false;
					}
				}
			}
		},
		//批量发送文件
		batchPublishChannel() {
			const _this = this;
			const list = _this.$kgrid.getSelected();
			this.showSubmitBtn = false;
			this.httpUtil
				.comnUpdate({
					action: "ProdInfoOds.batchSendProdFile",
					params: { list: JSON.stringify(list) },
					successAlert: true,
				})
				.then((data) => {
					this.showSubmitBtn = true;
					this.$refs.noticePublishPopup.close();
					this.$refs.prodInfoOdsGrid.load(this.queryParam);
					this.$refs.prodInfoOdsGrid.setSelected([]);
				})
				.catch(() => {
					this.showSubmitBtn = true;
					this.$refs.noticePublishPopup.close();
					this.$refs.prodInfoOdsGrid.load(this.queryParam);
					this.$refs.prodInfoOdsGrid.setSelected([]);
				});
		},
		resetCheckInon() {
			this.$set(this.formData, "checkInon", "");
		},
		queryRelust() {
			this.startTime = "";
			this.endTime = "";
			this.reportDate = "";
			this.resultStatus = "";
			this.resultInfo = "";

			this.httpUtil.comnQuery({
			action: "BaseReportReloadLog.findBaseReportReloadLogs",
			params: {
				menuId: this.menuId
			}
			}).then(data => {
			var rows = data.rows;
			if(rows.length>0) {
				this.vShow = true;
				this.startTime = rows[0].startTime;
				this.endTime = rows[0].endTime;
				this.reportDate = rows[0].reportDate;
				this.resultStatus = rows[0].resultStatus;
				this.resultInfo = rows[0].resultInfo;
			} else {
				this.vShow = false;
			}
			}).catch({});
		},
		handleClick() {
			if (this.resultInfo) {
				if (this.resultStatus.indexOf("成功") > 0) {
					Tools.alertTime(this.resultStatus, "success", 5000);
				} else if (this.resultStatus.indexOf("失败") > 0) {
					Tools.alertTime(this.resultStatus + "，具体原因如下：<br>" + this.resultInfo, "danger", 5000);
				}
			} else {
				if (this.resultStatus.indexOf("中") > 0) {
					Tools.alertTime(this.resultStatus + "，请稍后", "warning", 5000);
				}
			}
		},
	},
	beforeDestroy() {
		this.removeTimer()
	}
};
</script>
<style lang="scss" scoped>
.edit-popup {
	/deep/ {
		.k-form-body {
			max-height: none;
			.el-form-item {
				margin-bottom: 5px;
				.el-input__inner {
					height: 28px;
					line-height: 28px;
				}
			}
		}
	}
	&.type-detail {
	}
}
.btn-custom-icon {
	background: #fff !important;
	border: 1px solid #fff !important;
	color: #417fffff !important;
	box-shadow: none;
	/deep/ path {
		fill: #417fffff !important;
	}
}
/deep/ {
	.show-form {
		.el-input, .el-select {
			width: 230px;
		}
	}
}

.right {
	font-size: 12px;
  font-weight: bold;
	.detail {
		margin: 0 2px;
		font-size: 12px;
		font-weight: normal;
	}
  .detail1 {
		margin: 0 2px;
		font-size: 12px;
		font-weight: normal;
    color: #417fffff;
	}
  .btn-custom-icon {
    background: #fff !important;
    border: 1px solid #fff !important;
    color: #417fffff !important;
    box-shadow: none;
    /deep/ path {
      fill: #417fffff !important;
    }
  }
}
</style>
