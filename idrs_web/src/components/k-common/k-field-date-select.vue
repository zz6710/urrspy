<template>
  <div class="date-select">
    <el-popover
      trigger="click"
      placement="right"
      :disabled="disable"
      v-model="show"
      @show="dateShow"
    >
      <div class="date-list">
        <div class="date-list-item"
          v-for="item in dateList"
          :key="item.value"
          :class="item.value == dateValue ? 'active' : ''"
          @click="dateSelect(item)"
        >{{item.label}}</div>
        <el-popover
          placement="right"
          trigger="click"
          v-model="customDateShow">
          <div class="date-select-container">
            <p>请选择日期</p>
            <el-date-picker
              v-model="customDateValue"
              type="daterange"
              :unlink-panels="unlinkPanels"
              format="yyyy/MM/dd"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
          </div>
          <div style="text-align: right; margin: 30px 0 0 0;">
            <k-btn class="md-simple" @click="customDateShow = false">取消</k-btn>
            <k-btn class="btn-custom-plain" @click="customDateConfirm">确定</k-btn>
          </div>
          <div
            slot="reference"
            class="date-list-item"
            :class="dateValue == 'custom' ? 'active' : ''"
            @click="customDateBtn">自定义</div>
        </el-popover>

      </div>
      <el-button slot="reference">{{dateRangeLabel}}</el-button>
    </el-popover>
  </div>
</template>
<script>
import moment from 'moment'
export default {
  props: {
    dateList: {
      type: Array,
      default: ()=>{
        return [
          {
            label: '1月',
            value: 1
          },
          {
            label: '3月',
            value: 3
          },
          {
            label: '6月',
            value: 6
          },
          {
            label: '1年',
            value: 12
          },
          {
            label: '3年',
            value: 36
          },
          {
            label: '5年',
            value: 60
          },
          {
            label: '近年来',
            value: -1
          },
          {
            label: '最大',
            value: 0
          }
        ]
      }
    },
    startDate: {
      type: String,
      default: ''
    },
    endDate: {
      type: String,
      default: ''
    },
    value: {
      type: [String, Number],
      default: ''
    },
    unlinkPanels: { // 日期联动
      type: Boolean,
      default: false
    }
  },
  computed: {
    dateRangeLabel() {
      const { startDateValue, endDateValue } = this
      return `${startDateValue.slice(0,4)}/${startDateValue.slice(4,6)}/${startDateValue.slice(6,8)}
                -
              ${endDateValue.slice(0,4)}/${endDateValue.slice(4,6)}/${endDateValue.slice(6,8)}
            `
    }
  },
  data() {
    return {
      disable: false,
      dateValue: '',
      customDateValue: '',
      startDateValue: '',
      endDateValue: '',
      show: false,
      customDateShow: true
    }
  },
  watch: {
    value: {
      handler(v) {
        this.dateValue = v
      },
      immediate: true
    },
    startDate: {
      handler(v) {
        this.startDateValue = v
      },
      immediate: true
    },
    endDate: {
      handler(v) {
        this.endDateValue = v
      },
      immediate: true
    }
  },
  methods: {
    dateShow() {
      if (this.dateValue == 'custom') {
        setTimeout(()=>{
          this.customDateShow = true
        }, 10)
      } else {
        this.customDateShow = false
      }
    },
    dateSelect(item) {
      this.dateValue = item.value
      this.$emit('dateSelect', item)
      this.customDateShow = false
    },
    customDateBtn() {
      this.dateValue = 'custom'
    },
    customDateConfirm() {
      this.show = false
      this.customDateShow = false
      const { customDateValue } = this
      this.startDateValue = moment(customDateValue[0]).format('YYYYMMDD')
      this.endDateValue = moment(customDateValue[1]).format('YYYYMMDD')
      this.$emit('customDateChange',this.startDateValue, this.endDateValue)
    }
  }
}
</script>
<style lang="scss" scoped>
.date-list {
  .date-list-item {
    line-height: 30px;
    color: #409EFF;
    padding: 0 5px;
    text-align: center;
    &:hover {
      color: #fff;
      background: rgba(64,158,255, 0.4);
      font-weight: bold;
    }
    &.active {
      color: #fff;
      background: #409EFF;
      font-weight: bold;
    }
  }
}
</style>
