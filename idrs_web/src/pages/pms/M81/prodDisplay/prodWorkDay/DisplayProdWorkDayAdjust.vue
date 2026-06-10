<template>
  <k-form class="my-form " ref="prodWorkdayAdjust" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">

    <k-form-item label="产品代码 ">
      <k-field-text v-model="fromData.prodCode" :data-allowblank="false" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="id" v-show="false">
      <k-field-text v-model="fromData.id" :data-allowblank="false" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="产品成立日">
      <k-field-date  v-model="fromData.establishDate" data-type="date" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="开放起始日">
      <k-field-date v-model="fromData.openStartDate" data-type="date" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="开放结束日" >
      <k-field-date v-model="fromData.openEndDate" data-type="date" :data-disabled="true" />
    </k-form-item>
    <k-form-item label="产品到期日">
      <k-field-date v-model="fromData.endDate" data-type="date" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="调整原因">
      <k-field-text v-model="fromData.adjustReason" :data-allowblank="false" :data-disabled="true"/>
    </k-form-item>
  </k-form>
</template>

<script>
    export default {
        name: "DisplayProdWorkdayAdjust",
      props: {
        fromData: {},
      },
    }
</script>

<style scoped>

</style>
