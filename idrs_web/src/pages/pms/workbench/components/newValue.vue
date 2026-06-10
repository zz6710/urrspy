<template>
	<div>
		<k-form-search-customize data-target="prodInfoGrid" v-model="prodSearchParam">
			<!-- <k-form-item label="产品代码">
				<k-field-select
					v-model="prodSearchParam.prodCode"
					data-action="T8Dict.findTaProdInfos"
					data-display-field="prodCode,prodName"
					data-value-field="prodCode"
				></k-field-select>
			</k-form-item> -->
			<k-form-item label="产品代码">
				<k-field-text v-model="prodSearchParam.productCode" data-validate-type="text" />
			</k-form-item>
			<k-form-item label="产品名称">
				<k-field-text v-model="prodSearchParam.productName" data-validate-type="text" />
			</k-form-item>
			<k-form-item label="净值日期">
				<k-field-date v-model="prodSearchParam.netvalueDate" />
			</k-form-item>
		</k-form-search-customize>

		<k-grid
			ref="prodInfoGrid"
			data-action="ProductNetvalue.findProductNetvalues"
			@data-row-select="selectRow"
			:data-operate-column="false"
			data-operate-width="200px"
		>
			<k-grid-column data-header="" data-name="id" :data-hidden="true" />
			<k-grid-column data-header="排名" data-name="index" />
			<k-grid-column data-header="产品代码" data-name="productCode" data-width="150" />
			<k-grid-column data-header="产品名称" data-name="productName" data-width="150" />
			<k-grid-column data-header="产品形态" data-dict="t8_prod_mode" data-name="prodMode" data-width="150" />
			<k-grid-column data-header="成立日期" data-name="establishDate" data-type="date" data-width="150" />
			<k-grid-column data-header="净值日期" data-name="netvalueDate" data-type="date" data-width="150"/>
			<k-grid-column data-header="单位净值" data-name="netvalue" data-width="150" />
			<k-grid-column data-header="累计净值" data-name="totalNetvalue" data-width="150"/>
			<k-grid-column data-header="净值涨跌幅%" data-name="yesterdayRiseAndFall" data-width="150" />
			<k-grid-column data-header="今年以来%" data-name="thisYearRiseAndFall" data-width="150" />
			<k-grid-column data-header="成立以来%" data-name="establishRiseAndFall" data-width="150" />
		</k-grid>
	</div>
</template>

<script>
import { assign } from "lodash";
export default {
	name: "newValue",
	data() {
		return {
			prodSearchParam: {
				prodName: "",
				prodDate: "",
			},
			queryForm: {
				seminarName: "",
				seminarDate: "",
				seminarStatus: "",
				seminarInitiator: "",
			},
			selectRowData: {},
		};
	},
	created() {},
	methods: {
		selectRow(row, column, event) {
			this.selectRowData = assign({}, row);
      console.log('row: ', row);
			if (row.prodMode != 4) {
				// 非现金类型
				this.$router.push({
					path: "pms/analysis",
					query: {
						productCode: row.productCode,
						row: JSON.stringify(row),
					},
				});
			} else {
				// 现金类型
				this.$router.push({
					path: "pms/analysis2",
					query: {
						productCode: row.productCode,
						row: JSON.stringify(row),
					},
				});
			}

			// this.$router.push({path: 'pms/analysis', query: {}})
		},
	},
};
</script>

<style scoped>
</style>
