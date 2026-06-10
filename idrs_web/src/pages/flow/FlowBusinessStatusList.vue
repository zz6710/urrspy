<template>
  <div>
    <k-form-search data-target="grid" data-model-name="WfBusinessExtendVO" data-label-width="80px">
    </k-form-search>
    <k-grid
      ref="grid"
      @data-row-select="selectRow"
      :data-display="false"
      data-action='WfBusinessExtendVO.find1'>
      <k-grid-column data-align="center"
                     data-header="业务"
                     data-name="serverName"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="流程"
                     data-name="processDisplayName"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="发起人"
                     data-name="username"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="流程状态"
                     data-name="processStatus"
                     data-dict="process_status"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="业务执行状态"
                     data-name="busStatus"
                     data-dict="wf_business_status"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="开始时间"
                     data-name="startDate"
                     data-render="renderCreateDateTime"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="最后修改时间"
                     data-name="updateDate"
                     data-render="renderUpdateDateTime"
      ></k-grid-column>
            <template slot="operate" slot-scope="{row}">
              <k-btn class="md-warning md-just-icon md-simple" data-descript="重新执行业务" data-functype="SUBMIT" data-size="mini"
                     :data-disabled="!processFinishButBusiError(row.row)"
                      :data-confirm="true" data-target="grid" data-action="WfBusinessExtendVO.reExecution"
                     v-if="global.isShowAuthorityButton('WfBusinessExtendVO.reExecution')">
                <md-icon>repeat</md-icon>
              </k-btn>
              <k-btn class="md-danger md-just-icon md-simple" data-descript="置为已处理" data-functype="SUBMIT" data-size="mini"
                     :data-disabled="!processFinishButBusiError(row.row)"
                     :data-confirm="true" data-target="grid" data-action="WfBusinessExtendVO.errorConfirm"
                     v-if="global.isShowAuthorityButton('WfBusinessExtendVO.errorConfirm')">
                <md-icon>check_box</md-icon>
              </k-btn>
            </template>
    </k-grid>

  </div>

</template>

<script>

  import {assign} from "lodash";
  import Tools from "@/utils/tools.js";
  import FlowProcessInstanceListDialog from './FlowProcessInstanceListDialog'

  export default {
    name: "FlowUserLaunch",
    components: {FlowProcessInstanceListDialog},
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
    methods: {
      processFinishButBusiError(row) {
        return (row.processStatus === this.global.wf_process_status.finish &&
          row.busStatus === this.global.wf_business_status.error) || (row.processStatus === this.global.wf_process_status.finish &&
          row.busStatus === this.global.wf_business_status.processing);
      },
      renderCreateDateTime(row) {
        return Tools.formatDateTime(row.startDate, row.startTime);
      },
      renderUpdateDateTime(row) {
        let date = Tools.formatDateTime(row.updateDate, row.updateTime);
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
  /deep/ .el-dialog {
    padding-top: 35px;
  }
</style>
