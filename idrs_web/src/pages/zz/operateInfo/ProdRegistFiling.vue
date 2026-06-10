<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="ProdRegistFiling" data-label-width="90px" data-target="prodRegistFilingGrid" v-model = "searchParam">
        <k-form-item label="操作日期">
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="操作人员">
          <k-field-text v-model="searchParam.summitUser"/>
        </k-form-item>
        <k-form-item label="操作类型">
          <k-field-select v-model="searchParam.opType"  data-dict="op_type"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="searchParam.prodName" />
        </k-form-item>
        <k-form-item label="行内标识码">
          <k-field-text v-model="searchParam.identCode"/>
        </k-form-item>
        <k-form-item label="产品品牌">
          <k-field-text v-model="searchParam.prodBrand" />
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <k-grid ref="prodRegistFilingGrid" @data-row-select="selectRow" data-operate-column="false" data-action="ProdRegistFiling.findProdRegistFilings" >
                <k-grid-column data-align="left"  data-width="120" data-header="操作人员" data-name="summitUser"></k-grid-column>
                <k-grid-column data-align="left"  data-width="120" data-header="操作类型" data-name="opType" data-dict="op_type"></k-grid-column>
                <k-grid-column data-align="left"  data-width="100" data-header="操作日期" data-name="createDate" ></k-grid-column>
                <k-grid-column data-align="left"  data-width="100" data-header="操作时间" data-name="createTime" data-type="time"></k-grid-column>
                <k-grid-column data-align="left" data-header="行内标识码" data-name="identCode" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="发行机构代码" data-name="bankCode" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品审批人姓名" data-name="prodAprvNm" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品审批人身份证号" data-name="approverIdCode" data-width="180"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品设计人姓名" data-name="prodDsnNm" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品设计人身份证号" data-name="designerIdCode" data-width="180"></k-grid-column>
                <k-grid-column data-align="left" data-header="投资经理姓名" data-name="invMngNm" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="投资经理身份证号" data-name="managerIdCode" data-width="180"></k-grid-column>
                <k-grid-column data-align="left" data-header="业务联络人姓名" data-name="contactName" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="业务联络人座机" data-name="contactTelphone" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="业务联络人手机" data-name="contactMobile" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="业务联络人邮箱" data-name="contactEmail" data-width="150"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品收益类型" data-name="prodRetrunType" data-dict="subm_prod_revenue_type" data-width="130"></k-grid-column>
                 <k-grid-column data-align="left" data-header="新老产品标记" data-name="newProd" data-dict="subm_newProd" data-width="100"></k-grid-column >
                <k-grid-column data-align="left" data-header="产品期限" data-name="prodTerm" data-dict="subm_t8_prod_term" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="是否金融同业专属" data-name="fiancialExclusive" data-dict="subm_isTrue" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="资金投向地区" data-name="invertRegion" data-dict="subm_invest_region" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品投资国家或地区（境外）" data-name="invertCountry" data-dict="tr_iss_country" data-width="130"></k-grid-column>
                <k-grid-column data-align="left" data-header="理财业务服务模式" data-name="serviceMode" data-dict="subm_t8_srv_mode" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品运作模式" data-name="operationMode" data-dict="subm_t8_product_operation_mode" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品募集方式" data-name="typeCollect" data-dict="subm_t8_raise_type"  data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品资产配置方式" data-name="assetAcMethod" data-dict="subm_t8_asset_maping" data-width="155"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品管理模式" data-name="prodManaMode" data-dict="subm_prod_manage_mode" data-width="140"></k-grid-column>
                <k-grid-column data-align="left" data-header="实际管理人名称" data-name="acManaName" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品定价方式" data-name="priceMethod" data-dict="subm_prod_price_way" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品投资性质" data-name="investType" data-dict="subm_prod_invest_nature" data-width="100"></k-grid-column>
                <k-grid-column data-align="right" data-header="业绩比较基准%" data-name="prodBenchmark" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="是否设置最短持有期限" data-name="minHoldPeriod" data-dict="subm_isTrue" data-width="100"></k-grid-column>
                <k-grid-column data-align="right" data-header="最短持有期限（天）" data-name="minHoldDay" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="最短持有期后是否自由赎回" data-name="optionRedemptPeriod" data-dict="subm_optionRedemptPeriod" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="是否现金管理类" data-name="cashManager" data-dict="subm_isTrue" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品销售区域" data-name="prodSalesRegion"  data-width="200"></k-grid-column>
                <k-grid-column data-align="right" data-header="起点销售金额" data-name="investThreshold" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="募集币种" data-name="fundCur" data-dict="subm_cur_type" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="兑付本金币种" data-name="principalCur" data-dict="subm_cur_type" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="兑付收益币种" data-name="incomeCur" data-dict="subm_cur_type" data-width="100"></k-grid-column>
                <k-grid-column data-align="right" data-header="销售手续费率%" data-name="salesCommissionRate" data-width="100"></k-grid-column>
                <k-grid-column data-align="right" data-header="托管费率%" data-name="cdFeeRate" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="募集起始日期（从)" data-name="startDateEarliest"  data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="募集起始日期（到)" data-name="startDateLatest"  data-width="100"></k-grid-column>
                <k-grid-column data-align="right" data-header="计划募集金额（元）" data-name="planFundAmt" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="境内托管机构名称" data-name="dcCdName" data-dict="subm_domestic_custodian_name" data-width="200"></k-grid-column>
                <k-grid-column data-align="left" data-header="境内托管机构代码 " data-validate-type="codeletter" data-name="dcCdIdentCode" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="境外托管机构国别" data-name="seasCdNation" data-dict="tr_iss_country" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="境外托管机构名称" data-name="seasCdName" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="投资者风险偏好" data-name="riskLevel" data-dict="subm_investor_risk_preference" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品风险等级" data-name="riskRate" data-dict="subm_prod_risk_level" data-width="120"></k-grid-column>
                <k-grid-column data-align="left" data-header="发行机构提前终止权标识" data-name="earlyTnOption" data-dict="subm_prod_credit_logo" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="客户赎回权标识" data-name="investRdmOption" data-dict="subm_prod_credit_logo" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品品牌" data-name="prodBrand" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品期次" data-name="prodTermNo" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品特殊属性" data-name="prodEspPrpt"  data-dict="subm_prodEspPrpt" data-width="130"></k-grid-column>
                <k-grid-column data-align="right" data-header="投资管理费率%" data-name="manageFeeRate" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="合作模式" data-name="cooperateMode" data-dict="subm_cooperation_mode" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="合作机构名称" data-name="cooperator" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="投资本金到账日" data-name="principalDueDate"  data-dict="subm_invest_income_arrive_date" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="投资收益到账日" data-name="incomeDueDate" data-dict="subm_invest_income_arrive_date" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品增信标识" data-name="prodCrtEnhance" data-dict="subm_prod_credit_logo" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品增信机构类型" data-name="crtInsType" data-dict="subm_prod_credit_type" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="产品增信形式" data-name="prodCrtMethod"  data-dict="subm_prod_credit_form" data-width="100"></k-grid-column>
                <k-grid-column data-align="left" data-header="投资资产种类及比例" data-name="investTypeRatio" data-width="300"></k-grid-column>
                <k-grid-column data-align="left" data-header="备注" data-name="details" data-width="200"></k-grid-column>

      </k-grid>
    </div>

  </div>
</template>

<script>
  export default {
    name: "prodRegistFiling",
    data() {
      return {
        formData: {
        addProdBrandDict:'',
        },
        selectRowData: {},
        searchParam:{},
        BreathDay:[],
        addProdBrandDict: {},
      };
    },
      created() {
          this.loadProdBrandDict();
        },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
      loadProdBrandDict(){
        //请求产品品牌的字典
        this.httpUtil.comnQuery({
          action: "ProdRegistFiling.loadProdBrandDict",
        }).then(data => {
          this.$set(this.formData, 'addProdBrandDict', '');
          this.formData.addProdBrandDict = data.rows;
        }).catch({})
      }
    },
    watch: {
      //查询起息日
      BreathDay() {
        this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
        this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
      },
    }
  };
</script>
