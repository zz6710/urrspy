<template>
  <div class="calendar-info">
    <div class="tabs">
      <div
        class="tab-item"
        :class="currentTabLabel == item.label ? 'active' : ''"
        v-for="item in tabs"
        :key="item.label"
        @click="change(item)"
      >{{item.label}}</div>
    </div>
    <div class="calendar">
      <el-calendar
        v-model="calendarValue"
      >
        <template
          slot="dateCell"
          slot-scope="{data}">
          <div @click="dayClick(data, calendarData[Number(data.day.split('-').slice(2).join(''))])" v-if="data.type=='current-month'">
            <span
              class="tip-count"
              v-if="data.type=='current-month' &&
              calendarData[Number(data.day.split('-').slice(2).join(''))]"
            >
              {{calendarData[Number(data.day.split('-').slice(2).join(''))]}}
            </span>
            <p :class="data.isSelected ? 'is-selected' : ''">
              {{ Number(data.day.split('-').slice(2).join('')) }}
            </p>
          </div>
        </template>
      </el-calendar>
    </div>
  </div>
</template>
<script>
import moment from 'moment'
export default {
  data() {
    return {
      currentTabLabel: '产品日历',
      tabs: [
        {
          label: '产品日历'
        },
        {
          label: '销售日历'
        }
      ],
      calendarValue: new Date(),
      calendarData: {}
    }
  },
  watch: {
    calendarValue: {
      handler(v, o) {
        const m1 = moment(v).format('MM')
        const m2 = moment(o).format('MM')
        console.log(m1, m2);
        if (m1 != m2) {
          this.getCalendarData()
        }
      },
    }
  },
  mounted() {
    this.getCalendarData()
  },
  methods: {
    change(item) {
      this.currentTabLabel = item.label
      this.getCalendarData()
    },
    getCalendarData() {
      if (this.currentTabLabel == '产品日历') {
        this.prodCalendar()
      } else {
        this.saleCalendar()
      }
    },
    dayClick(data, count) {
      console.log("data",data);
      console.log("count",count);
      let year = data.day.split('-')[0];
      if (!count) return
      setTimeout(()=>{
        let path = ''
        if (this.currentTabLabel == '产品日历') {
          path = '/main/pms/M81/prodCalendar/prodOpenCalendar'
        } else {
          path = '/main/pms/M81/prodCalendar/prodSaleCalendar'
        }
        this.$router.push({
          path,
          query: {
            year:year,
            month: data.day.split('-')[1]
          }
        });
      }, 500)
    },
    isShow(day) {
      return Math.random() > 0.6
    },
    getRandom() {
      let count = Math.random() * (15 - 1) + 1
      return Math.floor(count)
    },
    prodCalendar() {
      this.httpUtil
				.comnQuery({
					action: "T8ProdPeriodDays.findProdCalendar",
					params: {
            calendarStartDate: moment(this.calendarValue).startOf('month').format('YYYYMMDD'),
            calendarEndDate: moment(this.calendarValue).endOf('month').format('YYYYMMDD'),
          },
				})
				.then((data) => {
          this.calendarData = data.rows[0].listOpenInfo[0]
				});
    },
    saleCalendar() {
      this.httpUtil
				.comnQuery({
					action: "T8ProdPeriodDays.findSaleCalendar",
					params: {
            calendarStartDate: moment(this.calendarValue).startOf('month').format('YYYYMMDD'),
            calendarEndDate: moment(this.calendarValue).endOf('month').format('YYYYMMDD'),
          },
				})
				.then((data) => {
          this.calendarData = data.rows[0].listOpenInfo[0]
				});
    }
  }
}
</script>
<style lang="scss" scoped>
.calendar-info {
  .tabs {
    display: flex;
    margin-top: 20px;
    .tab-item {
      font-size: 14px;
      line-height: 30px;
      margin-right: 20px;
      cursor: pointer;
      &.active {
        color: #00bcd4;
        font-weight: bold;
        border-bottom: 1px solid #00bcd4;
      }
    }
  }
  .el-calendar {
    text-align: center;
     .el-calendar-table  {
      .el-calendar-day {
        height: auto;
        position: relative;
        .tip-count {
          position: absolute;
          left: 25px;
          top: 1px;
          font-size: 12px;
          color: #fff;
          background: #00bcd4;
          // background: #ff9e00;
          border-radius: 8px;
          height: 14px;
          line-height: 14px;
          padding: 0 4px;
        }
      }
      p {
        margin: 0;
        width: 30px;
        height: 30px;
        line-height: 30px;
      }
      td {
        border: none;
        &:hover {
          .el-calendar-day {
            background: none;
          }
          p {
            background: rgba(0,188,212,0.1);
            border-radius: 50%;
          }
        }
        &.is-selected {
          background: transparent;
          // color: #fff;
          .tip-count {
            // background: #fff;
            // color: #00bcd4;
            // color: #ff9e00;
            // border: 1px solid #00bcd4;
            // border: 1px solid #ff9e00;;
          }
          &:hover {
            .el-calendar-day {
              background: none;
            }
          }
          p {
            background: rgba(0,188,212,0.1);
            // background: #ff9e00;
            border-radius: 50%;
          }
        }
        &.prev {
          pointer-events: none;
        }
        &.next {
          pointer-events: none;
        }
      }
    }
  }
}
</style>
