<template>
  <div class="formPanel" ref="formPanel" style="margin-left: 15%;">
    <div class="form-item prod-panel" >
      <div class="title">
      </div>
      <DisplayFeeDivide ref="baseInfoForm" v-model="detailData" :detailData="detailData"/>
    </div>
  </div>
</template>

<script>
  import DisplayFeeDivide         from "../../M81/prodDisplay/DisplayFeeDivide.vue"
    export default {
        name: "FeeDivideFlow",
      components: {
        DisplayFeeDivide
      },
      props:{
        taskInfo: {},
      },
      data() {
        return {
          detailData:{},
        }
      },
      created() {
        this.httpUtil
          .ajax({
            url: "/wf/formData/getSubmitParamsByProcessInstanceId.json",
            params: {processInstanceId: this.taskInfo.processInstanceId,_wfProcessInstanceId:this.taskInfo.processInstanceId}
          }).then(res => {
          if (res.data) {
            this.detailData = JSON.parse(res.data.submitParams);
            let strBool = this.detailData.switchDetailSegmentValue;
            if(strBool == 'true' || strBool == true){
              this.detailData.switchDetailSegmentValue = true;
            }else{
              this.detailData.switchDetailSegmentValue = false;
            }
            if (this.detailData.t8FeeDivideSectionList) {
              let t8FeeDivideSectionList = JSON.parse(this.detailData.t8FeeDivideSectionList);
              this.$set(this.detailData,'tailingCommisionMoneyList',t8FeeDivideSectionList);
            }

          }
        });

      },
    }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
