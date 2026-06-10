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
      <k-form-item label="是否由金融资产投资公司发行">
        <k-field-select v-model="formData.isIssueFaic"  :data-allowblank="false" :data-disabled="formData.isIssueFaicDisabled" data-dict ="1yes2no"/>
      </k-form-item>
      <k-form-item label="投资行业">
        <k-field-select v-model="formData.investmentIndustry"  :data-allowblank="false" :data-disabled="formData.investmentIndustryDisabled" data-dict="pbc_eco_inds_typ_det"/>
      </k-form-item>
      <k-form-item label="登记备案机构">
        <k-field-select v-model="formData.fndRfa" id="fndRfa" :data-allowblank="false" :data-disabled="formData.fndRfaDisabled" data-dict="regRcdOrg" @data-on-change="change_cmt" :data-default-value="'04'"/>
      </k-form-item>
      <k-form-item label="是否为固定收益类">
        <k-field-select v-model="formData.isFic"  :data-allowblank="false" :data-disabled="formData.isFicDisabled" data-dict ="1yes2no" />
      </k-form-item>
      <k-form-item label="是否属于政府投资基金">
        <k-field-select v-model="formData.isGifnd"  :data-allowblank="isGifndallowblank"  :data-disabled="formData.isGifndDisabled" data-dict ="1yes2no" @data-on-change="change_fndIdg"/>
      </k-form-item>
      <k-form-item label="政府投资基金投向">
        <k-field-select v-model="formData.fndIdg"  :data-allowblank="fndIdgallowblank" :data-disabled="formData.fndIdgDisabled" data-dict ="government_investment_fund"/>
      </k-form-item>
      <k-form-item label="基金公司名称">
        <k-field-select v-model="formData.fndMi" data-action="T8OrgSheet.findOrgNmAll"   :dataRemote="true" :data-disabled="formData.fndMiDisabled"
                        :data-params="{orgFullName:this.formData.fndMi}" data-value-field="orgNbrExt" data-display-field="orgFullName" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="基金管理机构名称">
        <k-field-select v-model="formData.fndMiName" data-action="T8OrgSheet.findOrgNmAll"   :dataRemote="true" :data-disabled="formData.fndMiNameDisabled"
                        :data-params="{orgFullName:this.formData.fndMiName}" data-value-field="orgNbrExt" data-display-field="orgFullName" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="基金托管机构名称">
        <k-field-select v-model="formData.fndCiName" data-action="T8OrgSheet.findOrgNmAll"   :dataRemote="true" :data-disabled="formData.fndCiNameDisabled"
                        :data-params="{orgFullName:this.formData.fndCiName}" data-value-field="orgNbrExt" data-display-field="orgFullName" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="投资阶段" >
        <k-field-select v-model="formData.fndIs" :data-allowblank="false" :data-disabled="formData.fndIsDisabled" data-dict="Investment_stage" @data-on-change="change_cmt"/>
      </k-form-item>
      <k-form-item label="是否投向金融资产投资公司或其附属机构发行的私募股权投资基金">
        <k-field-select v-model="formData.isInvPeif"  :data-allowblank="false" :data-disabled="formData.isInvPeifDisabled" data-dict ="1yes0no"/>
      </k-form-item>
      <k-form-item label="资产分类">
        <k-field-select v-model="formData.fndType"  :data-allowblank="false" :data-disabled="formData.fndTypeDisabled" data-dict ="asset_classification"/>
      </k-form-item>
      <k-form-item label="备注">
        <k-field-text v-model="formData.cmt" :data-max-length="256" :data-allowblank="cmtallowblank"/>
      </k-form-item>
      <div class ="tableLine2" ><span class="leftText2">中债分类信息</span></div>
      <k-form-item label="中债一级分类">
        <k-field-select  v-model="formData.cbndFrsCtg"
                         :data-disabled="true"
                         :data-allowblank="false"
                         :data-data="cbndFrsCtgDict"
                         data-display-field="TEXT"
                         data-value-field="VALUE"
                         :data-default-value="'12'"/>
      </k-form-item>
      <k-form-item label="中债二级分类">
        <k-field-select v-model="formData.cbndScdCtg"
                        :data-data="cbndScdCtgDict"
                        @data-on-change ="change_value_dis"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        :data-allowblank="false"
                        :data-disabled="formData.cbndScdCtgDisabled"/>
      </k-form-item>
      <k-form-item label="投资企业类型（按规模划分）" >
        <k-field-select v-model="formData.lvrgTypSiz" :data-allowblank="lvrgTypallowblank" :data-disabled="formData.ilvrgTypSizDisabled" data-dict="debtor_scale_type" @data-on-change="change_cmt"/>
      </k-form-item>
      <k-form-item label="投资企业类型（按技术领域划分）" >
        <k-field-select v-model="formData.lvrgTypTchno" :data-allowblank="lvrgTypallowblank" :data-disabled="formData.ilvrgTypTchnoDisabled" data-dict="isuOrgTypTchno" @data-on-change="change_cmt" />
      </k-form-item>
      <k-form-item label="投资企业类型（按经济类型划分）" >
        <k-field-select v-model="formData.lvrgTypEcn"  :data-allowblank="lvrgTypallowblank" :data-disabled="formData.lvrgTypEcnDisabled" data-dict="isuOrgTypEcn" @data-on-change="change_cmt"/>
      </k-form-item>
      <k-form-item label="基金投资资产">
        <k-field-text v-model="formData.invAsset" :data-max-length="400" :data-allowblank="false"/>
      </k-form-item>
      <div class ="tableLine2" ><span class="leftText2">G06分类信息</span></div>
      <k-form-item label="G06一级分类">
        <k-field-select  v-model="formData.g06FrsCtg"
                         :data-disabled="true"
                         :data-data="g06FrsCtgDict"
                         data-display-field="TEXT"
                         data-value-field="VALUE"
                         :data-default-value="'1.12'"/>
      </k-form-item>
      <k-form-item label="G06二级分类">
        <k-field-select  v-model="formData.g06ScdCtg"
                         :data-disabled="formData.g06ScdCtgDisabled"
                         @data-on-change ="change_g06Trd_dict"
                         :data-data="g06ScdCtgDict"
                         data-display-field="TEXT"
                         data-value-field="VALUE"
                         />
      </k-form-item>
      <k-form-item label="G06三级分类">
        <k-field-select  v-model="formData.g06TrdCtg"
                         :data-disabled="formData.g06TrdCtgDisabled"
                         :data-data="g06TrdCtgDict"
                         data-display-field="TEXT"
                         data-value-field="VALUE"
                         />
      </k-form-item>
      <div class ="tableLine2" ><span class="leftText2">人行分类信息</span></div>
      <k-form-item label="人行一级分类">
        <k-field-select  v-model="formData.pbnkFrsCtg"
                         :data-disabled="true"
                         data-dict="asset_frs_ctg"
                         :data-default-value="'01'"/>
      </k-form-item>
      <k-form-item label="人行二级分类">
        <k-field-select  v-model="formData.pbnkScdCtg"
                         :data-disabled="formData.pbnkScdCtgDisabled"
                         data-dict="pbnkFrsCtg"
                         @data-on-change ="change_pbnkTrd_dict"
                         :data-default-value="'g'"
        />
      </k-form-item>
      <k-form-item label="人行三级分类">
        <k-field-select  v-model="formData.pbnkTrdCtg"
                         :data-disabled="formData.pbnkTrdCtgDisabled"
                         @data-on-change ="change_pbnkFou_dict"
                         :data-data="pbnkTrdCtgDict"
                         data-display-field="TEXT"
                         data-value-field="VALUE"
                         :data-default-value="'g2'"
        />
      </k-form-item>
      <k-form-item label="人行四级分类">
        <k-field-select  v-model="formData.pbnkFouCtg"
                         :data-disabled="formData.pbnkFouCtgDisabled"
                         :data-data="pbnkFouCtgDict"
                         data-display-field="TEXT"
                         data-value-field="VALUE"
                         :data-default-value="'g29'"
        />
      </k-form-item>
      <k-form-footer slot="footer" data-align="center">
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
  },
  data() {
    return {
      formData: {},
      cbndFrsCtgDict:{},
      cbndScdCtgDict:{},
      g06FrsCtgDict:{},
      g06ScdCtgDict:{},
      g06TrdCtgDict:{},
      pbnkTrdCtgDict:{},
      pbnkFouCtgDict:{},
      isGifndallowblank:true,
      fndIdgallowblank:true,
      lvrgTypallowblank:true,
      cmtallowblank:true,
      action: {
        type:Object,
      },

    };
  },
  created() {
    this.formData = this.info;
    if (this.disabledVal) {
      this.$set(this.formData,"scrCdDisabled",'true');
    }
    AssetCommon.areaDict(this,'cbndFrsCtgDict','cbndFrsCtg','12',false);
    AssetCommon.areaDict(this,'cbndScdCtgDict','cbndScdCtg','26',true);
    AssetCommon.areaDict(this,'g06FrsCtgDict','g06_first_type','1.12',false);
    AssetCommon.areaDict(this,'g06ScdCtgDict','g06_scd_type','1.12.',true);
    AssetCommon.areaDict(this,'g06TrdCtgDict','g06_trd_type','1.12.',true);
    AssetCommon.areaDict(this,'pbnkTrdCtgDict','pbnkScdCtg','',true);
    AssetCommon.areaDict(this,'pbnkFouCtgDict','pbnkTrdCtg','',true);
    this.getAction();
  },
  methods: {
    getAction(){
      if (this.disabledVal){
        this.action = "FundPrivateInfoModel.updateFundPrivateInfo"
        return;
      }
      this.action = "FundPrivateInfoModel.addFundPrivateInfo"
    },
    checkValues(value) {
      if(!this.$refs.editInfoModelForm.validate()){
        return false;
      }
    },
    submitHandle(value) {
      this.$emit('loadGriding',this.formData);
    },
    change_g06Trd_dict(value){
      this.$set(this.formData,"g06TrdCtg",'');
      AssetCommon.areaDict(this,'g06TrdCtgDict','g06_trd_type',value,true);
    },
    change_pbnkTrd_dict(value){
      this.$set(this.formData,"pbnkTrdCtg",'');
      AssetCommon.areaDict(this,'pbnkTrdCtgDict','pbnkScdCtg',value,true);
    },
    change_pbnkFou_dict(value){
      this.$set(this.formData,"pbnkFouCtg",'');
      AssetCommon.areaDict(this,'pbnkFouCtgDict','pbnkTrdCtg',value,true);
    },
    change_value_dis(value){
      if(value==='2602'){
        this.$set(this.formData,"isFic",'01');
      }else{
        this.$set(this.formData,"isFic",'02');
      }
      this.isGifndallowblank = !(value === '2607' || value === '2610' || value === '2611' || value === '2612' || value === '2613');
      this.lvrgTypallowblank = !(value === '2610' || value === '2611' || value === '2612' || value === '2613');
    },
    change_fndIdg(value){
      this.fndIdgallowblank = (value === '02')
    },
    change_cmt(value){
      this.cmtallowblank = !(this.formData.lvrgTypSiz === '99'||this.formData.lvrgTypTchno==='99'||this.formData.lvrgTypEcn==='99')
    }
  },
};
</script>
<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
