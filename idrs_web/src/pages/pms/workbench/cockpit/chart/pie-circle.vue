<template>
  <div>
    <div class="chart-area" ref="chartArea"></div>
  </div>
</template>
<script>
export default {
  props: {
    color: {
      type: Array,
      default: () => {
        return ['#e84c79', '#f6c04b', '#e7985d', '#3e37e2', '#2a6fe0', '#234282', '#a54bc5']
      }
    },
    pieData: {
      type: Object,
      default: () => {
        return {}
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
    pieData: {
      handler(v) {
        if (!v.data) return
        this.$nextTick(() => {
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
      this.echartElem = this.$echarts.init(this.$refs.chartArea)
      const option = {
        tooltip: {
          trigger: 'item',
          textStyle: {
            fontSize: 12
          }
        },
        legend: {
          show: false,
        },
        color: _this.color,
        series: [
          {
            name: '',
            type: 'pie',
            roseType: 'area',
            radius: ['10%', '70%'],
            label: {
              color: '#fff',
              alignTo: 'edge',
              edgeDistance: 0,
              width: 100,
              formatter: function (item) {
                return [
                  `{a|${item.name}} {b|${item.value}%}`,
                  ` {b|${item.data.amount}${_this.pieData.unit}}`,
                ].join('\n')
              },
              rich: {
                a: {
                  color: '#77c4ce',
                  fontSize: 10,
                  fontWeight: 600
                },
                b: {
                  color: '#fff',
                  fontSize: 10,
                  fontWeight: 600,
                }
              }
            },
            emphasis: {
              label: {}
            },
            labelLine: {
              showAbove: false,
              length: 5,
            },
            labelLayout: function (params) {
              const isLeft = params.labelRect.x < _this.echartElem.getWidth() / 2;
              const points = params.labelLinePoints;
              // Update the end point.
              points[2][0] = isLeft
                ? params.labelRect.x
                : params.labelRect.x + params.labelRect.width;
              return {
                labelLinePoints: points
              };
            },
            data: _this.pieData.data
          }
        ]
      };
      this.echartElem.setOption(option, true)
    }
  }
}
</script>
<style lang="scss" scoped>
.chart-area {
  width: 100%;
  height: 180px;
}
</style>
