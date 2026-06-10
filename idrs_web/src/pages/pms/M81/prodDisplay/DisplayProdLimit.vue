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
                          :data-disabled="true">
        </k-field-checkbox>
      </k-form-item>
      <k-form-item label="销售区域" :data-col="2">
        <k-field-text  v-model="T8ProdLimit.salesArea" inputType="textarea" :rows="5" :data-max-length="2000" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="最高募集金额">
        <k-field-text v-model="T8ProdLimit.maxRaiseAmt" :data-max-length="19" data-digits="2" data-integer-length="16" data-show-gbmoney="true"
                      :data-min-value="T8ProdLimit.minRaiseAmt+''"  data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="最低募集金额">
        <k-field-text v-model="T8ProdLimit.minRaiseAmt" :data-max-length="19" data-digits="2"  data-integer-length="16" data-show-gbmoney="true"
                      :data-max-value="T8ProdLimit.maxRaiseAmt" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="巨额赎回比例(%)">
        <k-field-text v-model="T8ProdLimit.hugeRedeem" :data-max-length="8" data-digits="4"  data-integer-length="3"
                      data-validate-type="number" data-type="number" data-max-value="100" :data-disabled="true"
                      :data-allowblank="true"/>
      </k-form-item>

      <k-form-item label="产品销售机构">
        <k-field-select v-model="T8ProdLimit.distributorCode" data-action="T8Dict.findTaDistributorInfos" :data-disabled="true"
                        data-display-field="distributorName"  data-value-field="distributorCode" :data-multiple="true" :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="销售渠道与销售机构说明" :data-col="2">
        <k-field-text  v-model="T8ProdLimit.channelOrMechanism" inputType="textarea" :rows="5" :data-max-length="2000" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="销售对象说明" :data-col="2">
        <k-field-text v-model="T8ProdLimit.saleCustomDesc" inputType="textarea" :rows="5" :data-max-length="2000" :data-disabled="true"/>
      </k-form-item>
    </k-form>




    <div class ="tableLine" v-show = "prodSaleCustoms.inst"><span class="midText">机构销售信息</span></div>
    <!-- 法人 -->
    <k-form class="my-form" ref="limitInfoInst" :data-col="2" dataLabelWidth="210px" dataInputWidth="260px" v-show = "prodSaleCustoms.inst">

      <k-form-item label="机构认购起点金额(元)">
        <k-field-text v-model="T8ProdLimit.minSubsMechanism" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="机构认购递增金额(元)">
        <k-field-text v-model="T8ProdLimit.stepSubsMechanism" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="机构单笔赎回最低份额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minRedeemMechanism" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="机构单笔认购追加最小金额(元)" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minSubsAppendMechanism"  :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="机构单笔申购追加最小金额(元)" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minStepApplyMechanism" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="机构最低持有份额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minHoldMechanism" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="机构持有份额上限">
        <k-field-text v-model="T8ProdLimit.shareUpperMechanism" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-validate-type="money" data-type="money" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="机构每日赎回限额" >
        <k-field-text v-model="T8ProdLimit.mechanismRedeemLimit" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="机构单笔赎回递增份额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.stepRedeemMechanism" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="机构首次申购最低金额(元)" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minFirstApplyMechanism" :data-max-length="19" data-digits="2" :data-disabled="true"
                      data-integer-length="16" data-min-value="0" data-validate-type="money" data-type="money"/>
      </k-form-item>
      <!-- <k-form-item label="机构投资者说明" :data-col="2">
        <k-field-text v-model="T8ProdLimit.investMechanismDesc" inputType="textarea" :rows="1" :data-max-length="2000" :data-disabled="true"/>
      </k-form-item> -->
    </k-form>

    <!-- 个人 -->
    <div class ="tableLine" v-show = "prodSaleCustoms.indi"><span class="midText">个人销售信息</span></div>
    <k-form class="my-form" ref="limitInfoIndi" :data-col="2" dataLabelWidth="210px" dataInputWidth="260px" v-show = "prodSaleCustoms.indi">
      <k-form-item label="个人认购起点金额(元)">
        <k-field-text v-model="T8ProdLimit.minSubsPerson" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="个人认购递增金额(元)">
        <k-field-text v-model="T8ProdLimit.stepSubsPerson" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="个人单笔赎回最低份额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minRedeemPerson" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="个人单笔认购追加最小金额(元)" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minSubsAppendPerson" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="个人单笔申购追加最小金额(元)" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minStepApplyPerson" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="个人最低持有份额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minHoldPerson" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="个人持有份额上限">
        <k-field-text v-model="T8ProdLimit.shareUpperPerson" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-validate-type="money" data-type="money"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="个人每日赎回限额" >
        <k-field-text v-model="T8ProdLimit.dayRedeemLimit" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="个人单笔赎回递增份额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.stepRedeemPerson" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="个人首次申购最低金额(元)" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minFirstApplyPerson" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <!-- <k-form-item label="个人投资者说明" :data-col="2">
        <k-field-text v-model="T8ProdLimit.investPersonDesc" inputType="textarea" :rows="1" :data-max-length="2000" :data-disabled="true"/>
      </k-form-item> -->

    </k-form>

    <!--    <div class ="tableLine" v-show = "prodSaleCustoms.insi  && prodMode != '1'"><span class="midText">同业销售销售信息</span></div>-->
    <div class ="tableLine" v-show = "prodSaleCustoms.insi"><span class="midText">同业销售信息</span></div>
    <k-form class="my-form" ref="limitInfoInsi" :data-col="2" dataLabelWidth="180px" dataInputWidth="290px" v-show = "prodSaleCustoms.insi">

      <k-form-item label="同业认购起点金额">
        <k-field-text v-model="T8ProdLimit.minSubsInterbank" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="同业认购递增金额">
        <k-field-text v-model="T8ProdLimit.stepSubsInterbank" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="同业单笔赎回最低份额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minRedeemInterbank" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="同业单笔认购追加最小金额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minSubsAppendInterbank" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="同业单笔申购追加最小金额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minStepApplyInterbank" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="同业最低持有份额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minHoldInterbank" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="同业持有份额上限">
        <k-field-text v-model="T8ProdLimit.shareUpperInterbank" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="同业每日赎回限额">
        <k-field-text v-model="T8ProdLimit.interbankRedeemLimit" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="同业单笔赎回递增份额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.stepRedeemInterbank" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="同业首次申购最低金额" v-if="this.prodMode != '1'">
        <k-field-text v-model="T8ProdLimit.minFirstApplyInterbank" :data-max-length="19" data-digits="2"  data-integer-length="16"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"/>
      </k-form-item>
      <!-- <k-form-item label="同业投资者说明" :data-col="2">
        <k-field-text v-model="T8ProdLimit.investInterbankDesc" inputType="textarea" :rows="1" :data-max-length="2000" :data-disabled="true"/>
      </k-form-item> -->
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
      showProdLimit(value)  {
        if (value.indexOf("0") != -1 || value.indexOf("2") != -1){
          this.prodSaleCustoms.indi = true;
        } else{
          this.prodSaleCustoms.indi = false;
        }

        if (value.indexOf("1") != -1){
          this.prodSaleCustoms.inst = true;
        } else{
          this.prodSaleCustoms.inst = false;
        }


        if (value.indexOf("3") != -1){
          this.prodSaleCustoms.insi = true;
        }else {
          this.prodSaleCustoms.insi = false;
        }
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
