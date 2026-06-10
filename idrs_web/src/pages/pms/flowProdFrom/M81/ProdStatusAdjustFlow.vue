<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 15%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayProdStatusAdjust ref="changeStatusForm" v-model="ProdStatusChange" :ProdStatusChange="ProdStatusChange"/>
    </div>
  </div>
</template>

<script>
  import DisplayProdStatusAdjust         from "../../M81/prodDisplay/DisplayProdStatusAdjust.vue"
  export default {
    name: "ProdStatusAdjustFlow",
    components: {
      DisplayProdStatusAdjust
    },
    props:{
      taskInfo: {},
    },
    data() {
      return {
        ProdStatusChange:{},
      }
    },
    created() {
      this.httpUtil
        .ajax({
          url: "/wf/formData/getSubmitParamsByProcessInstanceId.json",
          params: {processInstanceId: this.taskInfo.processInstanceId,_wfProcessInstanceId:this.taskInfo.processInstanceId}
        }).then(res => {
        if (res.data) {
          this.ProdStatusChange = JSON.parse(res.data.submitParams);
        }
      });

    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
