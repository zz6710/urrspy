<template>
  <div>
    <k-form-search-customize data-target="t8ProdDeclaraModelGrid" v-model="searchForm">
      <k-form-item label="模板名称">
        <k-field-select v-model="searchForm.id"  data-action="T8ProdDeclaraModel.findT8ProdDeclaraModels"
                        data-display-field="modelName" data-value-field="id" ></k-field-select>
      </k-form-item>
      <k-form-item label="产品经理">
        <k-field-select v-model="searchForm.designerName"  data-action="T8ProdDeclaraModel.findT8ProdDeclaraModels"
                        data-display-field="designerName" data-value-field="designerName" ></k-field-select>
      </k-form-item>
      <k-form-item label="投资经理名称">
        <k-field-select v-model="searchForm.investManageName"  data-action="T8ProdDeclaraModel.findT8ProdDeclaraModels"
                        data-display-field="investManageName" data-value-field="investManageName" ></k-field-select>
      </k-form-item>
      <k-form-item label="产品定价方式" >
        <k-field-select   v-model="searchForm.prodPriceWay"  data-dict="prod_price_way" ></k-field-select>
      </k-form-item>
      <k-form-item label="合作模式" >
        <k-field-select   v-model="searchForm.cooperationMode" data-dict="cooperation_mode"  ></k-field-select>
      </k-form-item>
      <k-form-item label="资金投向地区" >
        <k-field-select   v-model="searchForm.investRegion" data-dict="invest_region"   ></k-field-select>
      </k-form-item>
      <k-btn slot="button"  class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.searchForm={}" data-target="addT8ProdDeclaraModelPopup"
             v-if="global.isShowAuthorityButton('T8ProdDeclaraModel.addT8ProdDeclaraModel')">
        <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
    </k-form-search-customize>
    <div>
      <k-grid ref="t8ProdDeclaraModelGrid" @data-row-select="selectRow" data-action="T8ProdDeclaraModel.findT8ProdDeclaraModels1" >
		<k-grid-column data-header="模板名称" data-name="modelName"  data-width="120"></k-grid-column>
		<k-grid-column data-header="产品经理" data-name="designerName" data-width="120"></k-grid-column>
		<k-grid-column data-header="投资经理名称" data-name="investManageName" data-width="120"></k-grid-column>
		<k-grid-column data-header="业务联系人姓名" data-name="businessContactName" data-width="120"></k-grid-column>
    <k-grid-column data-header="产品审批人姓名" data-name="approverName" data-width="120"></k-grid-column>
		<k-grid-column data-header="资金投向地区" data-name="investRegion" data-dict="invest_region" data-width="120"></k-grid-column>
		<k-grid-column data-header="产品资产配置方式" data-name="prodAssetAllocation" data-dict="t8_prodAssetAllocation" data-width="150"></k-grid-column>
		<k-grid-column data-header="产品管理模式" data-name="prodManageMode" data-dict="prod_manage_mode" data-width="120"></k-grid-column>
		<k-grid-column data-header="实际管理人名称" data-name="actualManagerName" data-width="120"></k-grid-column>
		<k-grid-column data-header="产品定价方式" data-name="prodPriceWay" data-dict="prod_price_way" data-width="120"></k-grid-column>
		<k-grid-column data-header="合作模式" data-name="cooperationMode"  data-dict="cooperation_mode" data-width="100"></k-grid-column>
		<k-grid-column data-header="产品增信标识" data-name="prodCreditLogo"  data-dict="prod_credit_logo" data-width="100"></k-grid-column>
    <k-grid-column data-header="备注" data-name="remarks"  data-width="150"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="产品申报要素模板详情" data-functype="POPUP" data-size="mini"
                 data-target="libraryT8ProdDeclaraModelPopup">
            <md-icon>library_books</md-icon>
          </k-btn>
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品申报要素模板" data-functype="POPUP" data-size="mini"
            data-target="editT8ProdDeclaraModelPopup"
                 v-if="global.isShowAuthorityButton('T8ProdDeclaraModel.updateT8ProdDeclaraModel')">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="T8ProdDeclaraModel.deleteT8ProdDeclaraModel"
                 data-size="mini"
               data-type="danger" data-target="t8ProdDeclaraModelGrid" :data-confirm="true" data-descript="删除产品申报要素模板"
                 v-if="global.isShowAuthorityButton('T8ProdDeclaraModel.deleteT8ProdDeclaraModel')">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>

    <!--  20210306 axin 添加组键-->

    <!--    产品申报要素模板详情  -->
    <k-popup ref="libraryT8ProdDeclaraModelPopup" data-title="详情" :dataDialogDrag="true">
      <T8ProdDeclaraModelParamsLibrary v-model="formData" :formData="formData"/>
    </k-popup>

	<!--    添加产品申报要素模板弹出框   -->
	<k-popup ref="addT8ProdDeclaraModelPopup" data-title="新增" :dataDialogDrag="true"  >
    <T8ProdDeclaraModelParams v-model="formData" :formData="formData" ref="addT8ProdDeclaraModelData" />
    <k-form>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" ref="sureBtn"
               data-from="addT8ProdDeclaraModelForm" @click="addValidateData"
               :data-model="formData" data-target="t8ProdDeclaraModelGrid">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消
        </k-btn>
      </k-form-footer>
    </k-form>

	</k-popup>


	<!--    修改产品申报要素模板弹出框   -->
	<k-popup ref="editT8ProdDeclaraModelPopup" data-title="修改" :dataDialogDrag="true">
    <T8ProdDeclaraModelParams v-model="formData" :formData="formData"  ref="editT8ProdDeclaraModelData" />
    <k-form>

      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT"
               data-from="addT8ProdDeclaraModelForm" @click="editValidateData"
               :data-model="formData" data-target="t8ProdDeclaraModelGrid" ref="sureUpdBtn">
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
import T8ProdDeclaraModelParams from './T8ProdDeclaraModelParams';
import T8ProdDeclaraModelParamsLibrary from '../prodDisplay/DisplayDeclaraModelParams';
import Tools from '@/utils/tools.js'
  export default {
    components: {T8ProdDeclaraModelParams, T8ProdDeclaraModelParamsLibrary},
    data() {
      return {
         searchForm: {},
        formData: {},
        selectRowData: {},
        saveLoading: false,
      };
    },
    methods: {
      addValidateData() {
        let result = this.$refs.addT8ProdDeclaraModelData.validateData();
          if(result===true){
              this.httpUtil.comnQuery({
              action: "T8ProdDeclaraModel.findT8ProdDeclaraModel",
              params: {
                modelName: this.formData.modelName
              },
            })
            .then((data) => {
                if (data.rows.length > 0) {
                  Tools.alert("模板名称已存在！", "danger");
                  this.$refs.sureBtn.loading = false;
                  return false;

                }else{
                    this.httpUtil.comnUpdate({
                      action: 'T8ProdDeclaraModel.addT8ProdDeclaraModel',
                      params: this.formData,

                    }).then(res => {

                      if(res.success==true){
                          this.$refs.addT8ProdDeclaraModelPopup.close();
                          this.$refs.t8ProdDeclaraModelGrid.load();
                      }
                    });
                }
            });
          }
          return result;

      },
      editValidateData() {

        let result = this.$refs.editT8ProdDeclaraModelData.validateData();
          if(result===true){
              this.httpUtil.comnQuery({
              action: "T8ProdDeclaraModel.findT8ProdDeclaraModel",
              params: {
                modelName: this.formData.modelName,
                id: this.formData.id
              },
            })
            .then((data) => {
                if (data.rows.length > 0) {
                  Tools.alert("模板名称已存在！", "danger");
                  this.$refs.sureUpdBtn.loading = false;
                  return false;

                }else{
                    this.httpUtil.comnUpdate({
                      action: 'T8ProdDeclaraModel.updateT8ProdDeclaraModel',
                      params: this.formData,

                    }).then(res => {

                        if(res.success==true){
                          this.$refs.editT8ProdDeclaraModelPopup.close();
                          this.$refs.t8ProdDeclaraModelGrid.load();
                      }
                    });
                }
            });
          }
          return result;


      },

      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
	  },

    }
  };
</script>
