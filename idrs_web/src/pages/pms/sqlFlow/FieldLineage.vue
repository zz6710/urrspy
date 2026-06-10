<template>
  <div class="py-page">
    <div>
		<k-form-search-customize data-model-name="FieldLineage" data-label-width="80px" v-model="searchParam" data-target="fieldLineageGrid">
			<k-form-item label="目标表">
				<k-field-text v-model="searchParam.toTableInfoId"/>
			</k-form-item>
      <k-form-item label="目标字段">
        <k-field-text v-model="searchParam.toTableFieldId"/>
      </k-form-item>
      <k-form-item label="源表">
        <k-field-text v-model="searchParam.fromTableInfoId"/>
      </k-form-item>
      <k-form-item label="源字段">
        <k-field-text v-model="searchParam.fromTableFieldId"/>
      </k-form-item>
		</k-form-search-customize>
    </div>
    <div class="py-page-container">
		<div class="table-top-btns">
      <div class="left">
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addFieldLineagePopup">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
      </div>
		</div>
      <k-grid ref="fieldLineageGrid" @data-row-select="selectRow" data-action="FieldLineage.findFieldLineages" >
		<k-grid-column data-header="目标表id" data-name="toTableInfoId"></k-grid-column>
		<k-grid-column data-header="目标字段id" data-name="toTableFieldId"></k-grid-column>
		<k-grid-column data-header="源表id" data-name="fromTableInfoId"></k-grid-column>
		<k-grid-column data-header="源字段id" data-name="fromTableFieldId"></k-grid-column>
		<k-grid-column data-header="全依赖关系" data-name="allDependency"></k-grid-column>
    <k-grid-column data-header="全派生关系" data-name="allSuperior"></k-grid-column>
		<k-grid-column data-header="是否手工维护" data-name="manualFlag" dataWidth="190" data-dict="1yes0no"></k-grid-column>
		<k-grid-column data-header="生成顺序" data-name="sequence"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改表字段血缘关系" data-functype="POPUP" data-size="mini"
            data-target="editFieldLineagePopup">
			  修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="FieldLineage.deleteFieldLineage" data-size="mini"
               data-type="danger" data-target="fieldLineageGrid" :data-confirm="true" data-descript="删除表字段血缘关系">
			  删除
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加表字段血缘关系弹出框   -->
	<k-popup ref="addFieldLineagePopup" data-title="新增">
    	<k-form ref="addFieldLineageForm" :data-col="2">
			<k-form-item label="目标表id">
	        	<k-field-text v-model="formData.toTableInfoId"/>
	     	</k-form-item>
			<k-form-item label="目标字段id">
	        	<k-field-text v-model="formData.toTableFieldId"/>
	     	</k-form-item>
			<k-form-item label="源表id">
	        	<k-field-text v-model="formData.fromTableInfoId"/>
	     	</k-form-item>
			<k-form-item label="源字段id">
	        	<k-field-text v-model="formData.fromTableFieldId"/>
	     	</k-form-item>
			<k-form-item label="全依赖关系">
	        	<k-field-text v-model="formData.allDependency"/>
	     	</k-form-item>
        <k-form-item label="全派生关系">
          <k-field-text v-model="formData.AllSuperior"/>
        </k-form-item>
			<k-form-item label="是否手工维护">
	        	<k-field-select v-model="formData.manualFlag" data-dict="1yes0no"/>
	     	</k-form-item>
			<k-form-item label="生成顺序">
	        	<k-field-text v-model="formData.sequence"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="FieldLineage.addFieldLineage" data-from="addFieldLineageForm"
		               :data-model="formData" data-target="fieldLineageGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改表字段血缘关系弹出框   -->
	<k-popup ref="editFieldLineagePopup" data-title="修改">
	  <k-form ref="editFieldLineageForm" :data-col="2">
		<k-form-item label="目标表id">
        	<k-field-text v-model="formData.toTableInfoId"/>
     	</k-form-item>
		<k-form-item label="目标字段id">
        	<k-field-text v-model="formData.toTableFieldId"/>
     	</k-form-item>
		<k-form-item label="源表id">
        	<k-field-text v-model="formData.fromTableInfoId"/>
     	</k-form-item>
		<k-form-item label="源字段id">
        	<k-field-text v-model="formData.fromTableFieldId"/>
     	</k-form-item>
		<k-form-item label="全依赖关系">
        	<k-field-text v-model="formData.allDependency"/>
     	</k-form-item>
    <k-form-item label="全派生关系">
        <k-field-text v-model="formData.allSuperior"/>
      </k-form-item>
		<k-form-item label="是否手工维护">
        	<k-field-select v-model="formData.manualFlag" data-dict="1yes0no"/>
     	</k-form-item>
		<k-form-item label="生成顺序">
        	<k-field-text v-model="formData.sequence"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="FieldLineage.updateFieldLineage" data-from="editFieldLineageForm"
	        :data-model="formData" data-target="fieldLineageGrid">
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
