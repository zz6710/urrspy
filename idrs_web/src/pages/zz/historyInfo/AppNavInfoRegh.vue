<template>
  <div class="py-page">
     <div>
        <k-form-search-customize data-model-name="AppNavInfoRegh" data-target="AppNavInfoReghGrid"  v-model = "searchParam">
              <k-form-item label="产品登记编码">
                <k-field-text v-model="searchParam.prodRegEnc"/>
              </k-form-item>
              <k-form-item label="业务登记日期">
                <k-field-date v-model="RegisterDate" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
              </k-form-item>
            </k-form-search-customize>
     </div>

    <div class="py-page-container">
      <k-grid ref="AppNavInfoReghGrid" @data-row-select="selectRow"  data-operate-column="false" data-action="AppNavInfoRegh.findAppNavInfoRegs" >
	        <k-grid-column data-header="报送日期" data-name="reportDate" data-export="false"></k-grid-column>
          <k-grid-column data-header="*发行机构代码" data-name="bankCode" data-width="120"></k-grid-column>
          <k-grid-column data-header="*产品登记编码" data-name="prodRegEnc" data-width="130"></k-grid-column>
          <k-grid-column data-header="*净值登记类型" data-name="navRegType" data-dict="navRegType" data-width="120" data-dict-type="1"></k-grid-column>
          <k-grid-column data-header="产品子份额代码" data-name="sonShareCode" data-width="120"></k-grid-column>
          <k-grid-column data-header="*币种" data-name="cny"></k-grid-column>
          <k-grid-column data-header="*净值" data-name="nav" data-width="120"></k-grid-column>
          <k-grid-column data-header="*折算人民币净值" data-name="rmbNav" data-width="140"></k-grid-column>
          <k-grid-column data-header="达基净值" data-name="djNav" data-width="120"></k-grid-column>
          <k-grid-column data-header="*累计净值" data-name="totalNav" data-width="120"></k-grid-column>
          <k-grid-column data-header="*折算人民币累计净值" data-name="rmbTotalNav" data-width="140"></k-grid-column>
          <k-grid-column data-header="*复权净值" data-name="fqNav" data-width="120"></k-grid-column>
          <k-grid-column data-header="*折算人民币复权净值" data-name="rmbFqNav" data-width="140"></k-grid-column>
          <k-grid-column data-header="*估值依据" data-name="navCalType" data-dict="navCalType" data-dict-type="1"></k-grid-column>
          <k-grid-column data-header="*份额" data-name="share" data-width="120"></k-grid-column>
          <k-grid-column data-header="*净值日期" data-name="navDate"></k-grid-column>
          <k-grid-column data-header="披露日期" data-name="disclosureDate"></k-grid-column>
          <k-grid-column data-header="*存续余额（元）" data-name="remainBal" data-width="120"></k-grid-column>
          <k-grid-column data-header="*折算人民币存续余额（元）" data-name="rmbRemainBal" data-width="170"></k-grid-column>
          <k-grid-column data-header="备注" data-name="details"></k-grid-column>
          <k-grid-column data-header="母产品代码" data-name="motherFundCode" data-width="120" :data-export="isExportField"></k-grid-column>
          <k-grid-column data-header="产品类型" data-name="openType" data-dict="openType" data-width="120" :data-export="isExportField"></k-grid-column>
          <k-grid-column data-header="0份额标识" data-name="volZeroFlag" data-dict="vol0Flag" data-width="120" :data-export="isExportField"></k-grid-column>
          <k-grid-column data-header="子产品成立日" data-name="establishDate" data-width="120" :data-export="isExportField"></k-grid-column>
          <k-grid-column data-header="子产品到期日" data-name="endDate" data-width="120" :data-export="isExportField"></k-grid-column>
          <k-grid-column data-header="上一基准日" data-name="jzDate" data-width="120" :data-export="isExportField"></k-grid-column>
          <k-grid-column data-header="业绩比较基准" data-name="prfrBnch"  data-width="120" :data-export="isExportField"></k-grid-column>
          <k-grid-column data-header="业绩基准类型" data-name="prfrBnchTyp" data-width="120" data-dict="prfrBnchTyp" :data-export="isExportField"></k-grid-column>
          <k-grid-column data-header="业绩基准上限" data-name="intrRtUpp" data-width="120" :data-export="isExportField"></k-grid-column>
          <k-grid-column data-header="业绩基准下限" data-name="intrRtFlr" data-width="120" :data-export="isExportField"></k-grid-column>
          <k-grid-column data-header="业绩基准说明" data-name="prfrBnchTypDscr" data-width="180" :data-export="isExportField"></k-grid-column>
          <k-grid-column data-header="万份收益" data-name="enDwjjsy" data-width="180" :data-export="isExportField"></k-grid-column>
          <k-grid-column data-header="上一基准日净值" data-name="lstJzNav" data-width="180" :data-export="isExportField"></k-grid-column>
          <k-grid-column data-header="上一工作日单位净值" data-name="lstWkdNav" data-width="180" :data-export="isExportField"></k-grid-column>
          <k-grid-column data-header="是否迁移产品" data-name="isProdTransfer" data-dict="1yes2no" data-width="180" :data-export="isExportField"></k-grid-column>
          <k-grid-column data-header="迁移产品迁移净值" data-name="transferNav" data-width="180" :data-export="isExportField"></k-grid-column>
          <k-grid-column data-header="净值日指数" data-name="navDtIndex" data-width="180" :data-export="isExportField"></k-grid-column>
          <k-grid-column data-header="净值日上一工作日指数" data-name="lstWkdIndex" data-width="180" :data-export="isExportField"></k-grid-column>
      </k-grid>
    </div>


  </div>
</template>

<script>
  export default {
    name: "AppNavInfoRegh",
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
        this.$set(this.searchParam, 'reportBeginDate', this.RegisterDate == null ? '' : this.RegisterDate[0]);
        this.$set(this.searchParam, 'reportEndDate', this.RegisterDate == null ? '' : this.RegisterDate[1]);
     },
    }
  };
</script>
