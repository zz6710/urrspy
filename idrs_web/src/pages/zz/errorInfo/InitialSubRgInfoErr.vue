<template>
  <div class="py-page">
         <div>
              <k-form-search-customize data-model-name="InitialSubRgInfoErr" data-target="initialSubRgInfoErrGrid" data-label-width="80px" v-model = "searchParam">
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
      <k-grid ref="initialSubRgInfoErrGrid" @data-row-select="selectRow" data-operate-column="false" data-action="InitialSubRgInfoErr.findInitialSubRgInfoErrs" >
		<k-grid-column data-header="发行机构代码错误" data-name="bankCodeDesc"></k-grid-column>
		<k-grid-column data-header="产品登记编码错误" data-name="prodCodeDesc"></k-grid-column>
		<k-grid-column data-header="个人投资者总数错误" data-name="numberIndivInvestDesc"></k-grid-column>
		<k-grid-column data-header="法人投资者总数错误" data-name="numberCorporInvestDesc"></k-grid-column>
		<k-grid-column data-header="非法人投资者总数错误" data-name="numberUcorInvestDesc"></k-grid-column>
		<k-grid-column data-header="认购币种错误" data-name="subscriptCurDesc"></k-grid-column>
		<k-grid-column data-header="折算人民币错误" data-name="convertRmbDesc"></k-grid-column>
		<k-grid-column data-header="产品销售区域错误" data-name="prodSalesRegionDesc"></k-grid-column>
		<k-grid-column data-header="是否有其他机构代销错误" data-name="otherDistributAgentsDesc"></k-grid-column>
		<k-grid-column data-header="认购金额错误" data-name="subscriptAmtDesc"></k-grid-column>
		<k-grid-column data-header="区域募集金额错误" data-name="subscriptAmtRegionDesc"></k-grid-column>
		<k-grid-column data-header="实际募集金额错误" data-name="actualSubscribedAmtDesc"></k-grid-column>
		<k-grid-column data-header="募集总份额错误" data-name="subscribedVolDesc"></k-grid-column>
		<k-grid-column data-header="代销总金额错误" data-name="amtOtherDbAgentsDesc"></k-grid-column>
		<k-grid-column data-header="备注错误" data-name="detailsDesc"></k-grid-column>
		<k-grid-column data-header="登记流水号" data-name="registerSerno"></k-grid-column>
		<k-grid-column data-header="导入日期" data-name="impDate"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品募集总量错误信息" data-functype="POPUP" data-size="mini"
            data-target="editInitialSubRgInfoErrPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="InitialSubRgInfoErr.deleteInitialSubRgInfoErr" data-size="mini"
               data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true" data-descript="删除产品募集总量错误信息">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加产品募集总量错误信息弹出框   -->
	<k-popup ref="addInitialSubRgInfoErrPopup" data-title="新增">
    	<k-form ref="addInitialSubRgInfoErrForm" :data-col="2">
			<k-form-item label="发行机构代码错误">
	        	<k-field-text v-model="formData.bankCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="产品登记编码错误">
	        	<k-field-text v-model="formData.prodCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="个人投资者总数错误">
	        	<k-field-text v-model="formData.numberIndivInvestDesc"/>
	     	</k-form-item>
			<k-form-item label="法人投资者总数错误">
	        	<k-field-text v-model="formData.numberCorporInvestDesc"/>
	     	</k-form-item>
			<k-form-item label="非法人投资者总数错误">
	        	<k-field-text v-model="formData.numberUcorInvestDesc"/>
	     	</k-form-item>
			<k-form-item label="认购币种错误">
	        	<k-field-text v-model="formData.subscriptCurDesc"/>
	     	</k-form-item>
			<k-form-item label="折算人民币错误">
	        	<k-field-text v-model="formData.convertRmbDesc"/>
	     	</k-form-item>
			<k-form-item label="产品销售区域错误">
	        	<k-field-text v-model="formData.prodSalesRegionDesc"/>
	     	</k-form-item>
			<k-form-item label="是否有其他机构代销错误">
	        	<k-field-text v-model="formData.otherDistributAgentsDesc"/>
	     	</k-form-item>
			<k-form-item label="认购金额错误">
	        	<k-field-text v-model="formData.subscriptAmtDesc"/>
	     	</k-form-item>
			<k-form-item label="区域募集金额错误">
	        	<k-field-text v-model="formData.subscriptAmtRegionDesc"/>
	     	</k-form-item>
			<k-form-item label="实际募集金额错误">
	        	<k-field-text v-model="formData.actualSubscribedAmtDesc"/>
	     	</k-form-item>
			<k-form-item label="募集总份额错误">
	        	<k-field-text v-model="formData.subscribedVolDesc"/>
	     	</k-form-item>
			<k-form-item label="代销总金额错误">
	        	<k-field-text v-model="formData.amtOtherDbAgentsDesc"/>
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
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="InitialSubRgInfoErr.addInitialSubRgInfoErr" data-from="addInitialSubRgInfoErrForm"
		               :data-model="formData" data-target="initialSubRgInfoErrGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改产品募集总量错误信息弹出框   -->
	<k-popup ref="editInitialSubRgInfoErrPopup" data-title="修改">
	  <k-form ref="editInitialSubRgInfoErrForm" :data-col="2">
		<k-form-item label="发行机构代码错误">
        	<k-field-text v-model="formData.bankCodeDesc"/>
     	</k-form-item>
		<k-form-item label="产品登记编码错误">
        	<k-field-text v-model="formData.prodCodeDesc"/>
     	</k-form-item>
		<k-form-item label="个人投资者总数错误">
        	<k-field-text v-model="formData.numberIndivInvestDesc"/>
     	</k-form-item>
		<k-form-item label="法人投资者总数错误">
        	<k-field-text v-model="formData.numberCorporInvestDesc"/>
     	</k-form-item>
		<k-form-item label="非法人投资者总数错误">
        	<k-field-text v-model="formData.numberUcorInvestDesc"/>
     	</k-form-item>
		<k-form-item label="认购币种错误">
        	<k-field-text v-model="formData.subscriptCurDesc"/>
     	</k-form-item>
		<k-form-item label="折算人民币错误">
        	<k-field-text v-model="formData.convertRmbDesc"/>
     	</k-form-item>
		<k-form-item label="产品销售区域错误">
        	<k-field-text v-model="formData.prodSalesRegionDesc"/>
     	</k-form-item>
		<k-form-item label="是否有其他机构代销错误">
        	<k-field-text v-model="formData.otherDistributAgentsDesc"/>
     	</k-form-item>
		<k-form-item label="认购金额错误">
        	<k-field-text v-model="formData.subscriptAmtDesc"/>
     	</k-form-item>
		<k-form-item label="区域募集金额错误">
        	<k-field-text v-model="formData.subscriptAmtRegionDesc"/>
     	</k-form-item>
		<k-form-item label="实际募集金额错误">
        	<k-field-text v-model="formData.actualSubscribedAmtDesc"/>
     	</k-form-item>
		<k-form-item label="募集总份额错误">
        	<k-field-text v-model="formData.subscribedVolDesc"/>
     	</k-form-item>
		<k-form-item label="代销总金额错误">
        	<k-field-text v-model="formData.amtOtherDbAgentsDesc"/>
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
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="InitialSubRgInfoErr.updateInitialSubRgInfoErr" data-from="editInitialSubRgInfoErrForm"
	        :data-model="formData" data-target="initialSubRgInfoErrGrid">
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
    name: "InitialSubRgInfoErr",
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
