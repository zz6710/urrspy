<template>
  <k-form ref="editTaCustodianBankForm" :data-col="2" dataLabelWidth="190px">
    <k-form-item label="产品代码">
      <k-field-text v-model="formData.prodCode"  :data-disabled="true" />
    </k-form-item>
    <k-form-item label="产品id" v-show="false">
      <k-field-text v-model="formData.prodId"  :data-disabled="true" />
    </k-form-item>
    <k-form-item label="产品名称">
      <k-field-text v-model="formData.prodName"  :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="产品到期日">
      <k-field-date v-model="formData.endDate"  data-type="date" :data-disabled="true" />
    </k-form-item>
    <k-form-item label="产品清盘日">
      <k-field-date v-model="formData.liquidate" data-type="date"  :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="登记编码">
      <k-field-text v-model="formData.registrationCode"  :data-disabled="true"/>
    </k-form-item>

    <k-form-item label="发行机构代码">
      <k-field-text v-model="formData.organizationCode"  :data-disabled="true" />
    </k-form-item>
    <k-form-item label="产品总资产">
      <k-field-text v-model="formData.totalAssets" data-digits="2"  data-integer-length="14" :data-disabled="true"
                    data-min-value="0" data-validisplay-type="money" data-type="money" data-placeholder="单位(元)"/>
    </k-form-item>
    <k-form-item label="实际终止日期">
      <k-field-date v-model="formData.prodRealCloseDate" data-type="date"  :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="银行实际实现收入(元)">
      <k-field-text v-model="formData.bankActualIncome"  data-digits="2"  data-integer-length="14" :data-disabled="true"
                    data-min-value="0" data-validisplay-type="money" data-type="money" data-placeholder="单位(元)"/>
    </k-form-item>
    <k-form-item label="兑付客户收益(元)">
      <k-field-text v-model="formData.cashCustomerIncome" data-digits="2"  data-integer-length="14" :data-disabled="true"
                    data-min-value="0" data-validisplay-type="money" data-type="money" data-placeholder="单位(元)"/>
    </k-form-item>
    <k-form-item label="兑付客户总金额(元)">
      <k-field-text v-model="formData.cashCustomerTotalAmount" data-digits="2"  data-integer-length="14" :data-disabled="true"
                    data-min-value="0" data-validisplay-type="money" data-type="money" data-placeholder="单位(元)"/>
    </k-form-item>
    <k-form-item label="兑付总份额(元)">
      <k-field-text v-model="formData.cashTotalAmount" data-digits="5"  data-integer-length="13" :data-disabled="true"
                    data-min-value="0" data-validisplay-type="money" data-type="money" data-placeholder="单位(元)"/></k-form-item>
    <k-form-item label="本机构托管费(元)">
      <k-field-text v-model="formData.trusteeshipFee" data-digits="2" data-integer-length="13" :data-disabled="true"
                    data-min-value="0" data-validisplay-type="money" data-type="money" data-placeholder="单位(元)"/>
    </k-form-item>
    <k-form-item label="本机构管理费(元)">
      <k-field-text v-model="formData.managementFee"  data-digits="2" data-integer-length="13" :data-disabled="true"
                    data-min-value="0" data-validisplay-type="money" data-type="money" data-placeholder="单位(元)"/>
    </k-form-item>
    <k-form-item label="本机构销售手续费(元)">
      <k-field-text v-model="formData.salesServiceCharge" data-digits="2" data-integer-length="13" :data-disabled="true"
                    data-min-value="0" data-validisplay-type="money" data-type="money" data-placeholder="单位(元)"/>
    </k-form-item>
    <k-form-item label="本机构其他产品费用(元)">
      <k-field-text v-model="formData.organizeOhterFee" data-digits="2" data-integer-length="13" :data-disabled="true"
                    data-min-value="0" data-validisplay-type="money" data-type="money" data-placeholder="单位(元)"/>
    </k-form-item>
    <k-form-item label="其他机构托管费(元)">
      <k-field-text v-model="formData.otherInstitutionsTrusteeshipFee" data-digits="2" data-integer-length="13" :data-disabled="true"
                    data-min-value="0" data-validisplay-type="money" data-type="money" data-placeholder="单位(元)"/>
    </k-form-item>
    <k-form-item label="其他机构管理费(元)">
      <k-field-text v-model="formData.otherInstitutionsManagementFee" data-digits="2" data-integer-length="13" :data-disabled="true"
                    data-min-value="0" data-validisplay-type="money" data-type="money" data-placeholder="单位(元)"/>
    </k-form-item>
    <k-form-item label="其他机构销售手续费(元)">
      <k-field-text v-model="formData.otherInstitutionsSalesServiceCharge" data-digits="2" data-integer-length="13" :data-disabled="true"
                    data-min-value="0" data-validisplay-type="money" data-type="money" data-placeholder="单位(元)"/>
    </k-form-item>
    <k-form-item label="其他机构其他产品费(元)">
      <k-field-text v-model="formData.otherInstitutionOtherFee" data-digits="2" data-integer-length="13" :data-disabled="true"v
                    data-min-value="0" data-validisplay-type="money" data-type="money" data-placeholder="单位(元)"/>
    </k-form-item>
    <k-form-item label="投资顾问费用(元)">
      <k-field-text v-model="formData.investmentConsultantFee"  data-digits="2" data-integer-length="13" :data-disabled="true"
                    data-min-value="0" data-validisplay-type="money" data-type="money" data-placeholder="单位(元)"/>
    </k-form-item>
    <k-form-item label="客户实际年化收益率(%)">
      <k-field-text v-model="formData.actualAnnualizedRate" data-type="number" :data-disabled="true"
                    data-digits="5" data-integer-length="3" data-validisplay-type="number"/>
    </k-form-item>
    <k-form-item label="产品实际年化收益率(%)">
      <k-field-text v-model="formData.actualAnnualizedYield" data-type="number" data-validisplay-type="number" :data-disabled="true"
                    data-digits="5" data-integer-length="3"/>
    </k-form-item>
  </k-form>
</template>

<script>
    export default {
        name: "DisplayProdExpire",
      props:{
        formData:{},
      },
    }
</script>

<style scoped>

</style>
