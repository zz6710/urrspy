<template>
  <k-form ref="editT8ProdQuotaForm" :data-col="2" data-input-width="160px" data-label-width="90px">

    <k-form-item label="产品代码">
      <k-field-select v-model="formData.prodCode" data-multiple="false" data-action="T8ProdInfo.getProdInfos" data-value-field="prodCode"
        data-display-field="prodCode,prodName" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="成立/开放日">
      <k-field-date v-model="formData.quotaDate" data-type="date" :data-disabled="true" />
    </k-form-item>
    <k-form-item label="决策类型" v-if="formData.feeMeetingType">
      <k-field-select v-model="formData.feeMeetingType" data-dict="decision_type" :data-disabled="true" />
    </k-form-item>
    <k-form-item label="会议/审批单" v-if="formData.feeMeetingName" >
      <k-field-text v-model="formData.feeMeetingName"   :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="决策类型" v-if="formData.decisionType">
      <k-field-select v-model="formData.decisionType" data-dict="decision_type" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="会议/审批单" v-if="formData.meetingId">
      <k-field-select v-model="formData.meetingId"  data-action="MeetCreate.findMeetDict"
                      data-display-field="meetName" data-value-field="id" :data-disabled="true" />
    </k-form-item>
    <k-form-item label="销售总额度(亿)">
      <k-field-text v-model="formData.totalSaleQuota" data-validate-type="money"  :data-disabled="true"/>
    </k-form-item>
  </k-form>
</template>

<script>
  export default {
    name: "DisplayQuotaConfirm",
    props:{
      formData:{},
    },
  }
</script>

<style scoped>

</style>
