<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 15%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayProdMeetCreate ref="applicationForm" v-model="value" :value="value"/>
    </div>
  </div>
</template>

<script>
  import DisplayProdMeetCreate         from "../../M81/prodDisplay/meetCreate/DisplayProdMeetCreate"
  export default {
    name: "ProdMeetCreateFlow",
    components: {
      DisplayProdMeetCreate
    },
    props:{
      taskInfo: {},
    },
    data() {
      return {
        value:{},
      }
    },
    created() {
      this.httpUtil
        .ajax({
          url: "/wf/formData/getSubmitParamsByProcessInstanceId.json",
          params: {processInstanceId: this.taskInfo.processInstanceId,_wfProcessInstanceId:this.taskInfo.processInstanceId}
        }).then(res => {
        if (res.data) {
          this.value = JSON.parse(res.data.submitParams);
        }
      });

    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
