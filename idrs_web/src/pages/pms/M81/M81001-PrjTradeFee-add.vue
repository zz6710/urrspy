<template>
  <div>

    <k-form ref="addForm" :data-col="2">
      <k-form-item label="产品代码" v-show="false">
        <k-field-text v-model="formData.prodCode"  :data-allowblank="false" data-disabled/>
      </k-form-item>
      <k-form-item label="费用类型">
        <k-field-select v-model="formData.feeType"  data-dict="fee_type" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="方案代码" v-show="false">
        <k-field-text v-model="formData.tailingCommisionCode" />
      </k-form-item>
      <k-form-item label="启用日期">
        <k-field-date v-model="formData.enableDate" :data-allowblank="false" :dataMinValue="this.currentWorkday" />
      </k-form-item>
      <k-form-item label="收费方式" style="height: 60px">
        <k-field-select v-model="formData.chargeType"  data-dict="charge_type" data-disabled="false"  @data-on-change="chargeTypeChange" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="认申购收费模式" v-show="formData.feeType == '0' || formData.feeType == '1' ">
        <k-field-select v-model="formData.buyfeeMode" data-dict="buyfee_mode"
        :data-allowblank="(formData.feeType == '0' || formData.feeType == '1') ? false: true "/>
      </k-form-item>
      <k-form-item label="认申购费用计算方式" v-show="formData.feeType == '0' || formData.feeType == '1' ">
        <k-field-select v-model="formData.buyfeeMethod" data-dict="buyfee_method"
        :data-allowblank="(formData.feeType == '0' || formData.feeType == '1') ? false: true "/>
      </k-form-item>
      <k-form-item label="费率计算方式" v-show="formData.feeType == '0' || formData.feeType == '1' " >
        <k-field-select v-model="formData.rateCalculateMethod" data-dict="rate_calculate_method"
        :data-allowblank="(formData.feeType == '0' || formData.feeType == '1') ? false: true "/>
      </k-form-item>
      <k-form-item label="费率合并方式" v-show="formData.feeType == '0' || formData.feeType == '1' " style="height: 60px">
        <k-field-select v-model="formData.rateMergeMethod" data-dict="rate_merge_method"
        :data-allowblank="(formData.feeType == '0' || formData.feeType == '1') ? false: true "/>
      </k-form-item>
      <k-form-item label="后收费用计算方式" v-show="false" >
        <k-field-select v-model="formData.backfeeCalculateMethod" data-dict="chargingAfterRedemption" />
      </k-form-item>
      <k-form-item label="维度组合" v-show="false" >
        <k-field-select v-model="formData.feeRole" data-dict="income_role" />
      </k-form-item>
      <k-form-item label="归产品资产计算方式" v-show="formData.feeType == '2' || formData.feeType == '3' " >
        <k-field-select v-model="formData.redemfeeAssetMethod" data-dict="redemfee_asset_method" />
      </k-form-item>
      <k-form-item label="归产品资产比例(%)" v-show="formData.feeType == '2' || formData.feeType == '3' " >
        <k-field-text v-model="formData.redemfeeAssetRat" data-validate-type="number" data-max-value="100" data-min-value='0' data-digits="5"/>
      </k-form-item>
      <k-form-item label="备注" v-show="false">
        <k-field-text v-model="formData.remark" :data-max-length="256"/>
      </k-form-item>
    </k-form>
    <div class="my-container">
      <div class="my-item1">
        <div>
          <md-switch v-model="constantRatesSitch" class="md-info" @change="changeConstantType">{{constantDesc}}</md-switch>
        </div>
        <div>
          <md-field style="padding-top: 17px !important;">
            <md-input class="md-input" style="width: 208px;" v-model="formData.constantRate" type="number" v-show="constantRatesSitch" @blur="handleBlur"></md-input>
          </md-field>
        </div>
      </div>
      <div class="my-item2">
        <div style="width: 135px !important">
          <md-switch v-model="switchSegmentValue" :disabled="moneySwitchDisabled" class="md-info" @change="changeSegmentType">金额分段</md-switch>
        </div>
        <div class="my-item2-chips">
          <md-chips v-model="moneyList" class="md-primary" style="padding-top: 4px !important;" md-placeholder="请输入分段金额,并按回车确认"  md-input-type="number"  @md-delete="deleteMoney" @md-insert="insertMoney"  v-show="switchSegmentValue" ></md-chips>
        </div>
      </div>
      <div class="my-item3">
        <div style="width: 135px !important">
          <md-switch v-model="switchTimeValue" :disabled="timeSwitchDisabled" class="md-info" @change="changeTimeType">时间分段</md-switch>
        </div>
        <div class="my-item2-chips">
          <md-chips v-model="timeList" class="md-primary" style="padding-top: 4px !important;" md-placeholder="请输入分段天数,并按回车确认" md-input-type="number"  @md-delete="deleteTime" @md-insert="insertTime" v-show="switchTimeValue" ></md-chips>
        </div>
      </div>
    </div>
    <div label="金额分段展示"  class="my-table" v-show="switchSegmentValue || switchTimeValue">
      <md-table>
        <md-table-row>
          <md-table-head v-show="moneyList.length > 0 " >金额段</md-table-head>
          <md-table-head v-show="timeList.length > 0" >持有时间</md-table-head>
          <md-table-head>{{feeTableHead}}</md-table-head>
          <md-table-head>最低费用</md-table-head>
          <md-table-head>最高费用</md-table-head>
        </md-table-row>

        <!--没有数据时才展示这一行 -->
        <md-table-row v-show="timeList.length == 0 && moneyList.length == 0">
          <md-table-cell :colspan="3" style="text-align:center">
              <p> 暂无数据</p>
          </md-table-cell>
        </md-table-row>

        <md-table-row   v-show="timeList.length > 0 || moneyList.length > 0" v-for="(item,index) in tailingCommisionList" :key="index">
          <md-table-cell v-show="item.showMoneyTd" :rowspan="item.moneyRowspan" >{{ item.moneyDesc }}</md-table-cell>
          <md-table-cell v-show="item.showTimeTd">{{ item.timeDesc }}</md-table-cell>
          <md-table-cell>
            <md-field>
              <span class="md-suffix" v-show="formData.chargeType=='0'||formData.chargeType==''">￥</span>
                <md-input class="md-input" style="width: 80px; text-align: right;" v-model="item.rate" md-input-type="number"  v-show="formData.chargeType=='1' "></md-input>
                <md-input class="md-input" style="width: 80px; text-align: right;" v-model="item.constantFee" md-input-type="number" v-show="formData.chargeType=='0'||formData.chargeType==''"></md-input>
              <span class="md-suffix" v-show="formData.chargeType=='1'">%</span>
            </md-field>
          </md-table-cell>
          <md-table-cell>
            <md-field>
              <span class="md-suffix" v-show="formData.chargeType=='1'">￥</span>
              <md-input class="md-input" style="width: 80px; text-align: right;" v-model="item.minFee" md-input-type="number"
              v-show="formData.chargeType=='1'"></md-input>
              <span class="md-suffix" style="width: 110px; text-align: right;" v-show="formData.chargeType=='0'||formData.chargeType==''">不适用</span>
            </md-field>
          </md-table-cell>
          <md-table-cell>
            <md-field >
              <span class="md-suffix" v-show="formData.chargeType=='1'">￥</span>
              <md-input class="md-input" style="width: 80px; text-align: right;" v-model="item.maxFee" md-input-type="number"
              v-show="formData.chargeType=='1'"></md-input>
              <span class="md-suffix" style="width: 100px; text-align: right;" v-show="formData.chargeType=='0'||formData.chargeType==''">不适用</span>
            </md-field>
          </md-table-cell>
        </md-table-row>
      </md-table>
    </div>
    <div>
      <k-form>
      <k-form-footer data-align="center">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T83004.addTa3004" :data-handler="beforeSubmit"
                 data-from="addForm" :data-model="formData"
                 data-target="T81001Grid">
         <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
      </k-btn>
      <k-btn class="btn-custom-plain" data-functype="CLOSE">
       <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>

    </k-form-footer>
      </k-form>
    </div>

  </div>
</template>

<script>
  import Tools from '@/utils/tools.js';
  export default {
    props: {
      info :{
        type:Object
      }
    },
    data() {
      return {
        userid: localStorage.getItem("userid"),
        formData :{
          t8PrjFeeLists:'',
          rateType: '0',
          constantRate : null
        },
        switchSegmentValue : false,   //金额分段切换按钮的值
        switchTimeValue : false,      //金额分段切换按钮的值
        constantRatesSitch: true,     //固定费率切换按钮的值
        moneyList: [],
        timeList:[],
        tailingCommisionList:[],
        tailingCommisionMoneyList:[],
        tailingCommisionTimeList:[],
        moneySwitchDisabled:false,    //金额分段切换按钮是否可用
        timeSwitchDisabled:false,     //时间分段切换按钮是否可用
        constantDesc : "固定费率(%)",
        feeTableHead: "费率",
        maxMoneyValue:99999999999999.99,
        maxRateValue:100,
        currentWorkday: null,
      };
    },
    created() {
      this.formData = this.info;
      this.httpUtil.sysDate().then(res=>{
        if (res) {
          this.currentWorkday = res;
        }
      });
    },
    methods: {
      beforeSubmit : function(value){

        if(this.formData.prodCode==null || this.formData.prodCode==''){
            Tools.alert("未录入产品代码","danger");
            return false;
        }

        let ret = this.$refs.addForm.validate();
        if(!ret){
          return false;
        }

        //默认主方案生效状态
        this.formData.dataStatus = 'E';
        //标记校验是否通过，是否允许提交
        let commitFlag = true;
        let errorMsg = "";

        if(this.constantRatesSitch){
          //固定费率，首先置空这个子方案列表
          this.tailingCommisionList = [];
          //创建固定费率子方案
          this.formData.feeRole = '0';
          if(this.formData.constantRate == null || this.formData.constantRate == ''){
            errorMsg =  this.constantDesc + "未录入";
            commitFlag =  false;
          }

          if(this.formData.chargeType=="0"){
            //固定费用
            if(this.formData.constantRate<0 || this.formData.constantRate> this.maxMoneyValue){
              errorMsg =  "固定费用值必须在0-"+this.maxMoneyValue+"之间";
              commitFlag =  false;
            }

            this.tailingCommisionList.push(
              {
                feeCode: null,
                rate: 0,
                constantFee:this.formData.constantRate,
                minFee: 0,
                maxFee: this.maxMoneyValue,
                dataStatus: 'E',
              }
            )
          }else{
            //固定费率
            if(this.formData.constantRate<0 || this.formData.constantRate> this.maxRateValue){
              errorMsg =  "固定费率值必须在0-100之间";
              commitFlag =  false;
            }

            this.tailingCommisionList.push(
              {
                feeCode: null,
                rate: this.formData.constantRate,
                constantFee:0,
                minFee: 0,
                maxFee: this.maxMoneyValue,
                dataStatus: 'E',
              }
            )
          }

        }else if(this.switchTimeValue || this.switchSegmentValue ){
          //维度组合：（0-不分段；1-金额段；2-持有时间；3-客户类型）；多个维度组合直接将多个值串连起来，最多三个维度组合
          //分段
          this.formData.feeRole = '0';
          if(this.switchSegmentValue){
            this.formData.feeRole = '1';
          }else if((!this.switchSegmentValue) && this.switchTimeValue){
            this.formData.feeRole = '2';
          }else{
            this.formData.feeRole = '12';
          }
          this.tailingCommisionList.forEach(e => {
            //数据校验，如果输入的是费用，则费率清空，否则费用清空
            if(this.formData.chargeType=="0"){
              if(e.constantFee == null || e.constantFee == ""){
                errorMsg =  "存在"+ this.feeTableHead +"未录入";
                commitFlag =  false;
              }
              if(Number(e.constantFee) < 0 || Number(e.constantFee) > (this.maxMoneyValue) ){
                errorMsg =  "费用值必须在0-"+this.maxMoneyValue+"之间";
                commitFlag =  false;
              }

              e.rate = null;
            }else{
              if(e.rate == null || e.rate == ""){
                errorMsg =  "存在"+ this.feeTableHead +"未录入";
                commitFlag =  false;
              }

              if(Number(e.rate)<0 || Number(e.rate) > Number(this.maxRateValue) ){
                errorMsg =  "费率值必须在0-"+this.maxRateValue+"之间";
                commitFlag =  false;
              }

              if(Number(e.minFee) < 0 || Number(e.minFee) > Number(e.maxFee) ) {
                errorMsg = "最低费用必须,大于0 小于最高费用"
                commitFlag = false;
              }

              if(Number(e.maxFee) < 0 ) {
                errorMsg = "最高费用必须大于0 "
                commitFlag = false;
              }

              e.constantFee = null;
            }

            if(e.minFee == null || e.minFee == ''){
              e.minFee = 0.00
            }
            if(e.maxFee == null || e.maxFee == ''){
              e.maxFee = this.maxMoneyValue;
            }
          })
        }else{
          errorMsg = "费率设置有误";
          commitFlag =  false;
        }

        if(commitFlag){
          this.formData.t8PrjFeeLists = JSON.stringify(this.tailingCommisionList);
          value.t8PrjFeeLists = this.formData.t8PrjFeeLists;
          value.dataStatus = this.formData.dataStatus;
          value.feeRole = this.formData.feeRole;
        }else{
          Tools.alert( errorMsg, "danger");
          return false;
        }

      },
      chargeTypeChange(value){
        //修改费用、费率描述
        if(value==='0'){
          //费用
          this.constantDesc = "固定费用(%)";
          this.feeTableHead = "费用";
        }else{
          //费率
          this.constantDesc = "固定费率(%)";
          this.feeTableHead = "费率";
        }

        //触发固定费用费率值校验
        this.handleBlur();
      },
      changeConstantType: function(value) {
        //选择固定费用/费率时，分段设置变为空
        this.switchSegmentValue = !value
        this.switchTimeValue = !value
        this.moneyList = []
        this.timeList = []
        this.formData.constantRate = null
        this.formData.rateType = this.formData.rateType == '0' ? '1' : '0'
        this.tailingCommisionList = []

        //当金额或时间分段不可用的时候，表示该费用类型下不能选这种方式，应该还原成不可编辑
        if(this.moneySwitchDisabled){
          this.switchSegmentValue = false;
        }
        if(this.timeSwitchDisabled){
          this.switchTimeValue = false;
        }
      },
      //true -> false
      //value == false
      changeSegmentType: function(value) {
        //金额分段
        if(this.constantRatesSitch && value){
          this.constantRatesSitch = false;
        }else if(this.switchTimeValue==false && value==false){
          //分段都为空，则固定费率不能为空
          this.constantRatesSitch = true;
        }

        this.moneyList = []
        this.formData.constantRate = null
        this.formData.rateType = this.formData.rateType == '0' ? '1' : '0'

        //重新生成列表
        this.buildTimeTable();
      },
      changeTimeType : function(value){
        //时间分段
        if(this.constantRatesSitch && value){
          this.constantRatesSitch = false;
        }else if(this.switchSegmentValue==false && value==false){
          //分段都为空，则固定费率不能为空
          this.constantRatesSitch = true;
        }

        this.timeList = []
        this.formData.constantRate = null
        this.formData.rateType = this.formData.rateType == '0' ? '1' : '0'

        //重新生成列表
        this.buildMoneyTable();
      },
      deleteMoney : function(text, index){
        //是否已经删完
        if(this.moneyList.length == 0){
          Tools.alert( "已经没有数据", "danger");
          //没有数据，要按时间维度重新刷新列表
          this.buildTimeTable();
          return false;
        }
        //inde-下标，从0开始
        if(index != this.moneyList.length){
          Tools.alert( "请顺序删除", "danger");
          //在指定位置添加元素,第一个参数指定位置,第二个参数指定要删除的元素,如果为0,则追加
          this.moneyList.splice(index, 0, text);
          return false;
        }
        this.buildMoneyTable();
      },
      deleteTime : function(text, index){
        //是否已经删完
        if(this.timeList.length == 0){
          Tools.alert( "已经没有数据", "danger");
          //没有数据，要按金额维度重新刷新列表
          this.buildMoneyTable();
          return false;
        }
        //inde-下标，从0开始
        if(index != this.timeList.length){
          Tools.alert( "请顺序删除", "danger");
          //在指定位置添加元素,第一个参数指定位置,第二个参数指定要删除的元素,如果为0,则追加
          this.timeList.splice(index, 0, text);
          return false;
        }
        this.buildTimeTable();
      },
      buildMoneyTable : function(){
        this.tailingCommisionList = [];
        //先按金额组合，再按持有时间
        this.tailingCommisionMoneyList = [];

        for(var i = 0 ;i < this.moneyList.length ; i++){

          //1-金额，2-时间
          this.tailingCommisionMoneyList.push(
            {
              dimension1Min: i == 0 ? 0 : this.moneyList[i-1],
              dimension1Max: this.moneyList[i],
              dimension2Min: null,
              dimension2Max: null,
              moneyDesc: (i == 0 ? 0 : this.moneyList[i-1] )+ ' <= 金额 < ' + this.moneyList[i],
              timeDesc: null,
              rate: null,
              constantFee:null,
              minFee: null,
              maxFee: null,
              dataStatus: 'E',
              moneyRowspan: 1,
              showMoneyTd: true,
              showTimeTd: false
            }
          )
        }
        var lastMoney = {
          dimension1Min: this.moneyList[this.moneyList.length-1],
          dimension1Max: '-1',
          dimension2Min: null,
          dimension2Max: null,
          moneyDesc: this.moneyList[this.moneyList.length-1] + ' <= 金额 < ∞',
          timeDesc: null,
          rate: null,
          constantFee:null,
          minFee: null,
          maxFee: null,
          dataStatus: 'E',
          moneyRowspan: 1,
          showMoneyTd: true,
          showTimeTd: false
        };
        this.tailingCommisionMoneyList.push(lastMoney);
        if(this.timeList == null || this.timeList.length == 0){
          //为空只有一个维度
          this.tailingCommisionList = this.tailingCommisionMoneyList;
        }else{
          //不为空，两个维度
          for(let i = 0 ;i < this.tailingCommisionMoneyList.length ; i++  ){
            for(var j = 0 ;j <= this.timeList.length ; j++ ){
              let temp = Object.assign({}, this.tailingCommisionMoneyList[i]);
              temp.showTimeTd = true;
              temp.showMoneyTd = j==0 ? true : false;
              temp.moneyRowspan = j==0 ? this.timeList.length+1 : 1;
              temp.timeDesc = (j == 0 ? 0 : this.timeList[j-1] )+ ' <= 天数 < ' + (j== this.timeList.length ? ' ∞ ' : this.timeList[j] );
              temp.dimension2Min = j == 0 ? 0 : this.timeList[j-1];
              temp.dimension2Max = j== this.timeList.length ? '-1' : this.timeList[j] ;
              this.tailingCommisionList.push(temp);
            }
          }
        }

        // console.log(this.tailingCommisionList);
      },
      buildTimeTable : function(){
        this.tailingCommisionList = [];
        //先按金额组合，再按持有时间
        this.tailingCommisionTimeList = [];
        for(var i = 0 ;i < this.timeList.length ; i++){

          //1-金额，2-时间
          this.tailingCommisionTimeList.push(
            {
              dimension1Min: null,
              dimension1Max: null,
              dimension2Min: i == 0 ? 0 : this.timeList[i-1],
              dimension2Max: this.timeList[i],
              moneyDesc: null,
              timeDesc: (i == 0 ? 0 : this.timeList[i-1] )+ ' <= 天数 < ' + this.timeList[i],
              rate: null,
              constantFee:null,
              minFee: null,
              maxFee: null,
              dataStatus: 'E',
              moneyRowspan: 1,
              showMoneyTd: false,
              showTimeTd: true
            }
          )
        }
        var lastTime = {
          dimension1Min: null,
          dimension1Max: null,
          dimension2Min: this.timeList[this.timeList.length-1],
          dimension2Max: '-1',
          moneyDesc: null,
          timeDesc: this.timeList[this.timeList.length-1] + ' <= 天数 < ∞',
          rate: null,
          constantFee:null,
          minFee: null,
          maxFee: null,
          dataStatus: 'E',
          moneyRowspan: 1,
          showMoneyTd: false,
          showTimeTd: true
        };
        this.tailingCommisionTimeList.push(lastTime);
        if(this.moneyList == null || this.moneyList.length == 0){
          //为空只有一个维度
          this.tailingCommisionList = this.tailingCommisionTimeList;
        }else{
          //不为空，两个维度
          for(let i = 0 ;i <= this.moneyList.length ; i++ ){
            for(let j = 0 ;j < this.tailingCommisionTimeList.length ; j++  ){
              let temp = Object.assign({}, this.tailingCommisionTimeList[j]);
              temp.showMoneyTd = j!=0 ? false : true;
              temp.moneyRowspan = j==0 ? this.tailingCommisionTimeList.length : 1;
              temp.moneyDesc = (i == 0 ? 0 : this.moneyList[i-1] )+ ' <= 金额 < ' + (this.moneyList.length == i ? ' ∞ ' : this.moneyList[i]);
              temp.dimension1Min = i == 0 ? 0 : this.moneyList[i-1];
              temp.dimension1Max = i == this.moneyList.length ? '-1' : this.moneyList[i];
              this.tailingCommisionList.push(temp);
            }
          }
        }
        // console.log(this.tailingCommisionList);
      },
      insertMoney : function(value){
        //插入后，已经插入了
        if(value <= 0 || parseInt(this.moneyList[this.moneyList.length-2]) > parseInt(value)){
          this.moneyList.pop();
          Tools.alert( "金额不能小于等于0，不能小于上次输入金额", "danger");
          return false;
        }

        this.buildMoneyTable();
        return value;
      },
      insertTime : function(value){
        //插入后，已经插入了
        if(value <= 0 || parseInt(this.timeList[this.timeList.length-2]) > parseInt(value)){
          this.timeList.pop();
          Tools.alert( "天数不能小于等于0，不能小于上次输入天数", "danger");
          return false;
        }
        this.buildTimeTable();
        return value;
      },
      handleBlur : function(){
        if(this.formData.constantRate !=null && this.formData.constantRate!=""){
          if(Number(this.formData.constantRate)>this.maxRateValue && (this.formData.chargeType=="1")){
            //固定费率限制小于100
            this.formData.constantRate = this.maxRateValue;
          }else if(Number(this.formData.constantRate)>this.maxMoneyValue && this.formData.chargeType=="0"){
            //固定费用限制小于 99999999999999.99
            this.formData.constantRate = this.maxMoneyValue;
          }
        }
      }
    },
    watch:{
      'formData.feeType'(value){
        //根据不同费用类型判断对应的切换按钮是否可用，在这里做监听是因为平台的data-on-change事件点击X时不会触发
        if(value == '2' || value == '5' || value == '6' ){
          this.moneySwitchDisabled = true;
          this.switchSegmentValue = false;
          this.changeSegmentType(this.switchSegmentValue);
        }else{
          this.moneySwitchDisabled = false;
        }

        if(value == '0' || value == '1' || value == '4' ){
          this.timeSwitchDisabled = true;
          this.switchTimeValue = false;
          this.changeTimeType(this.switchTimeValue);
        }else{
          this.timeSwitchDisabled = false;
        }
      },

    }
  };
</script>
<style lang="scss" scoped>
  .my-container{
    display: flex;
    flex-flow:column;
    margin-top: -30px;
  }
  .my-item1{
    display: flex;
    flex-flow: row;
    margin: 10px 0 15px 10px;
  }
  .my-item2{
    display: flex;
    flex-flow: row;
    margin: -45px 0 15px 10px;
  }
  .my-item3{
    display: flex;
    flex-flow: row;
    margin: -45px 0 15px 10px;
  }
  .my-display{
    font-size: 17px;
  }
  .md-switch-label {
    font-size: inherit !important;
  }
  .md-switch{
    margin-top: 38px !important;
  }
  .my-item2-chips{
    margin: 15px 0 0px 0px;
    width: 356px !important;
  }
  .md-chips.md-field .md-chip{
      margin-top: 7px;
  }
  .my-table{
    margin-top: -10px;
  }

  ::v-deep .md-table-head-container{
    text-align: center;
  }

  ::v-deep input::-webkit-outer-spin-button,
  ::v-deep input::-webkit-inner-spin-button {
    -webkit-appearance: none !important;
  }
  ::v-deep input[type="number"]{
    -moz-appearance: textfield;
  }
</style>
