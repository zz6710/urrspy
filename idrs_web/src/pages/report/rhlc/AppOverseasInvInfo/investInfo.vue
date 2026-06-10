<template>
	<div class="py-page">
		<!-- <div>
			<k-form-search-customize
				data-model-name="AppOverseasInvInfo"
				data-label-width="80px"
				v-model="searchParam"
				data-target="appOverseasInvInfoGrid"
			>
				<k-form-item label="数据日期">
					<k-field-date v-model="searchParam.dataDate" data-type="date" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" />
				</k-form-item>
			</k-form-search-customize>
		</div> -->
		<div class="py-page-container">
			<k-grid
				ref="appOverseasInvInfoGrid"
				@data-row-select="selectRow"
				data-action="AppOverseasInvInfo.findAppOverseasInvInfos"
				:dataOperateColumn="false"
				:dataAutoload="false"
			>
				<k-grid-column data-header="数据日期" data-name="reportDate"></k-grid-column>
				<k-grid-column data-header="序号" data-name="num"></k-grid-column>
				<k-grid-column data-header="公司全称" data-name="compNm"></k-grid-column>
				<k-grid-column data-header="资金来源类型" data-name="fundSourType"></k-grid-column>
				<k-grid-column data-header="资金最终持有人与投资资产是否均在境外" data-name="fundSourOver" data-width="100"></k-grid-column>
				<k-grid-column data-header="涉及通过产品投资的产品名称" data-name="prodCd" data-width="100"></k-grid-column>
				<k-grid-column data-header="产品登记编码" data-name="prodRegEnc"></k-grid-column>
				<k-grid-column data-header="理财产品境外托管机构名称" data-name="prodOrgNm"></k-grid-column>
				<k-grid-column data-header="理财产品境外托管机构国别" data-name="prodOrgSeas"></k-grid-column>
				<k-grid-column data-header="投资形式" data-name="invType"></k-grid-column>
				<k-grid-column data-header="币种" data-name="prodCcy"></k-grid-column>
				<k-grid-column data-header="投资初始日期" data-name="finvTm"></k-grid-column>
				<k-grid-column data-header="投资方式1" data-name="invTypeF"></k-grid-column>
				<k-grid-column data-header="投资方式2" data-name="invTypeS"></k-grid-column>
				<k-grid-column data-header="是否境外主权债权" data-name="overSoveBd"></k-grid-column>
				<k-grid-column data-header="外汇额度类型" data-name="fxType"></k-grid-column>
				<k-grid-column data-header="投资区域1（国家或地区）" data-name="invSeasF"></k-grid-column>
				<k-grid-column data-header="投资区域2（国家或地区）" data-name="invSeasS"></k-grid-column>
				<k-grid-column data-header="投资区域3（国家或地区）" data-name="invSeasT"></k-grid-column>
				<k-grid-column data-header="投资资产类型1" data-name="invAsstF"></k-grid-column>
				<k-grid-column data-header="投资资产类型2" data-name="invAsstS"></k-grid-column>
				<k-grid-column data-header="投资资产类型3" data-name="invAsstT"></k-grid-column>
				<k-grid-column data-header="港股通基金或QDII基金" data-name="qdiiFund"></k-grid-column>
				<k-grid-column data-header="投资资产名称" data-name="invAsstNm"></k-grid-column>
				<k-grid-column data-header="交易对手" data-name="tranCount"></k-grid-column>
				<k-grid-column data-header="投资资产本金（万元）" data-name="invAsstCost"></k-grid-column>
				<k-grid-column data-header="投资资产余额（万元）" data-name="invAsstMkt"></k-grid-column>
				<k-grid-column data-header="债务期限结构" data-name="bdRmaiType"></k-grid-column>
				<k-grid-column data-header="风险事件" data-name="riskEnvn"></k-grid-column>
				<k-grid-column data-header="风险项目余额（万元）" data-name="riskPjAmt"></k-grid-column>
				<k-grid-column data-header="计提减值准备金额（万元）" data-name="deprRdyAmt"></k-grid-column>
				<k-grid-column data-header="已计提减值（万元）" data-name="deprAmt"></k-grid-column>
				<k-grid-column data-header="备注" data-name="note"></k-grid-column>
			</k-grid>
		</div>
	</div>
</template>

<script>
export default {
	data() {
		return {
			formData: {},
			selectRowData: {},
			searchParam: {},
		};
	},
	methods: {
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},
	},
};
</script>
