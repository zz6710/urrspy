<template>
  <div class="py-page">
    <div>
        <k-form-search-customize data-model-name="SubseqSubscrRegistInfoh" data-target="SubseqSubscrRegistInfohGrid" v-model = "searchParam">
           <k-form-item label="产品登记编码">
             <k-field-text v-model="searchParam.prodCode"/>
            </k-form-item>
            <k-form-item label="业务结束日">
              <k-field-date v-model="BusinessEndDate" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
            </k-form-item>
            <k-form-item label="业务登记日期">
              <k-field-date v-model="RegisterDate" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
            </k-form-item>
        </k-form-search-customize>
     </div>
    <div class="py-page-container">
      <k-grid ref="SubseqSubscrRegistInfohGrid" @data-row-select="selectRow" data-operate-column="false" data-action="SubseqSubscrRegistInfoh.findSubseqSubscrRegistInfos" >
		        <k-grid-column data-align="left" data-header="报送状态" data-name="registerStatus" data-dict="subm_report_status"  data-width="100" data-export="false"></k-grid-column>
            <k-grid-column data-align="left" data-header="产品登记编码" data-name="prodCode" data-width="130"></k-grid-column>
            <k-grid-column data-align="left" data-header="发行机构代码" data-name="bankCode" data-width="100"></k-grid-column>
            <k-grid-column data-align="right" data-header="初始净值" data-name="initialNav" data-width="100"></k-grid-column>
            <k-grid-column data-align="right" data-header="产品净值" data-name="nav" data-width="100"></k-grid-column>
            <k-grid-column data-align="right" data-header="累计净值" data-name="aggregateNav" data-width="100"></k-grid-column>
            <k-grid-column data-align="right" data-header="净值币种" data-name="navCur" data-dict="tr_cur" data-width="100"></k-grid-column>
            <k-grid-column data-align="right" data-header="折算人民币初始净值" data-name="convertInitialNav" data-width="100"></k-grid-column>
            <k-grid-column data-align="right" data-header="折算人民币净值" data-name="convertRmbNav" data-width="120"></k-grid-column>
            <k-grid-column data-align="right" data-header="折算人民币累计净值" data-name="convertRmbAggNav" data-width="120"></k-grid-column>
            <k-grid-column data-align="right" data-header="净值日期" data-name="navDt" data-width="120"></k-grid-column>
            <k-grid-column data-align="right" data-header="实现收益率%" data-name="realizedAnnualReturn" data-width="120"></k-grid-column>
            <k-grid-column data-align="right" data-header="最新预期收益率%" data-name="expectedAnnualReturn" data-width="120"></k-grid-column>
            <k-grid-column data-align="right" data-header="银行实现收益(元)" data-name="inconmeBank" data-width="120"></k-grid-column>
            <k-grid-column data-align="left" data-header="业务起始日" data-name="businessStartDate"   data-width="100"></k-grid-column>
            <k-grid-column data-align="left" data-header="业务结束日" data-name="businessEndDate"   data-width="100"></k-grid-column>
            <k-grid-column data-align="right" data-header="累计申购份额" data-name="subscribedLatestVol" data-width="120"></k-grid-column>
            <k-grid-column data-align="right" data-header="累计赎回份额" data-name="redeemedLatestVol" data-width="120"></k-grid-column>
            <k-grid-column data-align="right" data-header="每万份份额分红" data-name="unitsBonus" data-width="120"></k-grid-column>
            <k-grid-column data-align="right" data-header="每万份现金分红" data-name="cashBonus" data-width="120"></k-grid-column>
            <k-grid-column data-align="right" data-header="产品余额(元)" data-name="prodAmt" data-width="150"></k-grid-column>
            <k-grid-column data-align="right" data-header="产品份额" data-name="prodVol" data-width="150"></k-grid-column>
            <k-grid-column data-align="left" data-header="币种和申购兑付信息" data-name="ccyAndPchRdm" data-width="250"></k-grid-column>
            <k-grid-column data-align="left" data-header="备注" data-name="details" data-width="150"></k-grid-column>
            <k-grid-column data-align="left" data-header="登记流水号" data-name="registerSerno" data-width="250" data-export="false"></k-grid-column>
            <k-grid-column data-align="left" data-header="登记日期" data-name="registerDate" data-width="100"   data-export="false"></k-grid-column>
            <k-grid-column data-align="left" data-header="新增日期" data-name="createDate"  data-width="100" data-export="false"></k-grid-column>
        </k-grid>
    </div>


  </div>
</template>

<script>
  export default {
    name: "SubseqSubscrRegistInfoh",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{},
        BusinessEndDate:[],
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
      BusinessEndDate() {
        this.$set(this.searchParam, 'busStartDate', this.BusinessEndDate == null ? '' : this.BusinessEndDate[0]);
        this.$set(this.searchParam, 'busEndDate', this.BusinessEndDate == null ? '' : this.BusinessEndDate[1]);
      },
    }
  };
</script>
