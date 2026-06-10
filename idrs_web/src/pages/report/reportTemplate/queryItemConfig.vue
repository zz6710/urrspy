<template>
	<div class="py-page">
		<k-form-search-customize v-model="searchParam" data-target="tableGrid">
			<k-form-item label="地址映射id">
				<k-field-text
					data-value-field="text"
					data-display-field="text"
					v-model="searchParam.menuid"
					data-exeid="JMREPORT0011"
					data-placeholder="地址映射id"
				/>
			</k-form-item>
		</k-form-search-customize>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn
						class="btn-custom-plain"
						data-functype="POPUP"
						data-target="addDialog"
					>
						新增条件
					</k-btn>
				</div>
			</div>
			<k-grid
				ref="tableGrid"
				:data-operate-column="true"
				data-action=""
				:data-data="tableData"
				data-operate-width="280px"
				@data-row-select="selectRow"
			>
				<k-grid-column data-header="查询名称" data-name="queryName"></k-grid-column>
				<k-grid-column data-header="地址映射id" data-name="menuid"></k-grid-column>
				<k-grid-column data-header="更新时间" data-name="upttime"></k-grid-column>
				<k-grid-column data-header="SQL语句" data-name="sql"></k-grid-column>
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
						data-functype="SUBMIT"
						data-action="AppraiseRgInfoErr.deleteAppraiseRgInfoErr"
						data-size="mini"
						data-type="danger"
						data-target="M8605P005"
						:data-confirm="true"
					>
						删除
					</k-btn>
					<k-btn
						class="btn-custom-text"
						:data-handler="handleCopy"
						data-size="mini"
						data-type="danger"
					>
						复制API
					</k-btn>
				</template>
			</k-grid>
		</div>
		<k-popup ref="addDialog" data-title="新增查询条件">
			<AddDialog />
		</k-popup>
		<k-popup ref="editDialog" data-title="修改">
			<AddDialog :dataData="formData" data-type="edit" />
		</k-popup>
	</div>
</template>
<script>
import AddDialog from "@/pages/report/reportTemplate/components/configReport/addDialog.vue";
import copy from "copy-to-clipboard";
export default {
	components: {
		AddDialog,
	},
	data() {
		return {
			searchParam: {},
			tableData: {
				rows: [
					{
						menuid: "001",
						menuname: "ee",
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
		handleCopy(row) {
			const c = copy("JmReport/querySQLApi?id=" + row.menuid);
			if (c) {
				this.$message({
					message: "代码已复制到剪贴板",
					type: "success",
				});
			} else {
				this.$message({
					message: "代码复制有点问题?",
					type: "error",
				});
			}
		},
	},
};
</script>