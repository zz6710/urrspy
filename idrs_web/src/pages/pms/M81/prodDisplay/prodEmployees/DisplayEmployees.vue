<template>
  <k-form ref="addCustomerInfoForm" :data-col="2" dataLabelWidth="140px" dataInputWidth="190px">
    <k-form-item label="姓名">
      <k-field-text v-model="value.custName" :dataAllowblank="false" :data-max-length="32" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="人物类型">
      <k-field-select v-model="value.custType" :dataAllowblank="false" data-dict="t8_cust_type" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="人员简介" :data-col="2">
      <k-field-text v-model="value.brief" :data-disabled="true" inputType="textarea" :rows="5"/>
    </k-form-item>
    <k-form-item label="工号">
      <k-field-text v-model="value.jobno" :data-max-length="20" :data-disabled="true" />
    </k-form-item>
    <k-form-item label="证件类型">
      <k-field-select v-model="value.idType" :dataAllowblank="value.custType=='3'" data-dict="t8_id_type" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="证件号码">
      <k-field-text v-model="value.idCode" :dataAllowblank="value.idType=='8'?true:value.custType=='3'"
                    :data-max-length="32" :data-min-length="6" :data-disabled="true" />
    </k-form-item>
    <k-form-item label="手机">
      <k-field-text v-model="value.mobile" :data-max-length="11" data-validate-type="telephone"
                    :dataAllowblank="value.custType!='3'" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="座机">
      <k-field-text v-model="value.homeTel" :data-max-length="20" :dataAllowblank="value.custType!='3'"
                    :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="电子邮箱">
      <k-field-text v-model="value.email" :dataAllowblank="value.custType!='3'" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="地址" :data-col="2">
      <k-field-text v-model="value.address" :data-max-length="128" :data-disabled="true" inputType="textarea"/>
    </k-form-item>
    <k-form-item label="从业年限">
      <k-field-text v-model="value.employmentTerm" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="投资管理从业年限">
      <k-field-text v-model="value.investEmploymentTerm" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="过往从业经历" :data-col="2">
      <k-field-text v-model="value.pastEmploymentExp" inputType="textarea" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="是否被处罚">
      <k-field-select v-model="value.isPunish" data-dict="is_punish" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="国籍">
      <k-field-select v-model="value.nationality" data-dict="t8_nation_code" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="学位学历">
      <k-field-text v-model="value.education" :data-disabled="true"/>
    </k-form-item>
     <k-form-item label="是否登记">
      <k-field-select v-model="value.isRegister" data-dict="is_register" :data-disabled="true" />
    </k-form-item>
     <k-form-item label="登记编号">
      <k-field-text v-model="value.register" :data-max-length="128" :data-disabled="true" />
    </k-form-item>
    <k-form-item label="任职日期">
      <k-field-date v-model="value.postDate"  :data-disabled="true" />
    </k-form-item>

  </k-form>
</template>

<script>
    export default {
        name: "DisplayEmployees",
      props:{
        value:{},
      },
    }
</script>

<style scoped>

</style>
