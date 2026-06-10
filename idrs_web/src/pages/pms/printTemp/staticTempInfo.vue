<template>
  <div>
    <k-form-search-customize data-target="staticTempGrid" v-model="staticTemp">
      <k-form-item label="文档名称">
        <k-field-text v-model="staticTemp.tempName"/>
      </k-form-item>
     <!-- <k-form-item label="文档类型">
        <k-field-select :data-disabled="true" v-model="staticTemp.docType" dava-default-value="9" data-dict="t8_print_doc" />
      </k-form-item>-->
      <k-form-item label="模板类型">
        <k-field-select v-model="staticTemp.tempType"  data-dict="t8_temp_type_qt"/>
      </k-form-item>
      <k-btn slot="button" style="width: 100px"  data-functype="POPUP" class="btn-custom-primary" :data-handler="addHandler"
             data-target="addPopup"
             v-if="global.isShowAuthorityButton('StaticTemp.saveStaticTempAndStaticTempVersion')">
        <md-icon md-src="/static/svg/add.svg"/>
        上传静态文档
      </k-btn>
    </k-form-search-customize>

    <k-grid ref="staticTempGrid" data-action="StaticTemp.getStaticTempList1" @data-row-select="selectStaticTemp">

      <k-grid-column data-align="center" data-header="文档类型" data-name="docType" data-dict="t8_print_doc"/>
      <k-grid-column data-align="center" data-header="模板类型" data-dict="t8_temp_type_qt" data-name="tempType"/>
      <k-grid-column data-align="center" data-header="文档名称" data-name="tempName"/>
      <k-grid-column data-align="center" data-header="托管行/销售商" data-name="distributorTruteeName"/>
      <k-grid-column data-align="center" data-header="备注" data-name="remark"/>
      <k-grid-column data-align="center" data-header="上传日期" data-type="date" data-name="createDate"/>
      <k-grid-column data-align="center" data-header="上传时间" data-type="time" data-name="createTime"/>
      <k-grid-column data-align="center" data-header="创建人" data-name="createUserName"/>
      <template slot="operate">
        <k-btn class="md-info md-just-icon md-simple" data-descript="更新版本" data-functype="POPUP" data-size="small"
               data-target="editPopup" :data-handler="editHandler"
               v-if="global.isShowAuthorityButton('StaticTemp.saveStaticTempVersion')">
          <md-icon>edit</md-icon>
        </k-btn>
      </template>
    </k-grid>


    <k-grid ref="staticTempVersionGrid" :data-autoload="false"
            data-action="StaticTempVersion.getStaticTempVersionByTempId">
      <k-grid-column data-align="center" data-header="版本id" data-name="id" data-hidden="true" />
      <k-grid-column data-align="center" data-header="对应模板id" data-name="t8StaticTempId" data-hidden="true" />
      <k-grid-column data-align="center" data-header="文档名称" data-name="tempName"/>
      <k-grid-column data-align="center" data-header="文档版本编号" data-name="version"/>
      <k-grid-column data-align="center" data-header="备注" data-name="remark"/>
      <k-grid-column data-align="center" data-header="创建人" data-name="createUserName"/>
      <k-grid-column data-align="center" data-header="生效状态" data-name="status" data-dict="t8_print_temp_version_status"/>
      <k-grid-column data-align="center" data-header="上传日期" data-type="date" data-name="createDate"/>
      <k-grid-column data-align="center" data-header="上传时间" data-type="time" data-name="createTime"/>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" :data-disabled="scope.row.row.status=='1'" data-descript="静态文档生效" data-functype="SUBMIT" data-size="small"
               data-action="StaticTempVersion.updateStaticTempVersionStatus" data-target="printTempVersionGrid" v-model="scope.row.row" :data-confirm="true"
               v-if="global.isShowAuthorityButton('StaticTempVersion.updateStaticTempVersionStatus')">
          <md-icon>done</md-icon>
        </k-btn>


        <k-btn class="md-info md-just-icon md-simple"  data-descript="作废" data-functype="SUBMIT" data-size="small"
               data-action="StaticTempVersion.deleteTempVersion" data-target="printTempVersionGrid" v-model="scope.row.row" :data-confirm="true"
               v-if="global.isShowAuthorityButton('StaticTempVersion.deleteTempVersion')">
          <md-icon>close</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" :data-download-name="scope.row.row.tempName"  data-descript="下载静态文档信息" data-functype="DOWNLOAD" data-size="small"
               data-url="/download/server/PmsApp/print/downloadStaticTempVersion.json" v-model="scope.row.row">
          <md-icon>cloud_download</md-icon>
        </k-btn>
      </template>
    </k-grid>


    <k-popup ref="addPopup" title="上传静态文档">
      <k-form ref="addForm" data-ui="element">
        <k-form-item label="文档类型">
          <k-field-select v-model="formData.docType" :data-allowblank="false" data-default-value="9"
                          data-dict="t8_print_doc" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="模板类型">
          <k-field-select v-model="formData.tempType" :data-allowblank="false"
                          data-dict="t8_temp_type_qt"/>
        </k-form-item>
        <k-form-item label="销售商信息"
                     v-if="formData.tempType=='90002'">
          <k-field-select v-model="formData.distributorCode" data-action="T8Dict.findTaDistributorInfos"
                          data-display-field="distributorName"  data-value-field="distributorCode"
                          :data-allowblank="!(formData.tempType == 90002)" />
        </k-form-item>
        <k-form-item label="托管行信息"
                     v-if="formData.tempType=='90001'">
          <k-field-select v-model="formData.t8TruteeInfoId" data-action="T82006.findTaCustodianBanks"
                          data-display-field="truteeName"  data-value-field="id"
                          :data-allowblank="!(formData.tempType == 90001)"/>
        </k-form-item>
        <k-form-item label="文档版本">
          <k-field-text v-model="formData.version" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="备注" data-input-width="590px">
          <k-field-text v-model="formData.remark" :data-max-length="255" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onSubmitError" :data-success="onSubmitSuccess"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/print/uploadStaticTemp.json">
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

    <k-popup ref="editPopup" title="上传静态文档子模板">
      <k-form ref="editForm" data-ui="element" v-model="editFormData">
        <k-form-item label="上一版本">
          <k-field-text v-model="editFormData.version" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="文档名称">
          <k-field-text v-model="editFormData.tempName" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="文档类型">
          <k-field-select v-model="editFormData.docType" data-dict="t8_print_doc" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="模板类型">
          <k-field-select v-model="editFormData.tempType" data-dict="t8_temp_type" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="销售商信息"
                     v-show="editFormData.tempType=='90002'">
          <k-field-select v-model="editFormData.distributorCode" :data-disabled="true" data-action="T8Dict.findTaDistributorInfos"
                          data-display-field="distributorName"  data-value-field="distributorCode"  />
        </k-form-item>
        <k-form-item label="托管行信息"
                     v-show="editFormData.tempType=='90001'">
          <k-field-select v-model="editFormData.t8TruteeInfoId" :data-disabled="true" data-action="T82006.findTaCustodianBanks"
                          data-display-field="truteeName"  data-value-field="id"  />
        </k-form-item>
        <k-form-item label="备注" data-input-width="590px">
          <k-field-text v-model="editFormData.remark" :data-max-length="255" inputType="textarea" :rows="1"/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="editUploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onEditSubmitError" :data-success="onEditSubmitSuccess" :data-allowblank="false"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/print/uploadStaticTempVersion.json">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  ref="editSubmitBtn"
                 data-from="editForm" :data-model="editFormData" :data-handler="submitEditUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>
</template>

<script>
  import {assign} from "lodash";
  import Tools from "@/utils/tools";

  export default {
    name: "staticTempInfo",
    data() {
      return {
        staticTemp:{
          tempName:'',
          docType:'',
          tempType:''
        },
        formData:{
          docType:'',
          tempType:'',
          t8TruteeInfoId:'',
          distributorCode:'',
          remark:'',
          version:'0',
        },
        editFormData:{
          version:'',
          remark:'',
          tempName:'',
          docType:'',
          tempType:'',
          t8StaticTempId:''
        }

      }
    },
    methods:{
      validateData() {
        return this.$refs.addForm.validate();
      },
      onDocTypeChange() {
        this.staticTemp.tempType = '';
        this.httpUtil.comnQuery({
          action: "PrintTemp.getTempTypeByDocType",
          params: {docType: this.staticTemp.docType}
        }).then(data => {
          this.docTypeDict = data.rows;
        }).catch({})
      },
      selectStaticTemp(row, column, event) {
        const _this = this;
        _this.selectRowData = assign({}, row);
        _this.formData = assign({}, row);
        this.$refs.staticTempVersionGrid.load({t8StaticTempId: _this.selectRowData.id});
      },
      addHandler() {
        this.formData.docType = '';
        this.formData.tempType = '';
        this.formData.remark = '';
        this.formData.distributorCode = '';
        this.formData.t8TruteeInfoId = '';
        this.formData.version = 0;
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
      onSubmitError() {
        this.$refs.uploadRef.doReset();
        this.$refs.submitBtn.setIconStyle(1, []);
      },
      onSubmitSuccess() {
        this.$refs.uploadRef.doReset();
        this.$refs.addForm.reset();
        this.$refs.addPopup.close();
        this.$refs.staticTempGrid.load();
      },
      submitUploadParam() {
        let formData = this.formData;
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        let flag = this.validateData();
        //console.log("flag = >>>>>",flag);
        if(flag){
          if(lis>0){
            this.$refs.uploadRef.upload(formData);
          }else{
            Tools.alert("上传附件不能为空!","danger");
            return false;
          }
        }else{
          //Tools.alert("请输入必填项!","danger");
          return false;
        }
      },
      onEditSubmitError(){
        this.$refs.editUploadRef.doReset();
        this.$refs.editSubmitBtn.setIconStyle(1, [])
      },
      onEditSubmitSuccess(){
          this.$refs.editUploadRef.doReset();
          this.$refs.editForm.reset();
          this.$refs.editPopup.close();
          this.$refs.staticTempVersionGrid.load();
      },
      submitEditUploadParam(){
        let editFormData = this.editFormData;
        this.$refs.editUploadRef.upload(editFormData);
      },
      editHandler(value){
        this.editFormData.t8StaticTempId =value.id;
        this.editFormData.docType=value.docType;
        this.editFormData.tempType=value.tempType;
        this.editFormData.tempName=value.tempName;
        this.editFormData.distributorCode=value.distributorCode;
        this.editFormData.t8TruteeInfoId=value.t8TruteeInfoId;
        this.editFormData.remark='';
        this.httpUtil.comnQuery({
          action: "StaticTempVersion.getNewestStaticTempVersion2",
          params: {t8StaticTempId: this.editFormData.t8StaticTempId}
        }).then(data => {
          this.editFormData.version = data.rows[0].version;
        }).catch({

        })
      }
    }
  }
</script>

<style scoped>

</style>
