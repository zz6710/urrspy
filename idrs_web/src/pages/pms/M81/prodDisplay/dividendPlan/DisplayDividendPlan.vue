<template>
  <k-form ref="ApproveForm" :data-col="2">
    <k-form-item label="产品代码">
      <k-field-text v-model="formData.prodCode" :data-disabled="true"  />
    </k-form-item>
    <k-form-item label="产品名称">
      <k-field-text v-model="formData.prodName" :data-disabled="true" />
    </k-form-item>
    <k-form-item label="分红方式">
      <k-field-select v-model="formData.dividendType" data-dict="t8_bonus_type" :data-disabled="true" />
    </k-form-item>
    <k-form-item label="当前份额">
      <k-field-text v-model="formData.share"  data-type="money" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="分红模式">
      <k-field-select v-model="formData.dividendMode" data-dict="t8_dividend_mode" :data-disabled="true" />
    </k-form-item>
    <k-form-item label="分红金额">
      <k-field-text v-model="formData.amount"  data-type="money" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="分红基准日">
      <k-field-date v-model="formData.dividendBaseDate" data-type="date" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="分红除权日">
      <k-field-date v-model="formData.dividendExDate" data-type="date" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="分红权益登记日">
      <k-field-date v-model="formData.dividendRegisterDate" data-type="date" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="红利发放/再投日">
      <k-field-date v-model="formData.handOutDate" data-type="date" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="分红清算天数">
      <k-field-text v-model="formData.liquidationDays" :data-disabled="true"  />
    </k-form-item>
    <k-form-item label="备注">
      <k-field-text v-model="formData.remarks" :data-disabled="true"  />
    </k-form-item>
    <k-form-item label="可分配利润">
      <k-field-text v-model="formData.profit" data-validate-type="money" data-type="money"  :data-disabled="true"
                       data-min-value="(0" :data-max-length="18" data-digits="2"/>
    </k-form-item>
  </k-form>
</template>

<script>
    export default {
        name: "DisplayDividendPlan",
      props:{
        formData:{},
      },
    }
</script>

<style scoped>

</style>
