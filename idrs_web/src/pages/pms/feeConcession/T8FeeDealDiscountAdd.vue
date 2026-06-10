<template>
  <div>
    <k-form ref="editForm" :data-col="2">
      <k-form-item label="产品代码" v-show="false">
        <k-field-text v-model="T8FeeDealDiscount.prodCode"/>
      </k-form-item>
      <k-form-item label="产品主表id" v-show="false">
        <k-field-text v-model="T8FeeDealDiscount.t8ProdInfoId"/>
      </k-form-item>
      <k-form-item label="交易费用类型">
        <k-field-select v-model="T8FeeDealDiscount.feeTypeDeal" :data-data="feeTypeDict"
                        :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="销售商代码">
        <k-field-select v-model="T8FeeDealDiscount.distributorCode" data-action="T8Dict.findProdDistributorInfos"
                        data-display-field="distributorName"  data-value-field="distributorCode"
                        :data-params="{prodCode:T8FeeDealDiscount.prodCode}" :data-allowblank="false"/>
      </k-form-item>
      <!-- <k-form-item label="交易客户类型">
        <k-field-select v-model="T8FeeDealDiscount.custTypeDeal" data-dict="t8_cust_type_deal"
                        :data-allowblank="false"/>
      </k-form-item> -->
      <k-form-item label="优惠比例（%）" v-show="this.isShowDiscountFee">
        <k-field-text v-model="T8FeeDealDiscount.discountProportion" data-validate-type="number" data-digits="2"
                      :dataAllowblank="!this.isShowDiscountFee" data-min-value="0" data-max-value="100"/>
      </k-form-item>
      <k-form-item label="优惠生效日期">
        <k-field-date v-model="T8FeeDealDiscount.validateDate"
                      data-type="date"
                      ref="startDate"
                      :dataAllowblank="false"
                      :data-min-value="info.establishDate"
                      :data-max-value="T8FeeDealDiscount.invalidateDate===''?'20991230':T8FeeDealDiscount.invalidateDate"
        />
      </k-form-item>
      <k-form-item label="优惠失效日期">
        <k-field-date v-model="T8FeeDealDiscount.invalidateDate"
                      data-type="date"
                      :dataAllowblank="true"
                      :data-min-value="T8FeeDealDiscount.validateDate===''?'19700101':T8FeeDealDiscount.validateDate"/>
      </k-form-item>
      <k-form-item label="基本费率(%)" v-if="!(this.switchSegmentValue||this.switchTimeValue)">
        <k-field-text v-model="T8FeeDealDiscount.baseFeeRate" :data-max-length="8" data-integer-length="3" data-max-value="100"
                      data-digits="4" :dataAllowblank="false"
                      data-min-value="0" data-validate-type="money" data-type="money" data-disabled="true"/>
      </k-form-item>
      <k-form-item label="优惠后费率(%)" v-show="!(this.switchSegmentValue||this.switchTimeValue)">
        <k-field-text v-model="T8FeeDealDiscount.feeRateAfterDiscount" :data-max-length="8" data-integer-length="3" data-max-value="100"
                      data-digits="4"
                      data-min-value="0" data-validate-type="money" data-type="money"  data-disabled="true"/>
      </k-form-item>
      <k-form-item label="是否按金额分段" v-show="false">
        <k-field-select v-model="T8FeeDealDiscount.isAmtSegment" data-default-value="" data-dict="t8_prod_isok"/>
      </k-form-item>
      <k-form-item label="是否按时间分段" v-show="false">
        <k-field-select v-model="T8FeeDealDiscount.isTimeSegment" data-dict="t8_prod_isok"/>
      </k-form-item>
    </k-form>
    <div label="金额分段展示"  class="my-table" v-show="switchSegmentValue || switchTimeValue" style="margin-top: 20px;">
      <md-table>
        <md-table-row>
          <md-table-head v-if="moneyList.length > 0 ">金额段</md-table-head>
          <md-table-head v-if="timeList.length > 0" >持有时间</md-table-head>
          <md-table-head>{{feeTableHead}}</md-table-head>
          <md-table-head>{{feeTableDiscount}}</md-table-head>
          <md-table-head>优惠后费率</md-table-head>
        </md-table-row>
        <md-table-row v-show="timeList.length > 0 || moneyList.length > 0"  v-for="(item,index) in tailingCommisionList" :key="index">
          <md-table-cell v-if="item.showMoneyTd" :rowspan="item.moneyRowspan" style="text-align: center">{{ item.moneyDesc }}</md-table-cell>
          <md-table-cell v-if="item.showTimeTd">{{ item.timeDesc }}</md-table-cell>
          <md-table-cell>
            <md-field style="width:70%;">
              <span class="md-suffix" v-if="ProdFeeDeal3.chargeType=='0'||ProdFeeDeal3.chargeType==''">￥</span>
              <md-input class="md-input" style="text-align: center;width:60px;" v-model="item.amtRate" maxlength="3" md-input-type="number"  :disabled="false" v-show="ProdFeeDeal3.chargeType=='1'"></md-input>
              <md-input class="md-input" style="text-align: center;width:60px" v-model="item.constantFee" md-input-type="number" :disabled="true"  v-show="ProdFeeDeal3.chargeType=='0'||ProdFeeDeal3.chargeType==''"></md-input>
              <span class="md-suffix" v-if="ProdFeeDeal3.chargeType=='1'">%</span>
            </md-field>
          </md-table-cell>
          <md-table-cell>
            <md-field style="width:70%;text-align:center;" >
              <span class="md-suffix" v-if="ProdFeeDeal3.chargeType=='0'||ProdFeeDeal3.chargeType==''">￥</span>
              <md-input class="md-input text-align-center" style="text-align: center;width:60px;padding-right:0px !important;" v-model="item.discountRate"  data-max-value="100"
                            v-show="ProdFeeDeal3.chargeType=='1'" data-min-value="0" @input="changeRate(item)" data-integer-length="3" data-validate-type="number"
                            data-type="number" data-digits="4" :data-disabled="false"/>
<!--              <md-input class="md-input" style="text-align: center;width:60px;" v-model="item.discountRate"  md-input-type="number" @input="" :disabled="false" v-show="ProdFeeDeal3.chargeType=='1'"></md-input>-->
              <span class="md-suffix" v-if="ProdFeeDeal3.chargeType=='1'">%</span>
            </md-field>
          </md-table-cell>
          <md-table-cell>
          <md-field style="width:70%">
            <span class="md-suffix" v-if="ProdFeeDeal3.chargeType=='0'||ProdFeeDeal3.chargeType==''">￥</span>
            <md-input v-model="item.amtRateAfterDiscount" class="md-input" style="text-align: center;width:60px;" :disabled="true"></md-input>
            <span class="md-suffix" v-if="ProdFeeDeal3.chargeType=='1'">%</span>
          </md-field>
          </md-table-cell>
        </md-table-row>
      </md-table>
    </div>
    <div>
      <k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8FeeDealDiscount.insertDealDiscount" data-from="editForm"
                 :data-handler="beforeSubmit" :data-model="T8FeeDealDiscount" data-target="feeDealDiscountGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn data-functype="CLOSE" class="btn-custom-plain"><md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </div>
  </div>
</template>

<script>
import Tools from '@/utils/tools.js';
export default {
  name:'T8FeeDealDiscountAdd',
  props: {
    info : {
      type:Object,
    },

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
      constantRatesSitch: true,
      moneyList: [],
      timeList:[],
      tailingCommisionList:[],
      tailingCommisionMoneyList:[],
      tailingCommisionTimeList:[],
      moneySwitchDisabled:false,    //金额分段切换按钮是否可用
      timeSwitchDisabled:false,     //时间分段切换按钮是否可用
      constantDesc : "固定费率(%)",
      feeTableHead: "费率",
      feeTableDiscount: "优惠比例",
      maxMoneyValue:99999999999999.99,
      maxRateValue:100,
      chargeType:'1',
      rateType: '0',
      constantRate:null,
      feeTypeDict:[{value:'2',label:'申购费'},{value:'3',label:'赎回费'}],
      t8ProdInfoId:'',  //产品id
      prodCode:'', //产品代码
      distributorCode:'', //销售商代码
      feeId:'',//交易费用id
      T8FeeDealDiscount:{},//交易费用优惠实体
      isShowDiscountFee:false,
    };
  },
  created() {
    this.t8ProdInfoId = this.info.id;
    this.prodCode = this.info.prodCode;
    this.T8FeeDealDiscount.t8ProdInfoId = this.info.id;
    this.T8FeeDealDiscount.prodCode = this.info.prodCode;
    //手动赋值，使用复制方法会有表单无法修改的问题，原因暂时未知
    this.ProdFeeDeal3 = {
      chargeType:  1,
      constantRate:  null,
      prodCode:  this.info.prodCode,
    }


    //根据收费类型改变描述
    //this.chargeTypeChange(this.ProdFeeDeal3.chargeType);
  },
  watch:{
    //监听优惠比例
    'T8FeeDealDiscount.discountProportion':function(value){
      if(value!=''){
        this.T8FeeDealDiscount.feeRateAfterDiscount = this.changeNumber(this.T8FeeDealDiscount.baseFeeRate*(1-(value/100)));
        if(this.tailingCommisionList != []){
          for(let i=0;i<this.tailingCommisionList.length;i++){
            this.tailingCommisionList[i].amtRateAfterDiscount = this.changeNumber(this.tailingCommisionList[i].amtRate*(1-(value/100)));

          }
        }
      }
    },
    //监听基本费率
    'T8FeeDealDiscount.baseFeeRate': function (value) {
      if (this.T8FeeDealDiscount.discountProportion != 0 &&this.T8FeeDealDiscount.discountProportion != undefined &&this.T8FeeDealDiscount.discountProportion != '') {
        this.T8FeeDealDiscount.feeRateAfterDiscount = this.changeNumber(value * (1 - (this.T8FeeDealDiscount.discountProportion / 100)));
      }
    },
    //监听费用类型  发生变化重新查询产品交易费用信息
    'T8FeeDealDiscount.feeTypeDeal': function (value) {
      this.findFeeDealByCodeAndType(value);
    },
    //监听交易客户类型 发生变化重新查询产品交易费用信息
    /*'T8FeeDealDiscount.custTypeDeal': function (value) {
      if (this.T8FeeDealDiscount.feeTypeDeal != '' && this.T8FeeDealDiscount.feeTypeDeal != undefined) {
        this.findFeeDealByCodeAndType(this.T8FeeDealDiscount.feeTypeDeal, value);
      }
    },*/

  },
  methods: {
    changeRate(value){
      if(value!=''){
        this.T8FeeDealDiscount.feeRateAfterDiscount = this.changeNumber(this.T8FeeDealDiscount.baseFeeRate*(1-(value.discountRate/100)));
        value.amtRateAfterDiscount = this.changeNumber(value.amtRate*(1-(value.discountRate/100)));
      }
    },
    //保留两位小数
    changeNumber(value){
      return Number(value).toFixed(5);
    },

    //初始化表格
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
        if(this.switchSegmentValue && !this.switchTimeValue){
          //只是金额分段,最小值不为0就要展示
          //if(e.dimension1Min != null){
          showMoneyFlag = true;
          //}
        }else{
          showMoneyFlag = false;
        }
        if(e.dimension1Max != "0"&&e.dimension1Max !="0.00"&&e.dimension1Max !=null&&e.dimension1Max !=""){
          showMoneyFlag = true;
        }
        if(showMoneyFlag){
          this.tailingCommisionList.push({
            minAmt: e.dimension1Min,
            maxAmt: e.dimension1Max,
            minHoldDays: e.dimension2Min,
            maxHoldDays: e.dimension2Max,
            moneyDesc: e.dimension1Min == null ? null :(e.dimension1Min + ' <= 金额 < ' + (e.dimension1Max == -1 ? '∞' : e.dimension1Max)),
            //timeDesc: e.dimension2Min == null ? null :(e.dimension2Min + ' <= 天数 < ' + (e.dimension2Max == -1 ? '∞' : e.dimension2Max)),
            discountRate:e.discountRate,
            amtRate: e.rate,
            moneyRowspan: e.dimension2Max == "0" ? this.timeList.length+1 : 1,    //2维的时候金额分段只要一条，所以这里选第一条来占行数，后面的都不展示
            showMoneyTd:  showMoneyFlag,
            showTimeTd: (e.dimension2Max == null||e.dimension2Max == 0) ? false : true,
            amtRateAfterDiscount:e.rate,
          })
        }else{
          this.tailingCommisionList.push({
            minAmt: e.dimension1Min,
            maxAmt: e.dimension1Max,
            minHoldDays: e.dimension2Min,
            maxHoldDays: e.dimension2Max,
            //moneyDesc: e.dimension1Min == null ? null :(e.dimension1Min + ' <= 金额 < ' + (e.dimension1Max == -1 ? '∞' : e.dimension1Max)),
            timeDesc: e.dimension2Min == null ? null :(e.dimension2Min + ' <= 天数 < ' + (e.dimension2Max == -1 ? '∞' : e.dimension2Max)),
            discountRate:e.discountRate,
            amtRate: e.rate,
            moneyRowspan: e.dimension2Max == "0" ? this.timeList.length+1 : 1,    //2维的时候金额分段只要一条，所以这里选第一条来占行数，后面的都不展示
            showMoneyTd:  showMoneyFlag,
            showTimeTd: e.dimension2Max == null ? false : true,
            amtRateAfterDiscount:e.rate,
          })
        }
      })
    },
    //根据交易费用id查询分段信息
    findPrjFeeListById(){
      this.httpUtil.comnQuery({
        action: 'T8PrjFeeList.findT8PrjFeeLists',
        params: {
          feeDealId : this.feeId
        }
      }).then(data => {
        if(data.rows.length<2){
          this.switchSegmentValue = false;
          this.switchTimeValue = false;
          this.constantRatesSitch = true;
        }else{
          this.constantRatesSitch = false;
          this.ProdFeeDeal3.t8PrjFeeLists = data.rows;
          data.rows.forEach((e,index) => {
            if(e.dimension2Max != "0"&&e.dimension2Max != "0.00"&&e.dimension2Max !=""){
              if(index<data.rows.length-1){
                this.timeList.push(e.dimension2Max);
              }
            }else if(e.dimension1Max != "0"&&e.dimension1Max != "0.00"&&e.dimension1Max !=""){
              if(index<data.rows.length-1) {
                this.moneyList.push(e.dimension1Max);
              }
            }
          });
          this.initTable(data.rows);
        }

      });
    },
    //根据产品代码与费用类型、交易客户类型查询交易费用信息
    findFeeDealByCodeAndType(feeType) {
      this.httpUtil.comnQuery({
        action: 'T8FeeDealDiscount.findFeeDealByCodeAndType',
        params: {
          prodCode: this.prodCode,
          feeTypeDeal: feeType,
        }
      }).then(data => {
        this.moneyList = [];
        this.timeList = [];
        this.tailingCommisionList = [];
        this.T8FeeDealDiscount.baseFeeRate='';
        this.T8FeeDealDiscount.feeRateAfterDiscount='';

        if(data.rows.length>0){ //存在分段信息
          this.isShowDiscountFee = false;
          this.ProdFeeDealInfo = data.rows[0];
          this.T8FeeDealDiscount.baseFeeRate = this.ProdFeeDealInfo.baseFeeRate;
          this.feeId = this.ProdFeeDealInfo.id;
          let isAmtSegment= this.ProdFeeDealInfo.isAmtSegment;
          let isTimeSegment = this.ProdFeeDealInfo.isTimeSegment;
          this.T8FeeDealDiscount.isAmtSegment= isAmtSegment;
          this.T8FeeDealDiscount.isTimeSegment=isTimeSegment;
          if(isAmtSegment === '1'){
            this.switchSegmentValue = true;
          }
          if(isTimeSegment === '1'){
            this.switchTimeValue = true;
          }
          if(isAmtSegment === '0'&&isTimeSegment === '0'){
            this.isShowDiscountFee = true;
          }

          this.findPrjFeeListById();
        }else{
          this.isShowDiscountFee = true;
          this.switchSegmentValue = false;
          this.switchTimeValue = false;
        }
      });
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
       let ret = this.$refs.editForm.validate();
       if(!ret){
         return false;
       }
      // //标记校验是否通过，是否允许提交
      if(this.switchTimeValue){
        this.ProdFeeDeal3.isTimeSegment = '1';
        this.ProdFeeDeal3.isAmtSegment = '0';
      }else
      if(this.switchSegmentValue){
        this.ProdFeeDeal3.isAmtSegment = '1';
        this.ProdFeeDeal3.isTimeSegment = '0';
      }else{
        this.ProdFeeDeal3.isTimeSegment = '0';
        this.ProdFeeDeal3.isAmtSegment = '0';
      }
      this.T8FeeDealDiscount.isAmtSegment = this.ProdFeeDeal3.isAmtSegment;
      this.T8FeeDealDiscount.isTimeSegment = this.ProdFeeDeal3.isTimeSegment;
      this.ProdFeeDeal3.t8PrjFeeLists = JSON.stringify(this.tailingCommisionList);
      value.t8PrjFeeLists = this.ProdFeeDeal3.t8PrjFeeLists;

      //审批流反显 axin
      this.$set(value,'switchSegmentValue',this.switchSegmentValue);
      this.$set(value,'switchTimeValue',this.switchTimeValue);
      this.$set(value,'feeTableHead',this.feeTableHead);
      this.$set(value,'moneyList',JSON.stringify(this.moneyList));
      this.$set(value,'timeList',JSON.stringify(this.timeList));
      this.$set(value,'ProdFeeDeal3',JSON.stringify(this.ProdFeeDeal3));
      this.$set(value,'tailingCommisionList',JSON.stringify(this.tailingCommisionList));
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
