<template>

  <div>
    <!--- 托管账户信息 -->
    <k-form class="my-form " ref= "truteeInfo" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-item label="t8ProdInfoId"  v-show="false">
        <k-field-text v-model="T8ProdEscrowAcct.t8ProdInfoId" />
      </k-form-item>
      <k-form-item label="prodCode" v-show="false">
        <k-field-text v-model="T8ProdEscrowAcct.prodCode"/>
      </k-form-item>
      <k-form-item label="境内托管银行">
        <k-field-select  v-model="T8ProdEscrowAcct.t8TruteeInfoId"  data-action="T82006.findTaCustodianBanks"
                         data-display-field="truteeCode,truteeName"  data-value-field="id" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="境外托管银行">
        <k-field-select  v-model="T8ProdEscrowAcct.t8OutTruteeInfoId"   data-action="T82006.findTaCustodianBanks2"
                         data-display-field="truteeCode,truteeName"  data-value-field="id" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="境内托管协议编号">
        <k-field-text v-model="T8ProdEscrowAcct.gdTrutee"  :data-max-length="200"  :data-disabled="true" />
      </k-form-item>

      <k-form-item label="境外托管协议编号">
        <k-field-text v-model="T8ProdEscrowAcct.gdOutTrutee" :data-max-length="200"  :data-disabled="true" />
      </k-form-item>

      <k-form-item label="境内托管协议生成日期">
        <k-field-text v-model="T8ProdEscrowAcct.truteeCustodyData" data-type="date"  :data-disabled="true" />
      </k-form-item>

      <k-form-item label="境外托管协议生成日期">
        <k-field-text v-model="T8ProdEscrowAcct.outTruteeCustodyData" data-type="date"  :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="境内托管落地支行" v-show="false">
        <k-field-text v-model="T8ProdEscrowAcct.truteeBranch" :data-max-length="200" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="境外托管落地支行" v-show="false">
        <k-field-text v-model="T8ProdEscrowAcct.outTruteeBranch"  :data-max-length="200" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="境内托管开户行">
        <k-field-text v-model="T8ProdEscrowAcct.truteeDeposit"  :data-max-length="128" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="境外托管开户行">
        <k-field-text v-model="T8ProdEscrowAcct.outTruteeDeposit"  :data-max-length="128" :data-disabled="true"/>
      </k-form-item>
    </k-form>

  </div>
</template>

<script>
  export default {
    model: {
      prop: 'T8ProdEscrowAcct',
      event: 'input'
    },
    props:{
      T8ProdEscrowAcct: {},
      menuName :'',
    },
    data() {
      return {
      }
    },
    methods: {
      validisplayData() {
        return this.$refs.truteeInfo.validisplay();
      },
    },
    created() {

    },
    mounted() {},
    watch: {
    }

  }
</script>

<style>

</style>
