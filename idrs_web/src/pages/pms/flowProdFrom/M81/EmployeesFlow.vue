<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 15%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayEmployees ref="addCustomerInfoForm" v-model="value" :value="value"/>
    </div>
  </div>
</template>

<script>
  import DisplayEmployees         from "../../M81/prodDisplay/prodEmployees/DisplayEmployees.vue"
  export default {
    name: "EmployeesFlow",
    components: {
      DisplayEmployees
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
