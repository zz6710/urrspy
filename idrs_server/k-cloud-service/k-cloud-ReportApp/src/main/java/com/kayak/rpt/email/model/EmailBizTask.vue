<template>
  <div class="py-page">
    <div>
		<k-form-search-customize data-model-name="EmailBizTask" data-label-width="80px" v-model="searchParam" data-target="emailBizTaskGrid">
			<k-form-item label="数据日期">
                <k-field-date v-model="searchParam.dataDate" data-type="date" data-date-format="yyyy-MM-dd"
                              data-value-format="yyyyMMdd"/>
            </k-form-item>
		</k-form-search-customize>
    </div>
    <div class="py-page-container">
		<div class="table-top-btns">
			<k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addEmailBizTaskPopup">
				<md-icon md-src="/static/svg/add.svg" />新增</k-btn>
		</div>
      <k-grid ref="emailBizTaskGrid" @data-row-select="selectRow" data-action="EmailBizTask.findEmailBizTasks" >
		<k-grid-column data-header="id" data-name="id"></k-grid-column>
		<k-grid-column data-header="业务类型" data-name="bizType"></k-grid-column>
		<k-grid-column data-header="业务名称" data-name="bizName"></k-grid-column>
		<k-grid-column data-header="业务表" data-name="bizTable"></k-grid-column>
		<k-grid-column data-header="业务表取数方法" data-name="bizTableMethod"></k-grid-column>
		<k-grid-column data-header="业务状态" data-name="bizStatus"></k-grid-column>
		<k-grid-column data-header="是否立即执行任务 0否 1是" data-name="taskFlag"></k-grid-column>
		<k-grid-column data-header="记录启用状态，0-未启用，1-已启用" data-name="status"></k-grid-column>
		<k-grid-column data-header="创建时间HHmmss" data-name="createTime"></k-grid-column>
		<k-grid-column data-header="创建日期yyyyMMdd" data-name="createDate"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改业务任务发邮件表" data-functype="POPUP" data-size="mini"
            data-target="editEmailBizTaskPopup">
			  修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="EmailBizTask.deleteEmailBizTask" data-size="mini"
               data-type="danger" data-target="emailBizTaskGrid" :data-confirm="true" data-descript="删除业务任务发邮件表">
			  删除
    	  </k-btn>
        </template>
      </k-grid>
    </div>
    
	<!--    添加业务任务发邮件表弹出框   -->
	<k-popup ref="addEmailBizTaskPopup" data-title="添加">
    	<k-form ref="addEmailBizTaskForm" :data-col="2">
			<k-form-item label="id">
	        	<k-field-text v-model="formData.id"/>
	     	</k-form-item>
			<k-form-item label="业务类型">
	        	<k-field-text v-model="formData.bizType"/>
	     	</k-form-item>
			<k-form-item label="业务名称">
	        	<k-field-text v-model="formData.bizName"/>
	     	</k-form-item>
			<k-form-item label="业务表">
	        	<k-field-text v-model="formData.bizTable"/>
	     	</k-form-item>
			<k-form-item label="业务表取数方法">
	        	<k-field-text v-model="formData.bizTableMethod"/>
	     	</k-form-item>
			<k-form-item label="业务状态">
	        	<k-field-text v-model="formData.bizStatus"/>
	     	</k-form-item>
			<k-form-item label="是否立即执行任务 0否 1是">
	        	<k-field-text v-model="formData.taskFlag"/>
	     	</k-form-item>
			<k-form-item label="记录启用状态，0-未启用，1-已启用">
	        	<k-field-text v-model="formData.status"/>
	     	</k-form-item>
			<k-form-item label="创建时间HHmmss">
	        	<k-field-text v-model="formData.createTime"/>
	     	</k-form-item>
			<k-form-item label="创建日期yyyyMMdd">
	        	<k-field-text v-model="formData.createDate"/>
	     	</k-form-item>
	  	
	      	<k-form-footer data-align="center">
              <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="EmailBizTask.addEmailBizTask" data-from="addEmailBizTaskForm"
                     :data-model="formData" data-target="emailBizTaskGrid">
                <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
              </k-btn>
              <k-btn class="btn-custom-plain" data-functype="CLOSE">
                <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
            </k-form-footer>
    	</k-form>
	</k-popup>
    
	<!--    修改业务任务发邮件表弹出框   -->
	<k-popup ref="editEmailBizTaskPopup" data-title="编辑">
	  <k-form ref="editEmailBizTaskForm" :data-col="2">
		<k-form-item label="id">
        	<k-field-text v-model="formData.id"/>
     	</k-form-item>
		<k-form-item label="业务类型">
        	<k-field-text v-model="formData.bizType"/>
     	</k-form-item>
		<k-form-item label="业务名称">
        	<k-field-text v-model="formData.bizName"/>
     	</k-form-item>
		<k-form-item label="业务表">
        	<k-field-text v-model="formData.bizTable"/>
     	</k-form-item>
		<k-form-item label="业务表取数方法">
        	<k-field-text v-model="formData.bizTableMethod"/>
     	</k-form-item>
		<k-form-item label="业务状态">
        	<k-field-text v-model="formData.bizStatus"/>
     	</k-form-item>
		<k-form-item label="是否立即执行任务 0否 1是">
        	<k-field-text v-model="formData.taskFlag"/>
     	</k-form-item>
		<k-form-item label="记录启用状态，0-未启用，1-已启用">
        	<k-field-text v-model="formData.status"/>
     	</k-form-item>
		<k-form-item label="创建时间HHmmss">
        	<k-field-text v-model="formData.createTime"/>
     	</k-form-item>
		<k-form-item label="创建日期yyyyMMdd">
        	<k-field-text v-model="formData.createDate"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="EmailBizTask.updateEmailBizTask" data-from="editEmailBizTaskForm"
	        :data-model="formData" data-target="emailBizTaskGrid">
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
