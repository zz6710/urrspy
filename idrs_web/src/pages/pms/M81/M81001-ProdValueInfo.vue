<template>

  <div>
    <!--净值信息-->
    <k-form class="my-form " ref="navInfo" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-item label="默认分红方式">
        <k-field-radio v-model="t8ProdInfo2.defDivMethod" @data-on-change="changeDefDivMethod" data-dict="def_div_method"
                        :data-allowblank="false"/>
      </k-form-item>

      <k-form-item label="是否允许变更分红方式">
        <k-field-radio v-model="t8ProdInfo2.divChgFlag" data-dict="div_chg_flag" @data-on-change="changeIsAllowMethod"
                        :data-allowblank="false" data-default-value="0"/>
      </k-form-item>

      <k-form-item label="最小分红金额" v-show="showMinDivAmt">
        <k-field-text v-model="t8ProdInfo2.minDivAmt" data-validate-type="money" data-type="money"
                      data-default-value="0.01" data-min-value="0"
                      :data-max-length="18" data-digits="2" :data-allowblank="isAllowChangeMethod" data-placeholder="不限"
                      data-integer-length="15" @input="forceUpdate"/>
      </k-form-item>

      <k-form-item label="清盘方式">
        <k-field-radio v-model="t8ProdInfo2.windupType" data-dict="windup_type" :data-allowblank="false"
                        data-default-value="0"/>
      </k-form-item>
    </k-form>
  </div>
</template>

<script>
  export default {
    computed: {},
    model: {
      prop: 't8ProdInfo2',
      event: 'input'
    },
    props: {
      t8ProdInfo2: {}
    },
    data() {
      return {
        isAllowChangeMethod : false,
        showMinDivAmt: true,
      }
    },
    methods: {
      forceUpdate(){
        this.$forceUpdate();
      },
      validateData() {
        return this.$refs.navInfo.validate();
      },
      changeDefDivMethod(value){
        let divChgFlag = this.t8ProdInfo2.divChgFlag;

        if (value == '1' || (divChgFlag != '' && divChgFlag == '1' && value == '0')){
          this.showMinDivAmt = true;
          this.isAllowChangeMethod = false;
        } else{
          this.showMinDivAmt = false;
          this.isAllowChangeMethod = true;
          this.t8ProdInfo2.minDivAmt = '';
        }
      },
      // 改变分红方式
      changeIsAllowMethod(value) {
        //console.log("changeIsAllowMethod" + value)
        let defDivMethod = this.t8ProdInfo2.defDivMethod;
        if (value == '1' || (defDivMethod != '' && defDivMethod == '1' && value == '0')){
          this.showMinDivAmt = true;
          this.isAllowChangeMethod = false;
        }else{
          this.showMinDivAmt = false;
          this.t8ProdInfo2.minDivAmt = '';
          this.isAllowChangeMethod = true;
        }

      }
    },
    created() {

      if (this.t8ProdInfo2.defDivMethod != '' || this.t8ProdInfo2.defDivMethod != null){
        this.changeDefDivMethod(this.t8ProdInfo2.defDivMethod);
      }

      if (this.t8ProdInfo2.divChgFlag != '' || this.t8ProdInfo2.divChgFlag != null){
        this.changeIsAllowMethod(this.t8ProdInfo2.divChgFlag);
      }
    },
    mounted() {

    },
    watch: {},

  }
</script>

<style>

</style>
