<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 15%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayTrustSeal ref="applicationForm" v-model="application" :application="application"/>
    </div>
  </div>
</template>

<script>
  import DisplayTrustSeal         from "../../M81/prodDisplay/documentSeal/DisplayTrustSeal"
  export default {
    name: "TrustSealFlow",
    components: {
      DisplayTrustSeal
    },
    props:{
      taskInfo: {},
    },
    data() {
      return {
        application:{},
      }
    },
    created() {
      this.httpUtil
        .ajax({
          url: "/wf/formData/getSubmitParamsByProcessInstanceId.json",
          params: {processInstanceId: this.taskInfo.processInstanceId,_wfProcessInstanceId:this.taskInfo.processInstanceId}
        }).then(res => {
        if (res.data) {
          this.application = JSON.parse(res.data.submitParams);
        }
      });

    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
