<template>
  <div>
    <k-form ref="addT8ProdSalesInfoDisForm" :data-col="2">
      <k-form-item label="产品流水事件">
        <k-field-select v-model="fromDataDistributor.prodDealType" :data-allowblank="false" data-dict="prod_deal_type"/>
      </k-form-item>
      <k-form-item label="产品代码">
        <k-field-select v-model="fromDataDistributor.prodCode" :data-allowblank="false"
                        data-action="T8ProdInfo.findT8ProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"
                        :data-params="{isShareSort:0}"/>
      </k-form-item>
<!--      <k-form-item label="子产品代码">
        <k-field-select v-model="fromDataDistributor.prodCodeSub"/>
      </k-form-item>-->
      <k-form-item label="销售商代码">
        <k-field-select v-model="fromDataDistributor.distributorCode" :data-allowblank="false"
                      data-display-field="distributorCode,distributorName"
                      data-value-field="distributorCode"  data-action="T8Dict.findTaProdDistributorInfos"
                      :data-params="{'prodCode':fromDataDistributor.prodCode}"/>
      </k-form-item>

      <k-form-item label="发生日期">
        <k-field-date v-model="fromDataDistributor.changeDate"  :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="认申购份额">
        <k-field-text v-model="fromDataDistributor.subsVol" data-type="money" data-digits="4"
                       data-validate-type="money" data-integer-length="16" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="认申购金额">
        <k-field-text v-model="fromDataDistributor.subsAmt" data-type="money" data-digits="2"
                      data-validate-type="money" data-default-value="" data-integer-length="16"/>
      </k-form-item>
      <k-form-item label="赎回份额">
        <k-field-text v-model="fromDataDistributor.redeemVol" data-type="money" data-digits="4" data-show-gbmoney="true"
                      data-validate-type="money" data-default-value="" data-integer-length="16"/>
      </k-form-item>
      <k-form-item label="赎回金额">
        <k-field-text v-model="fromDataDistributor.redeemAmt" data-type="money" data-digits="2"
                      data-validate-type="money" data-default-value="" data-integer-length="16"/>
      </k-form-item>
      <k-form-item label="费用类型">
        <k-field-select v-model="fromDataDistributor.feeType" data-dict="t8_fee_type_deal"/>
      </k-form-item>
      <k-form-item label="费用金额">
        <k-field-text v-model="fromDataDistributor.feeMoney" data-type="money" data-digits="2"
                      data-validate-type="money" data-default-value="" data-integer-length="16"/>
      </k-form-item>
<!--      <k-form-item label="是否有其他机构代销">
        <k-field-select v-model="fromDataDistributor.saleByProxy" data-dict="t8_prod_isok"/>
      </k-form-item>
      <k-form-item label="代销总额">
        <k-field-text v-model="fromDataDistributor.proxySumMoney" data-type="money" data-digits="2"
                      data-validate-type="money" data-default-value=" " data-integer-length="16"/>
      </k-form-item>-->
      <k-form-item label="币种">
        <k-field-select v-model="fromDataDistributor.buyCcy" data-dict="t8_prod_currtype_more" :data-default-value="'CNY'"
                      :data-allowblank="false" />
      </k-form-item>
      <k-form-item label="备注" data-input-width="590px">
        <k-field-text v-model="fromDataDistributor.remark" :data-max-length="2000" inputType="textarea" :rows="1"/>
      </k-form-item>

    </k-form>
  </div>
</template>

<script>
  export default {
    props: {
      openType:{
        type: String,
        default: ''
      },
      fromDataDistributor:{},

    },
    data() {
      return {


      };
    },

    created() {
      if(this.openType=='add'){
        this.fromDataDistributor.subsVol="";
        this.fromDataDistributor.subsAmt="";
        this.fromDataDistributor.redeemVol="";
        this.fromDataDistributor.redeemAmt="";
        this.fromDataDistributor.feeType="";
        this.fromDataDistributor.feeMoney="";
        this.fromDataDistributor.remark="";
        this.fromDataDistributor.buyCcy="";
      }
    },

    methods: {
      validateData() {
        return this.$refs.addT8ProdSalesInfoDisForm.validate();
      },
    }
  };
</script>

<style scoped>

</style>
