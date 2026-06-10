<template>
<div>
  <k-form class="my-form" ref="addT8ProdIssueInfo" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">

   <k-form-item label="产品登记编码">
     <k-field-text v-model="T8ProdIssueRegisFields.registCode" :data-disabled="true" :data-allowblank="false"></k-field-text>
   </k-form-item>
   <k-form-item label="产品代码">
     <k-field-text v-model="T8ProdIssueRegisFields.prodCode" :data-disabled="true" :data-allowblank="false"></k-field-text>
   </k-form-item>
   <k-form-item label="发行机构代码">
     <k-field-text value="C10308" :data-disabled="true" :data-allowblank="false"></k-field-text>
   </k-form-item>
   <k-form-item label="募集起始日期">
     <k-field-date v-model="T8ProdIssueRegisFields.applyStartDate" data-date-format="yyyy-MM-dd" :data-disabled="true" :data-allowblank="false"></k-field-date>
   </k-form-item>
   <k-form-item label="募集结束日期">
     <k-field-date v-model="T8ProdIssueRegisFields.applyEndDate" data-date-format="yyyy-MM-dd" :data-disabled="true" :data-allowblank="false"></k-field-date>
   </k-form-item>
   <k-form-item label="产品起始日期">
     <k-field-date v-model="T8ProdIssueRegisFields.establishDate" data-date-format="yyyy-MM-dd" :data-disabled="true" :data-allowblank="false"></k-field-date>
   </k-form-item>
   <k-form-item label="产品终止日期">
     <k-field-date v-model="T8ProdIssueRegisFields.endDate" data-date-format="yyyy-MM-dd" :data-disabled="true" :data-allowblank="false"></k-field-date>
   </k-form-item>
   <k-form-item label="管理方式">
     <k-field-select v-model="T8ProdIssueRegisFields.manageMethod" data-dict="t8_managetype" :data-allowblank="false" > </k-field-select>
   </k-form-item>
   <k-form-item label="是否为结构化（分级）产品">
     <k-field-select v-model="T8ProdIssueRegisFields.isStructural" data-dict="t8_prod_is_structural" :data-allowblank="false" > </k-field-select>
   </k-form-item>
   <k-form-item label="业绩比较基准上限%">
     <k-field-text v-model="T8ProdIssueRegisFields.performBenchmarksUpper" data-validate-type="number" data-max-value="100"
                   data-min-value="T8ProdIssueRegisFields.performBenchmarksLower" ></k-field-text>
   </k-form-item>
   <k-form-item label="业绩比较基准下限%">
     <k-field-text v-model="T8ProdIssueRegisFields.performBenchmarksLower" data-validate-type="number"
                   data-max-value="T8ProdIssueRegisFields.performBenchmarksUpper"
                   data-min-value="0"></k-field-text>
   </k-form-item>
   <k-form-item label="开放模式" v-if="prodMode!='1'">
     <k-field-select v-model="T8ProdIssueRegisFields.openMod" data-dict="open_mod"
                   :data-allowblank="prodMode=='1'"></k-field-select>
   </k-form-item>
   <k-form-item label="规律开放周期"  v-if="prodMode!='1' && T8ProdIssueRegisFields.openMod == '01'">
     <k-field-select v-model="T8ProdIssueRegisFields.openCalendar" data-dict="t8_open_calendar"
                     :data-allowblank="prodMode=='1' || T8ProdIssueRegisFields.openMod != '01'"></k-field-select>
   </k-form-item>
    <k-form-item label="其他规律开放周期（天）" v-if="prodMode!='1' && T8ProdIssueRegisFields.openMod == '01' && T8ProdIssueRegisFields.openCalendar == '99'">
      <k-field-text v-model="T8ProdIssueRegisFields.otherRegularOpenCycle" data-validate-type="number"
                    :data-allowblank="prodMode=='1' || T8ProdIssueRegisFields.openMod != '01' || T8ProdIssueRegisFields.openCalendar != '99'"></k-field-text>
    </k-form-item>
    <k-form-item label="首次开放周期起始日" v-if="prodMode!='1'">
      <k-field-date v-model="T8ProdIssueRegisFields.firstOpenStartDate" data-date-format="yyyy-MM-dd" :data-allowblank="prodMode=='1'"/>
    </k-form-item>
    <k-form-item label="节假日是否开放" v-if="prodMode!='1'">
      <k-field-select v-model="T8ProdIssueRegisFields.isOpen" data-dict="t8_prod_isok" data-default-value="1"  :data-allowblank="prodMode=='1'"/>
    </k-form-item>
    <k-form-item label="平均开放次数（年化）" v-if="prodMode!='1'">
      <k-field-text v-model="T8ProdIssueRegisFields.averageYearOpenTimes" data-validate-type="number" :data-allowblank="prodMode=='1'"></k-field-text>
    </k-form-item>
    <k-form-item label="开放期业务" v-if="prodMode!='1'">
      <k-field-select v-model="T8ProdIssueRegisFields.openDuringBusiness" data-dict="t8_open_control"  :data-allowblank="prodMode=='1'" > </k-field-select>
    </k-form-item>
    <k-form-item label="资金托管账号">
      <k-field-text v-model="T8ProdIssueRegisFields.fundTrusteeshipAccountNo" :data-allowblank="false"></k-field-text>
    </k-form-item>
    <k-form-item label="资金托管账户">
      <k-field-text v-model="T8ProdIssueRegisFields.fundTrusteeshipAccount" :data-allowblank="false"></k-field-text>
    </k-form-item>
    <k-form-item label="业绩比较基准说明" :data-col="2">
      <k-field-text v-model="T8ProdIssueRegisFields.performBenchmarksDesc" inputType="textarea" :rows="1" :data-max-length="2000"></k-field-text>
    </k-form-item>
    <k-form-item label="无规律开放说明" v-if="prodMode!='1' && T8ProdIssueRegisFields.openMod == '02'" :data-col="2">
      <k-field-text v-model="T8ProdIssueRegisFields.irregularOpenDesc"
                    :data-allowblank="prodMode=='1' || T8ProdIssueRegisFields.openMod != '02'"
                    inputType="textarea" :rows="1" :data-max-length="2000"></k-field-text>
    </k-form-item>
    <k-form-item label="开放期业务说明" v-if="prodMode!='1'" :data-col="2">
      <k-field-text v-model="T8ProdIssueRegisFields.openDuringBusinessDesc"
                    :data-allowblank="prodMode=='1' || T8ProdIssueRegisFields.openDuringBusiness!='99'"
                    inputType="textarea" :rows="1" :data-max-length="2000"></k-field-text>
    </k-form-item>
    <k-form-footer data-align="center"  v-show="menuName == 'ProdIssueAdjustList'">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdIssueRegisFields.addProdIssueInfoAdd" data-from="addT8ProdIssueInfo"
             :data-model="T8ProdIssueRegisFields"  :data-handler="addHandler" :data-after-success="passDataSuccess">
        <md-icon md-src="/static/svg/confirm.svg"></md-icon>保存
      </k-btn>
    </k-form-footer>
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
    menuName:"",
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
    passDataSuccess(){
      this.$emit('isShowButton', '1')
    },
    addHandler(val){
      this.$set(val,'assemblyMenuType','prodIssueAdjust');
    },
    validateData() {
      return this.$refs.addT8ProdIssueInfo.validate();
    }
  }

}
</script>

<style scoped>

</style>
