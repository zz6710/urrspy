<template>
  <div>
    <k-form ref="editMidTrmDpsInfForm" :data-col="2" isFormBodyScreen>

      <div class ="tableLine2" ><span class="leftText2">基础信息</span></div>

      <k-form-item label="证券编码" v-show="false">
        <k-field-text v-model="formData.scrId"  id="scrId" :data-disabled="formData.scrIdDisabled"/>
      </k-form-item>
      <k-form-item label="存款代码">
        <k-field-text v-model="formData.scrCd" id="scrCd" :data-allowblank="false" :data-disabled="formData.scrCdDisabled" :data-max-length="32"/>
      </k-form-item>
      <k-form-item label="存款名称">
        <k-field-text v-model="formData.scrNm" id="scrNm" :data-allowblank="false" :data-disabled="formData.scrNmDisabled" :data-max-length="200"/>
      </k-form-item>
      <k-form-item label="交易流通场所">
        <k-field-select v-model="formData.trxMkt" id="trxMkt" :data-allowblank="false" :data-disabled="formData.trxMktDisabled"
                        :data-data="tacdingPlaceDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE" :data-default-value="'99'"/>
      </k-form-item>
      <k-form-item label="存款金额(元)">
        <k-field-text v-model="formData.dpsAmt" data-validate-type="money" data-type="money" id="dpsAmt"
                      data-min-value="0" data-show-gbmoney="true" :data-disabled="formData.dpsAmtDisabled"
                      :data-integer-length="14" data-digits="2" :data-allowblank="false" data-placeholder="单位(元)"/>
      </k-form-item>
      <k-form-item label="币种" v-if="false">
        <k-field-select v-model="formData.ccy" :data-allowblank="false" id="ccy" :data-disabled="formData.ccyDisabled" data-dict="cur_type" :data-default-value="'CNY'"/>
      </k-form-item>
      <k-form-item label="存款年利率(%)">
        <k-field-text v-model="formData.anlYld" id="anlYld" data-max-value="100" data-validate-type="number" :data-disabled="formData.anlYldDisabled"
                      data-min-value="0"  :data-allowblank="false" :data-integer-length="12" :data-digits="4" />
      </k-form-item>
      <!--      <k-form-item label="存款账号" v-if="false">-->
      <!--        <k-field-text v-model="formData.dpsActNbr" :data-max-length="30" :data-allowblank="false"/>-->
      <!--      </k-form-item>-->
      <k-form-item label="起息日期">
        <k-field-date v-model="formData.valDt" id="valDt" :dataMaxValue="formData.mtuDt" :data-allowblank="false" :data-disabled="formData.valDtDisabled"/>
      </k-form-item>
      <k-form-item label="到期日期">
        <k-field-date v-model="formData.mtuDt" id="mtuDt" :dataMinValue="'('+formData.valDt" :data-allowblank="false" :data-disabled="formData.mtuDtDisabled"/>
      </k-form-item>
      <k-form-item label="计息基础">
        <k-field-select v-model="formData.intrBas" id="intrBas" data-dict="intr_base" :data-allowblank="false" :data-disabled="formData.intrBasDisabled"/>
      </k-form-item>
      <k-form-item label="存款类型">
        <k-field-select v-model="formData.dpsTyp" id="dpsTyp" data-dict="deposit_type"  :data-allowblank="false" :data-disabled="formData.dpsTypDisabled"/>
      </k-form-item>
      <k-form-item label="存款银行">
        <k-field-text v-model="formData.dpsBnk" id="dpsBnk" :data-allowblank="false" :data-max-length="256" :data-disabled="formData.dpsBnkDisabled"/>
      </k-form-item>
      <k-form-footer slot="footer" data-align="center" v-if="!isDetailShow">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="editMidTrmDpsInfForm"
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
  name: "MidTrmDpsInfEdit",
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
      tacdingPlaceDict: {},
      newLabel:[],
    };
  },

  methods: {
    //处理补录页面默认值
    defaultParamDeal(val){
      val.scrId=AssetCommon.dealDefaultVal(val.scrId,val.scrCd+"."+val.trxMkt+"."+"1");
      val.ggCbcSubType=AssetCommon.dealDefaultVal(val.dpsBnk==="南京银行股份有限公司"?'1.1.2':'','1.1.3');
    },

    submitHandler(val){
      if (!this.$refs.editMidTrmDpsInfForm.validate())
        return
      AssetCommon.removeVifRubbish(this,'formData','editMidTrmDpsInfForm',val);
      this.defaultParamDeal(val);
      this.httpUtil.comnUpdate({
        action: this.disabledVal?"MidTrmDpsInf.updateMidTrmDpsInf":"MidTrmDpsInf.addMidTrmDpsInf",
        params: val,
      }).then(data => {
        if (data.success === true) {
          this.$emit('loadGriding',val);
        }

      });
    }
  },
  created() {
    this.formData = this.info;
    if (this.disabledVal) {
      AssetCommon.checkColumn(this,'formData','Disabled','13','02',this.isDetailShow);
    }
    AssetCommon.areaDict(this,'tacdingPlaceDict','tacdingPlace','99',false);
  }


}
</script>
<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
