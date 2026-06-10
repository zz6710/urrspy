<template>
  <k-form dataInputWidth="300px" ref="addT8ProdConsumerInsuranceForm" :data-col="2">
    <div class="form-item prod-panel" style="margin-top:50px;" >
      <div class="title"  >
        <div class="prod-items"></div>
        <k-field-display class="title-desc" value="基本信息"></k-field-display>
      </div>
      <k-form ref="adddisclosureNotice2Form" :data-col="2">
        <k-form-item label="id" v-show="false">
          <k-field-text v-model="formData.id" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品id" v-show="false">
          <k-field-text v-model="formData.t8ProdInfoId" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="公告标题" data-input-width="600px">
          <k-field-text v-model="formData.noticeTitle" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prodCode" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodName" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披规则id" v-show="false">
          <k-field-text v-model="formData.t8DisclosureRuleId" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披类型">
          <k-field-select v-model="formData.disclosureType" data-dict="xp_doc_type" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披子类型">
          <k-field-select v-model="formData.disclosureSonType" data-dict="xp_son_type" :data-disabled="true"/>
        </k-form-item>
     <!--   <k-form-item label="公告完成度">
          <k-field-text v-model="formData.noticeProcess" :data-disabled="true"/>
        </k-form-item>-->
        <k-form-item label="当前阶段">
          <k-field-select v-model="formData.stage" data-dict="t8_current_stage" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披标题" data-input-width="600px">
          <k-field-text v-model="formData.ruleName" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披模板" data-input-width="600px">
          <k-field-text v-model="formData.ruleDocName" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披公告" data-input-width="600px">
          <k-field-text v-model="formData.noticeTitle" :data-disabled="true"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn style="width:100px;" class="btn-custom-plain" :data-download-name="formData.noticeTitle"
                 data-descript="模板信息" data-size="small"
                 @click="downloadXPGGTempVersion(formData)" :data-model="this.formData">
            <md-icon md-src="/static/svg/add.svg" />下载文档
          </k-btn>
        </k-form-footer>

      </k-form>
    </div>
    <!--
        <div class="form-item prod-panel" id="channelInfo" >
          <div class="title"  >
            <div class="prod-items"></div>
            <k-field-display class="title-desc" value="渠道信息"></k-field-display>
          </div>

          <div class="add-btn-div">
            <div class="add-btn"  @click="addHandler">+</div>
          </div>
          <div style="width:100%;">
            <div style="display: inline-block;position: relative;top:40px;"><label class="el-form-item__label" >发件邮箱</label></div>
            <div label="发件邮箱" style="width:225px;">
              <k-field-text style="margin-left:70px;" v-model="noticeTitle" :data-disabled="true"/>
            </div>
          </div>

          <k-grid ref="disclosureRuleGrid" @data-row-select="selectRow" :data-operate-column="false"  data-action="findChannelRuleList"
                  @init="(grid)=>{this.disclosureRule.$AssetInfoGrid = grid}">
            <k-grid-column data-header="渠道id" data-name="id" :data-hidden="true"></k-grid-column>
            <k-grid-column data-header="发布渠道" data-name="channelName" :data-hidden="false"></k-grid-column>
            <k-grid-column data-header="地址" data-name="emails" :data-hidden="false"></k-grid-column>
            <k-grid-column data-header="发布状态" data-name="publicStatus" :data-hidden="false"></k-grid-column>
            <k-grid-column data-header="发布时间" data-name="publicTime" :data-hidden="false"></k-grid-column>
          </k-grid>
        </div>

            <div class="form-item prod-panel" id="companyApproval"  v-show="false">
              <div class="title"  >
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="公司审批"></k-field-display>
              </div>
              <k-form ref="adddisclosureNotice2Form" :data-col="2">
                <k-form-item label="id" v-show="false" :data-col="2">
                  <k-field-text v-model="formData.id"/>
                </k-form-item>
                <k-form-item label="产品id" v-show="false">
                  <k-field-text v-model="formData.t8ProdInfoId"/>
                </k-form-item>
                <k-form-item label="是否需公司审批">
                  <k-field-select v-model="formData.trusteeExamine" data-dict="t8_prod_isok" :data-default-value="'0'" @data-on-change="changedCompany"/>
                </k-form-item>
                <k-form-item label="审批状态" v-show="this.isCompanyCheckFlag==='1'">
                  <k-field-select v-model="formData.prodCode" data-dict="t8_approval_status" :data-default-value="'0'"/>
                </k-form-item>
                <k-form-item label="流程审批条件" v-show="this.isCompanyCheckFlag==='1'">
                  <k-field-select v-model="formData.prodName" data-dict="t8_flow_check"/>
                </k-form-item>
                <k-form-item label="需关注要点" data-input-width="600px" v-show="this.isCompanyCheckFlag==='1'">
                  <k-field-text v-model="formData.t8DisclosureRuleId" input-type="textarea" />
                </k-form-item>
              </k-form>
            </div>

           <div class="form-item prod-panel" id="truteeApproval" >
              <div class="title"  >
                <div class="prod-items"></div>
                <k-field-display class="title-desc" value="托管行审批"></k-field-display>
              </div>
              <k-form ref="adddisclosureNotice2Form" :data-col="2">
                <k-form-item label="id" v-show="false" :data-col="2">
                  <k-field-text v-model="truteeApproval.id"/>
                </k-form-item>
                <k-form-item label="产品id" v-show="false">
                  <k-field-text v-model="truteeApproval.t8ProdInfoId"/>
                </k-form-item>
                <k-form-item label="是否托管行审批">
                  <k-field-select v-model="truteeApproval.trusteeExamine" data-dict="t8_prod_isok" @data-on-change="changedTrutee"/>
                </k-form-item>
                <k-form-item label="托管审批状态" v-show="this.isTruteeCheckFlag==='1'">
                  <k-field-select v-model="truteeApproval.approvalStatus" data-dict="t8_approval_status" :data-default-value="'0'"/>
                </k-form-item>
                <k-form-item label="信披托管行名称" v-show="this.isTruteeCheckFlag==='1'">
                  <k-field-text v-model="truteeApproval.truteeName"/>
                </k-form-item>
                <k-form-item label="信披托管行邮箱" v-show="this.isTruteeCheckFlag==='1'">
                  <k-field-text v-model="truteeApproval.truteeEmail"/>
                </k-form-item>
                <k-form-item label="境外信披托管行名称" v-show="this.isTruteeCheckFlag==='1'">
                  <k-field-text v-model="truteeApproval.outTruteeName"/>
                </k-form-item>
                <k-form-item label="境外信披托管行邮箱" v-show="this.isTruteeCheckFlag==='1'">
                  <k-field-text v-model="truteeApproval.outTruteeEmail"/>
                </k-form-item>
                <k-form-item label="复核状态" v-show="this.isTruteeCheckFlag==='1'">
                  <k-field-select v-model="truteeApproval.recheckStatus" data-dict="t8_approval_status" :data-default-value="'0'"/>
                </k-form-item>
                <k-form-footer data-align="center" v-show="this.isTruteeCheckFlag==='1'">
                  <k-btn style="width:100px;" class="btn-custom-primary" data-functype="SUBMIT" data-action="" data-from="adddisclosureNotice2Form"
                         :data-model="truteeApproval" data-target="disclosureNotice2Grid">
                    <md-icon md-src="/static/svg/confirm.svg"></md-icon>发送托管行
                  </k-btn>
                </k-form-footer>
                <k-form-item label="托管行审批意见" data-input-width="600px" v-show="this.isTruteeCheckFlag==='1'">
                  <k-field-radio data-label-width="120px" v-model="truteeApproval.truteeApprovalResult"
                                 :data-data="spyj" data-display-field="label" data-value-field="value" :data-default-value="'1'"/>
                </k-form-item>
                <k-form-item label="托管机构附件" v-show="this.isTruteeCheckFlag==='1'" style="width:650px;">
                  <k-field-text style="width:70%;" v-model="truteeApproval.uploadFileName" :data-disabled="true"/>
                  <div style="display: inline-block;margin-left:10px;">
                    <k-btn class="md-info md-just-icon md-simple"   data-descript="上传托管机构附件"
                           data-functype="POPUP" data-size="small"  data-target="filePopup">
                      <md-icon>backup</md-icon>
                    </k-btn>
                  </div>
                </k-form-item>
                <k-form-item label="托管机构报告" data-input-width="600px" v-show="this.isTruteeCheckFlag==='1'">
                  <k-field-text v-model="truteeApproval.truteeNotice" input-type="textarea" />
                </k-form-item>
              </k-form>
              <div style="margin-top: 100px;"></div>
            </div>-->
  </k-form>
</template>

<script>
import Tools from "@/utils/tools";

export default {
  name: "DisplaydisclosureNotice",
  props:{
    formData:{

    },
    /*truteeApproval:{},*/
  },methods:{
    //下载定期报告
    downloadXPGGTempVersion(params){
      var filieName = params.noticeTitle+".docx";
      console.log("文件名",filieName)
      if(params.disclosureType=='2'||params.disclosureType=='3'){
        params.docType = ''
        //发行公告到期公告下载是单独写的比较特殊
        this.httpUtil.download({
          url: "/download/server/PmsApp/print/downloadIssued.json",
          params: params,
          callback: response => {
            //response)
            Tools.alert("下载完成");
          }
        }, filieName);
      }else {
        //var filieName = params.ruleDocName;
        this.httpUtil.download({
          url: "/download/server/PmsApp/print/downloadXPGGTempVersion.json",
          params: params,
          callback: response => {
            console.log(response)
            Tools.alert("下载完成");
          }
        }, filieName);
      }
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
