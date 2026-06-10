<template>
  <k-form ref="addDelyForm" :data-col="2">
    <k-form-item label="产品代码">
      <k-field-text v-model="delyFormData.prodCode"  :data-disabled="true" />
    </k-form-item>
    <k-form-item label="产品id" v-show="false">
      <k-field-text v-model="delyFormData.prodId"  :data-disabled="true" />
    </k-form-item>
    <k-form-item label="产品名称">
      <k-field-text v-model="delyFormData.prodName"  :data-disabled="true" />
    </k-form-item>
    <k-form-item label="产品原到期日">
      <k-field-date v-model="delyFormData.oldEndDate"   :data-disabled="true" data-type="date" />
    </k-form-item>
    <k-form-item label="调整后到期日">
      <k-field-date v-model="delyFormData.endDate"  :data-disabled="true" data-type="date" />
    </k-form-item>
    <k-form-item label="产品调整日">
      <k-field-date v-model="delyFormData.adjustDate" :data-disabled="true" data-type="date"  />
    </k-form-item>

  </k-form>
</template>

<script>
    export default {
        name: "DisplayProdDelay",
      props:{
        delyFormData:{},
      },
    }
</script>

<style scoped>

</style>
