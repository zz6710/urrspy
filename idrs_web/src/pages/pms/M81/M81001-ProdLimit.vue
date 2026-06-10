<template>
  <div>
    <k-form class="my-form" ref="limitInfoBase" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">

      <k-form-item label="产品代码" v-show="false">
        <k-field-text v-model="t8ProdLimit.prodCode"  :data-default-value="this.prodCode"/>
      </k-form-item>
      <k-form-item label="产品主表id" v-show="false">
        <k-field-text v-model="t8ProdLimit.t8ProdInfoId" />
      </k-form-item>

      <k-form-item label="销售对象">
        <!--<k-field-checkbox v-model="t8ProdLimit.prodSaleCustom" data-dict="prod_sale_custom_fb" :data-allowblank="false"
                          @data-on-change="showProdLimit" v-if="prodMode == '1'">
        </k-field-checkbox>-->
        <k-field-checkbox v-model="t8ProdLimit.prodSaleCustom" data-dict="prod_sale_custom" :data-allowblank="false"
                          @data-on-change="showProdLimit">
        </k-field-checkbox>
      </k-form-item>

      <k-form-item label="首次投资认定标准">
        <k-field-select   v-model="t8ProdLimit.firstInvest" data-dict="first_invest" :data-allowblank="false"
                          :data-disabled="false" data-displaykeyvalue="true" />
      </k-form-item>

      <k-form-item label="最低募集人数" ><!--v-if="this.raiseType == 1"-->
        <k-field-text v-model="t8ProdLimit.minHoldPeoples" data-max-value="{max_hold_peoples}"
                      :data-allowblank="raiseType == '0'" data-validate-type="code"
                      :data-max-length="8" @data-on-blur="validatePeopleNum(t8ProdLimit, 0)"/>
      </k-form-item>
      <k-form-item label="最高募集人数" ><!--v-if="this.raiseType == 1"-->
        <k-field-text v-model="t8ProdLimit.maxHoldPeoples" data-min-value="{min_hold_peoples}"
                      :data-allowblank="raiseType == '0'" :data-max-length="9" data-validate-type="code" data-type="code"
                      @data-on-blur="validatePeopleNum(t8ProdLimit, 1)"/>
      </k-form-item>
      <!--当募集类型为私募才展示-->

      <k-form-item label="超过客户最高购买金额">
        <k-field-select v-model="t8ProdLimit.overTotalSubs" data-dict="over_total_subs"
                        :data-allowblank="false" data-displaykeyvalue="true" />
      </k-form-item>

      <k-form-item label="资产过低判断类型" v-if="prodMode != '1'">
        <k-field-select v-model="t8ProdLimit.lowAssetJudType" data-dict="low_asset_jud_type"
                        :data-allowblank="false" data-displaykeyvalue="true" data-placeholder="请选择"/>
      </k-form-item>

      <k-form-item label="最低资产限额" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.minAssetLimit" :data-allowblank="true" data-validate-type="money"
                      data-type="money" data-min-value="0"  data-integer-length="14"
                      :data-max-length="18" data-digits="2" data-placeholder="单位(元)"/>
      </k-form-item>

      <k-form-item label="最低持有天数" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.minHoldDays" data-validate-type="code" :data-allowblank="true"
                      :data-max-length="4" data-placeholder="单位(天)"/>
      </k-form-item>
    </k-form>

    <!-- 个人 -->
    <div class ="tableLine" v-show = "prodSaleCustoms.indi"><span class="midText">个人限制信息</span></div>
    <k-form class="my-form" ref="limitInfoIndi" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px" v-show = "prodSaleCustoms.indi">
      <k-form-item label="个人首次认购最低金额">
        <k-field-text v-model="t8ProdLimit.minSubsP" data-validate-type="money" data-type="money" data-min-value="(0"
                      :data-max-length="18" data-digits="2"  :data-allowblank="false"  data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldP(t8ProdLimit.minSubsP, t8ProdLimit, formField[0])"/>
      </k-form-item>

      <k-form-item label="个人认购金额单位">
        <k-field-text v-model="t8ProdLimit.stepSubsP" data-validate-type="money" data-type="money" data-min-value="0.01"
                      :data-max-length="18" data-digits="2" :data-allowblank="false" data-integer-length="14" data-placeholder="单位(元)"
                      @data-on-blur="validateFormFieldP(t8ProdLimit.stepSubsP, t8ProdLimit, formField[1])"/>
      </k-form-item>

      <k-form-item label="个人追加起点金额" >
        <k-field-text v-model="t8ProdLimit.minAppendP" data-validate-type="money" data-type="money" data-min-value="(0"
                      :data-max-length="18" data-digits="2"  :data-allowblank="false" data-placeholder="单位(元)"
                      data-integer-length="14" @data-on-blur="validateFormFieldP(t8ProdLimit.minAppendP, t8ProdLimit, formField[2])"/>
      </k-form-item>

      <k-form-item label="个人单笔购买上限" >
        <k-field-text v-model="t8ProdLimit.maxSubsP" data-validate-type="money" data-type="money" data-min-value="0"
                      :data-max-length="18" data-digits="2"  :data-allowblank="false " data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldP(t8ProdLimit.maxSubsP, t8ProdLimit, formField[3])"/>
      </k-form-item>

      <k-form-item label="个人最高认购金额" >
        <k-field-text v-model="t8ProdLimit.maxBuyP" data-validate-type="money" data-type="money" data-min-value="0"
                      :data-max-length="18" data-digits="2"  :data-allowblank="false" data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldP(t8ProdLimit.maxBuyP, t8ProdLimit, formField[4])"/>
      </k-form-item>

      <k-form-item label="个人首次申购最低金额" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.minApplyP" data-validate-type="money" data-type="money" data-min-value="0.01"
                      :data-max-length="18" data-digits="2"  :data-allowblank="false" data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldP(t8ProdLimit.minApplyP, t8ProdLimit, formField[5])"/>
      </k-form-item>

      <k-form-item label="个人申购金额单位" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.stepApplyP" data-validate-type="money" data-type="money" data-min-value="0.01"
                      :data-max-length="18" data-digits="2"  :data-allowblank="false" data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldP(t8ProdLimit.stepApplyP, t8ProdLimit, formField[6])" />
      </k-form-item>

      <k-form-item label="个人当日累计购买最大金额" >
        <k-field-text v-model="t8ProdLimit.maxDailySubsP" :data-allowblank="false" data-validate-type="money" data-type="money"
                      data-min-value="0" :data-max-length="18" data-digits="2" data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldP(t8ProdLimit.maxDailySubsP, t8ProdLimit, formField[7])"/>
      </k-form-item>

      <k-form-item label="个人最高持有金额" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.maxHoldamtP" data-validate-type="money" data-type="money"
                      :data-max-length="18" data-digits="2" :data-allowblank="false" data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldP(t8ProdLimit.maxHoldamtP, t8ProdLimit, formField[8])"/>
      </k-form-item>

      <k-form-item label="个人最高持有比例(%)" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.maxHoldrateP" data-validate-type="money" data-type="money"
                      data-rate-convert="true" data-integer-length="3"
                      data-max-value="100" data-min-value="(0" :data-max-length="9" data-digits="5"
                      :data-allowblank="true" data-placeholder="不限"
        />
      </k-form-item>

      <k-form-item label="个人最低持有份额" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.minHoldP" data-validate-type="money" data-type="money" data-min-value="0"
                      :data-max-length="18" data-digits="2" :data-allowblank="false" data-integer-length="14"/>
      </k-form-item>

      <k-form-item label="个人最低赎回份额" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.minRedeemP" data-validate-type="money" data-type="money" data-min-value="(0"
                      :data-max-length="18" data-digits="2" :data-allowblank="false"  data-integer-length="14"
                      @data-on-blur="validateFormFieldP(t8ProdLimit.minRedeemP, t8ProdLimit, formField[9])"/>
      </k-form-item>

      <k-form-item label="个人单笔最大赎回份额" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.maxRedeemP" data-validate-type="money" data-type="money"
                      :data-max-length="18" data-digits="2"   :data-allowblank="false"  data-integer-length="14"
                      @data-on-blur="validateFormFieldP(t8ProdLimit.maxRedeemP, t8ProdLimit, formField[10])"/>
      </k-form-item>

      <k-form-item label="个人当日累计赎回最大份额" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.maxDailyRedeemP" data-type="money" data-validate-type="money"
                      :data-max-length="18" data-digits="2" :data-allowblank="false"  data-integer-length="14"
                      @data-on-blur="validateFormFieldP(t8ProdLimit.maxDailyRedeemP, t8ProdLimit, formField[11])"/>
      </k-form-item>
    </k-form>

    <div class ="tableLine" v-show = "prodSaleCustoms.inst"><span class="midText">法人限制信息</span></div>
    <!-- 法人 -->
    <k-form class="my-form" ref="limitInfoInst" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px" v-show = "prodSaleCustoms.inst">
      <k-form-item label="法人首次认购最低金额" >
        <k-field-text v-model="t8ProdLimit.minSubsM" data-validate-type="money" data-type="money"  data-min-value="(0"
                      :data-max-length="18" data-digits="2"   :data-allowblank="false" data-integer-length="14" data-placeholder="单位(元)"
                      @data-on-blur="validateFormFieldM(t8ProdLimit.minSubsM, t8ProdLimit, formField[0])"/>
      </k-form-item>

      <k-form-item label="法人认购金额单位">
        <k-field-text v-model="t8ProdLimit.stepSubsM" data-validate-type="money" data-type="money" data-min-value="0.01"
                      :data-max-length="18" data-digits="2" :data-allowblank="false" data-integer-length="14" data-placeholder="单位(元)"
                      @data-on-blur="validateFormFieldM(t8ProdLimit.stepSubsM, t8ProdLimit, formField[1])"/>
      </k-form-item>

      <k-form-item label="法人追加起点金额" >
        <k-field-text v-model="t8ProdLimit.minAppendM" data-validate-type="money" data-type="money" data-min-value="(0"
                      :data-max-length="18" data-digits="2"   :data-allowblank="false" data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldM(t8ProdLimit.minAppendM, t8ProdLimit, formField[2])"/>
      </k-form-item>

      <k-form-item label="法人单笔购买上限" >
        <k-field-text v-model="t8ProdLimit.maxSubsM" data-validate-type="money" data-type="money"
                      :data-max-length="18" data-digits="2"   :data-allowblank="false" data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldM(t8ProdLimit.maxSubsM, t8ProdLimit, formField[3])"/>
      </k-form-item>

      <k-form-item label="法人最高认购金额" >
        <k-field-text v-model="t8ProdLimit.maxBuyM" data-validate-type="money" data-type="money"
                      :data-max-length="18" data-digits="2"   :data-allowblank="false" data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldM(t8ProdLimit.maxBuyM, t8ProdLimit, formField[4])"/>
      </k-form-item>

      <k-form-item label="法人首次申购最低金额" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.minApplyM" data-validate-type="money" data-type="money" data-min-value="(0" data-default-value="0.01"
                      :data-max-length="18" data-digits="2"   :data-allowblank="false" data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldM(t8ProdLimit.stepSubsM, t8ProdLimit, formField[5])"/>
      </k-form-item>

      <k-form-item label="法人申购金额单位" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.stepApplyM" data-validate-type="money" data-type="money" data-min-value="(0"
                      :data-max-length="18" data-digits="2"   :data-allowblank="false" data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldM(t8ProdLimit.stepApplyM, t8ProdLimit, formField[6])"/>
      </k-form-item>

      <k-form-item label="法人当日累计购买最大金额">
        <k-field-text v-model="t8ProdLimit.maxDailySubsM" :data-allowblank="false" data-validate-type="money" data-type="money"
                      :data-max-length="18" data-digits="2" data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldM(t8ProdLimit.maxDailySubsM, t8ProdLimit, formField[7])"/>
      </k-form-item>

      <k-form-item label="法人最高持有金额" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.maxHoldamtM" data-validate-type="money" data-type="money"
                      :data-max-length="18" data-digits="2"   :data-allowblank="false" data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldM(t8ProdLimit.maxHoldamtM, t8ProdLimit, formField[8])"/>
      </k-form-item>

      <k-form-item label="法人最高持有比例(%)" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.maxHoldrateM" data-validate-type="money" data-type="money"
                      data-rate-convert="true" data-max-value="100" data-placeholder="不限" data-integer-length="3"
                      data-min-value="(0" :data-max-length="9" data-digits="5" :data-allowblank="true"
        />
      </k-form-item>

      <k-form-item label="法人最低持有份额" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.minHoldM" data-validate-type="money" data-type="money" data-min-value="(0"
                      :data-max-length="18" data-digits="2" :data-allowblank="false" data-placeholder="单位(元)"
                      data-integer-length="14"/>
      </k-form-item>

      <k-form-item label="法人最低赎回份额" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.minRedeemM" data-validate-type="money" data-type="money" data-min-value="(0"
                      :data-max-length="18" data-digits="2"   :data-allowblank="false" data-integer-length="14"
                      @data-on-blur="validateFormFieldM(t8ProdLimit.minRedeemM, t8ProdLimit, formField[9])"/>
      </k-form-item>

      <k-form-item label="法人单笔最大赎回份额" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.maxRedeemM" data-validate-type="money" data-type="money"
                      :data-max-length="18" data-digits="2"   :data-allowblank="false"  data-integer-length="14"
                      @data-on-blur="validateFormFieldM(t8ProdLimit.maxRedeemM, t8ProdLimit, formField[10])"/>
      </k-form-item>

      <k-form-item label="法人当日累计赎回最大份额" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.maxDailyRedeemM" data-validate-type="money" data-type="money"
                      :data-max-length="18" data-digits="2"   :data-allowblank="false" data-integer-length="14"
                      @data-on-blur="validateFormFieldM(t8ProdLimit.maxDailyRedeemM, t8ProdLimit, formField[11])"/>
      </k-form-item>
    </k-form>
<!--    <div class ="tableLine" v-show = "prodSaleCustoms.insi  && prodMode != '1'"><span class="midText">同业限制信息</span></div>-->
    <div class ="tableLine" v-show = "prodSaleCustoms.insi"><span class="midText">同业限制信息</span></div>
    <k-form class="my-form" ref="limitInfoInsi" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px" v-show = "prodSaleCustoms.insi">
      <k-form-item label="同业首次认购最低金额">
        <k-field-text v-model="t8ProdLimit.minSubsI" data-validate-type="money" data-type="money" data-min-value="(0"
                      :data-max-length="18" data-digits="2"  :data-allowblank="false" data-integer-length="14" data-placeholder="单位(元)"
                      @data-on-blur="validateFormFieldI(t8ProdLimit.minSubsI, t8ProdLimit, formField[0])"/>
      </k-form-item>

      <k-form-item label="同业认购金额单位">
        <k-field-text v-model="t8ProdLimit.stepSubsI" data-validate-type="money" data-type="money" data-min-value="0.01"
                      :data-max-length="18" data-digits="2" :data-allowblank="false" data-integer-length="14" data-placeholder="单位(元)"
                      @data-on-blur="validateFormFieldI(t8ProdLimit.stepSubsI, t8ProdLimit, formField[1])"/>
      </k-form-item>

      <k-form-item label="同业追加起点金额" >
        <k-field-text v-model="t8ProdLimit.minAppendI" data-validate-type="money" data-type="money" data-min-value="(0"
                      :data-max-length="18" data-digits="2"  :data-allowblank="false" data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldI(t8ProdLimit.minAppendI, t8ProdLimit, formField[2])"/>
      </k-form-item>

      <k-form-item label="同业单笔购买上限" >
        <k-field-text v-model="t8ProdLimit.maxSubsI" data-validate-type="money" data-type="money" data-min-value="0"
                      :data-max-length="18" data-digits="2"  :data-allowblank="false"data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldI(t8ProdLimit.maxSubsI, t8ProdLimit, formField[3])"/>
      </k-form-item>

      <k-form-item label="同业最高认购金额" >
        <k-field-text v-model="t8ProdLimit.maxBuyI" data-validate-type="money" data-type="money" data-min-value="0"
                      :data-max-length="18" data-digits="2"  :data-allowblank="false" data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldI(t8ProdLimit.maxBuyI, t8ProdLimit, formField[4])"/>
      </k-form-item>

      <k-form-item label="同业首次申购最低金额" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.minApplyI" data-validate-type="money" data-type="money" data-min-value="(0"
                      :data-max-length="18" data-digits="2"  :data-allowblank="false" data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldI(t8ProdLimit.minApplyI, t8ProdLimit, formField[5])"/>
      </k-form-item>

      <k-form-item label="同业申购金额单位" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.stepApplyI" data-validate-type="money" data-type="money" data-min-value="0.01"
                      :data-max-length="18" data-digits="2"  :data-allowblank="false" data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldI(t8ProdLimit.stepApplyP, t8ProdLimit, formField[6])"/>
      </k-form-item>

      <k-form-item label="同业当日累计购买最大金额" >
        <k-field-text v-model="t8ProdLimit.maxDailySubsI" :data-allowblank="false" data-validate-type="money" data-type="money"
                      data-min-value="0" :data-max-length="18" data-digits="2" data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldI(t8ProdLimit.maxDailySubsI, t8ProdLimit, formField[7])"/>
      </k-form-item>

      <k-form-item label="同业最高持有金额" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.maxHoldamtI" data-validate-type="money" data-type="money"
                      :data-max-length="18" data-digits="2" :data-allowblank="false" data-placeholder="单位(元)" data-integer-length="14"
                      @data-on-blur="validateFormFieldI(t8ProdLimit.maxHoldamtI, t8ProdLimit, formField[8])"/>
      </k-form-item>

      <k-form-item label="同业最高持有比例(%)" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.maxHoldrateI" data-validate-type="money" data-type="money"
                      data-rate-convert="true" data-integer-length="3"
                      data-max-value="100" data-min-value="(0" :data-max-length="9" data-digits="5"
                      :data-allowblank="true" data-placeholder="不限"
        />
      </k-form-item>

      <k-form-item label="同业最低持有份额" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.minHoldI" data-validate-type="money" data-type="money" data-min-value="0"
                      :data-max-length="18" data-digits="2" :data-allowblank="false" data-integer-length="14"/>
      </k-form-item>

      <k-form-item label="同业最低赎回份额" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.minRedeemI" data-validate-type="money" data-type="money" data-min-value="0.01"
                      :data-max-length="18" data-digits="2" :data-allowblank="false"  data-integer-length="14"
                      @data-on-blur="validateFormFieldI(t8ProdLimit.minRedeemI, t8ProdLimit, formField[9])"/>
      </k-form-item>

      <k-form-item label="同业单笔最大赎回份额" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.maxRedeemI" data-validate-type="money" data-type="money"
                      :data-max-length="18" data-digits="2"   :data-allowblank="false"  data-integer-length="14"
                      @data-on-blur="validateFormFieldI(t8ProdLimit.maxRedeemI, t8ProdLimit, formField[10])"/>
      </k-form-item>

      <k-form-item label="同业当日累计赎回最大份额" v-if="prodMode != '1'">
        <k-field-text v-model="t8ProdLimit.maxDailyRedeemI" data-type="money" data-validate-type="money"
                      :data-max-length="18" data-digits="2" :data-allowblank="false"  data-integer-length="14"
                      @data-on-blur="validateFormFieldI(t8ProdLimit.maxDailyRedeemI, t8ProdLimit, formField[11])"/>
      </k-form-item>
    </k-form>
  </div>
</template>

<script>
  import Tools from '@/utils/tools.js';
  export default {
    computed: {},
    model: {
      prop: 't8ProdLimit',
      event: 'input'
    },
    props: {

      t8ProdLimit: {},
      prodMode: '',
      raiseType : '',
      prodCode: {
        type: String,
        default: ''
      },
      t8ProdInfoId: {
        type: String,
        default: ''
      },
    },
    data() {
      return {
        taProdMode: null,
        prodSaleCustoms :[{indi:false, inst:false, insi:false}],// 个人 法人
        formField: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],

      }
    },
    methods: {

      // 提供给父组件调用校验
      validateData() {

        let validateBase = true;
        let validateIndi = true;
        let validateInst = true;
        let validateInsi = true;

        validateBase = this.$refs.limitInfoBase.validate();

        if (this.prodSaleCustoms.indi){
          validateIndi = this.$refs.limitInfoIndi.validate();
        }

        if (this.prodSaleCustoms.inst){
          validateInst = this.$refs.limitInfoInst.validate();
        }

        if (this.prodSaleCustoms.insi){
          validateInsi = this.$refs.limitInfoInsi.validate();
        }

        return validateBase && validateIndi && validateInst && validateInsi;

      },

      /**
       * 展示个人、法人、同业表单
       */
      showProdLimit(value)  {
        if (value.indexOf("0") != -1 || value.indexOf("2") != -1){
          this.prodSaleCustoms.inst = true;
        } else{
          this.prodSaleCustoms.inst = false;
        }

        if (value.indexOf("1") != -1){
          this.prodSaleCustoms.indi = true;
        } else{
          this.prodSaleCustoms.indi = false;

        }

        if (value.indexOf("3") != -1){
          this.prodSaleCustoms.insi = true;
        }else {
          this.prodSaleCustoms.insi = false;
        }
      },

      /**
       * 判断字符串是否为空
       */
      // isEmpty(obj) {
      //
      //   //console.log("isEmpty----" + obj);
      //   if (typeof obj == "undefined" || obj == null || obj == "") {
      //     return false;
      //   } else {
      //     return true;
      //   }
      // },

      /**
       * 校验私募产品募集人数
       */
      validatePeopleNum(params, key){
        if (Tools.isEmpty(params.minHoldPeoples)  && Tools.isEmpty(params.maxHoldPeoples)) {
          switch (key) {
            case 0:
              if (Number(params.minHoldPeoples) > Number(params.maxHoldPeoples)) {
                this.t8ProdLimit.minHoldPeoples = '';
                Tools.alert("不能大于最高募集人数", "warning");
              }
              break;
            case 1:
              if (Number(params.maxHoldPeoples) < Number(params.minHoldPeoples)) {
                this.t8ProdLimit.maxHoldPeoples = '';
                Tools.alert("不能小于最低募集人数", "warning");
              }
              break;
            default:
              console.log("key" + key + "params.minHoldPeoples" + params.minHoldPeoples + "params.maxHoldPeoples" + params.maxHoldPeoples);
              break;
          }
        }
      },

      /**
       * 校验限额字段值(个人)
       */
      validateFormFieldP(value, params, key) {

        value = Number(value);
        params = params.valueOf();

        if (Tools.isEmpty(value)){
          let flag = false; // 标志位判断 是否重置该字段的值

          switch (key) {
            case this.formField[0]: // 个人首次认购最低金额
              flag = this.validateFormFieldToolTips3(value, params, flag);
              if (flag) {
                this.t8ProdLimit.minSubsP = '';
              }
              break;
            case this.formField[1]: // 个人认购金额单位
              flag = this.validateFormFieldToolTips3(value, params, flag);
              if (flag) {
                this.t8ProdLimit.stepSubsP = '';
              }
              break;
            case this.formField[2]: // 个人追加认购金额
              flag = this.validateFormFieldToolTips3(value, params, flag);
              if (flag) {
                this.t8ProdLimit.minAppendP = '';
              }
              break;
            case this.formField[3]: // 个人单笔购买上限
              if (Tools.isEmpty(params.maxBuyP) && value > Number(params.maxBuyP)) {
                flag = true;
                Tools.alert("不能大于个人最高认购金额", "warning");
              } else if (Tools.isEmpty(params.maxDailySubsP) && value > Number(params.maxDailySubsP)) {
                flag = true;
                Tools.alert("不能大于个人当日累计购买最大金额", "warning");
              } else if (Tools.isEmpty(params.maxHoldamtP) && value > Number(params.maxHoldamtP)) {
                flag = true;
                Tools.alert("不能大于个人最高持有金额", "warning");
              } else {
                flag = this.validateFormFieldToolTips2(value, params, flag);
              }
              if (flag) {
                this.t8ProdLimit.maxSubsP = '';
              }
              break;
            case this.formField[4]:// 个人最高认购金额

              if (Tools.isEmpty(params.maxSubsP) && value < Number(params.maxSubsP)) {
                flag = true;
                Tools.alert("不能小于个人单笔购买上限", "warning");
              } else if (Tools.isEmpty(params.maxDailySubsP) && value < Number(params.maxDailySubsP)) {
                flag = true;
                Tools.alert("不能小于个人当日累计购买最大金额", "warning");
              } else if (Tools.isEmpty(params.maxHoldamtP) && value > Number(params.maxHoldamtP)) {
                flag = true;
                Tools.alert("不能大于个人最高持有金额", "warning");
              } else{
                flag = this.validateFormFieldToolTips2(value, params, flag);
              }
              if (flag) {
                this.t8ProdLimit.maxBuyP = '';
              }
              break;
            case this.formField[5]:// 个人首次申购最低金额
              flag = this.validateFormFieldToolTips(value, params, flag);
              if (flag){
                this.t8ProdLimit.minApplyP = '';
              }
              break;
            case this.formField[6]:// 个人申购金额单位
              flag = this.validateFormFieldToolTips(value, params, flag);
              if (flag){
                this.t8ProdLimit.stepApplyP = '';
              }
              break;
            case this.formField[7]:// 个人当日累计购买最大金额
              if (Tools.isEmpty(params.maxSubsP) && value < Number(params.maxSubsP)) {
                flag = true;
                Tools.alert("不能小于个人单笔购买上限", "warning");
              } else if (Tools.isEmpty(params.maxBuyP) && value > Number(params.maxBuyP)){
                flag = true;
                Tools.alert("不能大于个人最高认购金额", "warning");
              }else if(Tools.isEmpty(params.minApplyP) && value < Number(params.minApplyP)){
                flag = true;
                Tools.alert("不能小于个人首次申购最低金额", "warning");
              } else if(Tools.isEmpty(params.stepApplyP) && value < Number(params.stepApplyP)){
                flag = true;
                Tools.alert("不能小于个人申购金额单位", "warning");
              } else if (Tools.isEmpty(params.maxHoldamtP) && value > Number(params.maxHoldamtP)) {
                flag = true;
                Tools.alert("不能大于个人最高持有金额", "warning");
              } else {
                flag = this.validateFormFieldToolTips2(value, params, flag);
              }
              if (flag){
                this.t8ProdLimit.maxDailySubsP = '';
              }
              break;
            case this.formField[8]:// 个人最高持有金额
              if (Tools.isEmpty(params.maxSubsP) && value < Number(params.maxSubsP)) {
                flag = true;
                Tools.alert("不能小于个人单笔购买上限", "warning");
              } else if (Tools.isEmpty(params.maxBuyP) && value < Number(params.maxBuyP)) {
                flag = true;
                Tools.alert("不能小于个人最高认购金额", "warning");
              } else if (Tools.isEmpty(params.maxDailySubsP) && value < Number(params.maxDailySubsP)) {
                flag = true;
                Tools.alert("不能小于个人当日累计购买最大金额", "warning");
              } else {
                flag = this.validateFormFieldToolTips2(value, params, flag);
              }
              if (flag){
                this.t8ProdLimit.maxHoldamtP = '';
              }
              break;
            // case this.formField[9]:// 个人最低持有份额
            case this.formField[9]:// 个人最低赎回份额
              if (Tools.isEmpty(params.maxDailyRedeemP) && value > Number(params.maxDailyRedeemP)) {
                flag = true;
                Tools.alert("不能大于个人当日累计赎回最大份额", "warning");
              } else if (Tools.isEmpty(params.maxRedeemP) && value > Number(params.maxRedeemP)){
                flag = true;
                Tools.alert("不能大于个人单笔最大赎回份额", "warning");
              }
              if (flag){
                this.t8ProdLimit.minRedeemP = '';
              }
              break;
            case this.formField[10]:// 个人单笔最大赎回份额
              if (Tools.isEmpty(params.minRedeemP) && value < Number(params.minRedeemP)) {
                flag = true;
                Tools.alert("不能小于个人最低赎回份额", "warning");
              } else if (Tools.isEmpty(params.maxDailyRedeemP) && value > Number(params.maxDailyRedeemP)){
                flag = true;
                Tools.alert("不能大于个人当日累计赎回最大份额", "warning");
              }
              if (flag){
                this.t8ProdLimit.maxRedeemP = '';
              }
              break;
            case this.formField[11]:// 个人当日累计赎回最大份额
              if (Tools.isEmpty(params.minRedeemP) && value < Number(params.minRedeemP)) {
                flag = true;
                Tools.alert("不能小于个人最低赎回份额", "warning");
              } else if(Tools.isEmpty(params.maxRedeemP) && value < Number(params.maxRedeemP)){
                flag = true;
                Tools.alert("不能小于个人单笔最大赎回份额", "warning");
              }
              if (flag){// 清空个人当日累计赎回最大份额
                this.t8ProdLimit.maxDailyRedeemP = '';
              }
              break;
            default:
              console.log("validateFormField" + value + "params" + params + "flag" + flag);
              break;
          }
        }
      },

      /**
       * 校验个人单笔购买上限、个人当日累计购买最大金额、个人最高持有金额、个人最高认购金额
       */
      validateFormFieldToolTips3(value, params, flag){
        if (Tools.isEmpty(params.maxBuyP) && value > Number(params.maxBuyP)) {
          flag = true;
          Tools.alert("不能大于个人最高认购金额", "warning");
        } else {
          // 个人单笔购买上限、个人当日累计购买最大金额、个人最高持有金额
          flag = this.validateFormFieldToolTips(value, params, flag);
        }
        return flag;
      },

      /**
       * 个人首次认购最低金额、个人认购金额单位、个人追加认购金额
       */
      validateFormFieldToolTips2(value, params, flag){
        if (Tools.isEmpty(params.minSubsP) && value < Number(params.minSubsP)) {
          flag = true;
          Tools.alert("不能小于个人首次认购最低金额", "warning");
        } else if (Tools.isEmpty(params.stepSubsP) && value < Number(params.stepSubsP)) {
          flag = true;
          Tools.alert("不能小于个人认购金额单位", "warning");
        } else if (Tools.isEmpty(params.minAppendP) && Number(value) < Number(params.minAppendP)) {
          flag = true;
          Tools.alert("不能小于个人追加认购金额", "warning");
        }

        return flag;
      },

      /**
       * 校验个人单笔购买上限、个人当日累计购买最大金额、个人最高持有金额
       */
      validateFormFieldToolTips(value, params, flag){
        if (Tools.isEmpty(params.maxSubsP) && value > Number(params.maxSubsP)) {
          flag = true;
          Tools.alert("不能大于个人单笔购买上限", "warning");
        } else if (Tools.isEmpty(params.maxDailySubsP) && value > Number(params.maxDailySubsP)) {
          flag = true;
          Tools.alert("不能大于个人当日累计购买最大金额", "warning");
        } else if (Tools.isEmpty(params.maxHoldamtP) && value > Number(params.maxHoldamtP)) {
          flag = true;
          Tools.alert("不能大于个人最高持有金额", "warning");
        } else {
        }

        return flag;
      },

      /**
       * 校验法人限额字段信息
       */
      validateFormFieldM(value, params, key){
        value = Number(value);
        params = params.valueOf();
        if (Tools.isEmpty(value)){
          let flag = false;

          switch (key) {
            case this.formField[0]: //法人首次认购最低金额
              this.validateFormFieldToolTips5(value, params, flag);
              if (flag){
                this.t8ProdLimit.minSubsM = '';
              }
              break;
            case this.formField[1]:// 法人认购金额单位
              this.validateFormFieldToolTips5(value, params, flag);
              if (flag){
                this.t8ProdLimit.stepSubsM = '';
              }
              break;
            case this.formField[2]: //法人追加认购金额
              this.validateFormFieldToolTips5(value, params, flag);
              if (flag){
                this.t8ProdLimit.minAppendM = '';
              }
              break;
            case this.formField[3]://法人单笔购买上限
              if (Tools.isEmpty(params.maxBuyM) && value > Number(params.maxBuyM)) {
                flag = true;
                Tools.alert("不能大法人最高认购金额", "warning");
              } else if (Tools.isEmpty(params.maxDailySubsM) && value > Number(params.maxDailySubsM)) {
                flag = true;
                Tools.alert("不能大于法人当日累计购买最大金额", "warning");
              } else if (Tools.isEmpty(params.maxHoldamtM) && value > Number(params.maxHoldamtM)) {
                flag = true;
                Tools.alert("不能大于法人最高持有金额", "warning");
              } else {
                flag = this.validateFormFieldToolTips6(value, params, flag);
              }
              if(flag){
                this.t8ProdLimit.maxSubsM = '';
              }
              break;
            case this.formField[4]:// 法人最高认购金额
              if (Tools.isEmpty(params.maxSubsM) && value < Number(params.maxSubsM)) {
                flag = true;
                Tools.alert("不能小于法人单笔购买上限", "warning");
              } else if (Tools.isEmpty(params.maxDailySubsM) && value < Number(params.maxDailySubsM)) {
                flag = true;
                Tools.alert("不能小于法人当日累计购买最大金额", "warning");
              } else if (Tools.isEmpty(params.maxHoldamtM) && value > Number(params.maxHoldamtM)) {
                flag = true;
                Tools.alert("不能大于法人最高持有金额", "warning");
              } else{
                flag = this.validateFormFieldToolTips6(value, params, flag);
              }
              if (flag) {
                this.t8ProdLimit.maxBuyM = '';
              }
              break;
            case this.formField[5]:// 法人首次申购最低金额
              flag = this.validateFormFieldToolTips4(value, params, flag);
              if (flag){
                this.t8ProdLimit.minApplyM = '';
              }
              break;
            case this.formField[6]:// 法人申购金额单位
              flag = this.validateFormFieldToolTips4(value, params, flag);
              if (flag){
                this.t8ProdLimit.stepApplyM = '';
              }
              break;
            case this.formField[7]:// 法人当日累计购买最大金额
              if (Tools.isEmpty(params.maxSubsM) && value < Number(params.maxSubsM)) {
                flag = true;
                Tools.alert("不能小于法人单笔购买上限", "warning");
              } else if (Tools.isEmpty(params.maxBuyM) && value > Number(params.maxBuyM)) {
                flag = true;
                Tools.alert("不能大于法人最高认购金额", "warning");
              } else if(Tools.isEmpty(params.minApplyM) && value < Number(params.minApplyM)){
                flag = true;
                Tools.alert("不能小于法人首次申购最低金额", "warning");
              } else if(Tools.isEmpty(params.stepApplyM) && value < Number(params.stepApplyM)){
                flag = true;
                Tools.alert("不能小于法人申购金额单位", "warning");
              } else if (Tools.isEmpty(params.maxHoldamtM) && value > Number(params.maxHoldamtM)) {
                flag = true;
                Tools.alert("不能大于法人最高持有金额", "warning");
              } else {
                flag = this.validateFormFieldToolTips6(value, params, flag);
              }
              if (flag){
                this.t8ProdLimit.maxDailySubsM = '';
              }
              break;
            case this.formField[8]: // 法人最高持有金额
              if (Tools.isEmpty(params.maxSubsM) && value < Number(params.maxSubsM)) {
                flag = true;
                Tools.alert("不能小于法人单笔购买上限", "warning");
              } else if (Tools.isEmpty(params.maxBuyM) && value < Number(params.maxBuyM)) {
                flag = true;
                Tools.alert("不能小于法人最高认购金额", "warning");
              } else if (Tools.isEmpty(params.maxDailySubsM) && value < Number(params.maxDailySubsM)) {
                flag = true;
                Tools.alert("不能小于法人当日累计购买最大金额", "warning")
              } else {
                flag = this.validateFormFieldToolTips6(value, params, flag);
              }
              if (flag){
                this.t8ProdLimit.maxHoldamtM = '';
              }
              break;
            case this.formField[9]:// 法人最低赎回份额
              if (Tools.isEmpty(params.maxDailyRedeemM) && value > Number(params.maxDailyRedeemM)) {
                flag = true;
                Tools.alert("不能大于法人当日累计赎回最大份额", "warning");
              } else if (Tools.isEmpty(params.maxRedeemM) && value > Number(params.maxRedeemM)){
                flag = true;
                Tools.alert("不能大于法人单笔最大赎回份额", "warning");
              }
              if (flag){
                this.t8ProdLimit.minRedeemM = '';
              }
              break;
            case this.formField[10]:// 法人单笔最大赎回份额
              if (Tools.isEmpty(params.minRedeemM) && value < Number(params.minRedeemM)) {
                flag = true;
                Tools.alert("不能小于法人最低赎回份额", "warning");
              } else if (Tools.isEmpty(params.maxDailyRedeemM) && value > Number(params.maxDailyRedeemM)){
                flag = true;
                Tools.alert("不能大于法人当日累计赎回最大份额", "warning");
              }
              if (flag){
                this.t8ProdLimit.maxRedeemM = '';
              }
              break;
            case this.formField[11]:// 法人当日累计赎回最大份额
              if (Tools.isEmpty(params.minRedeemM) && value < Number(params.minRedeemM)) {
                flag = true;
                Tools.alert("不能小于法人最低赎回份额", "warning");
              } else if(Tools.isEmpty(params.maxRedeemM) && value < Number(params.maxRedeemM)){
                flag = true;
                Tools.alert("不能小于法人单笔最大赎回份额", "warning");
              }
              if (flag){// 清空法人当日累计赎回最大份额
                this.t8ProdLimit.maxDailyRedeemM = '';
              }
              break;
            default:
              console.log("validateFormFieldM" + value + "params" + params + "flag" + flag);
              break;
          }
        }

      },

      /**
       * 法人首次认购最低金额、法人认购金额单位、法人追加认购金额
       */
      validateFormFieldToolTips6(value, params, flag){
        if (Tools.isEmpty(params.minSubsM) && value < Number(params.minSubsM)) {
          flag = true;
          Tools.alert("不能小于法人首次认购最低金额", "warning");
        } else if (Tools.isEmpty(params.stepSubsM) && value < Number(params.stepSubsM)) {
          flag = true;
          Tools.alert("不能小于法人认购金额单位", "warning");
        } else if (Tools.isEmpty(params.minAppendM) && value < Number(params.minAppendM)) {
          flag = true;
          Tools.alert("不能小于法人追加认购金额", "warning");
        }
        return flag;
      },

      /**
       *法人最高认购金额、法人单笔购买上限、法人当日累计购买最大金额、法人最高持有金额
       */
      validateFormFieldToolTips5(value, params, flag){
        if (Tools.isEmpty(params.maxBuyM) && value > Number(params.maxBuyM)){
          flag = true;
          Tools.alert("不能大于法人最高认购金额", "warning");
        } else {
          flag = this.validateFormFieldToolTips4(value, params, flag);
        }
        return flag;
      },

      /**
       * 法人单笔购买上限、法人当日累计购买最大金额、法人最高持有金额
       */
      validateFormFieldToolTips4(value, params, flag){
        if(Tools.isEmpty(params.maxSubsM) && value > Number(params.maxSubsM)){
          flag = true;
          Tools.alert("不能大于法人单笔购买上限", "warning");
        } else if(Tools.isEmpty(params.maxDailySubsM) && value > Number(params.maxDailySubsM)){
          flag = true;
          Tools.alert("不能大于法人当日累计购买最大金额", "warning");
        } else if (Tools.isEmpty(params.maxHoldamtM) && value > Number(params.maxHoldamtM)){
          flag = true;
          Tools.alert("不能大于法人最高持有金额", "warning");
        }
        return flag;
      },

      /**
       * 校验同业限制信息参数
       * @param value
       * @param params
       * @param key
       */
      validateFormFieldI(value, params, key) {
        value = Number(value);
        params = params.valueOf();
        if (Tools.isEmpty(value)){
          let flag = false; // 标志位判断 是否重置该字段的值

          switch (key) {
            case this.formField[0]: // 同业首次认购最低金额
              flag = this.validateFormFieldToolTips7(value, params, flag);
              if (flag) {
                this.t8ProdLimit.minSubsI = '';
              }
              break;
            case this.formField[1]: // 同业认购金额单位
              flag = this.validateFormFieldToolTips7(value, params, flag);
              if (flag) {
                this.t8ProdLimit.stepSubsI = '';
              }
              break;
            case this.formField[2]: // 同业追加认购金额
              flag = this.validateFormFieldToolTips7(value, params, flag);
              if (flag) {
                this.t8ProdLimit.minAppendI = '';
              }
              break;
            case this.formField[3]: // 同业单笔购买上限
              if (Tools.isEmpty(params.maxBuyI) && value > Number(params.maxBuyI)) {
                flag = true;
                Tools.alert("不能大于同业最高认购金额", "warning");
              } else if (Tools.isEmpty(params.maxDailySubsI) && value > Number(params.maxDailySubsI)) {
                flag = true;
                Tools.alert("不能大于同业当日累计购买最大金额", "warning");
              } else if (Tools.isEmpty(params.maxHoldamtI) && value > Number(params.maxHoldamtI)) {
                flag = true;
                Tools.alert("不能大于同业最高持有金额", "warning");
              } else {
                flag = this.validateFormFieldToolTips9(value, params, flag);
              }
              if (flag) {
                this.t8ProdLimit.maxSubsI = '';
              }
              break;
            case this.formField[4]:// 同业最高认购金额
              if (Tools.isEmpty(params.maxSubsI) && value < Number(params.maxSubsI)) {
                flag = true;
                Tools.alert("不能小于同业单笔购买上限", "warning");
              } else if (Tools.isEmpty(params.maxDailySubsI) && value < Number(params.maxDailySubsI)) {
                flag = true;
                Tools.alert("不能小于同业当日累计购买最大金额", "warning");
              } else if (Tools.isEmpty(params.maxHoldamtI) && value > Number(params.maxHoldamtI)) {
                flag = true;
                Tools.alert("不能大于同业最高持有金额", "warning");
              } else{
                flag = this.validateFormFieldToolTips9(value, params, flag);
              }
              if (flag) {
                this.t8ProdLimit.maxBuyI = '';
              }
              break;
            case this.formField[5]:// 同业首次申购最低金额
              flag = this.validateFormFieldToolTips8(value, params, flag);
              if (flag){
                this.t8ProdLimit.minApplyI = '';
              }
              break;
            case this.formField[6]:// 同业申购金额单位
              flag = this.validateFormFieldToolTips8(value, params, flag);
              if (flag){
                this.t8ProdLimit.stepApplyI = '';
              }
              break;
            case this.formField[7]:// 同业当日累计购买最大金额
              if (Tools.isEmpty(params.maxSubsI) && value < Number(params.maxSubsI)) {
                flag = true;
                Tools.alert("不能小于同业单笔购买上限", "warning");
              } else if (Tools.isEmpty(params.maxBuyI) && value > Number(params.maxBuyI)) {
                flag = true;
                Tools.alert("不能大于同业最高认购金额", "warning");
              } else if(Tools.isEmpty(params.minApplyI) && value < Number(params.minApplyI)){
                flag = true;
                Tools.alert("不能小于同业首次申购最低金额", "warning");
              } else if(Tools.isEmpty(params.stepApplyI) && value < Number(params.stepApplyI)){
                flag = true;
                Tools.alert("不能小于同业申购金额单位", "warning");
              } else if (Tools.isEmpty(params.maxHoldamtI) && value > Number(params.maxHoldamtI)) {
                flag = true;
                Tools.alert("不能大于同业最高持有金额", "warning");
              } else {
                flag = this.validateFormFieldToolTips9(value, params, flag);
              }
              if (flag){
                this.t8ProdLimit.maxDailySubsI = '';
              }
              break;
            case this.formField[8]:// 同业最高持有金额
              if (Tools.isEmpty(params.maxSubsI) && value < Number(params.maxSubsI)) {
                flag = true;
                Tools.alert("不能小于同业单笔购买上限", "warning");
              } else if (Tools.isEmpty(params.maxBuyI) && value < Number(params.maxBuyI)) {
                flag = true;
                Tools.alert("不能小于同业最高认购金额", "warning");
              } else if (Tools.isEmpty(params.maxDailySubsI) && value < Number(params.maxDailySubsI)) {
                flag = true;
                Tools.alert("不能小于同业当日累计购买最大金额", "warning");
              } else {
                flag = this.validateFormFieldToolTips9(value, params, flag);
              }
              if (flag){
                this.t8ProdLimit.maxHoldamtI = '';
              }
              break;
            // case this.formField[9]:// 同业最低持有份额
            case this.formField[9]:// 同业最低赎回份额
              if (Tools.isEmpty(params.maxDailyRedeemI) && value > Number(params.maxDailyRedeemI)) {
                flag = true;
                Tools.alert("不能大于同业当日累计赎回最大份额", "warning");
              } else if (Tools.isEmpty(params.maxRedeemI) && value > Number(params.maxRedeemI)){
                flag = true;
                Tools.alert("不能大于同业单笔最大赎回份额", "warning");
              }
              if (flag){
                this.t8ProdLimit.minRedeemI = '';
              }
              break;
            case this.formField[10]:// 同业单笔最大赎回份额
              if (Tools.isEmpty(params.minRedeemI) && value < Number(params.minRedeemI)) {
                flag = true;
                Tools.alert("不能小于同业最低赎回份额", "warning");
              } else if (Tools.isEmpty(params.maxDailyRedeemI) && value > Number(params.maxDailyRedeemI)){
                flag = true;
                Tools.alert("不能大于同业当日累计赎回最大份额", "warning");
              }
              if (flag){
                this.t8ProdLimit.maxRedeemI = '';
              }
              break;
            case this.formField[11]:// 同业当日累计赎回最大份额
              if (Tools.isEmpty(params.minRedeemI) && value < Number(params.minRedeemI)) {
                flag = true;
                Tools.alert("不能小于同业最低赎回份额", "warning");
              } else if(Tools.isEmpty(params.maxRedeemI) && value < Number(params.maxRedeemI)){
                flag = true;
                Tools.alert("不能小于同业单笔最大赎回份额", "warning");
              }
              if (flag){// 清空同业当日累计赎回最大份额
                this.t8ProdLimit.maxDailyRedeemI = '';
              }
              break;
            default:
              console.log("validateFormField" + value + "params" + params + "flag" + flag);
              break;
          }
        }
      },


      validateFormFieldToolTips7(value, params, flag){
        if (Tools.isEmpty(params.maxBuyI) && value > Number(params.maxBuyI)) {
          flag = true;
          Tools.alert("不能大于同业最高认购金额", "warning");
        } else {
          // 同业单笔购买上限、同业当日累计购买最大金额、同业最高持有金额
          flag = this.validateFormFieldToolTips8(value, params, flag);
        }
        return flag;
      },
      /**
       * 校验同业单笔购买上限、同业当日累计购买最大金额、同业最高持有金额
       */
      validateFormFieldToolTips8(value, params, flag){
        if (Tools.isEmpty(params.maxSubsI) && value > Number(params.maxSubsI)) {
          flag = true;
          Tools.alert("不能大于同业单笔购买上限", "warning");
        } else if (Tools.isEmpty(params.maxDailySubsI) && value > Number(params.maxDailySubsI)) {
          flag = true;
          Tools.alert("不能大于同业当日累计购买最大金额", "warning");
        } else if (Tools.isEmpty(params.maxHoldamtI) && value > Number(params.maxHoldamtI)) {
          flag = true;
          Tools.alert("不能大于同业最高持有金额", "warning");
        } else {
        }

        return flag;
      },

      /**
       * 同业首次认购最低金额、同业认购金额单位、同业追加认购金额
       */
      validateFormFieldToolTips9(value, params, flag){
        if (Tools.isEmpty(params.minSubsI) && value < Number(params.minSubsI)) {
          flag = true;
          Tools.alert("不能小于同业首次认购最低金额", "warning");
        } else if (Tools.isEmpty(params.stepSubsI) && value < Number(params.stepSubsI)) {
          flag = true;
          Tools.alert("不能小于同业认购金额单位", "warning");
        } else if (Tools.isEmpty(params.minAppendI) && value < Number(params.minAppendI)) {
          flag = true;
          Tools.alert("不能小于同业追加认购金额", "warning");
        }

        return flag;
      },
    },

    created() {

      if (this.t8ProdLimit.prodSaleCustom != null){
        this.showProdLimit(this.t8ProdLimit.prodSaleCustom)
      }


    },
    mounted() {},
    watch: {
      // prodMode: {
      //   handler: function(val) {
      //     this.$emit('@changeProdMode', val);
      //   },
      //   deep: true
      // },
      // raiseType: {
      //   handler: function(val) {
      //     this.$emit('@changeRaiseType', val);
      //   },
      //   deep: true
      // }

      watch: {
        t8ProdInfoId: function () {
          this.t8ProdLimit.t8ProdInfoId = this.t8ProdInfoId;
        },
      },
    },

  }
</script>

<style>
  .tableLine {
    margin: 30px 8px 20px 6px;border-top:1px dotted #C0C0C0;
    width: 900px;
    position: relative;
    text-align: center;
    font-size: 14px;
  }
  .midText {
    position: absolute;
    left: 50%;
    background-color: #ffffff;
    font-weight: 300;
    padding: 0 15px;
    transform: translateX(-50%) translateY(-50%);
  }
</style>
