<template>
  <div>
    <k-flow-grid :task-info="taskInfo"></k-flow-grid>
    <k-flow-formInfo :task-info="taskInfo"></k-flow-formInfo>
    <k-flow-modify-information :task-info="taskInfo"></k-flow-modify-information>
  </div>

</template>

<script>

  import KBtn from "../../components/k-element/k-btn/k-btn";
  import Tools from "@/utils/tools.js";

  export default {
    name: "FlowProcessInstanceListDialog",
    props: {
      taskInfo: {}
    },
    data() {
      return {
        formData: {},
        taskFormComponentName: null,
        resultDict: {
          "1": "开始",
          "2": "驳回",
          "3": "拒绝",
          "4": "驳回到开始申请节点",
          "5": "通过",
          "6": "重新提交申请",
          "7": "审批中",
          "9": "完成"
        }
      };
    },
    created() {
      this.httpUtil
        .ajax({
          url: "/wf/conf/getTaskBtns.json",
          params: {"processId": this.taskInfo.processId}
        })
        .then(res => {
          if (res && res.data && res.data.length > 0) {
            let data = res.data;
            for (let i = 0; i < data.length; i++) {
              if (this.taskInfo.taskName == data[i].taskName) {
                let btns = data[i].btns;
                if (btns) {
                  this.btns = btns;
                }
                break;
              }
            }
          }
        });

      this.httpUtil
        .ajax({
          url: "/wf/wf/attachment/enableAttachment.json",
          params: {"processId": this.taskInfo.processId}
        })
        .then(res => {
          this.enableAttachment = res.data == 1 ? true : false;
        });

      this.httpUtil
        .ajax({
          url: "/wf/conf/getFormConf.json",
          params: {"processId": this.taskInfo.processId}
        })
        .then(res => {
          let formConfData = res.data;
          if (formConfData && formConfData.length > 0) {
            // 默认取第一条数据
            this.taskFormComponentName = formConfData[0].formUrl;
            for (let i = 1; i < formConfData.length; i++) {
              if (formConfData[i].taskName == this.taskInfo.taskName && formConfData[i].formUrl) {
                this.taskFormComponentName = formConfData[i].formUrl;
                break;
              }
            }
          }
        });

      if (this.taskInfo) {
        this.formData.taskId = this.taskInfo.taskId
      }
    },
    methods: {
      renderDiffDateTime(row) {
        if (row.createDate && row.createTime && row.finishDate && row.finishTime) {
          let diffTime = Tools.diffDateTime(row.createDate, row.createTime, row.finishDate, row.finishTime)
          if (diffTime == '0天0时0分0秒') {
            return "-";
          } else {
            return diffTime;
          }
        } else {
          return "-";
        }
      },
      renderApplyUser(row) {
        if (row.approvalUser) {
          return row.approvalUser
        } else {
          return row.applyUser
        }
      },
      renderResult(row) {
        if (!row.result) {
          return "提交流程申请"
        } else {
          return this.resultDict[row.result];
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
      }
    }
  };
</script>

<style>
</style>
