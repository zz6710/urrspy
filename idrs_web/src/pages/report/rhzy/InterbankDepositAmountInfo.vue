<template>
	<div class="py-page">
		<div>
			<k-form-search-customize ref="searchRef" data-model-name="InterbankDepositAmountInfo" v-model="searchParam" data-target="tableGrid">
				<k-form-item label="数据日期">
					<k-field-date
						v-model="searchParam.reportDate"
						data-type="month"
						data-date-format="yyyy-MM"
						data-value-format="yyyyMM"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-form-item label="业务类型">
					<k-field-select v-model="searchParam.busiType" data-dict="interbank_deposit_type" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="存款账户编码">
					<k-field-text v-model="searchParam.depositAccoCode" />
				</k-form-item>
				<k-form-item label="存款协议代码">
					<k-field-text v-model="searchParam.depositProtocolCode" />
				</k-form-item>
				<k-form-item label="交易流水号">
					<k-field-text v-model="searchParam.tradeSerNo" />
				</k-form-item>
				<k-form-item label="交易方向">
					<k-field-select v-model="searchParam.tradeDire" data-dict="trade_dire" data-dict-type="1" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn ref="assetSend" slot="button" class="btn-custom-plain" data-size="small" @click="creatZipFile('TYCKFS')">
						<md-icon>cloud_download</md-icon>
						数据报送
					</k-btn>
					<k-btn
						slot="button"
						class="btn-custom-plain"
						data-target="tableGrid"
						:data-export-name="getFileName('TYCKFS')"
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
				data-action="InterbankDepositAmountInfo.findInterbankDepositAmountInfos"
				:data-autoload="false"
				data-operate-width="120px"
				data-fixed="right"
				data-dict-type="1"
			>
				<k-grid-column data-align="left" data-header="数据日期" data-name="reportDate" data-width="120" data-export="false"></k-grid-column>
				<k-grid-column data-header="金融机构代码" data-name="orgCode" data-width="130"></k-grid-column>
				<k-grid-column data-header="内部机构号" data-name="innerOrgCode" data-width="150"></k-grid-column>
				<k-grid-column data-header="业务类型" data-name="busiType" data-dict="interbank_deposit_type" data-width="130"></k-grid-column>
				<k-grid-column data-header="交易对手证件类型" data-name="cntrIdType" data-width="150" data-dict="cntr_id_type"></k-grid-column>
				<k-grid-column data-header="交易对手代码" data-name="cntrCode" data-width="130"></k-grid-column>
				<k-grid-column data-header="存款账户编码" data-name="depositAccoCode" data-width="130"></k-grid-column>
				<k-grid-column data-header="存款协议代码" data-name="depositProtocolCode" data-width="130"></k-grid-column>
				<k-grid-column data-header="协议起始日期" data-name="protocolStartDate" data-width="100"></k-grid-column>
				<k-grid-column data-header="协议到期日期" data-name="protocolEndDate" data-width="100"></k-grid-column>
				<k-grid-column data-header="币种" data-name="cur" data-dict="cur_type" data-width="100"></k-grid-column>
				<k-grid-column data-header="交易金额" data-name="tradeAmount" data-width="120"></k-grid-column>
				<k-grid-column data-header="交易金额折人民币" data-name="tradeAmountRmb" data-width="160"></k-grid-column>
				<k-grid-column data-header="交易日期" data-name="tradeDate" data-width="100"></k-grid-column>
				<k-grid-column data-header="交易流水号" data-name="tradeSerNo" data-width="120"></k-grid-column>
				<k-grid-column data-header="利率水平" data-name="rateLevel"></k-grid-column>
				<k-grid-column data-header="交易账户号" data-name="tradeAccoNo" data-width="120"></k-grid-column>
				<k-grid-column data-header="交易账户开户行号" data-name="tradeAccoBankNo" data-width="160"></k-grid-column>
				<k-grid-column data-header="交易对手账户号" data-name="cntrAccoNo" data-width="160"></k-grid-column>
				<k-grid-column data-header="交易方向" data-name="tradeDire" data-dict="trade_dire"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn class="btn-custom-text" data-descript="修改同业存款发生额信息" data-functype="POPUP" data-size="mini" data-target="editPopup">
						修改
					</k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="InterbankDepositAmountInfo.deleteInterbankDepositAmountInfo"
						data-size="mini"
						data-type="danger"
						data-target="tableGrid"
						:data-confirm="true"
						data-descript="删除同业存款发生额信息"
					>
						删除
					</k-btn>
				</template>
			</k-grid>
		</div>

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
						data-upload-url="/upload/server/RptApp/rhzy/uploadInterbankDepositAmountInfo.json"
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

		<!--    修改同业存款发生额信息弹出框   -->
		<k-popup ref="editPopup" data-title="修改">
			<k-form ref="editForm" :data-col="2" data-label-width="140px">
				<k-form-item label="金融机构代码">
					<k-field-text v-model="formData.orgCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="内部机构号">
					<k-field-text v-model="formData.innerOrgCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="业务类型">
					<k-field-select v-model="formData.busiType" data-dict="interbank_deposit_type" :data-allowblank="false" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="交易对手证件类型">
					<k-field-select v-model="formData.cntrIdType" data-dict="cntr_id_type" :data-allowblank="false" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="交易对手代码">
					<k-field-text v-model="formData.cntrCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="存款账户编码">
					<k-field-text v-model="formData.depositAccoCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="存款协议代码">
					<k-field-text v-model="formData.depositProtocolCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="协议起始日期">
					<k-field-text v-model="formData.protocolStartDate" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="协议到期日期">
					<k-field-text v-model="formData.protocolEndDate" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="币种">
					<k-field-select v-model="formData.cur" data-dict="cur_type" :data-allowblank="false" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="交易金额">
					<k-field-text v-model="formData.tradeAmount" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="交易金额折人民币">
					<k-field-text v-model="formData.tradeAmountRmb" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="交易日期">
					<k-field-text v-model="formData.tradeDate" />
				</k-form-item>
				<k-form-item label="交易流水号">
					<k-field-text v-model="formData.tradeSerNo" />
				</k-form-item>
				<k-form-item label="利率水平">
					<k-field-text v-model="formData.rateLevel" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="交易账户号">
					<k-field-text v-model="formData.tradeAccoNo" />
				</k-form-item>
				<k-form-item label="交易账户开户行号">
					<k-field-text v-model="formData.tradeAccoBankNo" />
				</k-form-item>
				<k-form-item label="交易对手账户号">
					<k-field-text v-model="formData.cntrAccoNo" />
				</k-form-item>
				<k-form-item label="交易方向">
					<k-field-select v-model="formData.tradeDire" data-dict="trade_dire" :data-allowblank="false" data-dict-type="1" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="InterbankDepositAmountInfo.updateInterbankDepositAmountInfo"
						data-from="editForm"
						:data-model="formData"
						data-target="tableGrid"
					>
						<md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE"> <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
	</div>
</template>

<script>
import ZyMixins from "@/pages/report/rhzy/zyMixin.js";
export default {
	name: "InterbankDepositAmountInfo",
	mixins: [ZyMixins],
};
</script>
