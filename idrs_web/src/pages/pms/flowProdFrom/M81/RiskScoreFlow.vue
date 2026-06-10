<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 5%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayRiskScore ref="applicationForm" v-model="prodRiskRat" :riskRemark="riskRemark" :prodRiskRat="prodRiskRat"/>
    </div>
  </div>
</template>

<script>
  import DisplayRiskScore         from "../../M81/prodDisplay/prodRiskScore/DisplayRiskScore"
  export default {
    name: "RiskScoreFlow",
    components: {
      DisplayRiskScore
    },
    props:{
      taskInfo: {},
    },
    data() {
      return {
        prodRiskRat:{},
        riskRemark: {}
      }
    },
    created() {
      this.httpUtil
        .ajax({
          url: "/wf/formData/getSubmitParamsByProcessInstanceId.json",
          params: {processInstanceId: this.taskInfo.processInstanceId,_wfProcessInstanceId:this.taskInfo.processInstanceId}
        }).then(res => {
        if (res.data) {
          let submitParams  = JSON.parse(res.data.submitParams);
          console.log('查询风险频分信息', submitParams)
          this.prodRiskRat=submitParams;
          this.riskRemark = submitParams.riskRemark;
          this.$set(this.prodRiskRat,'RatGrid',JSON.parse(submitParams.datas));
        }
      });

    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
