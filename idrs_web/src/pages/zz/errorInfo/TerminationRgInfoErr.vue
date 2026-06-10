<template>
  <div class="py-page">
    <div>
        <k-form-search-customize data-model-name="TerminationRgInfoErr" data-target="TerminationRgInfoErrGrid" data-label-width="80px" v-model = "searchParam">
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
      <k-grid ref="TerminationRgInfoErrGrid" @data-row-select="selectRow" data-operate-column="false" data-action="TerminationRgInfoErr.findTerminationRgInfos" >
		<k-grid-column data-header="产品登记编码错误" data-name="prodCodeDesc"></k-grid-column>
		<k-grid-column data-header="发行机构代码错误" data-name="bankCodeDesc"></k-grid-column>
		<k-grid-column data-header="理财产品实际终止日期错误" data-name="actualProdTerDateDesc"></k-grid-column>
		<k-grid-column data-header="银行实际实现收入错误" data-name="realizedBankIncomeDesc"></k-grid-column>
		<k-grid-column data-header="兑付客户收益" data-name="interestPaymentDesc"></k-grid-column>
		<k-grid-column data-header="兑付客户总金额错误" data-name="paymentDesc"></k-grid-column>
		<k-grid-column data-header="兑付总份额错误" data-name="deliveredVolDesc"></k-grid-column>
		<k-grid-column data-header="本机构托管费" data-name="inCustodianFeeDesc"></k-grid-column>
		<k-grid-column data-header="本机构管理费" data-name="inManageFeeDesc"></k-grid-column>
		<k-grid-column data-header="本机构销售手续费" data-name="inSalesCommisionDesc"></k-grid-column>
		<k-grid-column data-header="本机构其他产品配用" data-name="inOtherProdFeeDesc"></k-grid-column>
		<k-grid-column data-header="其他机构托管费" data-name="otherCustodianFeeDesc"></k-grid-column>
		<k-grid-column data-header="其他机构管理费" data-name="otherManageFeeDesc"></k-grid-column>
		<k-grid-column data-header="其他机构销售手续费" data-name="otherSalesCommDesc"></k-grid-column>
		<k-grid-column data-header="投资顾问费用" data-name="consultFeeDesc"></k-grid-column>
		<k-grid-column data-header="其他机构其他产品费用" data-name="otherProdFeeDesc"></k-grid-column>
		<k-grid-column data-header="客户实际年化收益率%错误" data-name="annualReturnClientDesc"></k-grid-column>
		<k-grid-column data-header="产品实际年化收益率%错误" data-name="annualReturnProdDesc"></k-grid-column>
		<k-grid-column data-header="登记流水号" data-name="registerSerno"></k-grid-column>
		<k-grid-column data-header="导入日期" data-name="impDate"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品终止登记错误信息" data-functype="POPUP" data-size="mini"
            data-target="editTerminationRgInfoErrPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="TerminationRgInfoErr.deleteTerminationRgInfo" data-size="mini"
               data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true" data-descript="删除产品终止登记错误信息">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加产品终止登记错误信息弹出框   -->
	<k-popup ref="addTerminationRgInfoErrPopup" data-title="新增">
    	<k-form ref="addTerminationRgInfoErrForm" :data-col="2">
			<k-form-item label="产品登记编码错误">
	        	<k-field-text v-model="formData.prodCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="发行机构代码错误">
	        	<k-field-text v-model="formData.bankCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="理财产品实际终止日期错误">
	        	<k-field-text v-model="formData.actualProdTerDateDesc"/>
	     	</k-form-item>
			<k-form-item label="银行实际实现收入错误">
	        	<k-field-text v-model="formData.realizedBankIncomeDesc"/>
	     	</k-form-item>
			<k-form-item label="兑付客户收益">
	        	<k-field-text v-model="formData.interestPaymentDesc"/>
	     	</k-form-item>
			<k-form-item label="兑付客户总金额错误">
	        	<k-field-text v-model="formData.paymentDesc"/>
	     	</k-form-item>
			<k-form-item label="兑付总份额错误">
	        	<k-field-text v-model="formData.deliveredVolDesc"/>
	     	</k-form-item>
			<k-form-item label="本机构托管费">
	        	<k-field-text v-model="formData.inCustodianFeeDesc"/>
	     	</k-form-item>
			<k-form-item label="本机构管理费">
	        	<k-field-text v-model="formData.inManageFeeDesc"/>
	     	</k-form-item>
			<k-form-item label="本机构销售手续费">
	        	<k-field-text v-model="formData.inSalesCommisionDesc"/>
	     	</k-form-item>
			<k-form-item label="本机构其他产品配用">
	        	<k-field-text v-model="formData.inOtherProdFeeDesc"/>
	     	</k-form-item>
			<k-form-item label="其他机构托管费">
	        	<k-field-text v-model="formData.otherCustodianFeeDesc"/>
	     	</k-form-item>
			<k-form-item label="其他机构管理费">
	        	<k-field-text v-model="formData.otherManageFeeDesc"/>
	     	</k-form-item>
			<k-form-item label="其他机构销售手续费">
	        	<k-field-text v-model="formData.otherSalesCommDesc"/>
	     	</k-form-item>
			<k-form-item label="投资顾问费用">
	        	<k-field-text v-model="formData.consultFeeDesc"/>
	     	</k-form-item>
			<k-form-item label="其他机构其他产品费用">
	        	<k-field-text v-model="formData.otherProdFeeDesc"/>
	     	</k-form-item>
			<k-form-item label="客户实际年化收益率%错误">
	        	<k-field-text v-model="formData.annualReturnClientDesc"/>
	     	</k-form-item>
			<k-form-item label="产品实际年化收益率%错误">
	        	<k-field-text v-model="formData.annualReturnProdDesc"/>
	     	</k-form-item>
			<k-form-item label="登记流水号">
	        	<k-field-text v-model="formData.registerSerno"/>
	     	</k-form-item>
			<k-form-item label="导入日期">
	        	<k-field-text v-model="formData.impDate"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="TerminationRgInfoErr.addTerminationRgInfo" data-from="addTerminationRgInfoErrForm"
		               :data-model="formData" data-target="TerminationRgInfoErrGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改产品终止登记错误信息弹出框   -->
	<k-popup ref="editTerminationRgInfoErrPopup" data-title="修改">
	  <k-form ref="editTerminationRgInfoErrForm" :data-col="2">
		<k-form-item label="产品登记编码错误">
        	<k-field-text v-model="formData.prodCodeDesc"/>
     	</k-form-item>
		<k-form-item label="发行机构代码错误">
        	<k-field-text v-model="formData.bankCodeDesc"/>
     	</k-form-item>
		<k-form-item label="理财产品实际终止日期错误">
        	<k-field-text v-model="formData.actualProdTerDateDesc"/>
     	</k-form-item>
		<k-form-item label="银行实际实现收入错误">
        	<k-field-text v-model="formData.realizedBankIncomeDesc"/>
     	</k-form-item>
		<k-form-item label="兑付客户收益">
        	<k-field-text v-model="formData.interestPaymentDesc"/>
     	</k-form-item>
		<k-form-item label="兑付客户总金额错误">
        	<k-field-text v-model="formData.paymentDesc"/>
     	</k-form-item>
		<k-form-item label="兑付总份额错误">
        	<k-field-text v-model="formData.deliveredVolDesc"/>
     	</k-form-item>
		<k-form-item label="本机构托管费">
        	<k-field-text v-model="formData.inCustodianFeeDesc"/>
     	</k-form-item>
		<k-form-item label="本机构管理费">
        	<k-field-text v-model="formData.inManageFeeDesc"/>
     	</k-form-item>
		<k-form-item label="本机构销售手续费">
        	<k-field-text v-model="formData.inSalesCommisionDesc"/>
     	</k-form-item>
		<k-form-item label="本机构其他产品配用">
        	<k-field-text v-model="formData.inOtherProdFeeDesc"/>
     	</k-form-item>
		<k-form-item label="其他机构托管费">
        	<k-field-text v-model="formData.otherCustodianFeeDesc"/>
     	</k-form-item>
		<k-form-item label="其他机构管理费">
        	<k-field-text v-model="formData.otherManageFeeDesc"/>
     	</k-form-item>
		<k-form-item label="其他机构销售手续费">
        	<k-field-text v-model="formData.otherSalesCommDesc"/>
     	</k-form-item>
		<k-form-item label="投资顾问费用">
        	<k-field-text v-model="formData.consultFeeDesc"/>
     	</k-form-item>
		<k-form-item label="其他机构其他产品费用">
        	<k-field-text v-model="formData.otherProdFeeDesc"/>
     	</k-form-item>
		<k-form-item label="客户实际年化收益率%错误">
        	<k-field-text v-model="formData.annualReturnClientDesc"/>
     	</k-form-item>
		<k-form-item label="产品实际年化收益率%错误">
        	<k-field-text v-model="formData.annualReturnProdDesc"/>
     	</k-form-item>
		<k-form-item label="登记流水号">
        	<k-field-text v-model="formData.registerSerno"/>
     	</k-form-item>
		<k-form-item label="导入日期">
        	<k-field-text v-model="formData.impDate"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="TerminationRgInfoErr.updateTerminationRgInfo" data-from="editTerminationRgInfoErrForm"
	        :data-model="formData" data-target="TerminationRgInfoErrGrid">
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
    name: "TerminationRgInfoErr",
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
