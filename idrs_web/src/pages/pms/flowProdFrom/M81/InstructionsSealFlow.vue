<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 15%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayInstructionsSeal ref="applicationForm" v-model="formData" :formData="formData"/>
    </div>
  </div>
</template>

<script>
  import DisplayInstructionsSeal         from "../../M81/prodDisplay/documentSeal/DisplayInstructionsSeal"
  import Tools from "@/utils/tools";
  export default {
    name: "InstructionsSealFlow",
    components: {
      DisplayInstructionsSeal
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
          console.log(this.formData)
          if(this.formData!= null && this.formData.createDate != null && this.formData.createDate != '' &&
             this.formData.createTime != null && this.formData.createTime != ''){
            this.$set(this.formData,'createDate',Tools.formatDateTime(this.formData.createDate,this.formData.createDate))
          }
          }
        });
    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
