<template>
  <div>
    <k-form-search-customize data-target="navGrid" v-model="formData">
      <k-form-item label="产品代码">
        <k-field-select v-model="formData.prodCode" data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"
                        :dataAllowblank="false"></k-field-select>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="formData.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="基准日期">
        <k-field-date v-model="formData.baseDate" data-type="date" data-date-format="yyyy-MM-dd"
                      :dataAllowblank="false"/>
      </k-form-item>
      <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" @click="saveImg" v-show="false"
             :data-handler="addFrom">
        <md-icon md-src="/static/svg/confirm.svg"></md-icon>保存图片
      </k-btn>
    </k-form-search-customize>


    <div style="background: #ffffff;height: 550px;padding-top: 30px">
      <div id="main" style="width: 1000px;height: 500px; margin: auto"></div>
    </div>

    <k-grid ref="navGrid" data-action='NavMatket.findNavMarketAll1' @data-row-select="selectRow" :data-autoload="false"
            data-operate-column-position="end" data-operate-width="300px" :data-after-load="ajaxGetData"
            data-operate-column="false">
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName" data-width="260"/>
      <k-grid-column data-align="center" data-header="日期" data-name="trandt"/>
      <k-grid-column data-align="center" data-header="净值数据" data-name="nav"/>
      <k-grid-column data-align="center" data-header="市场数据" data-name="market"/>
    </k-grid>

  </div>
</template>

<script>
import {assign} from "lodash";
// import echarts from 'echarts'
import * as echarts from 'echarts';
import Tools from "@/utils/tools";

export default {
  name: '',
  data() {
    return {
      queryParam: {},
      formData: {},
      cascaderValue: [],
      queryParamDateRange: [],
      selectRowData: {},
      fromRowId: '',
      charts: '',
      min: '',
      max: '',
      lineRed:"净值表现",
      lineBlue:"业绩基准",
      splitNumber: '',
      xData: [],
      marketData: [],
      navData: []
    };
  },

  computed: {
    queryParam1() {
      return {
        'prodCode': this.queryParam.prodCode,
        'baseDate': this.queryParam.baseDate,
        /* 'raiseDateStart': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
         'raiseDateEnd': this.queryParamDateRange ? this.queryParamDateRange[1] : null,*/
      }
    }
  },


  methods: {
    saveImg() {
      Tools.alert("图片正在生成中", "info");
      this.httpUtil.ajax({
        url: '/server/form/PmsApp/navMarket/createImg.json',
        params: {
          prodCode: this.queryParam.prodCode,
          baseDate: this.queryParam.baseDate,
          raiseDateStart: this.queryParam1.raiseDateStart,
          raiseDateEnd: this.queryParam1.raiseDateEnd
        }
      }).then(res => {
        if (res.returndata.status == "error") {
          Tools.alert("图片保存失败", "error");
        } else {
          Tools.alert("图片保存成功", "success");
        }
      });


    },
    ajaxGetData() {
      this.httpUtil.ajax({
        url: '/server/form/PmsApp/navMarket/getNavMarketEchartsDate.json',
        params: {
          prodCode: this.formData.prodCode,
          baseDate: this.formData.baseDate
        }
      }).then(res => {
        if (res.returndata.status == "error") {
          Tools.alert("数据获取失败", "error");
        } else {
          this.lineRed = res.returndata.lineRed;
          this.lineBlue = res.returndata.lineBlue;
          //this.splitNumber = res.returndata.splitNumber;
          this.splitNumber = "20";
          this.min = res.returndata.min;
          this.max = res.returndata.max;
          this.xData = res.returndata.xData;
          this.navData = res.returndata.navData;
          this.marketData = res.returndata.marketData;
        }
        this.drawLine('main');
      });
    },
    addFrom() {
      this.formData = {};
    },

    /*   checkInfo(){
         //alert("验证信息!!!");
         console.log(111111)
       },*/
    selectRow(row, column, event) {
      const _this = this
      _this.selectRowData = assign({}, row)
      _this.formData = assign({}, row)
    },
    dataBeforeLoad() {
      return {"excOrgno": "ROOT"}
    },
    statusRender(row) {

    },
    updSuccess(pop) {
      this.$refs.taCustodianBankGrid.load()
      pop.close()
    },
    drawLine(id) {
      this.charts = echarts.init(document.getElementById(id))
      //console.log("this.xData=:>>>>",this.xData);
      let xLength = 8;
      if(this.xData.length>0){
        xLength =  Math.floor(this.xData.length/8);
      }
      this.charts.setOption({
        title: {
          text: '净值表现与业绩基准对比'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          //top:"bottom",
          data: [this.lineRed, this.lineBlue]
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          axisLine: {show: false},
          axisTick: {show: false},
          axisLabel: {
                 rotate: 40,
                 interval: xLength,/*拿到X轴时间平分8份*/
          },
          type: 'category',
          boundaryGap: false,
          data: this.xData,
        },
        yAxis: {
          axisLine: {show: false},
          axisTick: {show: false},
          splitNumber: this.splitNumber,
          type: 'value',
          max: this.max,
          min: this.min,
          axisLabel: {
            show: true,
            interval: 'auto',
            //formatter: '{value} %'
            formatter: function (value, index) {
              return value =  value.toFixed(4);
              //return '{value} %';
            }
          },
          show: true
        },
        series: [{
          name: this.lineRed,
          type: 'line',
          smooth:true,
          symbol:'none',
          data: this.navData,
          itemStyle: {
            normal: {
              lineStyle: {
                color: '#ff0000'
              }
            }
          }
        }, {
          name: this.lineBlue,
          type: 'line',
          smooth:true,/*平滑的线*/
          symbol:'none',/*没有折线点*/
          data: this.marketData,
          itemStyle: {
            normal: {
              lineStyle: {
                color: '#3d85c6'
              }
            }
          }
        }
        ]
      })
    }

  },
  //调用
  mounted() {
    this.$nextTick(function () {
      this.drawLine('main')
    })
  }
};
</script>
