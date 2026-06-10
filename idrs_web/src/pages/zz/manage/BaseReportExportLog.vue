<template>
	<div class="py-page" v-loading="loading">
		<div>
			<k-form-search-customize
				data-model-name="BaseReportExportLog"
				v-model="queryParam"
				data-target="baseReportExportLogGrid"
				data-label-width="80px"
			>
				<k-form-item label="审批状态">
					<k-field-select v-model="queryParam.dataStatus" data-dict="reportExportStatus"></k-field-select>
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<k-grid
				ref="baseReportExportLogGrid"
				@data-row-select="selectRow"
				data-action="BaseReportExportLog.findBaseReportExportLogs"
			>
				<k-grid-column data-header="id" data-name="id" data-hidden="true"></k-grid-column>
				<k-grid-column data-header="用户" data-name="username"></k-grid-column>
				<k-grid-column data-header="报表名称" data-name="reportName"></k-grid-column>
				<k-grid-column data-header="申请时间" data-name="applyTime"></k-grid-column>
				<k-grid-column data-header="数据日期" data-name="dataTime" data-type="date"></k-grid-column>
				<k-grid-column
					data-header="审批状态"
					data-name="dataStatus"
					data-dict="reportExportStatus"
				></k-grid-column>
				<k-grid-column data-header="流程id" data-name="processInstanceId" data-hidden="true"></k-grid-column>
				<k-grid-column data-header="文件路径" data-name="filePath" data-hidden="true"></k-grid-column>
				<!--		<k-grid-column data-header="创建人" data-name="createBy"></k-grid-column>-->
				<!--		<k-grid-column data-header="创建时间" data-name="createTime"></k-grid-column>-->
				<k-grid-column
					data-header="文件状态"
					data-name="fileStatus"
					data-dict="exportFileStatus"
				></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-descript="下载报表"
						@click="download(scope.row.row)"
						data-size="mini"
						v-show="scope.row.row.dataStatus == '2'"
						data-target="editBaseReportExportLogPopup"
					>
						下载
					</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="BaseReportExportLog.deleteBaseReportExportLog"
						data-size="mini"
						v-show="scope.row.row.dataStatus == '2'"
						data-type="danger"
						data-target="baseReportExportLogGrid"
						:data-confirm="true"
						data-descript="删除报表"
					>
						删除
					</k-btn>
				</template>
			</k-grid>
		</div>

		<!--    添加报表导出审批历史表弹出框   -->
		<k-popup ref="addBaseReportExportLogPopup" data-title="新增">
			<k-form ref="addBaseReportExportLogForm" :data-col="2">
				<k-form-item label="id">
					<k-field-text v-model="formData.id" />
				</k-form-item>
				<k-form-item label="用户id">
					<k-field-text v-model="formData.userid" />
				</k-form-item>
				<k-form-item label="报表id">
					<k-field-text v-model="formData.reportId" />
				</k-form-item>
				<k-form-item label="申请时间">
					<k-field-text v-model="formData.applyTime" />
				</k-form-item>
				<k-form-item label="数据日期">
					<k-field-text v-model="formData.dataTime" />
				</k-form-item>
				<k-form-item label="状态">
					<k-field-text v-model="formData.dataStatus" />
				</k-form-item>
				<k-form-item label="流程id">
					<k-field-text v-model="formData.processInstanceId" />
				</k-form-item>
				<k-form-item label="创建人">
					<k-field-text v-model="formData.createBy" />
				</k-form-item>
				<k-form-item label="创建时间">
					<k-field-text v-model="formData.createTime" />
				</k-form-item>
				<k-form-item label="更新人">
					<k-field-text v-model="formData.updateBy" />
				</k-form-item>
				<k-form-item label="更新时间">
					<k-field-text v-model="formData.updateTime" />
				</k-form-item>
				<k-form-item label="报表名称">
					<k-field-text v-model="formData.reportName" />
				</k-form-item>
				<k-form-item label="文件路径">
					<k-field-text v-model="formData.filePath" />
				</k-form-item>
				<k-form-item label="文件状态">
					<k-field-text v-model="formData.fileStatus" />
				</k-form-item>

				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="BaseReportExportLog.addBaseReportExportLog"
						data-from="addBaseReportExportLogForm"
						:data-model="formData"
						data-target="baseReportExportLogGrid"
					>
						<i class="icon-confirm" />确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <i class="icon-cancel" />取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<!--    修改报表导出审批历史表弹出框   -->
		<k-popup ref="editBaseReportExportLogPopup" data-title="修改">
			<k-form ref="editBaseReportExportLogForm" :data-col="2">
				<k-form-item label="id">
					<k-field-text v-model="formData.id" />
				</k-form-item>
				<k-form-item label="用户id">
					<k-field-text v-model="formData.userid" />
				</k-form-item>
				<k-form-item label="报表id">
					<k-field-text v-model="formData.reportId" />
				</k-form-item>
				<k-form-item label="申请时间">
					<k-field-text v-model="formData.applyTime" />
				</k-form-item>
				<k-form-item label="数据日期">
					<k-field-text v-model="formData.dataTime" />
				</k-form-item>
				<k-form-item label="状态">
					<k-field-text v-model="formData.dataStatus" />
				</k-form-item>
				<k-form-item label="流程id">
					<k-field-text v-model="formData.processInstanceId" />
				</k-form-item>
				<k-form-item label="创建人">
					<k-field-text v-model="formData.createBy" />
				</k-form-item>
				<k-form-item label="创建时间">
					<k-field-text v-model="formData.createTime" />
				</k-form-item>
				<k-form-item label="更新人">
					<k-field-text v-model="formData.updateBy" />
				</k-form-item>
				<k-form-item label="更新时间">
					<k-field-text v-model="formData.updateTime" />
				</k-form-item>
				<k-form-item label="报表名称">
					<k-field-text v-model="formData.reportName" />
				</k-form-item>
				<k-form-item label="文件路径">
					<k-field-text v-model="formData.filePath" />
				</k-form-item>
				<k-form-item label="文件状态">
					<k-field-text v-model="formData.fileStatus" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="BaseReportExportLog.updateBaseReportExportLog"
						data-from="editBaseReportExportLogForm"
						:data-model="formData"
						data-target="baseReportExportLogGrid"
					>
						<i class="icon-confirm" />确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <i class="icon-cancel" />取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
	</div>
</template>

<script>
import Tools from "@/utils/tools";
import moment from "moment";
export default {
	name: "BaseReportExportLog",
	data() {
		return {
			formData: {},
			selectRowData: {},
			queryParam: {},
			loading: false
		};
	},
	methods: {
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},

		download(row) {
			let id = row.id;
			let fileStatus = row.fileStatus;
			if (id === null || id === "") {
				Tools.alert("请选择一条记录！", "danger");
				return;
			}
			if (fileStatus !== "2") {
				Tools.alert("当前文件状态为未生成，请稍后下载！", "danger");
				return;
			}
			this.loading = true;
			this.httpUtil.download({
				url: "download/server/RptApp/baseReportExportLog/download.json",
				params: {
					id: id,
				},
				callback: () => {
					this.loading = false;
				}
			}, row.reportName + "_" + row.dataTime + ".zip");
		},
	},
};
</script>
