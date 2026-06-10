<template>
  <div>
    <div>
      <k-form-search-customize v-model="searchParam" data-target="disclosureProdGrid">
        <k-form-item label="报告期（月份）">
          <k-field-date v-model="queryParamDateRange" data-type="daterange" data-date-format="yyyyMM"
                        data-value-format="yyyyMM"/>
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-select v-model="searchParam.prodCode" data-action="T8ProdInfo.getProdInfosZG" data-value-field="prodCode"
                          data-display-field="prodCode,prodName"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="searchParam.prodName" :data-max-length="100"></k-field-text>
        </k-form-item>
        <k-form-item label="产品形态">
          <k-field-select v-model="searchParam.prodForm" data-dict="xp_prod_form"></k-field-select>
        </k-form-item>
        <k-form-item label="募集方式">
          <k-field-select v-model="searchParam.prodClcMth" data-dict="xp_raise_type"></k-field-select>
        </k-form-item>
        <k-form-item label="产品投资性质">
          <k-field-select v-model="searchParam.prodInvTyp" data-dict="xp_prod_invest_nature"></k-field-select>
        </k-form-item>
        <k-form-item label="信披子类型" >
          <k-field-select v-model="searchParam.disclosureSonType" :data-data="addDocTypeDict" data-value-field="value" data-display-field="text"></k-field-select>
        </k-form-item>
        <k-btn slot="button" class="md-rose" data-functype="EXPORT" data-target="disclosureProdGrid"
               :data-export-name="'定期报告台账'" v-if="global.isShowAuthorityButton('RegularDisProdConfirm.exportRightControl')">
          <md-icon>cloud_download</md-icon>
          导出
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="disclosureProdGrid" @data-row-select="selectRow" :data-autoload="true" :data-operate-column="false"
              data-action="RegularDisProdConfirm.findRegularDisProdConfirmAuth">
        <k-grid-column data-align="left" data-header="公告id" data-name="noticeId" data-hidden="true"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品代码" data-name="prodCode" data-width="110"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品名称" data-name="prodName" data-width="230"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品形态" data-name="prodForm" data-dict="xp_prod_form" data-width="90"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品投资性质" data-name="prodInvTyp" data-dict="xp_prod_invest_nature" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="募集方式" data-name="prodClcMth" data-dict="xp_raise_type" data-width="70"></k-grid-column>
<!--        <k-grid-column data-align="center" data-header="模板名称" data-name="modDocName" data-width="100"></k-grid-column>
        <k-grid-column data-align="center" data-header="模板版本号" data-name="modVersionNumber" data-width="100"></k-grid-column>-->
        <k-grid-column data-align="left" data-header="公告标题" data-name="noticeTitle" data-width="265"></k-grid-column>
        <k-grid-column data-align="left" data-header="信披子类型" data-name="disclosureSonType" data-dict="xp_son_type" data-width="130" ></k-grid-column>
        <k-grid-column data-align="left" data-header="产品成立日" data-name="establishDate" data-type="date" data-width="90"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品到期日" data-name="endDate" data-type="date" data-width="90"></k-grid-column>
        <k-grid-column data-align="left" data-header="基准日期" data-name="prodBaseDate" data-type="date" data-width="90"></k-grid-column>
        <k-grid-column data-align="left" data-header="报告日期" data-name="reportDate" data-type="date" data-width="90"></k-grid-column>
        <k-grid-column data-align="left" data-header="距成立日天数" data-name="toEstablishDateDays" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="距到期日天数" data-name="toEndDateDays" data-width="100"></k-grid-column>
      </k-grid>
    </div>
  </div>
</template>

<script>
import DisclosureProdRuleAdd from "@/pages/pms/disclosureControl/prodDisclosureRule/DisclosureProdRuleAdd";
import DisclosureProdRuleEdit from "@/pages/pms/disclosureControl/prodDisclosureRule/DisclosureProdRuleEdit";
import DisclosureProdRuleCopy from "@/pages/pms/disclosureControl/prodDisclosureRule/DisclosureProdRuleCopy";
import DisclosureProdRuleDetail from "@/pages/pms/disclosureControl/prodDisclosureRule/DisclosureProdRuleDetail";
import httpUtil from "@/frame/httpUtil";
import Tools from "@/utils/tools";

export default {
  name: "RegularDisProdConfirmList",
  components: {
    DisclosureProdRuleAdd, DisclosureProdRuleEdit, DisclosureProdRuleCopy, DisclosureProdRuleDetail

  },
  data() {
    return {
      addDocTypeDict:'',
      formData: {},
      selectRowData: {},
      searchParam: {},
      userid: '',
      queryParamDateRange: [],
    };
  },
  created() {
    this.queryProd();
  },

  watch: {
    queryParamDateRange() {
      this.$set(this.searchParam, 'startMonth', this.queryParamDateRange == null ? '' : this.queryParamDateRange[0]);
      this.$set(this.searchParam, 'endMonth', this.queryParamDateRange == null ? '' : this.queryParamDateRange[1]);
    }
  },
  methods: {
    queryProd(){
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: '5'}
      }).then(data => {
        this.addDocTypeDict = data.rows;
      }).catch({})
    },
    renderDateTimeCreate(row) {
      return Tools.formatDateTime(row.crtDate, row.crtTime);
    },
    //行被选中事件
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    },
    getUserPermission() {
      this.global.getRoleAndProd('');
    },
  }
};
</script>
