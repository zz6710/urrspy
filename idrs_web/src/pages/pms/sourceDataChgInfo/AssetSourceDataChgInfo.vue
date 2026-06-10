<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="SourceDataChgInfoModel" v-model="queryParam" data-target="assetSourceDataChgInfoGrid">
        <k-form-item label="数据确认状态">
          <k-field-select v-model="queryParam.status" data-dict="sourceDataStatus"/>
        </k-form-item>
        <k-form-item label="变更日期">
          <k-field-date v-model="queryParam.dealDate"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <k-grid ref="assetSourceDataChgInfoGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="200px" data-action="SourceDataChgInfoModel.findAssetSourceDataChgInfoModelForOne" :data-autoload="true">
        <k-grid-column data-header="数据库表名" data-name="tableName"></k-grid-column>
        <k-grid-column data-header="主键信息" data-name="naturalKeys" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="主键信息名" data-name="naturalKeysName"></k-grid-column>
        <k-grid-column data-header="数据涉及报表" data-name="reportName" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="数据涉及报表名" data-name="reportsName"></k-grid-column>
        <k-grid-column data-header="涉及字段" data-name="changeField" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="数据库表" data-name="tables" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="原数据" data-name="fieldOld" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="新数据" data-name="fieldNew" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="数据状态" data-name="status" data-dict="sourceDataStatus"></k-grid-column>
        <k-grid-column data-header="变更日期" data-name="dealDate"></k-grid-column>
        <k-grid-column data-header="确认日期" data-name="updDt"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="确认源数据变化信息" data-functype="POPUP" v-if="global.isShowAuthorityButton('SourceDataChgInfoModel.confirmSourceDataChgInfoModel')"
                 data-target="confirmSourceDataChgInfoPopup" :data-disabled="scope.row.row.status === '1'">
            确认
          </k-btn>
          <k-btn class="btn-custom-text" data-descript="查看源数据变化信息" data-functype="POPUP" v-if="global.isShowAuthorityButton('SourceDataChgInfoModel.confirmSourceDataChgInfoModel')"
                 data-target="msgSourceDataChgInfoPopup">
            详情
          </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    确认SourceDataChginfo弹出框   -->
    <k-popup ref="confirmSourceDataChgInfoPopup" data-title="确认">
      <EditComp ref="editComp" @loadGriding="loadGriding"
                :info="formData"
                :sourceDataGrid="sourceDataChgs"
                :disabledVal="false"/>
    </k-popup>

    <!--    SourceDataChginfo详情弹出框   -->
    <k-popup ref="msgSourceDataChgInfoPopup" data-title="详情">
      <MsgComp ref="msgComp" @loadGriding="loadGriding"
                :info="formData"
                :sourceDataGrid="sourceDataChgs"
                :disabledVal="false" />
    </k-popup>
  </div>
</template>

<script>
  import {assign} from "lodash";
  import EditComp from "@/pages/pms/sourceDataChgInfo/ConfirmSourceData";
  import MsgComp from "@/pages/pms/sourceDataChgInfo/MsgSourceData";
  import Tools from "@/utils/tools";
  export default {
    name: "AssetSourceDataChgInfo",
    components: {EditComp,MsgComp},
    data() {
      return {
        queryParam: {},
        formData: {},
        selectRowData: {},
        remindFields: [],
        reports: [],
        sourceDataChgs: {}
      };
    },
    created() {
      if(this.$route.query!=null && this.$route.query!==''&& this.$route.query!==undefined){
        let status=this.$route.query.status;
        let deal_date=this.$route.query.dealDate;
        this.$set(this.queryParam,"status",status);
        this.$set(this.queryParam,"dealDate",deal_date);
        this.$nextTick(() => {
          this.$refs.assetSourceDataChgInfoGrid.load(this.queryParam);
        });
      }
    },
    methods: {
      loadGriding(val){
        this.$refs.confirmSourceDataChgInfoPopup.close();
        this.$refs.assetSourceDataChgInfoGrid.load(this.queryParam);
      },
      selectRow(row) {
        this.selectRowData = assign({}, row)
        this.formData = assign({}, row)
      },
    }
  };
</script>
