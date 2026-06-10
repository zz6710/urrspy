<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="CustVolRegister" data-label-width="110px" data-target="custVolRegisterGrid" v-model = "searchParam">
        <k-form-item label="操作日期">
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="操作人员">
          <k-field-text v-model="searchParam.summitUser"/>
        </k-form-item>
        <k-form-item label="操作类型">
          <k-field-select v-model="searchParam.opType"  data-dict="op_type"/>
        </k-form-item>
        <k-form-item label="持有日期">
          <k-field-date v-model="HoldDate" data-type="daterange" data-date-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="产品登记编码">
          <k-field-text v-model="searchParam.prodCode"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <k-grid ref="custVolRegisterGrid" @data-row-select="selectRow" data-operate-column="false" data-action="CustVolRegister.findCustVolRegisters" >
        <k-grid-column data-align="left" data-header="操作用户" data-name="summitUser"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作日期" data-name="createDate" ></k-grid-column>
        <k-grid-column data-align="left" data-header="操作时间" data-name="createTime" data-type="time" ></k-grid-column>
        <k-grid-column data-align="left" data-header="操作类型" data-name="opType" data-dict="op_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记机构代码" data-name="bankCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品登记编码" data-name="prodCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="识别标识" data-name="custNo" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="持有日期" data-name="holdDate"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="币种" data-name="cur" data-dict="tr_cur"></k-grid-column>
        <k-grid-column data-align="right" data-header="持有份额" data-name="holdVol"></k-grid-column>
        <k-grid-column data-align="right" data-header="持有金额" data-name="holdAmt"></k-grid-column>
        <k-grid-column data-align="right" data-header="折算人民币金额（元）" data-name="convertRmb"></k-grid-column>
      </k-grid>
    </div>


  </div>
</template>

<script>
  export default {
    name: "custVolRegister",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{},
        BreathDay:[],
        HoldDate:[]
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      }
    },
    watch: {
      // 查询起息日
      BreathDay() {
        this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
        this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
      },
      HoldDate() {
        this.$set(this.searchParam, 'holdStartDate', this.HoldDate == null ? '' : this.HoldDate[0]);
        this.$set(this.searchParam, 'holdEndDate', this.HoldDate == null ? '' : this.HoldDate[1]);
      },
    }
  };
</script>
