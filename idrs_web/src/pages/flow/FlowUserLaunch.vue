<template>
  <div>
    <k-form-search-customize data-target="grid" v-model="queryParam">
      <k-form-item label="流程名">
        <k-field-select v-model="queryParamProcessName" data-display-field="text" data-value-field="id"
                        data-url="/wf/process/select2/allProcessNames.json"/>
      </k-form-item>
      <k-form-item label="状态">
        <k-field-select v-model="queryParamStatus"
                        data-dict="process_status" />
      </k-form-item>
      <k-form-item label="发起时间">
        <k-field-date v-model="queryParamCreateDateRange" data-type="daterange" />
      </k-form-item>
      <k-form-item label="结束时间">
        <k-field-date v-model="queryParamFinishDateRange" data-type="daterange" />
      </k-form-item>
    </k-form-search-customize>

    <k-grid
      ref="grid"
      @data-row-select="selectRow"
      :data-display="false"
      data-url='/wf/processInstance/created/list.json'>
      <k-grid-column data-align="center"
                     data-header="流程名"
                     data-name="processDisplayName"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="流程状态"
                     data-name="status"
                     data-dict="process_status"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="发起时间"
                     data-name="createDate"
                     data-render="renderCreateDateTime"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="结束时间"
                     data-name="finishDate"
                     data-render="renderFinishDateTime"
      ></k-grid-column>
      <template slot="operate" slot-scope="{row}">
        <k-btn class="md-info md-just-icon md-simple" data-descript="详情" data-functype="POPUP" data-size="mini"
               data-target="editPopup">
          <md-icon>library_books</md-icon>
        </k-btn>
        <k-btn class="md-danger md-just-icon md-simple"
               :data-disabled="row.row.status == global.wf_process_status.finish ||
                                row.row.status == global.wf_process_status.refuse"
               data-descript="撤回" data-functype="SUBMIT" data-size="mini"
               data-target="grid" :data-confirm="true" data-url="/wf/processInstance/revoke.json">
          <md-icon>undo</md-icon>
        </k-btn>

        <k-btn class="md-info md-just-icon md-simple" data-descript="一键审批"  data-functype="POPUP" data-size="mini"
               data-target="uploadFilePopup" :data-disabled="row.row.status == global.wf_process_status.finish ||
                                row.row.status == global.wf_process_status.refuse">
          <md-icon >cloud_upload</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="editPopup" class="popClass" data-title="流程审批详情" data-width="80%">
      <div style="overflow: auto">
      <FlowProcessInstanceListDialog :task-info="selectRowData" />
      </div>
    </k-popup>

    <k-popup ref="uploadFilePopup" data-title="上传">
      <FlowUploadFile :task-info="selectRowData"  @submitClose="popupClose"/>
    </k-popup>

  </div>

</template>

<script>

  import {assign} from "lodash";
  import Tools from "@/utils/tools.js";
  import FlowProcessInstanceListDialog from './FlowProcessInstanceListDialog'
  import FlowUploadFile from "./FlowUploadFile";

  export default {
    name: "FlowUserLaunch",
    components: {FlowProcessInstanceListDialog,FlowUploadFile},
    data() {
      return {
        formData: {},
        envItems: [],
        selectRowData: {},
        queryParamProcessName: '',
        queryParamStatus: '',
        queryParamCreateDateRange: [],
        queryParamFinishDateRange: []
      };
    },
    computed: {
      queryParam() {
        return {
          'processName': this.queryParamProcessName,
          'status': this.queryParamStatus,
          'createStartDate': this.queryParamCreateDateRange ? this.queryParamCreateDateRange[0] : null,
          'createEndDate': this.queryParamCreateDateRange ? this.queryParamCreateDateRange[1] : null,
          'finishStartDate': this.queryParamFinishDateRange ? this.queryParamFinishDateRange[0] : null,
          'finishEndDate': this.queryParamFinishDateRange ? this.queryParamFinishDateRange[1] : null
        }
      }
    },
    methods: {
      popupClose(){
        this.$refs.uploadFilePopup.close();
        this.$refs.grid.popup();
      },
      renderCreateDateTime(row) {
        return Tools.formatDateTime(row.createDate, row.createTime);
      },
      renderFinishDateTime(row) {
        let date = Tools.formatDateTime(row.finishDate, row.finishTime);
        if (date == "") {
          return '-'
        }
        return date;
      },
      renderApplyUser(row) {
       if (row.approvalUser) {
         return row.approvalUser
       } else {
         return row.applyUser
       }
      },
      renderResult(row) {
        if (!result) {
         return "提交流程申请"
        }
      },
      renderOpinion(row) {
        if (row.opinion) {
          return row.opinion
        } else {
          return "-"
        }
      },
      selectRow(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row)
        _this.formData = assign({}, row)
      }
    }
  };
</script>

<style scoped>
  .popClass /deep/ .el-dialog {
    padding-top: 35px;
    margin-right: 24px;
  }
</style>
