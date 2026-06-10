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
        <DisplayProdConsumerInsurance ref="addProdForm" v-model="formData" :formData="formData"/>
      </div>
    </div>
  </div>
</template>

<script>
  import DisplayProdConsumerInsurance         from "../../M81/prodDisplay/prodConsumerInsurance/DisplayProdConsumerInsurance.vue"
  export default {
    name: "ProdConsumerInsuranceFlow",
    components: {
      DisplayProdConsumerInsurance
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
