<template>
	<div class="py-page">
		<div>
			<k-form-search-customize v-model="searchParam" data-target="reportDataGrid" @handleReset="reportOnChangeSearch">
				<k-form-item label="报表大类">
					<k-field-select
						v-model="searchParam.reportType"
						data-dict="report_type"
						@data-on-change="reportOnChangeSearch"
					/>
				</k-form-item>
				<k-form-item label="报送报表名称">
					<k-field-select
						v-model="searchParam.reportTable"
						data-action="ReportTimeConfig.getReportTable"
						:data-params="{reportType: searchParam.reportType}"
						data-value-field="reportTable"
						data-display-field="tableName"
						:key="formKey"
						:data-allowblank="isBlank"
					/>
				</k-form-item>

				<k-form-item label="校验类型">
					<k-field-select v-model="searchParam.validateType" data-dict="rpt_validate_type" />
				</k-form-item>

				<k-form-item label="指标代码">
					<k-field-text v-model="searchParam.indexCode" />
				</k-form-item>

				<k-form-item label="校验结果">
					<k-field-select v-model="searchParam.validateResult" data-dict="rpt_validate_result" />
				</k-form-item>

				<k-form-item label="数据日期">
					<k-field-date v-model="searchParam.dealDate" data-type="date" data-date-format="yyyy-MM-dd" />
				</k-form-item>

				<k-form-item label="报送日期">
          <k-field-date v-model="searchParam.reportDate" data-type="date" data-date-format="yyyy-MM-dd" />
        </k-form-item>

				<k-form-item label="校验日期">
					<k-field-date v-model="searchParam.createDate" data-type="date" data-date-format="yyyy-MM-dd" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn
						class="btn-custom-plain"
						data-functype="POPUP"
						:data-handler="() => (this.indexCheck = {})"
						slot="button"
						data-target="reportDataCheckPopup"
						>指标校验</k-btn
					>
					<k-btn
						slot="button"
						class="btn-custom-plain"
						data-target="reportDataGrid"
						:data-export-name="'报送数据校验'"
						data-descript="数据导出"
						data-functype="EXPORT" data-export-dict="true"
						data-size="small"
						data-url="ReportValidationModel.findReportValidationResultInformation"
					>
						<md-icon>cloud_download</md-icon>
						导出
					</k-btn>
				</div>
			</div>
			<k-grid
				ref="reportDataGrid"
				@data-row-select="selectRow"
				data-operate-width="200px"
				data-action="ReportValidationModel.findReportValidationResultInformation"
				@init="
					(id) => {
						this.$kgrid = id;
					}
				"
				:data-checkbox="false"
				data-checkbox-id="id"
				:data-autoload="false"
			>
				<!--<k-grid-column data-align="left" data-header="id" data-name="id" data-hidden="true"></k-grid-column>-->
				<k-grid-column
					data-align="left"
					data-header="校验报表"
					data-name="validateTable"
					data-width="120"
				></k-grid-column>
				<k-grid-column
					data-align="left"
					data-header="校验报表"
					data-name="reportTable"
					data-hidden="true"
				></k-grid-column>
				<k-grid-column
					data-align="left"
					data-header="指标代码"
					data-name="indexCode"
					data-width="120"
				></k-grid-column>
				<k-grid-column
					data-align="left"
					data-header="指标名称"
					data-name="indexName"
					data-width="120"
				></k-grid-column>
				<k-grid-column
					data-align="left"
					data-header="数据日期"
					data-name="dealDate"
					data-width="80"
				></k-grid-column>
				<k-grid-column
					data-align="left"
					data-header="校验类型"
					data-name="validateType"
					data-dict="rpt_validate_type"
					data-width="150"
				></k-grid-column>
				<k-grid-column
					data-align="left"
					data-header="校验结果"
					data-name="validateResult"
					data-dict="rpt_validate_result"
					data-width="100"
				></k-grid-column>
				<!--<k-grid-column data-align="left" data-header="校验字段代码" data-name="columnCode" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="报表行名称" data-name="validateRow" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="报表列名称" data-name="validateColumn" data-hidden="true"></k-grid-column>-->
				<k-grid-column data-align="left" data-header="校验日志" data-name="reason"></k-grid-column>
				<k-grid-column
					data-align="left"
					data-header="校验数据所在表id"
					data-name="dataId"
					data-hidden="true"
				></k-grid-column>
				<k-grid-column
					data-align="left"
					data-header="校验日期"
					data-name="createDate"
					data-width="80"
				></k-grid-column>
				<k-grid-column
					data-align="left"
					data-header="校验时间"
					data-name="createTime"
					data-width="80"
				></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-descript="详情"
						data-functype="POPUP"
						data-size="mini"
						:data-handler="selectRow"
						data-target="reportDataValidationPopup"
					>
						详情
					</k-btn>
					<!--          <k-btn data-functype="PAGE" data-size="mini"  class="btn-custom-plain" :data-model="scope.row.row.id"-->
					<!--                 @click="popupEdit(scope.row.row)"  data-descript="跳转">-->
					<!--            跳转-->
					<!--          </k-btn>-->
				</template>
			</k-grid>
		</div>

		<!--   报送数据详情弹出框   -->
		<k-popup ref="reportDataValidationPopup" data-title="详情">
			<k-form ref="reportDataValidationForm" :data-col="2">
				<k-form-item label="id" vf-show="false">
					<k-field-display v-model="reportData.id" />
				</k-form-item>

				<k-form-item label="校验类型">
					<k-field-display v-model="reportData.validateType" data-dict="rpt_validate_type" />
				</k-form-item>

				<k-form-item label="指标代码">
					<k-field-display v-model="reportData.indexCode" />
				</k-form-item>

				<k-form-item label="指标名称">
					<k-field-display v-model="reportData.indexName" />
				</k-form-item>

				<k-form-item label="校验结果">
					<k-field-display v-model="reportData.validateResult" data-dict="rpt_validate_result" />
				</k-form-item>

				<k-form-item label="指标详述" :data-col="2" :data-row="2">
					<k-field-display v-model="reportData.indexDetail" />
				</k-form-item>

				<k-form-item label="校验日志" :data-col="2" :data-row="2">
					<k-field-display v-model="reportData.reason" />
				</k-form-item>

				<k-form-item label="校验报表">
					<k-field-display v-model="reportData.validateTable" />
				</k-form-item>

				<k-form-item label="校验字段代码" v-show="false">
					<k-field-display v-model="reportData.columnCode" />
				</k-form-item>

				<k-form-item label="报表行名称" v-show="false">
					<k-field-display v-model="reportData.validateRow" />
				</k-form-item>

				<k-form-item label="报表列名称" v-show="false">
					<k-field-display v-model="reportData.validateColumn" />
				</k-form-item>

				<k-form-item label="校验日期">
					<k-field-display v-model="reportData.createDate" />
				</k-form-item>

				<k-form-item label="校验时间">
					<k-field-display v-model="reportData.createTime" />
				</k-form-item>
			</k-form>
		</k-popup>

		<!-- 报送数据校验 -->
		<k-popup ref="reportDataCheckPopup" data-title="报送数据校验">
			<k-form ref="reportDataCheckForm" :data-col="2">
				<k-form-item label="报表大类">
					<k-field-select
						v-model="indexCheck.reportType"
						data-dict="report_type"
						@data-on-change="reportOnChange"
					/>
				</k-form-item>

				<k-form-item label="报送报表名称">
					<k-field-select
						v-model="indexCheck.reportTable"
						data-action="ReportTimeConfig.getReportTable"
						:data-params="{reportType: indexCheck.reportType}"
						data-value-field="reportTable"
						data-display-field="tableName"
						:dataAllowblank="false"
						:key="formKey1"
						:data-Allowblank="false"
					/>
				</k-form-item>

				<k-form-item label="指标类型">
					<k-field-select v-model="indexCheck.indexType" data-dict="rpt_validate_type" />
				</k-form-item>

				<k-form-item label="校验指标">
					<k-field-text v-model="indexCheck.indexCode" />
				</k-form-item>

				<k-form-item label="数据日期">
					<k-field-date v-model="indexCheck.settleDate" data-type="date" data-date-format="yyyy-MM-dd" />
				</k-form-item>

				<k-form-item label="报送日期" >
          <k-field-date v-model="indexCheck.reportDate" data-type="date" data-date-format="yyyy-MM-dd" />
          <k-tooltip data-content="报送/数据日期必填一项"></k-tooltip>
        </k-form-item>

				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						ref="checkButton"
						data-functype="SUBMIT"
						data-from="reportDataCheckPopup"
						:data-handler="checkingIndexConfig"
						:data-model="indexCheck"
						data-target="reportDataGrid"
					>
			  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
						<md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn
					>
				</k-form-footer>
			</k-form>
		</k-popup>
	</div>
</template>

<script>
import Tools from "@/utils/tools";

export default {
	name: "reportDataValRes",
	data() {
		return {
			selectRowData: {},
			reportTableShowDict: {},
			searchParam: {
				dealDate: localStorage.getItem("currentWorkday"),
				reportTable: '',
			},
			reportData: {},
			indexCheck: {},
			showSubmitBtn: true,
			formKey: 1,
			formKey1: 1,
			isBlank: true
		};
	},
	created() {
		if (this.$route.query != null) {
			let reportType = this.$route.query.reportCatgory;
			let reportTable = this.$route.query.validateTable;
			let dealDate =
				this.$route.query.dealDate === undefined
					? localStorage.getItem("currentWorkday")
					: this.$route.query.dealDate;
			let createDate = this.$route.query.createDate;
			let validateResult = this.$route.query.validateResult;
			this.$set(this.searchParam, "reportType", reportType);
			this.$set(this.searchParam, "reportTable", reportTable);
			this.$set(this.searchParam, "dealDate", dealDate);
			this.$set(this.searchParam, "createDate", createDate);
			this.$set(this.searchParam, "validateResult", validateResult);
		}
		this.$nextTick(() => {
			this.$refs.reportDataGrid.load(this.searchParam);
		});
	},
	watch: {
		"searchParam.reportDate": {
			handler(v) {
				if (v == "" || v == null) {
					this.isBlank = true;
				} else {
                    this.isBlank = false;
				}
			},
		},
	},
	methods: {
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.reportData = Object.assign({}, row);
		},
		reportOnChange() {
			this.$set(this.indexCheck, "reportTable", "");
			this.formKey1 += 1;
		},
		reportOnChangeSearch() {
			this.searchParam.reportTable = "";
			this.formKey += 1;
		},
		checkingIndexConfig() {
			if (!this.$refs.reportDataCheckForm.validate()) {
					return;
			}
			this.showSubmitBtn = false;
			this.$refs.checkButton.setIconStyle(0)
			//提交开始校验指标
			this.httpUtil
				.ajax({
					url: "/server/form/DpsApp/reportDataTask.action",
					params: this.indexCheck,
				})
				.then((data) => {
					this.showSubmitBtn = true;
					this.$refs.checkButton.setIconStyle(1, []);
					this.$refs.reportDataGrid.load(this.searchParam);
					if (data.success == true) {
						Tools.alert(data.returnmsg);
						this.$refs.reportDataCheckPopup.close();
					} else if (data.success == false) {
						// Tools.alert(data.returnmsg, "danger");
					}
				});
        return false;
		},
		popupEdit(row) {
			let pathUrl = "";

			switch (row.reportTable) {
				//人行数据采集dat文件数据校验
				case "app_pbc_report_zg01":
					pathUrl = "/main/report/rhzg/M07RHZG01";
					break;
				case "app_pbc_report_zg02":
					pathUrl = "/main/report/rhzg/M07RHZG02";
					break;
				case "app_pbc_report_zg03":
					pathUrl = "/main/report/rhzg/M07RHZG03";
					break;
				case "app_pbc_report_zg04":
					pathUrl = "/main/report/rhzg/M07RHZG04";
					break;
				case "app_pbc_report_zg05":
					pathUrl = "/main/report/rhzg/M07RHZG05";
					break;
				case "app_pbc_report_zg06":
					pathUrl = "/main/report/rhzg/M07RHZG06";
					break;
				case "app_pbc_report_zg07":
					pathUrl = "/main/report/rhzg/M07RHZG07";
					break;
				case "app_pbc_report_zg08":
					pathUrl = "/main/report/rhzg/M07RHZG08";
					break;
				case "app_pbc_report_zg09":
					pathUrl = "/main/report/rhzg/M07RHZG09";
					break;
				case "app_pbc_report_zg10":
					pathUrl = "/main/report/rhzg/M07RHZG10";
					break;
				case "app_pbc_report_zg11":
					pathUrl = "/main/report/rhzg/M07RHZG11";
					break;
				//中债一二三期直联数据校验
				case "app_prod_regist_filing_info":
					pathUrl = "/main/zz/manage/ProdRegistFilingInfo";
					break;
				case "app_prod_issuance_regist_info":
					pathUrl = "/main/zz/manage/ProdIssuanceRegistInfo";
					break;
				case "app_initial_sub_regist_info":
					pathUrl = "/main/zz/manage/InitialSubRegistInfo";
					break;
				case "app_subseq_subscr_regist_info":
					pathUrl = "/main/zz/manage/SubseqSubscrRegistInfo";
					break;
				case "app_termination_regist_info":
					pathUrl = "/main/zz/manage/TrTerminationRegistInfo";
					break;
				case "app_practy_regist_info":
					pathUrl = "/main/zz/manage/TrPractyRegistInfo";
					break;
				case "app_asset_debt_register_info":
					pathUrl = "/main/zz/manage/AssetDebtRegisterInfo";
					break;
				case "app_prod_trans_regist_info":
					pathUrl = "/main/zz/manage/ProdTransRegistInfo";
					break;
				case "app_appraise_regist_info":
					pathUrl = "/main/zz/manage/AppraiseRegistInfo";
					break;
				case "app_cust_register_info":
					pathUrl = "/main/zz/manage/TrCustRegisterInfo";
					break;
				case "app_cust_vol_register_info":
					pathUrl = "/main/zz/manage/TrCustVolRegisterInfo";
					break;
				case "app_cust_trans_info":
					pathUrl = "/main/zz/manage/TrCustTransInfo";
					break;

				default:
					break;
			}

			//中债一二三期直联数据校验

			if (pathUrl == "") {
				Tools.alert("无法从参数获取需要跳转页面", "danger");
			}
			localStorage.setItem("directedData", JSON.stringify(row));
			this.$router.push({
				path: pathUrl,
				query: { directedData: row },
			});
		},
	},
};
</script>
