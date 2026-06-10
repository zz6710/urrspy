<!--待审核任务-->
<template>
  <div class="py-page">
    <k-form-search-customize data-target="taskGrid" v-model="queryParam" data-label-width="80px">
      <k-form-item label="抄送人">
        <k-field-select data-placeholder="请搜索" v-model="queryParamLaunchCopyUserId" :data-auto-load="false" :data-remote="true" dataContentType="json" data-url="wf/system/user/list.json" data-display-field="username" data-value-field="userid" />
      </k-form-item>
      <k-form-item label="流程名">
        <k-field-select v-model="queryParamProcessKey" dataContentType="json" data-url="wf/deploy/list.json" data-params="{start:0,limit: 10000000}" data-value-field="processKey" data-display-field="processName" />
      </k-form-item>
      <k-form-item label="抄送时间">
        <k-field-date v-model="queryParamDateRange" data-type="daterange" />
      </k-form-item>
      <k-form-item label="是否已阅">
        <k-field-select v-model="queryParamRead" :data-data="readSelect" data-value-field="value" data-display-field="label" />
      </k-form-item>
    </k-form-search-customize>
    <div class="py-page-container">
      <k-grid ref="taskGrid" dataContentType="json" data-url='wf/copy/list.json' :data-display="false">
        <k-grid-column data-align="center" data-header="流程名" data-name="procDefName"></k-grid-column>
        <k-grid-column data-align="center" data-header="流程版本">
          <template slot-scope="scope">
            <el-tag size="medium">v{{ scope.row.row.procDefVersion }}</el-tag>
          </template>
        </k-grid-column>
        <!-- <k-grid-column data-align="center" data-header="任务名" data-name="taskName"></k-grid-column> -->
        <k-grid-column data-align="center" data-header="抄送人" data-name="launchCopyUserName"></k-grid-column>
        <k-grid-column data-align="center" data-header="抄送时间" data-name="createTime"></k-grid-column>
        <k-grid-column data-align="center" data-header="状态">
          <template slot-scope="scope">
            <el-tag type="success" v-if="scope.row.row.read=='1'">已确认</el-tag>
            <el-tag type="warning" v-if="scope.row.row.read=='0'">未确认</el-tag>
          </template>
        </k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="详情" @click.native.stop="approveBtn(scope.row.row)" data-size="mini">
            <md-icon>library_books</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

    <k-popup ref="detailPopup" :data-title="selectRowData.procDefName"  data-width="1200px">
      <el-collapse style="margin-left:20px;margin-right:20px" v-model="activeNames">
        <el-collapse-item title="操作历史" name="his">
          <FlowHisTaskDetail :taskInfo="selectRowData"></FlowHisTaskDetail>
        </el-collapse-item>
        <el-collapse-item title="抄送表单信息" name="form">
          <FlowCopyFormInfo :taskInfo="selectRowData"></FlowCopyFormInfo>
        </el-collapse-item>
        <el-collapse-item title="流程图" name="flow">
          <FlowViewer :taskInfo="selectRowData"></FlowViewer>
        </el-collapse-item>
      </el-collapse>
      <div class="k-form-footer-content k-form-footer-align-center">
        <k-btn v-if="selectRowData.read=='0'" class="btn-custom-primary" data-functype="SUBMIT" data-url="wf/copy/read.json" data-target="taskGrid" dataContentType='json' :data-params='{"copyId":selectRowData.copyId,"read":"1"}' data-size="small">
          <md-icon md-src="/static/svg/confirm.svg" />
          确认
        </k-btn>
        <k-btn v-else-if="selectRowData.read=='1'" class="btn-custom-primary" data-disabled="true" data-size="small">
          已确认
        </k-btn>
      </div>
    </k-popup>
  </div>
</template>

    <script>
import FlowHisTaskDetail from "../components/FlowHisTaskDetail.vue";
import FlowCopyFormInfo from "../components/FlowCopyFormInfo.vue";
import FlowViewer from "../components/FlowViewer.vue";

export default {
	name: "copy",
	components: {
		FlowHisTaskDetail,
		FlowCopyFormInfo,
		FlowViewer,
	},
	data() {
		return {
			selectRowData: {},
			queryParamProcessKey: "",
			queryParamLaunchCopyUserId: "",
			queryParamDateRange: [],
			queryParamRead: "",
			activeNames: ["his", "form"],
			readSelect: [
				{
					label: "已确认",
					value: "1",
				},
				{
					label: "未确认",
					value: "0",
				},
			],
		};
	},
	computed: {
		queryParam() {
			return {
				procKey: this.queryParamProcessKey,
				launchCopyUserId: this.queryParamLaunchCopyUserId,
				createStartDate: this.queryParamDateRange ? this.queryParamDateRange[0] : null,
				createEndDate: this.queryParamDateRange ? this.queryParamDateRange[1] : null,
				read: this.queryParamRead,
			};
		},
	},
	created() {},
	methods: {
		//审核按钮
		approveBtn(row) {
			this.selectRowData = row;
			this.$refs.detailPopup.popup();
		},
		readChange(value) {
			return this.httpUtil
				.ajaxJson({
					url: "wf/copy/read.json",
					params: { read: value, copyId: this.selectRowData.copyId },
				})
				.then((response) => {
					this.$refs.taskGrid.load();
				});
		},
	},
};
</script>
  <style lang="scss" scoped>
.k-form-footer-content {
	width: 100%;
	display: flex;
	margin: 20px 0;
	position: relative;
}
.k-form-footer-align-center {
	justify-content: center;
}
</style>
