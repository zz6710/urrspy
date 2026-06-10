<template>
  <div class="py-page">
     <div>
         <k-form-search-customize data-model-name="TerminationRegistInfoh" data-target="TerminationRegistInfohGrid"  v-model = "searchParam">
           <k-form-item label="产品登记编码">
             <k-field-text v-model="searchParam.prodCode"/>
           </k-form-item>
           <k-form-item label="产品实际终止日期">
             <k-field-date v-model="ActualProdTerDate" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
           </k-form-item>
           <k-form-item label="登记日期">
             <k-field-date v-model="RegisterDate" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
           </k-form-item>
         </k-form-search-customize>
     </div>
   <div class="py-page-container">
      <k-grid ref="TerminationRegistInfohGrid" @data-row-select="selectRow" data-operate-column="false" data-action="TerminationRegistInfoh.findTerminationRegistInfos" >
		        <k-grid-column data-align="left" data-header="报送状态" data-name="registerStatus"  data-dict="subm_report_status" data-export="false"  data-width="120"></k-grid-column>
            <k-grid-column data-align="left" data-header="产品登记编码" data-name="prodCode"  data-width="150"></k-grid-column>
            <k-grid-column data-align="left" data-header="发行机构代码" data-name="bankCode"  data-width="120"></k-grid-column>
            <k-grid-column data-align="left" data-header="理财产品实际终止日期" data-name="actualProdTerDate"   data-width="150"></k-grid-column>
            <k-grid-column data-align="right" data-header="银行实际实现收入（元）" data-name="realizedBankIncome"  data-width="170"></k-grid-column>
            <k-grid-column data-align="right" data-header="兑付客户收益（元）" data-name="interestPayment"  data-width="150"></k-grid-column>
            <k-grid-column data-align="right" data-header="兑付客户总金额（元）" data-name="payment"  data-width="150"></k-grid-column>
            <k-grid-column data-align="right" data-header="兑付总份额" data-name="deliveredVol"  data-width="150"></k-grid-column>
            <k-grid-column data-align="right" data-header="本机构托管费（元）" data-name="inCustodianFee"  data-width="150"></k-grid-column>
            <k-grid-column data-align="right" data-header="本机构管理费（元）" data-name="inManageFee"  data-width="150"></k-grid-column>
            <k-grid-column data-align="right" data-header="本机构销售手续费（元）" data-name="inSalesCommision"  data-width="170"></k-grid-column>
            <k-grid-column data-align="right" data-header="本机构其他产品费用（元）" data-name="inOtherProdFee"  data-width="170"></k-grid-column>
            <k-grid-column data-align="right" data-header="其他机构托管费（元）" data-name="otherCustodianFee"  data-width="150"></k-grid-column>
            <k-grid-column data-align="right" data-header="其他机构管理费（元）" data-name="otherManageFee"  data-width="150"></k-grid-column>
            <k-grid-column data-align="right" data-header="其他机构销售手续费（元）" data-name="otherSalesComm"  data-width="170"></k-grid-column>
            <k-grid-column data-align="right" data-header="投资顾问费用（元）" data-name="consultFee"  data-width="150"></k-grid-column>
            <k-grid-column data-align="right" data-header="其他机构其他产品费用（元）" data-name="otherProdFee"  data-width="180"></k-grid-column>
            <k-grid-column data-align="right" data-header="客户实际年化收益率%" data-name="annualReturnClient"  data-width="150"></k-grid-column>
            <k-grid-column data-align="right" data-header="产品实际年化收益率%" data-name="annualReturnProd"  data-width="150"></k-grid-column>
            <k-grid-column data-align="left" data-header="登记流水号" data-name="registerSerno" data-export="false"  data-width="250"></k-grid-column>
            <k-grid-column data-align="left" data-header="登记日期" data-name="registerDate"  data-export="false"  data-width="120"></k-grid-column>
            <k-grid-column data-align="left" data-header="新增日期" data-name="createDate"  data-export="false"  data-width="120"></k-grid-column>
      </k-grid>
    </div>

  </div>
</template>

<script>
  export default {
    name: "TerminationRegistInfoh",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{},
        ActualProdTerDate:[],
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
     ActualProdTerDate() {
        this.$set(this.searchParam, 'actProdTerStartDate', this.ActualProdTerDate == null ? '' : this.ActualProdTerDate[0]);
        this.$set(this.searchParam, 'actProdTerEndDate', this.ActualProdTerDate == null ? '' : this.ActualProdTerDate[1]);
      },
    }
  };
</script>
