<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="ChinaDebtValuation" data-target="chinaDebtValuationGrid"  v-model="searchParam">
        <k-form-item label="债券代码">
          <k-field-select v-model="searchParam.scrCd" data-action="ChinaDebtValuation.findBondInfoModelsCdAndNm"  :dataRemote="true"
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
          <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addChinaDebtValuationPopup" slot="button"  v-if="global.isShowAuthorityButton('ChinaDebtValuation.addChinaDebtValuation')">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        </div>
      </div>
      <k-grid ref="chinaDebtValuationGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="250px" data-action="ChinaDebtValuation.findChinaDebtValuations" >
        <k-grid-column data-header="证券编号" data-hidden="true" data-name="scrId"></k-grid-column>
        <k-grid-column data-header="债券市场" data-name="trxMkt" data-dict="market"></k-grid-column>
        <k-grid-column data-header="债券代码" data-name="scrCd"></k-grid-column>
        <k-grid-column data-header="债券简称" data-name="scrShtNm"></k-grid-column>
        <k-grid-column data-header="估值日期" data-name="trxDt" data-type="date"></k-grid-column>
        <k-grid-column data-header="估值净价(元)" data-name="evalNetPrc"></k-grid-column>
        <k-grid-column data-header="估值全价(元)" data-name="evalFullPrc"></k-grid-column>
        <k-grid-column data-header="应计利息(元)" data-name="acrIntr"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改中债登估值信息" data-functype="POPUP" data-size="mini" v-if="global.isShowAuthorityButton('ChinaDebtValuation.updateChinaDebtValuation')"
            data-target="editChinaDebtValuationPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ChinaDebtValuation.deleteChinaDebtValuation" data-size="mini" v-if="global.isShowAuthorityButton('ChinaDebtValuation.deleteChinaDebtValuation')"
               data-type="danger" data-target="chinaDebtValuationGrid" :data-confirm="true" data-descript="删除中债登估值信息">
          	删除
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加ChinaDebtValuation弹出框   -->
	<k-popup ref="addChinaDebtValuationPopup" data-title="新增" @data-opened="addInfo">
    	<k-form ref="addChinaDebtValuationForm" :data-col="2" >

        <k-form-item label="债券市场">
          <k-field-select v-model="formData.trxMkt" data-dict="market" :data-allowblank="false" @data-on-change="bondIdAndNm"/>
        </k-form-item>
        <k-form-item label="债券代码">
        <k-field-select v-model="formData.scrCd" :data-data="bondInfodict"
                      data-display-field="scrCd,scrShtNm" data-value-field="scrCd"  :data-allowblank="false" @data-on-change="changeProdCd"/>
      </k-form-item>
        <k-form-item label="债券简称">
          <k-field-text v-model="formData.scrShtNm" :data-allowblank="false" :data-disabled="true"/>
        </k-form-item>
			<k-form-item label="估值日期">
	        	<k-field-date v-model="formData.trxDt" :data-allowblank="false"/>
	     	</k-form-item>
			<k-form-item label="估值全价(元)">
	        	<k-field-text v-model="formData.evalFullPrc" :data-allowblank="false"/>
	     	</k-form-item>
			<k-form-item label="应计利息(元)">
	        	<k-field-text v-model="formData.acrIntr" :data-allowblank="false"/>
	     	</k-form-item>
			<k-form-item label="估值净价(元)">
	        	<k-field-text v-model="formData.evalNetPrc" :data-allowblank="false"/>
	     	</k-form-item>


	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ChinaDebtValuation.addChinaDebtValuation" data-from="addChinaDebtValuationForm"
		               :data-model="formData" data-target="chinaDebtValuationGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改ChinaDebtValuation弹出框   -->
	<k-popup ref="editChinaDebtValuationPopup" data-title="修改" @data-opened="addInfo">
	  <k-form ref="editChinaDebtValuationForm" :data-col="2">
      <k-form-item label="债券市场">
        <k-field-select v-model="formData.trxMkt" data-dict="market" :data-allowblank="false" @data-on-change="bondIdAndNm"/>
      </k-form-item>
      <k-form-item label="债券代码">
        <k-field-select v-model="formData.scrCd" :data-data="bondInfodict"
                        data-display-field="scrCd,scrShtNm" data-value-field="scrCd"  :data-allowblank="false" @data-on-change="changeProdCd"/>
      </k-form-item>
      <k-form-item label="债券简称">
        <k-field-text v-model="formData.scrShtNm" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="估值日期">
        <k-field-date v-model="formData.trxDt" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="估值全价(元)">
        <k-field-text v-model="formData.evalFullPrc" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="应计利息(元)">
        <k-field-text v-model="formData.acrIntr" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="估值净价(元)">
        <k-field-text v-model="formData.evalNetPrc" :data-allowblank="false"/>
      </k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ChinaDebtValuation.updateChinaDebtValuation" data-from="editChinaDebtValuationForm"
	        :data-model="formData" data-target="chinaDebtValuationGrid">
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
    name:"ChinaDebtValuation",
    data() {
      return {
        formData: {
          scrCd:'',
          scrShtNm:''
        },
        selectRowData: {},
        searchParam:{},
        bondInfodict:{},
        BreathDay:[],
      };
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
        this.bondIdAndNm()
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
