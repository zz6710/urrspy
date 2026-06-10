<template>
  <div class="py-page">
    <div>
		<k-form-search-customize data-model-name="BaseSubjectMap" v-model="searchParam" data-target="baseSubjectMapGrid" data-label-width="100px">
			<k-form-item label="科目代码">
          <k-field-text v-model="searchParam.accountCode"/>
      </k-form-item>
      <k-form-item label="科目名称">
        <k-field-text v-model="searchParam.accountName"/>
      </k-form-item>
      <k-form-item label="资产三类编码">
        <k-field-select v-model="searchParam.asst3Knd" data-dict="asst_3_knd" data-display-field="itemkey,itemval"/>
      </k-form-item>
      <k-form-item label="报表名称">
        <k-field-text v-model="searchParam.reportName"/>
            </k-form-item>
			<k-form-item label="报表细类">
        <k-field-select v-model="searchParam.ctgCd" data-dict="ctg_cd"/>
      </k-form-item>
			<k-form-item label="资产代码">
                <k-field-text v-model="searchParam.asstCd"/>
            </k-form-item>
		</k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addBaseSubjectMapPopup">
            <md-icon md-src="/static/svg/add.svg" />新增
          </k-btn>
          <k-btn slot="button" ref="uploadBtnRef" data-functype="POPUP" class="btn-custom-plain" data-target="uploadBaseSubjectMapPopup" :load-disabled="false">
            <md-icon>cloud_upload</md-icon>导入
          </k-btn>
          <k-btn slot="button" class="btn-custom-plain" data-functype="EXPORT" data-target="baseSubjectMapGrid" data-export-name="资产负债分类配置表">
            <md-icon>cloud_download</md-icon>导出
          </k-btn>
          <k-btn slot="button" ref="reloadBtnRef" class="btn-custom-plain" data-functype="POPUP" data-target="handleTaskPopup" loading-tip="正在重新生成报表，请稍后重试！">
            <md-icon>cloud_download</md-icon>重新生成报表
				  </k-btn>
        </div>
        <ReReport ref="reReportRef" :formData="formData" :menuId="menuId" :buttonName="buttonName" />
      </div>
      <k-grid ref="baseSubjectMapGrid" @data-row-select="selectRow" data-action="BaseSubjectMap.findBaseSubjectMaps" >
    <k-grid-column data-header="id" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
    <k-grid-column data-header="科目代码" data-name="accountCode"></k-grid-column>
    <k-grid-column data-header="科目名称" data-name="accountName"></k-grid-column>
    <k-grid-column data-header="资产三类编码" data-name="asst3Knd"></k-grid-column>
    <k-grid-column data-header="资产三类名称" data-name="asst3KndName"></k-grid-column>
		<k-grid-column data-header="报表名称" data-name="reportName"></k-grid-column>
		<k-grid-column data-header="报表细类" data-name="ctgCd"></k-grid-column>
		<k-grid-column data-header="资产代码" data-name="asstCd"></k-grid-column>
		<k-grid-column data-header="备注" data-name="remark"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改资产负债分类配置表" data-functype="POPUP" data-size="mini"
            data-target="editBaseSubjectMapPopup">
			  修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="BaseSubjectMap.deleteBaseSubjectMap" data-size="mini"
            data-type="danger" data-target="baseSubjectMapGrid" :data-confirm="true" data-descript="删除资产负债分类配置表">
			  删除
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加资产负债分类配置表弹出框   -->
	<k-popup ref="addBaseSubjectMapPopup" data-title="新增">
    	<k-form ref="addBaseSubjectMapForm" :data-col="2">
      <k-form-item label="id" v-show="false">
        <k-field-text v-model="formData.id"/>
      </k-form-item>
      <k-form-item label="科目代码">
        <k-field-text v-model="formData.accountCode"/>
      </k-form-item>
			<k-form-item label="资产三类编码">
	        	<k-field-select v-model="formData.asst3Knd" data-dict="asst_3_knd" data-display-field="itemkey,itemval"/>
	     	</k-form-item>
      <k-form-item label="报表名称">
        <k-field-text v-model="formData.reportName"/>
      </k-form-item>
			<k-form-item label="报表细类">
            <k-field-select v-model="formData.ctgCd" data-dict="ctg_cd"/>
	     	</k-form-item>
			<k-form-item label="资产代码">
	        	<k-field-text v-model="formData.asstCd"/>
	     	</k-form-item>
			<k-form-item label="备注">
	        	<k-field-text v-model="formData.remark"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
              <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="BaseSubjectMap.addBaseSubjectMap" data-from="addBaseSubjectMapForm"
                     :data-model="formData" data-target="baseSubjectMapGrid">
                <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
              </k-btn>
              <k-btn class="btn-custom-plain" data-functype="CLOSE">
                <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
            </k-form-footer>
    	</k-form>
	</k-popup>

	<k-popup ref="uploadBaseSubjectMapPopup" data-title="导入">
        <k-form ref="addForm" data-ui="element">

          <k-form-item label="附件" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                                  data-accept=".xlsx,.xls" :data-error="onSubmitError" :data-success="onSubmitSuccess"
                                  :data-auto-upload="false"
                                  data-upload-url="upload/server/RptApp/uploadBaseSubjectMap.json">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="baseSubjectMapGrid" ref="submitBtn"
                   :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>
  
  <k-popup ref="handleTaskPopup" data-title="重新生成报表">
			<k-form ref="handleTaskAppForm" data-ui="element">
				<k-form-item label="数据日期" data-ui="element" data-input-width="500px">
					<k-field-date
						v-model="formData.reportDate"
						data-type="date"
						data-date-format="yyyy-MM-dd"
						data-value-format="yyyyMMdd"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn class="btn-custom-primary" data-from="editForm" :data-handler="handleTaskApp">
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
	</k-popup>

	<!--    修改资产负债分类配置表弹出框   -->
	<k-popup ref="editBaseSubjectMapPopup" data-title="修改">
	  <k-form ref="editBaseSubjectMapForm" :data-col="2">
		<k-form-item label="id" v-show="false">
        	<k-field-text v-model="formData.id"/>
     	</k-form-item>
		<k-form-item label="科目代码">
        	<k-field-text v-model="formData.accountCode"/>
     	</k-form-item>
		<k-form-item label="资产三类编码">
        	<k-field-select v-model="formData.asst3Knd" data-dict="asst_3_knd" data-display-field="itemkey,itemval"/>
     	</k-form-item>
    <k-form-item label="报表名称">
      <k-field-text v-model="formData.reportName"/>
    </k-form-item>
		<k-form-item label="报表细类">
        	<k-field-select v-model="formData.ctgCd1" data-dict="ctg_cd"/>
     	</k-form-item>
		<k-form-item label="资产代码">
        	<k-field-text v-model="formData.asstCd"/>
     	</k-form-item>
		<k-form-item label="备注">
        	<k-field-text v-model="formData.remark"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="BaseSubjectMap.updateBaseSubjectMap" data-from="editBaseSubjectMapForm"
	        :data-model="formData" data-target="baseSubjectMapGrid">
			  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
			  <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>
  </div>
</template>

<script>
  import ReReport from "@/utils/ReReport.vue";
  export default {
    components: {
      ReReport
   	},
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam: {},
        menuId: "M0408",
        buttonName: "重新生成报表",
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },

      onSubmitSuccess() {
          this.$refs.baseSubjectMapGrid.load(this.searchParam);
          this.$refs.uploadBtnRef.setIconStyle(1)
        },

        onSubmitError() {
          this.$refs.uploadBtnRef.setIconStyle(1)
        },

        submitUploadParam(){
          //文件上传校验
          let temp = document.getElementsByClassName('upload-demo');
          let lis = temp[0].childNodes[1].childNodes.length;
          if (lis > 0) {
            let formData = this.formData;
            this.$refs.uploadRef.upload(formData);
            this.$refs.uploadBtnRef.setIconStyle(0);
            setTimeout(()=>{
              this.$refs.uploadBaseSubjectMapPopup.close();
            }, 300)
          } else {
            this.$message.error("上传文件不能为空!");
            return false;
          }
        },

        handleTaskApp() {
          this.$refs.reReportRef.handleReports(this.formData.reportDate);
        },

    }
  };
</script>
