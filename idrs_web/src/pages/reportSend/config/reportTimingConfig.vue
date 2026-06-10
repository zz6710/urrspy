<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="ReportTimeConfig" data-target="reportTimeConfigGrid" data-label-width="100px" v-model="searchParam" @handleReset="reportOnChangeSearch">
        <k-form-item label="报表大类">
          <k-field-select v-model="searchParam.reportType" data-dict="report_type" @data-on-change="reportOnChangeSearch"/>
        </k-form-item>
        <k-form-item label="报送报表名称">
					<k-field-select
						v-model="searchParam.tableName"
						data-action="ReportTimeConfig.getReportTable"
						:data-params="{reportType: searchParam.reportType}"
						data-value-field="reportTable"
						data-display-field="tableName"
						:key="formKey"
					/>
				</k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addReportTimeConfigPopup" slot="button">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
            <k-btn slot="button"  data-functype="POPUP" class="btn-custom-plain"
                data-target="uploadReportTimingConfigPopup">
            <md-icon>cloud_upload</md-icon>
            导入
          </k-btn>
          <k-btn slot="button"  class="btn-custom-plain"  data-functype="EXPORT" @click ="downTemplateFile" :data-export-name="'报表时点配置模板'">
            <md-icon>cloud_download</md-icon>
            下载模板
          </k-btn>
        </div>
      </div>
      <k-grid ref="reportTimeConfigGrid" @data-row-select="selectRow" data-operate-width="150px" data-action="ReportTimeConfig.findReportTimeConfigs" >
		<k-grid-column data-header="id" data-name="id" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="报表大类" data-name="reportType" data-dict="report_type"></k-grid-column>
		<k-grid-column data-header="报表名称" data-name="reportTable" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="报表名称" data-name="tableName"></k-grid-column>
		<k-grid-column data-header="基准日期" data-name="baseType" data-dict="base_type"></k-grid-column>
    <k-grid-column data-header="时点类型" data-name="timeType" data-dict="reportTimeType"></k-grid-column>
		<k-grid-column data-header="日期类型" data-name="dataType" data-dict="date_type"></k-grid-column>
		<k-grid-column data-header="行内报送时点要求标识" data-name="innerSubmissionTime" data-dict="t8_disclosure_compute_date" :data-hidden="true"></k-grid-column>
		<k-grid-column data-header="行内报送时点要求（天）" data-name="innerSubmissionTimeRequire"></k-grid-column>
		<k-grid-column data-header="监管报送时点要求标识" data-name="superviseSubmissionTime" data-dict="t8_disclosure_compute_date" :data-hidden="true"></k-grid-column>
		<k-grid-column data-header="监管报送时点要求（天）" data-name="superviseSubmissionTimeRequire"></k-grid-column>
		<k-grid-column data-header="报送数据生成日期(天)" data-name="dataGenerTimeRequire"></k-grid-column>
		<k-grid-column data-header="更新时间" data-name="updateDate" data-type="date"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text specialClass" data-descript="修改报送时点配置" data-functype="POPUP" data-size="mini" style="min-width:60px;"
                 data-target="editReportTimeConfigPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text specialClass" data-functype="SUBMIT" data-action="ReportTimeConfig.deleteReportTimeConfig" data-size="mini" style="min-width:60px;"
                 data-type="danger" data-target="reportTimeConfigGrid" :data-confirm="true" data-descript="删除报送时点配置">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加报送时点配置弹出框   -->
	<k-popup ref="addReportTimeConfigPopup" data-title="新增" @data-closed="getReportName">
    	<k-form ref="addReportTimeConfigForm" :data-col="2" data-label-width="140px">
			<k-form-item label="报表大类">
	        	<k-field-select v-model="formData.reportType" :data-allowblank="false" data-dict="report_type" @data-on-change="reportOnChange"/>
	     	</k-form-item>
      <k-form-item label="报送报表名称">
        <k-field-select
          v-model="formData.reportTable"
          :data-allowblank="false"
          data-action="ReportTimeConfig.getReportTable"
          :data-params="{reportType: formData.reportType}"
          data-value-field="reportTable"
          data-display-field="tableName"
          :key="formKey1"
        />
      </k-form-item>
			<k-form-item label="基准日期">
	        	<k-field-select v-model="formData.baseType" data-dict="base_type" :data-allowblank="false"/>
	     	</k-form-item>
      <k-form-item label="时点类型">
          <k-field-select v-model="formData.timeType" data-dict="reportTimeType" :data-allowblank="false" data-default-value="0" @data-on-change="changetimeType"/>
          <k-btn style="margin: 0" class="btn-custom-primary" v-if="formData.timeType == '1'" data-target="calendarPopup" data-functype="POPUP" data-size="mini">设置</k-btn>
      </k-form-item>
      <k-form-item label="日期类型">
	        	<k-field-select v-model="formData.dataType" data-dict="date_type" :data-allowblank="false"/>
	     	</k-form-item>
      <template v-if="formData.timeType == '0'">
        <k-form-item label="行内计划时间设置" :data-col="2">
          <div>
            <div>
              <span style="color: #F56C6C">*</span>行内报送时点要求(天) T
              <k-field-select v-model="formData.innerSubmissionTime" data-dict="t8_disclosure_compute_date" style="width: 60px;"
                              data-placeholder=""  data-default-value="2"/>
              <input-number-controller v-model="formData.innerSubmissionTimeRequire" controls-position="right" :min="0"
                                       :precision="0" :step="1" ref="innerSubmissionTimeRequire"/>
              日
            </div>
          </div>
        </k-form-item>
        <k-form-item label="监管计划时间设置" :data-col="2">
                  <div>
                    <div>
                      <span style="color: #F56C6C">*</span>监管报送时点要求(天) T
                      <k-field-select v-model="formData.superviseSubmissionTime" data-dict="t8_disclosure_compute_date" style="width: 60px;"
                                      data-placeholder=""  data-default-value="2"/>
                      <input-number-controller v-model="formData.superviseSubmissionTimeRequire" controls-position="right" :min="0"
                                               :precision="0" :step="1" ref="superviseSubmissionTimeRequire"/>
                      日
                    </div>
                  </div>
                </k-form-item>
      </template>
      <k-form-item label="任务计划时间设置" :data-col="2">
          <div>
            <div>
              <span style="color: #F56C6C">*</span>报送数据生成日期(天) T
              <k-field-select v-model="formData.dataGenerTime" data-dict="t8_disclosure_compute_date" style="width: 60px;"
                              data-placeholder="" data-default-value="2" />
              <input-number-controller v-model="formData.dataGenerTimeRequire" controls-position="right" :min="0"
                                        :precision="0" :step="1" ref="dataGenerTimeRequire"/>
              日
            </div>
          </div>
        </k-form-item>
	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ReportTimeConfig.addReportTimeConfig" data-from="addReportTimeConfigForm" :data-handler="salesSubmitHandle"
		               :data-model="formData" data-target="reportTimeConfigGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改报送时点配置弹出框   -->
	<k-popup ref="editReportTimeConfigPopup" data-title="修改" @data-opened="editConfig" @data-closed="getReportName">
	  <k-form ref="editReportTimeConfigForm" :data-col="2" data-label-width="140px">
      <k-form-item label="报表大类">
        <k-field-select v-model="formData.reportType" :data-allowblank="false" data-dict="report_type" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="报送报表名称">
        <k-field-select v-model="formData.reportTable" :data-allowblank="false" :data-data="formData.reportTableDict" data-value-field="reportTable" data-display-field="tableName" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="基准日期">
        <k-field-select v-model="formData.baseType" data-dict="base_type" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="时点类型">
          <k-field-select v-model="formData.timeType" data-dict="reportTimeType" :data-allowblank="false"/>
          <k-btn style="margin: 0" class="btn-custom-primary" v-if="formData.timeType == '1'" data-target="calendarPopup" data-functype="POPUP" data-size="mini">设置</k-btn>
      </k-form-item>
      <k-form-item label="日期类型">
        <k-field-select v-model="formData.dataType" data-dict="date_type" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="计划时间设置" :data-col="2">
        <div>
          <div>
            <span style="color: #F56C6C">*</span>报送时点要求(天) T
            <k-field-select v-model="formData.innerSubmissionTime" data-dict="t8_disclosure_compute_date" style="width: 60px;"  :data-allowblank="false"
                            data-placeholder="" />
            <input-number-controller v-model="formData.innerSubmissionTimeRequire" controls-position="right" :min="0" :data-allowblank="false"
                                     :precision="0" :step="1" ref="innerSubmissionTimeRequire"/>
            日
          </div>
        </div>
      </k-form-item>
      <template v-if="formData.timeType == '0'">      
      <k-form-item label="监管计划时间设置" :data-col="2">
          <div>
            <div>
              <span style="color: #F56C6C">*</span>监管报送时点要求(天) T
              <k-field-select v-model="formData.superviseSubmissionTime" data-dict="t8_disclosure_compute_date" style="width: 60px;"  :data-allowblank="false"
                              data-placeholder=""  />
              <input-number-controller v-model="formData.superviseSubmissionTimeRequire" controls-position="right" :min="0" :data-allowblank="false"
                                       :precision="0" :step="1"ref="superviseSubmissionTimeRequire"/>
              日
            </div>
          </div>
        </k-form-item>
    </template>
    <k-form-item label="任务计划时间设置" :data-col="2">
        <div>
          <div>
            <span style="color: #F56C6C">*</span>报送数据生成日期(天) T
            <k-field-select v-model="formData.dataGenerTime" data-dict="t8_disclosure_compute_date" style="width: 60px;"  :data-allowblank="false"
                            data-placeholder=""  />
            <input-number-controller v-model="formData.dataGenerTimeRequire" controls-position="right" :min="0" :data-allowblank="false"
                                      :precision="0" :step="1" ref="dataGenerTimeRequire"/>
            日
          </div>
        </div>
      </k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ReportTimeConfig.updateReportTimeConfig" data-from="editReportTimeConfigForm" :data-handler="salesSubmitHandle"
	        :data-model="formData" data-target="reportTimeConfigGrid">
	        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
	        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>

  <k-popup ref="uploadReportTimingConfigPopup" title="报送时点配置导入">
        <k-form ref="addForm" data-ui="element">
          <k-form-item label="报送时点配置导入" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                data-accept=".xlsx,.xls"
                :data-error="onSubmitError" :data-success="onSubmitSuccess"
                :data-auto-upload="false"
                data-upload-url="upload/server/RptApp/reportManage/reportTimeConfigImport.json">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="trCustRegisterInfoGrid" ref="submitBtn"
                  :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
          </k-form-footer>
        </k-form>
        </k-popup>

  <k-popup ref="calendarPopup" data-title="设置" data-width="80%" class="h-dialog">
    <div class="dialog-content">
			<div class="dialog-main">
        <CalendarYear ref="calendar" />
      </div>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" :data-handler="handleCalendarconfirm">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </k-form-footer>
    </div>
  </k-popup>
  </div>
</template>

<script>
  import inputNumberController from "@/pages/pms/basePublish/DisclosureRule/input-number-controller";
  import Tools from "@/utils/tools";
  import CalendarYear from "@/pages/reportSend/config/components/calendarYear.vue"
  export default {
    name:"reportTimingConfig",
    components: {inputNumberController, CalendarYear},
    data() {
      return {
        formData: {
          reportTableDict:'',
          reportTableShowDict:'',
          endDateList: [] /*非规则配置，监管报送截止日列表 */
        },
        selectRowData: {},
        searchParam:{
          tableName: ''
        },
        formKey: 1,
        formKey1: 1,
        submitFormData: {}
      };
    },
    created() {
      this.getReportName();
    },
    methods: {
      noUseMethod(){ //从后端项目resource目录下载文件，由于打包为jar包，因此该方法不适用[后端功能含有，但是弃用不合适当前项目]
        this.httpUtil.download({
              url: '/download/server/RptApp/downloadStaticFile/downFileByName.json',
              params: { fileName: "report_time_config_template.xlsx"},
              callback: () => {

              }
           });
      },
      downTemplateFile(){
        /*
        1. 配置一个报表（下面仅供参考） 注意：system_table_name字段的值必须为数据库中存在的表，否则会报错
        INSERT INTO app_table_info (system_table_name, system_table_name_cn, upt_date, upt_time, report_type, export_template, export_table_id, start_sheet)
          VALUES('base_submission_time_config', '报表时点配置模板', NULL, NULL, '99', NULL, NULL, '0');
        2.在页面 【基础数据 - 报送数据导入】 上传一个模板文件
         */
        this.httpUtil.download({
              url: '/download/server/DpsApp/templateDownload/downFileByReportId.json',
              params: { reportId: "base_submission_time_config"}, /*该参数是配置的模板id */
              callback: () => {

              }
           });
      },
      handleCalendarconfirm() {
        this.formData.endDateList = this.$refs.calendar.getAllDate()
        this.formData.endDateString = this.formData.endDateList.toString();
        this.$refs.calendarPopup.close()
        return false
      },
      changetimeType() {
        if (this.formData.timeType == '0') {

        } else if (this.formData.timeType == '1') {
          //当为非规则配置时，设置默认值
          this.formData.innerSubmissionTime = "0";
          this.formData.innerSubmissionTimeRequire = "0";
          this.formData.superviseSubmissionTime = "0";
          this.formData.superviseSubmissionTimeRequire = "0";
        }
      },
      reportOnChangeSearch(){
        this.searchParam.tableName = "";
			  this.formKey += 1;
      },
      editConfig(){
        this.$set(this.formData, 'reportTableDict', '');
        this.findTableName()
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
        this.formData.endDateList = this.formData.endDateString.split(",");
      },
      reportOnChange(){
        this.$set(this.formData, "reportTable", "");
			  this.formKey1 += 1;
      },
      getReportName() {
        this.$set(this.formData, 'reportTableShowDict', '');
        this.httpUtil.comnQuery({
          action: "ReportTimeConfig.getReportTable",
          params: null
        }).then(data => {
          this.formData.reportTableShowDict = data.rows;
        }).catch({})
      },
      findTableName(){
        this.httpUtil.comnQuery({
          action: "ReportTimeConfig.getReportTable",
          params: {reportTable: this.formData.reportTable}
        }).then(data => {
          this.formData.reportTableDict = data.rows;
        }).catch({})
      },
      onSubmitSuccess() {
      this.$refs.uploadRef.doReset();
      this.$refs.addForm.reset();
      this.$refs.uploadReportTimingConfigPopup.close();
      this.$refs.reportTimeConfigGrid.load({reportTable: this.formData.reportTable});
    },
    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, []);
    },
    submitUploadParam() {
      //文件上传校验
      var validate = this.$refs.addForm.validate();
      if (validate) {
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          this.$refs.uploadRef.upload();
          this.showSubmitBtn = true;
        } else {
          Tools.alert("上传文件不能为空!", "danger");
          this.showSubmitBtn = true;
          return false;
        }
      }
    },
      salesSubmitHandle(){
        if (this.formData.timeType == '1') {
          if (!this.formData.endDateList || !this.formData.endDateList.length) {
            Tools.alert("请配置日期规则！","danger");
            return false
          }
        } else if (this.formData.timeType == '0') {
          if(!this.formData.reportType || !this.formData.reportTable || !this.formData.baseType || !this.formData.dataType){
          }else{
            //  if (!this.formData.innerSubmissionTime || !this.formData.innerSubmissionTimeRequire){
            //    Tools.alert("请完善行内报送时点要求！","danger");
            //    return false;
            // }
            if (!this.formData.superviseSubmissionTime){
               Tools.alert("请完善监管报送时点要求！","danger");
               return false;
            }
            if (!this.formData.dataGenerTime) {
              Tools.alert("请完善报送数据生成日期！","danger");
               return false;
            }
          }
        }
       }
    },

  };
</script>
<style scoped>
>>> .el-table__cell {
  padding: 1px 0 !important;
}
>>> .specialClass > .md-ripple{
  padding: 5px !important;
}
</style>
