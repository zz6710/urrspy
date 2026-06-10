<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 15%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayProdLiquidation ref="baseInfoForm" v-model="formData" :formData="formData"/>
    </div>
  </div>
</template>

<script>
  import DisplayProdLiquidation         from "../../M81/prodDisplay/prodExpiration/DisplayProdLiquidation.vue"
  export default {
    name: "ProdLiquidationFlow",
    components: {
      DisplayProdLiquidation
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
