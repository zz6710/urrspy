<template>
  <div class="py-page">
    <div>
		<k-form-search-customize data-model-name="AppAssetUnincorporatedEntity" data-label-width="80px" v-model="searchParam" data-target="appAssetUnincorporatedEntityGrid">
			<k-form-item label="报送日期">
                <k-field-date v-model="searchParam.reportDate" data-type="date" data-date-format="yyyy-MM-dd"
                              data-value-format="yyyyMMdd"/>
            </k-form-item>
			<k-form-item label="所属期">
                <k-field-date v-model="searchParam.dtDt" data-type="date" data-date-format="yyyy-MM-dd"
                              data-value-format="yyyy-MM-dd"/>
            </k-form-item>
			<k-form-item label="产品全称">
                <k-field-text v-model="searchParam.prdcNm"></k-field-text>
            </k-form-item>
		</k-form-search-customize>
    </div>
    <div class="py-page-container">
		<div class="table-top-btns">
			<div class="left">
				<k-btn slot="button" ref="downloadRef" class="btn-custom-plain" :data-handler="downloadFile">
					<md-icon>cloud_download</md-icon>导出
					</k-btn>
				<k-btn slot="button" ref="uploadBtnRef" data-functype="POPUP" class="btn-custom-plain" :load-disabled="false"
					data-target="uploadAssetUnincorporatedEntityPopup">
					<md-icon>cloud_upload</md-icon>导入
				</k-btn>
				<k-btn slot="button" ref="reloadBtnRef" class="btn-custom-plain" data-functype="POPUP" data-target="handleReportPopup">
					<md-icon md-src="/static/svg/icon/reset.svg"></md-icon>生成报表数据
				</k-btn>
			</div>
		</div>
      <k-grid ref="appAssetUnincorporatedEntityGrid" @data-row-select="selectRow" data-operate-column="false" :data-autoload="false"
	    data-action="AppAssetUnincorporatedEntity.findAppAssetUnincorporatedEntitys" >
		<k-grid-column data-header="报送日期" data-name="reportDate"></k-grid-column>
		<k-grid-column data-header="所属期" data-name="dtDt"></k-grid-column>
		<k-grid-column data-header="产品全称" data-name="prdcNm"></k-grid-column>
		<k-grid-column data-header="产品21位码" data-name="prdcCd"></k-grid-column>
		<k-grid-column data-header="产品类型" data-name="prdcType"></k-grid-column>
		<k-grid-column data-header="产品性质" data-name="prdcClass"></k-grid-column>
		<k-grid-column data-header="是否特殊类型基金" data-name="isSpecialFund"></k-grid-column>
		<k-grid-column data-header="净资产（亿元）" data-name="asset"></k-grid-column>
        <!-- <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改非法人财务数据" data-functype="POPUP" data-size="mini"
            data-target="editAppAssetUnincorporatedEntityPopup">
			  修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="AppAssetUnincorporatedEntity.deleteAppAssetUnincorporatedEntity" data-size="mini"
               data-type="danger" data-target="appAssetUnincorporatedEntityGrid" :data-confirm="true" data-descript="删除非法人财务数据">
			  删除
    	  </k-btn>
        </template> -->
      </k-grid>
    </div>

	<!--    添加非法人财务数据弹出框   -->
	<k-popup ref="addAppAssetUnincorporatedEntityPopup" data-title="添加">
    	<k-form ref="addAppAssetUnincorporatedEntityForm" :data-col="2">
			<k-form-item label="报送日期">
	        	<k-field-text v-model="formData.reportDate"/>
	     	</k-form-item>
			<k-form-item label="所属期">
	        	<k-field-text v-model="formData.dtDt"/>
	     	</k-form-item>
			<k-form-item label="产品全称">
	        	<k-field-text v-model="formData.prdcNm"/>
	     	</k-form-item>
			<k-form-item label="产品21位码">
	        	<k-field-text v-model="formData.prdcCd"/>
	     	</k-form-item>
			<k-form-item label="产品类型">
	        	<k-field-text v-model="formData.prdcType"/>
	     	</k-form-item>
			<k-form-item label="产品性质">
	        	<k-field-text v-model="formData.prdcClass"/>
	     	</k-form-item>
			<k-form-item label="是否特殊类型基金">
	        	<k-field-text v-model="formData.isSpecialFund"/>
	     	</k-form-item>
			<k-form-item label="净资产（亿元）">
	        	<k-field-text v-model="formData.asset"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
              <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AppAssetUnincorporatedEntity.addAppAssetUnincorporatedEntity" data-from="addAppAssetUnincorporatedEntityForm"
                     :data-model="formData" data-target="appAssetUnincorporatedEntityGrid">
                <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
              </k-btn>
              <k-btn class="btn-custom-plain" data-functype="CLOSE">
                <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
            </k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改非法人财务数据弹出框   -->
	<k-popup ref="editAppAssetUnincorporatedEntityPopup" data-title="编辑">
	  <k-form ref="editAppAssetUnincorporatedEntityForm" :data-col="2">
		<k-form-item label="报送日期">
        	<k-field-text v-model="formData.reportDate"/>
     	</k-form-item>
		<k-form-item label="所属期">
        	<k-field-text v-model="formData.dtDt"/>
     	</k-form-item>
		<k-form-item label="产品全称">
        	<k-field-text v-model="formData.prdcNm"/>
     	</k-form-item>
		<k-form-item label="产品21位码">
        	<k-field-text v-model="formData.prdcCd"/>
     	</k-form-item>
		<k-form-item label="产品类型">
        	<k-field-text v-model="formData.prdcType"/>
     	</k-form-item>
		<k-form-item label="产品性质">
        	<k-field-text v-model="formData.prdcClass"/>
     	</k-form-item>
		<k-form-item label="是否特殊类型基金">
        	<k-field-text v-model="formData.isSpecialFund"/>
     	</k-form-item>
		<k-form-item label="净资产（亿元）">
        	<k-field-text v-model="formData.asset"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AppAssetUnincorporatedEntity.updateAppAssetUnincorporatedEntity" data-from="editAppAssetUnincorporatedEntityForm"
	        :data-model="formData" data-target="appAssetUnincorporatedEntityGrid">
			  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
	      </k-btn>
	      <k-btn class="btn-custom-plain" data-functype="CLOSE">
			  <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	    </k-form-footer>
	  </k-form>
	</k-popup>

    <k-popup ref="uploadAssetUnincorporatedEntityPopup" data-title="导入非法人财务数据">
		<k-form ref="addForm" data-ui="element">
			<k-form-item label="附件" data-ui="element" data-input-width="500px">
			<k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
				data-accept=".xlsx,.xls"
				:data-error="onSubmitError" :data-success="onSubmitSuccess"
				:data-auto-upload="false"
				data-upload-url="upload/server/RptApp/reportManage/assetUnincorporatedEntityImport.json">
			</k-field-excel-upload>
			</k-form-item>
			<k-form-footer data-align="center">
			<k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="appAssetUnincorporatedEntityGrid" ref="submitBtn"
					:data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">
				<i class="icon-confirm"/>确定
			</k-btn>
			<k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
			</k-form-footer>
		</k-form>
	</k-popup>

	<k-popup ref="handleReportPopup" data-title="生成报表数据">
        <k-form ref="handleReportAppForm" data-ui="element">
          <k-form-item label="所属期" data-ui="element" data-input-width="500px">
            <k-field-date v-model="formData.dtDt" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyy-MM-dd" :data-allowblank="false"/>
          </k-form-item>
          <k-form-footer data-align="center">
           <k-btn ref="confirmBtnRef" class="btn-custom-primary" data-from="editForm" :data-handler="handleReport">
				<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
                <md-icon md-src="/static/svg/cancel.svg" ></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
    </k-popup>

  </div>
</template>

<script>
  import Tools from "@/utils/tools";
  export default {
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam: {
			reportDate: Tools.getCurrentDate(""),
		}
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
	  downloadFile() {
		this.$refs.downloadRef.setIconStyle(0);
		this.$refs.downloadRef.setLoading(true);
		this.httpUtil.download({
			url: "/download/server/RptApp/JmReport/exportTemplate/downloadUnincorporatedEntity.json",
			params: {
				reportDate: this.searchParam.reportDate ? this.searchParam.reportDate : '',
				dtDt: this.searchParam.dtDt ? this.searchParam.dtDt : '',
				prdcNm: this.searchParam.prdcNm ? this.searchParam.prdcNm : '',
			},
			callback: () => {
				this.$refs.downloadRef.setIconStyle(1);
				this.$refs.downloadRef.setLoading(false);
			},
		});
	  },
	  onSubmitError() {
		 this.$refs.uploadBtnRef.setIconStyle(1);
	  },
	  onSubmitSuccess() {
		 this.$refs.uploadBtnRef.setIconStyle(1);
		 this.$refs.appAssetUnincorporatedEntityGrid.load(this.searchParam);
	  },
      submitUploadParam() {
		//文件上传校验
        let validate = this.$refs.addForm.validate();
        if (validate) {
          let formData = {};
          let temp = document.getElementsByClassName('upload-demo');
          let lis = temp[0].childNodes[1].childNodes.length;
          if (lis > 0) {
            this.$refs.uploadRef.upload(formData);
            this.$refs.uploadBtnRef.setIconStyle(0);
            setTimeout(() => {
				this.$refs.uploadAssetUnincorporatedEntityPopup.close();
			}, 300);
          } else {
            this.$message.error("上传文件不能为空!");
            return false;
          }
        }
      },
	  handleReport() {
		if (this.$refs.handleReportAppForm.validate()) {
			this.$refs.confirmBtnRef.setIconStyle(0);
		    this.httpUtil
				.comnUpdate({
					action: "AppAssetUnincorporatedEntity.reloadData",
					params: {
						dtDt: this.formData.dtDt
					},
					successAlert: true,
				})
				.then((data) => {
					this.$refs.appAssetUnincorporatedEntityGrid.load(this.searchParam);
				});
			setTimeout(() => {
				this.$refs.confirmBtnRef.setIconStyle(1);
				this.$refs.handleReportPopup.close();
			}, 300);	
		}
	  },
    }
  };
</script>
