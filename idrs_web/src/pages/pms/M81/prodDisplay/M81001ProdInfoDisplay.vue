<template>
  <div >

    <div class="form-item prod-panel" id="prodInfo" v-show="formData.showPanels && formData.showPanels.prodInfo">
      <div class="title"  >
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="基本信息"></k-field-display>
      </div>
      <ProdInfo ref="prodInfo" v-model="formData.T8ProdInfo"  />
    </div>


    <div class="form-item prod-panel" id="truteeInfo" v-show="formData.showPanels && formData.showPanels.truteeInfo">
      <div class="title">
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="托管信息"></k-field-display>
      </div>
      <ProdEscrowAcct ref="truteeInfo" v-model="formData.T8ProdEscrowAcct" :ProdEscrowAcct="formData.T8ProdEscrowAcct"
                      :prodCode="formData.T8ProdInfo.prodCode" :t8ProdInfoId="formData.T8ProdInfo.id" />
    </div>

    <div class="form-item prod-panel" id="prodCalendar" v-show="formData.showPanels && formData.showPanels.prodCalendar">
      <div class="title" >
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="周期信息"></k-field-display>
      </div>
      <T8ProdCalendar ref="prodCalendar" v-model="formData.T8ProdCalendar" :T8ProdCalendar="formData.T8ProdCalendar"
                      :prodCode="formData.T8ProdInfo.prodCode" :prodMode="formData.T8ProdInfo.prodMode"
                      :t8ProdInfoId="formData.T8ProdInfo.id" />
    </div>

    <div class="form-item prod-panel" id="limitInfo" v-show="formData.showPanels && formData.showPanels.limitInfo">
      <div class="title">
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="销售信息"></k-field-display>
      </div>
      <ProdLimit ref="limitInfo" v-model="formData.T8ProdLimit" :T8ProdLimit="formData.T8ProdLimit"
                 :prodMode="formData.T8ProdInfo.prodMode" :prodCode="formData.T8ProdInfo.prodCode"
                 :t8ProdInfoId="formData.T8ProdInfo.id" :showShareSort="showShareSort"/>
    </div>


    <div class="form-item prod-panel" id="prodBonus" v-show="formData.showPanels && formData.showPanels.prodBonus">
      <div class="title">
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="产品分红信息"></k-field-display>
      </div>
      <T8ProdBonus ref="prodBonus" v-model="formData.T8ProdBonus" :T8ProdBonus="formData.T8ProdBonus"
                   :prodCode="formData.T8ProdInfo.prodCode" :t8ProdInfoId="formData.T8ProdInfo.id"/>
    </div>

    <div class="form-item prod-panel" id="prodInvest" v-show="formData.showPanels && formData.showPanels.prodInvest">
      <div class="title">
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="产品投资信息"></k-field-display>
      </div>
      <ProdInvest ref="prodInvest" v-model="formData.ProdInvest" :ProdInvest="formData.ProdInvest"
                  :prodCode="formData.T8ProdInfo.prodCode" :t8ProdInfoId="formData.T8ProdInfo.id"/>
    </div>

    <div class="form-item prod-panel" id="prodValuation" v-show="formData.showPanels && formData.showPanels.prodValuation">
      <div class="title">
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="产品估值信息"></k-field-display>
      </div>
      <ProdValuation ref="prodValuation" v-model="formData.ProdValuation" :ProdValuation="formData.ProdValuation"
                     :prodCode="formData.T8ProdInfo.prodCode" :t8ProdInfoId="formData.T8ProdInfo.id" :prodMode="formData.T8ProdInfo.prodMode"/>
    </div>

    <div class="form-item prod-panel" id="prodFee" v-show="formData.showPanels && formData.showPanels.prodFee">
      <div class="title">
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="产品费用信息"></k-field-display>
      </div>
      <ProdFee ref="prodFee" v-model="formData.ProdFee" :ProdFee="formData.ProdFee" :prodCode="formData.T8ProdInfo.prodCode"
               :T8ProdCalendar="formData.T8ProdCalendar"
               :t8ProdInfoId="formData.T8ProdInfo.id" />
    </div>

    <div class="form-item prod-panel" id="feeDeal" v-show="formData.showPanels && formData.showPanels.feeDeal">
      <div class="title">
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="交易费用信息"></k-field-display>
      </div>
      <ProdFeeDeal ref="feeDeal" v-model="formData.ProdFeeDeal" :ProdFeeDeal="formData.ProdFeeDeal"
                   :prodCode="formData.T8ProdInfo.prodCode" :t8ProdInfoId="formData.T8ProdInfo.id"
                   :prodMode="formData.T8ProdInfo.prodMode" />
    </div>


    <div class="form-item prod-panel" id="performanceInfo" v-show="formData.showPanels && formData.showPanels.performanceInfo">
      <div class="title">
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="业绩报酬信息"></k-field-display>
      </div>
      <T8ProdPerformance ref="performanceInfo" v-model="formData.T8ProdPerformance"
                         :T8ProdPerformance="formData.T8ProdPerformance" :prodCode="formData.T8ProdInfo.prodCode"
                         :t8ProdInfoId="formData.T8ProdInfo.id" :showShareSort="showShareSort"/>
    </div>

    <div class="form-item prod-panel" id="prodDocInfo" v-show="formData.showPanels && formData.showPanels.prodDocInfo">
      <div class="title">
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="产品文档信息"></k-field-display>
      </div>
      <T8ProdDocMods ref="prodDocInfo" v-model="formData.T8ProdDocMods" :T8ProdDocMods="formData.T8ProdDocMods"
                     :prodCode="formData.T8ProdInfo.prodCode" :t8ProdInfoId="formData.T8ProdInfo.id"
                     :raiseType="formData.T8ProdInfo.raiseType" :prodMode="formData.T8ProdInfo.prodMode"/>
    </div>

    <div class="form-item prod-panel" id="ProdShareSort" v-if="formData.showPanels && formData.showPanels.ProdShareSort
                && formData.T8ProdShareSort != undefined">
      <div class="title">
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="份额分类"></k-field-display>
      </div>
      <ProdShareSort ref="prodShareSort" v-model="formData.T8ProdShareSort"
                     :dataParams="formData.T8ProdShareSort.dataParams"
                     :prodCode="formData.T8ProdInfo.prodCode"
                     :t8ProdInfoId="formData.T8ProdInfo.id"/>
    </div>

    <div class="form-item prod-panel" id="declarationInfo"
         v-if="formData.showPanels && formData.showPanels.declarationInfo">
      <div class="title">
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="申报要素信息"></k-field-display>
      </div>
      <T8DeclarationInfo ref="declarationInfo" v-model="formData.T8DeclarationInfo"
                         :T8DeclarationInfo="formData.T8DeclarationInfo"
                         :prodCode="formData.T8ProdInfo.prodCode" :t8ProdInfoId="formData.T8ProdInfo.id"/>
    </div>

    <div class="form-item prod-panel" id="prodIssueAdjust"
         v-if="formData.showPanels && formData.showPanels.prodIssueAdjust">
      <div class="title">
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="发行要素信息"></k-field-display>
      </div>
      <ProdIssueAdjust ref="prodIssueAdjust" v-model="formData.T8ProdIssueRegisFields"
                       :prodCode="formData.T8ProdInfo.prodCode" :t8ProdInfoId="formData.T8ProdInfo.id"
                       :T8ProdIssueRegisFields="formData.T8ProdIssueRegisFields"
                       :prodMode="formData.T8ProdInfo.prodMode"/>
    </div>


  </div>


</template>

<script>
    import ProdInfo         from "../prodDisplay/DisplayProdInfo"
    import ProdEscrowAcct   from "../prodDisplay/DisplayProdEscrowAcct.vue"
    import T8ProdPerformance    from "../prodDisplay/DisplayProdPerformance.vue"
    import ProdLimit        from "../prodDisplay/DisplayProdLimit"
    import ProdFeeDeal      from "../prodDisplay/DisplayFeeDeal.vue"
    import ProdFee      from "../prodDisplay/DisplayFeeProd.vue"
    import ProdInvest      from "../prodDisplay/DisplayProdInvest.vue"
    import ProdValuation      from "../prodDisplay/DisplayProdValuation.vue"
    import T8ProdCalendar      from "../prodDisplay/DisplayProdCalendar.vue"
    import T8ProdBonus      from "../prodDisplay/DisplayProdBonusNew.vue"
    import T8ProdDocMods      from "../prodDisplay/DisplayT8ProdDocMods.vue"
    import T8DeclarationInfo      from "../prodDisplay/DisplayDeclarationInfo.vue"
    import ProdIssueAdjust from "../prodDisplay/DisplayProdIssueAdjust.vue"
    import ProdShareSort from "../prodDisplay/DisplayProdShareSort.vue"
    import eventBus from "@/utils/eventBus";
    export default {
      name: "M81001ProdInfoDisplay",
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
        T8DeclarationInfo,
        ProdIssueAdjust,
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
            switchSegmentValue:false,
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
            t8ProdInfoId:""
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
          T8ProdShareSort:{
            prodCode : "",
            t8ProdInfoId:""
          },
          T8ProdDocMods:{
            dataParams:[],
            prodCode : "",
            t8ProdInfoId:""
          },
          T8DeclarationInfo:{
            prodCode: "",
            t8ProdInfoId: ""
          },
          T8ProdIssueRegisFields: {
            prodCode: "",
            t8ProdInfoId: "",
          },
          showPanels: {},
          assemblyMenuType: '',
        },
        hiddenShowShareSort: {
          type: Boolean,
          default: false
        },
        T8ProdShareSort: {
          dataParams: []
        },
      },

      data() {
        return {
          envItems: [{"ratioIndex": ''}, {"coefficient": ''}],
          switchSegmentValue: false,   //金额分段切换按钮的值
          moneyList: [],
          tailingCommisionList: [],
          tailingCommisionMoneyList: [],
          riskItems: [],
          showShareSort: false,
        }
      },

      created() {
        eventBus.$on('shareSortChange', item => {
          if (item.shareSort === '1') {
            this.showShareSort = true;
          } else {
            this.showShareSort = false;
          }
        });

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

      },
      mounted() {},
    }
</script>

<style lang="scss" scoped>

  @import "../../../../styles/T81001.scss";

</style>
