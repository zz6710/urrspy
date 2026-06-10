<template>
  <div class="all workday" v-if="hackReset" v-loading="loading">
    <div class="top">
      <div class="options">
        <div style="display: inline-block;margin-right: 32px;">
          <span class="circle"
                style="background-color:#41A0EB;border:1px solid rgb(114, 214, 169)"> &nbsp;</span>
          <small style="font-size:12px">分红日</small>
          <span class="circle" style="background-color:rgb(114, 214, 169);border:1px solid #96bff5">&nbsp;</span>
          <small style="font-size:12px">增加</small>
          <span class="circle" style="background-color:#f4b9b8;border:1px solid #f4b9b8"> &nbsp;</span>
          <small style="font-size:12px">删除</small>
        </div>
<!--        <el-button style="margin-right:18px" @click="updateWorkdays" type="warning" icon="el-icon-date"-->
<!--                   size="mini">初始化-->
<!--        </el-button>-->
        <el-button style="margin-right:18px" @click="saveWorkdays" type="success" icon="el-icon-check" v-if="this.isShowSave"
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
</template>
<script type="text/javascript">

  import Tools from "../../../../utils/tools";

  export default {
    name: "EditMchtSign",
    props: {
      pgmno: '',
      t8ProdInfoId:'',
      isShowSave:true,
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
            action: "T8ProdBonusDay.find",
            params: {t8ProdInfoId:this.t8ProdInfoId}
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
        var temp = [];
        if (this.checkList.length == 0) {
          Tools.alert("未选择日期。","danger");
          return;
        }
        for (var i = 0; i < this.checkList.length; i++) {
          if (temp.indexOf(this.checkList[i]) == -1) {
            temp.push(this.checkList[i]);
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
          action: "T8ProdBonusDay.saveBonusDay",
          // TODO
          params: {"pgmno": this.pgmno, "year": String(this.currentYear), "workdays": this.action.workdays,t8ProdInfoId: this.t8ProdInfoId},
          mask: true,
          dataAfterSuccess: this.dataAfterSuccess
        }).then(data => {
          // if(data.success){
          //   //保存完，更新所有产品开放日（主要用于当前日期以后得修改及每年设置一次工作日）
          //   this.prodWorkdays();
          // }

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
                action: "T8ProdBonusDay.find",
                params: {t8ProdInfoId:this.t8ProdInfoId}
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
              if (label[i].lastChild.className == "el-checkbox-button__inner") {
                label[i].lastChild.className = "delete";
              } else if (label[i].lastChild.className == "delete") {
                label[i].lastChild.className = "el-checkbox-button__inner";
              }
            }
          }
        }
        if (this.aa.indexOf(day.Format("yyyy-MM-dd")) == -1) {
          let label = document.getElementsByClassName("el-checkbox-button--mini");
          for (var i = 0; i < label.length; i++) {
            if (label[i].firstChild.value == day.Format("yyyy-MM-dd")) {
              if (label[i].lastChild.className == "el-checkbox-button__inner") {
                label[i].lastChild.className = "add";
              } else if (label[i].lastChild.className == "add") {
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
  .year-select {
    width: 300px;
    margin-left: 41%;
    margin-top: -16px;
    margin-bottom: 10px;
  }

  .fc-button-primary {
    font-size: 15px;
    background: #41A0EB;
    box-shadow: 0 4px 4px 0 rgba(213, 236, 255, 0.74);
    border-radius: 21px;
    color: #fff;
  }


  .fc-button-group .fc-button-primary:hover {
    color: #fff;
    background-color: #133a8f;
    border-color: #133a8f;
    font-size: 15px;
  }

  .workday ::v-deep .el-checkbox-group {
    height: 35px;
  }

  .workday ::v-deep .el-checkbox-button__inner {
    border-radius: 100% !important;
    display: inline-block;
    font-size: 20px;
    font-weight: 400;
    padding: 2px !important;
    cursor: pointer;
    width: 35px;
    height: 35px;
    border-radius: 100%;
    line-height: 1.7;
    text-align: center;
    border: none;
    margin-top: 10px;
    margin-bottom: 10px;
  }

  .workday ::v-deep .delete {
    display: inline-block;
    color: white;
    background-color: #f4b9b8;
    font-size: 20px;
    font-weight: 400;
    padding: 2px;
    cursor: pointer;
    width: 35px;
    height: 35px;
    border-radius: 100%;
    line-height: 1.7;
    text-align: center;
    margin-top: 10px;
    margin-bottom: 10px;
  }

  .bk-red ::v-deep span {
    color: red;
  }

  .workday ::v-deep .add {
    display: inline-block;
    color: white;
    background-color: rgb(120, 214, 172);
    font-weight: 400;
    font-size: 20px;
    padding: 2px;
    cursor: pointer;
    width: 35px;
    height: 35px;
    border-radius: 100%;
    line-height: 1.7;
    text-align: center;
    margin-top: 10px;
    margin-bottom: 10px;
  }

  .workday ::v-deep .top {
    height: auto;
  }

  .workday ::v-deep ul {
    list-style-type: none;
  }

  /*.workday ::v-deep body {
    font-family: Verdana, sans-serif;
    background: #e8f0f3;
  }*/

  .workday ::v-deep #calendar {
    float: left;
    width: 32%;
    height: 350px;
    margin: 5px 5px;
  }

  .workday ::v-deep .month {
    width: 100%;
  }

  .workday ::v-deep .month ul {
    text-align: center;
    margin: 0;
    padding: 0;
    display: flex;
    justify-content: space-between;
  }

  .workday ::v-deep .month ul li {
    margin-left: 45%;
  }

  .workday ::v-deep .year-month {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-around;
  }

  /* .year-month:hover {
      background: rgba(150, 2, 12, 0.1);
    } */

  .workday ::v-deep .choose-year {
    padding-left: 20px;
    padding-right: 20px;
  }

  .workday ::v-deep .choose-month {
    text-align: center;
    font-size: 25px;
    margin-top: 4px;
  }

  .workday ::v-deep .month ul li {
    color: #000000;
    font-size: 20px;
    text-transform: uppercase;
    letter-spacing: 3px;
  }

  .workday ::v-deep .weekdays {
    padding: 6px 0;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
    margin-bottom: 0px;
  }

  .workday ::v-deep .weekdays li {
    display: inline-block;
    width: 13.6%;
    text-align: center;
    font-size: 15px;
  }

  .workday ::v-deep .days {
    padding: 0;
    background: #ffffff;
    margin: 0;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
  }

  .workday ::v-deep .days li {
    list-style-type: none;
    display: inline-block;
    width: 14%;
    padding-left: 3%;
    margin-top: 5px;
    font-size: 1rem;
    color: #000;
    border-radius: 18.25em;
  }

  .workday ::v-deep .days li .other-month {
    padding: 5px;
    color: gainsboro;
  }

   .days span:hover {
      background-color: #e1e1e1 !important;
    }

  .workday ::v-deep .el-checkbox__label {
    padding-left: 1px;
  }

  .workday ::v-deep .el-checkbox {
    margin-left: 0px;
  }

  .workday ::v-deep .el-checkbox-button:last-child .el-checkbox-button__inner {
    border-radius: 10%;
    height: 35px !important;
    width: 35px !important;
    margin-top: 10px !important;
    margin-bottom: 10px !important;
  }

  .workday ::v-deep .el-checkbox-button--mini .el-checkbox-button__inner {
    padding: 2px;
  }

  .workday ::v-deep .el-checkbox-button__inner {
    line-height: 1.7;
  }

  .workday ::v-deep .el-checkbox-button.is-checked .el-checkbox-button__inner {
    background-color: #41A0EB;
  }

  .workday ::v-deep .el-checkbox-button.is-disabled .el-checkbox-button__inner {
    color: rgb(59, 59, 59);
    opacity: 0.3;
  }

  .workday ::v-deep .circle {
    display: inline-block;
    width: 20px;
    height: 20px;
    border-radius: 10%;
    text-align: center;
    color: white;
    margin-left: 10px;
  }
  .options {
    display: inline-block;
    float: right;
  }

</style>
