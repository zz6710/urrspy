<template>
  <div class="py-page">
    <div>
      <k-form-search-customize ref="QueryData" v-model="searchParam" data-target="indexConfigGrid" dataLabelWidth="100px">
        <k-form-item label="目标库">
          <k-field-select v-model="searchParam.targetDb" data-placeholder="目标库" :data-data="dbList" data-value-field="dbName" data-display-field="dbName"  @data-on-change="getTableList"/>
        </k-form-item>

        <k-form-item label="目标表名">
          <k-field-select v-model="searchParam.targetTable" data-placeholder="目标表名" :data-data="tableList" data-value-field="tableName" data-display-field="tableName"/>
        </k-form-item>

        <k-form-item label="备份方式">
          <k-field-select v-model="searchParam.type" data-placeholder="备份方式" data-dict="bak_type"/>
        </k-form-item>

        <k-form-item label="自动建表">
          <k-field-select v-model="searchParam.autoTable" data-placeholder="自动建表" data-dict="bak_auto_table"/>
        </k-form-item>

        <k-form-item label="归档频率">
          <k-field-text v-model="searchParam.reportTable" data-placeholder="归档频率" />
        </k-form-item>

        <k-form-item label="源数据">
          <k-field-select v-model="searchParam.autoTable" data-placeholder="源数据" data-dict="bak_source_data"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.indexConfigAdd={}" slot="button"
               data-target="addIndexConfigPopup">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
      </div>
      <k-grid ref="indexConfigGrid" @data-row-select="selectRow"  data-operate-width="300px" data-action="SysBakConfigPO.getBakConfigList"
              @init="(id)=>{this.$kgrid = id}" :data-checkbox="false" data-checkbox-id="id">
        <k-grid-column data-align="left" data-header="目标库" data-name="targetDb" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="目标表名" data-name="targetTable" data-width="200"></k-grid-column>
        <k-grid-column data-align="left" data-header="日期标识字段" data-name="targetField"></k-grid-column>
        <k-grid-column data-align="left" data-header="备份方式" data-name="type" data-dict="bak_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="下次归档日期" data-name="nextDate"></k-grid-column>
        <k-grid-column data-align="left" data-header="下次归档起始日期" data-date-format="yyyyMMdd" data-name="nextLeftDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="下次归档终止日期" data-name="nextRightDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="删除起始日期" data-name="deleteStartDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="删除结束日期" data-name="deleteEndDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="删除方式" data-dict="delete_type" data-name="deleteType" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="删除频率" data-name="deleteFrequency" data-width="150"></k-grid-column>
        <template slot="operate" slot-scope="scope" system-aligen>
          <k-btn data-functype="POPUP" data-confirm data-size="mini" class="btn-custom-text" :data-handler="selectRow"
                 data-target="editIndexConfigPopup" data-descript="修改归档配置信息">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="SysBakConfigPO.deleteBakConfig"
                 :data-confirm="true" data-size="mini" data-type="danger" data-target="indexConfigGrid" data-descript="删除">
            删除
          </k-btn>
          <k-btn class="btn-custom-text" data-descript="详情" data-functype="POPUP" data-size="mini"
                 :data-handler="selectRow" data-target="ReportIndexConfigPopup">
            详情
          </k-btn>
          <k-btn class="btn-custom-text" data-descript="还原" data-functype="POPUP" data-size="mini"
                 :data-handler="selectRow" data-target="RedoDataPopup">
            还原
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    归档配置新增弹出框   -->
    <k-popup ref="addIndexConfigPopup" data-title="新增">
      <k-form ref="addIndexConfigForm" :data-col="2">
        <k-form-item label="目标库">
          <k-field-select v-model="indexConfigAdd.targetDb" :data-data="dbList" data-value-field="dbName" data-display-field="dbName" @data-on-change="getTableList"/>
        </k-form-item>
        <k-form-item label="目标表名">
          <k-field-select v-model="indexConfigAdd.targetTable" :data-data="tableList" data-value-field="tableName" data-display-field="tableName"/>
        </k-form-item>
        <k-form-item label="日期标识字段" >
          <k-field-text v-model="indexConfigAdd.targetField" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="备份方式">
          <k-field-select v-model="indexConfigAdd.type" data-dict="bak_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="自动建表">
          <k-field-select v-model="indexConfigAdd.autoTable" data-dict="bak_auto_table" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="归档频率">
          <k-field-text v-model="indexConfigAdd.frequency" data-validate-type="number" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="分表阈值">
          <k-field-text v-model="indexConfigAdd.threshold"/>
        </k-form-item>
        <k-form-item label="源数据">
          <k-field-select v-model="indexConfigAdd.sourceData" data-dict="bak_source_data" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="字段自动适配">
          <k-field-select v-model="indexConfigAdd.fieldChange" data-dict="bak_field_change" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="生效日期">
          <k-field-date v-model="indexConfigAdd.beginDate" data-date-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="删除起始日期">
          <k-field-date v-model="indexConfigAdd.deleteStartDate" data-date-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="删除结束日期">
          <k-field-date v-model="indexConfigAdd.deleteEndDate" data-date-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="删除方式">
          <k-field-select v-model="indexConfigAdd.deleteType" data-dict="delete_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="删除频率">
          <k-field-text v-model="indexConfigAdd.deleteFrequency" :data-allowblank="false"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  data-from="addIndexConfigPopup"
                 :data-model="indexConfigAdd" data-target="indexConfigGrid" data-action="SysBakConfigPO.addBakConfig">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--   数据归档修改弹出框   -->
    <k-popup ref="editIndexConfigPopup" data-title="归档配置修改">
      <k-form ref="editIndexConfigForm" :data-col="2">

        <k-form-item label="主键id">
          <k-field-text v-model="indexConfig.id" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="目标库">
          <k-field-text v-model="indexConfig.targetDb" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="目标表名">
          <k-field-text v-model="indexConfig.targetTable" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="日期标识字段" >
          <k-field-text v-model="indexConfig.targetField" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="备份方式">
          <k-field-select v-model="indexConfig.type" data-dict="bak_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="自动建表">
          <k-field-select v-model="indexConfig.autoTable" data-dict="bak_auto_table" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="归档频率">
          <k-field-text v-model="indexConfig.frequency" data-validate-type="number" :data-allowblank="false"/>
         </k-form-item>
        <k-form-item label="分表阈值">
          <k-field-text v-model="indexConfig.threshold"/>
        </k-form-item>
        <k-form-item label="源数据">
          <k-field-select v-model="indexConfig.sourceData" data-dict="bak_source_data" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="字段自动适配">
          <k-field-select v-model="indexConfig.fieldChange" data-dict="bak_field_change" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="下次归档日期">
          <k-field-date v-model="indexConfig.nextDate" data-date-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="下次归档起始日期">
          <k-field-date v-model="indexConfig.nextLeftDate" data-date-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="下次归档终止日期">
          <k-field-date v-model="indexConfig.nextRightDate" data-date-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="删除起始日期">
          <k-field-date v-model="indexConfig.deleteStartDate" data-date-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="删除结束日期">
          <k-field-date v-model="indexConfig.deleteEndDate" data-date-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="删除方式">
          <k-field-select v-model="indexConfig.deleteType" data-dict="delete_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="删除频率">
          <k-field-text v-model="indexConfig.deleteFrequency" :data-allowblank="false"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="editIndexConfigPopup" @click="updateBakConfig"
                 :data-model="indexConfig" data-target="indexConfigGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>

    <!--   归档备份详情   -->
    <k-popup ref="ReportIndexConfigPopup" data-title="归档备份详情">
      <k-form :data-col="3">
        <k-form-item label="配置ID">
          <k-field-display v-model="indexConfig.id"/>
        </k-form-item>
        <k-form-item label="目标库">
          <k-field-display v-model="indexConfig.targetDb"/>
        </k-form-item>
        <k-form-item label="目标表名">
          <k-field-display v-model="indexConfig.targetTable" />
        </k-form-item>
        <k-form-item label="日期标识字段">
          <k-field-display v-model="indexConfig.targetField" />
        </k-form-item>
        <k-form-item label="已归档数据量" >
          <k-field-display v-model="indexConfig.tableBakNum"/>
        </k-form-item>
        <k-form-item label="备份方式">
          <k-field-display v-model="indexConfig.type" data-dict="bak_type"/>
        </k-form-item>
        <k-form-item label="自动建表">
          <k-field-display v-model="indexConfig.autoTable" data-dict="bak_auto_table"/>
        </k-form-item>
        <k-form-item label="分表阈值">
          <k-field-display v-model="indexConfig.threshold"/>
        </k-form-item>
        <k-form-item label="归档频率">
          <k-field-display v-model="indexConfig.frequency"/>
        </k-form-item>
        <k-form-item label="源数据">
          <k-field-display v-model="indexConfig.autoTable" data-dict="bak_source_data"/>
        </k-form-item>
        <k-form-item label="字段自动适配">
          <k-field-display v-model="indexConfig.fieldChange" data-dict="bak_field_change"/>
        </k-form-item>
        <k-form-item label="生效日期">
          <k-field-display v-model="indexConfig.beginDate"/>
        </k-form-item>
        <k-form-item label="上次备份日期">
          <k-field-display v-model="indexConfig.lastDate"/>
        </k-form-item>
        <k-form-item label="下次备份日期">
          <k-field-display v-model="indexConfig.nextDate"/>
        </k-form-item>
        <k-form-item label="上次归档起始日期">
          <k-field-display v-model="indexConfig.lastLeftDate"/>
        </k-form-item>
        <k-form-item label="上次归档终止日期">
          <k-field-display v-model="indexConfig.lastRightDate"/>
        </k-form-item>
        <k-form-item label="下次归档起始日期">
          <k-field-display v-model="indexConfig.nextLeftDate"/>
        </k-form-item>
        <k-form-item label="下次归档终止日期">
          <k-field-display v-model="indexConfig.nextRightDate"/>
        </k-form-item>
        <k-form-item label="创建日期">
          <k-field-display v-model="indexConfig.createTime"/>
        </k-form-item>
        <k-form-item label="修改日期">
          <k-field-display v-model="indexConfig.updateTime"/>
        </k-form-item>

        <k-form-item label="删除起始日期">
          <k-field-display v-model="indexConfig.deleteStartDate" data-date-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="删除结束日期">
          <k-field-display v-model="indexConfig.deleteEndDate" data-date-format="yyyyMMdd" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="删除方式">
          <k-field-display v-model="indexConfig.deleteType" data-dict="delete_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="删除频率">
          <k-field-display v-model="indexConfig.deleteFrequency" :data-allowblank="false"/>
        </k-form-item>
      </k-form>
    </k-popup>

    <!--   还原已归档数据   -->
    <k-popup ref="RedoDataPopup" data-title="还原已归档数据库">
      <k-form :data-col="1">
        <k-form-item label="目标库">
          <k-field-display v-model="indexConfig.targetDb" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="目标表名">
          <k-field-display v-model="indexConfig.targetTable" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="还原数据起始日期">
          <k-field-date v-model="indexConfig.redoStartDate" data-date-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="还原数据结束日期" >
          <k-field-date v-model="indexConfig.redoEndDate" data-date-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="RedoDataPopup" @click="redoData"
                 :data-model="indexConfig" >
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
  import Tools from "@/utils/tools";

  export default {
    name:"bakConfig",
    data() {
      return {
        searchParam:{},
        indexConfig:{},
        bakCollection:{},
        reportTableDict:{},
        indexConfigAdd:{},
        dbList:{},
        tableList:{}
      };
    },
    created() {
      this.getBakConfigList();
      this.getDbList();
    },
    methods: {
      getDbList() {
        this.$set(this.searchParam, 'dbList', '');
        this.httpUtil.comnQuery({
          action: "SysBakConfigPO.getDbList",
          params: null
        }).then(data => {
          this.dbList = data.rows;
        }).catch({})
      },
      getTableList(value) {
        this.$set(this.searchParam, 'tableList', '');
          this.httpUtil.comnQuery({
            action: "SysBakConfigPO.getTableList",
            params: {targetDb: value}
          }).then(data => {
            this.tableList = data.rows;
          }).catch({})
      },
      //查询归档配置
      getBakConfigList() {
        this.httpUtil.comnQuery({
          action: "SysBakConfigPO.getBakConfigList",
          params: null
        }).then(data => {
          this.reportTableDict = data.rows;
        }).catch({})
      },
      selectRow(row) {
        this.indexConfig = Object.assign({}, row);
      },
      selectCollectionRow() {
        this.httpUtil.comnQuery({
          action: "SysBakConfigPO.getBakCollectionList",
          params: this.indexConfig
        }).then(data => {
          this.bakCollection = data;
        }).catch({})
      },
      //更新归档配置
      updateBakConfig(){
        this.httpUtil.comnQuery({
          action: "SysBakConfigPO.updateBakConfig",
          params: this.indexConfig
        }).then(data => {
          this.$refs.editIndexConfigPopup.close();
          Tools.alert(data.returnmsg);
          this.$refs.QueryData.query();
        })
      },
      //归档数据还原
      redoData(){
        this.httpUtil.ajax({
          url:"/server/form/DpsApp/bakRedo/redoData.action",
          params: this.indexConfig
        }).then(res=>{
          this.$refs.addIndexConfigPopup.close();
          this.$refs.indexConfigGrid.load();
          Tools.alert(res.returnmsg);
          this.$refs.QueryData.query();
        })
      },
     }
  };
</script>
