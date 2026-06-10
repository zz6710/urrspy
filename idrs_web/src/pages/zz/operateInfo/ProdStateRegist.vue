<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="ProdStateRegist" data-target="ProdStateRegistRegistGrid" v-model = "searchParam">
        <k-form-item label="操作人员">
          <k-field-text v-model="searchParam.summitUser"/>
        </k-form-item>
        <k-form-item label="操作日期">
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="操作类型">
          <k-field-select v-model="searchParam.opType"  data-dict="op_type"/>
        </k-form-item>
        <k-form-item label="产品登记编码">
          <k-field-text v-model="searchParam.prodRegEnc"/>
        </k-form-item>
        <k-form-item label="统计日期">
          <k-field-date v-model="valdate" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <k-grid ref="ProdStateRegistRegistGrid" @data-row-select="selectRow"  data-operate-column="false" data-action="ProdStateRegist.findProdStateRegists" >
        <k-grid-column data-align="left" data-header="操作用户" data-name="summitUser" data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作日期" data-name="createDate"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作时间" data-name="createTime" data-type="time"  data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作类型" data-name="opType" data-dict="op_type"  data-width="80"></k-grid-column>
        <k-grid-column  data-align="left" data-header="发行机构代码" data-name="bankCode" data-width="120"></k-grid-column>
        <k-grid-column  data-align="left" data-header="产品登记编码" data-name="prodRegEnc" data-width="150"></k-grid-column>
        <k-grid-column  data-align="left" data-header="理财产品总资产金额(元)" data-name="totAssets"  data-width="150"></k-grid-column>
        <k-grid-column  data-align="left" data-header="理财产品杠杆率" data-name="rate" data-width="150"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="统计日期" data-name="valdate"   data-width="150"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="备注" data-name="details" data-width="150"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="登记流水号" data-name="registerSerno" data-export="false"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="登记日期" data-name="registerDate"  data-export="false" data-width="100"></k-grid-column>
     </k-grid>
    </div>


  </div>
</template>

<script>
export default {
  name: "ProdStateRegistRegist",
  data() {
    return {
      formData: {},
      selectRowData: {},
      searchParam:{},
      BreathDay:[],
      valdate:[]
    };
  },
  methods: {
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    }
  },
  watch: {
    valdate(){
      this.$set(this.searchParam, 'valStartDate', this.valdate == null ? '' : this.valdate[0]);
      this.$set(this.searchParam, 'valEndDate', this.valdate == null ? '' : this.valdate[1]);
    },
    //查询起息日
    BreathDay() {
      this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
      this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
    },
  }
};
</script>
