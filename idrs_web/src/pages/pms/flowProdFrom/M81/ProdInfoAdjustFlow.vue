<template>
  <div class="formPanel" ref="formPanel">

    <div class="formPanel" ref="formPanel">
      <M81001ProdInfoDisplay ref="M81001ProdInfoDisplay" :formData="formData"
                             :hiddenShowShareSort="hiddenShowShareSort"/>
    </div>

  </div>


</template>

<script>
  import M81001ProdInfoDisplay from "../../M81/prodDisplay/M81001ProdInfoDisplay.vue"
  export default {
    name: "ProdInfoAdjustFlow",
    components: {
      M81001ProdInfoDisplay
    },
    props:{
      taskInfo: {},
    },

    computed: {},
    data() {
      return {
        prodInfoId:'',
        prodMode:'',
        prodCode:'',
        assemblyId:'',
        formData: {
          //产品基本信息
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
        hiddenShowShareSort: true,
      }
    },
    methods: {
      //反显
      getProdInfoData(item){
        this.formData.T8ProdInfo = JSON.parse(item.T8ProdInfo);
        if (this.assemblyId == 'truteeInfo'){
        //托管信息
          this.formData.T8ProdEscrowAcct = JSON.parse(item.T8ProdEscrowAcct);
        }
        if (this.assemblyId == 'prodCalendar'){
          //周期信息
          this.formData.T8ProdCalendar = JSON.parse(item.T8ProdCalendar);
        }
        if (this.assemblyId == 'limitInfo'){
          //产品销售信息
          this.formData.T8ProdLimit = JSON.parse(item.T8ProdLimit);
        }
        if (this.assemblyId == 'prodBonus'){
          //产品分红信息
          this.formData.T8ProdBonus = JSON.parse(item.T8ProdBonus);
        }
        if (this.assemblyId == 'prodInvest'){
          this.formData.ProdInvest = JSON.parse(item.ProdInvest);
          this.$refs.M81001ProdInfoDisplay.riskItems = JSON.parse(item.t8ProdRiskConfig);
        }
        if (this.assemblyId == 'prodValuation'){
          this.formData.ProdValuation = JSON.parse(item.ProdValuation);
        }
        if (this.assemblyId == 'prodFee'){
          //产品费用信息
          this.formData.ProdFee.dataParams = JSON.parse(item.t8ProdFees);
        }
        if (this.assemblyId == 'feeDeal'){
          //交易费用信息
          this.formData.ProdFeeDeal.dataParams = JSON.parse(item.t8FeeDeals);
        }
        if (this.assemblyId == 'performanceInfo'){
          //业绩报酬
          this.formData.T8ProdPerformance = JSON.parse(item.T8ProdPerformance);
          let baseType = this.formData.T8ProdPerformance.baseType;
          let moneyList = [];
          let t8PrjFeeLists = [];
            this.$nextTick(()=> {
            if (baseType == '3' || baseType == '5') {
              this.$refs.M81001ProdInfoDisplay.envItems = JSON.parse(item.t8ProdPerformanceRatios);
            } else if (baseType == '2') {
              t8PrjFeeLists = JSON.parse(item.t8PrjFeeLists);
              if (t8PrjFeeLists.length > 1) {
                this.$refs.M81001ProdInfoDisplay.switchSegmentValue = true;
                for (let j = 0; j < t8PrjFeeLists.length; j++) {
                  if (j < t8PrjFeeLists.length - 1) {
                    moneyList.push(parseInt(t8PrjFeeLists[j].dimension2Max));
                  }
                }
              }
            }
            this.$refs.M81001ProdInfoDisplay.moneyList = moneyList;
            this.$refs.M81001ProdInfoDisplay.tailingCommisionList = t8PrjFeeLists;
            this.$refs.M81001ProdInfoDisplay.tailingCommisionMoneyList = t8PrjFeeLists;
          });
        }


        if (this.assemblyId == 'prodDocInfo'){
          //产品文档模板
          this.formData.T8ProdDocMods.dataParams = JSON.parse(item.t8ProdDocMods);
          let prodMode_ = this.formData.T8ProdInfo.prodMode;
          let raiseType_ = this.formData.T8ProdInfo.raiseType;
          let docTypeDict = '';
          if(prodMode_ == '1' && raiseType_=='01'){
            docTypeDict = 't8_temp_type_fb_gm';
          }else if (prodMode_ == '2' && raiseType_=='01'){
            docTypeDict = 't8_temp_type_tt_gm';
          }else if (prodMode_ == '3' && raiseType_=='01'){
            docTypeDict = 't8_temp_type_zq_gm';
          }else if (prodMode_ == '1' && raiseType_=='02'){
            docTypeDict = 't8_temp_type_fb_sm';
          }else if (prodMode_ == '2' && raiseType_=='02'){
            docTypeDict = 't8_temp_type_tt_sm';
          }else if (prodMode_ == '3' && raiseType_=='02'){
            docTypeDict = 't8_temp_type_zq_sm';
          }else if (prodMode_ == '4'){
            docTypeDict = 't8_temp_type_hb';
          }
          this.$set(this.formData.T8ProdDocMods,'docTypeDict',docTypeDict);
        }


        //申报登记要素
        this.formData.T8DeclarationInfo = JSON.parse(item.T8DeclarationInfo);
        //发行登记要素
        this.formData.T8ProdIssueRegisFields = JSON.parse(item.T8ProdIssueRegisFields);
      },


    },








    created() {
      let _this = this
      this.httpUtil
        .ajax({
          url: "/wf/formData/getSubmitParamsByProcessInstanceId.json",
          params: {processInstanceId: this.taskInfo.processInstanceId,_wfProcessInstanceId:this.taskInfo.processInstanceId}
        }).then(res => {
        if (res.data) {
          let item = JSON.parse(res.data.submitParams);
          //查询渲染组件
          this.assemblyId = item.assemblyMenuType;
          //如果不是份额分类组件id，将份额分类组件隐藏
          if (this.assemblyId != 'prodShareSort') {
            this.hiddenShowShareSort = true;
          } else {
            this.hiddenShowShareSort = false;
          }
          this.formData.assemblyMenuType = '0';
          this.$set(this.formData.showPanels, this.assemblyId, true);
          if (this.assemblyId != 'prodInfo') {
            this.$set(this.formData.showPanels, 'prodInfo', true);
          }
          this.getProdInfoData(item);//反显;
          let t8ProdInfo = JSON.parse(item.T8ProdInfo);
          this.prodInfoId = t8ProdInfo.prodInfoId;
          this.prodCode = t8ProdInfo.prodCode;
          this.prodMode = t8ProdInfo.prodMode;
        }
      });
    },




    mounted() {},
    model:{},
    watch: {},

  }
</script>

<style lang="scss" scoped>

  @import "../../../../styles/T81001.scss";

</style>
