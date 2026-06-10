<template>
  <div class="py-page">
     <div>
         <k-form-search-customize data-model-name="ProdIssRgInfoErr" data-target="prodIssRgInfoErrGrid" data-label-width="80px" v-model = "searchParam">
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
      <k-grid ref="prodIssRgInfoErrGrid" @data-row-select="selectRow" data-operate-column="false" data-action="ProdIssRgInfoErr.findProdIssRgInfoErrs" >
		<k-grid-column data-header="登记流水号" data-name="registerSerno"></k-grid-column>
		<k-grid-column data-header="导入日期" data-name="impDate"></k-grid-column>
		<k-grid-column data-header="投资者登记日期" data-name="registerDate"></k-grid-column>
		<k-grid-column data-header="报送状态" data-name="registerStatus" data-dict="report_status"></k-grid-column>
		<k-grid-column data-header="产品登记编码错误信息" data-name="prodCodeDesc"></k-grid-column>
		<k-grid-column data-header="发行单位代码错误信息" data-name="bankCodeDesc"></k-grid-column>
		<k-grid-column data-header="理财产品代码错误信息" data-name="prodIdentCodeDesc"></k-grid-column>
		<k-grid-column data-header="募集起始日期错误信息" data-name="subscriptionStartDateDesc"></k-grid-column>
		<k-grid-column data-header="募集结束日期错误信息" data-name="subscriptionEndDateDesc"></k-grid-column>
		<k-grid-column data-header="产品起始日期错误信息" data-name="prodValueDateDesc"></k-grid-column>
		<k-grid-column data-header="产品终止日期错误信息" data-name="prodMaturityDateDesc"></k-grid-column>
		<k-grid-column data-header="管理方式错误信息" data-name="managementMethodDesc"></k-grid-column>
		<k-grid-column data-header="是否为结构化产品错误信息" data-name="structuredProdDesc"></k-grid-column>
		<k-grid-column data-header="业绩比较基准说明错误信息" data-name="detailsPerRateDesc"></k-grid-column>
		<k-grid-column data-header="开放模式错误信息" data-name="openingModeDesc"></k-grid-column>
		<k-grid-column data-header="业绩比较基准上限错误信息" data-name="upLimitPerRateDesc"></k-grid-column>
		<k-grid-column data-header="业绩比较基准下限)错误信息" data-name="lowLimitPerRateDesc"></k-grid-column>
		<k-grid-column data-header="规律开放周期错误信息" data-name="regularOpenPeriodDesc"></k-grid-column>
		<k-grid-column data-header="其他规律开发周期错误信息" data-name="otherOpenPeriodDesc"></k-grid-column>
		<k-grid-column data-header="无规律开放说明错误信息" data-name="disorderOpenPeriodDesc"></k-grid-column>
		<k-grid-column data-header="首次开放周期起始日错误信息" data-name="firstOpenDayDesc"></k-grid-column>
		<k-grid-column data-header="节假日是否开放错误信息" data-name="holidayOpenTypeDesc"></k-grid-column>
		<k-grid-column data-header="平均开放次数错误信息" data-name="averageOpenNoDesc"></k-grid-column>
		<k-grid-column data-header="开放期业务错误信息" data-name="busiOpenPeriodDesc"></k-grid-column>
		<k-grid-column data-header="开放期业务说明错误信息" data-name="detailsBusiOpPeriodDesc"></k-grid-column>
		<k-grid-column data-header="资金托管账号错误信息" data-name="custodyAcctNoDesc"></k-grid-column>
		<k-grid-column data-header="资金托管账户错误信息" data-name="custodyAcctNameDesc"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品发行登记错误信息" data-functype="POPUP" data-size="mini"
            data-target="editProdIssRgInfoErrPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="ProdIssRgInfoErr.deleteProdIssRgInfoErr" data-size="mini"
               data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true" data-descript="删除产品发行登记错误信息">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加产品发行登记错误信息弹出框   -->
	<k-popup ref="addProdIssRgInfoErrPopup" data-title="新增">
    	<k-form ref="addProdIssRgInfoErrForm" :data-col="2">
			<k-form-item label="登记流水号">
	        	<k-field-text v-model="formData.registerSerno"/>
	     	</k-form-item>
			<k-form-item label="导入日期">
	        	<k-field-text v-model="formData.impDate"/>
	     	</k-form-item>
			<k-form-item label="投资者登记日期">
	        	<k-field-text v-model="formData.registerDate"/>
	     	</k-form-item>
			<k-form-item label="登记状态">
	        	<k-field-select v-model="formData.registerStatus" data-dict="tr_register_status"/>
	     	</k-form-item>
			<k-form-item label="产品登记编码错误信息">
	        	<k-field-text v-model="formData.prodCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="发行单位代码错误信息">
	        	<k-field-text v-model="formData.bankCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="理财产品代码错误信息">
	        	<k-field-text v-model="formData.prodIdentCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="募集起始日期错误信息">
	        	<k-field-text v-model="formData.subscriptionStartDateDesc"/>
	     	</k-form-item>
			<k-form-item label="募集结束日期错误信息">
	        	<k-field-text v-model="formData.subscriptionEndDateDesc"/>
	     	</k-form-item>
			<k-form-item label="产品起始日期错误信息">
	        	<k-field-text v-model="formData.prodValueDateDesc"/>
	     	</k-form-item>
			<k-form-item label="产品终止日期错误信息">
	        	<k-field-text v-model="formData.prodMaturityDateDesc"/>
	     	</k-form-item>
			<k-form-item label="管理方式错误信息">
	        	<k-field-text v-model="formData.managementMethodDesc"/>
	     	</k-form-item>
			<k-form-item label="是否为结构化产品错误信息">
	        	<k-field-text v-model="formData.structuredProdDesc"/>
	     	</k-form-item>
			<k-form-item label="业绩比较基准说明错误信息">
	        	<k-field-text v-model="formData.detailsPerRateDesc"/>
	     	</k-form-item>
			<k-form-item label="开放模式错误信息">
	        	<k-field-text v-model="formData.openingModeDesc"/>
	     	</k-form-item>
			<k-form-item label="业绩比较基准上限错误信息">
	        	<k-field-text v-model="formData.upLimitPerRateDesc"/>
	     	</k-form-item>
			<k-form-item label="业绩比较基准下限)错误信息">
	        	<k-field-text v-model="formData.lowLimitPerRateDesc"/>
	     	</k-form-item>
			<k-form-item label="规律开放周期错误信息">
	        	<k-field-text v-model="formData.regularOpenPeriodDesc"/>
	     	</k-form-item>
			<k-form-item label="其他规律开发周期错误信息">
	        	<k-field-text v-model="formData.otherOpenPeriodDesc"/>
	     	</k-form-item>
			<k-form-item label="无规律开放说明错误信息">
	        	<k-field-text v-model="formData.disorderOpenPeriodDesc"/>
	     	</k-form-item>
			<k-form-item label="首次开放周期起始日错误信息">
	        	<k-field-text v-model="formData.firstOpenDayDesc"/>
	     	</k-form-item>
			<k-form-item label="节假日是否开放错误信息">
	        	<k-field-text v-model="formData.holidayOpenTypeDesc"/>
	     	</k-form-item>
			<k-form-item label="平均开放次数错误信息">
	        	<k-field-text v-model="formData.averageOpenNoDesc"/>
	     	</k-form-item>
			<k-form-item label="开放期业务错误信息">
	        	<k-field-text v-model="formData.busiOpenPeriodDesc"/>
	     	</k-form-item>
			<k-form-item label="开放期业务说明错误信息">
	        	<k-field-text v-model="formData.detailsBusiOpPeriodDesc"/>
	     	</k-form-item>
			<k-form-item label="资金托管账号错误信息">
	        	<k-field-text v-model="formData.custodyAcctNoDesc"/>
	     	</k-form-item>
			<k-form-item label="资金托管账户错误信息">
	        	<k-field-text v-model="formData.custodyAcctNameDesc"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ProdIssRgInfoErr.addProdIssRgInfoErr" data-from="addProdIssRgInfoErrForm"
		               :data-model="formData" data-target="prodIssRgInfoErrGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改产品发行登记错误信息弹出框   -->
	<k-popup ref="editProdIssRgInfoErrPopup" data-title="修改">
	  <k-form ref="editProdIssRgInfoErrForm" :data-col="2">
		<k-form-item label="登记流水号">
        	<k-field-text v-model="formData.registerSerno"/>
     	</k-form-item>
		<k-form-item label="导入日期">
        	<k-field-text v-model="formData.impDate"/>
     	</k-form-item>
		<k-form-item label="投资者登记日期">
        	<k-field-text v-model="formData.registerDate"/>
     	</k-form-item>
		<k-form-item label="登记状态">
        	<k-field-select v-model="formData.registerStatus" data-dict="tr_register_status"/>
     	</k-form-item>
		<k-form-item label="产品登记编码错误信息">
        	<k-field-text v-model="formData.prodCodeDesc"/>
     	</k-form-item>
		<k-form-item label="发行单位代码错误信息">
        	<k-field-text v-model="formData.bankCodeDesc"/>
     	</k-form-item>
		<k-form-item label="理财产品代码错误信息">
        	<k-field-text v-model="formData.prodIdentCodeDesc"/>
     	</k-form-item>
		<k-form-item label="募集起始日期错误信息">
        	<k-field-text v-model="formData.subscriptionStartDateDesc"/>
     	</k-form-item>
		<k-form-item label="募集结束日期错误信息">
        	<k-field-text v-model="formData.subscriptionEndDateDesc"/>
     	</k-form-item>
		<k-form-item label="产品起始日期错误信息">
        	<k-field-text v-model="formData.prodValueDateDesc"/>
     	</k-form-item>
		<k-form-item label="产品终止日期错误信息">
        	<k-field-text v-model="formData.prodMaturityDateDesc"/>
     	</k-form-item>
		<k-form-item label="管理方式错误信息">
        	<k-field-text v-model="formData.managementMethodDesc"/>
     	</k-form-item>
		<k-form-item label="是否为结构化产品错误信息">
        	<k-field-text v-model="formData.structuredProdDesc"/>
     	</k-form-item>
		<k-form-item label="业绩比较基准说明错误信息">
        	<k-field-text v-model="formData.detailsPerRateDesc"/>
     	</k-form-item>
		<k-form-item label="开放模式错误信息">
        	<k-field-text v-model="formData.openingModeDesc"/>
     	</k-form-item>
		<k-form-item label="业绩比较基准上限错误信息">
        	<k-field-text v-model="formData.upLimitPerRateDesc"/>
     	</k-form-item>
		<k-form-item label="业绩比较基准下限)错误信息">
        	<k-field-text v-model="formData.lowLimitPerRateDesc"/>
     	</k-form-item>
		<k-form-item label="规律开放周期错误信息">
        	<k-field-text v-model="formData.regularOpenPeriodDesc"/>
     	</k-form-item>
		<k-form-item label="其他规律开发周期错误信息">
        	<k-field-text v-model="formData.otherOpenPeriodDesc"/>
     	</k-form-item>
		<k-form-item label="无规律开放说明错误信息">
        	<k-field-text v-model="formData.disorderOpenPeriodDesc"/>
     	</k-form-item>
		<k-form-item label="首次开放周期起始日错误信息">
        	<k-field-text v-model="formData.firstOpenDayDesc"/>
     	</k-form-item>
		<k-form-item label="节假日是否开放错误信息">
        	<k-field-text v-model="formData.holidayOpenTypeDesc"/>
     	</k-form-item>
		<k-form-item label="平均开放次数错误信息">
        	<k-field-text v-model="formData.averageOpenNoDesc"/>
     	</k-form-item>
		<k-form-item label="开放期业务错误信息">
        	<k-field-text v-model="formData.busiOpenPeriodDesc"/>
     	</k-form-item>
		<k-form-item label="开放期业务说明错误信息">
        	<k-field-text v-model="formData.detailsBusiOpPeriodDesc"/>
     	</k-form-item>
		<k-form-item label="资金托管账号错误信息">
        	<k-field-text v-model="formData.custodyAcctNoDesc"/>
     	</k-form-item>
		<k-form-item label="资金托管账户错误信息">
        	<k-field-text v-model="formData.custodyAcctNameDesc"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ProdIssRgInfoErr.updateProdIssRgInfoErr" data-from="editProdIssRgInfoErrForm"
	        :data-model="formData" data-target="prodIssRgInfoErrGrid">
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
    name: "ProdIssRgInfoErr",
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
