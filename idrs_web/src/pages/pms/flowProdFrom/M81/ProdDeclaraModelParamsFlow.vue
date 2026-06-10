
<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 15%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <T8ProdDeclaraModelParams ref="prodInfo" v-model="formData" :formData="formData"/>
    </div>
  </div>
</template>

<script>
  import T8ProdDeclaraModelParams         from "../../M81/prodDisplay/DisplayDeclaraModelParams.vue"
    export default {
        name: "ProdDeclaraModelParamsFlow",
      components: {
        T8ProdDeclaraModelParams
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
