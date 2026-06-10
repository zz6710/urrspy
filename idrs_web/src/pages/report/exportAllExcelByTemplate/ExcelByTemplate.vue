<template>
	<div class="py-page">
		<div>
			<k-form-search-customize data-model-name="ExcelByTemplate" v-model="searchParam" data-target="tableGrid">
        <k-form-item label="系统表名 ">
          <k-field-select
            v-model="searchParam.systemTableName"
            :data-data="tableNameDict"
            data-display-field="systemTableName"
            data-value-field="id"
          />
        </k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<k-grid
				ref="tableGrid"
				@data-row-select="selectRow"
				data-action="ExcelByTemplate.findExcelByTemplateConfig"
			>
			   <k-grid-column data-header="id" data-name="id" data-hidden="true"></k-grid-column>
			  <k-grid-column data-header="子模板ID" data-name="exportTableId" data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="系统表名" data-name="systemTableName" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="系统表名" data-name="tableName"></k-grid-column>
        <k-grid-column data-header="表名" data-name="reportTable" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="模板名称" data-name="templateName"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn
            class="btn-custom-text"
            data-descript="导出"
            data-functype="POPUP"
            data-size="mini"
            data-target="updatePortFieldManageInfoPopup"
          >
            导出
          </k-btn>
            </template>
			</k-grid>
		</div>

    <!--       -->
    <k-popup ref="updatePortFieldManageInfoPopup" data-title="导出" @data-close="beforeDown">
      <k-form ref="updatePortFieldManageInfoForm" :data-col="2">
        <k-form-item label="数据日期">
          <k-field-date v-model="searchParam.reportDate" data-type="date"  data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn
          class="btn-custom-plain"
          data-functype="EXPORT"
          data-target="tableGrid"
          data-export-dict="true"
		  data-export-type ="2"
          :data-excel-template ="formData.templateName+'.xlsx'"
          :data-template-name="formData.templateName"
          :data-model="formData"
          :data-export-name="dataExportName"
		  :dataParams="searchParam">
          <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn
          >
        </k-form-footer>
      </k-form>
    </k-popup>
	</div>
</template>

<script>
import Tools from "@/utils/tools";
export default {
	name: "ExcelByTemplate",
	data() {
		return {
			formData: {},
			searchParam: {
				reportDate: ""
			},
			tableNameDict: [],
			dataExportName: '',
		};
	},
	created() {},
	watch: {
		'searchParam.reportDate'(value) {
			let templateName = this.formData.templateName;
			let excelName = templateName.replaceAll('YYYY年MM月', Tools.formatMonth(value))
			                            .replaceAll('YYYYMM', value.substring(0, 4) + value.substring(4, 6));
			this.dataExportName = templateName == '人行15张' ? value+'-01-91310000MA7GTQK786-浦银理财有限责任公司' : excelName;
		},
	},
	mounted: function () {
		this.initTableDict();
	},
	methods: {
		selectRow(row, column, event) {
			this.formData = Object.assign({}, row);
		},
		beforeDown() {
		    this.searchParam.reportDate = '';
		},
		initTableDict() {
			this.httpUtil
				.comnQuery({
					action: "ImportTemplateManageField01.findTableName",
					params: { dictName: "systemTableNameDict" },
				})
				.then((data) => {
					this.tableNameDict = data.rows;
				}).catch({});
		},
		onSubmitSuccess() {
			this.$refs.uploadRef.doReset();
			this.$refs.addForm.reset();
			this.$refs.addPopup.close();
			this.$refs.tableGrid.load(this.searchParam);
		},
		onSubmitError() {
			this.$refs.uploadRef.doReset();
		},
	},
};
</script>
