<template>
  <div>
    <k-form class="my-form " ref="prodInfo" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">

      <k-form-item label="产品模型">
        <k-field-select id="prod_mode" v-model="t8ProdInfo.prodMode"
          data-action="T8ProdModeInfo.findT8ProdModeInfos" data-display-field="prodModeName" data-value-field="prodMode"
          :data-disabled="true" :data-allowblank="false"/>
      </k-form-item>

      <k-form-item label="产品代码">
        <k-field-text id="prod_code" v-model="t8ProdInfo.prodCode"  :data-allowblank="false" :data-max-length="32"
          @data-on-blur="chkProdIsExit(t8ProdInfo.prodCode)" data-validate-type="codeLetterLine"
          :data-disabled="updateProduct" />
      </k-form-item>

      <k-form-item label="产品名称">
        <k-field-text v-model="t8ProdInfo.prodName" :data-allowblank="false" :data-max-length="128"  />
      </k-form-item>

      <k-form-item label="产品简称">
        <k-field-text v-model="t8ProdInfo.prodNameShort" :data-max-length="15" :data-allowblank="false"/>
      </k-form-item>

      <k-form-item label="产品说明">
        <k-field-text v-model="t8ProdInfo.prodExplain" :data-max-length="255" />
      </k-form-item>

      <k-form-item label="产品批次">
        <k-field-select id="taskGroup" v-model="t8ProdInfo.taskGroup" :data-max-length="32" data-action="T8ClearGroupInfo.findT8ClearGroupInfos" :dataParams="{'execTaskType':'2'}" data-display-field="taskGroupName"  data-value-field="taskGroup" :data-allowblank="false"
                        :data-disabled="this.subsBeginDate <= this.currentWorkday && updateProduct"/>
      </k-form-item>

      <k-form-item label="产品主办机构">
        <k-field-select  v-model="t8ProdInfo.prodSponOrg" :data-allowblank="true"
                        data-dict="prod_spon_org" />
      </k-form-item>
      <k-form-item label="产品主办部门">
        <k-field-cascader v-model="t8ProdInfo.prodSponDep"  data-diffcondition="deptno,parentdeptno" :data-graphql="querydeptGraphql"
                        data-display-child="children" data-check-strictly data-show-num :data-props="{ expandTrigger: 'hover'}"
                        data-size="medium" data-placeholder="请选择所属部门" data-clearable data-fileterable data-display-field="deptname"
                        data-value-field="deptno" :data-allowblank="true"/>
      </k-form-item>

      <k-form-item label="产品研发部门">
        <k-field-cascader v-model="t8ProdInfo.prodReseDep" data-diffcondition="deptno,parentdeptno"
                          :data-graphql="querydeptGraphql"
                          data-display-child="children" data-check-strictly data-show-num
                          :data-props="{ expandTrigger: 'hover'}"
                          data-size="medium" data-placeholder="请选择所属部门" data-clearable data-fileterable
                          data-display-field="deptname"
                          data-value-field="deptno" :data-allowblank="false"/>
      </k-form-item>

      <k-form-item label="产品销售部门">
        <k-field-cascader v-model="t8ProdInfo.prodSaleDep" data-diffcondition="deptno,parentdeptno"
                          :data-graphql="querydeptGraphql"
                          data-display-child="children" data-check-strictly data-show-num
                          :data-props="{ expandTrigger: 'hover'}"
                          data-size="medium" data-placeholder="请选择所属部门" data-clearable data-fileterable
                          data-display-field="deptname"
                          data-value-field="deptno" :data-allowblank="false">
        </k-field-cascader>
      </k-form-item>

      <k-form-item label="风险等级">
        <k-field-select v-model="t8ProdInfo.prodRiskLevel" data-dict="risklevel" :data-allowblank="false" />
      </k-form-item>

      <k-form-item label="产品登记编码">
        <k-field-text id="regist_code" v-model="t8ProdInfo.registCode" :data-allowblank="false" :data-max-length="14"
                      data-validate-type="codeLetter"  @data-on-change="checkRegistCodeValue(t8ProdInfo.registCode)"/>
      </k-form-item>

      <!-- 默认为人民币 -->
      <k-form-item label="产品币种">
        <k-field-select v-model="t8ProdInfo.prodCur" data-dict="currtype" data-default-value="156" :data-allowblank="false" />
      </k-form-item>

      <k-form-item label="钞汇标识">
        <k-field-select v-model="t8ProdInfo.bnoteRemitFlag" data-dict="bnote_remit_flag" data-default-value="0" :data-allowblank="false"/>
      </k-form-item>

      <k-form-item label="发行价格">
        <k-field-text v-model="t8ProdInfo.price"
                      data-validate-type="money" data-type="money"  data-min-value="0.00"
                      :data-max-length="13" data-digits="8" :data-allowblank="false" data-integer-length="4"
                      data-placeholder="单位(元)" data-default-value="1.00"/>
      </k-form-item>

      <k-form-item label="产品管理人">
        <k-field-select v-model="t8ProdInfo.managerCode" :data-allowblank="false" :data-max-length="128"
                        data-action="T82007.findTaManagerInfos"
                        data-display-field="managerCode,managerName" data-value-field="managerCode"/>
      </k-form-item>

       <k-form-item label="托管行">
        <k-field-select v-model="t8ProdInfo.truteeCode" :data-allowblank="false" :data-max-length="128"
                        data-action="T82006.findTaCustodianBanks"
                        data-display-field="truteeName" data-value-field="truteeCode" />
      </k-form-item>

      <k-form-item label="产品投资经理">
        <k-field-text v-model="t8ProdInfo.productInvManager" :data-max-length="16"  />
      </k-form-item>

      <k-form-item label="募集方式">
        <k-field-select v-model="t8ProdInfo.raiseType" data-dict="raise_type" :data-allowblank="false"/>
      </k-form-item>

      <k-form-item label="发行规模控制方式">
        <k-field-select v-model="t8ProdInfo.subsQuotaType" @data-on-change="controllerMaxSize" :data-allowblank="false"
                        data-dict="ta_subs_quota_type" data-displaykeyvalue="true" />
      </k-form-item>
      <k-form-item label="最高募集金额">
        <k-field-text v-model="t8ProdInfo.maxRaiseAmt"
                      :data-max-length="18" data-digits="2"
                      data-integer-length="15"
                      data-validate-type="money" data-show-gbmoney="true" data-type="money" data-placeholder="单位(元)"
                      :data-allowblank="blank" data-min-value="(0"
                      :data-disabled="blank"
                      @data-on-blur="validateRaiseAmt(t8ProdInfo, 0)"/>
      </k-form-item>
      <k-form-item label="最低募集金额">
        <k-field-text v-model="t8ProdInfo.minRaiseAmt" data-validate-type="money" data-type="money" data-min-value="0"
                      :data-max-length="18" data-digits="2" :data-allowblank="false" data-integer-length="15"
                      data-placeholder="单位(元)" data-default-value="0"
                      @data-on-blur="validateRaiseAmt(t8ProdInfo, 1)"/>
      </k-form-item>
      <k-form-item label="低于发行规模下限">
        <k-field-select v-model="t8ProdInfo.lowerLimit" :data-allowblank="false" data-dict="lower_limit"/>
      </k-form-item>
      <k-form-item label="产品工作日方案">
        <k-field-select v-model="t8ProdInfo.pgmno" :data-allowblank="false"
        data-params='{"pgmtype":"2"}' data-action="WorkdayProgram.find"
        data-display-field="pgmname" data-value-field="pgmno" :data-disabled="false"  />
      </k-form-item>

      <k-form-item label="认购确认日">
        <k-field-select   v-model="t8ProdInfo.subsCfmM" data-dict="subs_cfm_n" @data-on-change="checkSubsCfmM"
                          data-displaykeyvalue="true"  :data-allowblank="false"  :data-disabled="false" />
      </k-form-item>

      <k-form-item label="申购确认日" v-if="t8ProdInfo.prodMode != '1'">
        <k-field-select v-model="t8ProdInfo.applyCfmM" data-dict="apply_cfm_m" data-default-value="1"
                        :data-allowblank="t8ProdInfo.prodMode == '1'"  data-displaykeyvalue="true" :data-disabled="subsCfmM"
                        @data-on-change="checkApplyCfmM"/>
      </k-form-item>

      <k-form-item label="赎回确认日" v-if="t8ProdInfo.prodMode != '1'">
        <k-field-select v-model="t8ProdInfo.redeemCfmM" data-dict="redeem_cfm_m" data-default-value="1"
                        :data-allowblank="t8ProdInfo.prodMode == '1'"  data-displaykeyvalue="true" :data-disabled="subsCfmM"
                        @data-on-change="checkRedeemCfmM"/>
      </k-form-item>

      <k-form-item label="产品状态" v-if="false">
        <k-field-select v-model="t8ProdInfo.prodLifecycle" :data-disabled="true" data-dict="prod_lifecycle" data-default-value="1" />
      </k-form-item>

      <!--周期设置还没提供方法-->
      <k-form-item label="产品期限">
        <k-field-text v-model="t8ProdInfo.prodDays" :data-disabled="true" data-placeholder="自动产生"/>
      </k-form-item>

     <k-form-item label="实际募集金额" v-if="false">
        <k-field-text v-model="t8ProdInfo.actualSubsSize" :data-disabled="true" />
      </k-form-item>

      <k-form-item label="成立配售比例(%)" v-if="false">
        <k-field-text v-model="t8ProdInfo.confirmRate" :data-allowblank="true"
          data-rate-convert="true" :data-disabled="true" data-on-blur="sub0" data-max-value="100" data-min-value="0" />
      </k-form-item>

      <k-form-item label="产品募集失败日期" v-if="false">
        <k-field-date v-model="t8ProdInfo.prodFailureDate" :data-disabled="true" />
      </k-form-item>

      <k-form-item label="数据状态" v-if="false">
        <k-field-text v-model="t8ProdInfo.dataStatus" />
      </k-form-item>

      <k-form-item label="是否支持快速过户" v-if="t8ProdInfo.prodMode == '3' ">
        <k-field-select v-model="t8ProdInfo.isQuicktransfer" data-dict="is_quicktransfer" :data-allowBlank="true" />
      </k-form-item>
      <k-form-item label="认购申请导入方式"> <!--@data-on-change="checkCapitalType"-->
        <k-field-select v-model="t8ProdInfo.subsCapitalType" data-dict="subs_capital_type" :data-allowblank="false"
                        data-displaykeyvalue="true" data-default-value="1" @data-on-change="checkCapitalType"/>
      </k-form-item>

      <k-form-item label="认购计息属性" >
        <k-field-select v-model="t8ProdInfo.subsIsInterest" data-dict="subs_is_interest" :data-allowblank="false"
                        data-default-value="0" @data-on-change="changeSubsIsInterest" data-displaykeyvalue="true"/>
      </k-form-item>

      <k-form-item label="认购计息年天数">
        <k-field-select v-model="t8ProdInfo.subsInterestYeardays" data-dict="yeardays"
                        :data-allowblank="interest || t8ProdInfo.subsIsInterest == '0'"
                        :data-disabled="capitalType || t8ProdInfo.subsIsInterest == '0'" placeholder="天"
                        data-displaykeyvalue="true" @input="forceUpdate"/>
      </k-form-item>

      <k-form-item label="认购计息起始日">
        <k-field-select v-model="t8ProdInfo.subsInterestStartDate" data-dict="subs_interest_start_date"
                        :data-allowblank="interest || t8ProdInfo.subsIsInterest == '0'"
                        :data-disabled="capitalType  || t8ProdInfo.subsIsInterest == '0'" data-displaykeyvalue="true"
                        @input="forceUpdate"/>
      </k-form-item>

      <k-form-item label="认购计息截止日">
        <k-field-date v-model="t8ProdInfo.subsInterestEndDate" data-dict="subs_interest_end_date"
                      :data-allowblank="interest || t8ProdInfo.subsIsInterest == '0'"
                      :data-disabled="capitalType  || t8ProdInfo.subsIsInterest == '0'"
                      @input="forceUpdate" :data-max-value="establishDate"/>
      </k-form-item>

      <k-form-item label="认购利息处理方式">
        <k-field-select v-model="t8ProdInfo.subsInterestDealMode" data-dict="subs_interest_deal_mode"
                        :data-allowblank="interest || t8ProdInfo.subsIsInterest == '0'"
                        :data-disabled="capitalType  || t8ProdInfo.subsIsInterest == '0'" data-displaykeyvalue="true"
                        @input="forceUpdate"/>
      </k-form-item>
      <k-form-item label="产品扣款模式">
        <k-field-radio v-model="t8ProdInfo.handlerMode" data-dict="handler_mode" data-displaykeyvalue="true"/>
      </k-form-item>

      <k-form-item label="份额明细处理" v-show="this.t8ProdInfo.prodMode!=3">
        <k-field-select v-model="t8ProdInfo.tnRedeemShare" data-dict="redemptionShare" :data-allowblank="false"
                      data-displaykeyvalue="true" data-default-value="1"/>
      </k-form-item>
    </k-form>
  </div>
</template>

<script>
  import Tools from '@/utils/tools.js';
  import moment from 'moment';

  export default {
    computed: {
      queryOrgGraphql() {
        return "{queryOrg(action:\"findChildren\",orgno:\"" + this.userOrgno +
          "\") {rows{orgid, orgname, parentorgno, orgno},results}}"
      },
      querydeptGraphql(){
        return "{queryDept(action:\"find\") {rows{deptno, deptname, parentdeptno, deptid},results}}"
      }
    },
    model: {
      prop: 't8ProdInfo',
      event: 'input'
    },
    props: {
      t8ProdInfo: {},
      subsEndDate : '',
      establishDate: '',
      endDate: '',
      subsBeginDate:''
    },
    data() {
      return {
        // 展示认购计息模块
        capitalType : false,
        formData: {},
        interest : true,
        updateProduct : false,
        blank: false,
        subsCfmM : false,
        currentWorkday:null,
      }
    },

    methods: {
      controlRate: function() {
        this.t8ProdInfo.maxPledgeRate = null;
      },
      forceUpdate(){
        this.$forceUpdate();
      },

      validateData() {
        return this.$refs.prodInfo.validate();
      },

      controllerMaxSize(value){
        if (value == '0'){
          this.t8ProdInfo.maxRaiseAmt = "";
          this.t8ProdInfo.minRaiseAmt = "";
          this.blank = true;
        }else {
          this.blank = false;
        }
        if (value == '3' && this.t8ProdInfo.subsCapitalType == '1'){
          this.t8ProdInfo.subsQuotaType = "";
          Tools.alert("末日导入不支持末日比例配售", "warning");
        }
      },

      /**
       * 校验最高最低募集金额的大小
       * @param params
       * @param key
       */
      validateRaiseAmt(params, key){
        if (Tools.isEmpty(params.minRaiseAmt)  && Tools.isEmpty(params.maxRaiseAmt)) {
          switch (key) {
            case 0:
              if (Number(params.maxRaiseAmt) < Number(params.minRaiseAmt) ) {
                this.t8ProdInfo.maxRaiseAmt = '';
                Tools.alert("不能小于最低募集金额", "warning");
              }
              break;
            case 1:
              if (Number(params.minRaiseAmt) > Number(params.maxRaiseAmt) ) {
                this.t8ProdInfo.minRaiseAmt = '';
                Tools.alert("不能大于最高募集金额", "warning");
              }
              break;
              default:
                console.log("params.minRaiseAmt" + params.minRaiseAmt + "params.maxRaiseAmt" + "key" + key)
          }
        }
      },

      checkSubsCfmM(value){
        if (value == '0' && this.t8ProdInfo.prodMode != '1'){
          this.t8ProdInfo.applyCfmM = '0';
          this.t8ProdInfo.redeemCfmM = '0';
          this.subsCfmM = true ;
        }
        if(value != '0' && this.t8ProdInfo.prodMode != '1'){
          if(this.t8ProdInfo.applyCfmM == '0'){ //申购确认日是T+0时改变
            this.t8ProdInfo.applyCfmM = '1';
          }
          if (this.t8ProdInfo.redeemCfmM == '0'){//赎回确认日是T+0时改变
            this.t8ProdInfo.redeemCfmM = '1';
          }
          this.subsCfmM = false ;
        }
      },

      checkApplyCfmM(value){
        if (value == '0' && this.t8ProdInfo.subsCfmM != '0'){
          Tools.alert("认购确认日不是T+0，申购确认日不能选择T+0", "warning");
          this.t8ProdInfo.applyCfmM = '';
        }
      },

      checkRedeemCfmM(value){
        if (value == '0' && this.t8ProdInfo.subsCfmM != '0'){
          Tools.alert("认购确认日不是T+0，赎回确认日不能选择T+0", "warning");
          this.t8ProdInfo.redeemCfmM = '';
        }
      },

      checkCapitalType (params){
        if (params == '1' && this.t8ProdInfo.subsQuotaType == '3' ) { // 0-逐日，1-末日
          this.t8ProdInfo.subsCapitalType = "";
          Tools.alert("末日导入不支持末日比例配售", "warning");
        }
      },

      // 末日导入时，计息属性为disabled
      // checkCapitalType (params){
      //   if (params == '1'){ // 0-逐日，1-末日
      //     // 当认购申请导入方式为末日导入，需要将认购计息属性，利息计息天数等清空
      //     this.t8ProdInfo.subsInterestYeardays = "";
      //     this.t8ProdInfo.subsInterestStartDate = "";
      //     this.t8ProdInfo.subsInterestEndDate = "";
      //     this.t8ProdInfo.subsInterestDealMode = "";
      //     this.t8ProdInfo.subsIsInterest = "0";
      //     this.capitalType = true;
      //   }else{
      //     this.t8ProdInfo.subsIsInterest = "";
      //     this.capitalType = false;
      //   }
      //   //console.log(params);
      // },

      // 不计息时，需要清空已经填入的计息信息
      changeSubsIsInterest (value){// 0-不计息，1-计息
        // console.log("value" + value);
        if(value=='0'){
          this.t8ProdInfo.subsInterestYeardays = "";
          this.t8ProdInfo.subsInterestStartDate = "";
          this.t8ProdInfo.subsInterestEndDate = "";
          this.t8ProdInfo.subsInterestDealMode = "";
          this.capitalType = true;
          this.interest = true;
        }else {
          this.capitalType = false;
          this.interest = false;
        }
      },

      // 校验是否存在相同的产品代码信息
      chkProdIsExit(value){
        //console.log("chkProdIsExit" + value);
        this.httpUtil.comnQuery({
          action: 'T8ProdInfo.findExistsTaProdCodeOrRegistCode',
          params: {
            prodCode : value
          }
        }).then(data => {

          if(data.rows.length > 0 ){
            //let prodInfo = data.rows[0];
            this.t8ProdInfo.prodCode = null;
            Tools.alert("已存在产品代码", "warning");
          }
        });
      },

      setProdDays(){

        if (this.establishDate != null && this.establishDate != ''
              && this.endDate != null && this.endDate !=  ''){

          let startDate = moment(this.establishDate, 'YYYYMMDD');
          let lastDate = moment(this.endDate, 'YYYYMMDD');

          let diffDays = lastDate.diff(startDate, 'days')

          this.t8ProdInfo.prodDays = diffDays;

        } else {

          this.t8ProdInfo.prodDays = '';
        }
      },

      checkRegistCodeValue(value){

        this.httpUtil.comnQuery({
          action:"T8ProdInfo.findRegistCodeOnly",
          params:{registCode:value}
        }).then(data => {
          if(data.rows.length > 0 ){
            this.t8ProdInfo.registCode = "";
            Tools.alert("产品登记编码已存在！","danger");
          }
        })
      }
    },
    created() {
      this.updateProduct = this.t8ProdInfo.updateProduct;

      // if (this.t8ProdInfo.subsCapitalType != null){
      //   this.checkCapitalType(this.t8ProdInfo.subsCapitalType)
      // }

      if (this.t8ProdInfo.subsIsInterest != null){
        this.changeSubsIsInterest(this.t8ProdInfo.subsIsInterest)
      }

      if (this.t8ProdInfo.subsQuotaType != null){
        // console.log("t8ProdInfo.subsQuotaType=====" + this.t8ProdInfo.subsQuotaType)
        this.controllerMaxSize(this.t8ProdInfo.subsQuotaType)
      }

      this.httpUtil.sysDate().then(res=>{
        if (res) {
          this.currentWorkday = res;
        }
      });

    },
    mounted() {},
    watch: {
      subsEndDate: {
        handler: function(val) {
          this.$emit('@changesubsEndDate', val);
        },
        deep: true
      },

      endDate: function(){

        this.setProdDays();
      },

      establishDate: function(){

        this.setProdDays();
      },





    },

  }
</script>

<style>

</style>
