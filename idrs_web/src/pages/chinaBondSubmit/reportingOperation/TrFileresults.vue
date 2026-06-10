<template>
  <div class="py-page">
    <div>
      <k-form-search-customize  v-model="queryParam" data-target="trFileresultsGrid">
        <k-form-item label="登记日期">
          <k-field-date v-model="queryParam.registerDate"></k-field-date>
        </k-form-item>
		  <k-form-item label="报表名称">
          <k-field-select v-model="queryParam.sysTableName"  data-dict = "t8_zzzl_table"
		   data-validate-type="text"/>
        </k-form-item>
		<k-form-item label="登记流水号">
          <k-field-text v-model="queryParam.registerSerno" data-validate-type="text"/>
      	</k-form-item>
		<k-form-item label="文件名称">
          <k-field-text v-model="queryParam.filename" data-validate-type="text"/>
      	</k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <k-grid ref="trFileresultsGrid" @data-row-select="selectRow" data-action="TrFileresults.findTrFileresultss"  :data-operate-column="false" >
        <k-grid-column data-header="登记日期" data-name="registerDate" data-width="100" data-type="date" data-date-format="yyyy-MM-dd"></k-grid-column>
        <k-grid-column data-header="报表名称" data-name="sysTableName"  data-width="180" data-dict = "t8_zzzl_table" ></k-grid-column>
        <k-grid-column data-header="文件类型" data-name="fileType" data-width="100" ></k-grid-column>
        <k-grid-column data-header="文件名称" data-name="filename"  data-width="180" ></k-grid-column>
        <k-grid-column data-header="登记流水号" data-name="registerSerno" data-width="180" ></k-grid-column>
        <k-grid-column data-header="数据序号" data-name="fileno" data-width="100" ></k-grid-column>
        <k-grid-column data-header="错误信息" data-name="errormsg" ></k-grid-column>
        <k-grid-column data-header="入库时间" data-name="crtTime" ></k-grid-column>
      </k-grid>
    </div>

	<!--    添加中债弹出框   -->
	<k-popup ref="addTrFileresultsPopup" data-title="新增">
    	<k-form ref="addTrFileresultsForm" :data-col="2">
			<k-form-item label="登记日期">
	        	<k-field-text v-model="formData.registerDate"/>
	     	</k-form-item>
			<k-form-item label="文件类型">
	        	<k-field-text v-model="formData.fileType"/>
	     	</k-form-item>
			<k-form-item label="文件名称">
	        	<k-field-text v-model="formData.filename"/>
	     	</k-form-item>
			<k-form-item label="序号">
	        	<k-field-text v-model="formData.fileno"/>
	     	</k-form-item>
			<k-form-item label="登记流水号">
	        	<k-field-text v-model="formData.registerSerno"/>
	     	</k-form-item>
			<k-form-item label="错误信息">
	        	<k-field-text v-model="formData.errormsg"/>
	     	</k-form-item>
			<k-form-item label="错误码">
	        	<k-field-text v-model="formData.errorcode"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="TrFileresults.addTrFileresults" data-from="addTrFileresultsForm"
		               :data-model="formData" data-target="trFileresultsGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改中债弹出框   -->
	<k-popup ref="editTrFileresultsPopup" data-title="修改">
	  <k-form ref="editTrFileresultsForm" :data-col="2">
		<k-form-item label="登记日期">
        	<k-field-text v-model="formData.registerDate"/>
     	</k-form-item>
		<k-form-item label="文件类型">
        	<k-field-text v-model="formData.fileType"/>
     	</k-form-item>
		<k-form-item label="文件名称">
        	<k-field-text v-model="formData.filename"/>
     	</k-form-item>
		<k-form-item label="序号">
        	<k-field-text v-model="formData.fileno"/>
     	</k-form-item>
		<k-form-item label="登记流水号">
        	<k-field-text v-model="formData.registerSerno"/>
     	</k-form-item>
		<k-form-item label="错误信息">
        	<k-field-text v-model="formData.errormsg"/>
     	</k-form-item>
		<k-form-item label="错误码">
        	<k-field-text v-model="formData.errorcode"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="TrFileresults.updateTrFileresults" data-from="editTrFileresultsForm"
	        :data-model="formData" data-target="trFileresultsGrid">
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
    name: "TrFileresults",
    data() {
      return {
        formData: {},
        queryParam:{},
        selectRowData: {}
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      }
    }
  };
</script>
