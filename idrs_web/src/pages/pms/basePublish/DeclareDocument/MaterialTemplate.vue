<template>
  <div>
    <k-form-search-customize data-target="TemplateDocGrid" v-model="SearchParam">
      <k-form-item label="模板名称">
        <k-field-text v-model="SearchParam.templateName" :data-max-length="200"/>
      </k-form-item>
      <k-form-item label="产品模式" >
        <k-field-select v-model="SearchParam.prodMod" data-dict="declare_prod_mod" />
      </k-form-item>
      <k-form-item label="文档类型">
        <k-field-select v-model="SearchParam.templateType" data-action="MaterialTemplate.getTemplateTypeDict"
                        data-display-field="templateType,templateLabel" data-value-field="templateType" />
      </k-form-item>
      <k-btn slot="button" class="md-rose" data-functype="POPUP" :data-handler="()=>this.formData={}" style="width:76px"
             data-target="uploadPopup" v-if="global.isShowAuthorityButton('MaterialTemplate.addMaterialTemplate')">
        <md-icon md-src="/static/svg/add.svg" />上传模板</k-btn>
    </k-form-search-customize>
    <k-grid ref="TemplateDocGrid" id="TemplateDocGrid" @data-row-select="selectRow" :data-page-size="5" data-operate-width="250px"
            data-fixed="right" data-action='MaterialTemplate.findMaterialTemplate' >
      <k-grid-column :data-sortable="true" data-align="center" data-header="模板id" data-hidden="true" data-name="templateId"/>
      <k-grid-column data-align="center" data-header="模板名称" data-name="templateName" />
      <k-grid-column data-align="center" data-header="产品模式" data-name="prodMod" data-dict="declare_prod_mod"/>
      <k-grid-column data-align="center" data-header="模板类型" data-name="templateType" data-hidden="true" />
      <k-grid-column data-align="center" data-header="模板子类型" data-name="templateSonType" data-hidden="true"/>
      <k-grid-column data-align="center" data-header="模板类型" data-name="templateLabel" />
      <k-grid-column data-align="center" data-header="模板子类型" data-name="templateSonLabel" />
      <k-grid-column data-align="center" data-header="托管行名称" data-name="truteeBankName" />
      <k-grid-column data-align="center" data-header="备注" data-name="remark"/>
      <k-grid-column data-align="center" data-header="创建日期" data-name="crtDate" data-type="date"  />
      <k-grid-column data-align="center" data-header="创建人" data-name="crtUser" />
      <template slot="operate" slot-scope="scope" >
        <k-btn class="md-rose specialClass" data-functype="POPUP" data-size="mini" :data-model="scope.row.row"
               data-descript="更新模板" data-target="editTemplatePopup"
               v-if="global.isShowAuthorityButton('MaterialTemplate.updateMaterialTemplate')">
          更新模板</k-btn>
        <k-btn class="md-primary specialClass" data-functype="POPUP" data-size="mini" :data-model="scope.row.row"
               style="width:76px;" data-descript="脚本配置" data-target="templateConfigPopup"
               v-if="global.isShowAuthorityButton('MaterialTemplate.updateMaterialTemplate')">
          数据源配置</k-btn>

        <k-btn class="md-danger specialClass" data-descript="删除文档" data-functype="SUBMIT" data-size="mini" v-model="scope.row.row"
               data-action='MaterialTemplate.deleteMaterialTemplate' :data-confirm="true" :data-after-success="reloadTemplateVersionGrid" data-target="TemplateDocGrid"
               v-if="global.isShowAuthorityButton('MaterialTemplate.deleteMaterialTemplate')">
          删除全部
        </k-btn>
      </template>
    </k-grid>


    <k-grid ref="TemplateVersionGrid" :data-autoload="false" data-fixed="right"
            data-action="MaterialTemplate.findMaterialVersion" :data-page-size="5" data-operate-width="230px">
      <k-grid-column data-align="center" data-header="版本id" data-name="versionId" :data-sortable="true" data-hidden="true" />
      <k-grid-column data-align="center" data-header="对应模板id" data-name="templateId" data-hidden="true" />
      <k-grid-column data-align="center" data-header="版本状态" data-name="versionStart" data-dict="xp_status" />
      <k-grid-column data-align="center" data-header="文件名称" data-name="versionName" data-width="320"/>
      <k-grid-column data-align="center" data-header="版本号" data-name="versionNum" />
      <k-grid-column data-align="center" data-header="上传日期" data-name="crtDate" />
      <k-grid-column data-align="center" data-header="上传人" data-name="crtUser" />
      <k-grid-column data-align="center" data-header="备注" data-name="remark" />

      <template slot="operate" slot-scope="scope">
        <k-btn class="btn-custom-text specialClass"  data-functype="SUBMIT" data-size="mini" data-action="MaterialTemplate.startVersion"
               data-target="TemplateVersionGrid" :data-confirm="true"
               v-if="scope.row.row.versionStart === '0' && global.isShowAuthorityButton('MaterialTemplate.startVersion')">
          启用
        </k-btn>
        <k-btn  class="btn-custom-text specialClass" data-functype="SUBMIT" data-size="mini" data-action="MaterialTemplate.stopVersion"
               data-type="danger" data-target="TemplateVersionGrid" :data-confirm="true"
               v-if="scope.row.row.versionStart === '1' && global.isShowAuthorityButton('MaterialTemplate.stopVersion')">
          停用
        </k-btn>
        <k-btn class="btn-custom-text specialClass" :data-download-name="scope.row.row.versionName"  data-descript="下载文档模板信息"
               data-functype="DOWNLOAD" data-size="mini"
               data-url="/download/server/PmsApp/materialController/MaterialDownloadAction.action" v-model="scope.row.row"
               v-if="global.isShowAuthorityButton('MaterialTemplate.MaterialDownload')">
          下载
        </k-btn>
        <k-btn class="btn-custom-text specialClass" data-descript="删除文档" data-functype="SUBMIT" data-size="mini" v-model="scope.row.row"
               data-action='MaterialTemplate.deleteMaterialVersion' :data-confirm="true" data-target="TemplateVersionGrid"
               v-if="global.isShowAuthorityButton('MaterialTemplate.deleteMaterialVersion')"
               :data-disabled="scope.row.row.versionStart==='1'">
          删除
        </k-btn>
      </template>

    </k-grid>


    <k-grid ref="TemplateConfigGrid" :data-autoload="false" data-fixed="right"
            data-action="MaterialTemplate.findMaterialConfig" :data-page-size="5">
      <k-grid-column data-align="center" data-header="数据源标识" data-name="sqlId"/>
      <k-grid-column data-align="center" data-header="字段说明" data-name="columnLabel" />
      <k-grid-column data-align="center" data-header="字段KEY" data-name="columnKey" />
      <k-grid-column data-align="center" data-header="数据源" data-name="sqlSelect"  data-hidden="true" />
      <k-grid-column data-align="center" data-header="数据源状态" data-name="sqlStart" data-dict="xp_status"/>
      <k-grid-column data-align="center" data-header="执行顺序" data-name="sqlOrder" />
      <k-grid-column data-align="center" data-header="备注" data-name="remark"/>
      <template slot="operate" slot-scope="scope">
        <k-btn  class="btn-custom-text specialClass" data-functype="SUBMIT" data-size="mini" data-action="MaterialTemplate.deleteMaterialConfig"
                data-type="danger" data-target="TemplateConfigGrid" :data-confirm="true"
                v-if="global.isShowAuthorityButton('MaterialTemplate.deleteMaterialConfig')">
          删除
        </k-btn>
      </template>

    </k-grid>


    <k-popup ref="uploadPopup" data-title="上传文档模板" :dataDialogDrag="true">
      <EditComp v-model="formData" @loadTemplateDoc="loadGrid" ref="uploadComp1" :info="{}" :disabledVal="false"/>
    </k-popup>
    <k-popup ref="editTemplatePopup" data-title="更新文档模板" :dataDialogDrag="true">
      <EditComp v-model="formData" @loadTemplateDoc="loadSonGrid" ref="uploadComp2" :info="formData" :disabledVal="true"/>
    </k-popup>

    <k-popup ref="templateConfigPopup" data-title="脚本配置" :dataDialogDrag="true">
      <k-form dataInputWidth="300px" ref="templateConfigFrom" :data-col="2">

        <k-form-item label="模板名称" >
          <k-field-text v-model="formData.templateName" :data-allowblank="false" :data-max-length="128" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="产品模式" >
          <k-field-select v-model="formData.prodMod" :data-allowblank="false" data-dict="declare_prod_mod" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="文档类型">
          <k-field-select v-model="formData.templateType" data-action="MaterialTemplate.getTemplateTypeDict" :data-allowblank="false"
                          data-display-field="templateType,templateLabel" data-value-field="templateType" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="文档子类型" v-if="formData.templateType==='6'">
          <k-field-select v-model="formData.templateSonType" data-action="MaterialTemplate.getTemplateSonTypeDict" :data-allowblank="false"
                          data-display-field="templateSonType,templateSonLabel" data-value-field="templateSonType" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="数据源" >
          <k-field-select v-model="formData.sqlId" data-action="DisclosureSource.findDisclosureSourcesAuth" :data-allowblank="false"
                          data-display-field="columnKey,columnLabel" data-value-field="id"  :data-params="{'status':'1'}"/>
        </k-form-item>
        <k-form-item label="执行顺序" >
          <k-field-text v-model="formData.sqlOrder"  data-validate-type="int" data-type="int"
                        :data-max-lenght="2" :data-allowblank="false" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="TemplateConfigGrid"
                 data-from="templateConfigFrom" :data-model="formData" data-action="MaterialTemplate.addMaterialConfig">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>

      </k-form>

    </k-popup>

  </div>
</template>

<script>

import { assign } from "lodash";
import EditComp from "./MaterialUpload";
import Tools from "@/utils/tools";

export default {
  components: {
    EditComp
  },
  name:"TemplateDoc",
  data() {
    return {
      SearchParam: {},//查询参数
      formData: {},
      selectRowData: {},
    };
  },
  computed: {

  },

  methods: {
    selectRow(row, column, event) {
      const _this = this
      _this.selectRowData = assign({}, row)
      _this.formData = assign({}, row)
      this.$refs.TemplateVersionGrid.load({templateId: this.formData.templateId});
      this.$refs.TemplateConfigGrid.load({templateId: this.formData.templateId});
    },

    loadGrid(val){
      this.$refs.uploadPopup.close();
      // this.$refs.editTemplatePopup.close();
      this.$refs.TemplateDocGrid.load(this.SearchParam);
      // this.$refs.TemplateVersionGrid.load({templateType: val.templateType});
      // this.$refs.TemplateConfigGrid.load({templateId: val.templateType});
    },

    loadSonGrid(val){
      // this.$refs.uploadPopup.close();
      this.$refs.editTemplatePopup.close();
      // this.$refs.TemplateDocGrid.load(this.SearchParam);
      this.$refs.TemplateVersionGrid.load({templateId: this.formData.templateId});
      this.$refs.TemplateConfigGrid.load({templateId: this.formData.templateId});
    },
    reloadTemplateVersionGrid(){
      this.$refs.TemplateVersionGrid.load({templateId: '000'});
    },



  }
};
</script>
<style scoped>
>>> .el-table__cell {
  padding: 1px 0 !important;
}
>>> .specialClass > .md-ripple{
  padding: 5px !important;
}
</style>
