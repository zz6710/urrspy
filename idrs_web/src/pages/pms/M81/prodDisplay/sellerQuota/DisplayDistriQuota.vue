<template>
  <div>
    <k-form ref="editDistriQuotaForm" :data-col="4" data-input-width="160px" data-label-width="100px"
            data-total-width="1100px">
      <k-form-item label="产品代码">
        <k-field-select v-model="formData.prodCode" data-multiple="false" data-action="T8ProdInfo.getProdInfos"
                        data-value-field="prodCode" data-display-field="prodCode" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="成立/开放日">
        <k-field-date v-model="formData.quotaDate" data-type="date" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="销售总额度(亿)">
        <k-field-text v-model="formData.totalSaleQuota" data-type="money" :data-disabled="true"/>
      </k-form-item>
    </k-form>
<!--    <hr style="margin-left: 5px%;" width="1000" color=#987cb9 SIZE=3/>-->
    <k-form ref="addForm3" v-for="(item, index) in formData.editEnvItems" :key="index"
            :data-col="5" data-input-width="130px" data-label-width="100px" data-total-width="1200px">
      <k-form-item label="销售商名称">
        <k-field-select ref="distributor" v-model="item.distributorCode" data-multiple="false"
                        data-value-field="distributorCode" data-display-field="distributorName"
                        data-action="T82001.findDistributorByCode" :data-disabled="true"
                        :data-params="{managerDept:formData.managerDept,prodCode:formData.prodCode}"/>
      </k-form-item>
      <k-form-item label="销售商类型">
        <k-field-select data-disabled="true" v-model="item.distributorType"
                        :data-disabled="true" data-dict="t8_distributor_type"/>
      </k-form-item>
      <k-form-item label="销售对象">
        <k-field-select v-model="item.prodSaleCustom" :data-allowblank="false"
                        :data-disabled="true" data-dict="prod_sale_custom"/>
      </k-form-item>
      <k-form-item label="申请额度(亿)">
        <k-field-text v-model="item.quota" :data-allowblank="false" :data-disabled="true"
                      data-validate-type="money" data-min-value="(0" data-max-value="999999999999.99"/>
      </k-form-item>

    </k-form>
  </div>
</template>

<script>
    export default {
        name: "DisplayDistriQuota",
      props:{
        formData:{},
      },
    }
</script>

<style scoped>

</style>
