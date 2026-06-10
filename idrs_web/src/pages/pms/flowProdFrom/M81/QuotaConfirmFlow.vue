<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 15%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayQuotaConfirm ref="editT8ProdQuotaForm" v-model="formData" :formData="formData"/>
    </div>
  </div>
</template>

<script>
  import DisplayQuotaConfirm         from "../../M81/prodDisplay/sellerQuota/DisplayQuotaConfirm.vue"
  export default {
    name: "QuotaConfirmFlow",
    components: {
      DisplayQuotaConfirm
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
        }
      });

    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
