<template>
  <div  class="md-card k-card md-theme-default parent-div" @scroll="scrollEvent" >
    <div class="md-card-header md-card-header-text md-card-header-green" id="mainPanel">
      <div class="display-flex">


        <div class="menu-container" style="top: 87px;">
          <div class="md-message-line">
            <div class="menu-list">
              <div class="menu" v-for="(item,index) in menuItems" :key="index">
                <div class="icon-container">
                  <div class = "icon-image" :class="item.iconClass" ></div>
                </div>
                <div class="desc-container " @click="changeTab(index)"
                     @mouseenter="mouseOnItem(item) "
                     @mouseleave="mouseOutItem(item)"
                     :class="[item.mouseOver, item.alive ? item.activeClass: '']">

                  <span class="desc">{{item.desc}}</span>
                </div>
              </div>
            </div>
          </div>
        </div>






        <div class="share-containerAdd">
          <div @click="back2Page" size="mini" class="pd-button" style="margin-left: 11px;">
            <md-icon md-src="/static/images/create/back4.svg" class="pd-icon-20"/>
            <div class="pd-text" style=" margin-top: 2px;">返回</div>
          </div>
        </div>

        <div class="formPanel" ref="formPanel">
          <M81001ProdInfoDisplay ref="M81001ProdInfoDisplay" :formData="formData"/>
        </div>

      </div>
    </div>
  </div>


</template>

<script>
  import Tools from "@/utils/tools";
  import M81001ProdInfoDisplay from "../prodDisplay/M81001ProdInfoDisplay.vue"
  import eventBus from "@/utils/eventBus";
  export default {
    name: "M81001display",
    components: {
      M81001ProdInfoDisplay
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
        prodMode : "",
        prodCode : "",
        menuItems:[],//菜单渲染数组
        showPanels:{},//表单渲染
        assemblyMenuType:'',
        menuName:'',
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
          ProdShareSort:{
            prodCode: "",
            t8ProdInfoId: ""
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
            dataParams: []
          },
          showPanels: {},
          assemblyMenuType: '',
        },
        tempMneu: []/*菜单组件信息,是否份额分类控制菜单显示隐藏使用*/,
        tempMneuIndex: []/*菜单组件信息对应的下标*/,
      }
    },
    methods: {

      changeTab: function(index) {
        this.menuItems.forEach((item, i) => {
          if (i == index) {
            this.$nextTick(()=>{
              // 滑动到目的地址
              //通过传递参数跳转到指定位置
              document.getElementById(item.id).scrollIntoView({
                block:'start',
                inline:'nearest',
                behavior:'smooth'
              })
            })
          }
        })
      },

      mouseOnItem(item){
        item.mouseOver = item.activeClass
      },

      // 目录渲染
      mouseOutItem(item){
        item.mouseOver = ''
      },












      //返回按钮点击事件
      back2Page(){
        let backPath = '/main/pms/M81/M81007';
        if (this.menuName == 'ProdMeetingList'){
          backPath = '/main/pms/M81/prodConfirm/ProdMeetingList';
        }else if (this.menuName == 'ProdDeclareParamList'){
          backPath = '/main/pms/M81/prodConfirm/ProdDeclareParamList';
        }else if (this.menuName == 'ProdIssueAdjustList'){
          backPath = '/main/pms/M81/prodConfirm/ProdIssueAdjustList';
        }else if(this.menuName == 'M8ProdAdjust'){
          backPath = '/main/pms/M81/prodAdjust/M8ProdAdjust';
        }else if(this.menuName == 'M81007'){
          backPath = '/main/pms/M81/M81007';
        }else if (this.menuName == 'T8ProdAccountFindInfo'){
          backPath = '/main/pms/prodAccountInfo/T8ProdAccountFindInfo';
        } else if(this.menuName == 'prodStandBook'){
          backPath = '/main/pms/M81/prodStandBook/prodStandBook'
        }
        this.$router.push({
          path: backPath,
          query: {},
        });

      },

      //@scroll
      scrollEvent () {
        this.menuItems.forEach((item, i) => {
          let heigthTop = document.getElementById(item.id).getBoundingClientRect().top
          if(heigthTop<=100){
            item.alive = true;
          }else{
            item.alive = false;
          }
          if(i!=0 && item.alive == true){
            this.menuItems[i-1].alive = false;
          }
        });
      },


      //组件渲染
      showPanelsApp (data){
        for (const row of data.rows) {
          const assembly = {};
          //按assemblySort排序---
          this.showPanels[row.assemblyId] = true;
          assembly.desc = row.assemblyDesc;
          assembly.id = row.assemblyId;
          assembly.activeClass = row.activeClass;
          assembly.mouseOver = row.mouseOver;
          assembly.iconClass = row.iconClass;
          if(row.alive == 'true'){ assembly.alive = true } else { assembly.alive = false }
          if(row.validate == 'true'){ assembly.validate = true} else{ assembly.validate = false }
          this.menuItems.push(assembly);
        }
        this.$set(this.formData,'showPanels',this.showPanels);
        this.selectProdInfo();//反显
      },

      selectProdInfo (){
        this.formData.T8ProdInfo.prodCode = this.prodCode;
        //查询产品信息及募集信息
        if (this.showPanels.prodInfo){
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
              if (this.showPanels.prodInvest){
                this.httpUtil.comnQuery({
                  action: 'T8ProdInvest.findT8ProdInvests',
                  params: {
                    prodCode : this.prodCode,
                    t8ProdInfoId : this.formData.T8ProdInfo.id,
                  }
                }).then(data => {
                  if(data.rows.length > 0 ) {
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
                      this.$refs.M81001ProdInfoDisplay.riskItems = data1.rows;
                    })
                  }
                  this.formData.ProdInvest.prodCode = this.formData.T8ProdInfo.prodCode;
                  this.formData.ProdInvest.t8ProdInfoId = this.formData.T8ProdInfo.id;
                });
              }

              //估值信息
              if (this.showPanels.prodValuation){

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
                   this.formData.ProdValuation.prodMode = this.formData.T8ProdInfo.prodMode;
                });
              }

              //业绩报酬
              if (this.showPanels.performanceInfo){

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
                      //this.$refs.performanceInfo.switchSegmentValue=true;
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
                     //   this.$refs.performanceInfo.envItems = data.rows;
                        this.$refs.M81001ProdInfoDisplay.envItems = data.rows;
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
                     //     this.$refs.performanceInfo.switchSegmentValue=true;
                          this.$refs.M81001ProdInfoDisplay.switchSegmentValue=true;
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
                      // this.$refs.performanceInfo.moneyList = moneyList2;
                      // this.$refs.performanceInfo.tailingCommisionList= array;
                      // this.$refs.performanceInfo.tailingCommisionMoneyList= array;
                      this.$refs.M81001ProdInfoDisplay.moneyList = moneyList2;
                      this.$refs.M81001ProdInfoDisplay.tailingCommisionList= array;
                      this.$refs.M81001ProdInfoDisplay.tailingCommisionMoneyList= array;
                    });
                  }
                  this.formData.T8ProdPerformance.prodCode = this.formData.T8ProdInfo.prodCode;
                  this.formData.T8ProdPerformance.t8ProdInfoId = this.formData.T8ProdInfo.id;
                });
              }

              //托管信息
              if (this.showPanels.truteeInfo){

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
              if (this.showPanels.limitInfo){
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
              if (this.showPanels.prodBonus){

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
              if (this.showPanels.prodCalendar){

                this.httpUtil.comnQuery({
                  action: 'T8ProdCalendar.findT8ProdCalendars',
                  params: {
                    prodCode : this.prodCode
                  }
                }).then(data => {
                  if(data.rows.length> 0 ){
                    this.formData.T8ProdCalendar = data.rows[0]?data.rows[0]:{};
                    // this.$set(this.formData.ProdFee,'establishDate',this.formData.T8ProdCalendar.establishDate);
                    // this.$set(this.formData.endDate,'endDate',this.formData.T8ProdCalendar.endDate);
                    this.$set(this.formData.T8ProdBonus,"establishDate",data.rows[0].establishDate);
                  }
                  this.formData.T8ProdCalendar.prodCode = this.formData.T8ProdInfo.prodCode;
                  this.formData.T8ProdCalendar.t8ProdInfoId = this.formData.T8ProdInfo.id;
                });
              }

              //交易费用信息
              if (this.showPanels.feeDeal){

                this.httpUtil.comnQuery({
                  action: 'T8FeeDeal.findT8FeeDeals',
                  params: {
                    prodCode : this.prodCode,
                    t8ProdInfoId : this.formData.T8ProdInfo.id,
                  }
                }).then(data => {
                  if(data.rows.length> 0 ){
                    this.$set(this.formData.ProdFeeDeal, 'dataParams', data.rows);
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
              if (this.showPanels.prodFee){

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



              //产品文档模板
              if (this.showPanels.prodDocInfo){
                this.httpUtil.comnQuery({
                  action: 'T8ProdDocInfo.findT8ProdDocInfos',
                  params: {
                    t8ProdInfoId : this.formData.T8ProdInfo.id,
                  }
                }).then(data => {
                  if(data.rows.length> 0 ){
                    this.formData.T8ProdDocMods.dataParams = data.rows;
                  }
                  this.formData.T8ProdDocMods.prodCode = this.formData.T8ProdInfo.prodCode;
                  this.formData.T8ProdDocMods.prodName = this.formData.T8ProdInfo.prodName;
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


              //申报登记要素
              if (this.showPanels.declarationInfo){
                this.httpUtil.comnQuery({
                  action: 'T8ProdDeclara.findT8ProdDeclaras',
                  params: {
                    t8ProdInfoId : this.formData.T8ProdInfo.id,
                  }
                }).then(data => {
                  if(data.rows.length> 0 ){
                    this.formData.T8DeclarationInfo = data.rows[0]?data.rows[0]:{};
                  }
                  this.formData.T8DeclarationInfo.prodName = this.formData.T8ProdInfo.prodName;
                  this.formData.T8DeclarationInfo.prodCode = this.formData.T8ProdInfo.prodCode;
                  this.$set(this.formData.T8DeclarationInfo,'internalIdentCode',this.formData.T8ProdInfo.prodCode);
                  this.$set(this.formData.T8DeclarationInfo,'t8ProdInfoId',this.formData.T8ProdInfo.id);
                  let prodMode = this.formData.T8ProdInfo.prodMode;
                  if(prodMode == '1'){
                    this.$set(this.formData.T8DeclarationInfo,'productOperationMode','01');
                  }else{
                    this.$set(this.formData.T8DeclarationInfo,'productOperationMode','03');
                  }
                });
              }
              //发行登记要素
              if (this.showPanels.prodIssueAdjust){
                //查询发行信息
                this.httpUtil.comnQuery({
                  action: 'T8ProdIssueRegisFields.findProdIssueInfoByProdId',
                  params: {
                    t8ProdInfoId : this.formData.T8ProdInfo.id,
                  }
                }).then(data => {
                  if (data.rows.length > 0) {
                    this.formData.T8ProdIssueRegisFields = data.rows[0] ? data.rows[0] : {};
                  }
                  this.formData.T8ProdIssueRegisFields.prodCode = this.formData.T8ProdInfo.prodCode;
                  this.formData.T8ProdIssueRegisFields.t8ProdInfoId = this.formData.T8ProdInfo.id;
                  this.$set(this.formData.T8ProdIssueRegisFields, "openMod", '01');

                });

              }
              //份额分类信息
              this.httpUtil.comnQuery({
                action: 'ProdShareSort.findProdShareSortsNoAuth',
                params: {
                  t8ProdInfoId: this.formData.T8ProdInfo.id,
                }
              }).then(data => {
                if (data.rows.length > 0) {
                  this.formData.T8ProdShareSort.dataParams = data.rows;
                }
              });

            }
          });
        }
      },


    },








    created() {
      this.menuName = this.$route.query.menuName;
      this.prodMode = this.$route.query.prodMode;               // 产品形态
      this.prodCode = this.$route.query.prodCode;               // 产品代码
      this.assemblyMenuType = this.$route.query.assemblyMenuType;
      this.$set(this.formData,'assemblyMenuType',this.assemblyMenuType);
      this.formData.T8ProdInfo.prodMode = this.prodMode;
      this.formData.T8ProdInfo.prodCode = this.prodCode;
      //查询渲染组件
      this.httpUtil.comnQuery({
        action: 'T8ProdAssembly.findT8ProdAssemblyConfirm',
        params: {
          assemblyType : this.assemblyMenuType,
          prodCode: this.prodCode,
        },
        successAlert: false,
      }).then(data => {
        if (data.rows.length > 0) {
          //用来单独判断是否渲染份额分类组件，由isShareSort控制
          if(this.$route.query.isShareSort == '0'){
            for(let i = 0;i<data.rows.length;i++){
              if(data.rows[i].assemblyId == 'ProdShareSort'){
                this.$delete(data.rows,i);
              }
            }
          }
          console.log(data)
          this.showPanelsApp(data);
        } else {
          Tools.alert("没有取到组件参数，请配置产品信息组件参数或者产品模型组件参数");
        }
      });
      // //bus通信控制份额分类组件的显示与隐藏
      // eventBus.$on('shareSortChange', item => {
      //   if (item.shareSort === '1') {
      //     //新增份额分类标签
      //     this.menuItems.push({
      //       'activeClass': 'selected-trutee',
      //       'alive': false,
      //       'desc': '份额分类',
      //       'iconClass': 'item-trutee',
      //       'id': 'ProdShareSort',
      //       'mouseOver': null,
      //       'validate': false
      //     })
      //     //删除销售份额标签
      //     // for (var i = 0; i < this.menuItems.length; i++) {
      //     //   //删除销售信息标签,隐藏销售信息组件
      //     //   if (this.menuItems[i].id === 'limitInfo') {
      //     //     //保存菜单
      //     //     this.tempMneu.push(this.menuItems[i]);
      //     //     //保存菜单下标
      //     //     this.tempMneuIndex.push(i);
      //     //     this.menuItems.splice(i, 1);
      //     //     this.$set(this.formData.showPanels, 'limitInfo', false);
      //     //     break;
      //     //   }
      //     // }
      //   } else {
      //     for (let i = 0; i < this.menuItems.length; i++) {
      //       //删除份额分类标签
      //       if (this.menuItems[i].id === 'ProdShareSort') {
      //         this.isShareSort = true;
      //         this.menuItems.splice(i, 1);
      //         break;
      //       }
      //     }
      //     // for (let i = 0; i < this.tempMneu.length; i++) {
      //     //   if (this.tempMneu[i].id === 'limitInfo') {
      //     //     //设置菜单插入的下标
      //     //     this.menuItems.splice(this.tempMneuIndex[i], 0, this.tempMneu[i]);
      //     //     //删除菜单信息
      //     //     this.tempMneu.splice(i, 1);
      //     //     //删除对应菜单的下标
      //     //     this.tempMneuIndex.splice(i, 1);
      //     //     this.$set(this.formData.showPanels, 'limitInfo', true);
      //     //   }
      //     // }
      //   }
      // });

    },


    mounted() {
      this.$refs.formPanel.style['height'] = (document.body.clientHeight - 112)+ 'px';
    },
    model:{
      prop: 'prodMode',
      event: 'input'
    },



    watch: {
      prodCode: {
        handler: function(val) {
          this.$emit('pordDetail', val);
        }
      },
    },

  }
</script>

<style lang="scss" scoped>

  @import "../../../../styles/T81001.scss";

</style>
