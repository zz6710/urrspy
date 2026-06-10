<template>
  <div>
    <div>
      <k-form-search-customize data-model-name="StockNavInfoModel" data-target="stockNavInfoModelGrid"
                               v-model="searchParam">
        <k-form-item label="股票代码">
<!--          <k-field-select v-model="searchParam.stockCode" data-dict="stockInfo"></k-field-select>-->
          <k-field-select v-model="searchParam.stockCode" data-action="StockNavInfoModel.findStockNavInfoCd"
                          :dataRemote="true"
                          data-display-field="stockCode" data-value-field="stockCode"></k-field-select>
        </k-form-item>
        <k-form-item label="股票名称">
          <k-field-text v-model="searchParam.stockName"/>
        </k-form-item>
        <k-form-item label="交易日期">
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd"
                        data-value-format="yyyyMMdd"></k-field-date>
        </k-form-item>
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
               data-target="addStockNavInfoModelPopup" slot="button"
               v-if="global.isShowAuthorityButton('FundNavInfoModel.updateFundNavInfoModel')">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
      </k-form-search-customize>
    </div>
<!--    查询结果-->
    <div>
      <k-grid ref="stockNavInfoModelGrid" @data-row-select="selectRow" data-fixed="right"
              data-operate-width="250px" data-action="StockNavInfoModel.findStockNavInfoModelsByStockCd" >
        <k-grid-column data-header="股票代码" data-name="stockCode" data-hidden="false"></k-grid-column>
        <k-grid-column data-header="股票名称" data-name="stockName"></k-grid-column>
        <k-grid-column data-header="股票类型" data-name="stockType" data-dict="stock_type"></k-grid-column>
        <k-grid-column data-header="交易起始日期" data-name="startDate" data-type="date"></k-grid-column>
        <k-grid-column data-header="交易结束日期" data-name="endDate" data-type="date"></k-grid-column>
        <k-grid-column data-header="昨收盘价（元）" data-name="lastCloPrice"></k-grid-column>
        <k-grid-column data-header="最高价（元）" data-name="highPrice"></k-grid-column>
        <k-grid-column data-header="最低价（元）" data-name="lowPrice"></k-grid-column>
        <k-grid-column data-header="收盘价（元）" data-name="cloPrice"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-danger" data-functype="SUBMIT" data-action="StockNavInfoModel.deleteStockNavInfoModel"
                 data-size="mini" v-if="global.isShowAuthorityButton('StockNavInfoModel.deleteStockNavInfoModel')"
                 data-type="danger" data-target="stockNavInfoModelGrid" :data-confirm="true"
                 data-descript="删除股票行情信息">
            删除
          </k-btn>
          <k-btn class="btn-custom-plain" data-descript="股票行情信息详情" data-functype="POPUP" data-size="mini"
                 v-if="global.isShowAuthorityButton('StockNavInfoModel.findStockNavInfoModelsByStockCd')"
                 data-target="detailStockNavInfoModelPopup">
            详情
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加股票行情信息弹出框   -->
    <k-popup ref="addStockNavInfoModelPopup" data-title="新增">
      <k-form ref="addStockNavInfoModelForm" :data-col="2">
        <!--          <k-field-text v-model="formData.scrId"/>-->
        <!--        </k-form-item>-->
        <k-form-item label="股票代码">
          <k-field-text v-model="formData.stockCode" data-dict="stockInfo" :data-allowblank="false"></k-field-text>
        </k-form-item>
        <k-form-item label="股票名称">
          <k-field-text v-model="formData.stockName"/>
        </k-form-item>
        <!--        todo-->
        <k-form-item label="股票类型">
          <k-field-select v-model="formData.stockType" data-dict="stock_type" :data-allowblank="false"
                          @data-on-change="fundIdAndNm"/>
        </k-form-item>
        <k-form-item label="交易日期">
          <k-field-date v-model="formData.formDateRanger" data-type="daterange" data-date-format="yyyyMMdd"
                        data-value-format="yyyyMMdd" :data-allowblank="false"></k-field-date>
        </k-form-item>
        <k-form-item label="昨收盘价（元）">
          <k-field-text v-model="formData.lastCloPrice"/>
        </k-form-item>
        <k-form-item label="最高价（元）">
          <k-field-text v-model="formData.highPrice"/>
        </k-form-item>
        <k-form-item label="最低价（元）">
          <k-field-text v-model="formData.lowPrice"/>
        </k-form-item>
        <!--        todo-->
        <k-form-item label="涨跌（元）">
          <k-field-text v-model="formData.profitAndLoss"/>
        </k-form-item>
        <k-form-item label="涨跌幅（元）">
          <k-field-text v-model="formData.profitAndLossRange"/>
        </k-form-item>
        <k-form-item label="成交量（手）">
          <k-field-text v-model="formData.tradingVolume"/>
        </k-form-item>
        <k-form-item label="成交金额（亿元）">
          <k-field-text v-model="formData.tradingMoney"/>
        </k-form-item>
        <k-form-item label="开盘价（元）">
          <k-field-text v-model="formData.startPrice"/>
        </k-form-item>
        <k-form-item label="收盘价（元）">
          <k-field-text v-model="formData.cloPrice"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="StockNavInfoModel.addStockNavInfoModel"
                 data-from="addFundNavInfoModelForm"
                 :data-model="formData" data-target="stockNavInfoModelGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    股票行情信息相亲弹出框   -->
    <k-popup ref="detailStockNavInfoModelPopup" data-title="详情" @data-opened="editInfo">
      <k-form ref="editFundNavInfoModelForm" :data-col="2">
        <k-form-item label="id" v-show="false">
          <k-field-text v-model="formData.scrId"/>
        </k-form-item>
        <k-form-item label="股票代码">
          <k-field-text v-model="formData.stockCode" :data-disabled="true"></k-field-text>
        </k-form-item>
        <k-form-item label="股票名称">
          <k-field-text v-model="formData.stockName" :data-disabled="true"/>
        </k-form-item>
        <!--        todo-->
        <k-form-item label="股票类型">
          <k-field-select v-model="formData.stockType" data-dict="stock_type" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="交易日期">
<!--          <k-field-date v-model="formDay" data-type="daterange" data-date-format="yyyyMMdd"-->
<!--                        data-value-format="yyyyMMdd" :data-allowblank="false"></k-field-date>-->
          <k-field-text v-model="formData.startDate+'-'+formData.endDate" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="昨收盘价（元）">
          <k-field-text v-model="formData.lastCloPrice" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="最高价（元）">
          <k-field-text v-model="formData.highPrice" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="最低价（元）">
          <k-field-text v-model="formData.lowPrice" :data-disabled="true"/>
        </k-form-item>
        <!--        todo-->
        <k-form-item label="涨跌（元）">
          <k-field-text v-model="formData.profitAndLoss" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="涨跌幅（元）">
          <k-field-text v-model="formData.profitAndLossRange" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="成交量（手）">
          <k-field-text v-model="formData.tradingVolume" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="成交金额（亿元）">
          <k-field-text v-model="formData.tradingMoney" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="开盘价（元）">
          <k-field-text v-model="formData.startPrice" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="收盘价（元）">
          <k-field-text v-model="formData.cloPrice" :data-disabled="true"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>

export default {
  name: "StockNavInfoModel",
  data() {
    return {
      formData: {
        stockCode: '',
        stockName: '',
        stockType: '',
        lastCloPrice: '',
        highPrice: '',
        lowPrice: '',
        cloPrice: '',
        formDateRanger: [],
        startDate: '',
        endDate: '',
      },
      selectRowData: {},
      searchParam: {},
      BreathDay: [],
      formDay: [],
      formDateRanger: [],
      fundInfoDict: {},
      stockInfoDict: {}
    };
  },
  created() {
    // this.fundIdAndNm();
  },
  methods: {
    editInfo() {
      this.fundIdAndNm()
    },
    setTypeAndNm(value) {
      this.httpUtil.comnQuery({
        action: "FundInfoModel.findFondInfoModelsCdAndNmByScrCd",
        params: {scrCd: this.formData.scrCd,}
      }).then(data => {
        if (value) {
          this.$set(this.formData, 'scrId', data.rows[0].scrId);
        }
        this.$set(this.formData, 'scrNm', data.rows[0].scrNm);
        this.$set(this.formData, 'fundType', data.rows[0].wdFrsCtg);
      }).catch({})
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },

    fundIdAndNm() {
      this.stockInfoDict = {};
      this.httpUtil.comnQuery({
        action: "StockNavInfoModel.findStockNavInfoModelsByStockCd",
        params: {stockCode: this.formData.stockCode,}
      }).then(data => {
        this.stockInfoDict = data.rows;
      }).catch({})
    },
  },
  watch: {
    //查询起息日
    BreathDay() {
      this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
      this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
    },
    formDay() {
      this.$set(this.formData, 'startDate', this.formDay == null ? '' : this.formDay[0]);
      this.$set(this.formData, 'endDate', this.formDay == null ? '' : this.formDay[1]);
    },
    'formData.formDateRanger'() {
      this.formData.startDate = this.formData.formDateRanger[0];
      this.formData.endDate = this.formData.formDateRanger[1];
    },
  }
};
</script>
