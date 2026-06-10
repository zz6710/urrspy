<template>
  <div class="py-page">
    <div>
		<k-form-search-customize ref="searchRef" data-model-name="SpvInvestAmountInfo" data-label-width="130px" v-model="searchParam" data-target="tableGrid">
			<k-form-item label="数据日期">
      					<k-field-date
      						v-model="searchParam.reportDate"
      						data-type="month"
      						data-date-format="yyyy-MM"
      						data-value-format="yyyyMM"
      						:data-allowblank="false"
      					/>
      				</k-form-item>
      <k-form-item label="特定目的载体类型">
      					<k-field-select v-model="searchParam.specificAimType" data-dict="spv_type" data-dict-type="1" />
      </k-form-item>
      <k-form-item label="特定目的载体代码">
      					<k-field-text v-model="searchParam.specificAimCode" />
            </k-form-item>
      <k-form-item label="交易方向">
            					<k-field-select v-model="searchParam.tradeDire" data-dict="spv_trade_dire" data-dict-type="1" />
            </k-form-item>
		</k-form-search-customize>
    </div>
    <div class="py-page-container">
		<div class="table-top-btns">
			<div class="left">
          <k-btn ref="assetSend" slot="button" class="btn-custom-plain" data-size="small" @click="creatZipFile('SPVFSX')">
            <md-icon>cloud_download</md-icon>
            数据报送
          </k-btn>
          <k-btn
            slot="button"
            class="btn-custom-plain"
            data-target="tableGrid"
            :data-export-name="getFileName('SPVFSX')"
            data-functype="EXPORT"
						data-export-form="searchRef"
          >
            <md-icon>cloud_download</md-icon>
            报送数据导出
          </k-btn>
          <k-btn slot="button" data-functype="POPUP" class="btn-custom-plain" data-target="addPopup">
            <md-icon>cloud_upload</md-icon>
            报送数据导入
          </k-btn>
      </div>
		</div>
    <k-grid
      ref="tableGrid"
      @data-row-select="selectRow"
      data-fixed="right"
      data-action="SpvInvestAmountInfo.findSpvInvestAmountInfos"
      data-operate-width="120px"
      :data-autoload="false"
      data-dict-type="1"
    >
		<k-grid-column data-header="数据日期" data-name="reportDate" data-width="120" data-export="false"></k-grid-column>
		<k-grid-column data-header="金融机构代码" data-name="orgCode" data-width="130"></k-grid-column>
		<k-grid-column data-header="内部机构号" data-name="innerOrgCode" data-width="150"></k-grid-column>
		<k-grid-column data-header="特定目的载体类型" data-name="specificAimType" data-dict="spv_type" data-width="130"></k-grid-column>
		<k-grid-column data-header="资管产品统计编码" data-name="productCode" data-width="150"></k-grid-column>
		<k-grid-column data-header="特定目的载体代码" data-name="specificAimCode" data-width="150"></k-grid-column>
		<k-grid-column data-header="发行人代码" data-name="issuerCode" data-width="150"></k-grid-column>
		<k-grid-column data-header="发行人地区代码" data-name="issuerArearCode" data-width="130">
      <template slot-scope="scope">
						{{ getRegionText(scope.row.row.issuerArearCode) }}
					</template>
    </k-grid-column>
		<k-grid-column data-header="运行方式" data-name="rnnCode" data-dict="spv_run_mode" data-width="100"></k-grid-column>
		<k-grid-column data-header="认购日期" data-name="subDate" data-width="100"></k-grid-column>
		<k-grid-column data-header="到期日期" data-name="endDate" data-width="100"></k-grid-column>
		<k-grid-column data-header="交易日期" data-name="tradeDate" data-width="100"></k-grid-column>
		<k-grid-column data-header="币种" data-name="cur" data-width="100"></k-grid-column>
		<k-grid-column data-header="交易金额" data-name="tradeAmount" data-width="140"></k-grid-column>
		<k-grid-column data-header="交易金额折人民币" data-name="tradeAmountRmb" data-width="140"></k-grid-column>
		<k-grid-column data-header="交易方向" data-name="tradeDire" data-dict="spv_trade_dire" data-width="100"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改特定目的载体投资发生额信息" data-functype="POPUP" data-size="mini"
            data-target="editPopup">
			  修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="SpvInvestAmountInfo.deleteSpvInvestAmountInfo" data-size="mini"
               data-type="danger" data-target="tableGrid" :data-confirm="true" data-descript="删除特定目的载体投资发生额信息">
			  删除
    	  </k-btn>
        </template>
      </k-grid>
    </div>

  <!--    特定目的载体投资发生额信息数据导入   -->
	<k-popup ref="addPopup" title="报送数据导入" @data-opened="uploadOpened()">
  			<k-form ref="addForm" data-ui="element">
  				<k-form-item label="数据日期">
  					<k-field-date v-model="reportDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyy-MM" :data-allowblank="false" />
  				</k-form-item>
  				<k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
  					<k-field-excel-upload
  						data-type="file"
  						ref="uploadRef"
  						:data-multiple="false"
  						:data-limit="1"
  						data-accept=".xlsx,.xls"
  						:data-error="onSubmitError"
  						:data-success="onSubmitSuccess"
  						:data-auto-upload="false"
  						data-upload-url="/upload/server/RptApp/rhzy/uploadSpvInvestAmountInfo.json"
  					>
  					</k-field-excel-upload>
  				</k-form-item>
  				<k-form-footer data-align="center">
  					<k-btn
  						class="btn-custom-primary"
  						data-functype="SUBMIT"
  						data-target="tableGrid"
  						ref="submitBtn"
  						:data-auto-upload="false"
  						data-from="addForm"
  						:data-handler="submitUploadParam"
  						>确定
  					</k-btn>
  					<k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
  				</k-form-footer>
  			</k-form>
  		</k-popup>

	<!--    修改特定目的载体投资发生额信息弹出框   -->
	<k-popup ref="editPopup" data-title="修改">
	  <k-form ref="editForm" :data-col="2">
		<k-form-item label="金融机构代码">
        	<k-field-text v-model="formData.orgCode" :data-allowblank="false" />
     	</k-form-item>
		<k-form-item label="内部机构号">
        	<k-field-text v-model="formData.innerOrgCode" :data-allowblank="false" />
     	</k-form-item>
    <k-form-item label="特定目的载体类型">
    			<k-field-select v-model="formData.specificAimType" data-dict="spv_type" :data-allowblank="false" data-dict-type="1" />
    	</k-form-item>
		<k-form-item label="资管产品统计编码">
        	<k-field-text v-model="formData.productCode" :data-allowblank="false" />
     	</k-form-item>
		<k-form-item label="特定目的载体代码">
        	<k-field-text v-model="formData.specificAimCode" :data-allowblank="false" />
     	</k-form-item>
		<k-form-item label="发行人代码">
        	<k-field-text v-model="formData.issuerCode" :data-allowblank="false" />
     	</k-form-item>
		<k-form-item label="发行人地区代码">
          <k-field-select
            v-model="formData.issuerArearCode"
            :data-allowblank="false"
            :data-data="regionList"
            data-value-field="VALUE"
            data-display-field="TEXT"
            data-dict-type="1"
          />
     	</k-form-item>
    <k-form-item label="运行方式">
        	<k-field-select v-model="formData.rnnCode" data-dict="spv_run_mode" :data-allowblank="false" data-dict-type="1" />
      </k-form-item>
		<k-form-item label="认购日期">
        	<k-field-text v-model="formData.subDate" :data-allowblank="false" />
     	</k-form-item>
		<k-form-item label="到期日期">
        	<k-field-text v-model="formData.endDate" :data-allowblank="false" />
     	</k-form-item>
		<k-form-item label="交易日期">
        	<k-field-text v-model="formData.tradeDate" :data-allowblank="false" />
     	</k-form-item>
		<k-form-item label="币种">
        	<k-field-text v-model="formData.cur" :data-allowblank="false" />
     	</k-form-item>
		<k-form-item label="交易金额">
        	<k-field-text v-model="formData.tradeAmount" :data-allowblank="false" />
     	</k-form-item>
		<k-form-item label="交易金额折人民币">
        	<k-field-text v-model="formData.tradeAmountRmb" :data-allowblank="false" />
     	</k-form-item>
    <k-form-item label="交易方向">
          <k-field-select v-model="formData.tradeDire" data-dict="spv_trade_dire" :data-allowblank="false" data-dict-type="1" />
      </k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="SpvInvestAmountInfo.updateSpvInvestAmountInfo" data-from="editForm"
	        :data-model="formData" data-target="tableGrid">
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
import ZyMixins from "@/pages/report/rhzy/zyMixin.js";
export default {
	name: "SpvInvestInfo",
	mixins: [ZyMixins],
	created() {
		this.getRegionList()
	},
};
</script>