<template>
  <div>
    <k-form ref="addForm" :data-col="2">
      <k-form-item label="产品代码" data-input-width="600px">
          <k-field-select v-model="formData.prodCode" data-action="T8Dict.findTaProdInfos"  :data-allowblank="false"
            data-display-field="prodName"  data-value-field="prodCode" :data-multiple="true"/>
      </k-form-item>
      <k-form-item label="销售商代码" v-show="false">
          <k-field-text v-model="formData.distributorCode"  :data-allowblank="false" data-disabled />
      </k-form-item>
      <k-form-item label="启用日期">
        <k-field-date v-model="formData.enableDate"  :data-allowblank="false" :dataMinValue="this.currentWorkday"/>
      </k-form-item>
      <k-form-item label="计提方式">
        <k-field-select v-model="formData.tailingCalcMode" data-dict="tailing_calc_mode" :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="年天数">
        <k-field-select v-model="formData.tailingCommisionYeardays" data-dict="yeardays"  :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="起付金额">
        <k-field-text data-validate-type="money" v-model="formData.minPayAmt" :dataAllowblank="false" data-min-value="(0" data-max-value="9999999999.99"  data-digits="2"/>
      </k-form-item>
      <k-form-item label="是否累进计算">
        <k-field-select v-model="formData.graduatedCalc" data-dict="1yes0no"  :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="备注" v-show="false">
        <k-field-text v-model="formData.remark" />
      </k-form-item>
    </k-form>


    <div class="my-container">
      <div class="my-item1">
        <div>
          <md-switch v-model="constantRatesSitch" class="md-info" @change="changeConstantType">固定费率(%)</md-switch>
        </div>
        <div>
          <md-field style="padding-top: 17px !important;">
            <md-input class="md-input" style="width: 208px;" v-model="formData.constantRate" type="number" v-show="constantRatesSitch" @blur="handleBlur"></md-input>
          </md-field>
        </div>
      </div>
      <div class="my-item2">
        <div style="width: 135px !important">
          <md-switch v-model="switchValue" class="md-info" @change="changeSegmentType" >分段费率</md-switch>
        </div>
        <div class="my-item2-chips">
          <md-chips v-model="moneyList" class="md-primary" style="padding-top: 4px !important;" md-placeholder="输入分段金额，并按回车确认"  md-input-type="number"
           @md-delete="deleteMoney" @md-insert="insertMoney"  v-show="switchValue" ></md-chips>
        </div>
      </div>
    </div>
    <div label="金额分段展示" id="moneyShowDiv"  v-show="this.tailingCommisionList.length == 0 ? false: true"  v-if="formData.rateType == '1'" class="my-table">
      <md-table v-model="tailingCommisionList">
        <md-table-row slot="md-table-row"  slot-scope="{ item }">
          <md-table-cell  md-label="金额段" >{{ item.desc }} <b id="moneyInfiniteDiv">{{item.Numbermax}} </b></md-table-cell>
          <md-table-cell  md-label="费率%" >
            <md-field>
              <md-input  class="md-input" style="width: 60px; text-align: center;" v-model="item.rate"   md-input-type="number"></md-input>
              <span class="md-suffix">%</span>
            </md-field>
          </md-table-cell>
        </md-table-row>
      </md-table>
    </div>

    <div label="金额默认值展示" id='defaunltMoneyShowDiv'   v-show="this.tailingNullList.length == 0 ? false: true"   v-if="formData.rateType == '1'" class="my-table">
      <md-table v-model="tailingNullList">
        <md-table-row slot="md-table-row"  slot-scope="{ item }">
          <md-table-cell md-label="金额段" >{{ item.desc }} </md-table-cell>
          <md-table-cell  md-label="费率%" >
              <p style="margin-left: -10%;"> 暂无数据</p>
          </md-table-cell>
        </md-table-row>
      </md-table>
    </div>
    <div>
        <k-form>
          <k-form-footer data-align="center">
            <k-btn data-functype="SUBMIT" data-action="T8PrjTailingCommision.addTaPrjTailingCommision" data-from="addForm" :data-model="formData" data-target="T82001Grid" :data-handler="beforeSubmit" class="btn-custom-primary">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn data-functype="CLOSE" class="btn-custom-plain">
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
      updSuccess: Function,
    },
    data() {
      return {
        userid: localStorage.getItem("userid"),
        formData :{
          tailingList:'',
          rateType: '0',
          constantRate : null
        },
        switchValue : false,
        constantRatesSitch: true,
        moneyList: [],
        tailingCommisionList:[],
        tailingNullList: [],
        currentWorkday: null,
      };
    },
    created() {
      this.formData.distributorCode = this.$attrs.value.distributorCode;
      this.httpUtil.sysDate().then(res=>{
        if (res) {
          this.currentWorkday = res;
        }
      });
    },

    methods: {
      beforeSubmit : function(value){

        let errorMsg = "";
        let isCommit = true;

        if(this.formData.distributorCode==null || this.formData.distributorCode==''){
            Tools.alert("未录入销售商代码:","danger");
            return false;
        }

        if(this.formData.rateType == "1"){
          //分段
          if (this.tailingCommisionList.length == 0 ){
            isCommit = false;
            errorMsg = "分段费率：存在尾佣费率未输入";
          }
          this.tailingCommisionList.forEach(e => {
            if(e.rate == null){
              isCommit = false;
              errorMsg = "分段费率：存在尾佣费率未输入";
            }

            if(e.rate <0 || e.rate>100 ){
              isCommit = false;
              errorMsg = "分段费率：存在尾佣费率的值不在0-100范围内";
            }
          })
        }else{
          if(this.formData.constantRate == null  || this.formData.constantRate == "" ){
            isCommit = false;
            errorMsg = "固定费率：费率未输入";
          }

          if (this.formData.constantRate <= 0 ){
            isCommit = false;
            errorMsg = "固定费率：固定费率必须大于0";
          }
        }

        if (!this.$refs.addForm.validate()){
          isCommit = false;
          errorMsg = "未填写必填项";
        }//调用校验必填项

        if(isCommit){
	        this.formData.tailingList = JSON.stringify(this.tailingCommisionList);
	        value.tailingList = this.formData.tailingList;
	        value.distributorCode = this.formData.distributorCode;
        }else{
          Tools.alert(errorMsg, "danger");
          return false;
        }

      },
      changeConstantType: function(value) {
        //固定
        this.switchValue = !this.constantRatesSitch
        this.moneyList = []
        this.formData.constantRate = null
        this.formData.rateType = this.formData.rateType == '0' ? '1' : '0'
        this.tailingCommisionList = []
      },
      //true -> false
      //value == false
      changeSegmentType: function(value) {

        //分段
        this.constantRatesSitch = !this.switchValue
        this.moneyList = []
        this.formData.constantRate = null
        this.formData.rateType = this.formData.rateType == '0' ? '1' : '0'
        this.tailingCommisionList = []
        this.tailingNullList = []
        this.tailingNullList.push({

        })
      },
      deleteMoney : function(text, index){
        //是否已经删完
        if(this.moneyList.length <= 0){
          //Tools.alert( "已经没有数据", "danger");
          this.tailingCommisionList = []
          this.tailingNullList = []
          this.tailingNullList.push({
            desc: ' ',
          })
           return false;
        }
        //index-下标，从0开始
        if(index != this.moneyList.length){
          Tools.alert( "请顺序删除", "danger");
          //在指定位置添加元素,第一个参数指定位置,第二个参数指定要删除的元素,如果为0,则追加
          this.moneyList.splice(index, 0, text);
          return false;
        }
        this.tailingCommisionList.pop();
        this.tailingCommisionList.pop();
        this.tailingCommisionList.push(
          {
            dimension1Min: this.moneyList[this.moneyList.length-1],
            dimension1max: -1,
            desc: this.moneyList[this.moneyList.length-1] + ' < 金额 <= ',
            Numbermax: '∞',
            rate: null
          }
        )
      },
      insertMoney : function(value){
        this.tailingNullList = []
        //插入后，已经插入了
        if(value <= 0 || Number(this.moneyList[this.moneyList.length-2]) > Number(value)){
          this.moneyList.pop();
          Tools.alert( "金额不能小于等于0，不能小于上次输入金额", "danger");
          return false;
        }
        this.tailingCommisionList.pop();
        this.tailingCommisionList.push(
          {
            dimension1Min: this.moneyList.length == 1 ? 0 : this.moneyList[this.moneyList.length-2],
            dimension1Max: value,
            desc: (this.moneyList.length == 1 ? '0' : this.moneyList[this.moneyList.length-2] )+ ' < 金额 <= ' + value,
            rate: null
          },{
            dimension1Min: value,
            dimension1max: -1,
            desc: value + ' < 金额 <= ',
            Numbermax: '∞',
            rate: null
          }
        )
        return value;
      },
      handleBlur : function(){
        if(this.formData.constantRate !=null && this.formData.constantRate!="" && Number(this.formData.constantRate)>100){
            //固定费率限制小于100
            this.formData.constantRate = 100;
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
    margin-top: -15px;
  }

  .md-table-head-container{
    text-align: center;
  }
  .md-table-head-label{
    font-size: inherit;
    font-weight: 800;
  }

  .md-table-cell-container{
    text-align: center;
  }
  #moneyInfiniteDiv{
      font-size: 20px;
  }
  ::v-deep #moneyShowDiv .md-table-head {
     text-align: center;
  }
  ::v-deep #moneyShowDiv  .md-input{
     text-align: center;
  }

  ::v-deep #moneyShowDiv .md-table-cell-container{
    margin-right: 40%;
     text-align: center;
  }
  ::v-deep #moneyShowDiv .md-table-head-label{
    margin-right: 40%;
  }

  ::v-deep #defaunltMoneyShowDiv .md-table-head-container {
     margin-right: 30%;
      text-align: center;
  }

  ::v-deep #defaunltMoneyShowDiv .md-table-cell {
      text-align: left;
  }

  ::v-deep #defaunltMoneyShowDiv .md-table-head-label {
     margin-right: 30%;
  }


::v-deep input::-webkit-outer-spin-button,
::v-deep input::-webkit-inner-spin-button {
  -webkit-appearance: none !important;
}
::v-deep input[type="number"]{
  -moz-appearance: textfield;
}

</style>
