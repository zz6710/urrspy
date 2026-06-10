<template>
  <k-form ref="riskScoreForm" data-ui="element">
    <k-form-item label="产品代码">
      <k-field-text v-model="formData.prodCode" :data-allowblank="false"  :data-disabled="true" />
    </k-form-item>
    <k-form-item label="产品名称">
      <k-field-text v-model="formData.prodName" :data-allowblank="false" :data-disabled="true" />
    </k-form-item>
    <k-form-item label="产品风险评级">
      <k-field-select v-model="formData.prodRiskLevel" data-dict="risklevel" :data-allowblank="false" :data-disabled="true" />
    </k-form-item>
  </k-form>
</template>

<script>
    export default {
        name: "DisplayRiskGrade",
      props:{
        formData:{},
      },
    }
</script>

<style scoped>

</style>
