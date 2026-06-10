<template>
  <!-- 认申赎确认日 -->
  <div>
    <k-form class="my-form " :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">

      <k-form-item label="认购确认日">
        <label class="k-field-label"></label>
        <k-field-select   v-model="t8ProdOpen.subsCfmN" data-dict="subs_cfm_n"
          data-displaykeyvalue="true"  data-allowblank="false"  data-disabled="false" />
      </k-form-item>
      <k-form-item label="申购确认日">
        <label class="k-field-label"></label>
        <k-field-select v-model="t8ProdOpen.applyCfmM" data-dict="apply_cfm_m"
          data-allowblank="false"  data-displaykeyvalue="true" />
      </k-form-item>
      <k-form-item label="赎回确认日">

        <label class="k-field-label"></label>
        <k-field-select v-model="t8ProdOpen.redeemCfmM" data-dict="redeem_cfm_m"
          data-allowblank="false"  data-displaykeyvalue="true" />
      </k-form-item>
    </k-form>

  </div>
</template>

<script>
  export default {
    model: {
      prop: 't8ProdOpen',
      event: 'input'
    },
    props: {
      t8ProdOpen: {}
    },
    data() {
      return {}
    },
    methods: {},
    created() {

    },
    mounted() {},
    watch: {

    },

  }
</script>

<style>

</style>
