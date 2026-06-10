<template>
  <div class="py-page">
     <div>
        <k-form-search-customize data-model-name="ProdStateRegistInfoh" data-target="ProdStateRegistInfohGrid" data-label-width="130px" v-model = "searchParam">
           <k-form-item label="产品登记编码" data-label-width="100px">
              <k-field-text v-model="searchParam.prodRegEnc"/>
           </k-form-item>
           <k-form-item label="登记日期" data-label-width="100px">
              <k-field-date v-model="RegisterDate" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
           </k-form-item>
        </k-form-search-customize>
     </div>
    <div class="py-page-container">
      <k-grid ref="ProdStateRegistInfohGrid" @data-row-select="selectRow" data-operate-column="false" data-action="ProdStateRegistInfoh.findProdStateRegistInfos" >
        <k-grid-column  data-align="left" data-header="报送状态" data-name="registerStatus" data-dict="subm_report_status" data-export="false"  data-width="100"></k-grid-column>
        <k-grid-column  data-align="left" data-header="发行机构代码" data-name="bankCode" data-width="120"></k-grid-column>
        <k-grid-column  data-align="left" data-header="产品登记编码" data-name="prodRegEnc" data-width="120"></k-grid-column>
        <k-grid-column  data-align="left" data-header="理财产品总资产金额(元)" data-name="totAssets"  data-width="120"></k-grid-column>
        <k-grid-column  data-align="left" data-header="理财产品杠杆率" data-name="rate" data-width="120"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="统计日期" data-name="valdate"   data-width="100"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="备注" data-name="details" data-width="150"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="登记流水号" data-name="registerSerno" data-export="false" data-width="250"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="登记日期" data-name="registerDate"  data-export="false" data-width="100"></k-grid-column>
        <k-grid-column  data-align="left"   data-header="新增日期" data-name="createDate"  data-export="false" data-width="100"></k-grid-column>
      </k-grid>
    </div>

  </div>
</template>

<script>
export default {
  name: "ProdStateRegistInfoh",
  data() {
    return {
      formData: {},
      selectRowData: {},
      searchParam:{},
      RegisterDate:[]
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
    RegisterDate() {
      this.$set(this.searchParam, 'startDate', this.RegisterDate == null ? '' : this.RegisterDate[0]);
      this.$set(this.searchParam, 'endDate', this.RegisterDate == null ? '' : this.RegisterDate[1]);
    },
  }
};
</script>
