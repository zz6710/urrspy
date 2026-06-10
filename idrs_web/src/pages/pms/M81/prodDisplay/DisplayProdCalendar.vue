<template>
  <div v-loading="loadCalendar">

    <!--    <div class="pc-main-container">-->
    <!--      <div class="pc-calendar">-->
    <div style="margin-top: -35px;
                    width: 100%;
                    justify-content: center;
                    display: flex;
                    align-items: center;
                    color: #ff003b;
                    font-size: 13px;"> {{validateResult}}</div>

    <k-field-text v-show="false" v-model="T8ProdCalendar.establishDate"></k-field-text>
    <k-field-text v-show="false" v-model="T8ProdCalendar.endDate"></k-field-text>
    <k-field-text v-show="false" v-model="prodCode"></k-field-text>


    <div class="md-layout">
      <div class="md-layout-item"
           style="margin-top: 14px; margin-bottom: 5px; display: grid; width: 100%; align-items: center;">

        <div ref="periodPanel" v-show="prodMode == '3'" v-for="(openItem, openItemIndex) in openRuleItems" v-bind:key="openItemIndex"
             class="fc-button-group" style="float:left; padding: 15px 0px 0px 8px;">

          <!-- 每NXYZ，遇节假日“D” -->
          <!-- N是数字 -->
          <!-- X下拉单选：天、周、月 -->
          <!-- Y是下拉多选：X是“天”就置灰，X是“周”为一到日，X是“月”为1-28 -->


          <span style="color: red;padding-top: 7px">*</span><span style="padding-top: 7px" >每</span>
          <!-- N -->
          <k-field-text v-model="T8ProdCalendar.cycleOpenTerm" class="md-padding-left-10" @data-on-change="resetOpenDate"
                        style="width: 65px;" :data-disabled="true"/>

          <!-- X -->
          <span style="color: red;padding-top: 7px">*</span>
          <k-field-select v-model="T8ProdCalendar.cycleOpenType" :data-data="openRuleOptions"
                          class="md-padding-left-10"
                          @data-on-change="changedPeriod(openItem)"
                          style="width: 70px;" :data-disabled="true"/>

          <!-- Y 最好采用这种选择模式，否则容易出现问题，删除第一行之后会保留第一行的字典-->
<!--          <k-field-text v-show="T8ProdCalendar.cycleOpenType == '1'" v-model="T8ProdCalendar.orderOpenDays" @data-on-change="resetOpenDate"-->
<!--                          :data-disabled="openItem.disabled" class="md-padding-left-10"-->
<!--                          style="width: 90px;"></k-field-text>-->
          <k-field-select v-show="T8ProdCalendar.cycleOpenType == '2'" v-model="T8ProdCalendar.orderOpenDays" :data-data="openWeeklyOptions" @data-on-change="resetOpenDate"
                          class="md-padding-left-10"
                          style="width: 90px;" :data-disabled="true"/>
          <k-field-select v-show="T8ProdCalendar.cycleOpenType == '3'" v-model="T8ProdCalendar.orderOpenDays" :data-data="openMonthlyOptions" @data-on-change="resetOpenDate"
                          class="md-padding-left-10"
                          style="width: 90px;" :data-disabled="true"/>

          <span style="color: red;padding-top: 7px">*</span>
          <k-field-select v-model="openItem.cycleOpenType" v-show="false" data-disabled="true" :data-data="openTypeOptions"
                          :data-default-value="'1'"  class="md-padding-left-10" style="width: 90px;" :data-disabled="true"/>

          <span style="padding-top: 7px" >开放，遇节假日</span><span style="color: red;padding-top: 7px">*</span>

          <k-field-select v-model="T8ProdCalendar.postponeRule" :data-data="weekendRuleOptions" :data-default-value="'1'" class="md-padding-left-10"
                          style="width: 160px;" :data-disabled="true"/>
          <k-field-select v-model="openItem.weekendRule" v-show="false" :data-data="weekendRuleOptions" @data-on-change="resetOpenDate"
                          class="md-padding-left-10"
                          style="width: 160px;" :data-disabled="true"/>



        </div>

      </div>
    </div>
    <k-form  ref="periodInfo" :data-col="2"  dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-item label="产品代码"   v-show="false">
        <k-field-text v-model="T8ProdCalendar.prodCode"  :data-allowblank="false" :data-default-value="this.prodCode"/>
      </k-form-item>
      <k-form-item label="产品主表id"  v-show="false" >
        <k-field-text v-model="T8ProdCalendar.t8ProdInfoId"  :data-allowblank="false" />
      </k-form-item>
      <k-form-item label="产品工作日方案">
        <k-field-select v-model="T8ProdCalendar.pgmno" :data-allowblank="false"
                        data-action="WorkdayProgram.find" :data-disabled="true"
                        data-display-field="pgmno,pgmname" data-value-field="pgmno" />
      </k-form-item>

      <k-form-item label="认购起始日">
        <k-field-date v-model="T8ProdCalendar.applyStartDate" data-date-format="yyyy-MM-dd"  :dataWorkday="true"
                      :dataWorkdayPgmno="this.T8ProdCalendar.pgmno" :dataMinValue="today" :data-disabled="true"
                      :dataAllowblank="false" :key="loadDate" />
      </k-form-item>
      <k-form-item label="认购结束日">
        <k-field-date v-model="T8ProdCalendar.applyEndDate" data-date-format="yyyy-MM-dd"  :dataWorkday="true"
                      :dataWorkdayPgmno="this.T8ProdCalendar.pgmno" :data-disabled="true"
                      :dataAllowblank="false" :key="loadDate" />
      </k-form-item>

      <k-form-item label="认购结束时间">
        <k-field-time v-model="T8ProdCalendar.applyEndTime" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="成立日">
        <k-field-date v-model="T8ProdCalendar.establishDate" data-date-format="yyyy-MM-dd"  :dataWorkday="true"
                      :data-on-change="getDateRegion(T8ProdCalendar.endDate,T8ProdCalendar.establishDate)"
                      :dataWorkdayPgmno="this.T8ProdCalendar.pgmno" :data-disabled="true"
                      :dataAllowblank="false" :key="loadDate" />
      </k-form-item>
      <k-form-item label="首次开放日" v-show="this.prodMode=='1'?false:true">
        <k-field-date v-model="T8ProdCalendar.openStartDate" data-date-format="yyyy-MM-dd" :dataWorkday="true"
                      :dataWorkdayPgmno="this.T8ProdCalendar.pgmno" :data-disabled="true"
                      :dataAllowblank="this.prodMode=='1'?true:false" :key="loadDate" />
      </k-form-item>
      <k-form-item label="开放结束日" v-if="this.prodMode=='1'?false:true" v-show="false">
        <k-field-date v-model="T8ProdCalendar.openEndDate" data-date-format="yyyy-MM-dd" :dataWorkday="false"
                      :dataWorkdayPgmno="this.T8ProdCalendar.pgmno" :data-disabled="true"
                      :dataAllowblank="this.prodMode=='1'?true:false" :key="loadDate" />
      </k-form-item>
      <k-form-item label="申赎开始时间" v-show="this.prodMode != '1'">
        <k-field-time v-model="T8ProdCalendar.redemStartTime" :data-allowblank="this.prodMode == '1'" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="申赎结束时间" v-show="this.prodMode != '1'">
        <k-field-time v-model="T8ProdCalendar.redemEndTime" :data-allowblank="this.prodMode == '1'" :data-disabled="true" />
      </k-form-item>

      <k-form-item label="到期日">
        <k-field-date v-model="T8ProdCalendar.endDate" data-date-format="yyyy-MM-dd"
                      :data-on-change="getDateRegion(T8ProdCalendar.endDate,T8ProdCalendar.establishDate)"
                      :dataWorkdayPgmno="this.T8ProdCalendar.pgmno" :data-disabled="true"
                      :dataAllowblank="false" :key="loadDate" />
      </k-form-item>
      <k-form-item label="清盘日">
        <k-field-date v-model="T8ProdCalendar.liquidate" data-date-format="yyyy-MM-dd"
                      :dataWorkdayPgmno="this.T8ProdCalendar.pgmno" :data-disabled="true"
                      :dataMinValue="T8ProdCalendar.endDate?T8ProdCalendar.endDate:T8ProdCalendar.establishDate"
                      :key="loadDate" ></k-field-date>
      </k-form-item>
      <k-form-item label="产品期限(天)">
        <k-field-text v-model="T8ProdCalendar.productTerm" :data-allowblank="false"  :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="认购确认日">
        <k-field-select v-model="T8ProdCalendar.subscripDate" :data-allowblank="false" :data-disabled="true" data-dict="t8_prod_tn"/>
      </k-form-item>
      <k-form-item label="申赎确认日" v-show="this.prodMode != '1'">
        <k-field-select v-model="T8ProdCalendar.redempDate" :data-allowblank="this.prodMode == '1'" :data-disabled="true" data-dict="t8_prod_tn"/>
      </k-form-item>
      <k-form-item label="资金到账日">
        <k-field-select v-model="T8ProdCalendar.redeemArrivalDate" :data-allowblank="this.prodMode == '1'" :data-disabled="true" data-dict="t8_prod_tn"/>
      </k-form-item>
      <k-form-item label="认购计息属性" >
        <k-field-select v-model="T8ProdCalendar.subsInterestType" :data-disabled="true" data-dict="t8_prod_interest_type"/>
      </k-form-item>
      <k-form-item label="认购计息年天数">
        <k-field-select v-model="T8ProdCalendar.subsInterestDays" :data-disabled="true" data-dict="t8_charging_index" />
      </k-form-item>
      <k-form-item label="产品终止清算速度">
        <k-field-select v-model="T8ProdCalendar.liquidateType" :data-disabled="true" :data-allowblank="false" data-dict="t8_prod_tn"/>
      </k-form-item>

      <k-form-item label="是否有封闭期投资日" v-show="this.prodMode != '1'">
        <k-field-select v-model="T8ProdCalendar.haveClosePeriod" data-dict="t8_prod_isok" :data-disabled="true" :data-allowblank="this.prodMode == '1'" />
      </k-form-item>
      <k-form-item label="封闭期投资期限时间" v-show="prodMode != '1' && T8ProdCalendar.haveClosePeriod=='1'">
        <k-field-text v-model="T8ProdCalendar.closePeriodInvest" :data-allowblank="this.prodMode == '1' || T8ProdCalendar.haveClosePeriod!='1'"
                      data-validate-type="code" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="封闭期投资期限单位" v-show="prodMode != '1' && T8ProdCalendar.haveClosePeriod=='1'">
        <k-field-select v-model="T8ProdCalendar.closePeriodInvestType" :data-allowblank="this.prodMode == '1' || T8ProdCalendar.haveClosePeriod!='1'"
                        data-dict="t8_term_unit" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="是否有最低持有期日" v-if="this.prodMode != '1'">
        <k-field-select v-model="T8ProdCalendar.haveMinHoldingPeriodTime" data-dict="t8_prod_isok" :data-disabled="true" :data-allowblank="this.prodMode == '1'" />
      </k-form-item>
      <k-form-item label="最低持有期时间" v-show="prodMode != '1' && T8ProdCalendar.haveMinHoldingPeriodTime=='1'">
        <k-field-text v-model="T8ProdCalendar.minHoldingPeriodTime" :data-allowblank="this.prodMode == '1' || T8ProdCalendar.haveMinHoldingPeriodTime!='1'"
                      data-validate-type="code" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="最低持有期时间单位" v-show="prodMode != '1' && T8ProdCalendar.haveMinHoldingPeriodTime=='1'">
        <k-field-select v-model="T8ProdCalendar.minHoldingPeriodTimeCompany" :data-allowblank="this.prodMode == '1' || T8ProdCalendar.haveMinHoldingPeriodTime!='1'"
                        data-dict="t8_term_unit" :data-disabled="true" />
      </k-form-item>

      <k-form-item label="预约申购天数" v-show="this.prodMode == '3'">
        <k-field-text v-model="T8ProdCalendar.openPeriodDays"
                      :data-max-length="3"  data-regx="^[0-9]*$" data-regx-text="请输入整数" :data-default-value="1" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="提前终止份额(份)">
        <k-field-text v-model="T8ProdCalendar.shareStop" data-type="number"  data-validate-type="number"
                      data-digits="2" :data-integer-length="14" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="提前终止净值(元)">
        <k-field-text v-model="T8ProdCalendar.navStop" data-type="number"  data-validate-type="number"
                      data-digits="2" :data-integer-length="14" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="募集期间资金及利息的处理方式"  :data-col="2">
        <k-field-text v-model="T8ProdCalendar.raisingPeriodType" inputType="textarea" :rows="5" :data-disabled="true" :data-max-length="2000" />
      </k-form-item>
      <k-form-item label="期初观察日" v-show="this.prodMode != '1'"  :data-col="2">
        <k-field-text v-model="T8ProdCalendar.startPeriodObsDate" inputType="textarea" :rows="1" :data-disabled="true" :data-max-length="2000" />
      </k-form-item>
      <k-form-item label="期末观察日" v-show="this.prodMode != '1'"  :data-col="2">
        <k-field-text v-model="T8ProdCalendar.endPeriodObsDate" inputType="textarea" :rows="1" :data-disabled="true" :data-max-length="2000" />
      </k-form-item>
      <k-form-item label="封闭期说明" v-show="this.prodMode != '1'" :data-col="2">
        <k-field-text v-model="T8ProdCalendar.closePeriodDesc"  inputType="textarea" :rows="5" :data-disabled="true" :data-max-length="2000"/>
      </k-form-item>
      <k-form-item label="开放日说明" v-show="this.prodMode != '1'" :data-col="2">
        <k-field-text v-model="T8ProdCalendar.periodDesc" inputType="textarea" :rows="5" :data-disabled="true" :data-max-length="2000"/>
      </k-form-item>
      <k-form-item label="提前终止说明" :data-col="2">
        <k-field-text v-model="T8ProdCalendar.stopDesc"  inputType="textarea" :rows="5" :data-disabled="true" :data-max-length="2000"/>
      </k-form-item>
    </k-form>




  </div>
</template>

<script>

  import moment from 'moment';

  export default {
    computed: {},
    model: {
      prop: 'T8ProdCalendar',
      event: 'input'
    },
    props:{
      T8ProdCalendar: {
        cycleOpenTerm:'',// 每几个
        cycleOpenType:'',//开放频率类型 1-天，2-周，3-月
        orderOpenDays:'',//开放时间 哪一天/周几  开放

        openPeriodDays:'',//开放时长 开放时长
        postponeRule:'',//顺延规则
      },
      assemblyMenuType :'',
      prodCode: {
        type: String
      },
      t8ProdInfoId:'',

      prodMode: {
        type: String,
        default: '1'
      },

      updateProduct:{
        type:Boolean,
        default:false
      }

    },
    data() {
      return {
        today: moment().format('YYYYMMDD'),
        currentYear:moment().format('YYYY'),
        months: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
        loadCalendar: false,
        MONTHS: {},
        monthlyDays: [],      // 用于存在月视图
        lstWorkday:[],        // workday只加载一年的
        lstDayIndexs: {},     // 日期索引， 主要记录日期的位置

        nextPeriodItem: {}, // 当前设置日期
        showTipsDate: "",
        validateResult: "",
        openRuleItems:[
          { // 周期型产品规则
            number: 2,          // 周期值
            openRule: '1',      // 开放周期规则： 1-天，2-周，3-月
            weekendRule: '1',   // 非工作日处理规则：1-顺延到下一工作日，2-提前到上一工作日， 3-节假日取消
            openDate: '',       // 开放日，如果开放规则是周，则为周一...周天，如果开放规则是月，则为1号...28号，如果开放规则是日，则该值无效
            openType: '',       // 开放类型： 1-开放、 2-申购、 3-赎回
            periodNumber:'',
            openDateOptions:[],
            disabled: true,
          }
        ],

        weekendRuleOptions: [{
          value: '1',
          label: '顺延到下一工作日'
        }, {
          value: '2',
          label: '提前到上一工作日'
        }, {
          value: '3',
          label: '节假日取消'
        }],
        openWeeklyOptions: [
          {value: '1', label: '周一'},
          {value: '2', label: '周二'},
          {value: '3', label: '周三'},
          {value: '4', label: '周四'},
          {value: '5', label: '周五'},
          {value: '6', label: '周六'},
          {value: '0', label: '周日'},

        ],
        openMonthlyOptions: [
          {value: '1',  label: '1号' },
          {value: '2',  label: '2号' },
          {value: '3',  label: '3号' },
          {value: '4',  label: '4号' },
          {value: '5',  label: '5号' },
          {value: '6',  label: '6号' },
          {value: '7',  label: '7号' },
          {value: '8',  label: '8号' },
          {value: '9',  label: '9号' },
          {value: '10', label: '10号'},
          {value: '11', label: '11号'},
          {value: '12', label: '12号'},
          {value: '13', label: '13号'},
          {value: '14', label: '14号'},
          {value: '15', label: '15号'},
          {value: '16', label: '16号'},
          {value: '17', label: '17号'},
          {value: '18', label: '18号'},
          {value: '19', label: '19号'},
          {value: '20', label: '20号'},
          {value: '21', label: '21号'},
          {value: '22', label: '22号'},
          {value: '23', label: '23号'},
          {value: '24', label: '24号'},
          {value: '25', label: '25号'},
          {value: '26', label: '26号'},
          {value: '27', label: '27号'},
          {value: '28', label: '28号'},
        ],
        openRuleOptions: [{
          value: '1',
          label: '天'
        }, {
          value: '2',
          label: '周'
        }, {
          value: '3',
          label: '月'
        }],
        openTypeOptions: [{
          value: '1',
          label: '开放'
        }, {
          value: '2',
          label: '申购'
        }, {
          value: '3',
          label: '赎回'
        }],

        weeklyItems:[
          {name: '一', itemClass: "thead-th"},
          {name: '二', itemClass: "thead-th"},
          {name: '三', itemClass: "thead-th"},
          {name: '四', itemClass: "thead-th"},
          {name: '五', itemClass: "thead-th"},
          {name: '六', itemClass: "thead-th weekend"},
          {name: '日', itemClass: "thead-th weekend"},
        ],


        legendItems:[         // 用于显示周期信息中的legend
          {name: '认购', legendClass: "pic-cycle-item1"},
          {name: '成立', legendClass: "pic-cycle-item2"},
          {name: '起息', legendClass: "pic-cycle-item8"},
          {name: '申购', legendClass: "pic-cycle-item3"},
          {name: '赎回', legendClass: "pic-cycle-item4"},
          {name: '开放', legendClass: "pic-cycle-item5"},
          {name: '到期', legendClass: "pic-cycle-item6"},
          {name: '清盘', legendClass: "pic-cycle-item7"},
          // {name: '兑付', legendClass: "pic-cycle-item8"},
        ],
        prodModeItems:{ // 产品形态(1：封闭净值 2：开放净值 3：货币净值 4：周期净值)
          "1": [ //封闭净值
            {suffix:"subsBeginDate",  name: '认购开始',   selected: false, itemClass: 'date-alive-item1', allowWeekend: false, minDate: '', maxDate:'subsEndDate,establishDate,endDate,windingDate,payDate', includeMinDate: true, includeMaxDate: true},
            {suffix:"subsEndDate",    name: '认购结束',   selected: false, itemClass: 'date-alive-item1', allowWeekend: false, minDate: 'subsBeginDate', maxDate:'establishDate,endDate,windingDate,payDate', includeMinDate: true, includeMaxDate: true},
            {suffix:"establishDate",  name: '成立日',     selected: false, itemClass: 'date-alive-item2', allowWeekend: false, minDate: 'subsEndDate,subsBeginDate', maxDate:'endDate,windingDate,payDate', includeMinDate: true, includeMaxDate: false},
            {suffix:"endDate",        name: '到期日',     selected: false, itemClass: 'date-alive-item6', allowWeekend: false, minDate: 'establishDate,subsEndDate,subsBeginDate', maxDate:'windingDate,payDate', includeMinDate: false, includeMaxDate: true},
            {suffix:"windingDate",    name: '清盘日',     selected: false, itemClass: 'date-alive-item7', allowWeekend: false, minDate: 'endDate,establishDate,subsEndDate,subsBeginDate', maxDate:'payDate', includeMinDate: true, includeMaxDate: true},
          ],
          "2": [ // 开放净值
            {suffix:"subsBeginDate",  name: '认购开始',   selected: false, itemClass: 'date-alive-item1', allowWeekend: false, minDate: '', maxDate:'subsEndDate, establishDate,openBeginDate,openEndDate,endDate,windingDate,payDate',       includeMinDate: true, includeMaxDate: true},
            {suffix:"subsEndDate",    name: '认购结束',   selected: false, itemClass: 'date-alive-item1', allowWeekend: false, minDate: 'subsBeginDate', maxDate:'establishDate,openBeginDate,openEndDate,,endDate,windingDate,payDate',      includeMinDate: true, includeMaxDate: true},
            {suffix:"establishDate",  name: '成立日',     selected: false, itemClass: 'date-alive-item2', allowWeekend: false, minDate: 'subsEndDate,subsBeginDate', maxDate:',openBeginDate,openEndDate,endDate,windingDate,payDate',        includeMinDate: true, includeMaxDate: false},
            {suffix:"openBeginDate",  name: '开放开始',   selected: false, itemClass: 'date-alive-item5', allowWeekend: false, minDate: 'establishDate,subsEndDate,subsBeginDate', maxDate:'openEndDate,endDate,windingDate,payDate',         includeMinDate: false, includeMaxDate: true},
            {suffix:"openEndDate",    name: '开放结束',   selected: false, itemClass: 'date-alive-item5', allowWeekend: false, minDate: 'openBeginDate,establishDate,subsEndDate,subsBeginDate,today', maxDate:'endDate,windingDate,payDate', includeMinDate: true, includeMaxDate: false},
            {suffix:"endDate",        name: '到期日',     selected: false, itemClass: 'date-alive-item6', allowWeekend: false, minDate: 'openEndDate,openBeginDate,establishDate,subsEndDate,subsBeginDate', maxDate:'windingDate,payDate',   includeMinDate: false, includeMaxDate: true},
            {suffix:"windingDate",    name: '清盘日',     selected: false, itemClass: 'date-alive-item7', allowWeekend: false, minDate: 'endDate,openEndDate,openBeginDate,establishDate,subsEndDate,subsBeginDate', maxDate:'payDate',       includeMinDate: true, includeMaxDate: true},
          ],
          "3": [ // 货币净值
            {suffix:"subsBeginDate",  name: '认购开始',   selected: false, itemClass: 'date-alive-item1', allowWeekend: false, minDate: '', maxDate:'subsEndDate, establishDate,valueDate,openBeginDate,openEndDate,endDate,windingDate,payDate',       includeMinDate: true, includeMaxDate: true},
            {suffix:"subsEndDate",    name: '认购结束',   selected: false, itemClass: 'date-alive-item1', allowWeekend: false, minDate: 'subsBeginDate', maxDate:'establishDate,valueDate,openBeginDate,openEndDate,,endDate,windingDate,payDate',      includeMinDate: true, includeMaxDate: true},
            {suffix:"establishDate",  name: '成立日',     selected: false, itemClass: 'date-alive-item2', allowWeekend: false, minDate: 'subsEndDate,subsBeginDate', maxDate:'valueDate,openBeginDate,openEndDate,endDate,windingDate,payDate',         includeMinDate: true, includeMaxDate: true},
            {suffix:"valueDate",      name: '起息日',     selected: false, itemClass: 'date-alive-item8', allowWeekend: false, minDate: 'establishDate,subsEndDate,subsBeginDate', maxDate:'openBeginDate,openEndDate,endDate,windingDate,payDate',     includeMinDate: true, includeMaxDate: false},
            {suffix:"openBeginDate",  name: '开放开始',   selected: false, itemClass: 'date-alive-item5', allowWeekend: false, minDate: 'valueDate,establishDate,subsEndDate,subsBeginDate', maxDate:'openEndDate,endDate,windingDate,payDate',         includeMinDate: false, includeMaxDate: true},
            {suffix:"openEndDate",    name: '开放结束',   selected: false, itemClass: 'date-alive-item5', allowWeekend: false, minDate: 'openBeginDate,valueDate,establishDate,subsEndDate,subsBeginDate,today', maxDate:'endDate,windingDate,payDate', includeMinDate: true, includeMaxDate: false},
            {suffix:"endDate",        name: '到期日',     selected: false, itemClass: 'date-alive-item6', allowWeekend: false, minDate: 'openEndDate,openBeginDate,valueDate,establishDate,subsEndDate,subsBeginDate', maxDate:'windingDate,payDate',   includeMinDate: false, includeMaxDate: true},
            {suffix:"windingDate",    name: '清盘日',     selected: false, itemClass: 'date-alive-item7', allowWeekend: false, minDate: 'endDate,openEndDate,openBeginDate,valueDate,establishDate,subsEndDate,subsBeginDate', maxDate:'payDate',       includeMinDate: true, includeMaxDate: true},
          ],
          "4": [ // 周期净值
            {suffix:"subsBeginDate",  name: '认购开始',   selected: false, itemClass: 'date-alive-item1', allowWeekend: false, minDate: '', maxDate:'subsEndDate, establishDate,openBeginDate,openEndDate,endDate,windingDate,payDate',       includeMinDate: true, includeMaxDate: true},
            {suffix:"subsEndDate",    name: '认购结束',   selected: false, itemClass: 'date-alive-item1', allowWeekend: false, minDate: 'subsBeginDate', maxDate:'establishDate,openBeginDate,openEndDate,,endDate,windingDate,payDate',      includeMinDate: true, includeMaxDate: true},
            {suffix:"establishDate",  name: '成立日',     selected: false, itemClass: 'date-alive-item2', allowWeekend: false, minDate: 'subsEndDate,subsBeginDate', maxDate:',openBeginDate,openEndDate,endDate,windingDate,payDate',        includeMinDate: true, includeMaxDate: false},
            {suffix:"openBeginDate",  name: '开放开始',   selected: false, itemClass: 'date-alive-item5', allowWeekend: false, minDate: 'establishDate,subsEndDate,subsBeginDate', maxDate:'openEndDate,endDate,windingDate,payDate',         includeMinDate: false, includeMaxDate: true},
            {suffix:"openEndDate",    name: '开放结束',   selected: false, itemClass: 'date-alive-item5', allowWeekend: false, minDate: 'openBeginDate,establishDate,subsEndDate,subsBeginDate,today', maxDate:'endDate,windingDate,payDate', includeMinDate: true, includeMaxDate: false},
            {suffix:"endDate",        name: '到期日',     selected: false, itemClass: 'date-alive-item6', allowWeekend: false, minDate: 'openEndDate,openBeginDate,establishDate,subsEndDate,subsBeginDate', maxDate:'windingDate,payDate',   includeMinDate: false, includeMaxDate: true},
            {suffix:"windingDate",    name: '清盘日',     selected: false, itemClass: 'date-alive-item7', allowWeekend: false, minDate: 'endDate,openEndDate,openBeginDate,establishDate,subsEndDate,subsBeginDate', maxDate:'payDate',       includeMinDate: true, includeMaxDate: true},
          ]

        },

        lstPeriodDate  : {}, // 设置日期参数，例如{"subsBeginDate": "20200202"}
        lstSettedDate : {}, // 设置日期事件，例如{"20200202": [{name:认购日}]}

        calendarInfo:[],
        rang:{
          subsBeginDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
          subsEndDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false ,disabled:false},
          establishDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
          valueDate:{min:moment().format('YYYYMMDD'),max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
          openBeginDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
          openEndDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
          endDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
          windingDate:{min:moment().format('YYYYMMDD') ,max:moment().format('YYYY') + "1231",showStatus:false,disabled:false},
        },
        loadDate:new Date().getTime(),
        endDateFunction:null,
        establishDateFunction:null,
        valueDateFunction:null,
        showCalendarFlag:false,
      }

    },

    methods: {

      getDateRegion (beginDate,endDate){
        if(beginDate && endDate){
          let aDate, oDate1, oDate2, iDays;
          if(beginDate.length==8){
            beginDate = beginDate.substr(0,4)+'-'+beginDate.substr(4,2)+'-'+beginDate.substr(6,2);
          }
          if(endDate.length==8){
            endDate = endDate.substr(0,4)+'-'+endDate.substr(4,2)+'-'+endDate.substr(6,2);
          }
          aDate = beginDate.split("-");
          oDate1 = new Date(aDate[1] + '/' + aDate[2] + '/' + aDate[0]);   //转换为12/13/2008格式
          aDate = endDate.split("-");
          oDate2 = new Date(aDate[1] + '/' + aDate[2] + '/' + aDate[0]);
          let i=(oDate1 - oDate2) / 1000 / 60 / 60 /24;
          /*if(i<0){
            i-=1;
          }else{
            i+=1;
          }*/
          iDays = i;   //把相差的毫秒数转换为天数
          //    this.T8ProdCalendar.$set("productTerm",iDays);
          this.T8ProdCalendar.productTerm = iDays;
        }
      },


      beforeSubmit : function(value){
        this.$set(value,"prodMode",this.prodMode);
        if(value.cycleOpenType == '1'){
          this.$set(value,"orderOpenDays","0");
        }

      },
      // 上一年
      prev () {

        this.currentYear = moment(this.currentYear).subtract(1, 'years').format('YYYY');
        this.createYearCalendar();
      },

      // 下一年
      next () {
        this.currentYear = moment(this.currentYear).add(1, 'years').format('YYYY');
        this.createYearCalendar();
      },

      // 切换到当年
      switchToday(){
        this.currentYear = moment().format('YYYY');
        this.createYearCalendar();
      },

      // 年视图鼠标事件
      mouseOnDay (date) {
        // 需要判断该日期是否可以选择，根据选择要素
        if (date.isWorkday || (!date.isWorkday && this.nextPeriodItem.allowWeekend)){

          var classArray = date.class.split(' ');

          if (classArray.indexOf('current-month') >= 0){

            date.isAlive = this.nextPeriodItem.itemClass;
            date.name = this.nextPeriodItem.name;
            this.showTipsDate = date.wholeDay;
          }


        }
      },

      // 年视图鼠标事件
      mouseOutDay (date) {
        date.isAlive = '';
        date.name = '';
        this.showTipsDate = '';
      },

      // 选择周期日期
      clickWorkday(date){
        this.validateResult = '';
        // 小于当日的，均不再接受修改
        if (date.wholeDay < this.today){
          return;
        }
        if (date.class.indexOf('not-current-month') >= 0){
          return;
        }
        // 点击事件包括几种情况：
        // 1. 删除、
        //    1.1 删除周期日，如果是关键日重叠，则逐个删除，联动删除自动设置的日期，如：认购日、申购日、赎回日、开放日
        //    1.2 删除非周期日，
        // 2. 切换，开放日切换、日期重叠后
        // 3. 增加，
        let subsBeginDate = this.lstPeriodDate['subsBeginDate'];  // 设置认购开始日
        let subsEndDate = this.lstPeriodDate['subsEndDate'];    // 设置的认购结束日
        let openBeginDate = this.lstPeriodDate['openBeginDate'];  // 设置的开放开始日
        let openEndDate = this.lstPeriodDate['openEndDate'];      // 设置的开放结束日
        if (this.lstSettedDate[date.wholeDay] != null){
          // 判断设置日期是否存在，如果存在，循环切换
          if (date.disable){
            // if(subsBeginDate != null && subsEndDate != null
            //   && date.wholeDay > subsBeginDate && date.wholeDay < subsEndDate){
            //   if (this.lstSettedDate[date.wholeDay] == null || this.lstSettedDate[date.wholeDay].calendarNumber == 0){
            //     // 日历事件个数为0，表示该日期无时间，增加一个认购事件
            //     this.addPeriodDate(date, 'subsDate');
            //   } else {
            //     // 存在事件，则
            //     this.cancelWorkday(date, 'subsDate');
            //   }
            // }
            if(openBeginDate != null && openEndDate != null
              && date.wholeDay > openBeginDate && date.wholeDay < openEndDate){
              // 该操作在开放期内，主要动作有：增加开放日，切换日期类型
              if (this.lstSettedDate[date.wholeDay] == null || this.lstSettedDate[date.wholeDay].calendarNumber == 0){
                // 日历事件个数为0，表示该日期无时间，增加一个认购事件
                this.addPeriodDate(date, 'openDate');
              } else {
                // 存在事件，则
                this.changeOpenDate(date);
              }
            }
          }
        } else {
          if(openBeginDate != null && openEndDate != null
            && date.wholeDay > openBeginDate && date.wholeDay < openEndDate){
            // 该操作在开放期内，主要动作有：增加开放日，切换日期类型
            if (this.lstSettedDate[date.wholeDay] == null || this.lstSettedDate[date.wholeDay].calendarNumber == 0){
              // 日历事件个数为0，表示该日期无时间，增加一个认购事件
              this.addPeriodDate(date, 'openDate');
            } else {
              // 存在事件，则
              this.changeOpenDate(date);
            }
          }
        }
      },


      /*
      * 修改产品开放周期的数据与样式
      * 申购赎回日 -> 申购日 -> 赎回日->申购赎回日->清理
      * 紫色         深蓝        蓝绿    紫        无样式
      * */
      changeOpenDate(date){
        let targetDate = date.wholeDay;
        for(let calendarBo of this.calendarInfo){
          if (calendarBo.sysDate == targetDate){
            if (calendarBo.change == 'openDate'){
              // 开放日切换为申购日
              calendarBo.change = 'applyDate';
              calendarBo.isApplyDate = '1';
              calendarBo.isRedeemDate = '0';
              date.calendarStyle = 'date-alive-item3'
              this.removeCalendarStyle(targetDate, 'date-alive-item5');
              this.addCalendarStyle(targetDate, 'date-alive-item3');
            } else if (calendarBo.change == 'applyDate'){
              // 申购日，切换为赎回日
              calendarBo.change = 'redeemDate';
              calendarBo.isApplyDate = '0';
              calendarBo.isRedeemDate = '1';
              date.calendarStyle = 'date-alive-item4'
              this.removeCalendarStyle(targetDate, 'date-alive-item3');
              this.addCalendarStyle(targetDate, 'date-alive-item4');
            } else if (calendarBo.change == 'redeemDate'){
              // 赎回日，切换为开放日
              calendarBo.change = 'cleanDate';
              calendarBo.isApplyDate = '1';
              calendarBo.isRedeemDate = '1';
              date.calendarStyle = 'date-alive-item5'
              this.removeCalendarStyle(targetDate, 'date-alive-item4');
              this.addCalendarStyle(targetDate, 'date-alive-item5');
            } else {
              this.cancelWorkday(date, 'openDate');
            }
          }
        }
      },

      // 此处只用element方式设置会更快，否则半天加载不出来
      addCalendarStyle(targetDate, targetStyle){
        var doc = document.getElementById("ID-" + targetDate);
        if (doc != null){
          doc.className = doc.className + ' ' + targetStyle;
        }

      },

      // 删除原来的样式
      removeCalendarStyle(targetDate, targetStyle){
        var doc = document.getElementById("ID-" + targetDate);
        if (doc != null){
          var classArray = doc.className.split(' ');
          let removedClass = '';
          classArray.forEach(classItem => {
            if (!classItem.startsWith('date-alive-item')){
              removedClass = removedClass + ' ' + classItem;
            }
          });
          doc.className = removedClass ;
        }
      },

      // 增加认购日或开放日
      addPeriodDate(date, dateSuffix){
        let targetDate = date.wholeDay; // 目标日期
        let calendarName = '';
        let clonedDay = {};
        // if(dateSuffix == 'openDate') {
        //   // 开放日， 获取开放开始日的calendarStyle属性
        //   clonedDay = this.lstPeriodDate['openBeginDate'];
        //   calendarName = '申购日';
        // }
        // if(dateSuffix == 'subsDate') {
        //   // 开放日， 获取开放开始日的calendarStyle属性
        //   clonedDay = this.lstPeriodDate['subsBeginDate'];
        //   calendarName = '认购日';
        // }
        // let clonedYear = clonedDay.substring(0,4);
        // // 根据周期不同，克隆认购开始日期或开放开放日期的calendarStyle属性
        // let dayIndex = this.lstDayIndexs[clonedYear][clonedDay];
        date.calendarStyle = "date-alive-item5";
        this.addCalendarStyle(date.wholeDay, date.calendarStyle);
        this.setCalendarBo(date, dateSuffix);

        // 设置日期事件
        let calendarEvent = {name: calendarName, periodSuffix: dateSuffix, dateSuffix: dateSuffix};

        if (this.lstSettedDate[targetDate] == null){
          this.lstSettedDate[targetDate] = [];
          this.lstSettedDate[targetDate].calendarNumber = 0;
        }

        this.lstSettedDate[targetDate].push(calendarEvent);
        this.lstSettedDate[targetDate].calendarNumber++;

      },

      // 设置只有一个日期的，例如成立日、清盘日，支付日、起息日
      setOnceDate(date, periodSuffix, dateSuffix, calendarName,className){
        // 先把日期都设置到已设置的列表中
        this.lstPeriodDate[periodSuffix] = date.wholeDay;
        // 设置日期事件
        let calendarEvent = {name: calendarName, periodSuffix: periodSuffix, dateSuffix: dateSuffix};

        if (this.lstSettedDate[date.wholeDay] == null){
          this.lstSettedDate[date.wholeDay] = [];
          this.lstSettedDate[date.wholeDay].calendarNumber = 0;
        }
        this.lstSettedDate[date.wholeDay].push(calendarEvent);
        this.lstSettedDate[date.wholeDay].calendarNumber++;
        //date.calendarStyle = this.getClassName(date.wholeDay , className);
        //this.removeCalendarStyle(date.wholeDay, date.calendarStyle);
        //this.addCalendarStyle(date.wholeDay, date.calendarStyle);
        this.setCalendarBo(date, dateSuffix);
      },

      /**
       * 生成一条 ta_prod_calender数据
       * 设置日历BO
       * @param openType 开放标志： 1-开放、2-申购、3-赎回
       */
      setCalendarBo(date, dateSuffix, calendarName, openType){
        // 设置日历BO
        let calendarBo = {};

        for(let cBo of this.calendarInfo){
          if (cBo.sysDate == date.wholeDay){
            calendarBo = cBo;
          }
        }

        if (calendarName == null || calendarName == ''){
          calendarName = this.nextPeriodItem.name;
        }

        if (this.isJsonEmpty(calendarBo)){
          calendarBo = {
            prodCode        : this.prodCode,
            sysDate         : date.wholeDay,
            name            : calendarName,
            change          : '',
            calendarNumber  :  0, // 事件条数默认为1
            isSubsDate      : '0',
            isEstablishDate : '0',
            isValueDate     : '0',
            isApplyDate     : '0',
            isRedeemDate    : '0',
            isOpenDate      : '0',
            isWindingDate   : '0',
            isEndDate       : '0',
            isRegisterDate  : '0',
            isConvertDate   : '0',
            isPayDate       : '0'
          };
        } else {
          let indexBo = this.calendarInfo.indexOf(calendarBo);
          this.calendarInfo.splice(indexBo, 1);
          calendarBo.calendarNumber--;
        }

        switch (dateSuffix) {
          case 'subsDate':
            calendarBo.isSubsDate = '1';
            calendarBo.calendarNumber++;
            break;
          case 'establishDate':
            calendarBo.isEstablishDate = '1';
            calendarBo.calendarNumber++;
            break;
          case 'valueDate':
            calendarBo.isValueDate = '1';
            calendarBo.calendarNumber++;
            break;
          case 'openBeginDate':
          case 'openEndDate':
            calendarBo.isApplyDate = '1';
            calendarBo.isRedeemDate = '1';
            calendarBo.isOpenDate = '1';
            calendarBo.change = 'openDate'; // 设置为开放日
            calendarBo.calendarNumber++;
            break;
          case 'openDate':
            // 开放标志： 1-开放日、2-申购日、3-赎回日
            if (openType == '2'){
              calendarBo.isApplyDate = '1';
              calendarBo.isRedeemDate = '0';
              calendarBo.isOpenDate = '1';
              calendarBo.change = 'applyDate'; // 设置为申购日
            } else if (openType == '3'){
              calendarBo.isApplyDate = '0';
              calendarBo.isRedeemDate = '1';
              calendarBo.isOpenDate = '1';
              calendarBo.change = 'redeemDate'; // 设置为赎回日
            } else {
              calendarBo.isApplyDate = '1';
              calendarBo.isRedeemDate = '1';
              calendarBo.isOpenDate = '1';
              calendarBo.change = 'openDate'; // 设置为开放日
            }
            calendarBo.calendarNumber++;
            break;
          case 'windingDate':
            calendarBo.isWindingDate = '1';
            calendarBo.calendarNumber++;
            break;
          case 'endDate':
            calendarBo.isEndDate = '1';
            calendarBo.calendarNumber++;
            break;
          case 'payDate':
            calendarBo.isPayDate = '1';
            calendarBo.calendarNumber++;
            break;
          default:
            break;
        }

        this.calendarInfo.push(calendarBo);
      },
      /*
 *自动添加认购日
* 认购起始日循环到认购结束日为止，中间有个判断是否为工作日
* */
      setSubsDate(subsBeginDate, subsEndDate){
        let days = 1;
        subsBeginDate = subsBeginDate < moment().format('YYYYMMDD')? moment().subtract(1,"days").format('YYYYMMDD'):subsBeginDate;
        while(true){
          let subsDate = moment(subsBeginDate).add(days, 'days').format('YYYYMMDD');
          if (subsDate >= subsEndDate){
            break;
          }
          days++;
          if (this.lstWorkday.indexOf(subsDate) > 0){ // 判断是工作日
            // 设置所有认购日
            this.setPeriodDate(subsDate, 'subsDate', 'subsDate', '认购日')
          }

        }

      },
      /*
      * 开放日的生成方法
      * 1.周期产品按照 周期值 开放周期规则生成产品开放日
      * 2.其余产品从产品开放日 到产品开放结束日的所有工作日都开放
      * */
      setOpenDate(date, openBeginDate, openEndDate){
        openBeginDate = openBeginDate < moment().format('YYYYMMDD')? moment().subtract(1,"days").format('YYYYMMDD'):openBeginDate;
        if (this.prodMode == '4') { //周期净值，才需要按照某种规则生成
          this.openRuleItems.forEach(periodRule => {
            let openRule = periodRule.openRule;
            let number = periodRule.number||1;
            number = number==0?1:number;
            let weekendRule = periodRule.weekendRule;
            let openType = periodRule.openType;
            let openDate = periodRule.openDate;
            // number: 2,          // 周期值
            // openRule: '1',      // 开放周期规则： 1-天，2-周，3-月
            // weekendRule: '1',   // 非工作日处理规则：1-顺延到下一工作日，2-提前到上一工作日， 3-节假日取消
            // openDate: '',       // 开放日，如果开放规则是周，则为周一...周天，如果开放规则是月，则为1号...28号，如果开放规则是日，则该值无效
            // openType: '',       // 开放类型： 1-开放、 2-申购、 3-赎回

            switch (openRule) {
              case "1":
                this.setDailyPeriodDate(date, openBeginDate, openEndDate, number, weekendRule, openType);
                break;
              case "2":
                this.setWeeklyPeriodDate(date, openBeginDate, openEndDate, number, weekendRule, openType, openDate)
                break;
              case "3":
                this.setMonthlyPeriodDate(date, openBeginDate, openEndDate, number, weekendRule, openType, openDate)
                break;
              default:
                break;
            }
          });
        } else { // 去掉周期产品，其他产品均为天天开放，工作日均生成

          let days = 1;
          while(true){
            let openDate = moment(openBeginDate).add(days, 'days').format('YYYYMMDD');
            if (openDate >= openEndDate){
              break;
            }
            days++;
            if (this.lstWorkday.indexOf(openDate) >= 0){ // 判断是工作日
              // 设置所有认购日
              this.setPeriodDate(openDate, 'openDate', 'openDate', '开放日')
            }
          }
        }
      },
      getClassName(date,className){
        let suffix = "";
        if(this.lstSettedDate[date]&& this.lstSettedDate[date].length > 0){
          for(let item of this.lstSettedDate[date]){
            if(item.periodSuffix){
              suffix += "," + item.periodSuffix;
            }
          }
        }
        if(suffix.indexOf("openDate")!= -1){
          className = "date-alive-item5";
        }else if (suffix.indexOf("subsDate")!=-1){
          className = "date-alive-item1";
        }else if (suffix.indexOf("windingDate")!=-1){
          className = "date-alive-item7";
        }else if (suffix.indexOf("endDate")!=-1){
          className = "date-alive-item6";
        }else if (suffix.indexOf("establishDate")!=-1){
          className = "date-alive-item2";
        }else if (suffix.indexOf("valueDate")!=-1){
          className = "date-alive-item8";
        }
        return className;
      },
      // 设置开放日，按日
      setDailyPeriodDate(date, openBeginDate, openEndDate, number, weekendRule, openType){

        let openDate = moment(openBeginDate).format('YYYYMMDD');
        let periodCycleIndex = 1;
        while(true){
          // 循环均是以开放开始日为起点递增的
          let addCycleDays = periodCycleIndex * number;
          // 开放日
          let cycleOpenDate = moment(openDate).add(addCycleDays, 'days').format('YYYYMMDD');
          if (cycleOpenDate >= openEndDate){ // 开放日大于开放结束日，则跳出
            break;
          }
          if (this.lstWorkday.indexOf(cycleOpenDate) > 0){
            // 表示工作日存在工作日中
            this.setPeriodDate(cycleOpenDate, 'openDate', 'openDate', '开放日', openType);
          } else {
            // 表示工作日不在工作日中
            // 根据非工作日处理规则处理： 非工作日处理规则：1-顺延到下一工作日，2-提前到上一工作日， 3-工作日取消出
            if (weekendRule == '1'){
              // 1-顺延到下一工作日
              let hasNext = false;
              let addDays = 1;
              while(!hasNext){
                let nextDate = moment(cycleOpenDate).add(addDays, 'days').format('YYYYMMDD');
                if (nextDate >= openEndDate){
                  break;
                }
                if (this.lstWorkday.indexOf(nextDate) > 0){
                  this.setPeriodDate(nextDate, 'openDate', 'openDate', '开放日', openType);
                  hasNext = true;
                }
                addDays ++;
              }
            } else if (weekendRule == '2'){
              // 2-提前到上一工作日
              let hasPrevious = false;
              let subtractDays = -1;
              while(!hasPrevious){
                let previousDate = moment(cycleOpenDate).add(subtractDays, 'days').format('YYYYMMDD');
                if (previousDate <= openBeginDate){
                  break;
                }
                if (this.lstWorkday.indexOf(previousDate) > 0){
                  this.setPeriodDate(previousDate, 'openDate', 'openDate', '开放日', openType);
                  hasPrevious = true;
                }
                subtractDays--;
              }
            } else {
              // 3-工作日取消出,直接返回
              // return false;
            }
          }
          periodCycleIndex++;
        }

      },

      // 设置开放日，按周
      setWeeklyPeriodDate(date, openBeginDate, openEndDate, number, weekendRule, openType, openDate){
        if (openDate != ''){ // 每几周,周几开放/申购/赎回，，日期
          // 从开放开始日算起，获取到第一个周几
          let addDays = 0;
          while(true){
            let addDate = moment(openBeginDate).add(addDays, 'days');
            if (addDate.day() == openDate){
              openBeginDate = addDate.format('YYYYMMDD');
              break;
            }
            addDays++;
          }
        }
        number = number * 7; // 一周7天
        this.setWeekPeriodDate(date, openBeginDate, openEndDate, number, weekendRule, openType )
      },
      setWeekPeriodDate(date, openBeginDate, openEndDate, number, weekendRule, openType ){
        let openDate = moment(openBeginDate).format('YYYYMMDD');

        let periodCycleIndex = 0;

        while(true){

          // 循环均是以开放开始日为起点递增的
          let addCycleDays = periodCycleIndex * number;

          // 开放日
          let cycleOpenDate = moment(openDate).add(addCycleDays, 'days').format('YYYYMMDD');

          if (cycleOpenDate >= openEndDate){ // 开放日大于开放结束日，则跳出
            break;
          }

          if (this.lstWorkday.indexOf(cycleOpenDate) > 0){
            // 表示工作日存在工作日中

            this.setPeriodDate(cycleOpenDate, 'openDate', 'openDate', '开放日', openType);

          } else {
            // 表示工作日不在工作日中
            // 根据非工作日处理规则处理： 非工作日处理规则：1-顺延到下一工作日，2-提前到上一工作日， 3-工作日取消出

            if (weekendRule == '1'){
              // 1-顺延到下一工作日

              let hasNext = false;
              let addDays = 1;

              while(!hasNext){

                let nextDate = moment(cycleOpenDate).add(addDays, 'days').format('YYYYMMDD');

                if (nextDate >= openEndDate){
                  break;
                }

                if (this.lstWorkday.indexOf(nextDate) > 0){


                  this.setPeriodDate(nextDate, 'openDate', 'openDate', '开放日', openType);

                  hasNext = true;
                }
                addDays ++;
              }

            } else if (weekendRule == '2'){

              // 2-提前到上一工作日
              let hasPrevious = false;
              let subtractDays = -1;
              while(!hasPrevious){
                let previousDate = moment(cycleOpenDate).add(subtractDays, 'days').format('YYYYMMDD');

                if (previousDate <= openBeginDate){
                  break;
                }

                if (this.lstWorkday.indexOf(previousDate) > 0){

                  this.setPeriodDate(previousDate, 'openDate', 'openDate', '开放日', openType);

                  hasPrevious = true;

                }
                subtractDays--;
              }

            } else {
              // 3-工作日取消出,直接返回
              // return false;

            }

          }

          periodCycleIndex++;
        }

      },
      setMonthlyPeriodDate(date, openBeginDate, openEndDate, number, weekendRule, openType, openDate){
        if (openDate != ''){ // 每几周,周几开放/申购/赎回，，日期
          // 从开放开始日算起，获取到第一个周几
          let addDays = 0;
          while(true){
            let addDate = moment(openBeginDate).add(addDays, 'days');
            if (addDate.format('D') == openDate){
              openBeginDate = addDate.format('YYYYMMDD');
              break;
            }
            addDays++;
          }
        }
        this.setMonthlyDate(date, openBeginDate, openEndDate, number, weekendRule, openType);

      },
      // 设置开放日，按月
      setMonthlyDate(date, openBeginDate, openEndDate, number, weekendRule, openType){
        let openDate = moment(openBeginDate).format('YYYYMMDD');
        let periodCycleIndex = 0;
        while(true){
          // 循环均是以开放开始日为起点递增的
          let addCycleMonths = periodCycleIndex * number;
          // 开放日
          let cycleOpenDate = moment(openDate).add(addCycleMonths, 'months').format('YYYYMMDD');
          if (cycleOpenDate >= openEndDate){ // 开放日大于开放结束日，则跳出
            return ;
          }
          if (this.lstWorkday.indexOf(cycleOpenDate) > 0){
            // 表示工作日存在工作日中
            this.setPeriodDate(cycleOpenDate, 'openDate', 'openDate', '开放日', openType);
          } else {
            // 表示工作日不在工作日中
            // 根据非工作日处理规则处理： 非工作日处理规则：1-顺延到下一工作日，2-提前到上一工作日， 3-工作日取消出
            if (weekendRule == '1'){
              // 1-顺延到下一工作日
              let hasNext = false;
              let addDays = 1;
              while(!hasNext){
                let nextDate = moment(cycleOpenDate).add(addDays, 'days').format('YYYYMMDD');
                if (nextDate >= openEndDate){
                  return ;
                }
                if (this.lstWorkday.indexOf(nextDate) > 0){
                  this.setPeriodDate(nextDate, 'openDate', 'openDate', '开放日', openType);
                  hasNext = true;
                }
                addDays ++;
              }
            } else if (weekendRule == '2'){
              // 2-提前到上一工作日
              let hasPrevious = false;
              let subtractDays = -1;
              while(!hasPrevious){
                let previousDate = moment(cycleOpenDate).add(subtractDays, 'days').format('YYYYMMDD');
                if (previousDate <= openBeginDate){
                  return ;
                }
                if (this.lstWorkday.indexOf(previousDate) > 0){
                  this.setPeriodDate(previousDate, 'openDate', 'openDate', '开放日', openType);
                  hasPrevious = true;
                }
                subtractDays--;
              }

            } else {
              // 3-工作日取消出,直接返回
              // return false;
            }
          }
          periodCycleIndex++;
        }
      },

      /**
       * @param workday   工作日
       * @param periodSuffix 周期日类型，例如：subsEndDate, 具体类型祥见
       * @param dateSuffix  日期类型，例如：subsDate, openDate
       */
      setPeriodDate(workday, periodSuffix, dateSuffix, calendarName, openType){
        let workYear = workday.substring(0,4);
        let dayIndex = this.lstDayIndexs[workYear][workday];
        let settedDate = this.MONTHS[workYear][dayIndex.month][dayIndex.weeks][dayIndex.weekday];
        if (this.lstSettedDate[workday] == null){
          this.lstSettedDate[workday] = [];
          this.lstSettedDate[workday].calendarNumber = 0;
        }
        // 设置日期事件
        let calendarEvent = {name: calendarName, periodSuffix: periodSuffix, dateSuffix: dateSuffix};
        // 排除周期设置开放日时，有前移或者后移导致的多个日期重复事件
        for(let setted of this.lstSettedDate[workday]){
          if (setted.periodSuffix == periodSuffix && (openType != '1' && openType != '2' && openType != '3')){
            return ;
          }
        }
        if (this.lstSettedDate[workday].length == 0){
          // 不存在该日期的事件
          if (openType == '2'){ // 申购
            settedDate.calendarStyle = 'date-alive-item3';
            this.addCalendarStyle(workday, 'date-alive-item3');
          } else if (openType == '3') { // 赎回
            settedDate.calendarStyle = 'date-alive-item4';
            this.addCalendarStyle(workday, 'date-alive-item4');
          } else {
            if(dateSuffix == "openDate"){
              settedDate.calendarStyle =  'date-alive-item5';
              this.addCalendarStyle(workday, 'date-alive-item5');
            }
            // else{
            //   settedDate.calendarStyle =  'date-alive-item1';
            //   this.addCalendarStyle(workday, 'date-alive-item1');
            // }
          }
          this.setCalendarBo(settedDate, periodSuffix, calendarName, openType);
          this.lstSettedDate[workday].push(calendarEvent);
          this.lstSettedDate[workday].calendarNumber++;
        } else {
          // 存在该日期的事件
          // 如果原来的样式中已经存在其他事件，则综合设置事件
          switch (openType) {
            case '1': // 申购
              this.setCalendarBo(settedDate, periodSuffix, calendarName, '1');
              settedDate.calendarStyle = 'date-alive-item5';
              this.addCalendarStyle(workday, 'date-alive-item5');
              break;
            case '2': // 申购
              if (settedDate.calendarStyle == 'date-alive-item4' ){
                this.setCalendarBo(settedDate, periodSuffix, calendarName, '1');
                settedDate.calendarStyle = 'date-alive-item5';
                this.addCalendarStyle(workday, 'date-alive-item5');
              }
              break;
            case '3': // 赎回
              if (settedDate.calendarStyle == 'date-alive-item3' ){
                this.setCalendarBo(settedDate, periodSuffix, calendarName, '1');
                settedDate.calendarStyle = 'date-alive-item5';
                this.addCalendarStyle(workday, 'date-alive-item5');
              }
              break;
            default:
              break;
          }
        }
      },

      // 删除工作日
      cancelWorkday(date, dateSuffix){
        // 两种类型，如果还存在多个事件，则不变
        switch (dateSuffix) {
          case 'openDate': // 开放日
            this.cancelOnceDate(date, dateSuffix, '');
            break;
          // case 'subsDate': // 认购日
          //   this.cancelOnceDate(date, dateSuffix, '');
          //   break;
          default:
            console.log("Wrong dateSuffix... ");
            break;
        }
      },
      // 删除只有一个日期的，例如成立日、清盘日，支付日
      /**
       * @param date 日期
       * @param periodSuffix 周期识别码，例如：subsBeginDate, openBeginDate,
       * @param dateSuffix  日期识别码，例如：subsDate, openDate
       */
      cancelOnceDate(date, dateSuffix, periodSuffix){
        // 删除该日已设置的值
        if (periodSuffix != null && periodSuffix != ''){
          if (periodSuffix == 'establishDate') {
            this.T8ProdCalendar.establishDate = '';
          } else if (periodSuffix == 'endDate'){
            this.T8ProdCalendar.endDate = '';
          } else if (periodSuffix == 'valueDate'){
            this.T8ProdCalendar.valueDate = '';
          }
          this.lstPeriodDate[periodSuffix] = null;
        }
        let targetDate = date.wholeDay;
        // 从日期中的日历事件中删除该事件，且事件减1
        for(let calendarIndex in this.lstSettedDate[targetDate]){
          if (this.lstSettedDate[targetDate][calendarIndex].dateSuffix == dateSuffix){
            this.lstSettedDate[targetDate].splice(calendarIndex, 1);
            this.lstSettedDate[targetDate].calendarNumber--;
          }
        }
        // 删除CalendarBo
        this.cancelCalendarBo(targetDate, dateSuffix);
        this.removeCalendarStyle(targetDate, date.calendarStyle);
        //this.addCalendarStyle(targetDate,this.getClassName(targetDate,""));
        if (this.lstSettedDate[targetDate]&&this.lstSettedDate[targetDate].length == 0){
          // 如果没有事件了，则设置为空
          date.calendarStyle = '';
          date.top = '';
        }
        for(let item of this.prodModeItems[this.prodMode]){
          if (item.suffix == periodSuffix){
            item.selected = false;
            date.isAlive = '';
            date.disable = false; // 此处设置为可以选择，说明关键日期已经更改
            break;
          }
        }

      },
      cancelCalendarBo(targetDate, dateSuffix){
        for(let calendarBo of this.calendarInfo){
          if (calendarBo.sysDate == targetDate){
            switch (dateSuffix) {
              case 'subsDate':
                calendarBo.isSubsDate = '0';
                break;
              case 'establishDate':
                calendarBo.isEstablishDate = '0';
                break;
              case 'openDate':
                calendarBo.isApplyDate = '0';
                calendarBo.isRedeemDate = '0';
                calendarBo.isOpenDate = '0';
                break;
              case 'windingDate':
                calendarBo.isWindingDate = '0';
                break;
              case 'end_date':
                calendarBo.isEndDate = '0';
                break;
              case 'payDate':
                calendarBo.isPayDate = '0';
                break;
              case 'valueDate':
                calendarBo.isValueDate = '0';
                break;
              default:
                break;
            }
            if (this.lstSettedDate[targetDate].calendarNumber == 0){ // 此时已经说明无事件了, 则删除该bo
              let boIdx = this.calendarInfo.indexOf(calendarBo);
              this.calendarInfo.splice(boIdx, 1);
            }
          }

        }
      },

      // 删除周期，包括认购日和开放日（申购日/赎回日）
      cancelPeriodDate(startDate, endDate, periodSuffix){
        let days = 1;
        while(true){
          let periodDate = moment(startDate).add(days, 'days').format('YYYYMMDD');
          if (periodDate >= endDate){
            break;
          }
          days++;
          if (periodDate < this.today){
            continue;
          }
          if (this.lstWorkday.indexOf(periodDate) > 0
            && this.lstSettedDate[periodDate] != null
            && this.lstSettedDate[periodDate].length > 0){
            for(let settedDate of this.lstSettedDate[periodDate]){
              if (settedDate.periodSuffix == periodSuffix){
                let periodYear = periodDate.substring(0,4);
                let dayIndex = this.lstDayIndexs[periodYear][periodDate];
                let cancelDate = this.MONTHS[periodYear][dayIndex.month][dayIndex.weeks][dayIndex.weekday];
                this.cancelOnceDate(cancelDate, periodSuffix);
              }
            }
          }
        }
      },

      /**
       * 创建月视图
       * @param currentYearMonth
       */
      createMonthlyCalendar(currentYearMonth, month) {
        let currentYear = currentYearMonth.substring(0,4);
        this.monthlyDays = [];
        // 获取当月的一号是星期几 以便来生成上月的日期 填补够42个格子
        const monthFirstDay = moment(currentYearMonth + '-01', 'YYYY-MM-DD')
        // 获得一号与第一个格子内应该有的天数距离 这里需要注意的是 weekday 是从周日 为 0 开始的
        let firstDayWeekday = moment(monthFirstDay).weekday()
        if (firstDayWeekday === 0) {
          firstDayWeekday = 7
        }
        let daysDistance = 1 - firstDayWeekday
        for (let weeks = 0; weeks < 6; weeks++) {
          this.monthlyDays.push([])
          for (let weekday = 0; weekday < 7; weekday++) {
            // 该对象有两个属性 一个是class属性 还有一个就是日期
            let date = {}
            date.day = moment(monthFirstDay).add(daysDistance, 'days')
            date.isAlive = '';
            date.wholeDay = moment(date.day).format('YYYYMMDD')
            const dayMonth = moment(date.day).month()
            // 是这个月的日期
            if (dayMonth === moment(monthFirstDay).month()) {
              date.class = 'current-month '
              date.dayID = 'ID-' + date.wholeDay
              // 设置日期索引
              if (this.lstDayIndexs[currentYear] == null){
                this.lstDayIndexs[currentYear] = {};
              }
              let dayIndex = {"month": month, "weeks": weeks, "weekday": weekday};
              this.lstDayIndexs[currentYear][date.wholeDay] = dayIndex;
              // 日期是今天的高亮
              if (moment(date.day).isSame(this.today, 'days')) {
                date.class += 'today '
              } else {
                date.disable = true;
              }
            } else {
              date.class = 'not-current-month '
            }
            let dayIdx = date.day.day();
            if (dayIdx == 0 || dayIdx == 6){ // 设置周六、周天
              date.class += 'weekend '
            }
            date.day = moment(date.day).format('D')
            this.monthlyDays[weeks].push(date);
            daysDistance++
          }
        }
      },

      queryProdProgram(){
        this.lstWorkday = [];
        this.httpUtil.comnQuery({
          action: 'WorkdayItem.find',
          params: {pgmno: this.T8ProdCalendar.pgmno}
        }).then(data => {
          let rows_ = data.rows;
          // 先将工作日存入到lstWorkday的数组中，方便后期再判断
          this.lstWorkday = rows_.map(function(item){
            return item.workday;
          });
          this.createYearCalendar();
          this.loadPeriodInfo();
          this.changeRang();
        });
      },

      /**
       * 判断一个json对象是否为空，即{}
       *
       * @param jsonObject
       * @returns {boolean}
       */
      isJsonEmpty(jsonObject){
        var isEmpty = true;
        for (var prop in jsonObject){
          isEmpty = false;
          break;
        }
        return isEmpty;
      },

      loadPeriodInfo(){
        if (this.prodCode != null && this.prodCode != ''){
          // 判断产品代码非空，如果非空，则查询产品日历表
          // 查询产品周期信息
          this.httpUtil.comnQuery({
            action: 'T8ProdPeriod.findT8ProdPeriods',
            params: {prodCode: this.prodCode}
          }).then(data => {
            let rows_ = data.rows;
            if(rows_[0]&&rows_[0].isequalDate == 1 ){
              if(this.establishDateFunction){
                this.establishDateFunction();
                this.establishDateFunction = null;
              }
              if(this.endDateFunction){
                this.endDateFunction();
                this.endDateFunction = null;
              }
              if(this.valueDateFunction){
                this.valueDateFunction();
                this.valueDateFunction = null;
              }
            }
            rows_.forEach(periodBo => {
              this.lstPeriodDate = periodBo;
              this.$nextTick(()=>{
                if(this.lstPeriodDate["openBeginDate"] && this.lstPeriodDate["openEndDate"]){
                  this.cancelPeriodDate( this.lstPeriodDate["openBeginDate"], this.lstPeriodDate["openEndDate"], "openDate");
                }
                // if(this.lstPeriodDate["subsBeginDate"] && this.lstPeriodDate["subsEndDate"]){
                //   this.cancelPeriodDate(this.lstPeriodDate["subsBeginDate"], this.lstPeriodDate["subsEndDate"], "subsDate");
                // }
                this.loadCalendarInfo();
              })
            });
          });
        }
      },
      loadCalendarInfo(){
        // 重置当年的信息
        if (this.prodCode != null && this.prodCode != ''){
          // 判断产品代码非空，如果非空，则查询产品日历表
          this.httpUtil.comnQuery({
            action: 'T8ProdCalendar.findProdCalendars',
            params: {prodCode: this.prodCode}
          }).then(data => {
            var rows_ = data.rows;
            this.checkCalendarInfo(rows_);
          });
        } else {
        }
      },
      checkCalendarInfo(rows_){
        rows_.forEach(calendarBo => {
          // if(calendarBo.isSubsDate== '1' && this.lstPeriodDate['subsBeginDate'] != calendarBo.sysDate && this.lstPeriodDate['subsEndDate'] != calendarBo.sysDate ){
          //   let  aliveItemClass = 'date-alive-item1';
          //     // 普通认购日
          //     if (this.lstSettedDate[calendarBo.sysDate] == null){
          //       this.lstSettedDate[calendarBo.sysDate] = [];
          //       this.lstSettedDate[calendarBo.sysDate].calendarNumber = 0;
          //     }
          //     this.calendarInfo.push(calendarBo);
          //     this.addOnceSettedDate(calendarBo.sysDate, '认购日', 'subsDate', 'subsDate', aliveItemClass);
          // }
          if (calendarBo.isApplyDate == '1' || calendarBo.isRedeemDate == '1'){
            calendarBo.isOpenDate = '1';
            let aliveItemClass = 'date-alive-item5';
            if (calendarBo.isApplyDate == '1' && calendarBo.isRedeemDate == '1'){
              // 开放日，包括认购和申购日
              calendarBo.change = 'openDate'; // 设置为开放日
              aliveItemClass = 'date-alive-item5'
            } else if (calendarBo.isApplyDate == '1' ){
              calendarBo.change = 'applyDate'; // 设置为申购日
              aliveItemClass = 'date-alive-item3';
            } else if (calendarBo.isRedeemDate == '1' ){
              calendarBo.change = 'redeemDate';
              aliveItemClass = 'date-alive-item4';
            }
            if (this.lstPeriodDate['openBeginDate'] == calendarBo.sysDate ){
              // 开放开始日
              //this.addOnceSettedDate(calendarBo.sysDate, '开放开始日', 'openBeginDate', 'openDate', aliveItemClass)
            } else if (this.lstPeriodDate['openEndDate'] == calendarBo.sysDate){
              // 开放结束日
              //this.addOnceSettedDate(calendarBo.sysDate, '开放结束日', 'openEndDate',  'openDate',aliveItemClass)
            } else {
              // 普通开放日
              if (this.lstSettedDate[calendarBo.sysDate] == null){
                this.lstSettedDate[calendarBo.sysDate] = [];
                this.lstSettedDate[calendarBo.sysDate].calendarNumber = 0;
              }
              this.calendarInfo.push(calendarBo);
              this.addOnceSettedDate(calendarBo.sysDate, '开放日', 'openDate', 'openDate', aliveItemClass);
            }
          }
        });
      },
      /*
      *
      * */
      addOnceSettedDate(sysDate, name, periodSuffix, dateSuffix, itemClass){
        let sysYear = sysDate.substring(0,4);
        let settedDate = {name: name, periodSuffix: periodSuffix, dateSuffix: dateSuffix}
        this.lstSettedDate[sysDate].push(settedDate);
        this.lstSettedDate[sysDate].calendarNumber++;
        let dayIndex = this.lstDayIndexs[sysYear][sysDate];
        let dateEvent = this.MONTHS[sysYear][dayIndex.month][dayIndex.weeks][dayIndex.weekday];
        this.MONTHS[sysYear][dayIndex.month][dayIndex.weeks][dayIndex.weekday].calendarStyle = itemClass;
        this.MONTHS[sysYear][dayIndex.month][dayIndex.weeks][dayIndex.weekday].disable = true;
        this.addCalendarStyle(sysDate, itemClass);
      },

      changedPeriod(item){
        item.openDateOptions.splice(0,item.openDateOptions.length);
        let openRule = item.openRule;
        switch (openRule) {
          case '1':
            item.disabled = true;
            item.openDate = '';
       //     this.orderOpenDays='';
            break;
          case '2':
            item.disabled = false;
            item.openDate = '';
            this.openWeeklyOptions.map(option=>{
              item.openDateOptions.push(option);
            });
            //this.orderOpenDays=item.openDateOptions.get(0);
            break;
          case '3':
            item.disabled = false;
            item.openDate = '';
            this.openMonthlyOptions.map(option=>{
              item.openDateOptions.push(option);
            });
            //this.orderOpenDays=item.openDateOptions.get(0);
            break;
          default:
            break;
        }

      },
      /**
       * 通过传过来的清盘日加载年视图，今年
       */
      createYearCalendar(){
        let endYear = this.windingDate;
        if(!this.windingDate|| this.windingDate === ''||endYear.substring(0,4)<this.currentYear ){
          endYear = this.currentYear;
        }

        if(this.lstWorkday.length > 0){
          let workYear = moment(this.lstWorkday[this.lstWorkday.length - 1]).format('YYYY') ;
          endYear = endYear < workYear ? workYear : endYear;
        }

        for(let year = this.currentYear; year <=  endYear.substring(0,4); year = moment(year).add(1, 'years').format('YYYY')) {
          if(!this.MONTHS[year]) {
            this.MONTHS[year] = {};
            this.months.forEach(month => {
              let currentYearMonth = moment(year + '-' + month).format('YYYY-MM');
              this.createMonthlyCalendar(currentYearMonth, month);
              this.MONTHS[year][month] = this.monthlyDays;
            });
          }
        }

        //设置工作日循环
        this.loadCalendar = true;
        for(let year in this.MONTHS){
          this.months.forEach(month =>{ // 年，循环每月
            this.MONTHS[year][month].forEach(week => { // 月，循环每周
              week.forEach(date => { // 周， 循环每日
                if (this.lstWorkday.indexOf(date.wholeDay) >= 0){
                  date.isWorkday = true;
                } else {
                  date.isWorkday = false;
                }
              });
            });
          });
        }
        setTimeout(() => {
          this.loadCalendar = false;
        }, 500);
      },
      // 删除周期产品中的选项
      removePeriodRow(item){
        for(let idx = 0; idx < this.openRuleItems.length; idx ++){
          if (item == this.openRuleItems[idx]){
            this.openRuleItems.splice(idx, 1);
          }
        }
      },
      // 增加周期产品中的选项
      addPeriodRow(){
        let openItem = { // 周期型产品规则
          number: 1,          // 周期值
          openRule: '1',      // 开放周期规则： 1-天，2-周，3-月
          openDate: '',       // 开放日，如果开放规则是周，则为周一...周天，如果开放规则是月，则为1号...28号，如果开放规则是日，则该值无效
          openType: '',       // 开放类型
          openDateOptions:[],
          disabled: true,
          weekendRule: '1'    // 非工作日处理规则：1-顺延到下一工作日，2-提前到上一工作日， 3-节假日取消
        };
        this.openRuleItems.push(openItem);
      },
      /**
       *控制日历控件边界值和是否可以修改
       */
      changeRang(){
        for(let item of this.prodModeItems[this.prodMode]) {//这是外循环，控制谁需要改范围。也是该产品有什么周期
          let minDate = this.rang[item.suffix].min;//获取默认值或者上一次的值
          let maxDate = this.rang[item.suffix].max;//获取默认值或者上一次的值
          let wdLength =  this.lstWorkday.length;
          if(wdLength >0){
            maxDate = this.lstWorkday[wdLength - 1];
          }
          let minDateArray = item.minDate.split(",");//获取应该比对的边界值  小于
          let maxDateArray = item.maxDate.split(",");//获取应该比对的边界值  大于
          let includeMin = item.includeMinDate;//获取是否可以与边界值相等
          let includeMax = item.includeMaxDate;//获取是否可以与边界值相等
          for(let key of minDateArray){
            if(this.lstPeriodDate.isequalDate && this.lstPeriodDate.isequalDate == 1 &&( key == "endDate" || key == "establishDate"|| key == "valueDate")){//隐藏时不作为边界
              continue;
            }
            if(this.lstPeriodDate[key]&&this.lstPeriodDate[key] > moment().format('YYYYMMDD') ){//查询边界值最小值
              minDate = this.lstPeriodDate[key];
              break;
            }
          }
          for(let key of maxDateArray){//查询边界值最大值
            if(this.lstPeriodDate.isequalDate && this.lstPeriodDate.isequalDate == 1 &&( key == "endDate" || key == "establishDate"|| key == "valueDate" )){//隐藏时不作为边界
              continue;
            }
            if(this.lstPeriodDate[key]){
              maxDate = this.lstPeriodDate[key];
              break;
            }
          }
          if(this.lstPeriodDate[item.suffix] && this.lstPeriodDate[item.suffix].length>7 &&this.lstPeriodDate[item.suffix] < moment().format('YYYYMMDD')){//小于今天不让修改日历
            this.rang[item.suffix].disabled = true;
          }
          this.rang[item.suffix].min = includeMin == false && minDate.length == 8  ?"(" + minDate: minDate ;
          this.rang[item.suffix].max = includeMax == false && maxDate.length == 8  ? maxDate + ")" : maxDate ;
          //是否展示开放日日历 star
          let showCalFlag = true;
          for(let item of this.prodModeItems[this.prodMode]){
            if(!this.lstPeriodDate[item.suffix]){
              showCalFlag = false;
              break;
            }
          }
          if(this.prodMode == '1'){
            showCalFlag = false;
          }
          this.showCalendarFlag = showCalFlag;
          //是否展示开放日日历 end
        }
        //是否展示成立日与到期日
        if(this.lstPeriodDate.isequalDate && this.lstPeriodDate.isequalDate == 1 ){
          this.rang.openBeginDate.showStatus=false;
          this.rang.openEndDate.showStatus = false;
          this.rang.valueDate.showStatus = false;
          this.rang.establishDate.max = this.rang.openBeginDate.max;
          this.rang.endDate.min = this.rang.openEndDate.min;
        }
      },
      changeOpenShow(val){
        if(val == null){
          this.$set(this.lstPeriodDate,'isequalDate','0');
        }else{
          this.$set(this.lstPeriodDate,'isequalDate',val);
        }
      },
      // 返回数据
      returnData(){
        let returnData = {};
        // 只要是非货币净值产品，起息日与产品成立日默认为同一天
        returnData.t8ProdPeriod = this.lstPeriodDate;
        returnData.T8ProdCalendar = this.calendarInfo;
        returnData.t8OpenPeriodInfo = this.openRuleItems;
        return returnData;
      },

      validateData(){
        return this.$refs.periodInfo.validate();
        // let blankDate = '';
        // let validateResult = true; // 校验结果
        // for(let item of this.prodModeItems[this.prodMode]){
        //   if(this.lstPeriodDate[item.suffix]){
        //     item.selected=true;
        //   }
        //   if (!item.selected){
        //     blankDate = blankDate + "," + item.name ;
        //     if (validateResult){
        //       validateResult = false;
        //     }
        //   }
        // }
        // if (!validateResult){
        //   this.validateResult = blankDate.slice(1) +'未设置';
        // }
        // return validateResult;
      },
      initData(){
        this.lstPeriodDate  = {}; // 设置日期参数，例如{"subsBeginDate": "20200202"}
        this.lstSettedDate = {};// 设置日期事件，例如{"20200202": [{name:认购日}]}
        this.calendarInfo=[];
        this.MONTHS = {};
      },

      initialCalendar(){
        this.loadDate = new Date().getTime();
        this.queryProdProgram();
      },
      changeCalendar(newVal,oldVal,dateSuffix,calendarName,className){
        if(oldVal) {
          let index = this.lstDayIndexs[oldVal.substring(0,4)][oldVal];
          if(this.MONTHS[oldVal.substring(0,4)]){
            this.cancelOnceDate(this.MONTHS[oldVal.substring(0,4)][index.month][index.weeks][index.weekday], dateSuffix, '');
          }
        }
        if(newVal){
          let dayIndex = this.lstDayIndexs[newVal.substring(0,4)][newVal];
          let date = this.MONTHS[newVal.substring(0,4)][dayIndex.month][dayIndex.weeks][dayIndex.weekday];
          date.disable = true;
          date.wholeDay = newVal;
          this.setOnceDate(date, dateSuffix, dateSuffix, calendarName,className);
        }
        this.changeRang();
      },
      resetOpenDate(){
        if(this.lstPeriodDate){
          let openBeginDate = this.lstPeriodDate['openBeginDate'];  // 设置的开放开始日
          let openEndDate = this.lstPeriodDate['openEndDate'];      // 设置的开放结束日
          if(openBeginDate && openEndDate){
            this.cancelPeriodDate(openBeginDate, openEndDate, "openDate");
            this.$nextTick(()=> { this.setOpenDate(null, openBeginDate, openEndDate);})
          }
        }
      },
    },
    created(){
      this.createYearCalendar();
      // for(let item of this.prodModeItems[this.prodMode]) {//这是外循环，控制谁需要改范围。也是该产品有什么周期
      //   this.rang[item.suffix].showStatus=true;
      // }
    },

    mounted(){

    },

    watch: {
      t8ProdInfoId:function(){
        this.T8ProdCalendar.t8ProdInfoId = this.t8ProdInfoId;
      },
      prodMode: function(){
        //console.log("watch prodMode value is: " + this.prodMode);
      },
      prodCode: function(){
        //console.log("watch prodCode value is: " + this.prodCode);
      },
      "T8ProdCalendar.pgmno": function(){
        //console.log("watch pgmno value is: " + this.T8ProdCalendar.pgmno);
        this.initData ();
        this.initialCalendar();
      },
      "lstPeriodDate.subsBeginDate":function(val,oldVal){
        let subsEndDate =this.lstPeriodDate["subsEndDate"];
        this.$set(this.T8ProdCalendar,"subsBeginDate",val);
        if(oldVal && subsEndDate){
          this.cancelPeriodDate(oldVal, subsEndDate, "subsDate");
        }
        this.changeCalendar(val,oldVal,"subsDate","认购起始日",'date-alive-item1');
        //取消每个开放日
        //添加开放日
        if(val && subsEndDate){
          this.setSubsDate( val, subsEndDate);
        }
      },
      "lstPeriodDate.subsEndDate":function(val,oldVal){
        //取消每个开放日
        let subsBeginDate =this.lstPeriodDate["subsBeginDate"];
        //添加开放日
        if(oldVal && subsBeginDate){
          this.cancelPeriodDate(subsBeginDate,oldVal , "subsDate");
        }
        this.changeCalendar(val,oldVal,"subsDate","认购结束日",'date-alive-item1');
        //添加开放日
        if(val && subsBeginDate){
          this.setSubsDate( subsBeginDate, val);
        }
      },
      "lstPeriodDate.establishDate":function(val,oldVal){
        this.changeCalendar(val,oldVal,"establishDate","成立日","");
        this.$set(this.T8ProdCalendar,"establishDate",val);
        if(this.lstPeriodDate.isequalDate == '1'){
          this.$set(this.lstPeriodDate,"valueDate",val);
          this.$set(this.lstPeriodDate,"openBeginDate",val);
        }
        if (this.prodMode != '3'){
          this.$set(this.lstPeriodDate,"valueDate",val);
        }
      },
      "lstPeriodDate.valueDate":function(val,oldVal){
        this.changeCalendar(val,oldVal,"valueDate","起息日","");
      },
      "lstPeriodDate.openBeginDate":function(val,oldVal){
        //取消每个开放日
        let openEndDate =this.lstPeriodDate["openEndDate"];
        if(oldVal && openEndDate){
          this.cancelPeriodDate(oldVal, openEndDate, "openDate");
        }
        this.changeCalendar(val,oldVal,"openDate","开放开始日","date-alive-item5");
        //添加开放日
        if(val && openEndDate){
          this.setOpenDate(null, val, openEndDate);
        }
      },
      "lstPeriodDate.openEndDate":function(val,oldVal){
        //取消每个开放日
        let openBeginDate = this.lstPeriodDate["openBeginDate"];
        if(oldVal && openBeginDate){
          this.cancelPeriodDate(openBeginDate, oldVal, "openDate");
        }
        this.changeCalendar(val,oldVal,"openDate","开放结束日","date-alive-item5");
        //添加开放日
        if(val && openBeginDate){
          this.setOpenDate(null, openBeginDate, val);
        }
      },
      "lstPeriodDate.windingDate":function(val,oldVal){
        this.changeCalendar(val,oldVal,"windingDate","清盘日","date-alive-item7");
      },
      "lstPeriodDate.endDate":function(val,oldVal){
        this.changeCalendar(val,oldVal,"endDate","到期日","date-alive-item6");
        this.$set(this.T8ProdCalendar,"endDate",val);
        if(this.lstPeriodDate.isequalDate == '1'){
          this.$set(this.lstPeriodDate,"openEndDate",val);
        }
      },
      "lstPeriodDate.isequalDate":function(val,oldVal){
        //   val ==1 切换到相等
        if( val == '1' ){
          this.rang.openBeginDate.showStatus=false;
          this.rang.openEndDate.showStatus = false;
          this.rang.valueDate.showStatus = false;
          this.$set(this.lstPeriodDate,"valueDate", this.lstPeriodDate.establishDate);
          this.$set(this.lstPeriodDate,"openBeginDate",this.lstPeriodDate.establishDate);
          this.$set(this.lstPeriodDate,"openEndDate",this.lstPeriodDate.endDate);
          this.$set(this.T8ProdCalendar,"establishDate",this.lstPeriodDate.establishDate);
          this.$set(this.T8ProdCalendar,"endDate",this.lstPeriodDate.endDate);
        }else {//val !=1 为不相等 展示隐藏
          if(this.prodMode!='1'){
            this.rang.openBeginDate.showStatus = true;
            this.rang.openEndDate.showStatus = true;
          }
          if (this.prodMode == "3") {
            this.rang.valueDate.showStatus = true;
          }
        }
        if(val=='0' && oldVal){
          if(this.lstPeriodDate.openBeginDate &&  this.lstPeriodDate.openEndDate){
            this.cancelPeriodDate(this.lstPeriodDate.openBeginDate,this.lstPeriodDate.openEndDate, "openDate");
          }
          this.$set(this.lstPeriodDate,"openBeginDate","");
          this.$set(this.lstPeriodDate,"openEndDate","");
          if(this.prodMode == "3"){
            this.$set(this.lstPeriodDate,"valueDate","");
          }
        }

      },

    },

  }
</script>

<style lang="scss" scoped>

  @import "../../../../styles/T81001-ProdCalendar.scss";

</style>
