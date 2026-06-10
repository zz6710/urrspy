<template>
  <div>
    <k-form ref="editMidAssAsharedescriptionForm" :data-col="2" isFormBodyScreen>

      <div class ="tableLine2" ><span class="leftText2">基础信息</span></div>


      <k-form-item label="证券编码" v-show="false">
        <k-field-text v-model="formData.scrId" id="scrId" :data-disabled="formData.scrIdDisabled"/>
      </k-form-item>
      <k-form-item label="股票代码">
        <k-field-text v-model="formData.scrCd" id="scrCd" :data-allowblank="false" :data-disabled="formData.scrCdDisabled" :data-max-length="40"/>
      </k-form-item>
      <k-form-item label="股票名称">
        <k-field-text v-model="formData.scrNm" id="scrNm" :data-allowblank="false" :data-disabled="formData.scrNmDisabled" :data-max-length="256"/>
      </k-form-item>
      <k-form-item label="市场">
        <k-field-select v-model="formData.trxMkt" id="trxMkt"
                        :data-data="trxMktDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        :data-allowblank="false"
                        :data-disabled="formData.trxMktDisabled"
                        @data-on-change="changeTrxMkt"/>
      </k-form-item>
      <k-form-item label="交易流通场所">
        <k-field-select v-model="formData.trxPla" id="trxPla"
                        :data-allowblank="false"
                        :data-data="trxPlaDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        :data-disabled="formData.trxPlaDisabled"/>
      </k-form-item>
      <k-form-item label="板块类型">
        <k-field-select v-model="formData.plateType" id="plateType" data-dict="plateType" :data-allowblank="false" :data-disabled="formData.plateTypeDisabled"/>
      </k-form-item>
      <k-form-item label="币种">
        <k-field-select v-model="formData.ccy" id="ccy" data-dict="cur_type" :data-allowblank="false" :data-default-value="'CNY'" :data-disabled="formData.ccyDisabled"/>
      </k-form-item>
      <k-form-item label="公司名称">
        <k-field-text v-model="formData.companyName" id="companyName" :data-allowblank="false" :data-disabled="formData.companyNameDisabled" :data-max-length="200"/>
      </k-form-item>

      <k-form-footer slot="footer" data-align="center" v-if="!isDetailShow">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-after-success="submitHandle" :data-handler="checkValues"
               :data-action="action" data-from="editMidAssAsharedescriptionForm"
               :data-model="formData" >
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </k-form-footer>
    </k-form>
  </div>
</template>

<script>
import AssetCommon from "@/pages/pms/asset/AssetComFunction";

export default {
  name: "MidAssAsharedescriptionEdit",
  props: {
    info : {
      type:Object,
    },
    //是否是不可修改
    disabledVal: {
      type:Boolean,
    },
    //是否只作为详情展示
    isDetailShow: {
      type:Boolean,
      default:false
    },
    action: {
      type:Object,
    },


  },
  data() {
    return {
      formData : {},
      trxMktDict : {},
      trxPlaDict : {},
    };
  },
  methods: {
    //处理补录页面默认值
    defaultParamDeal(val){
      val.scrId=AssetCommon.dealDefaultVal(val.scrId,val.scrCd+"."+val.trxMkt+"."+"6");
    },
    checkValues(val){
      if(!this.$refs.editMidAssAsharedescriptionForm.validate()){
        return false;
      }
      AssetCommon.removeVifRubbish(this,'formData','editMidAssAsharedescriptionForm',val);
      //处理补录页面默认值
      this.defaultParamDeal(val);
      return val;
    },
    submitHandle(value) {
      this.$emit('loadGriding',this.formData);
    },
    getAction(){
      if (this.disabledVal){
        this.action = "MidAssAsharedescription.updateMidAssAsharedescription"
        return;
      }
      this.action = "MidAssAsharedescription.addMidAssAsharedescription"
    },
    changeTrxMkt(){
      this.$set(this.formData,"trxPla","");
      if (this.formData.trxMkt === '1') {
        this.formData.trxPla= '03'
      }
      if (this.formData.trxMkt === '2') {
        this.formData.trxPla= '04'
      }
      if (this.formData.trxMkt === '10') {
        this.formData.trxPla= '99'
      }
    },

  },
  created() {
    this.formData = this.info;
    if (this.disabledVal){
      AssetCommon.checkColumn(this,'formData','Disabled','5','02',this.isDetailShow);
    }
    AssetCommon.areaDict(this,'trxMktDict','market_asset','1,2,10',false);
    AssetCommon.areaDict(this,'trxPlaDict','tacdingPlace','03,04,99',false);
    this.getAction();
  }


}
</script>
<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
