<template>
	<div class="py-page">
		<div>
			<k-form-search-customize data-model-name="InvestorSubHoldMark" data-label-width="100px" v-model="searchParam" data-target="InvestorSubHoldMarkGrid">
				<k-form-item label="操作日期">
					<k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd" />
				</k-form-item>
				<k-form-item label="操作人员">
					<k-field-text v-model="searchParam.summitUser" />
				</k-form-item>
				<k-form-item label="操作类型">
					<k-field-select v-model="searchParam.opType" data-dict="op_type" />
				</k-form-item>
				<k-form-item label="持有日期">
					<k-field-date v-model="HoldDate" data-type="daterange" data-date-format="yyyyMMdd" />
				</k-form-item>
				<k-form-item label="产品登记编码">
					<k-field-text v-model="searchParam.prodCode" />
				</k-form-item>
				<k-form-item label="子产品代码">
					<k-field-text v-model="searchParam.prodCodeS" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<k-grid ref="InvestorSubHoldMarkGrid" @data-row-select="selectRow" data-operate-column="false" data-action="InvestorSubHoldMark.findInvestorSubHoldMarks">
				<k-grid-column data-header="操作用户" data-name="opUser"></k-grid-column>
				<k-grid-column data-header="操作日期" data-name="opDate" data-width="120"></k-grid-column>
				<k-grid-column data-header="操作时间" data-name="opTime" data-type="time" data-width="120"></k-grid-column>
				<k-grid-column data-header="操作类型" data-name="opType" data-dict="op_type"></k-grid-column>
				<k-grid-column data-header="登记机构代码" data-name="bankCode" data-width="120"></k-grid-column>
				<k-grid-column data-header="子产品代码" data-name="prodCodeS" data-width="120"></k-grid-column>
				<k-grid-column data-header="产品代码" data-name="prodCodeM" data-width="120"></k-grid-column>
				<k-grid-column data-header="产品登记编码" data-name="prodCode" data-width="120"></k-grid-column>
				<k-grid-column data-header="识别标识" data-name="custNo" data-width="120"></k-grid-column>
				<k-grid-column data-header="持有日期" data-name="holdDate" data-width="120"></k-grid-column>
				<k-grid-column data-header="币种" data-name="cur" data-width="80"></k-grid-column>
				<k-grid-column data-align="right" data-header="持有份额" data-name="holdVol" data-width="120"></k-grid-column>
				<k-grid-column data-align="right" data-header="持有金额" data-name="holdAmt" data-width="120"></k-grid-column>
				<k-grid-column data-align="right" data-header="折算人民币金额(元)" data-name="convertRmb" data-width="160"></k-grid-column>
				<k-grid-column data-header="TA_ID" data-name="taId" data-width="120"></k-grid-column>
				<k-grid-column data-header="渠道号" data-name="channelCode" data-width="120"></k-grid-column>
				<k-grid-column data-header="投资者类别" data-name="custType" data-dict="tr_cust_type" data-width="120"></k-grid-column>
				<k-grid-column data-header="个人证件类别" data-name="personalIdType" data-dict="tr_personal_id_type" data-width="100"></k-grid-column>
				<k-grid-column data-header="机构证件类别" data-name="organizationIdType" data-dict="tr_organization_id_type" data-width="100"></k-grid-column>
				<k-grid-column data-header="其他证件名称" data-name="otherIdName" data-width="100"></k-grid-column>
				<k-grid-column data-header="证件号码" data-name="idCode" data-width="100"></k-grid-column>
				<k-grid-column data-header="业务登记日期" data-name="registerDate" data-width="100" data-hidden="true" data-export="false"></k-grid-column>
				<k-grid-column data-header="报表日期" data-name="reportDate" data-width="100" data-hidden="true" data-export="false"></k-grid-column>
				<k-grid-column data-header="登记状态" data-name="registerStatus" data-width="100" data-hidden="true" data-export="false"></k-grid-column>
				<k-grid-column data-header="登记流水号" data-name="registerSerno" data-width="100" data-hidden="true" data-export="false"></k-grid-column>
				<k-grid-column data-header="理论报送起始日期" data-name="theoryReportStartDate" data-width="100" data-hidden="true" data-export="false"></k-grid-column>
				<k-grid-column data-header="理论报送截止日期" data-name="theoryReportEndDate" data-width="100" data-hidden="true" data-export="false"></k-grid-column>
				<k-grid-column data-header="新增日期" data-name="createDate" data-width="100" data-hidden="true" data-export="false"></k-grid-column>
			</k-grid>
		</div>
	</div>
</template>

<script>
export default {
	data() {
		return {
			formData: {},
			selectRowData: {},
			searchParam: {},
			BreathDay: [],
			HoldDate: [],
		};
	},
	watch: {
		// 查询起息日
		BreathDay() {
			this.$set(this.searchParam, "startDate", this.BreathDay == null ? "" : this.BreathDay[0]);
			this.$set(this.searchParam, "endDate", this.BreathDay == null ? "" : this.BreathDay[1]);
		},
		HoldDate() {
			this.$set(this.searchParam, "holdStartDate", this.HoldDate == null ? "" : this.HoldDate[0]);
			this.$set(this.searchParam, "holdEndDate", this.HoldDate == null ? "" : this.HoldDate[1]);
		},
	},
	methods: {
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},
	},
};
</script>
