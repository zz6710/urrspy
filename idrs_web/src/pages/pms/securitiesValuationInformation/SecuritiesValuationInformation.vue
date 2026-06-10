<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="SecuritiesValuationInformation" data-target="securitiesValuationInformationGrid" v-model="searchParam">
        <k-form-item label="债券代码">
          <k-field-select v-model="searchParam.scrCd" data-action="SecuritiesValuationInformation.findSecuritiesValuationInformationsCdAndNm"   :dataRemote="true"
                          data-display-field="scrCd,scrShtNm" data-value-field="scrCd" />
        </k-form-item>
        <k-form-item label="市场">
          <k-field-select v-model="searchParam.trxMkt" data-dict="market"></k-field-select>
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
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addSecuritiesValuationInformationPopup" slot="button"  v-if="global.isShowAuthorityButton('SecuritiesValuationInformation.addSecuritiesValuationInformation')" >
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        </div>
      </div>

      <k-grid ref="securitiesValuationInformationGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="250px" data-action="SecuritiesValuationInformation.findSecuritiesValuationInformations" >
		<k-grid-column data-header="证券编号" data-hidden="true" data-name="scrId"></k-grid-column>
        <k-grid-column data-header="债券市场" data-name="trxMkt" data-dict="market"></k-grid-column>
        <k-grid-column data-header="债券代码" data-name="scrCd"></k-grid-column>
		<k-grid-column data-header="债券简称" data-name="scrShtNm" ></k-grid-column>
		<k-grid-column data-header="估值日期" data-name="trxDt" data-type="date"></k-grid-column>
		<k-grid-column data-header="估值全价（元）" data-name="calcPrc"></k-grid-column>
		<k-grid-column data-header="估值净价（元）" data-name="netPrc"></k-grid-column>
		<k-grid-column data-header="应计利息（元）" data-name="acrIntr"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改中证估值" data-functype="POPUP" data-size="mini"  v-if="global.isShowAuthorityButton('SecuritiesValuationInformation.updateSecuritiesValuationInformation')"
            data-target="editSecuritiesValuationInformationPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="SecuritiesValuationInformation.deleteSecuritiesValuationInformation" data-size="mini"  v-if="global.isShowAuthorityButton('SecuritiesValuationInformation.deleteSecuritiesValuationInformation')"
               data-type="danger" data-target="securitiesValuationInformationGrid" :data-confirm="true" data-descript="删除中证估值">
          	删除
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加SecuritiesValuationInformation弹出框   -->
	<k-popup ref="addSecuritiesValuationInformationPopup" data-title="新增" @data-opened="addInfo">
    	<k-form ref="addSecuritiesValuationInformationForm" :data-col="2">
        <k-form-item label="债券市场">
          <k-field-select v-model="formData.trxMkt" :data-allowblank="false" data-dict="market" @data-on-change="bondIdAndNm"/>
        </k-form-item>
        <k-form-item label="债券代码">
	        	<k-field-select v-model="formData.scrCd" :data-data="bondInfodict"
                          data-display-field="scrCd,scrShtNm" data-value-field="scrCd" :data-allowblank="false"   @data-on-change="changeProdCd"/>
	     	</k-form-item>
        <k-form-item label="债券简称">
          <k-field-text v-model="formData.scrShtNm" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
			<k-form-item label="估值日期">
	        	<k-field-date v-model="formData.trxDt" :data-allowblank="false"/>
	     	</k-form-item>
			<k-form-item label="估值全价（元）">
	        	<k-field-text v-model="formData.calcPrc" :data-allowblank="false"/>
	     	</k-form-item>
			<k-form-item label="估值净价（元）" >
	        	<k-field-text v-model="formData.netPrc" :data-allowblank="false"/>
	     	</k-form-item>
			<k-form-item label="应计利息（元）">
	        	<k-field-text v-model="formData.acrIntr" :data-allowblank="false"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="SecuritiesValuationInformation.addSecuritiesValuationInformation" data-from="addSecuritiesValuationInformationForm"
		               :data-model="formData" data-target="securitiesValuationInformationGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改SecuritiesValuationInformation弹出框   -->
	<k-popup ref="editSecuritiesValuationInformationPopup" data-title="修改" @data-opened="addInfo">
	  <k-form ref="editSecuritiesValuationInformationForm" :data-col="2">
      <k-form-item label="债券市场">
        <k-field-select v-model="formData.trxMkt" :data-allowblank="false" data-dict="market" @data-on-change="bondIdAndNm"/>
      </k-form-item>
      <k-form-item label="债券代码">
        <k-field-select v-model="formData.scrCd" :data-data="bondInfodict"
                        data-display-field="scrCd,scrShtNm" data-value-field="scrCd" :data-allowblank="false" @data-on-change="changeProdCd"/>
      </k-form-item>
      <k-form-item label="债券简称">
        <k-field-text v-model="formData.scrShtNm" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="估值日期">
        <k-field-date v-model="formData.trxDt" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="估值全价（元）">
        <k-field-text v-model="formData.calcPrc" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="估值净价（元）" >
        <k-field-text v-model="formData.netPrc" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="应计利息（元）">
        <k-field-text v-model="formData.acrIntr" :data-allowblank="false"/>
      </k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="SecuritiesValuationInformation.updateSecuritiesValuationInformation" data-from="editSecuritiesValuationInformationForm"
	        :data-model="formData" data-target="securitiesValuationInformationGrid">
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
    name:"SecuritiesValuationInformation",
    data() {
      return {
        formData: {
          scrShtNm:'',
          trxMkt:''
        },
        selectRowData: {},
        searchParam:{},
      bondInfodict:{},
      BreathDay:[],
      };
    },
    created() {
      // this.bondIdAndNm();
    },
    methods: {
      changeProdCd(){
        this.$set(this.formData, 'scrShtNm', '');
        if (this.formData.scrCd!==undefined&&this.formData.scrCd !== ''&&this.formData.scrCd!==null) {
          this.httpUtil.comnQuery({
            action: "BondInfoModel.findBondInfoName",
            params: {scrCd: this.formData.scrCd}
          }).then(data => {
            this.formData.scrShtNm = data.rows[0].scrShtNm;
            console.log("--------------",data.rows)
          }).catch({});
        }
      },
      addInfo(){
        this.bondIdAndNm();

      },
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
      bondIdAndNm(){
        this.bondInfodict = {};
        this.httpUtil.comnQuery({
          action: "BondInfoModel.findBondInfoModelsCdAndNmByTrxMkt",
          params: {trxMkt:this.formData.trxMkt,}
        }).then(data => {
          this.bondInfodict = data.rows;
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
