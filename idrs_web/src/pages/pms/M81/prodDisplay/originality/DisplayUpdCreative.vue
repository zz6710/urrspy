<template>
  <k-form ref="editForm1" :data-col="2" data-input-width="220px" data-label-width="62px" data-total-width="988px">
    <k-form-item label="会议名称">
      <k-field-text v-model="formData.seminarName" :data-allowblank="false" :data-disabled="true" :data-max-length="128"/>
    </k-form-item>
    <k-form-item label="会议地点">
      <k-field-text v-model="formData.seminarAddr" :data-allowblank="false" :data-disabled="true" :data-max-length="64"/>
    </k-form-item>
    <k-form-item label="会议日期">
      <k-field-date v-model="formData.seminarDate" :data-allowblank="false" :data-disabled="true" data-type="date"/>
    </k-form-item>
    <k-form-item label="会议时间">
      <k-field-time v-model="formData.seminarTime" :data-allowblank="false" :data-disabled="true" data-type="time"/>
    </k-form-item>
    <k-form-item label="与会人">
      <k-field-select v-model="formData.participant" data-multiple="true" data-action="User.findUsers"
        data-value-field="username" data-display-field="username" :data-allowblank="false" :data-disabled="true"/>
    </k-form-item>
  </k-form>
</template>

<script>
    export default {
        name: "DisplayUpdCreative",
      props:{
        formData:{},
      },
    }
</script>

<style scoped>

</style>
