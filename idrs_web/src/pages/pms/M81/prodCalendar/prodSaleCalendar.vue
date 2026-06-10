<template>

  <div class="md-card k-card md-theme-default" v-loading="loading" style="min-width: 1200px;">
    <div class="md-card-header md-card-header-text md-card-header-green">
      <div class="card-icon" :style="iconStyle">
        <i class="md-icon md-icon-font md-theme-default">assignment</i>
      </div>

      <div class="pc-calendar">

        <div class="fc-toolbar fc-header-toolbar">

          <div class="fc-left">
            <div class="md-autocomplete">
              <k-form ref="periodInfo" :data-col="1" dataLabelWidth="70px" dataInputWidth="300px">
                <k-form-item label="产品代码">
                  <k-field-select v-model="prodCode" data-action="T8ProdInfo.findT8ProdInfos"
                                  data-display-field="prodCode,prodName"
                                  data-value-field="prodCode" @data-on-change="prodCodeChanged"></k-field-select>
                </k-form-item>
                <k-form-item label="代码回收">
                  <k-field-select v-model="isRecycleCode" data-dict="1yes0no" @data-on-change="prodIsRecycleCodeChanged"></k-field-select>
                </k-form-item>
              </k-form>

              <!--                  <md-autocomplete v-model="prodCode" :md-options="lstProdCode"-->
              <!--                          @md-changed="queryProdCodes(prodCode)" @md-selected="prodCodeChanged" :md-open-on-focus="true">-->
              <!--                      <label>产品代码</label>-->
              <!--                  </md-autocomplete>-->


            </div>
          </div>

          <div class="fc-center">

            <div class="fc-button-group">
              <el-button class="fc-button-primary" @click="prev">&nbsp;&lt;&nbsp;</el-button>
              <h2>&nbsp;</h2>
              <!--                  数据字典需要更新，年份用数组更好，取前后十年得年份，-->
              <!--                  <k-field-select v-model="showHeader.year" data-data="years" @data-on-change="skipDay" :dataClearable="false" ></k-field-select>-->
              <k-field-select v-model="showHeader.year" data-display-field="label" data-value-field="value"
                              style="width: 80px;"
                              @data-on-change="skipDay" :dataClearable="false"
                              :data-data="yearSelectList"></k-field-select>
              <span style="padding-left:10px;margin-top: 5px">年</span>
              <k-field-select v-show="showMonth" v-model="showHeader.month" data-dict="t8_months"
                              @data-on-change="skipDay" style="width: 75px; padding-left:10px"
                              :dataClearable="false"></k-field-select>
              <span style="padding-left:10px;margin-top: 5px">月</span>
              <h2>&nbsp;</h2>
              <el-button class="fc-button-primary" @click="next">&nbsp;&gt;&nbsp;</el-button>
            </div>
          </div>

          <div class="fc-right" style="width: 360px">


            <div class="fc-button-group">
              <!--                  <el-button class="fc-button-primary" @click="switchToday">今天</el-button>-->
              <el-button class="fc-button-primary" :class="showYear ? 'fc-button-active' : ''" @click="switchYear">
                &nbsp;年&nbsp;
              </el-button>
              <el-button class="fc-button-primary" :class="showMonth ? 'fc-button-active' : ''" @click="switchMonth">
                &nbsp;月&nbsp;
              </el-button>
              <!-- <el-button class="fc-button-primary" :class="showWeek ? 'fc-button-active' : ''" @click="switchWeek">&nbsp周&nbsp</el-button> -->
              <!-- <el-button class="fc-button-primary" :class="showList ? 'fc-button-active' : ''" @click="switchList">&nbsp日&nbsp</el-button> -->
            </div>
          </div>

        </div>

      </div>
    </div>

    <div class="md-card-content">

      <div class="fc-view-container">
        <div class="fc-view fc-dayGridMonth-view fc-dayGrid-view">

          <!-- 年视图-->
          <table class="year-table" cellspacing="0" cellpadding="0" v-show="showYear">

            <div class="year-calendar" v-for="(_month,index) in MONTHS" :key="index">
              <div class="pcy-month" @click="switchMonthByYear(index)">
                <ul>
                  <span>{{ index }} 月</span>
                </ul>
              </div>
              <!-- <thead class="pc-year-thread">
                <th>周一</th>
                <th>周二</th>
                <th>周三</th>
                <th>周四</th>
                <th>周五</th>
                <th>周六</th>
                <th>周日</th>
              </thead> -->
              <tbody style="display: inline">
              <!-- 双重for循环生成 6 * 7 = 42 个日期格子 -->
              <tr class="year-month" v-for="(week, weekIndex) in _month" v-bind:key="weekIndex">
                <td v-for="(date, dateIndex) in _month[weekIndex]"
                    v-bind:key="dateIndex"
                    @mouseenter="mouseOnYearView(index, weekIndex, dateIndex, date)"
                    @mouseleave="mouseOutYearView(index, weekIndex, dateIndex, date)"
                >

                  <!-- 显示事件的小三角形 -->
                  <div v-show="date.yearEvent != null" :class="[date.yearEvent, date.class]"></div>
                  <!-- 显示日期 -->
                  <div class="year-date" :class="[date.class, date.isAlive]">
                    <span>{{date.day}}</span>
                  </div>

                  <div v-show="date.yearEvent != null && date.wholeDay == showDayListHeader && showDayList"
                       class="fc-popover fc-more-popover "
                       :class="[date.class]"
                       style="margin-top: -14px; margin-left: 33px; padding-bottom: 6px">
                    <div class="fc-header fc-widget-header">
                      <span class="fc-title">{{date.wholeDay}}</span>
                      <!-- <span class="fc-close fc-icon fc-icon-x" @click="closeDayEventList"></span> -->
                    </div>

              <tbody class="fc-body fc-widget-content">
              <ul class="pc-event-dialog" v-for="(event,eventIndex) in date.wholeEvent" :key="eventIndex">
                <div :class="[event.class]">
                  <span class="fc-time">{{event.prodCode}}</span>
                  <span class="fc-title">{{event.eventName}}</span>
                </div>

              </ul>
              </tbody>
            </div>
            </td>
            </tr>
            </tbody>
        </div>

        </table>
      </div>

      <!-- 月视图-->
      <div class="fc-view-container">
        <div class="fc-view fc-dayGridMonth-view fc-dayGrid-view">

          <table class="month-table" cellspacing="0" cellpadding="0" v-show="showMonth">
            <thead>
            <th>周一</th>
            <th>周二</th>
            <th>周三</th>
            <th>周四</th>
            <th>周五</th>
            <th class="weekend">周六</th>
            <th class="weekend">周日</th>
            </thead>
            <tbody>
            <!-- 双重for循环生成 6 * 7 = 42 个日期格子 -->
            <tr v-for="(week, weekIndex) in monthlyDays" v-bind:key="weekIndex">
              <td v-for="(date, dateIndex) in monthlyDays[weekIndex]" v-bind:key="dateIndex"
                  @mouseenter="mouseOnMonthView(weekIndex, dateIndex)"
                  @mouseleave="mouseOutMonthView(weekIndex, dateIndex)">
                <div class="date" :class="[date.class, date.isAlive]">
                  <span>{{date.day}}</span>
                  <ul class="pc-event" v-for="(event,eventIndex) in monthlyDays[weekIndex][dateIndex].event"
                      :key="eventIndex">
                    <div :class="[event.class]" @click="link2Page(event)">
                      <span class="fc-time">{{event.prodCode}}</span>
                      <span class="fc-title">{{event.eventName}}</span>
                    </div>

                  </ul>
                  <div>
                    <a v-show="date.hasMore" class="fc-more" @click="showMonthlyEventList(date)">展开 {{date.more}} 条</a>
                  </div>

                </div>

                <div v-show="date.monthMore && date.wholeDay == showDayListHeader && showDayList"
                     class="fc-popover fc-more-popover "
                     style="margin-top: -117px; margin-left: -1px; padding-bottom: 6px">

                  <div class="fc-header fc-widget-header">
                    <span class="fc-title">{{date.wholeDay}}</span>
                    <span class="fc-close fc-icon fc-icon-x" @click="closeDayEventList"></span>
                  </div>

            <tbody class="fc-body fc-widget-content">
            <ul class="pc-event-dialog" v-for="(event,eventIndex) in monthlyDays[weekIndex][dateIndex].wholeEvent"
                :key="eventIndex"
            >
              <div :class="[event.class]" @click="link2Page(event)">
                <span class="fc-time">{{event.prodCode}}</span>
                <span class="fc-title">{{event.eventName}}</span>
              </div>

            </ul>
            </tbody>
        </div>
        </td>
        </tr>
        </tbody>
        </table>

      </div>

    </div>

    <!-- 周视图、 生成一个周 时间列表 -->
    <table class="week-table" cellspacing="0" cellpadding="0" v-show="showWeek">
      <thead>
      <th></th>
      <th v-for="(header, index) in weekTableHeader" v-bind:key="index" :class="header.class">{{header.date}}</th>
      </thead>
      <tbody>
      <tr v-for="(hourMinute, index) in weekList[0]" v-bind:key="index">
        <td>
          <div class="hours">
            <span>{{hourMinute}}</span>
          </div>
        </td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      </tbody>
    </table>

  </div>

  </div>

  </div>


</template>

<script>
  import moment from 'moment';


  var YEAR_VIEW_CONSTANT = '1';
  var MONTH_VIEW_CONSTANT = '0';

  export default {
    name: "prodOpenCalendar",
    data() {
      return {
        today: moment().format('YYYY-MM-DD'),
        currentYearMonth: moment().format('YYYY-MM'),
        currentYear: moment().format('YYYY'),
        showHeader: {
          year: moment().format('YYYY'),
          month: moment().format('MM')
        },
        weekTableHeader: [],
        monthlyDays: [],      // 用于存在月视图
        yearlyDays: [],       // 用于存在年视图
        tmpMonthlyDays: [],    // 用于存放临时月视图
        showYear: false,
        showMonth: true,      // 默认显示月视图
        showWeek: false,
        showList: false,
        weekList: [],
        headerContent: moment().format('YYYY-MM'),
        currentWeekday: '',
        months: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
        MONTHS: {},
        lstEventDates: {},     // 列表存在事件的天数
        lstEvent: {},          // 列表事件[显示所有]
        lstEventShort: {},      // 列表时间【用于月视图中，显示三个】
        showDayListHeader: '',
        showDayList: false,
        loading: true,
        prodCode: null,
        isRecycleCode:'0',
        lstProdCode: [],
        yearSelectList: [],

      }
    },

    methods: {

      initYearSelectList() {//前十年和后十年
        let startYear = this.currentYear - 10;
        this.yearSelectList.length = 0;
        for (let i = startYear; i < startYear + 20; i++) {
          this.yearSelectList.push({
            "label": i,
            "value": i
          })
        }
      },
      // 点击上个月 通过改变currentYearMonth来获取上个月的 YYYY-MM 格式
      prev() {
        if (this.showMonth) {
          this.currentYearMonth = moment(this.currentYearMonth).subtract(1, 'months').format('YYYY-MM')
          this.headerContent = this.currentYearMonth
          this.createMonthCalendar()
        } else if (this.showYear) {
          this.currentYear = moment(this.currentYear).subtract(1, 'years').format('YYYY');
          this.headerContent = this.currentYear;
          this.createYearCalendar();
        } else {
          this.currentWeekday = moment(this.currentWeekday).subtract(7, 'days')
          this.weekHeaderContent()
          this.createWeekList()
        }

        this.setShowHeader();
      },

      next() {
        if (this.showMonth) {
          this.currentYearMonth = moment(this.currentYearMonth).add(1, 'months').format('YYYY-MM')
          this.headerContent = this.currentYearMonth
          this.createMonthCalendar()
        } else if (this.showYear) {
          this.currentYear = moment(this.currentYear).add(1, 'years').format('YYYY');
          this.headerContent = this.currentYear;
          this.createYearCalendar();
        } else {
          this.currentWeekday = moment(this.currentWeekday).add(7, 'days')
          this.weekHeaderContent()
          this.createWeekList()
        }

        this.setShowHeader();
      },

      /**
       * 通过年视图，点击月份，跳转到月视图
       */
      switchMonthByYear(month) {

        this.currentYearMonth = moment(this.currentYear + '-' + month).format('YYYY-MM');

        this.switchMonth();
        this.headerContent = this.currentYearMonth

        this.setShowHeader();

        this.createMonthCalendar()

      },

      setShowHeader() {

        if (this.showYear) {

          this.showHeader.year = this.currentYear;

        } else if (this.showMonth) {

          this.showHeader.year = moment(this.currentYearMonth, 'YYYY-MM').format('YYYY');
          this.showHeader.month = moment(this.currentYearMonth, 'YYYY-MM').format('MM');

        } else if (this.showWeek) {

        }
      },

      skipDay() {

        if (this.showYear) {
          // 月份格式为： YYYY
          this.currentYear = this.showHeader.year;
        } else if (this.showMonth) {
          // 月份格式为： YYYY-MM
          this.currentYearMonth = this.showHeader.year + '-' + this.showHeader.month;

        } else if (this.showWeek) {


        } else if (this.showList) {

          // TODO 暂时不需要

        } else {
          console.log("Do Nothing...")
        }

        this.switchDay();

      },


      switchDay() {

        if (this.showYear) {

          this.headerContent = this.currentYear;

          this.createYearCalendar();

        } else if (this.showMonth) {
          this.headerContent = this.currentYearMonth

          this.createMonthCalendar()

        } else if (this.showWeek) {

          this.weekHeaderContent()
          this.createWeekList()

        } else if (this.showList) {

          // TODO 暂时不需要

        } else {
          console.log("Do Nothing...")
        }

        this.setShowHeader();

      },

      prodCodeChanged(prodCode) {
        this.prodCode = prodCode;
        this.switchDay();
      },
      prodIsRecycleCodeChanged(isRecycleCode) {
        this.isRecycleCode = isRecycleCode;
        this.switchDay();
      },

      /**
       * 切换到当日视图，此按钮，需要根据不同的视图，切换到对应当日的视图中
       * 例如：月视图中，会自动切换到当月
       *       年视图中，会自动切换到当年
       *       日视图中，会自动切换到当日
       */
      switchToday() {

        if (this.showYear) {
          this.currentYear = moment().format('YYYY');

        } else if (this.showMonth) {
          this.currentYearMonth = moment().format('YYYY-MM');

        } else if (this.showWeek) {
          let weekday = moment().weekday() === 0 ? 7 : moment().weekday()
          let daysDistance = 1 - weekday
          this.currentWeekday = moment().add(daysDistance, 'days')

        } else if (this.showList) {

          // TODO 暂时不需要
        } else {
          console.log("Do Nothing...")
        }

        this.switchDay();
      },

      /**
       * 切换到年视图
       */
      switchYear() {

        this.showYear = true
        this.showMonth = false
        this.showWeek = false
        this.showList = false

        this.headerContent = this.currentYear
        this.skipDay()

      },

      /**
       * 切换到月视图
       */
      switchMonth() {
        this.showYear = false
        this.showMonth = true
        this.showWeek = false
        this.showList = false
        this.headerContent = this.currentYearMonth
        this.skipDay()
      },

      /**
       * 切换到周视图
       */
      switchWeek() {

        this.showYear = false
        this.showMonth = false
        this.showWeek = true
        this.showList = false

        this.weekHeaderContent()
      },

      /**
       * 切换到日视图，显示日事件
       */
      switchList() {

        this.showYear = false
        this.showMonth = false
        this.showWeek = false
        this.showList = true

        this.listHeaderContent()
      },

      /**
       * 月视图中，鼠标滑动的效果
       */
      mouseOnMonthView(weekIndex, dateIndex) {
        this.monthlyDays[weekIndex][dateIndex].isAlive = 'date-alive'
      },

      /**
       * 月视图中，鼠标滑动的效果
       */
      mouseOutMonthView(weekIndex, dateIndex) {
        this.monthlyDays[weekIndex][dateIndex].isAlive = ''
      },

      // 年视图鼠标事件
      mouseOnYearView(index, weekIndex, dateIndex, date) {
        this.MONTHS[index][weekIndex][dateIndex].isAlive = 'date-alive'

        if (date.wholeEvent != null) {
          this.showDayList = true;
          this.showDayListHeader = date.wholeDay;
        } else {
          this.showDayList = false;
          this.showDayListHeader = '';
        }
      },

      // 年视图鼠标事件
      mouseOutYearView(index, weekIndex, dateIndex, date) {
        this.MONTHS[index][weekIndex][dateIndex].isAlive = ''

        this.showDayList = false;
        this.showDayListHeader = '';


      },

      showMonthlyEventList(date) {
        date.monthMore = true;
        this.showDayListHeader = date.wholeDay;
        this.showDayList = true;

      },

      closeDayEventList() {

        this.showDayListHeader = '';
        this.showDayList = false;
      },

      // 创建年视图
      createYearCalendar() {
        this.MONTHS = {};

        const tmpCurrentYearMonth = this.currentYearMonth;

        this.months.forEach(month => {

          this.currentYearMonth = moment(this.currentYear + '-' + month).format('YYYY-MM');

          this.createMonthlyCalendar(YEAR_VIEW_CONSTANT);

          this.MONTHS[month] = this.yearlyDays;

        });
        this.headerContent = this.currentYear;
        this.currentYearMonth = tmpCurrentYearMonth;

        // 获取整年最小，最大日期
        this.getYearlyQueryDate();
        // 获取一整年的所有事件
        this.queryProdCalendar(YEAR_VIEW_CONSTANT);

      },

      createMonthCalendar() {

        this.createMonthlyCalendar(MONTH_VIEW_CONSTANT)

        // 获取整月最小，最大日期
        this.getMonthlyQueryDate();
        // 获取一整月的所有事件
        this.queryProdCalendar(MONTH_VIEW_CONSTANT);
      },


      /**
       * 创建月视图
       * @param view  1-year视图， 0- month视图
       */
      createMonthlyCalendar(view) {

        this.tmpMonthlyDays = [];

        // 获取当月的一号是星期几 以便来生成上月的日期 填补够42个格子
        const monthFirstDay = moment(this.currentYearMonth + '-01', 'YYYY-MM-DD')

        // 获得一号与第一个格子内应该有的天数距离 这里需要注意的是 weekday 是从周日 为 0 开始的
        let firstDayWeekday = moment(monthFirstDay).weekday()
        if (firstDayWeekday === 0) {
          firstDayWeekday = 7
        }
        let daysDistance = 1 - firstDayWeekday


        for (let weeks = 0; weeks < 6; weeks++) {
          this.tmpMonthlyDays.push([])
          for (let weekday = 0; weekday < 7; weekday++) {
            // 该对象有两个属性 一个是class属性 还有一个就是日期
            let date = {}
            date.day = moment(monthFirstDay).add(daysDistance, 'days')
            date.isAlive = ''

            const dayMonth = moment(date.day).month()
            // 是这个月的日期
            if (dayMonth === moment(monthFirstDay).month()) {
              date.class = 'current-month '
              // 日期是今天的高亮
              if (moment(date.day).isSame(this.today, 'days')) {
                date.class += 'today '
              }
            } else {
              date.class = 'not-current-month '
            }
            let dayIdx = date.day.day();

            if (dayIdx == 0 || dayIdx == 6) { // 设置周六、周天
              date.class += 'weekend '
            }

            date.wholeDay = moment(date.day).format('YYYYMMDD')

            date.day = moment(date.day).format('D')

            this.tmpMonthlyDays[weeks].push(date)
            daysDistance++

          }

          if (view == '1') {
            // 赋值给年视图日期
            this.yearlyDays = this.tmpMonthlyDays;
          } else {
            // 月视图
            this.monthlyDays = this.tmpMonthlyDays;
          }

        }

      },

      createWeekList() {
        this.weekTableHeader = ['周一', '周二', '周三', '周四', '周五', '周六', '周天']
        this.weekList = []
        for (let index = 0; index < 7; index++) {
          let item = {}
          item.date = moment(this.currentWeekday).add(index, 'days')
          if (item.date.isSame(this.today, 'days')) {
            item.class = 'today'
          }
          item.date = this.weekTableHeader[index] + item.date.format('MM/DD')
          this.weekTableHeader[index] = item
        }
        for (let days = 0; days < 7; days++) {
          this.weekList.push([])
          let item = ''
          for (let hours = 0; hours < 48; hours++) {
            if (hours % 2 === 0) {
              item = hours / 2 + '点'
            } else {
              item = ''
            }
            this.weekList[days].push(item)
          }
        }
      },

      weekHeaderContent() {
        this.headerContent = moment(this.currentWeekday).format('MM/DD') + '~' + moment(this.currentWeekday).add(6, 'days').format('MM/DD')
      },

      listHeaderContent() {
        this.headerContent = moment(this.currentWeekday).format('YYYY-MM-DD');
      },

      // 获取月份的最大最小日期
      getMonthlyQueryDate() {
        if (this.currentYearMonth == null || this.currentYearMonth == '') {
          this.currentYearMonth = moment().format('YYYY-MM');
        }

        this.minSysDate = moment(this.currentYearMonth).startOf('month').format('YYYYMMDD');
        this.maxSysDate = moment(this.currentYearMonth).endOf('month').format('YYYYMMDD');
      },

      // 获取月份的最大最小日期
      getYearlyQueryDate() {
        if (this.currentYear == null || this.currentYear == '') {
          this.currentYear = moment().format('YYYY');
        }

        this.minSysDate = moment(this.currentYear).startOf('year').format('YYYYMMDD');
        this.maxSysDate = moment(this.currentYear).endOf('year').format('YYYYMMDD');
      },

      // 获取周的最大最小日期
      getWeeklyQueryDate() {
        if (this.currentWeekday == null || this.currentWeekday == '') {
          this.currentWeekday = moment().format('YYYY-MM-DD');
        }

        this.minSysDate = moment(this.currentWeekday).startOf('week').format('YYYYMMDD');
        this.maxSysDate = moment(this.currentWeekday).endOf('week').format('YYYYMMDD');
      },

      /**
       * view YEAR_VIEW_CONSTANT-年视图， MONTH_VIEW_CONSTANT- 月视图
       */
      queryProdCalendar(view) {

        this.loading = true;
        this.lstEventShort = [];
        this.lstEvent = [];


        this.httpUtil.comnQuery({
          action: 'SalesCalendar.findT81002',
          params: {
            prodCode: this.prodCode,
            isRecycleCode:this.isRecycleCode,
            minSysDate: this.minSysDate,
            maxSysDate: this.maxSysDate
          }
        }).then(data => {

          var rows_ = data.rows;

          rows_.forEach(event => {
            if(event.prodMode===''||event.prodMode===null||event.prodMode===undefined){
              return ;
            }

            if (event.raiseStartDate == '1') {
              this.putEvents2Lst(event, '募集起始', 'event-apply ', '/main/pms/M84/M84015')

            }
            if (event.raiseEndDate == '1') {
              this.putEvents2Lst(event, '募集结束', 'event-redeem ', '/main/pms/M84/M84015')
            }

            if (event.establishDate == '1') {
              this.putEvents2Lst(event, '  成立', 'event-establish ', '/main/pms/M84/M84015')
            }

            if (event.openDate == '1' && event.prodMode!='2' && event.prodMode!='4') {
              this.putEvents2Lst(event, '  开放', 'event-open ', '/main/pms/M84/M84015')

            }
            if (event.closeInvestDate == '1') {
              this.putEvents2Lst(event, '  封闭期投资日', 'event-apply ', '/main/pms/M84/M84015')
            }

            if (event.endDate == '1') {
              this.putEvents2Lst(event, '  到期', 'event-winding ', '/main/pms/M81/prodInfoGD/M81001-ProdExpiration')
            }

          });

          if (view == YEAR_VIEW_CONSTANT) { // 年视图
            this.months.forEach(month => { // 年，循环每月

              this.MONTHS[month].forEach(week => { // 月，循环每周

                week.forEach(date => { // 周， 循环每日

                  if (this.lstEvent[date.wholeDay] != null) {

                    // 当两者记录条数不相同时，月视图中，需要显示“More+”字样
                    if (this.lstEvent[date.wholeDay].length > this.lstEventShort[date.wholeDay].length) {

                      date.wholeEvent = this.lstEvent[date.wholeDay];
                      date.event = this.lstEventShort[date.wholeDay];
                      date.hasMore = true;
                    } else {

                      date.wholeEvent = this.lstEvent[date.wholeDay];
                      date.event = this.lstEvent[date.wholeDay];
                      date.hasMore = false;
                    }

                    date.yearEvent = 'year-event '
                  }

                });
              });

            });

          } else if (view == MONTH_VIEW_CONSTANT) {  // 月视图

            this.monthlyDays.forEach(week => { // 月，循环每周

              week.forEach(date => { // 周， 循环每日

                if (this.lstEvent[date.wholeDay] != null) {

                  // 当两者记录条数不相同时，月视图中，需要显示“More+”字样
                  if (this.lstEvent[date.wholeDay].length > this.lstEventShort[date.wholeDay].length) {
                    // console.log("more.... ")

                    date.wholeEvent = this.lstEvent[date.wholeDay];
                    date.event = this.lstEventShort[date.wholeDay];
                    date.hasMore = true;
                    date.more = this.lstEvent[date.wholeDay].length - this.lstEventShort[date.wholeDay].length;

                  } else {

                    // console.log("less.... ")
                    date.wholeEvent = this.lstEvent[date.wholeDay];
                    date.event = this.lstEvent[date.wholeDay];
                    date.hasMore = false;
                    date.more = 0;
                  }

                  date.yearEvent = 'year-event '
                }

              });
            });
          }

          setTimeout(() => {
            this.loading = false;
          }, 500);

        });

      },

      putEvents2Lst(event, eventName, eventClass, linkPath) {

        let eventBO = {};

        eventBO.prodCode = event.prodCode;
        eventBO.eventName = eventName;
        eventBO.class = eventClass;

        if (linkPath != null && linkPath != '') {
          eventBO.linkPath = linkPath;
        }

        let sysdate = event.sysDate;

        if (this.lstEvent[sysdate] == null) {
          this.lstEvent[sysdate] = [];
        }

        if (this.lstEventShort[sysdate] == null) {
          this.lstEventShort[sysdate] = [];
        }

        for (let e of this.lstEvent[sysdate]) {

          if (e.prodCode == eventBO.prodCode
            && e.eventName == eventBO.eventName
            && e.class == eventBO.class) {
            return;
          }

        }

        // 当小于3条记录时，全显示,
        this.lstEvent[sysdate].push(eventBO);
        if (this.lstEvent[sysdate].length <= 3) {
          this.lstEventShort[sysdate].push(eventBO);
        }
      },

      // queryProdCodes(prodCode) {
      //
      //   // 产品代码为空时，要查询所有，且不去后台查询产品下拉框
      //   if (prodCode == null || prodCode == '') {
      //     this.prodCodeChanged();
      //     return;
      //   }
      //
      //   this.httpUtil.comnQuery({
      //     action: 'T81002.findTaProdCodeList',
      //     params: {prodCode: prodCode}
      //   }).then(data => {
      //
      //     var rows_ = data.rows;
      //
      //     this.lstProdCode = [];
      //
      //     rows_.forEach(prodInfo => {
      //
      //       this.lstProdCode.push(prodInfo.prodCode);
      //
      //     });
      //
      //   });
      //
      // },

      link2Page(event) {
        console.log("prodCode=:>>",event.prodCode);
        this.$router.push({
          path: event.linkPath,
          query: {prodCode: event.prodCode}
        });

      }
    },


    created() {
      // v-show是在创建后 保留dom 只是做切换的 不会v-if那样 销毁dom 所以只需要创建一次
      let weekday = moment().weekday() === 0 ? 7 : moment().weekday()
      let daysDistance = 1 - weekday
      this.currentWeekday = moment().add(daysDistance, 'days')
      this.createWeekList()
      this.createYearCalendar();
      this.createMonthCalendar()

      this.switchDay();
      this.initYearSelectList();

    },
    computed: {
      iconStyle() {
        let iconStyle = {};
        iconStyle.background = this.$store.state.system.cardBackground
        return iconStyle;
      }
    },
  }
</script>

<style lang="scss" scoped>

  @import "../../../../styles/calendar.scss";


  ::v-deep .el-loading-mask {
    z-index: 1000;
  }


</style>
