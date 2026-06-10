<template>
  <div class="py-page">
    <div>
		<k-form-search-customize data-model-name="DwsG06BIIFbAssetInfo" v-model="searchParam" data-target="tableGrid" data-label-width="150px">
      <k-form-item label="数据日期">
        <k-field-date v-model="searchParam.reportDate" data-type="month" data-date-format="yyyy-MM" data-value-format="yyyyMM" :data-allowblank="false"/>
      </k-form-item>
			<k-form-item label="产品代码">
          <k-field-text v-model="searchParam.prodCode"/>
      </k-form-item>
			<k-form-item label="资产代码">
          <k-field-text v-model="searchParam.assetCode"/>
      </k-form-item>
			<k-form-item label="穿透后分类">
          <k-field-select v-model="searchParam.g06Type" data-dict-type="1" data-dict="g06_scd_type_fb"/>
      </k-form-item>
		</k-form-search-customize>
    </div>
    <div class="py-page-container">
        <div class="left">
          <k-btn slot="button" class="btn-custom-plain" data-functype="EXPORT" data-target="tableGrid" data-export-name="G06BII非标资产明细中间表">
            <md-icon>cloud_download</md-icon>导出
          </k-btn>
        </div>
      <k-grid ref="tableGrid" @data-row-select="selectRow" data-action="DwsG06BIIFbAssetInfo.findDwsG06BIIFbAssetInfo" :dataAutoload="false" data-dict-type="1"
        data-operate-width="120px" data-fixed="right">
      <k-grid-column data-header="id" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
      <k-grid-column data-header="产品代码" data-name="prodCode"></k-grid-column>
		  <k-grid-column data-header="资产代码" data-name="assetCode"></k-grid-column>
		  <k-grid-column data-header="资产三类" data-name="assetThrCode"></k-grid-column>
		  <k-grid-column data-header="资产名称" data-name="assetName"></k-grid-column>
		  <k-grid-column data-header="折算人民币金额" data-name="netValue" data-digits="2"></k-grid-column>
		  <k-grid-column data-header="穿透后分类" data-name="g06Type" data-dict="g06_scd_type_fb"></k-grid-column>
		  <k-grid-column data-header="如填列1.5.16 非标，对股质类业务区分“1.5.16.a 场内”和“1.5.16.b 场外" data-width="150" data-name="nonPlace" data-dict="stock_bus_typ"></k-grid-column>
		  <k-grid-column data-header="融资主体" data-name="finEnt"></k-grid-column>
		  <k-grid-column data-header="融资主体统一社会信用代码" data-name="finEntCd"></k-grid-column>
		  <k-grid-column data-header="融资主体外部信用评级" data-name="finEntRt" data-dict="mainRating"></k-grid-column>
		  <k-grid-column data-header="融资主体外部信用评级日期" data-name="finEntDt"></k-grid-column>
		  <k-grid-column data-header="是否抵质押" data-name="ifPle" data-dict="1yes2no"></k-grid-column>
		  <k-grid-column data-header="履约担保比" data-name="perGuaRat"></k-grid-column>
      <k-grid-column data-header="是否足额抵质押" data-name="ifFullPle" data-dict="1yes2no"></k-grid-column>
      <k-grid-column data-header="是否保证类" data-name="ifWar" data-dict="1yes2no"></k-grid-column>
      <k-grid-column data-header="是否足额保证" data-name="ifFullWar" data-dict="1yes2no"></k-grid-column>
      <k-grid-column data-header="第三方保证人" data-name="warNm"></k-grid-column>
      <k-grid-column data-header="第三方保证人统一社会信用代码" data-width="100" data-name="warCd"></k-grid-column>
      <k-grid-column data-header="第三方保证人外部评级" data-name="warRt" data-dict="mainRating"></k-grid-column>
      <k-grid-column data-header="第三方保证人外部评级日期" data-name="warDt"></k-grid-column>
      <k-grid-column data-header="是否信用" data-name="ifCre" data-dict="1yes2no"></k-grid-column>
      <k-grid-column data-header="非标资产类别" data-name="nonAssetType" data-dict="fb_asset_typ"></k-grid-column>
      <k-grid-column data-header="数据日期" data-type="date" data-name="reportDate"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改G06BII非标资产明细中间表" data-functype="POPUP" data-size="mini"
            data-target="editPopup">
			  修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="DwsG06BIIFbAssetInfo.deleteDwsG06BIIFbAssetInfo" data-size="mini"
               data-type="danger" data-target="tableGrid" :data-confirm="true" data-descript="删除G06BII非标资产明细中间表">
			  删除
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!-- 修改G06BII非标资产明细中间表弹出框 -->
	<k-popup ref="editPopup" data-title="修改">
	  <k-form ref="editForm" :data-col="2">

      <k-form-item label="id" v-show="false">
        <k-field-text v-model="formData.id"/>
      </k-form-item>
      <k-form-item label="产品代码" :class="[handleItemDiff('prodCode')]">
          <k-field-text v-model="formData.prodCode" data-disabled="true"/>
      </k-form-item>
      <k-form-item label="资产代码" :class="[handleItemDiff('assetCode')]">
          <k-field-text v-model="formData.assetCode" data-disabled="true"/>
      </k-form-item>
      <k-form-item label="资产三类" :class="[handleItemDiff('assetThrCode')]">
          <k-field-text v-model="formData.assetThrCode"/>
      </k-form-item>
      <k-form-item label="资产名称" :class="[handleItemDiff('assetName')]">
          <k-field-text v-model="formData.assetName"/>
      </k-form-item>
      <k-form-item label="折算人民币金额" :class="[handleItemDiff('netValue')]">
          <k-field-text v-model="formData.netValue"/>
      </k-form-item>
      <k-form-item label="穿透后分类" :class="[handleItemDiff('g06Type')]">
          <k-field-select v-model="formData.g06Type" data-dict-type="1" data-dict="g06_scd_type_fb"/>
      </k-form-item>
      <k-form-item label="如填列1.5.16 非标，对股质类业务区分“1.5.16.a 场内”和“1.5.16.b 场外" :class="[handleItemDiff('nonPlace')]">
          <k-field-select v-model="formData.nonPlace" data-dict-type="1" data-dict="stock_bus_typ"/>
      </k-form-item>
      <k-form-item label="融资主体" :class="[handleItemDiff('finEnt')]">
          <k-field-text v-model="formData.finEnt"/>
      </k-form-item>
      <k-form-item label="融资主体统一社会信用代码" :class="[handleItemDiff('finEntCd')]">
          <k-field-text v-model="formData.finEntCd"/>
      </k-form-item>
      <k-form-item label="融资主体外部信用评级" :class="[handleItemDiff('finEntRt')]">
          <k-field-select v-model="formData.finEntRt" data-dict-type="1" data-dict="mainRating"/>
      </k-form-item>
      <k-form-item label="融资主体外部信用评级日期" :class="[handleItemDiff('finEntDt')]">
          <k-field-date v-model="formData.finEntDt" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
      </k-form-item>
      <k-form-item label="是否抵质押" :class="[handleItemDiff('ifPle')]">
          <k-field-select v-model="formData.ifPle" data-dict-type="1" data-dict="1yes2no"/>
      </k-form-item>
      <k-form-item label="履约担保比" :class="[handleItemDiff('perGuaRat')]">
          <k-field-text v-model="formData.perGuaRat" />
      </k-form-item>
      <k-form-item label="是否足额抵质押" :class="[handleItemDiff('ifFullPle')]">
          <k-field-select v-model="formData.ifFullPle" data-dict-type="1" data-dict="1yes2no"/>
      </k-form-item>
      <k-form-item label="是否保证类" :class="[handleItemDiff('ifWar')]">
          <k-field-select v-model="formData.ifWar" data-dict-type="1" data-dict="1yes2no"/>
      </k-form-item>
      <k-form-item label="是否足额保证" :class="[handleItemDiff('ifFullWar')]">
          <k-field-select v-model="formData.ifFullWar" data-dict-type="1" data-dict="1yes2no"/>
      </k-form-item>
      <k-form-item label="第三方保证人" :class="[handleItemDiff('warNm')]">
          <k-field-text v-model="formData.warNm"/>
      </k-form-item>
      <k-form-item label="第三方保证人统一社会信用代码" :class="[handleItemDiff('warCd')]">
          <k-field-text v-model="formData.warCd"/>
      </k-form-item>
      <k-form-item label="第三方保证人外部评级" :class="[handleItemDiff('warRt')]">
          <k-field-select v-model="formData.warRt" data-dict-type="1" data-dict="mainRating"/>
      </k-form-item>
      <k-form-item label="第三方保证人外部评级日期" :class="[handleItemDiff('warDt')]">
          <k-field-date v-model="formData.warDt" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
      </k-form-item>
      <k-form-item label="是否信用" :class="[handleItemDiff('ifCre')]">
          <k-field-select v-model="formData.ifCre" data-dict-type="1" data-dict="1yes2no"/>
      </k-form-item>
      <k-form-item label="非标资产类别" :class="[handleItemDiff('nonAssetType')]">
          <k-field-select v-model="formData.nonAssetType" data-dict-type="1" data-dict="fb_asset_typ"/>
      </k-form-item>

	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="DwsG06BIIFbAssetInfo.updateDwsG06BIIFbAssetInfo" data-from="editForm"
	        :data-model="formData" data-target="tableGrid" :handle-before="handleBefore">
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
  import AssetMixin from "@/pages/pms/subject/mixins/assetMixin.js"

  export default {
    name: "DwsG06BIIFbAssetInfo",
    mixins: [AssetMixin],
    methods: {
      handleBefore() {
        if (this.formNoChangeCb()) {
          this.$refs.editPopup.close();
          return false
        }
        return true
      },
    }
  };
</script>
