<template>
  <div class="py-page">
    <k-form-search-customize data-model-name="AmsFilesInfo" data-target="AmsFilesInfoGrid" v-model="searchParam">
      <k-form-item label="文件名">
        <k-field-text v-model="searchParam.fileName"/>
      </k-form-item>
    </k-form-search-customize>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-plain" style="width: 100px" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="uploadFilePopup" slot="button" v-if="global.isShowAuthorityButton('ImportTemplateManage.importTemplate')">
            <md-icon md-src="/static/svg/add.svg" />上传文件</k-btn>
        </div>
      </div>
      <k-grid ref="AmsFilesInfoGrid" @data-row-select="selectRow" data-action="AmsFilesInfo.findAmsFilesInfos" >
		<k-grid-column data-header="主键" data-name="id" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="文件名" data-name="fileName"></k-grid-column>
		<k-grid-column data-header="文件路径" data-name="ossPath"></k-grid-column>
		<k-grid-column data-header="文件类型" data-name="fileType"></k-grid-column>
		<k-grid-column data-header="上传时间" data-name="uploadTime"></k-grid-column>
        <template slot="operate" slot-scope="scope">
<!--          <k-btn class="btn-custom-text" data-descript="编辑" data-functype="POPUP" data-size="mini"-->
<!--                 data-target="editAmsFilesInfoPopup">-->
<!--            编辑-->
<!--          </k-btn>-->
          <k-btn class="btn-custom-text" data-descript="下载" data-functype="DOWNLOAD" data-size="mini" v-model="scope.row.row"
                 data-url="/download/server/BaseServer/fileManage/download.json">
            下载
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="AmsFilesInfo.deleteAmsFilesInfo" data-size="mini"
               data-type="danger" data-target="AmsFilesInfoGrid" :data-confirm="true" data-descript="删除文件管理">
          	删除
    	    </k-btn>
        </template>
      </k-grid>
    </div>

    <!--    上传模板   -->
    <k-popup ref="uploadFilePopup" data-title="上传模板" >
      <k-form ref="addForm" :data-col="2">
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit=1
                                data-accept="." :data-error="onSubmitError" :data-success="onSubmitSuccess"
                                :data-auto-upload="false"
                                data-upload-url="/upload/server/BaseServer/fileManage/uploadFile.json">
          </k-field-excel-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" ref="submitBtn" @click="submitUploadParam">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

	<!--    添加文件管理弹出框   -->
	<k-popup ref="addAmsFilesInfoPopup" data-title="新增">
    	<k-form ref="addAmsFilesInfoForm" :data-col="2">
			<k-form-item label="主键">
	        	<k-field-text v-model="formData.id"/>
	     	</k-form-item>
			<k-form-item label="文件名">
	        	<k-field-text v-model="formData.fileName"/>
	     	</k-form-item>
      <k-form-item label="文件类型">
        <k-field-text v-model="formData.fileType"/>
      </k-form-item>
			<k-form-item label="oss路径">
	        	<k-field-text v-model="formData.ossPath"/>
	     	</k-form-item>
			<k-form-item label="上传时间">
	        	<k-field-text v-model="formData.uploadTime"/>
	     	</k-form-item>
	      	<k-form-footer data-align="center">
		        <k-btn class="md-primary" data-functype="SUBMIT" data-action="AmsFilesInfo.addAmsFilesInfo" data-from="addAmsFilesInfoForm"
		               :data-model="formData" data-target="AmsFilesInfoGrid">
		          <i class="icon-confirm" />确定
		        </k-btn>
		        <k-btn class="md-info" data-functype="CLOSE">
		          <i class="icon-cancel" />取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改文件管理弹出框   -->
	<k-popup ref="editAmsFilesInfoPopup" data-title="修改">
	  <k-form ref="editAmsFilesInfoForm" :data-col="2">
		<k-form-item label="主键">
        	<k-field-text v-model="formData.id"/>
     	</k-form-item>
		<k-form-item label="文件名">
        	<k-field-text v-model="formData.fileName"/>
     	</k-form-item>
		<k-form-item label="oss路径">
        	<k-field-text v-model="formData.ossPath"/>
     	</k-form-item>
		<k-form-item label="文件类型">
        	<k-field-text v-model="formData.fileType"/>
     	</k-form-item>
		<k-form-item label="上传时间">
        	<k-field-text v-model="formData.uploadTime"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="md-primary" data-functype="SUBMIT" data-action="AmsFilesInfo.updateAmsFilesInfo" data-from="editAmsFilesInfoForm"
	        :data-model="formData" data-target="AmsFilesInfoGrid">
	        <i class="icon-confirm" />确定
	      </k-btn>
	      <k-btn class="md-info" data-functype="CLOSE">
	        <i class="icon-cancel" />取消</k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>
  </div>
</template>

<script>
  import KFieldSelect from "@/components/k-element/k-field-select/k-field-select.vue";
  import KFieldText from "@/components/k-element/k-field-text/k-field-text.vue";
  import Tools from "@/utils/tools";

  export default {
    components: {KFieldText, KFieldSelect},
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam: {},//查询参数
        showSubmitBtn:true,
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },

      onSubmitError() {
        this.fileList=[];
        this.fileNameList = [];
        this.$refs.uploadRef.doReset();
        this.$refs.submitBtn.setIconStyle(1);
        this.showSubmitBtn = true;
      },
      onSubmitSuccess() {
        this.fileList=[];
        this.fileNameList = [];
        this.$refs.uploadRef.doReset();
        this.$refs.addForm.reset();
        this.$refs.uploadFilePopup.close();
        this.$refs.submitBtn.setIconStyle(1);
        this.$refs.AmsFilesInfoGrid.load();
      },

      submitUploadParam() {
        //文件上传校验
        let temp = document.getElementsByClassName('upload-demo');
        let files = temp[0].childNodes[1].childNodes;
        let count = files.length;
        if (count === 0) {
          this.$message.error("上传文件不能为空!");
          return false;
        }
        let formData = this.formData;
        let file = files[0];
        this.$set(formData,'fileName', file.innerText);
        this.$refs.uploadRef.upload(formData);
      },

    }
  };
</script>
