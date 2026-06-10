<template>
  <div>
    <k-form-search-customize data-target="taskGrid" v-model="queryParam">
      <k-form-item label="发起人">
        <k-field-select v-model="queryParamApplyUser" ref="surrogateSelect" data-action="User.findUsersWithQY"
                        data-display-field="username"  data-value-field="userid"/>
      </k-form-item>
      <k-form-item label="流程名">
        <k-field-select v-model="queryParamProcessName" data-action="SelectEntity.listProcess"
                        data-value-field="id" data-display-field="text" />
      </k-form-item>
      <k-form-item label="发起时间">
        <k-field-date v-model="queryParamDateRange" data-type="daterange" />
      </k-form-item>
    </k-form-search-customize>

    <k-grid ref="taskGrid" @data-row-select="selectRow" data-action="Task.listActiveTasks" :data-params="{'userid':userid,'taskType':'0'}">
      <k-grid-column data-align="center" data-header="流程名" data-name="processDisplayName" />
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode" />
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName" />
      <k-grid-column data-align="center" data-header="任务名" data-name="taskDisplayName" />
      <k-grid-column data-align="center" data-header="发起人" data-name="applyUser" />
      <k-grid-column data-align="center" data-header="任务创建时间" data-name="taskCreateDate" data-render="renderTaskCreateDateTime" />
      <k-grid-column data-align="center" data-header="发起时间" data-name="processInstanceCreateDate" data-render="renderProcessInstanceDateTime" />
      <template slot="operate" slot-scope="scope">
        <k-btn v-if="scope.row.row.taskType=='0'" class="btn-custom-plain" data-descript="详情" data-functype="PAGE" data-size="mini" data-target="/main/operation/flow/flow_detail">
          详情
        </k-btn>
        <span v-else-if="scope.row.row.taskType=='1'" class="md-info">
          待审批
        </span>
      </template>
    </k-grid>
  </div>

</template>

<script>
  import {assign} from "lodash";
  import Tools from "@/utils/tools.js";
  import moment from 'moment';

  export default {
    name: "flow_todo",
    data() {
      return {
        userid: localStorage.getItem("userid"),
        formData: {},
        envItems: [],
        selectRowData: {},
        queryParamProcessName: '',
        appDisplay: '',
        queryParamApplyUser: '',
        queryParamDateRange: [],
      };
    },
    computed: {
      queryParam() {
        return {
          processName: this.queryParamProcessName,
          appDisplay: this.appDisplay,
          applyUser: this.queryParamApplyUser,
          createStartDate: this.queryParamDateRange ? this.queryParamDateRange[0] : null,
          createEndDate: this.queryParamDateRange ? this.queryParamDateRange[1] : null,
          userid: localStorage.getItem("userid"),
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
  .editPopupClass /deep/ .el-dialog {
    padding-top: 35px;
    margin-right: 24px;
  }
</style>
