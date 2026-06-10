<template>
  <div>
    <k-form ref="InfoModelForm" :data-col="2" isFormBodyScreen>

      <div class ="tableLine2" ><span id="JCXX" class="leftText2">基础信息</span></div>

      <k-form-item label="资产编码" v-show="false">
        <k-field-text v-model="formData.scrId"  id="scrId" :data-disabled="formData.scrIdDisabled"/>
      </k-form-item>
      <k-form-item label="资产代码">
        <k-field-text v-model="formData.scrCd" id="scrCd" :data-allowblank="false" :data-max-length="40"  :data-disabled="formData.scrCdDisabled"/>
      </k-form-item>
      <k-form-item label="资产名称">
        <k-field-text v-model="formData.scrNm" id="scrNm" :data-allowblank="false" :data-max-length="256" :data-disabled="formData.scrNmDisabled"/>
      </k-form-item>
      <k-form-item label="市场">
        <k-field-select v-model="formData.trxMkt" id="trxMkt" :data-allowblank="false"
                        :data-disabled="formData.trxMktDisabled"
                        :data-data="trxMktDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        :data-default-value="'8'"/>
      </k-form-item>
      <k-form-item label="交易流通场所">
        <k-field-select  v-model="formData.trxPla" id="trxPla" :data-allowblank="false"
                         :data-default-value="'99'"
                         :data-data="tacdingPlaceDict"
                         data-display-field="TEXT"
                         data-value-field="VALUE" :data-disabled="formData.trxPlaDisabled"/>
      </k-form-item>
      <k-form-item label="发行方式">
        <k-field-select v-model="formData.issMod" id="issMod" data-dict="iss_mode_bond" :data-default-value="'02'" :data-disabled="formData.issModDisabled"/>
      </k-form-item>



      <div class ="tableLine1" ><span class="leftText">主体信息</span><div class="itemsCorn"></div></div>
      <k-form-item label="发行主体">
        <k-field-select v-model="formData.issuer" id="issuer"  :data-allowblank="false"  :data-disabled="formData.issuerDisabled" :data-max-length="265"
                      data-action="T8OrgSheet.findOrgNmAll" :data-params="{orgFullName:this.formData.issuer}" :dataRemote="true"
                      data-value-field="orgNbrExt" data-display-field="orgFullName"/>
      </k-form-item>
      <k-form-item label="中债发行机构所属行业" >
        <k-field-select v-model="formData.publisherTrade" id="publisherTrade" :data-disabled="formData.publisherTradeDisabled" :data-allowblank="false" data-dict="isuOrgBlgIdt"/>
      </k-form-item>
      <k-form-item label="发行机构类型（按规模划分）">
        <k-field-select v-model="formData.isuOrgTypScaleSiz" id="isuOrgTypScaleSiz" :data-allowblank="false" :data-disabled="formData.isuOrgTypScaleSizDisabled" data-dict="debtor_scale_type"/>
      </k-form-item>
      <k-form-item label="发行机构类型（按技术领域划分）">
        <k-field-select v-model="formData.isuOrgTypTchno" id="isuOrgTypTchno" :data-allowblank="false" :data-disabled="formData.isuOrgTypTchnoDisabled" data-dict="isuOrgTypTchno" />
      </k-form-item>
      <k-form-item label="发行机构类型（按经济类型划分）">
        <k-field-select v-model="formData.isuOrgTypEcn" id="isuOrgTypEcn" :data-allowblank="false" :data-disabled="formData.isuOrgTypEcnDisabled" data-dict="isuOrgTypEcn"/>
      </k-form-item>
      <k-form-item label="登记托管机构">
        <k-field-select v-model="formData.regTrstOrg" id="regTrstOrg" :data-default-value="'01'" :data-allowblank="false" :data-disabled="formData.regTrstOrgDisabled" data-dict="regTrstOrg"/>
      </k-form-item>


      <div class ="tableLine1" ><span class="leftText">主体评级</span><div class="itemsCorn"></div></div>
      <k-form-item label="主体评级（外部）">
        <k-field-select v-model="formData.subLevel" id="subLevel" :data-allowblank="false" :data-disabled="formData.subLevelDisabled" data-dict ="mainRating" />
      </k-form-item>

      <k-form-footer slot="footer" data-align="center" v-if="!isDetailShow">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-after-success="submitHandle" :data-handler="checkValues"
               :data-action="action" data-from="InfoModelForm"
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
  name:"MidDirectFusionEdit",
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
      formData: {},
      tacdingPlaceDict: {},
      trxMktDict: {},
    };
  },
  created() {
    this.formData = this.info;
    if (this.disabledVal){
      AssetCommon.checkColumn(this,'formData','Disabled','16','02',this.isDetailShow);
    }
    this.getAction();
    AssetCommon.areaDict(this,'tacdingPlaceDict','tacdingPlace','99',false);
    AssetCommon.areaDict(this,'trxMktDict','market_asset','8',false);
  },
  methods: {
    MenuSelect(index) {
      this.activeMenu = index;
      this.scrollToTarget('JCXX');
    },
    scrollToTarget(id) {
      const target = document.getElementById(id);
      if (target) { target.scrollIntoView({ behavior: 'smooth' }); }
    },
    //处理补录页面默认值
    defaultParamDeal(val){
      val.scrId=AssetCommon.dealDefaultVal(val.scrId,val.scrCd+"."+val.trxMkt+"."+"5");
    },
    getAction(){
      if (this.disabledVal){
        this.action = "MidDirectFusion.updateMidDirectFusion"
        return;
      }
      this.action = "MidDirectFusion.addMidDirectFusion"
    },
    changeTrxMkt(){
      this.$set(this.formData,"trxPla","");
      if (this.formData.trxMkt === '1') {
        this.formData.trxPla= '03'
      }
      if (this.formData.trxMkt === '2') {
        this.formData.trxPla= '04'
      }
      if (this.formData.trxMkt === '3') {
        this.formData.trxPla= '01'
      }
    },

    checkValues(val){
      if(!this.$refs.InfoModelForm.validate()){
        return false;
      }
      AssetCommon.removeVifRubbish(this,'formData','InfoModelForm',val);
      this.defaultParamDeal(val);
      return val;
    },
    submitHandle(value) {
      this.$emit('loadGriding',this.formData);
    },

  },
};
</script>
<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
