<template>
  <div>
    <k-form-search-customize data-target="printTempGrid" v-model="printTemp">
      <k-form-item label="文档名称">
        <k-field-text v-model="printTemp.tempName"/>
      </k-form-item>
      <k-form-item label="文档类型">
        <k-field-select v-model="printTemp.docType" data-dict="t8_print_doc" @data-on-change="onDocTypeChange"/>
      </k-form-item>
      <k-form-item label="模板类型">
        <k-field-select v-model="printTemp.tempType" :data-data="docTypeDict" data-value-field="value"
                        data-display-field="value,text"/>
      </k-form-item>
      <k-btn slot="button" data-functype="POPUP" class="btn-custom-primary" :data-handler="addHandler"
             data-target="addPopup">
        <md-icon md-src="/static/svg/add.svg"/>
        上传文档模板
      </k-btn>
    </k-form-search-customize>
    <k-grid ref="printTempGrid" data-action="PrintTemp.getPrintTempList" @data-row-select="selectPrintTemp">
      <k-grid-column :data-sortable="true" data-default-sort="DESC" data-align="center" data-header="模板id" data-name="id" data-hidden="true"/>
      <k-grid-column data-align="center" data-header="文档类型" data-name="docType" data-dict="t8_print_doc"/>
      <k-grid-column data-align="center" data-header="模板类型" data-dict="t8_temp_type" data-name="tempType"/>
      <k-grid-column data-align="center" data-header="文档名称" data-name="tempName"/>
      <k-grid-column data-align="center" data-header="备注" data-name="remark"/>
      <k-grid-column data-align="center" data-header="上传日期" data-type="date" data-name="createDate"/>
      <template slot="operate">
        <k-btn class="md-info md-just-icon md-simple" data-descript="修改模板内容" data-functype="POPUP" data-size="small"
               data-target="editPrintTempVersionPopup" :data-handler="editHandler">
          <md-icon>edit</md-icon>
        </k-btn>
      </template>
    </k-grid>

    <k-grid ref="printTempVersionGrid" :data-autoload="false"
            data-action="PrintTempVersion.getPrintTempVersionByTempId">
      <k-grid-column data-align="center" data-header="版本id" data-name="id" data-hidden="true" />
      <k-grid-column data-align="center" data-header="对应模板id" data-name="t8PrintTempId" data-hidden="true" />
      <k-grid-column data-align="center" data-header="文档名称" data-name="tempName"/>
      <k-grid-column data-align="center" data-header="文档版本编号" data-name="version"/>
      <k-grid-column data-align="center" data-header="备注" data-name="remark"/>
      <k-grid-column data-align="center" data-header="创建人" data-name="createUserName"/>
      <k-grid-column data-align="center" data-header="生效状态" data-name="status" data-dict="t8_print_temp_version_status"/>
      <k-grid-column data-align="center" data-header="上传日期" data-type="date" data-name="createDate"/>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" :data-disabled="scope.row.row.status=='1'" data-descript="文档模板生效" data-functype="SUBMIT" data-size="small"
               data-action="PrintTempVersion.updatePrintTempVersionStatus" data-target="printTempVersionGrid" v-model="scope.row.row" :data-confirm="true">
          <md-icon>done</md-icon>
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
      </template>
    </k-grid>

    <k-popup ref="addPopup" title="上传文档模板">
      <k-form ref="addForm" data-ui="element">
        <k-form-item label="文档类型">
          <k-field-select v-model="formData.docType" :data-allowblank="false" data-dict="t8_print_doc"
                          @data-on-change="onAddDocTypeChange"/>
        </k-form-item>
        <k-form-item label="模板类型">
          <k-field-select v-model="formData.tempType" :data-allowblank="false" :data-data="addDocTypeDict"
                          data-value-field="value" data-display-field="value,text"/>
        </k-form-item>
        <k-form-item label="销售商信息"
                     v-show="formData.tempType=='10003' ||
                      formData.tempType=='20003' ||
                      formData.tempType=='30003' ||
                      formData.tempType=='40003' ||
                      formData.tempType=='50003' ||
                      formData.tempType=='60003' ||
                      formData.tempType=='70003'">
          <k-field-select v-model="formData.distributorCode" data-action="T8Dict.findTaDistributorInfos"
                          data-display-field="distributorName"  data-value-field="distributorCode"  />
        </k-form-item>
        <k-form-item label="托管行信息"
                     v-show="formData.tempType=='10002' ||
                      formData.tempType=='20002' ||
                      formData.tempType=='30002' ||
                      formData.tempType=='40002' ||
                      formData.tempType=='50002' ||
                      formData.tempType=='60002' ||
                      formData.tempType=='70002'">
          <k-field-select v-model="formData.t8TruteeInfoId" data-action="T82006.findTaCustodianBanks"
                          data-display-field="truteeName"  data-value-field="id"  />
        </k-form-item>
        <k-form-item label="创设会信息"
                     v-show="formData.tempType=='10001' ||
                      formData.tempType=='20001' ||
                      formData.tempType=='30001' ||
                      formData.tempType=='40001' ||
                      formData.tempType=='50001' ||
                      formData.tempType=='60001' ||
                      formData.tempType=='70001'">
          <k-field-select v-model="formData.t8MeetCreateId" data-action="T8ProdCreateMeeting.createMeetingDict"
                          data-display-field="meetingName"  data-value-field="id"  />
        </k-form-item>
        <k-form-item label="文档版本">
          <k-field-text v-model="formData.version" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.remark"/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit=1
                          :data-error="onSubmitError" :data-success="onSubmitSuccess"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/print/uploadPrintTemp.json">
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

    <k-popup ref="editPrintTempVersionPopup" title="上传修改文档模板">
      <k-form ref="editPrintTempVersionForm" data-ui="element" v-model="editFormData">
        <k-form-item label="对比版本">
          <k-field-text v-model="editFormData.version" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="模板类型">
          <k-field-text v-model="editFormData.tempType"/>
        </k-form-item>
        <k-form-item label="模板名称">
          <k-field-text v-model="editFormData.tempName"/>
        </k-form-item>
        <k-form-item label="托管行">
          <k-field-text v-model="editFormData.t8TruteeInfoId"/>
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="editFormData.remark"/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="editPrintTempVersionRef" :data-multiple="false" :data-limit=1
                          :data-error="onEditPrintTempVersionSubmitError" :data-success="onEditPrintTempVersionSubmitSuccess"
                          :data-change="onEditPrintTempVersionChange"
                          :data-auto-upload="false" :data-upload-url="editUploadUrl">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="printTempVersionGrid" ref="editPrintTempVersionSubmitBtn"
                 data-from="editPrintTempVersionForm" :data-model="editFormData" :data-handler="submitEditPrintTempVersionUploadParam">确定
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
        <k-form-item label="备注">
          <k-field-text v-model="compareFormData.remark" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="comparePrintTempVersionRef" :data-multiple="false" :data-limit=1
                          :data-error="onCompareSubmitError" :data-success="onCompareSubmitSuccess"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/print/comparePrintTempVersion.json">
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

  export default {
    name: "printTempInfo",
    data() {
      return {
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
          version: 0
        },
        editFormData:{
          t8PrintTempId:'',
          t8TruteeInfoId:'',
          tempType: '',
          tempName: '',
          version:'',
          remark:''
        },
        compareFormData:{
          id:'',
          version:'',
          remark:''
        },
        editUploadUrl:'/upload/server/PmsApp/print/comparePrintTempVersion.json',
      };
    },
    methods: {
      onDocTypeChange() {
        this.printTemp.tempType = '';
        this.httpUtil.comnQuery({
          action: "PrintTemp.getTempTypeByDocType",
          params: {docType: this.printTemp.docType}
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
        this.$refs.printTempGrid.load();
      },
      addHandler() {
        this.formData.docType = '';
        this.formData.tempType = '';
        this.formData.remark = '';
        this.formData.distributorCode = '';
        this.formData.t8TruteeInfoId = '';
        this.formData.t8MeetCreateId = '';
        this.formData.version = 0;
      },
      selectPrintTemp(row, column, event) {
        const _this = this;
        _this.selectRowData = assign({}, row);
        _this.formData = assign({}, row);
        this.$refs.printTempVersionGrid.load({t8PrintTempId: _this.selectRowData.id});
      },
      editHandler(value) {
        console.log("value=>>>>",value);
        this.editFormData.t8PrintTempId =value.id;
        this.editFormData.t8TruteeInfoId =value.t8TruteeInfoId;
        this.editFormData.tempType=value.tempType;
        this.editFormData.tempName=value.tempName;
        this.editFormData.remark='';
        this.editUploadUrl='/upload/server/PmsApp/print/comparePrintTempVersion.json';
        this.httpUtil.comnQuery({
          action: "PrintTempVersion.getNewestPrintTempVersion2",
          params: {t8PrintTempId: this.editFormData.t8PrintTempId}
        }).then(data => {
          if(data.rows.length>0){
            this.editFormData.version = data.rows[0].version;
          }
          //this.editFormData.version = data.rows[0].version;
        }).catch({

        })
      },
      onEditPrintTempVersionSubmitError(){
        this.$refs.editPrintTempVersionRef.doReset();
        this.$refs.editPrintTempVersionSubmitBtn.setIconStyle(1, [])
      },
      onEditPrintTempVersionSubmitSuccess(response,file){
        let returnData = response.response.returndata;
        //当返回类型为1是打开比对页面，否则代表上传成功
        if (returnData.type =='1'){
          let url = returnData.url;
          //重置文件上传状态
          file[0].status='ready';
          this.$refs.editPrintTempVersionSubmitBtn.setIconStyle(1, []);
          window.open(url,'_blank','width=1000,height=800,toolbars=yes,resizable=yes,scrollbars=yes,left=20,top=30');
        }else {
          this.$refs.editPrintTempVersionRef.doReset();
          this.$refs.editPrintTempVersionForm.reset();
          this.$refs.editPrintTempVersionPopup.close();
          this.$refs.printTempVersionGrid.load();
          this.editUploadUrl='/upload/server/PmsApp/print/comparePrintTempVersion.json';
        }
      },
      submitEditPrintTempVersionUploadParam(){
        let editFormData = this.editFormData;
        this.editUploadUrl='/upload/server/PmsApp/print/uploadPrintTempVersion.json';
        this.$refs.editPrintTempVersionRef.upload(editFormData);
      },
      previewPrintTempVersion(value){
        this.httpUtil.ajax({
          url: "/server/form/PmsApp/print/previewPrintTempVersion.json",
          params: value
        }).then(data => {
          this.$nextTick(()=>{
            let url = data.returndata.url;
            console.log(url);
            window.open(url,'_blank','width=1000,height=800,toolbars=yes,resizable=yes,scrollbars=yes,left=20,top=30');
          })
        }).catch({

        })
      },
      onEditPrintTempVersionChange(file){
        if (file.status=='ready' && file.response==null){
          let editFormData = this.editFormData;
          this.$refs.editPrintTempVersionSubmitBtn.setIconStyle(0, []);
          this.$refs.editPrintTempVersionRef.upload(editFormData);
        }
      },
      compareHandler(value){
        this.compareFormData = value;
      },
      submitCompare(){
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
      }
    }
  }
</script>

<style scoped>

</style>
