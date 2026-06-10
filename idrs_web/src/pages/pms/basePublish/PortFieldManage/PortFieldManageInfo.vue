<template>
  <div>
    <div>
      <k-form-search-customize data-model-name="PortFieldManageInfo" data-target="portFieldManageInfoGrid" v-model="searchParam">
        <k-form-item label="接口代码">
          <k-field-text v-model="searchParam.portCode"/>
        </k-form-item>
        <k-form-item label="字段名称">
          <k-field-text v-model="searchParam.fieldName"/>
        </k-form-item>
        <k-form-item label="录入柜员">
          <k-field-text v-model="searchParam.inputuser"/>
        </k-form-item>

        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addPortFieldManageInfoPopup"  slot="button">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="portFieldManageInfoGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="250px" data-action="PortFieldManageInfo.findPortFieldManageInfos" >
		<k-grid-column data-header="接口代码 " data-name="portCode"></k-grid-column>
		<k-grid-column data-header="id " data-name="id" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="字段代码 " data-name="fieldCode"></k-grid-column>
		<k-grid-column data-header="字段名称 " data-name="fieldName"></k-grid-column>
		<k-grid-column data-header="字段类型 " data-name="fieldType"></k-grid-column>
		<k-grid-column data-header="字段长度 " data-name="fieldLength"></k-grid-column>
		<k-grid-column data-header="字段小数位 " data-name="fieldDights"></k-grid-column>
		<k-grid-column data-header="字段序号 " data-name="fieldSeq"></k-grid-column>
		<k-grid-column data-header="录入柜员 " data-name="inputuser"></k-grid-column>
		<k-grid-column data-header="创建日期 " data-name="crtDate" data-type="date"></k-grid-column>
		<k-grid-column data-header="创建时间 " data-name="crtTime" data-type="time"></k-grid-column>
		<k-grid-column data-header="更新日期 " data-name="updDate" data-type="date"></k-grid-column>
		<k-grid-column data-header="更新时间 " data-name="updTime" data-type="time"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-plain" data-descript="修改接口文件字段信息" data-functype="POPUP" data-size="mini"
                  data-target="editPortFieldManageInfoPopup">
            修改
          </k-btn>
          <k-btn class="md-danger" data-functype="SUBMIT" data-action="PortFieldManageInfo.deletePortFieldManageInfo"
                 :data-confirm="true" data-size="mini" data-type="danger" data-target="portFieldManageInfoGrid" data-descript="删除接口文件字段信息">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加接口文件字段信息弹出框   -->
	<k-popup ref="addPortFieldManageInfoPopup" data-title="新增">
    	<k-form ref="addPortFieldManageInfoForm" :data-col="2">
			<k-form-item label="接口代码 ">
	        	<k-field-text v-model="formData.portCode" :data-allowblank="false"/>
	     	</k-form-item>
			<k-form-item label="字段代码 ">
	        	<k-field-text v-model="formData.fieldCode" :data-allowblank="false"/>
	     	</k-form-item>
			<k-form-item label="字段名称 ">
	        	<k-field-text v-model="formData.fieldName" :data-allowblank="false"/>
	     	</k-form-item>
			<k-form-item label="字段类型 ">
	        	<k-field-text v-model="formData.fieldType" :data-allowblank="false"/>
	     	</k-form-item>
			<k-form-item label="字段长度 ">
	        	<k-field-text v-model="formData.fieldLength" :data-allowblank="false" data-validate-type="int"/>
	     	</k-form-item>
			<k-form-item label="字段小数位 ">
	        	<k-field-text v-model="formData.fieldDights" data-validate-type="int"/>
	     	</k-form-item>
			<k-form-item label="字段序号 ">
	        	<k-field-text v-model="formData.fieldSeq" :data-allowblank="false" data-validate-type="int"/>
	     	</k-form-item>
	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="PortFieldManageInfo.addPortFieldManageInfo" data-from="addPortFieldManageInfoForm"
		               :data-model="formData" data-target="portFieldManageInfoGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改接口文件字段信息弹出框   -->
	<k-popup ref="editPortFieldManageInfoPopup" data-title="修改">
	  <k-form ref="editPortFieldManageInfoForm" :data-col="2">
      <k-form-item label="接口代码 ">
        <k-field-text v-model="formData.portCode" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="字段代码 ">
        <k-field-text v-model="formData.fieldCode" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="字段名称 ">
        <k-field-text v-model="formData.fieldName" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="字段类型 ">
        <k-field-text v-model="formData.fieldType" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="字段长度 ">
        <k-field-text v-model="formData.fieldLength" :data-allowblank="false" data-validate-type="int"/>
      </k-form-item>
      <k-form-item label="字段小数位 ">
        <k-field-text v-model="formData.fieldDights" data-validate-type="int"/>
      </k-form-item>
      <k-form-item label="字段序号 ">
        <k-field-text v-model="formData.fieldSeq" :data-allowblank="false" data-validate-type="int"/>
      </k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="PortFieldManageInfo.updatePortFieldManageInfo" data-from="editPortFieldManageInfoForm"
	        :data-model="formData" data-target="portFieldManageInfoGrid">
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
    name:"PortFieldManageInfo",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{}
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
