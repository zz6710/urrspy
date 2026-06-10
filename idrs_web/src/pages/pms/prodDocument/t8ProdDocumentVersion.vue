<template>
  <div>
    <k-form-search-customize data-target="prodInfoGrid" v-model="prodSearchParam">
      <k-form-item label="产品代码">
        <k-field-select v-model="prodSearchParam.prodCode" data-action="T8Dict.findNotEstablishProdInfos"
                        data-display-field="prodCode,prodName" data-value-field="prodCode"/>
      </k-form-item>
    </k-form-search-customize>

    <k-grid ref="prodInfoGrid" data-action="ProdFileCabinets.find" @data-row-select="selectRow"
            data-diffcondition="id,parentId" :data-show-tree="true" dataTreeId="id">
      <k-grid-column data-align="center" data-header="产品代码" data-name="prodCode"/>
      <k-grid-column data-align="center" data-header="文件名称" data-name="fileName"/>
      <template slot="operate" slot-scope="scope">

      </template>
    </k-grid>
  </div>
</template>

<script>
  import {assign} from "lodash";
  export default {
    name: "",
    data() {
      return {
        prodSearchParam:{
          prodCode:''
        },
        formData:{
          prodCode:'',
          prodName:'',
          version:'',
          remark:'',
          docType:''
        },
        addPopupTitle:'',
      }
    },
    methods:{
      selectRow(row, column, event){
        const _this = this;
        _this.selectRowData = assign({}, row);
      }
    }
  }
</script>

<style scoped>

</style>
