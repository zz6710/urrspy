<template>
  <k-form ref="addForm1" :data-col="2" style="margin-left:-5%;" data-input-width="200px" data-label-width="360px" data-total-width="1288px">
    <k-form-item label="产品名称">
      <k-field-text v-model="addDataFrom.prodName" :data-allowblank="false" :data-disabled="true" :data-max-length="128"></k-field-text>
    </k-form-item>
    <k-form-item label="产品代码">
      <k-field-text v-model="addDataFrom.prodCode" :data-allowblank="false" :data-disabled="true" :data-max-length="64"></k-field-text>
    </k-form-item>
    <k-form-item label="产品管理人名称">
      <k-field-text v-model="addDataFrom.managerCode" :data-disabled="true" :data-allowblank="false"></k-field-text>
    </k-form-item>
    <k-form-item label="新任投资经理姓名">
      <k-field-select v-model="addDataFrom.newInvestId" @data-on-change="getInvestInfo(addDataFrom)" data-action="T8Dict.findT8InvestManagerInfos"
                      data-display-field="custNo,custName" data-value-field="custNo" :data-allowblank="false" :data-disabled="true"></k-field-select>
    </k-form-item>
    <k-form-item label="共同管理本产品的其他投资经理姓名">
      <k-field-text v-model="addDataFrom.otherManagerId" :data-disabled="true"></k-field-text>
    </k-form-item>
    <k-form-item label="共同管理本产品的其他投资经理姓名" v-show="false">
      <k-field-text v-model="addDataFrom.otherManagerName" :data-disabled="true"></k-field-text>
    </k-form-item>
    <k-form-item label="管理本产品开始日">
      <k-field-date v-model="addDataFrom.postDate" :data-allowblank="false" :data-disabled="true"></k-field-date>
    </k-form-item>
    <k-form-item label="从业年限">
      <k-field-text v-model="addDataFrom.employmentTerm" :data-max-length="2" data-min-value="1" data-validate-type="number" data-type="number" data-digits="0" :data-allowblank="false" :data-disabled="true"></k-field-text>
    </k-form-item>
    <k-form-item label="投资管理从业年限">
      <k-field-text v-model="addDataFrom.investEmploymentTerm" :data-max-length="2" data-min-value="1" data-validate-type="number" data-type="number" data-digits="0" :data-allowblank="false" :data-disabled="true"></k-field-text>
    </k-form-item>
    <k-form-item label="过往从业经历">
      <k-field-text v-model="addDataFrom.pastEmploymentExp" :data-max-length="254" :data-allowblank="false" :data-disabled="true"></k-field-text>
    </k-form-item>
    <k-form-item label="是否曾被监管机构予以行政处罚或采取行政监管措施">
      <k-field-select v-model="addDataFrom.isPunish" :data-allowblank="false" :data-default-value="'0'" data-dict="t8_prod_isok" :data-disabled="true"></k-field-select>
    </k-form-item>
    <k-form-item label="国籍">
      <k-field-select v-model="addDataFrom.nationality" data-dict="t8_nation_code" :data-default-value="'CHN'" :data-allowblank="false" :data-disabled="true"></k-field-select>
    </k-form-item>
    <k-form-item label="学历、学位">
      <k-field-text v-model="addDataFrom.education" :data-max-length="254" :data-allowblank="false" :data-disabled="true"></k-field-text>
    </k-form-item>
    <k-form-item label="是否已按规定在银行业理财登记托管中心注册">
      <k-field-select v-model="addDataFrom.isRegister" data-dict="t8_prod_isok" :data-default-value="'1'" :data-allowblank="false" :data-disabled="true"></k-field-select>
    </k-form-item>
    <k-form-item label="登记编号">
      <k-field-text v-model="addDataFrom.register" :data-max-length="254" :data-allowblank="false"
                    :data-disabled="true"></k-field-text>
    </k-form-item>
  </k-form>
</template>

<script>
export default {
  name: "DisplayInvestManager",
  props:{
    addDataFrom:{},
  },
}
</script>

<style scoped>

</style>
