<template>
  <div class="cockpit" :key="pageKey">
    <CockpitTitle class="cockpit-title" />
    <div class="cockpit-container">
      <div class="part part1">
        <div class="part1-1">
          <div class="part1-1-pie2">
            <ChartPart btnName="资产占比情况" :hasExport="true" @showTable="showAssetTable">
              <div slot="select">
                <CockpitSelect2  :processLoading="prodInfo.length>0" :options="prodInfo" @select="findAssetPositions"/>
              </div>
              <PieCircle :pieData="pieData" />
            </ChartPart>
          </div>
        </div>
        <div class="part1-2">
          <div class="center-bg">
            <img :src="require('@/assets/img/cockpit/bg.png')" />
          </div>
          <div class="part1-2-2">
            <ChartPart btnName="产品规模分析" type="type2" btnAlign="center" @showTable="showProdTable">
              <div class="analysis">
                <div class="card-box" v-for="item in cardList" :key="item.title">
                  <Card  :data="item" />
                </div>
              </div>
            </ChartPart>
          </div>
        </div>
        <div class="part1-3">
          <div class="part1-3-bar3">
            <ChartPart btnName="各机构持有量统计" @showTable="showOrgTable">
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
        <div slot="select" style="padding-left: 4%">
          <CockpitSelect :options="assetType" @select="findAssetDistribute"/>
        </div>
        <ChartPart btnName="穿透前后债券分布情况" type="type2" btnAlign="center" @showTable="showDistributeTable">
          <Bar
            :color="oneYearData.color"
            :axisData="oneYearData.axisData"
            :seriesData="oneYearData.seriesData"
            :hasLegend="true"
          />
        </ChartPart>
      </div>
    </div>

    <!--  资产占比弹窗  -->
    <k-popup ref="showAssetPopup" :data-title="findCondition.prodCode+'资产占比情况'" style="padding-left: 120px;" :dataDialogDrag="true">
      <div >
        <k-form :data-col="2" style="margin-top:3%">
          <k-form-item label="产品代码">
            <k-field-select v-model="findCondition.prodCode" data-action='OperationSituation.findProdInfo'
                            data-value-field="prodCode" data-display-field="prodCode,prodName" @data-on-change="findAssetPositions" />
          </k-form-item>
        </k-form>
        <k-grid ref="AssetPositionsGrid" :data-checkbox="false" data-checkbox-id="id" data-fixed="right"
                data-action='OperationSituation.findAssetPositions' @init="(grid)=>{this.$kgrid = grid}" data-operate-width="100px"
                :data-params="findCondition"  data-operate-column="false" :data-page-size="5">
          <k-grid-column data-align="center" data-header="资产代码" data-name="assetCode" data-width="120"/>
          <k-grid-column data-align="center" data-header="资产名称" data-name="assetName" data-width="180"/>
          <k-grid-column data-align="center" data-header="持仓金额(元)" data-name="positionAmt" data-width="150" />
          <k-grid-column data-align="center" data-header="占有比例(%)" data-name="value" data-width="120" />
          <k-grid-column data-align="center" data-header="持仓日期" data-name="tradeDate" data-type="date"/>
        </k-grid>
      </div>
    </k-popup>

    <!--  产品规模弹窗  -->
    <k-popup ref="showProdPopup" data-title="产品规模详情" style="padding-left: 120px" :dataDialogDrag="true">
      <div>
        <k-form :data-col="2" style="margin-top:3%" >
          <k-form-item label="产品代码">
            <k-field-select v-model="findCondition.scaleProd" data-action='OperationSituation.findProdInfo'
                            data-value-field="prodCode" data-display-field="prodCode,prodName" @data-on-change="findProdScaleDetails" />
          </k-form-item>
        </k-form>
        <k-grid ref="prodScaleGrid" :data-checkbox="false" data-checkbox-id="id" data-fixed="right"
                data-action='OperationSituation.findProdScaleDetails' @init="(grid)=>{this.$kgrid = grid}" data-operate-width="100px"
                data-operate-column="false" :data-page-size="5">
          <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode" data-width="100"/>
          <k-grid-column data-align="center" data-header="产品名称" data-name="prodName" data-width="120"/>
          <k-grid-column data-align="center" data-header="子产品" data-name="prodCodeSub" data-width="100"/>
          <k-grid-column data-align="center" data-header="现有规模(元)" data-name="existingScale" data-width="150"/>
          <k-grid-column data-align="center" data-header="同比上月增长(%)" data-name="proportion" data-width="120"/>
          <k-grid-column data-align="center" data-header="持仓日期" data-name="tradeDate"  data-type="date"/>
        </k-grid>
      </div>
    </k-popup>

    <!--  机构持有弹窗  -->
    <k-popup ref="showOrgPopup" data-title="机构持有量统计" style="padding-left: 120px" :dataDialogDrag="true">
      <div>
        <k-form :data-col="2" style="margin-top:3%">
          <k-form-item label="产品代码">
            <k-field-select v-model="findCondition.orgProd" data-action='OperationSituation.findProdInfo'
                            data-value-field="prodCode" data-display-field="prodCode,prodName" @data-on-change="findOrgHoldDetails" />
          </k-form-item>
          <k-form-item label="机构名称">
            <k-field-select v-model="findCondition.orgCode" data-action='OperationSituation.findOrgDict'
                            data-value-field="orgCode" data-display-field="orgCode,orgName" @data-on-change="findOrgHoldDetails" />
          </k-form-item>
        </k-form>
        <k-grid ref="prodOrgGrid" :data-checkbox="false" data-checkbox-id="id" data-fixed="right"
                data-action='OperationSituation.findOrgHoldDetails' @init="(grid)=>{this.$kgrid = grid}" data-operate-width="100px"
                data-operate-column="false" :data-page-size="5">
          <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode" data-width="120"/>
          <k-grid-column data-align="center" data-header="机构代码" data-name="orgCode" data-width="120"/>
          <k-grid-column data-align="center" data-header="机构名称" data-name="orgName" data-width="150"/>
          <k-grid-column data-align="center" data-header="持有量(元)" data-name="holdScale" data-width="200"/>
          <k-grid-column data-align="center" data-header="持仓日期" data-name="tradeDate" data-type="date" />
        </k-grid>
      </div>
    </k-popup>


    <!--  机构持有弹窗  -->
    <k-popup ref="showDistributePopup" data-width-percent="68%" data-title="机构持有量统计" style="padding-left: 120px;" :dataDialogDrag="true">
      <div>
        <k-form :data-col="2" style="width: 1000px;margin-top:3%">
          <k-form-item label="资产类型" style="width: 300px;margin-left:3%">
            <k-field-select v-model="findCondition.assetType" data-action='OperationSituation.findTypeDict'
                            data-value-field="assetType" data-display-field="assetType" @data-on-change="findAssetDistributeDetails" />
          </k-form-item>
          <k-form-item label="评级" style="width: 300px;margin-left:-5%">
            <k-field-select v-model="findCondition.grade" data-action='OperationSituation.findGradeDict'
                            data-value-field="grade" data-display-field="grade" @data-on-change="findAssetDistributeDetails" />
          </k-form-item>
          <k-form-item label="行业" style="width: 300px;margin-left:-5%">
            <k-field-select v-model="findCondition.industry" data-action='OperationSituation.findIndustryDict'
                            data-value-field="industry" data-display-field="industry" @data-on-change="findAssetDistributeDetails" />
          </k-form-item>
        </k-form>
        <k-grid ref="prodDistributeGrid" :data-checkbox="false" data-checkbox-id="id" data-fixed="right"
                data-action='OperationSituation.findAssetDistributeDetails' @init="(grid)=>{this.$kgrid = grid}" data-operate-width="100px"
                data-operate-column="false" :data-page-size="5">
          <k-grid-column data-align="center" data-header="资产代码" data-name="assetCode" data-width="120"/>
          <k-grid-column data-align="center" data-header="评级" data-name="grade" data-width="100"/>
          <k-grid-column data-align="center" data-header="行业" data-name="industry" data-width="150"/>
          <k-grid-column data-align="center" data-header="资产类型" data-name="assetType" data-width="150"/>
          <k-grid-column data-align="center" data-header="穿透前(元)" data-name="pierceBefore" data-width="150"/>
          <k-grid-column data-align="center" data-header="穿透后(元)" data-name="pierceLater" data-width="150"/>
          <k-grid-column data-align="center" data-header="持仓日期" data-name="tradeDate" data-type="date" />
        </k-grid>
      </div>
    </k-popup>



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
import CockpitSelect2 from '@/pages/pms/workbench/cockpit/component/select2.vue'
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
    CockpitSelect,
    CockpitSelect2
  },
  data() {
    return {

      assetType:[
        {
          label: '评级',
          value: '1'
        },{
          label: '行业',
          value: '2'
        },{
          label: '债券类型',
          value: '3'
        },
      ],

      prodInfo : [],

      pieData: {
        unit: '亿元',
        data: []
      },

      reportData: {
        color: ['#7bede8'],
        axisData: [],
        seriesData: []
      },
      oneYearData: {
        color: ['#eb4e7a', '#4869ea'],
        axisData: [],
        seriesData: []
      },
      cardList: [],

      findCondition:{
        prodCode : '',
        scaleProd : '',
        orgProd : '',
        orgCode : '',
        grade : '',
        industry : '',
        assetType : '',
      },

      pageKey: 0
    }
  },
  methods: {
    // 更新页面
    setPageKey() {
      this.pageKey++
    },

    /** ============== 弹窗部分==================  **/
    showAssetTable(){
      this.$refs.showAssetPopup.popup();
    },
    showProdTable(){
      this.findCondition.scaleProd = '';
      this.$refs.showProdPopup.popup();
    },
    showOrgTable(){
      this.findCondition.orgProd = '';
      this.findCondition.orgCode = '';
      this.$refs.showOrgPopup.popup();
    },

    showDistributeTable(){
      this.findCondition.grade = '';
      this.findCondition.industry = '';
      this.findCondition.assetType = '';
      this.$refs.showDistributePopup.popup();
    },

    /** ============== 数据查询部分部分==================  **/
    async findProdInfo(){
      this.httpUtil.comnQuery({
        action: 'OperationSituation.findProdInfo',
        params: {},
      }).then(data => {
        let rows = data.rows;
        if(rows){
          this.prodInfo = rows;
        }
      });
    },

    async findAssetPositions(param){

      if (param)
        this.findCondition.prodCode = param;

      this.httpUtil.comnQuery({
        action: 'OperationSituation.findAssetPositions',
        params: {prodCode: param},
      }).then(data => {
        let rows = data.rows;
        if(rows){
          this.pieData.data = rows;
          if(this.$refs.AssetPositionsGrid){
            this.$refs.AssetPositionsGrid.load();
          }
        }
      });
    },

    async findProdScaleTotal(param){
      this.httpUtil.comnQuery({
        action: 'OperationSituation.findProdScaleTotal',
        params: {prodCode: param},
      }).then(data => {
        let rows = data.rows;
        if(rows){
          this.cardList = rows;
        }
      });
    },

    findProdScaleDetails(param){
      this.$refs.prodScaleGrid.load({prodCode: param});
    },

    findOrgHoldDetails(param){
      this.$refs.prodOrgGrid.load({orgCode: this.findCondition.orgCode,prodCode: this.findCondition.orgProd});
    },
    findAssetDistributeDetails(param){
      this.$refs.prodDistributeGrid.load(this.findCondition);
    },


    async findOrgHold(){
      this.httpUtil.comnQuery({
        action: 'OperationSituation.findOrgHold',
        params: {},
      }).then(data => {
        let rows = data.rows;
        if(rows){
          Object.assign(this.reportData, rows[0]);
        }
      });
    },

    async findAssetDistribute(param){
      this.httpUtil.comnQuery({
        action: 'OperationSituation.findAssetDistribute',
        params: param,
      }).then(data => {
        let rows = data.rows;
        if(rows){
          Object.assign(this.oneYearData, rows[0]);
        }
      });
    }

  },


  mounted() {
    const _this = this
    window.addEventListener('resize', throttle(()=>{
      if (window.innerWidth > 1100) {
        _this.setPageKey()
      }
    }, 300, { trailing: true }));

    //加载产品代码
    this.findProdInfo();
    //查询资产占比情况
    this.findAssetPositions();
    //产品总规模
    this.findProdScaleTotal();
    //各机构持有量统计
    this.findOrgHold();
    //穿透分布图
    this.findAssetDistribute({'value':'1'});

  }
}
</script>
<style lang="scss" scoped>

.aaa{
  background: linear-gradient(135deg, #000, #111843, #3e123a)
}

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
        margin-top: 40px;
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
        padding: 0 1%;
        margin-top: 100px;
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
