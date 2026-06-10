<template>
  <k-form  ref="addProdForm"  :data-col="2">
    <k-form-item label="账户类型">
      <k-field-select v-model="formData.accountType" :data-allowblank="false"
                      data-display-field="itemval" data-value-field="itemkey" :data-disabled="true"
                      data-dict="t8_account_type"/>
    </k-form-item>
    <k-form-item label="托管行名称" :data-col="2"
                 v-show="this.formData.accountType == '1' || this.formData.accountType == '2'">
      <k-field-select v-model="formData.t8TruteeInfoId" data-action="T82006.findTaCustodianBanks"
                      data-display-field="truteeName" data-value-field="id" :data-disabled="true"
                      :data-allowblank="!(this.formData.accountType == '1' || this.formData.accountType == '2')"/>
    </k-form-item>
    <k-form-item label="开户行名称" :data-col="2"
                 v-if="this.formData.accountType == '1' || this.formData.accountType == '2'|| this.formData.accountType == '3'|| this.formData.accountType == '4'|| this.formData.accountType == '8'|| this.formData.accountType == '9'">
      <k-field-text v-model="formData.openAccountName" :data-max-length="128" :data-allowblank="false"
                    :data-disabled="true"/>
    </k-form-item>

    <k-form-item label="交易账户名称" :data-col="2" v-if=" this.formData.accountType == '5'">
      <k-field-text v-model="formData.accountName" :data-max-length="128" :data-allowblank="false"
                    :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="证券账号名称" :data-col="2" v-if=" this.formData.accountType == '7'">
      <k-field-text v-model="formData.accountName" :data-max-length="128" :data-allowblank="false"
                    :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="资金账户名称" :data-col="2"
                 v-if="this.formData.accountType == '1'|| this.formData.accountType == '2'|| this.formData.accountType == '3'|| this.formData.accountType == '4'|| this.formData.accountType == '6'|| this.formData.accountType == '8'|| this.formData.accountType == '9'">
      <k-field-text v-model="formData.accountName" :data-max-length="128" :data-allowblank="false"
                    :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="券商名称" :data-col="2" v-if="this.formData.accountType == '6' ">
      <k-field-text v-model="formData.brokerName" :data-max-length="128"
                    :data-allowblank="!(this.formData.accountType == '6')" :data-disabled="true"/>
    </k-form-item>
         <k-form-item label="基金账号"  v-if="this.formData.accountType == '5'">
          <k-field-text v-model="formData.accountCode"   data-validate-type="int" data-type="int" :data-max-lenght="32" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
         <k-form-item label="证券账号号码"  v-if="this.formData.accountType == '7'">
          <k-field-text v-model="formData.accountCode"   data-validate-type="int" data-type="int" :data-max-lenght="32" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="资金账号"  v-if="this.formData.accountType == '1'|| this.formData.accountType == '2'|| this.formData.accountType == '3'|| this.formData.accountType == '4'|| this.formData.accountType == '6'|| this.formData.accountType == '8'|| this.formData.accountType == '9'">
          <k-field-text v-model="formData.accountCode"   data-validate-type="int" data-type="int" :data-max-lenght="32" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="大额行号" v-if="this.formData.accountType == '1' || this.formData.accountType == '2'|| this.formData.accountType == '3'|| this.formData.accountType == '4'|| this.formData.accountType == '8'|| this.formData.accountType == '9'">
          <k-field-text v-model="formData.bankAccNum" :data-max-length="32" data-validate-type="int" data-type="int" :data-disabled="true" />
        </k-form-item>
    <k-form-item label="产品代码" v-show="this.formData.accountType == '1' " :data-col="2">
      <k-field-select v-model="formData.prodCodes" data-action="T8Dict.findNotEstablishProdInfos" :data-disabled="true"
                      data-display-field="prodCode,prodName" data-value-field="prodCode" :data-multiple="true"
                      :dataAllowblank="!(this.formData.accountType == '1')"/>
    </k-form-item>
    <k-form-item label="产品代码" v-show="this.formData.accountType == '2'" :data-col="2">
      <k-field-select v-model="formData.prodCodes" data-action="T8Dict.findNotEstablishProdInfos"
                      data-display-field="prodCode,prodName" data-value-field="prodCode" :data-disabled="true"
                      :dataAllowblank="!(this.formData.accountType == '2')"/>
    </k-form-item>
    <k-form-item label="产品代码" v-show="this.formData.accountType != '1' && this.formData.accountType != '2'"
                 :data-col="2">
      <k-field-select v-model="formData.prodCodes" data-action="T8Dict.findNotEstablishProdInfos" :data-disabled="true"
                      data-display-field="prodCode,prodName" data-value-field="prodCode" :data-multiple="true"
                      :dataAllowblank="!(this.formData.accountType == '1' || this.formData.accountType == '2')"/>
    </k-form-item>
    <k-form-item label="币种" v-show="this.formData.accountType == '2'">
      <k-field-select v-model="formData.cur" data-dict="t8_prod_currtype" data-default-value="156" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="托管场所" v-show="this.formData.accountType == '3' || this.formData.accountType == '4'"
                 :data-col="2">
      <k-field-select v-model="formData.trusteeMarket" data-dict="t8_trustee_market_dvp" :data-disabled="true"
                      :data-allowblank="!(this.formData.accountType == '3' || this.formData.accountType == '4')"/>
    </k-form-item>
    <k-form-item label="托管场所" v-show="this.formData.accountType == '7'" :data-col="2">
      <k-field-select v-model="formData.trusteeMarket" data-dict="t8_trustee_market_exchange" :data-disabled="true"
                      :data-allowblank="!(this.formData.accountType == '7')"/>
    </k-form-item>
    <k-form-item label="一码通账号" v-show="this.formData.accountType == '7'">
      <k-field-text v-model="formData.oneYardPassAccountCode" data-min-value="0" data-validate-type="number" :data-disabled="true"
                       data-type="number" data-digits="0" :data-allowblank="!(this.formData.accountType == '7')"/>
    </k-form-item>
    <k-form-item label="基金公司名称" :data-col="2" v-show="this.formData.accountType == '5'">
      <k-field-text v-model="formData.fundOside" :data-max-length="128" :data-disabled="true"
                    :data-allowblank="!(this.formData.accountType == '5')"/>
    </k-form-item>
    <k-form-item label="交易账号" v-show="this.formData.accountType == '5'">
      <k-field-text v-model="formData.tranAccount" data-validate-type="number" :data-disabled="true"
                       data-type="number" :data-max-lenght="32" data-digits="0" :data-allowblank="!(this.formData.accountType == '5')"/>
    </k-form-item>
    <k-form-item label="深交所席位号" v-show="this.formData.accountType == '6'">
      <k-field-text v-model="formData.szSeatsCode" data-validate-type="number" data-type="number"  :data-disabled="true" data-digits="0"/>
    </k-form-item>
    <k-form-item label="上交所席位号" v-show="this.formData.accountType == '6'" >
      <k-field-text v-model="formData.shSeatsCode" data-validate-type="number" data-type="number" :data-disabled="true" data-digits="0"/>
    </k-form-item>
    <k-form-item label="备注" :data-col="2">
      <k-field-text v-model="formData.remark" :data-max-length="2000" inputType="textarea" :data-disabled="true" :rows="1"/>
    </k-form-item>
  </k-form>
</template>

<script>
    export default {
        name: "DisplayAccountInfo",
      props:{
        formData:{},
      },
    }
</script>

<style scoped>

</style>
