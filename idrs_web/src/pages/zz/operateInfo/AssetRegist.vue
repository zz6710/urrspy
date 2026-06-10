<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="AssetRegist" data-label-width="130px" data-target="assetRegistRegistGrid" v-model = "searchParam">
        <k-form-item label="操作人员">
          <k-field-text v-model="searchParam.summitUser"/>
        </k-form-item>
        <k-form-item label="操作日期">
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="操作类型">
          <k-field-select v-model="searchParam.opType"  data-dict="op_type"/>
        </k-form-item>
        <k-form-item label="行内资产/负债编码">
          <k-field-text v-model="searchParam.assetCode"/>
        </k-form-item>
        <k-form-item label="持仓日期">
          <k-field-date v-model="ReportDate" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <k-grid ref="assetRegistRegistGrid" @data-row-select="selectRow" data-operate-column="false" data-action="AssetRegist.findAssetRegists" >
        <k-grid-column data-align="left" data-header="操作用户" data-name="summitUser" data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作日期" data-name="createDate"  data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作时间" data-name="createTime" data-type="time"  data-width="80" ></k-grid-column>
        <k-grid-column data-align="left" data-header="操作类型" data-name="opType" data-dict="op_type"  data-width="80"></k-grid-column>
        <k-grid-column  data-align="left" data-header="发行机构代码" data-name="bankCode" data-width="120"></k-grid-column>
         <k-grid-column  data-align="left" data-header="产品登记编码" data-name="prodRegEnc" data-width="120"></k-grid-column>
         <k-grid-column  data-align="left" data-header="持仓类别" data-name="holdingType" data-dict ="holding_type" data-width="120"></k-grid-column>
         <k-grid-column  data-align="left" data-header="行内资产/负债编码" data-name="assetCode" data-width="120"></k-grid-column>
         <k-grid-column  data-align="left" data-header="资产穿透情况" data-name="investedAsset" data-dict ="invested_asset_type" data-width="120"></k-grid-column>
         <k-grid-column  data-align="left" data-header="中间层数" data-name="mezzanineNumber" data-width="120"></k-grid-column>
         <k-grid-column  data-align="left"  data-header="中间层行内资产/负债编码" data-name="mezzanineAssetCode" data-width="120"></k-grid-column>
         <k-grid-column  data-align="left"  data-header="会计科目名称" data-name="accountCode" data-width="120"></k-grid-column>
         <k-grid-column  data-align="right"  data-header="金额" data-name="investedAmount" data-width="120"></k-grid-column>
         <k-grid-column  data-align="right"  data-header="折算人民币金额" data-name="investedAmountCny"  data-width="120"></k-grid-column>
         <k-grid-column  data-align="right"  data-header="公允价值" data-name="fairValue"  data-width="120"></k-grid-column>
         <k-grid-column  data-align="right"  data-header="折算人民币公允价" data-name="fairValueCny"  data-width="120"></k-grid-column>
         <k-grid-column  data-align="right"  data-header="单位估值(净价)" data-name="netValuation"  data-width="120"></k-grid-column>
         <k-grid-column  data-align="right"  data-header="单位估值(全价)" data-name="flValuation"  data-width="120"></k-grid-column>
         <k-grid-column  data-align="right"  data-header="数量" data-name="quantity"  data-width="120"></k-grid-column>
         <k-grid-column  data-align="left"   data-header="币种" data-name="cny"  data-width="100"></k-grid-column>
         <k-grid-column  data-align="left"   data-header="持仓日期" data-name="holdingDate"   data-width="100"></k-grid-column>
         <k-grid-column  data-align="left"   data-header="备注" data-name="details" data-width="150"></k-grid-column>
      </k-grid>
    </div>


  </div>
</template>

<script>
  export default {
    name: "assetRegistRegist",
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
