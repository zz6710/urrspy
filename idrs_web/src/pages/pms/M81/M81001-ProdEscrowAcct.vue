<template>

  <div>
    <!--- 托管账户信息 -->
    <k-form class="my-form " ref= "truteeInfo" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-item label="托管银行">
        <k-field-select  v-model="t8ProdEscrowAcct.bankNo" data-displaykeyvalue="true"
                         :data-allowblank="true" data-action="T82006.findTaCustodianBanks"
                         data-display-field="truteeName" data-value-field="truteeCode" data-disabled/>
      </k-form-item>

      <k-form-item label="托管账户名">
        <k-field-text v-model="t8ProdEscrowAcct.acctName" :data-allowblank="true"
          :data-max-length="32" />
      </k-form-item>

      <k-form-item label="托管账号">
        <k-field-text v-model="t8ProdEscrowAcct.acctNo" :data-allowblank="true"
                      :data-max-length="32" data-validate-type="code" data-type="code"/>
      </k-form-item>

      <k-form-item label="托管账户开户行">
        <k-field-text v-model="t8ProdEscrowAcct.acctBank" :data-allowblank="true"
          :data-max-length="32" />
      </k-form-item>

      <k-form-item label="托管账户币种">
        <k-field-select  v-model="t8ProdEscrowAcct.cur" id="cur" data-dict="currtype" :data-allowblank="true"
          data-displaykeyvalue="true" onfocus="changeModeJianCha()" data-default-value="156"/>
      </k-form-item>

      <k-form-item label="大额支付行号">
        <k-field-text v-model="t8ProdEscrowAcct.largePayNo" :data-allowblank="true"
          :data-max-length="32" data-validate-type="code" data-type="code"/>
      </k-form-item>
    </k-form>

  </div>
</template>

<script>
  export default {
    computed: {},
    model: {
      prop: 't8ProdEscrowAcct',
      event: 'input'
    },
    props:{
      t8ProdEscrowAcct: {},
      truteeCode:{
        type:String,
        default:'',
      }
    },
    data() {
      return {
      }
    },
    methods: {
      validateData() {
        return this.$refs.truteeInfo.validate();
      },
    },
    created() {

    },
    mounted() {},
    watch: {
      'truteeCode'(val){
        this.$set(this.t8ProdEscrowAcct,"bankNo",val);
      }
    }

  }
</script>

<style>

</style>
