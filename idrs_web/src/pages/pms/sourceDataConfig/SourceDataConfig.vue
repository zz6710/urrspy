<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="SourceDataConfigModel" v-model="queryParam" data-target="sourceDataConfigGrid">
        <k-form-item label="数据库表">
          <k-field-select v-model="queryParam.tableName" data-action="SourceDataConfigModel.findTables"
                          data-display-field="tablesName" data-value-field="tables"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" slot="button" :data-handler="addDataHandler" data-target="addConfig" v-if="global.isShowAuthorityButton('SourceDataConfigModel.addSourceDataConfigModel')">
            <md-icon md-src="/static/svg/add.svg" />新增
          </k-btn>
        </div>
      </div>
      <k-grid ref="sourceDataConfigGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="250px"
              data-action="SourceDataConfigModel.findSourceDataConfigModel" :data-autoload="true" :data-display="false">
        <k-grid-column data-header="ID" data-name="id" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="数据库表" data-name="tableName" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="对比规则" data-name="comparisonRules" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="数据库表" data-name="tablesName"></k-grid-column>
        <k-grid-column data-header="数据确认规则" data-name="remindType" data-dict="remindType"></k-grid-column>
        <k-grid-column data-header="涉及报表" data-name="relatedReport" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="涉及报表名称" data-name="reportName"></k-grid-column>
        <k-grid-column data-header="更新时间" data-name="updDt" ></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="编辑源数据配置信息" data-size="mini" data-functype="POPUP" v-if="global.isShowAuthorityButton('SourceDataConfigModel.addSourceDataConfigModel')"
                 data-target="editConfig">
            编辑
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="SourceDataConfigModel.deleteSourceDataConfigModel" v-if="global.isShowAuthorityButton('SourceDataConfigModel.deleteSourceDataConfigModel')"
                 data-type="danger" data-target="sourceDataConfigGrid" :data-confirm="true" data-descript="删除源数据配置信息">
            删除
          </k-btn>
          <k-btn class="btn-custom-text" data-descript="源数据配置信息详情" data-size="mini" data-functype="POPUP" data-target="msgConfig">
            详情
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加弹出框   -->
    <k-popup ref="addConfig" data-title="新增" :dataDialogDrag="true">
      <EditComp ref="addComp" @loadGriding="loadGriding"
                :info="{}"
                :fieldCodeFGrid="fieldCodeGridData"
                :remindFields2="null"
                :isEdit="false"
                :disabledVal="false"/>
    </k-popup>

    <!--    编辑弹出框   -->
    <k-popup ref="editConfig" data-title="修改" :dataDialogDrag="true">
      <EditComp ref="editComp" @loadGriding="loadGriding"
                :info="formData"
                :fieldCodeFGrid="fieldCodeGridData"
                :remindFields2="remindFields"
                :isEdit="true"
                :disabledVal="false"/>
    </k-popup>

    <!--    详情弹出框   -->
    <k-popup ref="msgConfig" data-title="详情" :dataDialogDrag="true">
      <EditComp ref="editComp" @loadGriding="loadGriding"
                :info="formData"
                :fieldCodeFGrid="fieldCodeGridData"
                :remindFields2="remindFields"
                :isEdit="true"
                :disabledVal="true"/>
    </k-popup>
  </div>

</template>

<script>
  import Tools from "@/utils/tools";
  import EditComp from "@/pages/pms/sourceDataConfig/EditSourceDataConfig";
  export default {
    name: "SourceDataConfig",
    components: {
      EditComp,
    },
    data() {
      return {
        fieldCodeGridData: {},
        queryParam: {},
        formData: {},
        selectRowData: {},
        remindFields: [],
        reports: []
      };
    },
    methods: {
      loadGriding(val){
        this.$refs.addConfig.close();
        this.$refs.editConfig.close();
        this.$refs.sourceDataConfigGrid.load(this.queryParam);
      },
      addDataHandler(){
        this.formData = {};
        this.$set(this.fieldCodeGridData, "rows", []);
        this.$set(this.fieldCodeGridData, "total", 0);
      },
      selectRow(row) {
        this.selectRowData = Object.assign({}, row);
        this.formData = Object.assign({}, row);
        let fields = this.formData.fieldCodeGridData;
        this.$set(this.fieldCodeGridData, "rows", fields);
        this.$set(this.fieldCodeGridData, "total", fields.length);
        this.httpUtil.comnQuery({
          action: "SourceDataConfigModel.findRemindField",
          params: {tableName: this.formData.tableName},
          successAlert: false
        }).then(data => {
          if(data.rows.length>0){
            this.remindFields=data.rows;
          }
        });
      },
    }
  };
</script>

<style scoped>

</style>
