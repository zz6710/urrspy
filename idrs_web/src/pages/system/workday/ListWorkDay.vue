<template>
  <div class="all workday" v-if="hackReset" v-loading="loading">
    <div class="top">
      <div class="options">
        <div style="display: inline-block;margin-right: 32px;">
          <span class="circle"
                style="background-color:#41A0EB;border:1px solid rgb(114, 214, 169)"> &nbsp;</span>
          <small style="font-size:12px">工作日</small>
          <span class="circle" style="background-color:rgb(114, 214, 169);border:1px solid #96bff5">&nbsp;</span>
          <small style="font-size:12px">增加</small>
          <span class="circle" style="background-color:#f4b9b8;border:1px solid #f4b9b8"> &nbsp;</span>
          <small style="font-size:12px">删除</small>
        </div>
        <el-button style="margin-right:18px" @click="updateWorkdays" type="warning" icon="el-icon-date"
                   size="mini">初始化
        </el-button>
        <el-button style="margin-right:18px" @click="saveWorkdays" type="success" icon="el-icon-check"
                   size="mini">保存
        </el-button>
      </div>
      <div class="year-select">
        <div class="fc-button-group">
          <el-button class="fc-button-primary" @click="pickPre(currentYear,currentMonth)">&nbsp;&lt;&nbsp;</el-button>
          <k-field-select v-model="currentYear" :data-data="yearSelectList" style="width: 100px;"
                          data-display-field="label" data-value-field="value"
                          @data-on-change="refreshData"></k-field-select>
          <el-button class="fc-button-primary" @click="pickNext(currentYear,currentMonth)">&nbsp;&gt;&nbsp;</el-button>
        </div>
      </div>
    </div>
    <div style="height: 500px; overflow-y: auto">
      <div id="calendar" v-for="(item,index) in MONTHS" :key="index">
        <div class="month">
          <ul>
            <li class="year-month">
              <span class="choose-month">{{ index }}月</span>
            </li>
          </ul>
        </div>
        <ul class="weekdays">
          <li>一</li>
          <li>二</li>
          <li>三</li>
          <li>四</li>
          <li>五</li>
          <li style="color:red">六</li>
          <li style="color:red">七</li>
        </ul>
        <ul class="days">
          <li v-for="(day,i) in item" :key="i">
            <el-checkbox-group v-model="checkList" size="mini" >
              <el-checkbox-button @change="changeWorkdays(day,i,index)" :class="isWeekend(i)"
                                  v-if="day.getMonth()+1 == index" :label="JSON.stringify(day).substr(1,10)">
                {{day.getDate()}}
              </el-checkbox-button>
            </el-checkbox-group>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>
<script type="text/javascript">
  import kayak from "@/frame/kayak.js";
  import {merge} from "lodash";

  export default {
    name: "EditMchtSign",
    props: {
      pgmno: ''
    },
    data: function() {
      return {
        hackReset: true,
        loading: true,
        currentDay: 1,
        currentMonth: 1,
        currentYear: new Date().getFullYear(),
        currentWeek: 1,
        days: [],
        yearSelectList: [],
        months: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
        MONTHS: {},
        checkList: [],
        action: {
          programNo: "",
          workdays: []
        },
        listActions: {
          workday: {
            programNo: "2019"
          },
          offset: 0,
          limit: 356
        },
        workdays: {
          data: [],
          total: ""
        },
        before: [],
        today: new Date().Format("yyyyMMdd"),
        CURRENTYEAR: new Date().getFullYear(),
        checkListExample: ["不可选", "工作日"],
        aa: [],
        label: [],
        copyLabel: []
      };
    },
    watch:{
      pgmno(newValue,oldValue){
        if (newValue) {
          this.listWorkdays();
          this.initData(null);
          this.initYearSelectList();
        }
      }
    },
    methods: {
      isWeekend(i) {
        return (i + 1)%7 == 0 || (i + 1)%7 == 6? 'bk-red' : '';
      },
      listWorkdays() {
        this.loading = true;
        this.aa = []
        this.checkList = [];
        this.httpUtil
          .comnQuery({
            action: "WorkdayItem.find",
            params: {"pgmno": this.pgmno, "workday":this.currentYear}
          })
          .then(result => {
            this.workdays.data = result.rows
            this.workdays.data.forEach(selectDay => {
              selectDay.workday = this.formatSelectNumber(selectDay.workday);
              this.checkList.push(selectDay.workday);
              this.aa.push(selectDay.workday);
            });
            setTimeout(() => {
              this.loading = false;
            }, 500);
          });
      },
      initYearSelectList() {
        let startYear = this.currentYear - 10;
        this.yearSelectList.length = 0;
        for (let i = startYear; i < startYear + 20; i++) {
          this.yearSelectList.push({
            "label": i,
            "value": i
          })
        }
      },
      initData: function(cur) {
        this.months.forEach(month => {
          var d = new Date(this.formatDate(this.currentYear, month, 1));
          var date = new Date(d);
          this.currentDay = date.getDate();
          this.currentYear = date.getFullYear();

          this.currentMonth = date.getMonth() + 1;
          this.currentWeek = date.getDay(); // 1...6,0
          if (this.currentWeek == 0) {
            this.currentWeek = 7;
          }
          var str = this.formatDate(
            this.currentYear,
            this.currentMonth,
            this.currentDay
          );
          // this.MONTHS[month].length = 0;
          // // 今天是周日，放在第一行第7个位置，前面6个
          this.MONTHS[month] = [];
          for (var i = this.currentWeek - 1; i >= 0; i--) {
            var d = new Date(str);
            d.setDate(d.getDate() - i);
            this.MONTHS[month].push(d);
          }
          for (var i = 1; i <= 42 - this.currentWeek; i++) {
            var d = new Date(str);
            d.setDate(d.getDate() + i);
            this.MONTHS[month].push(d);
          }
        });
      },
      refreshData() {
        this.checkList = [];
        this.listWorkdays();
        this.initData();
        let label = document.getElementsByClassName("el-checkbox-button--mini");
        for (var i = 0; i < label.length; i++) {
          if (
            label[i].lastChild.className == "add" ||
            label[i].lastChild.className == "delete"
          ) {
            label[i].lastChild.className = "el-checkbox-button__inner";
          }
        }
      },
      pickPre: function(year, month) {
        this.currentYear = this.currentYear - 1;
        this.refreshData();
      },
      pickNext: function(year, month) {
        this.currentYear = this.currentYear + 1;
        this.refreshData();
      },
      // 返回 类似 2016-01-02 格式的字符串
      formatDate: function(year, month, day) {
        var y = year;
        var m = month;
        if (m < 10) m = "0" + m;
        var d = day;
        if (d < 10) d = "0" + d;
        return y + "-" + m + "-" + d;
      },
      formatSelectNumber(str) {
        return (
          str.substring(0, 4) +
          "-" +
          str.substring(4, 6) +
          "-" +
          str.substring(6, 8)
        );
      },
      // 返回 类似 20160102 格式的字符串
      formatNumber(num) {
        return num.split("-").join("");
      },
      prodWorkdays(){
        this.httpUtil.comnQuery({
          action: "T8ProdWorkdays.saveProdDaysByWorkDays",
          params: {"pgmno": this.pgmno,},
        }).then(data => {

        });
      },
      saveWorkdays() {
        // this.loading = true;
        // checkList数组去重
        let temp = [];
        let deleteArr = [];
        let label = document.getElementsByClassName("el-checkbox-button--mini");
        for (let i = 0; i < label.length; i++) {
          if (label[i].lastChild.className == "delete") {
            deleteArr.push(label[i].firstChild.value);
          }
        }
        for (let i = 0; i < this.checkList.length; i++) {
          if (temp.indexOf(this.checkList[i]) == -1) {
            if(deleteArr.indexOf(this.checkList[i])==-1){
              temp.push(this.checkList[i]);
            }
          }
        }


        this.checkList = temp;
        // ..........................
        this.action.programNo = String(this.currentYear);
        this.action.workdays = ''
        this.checkList.forEach(selectDay => {
          this.action.workdays = this.action.workdays + this.formatNumber(selectDay) + ','
        });
        this.httpUtil.comnUpdate({
          action: "WorkdayItemSave.save",
          // TODO
          params: {"pgmno": this.pgmno, "year": String(this.currentYear), "workdays": this.action.workdays},
          mask: true,
          dataAfterSuccess: this.dataAfterSuccess
        }).then(data => {
          if(data.success){
            //保存完，更新所有产品开放日（主要用于当前日期以后得修改及每年设置一次工作日）
            //this.prodWorkdays();//信披报送系统无需维护产品工作日
          }

          //重新反显
          this.action.workdays = '';
          this.listWorkdays();

          // 保存还原样式
          let label = document.getElementsByClassName("el-checkbox-button--mini");
          for (var i = 0; i < label.length; i++) {
            if (
              label[i].lastChild.className == "add" ||
              label[i].lastChild.className == "delete"
            ) {
              label[i].lastChild.className = "el-checkbox-button__inner";
            }
          }

          this.hackReset = false; //销毁组件
          this.$nextTick(() => {
            this.hackReset = true; //重建组件
            this.aa = [];
            this.checkList = [];
            this.httpUtil
              .comnQuery({
                action: "WorkdayItem.find",
                params: {"pgmno": this.pgmno, "workday":this.currentYear}
              })
              .then(result => {
                this.workdays.data = result.rows
                this.workdays.data.forEach(selectDay => {
                  selectDay.workday = this.formatSelectNumber(selectDay.workday);
                  this.checkList.push(selectDay.workday);
                  this.aa.push(selectDay.workday);
                });
              });
          });
        })
      },
      updateWorkdays() {
        for (var MONTH in this.MONTHS) {
          this.MONTHS[MONTH].forEach(day => {
            var d = new Date(
              this.formatDate(this.currentYear, MONTH, day.getDate())
            );
            var date;
            date = new Date(d);
            this.currentDay = date.getDate();
            this.currentYear = date.getFullYear();
            this.currentMonth = date.getMonth() + 1;
            var str = this.formatDate(
              this.currentYear,
              this.currentMonth,
              this.currentDay
            );
            var dateStr = str;
            var myDate = new Date(Date.parse(dateStr.replace(/-/g, "/")));
            if (
              myDate.getDay() == "1" ||
              myDate.getDay() == "2" ||
              myDate.getDay() == "3" ||
              myDate.getDay() == "4" ||
              myDate.getDay() == "5"
            ) {
              this.checkList.push(str);
              this.aa.push(str);
            }
            if (myDate.getDay() == "6" || myDate.getDay() == "0") {
              // 过期不可更改
              if (this.checkList.indexOf(str) > -1) {
                this.checkList.splice(this.checkList.indexOf(str), 1);
                this.aa.splice(this.checkList.indexOf(str), 1);
              }
            }
          });
        }
        // checkList数组去重
        var temp = [];
        for (var i = 0; i < this.checkList.length; i++) {
          if (temp.indexOf(this.checkList[i]) == -1) {
            temp.push(this.checkList[i]);
          }
        }
        this.checkList = temp;
        // this.saveWorkdays();
      },
      // 新增删除样式
      changeWorkdays(day, i, index) {
        if (this.aa.indexOf(day.Format("yyyy-MM-dd")) > -1) {
          let label = document.getElementsByClassName("el-checkbox-button--mini");
          for (var i = 0; i < label.length; i++) {
            if (label[i].firstChild.value == day.Format("yyyy-MM-dd")) {
              if (label[i].lastChild.className === "el-checkbox-button__inner") {
                label[i].lastChild.className = "delete";
              } else if (label[i].lastChild.className === "delete") {
                label[i].lastChild.className = "el-checkbox-button__inner";
              }
            }
          }
        }
        if (this.aa.indexOf(day.Format("yyyy-MM-dd")) == -1) {
          let label = document.getElementsByClassName("el-checkbox-button--mini");
          for (var i = 0; i < label.length; i++) {
            if (label[i].firstChild.value == day.Format("yyyy-MM-dd")) {
              if (label[i].lastChild.className === "el-checkbox-button__inner") {
                label[i].lastChild.className = "add";
              } else if (label[i].lastChild.className === "add") {
                label[i].lastChild.className = "el-checkbox-button__inner";
              }
            }
          }
        }
      }
    }
  };
</script>
<style lang="scss" scoped>

</style>
