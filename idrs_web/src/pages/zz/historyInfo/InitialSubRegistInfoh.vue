<template>
  <div class="py-page">
     <div>
        <k-form-search-customize data-model-name="InitialSubRegistInfoh" data-target="InitialSubRegistInfohGrid"  v-model = "searchParam">
              <k-form-item label="产品登记编码">
                <k-field-text v-model="searchParam.prodCode"/>
              </k-form-item>
              <k-form-item label="业务登记日期">
                <k-field-date v-model="RegisterDate" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
              </k-form-item>
            </k-form-search-customize>
     </div>

    <div class="py-page-container">
      <k-grid ref="InitialSubRegistInfohGrid" @data-row-select="selectRow"  data-operate-column="false" data-action="InitialSubRegistInfoh.findInitialSubRegistInfos" >
	        <k-grid-column data-align="left" data-header="报送状态" data-name="registerStatus" data-dict="report_status" data-width="100" data-export="false"></k-grid-column>
          <k-grid-column data-align="left" data-header="产品登记编码" data-name="prodCode" data-width="120"></k-grid-column>
          <k-grid-column data-align="left" data-header="发行机构代码" data-name="bankCode" data-width="100"></k-grid-column>
          <k-grid-column data-align="left" data-header="资金托管账号" data-name="fndTrstActNbr" data-width="150"></k-grid-column>
          <k-grid-column data-align="left" data-header="资金托管账户" data-name="fndTrstAct" data-width="300"></k-grid-column>
          <k-grid-column data-align="left" data-header="id" data-name="id" :data-hidden="true" data-export="false"></k-grid-column>
          <k-grid-column data-align="right" data-header="个人投资者总数" data-name="numberIndivInvest" data-width="120"></k-grid-column>
          <k-grid-column data-align="right" data-header="法人投资者总数" data-name="numberCorporInvest" data-width="120"></k-grid-column>
          <k-grid-column data-align="right" data-header="非法人投资者总数" data-name="numberUcorInvest" data-width="120"></k-grid-column>
          <k-grid-column data-align="right" data-header="实际募集金额(元)" data-name="actualSubscribedAmt" data-width="130"></k-grid-column>
          <k-grid-column data-align="right" data-header="募集总份额" data-name="subscribedVol" data-width="130"></k-grid-column>
          <k-grid-column data-align="left" data-header="是否有其他机构代销" data-name="otherDistributAgents" data-dict="subm_isTrue" data-width="100"></k-grid-column>
          <k-grid-column data-align="right" data-header="代销总金额" data-name="amtOtherDbAgents" data-width="100"></k-grid-column>
          <k-grid-column data-align="right" data-header="产品销售区域及募集金额" data-name="zonClcAmt" data-width="300"></k-grid-column>
          <k-grid-column data-align="right" data-header="认购币种" data-name="prodCcy" data-dict="tr_cur"  data-width="100"></k-grid-column>
          <k-grid-column data-align="left" data-header="备注" data-name="details" data-width="150"></k-grid-column>
          <k-grid-column data-align="left" data-header="登记流水号" data-name="registerSerno" data-width="250"  data-export="false"></k-grid-column>
          <k-grid-column data-align="left" data-header="登记日期" data-name="registerDate" data-width="100"   data-export="false"></k-grid-column>
           <k-grid-column data-align="left" data-header="新增日期" data-name="createDate" data-width="100"  data-export="false"></k-grid-column>
      </k-grid>
    </div>


  </div>
</template>

<script>
  export default {
    name: "InitialSubRegistInfoh",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{},
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
    }
  };
</script>
