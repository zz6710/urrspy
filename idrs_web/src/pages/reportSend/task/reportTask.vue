<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="BaseReportResultInfo" data-target="BaseReportResultInfoGrid" data-label-width="100px" v-model="searchParam" @handleReset="reportOnChangeSearch">
        <k-form-item label="报表大类">
          <k-field-select v-model="searchParam.reportType" data-dict="report_type" @data-on-change="reportOnChangeSearch"/>
        </k-form-item>
        <k-form-item label="报送报表名称">
					<k-field-select
						v-model="searchParam.reportTableName"
						data-action="ReportTimeConfig.getReportTable"
						:data-params="{reportType: searchParam.reportType}"
						data-value-field="reportTable"
						data-display-field="tableName"
						:key="formKey"
					/>
				</k-form-item>
         <k-form-item label="行内报送日" data-label-width="120px">
            <k-field-date v-model="searchParam.theoryReportStartDate"/>
          </k-form-item>
         <k-form-item label="整体状态">
          <k-field-select v-model="searchParam.status" data-dict="allStatus"/>
        </k-form-item>


      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button"  class="btn-custom-plain"  data-functype="EXPORT" data-target="BaseReportResultInfoGrid" :data-export-name="'报送任务管理'">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
        </div>
      </div>
     <k-grid ref="BaseReportResultInfoGrid" @data-row-select="selectRow" data-action="BaseReportResultModel.findBaseReportResultInfo"
         data-operate-width="160px" data-fixed="right" :data-autoload="false">
        <template slot="operate" slot-scope="scope" >
           <k-btn class="btn-custom-text" data-descript="手工报送" data-functype="POPUP" data-size="mini"
               data-target="addBaseReportResultInfoPopup" :data-disabled="scope.row.row.status==1">手工报送</k-btn>
           <k-btn class="btn-custom-text" data-descript="撤销报送" data-functype="SUBMIT" data-confirm="true" data-action="BaseReportResultModel.cancelBaseReportResultInfo"
                data-size="mini" data-target="BaseReportResultInfoGrid" :data-disabled="scope.row.row.status==2">撤销报送</k-btn>
        </template>
        <k-grid-column data-align="left" data-header="id" data-name="id" data-hidden="true"  data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="报表大类" data-name="reportType" data-dict="report_type" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="系统关联表" data-name="reportTable" data-export="false" data-width="150" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="报表名称" data-name="reportTableName" data-width="200"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品代码" data-name="prodRegEnc" data-width="120" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="行内报送日" data-name="theoryReportStartDate"  data-type="date" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="监管报送截止日" data-name="theoryReportEndDate"  data-type="date" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="实际报送日期" data-name="registerDate" data-type="date" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="总数量" data-name="total" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="报送成功数量" data-name="reportSuccessNumber" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="整体状态" data-name="status" data-dict="allStatus" data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="创建日期" data-name="createDate" data-type="date"  data-export="false" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="创建时间" data-name="createTime" data-export="false"  data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="更新日期" data-name="updateDate" data-type="date"  data-export="false" ></k-grid-column>
        <k-grid-column data-align="left" data-header="更新时间" data-name="updateTime"  data-export="false" ></k-grid-column>
      </k-grid>
    </div>

	<!--    手工报送弹出框   -->
	<k-popup ref="addBaseReportResultInfoPopup" data-title="手工报送">
    	<k-form ref="addBaseReportResultInfoForm" :data-col="2" isFormBodyScreen>
			<k-form-item label="报表大类">
	        	<k-field-select v-model="formData.reportType" data-dict="report_type"  :data-allowblank="false" :data-max-length="6" :data-disabled="true"/>
	     	</k-form-item>
	    <k-form-item label="系统关联表" v-show="false">
      	    <k-field-text v-model="formData.reportTable" :data-disabled="true"/>
      </k-form-item>
			<k-form-item label="报表名称">
	        	<k-field-text v-model="formData.reportTableName" :data-disabled="true"/>
	    </k-form-item>
	    <k-form-item label="产品代码" v-show="false">
      	    <k-field-text v-model="formData.prodRegEnc" :data-disabled="true"/>
      </k-form-item>
			<k-form-item label="行内报送日">
	        	<k-field-date v-model="formData.theoryReportStartDate"  data-type="date"  data-date-format="yyyy-MM-dd"  data-value-format="yyyyMMdd"  :data-allowblank="false" :data-disabled="true"/>
	     	</k-form-item>
			<k-form-item label="实际报送日期">
	        	<k-field-date v-model="formData.registerDate"  data-type="date"  data-date-format="yyyy-MM-dd"  data-value-format="yyyyMMdd"  :data-allowblank="false"/>
	    </k-form-item>
			<k-form-item label="总数量">
	        	<k-field-text v-model="formData.total"  :data-disabled="true" />
	    </k-form-item>
	    <k-form-footer slot="footer" data-align="center">
		      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="BaseReportResultModel.updateBaseReportResultInfo" data-from="addBaseReportResultInfoForm"
		               :data-model="formData" data-target="BaseReportResultInfoGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
		      <k-btn class="btn-custom-plain" data-functype="CLOSE"><md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	     </k-form-footer>
    	</k-form>
	</k-popup>
  </div>
</template>

<script>
  import Tools from '@/utils/tools.js';
  export default {
    name: "reportTask",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{
          reportTableName: ''
        },
        formKey: 1
      };
    },
    created() {
      this.init()
    },
    activated() {
      this.init()
    },
    methods: {
      init() {
        if(this.$route.query.reportType != null && this.$route.query.reportType !=undefined && this.$route.query.reportType !=''){
            let reportType=this.$route.query.reportType;
            let reportTableName=this.$route.query.reportTableName;
            let theoryReportStartDate=this.$route.query.theoryReportStartDate;
            let status=this.$route.query.status;
            this.$set(this.searchParam,"reportType",reportType);
            this.$set(this.searchParam,"reportTableName",reportTableName);
            this.$set(this.searchParam, 'theoryReportStartDate',theoryReportStartDate);
            this.$set(this.searchParam,"status",status);
            this.$nextTick(() => {
                  this.$refs.BaseReportResultInfoGrid.load(this.searchParam);
            });
        }else{
          this.httpUtil.sysDate().then(res => {
            if (res){
              this.$set(this.searchParam, 'theoryReportStartDate', res.toString());
              this.$set(this.searchParam, 'status', '2');
              this.$refs.BaseReportResultInfoGrid.load(this.searchParam);
            }
          })
        }
      },
      reportOnChangeSearch(){
        this.searchParam.reportTableName = "";
			  this.formKey += 1;
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
    },
  };
</script>

