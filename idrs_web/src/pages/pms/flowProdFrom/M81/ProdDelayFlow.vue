<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 15%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayProdDelay ref="addDelyForm" v-model="delyFormData" :delyFormData="delyFormData"/>
    </div>
  </div>
</template>

<script>
  import DisplayProdDelay         from "../../M81/prodDisplay/prodExpiration/DisplayProdDelay.vue"
  export default {
    name: "ProdDelayFlow",
    components: {
      DisplayProdDelay
    },
    props:{
      taskInfo: {},
    },
    data() {
      return {
        delyFormData:{},
      }
    },
    created() {
      this.httpUtil
        .ajax({
          url: "/wf/formData/getSubmitParamsByProcessInstanceId.json",
          params: {processInstanceId: this.taskInfo.processInstanceId,_wfProcessInstanceId:this.taskInfo.processInstanceId}
        }).then(res => {
        if (res.data) {
          this.delyFormData = JSON.parse(res.data.submitParams);
        }
      });

    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
