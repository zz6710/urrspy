<template>
<div class="guage">
  <div class="chart-area" ref="chartArea">
  </div>
  <div class="guage-info">
    <div>
      <span class="guage-info-label">{{data.name}}：</span>
      <span class="guage-info-value">{{data.finish}} {{data.unit}}</span>
    </div>
    <div>
      <span class="guage-info-label">已完成量：</span>
      <span class="guage-info-value">{{data.total || 0}} {{data.unit}}</span>
    </div>
  </div>
</div>

</template>
<script>
export default {
  props: {
    color: {
      type: Array,
      default: ()=>{
        return ['#eb4e7a', '#4869ea', '#7bede8']
      }
    },
    data: {
      type: Object,
      default: ()=>{
        return {
          total: 1,
          finish: 0
        }
      }
    }
  },
  data() {
    return {
      echartElem: null
    }
  },
  mounted() {
    // this.initData()
  },
  watch: {
    data: {
      handler(v) {
        if (!v) return
        this.$nextTick(()=>{
          this.initData()
        })
      },
      immediate: true,
      deep: true
    },
  },
  methods: {
    initData() {
      const _this = this
      let percent = this.data.finish / this.data.total * 100
      percent = Number(percent.toFixed(2))
      this.echartElem = this.$echarts.init(this.$refs.chartArea);
      const option = {
        tooltip: {
          textStyle: {
            fontSize: 12
          }
        },
        series: [
          {
            name: _this.data.name,
            type: 'gauge',
            radius: '85%',
            axisLine: {
              lineStyle: {
                width: 2,
                color: [
                  [0.1, '#468c43'],
                  [0.7, '#4794f4'],
                  [1, '#e75f37']
                ]
              }
            },
            axisTick: {
              distance: 0,
              lineStyle: {
                color: 'auto',
              }
            },
            splitLine: {
              distance: 0,
              lineStyle: {
                color: '#fff',
                width: 2
              }
            },
            axisLabel: {
              color: '#fff',
              distance: 6,
              fontSize: 10
            },
            pointer: {
              itemStyle: {
                color: 'auto',
              }
            },
            detail: {
              color: 'auto',
              fontWight: 'bold',
              formatter: function(value) {
                return [
                  `{a|${value}}`+`{b|%}`
                ].join('\n')
              },
              rich: {
                a: {
                  fontSize: 16,
                  fontWeight: 600,
                  verticalAlign: 'bottom'
                },
                b: {
                  fontSize: 10,
                  verticalAlign: 'bottom'
                }
              }
            },
            title: {
              show: false
            },
            min: 0,
            max: 100,
            data: [
              {
                value: percent,
                name: '已完成量'
              }
            ]
          }
        ]
      }
      this.echartElem.setOption(option)
    },
  },
}
</script>
<style lang="scss" scoped>
.chart-area {
  width: 100%;
  height: 200px;
}
.guage-info {
  color: #fff;
  text-align: center;
  font-size: 12px;
  font-weight: bold;
  position: relative;
  top: -30px;
  z-index: 1;
  .guage-info-label {
    color: #9599c6;
    cursor: pointer;
  }
  .guage-info-value {
    color: #e7985d;
    cursor: pointer;
    transition: transform 0.3s;
    display: inline-block;
    &:hover {
      transform: scale(1.2);
    }
  }
}
</style>
