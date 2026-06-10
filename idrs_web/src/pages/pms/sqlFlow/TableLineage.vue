<template>
  <div class="py-page">
    <div>
		<k-form-search-customize data-model-name="TableLineage" data-label-width="80px" v-model="searchParam" data-target="tableLineageGrid">
			<k-form-item label="目标表">
				<k-field-text v-model="searchParam.toTableInfoId"/>
			</k-form-item>
      <k-form-item label="源表">
        <k-field-text v-model="searchParam.fromTableInfoId"/>
      </k-form-item>
		</k-form-search-customize>
    </div>
    <div class="py-page-container">
		<div class="table-top-btns">
      <div class="left">
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addTableLineagePopup">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
      </div>
		</div>
      <k-grid ref="tableLineageGrid" @data-row-select="selectRow" data-action="TableLineage.findTableLineages" >
		<k-grid-column data-header="主键" data-name="id" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="exeid" data-name="exeid"></k-grid-column>
		<k-grid-column data-header="目标表id" data-name="toTableInfoId"></k-grid-column>
		<k-grid-column data-header="源表id" data-name="fromTableInfoId"></k-grid-column>
		<k-grid-column data-header="是否手工维护" data-name="manualFlag" dataWidth="190" data-dict="1yes0no"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改表血缘关系" data-functype="POPUP" data-size="mini"
            data-target="editTableLineagePopup">
			  修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="TableLineage.deleteTableLineage" data-size="mini"
               data-type="danger" data-target="tableLineageGrid" :data-confirm="true" data-descript="删除表血缘关系">
			  删除
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加表血缘关系弹出框   -->
	<k-popup ref="addTableLineagePopup" data-title="新增">
    	<k-form ref="addTableLineageForm" :data-col="2">
			<k-form-item label="主键" v-show="false">
	        	<k-field-text v-model="formData.id"/>
	     	</k-form-item>
			<k-form-item label="exeid">
	        	<k-field-text v-model="formData.exeid"/>
	     	</k-form-item>
			<k-form-item label="目标表id">
	        	<k-field-text v-model="formData.toTableInfoId"/>
	     	</k-form-item>
			<k-form-item label="源表id">
	        	<k-field-text v-model="formData.fromTableInfoId"/>
	     	</k-form-item>
			<k-form-item label="是否手工维护">
	        	<k-field-select v-model="formData.manualFlag" data-dict="1yes0no"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="TableLineage.addTableLineage" data-from="addTableLineageForm"
		               :data-model="formData" data-target="tableLineageGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改表血缘关系弹出框   -->
	<k-popup ref="editTableLineagePopup" data-title="修改">
	  <k-form ref="editTableLineageForm" :data-col="2">
		<k-form-item label="id">
        	<k-field-text v-model="formData.id" :data-disabled="true"/>
     	</k-form-item>
		<k-form-item label="exeid">
        	<k-field-text v-model="formData.exeid"/>
     	</k-form-item>
		<k-form-item label="目标表id">
        	<k-field-text v-model="formData.toTableInfoId"/>
     	</k-form-item>
		<k-form-item label="源表id">
        	<k-field-text v-model="formData.fromTableInfoId"/>
     	</k-form-item>
		<k-form-item label="是否手工维护">
        	<k-field-select v-model="formData.manualFlag" data-dict="1yes0no"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="TableLineage.updateTableLineage" data-from="editTableLineageForm"
	        :data-model="formData" data-target="tableLineageGrid">
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
