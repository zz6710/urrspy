<template>
  <div class="formPanel" ref="formPanel">


    <div class="form-item prod-panel" id="prodInfoDisplay" v-if="assemblyId != 'prodInfo'">
      <div class="title">
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="基本信息"></k-field-display>
      </div>
      <ProdInfoDisplay ref="prodInfo" v-model="formData.T8ProdInfo" />
    </div>

    <M81001ProdInfoAdd ref="M81001ProdInfoAdd" :formData="formData" :hiddenShowShareSort="true"/>

      <div style="text-align: center">
        <k-btn data-functype="SUBMIT" data-size="mini" class="btn-custom-primary" :data-after-success="back2Page"
               ref="submitBtn"
               data-target="T8ProdInfoLibrary"
               :data-model="paramsData" data-descript="调整产品信息" :data-handler="saveProduct">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>提交
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="PAGE"  data-target="/main/pms/M81/prodAdjust/M8ProdAdjust">
          <i class="icon-reset" />返回</k-btn>
      </div>
  </div>


</template>

<script>
  import ProdInfoDisplay         from "../prodDisplay/DisplayProdInfo"
  import M81001ProdInfoAdd      from "../prodInfoGD/M81001ProdInfoAdd.vue"

  export default {
    name: "M81001ProdAdjust",
    components: {
      ProdInfoDisplay,M81001ProdInfoAdd
    },
    props:{
      formClass: {
        type: String,
        default: 'height: 500px;'
      },

    },

    computed: {},
    data() {
      return {
        prodMode:'',
        prodCode:'',
        assemblyId:'',
        menuName:'M81001ProdAdjust',
        assembly:{},
 //       actionUrl:'',
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

      //返回按钮点击事件
      back2Page(){
        let backPath = '/main/pms/M81/prodAdjust/M8ProdAdjust';
        this.$router.push({
          path: backPath,
          query: {},
        });

      },

      saveProduct(paramsData) {
        let isCompLite = true; // 校验是否均通过
        if (this.assembly.validate) { //是否需要需要验证
          isCompLite = this.$refs.M81001ProdInfoAdd.$refs[this.assembly.assemblyId].validateData();
        }
        if (isCompLite) {
          for (let v in this.formData) {
            paramsData[v] = JSON.stringify(this.formData[v]);
          }

          let paramsAdjust = {};
          this.$set(paramsAdjust, 'adjustCause', this.prodAdjust.adjustCause);
          this.$set(paramsAdjust, 'adjustType', this.assemblyId);
          this.$set(paramsAdjust, 't8ProdInfoId', this.formData.T8ProdInfo.id);
          this.$set(paramsAdjust, 'prodCode', this.formData.T8ProdInfo.prodCode);
          paramsData["ProdStatusChange"] = JSON.stringify(paramsAdjust);
          paramsData['assemblyMenuType'] = this.assemblyId;
          if (this.assemblyId == 'prodInfo') {


          }
          if (this.assemblyId == 'truteeInfo') {


          }
          if (this.assemblyId == 'prodCalendar') {


          }
          if (this.assemblyId == 'limitInfo') {


          }
          if (this.assemblyId == 'prodBonus') {


          }
          if (this.assemblyId == 'prodInvest') {
            let configs = this.$refs.M81001ProdInfoAdd.$refs.prodInvest.riskItems;
            paramsData['t8ProdRiskConfig'] = JSON.stringify(configs);
          }
          if (this.assemblyId == 'prodValuation') {


          }
          if (this.assemblyId == 'prodFee') {

            let dataParamsProdFee = this.$refs.M81001ProdInfoAdd.$refs.prodFee.dataParams;
            paramsData['t8ProdFees'] = JSON.stringify(dataParamsProdFee);

          }
          if (this.assemblyId == 'feeDeal') {
            let dataParamsProdFee = this.$refs.M81001ProdInfoAdd.$refs.feeDeal.dataParams;
            paramsData['t8FeeDeals'] = JSON.stringify(dataParamsProdFee);
          }
          if (this.assemblyId == 'performanceInfo') {

            let envItems = this.$refs.M81001ProdInfoAdd.$refs.performanceInfo.envItems;
            let tailingCommisionList = this.$refs.M81001ProdInfoAdd.$refs.performanceInfo.tailingCommisionList;
            let value = this.formData.T8ProdPerformance;
            if (value.baseType == '3' || value.baseType == '5') {
              paramsData["t8ProdPerformanceRatios"] = JSON.stringify(envItems);
            }
            if (value.baseType == '2') {
              paramsData["t8PrjTailingCommisionList"] = JSON.stringify(tailingCommisionList);
              paramsData["t8PrjFeeLists"] = JSON.stringify(tailingCommisionList);
            }
          }
          if (this.assemblyId == 'prodDocInfo') {
            let dataParamsProdDocInfo = this.$refs.M81001ProdInfoAdd.$refs.prodDocInfo.dataParams;
            paramsData['t8ProdDocMods'] = JSON.stringify(dataParamsProdDocInfo);

          }
          //add by zhangchangsi 20211202 添加产品代码、审批流过滤判断产品代码是否有值时使用时使用
          paramsData['prodCode'] = this.prodCode;
          //提交数据
          this.httpUtil.comnUpdate({
            action: 'T8ProdAllInfoAdjust.addT8ProdInfoAdjust',
            params: paramsData,
            successAlert: true
          }).then(data => {
            this.$refs.submitBtn.loading = false;
          });
        } else {
          return false;
        }

      },
      //反显
      selectProdInfo (){
        this.formData.T8ProdInfo.prodCode = this.prodCode;
        //查询产品信息及募集信息
        if (this.assemblyId){
          this.httpUtil.comnQuery({
            action: 'T8ProdInfo.findT8ProdInfos',
            params: {
              prodCode : this.prodCode,
              t8ProdInfoId : this.formData.T8ProdInfo.id,
            }
          }).then(data => {
            if(data.rows.length > 0 ){
              this.formData.T8ProdInfo = data.rows[0];
              this.$set(this.formData.ProdFee,'establishDate',this.formData.T8ProdInfo.establishDate);
              this.$set(this.formData.ProdFee,'endDate',this.formData.T8ProdInfo.endDate);
              //投资信息
              if (this.assemblyId == 'prodInvest'){
                this.httpUtil.comnQuery({
                  action: 'T8ProdInvest.findT8ProdInvests',
                  params: {
                    prodCode : this.prodCode,
                    t8ProdInfoId : this.formData.T8ProdInfo.id,
                  }
                }).then(data => {
                  if(data.rows.length > 0 ){
                    this.formData.ProdInvest = data.rows[0] ? data.rows[0] : {};
                    this.httpUtil.comnQuery({
                      action: 'T8ProdRiskConfig.findProdRiskOther',
                      params: {
                        t8ProdInfoId: this.formData.T8ProdInfo.id,
                      }
                    }).then(data1 => {
                      let prodRisk = '';
                      for (let i in data1.rows) {
                        prodRisk += data1.rows[i].prodRisk;
                        prodRisk += ',';
                      }
                      prodRisk = prodRisk.substring(0, prodRisk.length - 1);
                      this.$set(this.formData.ProdInvest, 'prodRisk', prodRisk);
                      this.$refs.M81001ProdInfoAdd.riskItems = data1.rows;

                    })
                  }
                  this.formData.ProdInvest.prodCode = this.formData.T8ProdInfo.prodCode;
                  this.formData.ProdInvest.t8ProdInfoId = this.formData.T8ProdInfo.id;
                });
              }

              //估值信息
              if (this.assemblyId == 'prodValuation'){

                this.httpUtil.comnQuery({
                  action: 'T8ProdInvest.findProdValuations',
                  params: {
                    prodCode : this.prodCode,
                    t8ProdInfoId : this.formData.T8ProdInfo.id,
                  }
                }).then(data => {
                  if(data.rows!=null && data.rows.length > 0 ){
                    this.formData.ProdValuation = data.rows[0]?data.rows[0]:{};
                  }
                  this.formData.ProdValuation.prodCode = this.formData.T8ProdInfo.prodCode;
                  this.formData.ProdValuation.t8ProdInfoId = this.formData.T8ProdInfo.id;
                });
              }

              //业绩报酬
              if (this.assemblyId == 'performanceInfo'){

                this.httpUtil.comnQuery({
                  action: 'T8ProdPerformance.findT8ProdPerformances',
                  params: {
                    prodCode : this.prodCode,
                    t8ProdInfoId : this.formData.T8ProdInfo.id,
                  }
                }).then(data => {
                  if(data.rows.length > 0 ){
                    this.formData.T8ProdPerformance = data.rows[0];
                    let baseType = this.formData.T8ProdPerformance.baseType;
                    if(baseType=='2'){
                      //this.$refs.M81001ProdInfoAdd.switchSegmentValue=true;
                    }
                    if(baseType=='3' || baseType=='5'){
                    }
                    //查询指数信息
                    this.httpUtil.comnQuery({
                      action: 'T8ProdPerformanceRatio.findT8ProdPerformanceRatio',
                      params: {
                        t8ProdPerformanceId : this.formData.T8ProdPerformance.id,
                      }
                    }).then(data => {
                      if(data.rows.length > 0 ){
                        this.$refs.M81001ProdInfoAdd.envItems = data.rows;
                      }
                    });
                    //查询分段信息
                    this.httpUtil.comnQuery({
                      action: 'T8PrjFeeList.findPerformanceT8PrjFeeLists',
                      params: {
                        feeCode : this.formData.T8ProdInfo.id,
                      }
                    }).then(data => {
                      let moneyList2 = [];
                      let array = [];
                      if(data.rows.length > 1 ){
                        if(baseType=='2'){
                          this.$refs.M81001ProdInfoAdd.switchSegmentValue=true;
                        }
                        if(baseType=='3' || baseType=='5'){
                        }
                        for(let j=0;j<data.rows.length;j++){
                          array.push(data.rows[j]);
                          if(j<data.rows.length-1){
                            moneyList2.push(parseInt(data.rows[j].dimension2Max));
                          }
                        }
                      }
                      this.$refs.M81001ProdInfoAdd.moneyList = moneyList2;
                      this.$refs.M81001ProdInfoAdd.tailingCommisionList= array;
                      this.$refs.M81001ProdInfoAdd.tailingCommisionMoneyList= array;
                    });
                  }
                  this.formData.T8ProdPerformance.prodCode = this.formData.T8ProdInfo.prodCode;
                  this.formData.T8ProdPerformance.t8ProdInfoId = this.formData.T8ProdInfo.id;
                  this.formData.T8ProdPerformance.prodIsShareSort = this.formData.T8ProdInfo.isShareSort == 0 ? false :true;
                });
              }

              //托管信息
              if (this.assemblyId == 'truteeInfo'){

                this.httpUtil.comnQuery({
                  action: 'T8ProdTrutee.findT8ProdTrutees',
                  params: {
                    prodCode : this.prodCode,
                    t8ProdInfoId : this.formData.T8ProdInfo.id,
                  }
                }).then(data => {
                  if(data.rows.length> 0 ){
                    this.formData.T8ProdEscrowAcct = data.rows[0]?data.rows[0]:{};
                  }
                  this.formData.T8ProdEscrowAcct.prodCode = this.formData.T8ProdInfo.prodCode;
                  this.formData.T8ProdEscrowAcct.t8ProdInfoId = this.formData.T8ProdInfo.id;
                  this.$set(this.formData.T8ProdDocMods,"t8TruteeInfoId",this.formData.T8ProdEscrowAcct.t8TruteeInfoId);
                  this.$set(this.formData.T8ProdDocMods,"t8OutTruteeInfoId",this.formData.T8ProdEscrowAcct.t8OutTruteeInfoId);
                });
              }

              //产品销售信息
              if (this.assemblyId == 'limitInfo'){
                this.httpUtil.comnQuery({
                  action: 'T8ProdSale.findT8ProdSales',
                  params: {
                    prodCode : this.prodCode,
                    t8ProdInfoId : this.formData.T8ProdInfo.id,
                  }
                }).then(data => {
                  if(data.rows.length> 0 ){
                    this.formData.T8ProdLimit = data.rows[0]?data.rows[0]:{};
                  }
                  this.formData.T8ProdLimit.prodCode = this.formData.T8ProdInfo.prodCode;
                  this.formData.T8ProdLimit.t8ProdInfoId = this.formData.T8ProdInfo.id;

                });
              }

              //产品分红信息
              if (this.assemblyId == 'prodBonus'){

                this.httpUtil.comnQuery({
                  action: 'T8ProdBonusNew.findT8ProdBonusNews',
                  params: {
                    prodCode : this.prodCode,
                    t8ProdInfoId : this.formData.T8ProdInfo.id,
                  }
                }).then(data => {
                  if(data.rows!=null&&data.rows.length> 0 ){
                    this.formData.T8ProdBonus = data.rows[0]?data.rows[0]:{};
                  }
                  this.formData.T8ProdBonus.prodCode = this.formData.T8ProdInfo.prodCode;
                  this.formData.T8ProdBonus.t8ProdInfoId = this.formData.T8ProdInfo.id;
                });
              }

              //周期信息
              if (this.assemblyId == 'prodCalendar'){

                this.httpUtil.comnQuery({
                  action: 'T8ProdCalendar.findT8ProdCalendars',
                  params: {
                    prodCode : this.prodCode
                  }
                }).then(data => {
                  if(data.rows.length> 0 ){
                    this.formData.T8ProdCalendar = data.rows[0]?data.rows[0]:{};
                  }
                  this.formData.T8ProdCalendar.prodCode = this.formData.T8ProdInfo.prodCode;
                  this.formData.T8ProdCalendar.t8ProdInfoId = this.formData.T8ProdInfo.id;
                });
              }

              //交易费用信息
              if (this.assemblyId == 'feeDeal'){

                this.httpUtil.comnQuery({
                  action: 'T8FeeDeal.findT8FeeDeals',
                  params: {
                    prodCode : this.prodCode,
                    t8ProdInfoId : this.formData.T8ProdInfo.id,
                  }
                }).then(data => {
                  if(data.rows.length> 0 ){
                    this.formData.ProdFeeDeal.dataParams = data.rows;
                    for(let i = 0;i < this.formData.ProdFeeDeal.dataParams.length; i++){
                      this.httpUtil.comnQuery({
                        action: 'T8PrjFeeList.findT8PrjFeeLists',
                        params: {
                          feeDealId : this.formData.ProdFeeDeal.dataParams[i].id,
                        }
                      }).then(rows => {
                        this.$set(this.formData.ProdFeeDeal.dataParams[i],'t8PrjFeeLists',rows.rows);
                      });
                    }
                  }
                  this.formData.ProdFeeDeal.prodCode = this.formData.T8ProdInfo.prodCode;
                  this.formData.ProdFeeDeal.t8ProdInfoId = this.formData.T8ProdInfo.id;
                });
              }

              //产品费用信息
              if (this.assemblyId == 'prodFee'){

                this.httpUtil.comnQuery({
                  action: 'T8FeeProd.findT8FeeProds',
                  params: {
                    prodCode : this.prodCode,
                    t8ProdInfoId : this.formData.T8ProdInfo.id,
                  }
                }).then(data => {
                  if(data.rows.length> 0 ){
                    this.formData.ProdFee.dataParams = data.rows;
                  }
                  this.formData.ProdFee.prodCode = this.formData.T8ProdInfo.prodCode;
                  this.formData.ProdFee.t8ProdInfoId = this.formData.T8ProdInfo.id;
                });
              }

              //产品模板
              if (this.assemblyId == 'prodDocInfo'){
                this.httpUtil.comnQuery({
                  action: 'T8ProdDocInfo.findT8ProdDocInfos',
                  params: {
                    t8ProdInfoId : this.formData.T8ProdInfo.id,
                  }
                }).then(data => {
                  if(data.rows.length> 0 ){
                    this.formData.T8ProdDocMods.dataParams = data.rows;
                  }
                  this.formData.T8DeclarationInfo.prodName = this.formData.T8ProdInfo.prodName;
                  this.formData.T8ProdDocMods.prodCode = this.formData.T8ProdInfo.prodCode;
                  this.formData.T8ProdDocMods.prodMode = this.formData.T8ProdInfo.prodMode;
                  this.formData.T8ProdDocMods.t8ProdInfoId = this.formData.T8ProdInfo.id;
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
                });
              }

            }
          });
        }

      },


    },








    created() {
      this.prodAdjust = this.$route.query;
      this.prodMode = this.$route.query.prodMode;               // 产品形态
      this.prodCode = this.$route.query.prodCode;               // 产品代码
      this.assemblyId = this.$route.query.assemblyId;
      this.formData.T8ProdInfo.prodMode = this.prodMode;
      this.formData.T8ProdInfo.prodCode = this.prodCode;
      this.httpUtil.comnQuery({
        action: 'T8ProdAssembly.findProdAssemblyInfo',
        params: {
          assemblyId:this.assemblyId
        },
      }).then(data => {
        if(data.rows.length > 0 ){
          const row = data.rows[0];
          this.assembly = row;
          if(row.alive == 'true'){ this.$set(this.assembly,'alive',true);} else {this.$set(this.assembly,'alive',false);}
          if(row.validate == 'true'){this.$set(this.assembly,'validate',true);} else{this.$set(this.assembly,'validate',false);}
        }

        this.formData.assemblyMenuType = '0';
        this.$set(this.formData.showPanels,this.assemblyId,true);

      });
      this.selectProdInfo();
    },


    mounted() {


    },
    model:{},



    watch: {},

  }
</script>

<style lang="scss" scoped>

  @import "../../../../styles/T81001.scss";

</style>
