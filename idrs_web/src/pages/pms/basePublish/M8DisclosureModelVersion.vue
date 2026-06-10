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

        <div class="k-form-search-footer">
          <k-btn slot="button" class="btn-custom-primary" style="width: 130px" data-functype="POPUP" :data-handler="editHandler"
                 data-target="editPrintTempVersionPopup">
            <md-icon md-src="/static/svg/add.svg"/>
            上传新版本
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="PAGE" data-target="/main/pms/basePublish/printTemp">
            返回
          </k-btn>
        </div>
      </md-card>
    </template>

    <k-grid ref="printTempVersionGrid"
            data-action="DisclosureModVersion.findDisclosureModVersionsAuth"
            :data-before-load="beforePopupLoad"
            @data-row-select="selectPrintTemp"
            data-operate-column="true"
            :data-display="false">
      <k-grid-column data-align="center" data-header="版本id" data-name="id" data-hidden="true"/>
      <k-grid-column data-align="center" data-header="对应模板id" data-name="disclosureModId" data-hidden="true"/>
      <k-grid-column data-align="center" data-header="信披类型" data-name="disclosureType" data-dict="xp_doc_type" data-width="100"/>
      <k-grid-column data-align="center" data-header="信披子类型" data-dict="xp_son_type" data-name="disclosureSonType" />
      <k-grid-column data-align="center" data-header="文档名称" data-name="docName" data-width="300"/>
      <k-grid-column data-align="center" data-header="备注" data-name="remark" data-width="250"/>
      <k-grid-column data-align="center" data-header="文档版本编号" data-name="version" data-width="100"/>
      <k-grid-column data-align="center" data-header="创建人" data-name="crtUserName"/>
      <k-grid-column data-align="center" data-header="生效状态" data-name="status" data-width="100" data-dict="t8_print_temp_version_status"/>
      <k-grid-column data-align="center" data-header="上传时间" data-name="crtDate" data-render="renderDateTimeCreate" data-width="130"/>
      <k-grid-column data-align="center" data-header="复核人" data-name="updUserName"/>
      <k-grid-column data-align="center" data-header="复核意见" data-name="opinion"/>
      <k-grid-column data-align="center" data-header="复核时间" data-name="updDate" data-render="renderDateTimeUpd" data-width="130"/>
      <template slot="operate" slot-scope="scope">
        <k-field-bswitch data-on-value="1" data-off-value="0" v-model="scope.row.row.status" data-on-action="DisclosureModVersion.recoverStatus"
                         data-off-action="DisclosureModVersion.stopStatus" :data-params=scope.row.row :data-confirm="true" data-on-confirm-info="启用"
                         data-off-confirm-info="停用" @data-on-change="flashing"/>
        <k-btn class="btn-custom-text" :data-download-name="scope.row.row.docName"  data-descript="下载文档模板信息" data-functype="DOWNLOAD" data-size="mini"
               data-url="/download/server/PmsApp/print/downloadXPTempVersion.json" v-model="scope.row.row">
          下载
        </k-btn>
<!--        <k-btn class="btn-custom-text" ref="previewRef" data-descript="预览文档模板信息" data-size="mini"-->
<!--               :data-handler="previewPrintTempVersion" v-model="scope.row.row">-->
<!--          预览-->
<!--        </k-btn>-->
        <k-btn class="btn-custom-text" data-descript="模板字段维护" data-functype="POPUP" data-size="small"
               data-target="xpModWordUpdate" :data-handler="versionChangeFnc">
          字段维护
        </k-btn>
        <k-btn class="btn-custom-text" data-descript="删除" data-functype="POPUP" data-size="mini"
               :data-handler="handleDelBtn">
          删除</k-btn>

      </template>
    </k-grid>

    <k-popup ref="editPrintTempVersionPopup" title="上传文档模板子模版">
      <k-form ref="editPrintTempVersionForm" data-ui="element" v-model="editFormData">
        <k-form-item label="对比版本">
          <k-field-text v-model="editFormData.version" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="对比版本名称">
          <k-field-text v-model="editFormData.docName" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="editFormData.remark" :data-allowblank="false" :data-max-length="256"/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload v-show="true" data-type="file" ref="editPrintTempVersionRef" :data-multiple="false" :data-limit=1
                          :data-error="onEditPrintTempVersionSubmitError" :data-success="onEditPrintTempVersionSubmitSuccess"
                          data-accept=".docx" :dataHttpRequest="httpRequest" :dataChange="onEditPrintTempVersionChange"
                          :data-auto-upload="false" :data-upload-url="editUploadUrl" >
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="printTempVersionGrid" ref="editPrintTempVersionSubmitBtn"
                 data-from="editPrintTempVersionForm" :data-model="editFormData" @click="submitEditPrintTempVersionUploadParam">
            <span v-show="this.showEditPrintTempVersionSubmitBtn">确定</span>
            <i v-show="!this.showEditPrintTempVersionSubmitBtn" class="el-icon-loading"/>
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="xpModWordUpdate" title="模板字段维护"  @data-opened="dataEcho" :data-dialog-drag="true" data-width="900px">
      <k-grid ref="updateGrid"  data-action="" :data-page-size="0" data-height="500px" data-operate-column="false"
              @init="(grid)=>{this.DisclosureModColumn.$RatGrid = grid}" data-display="false" style="height: 600px; overflow: auto;">
        <k-grid-column data-header="id" data-name="id" data-width="20" data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="字段描述" data-name="columnLabel" data-width="150"></k-grid-column>
        <k-grid-column data-header="字段key" data-name="columnKey" data-width="80"></k-grid-column>
        <k-grid-column data-header="字段默认值" data-name="columnValue" ></k-grid-column>
        <k-grid-column data-header="是否显示" data-name="isdisplay"  data-dict="xp_if_ok" data-width="150">
          <template slot-scope="scope" >
            <k-field-select v-model="scope.row.row.isdisplay" data-dict="xp_if_ok" :data-default-value="'1'" :data-allowblank="false"/>
          </template>
        </k-grid-column>
        <k-grid-column data-header="取值类型" data-name="isSysvalue" data-width="150">
          <template slot-scope="scope" >

            <k-field-select v-model="scope.row.row.isSysvalue" data-dict="xp_is_sysvalue"
                            :data-allowblank="false"/>
          </template>
        </k-grid-column>
        <k-grid-column data-header="角色" data-name="roleids" data-width="150">
          <template slot-scope="scope" >
            <k-field-select v-model="scope.row.row.roleids"  :data-data="roleList"  data-display-field="label" data-value-field="value" :data-multiple="true" :data-allowblank="scope.row.row.isSysvalue == '1'" />
          </template>
        </k-grid-column>
        <k-grid-column data-header="默认用户" data-name="userid" data-width="150">
          <template slot-scope="scope" >
            <k-field-select v-model="scope.row.row.userid"  :data-data="formData.userList"  data-display-field="label" data-value-field="value" :data-multiple="true" :data-allowblank="scope.row.row.isSysvalue == '1'" />
          </template>
        </k-grid-column>

      </k-grid>
      <div style="text-align: right;">
        <k-btn class="btn-custom-primary" :data-handler="saveModColumns" :disabled="showSubmitBtn === false">
          <span v-show="showSubmitBtn" >保存</span>
          <i v-show="!showSubmitBtn" class="el-icon-loading"/>
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
        </k-btn>
      </div>
    </k-popup>

    <!--复核-->
    <k-popup ref="reviewPopup" title="复核">
      <k-form ref="reviewForm" data-ui="element">
        <k-form-item label="模板名称">
          <k-field-text v-model="formData.docName" :data-allowblank="false" :data-max-length="128" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披类型">
          <k-field-select v-model="formData.disclosureType" :data-data="DocTypeDict"   data-value-field="value" data-display-field="text" :data-allowblank="false" :data-disabled="true"
                          @data-on-change="onDocTypeChange"/>
        </k-form-item>
        <k-form-item label="信披子类型" v-if="formData.disclosureType=='5'||formData.disclosureType=='6'">
          <k-field-select v-model="formData.disclosureSonType"  data-dict="xp_son_type"  :data-data="addDocTypeDict" :data-allowblank="false" :data-disabled="true"
          />
        </k-form-item>
        <k-form-item label="文档版本">
          <k-field-text v-model="formData.version" :data-allowblank="false" :data-disabled="true" />
        </k-form-item>
        <k-form-item label="备注" :dataCol=2>
          <k-field-text v-model="formData.remark"  inputType="textarea" :rows="1" :data-max-length="256" :data-hidden="true"/>
        </k-form-item>
        <k-form-item label="审批意见" :dataCol="2">
          <k-field-text v-model="formData.opinion" inputType="textarea"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="POPUP"
                 data-target="printTempVersionGrid"
                 :data-handler="takeEffect">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>生效
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="POPUP"
                 data-target="printTempVersionGrid"  :data-handler="Invalidation">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>不生效
          </k-btn>
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
  name: "M8DisclosureModelVersion",
  data() {
    return {
      fileData: '',
      flowFileData: {},
      DisclosureModColumn: {
        disclosureModVersionId: '',
        $RatGrid: null,
      },
      fileList:[],
      roleList:[],
      childFileList:[],
      fileNameList:[],
      childFileNameList:[],
      printTemp: {
        disclosureType:'',
        modName:''
      },
      docTypeDict: {},
      addDocTypeDict: {},
      formData: {
        userList:[],
        docType: '',
        tempType: '',
        distributorCode: '',
        t8TruteeInfoId: '',
        t8MeetCreateId: '',
        remark: '',
        version: 0,
        onlineUrl: '',
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
        remark:''
      },
      editUploadUrl:'',
      disclosureModId:'',
    };
  },
  created() {
    this.queryUserId();
    this.queryRoleId();
  },
  watch: {
    //信披子类型发生变化
    'roleids'(value) {
      this.$set(this, 'userid', '');
      this.$set(this.formData, 'userList', '');
      this.queryUserId(value);
    },
  },
  methods: {
    flashing(){
      this.$refs.printTempVersionGrid.load();
    },
    queryUserId(value){
      this.httpUtil.comnQuery({
        action: "Role.findAllUser",
        params: value
      }).then(data => {
        if(data.rows.length>0){
          this.formData.userList = data.rows;
          //console.log(this.roleList);
        }
      }).catch({
      });
    },
    queryRoleId(){
      this.httpUtil.comnQuery({
        action: "Role.findAll",
        params: null
      }).then(data => {
        if(data.rows.length>0){
          this.roleList = data.rows;
          //console.log(this.roleList);
        }
      }).catch({
      });
    },
    renderDateTimeCreate(row) {
      return Tools.formatDateTime(row.crtDate, row.crtTime);
    },
    renderDateTimeUpd(row) {
      return Tools.formatDateTime(row.updDate, row.updTime);
    },
    selectPrintTemp(row, column, event) {
      const _this = this
      _this.selectRowData = assign({}, row)
      this.$refs.printTempVersionGrid.load({disclosureModId: row.disclosureModId});
    },
    beforePopupLoad(params) {
      params.disclosureModId = this.$route.query.disclosureModId;
      return params;
    },
    Invalidation() {
      //失效
      this.formData.status=0
      this.httpUtil.comnQuery({
        action: 'DisclosureModVersion.updateXPTempVersionStatus',
        params: this.formData
      }).then(data => {
        if(data.success==true){
          Tools.alert(data.returnmsg)
          //刷新
          this.$refs.printTempVersionGrid.load({disclosureModId:this.formData.disclosureModId})
        }

      });
      //关闭弹窗
      this.$refs.reviewPopup.close()
    },
    takeEffect() {
      //生效
      this.formData.status=1
      this.httpUtil.comnQuery({
        action: 'DisclosureModVersion.updateXPTempVersionStatus',
        params: this.formData
      }).then(data => {
        if(data.success==true){
          Tools.alert(data.returnmsg)
          //刷新
          this.$refs.printTempVersionGrid.load({disclosureModId:this.formData.disclosureModId})

        }

      });

      //关闭弹窗
      this.$refs.reviewPopup.close()
    },
    dataEcho(){

      this.DisclosureModColumn.$RatGrid.load({disclosureModVersionId: this.DisclosureModColumn.disclosureModVersionId});
    },
    versionChangeFnc(val){
      this.DisclosureModColumn.disclosureModVersionId = val.id;

    },
    getDataFlow(val){
      this.formData.version=val.version
      this.formData.docName=val.docName
      this.$set(this.formData,'opinion',val.opinion);
      this.formData.remark=val.remark
      this.formData.disclosureModId=val.disclosureModId
      this.formData.disclosureType = val.disclosureType
      this.formData.disclosureSonType = val.disclosureSonType
      this.formData.id = val.id
      return val;
    },
    // validateData() {
    //   return this.$refs.addForm.validate();
    // },
    httpRequest(file){
      this.fileData.append('files', file.file);
    },
    // validateData2() {
    //   return this.$refs.editPrintTempVersionForm.validate();
    // },
    onDocTypeChange(disclosureType) {

      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: disclosureType}
      }).then(data => {
        this.addDocTypeDict = data.rows;
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

    onChildSubmitSuccess() {
      this.$refs.editPrintTempVersionRef.doReset();
      this.$refs.editPrintTempVersionForm.reset();
      this.$refs.editPrintTempVersionPopup.close();
      this.$refs.printTempVersionGrid.load({disclosureModId: this.$route.query.disclosureModId})
    },

    editHandler(value) {
      this.fileData='';
      this.editFormData.disclosureModId =this.$route.query.disclosureModId;
      this.editFormData.remark='';
      this.httpUtil.comnQuery({
        action: "DisclosureModVersion.getNewestXPVersion",
        params: {disclosureModId: this.editFormData.disclosureModId}
      }).then(data => {
        if(data.rows.length>0){
          let version = data.rows[0].version.split(".");
          this.editFormData.version = data.rows[0].version;
          this.editFormData.docName = data.rows[0].doc_name;
          console.log(data.rows[0])
        }
      }).catch({
      })
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

      this.showEditPrintTempVersionSubmitBtn=true;
      this.$refs.editPrintTempVersionRef.doReset();
      this.$refs.editPrintTempVersionForm.reset();
      this.$refs.editPrintTempVersionPopup.close();
      //this.editUploadUrl='/upload/server/PmsApp/onlineEdit/comparePrintTempVersion.json';
    },
    submitEditPrintTempVersionUploadParam(){
      let urlPath = window.document.location.href;  //浏览器显示地址 http://10.1.20.88:8201/xxx/xxx
      let docPath = "8201"; //服务器相对地址 8201/xxx/xxx
      let index = urlPath.indexOf(docPath);
      let serverPath = urlPath.substring(0, index);
      let onlineUrl = this.httpUtil.onlineUrl;
      if(onlineUrl!="undefined"&&onlineUrl!=null&&onlineUrl!=""){
        this.editFormData.onlineUrl = onlineUrl;
      }else{
        this.editFormData.onlineUrl = serverPath+"8201";
      }
      let editFormData = this.editFormData;
      let temp = document.getElementsByClassName('upload-demo');
      let lis = temp[0].childNodes[1].childElementCount;
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
        let str = '';//（进入审批流且成功或者没有审批流），并且有按钮权限
            let uploadData = this.editFormData;
            this.fileData.append('params', JSON.stringify(uploadData));
            this.httpUtil.upload({
              url:"/upload-files/server/PmsApp/xpdoc/uploadTempVersion.json",
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
            });
      }else{
        Tools.alert("上传附件不能为空!","danger");
        this.showEditPrintTempVersionSubmitBtn = true;
        return false;
      }
    },
    previewPrintTempVersion(value){
      this.httpUtil.comnQuery({
        action:'DisclosureModColumn.getMaxXPVersionId',
        params: {disclosureModVersionId:value.id}
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

      // let urlPath = window.document.location.href;  //浏览器显示地址 http://10.1.20.88:8201/xxx/xxx
      // let docPath = "8201"; //服务器相对地址 8201/xxx/xxx
      // let index = urlPath.indexOf(docPath);
      // let serverPath = urlPath.substring(0, index);
      // let onlineUrl = _this.httpUtil.onlineUrl;
      // if(onlineUrl!="undefined"&&onlineUrl!=null&&onlineUrl!=""){
      //   _this.editFormData.onlineUrl = onlineUrl;
      // }else{
      //   _this.editFormData.onlineUrl = serverPath+"8201";
      // }
      // let fileName = file.name
      // this.childFileList.push(fileName);
      // let suffix = fileName.substr(fileName.lastIndexOf('.') + 1);
      //  if ('docx' != suffix) {
      //    Tools.alert("只能上传格式为docx类型的文档!","danger");
      //    _this.$refs.editPrintTempVersionRef.doReset();
      //    return false;
      //  }
      // if (file.status=='ready' && file.response==null){
      //   this.showEditPrintTempVersionSubmitBtn=false;
      //   let editFormData = _this.editFormData;
      //   this.$refs.editPrintTempVersionSubmitBtn.setIconStyle(1, []);
      //   this.$refs.editPrintTempVersionRef.upload(editFormData);
      // }
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

<style scoped>
.popClass  ::v-deep .el-dialog {margin-right: 20px}

</style>
