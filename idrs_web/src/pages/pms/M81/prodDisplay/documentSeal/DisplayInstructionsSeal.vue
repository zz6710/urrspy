<template>
  <k-form dataInputWidth="700px" ref="addT8ProdForm" :data-col="1">
    <div style="padding-top: 2px;">
      <div class="form-item prod-panel"  >
        <div class="title" >
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="文档模板信息"/>
        </div>
        <k-form ref="addForm" data-input-width="240px">
          <k-form-item label="产品代码">
            <k-field-text v-model="formData.prodCode" :data-disabled="true" :dataAllowblank="false"/>
          </k-form-item>
          <k-form-item label="产品名称">
            <k-field-text v-model="formData.prodName" :data-disabled="true" :dataAllowblank="false"/>
          </k-form-item>
          <k-form-item label="文件名称">
            <k-field-text v-model="formData.fileName" :data-disabled="true" :dataAllowblank="false"/>
          </k-form-item>
          <k-form-item label="文档版本">
            <k-field-text v-model="formData.version" :data-disabled="true" :dataAllowblank="false"/>
          </k-form-item>
          <k-form-item label="文档类型">
            <k-field-text v-model="formData.documentName" :data-disabled="true" :dataAllowblank="false"/>
          </k-form-item>
          <k-form-item label="是否模板文档">
            <k-field-select v-model="formData.isTemplateFile" data-dict="1yes0no" :data-disabled="true" :dataAllowblank="false"/>
          </k-form-item>
          <k-form-item label="说明书状态">
            <k-field-select v-model="formData.confirmStatus" data-dict="doc_confirm_status" :data-disabled="true" :dataAllowblank="false"/>
          </k-form-item>
          <k-form-item label="创建人">
            <k-field-text v-model="formData.createUserName" :dataAllowblank="false" :data-disabled="true" />
          </k-form-item>
          <k-form-item label="创建时间">
            <k-field-text v-model="formData.createDate"  :dataAllowblank="false" :data-disabled="true" />
          </k-form-item>

<!--          <k-form-footer data-align="center">-->
<!--            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="" data-from="prodCreateInfoFrom"-->
<!--                   :data-model="prodCreateInfo" data-target="prodInfoGrid">-->
<!--              <md-icon>zoom_in</md-icon>预览-->
<!--            </k-btn>-->
<!--          </k-form-footer>-->
        </k-form>


      </div>
    </div>
  </k-form>
</template>

<script>
import Tools from "@/utils/tools";
    export default {
        name: "DisplayInstructionsSeal",
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
