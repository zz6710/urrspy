<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="DataNaturalKeyModel" v-model="queryParam" data-target="dataNaturalKeyGrid">
        <k-form-item label="所属层级">
          <k-field-select v-model="queryParam.hierarchy" data-dict="dataLayer"/>
        </k-form-item>
        <k-form-item label="数据库表">
          <k-field-select v-model="queryParam.tableName" data-action="SourceDataConfigModel.findTables"
                          data-display-field="tablesName" data-value-field="tables"/>
        </k-form-item>
        
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" slot="button" :data-handler="addDataHandler"
               data-target="addConfig" v-if="global.isShowAuthorityButton('DataNaturalKeyModel.addDataNarutalKeyModel')">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
        </div>
      </div>
      <k-grid ref="dataNaturalKeyGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="250px"
              data-action="DataNaturalKeyModel.findDataNarutalKeyModel" :data-autoload="true" :data-display="false">
        <k-grid-column data-header="ID" data-name="id" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="所属层级" data-name="hierarchy" data-dict="dataLayer"></k-grid-column>
        <k-grid-column data-header="数据库表" data-name="tableName" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="数据库表名" data-name="tablesName"></k-grid-column>
        <k-grid-column data-header="更新时间" data-name="updDt" ></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="编辑各层级业务主键信息" data-size="mini" data-functype="POPUP" v-if="global.isShowAuthorityButton('DataNaturalKeyModel.addDataNarutalKeyModel')"
                 data-target="editConfig">
            编辑
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="DataNaturalKeyModel.deleteDataNarutalKeyModel" v-if="global.isShowAuthorityButton('DataNaturalKeyModel.deleteDataNarutalKeyModel')"
                 data-type="danger" data-target="dataNaturalKeyGrid" :data-confirm="true" data-descript="删除各层级业务主键信息">
            删除
          </k-btn>
          <k-btn class="btn-custom-text" data-descript="各层级业务主键信息详情" data-size="mini" data-functype="POPUP" data-target="msgConfig">
            详情
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    添加弹出框   -->
    <k-popup ref="addConfig" data-title="新增" :dataDialogDrag="true">
      <EditComp ref="addComp" @loadGriding="loadGriding"
                :info="{}"
                :naturalKeyFGrid="naturalKeyGrid"
                :naturalKeys2="null"
                :isEdit="false"
                :disabledVal="false"/>
    </k-popup>

    <!--    编辑弹出框   -->
    <k-popup ref="editConfig" data-title="修改" :dataDialogDrag="true">
      <EditComp ref="editComp" @loadGriding="loadGriding"
                :info="formData"
                :naturalKeyFGrid="naturalKeyGrid"
                :naturalKeys2="naturalKeys"
                :isEdit="true"
                :disabledVal="false"/>
    </k-popup>

    <!--    详情弹出框   -->
    <k-popup ref="msgConfig" data-title="详情" :dataDialogDrag="true">
      <EditComp ref="editComp" @loadGriding="loadGriding"
                :info="formData"
                :naturalKeyFGrid="naturalKeyGrid"
                :naturalKeys2="naturalKeys"
                :isEdit="true"
                :disabledVal="true"/>
    </k-popup>
  </div>
</template>

<script>
  import {assign} from "lodash";
  import EditComp from "@/pages/pms/dataNaturalKey/EditDataNaturalKey";
  export default {
    name: "DataNaturalKeyModel",
    components: {EditComp},
    data() {
      return {
        queryParam: {},
        formData: {},
        selectRowData: {},
        tableNames: [],
        naturalKeyGrid: {},
        naturalKeys: []
      };
    },
    methods: {
      loadGriding(val){
        this.$refs.addConfig.close();
        this.$refs.editConfig.close();
        this.$refs.dataNaturalKeyGrid.load(this.queryParam);
      },
      addDataHandler(){
        this.formData = {};
        this.$set(this.naturalKeyGrid, "rows", []);
        this.$set(this.naturalKeyGrid, "total", 0);
      },
      selectRow(row) {
        this.selectRowData = assign({}, row)
        this.formData = assign({}, row)
        let fields = this.formData.naturalKeyGridData;
        this.$set(this.naturalKeyGrid, "rows", fields);
        this.$set(this.naturalKeyGrid, "total", fields.length);
        this.httpUtil.comnQuery({
          action: "DataNaturalKeyModel.findNaturalKey",
          params: {tableName: this.formData.tableName},
          successAlert: false
        }).then(data => {
          if(data.rows.length>0){
            this.naturalKeys=data.rows;
          }
        });
      },
    }
  };
</script>
