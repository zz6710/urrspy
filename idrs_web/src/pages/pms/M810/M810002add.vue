<template>
  <k-form  ref="addForm"  :data-col="2">
    <k-form-item label="产品代码" >
      <k-field-select v-model="value.productCode"  :data-allowblank="false"
                    data-action="T810002.findTaProdInfos" data-display-field="productName,productCode" data-value-field="productCode" data-params="" @data-on-change="queryProd" ></k-field-select>
    </k-form-item>
    <k-form-item label="产品名称" >
      <k-field-text v-model="productName" :data-disabled="true"  :data-allowblank="false" :dataMaxLength="80" data-regx="^\S*$" data-regx-text="不能包含空格"></k-field-text>
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
      <k-field-text v-model="proEntrustAcc"  :data-allowblank="false" :data-disabled="true"></k-field-text>
    </k-form-item>
    <k-form-item label="发行币种" >
      <k-field-select v-model="amtType" :data-disabled="true"  :data-allowblank="false" data-dict="currtype"></k-field-select>
    </k-form-item>
    <k-form-item label="是否轧差" >
      <k-field-select v-model="value.nettingFlag"  :data-allowblank="false" data-dict="netting_flag"></k-field-select>
    </k-form-item>
    <k-form-footer data-align="center">
      <k-btn data-target="queryTable" data-functype="SUBMIT" class="btn-custom-primary" :data-model="value" data-action="T810002.add" data-from="addForm">
        <i class="icon-confirm"></i>确定
      </k-btn>
      <k-btn  data-functype="CLOSE" class="btn-custom-plain">
        <i class="icon-cancel"></i>取消
      </k-btn>
    </k-form-footer>
  </k-form>
</template>

<script>
  import Tools from '@/utils/tools.js';
  export default {
    props:{
      updSuccess:Function
    },
    data(){
      return {
        amtType:'',
        proEntrustAcc:'',
        productName:''
      }
    },
    computed: {
      value() {
        return this.$attrs.value;
      },
    },
    methods:{
      queryProd(){
        this.productName ='';
        this.amtType = '';
        this.proEntrustAcc= '';
        this.$attrs.value.proEntrustAcc= '';
        this.$attrs.value.amtType = '';
        this.$attrs.value.productName = '';
        this.httpUtil.comnQuery({
          action:"T810002.queryDeposit",
          params:{"productCode":this.value.productCode}
        }).then(data=>{
          if(!data.rows[0]){
            Tools.alert("未查询到该产品托管账户信息！","danger");
            return
          }
          this.productName = data.rows[0].productName;
          this.amtType = data.rows[0].amtType;
          this.proEntrustAcc= data.rows[0].proEntrustAcc;
          this.$attrs.value.proEntrustAcc= data.rows[0].proEntrustAcc;
          this.$attrs.value.amtType = data.rows[0].amtType;
          this.$attrs.value.productName = data.rows[0].amtType;
        });
        this.checkProductCode()
      },
      checkProductCode(){
        this.httpUtil.comnQuery({
          action:"T810002.checkProdCode",
          params:{"productCode":this.value.productCode}
        }).then(data=>{
          if(data.rows.length>0){
            Tools.alert("该产品已存在！","danger");
          }
        })
      }
    }
  }
</script>

<style scoped>

</style>
