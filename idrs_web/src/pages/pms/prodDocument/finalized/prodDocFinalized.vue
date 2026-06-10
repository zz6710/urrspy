<template>
  <div>
    <k-form-search-customize data-target="prodDocumentGrid" v-model="prodSearchParam">
      <k-form-item label="产品代码">
        <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findNotEstablishProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"/>
      </k-form-item>
    </k-form-search-customize>
    <k-grid ref="prodDocumentGrid"  data-action="ProdDocument.getProdDocumentFinalizedList">
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="产品名称" data-name="prodName"/>
      <k-grid-column data-align="center" data-header="文档名称" data-name="documentName"/>
      <k-grid-column data-align="center" data-header="文档版本" data-name="version"/>
      <k-grid-column data-align="center" data-header="定稿状态" data-dict="finalized_status" data-name="finalizedStatus"/>
      <k-grid-column data-align="center" data-header="文档类型" data-dict="t8_temp_type" data-name="documentType"/>
      <template slot="operate" slot-scope="scope">
        <k-btn class="md-info md-just-icon md-simple" :data-disabled="scope.row.row.finalizedStatus == '1'" data-descript="产品文档定稿" data-functype="SUBMIT" data-size="small"
               data-action="ProdDocument.finalizedProdDocument" data-target="prodDocumentGrid" v-model="scope.row.row" :data-confirm="true">
          <md-icon v-show="scope.row.row.finalizedStatus == '1'">star</md-icon>
          <md-icon v-show="scope.row.row.finalizedStatus != '1'">star_border</md-icon>
        </k-btn>
        <k-btn class="md-info md-just-icon md-simple" :data-disabled="scope.row.row.finalizedStatus == '0'"  :data-download-name="scope.row.row.prodName+'-'+scope.row.row.documentName+'.pdf'"  data-descript="下载定稿文档" data-functype="DOWNLOAD" data-size="small"
               data-url="/download/server/PmsApp/prodDocument/downloadFinalizedT8ProdDocumentVersion.json" v-model="scope.row.row">
          <md-icon>cloud_download</md-icon>
        </k-btn>
      </template>
    </k-grid>
  </div>
</template>

<script>
  import Tools from "../../../../utils/tools";
  export default {
    name: "",
    data() {
      return {
        prodSearchParam: {
          prodCode:'',
        },
      }
    },
    methods:{
    }

  }
</script>

<style scoped>

</style>
