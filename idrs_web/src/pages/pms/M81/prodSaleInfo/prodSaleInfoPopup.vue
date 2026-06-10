<template>
  <div>
    <k-form ref="addT8ProdSalesInfoForm" :data-col="2" dataLabelWidth="150px" dataInputWidth="200px">
      <k-form-item label="产品流水事件">
        <k-field-select v-model="formData.prodDealType" :data-allowblank="false" data-dict="prod_deal_type"/>
      </k-form-item>
      <k-form-item label="产品代码">
        <k-field-select v-model="formData.prodCode" :data-allowblank="false" data-action="T8ProdInfo.findT8ProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"
                        :data-params="{isShareSort:0}"/>
      </k-form-item>
<!--      <k-form-item label="子产品代码">
        <k-field-select v-model="formData.prodCodeSub" />
      </k-form-item>-->
      <k-form-item label="发生日期">
        <k-field-date v-model="formData.changeDate"  :data-allowblank="false" />
      </k-form-item>
      <k-form-item label="认申购份额">
        <k-field-text v-model="formData.subsVol" data-digits="4"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true" />
      </k-form-item>
      <k-form-item label="认申购金额">
        <k-field-text v-model="formData.subsAmt" data-type="money" data-digits="2"
                      data-validate-type="money" data-integer-length="16"/>
      </k-form-item>
      <k-form-item label="赎回份额">
        <k-field-text v-model="formData.redeemVol" data-type="money" data-digits="4"
                      data-validate-type="money" data-integer-length="16" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="赎回金额">
        <k-field-text v-model="formData.redeemAmt" data-type="money" data-digits="2"
                      data-validate-type="money" data-integer-length="16"/>
      </k-form-item>
      <k-form-item label="费用类型">
        <k-field-select v-model="formData.feeType"  data-dict="t8_fee_type_deal"/>
      </k-form-item>
      <k-form-item label="费用金额">
        <k-field-text v-model="formData.feeMoney" data-type="money" data-digits="2"
                      data-validate-type="money" data-integer-length="16"/>
      </k-form-item>
      <k-form-item label="是否有其他机构代销">
        <k-field-select v-model="formData.saleByProxy"  data-dict="t8_prod_isok"/>
      </k-form-item>
      <k-form-item label="代销总额">
        <k-field-text v-model="formData.proxySumMoney" data-type="money" data-digits="2"
                      data-validate-type="money" data-integer-length="16"/>
      </k-form-item>
      <k-form-item label="币种">
        <k-field-select v-model="formData.buyCcy" data-dict="t8_prod_currtype_more" :data-default-value="'CNY'"/>
      </k-form-item>
      <k-form-item label="备注" data-input-width="590px">
        <k-field-text v-model="formData.remark" :data-max-length="2000" inputType="textarea" :rows="1"/>
      </k-form-item>
    </k-form>
  </div>
</template>

<script>
  export default {
    props: {
      formData:{},
    },
    data() {
      return {


      };
    },

    created() {

    },

    methods: {
      validateData() {
        return this.$refs.addT8ProdSalesInfoForm.validate();
      },
    }
  };
</script>

<style scoped>

</style>
