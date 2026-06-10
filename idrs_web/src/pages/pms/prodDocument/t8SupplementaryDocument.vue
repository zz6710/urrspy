
<template>
  <div>
    <k-form-search-customize data-target="prodInfoGrid" v-model="prodSearchParam">

      <k-form-item label="产品代码">
        <k-field-select v-model="prodSearchParam.prodCode" data-action="T8ProdInfo.findT8ProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"/>
      </k-form-item>
      <k-form-item label="产品名称">
        <k-field-text v-model="prodSearchParam.prodName" data-validate-type="text"/>
      </k-form-item>
      <k-form-item label="是否存在代销协议" v-show="true" data-input-width="194px" data-label-width="150px">
        <k-field-select v-model="prodSearchParam.isHave" data-dict="t8_prod_isok"/>
      </k-form-item>
      <k-form-item label="代码回收">
        <k-field-select v-model="prodSearchParam.isRecycleCode" data-dict="1yes0no"></k-field-select>
      </k-form-item>
    </k-form-search-customize>
    <k-grid ref="prodInfoGrid" data-action="T8ProdSupplementaryDocument.findProdInfo1" @data-row-select="selectRow">
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
       <k-grid-column data-align="center" data-header="id" data-name="id" data-hidden="true"/>
      <k-grid-column data-align="center" data-header="产品状态" data-dict="t8_prod_status" data-name="prodStatus"/>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple"  v-model="scope.row.row" data-descript="添加补充协议" data-functype="POPUP" data-size="small"
               data-target="addPopup" :data-handler="addHandler" v-if="global.getProdIfUser(scope.row.row.id)&&
               global.isShowAuthorityButton('T8ProdSupplementaryDocument.addSupplementaryAgreement')">
          <md-icon>backup</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-grid ref="prodManualGrid" :data-autoload="false"
            data-action="T8ProdSupplementaryDocument.findT8ProdSupplementaryDocumentByProdCode">
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="文件名称" data-name="documentName"/>
      <k-grid-column data-align="center" data-header="文件类型" data-name="documentType" data-dict="document_type_bc"/>
      <k-grid-column data-align="center" data-header="创建人" data-name="crtUserName"/>
      <k-grid-column data-align="center" data-header="创建时间" data-type="date" data-name="crtDate"
                     data-render="renderDateTimeCreate"/>
<!--      <k-grid-column data-align="center" data-header="更新时间" data-type="date" data-name="updDate"-->
<!--                     data-render="renderDateTimeUpdate"/>-->
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple"
               :data-download-name="scope.row.row.prodName+scope.row.row.documentName"
               data-descript="下载补充协议文档" data-functype="DOWNLOAD" data-size="small"
               data-url="/download/server/PmsApp/prodDocument2/downloadT8ProdSupplementaryDocument.json"
               v-model="scope.row.row">
          <md-icon>cloud_download</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-popup ref="addPopup" :title="addPopupTitle">
      <k-form ref="addForm" data-ui="element">
        <k-form-item label="产品代码">
          <k-field-text v-model="formData.prodCode" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="模板ID" v-if="false" data-hidden="true">
          <k-field-text v-model="formData.tempId" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodName" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="补充协议类型">
          <k-field-select v-model="formData.docType" data-dict="document_type_bc" :data-allowblank="false" @data-on-change="docTypeChange"/>
        </k-form-item>

         <k-form-item label="托管行" v-if="formData.docType=='2'">
          <k-field-select v-model="formData.t8TruteeInfoId" :data-data="t8TruteeInfos" data-display-field="truteeName"      :data-params="{'t8ProdInfoId':formData.t8ProdInfoId}"  data-value-field="id"  :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="代销商" v-if="formData.docType=='3'">
          <k-field-select v-model="formData.distributorCode" :data-data="distributors"  data-action="T82001.findDistributorByProdCode" :data-params="{'prodCode':formData.prodCode}"  data-display-field="distributorName"  data-value-field="distributorCode" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="文档版本" v-if="formData.showFlag==true">
          <k-field-text v-model="formData.version"  :data-allowblank="false"   :data-disabled="true"/>
        </k-form-item>

        <k-form-item label="文档id" v-if="formData.showFlag==true" data-hidden="true">
          <k-field-text v-model="formData.documentId"  :data-allowblank="false"  :data-disabled="true" />
        </k-form-item>
        <k-form-item label="备注" data-input-width="590px">
          <k-field-text v-model="formData.remark" :data-max-length="2000" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onSubmitError" :data-success="onSubmitSuccess"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/prodDocument2/uploadT8ProdSupplementaryDocument.json">
          </k-field-upload>
        </k-form-item>
        <span style="margin-left: 130px;color:red;">同名文件上传会覆盖原文件,请悉知!</span>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="printTempGrid" ref="submitBtn"
                 data-from="addForm" :data-model="formData" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>
</template>

<script>
import {assign} from "lodash";
 import Tools from '@/utils/tools.js';
import KFieldUpload from "../../../components/k-element/k-field-upload/k-field-upload"
export default {
  name: "",
  components: {KFieldUpload},
  data() {
    return {
      prodSearchParam:{
        prodCode:''
      },
      formData:{
        prodCode:'',
        prodName:'',
        remark:'',
        docType:'',
        tempId:'',
        version:'',
        t8ProdInfoId:'',
        distributorCode:'',
        documentId:'',
        t8TruteeInfoId:''

      },
      count:0,
      t8TruteeInfos:{},
      distributors:{},
      addPopupTitle:'',
      version:'',
      showFlag:false
    }
  },

  created() {
    this.global.getProdUser('');
    this.$nextTick(()=>{
      let prodCode = this.$route.query.prod_code;
      if(prodCode !=''&&prodCode!=undefined){
        this.$refs.prodInfoGrid.load({prodCode:prodCode});
      }
    });
  },
  methods: {
    renderDateTimeCreate(row) {
      return Tools.formatDateTime(row.crtDate, row.crtTime);
    },
    // renderDateTimeUpdate(row) {
    //   return Tools.formatDateTime(row.updDate, row.updTime);
    // },
    selectRow(row, column, event) {
      const _this = this;
      _this.selectRowData = assign({}, row);
      this.$refs.prodManualGrid.load({
        prodCode: _this.selectRowData.prodCode,
        documentType: _this.selectRowData.documentType,
        upperDocumentId: _this.selectRowData.tempId
      });
      //this.$refs.prodManualGrid.load({prodCode: _this.selectRowData.prodCode,documentType:_this.selectRowData.documentType});
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
    submitUploadParam() {
      /*表单校验*/
      let validateResult = this.$refs.addForm.validate();
      if (validateResult) {
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
          if(this.formData.docType=='2'){
             if(this.count<1){
                Tools.alert("未查托管协议版本，无法上传","danger");
                return false;
             }
          }

          if(this.formData.docType=='3'){
             if(this.count<1){
                Tools.alert("未查补充代销协议版本，无法上传","danger");
                return false;
             }
          }
          if (lis <= 0) {
              Tools.alert("请选择需要上传的文件","danger");
              return fasle;
            }
            let formData = this.formData;
            this.$refs.uploadRef.upload(formData);
      }
    },


    addHandler(value){
       this.formData.docType='';
       this.formData.t8TruteeInfoId='';
       this.formData.distributorCode='';
       this.formData.version='';
       this.formData.documentId='';
        this.formData.remark='';
        this.showFlag=false;
        this.formData.prodCode = value.prodCode;
        this.formData.prodName = value.prodName;
        this.formData.t8ProdInfoId = value.id;
        this.addPopupTitle='上传补充协议';
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

    addLegalTrialHandler(value){
      this.formData.prodCode = value.prodCode;
      this.formData.prodName = value.prodName;
      this.formData.version = value.version;
      this.formData.docType = value.docType;
      this.addPopupTitle='上传补充协议';
    },
    checkUploadLegalTrialStatus(value){
      if (value.confirmStatus == '0'){
        return true;
      }
      return value.taskStatus != 2;
    },
    checkDownLoadLegalTrialStatus(value){
      if (value.confirmStatus == '0'){
        return true;
      }
      return value.taskStatus != 3;
    },
    queryCount(documentType){
      this.count=0;
       this.httpUtil.comnQuery({
            action:"T8ProdSupplementaryDocument.findProdDoc",
            params:{"prodCode":this.formData.prodCode,
                    "documentType":documentType}
          }).then(data => {
            this.count = data.count;
          })
    },
    docTypeChange(){
      if(this.formData.docType=='2'){

         this.httpUtil.comnQuery({
          action:"T82006.findTaCustodianBankByProdCode",
          params:this.formData
        }).then(data => {
          this.t8TruteeInfos = data.rows;
          if(data.rows.length==1){
              this.formData.t8TruteeInfoId = data.rows[0].id;
              var documentType = "10002,20002,30002,40002,50002,60002,70002,10102";
              this.queryCount(documentType);

          }
        })

      }
      if(this.formData.docType=='3'){

        this.httpUtil.comnQuery({
          action:"T82001.findDistributorByProdCode",
          params:this.formData
        }).then(data => {
          this.distributors = data.rows;
          if(data.rows.length==1){
            this.formData.distributorCode = data.rows[0].distributorCode;
            var documentType = "10003,20003,30003,40003,50003,60003,70003,10103";
            this.queryCount(documentType);
          }
        })
      }
    },
     getDocVersion(){
      this.httpUtil.comnQuery({
          action:"ProdDocument.queryDocVersion",
          params:this.formData
        }).then(data => {
          if(data!=null && data!=undefined && data!='' && data !=""){
            if(data.rows.length>=1){
              this.formData.showFlag = true;

              this.$set(this.formData,'version',data.rows[0].version);
              this.$set(this.formData,'documentId',data.rows[0].id);
            }
          }
        })

    },

    checkInitiateLegalTrialStatus(value){
      if (value.confirmStatus == '0'){
        return true;
      }
      return value.taskStatus != 1;
    }
  },

}
</script>

<style scoped>

</style>

