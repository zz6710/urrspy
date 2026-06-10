<template>
  <div>
    <k-form-search data-target="ta1004Grid" data-model-name="T81004">
      <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addT81004Popup">
        <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
    </k-form-search>

    <k-grid ref="ta1004Grid"
            data-action='T81004.findT81004s' data-operate-column-position="end"
            @data-row-select="selectRow" data-operate-width="200px"
            :data-before-load="loadProdCode">

      <template slot="operate">
        <k-btn data-functype="POPUP" data-size="mini" data-target="updT81004Popup" class="md-info md-just-icon md-simple"
               :data-handler="modifyProdBonus" data-descript="修改分红记录">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn data-functype="SUBMIT" data-size="mini" data-type="danger" class="md-danger md-just-icon md-simple"
               data-action="T81004.deleteT81004" :data-model="selectRowData" data-target="ta1004Grid"
               data-confirm data-descript="删除分红记录">
          <md-icon>close</md-icon>
        </k-btn>
      </template>

      <k-grid-column data-name="prodCode" data-header="产品代码" data-sortable="true"></k-grid-column>
      <k-grid-column data-name="prodName" data-header="产品名称"></k-grid-column>
      <k-grid-column data-name="dividMode" data-header="分红模式" data-dict="divid_mode"></k-grid-column>
      <k-grid-column data-name="divMethod" data-header="分红方式" data-dict="div_method"></k-grid-column>
      <k-grid-column data-name="perBonus" data-header="单位分红金额" data-type="money"></k-grid-column>
      <k-grid-column data-name="totalBonus" data-header="分红总金额" data-type="money"></k-grid-column>
      <k-grid-column data-name="registerDate" data-header="权益登记日" data-type="date"></k-grid-column>
      <k-grid-column data-name="exDividendDate" data-header="除权日" data-type="date"></k-grid-column>
      <k-grid-column data-name="paidDate" data-header="分红下发日" data-type="date"></k-grid-column>
      <k-grid-column data-name="execStatus" data-header="权益登记执行状态" data-dict="exec_status"></k-grid-column>
      <k-grid-column data-name="paidExecStatus" data-header="红利下发执行状态" data-dict="exec_status"></k-grid-column>
    </k-grid>

    <!--    添加分红记录弹出框   -->
    <k-popup ref="addT81004Popup" data-title="添加产品分红方案">
      <k-form ref="addT81004Form" :data-col="2" @input="forceUpdate">

        <k-form-item label="产品代码">
          <k-field-select v-model="formData.prodCode" data-action="T8Dict.findNavProdInfo"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"
                          @data-on-change="setNavDatePeriod"
                          :dataAllowblank="false"/></k-form-item>
        <k-form-item label="分红模式">
          <k-field-select v-model="formData.dividMode" data-dict="divid_mode"
                          @data-on-change="setDividMode"
                          :dataAllowblank="false"/></k-form-item>
        <k-form-item label="分红方式">
          <k-field-select v-model="formData.divMethod" data-dict="div_method" :dataAllowblank="false"/></k-form-item>

        <k-form-item label="单位分红金额" v-show="elementStyle[0].show">
          <k-field-text v-model="formData.perBonus" data-validate-type="number"
                        :data-digits="7" :data-max-length="10" data-min-value="(0" data-max-value="100)"
                        :dataAllowblank="elementStyle[0].allowBlank" @input="forceUpdate"/></k-form-item>
        <k-form-item label="分红总金额" v-show="elementStyle[1].show">
          <k-field-text v-model="formData.totalBonus" data-validate-type="number"
                        :data-digits="2" :data-max-length="15" data-min-value="(0"
                        :dataAllowblank="elementStyle[1].allowBlank" @input="forceUpdate"/></k-form-item>
        <k-form-item label="权益登记日">
          <k-field-date v-model="formData.registerDate"
                        :data-min-value="'('+minNavDate" :data-max-value="formData.exDividendDate==null || formData.exDividendDate=='' ? maxNavDate : formData.exDividendDate+')'"
                        :data-max-length="8"
                        @data-on-change="changeRegisterDate"
                        :dataAllowblank="false"/></k-form-item>
        <k-form-item label="除权日">
          <k-field-date v-model="formData.exDividendDate"
                        :data-min-value="formData.registerDate" :data-max-value="formData.paidDate==null || formData.paidDate=='' ? maxNavDate : formData.paidDate+')'"
                        :data-max-length="8"
                        :dataAllowblank="false"/></k-form-item>
        <k-form-item label="红利下发日">
          <k-field-date v-model="formData.paidDate"
                        :data-min-value="'('+formData.exDividendDate" :data-max-value="maxNavDate"
                        :data-max-length="8"
                        :dataAllowblank="false"/></k-form-item>


        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T81004.addT81004"
                 data-from="addT81004Form" :data-model="formData"
                 data-target="ta1004Grid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改分红方案   -->
    <k-popup ref="updT81004Popup" data-title="修改产品分红方案">
      <k-form ref="editUserForm" :data-col="2">

        <k-form-item label="产品代码">
          <k-field-select v-model="formData.prodCode"
                          :dataAllowblank="false" :data-disabled="true"/></k-form-item>
        <k-form-item label="分红模式">
          <k-field-select v-model="formData.dividMode" data-dict="divid_mode"
                          :dataAllowblank="false"
                          @data-on-change="setDividMode"/></k-form-item>
        <k-form-item label="分红方式">
          <k-field-select v-model="formData.divMethod" data-dict="div_method" :dataAllowblank="false"/></k-form-item>
        <k-form-item label="单位分红金额" v-show="elementStyle[0].show">
          <k-field-text v-model="formData.perBonus"
                        :data-digits="7" :data-max-length="10" data-min-value="(0" data-validate-type="number" data-max-value="100)"
                        :dataAllowblank="elementStyle[0].allowBlank"@input="forceUpdate"/></k-form-item>
        <k-form-item label="分红总金额" v-show="elementStyle[1].show">
          <k-field-text v-model="formData.totalBonus"
                        :data-digits="2" :data-max-length="15" data-min-value="(0" data-validate-type="number"
                        :dataAllowblank="elementStyle[1].allowBlank"@input="forceUpdate"/></k-form-item>
        <k-form-item label="权益登记日">
          <k-field-date v-model="formData.registerDate"
                        :data-min-value="'('+minNavDate" :data-max-value="formData.exDividendDate==null || formData.exDividendDate=='' ? maxNavDate : formData.exDividendDate+')'"
                        :data-max-length="8"
                        @data-on-change="changeRegisterDate"
                        :dataAllowblank="false" :data-disabled="true"/></k-form-item>
        <k-form-item label="除权日">
          <k-field-date v-model="formData.exDividendDate"
                        :data-min-value="formData.registerDate" :data-max-value="formData.paidDate==null || formData.paidDate=='' ? maxNavDate : formData.paidDate+')'"
                        :data-max-length="8"
                        :dataAllowblank="false"/></k-form-item>
        <k-form-item label="红利下发日">
          <k-field-date v-model="formData.paidDate"
                        :data-min-value="'('+formData.exDividendDate" :data-max-value="maxNavDate"
                        :data-max-length="8"
                        :dataAllowblank="false"/></k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T81004.updateT81004"
                 data-from="editUserForm" :data-model="formData"
                 data-target="ta1004Grid">
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
  name:"M81004",
  data() {
    return {
      formData: {},
      selectRowData: {},
      minNavDate: "",
      maxNavDate: "",
      elementStyle: [
        {
          name: 'perBonus',  // 单位分红
          allowBlank: true,
          show: true
        },{
          name: 'totalBonus', // 总额分红
          allowBlank: true,
          show: true
        }

      ],
      prodCode: ""
    };
  },

  methods: {
    forceUpdate(){
      this.$forceUpdate();
    },
    selectRow(row, column, event) {
      const _this = this
      _this.selectRowData = assign({}, row)
      _this.formData = assign({}, row)
    },

    setNavDatePeriod(value) {

      // 查询产品周期信息，设置产品成立日为分红登记最小日期，开放结束日为分红登记最大日期
      this.httpUtil.comnQuery({
        action: "T8ProdPeriod.findT8ProdPeriods",
        params: {prodCode: value}
      }).then(data => {
        let rows = data.rows;

        //console.log("rows is =======>", rows)
        this.handlePeriodData(rows);
      });

    },

    //处理数据
    handlePeriodData(rows) {
      rows.map(row => {

        this.minNavDate = rows[0].establishDate;
        this.maxNavDate = rows[0].windingDate;

      });
    },

    setDividMode(value) {
      let dividMode = value;

      if (dividMode == '1'){
        // 总额分红

        // 单位分红隐藏，总金额分红显示，且必输
        this.elementStyle[0].allowBlank = true;
        this.elementStyle[0].show       = false;
        this.formData.perBonus = "";

        this.elementStyle[1].allowBlank = false;
        this.elementStyle[1].show       = true;


      } else {
        // 单位分红
        // 总金额分红隐藏，单位分红显示，且必输
        this.elementStyle[0].allowBlank = false;
        this.elementStyle[0].show       = true;

        this.elementStyle[1].allowBlank = true;
        this.elementStyle[1].show       = false;
        this.formData.totalBonus = "";
      }
    },

    changeRegisterDate(value) {
      let registerDate = value;

    },

    modifyProdBonus(row){
      const _this = this;

      let prodCode = row.prodCode;

      this.setNavDatePeriod(prodCode);

      let dividMode = row.dividMode
      this.setDividMode(dividMode)

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

  created () {
    this.prodCode = this.$route.query.prodCode;
  }
};
</script>
