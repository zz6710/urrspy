<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 0%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayInvestManager ref="addMeetCreateForm" v-model="addDataFrom" :addDataFrom="addDataFrom"/>
    </div>
  </div>
</template>

<script>
import DisplayInvestManager        from "../../M81/prodDisplay/disclosureFlow/DisplayInvestManager.vue"
export default {
  name: "InvestManagerFlow",
  components: {
    DisplayInvestManager
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
