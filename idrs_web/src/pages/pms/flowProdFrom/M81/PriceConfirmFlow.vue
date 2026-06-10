<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 15%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayPriceConfirm ref="editT8ProdQuotaForm" v-model="priceData" :priceData="priceData"/>
    </div>
  </div>
</template>

<script>
  import DisplayPriceConfirm         from "../../M81/prodDisplay/priceConfirm/DisplayPriceConfirm.vue"
  export default {
    name: "PriceConfirmFlow",
    components: {
      DisplayPriceConfirm
    },
    props:{
      taskInfo: {},
    },
    data() {
      return {
        priceData:{},
      }
    },
    created() {
      this.httpUtil
        .ajax({
          url: "/wf/formData/getSubmitParamsByProcessInstanceId.json",
          params: {processInstanceId: this.taskInfo.processInstanceId,_wfProcessInstanceId:this.taskInfo.processInstanceId}
        }).then(res => {
        if (res.data) {
          this.priceData = JSON.parse(res.data.submitParams);
        }
      });

    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
