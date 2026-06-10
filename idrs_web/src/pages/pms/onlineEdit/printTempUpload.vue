<template>
  <div>
    <k-form-search-customize data-target="printTempGrid" v-model="printTemp">
      <k-form-item label="文档名称">
        <k-field-text v-model="printTemp.tempName"/>
      </k-form-item>
      <k-form-item label="文档类型">
        <k-field-select v-model="printTemp.docType" data-action="T8Dict.t8PrintDoc"
                        data-display-field="itemval"  data-value-field="itemkey"
                        />
      </k-form-item>
      <k-form-item label="模板类型">
        <k-field-select v-model="printTemp.tempType" :data-data="docTypeDict" data-value-field="value"
                        data-display-field="text"/>
      </k-form-item>
      <k-btn slot="button" class="btn-custom-primary" style="width: 100px" data-functype="POPUP"  :data-handler="addHandler"
             data-target="addPopup"
             v-if="global.isShowAuthorityButton('PrintTemp.savePrintTempInfo')">
        <md-icon md-src="/static/svg/add.svg"/>
        上传文档模板
      </k-btn>
    </k-form-search-customize>
    <k-grid ref="printTempGrid" data-action="PrintTemp.getPrintTempList1">
      <k-grid-column :data-sortable="true" data-default-sort="DESC" data-align="center" data-header="模板id" data-hidden="true" data-name="id"/>
      <k-grid-column data-align="center" data-header="文档类型" data-name="docType" data-dict="t8_print_doc"/>
      <k-grid-column data-align="center" data-header="模板类型" data-dict="t8_temp_type" data-name="tempType"/>
      <k-grid-column data-align="center" data-header="文档名称" data-name="tempName" data-hidden="true"/>
      <k-grid-column data-align="center" data-header="备注" data-name="remark" data-width="400"/>
      <k-grid-column data-align="center" data-header="生效状态" data-name="status" data-dict="t8_print_temp_version_status"/>
      <k-grid-column data-align="center" data-header="更新时间" data-name="TempVersionUpdate" data-render="renderDateTimeCreate"/>
      <k-grid-column data-align="center" data-header="上传日期" data-type="date" data-name="createDate"/>
      <template slot="operate" slot-scope="scope">

         <k-btn class="md-info md-just-icon md-simple" data-descript="文档模板修改" data-functype="POPUP" data-size="mini"
               data-target="editPopup" v-if="global.isShowAuthorityButton('PrintTemp.updatePrintTempInfo')"  :data-handler="editHandler1">
          <md-icon>edit</md-icon>
        </k-btn>

        <k-btn data-functype="PAGE" data-size="mini"  class="md-info md-just-icon md-simple" :data-model="scope.row.row.id"
               @click="popupEdit(scope.row.row)"  data-descript="文档子模板详情管理">
          <md-icon>weekend</md-icon>
        </k-btn>

      </template>
    </k-grid>


    <k-popup ref="addPopup" title="上传文档模板">
      <k-form ref="addForm" data-ui="element" dataLabelWidth="130px" dataInputWidth="220px">
        <k-form-item label="文档类型">
          <k-field-select v-model="formData.docType" :data-allowblank="false" data-action="T8Dict.t8PrintDoc"
                          data-display-field="itemval"  data-value-field="itemkey"
                          @data-on-change="onAddDocTypeChange"/>
        </k-form-item>
        <k-form-item label="模板类型">
          <k-field-select v-model="formData.tempType" :data-allowblank="false" :data-data="addDocTypeDict"
                          data-value-field="value" data-display-field="value,text"/>
        </k-form-item>
        <k-form-item label="销售商信息" key="distributor"
                     v-if="formData.tempType=='10003' ||
                      formData.tempType=='20003' ||
                      formData.tempType=='30003' ||
                      formData.tempType=='40003' ||
                      formData.tempType=='50003' ||
                      formData.tempType=='60003' ||
                      formData.tempType=='70003' ||
                      formData.tempType=='10103'">
          <k-field-select v-model="formData.distributorCode" :data-allowblank="false"  data-action="T8Dict.findTaDistributorInfos"
                          data-display-field="distributorName"  data-value-field="distributorCode"  />
        </k-form-item>
        <k-form-item label="托管行信息" key="t8TruteeIn"
                     v-if="formData.tempType=='10002' ||
                      formData.tempType=='20002' ||
                      formData.tempType=='30002' ||
                      formData.tempType=='40002' ||
                      formData.tempType=='50002' ||
                      formData.tempType=='60002' ||
                      formData.tempType=='70002' ||
                      formData.tempType=='10102'">
          <k-field-select v-model="formData.t8TruteeInfoId" :data-allowblank="false" data-action="T82006.findTaCustodianBanks3"
                          data-display-field="truteeName"  data-value-field="id"  />
        </k-form-item>
        <k-form-item label="所属会议" key="t8MeetCreate"
                     v-if="formData.tempType=='10001' ||

                      formData.tempType=='20001' ||
                      formData.tempType=='30001' ||
                      formData.tempType=='40001' ||
                      formData.tempType=='50001' ||
                      formData.tempType=='60001' ||
                      formData.tempType=='70001' ||
                      formData.tempType=='10101'">
          <k-field-select v-model="formData.t8MeetCreateId" :data-allowblank="false" data-action="MeetCreate.findMeetCreate"
                          data-display-field="meetName"  data-value-field="id"  />
        </k-form-item>
        <k-form-item label="已存在风险数目" key="riskNum"
                     v-if="formData.tempType=='10001' ||

                      formData.tempType=='20001' ||
                      formData.tempType=='30001' ||
                      formData.tempType=='40001' ||
                      formData.tempType=='50001' ||
                      formData.tempType=='60001' ||
                      formData.tempType=='70001'||
                       formData.tempType=='10101'||
                      formData.tempType=='10007' ||
                      formData.tempType=='20007' ||
                      formData.tempType=='30007' ||
                      formData.tempType=='40007' ||
                      formData.tempType=='50007' ||
                      formData.tempType=='60007' ||
                      formData.tempType=='70007' ||
                      formData.tempType=='10107'">
          <k-field-text v-model="formData.riskNum" :data-allowblank="false"  data-value-field="id"  :data-regx="'^[0-9]+(\\.[0-9]{2})?$'"
                                data-regx-text="请输入正确的数字" data-validate-type="number" data-type="number"  :data-max-length="2"/>
        </k-form-item>
        <k-form-item label="文档版本">
          <k-field-text v-model="formData.version" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="备注" data-input-width="590px">
          <k-field-text v-model="formData.remark" :data-allowblank="false" :data-max-length="2000" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
<!--          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit=1-->
<!--                          :data-error="onSubmitError"  data-accept=".docx" :data-success="onSubmitSuccess"-->
<!--                          :data-auto-upload="false" :data-change="onUploadFileChange"-->
<!--                          data-upload-url="/upload/server/PmsApp/onlineEdit/uploadPrintTemp.json">-->
<!--          </k-field-upload>-->

          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="true" :data-limit=1
                          :data-error="onSubmitError" :data-success="onSubmitDocSuccess" :dataChange="onUploadChange" data-accept=".docx"
                          :dataHttpRequest="httpRequest"
                          :data-auto-upload="false">
          </k-field-upload>

        </k-form-item>
        <k-form-footer data-align="center">
<!--          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="printTempGrid" ref="submitBtn"-->
<!--                 data-from="addForm" :data-model="formData" :data-handler="submitUploadParam">确定-->
<!--          </k-btn>-->

          <k-btn class="btn-custom-primary" data-target="prodInfoGrid" ref="submitBtn" data-from="addForm" :data-model="formData" @click="submitUploadParam">
            <span v-show="showSubmitBtn">确定</span>
            <i v-show="!showSubmitBtn" class="el-icon-loading"/>
          </k-btn>

          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


    <k-popup ref="editPopup" title="上传文档修改">
      <k-form ref="editForm1" data-ui="element" dataLabelWidth="130px" dataInputWidth="220px">
        <k-form-item label="文档类型">
          <k-field-select v-model="editForm.docType" :data-allowblank="false" data-action="T8Dict.t8PrintDoc"
                          data-display-field="itemval"  data-value-field="itemkey"
                          :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="模板类型">
          <k-field-select v-model="editForm.tempType" :data-allowblank="false" :data-data="addDocTypeDict"
                          data-value-field="value" data-display-field="value,text" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="id" :hidden="true">
          <k-field-text v-model="editForm.id" :data-allowblank="false"   :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="备注" data-input-width="590px">
          <k-field-text v-model="editForm.remark" :data-allowblank="false" :data-max-length="2000" inputType="textarea" :rows="1"/>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="prodInfoGrid" ref="submitBtn" data-from="editForm1" :data-model="editForm" data-action="PrintTemp.updatePrintTempInfo" :data-after-success="onEdittSuccess">
            <span v-show="showSubmitBtn">确定</span>
            <i v-show="!showSubmitBtn" class="el-icon-loading"/>
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
    name: "",
    data() {
      return {
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
          riskNum:'',
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
        editUploadUrl:''
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
      renderDateTimeCreate(row) {
        //console.log(row.tempVersionUpdateTime)
        if(!row.tempVersionUpdateTime){
          return Tools.formatDate(row.tempVersionUpdateDate);
        }
        return Tools.formatDateTime(row.tempVersionUpdateDate, row.tempVersionUpdateTime);
      },
      popupEdit(row){
        localStorage.setItem("printTempData", JSON.stringify(row));
        let pathUrl = '/main/pms/onlineEdit/printTempVersionUpload';
        this.$router.push({
          path: pathUrl,
            query: {PrintTempData: row},
        });
      },
      validateData() {
        return this.$refs.addForm.validate();
      },
      httpRequest(file){
        this.fileData.append('files', file.file);
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
      onAddDocTypeChange() {
        this.formData.tempType = '';
        this.httpUtil.comnQuery({
          action: "PrintTemp.getTempTypeByDocType",
          params: {docType: this.formData.docType}
        }).then(data => {
          this.addDocTypeDict = data.rows;
        }).catch({})
      },
      onUploadFileChange(file) {
        let fileName = file.name
        let suffix = fileName.substr(fileName.lastIndexOf('.') + 1);
        if ('docx' != suffix) {
          Tools.alert("只能上传格式为docx类型的文档!","danger");
          this.$refs.uploadRef.doReset();
          return false;
        }
      },
      submitUploadParam() {
        /*let urlPath = window.document.location.href;  //浏览器显示地址 http://10.1.20.88:8201/xxx/xxx
        let docPath = "8201"; //服务器相对地址 8201/xxx/xxx
        let index = urlPath.indexOf(docPath);*/
        let serverPath = this.serverPath;
        let onlineUrl = this.onlineUrl;
        if(onlineUrl!="undefined"&&onlineUrl!=null&&onlineUrl!=""){
          this.formData.onlineUrl = onlineUrl;
        }else{
          this.formData.onlineUrl = serverPath+"8201";
        }
        let flag = this.validateData();
        if(!flag){
          return false;
        }
        let temp = document.getElementsByClassName('upload-demo');

        this.fileNameList = [];
        for(let i in this.fileList){
          this.fileNameList.push(this.fileList[i].name);
        }
        this.formData['fileNameList'] = JSON.stringify(this.fileNameList);
        this.showSubmitBtn = false;
        let str = '';
        this.$set(this.formData,'tempName',this.fileNameList[0].substring(0,this.fileNameList[0].lastIndexOf(".")));
        this.httpUtil.comnUpdate({
          action: "PrintTemp.checkFlowPrintTemp",
          params: this.formData,
          mask: false,
          successAlert: false,
        }).then(data2 => {
          if(data2.success){
            //注意权限，是否要走审批流及提示信息 axin
            this.httpUtil.comnUpdate({
              action: "PrintTempVersion.insDocumentFlow",
              params: this.formData,
              mask: false,
              successAlert: false,
            }).then(data => {
              //判断是否进入了审批流
              if(data.returndata != undefined && data.returndata.data != undefined
                && data.returndata.data.processInstanceId != undefined){
                this.$set(this.formData,'processInstanceId',data.returndata.data.processInstanceId);//获取进入审批流后的实列id
                str = data.returnmsg;//成功后的返回信息
              }
              if(data.success){//（进入审批流且成功或者没有审批流），并且有按钮权限
                let uploadData = this.formData;
                this.fileData = new FormData();
                this.$refs.uploadRef.upload();
                this.fileData.append('params', JSON.stringify(uploadData));
                this.httpUtil.upload({
                  url:"/upload-files/server/PmsApp/onlineEdit/uploadPrintTemp.json",
                  formData: this.fileData
                }).then(res=>{
                  this.showSubmitBtn = true;

                  if(res.data.success){//如果没有审批流获取上传后的提示信息

                    if(this.formData.processInstanceId!=null&&this.formData.processInstanceId!=undefined&&this.formData.processInstanceId!=""){
                      Tools.alert(str);
                    }else{
                      Tools.alert(res.data.returnmsg);
                    }
                    this.onSubmitSuccess()
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
                this.showSubmitBtn = true;
                //Tools.alert(data.returnmsg);
              }
            });
          }else{
            this.showSubmitBtn = true;
          }
        });
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
      onSubmitError() {
        this.$refs.uploadRef.doReset();
        this.$refs.submitBtn.setIconStyle(1, []);
        this.showSubmitBtn = true;
      },
      onSubmitDocSuccess() {
        this.$refs.uploadRef.doReset();
        this.$refs.addForm.reset();
        this.$refs.addPopup.close();
        this.$refs.printTempGrid.load();
      },
      onSubmitSuccess() {
        this.$refs.uploadRef.doReset();
        this.$refs.addForm.reset();
        this.$refs.addPopup.close();
        this.$refs.printTempGrid.load();
      },
       onEdittSuccess() {
        this.$refs.editForm1.reset();
        this.$refs.editPopup.close();
        this.$refs.printTempGrid.load();
      },

      onChildSubmitSuccess() {
        this.$refs.editPrintTempVersionRef.doReset();
        this.$refs.editPrintTempVersionForm.reset();
        this.$refs.editPrintTempVersionPopup.close();
        this.$refs.printTempGrid.load();
      },
      addHandler() {
        this.formData.docType = '';
        this.formData.tempType = '';
        this.formData.riskNum = '';
         this.formData.remark = '';
        this.formData.distributorCode = '';
        this.formData.t8TruteeInfoId = '';
        this.formData.t8MeetCreateId = '';
        this.formData.version = 'V1.0';
      },
      selectPrintTemp(row, column, event) {
        const _this = this;
        _this.selectRowData = assign({}, row);
        _this.formData = assign({}, row);
      },
      editHandler(value) {
        this.fileData='';
        this.editFormData.t8PrintTempId =value.id;
        this.editFormData.remark='';
        this.editFormData.docType = value.docType;
        this.editFormData.tempType = value.tempType;
        this.editFormData.distributorCode = value.distributorCode;
        this.editFormData.t8TruteeInfoId = value.t8TruteeInfoId;
        this.editFormData.t8MeetCreateId = value.t8MeetCreateId;
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

      editHandler1(value) {
        this.$set(this.editForm,'id',value.id);
        this.$set(this.editForm,'remark',value.remark);
        this.$set(this.editForm,'docType',value.docType);
        if((value.tempType).endsWith("01")){
        this.$set(this.editForm,'tempType',"产品说明书");
        }
         if(value.tempType.endsWith("02")){
          this.$set(this.editForm,'tempType',"托管协议");
        }
         if(value.tempType.endsWith("03")){
            this.$set(this.editForm,'tempType',"补充代销协议");

        }
         if(value.tempType.endsWith("04")){
            this.$set(this.editForm,'tempType',"可行性报告");

        }
         if(value.tempType.endsWith("05")){
            this.$set(this.editForm,'tempType',"报告主体文件");

        }
         if(value.tempType.endsWith("06")){
          this.$set(this.editForm,'tempType',"申请发行说明");
        }
         if(value.tempType.endsWith("07")){
             this.$set(this.editForm,'tempType',"创设方案");
        }
         if(value.tempType.endsWith("08")){
             this.$set(this.editForm,'tempType',"消保审核");
        }
         if(value.tempType.endsWith("09")){
             this.$set(this.editForm,'tempType',"产品分红参数表");
        }
         if(value.tempType.endsWith("10")){
             this.$set(this.editForm,'tempType',"产品定价审批表");
        }

      },
      onUploadChange(file,fileList){
        this.fileList = fileList;
      },
    },
  }
</script>

<style scoped>

</style>
