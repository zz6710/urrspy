<template>
  <k-form dataInputWidth="700px" ref="addT8ProdConsumerInsuranceForm" :data-col="1">
    <div style="padding-top: 2px;">
      <div class="form-item prod-panel"  >
        <div class="title" >
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="产品基本信息"/>
        </div>
        <k-form ref="addForm" data-input-width="240px">

        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodName" :data-disabled="true" :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="产品代码  ">
          <k-field-text v-model="formData.prodCode" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
          <k-form-item label="投资经理" >
            <k-field-text v-model="formData.productInvManager" :data-allowblank="false" :data-disabled="true">
            </k-field-text>
          </k-form-item>
          <k-form-item label="产品经理" >
            <k-field-text v-model="formData.prodManageName"  :dataAllowblank="false" :data-disabled="true"/>
          </k-form-item>
        <k-form-item label="产品形态">
          <k-field-select v-model="formData.prodMode" :dataAllowblank="false" :data-disabled="true" data-dict="t8_prod_create_type"/>
        </k-form-item>
        <k-form-item label="产品状态">
          <k-field-select v-model="formData.prodStatus" :data-disabled="true" :data-allowblank="false" data-dict="t8_prod_status"/>
        </k-form-item>
          <k-form-item label="产品子状态">
            <k-field-select v-model="formData.prodSonStatus" :data-disabled="true" :data-allowblank="false" data-dict="t8_prod_son_status"/>
          </k-form-item>
          <k-form-item label="份额分类" >
            <k-field-select v-model="formData.isShareSort"  :dataAllowblank="false" :data-disabled="true" data-dict="1yes0no"/>
          </k-form-item>

        </k-form>
      </div>
    </div>
  </k-form>
</template>

<script>
    export default {
        name: "DisplayPrintTempVersion",
      props:{
        formData:{},
      },
      computed: {
        querydeptGraphql() {
          return "{queryDept(action:\"find\") {rows{deptno, deptname, parentdeptno, deptid},results}}"
        }
      },
      data() {
        return {
        }
      }
    }
</script>

<style scoped>

</style>
