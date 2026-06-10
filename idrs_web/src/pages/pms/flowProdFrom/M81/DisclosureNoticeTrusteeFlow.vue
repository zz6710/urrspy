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
        <DisplayDisclosureNoticeTrustee ref="addProdForm" v-model="formData" :formData="formData"/>
      </div>
    </div>
  </div>
</template>

<script>
  import DisplayDisclosureNoticeTrustee         from "../../M81/DisclosureDisplay/DisplayDisclosureNoticeTrustee.vue"
  export default {
    name: "DisclosureNoticeTrusteeFlow",
    components: {
      DisplayDisclosureNoticeTrustee
    },
    props:{
      taskInfo: {},
    },
    data() {
      return {
        formData:{},
        spyj: [
          {label: '同意', value: '1'},
          {label: '不同意', value: '0'},
        ],
        disclosureNotice: {},
        selectRowData: {},
        isCompanyCheckFlag:'',
        isTruteeCheckFlag:'',
        disclosureRule: {
          $AssetInfoGrid: null,
        },
        addDisclosureRuleForm: {},
        editDisclosureRuleForm: {},
        filFormData:{
          prodCode:'',
          prodName:'',
          documentType:'',
          version:'',
          isTemplateFile:'',
          t8TruteeInfoId:''
        },
        uploadFileName:'',
        truteeApproval: {},
        inGroup:''
      }
    },
    created() {
      this.httpUtil
        .ajax({
          url: "/wf/formData/getSubmitParamsByProcessInstanceId.json",
          params: {processInstanceId: this.taskInfo.processInstanceId,_wfProcessInstanceId:this.taskInfo.processInstanceId}
        }).then(res => {
        if (res.data) {
          console.log("res.data.submitParams=:>>>>",res.data.submitParams);
          let json = JSON.parse(res.data.submitParams);
          //console.log("json.disclosureNotice=:>>>>",json.truteeApproval);
          //this.formData = JSON.parse(json.truteeApproval);
          this.formData = json;
          //this.$set(this.formData,'disclosureNotice',json);
          //console.log("this.fromData=:>>>>",this.formData);
          console.log("this.formData=:>>>>",this.formData);
        }
      });

    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
