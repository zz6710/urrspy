<template>
  <div>
    <k-form ref="blMidAssAsharedescriptionForm" :data-col="2" isFormBodyScreen>

      <div class ="tableLine2" ><span class="leftText2">补录信息</span></div>


      <k-form-item label="证券编码" v-show="false">
        <k-field-text v-model="formData.scrId"  id="scrId" :data-disabled="formData.scrIdDisabled" />
      </k-form-item>
      <k-form-item label="股票类型">
        <k-field-select v-model="formData.stockType" id="stockType" data-dict="stock_type" :data-disabled="formData.stockTypeDisabled"/>
      </k-form-item>
      <k-form-item label="机构类型（按规模划分）">
        <k-field-select v-model="formData.isuOrgTypSiz" id="isuOrgTypSiz" data-dict="debtor_scale_type"  :data-allowblank="false" :data-disabled="formData.isuOrgTypSizDisabled"/>
      </k-form-item>
      <k-form-item label="机构类型（按技术领域划分）">
        <k-field-select v-model="formData.isuOrgTypTchno" id="isuOrgTypTchno" data-dict="isuOrgTypTchno"  :data-allowblank="false" :data-disabled="formData.isuOrgTypTchnoDisabled"/>
      </k-form-item>
      <k-form-item label="机构类型（按经济类型分）">
        <k-field-select v-model="formData.isuOrgTypEcn" id="isuOrgTypEcn" data-dict="isuOrgTypEcn"  :data-allowblank="false"  :data-disabled="formData.isuOrgTypEcnDisabled"/>
      </k-form-item>
      <k-form-item label="机构所属行业（中债）">
        <k-field-select v-model="formData.industryIssuer" id="industryIssuer" :data-allowblank="false" data-dict="isuOrgBlgIdt" :data-disabled="formData.industryIssuerDisabled"/>
      </k-form-item>
      <k-form-item label="外部资讯分类">
        <k-field-select v-model="formData.assInfClass" id="assInfClass" data-dict="assInfClassWb"  :data-allowblank="false"  :data-disabled="formData.assInfClassDisabled"/>
      </k-form-item>
      <k-form-item label="中债一级分类">
        <k-field-select v-model="formData.cbndFrsCtg" id="cbndFrsCtg"
                        :data-data="cbndFrsCtgDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        :data-disabled="formData.cbndFrsCtgDisabled"/>
      </k-form-item>
      <k-form-item label="中债二级分类">
        <k-field-select v-model="formData.cbndScdCtg" id="cbndScdCtg"
                        :data-data="cbndScdCtgDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        :data-disabled="formData.cbndScdCtgDisabled"/>
      </k-form-item>

      <k-form-item label="G06一级分类">
        <k-field-select v-model="formData.ggCbcType" id="ggCbcType"  :data-allowblank="false" :data-disabled="formData.ggCbcTypeDisabled"
                        :data-data="ggCbcTypeDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE" />
      </k-form-item>
      <k-form-item label="G06二级分类">
        <k-field-select v-model="formData.ggCbcSubType" id="ggCbcSubType"
                        :data-allowblank="false"
                        :data-data="ggCbcSubTypeDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        :data-disabled="formData.ggCbcSubTypeDisabled"/>
      </k-form-item>

      <k-form-item label="人行一级分类">
        <k-field-select v-model="formData.pbnkFrsCtg" id="pbnkFrsCtg"  :data-disabled="formData.pbnkFrsCtgDisabled"
                        :data-data="pbnkFrsCtgDict"
                        data-value-field="VALUE"
                        data-display-field="TEXT"/>
      </k-form-item>
      <k-form-item label="人行二级分类">
        <k-field-select v-model="formData.pbnkScdCtg" id="pbnkScdCtg"
                        :data-data="pbnkScdCtgDict"
                        data-value-field="VALUE"
                        data-display-field="TEXT"  :data-disabled="formData.pbnkScdCtgDisabled"/>
      </k-form-item>
      <k-form-item label="人行三级分类">
        <k-field-select v-model="formData.pbnkTrdCtg" id="pbnkTrdCtg"
                        :data-data="pbnkTrdCtgDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE" :data-disabled="formData.pbnkTrdCtgDisabled"/>
      </k-form-item>
      <k-form-item label="投资阶段" v-if="false">
        <k-field-select v-model="formData.investmentType" id="investmentType" data-dict="invest_stage" :data-disabled="formData.investmentTypeDisabled"/>
      </k-form-item>
      <k-form-item label="股权退出安排" v-if="false" >
        <k-field-date v-model="formData.sharehold" id="sharehold" :data-disabled="formData.shareholdDisabled"/>
      </k-form-item>
      <k-form-item label="是否为质押融资" v-if="false">
        <k-field-select v-model="formData.pledgedFinace" id="pledgedFinace"  data-dict="1yes2no" :data-disabled="formData.pledgedFinaceDisabled"/>
      </k-form-item>
      <k-form-item label="是否为债转股" v-if="false">
        <k-field-select v-model="formData.debtEquitySwap" id="debtEquitySwap"  data-dict="1yes2no" :data-disabled="formData.debtEquitySwapDisabled"/>
      </k-form-item>
      <k-form-item label="版本" >
        <k-field-text v-model="formData.version" id="version" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="备注"  :data-col="2">
        <k-field-text v-model="formData.cmt" id="cmt" inputType="textarea" :rows="3"
                      :data-allowblank="!(formData.isuOrgTypSiz === '99'||formData.isuOrgTypTchno === '99'||
                      formData.isuOrgTypEcn === '99'||formData.industryIssuer==='99'||formData.stockType==='99'||this.formData.trxPla==='99')" :data-disabled="formData.cmtDisabled"/>
      </k-form-item>
      <k-form-footer slot="footer" data-align="center" v-if="!isDetailShow">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="blMidAssAsharedescriptionForm"
               :data-model="formData" :data-handler="submitHandler" >
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
  name: "MidAssAsharedescriptionCont",
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
    //处理补录页面默认值
    defaultParamDeal(val){
      val.cbndFrsCtg=AssetCommon.dealDefaultVal(val.cbndFrsCtg,'6');
      val.ggCbcType=AssetCommon.dealDefaultVal(val.ggCbcType,'1.6');
      val.pbnkFrsCtg=AssetCommon.dealDefaultVal(val.pbnkFrsCtg,'01');
      val.pbnkScdCtg=AssetCommon.dealDefaultVal(val.pbnkScdCtg,'g');
      val.pbnkTrdCtg=AssetCommon.dealDefaultVal(val.pbnkTrdCtg,'g3');
      val.stockType=AssetCommon.dealDefaultVal(val.stockType,'01');
    },
    submitHandler(val){
      if (!this.$refs.blMidAssAsharedescriptionForm.validate())
      return
      AssetCommon.removeVifRubbish(this,'formData','blMidAssAsharedescriptionForm',val);
      this.httpUtil.comnUpdate({
        action: 'MidAssAsharedescription.updateMidAssSupplyAsharedescription',
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
    if(this.disabledVal){
      AssetCommon.checkColumn(this,'formData','Disabled','5','01',this.isDetailShow);
    }
    //中债字典处理
    AssetCommon.areaDict(this,'cbndFrsCtgDict','cbndFrsCtg','6',false);
    AssetCommon.areaDict(this,'cbndScdCtgDict','cbndScdCtg','1301,1305,1302,1399',false);
    //g06字典处理
    AssetCommon.areaDict(this,'ggCbcTypeDict','g06_first_type',"'1.6'",false);
    AssetCommon.areaDict(this, 'ggCbcSubTypeDict', 'g06_scd_type', "'1.6.2','1.6.3','1.6.4'",false);
    //人行字典处理
    AssetCommon.areaDict(this,'pbnkFrsCtgDict','asseFrsCtg','01',false);
    AssetCommon.areaDict(this,'pbnkScdCtgDict','pbnkFrsCtg',"'g'",false);
    AssetCommon.areaDict(this,'pbnkTrdCtgDict','pbnkScdCtg',"'g3'",false);
  }
}
</script>
<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
