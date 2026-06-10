<template>
  <k-form ref="editT8ProdCreativeProjectForm" :data-col="2">
    <k-form-item label="" v-show="false">
      <k-field-text v-model="formData.id" />
    </k-form-item>
    <k-form-item label="研讨会id" v-show="false">
      <k-field-text v-model="formData.seminarId" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="会议名称">
      <k-field-text v-model="formData.seminarName" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="创意名称">
      <k-field-text v-model="formData.originalityName" :data-max-length="100" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="创意类型">
      <k-field-select data-dict="t8_originality_type" v-model="formData.originalityType" :data-disabled="true" />
    </k-form-item>
    <k-form-item label="创意发明人">
      <k-field-select v-model="formData.inventor" data-action="User.findUsers" data-value-field="userid"
                       data-display-field="username" :data-allowblank="false" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="创意状态">
      <k-field-select data-dict="t8_originality_status" :data-disabled="true"  v-model="formData.status" />
    </k-form-item>

  </k-form>
</template>

<script>
    export default {
        name: "DisplayUpdSeminar",
      props:{
        formData:{},
      },
    }
</script>

<style scoped>

</style>
