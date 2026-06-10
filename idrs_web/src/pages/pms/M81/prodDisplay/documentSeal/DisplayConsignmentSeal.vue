<template>
  <k-form ref="applicationForm" :data-col="2" >
    <k-field-text v-model="application.t8ProdDocumentVersionId" v-show="false" :data-allowblank="false" />
    <k-form-item label="用印件名称">
      <k-field-text v-model="application.printName" :data-allowblank="false" :data-disabled="true" />
    </k-form-item>

    <k-form-item label="用印发起日期">
      <k-field-date v-model="application.printDate" :data-allowblank="false" :data-disabled="true"/>
    </k-form-item>

    <k-form-item label="用印部门">
      <k-field-select v-model="application.printOrg" data-action='Dept.find' data-display-field="deptname"
                      data-value-field="deptno" :data-allowblank="false" :data-disabled="true"/>
    </k-form-item>

    <k-form-item label="用印人">
      <k-field-select v-model="application.printUser" data-action="User.findUsers" data-display-field="username"  data-value-field="userid"
                      :data-allowblank="false" :data-disabled="true"/>
    </k-form-item>

    <k-form-item label="联系电话">
      <k-field-text v-model="application.phone" :data-max-length="11"
                    data-validate-type="int" data-type="int" :data-allowblank="false" :data-disabled="true"/>
    </k-form-item>

    <k-form-item label="用印种类">
      <k-field-select v-model="application.printType" data-dict="t8_print_type" :data-allowblank="false" :data-disabled="true"/>
    </k-form-item>

    <k-form-item label="发往单位">
      <k-field-text v-model="application.shipUnit" :data-allowblank="false" :data-disabled="true"/>
    </k-form-item>

    <k-form-item label="用印性质">
      <k-field-select v-model="application.printProperties" data-dict="t8_print_properties" :data-disabled="true" :data-allowblank="false"/>
    </k-form-item>

    <k-form-item label="法律审查意见书编号">
      <k-field-text v-model="application.opinionNumber" :data-allowblank="false" :data-disabled="true"/>
    </k-form-item>

    <k-form-item label="审批类型">
      <k-field-select v-model="application.approvalType" :data-allowblank="false" :data-disabled="true" data-dict="t8_approval_type"/>
    </k-form-item>

    <k-form-item label="用印种类及个数详情" data-input-width="590px">
      <k-field-text v-model="application.typeNum" :data-allowblank="false" :data-max-length="2000" inputType="textarea" :rows="1" :data-disabled="true"/>
    </k-form-item>

    <k-form-item label="用印事由" data-input-width="590px">
      <k-field-text v-model="application.printReason" :data-disabled="true" :data-max-length="2000" inputType="textarea" :rows="1" :data-allowblank="false"/>
    </k-form-item>
  </k-form>
</template>

<script>
    export default {
        name: "DisplayConsignmentSeal",
      props:{
        application:{},
      },
    }
</script>

<style scoped>

</style>
