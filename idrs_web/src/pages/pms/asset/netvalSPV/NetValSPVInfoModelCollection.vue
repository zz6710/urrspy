<template>
  <div>
    <k-form ref="collectNetValSPVInfoModelForm" :data-col="2" isFormBodyScreen>

      <div class ="tableLine2" ><span id="BLXX" class="leftText2">补录信息</span></div>


      <k-form-item label="资产编码" v-show="false">
        <k-field-text v-model="formData.scrId"  id="scrId" :data-disabled="formData.scrIdDisabled"/>
      </k-form-item>
      <k-form-item label="金额(元)">
        <k-field-text v-model="formData.amt" data-validate-type="money" data-type="money" id="amt"
                      data-min-value="(0" data-show-gbmoney="true" :data-disabled="formData.amtDisabled"
                      :data-integer-length="13" data-digits="2" :data-allowblank="false" data-placeholder="单位(元)"/>
      </k-form-item>
      <k-form-item label="资金运用方式">
        <k-field-text v-model="formData.fndCrryMth" id="fndCrryMth" :data-max-length="300" :data-allowblank="false" :data-disabled="formData.fndCrryMthDisabled"/>
      </k-form-item>
      <k-form-item label="资管计划属性">
        <k-field-select v-model="formData.astMngPlanPrpt" id="astMngPlanPrpt" data-dict="astMngPlanPrpt" :data-allowblank="false" :data-disabled="formData.astMngPlanPrptDisabled"/>
      </k-form-item>
      <k-form-item label="购买结构">
        <k-field-select v-model="formData.buyStrc" id="buyStrc" :data-allowblank="false" data-dict="buyStrc" :data-disabled="formData.buyStrcDisabled"/>
      </k-form-item>
      <k-form-item label="管理方式">
        <k-field-select v-model="formData.mngMth" id="mngMth" :data-allowblank="false" data-dict="mngMth" :data-disabled="formData.mngMthDisabled"/>
      </k-form-item>
      <k-form-item label="管理费率(%)">
        <k-field-text v-model="formData.mngFeeTat" id="mngFeeTat" :data-allowblank="false" data-validate-type="number" :data-digits="5" :data-integer-length="3" :data-disabled="formData.mngFeeTatDisabled"/>
      </k-form-item>
      <k-form-item label="托管费率(%)">
        <k-field-text v-model="formData.trstFeeTat" id="trstFeeTat" :data-allowblank="false" data-validate-type="number" :data-digits="5" :data-integer-length="3" :data-disabled="formData.trstFeeTatDisabled"/>
      </k-form-item>
      <k-form-item label="交易相关合计费率(%)">
        <k-field-text v-model="formData.trxRelSmrFeeRat" id="trxRelSmrFeeRat" :data-allowblank="false" data-validate-type="number" :data-digits="5" :data-integer-length="3" :data-disabled="formData.trxRelSmrFeeRatDisabled"/>
      </k-form-item>
      <k-form-item label="中介服务机构合计费率(%)">
        <k-field-text v-model="formData.medAgnSrvOrgSmrFeeRat" id="medAgnSrvOrgSmrFeeRat" :data-allowblank="false" data-validate-type="number" :data-digits="5" :data-integer-length="3" :data-disabled="formData.medAgnSrvOrgSmrFeeRatDisabled"/>
      </k-form-item>
      <k-form-item label="其他合计费率(%)">
        <k-field-text v-model="formData.othSmrFeeRat" id="othSmrFeeRat" :data-allowblank="false" data-validate-type="number" :data-digits="5" :data-integer-length="3" :data-disabled="formData.othSmrFeeRatDisabled"/>
      </k-form-item>
      <k-form-item label="资管计划发起人机构编码 ">
        <k-field-text v-model="formData.isuOrgEnc" id="isuOrgEnc" :data-allowblank="false" :data-disabled="formData.isuOrgEncDisabled" :data-max-length="32"/>
      </k-form-item>
      <k-form-item label="SPV机构编码 ">
        <k-field-text v-model="formData.spvOrgEnc" id="spvOrgEnc" :data-allowblank="false" :data-disabled="formData.spvOrgEncDisabled" :data-max-length="14"/>
      </k-form-item>
      <k-form-item label="SPV产品登记编码">
        <k-field-text v-model="formData.spvProdRegEnc" id="spvProdRegEnc" :data-allowblank="false" :data-disabled="formData.spvProdRegEncDisabled" :data-max-length="256"/>
      </k-form-item>
      <k-form-item label="中债一级分类">
        <k-field-select v-model="formData.cbndFrsCtg" id="cbndFrsCtg"
                        :data-allowblank="false"
                        :data-disabled="formData.cbndFrsCtgDisabled"
                        :data-data="cbndFrsCtgDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE" />
      </k-form-item>
      <k-form-item label="中债二级分类">
        <k-field-select v-model="formData.cbndScdCtg" id="cbndScdCtg"
                        :data-allowblank="false"
                        :data-disabled="formData.cbndScdCtgDisabled"
                        :data-data="cbndScdCtgDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE" />
      </k-form-item>
      <k-form-item label="是否为银行理财产品" v-if="this.formData.cbndScdCtg==='1705'">
        <k-field-select v-model="formData.bnkInvProdF" id="bnkInvProdF"  :data-allowblank="false" data-dict="1yes2no" :data-disabled="formData.bnkInvProdFDisabled"/>
      </k-form-item>
      <k-form-item label="是否由金融资产投资公司发行" v-if="this.formData.cbndScdCtg==='1705'">
        <k-field-select v-model="formData.finAstInvCmpIsuF" id="finAstInvCmpIsuF"  :data-allowblank="false" data-dict="1yes2no" :data-disabled="formData.finAstInvCmpIsuFDisabled"/>
      </k-form-item>
      <k-form-item label="产品登记编码" v-if="formData.bnkInvProdF==='01'" >
        <k-field-text v-model="formData.prodRegEnc" id="prodRegEnc" :data-allowblank="false" :data-disabled="formData.prodRegEncDisabled"  :data-max-length="15" />
      </k-form-item>
      <k-form-item label="是否投向金融公司私募产品">
        <k-field-select v-model="formData.isFinIsuF" id="isFinIsuF" :data-allowblank="false" data-dict="1yes2no" :data-disabled="formData.isFinIsuFDisabled" />
      </k-form-item>
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
      <k-form-item label="人行一级分类">
        <k-field-select v-model="formData.pbnkFrsCtg"  id="pbnkFrsCtg"
                        :data-allowblank="true"
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
                        data-display-field="TEXT"
                        data-value-field="VALUE" />
      </k-form-item>
      <k-form-item label="人行四级分类">
        <k-field-select v-model="formData.pbnkFurCtg" id="pbnkFurCtg"
                        :data-allowblank="false"
                        :data-disabled="formData.pbnkFurCtgDisabled"
                        :data-data="pbnkFurCtgDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"/>
      </k-form-item>
      <k-form-item label="版本" >
        <k-field-text v-model="formData.version" id="version" :data-disabled="true"/>
      </k-form-item>

      <k-form-footer slot="footer" data-align="center" v-if="!isDetailShow">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="collectNetValSPVInfoModelForm"
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
  name: "NetValSPVInfoModelCollection",
  props: {
    info : {
      type:Object,
      default:()=>{
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
      formData: {
      },
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
    if(this.formData.cbndScdCtg===null||this.formData.cbndScdCtg===''){
          if(this.formData.sambusorgtyp==='07'){
            this.$set(this.formData,"cbndScdCtg","1701");
          }
          if(this.formData.sambusorgtyp==='08'){
            console.log('2');
            this.$set(this.formData,"cbndScdCtg","1703");
          }
          if(this.formData.sambusorgtyp==='09'){
            this.$set(this.formData,"cbndScdCtg","1704");
          }
          if(this.formData.sambusorgtyp==='10'){
            this.$set(this.formData,"cbndScdCtg","1706");
          }
          if(this.formData.sambusorgtyp==='11'){
            this.$set(this.formData,"cbndScdCtg","1702");
          }
          if(this.formData.sambusorgtyp!='07'&&this.formData.sambusorgtyp!='08'&&this.formData.sambusorgtyp!='09'&&this.formData.sambusorgtyp!='10'&&this.formData.sambusorgtyp!='11'){
            this.$set(this.formData,"cbndScdCtg","1705");
          }
        }
    console.log(this.formData);
    if (this.disabledVal) {
      AssetCommon.checkColumn(this,'formData','Disabled','9','01',this.isDetailShow);
    }
    //中债字典处理
    AssetCommon.areaDict(this,'cbndFrsCtgDict','cbndFrsCtg','13',false);
    AssetCommon.areaDict(this,'cbndScdCtgDict','cbndScdCtg','17',true);
    //g06字典处理
    AssetCommon.areaDict(this,'ggCbcTypeDict','g06_first_type',"'1.13'",false);
    AssetCommon.areaDict(this, 'ggCbcSubTypeDict', 'g06_scd_type', '1.13',true);
    //人行字典处理
    AssetCommon.areaDict(this,'pbnkFrsCtgDict','asseFrsCtg','01',false);
    AssetCommon.areaDict(this,'pbnkScdCtgDict','pbnkFrsCtg',"'g'",false);
    AssetCommon.areaDict(this,'pbnkTrdCtgDict','pbnkScdCtg',"'g2'",false);
    AssetCommon.areaDict(this,'pbnkFurCtgDict','pbnkTrdCtg',this.formData.pbnkTrdCtg?this.formData.pbnkTrdCtg:'g2',true);
  },
  methods: {
    changePbnkTrdCtg(){
      this.$set(this.formData,"pbnkFurCtg","");
      AssetCommon.areaDict(this,'pbnkFurCtgDict','pbnkTrdCtg',this.formData.pbnkTrdCtg?this.formData.pbnkTrdCtg:"g2",true);
    },

    //处理补录页面默认值
    defaultParamDeal(val){
      val.cbndFrsCtg=AssetCommon.dealDefaultVal(val.cbndFrsCtg,'13');
      val.ggCbcType=AssetCommon.dealDefaultVal(val.ggCbcType,'1.13');
      val.pbnkFrsCtg=AssetCommon.dealDefaultVal(val.pbnkFrsCtg,'01');
      val.pbnkScdCtg=AssetCommon.dealDefaultVal(val.pbnkScdCtg,'g');
      val.pbnkTrdCtg=AssetCommon.dealDefaultVal(val.pbnkTrdCtg,'g2');
    },
    MenuSelect(index) {
      this.activeMenu = index;
      this.scrollToTarget('BLXX');
    },
    scrollToTarget(id) {
      const target = document.getElementById(id);
      if (target) { target.scrollIntoView({ behavior: 'smooth' }); }
    },
    submitHandler(val){
      if (!this.$refs.collectNetValSPVInfoModelForm.validate())
        return
      AssetCommon.removeVifRubbish(this,'formData','collectNetValSPVInfoModelForm',val);
      this.httpUtil.comnUpdate({
        action: "NetValSPVInfoModel.updateNetValSPVInfoModelBl",
        params: val,
      }).then(data => {
        if (data.success === true) {
          this.$emit('loadGriding',val);
        }

      });
    },

  },
};
</script>
<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
