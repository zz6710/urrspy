<template>
  <k-form dataInputWidth="300px" ref="addT8ProdConsumerInsuranceForm" :data-col="2">
           <div class="form-item prod-panel" id="truteeApproval" >
              <div class="title"  >
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="托管行审批"></k-field-display>
              </div>
              <k-form ref="adddisclosureNotice2Form" :data-col="2">
                <k-form-item label="id" v-show="false" :data-col="2">
                  <k-field-text v-model="formData.id"/>
                </k-form-item>
                <k-form-item label="产品id" v-show="false">
                  <k-field-text v-model="formData.t8ProdInfoId"/>
                </k-form-item>
                <k-form-item label="是否托管行审批">
                  <k-field-select v-model="formData.trusteeExamine" data-dict="t8_prod_isok" @data-on-change="changedTrutee"/>
                </k-form-item>
                <k-form-item label="托管审批状态" v-show="formData.truteeApprovalResult==='1'">
                  <k-field-select v-model="formData.approvalStatus" data-dict="t8_approval_status" :data-default-value="'0'"/>
                </k-form-item>
                <k-form-item label="信披托管行名称" v-show="formData.truteeApprovalResult==='1'">
                  <k-field-text v-model="formData.truteeName"/>
                </k-form-item>
                <k-form-item label="信披托管行邮箱" v-show="formData.truteeApprovalResult==='1'">
                  <k-field-text v-model="formData.truteeEmail"/>
                </k-form-item>
                <k-form-item label="境外信披托管行名称" v-show="formData.truteeApprovalResult==='1'">
                  <k-field-text v-model="formData.outTruteeName"/>
                </k-form-item>
                <k-form-item label="境外信披托管行邮箱" v-show="formData.truteeApprovalResult==='1'">
                  <k-field-text v-model="formData.outTruteeEmail"/>
                </k-form-item>
                <k-form-item label="复核状态" v-show="formData.truteeApprovalResult==='1'">
                  <k-field-select v-model="formData.recheckStatus" data-dict="t8_approval_status" :data-default-value="'0'"/>
                </k-form-item>
                <k-form-footer data-align="center" v-show="formData.truteeApprovalResult==='1'" v-if="false">
                  <k-btn style="width:100px;" class="btn-custom-primary" data-functype="SUBMIT" data-action="" data-from="adddisclosureNotice2Form"
                         :data-model="formData" data-target="disclosureNotice2Grid">
                    <md-icon md-src="/static/svg/confirm.svg"></md-icon>发送托管行
                  </k-btn>
                </k-form-footer>
                <k-form-item label="托管行审批意见" data-input-width="600px" v-show="formData.truteeApprovalResult==='1'">
                  <k-field-radio data-label-width="120px" v-model="formData.truteeApprovalResult"
                                 :data-data="spyj" data-display-field="label" data-value-field="value" :data-default-value="'1'"/>
                </k-form-item>
                <k-form-item label="托管机构附件" v-show="formData.truteeApprovalResult==='1'" style="width:650px;">
                  <k-field-text style="width:70%;" v-model="formData.uploadFileName" :data-disabled="true"/>
                  <div style="display: inline-block;margin-left:10px;" v-show="false">
                    <k-btn class="md-info md-just-icon md-simple"   data-descript="上传托管机构附件"
                           data-functype="POPUP" data-size="small"  data-target="filePopup">
                      <md-icon>backup</md-icon>
                    </k-btn>
                  </div>
                </k-form-item>
                <k-form-item label="托管机构报告" data-input-width="600px" v-show="formData.truteeApprovalResult==='1'">
                  <k-field-text v-model="formData.truteeApprovalResultDesc" input-type="textarea" />
                </k-form-item>
                <k-form-footer data-align="center">
                  <k-btn style="width:100px;" class="btn-custom-plain" :data-download-name="formData.truteeNotice"  data-descript="模板信息" data-size="small"
                         @click="downloadXPGGTempVersion(formData)" :data-model="formData">
                    <md-icon md-src="/static/svg/add.svg" />下载文档</k-btn>
                </k-form-footer>
              </k-form>
              <div style="margin-top: 100px;"></div>
            </div>
  </k-form>
</template>

<script>
import Tools from "@/utils/tools";

export default {
  name: "DisplayDisclosureNoticeTrustee",
  props:{
    formData:{
      truteeApprovalResult:'0'
    },
    /*truteeApproval:{},*/
  },
  data() {
    return {
      isTruteeCheckFlag:'0',
      spyj: [
        {label: '同意', value: '1'},
        {label: '不同意', value: '0'},
      ],
    };
  },methods:{
    changedTrutee(value){
      //alert("value=:>>>>"+value);
      if(value==='1'){
        this.formData.truteeApprovalResult='1';
      }else{
        this.formData.truteeApprovalResult='0';
      }
      //console.log(this.isTruteeCheckFlag==='1');
    },
    //下载定期报告
    downloadXPGGTempVersion(params){
      var filieName = params.ruleDocName;
      this.httpUtil.download({
        url: "/download/server/PmsApp/print/downloadXPGGTempVersion.json",
        params: params,
        callback: response => {
          console.log(response)
          Tools.alert("下载完成");
        }
      }, filieName);
      /*Tools.confirm(() => {
        },
        "该模板为业务人员手动上传,内容可能与补录界面信息有差别,是否下载?"
      )*/
    },
  },
}
</script>

<style scoped>

</style>
