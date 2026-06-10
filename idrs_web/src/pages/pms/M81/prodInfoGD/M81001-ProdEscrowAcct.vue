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
        <k-field-select  v-model="T8ProdEscrowAcct.t8TruteeInfoId" data-action="T82006.findTaCustodianBanks"
                         data-display-field="truteeCode,truteeName"  data-value-field="id"
                         :data-default-value="T8ProdEscrowAcct.t8TruteeInfoId?T8ProdEscrowAcct.t8TruteeInfoId:'7'"/>
      </k-form-item>
<!--      <k-form-item label="巨额赎回比例(%)">
        <k-field-text v-model="T8ProdEscrowAcct.hugeRedem" :data-allowblank="false" :data-max-length="8"
                      data-min-value="0"  data-integer-length="3" data-validate-type="money" data-max-value="100"
                      data-type="money" data-digits="2" />
      </k-form-item>-->

      <k-form-item label="境外托管银行">
        <k-field-select  v-model="T8ProdEscrowAcct.t8OutTruteeInfoId"   data-action="T82006.findTaCustodianBanks2"
                         data-display-field="truteeCode,truteeName"  data-value-field="id"/>
      </k-form-item>

      <k-form-item label="境内托管协议编号">
        <k-field-text v-model="T8ProdEscrowAcct.gdTrutee"  :data-max-length="200" :data-disabled="true" />
      </k-form-item>

      <k-form-item label="境外托管协议编号">
        <k-field-text v-model="T8ProdEscrowAcct.gdOutTrutee" :data-max-length="200" :data-disabled="true" />
      </k-form-item>

      <k-form-item label="境内托管协议生成日期">
        <k-field-date v-model="T8ProdEscrowAcct.truteeCustodyData" :data-max-length="128"  :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="境外托管协议生成日期">
        <k-field-date v-model="T8ProdEscrowAcct.outTruteeCustodyData"  :data-max-length="128"  :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="境内托管落地支行" v-show="false">
        <k-field-text v-model="T8ProdEscrowAcct.truteeBranch" :data-max-length="200"/>
      </k-form-item>

      <k-form-item label="境外托管落地支行" v-show="false">
        <k-field-text v-model="T8ProdEscrowAcct.outTruteeBranch"  :data-max-length="200"/>
      </k-form-item>

      <k-form-item label="境内托管开户行">
        <k-field-text v-model="T8ProdEscrowAcct.truteeDeposit" :data-max-length="128"/>
      </k-form-item>

      <k-form-item label="境外托管开户行">
        <k-field-text v-model="T8ProdEscrowAcct.outTruteeDeposit"  :data-max-length="128"/>
      </k-form-item>

      <k-form-footer data-align="center"  v-show="menuName == 'M81007'">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdTrutee.addT8ProdTrutee" data-from="truteeInfo"
               :data-model="T8ProdEscrowAcct"  :data-handler="addHandler"  :data-after-success="passDataSuccess">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>保存
        </k-btn>
      </k-form-footer>
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
      passDataSuccess(){
        this.$emit('isShowButton', '1')
      },
      addHandler(val){
        this.$set(val,'assemblyMenuType','truteeInfo');
      },
      validateData() {
        return this.$refs.truteeInfo.validate();
      },
    },
    created() {

    },
    mounted() {},
    watch: {
      'truteeCode'(val){
        this.$set(this.T8ProdEscrowAcct,"bankNo",val);
      }
    }

  }
</script>

<style>

</style>
