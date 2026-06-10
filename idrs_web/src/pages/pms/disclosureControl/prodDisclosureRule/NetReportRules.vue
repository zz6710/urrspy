<template>

  <div>
    <k-form-search-customize data-target="NetReportRulesGrid" v-model="SearchParam">
      <k-form-item label="运作模式">
        <k-field-select v-model="SearchParam.operationMode" data-dict="operation_mode"
                        data-display-field="itemkey,itemval" data-value-field="itemkey" />
      </k-form-item>
      <k-form-item label="规律开放周期">
        <k-field-select v-model="SearchParam.regularOpenCycle" data-dict="regular_open_cycle"
                        data-display-field="itemkey,itemval" data-value-field="itemkey" />
      </k-form-item>
      <k-btn slot="button" class="md-rose" data-functype="POPUP" :data-handler="()=>this.formData={}" style="width:76px"
             data-target="addNetReportRulesPopup" v-if="global.isShowAuthorityButton('NetReportRules.addNetReportRules')">
        <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
    </k-form-search-customize>

    <k-grid ref="NetReportRulesGrid" @data-row-select="selectRow" data-action="NetReportRules.findNetReportRules"
            data-fixed="right" data-operate-width="170px">
      <k-grid-column data-header="id" data-name="ID" data-hidden="true"/>
      <k-grid-column data-header="运作模式" data-name="operationMode" data-dict="operation_mode" data-width="120"/>
      <k-grid-column data-header="规律开放周期" data-name="regularOpenCycle" data-dict="regular_open_cycle" data-width="120"/>
      <k-grid-column data-header="报送规则" data-name="reportRules" data-dict="report_rules" data-width="120"/>
      <k-grid-column data-header="报送频率" data-name="reportFreqVal" data-width="120" />
      <k-grid-column data-header="频率长度" data-name="lengthFreq" data-hidden="true"/>
      <k-grid-column data-header="报送频率" data-name="reportFreq" data-dict="report_freq" data-hidden="true"/>
      <k-grid-column data-header="具体日期" data-name="specificDate" data-hidden="true"/>
      <k-grid-column data-header="具体日期" data-name="specificDateVal" data-width="120"/>
      <k-grid-column data-header="报送估值日" data-name="reportDate" data-dict="report_date" data-width="120"/>
      <k-grid-column data-header="月末是否报送" data-name="reportMonth" data-dict="1yes2no" data-width="120"/>
      <k-grid-column data-header="确认日是否报送" data-name="reportConfirmDate" data-dict="1yes2no" data-width="120"/>
      <template slot="operate" slot-scope="scope">
        <k-btn class="btn-custom-text specialClass" data-descript="修改" data-functype="POPUP" data-size="mini"
               data-target="editNetReportRulesPopup" v-if="global.isShowAuthorityButton('NetReportRules.updateNetReportRules')">
          修改
        </k-btn>
        <k-btn class="btn-custom-text specialClass" data-functype="SUBMIT" data-size="mini" data-action="NetReportRules.deleteNetReportRules"
               data-type="danger" data-target="NetReportRulesGrid" :data-confirm="true" data-descript="删除"
               v-if="global.isShowAuthorityButton('NetReportRules.deleteNetReportRules')" >
          删除
        </k-btn>
      </template>
    </k-grid>


    <!--    添加弹出框   -->
    <k-popup ref="addNetReportRulesPopup" data-title="新增" :dataDialogDrag="true">
      <EditComp @loadGrid="loadGrid" ref="addComp" :info="{}" :disabledVal="false"/>
    </k-popup>

    <!--    修改弹出框   -->
    <k-popup ref="editNetReportRulesPopup" data-title="修改" :dataDialogDrag="true">
      <EditComp  @loadGrid="loadGrid" ref="editComp" :info="formData" :disabledVal="true"/>
    </k-popup>

  </div>

</template>

<script>
import EditComp from "./NetReportRulesEdit";
export default {
  name: "NetReportRules",
  components: {
    EditComp
  },
  props: {
    disabled:{
      type : Boolean
    }
  },
  data() {
    return {
      formData: {},
      selectRowData: {},
      SearchParam: {},//查询参数
    };
  },
  methods: {

    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },

    loadGrid(val){
      this.$refs.addNetReportRulesPopup.close();
      this.$refs.editNetReportRulesPopup.close();
      this.$refs.NetReportRulesGrid.load(this.SearchParam);
    },

  },
  watch: {

  }
};
</script>

<style scoped>
>>> .el-table__cell {
  padding: 1px 0 !important;
}
>>> .specialClass > .md-ripple{
  padding: 5px !important;
}
</style>
