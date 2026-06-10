<template>
  <div>
    <k-form-search-customize data-target="prodInfoGrid" v-model="prodSearchParam">

      <k-form-item label="产品代码">
        <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findTaProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"/>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="产品状态">
        <k-field-select v-model="prodSearchParam.prodStatus" data-dict="t8_prod_status"/>
      </k-form-item>
      <k-form-item label="审批状态" v-show="false">
        <k-field-select v-model="prodSearchParam.processStatus" data-dict="process_status"/>
      </k-form-item>
      <k-form-item label="是否存在托管协议" v-show="true" data-input-width="194px" data-label-width="150px">
        <k-field-select v-model="prodSearchParam.isHave" data-dict="t8_prod_isok"/>
      </k-form-item>
      <k-form-item label="代码回收">
        <k-field-select v-model="prodSearchParam.isRecycleCode" data-dict="1yes0no"></k-field-select>
      </k-form-item>
    </k-form-search-customize>

    <k-grid ref="prodInfoGrid" data-action="EscrowAgreement.findProdEscrowAgreement1" @data-row-select="selectRow">
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="产品状态" data-name="prodStatus" data-dict="t8_prod_status"/>
      <k-grid-column data-align="center" data-header="托管行名称" data-name="truteeName"/>
      <k-grid-column data-align="center" data-header="托管行代码" data-name="truteeCode"/>
      <k-grid-column data-align="center" data-header="托管行协议编号" data-name="truteeNum"/>
      <k-grid-column data-align="center" data-header="是否境外" data-name="isOutside"  data-dict="t8_prod_isok"/>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="生成托管协议文档" data-functype="SUBMIT" data-size="small"
               data-action="EscrowAgreement.generateEscrowAgreementByProdCode" data-target="escrowAgreementGrid"
               v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
               global.isShowAuthorityButton('EscrowAgreement.generateEscrowAgreementByProdCode')"
               :data-after-success="changeVersion"
               v-model="scope.row.row" :data-confirm="true" v-show="showGenerate"
               :data-disabled="scope.row.row.hasTemplate === '0'">
          <md-icon>add_circle</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" v-model="scope.row.row" data-descript="上传托管协议"
               data-functype="POPUP" data-size="small" @click="setFileParams(scope.row.row)" data-target="filePopup"
               v-if="global.isShowAuthorityButton('EscrowAgreement.uploadHostAgreement')"
               v-show="showUploadEscrowAgree">
          <md-icon>backup</md-icon>
        </k-btn>

        <k-btn data-functype="POPUP" data-size="mini" data-target="editTable" data-descript="托管协议编号维护"
               class="md-info md-just-icon md-simple" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
               global.isShowAuthorityButton('EscrowAgreement.updateTrutee')"
               v-show="showEditTruteeNo">
          <md-icon>edit</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="editTable"  data-title="托管协议编号维护">
      <k-form ref="editForm" data-ui="element">
        <k-form-item label="产品代码">
          <k-field-text v-model="selectRowData.prodCode" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="selectRowData.prodName" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="托管行名称">
          <k-field-text v-model="selectRowData.truteeName" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="是否境外">
          <k-field-select v-model="selectRowData.isOutside" :data-allowblank="false" :data-disabled="true"   data-dict="t8_prod_isok"/>
        </k-form-item>

        <k-form-item label="境内托管协议编号" v-show="selectRowData.isOutside == '0'">
          <k-field-text v-model="selectRowData.gdTrutee"  :data-max-length="200" :data-allowblank="selectRowData.isOutside != '0'"/>
        </k-form-item>
        <k-form-item label="境外托管协议编号" v-show="selectRowData.isOutside == '1'">
          <k-field-text v-model="selectRowData.gdOutTrutee" :data-max-length="200" :data-allowblank="selectRowData.isOutside != '1'" />
        </k-form-item>

        <k-form-item label="境内托管协议生成日期" v-show="selectRowData.isOutside == '0'">
          <k-field-date v-model="selectRowData.truteeCustodyData" :data-max-length="20" :data-allowblank="selectRowData.isOutside != '0'"/>
        </k-form-item>

        <k-form-item label="境外托管协议生成日期" v-show="selectRowData.isOutside == '1'">
          <k-field-date v-model="selectRowData.outTruteeCustodyData"  :data-max-length="20" :data-allowblank="selectRowData.isOutside != '1'" />
        </k-form-item>


        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                 data-from="editForm" :data-model="selectRowData" data-target="prodInfoGrid"
                 data-action="EscrowAgreement.updateTrutee">
            <md-icon md-src="/static/svg/confirm.svg"/>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"/>取消
          </k-btn>
        </k-form-footer>

      </k-form>
    </k-popup>

    <k-grid ref="escrowAgreementGrid" :data-autoload="false" data-action="T8ProdDocumentVersion.findEscrowAgreementByProdCode">
      <k-grid-column data-align="center" data-hidden="true" data-header="文档类型" data-name="documentType"/>
      <k-grid-column data-align="center" data-header="版本id" data-name="id" data-hidden="true"/>
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="文件名" data-name="fileName"/>
      <k-grid-column data-align="center" data-header="文档版本" data-name="version"/>
      <k-grid-column data-align="center" data-header="是否模板文档" data-dict="1yes0no" data-name="isTemplateFile"/>
      <k-grid-column data-align="center" data-header="托管协议名称" data-name="documentName"/>
      <k-grid-column data-align="center" data-header="托管行名称" data-name="truteeName"/>
      <!-- <k-grid-column data-align="center" data-header="审批状态"  data-name="approveStatus" data-dict="process_status"/> -->
      <k-grid-column data-align="center" data-header="用印审批状态"  data-name="approveStatus" data-dict="t8_process_status"/>
      <k-grid-column data-align="center" data-header="托管协议状态" data-dict="t8_document_status" data-name="documentStatus"/>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" :data-download-name="downFileName(scope.row.row)"
               data-descript="下载托管协议" data-functype="DOWNLOAD" data-size="small"
               data-url="/download/server/PmsApp/prodDocument/downloadOnlineEditT8ProdDocumentVersion.json" v-model="scope.row.row">
          <md-icon>cloud_download</md-icon>
        </k-btn>
        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple"  v-model="scope.row.row" data-descript="投资审核" data-functype="POPUP" data-size="small"
                 @click="auditBefore(scope.row.row, '1')" data-target="investmentAuditPopup"
                 v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
               global.isShowAuthorityButton('EscrowAgreement.addAudiopinion')"
                 v-show="showInvestAudit">
            <md-icon>person_add</md-icon>
          </k-btn>
        </div>

        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple"  v-model="scope.row.row" data-descript="运营审核" data-functype="POPUP" data-size="small"
                 @click="auditBefore(scope.row.row, '2')" data-target="operationAuditPopup"
                 v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
               global.isShowAuthorityButton('EscrowAgreement.addAudiopinion2')"
                 v-show="showOperationAudit">
            <md-icon>person_add</md-icon>
          </k-btn>
        </div>
        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple" :data-disabled="scope.row.row.documentStatus!=1"  ref="statusConfirm"
                 v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
                 global.isShowAuthorityButton('EscrowAgreement.udpateDocumentStatus')"
                 data-descript="托管协议确认操作" data-functype="SUBMIT" data-size="small"
                 data-action="EscrowAgreement.udpateDocumentStatus" :data-after-success="confirmAfterSuccess" data-target="escrowAgreementGrid"
                 v-model="scope.row.row" :data-confirm="true" v-show="showConfirm">
            <md-icon>done</md-icon>
          </k-btn>
        </div>

        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple" :data-disabled="scope.row.row.documentStatus=='1'"
                 v-model="scope.row.row" data-descript="上传法审文件" data-functype="POPUP" data-size="small"
                 @click="addSealCilck(scope.row.row,'2','上传法审文件')" data-target="addPopup"
                 v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
                 global.isShowAuthorityButton('EscrowAgreement.uploadLegalDocuments')"
                 v-show="showUploadLaw">
            <md-icon>backup</md-icon>
          </k-btn>
        </div>
        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple" :data-disabled="scope.row.row.documentStatus=='1'"
                 v-model="scope.row.row" data-descript="上传用印扫描件" data-functype="POPUP" data-size="small"
                 @click="addSealCilck(scope.row.row,'6','上传用印扫描件')" data-target="addPopup"
                 v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
                 global.isShowAuthorityButton('EscrowAgreement.uploadScannedCopy')"
                 v-show="showUploadSeal">
            <md-icon>backup</md-icon>
          </k-btn>
        </div>
        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple"   v-model="scope.row.row" data-descript="用印申请"
                 v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
                 global.isShowAuthorityButton('EscrowAgreement.applicationPrinting')"
                 data-functype="POPUP" data-size="small"
                 @click="applicationData(scope.row.row)" v-show="showApplySeal">
            <md-icon>create_new_folder</md-icon>
          </k-btn>
        </div>
        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple" data-descript="在线编辑"
                 data-size="small" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
                 global.isShowAuthorityButton('EscrowAgreement.getT8OnlineWordValueList')"
                 :data-disabled="scope.row.row.isTemplateFile === '0'"
                 @click="onlineEditHandler(scope.row.row)"
                 data-functype="POPUP"  data-target="onlineEditPopup" v-model="scope.row.row" v-show="showEditOnline">
            <md-icon>edit</md-icon>
          </k-btn>
        </div>

        <k-btn class="md-info md-just-icon md-simple" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)"
               data-descript="下载附件" data-functype="POPUP" data-target="downListPOPUP" v-model="scope.row.row" v-show="showDownloadAttach">
          <md-icon>weekend</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)"
               data-descript="用印记录" data-functype="POPUP" @click="getTempId(scope.row.row)" data-target="getListPOPUP" v-model="scope.row.row" v-show="showDownloadAttach">
          <md-icon>library_books</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <!--上传托管协议-->
    <k-popup ref="filePopup" title="上传托管协议">
      <k-form ref="fileForm" data-ui="element">
        <k-form-item label="产品代码" v-show="false">
          <k-field-text v-model="filFormData.prodCode" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品代码" v-show="false">
          <k-field-text v-model="filFormData.prodName" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="是否模板文件" v-show="false">
          <k-field-text v-model="filFormData.isTemplateFile" data-default-value="0" />
        </k-form-item>
        <k-form-item label="托管行id" v-show="false">
          <k-field-text v-model="filFormData.t8TruteeInfoId"/>
        </k-form-item>
        <k-form-item label="托管协议" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="fileUploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onFileSubmitError" :data-success="onFileSubmitSuccess"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/escrowAgreement/uploadEscrowAgreement.json">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="printTempGrid" ref="fileSubmitBtn"
                 data-from="fileForm" :data-model="filFormData" :data-handler="fileSubmitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--投资审核-->
    <k-popup ref="investmentAuditPopup" data-title="投资审核">
      <k-form ref="investmentAuditForm" :data-col="2" >
        <template v-for="item in queryList">
          <k-form-item label="投资审核意见" :data-col="2">
            <k-field-text v-model="item.auditOpinion" :data-allowblank="false" :data-disabled="true"
                          :data-max-length="2000" inputType="textarea" :rows="1"/>
          </k-form-item>
          <k-form-item label="审核日期">
            <k-field-text v-model="item.opinionDate" :data-allowblank="false" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="审核人">
            <k-field-select v-model="item.auditor" data-action="User.findUsersForLogin"
                            data-value-field="userid"
                            data-display-field="username" :data-allowblank="false" data-disabled="true"/>
          </k-form-item>
        </template>
        <k-field-text v-model="formData.t8ProdDocumentVersionId" v-show="false" />
        <k-field-text v-model="formData.auditType" v-show="false"  />
        <k-form-item label="投资审核意见" :data-col="2">
          <k-field-text v-model="formData.auditOpinion" :data-allowblank="false" :data-max-length="2000" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="审核日期">
          <k-field-date v-model="formData.opinionDate" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="审核人">
          <k-field-select v-model="formData.auditor"
                          data-action="User.findUsersForLogin"
                          data-value-field="userid"
                          data-display-field="username" :data-default-value="userid"
                          :data-allowblank="false" data-disabled="true"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  ref="submitBtn"
                 data-from="investmentAuditForm" :data-model="formData" data-action="EscrowAgreement.addAudiopinion">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <!--运营审核-->
    <k-popup ref="operationAuditPopup" data-title="运营审核">
      <k-form ref="operationAuditForm" :data-col="2">
        <template v-for="item in queryList">
          <k-form-item label="运营审核意见" :data-col="2">
            <k-field-text v-model="item.auditOpinion" :data-allowblank="false" :data-disabled="true"
                          :data-max-length="2000" inputType="textarea" :rows="1"/>
          </k-form-item>
          <k-form-item label="审核日期">
            <k-field-text v-model="item.opinionDate" :data-allowblank="false" :data-disabled="true"/>
          </k-form-item>
          <k-form-item label="审核人">
            <k-field-select v-model="item.auditor" data-action="User.findUsersForLogin"
                            data-value-field="userid"
                            data-display-field="username" :data-allowblank="false" data-disabled="true"/>
          </k-form-item>
        </template>
        <k-field-text v-model="formData.t8ProdDocumentVersionId" v-show="false" />
        <k-field-text v-model="formData.auditType" v-show="false"  />
        <k-form-item label="运营审核意见"  :data-col="2">
          <k-field-text v-model="formData.auditOpinion" :data-allowblank="false" :data-max-length="2000" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="审核日期">
          <k-field-date v-model="formData.opinionDate" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="审核人">
          <k-field-select v-model="formData.auditor"
                          data-action="User.findUsersForLogin"
                          data-value-field="userid"
                          data-display-field="username" :data-default-value="userid"
                          :data-allowblank="false" data-disabled="true"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  ref="submitBtn"
                 data-from="operationAuditForm" :data-model="formData" data-action="EscrowAgreement.addAudiopinion2">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--上传附件弹出框-->
    <k-popup ref="addPopup" :data-title="AttachmentpopupTitle">
      <k-form ref="addForm" data-ui="element" >
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prodCode" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodName" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onSubmitError" :data-success="onSubmitSuccess"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/escrowAgreement/uploadEscrowAgreementAttached.json">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  ref="submitBtn"
                 data-from="addForm" :data-model="formData" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--用印申请弹出框-->
    <k-popup ref="applicationPopup" data-title="用印申请">
      <k-form ref="applicationForm" :data-col="2" dataLabelWidth="150px" dataInputWidth="200px">
        <k-field-text v-model="application.t8ProdDocumentVersionId" v-show="false" :data-allowblank="false" />
        <k-form-item label="用印件名称">
          <k-field-text v-model="application.printName" :data-allowblank="false" />
        </k-form-item>

        <k-form-item label="用印发起日期">
          <k-field-date v-model="application.printDate" :data-allowblank="false" />
        </k-form-item>

        <k-form-item label="用印部门">
          <k-field-select v-model="application.printOrg" data-action='Dept.find' data-display-field="deptname"
                          data-value-field="deptno" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="用印人">
          <k-field-select v-model="application.printUser" data-action="User.findUsers" data-display-field="username"  data-value-field="userid"
                          :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="联系电话">
          <k-field-text v-model="application.phone" :data-max-length="11"
                        data-validate-type="int" data-type="int" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="用印种类">
          <k-field-select v-model="application.printType" data-dict="t8_print_type" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="发往单位">
          <k-field-text v-model="application.shipUnit" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="用印性质">
          <k-field-select v-model="application.printProperties" data-dict="t8_print_properties" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="法律审查意见书编号">
          <k-field-text v-model="application.opinionNumber" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="审批类型">
          <k-field-select v-model="application.approvalType" :data-allowblank="false" data-dict="t8_approval_type"/>
        </k-form-item>

        <k-form-item label="用印种类及个数详情" data-input-width="590px">
          <k-field-text v-model="application.typeNum" :data-allowblank="false" :data-max-length="2000" inputType="textarea" :rows="1"/>
        </k-form-item>

        <k-form-item label="用印事由" data-input-width="590px">
          <k-field-text v-model="application.printReason" :data-allowblank="false" :data-max-length="2000" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="addAudiopinion"
                 data-from="applicationForm" :data-model="application">确定
          </k-btn>

          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--下载附件弹出框-->
    <k-popup ref="downListPOPUP" title="附件信息列表" @data-opened="loadAttachmentInfo">
      <k-grid ref="downloadAgencyAgreementGrid"  :data-autoload="false"
              data-action="DocumentAttachment.getAttachmentInfo" :dataPopupAppendToBody="true">
        <k-grid-column data-align="center" data-header="附件名称" data-name="fileName"/>
        <k-grid-column data-align="center" data-header="创建日期" data-name="crtDate"/>
        <k-grid-column data-align="center" data-header="创建时间" data-name="crtTime"/>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" :data-download-name="scope.row.row.fileName"
                 data-descript="下载" data-functype="DOWNLOAD" data-size="small"
                 data-url="/download/server/PmsApp/documentAttachment/downAttachment.json" v-model="scope.row.row">
            <md-icon>cloud_download</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple"  data-descript="删除附件" data-functype="SUBMIT"  :data-confirm="true"
                 data-target="downloadAgencyAgreementGrid"
                 data-action="EscrowAgreement.deleteFile"
                 v-if="global.isShowAuthorityButton('EscrowAgreement.deleteFile')">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </k-popup>

    <!--用印记录-->
    <k-popup ref="getListPOPUP" title="用印记录列表" @data-opened="loadApproveInfo"  >
      <k-grid ref="getAgencyAgreementGrid"  :data-autoload="false"
              data-action="T8PrintApplication.findApprovalAudiopinion" :dataPopupAppendToBody="true" :data-operate-column="false">
        <k-grid-column data-align="center" data-header="产品文档id" data-name="t8ProdDocumentVersionId" data-hidden="true"/>
        <k-grid-column data-align="center" data-header="用印件名称" data-name="printName" dataWidth="200"/>
        <k-grid-column data-align="center" data-header="用印部门" data-name="printOrg" data-hidden="true"/>
        <k-grid-column data-align="center" data-header="用印部门" data-name="printOrgName" dataWidth="120"/>
        <k-grid-column data-align="center" data-header="用印人" data-name="printUser" data-hidden="true"/>
        <k-grid-column data-align="center" data-header="用印人" data-name="printUserName" dataWidth="120"/>
        <k-grid-column data-align="center" data-header="联系电话" data-name="phone" dataWidth="120"/>
        <k-grid-column data-align="center" data-header="用印种类" data-name="printType" data-dict="t8_print_type" dataWidth="120"/>
        <k-grid-column data-align="center" data-header="用印种类及数量" data-name="typeNum" dataWidth="120"/>
        <k-grid-column data-align="center" data-header="发往单位" data-name="shipUnit" dataWidth="120"/>
        <k-grid-column data-align="center" data-header="用印性质" data-name="printProperties" data-dict="t8_print_properties" dataWidth="120"/>
        <k-grid-column data-align="center" data-header="用印事由" data-name="printReason" dataWidth="120"/>
        <k-grid-column data-align="center" data-header="法律审查意见书编号" data-name="opinionNumber" dataWidth="120"/>
        <k-grid-column data-align="center" data-header="审批类型" data-name="approvalType" data-dict="t8_approval_type" dataWidth="120"/>
        <k-grid-column data-align="center" data-header="创建日期" data-type="date" data-name="crtDate" dataWidth="120"/>
        <k-grid-column data-align="center" data-header="创建时间" data-type="time" data-name="crtTime" dataWidth="120"/>
        <k-grid-column data-align="center" data-header="更新日期" data-type="date" data-name="updDate" data-hidden="true"/>
        <k-grid-column data-align="center" data-header="更新时间" data-type="time" data-name="updTime" data-hidden="true"/>
        <k-grid-column data-align="center" data-header="录入人" data-name="inputuser" data-hidden="true"/>
        <k-grid-column data-align="center" data-header="录入人" data-name="inputusername" dataWidth="120"/>
        <k-grid-column data-align="center" data-header="修改人" data-name="udpateuser" data-hidden="true"/>
        <k-grid-column data-align="center" data-header="审批任务ID" data-name="processInstanceId" data-hidden="true"/>

      </k-grid>
    </k-popup>

    <k-popup ref="onlineEditPopup" data-width="80%" :data-dialog-drag="true" style="margin-left:10%;">
      <div class="edit">
        <div class="word">
          <iframe name="onlineEdit" id="onlineEdit" :src="viewUrl"></iframe>
        </div>
        <div class="form">
          <div>
            <k-btn data-functype="SUBMIT" :data-handler="save" class="btn-custom-primary"
                   data-form="setRoleForm">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn @click="closePopup" class="btn-custom-plain"
                   data-form="setRoleForm">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
            </k-btn>
          </div>
          <k-form ref="tableFormData" :data-col="1" style="width: 100%">
            <k-form-item v-for="(item,index) in onlineEditData" :key="index" :label="item.wordComment+':'">
              <k-field-text v-show="item.isDisabled=='1'" style="width: 130%" inputType="textarea" :ref="item.wordKey" @input="itemChange($event,item.wordKey)" v-model="item.wordValue"/>
              <k-field-text v-show="item.isDisabled=='0'" style="width: 130%" inputType="textarea" :rows="1" :data-disabled="true" :value="item.wordValue"/>
            </k-form-item>
          </k-form>
        </div>
      </div>
    </k-popup>
  </div>
</template>

<script>
  import {assign} from "lodash";
  import Tools from "@/utils/tools";
  export default {
    name: "escrowAgreement",
    data() {
      return {
        prodSearchParam: {
          prodCode: ''
        },
        formData: {
          prodCode: '',
          prodName: '',
          version: '',
          remark:'',
          docType:'',
          auditOpinion: '',
          opinionDate: '',
          auditor: '',
          currentWorkday: '',
          auditType: '',
          t8ProdDocumentVersionId: '',
          attachmentType: ''
        },
        filFormData:{
          prodCode:'',
          prodName:'',
          documentType:'',
          version:'',
          isTemplateFile:'',
          t8TruteeInfoId:''
        },
        selectRowData:{},
        onlineEditData:{},
        viewUrl: '',
        userid: '',
        queryList: [],
        opinionTitle: '',
        application: {},
        AttachmentpopupTitle: '',
        toDate:'',
        showGenerate: true,//是否显示生成按钮
        showEditTruteeNo: true,//是否显示维护托管协议编号按钮
        showInvestAudit: true,//是否显示投资审核按钮
        showOperationAudit: true,//是否显示运营审核按钮
        showConfirm: true,//是否显示确认按钮
        showUploadLaw: true,//是否显示上传法审版按钮
        showUploadSeal: true,//是否显示上传用印按钮
        showApplySeal: true,//是否显示用印申请按钮
        showEditOnline: true,//是否显示在线编辑按钮
        showDownloadAttach: true,//是否显示下载附件按钮
        showUploadEscrowAgree: true,//是否显示上传托管协议按钮
        lastVersion: '',
        tempId:'',
      }

    },

    mounted() {
      //获取系统当前用户
      Tools.getLoginUser().then(res => {
        this.userid = res.userid;
      })
      //获取系统当前时间
        this.httpUtil.sysDate().then(res=>{
        //console.log(res);
        if (res) {
          this.$set(this.formData, 'opinionDate', res)
        }
      });

      window.addEventListener('message', (e)=>{
        if(e.data.key){
          let refName=e.data.key
          this.$refs[refName][0].focus()
        }
      })
    },
    created() {
      this.global.getProdUser('');
      this.$nextTick(()=>{
        //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
        this.global.getHideButtons(this);
        let prodCode = this.$route.query.prod_code;
        if(prodCode !=''&&prodCode!=undefined){
          this.$refs.prodInfoGrid.load({prodCode:prodCode});
        }
      });
    },
    methods:{
      changeVersion(data){
        this.httpUtil.comnQuery({
          action: 'T8ProdDocumentVersion.getNewestT8ProdDocumentVersion',
          params: {
            prodCode: data.returndata.prodCode,
            documentType:data.returndata.documentType
          }
        }).then(data => {
          this.$nextTick(() => {
            let version = data.rows[0].version;
            let array = version.split(".");
            let numbers = array[1];
            numbers = numbers-1;
            this.lastVersion = array[0]+"."+numbers;
          })
        });
      },
      //20210401 判断是否是最新版本
      checkVersion(version){
        if(version==this.lastVersion){
          return true;
        }else{
          return false;
        }
      },
      //axin 反显数据
      applicationData(val){
        this.$set(this.application,'t8ProdDocumentVersionId',val.id);
        this.$set(this.application,'printName',val.prodName+'-'+val.prodCode+' '+'托管协议用印申请');
        this.$set(this.application,'printDate',this.toDate);

        this.httpUtil.comnQuery({
          action: "User.findUsersForLogin",
          params: {},
          mask: false,
          successAlert:false
        }).then(data => {
            this.$nextTick(()=>{
              this.httpUtil.comnQuery({
                action: "PublishNote.findApprovalInfo",
                params: {t8ProdDocumentVersionId: val.id},
                mask: false,
                successAlert:false
              }).then(res => {
                this.$nextTick(()=>{
                  if(res.rows.length>0){
                    let result = res.rows[0];
                    this.$set(this.application,'phone',result.phone);
                    this.$set(this.application,'printType',result.printType);
                    this.$set(this.application,'typeNum',result.typeNum);
                    this.$set(this.application,'shipUnit',result.shipUnit);
                    this.$set(this.application,'printProperties',result.printProperties);
                    this.$set(this.application,'printReason',result.printReason);
                    this.$set(this.application,'opinionNumber',result.opinionNumber);
                    this.$set(this.application,'approvalType',result.approvalType);
                  }
                  let row = data.rows[0];
                  this.$set(this.application,'printUser',row.userid);
                  this.$set(this.application,'printOrg',row.deptno);
                  //this.$set(this.application,'phone',row.mobileno);
                  this.$refs.applicationPopup.popup();
                });
              });
            });
        });

      },
      downFileName(rows) {
        if (rows.isTemplateFile === '0') {
          return rows.fileName
        } else {
          return rows.prodName+"产品"+rows.documentName+rows.version+'.docx'
        }
      },
      setFileParams(rows) {
        this.filFormData.documentType = rows.documentType
        this.filFormData.prodCode = rows.prodCode
        this.filFormData.prodName = rows.prodName
        this.filFormData.t8TruteeInfoId = rows.t8TruteeInfoId
      },
      onFileSubmitError(){
        this.$refs.fileUploadRef.doReset();
        this.$refs.fileSubmitBtn.setIconStyle(1, []);
      },
      onFileSubmitSuccess() {
        this.$refs.fileUploadRef.doReset();
        this.$refs.fileForm.reset();
        this.$refs.filePopup.close();
      },
      fileSubmitUploadParam(){
        let formData = this.filFormData;
        this.$refs.fileUploadRef.upload(formData);
      },
      confirmAfterSuccess(){
        this.$refs.escrowAgreementGrid.load({prodCode: this.selectRowData.prodCode,documentType:this.selectRowData.documentType,t8TruteeInfoId:this.selectRowData.t8TruteeInfoId});
      },
      addAudiopinion(params){
        /*表单校验*/
        let validateResult = this.$refs.applicationForm.validate();
        if (validateResult) {
          //是否选择线下审批
          if(params.approvalType == 2){
            this.saveAudiopinion(params);
          }else{
            this.httpUtil.comnUpdate({
              action: "T8PrintApplication.approvalAudiopinion",
              params: params,
              mask: false,
              successAlert:false
            }).then(data => {

              //判断是否进入了审批流
              if(data.returndata != undefined && data.returndata.data != undefined && data.returndata.data.processInstanceId != undefined){
                params.processInstanceId=data.returndata.data.processInstanceId;
                Tools.alert("流程开启成功");
              }else {
                Tools.error("流程开启失败")
              }
              this.saveAudiopinion(params);
            });
          }
          return true;
        }

      },
      addSealCilck(row,attachmentType, title) {
        this.formData.prodCode = row.prodCode;
        this.formData.prodName = row.prodName;
        this.formData.version = row.version;
        this.formData.id = row.id;
        this.formData.documentType=row.documentType;
        this.formData.t8TruteeInfoId=row.t8TruteeInfoId;
        this.formData.attachmentType = attachmentType;
        this.AttachmentpopupTitle = title
      },
      saveAudiopinion(params){
        this.httpUtil.comnUpdate({
          action: "T8PrintApplication.addAudiopinion",
          params: params,
          mask: false,
          successAlert:false
        }).then(data => {

          //关闭当前弹出窗口
          this.$refs.applicationPopup.close();
          this.$refs.escrowAgreementGrid.load({prodCode: this.selectRowData.prodCode,documentType:this.selectRowData.documentType,t8TruteeInfoId:this.selectRowData.t8TruteeInfoId});
        });
      },
      checkCount(data){
        this.httpUtil.comnQuery({
          action: "T8ProdDocumentVersion.getNewestT8ProdDocumentVersion",
          params: {prodCode: this.formData.prodCode,documentType: this.formData.documentType}
        }).then(data => {
          this.formData.version = data.rows[0].version;
        }).catch({
        });
      },
      selectRow(row, column, event){
        const _this = this;
        _this.selectRowData = assign({}, row);
        var documentType='10002,20002,30002,40002,50002,60002,70002,10102';
        this.httpUtil.comnQuery({
          action: 'T8ProdDocumentVersion.getNewestT8ProdDocumentVersion',
          params: {
            prodCode: _this.selectRowData.prodCode,
            documentType:documentType
          }
        }).then(data => {
            this.$nextTick(() => {
              let version = data.rows[0].version;
              //console.log(version);
              let array = version.split(".");
              let numbers = array[1];
              numbers = numbers-1;
              //this.$set(this, "lastVersion", array[0]+"."+numbers),
              this.lastVersion = array[0]+"."+numbers;
              this.$refs.escrowAgreementGrid.load({prodCode: _this.selectRowData.prodCode,documentType:documentType,t8TruteeInfoId: _this.selectRowData.t8TruteeInfoId});
            })
        });
      },
      loadAttachmentInfo(){
        this.$refs.downloadAgencyAgreementGrid.load({
          t8TruteeInfoId: this.selectRowData.t8TruteeInfoId,
          attachmentType: '2,6',
          prodCode: this.selectRowData.prodCode
        })
      },
      loadApproveInfo(){
        //console.log("this.tempId=:>",this.tempId);
        this.$refs.getAgencyAgreementGrid.load({
          t8ProdDocumentVersionId: this.tempId,
        })
      },
      auditBefore(rowsColumns,auditType){
        this.formData.t8ProdDocumentVersionId = rowsColumns.id
        this.formData.prodCode = rowsColumns.prodCode
        this.formData.auditType = auditType
        //查询审批意见
        this.httpUtil.comnQuery({
          action: "EscrowAgreement.findAudiopinion",
          params: {auditType: this.formData.auditType, prodCode: this.formData.prodCode}
        }).then(data => {
          this.queryList = data.rows
        }).catch({})
        this.formData.auditOpinion = ''
      },
      getTempId(row){
        this.tempId=row.id;
        //console.log("this.tempId=:>>>",this.tempId);
      },
      submitUploadParam() {
        let formData = this.formData;
        this.$refs.uploadRef.upload(formData);
      },
      onSubmitError() {
        this.$refs.uploadRef.doReset();
        this.$refs.submitBtn.setIconStyle(1, []);
      },
      onSubmitSuccess() {
        this.$refs.uploadRef.doReset();
        this.$refs.addForm.reset();
        this.$refs.addPopup.close();
        this.$refs.escrowAgreementGrid.load({prodCode: this.selectRowData.prodCode,documentType:this.selectRowData.documentType,t8TruteeInfoId: this.selectRowData.t8TruteeInfoId});
      },
      save(){
        var _this = this
        this.httpUtil.ajax({
          url: '/server/form/PmsApp/onlineEdit/saveProdDocumentVersionWordValue.json',
          params: {onlineEditData: JSON.stringify(this.onlineEditData)},
          successAlert: true
        }).then(res=>{
          this.$refs.onlineEditPopup.close();
          this.$refs.escrowAgreementGrid.load({
            prodCode: _this.selectRowData.prodCode,
            documentType:_this.selectRowData.documentType,
            t8TruteeInfoId: _this.selectRowData.t8TruteeInfoId
          })
        })
      },
      itemChange(value,key){
        document.getElementById("onlineEdit").contentWindow.document.querySelectorAll("span[name='v_"+key+"']").forEach(item=>{
          var reg = new RegExp( "\n" , "g" );
          var reg1 = new RegExp( " " , "g" );
          //换行符替换
          value = value.replace(reg, "<br/>");
          /*使用半角空格替换java的空格*/
          value = value.replace(reg1, "&ensp;");
          item.innerHTML=value
        })
      },
      onlineEditHandler(value){
        this.httpUtil.comnQuery({
          action: 'T8OnlineWordValue.getT8OnlineWordValueList',
          params: {
            t8ProdDocumentVersionId: value.id,
          }
        }).then(data => {
          if (data.rows.length > 0) {
            this.viewUrl = data.rows[0].viewUrl;
            this.onlineEditData = data.rows;
            setTimeout(() => {
              for (let i = 0; i < this.onlineEditData.length; i++) {
                let data = this.onlineEditData[i];
                document.getElementById("onlineEdit").contentWindow.document.querySelectorAll("span[name='v_" + data.wordKey + "']").forEach(item => {
                  var val = data.wordValue;
                  if (val != null && val.trim() !='' && val != 'null'){
                    //将java换行符替换成html换行符
                    //val = val.replaceAll("\n","<br/>");
                    /*使用半角空格替换java的空格*/
                    //val = val.replaceAll(" ", "&ensp;");
                    var reg = new RegExp( "\n" , "g" );
                    var reg1 = new RegExp( " " , "g" );
                    val = val.replace(reg, "<br/>");
                    val = val.replace(reg1, "&ensp;");
                    item.innerHTML = val;
                  } else{
                    item.innerHTML = data.wordComment;
                  }
                })
              }
            }, 3000)
          }
        });
      },
      closePopup(){
        this.$refs.onlineEditPopup.close()
      },
    }
  }
</script>


<style lang="scss" scoped>
  .edit{
    display: flex;
    flex-direction: row;
    width: 100%;
    height: 600px;
    .word{
      width: 70%;
      iframe{
        width: 100%;
        height: 100%;
      }
    }
    .form{
      padding-left: 20px;
      width: 40%;
      overflow-y:auto;
      .form-item{
        display: flex;
        align-items: center;
        margin-bottom: 10px;
        .form-item-span{
          margin-right: 5px;
          width: 100px;
          text-align: left;
        }
        .k-field-text{
          margin-left: 5px;
          width: 300px;
          height: 30px;
        }
      }
    }
  }
</style>
