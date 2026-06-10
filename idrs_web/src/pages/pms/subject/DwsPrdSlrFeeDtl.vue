<template>
  <div class="py-page">
    <div>
		<k-form-search-customize data-model-name="DwsPrdSlrFeeDtl" v-model="searchParam" data-target="dwsPrdSlrFeeDtlGrid">
			<k-form-item label="数据日期">
        <k-field-date v-model="searchParam.dealDate" :data-allowblank="false"/>
      </k-form-item>
			<k-form-item label="产品代码">
        <k-field-text v-model="searchParam.prdcCd"/>
      </k-form-item>
<!--			<k-form-item label="产品名称">-->
<!--                <k-field-text v-model="searchParam.prdcNm"/>-->
<!--            </k-form-item>-->
			<k-form-item label="母产品代码">
        <k-field-text v-model="searchParam.motherPrdcCd"/>
      </k-form-item>
      <k-form-item label="销售商代码">
        <k-field-text v-model="searchParam.slrCd"/>
      </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
        <div class="left">
<!--          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addDwsPrdSlrFeeDtlPopup">-->
<!--            <md-icon md-src="/static/svg/add.svg" />新增-->
<!--          </k-btn>-->
<!--          <k-btn slot="button" data-functype="POPUP" class="btn-custom-plain" data-target="uploadDwsPrdSlrFeeDtlPopup">-->
<!--            <md-icon>cloud_upload</md-icon>导入-->
<!--          </k-btn>-->
          <k-btn slot="button" class="btn-custom-plain" data-functype="EXPORT" data-export-dict="false" data-target="dwsPrdSlrFeeDtlGrid" data-export-name="产品销售商费用明细表">
            <md-icon>cloud_download</md-icon>导出
          </k-btn>
        </div>
      <k-grid ref="dwsPrdSlrFeeDtlGrid" @data-row-select="selectRow" data-action="DwsPrdSlrFeeDtl.findDwsPrdSlrFeeDtls" data-operate-column="false" data-autoload="false">
		<k-grid-column data-header="ID" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
		<k-grid-column data-header="数据日期" data-name="dealDate"></k-grid-column>
		<k-grid-column data-header="产品代码" data-name="prdcCd"></k-grid-column>
		<k-grid-column data-header="产品名称" data-name="prdcNm"></k-grid-column>
		<k-grid-column data-header="母产品代码" data-name="motherPrdcCd"></k-grid-column>
		<k-grid-column data-header="费用类型" data-name="feeType"></k-grid-column>
		<k-grid-column data-header="费用" data-name="feeAmt"></k-grid-column>
		<k-grid-column data-header="销售商代码" data-name="slrCd"></k-grid-column>
		<k-grid-column data-header="销售商名称" data-name="slrNm"></k-grid-column>
		<k-grid-column data-header="创建日期" data-name="crtDt" data-export="false"></k-grid-column>
		<k-grid-column data-header="创建时间" data-name="crtTm" data-export="false"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改产品销售商费用明细表" data-functype="POPUP" data-size="mini"
            data-target="editDwsPrdSlrFeeDtlPopup">
			  修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="DwsPrdSlrFeeDtl.deleteDwsPrdSlrFeeDtl" data-size="mini"
               data-type="danger" data-target="dwsPrdSlrFeeDtlGrid" :data-confirm="true" data-descript="删除产品销售商费用明细表">
			  删除
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加产品销售商费用明细表弹出框   -->
	<k-popup ref="addDwsPrdSlrFeeDtlPopup" data-title="新增">
    	<k-form ref="addDwsPrdSlrFeeDtlForm" :data-col="2">
			<k-form-item label="ID" v-show="false">
	        	<k-field-text v-model="formData.id"/>
	     	</k-form-item>
			<k-form-item label="数据日期">
	        	<k-field-date v-model="formData.dealDate" :data-allowblank="false"/>
	     	</k-form-item>
<!--			<k-form-item label="产品代码">-->
<!--	        	<k-field-text v-model="formData.prdcCd" :data-allowblank="false"/>-->
<!--	     	</k-form-item>-->
			<k-form-item label="产品名称">
	        	<k-field-text v-model="formData.prdcNm"/>
	     	</k-form-item>
			<k-form-item label="母产品代码">
	        	<k-field-text v-model="formData.motherPrdcCd"/>
	     	</k-form-item>
			<k-form-item label="费用类型">
	        	<k-field-text v-model="formData.feeType"/>
	     	</k-form-item>
			<k-form-item label="费用">
	        	<k-field-text v-model="formData.feeAmt"/>
	     	</k-form-item>
			<k-form-item label="销售商代码">
	        	<k-field-text v-model="formData.slrCd"/>
	     	</k-form-item>
			<k-form-item label="销售商名称">
	        	<k-field-text v-model="formData.slrNm"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
              <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DwsPrdSlrFeeDtl.addDwsPrdSlrFeeDtl" data-from="addDwsPrdSlrFeeDtlForm"
                     :data-model="formData">
                <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
              </k-btn>
              <k-btn class="btn-custom-plain" data-functype="CLOSE">
                <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
            </k-form-footer>
    	</k-form>
	</k-popup>

	<k-popup ref="uploadDwsPrdSlrFeeDtlPopup" data-title="导入">
        <k-form ref="addForm" data-ui="element">

          <k-form-item label="附件" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                                  data-accept=".xlsx,.xls" :data-error="onSubmitError" :data-success="onSubmitSuccess"
                                  :data-auto-upload="false"
                                  data-upload-url="upload/server/RptApp/uploadDwsPrdSlrFeeDtl.json">
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

	<!--    修改产品销售商费用明细表弹出框   -->
	<k-popup ref="editDwsPrdSlrFeeDtlPopup" data-title="修改">
	  <k-form ref="editDwsPrdSlrFeeDtlForm" :data-col="2">
		<k-form-item label="ID" v-show="false">
        	<k-field-text v-model="formData.id"/>
     	</k-form-item>
		<k-form-item label="数据日期">
        	<k-field-date v-model="formData.dealDate" :data-allowblank="false" data-disabled="true"/>
     	</k-form-item>
		<k-form-item label="产品代码">
        	<k-field-text v-model="formData.prdcCd" :data-allowblank="false" data-disabled="true"/>
     	</k-form-item>
		<k-form-item label="产品名称">
        	<k-field-text v-model="formData.prdcNm"/>
     	</k-form-item>
		<k-form-item label="母产品代码">
        	<k-field-text v-model="formData.motherPrdcCd"/>
     	</k-form-item>
		<k-form-item label="费用类型">
        	<k-field-text v-model="formData.feeType"/>
     	</k-form-item>
		<k-form-item label="费用">
        	<k-field-text v-model="formData.feeAmt"/>
     	</k-form-item>
		<k-form-item label="销售商代码">
        	<k-field-text v-model="formData.slrCd"/>
     	</k-form-item>
		<k-form-item label="销售商名称">
        	<k-field-text v-model="formData.slrNm"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DwsPrdSlrFeeDtl.updateDwsPrdSlrFeeDtl" data-from="editDwsPrdSlrFeeDtlForm"
	        :data-model="formData" data-target="dwsPrdSlrFeeDtlGrid">
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
  export default {
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam: {}
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },

      onSubmitSuccess() {
          this.$refs.uploadRef.doReset();
          this.$refs.addForm.reset();
          this.$refs.uploadDwsPrdSlrFeeDtlPopup.close();
          this.$refs.dwsPrdSlrFeeDtlGrid.load(this.searchParam);
        },

        onSubmitError() {
          this.$refs.uploadRef.doReset();
          this.$refs.submitBtn.setIconStyle(1, [])
        },

        submitUploadParam(){
          //文件上传校验
          let temp = document.getElementsByClassName('upload-demo');
          let lis = temp[0].childNodes[1].childNodes.length;
          if (lis > 0) {
            let formData = this.formData;
            this.$refs.uploadRef.upload(formData);
          } else {
            this.$message.error("上传文件不能为空!");
            return false;
          }
        },

    }
  };
</script>
