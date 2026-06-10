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
        return ['#7bede8']
      }
    },
    axisData: {
      type: Array,
      default: ()=>{
        return []
      }
    },
    seriesData: {
      type: Array,
      default: ()=>{
        return [
          {
            type: 'bar',
            data: [110, 200, 50, 60, 100]
          }
        ]
      }
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
    seriesData: {
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
        const data = JSON.parse(JSON.stringify(item.data)) || []
        series.push({
          type: item.type,
          data: data.reverse(),
          barWidth: 10,
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
        legend: {},
        tooltip: {
          trigger: 'item',
          axisPointer: {
            type: 'none'
          },
          textStyle: {
            fontSize: 12
          }
        },
        grid: {
          top: 10,
          left: 50,
          right: 35,
          bottom: 10
        },
        label: {
          show: true,
          color: '#fff',
          position: 'right',
          width: 60,
          overflow: 'breakAll'
        },
        xAxis: {
          type: 'value',
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
          axisLabel: {
            show: false,
            color: '#fff',
            fontSize: 10
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(255,255,255,0)'
            }
          },
        },
        yAxis: {
          type: 'category',
          name: '',
          axisLabel: {
            color: 'rgba(255,255,255,0.6)',
            fontSize: 10
          },
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
          data: _this.axisData.reverse()
        },
        itemStyle: {
          borderRadius: 4,
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
