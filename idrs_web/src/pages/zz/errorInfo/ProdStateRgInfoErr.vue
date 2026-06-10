<template>
  <div class="py-page">
      <div>
          <k-form-search-customize data-model-name="ProdStateRgInfoErr" data-target="prodStateRgInfoErrGrid" data-label-width="80px" v-model = "searchParam">
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
      <k-grid ref="prodStateRgInfoErrGrid" @data-row-select="selectRow" data-operate-column="false" data-action="ProdStateRgInfoErr.findProdStateRgInfoErrs" >
      <k-grid-column  data-header="发行机构代码错误" data-name="bankCodeDesc" data-width="120"></k-grid-column>
      <k-grid-column  data-header="产品登记编码错误" data-name="prodRegEncDesc" data-width="120"></k-grid-column>
      <k-grid-column  data-header="理财产品总资产金额(元)错误" data-name="totAssetsDesc"  data-width="120"></k-grid-column>
      <k-grid-column  data-header="理财产品杠杆率错误" data-name="rateDesc" data-width="120"></k-grid-column>
      <k-grid-column  data-header="统计日期错误" data-name="valdateDesc"   data-width="100"></k-grid-column>
      <k-grid-column  data-header="备注错误" data-name="detailsDesc" data-width="150"></k-grid-column>
		  <k-grid-column  data-header="登记流水号" data-name="registerSerno"></k-grid-column>
		  <k-grid-column  data-header="导入日期" data-name="impDate"></k-grid-column>
      </k-grid>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ProdStateRgInfoErr",
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
