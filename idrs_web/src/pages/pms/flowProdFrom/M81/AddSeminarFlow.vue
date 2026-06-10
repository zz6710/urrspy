<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 15%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayAddSeminar ref="applicationForm" v-model="formData" :formData="formData"/>
    </div>
  </div>
</template>

<script>
  import DisplayAddSeminar         from "../../M81/prodDisplay/originality/DisplayAddSeminar"
  export default {
    name: "AddSeminarFlow",
    components: {
      DisplayAddSeminar
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
          let json = JSON.parse(this.formData.json);
          let envItemsConf = json.envItemsConf;
          this.$set(this.formData,'envItems',envItemsConf);
        }
      });

    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
