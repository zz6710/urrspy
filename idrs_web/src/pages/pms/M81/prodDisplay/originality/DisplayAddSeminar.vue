<template>
  <div>
    <k-form ref="addForm1" :data-col="3" data-input-width="220px" data-label-width="62px" data-total-width="988px">
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
        <k-field-select v-model="formData.participant" data-multiple="true" data-action="User.findUsers" :data-disabled="true"
                         data-value-field="username" data-display-field="username" :data-allowblank="false" />
      </k-form-item>
    </k-form>

    <k-form ref="addForm2" v-for="(item, index) in formData.envItems" :key="index" :data-col="6" data-input-width="120px"
            data-label-width="90px" data-total-width="1118px" >
      <k-form-item label="创意名称">
        <k-field-text v-model="item.originalityName" :data-allowblank="false" :data-disabled="true" :data-max-length="100"/>
      </k-form-item>
      <k-form-item label="创意类型">
        <k-field-select v-model="item.originalityType" data-dict="t8_originality_type" :data-disabled="true" :data-allowblank="false" />
      </k-form-item>
      <k-form-item label="创意发明人">
        <k-field-select v-model="item.inventor" data-action="User.findUsers" data-value-field="username"
                         data-display-field="username" :data-allowblank="false" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="创意状态">
        <k-field-select v-model="item.status" data-dict="t8_originality_status" :data-allowblank="false" :data-disabled="true" />
      </k-form-item>
    </k-form>
  </div>
</template>

<script>
    export default {
        name: "DisplayAddSeminar",
      props:{
        formData:{},
      },
    }
</script>

<style scoped>

</style>
