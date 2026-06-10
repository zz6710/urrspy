<template>
  <div>
    <k-form ref="blMidTrmDpsInfForm" :data-col="2" isFormBodyScreen>

      <div class ="tableLine2" ><span class="leftText2">补录信息</span></div>

      <k-form-item label="证券编码" v-show="false">
        <k-field-text v-model="formData.scrId"  id="scrId" :data-disabled="formData.scrIdDisabled" />
      </k-form-item>
      <k-form-item label="付息频率">
        <k-field-select v-model="formData.payinterestFreq" id="payinterestFreq" data-dict="payIntrFrq" :data-allowblank="true" :data-disabled="formData.payinterestFreqDisabled"/>
      </k-form-item>
      <k-form-item label="中债一级分类">
        <k-field-select v-model="formData.cbndFrsCtg" id="cbndFrsCtg"
                        :data-disabled="formData.cbndFrsCtgDisabled"
                        :data-data="cbndFrsCtgDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"/>
      </k-form-item>
      <k-form-item label="中债二级分类">
        <k-field-select v-model="formData.cbndScdCtg" id="cbndScdCtg"
                        :data-disabled="formData.cbndScdCtgDisabled"
                        :data-data="cbndScdCtgDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"/>
      </k-form-item>

      <k-form-item label="挂钩标的类别" v-if="(formData.cbndScdCtg==='1002'||formData.cbndScdCtg==='1003')&&formData.dpsTyp==='06'">
        <k-field-select v-model="formData.lnkSbjMatTyp" id="lnkSbjMatTyp" data-dict="stru_deposit_type" :data-allowblank="false" :data-disabled="formData.lnkSbjMatTypDisabled" :data-multiple="true"/>
      </k-form-item>
      <k-form-item label="挂钩标的" v-if="(formData.cbndScdCtg==='1002'||formData.cbndScdCtg==='1003')&&formData.dpsTyp==='06'">
        <k-field-text v-model="formData.lnkSbjMat" id="lnkSbjMat" data-name="bbStructDeposit" :data-max-length="256"  data-placeholder="请按照“资产实际名称”+“资产代码”格式填写" :data-allowblank="false"
                      :data-disabled="formData.lnkSbjMatDisabled"/>
      </k-form-item>

      <k-form-item label="G06一级分类">
        <k-field-select v-model="formData.ggCbcType" id="ggCbcType"
                        :data-disabled="formData.ggCbcTypeDisabled"
                        :data-data="ggCbcTypeDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"/>
      </k-form-item>
      <k-form-item label="G06二级分类">
        <k-field-select v-model="formData.ggCbcSubType" id="ggCbcSubType"
                        :data-data="ggCbcSubTypeDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        :data-disabled="formData.ggCbcSubTypeDisabled"/>
      </k-form-item>
      <k-form-item label="人行一级分类">
        <k-field-select v-model="formData.pbnkFrsCtg" id="pbnkFrsCtg"
                        :data-allowblank="false"
                        :data-disabled="formData.pbnkFrsCtgDisabled"
                        :data-data="pbnkFrsCtgDict"
                        data-value-field="VALUE"
                        data-display-field="TEXT"/>
      </k-form-item>
      <k-form-item label="人行二级分类">
        <k-field-select v-model="formData.pbnkScdCtg" id="pbnkScdCtg"
                        :data-allowblank="false"
                        :data-disabled="formData.pbnkScdCtgDisabled"
                        :data-data="pbnkScdCtgDict"
                        data-value-field="VALUE"
                        data-display-field="TEXT"/>
      </k-form-item>
      <k-form-item label="人行三级分类">
        <k-field-select v-model="formData.pbnkTrdCtg" id="pbnkTrdCtg"
                        :data-allowblank="false"
                        :data-disabled="formData.pbnkTrdCtgDisabled"
                        @data-on-change="changePbnkTrdCtg"
                        :data-data="pbnkTrdCtgDict"
                        data-value-field="VALUE"
                        data-display-field="TEXT"/>
      </k-form-item>
      <k-form-item label="人行四级分类">
        <k-field-select v-model="formData.pbnkFurCtg" id="pbnkFurCtg"
                        :data-allowblank="false"
                        :data-disabled="formData.pbnkFurCtgDisabled"
                        :data-data="pbnkFurCtgDict"
                        data-value-field="VALUE"
                        data-display-field="TEXT"/>
      </k-form-item>
      <k-form-item label="版本" >
        <k-field-text v-model="formData.version" id="version" :data-disabled="true"/>
      </k-form-item>
      <k-form-footer slot="footer" data-align="center" v-if="!isDetailShow">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="blMidTrmDpsInfForm"
               :data-model="formData" :data-handler="submitHandler">
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
  name: "MidTrmDpsInfCollection",
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
  methods: {

    changePbnkTrdCtg(){
      this.$set(this.formData,"pbnkFurCtg","");
      AssetCommon.areaDict(this,'pbnkFurCtgDict','pbnkTrdCtg',this.formData.pbnkTrdCtg?this.formData.pbnkTrdCtg:'b1',true);
    },
    //处理补录页面默认值
    defaultParamDeal(val){
      val.ggCbcSubType=AssetCommon.dealDefaultVal(val.dpsBnk==="南京银行股份有限公司"?'1.1.2':'','1.1.3');
      val.cbndFrsCtg=AssetCommon.dealDefaultVal(val.cbndFrsCtg,'1');
      val.cbndScdCtg=AssetCommon.dealDefaultVal(val.dpsBnk==="南京银行股份有限公司"?'1002':'','1003');
      val.ggCbcType=AssetCommon.dealDefaultVal(val.ggCbcType,'1.1');
      val.pbnkFrsCtg=AssetCommon.dealDefaultVal(val.pbnkFrsCtg,'01');
      val.pbnkScdCtg=AssetCommon.dealDefaultVal(val.pbnkScdCtg,'b');
      val.pbnkTrdCtg=AssetCommon.dealDefaultVal(val.pbnkTrdCtg,'b1');
      val.pbnkFurCtg=AssetCommon.dealDefaultVal(val.pbnkFurCtg,'b12');
      val.payinterestFreq=AssetCommon.dealDefaultVal(val.payinterestFreq,'5');
    },

    submitHandler(val){
      if (!this.$refs.blMidTrmDpsInfForm.validate())
        return
      AssetCommon.removeVifRubbish(this,'formData','blMidTrmDpsInfForm',val);
      this.httpUtil.comnUpdate({
        action: "MidTrmDpsInf.updateMidTrmSupplyDpsInf",
        params: val,
      }).then(data => {
        if (data.success === true) {
          this.$emit('loadGriding',val);
        }

      });
    },
  },
  created() {
    this.formData = this.info;
    //如果已经补录过（即存在版本号），则补录字段不需要默认值
    if (!this.formData.version){
      this.defaultParamDeal(this.formData);
    }
    if (this.disabledVal) {
      AssetCommon.checkColumn(this,'formData','Disabled','13','01',this.isDetailShow);
    }
    //中债字典处理
    AssetCommon.areaDict(this,'cbndFrsCtgDict','cbndFrsCtg','1',false);
    AssetCommon.areaDict(this,'cbndScdCtgDict','cbndScdCtg','1002,1003',false);
    //g06字典处理
    AssetCommon.areaDict(this,'ggCbcTypeDict','g06_first_type',"'1.1'",false);
    AssetCommon.areaDict(this, 'ggCbcSubTypeDict', 'g06_scd_type', "'1.1.2','1.1.3'",false);
    //人行字典处理
    AssetCommon.areaDict(this,'pbnkFrsCtgDict','asseFrsCtg','01',false);
    AssetCommon.areaDict(this,'pbnkScdCtgDict','pbnkFrsCtg',"'b'",false);
    AssetCommon.areaDict(this,'pbnkTrdCtgDict','pbnkScdCtg','b',true);
    AssetCommon.areaDict(this,'pbnkFurCtgDict','pbnkTrdCtg',this.formData.pbnkTrdCtg?this.formData.pbnkTrdCtg:'b1',true);
  }


}
</script>
<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
