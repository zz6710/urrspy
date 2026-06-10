<template>
  <div>
    <k-form-search-customize data-target="t8ProdRiskGrid" v-model="queryParam">
      <k-form-item label="风险名称">
        <k-field-select v-model="queryParam.id"  data-action="T8ProdRiskConfig.findProdRiskConfig"
                        data-display-field="prodRisk" data-value-field="id" ></k-field-select>
      </k-form-item>
      <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addPopup"
             v-if="global.isShowAuthorityButton('T8ProdRiskConfig.addProdRiskConfig')">
        <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
    </k-form-search-customize>

    <!--  grid模板列表  -->
    <k-grid ref="t8ProdRiskGrid" data-action="T8ProdRiskConfig.findProdRiskConfig1" @data-row-select="selectRow">
      <k-grid-column data-align="center" data-header="序号" data-name="numberId" data-width="100"/>
      <k-grid-column data-align="center" data-header="风险名称" data-name="prodRisk"  data-width="200"/>
      <k-grid-column data-align="center" data-header="风险描述" data-name="prodRiskDesc" data-width="1200"/>
      <template slot="operate" slot-scope="scope">
        <k-btn data-functype="POPUP" data-confirm data-size="mini" class="md-info md-just-icon md-simple"
               data-target="editPopup" data-descript="修改" v-if="global.isShowAuthorityButton('T8ProdRiskConfig.updProdRiskConfig')">
          <md-icon>edit</md-icon>
        </k-btn>
        <k-btn data-functype="SUBMIT" data-size="mini" class="md-danger md-just-icon md-simple" data-descript="删除"
               data-target="t8ProdRiskGrid"   data-action="T8ProdRiskConfig.deleteProdRiskConfig" data-confirm data-type="danger"
               v-if="global.isShowAuthorityButton('T8ProdRiskConfig.deleteProdRiskConfig')">
          <md-icon>close</md-icon>
        </k-btn>
      </template>

    </k-grid>

    <!--  风险新增  -->
    <k-popup ref="addPopup" title="添加" :dataDialogDrag="true">
      <T8ProdRiskConfigPopup v-model="formData" :formData="formData" ref="addT8ProdRiskData" />
      <k-form>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdRiskConfig.addProdRiskConfig"
                 data-from="t8ProdRiskConfigForm" :data-model="formData" data-target="t8ProdRiskGrid"
                 :data-handler="addValidateData">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--  风险编辑  -->
    <k-popup ref="editPopup" data-title="修改" :dataDialogDrag="true">
      <T8ProdRiskConfigPopup v-model="formData" :formData="formData" ref="editT8ProdRiskData" />
      <k-form>

        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdRiskConfig.updProdRiskConfig"
                 data-from="t8ProdRiskConfigForm" :data-model="formData" data-target="t8ProdRiskGrid"
                 :data-handler="editValidateData" >
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
  import T8ProdRiskConfigPopup from "./T8ProdRiskConfigPopup";

  export default {
    components: {T8ProdRiskConfigPopup},
    data() {
      return {
        formData: {},
        selectRowData: {},
        queryParam: {},
      };
    },
    methods: {

      addValidateData() {
        return this.$refs.addT8ProdRiskData.validateData();
      },
      editValidateData() {
        return this.$refs.editT8ProdRiskData.validateData();
      },

      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },

    }
  }
</script>

<style scoped>

</style>
