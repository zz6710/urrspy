<template>
  <div>
    	<k-form class="my-form " ref="addT8ProdInvestForm" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">
			<k-form-item label="产品代码" v-show="false">
	        	<k-field-text v-model="ProdInvest.prodCode"/>
	     	</k-form-item>
        <k-form-item label="产品主表id" v-show="false">
          <k-field-text v-model="ProdInvest.t8ProdInfoId"/>
        </k-form-item>
        <k-form-item label="建仓期单位">
          <k-field-select v-model="ProdInvest.accumCompany" data-dict="t8_prod_open_type"  :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="建仓期">
          <k-field-text v-model="ProdInvest.accumTerm" :data-max-length="3"
                           data-digits="0"  data-integer-length="3"  :data-disabled="true"
                           data-validate-type="number" data-type="number"/>
        </k-form-item>

        <k-form-item label="投资渠道">
          <k-field-checkbox v-model="ProdInvest.investmentType" data-dict="t8_investment_type" @data-on-change="showProdInvest" :data-disabled="true"/>
        </k-form-item>
        <k-form-item label="是否需新增证券公司" v-if="investment.bond">
          <k-field-select v-model="ProdInvest.isAddBond" data-dict="t8_prod_isok" :data-allowblank="!investment.bond":data-disabled="true"/>
        </k-form-item>
        <k-form-item label="是否需新增委外机构"  v-if="investment.outsource">
          <k-field-select v-model="ProdInvest.isAddOutsource" data-dict="t8_prod_isok" :data-allowblank="!investment.outsource":data-disabled="true"/>
        </k-form-item>
        <k-form-item label="是否需新增期货公司"  v-if="investment.future">
          <k-field-select v-model="ProdInvest.idAddFuture" data-dict="t8_prod_isok" :data-allowblank="!investment.future":data-disabled="true"/>
        </k-form-item>
        <k-form-item label="委外专户估值频率">
          <k-field-select v-model="ProdInvest.outsourceFreq" data-dict="t8_valuation_freq" data-default-value="1":data-disabled="true"/>
        </k-form-item>
        <k-form-item label="非标产品估值频率">
          <k-field-select v-model="ProdInvest.nonstandardFreq" data-dict="t8_valuation_freq" data-default-value="1":data-disabled="true"/>
        </k-form-item>
        <k-form-item label="委外投资的业绩报酬计提时点匹配">
          <k-field-select v-model="ProdInvest.isMatchPointtime" data-dict="t8_prod_isok" :data-disabled="true"/>
        </k-form-item>

			<k-form-item label="投资目标说明" :data-col="2">
	        	<k-field-text v-model="ProdInvest.investTargetDesc" :data-max-length="8000" inputType="textarea" :data-disabled="true" :rows="5"/>
	     	</k-form-item>
			<k-form-item label="投资范围说明" :data-col="2">
	        	<k-field-text v-model="ProdInvest.investRangeDesc" :data-max-length="8000" inputType="textarea" :data-disabled="true" :rows="5"/>
	     	</k-form-item>
			<k-form-item label="投资比例说明" :data-col="2">
	        	<k-field-text v-model="ProdInvest.investScaleDesc" :data-max-length="8000" inputType="textarea" :data-disabled="true" :rows="5"/>
	     	</k-form-item>
			<k-form-item label="投资策略说明" :data-col="2">
	        	<k-field-text v-model="ProdInvest.investPolicyDesc" :data-max-length="8000" inputType="textarea" :data-disabled="true" :rows="5"/>
	     	</k-form-item>
			<k-form-item label="投资限制说明" :data-col="2">
	        	<k-field-text v-model="ProdInvest.investLimitDesc" :data-max-length="8000" inputType="textarea" :data-disabled="true" :rows="5"/>
	     	</k-form-item>
			<k-form-item label="挂钩标的说明" :data-col="2">
	        	<k-field-text v-model="ProdInvest.hookTargetDesc" :data-max-length="8000" inputType="textarea" :data-disabled="true" :rows="5"/>
	     	</k-form-item>
			<k-form-item label="产品期权结构说明" :data-col="2">
	        	<k-field-text v-model="ProdInvest.optionStructDesc" :data-max-length="8000" inputType="textarea" :data-disabled="true" :rows="5"/>
	     	</k-form-item>
        <k-form-item label="资金用途" :data-col="2">
          <k-field-text v-model="ProdInvest.purposeUse" :data-max-length="8000" inputType="textarea" :data-disabled="true" :rows="5"/>
        </k-form-item>
        <k-form-item label="交易安排" :data-col="2">
          <k-field-text v-model="ProdInvest.transactionArrangement" :data-max-length="8000" inputType="textarea" :data-disabled="true" :rows="5"/>
        </k-form-item>
        <k-form-item label="产品风险" :data-col="2">
          <k-field-text v-model="ProdInvest.productRisk" :data-max-length="8000" inputType="textarea" :data-disabled="true" :rows="5"/>
        </k-form-item>
        <k-form-item label="相关当事人介绍" :data-col="2">
          <k-field-text v-model="ProdInvest.partyIntroduction" :data-max-length="8000" inputType="textarea" :data-disabled="true" :rows="5"/>
        </k-form-item>

        <k-form-item label="产品其他风险" :data-col="2">
          <k-field-select v-model="ProdInvest.prodRisk" data-action="T8ProdRiskConfig.findProdRiskConfig"
                          data-multiple="true" inputType="textarea" :rows="1"
                          data-display-field="prodRisk" data-value-field="prodRisk" :data-disabled="true" />
        </k-form-item>
    	</k-form>

    <!--    //20210410 axin 风险-->
    <k-form  ref="addForm2" v-for="(item,index) in riskItems" :key="index"
             :data-col="6" data-input-width="300px" data-label-width="170px" data-total-width="1118px">

      <k-form-item label="序号" v-show="false">
        <k-field-text v-model="item.numberId" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="风险名称">
        <k-field-text v-model="item.prodRisk" :data-allowblank="false" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="风险描述"  :data-col="2">
        <k-field-text v-model="item.prodRiskDesc"  :data-max-length="2000" inputType="textarea" :rows="1" :data-disabled="true" />
      </k-form-item>
    </k-form>

	<!--    修改产品投资信息表弹出框   -->
  </div>
</template>

<script>
  export default {
    computed: {},
    model: {
      prop: 'T8ProdCalendar',
      event: 'input'
    },
    props:{
      assemblyMenuType :'',
      ProdInvest: {

      },
      prodCode: {
        type: String,
        default: ''
      },
      t8ProdInfoId: {
        type: String,
        default: ''
      },
    },
    data() {
      return {
        //ProdInvest: {},
        selectRowData: {},
        investment:{
          bond:false,
          outsource:false,
          future:false,
        },
        riskItems:[],
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.ProdInvest = Object.assign({}, row)
      },

      validateData() {
        return this.$refs.addT8ProdInvestForm.validate();
      },
      showProdInvest(value){
        if (value.indexOf("1") != -1){
          this.investment.bond = true;
        } else{
          this.investment.bond = false;
        }

        if (value.indexOf("2") != -1){
          this.investment.outsource = true;
        } else{
          this.investment.outsource = false;
        }

        if (value.indexOf("3") != -1){
          this.investment.future = true;
        } else{
          this.investment.future = false;
        }
      },
    },
    created() {
      if (this.ProdInvest.investmentType != null){
        this.showProdInvest(this.ProdInvest.investmentType);
      }
    },

    watch: {
      'ProdInvest.investmentType': function (val) {
        this.showProdInvest(val);
      },
    },
  };
</script>
