<template>
	<div class="py-page">
		<k-form-search-customize v-model="searchParam" data-target="tableGrid">
			<k-form-item label="报表名称">
				<k-field-select
					data-value-field="reportName"
					data-display-field="reportId"
					v-model="searchParam.reportName"
					data-placeholder="报表名称"
				/>
			</k-form-item>
		</k-form-search-customize>
		<div class="py-page-container">
			<k-grid
				ref="tableGrid"
				:data-operate-column="true"
				data-action=""
				:data-data="tableData"
				data-operate-width="80px"
				@data-row-select="selectRow"
			>
				<k-grid-column data-header="主键" data-name="count"></k-grid-column>
				<k-grid-column data-header="名称" data-name="name"></k-grid-column>
				<k-grid-column data-header="类型" data-name="type"></k-grid-column>
				<k-grid-column data-header="创建人" data-name="createser"></k-grid-column>
				<k-grid-column data-header="更新人" data-name="updateUser"></k-grid-column>
				<k-grid-column data-header="删除标识" data-name="delFlag"></k-grid-column>
				<k-grid-column data-header="是否是模板" data-name="isTemplate"></k-grid-column>
				<k-grid-column data-header="浏览次数" data-name="viewCount"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text"
						data-functype="POPUP"
						:data-handler="editJmReport"
					>
						编辑
					</k-btn>
				</template>
			</k-grid>
		</div>
	</div>
</template>
<script>
export default {
	data() {
		return {
			searchParam: {},
			tableData: {
				rows: [
					{
						name: "a",
					},
				],
				total: {},
			},
			formData: {},
			selectRowData: {},
		};
	},
	methods: {
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},
		editJmReport() {
			this.$router.push({
				path: "/main/report/reportTemplate/editReport",
			});
		},
	},
};
</script>