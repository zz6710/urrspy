<template>
  <div class="py-page">
    <div>
      <k-form-search-customize  v-model="queryParam"   data-target="assetRightGrid">
        <k-form-item label="股权代码">
          <k-field-text v-model="queryParam.assNbrExt"  ></k-field-text>
        </k-form-item>
        <k-form-item label="融资企业行业">
          <k-field-select v-model="queryParam.industryIssuer" data-dict="isuOrgBlgIdt"></k-field-select>
        </k-form-item>
        <k-form-item label="融资企业名称">
          <k-field-select v-model="queryParam.orgNbrExt"  data-action="T8OrgSheet.findOrgNmAll" :dataRemote="true" data-value-field="orgNbrExt" data-display-field="orgFullName"></k-field-select>
        </k-form-item>
        
<!--        <k-btn slot="button"  class="btn-custom-plain"  data-functype="EXPORT" data-target="assetRightGrid" :data-export-name="'股权信息导出'">-->
<!--          <md-icon>cloud_download</md-icon>-->
<!--          导出-->
<!--        </k-btn>-->
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" slot="button" :data-handler="()=>this.formData={}" data-target="addAssetRightPopup" v-if="global.isShowAuthorityButton('AssetRightModel.addAssetRight')">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
          <k-btn slot="button" class="btn-custom-plain" :data-download-name="'股权信息导入'+'.xlsx'"
                data-descript="下载Excel模板" data-functype="DOWNLOAD" data-size="small"
                data-url="/download/server/DpsApp/AssetRight/comn-download.json">
            <md-icon>cloud_download</md-icon>
            下载Excel模板
          </k-btn>
          <k-btn slot="button"  data-functype="POPUP" class="btn-custom-plain" data-target="uploadUnderRightInfoPopup"
                v-if="global.isShowAuthorityButton('AssetRightModel.rightUploadAction')">
            <md-icon>cloud_upload</md-icon>
            导入
          </k-btn>
        </div>
      </div>

      <k-grid ref="assetRightGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="300px" data-action="AssetRightModel.findAssetRight"
              v-if="global.isShowAuthorityButton('AssetRightModel.findAssetRight')">
      <k-grid-column data-align="left" data-header="股权代码" data-name="assNbrExt"></k-grid-column>
      <k-grid-column data-align="left" data-header="融资企业名称" data-name="orgFullName"></k-grid-column>
      <k-grid-column data-align="left" data-header="融资企业行业" data-name="industryIssuer"   data-dict="isuOrgBlgIdt"></k-grid-column>
      <k-grid-column data-align="left" data-header="投资阶段" data-name="investmentType" data-dict="subm_invest_stage"></k-grid-column>
      <k-grid-column data-align="left" data-header="股权退出安排" data-name="sharehold"></k-grid-column>
      <k-grid-column data-align="left" data-header="是否通道投资" data-name="isChannel"  data-dict="isTrue"></k-grid-column>
      <k-grid-column data-align="left" data-header="通道" data-name="channelCode"  ></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text specialClass" data-descript="修改股权信息" data-functype="POPUP" data-size="mini" v-if="global.isShowAuthorityButton('AssetRightModel.updateAssetRight')"
                 data-target="editAssetRightPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text specialClass" data-functype="SUBMIT" data-action="AssetRightModel.deleteAssetRight" data-size="mini" v-if="global.isShowAuthorityButton('AssetRightModel.deleteAssetRight')"
                 data-type="danger" data-target="assetRightGrid" :data-confirm="true" data-descript="删除股权信息">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加assetRight弹出框   -->
	<k-popup ref="addAssetRightPopup" data-title="新增" :dataDialogDrag="true">
    <assetRightEdit  @loadGriding="loadGriding" ref="addComp" :info="formData" :disabledVal="false"/>
	</k-popup>

	<!--    修改assetRight弹出框   -->
	<k-popup ref="editAssetRightPopup" data-title="修改" :dataDialogDrag="true">
    <assetRightEdit  @loadGriding="loadGriding" ref="editComp" :info="formData" :disabledVal="true"/>
	</k-popup>

    <k-popup ref="uploadUnderRightInfoPopup" title="股权信息导入">
      <k-form ref="addForm" data-ui="element">

        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                                data-accept=".xlsx,.xls" :data-error="onSubmitError" :data-success="onSubmitSuccess"
                                :data-auto-upload="false"
                                data-upload-url="upload/server/DpsApp/excelUploadAction/rightExcelUploadAction.json">
          </k-field-excel-upload>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="assetRightGrid" ref="submitBtn"
                 :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

  </div>
</template>

<script>
  import assetRightEdit from "@/pages/pms/asset/right/assetRightEdit";
  export default {
    name: "assetRight",
    components: {assetRightEdit},
    data() {
      return {
        formData: {},
        selectRowData: {},
        queryParam:{},
      };
    },
    created() {
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
      loadGriding(val){
        this.$refs.editAssetRightPopup.close();
        this.$refs.addAssetRightPopup.close();
        this.$refs.assetRightGrid.load(this.queryParam);
      },

      submitUploadParam(){
        let formData = this.formData;
        this.$refs.uploadRef.upload(formData);
      },
      onSubmitSuccess() {
        this.$refs.uploadRef.doReset();
        this.$refs.addForm.reset();
        this.$refs.uploadUnderRightInfoPopup.close();
        this.$refs.assetRightGrid.load(this.queryParam);
      },
      onSubmitError() {
        this.$refs.uploadRef.doReset();
        this.$refs.submitBtn.setIconStyle(1, [])
      },
    }
  };
</script>


<style scoped>
>>> .el-table__cell {
  padding: 1px 0 !important;
}
.specialClass {
  min-width: 40px !important;
}
>>> .specialClass > .md-ripple{
  padding: 8px !important;
}
</style>
