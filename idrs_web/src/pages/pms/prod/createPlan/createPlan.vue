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
      <k-form-item label="系列名称">
        <k-field-select v-model="prodSearchParam.seriesName" data-action="T8ProdCreatePlan.getSeriesName"
                        data-display-field="seriesName" data-value-field="seriesName"/>
      </k-form-item>
      <k-form-item label="募集方式">
        <k-field-select v-model="prodSearchParam.raiseType"  data-dict="t8_raise_type" />
      </k-form-item>
      <k-form-item label="是否存在创设方案" data-input-width="194px" data-label-width="150px">
        <k-field-select v-model="prodSearchParam.isExistPlan"  data-dict="is_default" />
      </k-form-item>
      <k-form-item label="产品状态">
        <k-field-select v-model="prodSearchParam.prodStatus"  data-dict="t8_prod_status" />
      </k-form-item>
      <k-form-item label="代码回收">
        <k-field-select v-model="prodSearchParam.isRecycleCode" data-dict="1yes0no"></k-field-select>
      </k-form-item>
    </k-form-search-customize>



    <k-grid ref="prodInfoGrid" data-action="T8ProdCreatePlan.getCreatePlan1" @data-row-select="selectRow"
            :data-params="this.prodSearchParam">
      <k-grid-column data-align="center" data-header="产品id" data-name="id" :data-hidden="true"/>
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="系列名称" data-name="seriesName" />
      <k-grid-column data-align="center" data-header="募集方式" data-name="raiseType"  data-dict="t8_raise_type"/>
      <k-grid-column data-align="center" data-header="文档类型" data-name="documentType"  data-hidden="true"/>
      <k-grid-column data-align="center" data-header="产品状态" data-name="prodStatus" data-dict="t8_prod_status"/>
      <k-grid-column data-align="center" data-header="显示状态" data-name="isShow" :data-hidden="true"/>
      <template slot="operate" slot-scope="scope">
        <!--    scope.row.row.documentType===undefined||scope.row.row.documentType===''||scope.row.row.documentType===null    -->
        <k-btn class="md-info md-just-icon md-simple"  data-descript="生成" data-functype="SUBMIT" data-size="small" :data-disabled="scope.row.row.isShow==='0'" v-show="showGenerate"
               v-if="global.getProdIfUser(scope.row.row.id)&&
               global.isShowAuthorityButton('T8ProdCreatePlan.generateCreatePlanByProdCode')"
               :data-after-success="changeVersion"
               data-action="T8ProdCreatePlan.generateCreatePlanByProdCode" data-target="prodManualGrid" v-model="scope.row.row" :data-confirm="true">
          <md-icon>add_circle</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple"  v-model="scope.row.row" data-descript="上传创设方案"
               data-functype="POPUP" data-size="small" @click="setFileParams(scope.row.row)" data-target="filePopup"
               v-if="global.isShowAuthorityButton('T8ProdCreatePlan.addT8ProdDocumentVersion')">
          <md-icon>backup</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple"  v-model="scope.row.row" data-descript="上传创设材料" :data-handler="addHandler"
               v-if="global.getProdIfUser(scope.row.row.id)&&
               global.isShowAuthorityButton('T8ProdCreatePlan.addCreatePlanAttachment')"
               data-functype="POPUP" data-size="small" data-target="addPopup" v-show="showUpload">
          <md-icon>backup</md-icon>
        </k-btn>
        <!-- <k-btn class="md-info md-just-icon md-simple" :data-download-name="scope.row.row.prodName+'.doc'"
              data-descript="下载创设材料" data-functype="DOWNLOAD" data-size="small"
              data-url="/download/server/PmsApp/createPlan/downAttachment.json" v-model="scope.row.row">
         <md-icon>cloud_download</md-icon>
       </k-btn> -->
        <k-btn data-functype="POPUP" data-confirm data-size="mini"   class="md-info md-just-icon md-simple"
               v-if="global.getProdIfUser(scope.row.row.id)"
               data-target="editAttachmentTable" :data-handler="toParams" data-descript="创设材料附件信息">
          <md-icon>weekend</md-icon>
        </k-btn>
      </template>
    </k-grid>


    <k-grid ref="prodManualGrid" :data-autoload="false" data-action="T8ProdDocumentVersion.findT8ProdDocumentVersionByProdCode">
      <k-grid-column data-align="center" data-header="版本id" data-name="id" />
      <k-grid-column data-align="center" data-header="产品id" data-name="t8ProdInfoId" :data-hidden="true"/>
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="文件名" data-name="fileName"/>
      <k-grid-column data-align="center" data-header="文档版本" data-name="version"/>
      <k-grid-column data-align="center" data-header="文档类型" data-name="documentName" :data-hidden="true"/>
      <k-grid-column data-align="center" data-header="文档类型" data-name="documentType" :data-hidden="true"/>
      <k-grid-column data-align="center" data-header="是否模板文档" data-dict="1yes0no" data-name="isTemplateFile"/>
      <k-grid-column data-align="center" data-header="审批状态" data-dict="task_status" data-name="taskStatus" :data-hidden="true"/>
      <k-grid-column data-align="center" data-header="确认状态" data-dict="confirm_status" data-name="confirmStatus" :data-hidden="false"/>
      <k-grid-column data-align="center" data-header="创建日期" data-type="timestamp" data-name="createDate"/>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" :data-download-name="downFileName(scope.row.row)"
               data-descript="下载创设方案" data-functype="DOWNLOAD" data-size="small"
               data-url="/download/server/PmsApp/prodDocument/downloadOnlineEditT8ProdDocumentVersion.json" v-model="scope.row.row">
          <md-icon>cloud_download</md-icon>
        </k-btn>
        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple"
                 data-descript="在线编辑"
                 data-size="small"
                 :data-disabled="scope.row.row.isTemplateFile === '0'"
                 v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
                 global.isShowAuthorityButton('T8ProdCreatePlan.getT8OnlineWordValueList')"
                 @click="onlineEditHandler(scope.row.row)"
                 data-functype="POPUP"
                 data-target="onlineEditPopup"
                 v-model="scope.row.row">
            <md-icon>edit</md-icon>
          </k-btn>
        </div>
        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple"
                 data-descript="预览" :data-disabled="scope.row.row.isTemplateFile==='0'"
                 data-size="small"  v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)"
                 @click="onlineEditHandler1(scope.row.row)"
                 data-functype="POPUP"
                 data-target="onlineLook"
                 v-model="scope.row.row">
            <md-icon>zoom_in</md-icon>
          </k-btn>
        </div>

        <div style="display:inline-block;" v-show="checkVersion(scope.row.row.version)">
          <k-btn class="md-info md-just-icon md-simple" :data-disabled="checkInitiateLegalTrialStatus(scope.row.row)"
                 data-descript="审批"  v-if="global.getProdIfUser(scope.row.row.t8ProdInfoId)&&
                 global.isShowAuthorityButton('T8ProdCreatePlan.updateTaskStatus')"
                 data-target="prodManualGrid"
                 data-functype="SUBMIT"
                 :data-confirm="true"
                 data-action="T8ProdCreatePlan.updateTaskStatus"
                 data-size="small"
                 v-model="scope.row.row" v-show="showApproval">
            <md-icon>near_me</md-icon>
          </k-btn>
        </div>

      </template>
    </k-grid>
    <k-popup ref="editAttachmentTable" title="管理附件列表" data-width="60%">
      <k-grid ref="editAttachmentGrid"
              data-action="DocumentAttachment.findAttachments"
              @data-row-select="selectRow"
              :data-before-load="beforePopupLoad"
              data-operate-column-position="end"
              data-align="center" data-operate-data-width="300px"
              data-operate-column="true"
              :data-display="false">
        <k-grid-column data-align="center" data-header="id" data-name="id"  data-hidden="true" />
        <k-grid-column data-align="center" data-header="父级id" data-name="parentId" data-hidden="true"/>
        <k-grid-column data-align="center" data-header="附件名称" data-name="fileName"/>
        <k-grid-column data-align="center" data-header="附件类型" data-name="attachment_type" data-hidden="true"/>
        <k-grid-column data-align="center" data-header="创建日期" data-name="crtDate"/>
        <k-grid-column data-align="center" data-header="创建时间" data-name="crtTime"/>
        <template slot="operate" slot-scope="scope">
          <k-btn data-functype="DOWNLOAD" :data-download-name="scope.row.row.fileName" data-confirm data-size="mini"
                 class="md-info md-just-icon md-simple"
                 data-target="prodInfoGrid" data-url="/download/server/PmsApp/createPlan/downAttachment.json" data-descript="下载">
            <md-icon>cloud_download</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple"  data-descript="删除附件" data-functype="SUBMIT"  data-confirm data-type="danger"
                 data-target="editAttachmentGrid"
                 data-action="T8ProdCreatePlan.deleteFile"
                 v-if="global.isShowAuthorityButton('T8ProdCreatePlan.deleteFile')">
            <md-icon>close</md-icon>
          </k-btn>

        </template>
      </k-grid>
    </k-popup>

    <k-popup ref="filePopup" title="上传创设方案">
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
        <k-form-item label="创设方案" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="fileUploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onFileSubmitError" :data-success="onFileSubmitSuccess"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/createPlan/createPlanUpload.json">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="printTempGrid" ref="fileSubmitBtn"
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
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onSubmitError" :data-success="onSubmitSuccess1"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/createPlan/upload.json">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="printTempGrid" ref="submitBtn"
                 data-from="addForm" :data-model="formData" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="onlineEditPopup" data-width="80%" :data-dialog-drag="true" style="margin-left:10%;">
      <div class="edit">
        <div class="word">
          <iframe name="onlineEdit" id="onlineEdit" :src="viewUrl"></iframe>
        </div>
        <div class="form">
          <div>
            <k-btn @click="save" class="btn-custom-primary"
                   data-form="setRoleForm">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>
            <k-btn @click="closePopup" class="btn-custom-plain"
                   data-form="setRoleForm">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
            </k-btn>
          </div>
          <k-form ref="tableFormData" :data-col="1"  style="width: 100%">
            <k-form-item v-for="(item,index) in onlineEditData" :key="index" :label="item.wordComment+':'">
              <k-field-text v-show="item.isDisabled=='1'" style="width: 130%" inputType="textarea" :ref="item.wordKey" @input="itemChange($event,item.wordKey)" v-model="item.wordValue"/>
              <k-field-text v-show="item.isDisabled=='0'" style="width: 130%" inputType="textarea"  :rows="1" :data-disabled="true" :value="item.wordValue"/>
            </k-form-item>
          </k-form>
        </div>
      </div>
    </k-popup>


    <!-- 预览 -->
    <k-popup ref="onlineLook" data-width="80%" :data-dialog-drag="true" style="margin-left:10%;">
      <div class="edit">
        <div class="word" style="width: 100%">
          <iframe name="onlineEdit" id="onlineEdit1" :src="viewUrl"></iframe>
        </div>
        <!--        <div class="form" style="width: 5%">
                <k-btn data-functype="SUBMIT" :data-handler="reset" class="md-primary"
                         data-form="setRoleForm">
                    <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
                  </k-btn>
                </div>-->

      </div>
    </k-popup>

  </div>
</template>

<script>
import {assign} from "lodash";
import KFieldDisplay from "../../../../components/k-element/k-field-display/k-field-display";
import KFormItem from "../../../../components/k-element/k-from/k-form-item";
import KFieldUpload from "../../../../components/k-element/k-field-upload/k-field-upload"
export default {
  name: "createPlan",
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
      addPopupTitle:'',
      onlineEditData:{},
      viewUrl:'',
      queryParentId:'',
      attachmentType:'',
      showGenerate:true,//是否显示生成按钮
      showUpload:true,//是否显示上传创设材料按钮
      showApproval:true,//是否显示审批按钮
    }
  },
  methods:{
    // prodInfoAfter(){
    //   localStorage.setItem("cache",JSON.stringify(this.prodSearchParam))
    //   console.log("查询之后",this.prodSearchParam)
    // },
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
          //console.log("this.lastVersion=>",this.lastVersion);
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
    selectRow(row, column, event){
      const _this = this;
      _this.selectRowData = assign({}, row);
      var documentType='10007,20007,30007,40007,50007,60007,70007,10107';
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
          this.$refs.prodManualGrid.load({prodCode: _this.selectRowData.prodCode,documentType:documentType});
        })
      });

    },
    addHandler(value){
      this.formData.prodCode = value.prodCode;
      this.formData.prodName = value.prodName;
    },
    setFileParams(rows) {
      this.filFormData.documentType = rows.documentType
      this.filFormData.prodCode = rows.prodCode
      this.filFormData.prodName = rows.prodName
    },
    onFileSubmitError(){
      this.$refs.fileUploadRef.doReset();
      this.$refs.fileSubmitBtn.setIconStyle(1, []);
    },
    onFileSubmitSuccess() {
      this.$refs.fileUploadRef.doReset();
      this.$refs.fileForm.reset();
      this.$refs.filePopup.close();
      this.$refs.prodInfoGrid.load();

      // this.httpUtil.comnQuery({
      //   action: 'T8ProdCreatePlan.getCreatePlan1',
      //   params: {}
      // }).then(data => {
      // });
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
      this.$refs.prodManualGrid.load();
    },
    onSubmitSuccess1() {
      this.$refs.uploadRef.doReset();
      this.$refs.addForm.reset();
      this.$refs.addPopup.close();

    },
    submitUploadParam() {
      let formData = this.formData;
      this.$refs.uploadRef.upload(formData);
    },
    downFileName(rows) {
      if (rows.isTemplateFile === '0') {
        return rows.fileName
      } else {
        return rows.prodName+rows.documentName+rows.version+'.docx'
      }
    },
    previewProdDocument(value){
      this.$set(value,"showIcon",true);
      this.httpUtil.ajax({
        url: "/server/form/PmsApp/prodDocument/previewT8ProdDocumentVersion.json",
        params: value
      }).then(data => {
        this.$nextTick(()=>{
          this.$set(value,"showIcon",false);
          let url = data.returndata.url;
          window.open(url,'_blank','width=1000,height=800,toolbars=yes,resizable=yes,scrollbars=yes,left=20,top=30');
        })
      }).catch({
      })
    },
    beforePopupLoad(params){
      params.parentId = this.queryParentId;
      params.attachmentType=this.attachmentType;
      return params;
    },
    toParams : function(row){
      this.attachmentType='13';
      this.queryParentId = row.prodCode;
    },
    checkInitiateLegalTrialStatus(value){
      if (value.confirmStatus == '1'){
        return true;
      }
      // return value.taskStatus != 1;
    },
    save(){
      this.httpUtil.ajax({
        url: '/server/form/PmsApp/onlineEdit/saveProdDocumentVersionWordValue.json',
        params: {onlineEditData: JSON.stringify(this.onlineEditData)},
        successAlert: true
      }).then(res=>{
        this.$refs.onlineEditPopup.close();
        this.$refs.prodManualGrid.load()
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
        action: 'T8ProdCreatePlan.getT8OnlineWordValueList',
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
                  //val = val.replaceAll("\n", "<br/>");
                  /*使用半角空格替换java的空格*/
                  //val = val.replaceAll(" ", "&ensp;");
                  var reg = new RegExp( "\n" , "g" );
                  var reg1 = new RegExp( " " , "g" );
                  val = val.replace(reg, "<br/>");
                  val = val.replace(reg1, "&ensp;");
                  item.innerHTML = val;
                } else {
                  item.innerHTML = data.wordComment;
                }
              })
            }
          }, 3000)
        }
      });
    },
    onlineEditHandler1(value){
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
              document.getElementById("onlineEdit1").contentWindow.document.querySelectorAll("span[name='v_" + data.wordKey + "']").forEach(item => {
                var val = data.wordValue;
                if (val != null && val.trim() !='' && val != 'null'){
                  //将java换行符替换成html换行符
                  //val = val.replaceAll("\n", "<br/>");
                  /*使用半角空格替换java的空格*/
                  //val = val.replaceAll(" ", "&ensp;");
                  var reg = new RegExp( "\n" , "g" );
                  var reg1 = new RegExp( " " , "g" );
                  val = val.replace(reg, "<br/>");
                  val = val.replace(reg1, "&ensp;");
                  item.innerHTML = val;
                } else {
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
    reset(){
      this.$refs.onlineLook.close();
    }
  },
  created() {
    this.global.getProdUser('');
    this.$nextTick(()=>{
      //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
      this.global.getHideButtons(this);
      //接收产品运营导航跳转传参，加载表格数据 rennannan 20210324
      let prodCode = this.$route.query.prod_code;
      if(prodCode !=''&&prodCode!=undefined){
        this.$refs.prodInfoGrid.load({prodCode:prodCode});
      }
    });

    // if(localStorage.getItem("cache")){
    //   var cache = JSON.parse(localStorage.getItem("cache"));
    //   this.prodSearchParam = cache;
    //   console.log("created",this.prodSearchParam)
    // }
  },
  activated() {
    if(this.$route.query.prod_code != null && this.$route.query.prod_code != undefined){
      this.$nextTick(()=>{
        //获取需要隐藏按钮属性  rennannan 20210322   将接收到的属性值置为false达到隐藏按钮的效果
        this.global.getHideButtons(this);
        //接收产品运营导航跳转传参，加载表格数据 rennannan 20210324
        this.prodSearchParam.prodCode = this.$route.query.prod_code;
        this.$refs.prodInfoGrid.load({prodCode:this.prodSearchParam.prodCode});
      });
    }
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
