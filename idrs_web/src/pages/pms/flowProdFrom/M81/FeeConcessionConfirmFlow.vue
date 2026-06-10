<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 10%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayFeeConcessionConfirm ref="addDelyForm" v-model="formData" :formData="formData"/>
    </div>
  </div>
</template>

<script>
  import DisplayFeeConcessionConfirm         from "../../M81/prodDisplay/feeConcessionConfirm/DisplayFeeConcessionConfirm.vue"
  export default {
    name: "FeeConcessionConfirmFlow",
    components: {
      DisplayFeeConcessionConfirm
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
          if(this.formData.feeJson){
            this.$set(this.formData,'feeData',JSON.parse(this.formData.feeJson));
          }
        }
      });

    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
