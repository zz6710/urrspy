<template>
  <k-form ref="updateProdCreateMeetingForm" :data-col="2">
    <k-form-item label="会议名称" data-input-width="225px">
      <k-field-text v-model="formDataCon.meetingName" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="决议类型">
      <k-field-select v-model="formDataCon.t8DecisionType" data-dict="t8_decision_type" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="产品名称" v-if="formDataCon.prodName != null && formDataCon.prodName !=''">
      <k-field-text v-model="formDataCon.prodName" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="产品代码" v-if="formDataCon.prodCode != null && formDataCon.prodCode !=''">
      <k-field-text v-model="formDataCon.prodCode" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="产品子系列" v-if="formDataCon.t8ProdSeriesId != null && formDataCon.t8ProdSeriesId !=''">
      <k-field-text v-model="formDataCon.t8ProdSeriesId" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="产品状态" v-if="formDataCon.meetingStatus != null && formDataCon.meetingStatus !=''">
      <k-field-select v-model="formDataCon.meetingStatus" data-dict="t8_prod_status" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="会议时间">
      <k-field-text v-model="formDataCon.crtDate" :data-disabled="true"/>
    </k-form-item>

    <k-form-item label="调整状态为">
      <k-field-select v-model="formDataCon.prodSonStatus" :data-disabled="true"  data-dict="t8_prod_son_status"/>
    </k-form-item>
    <k-form-item label="会议表决结果">
      <k-field-select v-model="formDataCon.meetingResult" :data-disabled="true"  data-dict="t8_meeting_result"/>
    </k-form-item>
  </k-form>
</template>

<script>
    export default {
        name: "DisplayProdMeetConfirm",
        props:{
        formDataCon:{},
      },
    }
</script>

<style scoped>

</style>
