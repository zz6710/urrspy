<template>
  <div class="py-page">
     <div>
         <k-form-search-customize data-model-name="AppraiseRgInfoErr" data-target="appraiseRgInfoErrGrid" data-label-width="80px" v-model = "searchParam">
           <k-form-item label="导入日期">
             <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMM"
                           data-value-format="yyyyMMdd"/>
           </k-form-item>
           <k-form-item label="登记流水号">
             <k-field-text v-model="searchParam.registerSerno"/>
           </k-form-item>
         </k-form-search-customize>
       </div>
    <div class="py-page-container">
      <k-grid ref="appraiseRgInfoErrGrid" @data-row-select="selectRow" data-operate-column="false" data-action="AppraiseRgInfoErr.findAppraiseRgInfoErrs" >
		<k-grid-column data-header="发行机构代码错误" data-name="bankCodeDesc"></k-grid-column>
		<k-grid-column data-header="行内资产负债编码错误" data-name="assetCodeDesc"></k-grid-column>
		<k-grid-column data-header="估值日期错误" data-name="valuationDateDesc"></k-grid-column>
		<k-grid-column data-header="单位估值" data-name="unitDebtNetDesc"></k-grid-column>
		<k-grid-column data-header="单位估值" data-name="unitDebtFullDesc"></k-grid-column>
		<k-grid-column data-header="备注错误" data-name="detailsDesc"></k-grid-column>
		<k-grid-column data-header="登记流水号" data-name="registerSerno"></k-grid-column>
		<k-grid-column data-header="导入日期" data-name="impDate"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改估值信息错误" data-functype="POPUP" data-size="mini"
            data-target="editAppraiseRgInfoErrPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="AppraiseRgInfoErr.deleteAppraiseRgInfoErr" data-size="mini"
               data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true" data-descript="删除估值信息错误">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加估值信息错误弹出框   -->
	<k-popup ref="addAppraiseRgInfoErrPopup" data-title="新增">
    	<k-form ref="addAppraiseRgInfoErrForm" :data-col="2">
			<k-form-item label="发行机构代码错误">
	        	<k-field-text v-model="formData.bankCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="行内资产负债编码错误">
	        	<k-field-text v-model="formData.assetCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="估值日期错误">
	        	<k-field-text v-model="formData.valuationDateDesc"/>
	     	</k-form-item>
			<k-form-item label="单位估值">
	        	<k-field-text v-model="formData.unitDebtNetDesc"/>
	     	</k-form-item>
			<k-form-item label="单位估值">
	        	<k-field-text v-model="formData.unitDebtFullDesc"/>
	     	</k-form-item>
			<k-form-item label="备注错误">
	        	<k-field-text v-model="formData.detailsDesc"/>
	     	</k-form-item>
			<k-form-item label="登记流水号">
	        	<k-field-text v-model="formData.registerSerno"/>
	     	</k-form-item>
			<k-form-item label="导入日期">
	        	<k-field-text v-model="formData.impDate"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AppraiseRgInfoErr.addAppraiseRgInfoErr" data-from="addAppraiseRgInfoErrForm"
		               :data-model="formData" data-target="appraiseRgInfoErrGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改估值信息错误弹出框   -->
	<k-popup ref="editAppraiseRgInfoErrPopup" data-title="修改">
	  <k-form ref="editAppraiseRgInfoErrForm" :data-col="2">
		<k-form-item label="发行机构代码错误">
        	<k-field-text v-model="formData.bankCodeDesc"/>
     	</k-form-item>
		<k-form-item label="行内资产负债编码错误">
        	<k-field-text v-model="formData.assetCodeDesc"/>
     	</k-form-item>
		<k-form-item label="估值日期错误">
        	<k-field-text v-model="formData.valuationDateDesc"/>
     	</k-form-item>
		<k-form-item label="单位估值">
        	<k-field-text v-model="formData.unitDebtNetDesc"/>
     	</k-form-item>
		<k-form-item label="单位估值">
        	<k-field-text v-model="formData.unitDebtFullDesc"/>
     	</k-form-item>
		<k-form-item label="备注错误">
        	<k-field-text v-model="formData.detailsDesc"/>
     	</k-form-item>
		<k-form-item label="登记流水号">
        	<k-field-text v-model="formData.registerSerno"/>
     	</k-form-item>
		<k-form-item label="导入日期">
        	<k-field-text v-model="formData.impDate"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AppraiseRgInfoErr.updateAppraiseRgInfoErr" data-from="editAppraiseRgInfoErrForm"
	        :data-model="formData" data-target="appraiseRgInfoErrGrid">
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
    name: "AppraiseRgInfoErr",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{}, //查询条件
        BreathDay:[],
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      }
    },
    watch: {
      // 查询导入日期
      BreathDay() {
        console.log(this.BreathDay);
        this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
        this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
      },
    }
  };
</script>
