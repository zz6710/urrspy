<template>
  <div class="formPanel" ref="formPanel">
    <M81001ProdInfoDisplay ref="M81001ProdInfoDisplay" :formData="formData" />
  </div>


</template>

<script>
  import M81001ProdInfoDisplay from "../../M81/prodDisplay/M81001ProdInfoDisplay.vue"
  export default {
    name: "ProdInfoFlow",
    components: {
      M81001ProdInfoDisplay
    },
    props:{
      taskInfo: {},
    },

    computed: {},
    data() {
      return {
        prodMode:'',
        prodCode:'',
        t8ProdInfoId:'',
        assemblyId:'',
        menuName:'M81001ProdAdjust',
        //     assembly:{},
        paramsData : [],
        prodAdjust:{},//调整信息
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
            prodCode : "",
            t8ProdInfoId:""
          },
          T8ProdIssueRegisFields:{
            prodCode : "",
            t8ProdInfoId:"",
          },
          showPanels:{},
          assemblyMenuType:'',

        },


      }
    },
    methods: {
      //反显
      getProdInfoData(item){
        if (this.assemblyId == 'prodInfo'){
          this.formData.T8ProdInfo = item;
        }else{
          this.httpUtil.comnQuery({
            action: 'T8ProdInfo.findT8ProdInfos',
            params: {
              prodCode : this.prodCode,
              t8ProdInfoId : this.t8ProdInfoId,
            }
          }).then(data => {
            this.formData.T8ProdInfo = data.rows[0];
            if (this.assemblyId == 'truteeInfo'){
              //托管信息
              this.formData.T8ProdEscrowAcct = item;
            }
            if (this.assemblyId == 'prodCalendar'){
              //周期信息
              this.formData.T8ProdCalendar = item;
            }
            if (this.assemblyId == 'limitInfo'){
              //产品销售信息
              this.formData.T8ProdLimit = item;
            }
            if (this.assemblyId == 'prodBonus'){
              //产品分红信息
              this.formData.T8ProdBonus = item;
            }
            if (this.assemblyId == 'prodInvest'){
              this.formData.ProdInvest = item;
              this.$refs.M81001ProdInfoDisplay.riskItems = JSON.parse(item.t8ProdRiskConfig);
            }
            if (this.assemblyId == 'prodValuation'){
              this.formData.ProdValuation = item;
            }
            if (this.assemblyId == 'prodFee'){
              //产品费用信息
              this.formData.ProdFee.dataParams = JSON.parse(item.t8ProdFees);
              // let dataParamsProdFee = this.$refs.prodFee.dataParams;
              // paramsData['t8ProdFees'] = JSON.stringify(dataParamsProdFee);

            }
            if (this.assemblyId == 'feeDeal'){
              //交易费用信息
              this.formData.ProdFeeDeal.dataParams = JSON.parse(item.t8FeeDeals);
              // let dataParamsProdFee = this.$refs.feeDeal.dataParams;
              // paramsData['t8FeeDeals'] = JSON.stringify(dataParamsProdFee);
            }
            if (this.assemblyId == 'performanceInfo'){
              //业绩报酬
              this.formData.T8ProdPerformance = item;
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

              // let dataParamsProdDocInfo = this.$refs.prodDocInfo.dataParams;
              // paramsData['t8ProdDocMods'] = JSON.stringify(dataParamsProdDocInfo);

            }


            // //申报登记要素
            // this.formData.T8DeclarationInfo = JSON.parse(item.T8DeclarationInfo);
            // //发行登记要素
            // this.formData.T8ProdIssueRegisFields = JSON.parse(item.T8ProdIssueRegisFields);


          });
        }

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
          this.prodInfoId = item.prodInfoId;
          this.prodCode = item.prodCode;
          this.prodMode = item.prodMode;
          this.assemblyId = item.assemblyMenuType;

          this.formData.assemblyMenuType = '0';
          this.$set(this.formData.showPanels,this.assemblyId,true);
          if(this.assemblyId != 'prodInfo'){
            this.$set(this.formData.showPanels,'prodInfo',true);
          }
          this.getProdInfoData(item);//反显;

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
