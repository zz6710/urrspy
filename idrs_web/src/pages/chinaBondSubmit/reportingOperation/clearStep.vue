<template>
  <div>
    <div>
      <k-form-search data-model-name="clearStep" data-target="clearStepGrid">
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addclearStepPopup">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
      </k-form-search>
    </div>
    <div>
      <k-grid ref="clearStepGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="250px" data-action="clearStep.findclearSteps" >
		<k-grid-column data-header="流程序号,必需连续,第0号流程用于锁定,小于0的序号属于特殊处理不在流程控制范围内" data-name="stepNo"></k-grid-column>
		<k-grid-column data-header="子流程序号" data-name="stepSubNo"></k-grid-column>
		<k-grid-column data-header="系统工作日" data-name="workdate"></k-grid-column>
		<k-grid-column data-header="业务代码" data-name="busiCode"></k-grid-column>
		<k-grid-column data-header="执行状态(0" data-name="execStatus"></k-grid-column>
		<k-grid-column data-header="子流程名称" data-name="stepSubName"></k-grid-column>
		<k-grid-column data-header="java/bizware" data-name="execType"></k-grid-column>
		<k-grid-column data-header="" data-name="className"></k-grid-column>
		<k-grid-column data-header="" data-name="methodName"></k-grid-column>
		<k-grid-column data-header="" data-name="serviceCode"></k-grid-column>
		<k-grid-column data-header="" data-name="businessCode"></k-grid-column>
		<k-grid-column data-header="" data-name="updDate"></k-grid-column>
		<k-grid-column data-header="" data-name="updTime"></k-grid-column>
		<k-grid-column data-header="备注" data-name="remark"></k-grid-column>
		<k-grid-column data-header="出错后,再次开始步骤" data-name="reStep"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改清算流程子步骤表,用于记录子步骤执行状态" data-functype="POPUP" data-size="mini"
            data-target="editclearStepPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="clearStep.deleteclearStep" data-size="mini"
               data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true" data-descript="删除清算流程子步骤表,用于记录子步骤执行状态">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加清算流程子步骤表,用于记录子步骤执行状态弹出框   -->
	<k-popup ref="addclearStepPopup" data-title="新增">
    	<k-form ref="addclearStepForm" :data-col="2">
			<k-form-item label="流程序号,必需连续,第0号流程用于锁定,小于0的序号属于特殊处理不在流程控制范围内">
	        	<k-field-text v-model="formData.stepNo"/>
	     	</k-form-item>
			<k-form-item label="子流程序号">
	        	<k-field-text v-model="formData.stepSubNo"/>
	     	</k-form-item>
			<k-form-item label="系统工作日">
	        	<k-field-text v-model="formData.workdate"/>
	     	</k-form-item>
			<k-form-item label="业务代码">
	        	<k-field-text v-model="formData.busiCode"/>
	     	</k-form-item>
			<k-form-item label="执行状态(
0">
	        	<k-field-text v-model="formData.execStatus"/>
	     	</k-form-item>
			<k-form-item label="子流程名称">
	        	<k-field-text v-model="formData.stepSubName"/>
	     	</k-form-item>
			<k-form-item label="java/bizware">
	        	<k-field-text v-model="formData.execType"/>
	     	</k-form-item>
			<k-form-item label="">
	        	<k-field-text v-model="formData.className"/>
	     	</k-form-item>
			<k-form-item label="">
	        	<k-field-text v-model="formData.methodName"/>
	     	</k-form-item>
			<k-form-item label="">
	        	<k-field-text v-model="formData.serviceCode"/>
	     	</k-form-item>
			<k-form-item label="">
	        	<k-field-text v-model="formData.businessCode"/>
	     	</k-form-item>
			<k-form-item label="">
	        	<k-field-text v-model="formData.updDate"/>
	     	</k-form-item>
			<k-form-item label="">
	        	<k-field-text v-model="formData.updTime"/>
	     	</k-form-item>
			<k-form-item label="备注">
	        	<k-field-text v-model="formData.remark"/>
	     	</k-form-item>
			<k-form-item label="出错后,再次开始步骤">
	        	<k-field-text v-model="formData.reStep"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="clearStep.addclearStep" data-from="addclearStepForm"
		               :data-model="formData" data-target="clearStepGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改清算流程子步骤表,用于记录子步骤执行状态弹出框   -->
	<k-popup ref="editclearStepPopup" data-title="修改">
	  <k-form ref="editclearStepForm" :data-col="2">
		<k-form-item label="流程序号,必需连续,第0号流程用于锁定,小于0的序号属于特殊处理不在流程控制范围内">
        	<k-field-text v-model="formData.stepNo"/>
     	</k-form-item>
		<k-form-item label="子流程序号">
        	<k-field-text v-model="formData.stepSubNo"/>
     	</k-form-item>
		<k-form-item label="系统工作日">
        	<k-field-text v-model="formData.workdate"/>
     	</k-form-item>
		<k-form-item label="业务代码">
        	<k-field-text v-model="formData.busiCode"/>
     	</k-form-item>
		<k-form-item label="执行状态(
0">
        	<k-field-text v-model="formData.execStatus"/>
     	</k-form-item>
		<k-form-item label="子流程名称">
        	<k-field-text v-model="formData.stepSubName"/>
     	</k-form-item>
		<k-form-item label="java/bizware">
        	<k-field-text v-model="formData.execType"/>
     	</k-form-item>
		<k-form-item label="">
        	<k-field-text v-model="formData.className"/>
     	</k-form-item>
		<k-form-item label="">
        	<k-field-text v-model="formData.methodName"/>
     	</k-form-item>
		<k-form-item label="">
        	<k-field-text v-model="formData.serviceCode"/>
     	</k-form-item>
		<k-form-item label="">
        	<k-field-text v-model="formData.businessCode"/>
     	</k-form-item>
		<k-form-item label="">
        	<k-field-text v-model="formData.updDate"/>
     	</k-form-item>
		<k-form-item label="">
        	<k-field-text v-model="formData.updTime"/>
     	</k-form-item>
		<k-form-item label="备注">
        	<k-field-text v-model="formData.remark"/>
     	</k-form-item>
		<k-form-item label="出错后,再次开始步骤">
        	<k-field-text v-model="formData.reStep"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="clearStep.updateclearStep" data-from="editclearStepForm"
	        :data-model="formData" data-target="clearStepGrid">
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
    name: "clearStep",
    data() {
      return {
        formData: {},
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
