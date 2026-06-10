<template>
  <div class="py-page">
    <div>
		<k-form-search-customize data-model-name="DwsAstMngPlanInfo" v-model="searchParam" data-target="dwsAstMngPlanInfoGrid">
			<k-form-item label="产品代码">
                <k-field-text v-model="searchParam.prodCd"/>
            </k-form-item>
			<k-form-item label="产品名称">
                <k-field-text v-model="searchParam.prodNm"/>
            </k-form-item>
			<k-form-item label="发行机构代码">
                <k-field-text v-model="searchParam.issuerOrgnCd"/>
            </k-form-item>
			<k-form-item label="发行机构名称">
                <k-field-text v-model="searchParam.issuerOrgnNm"/>
            </k-form-item>
<!--			<k-form-item label="产品品种名称">-->
<!--                <k-field-text v-model="searchParam.prodBredCd"/>-->
<!--            </k-form-item>-->
<!--			<k-form-item label="产品起始日期">-->
<!--                <k-field-date v-model="searchParam.prodOpnDt"/>-->
<!--            </k-form-item>-->
<!--			<k-form-item label="产品变更日期">-->
<!--                <k-field-date v-model="searchParam.prodUpDt"/>-->
<!--            </k-form-item>-->
<!--			<k-form-item label="产品预计终止日期">-->
<!--                <k-field-date v-model="searchParam.prodExpcEndDt"/>-->
<!--            </k-form-item>-->
<!--			<k-form-item label="产品实际终止日期">-->
<!--                <k-field-date v-model="searchParam.prodActlEndDt"/>-->
<!--            </k-form-item>-->
<!--			<k-form-item label="数据日期">-->
<!--                <k-field-date v-model="searchParam.actDt"/>-->
<!--            </k-form-item>-->
		</k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addDwsAstMngPlanInfoPopup">
            <md-icon md-src="/static/svg/add.svg" />新增
          </k-btn>
          <k-btn slot="button" ref="uploadBtnRef" data-functype="POPUP" class="btn-custom-plain" data-target="uploadDwsAstMngPlanInfoPopup" :load-disabled="false">
            <md-icon>cloud_upload</md-icon>导入
          </k-btn>
          <k-btn slot="button" class="btn-custom-plain" data-functype="EXPORT" data-export-dict="false" data-target="dwsAstMngPlanInfoGrid" data-export-name="公开spv信息">
            <md-icon>cloud_download</md-icon>导出
          </k-btn>
          <k-btn slot="button" ref="reloadBtnRef" class="btn-custom-plain" data-functype="POPUP" data-target="handleTaskPopup" loading-tip="正在重新生成报表，请稍后重试！">
            <md-icon>cloud_download</md-icon>重新生成报表
				  </k-btn>
        </div>
        <ReReport ref="reReportRef" :formData="formData" :menuId="menuId" :buttonName="buttonName" />
      </div>

      <k-grid ref="dwsAstMngPlanInfoGrid" @data-row-select="selectRow" data-action="DwsAstMngPlanInfo.findDwsAstMngPlanInfos" data-autoload="false">
        <k-grid-column data-header="id" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
		<k-grid-column data-header="产品代码" data-name="prodCd"></k-grid-column>
		<k-grid-column data-header="发行机构代码" data-name="issuerOrgnCd"></k-grid-column>
		<k-grid-column data-header="发行机构名称" data-name="issuerOrgnNm"></k-grid-column>
		<k-grid-column data-header="产品品种名称" data-name="prodBredCd"></k-grid-column>
    <k-grid-column data-header="产品名称" data-name="prodNm"></k-grid-column>
		<k-grid-column data-header="产品起始日期" data-name="prodOpnDt"></k-grid-column>
		<k-grid-column data-header="产品变更日期" data-name="prodUpDt"></k-grid-column>
		<k-grid-column data-header="产品预计终止日期" data-name="prodExpcEndDt"></k-grid-column>
		<k-grid-column data-header="产品实际终止日期" data-name="prodActlEndDt"></k-grid-column>
<!--		<k-grid-column data-header="数据日期" data-name="actDt" data-hidden="true" data-export="false"></k-grid-column>-->
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改公开spv信息" data-functype="POPUP" data-size="mini"
            data-target="editDwsAstMngPlanInfoPopup">
			  修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="DwsAstMngPlanInfo.deleteDwsAstMngPlanInfo" data-size="mini"
               data-type="danger" data-target="dwsAstMngPlanInfoGrid" :data-confirm="true" data-descript="删除公开spv信息">
			  删除
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加公开spv信息弹出框   -->
	<k-popup ref="addDwsAstMngPlanInfoPopup" data-title="新增">
    	<k-form ref="addDwsAstMngPlanInfoForm" :data-col="2">
        <k-form-item label="id" v-show="false">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
			<k-form-item label="产品代码">
	        	<k-field-text v-model="formData.prodCd" :data-allowblank="false"/>
	     	</k-form-item>
			<k-form-item label="产品名称">
	        	<k-field-text v-model="formData.prodNm"/>
	     	</k-form-item>
			<k-form-item label="发行机构代码">
	        	<k-field-text v-model="formData.issuerOrgnCd"/>
	     	</k-form-item>
			<k-form-item label="发行机构名称">
	        	<k-field-text v-model="formData.issuerOrgnNm"/>
	     	</k-form-item>
			<k-form-item label="产品品种名称">
	        	<k-field-text v-model="formData.prodBredCd"/>
	     	</k-form-item>
			<k-form-item label="产品起始日期">
	        	<k-field-date v-model="formData.prodOpnDt"/>
	     	</k-form-item>
			<k-form-item label="产品变更日期">
	        	<k-field-date v-model="formData.prodUpDt"/>
	     	</k-form-item>
			<k-form-item label="产品预计终止日期">
	        	<k-field-date v-model="formData.prodExpcEndDt"/>
	     	</k-form-item>
			<k-form-item label="产品实际终止日期">
	        	<k-field-date v-model="formData.prodActlEndDt"/>
	     	</k-form-item>
<!--			<k-form-item label="数据日期">-->
<!--	        	<k-field-date v-model="formData.actDt"/>-->
<!--	     	</k-form-item>-->

	      	<k-form-footer data-align="center">
              <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DwsAstMngPlanInfo.addDwsAstMngPlanInfo" data-from="addDwsAstMngPlanInfoForm"
                     :data-model="formData">
                <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
              </k-btn>
              <k-btn class="btn-custom-plain" data-functype="CLOSE">
                <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
            </k-form-footer>
    	</k-form>
	</k-popup>

	<k-popup ref="uploadDwsAstMngPlanInfoPopup" data-title="导入">
        <k-form ref="addForm" data-ui="element">

          <k-form-item label="附件" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                                  data-accept=".xlsx,.xls,.csv" :data-error="onSubmitError" :data-success="onSubmitSuccess"
                                  :data-auto-upload="false"
                                  data-upload-url="upload/server/RptApp/uploadDwsAstMngPlanInfo.json">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" ref="submitBtn"
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

	<!--    修改公开spv信息弹出框   -->
	<k-popup ref="editDwsAstMngPlanInfoPopup" data-title="修改">
	  <k-form ref="editDwsAstMngPlanInfoForm" :data-col="2">
      <k-form-item label="id" v-show="false">
        <k-field-text v-model="formData.id"/>
      </k-form-item>
		<k-form-item label="产品代码">
        	<k-field-text v-model="formData.prodCd" :data-allowblank="false" data-disabled="true"/>
     	</k-form-item>
		<k-form-item label="产品名称">
        	<k-field-text v-model="formData.prodNm"/>
     	</k-form-item>
		<k-form-item label="发行机构代码">
        	<k-field-text v-model="formData.issuerOrgnCd"/>
     	</k-form-item>
		<k-form-item label="发行机构名称">
        	<k-field-text v-model="formData.issuerOrgnNm"/>
     	</k-form-item>
		<k-form-item label="产品品种名称">
        	<k-field-text v-model="formData.prodBredCd"/>
     	</k-form-item>
		<k-form-item label="产品起始日期">
        	<k-field-date v-model="formData.prodOpnDt"/>
     	</k-form-item>
		<k-form-item label="产品变更日期">
        	<k-field-date v-model="formData.prodUpDt"/>
     	</k-form-item>
		<k-form-item label="产品预计终止日期">
        	<k-field-date v-model="formData.prodExpcEndDt"/>
     	</k-form-item>
		<k-form-item label="产品实际终止日期">
        	<k-field-date v-model="formData.prodActlEndDt"/>
     	</k-form-item>
<!--		<k-form-item label="数据日期">-->
<!--        	<k-field-date v-model="formData.actDt"/>-->
<!--     	</k-form-item>-->
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DwsAstMngPlanInfo.updateDwsAstMngPlanInfo" data-from="editDwsAstMngPlanInfoForm"
	        :data-model="formData" data-target="dwsAstMngPlanInfoGrid">
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
        menuId: "M061802",
        buttonName: "重新生成报表",
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },

      onSubmitSuccess() {
          this.$refs.dwsAstMngPlanInfoGrid.load(this.searchParam);
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
          this.$refs.uploadBtnRef.setIconStyle(0)
          setTimeout(()=>{
            this.$refs.uploadDwsAstMngPlanInfoPopup.close();
          }, 300)
        } else {
          this.$message.error("上传文件不能为空!");
          return false;
        }
        return false
      },

      handleTaskApp() {
        this.$refs.reReportRef.handleReports(this.formData.reportDate);
      },
    },
  };
</script>
