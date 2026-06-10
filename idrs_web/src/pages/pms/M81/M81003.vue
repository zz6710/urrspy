<template>
  <div>
    <k-form-search data-target="t81003Grid" data-model-name="T81003">
      <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addT81003Popup">
        <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
      <k-btn class="md-rose" data-functype="EXPORT" data-target="t81003Grid" data-export-name="产品行情信息表">
        <i class="icon-export" />导出
      </k-btn>
    </k-form-search>

    <k-grid ref="t81003Grid"
            data-action='T81003.findT81003s' data-operate-column-position="end"
            @data-row-select="selectRow" data-operate-width="100px" :data-before-load="loadProdCode">
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-functype="POPUP" data-size="mini" data-target="updT81003Popup" :data-disabled="scope.row.row.checkStatus != '0'"
               :data-handler="modifyProdNav" data-descript="修改净值记录">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-size="mini" data-type="danger"
               data-action="T81003.deleteT81003" :data-model="selectRowData"
               data-confirm data-descript="删除净值记录" :data-disabled="scope.row.row.checkStatus != '0'" data-target="t81003Grid">
          <md-icon>close</md-icon>
        </k-btn>
      </template>

      <k-grid-column data-name="prodCode" data-header="产品代码" data-width="80"></k-grid-column>
      <k-grid-column data-name="prodName" data-header="产品名称" data-width="80"></k-grid-column>
      <k-grid-column data-name="navDate" data-header="净值日期" data-type="date"></k-grid-column>
      <k-grid-column data-name="nav" data-header="单位净值" data-type="double"></k-grid-column>
      <k-grid-column data-name="totalNav" data-header="累计净值" data-type="double"></k-grid-column>
      <k-grid-column data-name="outTenThousandIncomeAmt" data-header="万份收益(对外披露)"  data-type="double"></k-grid-column>
      <k-grid-column data-name="tenThousandIncomeAmt" data-header="万份收益(对内计算)" data-type="double"></k-grid-column>
      <k-grid-column data-name="sevenDaysIncomeRate" data-header="近七日年化收益率(%)" data-renderer="sevenDaysIncomeRateConvert" data-type="double"></k-grid-column>
      <k-grid-column data-name="thirtyDaysIncomeRate" data-header="近30日年化收益率(%)" data-renderer="thirtyDaysIncomeRateConvert" data-type="double"></k-grid-column>
      <k-grid-column data-name="totalIncomeAmt" data-header="收益总额" data-type="money"></k-grid-column>
      <k-grid-column data-name="totalVol" data-header="总份额" data-type="double"></k-grid-column>
      <k-grid-column data-name="assetNav" data-header="资产净值" data-type="double"></k-grid-column>
      <k-grid-column data-name="windingTotalAmt" data-header="清盘总金额" data-type="money"></k-grid-column>
      <k-grid-column data-name="importDate" data-header="导入日期" data-type="date"></k-grid-column>
      <k-grid-column data-name="remark" data-header="备注"></k-grid-column>
    </k-grid>


    <!--    添加净值记录弹出框   -->
    <k-popup ref="addT81003Popup" data-title="添加产品净值">
      <k-form ref="addT81003Form" :data-col="2" >

        <k-form-item label="产品名称">
          <k-field-select v-model="formData.prodCode" data-action="T8Dict.findTaProdInfos"
                          data-display-field="prodCode,prodName" data-value-field="prodCode" :dataAllowblank="false"
                          @data-on-change="setNavDatePeriod"/></k-form-item>
        <k-form-item label="净值日期">
          <k-field-date v-model="formData.navDate"
                        :dataAllowblank="false" :data-min-value="minNavDate" :data-max-value="maxNavDate"/></k-form-item>
        <k-form-item label="单位净值">
          <k-field-text v-model="formData.nav"
                        :data-digits="8" :data-max-length="13"
                        data-max-value="10000)"
                        :dataAllowblank="false" data-validate-type="number" data-min-value="0"/></k-form-item>
        <k-form-item label="累计净值">
          <k-field-text v-model="formData.totalNav"
                        :data-digits="8" :data-max-length="13" data-max-value="10000)"
                        data-validate-type="number" data-min-value="0"/></k-form-item>
        <k-form-item label="万份收益(对外披露)" v-show="elementStyle[0].show">
          <k-field-text v-model="formData.outTenThousandIncomeAmt" data-validate-type="money"
                        :data-max-length="13" :data-digits="4"
                        data-min-value="0" data-max-value="10000)"
                        :dataAllowblank="elementStyle[0].allowBlank" @input="forceUpdate"/></k-form-item>
        <k-form-item label="万份收益(对内计算)" v-show="elementStyle[1].show">
          <k-field-text v-model="formData.tenThousandIncomeAmt" data-validate-type="number"
                        :data-max-length="13" :data-digits="8"
                        data-min-value="0" data-max-value="10000)"
                        :dataAllowblank="elementStyle[1].allowBlank"@input="forceUpdate"/></k-form-item>
        <k-form-item label="近七日年化收益率(%)" v-show="elementStyle[2].show">
          <k-field-text v-model="formData.sevenDaysIncomeRate" data-validate-type="number"
                        data-min-value="(0" data-max-value="100)" :data-max-length="10" :data-digits="7"
                        :dataAllowblank="elementStyle[2].allowBlank" @input="forceUpdate"/></k-form-item>
        <k-form-item label="近30日年化收益率(%)" v-show="elementStyle[3].show">
          <k-field-text v-model="formData.thirtyDaysIncomeRate" data-validate-type="number"
                        data-min-value="(0" data-max-value="100)"
                        :data-digits="5" :data-max-length="10"
                        :dataAllowblank="elementStyle[3].allowBlank" @input="forceUpdate"/></k-form-item>
        <k-form-item label="收益总额">
          <k-field-text v-model="formData.totalIncomeAmt"
                        :data-digits="2" :data-max-length="18" data-min-value="0"
                        data-validate-type="number" data-max-value="1000000000000000)" :dataAllowblank="elementStyle[4].allowBlank"/></k-form-item>
        <k-form-item label="总份额">
          <k-field-text v-model="formData.totalVol"
                        :data-digits="2" :data-max-length="18" data-min-value="0"
                        data-validate-type="number" data-max-value="1000000000000000)"/></k-form-item>
        <k-form-item label="资产净值">
          <k-field-text v-model="formData.assetNav"
                        :data-digits="2" :data-max-length="18" data-min-value="0"
                        data-validate-type="number" data-max-value="1000000000000000)" :dataAllowblank="elementStyle[5].allowBlank"/></k-form-item>
        <k-form-item label="清盘总金额">
          <k-field-text v-model="formData.windingTotalAmt"
                        :data-digits="2" :data-max-length="18" data-min-value="0"
                        data-validate-type="number" data-max-value="1000000000000000)"/></k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.remark" :data-max-length="80"/></k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T81003.addT81003"
                 data-from="addT81003Form" :data-model="formData"
                 data-target="t81003Grid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改净值弹出框   -->
    <k-popup ref="updT81003Popup" data-title="修改产品净值">
      <k-form ref="editUserForm" :data-col="2">
        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodCode" :dataAllowblank="false" :data-disabled="true"/></k-form-item>
        <k-form-item label="净值日期">
          <k-field-text v-model="formData.navDate" :dataAllowblank="false" :data-disabled="true"/></k-form-item>
        <k-form-item label="单位净值">
          <k-field-text v-model="formData.nav"
                        :data-digits="8" :data-max-length="13"
                        data-max-value="10000)"
                        :dataAllowblank="false" data-validate-type="number"/></k-form-item>
        <k-form-item label="累计净值">
          <k-field-text v-model="formData.totalNav"
                        :data-digits="8" :data-max-length="13" data-max-value="10000)"
                        data-validate-type="number"/></k-form-item>
        <k-form-item label="万份收益(对外披露)" v-show="elementStyle[0].show">
          <k-field-text v-model="formData.outTenThousandIncomeAmt" data-validate-type="money"
                        :data-max-length="13" :data-digits="4"
                        data-min-value="0" data-max-value="10000)"
                        :dataAllowblank="elementStyle[0].allowBlank"/></k-form-item>
        <k-form-item label="万份收益(对内计算)" v-show="elementStyle[1].show">
          <k-field-text v-model="formData.tenThousandIncomeAmt" data-validate-type="number"
                        :data-max-length="13" :data-digits="8"
                        data-min-value="0" data-max-value="10000)"
                        :dataAllowblank="elementStyle[1].allowBlank"/></k-form-item>
        <k-form-item label="近七日年化收益率(%)" v-show="elementStyle[2].show">
          <k-field-text v-model="formData.sevenDaysIncomeRate" data-validate-type="number"
                        data-min-value="(0" data-max-value="100)" :data-max-length="10" :data-digits="7"
                        :dataAllowblank="elementStyle[2].allowBlank"/></k-form-item>
        <k-form-item label="近30日年化收益率(%)" v-show="elementStyle[3].show">
          <k-field-text v-model="formData.thirtyDaysIncomeRate" data-validate-type="number"
                        data-min-value="(0" data-max-value="100)"
                        :data-digits="5" :data-max-length="10"
                        :dataAllowblank="elementStyle[3].allowBlank"/></k-form-item>
        <k-form-item label="收益总额">
          <k-field-text v-model="formData.totalIncomeAmt"
                        :data-digits="2" :data-max-length="18" data-min-value="0"
                        data-validate-type="number" data-max-value="1000000000000000)" :dataAllowblank="elementStyle[4].allowBlank"/></k-form-item>
        <k-form-item label="总份额">
          <k-field-text v-model="formData.totalVol"
                        :data-digits="2" :data-max-length="18" data-min-value="0"
                        data-validate-type="number" data-max-value="1000000000000000)"/></k-form-item>
        <k-form-item label="资产净值">
          <k-field-text v-model="formData.assetNav"
                        :data-digits="2" :data-max-length="18" data-min-value="0"
                        data-validate-type="number" data-max-value="1000000000000000)" :dataAllowblank="elementStyle[5].allowBlank"/></k-form-item>
        <k-form-item label="清盘总金额">
          <k-field-text v-model="formData.windingTotalAmt"
                        :data-digits="2" :data-max-length="18" data-min-value="0"
                        data-validate-type="number" data-max-value="1000000000000000)"/></k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.remark" :data-max-length="80"/></k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T81003.updateT81003"
                 data-from="editUserForm" :data-model="formData"
                 data-target="t81003Grid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
import { assign } from "lodash";

export default {
  data() {
    return {
      formData: {},
      selectRowData: {},
      minNavDate: "",                 // 最小净值日期
      maxNavDate: "",                 // 最大净值日期
      $kgrid: null,
      currentWorkday:null,
      elementStyle: [
        {
          name: 'outTenThousandIncomeAmt',  // 万份收益(对外披露)
          allowBlank: true,
          show: true
        },{
          name: 'tenThousandIncomeAmt', // 万份收益(对内计算)
          allowBlank: true,
          show: true
        },{
          name: 'sevenDaysIncomeRate',  // 近七日年化收益率(%)
          allowBlank: true,
          show: true
        },{
          name: 'thirtyDaysIncomeRate', // 近30日年化收益率(%)
          allowBlank: true,
          show: true
        },{
          name: 'totalIncomeAmt',  //收益总额
          allowBlank: true,
        },{
          name:'assetNav', //资产净值
          allowBlank: true,
        }

      ]

    };
  },

  methods: {
    forceUpdate(){
      this.$forceUpdate();
    },
    selectRow(row, column, event) {
      const _this = this
      _this.selectRowData = Object.assign({}, row)
      _this.formData = Object.assign({}, row)
    },


    modifyProdNav(row){

      const _this = this;

      let prodCode = row.prodCode;

      this.setInputHide(prodCode);

    },

    setNavDatePeriod(value) {
      this.$delete(this.formData,"navDate");
      // 查询产品周期信息，设置产品成立日为净值最小日期，开放结束日为净值最大日期
      this.httpUtil.comnQuery({
        action: "T8ProdPeriod.findT8ProdPeriods",
        params: {prodCode: value}
      }).then(data => {
        let rows = data.rows;

        //console.log("rows is =======>", rows)
        this.handlePeriodData(rows);
      });

      // 动态显示对应字段
      this.setInputHide(value);
    },

    //处理数据
    handlePeriodData(rows) {
      rows.map(row => {

        // 最小日期修改为最小募集日
        // this.minNavDate = rows[0].establishDate;
        this.httpUtil.sysDate().then(res=>{
          if (res) {
            this.currentWorkday = res;
          }
          this.minNavDate = rows[0].subsBeginDate < this.currentWorkday ? this.currentWorkday : rows[0].subsBeginDate ;
          this.maxNavDate = rows[0].endDate;
        });
      });
    },

    setInputHide(value){

      // 查询产品信息表
      this.httpUtil.comnQuery({
        action: "T8ProdInfo.findTaProdInfos",
        params: {prodCode: value}
      }).then(data => {
        let rows = data.rows;

        let prodMode = rows[0].prodMode;


        if (prodMode == "1" || prodMode == "2" || prodMode == "4" ) { // 净值型产品
          // 净值型产品，不通过收益率体现，隐藏字段：万份收益（对外披露）、万份收益（对内计算）、近7日年化收益率（%）、近30日年化收益率（%）

          this.elementStyle[0].allowBlank = true;
          this.elementStyle[0].show       = false;
          this.elementStyle[1].allowBlank = true;
          this.elementStyle[1].show       = false;
          this.elementStyle[2].allowBlank = true;
          this.elementStyle[2].show       = false;
          this.elementStyle[3].allowBlank = true;
          this.elementStyle[3].show       = false;
          this.elementStyle[4].allowBlank = true;
          this.elementStyle[5].allowBlank = true;

          this.formData.outTenThousandIncomeAmt = '';
          this.formData.tenThousandIncomeAmt = '';
          this.formData.sevenDaysIncomeRate = '';
          this.formData.thirtyDaysIncomeRate = '';

        } else if (prodMode == '3') {
          // 货币型产品，才通过收益率体现，显示字段：万份收益（对外披露）、万份收益（对内计算）、近7日年化收益率（%）、近30日年化收益率（%）

          this.elementStyle[0].allowBlank = false;
          this.elementStyle[0].show       = true;
          this.elementStyle[1].allowBlank = false;
          this.elementStyle[1].show       = true;
          this.elementStyle[2].allowBlank = false;
          this.elementStyle[2].show       = true;
          this.elementStyle[3].allowBlank = false;
          this.elementStyle[3].show       = true;
          this.elementStyle[4].allowBlank = false;
          this.elementStyle[5].allowBlank = false;

          this.formData.outTenThousandIncomeAmt = '';
          this.formData.tenThousandIncomeAmt = '';
          this.formData.sevenDaysIncomeRate = '';
          this.formData.thirtyDaysIncomeRate = '';
        }

      });

    },
    loadProdCode(params){

      if (this.prodCode != null && this.prodCode != ''){

        params.prodCode = this.prodCode;

        // 该值只用一次
        this.prodCode = "";
      }
      return params;
    }

  },
  created(){
    this.prodCode = this.$route.query.prodCode;
  },
};
</script>
