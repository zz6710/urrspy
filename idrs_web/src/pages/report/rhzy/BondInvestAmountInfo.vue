<template>
	<div class="py-page">
		<div>
			<k-form-search-customize ref="searchRef" data-model-name="BondInvestAmountInfo" data-label-width="110px" v-model="searchParam" data-target="tableGrid">
				<k-form-item label="数据日期">
					<k-field-date
						v-model="searchParam.reportDate"
						data-type="month"
						data-date-format="yyyy-MM"
						data-value-format="yyyyMM"
						:data-allowblank="false"
					/>
				</k-form-item>
				<k-form-item label="债券品种">
					<k-field-select v-model="searchParam.bondCate" data-dict="bond_cate" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="交易流水号">
					<k-field-text v-model="searchParam.tradeSerNo" />
				</k-form-item>
				<k-form-item label="买入/卖出标志">
					<k-field-select v-model="searchParam.tradeFlag" data-dict="trade_flag" data-dict-type="1" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<div class="left">
					<k-btn ref="assetSend" slot="button" class="btn-custom-plain" data-size="small" @click="creatZipFile('ZQTZFS')">
						<md-icon>cloud_download</md-icon>
						数据报送
					</k-btn>
					<k-btn
						slot="button"
						class="btn-custom-plain"
						data-target="tableGrid"
						:data-export-name="getFileName('ZQTZFS')"
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
				data-action="BondInvestAmountInfo.findBondInvestAmountInfos"
				:data-autoload="false"
				data-operate-width="120px"
				data-fixed="right"
				data-dict-type="1"
			>
				<k-grid-column data-header="数据日期" data-name="reportDate" data-width="120" data-export="false"></k-grid-column>
				<k-grid-column data-header="金融机构代码" data-name="orgCode" data-width="130"></k-grid-column>
				<k-grid-column data-header="内部机构号" data-name="innerOrgCode" data-width="150"></k-grid-column>
				<k-grid-column data-header="债券代码" data-name="bondCode" data-width="150"></k-grid-column>
				<k-grid-column data-header="债券总托管机构" data-name="bondTrustspOrg" data-width="250" data-dict="bond_trustsp_org"></k-grid-column>
				<k-grid-column data-header="债券品种" data-name="bondCate" data-dict="bond_cate" data-width="150"></k-grid-column>
				<k-grid-column data-header="债券信用级别" data-name="bondCreditGrade" data-width="150" data-dict="bond_credit_grade"></k-grid-column>
				<k-grid-column data-header="币种" data-name="cur" data-dict="cur_type" data-width="100"></k-grid-column>
				<k-grid-column data-header="债权债务登记日" data-name="debtRegDate" data-width="150"></k-grid-column>
				<k-grid-column data-header="起息日" data-name="valueDate" data-width="100"></k-grid-column>
				<k-grid-column data-header="兑付日期" data-name="redemDate" data-width="100"></k-grid-column>
				<k-grid-column data-header="票面利率" data-name="couponRate"></k-grid-column>
				<k-grid-column data-header="发行人证件代码" data-name="issuerIdCode" data-width="150"></k-grid-column>
				<k-grid-column data-header="发行人地区代码" data-name="issuerRegionCode" data-width="150">
					<template slot-scope="scope">
						{{ getRegionText(scope.row.row.issuerRegionCode) }}
					</template>
				</k-grid-column>
				<k-grid-column data-header="发行人行业" data-name="issuerIndustry" data-width="150" data-dict="pbc_eco_inds_typ"></k-grid-column>
				<k-grid-column data-header="发行人企业规模" data-name="issuerEntpScale" data-width="150" data-dict="pbc_enterprise_scale"></k-grid-column>
				<k-grid-column data-header="发行人经济成分" data-name="issuerEcoSector" data-width="150" data-dict="issuer_eco_sector"></k-grid-column>
				<k-grid-column data-header="发行人国民经济部门" data-name="issuerEcoDept" data-width="150" data-dict="eco_dept_cate"></k-grid-column>
				<k-grid-column data-header="交易日期" data-name="tradeDate" data-width="100"></k-grid-column>
				<k-grid-column data-header="交易流水号" data-name="tradeSerNo" data-width="150"></k-grid-column>
				<k-grid-column data-header="成交金额" data-name="tradeAmount"></k-grid-column>
				<k-grid-column data-header="成交金额折人民币" data-name="tradeAmountRmb" data-width="150"></k-grid-column>
				<k-grid-column data-header="买入/卖出标志" data-name="tradeFlag" data-width="120" data-dict="trade_flag"></k-grid-column>
				<template slot="operate" slot-scope="scope">
					<k-btn class="btn-custom-text" data-functype="POPUP" data-size="mini" data-target="editPopup"> 修改 </k-btn>
					<k-btn
						class="btn-custom-text"
						data-functype="SUBMIT"
						data-action="BondInvestAmountInfo.deleteBondInvestAmountInfo"
						data-size="mini"
						data-type="danger"
						data-target="tableGrid"
						:data-confirm="true"
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
						data-upload-url="/upload/server/RptApp/rhzy/uploadBondInvestAmountInfo.json"
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

		<!--    修改债券投资发生额信息弹出框   -->
		<k-popup ref="editPopup" data-title="修改">
			<k-form ref="editForm" :data-col="2" data-label-width="150px">
				<k-form-item label="金融机构代码">
					<k-field-text v-model="formData.orgCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="内部机构号">
					<k-field-text v-model="formData.innerOrgCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="债券代码">
					<k-field-text v-model="formData.bondCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="债券总托管机构">
					<k-field-select v-model="formData.bondTrustspOrg" :data-allowblank="false" data-dict="bond_trustsp_org" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="债券品种">
					<k-field-select v-model="formData.bondCate" :data-allowblank="false" data-dict="bond_cate" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="债券信用级别">
					<k-field-select v-model="formData.bondCreditGrade" :data-allowblank="false" data-dict="bond_credit_grade" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="币种">
					<k-field-select v-model="formData.cur" :data-allowblank="false" data-name="cur" data-dict="cur_type" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="债权债务登记日">
					<k-field-date v-model="formData.debtRegDate" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="起息日">
					<k-field-date v-model="formData.valueDate" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="兑付日期">
					<k-field-date v-model="formData.redemDate" />
				</k-form-item>
				<k-form-item label="票面利率">
					<k-field-text v-model="formData.couponRate" />
				</k-form-item>
				<k-form-item label="发行人证件代码">
					<k-field-text v-model="formData.issuerIdCode" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="发行人地区代码">
					<k-field-select
						v-model="formData.issuerRegionCode"
						:data-allowblank="false"
						:data-data="regionList"
						data-value-field="VALUE"
						data-display-field="TEXT"
						data-dict-type="1"
					/>
				</k-form-item>
				<k-form-item label="发行人行业">
					<k-field-select v-model="formData.issuerIndustry" :data-allowblank="false" data-dict="pbc_eco_inds_typ" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="发行人企业规模">
					<k-field-select v-model="formData.issuerEntpScale" data-dict="pbc_enterprise_scale" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="发行人经济成分">
					<k-field-select v-model="formData.issuerEcoSector" data-dict="issuer_eco_sector" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="发行人国民经济部门">
					<k-field-select v-model="formData.issuerEcoDept" :data-allowblank="false" data-dict="eco_dept_cate" data-dict-type="1" />
				</k-form-item>
				<k-form-item label="交易日期">
					<k-field-text v-model="formData.tradeDate" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="交易流水号">
					<k-field-text v-model="formData.tradeSerNo" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="成交金额">
					<k-field-text v-model="formData.tradeAmount" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="成交金额折人民币">
					<k-field-text v-model="formData.tradeAmountRmb" :data-allowblank="false" />
				</k-form-item>
				<k-form-item label="买入/卖出标志">
					<k-field-select v-model="formData.tradeFlag" :data-allowblank="false" data-dict="trade_flag" data-dict-type="1" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-action="BondInvestAmountInfo.updateBondInvestAmountInfo"
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
	name: "BondInvestAmountInfo",
	mixins: [ZyMixins],
	created() {
		this.getRegionList();
	},
};
</script>
