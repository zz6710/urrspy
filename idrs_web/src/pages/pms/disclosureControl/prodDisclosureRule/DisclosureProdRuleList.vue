<template>
  <div>
    <div>
      <k-form-search-customize v-model="searchParam" data-target="disclosureProdRuleGrid">
        <k-form-item label="产品代码">
          <k-field-select v-model="searchParam.prodCode" data-action="T8ProdInfo.getProdInfosZG" data-value-field="prodCode"
                          data-display-field="prodCode,prodName"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="searchParam.prodName" data-validate-type="text" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="模板名称">
          <k-field-text v-model="searchParam.t8DisclosureModName" data-validate-type="text" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="数据来源">
          <k-field-select v-model="searchParam.source" data-dict="xp_prod_disclosure_rule_source"/>
        </k-form-item>
        <k-form-item label="信披类型">
          <k-field-select v-model="searchParam.disclosureType" :data-data="DocTypeDict"   data-value-field="value" data-display-field="text" @data-on-change="queryProd"></k-field-select>
        </k-form-item>
        <k-form-item label="信披子类型" v-if="searchParam.disclosureType==='5'||searchParam.disclosureType==='6'||searchParam.disclosureType==='1'||searchParam.disclosureType==='9'">
          <k-field-select v-model="searchParam.disclosureSonType" :data-data="addDocTypeDict" data-value-field="value" data-display-field="text"></k-field-select>
        </k-form-item>
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
               data-target="addDisclosureProdRulePopup" slot="button" v-if="global.isShowAuthorityButton('DisclosureProdRule.addDisclosureProdRule')">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
        <k-btn class="btn-custom-primary" data-functype="POPUP"
               data-target="copyDisclosureProdRulePopup" slot="button" v-if="global.isShowAuthorityButton('DisclosureProdRule.copyDisclosureProdRule')">
          <md-icon>content_copy</md-icon>
          复制
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="disclosureProdRuleGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="250px"
              data-action="DisclosureProdRule.findDisclosureProdRulesAuth">
        <k-grid-column data-header="产品规则id" data-name="id" :data-sortable="true" data-default-sort="DESC" data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品代码" data-name="prodCode" data-width="110"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品名称" data-name="prodName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品全称" data-name="prodFullName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="信披类型" data-name="disclosureType" data-dict="xp_doc_type" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="信披子类型" data-name="disclosureSonType" data-dict="xp_son_type" data-width="130"></k-grid-column>
        <k-grid-column data-align="left" data-header="模板名称" data-name="t8DisclosureModName" data-width="130"></k-grid-column>
        <k-grid-column data-align="left" data-header="版本号" data-name="versionNumber" data-width="63"></k-grid-column>
        <k-grid-column data-align="left" data-header="公告标题" data-name="noticeTitle" data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否需要补录" data-name="ifClearing" data-dict="xp_if_ok" data-hidden="true" data-export="false" data-width="50"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否复核" data-name="ifCondition" data-dict="if_ok" data-width="70"></k-grid-column>
        <k-grid-column data-align="left" data-header="发起方式" data-name="startRule" data-dict="xp_disclosure_start_rule" data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="基准日期" data-name="baseDate" data-dict="xp_disclosure_base_date" data-width="105"></k-grid-column>
        <k-grid-column data-align="left" data-header="数据来源" data-name="source" data-dict="xp_prod_disclosure_rule_source" data-width="80"></k-grid-column>

        <k-grid-column data-align="left" data-header="创建时间" data-name="crtDate" data-type="date" data-render="renderDateTimeCreate" data-width="125"></k-grid-column>
        <k-grid-column data-align="left" data-header="创建人" data-name="crtUserName" data-width="100"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn data-functype="POPUP" data-confirm data-size="mini" class="btn-custom-plain" style="margin:0"
                 data-descript="详情"
                 data-target="detailDisclosureProdRulePopup">
            详情
          </k-btn>
          <k-btn class="btn-custom-plain" data-descript="修改产品信披规则" data-functype="POPUP" data-size="mini" style="margin:0"
                 data-target="editDisclosureProdRulePopup" :openType="'edit'" v-if="global.isShowAuthorityButton('DisclosureProdRule.updateDisclosureProdRule')">
            修改
          </k-btn>
          <k-btn class="md-danger" data-functype="SUBMIT"
                 data-action="DisclosureProdRule.deleteDisclosureProdRule" data-size="mini" style="margin:0"
                 data-type="danger" data-target="disclosureProdRuleGrid" :data-confirm="true" data-descript="删除产品信披规则"
                 v-if="global.isShowAuthorityButton('DisclosureProdRule.deleteDisclosureProdRule')">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>
    <!-- 产品信披规则详情 -->
    <k-popup ref="detailDisclosureProdRulePopup" data-title="详情"  :data-dialog-drag="true"  data-width="760px">
      <disclosure-prod-rule-detail :formData="formData" :disclosureType="formData.disclosureType"></disclosure-prod-rule-detail>
    </k-popup>
    <!--    添加产品信披规则弹出框   -->
    <k-popup ref="addDisclosureProdRulePopup" data-title="新增"  :data-dialog-drag="true" data-width="760px">
      <disclosure-prod-rule-add :formData="formData" ></disclosure-prod-rule-add>
    </k-popup>

    <!--    修改产品信披规则弹出框   -->
    <k-popup ref="editDiscllosureProdRulePopup" data-title="修改"  :data-dialog-drag="true" data-width="760px">
      <disclosure-prod-rule-edit  :formData="formData"></disclosure-prod-rule-edit>
    </k-popup>

    <!-- 复制产品信披规则 -->
    <k-popup ref="copyDisclosureProdRulePopup" data-title="复制" :data-dialog-drag="true">
      <disclosure-prod-rule-copy></disclosure-prod-rule-copy>
    </k-popup>
  </div>
</template>

<script>
import DisclosureProdRuleAdd from "@/pages/pms/disclosureControl/prodDisclosureRule/DisclosureProdRuleAdd";
import DisclosureProdRuleEdit from "@/pages/pms/disclosureControl/prodDisclosureRule/DisclosureProdRuleEdit";
import DisclosureProdRuleCopy from "@/pages/pms/disclosureControl/prodDisclosureRule/DisclosureProdRuleCopy";
import DisclosureProdRuleDetail from "@/pages/pms/disclosureControl/prodDisclosureRule/DisclosureProdRuleDetail";
import Tools from "@/utils/tools";

export default {
  name:"DisclosureProdRuleList",
  components: {
    DisclosureProdRuleAdd, DisclosureProdRuleEdit, DisclosureProdRuleCopy, DisclosureProdRuleDetail
  },
  data() {
    return {
      addDocTypeDict:{},
      formData: {},
      selectRowData: {},
      DocTypeDict: {},
      searchParam: {},
      oldSelectChannels: '',//原来被选中的渠道
    };
  },
  created() {
    this.xpType();
    this.queryProd(this.searchParam.disclosureType);
  },
  methods: {
    xpType() {
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeInProd",
        params: null
      }).then(data => {
        this.DocTypeDict = data.rows;
      }).catch({})
    },
    queryProd(disclosureType){
      this.$set(this.searchParam, 'disclosureSonType', '');
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: disclosureType}
      }).then(data => {
        this.addDocTypeDict = data.rows;
      }).catch({})
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row);
      this.formData = Object.assign({}, row);
    },
    renderDateTimeCreate(row) {
      return Tools.formatDateTime(row.crtDate, row.crtTime);
    },
  }
};
</script>
