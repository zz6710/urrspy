<template>
  <k-form ref="editForm" :data-col="3">
    <k-form-item label="产品代码" v-show="false">
      <k-field-text v-model="value.prodCode" :data-allowblank="false" data-disabled/>
    </k-form-item>
    <k-form-item label="服务费类型">
      <k-field-select v-model="value.feeType" data-dict="sale_manager_fee_type" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="计提开始日">
      <k-field-date v-model="value.startDate" :data-allowblank="false" ct data-disabled :data-max-value="value.endDate"/>
    </k-form-item>
    <k-form-item label="计提截止日">
      <k-field-date v-model="value.endDate" :data-allowblank="false"  data-disabled :data-min-value="value.startDate"/>
    </k-form-item>
    <k-form-item label="计划支付日">
      <k-field-date v-model="value.theoryPayDate" :data-allowblank="false" :data-min-value="'('+value.endDate"/>
    </k-form-item>
    <k-form-item label="实际支付日">
      <k-field-date v-model="value.factPayDate" :data-min-value="'('+value.endDate"/>
    </k-form-item>
    <k-form-item label="创建人" v-show="false">
      <k-field-text v-model="value.crtUser"/>
    </k-form-item>
    <k-form-item label="备注">
      <k-field-text v-model="value.remark" :data-max-length="150"/>
    </k-form-item>
    <k-form-footer data-align="center">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T83002.updateT83002"
            data-from="editForm" :data-model="value" data-target="T81001Grid" >
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
