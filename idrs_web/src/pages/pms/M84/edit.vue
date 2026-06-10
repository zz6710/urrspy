<template>
  <k-form ref="editForm" :data-col="2">
    <k-form-item label="产品代码">
      <k-field-text v-model="value.prodCode" data-disabled="true"/>
    </k-form-item>
    <k-form-item label="销售商代码">
      <k-field-text v-model="value.distributorCode" data-disabled="true"/>
    </k-form-item>
    <k-form-item label="启用日期">
      <k-field-text v-model="value.enableDate" data-disabled="true" />
    </k-form-item>
    <k-form-item label="计提方式">
      <k-field-select v-model="value.tailingCalcMode" data-dict="pgmtype"/>
    </k-form-item>
    <k-form-item label="年天数">
      <k-field-select v-model="value.tailingCommisionYeardays" data-dict="pgmtype"/>
    </k-form-item>
    <k-form-item label="起付金额">
      <k-field-text v-model="value.minPayAmt"/>
    </k-form-item>
    <k-form-item label="是否累进计算">
      <k-field-select v-model="value.graduatedCalc" data-dict="pgmtype"/>
    </k-form-item>
    <k-form-item label="更新人">
      <k-field-text v-model="value.updUser"/>
    </k-form-item>
    <k-form-item label="备注">
      <k-field-text v-model="value.remark" data-allowblank/>
    </k-form-item>
    <k-form-footer data-align="center">
      <k-btn data-functype="SUBMIT" data-action="T8PrjTailingCommision.updateTaPrjTailingCommision"
             data-from="editForm" :data-model="value" data-target="grid">
        确定
      </k-btn>
      <k-btn data-functype="CLOSE">取消</k-btn>
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
