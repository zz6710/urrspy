<template>
    <div>
      <div class="form-item prod-panel" id="prodInfo" v-show="formData.showPanels && formData.showPanels.prodInfo">
        <div class="title"  >
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="基本信息"></k-field-display>
        </div>
        <ProdInfo ref="prodInfo" v-model="formData.T8ProdInfo" :menuName="formData.menuName" @isShowButton="isProdParamsCount"/>
      </div>


      <div class="form-item prod-panel" id="truteeInfo" v-show="formData.showPanels && formData.showPanels.truteeInfo">
        <div class="title">
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="托管信息"></k-field-display>
        </div>
        <ProdEscrowAcct ref="truteeInfo" v-model="formData.T8ProdEscrowAcct" :ProdEscrowAcct="formData.T8ProdEscrowAcct"  @isShowButton="isProdParamsCount"
                        :prodCode="formData.T8ProdInfo.prodCode" :t8ProdInfoId="formData.T8ProdInfo.id" :menuName="formData.menuName"/>
      </div>

      <div class="form-item prod-panel" id="prodCalendar" v-show="formData.showPanels && formData.showPanels.prodCalendar">
        <div class="title" >
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="周期信息"></k-field-display>
        </div>
        <T8ProdCalendar ref="prodCalendar" v-model="formData.T8ProdCalendar" :T8ProdCalendar="formData.T8ProdCalendar"
                        :prodCode="formData.T8ProdInfo.prodCode" :prodMode="formData.T8ProdInfo.prodMode"
                        :menuName="formData.menuName" :t8ProdInfoId="formData.T8ProdInfo.id"  @isShowButton="isProdParamsCount"/>
      </div>

      <div class="form-item prod-panel" id="limitInfo" v-show="formData.showPanels && formData.showPanels.limitInfo">
        <div class="title">
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="销售信息"></k-field-display>
        </div>
        <ProdLimit ref="limitInfo" v-model="formData.T8ProdLimit" :T8ProdLimit="formData.T8ProdLimit"
                   :prodMode="formData.T8ProdInfo.prodMode" :prodCode="formData.T8ProdInfo.prodCode"
                   :menuName="formData.menuName" @isShowButton="isProdParamsCount"
                   :t8ProdInfoId="formData.T8ProdInfo.id" :showShareSort="showShareSort"/>
      </div>


      <div class="form-item prod-panel" id="prodBonus" v-show="formData.showPanels && formData.showPanels.prodBonus">
        <div class="title">
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="产品分红信息"></k-field-display>
        </div>
        <T8ProdBonus ref="prodBonus" v-model="formData.T8ProdBonus" :T8ProdBonus="formData.T8ProdBonus" :menuName="formData.menuName"
                     :prodCode="formData.T8ProdInfo.prodCode" :t8ProdInfoId="formData.T8ProdInfo.id"
                     @isShowButton="isProdParamsCount"/>
      </div>

      <div class="form-item prod-panel" id="prodInvest" v-show="formData.showPanels && formData.showPanels.prodInvest">
        <div class="title">
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="产品投资信息"></k-field-display>
        </div>
        <ProdInvest ref="prodInvest" v-model="formData.ProdInvest" :ProdInvest="formData.ProdInvest"
                    :menuName="formData.menuName" :prodCode="formData.T8ProdInfo.prodCode"
                    @isShowButton="isProdParamsCount" :t8ProdInfoId="formData.T8ProdInfo.id"/>
      </div>

      <div class="form-item prod-panel" id="prodValuation" v-show="formData.showPanels && formData.showPanels.prodValuation">
        <div class="title">
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="产品估值信息"></k-field-display>
        </div>
        <ProdValuation ref="prodValuation" v-model="formData.ProdValuation" :ProdValuation="formData.ProdValuation"
                       :menuName="formData.menuName" :prodCode="formData.T8ProdInfo.prodCode" :prodMode="formData.T8ProdInfo.prodMode"
                       @isShowButton="isProdParamsCount" :t8ProdInfoId="formData.T8ProdInfo.id"/>
      </div>

      <div class="form-item prod-panel" id="prodFee" v-show="formData.showPanels && formData.showPanels.prodFee">
        <div class="title">
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="产品费用信息"></k-field-display>
        </div>
        <ProdFee ref="prodFee" v-model="formData.ProdFee" :ProdFee="formData.ProdFee" :prodCode="formData.T8ProdInfo.prodCode"
                 :T8ProdCalendar="formData.T8ProdCalendar" @isShowButton="isProdParamsCount" :prodIsShareSort="this.showShareSort"
                 :menuName="formData.menuName" :t8ProdInfoId="formData.T8ProdInfo.id" />
      </div>

      <div class="form-item prod-panel" id="feeDeal" v-show="formData.showPanels && formData.showPanels.feeDeal">
        <div class="title">
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="交易费用信息"></k-field-display>
        </div>
        <ProdFeeDeal ref="feeDeal" v-model="formData.ProdFeeDeal" :ProdFeeDeal="formData.ProdFeeDeal"
                     :prodCode="formData.T8ProdInfo.prodCode" :t8ProdInfoId="formData.T8ProdInfo.id"
                     :prodMode="formData.T8ProdInfo.prodMode" :menuName="formData.menuName"
                     @isShowButton="isProdParamsCount"/>
      </div>


      <div class="form-item prod-panel" id="performanceInfo" v-show="formData.showPanels && formData.showPanels.performanceInfo">
        <div class="title">
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="业绩报酬信息"></k-field-display>
        </div>
        <T8ProdPerformance ref="performanceInfo" v-model="formData.T8ProdPerformance" @getBaseType="getBaseType"
                           :T8ProdPerformance="formData.T8ProdPerformance" :prodIsShareSort="this.showShareSort" :prodCode="formData.T8ProdInfo.prodCode"
                           :menuName="formData.menuName" @isShowButton="isProdParamsCount"
                           :t8ProdInfoId="formData.T8ProdInfo.id"/>
      </div>

      <div class="form-item prod-panel" id="prodDocInfo" v-show="formData.showPanels && formData.showPanels.prodDocInfo">
        <div class="title">
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="产品文档信息"></k-field-display>
        </div>
        <T8ProdDocMods ref="prodDocInfo" v-model="formData.T8ProdDocMods" :T8ProdDocMods="formData.T8ProdDocMods"
                       :prodCode="formData.T8ProdInfo.prodCode" :t8ProdInfoId="formData.T8ProdInfo.id"
                       :menuName="formData.menuName" @isShowButton="isProdParamsCount"
                       :raiseType="formData.T8ProdInfo.raiseType" :prodMode="formData.T8ProdInfo.prodMode"/>
      </div>

      <div class="form-item prod-panel" id="ProdShareSort"
           v-show="formData.showPanels && formData.showPanels.ProdShareSort">
        <div class="title">
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="份额分类"></k-field-display>
        </div>
        <ProdShareSort ref="prodShareSort" v-model="formData.T8ProdShareSort" :prodShareSort="formData.T8ProdShareSort"
                       :prodCode="formData.T8ProdInfo.prodCode"
                       :prodName="formData.T8ProdInfo.prodName"
                       :t8ProdInfoId="formData.T8ProdInfo.id" :salesShareStatus="formData.T8ProdInfo.prodStatus"
                       :prodMode="formData.T8ProdInfo.prodMode" :menuName="formData.menuName"
                       :baseType="formData.T8ProdPerformance.baseType" :key="formData.T8ProdPerformance.baseType"/>
      </div>
      <!--      增加最后一个panel高度,实现左侧选择框选中状态      -->
      <div class="form-item prod-panel" style="height:600px;" v-show="formData.assemblyMenuType=='1'"/>
    </div>

</template>

<script>
  import eventBus from '@/utils/eventBus'
  import ProdInfo         from "./M81001-ProdInfo"
  import ProdEscrowAcct   from "./M81001-ProdEscrowAcct.vue"
  import T8ProdPerformance    from "./M81001-ProdPerformance.vue"
  import ProdLimit        from "./M81001-ProdLimit"
  import ProdFeeDeal      from "./M81001-FeeDeal.vue"
  import ProdFee      from "./M81001-FeeProd.vue"
  import ProdInvest      from "./M81001-ProdInvest.vue"
  import ProdValuation      from "./M81001-ProdValuation.vue"
  import T8ProdCalendar      from "./M81001-ProdCalendar.vue"
  import T8ProdBonus      from "./M81001-ProdBonusNew.vue"
  import T8ProdDocMods      from "./M81001-ProdDocMods.vue"
  import ProdShareSort      from "./M81001-ProdShareSort"
    export default {
        name: "M81001ProdInfoAdd",
      components: {
        ProdInfo,
        ProdEscrowAcct,
        T8ProdPerformance,
        ProdFeeDeal,
        ProdFee,
        ProdInvest,
        ProdValuation,
        T8ProdCalendar,
        T8ProdBonus,
        T8ProdDocMods,
        ProdLimit,
        ProdShareSort
      },
      props: {
        formData: {
          T8ProdInfo: {
            id                  : "",
            prodCode            : "",
            prodMode            : "",
            raiseType           : "",
            pgmno               : "",
            updateProduct       :false,

          },

          T8ProdEscrowAcct:{
            prodCode : "",
            t8ProdInfoId:"",
          },

          T8ProdPerformance :{
            prodCode : "",
            t8ProdInfoId:"",
            moneyList:[],
            switchSegmentValue:"",
            tailingCommisionList:[],
            tailingCommisionMoneyList:[],
          },

          ProdOtherInfo:{
            prodCode : "",
            t8ProdInfoId:""
          },
          PrjFeeDivide:{
            prodCode : "",
            t8ProdInfoId:""
          },
          ProdFeeDeal:{
            dataParams:[],
            prodCode : "",
            t8ProdInfoId:""
          },
          ProdFee:{
            dataParams:[],
            prodCode : "",
            t8ProdInfoId:""
          },
          ProdInvest:{
            prodCode : "",
            t8ProdInfoId:""
          },
          ProdValuation:{
            prodCode : "",
            t8ProdInfoId:"",
            prodMode:""
          },
          T8ProdCalendar:{
            prodCode : "",
            t8ProdInfoId:""
          },
          T8ProdLimit:{
            prodCode : "",
            t8ProdInfoId:""
          },
          T8ProdBonus:{
            prodCode : "",
            t8ProdInfoId:""
          },
          T8ProdDocMods:{
            dataParams:[],
            prodCode : "",
            t8ProdInfoId:""
          },
          T8DeclarationInfo:{
            prodCode : "",
            t8ProdInfoId:""
          },
          T8ProdIssueRegisFields:{
            prodCode : "",
            t8ProdInfoId:"",
          },
          T8ProdShareSort:{
            dataParams: [],
            prodCode : "",
            t8ProdInfoId:"",
          },
          showPanels:{},
          assemblyMenuType:'',
          menuName:'',
        },
        hiddenShowShareSort:{
          type: Boolean,
          default: false
        },
      },

      data() {
        return {
          envItems: [{"ratioIndex":''},{"coefficient":''}],
          switchSegmentValue: false,   //金额分段切换按钮的值
          moneyList: [],
          tailingCommisionList: [],
          tailingCommisionMoneyList: [],
          riskItems:[],
          showShareSort: false
        }
      },

      created() {
        eventBus.$on('shareSortChange', item => {
          if (item.shareSort === '1') { //是否份额分类 1代表是
            this.showShareSort = true;
          } else {
            this.showShareSort = false;
          }
        })
      },
      watch: {
        envItems:function(value) {
          this.$refs.performanceInfo.envItems=value;
        },
        switchSegmentValue:function(value)  {
          this.$refs.performanceInfo.switchSegmentValue=value;
        },
        moneyList:function(value)  {
          this.$refs.performanceInfo.moneyList = value;
        },
        tailingCommisionList:function(value)  {
          this.$refs.performanceInfo.tailingCommisionList= value;
        },
        tailingCommisionMoneyList: function(value) {
          this.$refs.performanceInfo.tailingCommisionMoneyList= value;
        },
        riskItems: function(value) {
          this.$refs.prodInvest.riskItems= value;
        },
      },


      methods: {
        isProdParamsCount(){
          this.$emit('isShowButton', '1')
        },
        getBaseType(val){
          this.formData.T8ProdPerformance.baseType=val
          //console.log("我是父组件",this.formData.T8ProdPerformance.baseType)
        }
      },
      mounted() {},
    }
</script>

<style lang="scss" scoped>

  @import "../../../../styles/T81001.scss";

</style>
