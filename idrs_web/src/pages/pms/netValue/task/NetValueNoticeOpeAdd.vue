<template>
  <div>
    <k-form :data-col="2">
      <k-form-item label="产品代码 ">
        <k-field-select v-model="formData.prodCode" :dataAllowblank="false"
                        data-action="T8Dict.findTaProdInfos" data-display-field="prodCode,prodName"
                        data-value-field="prodCode"/>
      </k-form-item>
      <k-form-item label="净值日期">
        <k-field-date v-model="formData.netvalDate" :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="产品总净值">
        <k-field-text v-model="formData.totalNet" data-digits="2" data-integer-length="14"
                      data-validate-type="number"/>
      </k-form-item>
      <k-form-item label="产品总份额">
        <k-field-text v-model="formData.totalVol" data-digits="2" data-integer-length="14"
                      data-validate-type="number"/>
      </k-form-item>
      <k-form-item label="产品单位净值">
        <k-field-text v-model="formData.nav" data-digits="6" data-integer-length="10"
                      data-validate-type="number"/>
      </k-form-item>
      <k-form-item label="当日收益">
        <k-field-text v-model="formData.navProfit" data-digits="2" data-integer-length="14"
                      data-validate-type="number"/>
      </k-form-item>
      <k-form-item label="单位万份收益">
        <k-field-text v-model="formData.tenThousandIncomeAmt" data-digits="4" data-integer-length="5"
                      data-validate-type="number"/>
      </k-form-item>
      <k-form-item label="产品累计净值">
        <k-field-text v-model="formData.totalNav" data-digits="4" data-integer-length="12"
                      data-validate-type="number"/>
      </k-form-item>
      <k-form-item label="近七日年化收益率">
        <k-field-text v-model="formData.sevenDaysIncomeRate" data-digits="4" data-integer-length="5"
                      data-validate-type="number"/>
      </k-form-item>
    </k-form>
  </div>
</template>

<script>
export default {
  name: "NetValueNoticeOpe.vue",
  props: {
    formData: {},
  },
}
</script>

<style scoped>

</style>
