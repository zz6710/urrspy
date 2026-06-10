<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 15%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayCreateProdInfo ref="prodInfo" v-model="prodCreateInfo" :prodCreateInfo="prodCreateInfo"/>
    </div>
  </div>
</template>

<script>
  import DisplayCreateProdInfo         from "../../M81/prodDisplay/DisplayCreateProdInfo.vue"
    export default {
        name: "ProdInfoCreateFlow",
      components: {
        DisplayCreateProdInfo
      },
      props:{
        taskInfo: {},
      },
      data() {
        return {
          prodCreateInfo:{},
        }
      },
      created() {
        this.httpUtil
          .ajax({
            url: "/wf/formData/getSubmitParamsByProcessInstanceId.json",
            params: {processInstanceId: this.taskInfo.processInstanceId,_wfProcessInstanceId:this.taskInfo.processInstanceId}
          }).then(res => {
          if (res.data) {
            this.prodCreateInfo = JSON.parse(res.data.submitParams);
          }
        });

      },
    }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
