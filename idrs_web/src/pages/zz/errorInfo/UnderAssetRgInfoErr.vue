<template>
  <div class="py-page">
     <div>
         <k-form-search-customize data-model-name="UnderAssetRgInfoErr" data-target="UnderAssetRgInfoErrGrid" data-label-width="80px" v-model = "searchParam">
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
      <k-grid ref="UnderAssetRgInfoErrGrid" @data-row-select="selectRow" data-operate-column="false" data-action="UnderAssetRgInfoErr.findUnderAssetRgInfos" >
		<k-grid-column data-header="发行机构代码错误" data-name="bankCode"></k-grid-column>
		<k-grid-column data-header="对应资管及委外资产行内资产/负债编码错误" data-name="assetManagerCode"></k-grid-column>
		<k-grid-column data-header="资管及委外资产当前总折算人民币金额" data-name="convertSumAmt"></k-grid-column>
		<k-grid-column data-header="资管及委外资产当前总数量错误" data-name="assetSumNumber"></k-grid-column>
		<k-grid-column data-header="资管及委外资产未投资头寸" data-name="nonInvestedAmt"></k-grid-column>
		<k-grid-column data-header="底层资产行内资产/负债编码错误" data-name="underAssetCode"></k-grid-column>
		<k-grid-column data-header="底层资产持仓数量错误" data-name="underAssetSum"></k-grid-column>
		<k-grid-column data-header="底层资产折算人民币市值" data-name="underConvertSumAmt"></k-grid-column>
		<k-grid-column data-header="持仓日期错误" data-name="reportDate"></k-grid-column>
		<k-grid-column data-header="登记流水号" data-name="registerSerno"></k-grid-column>
		<k-grid-column data-header="导入日期" data-name="impDate"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改底层资产错误信息" data-functype="POPUP" data-size="mini"
            data-target="editUnderAssetRgInfoErrPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="UnderAssetRgInfoErr.deleteUnderAssetRgInfo" data-size="mini"
               data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true" data-descript="删除底层资产错误信息">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加底层资产错误信息弹出框   -->
	<k-popup ref="addUnderAssetRgInfoErrPopup" data-title="新增">
    	<k-form ref="addUnderAssetRgInfoErrForm" :data-col="2">
			<k-form-item label="发行机构代码错误">
	        	<k-field-text v-model="formData.bankCode"/>
	     	</k-form-item>
			<k-form-item label="对应资管及委外资产行内资产/负债编码错误">
	        	<k-field-text v-model="formData.assetManagerCode"/>
	     	</k-form-item>
			<k-form-item label="资管及委外资产当前总折算人民币金额">
	        	<k-field-text v-model="formData.convertSumAmt"/>
	     	</k-form-item>
			<k-form-item label="资管及委外资产当前总数量错误">
	        	<k-field-text v-model="formData.assetSumNumber"/>
	     	</k-form-item>
			<k-form-item label="资管及委外资产未投资头寸">
	        	<k-field-text v-model="formData.nonInvestedAmt"/>
	     	</k-form-item>
			<k-form-item label="底层资产行内资产/负债编码错误">
	        	<k-field-text v-model="formData.underAssetCode"/>
	     	</k-form-item>
			<k-form-item label="底层资产持仓数量错误">
	        	<k-field-text v-model="formData.underAssetSum"/>
	     	</k-form-item>
			<k-form-item label="底层资产折算人民币市值">
	        	<k-field-text v-model="formData.underConvertSumAmt"/>
	     	</k-form-item>
			<k-form-item label="持仓日期错误">
	        	<k-field-text v-model="formData.reportDate"/>
	     	</k-form-item>
			<k-form-item label="登记流水号">
	        	<k-field-text v-model="formData.registerSerno"/>
	     	</k-form-item>
			<k-form-item label="导入日期">
	        	<k-field-text v-model="formData.impDate"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="UnderAssetRgInfoErr.addUnderAssetRgInfo" data-from="addUnderAssetRgInfoErrForm"
		               :data-model="formData" data-target="UnderAssetRgInfoErrGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改底层资产错误信息弹出框   -->
	<k-popup ref="editUnderAssetRgInfoErrPopup" data-title="修改">
	  <k-form ref="editUnderAssetRgInfoErrForm" :data-col="2">
		<k-form-item label="发行机构代码错误">
        	<k-field-text v-model="formData.bankCode"/>
     	</k-form-item>
		<k-form-item label="对应资管及委外资产行内资产/负债编码错误">
        	<k-field-text v-model="formData.assetManagerCode"/>
     	</k-form-item>
		<k-form-item label="资管及委外资产当前总折算人民币金额">
        	<k-field-text v-model="formData.convertSumAmt"/>
     	</k-form-item>
		<k-form-item label="资管及委外资产当前总数量错误">
        	<k-field-text v-model="formData.assetSumNumber"/>
     	</k-form-item>
		<k-form-item label="资管及委外资产未投资头寸">
        	<k-field-text v-model="formData.nonInvestedAmt"/>
     	</k-form-item>
		<k-form-item label="底层资产行内资产/负债编码错误">
        	<k-field-text v-model="formData.underAssetCode"/>
     	</k-form-item>
		<k-form-item label="底层资产持仓数量错误">
        	<k-field-text v-model="formData.underAssetSum"/>
     	</k-form-item>
		<k-form-item label="底层资产折算人民币市值">
        	<k-field-text v-model="formData.underConvertSumAmt"/>
     	</k-form-item>
		<k-form-item label="持仓日期错误">
        	<k-field-text v-model="formData.reportDate"/>
     	</k-form-item>
		<k-form-item label="登记流水号">
        	<k-field-text v-model="formData.registerSerno"/>
     	</k-form-item>
		<k-form-item label="导入日期">
        	<k-field-text v-model="formData.impDate"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="UnderAssetRgInfoErr.updateUnderAssetRgInfo" data-from="editUnderAssetRgInfoErrForm"
	        :data-model="formData" data-target="UnderAssetRgInfoErrGrid">
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
    name: "UnderAssetRgInfoErr",
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
