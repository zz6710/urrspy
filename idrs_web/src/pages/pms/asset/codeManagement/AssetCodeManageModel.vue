<template>
  <div>
    <div>
      <k-form-search-customize  data-model-name="AssetCodeManageModel" data-target="AssetCodeManageModelGrid" v-model = "searchParam">
        <k-form-item label="原资产代码" >
          <k-field-text v-model="searchParam.oldScrCd" />
        </k-form-item>
        <k-form-item label="新资产名称" >
          <k-field-text v-model="searchParam.scrCd" />
        </k-form-item>
        <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}"
               data-target="addAssetCodeManageModelPopup" slot="button" v-if="global.isShowAuthorityButton('AssetCodeManageModel.insertAssetCodeManage')">
          <md-icon md-src="/static/svg/add.svg" />新增
        </k-btn>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="AssetCodeManageModelGrid" @data-row-select="selectRow" data-action="AssetCodeManageModel.findAssetCodeManageModels" data-fixed="right" data-operate-width="160px" >
        <k-grid-column data-header="ID" data-name="id" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="原资产代码" data-name="oldScrCd"></k-grid-column>
        <k-grid-column data-header="新资产代码" data-name="scrCd"></k-grid-column>
        <k-grid-column data-header="资产名称" data-name="scrNm" ></k-grid-column>
        <k-grid-column data-header="资产分类" data-name="assetType" data-dict ="cbndFrsCtg"></k-grid-column>
        <k-grid-column data-header="生效日期" data-name="effectiveDate" ></k-grid-column>
        <k-grid-column data-header="生效时间" data-name="effectiveTime" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="失效日期" data-name="expirationDate" ></k-grid-column>
        <k-grid-column data-header="失效时间" data-name="expirationTime" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="生效状态" data-name="status" data-dict="asset_code_status"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info specialClass" data-descript="修改资产代码信息" data-functype="POPUP" data-size="mini"
                 data-target="editAssetCodeManageModelPopup" v-if="global.isShowAuthorityButton('AssetCodeManageModel.updateAssetCodeManage')">
            修改
          </k-btn>
          <k-btn class="md-danger specialClass" data-functype="SUBMIT" data-action="AssetCodeManageModel.deleteAssetCodeManage" data-size="mini" v-if="global.isShowAuthorityButton('AssetCodeManageModel.deleteAssetCodeManage')"
                 data-type="danger" data-target="AssetCodeManageModelGrid" :data-confirm="true" data-descript="删除资产代码信息">
            删除
          </k-btn>
          <k-btn class="md-info specialClass" data-descript="详情信息" data-functype="POPUP" data-size="mini"
                 data-target="detailAssetCodeManageModelPopup">
            详情
          </k-btn>
        </template>
      </k-grid>
    </div>
    <!--    新增资产代码信息弹出框   -->
    <k-popup ref="addAssetCodeManageModelPopup" data-title="新增">
      <k-form ref="addAssetCodeManageModelForm" :data-col="2">
        <k-form-item label="ID" v-show="false">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="资产类型">
          <k-field-select v-model="formData.assetType" data-dict="cbndFrsCtg" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="市场">
          <k-field-select v-model="formData.trxMkt" data-dict="market_asset" />
        </k-form-item>
        <k-form-item label="原资产代码">
          <k-field-text v-model="formData.oldScrCd" :data-allowblank="false"  :data-max-length="50"/>
        </k-form-item>
        <k-form-item label="新资产代码">
          <k-field-text v-model="formData.scrCd" :data-allowblank="false" :data-max-length="50" />
        </k-form-item>
        <k-form-item label="资产代码名称">
          <k-field-text v-model="formData.scrNm"  :data-allowblank="false" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="数据来源">
          <k-field-select v-model="formData.dataSource" data-dict="asset_code_datasource" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="生效状态">
          <k-field-select v-model="formData.status" data-dict="asset_code_status" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="生效日期">
          <k-field-date v-model="formData.effectiveDate"  :data-allowblank="false" :dataMaxValue="formData.expirationDate"/>
        </k-form-item>
        <k-form-item label="失效日期">
          <k-field-date v-model="formData.expirationDate"   :dataMinValue="formData.effectiveDate" />
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AssetCodeManageModel.insertAssetCodeManage" data-from="addAssetCodeManageModelForm"
                 :data-model="formData" data-target="AssetCodeManageModelGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
    <!--    修改资产代码信息弹出框   -->
    <k-popup ref="editAssetCodeManageModelPopup" data-title="修改">
      <k-form ref="editAssetCodeManageModelForm" :data-col="2">
        <k-form-item label="ID" v-show="false">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="资产类型">
          <k-field-select v-model="formData.assetType" data-dict="cbndFrsCtg"   :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="市场">
          <k-field-select v-model="formData.trxMkt" data-dict="market_asset"/>
        </k-form-item>
        <k-form-item label="原资产代码">
          <k-field-text v-model="formData.oldScrCd" :data-disabled ="true" />
        </k-form-item>
        <k-form-item label="新资产代码">
          <k-field-text v-model="formData.scrCd" :data-allowblank="false" :data-max-length="50" />
        </k-form-item>
        <k-form-item label="资产代码名称">
          <k-field-text v-model="formData.scrNm" :data-disabled ="true" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="数据来源">
          <k-field-select v-model="formData.dataSource" data-dict="asset_code_datasource" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="生效状态">
          <k-field-select v-model="formData.status" data-dict="asset_code_status" :data-allowblank="false" />
        </k-form-item>
        <k-form-item label="生效日期">
          <k-field-date v-model="formData.effectiveDate"  :data-allowblank="false"  :dataMaxValue="formData.expirationDate" />
        </k-form-item>
        <k-form-item label="失效日期">
          <k-field-date v-model="formData.expirationDate"  :dataMinValue="formData.effectiveDate"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AssetCodeManageModel.updateAssetCodeManage" data-from="editAssetCodeManageModelForm"
                 :data-model="formData" data-target="AssetCodeManageModelGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    详情   -->
    <k-popup ref="detailAssetCodeManageModelPopup" data-title="详情">
      <k-form ref="detailAssetCodeManageModelForm" :data-col="2">
        <k-form-item label="ID" v-show="false">
          <k-field-text v-model="formData.id"/>
        </k-form-item>
        <k-form-item label="资产类型">
          <k-field-select v-model="formData.assetType" data-dict="cbndFrsCtg"   :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="市场">
          <k-field-select v-model="formData.trxMkt" data-dict="market_asset"  :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="原资产代码">
          <k-field-text v-model="formData.oldScrCd" :data-disabled ="true"/>
        </k-form-item>
        <k-form-item label="新资产代码">
          <k-field-text v-model="formData.scrCd"  :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="资产代码名称">
          <k-field-text v-model="formData.scrNm" :data-disabled ="true"/>
        </k-form-item>
        <k-form-item label="数据来源">
          <k-field-select v-model="formData.dataSource" data-dict="asset_code_datasource"   :data-disabled ="true" />
        </k-form-item>
        <k-form-item label="生效状态">
          <k-field-select v-model="formData.status" data-dict="asset_code_status"   :data-disabled ="true" />
        </k-form-item>
        <k-form-item label="生效日期">
          <k-field-date v-model="formData.effectiveDate"   :dataMaxValue="formData.expirationDate" :data-disabled ="true" />
        </k-form-item>
        <k-form-item label="失效日期">
          <k-field-date v-model="formData.expirationDate"    :dataMinValue="formData.effectiveDate" :data-disabled ="true"/>
        </k-form-item>
        <k-form-footer data-align="center">
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>关闭</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
export default {
  name:"AssetCodeManageModel",
  data() {
    return {
      formData: {},
      selectRowData: {},
      searchParam:{}
    };
  },
  methods: {
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
    }
  }
};
</script>
<style scoped lang="scss">
@import "../AssetComFunction.css";
</style>
