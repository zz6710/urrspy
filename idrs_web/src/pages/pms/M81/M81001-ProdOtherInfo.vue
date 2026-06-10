<template>
  <div>
    <k-form class="my-form " ref="otherInfo" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">

      <k-form-item label="投资组合代码">
        <k-field-text v-model="taProdAddition.investGrpCode" data-allowblank="true" data-disabled="false" :data-max-length="4"
        />
      </k-form-item>

      <k-form-item label="产品投资性质(行内理财)">
        <k-field-select data-allowblank="true" v-model="taProdAddition.prodIncestIn" data-dict="investment_type" data-disabled="false"
                        data-displaykeyvalue="true" />
      </k-form-item>

      <k-form-item label="挂钩标的说明">
        <k-field-text v-model="taProdAddition.hookTargetDesc" :data-max-length="255" rows="3" cols="15"></k-field-text>
      </k-form-item>

      <k-form-item label="产品投资性质(代为推介)">
        <k-field-select data-allowblank="true" v-model="taProdAddition.prodIncestOut" data-dict="investment_type" data-disabled="false"
                        data-displaykeyvalue="true" />
      </k-form-item>

<!--      <k-form-item label="产品系列">-->
<!--        <k-field-select v-model="taProdAddition.prod_advance_stop" data-dict="" data-displaykeyvalue="true" />-->
<!--      </k-form-item>-->

      <!--<k-form-item label="品牌代码">
        <k-field-text v-model="taProdAddition.brandCode" :data-max-length="255" rows="3" data-value="" cols="15" ></k-field-text>
      </k-form-item>

      <k-form-item label="产品提前终止权">
        <k-field-select v-model="taProdAddition.prod_advance_stop" data-dict="prod_advance_stop" data-displaykeyvalue="true" />
      </k-form-item>

      <k-form-item label="产品增信标识">
        <k-field-select v-model="taProdAddition.increaseTrust" data-dict="increase_trust" data-displaykeyvalue="true" />
      </k-form-item>

      <k-form-item label="增信机构类型">
        <k-field-select v-model="taProdAddition.trustOrgtype" data-dict="trust_orgtype" data-displaykeyvalue="true" />
      </k-form-item>

      <k-form-item label="增信形式">
        <k-field-select v-model="taProdAddition.trustType" data-dict="trust_type" data-displaykeyvalue="true" />
      </k-form-item>

      <k-form-item label="管理方式">
        <k-field-select v-model="taProdAddition.manageType" data-dict="reserve6" data-displaykeyvalue="true" />
      </k-form-item>

      <k-form-item label="业务模式">
        <k-field-select v-model="taProdAddition.businessMode" data-dict="reserve7" data-displaykeyvalue="true" />
      </k-form-item>

      <k-form-item label="境内托管机构代码">
        <k-field-text v-model="taProdAddition.domesticCode"  :data-max-length="64" />
      </k-form-item>

      <k-form-item label="境外托管机构国别">
        <k-field-text v-model="taProdAddition.overseasCountries"  :data-max-length="64" />
      </k-form-item>

      <k-form-item label="境外托管机构名称">
        <k-field-text v-model="taProdAddition.overseasName" :data-max-length="128"/>
      </k-form-item>

      <k-form-item label="产品对应资产池">
        <k-field-text v-model="taProdAddition.assetPoolCode" :data-max-length="20" />
      </k-form-item>

      <k-form-item label="境外资金运作模式">
        <k-field-select v-model="taProdAddition.overseasCapitalMode" data-dict="outside_amt_help" data-displaykeyvalue="true" />
      </k-form-item>

      <k-form-item label="信托资金投向说明">
        <k-field-text v-model="taProdAddition.trustsCapitalDesc" :data-max-length="255" rows="3" cols="15"></k-field-text>
      </k-form-item>

      <k-form-item label="收益说明">
        <k-field-text v-model="taProdAddition.incomeDesc" :data-max-length="255"></k-field-text>
      </k-form-item>-->
    </k-form>
  </div>
</template>

<script>
  export default {
    computed: {},
    model: {
      prop: 'taProdAddition',
      event: 'input'
    },
    props:{
      taProdAddition: {}
    },
    data() {
      return {
      }
    },
    methods: {

      validateData() {
        return this.$refs.otherInfo.validate();
      },

    },
    created() {

    },
    mounted() {},
    watch: {},

  }
</script>

<style>

</style>
