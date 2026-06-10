<template>
  <div class="py-page">
		<div>
		<k-form-search-customize data-model-name="ReportDataAudit" v-model="searchParam" data-target="tableGrid">
          <k-form-item label="复核日期">
            <k-field-date v-model="searchParam.auditDate"/>
          </k-form-item>
          <k-form-item label="报表名称">
            <k-field-select v-model="searchParam.tableId" data-dict="report_data_table"/>
          </k-form-item>
        </k-form-search-customize>
		</div>
    <div class="py-page-container">
			<k-grid ref="tableGrid" @data-row-select="selectRow" data-action="ReportDataAudit.findReportDataAudits" data-operate-column="false">
				<k-grid-column data-header="ID" data-name="id" data-hidden="true"></k-grid-column>
				<k-grid-column data-header="报送报表" data-name="tableId"></k-grid-column>
				<k-grid-column data-header="报表名称" data-name="tableName"></k-grid-column>
				<k-grid-column data-header="起始日期" data-name="beginDate" data-type="date" data-date-format="yyyy-MM-dd"></k-grid-column>
				<k-grid-column data-header="结束日期" data-name="endDate" data-type="date" data-date-format="yyyy-MM-dd"></k-grid-column>
				<k-grid-column data-header="日期类型" data-name="dateType" data-dict="audit_date_type"></k-grid-column>
				<k-grid-column data-header="复核日期" data-name="auditDate" data-type="date" data-date-format="yyyy-MM-dd"></k-grid-column>
				<k-grid-column data-header="复核状态" data-name="auditStatus" data-dict="xp_disclosure_check_status"></k-grid-column>
			</k-grid>
		</div>
	</div>
</template>

<script>
import Tools from "@/utils/tools";
export default {
	name: "ReportDataAudit",
	data() {
		return {
			formData: {},
			selectRowData:{},
			searchParam: {},
		};
	},
	methods: {
		selectRow(row, column, event) {
			this.selectRowData = assign({}, row);
      this.formData = assign({}, row);
		},
	},
};
</script>

