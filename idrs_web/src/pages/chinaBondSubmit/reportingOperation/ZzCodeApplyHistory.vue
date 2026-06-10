<template>
  <div class="py-page">
		<div>
		<k-form-search-customize data-model-name="ZzCodeApplyHistory" v-model="searchParam" data-target="tableGrid">
          <k-form-item label="产品登记编码">
            <k-field-text v-model="searchParam.prodRegEnc"  />
          </k-form-item>
           <k-form-item label="行内标识码">
                      <k-field-text v-model="searchParam.innerCode"  />
           </k-form-item>
        </k-form-search-customize>
		</div>
    <div class="py-page-container">
			<k-grid ref="tableGrid" @data-row-select="selectRow" data-action="ZzCodeApplyHistory.findZzCodeApplyHistorys" data-operate-column="false">
				<k-grid-column data-header="ID" data-name="id" data-hidden="true"></k-grid-column>
				<k-grid-column data-header="产品名称" data-name="prodNm"></k-grid-column>
				<k-grid-column data-header="行内标识码" data-name="innerCode"></k-grid-column>
				<k-grid-column data-header="产品登记编码" data-name="prodRegEnc"></k-grid-column>
				<k-grid-column data-header="审核结果" data-name="chekResult" ></k-grid-column>
				<k-grid-column data-header="审核意见" data-name="chekOpinion"></k-grid-column>
				<k-grid-column data-header="文件名称" data-name="directZipNm"></k-grid-column>
				<k-grid-column data-header="入库时间" data-name="crtTime"></k-grid-column>
			</k-grid>
		</div>
	</div>
</template>

<script>
import Tools from "@/utils/tools";
export default {
	name: "ZzCodeApplyHistory",
	data() {
		return {
			formData: {},
			selectRowData:{},
			searchParam: {},
		};
	},
	methods: {
		selectRow(row, column, event) {
			this.selectRowData = assign({}, row);
      this.formData = assign({}, row);
		},
	},
};
</script>

