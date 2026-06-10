<template>

  <div>
    <!-- 周期信息 -->
    <k-form class="my-form " :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-item label="是否支持自动续投">

        <k-field-select data-allowblank="false" v-model="T8ProdInfo.isVoluntBuy" data-dict="is_volunt_buy"
          data-value="0" data-disabled="false" data-displaykeyvalue="true" />
      </k-form-item>
      <k-form-item label="投资周期（天）">

        <k-field-text v-model="T8ProdInfo.investDays"  :data-max-length="4"
          placeholder="天" data-disabled="false" />
      </k-form-item>
      <k-form-item label="申购截止日">

        <k-field-date v-model="T8ProdInfo.applyEndDate" data-disabled="false" />
      </k-form-item>
      <k-form-item label="自动续投申购确认延迟天数">

        <k-field-text v-model="T8ProdInfo.applyCfmDays"  :data-max-length="4"
          placeholder="天" data-disabled="false" />
      </k-form-item>
      <k-form-item label="自动续投投资次数限制天数">

        <k-field-text v-model="T8ProdInfo.investNumDays"  :data-max-length="4"
          placeholder="天" data-disabled="false" />
      </k-form-item>
    </k-form>
  </div>
</template>

<script>
  export default {
    computed: {},
    data() {
      return {

        T8ProdInfo: {}
      }
    },
    methods: {},
    created() {

    },
    mounted() {},
    watch: {},

  }
</script>

<style>

</style>
