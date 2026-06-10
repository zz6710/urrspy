<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="UnderAssetRegist" data-label-width="130px" data-target="underAssetRegistGrid" v-model = "searchParam">
         <k-form-item label="操作日期" data-label-width="80px">
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="操作人员" data-label-width="80px">
          <k-field-text v-model="searchParam.summitUser"/>
        </k-form-item>
        <k-form-item label="操作类型" data-label-width="80px">
          <k-field-select v-model="searchParam.opType"  data-dict="op_type"/>
        </k-form-item>
        <k-form-item label="资管计划行内编码">
          <k-field-text v-model="searchParam.assetManagerCode"/>
        </k-form-item>
        <k-form-item label="底层资产行内编码">
          <k-field-text v-model="searchParam.underAssetCode" />
        </k-form-item>
        <k-form-item label="持仓日期" data-label-width="80px">
          <k-field-date v-model="ReportDate" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <k-grid ref="underAssetRegistGrid" @data-row-select="selectRow" data-operate-column="false" data-action="UnderAssetRegist.findUnderAssetRegists" >
        <k-grid-column data-align="left" data-header="操作人员" data-name="summitUser" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作类型" data-name="opType" data-dict="op_type" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作日期" data-name="createDate" data-type="date" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作时间" data-name="createTime" data-type="time" data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构代码" data-name="bankCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="对应资管及委外资产行内资产/负债编码" data-name="assetManagerCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="资管及委外资产当前总数量" data-name="assetSumNumber" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="资管及委外资产当前总折算人民币金额(元)" data-name="convertSumAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="资管及委外资产未投资头寸(元)" data-name="nonInvestedAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="底层资产行内资产负债/编码" data-name="underAssetCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="底层资产持仓数量" data-name="underAssetSum" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="底层资产折算人民币市值(元)" data-name="underConvertSumAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="持仓日期" data-name="reportDate" data-type="date" data-width="150"></k-grid-column>
      </k-grid>
    </div>


  </div>
</template>

<script>
  export default {
    name: "underAssetRegist",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{},
        BreathDay:[],
        ReportDate:[]
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      }
    },
    watch: {
      ReportDate(){
        this.$set(this.searchParam, 'reportStartDate', this.ReportDate == null ? '' : this.ReportDate[0]);
        this.$set(this.searchParam, 'reportEndDate', this.ReportDate == null ? '' : this.ReportDate[1]);
      },
      //查询起息日
      BreathDay() {
        this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
        this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
      },
    }
  };
</script>
