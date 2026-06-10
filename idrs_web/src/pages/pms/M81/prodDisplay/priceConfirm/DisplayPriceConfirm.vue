<template>
  <div>

    <k-form class="my-form" ref="performanceInfo" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-item label="产品代码">
        <k-field-text v-model="priceData.prodCode"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="priceData.prodName"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品成立日">
        <k-field-text v-model="priceData.prodDate" data-type="date" :data-disabled="true"/>
      </k-form-item>



      <k-form-item label="基准类型">
        <k-field-select v-model="T8ProdPerformance.baseType" data-dict="t8_base_type"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="超额业绩报酬说明">
        <k-field-text v-model="T8ProdPerformance.excessPerfExplain"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="业绩比较基准说明">
        <k-field-text v-model="T8ProdPerformance.perfMethodExplain"   :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="业绩报酬提取比例%">
        <k-field-text v-model="T8ProdPerformance.performanceOut" :data-disabled="true"
                      data-type="money" data-digits="2"/>
      </k-form-item>

      <k-form-item label="基准利率" v-show="T8ProdPerformance.baseType == '1'">
        <k-field-text v-model="T8ProdPerformance.baseRate" :data-max-length="8" :data-disabled="true"
                         data-min-value="0"  data-integer-length="3" data-validate-type="money" data-max-value="100"
                         data-type="money" data-digits="2" />
      </k-form-item>
      <k-form-item label="自定义利率" v-show="T8ProdPerformance.baseType == '5'">
        <k-field-text v-model="T8ProdPerformance.custom" :data-max-length="8"
                         data-min-value="0"  data-integer-length="3" data-validate-type="money"
                         data-type="money" data-digits="2" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="基准利率下限%" v-show="T8ProdPerformance.baseType == '2'">
        <k-field-text v-model="T8ProdPerformance.baseMinRate"  :data-max-length="8" :data-disabled="true"
                         data-min-value="0"  data-integer-length="3" data-validate-type="money"
                         :data-max-value="T8ProdPerformance.baseMaxRate"
                         data-type="money" data-digits="2" />
      </k-form-item>
      <k-form-item label="基准利率上限%" v-show="T8ProdPerformance.baseType == '2'">
        <k-field-text v-model="T8ProdPerformance.baseMaxRate" :data-max-length="8"
                         :data-min-value="T8ProdPerformance.baseMinRate" :data-disabled="true"
                         data-integer-length="3" data-validate-type="money" data-max-value="100"
                         data-type="money" data-digits="2"  />
      </k-form-item>
      <k-form-item label="市场利率类型" v-show="T8ProdPerformance.baseType == '4'">
        <k-field-select v-model="T8ProdPerformance.marketRate" data-dict="t8_market_rate"  :data-disabled="true"  />
      </k-form-item>

    </k-form>

    <div label="分段计提展示"  class="my-table" v-show="T8ProdPerformance.baseType == '2'">
      <md-table style="width: 85%;margin-left:95px;">
        <md-table-row>
          <md-table-head v-show="moneyList.length > 0  && T8ProdPerformance.baseType == '2'" >基准利率区间%</md-table-head>
          <md-table-head v-show="moneyList.length > 0  && T8ProdPerformance.baseType == '2'"> {{feeTableHead}}</md-table-head>
        </md-table-row>

        <md-table-row   v-show="timeList.length > 0 || moneyList.length > 0 && T8ProdPerformance.baseType == '2'" style="color:#aaa"
                        v-for="(item,index) in tailingCommisionList" :key="index" >
          <md-table-cell v-show="item.showMoneyTd && T8ProdPerformance.baseType == '2'" :rowspan="item.moneyRowspan" padding-left="10" >
            {{ item.moneyDesc }}
          </md-table-cell>
          <md-table-cell v-show="item.showTimeTd && T8ProdPerformance.baseType == '2'">
            {{ item.timeDesc }}
          </md-table-cell>
          <md-table-cell>
            <md-field>
              <md-input class="md-input" style="width: 20px; text-align: left;" v-model="item.rate" md-input-type="number" ></md-input>
              <span class="md-suffix" >%</span>
            </md-field>
          </md-table-cell>
        </md-table-row>
      </md-table>
    </div>




    <k-form ref="addForm2" v-for="(item,index) in envItems" :key="index"
            v-show="T8ProdPerformance.baseType == '3' || T8ProdPerformance.baseType == '5'"
            :data-col="6" data-input-width="300px" data-label-width="170px" data-total-width="1118px">
      <k-form-item :label="'指数名称'">
        <k-field-select v-model="item.ratioIndex" data-value-field="indexCode" :data-disabled="true"
                         data-display-field="indexName" data-action="T8IndexInfo.find" />
      </k-form-item>
      <k-form-item :label="'系数%'">
        <k-field-text v-model="item.coefficient" :data-max-length="8"
                         data-min-value="0"  data-integer-length="3" data-validate-type="money"
                         data-type="money" data-digits="2" :data-disabled="true"/>
      </k-form-item>
    </k-form>
    <k-form class="my-form" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px" ref="submitForm">
      <k-form-item label="决策类型" v-if="prodStatus<=6 && priceData.priceDataMenu == 'confirm'" >
        <k-field-select v-model="feeMeetingType" data-dict="decision_type" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="会议/审批单" v-if="prodStatus<=6 && priceData.priceDataMenu == 'confirm'">
        <k-field-text v-model="feeMeetingName"   :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="决策类型" v-if="prodStatus>6 && priceData.priceDataMenu == 'confirm'">
        <k-field-select v-model="priceData.decisionType"  data-dict="decision_type" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="会议/审批单" v-if="prodStatus>6 && priceData.priceDataMenu == 'confirm'">
        <k-field-select v-model="priceData.meetingId"  data-action="MeetCreate.findMeetDict" data-display-field="meetName"
                         data-value-field="id" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="市场端业绩基准">
        <k-field-text v-model="priceData.marketPerformanceOut"   :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="综合成本%">
        <k-field-text v-model="priceData.compositeCost" :data-max-length="8" data-digits="4" data-type="number"  data-validate-type="number"
                         data-max-value="100" data-min-value="0" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="生效日期">
        <k-field-date v-model="priceData.validateDate" data-type="date" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="失效日期">
        <k-field-date v-model="priceData.invalidateDate" data-type="date" :data-disabled="true"/>
      </k-form-item>
    </k-form>
  </div>
</template>

<script>
  export default {
    name: "DisplayPriceConfirm",
    props: {
      priceData :{},
    },
    data() {
      return {
        T8ProdPerformance:{},
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
        currentWorkday: null,
        prodCode:'',//产品代码
        t8ProdInfoId:'',//产品id
        prodStatus:0,//产品状态
        feeMeetingName:'',//会议名称
        feeMeetingType:'',//决策类型
      }
    },
    created() {

    },
    methods: {
      //成立及成立前反显额度决策信息
      findQuotaMeeting(prodCode){
        this.httpUtil.comnQuery({
          action: 'QuotaMeeting.findMeetingByProdCode',
          params: {
            prodCode : prodCode,
          }
        }).then(data => {
          let meeting = data.rows[0];
          this.feeMeetingName= meeting.meetingName;
          this.feeMeetingType = meeting.type;
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
          if(data.rows.length > 0 ){
            let array = [];
            let moneyList2 = [];
            for(let j=0;j<data.rows.length;j++){
              array.push(data.rows[j]);
              if(j<data.rows.length-1){
                moneyList2.push(parseInt(data.rows[j].dimension2Max));
              }
            }
            this.moneyList = moneyList2;
            this.tailingCommisionList= array;
            this.tailingCommisionMoneyList= array;
          }
        });
      },
    },

    watch:{
      priceData(val){
        this.prodCode = val.prodCode;
        this.t8ProdInfoId = val.t8ProdInfoId;
        this.prodStatus = val.prodStatus;
        if(this.prodCode== null ||this.prodCode==""){
          return;
        }
        //查询业绩报酬信息
        this.findProdPerformance();
        if(this.prodStatus<=6 && this.priceData.priceDataMenu == 'confirm'){
          //查询额度决策会信息
          this.findQuotaMeeting(this.prodCode);
        }
      }
    }
  }
</script>

<style scoped>

</style>
