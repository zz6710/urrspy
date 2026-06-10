<template>
  <div>
    <k-form class="my-form" ref="DeclarationInfo" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px">
      <k-form-item label="产品要素模板">
        <k-field-select v-model="T8DeclarationInfo.declaraModelId"
                        data-action="T8ProdDeclaraModel.findT8ProdDeclaraModels"
                        data-display-field="id,modelName" data-value-field="id" @data-on-change="modelSwitch"/>
      </k-form-item>

      <k-form-item label="产品信息主表id" v-show="false">
        <k-field-text v-model="T8DeclarationInfo.t8ProdInfoId"/>
      </k-form-item>
      <k-form-item label="产品代码" v-show="false">
        <k-field-text v-model="T8DeclarationInfo.prodCode" :data-allowblank="false"/>
      </k-form-item>

      <k-form-item label="产品名称">
        <k-field-text v-model="T8DeclarationInfo.prodName" :data-disabled="true" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="行内标识码">
        <k-field-text v-model="T8DeclarationInfo.internalIdentCode" :data-allowblank="false" :data-disabled="true" />
      </k-form-item>
      <k-form-item label="发行机构代码">
        <k-field-text v-model="T8DeclarationInfo.issuerCode" :data-default-value="'C10308'"/>
      </k-form-item>
      <k-form-item label="产品审批人姓名">
        <k-field-select v-model="T8DeclarationInfo.approverName" data-action='T8ProdCustomerInfo.find2'
                        :data-params="{'custType':'0'}" :dataAllowblank='false'
                        data-display-field="custName" data-value-field="custName" @data-on-change="checkChange"/>

      </k-form-item>
      <k-form-item label="产品审批人证件号">
        <k-field-text v-model="T8DeclarationInfo.approverIdcardNoTm" :data-allowblank="false"
                      :data-max-length="32" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品设计人姓名">
        <k-field-select v-model="T8DeclarationInfo.designerName" data-action='T8ProdCustomerInfo.find2'
                        :data-params="{'custType':'1',}" data-display-field="custName" data-value-field="custName"
                        :data-allowblank="false" @data-on-change="designerNameChange"/>
      </k-form-item>
      <k-form-item label="产品设计人证件号">
        <k-field-text v-model="T8DeclarationInfo.designerIdcardNoTm"
                      :data-max-length="32" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="投资经理名称">
        <k-field-select v-model="T8DeclarationInfo.investManageName" data-action='T8ProdCustomerInfo.find2'
                        :data-params="{'custType':'2'}" data-display-field="custName" data-value-field="custName"
                        @data-on-change="investManageNameChange" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="投资经理证件号">
        <k-field-text v-model="T8DeclarationInfo.investManageIdcardNoTm" :data-allowblank="false"
                      :data-max-length="32" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="业务联系人姓名">
        <k-field-select v-model="T8DeclarationInfo.businessContactName" data-action='T8ProdCustomerInfo.find2'
                        :data-params="{'custType':'3'}" data-display-field="custName" data-value-field="custName"
                        :data-allowblank="false" @data-on-change="businessContactNameChange"/>
      </k-form-item>
      <k-form-item label="业务联系座机号">
        <k-field-text v-model="T8DeclarationInfo.businessContactLandline" :data-allowblank="false" :data-max-length="20"
                      :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="业务联系人手机号">
        <k-field-text v-model="T8DeclarationInfo.businessContactPhoneTm" :data-allowblank="false" :data-max-length="11"
                      :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="业务联络人邮箱">
        <k-field-text v-model="T8DeclarationInfo.businessContactEmail" :data-allowblank="false" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品收益类型">
        <k-field-select v-model="T8DeclarationInfo.revenueType" data-dict="prod_revenue_type" :data-allowblank="false"
                        :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品期限">
        <k-field-select v-model="T8DeclarationInfo.prodTerm" data-dict="t8_prod_term" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="是否金融同业专属">
        <k-field-select v-model="T8DeclarationInfo.isFinancialIndustry" data-dict="is_financial_industry"
                        :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="资金投向地区">
        <k-field-select v-model="T8DeclarationInfo.investRegion" data-dict="invest_region" :data-default-value="'01'"
                        :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="产品投资国家或地区" v-show="T8DeclarationInfo.investRegion != '01'">
        <k-field-select v-model="T8DeclarationInfo.investRegionOutside" data-dict="t8_countries_regions"
                        :data-multiple="true"/>
      </k-form-item>
      <k-form-item label="理财业务服务模式" v-show="T8DeclarationInfo.investRegion != '02'">
        <k-field-select v-model="T8DeclarationInfo.financialServiceMode" data-dict="t8_invest_region"
                        :data-allowblank="T8DeclarationInfo.investRegion == '02'" data-default-value="01"/>
      </k-form-item>
      <k-form-item label="产品运作模式">
        <k-field-select v-model="T8DeclarationInfo.productOperationMode" :data-allowblank="false"
                        data-dict="t8_product_operation_mode" :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品募集方式">
        <k-field-select v-model="T8DeclarationInfo.prodRaiseMethod" data-dict="t8_raise_type" :data-allowblank="false"
                        :data-disabled="true"/>
      </k-form-item>
      <k-form-item label="产品资产配置方式">
        <k-field-select v-model="T8DeclarationInfo.prodAssetAllocation" data-dict="t8_asset_maping"
                        :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="产品管理模式">
        <k-field-select v-model="T8DeclarationInfo.prodManageMode" data-dict="t8_manage_mode" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="实际管理人名称"
                   v-show="T8DeclarationInfo.prodManageMode != '01' && T8DeclarationInfo.prodManageMode != '04'">
        <k-field-text v-model="T8ProdInfo.managerCode"
                      :data-allowblank="T8DeclarationInfo.prodManageMode == '01' || T8DeclarationInfo.prodManageMode == '04'"
                      :data-max-length="120"/>
      </k-form-item>
      <k-form-item label="产品定价方式">
        <k-field-select v-model="T8DeclarationInfo.prodPriceWay" :data-allowblank="false" data-dict="prod_price_way"/>
      </k-form-item>
      <k-form-item label="产品投资性质">
        <k-field-select v-model="T8DeclarationInfo.prodInvestNature" data-dict="prod_invest_nature"
                        :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="业绩比较基准">
        <k-field-text v-model="T8DeclarationInfo.performanceBenchmark" data-digits="5" data-type="number"
                      data-validate-type="number"
                      data-integer-length="3" data-min-value="(0"/>
      </k-form-item>
      <k-form-item label="是否设置最短持有期限" v-show="T8DeclarationInfo.productOperationMode != '1'">
        <k-field-select v-model="T8DeclarationInfo.isMinHoldPeriod"
                        :data-allowblank="T8DeclarationInfo.productOperationMode == '1'"
                        data-dict="is_min_hold_period"/>
      </k-form-item>
      <k-form-item label="最短持有期限(天)"
                   v-show="T8DeclarationInfo.isMinHoldPeriod == '01' && T8DeclarationInfo.productOperationMode != '1'">
        <k-field-text v-model="T8DeclarationInfo.minHoldPeriod" data-min-value="0" data-max-value="99999"
                      data-validate-type="number" data-type="number"
                      :data-allowblank="T8DeclarationInfo.isMinHoldPeriod != '01' || T8DeclarationInfo.productOperationMode == '1'"/>
      </k-form-item>
      <k-form-item label="最短持有期后是否自由赎回"
                   v-show="T8DeclarationInfo.isMinHoldPeriod == '01' && T8DeclarationInfo.productOperationMode != '1'">
        <k-field-select v-model="T8DeclarationInfo.isFreeRedemption" data-dict="is_free_redemption"
                        :data-allowblank="T8DeclarationInfo.isMinHoldPeriod != '01' || T8DeclarationInfo.productOperationMode == '1'"/>
      </k-form-item>
      <k-form-item label="是否现金管理类">
        <k-field-select v-model="T8DeclarationInfo.isCashManagement" data-dict="is_cash_management"
                        :data-default-value="T8DeclarationInfo.productOperationMode == 3 ? '01' : '02'"
                        :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="产品销售区域" :data-col="2">
        <k-field-select v-model="T8DeclarationInfo.prodSalesArea" :data-allowblank="false" data-dict="prod_sale_area"
                        data-multiple="true"
                        :data-max-length="2000" inputType="textarea" :rows="1"/>
      </k-form-item>
      <!--  <k-form-item label="产品销售区域"  :data-col="2">-->
      <!--    <k-field-text v-model="T8DeclarationInfo.prodSalesArea" :data-allowblank="false"-->
      <!--                    :data-max-length="2000" inputType="textarea" :rows="5"/>-->
      <!--  </k-form-item>-->
      <k-form-item label="起点销售金额">
        <k-field-text v-model="T8DeclarationInfo.startSalesAmount" :data-allowblank="false" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="募集币种">
        <k-field-select v-model="T8DeclarationInfo.raisedCurrency" data-dict="t8_prod_currtype_more"
                        :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="兑付本金币种">
        <k-field-select v-model="T8DeclarationInfo.cashGoldCoin" data-dict="t8_prod_currtype_more"
                        :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="兑付收益币种">
        <k-field-select v-model="T8DeclarationInfo.currencyCashIncome" data-dict="t8_prod_currtype_more"
                        :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="销售手续费率%">
        <k-field-text v-model="T8DeclarationInfo.salesServiceRate" :data-allowblank="false" data-digits="5"
                      data-integer-length="3"
                      data-min-value="0" data-validate-type="money" data-type="money"/>
      </k-form-item>
      <k-form-item label="托管费率%">
        <k-field-text v-model="T8DeclarationInfo.escrowRate" :data-allowblank="false" data-digits="5"
                      data-integer-length="3"
                      data-min-value="0" data-validate-type="money" data-type="money"/>
      </k-form-item>
      <k-form-item label="募集起始日期（从）">
        <k-field-date v-model="T8DeclarationInfo.raiseDateStart" :data-allowblank="false"
                      data-date-format="yyyy-MM-dd"/>
      </k-form-item>
      <k-form-item label="募集结束日期（到）">
        <k-field-date v-model="T8DeclarationInfo.raiseDateEnd" :data-allowblank="false" data-date-format="yyyy-MM-dd"/>
      </k-form-item>
      <k-form-item label="计划募集金额">
        <k-field-text v-model="T8DeclarationInfo.planFundRaiseAmount" :data-allowblank="false" data-digits="2"
                      data-integer-length="14"
                      data-min-value="0" data-validate-type="money" data-type="money" data-show-gbmoney="true"/>
      </k-form-item>
      <k-form-item label="境内托管机构名称" v-show="T8DeclarationInfo.investRegion != '02'">
        <k-field-select v-model="T8DeclarationInfo.domesticTrusteeName" data-dict="t8_trutee_bank"
                        :data-disabled="true" :data-allowblank="T8DeclarationInfo.investRegion == '02'"/>
      </k-form-item>
      <k-form-item label="境内托管机构代码" v-show="T8DeclarationInfo.investRegion != '02'">
        <k-field-text v-model="T8DeclarationInfo.domesticTrusteeCode"
                      :data-disabled="true" :data-allowblank="T8DeclarationInfo.investRegion == '02'"/>
      </k-form-item>
      <k-form-item label="境外托管机构国别" v-show="T8DeclarationInfo.investRegion != '01'">
        <k-field-select v-model="T8DeclarationInfo.countryOverseasTrusteeshipInstitution" data-dict="t8_nation_code"
                        :data-allowblank="T8DeclarationInfo.investRegion == '01'"/>
      </k-form-item>
      <k-form-item label="境外托管机构名称" v-show="T8DeclarationInfo.investRegion != '01'">
        <k-field-text v-model="T8DeclarationInfo.overseasTrusteeshipInstitutionName"
                      :data-allowblank="T8DeclarationInfo.investRegion == '01'"/>
      </k-form-item>
      <k-form-item label="投资者风险偏好">
        <k-field-select v-model="T8DeclarationInfo.investorRiskPreference" data-dict="investor_risk_preference"
                        data-multiple="true" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="产品风险等级">
        <k-field-select v-model="T8DeclarationInfo.prodRiskLevel" data-dict="prod_risk_level" :data-allowblank="false"/>
      </k-form-item>
      <k-form-item label="发行机构提前终止权标识">
        <k-field-select v-model="T8DeclarationInfo.earlyTerminationFlag" :data-allowblank="false"
                        data-dict="early_termination_flag"/>
      </k-form-item>
      <k-form-item label="客户赎回权标识">
        <k-field-select v-model="T8DeclarationInfo.customerRedemptionFlag" :data-allowblank="true"
                        data-dict="customer_redemption_flag"/>
      </k-form-item>
      <k-form-item label="产品品牌">
        <k-field-select v-model="T8DeclarationInfo.prodBrand" data-dict="t8_prod_brand"/>
      </k-form-item>
      <k-form-item label="产品期次">
        <k-field-text v-model="T8DeclarationInfo.prodPeriod" :data-allowblank="false"
                      data-min-value="0" data-validate-type="number" data-type="number"/>
      </k-form-item>
      <k-form-item label="投资管理费率%">
        <k-field-text v-model="T8DeclarationInfo.investManageRate" :data-allowblank="false" data-digits="5"
                      data-integer-length="3"
                      data-min-value="0" data-validate-type="money" data-type="money"/>
      </k-form-item>
      <k-form-item label="合作模式">
        <k-field-select v-model="T8DeclarationInfo.cooperationMode" :data-allowblank="false"
                        data-dict="cooperation_mode"/>
      </k-form-item>
      <k-form-item label="合作机构名称" v-show="T8DeclarationInfo.cooperationMode != '01'">
        <k-field-text v-model="T8DeclarationInfo.cooperateOrganizationName" :data-max-length="120"
                      :data-allowblank="T8DeclarationInfo.cooperationMode == '01'"/>
      </k-form-item>
      <k-form-item label="投资本金到账日">
        <k-field-select v-model="T8DeclarationInfo.investPrincipalArriveDate" :data-allowblank="false"
                        data-dict="invest_principal_arrive_date"/>
      </k-form-item>
      <k-form-item label="投资收益到账日">
        <k-field-select v-model="T8DeclarationInfo.investIncomeArriveDate" :data-allowblank="false"
                        data-dict="invest_income_arrive_date"/>
      </k-form-item>
      <k-form-item label="产品增信标识">
        <k-field-select v-model="T8DeclarationInfo.prodCreditLogo" :data-allowblank="false"
                        data-dict="prod_credit_logo"/>
      </k-form-item>
      <k-form-item label="产品增信机构类型" v-show="T8DeclarationInfo.prodCreditLogo == '01'">
        <k-field-select v-model="T8DeclarationInfo.prodCreditType"
                        :data-allowblank="T8DeclarationInfo.prodCreditLogo != '01'"
                        data-dict="prod_credit_type" :data-multiple="true"/>
      </k-form-item>
      <k-form-item label="产品增信形式" v-show="T8DeclarationInfo.prodCreditLogo == '01'">
        <k-field-select v-model="T8DeclarationInfo.prodCreditForm"
                        :data-allowblank="T8DeclarationInfo.prodCreditLogo != '01'"
                        data-dict="prod_credit_form"/>
      </k-form-item>
      <k-form-item label="投资资产种类及比例" :data-col="2">
        <k-field-text v-model="T8DeclarationInfo.investAssetTypeProportion" inputType="textarea"
                      :data-allowblank="false" :data-max-length="300"/>
      </k-form-item>
      <k-form-item label="备注" :data-col="2">
        <k-field-text v-model="T8DeclarationInfo.remarks" :data-max-length="2000" inputType="textarea" :rows="1"
                      :data-allowblank="T8DeclarationInfo.isFreeRedemption != '99' || T8DeclarationInfo.isMinHoldPeriod != '01' || T8DeclarationInfo.productOperationMode == '1'"/>
      </k-form-item>
      <k-form-footer data-align="center" v-show="menuName == 'ProdDeclareParamList'">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="T8ProdDeclara.addT8ProdDeclaraAdd"
               data-from="DeclarationInfo"
               :data-model="T8DeclarationInfo" data-target="t8ProdDeclaraGrid" :data-max-length="255"
               :data-handler="addHandler"
               :data-after-success="passDataSuccess">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>保存
        </k-btn>
      </k-form-footer>
    </k-form>
  </div>
</template>

<script>
import kayak from '@/frame/kayak.js'

export default {
  props: {
    T8ProdInfo: {},
    menuName: '',
    T8DeclarationInfo: {},
    t8ProdInfoId: {
      type: String,
      default: ''
    },
    t8TruteeInfoId: {
      type: String,
      default: ''
    },
    updSuccess: Function
  },
  data() {
    return {};
  },
  methods: {
    passDataSuccess() {
      this.$emit('isShowButton', '1')
    },
    addHandler(val) {
      this.$set(val, 'assemblyMenuType', 'declarationInfo');
    },
    modelSwitch(value) {
      //this.detailData = value;
      this.httpUtil.comnQuery({
        action: 'T8ProdDeclaraModel.findT8ProdDeclaraModels',
        params: {
          id: value,
        }
      }).then(data => {
        this.$nextTick(() => {
          if (data.rows.length > 0) {
            this.$set(this.T8DeclarationInfo, "declaraModelId", data.rows[0].id);
            this.$set(this.T8DeclarationInfo, "approverName", data.rows[0].approverName);          //产品审批人姓名
            this.$set(this.T8DeclarationInfo, "approverIdcardNo", data.rows[0].approverIdcardNo);
            this.$set(this.T8DeclarationInfo, "approverIdcardNoTm", data.rows[0].approverIdcardNoTm);//产品审批人证件号
            this.$set(this.T8DeclarationInfo, "designerName", data.rows[0].designerName);             //产品设计人姓名
            this.$set(this.T8DeclarationInfo, "designerIdcardNo", data.rows[0].designerIdcardNo);     //产品设计人证件号
            this.$set(this.T8DeclarationInfo, "designerIdcardNoTm", data.rows[0].designerIdcardNoTm);
            this.$set(this.T8DeclarationInfo, "investManageName", data.rows[0].investManageName);     //投资经理名称
            this.$set(this.T8DeclarationInfo, "investManageIdcardNo", data.rows[0].investManageIdcardNo); //投资经理证件号
            this.$set(this.T8DeclarationInfo, "investManageIdcardNoTm", data.rows[0].investManageIdcardNoTm);
            this.$set(this.T8DeclarationInfo, "businessContactName", data.rows[0].businessContactName);    //业务联系人姓名
            this.$set(this.T8DeclarationInfo, "businessContactLandline", data.rows[0].businessContactLandline);///业务联系座机号
            this.$set(this.T8DeclarationInfo, "businessContactPhone", data.rows[0].businessContactPhone);      //业务联系人手机号
            this.$set(this.T8DeclarationInfo, "businessContactPhoneTm", data.rows[0].businessContactPhoneTm);
            this.$set(this.T8DeclarationInfo, "businessContactEmail", data.rows[0].businessContactEmail);    //业务联络人邮箱
            this.$set(this.T8DeclarationInfo, "investRegion", data.rows[0].investRegion);
            this.$set(this.T8DeclarationInfo, "investRegionOutside", data.rows[0].investRegionOutside);     //产品投资国家或地区
            this.$set(this.T8DeclarationInfo, "financialServiceMode", data.rows[0].financialServiceMode);     //理财业务服务模式
            this.$set(this.T8DeclarationInfo, "prodAssetAllocation", data.rows[0].prodAssetAllocation);       //产品资产配置方式
            this.$set(this.T8DeclarationInfo, "prodManageMode", data.rows[0].prodManageMode);            //产品管理模式
            this.$set(this.T8DeclarationInfo, "actualManagerName", data.rows[0].actualManagerName);       //实际管理人名称
            this.$set(this.T8DeclarationInfo, "prodPriceWay", data.rows[0].prodPriceWay);               //产品定价方式
            // this.$set(this.T8DeclarationInfo, "prodSalesArea", data.rows[0].prodSalesArea);               //产品销售区域
            this.$set(this.T8DeclarationInfo, "earlyTerminationFlag", data.rows[0].earlyTerminationFlag);//发行机构提前终止权标识
            this.$set(this.T8DeclarationInfo, "customerRedemptionFlag", data.rows[0].customerRedemptionFlag);//客户赎回权标识
            this.$set(this.T8DeclarationInfo, "cooperationMode", data.rows[0].cooperationMode);               //合作模式
            this.$set(this.T8DeclarationInfo, "cooperateOrganizationName", data.rows[0].cooperateOrganizationName); //合作机构名称
            this.$set(this.T8DeclarationInfo, "investPrincipalArriveDate", data.rows[0].investPrincipalArriveDate);//投资本金到账日
            this.$set(this.T8DeclarationInfo, "investIncomeArriveDate", data.rows[0].investIncomeArriveDate);      //投资金收益到账日
            this.$set(this.T8DeclarationInfo, "prodCreditLogo", data.rows[0].prodCreditLogo);                 //产品增信标识
            this.$set(this.T8DeclarationInfo, "prodCreditType", data.rows[0].prodCreditType);                 //产品增信机构类型
            this.$set(this.T8DeclarationInfo, "prodCreditForm", data.rows[0].prodCreditForm);                 //产品增信形式
            this.$set(this.T8DeclarationInfo, "investAssetTypeProportion", data.rows[0].investAssetTypeProportion);
            this.$set(this.T8DeclarationInfo, "remarks", data.rows[0].remarks);


            // this.$set(this.T8DeclarationInfo,"isMinHoldPeriod",data.rows[0].isMinHoldPeriod);
            // this.$set(this.T8DeclarationInfo,"minHoldPeriod",data.rows[0].minHoldPeriod);
            // this.$set(this.T8DeclarationInfo,"isFreeRedemption",data.rows[0].isFreeRedemption);


          }
        })
      });
    },
    checkChange(value) {
      this.httpUtil.comnQuery({
        action: 'T8ProdCustomerInfo.find2',
        params: {
          custName: value,
          custType: '0'
        }
      }).then(data0 => {

        this.$nextTick(() => {
          if (data0.rows.length > 0) {
            this.$set(this.T8DeclarationInfo, "approverIdcardNoTm", data0.rows[0].idCode);
            this.$set(this.T8DeclarationInfo, "approverIdcardNo", data0.rows[0].idCodeNo);
          }
        })
      });
    },
    designerNameChange(value) {
      this.httpUtil
        .comnQuery({
          action: "T8ProdCustomerInfo.find2",
          params: {
            custName: value,
            custType: '1'
          },
        })
        .then((data1) => {
          this.$nextTick(() => {
            if (data1.rows.length > 0) {
              this.$set(this.T8DeclarationInfo, "designerIdcardNoTm", data1.rows[0].idCode);
              this.$set(this.T8DeclarationInfo, "designerIdcardNo", data1.rows[0].idCodeNo);

            }
          });
        });
    },
    investManageNameChange(value) {
      this.httpUtil
        .comnQuery({
          action: "T8ProdCustomerInfo.find2",
          params: {
            custName: value,
            custType: '2'
          },
        })
        .then((data2) => {
          this.$nextTick(() => {
            if (data2.rows.length > 0) {
              this.$set(this.T8DeclarationInfo, "investManageIdcardNoTm", data2.rows[0].idCode);
              this.$set(this.T8DeclarationInfo, "investManageIdcardNo", data2.rows[0].idCodeNo);

            }
          });
        });
    },
    businessContactNameChange(value) {
      this.$set(this.T8DeclarationInfo, "businessContactLandline", '');
      this.$set(this.T8DeclarationInfo, "businessContactPhone", '');
      this.$set(this.T8DeclarationInfo, "businessContactEmail", '');
      this.httpUtil
        .comnQuery({
          action: "T8ProdCustomerInfo.find2",
          params: {
            custName: value,
            custType: '3'
          },
        })
        .then((data3) => {
          this.$nextTick(() => {
            if (data3.rows.length > 0) {
              this.T8DeclarationInfo.businessContactLandline = data3.rows[0].homeTel;
              this.$set(this.T8DeclarationInfo, 'businessContactPhone', data3.rows[0].mobileNo);
              this.T8DeclarationInfo.businessContactPhoneTm = data3.rows[0].mobile;
              this.T8DeclarationInfo.businessContactEmail = data3.rows[0].email;

            }
          });
        });
    },
    // prodChange(value){
    //   this.httpUtil
    //     .comnQuery({
    //       action: "T8ProdInfo.getProdInfos",
    //       params: {
    //         prodCode: value,
    //       },
    //     })
    //     .then((data) => {
    //       this.$nextTick(() => {
    //         if (data.rows.length > 0) {
    //           console.log(data.rows[0]);
    //
    //           this.T8DeclarationInfo.internalIdentCode = data.rows[0].prodCode;
    //           this.T8DeclarationInfo.prodName = data.rows[0].prodName;
    //         }
    //       });
    //     });
    // },

    validateData() {
      return this.$refs.DeclarationInfo.validate();
    }
  },
  computed: {
    value() {
      return this.$attrs.value
    }
  },
  watch: {
    t8TruteeInfoId: {
      handler(newVal, oldVal) {
        /*findTaCustodianBanks3*/
        this.httpUtil.comnQuery({
          action: 'T82006.findTaCustodianBanks3',
          params: {
            id: newVal,
          }
        }).then(data => {
          if (data.rows && data.rows.length > 0) {
            var rows = data.rows;
            for (var i = 0; i < rows.length; i++) {
              if (rows[i].isOutside === '0') {
                this.T8DeclarationInfo.domesticTrusteeName = rows[i].truteeName;
                this.T8DeclarationInfo.domesticTrusteeCode = rows[i].truteeCode;
              } else if (rows[i].isOutside === '1') {
                this.T8DeclarationInfo.countryOverseasTrusteeshipInstitution = rows[i].truteeNation
                this.T8DeclarationInfo.overseasTrusteeshipInstitutionName = rows[i].truteeName;
              }
            }
          }
        });
      }
    }
  }
};
</script>
