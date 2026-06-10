<template>
  <!-- 开放信息 -->
  <div>
    <k-form class="my-form" ref="openInfo" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">

      <k-form-item label="部分赎回结息方式">
        <k-field-radio v-model="t8ProdInfo.interestConvertType" data-dict="interest_convert_type"
                          :data-allowblank="false" data-default-value="2" :data-disabled="true">
        </k-field-radio>
      </k-form-item>

      <k-form-item label="是否锁定期产品">
        <k-field-select :data-allowblank="false" v-model="t8ProdInfo.isLockProd" data-dict="is_lock_prod"
          :data-disabled="false" data-displaykeyvalue="true" data-default-value="0"/>
      </k-form-item>

      <k-form-item label="锁定期期限（天）">
        <k-field-text v-model="t8ProdInfo.lockDays" placeholder="天" :data-max-length="3"
                      data-validate-type="code" data-type="code" :data-allowblank="t8ProdInfo.isLockProd == '0'"
                      :data-disabled="t8ProdInfo.isLockProd == '0'" />
      </k-form-item>

      <k-form-item label="小份额强赎触发条件">
        <k-field-select  v-model="t8ProdLimit.smallVolMtc" data-dict="small_vol_mtc"
                        @data-on-change="changeSmallVolMtc" :data-allowblank="false"
                        data-displaykeyvalue="true" data-default-value="0"/>
      </k-form-item>

      <k-form-item label="小份额强赎处理方式" v-show="t8ProdLimit.smallVolMtc =='0'">
        <k-field-select  v-model="t8ProdLimit.smallVolMth" data-dict="small_vol_mth"
                        data-displaykeyvalue="true"  :data-allowblank="t8ProdLimit.smallVolMtc != '0'" @input="forceUpdate"/>
      </k-form-item>

      <k-form-item label="巨额赎回处理方式">
        <k-field-select :data-allowblank="false" v-model="t8ProdLimit.redeemMode" data-dict="redeem_mode"
          data-displaykeyvalue="true" data-default-value="0"/>
      </k-form-item>

      <k-form-item label="巨额赎回比例(%)">
        <k-field-text v-model="t8ProdLimit.redeemRatio" data-validate-type="number" data-type="number"
                      data-rate-convert="true" data-max-value="100" data-integer-length="2"
                      data-min-value="0" :data-max-length="9" data-digits="5" :data-allowblank="false"/>
      </k-form-item>

      <k-form-item label="收益尾差处理方式" v-show="prodMode == '3'">
        <k-field-select v-model="t8ProdLimit.incomeMode" data-type="number"  data-dict="prod_income_mode"
                      data-displaykeyvalue="true" />
      </k-form-item>
    </k-form>

  </div>
</template>

<script>
  export default {
    computed: {},
    model: {
      prop: 't8ProdInfo',
      event: 'input'
    },
    props:{
      t8ProdInfo: {},
      t8ProdLimit: {
      },
      prodMode: '',
    },
    data() {
      return {
        showSmallVolMth : false,
      }
    },
    methods: {
      validateData() {
        return this.$refs.openInfo.validate();
      },
      forceUpdate(){
        this.$forceUpdate();
      },
      changeSmallVolMtc(value){
        if (value == '0'){
         // this.showSmallVolMth = false;
        }else{
          this.t8ProdLimit.smallVolMth = '';
         // this.showSmallVolMth = true;

        }
      }
    },
    created() {
      if (this.t8ProdLimit.smallVolMtc != '' || this.t8ProdLimit.smallVolMtc != null){
        this.changeSmallVolMtc(this.t8ProdLimit.smallVolMtc);
      }
    },
    watch: {
      t8ProdLimit: {
         handler: function(val) {
            this.$emit('changeProOpen', val);
          },
          deep: true
      },
      prodMode: {
        handler: function(val) {
          this.$emit('@changeProdMode', val);
        },
        deep: true
      },
    },
    mounted() {},

  }
</script>

<style>

</style>
