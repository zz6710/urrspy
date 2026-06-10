<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 15%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayMeetCreate ref="addMeetCreateForm" v-model="addDataFrom" :addDataFrom="addDataFrom"/>
    </div>
  </div>
</template>

<script>
  import DisplayMeetCreate         from "../../M81/prodDisplay/meetCreate/DisplayMeetCreate.vue"
  export default {
    name: "MeetCreateFlow",
    components: {
      DisplayMeetCreate
    },
    props:{
      taskInfo: {},
    },
    data() {
      return {
        addDataFrom:{},
      }
    },
    created() {
      this.httpUtil
        .ajax({
          url: "/wf/formData/getSubmitParamsByProcessInstanceId.json",
          params: {processInstanceId: this.taskInfo.processInstanceId,_wfProcessInstanceId:this.taskInfo.processInstanceId}
        }).then(res => {
        if (res.data) {
          this.addDataFrom = JSON.parse(res.data.submitParams);
        }
      });

    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
