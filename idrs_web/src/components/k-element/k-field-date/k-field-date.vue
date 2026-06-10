<template>
  <el-date-picker
    ref="elDatePicker"
    :value="value"
    :type="dataType"
    :format="dataDateFormat"
    :value-format="dataValueFormat"
    :disabled="dataDisabled === true || dataDisabled === 'true'"
    :picker-options="pickerOptions"
    @change="handleChange"
    @input="handleInput"
    @blur="handleBlur"
    @focus="handleFocus"
    range-separator="-"
    start-placeholder="开始日期"
    end-placeholder="结束日期"
    :clearable="true"
    :placeholder="dataPlaceholder || '选择日期'"
    unlink-panels
  >
  </el-date-picker>
</template>

<script>
  import props from "@/components/k-element/common/k-field-props.js";
  import event from "@/components/k-element/common/k-field-event.js";
  import emitter from "@/components/k-element/common/k-emitter.js";
  import moment from "moment"

  export default {
    name: 'KFieldDate',
    mixins: [props(), event(), emitter()],
    props: {
      dataType: {
        type: String,
        default: 'date'
      },
      dataDateFormat: {
        type: String,
        default: 'yyyy-MM-dd'
      },
      dataValueFormat: {
        type: String,
        default: 'yyyyMMdd'
      },
      dataMaxValue: {
        type: String,
        default: '20991231'
      },
      dataMinValue: {
        type: String,
        default: '19700101'
      },
      dataWorkday: {
        type: [Boolean, String]
      },
      dataWorkdayPgmno: {
        type: String
      },
      dataValidate: {
        type: Function
      }
    },
    data() {
      return {
        temValue: "",
        workdays: {},
        workdaysLastDate: ""
      }
    },
    computed: {
      pickerOptions() {
        let _this = this;

        function range(time) {
          let dataMinValue = _this.dataMinValue
          let dataMaxValue = _this.dataMaxValue
          let minDate
          let maxDate
          if (dataMinValue.startsWith('(')) {
            minDate = new Date(_this.formateDate(dataMinValue, 1)).getTime()
          } else {
            minDate = new Date(_this.formateDate(dataMinValue, 0)).getTime() - 86400000
          }
          if (dataMaxValue.endsWith(')')) {
            maxDate = new Date(_this.formateDate(dataMaxValue, 0)).getTime() - 86400000
          } else {
            maxDate = new Date(_this.formateDate(dataMaxValue, 0)).getTime()
          }
          return time.getTime() >= minDate && time.getTime() <= maxDate
        }

        //添加时间限制,判断是否是本年度期间,超过本年度期间,周期信息去除限制条件
        /*return {
          disabledDate(time) {
            let year = moment().format('yyyy');
            //false 不可选中
            if (_this.dataWorkday) {
              let timeStr = moment(time).format(_this.dataValueFormat.toUpperCase());
              console.log("_this.workdays=:>>>>>",_this.workdays);
              //console.log("timeStr=:>>>>>",timeStr);
              //console.log("_this.workdays[timeStr]=:>>>>>",_this.workdays[timeStr]);
              let currentTimeStr = timeStr.substring(0,4);
              if(currentTimeStr>year ||currentTimeStr<year){
                return (range(time) && _this.workdays[timeStr]);
              }else{
                return !(range(time) && _this.workdays[timeStr]);
              }
            } else {
              return (range(time) && _this.workdays[timeStr]);
            }
          }
        }*/

        return {
          disabledDate(time) {

            //console.log("this.workdaysLastDate=:>>>>",_this.workdaysLastDate);
            if (_this.dataWorkday) {
              let timeStr = moment(time).format(_this.dataValueFormat.toUpperCase());
              let timeStr2 = moment(time).format("yyyyMMdd".toUpperCase()).toString();
              //工作日最后一天日期小于当前日期,表示没有工作日,默认全部可以选中
              if(_this.workdaysLastDate<timeStr2){
                return !range(time);
              }else{
                return !(range(time) && _this.workdays[timeStr]);
              }
            } else {
              return !range(time);
            }
          }
        }
      }
    },
    watch: {
      value: function (value) {
        this.value = value
      }
    },
    mounted() {
      if (this.dataWorkday === true || this.dataWorkday === 'true') {
         this.initWorkday();
      }
    },
    methods: {
      focus(){
        this.$refs.elDatePicker.focus()
      },
      formateDate(value, startIndex) {
        let dateStr = (value.substr(startIndex, 4) + '-' + value.substr(startIndex + 4, 2) + '-' + value.substr(startIndex + 6, 2))
        return dateStr
      },
      initWorkday() {
        if (this.dataWorkdayPgmno) {
          // 产品工作日
          this.doInitWorkday(this.dataWorkdayPgmno);
        } else {
          // 系统工作日，先获取系统工作日编号
          this.httpUtil.comnQuery({
            action: "WorkdayProgram.find",
            params: {"pgmtype": 1}
          }).then(data => {
            let rows = data.rows;
            if (rows && rows.length == 1) {
              this.doInitWorkday(rows[0].pgmno);
            }
          })
        }
      },
      doInitWorkday(pgmno) {
        this.httpUtil.comnQuery({
          action: "WorkdayItem.find",
          params: {"pgmno": pgmno}
        }).then(r => {
          let workdaySetRows = r.rows;
          //记录工作日最后一点日期
          this.workdaysLastDate = workdaySetRows[workdaySetRows.length-1].workday
          for (let i = 0; i < workdaySetRows.length; i++) {
            this.workdays[workdaySetRows[i].workday] = true;
          }
        })
      },
      handleInputCallBack(value) {
        if (this.dataType == "daterange") {
          if (value && value.length == 2) {
            let startDate = value[0];
            let endDate = value[1];
            this.$emit('data-startdate-change', startDate);
            this.$emit('data-enddate-change', endDate);
          } else {
            this.$emit('data-startdate-change', null);
            this.$emit('data-enddate-change', null);
          }
        }
      }
    }
  }
</script>

<style lang="scss">
  @import "./k-field-date.scss";
</style>
