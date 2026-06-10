<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="T8SqlParamInfo" data-label-width="80px" v-model="searchParam" data-target="t8SqlParamInfoGrid">
        <k-form-item label="参数名">
          <k-field-text v-model="searchParam.code"/>
        </k-form-item>
        <k-form-item label="数据类型">
          <k-field-select v-model="searchParam.dataType" data-dict="data_type"/>
        </k-form-item>
        <k-form-item label="状态">
          <k-field-select v-model="searchParam.status"  data-dict="review_status"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addT8SqlParamInfoPopup">
          <md-icon md-src="/static/svg/add.svg"/>新增</k-btn>
      </div>
      <k-grid ref="t8SqlParamInfoGrid" @data-row-select="selectRow" data-action="T8SqlParamInfo.findT8SqlParamInfos" >
		<k-grid-column data-header="主键" data-name="id" data-hidden="true"/>
		<k-grid-column data-header="参数名" data-name="code"/>
		<k-grid-column data-header="取值SQL" data-name="sqlstr"/>
		<k-grid-column data-header="数据类型" data-name="dataType" data-dict="data_type"/>
		<k-grid-column data-header="参数说明" data-name="remark"/>
		<k-grid-column data-header="状态" data-name="status" data-dict="review_status"/>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改SQL语句参数配置" data-functype="POPUP" data-size="mini"
            data-target="editT8SqlParamInfoPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="T8SqlParamInfo.deleteT8SqlParamInfo" data-size="mini"
               data-type="danger" data-target="t8SqlParamInfoGrid" :data-confirm="true" data-descript="删除SQL语句参数配置">
            删除
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加SQL语句参数配置弹出框   -->
	<k-popup ref="addT8SqlParamInfoPopup" data-title="新增">
    	<k-form ref="addT8SqlParamInfoForm" :data-col="2">
			<k-form-item label="参数名">
	        	<k-field-text v-model="formData.code" :data-allowblank="false"/>
	     	</k-form-item>
			<k-form-item label="数据类型">
	        	<k-field-select v-model="formData.dataType" data-dict="data_type"/>
	     	</k-form-item>
			<k-form-item label="参数说明">
	        	<k-field-text v-model="formData.remark"/>
	     	</k-form-item>
			<k-form-item label="状态">
	        	<k-field-select v-model="formData.status" data-dict="review_status"/>
	     	</k-form-item>
        <k-form-item label="取值SQL" :data-col="2" class="form-item-100">
          <k-field-text v-model="formData.sqlstr" :data-max-length="500" inputType="textarea" :rows="4" :data-allowblank="false"/>
        </k-form-item>
	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8SqlParamInfo.addT8SqlParamInfo" data-from="addT8SqlParamInfoForm"
		               :data-model="formData" data-target="t8SqlParamInfoGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改SQL语句参数配置弹出框   -->
	<k-popup ref="editT8SqlParamInfoPopup" data-title="修改">
	  <k-form ref="editT8SqlParamInfoForm" :data-col="2">
		<k-form-item label="主键" v-show="false">
        	<k-field-text v-model="formData.id"/>
     	</k-form-item>
		<k-form-item label="参数名">
        	<k-field-text v-model="formData.code" :data-allowblank="false"/>
     	</k-form-item>
      <k-form-item label="数据类型">
        <k-field-select v-model="formData.dataType" data-dict="data_type"/>
      </k-form-item>
		<k-form-item label="参数说明">
        	<k-field-text v-model="formData.remark"/>
     	</k-form-item>
      <k-form-item label="状态">
        <k-field-select v-model="formData.status" data-dict="review_status"/>
      </k-form-item>
      <k-form-item label="取值SQL" :data-col="2" class="form-item-100">
        <k-field-text v-model="formData.sqlstr" inputType="textarea" :data-max-length="500" :rows="4" :data-allowblank="false" />
      </k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8SqlParamInfo.updateT8SqlParamInfo" data-from="editT8SqlParamInfoForm"
	        :data-model="formData" data-target="t8SqlParamInfoGrid">
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
<style lang="scss" scoped>
.form-item-100 {
	width: 100%;
}
</style>
