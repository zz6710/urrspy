<template>
  <div>
    <k-form-search d="ta1005Grid" data-model-name="T81005">
      <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" d="addT81005Popup">
        <md-icon md-src="/static/svg/add.svg" />新增
      </k-btn>
    </k-form-search>

    <k-grid ref="ta1005Grid"
            data-action='T81005.findT81005s' data-operate-column-position="end"
            @data-row-select="selectRow" data-operate-width="200px" :data-before-load="loadProdCode">

      <template slot="operate" slot-scope="scope">
        <k-btn data-functype="POPUP" data-size="mini" d="updT81005Popup"
               class="md-info md-just-icon md-simple"
               :data-disabled="scope.row.row.execStatus != '0'" data-descript="修改分红计划记录">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn data-functype="SUBMIT" data-size="mini" data-type="danger" class="md-danger md-just-icon md-simple"
               data-action="T81005.deleteT81005" :data-model="selectRowData" d="ta1005Grid"
               data-confirm data-descript="删除分红计划记录"
               :data-disabled="scope.row.row.execStatus != '0'">
          <md-icon>close</md-icon>
        </k-btn>
      </template>

      <k-grid-column data-name="prodCode" data-header="产品代码" data-sortable="true"></k-grid-column>
      <k-grid-column data-name="prodName" data-header="产品名称" data-sortable="true"></k-grid-column>
      <!--      <k-grid-column data-name="withdrawalStartDate" data-header="计提区间起始日" data-type="date"></k-grid-column>
            <k-grid-column data-name="withdrawalEndDate" data-header="计提区间结束日" data-type="date"></k-grid-column> -->
      <k-grid-column data-name="dividDate" data-header="分红计划下发日" data-type="date"></k-grid-column>
      <k-grid-column data-name="isAuto" data-header="是否自动生成结转方案" data-dict="lyesOno" data-width="120"></k-grid-column>
      <k-grid-column data-name="period" data-header="结转周期" data-dict="period"></k-grid-column>
      <k-grid-column data-name="frequence" data-header="结转频率" data-render="depositRender"></k-grid-column>
      <k-grid-column data-name="remark" data-header="备注"></k-grid-column>
    </k-grid>

    <!--    添加分红计划记录弹出框   -->
    <k-popup ref="addT81005Popup" data-title="添加产品分红计划方案">
      <k-form ref="addT81005Form" :data-col="2">

        <k-form-item label="产品代码">
          <k-field-select v-model="formData.prodCode" data-action="TaDict.findCurProdInfo"
                          data-display-field="prodCode,prodName" data-value-field="prodCode"
                          @data-on-change="setNavDatePeriod"
                          :dataAllowblank="false"/>
        </k-form-item>

        <k-form-item label="是否自动生成结转方案">
          <k-field-select v-model="formData.isAuto"
                          data-dict="lyesOno" @data-on-change="clearAll"
                          :dataAllowblank="false"/>
        </k-form-item>

        <k-form-item label="红利下发日" v-show="formData.isAuto != 1">
          <k-field-date v-model="formData.dividDate"
                        @data-on-change="checkDividDate(formData.dividDate)"
                        :data-max-length="8"
                        :dataAllowblank="false"/>
        </k-form-item>

        <k-form-item label="首次红利下发日" v-show="formData.isAuto == 1">
          <k-field-date v-model="formData.dividDate"
                        @data-on-change="checkDividDate(formData.dividDate)"
                        :data-max-length="8"
                        :dataAllowblank="false"/>
        </k-form-item>

        <k-form-item label="结转周期" v-show="formData.isAuto == 1">
          <k-field-select v-model="formData.period"
                          @data-on-change="clearfrequence"
                          data-dict="period"
                          @input="forceUpdate"
          />
        </k-form-item>

        <k-form-item label="结转频率" v-show="formData.isAuto == 1 & formData.period == 1">
          <k-field-select v-model="formData.frequence"
                          data-dict="frequence_month"    @input="forceUpdate">
          </k-field-select>
        </k-form-item>

        <k-form-item label="结转频率" v-show="formData.isAuto == 1 & formData.period == 2">
          <k-field-select v-model="formData.frequence"
                          data-dict="frequence_weekly">
          </k-field-select>
        </k-form-item>

        <k-form-item label="备注">
          <k-field-text v-model="formData.remark"
                        :data-max-length="80"
                        :dataAllowblank="true"/>
        </k-form-item>


        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T81005.addT81005"
                 data-from="addT81005Form" :data-model="formData" :data-handler="checkParam"
                 d="ta1005Grid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改分红计划方案   -->
    <k-popup ref="updT81005Popup" data-title="修改产品分红计划方案">
      <k-form ref="editUserForm" :data-col="2">

        <k-form-item label="产品代码">
          <k-field-select v-model="formData.prodCode" data-action="TaDict.findCurProdInfo"
                          data-display-field="prodName" data-value-field="prodCode"
                          @data-on-change="setNavDatePeriod"
                          :dataAllowblank="false" :data-disabled="true"/>
        </k-form-item>

        <k-form-item label="是否自动生成结转方案">
          <k-field-select v-model="formData.isAuto"
                          data-dict="lyesOno" @data-on-change="clearAll"
                          :dataAllowblank="false"/>
        </k-form-item>

        <k-form-item label="红利下发日" v-show="formData.isAuto != 1">
          <k-field-date v-model="formData.dividDate"
                        @data-on-blur="checkDividDate(formData.dividDate)"
                        :data-max-length="8"
                        :dataAllowblank="false"/>
        </k-form-item>

        <k-form-item label="首次红利下发日" v-show="formData.isAuto == 1">
          <k-field-date v-model="formData.dividDate"
                        @data-on-blur="checkDividDate(formData.dividDate)"
                        :data-max-length="8"
                        :dataAllowblank="false"/>
        </k-form-item>

        <k-form-item label="结转周期" v-show="formData.isAuto == 1">
          <k-field-select v-model="formData.period"
                          @data-on-change="clearfrequence"
                          data-dict="period"
                          @input="forceUpdate"
          />
        </k-form-item>

        <k-form-item label="结转频率" v-show="formData.isAuto==1 & formData.period == 1">
          <k-field-select v-model="formData.frequence"
                          data-dict="frequence_month"
                          @input="forceUpdate"
          />
        </k-form-item>

        <k-form-item label="结转频率" v-show="formData.isAuto==1 & formData.period == 2">
          <k-field-select v-model="formData.frequence"
                          data-dict="frequence_weekly"
          />
        </k-form-item>

        <k-form-item label="备注">
          <k-field-text v-model="formData.remark"
                        :data-max-length="80"
                        :dataAllowblank="true"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T81005.updateT81005"
                 data-from="editUserForm" :data-model="formData" :data-handler="checkParam"
                 d="ta1005Grid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
import {assign} from "lodash";
import Tools from '@/utils/tools.js'

export default {
  name:"M81005",
  data() {
    return {
      formData: {},
      selectRowData: {},
      minNavDate: "",
      maxNavDate: "",
      $kgrid: "",
    };
  },

  methods: {
    selectRow(row, column, event) {
      const _this = this
      _this.selectRowData = assign({}, row)
      _this.formData = assign({}, row)
    },
    forceUpdate(){
      this.$forceUpdate();
    },

    setNavDatePeriod(value) {

      // 查询产品周期信息，设置产品成立日为分红计划最小日期，开放结束日为分红计划最大日期
      this.httpUtil.comnQuery({
        action: "T8ProdPeriod.findT8ProdPeriods",
        params: {prodCode: value}
      }).then(data => {
        let rows = data.rows;
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
    loadProdCode(params) {

      if (this.prodCode != null && this.prodCode != '') {

        params.prodCode = this.prodCode;

        // 该值只用一次
        this.prodCode = "";
      }
      return params;
    },
    depositRender(row) {
      // 字典中个位数在加载时 不会使用字典，找了半天不知道为什么，推测是因为像Integer 那样对个位数的有缓存，所以暂时先吧字典键值 从10开始。
      if (row.period == '1') {
        return '每月' + (row.frequence) + '号'
      } else if (row.period == '2') {
        return '每周' + (row.frequence)
      } else if(row.period == '3'){
        return '每日'
      }else {
        return '';
      }
    },
    checkDividDate(val) {
      if (val != null && val != "") {
        let date = new Date();
        date.setMinutes(date.getMinutes() - date.getTimezoneOffset()); // toJSON 的时区补偿
        let newdate = date.toJSON().substr(0, 11).replace(/[-T]/g, '')

        if (newdate > val) {
          Tools.alert("分红下发时间不能小于当前时间", "danger");
          this.formData.dividDate = "";
        }
      }

    },
    checkParam() {
      if (this.formData.isAuto == 1) {

        if (this.formData.period == null || this.formData.frequence == null ){
          Tools.alert("自动结转方案时,结转周期/结转频率不可为空", "danger");
          return false;
        }


      }
      return true;
    },
    clearAll() {
      if (this.formData.isAuto == 0) {
        this.formData.frequence = "";
        this.formData.period = "";
      }
    }

  }
  ,
  created() {
    this.prodCode = this.$route.query.prodCode;
  }
  ,


}
;
</script>
