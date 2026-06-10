<template>
  <div>
    <k-form class="my-form" ref="limitInfoBase" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-item label="产品代码"   v-show="false">
        <k-field-text v-model="T8ProdLimit.prodCode"  :data-default-value="this.prodCode"/>
      </k-form-item>
      <k-form-item label="产品主表id"   v-show="false">
        <k-field-text v-model="T8ProdLimit.t8ProdInfoId" />
      </k-form-item>
      <k-form-item label="销售对象" v-if="!showShareSort">
        <k-field-checkbox v-model="T8ProdLimit.prodSaleCustom" data-dict="t8_prod_sale_custom" :data-allowblank="false"
                          @data-on-change="showProdLimit">
        </k-field-checkbox>
      </k-form-item>

      <k-form-item label="销售区域" :data-col="2">
        <k-field-text  v-model="T8ProdLimit.salesArea" inputType="textarea" :rows="5" :data-max-length="2000"/>
      </k-form-item>

      <k-form-item label="最高募集金额(元)">
        <k-field-text v-model="T8ProdLimit.maxRaiseAmt" :data-max-length="19" data-digits="2" data-integer-length="16"
                      :data-min-value="T8ProdLimit.minRaiseAmt+''"  data-validate-type="money" data-type="money"
                      data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="最低募集金额(元)">
        <k-field-text v-model="T8ProdLimit.minRaiseAmt" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      :data-max-value="T8ProdLimit.maxRaiseAmt" data-validate-type="money" data-type="money"
                      data-show-gbmoney="true"/>
      </k-form-item>

      <k-form-item label="巨额赎回比例(%)">
        <k-field-text v-model="T8ProdLimit.hugeRedeem" :data-max-length="8" data-digits="4"  data-integer-length="3"
                      data-validate-type="number" data-type="number" data-max-value="100"
                      :data-allowblank="this.prodMode==='1'"/>
      </k-form-item>

      <k-form-item label="产品销售机构">
        <k-field-select v-model="T8ProdLimit.distributorCode" data-action="T8Dict.findTaDistributorInfos"
                        data-display-field="distributorName"  data-value-field="distributorCode" :data-multiple="true" :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="销售渠道与销售机构说明" :data-col="2">
        <k-field-text  v-model="T8ProdLimit.channelOrMechanism" inputType="textarea" :rows="5" :data-max-length="2000"/>
      </k-form-item>

      <k-form-item label="销售对象说明" :data-col="2">
        <k-field-text v-model="T8ProdLimit.saleCustomDesc" inputType="textarea" :rows="5" :data-max-length="2000"/>
      </k-form-item>
    </k-form>




    <div class ="tableLine" v-show = "prodSaleCustoms.inst"><span class="midText">机构销售信息</span></div>
    <!-- 法人 -->
    <k-form class="my-form" ref="limitInfoInst" :data-col="2" dataLabelWidth="210px" dataInputWidth="260px" v-show = "prodSaleCustoms.inst">

      <k-form-item label="机构认购起点金额(元)">
        <k-field-text v-model="T8ProdLimit.minSubsMechanism" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-allowblank="false" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="机构认购递增金额(元)">
        <k-field-text v-model="T8ProdLimit.stepSubsMechanism" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-allowblank="false" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="机构单笔赎回最低份额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minRedeemMechanism" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true" />
      </k-form-item>
      <k-form-item label="机构单笔认购追加最小金额(元)" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minSubsAppendMechanism"  :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="机构单笔申购追加最小金额(元)" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minStepApplyMechanism" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="机构最低持有份额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minHoldMechanism" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="机构持有份额上限">
        <k-field-text v-model="T8ProdLimit.shareUpperMechanism" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-validate-type="money" data-type="money"  data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="机构每日赎回限额" >
        <k-field-text v-model="T8ProdLimit.mechanismRedeemLimit" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="机构单笔赎回递增份额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.stepRedeemMechanism" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="机构首次申购最低金额(元)" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minFirstApplyMechanism" :data-max-length="19" data-digits="2"
                      data-integer-length="16" data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <!-- <k-form-item label="机构投资者说明" :data-col="2">
        <k-field-text v-model="T8ProdLimit.investMechanismDesc" inputType="textarea" :rows="1" :data-max-length="2000"/>
      </k-form-item> -->
    </k-form>

    <!-- 个人 -->
    <div class ="tableLine" v-show = "prodSaleCustoms.indi"><span class="midText">个人销售信息</span></div>
    <k-form class="my-form" ref="limitInfoIndi" :data-col="2" dataLabelWidth="210px" dataInputWidth="260px" v-show = "prodSaleCustoms.indi">
      <k-form-item label="个人认购起点金额(元)">
        <k-field-text v-model="T8ProdLimit.minSubsPerson" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-allowblank="false" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="个人认购递增金额(元)">
        <k-field-text v-model="T8ProdLimit.stepSubsPerson" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-allowblank="false" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="个人单笔赎回最低份额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minRedeemPerson" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="个人单笔认购追加最小金额(元)" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minSubsAppendPerson" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="个人单笔申购追加最小金额(元)" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minStepApplyPerson" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="个人最低持有份额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minHoldPerson" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="个人持有份额上限">
        <k-field-text v-model="T8ProdLimit.shareUpperPerson" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-validate-type="money" data-type="money"  data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="个人每日赎回限额" >
        <k-field-text v-model="T8ProdLimit.dayRedeemLimit" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="个人单笔赎回递增份额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.stepRedeemPerson" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="个人首次申购最低金额(元)" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minFirstApplyPerson" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <!-- <k-form-item label="个人投资者说明" :data-col="2">
        <k-field-text v-model="T8ProdLimit.investPersonDesc" inputType="textarea" :rows="1" :data-max-length="2000"/>
      </k-form-item> -->

    </k-form>

    <!--    <div class ="tableLine" v-show = "prodSaleCustoms.insi  && prodMode != '1'"><span class="midText">同业销售销售信息</span></div>-->
    <div class ="tableLine" v-show = "prodSaleCustoms.insi"><span class="midText">同业销售信息</span></div>
    <k-form class="my-form" ref="limitInfoInsi" :data-col="2" dataLabelWidth="180px" dataInputWidth="290px" v-show = "prodSaleCustoms.insi">

      <k-form-item label="同业认购起点金额">
        <k-field-text v-model="T8ProdLimit.minSubsInterbank" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-allowblank="false" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="同业认购递增金额">
        <k-field-text v-model="T8ProdLimit.stepSubsInterbank" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-allowblank="false" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="同业单笔赎回最低份额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minRedeemInterbank" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="同业单笔认购追加最小金额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minSubsAppendInterbank" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="同业单笔申购追加最小金额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minStepApplyInterbank" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="同业最低持有份额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minHoldInterbank" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="同业持有份额上限">
        <k-field-text v-model="T8ProdLimit.shareUpperInterbank" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="同业每日赎回限额">
        <k-field-text v-model="T8ProdLimit.interbankRedeemLimit" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="同业单笔赎回递增份额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.stepRedeemInterbank" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="同业首次申购最低金额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minFirstApplyInterbank" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <!-- <k-form-item label="同业投资者说明" :data-col="2">
        <k-field-text v-model="T8ProdLimit.investInterbankDesc" inputType="textarea" :rows="1" :data-max-length="2000"/>
      </k-form-item> -->
    </k-form>

    <k-form  dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-footer data-align="center"  v-show="menuName == 'M81007'">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdSale.addT8ProdSale" data-from="addT8ProdSaleForm"
               :data-handler="checkValues" :data-model="T8ProdLimit"  :data-after-success="passDataSuccess">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>保存
        </k-btn>
      </k-form-footer>
    </k-form>
  </div>
</template>

<script>
import Tools from '@/utils/tools.js';
export default {
  computed: {},
  model: {
    prop: 'T8ProdLimit',
    event: 'input'
  },
  props: {
    menuName: '',
    t8ProdInfoId: '',
    prodCode: '',
    T8ProdLimit: {},
    prodMode: '',
    raiseType: '',
    showShareSort: false,//是否份额分类 默认否
  },
  data() {
    return {
      taProdMode: null,
      prodSaleCustoms :[{indi:false, inst:false, insi:false}],// 个人 法人
      formField: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],

    }
  },
  methods: {
    passDataSuccess(){
      this.$emit('isShowButton', '1')
    },
    checkValues(val){
      this.$set(val,'assemblyMenuType','limitInfo');
      let flags = this.validateData();
      //Tools.alert(flags?"验证通过!":"验证失败!");
      if(!flags){
        return false;
      }
      return val;
    },

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
        this.prodSaleCustoms.indi = true;
      } else{
        this.prodSaleCustoms.indi = false;
        this.$refs.limitInfoIndi.reset();
      }

      if (value.indexOf("1") != -1){
        this.prodSaleCustoms.inst = true;
      } else{
        this.prodSaleCustoms.inst = false;
        this.$refs.limitInfoInst.reset();
      }


      if (value.indexOf("3") != -1){
        this.prodSaleCustoms.insi = true;
      }else {
        this.prodSaleCustoms.insi = false;
        this.$refs.limitInfoInsi.reset();
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
              this.T8ProdLimit.minHoldPeoples = '';
              Tools.alert("不能大于最高募集人数", "warning");
            }
            break;
          case 1:
            if (Number(params.maxHoldPeoples) < Number(params.minHoldPeoples)) {
              this.T8ProdLimit.maxHoldPeoples = '';
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
              this.T8ProdLimit.minSubsP = '';
            }
            break;
          case this.formField[1]: // 个人认购金额单位
            flag = this.validateFormFieldToolTips3(value, params, flag);
            if (flag) {
              this.T8ProdLimit.stepSubsP = '';
            }
            break;
          case this.formField[2]: // 个人追加认购金额
            flag = this.validateFormFieldToolTips3(value, params, flag);
            if (flag) {
              this.T8ProdLimit.minAppendP = '';
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
              this.T8ProdLimit.maxSubsP = '';
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
              this.T8ProdLimit.maxBuyP = '';
            }
            break;
          case this.formField[5]:// 个人首次申购最低金额
            flag = this.validateFormFieldToolTips(value, params, flag);
            if (flag){
              this.T8ProdLimit.minApplyP = '';
            }
            break;
          case this.formField[6]:// 个人申购金额单位
            flag = this.validateFormFieldToolTips(value, params, flag);
            if (flag){
              this.T8ProdLimit.stepApplyP = '';
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
              this.T8ProdLimit.maxDailySubsP = '';
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
              this.T8ProdLimit.maxHoldamtP = '';
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
              this.T8ProdLimit.minRedeemP = '';
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
              this.T8ProdLimit.maxRedeemP = '';
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
              this.T8ProdLimit.maxDailyRedeemP = '';
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
              this.T8ProdLimit.minSubsM = '';
            }
            break;
          case this.formField[1]:// 法人认购金额单位
            this.validateFormFieldToolTips5(value, params, flag);
            if (flag){
              this.T8ProdLimit.stepSubsM = '';
            }
            break;
          case this.formField[2]: //法人追加认购金额
            this.validateFormFieldToolTips5(value, params, flag);
            if (flag){
              this.T8ProdLimit.minAppendM = '';
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
              this.T8ProdLimit.maxSubsM = '';
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
              this.T8ProdLimit.maxBuyM = '';
            }
            break;
          case this.formField[5]:// 法人首次申购最低金额
            flag = this.validateFormFieldToolTips4(value, params, flag);
            if (flag){
              this.T8ProdLimit.minApplyM = '';
            }
            break;
          case this.formField[6]:// 法人申购金额单位
            flag = this.validateFormFieldToolTips4(value, params, flag);
            if (flag){
              this.T8ProdLimit.stepApplyM = '';
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
              this.T8ProdLimit.maxDailySubsM = '';
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
              this.T8ProdLimit.maxHoldamtM = '';
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
              this.T8ProdLimit.minRedeemM = '';
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
              this.T8ProdLimit.maxRedeemM = '';
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
              this.T8ProdLimit.maxDailyRedeemM = '';
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
     * 校验同业销售信息参数
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
              this.T8ProdLimit.minSubsI = '';
            }
            break;
          case this.formField[1]: // 同业认购金额单位
            flag = this.validateFormFieldToolTips7(value, params, flag);
            if (flag) {
              this.T8ProdLimit.stepSubsI = '';
            }
            break;
          case this.formField[2]: // 同业追加认购金额
            flag = this.validateFormFieldToolTips7(value, params, flag);
            if (flag) {
              this.T8ProdLimit.minAppendI = '';
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
              this.T8ProdLimit.maxSubsI = '';
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
              this.T8ProdLimit.maxBuyI = '';
            }
            break;
          case this.formField[5]:// 同业首次申购最低金额
            flag = this.validateFormFieldToolTips8(value, params, flag);
            if (flag){
              this.T8ProdLimit.minApplyI = '';
            }
            break;
          case this.formField[6]:// 同业申购金额单位
            flag = this.validateFormFieldToolTips8(value, params, flag);
            if (flag){
              this.T8ProdLimit.stepApplyI = '';
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
              this.T8ProdLimit.maxDailySubsI = '';
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
              this.T8ProdLimit.maxHoldamtI = '';
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
              this.T8ProdLimit.minRedeemI = '';
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
              this.T8ProdLimit.maxRedeemI = '';
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
              this.T8ProdLimit.maxDailyRedeemI = '';
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
    if (this.T8ProdLimit.prodSaleCustom != null){
      this.showProdLimit(this.T8ProdLimit.prodSaleCustom)
    }


  },
  mounted() {},
  watch: {
    prodMode: {
      handler: function(val) {
        this.$emit('@changeProdMode', val);
      },
      deep: true
    },
    raiseType: {
      handler: function(val) {
        this.$emit('@changeRaiseType', val);
      },
      deep: true
    },
    t8ProdInfoId: function () {
      this.T8ProdLimit.t8ProdInfoId = this.t8ProdInfoId;
    },

    'T8ProdLimit.prodSaleCustom' : function (value) {
      this.showProdLimit(value);
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
