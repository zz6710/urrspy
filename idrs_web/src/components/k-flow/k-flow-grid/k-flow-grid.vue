<template>
	<div>
		<h4>操作历史</h4>
		<k-grid
			ref="dialogGrid"
			data-url="/wf/task/hisTaskDetail.json"
			:data-display="false"
			:data-params='{"processInstanceId": taskInfo.processInstanceId}'
			:data-operate-column="false"
		>
			<k-grid-column data-header="操作人" data-name="applyUser" data-render="renderApplyUser"></k-grid-column>
			<k-grid-column data-header="操作结果" data-name="result" data-render="renderResult"></k-grid-column>
			<k-grid-column data-header="操作任务" data-name="taskDisplayName"></k-grid-column>
			<k-grid-column data-header="处理意见" data-name="opinion" data-render="renderOpinion"></k-grid-column>
			<k-grid-column data-header="处理时长" data-name="diffDateTime" data-render="renderDiffDateTime"></k-grid-column>
			<k-grid-column data-header="操作时间" data-name="finishDate" data-render="renderFinishDateTime"></k-grid-column>
		</k-grid>
	</div>
</template>

<script>
  import Tools from "@/utils/tools.js";

export default {
	name: "KFlowGrid",
    props: {
      taskInfo: {},
    },
    data() {
      return {
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
          url: "/wf/task/getApprovalUserNameByTaskId.json",
          params: {"taskId": this.taskInfo.taskId}
        })
        .then(data => {
          if (data && data.data) {
            this.$refs.dialogGrid.list.push({'applyUser': data.data, 'result': '-', 'taskDisplayName': this.taskInfo.taskDisplayName})
          }
        });
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
