<template>
  <div class="py-page">
    <div>
		<k-form-search-customize data-model-name="${model}" data-label-width="80px" v-model="searchParam" data-target="${lowHeadModel}Grid">
			<k-form-item label="数据日期">
                <k-field-date v-model="searchParam.dataDate" data-type="date" data-date-format="yyyy-MM-dd"
                              data-value-format="yyyyMMdd"/>
            </k-form-item>
		</k-form-search-customize>
    </div>
    <div class="py-page-container">
		<div class="table-top-btns">
			<k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="add${model}Popup">
				<md-icon md-src="/static/svg/add.svg" />新增</k-btn>
		</div>
      <k-grid ref="${lowHeadModel}Grid" @data-row-select="selectRow" data-action="${model}.find${model}s" >
      	<#list dbTable.tableFields as field>
		<k-grid-column data-header="${field.comment}" data-name="${field.field}"></k-grid-column>
	  	</#list>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改${dbTable.comment}" data-functype="POPUP" data-size="mini"
            data-target="edit${model}Popup">
			  修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="${model}.delete${model}" data-size="mini"
               data-type="danger" data-target="${lowHeadModel}Grid" :data-confirm="true" data-descript="删除${dbTable.comment}">
			  删除
    	  </k-btn>
        </template>
      </k-grid>
    </div>
    
	<!--    添加${dbTable.comment}弹出框   -->
	<k-popup ref="add${model}Popup" data-title="添加">
    	<k-form ref="add${model}Form" :data-col="2">
			<#list dbTable.tableFields as field>
			<k-form-item label="${field.comment}">
	        	<k-field-text v-model="formData.${field.field}"/>
	     	</k-form-item>
		  	</#list>
	  	
	      	<k-form-footer data-align="center">
              <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="${model}.add${model}" data-from="add${model}Form"
                     :data-model="formData" data-target="${lowHeadModel}Grid">
                <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
              </k-btn>
              <k-btn class="btn-custom-plain" data-functype="CLOSE">
                <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
            </k-form-footer>
    	</k-form>
	</k-popup>
    
	<!--    修改${dbTable.comment}弹出框   -->
	<k-popup ref="edit${model}Popup" data-title="编辑">
	  <k-form ref="edit${model}Form" :data-col="2">
	    <#list dbTable.tableFields as field>
		<k-form-item label="${field.comment}">
        	<k-field-text v-model="formData.${field.field}"/>
     	</k-form-item>
	  	</#list>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="${model}.update${model}" data-from="edit${model}Form"
	        :data-model="formData" data-target="${lowHeadModel}Grid">
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
      }
    }
  };
</script>
