<template>
  <div>

    <k-form class="my-form" ref="performanceInfo" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-item label="产品代码">
        <k-field-text v-model="priceData.prodCode" data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="priceData.prodName" data-disabled="true"/>
      </k-form-item>
      <!--
				<k-form-item label="预期募集规模(万元)">
					<k-field-text v-model="T8ProdPerformance.expectedRaiseFund" :data-allowblank="false" data-disabled="true"/>
				</k-form-item>
				-->
      <k-form-item label="基准类型">
        <k-field-select v-model="T8ProdPerformance.baseType" data-dict="t8_base_type" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="超额业绩报酬说明">
        <k-field-text v-model="T8ProdPerformance.excessPerfExplain" :data-max-length="255" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="业绩比较基准说明">
        <k-field-text v-model="T8ProdPerformance.perfMethodExplain" :data-max-length="255" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="业绩报酬提取比例%">
        <k-field-text v-model="T8ProdPerformance.performanceOut" :data-disabled="true"
                      data-type="money" data-digits="2"/>
      </k-form-item>

      <k-form-item label="基准利率%" v-show="T8ProdPerformance.baseType == '1'">
        <k-field-text v-model="T8ProdPerformance.baseRate" :data-max-length="8"
                      data-min-value="0" data-integer-length="3" data-validate-type="money" data-max-value="100"
                      data-type="money" data-digits="2" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="自定义利率" v-show="T8ProdPerformance.baseType == '5'">
        <k-field-text v-model="T8ProdPerformance.custom" :data-max-length="8"
                      data-min-value="0" data-integer-length="3" data-validate-type="money"
                      data-type="money" data-digits="2" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="基准利率下限%" v-show="T8ProdPerformance.baseType == '2'">
        <k-field-text v-model="T8ProdPerformance.baseMinRate"  :data-max-length="8"
                      data-min-value="0"  data-integer-length="3" data-validate-type="money"
                      :data-max-value="T8ProdPerformance.baseMaxRate"
                      data-type="money" data-digits="2" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="基准利率上限%" v-show="T8ProdPerformance.baseType == '2'">
        <k-field-text v-model="T8ProdPerformance.baseMaxRate" :data-max-length="8"
                      :data-min-value="T8ProdPerformance.baseMinRate"
                      data-integer-length="3" data-validate-type="money" data-max-value="100"
                      data-type="money" data-digits="2" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="市场利率类型" v-show="T8ProdPerformance.baseType == '4'">
        <k-field-select v-model="T8ProdPerformance.marketRate" data-dict="t8_market_rate" :data-disabled="true"
        />
      </k-form-item>

    </k-form>

    <div label="分段计提展示" class="my-table" v-show="T8ProdPerformance.baseType == '2'"
         style="margin-top:15px;margin-bottom:8px;font-size:14px;text-align: center">
      <md-table>
        <md-table-row>
          <md-table-head style="text-align: center;"
                         v-show="moneyList.length > 0  && T8ProdPerformance.baseType == '2'">基准利率区间%
          </md-table-head>
          <md-table-head style="text-align: center;"
                         v-show="moneyList.length > 0  && T8ProdPerformance.baseType == '2'"> {{ feeTableHead }}
          </md-table-head>
        </md-table-row>

        <md-table-row v-show="timeList.length > 0 || moneyList.length > 0 && T8ProdPerformance.baseType == '2'"
                      v-for="(item,index) in tailingCommisionList" :key="index">
          <md-table-cell v-if="item.showMoneyTd && T8ProdPerformance.baseType == '2'" :rowspan="item.moneyRowspan"
                         style="width: 50px">
            {{ item.moneyDesc }}
          </md-table-cell>
          <md-table-cell v-if="item.showTimeTd && T8ProdPerformance.baseType == '2'" style="width: 50px">
            {{ item.timeDesc }}
          </md-table-cell>
          <md-table-cell style="width: 50px">
            <span>{{ item.rate }}%</span>
          </md-table-cell>
        </md-table-row>
      </md-table>
    </div>

    <k-form ref="addForm2" v-for="(item,index) in envItems" :key="index" v-show="T8ProdPerformance.baseType == '3' || T8ProdPerformance.baseType == '5'"
            :data-col="6" data-input-width="300px" data-label-width="170px" data-total-width="1118px">
      <k-form-item :label="'指数名称'">
        <k-field-select v-model="item.ratioIndex" data-value-field="indexCode"
                        data-display-field="indexName" data-action="T8IndexInfo.find" data-disabled="true"/>
      </k-form-item>
      <k-form-item :label="'系数%'">
        <k-field-text v-model="item.coefficient" :data-max-length="8"
                      data-min-value="0"  data-integer-length="3" data-validate-type="money"
                      data-type="money" data-digits="2" data-disabled="true"/>
      </k-form-item>
    </k-form>
    <k-form class="my-form" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px" ref="submitForm">
      <k-form-item label="成立/开放日">
        <k-field-select
          v-model="priceData.prodDate"
          :data-params="{prodCode:prodCode}"
          data-action="T8ProdWorkdays.findProdOpenDays"
          :data-allowblank="false"
          data-display-field="changeDate"
          data-value-field="changeDate"
          @data-on-change="changeProdDate"
        />
      </k-form-item>
      <k-form-item label="市场端业绩基准">
        <k-field-text v-model="priceData.marketPerformanceOut" :data-max-length="255" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="综合成本%">
        <k-field-text v-model="priceData.compositeCost" :data-max-length="8" data-digits="4" data-type="number"
                      data-validate-type="number"
                      :data-allowblank="true" data-max-value="100" data-min-value="0"/>
      </k-form-item>
      <k-form-item label="生效日期">
        <k-field-date v-model="priceData.validateDate"
                      :data-max-value="priceData.invalidateDate==''?'29991230':priceData.invalidateDate"
                      :data-allowblank="false">

        </k-field-date>
      </k-form-item>
      <k-form-item label="失效日期">
        <k-field-date v-model="priceData.invalidateDate"
                      :data-min-value="'('+(this.priceData.validateDate==''?this.priceData.prodDate:this.priceData.validateDate)"
                      :data-allowblank="false">

        </k-field-date>
      </k-form-item>
      <k-form-item label="决策类型">
        <k-field-select v-model="priceData.decisionType" data-dict="decision_type" :data-allowblank="false"
                        @data-on-change="changeDecisionType"></k-field-select>
      </k-form-item>
      <k-form-item label="会议/审批单">
        <k-field-select data-action="QuotaMeeting.findMeetByTypeAndCode"
                        :data-params="{type:priceData.decisionType,prodCode:priceData.prodCode}"
                        v-model="priceData.meetId"
                        :data-allowblank="false"
                        data-display-field="meetingName"
                        data-value-field="id"
                        ref="meetings"></k-field-select>
      </k-form-item>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdPriceConfirm.addOrUpdateT8ProdPrice"
               :data-model="priceData" data-target="priceGrid">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
        </k-btn>
      </k-form-footer>
    </k-form>
  </div>
</template>

<script>;
import Tools from "@/utils/tools";
export default {
  props: {
    prodCode: '',//产品代码
    t8ProdInfoId: '',//产品id
    prodName: '',//产品名称
    priceId: '',//业绩基准id  修改时才存在值
  },
  data() {
    return {
      envItems: [{"ratioIndex":''},{"coefficient":''}],
      userid: localStorage.getItem("userid"),
      constantRatesSitch: true,     //固定切换按钮的值
      moneyList: [],
      timeList: [],
      tailingCommisionList: [],
      tailingCommisionMoneyList: [],
      tailingCommisionTimeList: [],
      feeTableHead: "计提比例%",
      maxMoneyValue: 99999999999999.99,
      maxRateValue: 100,
      priceData: {
        prodCode: '',
      },//市场端业绩基准信息
    }
  },
  watch:{
    'priceData.decisionType'(value){
      this.$refs.meetings.load({type: value, prodCode: this.priceData.prodCode});
    }
  },
  created() {
    this.$set(this.priceData, 'prodCode', this.prodCode);
    this.$set(this.priceData, 'prodName', this.prodName);
    if (this.priceId != '' && this.priceId != undefined) { //修改功能，需要查询业绩基准信息
      this.findProdPrice();
    }
    //查询业绩报酬信息
    this.findProdPerformance();
  },
  methods: {
    //产品成立/开放日发生变化  查询出对应的开放日的开始日期与结束日期 并赋值给生效日期与失效日期
    changeProdDate() {
      this.$set(this.priceData, 'validateDate', this.priceData.prodDate);
      this.httpUtil.comnQuery({
        action: 'T8ProdWorkdays.findStartAndEndDate',
        params: {
          prodCode: this.priceData.prodCode,
          changeDate: this.priceData.prodDate
        },
      }).then(data => {
        if (data.rows.length > 0) {
          this.$set(this.priceData, 'invalidateDate', data.rows[0].changeDate);
        } else {
          this.$set(this.priceData, 'invalidateDate', '');
        }
      });
    },
    //决策类型改变事件
    changeDecisionType() {
      this.priceData.meetId = '';
      //this.$refs.meetings.load({type: this.priceData.decisionType, prodCode: this.priceData.prodCode});
    },
    //查询业绩基准信息
    findProdPrice() {
      this.priceData = {};
      this.httpUtil.comnQuery({
        action: 'T8ProdPrice.findProdPriceById',
        params: {
          id: this.priceId
        },
      }).then(data => {
        if (data.rows.length > 0) {
          this.priceData = data.rows[0];
          this.$set(this.priceData, 'prodName', this.prodName);
        }
      });
    },
    //查询产品创设业绩报酬信息
    findProdPerformance(){
      //查询业绩基准信息
      this.T8ProdPerformance = {};
      this.httpUtil.comnQuery({
        action: 'T8ProdPerformance.findT8ProdPerformances',
        params: {
          prodCode : this.prodCode,
          t8ProdInfoId : this.t8ProdInfoId,
        }
      }).then(data => {
        if(data.rows.length > 0 ){
          this.T8ProdPerformance = data.rows[0];
          //查询指数信息
          this.findPerformanceRation(this.T8ProdPerformance.id);
          //查询分段信息
          this.findPerformancePrjFeeList();
        }
      });
    },
    //查询指数信息
    findPerformanceRation(t8ProdPerformanceId){
      this.httpUtil.comnQuery({
        action: 'T8ProdPerformanceRatio.findT8ProdPerformanceRatio',
        params: {
          t8ProdPerformanceId : t8ProdPerformanceId,
        }
      }).then(data => {
        if(data.rows.length > 0 ){
          this.envItems = data.rows;
        }
      });
    },
    //查询分段信息
    findPerformancePrjFeeList(){
      this.httpUtil.comnQuery({
        action: 'T8PrjFeeList.findPerformanceT8PrjFeeLists',
        params: {
          feeCode : this.t8ProdInfoId,
        }
      }).then(data => {
        var moneyList2 = [];
        if(data.rows.length > 0 ){
          var array = [];
          for(let j=0;j<data.rows.length;j++){
            array.push(data.rows[j]);
            if(j<data.rows.length-1){
              moneyList2.push(parseInt(data.rows[j].dimension2Max));
            }
          }
        }
        this.moneyList = moneyList2;
        this.tailingCommisionList= array;
        this.tailingCommisionMoneyList= array;
      });
    },
  },
}
</script>

<style>

</style>

