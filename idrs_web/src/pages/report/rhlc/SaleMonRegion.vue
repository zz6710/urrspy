<template>
	<div class="py-page">
		<div>
			<k-form-search-customize data-model-name="SaleMonRegion" v-model="searchParam" data-target="saleMonRegionGrid">
				<k-form-item label="数据日期">
					<k-field-date v-model="searchParam.reportDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyyMM" :data-allowblank="false"/>
				</k-form-item>
				<k-form-item label="产品登记编码">
					<k-field-text v-model="searchParam.prodRegEnc" />
				</k-form-item>
				<k-form-item label="地区类型">
					<k-field-select v-model="searchParam.regionType" data-dict="tr_buy_place" />
				</k-form-item>
			</k-form-search-customize>
		</div>
		<div class="py-page-container">
			<div class="table-top-btns">
				<k-btn
					slot="button"
					class="btn-custom-plain"
					data-target="saleMonRegionGrid"
					:data-export-name="'销售月度统计-分投地区类型_' + searchParam.reportDate"
					data-functype="EXPORT"
					data-size="small"
					data-url="SaleMonRegion.findSaleMonRegions" data-excel-template ="销售月度统计-分投地区类型.xlsx" data-excel-start-line ="3" data-template-name="销售月度统计-分地区类型导出"
				>
					<md-icon>cloud_download</md-icon>
					导出
				</k-btn>
			</div>
			<k-grid ref="saleMonRegionGrid" @data-row-select="selectRow" data-action="SaleMonRegion.findSaleMonRegions" :dataOperateColumn="false" :data-autoload="false">
				<k-grid-column data-header="理财公司理财业务销售数据月度统计表-分地区类型" dataAlign="center">
					<k-grid-column data-header="单位：个、人民币万元" dataAlign="right">
						<k-grid-column data-header="*产品登记编码" data-name="prodRegEnc"></k-grid-column>
						<k-grid-column data-header="*地区类型" data-name="regionType"></k-grid-column>
						<k-grid-column data-header="*投资者数量" data-name="investorNum" dataAlign="right"></k-grid-column>
						<k-grid-column data-header="*总销售金额" data-name="saleTotalMoney" dataAlign="right"></k-grid-column>
						<k-grid-column data-header="*净销售金额" data-name="saleNetMoney" dataAlign="right"></k-grid-column>
						<k-grid-column data-header="*持有余额" data-name="holdBabance" dataAlign="right"></k-grid-column>
						<k-grid-column data-header="备注" data-name="remark"></k-grid-column>
						<k-grid-column data-header="产品特殊销售渠道" data-name="prodEspSaleChannel"></k-grid-column>
					</k-grid-column>
				</k-grid-column>
			</k-grid>
		</div>
	</div>
</template>

<script>
import Tools from '@/utils/tools.js';
export default {
	name: "SaleMonRegion",
	data() {
		return {
			formData: {},
			selectRowData: {},
			searchParam: {
				reportDate: Tools.getPreviousMonth()
			},
			nowDate: "",
		};
	},
	created() {
		this.getNowDate();
	},
	methods: {
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},
		getNowDate() {
			const timeOne = new Date();
			const year = timeOne.getFullYear();
			let month = timeOne.getMonth() + 1;
			let day = timeOne.getDate();
			month = month < 10 ? "0" + month : month;
			day = day < 10 ? "0" + day : day;
			this.nowDate = year + "" + month + "" + day;
		},
	},
};
</script>
<style lang="scss" scoped>
/deep/ {
	.el-table {
		thead.is-group {
			tr.header0 {
				font-size: 18px;
				th.el-table__cell {
					// background: #fff;
				}
			}
			tr.header1 {
				th.el-table__cell {
					// background: #fff;
				}
			}
		}
	}
}
</style>
