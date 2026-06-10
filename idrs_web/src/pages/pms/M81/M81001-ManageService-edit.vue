<template>
  <k-form ref="editForm" :data-col="2">
    <k-form-item label="产品代码" v-show="false">
      <k-field-text v-model="value.prodCode" :data-disabled="true" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="启用日期">
      <k-field-date v-model="value.enableDate"   :data-disabled="true" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="管理费计算方式">
      <k-field-select v-model="value.calcType"  data-dict="calSalesFee" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="年天数">
      <k-field-select v-model="value.managefeeYeardays" data-dict="yeardays" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="净值取值规则">
      <k-field-select v-model="value.navRule" data-dict="nav_rule" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="管理费率(%)">
      <k-field-text data-validate-type="number" v-model="value.rate" :data-allowblank="false"  data-min-value="0" data-max-value="100" data-digits="5"/>
    </k-form-item>
    <k-form-item label="备注">
      <k-field-text v-model="value.remark" :data-max-length="256"/>
    </k-form-item>
    <k-form-footer data-align="center">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T83007.updateT83007"
            data-from="editForm" :data-model="value"
            data-target="T81001Grid" :data-handler="beforeSubmit">
           <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
           </k-btn>
     <k-btn class="btn-custom-plain" data-functype="CLOSE">
           <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
    </k-form-footer>
  </k-form>
</template>

<script>
  import kayak from '@/frame/kayak.js'

  export default {
    props: {
      updSuccess: Function
    },
    data() {
      return {
      };
    },
    methods: {
    },
    computed: {
      value() {
        return this.$attrs.value
      }
    }
  };
</script>
