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
      <k-form-item label="是否在移动端展示" data-label-width="180px">
        <k-field-select v-model="appDisplay" data-dict="1yes0no"/>
      </k-form-item>
      <k-form-item label="发起时间">
        <k-field-date v-model="queryParamDateRange" data-type="daterange" />
      </k-form-item>
    </k-form-search-customize>

    <k-grid
      ref="taskGrid"
      @data-row-select="selectRow"
      data-url='/wf/task/listActiveTasks.json' :data-params="{'userid':userid}">
      <k-grid-column data-align="center"
                     data-header="流程名"
                     data-name="processDisplayName"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="业务操作"
                     data-name="serverName"
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
      <k-grid-column data-align="center"
                     data-header="是否在移动端展示"
                     data-name="appDisplay"
                     data-dict="1yes0no"
      ></k-grid-column>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="审核" data-functype="POPUP" data-size="mini"
               data-target="editPopup">
          <md-icon>library_add_check</md-icon>
        </k-btn>
      </template>
    </k-grid>

   <k-popup ref="editPopup" class="editPopupClass"  data-width="80%" :data-title="selectRowData.processDisplayName" >
     <div style="overflow: auto">
      <FlowActiveTaskDialog :task-info="selectRowData" @submitClose="popupClose"/>
     </div>
    </k-popup>



  </div>

</template>

<script>

  import {assign} from "lodash";
  import Tools from "@/utils/tools.js";
  import FlowActiveTaskDialog from './flowActiveTaskDialog'
  import moment from 'moment';

  export default {
    name: "flowActiveTask",
    components: {FlowActiveTaskDialog},
    data() {
      return {
        userid:localStorage.getItem("userid"),
        formData: {},
        envItems: [],
        selectRowData: {},
        queryParamProcessName: '',
        appDisplay:'',
        queryParamApplyUser: '',
        queryParamDateRange: [],
      };
    },
    computed: {
      queryParam() {
        return {
          'processName': this.queryParamProcessName,
          'appDisplay': this.appDisplay,
          'applyUser': this.queryParamApplyUser,
          'createStartDate': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
          'createEndDate': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
          'userid':localStorage.getItem("userid"),
        }
      }
    },
    methods: {
      popupClose(){
        this.$refs.editPopup.close();
      },

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
