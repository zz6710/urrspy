<template>
  <div class="py-page">
    <div>
		<k-form-search-customize data-model-name="RmsFieldInfo" data-label-width="80px" v-model="searchParam" data-target="rmsFieldInfoGrid">
      <k-form-item label="表名称">
<!--        <k-field-select v-model="searchParam.tableName" data-action="RmsTableInfo.findRmsTableInfoDict" @data-on-change="setFieldName"-->
<!--                        data-display-field="tableName,comment" data-value-field="tableName"/>-->
        <k-field-text v-model="searchParam.tableName"/>
      </k-form-item>
      <k-form-item label="字段名称">
<!--        <k-field-select v-model="searchParam.fieldName" :data-data="fieldNames"-->
<!--                        data-display-field="fieldName,fieldComment" data-value-field="fieldName"/>-->
        <k-field-text v-model="searchParam.fieldName"/>
      </k-form-item>
      <k-form-item label="表所属层级">
        <k-field-select data-dict="table_owner" v-model="searchParam.owner"/>
      </k-form-item>
		</k-form-search-customize>
    </div>
    <div class="py-page-container">
		<div class="table-top-btns">
      <div class="left">
			<k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="handleEdit">
				<md-icon md-src="/static/svg/edit.svg" />修改字段血缘关系</k-btn>
      </div>
		</div>
      <k-grid ref="rmsFieldInfoGrid" @data-row-select="selectRow" data-action="RmsFieldInfo.findRmsFieldInfos" data-operate-width="350px">
		<k-grid-column data-header="主键" data-name="id" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="字段id" data-name="tableFieldId" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="表id" data-name="tableInfoId" data-hidden="true"></k-grid-column>
    <k-grid-column data-header="库名" data-name="databaseName"></k-grid-column>
    <k-grid-column data-header="表名" data-name="tableName"></k-grid-column>
    <k-grid-column data-header="表注释" data-name="comment"></k-grid-column>
		<k-grid-column data-header="表所属层级" data-name="owner" data-dict="table_owner"></k-grid-column>
		<k-grid-column data-header="字段名" data-name="fieldName"></k-grid-column>
		<k-grid-column data-header="数据类型" data-name="fieldDataType"></k-grid-column>
		<k-grid-column data-header="字段注释" data-name="fieldComment"></k-grid-column>
		<k-grid-column data-header="字段排序" data-name="fieldIndex" data-hidden="true"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-size="mini" @click="findStreamFieldLineage(scope.row.row, 0)">
            上游血缘关系
          </k-btn>
          <k-btn class="btn-custom-text" data-size="mini" @click="findStreamFieldLineage(scope.row.row, 1)">
            下游血缘关系
          </k-btn>
          <k-btn class="btn-custom-text" data-size="mini" @click="findStreamFieldLineage(scope.row.row, 2)">
            全血缘关系
          </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加表字段基础信息弹出框   -->
	<k-popup ref="addRmsFieldInfoPopup" data-title="新增">
    	<k-form ref="addRmsFieldInfoForm" :data-col="2">
			<k-form-item label="字段id">
	        	<k-field-text v-model="formData.tableFieldId"/>
	     	</k-form-item>
			<k-form-item label="表id">
	        	<k-field-text v-model="formData.tableInfoId"/>
	     	</k-form-item>
			<k-form-item label="字段名">
	        	<k-field-text v-model="formData.fieldName"/>
	     	</k-form-item>
			<k-form-item label="字段数据类型">
	        	<k-field-text v-model="formData.fieldDataType"/>
	     	</k-form-item>
			<k-form-item label="字段注释">
	        	<k-field-text v-model="formData.fieldComment"/>
	     	</k-form-item>
			<k-form-item label="字段排序">
	        	<k-field-text v-model="formData.fieldIndex"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="md-primary" data-functype="SUBMIT" data-action="RmsFieldInfo.addRmsFieldInfo" data-from="addRmsFieldInfoForm"
		               :data-model="formData" data-target="rmsFieldInfoGrid">
		          <i class="icon-confirm" />确定
		        </k-btn>
		        <k-btn class="md-info" data-functype="CLOSE">
		          <i class="icon-cancel" />取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改表字段基础信息弹出框   -->
	<k-popup ref="editRmsFieldInfoPopup" data-title="修改">
	  <k-form ref="editRmsFieldInfoForm" :data-col="2">
		<k-form-item label="字段id">
        	<k-field-text v-model="formData.tableFieldId"/>
     	</k-form-item>
		<k-form-item label="表id">
        	<k-field-text v-model="formData.tableInfoId"/>
     	</k-form-item>
		<k-form-item label="字段名">
        	<k-field-text v-model="formData.fieldName"/>
     	</k-form-item>
		<k-form-item label="字段数据类型">
        	<k-field-text v-model="formData.fieldDataType"/>
     	</k-form-item>
		<k-form-item label="字段注释">
        	<k-field-text v-model="formData.fieldComment"/>
     	</k-form-item>
		<k-form-item label="字段排序">
        	<k-field-text v-model="formData.fieldIndex"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="RmsFieldInfo.updateRmsFieldInfo" data-from="editRmsFieldInfoForm"
	        :data-model="formData" data-target="rmsFieldInfoGrid">
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
  import KFieldSelect from "@/components/k-element/k-field-select/k-field-select.vue";
  import KFieldText from "@/components/k-element/k-field-text/k-field-text.vue";

  export default {
    components: {KFieldText, KFieldSelect},
    data() {
      return {
        formData: {},
        selectRowData: {},
	    searchParam: {},
        fieldNames: []
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },

      // 展示血缘关系图
      findStreamFieldLineage(row, number) {
        let action = null;
        if (number === 0) {
          action = 'FieldLineage.findUpStreamFieldLineage'
        } else if (number === 1) {
          action = 'FieldLineage.findDownStreamFieldLineage'
        } else {
          action = 'FieldLineage.findFieldLineage'
        }
        this.httpUtil.comnUpdate({
          action: action,
          params: row,
          successAlert: true,
        }).then(data => {
          if (data.success) {
            this.$router.push({
              name: "血缘关系图",
              params: {data: data.returndata}
            });
          }
        });
      },
			handleEdit() {
				this.$router.push({
					path: '/main/pms/sqlFlow/fieldLineage',
				})
			},
      setFieldName(val){
        this.httpUtil.comnQuery({
          action:"RmsFieldInfo.findRmsFieldInfos",
          params:{
            tableName : val,
          }
        }).then(data=>{
          this.fieldNames = data.rows;
        })
      },
    }
  };
</script>
