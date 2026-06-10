<template>
  <div>
    <template>
      <md-card class="box-card" style="overflow: visible;position: unset">
        <md-card-header class="md-card-header-text md-card-header-green" style="margin-right: 0;">
          <div class="search-header">
            <div class="card-icon" :style="iconStyle">
              <md-icon md-src="/static/svg/form.svg"></md-icon>
            </div>
            <div>
              <i class="el-icon-d-caret" @click="show"></i>
            </div>
          </div>
        </md-card-header>

        <div slot="header" class="clearfix" style="text-align:right">

        </div>
        <div class="show-form" id="show-form">
          <k-form ref="searchForm" :data-col="0">
            <slot></slot>
          </k-form>
        </div>

        <div class="k-form-search-footer" style="width:100%;text-align: center;">
          <k-btn slot="button" class="btn-custom-primary" style="width: 130px" data-functype="SUBMIT"
                 data-action="T8ProdManualVersion.generateProdManualVersionByProdCode" :data-after-success="changeVersion"
                 v-show="showGenerate"
                 :data-model="this.prodManualData" :data-confirm="true"
                 :data-disabled="this.prodManualData.hasTemplate === '0'"
                 v-if="global.getProdIfUser(this.prodInfoId)&&
               global.isShowAuthorityButton('T8ProdManualVersion.generateProdManualVersionByProdCode')">
            <md-icon md-src="/static/svg/add.svg"/>
            生成产品说明书
          </k-btn>

          <k-btn slot="button" class="btn-custom-primary" style="width: 130px" data-functype="POPUP" :data-handler="setFileParams"
                 v-if="global.isShowAuthorityButton('T8ProdManualVersion.addT8ProdManual')"
                 data-target="filePopup">
            <md-icon md-src="/static/svg/add.svg"/>
            上传产品说明书
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="PAGE" data-target="/main/pms/prodDocument/prodManual">
            返回
          </k-btn>
        </div>
      </md-card>
    </template>

    <k-grid ref="prodManualGrid"
            data-action="T8ProdManualVersion.findT8ProdDocumentVersionByProdCodes"
            :data-before-load="beforePopupLoad"
            @data-row-select="selectPrintTemp"
            data-operate-column="true"
            :data-display="false">
      <k-grid-column data-align="center" data-header="版本id" data-name="id"/>
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="文件名"  data-name="fileName"/>
      <k-grid-column data-align="center" data-header="文档版本" data-name="version"/>
      <k-grid-column data-align="center" data-header="文档类型" data-name="documentName"/>
      <k-grid-column data-align="center" data-header="文档类型" data-name="documentType" :data-hidden="true"/>
      <k-grid-column data-align="center" data-header="是否模板文档" data-dict="1yes0no" data-name="isTemplateFile"/>
      <k-grid-column data-align="center" data-header="说明书状态" data-dict="doc_confirm_status" data-name="confirmStatus"/>
      <k-grid-column data-align="center" data-header="创建人" data-name="createUserName"/>
      <k-grid-column data-align="center" data-header="创建时间" data-name="createDate" data-render="renderDateTimeCreate"/>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" :data-download-name="downFileName(scope.row.row)"
               data-descript="下载产品文档" data-functype="DOWNLOAD" data-size="small"
               data-url="/download/server/PmsApp/prodDocument/downloadOnlineEditT8ProdDocumentVersion.json" v-model="scope.row.row">
          <md-icon>cloud_download</md-icon>
        </k-btn>
        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple" data-descript="在线编辑"  :data-disabled="scope.row.row.isTemplateFile === '0'"
                 data-size="small" @click="onlineEditHandler(scope.row.row)"  v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
               global.isShowAuthorityButton('T8ProdManualVersion.getT8OnlineWordValueList')"
                 data-functype="POPUP" data-target="onlineEditPopup" v-model="scope.row.row" v-show="showEditOnline">
            <md-icon>edit</md-icon>
          </k-btn>
        </div>
        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple" v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)
          &&global.isShowAuthorityButton('T8ProdManualVersion.updateConfirm')"
                 :data-handler="setConfirmStatus" data-descript="将产品说明书变更为已确认" data-functype="SUBMIT" data-size="small"
                 data-action="T8ProdManualVersion.updateConfirm" data-target="prodManualGrid"
                 :data-disabled="scope.row.row.confirmStatus!='0'"

                 v-model="scope.row.row" :data-confirm="true" v-show="showConfirm">
            <md-icon>done</md-icon>
          </k-btn>
        </div>
        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple"
                 v-model="scope.row.row" data-descript="上传法审结果" data-functype="POPUP" data-size="small"
                 :data-disabled="scope.row.row.confirmStatus=='0'"  v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
                 global.isShowAuthorityButton('T8ProdManualVersion.uploadLegaResults')"
                 data-target="addPopup" :data-handler="addLegalTrialHandler" v-show="showUploadLaw">
            <md-icon>backup</md-icon>
          </k-btn>
        </div>
        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple"
                 data-descript="下载附件" data-functype="POPUP" data-target="downListPOPUP" v-model="scope.row.row">
            <md-icon>weekend</md-icon>
          </k-btn>
        </div>

<!--        <k-btn class="md-info md-just-icon md-simple" :data-disabled="checkInitiateLegalTrialStatus(scope.row.row)"-->
<!--               data-descript="发起法审流程" data-target="prodManualGrid"-->
<!--               data-functype="SUBMIT"-->
<!--               :data-confirm="true" data-action="T8ProdDocumentVersion.updateTaskStatus"-->
<!--               data-size="small" v-model="scope.row.row">-->
<!--          <md-icon>near_me</md-icon>-->
<!--        </k-btn>-->
        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple" :data-disabled="scope.row.row.confirmStatus == '0' || scope.row.row.confirmStatus == '3'"
                 v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)"
                 :data-handler = "setConfirmStatusMa" data-descript="定稿" data-functype="SUBMIT" data-size="small"
                 data-action="T8ProdManualVersion.updateConfirmStatus" data-target="prodManualGrid"
                 v-model="scope.row.row" :data-confirm="true" v-show="showFinalize">
            <md-icon>offline_pin</md-icon>
          </k-btn>
        </div>

        <div style="display:inline-block;">
        <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
               data-action="T8ProdManualVersion.deleteProdManualById" data-size="mini"
               data-type="danger" data-target="prodManualGrid" :data-confirm="true"
               data-descript="删除"
               v-if="global.getUswerIfAdmin(scope.row.row.t8ProdInfoId)">
          <md-icon>close</md-icon>
        </k-btn>
        </div>


      </template>
    </k-grid>

    <k-popup ref="filePopup" title="上传产品说明书">
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
        <k-form-item label="产品说明书" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="fileUploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onFileSubmitError" :data-success="onFileSubmitSuccess"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/prodDocument/prodManualUpload.json">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="prodManualGrid" ref="fileSubmitBtn"
                 data-from="fileForm" :data-model="formData" :data-handler="fileSubmitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="addPopup" :title="addPopupTitle">
      <k-form ref="addForm" data-ui="element">
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prodCode" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodName" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="提交状态" v-show="false">
          <k-field-text v-model="formData.confirmStatus" />
        </k-form-item>
        <k-form-item label="id" v-show="false">
          <k-field-text v-model="formData.id" />
        </k-form-item>

        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onSubmitError" :data-success="onSubmitSuccess"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/prodDocument/uploadT8ProdDocumentVersion.json">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="prodManualGrid" ref="submitBtn"
                 data-from="addForm" :data-model="formData" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

   <!--附件下载弹出框-->
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
                 data-action="T8ProdManualVersion.deleteFile"
                 v-if="global.isShowAuthorityButton('T8ProdManualVersion.deleteFile')">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>
    </k-popup>

    <!--在线编辑弹出框-->
    <k-popup ref="onlineEditPopup" data-width="80%" :data-dialog-drag="true" class="onLineClass"
             style="margin-left:10%;">
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
              <k-field-text v-show="item.isDisabled=='1'"  style="width: 130%" inputType="textarea" :ref="item.wordKey" @input="itemChange($event,item.wordKey)" v-model="item.wordValue"/>
              <k-field-text v-show="item.isDisabled=='0'"  style="width: 130%" inputType="textarea" :rows="1" :data-disabled="true" :value="item.wordValue"/>
            </k-form-item>
          </k-form>
        </div>
      </div>
    </k-popup>

  </div>
</template>

<script>
  import KFieldUpload from "../../../components/k-element/k-field-upload/k-field-upload";
  import {assign} from "lodash";
  import KFieldDisplay from "../../../components/k-element/k-field-display/k-field-display";
  import KFormItem from "../../../components/k-element/k-from/k-form-item";
  import Tools from "@/utils/tools";
  export default {
    name: "prodManual",
    components: {KFormItem, KFieldDisplay, KFieldUpload},
    mounted() {
      window.addEventListener('message', (e)=>{
        if(e.data.key){
          let refName=e.data.key
          this.$refs[refName][0].focus()
        }
      })
    },
    data() {
      return {
        prodManualData:{},
        documentType:'',
        prodCode:'',
        prodSearchParam:{
          prodCode:''
        },
        formData:{
          prodCode:'',
          prodName:'',
          documentType:'',
          version:'',
          remark:'',
          docType:''
        },
        filFormData:{
          prodCode:'',
          prodName:'',
          documentType:'',
          version:'',
          isTemplateFile:''
        },
        prodInfoId:'',
        lastVersion:'',
        TopNewVersion: '',
        addPopupTitle: '',
        onlineEditData: {},
        viewUrl: '',
        showGenerate: true,//是否显示生成按钮
        showEditOnline: true,//是否显示在线编辑按钮
        showConfirm: true,//是否显示确认按钮
        showUploadLaw: true,//是否显示上传法审版按钮
        showFinalize: true,//是否显示定稿按钮
        onlineParams: {},//走审批用
        showUploadManual: true,//是否显示上传说明书按钮
      }
    },
    created() {
      if(this.$route.query.prodManualData.id == null || this.$route.query.prodManualData.id == undefined){
        // var prodManualData = localStorage.getItem("prodManualData");
        // if(prodManualData){
        //   this.prodManualData = JSON.parse(prodManualData);
        //   this.prodCode = this.prodManualData.prodCode;
        //   this.documentType = this.prodManualData.documentType;
        //   this.lastVersion = this.prodManualData.lastVersion;
        //   this.prodInfoId = this.prodManualData.id;
        //   this.getNewVersion();
        //   this.$nextTick(() => {
        //     this.$refs.prodManualGrid.load({prodCode: this.prodCode,documentType:this.documentType});
        //   })
        //   console.log("缓存",JSON.parse(prodManualData))
        // }
      } else {
        //不刷新二级页面，每一次只走这里
        this.prodManualData = this.$route.query.prodManualData
        this.prodCode = this.$route.query.prodManualData.prodCode;
        this.documentType = this.$route.query.prodManualData.documentType;
        this.lastVersion = this.$route.query.prodManualData.lastVersion;
        this.prodInfoId = this.$route.query.prodManualData.id;
        this.getNewVersion();
        this.$nextTick(()=>{
          this.$refs.prodManualGrid.load({prodCode: this.prodCode,documentType:this.documentType});
        });
        console.log("路由",this.$route.query.prodManualData)
      }
    },
    activated() {
      //除了刷新走created和activated,以后每一次路由走activated(一级列表不关闭)
      if(this.$route.query.prodManualData.id == null || this.$route.query.prodManualData.id == undefined){
        var prodManualData = localStorage.getItem("prodManualData");
        if(prodManualData){
          this.prodManualData = JSON.parse(prodManualData);
          this.prodCode = this.prodManualData.prodCode;
          this.documentType = this.prodManualData.documentType;
          this.lastVersion = this.prodManualData.lastVersion;
          this.prodInfoId = this.prodManualData.id;
          this.getNewVersion();
          this.$nextTick(() => {
            this.$refs.prodManualGrid.load({prodCode: this.prodCode,documentType:this.documentType});
          })
          // console.log("缓存",JSON.parse(prodManualData))
        }
      } else {
        //刷新之后再路由进来，会走activated
        this.prodManualData = this.$route.query.prodManualData
        this.prodCode = this.$route.query.prodManualData.prodCode;
        this.documentType = this.$route.query.prodManualData.documentType;
        this.lastVersion = this.$route.query.prodManualData.lastVersion;
        this.prodInfoId = this.$route.query.prodManualData.id;
        this.getNewVersion();
        this.$nextTick(()=>{
          this.$refs.prodManualGrid.load({prodCode: this.prodCode,documentType:this.documentType});
        });
        console.log("刷新后路由",this.$route.query.prodManualData)
      }
    },
    methods: {
      changeVersion(data) {
        this.httpUtil.comnQuery({
          action: 'T8ProdDocumentVersion.getNewestT8ProdDocumentVersion',
          params: {
            prodCode: this.prodCode,
            documentType: this.documentType
          }
        }).then(data => {
          this.$nextTick(() => {
            let version = data.rows[0].version;
            let array = version.split(".");
            let numbers = array[1];
            numbers = numbers-1;
            this.lastVersion = array[0]+"."+numbers;
            this.$refs.prodManualGrid.load({prodCode: this.prodCode,documentType:this.documentType});
          })
        });
      },
      getNewVersion(){
        var documentType='10001,20001,30001,40001,50001,60001,70001,10101';
        this.httpUtil.comnQuery({
          action: 'T8ProdDocumentVersion.getNewestT8ProdDocumentVersion',
          params: {
            prodCode: this.prodCode,
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
          })
        });
      },
      selectPrintTemp(row, column, event) {
        const _this = this
        _this.selectRowData = assign({}, row)
        this.$refs.prodManualGrid.load({prodCode: row.prodCode,documentType: row.documentType});
      },
      beforePopupLoad(params) {
        params.prodCode = this.prodCode ;
        params.documentType = this.documentType;
        return params;
      },
      renderDateTimeCreate(row) {
        return Tools.formatDateTime(row.createDate, row.createTime);
      },
      checkVersion(version){
        if(version == this.lastVersion){
          return true;
        }else{
          return false;
        }
      },
      setConfirmStatus(params){
        params.confirmStatus = '1';
      },
      setConfirmStatusMa(params){
        params.confirmStatus = '3';
      },
      setFileParams() {
        this.filFormData.documentType = this.$route.query.prodManualData.documentType
        this.filFormData.prodCode = this.$route.query.prodManualData.prodCode
        this.filFormData.prodName = this.$route.query.prodManualData.prodName
      },
      downFileName(rows) {
          return rows.fileName
      },
      onFileSubmitError(){
        this.$refs.fileUploadRef.doReset();
        this.$refs.fileSubmitBtn.setIconStyle(1, []);
      },
      onFileSubmitSuccess() {
        this.getNewVersion();
        this.$refs.fileUploadRef.doReset();
        this.$refs.fileForm.reset();
        this.$refs.filePopup.close();
        this.$refs.prodManualGrid.load({prodCode: this.prodCode,documentType: this.documentType})
      },
      fileSubmitUploadParam(){
        let formData = this.filFormData;
        this.$refs.fileUploadRef.upload(formData);
      },
      onSubmitError() {
        this.$refs.uploadRef.doReset();
        this.$refs.submitBtn.setIconStyle(1, []);
      },
      onSubmitSuccess() {
        this.$refs.uploadRef.doReset();
        this.$refs.addForm.reset();
        this.$refs.addPopup.close();
        this.$refs.prodManualGrid.load({prodCode: this.selectRowData.prodCode,documentType:this.selectRowData.documentType});
      },
      submitUploadParam() {
        let formData = this.formData;
        this.$refs.uploadRef.upload(formData);
      },
      addLegalTrialHandler(value){
        this.formData.id = value.id;
        this.formData.prodCode = value.prodCode;
        this.formData.prodName = value.prodName;
        this.formData.version = value.version;
        this.formData.docType = '2';
        this.formData.documentType = value.documentType;
        this.addPopupTitle='上传法审结果';
        this.formData.confirmStatus = '2';
      },
      loadAttachmentInfo(){
        this.$refs.downloadAgencyAgreementGrid.load({
          prodCode: this.selectRowData.prodCode,
          attachmentType: '4'
        })
      },
      checkInitiateLegalTrialStatus(value){
        if (value.confirmStatus == '0'){
          return true;
        }
        return value.taskStatus != 1;
      },

      save(val){
        //axin  修改保存方式
        const _this = this
        val = this.onlineParams;
        this.$set(val,'viewUrl',this.viewUrl);
        val['onlineEditData'] = JSON.stringify(this.onlineEditData);
        this.httpUtil.comnUpdate({
          action: "T8ProdManualVersion.prodManualWordValue",
          params: val,
          mask: false,
          successAlert:false
        }).then(res => {
          this.$refs.onlineEditPopup.close();
          this.$refs.prodManualGrid.load({prodCode: _this.selectRowData.prodCode,documentType:_this.selectRowData.documentType});
        });

        // this.httpUtil.ajax({
        //   url: '/server/form/PmsApp/onlineEdit/saveProdDocumentVersionWordValue.json',
        //   params: {onlineEditData: JSON.stringify(this.onlineEditData)},
        //   successAlert: true
        // }).then(res=>{
        //   _this.$refs.onlineEditPopup.close();
        //   _this.$refs.prodManualGrid.load({prodCode: _this.selectRowData.prodCode,documentType:_this.selectRowData.documentType});
        // })
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
          this.onlineParams = value;
          this.httpUtil.comnQuery({
            action: 'T8OnlineWordValue.getT8OnlineWordValueList',
            params: {
              t8ProdDocumentVersionId: value.id,
              prodCode: value.prodCode,
              documentType: value.documentType
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
      show() {
        let e = document.getElementById('show-form')
        if (this.extends) {
          e.style.display = "none"
        } else {
          e.style.display = ""
        }
        this.extends = !this.extends
      },
    },
    computed: {
      iconStyle() {
        let iconStyle = {};
        iconStyle.background = this.$store.state.system.cardBackground
        return iconStyle;
      }
    },
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

/*  .onLineClass ::v-deep .el-dialog {
    position: fixed !important;  // 浮动
    z-index: 999999 !important;
  }*/

</style>
