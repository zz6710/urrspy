<template>
  <div>
    <k-form ref="editForm" :data-col="2">
      <k-form-item label="产品代码" v-show="false">
        <k-field-text v-model="ProdFeeDealInfo.prodCode"  />
      </k-form-item>
      <k-form-item label="产品主表id" v-show="false">
        <k-field-text v-model="ProdFeeDealInfo.t8ProdInfoId"  />
      </k-form-item>
      <k-form-item label="交易费用类型" >
        <k-field-select v-model="ProdFeeDealInfo.feeTypeDeal" data-dict="t8_fee_type_deal" @data-on-change="changeSelectValue"
                        :data-allowblank="false" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="基本费率%" v-if="!this.switchSegmentValue && !this.switchTimeValue">
        <k-field-text v-model="ProdFeeDealInfo.baseFeeRate"  :data-max-length="8"
                      data-integer-length="3" data-max-value="100"
                      data-digits="4"
                      data-min-value="0" data-validate-type="money" data-type="money"
                      :data-allowblank="this.switchSegmentValue || this.switchTimeValue"/>
      </k-form-item>
      <k-form-item label="最高费用">
        <k-field-text v-model="ProdFeeDealInfo.maxCost" data-type="money" :data-max-length="19" data-digits="2"
                      data-integer-length="16" data-validate-type="money"  data-show-gbmoney="true"
                      data-min-value="ProdFeeDealInfo.minCost" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="最低费用">
        <k-field-text v-model="ProdFeeDealInfo.minCost" data-type="money" :data-max-length="19" data-digits="2"
                      data-integer-length="16"  data-validate-type="money"  data-show-gbmoney="true"
                      data-max-value="ProdFeeDealInfo.maxCost" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="是否按金额分段" v-if="false">
        <k-field-select v-model="ProdFeeDealInfo.isAmtSegment" data-default-value="" data-dict="t8_prod_isok" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="是否按时间分段" v-if="false">
        <k-field-select v-model="ProdFeeDealInfo.isTimeSegment" data-dict="t8_prod_isok" :data-disabled="true"/>
      </k-form-item>
      <!-- <k-form-item label="费用说明" :data-col="2">
        <k-field-text v-model="ProdFeeDealInfo.costDesc" :data-allowblank="false"  :data-max-length="2000" inputType="textarea" :rows="1"/>
      </k-form-item> -->
    </k-form>
    <div class="my-container" style="margin-top:30px;">
      <div class="my-item2" v-show="ProdFeeDealInfo.feeTypeDeal !='3'">
        <div style="width: 135px !important" >
          <md-switch :v-model="false" :disabled="true" class="md-info" @change="changeSegmentType" >金额分段</md-switch>
        </div>
        <div class="my-item2-chips">
          <k-field-text v-model="moneyList" class="md-primary" style="padding-top: 4px !important;" md-placeholder="请输入分段金额,并按回车确认"  md-input-type="number"
                    @md-delete="deleteMoney" @md-insert="insertMoney"  v-show="switchSegmentValue" :data-disabled="true"></k-field-text>
        </div>
      </div>
      <div class="my-item3" v-show="prodMode!='1' && ProdFeeDealInfo.feeTypeDeal =='3'">
        <div style="width: 135px !important" >
          <md-switch :v-model="false" :disabled="true"  class="md-info" @change="changeTimeType" >时间分段</md-switch>
        </div>
        <div class="my-item2-chips" >
          <k-field-text v-model="timeList" class="md-primary" style="padding-top: 4px !important;" md-placeholder="请输入分段天数,并按回车确认" md-input-type="number"
                    @md-delete="deleteTime" @md-insert="insertTime"  v-show="switchTimeValue" :data-disabled="true"></k-field-text>
        </div>
      </div>
    </div>
    <div label="金额分段展示"  class="my-table" v-show="switchSegmentValue || switchTimeValue">
      <md-table>
        <md-table-row>
          <md-table-head v-show="moneyList.length > 0 " >金额段</md-table-head>
          <md-table-head v-show="timeList.length > 0" >持有时间</md-table-head>
          <md-table-head>{{feeTableHead}}</md-table-head>
        </md-table-row>

        <!--没有数据时才展示这一行 -->
        <md-table-row v-show="timeList.length == 0 && moneyList.length == 0">
          <md-table-cell :colspan="3" style="text-align:center">
            <p> 暂无数据</p>
          </md-table-cell>
        </md-table-row>

        <md-table-row v-show="timeList.length > 0 || moneyList.length > 0"  v-for="(item,index) in tailingCommisionList" :key="index">
          <md-table-cell v-show="item.showMoneyTd" :rowspan="item.moneyRowspan" >{{ item.moneyDesc }}</md-table-cell>
          <md-table-cell v-show="item.showTimeTd">{{ item.timeDesc }}</md-table-cell>
          <md-table-cell>
            <md-field>
              <span class="md-suffix" v-show="ProdFeeDeal3.chargeType=='0'||ProdFeeDeal3.chargeType==''">￥</span>
              <k-field-text class="md-input" style="width: 80px; text-align: right;" v-model="item.rate"  data-max-value="100"
                            v-show="ProdFeeDeal3.chargeType=='1' "  data-show-gbmoney="true"
                            data-min-value="0"  data-integer-length="3" data-validate-type="money"
                            data-type="money" data-digits="4" :data-disabled="true"/>
<!--              <md-input class="md-input" style="width: 80px; text-align: right;" v-model="item.rate" maxlength="3" md-input-type="number"  -->
<!--                        v-show="ProdFeeDeal3.chargeType=='1'"></md-input>-->
              <md-input class="md-input" style="width: 80px; text-align: right;" v-model="item.constantFee" md-input-type="number" v-show="ProdFeeDeal3.chargeType=='0'||ProdFeeDeal3.chargeType==''" :data-disabled="true"></md-input>
              <span class="md-suffix" v-show="ProdFeeDeal3.chargeType=='1'">%</span>
            </md-field>
          </md-table-cell>
        </md-table-row>
      </md-table>
    </div>
<!--    <div>
      <k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="CLOSE"  data-from="editForm"
                 >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn data-functype="CLOSE" class="btn-custom-plain"><md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </div>-->
  </div>
</template>

<script>
import Tools from '@/utils/tools.js';
export default {
  props: {
    info : {
      type:Object,
    },
    dataParams:{
      type:Array,
    },
    prodMode:'',

  },
  data() {
    return {
      userid: localStorage.getItem("userid"),
      ProdFeeDeal3 :{
        t8PrjFeeLists:[],
        rateType: '0',
        constantRate : ''
      },
      ProdFeeDealInfo:{},
      switchSegmentValue : false,
      switchTimeValue : false,
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
      chargeType:'1',
      rateType: '0',
      constantRate:null,
    };
  },
  created() {
    //手动赋值，使用复制方法会有表单无法修改的问题，原因暂时未知
    this.ProdFeeDeal3 = {
      chargeType:  1,
      constantRate:  null,
      dataStatus:  this.info.dataStatus,
      feeCode:  this.info.id,
      feeRole:  this.info.feeRole,
      feeType:  this.info.isAmtSegment,
      prodCode:  this.info.prodCode,
      t8PrjFeeLists:  this.info.t8PrjFeeLists,
    }

    if(this.ProdFeeDeal3.feeType == '3' ){
      this.timeSwitchDisabled = false;
      this.switchTimeValue = true;
      this.switchSegmentValue = false;
    }else{
      this.timeSwitchDisabled = false;
      this.switchTimeValue = false;
      this.switchSegmentValue = true;
    }
    //根据收费类型改变描述
    this.chargeTypeChange(this.ProdFeeDeal3.chargeType);

    let data = this.info.t8PrjFeeLists;
    if(data.length<2){
      this.switchSegmentValue = false;
      this.switchTimeValue = false;
    }else{
      this.ProdFeeDeal3.t8PrjFeeLists = data;
      data.forEach((e,index) => {
        if(e.dimension2Max != 0  && e.dimension2Max != 0 && e.dimension2Max != null  && e.dimension2Max != null){
          if(index<data.length-1){
            this.timeList.push(e.dimension2Max);
          }

        }else if(e.dimension1Max != 0  &&  e.dimension1Max != 0 && e.dimension1Max != null  &&  e.dimension1Max != null){
          if(index<data.length-1) {
            this.moneyList.push(e.dimension1Max);
          }
        }
      });
      this.initTable(data);
    }

    this.ProdFeeDealInfo =this.info;
  },




  methods: {
    initTable : function(rows){

      rows.forEach((e,index) => {
        if(!(e.dimension2Min == null || e.dimension2Min == '')){
          if(e.dimension2Min != "0" && (this.timeList.indexOf(e.dimension2Min) === -1)){
            this.timeList.push(e.dimension2Min);
          }
          this.switchTimeValue = true;
        }else{
          this.switchTimeValue = false;
        }
      })

      rows.forEach((e,index) => {
        let showMoneyFlag = false;
        let tempFlag = false;
        //alert(this.switchSegmentValue && !this.switchTimeValue);
        //console.log(this.switchSegmentValue);
        //console.log(!this.switchTimeValue);
        if(this.switchSegmentValue && !this.switchTimeValue){
          //只是金额分段,最小值不为0就要展示
          //if(e.dimension1Min != null){
            showMoneyFlag = true;
          //}
        }else{
          //其他情况金额分段描述都不用展示
          showMoneyFlag = false;
        }
        //console.log("showMoneyFlag=:",showMoneyFlag)
        //console.log("天数=:",e.dimension2Max)
        //console.log("金额=:",e.dimension1Max)
        //console.log("费率=:",e.rate)
        if(e.dimension1Max != 0 && e.dimension1Max != 0 && e.dimension1Max != null && e.dimension1Max != null){
          showMoneyFlag = true;
          }
        if(showMoneyFlag){
          this.tailingCommisionList.push({
            dimension1Min: e.dimension1Min,
            dimension1Max: e.dimension1Max,
            dimension2Min: e.dimension2Min,
            dimension2Max: e.dimension2Max,
            moneyDesc: e.dimension1Min == null ? null :(e.dimension1Min + ' <= 金额 < ' + (e.dimension1Max == -1 ? '∞' : e.dimension1Max)),
            //timeDesc: e.dimension2Min == null ? null :(e.dimension2Min + ' <= 天数 < ' + (e.dimension2Max == -1 ? '∞' : e.dimension2Max)),
            rate: e.rate,
            moneyRowspan: e.dimension2Max == "0" ? this.timeList.length+1 : 1,    //2维的时候金额分段只要一条，所以这里选第一条来占行数，后面的都不展示
            showMoneyTd:  showMoneyFlag,
            showTimeTd: e.dimension2Max == null ? false : true
          })
        }else{

          this.tailingCommisionList.push({
            dimension1Min: e.dimension1Min,
            dimension1Max: e.dimension1Max,
            dimension2Min: e.dimension2Min,
            dimension2Max: e.dimension2Max,
            //moneyDesc: e.dimension1Min == null ? null :(e.dimension1Min + ' <= 金额 < ' + (e.dimension1Max == -1 ? '∞' : e.dimension1Max)),
            timeDesc: e.dimension2Min == null ? null :(e.dimension2Min + ' <= 天数 < ' + (e.dimension2Max == -1 ? '∞' : e.dimension2Max)),
            rate: e.rate,
            moneyRowspan: e.dimension2Max == "0" ? this.timeList.length+1 : 1,    //2维的时候金额分段只要一条，所以这里选第一条来占行数，后面的都不展示
            showMoneyTd:  showMoneyFlag,
            showTimeTd: e.dimension2Max == null ? false : true
          })
        }

      })
    },
    chargeTypeChange(value){
      //修改费用、费率描述
      if(value=='0'){
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

    validateData() {
      return this.$refs.editForm.validate();
    },

    beforeSubmit : function(value){
      if(this.ProdFeeDeal3.prodCode==null || this.ProdFeeDeal3.prodCode==''){
        Tools.alert("未取到产品代码","danger");
        return false;
      }

      let ret = this.$refs.editForm.validate();
      if(!ret){
        return false;
      }

      if(this.switchTimeValue || this.switchSegmentValue){
        value.baseFeeRate = '';
      }


      let flags = true;
      // let temp = this.dataParams[value.numId];
      // this.$delete(this.dataParams,value.numId);
      this.dataParams.forEach(function(i,index){
        if(value.feeTypeDeal == i.feeTypeDeal && value.custTypeDeal == i.custTypeDeal && i.numId != value.numId){
          flags = false;
        }
      })
      if(!flags){
        // this.dataParams.push(temp);
        Tools.alert("客户对象已经设置了该费用类型，不要重复设置!","danger");
        return false;
      }
      // this.dataParams.push(value);

      //标记校验是否通过，是否允许提交
      let commitFlag = true;
      let errorMsg = "";
      if(this.switchTimeValue){
        value.isTimeSegment = '1';
        value.isAmtSegment = '0';
        if(this.tailingCommisionList.length < 1){
          commitFlag = false;
          errorMsg = "请输入时间分段";
        }
      }else if(this.switchSegmentValue){
        value.isAmtSegment = '1';
        value.isTimeSegment = '0';
        if(this.tailingCommisionList.length < 1){
          commitFlag = false;
          errorMsg = "请输入金额分段";
        }
      }else{
        value.isTimeSegment = '0';
        value.isAmtSegment = '0';
      }

      if(commitFlag){
        value.t8PrjFeeLists = this.tailingCommisionList;
        value.dataStatus = this.ProdFeeDeal3.dataStatus;
        value.feeRole = this.ProdFeeDeal3.feeRole;
      }else{
        Tools.alert( errorMsg, "danger");
        return false;
      }
      this.$emit('getUptDate',value);


    },
    changeConstantType: function(value) {
      //选择固定费用/费率时，分段设置变为空
      this.switchSegmentValue = !value
      this.switchTimeValue = !value
      this.moneyList = []
      this.timeList = []
      this.ProdFeeDeal3.constantRate = null
      this.ProdFeeDeal3.rateType = this.ProdFeeDeal3.rateType == '0' ? '1' : '0'
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
    changeSelectValue: function(value) {
      //金额分段
      //alert("value=:"+value);
      //console.log("value=:",value)
      //12是金额分段,3是时间分段
      if(value!=3){
        this.moneyList = [];
        this.timeList=[];
        this.ProdFeeDeal3.rateType = this.ProdFeeDeal3.rateType == '0' ? '1' : '0'
        this.switchSegmentValue=false;
        if(this.switchSegmentValue==true){
          this.switchTimeValue=false;
          //this.timeSwitchDisabled=true;
          //this.moneySwitchDisabled = false;
        }else{
          this.switchSegmentValue=false
          //this.timeSwitchDisabled=false;
          //this.moneySwitchDisabled = true;
        }
        this.tailingCommisionList = []
      }else{
        this.moneyList = [];
        this.timeList=[];
        this.ProdFeeDeal3.constantRate = null
        this.ProdFeeDeal3.rateType = this.ProdFeeDeal3.rateType == '0' ? '1' : '0'
        this.switchTimeValue=false;
        if(this.switchSegmentValue==true){
          this.switchTimeValue=false;
          //this.timeSwitchDisabled=true;
          //this.moneySwitchDisabled = false;
        }else{
          this.switchSegmentValue=false
          //this.timeSwitchDisabled=false;
          //this.moneySwitchDisabled = true;
        }
        this.tailingCommisionList = []
      }

      //todo 重新生成列表
    },
    changeSegmentType: function(value) {
      //金额分段

      this.moneyList = [];
      this.timeList=[];
      this.ProdFeeDeal3.constantRate = null
      this.ProdFeeDeal3.rateType = this.ProdFeeDeal3.rateType == '0' ? '1' : '0'
      if(this.switchSegmentValue==true){
        this.switchTimeValue=false;
        //this.timeSwitchDisabled=true;
        //this.moneySwitchDisabled = false;
      }else{
        this.switchSegmentValue=false
        //this.timeSwitchDisabled=false;
        //this.moneySwitchDisabled = true;
      }
      this.tailingCommisionList = []
      //todo 重新生成列表
    },
    changeTimeType : function(value){


      this.moneyList = [];
      this.timeList=[];
      this.ProdFeeDeal3.constantRate = null
      this.ProdFeeDeal3.rateType = this.ProdFeeDeal3.rateType == '0' ? '1' : '0'
      if(this.switchTimeValue==true){
        this.switchSegmentValue=false;
        //this.timeSwitchDisabled=false;
        //this.moneySwitchDisabled = true;
      }else{
        this.switchTimeValue=false;
        //this.timeSwitchDisabled=false;
        //this.moneySwitchDisabled = false;
      }
      this.tailingCommisionList = []
      //todo 重新生成列表
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
    },
    insertMoney : function(value){
      if(value.toString().length>17){
        this.moneyList.pop();
        Tools.alert( "输入数字长度不能大于16字符!", "danger");
        return false;
      }else{
        if(value.indexOf(".")!=-1){
          let arr = value.toString().split(".");
          //console.log("arr=:>>>>",arr);
          if(arr[1].length>2){
            this.moneyList.pop();
            Tools.alert( "小数点后长度不能大于2字符!", "danger");
            return false;
          }
        }

      }
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
      if((value.indexOf(".")!=-1)||value.indexOf("-")!=-1){
        this.timeList.pop();
        Tools.alert( "请输入整数", "danger");
        return false;
      }
      if(value.toString().length>16){
        this.timeList.pop();
        Tools.alert( "输入数字长度不能大于16字符!", "danger");
        return false;
      }
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
      if(this.ProdFeeDeal3.constantRate !=null && this.ProdFeeDeal3.constantRate!=""){
        if(Number(this.ProdFeeDeal3.constantRate)>this.maxRateValue && this.ProdFeeDeal3.chargeType=="1"){
          //固定费率限制小于100
          this.ProdFeeDeal3.constantRate = this.maxRateValue;
        }else if(Number(this.ProdFeeDeal3.constantRate)>this.maxMoneyValue && this.ProdFeeDeal3.chargeType=="0"){
          //固定费用限制小于 99999999999999.99
          this.ProdFeeDeal3.constantRate = this.maxMoneyValue;
        }
      }
    }
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
  margin: -40px 0 15px 10px;
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
