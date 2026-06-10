<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="ProdRegistRelationInfo" data-target="prodRegistRelationInfoGrid" v-model="queryParam">
        <k-form-item label="产品代码">
          <k-field-text v-model="queryParam.prodCode"/>
        </k-form-item>
        <k-form-item label="产品登记编码">
          <k-field-text v-model="queryParam.regCode"/>
        </k-form-item>
        <k-btn slot="button" style="width: 120px" data-functype="POPUP" class="btn-custom-primary" v-if="false"
               data-target="addPopup">
          <md-icon>library_books</md-icon>
          产品登记编码同步
        </k-btn>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <k-grid ref="prodRegistRelationInfoGrid" @data-row-select="selectRow"  data-operate-column="false" data-action="ProdRegistRelationInfo.findProdRegistRelationInfos" >
		    <k-grid-column data-header="ID" data-name="id" data-hidden="true"></k-grid-column>
		    <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
		    <k-grid-column data-header="理财登记编码" data-name="regCode"></k-grid-column>
		    <k-grid-column data-header="文件名称" data-name="fileName"></k-grid-column>
		    <k-grid-column data-header="入库时间" data-name="crtTime"></k-grid-column>
      </k-grid>
    </div>

	<!--    添加产品登记编码弹出框   -->
	<k-popup ref="addProdRegistRelationInfoPopup" data-title="新增">
    	<k-form ref="addProdRegistRelationInfoForm" :data-col="2" isFormBodyScreen>
			<k-form-item label="产品代码">
	        	<k-field-text v-model="formData.prodCode"/>
	     	</k-form-item>
			<k-form-item label="理财登记编码">
	        	<k-field-text v-model="formData.regCode"/>
	     	</k-form-item>
			<k-form-item label="日期">
	        	<k-field-text v-model="formData.workdate"/>
	     	</k-form-item>
			<k-form-item label="备注">
	        	<k-field-text v-model="formData.remark"/>
	     	</k-form-item>

	      	<k-form-footer slot="footer" data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ProdRegistRelationInfo.addProdRegistRelationInfo" data-from="addProdRegistRelationInfoForm"
		               :data-model="formData" data-target="prodRegistRelationInfoGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改产品登记编码弹出框   -->
	<k-popup ref="editProdRegistRelationInfoPopup" data-title="修改">
	  <k-form ref="editProdRegistRelationInfoForm" :data-col="2" isFormBodyScreen>
		<k-form-item label="产品代码">
        	<k-field-text v-model="formData.prodCode"/>
     	</k-form-item>
		<k-form-item label="理财登记编码">
        	<k-field-text v-model="formData.regCode"/>
     	</k-form-item>
		<k-form-item label="日期">
        	<k-field-text v-model="formData.workdate"/>
     	</k-form-item>
		<k-form-item label="备注">
        	<k-field-text v-model="formData.remark"/>
     	</k-form-item>
	    <k-form-footer slot="footer" data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ProdRegistRelationInfo.updateProdRegistRelationInfo" data-from="editProdRegistRelationInfoForm"
	        :data-model="formData" data-target="prodRegistRelationInfoGrid">
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
    name:"ProdRegistRelationInfo",
    data() {
      return {
        formData: {},
        selectRowData: {},
        queryParam:{}
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
