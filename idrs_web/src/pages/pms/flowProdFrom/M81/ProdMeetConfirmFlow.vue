<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 15%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayProdMeetConfirm ref="baseInfoForm" v-model="formDataCon" :formDataCon="formDataCon"/>
    </div>
  </div>
</template>

<script>
  import DisplayProdMeetConfirm         from "../../M81/prodDisplay/meetCreate/DisplayProdMeetConfirm.vue"
  export default {
    name: "ProdMeetConfirmFlow",
    components: {
      DisplayProdMeetConfirm
    },
    props:{
      taskInfo: {},
    },
    data() {
      return {
        formDataCon:{},
      }
    },
    created() {
      this.httpUtil
        .ajax({
          url: "/wf/formData/getSubmitParamsByProcessInstanceId.json",
          params: {processInstanceId: this.taskInfo.processInstanceId,_wfProcessInstanceId:this.taskInfo.processInstanceId}
        }).then(res => {
        if (res.data) {
          this.formDataCon = JSON.parse(res.data.submitParams);

        }
      });

    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
