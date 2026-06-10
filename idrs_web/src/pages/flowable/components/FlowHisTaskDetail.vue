<template>
  <div>
    <k-grid ref="dialogGrid" :dataPageSize="-1" :data-url="'wf/process/hisTaskDetail/'+taskInfo.procInsId +'.json'" :data-display="false" :data-operate-column="false" data-height="200">
      <k-grid-column data-header="操作人" dataWidth="120px" dataOverflow="true" data-render="renderUser"></k-grid-column>
      <k-grid-column data-header="操作任务" data-name="taskName" dataWidth="120px" dataOverflow="true"></k-grid-column>
      <k-grid-column data-header="任务状态" data-align="center" dataWidth="80px">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.row.taskStatus==='1'">已审批</el-tag>
          <el-tag v-if="scope.row.row.taskStatus==='2'">审批中</el-tag>
          <el-tag type="info" v-if="scope.row.row.taskStatus==='3'">待审批</el-tag>
        </template>
      </k-grid-column>
      <k-grid-column data-header="操作结果" data-render="renderCommentType" dataWidth="80px"></k-grid-column>
      <k-grid-column data-header="处理意见" data-name="message" dataOverflow="true"></k-grid-column>
      <k-grid-column data-header="接收时间" data-name="createTime" dataWidth="135px"></k-grid-column>
      <k-grid-column data-header="办结时间" data-name="finishTime" dataWidth="135px"></k-grid-column>
      <k-grid-column data-header="处理时长" data-name="duration" dataWidth="135px"></k-grid-column>
    </k-grid>
  </div>
</template>
  
  <script>
import wfStatus from "../enum/enum.js";
export default {
	name: "FlowHisTaskDetail",
	props: {
		taskInfo: {},
	},
	data() {
		return {
			wfTaskStatus: Object.values(wfStatus.task),
		};
	},
	created() {},
	methods: {
		renderCommentType(row) {
			let arr = this.wfTaskStatus.filter((t) => t.value == row.messageType);
			if (arr && arr.length > 0) {
				return arr[0].label;
			}
		},
		renderUser(row) {
			return row.assigneeName ?? row.candidate;
		},
	},
};
</script>
  