<template>
  <div class="py-page">
     <div>
         <k-form-search-customize data-model-name="CustVolRgInfoErr" data-target="CustVolRgInfoErrGrid" data-label-width="80px" v-model = "searchParam">
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
      <k-grid ref="CustVolRgInfoErrGrid" @data-row-select="selectRow" data-operate-column="false" data-action="CustVolRgInfoErr.findCustVolRgInfos" >
		<k-grid-column data-header="银行代码错误描述" data-name="bankCodeDesc"></k-grid-column>
		<k-grid-column data-header="产品登记编码错误描述" data-name="prodCodeDesc"></k-grid-column>
		<k-grid-column data-header="识别标识错误描述" data-name="custNoDesc"></k-grid-column>
		<k-grid-column data-header="持有日期错误描述" data-name="holdDateDesc"></k-grid-column>
		<k-grid-column data-header="币种错误描述" data-name="curDesc"></k-grid-column>
		<k-grid-column data-header="持有份额错误描述" data-name="holdVolDesc"></k-grid-column>
		<k-grid-column data-header="持有金额错误描述" data-name="holdAmtDesc"></k-grid-column>
		<k-grid-column data-header="折算人民币金额错误描述" data-name="convertRmbDesc"></k-grid-column>
		<k-grid-column data-header="导入日期" data-name="impDate"></k-grid-column>
		<k-grid-column data-header="登记流水号" data-name="registerSerno"></k-grid-column>
		<k-grid-column data-header="记录ID" data-name="id"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改投资者持有错误信息" data-functype="POPUP" data-size="mini"
            data-target="editCustVolRgInfoErrPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="CustVolRgInfoErr.deleteCustVolRgInfo" data-size="mini"
               data-type="danger" data-target="CustVolRgInfoErrGrid" :data-confirm="true" data-descript="删除投资者持有错误信息">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加投资者持有错误信息弹出框   -->
	<k-popup ref="addCustVolRgInfoErrPopup" data-title="新增">
    	<k-form ref="addCustVolRgInfoErrForm" :data-col="2">
			<k-form-item label="银行代码错误描述">
	        	<k-field-text v-model="formData.bankCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="产品登记编码错误描述">
	        	<k-field-text v-model="formData.prodCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="识别标识错误描述">
	        	<k-field-text v-model="formData.custNoDesc"/>
	     	</k-form-item>
			<k-form-item label="持有日期错误描述">
	        	<k-field-text v-model="formData.holdDateDesc"/>
	     	</k-form-item>
			<k-form-item label="币种错误描述">
	        	<k-field-text v-model="formData.curDesc"/>
	     	</k-form-item>
			<k-form-item label="持有份额错误描述">
	        	<k-field-text v-model="formData.holdVolDesc"/>
	     	</k-form-item>
			<k-form-item label="持有金额错误描述">
	        	<k-field-text v-model="formData.holdAmtDesc"/>
	     	</k-form-item>
			<k-form-item label="折算人民币金额错误描述">
	        	<k-field-text v-model="formData.convertRmbDesc"/>
	     	</k-form-item>
			<k-form-item label="导入日期">
	        	<k-field-text v-model="formData.impDate"/>
	     	</k-form-item>
			<k-form-item label="登记流水号">
	        	<k-field-text v-model="formData.registerSerno"/>
	     	</k-form-item>
			<k-form-item label="记录ID">
	        	<k-field-text v-model="formData.id"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="CustVolRgInfoErr.addCustVolRgInfo" data-from="addCustVolRgInfoErrForm"
		               :data-model="formData" data-target="CustVolRgInfoErrGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改投资者持有错误信息弹出框   -->
	<k-popup ref="editCustVolRgInfoErrPopup" data-title="修改">
	  <k-form ref="editCustVolRgInfoErrForm" :data-col="2">
		<k-form-item label="银行代码错误描述">
        	<k-field-text v-model="formData.bankCodeDesc"/>
     	</k-form-item>
		<k-form-item label="产品登记编码错误描述">
        	<k-field-text v-model="formData.prodCodeDesc"/>
     	</k-form-item>
		<k-form-item label="识别标识错误描述">
        	<k-field-text v-model="formData.custNoDesc"/>
     	</k-form-item>
		<k-form-item label="持有日期错误描述">
        	<k-field-text v-model="formData.holdDateDesc"/>
     	</k-form-item>
		<k-form-item label="币种错误描述">
        	<k-field-text v-model="formData.curDesc"/>
     	</k-form-item>
		<k-form-item label="持有份额错误描述">
        	<k-field-text v-model="formData.holdVolDesc"/>
     	</k-form-item>
		<k-form-item label="持有金额错误描述">
        	<k-field-text v-model="formData.holdAmtDesc"/>
     	</k-form-item>
		<k-form-item label="折算人民币金额错误描述">
        	<k-field-text v-model="formData.convertRmbDesc"/>
     	</k-form-item>
		<k-form-item label="导入日期">
        	<k-field-text v-model="formData.impDate"/>
     	</k-form-item>
		<k-form-item label="登记流水号">
        	<k-field-text v-model="formData.registerSerno"/>
     	</k-form-item>
		<k-form-item label="记录ID">
        	<k-field-text v-model="formData.id"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="CustVolRgInfoErr.updateCustVolRgInfo" data-from="editCustVolRgInfoErrForm"
	        :data-model="formData" data-target="CustVolRgInfoErrGrid">
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
    name: "CustVolRgInfoErr",
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
