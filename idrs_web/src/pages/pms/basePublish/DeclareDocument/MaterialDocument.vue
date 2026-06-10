<template>
  <div>
    <k-form-search-customize data-target="ProdGrid" v-model="SearchParam">
      <k-form-item label="产品名称">
        <k-field-select v-model="SearchParam.prodCode" data-action='MaterialDocument.findProdInfo'
                        data-display-field="prodCode,prodName" data-value-field="prodCode"/>
      </k-form-item>
      <k-form-item label="产品模式" >
        <k-field-select v-model="SearchParam.prodMod" data-dict="prod_mod" />
      </k-form-item>
      <k-form-item label="成立日" >
        <k-field-date v-model="SearchParam.foundDate" />
      </k-form-item>
    </k-form-search-customize>

    <k-grid ref="ProdGrid" id="ProdGrid" @data-row-select="selectRow" :data-page-size="5" data-operate-width="230px"
            data-fixed="right" data-action='MaterialDocument.findProdInfo' >
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode" />
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName" />
      <k-grid-column data-align="center" data-header="托管行" data-name="truteeBank" />
      <k-grid-column data-align="center" data-header="产品模式" data-name="prodMod" data-dict="prod_mod"/>
      <k-grid-column data-align="center" data-header="成立日" data-name="foundDate" data-type="date"/>
      <k-grid-column data-align="center" data-header="到期日" data-name="mtuDate" data-type="date"/>
      <k-grid-column data-align="center" data-header="产品状态" data-name="prodSts" data-dict="prod_status" />
      <template slot="operate" slot-scope="scope" >
        <k-btn class="btn-custom-text specialClass" data-functype="SUBMIT" data-size="mini" :data-model="scope.row.row"
               data-descript="批量生成文档" :data-confirm="true" data-target="DocumentGrid"
               data-action="MaterialDocument.addMaterialDocumentAll"
               v-if="global.isShowAuthorityButton('MaterialDocument.addMaterialDocument')">
          一键生成</k-btn>
        <k-btn class="btn-custom-text specialClass" data-functype="POPUP" data-size="mini" :data-model="scope.row.row"
               data-descript="上传文件" data-target="uploadDocumentPopup"
               v-if="global.isShowAuthorityButton('MaterialDocument.uploadDocument')">
          上传文件</k-btn>
        <k-btn class="btn-custom-text specialClass" :data-download-name="scope.row.row.prodCode+'_'+scope.row.row.prodName+'_申报材料.zip'"
               data-descript="下载文档" data-functype="DOWNLOAD" data-size="mini" v-model="scope.row.row"
               data-url="/download/server/PmsApp/materialController/DocumentDownloadActionAll.action"
               v-if="global.isShowAuthorityButton('MaterialTemplate.downloadDocument')">
          一键下载
        </k-btn>
      </template>
    </k-grid>

    <k-grid ref="DocumentGrid" :data-autoload="false" data-fixed="right" @data-row-select="DocumentGridRow"
            data-action="MaterialDocument.findMaterialDocument" :data-page-size="10" data-operate-width="230px">
      <k-grid-column :data-sortable="true" data-align="center" data-header="文档id" data-hidden="true" data-name="documentId"/>
      <k-grid-column data-align="center" data-header="序列" data-name="documentNum" data-width="100" />
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode" data-width="150" />
<!--      <k-grid-column data-align="center" data-header="托管行" data-name="truteeBank" />-->
      <k-grid-column data-align="center" data-header="产品模式" data-name="prodMod" data-hidden="true"/>
      <k-grid-column data-align="center" data-header="文档名称" data-name="documentName" />
      <k-grid-column data-align="center" data-header="文档路径" data-name="documentPath" data-hidden="true" />
      <k-grid-column data-align="center" data-header="文档类型" data-name="templateType" data-hidden="true" />
      <k-grid-column data-align="center" data-header="文档类型" data-name="templateLabel" />
      <k-grid-column data-align="center" data-header="创建日期" data-name="crtDate" data-type="date" data-width="150" />
      <k-grid-column data-align="center" data-header="创建人" data-name="crtUser" data-width="150" />
      <template slot="operate" slot-scope="scope" >
        <k-btn class="btn-custom-text specialClass" data-functype="SUBMIT" data-size="mini" :data-model="scope.row.row" data-target="DocumentGrid"
               data-descript="生成文档" :data-confirm="true" data-action="MaterialDocument.addMaterialDocument"
               v-if="global.isShowAuthorityButton('MaterialDocument.addMaterialDocument')">
          重新生成</k-btn>
        <k-btn class="btn-custom-text specialClass" :data-download-name="scope.row.row.documentName"
               data-descript="下载文档" data-functype="DOWNLOAD" data-size="mini" v-model="scope.row.row"
               data-url="/download/server/PmsApp/materialController/DocumentDownloadAction.action"
               v-if="scope.row.row.templateType!=='06' && global.isShowAuthorityButton('MaterialTemplate.MaterialDownload')"
               :data-disabled="scope.row.row.documentPath===''">
          下载
        </k-btn>
        <k-btn class="btn-custom-text specialClass" data-functype="POPUP" data-size="mini" :data-model="scope.row.row"
               data-descript="下载文档" data-target="downloadPopup"
               v-if="scope.row.row.templateType==='06' && global.isShowAuthorityButton('MaterialDocument.downloadDocument')">
          下载</k-btn>
<!--        <k-btn class="md-info specialClass" data-functype="POPUP" data-size="mini" :data-model="scope.row.row"
               data-descript="历史文档" data-target="downloadHisPopup">
          查看历史</k-btn>-->
      </template>
    </k-grid>

    <k-popup ref="uploadDocumentPopup" data-title="文件上传" :dataDialogDrag="true">
      <k-form dataInputWidth="300px" ref="uploadFrom" :data-col="2">
        <k-form-item label="产品代码" >
          <k-field-select v-model="formData.prodCode" :data-disabled="true" />
        </k-form-item>
        <k-form-item label="产品模式" >
          <k-field-select v-model="formData.prodMod" :data-disabled="true" data-dict="declare_prod_mod" />
        </k-form-item>
        <k-form-item label="文档类型">
          <k-field-select v-model="formData.templateType" data-action="MaterialTemplate.getTemplateTypeDict" :data-allowblank="false"
                          data-display-field="templateType,templateLabel" data-value-field="templateType"
                          @data-on-change="templateTypeChange"/>
        </k-form-item>
        <k-form-item label="文档子类型" v-if="templateTypeDict.length>1">
          <k-field-select v-model="formData.templateSonType"  :data-data="templateTypeDict" :data-allowblank="false"
                          data-display-field="templateSonType,templateSonLabel" data-value-field="templateSonType" />
        </k-form-item>
        <k-form-item label="附件" data-ui="element" data-input-width="500px">
          <k-field-upload data-type="file" ref="fileUploadRef"
                          :data-error="onFileSubmitError" :data-success="onFileSubmitSuccess" :data-before-upload="checkFile"
                          :data-auto-upload="false" data-upload-url="upload/server/PmsApp/materialController/DocumentUploadAction.action">
          </k-field-upload>
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT"  ref="fileSubmitBtn"
                 data-from="uploadFrom" :data-model="formData" :data-handler="fileSubmitUploadParam">确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <k-popup ref="downloadPopup" :data-title="downloadData.templateLabel+'管理'" data-width="60%" :dataDialogDrag="true">
      <div>
        <k-grid ref="downloadGrid" @data-row-select="selectDownloadRow" :data-checkbox="false" data-checkbox-id="id" data-fixed="right"
                data-action='MaterialDocument.findSalesDocument' @init="(grid)=>{this.$kgrid = grid}" data-operate-width="100px"
                :data-params="{prodCode:downloadData.prodCode,templateType:downloadData.templateType}" >
          <k-grid-column :data-sortable="true" data-align="center" data-header="文档id" data-hidden="true" data-name="documentId"/>
          <k-grid-column data-align="center" data-header="序列" data-name="documentNum" data-width="60" />
          <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode" data-width="150"/>
          <k-grid-column data-align="center" data-header="文档名称" data-name="documentName" />
          <k-grid-column data-align="center" data-header="文档路径" data-name="documentPath" data-hidden="true" />
          <k-grid-column data-align="center" data-header="文档类型" data-name="templateType" data-hidden="true" />
          <k-grid-column data-align="center" data-header="文档类型" data-name="templateLabel" data-width="180"/>
          <k-grid-column data-align="center" data-header="文档子类型" data-name="templateSonType" data-hidden="true"/>
          <k-grid-column data-align="center" data-header="文档子类型" data-name="templateSonLabel" data-width="180" />
          <template slot="operate" slot-scope="scope" >
            <k-btn class="btn-custom-text specialClass" :data-download-name="scope.row.row.documentName"
                   :data-disabled="scope.row.row.documentPath===''"
                   data-descript="下载文档" data-functype="DOWNLOAD" data-size="mini" v-model="scope.row.row"
                   data-url="/download/server/PmsApp/materialController/DocumentDownloadAction.action">
              下载
            </k-btn>
          </template>
        </k-grid>
      </div>
      <div>
        <k-form>
          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" :data-download-name="downloadData.prodCode + '_产品销售文件.zip'"
                   data-descript="下载文档" data-functype="DOWNLOAD" data-size="mini" :data-model="downloadData"
                   data-url="/download/server/PmsApp/materialController/SalesDownloadAction.action">
              下载全部
            </k-btn>
            <k-btn data-functype="CLOSE" class="btn-custom-plain"><md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
      </div>
    </k-popup>


    <k-popup ref="downloadHisPopup" :data-title="downloadData.templateLabel+'-历史记录管理'" data-width="70%" :dataDialogDrag="true">
      <div>
        <k-grid ref="downloadHisGrid" @data-row-select="selectDownloadRow" :data-checkbox="false" data-checkbox-id="id" data-fixed="right"
                data-action='MaterialDocument.findHisDocumentByType' @init="(grid)=>{this.$kgrid = grid}" data-operate-width="230px"
                :data-params="{prodCode:downloadData.prodCode,templateType:downloadData.templateType}" >
          <k-grid-column data-align="center" data-header="序列"  data-name="documentId" data-width="60"/>
          <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode" data-width="120"/>
          <k-grid-column data-align="center" data-header="文档名称" data-name="documentName" />
          <k-grid-column data-align="center" data-header="文档路径" data-name="documentPath" data-hidden="true" />
          <k-grid-column data-align="center" data-header="文档类型" data-name="templateType" data-hidden="true" />
          <k-grid-column data-align="center" data-header="文档类型" data-name="templateLabel" data-width="120" />
          <k-grid-column data-align="center" data-header="文档子类型" data-name="templateSonType" data-hidden="true"/>
          <k-grid-column data-align="center" data-header="文档子类型" data-name="templateSonLabel" data-width="120" />
          <k-grid-column data-align="center" data-header="创建方式" data-name="handUpload" data-dict="pids_hand_upload" data-width="100" />
          <k-grid-column data-align="center" data-header="创建日期" data-name="crtDate" data-width="100" />
          <k-grid-column data-align="center" data-header="创建人" data-name="crtUser" data-width="100" />
          <template slot="operate" slot-scope="scope" >
            <k-btn class="btn-custom-text specialClass" :data-download-name="scope.row.row.documentName"
                   data-descript="下载文档" data-functype="DOWNLOAD" data-size="mini" v-model="scope.row.row"
                   data-url="/download/server/PmsApp/materialController/DocumentDownloadAction.action">
              下载
            </k-btn>
            <k-btn class="btn-custom-text specialClass" data-descript="回滚文档" data-functype="SUBMIT" data-size="mini" v-model="scope.row.row"
                   data-action="MaterialDocument.rollBackMaterialDocument" :data-confirm="true" data-target="DocumentGrid">
              回滚文档
            </k-btn>
            <k-btn class="btn-custom-text specialClass" data-descript="删除文档" data-functype="SUBMIT" data-size="mini" v-model="scope.row.row"
                   data-action='MaterialDocument.deleteHisDocumentById' :data-confirm="true" data-target="downloadHisGrid">
              删除
            </k-btn>
          </template>
        </k-grid>
      </div>
      <div>
        <k-form>
          <k-form-footer data-align="center" >
            <k-btn class="btn-custom-primary" :data-download-name="downloadData.prodCode + downloadData.templateLabel + '_历史版本文件.zip'"
                   data-descript="下载全部" data-functype="DOWNLOAD" data-size="mini" :data-model="downloadData"
                   data-url="/download/server/PmsApp/materialController/HisDownloadAction.action">
              下载全部
            </k-btn>
            <k-btn class="md-danger" data-descript="删除全部" data-functype="SUBMIT" data-size="mini" :data-model="downloadData"
                   data-action='MaterialDocument.deleteHisDocumentByType' :data-confirm="true">
              删除全部
            </k-btn>
            <k-btn data-functype="CLOSE" class="btn-custom-plain"><md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
      </div>
    </k-popup>



  </div>
</template>

<script>
import {assign} from "lodash";
import Tools from "@/utils/tools";

export default {
  name: "MaterialDocument",
  data() {
    return {
      SearchParam: {},//查询参数
      formData: {},
      formDownloadData: {},
      downloadData:{},

      templateTypeDict:[],
    };
  },
  computed: {

  },

  methods: {
    selectRow(row, column, event) {
      const _this = this
      _this.formData = assign({}, row)
      this.$refs.DocumentGrid.load({prodCode: row.prodCode});
    },

    selectDownloadRow(row, column, event) {
      const _this = this
      _this.formDownloadData = assign({}, row)
    },
    DocumentGridRow(row, column, event) {
      const _this = this
      _this.downloadData = assign({}, row)
    },

    onFileSubmitError(){
      this.$refs.fileUploadRef.doReset();
      this.$refs.fileSubmitBtn.setIconStyle(1, []);
    },
    onFileSubmitSuccess() {
      this.$refs.DocumentGrid.load({prodCode: this.formData.prodCode});
      this.$refs.fileUploadRef.doReset();
      this.$refs.uploadFrom.reset();
      this.$refs.uploadDocumentPopup.close();

    },
    checkFile(file){
      if(file !=null && file !='' && file !=undefined){
         return;
      }else{
        Tools.alert("上传文件不能为空!", "danger")
        return ;
      }
    },
    fileSubmitUploadParam(){
      let formData = this.formData;
      let validate = this.$refs.uploadFrom.validate();
      if(validate){
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          this.$refs.fileUploadRef.upload(formData);
        } else {
          Tools.alert("上传文件不能为空!", "danger")
          return false;
        }
      }
    },

    templateTypeChange(val) {
      this.httpUtil.comnQuery({
        action: "MaterialTemplate.getTemplateSonTypeDict",
        params: {templateType: val}
      }).then(data => {
        this.templateTypeDict = data.rows;
      })
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
