<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="FundNavInfoModel" data-target="fundNavInfoModelGrid" v-model="searchParam">
        <k-form-item label="市场">
          <k-field-select v-model="searchParam.trxMkt" data-dict="marketFund"></k-field-select>
        </k-form-item>
        <k-form-item label="基金代码">
          <k-field-select v-model="searchParam.scrCd" data-action="FundNavInfoModel.findFundNavInfoModels"  :dataRemote="true"
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
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addFundNavInfoModelPopup" slot="button" v-if="global.isShowAuthorityButton('FundNavInfoModel.updateFundNavInfoModel')">
          <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        </div>
      </div>
      <k-grid ref="fundNavInfoModelGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="250px" data-action="FundNavInfoModel.findFundNavInfoModelsByScrCd" >
		<k-grid-column data-header="证券编号" data-name="scrId" data-hidden="true"></k-grid-column>
        <k-grid-column data-header="市场" data-name="trxMkt" data-dict="marketFund"></k-grid-column>
        <k-grid-column data-header="基金代码" data-name="scrCd"></k-grid-column>
        <k-grid-column data-header="基金名称" data-name="scrNm"></k-grid-column>
        <k-grid-column data-header="基金类型" data-name="fundType" data-dict="tr_fund_frs_type"></k-grid-column>
        <k-grid-column data-header="估值日期" data-name="stopDt" data-type="date"></k-grid-column>
        <k-grid-column data-header="公告日期" data-name="ntcDt" data-type="date"></k-grid-column>
        <k-grid-column data-header="单位净值（元）" data-name="untNav"></k-grid-column>
        <k-grid-column data-header="收盘价（元）" data-name="clsPrc"></k-grid-column>
        <k-grid-column data-header="万份收益（元）" data-name="tenThsdShrErn"></k-grid-column>
        <k-grid-column data-header="七日年化收益率（%）" data-name="rct7dAnlYld"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改基金净值信息" data-functype="POPUP" data-size="mini" v-if="global.isShowAuthorityButton('FundNavInfoModel.updateFundNavInfoModel')"
            data-target="editFundNavInfoModelPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="FundNavInfoModel.deleteFundNavInfoModel" data-size="mini" v-if="global.isShowAuthorityButton('FundNavInfoModel.deleteFundNavInfoModel')"
               data-type="danger" data-target="fundNavInfoModelGrid" :data-confirm="true" data-descript="删除基金净值信息">
          	删除
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加基金净值信息弹出框   -->
	<k-popup ref="addFundNavInfoModelPopup" data-title="新增" >
    	<k-form ref="addFundNavInfoModelForm" :data-col="2">
        <k-form-item label="id"  v-show="false">
          <k-field-text v-model="formData.scrId" />
        </k-form-item>
        <k-form-item label="市场">
          <k-field-select v-model="formData.trxMkt" data-dict="marketFund" :data-allowblank="false"  @data-on-change="fundIdAndNm"/>
        </k-form-item>
        <k-form-item label="基金代码">
          <k-field-select v-model="formData.scrCd" :data-data="fundInfoDict"
                        data-display-field="scrCd,scrNm" data-value-field="scrCd" :data-allowblank="false" @data-on-change="setTypeAndNm(true)"/>
        </k-form-item>
        <k-form-item label="基金名称">
          <k-field-select v-model="formData.scrNm" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="基金类型">
          <k-field-select v-model="formData.fundType" :data-disabled="true" data-dict="tr_fund_frs_type"/>
        </k-form-item>
        <k-form-item label="估值日期">
          <k-field-date v-model="formData.stopDt" :data-allowblank="false"/>
        </k-form-item>

        <k-form-item label="公告日期" v-if="this.formData.trxMkt == 6">
          <k-field-date v-model="formData.ntcDt" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="单位净值"  v-if="this.formData.trxMkt == 6 && this.formData.fundType != 4">
          <k-field-text v-model="formData.untNav" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="收盘价"  v-if="this.formData.trxMkt == 1 || this.formData.trxMkt == 2">
          <k-field-text v-model="formData.clsPrc" :data-allowblank="false"/>
        </k-form-item>


			<k-form-item label="万份收益（元）" v-if="this.formData.fundType == 4 && this.formData.trxMkt == 6">
	        	<k-field-text v-model="formData.tenThsdShrErn" :data-allowblank="false"/>
	     	</k-form-item>
			<k-form-item label="七日年化收益率（%）" v-if="this.formData.fundType == 4 && this.formData.trxMkt == 6">
	        	<k-field-text v-model="formData.rct7dAnlYld" :data-allowblank="false"/>
	     	</k-form-item>


	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="FundNavInfoModel.addFundNavInfoModel" data-from="addFundNavInfoModelForm"
		               :data-model="formData" data-target="fundNavInfoModelGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改基金净值信息弹出框   -->
	<k-popup ref="editFundNavInfoModelPopup" data-title="修改" @data-opened="editInfo">
	  <k-form ref="editFundNavInfoModelForm" :data-col="2">
      <k-form-item label="id" v-show="false">
        <k-field-text v-model="formData.scrId"/>
      </k-form-item>
      <k-form-item label="市场">
        <k-field-select v-model="formData.trxMkt" :data-allowblank="false" data-dict="marketFund" :data-disabled="true"  @data-on-change="fundIdAndNm"/>
      </k-form-item>
      <k-form-item label="基金代码">
        <k-field-select v-model="formData.scrCd" :data-data="fundInfoDict" :data-disabled="true"
                      data-display-field="scrCd,scrNm" :data-allowblank="false" data-value-field="scrCd" @data-on-change="setTypeAndNm(false)"/>
      </k-form-item>
      <k-form-item label="基金名称">
        <k-field-select v-model="formData.scrNm" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="基金类型">
        <k-field-select v-model="formData.fundType" :data-allowblank="false" :data-disabled="true" data-dict="tr_fund_frs_type"/>
      </k-form-item>
      <k-form-item label="估值日期">
        <k-field-date v-model="formData.stopDt" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="公告日期" v-if="this.formData.trxMkt == 6">
        <k-field-date v-model="formData.ntcDt" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="单位净值"  v-if="this.formData.trxMkt == 6">
        <k-field-text v-model="formData.untNav" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="收盘价"  v-if="this.formData.trxMkt == 1 || this.formData.trxMkt == 2">
        <k-field-text v-model="formData.clsPrc" :data-allowblank="false"/>
      </k-form-item>


      <k-form-item label="万份收益（元）" v-if="this.formData.fundType == 4">
        <k-field-text v-model="formData.tenThsdShrErn" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="七日年化收益率（%）" v-if="this.formData.fundType == 4">
        <k-field-text v-model="formData.rct7dAnlYld" :data-allowblank="false"/>
      </k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="FundNavInfoModel.updateFundNavInfoModel" data-from="editFundNavInfoModelForm"
	        :data-model="formData" data-target="fundNavInfoModelGrid">
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
    name:"FundNavInfoModel",
    data() {
      return {
        formData: {
          trxMkt:'',
          scrNm:'',
          fundType:'',
          scrCd:'',
          scrId:''
        },
        selectRowData: {},
        searchParam:{},
        BreathDay:[],
        fundInfoDict:{}
      };
    },
    created() {
      // this.fundIdAndNm();
    },
    methods: {
      editInfo(){
        this.fundIdAndNm()
      },
      setTypeAndNm(value){
        this.httpUtil.comnQuery({
          action: "FundInfoModel.findFondInfoModelsCdAndNmByScrCd",
          params: {scrCd:this.formData.scrCd,}
        }).then(data => {
          if (value) {
            this.$set(this.formData, 'scrId', data.rows[0].scrId);
          }
          this.$set(this.formData, 'scrNm', data.rows[0].scrNm);
          this.$set(this.formData, 'fundType', data.rows[0].wdFrsCtg);
        }).catch({})
      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
      fundIdAndNm(){
        this.fundInfoDict = {};
        this.httpUtil.comnQuery({
          action: "FundInfoModel.findFondInfoModelsCdAndNmByTrxMkt",
          params: {trxMkt:this.formData.trxMkt,}
        }).then(data => {
          this.fundInfoDict = data.rows;
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
