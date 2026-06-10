<template>

  <div>
    <!-- 精度信息-->
    <k-form class="my-form " ref= "capitalInfo" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-item label="费用精度处理方式">
        <k-field-select v-model="t8ProdPrecision.feePrecisionMth" data-dict="precisionProcess" :data-allowblank="false"
                        data-default-value="0" data-displaykeyvalue="true"/>
      </k-form-item>

      <k-form-item label="金额精度处理方式">
        <k-field-select v-model="t8ProdPrecision.moneyPrecisionMth" data-dict="precisionProcess"
                        data-displaykeyvalue="true"
                        :data-allowblank="false" data-default-value="0"/>
      </k-form-item>

      <k-form-item label="份额精度处理方式">
        <k-field-select v-model="t8ProdPrecision.volumnPrecisionMth" data-dict="precisionProcess"
                        data-displaykeyvalue="true"
                        :data-allowblank="false" data-default-value="0"/>
      </k-form-item>

      <k-form-item label="分红精度处理方式">
        <k-field-select v-model="t8ProdPrecision.dividPrecisionMth" data-dict="precisionProcess"
                        data-displaykeyvalue="true"
                        :data-allowblank="false" data-default-value="0"/>
      </k-form-item>

      <k-form-item label="利息精度处理方式">
        <k-field-select v-model="t8ProdPrecision.interestPrecisionMth" data-dict="precisionProcess"
                        data-displaykeyvalue="true" :data-allowblank="false" data-default-value="0"/>
      </k-form-item>

      <k-form-item label="归资产精度处理方式">
        <k-field-select v-model="t8ProdPrecision.profitPrecisionMth" data-dict="precisionProcess"
                        data-displaykeyvalue="true"
                        :data-allowblank="false" data-default-value="0"/>
      </k-form-item>

      <k-form-item label="收益率精度处理方式">
        <k-field-select v-model="t8ProdPrecision.ratePrecisionMth" data-dict="precisionProcess"
                        data-displaykeyvalue="true"
                        :data-allowblank="false" data-default-value="0"/>
      </k-form-item>
    </k-form>

  </div>
</template>

<script>
  export default {
    model: {
      prop: 't8ProdInfo',
      event: 'input'
    },
    props:{
      t8ProdInfo: {},
      t8ProdPrecision: {}
    },
    data() {
      return {

      }
    },
    methods: {
      validateData() {
        return this.$refs.capitalInfo.validate();
      },
    },
    created() {

    },
    mounted() {},
    watch: {
      t8ProdPrecision: {
         handler: function(val) {
            this.$emit('@changeProdPrecision', val);
          },
          deep: true
      }
    }

  }
</script>

<style>

</style>
