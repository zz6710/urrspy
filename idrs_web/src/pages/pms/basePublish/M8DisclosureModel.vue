<template>
  <div>
    <k-form-search-customize data-model-name="DisclosureModel" data-target="printTempGrid" v-model="printTemp" flashing-target="printTempVersionGrid" flashing-data="disclosureModId">
      <k-form-item label="模板名称">
        <k-field-text v-model="printTemp.modName" :data-max-length="200"/>
      </k-form-item>
      <k-form-item label="信披类型" >
        <k-field-select v-model="printTemp.disclosureType" :data-data="DocTypeDict"   data-value-field="value" data-display-field="text" />
      </k-form-item>
      <k-form-item label="信披子类型" v-if="printTemp.disclosureType=='6' ||  printTemp.disclosureType=='1'||  printTemp.disclosureType=='9'">
        <k-field-select v-model="printTemp.disclosureSonType" data-value-field="value" data-display-field="text" :data-data="printTemp.addDocTypeDict"/>
      </k-form-item>
      <k-btn slot="button" class="btn-custom-primary" style="width: 100px" data-functype="POPUP"  :data-handler="addHandler"
             data-target="addPopup" v-if="global.isShowAuthorityButton('DisclosureMod.upLoadRightControl')">
        <md-icon md-src="/static/svg/add.svg"/>
        上传模板
      </k-btn>
    </k-form-search-customize>
    <k-grid ref="printTempGrid" data-action="DisclosureMod.findDisclosureMods" @data-row-select="selectPrintTemp" data-fixed="right"
             data-operate-width="200px" :data-autoload="true">
      <k-grid-column :data-sortable="true" data-default-sort="DESC" data-align="center" data-header="模板id" data-hidden="true" data-name="id"/>
      <k-grid-column data-align="left" data-header="模板名称" data-name="modName" />
      <k-grid-column data-align="center" data-header="信披类型" data-name="disclosureType" data-dict="xp_doc_type" />
      <k-grid-column data-align="center" data-header="信披子类型" data-dict="xp_son_type" data-name="disclosureSonType" />
      <k-grid-column data-align="center" data-header="备注" data-name="remark"/>
      <k-grid-column data-align="center" data-header="创建日期" data-name="crtDate" data-type="date"  />
      <k-grid-column data-align="center" data-header="创建人" data-name="crtUserName" />
      <k-grid-column data-align="center" data-header="版本号" data-name="version" data-hidden="true" />
      <template slot="operate" slot-scope="scope" >
        <k-btn class="md-info specialClass" data-descript="修改模板名称" data-functype="POPUP" data-size="mini" ref="editButton"
               style="min-width:50px;" data-target="editNamePopup" :data-handler="editHandler" v-if="global.isShowAuthorityButton('DisclosureMod.updateDisclosureModName')" >
          修改</k-btn>
        <k-btn data-functype="POPUP" data-size="mini" class="md-info specialClass" :data-model="scope.row.row.id" data-target="editPrintTempVersionPopup1"
               style="min-width:50px;" data-descript="更新模板" :data-handler="editHandler" v-if="global.isShowAuthorityButton('DisclosureMod.upDateRightControl')">
          更新模板</k-btn>
        <k-btn class="md-danger specialClass" data-descript="删除" data-functype="POPUP" data-size="mini"
               style="min-width:50px;" :data-handler="handleDelBtn" v-if="global.isShowAuthorityButton('DisclosureMod.checkDisclosureMod')" >
          删除</k-btn>
      </template>
    </k-grid>

    <k-grid ref="printTempVersionGrid" :data-autoload="false" data-operate-width="250px"
            data-action="DisclosureModVersion.findDisclosureModVersionsAuth" data-fixed="right" :data-page-size="0">
      <k-grid-column data-align="center" data-header="版本id" data-name="id" :data-sortable="true" data-default-sort="DESC" data-hidden="true" />
      <k-grid-column data-align="center" data-header="对应模板id" data-name="disclosureModId" data-hidden="true" />
      <k-grid-column data-align="left" data-header="模板名称" data-name="modName" data-hidden="true"/>
      <k-grid-column data-align="center" data-header="信披类型" data-name="disclosureType" data-dict="xp_doc_type" data-hidden="true"/>
      <k-grid-column data-align="center" data-header="信披子类型" data-dict="xp_son_type" data-name="disclosureSonType" data-hidden="true"/>
      <k-grid-column data-align="left" data-header="文档名称" data-name="docName" data-width="320"/>
      <k-grid-column data-align="center" data-header="版本号" data-name="version" />
      <k-grid-column data-align="center" data-header="上传日期" data-name="crtDate" data-render="renderDateTimeCreate" />
      <k-grid-column data-align="center" data-header="上传人" data-name="crtUserName" />
      <k-grid-column data-align="center" data-header="备注" data-name="remark" />
      <k-grid-column data-align="center" data-header="状态" data-name="status"  data-dict="xp_status"/>
<!--      <k-grid-column data-align="center" data-header="复核人" data-name="updUserName"  data-hidden="true"/>-->
<!--      <k-grid-column data-align="center" data-header="复核意见" data-name="opinion"  data-hidden="true"/>-->
      <template slot="operate" slot-scope="scope">
<!--        <k-field-bswitch data-on-value="1" data-off-value="0" v-model="scope.row.row.status" data-on-action="DisclosureModVersion.recoverStatus"
                         data-off-action="DisclosureModVersion.stopStatus" :data-params=scope.row. :data-confirm="true" data-on-confirm-info="启用"
                         data-off-confirm-info="停用" :data-after-handler="flashing" v-if="global.isrowShowAuthorityButton('DisclosureMod.turnOnRightControl')"/>-->

        <k-btn class="btn-custom-text specialClass"  data-functype="SUBMIT" data-size="mini" data-action="DisclosureModVersion.recoverStatus"
               style="min-width:50px;" data-target="printTempVersionGrid" :data-confirm="true" v-if="scope.row.row.status == '0' && global.isShowAuthorityButton('DisclosureMod.turnOnRightControl')  ">
          启用
        </k-btn>
        <k-btn class="btn-custom-text specialClass" data-functype="SUBMIT" data-action="DisclosureModVersion.stopStatus" v-if="scope.row.row.status == '1' && global.isShowAuthorityButton('DisclosureMod.turnOnRightControl')  "
               style="min-width:50px;" :data-confirm="true" data-size="mini" data-type="danger" data-target="printTempVersionGrid" >
          停用
        </k-btn>
        <k-btn class="btn-custom-text specialClass" :data-download-name="scope.row.row.docName"  data-descript="下载文档模板信息" data-functype="DOWNLOAD" data-size="mini"
               style="min-width:50px;" data-url="/download/server/PmsApp/print/downloadXPTempVersion.json" v-model="scope.row.row" v-if="global.isShowAuthorityButton('DisclosureMod.downLoadRightControl')">
          下载
        </k-btn>
<!--        <k-btn class="btn-custom-text" ref="previewRef" data-descript="预览文档模板信息" data-size="mini"-->
<!--               :data-handler="previewPrintTempVersion" v-model="scope.row.row">-->
<!--          预览-->
<!--        </k-btn>-->
        <k-btn class="btn-custom-text specialClass" data-descript="模板字段维护" data-functype="POPUP" data-size="small"
               style="min-width:50px;" data-target="xpModWordUpdate" :data-handler="versionChangeFnc" v-if="global.isShowAuthorityButton('DisclosureMod.geXPbyModIdRightControl')">
          字段维护
        </k-btn>
        <k-btn class="btn-custom-text specialClass" data-descript="删除" data-functype="POPUP" data-size="mini"
               style="min-width:50px;" :data-handler="btnHandleDel" v-if="global.isShowAuthorityButton('DisclosureModVersion.checkDisclosureMod')">
          删除</k-btn>

      </template>
    </k-grid>


    <k-popup ref="xpModWordUpdate" title="模板字段维护"  @data-opened="dataEcho" :data-dialog-drag="true" data-width="900px">
      <k-grid ref="updateGrid"  data-action="DisclosureModColumn.geXPbyModId" :data-page-size="0" data-height="500px" data-operate-column="false"
              @init="(grid)=>{this.DisclosureModColumn.$RatGrid = grid}" data-display="false" style="height: 600px; overflow: auto;">
        <k-grid-column data-header="id" data-name="id" data-width="20" :data-sortable="true" data-default-sort="DESC" data-hidden="true" ></k-grid-column>
        <k-grid-column data-header="字段描述" data-name="columnLabel" data-width="150"></k-grid-column>
        <k-grid-column data-header="字段key" data-name="columnKey" data-width="150"></k-grid-column>
        <k-grid-column data-header="字段默认值" data-name="columnValue" data-width="150"></k-grid-column>
        <k-grid-column data-header="是否显示" data-name="isdisplay"  data-dict="xp_if_ok" data-width="150">
          <template slot-scope="scope" >
            <k-field-select v-model="scope.row.row.isdisplay" data-dict="xp_if_ok"
                            :data-default-value="scope.row.row.isdisplay == null || scope.row.row.isdisplay == '' ? '1' : scope.row.row.isdisplay"
                            :data-allowblank="false"/>
          </template>
        </k-grid-column>
        <k-grid-column data-header="取值类型" data-name="isSysvalue" data-width="150">
          <template slot-scope="scope" >

            <k-field-select v-model="scope.row.row.isSysvalue" data-dict="xp_is_sysvalue"
                            :data-default-value="scope.row.row.isSysvalue == null || scope.row.row.isSysvalue == '' ? '1' : scope.row.row.isSysvalue"
                            :data-allowblank="false" />
          </template>
        </k-grid-column>
        <k-grid-column data-header="负责角色" data-name="roleids" data-width="150" >
          <template slot-scope="scope" v-if="scope.row.row.isSysvalue =='2'" :data-allowblank="scope.row.row.isSysvalue !=='2'">
            <k-field-select v-model="scope.row.row.roleids"  :data-data="roleList"  data-display-field="label" data-value-field="value"
                            @data-on-change="queryUserId"/>
          </template>
        </k-grid-column>
        <k-grid-column data-header="默认用户" data-name="userid" data-width="150"  v-if="false">
          <template slot-scope="scope" v-if="false" >
            <k-field-select v-model="scope.row.row.userid"  :data-data="formData.userList"  data-display-field="label" data-value-field="value" :data-default-value="''"/>
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

    <k-popup ref="editPrintTempVersionPopup1" title="上传文档模板子模版">
      <k-form ref="editPrintTempVersionForm" data-ui="element" v-model="editFormData" :isFormBodyScreen="true">
        <k-form-item label="模板名称">
          <k-field-text v-model="formData.modName" :data-allowblank="false" :data-max-length="128"
                        :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披类型">
          <k-field-select v-model="formData.disclosureType" data-dict="xp_doc_type" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披子类型" v-if="formData.disclosureType=='6'|| formData.disclosureType=='1'|| formData.disclosureType=='9'">
          <k-field-select v-model="formData.disclosureSonType"  data-dict="xp_son_type" :data-allowblank="false"
                           :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="版本号">
          <k-field-text v-model="formData.version" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="备注" :data-col="2">
          <k-field-text v-model="formData.remark" inputType="textarea" :data-allowblank="true" :data-max-length="256"/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload v-show="true" data-type="file" ref="editPrintTempVersionRef" :data-multiple="false" :data-limit=1
                          :data-error="onEditPrintTempVersionSubmitError" :data-success="onEditPrintTempVersionSubmitSuccess"
                           :dataHttpRequest="httpRequest" :dataChange="onEditPrintTempVersionChange"
                          :data-auto-upload="false" :data-upload-url="editUploadUrl" >
          </k-field-upload>
<!--          data-accept=".docx"-->
        </k-form-item>
      </k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="printTempVersionGrid" ref="editPrintTempVersionSubmitBtn"
                 data-from="editPrintTempVersionForm" :data-model="editFormData" @click="submitEditPrintTempVersionUploadParam">
            <span v-show="this.showEditPrintTempVersionSubmitBtn1">确定</span>
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
    </k-popup>


    <k-popup ref="addPopup" title="上传文档模板">
      <k-form ref="addForm" data-ui="element" :isFormBodyScreen="true">
        <k-form-item label="模板名称" >
          <k-field-text v-model="formData.modName" :data-allowblank="false" :data-max-length="128"
                        />
        </k-form-item>
        <k-form-item label="信披类型">
          <k-field-select v-model="formData.disclosureType" :data-data="DocTypeDict"   data-value-field="value" data-display-field="text" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="信披子类型" v-if="formData.disclosureType=='6'|| formData.disclosureType=='1'|| formData.disclosureType=='9'">
          <k-field-select v-model="formData.disclosureSonType"  :data-data="formData.addDocTypeDict" :data-allowblank="false"
                          data-value-field="value" data-display-field="text" />
        </k-form-item>
        <k-form-item label="版本号">
          <k-field-text v-model="formData.version" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="备注" :dataCol=2>
          <k-field-text v-model="formData.remark"  inputType="textarea" :rows="1" :data-max-length="256"/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onSubmitError" :data-success="onSubmitSuccess" :dataChange="onUploadChange"
                          :dataHttpRequest="httpRequest"
                          :data-auto-upload="false">
          </k-field-upload>
<!--          data-accept=".docx"-->
        </k-form-item>

      </k-form>
        <k-form-footer data-align="center">

          <k-btn class="btn-custom-primary" ref="submitBtn" data-target="printTempGrid" data-functype="SUBMIT"  data-from="addForm" :data-model="formData" @click="submitUploadParam" :data-handler="validateForm" :data-disabled="!showSubmitBtn">
            <span v-show="showSubmitBtn">确定</span>

          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
    </k-popup>



    <!--    修改模板名称弹出框   -->
    <k-popup ref="editNamePopup" data-title="修改" >
      <k-form ref="editNameForm" :data-col="2" :isFormBodyScreen="true">
        <k-form-item label="id" v-show="false">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="模板名称">
          <k-field-text v-model="formData.modName" :data-disabled="false" :data-allowblank="false" :data-max-length="255"/>
        </k-form-item>
        <k-form-item label="信披类型">
          <k-field-select v-model="formData.disclosureType" data-dict="xp_doc_type" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="信披子类型"  v-if="formData.disclosureType=='6'|| formData.disclosureType=='1'|| formData.disclosureType=='9'">
          <k-field-select ref="sonType" v-model="formData.disclosureSonType" data-dict="xp_son_type" :data-allowblank="false"
                          :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="备注" :dataCol=2>
          <k-field-text v-model="formData.remark"  inputType="textarea" :rows="1" :data-max-length="256"/>
        </k-form-item>

      </k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  data-from="editNameForm" data-action="DisclosureMod.updateDisclosureModName"
                 :data-model="formData" data-target="printTempGrid" >
            <span v-show="this.showEditPrintTempVersionSubmitBtn">确定</span>
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
    </k-popup>
  </div>
</template>

<script>
import {assign} from "lodash";
import openWindow from "../../../utils/openWindow";
import Tools from "@/utils/tools";

export default {
  name: "M8DisclosureModel",
  data() {
    return {
      fileData: new FormData(),
      DisclosureModColumn:{
        disclosureModVersionId:'',
        $RatGrid: null,
      },
      flashingData: 'disclosureModId',
      fileList:[],
      DocTypeDict:{},
      roleList:[],
      childFileList:[],
      fileNameList:[],
      printTemp: {
        disclosureType:'',
        modName:'',
        disclosureModId:'-1',
        addDocTypeDict: {},
      },
      nextVersion: {},
      formData: {
        userList: {},
        disclosureModId:'',
        remark: '',
        version: '',
        onlineUrl: '',
        isShareSort: '',
        addDocTypeDict: {},
      },
      showSubmitBtn:true,
      showEditPrintTempVersionSubmitBtn:true,
      showEditPrintTempVersionSubmitBtn1:true,
      editFormData:{
        disclosureModId:'',
        t8PrintTempId:'',
        version:'',
        onlineUrl:'',
        remark:'',
      },
      editUploadUrl:'',
    };
  },
  watch: {
    'printTemp.disclosureType'() {
      this.$set(this.printTemp, 'disclosureSonType', '');
      this.$set(this.printTemp, 'addDocTypeDict', '');
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: this.printTemp.disclosureType}
      }).then(data => {
        this.printTemp.addDocTypeDict = data.rows;
      }).catch({})
    },
    'formData.disclosureType'() {
      this.$set(this.formData, 'disclosureSonType', '');
      this.$set(this.formData, 'addDocTypeDict', '');
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: this.formData.disclosureType}
      }).then(data => {
        this.formData.addDocTypeDict = data.rows;
      }).catch({})
    },
  },
  created() {
    this.xpType();
    this.queryRoleIdId();
  },
  methods: {
    querySonType() {
      this.$set(this.editFormData, 'addDocTypeDict', '');
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPTypeByDocType",
        params: {disclosureType: this.formData.disclosureType}
      }).then(data => {
        this.editFormData.addDocTypeDict = data.rows;
      }).catch({})
    },
    //筛选需要展示的类型字典
    xpType() {
      this.httpUtil.comnQuery({
        action: "DisclosureMod.getXPType",
        params: null
      }).then(data => {
        this.DocTypeDict = data.rows;
      }).catch({})
    },
    queryUserId(value){
      this.$set(this.formData, 'userList', '');
      if (value == null||value==''){
        value = '-1';
      }
      this.httpUtil.comnQuery({
        action: "Role.findAllUser",
        params: {roleid: value}
      }).then(data => {
        if(data.rows.length>0){
          this.formData.userList = data.rows;
        }
      }).catch({
      });
    },
    queryRoleIdId(value){
      this.httpUtil.comnQuery({
        action: "Role.findAll",
        params: null
      }).then(data => {
        if(data.rows.length>0){
          this.roleList = data.rows;
        }
      }).catch({
      })
    },
    onEditPrintTempVersionSubmitError(){
      this.$refs.editPrintTempVersionRef.doReset();
      this.showEditPrintTempVersionSubmitBtn1=true;
      this.$refs.editPrintTempVersionSubmitBtn.setIconStyle(1)
    },
    onEditPrintTempVersionSubmitSuccess(response,file){
      this.$refs.editPrintTempVersionSubmitBtn.setIconStyle(1)
      this.showEditPrintTempVersionSubmitBtn1=true;
      this.$refs.editPrintTempVersionRef.doReset();
      this.$refs.editPrintTempVersionForm.reset();
      this.$refs.editPrintTempVersionPopup1.close();
      //this.editUploadUrl='/upload/server/PmsApp/onlineEdit/comparePrintTempVersion.json';
    },
    editHandler(value) {
      this.$nextTick(function () {
        this.xpType();
      })
      this.httpUtil.comnQuery({
        action:'DisclosureModVersion.getMaxXPVersion',
        params: {disclosureModId:value.id}
      }).then(data => {
            this.nextVersion = data.rows;
            this.$set(this.formData,'version',data.rows[0].version);
      }).catch({
      })
      this.$set(this.formData,'id',value.id);
      this.$set(this.formData,'modName',value.modName);
      this.$set(this.formData,'disclosureType',value.disclosureType);
      this.$set(this.formData,'disclosureSonType',value.disclosureSonType);
      this.$set(this.formData,'remark',value.remark);
    },
    saveModColumns(){
      this.showSubmitBtn = true
      this.httpUtil.comnUpdate({
        action: "DisclosureModVersion.updateModColumns",
        params: {
          datas: JSON.stringify(this.DisclosureModColumn.$RatGrid.list)
        },
        mask: true
      }).then(data => {
        this.showSubmitBtn = true
        if(data.success){
          this.$refs.xpModWordUpdate.close();
          this.$refs.printTempGrid.load();
        }
      });
    },
    dataEcho(){
      this.DisclosureModColumn.$RatGrid.load({disclosureModVersionId: this.DisclosureModColumn.disclosureModVersionId});
    },
    flashing(){
        this.$refs.printTempVersionGrid.load({disclosureModId: this.formData.id});
    },
    btnHandleDel(row) {
      this.$confirm("确认删除吗？", "操作提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
            this.httpUtil.comnQuery({
              //查询该模板版本是否绑定规则
              action: 'DisclosureModVersion.checkDisclosureMod',
              params: {id: row.id}
            }).then(data=> {
              if (data.success) {
                Tools.alert(data.returnmsg);
                this.$refs.printTempVersionGrid.load({disclosureModId: row.disclosureModId});
              } else {
              }
            })
          })
          .catch(() => {
          });
    },
    versionChangeFnc(val){
      this.DisclosureModColumn.disclosureModVersionId = val.id;
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
    },
    submitEditPrintTempVersionUploadParam(row){
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
      this.editFormData.remark = this.formData.remark;
      this.editFormData.disclosureModId = this.formData.id;
      let editFormData = this.editFormData;
      let temp = document.getElementsByClassName('upload-demo');
      let lis = temp[0].childNodes[1].childElementCount;
      if(lis>0){
        this.fileNameList = [];
        for(let i in this.childFileList){
          this.fileNameList.push(this.childFileList[i]);
        }
        this.editFormData['fileNameList'] = JSON.stringify(this.fileNameList);
        let str = '';
        let uploadData = this.editFormData;
        this.fileData.delete('params');
        this.fileData.append('params', JSON.stringify(uploadData));
        this.httpUtil.upload({
          url:"/upload-files/server/PmsApp/xpdoc/uploadTempVersion.json",
          formData: this.fileData
        }).then(res=>{
          this.showEditPrintTempVersionSubmitBtn1 = true;
          this.fileList=[];
          if(res.data.success){
            str = res.data.returnmsg;
            Tools.alert(str);
            this.$refs.editPrintTempVersionPopup1.close();
            this.$refs.printTempVersionGrid.load({disclosureModId: this.formData.id});
          }else{
            str = res.data.returnmsg;
            Tools.alert(str,'danger');
            this.$refs.editPrintTempVersionSubmitBtn.setIconStyle(1, []);
          }
        })
      }else{
        this.fileList=[];
        Tools.alert("上传附件不能为空!","danger");
        this.showEditPrintTempVersionSubmitBtn1 = true;
        this.$refs.editPrintTempVersionSubmitBtn.setIconStyle(1, []);
        return false;
      }
    },
    handleDelBtn(row) {
      this.$confirm("确认删除吗？", "操作提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
            this.httpUtil.comnQuery({
              //查询该模板是否绑定规则
              action: 'DisclosureMod.checkDisclosureMod',
              params: {id: row.id}
            }).then(data=> {
              if (data.success) {
                Tools.alert(data.returnmsg);
                this.$refs.printTempGrid.load({disclosureType:this.printTemp.disclosureType,disclosureSonType:this.printTemp.disclosureSonType});
                this.$refs.printTempVersionGrid.load({disclosureModId: this.selectRowData.id});
              } else {
              }
            })
          })
          .catch(() => {
          });
    },
    renderDateTimeCreate(row) {
      return Tools.formatDateTime(row.crtDate, row.crtTime);
    },
    httpRequest(file){
      const _this = this
      _this.fileList=[];
      _this.fileData.delete('files');
      _this.fileData.append('files', file.file);
      _this.fileList.push(file.filename);
    },
    // onDocTypeChange(disclosureType) {
    //   //刷新查询框子类型字典值
    //   this.$set(this.printTemp, 'disclosureSonType', '');
    //   this.$set(this.formData, 'disclosureSonType', '');
    //   this.httpUtil.comnQuery({
    //     action: "DisclosureMod.getXPTypeByDocType",
    //     params: {disclosureType: disclosureType}
    //   }).then(data => {
    //     this.addDocTypeDict = data.rows;
    //     console.log(data.rows);
    //   }).catch({})
    // },
    validateForm(){
      var validate = this.$refs.addForm.validate();
      if(validate==false){
        return false;
      }
      if (validate) {
        let formData = this.formData;
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if(lis>0){
          this.$refs.uploadRef.upload(formData);
        }else{
          // Tools.alert("上传文件不能为空!","danger");
          this.showSubmitBtn=true;
          // this.$refs.editPrintTempVersionSubmitBtn.setIconStyle(1, []);
          return false;
        }
      }
    },
    submitUploadParam() {
      this.$refs.submitBtn.setIconStyle(0);
      var validate = this.$refs.addForm.validate();
      if(validate==false){
        this.$refs.submitBtn.setIconStyle(1);
        return false;
      }
      let urlPath = window.document.location.href;  //浏览器显示地址 http://10.1.20.88:8201/xxx/xxx
      let docPath = "8201"; //服务器相对地址 8201/xxx/xxx
      let index = urlPath.indexOf(docPath);
      let serverPath = urlPath.substring(0, index);
      let onlineUrl = this.httpUtil.onlineUrl;
      if(onlineUrl!="undefined"&&onlineUrl!=null&&onlineUrl!=""){
        this.formData.onlineUrl = onlineUrl;
      }else{
        this.formData.onlineUrl = serverPath+"8201";
      }

      this.fileNameList = [];
      if(this.fileList==null || this.fileList.length<=0){
        Tools.alert("上传附件不能为空!","danger");
        this.$refs.submitBtn.setIconStyle(1);
        return false;
      }

      /*for(let i in this.fileList){
        this.fileNameList.push(this.fileList[i].name);
      }
      this.formData['fileNameList'] = JSON.stringify(this.fileNameList);*/
      //this.showSubmitBtn = false;
      //this.$set(this.formData,'tempName',this.fileNameList[0].substring(0,this.fileNameList[0].lastIndexOf(".")));
      let uploadData = this.formData;
      //this.fileData = new FormData();
      this.$refs.uploadRef.upload();
      this.fileData.delete('params');
      this.fileData.append('params', JSON.stringify(uploadData));
      this.httpUtil.upload({
        url:"/upload-files/server/PmsApp/xpdoc/uploadTemp.json",
        formData: this.fileData
      }).then(res=>{
        this.showSubmitBtn = true;
        this.$refs.submitBtn.setIconStyle(1);
        if(res.data.success){
          Tools.alert(res.data.returnmsg);
          this.onSubmitSuccess()
        }else{
          this.$refs.submitBtn.setIconStyle(1);
          this.showSubmitBtn = true;
          if (res.data.returnmsg!==''&&res.data.returnmsg!==null&&res.data.returnmsg!==undefined){
            Tools.alert(res.data.returnmsg,"danger");
          }else {
            Tools.alert("上传文件失败！","danger");
          }
          //this.$refs.submitBtn.setIconStyle(1);
        }
      })
    },
    onSubmitError() {
      this.fileList=[];
      this.fileNameList = [];
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1);
      this.showSubmitBtn = true;
    },
    onSubmitSuccess() {
      this.fileList=[];
      this.fileNameList = [];
      this.$refs.uploadRef.doReset();
      this.$refs.addForm.reset();
      this.$refs.addPopup.close();
      this.$refs.submitBtn.setIconStyle(1);
    },
    addHandler() {
      this.xpType();
      this.$set(this.formData,"modName","");
      this.$set(this.formData,"disclosureType","");
      this.$set(this.formData,"remark","");
      this.formData.version = 'V1.0';
    },
    selectPrintTemp(row, column, event) {
      const _this = this;
      _this.selectRowData = assign({}, row);
      _this.formData = assign({}, row);
      this.$refs.printTempVersionGrid.load({disclosureModId: _this.selectRowData.id});
    },
    onUploadChange(file,fileList){
      this.fileData.delete("files");
      this.fileData.append('files', file.file);
      console.log("this.fileList=:>>>",fileList);
      this.fileList = fileList;
      this.fileNameList = [];
      for(let i in this.fileList){
        this.fileNameList.push(this.fileList[i].name);
      }
      if (this.formData.modName ===''||this.formData.modName== null){
        this.$set(this.formData,'modName',this.fileNameList[0].substring(0,this.fileNameList[0].lastIndexOf(".")));
      }
    },
  }
}
</script>

<style scoped>
.popClass  ::v-deep .el-dialog {margin-right: 20px}
>>> .el-table__cell {
  padding: 0px 0 !important;
}
>>> .specialClass > .md-ripple{
  padding: 8px !important;
}
</style>
