<template>
  <k-form ref="addLiquidationForm" :data-col="2">
    <k-form-item label="产品代码">
      <k-field-text v-model="formData.prodCode"  :data-disabled="true" />
    </k-form-item>

    <k-grid-column data-hidden="true" v-model="formData.prodId" :data-disabled="true"/>

    <k-form-item label="产品名称">
      <k-field-text v-model="formData.prodName" :data-disabled="true" />
    </k-form-item>
    <k-form-item label="产品清盘日">
      <k-field-date v-model="formData.liquidate" :dataAllowblank="false" data-type="date" :data-disabled="true" />
    </k-form-item>
  </k-form>
</template>

<script>
    export default {
        name: "DisplayProdLiquidation",
      props:{
        formData:{},
      },
    }
</script>

<style scoped>

</style>
