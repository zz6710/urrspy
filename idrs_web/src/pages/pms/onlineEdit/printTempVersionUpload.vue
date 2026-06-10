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

          <k-btn slot="button" class="btn-custom-primary" style="width: 130px" data-functype="POPUP" :data-handler="editHandler"
                 v-if="global.isShowAuthorityButton('PrintTemp.savePrintTempVersion')" data-target="editPrintTempVersionPopup">
            <md-icon md-src="/static/svg/add.svg"/>
            上传文档模板子模版
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="PAGE" data-target="/main/pms/onlineEdit/printTempUpload">
            返回
          </k-btn>
        </div>
      </md-card>
    </template>

    <k-grid ref="printTempVersionGrid" data-action="PrintTempVersion.getPrintTempVersionByTempId"
            :data-before-load="beforePopupLoad"
            data-operate-column="true"
            :data-display="false">
      <k-grid-column data-align="center" data-header="版本id" data-name="id" data-hidden="true" />
      <k-grid-column data-align="center" data-header="对应模板id" data-name="t8PrintTempId" data-hidden="true" />
      <k-grid-column data-align="center" data-header="文档名称" data-name="tempName" data-width="400"/>
      <k-grid-column data-align="center" data-header="文档版本编号" data-name="version"  data-width="100"/>
      <k-grid-column data-align="center" data-header="风险数目" data-name="riskNum"/>
      <k-grid-column data-align="center" data-header="备注" data-name="remark" data-width="400"/>
      <k-grid-column data-align="center" data-header="创建人" data-name="createUserName"/>
      <k-grid-column data-align="center" data-header="生效状态" data-name="status" data-dict="t8_print_temp_version_status"/>
      <k-grid-column data-align="center" data-header="生效日期" data-name="effectiveDate" data-width="180"/>
      <k-grid-column data-align="center" data-header="失效日期" data-name="expirationDate" data-width="180"/>
      <k-grid-column data-align="center" data-header="上传日期" data-type="date" data-name="createDate" data-render="renderDateTimeCreate" data-width="180"/>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" :data-disabled="scope.row.row.status=='1'" data-descript="文档模板版本生效" data-functype="SUBMIT" data-size="small"
               data-action="PrintTempVersion.updatePrintTempVersionStatus" data-target="printTempVersionGrid" :data-handler="getDataFlow"
               v-model="scope.row.row" :data-confirm="true"
               v-if="global.isShowAuthorityButton('PrintTempVersion.updatePrintTempVersionStatus')">
          <md-icon>done</md-icon>
        </k-btn>
         <k-btn class="md-info md-just-icon md-simple"  data-descript="文档模板作废" data-functype="SUBMIT" data-size="small"
               data-action="PrintTempVersion.deletePrintTempVersionStatus" data-target="printTempVersionGrid" v-model="scope.row.row" :data-confirm="true"
                v-if="global.isShowAuthorityButton('PrintTempVersion.deletePrintTempVersionStatus')">
          <md-icon>close</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" :data-download-name="scope.row.row.tempName"  data-descript="下载文档模板信息" data-functype="DOWNLOAD" data-size="small"
               data-url="/download/server/PmsApp/print/downloadPrintTempVersion.json" v-model="scope.row.row">
          <md-icon>cloud_download</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" ref="previewRef" data-descript="预览文档模板信息" data-size="small"
               :data-handler="previewPrintTempVersion" v-model="scope.row.row">
          <md-icon>zoom_in</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-functype="POPUP" ref="compareBtnRef" data-descript="比对当前文档" data-size="small"
               :data-handler="compareHandler" data-target="comparePrintTempVersionPopup" v-model="scope.row.row">
          <md-icon>compare_arrows</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" data-descript="文档模板修改" data-functype="POPUP" data-size="mini"
               data-target="editPopup1" v-if="global.isShowAuthorityButton('PrintTempVersion.updatePrintTempInfo')"  :data-handler="editHandler2">
          <md-icon>edit</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="editPopup1" title="模板子版本修改">
      <k-form ref="editForm2" data-ui="element" dataLabelWidth="130px" dataInputWidth="220px">

        <k-form-item label="文档名称" >
          <k-field-text v-model="editForm1.tempName" :data-allowblank="false"   :data-disabled="true"/>
        </k-form-item>
         <k-form-item label="文档版本编号" >
          <k-field-text v-model="editForm1.version" :data-allowblank="false"   :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="版本id" :hidden="true">
          <k-field-text v-model="editForm1.id" :data-allowblank="false"   :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="备注" data-input-width="590px">
          <k-field-text v-model="editForm1.remark" :data-allowblank="false" :data-max-length="2000" inputType="textarea" :rows="1"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="prodInfoGrid" ref="submitBtn" data-from="editForm2" :data-model="editForm1" data-action="PrintTempVersion.updatePrintTempVersion" :data-after-success="onEdittSuccess1">
            <span v-show="showSubmitBtn">确定</span>
            <i v-show="!showSubmitBtn" class="el-icon-loading"/>
          </k-btn>

          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="editPrintTempVersionPopup" title="上传文档模板子模版">
      <k-form ref="editPrintTempVersionForm" data-ui="element" v-model="editFormData" dataLabelWidth="150px" dataInputWidth="180px">
        <k-form-item label="对比版本">
          <k-field-text v-model="editFormData.version" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
         <k-form-item label="已存在风险数目" key="riskNum"
                     v-if="editFormData.tempType=='10001' ||
                      editFormData.tempType=='20001' ||
                      editFormData.tempType=='30001' ||
                      editFormData.tempType=='40001' ||
                      editFormData.tempType=='50001' ||
                      editFormData.tempType=='60001' ||
                      editFormData.tempType=='70001' ||
                      editFormData.tempType=='10101' ||
                      editFormData.tempType=='10007' ||
                      editFormData.tempType=='20007' ||
                      editFormData.tempType=='30007' ||
                      editFormData.tempType=='40007' ||
                      editFormData.tempType=='50007' ||
                      editFormData.tempType=='60007' ||
                      editFormData.tempType=='70007' ||
                      editFormData.tempType=='10107'">
          <k-field-text v-model="editFormData.riskNum" :data-allowblank="false"  data-value-field="id"  :data-regx="'^[0-9]+(\\.[0-9]{2})?$'"
                                data-regx-text="请输入正确的数字" data-validate-type="number" data-type="number" :data-max-length="2"/>
        </k-form-item>
        <k-form-item label="备注" data-input-width="550px">
          <k-field-text v-model="editFormData.remark" :data-allowblank="false" :data-max-length="2000" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload v-show="true" data-type="file" ref="editPrintTempVersionRef" :data-multiple="false" :data-limit=1
                          :data-error="onEditPrintTempVersionSubmitError" :data-success="onEditPrintTempVersionSubmitSuccess"
                          data-accept=".docx" :data-change="onEditPrintTempVersionChange"
                          :data-auto-upload="false" :data-upload-url="editUploadUrl" >
          </k-field-upload>


        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="printTempVersionGrid" ref="editPrintTempVersionSubmitBtn"
                 data-from="editPrintTempVersionForm" :data-model="editFormData" @click="submitEditPrintTempVersionUploadParam"
          >
            <span v-show="this.showEditPrintTempVersionSubmitBtn">确定</span>
            <i v-show="!this.showEditPrintTempVersionSubmitBtn" class="el-icon-loading"/>
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="comparePrintTempVersionPopup" title="对比产品文档模板">
      <k-form ref="comparePrintTempVersionForm" data-ui="element" v-model="compareFormData">
        <k-form-item label="版本Id">
          <k-field-text v-model="compareFormData.id" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="当前版本">
          <k-field-text v-model="compareFormData.version" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="备注" data-input-width="590px">
          <k-field-text v-model="compareFormData.remark" :data-disabled="true" :data-max-length="2000" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="comparePrintTempVersionRef" :data-multiple="false" :data-limit=1
                          :data-error="onCompareSubmitError" :data-success="onCompareSubmitSuccess"
                          data-accept=".docx"  :data-change="onCompareFileChange"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/onlineEdit/comparePrintTempVersion.json">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" ref="compareSubmitBtn"
                 data-from="comparePrintTempVersionForm" :data-model="compareFormData" :data-handler="submitCompare">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
  import {assign} from "lodash";
  import openWindow from "../../../utils/openWindow";
  import Tools from "@/utils/tools";

  export default {
    name: "printTempVersionUpload",
    data() {
      return {
        PrintTempData: {},
        fileData:'',
        onlineUrl: '',
        serverPath: '',
        flowFileData:{

        },
        fileList:[],
        childFileList:[],
        fileNameList:[],
        childFileNameList:[],
        printTemp: {
          tempType: '',
          docType: '',
          tempName: ''
        },
        docTypeDict: {},
        addDocTypeDict: {},
        formData: {
          docType: '',
          tempType: '',
          distributorCode: '',
          t8TruteeInfoId: '',
          t8MeetCreateId: '',
          remark: '',
          version: 0,
          onlineUrl: '',
        },

        editForm: {
          id: '',
          docType: '',
          tempType: '',

          remark: '',

        },
        editForm1: {
          id: '',
          tempName: '',
          version: '',

          remark: '',

        },
        showSubmitBtn:true,
        showEditPrintTempVersionSubmitBtn:true,
        editFormData:{
          riskNum:'',
          t8PrintTempId:'',
          version:'',
          onlineUrl:'',
          remark:'',
          docType: '',
          tempType: '',
          distributorCode: '',
          t8TruteeInfoId: '',
          t8MeetCreateId: '',
        },
        editFormData2:{
          riskNum:'',
          t8PrintTempId:'',
          version:'',
          onlineUrl:'',
          remark:'',
          docType: '',
          tempType: '',
          distributorCode: '',
          t8TruteeInfoId: '',
          t8MeetCreateId: '',
        },
        compareFormData:{
          id:'',
          version:'',
          remark:'',
          onlineUrl: ''
        },
        editUploadUrl:'',
        submitVersion:'',//本次提交后的版本
      };
    },
    mounted() {
      let urlPath = window.document.location.href;  //浏览器显示地址 http://10.1.20.88:8201/xxx/xxx
      let docPath = "8201"; //服务器相对地址 8201/xxx/xxx
      let index = urlPath.indexOf(docPath);
      this.serverPath = urlPath.substring(0, index);
      this.onlineUrl = this.httpUtil.onlineUrl;
      this.onDocTypeChange()
    },
    methods: {
      getSubmitVersion(tempVersion){//v1.23
        let version = tempVersion.split(".")[1];
        this.submitVersion = parseInt(version)+1;
        console.log("this.submitVersion=:>>",this.submitVersion);
        return "V1."+this.submitVersion;
      },
      renderDateTimeCreate(row) {
        if(!row.createTime){
          return Tools.formatDate(row.createDate);
        }
        return Tools.formatDateTime(row.createDate, row.createTime);
      },
      beforePopupLoad(params) {
        params.t8PrintTempId = this.PrintTempData.id;
        return params;
      },
      getDataFlow(val){
        this.$set(val,'tempType',this.formData.tempType);
        this.$set(val,'docType',this.formData.docType);
        this.$set(val,'distributorCode',this.formData.distributorCode);
        this.$set(val,'t8TruteeInfoId',this.formData.t8TruteeInfoId);
        this.$set(val,'t8MeetCreateId',this.formData.t8MeetCreateId);
        return val;
      },
      onDocTypeChange() {
       // this.printTemp.tempType = '';
        this.httpUtil.comnQuery({
          action: "PrintTemp.getTempTypeByDocType",
          params: {docType: 1}
        }).then(data => {
          this.docTypeDict = data.rows;
        }).catch({})
      },
      rollbackFlow(dataParams){
        this.httpUtil.comnUpdate({
          action: "PrintTempVersion.rollbackFlow",
          params: dataParams,
          mask: false,
          successAlert:false
        }).then(data => {
        });
      },
       onEdittSuccess1() {
        this.$refs.editForm2.reset();
        this.$refs.editPopup1.close();
        this.$refs.printTempVersionGrid.load();
      },
      onChildSubmitSuccess() {
        this.$refs.editPrintTempVersionRef.doReset();
        this.$refs.editPrintTempVersionForm.reset();
        this.$refs.editPrintTempVersionPopup.close();
        this.$refs.printTempVersionGrid.load();
      },
      selectPrintTemp(row, column, event) {
        const _this = this;
        _this.selectRowData = assign({}, row);
        _this.formData = assign({}, row);
        this.$refs.printTempVersionGrid.load({t8PrintTempId: _this.selectRowData.id});
      },
      editHandler(value) {
        this.fileData='';
        this.editFormData.t8PrintTempId =this.PrintTempData.id;
        this.editFormData.remark='';
        this.editFormData.docType = this.PrintTempData.docType;
        this.editFormData.tempType = this.PrintTempData.tempType;
        this.editFormData.distributorCode = this.PrintTempData.distributorCode;
        this.editFormData.t8TruteeInfoId = this.PrintTempData.t8TruteeInfoId;
        this.editFormData.t8MeetCreateId = this.PrintTempData.t8MeetCreateId;
        this.editUploadUrl='/upload/server/PmsApp/onlineEdit/comparePrintTempVersion.json';
        this.httpUtil.comnQuery({
          action: "PrintTempVersion.getNewestPrintTempVersion2",
          params: {t8PrintTempId: this.editFormData.t8PrintTempId}
        }).then(data => {
          if(data.rows.length>0){
            let version = data.rows[0].version.split(".");
            this.editFormData.version = data.rows[0].version;
          }
        }).catch({
        })
      },

      editHandler2(value) {
        this.$set(this.editForm1,'id',value.id);
        this.$set(this.editForm1,'version',value.version);
        this.$set(this.editForm1,'remark',value.remark);
        this.$set(this.editForm1,'tempName',value.tempName);


      },
      onEditPrintTempVersionSubmitError(){
        //this.showEditPrintTempVersionSubmitBtn = true;
        this.$refs.editPrintTempVersionRef.doReset();
        this.showEditPrintTempVersionSubmitBtn=true;
        this.$refs.editPrintTempVersionSubmitBtn.setIconStyle(1, [])
      },
      onEditPrintTempVersionSubmitSuccess(response,file){
        this.showEditPrintTempVersionSubmitBtn=true;
        //this.$set(this.showEditPrintTempVersionSubmitBtn,true);
        let returnData = response.response.returndata;
        //const _this = this
        //_this.showEditPrintTempVersionSubmitBtn=true;
        //当返回类型为1是打开比对页面，否则代表上传成功
        if (returnData.type =='1'){
          let url = returnData.url;
          //重置文件上传状态
          file[0].status='ready';
          this.$refs.editPrintTempVersionSubmitBtn.setIconStyle(1, []);
          this.showEditPrintTempVersionSubmitBtn=true;
          window.open(url,'_blank','width=1000,height=800,toolbars=yes,resizable=yes,scrollbars=yes,left=20,top=30');
        }else {
          this.showEditPrintTempVersionSubmitBtn=true;
          this.$refs.editPrintTempVersionRef.doReset();
          this.$refs.editPrintTempVersionForm.reset();
          this.$refs.editPrintTempVersionPopup.close();
          this.$refs.printTempVersionGrid.load();
          this.editUploadUrl='/upload/server/PmsApp/onlineEdit/comparePrintTempVersion.json';
        }
      },
      submitEditPrintTempVersionUploadParam(){
        /*let urlPath = window.document.location.href;  //浏览器显示地址 http://10.1.20.88:8201/xxx/xxx
        let docPath = "8201"; //服务器相对地址 8201/xxx/xxx
        let index = urlPath.indexOf(docPath);*/
        let serverPath = this.serverPath;
        let onlineUrl = this.onlineUrl;
        if(onlineUrl!="undefined"&&onlineUrl!=null&&onlineUrl!=""){
          this.editFormData.onlineUrl = onlineUrl;
        }else{
          this.editFormData.onlineUrl = serverPath+"8201";
        }
        let editFormData = this.editFormData;
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childElementCount;
        if(editFormData.riskNum == ""&&(editFormData.tempType=='10001' ||
                      editFormData.tempType=='20001' ||
                      editFormData.tempType=='30001' ||
                      editFormData.tempType=='40001' ||
                      editFormData.tempType=='50001' ||
                      editFormData.tempType=='60001' ||
                      editFormData.tempType=='70001' ||
                      editFormData.tempType=='10101' ||
                      editFormData.tempType=='10007' ||
                      editFormData.tempType=='20007' ||
                      editFormData.tempType=='30007' ||
                      editFormData.tempType=='40007' ||
                      editFormData.tempType=='50007' ||
                      editFormData.tempType=='60007' ||
                      editFormData.tempType=='70007' ||
                      editFormData.tempType=='10107')){
          Tools.alert("请输入已存在风险数目！","danger");
          return false;
        }

        if(editFormData.remark == ""){
          Tools.alert("请输入备注！","danger");
          return false;
        }
        if(lis>0){
          this.fileNameList = [];
          for(let i in this.childFileList){
            this.fileNameList.push(this.childFileList[i]);
          }
          this.editFormData['fileNameList'] = JSON.stringify(this.fileNameList);
          let str = '';
          this.editFormData2 = this.editFormData;
          let newVersion = this.getSubmitVersion(this.editFormData2.version);
          this.$set(this.editFormData2,'version',newVersion);//提交数据时的文档版本号
          console.log("this.editFormData2=:>>>>",this.editFormData2);
          //注意权限，是否要走审批流及提示信息 axin
          this.httpUtil.comnUpdate({
            action: "PrintTempVersion.insChildDocumentFlow",
            params: this.editFormData2,
            mask: false,
            successAlert:false
          }).then(data => {
            //判断是否进入了审批流
            if(data.returndata != undefined && data.returndata.data != undefined
              && data.returndata.data.processInstanceId != undefined){
              this.$set(this.editFormData2,'processInstanceId',data.returndata.data.processInstanceId);//获取进入审批流后的实列id
              str = data.returnmsg;//成功后的返回信息
            }
            if(data.success){//（进入审批流且成功或者没有审批流），并且有按钮权限
              let uploadData = this.editFormData2;
              this.fileData.append('params', JSON.stringify(uploadData));
              this.httpUtil.upload({
                url:"/upload-files/server/PmsApp/onlineEdit/uploadPrintTempVersion.json",
                formData: this.fileData
              }).then(res=>{
                this.showEditPrintTempVersionSubmitBtn = true;
                if(res.data.success){
                  if(str==''||str==null||str==undefined){
                    str = res.data.returnmsg;
                    Tools.alert(str);
                    this.onChildSubmitSuccess()
                  }else{
                    Tools.alert(str);
                    this.onChildSubmitSuccess()
                  }
                }else{
                  str = res.data.returnmsg;
                  Tools.alert(str,'danger');
                  if(data.returndata != undefined && data.returndata.data != undefined
                    && data.returndata.data.processInstanceId != undefined){
                    this.$set(this.formData,'processInstanceId',data.returndata.data.processInstanceId);
                    this.rollbackFlow(this.formData);
                  }
                }
              })
            }else{//进入没成功或者没权限
              this.showEditPrintTempVersionSubmitBtn = true;
            }
          });
        }else{
          Tools.alert("上传附件不能为空!","danger");
          this.showEditPrintTempVersionSubmitBtn = true;
          return false;
        }
      },
      previewPrintTempVersion(value){
        this.httpUtil.comnQuery({
          action:'T8OnlineWordValue.getMaxT8OnlineWordValueByT8PrintTempVersionId1',
          params: {t8PrintTempVersionId:value.id}
        }).then(data => {
          this.$nextTick(()=>{
            if (data != null && data.rows.length > 0){
              let url = data.rows[0].viewUrl;
              window.open(url,'_blank','width=1000,height=800,toolbars=yes,resizable=yes,scrollbars=yes,left=20,top=30');
            }
          })
        }).catch({

        })
      },
      onEditPrintTempVersionChange(file,fileList){
        const _this = this
        _this.fileData = new FormData();
        _this.fileData.append('files', file.raw);
        _this.fileList = fileList;
/*        let urlPath = window.document.location.href;  //浏览器显示地址 http://10.1.20.88:8201/xxx/xxx
        let docPath = "8201"; //服务器相对地址 8201/xxx/xxx
        let index = urlPath.indexOf(docPath);*/
        let serverPath = this.serverPath;
        let onlineUrl = this.onlineUrl;
        if(onlineUrl!="undefined"&&onlineUrl!=null&&onlineUrl!=""){
          _this.editFormData.onlineUrl = onlineUrl;
        }else{
          _this.editFormData.onlineUrl = serverPath+"8201";
        }
        let fileName = file.name
        this.childFileList.push(fileName);
        let suffix = fileName.substr(fileName.lastIndexOf('.') + 1);
         if ('docx' != suffix) {
           Tools.alert("只能上传格式为docx类型的文档!","danger");
           _this.$refs.editPrintTempVersionRef.doReset();
           return false;
         }
        if (file.status=='ready' && file.response==null){
          this.showEditPrintTempVersionSubmitBtn=false;
          let editFormData = _this.editFormData;
          this.$refs.editPrintTempVersionSubmitBtn.setIconStyle(1, []);
          this.$refs.editPrintTempVersionRef.upload(editFormData);
        }
      },
      compareHandler(value){
        this.compareFormData = value;
      },
      submitCompare(){
        let serverPath = this.serverPath;
        let onlineUrl = this.onlineUrl;
        if(onlineUrl!="undefined"&&onlineUrl!=null&&onlineUrl!=""){
          this.compareFormData.onlineUrl = onlineUrl;
        }else{
          this.compareFormData.onlineUrl = serverPath+"8201";
        }
        let compareFormData = this.compareFormData;
        this.$refs.comparePrintTempVersionRef.upload(compareFormData);
      },
      onCompareSubmitError(){
        this.$refs.comparePrintTempVersionRef.doReset();
        this.$refs.compareSubmitBtn.setIconStyle(1, []);
      },
      onCompareSubmitSuccess(response,file){
        let returnData = response.response.returndata;
        file[0].status='ready';
        let url = returnData.url;
        this.$refs.compareSubmitBtn.setIconStyle(1, []);
        window.open(url,'_blank','width=1000,height=800,toolbars=yes,resizable=yes,scrollbars=yes,left=20,top=30');
      },
      onCompareFileChange(file) {
        let fileName = file.name
        let suffix = fileName.substr(fileName.lastIndexOf('.') + 1);
        if ('docx' != suffix) {
          Tools.alert("只能对比docx类型的文档!","danger");
          this.$refs.comparePrintTempVersionRef.doReset();
          return false;
        }
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
    created() {
      if(this.$route.query.PrintTempData.id == undefined){
        // var Data = localStorage.getItem('printTempData')
        // if (Data) {
        //   let T8ProdList = JSON.parse(Data);
        //   this.PrintTempData = T8ProdList;
        // }
      } else {
        //不刷新二级页面，每一次只走这里
        this.PrintTempData = this.$route.query.PrintTempData;
      }
      this.$nextTick(() => {
        this.$refs.printTempVersionGrid.load({t8PrintTempId:this.PrintTempData.id});
      });
    },
    activated() {
      //除了刷新走created和activated,以后每一次路由走activated(一级列表不关闭)
      if(this.$route.query.PrintTempData.id == undefined){
        var Data = localStorage.getItem('printTempData')
        if (Data) {
          let T8ProdList = JSON.parse(Data);
          this.PrintTempData = T8ProdList;
        }
      } else {
        this.PrintTempData = this.$route.query.PrintTempData;
      }
      //this.PrintTempData = this.$route.query.PrintTempData;
      this.$nextTick(() => {
        this.$refs.printTempVersionGrid.load({t8PrintTempId:this.PrintTempData.id});
      });
    }
  }
</script>
