<template>
  <el-time-picker ref="elTimePicker" :value="value" :disabled="dataDisabled === true || dataDisabled === 'true'" @input="handleInput"
                  @change="handleChange" @blur="handleBlur" @focus="handleFocus" :value-format="dataValueFormat"
                  :picker-options="{selectableRange: cSelectableRange}"/>
</template>

<script>
  import props from "@/components/k-element/common/k-field-props.js";
  import event from "@/components/k-element/common/k-field-event.js";
  import emitter from "@/components/k-element/common/k-emitter.js";


  export default {
    name: 'KFieldTime',
    mixins: [props(), event(), emitter()],
    props: {
      dataMinValue: {
        type: String,
      },
      dataMaxValue: {
        type: String,
      },
      dataValueFormat: {
        type: String,
        default: 'HHmmss'
      },
      dataValidate: {
        type: Function
      }
    },
    data() {
      return {
      }
    },
    computed: {
      cSelectableRange() {
        let dataMinValue = this.dataMinValue

        let minDate
        if (!dataMinValue) {
          minDate = new Date(2020,1,0,0,0)
        } else if (dataMinValue.startsWith('(')) {
          minDate = new Date(this.formateDate(dataMinValue, 1).getTime() + 1000)
        } else {
          minDate = new Date(this.formateDate(dataMinValue, 1))
        }

        let dataMaxValue = this.dataMaxValue;
        let maxDate
        if (!dataMaxValue) {
          maxDate = new Date(2020,1,1,23,59,59)
        }else if (dataMaxValue.endsWith(')')) {
          maxDate = new Date(this.formateDate(dataMaxValue, 0).getTime() - 1000)
        } else {
          maxDate = new Date(this.formateDate(dataMaxValue, 0).getTime())
        }
        return minDate.getHours() + ":" + minDate.getMinutes() + ":" + minDate.getSeconds()
          + ' - ' +  maxDate.getHours() + ":" + maxDate.getMinutes() + ":" + maxDate.getSeconds()
      }
    },
    methods: {
      focus(){
        this.$refs.elTimePicker.focus()
      },
      doReset() {
        this.value = ''
      },
      formateDate(value, startIndex) {
        let hour = this.getTime(value, 'H')
        if (hour == -1) {
          hour = this.getTime(value, 'h')
        }
        let minute = this.getTime(value, 'm')
        if (minute == -1) {
          minute = this.getTime(value, 'M')
        }
        let second = this.getTime(value, 's')
        if (second == -1) {
          second = this.getTime(value, 'S')
        }
        let date = new Date();
        date.setHours(hour)
        date.setMinutes(minute)
        date.setSeconds(second)
        return date
      },
      getTime(value, str){
        const dataValueFormat = this.dataValueFormat;
        let first = dataValueFormat.indexOf(str)
        if (first == -1) {
          return -1;
        }
        let lastIndex = dataValueFormat.lastIndexOf(str)

        let length
        if (first == lastIndex) {
          length = 1
        } else {
          length = 2
        }

        if (value.startsWith('(')) {
          first++
        }

        return value.substr(first, length)
      }


    }
  };
</script>
