<template>

  <div>

    <k-form-search data-model-name="T8ProdAssembly" data-target="prodAssemblyGrid">

    </k-form-search>

    <k-grid ref="prodAssemblyGrid" data-action="T8ProdInfo.findProdInfos" @data-row-select="selectRow"  >

      <k-grid-column data-header="产品代码" data-name="prodCode" />
      <k-grid-column data-header="产品名称" data-name="prodName" />
      <k-grid-column data-header="成立日" data-name="prodCode"/>
      <k-grid-column data-header="到期日" data-name="prodCode"/>
      <template slot="operate" slot-scope="props">
        <k-btn class="md-info md-just-icon md-simple" data-descript="新增产品操作信息" data-functype="POPUP" data-size="mini"
               data-target="addAssemblyPopup">
          <md-icon md-src="/static/svg/add.svg" />
        </k-btn>
      </template>
    </k-grid>




    <k-grid ref="T8ProdMenuItemsGrid" :data-operate-="false" :data-autoload="false" @data-row-select="selectRowMenu"
            data-action="T8ProdAssembly.findAssemblyMenu" :data-page-size=5>
      <k-grid-column data-header="产品代码" data-name="prodCode" />
      <k-grid-column data-header="操作类型" data-name="menuItemsType" data-dict="t8_menu_items_type"/>
      <k-grid-column data-header="组件id" data-name="assemblyId" />
      <k-grid-column data-header="组件名称" data-name="assemblyDesc" />
      <template slot="operate" slot-scope="props">
        <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="T8ProdAssembly.deleteAssemblyMenu" data-size="mini"
               data-type="danger" data-target="T8ProdMenuItemsGrid" :data-confirm="true" data-descript="删除">
          <md-icon md-src="/static/svg/delete.svg" />
        </k-btn>
      </template>
    </k-grid>


    <k-popup ref="addAssemblyPopup" data-title="产品组件设置">
      <k-form ref="addAssemblyFrom" :data-col="2">
        <k-form-item label="产品代码">
          <k-field-select v-model="fromDate.prodCode"  data-action="T8Dict.findTaProdInfos" data-display-field="prodCode,prodName"
                          data-disabled data-value-field="prodCode" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="产品操作类型">
          <k-field-select v-model="fromDate.menuItemsType" data-dict="t8_menu_items_type" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="组件">
          <k-field-select v-model="fromDate.assemblyId" data-action="T8ProdAssembly.findProdAssemblyInfo"  :data-allowblank="false"
                          data-display-field="assemblyId,assemblyDesc" data-value-field="assemblyId" />
        </k-form-item>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="T8ProdMenuItemsGrid" data-action="T8ProdAssembly.addAssemblyMenu"
                 data-from="addAssemblyFrom" :data-model="fromDate">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>



  </div>
</template>

<script>
  import {assign} from "lodash";

  export default {
    data() {
      return {
        fromDate : {},
        selectRowData: {},
        AssemblyData: {},
      };
    },
    methods: {

      selectRow(row, column, event) {
        this.selectRowData = assign({}, row);
        this.fromDate = assign({}, row);
        this.$refs.T8ProdMenuItemsGrid.load({prodCode: row.prodCode});
      },

      selectRowMenu(row, column, event){
        this.AssemblyData = assign({}, row);
      },

      addProdAssembly (){},



    }
  }



</script>

<style scoped>

</style>
