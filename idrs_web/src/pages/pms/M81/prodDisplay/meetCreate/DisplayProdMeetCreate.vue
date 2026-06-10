<template>
  <k-form ref="updateProdCreateMeetingForm" :data-col="2">
    <k-form-item label="会议名称">
      <k-field-text v-model="value.meetingName" :data-allowblank="false"  :data-disabled="true"  :data-max-length="32"/>
    </k-form-item>
    <k-form-item label="会议地点">
      <k-field-text v-model="value.meetingAddress" :data-allowblank="false"   :data-disabled="true" :data-max-length="250"/>
    </k-form-item>
    <k-form-item label="会议日期">
      <k-field-date v-model="value.meetingDate"  data-type="date" :data-allowblank="false" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="会议时间">
      <k-field-text v-model="value.meetingTime"  data-type="time" :data-allowblank="false"  :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="会议类型">
      <k-field-select v-model="value.meetingType" data-dict="t8_meeting_type" :dataAllowblank='false' :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="产品代码" data-input-width="600px">
      <k-field-select v-model="value.t8ProdInfoId" data-action="T8Dict.findTaProdInfos" :data-allowblank="false" :data-disabled="true"
                      data-display-field="prodCode,prodName"  data-value-field="t8ProdInfoId" :data-multiple="true"/>
    </k-form-item>
    <k-form-item label="与会人"  :data-max-length="255">
      <k-field-select v-model="value.participant" data-multiple="true" data-action="User.findUsers"
        data-value-field="username" data-display-field="username" :data-allowblank="false" :data-disabled="true"/>
    </k-form-item>
  </k-form>
</template>

<script>
    export default {
        name: "DisplayProdMeetCreate",
      props:{
        value:{},
      },
    }
</script>

<style scoped>

</style>
