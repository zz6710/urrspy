<template>
<div>
  <k-form class="my-form" ref="addT8ProdIssueInfo" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">

   <k-form-item label="产品登记编码">
     <k-field-text v-model="T8ProdIssueRegisFields.registCode"  :data-disabled="true"/>
   </k-form-item>
   <k-form-item label="产品代码">
     <k-field-text v-model="T8ProdIssueRegisFields.prodCode"  :data-disabled="true"/>
   </k-form-item>
   <k-form-item label="发行机构代码">
     <k-field-text value="C10308"  :data-disabled="true"/>
   </k-form-item>
   <k-form-item label="募集起始日期">
     <k-field-date v-model="T8ProdIssueRegisFields.applyStartDate" data-type="date" :data-disabled="true" />
   </k-form-item>
   <k-form-item label="募集结束日期">
     <k-field-date v-model="T8ProdIssueRegisFields.applyEndDate"data-type="date" :data-disabled="true"/>
   </k-form-item>
   <k-form-item label="产品起始日期">
     <k-field-date v-model="T8ProdIssueRegisFields.establishDate" data-type="date" :data-disabled="true"/>
   </k-form-item>
   <k-form-item label="产品终止日期">
     <k-field-date v-model="T8ProdIssueRegisFields.endDate" data-type="date" :data-disabled="true" />
   </k-form-item>
   <k-form-item label="管理方式">
     <k-field-select v-model="T8ProdIssueRegisFields.manageMethod" data-dict="t8_managetype" :data-disabled="true" />
   </k-form-item>
   <k-form-item label="是否为结构化（分级）产品">
     <k-field-select v-model="T8ProdIssueRegisFields.isStructural" data-dict="t8_prod_is_structural" :data-disabled="true" />
   </k-form-item>
   <k-form-item label="业绩比较基准上限%">
     <k-field-text v-model="T8ProdIssueRegisFields.performBenchmarksUpper" data-validate-type="number" data-max-value="100"
                   data-min-value="T8ProdIssueRegisFields.performBenchmarksLower" :data-disabled="true"/>
   </k-form-item>
   <k-form-item label="业绩比较基准下限%">
     <k-field-text v-model="T8ProdIssueRegisFields.performBenchmarksLower" data-validate-type="number"
                   data-max-value="T8ProdIssueRegisFields.performBenchmarksUpper" :data-disabled="true"
                   data-min-value="0"/>
   </k-form-item>
   <k-form-item label="业绩比较基准说明" :data-col="2">
     <k-field-text v-model="T8ProdIssueRegisFields.performBenchmarksDesc" :data-max-length="2000" inputType="textarea" :rows="1"
                      :data-disabled="true" />
   </k-form-item>
   <k-form-item label="开放模式" v-show="prodMode!='1'">
     <k-field-select v-model="T8ProdIssueRegisFields.openMod" data-dict="open_mod"   :data-disabled="true"/>
   </k-form-item>
   <k-form-item label="规律开放周期"  v-show="prodMode!='1' && T8ProdIssueRegisFields.openMod == '01'">
     <k-field-select v-model="T8ProdIssueRegisFields.openCalendar" data-dict="t8_open_calendar"  :data-disabled="true"/>
   </k-form-item>
    <k-form-item label="其他规律开放周期（天）" v-show="prodMode!='1' && T8ProdIssueRegisFields.openMod == '01' && T8ProdIssueRegisFields.openCalendar == '99'">
      <k-field-text v-model="T8ProdIssueRegisFields.otherRegularOpenCycle" data-validate-type="number"  :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="无规律开放说明" v-show="prodMode!='1' && T8ProdIssueRegisFields.openMod == '02'">
      <k-field-text v-model="T8ProdIssueRegisFields.irregularOpenDesc"  :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="首次开放周期起始日" v-show="prodMode!='1'">
      <k-field-date v-model="T8ProdIssueRegisFields.firstOpenStartDate" data-date-format="yyyy-MM-dd"  :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="节假日是否开放" v-show="prodMode!='1'">
      <k-field-select v-model="T8ProdIssueRegisFields.isOpen" data-dict="t8_prod_isok" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="平均开放次数（年化）" v-show="prodMode!='1'">
      <k-field-text v-model="T8ProdIssueRegisFields.averageYearOpenTimes" data-validate-type="number" v-show="prodMode!='1'"  :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="开放期业务" v-show="prodMode!='1'">
      <k-field-select v-model="T8ProdIssueRegisFields.openDuringBusiness" data-dict="t8_open_control"  :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="开放期业务说明" v-show="prodMode!='1'" :data-col="2">
      <k-field-text v-model="T8ProdIssueRegisFields.openDuringBusinessDesc"  :data-disabled="true" :data-max-length="2000" inputType="textarea" :rows="1"/>
    </k-form-item>
    <k-form-item label="资金托管账号">
      <k-field-text v-model="T8ProdIssueRegisFields.fundTrusteeshipAccountNo"  :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="资金托管账户">
      <k-field-text v-model="T8ProdIssueRegisFields.fundTrusteeshipAccount"  :data-disabled="true" />
    </k-form-item>
  </k-form>

</div>
</template>

<script>
export default {
  name: "M81001-ProdIssueAdjust",
  model: {
    prop: 'T8ProdIssueRegisFields',
    event: 'input'
  },
  props:{
    T8ProdIssueRegisFields :{},
    assemblyMenuType :'',
    prodCode: {
      type: String,
      default: ''
    },
    prodMode :'',
    t8ProdInfoId: {
      type: String,
      default: ''
    },
  },
  data(){
    return{

    }
  },
  created() {
  },
  methods:{
    validateData() {
      return this.$refs.addT8ProdIssueInfo.validate();
    }
  }

}
</script>

<style scoped>

</style>
