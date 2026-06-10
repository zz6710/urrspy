<template>

  <div class="formPanel" ref="formPanel" >
    <k-form dataInputWidth="300px" ref="addT8ProdConsumerInsuranceForm" :data-col="2">
      <div class="form-item prod-panel" style="margin-top:50px;" >
        <div class="title"  >
          <div class="prod-items"></div>
          <k-field-display class="title-desc" value="托管意见确认信息"></k-field-display>
        </div>
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prodCode" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodName" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披类型">
          <k-field-select v-model="formData.disclosureType" data-dict="xp_doc_type" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披子类型">
          <k-field-select v-model="formData.disclosureSonType" data-dict="xp_son_type" :data-disabled="true"/>
        </k-form-item>
        <k-form ref="OpinionForm" :data-col="2">
          <k-form-item label="托管行审批意见" data-input-width="600px" >
            <k-field-radio data-label-width="120px" v-model="formData.truteeApprovalResult"
                           :data-data="spyj" data-display-field="label" data-value-field="value"
                          :data-disabled="true"/>
          </k-form-item>

          <k-form-item label="托管机构附件"  style="width:650px;">
            <k-field-text style="width:70%;" v-model="formData.uploadFileName" :data-disabled="true"/>
            <div style="display: inline-block;margin-left:10px;">
            <k-btn data-confirm data-size="mini" class="md-info md-just-icon md-simple"
                     data-functype="DOWNLOAD" data-url="/download/server/PmsApp/disclosure/downloadCustodian.json"
                     :data-model="formData"
                     :data-download-name="formData.uploadFileName"
                     data-descript="下载托管机构附件">
                <md-icon>cloud_download</md-icon>
              </k-btn>
            </div>
          </k-form-item>
          <k-form-item label="托管机构报告" data-input-width="600px" >
            <k-field-text v-model="formData.truteeApprovalResultDesc" input-type="textarea" :data-disabled="true"/>
          </k-form-item>
        </k-form>
      </div>
    </k-form>
  </div>
</template>

<script>

  export default {
    name: "TruteeApprovalOpinionFlow",
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
        downloadFlag: true,
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
