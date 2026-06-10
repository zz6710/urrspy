<template>
  <div>
    <k-form ref="editInfForm" :data-col="2" isFormBodyScreen>

      <div class ="tableLine2" ><span id="JCXX" class="leftText2">基础信息</span></div>


      <k-form-item label="证券编码" v-show="false">
        <k-field-text v-model="formData.scrId"  id="scrId" :data-disabled="formData.scrIdDisabled"/>
      </k-form-item>
      <k-form-item label="资管计划代码">
        <k-field-text v-model="formData.scrCd" id="scrCd" :data-allowblank="false" :data-disabled="formData.scrCdDisabled" :data-max-length="40"/>
      </k-form-item>
      <k-form-item label="资管计划名称">
        <k-field-text v-model="formData.scrNm" id="scrNm" :data-allowblank="false" :data-disabled="formData.scrNmDisabled" :data-max-length="128"/>
      </k-form-item>
      <k-form-item label="市场">
        <k-field-select v-model="formData.trxMkt" id="trxMkt" :data-allowblank="false" :data-disabled="formData.trxMktDisabled" data-dict ="market_asset" />
      </k-form-item>
      <k-form-item label="交易流通场所">
        <k-field-select  v-model="formData.trxPla" id="trxPla" :data-allowblank="false" data-dict="tacdingPlace" :data-disabled="formData.trxPlaDisabled"/>
      </k-form-item>
      <k-form-item label="成立日期">
        <k-field-date v-model="formData.setUpDt" id="setUpDt" :dataMaxValue="this.formData.mtuDt==''||this.formData.mtuDt==null?'20991231':this.formData.mtuDt+')'" :data-allowblank="false" :data-disabled="formData.setUpDtDisabled"/>
      </k-form-item>
      <k-form-item label="到期日期">
        <k-field-date v-model="formData.mtuDt" id="mtuDt" :dataMinValue="this.formData.setUpDt==''||this.formData.setUpDt==null||this.formData.setUpDt===undefined?'('+this.nowdate:'('+this.formData.setUpDt" :data-allowblank="false" :data-disabled="formData.mtuDtDisabled"/>
      </k-form-item>
      <k-form-item label="投资方式">
        <k-field-select v-model="formData.investWay" id="investWay" data-dict="investWay" :data-allowblank="false" :data-disabled="formData.investWayDisabled"/>
      </k-form-item>
      <k-form-item label="资金运用行业">
        <k-field-select v-model="formData.fndCrryIdt" id="fndCrryIdt" data-dict="isuOrgBlgIdt" :data-allowblank="false" :data-disabled="formData.fndCrryIdtDisabled"/>
      </k-form-item>
      <k-form-item label="管理人">
        <k-field-select v-model="formData.mng" id="mng" data-action="T8OrgSheet.findOrgNmAll" :dataRemote="true"
                        :data-params="{orgFullName:this.formData.mng}" data-value-field="orgNbrExt" data-display-field="orgFullName"
                        :data-allowblank="false" :data-disabled="formData.mngDisabled"/>
      </k-form-item>
      <k-form-item label="托管人">
        <k-field-select v-model="formData.cstd" id="cstd"  :data-allowblank="false" :data-disabled="formData.cstdDisabled" :data-max-length="200"
                        data-action="T8OrgSheet.findOrgNmAll" :data-params="{orgFullName:this.formData.cstd}" data-value-field="orgNbrExt" data-display-field="orgFullName"/>
      </k-form-item>
      <k-form-item label="是否有预期收益率">
        <k-field-select v-model="formData.expeRatF" id="expeRatF" data-dict="1yes2no" :data-default-value="'02'" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="资金实际投向">
        <k-field-select v-model="formData.fndActlDir" id="fndActlDir"
                        :data-allowblank="false"
                        :data-data="fndActlDirDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"
                        :data-disabled="formData.fndActlDirDisabled" />
      </k-form-item>

      <k-form-footer slot="footer" data-align="center" v-if="!isDetailShow">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="editInfForm"
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
import moment from "moment";

export default {
  name: "NetValSPVInfoEdit",
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
      fndActlDirDict : {},
      nextSetUpDt: '20231101',
      nowdate:'',//传递值使用
    };
  },
  created() {
    this.formData = this.info;
    if (this.disabledVal) {
      AssetCommon.checkColumn(this,'formData','Disabled','9','02',this.isDetailShow);
    }
    AssetCommon.areaDict(this,'fndActlDirDict','actual_invest_dir_fund','20,21,22,23',false);
    this.$nextTick(()=>{
      var now = new Date();
      var year=now.getFullYear();
      var month=now.getMonth()+1;
      var date=now.getDate();
      if(month<10){
        month = '0'+month
      }if(date<10){
        date = '0'+date
      }
      this.nowdate=year+""+month+""+date;

    });
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
      val.scrId=AssetCommon.dealDefaultVal(val.scrId,val.scrCd+"."+val.trxMkt+"."+"13");
    },

    submitHandler(val){
      if (!this.$refs.editInfForm.validate())
        return
      AssetCommon.removeVifRubbish(this,'formData','editInfForm',val);
      this.defaultParamDeal(val);
      this.httpUtil.comnUpdate({
        action: this.disabledVal?"NetValSPVInfoModel.updateNetValSPVInfoModel":"NetValSPVInfoModel.addNetValSPVInfoModel",
        params: val,
      }).then(data => {
        if (data.success === true) {
          this.$emit('loadGriding',val);
        }

      });
    },
    changeMtuDt(value){
      this.nextSetUpDt= moment(value).add(1,'days').format("yyyy-MM-DD");
    }
  },
}
</script>
<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
