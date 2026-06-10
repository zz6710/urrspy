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
        <DisplayDisclosureNotice ref="addProdForm" v-model="formData" :formData="formData"/>
      </div>
    </div>
  </div>
</template>

<script>
  import DisplayDisclosureNotice         from "../../M81/DisclosureDisplay/DisplayDisclosureNotice.vue"
  export default {
    name: "DisclosureNoticeFlow",
    components: {
      DisplayDisclosureNotice
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
          let json = JSON.parse(res.data.submitParams);
          this.formData = json;
        }
      });

    },
  }
</script>

<style lang="scss" scoped>
  @import "../../../../styles/T81001.scss";
</style>
