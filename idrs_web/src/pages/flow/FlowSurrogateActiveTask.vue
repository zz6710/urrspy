<template>
  <div>
    <k-form-search-customize data-target="taskGrid" v-model="queryParam">
      <k-form-item label="发起人">
        <k-field-select v-model="queryParamApplyUser" ref="surrogateSelect" data-action="User.findUsersWithQY"
                        data-display-field="username"  data-value-field="userid"/>
      </k-form-item>
      <k-form-item label="流程名">
        <k-field-select v-model="queryParamProcessName" data-url="/wf/process/select2/allProcessNames.json"
                        data-value-field="id" data-display-field="text" />
      </k-form-item>
      <k-form-item label="发起时间">
        <k-field-date v-model="queryParamDateRange" data-type="daterange" />
      </k-form-item>
    </k-form-search-customize>
    <k-grid
      ref="taskGrid"
      @data-row-select="selectRow"
      data-url='/wf/task/listSurrogateTasks.json'>
      <k-grid-column data-align="center"
                     data-header="流程名"
                     data-name="processDisplayName"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="任务名"
                     data-name="taskDisplayName"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="发起人"
                     data-name="applyUser"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="任务创建时间"
                     data-name="taskCreateDate"
                     data-render="renderTaskCreateDateTime"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="发起时间"
                     data-name="processInstanceCreateDate"
                     data-render="renderProcessInstanceDateTime"
      ></k-grid-column>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="审核" data-functype="POPUP" data-size="mini"
               data-target="editPopup">
          <md-icon>library_add_check</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="editPopup" :data-title="selectRowData.processDisplayName" :data-fullscreen="true">
      <FlowSurrogateActiveTaskDialog :task-info="selectRowData" />
    </k-popup>

  </div>

</template>

<script>

  import {assign} from "lodash";
  import Tools from "@/utils/tools.js";
  import FlowSurrogateActiveTaskDialog from './FlowSurrogateActiveTaskDialog'
  import moment from 'moment';

  export default {
    name: "FlowSurrogateActiveTask",
    components: {FlowSurrogateActiveTaskDialog},
    data() {
      return {
        formData: {},
        envItems: [],
        selectRowData: {},
        queryParamProcessName: '',
        queryParamApplyUser: '',
        queryParamDateRange: [],
      };
    },
    computed: {
      queryParam() {
        return {
          'processName': this.queryParamProcessName,
          'applyUser': this.queryParamApplyUser,
          'createStartDate': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
          'createEndDate': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
        }
      }
    },
    methods: {
      renderProcessInstanceDateTime(row) {
       return Tools.formatDateTime(row.processInstanceCreateDate, row.processInstanceCreateTime);
      },
      renderTaskCreateDateTime(row) {
        return Tools.formatDateTime(row.taskCreateDate, row.taskCreateTime);
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
      renderFinishDateTime(row) {
        if (row.finishDate && row.finishTime) {
         return Tools.formatDateTime(row.finishDate, row.finishTime);
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
