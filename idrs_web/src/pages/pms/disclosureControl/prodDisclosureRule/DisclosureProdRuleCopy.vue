<template>
  <div>
    <k-form ref="copyDisclosureProdRuleForm" :data-col="2">
      <k-form-item label="复制产品名称" :dataCol="2">
        <k-field-select v-model="ruleData.prodCode" data-action="DisclosureProdRule.findDisclosureProdDict"
                        data-value-field="prodCode"   @data-on-change="queryProd"
                        data-display-field="prodCode,prodName" :dataAllowblank="false"/>
      </k-form-item>
      <k-form-item label="新产品名称" :dataCol="2">
        <k-field-select v-model="ruleData.prodCodes" :data-data="addProd"
                        data-value-field="prodCode"
                        data-display-field="prodCode,prodName" :data-multiple="true" :dataAllowblank="false"/>
      </k-form-item>

      <k-form-footer data-align="center" >
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DisclosureProdRule.copyDisclosureProdRule"
               data-from="copyDisclosureProdRuleForm"
               :data-model="ruleData" data-target="disclosureProdRuleGrid" >
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
        </k-btn>
      </k-form-footer>
    </k-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      ruleData: {},
      addProd:{},
    };
  },
  created() {
    this.queryProd("");
  },
  methods: {
    queryProd(prodCode){
      this.$set(this.ruleData, 'prodCodes', '');
      this.httpUtil.comnQuery({
        action: "DisclosureProdRule.findNeedCopyProds",
        params: {prodCode: prodCode}
      }).then(data => {
        this.addProd = data.rows;
      }).catch({})
    },
  }
};
</script>
