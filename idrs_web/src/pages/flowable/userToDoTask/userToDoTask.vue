<!--待审核任务-->
<template>
  <div class="py-page">
    <div class="py-page-container">
    <el-tabs v-model="activeName">
      <el-tab-pane name="myTodoTask">
        <span slot="label">审批任务
          <el-badge class="mark" :value="myTodoTaskNum" />
        </span>
        <div class="tab-page">
          <k-form-search-customize data-target="taskGrid" v-model="queryParam" data-label-width="60px">
            <k-form-item label="发起人">
              <k-field-select data-placeholder="请搜索" v-model="queryParamApplyUser" :data-auto-load="false" :data-remote="true" dataContentType="json" data-url="wf/system/user/list.json" data-display-field="username" data-value-field="userid" />
            </k-form-item>
            <k-form-item label="流程名">
              <k-field-select v-model="queryParamProcessKey" dataContentType="json" data-url="wf/deploy/list.json" data-params="{start:0,limit: 10000000}" data-value-field="processKey" data-display-field="processName" />
            </k-form-item>
            <k-form-item label="发起时间" data-label-width="80px">
              <k-field-date v-model="queryParamDateRange" data-type="daterange" />
            </k-form-item>
            <k-form-item label="关键词" data-label-width="80px">
              <k-field-text
                v-model="queryParamValues"
              />
            </k-form-item>
          </k-form-search-customize>
          <k-grid ref="taskGrid" :data-after-load="taskGridAfterLoad" :data-params='{"surrogateFlag":"0"}' dataContentType="json" data-url='wf/process/todoList.json'>
            <k-grid-column data-align="center" data-header="流程名" data-name="procDefName"></k-grid-column>
            <k-grid-column data-align="center" data-header="关键词" data-name="values"></k-grid-column>
            <k-grid-column data-align="center" data-header="关键词名称" data-name="valuesName"></k-grid-column>
            <k-grid-column data-align="center" data-header="任务名" data-name="taskName"></k-grid-column>
            <k-grid-column data-align="center" data-header="发起人" data-name="startUserName"></k-grid-column>
            <k-grid-column data-align="center" data-header="任务创建时间" data-name="createTime"></k-grid-column>
            <k-grid-column data-align="center" data-header="流程发起时间" data-name="procStartTime"></k-grid-column>
            <template slot="operate" slot-scope="scope">
              <k-btn class="md-info md-just-icon md-simple" data-descript="审核" @click.native.stop="approveBtn(scope.row.row)" data-size="mini">
                <md-icon>library_add_check</md-icon>
              </k-btn>
            </template>
          </k-grid>
        </div>
      </el-tab-pane>
     <!--  <el-tab-pane name="surrogateTask">
        <span slot="label">转审批任务
          <el-badge class="mark" :value="surrogateTaskNum" />
        </span>
        <div class="tab-page">
          <k-form-search-customize data-target="surrogateTaskGrid" v-model="queryParam" data-label-width="60px">
            <k-form-item label="发起人">
              <k-field-select data-placeholder="请搜索" v-model="queryParamApplyUser" :data-auto-load="false" :data-remote="true" dataContentType="json" data-url="wf/system/user/list.json" data-display-field="username" data-value-field="userid" />
            </k-form-item>
            <k-form-item label="流程名">
              <k-field-select v-model="queryParamProcessKey" dataContentType="json" data-url="wf/deploy/list.json" data-params="{start:0,limit: 10000000}" data-value-field="processKey" data-display-field="processName" />
            </k-form-item>
            <k-form-item label="发起时间" data-label-width="80px">
              <k-field-date v-model="queryParamDateRange" data-type="daterange" />
            </k-form-item>
          </k-form-search-customize>
          <k-grid ref="surrogateTaskGrid" :data-after-load="surrogateTaskGridAfterLoad" :data-params='{"surrogateFlag":"1"}' dataContentType="json" data-url='wf/process/todoList.json'>
            <k-grid-column data-align="center" data-header="流程名" data-name="procDefName"></k-grid-column>
            <k-grid-column data-align="center" data-header="流程版本">
              <template slot-scope="scope">
                <el-tag size="medium">v{{ scope.row.row.procDefVersion }}</el-tag>
              </template>
            </k-grid-column>
            <k-grid-column data-align="center" data-header="任务名" data-name="taskName"></k-grid-column>
            <k-grid-column data-align="center" data-header="发起人" data-name="startUserName"></k-grid-column>
            <k-grid-column data-align="center" data-header="授权人" data-name="authorizeName"></k-grid-column>
            <k-grid-column data-align="center" data-header="任务创建时间" data-name="createTime"></k-grid-column>
            <k-grid-column data-align="center" data-header="流程发起时间" data-name="procStartTime"></k-grid-column>
            <template slot="operate" slot-scope="scope">
              <k-btn class="md-info md-just-icon md-simple" data-descript="审核" @click.native.stop="approveBtn(scope.row.row)" data-size="mini">
                <md-icon>library_add_check</md-icon>
              </k-btn>
            </template>
          </k-grid>
        </div>
      </el-tab-pane> -->
    </el-tabs>
    </div>

    <k-popup ref="detailPopup" :data-open="getEnableFileUpload" :data-title="selectRowData.procDefName"  data-width="1200px" class="h-dialog">
      <el-collapse style="margin-left:20px;margin-right:20px" v-model="activeNames">
        <el-collapse-item title="操作历史" name="his">
          <FlowHisTaskDetail :taskInfo="selectRowData"></FlowHisTaskDetail>
        </el-collapse-item>
        <el-collapse-item title="审核表单信息" name="form">
          <FlowFormInfo :taskInfo="selectRowData"></FlowFormInfo>
        </el-collapse-item>
        <!-- <el-collapse-item v-if="busiFormInfo.length>0" title="业务表单信息" name="busiForm">
			<FlowBusiFormInfo :taskInfo="selectRowData" :busiFormInfo="busiFormInfo"></FlowBusiFormInfo>
		  </el-collapse-item> -->
        <el-collapse-item v-if="enableFileUpload=='1'" title="附件列表" name="file">
          <FlowFileUpload :taskInfo="selectRowData"></FlowFileUpload>
        </el-collapse-item>
        <el-collapse-item title="流程图" name="flow">
          <FlowViewer :taskInfo="selectRowData"></FlowViewer>
        </el-collapse-item>
        <!-- <el-collapse-item title="填写业务表单" name="fillBusiForm">
			<FlowFillBusiFormInfo :taskInfo="selectRowData"></FlowFillBusiFormInfo>
		  </el-collapse-item> -->
      </el-collapse>
      <p></p>
      <FlowApproval :taskInfo="selectRowData" @closeDetailPopup="closeDetailPopup" @loadTaskGrid="loadTaskGrid"></FlowApproval>
    </k-popup>
  </div>

</template>

  <script>
import FlowHisTaskDetail from "../components/FlowHisTaskDetail.vue";
import FlowFormInfo from "../components/FlowFormInfo.vue";
import FlowFileUpload from "../components/FlowFileUpload.vue";
import FlowViewer from "../components/FlowViewer.vue";
import FlowApproval from "../components/FlowApproval.vue";
import FlowFillBusiFormInfo from "../components/FlowFillBusiFormInfo.vue";
import FlowBusiFormInfo from "../components/FlowBusiFormInfo.vue";

export default {
	name: "userToDoTask",
	components: { FlowHisTaskDetail, FlowFormInfo, FlowViewer, FlowFileUpload, FlowApproval, FlowFillBusiFormInfo, FlowBusiFormInfo },
	data() {
		return {
			selectRowData: {},
			queryParamProcessKey: "",
			queryParamApplyUser: "",
			queryParamValues:"",
			queryParamDateRange: [],
			activeNames: ["his", "form", "file", "busiForm", "fillBusiForm"],
			enableFileUpload: "0",
			busiFormInfo: [],
			surrogateFlagOption: [
				{
					label: "是",
					value: "1",
				},
				{
					label: "否",
					value: "0",
				},
			],
			activeName: "myTodoTask",
			myTodoTaskNum: 0,
			surrogateTaskNum: 0,
		};
	},
	computed: {
		queryParam() {
			return {
				processKey: this.queryParamProcessKey,
				applyUser: this.queryParamApplyUser,
				values:this.queryParamValues,
				createStartDate: this.queryParamDateRange ? this.queryParamDateRange[0] : null,
				createEndDate: this.queryParamDateRange ? this.queryParamDateRange[1] : null,
				surrogateFlag: this.activeName == "myTodoTask" ? "0" : "1",
			};
		},
	},
	created() {},
	methods: {
		taskGridAfterLoad() {
			this.myTodoTaskNum = this.$refs.taskGrid.total;
		},
		surrogateTaskGridAfterLoad() {
			this.surrogateTaskNum = this.$refs.surrogateTaskGrid.total;
		},
		//审核按钮
		approveBtn(row) {
			this.selectRowData = row;
			this.getEnableFileUpload();
			//this.getBusiFormInfo();
			this.$refs.detailPopup.popup();
		},
		closeDetailPopup() {
			this.$refs.detailPopup.close();
		},
		loadTaskGrid() {
			this.$refs.taskGrid.load();
			this.$refs.surrogateTaskGrid.load();
		},
		//是否开启文件上传
		getEnableFileUpload() {
			this.httpUtil
				.ajaxJson({
					url: "wf/fileUpload/enableFileUpload/" + this.selectRowData.procDefId + ".json",
				})
				.then((res) => {
					this.enableFileUpload = res.data;
				});
		},

		// getBusiFormInfo() {
		// 	// 获取审批表单数据
		// 	return this.httpUtil
		// 		.ajaxJson({
		// 			url: "wf/process/busiFormInfo/" + this.selectRowData.procDefId + "/" + this.selectRowData.procInsId + ".json",
		// 		})
		// 		.then((res) => {
		// 			this.busiFormInfo = res.rows;
		// 		});
		// },
	},
};
</script>
<style scoped>

</style>
