<template>
  <k-form dataInputWidth="700px" ref="addT8ProdConsumerInsuranceForm" :data-col="1">
    <div style="padding-top: 2px;">
      <div class="form-item prod-panel"  >
        <div class="title" >
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="文档模板信息"/>
        </div>
        <k-form ref="addForm" data-input-width="240px">
        <k-form-item label="文档版本编号">
          <k-field-text v-model="formData.version" :data-disabled="true" :dataAllowblank="false"/>
        </k-form-item>
        <k-form-item label="创建人">
          <k-field-text v-model="formData.crtUserName" :dataAllowblank="false" :data-disabled="true" />
        </k-form-item>
          <k-form-item label="上传日期">
            <k-field-text v-model="formData.crtDate" :data-disabled="true" :dataAllowblank="false"/>
          </k-form-item>
          <k-form-item label="文档名称" data-input-width="620px">
            <k-field-text v-model="formData.docName"  :dataAllowblank="false" :data-disabled="true" inputType="textarea" :rows="1"/>
          </k-form-item>
          <k-form-item label="备注" data-input-width="620px">
            <k-field-text v-model="formData.remark" :data-allowblank="false" :data-disabled="true" inputType="textarea" :rows="1">
            </k-field-text>
          </k-form-item>

          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="" data-from="prodCreateInfoFrom"
                   :data-model="prodCreateInfo" data-target="prodInfoGrid">
              <md-icon>zoom_in</md-icon>预览
            </k-btn>
          </k-form-footer>
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
