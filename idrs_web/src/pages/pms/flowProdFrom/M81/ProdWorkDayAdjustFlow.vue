<template>
  <div class="formPanel" ref="formPanel" >
    <div class="form-item prod-panel" id="prodAdjust" >
      <div class="title">
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="调整信息"></k-field-display>
      </div>
      <DisplayProdWorkdayAdjust ref="prodWorkdayAdjust" v-model="fromData" :fromData="fromData"/>
    </div>

    <div class="form-item prod-panel" id="prodWorkDay" >
      <div class="title">
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="调整详情"></k-field-display>
      </div>
      <div style="margin-top: 40px">
        <DisplayProdWorkDayList ref="prodWorkdayList" v-model="fromData" :fromData="fromData"/>
      </div>
    </div>


  </div>

</template>

<script>
  import DisplayProdWorkdayAdjust         from "../../M81/prodDisplay/prodWorkDay/DisplayProdWorkDayAdjust"
  import DisplayProdWorkDayList           from "../../M81/prodDisplay/prodWorkDay/DisplayProdWorkDayList"
  export default {
    name: "ProdWorkdayAdjustFlow",
    components: {
      DisplayProdWorkdayAdjust,
      DisplayProdWorkDayList
    },
    props:{
      taskInfo: {},
    },
    data() {
      return {
        fromData:{},

      }
    },
    created() {
      this.httpUtil
        .ajax({
          url: "/wf/formData/getSubmitParamsByProcessInstanceId.json",
          params: {processInstanceId: this.taskInfo.processInstanceId,_wfProcessInstanceId:this.taskInfo.processInstanceId}
        }).then(res => {
        if (res.data) {
          let submitParams = JSON.parse(res.data.submitParams);
          this.fromData = JSON.parse(submitParams.fromData);
          this.$set(this.fromData,'t8ProdInfoId',submitParams.t8ProdInfoId);
          this.$set(this.fromData,'prodCode',submitParams.prodCode);
          this.$set(this.fromData,'dateType',submitParams.dateType);
          this.$set(this.fromData,'pgmno',submitParams.pgmno);
          this.$set(this.fromData,'changeDate',submitParams.changeDate);
        }
      });

    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
