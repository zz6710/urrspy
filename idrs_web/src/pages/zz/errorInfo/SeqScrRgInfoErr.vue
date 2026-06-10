<template>
  <div class="py-page">
    <div>
        <k-form-search-customize data-model-name="SeqScrRgInfoErr" data-target="SeqScrRgInfoErrGrid" data-label-width="80px" v-model = "searchParam">
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
      <k-grid ref="SeqScrRgInfoErrGrid" @data-row-select="selectRow" data-operate-column="false" data-action="SeqScrRgInfoErr.findSeqScrRgInfos" >
		<k-grid-column data-header="发行机构代码错误" data-name="bankCode"></k-grid-column>
		<k-grid-column data-header="产品登记编码错误" data-name="prodCode"></k-grid-column>
		<k-grid-column data-header="初始净值错误" data-name="initialNav"></k-grid-column>
		<k-grid-column data-header="产品净值错误" data-name="nav"></k-grid-column>
		<k-grid-column data-header="累计净值错误" data-name="aggregateNav"></k-grid-column>
		<k-grid-column data-header="净值币种错误" data-name="navCur"></k-grid-column>
		<k-grid-column data-header="折算人民币净值错误" data-name="convertRmbNav"></k-grid-column>
		<k-grid-column data-header="折算人民币累计净值错误" data-name="convertRmbAggNav"></k-grid-column>
		<k-grid-column data-header="实现收益率%错误" data-name="realizedAnnualReturn"></k-grid-column>
		<k-grid-column data-header="最新预期收益率%错误" data-name="expectedAnnualReturn"></k-grid-column>
		<k-grid-column data-header="银行实现收益" data-name="inconmeBank"></k-grid-column>
		<k-grid-column data-header="业务起始日错误" data-name="businessStartDate"></k-grid-column>
		<k-grid-column data-header="业务结束日错误" data-name="businessEndDate"></k-grid-column>
		<k-grid-column data-header="币种错误" data-name="cur"></k-grid-column>
		<k-grid-column data-header="该币种累计申购金额" data-name="subAmtLassPeriod"></k-grid-column>
		<k-grid-column data-header="该币种累计兑付金额" data-name="curPrincipalPeriod"></k-grid-column>
		<k-grid-column data-header="该币种累计兑付收益金额" data-name="curPayPeriod"></k-grid-column>
		<k-grid-column data-header="累计申购份额错误" data-name="subscribedLatestVol"></k-grid-column>
		<k-grid-column data-header="累计赎回份额错误" data-name="redeemedLatestVol"></k-grid-column>
		<k-grid-column data-header="每万份份额分红错误" data-name="unitsBonus"></k-grid-column>
		<k-grid-column data-header="每万份现金分红错误" data-name="cashBonus"></k-grid-column>
		<k-grid-column data-header="产品余额错误" data-name="prodAmt"></k-grid-column>
		<k-grid-column data-header="产品份额错误" data-name="prodVol"></k-grid-column>
		<k-grid-column data-header="备注错误" data-name="details"></k-grid-column>
		<k-grid-column data-header="折算人民币初始净值错误" data-name="convertInitialNav"></k-grid-column>
		<k-grid-column data-header="登记流水号" data-name="registerSerno"></k-grid-column>
		<k-grid-column data-header="导入日期" data-name="impDate"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品存续期错误信息" data-functype="POPUP" data-size="mini"
            data-target="editSeqScrRgInfoErrPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="SeqScrRgInfoErr.deleteSeqScrRgInfo" data-size="mini"
               data-type="danger" data-target="SeqScrRgInfoErrGrid" :data-confirm="true" data-descript="删除产品存续期错误信息">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加产品存续期错误信息弹出框   -->
	<k-popup ref="addSeqScrRgInfoErrPopup" data-title="新增">
    	<k-form ref="addSeqScrRgInfoErrForm" :data-col="2">
			<k-form-item label="发行机构代码错误">
	        	<k-field-text v-model="formData.bankCode"/>
	     	</k-form-item>
			<k-form-item label="产品登记编码错误">
	        	<k-field-text v-model="formData.prodCode"/>
	     	</k-form-item>
			<k-form-item label="初始净值错误">
	        	<k-field-text v-model="formData.initialNav"/>
	     	</k-form-item>
			<k-form-item label="产品净值错误">
	        	<k-field-text v-model="formData.nav"/>
	     	</k-form-item>
			<k-form-item label="累计净值错误">
	        	<k-field-text v-model="formData.aggregateNav"/>
	     	</k-form-item>
			<k-form-item label="净值币种错误">
	        	<k-field-text v-model="formData.navCur"/>
	     	</k-form-item>
			<k-form-item label="折算人民币净值错误">
	        	<k-field-text v-model="formData.convertRmbNav"/>
	     	</k-form-item>
			<k-form-item label="折算人民币累计净值错误">
	        	<k-field-text v-model="formData.convertRmbAggNav"/>
	     	</k-form-item>
			<k-form-item label="实现收益率%错误">
	        	<k-field-text v-model="formData.realizedAnnualReturn"/>
	     	</k-form-item>
			<k-form-item label="最新预期收益率%错误">
	        	<k-field-text v-model="formData.expectedAnnualReturn"/>
	     	</k-form-item>
			<k-form-item label="银行实现收益">
	        	<k-field-text v-model="formData.inconmeBank"/>
	     	</k-form-item>
			<k-form-item label="业务起始日错误">
	        	<k-field-text v-model="formData.businessStartDate"/>
	     	</k-form-item>
			<k-form-item label="业务结束日错误">
	        	<k-field-text v-model="formData.businessEndDate"/>
	     	</k-form-item>
			<k-form-item label="币种错误">
	        	<k-field-text v-model="formData.cur"/>
	     	</k-form-item>
			<k-form-item label="该币种累计申购金额">
	        	<k-field-text v-model="formData.subAmtLassPeriod"/>
	     	</k-form-item>
			<k-form-item label="该币种累计兑付金额">
	        	<k-field-text v-model="formData.curPrincipalPeriod"/>
	     	</k-form-item>
			<k-form-item label="该币种累计兑付收益金额">
	        	<k-field-text v-model="formData.curPayPeriod"/>
	     	</k-form-item>
			<k-form-item label="累计申购份额错误">
	        	<k-field-text v-model="formData.subscribedLatestVol"/>
	     	</k-form-item>
			<k-form-item label="累计赎回份额错误">
	        	<k-field-text v-model="formData.redeemedLatestVol"/>
	     	</k-form-item>
			<k-form-item label="每万份份额分红错误">
	        	<k-field-text v-model="formData.unitsBonus"/>
	     	</k-form-item>
			<k-form-item label="每万份现金分红错误">
	        	<k-field-text v-model="formData.cashBonus"/>
	     	</k-form-item>
			<k-form-item label="产品余额错误">
	        	<k-field-text v-model="formData.prodAmt"/>
	     	</k-form-item>
			<k-form-item label="产品份额错误">
	        	<k-field-text v-model="formData.prodVol"/>
	     	</k-form-item>
			<k-form-item label="备注错误">
	        	<k-field-text v-model="formData.details"/>
	     	</k-form-item>
			<k-form-item label="折算人民币初始净值错误">
	        	<k-field-text v-model="formData.convertInitialNav"/>
	     	</k-form-item>
			<k-form-item label="登记流水号">
	        	<k-field-text v-model="formData.registerSerno"/>
	     	</k-form-item>
			<k-form-item label="导入日期">
	        	<k-field-text v-model="formData.impDate"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="SeqScrRgInfoErr.addSeqScrRgInfo" data-from="addSeqScrRgInfoErrForm"
		               :data-model="formData" data-target="SeqScrRgInfoErrGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改产品存续期错误信息弹出框   -->
	<k-popup ref="editSeqScrRgInfoErrPopup" data-title="修改">
	  <k-form ref="editSeqScrRgInfoErrForm" :data-col="2">
		<k-form-item label="发行机构代码错误">
        	<k-field-text v-model="formData.bankCode"/>
     	</k-form-item>
		<k-form-item label="产品登记编码错误">
        	<k-field-text v-model="formData.prodCode"/>
     	</k-form-item>
		<k-form-item label="初始净值错误">
        	<k-field-text v-model="formData.initialNav"/>
     	</k-form-item>
		<k-form-item label="产品净值错误">
        	<k-field-text v-model="formData.nav"/>
     	</k-form-item>
		<k-form-item label="累计净值错误">
        	<k-field-text v-model="formData.aggregateNav"/>
     	</k-form-item>
		<k-form-item label="净值币种错误">
        	<k-field-text v-model="formData.navCur"/>
     	</k-form-item>
		<k-form-item label="折算人民币净值错误">
        	<k-field-text v-model="formData.convertRmbNav"/>
     	</k-form-item>
		<k-form-item label="折算人民币累计净值错误">
        	<k-field-text v-model="formData.convertRmbAggNav"/>
     	</k-form-item>
		<k-form-item label="实现收益率%错误">
        	<k-field-text v-model="formData.realizedAnnualReturn"/>
     	</k-form-item>
		<k-form-item label="最新预期收益率%错误">
        	<k-field-text v-model="formData.expectedAnnualReturn"/>
     	</k-form-item>
		<k-form-item label="银行实现收益">
        	<k-field-text v-model="formData.inconmeBank"/>
     	</k-form-item>
		<k-form-item label="业务起始日错误">
        	<k-field-text v-model="formData.businessStartDate"/>
     	</k-form-item>
		<k-form-item label="业务结束日错误">
        	<k-field-text v-model="formData.businessEndDate"/>
     	</k-form-item>
		<k-form-item label="币种错误">
        	<k-field-text v-model="formData.cur"/>
     	</k-form-item>
		<k-form-item label="该币种累计申购金额">
        	<k-field-text v-model="formData.subAmtLassPeriod"/>
     	</k-form-item>
		<k-form-item label="该币种累计兑付金额">
        	<k-field-text v-model="formData.curPrincipalPeriod"/>
     	</k-form-item>
		<k-form-item label="该币种累计兑付收益金额">
        	<k-field-text v-model="formData.curPayPeriod"/>
     	</k-form-item>
		<k-form-item label="累计申购份额错误">
        	<k-field-text v-model="formData.subscribedLatestVol"/>
     	</k-form-item>
		<k-form-item label="累计赎回份额错误">
        	<k-field-text v-model="formData.redeemedLatestVol"/>
     	</k-form-item>
		<k-form-item label="每万份份额分红错误">
        	<k-field-text v-model="formData.unitsBonus"/>
     	</k-form-item>
		<k-form-item label="每万份现金分红错误">
        	<k-field-text v-model="formData.cashBonus"/>
     	</k-form-item>
		<k-form-item label="产品余额错误">
        	<k-field-text v-model="formData.prodAmt"/>
     	</k-form-item>
		<k-form-item label="产品份额错误">
        	<k-field-text v-model="formData.prodVol"/>
     	</k-form-item>
		<k-form-item label="备注错误">
        	<k-field-text v-model="formData.details"/>
     	</k-form-item>
		<k-form-item label="折算人民币初始净值错误">
        	<k-field-text v-model="formData.convertInitialNav"/>
     	</k-form-item>
		<k-form-item label="登记流水号">
        	<k-field-text v-model="formData.registerSerno"/>
     	</k-form-item>
		<k-form-item label="导入日期">
        	<k-field-text v-model="formData.impDate"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="SeqScrRgInfoErr.updateSeqScrRgInfo" data-from="editSeqScrRgInfoErrForm"
	        :data-model="formData" data-target="SeqScrRgInfoErrGrid">
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
    name: "SeqScrRgInfoErr",
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
