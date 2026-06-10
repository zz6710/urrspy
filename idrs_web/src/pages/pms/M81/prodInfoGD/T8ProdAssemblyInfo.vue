<template>
    <div>
      <k-form-search-customize data-target="prodAssemblyGrid" v-model="formData">
        <k-form-item label="模板名称">
          <k-field-select v-model="formData.assemblyId"  data-action="T8ProdAssembly.findProdAssemblyInfo"
                          data-display-field="assemblyDesc" data-value-field="assemblyId" ></k-field-select>
        </k-form-item>
        <k-form-item label="是否启用">
          <k-field-select v-model="formData.assemblyStatus"  data-dict="status" ></k-field-select>
        </k-form-item>
        <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addAssemblyPopup"
                v-if="global.isShowAuthorityButton('T8ProdAssembly.addProdAssemblyInfo')">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
      </k-form-search-customize>

      <k-grid ref="prodAssemblyGrid" data-action="T8ProdAssembly.findProdAssemblyInfo1" @data-row-select="selectRow"  >
        <k-grid-column data-header="组件名称" data-name="assemblyDesc" />
        <k-grid-column data-header="组件id" data-name="assemblyId"/>
        <k-grid-column data-header="默认菜单点亮" data-name="alive" data-dict="true_false"/>
        <k-grid-column data-header="鼠标悬停效果" data-name="mouseOver" />
        <k-grid-column data-header="数据是否验证" data-name="validate" data-dict="true_false"/>
        <k-grid-column data-header="菜单渲染图标" data-name="iconClass"/>
        <k-grid-column data-header="菜单作用渲染图标" data-name="activeClass" />
        <k-grid-column data-header="排序顺序" data-name="assemblySort" />
        <k-grid-column data-header="是否启用" data-name="assemblyStatus" data-dict="status"/>
        <template slot="operate" slot-scope="props">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改组件" data-functype="POPUP" data-size="mini"
                 data-target="editAssemblyPopup" v-if="global.isShowAuthorityButton('T8ProdAssembly.updateProdAssemblyInfo')">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT"
                 data-action="T8ProdAssembly.deleteProdAssemblyInfo"
                 data-size="mini" data-type="danger" data-target="prodAssemblyGrid" :data-confirm="true"
                 data-descript="删除" v-if="global.isShowAuthorityButton('T8ProdAssembly.deleteProdAssemblyInfo')">
            <md-icon>close</md-icon>
          </k-btn>
        </template>
      </k-grid>



      <k-popup ref="addAssemblyPopup" data-title="产品组件新增">
        <k-form ref="addAssemblyFrom" :data-col="2">
          <k-form-item label="组件名称">
            <k-field-text v-model="formData.assemblyDesc"  :data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="组件ID">
            <k-field-text v-model="formData.assemblyId"  :data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="默认菜单点亮">
            <k-field-select v-model="formData.alive"  :data-allowblank="false" data-dict="true_false"/>
          </k-form-item>
          <k-form-item label="鼠标悬停效果">
            <k-field-text v-model="formData.mouseOver"   />
          </k-form-item>
          <k-form-item label="排序顺序">
            <k-field-text v-model="formData.assemblySort"  data-validate-type="number" data-type="number" :data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="数据是否验证">
            <k-field-select v-model="formData.validate"  :data-allowblank="false" data-dict="true_false"/>
          </k-form-item>
          <k-form-item label="菜单渲染图标">
            <k-field-text v-model="formData.iconClass"  :data-allowblank="false" />
          </k-form-item>
          <k-form-item label="菜单作用渲染图标">
            <k-field-text v-model="formData.activeClass"  :data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="组件类型">
            <k-field-select v-model="formData.assemblyType"  :data-allowblank="false" data-dict="t8_assembly_type"/>
          </k-form-item>
          <k-form-item label="是否启用">
            <k-field-select v-model="formData.assemblyStatus"  :data-allowblank="false" data-dict="status"/>
          </k-form-item>

          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="prodAssemblyGrid" data-action="T8ProdAssembly.addProdAssemblyInfo"
                   data-from="addAssemblyFrom" :data-model="formData">
              <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
            </k-btn>

            <k-btn class="btn-custom-plain" data-functype="CLOSE">
              <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
          </k-form-footer>
        </k-form>
      </k-popup>


      <k-popup ref="editAssemblyPopup" data-title="产品组件修改">
        <k-form ref="editAssemblyFrom" :data-col="2">
          <k-form-item label="组件名称">
            <k-field-text v-model="updateData.assemblyDesc"  :data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="默认菜单点亮">
            <k-field-select v-model="updateData.alive"  :data-allowblank="false" data-dict="true_false"/>
          </k-form-item>
          <k-form-item label="鼠标悬停效果">
            <k-field-text v-model="updateData.mouseOver"   />
          </k-form-item>
          <k-form-item label="数据是否验证">
            <k-field-select v-model="updateData.validate"  data-allowblank="false" data-dict="true_false"/>
          </k-form-item>
          <k-form-item label="排序顺序">
            <k-field-text v-model="updateData.assemblySort"  data-validate-type="number" data-type="number" :data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="菜单渲染图标">
            <k-field-text v-model="updateData.iconClass"  data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="菜单作用渲染图标">
            <k-field-text v-model="updateData.activeClass"  data-allowblank="false"/>
          </k-form-item>
          <k-form-item label="组件类型">
            <k-field-select v-model="updateData.assemblyType"  :data-allowblank="false" data-dict="t8_assembly_type"/>
          </k-form-item>
          <k-form-item label="是否启用">
            <k-field-select v-model="updateData.assemblyStatus"  data-allowblank="false" data-dict="status"/>
          </k-form-item>

          <k-form-footer data-align="center">
            <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="prodAssemblyGrid" data-action="T8ProdAssembly.updateProdAssemblyInfo"
                   data-from="editAssemblyFrom" :data-model="updateData">
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
        formData: {},
        updateData: {},
        selectRowData: {},
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = assign({}, row);
        this.updateData = assign({}, row);
      },

    }
  }


</script>

<style scoped>

</style>
