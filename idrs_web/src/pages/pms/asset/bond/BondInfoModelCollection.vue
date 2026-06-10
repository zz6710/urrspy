<template>
  <div>
    <k-form ref="blBondInfoModelForm" :data-col="2" isFormBodyScreen>

      <div class ="tableLine2" ><span id="BLXX" class="leftText2">补录信息</span></div>

      <k-form-item label="证券编码" v-show="false">
        <k-field-text v-model="formData.scrId"  id="scrId" :data-disabled="formData.scrIdDisabled" />
      </k-form-item>
      <div class ="tableLine1" ><span class="leftText">中债分类</span><div class="itemsCorn"></div></div>
      <k-form-item label="中债一级分类">
        <k-field-select v-model="formData.cbndFrsCtg" id="cbndFrsCtg"
                        :data-allowblank="false"
                        :data-disabled="formData.cbndFrsCtgDisabled"
                        :data-data="cbndFrsCtgDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"/>
      </k-form-item>
      <k-form-item label="中债二级分类">
        <k-field-select v-model="formData.cbndScdCtg" id="cbndScdCtg"
                        :data-allowblank="false"
                        :data-disabled="formData.cbndScdCtgDisabled"
                        :data-data="cbndScdCtgDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE" />
      </k-form-item>
      <k-form-item label="具体类别">
        <k-field-select v-model="formData.spcType" id="spcType"
                        :data-allowblank="!(formData.cbndScdCtg === '1112' ||
                                              formData.cbndScdCtg === '1113' ||
                                              formData.cbndScdCtg === '1114' ||
                                              formData.cbndScdCtg === '1115' ||
                                              formData.cbndScdCtg === '1116' ||
                                              formData.cbndScdCtg === '1117')"
                        :data-disabled="formData.spcTypeDisabled"
                        data-dict ="spcType" />
      </k-form-item>

      <div class ="tableLine1" ><span class="leftText">G06分类</span><div class="itemsCorn"></div></div>
      <k-form-item label="G06一级分类">
        <k-field-select v-model="formData.ggCbcType" id="ggCbcType"
                        :data-allowblank="false"
                        :data-disabled="formData.ggCbcTypeDisabled"
                        :data-data="ggCbcTypeDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE" />
      </k-form-item>
      <k-form-item label="G06二级分类">
        <k-field-select v-model="formData.ggCbcSubType" id="ggCbcSubType"
                        :data-allowblank="false"
                        :data-disabled="formData.ggCbcSubTypeDisabled"
                        :data-data="ggCbcSubTypeDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE" />
      </k-form-item>

      <div class ="tableLine1" ><span class="leftText">人行分类</span><div class="itemsCorn"></div></div>
      <k-form-item label="人行一级分类">
        <k-field-select v-model="formData.pbnkFrsCtg" id="pbnkFrsCtg"
                        :data-allowblank="true"
                        :data-disabled="formData.pbnkFrsCtgDisabled"
                        :data-data="pbnkFrsCtgDict"
                        data-value-field="VALUE"
                        data-display-field="TEXT"/>
      </k-form-item>
      <k-form-item label="人行二级分类">
        <k-field-select v-model="formData.pbnkScdCtg" id="pbnkScdCtg"
                        :data-allowblank="true"
                        :data-disabled="formData.pbnkScdCtgDisabled"
                        :data-data="pbnkScdCtgDict"
                        data-value-field="VALUE"
                        data-display-field="TEXT" />
      </k-form-item>
      <k-form-item label="人行三级分类">
        <k-field-select v-model="formData.pbnkTrdCtg" id="pbnkTrdCtg"
                        :data-allowblank="true"
                        :data-disabled="formData.pbnkTrdCtgDisabled"
                        :data-data="pbnkTrdCtgDict"
                        data-value-field="VALUE"
                        data-display-field="TEXT" />
      </k-form-item>
      <k-form-item label="人行四级分类">
        <k-field-select v-model="formData.pbnkFurCtg" id="pbnkFurCtg" :data-allowblank="false"
                        :data-disabled="formData.pbnkFurCtgDisabled"
                        data-dict="pbnkTrdCtg"/>
      </k-form-item>
      <k-form-item label="人行发行机构所属行业" >
        <k-field-select v-model="formData.pbnkIndustryIssuer" id="pbnkIndustryIssuer" :data-allowblank="false" :data-disabled="formData.pbnkIndustryIssuerDisabled" data-dict="isuOrgBlgIdt"/>
      </k-form-item>
      <k-form-item label="人行发行机构企业规模">
        <k-field-select v-model="formData.isuOrgTypSiz" id="isuOrgTypSiz" :data-allowblank="false" :data-disabled="formData.isuOrgTypSizDisabled" data-dict ="debtor_type" />
      </k-form-item>
      <k-form-item label="版本" >
        <k-field-text v-model="formData.version" id="version" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="备注" :data-col="2">
        <k-field-text v-model="formData.cmt" id="cmt" inputType="textarea" :rows="3" :data-disabled="formData.cmtDisabled" :data-allowblank="!(this.formData.isuOrgTypEcn === '99'||this.formData.isuOrgTypTchno === '99'||this.formData.isuOrgTypScaleSiz === '99')" :data-max-length="256"/>
      </k-form-item>

      <k-form-footer slot="footer" data-align="center" v-if="!isDetailShow">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="checkValues"
               data-from="blBondInfoModelForm" :data-model="formData" >
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
  name:"BondInfoModelCollection",
  props: {
    info : {
      type:Object,
      default: ()=>{
        return {}
      }
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
      //中债
      cbndFrsCtgDict:{},
      cbndScdCtgDict:{},
      //g06
      ggCbcTypeDict:{},
      ggCbcSubTypeDict:{},
      //人行
      pbnkFrsCtgDict: {},
      pbnkScdCtgDict:{},
      pbnkTrdCtgDict:{},
      pbnkFurCtgDict:{},
    };
  },
  created() {
    this.formData = this.info;
    //如果已经补录过（即存在版本号），则补录字段不需要默认值
    if (!this.formData.version){
      this.defaultParamDeal(this.formData);
    }
    if (this.disabledVal) {
      AssetCommon.checkColumn(this,'formData','Disabled','1','01',this.isDetailShow);
    }
    //中债字典处理
    AssetCommon.areaDict(this,'cbndFrsCtgDict','cbndFrsCtg','4',false);
    AssetCommon.areaDict(this,'cbndScdCtgDict','cbndScdCtg','1101,1102,1110,1111,1112,1113,1114,1115,1116,1117,2501,2502',false);
    //g06字典处理
    AssetCommon.areaDict(this,'ggCbcTypeDict','g06_first_type',"'1.4'",false);
    AssetCommon.areaDict(this, 'ggCbcSubTypeDict', 'g06_scd_type', this.formData.debtEquityClass==='02'?"'1.6.4'":"'1.4.1','1.4.2','1.4.3','1.4.4','1.4.5','1.4.6','1.4.7','1.4.8','1.4.9','1.4.10','1.4.11','1.4.12','1.2.1','1.2.2'",false);
    //人行字典处理
    AssetCommon.areaDict(this,'pbnkFrsCtgDict','asseFrsCtg','01',false);
    AssetCommon.areaDict(this,'pbnkScdCtgDict','pbnkFrsCtg',"'d'",false);
    AssetCommon.areaDict(this,'pbnkTrdCtgDict','pbnkScdCtg','d',true);
    // AssetCommon.areaDict(this,'pbnkFurCtgDict','pbnkTrdCtg','d',true);
  },
  methods: {
    //处理补录页面默认值
    defaultParamDeal(val){
      val.cbndFrsCtg=AssetCommon.dealDefaultVal(val.cbndFrsCtg,'4');
      val.ggCbcType=AssetCommon.dealDefaultVal(val.ggCbcType,'1.4');
      val.pbnkFrsCtg=AssetCommon.dealDefaultVal(val.pbnkFrsCtg,'01');
      val.pbnkScdCtg=AssetCommon.dealDefaultVal(val.pbnkScdCtg,'d');
    },
    MenuSelect(index) {
      this.activeMenu = index;
      this.scrollToTarget('BLXX');
    },
    scrollToTarget(id) {
      const target = document.getElementById(id);
      if (target) { target.scrollIntoView({ behavior: 'smooth' }); }
    },
    checkValues(val){
      if(!this.$refs.blBondInfoModelForm.validate()){
        return false;
      }
      AssetCommon.removeVifRubbish(this,'formData','blBondInfoModelForm',val);
      this.httpUtil.comnUpdate({
        action: "BondInfoModel.blBondInfoModel",
        params: val,
        successAlert: true
      }).then(data => {
        this.$emit('loadGriding',this.formData);
      });
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
