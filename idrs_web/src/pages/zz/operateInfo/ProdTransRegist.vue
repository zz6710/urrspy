<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-target="prodTransRegistGrid" data-label-width="140px" v-model = "searchParam">
        <k-form-item label="操作日期">
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="操作人员">
          <k-field-text v-model="searchParam.summitUser"/>
        </k-form-item>
        <k-form-item label="操作类型">
          <k-field-select v-model="searchParam.opType"  data-dict="op_type"/>
        </k-form-item>
        <k-form-item label="产品登记编码">
          <k-field-text v-model="searchParam.prodCode"/>
        </k-form-item>
        <k-form-item label="行内资产/负债编码">
          <k-field-text v-model="searchParam.assetCode" />
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <k-grid ref="prodTransRegistGrid" @data-row-select="selectRow" data-operate-column="false" data-action="ProdTransRegist.findProdTransRegists" >
        <k-grid-column data-align="left" data-header="操作人员" data-name="summitUser" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作类型" data-name="opType" data-dict="op_type"  data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作日期" data-name="createDate" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作时间" data-name="createTime" data-type="time"  data-width="80" ></k-grid-column>
        <k-grid-column data-align="left" data-header="产品登记编码" data-name="prodCode"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构代码" data-name="bankCode"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="行内资产/负债编码" data-name="assetCode"  data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="行内交易编码" data-name="transCode"  data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="资金流动类型" data-name="cashType" data-dict="subm_cash_type"  data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="资金流动类型说明" data-name="detailCashType"  data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="数量" data-name="quantity"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="资产计量方式" data-name="methodAssetMeasure" data-dict="subm_asset_measure"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="币种" data-name="cur" data-dict="subm_t8_prod_currtype_more"  data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="折算人民币金额" data-name="convertRmb" data-type="money"  data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="发生金额" data-name="amt" data-type="money"  data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="单位成交价格(净价)" data-name="unitPriceNet"  data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="单位成交价格(全价)" data-name="unitPriceFull"  data-width="120"></k-grid-column>
        <k-grid-column data-align="right" data-header="到期收益率%" data-name="rateAnnualReturn"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易对手方" data-name="tradeCounter"  data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="关联交易情况" data-name="relatedPartyTrans" data-dict="subm_related_party_trans"  data-width="200"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易对手方类型" data-name="counterType" data-dict="subm_counterparty_type"  data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="成交编号/合同号" data-name="transIdentCode"  data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易审批人身份证号" data-name="transApproveId"  data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易审批人姓名" data-name="transApproveName"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易员身份证号" data-name="traderId"  data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易员姓名" data-name="traderName"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易日" data-name="tradeDate"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易发起时间" data-name="trxTm"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="备注" data-name="details"  data-width="120"></k-grid-column>
      </k-grid>
    </div>




  </div>
</template>

<script>
  export default {
    name: "prodTransRegist",
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

      BreathDay() {
        this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
        this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
      },
    }
  };
</script>
