<template>
  <div class="py-page">
    <div>
		<k-form-search-customize ref="searchRef" data-model-name="SumButtomAsset" data-label-width="80px" v-model="searchParam" data-target="SumButtomAssetGrid">
			<k-form-item label="数据日期">
                <k-field-date v-model="searchParam.inputDate" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
            </k-form-item>
			<k-form-item label="理财投资资产代码">
              <k-field-text v-model="searchParam.icode" data-validate-type="text"/>
            </k-form-item>
			<k-form-item label="底层代码">
			  <k-field-text v-model="searchParam.bottomCode" data-validate-type="text"/>
			</k-form-item>
			<k-form-item label="G06穿透底层分类">
			  <k-field-select v-model="searchParam.g06Type" data-dict="g06_type"/>
			</k-form-item>
		</k-form-search-customize>
    </div>
    <div class="py-page-container">
		<div class="left">
          <k-btn slot="button" class="btn-custom-plain" data-functype="EXPORT" data-export-dict="true" data-target="SumButtomAssetGrid" 
						data-export-name="底层估值明细表（调整前）" data-export-form="searchRef">
            <md-icon>cloud_download</md-icon>导出
          </k-btn>
        </div>

      <k-grid ref="SumButtomAssetGrid" @data-row-select="selectRow" data-action="SumButtomAsset.findSumButtomAssets" data-operate-column="false" :data-autoload="false">
		<k-grid-column data-header="数据日期" data-name="inputDate"></k-grid-column>
    <k-grid-column data-header="理财投资资产代码" data-name="icode"></k-grid-column>
    <k-grid-column data-header="资管产品名称(第一层)" data-name="inamec1"></k-grid-column>
		<k-grid-column data-header="资管产品名称(第二层)" data-name="inamec2"></k-grid-column>
		<k-grid-column data-header="底层代码" data-name="bottomCode"></k-grid-column>
		<k-grid-column data-header="科目名称" data-name="itemName"></k-grid-column>
		<k-grid-column data-header="组合代码" data-name="comcode"></k-grid-column>
		<k-grid-column data-header="成本" data-name="cost"></k-grid-column>
		<k-grid-column data-header="市值" data-name="amount"></k-grid-column>
		<k-grid-column data-header="币种" data-name="currency"></k-grid-column>
		<k-grid-column data-header="G06穿透底层分类" data-name="g06Type" data-dict="g06_type"></k-grid-column>
		<k-grid-column data-header="信用等级" data-name="ratLevel" data-dict="g06_bond_credit" data-width="134"></k-grid-column>
		<k-grid-column data-header="是否投向公共私营合作项目（PPP）的部分" data-name="isPppPart" data-dict="g06_yes_no"></k-grid-column>
		<k-grid-column data-header="是否投向市场化债转股相关" data-name="isMktBtsRlt" data-dict="g06_yes_no"></k-grid-column>
		<k-grid-column data-header="是否投向地方政府融资平台的部分" data-name="isGovFncPart" data-dict="g06_yes_no"></k-grid-column>
		<k-grid-column data-header="是否投向房地产业" data-name="isRealSetate" data-dict="g06_yes_no"></k-grid-column>
		<k-grid-column data-header="商业银行优先股" data-name="isFncStk" data-dict="g06_yes_no"></k-grid-column>
		<k-grid-column data-header="商业银行永续债" data-name="isFncBnd" data-dict="g06_yes_no"></k-grid-column>
		<k-grid-column data-header="商业银行二级资本债" data-name="isFncScdBnd" data-dict="g06_yes_no"></k-grid-column>
		<k-grid-column data-header="商业银行可转债" data-name="isFncTsfBnd" data-dict="g06_yes_no"></k-grid-column>
		<k-grid-column data-header="其他银行资本补充工具" data-name="isOthBnkTls" data-dict="g06_yes_no"></k-grid-column>
		<k-grid-column data-header="是否地方政府专项债" data-name="isGovSpcBnd" data-dict="g06_yes_no"></k-grid-column>
		<k-grid-column data-header="汇率" data-name="exchangeRate"></k-grid-column>
		<k-grid-column data-header="投资估值表比例" data-name="invValRate"></k-grid-column>
		<k-grid-column data-header="是否现管产品投资" data-name="isCshMng" data-dict="g06_yes_no"></k-grid-column>
		<k-grid-column data-header="投资估值表比例(现管产品)" data-name="invValRateCsh"></k-grid-column>
		<k-grid-column data-header="非保本比例" data-name="nonGrtRate"></k-grid-column>
		<k-grid-column data-header="投向非保本金额" data-name="nonGrtAmt"></k-grid-column>
		<k-grid-column data-header="管理方式" data-name="manageMode" data-dict="g06_manager_type"></k-grid-column>
		<k-grid-column data-header="备注" data-name="mark"></k-grid-column>
        <!-- <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改底层估值明细表（调整前）" data-functype="POPUP" data-size="mini"
            data-target="editSumButtomAssetPopup">
			  修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="SumButtomAsset.deleteSumButtomAsset" data-size="mini"
               data-type="danger" data-target="SumButtomAssetGrid" :data-confirm="true" data-descript="删除底层估值明细表（调整前）">
			  删除
    	  </k-btn>
        </template> -->
      </k-grid>
    </div>

	<!--    添加底层估值明细表（调整前）弹出框   -->
	<k-popup ref="addSumButtomAssetPopup" data-title="新增">
    	<k-form ref="addSumButtomAssetForm" :data-col="2">
			<k-form-item label="组合代码">
	        	<k-field-text v-model="formData.comcode"/>
	     	</k-form-item>
			<k-form-item label="底层资产代码">
	        	<k-field-text v-model="formData.bottomCode"/>
	     	</k-form-item>
			<k-form-item label="类型">
	        	<k-field-text v-model="formData.assetType"/>
	     	</k-form-item>
			<k-form-item label="市值">
	        	<k-field-text v-model="formData.amount"/>
	     	</k-form-item>
			<k-form-item label="成本">
	        	<k-field-text v-model="formData.cost"/>
	     	</k-form-item>
			<k-form-item label="币种">
	        	<k-field-text v-model="formData.currency"/>
	     	</k-form-item>
			<k-form-item label="估值日期">
	        	<k-field-text v-model="formData.inputDate"/>
	     	</k-form-item>
			<k-form-item label="科目代码">
	        	<k-field-text v-model="formData.itemId"/>
	     	</k-form-item>
			<k-form-item label="科目名称">
	        	<k-field-text v-model="formData.itemName"/>
	     	</k-form-item>
			<k-form-item label="导入日期">
	        	<k-field-text v-model="formData.importDate"/>
	     	</k-form-item>
			<k-form-item label="理财投资资产代码">
	        	<k-field-text v-model="formData.icode"/>
	     	</k-form-item>
			<k-form-item label="资管产品名称(第一层)">
	        	<k-field-text v-model="formData.inamec1"/>
	     	</k-form-item>
			<k-form-item label="资管产品名称(第二层)">
	        	<k-field-text v-model="formData.inamec2"/>
	     	</k-form-item>
			<k-form-item label="资产类型">
	        	<k-field-text v-model="formData.atype"/>
	     	</k-form-item>
			<k-form-item label="市场类型">
	        	<k-field-text v-model="formData.mtype"/>
	     	</k-form-item>
			<k-form-item label="起息代码">
	        	<k-field-text v-model="formData.assetCode"/>
	     	</k-form-item>
			<k-form-item label="层级">
	        	<k-field-text v-model="formData.orgLevel"/>
	     	</k-form-item>
			<k-form-item label="净价金额">
	        	<k-field-text v-model="formData.netValue"/>
	     	</k-form-item>
			<k-form-item label="中债资产报送类别(表层)">
	        	<k-field-text v-model="formData.zzReportType"/>
	     	</k-form-item>
			<k-form-item label="g06分类(穿透底层)">
	        	<k-field-text v-model="formData.g06Type"/>
	     	</k-form-item>
			<k-form-item label="信用等级(底层资产)">
	        	<k-field-text v-model="formData.ratLevel"/>
	     	</k-form-item>
			<k-form-item label="是否投向公共私营合作项目（PPP）的部分">
	        	<k-field-text v-model="formData.isPppPart"/>
	     	</k-form-item>
			<k-form-item label="是否投向市场化债转股相关">
	        	<k-field-text v-model="formData.isMktBtsRlt"/>
	     	</k-form-item>
			<k-form-item label="是否投向地方政府融资平台的部分">
	        	<k-field-text v-model="formData.isGovFncPart"/>
	     	</k-form-item>
			<k-form-item label="商业银行优先股">
	        	<k-field-text v-model="formData.isFncStk"/>
	     	</k-form-item>
			<k-form-item label="商业银行永续债">
	        	<k-field-text v-model="formData.isFncBnd"/>
	     	</k-form-item>
			<k-form-item label="商业银行二级资本债">
	        	<k-field-text v-model="formData.isFncScdBnd"/>
	     	</k-form-item>
			<k-form-item label="商业银行可转债">
	        	<k-field-text v-model="formData.isFncTsfBnd"/>
	     	</k-form-item>
			<k-form-item label="其他银行资本补充工具">
	        	<k-field-text v-model="formData.isOthBnkTls"/>
	     	</k-form-item>
			<k-form-item label="是否地方政府专项债">
	        	<k-field-text v-model="formData.isGovSpcBnd"/>
	     	</k-form-item>
			<k-form-item label="汇率">
	        	<k-field-text v-model="formData.exchangeRate"/>
	     	</k-form-item>
			<k-form-item label="投资估值表比例">
	        	<k-field-text v-model="formData.invValRate"/>
	     	</k-form-item>
			<k-form-item label="非保本比例">
	        	<k-field-text v-model="formData.nonGrtRate"/>
	     	</k-form-item>
			<k-form-item label="投向非保本金额">
	        	<k-field-text v-model="formData.nonGrtAmt"/>
	     	</k-form-item>
			<k-form-item label="交易场所">
	        	<k-field-text v-model="formData.tradePlace"/>
	     	</k-form-item>
			<k-form-item label="是否公募基金">
	        	<k-field-text v-model="formData.isPublic"/>
	     	</k-form-item>
			<k-form-item label="管理方式">
	        	<k-field-text v-model="formData.manageMode"/>
	     	</k-form-item>
			<k-form-item label="备注">
	        	<k-field-text v-model="formData.mark"/>
	     	</k-form-item>
			<k-form-item label="数据插入日期">
	        	<k-field-text v-model="formData.dataInsrDt"/>
	     	</k-form-item>
			<k-form-item label="处理日期">
	        	<k-field-text v-model="formData.dealDate"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
              <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="SumButtomAsset.addSumButtomAsset" data-from="addSumButtomAssetForm"
                     :data-model="formData" data-target="SumButtomAssetGrid">
                <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
              </k-btn>
              <k-btn class="btn-custom-plain" data-functype="CLOSE">
                <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
            </k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改底层估值明细表（调整前）弹出框   -->
	<k-popup ref="editSumButtomAssetPopup" data-title="修改">
	  <k-form ref="editSumButtomAssetForm" :data-col="2">
		<k-form-item label="组合代码">
        	<k-field-text v-model="formData.comcode"/>
     	</k-form-item>
		<k-form-item label="底层资产代码">
        	<k-field-text v-model="formData.bottomCode"/>
     	</k-form-item>
		<k-form-item label="类型">
        	<k-field-text v-model="formData.assetType"/>
     	</k-form-item>
		<k-form-item label="市值">
        	<k-field-text v-model="formData.amount"/>
     	</k-form-item>
		<k-form-item label="成本">
        	<k-field-text v-model="formData.cost"/>
     	</k-form-item>
		<k-form-item label="币种">
        	<k-field-text v-model="formData.currency"/>
     	</k-form-item>
		<k-form-item label="估值日期">
        	<k-field-text v-model="formData.inputDate"/>
     	</k-form-item>
		<k-form-item label="科目代码">
        	<k-field-text v-model="formData.itemId"/>
     	</k-form-item>
		<k-form-item label="科目名称">
        	<k-field-text v-model="formData.itemName"/>
     	</k-form-item>
		<k-form-item label="导入日期">
        	<k-field-text v-model="formData.importDate"/>
     	</k-form-item>
		<k-form-item label="理财投资资产代码">
        	<k-field-text v-model="formData.icode"/>
     	</k-form-item>
		<k-form-item label="资管产品名称(第一层)">
        	<k-field-text v-model="formData.inamec1"/>
     	</k-form-item>
		<k-form-item label="资管产品名称(第二层)">
        	<k-field-text v-model="formData.inamec2"/>
     	</k-form-item>
		<k-form-item label="资产类型">
        	<k-field-text v-model="formData.atype"/>
     	</k-form-item>
		<k-form-item label="市场类型">
        	<k-field-text v-model="formData.mtype"/>
     	</k-form-item>
		<k-form-item label="起息代码">
        	<k-field-text v-model="formData.assetCode"/>
     	</k-form-item>
		<k-form-item label="层级">
        	<k-field-text v-model="formData.orgLevel"/>
     	</k-form-item>
		<k-form-item label="净价金额">
        	<k-field-text v-model="formData.netValue"/>
     	</k-form-item>
		<k-form-item label="中债资产报送类别(表层)">
        	<k-field-text v-model="formData.zzReportType"/>
     	</k-form-item>
		<k-form-item label="g06分类(穿透底层)">
        	<k-field-text v-model="formData.g06Type"/>
     	</k-form-item>
		<k-form-item label="信用等级(底层资产)">
        	<k-field-text v-model="formData.ratLevel"/>
     	</k-form-item>
		<k-form-item label="是否投向公共私营合作项目（PPP）的部分">
        	<k-field-text v-model="formData.isPppPart"/>
     	</k-form-item>
		<k-form-item label="是否投向市场化债转股相关">
        	<k-field-text v-model="formData.isMktBtsRlt"/>
     	</k-form-item>
		<k-form-item label="是否投向地方政府融资平台的部分">
        	<k-field-text v-model="formData.isGovFncPart"/>
     	</k-form-item>
		<k-form-item label="商业银行优先股">
        	<k-field-text v-model="formData.isFncStk"/>
     	</k-form-item>
		<k-form-item label="商业银行永续债">
        	<k-field-text v-model="formData.isFncBnd"/>
     	</k-form-item>
		<k-form-item label="商业银行二级资本债">
        	<k-field-text v-model="formData.isFncScdBnd"/>
     	</k-form-item>
		<k-form-item label="商业银行可转债">
        	<k-field-text v-model="formData.isFncTsfBnd"/>
     	</k-form-item>
		<k-form-item label="其他银行资本补充工具">
        	<k-field-text v-model="formData.isOthBnkTls"/>
     	</k-form-item>
		<k-form-item label="是否地方政府专项债">
        	<k-field-text v-model="formData.isGovSpcBnd"/>
     	</k-form-item>
		<k-form-item label="汇率">
        	<k-field-text v-model="formData.exchangeRate"/>
     	</k-form-item>
		<k-form-item label="投资估值表比例">
        	<k-field-text v-model="formData.invValRate"/>
     	</k-form-item>
		<k-form-item label="非保本比例">
        	<k-field-text v-model="formData.nonGrtRate"/>
     	</k-form-item>
		<k-form-item label="投向非保本金额">
        	<k-field-text v-model="formData.nonGrtAmt"/>
     	</k-form-item>
		<k-form-item label="交易场所">
        	<k-field-text v-model="formData.tradePlace"/>
     	</k-form-item>
		<k-form-item label="是否公募基金">
        	<k-field-text v-model="formData.isPublic"/>
     	</k-form-item>
		<k-form-item label="管理方式">
        	<k-field-text v-model="formData.manageMode"/>
     	</k-form-item>
		<k-form-item label="备注">
        	<k-field-text v-model="formData.mark"/>
     	</k-form-item>
		<k-form-item label="数据插入日期">
        	<k-field-text v-model="formData.dataInsrDt"/>
     	</k-form-item>
		<k-form-item label="处理日期">
        	<k-field-text v-model="formData.dealDate"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="SumButtomAsset.updateSumButtomAsset" data-from="editSumButtomAssetForm"
	        :data-model="formData" data-target="SumButtomAssetGrid">
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
        selectRowData: {},
        searchParam: {}
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      }
    }
  };
</script>
