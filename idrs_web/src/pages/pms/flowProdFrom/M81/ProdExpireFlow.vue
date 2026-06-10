<template>
  <div class="formPanel" ref="formPanel" >
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayProdExpire ref="editTaCustodianBankForm" v-model="formData" :formData="formData"/>
    </div>
  </div>
</template>

<script>
  import DisplayProdExpire         from "../../M81/prodDisplay/prodExpiration/DisplayProdExpire.vue"
  export default {
    name: "ProdExpireFlow",
    components: {
      DisplayProdExpire
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
