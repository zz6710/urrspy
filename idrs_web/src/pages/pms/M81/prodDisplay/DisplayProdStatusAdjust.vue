<template>
  <k-form ref="changeStatusForm">
    <k-form-item label="产品代码">
      <k-field-text v-model="ProdStatusChange.prodCode" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="产品名称">
      <k-field-text v-model="ProdStatusChange.prodName"  :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="调整类型" >
      <k-field-select v-model="ProdStatusChange.adjustType"  data-dict="t8_adjust_type" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="调整状态" >
      <k-field-select v-model="ProdStatusChange.prodStatus"  data-dict="t8_prod_status" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="调整子状态" >
      <k-field-select v-model="ProdStatusChange.prodSonStatus"  data-dict="t8_prod_son_status" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="调整原因" >
      <k-field-text v-model="ProdStatusChange.adjustCause" :data-disabled="true"></k-field-text>
    </k-form-item>
    <k-form-item label="是否回收代码" v-show="ProdStatusChange.isRecycle">
      <k-field-radio v-model="ProdStatusChange.isRecycle" data-dict="1yes0no" data-default-value="0" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="原产品代码" v-show="ProdStatusChange.isRecycle==='1'">
      <k-field-text v-model="ProdStatusChange.prodCodeOld" :data-disabled="true" />
    </k-form-item>
    <k-form-item label="产品名称" v-show="ProdStatusChange.isRecycle==='1'">
      <k-field-text v-model="ProdStatusChange.prodName" :data-disabled="true" />
    </k-form-item>
    <k-form-item label="新产品代码" v-show="ProdStatusChange.isRecycle==='1'">
      <k-field-text v-model="ProdStatusChange.prodCodeNew" :data-disabled="true" />
    </k-form-item>

  </k-form>
</template>

<script>
    export default {
        name: "DisplayProdStatusAdjust",
      props:{
        ProdStatusChange:{},
      },
    }
</script>

<style scoped>

</style>
