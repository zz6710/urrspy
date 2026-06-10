<template>
  <k-form ref="editForm"  :data-col="2" >
    <k-form-item label="产品代码" >
      <k-field-select v-model="value.productCode"  :data-allowblank="false" :data-disabled="true"
                      data-action="T810002.findTaProdInfos" data-display-field="productName,productCode" data-value-field="productCode"  ></k-field-select>
    </k-form-item>
    <k-form-item label="产品名称" >
      <k-field-text v-model="value.productName" :data-disabled="true"  :data-allowblank="false" :dataMaxLength="80" data-regx="^\S*$" data-regx-text="不能包含空格"></k-field-text>
    </k-form-item>
    <k-form-item label="TA清算账户" >
      <k-field-select v-model="value.taSettleAcc"  :data-allowblank="false"
                      data-action="T810001.queryWithType" data-display-field="bankAccName,bankAccNumber" data-value-field="acctId" data-params="{'settleAccType':'1'}"></k-field-select>
    </k-form-item>
    <k-form-item label="管理人财务账户" >
      <k-field-select v-model="value.mgrFinanceAcc" :data-allowblank="false"
                      data-action="T810001.queryWithType" data-display-field="bankAccName,bankAccNumber" data-value-field="acctId" data-params="{'settleAccType':'6'}"></k-field-select>
    </k-form-item>
    <k-form-item label="TA募集账户" >
      <k-field-select v-model="value.taCollectAcc"  :data-allowblank="false"
                      data-action="T810001.queryWithType" data-display-field="bankAccName,bankAccNumber" data-value-field="acctId" data-params="{'settleAccType':'0'}"></k-field-select>
    </k-form-item>
    <k-form-item label="产品托管户" >
      <k-field-text v-model="value.proEntrustAcc"   :data-allowblank="false" :data-disabled="true"></k-field-text>
    </k-form-item>
    <k-form-item label="发行币种" >
      <k-field-select v-model="value.amtType"  :data-allowblank="false" data-dict="currtype" :data-disabled="true"></k-field-select>
    </k-form-item>
    <k-form-item label="是否轧差" >
      <k-field-select v-model="value.nettingFlag"  :data-allowblank="false" data-dict="netting_flag"></k-field-select>
    </k-form-item>
    <k-form-footer data-align="center">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T810002.update"
             data-from="editForm" :data-model="value"
             data-target="queryTable" :data-handler="beforeSubmit">
        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
      </k-btn>
      <k-btn class="btn-custom-plain" data-functype="CLOSE">
        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
    </k-form-footer>
  </k-form>
</template>

<script>
  export default {
    name: "edit",
    props:{
      updSuccess:Function
    },
    data(){
      return {}
    },
    computed: {
      value() {
        return this.$attrs.value;
      },
      beforeSubmit(){

      }
    }
  }
</script>

<style scoped>

</style>
