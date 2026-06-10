<template>
  <div>
    <template>
      <md-card class="box-card" style="overflow: visible;position: unset">
        <md-card-header class="md-card-header-text md-card-header-green" style="margin-right: 0;">
          <div class="search-header">
            <div class="card-icon" :style="iconStyle">
              <md-icon md-src="/static/svg/form.svg"></md-icon>
            </div>
            <div>
              <i class="el-icon-d-caret" @click="show"></i>
            </div>
          </div>
        </md-card-header>

        <div slot="header" class="clearfix" style="text-align:right">

        </div>
        <div class="show-form" id="show-form">
          <k-form ref="searchForm" :data-col="0">
            <slot></slot>
          </k-form>
        </div>

        <div class="k-form-search-footer" style="width:100%;text-align: center;">
          <k-btn slot="button" class="btn-custom-primary" style="width: 130px" data-functype="SUBMIT"
                 data-action="T8ProdIssueRegister.addT8ProdIssueRegister" data-target="t8IssuanceRegisterGrid"
                 :data-model="this.RegisterData"
                 :data-after-success="loadEdition" v-if="global.getProdIfUser(this.RegisterData.t8ProdInfoId)&&
                 global.isShowAuthorityButton('T8ProdIssueRegister.addT8ProdIssueRegister')"
                 v-show="showGenerate"
                 :data-confirm="true">
            <md-icon md-src="/static/svg/add.svg"/>
            生成产品登记要素
          </k-btn>

          <k-btn class="btn-custom-plain" data-functype="PAGE" data-target="/main/pms/issuanceRegister/issuanceRegister">
            返回
          </k-btn>
        </div>
      </md-card>
    </template>

    <k-form ref="form2"  :data-col="1" data-input-width="150px"
            data-label-width="62px" data-total-width="1018px">
      <k-form-item>
        <k-field-select  ref="editionFrom" v-model="formData.edition" :data-data="editionData"
                        data-display-field="edition" data-value-field="edition" @data-on-change="loadGrid"
                        data-placeholder="版本..."/>
      </k-form-item>
      <k-btn class="md-rose" data-functype="EXPORT" data-target="t8IssuanceRegisterGrid"
             :data-export-name="'产品发行登记'">
        <md-icon>cloud_download</md-icon>导出
      </k-btn>
    </k-form>



    <k-grid ref="t8IssuanceRegisterGrid" :data-autoload="true"
            data-action="T8ProdIssueRegister.findT8ProdIssueRegisters"
            :data-before-load="beforePopupLoad"
            data-operate-column="true"
            :data-display="false"
            @data-row-select="selectRowLoad">
      <k-grid-column data-align="center" data-header="产品登记编码" data-name="registCode" data-width="120"/>
      <k-grid-column data-align="center" data-header="理财产品代码" data-name="prodCode" data-width="100"/>
      <!-- <k-grid-column data-align="center" data-header="模式" data-name="prodMode" data-width="100"/> -->
      <k-grid-column data-align="center" data-header="发行机构代码" data-name="orgId" data-width="100"/>
      <k-grid-column data-align="center" data-header="募集起始日"  data-name="applyStartDate" data-width="100"/>
      <k-grid-column data-align="center" data-header="募集结束日"  data-name="applyEndDate" data-width="100"/>
      <k-grid-column data-align="center" data-header="产品成立日"  data-name="establishDate" data-width="100"/>
      <k-grid-column data-align="center" data-header="产品到期日"  data-name="endDate" data-width="100"/>
      <k-grid-column data-align="center" data-header="管理方式" data-name="manageMethod"  data-width="100"/>
      <k-grid-column data-hidden="true" data-align="center" data-header="是否为结构化（分级）产品"   data-name="isStructural"/>
      <k-grid-column data-hidden="true" data-align="center" data-header="业绩比较基准上限" data-name="performBenchmarksUpper"/>
      <k-grid-column data-hidden="true" data-align="center" data-header="业绩比较基准下限" data-name="performBenchmarksLower"/>
      <k-grid-column data-hidden="true" data-align="center" data-header="业绩比较基准说明" data-name="performBenchmarksDesc"/>
      <k-grid-column data-hidden="true" data-align="center" data-header="开放模式" data-name="openMod"   />
      <k-grid-column data-hidden="true" data-align="center" data-header="规律开放周期" data-name="openCalendar"  />
      <k-grid-column data-hidden="true" data-align="center" data-header="其他规律开放周期说明（天）" data-name="otherRegularOpenCycle"/>
      <k-grid-column data-hidden="true" data-align="center" data-header="无规律开放说明" data-name="irregularOpenDesc"/>
      <k-grid-column data-hidden="true" data-align="center" data-header="首次开放周期起始日" data-name="firstOpenStartDate"/>
      <k-grid-column data-hidden="true" data-align="center" data-header="节假日是否开放" data-name="isOpen" />
      <k-grid-column data-hidden="true" data-align="center" data-header="平均开放次数（年化）" data-name="averageYearOpenTimes"/>
      <k-grid-column data-hidden="true" data-align="center" data-header="开放期业务" data-name="openDuringBusiness" />
      <k-grid-column data-hidden="true" data-align="center" data-header="开放期业务说明" data-name="openDuringBusinessDesc"/>
      <k-grid-column data-hidden="true" data-align="center" data-header="资金托管账号" data-name="fundTrusteeshipAccountNo"/>
      <k-grid-column data-hidden="true" data-align="center" data-header="资金托管账户" data-name="fundTrusteeshipAccount"/>
      <k-grid-column data-align="center" data-header="版本" data-name="edition" data-export="false"  data-width="100"/>
      <k-grid-column data-align="center" data-header="确认状态" data-dict="confirm_status" data-export="false" data-name="status" data-width="100"/>
      <k-grid-column data-align="center" data-header="创建人" data-name="crtUser" data-export="false" data-width="100"/>
      <k-grid-column data-align="center" data-header="创建时间" data-type="timestamp" data-export="false" data-name="crtDate"  data-width="150"/>
      <k-grid-column data-align="center" data-header="确认人" data-name="updUser" data-export="false" data-width="100"/>
      <k-grid-column data-align="center" data-header="确认时间" data-type="timestamp" data-export="false" data-name="updDate"  data-width="150"/>
      <template slot="operate" slot-scope="scope">
        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.edition)">
          <k-btn class="md-info md-just-icon md-simple" :data-handler="checkSeconds" data-functype="POPUP" data-size="mini"
                 data-target="addT81004Popup" :data-disabled="scope.row.row.status=='1'" v-model="scope.row.row"
                 data-descript="确认产品发行登记" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
                 global.isShowAuthorityButton('T8ProdIssueRegister.updateStatus')"
                 v-show="showConfirmIssueInfo">
            <md-icon>done</md-icon>
          </k-btn>
        </div>

        <k-btn class="md-info md-just-icon md-simple" data-descript="产品发行要素版本详情" data-functype="POPUP" data-size="mini"
               data-target="editT8ProdIssuePopupV" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)">
          <md-icon>library_books</md-icon>
        </k-btn>
<!--        <k-btn class="md-info md-just-icon md-simple" :data-disabled="scope.row.row.status=='0'" :data-download-name="'产品发行登记.xls'"-->
<!--               data-descript="导出产品发行登记信息" data-functype="DOWNLOAD" data-size="small"-->
<!--               data-url="/download/server/PmsApp/issuanceRegister/downloadT8IssuanceRegister.json" v-model="scope.row.row">-->
<!--          <md-icon>cloud_download</md-icon>-->
<!--        </k-btn>-->

      </template>
    </k-grid>



    <!--    添加确认信息弹出框   -->
    <k-popup ref="addT81004Popup" >
      <div  dataLabelWidth="170px" dataInputWidth="300px">
        <ProdIssueAdjust ref="prodIssueAdjustP" v-model="formDataLoad" :T8ProdIssueRegisFields="formDataLoad"
                         :prodMode="formDataLoad.prodMode"
                         :prodCode="formDataLoad.prodCode" :assemblyMenuType="'issuanceRegister'"/>
        <span style="font-size: 18px;">确认完成后请</span><span style="color:red;font-size: 18px;">线下通知报备人员下载发行登记表并开始报备！！</span>
        <k-form-footer data-align="center">
          <input style="width: 90px;
                border-radius: 5px;
                background-color: #47A44B;
                color: white !important;
                border: 0px;" type="button" :disabled="countFlag"
                 v-model="btnMsg == null ? '确定('+countNum+'s)' : btnMsg"  @click="updateStatus(formDataLoad)" >
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </div>
    </k-popup>


    <k-popup ref="editT8ProdIssuePopup" data-title="详情">
      <ProdIssueAdjust ref="prodIssueAdjustF" v-model="formData" :T8ProdIssueRegisFields="formData"
                       :prodMode="formDataLoad.prodMode"
                         :prodCode="formData.prodCode" :assemblyMenuType="'issuanceRegister'"/>
    </k-popup>

    <k-popup ref="editT8ProdIssuePopupV" data-title="详情">
      <ProdIssueAdjust ref="prodIssueAdjustV" v-model="formDataLoad" :T8ProdIssueRegisFields="formDataLoad"
                       :prodMode="formDataLoad.prodMode"
                       :prodCode="formDataLoad.prodCode" :assemblyMenuType="'issuanceRegister'"/>
    </k-popup>
    <k-popup ref="confirmPopup" title="报备确认" >
      <k-form ref="confirmForm" :data-col="2">
        <k-form-item label="报备日期" >
          <k-field-date :dataAllowblank="false" v-model="formData.issueRegistDate">

          </k-field-date>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="confirmForm" :data-handler="beforeSubmit"
                 :data-model="formData" data-target="prodElementGrid" data-action="T8ProdIssueRegister.updateT8ProdInfoStatusForIssuancce" >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>
</template>

<script>
  import {assign} from "lodash";
  import ProdIssueAdjust from "@/pages/pms/M81/prodDisplay/DisplayProdIssueAdjust.vue";
  export default {
    name: "issuanceRegister",
    components: {
      ProdIssueAdjust,
    },
    data() {
      return {
      editionData:{},
      RegisterData:{},
        prodCode: '',
        formData:{},
        formDataLoad:{},
        rowData2:{
        },
        prodSearchParam: {
          prodCode:''
        },
        // 倒计时周期
        countNum:20,
        // 用于倒计时标记，true-正在倒计时
        countFlag:false,
        // 定时器
        intervalBtn:{},
        //默认按钮的值
        btnMsg:"确定(",
        mobile:"",
        applyDate:[],
        establishDate:[],
        showGenerate:true,//是否显示生成按钮
        showConfirm:true,//是否显示确认报备按钮
        showConfirmIssueInfo:true,//是否显示确认产品发行登记信息
        lastVersion:'',
      }
    },
    created() {
      this.RegisterData = this.$route.query.RegisterData;
      this.prodCode = this.$route.query.RegisterData.prodCode;
      this.getNewVersion();
      this.httpUtil.comnQuery({
        action: 'T8ProdIssueRegister.getEdition',
        params: {t8ProdInfoId: this.RegisterData.t8ProdInfoId},
      }).then(data => {
        console.log(data)
        this.editionData = data.rows;
      });

      this.global.getProdUser('');
      this.$nextTick(()=>{
        // //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
        // this.global.getHideButtons(this);
        // let prodCode = this.$route.query.prod_code;
        // if(prodCode !=''&&prodCode!=undefined){
        //   this.$refs.prodElementGrid.load({prodCode:prodCode});
        // }
      });
    },
    activated() {
      //刷新页面取缓存
     var issuanceRegisterData =  localStorage.getItem("issuanceRegisterData");
     if(issuanceRegisterData){
       this.RegisterData = JSON.parse(issuanceRegisterData);
       //给lastVersion赋值
       this.getNewVersion();
       this.prodCode = this.RegisterData.prodCode;
       this.$refs.t8IssuanceRegisterGrid.load({prodCode:this.prodCode});
     }
    },
    methods:{
      getNewVersion(){
        this.httpUtil.comnQuery({
          action: 'T8ProdIssueRegister.getMaxEdition',
          params: {
            t8ProdInfoId: this.RegisterData.t8ProdInfoId,
          }
        }).then(data => {
          this.$nextTick(() => {
            let version = data.rows[0].version;
            this.lastVersion = version;
          })
        });
      },
      beforePopupLoad(params) {
        params.prodCode = this.prodCode;
        return params;
      },
      checkVersion(version){
        if(version==this.lastVersion){
          return true;
        }else{
          return false;
        }
      },
      countDown(){
        this.countFlag=false;
        // 设置btn倒计时时显示的信息
        this.btnMsg = null;
        // 更改btn状态
        this.countFlag =! this.countFlag;
        //每次打开弹窗都重置定时器
        clearInterval(this.intervalBtn);
        this.countNum=20;
        // 设置倒计时
        this.intervalBtn = setInterval(() => {
          if(this.countNum <= 0) {
            // 重置btn提示信息
            this.btnMsg = "确定";
            // 清除定时器
            clearInterval(this.intervalBtn)
            // 更改btn状态
            this.countFlag =! this.countFlag;
            // 重置倒计时状态
            this.countNum = 20;
          };
          // 倒计时
          this.countNum -- ;
        }, 1000);
      },
      checkSeconds(val){
        this.rowData2 = {};
        this.rowData2 = val;
        //触发定时器方法
        this.countDown()
      },
      updateStatus(formDataLoad){
        this.btnMsg="执行中...";
        this.countFlag = true;
        this.httpUtil.comnUpdate({
          action: 'T8ProdIssueRegister.updateStatus',
          params: this.rowData2,
          successAlert: true
        }).then(data => {
          this.countFlag = false;
          if(data.success==true){
            this.$refs.addT81004Popup.close();
            //this.$refs.prodElementGrid.load();
            this.$refs.t8IssuanceRegisterGrid.load({prodCode:formDataLoad.prodCode});
          }
        });
      },

      beforeSubmit(val){
        this.$set(val,'prodStatus','5');
        this.$set(val,'prodSonStatus','12');
        return val;
      },


      selectRowLoad(row, column, event){
        const _this = this;
        this.formDataLoad = Object.assign({}, row)
      },

      loadGrid(edition){
        this.$refs.t8IssuanceRegisterGrid.load({prodCode:this.formData.prodCode , edition:edition});
      },

      loadEdition(data){
        //生成要素成功后执行
        this.httpUtil.comnQuery({
          action: 'T8ProdIssueRegister.getEdition',
          params: {t8ProdInfoId: this.RegisterData.t8ProdInfoId},
        }).then(data => {
          console.log(data)
          this.editionData = data.rows;
        });
        this.lastVersion = data.returndata.version;
        //console.log("this.lastVersion=>>>>>>",this.lastVersion);
        this.$refs.editionFrom.load({t8ProdInfoId:data.t8ProdInfoId});
      },
      show() {
        let e = document.getElementById('show-form')
        if (this.extends) {
          e.style.display = "none"
        } else {
          e.style.display = ""
        }
        this.extends = !this.extends
      },
    },
    computed: {
      queryParam() {
        return {
          'prodName': this.prodSearchParam.prodName,//产品名称
          'isCompleteConfirm': this.prodSearchParam.isCompleteConfirm,//是否完成确认
          'prodCode': this.prodSearchParam.prodCode,//产品代码
          'applyStartDate':this.applyDate ? this.applyDate[0] : null,//募集期开始日
          'applyEndDate':this.applyDate ? this.applyDate[1] : null,//募集期结束日
          'establishDate': this.establishDate ? this.establishDate[0] : null,//成立期开始日
          'endDate': this.establishDate ? this.establishDate[1] : null,//成立日结束日
        }
      },
      iconStyle() {
        let iconStyle = {};
        iconStyle.background = this.$store.state.system.cardBackground
        return iconStyle;
      }
    }
  }
</script>

<style scoped>

</style>
