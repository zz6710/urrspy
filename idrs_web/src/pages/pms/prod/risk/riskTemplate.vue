<template>
  <div>
    <k-form-search-customize data-target="printTempGrid" v-model="queryParam">
      <k-form-item label="模板名称">
        <k-field-select v-model="queryParam.id"  data-action="ProdRiskTemplate.getTemplateListByCondition"
                        data-display-field="templateName" data-value-field="id" style="width: 550px;"></k-field-select>
      </k-form-item>
      <k-btn slot="button" data-functype="POPUP" class="btn-custom-primary" @click="setPopType('1')"
        data-target="addPopup" style="width: 120px;"
        v-if="global.isShowAuthorityButton('ProdRiskTemplateVersion.addTemplateVersionInfo')">
        <md-icon md-src="/static/svg/add.svg"/>
        上传文档模板
      </k-btn>
    </k-form-search-customize>

    <!--  grid模板列表  -->
    <k-grid ref="printTempGrid" data-action="ProdRiskTemplate.getTemplateListByCondition1" @data-row-select="selectPrintTemp">
      <k-grid-column data-align="center" data-header="模板id" data-hidden="true" data-name="id"/>
      <k-grid-column data-align="center" data-header="模板名称" data-name="templateName" data-width="550"/>
      <k-grid-column data-align="center" data-header="上传日期" data-name="crtDate" data-render="renderDateTimeCreate"/>
<!--      <k-grid-column data-align="center" data-header="上传时间" data-name="crtTime" data-type="time"/>-->
      <k-grid-column data-align="center" data-header="更新日期" data-name="updDate" data-render="renderDateTimeUpdate"/>
<!--      <k-grid-column data-align="center" data-header="更新时间" data-name="updTime" data-type="time"/>-->
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" data-descript="上传新版本" @click="setPopType('2',scope.row.row)" data-functype="POPUP" data-size="mini"
               data-target="addPopup"
               v-if="global.isShowAuthorityButton('ProdRiskTemplateVersion.addTemplateVersionInfo1')">
          <md-icon>cloud_upload</md-icon>
        </k-btn>
<!--        <k-btn class="md-info md-just-icon md-simple"  data-descript="确认" data-functype="SUBMIT" data-size="mini"-->
<!--               data-target="printTempVersionGrid" data-action="ProdRiskTemplateVersion.takeEffectTemplate"-->
<!--               v-if="global.isShowAuthorityButton('ProdRiskTemplateVersion.takeEffectTemplate')">-->
<!--          <md-icon >done</md-icon>-->
<!--        </k-btn>-->
          <!--<k-btn class="md-info md-just-icon md-simple" :data-download-name="scope.row.row.templateName"  data-descript="下载文档模板信息" data-functype="DOWNLOAD" data-size="small"
               data-url="/download/server/PmsApp/risk/template/comn-download.json" v-model="scope.row.row">
          <md-icon>cloud_download</md-icon>
        </k-btn>-->
      </template>

    </k-grid>


    <!--  grid模板项目列表  -->
    <k-grid ref="printTempVersionGrid"
           
            :data-operate-="false" :data-autoload="false"
            data-action="ProdRiskTemplateVersion.findByCondition">
            
      <k-grid-column data-align="center" data-header="t8RiskTemplateId" data-name="t8RiskTemplateId"  data-hidden="true"/>
      <k-grid-column data-align="center" data-header="模板名称" data-name="templateName" data-width="550"/>
      <k-grid-column data-align="center" data-header="模板描述" data-name="description" />
      <k-grid-column data-align="center" data-header="版本" data-name="version"/>
      <k-grid-column data-align="center" data-header="模板状态" data-name="templateStatus" data-dict="t8_print_temp_version_status"/>
      <k-grid-column data-align="center" data-header="上传日期" data-name="crtDate" data-render="renderDateTimeCreate"/>
<!--      <k-grid-column data-align="center" data-header="上传时间" data-name="crtTime" data-type="time"/>-->

      <template slot="operate" slot-scope="scope">
       
          <k-btn class="md-info md-just-icon md-simple" :data-download-name="scope.row.row.templateName"  data-descript="下载文档模板信息" data-functype="DOWNLOAD" data-size="small"
               data-url="/download/server/PmsApp/risk/template/comn-download.json" v-model="scope.row.row">
          <md-icon>cloud_download</md-icon>
        </k-btn>
      </template>

    </k-grid>



    <!--  模板上传  -->
    <k-popup ref="addPopup" title="上传文档模板" data-opened="setVal">
      <k-form ref="addForm" data-ui="element">
        <k-form-item label="父模板id" v-show="false">
          <k-field-text v-model="formData.t8RiskTemplateId" ></k-field-text>
        </k-form-item>
        <k-form-item label="模板版本:">
          <k-field-text v-model="formData.version"  data-allowblank="false" data-disabled="true"></k-field-text>
        </k-form-item>
        <k-form-item label="模板描述:" data-input-width="530px">
          <k-field-text v-model="formData.description" :data-max-length="255" inputType="textarea" :rows="1"></k-field-text>
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit=1
                           data-accept=".xlsx,.xls"
                          :data-error="onSubmitError" :data-success="onSubmitSuccess"
                          :data-auto-upload="false" data-upload-url="/upload/server/PmsApp/risk/template/comn-upload.json">
          </k-field-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="printTempGrid" ref="submitBtn"
                 data-from="addForm"  :data-handler="submitUploadParam">确定
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
  data() {
    return {
      formData: {
        version: '',
        t8RiskTemplateId: '',
        description:''
      },
      selectRowData: {},
      queryParam: {},
      ProdRiskTemplate: {},
      ProdRiskProject: {},
      PopType: ''
    };
  },
  methods: {
    renderDateTimeCreate(row) {
      return Tools.formatDateTime(row.crtDate, row.crtTime);
    },
    renderDateTimeUpdate(row) {
      return Tools.formatDateTime(row.updDate, row.updTime);
    },
    setPopType(val,  rows) {
      var version = 'V1.0'
      this.formData.description = ''
      if (val == '1') {
       this.formData.version = 'V1.0'
        //上传父模板
        this.formData.t8RiskTemplateId = ''
      } else {
        this.httpUtil.comnQuery({
          action: "ProdRiskTemplateVersion.getNewestRiskTemplateVersion",
          params: {t8RiskTemplateId: rows.id}
        }).then(data => {
          if (data.rows.length > 0 && data.rows[0].version) {
            this.formData.version = data.rows[0].version;
          } else {
            this.formData.version = 'V1.1'
          }
          //上传子模板
          this.formData.t8RiskTemplateId = rows.id
        }).catch({

        });
      }
    },
    selectPrintTemp(row, column, event) {
      const _this = this
      _this.selectRowData = assign({}, row)
      this.$refs.printTempVersionGrid.load({t8RiskTemplateId: row.id});
    },

    onSubmitError() {
      this.$refs.uploadRef.doReset();
      this.$refs.submitBtn.setIconStyle(1, [])
    },

    onSubmitSuccess() {

      this.$refs.printTempVersionGrid.load({t8RiskTemplateId: this.selectRowData.id})
      this.$refs.uploadRef.doReset();
      this.$refs.addForm.reset();
      this.$refs.addPopup.close();
      this.$refs.printTempGrid.load();
    },
    submitUploadParam() {
      let formData = this.formData;
      this.$refs.uploadRef.upload(formData);
    },

  }
}
</script>

<style scoped>

</style>
