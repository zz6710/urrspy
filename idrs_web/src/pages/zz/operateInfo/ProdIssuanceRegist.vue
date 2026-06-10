<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="ProdIssuanceRegist" data-label-width="130px" data-target="prodIssuanceRegistGrid"  v-model = "searchParam">
       <k-form-item label="操作日期">
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="操作人员">
          <k-field-text v-model="searchParam.summitUser"/>
        </k-form-item>

        <k-form-item label="操作类型">
          <k-field-select v-model="searchParam.opType"  data-dict="op_type"/>
        </k-form-item>
        <k-form-item label="产品登记编码">
          <k-field-text v-model="searchParam.prodCode"/>
        </k-form-item>
        <k-form-item label="理财产品代码">
          <k-field-text v-model="searchParam.prodIdentCode" />
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <k-grid ref="prodIssuanceRegistGrid" @data-row-select="selectRow" data-operate-column="false" data-action="ProdIssuanceRegist.findProdIssuanceRegists" >
        <k-grid-column data-align="left" data-header="操作人员" data-name="summitUser"  data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作类型" data-name="opType" data-dict="op_type" data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作日期" data-name="createDate" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作时间" data-name="createTime" data-type="time" data-width="80"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品登记编码" data-name="prodCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="理财产品代码" data-name="prodIdentCode" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="id" data-name="id" :data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构代码" data-name="bankCode" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="募集起始日期" data-name="subscriptionStartDate"  data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="募集结束日期" data-name="subscriptionEndDate"  data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品起始日期" data-name="prodValueDate"  data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品终止日期" data-name="prodMaturityDate"  data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="管理方式" data-name="managementMethod" data-dict="subm_managementMethod" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否为结构化（分级）产品" data-name="structuredProd" data-dict="subm_isTrue" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="分级比列" data-name="clsfSto" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="业绩比较基准下限%" data-name="upLimitPerRate" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="业绩比较基准上限%" data-name="lowLimitPerRate" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="业绩比较基准说明" data-name="detailsPerRate" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="开放模式" data-name="openingMode" data-dict="subm_open_mod" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="规律开放周期" data-name="regularOpenPeriod" data-dict="subm_t8_open_calendar" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="定期开放周期（天）" data-name="regularOpenPeriodDay" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="其他规律开放周期(天)" data-name="otherOpenPeriod" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="无规律开放说明" data-name="disorderOpenPeriod" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="首次开放周期起始日" data-name="firstOpenDay"  data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="节假日是否开放" data-name="holidayOpenType" data-dict="subm_isTrue" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="平均开放次数（年化）" data-name="averageOpenNo" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="开放期业务" data-name="busiOpenPeriod" data-dict="subm_t8_open_control" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="开放期业务说明" data-name="detailsBusiOpPeriod" data-width="150"></k-grid-column>
      </k-grid>
    </div>




  </div>
</template>

<script>
  export default {
    name: "prodIssuanceRegist",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{},
        BreathDay:[]
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
        console.log(this.BreathDay);
        this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
        this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
      },
    }
  };
</script>
