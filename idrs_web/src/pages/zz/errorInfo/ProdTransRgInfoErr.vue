<template>
  <div class="py-page">
      <div>
          <k-form-search-customize data-model-name="ProdTransRgInfoErr" data-target="prodTransRgInfoErrGrid" data-label-width="80px" v-model = "searchParam">
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
      <k-grid ref="prodTransRgInfoErrGrid" @data-row-select="selectRow" data-operate-column="false" data-action="ProdTransRgInfoErr.findProdTransRgInfoErrs" >
		<k-grid-column data-header="发行机构代码错误" data-name="bankCodeDesc"></k-grid-column>
		<k-grid-column data-header="产品登记编码错误" data-name="prodCodeDesc"></k-grid-column>
		<k-grid-column data-header="行内交易编码错误" data-name="transCodeDesc"></k-grid-column>
		<k-grid-column data-header="资产负债编码错误" data-name="assetCodeDesc"></k-grid-column>
		<k-grid-column data-header="币种错误" data-name="curDesc"></k-grid-column>
		<k-grid-column data-header="发生金额错误" data-name="amtDesc"></k-grid-column>
		<k-grid-column data-header="折算人民币金额" data-name="convertRmbDesc"></k-grid-column>
		<k-grid-column data-header="数量错误" data-name="quantityDesc"></k-grid-column>
		<k-grid-column data-header="资产计量方式错误" data-name="methodAssetMeasureDesc"></k-grid-column>
		<k-grid-column data-header="资金流动类型错误" data-name="cashTypeDesc"></k-grid-column>
		<k-grid-column data-header="资金流动类型说明错误" data-name="detailCashTypeDesc"></k-grid-column>
		<k-grid-column data-header="交易日错误" data-name="tradeDateDesc"></k-grid-column>
		<k-grid-column data-header="交易对手方错误" data-name="tradeCounterDesc"></k-grid-column>
		<k-grid-column data-header="交易对手方类型错误" data-name="counterTypeDesc"></k-grid-column>
		<k-grid-column data-header="单位成交价格" data-name="unitPriceFullDesc"></k-grid-column>
		<k-grid-column data-header="单位成交价格" data-name="unitPriceNetDesc"></k-grid-column>
		<k-grid-column data-header="到期收益率%错误" data-name="rateAnnualReturnDesc"></k-grid-column>
		<k-grid-column data-header="成交编号/合同号错误" data-name="transIdentCodeDesc"></k-grid-column>
		<k-grid-column data-header="备注错误" data-name="detailsDesc"></k-grid-column>
		<k-grid-column data-header="登记流水号" data-name="registerSerno"></k-grid-column>
		<k-grid-column data-header="导入日期" data-name="impDate"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改交易信息错误" data-functype="POPUP" data-size="mini"
            data-target="editProdTransRgInfoErrPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="ProdTransRgInfoErr.deleteProdTransRgInfoErr" data-size="mini"
               data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true" data-descript="删除交易信息错误">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加交易信息错误弹出框   -->
	<k-popup ref="addProdTransRgInfoErrPopup" data-title="新增">
    	<k-form ref="addProdTransRgInfoErrForm" :data-col="2">
			<k-form-item label="发行机构代码错误">
	        	<k-field-text v-model="formData.bankCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="产品登记编码错误">
	        	<k-field-text v-model="formData.prodCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="行内交易编码错误">
	        	<k-field-text v-model="formData.transCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="资产负债编码错误">
	        	<k-field-text v-model="formData.assetCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="币种错误">
	        	<k-field-text v-model="formData.curDesc"/>
	     	</k-form-item>
			<k-form-item label="发生金额错误">
	        	<k-field-text v-model="formData.amtDesc"/>
	     	</k-form-item>
			<k-form-item label="折算人民币金额">
	        	<k-field-text v-model="formData.convertRmbDesc"/>
	     	</k-form-item>
			<k-form-item label="数量错误">
	        	<k-field-text v-model="formData.quantityDesc"/>
	     	</k-form-item>
			<k-form-item label="资产计量方式错误">
	        	<k-field-text v-model="formData.methodAssetMeasureDesc"/>
	     	</k-form-item>
			<k-form-item label="资金流动类型错误">
	        	<k-field-text v-model="formData.cashTypeDesc"/>
	     	</k-form-item>
			<k-form-item label="资金流动类型说明错误">
	        	<k-field-text v-model="formData.detailCashTypeDesc"/>
	     	</k-form-item>
			<k-form-item label="交易日错误">
	        	<k-field-text v-model="formData.tradeDateDesc"/>
	     	</k-form-item>
			<k-form-item label="交易对手方错误">
	        	<k-field-text v-model="formData.tradeCounterDesc"/>
	     	</k-form-item>
			<k-form-item label="交易对手方类型错误">
	        	<k-field-text v-model="formData.counterTypeDesc"/>
	     	</k-form-item>
			<k-form-item label="单位成交价格">
	        	<k-field-text v-model="formData.unitPriceFullDesc"/>
	     	</k-form-item>
			<k-form-item label="单位成交价格">
	        	<k-field-text v-model="formData.unitPriceNetDesc"/>
	     	</k-form-item>
			<k-form-item label="到期收益率%错误">
	        	<k-field-text v-model="formData.rateAnnualReturnDesc"/>
	     	</k-form-item>
			<k-form-item label="成交编号/合同号错误">
	        	<k-field-text v-model="formData.transIdentCodeDesc"/>
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
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ProdTransRgInfoErr.addProdTransRgInfoErr" data-from="addProdTransRgInfoErrForm"
		               :data-model="formData" data-target="prodTransRgInfoErrGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改交易信息错误弹出框   -->
	<k-popup ref="editProdTransRgInfoErrPopup" data-title="修改">
	  <k-form ref="editProdTransRgInfoErrForm" :data-col="2">
		<k-form-item label="发行机构代码错误">
        	<k-field-text v-model="formData.bankCodeDesc"/>
     	</k-form-item>
		<k-form-item label="产品登记编码错误">
        	<k-field-text v-model="formData.prodCodeDesc"/>
     	</k-form-item>
		<k-form-item label="行内交易编码错误">
        	<k-field-text v-model="formData.transCodeDesc"/>
     	</k-form-item>
		<k-form-item label="资产负债编码错误">
        	<k-field-text v-model="formData.assetCodeDesc"/>
     	</k-form-item>
		<k-form-item label="币种错误">
        	<k-field-text v-model="formData.curDesc"/>
     	</k-form-item>
		<k-form-item label="发生金额错误">
        	<k-field-text v-model="formData.amtDesc"/>
     	</k-form-item>
		<k-form-item label="折算人民币金额">
        	<k-field-text v-model="formData.convertRmbDesc"/>
     	</k-form-item>
		<k-form-item label="数量错误">
        	<k-field-text v-model="formData.quantityDesc"/>
     	</k-form-item>
		<k-form-item label="资产计量方式错误">
        	<k-field-text v-model="formData.methodAssetMeasureDesc"/>
     	</k-form-item>
		<k-form-item label="资金流动类型错误">
        	<k-field-text v-model="formData.cashTypeDesc"/>
     	</k-form-item>
		<k-form-item label="资金流动类型说明错误">
        	<k-field-text v-model="formData.detailCashTypeDesc"/>
     	</k-form-item>
		<k-form-item label="交易日错误">
        	<k-field-text v-model="formData.tradeDateDesc"/>
     	</k-form-item>
		<k-form-item label="交易对手方错误">
        	<k-field-text v-model="formData.tradeCounterDesc"/>
     	</k-form-item>
		<k-form-item label="交易对手方类型错误">
        	<k-field-text v-model="formData.counterTypeDesc"/>
     	</k-form-item>
		<k-form-item label="单位成交价格">
        	<k-field-text v-model="formData.unitPriceFullDesc"/>
     	</k-form-item>
		<k-form-item label="单位成交价格">
        	<k-field-text v-model="formData.unitPriceNetDesc"/>
     	</k-form-item>
		<k-form-item label="到期收益率%错误">
        	<k-field-text v-model="formData.rateAnnualReturnDesc"/>
     	</k-form-item>
		<k-form-item label="成交编号/合同号错误">
        	<k-field-text v-model="formData.transIdentCodeDesc"/>
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
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ProdTransRgInfoErr.updateProdTransRgInfoErr" data-from="editProdTransRgInfoErrForm"
	        :data-model="formData" data-target="prodTransRgInfoErrGrid">
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
    name: "ProdTransRgInfoErr",
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
