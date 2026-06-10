<template>
  <div>
    <k-form-search-customize data-target="priceGrid" v-model="queryParam">

      <k-form-item label="产品代码">
        <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"/>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="成立日">
        <k-field-date v-model="prodSearchParam.startEstablishDate" data-type="daterange" :data-max-value="prodSearchParam.endEstablishDate===''?'29991230':prodSearchParam.endEstablishDate"/>
      </k-form-item>
      <k-form-item label="生效日期">
        <k-field-date v-model="prodSearchParam.validateDate" data-type="daterange"/>
      </k-form-item>
      <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" data-target="addProdPrice" :data-handler="initFormData"
             v-if="global.isShowAuthorityButton('T8ProdPriceConfirm.addOrUpdateT8ProdPriceCoonfirm')">
        <md-icon md-src="/static/svg/add.svg" />新增
      </k-btn>
    </k-form-search-customize>

    <k-grid ref="priceGrid" data-action="T8ProdPrice.findProdPriceInfoByProdCode" :data-autoload="false"
            @data-row-select="selectPriceRow">
      <k-grid-column data-align="center" data-header="id" data-name="id" hidden="true"/>
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="基准类型" data-dict="t8_base_type" data-name="baseType" hidden="true"/>
      <k-grid-column data-align="center" data-header="成立/开放日" data-name="prodDate"/>
      <k-grid-column data-align="center" data-header="市场端业绩基准" data-name="marketPerformanceOut"/>
      <k-grid-column data-align="center" data-header="综合成本" data-name="compositeCost"/>
      <k-grid-column data-align="center" data-header="生效日期" data-name="validateDate" data-type="date"/>
      <k-grid-column data-align="center" data-header="失效日期" data-name="invalidateDate" data-type="date"/>
      <k-grid-column data-align="center" data-header="创建人" data-name="createUserName"/>
      <k-grid-column data-align="center" data-header="创建日期" data-name="createDate" data-type="date"/>
      <k-grid-column data-align="center" data-header="创建时间" data-name="createTime" data-type="time"/>
      <k-grid-column data-align="center" data-header="状态" data-name="confirmStatus" data-dict="confirm_status"/>
      <k-grid-column data-align="center" data-header="决策类型" data-name="decisionType" data-dict="decision_type"/>
      <k-grid-column data-align="center" data-header="决策名称" data-name="meetingName"/>
      <template slot="operate" slot-scope="scope">
        <k-btn data-functype="POPUP" data-size="mini" data-target="addPopup"
               v-if="global.isShowAuthorityButton('T8ProdPriceConfirm.addOrUpdateT8ProdPrice')"
               class="md-info md-just-icon md-simple" :data-handler="getProdPriceInfo"
               data-descript="确认业绩基准" :data-disabled="scope.row.row.confirmStatus=='2'">
          <md-icon>check</md-icon>
        </k-btn>
      </template>
    </k-grid>




    <k-popup ref="addProdPrice" data-title="新增" >
      <k-form class="my-form" ref="performanceInfo" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">
        <k-form-item label="产品代码">
          <k-field-select v-model="priceData.prodCode" data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"
                          @data-on-change="getProdNameByCode(priceData.prodCode)" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="priceData.prodName" :data-disabled="true" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="基准类型">
          <k-field-select v-model="T8ProdPerformance.baseType" data-dict="t8_base_type" :data-disabled="true"></k-field-select>
        </k-form-item>
        <k-form-item label="超额业绩报酬说明">
          <k-field-text v-model="T8ProdPerformance.excessPerfExplain" :data-max-length="255" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="业绩比较基准说明">
          <k-field-text v-model="T8ProdPerformance.perfMethodExplain" :data-max-length="255" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="业绩报酬提取比例%">
          <k-field-text v-model="T8ProdPerformance.performanceOut" data-type="money" data-digits="2" :data-disabled="true"/>
        </k-form-item>

        <k-form-item label="基准利率%" v-show="T8ProdPerformance.baseType == '1'">
          <k-field-text v-model="T8ProdPerformance.baseRate" :data-disabled="true"
                        data-type="money" data-digits="2"/>
        </k-form-item>
        <k-form-item label="自定义利率" v-show="T8ProdPerformance.baseType == '5'">
          <k-field-text v-model="T8ProdPerformance.custom"
                        data-type="money" data-digits="2" :data-disabled="true"/>
        </k-form-item>

        <k-form-item label="基准利率下限%" v-show="T8ProdPerformance.baseType == '2'">
          <k-field-text v-model="T8ProdPerformance.baseMinRate"  :data-disabled="true"
                        data-type="money" data-digits="2"/>
        </k-form-item>
        <k-form-item label="基准利率上限%" v-show="T8ProdPerformance.baseType == '2'">
          <k-field-text v-model="T8ProdPerformance.baseMaxRate" :data-disabled="true"
                        data-type="money" data-digits="2"/>
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
          <k-field-select v-model="item.ratioIndex" data-value-field="indexCode" :data-disabled="true"
                          data-display-field="indexName" data-action="T8IndexInfo.find" />
        </k-form-item>
        <k-form-item :label="'系数%'">
          <k-field-text v-model="item.coefficient" :data-disabled="true" :data-max-length="8"
                        data-min-value="0"  data-integer-length="3" data-validate-type="money"
                        data-type="money" data-digits="2" />
        </k-form-item>
      </k-form>

      <k-form class="my-form" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px" ref="submitForm">
        <k-form-item label="成立/开放日">
          <k-field-select
            v-model="priceData.prodDate"
            :data-data="prodDateList"
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
          <k-field-date v-model="priceData.validateDate"@data-on-change="change"
                        :data-min-value= "priceData.prodDate == '' || priceData.prodDate ==null?'19701010':priceData.prodDate"
                        :data-max-value="priceData.invalidateDate=='' || priceData.invalidateDate ==null?'29991230':priceData.invalidateDate"
                        :data-allowblank="false">
          </k-field-date>
        </k-form-item>
        <k-form-item label="失效日期">
          <k-field-date v-model="priceData.invalidateDate"
                        :data-min-value="this.priceData.validateDate=='' || this.priceData.validateDate== null?this.priceData.prodDate:this.priceData.validateDate"
                        :data-allowblank="false">
          </k-field-date>
        </k-form-item>

        <k-form-item label="决策类型">
          <k-field-select v-model="priceData.decisionType" data-dict="decision_type"
                          @data-on-change="changeDecisionType(priceData.decisionType)" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="决策名称">
          <k-field-select data-action="QuotaMeeting.findMeetByTypeAndCode"
                          :data-params="{type:priceData.decisionType,prodCode:priceData.prodCode}"
                          v-model="priceData.meetId"
                          data-display-field="meetingName"
                          data-value-field="id"
                          ref="meetings" :data-allowblank="false"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                 :data-model="priceData" :data-handler="beforeSubmit" data-target="priceGrid" ref="addBtn">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>



    <k-popup ref="addPopup" data-title="确认" >
      <k-form class="my-form" ref="performanceInfo" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">

          <k-form-item label="产品代码">
            <k-field-text v-model="priceData.prodCode" data-disabled="true"/>
          </k-form-item>
          <k-form-item label="产品名称">
            <k-field-text v-model="priceData.prodName" data-disabled="true"/>
          </k-form-item>
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
              :data-data="prodDateList"
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

    </k-popup>

  </div>
</template>

<script>
import AddComp from "./ProdPerformanceConfirm"
import {assign} from "lodash";
import ProdPerformance from "../M81/prodInfoGD/M81001-ProdPerformance"
import Tools from "@/utils/tools";

export default {
  name: "",
  components: {ProdPerformance,AddComp},
  data() {
    return {
      prodSearchParam: {
        prodCode: '',
        startEstablishDate: '',
        endEstablishDate: '',
        offerDate:''
      },
      T8ProdPerformance:{},
      minDate: '19701001',//最小日期
      prodCode: '',//产品代码
      t8ProdInfoId: '',//产品id
      prodName: '',//产品名称
      prodDateList:'',
      priceId: '',
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
    created() {
      this.global.getProdUser('');
      this.$nextTick(()=>{
        let prodCode = this.$route.query.prod_code;
        //console.log(prodCode)
        if(prodCode !=''&&prodCode!=undefined){
          this.$refs.priceGrid.load({prodCode:prodCode});
        }
      });
    },
  watch:{
    'prodSearchParam.startEstablishDate'(value){
      if(value==''){
        this.minDate ='19701001';
      }else{
        this.minDate = value;
      }
    },
    // 'priceData.decisionType'(value){
    //   this.$refs.meetings.load({type: value, prodCode: this.priceData.prodCode});
    //   this.$set(this.priceData,'meetId','');
    // },
      'priceData.prodDate'(value){
      if(value =null || value == ''){
        this.$refs.submitForm.reset();
        // this.$set(this.T8ProdPerformance,'baseType','');
      }
      }
  },
  methods: {
    change() {
      if(this.priceData.validateDate != null && this.priceData.validateDate != '' &&
        this.priceData.prodDate != null && this.priceData.prodDate != '' &&
        this.priceData.validateDate<this.priceData.prodDate){
        Tools.alert("优惠生效日期必须大于成立日期！")
        this.$set(this.priceData,'validateDate','');
        return false ;
      } else {
        if(this.priceData.validateDate != null && this.priceData.validateDate != '' &&
          this.priceData.invalidateDate != null && this.priceData.invalidateDate != '' &&
          this.priceData.validateDate>this.priceData.invalidateDate){
          Tools.alert("优惠生效日期必须小于优惠失效日期！")
          this.$set(this.priceData,'validateDate','');
          return false ;
        }
      }
    },
    initFormData(){
      this.priceData = {};
      this.T8ProdPerformance = {};
    },
    getProdNameByCode(value){
      //判断产品代码为空，则全置空
      if(this.priceData.prodCode == null || this.priceData.prodCode == ''){
        this.$refs.performanceInfo.reset();
        this.$refs.submitForm.reset();
      } else {
        //通过产品代码查询产品及其信息
        this.httpUtil.comnQuery({
          action: "T8ProdInfo.getProdNameByProdCode",
          params: {prodCode: this.priceData.prodCode}
        }).then(data => {
          if(data.rows.length>0){
            //给其他输入框赋值
            //console.log(this.priceData.prodCode);
            this.$set(this.T8ProdPerformance,"baseType",data.rows[0].baseType);
            this.$set(this.priceData,"prodName",data.rows[0].prodName);
          }
        }).catch({})

        //通过产品代码与类型查询产品所有开放日期信息
        this.httpUtil.comnQuery({
          action: "T8ProdWorkdays.findProdOpenDays",
          params: {prodCode: this.priceData.prodCode}
        }).then(data => {
          this.prodDateList = data.rows;
        }).catch({})

        //查询业绩报酬信息
        this.findProdPerformance();
      }
    },
    getProdPriceInfo(value){
      this.$set(this.priceData,'prodCode','');
      //.log(this.priceData.prodCode)
      this.prodCode = value.prodCode;
      this.priceId = value.id;
      this.httpUtil.comnQuery({
        action: 'T8ProdPrice.getProdInfoId',
        params:{prodCode: value.prodCode}
      }).then(data => {
        if(data.rows.length>0){
          this.t8ProdInfoId = data.rows[0].id;
          //查询业绩报酬信息
          this.findProdPerformance();
        }
      });
    },
    //二级查询被选中
    selectPriceRow(row) {
      const _this = this;
      _this.priceData = assign({}, row);
      //console.log(_this.priceData)
    },
    beforeSubmit(value){
      if(this.$refs.performanceInfo.validate() === true & this.$refs.submitForm.validate() === true ){
        this.httpUtil.comnQuery({
          action: "T8ProdPrice.check",
          params: {prodCode: this.priceData.prodCode,
            prodDate: this.priceData.prodDate},
          successAlert: false
        }).then(data => {
          //console.log(data)
          if(data.rows.length>0){
            Tools.alert("该产品已存在对应日期业绩基准,请选择其他日期业绩基准！");
            this.$refs.submitForm.reset();
            this.$refs.addBtn.loading = false;
            return false;
          } else {
            this.httpUtil.comnUpdate({
              action: "T8ProdPrice.addOrUpdateT8ProdPrice",
              params: this.priceData,
            }).then(data => {
              this.priceData = {};
              this.$refs.addProdPrice.close();
              this.$refs.priceGrid.load();
              return true;
            });
          }
        });
      } else {
        return false;
      }
    },
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
    changeDecisionType(value) {
      if(value ==null || value == ''){
        this.$set(this.priceData,'meetId','');
      }
      this.$refs.meetings.load({type: this.priceData.decisionType, prodCode: this.priceData.prodCode});
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
  computed: {
    queryParam() {
      return {
        'prodName': this.prodSearchParam.prodName,//产品名称
        'prodCode': this.prodSearchParam.prodCode,//产品代码
        'startEstablishDateMin': this.prodSearchParam.startEstablishDate ? this.prodSearchParam.startEstablishDate[0]:null,//成立起始日最小值
        'startEstablishDateMax': this.prodSearchParam.startEstablishDate ? this.prodSearchParam.startEstablishDate[1]:null,//成立起始日最大值
        'validateDateMin': this.prodSearchParam.validateDate ? this.prodSearchParam.validateDate[0]:null,//生效日最小值
        'validateDateMax': this.prodSearchParam.validateDate ? this.prodSearchParam.validateDate[1]:null,//生效日最大值
      }
    }
  },
};
</script>

<style lang="scss" scoped>
.my-table {
  margin-top: 15px;
  margin-left:50px;
}
::v-deep #moneyShowDiv .md-table-head {
  text-align: center;
}

::v-deep #moneyShowDiv .md-input {
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

::v-deep input[type="number"] {
  -moz-appearance: textfield;
  width: 10px;
}
::v-deep .el-dialog {
  margin-right: 10%;
}

</style>
