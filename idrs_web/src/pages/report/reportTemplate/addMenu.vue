<template>
	<div class="py-page">
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn class="btn-custom-primary" data-functype="POPUP" data-target="addDialog"
						>新增菜单</k-btn
					>
					<k-btn class="btn-custom-plain" data-functype="POPUP" :data-handler="reportCheck"
						>报表查询</k-btn
					>
					<k-btn
						class="btn-custom-plain"
						data-functype="POPUP"
						data-target="M8430P004"
						data-param-handler="singleUploadHandler"
					>
						导入单个报表
					</k-btn>
				</div>
			</div>
			<k-grid
				ref="tableGrid"
				:data-operate-column="true"
				data-action="JmReportMenuInfo.findReportMenuInfo"
				data-operate-width="320px"
				@data-row-select="selectRow"
			>
				<k-grid-column data-header="报表菜单ID" data-name="menuid"></k-grid-column>
				<k-grid-column data-header="报表菜单名" data-name="menuname"></k-grid-column>
				<k-grid-column data-header="上级菜单ID" data-name="upperid"></k-grid-column>
				<k-grid-column data-header="积木报表ID" data-name="jimuReportId"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-functype="POPUP"
						data-size="mini"
						data-target="editDialog"
					>
						编辑
					</k-btn>
					<k-btn
						class="btn-custom-text"
						:dataParams="scope.row"
						:data-handler="reportConfig"
						data-size="mini"
						data-type="danger"
					>
						报表配置
					</k-btn>
					<!-- <k-btn
								class="md-success"
								:data-handler="handleDown"
								data-size="mini"
								data-descript="导出单个报表"
								data-functype="EXPORT"
								data-target="tableGrid"
								data-export-name="菜单文件"
							>
								导出
							</k-btn> -->
					<k-btn
						data-functype="DOWNLOAD"
						:data-download-name="scope.row.row.menuname + '.docx'"
						class="btn-custom-text"
						v-if="global.getProdIfUser(scope.row.row.id)"
						data-target="prodInfoGrid"
						data-url="/download/server/PmsApp/prod/downloadParamDistribution.json"
					>
						导出
					</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="JmReportMenuInfo.deleteReportMenuInfo"
						data-size="mini"
						data-type="danger"
						data-target="tableGrid"
						:data-confirm="true"
					>
						删除
					</k-btn>
				</template>
			</k-grid>
		</div>
		<!--新增菜单-->
		<k-popup ref="addDialog" data-title="增加" data-width="50%">
			<AddMenuDialog data-type="insert" />
		</k-popup>
		<k-popup ref="editDialog" data-title="修改" data-width="50%">
			<AddMenuDialog :dataData="formData" data-type="update" />
		</k-popup>
		<!-- 上传xml文件 -->
		<k-popup ref="M8430P004" data-title="上传xml文件">
			<UploadDialog />
		</k-popup>
	</div>
</template>
<script>
import AddMenuDialog from "@/pages/report/reportTemplate/components/addMenu/addMenuDialog.vue";
import UploadDialog from "@/pages/report/reportTemplate/components/addMenu/uploadDialog.vue";

export default {
	components: {
		AddMenuDialog,
		UploadDialog,
	},
	data() {
		return {
			tableData: {
				rows: [
					{
						menuid: "001",
						menuname: "ee",
					},
				],
				total: {},
			},
			selectRowData: {},
			formData: {},
		};
	},
	methods: {
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},
		handleDown() {},
		reportCheck() {
			this.$router.push({
				path: "/main/report/reportTemplate/reportQuery",
			});
		},
		reportConfig(v) {
			this.$router.push({
				path: "/main/report/reportTemplate/configReport",
				query: {
					menuid: v.menuid,
				},
			});
		},
	},
};
</script>
<style lang="scss" scoped></style>
