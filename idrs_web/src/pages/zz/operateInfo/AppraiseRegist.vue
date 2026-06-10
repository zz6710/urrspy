<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="AppraiseRegist" data-target="appraiseRegistGrid"  v-model = "searchParam">
        <k-form-item label="操作日期">
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="操作人员">
          <k-field-text v-model="searchParam.summitUser"/>
        </k-form-item>
        <k-form-item label="操作类型">
          <k-field-select v-model="searchParam.opType"  data-dict="op_type"/>
        </k-form-item>
        <k-form-item label="资产/负债编码">
          <k-field-text v-model="searchParam.assetCode"/>
        </k-form-item>
        <k-form-item label="估值日期">
          <k-field-date v-model="valuationDate" data-type="daterange" data-date-format="yyyyMMdd"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <k-grid ref="appraiseRegistGrid" @data-row-select="selectRow" data-operate-column="false" data-action="AppraiseRegist.findAppraiseRegists" >
        <k-grid-column data-header="操作人员" data-name="summitUser"></k-grid-column>
        <k-grid-column data-header="操作日期" data-name="createDate"  data-sortable="true" data-default-sort="DESC"></k-grid-column>
        <k-grid-column data-header="操作时间" data-name="createTime" data-type="time"  data-sortable="true" data-default-sort="DESC"></k-grid-column>
        <k-grid-column data-header="操作类型" data-name="opType" data-dict="op_type"></k-grid-column>
		<k-grid-column data-header="行内资产负债编码" data-name="assetCode"></k-grid-column>
        <k-grid-column data-header="发行机构代码" data-name="bankCode"></k-grid-column>
        <k-grid-column data-header="估值日期" data-name="valuationDate" ></k-grid-column>
		<k-grid-column data-header="单位估值（净价）" data-name="unitDebtNet"></k-grid-column>
		<k-grid-column data-header="单位估值（全价）" data-name="unitDebtFull"></k-grid-column>
      </k-grid>
    </div>



  </div>
</template>

<script>
  export default {
    name: "appraiseRegist",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{},
        BreathDay:[],
        valuationDate:[]
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      }
    },
    watch: {
      //查询起息日
      BreathDay() {
        this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
        this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
      },
      valuationDate(){
        this.$set(this.searchParam, 'valuationStartDate', this.valuationDate == null ? '' : this.valuationDate[0]);
        this.$set(this.searchParam, 'valuationEndDate', this.valuationDate == null ? '' : this.valuationDate[1]);
      }
    }
  };
</script>
