<template>
  <div class="py-page">
     <div>
         <k-form-search-customize data-model-name="AssetRgInfoErr" data-target="AssetRgInfoErrGrid" data-label-width="80px" v-model = "searchParam">
           <k-form-item label="导入日期">
             <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMM"
                           data-value-format="yyyyMMdd"/>
           </k-form-item>
           <k-form-item label="登记流水号">
             <k-field-text v-model="searchParam.registerSerno"/>
           </k-form-item>
         </k-form-search-customize>
       </div>
    <div class="py-page-container">
      <k-grid ref="AssetRgInfoErrGrid" @data-row-select="selectRow" data-operate-column="false" data-action="AssetRgInfoErr.findAssetRgInfos" >
        <k-grid-column data-header="登记流水号" data-name="registerSerno"></k-grid-column>
        <k-grid-column data-header="导入日期" data-name="impDate"></k-grid-column>
        <k-grid-column data-header="发行机构代码错误描述" data-name="bankCodeDesc"></k-grid-column>
        <k-grid-column data-header="产品登记编码错误描述" data-name="prodRegEncDesc"></k-grid-column>
        <k-grid-column data-header="持仓类别错误描述" data-name="holdingTypeDesc"></k-grid-column>
        <k-grid-column data-header="行内资产/负债编码错误描述" data-name="assetCodeDesc"></k-grid-column>
        <k-grid-column data-header="资产穿透情况错误描述" data-name="investedAsset"></k-grid-column>
        <k-grid-column data-header="中间层数错误描述" data-name="mezzanineNumberDesc"></k-grid-column>
        <k-grid-column data-header="中间层行内资产/负债编码错误描述" data-name="mezzanineAssetCodeDesc"></k-grid-column>
        <k-grid-column data-header="会计科目名称错误描述" data-name="accountCodeDesc"></k-grid-column>
        <k-grid-column data-header="金额错误描述" data-name="investedAmountDesc"></k-grid-column>
        <k-grid-column data-header="折算人民币金额错误描述" data-name="investedAmountCnyDesc" ></k-grid-column>
        <k-grid-column data-header="公允价值错误描述" data-name="fairValueDesc" ></k-grid-column>
        <k-grid-column data-header="折算人民币公允价错误描述" data-name="fairValueCnyDesc" ></k-grid-column>
        <k-grid-column data-header="单位估值(净价)错误描述" data-name="netValuationDesc" ></k-grid-column>
        <k-grid-column data-header="单位估值(全价)错误描述" data-name="flValuationDesc" ></k-grid-column>
        <k-grid-column data-header="数量错误描述" data-name="quantityDesc" ></k-grid-column>
        <k-grid-column data-header="币种错误描述" data-name="cnyDesc" ></k-grid-column>
        <k-grid-column data-header="持仓日期错误描述" data-name="holdingDateDesc" data-type="date"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改资产持仓错误信息" data-functype="POPUP" data-size="mini"
            data-target="editAssetRgInfoErrPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="AssetRgInfoErr.deleteAssetRgInfo" data-size="mini"
               data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true" data-descript="删除资产持仓错误信息">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加底层资产错误信息弹出框   -->
	<k-popup ref="addAssetRgInfoErrPopup" data-title="新增">
    	<k-form ref="addAssetRgInfoErrForm" :data-col="2">
        <k-form-item label="登记流水号">
          <k-field-text v-model="formData.registerSerno"/>
        </k-form-item>
        <k-form-item label="导入日期">
          <k-field-text v-model="formData.impDate"/>
        </k-form-item>
        <k-form-item label="发行机构代码错误描述">
          <k-field-text v-model="formData.bankCode" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="产品登记编码错误描述">
          <k-field-text v-model="formData.prodRegEncDesc" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="持仓类别错误描述">
          <k-field-select v-model="formData.holdingTypeDesc" :data-allowblank="false" data-dict="holding_type"/>
        </k-form-item>
        <k-form-item label="行内资产/负债编码错误描述">
          <k-field-text v-model="formData.assetCodeDesc" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="资产穿透情况错误描述">
          <k-field-select v-model="formData.investedAssetDesc" :data-allowblank="false" data-dict="invested_asset_type"/>
        </k-form-item>
        <k-form-item label="中间层数错误描述">
          <k-field-text v-model="formData.mezzanineNumberDesc" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="中间层行内资产/负债编码错误描述">
          <k-field-text v-model="formData.mezzanineAssetCodeDesc" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="会计科目名称错误描述">
          <k-field-text v-model="formData.accountCodeDesc" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="金额错误描述">
          <k-field-text v-model="formData.investedAmountDesc" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="折算人民币金额错误描述">
          <k-field-text v-model="formData.investedAmountCnyDesc" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="公允价值错误描述">
          <k-field-text v-model="formData.fairValueDesc" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="折算人民币公允价错误描述">
          <k-field-text v-model="formData.fairValueCnyDesc" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="单位估值(净价)错误描述">
          <k-field-text v-model="formData.netValuationDesc" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="单位估值(全价)错误描述">
          <k-field-text v-model="formData.flValuationDesc" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="数量错误描述">
          <k-field-text v-model="formData.quantityDesc" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="币种错误描述">
          <k-field-select v-model="formData.cnydesc" :data-allowblank="false" data-dict="cur_type"/>
        </k-form-item>
        <k-form-item label="持仓日期错误描述">
          <k-field-date v-model="formData.holdingDateDesc" :data-allowblank="false"/>
        </k-form-item>
	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AssetRgInfoErr.addAssetRgInfo" data-from="addAssetRgInfoErrForm"
		               :data-model="formData" data-target="AssetRgInfoErrGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改底层资产错误信息弹出框   -->
	<k-popup ref="editAssetRgInfoErrPopup" data-title="修改">
	  <k-form ref="editAssetRgInfoErrForm" :data-col="2">
      <k-form-item label="登记流水号">
        <k-field-text v-model="formData.registerSerno"/>
      </k-form-item>
      <k-form-item label="导入日期">
        <k-field-text v-model="formData.impDate"/>
      </k-form-item>
      <k-form-item label="发行机构代码错误描述">
        <k-field-text v-model="formData.bankCode" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="产品登记编码错误描述">
        <k-field-text v-model="formData.prodRegEncDesc" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="持仓类别错误描述">
        <k-field-select v-model="formData.holdingTypeDesc" :data-allowblank="false" data-dict="holding_type"/>
      </k-form-item>
      <k-form-item label="行内资产/负债编码错误描述">
        <k-field-text v-model="formData.assetCodeDesc" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="资产穿透情况错误描述">
        <k-field-select v-model="formData.investedAssetDesc" :data-allowblank="false" data-dict="invested_asset_type"/>
      </k-form-item>
      <k-form-item label="中间层数错误描述">
        <k-field-text v-model="formData.mezzanineNumberDesc" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="中间层行内资产/负债编码错误描述">
        <k-field-text v-model="formData.mezzanineAssetCodeDesc" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="会计科目名称错误描述">
        <k-field-text v-model="formData.accountCodeDesc" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="金额错误描述">
        <k-field-text v-model="formData.investedAmountDesc" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="折算人民币金额错误描述">
        <k-field-text v-model="formData.investedAmountCnyDesc" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="公允价值错误描述">
        <k-field-text v-model="formData.fairValueDesc" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="折算人民币公允价错误描述">
        <k-field-text v-model="formData.fairValueCnyDesc" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="单位估值(净价)错误描述">
        <k-field-text v-model="formData.netValuationDesc" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="单位估值(全价)错误描述">
        <k-field-text v-model="formData.flValuationDesc" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="数量错误描述">
        <k-field-text v-model="formData.quantityDesc" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="币种错误描述">
        <k-field-select v-model="formData.cnydesc" :data-allowblank="false" data-dict="cur_type"/>
      </k-form-item>
      <k-form-item label="持仓日期错误描述">
        <k-field-date v-model="formData.holdingDateDesc" :data-allowblank="false"/>
      </k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AssetRgInfoErr.updateAssetRgInfo" data-from="editAssetRgInfoErrForm"
	        :data-model="formData" data-target="AssetRgInfoErrGrid">
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
  export default {
    name: "AssetRgInfoErr",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{}, //查询条件
        BreathDay:[],
      };
    },created() {
    let registerSerno = this.$route.query.registerSerno;
    if(registerSerno != null || registerSerno != undefined || registerSerno != ''){
      this.searchParam.registerSerno = registerSerno;
      this.$nextTick(()=>{
        this.$refs.AssetRgInfoErrGrid.load(this.searchParam);
      });
    }
  },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      }
    },
    watch: {
      // 查询导入日期
      BreathDay() {
        console.log(this.BreathDay);
        this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
        this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
      },
    }
  };
</script>
