<template>
  <div>
    <div>
      <k-form-search data-model-name="T8OdsTaskLog" data-target="t8OdsTaskLogGrid">
<!--        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addT8OdsTaskLogPopup">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>-->
      </k-form-search>
    </div>
    <div>
      <k-grid ref="t8OdsTaskLogGrid" @data-row-select="selectRow" data-action="T8OdsTaskLog.findT8OdsTaskLogs" >
		<k-grid-column data-header="ID" data-name="id" data-width="100"></k-grid-column>
		<k-grid-column data-header="同步表名称" data-name="tableName" data-width="200"></k-grid-column>
		<k-grid-column data-header="同步条件" data-name="selectCondition" data-width="200"></k-grid-column>
		<k-grid-column data-header="执行开始时间" data-name="execStartTime" data-render="renderDateTimeStart"></k-grid-column>
		<k-grid-column data-header="执行结束时间" data-name="execEndTime" data-render="renderDateTimeEnd"></k-grid-column>
		<k-grid-column data-header="上次同步耗时(秒)" data-name="costTime"></k-grid-column>
		<k-grid-column data-header="运行状态" data-name="taskStatus" data-dict="t8_ods_task_status"></k-grid-column>
		<k-grid-column data-header="更新日期" data-name="updateDate"></k-grid-column>
		<k-grid-column data-header="更新时间" data-name="updateTime"></k-grid-column>
		<k-grid-column data-header="备注" data-name="remark"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="查看批处理记录表" data-functype="POPUP" data-size="mini"
            data-target="editT8OdsTaskLogPopup">
            <md-icon>edit</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加批处理记录表弹出框   -->
	<k-popup ref="addT8OdsTaskLogPopup" data-title="新增">
    	<k-form ref="addT8OdsTaskLogForm" :data-col="2">
			<k-form-item label="ID">
	        	<k-field-text v-model="formData.id"/>
	     	</k-form-item>
			<k-form-item label="同步表名称">
	        	<k-field-text v-model="formData.tableName"/>
	     	</k-form-item>
			<k-form-item label="执行开始时间">
	        	<k-field-text v-model="formData.execStartTime"/>
	     	</k-form-item>
			<k-form-item label="执行结束时间">
	        	<k-field-text v-model="formData.execEndTime"/>
	     	</k-form-item>
			<k-form-item label="上次同步耗时">
	        	<k-field-text v-model="formData.costTime"/>
	     	</k-form-item>
			<k-form-item label="运行状态">
	        	<k-field-text v-model="formData.taskStatus"/>
	     	</k-form-item>
			<k-form-item label="更新日期">
	        	<k-field-text v-model="formData.updateDate"/>
	     	</k-form-item>
			<k-form-item label="更新时间">
	        	<k-field-text v-model="formData.updateTime"/>
	     	</k-form-item>
			<k-form-item label="备注">
	        	<k-field-text v-model="formData.remark"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8OdsTaskLog.addT8OdsTaskLog" data-from="addT8OdsTaskLogForm"
		               :data-model="formData" data-target="t8OdsTaskLogGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改批处理记录表弹出框   -->
	<k-popup ref="editT8OdsTaskLogPopup" data-title="修改" :dataDialogDrag="true">
	  <k-form ref="editT8OdsTaskLogForm" :data-col="2">
		<k-form-item label="ID">
        	<k-field-text v-model="formData.id" data-disabled="true"/>
     	</k-form-item>
		<k-form-item label="同步表名称">
        	<k-field-text v-model="formData.tableName" data-disabled="true"/>
     	</k-form-item>
      <k-form-item label="同步条件" :data-col="2">
        <k-field-text v-model="formData.selectCondition" inputType="textarea" :rows="3" data-disabled="true"/>
      </k-form-item>
		<k-form-item label="执行开始时间">
        	<k-field-text v-model="formData.execStartTime" data-disabled="true"/>
     	</k-form-item>
		<k-form-item label="执行结束时间">
        	<k-field-text v-model="formData.execEndTime" data-disabled="true"/>
     	</k-form-item>
		<k-form-item label="上次同步耗时(秒)">
        	<k-field-text v-model="formData.costTime" data-disabled="true"/>
     	</k-form-item>
		<k-form-item label="运行状态">
        	<k-field-select v-model="formData.taskStatus" data-disabled="true" data-dict="t8_ods_task_status"/>
     	</k-form-item>
		<k-form-item label="更新日期">
        	<k-field-text v-model="formData.updateDate" data-disabled="true"/>
     	</k-form-item>
		<k-form-item label="更新时间">
        	<k-field-text v-model="formData.updateTime" data-disabled="true"/>
     	</k-form-item>
		<k-form-item label="备注" :data-col="2">
        	<k-field-text v-model="formData.remark" inputType="textarea" :rows="5" data-disabled="true"/>
     	</k-form-item>
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
        selectRowData: {}
      };
    },
    methods: {
      renderDateTimeStart(row) {
        return Tools.formatDateTimeStr(row.execStartTime);
      },
      renderDateTimeEnd(row) {
        return Tools.formatDateTimeStr(row.execEndTime);
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      }
    }
  };
</script>
