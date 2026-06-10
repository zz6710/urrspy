<template>
  <div class="py-page">
    <div>
		<k-form-search-customize data-model-name="RmsOdsZyG06b" data-label-width="80px" v-model="searchParam" data-target="rmsOdsZyG06bGrid">
			<k-form-item label="数据日期">
        <k-field-date v-model="searchParam.dataDate" data-type="date" data-date-format="yyyy-MM-dd"
                      data-value-format="yyyyMMdd"/>
			</k-form-item>
		</k-form-search-customize>
    </div>
    <div class="py-page-container">
		<div class="table-top-btns">
			<k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addRmsOdsZyG06bPopup">
				<md-icon md-src="/static/svg/add.svg" />新增</k-btn>
		</div>
      <k-grid ref="rmsOdsZyG06bGrid" @data-row-select="selectRow" data-action="RmsOdsZyG06b.findRmsOdsZyG06bs" >
		<k-grid-column data-header="主键" data-name="id" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="数据日期" data-name="dataDate" data-type="date"></k-grid-column>
		<k-grid-column data-header="账龄1个月至3个月（含）-期末余额（元）" data-name="b09"></k-grid-column>
		<k-grid-column data-header="账龄3个月至6个月（含）-期末余额（元）" data-name="b10"></k-grid-column>
		<k-grid-column data-header="账龄6个月至1年（含）-期末余额（元）" data-name="b11"></k-grid-column>
		<k-grid-column data-header="账龄1年以上-期末余额" data-name="b12"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改g06b期末余额手工维护" data-functype="POPUP" data-size="mini"
            data-target="editRmsOdsZyG06bPopup">
			  修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="RmsOdsZyG06b.deleteRmsOdsZyG06b" data-size="mini"
               data-type="danger" data-target="rmsOdsZyG06bGrid" :data-confirm="true" data-descript="删除G06b_I银行理财子公司净资本计算表数据">
			  删除
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加g06b期末余额手工维护弹出框   -->
	<k-popup ref="addRmsOdsZyG06bPopup" data-title="新增">
    	<k-form ref="addRmsOdsZyG06bForm" :data-col="2">
			<k-form-item label="报表日期">
	        	<k-field-date v-model="formData.dataDate" data-type="date" data-date-format="yyyy-MM-dd"  data-value-format="yyyyMMdd"
                          :dataAllowblank="false"/>
	     	</k-form-item>
			<k-form-item label="账龄1个月至3个月（含）-期末余额（元）">
	        	<k-field-text v-model="formData.b09" :dataAllowblank="false"/>
	     	</k-form-item>
			<k-form-item label="账龄3个月至6个月（含）-期末余额（元）">
	        	<k-field-text v-model="formData.b10" :dataAllowblank="false"/>
	     	</k-form-item>
			<k-form-item label="账龄6个月至1年（含）-期末余额（元）">
	        	<k-field-text v-model="formData.b11" :dataAllowblank="false"/>
	     	</k-form-item>
			<k-form-item label="账龄1年以上-期末余额（元）">
	        	<k-field-text v-model="formData.b12" :dataAllowblank="false"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="RmsOdsZyG06b.addRmsOdsZyG06b" data-from="addRmsOdsZyG06bForm"
		               :data-model="formData" data-target="rmsOdsZyG06bGrid">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改g06b期末余额手工维护弹出框   -->
	<k-popup ref="editRmsOdsZyG06bPopup" data-title="修改">
	  <k-form ref="editRmsOdsZyG06bForm" :data-col="2">
		<k-form-item label="主键" v-show="false">
        	<k-field-text v-model="formData.id"/>
     	</k-form-item>
		<k-form-item label="报表日期" :class="[handleItemDiff('dataDate')]">
      <k-field-date v-model="formData.dataDate" data-type="date" data-date-format="yyyy-MM-dd"  data-value-format="yyyyMMdd"
                    :dataAllowblank="false" data-disabled="true"/>
     	</k-form-item>
		<k-form-item label="账龄1个月至3个月（含）-期末余额（元）" :class="[handleItemDiff('b09')]">
        	<k-field-text v-model="formData.b09":dataAllowblank="false"/>
     	</k-form-item>
		<k-form-item label="账龄3个月至6个月（含）-期末余额（元）" :class="[handleItemDiff('b10')]">
        	<k-field-text v-model="formData.b10":dataAllowblank="false"/>
     	</k-form-item>
		<k-form-item label="账龄6个月至1年（含）-期末余额（元）" :class="[handleItemDiff('b11')]">
        	<k-field-text v-model="formData.b11":dataAllowblank="false"/>
     	</k-form-item>
		<k-form-item label="账龄1年以上-期末余额（元）" :class="[handleItemDiff('b12')]">
        	<k-field-text v-model="formData.b12":dataAllowblank="false"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="RmsOdsZyG06b.updateRmsOdsZyG06b" data-from="editRmsOdsZyG06bForm"
	        :data-model="formData" data-target="rmsOdsZyG06bGrid" :handle-before="handleBefore">
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
        formDataCopy: {},
        selectRowData: {},
        searchParam: {}
      };
    },
    methods: {
			handleBefore() {
        if (this.formNoChangeCb()) {
          this.$refs.editRmsOdsZyG06bPopup.close();
          return false
        }
        return true
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
        this.formDataCopy = Object.assign({}, row)
      }
    }
  };
</script>
