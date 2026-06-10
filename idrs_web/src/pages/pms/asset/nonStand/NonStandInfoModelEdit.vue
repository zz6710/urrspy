<template>
  <div>
    <k-form ref="editInfoModelForm" :data-col="2" isFormBodyScreen>


      <div class ="tableLine2" ><span id="JCXX" class="leftText2">基础信息</span></div>


      <k-form-item label="资产编码" v-show="false">
        <k-field-text v-model="formData.scrId"  id="scrId" :data-disabled="formData.scrIdDisabled"/>
      </k-form-item>
      <k-form-item label="资产代码">
        <k-field-text v-model="formData.scrCd" id="scrCd" :data-allowblank="false" :data-max-length="40"  :data-disabled="formData.scrCdDisabled"/>
      </k-form-item>
      <k-form-item label="资产名称">
        <k-field-text v-model="formData.scrNm" id="scrNm" :data-allowblank="false" :data-max-length="256" :data-disabled="formData.scrNmDisabled"/>
      </k-form-item>
      <k-form-item label="市场" v-if="false">
        <k-field-select v-model="formData.trxMkt" id="trxMkt" :data-allowblank="false" :data-disabled="formData.trxMktDisabled" data-dict ="market_bond" />
      </k-form-item>
      <k-form-item label="交易流通场所">
        <k-field-select  v-model="formData.trxPla" id="trxPla" :data-allowblank="false" data-dict="tacdingPlace" :data-disabled="formData.trxPlaDisabled"/>
      </k-form-item>
      <k-form-item label="中债一级分类">
        <k-field-select v-model="formData.cbndFrsCtg"
                        :data-allowblank="false"
                        :data-disabled="formData.cbndFrsCtgDisabled"
                        :data-data="cbndFrsCtgDict"
                        :data-default-value="'5'"
                        id="cbndFrsCtg"
                        data-value-field="VALUE"
                        data-display-field="TEXT"/>
      </k-form-item>
      <k-form-item label="中债二级分类">
        <k-field-select v-model="formData.cbndScdCtg"
                        :data-data="cbndScdCtgDict"
                        :data-disabled="formData.cbndScdCtgDisabled"
                        id="cbndScdCtg"
                        :data-allowblank="false"
                        data-value-field="VALUE"
                        data-display-field="TEXT"/>
      </k-form-item>
      <k-form-item label="金额(元)" >
        <k-field-text v-model="formData.amt" id="amt"  :data-digits="2"  :data-integer-length="13" :data-min-value="0"  data-show-gbmoney="true"
                      data-validate-type="money" data-type="money"  :data-allowblank="false"
                      :data-disabled="formData.amtDisabled"/>
      </k-form-item>
      <k-form-item label="是否有预期收益率" >
        <k-field-select v-model="formData.expeRatF" id="expeRatF" :data-allowblank="false" data-dict="1yes2no" :data-disabled="formData.expeRatFDisabled"/>
      </k-form-item>
      <k-form-item label="项目收益率(%)"v-if="formData.expeRatF==='01'">
        <k-field-text v-model="formData.yld" id="yld" :data-allowblank="false" :data-max-value="100" data-validate-type="number"
                      :data-digits="5" :data-integer-length="3" data-placeholder="%" :data-min-value="0"
                      :data-disabled="formData.yldDisabled"/>
      </k-form-item>
      <k-form-item label="融资人">
        <k-field-select v-model="formData.lvrg" id="lvrg" :data-max-length="200" :data-allowblank="false" :data-disabled="formData.lvrgDisabled"
                      data-action="T8OrgSheet.findOrgNmAll" :dataRemote="true" :data-params="{orgFullName:this.formData.lvrg}" data-value-field="orgNbrExt" data-display-field="orgFullName"/>
      </k-form-item>
      <k-form-item label="计息类型"v-if="!(formData.cbndScdCtg==='2202')">
        <k-field-select v-model="formData.intrTyp" id="intrTyp" data-dict="interest_type" :data-disabled="formData.intrTypDisabled"
                        :data-allowblank="!(formData.cbndScdCtg==='1202'||
                                        formData.cbndScdCtg==='1203'||
                                        formData.cbndScdCtg==='1204'||
                                        formData.cbndScdCtg==='1205'||
                                        formData.cbndScdCtg==='1206'||
                                        formData.cbndScdCtg==='1207'||
                                        formData.cbndScdCtg==='1208'||
                                        formData.cbndScdCtg==='1209'||
                                        formData.cbndScdCtg==='1211'||
                                        formData.cbndScdCtg==='1212'||
                                        formData.cbndScdCtg==='1213')"/>
      </k-form-item>
      <k-form-item label="是否规则付息"v-if="!(formData.cbndScdCtg==='2202')">
        <k-field-select v-model="formData.rulPayIntrF" id="rulPayIntrF" data-dict="1yes2no" :data-disabled="formData.rulPayIntrFDisabled"
                        :data-allowblank="!(formData.cbndScdCtg==='1202'||
                                        formData.cbndScdCtg==='1203'||
                                        formData.cbndScdCtg==='1204'||
                                        formData.cbndScdCtg==='1205'||
                                        formData.cbndScdCtg==='1206'||
                                        formData.cbndScdCtg==='1207'||
                                        formData.cbndScdCtg==='1208'||
                                        formData.cbndScdCtg==='1209'||
                                        formData.cbndScdCtg==='1211'||
                                        formData.cbndScdCtg==='1212'||
                                        formData.cbndScdCtg==='1213')"/>
      </k-form-item>
      <k-form-item label="付息频率（个月/次）">
        <k-field-text v-model="formData.payIntrFrq" id="payIntrFrq" :data-allowblank="false"  :data-max-length="2"
                      :data-integer-length="2" :data-min-value="0" :data-disabled="formData.payIntrFrqDisabled"
                      data-type="INT"  data-placeholder="如六个月付息一次填数字“6”,到期一次性付息填”0“,无固定付息频率”99“"/>
      </k-form-item>
      <k-form-item label="还本付息情况说明">
        <k-field-text v-model="formData.payPrcpIntrStsCmt" id="payPrcpIntrStsCmt" :data-allowblank="false" :data-max-length="200"
                      :data-disabled="formData.payPrcpIntrStsCmtDisabled" data-placeholder="如无则填写“无”"/>
      </k-form-item>
      <k-form-item label="基准利率种类">
        <k-field-select v-model="formData.bchmRatTyp" id="bchmRatTyp" :data-allowblank="!(formData.intrTyp==='03'||formData.intrTyp==='05')" data-dict="bchmRatTyp"
                        :data-disabled="formData.bchmRatTypDisabled"/>
      </k-form-item>
      <k-form-item label="利差(%)">
        <k-field-text v-model="formData.sprd" id="sprd" :data-digits="5"  :data-integer-length="3" :data-disabled="formData.sprdDisabled"
                      data-validate-type="number"   />
      </k-form-item>
      <k-form-item label="分期还本条款标识"v-if="!(formData.cbndScdCtg==='2202')">
        <k-field-select v-model="formData.insPayPrcpF" id="insPayPrcpF" data-dict="insPayPrcpF"  :data-disabled="formData.insPayPrcpFDisabled"
                        :data-allowblank="!(formData.cbndScdCtg==='1202'||
                                        formData.cbndScdCtg==='1203'||
                                        formData.cbndScdCtg==='1204'||
                                        formData.cbndScdCtg==='1205'||
                                        formData.cbndScdCtg==='1206'||
                                        formData.cbndScdCtg==='1207'||
                                        formData.cbndScdCtg==='1208'||
                                        formData.cbndScdCtg==='1209'||
                                        formData.cbndScdCtg==='1211'||
                                        formData.cbndScdCtg==='1212'||
                                        formData.cbndScdCtg==='1213')"/>
      </k-form-item>
      <k-form-item label="担保方式">
        <k-field-select v-model="formData.grntMth" id="grntMth" :data-allowblank="true"  data-dict="grntWay" :data-disabled="formData.grntMthDisabled"/>
      </k-form-item>
      <k-form-item label="抵质押物类型"v-if="!(formData.grntMth==='01'||formData.grntMth==='02'||formData.grntMth==='05'||formData.grntMth==='99'||formData.grntMth==='')">
        <k-field-select v-model="formData.plgTyp" id="plgTyp"  data-dict="plgTyp" :data-disabled="formData.plgTypDisabled"
                        :data-allowblank="!(formData.grntMth==='03'||formData.grntMth==='04')"/>
      </k-form-item>
      <k-form-item label="抵质押物价值（元）"v-if="!(formData.grntMth==='01'||formData.grntMth==='02'||formData.grntMth==='05'||formData.grntMth==='99'||formData.grntMth==='')">
        <k-field-text v-model="formData.plgVal" id="plgVal" :data-allowblank="!(formData.grntMth==='03'||formData.grntMth==='04')"
                      :data-digits="2"  :data-integer-length="13"  data-show-gbmoney="true"
                      data-validate-type="money" data-type="money" :data-disabled="formData.plgValDisabled"/>
      </k-form-item>
      <k-form-item label="担保性质"v-if="!(formData.grntMth==='03'||formData.grntMth==='04'||formData.grntMth==='05'||formData.grntMth==='99'||formData.grntMth==='')">
        <k-field-select v-model="formData.grntChr" id="grntChr"  data-dict="grntChr" :data-disabled="formData.grntChrDisabled"
                        :data-allowblank="!(formData.grntMth==='01'||formData.grntMth==='02')"/>
      </k-form-item>
      <k-form-item label="担保人与融资人关系"v-if="!(formData.grntMth==='03'||formData.grntMth==='04'||formData.grntMth==='05'||formData.grntMth==='99'||formData.grntMth==='')">
        <k-field-select v-model="formData.grntLvrgRel" id="grntLvrgRel" :data-allowblank="!(formData.grntMth==='01'||formData.grntMth==='02')"
                        data-dict="grntLvrgRel" :data-disabled="formData.grntLvrgRelDisabled"/>
      </k-form-item>
      <k-form-item label="含权类型" v-if="!(formData.cbndScdCtg==='2202')">
        <k-field-select v-model="formData.embOptTyp" id="embOptTyp" data-dict="embOptTyp" :data-disabled="formData.embOptTypDisabled"
                        :data-allowblank="formData.cbndScdCtg==='1202'||
                        formData.cbndScdCtg==='1203'||
                        formData.cbndScdCtg==='1204'||
                        formData.cbndScdCtg==='1205'||
                        formData.cbndScdCtg==='1206'||
                        formData.cbndScdCtg==='1207'||
                        formData.cbndScdCtg==='1208'||
                        formData.cbndScdCtg==='1209'||
                        formData.cbndScdCtg==='1211'||
                        formData.cbndScdCtg==='1212'||
                        formData.cbndScdCtg==='1213'"/>
      </k-form-item>
      <k-form-item label="行权方式" v-if="!(((formData.cbndScdCtg==='1202'||
                        formData.cbndScdCtg==='1203'||
                        formData.cbndScdCtg==='1204'||
                        formData.cbndScdCtg==='1205'||
                        formData.cbndScdCtg==='1206'||
                        formData.cbndScdCtg==='1207'||
                        formData.cbndScdCtg==='1208'||
                        formData.cbndScdCtg==='1209'||
                        formData.cbndScdCtg==='1211'||
                        formData.cbndScdCtg==='1212'||
                        formData.cbndScdCtg==='1213') && (formData.embOptTyp===''||formData.embOptTyp===undefined||formData.embOptTyp==null))||formData.cbndScdCtg==='2202')">
        <k-field-select v-model="formData.xcsRitMth" id="xcsRitMth" data-dict="xcsRitMth"  :data-disabled="formData.xcsRitMthDisabled"
                        :data-allowblank="!((formData.cbndScdCtg==='1202'||
                        formData.cbndScdCtg==='1203'||
                        formData.cbndScdCtg==='1204'||
                        formData.cbndScdCtg==='1205'||
                        formData.cbndScdCtg==='1206'||
                        formData.cbndScdCtg==='1207'||
                        formData.cbndScdCtg==='1208'||
                        formData.cbndScdCtg==='1209'||
                        formData.cbndScdCtg==='1211'||
                        formData.cbndScdCtg==='1212'||
                        formData.cbndScdCtg==='1213') && !(formData.embOptTyp===''))"/>
      </k-form-item>
      <k-form-item label="固定行权日" v-if="!(((formData.cbndScdCtg==='1202'||
                        formData.cbndScdCtg==='1203'||
                        formData.cbndScdCtg==='1204'||
                        formData.cbndScdCtg==='1205'||
                        formData.cbndScdCtg==='1206'||
                        formData.cbndScdCtg==='1207'||
                        formData.cbndScdCtg==='1208'||
                        formData.cbndScdCtg==='1209'||
                        formData.cbndScdCtg==='1211'||
                        formData.cbndScdCtg==='1212'||
                        formData.cbndScdCtg==='1213')&& ((formData.embOptTyp===''||formData.embOptTyp===undefined||formData.embOptTyp==null)|| (
                       !(formData.embOptTyp===''||formData.embOptTyp===undefined||formData.embOptTyp==null) &&(formData.xcsRitMth==='01'||formData.xcsRitMth==='03')))
                        ) || formData.cbndScdCtg==='2202') ">
        <k-field-date v-model="formData.fixXcsRitDt" id="fixXcsRitDt" :data-disabled="formData.fixXcsRitDtDisabled"
                      :data-allowblank="!((formData.cbndScdCtg==='1202'||
                        formData.cbndScdCtg==='1203'||
                        formData.cbndScdCtg==='1204'||
                        formData.cbndScdCtg==='1205'||
                        formData.cbndScdCtg==='1206'||
                        formData.cbndScdCtg==='1207'||
                        formData.cbndScdCtg==='1208'||
                        formData.cbndScdCtg==='1209'||
                        formData.cbndScdCtg==='1211'||
                        formData.cbndScdCtg==='1212'||
                        formData.cbndScdCtg==='1213') && !(formData.embOptTyp==='') && formData.xcsRitMth==='02')"/>
      </k-form-item>
      <k-form-item label="行权价格(元)" v-if="!(formData.cbndScdCtg==='2202'||formData.embOptTyp===''||formData.embOptTyp===undefined||formData.embOptTyp==null)">
        <k-field-text v-model="formData.xcsRitPrc" id="xcsRitPrc"  :data-digits="4"  :data-integer-length="13"
                      data-validate-type="money" data-type="money" :data-disabled="formData.xcsRitPrcDisabled"
                      :data-allowblank="(formData.cbndScdCtg==='1202'||
                        formData.cbndScdCtg==='1203'||
                        formData.cbndScdCtg==='1204'||
                        formData.cbndScdCtg==='1205'||
                        formData.cbndScdCtg==='1206'||
                        formData.cbndScdCtg==='1207'||
                        formData.cbndScdCtg==='1208'||
                        formData.cbndScdCtg==='1209')&&(formData.embOptTyp==='01'||formData.embOptTyp==='04')"/>
      </k-form-item>
      <k-form-item label="增信机构代码" v-if="!(formData.cbndScdCtg==='2202')">
        <k-field-text v-model="formData.incCrdOrgCd" id="incCrdOrgCd" :data-max-length="18" :data-disabled="formData.incCrdOrgCdDisabled"
                      data-validate-type="code"
                      :data-allowblank="!(formData.cbndScdCtg==='1202'||
                        formData.cbndScdCtg==='1203'||
                        formData.cbndScdCtg==='1204'||
                        formData.cbndScdCtg==='1205'||
                        formData.cbndScdCtg==='1206'||
                        formData.cbndScdCtg==='1207'||
                        formData.cbndScdCtg==='1208'||
                        formData.cbndScdCtg==='1209'||
                        formData.cbndScdCtg==='1211'||
                        formData.cbndScdCtg==='1212'||
                        formData.cbndScdCtg==='1213')" />
      </k-form-item>
      <k-form-item label="增信机构名称" v-if="!(formData.cbndScdCtg==='2202')">
        <k-field-text v-model="formData.incCrdOrgNm" id="incCrdOrgNm" :data-max-length="200" :data-disabled="formData.incCrdOrgNmDisabled"
                      :data-allowblank="!(formData.cbndScdCtg==='1202'||
                        formData.cbndScdCtg==='1203'||
                        formData.cbndScdCtg==='1204'||
                        formData.cbndScdCtg==='1205'||
                        formData.cbndScdCtg==='1206'||
                        formData.cbndScdCtg==='1207'||
                        formData.cbndScdCtg==='1208'||
                        formData.cbndScdCtg==='1209'||
                        formData.cbndScdCtg==='1211'||
                        formData.cbndScdCtg==='1212'||
                        formData.cbndScdCtg==='1213')" />
      </k-form-item>
      <k-form-item label="融资人类型（按规模划分）">
        <k-field-select v-model="formData.lvrgTypSiz" id="lvrgTypSiz" data-dict="instituteTypeTech" :data-allowblank="false" :data-disabled="formData.lvrgTypSizDisabled"/>
      </k-form-item>
      <k-form-item label="融资人类型（按技术领域划分）">
        <k-field-select v-model="formData.lvrgTypTchno" id="lvrgTypTchno" data-dict="isuOrgTypTchno" :data-allowblank="false" :data-disabled="formData.lvrgTypTchnoDisabled"/>
      </k-form-item>
      <k-form-item label="融资人类型（按经济类型划分）">
        <k-field-select v-model="formData.lvrgTypEcn" id="lvrgTypEcn" data-dict="isuOrgTypEcn" :data-allowblank="false" :data-disabled="formData.lvrgTypEcnDisabled"/>
      </k-form-item>
      <k-form-item label="融资人内部信用评级">
        <k-field-select v-model="formData.lvrgInCrdRat" id="lvrgInCrdRat" :data-allowblank="false" data-dict="mainRating" :data-disabled="formData.lvrgInCrdRatDisabled"/>
      </k-form-item>
      <k-form-item label="外部评级机构及融资人评级">
        <k-field-text v-model="formData.outRatOrgAndLvrgRat" id="outRatOrgAndLvrgRat" :data-max-length="200" :data-disabled="formData.outRatOrgAndLvrgRatDisabled"/>
      </k-form-item>
      <k-form-item label="融资人主体评级">
        <k-field-select v-model="formData.grntMainRat" id="grntMainRat" data-dict="mainRating" :data-allowblank="false" :data-disabled="formData.grntMainRatDisabled"/>
      </k-form-item>
      <k-form-item label="融资人所属地区">
        <k-field-select v-model="formData.lvrgBlgZon" id="lvrgBlgZon" data-dict="prod_sale_area" :data-allowblank="false" :data-disabled="formData.lvrgBlgZonDisabled"/>
      </k-form-item>
      <k-form-item label="融资人组织机构（社会信用）代码">
        <k-field-text v-model="formData.lvrgOrgOrgCd" id="lvrgOrgOrgCd" :data-allowblank="false" :data-max-length="18" :data-disabled="formData.lvrgOrgOrgCdDisabled"
                      data-validate-type="code"  />
      </k-form-item>
      <k-form-item label="起息日期">
        <k-field-date v-model="formData.valDt" id="valDt" :data-allowblank="false" :data-disabled="formData.valDtDisabled" :data-max-value="formData.valDtmax" @data-on-change="changeIntrDt"/>
      </k-form-item>
      <k-form-item label="首次付息日">
        <k-field-date v-model="formData.frsPayIntrDt" id="frsPayIntrDt" :data-allowblank="false" :data-disabled="formData.frsPayIntrDtDisabled"
                      :data-min-value="formData.valDt" :data-max-value="formData.mtuDt"/>
      </k-form-item>
      <k-form-item label="到期日期">
        <k-field-date v-model="formData.mtuDt" id="mtuDt" :data-allowblank="false" :data-disabled="formData.mtuDtDisabled" :data-min-value="formData.mtuDtmin" @data-on-change="changeIntrDt"/>
      </k-form-item>
      <k-form-item label="付息频率">
        <k-field-select v-model="formData.payinterestFreq" id="payinterestFreq" data-dict="payIntrFrqAll" :data-allowblank="false" :data-disabled="formData.payinterestFreqDisabled"@data-on-change="changePayInterestFreq"/>
      </k-form-item>
      <k-form-item label="计息基础">
        <k-field-select v-model="formData.intrBas" id="intrBas" :data-allowblank="false" data-dict="nonIntrBas" :data-disabled="formData.intrBasDisabled"/>
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
    action: {
      type:Object,
    },

  },
  data() {
    return {
      formData: {},
      //中债
      cbndFrsCtgDict:{},
      cbndScdCtgDict:{},
    };
  },
  created() {
    this.formData = this.info
    console.log(this.formData.embOptTyp==null);
    if (this.disabledVal) {
      AssetCommon.checkColumn(this,'formData','Disabled','12','02',this.isDetailShow);
    }
    this.getAction();
    //中债字典处理
    AssetCommon.areaDict(this,'cbndFrsCtgDict','cbndFrsCtg','5',false);
    AssetCommon.areaDict(this,'cbndScdCtgDict','cbndScdCtg','1201,1210,1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213,2101,2202,1299',false);
  },
  methods: {
    //处理补录页面默认值
    defaultParamDeal(val){
      val.scrId=AssetCommon.dealDefaultVal(val.scrId,val.scrCd+"."+"3"+"."+"5");
    },

    MenuSelect(index) {
      this.activeMenu = index;
      this.scrollToTarget('JCXX');
    },
    scrollToTarget(id) {
      const target = document.getElementById(id);
      if (target) { target.scrollIntoView({ behavior: 'smooth' }); }
    },
    changePayInterestFreq(){
      if(this.formData.payinterestFreq==='01'){
        this.$set(this.formData,"frsPayIntrDt",this.formData.mtuDt);
      }
    },

    changeIntrDt(){

      if(this.formData.valDt!=''&&this.formData.valDt!=undefined&&this.formData.valDt!=null) {
        this.$set(this.formData, "mtuDtmin", '('+this.formData.valDt );
      }
      if(this.formData.mtuDt!=''&&this.formData.mtuDt!=undefined&&this.formData.mtuDt!=null) {
        this.$set(this.formData, "valDtmax", this.formData.mtuDt +')');
      }
      console.log(this.formData);
      if (this.formData.mtuDt<this.formData.frsPayIntrDt){
        this.$set(this.formData,"frsPayIntrDt","")
      }
      if (this.formData.valDt>this.formData.frsPayIntrDt){
        this.$set(this.formData,"frsPayIntrDt","")
      }
      if(this.formData.payinterestFreq==='01'){
        this.$set(this.formData,"frsPayIntrDt",this.formData.mtuDt);
      }
    },
    getAction(){
      if (this.disabledVal){
        this.action = "NonStandInfoModel.updateNonStandInfoModel"
        return;
      }
      this.action = "NonStandInfoModel.addNonStandInfoModel"
    },
    checkValues(value) {
      if(!this.$refs.editInfoModelForm.validate())
        return false;
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
