<template>
  <div class="py-page">
       <div>
         <k-form-search-customize data-model-name="UnderAssetRegistInfoh" data-target="UnderAssetRegistInfohGrid" data-label-width="150px" v-model = "searchParam">
           <k-form-item label="登记日期" data-label-width="80px">
             <k-field-date v-model="RegisterDate" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
           </k-form-item>
            <k-form-item class="lh16" label="对应资管及委外资产行内资产/负债编码">
             <k-field-text v-model="searchParam.assetManagerCode"/>
            </k-form-item>
           <k-form-item class="lh16" label="底层资产行内资产负债/编码">
             <k-field-text v-model="searchParam.underAssetCode"/>
           </k-form-item>
           <k-form-item label="持仓日期" data-label-width="80px">
             <k-field-date v-model="ReportDate" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
           </k-form-item>
         </k-form-search-customize>
       </div>
    <div class="py-page-container">
      <k-grid ref="UnderAssetRegistInfohGrid" @data-row-select="selectRow" data-operate-column="false" data-action="UnderAssetRegistInfoh.findUnderAssetRegistInfos" >
		<k-grid-column data-align="left" data-header="报送状态" data-name="registerStatus" data-dict="subm_report_status"  data-export="false"  data-width="120"></k-grid-column>
            <k-grid-column data-align="left" data-header="发行机构编码" data-name="id" :data-hidden="true"  data-export="false" data-width="150"></k-grid-column>
            <k-grid-column data-align="left" data-header="发行机构代码" data-name="bankCode" data-width="120"></k-grid-column>
            <k-grid-column data-align="left" data-header="对应资管及委外资产行内资产/负债编码" data-name="assetManagerCode" data-width="150"></k-grid-column>
            <k-grid-column data-align="right" data-header="资管及委外资产当前总数量" data-name="assetSumNumber" data-width="150"></k-grid-column>
            <k-grid-column data-align="right" data-header="资管及委外资产当前总折算人民币金额(元)" data-name="convertSumAmt" data-width="150"></k-grid-column>
            <k-grid-column data-align="right" data-header="资管及委外资产未投资头寸(元)" data-name="nonInvestedAmt" data-width="150"></k-grid-column>
            <k-grid-column data-align="left" data-header="底层资产行内资产负债/编码" data-name="underAssetCode" data-width="150"></k-grid-column>
            <k-grid-column data-align="right" data-header="底层资产持仓数量" data-name="underAssetSum" data-width="150"></k-grid-column>
            <k-grid-column data-align="right" data-header="底层资产折算人民币市值(元)" data-name="underConvertSumAmt" data-width="150"></k-grid-column>
            <k-grid-column data-align="left" data-header="持仓日期" data-name="reportDate" data-type="date" data-width="150"></k-grid-column>
            <k-grid-column data-align="left" data-header="登记流水号" data-name="registerSerno"  data-export="false" data-width="250"></k-grid-column>
            <k-grid-column data-align="left" data-header="登记日期" data-name="registerDate"  data-type="date"  data-export="false" data-width="150"></k-grid-column>
            <k-grid-column data-align="left" data-header="新增日期" data-name="createDate" data-type="date"  data-export="false" data-width="150"></k-grid-column>
      </k-grid>
    </div>

  </div>
</template>

<script>
  export default {
    name: "UnderAssetRegistInfoh",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{},
        ReportDate:[],
        RegisterDate:[]
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      }
    },
    watch: {
      //查询起息日
      RegisterDate() {
        this.$set(this.searchParam, 'startDate', this.RegisterDate == null ? '' : this.RegisterDate[0]);
        this.$set(this.searchParam, 'endDate', this.RegisterDate == null ? '' : this.RegisterDate[1]);
     },
      ReportDate() {
        this.$set(this.searchParam, 'reportStartDate', this.ReportDate == null ? '' : this.ReportDate[0]);
        this.$set(this.searchParam, 'reportEndDate', this.ReportDate == null ? '' : this.ReportDate[1]);
      },
    }
  };
</script>


