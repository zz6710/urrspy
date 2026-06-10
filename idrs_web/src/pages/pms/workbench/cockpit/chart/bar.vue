<template>
  <div class="chart-area" ref="chartArea">
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
    axisData: {
      type: Array,
      default: ()=>{
        return ['']
      }
    },
    seriesData: {
      type: Array,
      default: ()=>{
        return [{}]
      }
    },
    hasLegend: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      echartElem: null
    }
  },
  mounted() {
  },
  watch: {
    axisData: {
      handler(v) {
        if (!v || !v.length) return
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
      this.echartElem = this.$echarts.init(this.$refs.chartArea);
      const series = []
      this.seriesData.forEach((item, index)=>{
        series.push({
          name: item.name,
          type: item.type,
          data: item.data,
          barWidth: 8,
          barGap: 0,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowColor: _this.color[index] || _this.color[0]
            }
          },
        })
      })
      const option = {
        legend: {
          show: _this.hasLegend,
          right: 0,
          textStyle: {
            color: 'rgba(255,255,255,0.6)'
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'none'
          },
          textStyle: {
            fontSize: 12
          }
        },
        grid: {
          top: 40,
          left: 30,
          right: 5,
          bottom: 20
        },
        xAxis: {
          type: 'category',
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
          axisLabel: {
            color: '#fff',
            fontSize: 10
          },
          data: _this.axisData
        },
        yAxis: {
          type: 'value',
          name: '单位：亿元',
          axisLabel: {
            color: '#fff'
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(255,255,255,0.1)'
            }
          }
        },
        itemStyle: {
          borderRadius: 2,
        },
        color: _this.color,
        series
      };
      this.echartElem.setOption(option)
    },
  },
}
</script>
<style lang="scss" scoped>
.chart-area {
  width: 100%;
  height: 180px;
}
</style>
