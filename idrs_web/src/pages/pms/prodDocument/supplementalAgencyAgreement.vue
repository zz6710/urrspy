<template>
  <div>
    <k-form-search-customize data-target="prodDocInfoGrid" v-model="queryParam">
      <k-form-item label="产品代码">
        <k-field-select v-model="queryParam.prodCode"  data-action="T8ProdInfo.findT8ProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode" ></k-field-select>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="queryParam.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="成立日期(起)">
        <k-field-date v-model="queryParam.queryStartDate" :data-max-value="queryParam.queryEndDate"/>
      </k-form-item>
      <k-form-item label="成立日期(到)">
        <k-field-date v-model="queryParam.queryEndDate" :data-min-value="queryParam.queryStartDate"/>
      </k-form-item>
      <k-form-item label="是否存在补充代销协议" v-show="true" data-input-width="164px" data-label-width="180px">
        <k-field-select v-model="queryParam.isHave" data-dict="t8_prod_isok"/>
      </k-form-item>
      <k-form-item label="销售商">
        <k-field-select v-model="queryParam.distributorCode" data-action="T8Dict.findTaDistributorInfos"
                        data-display-field="distributorName"  data-value-field="distributorCode"/>
      </k-form-item>
      <k-form-item label="代码回收">
        <k-field-select v-model="queryParam.isRecycleCode" data-dict="1yes0no"></k-field-select>
      </k-form-item>
      <k-btn slot="button" class="btn-custom-primary" data-functype="SUBMIT" ref="batchGenrateDocButton"
             v-show="showBatchGenerate"  :data-handler="batchGenrateDoc" style="width: 120px;"
             v-if="global.isShowAuthorityButton('SupplementalAgencyAgreement.batchGenerateDistributorDoc')">
        <md-icon md-src="/static/svg/add.svg" />批量生成文档</k-btn>
    </k-form-search-customize>

    <k-grid :data-checkbox="true" data-checkbox-id="prodCode" ref="prodDocInfoGrid" data-action="SupplementalAgencyAgreement.findDocInfo1"
            @init="(grid)=>{this.$kgrid = grid}" @data-row-select="selectRow">
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="销售商名称" data-name="distributorName"/>
      <k-grid-column data-align="center" data-header="文档描述" data-name="docDesc" :data-hidden="true"/>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-functype="SUBMIT"
               data-target="supplementalAgencyAgreementGrid" data-descript="生成补充代销协议" data-size="small"
               data-action="SupplementalAgencyAgreement.generateAgencyAgreement" :data-after-success="changeVersion"
               v-model="scope.row.row" :data-confirm="true"
               v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
               global.isShowAuthorityButton('SupplementalAgencyAgreement.generateAgencyAgreement')"
               v-show="showGenerate"
               :data-disabled="scope.row.row.hasTemplate === '0'">
          <md-icon>add_circle</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple"  v-model="scope.row.row" data-descript="上传补充代销协议"
               data-functype="POPUP" data-size="small" @click="setFileParams(scope.row.row)" data-target="filePopup"
               v-if="global.isShowAuthorityButton('SupplementalAgencyAgreement.addT8ProdDocumentVersion')">
          <md-icon>backup</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-grid ref="supplementalAgencyAgreementGrid" @data-row-select="selectVersionRow" :data-autoload="false"
            data-action="T8ProdDocumentVersion.findEscrowAgreementByProdCode">
      <k-grid-column data-align="center" data-hidden="true" data-header="文档类型" data-name="documentType"/>
      <k-grid-column data-align="center" data-header="版本id" data-name="id" data-hidden="true"/>
      <k-grid-column data-align="center" data-header="文件名" data-name="fileName" data-hidden="true"/>
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="文件名" data-name="fileName"/>
      <k-grid-column data-align="center" data-header="销售商" data-name="distributorName"/>
      <k-grid-column data-align="center" data-header="文档版本" data-name="version"/>
      <k-grid-column data-align="center" data-header="是否模板文档" data-dict="1yes0no" data-name="isTemplateFile"/>
      <k-grid-column data-align="center" data-header="用印审批状态"  data-name="approveStatus" data-dict="t8_process_status"/>
      <k-grid-column data-align="center" data-header="文档状态" data-name="documentStatus" data-dict="t8_document_status"/>
      <template slot="operate" slot-scope="scope">
        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple" :data-disabled="scope.row.row.documentStatus!=1"
                 ref="statusConfirm"
                 data-descript="确认" data-functype="SUBMIT" data-size="small"
                 data-action="SupplementalAgencyAgreement.udpateDocumentStatus" :data-after-success="confirmAfterSuccess"
                 v-model="scope.row.row" :data-confirm="true" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
                 global.isShowAuthorityButton('SupplementalAgencyAgreement.udpateDocumentStatus')"
                 v-show="showConfirm">
            <md-icon>done</md-icon>
          </k-btn>
        </div>
        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple" data-descript="在线编辑" data-size="small"
                 @click="onlineEditHandler(scope.row.row)" data-functype="POPUP"  :data-disabled="scope.row.row.isTemplateFile === '0'"
                 data-target="onlineEditPopup" v-model="scope.row.row" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
                 global.isShowAuthorityButton('SupplementalAgencyAgreement.getT8OnlineWordValueList')"
                 v-show=" showEditOnline">
            <md-icon>edit</md-icon>
          </k-btn>
        </div>
        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple" data-descript="上传法审文件" :data-disabled="scope.row.row.documentStatus=='1'"
                 data-functype="POPUP" data-size="mini" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
                 global.isShowAuthorityButton('SupplementalAgencyAgreement.uploadLegalDocuments')"
                 data-target="annexPopup" @click="annexBefore(scope.row.row, '上传法审文件', '1')" v-show="showUploadLaw">
            <md-icon >cloud_upload</md-icon>
          </k-btn>
        </div>
        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple" data-descript="上传用印扫描件" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
                 global.isShowAuthorityButton('SupplementalAgencyAgreement.uploadScannedCopy')"
                 :data-disabled="scope.row.row.documentStatus=='1'" data-functype="POPUP" data-size="mini"
                 data-target="annexPopup1" @click="annexBefore(scope.row.row, '上传用印扫描件', '5')" v-show="showUploadSeal">
            <md-icon >cloud_upload</md-icon>
          </k-btn>
        </div>
        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple"   v-model="scope.row.row" data-descript="用印申请" data-functype="POPUP" data-size="small"
                 @click="applicationData(scope.row.row)"
                 v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)" v-show="showApplySeal&&
                 global.isShowAuthorityButton('SupplementalAgencyAgreement.approvalAudiopinion')">
            <md-icon>create_new_folder</md-icon>
          </k-btn>
        </div>

        <k-btn class="md-info md-just-icon md-simple" :data-download-name="downFileName(scope.row.row)"
               data-descript="下载补充代销协议" data-functype="DOWNLOAD" data-size="small"
               data-url="/download/server/PmsApp/prodDocument/downloadOnlineEditT8ProdDocumentVersion.json" v-model="scope.row.row">
          <md-icon>cloud_download</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple"
               data-descript="下载附件" data-functype="POPUP" data-target="downListPOPUP" v-model="scope.row.row" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)">
          <md-icon>weekend</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <!--上传补充代销协议-->
    <k-popup ref="filePopup" title="上传补充代销协议">
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
        <k-form-item label="销售商代码" v-show="false">
          <k-field-text v-model="filFormData.distributorCode"/>
        </k-form-item>
        <k-form-item label="补充代销协议" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="fileUploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onFileSubmitError" :data-success="onFileSubmitSuccess"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/SupplementalAgencyAgreemen/uploadAgreement.json">
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

    <!-- 上传附件  -->
    <k-popup ref="annexPopup" :data-title="annexPopupTitle">
      <k-form ref="annexForm" :data-col="2">
        <k-form-item style="display:none" label="id">
          <k-field-text v-model="uploadData.id" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item >
          <k-field-upload label="附件信息" data-type="file" ref="uploadonAnnexRef" :data-multiple="true" :data-limit=10
                          :data-error="onAnnexSubmitError" :dataChange="onUploadChange"
                          :dataHttpRequest="httpRequest"
                          :data-auto-upload="false">
          </k-field-upload>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-target="prodInfoGrid" ref="submitBtn"
                 data-from="minutesOfMeetingForm" :data-model="uploadData" @click="batchSubmit">
            <span v-show="showSubmitBtn">确定</span>
            <i v-show="!showSubmitBtn" class="el-icon-loading"/>
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!-- 上传附件  -->
    <k-popup ref="annexPopup1" :data-title="annexPopupTitle">
      <k-form ref="annexForm1" :data-col="2">
        <k-form-item style="display:none" label="id">
          <k-field-text v-model="uploadData.id" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item >
          <k-field-upload label="附件信息" data-accept=".pdf" data-type="file" ref="uploadonAnnexRef" :data-multiple="true" :data-limit=10
                          :data-error="onAnnexSubmitError" :dataChange="onUploadChange"
                          :dataHttpRequest="httpRequest"
                          :data-auto-upload="false">
          </k-field-upload>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-target="prodInfoGrid" ref="submitBtn"
                 data-from="minutesOfMeetingForm" :data-model="uploadData" @click="batchSubmit1">
            <span v-show="showSubmitBtn">确定</span>
            <i v-show="!showSubmitBtn" class="el-icon-loading"/>
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <!--用印申请弹出框-->
    <k-popup ref="applicationPopup" data-title="用印申请">
      <k-form ref="applicationForm" :data-col="2" >
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
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"
                 data-from="applicationForm" :data-model="application" :data-handler="addAudiopinion">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

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
          <k-btn class="md-danger md-just-icon md-simple"  data-descript="删除附件" data-functype="SUBMIT"  data-confirm data-type="danger"
                 data-target="downloadAgencyAgreementGrid"
                 data-action="SupplementalAgencyAgreement.deleteFile"
                 v-if="global.isShowAuthorityButton('SupplementalAgencyAgreement.deleteFile')">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
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
    name: "supplementalAgency",
    data() {
      return {
        queryParam: {
          prodCode: ''
        },
        formData: {
          prodCode: '',
          prodName: '',
          version: '',
          remark:'',
          docType: '',
          documentType:'',
          distributorCode: ''
        },
        application: {},
        selectRowData:{},
        selectVersionData: {},
        fileList:[],
        fileData:'',
        uploadData:{
          id:'',
          type: '',
        },
        filFormData:{
          prodCode:'',
          prodName:'',
          documentType:'',
          version:'',
          isTemplateFile:'',
          distributorCode:''
        },
        toDate:'',
        lastVersion:'',
        showSubmitBtn:true,
        onlineEditData:{},
        viewUrl: '',
        annexPopupTitle: '',
        attachmentType: '',
        showBatchGenerate:true,//是否显示批量生成按钮
        showGenerate:true,//是否显示生成补充代销协议按钮
        showConfirm:true,//是否显示确认按钮
        showEditOnline:true,//是否显示在线编辑按钮
        showUploadLaw:true,//是否显示上传法审版按钮
        showUploadSeal:true,//是否显示上传用印按钮
        showApplySeal:true,//是否显示用印申请按钮
      }
    },
    created() {
      this.global.getProdUser('');
      let prod_Code = this.$route.query.prod_code;
      this.$nextTick(()=>{
        //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
        this.global.getHideButtons(this);
        //rennannan 运营导航传过来产品代码
        if (prod_Code != '' && prod_Code != undefined) {
          this.queryParam.prodCode = prod_Code;
          this.$refs.prodDocInfoGrid.load({prodCode: prod_Code});

        }
        //axin
        let prodCode = this.$route.query.prodCode;
        if (prodCode) {
          this.$refs.prodDocInfoGrid.load({prodCode: prodCode});
          this.queryParam.prodCode = prodCode;
        }
      });
    },
    mounted() {
      //axin 获取系统时间
      this.httpUtil.sysDate().then(res=>{
        if (res) {
          this.toDate = res;
        }
      })
      window.addEventListener('message', (e)=>{
        if(e.data.key){
          let refName=e.data.key
          this.$refs[refName][0].focus()
        }
      })
    },
    methods:{
      changeVersion(row){
        //console.log("data=>>>>>",row);
        this.httpUtil.comnQuery({
          action: 'T8ProdDocumentVersion.getNewestT8ProdDocumentVersion',
          params: {
            prodCode: row.returndata.prodCode,
            documentType:row.returndata.documentType
          }
        }).then(data => {
          this.$nextTick(() => {
            let version = data.rows[0].version;
            let array = version.split(".");
            let numbers = array[1];
            numbers = numbers-1;
            this.lastVersion = array[0]+"."+numbers;
            this.$refs.supplementalAgencyAgreementGrid.load({prodCode: row.returndata.prodCode,documentType:row.returndata.documentType,distributorCode:row.returndata.distributorCode});
          })
        });
      },
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
        this.$set(this.application,'printName',val.prodName+'-'+val.prodCode+' '+'补充代销协议用印申请');
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
        this.filFormData.distributorCode = rows.distributorCode
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
        /*提交成功后重新加载二级查询表单数据*/
        this.$refs.supplementalAgencyAgreementGrid.load({prodCode: this.selectRowData.prodCode,documentType:this.selectRowData.documentType,distributorCode:this.selectRowData.distributorCode});

      },
      addAudiopinion(params){
        /*表单校验*/
        let validateResult = this.$refs.applicationForm.validate();
        if (validateResult) {
          //是否选择线下审批
          if(params.approvalType == 2){
            this.saveAudiopinion(params);
          }else{
            // Tools.alert("提交中...");
            this.httpUtil.comnUpdate({
              action: "SupplementalAgencyAgreement.approvalAudiopinion",
              params: params,
              mask: false,
              successAlert:false
            }).then(data => {
              //判断是否进入了审批流
              if(data.returndata != undefined && data.returndata.data != undefined && data.returndata.data.processInstanceId != undefined){
                params.processInstanceId=data.returndata.data.processInstanceId;
                Tools.alert("流程开启成功");
              }
              this.saveAudiopinion(params);
            });
          }
          return true;
        }
      },
      annexBefore(rows,title,type) {
        this.annexPopupTitle = title;
        /*附件类型：1法审文件，5-用印扫描件*/
        this.attachmentType = type;
      },
      saveAudiopinion(params){
        this.httpUtil.comnUpdate({
          action: "SupplementalAgencyAgreement.addAudiopinion",
          params: params,
          mask: false,
          successAlert:false
        }).then(data => {
          //Tools.alert("流程开启成功");
          //关闭当前弹出窗口
          this.$refs.applicationPopup.close();
          //重新加载列表
          this.table_load(this);
        });
      },
      selectRow(row, column, event){
        //console.log(row);
        const _this = this;
        _this.selectRowData = assign({}, row);
        var documentType='10003,20003,30003,40003,50003,60003,70003,10103';
        this.httpUtil.comnQuery({
          action: 'T8ProdDocumentVersion.getNewestT8ProdDocumentVersion',
          params: {
            prodCode: _this.selectRowData.prodCode,
            documentType:documentType
          }
        }).then(data => {
          this.$nextTick(() => {
            let version = data.rows[0].version;
            let array = version.split(".");
            let numbers = array[1];
            numbers = numbers-1;
            //this.$set(this, "lastVersion", array[0]+"."+numbers),
            this.lastVersion = array[0]+"."+numbers;
            //this.table_load(_this);
            this.$refs.supplementalAgencyAgreementGrid.load({prodCode: _this.selectRowData.prodCode,documentType:documentType,distributorCode:_this.selectRowData.distributorCode});
          })
        });

      },
      table_load(_this){
        this.$refs.supplementalAgencyAgreementGrid.load({prodCode: _this.selectRowData.prodCode,documentType:documentType,distributorCode:_this.selectRowData.distributorCode});
      },
      batchGenrateDoc(){
        const _this = this
        const list = _this.$kgrid.getSelected();
        for (let i = 0; i < list.length; i++) {
          if (list[i].hasTemplate == '0') {
            Tools.alert('产品' + list[i].prodName + '未选择销售商' + list[i].distributorName + '的补充代销协议模板', "danger");
            return false;
          }
        }

        _this.$refs.batchGenrateDocButton.setIconStyle(0, []);
        /*if (list.length > 0) {*/
        this.httpUtil.comnUpdate({
          action: 'SupplementalAgencyAgreement.batchGenerateDistributorDoc',
          params: {list: JSON.stringify(list)},
          successAlert: true,
        }).then(data => {

          _this.$refs.batchGenrateDocButton.setIconStyle(1, []);
          _this.$kgrid.setSelected([]);
        });

      },
      selectVersionRow(row){
        const _this = this;
        _this.selectVersionData = assign({}, row);
      },
      httpRequest(file){
        this.fileData.append('files', file.file);
      },
      onUploadChange(file,fileList){
        this.fileList = fileList;
      },
      onAnnexSubmitError() {
        this.$refs.uploadonAnnexRef.doReset();
        this.showSubmitBtn = true;
      },
      batchSubmit(){
        let uploadData = this.selectVersionData;
        uploadData.attachmentType = this.attachmentType;
        this.showSubmitBtn = false;
        this.fileData = new FormData();
        this.$refs.uploadonAnnexRef.upload();
        this.fileData.append('params', JSON.stringify(uploadData));
        this.httpUtil.upload({
          url:"/upload-files/server/PmsApp/documentAttachment/upload.json",
          formData: this.fileData
        }).then(res=>{
          this.showSubmitBtn = true;
          Tools.alert(res.data.returnmsg)
          this.onSubmitAnnexSuccess()
        })
      },
      batchSubmit1(){
        let uploadData = this.selectVersionData;
        uploadData.attachmentType = this.attachmentType;
        this.showSubmitBtn = false;
        this.fileData = new FormData();
        this.$refs.uploadonAnnexRef.upload();
        this.fileData.append('params', JSON.stringify(uploadData));
        this.httpUtil.upload({
          url:"/upload-files/server/PmsApp/documentAttachment/upload.json",
          formData: this.fileData
        }).then(res=>{
          this.showSubmitBtn = true;
          Tools.alert(res.data.returnmsg)
          this.onSubmitAnnexSuccess()
        })
      },
      onSubmitAnnexSuccess() {
        this.$refs.uploadonAnnexRef.doReset();
        //this.$refs.annexForm.reset();
        //this.$refs.annexForm1.reset();
        this.$refs.annexPopup.close();
        this.$refs.annexPopup1.close();
        this.$refs.supplementalAgencyAgreementGrid.load({prodCode: this.selectRowData.prodCode,documentType:this.selectRowData.documentType,distributorCode:this.selectRowData.distributorCode});

      },
      loadAttachmentInfo(){
        this.$refs.downloadAgencyAgreementGrid.load({
          attachmentType: '1,5',
          prodCode: this.selectVersionData.prodCode,
          distributorCode: this.selectVersionData.distributorCode
        })
      },
      save(){
        const _this = this
        this.httpUtil.ajax({
          url: '/server/form/PmsApp/onlineEdit/saveProdDocumentVersionWordValue.json',
          params: {onlineEditData: JSON.stringify(this.onlineEditData)},
          successAlert: true
        }).then(res=>{
          this.$refs.onlineEditPopup.close();
          this.$refs.supplementalAgencyAgreementGrid.load({prodCode: _this.selectRowData.prodCode,documentType:_this.selectRowData.documentType,distributorCode:_this.selectRowData.distributorCode});

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
          action: 'SupplementalAgencyAgreement.getT8OnlineWordValueList',
          params: {
            t8ProdDocumentVersionId: value.id,
          }
        }).then(data => {
          if (data.rows.length > 0) {
            this.viewUrl = data.rows[0].viewUrl;
            this.onlineEditData = data.rows;
            setTimeout(() => {
              /*将数据库的值替换到html*/
              for (let i = 0; i < this.onlineEditData.length; i++) {
                let data = this.onlineEditData[i];
                document.getElementById("onlineEdit").contentWindow.document.querySelectorAll("span[name='v_" + data.wordKey + "']").forEach(item => {
                  var val = data.wordValue;
                  if (val != null && val.trim() !='' && val != 'null'){
                    //将java换行符替换成html换行符
                    //val = val.replaceAll("\n","<br/>");
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
