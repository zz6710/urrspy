<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 15%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayIssueSeal ref="applicationForm" v-model="application" :application="application"/>
    </div>
  </div>
</template>

<script>
  import DisplayIssueSeal         from "../../M81/prodDisplay/documentSeal/DisplayIssueSeal"
  export default {
    name: "IssueSealFlow",
    components: {
      DisplayIssueSeal
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
