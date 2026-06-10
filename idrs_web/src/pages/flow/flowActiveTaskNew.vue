<template>
  <div>
    <k-form-search-customize data-target="taskGrid" v-model="queryParam">
      <k-form-item label="发起人">
        <k-field-select v-model="queryParamApplyUser" ref="surrogateSelect" data-action="User.findUsersWithQY"
                        data-display-field="username"  data-value-field="userid"/>
      </k-form-item>
      <k-form-item label="审批任务名称">
        <k-field-text v-model="queryParamProcessName"
                        data-value-field="id" data-display-field="text" />
      </k-form-item>
      <k-form-item label="目标处理日期">
        <k-field-date v-model="queryParamProcessDeadline" data-type="daterange" />
      </k-form-item>
<!--      <k-form-item label="是否在移动端展示" data-label-width="180px">-->
<!--        <k-field-select v-model="appDisplay" data-dict="1yes0no"/>-->
<!--      </k-form-item>-->
      <k-form-item label="发起时间">
        <k-field-date v-model="queryParamDateRange" data-type="daterange"
                      data-value-format="yyyy-MM-dd HH:mm:ss"/>
      </k-form-item>
    </k-form-search-customize>

    <k-grid
      ref="taskGrid"
      @data-row-select="selectRow"
      data-url='/wf/approvalTask/listPendingApprovalTasks.json' :data-params="{'userid':userid,'processId':processId}">
      <k-grid-column data-align="center"
                     data-header="审批任务名称"
                     data-name="processDisplayName"
      ></k-grid-column>
<!--      <k-grid-column data-align="center"-->
<!--                     data-header="流程名"-->
<!--                     data-name="serverName"-->
<!--      ></k-grid-column>-->
<!--      <k-grid-column data-align="center"-->
<!--                     data-header="业务操作"-->
<!--                     data-name="serverName"-->
<!--      ></k-grid-column>-->
<!--      -->
<!--      <k-grid-column data-align="center"-->
<!--                     data-header="任务名"-->
<!--                     data-name="taskDisplayName"-->
<!--      ></k-grid-column>-->
      <k-grid-column data-align="center"
                     data-header="发起人"
                     data-name="applyUser"
      ></k-grid-column>
      <k-grid-column data-align="center"
                     data-header="目标处理日期"
                     data-name="processDeadline"
      ></k-grid-column>

<!--      <k-grid-column data-align="center"-->
<!--                     data-header="任务创建时间"-->
<!--                     data-name="taskCreateDate"-->
<!--                     data-render="renderTaskCreateDateTime"-->
<!--      ></k-grid-column>-->
      <k-grid-column data-align="center"
                     data-header="发起时间"
                     data-name="processCreateTime"
      ></k-grid-column>
<!--      <k-grid-column data-align="center"-->
<!--                     data-header="是否在移动端展示"-->
<!--                     data-name="appDisplay"-->
<!--                     data-dict="1yes0no"-->
<!--      ></k-grid-column>-->
      <template slot="operate" slot-scope="scope">
        <k-btn class="btn-custom-text" data-descript="审批" data-functype="POPUP" data-size="mini"
               data-target="editPopup">
          审批
<!--          <md-icon>library_add_check</md-icon>-->
        </k-btn>
        <k-btn class="btn-custom-text" data-descript="自定义处理日期" data-functype="POPUP" data-size="mini"
               data-target="taskGrid" :data-handler="returnParams" :data-model="taskDataDeadline" >
          自定义处理日期
          <!--          <md-icon>library_add_check</md-icon>-->
        </k-btn>
      </template>
    </k-grid>

<!--   <k-popup ref="editPopup" class="editPopupClass"  data-width="80%" :data-title="selectRowData.processDisplayName" >-->
<!--     <div style="overflow: auto">-->
<!--      <FlowActiveTaskDialog :task-info="selectRowData" @submitClose="popupClose"/>-->
<!--     </div>-->
<!--    </k-popup>-->

    <k-popup ref="editPopup" class="editPopupClass"  data-width="60%"  >
      <div>
        <FlowActiveTaskDia ref="FlowActiveTaskDia" :fromData="selectRowData" :updSuccess="()=> {this.$refs.editPopup.close();this.$refs.taskGrid.load()}" @submitClose="popupClose"/>
      </div>
    </k-popup>

    <!--  自定义目标处理日期  -->
    <k-popup ref="customizeProcessDeadline" data-title="自定义目标处理日期" data-width="50%">
      <div style="width:100%;"><span style="color: #ed3333;margin-left: 80px;font-size: 13px;font-weight: bold">注释：如果审批流程不需要当前实时处理,可自定义一个目标处理日期,以方便后续处理</span></div>
      <k-form ref="customizeProcessDeadlineForm" data-ui="element" style="margin-top: 50px;">
        <k-form-item label="审批ID" v-show="false">
          <k-field-text v-model="taskDataDeadline.taskId"></k-field-text>
        </k-form-item>
        <k-form-item label="审批名称" data-width="400px">
          <k-field-text v-model="taskDataDeadline.displayName" :data-disabled="true"></k-field-text>
        </k-form-item>
        <k-form-item label="目标处理日期">
          <k-field-date v-model="taskDataDeadline.processDeadline" :dataAllowblank="false"></k-field-date>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-model="taskDataDeadline" data-target="taskGrid" @submitClose="popupClose2"
                 data-from="customizeProcessDeadlineForm" :data-handler="updateDeadlineForm">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>



  </div>

</template>

<script>

  import {assign} from "lodash";
  import Tools from "@/utils/tools.js";
  import FlowActiveTaskDialog from './flowActiveTaskDialog'
  import moment from 'moment';
  import FlowActiveTaskDia from "@/pages/flow/FlowActiveTaskDia";

  export default {
    name: "flowActiveTaskNew",
    components: {FlowActiveTaskDialog,FlowActiveTaskDia},
    data() {
      return {
        userid:localStorage.getItem("userid"),
        formData: {},
        taskDataDeadline:{},
        envItems: [],
        selectRowData: {},
        queryParamProcessName: '',
        appDisplay:'',
        queryParamApplyUser: '',
        queryParamProcessDeadline: [],
        queryParamDateRange: [],
        processId:'',
      };
    },
    computed: {
      queryParam() {
        return {
          'processNameLike': this.queryParamProcessName,
          'appDisplay': this.appDisplay,
          'applyUser': this.queryParamApplyUser,
          'processDeadlineStartDate': this.queryParamProcessDeadline ? this.queryParamProcessDeadline[0] : null,
          'processDeadlineEndDate': this.queryParamProcessDeadline ? this.queryParamProcessDeadline[1] : null,
          'createStartDate': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
          'createEndDate': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
          'userid':localStorage.getItem("userid"),
        }
      }
    },
    methods: {
      popupClose(value){
        this.$refs.editPopup.close();
        this.$refs.taskGrid.load();
      },

      renderProcessInstanceDateTime(row) {
        return Tools.formatDateTime(row.processInstanceCreateDate, row.processInstanceCreateTime);
      },
      renderTaskCreateDateTime(row) {
        return Tools.formatDateTime(row.taskCreateDate, row.taskCreateTime);
      },

      updateDeadlineForm(val){
        if(val.processDeadline == '' || val.processDeadline == null){
          Tools.alert("目标处理日期未填写!", "danger");
          return false;
        }else{
          this.httpUtil.ajax({
            url: "/wf/approvalTask/updateApprovalTaskProcessDeadline.json",
            params: val
          }).then(res => {
            if (res.status == '200') {
              Tools.alert(res.data);
              this.$refs.customizeProcessDeadline.close();
              this.$refs.taskGrid.load();
            }
          });
        }
      },

      popupClose2() {
        this.$refs.customizeProcessDeadline.close();
      },

      returnParams(val){
        this.taskDataDeadline={};
        this.$set(this.taskDataDeadline,'taskId',val.approvalTaskId);
        this.$set(this.taskDataDeadline,'displayName',val.processDisplayName);
        let processDeadline="";
        if(val.processDeadline != null && val.processDeadline != ''){
          let processDeadlineArr;
          processDeadlineArr=val.processDeadline.split("-");
          if(processDeadlineArr.length == 3){
            processDeadline=processDeadlineArr[0]+processDeadlineArr[1]+processDeadlineArr[2];
          }
        }
        this.$set(this.taskDataDeadline,'processDeadline',processDeadline);
        this.$refs.customizeProcessDeadline.popup();
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
      },
      findApproval(){
        this.httpUtil.ajax({
          url: "/wf/approvalTask/listPendingApprovalTasks.json",
          params: {userid:'EW0172'},
        }).then(data => {
          console.log('data',data)
        })
      }
    },
    created() {
      this.processId = this.$route.query.processId;
      // this.findApproval();
    },
    mounted() {
      this.processId='';
    }
  };
</script>

<style scoped>
  .editPopupClass /deep/ .el-dialog {
    padding-top: 35px;
    margin-left: 300px;
  }
</style>
