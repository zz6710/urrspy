<template>
  <div>
    <k-form class="my-form" ref="DeclarationInfo" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-item label="产品要素模板">
        <k-field-select v-model="T8DeclarationInfo.declaraModelId" data-action="T8ProdDeclaraModel.findT8ProdDeclaraModels"
                        data-display-field="id,modelName"  data-value-field="id"  :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="产品信息主表id" v-show="false">
        <k-field-text v-model="T8DeclarationInfo.t8ProdInfoId"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品代码" v-show="false">
        <k-field-text v-model="T8DeclarationInfo.prodCode" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>

      <k-form-item label="产品名称">
        <k-field-text v-model="T8DeclarationInfo.prodName" :data-disabled="true" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="行内标识码">
        <k-field-text v-model="T8DeclarationInfo.internalIdentCode" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="发行机构代码">
        <k-field-text v-model="T8DeclarationInfo.issuerCode" :data-default-value="'C10308'"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品审批人姓名">
        <k-field-select v-model="T8DeclarationInfo.approverName" data-action='T8ProdCustomerInfo.find'
                         :data-params="{'custType':'0'}" :dataAllowblank='false' :data-disabled="true"
                        data-display-field="custName"  data-value-field="custName"/>

      </k-form-item>
      <k-form-item label="产品审批人证件号">
        <k-field-text v-model="T8DeclarationInfo.approverIdcardNoTm" :data-allowblank="false"
                      :data-max-length="32" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品设计人姓名">
        <k-field-select v-model="T8DeclarationInfo.designerName"  data-action='T8ProdCustomerInfo.find' :data-disabled="true"
                        :data-params="{'custType':'1',}" data-display-field="custName" data-value-field="custName"
                        :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="产品设计人证件号">
        <k-field-text v-model="T8DeclarationInfo.designerIdcardNoTm"
                      :data-max-length="32" :data-allowblank="false"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="投资经理名称">
        <k-field-select v-model="T8DeclarationInfo.investManageName" data-action='T8ProdCustomerInfo.find' :data-disabled="true"
                        :data-params="{'custType':'2'}" data-display-field="custName" data-value-field="custName"
                         :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="投资经理证件号">
        <k-field-text v-model="T8DeclarationInfo.investManageIdcardNoTm" :data-allowblank="false"
                      :data-max-length="32" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="业务联系人姓名">
        <k-field-select v-model="T8DeclarationInfo.businessContactName" data-action='T8ProdCustomerInfo.find'
                        :data-params="{'custType':'3'}" data-display-field="custName" data-value-field="custName"
                        :data-allowblank="false"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="业务联系座机号">
        <k-field-text v-model="T8DeclarationInfo.businessContactLandline" :data-allowblank="false" :data-max-length="20"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="业务联系人手机号">
        <k-field-text v-model="T8DeclarationInfo.businessContactPhoneTm" :data-allowblank="false" :data-max-length="11"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="业务联络人邮箱">
        <k-field-text v-model="T8DeclarationInfo.businessContactEmail" :data-allowblank="false"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品收益类型">
        <k-field-select v-model="T8DeclarationInfo.revenueType" data-dict="prod_revenue_type" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品期限">
        <k-field-select v-model="T8DeclarationInfo.prodTerm" data-dict="t8_prod_term" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="是否金融同业专属">
        <k-field-select v-model="T8DeclarationInfo.isFinancialIndustry" data-dict="is_financial_industry" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="资金投向地区">
        <k-field-select v-model="T8DeclarationInfo.investRegion" data-dict="invest_region" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品投资国家或地区" v-show="T8DeclarationInfo.investRegion != '01'">
        <k-field-select v-model="T8DeclarationInfo.investRegionOutside"  data-dict="t8_countries_regions"
                        :data-allowblank="T8DeclarationInfo.investRegion == '01'" :data-multiple="true"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="理财业务服务模式" v-show="T8DeclarationInfo.investRegion != '02'">
        <k-field-select v-model="T8DeclarationInfo.financialServiceMode" data-dict="t8_invest_region"
                        :data-allowblank="T8DeclarationInfo.investRegion == '02'"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品运作模式">
        <k-field-select v-model="T8DeclarationInfo.productOperationMode" :data-allowblank="false" data-dict="t8_product_operation_mode" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品募集方式">
        <k-field-select v-model="T8DeclarationInfo.prodRaiseMethod" data-dict="t8_raise_type" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品资产配置方式">
        <k-field-select v-model="T8DeclarationInfo.prodAssetAllocation" data-dict="t8_asset_maping" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品管理模式">
        <k-field-select v-model="T8DeclarationInfo.prodManageMode" data-dict="t8_manage_mode" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="实际管理人名称" v-show="T8DeclarationInfo.prodManageMode != '01' && T8DeclarationInfo.prodManageMode != '04'">
        <k-field-text v-model="T8DeclarationInfo.actualManagerName" :data-allowblank="T8DeclarationInfo.prodManageMode == '01' || T8DeclarationInfo.prodManageMode == '04'"
                      :data-max-length="120" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品定价方式">
        <k-field-select v-model="T8DeclarationInfo.prodPriceWay" :data-allowblank="false" data-dict="prod_price_way" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品投资性质">
        <k-field-select v-model="T8DeclarationInfo.prodInvestNature" data-dict="prod_invest_nature" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="业绩比较基准">
        <k-field-text v-model="T8DeclarationInfo.performanceBenchmark" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="是否设置最短持有期限" v-show="T8DeclarationInfo.productOperationMode != '1'">
        <k-field-select v-model="T8DeclarationInfo.isMinHoldPeriod" :data-allowblank="T8DeclarationInfo.productOperationMode == '1'"
                        data-dict="is_min_hold_period" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="最短持有期限(天)" v-show="T8DeclarationInfo.isMinHoldPeriod == '01' && T8DeclarationInfo.productOperationMode != '1'">
        <k-field-text v-model="T8DeclarationInfo.minHoldPeriod" data-min-value="0" data-max-value="99999" data-validate-type="number" data-type="number"
                      :data-allowblank="T8DeclarationInfo.isMinHoldPeriod != '01' || T8DeclarationInfo.productOperationMode == '1'" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="最短持有期后是否自由赎回" v-show="T8DeclarationInfo.isMinHoldPeriod == '01' && T8DeclarationInfo.productOperationMode != '1'">
        <k-field-select v-model="T8DeclarationInfo.isFreeRedemption" data-dict="is_free_redemption" :data-disabled="true"
                        :data-allowblank="T8DeclarationInfo.isMinHoldPeriod != '01' || T8DeclarationInfo.productOperationMode == '1'"/>
      </k-form-item>
      <k-form-item label="是否现金管理类">
        <k-field-select v-model="T8DeclarationInfo.isCashManagement" data-dict="is_cash_management" :data-disabled="true"
                        :data-default-value="T8DeclarationInfo.productOperationMode == 3 ? '01' : '02'" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="产品销售区域" :data-col="2">
        <k-field-select v-model="T8DeclarationInfo.prodSalesArea" :data-allowblank="false" data-dict="prod_sale_area" data-multiple="true" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="起点销售金额">
        <k-field-text v-model="T8DeclarationInfo.startSalesAmount" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="募集币种">
        <k-field-select v-model="T8DeclarationInfo.raisedCurrency" data-dict="t8_prod_currtype_more" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="兑付本金币种">
        <k-field-select v-model="T8DeclarationInfo.cashGoldCoin" data-dict="t8_prod_currtype_more" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="兑付收益币种">
        <k-field-select v-model="T8DeclarationInfo.currencyCashIncome" data-dict="t8_prod_currtype_more" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="销售手续费率%">
        <k-field-text v-model="T8DeclarationInfo.salesServiceRate" :data-allowblank="false" data-digits="5"  data-integer-length="3"
                      data-min-value="0" data-validate-type="money" data-type="money" :data-disabled="true"
                      data-placeholder="1%，直联渠道时填写数字’0.01‘"/>
      </k-form-item>
      <k-form-item label="托管费率%">
        <k-field-text v-model="T8DeclarationInfo.escrowRate" :data-allowblank="false" data-digits="5"  data-integer-length="3"
                      data-min-value="0" data-validate-type="money" data-type="money"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="募集起始日期（从）">
        <k-field-date v-model="T8DeclarationInfo.raiseDateStart" :data-allowblank="false" data-type="date" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="募集结束日期（到）">
        <k-field-date v-model="T8DeclarationInfo.raiseDateEnd" :data-allowblank="false" data-type="date" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="计划募集金额">
        <k-field-text v-model="T8DeclarationInfo.planFundRaiseAmount" :data-allowblank="false" data-digits="2"  :data-disabled="true" data-integer-length="15"
                      data-min-value="0" data-validate-type="money" data-type="money" />
      </k-form-item>
      <k-form-item label="境内托管机构名称"  v-show="T8DeclarationInfo.investRegion != '02'">
        <k-field-select v-model="T8DeclarationInfo.domesticTrusteeName" data-dict="t8_trutee_bank" :data-disabled="true" :data-allowblank="T8DeclarationInfo.investRegion == '02'"/>
      </k-form-item>
      <k-form-item label="境内托管机构代码"  v-show="T8DeclarationInfo.investRegion != '02'">
        <k-field-text v-model="T8DeclarationInfo.domesticTrusteeCode"  :data-allowblank="T8DeclarationInfo.investRegion == '02'" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="境外托管机构国别" v-show="T8DeclarationInfo.investRegion != '01'">
        <k-field-select v-model="T8DeclarationInfo.countryOverseasTrusteeshipInstitution" data-dict="t8_nation_code" :data-disabled="true"
                        :data-allowblank="T8DeclarationInfo.investRegion == '01'"/>
      </k-form-item>
      <k-form-item label="境外托管机构名称" v-show="T8DeclarationInfo.investRegion != '01'">
        <k-field-text v-model="T8DeclarationInfo.overseasTrusteeshipInstitutionName"
                      :data-allowblank="T8DeclarationInfo.investRegion == '01'"/>
      </k-form-item>
      <k-form-item label="投资者风险偏好">
        <k-field-select v-model="T8DeclarationInfo.investorRiskPreference" data-dict="investor_risk_preference" :data-disabled="true"
                        data-multiple="true" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="产品风险等级">
        <k-field-select v-model="T8DeclarationInfo.prodRiskLevel" data-dict="prod_risk_level" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="发行机构提前终止权标识">
        <k-field-select v-model="T8DeclarationInfo.earlyTerminationFlag" :data-allowblank="false" :data-disabled="true" data-dict="early_termination_flag"/>
      </k-form-item>
      <k-form-item label="客户赎回权标识">
        <k-field-select v-model="T8DeclarationInfo.customerRedemptionFlag" :data-allowblank="false" :data-disabled="true" data-dict="customer_redemption_flag"/>
      </k-form-item>
      <k-form-item label="产品品牌">
        <k-field-select v-model="T8DeclarationInfo.prodBrand" data-dict="t8_prod_brand"   :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品期次">
        <k-field-text v-model="T8DeclarationInfo.prodPeriod" :data-allowblank="false" :data-disabled="true"
                      data-min-value="0" data-validate-type="number" data-type="number"/>
      </k-form-item>
      <k-form-item label="投资管理费率%">
        <k-field-text v-model="T8DeclarationInfo.investManageRate" :data-allowblank="false" data-digits="5"  data-integer-length="3"
                      data-min-value="0" data-validate-type="money" data-type="money"  :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="合作模式">
        <k-field-select v-model="T8DeclarationInfo.cooperationMode" :data-allowblank="false" :data-disabled="true" data-dict="cooperation_mode"/>
      </k-form-item>
      <k-form-item label="合作机构名称" v-show="T8DeclarationInfo.cooperationMode != '01'">
        <k-field-text v-model="T8DeclarationInfo.cooperateOrganizationName"  :data-max-length="120" :data-disabled="true"
                      :data-allowblank="T8DeclarationInfo.cooperationMode == '01'"/>
      </k-form-item>
      <k-form-item label="投资本金到账日">
        <k-field-select v-model="T8DeclarationInfo.investPrincipalArriveDate" :data-allowblank="false" :data-disabled="true" data-dict="invest_principal_arrive_date"/>
      </k-form-item>
      <k-form-item label="投资收益到账日">
        <k-field-select v-model="T8DeclarationInfo.investIncomeArriveDate" :data-allowblank="false" :data-disabled="true" data-dict="invest_income_arrive_date"/>
      </k-form-item>
      <k-form-item label="产品增信标识">
        <k-field-select v-model="T8DeclarationInfo.prodCreditLogo" :data-allowblank="false" :data-disabled="true" data-dict="prod_credit_logo"/>
      </k-form-item>
      <k-form-item label="产品增信机构类型" v-show="T8DeclarationInfo.prodCreditLogo == '01'">
        <k-field-select v-model="T8DeclarationInfo.prodCreditType" :data-allowblank="T8DeclarationInfo.prodCreditLogo != '01'"
                        data-dict="prod_credit_type" :data-multiple="true" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品增信形式" v-show="T8DeclarationInfo.prodCreditLogo == '01'">
        <k-field-select v-model="T8DeclarationInfo.prodCreditForm" :data-allowblank="T8DeclarationInfo.prodCreditLogo != '01'"
                        data-dict="prod_credit_form" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="投资资产种类及比例">
        <k-field-text v-model="T8DeclarationInfo.investAssetTypeProportion" :data-allowblank="false" :data-max-length="300" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="备注" :data-col="2">
        <k-field-text v-model="T8DeclarationInfo.remarks" :data-max-length="2000" inputType="textarea" :rows="1" :data-disabled="true"
                      :data-allowblank="T8DeclarationInfo.isFreeRedemption != '99' || T8DeclarationInfo.isMinHoldPeriod != '01' || T8DeclarationInfo.productOperationMode == '1'"/>
      </k-form-item>
    </k-form>
  </div>
</template>

<script>
  import kayak from '@/frame/kayak.js'

  export default {
    props: {
      menuName :'',
      T8DeclarationInfo:{},
      t8ProdInfoId: {
        type: String,
        default: ''
      },
      updSuccess: Function
    },
    data() {
      return {
      };
    },
    methods: {
    },
    computed: {
      value() {
        return this.$attrs.value
      }
    }
  };
</script>
