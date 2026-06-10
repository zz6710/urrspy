<template>
  <div class="formPanel" ref="formPanel">

    <div class="formPanel" ref="formPanel">
      <M81001ProdInfoDisplay ref="M81001ProdInfoDisplay" :formData="formData"/>
    </div>

  </div>


</template>

<script>
  import M81001ProdInfoDisplay from "../../M81/prodDisplay/M81001ProdInfoDisplay.vue"

  import Tools from "@/utils/tools";
  import eventBus from "@/utils/eventBus";
  export default {
    name: "ProdInfoAllFlow",
    components: {
      M81001ProdInfoDisplay
    },
    props:{
      taskInfo: {},
    },
    data() {
      return {
        prodInfoId:"",
        prodMode : "",
        prodCode : "",
        assemblyMenuType:'',
        menuItems:[],//菜单渲染数组
        showPanels:{},//表单渲染
        formData: {
          //产品基本信息
          T8ProdInfo: {
            id                  : "",
            prodCode            : "",
            prodMode            : "",
            raiseType           : "",
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
          T8ProdDocMods:{
            dataParams:[],
            prodCode : "",
            t8ProdInfoId: ""
          },
          T8DeclarationInfo: {
            prodCode: "",
            t8ProdInfoId: ""
          },
          T8ProdIssueRegisFields: {
            prodCode: "",
            t8ProdInfoId: "",
          },
          T8ProdShareSort: {
            dataParams: [],
            prodCode: "",
            t8ProdInfoId: "",
          },
          showPanels: {},
          assemblyMenuType: '',
        },
        tempMneu: []/*菜单组件信息,是否份额分类控制菜单显示隐藏使用*/,
        tempMneuIndex: []/*菜单组件信息对应的下标*/,
      }
    },

    computed: {},
    created() {
      this.httpUtil
        .ajax({
          url: "/wf/formData/getSubmitParamsByProcessInstanceId.json",
          params: {processInstanceId: this.taskInfo.processInstanceId,_wfProcessInstanceId:this.taskInfo.processInstanceId}
        }).then(res => {
        if (res.data) {
          let item = JSON.parse(res.data.submitParams);
          let t8ProdInfo = JSON.parse(item.T8ProdInfo);
          this.prodInfoId = t8ProdInfo.prodInfoId;
          this.prodCode = t8ProdInfo.prodCode;
          this.prodMode = t8ProdInfo.prodMode;
          this.prodName = t8ProdInfo.prodName;
          this.assemblyMenuType = item.assemblyMenuType;
          this.$set(this.formData,'assemblyMenuType',this.assemblyMenuType);
          //查询渲染组件
          this.httpUtil.comnQuery({
            action: 'T8ProdAssembly.findT8ProdAssemblyConfirm',
            params: {
              assemblyType : this.assemblyMenuType,
              prodCode : this.prodCode,
            },
            successAlert: false,
          }).then(data => {
            if(data.rows.length > 0 ){
              for (const row of data.rows) {
                this.$set(this.showPanels,row.assemblyId,true)
              }
              this.$set(this.formData, 'showPanels', this.showPanels);
              this.getProdInfoData(item);//反显
            } else {
              Tools.alert("没有取到组件参数，请配置产品信息组件参数或者产品模型组件参数");
            }
          });

        }
      });
      //bus通信控制份额分类组件的显示与隐藏
      eventBus.$on('shareSortChange', item => {
        if (item.shareSort === '1') {//是份额分类
          //删除销售份额标签
          // this.$set(this.formData.showPanels, 'limitInfo', false);

        } else {
          // this.$set(this.formData.showPanels, 'limitInfo', true);
        }
      });
    },

    mounted() {
      this.$refs.formPanel.style['height'] = (document.body.clientHeight - 112)+ 'px';
    },

    methods: {
      getProdInfoData(item){
        console.log(item);
        this.formData.T8ProdInfo = JSON.parse(item.T8ProdInfo);
        this.formData.ProdInvest = JSON.parse(item.ProdInvest);
        this.$refs.M81001ProdInfoDisplay.riskItems = JSON.parse(item.t8ProdRiskConfig);
        this.formData.ProdValuation = JSON.parse(item.ProdValuation);

        //托管信息
        this.formData.T8ProdEscrowAcct = JSON.parse(item.T8ProdEscrowAcct);

        //产品销售信息
        this.formData.T8ProdLimit = JSON.parse(item.T8ProdLimit);
        //产品分红信息
        this.formData.T8ProdBonus = JSON.parse(item.T8ProdBonus);
        //周期信息
        this.formData.T8ProdCalendar = JSON.parse(item.T8ProdCalendar);
        //交易费用信息
        this.$set(this.formData.ProdFeeDeal, 'dataParams', JSON.parse(item.t8FeeDeals));
        // for(let i = 0;i < this.formData.ProdFeeDeal.dataParams.length; i++) {
        //   this.httpUtil.comnQuery({
        //     action: 'T8PrjFeeList.findT8PrjFeeLists',
        //     params: {
        //       feeDealId: this.formData.ProdFeeDeal.dataParams[i].id,
        //     }
        //   }).then(rows => {
        //     this.$set(this.formData.ProdFeeDeal.dataParams[i], 't8PrjFeeLists', rows.rows);
        //   });
        // }
        //产品费用信息
        this.$set(this.formData.ProdFee, 'dataParams', JSON.parse(item.t8ProdFees));
        //产品文档模板
        this.$set(this.formData.T8ProdDocMods, 'dataParams', JSON.parse(item.t8ProdDocMods));
        //份额信息
        this.$set(this.formData.T8ProdShareSort, 'dataParams', JSON.parse(item.T8ProdShareSort).dataParams)
        console.log(JSON.parse(item.T8ProdShareSort).dataParams);
        let prodMode_ = this.formData.T8ProdInfo.prodMode;
        let raiseType_ = this.formData.T8ProdInfo.raiseType;
        let docTypeDict = '';
        if (prodMode_ == '1' && raiseType_ == '01') {
          docTypeDict = 't8_temp_type_fb_gm';
        } else if (prodMode_ == '2' && raiseType_ == '01') {
          docTypeDict = 't8_temp_type_tt_gm';
        } else if (prodMode_ == '3' && raiseType_ == '01') {
          docTypeDict = 't8_temp_type_zq_gm';
        } else if (prodMode_ == '1' && raiseType_ == '02') {
          docTypeDict = 't8_temp_type_fb_sm';
        }else if (prodMode_ == '2' && raiseType_=='02'){
          docTypeDict = 't8_temp_type_tt_sm';
        }else if (prodMode_ == '3' && raiseType_=='02'){
          docTypeDict = 't8_temp_type_zq_sm';
        }else if (prodMode_ == '4'){
          docTypeDict = 't8_temp_type_hb';
        }
        this.$set(this.formData.T8ProdDocMods,'docTypeDict',docTypeDict);


        //申报登记要素
        this.formData.T8DeclarationInfo = JSON.parse(item.T8DeclarationInfo);
        //发行登记要素
        this.formData.T8ProdIssueRegisFields = JSON.parse(item.T8ProdIssueRegisFields);


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


      },

    },



    watch: {},

  }
</script>

<style lang="scss" scoped>

  @import "../../../../styles/T81001.scss";

</style>
