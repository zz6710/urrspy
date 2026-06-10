<!--业务流程状态-->
<template>
  <div class="py-page">
    <k-form-search-customize data-target="grid" v-model="queryParams" data-label-width="80px">
      <k-form-item label="流程状态">
        <k-field-select v-model="queryParams.processStatus" :data-data="wfProcessStatus" data-display-field="label" data-value-field="value" />
      </k-form-item>
      <k-form-item label="业务状态">
        <k-field-select v-model="queryParams.busStatus" :data-data="wfBusinessStatus" data-display-field="label" data-value-field="value" />
      </k-form-item>
    </k-form-search-customize>
		<div class="py-page-container">
			<k-grid ref="grid" dataContentType="json" :data-display="false" data-url='wf/busiInfo/list.json'>
				<k-grid-column data-align="center" data-header="服务" data-name="server"></k-grid-column>
				<k-grid-column data-align="center" data-header="业务" data-name="url"></k-grid-column>
				<k-grid-column data-align="center" data-header="流程名称" data-render="renderProcessName"></k-grid-column>
				<k-grid-column data-align="center" data-header="发起人" data-name="creatorName"></k-grid-column>
				<k-grid-column data-align="center" data-header="流程状态" data-render="renderProcessStatus"></k-grid-column>
				<k-grid-column data-align="center" data-header="业务状态" data-render="renderBusinessStatus"></k-grid-column>
				<k-grid-column data-align="center" data-header="回调信息" data-name="busReturnMsg" dataOverflow="true"></k-grid-column>
				<k-grid-column data-align="center" data-header="回调次数" data-name="callbackNum" dataWidth="70px"></k-grid-column>
				<k-grid-column data-align="center" data-header="开始时间" data-name="createTime" dataWidth="135px"></k-grid-column>
				<k-grid-column data-align="center" data-header="最后修改时间" data-name="updateTime" dataWidth="135px"></k-grid-column>
				<template slot="operate" slot-scope="{row}">
					<k-btn class="md-warning md-just-icon md-simple" dataContentType="json" data-descript="重新执行业务" data-functype="SUBMIT" data-size="mini" :data-disabled="!processFinishButBusiError(row.row)" :data-confirm="true" data-target="grid" data-url="wf/busiInfo/execute.json">
						<md-icon>repeat</md-icon>
					</k-btn>
					<k-btn class="md-danger md-just-icon md-simple" dataContentType="json" data-descript="置为已处理" data-functype="SUBMIT" data-size="mini" :data-disabled="!processFinishButBusiError(row.row)" :data-confirm="true" data-target="grid" data-url="wf/busiInfo/errorConfirm.json">
						<md-icon>check_box</md-icon>
					</k-btn>
				</template>
			</k-grid>
		</div>
  </div>

</template>

<script>
import wfStatus from "../enum/enum.js";
export default {
	name: "businessStatus",
	data() {
		return {
			queryParams: {
				processStatus: "",
				busStatus: "",
			},
			wfProcessStatus: Object.values(wfStatus.process),
			wfBusinessStatus: Object.values(wfStatus.business),
			processList: [],
		};
	},
	created() {
		this.getProcessList();
	},
	methods: {
		getProcessList() {
			return this.httpUtil
				.ajaxJson({
					url: "wf/deploy/list.json",
					params: { start: 0, limit: 10000000 },
				})
				.then((response) => {
					this.processList = response.rows;
				});
		},
		processFinishButBusiError(row) {
			return row.processStatus == wfStatus.process.finish.value && row.busStatus == wfStatus.business.error.value;
		},
		renderProcessStatus(row) {
			if (row.processStatus) {
				let arr = this.wfProcessStatus.filter((t) => t.value == row.processStatus);
				if (arr && arr.length > 0) {
					return arr[0].label;
				}
			} else {
				return "-";
			}
		},
		renderBusinessStatus(row) {
			if (row.busStatus) {
				let arr = this.wfBusinessStatus.filter((t) => t.value == row.busStatus);
				if (arr && arr.length > 0) {
					return arr[0].label;
				}
			} else {
				return "-";
			}
		},
		renderProcessName(row) {
			let arr = this.processList.filter((t) => t.processKey == row.processKey);
			if (arr && arr.length > 0) {
				return arr[0].processName;
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
