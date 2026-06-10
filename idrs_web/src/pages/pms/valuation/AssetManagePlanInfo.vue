<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="AssetManagePlanInfo" data-target="assetManagePlanInfoGrid" v-model="searchParam">
        <k-form-item label="资产代码">
          <k-field-select v-model="searchParam.scrCd" data-action="AssetManagePlanInfo.findAssetManagePlanInfos"  :dataRemote="true"
                          data-display-field="scrCd,scrNm" data-value-field="scrCd" />
        </k-form-item>
        <k-form-item label="估值日期">
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd"
                        data-value-format="yyyyMMdd"></k-field-date>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addAssetManagePlanInfoPopup" slot="button"  v-if="global.isShowAuthorityButton('AssetManagePlanInfo.addAssetManagePlanInfo')">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        </div>
      </div>
      <k-grid ref="assetManagePlanInfoGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="250px" data-action="AssetManagePlanInfo.findAssetManagePlanInfosByScrCd" >
		<k-grid-column data-header="证券编号" data-name="scrId" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="资产代码" data-name="scrCd"></k-grid-column>
		<k-grid-column data-header="资产名称" data-name="scrNm"></k-grid-column>
		<k-grid-column data-header="估值日期" data-name="valDt" data-type="date"></k-grid-column>
		<k-grid-column data-header="净价（元）" data-name="untVal" ></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改净值SPV估值信息" data-functype="POPUP" data-size="mini"  v-if="global.isShowAuthorityButton('AssetManagePlanInfo.updateAssetManagePlanInfo')"
            data-target="editAssetManagePlanInfoPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="AssetManagePlanInfo.deleteAssetManagePlanInfo" data-size="mini"  v-if="global.isShowAuthorityButton('AssetManagePlanInfo.deleteAssetManagePlanInfo')"
               data-type="danger" data-target="assetManagePlanInfoGrid" :data-confirm="true" data-descript="删除净值SPV估值信息">
          	删除
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加净值SPV估值信息弹出框   -->
	<k-popup ref="addAssetManagePlanInfoPopup" data-title="新增" @data-opened="NetValSPVIdAndNm">
    	<k-form ref="addAssetManagePlanInfoForm" :data-col="2">
        <k-form-item label="id" v-show="false">
          <k-field-text v-model="formData.scrId"/>
        </k-form-item>
			<k-form-item label="资产代码">
	        	<k-field-select v-model="formData.scrCd" :data-data="NetValSPVInfodict"
                          data-display-field="scrCd,scrNm" data-value-field="scrCd" :data-allowblank="false" @data-on-change="NetValFindNm(true)"/>
	     	</k-form-item>
        <k-form-item label="资产名称">
          <k-field-text v-model="formData.scrNm" :data-disabled="true" :data-allowblank="false"/>
        </k-form-item>
			<k-form-item label="估值日期">
	        	<k-field-date v-model="formData.valDt" :data-allowblank="false"/>
	     	</k-form-item>
			<k-form-item label="净价（元）">
	        	<k-field-text v-model="formData.untVal" :data-allowblank="false" data-validate-type="money" data-type="money" data-integer-length="20" data-digits="4"/>
	     	</k-form-item>


	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AssetManagePlanInfo.addAssetManagePlanInfo" data-from="addAssetManagePlanInfoForm"
		               :data-model="formData" data-target="assetManagePlanInfoGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改净值SPV估值信息弹出框   -->
	<k-popup ref="editAssetManagePlanInfoPopup" data-title="修改" @data-opened="NetValSPVIdAndNm">
	  <k-form ref="editAssetManagePlanInfoForm" :data-col="2">
      <k-form-item label="id" v-show="false">
        <k-field-text v-model="formData.scrId"/>
      </k-form-item>
      <k-form-item label="资产代码">
        <k-field-select v-model="formData.scrCd" :data-data="NetValSPVInfodict" :data-disabled="true"
                      data-display-field="scrCd,scrNm" data-value-field="scrCd" :data-allowblank="false" @data-on-change="NetValFindNm(false)"/>
      </k-form-item>
      <k-form-item label="资产名称">
        <k-field-text v-model="formData.scrNm" :data-disabled="true" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="估值日期">
        <k-field-date v-model="formData.valDt" :data-allowblank="false" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="净价（元）">
        <k-field-text v-model="formData.untVal" :data-allowblank="false" data-validate-type="money" data-type="money" data-integer-length="20" data-digits="4"/>
      </k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AssetManagePlanInfo.updateAssetManagePlanInfo" data-from="editAssetManagePlanInfoForm"
	        :data-model="formData" data-target="assetManagePlanInfoGrid">
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
    name:"AssetManagePlanInfo",
    data() {
      return {
        formData: {
          scrNm:'',
          scrCd:'',
          scrId:''
        },
        selectRowData: {},
        NetValSPVInfodict:{},
        BreathDay:[],
        searchParam:{}
      };
    },
    created() {
     // this.NetValSPVIdAndNm()
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
      NetValSPVIdAndNm(){
        this.NetValSPVInfodict = {};
        this.httpUtil.comnQuery({
          action: "NetValSPVInfoModel.findNetValSPVInfoModels",
          params: null
        }).then(data => {
          this.NetValSPVInfodict = data.rows;
        }).catch({})
      },
      NetValFindNm(value){
        this.httpUtil.comnQuery({
          action: "NetValSPVInfoModel.findNetValSPVInfoModelsCdAndNm",
          params: {scrCd:this.formData.scrCd}
        }).then(data => {
          if (value) {
            this.formData.scrId = data.rows[0].scrId;
          }
          this.formData.scrNm = data.rows[0].scrNm;
        }).catch({})
      },
    },
    watch: {
      //查询起息日
      BreathDay() {
        console.log(this.BreathDay);
        this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
        this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
      },
    }
  };
</script>
