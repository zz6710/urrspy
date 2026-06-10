<template>
  <div class="py-page">
    <div>
		<k-form-search-customize data-model-name="DwsDailyPrdDtl" v-model="searchParam" data-target="dwsDailyPrdDtlGrid">
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
		</k-form-search-customize>
    </div>
    <div class="py-page-container">
        <div class="left">
<!--          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addDwsDailyPrdDtlPopup">-->
<!--            <md-icon md-src="/static/svg/add.svg" />新增-->
<!--          </k-btn>-->
<!--          <k-btn slot="button" data-functype="POPUP" class="btn-custom-plain" data-target="uploadDwsDailyPrdDtlPopup">-->
<!--            <md-icon>cloud_upload</md-icon>导入-->
<!--          </k-btn>-->
          <k-btn slot="button" class="btn-custom-plain" data-functype="EXPORT" data-export-dict="true" data-target="dwsDailyPrdDtlGrid" data-export-name="产品明细日中间表">
            <md-icon>cloud_download</md-icon>导出
          </k-btn>
        </div>
      <k-grid ref="dwsDailyPrdDtlGrid" @data-row-select="selectRow" data-action="DwsDailyPrdDtl.findDwsDailyPrdDtls" data-operate-column="false" data-autoload="false">
		<k-grid-column data-header="ID" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
		<k-grid-column data-header="数据日期" data-name="dealDate"></k-grid-column>
		<k-grid-column data-header="产品代码" data-name="prdcCd"></k-grid-column>
		<k-grid-column data-header="产品名称" data-name="prdcNm"></k-grid-column>
		<k-grid-column data-header="母产品代码" data-name="motherPrdcCd"></k-grid-column>
		<k-grid-column data-header="认购金额" data-name="sspAmt"></k-grid-column>
		<k-grid-column data-header="申购金额" data-name="ssbAmt"></k-grid-column>
		<k-grid-column data-header="赎回金额" data-name="rdmAmt"></k-grid-column>
		<k-grid-column data-header="到期金额" data-name="expAmt"></k-grid-column>
		<k-grid-column data-header="强减金额" data-name="sbtAmtF"></k-grid-column>
		<k-grid-column data-header="当日客户端收益总额" data-name="invYldAmtDly"></k-grid-column>
		<k-grid-column data-header="赎回收益" data-name="rdmYldAmt"></k-grid-column>
		<k-grid-column data-header="到期收益" data-name="expYldAmt"></k-grid-column>
		<k-grid-column data-header="份额强减收益" data-name="shrSbtYldAmtF"></k-grid-column>
		<k-grid-column data-header="现金分红" data-name="cshDvd"></k-grid-column>
		<k-grid-column data-header="红利转投份额" data-name="shrRvtDvd"></k-grid-column>
		<k-grid-column data-header="创建日期" data-name="crtDt" data-export="false"></k-grid-column>
		<k-grid-column data-header="创建时间" data-name="crtTm" data-export="false"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改产品明细日中间表" data-functype="POPUP" data-size="mini"
            data-target="editDwsDailyPrdDtlPopup">
			  修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="DwsDailyPrdDtl.deleteDwsDailyPrdDtl" data-size="mini"
               data-type="danger" data-target="dwsDailyPrdDtlGrid" :data-confirm="true" data-descript="删除产品明细日中间表">
			  删除
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加产品明细日中间表弹出框   -->
	<k-popup ref="addDwsDailyPrdDtlPopup" data-title="新增">
    	<k-form ref="addDwsDailyPrdDtlForm" :data-col="2">
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
			<k-form-item label="认购金额">
	        	<k-field-text v-model="formData.sspAmt"/>
	     	</k-form-item>
			<k-form-item label="申购金额">
	        	<k-field-text v-model="formData.ssbAmt"/>
	     	</k-form-item>
			<k-form-item label="赎回金额">
	        	<k-field-text v-model="formData.rdmAmt"/>
	     	</k-form-item>
			<k-form-item label="到期金额">
	        	<k-field-text v-model="formData.expAmt"/>
	     	</k-form-item>
			<k-form-item label="强减金额">
	        	<k-field-text v-model="formData.sbtAmtF"/>
	     	</k-form-item>
			<k-form-item label="当日客户端收益总额">
	        	<k-field-text v-model="formData.invYldAmtDly"/>
	     	</k-form-item>
			<k-form-item label="赎回收益">
	        	<k-field-text v-model="formData.rdmYldAmt"/>
	     	</k-form-item>
			<k-form-item label="到期收益">
	        	<k-field-text v-model="formData.expYldAmt"/>
	     	</k-form-item>
			<k-form-item label="份额强减收益">
	        	<k-field-text v-model="formData.shrSbtYldAmtF"/>
	     	</k-form-item>
			<k-form-item label="现金分红">
	        	<k-field-text v-model="formData.cshDvd"/>
	     	</k-form-item>
			<k-form-item label="红利转投份额">
	        	<k-field-text v-model="formData.shrRvtDvd"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
              <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DwsDailyPrdDtl.addDwsDailyPrdDtl" data-from="addDwsDailyPrdDtlForm"
                     :data-model="formData">
                <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
              </k-btn>
              <k-btn class="btn-custom-plain" data-functype="CLOSE">
                <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
            </k-form-footer>
    	</k-form>
	</k-popup>

	<k-popup ref="uploadDwsDailyPrdDtlPopup" data-title="导入">
        <k-form ref="addForm" data-ui="element">

          <k-form-item label="附件" data-ui="element" data-input-width="500px">
            <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                                  data-accept=".xlsx,.xls" :data-error="onSubmitError" :data-success="onSubmitSuccess"
                                  :data-auto-upload="false"
                                  data-upload-url="upload/server/RptApp/uploadDwsDailyPrdDtl.json">
            </k-field-excel-upload>
          </k-form-item>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="dwsDailyPrdDtlGrid" ref="submitBtn"
                   :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>

	<!--    修改产品明细日中间表弹出框   -->
	<k-popup ref="editDwsDailyPrdDtlPopup" data-title="修改">
	  <k-form ref="editDwsDailyPrdDtlForm" :data-col="2">
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
		<k-form-item label="认购金额">
        	<k-field-text v-model="formData.sspAmt"/>
     	</k-form-item>
		<k-form-item label="申购金额">
        	<k-field-text v-model="formData.ssbAmt"/>
     	</k-form-item>
		<k-form-item label="赎回金额">
        	<k-field-text v-model="formData.rdmAmt"/>
     	</k-form-item>
		<k-form-item label="到期金额">
        	<k-field-text v-model="formData.expAmt"/>
     	</k-form-item>
		<k-form-item label="强减金额">
        	<k-field-text v-model="formData.sbtAmtF"/>
     	</k-form-item>
		<k-form-item label="当日客户端收益总额">
        	<k-field-text v-model="formData.invYldAmtDly"/>
     	</k-form-item>
		<k-form-item label="赎回收益">
        	<k-field-text v-model="formData.rdmYldAmt"/>
     	</k-form-item>
		<k-form-item label="到期收益">
        	<k-field-text v-model="formData.expYldAmt"/>
     	</k-form-item>
		<k-form-item label="份额强减收益">
        	<k-field-text v-model="formData.shrSbtYldAmtF"/>
     	</k-form-item>
		<k-form-item label="现金分红">
        	<k-field-text v-model="formData.cshDvd"/>
     	</k-form-item>
		<k-form-item label="红利转投份额">
        	<k-field-text v-model="formData.shrRvtDvd"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DwsDailyPrdDtl.updateDwsDailyPrdDtl" data-from="editDwsDailyPrdDtlForm"
	        :data-model="formData" data-target="dwsDailyPrdDtlGrid">
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
          this.$refs.uploadDwsDailyPrdDtlPopup.close();
          this.$refs.dwsDailyPrdDtlGrid.load(this.searchParam);
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
