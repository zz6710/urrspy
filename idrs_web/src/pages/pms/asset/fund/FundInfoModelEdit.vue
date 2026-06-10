<template>
  <div>
    <k-form ref="editInfoModelForm" :data-col="2" isFormBodyScreen>

      <div class ="tableLine2" ><span class="leftText2">基本信息</span></div>


      <k-form-item label="资产编码" v-show="false">
        <k-field-text v-model="formData.scrId"  id="scrId" :data-disabled="formData.scrIdDisabled"/>
      </k-form-item>
      <k-form-item label="基金代码">
        <k-field-text v-model="formData.scrCd" id="scrCd" :data-allowblank="false" :data-max-length="40"  :data-disabled="formData.scrCdDisabled"/>
      </k-form-item>
      <k-form-item label="基金名称">
        <k-field-text v-model="formData.scrNm" id="scrNm" :data-allowblank="false" :data-max-length="256" :data-disabled="formData.scrNmDisabled"/>
      </k-form-item>
      <k-form-item label="市场">
        <k-field-select v-model="formData.trxMkt" id="trxMkt"
                        :data-allowblank="false"
                        :data-disabled="formData.trxMktDisabled"
                        :data-data="trxMktDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        @data-on-change="changeTrxMkt"/>
      </k-form-item>
      <k-form-item label="交易流通场所">
        <k-field-select  v-model="formData.trxPla" id="trxPla"
                         :data-allowblank="false"
                         :data-data="trxPlaDict"
                         data-display-field="TEXT"
                         data-value-field="VALUE"
                         :data-disabled="formData.trxPlaDisabled"/>
      </k-form-item>
      <k-form-item label="基金发行公司">
        <k-field-select v-model="formData.fndCmpNm" id="fndCmpNm" :data-max-length="256"  :data-allowblank="false"
                      :data-disabled="formData.fndCmpNmDisabled" data-action="T8OrgSheet.findOrgNmAll" :dataRemote="true"
                      :data-params="{orgFullName:this.formData.fndCmpNm}" data-display-field="orgFullName" data-value-field="orgNbrExt"/>
      </k-form-item>
      <k-form-item label="登记备案机构">
        <k-field-select v-model="formData.regRcdOrg" id="regRcdOrg" :data-allowblank="false" :data-disabled="formData.regRcdOrgDisabled" data-dict="regRcdOrg"/>
      </k-form-item>
      <k-form-item label="发行机构所属行业" >
        <k-field-select v-model="formData.idt" id="idt" :data-disabled="formData.idtDisabled" :data-allowblank="false" data-dict="isuOrgBlgIdt"/>
      </k-form-item>
      <k-form-item label="基金管理机构名称">
        <k-field-select v-model="formData.fndMngOrgNm" id="fndMngOrgNm" :data-allowblank="false" :data-disabled="formData.fndMngOrgNmDisabled"
                        data-action="T8OrgSheet.findOrgNmAll" :dataRemote="true" :data-params="{orgFullName:this.formData.fndMngOrgNm}"
                        data-value-field="orgNbrExt" data-display-field="orgFullName"/>
      </k-form-item>
      <k-form-item label="基金托管机构名称">
        <k-field-select v-model="formData.fndTrstOrgNm" id="fndTrstOrgNm" :data-allowblank="false" :data-disabled="formData.fndTrstOrgNmDisabled"
                        data-action="T8OrgSheet.findOrgNmAll" :dataRemote="true" :data-params="{orgFullName:this.formData.fndTrstOrgNm}"
                        data-value-field="orgNbrExt" data-display-field="orgFullName"/>
      </k-form-item>
      <k-form-item label="投资企业类型（按规模划分）" v-if="false">
        <k-field-select v-model="formData.invEntpTypSiz" id="invEntpTypSiz" :data-allowblank="false" :data-disabled="formData.invEntpTypSizDisabled" data-dict="debtor_scale_type"/>
      </k-form-item>
      <k-form-item label="投资企业类型（按技术领域划分）" v-if="false">
        <k-field-select v-model="formData.invEntpTypTchno" id="invEntpTypTchno" :data-allowblank="false" :data-disabled="formData.invEntpTypTchnoDisabled" data-dict="isuOrgTypTchno" />
      </k-form-item>
      <k-form-item label="投资企业类型（按经济类型划分）" v-if="false">
        <k-field-select v-model="formData.invEntpTypEcn" id="invEntpTypEcn" :data-allowblank="false" :data-disabled="formData.invEntpTypEcnDisabled" data-dict="isuOrgTypEcn"/>
      </k-form-item>
      <k-form-footer slot="footer" data-align="center" v-if="!isDetailShow">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="checkValues" :data-action="action" data-from="editInfoModelForm"
               :data-model="formData" :data-after-success="submitHandle" >
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
  name:"NonStandInfoModelEdit",
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
      trxMktDict:{},
      trxPlaDict:{},
    };
  },
  created() {
    this.formData = this.info;
    if (this.disabledVal) {
      AssetCommon.checkColumn(this,'formData','Disabled','3','02',this.isDetailShow);
    }
    AssetCommon.areaDict(this,'trxMktDict','market_asset','1,2,6',false);
    AssetCommon.areaDict(this,'trxPlaDict','tacdingPlace','03,04,99',false);
    this.getAction();
  },
  methods: {
    //处理补录页面默认值
    defaultParamDeal(val){
      val.scrId=AssetCommon.dealDefaultVal(val.scrId,val.scrCd+"."+val.trxMkt+"."+"11");
    },
    changeTrxMkt(){
      this.$set(this.formData,"trxPla","");
      if (this.formData.trxMkt === '1') {
        this.formData.trxPla= '03'
      }
      if (this.formData.trxMkt === '2') {
        this.formData.trxPla= '04'
      }
      if (this.formData.trxMkt === '6') {
        this.formData.trxPla= '99'
      }
    },
    getAction(){
      if (this.disabledVal){
        this.action = "FundInfoModel.updateFundInfo"
        return;
      }
      this.action = "FundInfoModel.addFoudInfo"
    },
    checkValues(value) {
      if(!this.$refs.editInfoModelForm.validate()){
        return false;
      }
      AssetCommon.removeVifRubbish(this,'formData','editInfoModelForm',value);
      this.defaultParamDeal(value);
      return value;
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
