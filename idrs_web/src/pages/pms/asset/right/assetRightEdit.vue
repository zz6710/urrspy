<template>
  <div>
    <k-form ref="editAssetRightForm" :data-col="2" isFormBodyScreen>

      <div class ="tableLine2" ><span class="leftText2">基础信息</span></div>

      <k-form-item label="序号" v-show="false">
        <k-field-text v-model="formData.id" id="id" :data-disabled="formData.idDisabled"/>
      </k-form-item>

      <k-form-item label="股权代码">
        <k-field-text v-model="formData.assNbrExt" id="assNbrExt" :data-allowblank="false" :data-disabled="formData.assNbrExtDisabled"/>
      </k-form-item>
      <k-form-item label="交易流通场所">
        <k-field-select v-model="formData.tradePlaces" id="tradePlaces"
                        :data-data="tacdingPlaceDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE" :data-disabled="formData.tradePlacesDisabled"
                        :data-allowblank="false" data-default-value="99" />
      </k-form-item>
      <k-form-item label="融资企业名称">
        <k-field-select v-model="formData.orgNbrExt" id="orgNbrExt" :data-allowblank="false"  :data-disabled="formData.orgNbrExtDisabled"
                        data-action="T8OrgSheet.findOrgNmAll" :data-params="{orgFullName:this.formData.orgNbrExt}" :dataRemote="true"
                        data-value-field="orgNbrExt" data-display-field="orgFullName"
                        @data-on-change="findOrgInfo" />
      </k-form-item>
      <k-form-item label="是否通道投资">
        <k-field-select v-model="formData.isChannel" id="isChannel" :data-allowblank="false"  :data-disabled="formData.isChannelDisabled"
                        data-dict="isTrue"/>
      </k-form-item>
      <k-form-item label="通道" v-if="formData.isChannel==='01'">
        <k-field-select v-model="formData.channelCode" id="channelCode" :data-allowblank="false" :data-disabled="formData.channelCodeDisabled"
                        data-action="FundPrivateInfoModel.findFundPrivateInfoModelsCdAndNm"  :dataRemote="true"
                        data-display-field="scrCd,scrNm" data-value-field="scrCd"/>
      </k-form-item>
      <k-form-item label="融资企业行业">
        <k-field-select v-model="formData.industryIssuer" id="industryIssuer" :data-disabled="formData.industryIssuerDisabled"
                        :data-allowblank="false"
                        data-dict="isuOrgBlgIdt"/>
      </k-form-item>
      <k-form-item label="企业类型（按规模划分）">
        <k-field-select v-model="formData.ggEnterTypeScale" id="ggEnterTypeScale"
                        :data-allowblank="true" :data-disabled="formData.ggEnterTypeScaleDisabled" data-dict="debtor_scale_type"/>
      </k-form-item>
      <k-form-item label="企业类型（按技术领域划分）">
        <k-field-select v-model="formData.ggEnterTypeTech" id="ggEnterTypeTech" :data-disabled="formData.ggEnterTypeTechDisabled"
                        :data-allowblank="true" data-dict="isuOrgTypTchno"/>
      </k-form-item>
      <k-form-item label="企业类型（按经济类型划分）">
        <k-field-select v-model="formData.ggEnterTypeEconomic" id="ggEnterTypeEconomic" :data-disabled="formData.ggEnterTypeEconomicDisabled"
                        :data-allowblank="true"   data-dict="isuOrgTypEcn"/>
      </k-form-item>
      <k-form-item label="投资阶段">
        <k-field-select v-model="formData.investmentType" id="investmentType" :data-disabled="formData.investmentTypeDisabled"
                        :data-allowblank="false"
                        data-dict="invest_stage"/>
      </k-form-item>
      <k-form-item label="股权退出安排">
        <k-field-date v-model="formData.sharehold" id="sharehold" :data-allowblank="false"  :data-disabled="formData.shareholdDisabled"/>
      </k-form-item>
      <k-form-item label="是否为质押融资">
        <k-field-select v-model="formData.ggPledgedFinace" id="ggPledgedFinace" :data-disabled="formData.ggPledgedFinaceDisabled"
                        :data-allowblank="false"
                        data-dict="isTrue"/>
      </k-form-item>
      <k-form-item label="是否为债转股">
        <k-field-select v-model="formData.ggDebtEquitySwap" id="ggDebtEquitySwap" :data-disabled="formData.ggDebtEquitySwapDisabled"
                        :data-allowblank="false"
                        data-dict="isTrue"/>
      </k-form-item>
      <k-form-item label="中债一级分类">
        <k-field-select v-model="formData.cbcType" id="cbcType" :data-allowblank="true"  :data-disabled="formData.cbcTypeDisabled"
                        :data-data="cbndFrsCtgDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        data-default-value="6"/>
      </k-form-item>
      <k-form-item label="中债二级分类">
        <k-field-select v-model="formData.cbcSubType" id="cbcSubType" :data-allowblank="true"  :data-disabled="formData.cbcSubTypeDisabled"
                        :data-data="cbndScdCtgDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        data-default-value="1301"/>
      </k-form-item>
      <k-form-item label="G06一级分类">
        <k-field-select v-model="formData.ggCbcType" id="ggCbcType" :data-allowblank="false" :data-disabled="formData.ggCbcTypeDisabled"
                        :data-data="ggCbcTypeDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        data-default-value="1.6"/>
      </k-form-item>
      <k-form-item label="G06二级分类">
        <k-field-select v-model="formData.ggCbcSubType" id="ggCbcSubType" :data-allowblank="false" :data-disabled="formData.ggCbcSubTypeDisabled"
                        :data-data="ggCbcSubTypeDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        data-default-value="1.6.1"/>
      </k-form-item>


      <k-form-footer slot="footer" data-align="center" v-if="!isDetailShow">

        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-after-success="submitHandle" :data-handler="checkValues"
               :data-action="action" data-from="editAssetRightForm"
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
import httpUtil from "@/frame/httpUtil";

export default {
  name: "assetRightEdit",
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
      formData : {},
      tacdingPlaceDict : {},
      action : '',
      //中债
      cbndFrsCtgDict:{},
      cbndScdCtgDict:{},
      //g06
      ggCbcTypeDict:{},
      ggCbcSubTypeDict:{},
    };
  },

  methods: {

    findOrgInfo() {
      this.$set(this.formData, 'ggEnterTypeScale', '');
      this.$set(this.formData, 'ggEnterTypeTech', '');
      this.$set(this.formData, 'ggEnterTypeEconomic', '');
      this.httpUtil.comnQuery({
        action: "T8OrgSheet.findOrgInfo",
        params: {orgNbrExt: this.formData.orgNbrExt}
      }).then(data => {
        this.formData.ggEnterTypeScale = data.rows[0].isuOrgTypScaleSiz;
        this.formData.ggEnterTypeTech = data.rows[0].isuOrgTypTchno;
        this.formData.ggEnterTypeEconomic = data.rows[0].isuOrgTypEcn;
      }).catch({})
    },
    checkValues(val){
      if(!this.$refs.editAssetRightForm.validate()){
        return false;
      }
      AssetCommon.removeVifRubbish(this,'formData','editAssetRightForm',val);
      return val;
    },
    submitHandle(value) {
      this.$emit('loadGriding',this.formData);
    },
    getAction(){
      if (this.disabledVal){
        this.action = "AssetRightModel.updateAssetRight"
        return;
      }
      this.action = "AssetRightModel.addAssetRight"
    },

  },
  created() {
    this.formData = this.info;
    if (this.disabledVal){
      AssetCommon.checkColumn(this,'formData','Disabled','18','02',this.isDetailShow);
    }
    this.getAction();
    AssetCommon.areaDict(this,'tacdingPlaceDict','tacdingPlace','99',false);
    //中债字典处理
    AssetCommon.areaDict(this,'cbndFrsCtgDict','cbndFrsCtg','6',false);
    AssetCommon.areaDict(this,'cbndScdCtgDict','cbndScdCtg','1301',false);
    //g06字典处理
    AssetCommon.areaDict(this,'ggCbcTypeDict','g06_first_type',"'1.6'",false);
    AssetCommon.areaDict(this, 'ggCbcSubTypeDict', 'g06_scd_type', "'1.6.1'",false);
  },



}
</script>

<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
