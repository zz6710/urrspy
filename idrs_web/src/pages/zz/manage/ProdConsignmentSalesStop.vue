<template>
	<div class="py-page">
		<div>
			<k-form-search-customize
				data-model-name="ProdConsignmentSalesStop"
				data-label-width="80px"
				v-model="queryDate"
				data-target="prodConsignmentSalesStopGrid"
			>
				<k-form-item label="报送日期">
					<k-field-date v-model="searchParam.reportDate" data-type="daterange" data-date-format="yyyy-MM-dd" data-value-format="yyyyMMdd" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<k-btn
					slot="button"
					ref="exportRef"
					class="btn-custom-plain"
					data-functype="EXPORT"
					data-target="prodConsignmentSalesStopGrid"
					data-export-name="委托销售产品编码停用导入"
					data-url="ProdConsignmentSalesStop.findProdConsignmentSalesStops"
					data-excel-template="委托销售产品编码停用导入.xlsx"
					data-excel-start-line="3"
					data-template-name="委托销售产品编码停用导入导出"
				>
					<md-icon>cloud_download</md-icon>
					导出
				</k-btn>
			</div>
			<k-grid
				ref="prodConsignmentSalesStopGrid"
				data-action="ProdConsignmentSalesStop.findProdConsignmentSalesStops"
				:data-operate-column="false"
				:data-autoload="false"
			>
				<k-grid-column data-header="委托销售产品编码停用导入" data-align="center">
					<k-grid-column data-header="发行机构代码">
						<k-grid-column data-header="代理销售机构代码" data-name="bankCode"></k-grid-column>
					</k-grid-column>
					<k-grid-column :data-header="orgNo">
						<k-grid-column data-header="委托销售产品登记编码" data-name="prodRegEnc"></k-grid-column>
					</k-grid-column>
				</k-grid-column>
			</k-grid>
		</div>
	</div>
</template>

<script>
import moment from "moment";
export default {
	data() {
		return {
			searchParam: {},
			orgNo: "",
		};
	},
	computed: {
		queryDate() {
			return {
				startDate: this.searchParam.reportDate ? this.searchParam.reportDate[0] : null,
				endDate: this.searchParam.reportDate ? this.searchParam.reportDate[1] : null,
			};
		},
	},
	created() {
		this.$set(this.searchParam, "reportDate", [moment().format("YYYYMMDD"), moment().format("YYYYMMDD")]);
	},
	mounted() {
		this.getOrgNo();
		this.$nextTick(() => {
			this.$refs.prodConsignmentSalesStopGrid.load(this.queryDate);
		});
	},
	methods: {
		getOrgNo() {
			this.httpUtil
				.comnQuery({
					action: "ProdConsignmentSales.getParaValueForFXJGDM",
				})
				.then((data) => {
					this.orgNo = data.returndata.paravalue;
				});
		},
	},
};
</script>
