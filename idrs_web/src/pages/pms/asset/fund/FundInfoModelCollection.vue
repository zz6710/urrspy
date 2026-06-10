<template>
  <div>
    <k-form ref="collectFundInfoModelForm" :data-col="2" isFormBodyScreen>

      <div class ="tableLine2" ><span class="leftText2">补录信息</span></div>

      <k-form-item label="资产编码" v-show="false">
        <k-field-text v-model="formData.scrId"  id="scrId" :data-disabled="formData.scrIdDisabled"/>
      </k-form-item>
      <k-form-item label="发行份额(亿份)">
        <k-field-text v-model="formData.isuLot" id="isuLot" :data-allowblank="false" data-validate-type="money"  :data-disabled="formData.isuLotDisabled"
                      data-type="money" data-digits="4"  data-integer-length="9" data-min-value="(0"  data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="运作方式">
        <k-field-select v-model="formData.runMth" id="runMth"
                        :data-allowblank="false"
                        :data-data="trunMthDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        :data-disabled="formData.runMthDisabled" />
      </k-form-item>
      <k-form-item label="基金登记编码">
        <k-field-text v-model="formData.fndProdRegEnc" id="fndProdRegEnc" :data-allowblank="false" :data-disabled="formData.fndProdRegEncDisabled" :data-max-length="15"/>
      </k-form-item>
      <k-form-item label="发行机构编码">
        <k-field-text v-model="formData.fndOrgEnc" id="fndOrgEnc" :data-allowblank="false" :data-disabled="formData.fndOrgEncDisabled" :data-max-length="14"/>
      </k-form-item>
      <k-form-item label="发行成立日">
        <k-field-date v-model="formData.setUpDt" id="setUpDt" :data-allowblank="true" :data-disabled="formData.setUpDtDisabled" />
      </k-form-item>
      <k-form-item label="基金投资资产">
        <k-field-text v-model="formData.fndInvAst" id="fndInvAst" :data-allowblank="false" :data-max-length="400" :data-disabled="formData.fndInvAstDisabled" />
      </k-form-item>
      <k-form-item label="外部资讯分类">
        <k-field-select v-model="formData.assInfClass" id="assInfClass" data-dict="assInfClassFund"  :data-disabled="formData.assInfClassDisabled"/>
      </k-form-item>
      <k-form-item label="中债一级分类">
        <k-field-select  v-model="formData.cbndFrsCtg" id="cbndFrsCtg"
                         :data-disabled="formData.cbndFrsCtgDisabled"
                         :data-allowblank="false"
                         :data-data="cbndFrsCtgDict"
                         data-display-field="TEXT"
                         data-value-field="VALUE"/>
      </k-form-item>
      <k-form-item label="中债二级分类">
        <k-field-select v-model="formData.cbndScdCtg" id="cbndScdCtg"
                        :data-data="cbndScdCtgDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        :data-allowblank="false"
                        :data-disabled="formData.cbndScdCtgDisabled"/>
      </k-form-item>
      <k-form-item label="G06一级分类">
        <k-field-select v-model="formData.ggCbcType" id="ggCbcType"
                        :data-allowblank="false"
                        :data-disabled="formData.ggCbcTypeDisabled"
                        :data-data="ggCbcTypeDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"/>
      </k-form-item>
      <k-form-item label="G06二级分类">
        <k-field-select v-model="formData.ggCbcSubType" id="ggCbcSubType"
                        :data-allowblank="false"
                        :data-disabled="formData.ggCbcSubTypeDisabled"
                        :data-data="ggCbcSubTypeDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE" />
      </k-form-item>
      <k-form-item label="人行一级分类">
        <k-field-select v-model="formData.pbnkFrsCtg"
                        id="pbnkFrsCtg"
                        :data-data="pbnkFrsCtgDict"
                        data-value-field="VALUE"
                        data-display-field="TEXT"
                        :data-disabled="formData.pbnkFrsCtgDisabled"
                        :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="人行二级分类">
        <k-field-select v-model="formData.pbnkScdCtg"
                        id="pbnkScdCtg"
                        :data-disabled="formData.pbnkScdCtgDisabled"
                        :data-data="pbnkScdCtgDict"
                        data-value-field="VALUE"
                        data-display-field="TEXT"
                        :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="人行三级分类">
        <k-field-select v-model="formData.pbnkTrdCtg"
                        @data-on-change="changePbnkTrdCtg"
                        :data-data="pbnkTrdCtgDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        id="pbnkTrdCtg"
                        :data-allowblank="false"
                        :data-disabled="formData.pbnkTrdCtgDisabled" />
      </k-form-item>
      <k-form-item label="人行四级分类" v-if="this.formData.pbnkTrdCtg!=='g3'">
        <k-field-select v-model="formData.pbnkFurCtg"
                        :data-data="pbnkFurCtgDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        id="pbnkFurCtg"
                        :data-allowblank="false"
                        :data-disabled="formData.pbnkFurCtgDisabled" />
      </k-form-item>
      <k-form-item label="版本" >
        <k-field-text v-model="formData.version" id="version" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="备注" :data-col="2">
        <k-field-text v-model="formData.cmt" id="cmt" inputType="textarea" :rows="3" :data-allowblank="true" :data-disabled="formData.cmtDisabled" :data-max-length="256"/>
      </k-form-item>
      <k-form-footer slot="footer" data-align="center" v-if="!isDetailShow">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="collectFundInfoModelForm"
               :data-model="formData" data-target="fundInfoModelGrid" :data-handler="submitHandler" >
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
  props: {
    info: {
      type: Object,
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
      trunMthDict:{},
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
      AssetCommon.checkColumn(this,'formData','Disabled','3','01',this.isDetailShow);
    }
    AssetCommon.areaDict(this,'trunMthDict','operation_mode','1,3',false);
    //中债字典处理
    AssetCommon.areaDict(this,'cbndFrsCtgDict','cbndFrsCtg','11,8',false);
    AssetCommon.areaDict(this,'cbndScdCtgDict','cbndScdCtg','1505,1106,1303,2401,2402,2403,2499',false);
    //g06字典处理
    AssetCommon.areaDict(this,'ggCbcTypeDict','g06_first_type',"'1.11','1.8'",false);
    AssetCommon.areaDict(this, 'ggCbcSubTypeDict', 'g06_scd_type', '1.11',true);
    //人行字典处理
    AssetCommon.areaDict(this,'pbnkFrsCtgDict','asseFrsCtg','01',false);
    AssetCommon.areaDict(this,'pbnkScdCtgDict','pbnkFrsCtg',"'g'",false);
    AssetCommon.areaDict(this,'pbnkTrdCtgDict','pbnkScdCtg','g',true);
    AssetCommon.areaDict(this,'pbnkFurCtgDict','pbnkTrdCtg',this.formData.pbnkTrdCtg?this.formData.pbnkTrdCtg:'g',true);
  },
  methods: {

    changePbnkTrdCtg(){
      this.$set(this.formData,"pbnkFurCtg","");
      AssetCommon.areaDict(this,'pbnkFurCtgDict','pbnkTrdCtg',this.formData.pbnkTrdCtg?this.formData.pbnkTrdCtg:"g",true);
    },
    //处理补录页面默认值
    defaultParamDeal(val){
      val.cbndFrsCtg=AssetCommon.dealDefaultVal(val.cbndFrsCtg,'11');
      val.ggCbcType=AssetCommon.dealDefaultVal(val.ggCbcType,'1.11');
      val.pbnkFrsCtg=AssetCommon.dealDefaultVal(val.pbnkFrsCtg,'01');
      val.pbnkScdCtg=AssetCommon.dealDefaultVal(val.pbnkScdCtg,'g');
      val.pbnkTrdCtg=AssetCommon.dealDefaultVal(val.pbnkTrdCtg,'g2');
      val.pbnkFurCtg=AssetCommon.dealDefaultVal(val.pbnkFurCtg,'g28');
    },
    submitHandle(value) {
      this.$emit('loadGriding', this.formData);
    },
    submitHandler(val){
      if (!this.$refs.collectFundInfoModelForm.validate())
        return
      AssetCommon.removeVifRubbish(this,'formData','collectFundInfoModelForm',val);
      this.httpUtil.comnUpdate({
        action: "FundInfoModel.updateFundInfoBl",
        params: val,
      }).then(data => {
        if (data.success === true) {
          this.$emit('loadGriding',val);
        }

      });
    },
  },
}
</script>

<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
