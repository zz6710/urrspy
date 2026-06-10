<template>
  <div>
    <k-form ref="addShareSortForm" :data-col="2" data-label-width="300px">
      <k-form-item label="产品代码">
        <k-field-text v-model="formProdShareSort.prodCode" :data-allowblank="true" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="formProdShareSort.prodName" :data-allowblank="true"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="份额名称">
        <k-field-select v-model="formProdShareSort.shareName" data-dict="t8_share_name"
                        :data-max-length="100" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="销售名称">
        <k-field-text v-model="formProdShareSort.salesName" :data-max-length="100" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="销售代码">
        <k-field-text v-model="formProdShareSort.salesCode" :data-max-length="60" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="销售客群">
        <k-field-text v-model="formProdShareSort.salesGroup" :data-max-length="200" :data-disabled="true"/>
      </k-form-item>
      <k-form-item>
      </k-form-item>
      <div class="tableLine"><span class="midText">份额分类信息</span></div>

      <k-form-item label="起点金额">
        <k-field-text v-model="formProdShareSort.minAmount" data-validate-type="money" data-type="money"
                      :data-disabled="true" data-placeholder="单位(元)"/>
      </k-form-item>
      <k-form-item label="递增金额">
        <k-field-text v-model="formProdShareSort.stepAmount" data-validate-type="money" data-type="money"
                      :data-disabled="true" data-placeholder="单位(元)"/>
      </k-form-item>
      <k-form-item label="认购追加金额">
        <k-field-text v-model="formProdShareSort.subAppendAmount" data-validate-type="money" data-type="money"
                      :data-disabled="true" data-placeholder="单位(元)"/>
      </k-form-item>
      <k-form-item label="申购追加金额" v-if="this.popShow">
        <k-field-text v-model="formProdShareSort.redeemAppendAmount" data-validate-type="money" data-type="money"
                      :data-disabled="true" data-placeholder="单位(元)"/>
      </k-form-item>
      <k-form-item label="单笔最小赎回份额" v-if="this.popShow">
        <k-field-text v-model="formProdShareSort.minRedeemVol" data-validate-type="money" data-type="money"
                      :data-disabled="true" data-placeholder="单位(份)"/>
      </k-form-item>
      <k-form-item label="持有份额下限">
        <k-field-text v-model="formProdShareSort.minHoleVol" data-validate-type="money" data-type="money"
                      :data-disabled="true" data-placeholder="单位(份)"/>
      </k-form-item>
      <k-form-item label="持有份额上限">
        <k-field-text v-model="formProdShareSort.maxHoleVol" data-validate-type="money" data-type="money"
                      :data-disabled="true" data-placeholder="单位(份)"/>
      </k-form-item>
      <div class="tableLine"><span class="midText">销售服务费信息</span></div>

      <k-form-item label="销售服务费率(%)">
        <k-field-text v-model="formProdShareSort.salesFeeRate" data-validate-type="number"
                      :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="付费规则">
        <k-field-select v-model="formProdShareSort.feeRules" data-dict="t8_payment_rules" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="计提基数">
        <k-field-select v-model="formProdShareSort.raiseBase" data-dict="t8_charging_index" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="计提规则">
        <k-field-select v-model="formProdShareSort.raiseRules" data-dict="t8_raise_rules" :data-disabled="true"/>
      </k-form-item>
      <div class="tableLine"><span class="midText">业绩基准信息</span></div>

      <k-form-item label="基准类型">
        <k-field-select v-model="formProdShareSort.baseType" data-dict="t8_base_type" :data-disabled="true"
        />
      </k-form-item>
      <div>
        <k-form ref="addForm2" v-for="(item,index) in envItems" :key="index"
                v-if="formProdShareSort.baseType == '3' || formProdShareSort.baseType == '5'"
                :data-col="2" data-input-width="100px" data-label-width="120px" data-total-width="1500px">
          <k-form-item :label="index=='0'?'':'操作符'">
            <k-field-select v-model="item.operator" data-dict="t8_performance_operator" data-display-field="itemval"  data-value-field="itemkey"
                            :data-disabled="true" v-if="index != '0'"></k-field-select>
          </k-form-item>
          <k-form-item :label="'数据类型'" v-if="formProdShareSort.baseType == '5'" key="ratioType">
            <k-field-select v-model="item.ratioType" data-dict="t8_performance_ratio_type" data-display-field="itemval"  data-value-field="itemkey"
                            :data-disabled="true" style="width: 120px"></k-field-select>
          </k-form-item>
          <k-form-item :label="'数据类型'" v-if="formProdShareSort.baseType == '3'" key="ratioType1">
            <k-field-select v-model="item.ratioType" data-dict="t8_performance_ratio_type" data-display-field="itemval"  data-value-field="itemkey"
                            data-default-value="1" data-disabled="true"
                            :data-allowblank="false" style="width: 120px"></k-field-select>
          </k-form-item>
          <k-form-item :label="'指数名称'" v-if="item.ratioType == '1' || item.ratioType == null || item.ratioType == ''" key="ratioIndex">
            <k-field-select v-model="item.ratioIndex" data-value-field="indexCode" :data-allowblank="false" :data-disabled="true"
                            data-display-field="indexName" data-action="T8IndexInfo.find" style="width: 160px"/>
          </k-form-item>
          <k-form-item :label="'自定义利率'" v-if="item.ratioType == '2'" key="fixedValue">
            <k-field-text v-model="item.fixedValue"  :data-disabled="true" :data-max-length="8"
                          data-min-value="0"  data-validate-type="money"
                          data-type="money" data-digits="2" style="width: 160px"/>
          </k-form-item>
          <k-form-item :label="'市场利率'" v-if="item.ratioType == '3'" key="marketRate">
            <k-field-select v-model="item.marketRate"  :data-disabled="true"
                            data-dict="t8_market_rate" data-display-field="itemval"  data-value-field="itemkey" style="width: 160px"/>
          </k-form-item>
          <k-form-item :label="'系数%'">
            <k-field-text v-model="item.coefficient"  :data-disabled="true" :data-max-length="8"
                          data-min-value="0" data-max-value="100" data-integer-length="3" data-validate-type="money"
                          data-type="money" data-digits="2" style="width:70%;"/>
          </k-form-item>
        </k-form>
      </div>
      <!--      <k-form-item label="业绩报酬提取比例%">-->
      <!--        <k-field-text v-model="formProdShareSort.performanceOut" :data-max-length="8"-->
      <!--                      data-min-value="0" data-integer-length="3" data-validate-type="money" data-max-value="100"-->
      <!--                      :data-disabled="true"/>-->
      <!--      </k-form-item>-->
      <k-form-item label="基准利率%" v-if="formProdShareSort.baseType == '1'">
        <k-field-text v-model="formProdShareSort.baseRate" :data-max-length="8"
                      :data-allowblank="formProdShareSort.baseType != '1'"
                      :data-disabled="true"/>
      </k-form-item>
<!--      <k-form-item label="业绩比较基准说明" :data-col="2" v-if="formProdShareSort.baseType != '0'">-->
<!--        <k-field-text v-model="formProdShareSort.perfMethodExplain" :data-max-length="2000" inputType="textarea"-->
<!--                      :rows="1" :data-disabled="true"/>-->
<!--      </k-form-item>-->
<!--      <k-form-item label="自定义利率%" v-if="formProdShareSort.baseType == '5' ">-->
<!--        <k-field-text v-model="formProdShareSort.custom" :data-max-length="8"-->
<!--                      data-min-value="0" data-max-value="100" data-integer-length="3" data-validate-type="money"-->
<!--                      :data-disabled="true"/>-->
<!--      </k-form-item>-->
      <k-form-item label="基准利率下限%" v-if="formProdShareSort.baseType == '2' ">
        <k-field-text v-model="formProdShareSort.baseMinRate" :data-max-length="8"
                      :data-allowblank="formProdShareSort.baseType != '2'"
                      data-min-value="0" data-integer-length="3" data-validate-type="money"
                      :data-max-value="formProdShareSort.baseMaxRate===''?'100':formProdShareSort.baseMaxRate"
                      :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="基准利率上限%" v-if="formProdShareSort.baseType == '2' ">
        <k-field-text v-model="formProdShareSort.baseMaxRate" :data-max-length="8"
                      :data-allowblank="formProdShareSort.baseType != '2'"
                      :data-min-value="formProdShareSort.baseMinRate===''?'0':formProdShareSort.baseMinRate"
                      data-integer-length="3" data-validate-type="money" data-max-value="100"
                      :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="市场利率类型" v-if="formProdShareSort.baseType == '4' ">
        <k-field-select v-model="formProdShareSort.marketRate" data-dict="t8_market_rate" :data-disabled="true"
                        :data-allowblank="formProdShareSort.baseType != '4'"
        />
      </k-form-item>
      <k-form-item label="业绩比较基准说明(产品要素、投资管理使用)" :data-col="2" v-if="formProdShareSort.baseType != '0'">
        <k-field-text v-model="formProdShareSort.perfMethodExplain" :data-max-length="2000" inputType="textarea"
                      :rows="1" :data-disabled="true"/>
      </k-form-item>
      <div style="width:100%">
        <div class="my-container" v-if="formProdShareSort.baseType == '2' ">
          <div class="my-item2">
            <div style="width: 135px !important">
              <md-switch v-model="switchSegmentValue" class="md-info" :disabled="true">分段计提</md-switch>
            </div>
            <div class="my-item2-chips">
              <md-chips v-model="moneyList" class="md-primary" style="padding-top: 4px !important;"
                        md-placeholder="请输入基准利率区间,并按回车确认"
                        md-input-type="number"
                        v-show="switchSegmentValue && formProdShareSort.baseType == '2'"></md-chips>
            </div>
          </div>
        </div>

        <div label="分段计提展示" class="my-table" v-if="switchSegmentValue  && formProdShareSort.baseType == '2' ">
          <md-table>
            <md-table-row>
              <md-table-head v-show="moneyList.length > 0  && formProdShareSort.baseType == '2'">基准利率区间%
              </md-table-head>
              <md-table-head v-show="moneyList.length > 0  && formProdShareSort.baseType == '2'"> 计提比例%
              </md-table-head>
            </md-table-row>

            <!--没有数据时才展示这一行 -->
            <md-table-row v-show="moneyList.length == 0 && formProdShareSort.baseType == '2'">
              <md-table-cell :colspan="3" style="text-align:center">
                <p> 暂无数据</p>
              </md-table-cell>
            </md-table-row>

            <md-table-row v-show="moneyList.length > 0 && formProdShareSort.baseType == '2'"
                          v-for="(item,index) in tailingCommisionList" :key="index">
              <md-table-cell v-show="formProdShareSort.baseType == '2'"
                             :rowspan="item.moneyRowspan">
                {{ item.moneyDesc }}
              </md-table-cell>
              <md-table-cell>
                <md-field style="width: 50%">
                  <md-input class="md-input" style="width: 80px; text-align: right;" v-model="item.rateAccrual"
                            type="number" :disabled="true"></md-input>
                  <span class="md-suffix">%</span>
                </md-field>
              </md-table-cell>
            </md-table-row>
          </md-table>
        </div>
      </div>



    </k-form>
  </div>
</template>

<script>
export default {
  name: "ProdShareSortDetail",
  props: {
    t8ProdInfoId:'',
    formProdShareSort: {},
    envItems: {
      type: Array,
      default: [{}],
    },
    tailingCommisionList: {
      type: Array,
      default: [],
    },
    moneyList: {
      type: Array,
      default: [],
    }
  },
  data() {
    return {
      timeList: [],
      constantRatesSitch: true,     //固定切换按钮的值
      switchSegmentValue: true,
      tailingCommisionMoneyList: [],
      tailingCommisionTimeList: [],
      tableParams: [],
      updateParam: [],
      popupTitle: '',
      formType: '',
      options: [],
       popShow:true,
    }
  },
  created() {
 console.log(this.t8ProdInfoId);
     this.httpUtil.comnQuery({
        action: 'T8ProdInfo.findT8ProdInfos',
        params: {
          id : this.t8ProdInfoId ,
        },
        successAlert: false,
      }).then(data => {
        if(data.rows.length > 0 ){
          let row = data.rows[0];
          if(row.prodMode=='1'){
            this.popShow=false;
          }

        }

      });
  },
  methods: {
    buildTimeTable: function () {
      this.tailingCommisionList = [];
      //先按利率组合，再按持有时间
      this.tailingCommisionTimeList = [];
      for (var i = 0; i < this.timeList.length; i++) {

        //1-利率，2-时间
        this.tailingCommisionTimeList.push(
          {
            dimension1Min: null,
            dimension1Max: null,
            dimension2Min: i == 0 ? 0 : this.timeList[i - 1],
            dimension2Max: this.timeList[i],
            moneyDesc: null,
            timeDesc: (i == 0 ? 0 : this.timeList[i - 1]) + ' <= 天数 < ' + this.timeList[i],
            rate: null,
            constantFee: null,
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
        dimension2Min: this.timeList[this.timeList.length - 1],
        dimension2Max: '-1',
        moneyDesc: null,
        timeDesc: this.timeList[this.timeList.length - 1] + ' <= 天数 < ∞',
        rate: null,
        constantFee: null,
        minFee: null,
        maxFee: null,
        dataStatus: 'E',
        moneyRowspan: 1,
        showMoneyTd: false,
        showTimeTd: true
      };
      this.tailingCommisionTimeList.push(lastTime);
      if (this.moneyList == null || this.moneyList.length == 0) {
        //为空只有一个维度
        this.tailingCommisionList = this.tailingCommisionTimeList;
      } else {
        //不为空，两个维度
        for (let i = 0; i <= this.moneyList.length; i++) {
          for (let j = 0; j < this.tailingCommisionTimeList.length; j++) {
            let temp = Object.assign({}, this.tailingCommisionTimeList[j]);
            temp.showMoneyTd = j != 0 ? false : true;
            temp.moneyRowspan = j == 0 ? this.tailingCommisionTimeList.length : 1;
            temp.moneyDesc = (i == 0 ? 0 : this.moneyList[i - 1]) + ' <= 利率 < ' + (this.moneyList.length == i ? ' ∞ ' : this.moneyList[i]);
            temp.dimension1Min = i == 0 ? 0 : this.moneyList[i - 1];
            temp.dimension1Max = i == this.moneyList.length ? '-1' : this.moneyList[i];
            this.tailingCommisionList.push(temp);
          }
        }
      }
    },
    buildMoneyTable: function () {
      this.tailingCommisionList = [];
      //先按金额组合，再按持有时间
      this.tailingCommisionMoneyList = [];

      for (var i = 0; i < this.moneyList.length; i++) {

        //1-金额，2-时间
        this.tailingCommisionMoneyList.push(
          {
            dimension1Min: i == 0 ? 0 : this.moneyList[i - 1],
            dimension1Max: this.moneyList[i],
            dimension2Min: null,
            dimension2Max: null,
            moneyDesc: (i == 0 ? 0 : this.moneyList[i - 1]) + ' <= 利率 < ' + this.moneyList[i],
            timeDesc: null,
            rate: null,
            constantFee: null,
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
        dimension1Min: this.moneyList[this.moneyList.length - 1],
        dimension1Max: '-1',
        dimension2Min: null,
        dimension2Max: null,
        moneyDesc: this.moneyList[this.moneyList.length - 1] + ' <= 利率 < ∞',
        timeDesc: null,
        rate: null,
        constantFee: null,
        minFee: null,
        maxFee: null,
        dataStatus: 'E',
        moneyRowspan: 1,
        showMoneyTd: true,
        showTimeTd: false
      };
      this.tailingCommisionMoneyList.push(lastMoney);
      if (this.timeList == null || this.timeList.length == 0) {
        //为空只有一个维度
        this.tailingCommisionList = this.tailingCommisionMoneyList;
      } else {
        //不为空，两个维度
        for (let i = 0; i < this.tailingCommisionMoneyList.length; i++) {
          for (var j = 0; j <= this.timeList.length; j++) {
            let temp = Object.assign({}, this.tailingCommisionMoneyList[i]);
            temp.showTimeTd = true;
            temp.showMoneyTd = j == 0 ? true : false;
            temp.moneyRowspan = j == 0 ? this.timeList.length + 1 : 1;
            temp.timeDesc = (j == 0 ? 0 : this.timeList[j - 1]) + ' <= 天数 < ' + (j == this.timeList.length ? ' 100 ' : this.timeList[j]);
            temp.dimension2Min = j == 0 ? 0 : this.timeList[j - 1];
            temp.dimension2Max = j == this.timeList.length ? '-1' : this.timeList[j];
            this.tailingCommisionList.push(temp);
          }
        }
      }
    },
  }
}
</script>

<style scoped>
.add-btn-div {
  position: relative;
  z-index: 1;
}

.add-btn {
  background-color: #4caf50;
  border-radius: 20px;
  box-shadow: 0 4px 5px 0 rgba(76, 175, 80, 0.14), 0 1px 10px 0 rgba(76, 175, 80, 0.12), 0 2px 4px -1px rgba(76, 175, 80, 0.2);
  width: 20px;
  height: 20px;
  line-height: 20.5px;
  font-size: 23px;
  font-weight: 400;
  cursor: pointer;
  color: #FFF;
  text-align: center;
}

.tableLine {
  margin: 30px 8px 20px 6px;
  border-top: 1px dotted #C0C0C0;
  width: 900px;
  position: relative;
  text-align: center;
  font-size: 14px;
}

.midText {
  position: absolute;
  left: 50%;
  background-color: #ffffff;
  font-weight: 300;
  padding: 0 15px;
  transform: translateX(-50%) translateY(-50%);
}
</style>
