<template>
  <div>
    <k-form ref="collectInfoModelForm" :data-col="2" isFormBodyScreen>


      <div class ="tableLine2" ><span id="BLXX" class="leftText2">补录信息</span></div>


      <k-form-item label="资产ID" v-show="false">
        <k-field-text v-model="formData.scrId"  id="scrId" :data-disabled="formData.scrIdDisabled"/>
      </k-form-item>
      <k-form-item label="是否通道投资">
        <k-field-select v-model="formData.isChannel" id="isChannel" :data-allowblank="false" data-dict="1yes2no"  :data-disabled="formData.isChannelDisabled"/>
      </k-form-item>
      <k-form-item label="通道" v-if="formData.isChannel==='01'">
        <k-field-select v-model="formData.channelNo"  data-action="BondInfoModel.findProd"
                        data-display-field="prodCode,prodName" data-value-field="prodCode" id="channelNo"
                        :data-allowblank="false" :data-disabled="formData.channelNoDisabled"/>
      </k-form-item>
      <k-form-item label="收/受益权类型" v-if="!(formData.cbndScdCtg==='1202'||
                                               formData.cbndScdCtg==='1203'||
                                               formData.cbndScdCtg==='1204'||
                                               formData.cbndScdCtg==='1206'||
                                               formData.cbndScdCtg==='1207'||
                                               formData.cbndScdCtg==='1208'||
                                               formData.cbndScdCtg==='1209'||
                                               formData.cbndScdCtg==='1211'||
                                               formData.cbndScdCtg==='1212'||
                                               formData.cbndScdCtg==='1213')">
        <k-field-select v-model="formData.incBenRitTyp" data-dict="incBenRitType"  :data-disabled="formData.incBenRitTypDisabled"
                        :data-allowblank="!(formData.cbndScdCtg==='1205')" id="incBenRitTyp"/>
      </k-form-item>
      <k-form-item label="是否属于买入返售">
        <k-field-select v-model="formData.buyBackF" data-dict="1yes2no" :data-allowblank="false" :data-disabled="formData.buyBackFDisabled" id="buyBackF"/>
      </k-form-item>
      <k-form-item label="份额面值" v-if="!(formData.cbndScdCtg==='2202')">
        <k-field-text v-model="formData.lotParVal"  :data-max-length="15" :data-digits="2"
                      :data-disabled="formData.lotParValDisabled"
                      :data-integer-length="12" :data-min-value="0"  data-show-gbmoney="true"
                      data-validate-type="money" data-type="money" id="lotParVal"
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
      <k-form-item label="法定到期日" v-if="!(formData.cbndScdCtg==='2202')">
        <k-field-date v-model="formData.staMtuDt" :data-disabled="formData.staMtuDtDisabled" id="staMtuDt"
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
      <k-form-item label="利息分布方式" v-if="!(formData.cbndScdCtg==='2202')">
        <k-field-select v-model="formData.intrAlcMth" data-dict="intrAlcMth" :data-disabled="formData.intrAlcMthDisabled"
                        id="intrAlcMth" :data-allowblank="!(formData.cbndScdCtg==='1202'||
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
      <k-form-item label="是否有浮动因子" v-if="!(formData.cbndScdCtg==='2202')">
        <k-field-select v-model="formData.fltFctF" data-dict="1yes2no" :data-disabled="formData.fltFctFDisabled" id="fltFctF"
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
      <k-form-item label="浮动因子（%）" v-if="!((formData.cbndScdCtg==='1202'||
                                        formData.cbndScdCtg==='1203'||
                                        formData.cbndScdCtg==='1204'||
                                        formData.cbndScdCtg==='1205'||
                                        formData.cbndScdCtg==='1206'||
                                        formData.cbndScdCtg==='1207'||
                                        formData.cbndScdCtg==='1208'||
                                        formData.cbndScdCtg==='1209'||
                                        formData.cbndScdCtg==='1211'||
                                        formData.cbndScdCtg==='1212'||
                                        formData.cbndScdCtg==='1213')&& formData.fltFctF==='02')&&!(formData.cbndScdCtg==='2202')">
        <k-field-text v-model="formData.fltFct"  :data-digits="5"  :data-integer-length="3"
                      data-validate-type="number" :data-disabled="formData.fltFctDisabled" id="fltFct"
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
                                        formData.cbndScdCtg==='1213')&& formData.fltFctF==='01')"/>
      </k-form-item>
      <k-form-item label="结构档次" v-if="!(formData.cbndScdCtg==='2202')">
        <k-field-select v-model="formData.strcGrd" data-dict="strcGrd" :data-disabled="formData.strcGrdDisabled" id="strcGrd"
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
      <k-form-item label="还本方式" v-if="!(formData.cbndScdCtg==='2202')">
        <k-field-select v-model="formData.payPrcpMth" data-dict="payPrcpMth" :data-disabled="formData.payPrcpMthDisabled" id="payPrcpMth"
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
      <k-form-item label="基础资产类型" v-if="!(formData.cbndScdCtg==='2202')">
        <k-field-text v-model="formData.basAstTyp" :data-max-length="200" :data-disabled="formData.basAstTypDisabled" id="basAstTyp"
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
      <k-form-item label="超额收益分配比例(%)" v-if="!(formData.cbndScdCtg==='2202')">
        <k-field-text v-model="formData.exsErnAlcRto" :data-digits="5"  :data-integer-length="3" :data-min-value="0"  :data-max-value="100"
                      data-validate-type="money" data-type="money"  :data-disabled="formData.exsErnAlcRtoDisabled" id="exsErnAlcRto"
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
      <k-form-item label="融资项目">
        <k-field-text v-model="formData.lvrgPrj" id="lvrgPrj" :data-allowblank="false" :data-max-length="200" :data-disabled="formData.lvrgPrjDisabled" />
      </k-form-item>
      <k-form-item label="项目归属重点监控行业和领域标识">
        <k-field-select v-model="formData.prjBlgKeyMntIdt" id="prjBlgKeyMntIdt" :data-allowblank="false" data-dict="1yes2no" :data-disabled="formData.prjBlgKeyMntIdtDisabled" />
      </k-form-item>
      <k-form-item label="重点监控行业和领域类别"  v-if="formData.prjBlgKeyMntIdt==='01'">
        <k-field-select v-model="formData.keyMntIdtTyp" id="keyMntIdtTyp" :data-allowblank="!(formData.prjBlgKeyMntIdt==='01')" data-dict="keyMntIdtTyp" :data-disabled="formData.keyMntIdtTypDisabled"/>
      </k-form-item>
      <k-form-item label="重点监控行业和领域类别说明" v-if="formData.prjBlgKeyMntIdt==='01'">
        <k-field-text v-model="formData.keyMntIdtTypCmt" id="keyMntIdtTypCmt" :data-max-length="256" :data-disabled="formData.keyMntIdtTypCmtDisabled"
                      :data-allowblank="!(formData.keyMntIdtTyp==='99')"/>
      </k-form-item>
      <k-form-item label="担保情况说明" v-if="!(formData.grntMth===null)">
        <k-field-text v-model="formData.grntStsCmt" id="grntStsCmt" :data-allowblank="!(formData.grntMth===null)" :data-max-length="200" :data-disabled="formData.grntStsCmtDisabled"/>
      </k-form-item>
      <k-form-item label="资产内部评级">
        <k-field-select v-model="formData.astInRat" id="astInRat" :data-allowblank="false" data-dict="mainRating" :data-disabled="formData.astInRatDisabled"/>
      </k-form-item>
      <k-form-item label="资产外部评级">
        <k-field-select v-model="formData.astOutRat" id="astOutRat" :data-allowblank="false" data-dict="mainRating" :data-disabled="formData.astOutRatDisabled"/>
      </k-form-item>
      <k-form-item label="行权周期" v-if="!(((formData.cbndScdCtg==='1202'||
                        formData.cbndScdCtg==='1203'||
                        formData.cbndScdCtg==='1204'||
                        formData.cbndScdCtg==='1205'||
                        formData.cbndScdCtg==='1206'||
                        formData.cbndScdCtg==='1207'||
                        formData.cbndScdCtg==='1208'||
                        formData.cbndScdCtg==='1209'||
                        formData.cbndScdCtg==='1211'||
                        formData.cbndScdCtg==='1212'||
                        formData.cbndScdCtg==='1213')&&(!(formData.embOptTyp===''||formData.embOptTyp===undefined||formData.embOptTyp==null)&& (formData.xcsRitMth==='01'||formData.xcsRitMth==='02'))
                        ||(formData.embOptTyp===''||formData.embOptTyp===undefined||formData.embOptTyp==null))||formData.cbndScdCtg==='2202')">
        <k-field-text v-model="formData.xcsRitPrd" :data-max-length="5" :data-integer-length="5"
                      :data-min-value="0" :data-disabled="formData.xcsRitPrdDisabled" id="xcsRitPrd"
                      data-type="INT" :data-allowblank="!((formData.cbndScdCtg==='1202'||
                        formData.cbndScdCtg==='1203'||
                        formData.cbndScdCtg==='1204'||
                        formData.cbndScdCtg==='1205'||
                        formData.cbndScdCtg==='1206'||
                        formData.cbndScdCtg==='1207'||
                        formData.cbndScdCtg==='1208'||
                        formData.cbndScdCtg==='1209')&& !(formData.embOptTyp==='') && formData.xcsRitMth==='03')"/>
      </k-form-item>
      <k-form-item label="永续条款类型" v-if="!(formData.cbndScdCtg==='2202')">
        <k-field-select v-model="formData.perpTyp" data-dict="perpTyp"  :data-disabled="formData.perpTypDisabled" id="perpTyp"
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
      <k-form-item label="利息递延条款类型" v-if="!(formData.cbndScdCtg==='2202')">
        <k-field-select v-model="formData.intrPpnTyp" data-dict="intrPpnTyp" :data-disabled="formData.intrPpnTypDisabled" id="intrPpnTyp"
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
      <k-form-item label="递延利息是否计息" v-if="!(((formData.cbndScdCtg==='1202'||
                        formData.cbndScdCtg==='1203'||
                        formData.cbndScdCtg==='1204'||
                        formData.cbndScdCtg==='1205'||
                        formData.cbndScdCtg==='1206'||
                        formData.cbndScdCtg==='1207'||
                        formData.cbndScdCtg==='1208'||
                        formData.cbndScdCtg==='1209'||
                        formData.cbndScdCtg==='1211'||
                        formData.cbndScdCtg==='1212'||
                        formData.cbndScdCtg==='1213')&& formData.intrPpnTyp==='01')||formData.cbndScdCtg==='2202')">
        <k-field-select v-model="formData.ppnIntrIntrF" data-dict="1yes2no" :data-disabled="formData.ppnIntrIntrFDisabled" id="ppnIntrIntrF"
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
                        formData.cbndScdCtg==='1213')&&(formData.intrPpnTyp==='02'||formData.intrPpnTyp==='03'))" />
      </k-form-item>
      <k-form-item label="首次重定价日期" v-if="!(((formData.cbndScdCtg==='1202'||
                        formData.cbndScdCtg==='1203'||
                        formData.cbndScdCtg==='1204'||
                        formData.cbndScdCtg==='1205'||
                        formData.cbndScdCtg==='1206'||
                        formData.cbndScdCtg==='1207'||
                        formData.cbndScdCtg==='1208'||
                        formData.cbndScdCtg==='1209'||
                        formData.cbndScdCtg==='1211'||
                        formData.cbndScdCtg==='1212'||
                        formData.cbndScdCtg==='1213')&& formData.perpTyp==='03')||formData.cbndScdCtg==='2202')">
        <k-field-date v-model="formData.frsRprcDt" id="frsRprcDt" :data-disabled="formData.frsRprcDtDisabled"
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
                        formData.cbndScdCtg==='1213')&&(formData.perpTyp==='01'||formData.perpTyp==='02'))"/>
      </k-form-item>
      <k-form-item label="重定价周期" v-if="!(((formData.cbndScdCtg==='1202'||
                        formData.cbndScdCtg==='1203'||
                        formData.cbndScdCtg==='1204'||
                        formData.cbndScdCtg==='1205'||
                        formData.cbndScdCtg==='1206'||
                        formData.cbndScdCtg==='1207'||
                        formData.cbndScdCtg==='1208'||
                        formData.cbndScdCtg==='1209'||
                        formData.cbndScdCtg==='1211'||
                        formData.cbndScdCtg==='1212'||
                        formData.cbndScdCtg==='1213')&&formData.perpTyp==='03')||formData.cbndScdCtg==='2202')">
        <k-field-text v-model="formData.rprcPrd"  :data-max-length="5" :data-disabled="formData.rprcPrdDisabled" id="rprcPrd"
                      :data-integer-length="5" data-type="INT" :data-allowblank="!((formData.cbndScdCtg==='1202'||
                        formData.cbndScdCtg==='1203'||
                        formData.cbndScdCtg==='1204'||
                        formData.cbndScdCtg==='1205'||
                        formData.cbndScdCtg==='1206'||
                        formData.cbndScdCtg==='1207'||
                        formData.cbndScdCtg==='1208'||
                        formData.cbndScdCtg==='1209'||
                        formData.cbndScdCtg==='1211'||
                        formData.cbndScdCtg==='1212'||
                        formData.cbndScdCtg==='1213')&&(formData.perpTyp==='01'||formData.perpTyp==='02'))"/>
      </k-form-item>
      <k-form-item label="部分赎回标识" v-if="!(formData.cbndScdCtg==='2202')">
        <k-field-select v-model="formData.partRdmF" :data-disabled="formData.partRdmFDisabled" data-dict="1yes2no" id="partRdmF"
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
      <k-form-item label="部分赎回比例" v-if="!(formData.cbndScdCtg==='2202')">
        <k-field-text v-model="formData.partRdmRto"  data-digits="5"  :data-integer-length="3" id="partRdmRto"
                      :data-min-value="0" :data-disabled="formData.partRdmRtoDisabled"
                      data-validate-type="number"
                      :data-allowblank="(formData.cbndScdCtg==='1202'||
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
      <k-form-item label="选择权">
        <k-field-text v-model="formData.chcRit" :data-allowblank="false" :data-max-length="60" id="chcRit" :data-disabled="formData.chcRitDisabled"/>
      </k-form-item>
      <k-form-item label="行权条件说明">
        <k-field-text v-model="formData.xcsRitCondCmt" :data-allowblank="false" :data-max-length="200" id="xcsRitCondCmt"
                      :data-disabled="formData.xcsRitCondCmtDisabled"/>
      </k-form-item>
      <k-form-item label="融资总费率(%)">
        <k-field-text v-model="formData.lvrgTotFee" :data-allowblank="false" id="lvrgTotFee"
                      :data-disabled="formData.lvrgTotFeeDisabled" :data-digits="5"  :data-integer-length="3" data-placeholder="%" :data-min-value="0"
                      data-validate-type="money" data-type="money" :data-max-value="100"/>
      </k-form-item>
      <k-form-item label="融资项目所属行业">
        <k-field-select v-model="formData.lvrgPrjBlgIdt" data-dict="isuOrgBlgIdt" id="lvrgPrjBlgIdt" :data-disabled="formData.lvrgPrjBlgIdtDisabled"
                        :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="付息计划" >
        <k-field-text v-model="formData.payPlan"  :data-allowblank="false" id="payPlan" :data-max-length="100" :data-disabled="formData.payPlanDisabled"/>
      </k-form-item>
      <k-form-item label="还本计划" >
        <k-field-text v-model="formData.repayPlan" :data-allowblank="false" id="repayPlan" :data-max-length="100" :data-disabled="formData.repayPlanDisabled"/>
      </k-form-item>
      <k-form-item label="投向">
        <k-field-select v-model="formData.mmActualDirect" data-dict="isuOrgBlgIdt" id="mmActualDirect" :data-allowblank="false"
                        :data-disabled="formData.mmActualDirectDisabled" />
      </k-form-item>
      <k-form-item label="首次行权日期" v-if="!((((formData.cbndScdCtg==='1202'||
                        formData.cbndScdCtg==='1203'||
                        formData.cbndScdCtg==='1204'||
                        formData.cbndScdCtg==='1205'||
                        formData.cbndScdCtg==='1206'||
                        formData.cbndScdCtg==='1207'||
                        formData.cbndScdCtg==='1208'||
                        formData.cbndScdCtg==='1209'||
                        formData.cbndScdCtg==='1211'||
                        formData.cbndScdCtg==='1212'||
                        formData.cbndScdCtg==='1213')&& !(formData.embOptTyp==='') && (formData.xcsRitMth==='02'||formData.xcsRitMth==='01'))||
                        ((formData.cbndScdCtg==='1202'||
                        formData.cbndScdCtg==='1203'||
                        formData.cbndScdCtg==='1204'||
                        formData.cbndScdCtg==='1205'||
                        formData.cbndScdCtg==='1206'||
                        formData.cbndScdCtg==='1207'||
                        formData.cbndScdCtg==='1208'||
                        formData.cbndScdCtg==='1209'||
                        formData.cbndScdCtg==='1211'||
                        formData.cbndScdCtg==='1212'||
                        formData.cbndScdCtg==='1213')&& formData.embOptTyp===''))||formData.cbndScdCtg==='2202')">
        <k-field-date v-model="formData.frsFixXcsDt" :data-disabled="formData.frsFixXcsDtDisabled" id="frsFixXcsDt"
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
                        formData.cbndScdCtg==='1213')&& !(formData.embOptTyp==='') && formData.xcsRitMth==='03')" />
      </k-form-item>
      <k-form-item label="中债发行机构所属行业" >
        <k-field-select v-model="formData.ccIndustryIssuer" id="ccIndustryIssuer" :data-disabled="formData.ccIndustryIssuerDisabled"
                        :data-allowblank="true" data-dict="isuOrgBlgIdt"/>
      </k-form-item>
      <k-form-item label="中债发行机构类型（按规模划分）">
        <k-field-select v-model="formData.isuOrgTypScaleSiz" id="isuOrgTypScaleSiz" data-dict="debtor_scale_type" :data-allowblank="false"
                        :data-disabled="formData.isuOrgTypScaleSizDisabled"/>
      </k-form-item>
      <k-form-item label="G06一级分类">
        <k-field-select v-model="formData.ggCbcType"
                        id="ggCbcType"
                        :data-allowblank="false"
                        :data-disabled="formData.ggCbcTypeDisabled"
                        :data-data="ggCbcTypeDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"/>
      </k-form-item>
      <k-form-item label="G06二级分类">
        <k-field-select v-model="formData.ggCbcSubType" id="ggCbcSubType"
                        :data-allowblank="false"
                        @data-on-change="changeGgCbcSubType"
                        :data-disabled="formData.ggCbcSubTypeDisabled"
                        :data-data="ggCbcSubTypeDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE" />
      </k-form-item>
      <k-form-item label="G06三级分类" v-if="this.formData.ggCbcSubType==='1.5.1'||this.formData.ggCbcSubType==='1.5.16'">
        <k-field-select v-model="formData.ggCbcTrdType" id="ggCbcTrdType"
                        :data-allowblank="false"
                        :data-disabled="formData.ggCbcSubTypeDisabled"
                        :data-data="ggCbcTrdTypeDict"
                        data-display-field="TEXT"
                        data-value-field="VALUE"/>
      </k-form-item>
      <k-form-item label="人行一级分类">
        <k-field-select v-model="formData.pbnkFrsCtg"
                        id="pbnkFrsCtg"
                        data-dict="asseFrsCtg"
                        :data-disabled="formData.pbnkFrsCtgDisabled"
                        :data-allowblank="false"/>

      </k-form-item>
      <k-form-item label="人行二级分类">

        <k-field-select v-model="formData.pbnkScdCtg"
                        id="pbnkScdCtg"
                        data-dict="pbnkFrsCtg"
                        :data-disabled="formData.pbnkScdCtgDisabled"
                        :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="人行三级分类">
        <k-field-select v-model="formData.pbnkTrdCtg"
                        data-dict="pbnkScdCtg"
                        id="pbnkTrdCtg"
                        :data-allowblank="false"
                        :data-disabled="formData.pbnkTrdCtgDisabled" />
      </k-form-item>
      <k-form-item label="人行四级分类">
        <k-field-select v-model="formData.pbnkFurCtg"
                        data-dict="pbnkTrdCtg"
                        id="pbnkFurCtg"
                        :data-allowblank="false"
                        :data-disabled="formData.pbnkFurCtgDisabled"
                        />
      </k-form-item>
      <k-form-item label="人行发行机构所属行业" >
        <k-field-select v-model="formData.pbnkIndustryIssuer" :data-allowblank="false" id="pbnkIndustryIssuer" :data-disabled="formData.pbnkIndustryIssuerDisabled" data-dict="subm_isuOrgBlgIdt"/>
      </k-form-item>
      <k-form-item label="人行发行机构企业规模">
        <k-field-select v-model="formData.isuOrgTypSiz" :data-allowblank="false" id="isuOrgTypSiz" :data-disabled="formData.isuOrgTypSizDisabled" data-dict ="debtor_type" />
      </k-form-item>
      <k-form-item label="版本" >
        <k-field-text v-model="formData.version" id="version" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="备注" :data-col="2">
        <k-field-text v-model="formData.cmt" :data-allowblank="!(formData.incBenRitTyp==='99'||formData.trxPla==='99'
                                    ||formData.bchmRatTyp==='99'||!(formData.lvrgTotFee==='0')||formData.plgTyp==='99'||formData.grntLvrgRel==='99'
                                    ||formData.lvrgTypEcn==='99'||formData.lvrgTypTchno==='99'||formData.lvrgTypSiz==='99')"
                      :data-max-length="256" :data-disabled="formData.cmtDisabled" inputType="textarea" :rows="3" id="cmt"/>
      </k-form-item>

      <k-form-footer slot="footer" data-align="center" v-if="!isDetailShow">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="NonStandInfoModel.updateNonStandInfoModelBl" data-from="collectInfoModelForm"
               :data-model="formData" :data-after-success="submitHandle" :data-handler="checkValues">
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
  name:"NonStandInfoModelCollection",
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
      formData: {},
      //g06
      ggCbcTypeDict:{},
      ggCbcSubTypeDict:{},
      ggCbcTrdTypeDict:{},
      //人行
      pbnkFrsCtgDict: {},
      pbnkScdCtgDict:{},
      pbnkTrdCtgDict:{},
      pbnkFurCtgDict:{},
    };
  },
  created() {
    this.formData = this.info;
    if(this.formData.prjBlgKeyMntIdt===''||this.formData.prjBlgKeyMntIdt==null||this.formData.prjBlgKeyMntIdt===undefined){
      this.$set(this.formData,"prjBlgKeyMntIdt","02");
    }
    //如果已经补录过（即存在版本号），则补录字段不需要默认值
    if (!this.formData.version){
      this.defaultParamDeal(this.formData);
    }
    if (this.disabledVal) {
      AssetCommon.checkColumn(this,'formData','Disabled','12','01',this.isDetailShow);
    }
    //g06字典处理
    AssetCommon.areaDict(this,'ggCbcTypeDict','g06_first_type',"'1.5'",false);
    AssetCommon.areaDict(this, 'ggCbcSubTypeDict', 'g06_scd_type', '1.5',true);
    var sadkjh = this.formData.ggCbcSubType==='1.5.1'?"'1.5.1.a'":(this.formData.ggCbcSubType==='1.5.16'?"'1.5.16.a','1.5.16.b'":'1.5.1');
    var sadkjh12 = !(this.formData.ggCbcSubType === '1.5.1' || this.formData.ggCbcSubType === '1.5.16');
    AssetCommon.areaDict(this, 'ggCbcTrdTypeDict', 'g06_trd_type',
                  this.formData.ggCbcSubType==='1.5.1'?"'1.5.1.a'":(this.formData.ggCbcSubType==='1.5.16'?"'1.5.16.a','1.5.16.b'":'1.5.1'),
                          !(this.formData.ggCbcSubType === '1.5.1' || this.formData.ggCbcSubType === '1.5.16'));
    //人行字典处理
    AssetCommon.areaDict(this,'pbnkFrsCtgDict','asseFrsCtg','01',false);
    AssetCommon.areaDict(this,'pbnkScdCtgDict','pbnkFrsCtg',"'g'",false);
    AssetCommon.areaDict(this,'pbnkTrdCtgDict','pbnkScdCtg',"'e'",false);
    AssetCommon.areaDict(this,'pbnkFurCtgDict','pbnkTrdCtg','e1',true);
  },
  methods:{
    changeGgCbcSubType(){
      this.$set(this.formData,"ggCbcTrdType","");
      AssetCommon.areaDict(this, 'ggCbcTrdTypeDict', 'g06_trd_type',
        this.formData.ggCbcSubType==='1.5.1'?"'1.5.1.a'":(this.formData.ggCbcSubType==='1.5.16'?"'1.5.16.a','1.5.16.b'":'1.5.1'),
        !(this.formData.ggCbcSubType === '1.5.1' || this.formData.ggCbcSubType === '1.5.16'));
    },
    MenuSelect(index) {
      this.activeMenu = index;
      this.scrollToTarget('BLXX');
    },
    scrollToTarget(id) {
      const target = document.getElementById(id);
      if (target) { target.scrollIntoView({ behavior: 'smooth' }); }
    },
    //处理补录页面默认值
    defaultParamDeal(val){
      val.ggCbcType=AssetCommon.dealDefaultVal(val.ggCbcType,'1.5');
      val.pbnkFrsCtg=AssetCommon.dealDefaultVal(val.pbnkFrsCtg,'01');
      val.pbnkScdCtg=AssetCommon.dealDefaultVal(val.pbnkScdCtg,'g');
      val.pbnkTrdCtg=AssetCommon.dealDefaultVal(val.pbnkTrdCtg,'g2');
      val.pbnkFurCtg=AssetCommon.dealDefaultVal(val.pbnkFurCtg,'g28');
    },

    checkValues(val){
      if(!this.$refs.collectInfoModelForm.validate()){
        return false;
      }
      AssetCommon.removeVifRubbish(this,'formData','collectInfoModelForm',val);
      return val;
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
