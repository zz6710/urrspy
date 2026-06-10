<!--流程追踪 -->
<template>
  <div class="tab-page">
    <k-form-search-customize data-target="grid" v-model="queryParam" data-label-width="70px">
      <k-form-item label="发起人">
        <k-field-select data-placeholder="请搜索" v-model="queryParamApplyUser" :data-auto-load="false" :data-remote="true" dataContentType="json" data-url="wf/system/user/list.json" data-display-field="username" data-value-field="userid" />
      </k-form-item>
      <k-form-item label="流程名" data-label-width="56px">
        <k-field-select dataContentType="json" data-params="{start:0,limit: 100}" v-model="queryParamProcessKey" data-url="wf/deploy/list.json" data-display-field="processName" data-value-field="processKey" />
      </k-form-item>
      <k-form-item label="状态" data-label-width="42px">
        <k-field-select v-model="queryParamStatus" :data-data="wfProcessStatus" data-display-field="label" data-value-field="value" />
      </k-form-item>
      <k-form-item label="发起日期">
        <k-field-date v-model="queryParamCreateDateRange" data-type="daterange" />
      </k-form-item>
      <k-form-item label="结束日期">
        <k-field-date v-model="queryParamFinishDateRange" data-type="daterange" />
      </k-form-item>
      <k-form-item label="关键词" data-label-width="50px">
        <k-field-text
          v-model="queryParamValues"
        />
      </k-form-item>
    </k-form-search-customize>

    <k-grid ref="grid" dataContentType="json" @data-row-select="selectRow" :data-display="false" data-url='wf/process/finishedList.json'>
      <k-grid-column data-align="center" data-header="流程名" data-name="procDefName"></k-grid-column>
      <k-grid-column data-align="center" data-header="关键词" data-name="values"></k-grid-column>
      <k-grid-column data-align="center" data-header="关键词名称" data-name="valuesName"></k-grid-column>
      <k-grid-column data-align="center" data-header="流程状态" data-render="renderProcStatus"></k-grid-column>
      <k-grid-column data-align="center" data-header="任务名" data-name="taskName"></k-grid-column>
      <k-grid-column data-align="center" data-header="发起人" data-name="startUserName"></k-grid-column>
      <k-grid-column data-align="center" data-header="发起时间" data-name="procStartTime" dataWidth="135px"></k-grid-column>
      <k-grid-column data-align="center" data-header="结束时间" data-name="procEndTime" dataWidth="135px"></k-grid-column>
      <k-grid-column data-align="center" data-header="耗时" data-name="duration" dataWidth="135px"></k-grid-column>
      <template slot="operate">
        <k-btn class="md-info md-just-icon md-simple" data-descript="详情" data-functype="POPUP" data-size="mini" data-target="editPopup">
          <md-icon>library_books</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="editPopup"  data-title="流程审批详情" data-width="1200px">
      <el-collapse style="margin-left:20px;margin-right:20px" v-model="activeNames">
        <el-collapse-item title="操作历史" name="his">
          <FlowHisTaskDetail :taskInfo="selectRowData"></FlowHisTaskDetail>
        </el-collapse-item>
        <el-collapse-item title="审核表单信息" name="form">
          <FlowFormInfo :taskInfo="selectRowData"></FlowFormInfo>
        </el-collapse-item>
        <el-collapse-item title="流程图" name="flow">
          <FlowViewer :taskInfo="selectRowData"></FlowViewer>
        </el-collapse-item>
      </el-collapse>
    </k-popup>
  </div>

</template>

  <script>
import { assign } from "lodash";
import FlowHisTaskDetail from "../components/FlowHisTaskDetail.vue";
import FlowFormInfo from "../components/FlowFormInfo.vue";
import FlowViewer from "../components/FlowViewer.vue";
import wfStatus from "../enum/enum.js";

export default {
	name: "myLaunch",
	components: { FlowHisTaskDetail, FlowFormInfo, FlowViewer },
	data() {
		return {
			activeNames: ["his", "form"],
			selectRowData: {},
			queryParamProcessKey: "",
			queryParamStatus: "",
      queryParamValues:"",
			queryParamApplyUser: "",
			queryParamCreateDateRange: [],
			queryParamFinishDateRange: [],
			wfProcessStatus: Object.values(wfStatus.process),
		};
	},
	mounted() {},
	computed: {
		queryParam() {
			return {
				applyUser: this.queryParamApplyUser,
				processKey: this.queryParamProcessKey,
				status: this.queryParamStatus,
        values:this.queryParamValues,
				createStartDate: this.queryParamCreateDateRange ? this.queryParamCreateDateRange[0] : null,
				createEndDate: this.queryParamCreateDateRange ? this.queryParamCreateDateRange[1] : null,
				finishStartDate: this.queryParamFinishDateRange ? this.queryParamFinishDateRange[0] : null,
				finishEndDate: this.queryParamFinishDateRange ? this.queryParamFinishDateRange[1] : null,
			};
		},
	},
	methods: {
		selectRow(row, column, event) {
			const _this = this;
			_this.selectRowData = assign({}, row);
		},
		renderProcStatus(row) {
			if (row.procStatus) {
				let arr = this.wfProcessStatus.filter((t) => t.value == row.procStatus);
				if (arr && arr.length > 0) {
					return arr[0].label;
				}
			} else {
				return "-";
			}
		},
	},
};
</script>

  <style scoped>
/deep/ .el-dialog {
	padding-top: 35px;
}
</style>
