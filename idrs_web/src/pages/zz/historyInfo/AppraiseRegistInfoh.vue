<template>
  <div class="py-page">
        <div>
           <k-form-search-customize data-model-name="AppraiseRegistInfoh" data-target="AppraiseRegistInfohGrid" data-label-width="110px" v-model = "searchParam">
                  <k-form-item label="业务登记日期">
                     <k-field-date v-model="RegisterDate" data-type="daterange" data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd"/>
                  </k-form-item>
                  <k-form-item label="资产/负债编码">
                     <k-field-text v-model="searchParam.assetCode"/>
                  </k-form-item>
                  <k-form-item label="估值日期" data-label-width="80px">
                      <k-field-date v-model="ValuationDate" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
                  </k-form-item>
               </k-form-search-customize>
        </div>
    <div class="py-page-container">
      <k-grid ref="AppraiseRegistInfohGrid" @data-row-select="selectRow"  data-operate-column="false" data-action="AppraiseRegistInfoh.findAppraiseRegistInfohs" >
        <k-grid-column data-align="left" data-header="报送状态" data-name="registerStatus" data-dict="subm_report_status"  data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="行内资产/负债编码" data-name="assetCode"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构代码" data-name="bankCode"></k-grid-column>
        <k-grid-column data-align="left" data-header="估值日期" data-name="valuationDate" data-type="date"></k-grid-column>
        <k-grid-column data-align="right" data-header="单位估值（净价）" data-name="unitDebtNet"></k-grid-column>
        <k-grid-column data-align="right" data-header="单位估值（全价）" data-name="unitDebtFull"></k-grid-column>
        <k-grid-column data-align="left" data-header="备注" data-name="details"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记流水号" data-name="registerSerno" data-export="false" data-width="250"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记日期" data-name="registerDate" data-type="date" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="理论报送起始日期" data-name="theoryReportStartDate" data-type="date" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="理论报送截止日期" data-name="theoryReportEndDate" data-type="date" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="新增日期" data-name="createDate" data-type="date" data-export="false"></k-grid-column>
      </k-grid>
    </div>

  </div>
</template>

<script>
  export default {
    name: "AppraiseRegistInfoh",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{},
        ValuationDate:[],
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
      ValuationDate() {
        this.$set(this.searchParam, 'startDate2', this.ValuationDate == null ? '' : this.ValuationDate[0]);
        this.$set(this.searchParam, 'endDate2', this.ValuationDate == null ? '' : this.ValuationDate[1]);
      },
    }
  };
</script>

