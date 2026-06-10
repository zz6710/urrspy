<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 15%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayPrintSubTempUpload ref="addProdForm" v-model="formData" :formData="formData"/>
    </div>

  </div>
</template>

<script>
  import DisplayPrintSubTempUpload         from "../../../M81/prodDisplay/prodDocument/DisplayPrintSubTempUpload.vue"
  export default {
    name: "PrintSubTempUploadFlow",
    components: {
      DisplayPrintSubTempUpload
    },
    props:{
      taskInfo: {},
    },
    data() {
      return {
        formData:{},
      }
    },
    created() {
      this.httpUtil
        .ajax({
          url: "/wf/formData/getSubmitParamsByProcessInstanceId.json",
          params: {processInstanceId: this.taskInfo.processInstanceId,_wfProcessInstanceId:this.taskInfo.processInstanceId}
        }).then(res => {
        if (res.data) {
          this.formData = JSON.parse(res.data.submitParams);
          this.$set(this.formData,'processInstanceId',this.taskInfo.processInstanceId);
          this.$set(this.formData,'tempName',JSON.parse(this.formData.fileNameList)[0]);
          this.$nextTick(() => {
            this.httpUtil.comnQuery({
              action: "PrintTemp.getTempTypeByDocType",
              params: {docType: this.formData.docType}
            }).then(data => {
              this.$set(this.formData,'addDocTypeDict',data.rows);
            }).catch({})
          })
        }
      });

    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../../styles/T81001.scss";
</style>
