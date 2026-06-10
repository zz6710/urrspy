<template>
  <div class="cockpit" :key="pageKey">
    <CockpitTitle class="cockpit-title" />
    <div class="cockpit-container">
      <div class="part part1">
        <div class="part1-1">
          <div class="part1-1-pie1">
            <div class="chart-area">
              <div class="name">产品情况分析</div>
            </div>
          </div>
          <div class="part1-1-pie2">
            <ChartPart btnName="产品规模统计" :hasExport="true">
              <div slot="select">
                <CockpitSelect />
              </div>
              <PieCircle :pieData="pieData" />
            </ChartPart>
          </div>
          <div class="part1-1-bar3">
            <ChartPart btnName="产品存续统计">
              <BarReverse
                :color="productStastic.color"
                :axisData="productStastic.axisData"
                :seriesData="productStastic.seriesData"
              />
            </ChartPart>
          </div>
        </div>
        <div class="part1-2">
          <div class="part1-2-1 sale-info">
            <div class="sale-info-title">
              <ChartPart btnName="报送合格率" type="type2" btnAlign="center">
                <div class="sale-info-container">
                  <div class="gauge-box" v-for="item in saleInfoList" :key="item.name">
                    <Gauge :data="item" />
                  </div>
                </div>
              </ChartPart>
            </div>
          </div>
          <div class="center-bg">
            <img :src="require('@/assets/img/cockpit/bg.png')" />
          </div>
          <div class="part1-2-2">
            <ChartPart btnName="总量分析" type="type2" btnAlign="center">
              <div class="analysis">
                <div class="card-box" v-for="item in cardList" :key="item.title">
                  <Card  :data="item" />
                </div>
              </div>
            </ChartPart>
          </div>
        </div>
        <div class="part1-3">
          <div class="part1-3-bar1">
            <div class="chart-area">
              <div class="name">资产情况分析</div>
            </div>
          </div>
          <div class="part1-3-bar2">
            <ChartPart btnName="资产占比情况" :hasExport="true">
              <div slot="select">
                <CockpitSelect />
              </div>
              <PieCircle :pieData="pieData" />
            </ChartPart>
          </div>
          <div class="part1-3-bar3">
            <ChartPart btnName="各机构持有量统计">
              <Bar
                :color="reportData.color"
                :axisData="reportData.axisData"
                :seriesData="reportData.seriesData"
              />
            </ChartPart>
          </div>
        </div>
      </div>
      <div class="part part2">
        <ChartPart btnName="穿透前后债券分布情况" type="type2" btnAlign="center">
          <Bar
            :color="oneYearData.color"
            :axisData="oneYearData.axisData"
            :seriesData="oneYearData.seriesData"
            :hasLegend="true"
          />
        </ChartPart>
      </div>
    </div>
  </div>
</template>
<script>
import CockpitTitle from '@/pages/pms/workbench/cockpit/component/title.vue'
import PieCircle from '@/pages/pms/workbench/cockpit/chart/pie-circle.vue'
import Bar from '@/pages/pms/workbench/cockpit/chart/bar.vue'
import BarReverse from '@/pages/pms/workbench/cockpit/chart/bar-reverse.vue'
import Gauge from '@/pages/pms/workbench/cockpit/chart/gauge.vue'
import ChartPart from '@/pages/pms/workbench/cockpit/component/chartPart.vue'
import Card from '@/pages/pms/workbench/cockpit/component/card.vue'
import CockpitSelect from '@/pages/pms/workbench/cockpit/component/select.vue'
import { throttle } from 'lodash'

export default {
  components: {
    CockpitTitle,
    PieCircle,
    Bar,
    BarReverse,
    Gauge,
    ChartPart,
    Card,
    CockpitSelect
  },
  data() {
    return {
      pieData: {
        unit: '亿元',
        data: [
          { value: 32, name: '封闭', amount: 80 },
          { value: 32, name: '定开', amount: 80 },
          { value: 28, name: '货币', amount: 70 },
          { value: 26, name: '滚存', amount: 50 },
        ]
      },
      productStastic: {
        color: ['#7bede8'],
        axisData: ['产品报送', '产品发行', '产品存续', '产品到期'],
        seriesData: [
          {
            type: 'bar',
            data: [21, 50, 60, 90]
          }
        ]
      },

      reportData: {
        color: ['#7bede8'],
        axisData: ['机构1', '机构2', '机构3', '机构4', '机构5', '机构6', '机构7'],
        seriesData: [{
          name: '机构',
          type: 'bar',
          data: [90, 220, 140, 60, 100, 90, 130],
          unit: '只'
        }]
      },
      oneYearData: {
        color: ['#eb4e7a', '#4869ea', '#7bede8'],
        axisData: ['债券1', '债券2', '债券3', '债券4', '债券5', '债券6',
          '债券7', '债券8', '债券9', '债券10'],
        seriesData: [{
          name: '穿透前',
          type: 'bar',
          data: [110, 280, 150, 30, 50, 60, 60, 70, 100, 120]
        },
          {
            name: '穿透后',
            type: 'bar',
            data: [160, 200, 130, 50, 110, 80, 130, 90, 150, 100]
          }]
      },
      cardList: [
        {
          title: '产品总规模',
          unit: '亿元',
          value: "20,015",
          status: 'up',
          percent: '',
          tip: ''
        },
        {
          title: '资产持有规模',
          unit: '亿元',
          value: "30,015",
          status: 'up',
          percent: '',
          tip: ''
        },
        {
          title: '产品存续数量',
          unit: '只',
          value: "152",
          status: 'up',
          percent: '',
          tip: ''
        },
        {
          title: '底层资产占比',
          unit: '%',
          value: "356",
          status: 'up',
          percent: '',
          tip: ''
        }
      ],
      saleInfoList: [
        {
          name: '季度合格率',
          total: 36,
          finish: 35,
          percent: 5.12,
          unit: '次'
        },
        {
          name: '年度合格率',
          total: 145,
          finish: 136,
          percent: 34.51,
          unit: '次'
        },
        {
          name: '月度合格率',
          total: 12,
          finish: 11,
          percent: 108.66,
          unit: '次'
        }
      ],
      pageKey: 0
    }
  },
  methods: {
    setPageKey() {
      console.log('ee', this.pageKey);
      this.pageKey++
    }
  },
  mounted() {
    const _this = this
    window.addEventListener('resize', throttle(()=>{
      if (window.innerWidth > 1100) {
        _this.setPageKey()
      }
    }, 300, { trailing: true }))
  }
}
</script>
<style lang="scss" scoped>
.cockpit {
  width: 100%;
  height: 100%;
  overflow-y: auto;
  min-width: 1100px;
  background: linear-gradient(135deg, #000, #111843, #3e123a);
  user-select: none;
  .cockpit-title {
    width: 80%;
    margin: 0 auto;
  }
  .cockpit-container {
    padding: 30px 15px 10px;
    .part {
      &.part1 {
        display: flex;
        .part1-1 {
          flex: 0.9;
          & > div {
            &:not(:first-child) {
              margin-top: 10px;
            }
          }
        }
        .part1-2 {
          flex: 1.7;
          padding: 0 40px;
          .sale-info {
            .sale-info-container {
              display: flex;
              margin-top: 10px;
              .gauge-box {
                flex: 1;
              }
            }
          }
          .part1-2-2 {
            margin-top: -55px;
            .analysis {
              display: flex;
              flex-wrap: wrap;
              justify-content: center;
              padding: 0 20px;
              margin-top: 30px;
              .card-box {
                width: 48%;
                margin-bottom: 15px;
                &:nth-child(odd) {
                  margin-right: 15px;
                }
              }
            }
          }
        }
        .part1-3 {
          flex: 1;
          & > div {
            &:not(:first-child) {
              margin-top: 10px;
            }
          }
        }
      }
      &.part2 {
        padding: 0 6%;
      }
    }
  }
  .center-bg {
    width: 90%;
    margin: 0 auto;
  }

  .chart-area {
    width: 100%;
    height: 180px;
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
    .name {
      font-size: 24px;
      font-weight: bold;
      color: #fff;
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
    }
    img {
      width: 45%;
    }
  }
}
</style>
