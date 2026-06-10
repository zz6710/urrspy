<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="InitialSubRegist" data-target="initialSubRegistGrid" v-model = "searchParam">
        <k-form-item label="操作人员">
          <k-field-text v-model="searchParam.summitUser"/>
        </k-form-item>
        <k-form-item label="操作日期">
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="操作类型">
          <k-field-select v-model="searchParam.opType"  data-dict="op_type"/>
        </k-form-item>
        <k-form-item label="产品登记编码">
          <k-field-text v-model="searchParam.prodCode"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <k-grid ref="initialSubRegistGrid" @data-row-select="selectRow" data-operate-column="false" data-action="InitialSubRegist.findInitialSubRegists" >
        <k-grid-column data-align="left" data-header="操作人员" data-name="summitUser" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作日期" data-name="createDate"  data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作时间" data-name="createTime" data-type="time" data-width="60"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作类型" data-name="opType" data-dict="op_type"  data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品登记编码" data-name="prodCode"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构代码" data-name="bankCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="资金托管账号" data-name="fndTrstActNbr" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="资金托管账户" data-name="fndTrstAct"  data-width="230"></k-grid-column>
		<k-grid-column data-align="right" data-header="个人投资者总数" data-name="numberIndivInvest" data-width="120"></k-grid-column>
		<k-grid-column data-align="right" data-header="法人投资者总数" data-name="numberCorporInvest" data-width="120"></k-grid-column>
		<k-grid-column data-align="right" data-header="非法人投资者总数" data-name="numberUcorInvest" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="实际募集金额" data-name="actualSubscribedAmt" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="募集总份额" data-name="subscribedVol" data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="是否有其他机构代销" data-name="otherDistributAgents" data-dict="isTrue" data-width="80"></k-grid-column>
		    <k-grid-column data-align="right" data-header="代销总金额" data-name="amtOtherDbAgents" data-type="money" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="备注" data-name="details" data-width="120"></k-grid-column>
      </k-grid>
    </div>



  </div>
</template>

<script>
  export default {
    name: "initialSubRegist",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{},
        BreathDay:[]
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
      BreathDay() {

        this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
        this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
      },
    }
  };
</script>
