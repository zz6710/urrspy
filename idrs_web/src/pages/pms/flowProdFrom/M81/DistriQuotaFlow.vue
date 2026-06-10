<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 5%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayDistriQuota ref="editT8ProdQuotaForm" v-model="formData" :formData="formData"/>
    </div>
  </div>
</template>

<script>
  import DisplayDistriQuota from "../../M81/prodDisplay/sellerQuota/DisplayDistriQuota.vue"
  export default {
    name: "DistriQuotaFlow",
    components: {
      DisplayDistriQuota
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
          let json = JSON.parse(this.formData.json);
          let editEnvItemsFlow = json.envItemsConf;
          this.$set(this.formData,'editEnvItems',editEnvItemsFlow);
        }
      });

    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
