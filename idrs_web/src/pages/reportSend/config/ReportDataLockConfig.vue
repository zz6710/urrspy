<template>
	<div class="py-page">
		<div>
			<k-form-search-customize data-model-name="ReportDataLockConfig" v-model="searchParam" data-target="tableGrid" @handleReset="handleReset">
				<k-form-item label="报表大类">
          <k-field-select v-model="searchParam.reportCategory" data-dict="report_type_sub" @data-on-change="reportOnChangeSearch" data-dict-type="1"/>
        </k-form-item>

        <k-form-item label="报送报表名称">
          <k-field-select
            v-model="searchParam.reportTable"
            data-action="ReportDataLockConfig.getReportTable"
            :data-params="{reportCategory: searchParam.reportCategory}"
            data-value-field="reportTable"
            data-display-field="tableName"
            :key="formKey" />
        </k-form-item>

        <k-form-item label="最近日期锁表状态">
          <k-field-select v-model="searchParam.isLstLock" data-dict="is_locked" data-dict-type="1"/>
        </k-form-item>

        <k-form-item label="上级任务ID">
          <k-field-select v-model="searchParam.upperGrade" data-dict="mid_data_table" data-dict-type="1"/>
        </k-form-item>
			</k-form-search-customize>
		</div>

		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn slot="button" ref="batchUnlockBtnRef" data-functype="POPUP" class="btn-custom-plain" :data-handler="dataCheckBeforeLock" data-target="releaseLock" :load-disabled="false">
						<md-icon>cloud_upload</md-icon>
						批量解锁
					</k-btn>
					<k-btn slot="button" ref="batchLockBtnRef" data-functype="POPUP" class="btn-custom-plain" :data-handler="dataCheckBeforeLock" data-target="addLock" :load-disabled="false">
						<md-icon>cloud_upload</md-icon>
						批量锁定
					</k-btn>
				</div>
			</div>
			<k-grid
				ref="tableGrid"
				@data-row-select="selectRow"
				data-action="ReportDataLockConfig.findReportDataLockConfigInfo"
				data-operate-column="false"
				data-checkbox="true">
				<k-grid-column data-header="报表大类" data-name="reportCategory" data-dict="report_type_sub" data-dict-type="1"></k-grid-column>
				<k-grid-column data-header="报送报表" data-name="reportTable"></k-grid-column>
				<k-grid-column data-header="报表名称" data-name="tableName"></k-grid-column>
				<k-grid-column data-header="任务ID" data-name="taskId"></k-grid-column>
				<k-grid-column data-header="依赖上级任务ID" data-name="upperGrade"></k-grid-column>
				<k-grid-column data-header="任务名称" data-name="taskName"></k-grid-column>
				<k-grid-column data-header="最近数据日期" data-name="latestDate" data-type="date" data-date-format="yyyy-MM-dd"></k-grid-column>
				<k-grid-column data-header="最近数据日期锁表状态" data-name="isLstLock" data-dict="is_locked"></k-grid-column>
			</k-grid>
		</div>

		<div>
      <k-form-search-customize data-model-name="ReportDataLockConfigSec" v-model="searchParamSec" data-target="secTableGrid" >
        <k-form-item label="数据日期">
          <k-field-date v-model="searchParamSec.reportDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyy-MM" />
        </k-form-item>

        <k-form-item label="报表名称" >
          <k-field-text v-model="searchParamSec.tableName" data-disabled="true"/>
        </k-form-item>

        <k-form-item label="报送报表" v-show="false">
          <k-field-text v-model="searchParamSec.reportTable" />
        </k-form-item>
      </k-form-search-customize>
    </div>

		<!--  报送数据锁定记录表子菜单  -->
    <div class="py-page-container">
      <k-grid ref="secTableGrid"  :data-autoload="false"  @init="(grid)=>{this.$kgrid = grid}" :data-after-load="stepGridAfterLoad"
        data-action="ReportDataLockRecord.findReportDataLockRecordInfo" @data-row-select="stepSelectRow" data-operate-width="100px">
        <k-grid-column data-name="reportTable" data-header="报送报表" data-align="center"></k-grid-column>
        <k-grid-column data-name="tableName" data-header="报表名称" data-align="center"></k-grid-column>
        <k-grid-column data-name="reportDate" data-header="数据日期" data-align="center" data-type="date" data-date-format="yyyy-MM-dd"></k-grid-column>
        <k-grid-column data-name="lockStatus" data-header="锁表状态" data-dict="is_locked" data-align="center"></k-grid-column>
        <k-grid-column data-name="optUser" data-header="操作用户" data-align="center"></k-grid-column>
        <k-grid-column data-name="optDate" data-header="操作日期" data-align="center" ></k-grid-column>
        <k-grid-column data-name="optTime" data-header="操作时间" data-align="center"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-field-bswitch
            data-on-value="02"
            data-off-value="01"
            v-model="scope.row.row.lockStatus"
            data-on-action="ReportDataLockRecord.releaseReportData"
            data-off-action="ReportDataLockRecord.lockReportData"
            :data-params="scope.row.row"
            :data-confirm="true"
            data-on-confirm-info="解锁"
            data-off-confirm-info="锁定"
            v-if="
              global.isShowAuthorityButton('ReportDataLockRecord.releaseReportData') ||
              global.isShowAuthorityButton('ReportDataLockRecord.lockReportData')
            "
          />
        </template>
      </k-grid>
    </div>

		<k-popup ref="addLock" data-title="批量锁定">
			<k-form ref="addLockForm" data-ui="element">
				<k-form-item label="数据日期">
          <k-field-date
            v-model="formData.reportDate" :data-allowblank="false"
            data-type="month" data-date-format="yyyy-MM" data-value-format="yyyy-MM"
          />
        </k-form-item>

				<k-form-footer data-align="center">
					<k-btn class="btn-custom-primary" data-target="secTableGrid" data-functype="SUBMIT" data-action="ReportDataLockRecord.batchLockReportData"
					       data-from="addLockForm" :data-model="formData" :data-handler="reportBatchLock" :data-after-success="passDataSuccess">
					  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">
					  <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
					</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<k-popup ref="releaseLock" data-title="批量解锁">
      <k-form ref="releaseLockForm" data-ui="element">
        <k-form-item label="数据日期">
          <k-field-date
            v-model="formData.reportDate" :data-allowblank="false"
            data-type="month" data-date-format="yyyy-MM" data-value-format="yyyy-MM"
          />
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-target="secTableGrid" data-functype="SUBMIT" data-action="ReportDataLockRecord.batchLockReportData"
                 data-from="releaseLockForm" :data-model="formData" :data-handler="reportBatchUnLock" :data-after-success="passDataSuccess">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
          </k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

	</div>
</template>

<script>
import Tools from "@/utils/tools";
import { assign } from "lodash";

export default {
	name: "ReportDataLockConfig",
	data() {
		return {
			formData: {},
			selectRowData: {},
      stepFormData: {},
      stepSelectRowData: {},
			searchParam: {},
			searchParamSec: {},
			nowDate: "",
			fileData: [],
			loading: false,
			formKey: 1,
			$kgrid : null,
			reportTableId:"",
		};
	},
	created() {
		handleReset();
		this.$refs.tableGrid.load();
    this.$refs.secTableGrid.load();
	},
	methods: {
		stepSelectRow(row, column, event) {
      const _this = this;
      _this.stepSelectRowData = assign({}, row);
      _this.stepFormData = assign({}, row);
    },
    stepGridAfterLoad(row){
      if(this.$kgrid.list.length>0){
        this.stepGridSelected = true;
      }else{
        this.stepGridSelected = false;
      }
      return row;
    },
		handleReset() {
			this.searchParam.reportCategory = "";
			this.searchParam.reportTable = "";
			this.searchParam.isLstLock = "";
			this.searchParamSec.reportTable = "";
			this.searchParamSec.tableName = "";
			this.searchParamSec.reportDate = "";
			this.reportOnChangeSearch();
		},
		reportOnChangeSearch(){
      this.$set(this.searchParam, "reportTable", "");
      this.formKey += 1;
    },
		selectRow(row, column, event) {
			this.selectRowData = assign({}, row);
      this.formData = assign({}, row);
      this.reportTableId=row.reportTable;
      this.searchParamSec.reportTable=row.reportTable;
      this.searchParamSec.tableName=row.tableName;

      this.stepFormData.reportTable = row.reportTable;
      this.$kgrid.load({reportTable:this.reportTableId,reportDate:this.searchParamSec.reportDate});
		},
		onSubmitError() {
			this.$refs.uploadRef.doReset();
		},
		dataCheckBeforeLock () {
		  const list = this.$refs.tableGrid.getSelected();
      let len = list.length;
      if(len < 1) {
        Tools.alert("请勾选至少一张报表！", "danger");
        return false;
      }
		},
		reportBatchLock (params) {
		  const list = this.$refs.tableGrid.getSelected();
		  params.len = list.length;
      params.lockStatus = "01";

      params.reportTable = "";
      for(let i=0; i < list.length; i++){
        params.reportTable = params.reportTable + list[i].reportTable + ",";
      }
      return true;
		},
		reportBatchUnLock (params) {
		  const list = this.$refs.tableGrid.getSelected();
      params.len = list.length;
      params.lockStatus = "02";

      params.reportTable = "";
      for(let i=0; i < list.length; i++){
        params.reportTable = params.reportTable + list[i].reportTable + ",";
      }
      return true;
		},
		passDataSuccess() {
		  this.$refs.tableGrid.load(this.searchParam);
		  this.$refs.secTableGrid.load();
		  this.formData.reportDate = "";
		}
	},
};
</script>

<style lang="scss" scoped>
.md-switch {
	position: relative;
	display: inline-block;
	margin: auto;
}
</style>
