<template>

  <div class="formPanel" ref="formPanel" >
<!--    <div class="form-item prod-panel" id="prodAdjust" >-->
<!--      <div class="title">-->
<!--      </div>-->
<!--    </div>-->

    <div class="form-item prod-panel" id="prodWorkDay" >
      <div class="title">
      </div>
      <div style="margin-top: 40px">
        <DisplayProdInfoServiceFlow ref="addProdForm" v-model="formData" :formData="formData"/>
      </div>
    </div>
  </div>
</template>

<script>
import DisplayProdInfoServiceFlow from "../../M81/prodDisplay/DisplayProdInfoServiceFlow.vue"
export default {
    name: "ProdInfoServiceFlow",
    components: {
      DisplayProdInfoServiceFlow
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
