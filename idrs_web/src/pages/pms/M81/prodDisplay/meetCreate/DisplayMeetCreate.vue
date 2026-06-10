<template>
  <k-form ref="addMeetCreateForm" :data-col="2">

    <k-form-item label="会议名称">
      <k-field-text v-model="addDataFrom.meetName"  :data-max-length="255"  :dataAllowblank="false"  :data-disabled="true" />
    </k-form-item>

    <k-form-item label="会议地点">
      <k-field-text v-model="addDataFrom.meetSite" :data-max-length="255" :data-disabled="true" />
    </k-form-item>

    <k-form-item label="会议日期">
      <k-field-date v-model="addDataFrom.meetDate" :data-allowblank="false" data-type="date" :data-disabled="true"/>
    </k-form-item>

    <k-form-item label="会议时间">
      <k-field-time v-model="addDataFrom.meetTime" :data-allowblank="false" data-type="time" :data-disabled="true"/>
    </k-form-item>

    <k-form-item label="与会人">
      <k-field-select v-model="addDataFrom.username" data-multiple="true" :data-disabled="true"
                      data-action="User.getUser" data-display-field="username"  data-value-field="username"/>
    </k-form-item>
  </k-form>
</template>

<script>
    export default {
        name: "DisplayMeetCreate",
      props:{
        addDataFrom:{},
      },
    }
</script>

<style scoped>

</style>
