<template>

  <div class="formPanel" ref="formPanel" >
<!--    <div class="form-item prod-panel" id="prodAdjust" >-->
<!--      <div class="title">-->
<!--      </div>-->
<!--    </div>-->

    <div class="form-item prod-panel" id="prodWorkDay" >
      <div class="title">
      </div>
      <div style="margin-top: 40px">
        <DisplayDisclosureNetValueNotice ref="addProdForm" v-model="formData" :formData="formData"/>
      </div>
    </div>
  </div>
</template>

<script>
  import DisplayDisclosureNetValueNotice         from "../../M81/DisclosureDisplay/DisplayDisclosureNetValueNotice.vue"
  export default {
    name: "DisclosureNoticeNetValueFlow",
    components: {
      DisplayDisclosureNetValueNotice
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
          let json = JSON.parse(res.data.submitParams);
          console.log("json=:>>",json);
          this.formData = json;
          console.log("this.formData1111=:>>",this.formData);
          //this.$set(this.formData,'disclosureNotice',json);
         // console.log("this.fromData=:>>>>",this.formData);
          //console.log("this.formData=:>>>>",this.formData);
        }
      });

    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
