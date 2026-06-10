<template>
  <div class="py-page">
    <div>
      <k-form-search-customize ref="searchFormRef" data-model-name="ProdRegistFilingInfo" data-target="tableGrid" data-label-width="80px" v-model="queryParam">
        <k-form-item label="新增日期">
          <k-field-date v-model="queryParamDateRange" :data-allowblank="false" data-type="daterange" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="searchParam.prodName" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="行内标识码">
          <k-field-text v-model="searchParam.identCode" data-validate-type="text"/>
        </k-form-item>
        <k-form-item label="报送状态">
          <k-field-select v-model="searchParam.registerStatus" data-dict="report_status"/>
        </k-form-item>

        <!-- <k-form-item label="登记流水号">
          <k-field-text v-model="searchParam.registerSerno" data-validate-type="text"/>
        </k-form-item> -->

      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" ref="uploadBtnRef" :load-disabled="false" data-functype="POPUP" class="btn-custom-plain"
                data-target="uploadProdRegistFilingInfoPopup">
            <md-icon>cloud_upload</md-icon>
            导入
          </k-btn>
          <k-btn slot="button" ref="exportRef" class="btn-custom-plain"  data-functype="EXPORT" data-target="tableGrid" data-export-dict="true"
                :data-export-name="'产品申报登记信息管理'" :handleBefore="handleBefore" @downSuccess="downSuccess" :data-handler="handleExport">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
          <k-btn slot="button" class="btn-custom-plain"
              :handleBefore="handleBefore" :data-handler="handleConfirmExport">
            <md-icon>cloud_download</md-icon>
            确认并导出
          </k-btn>
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="auditPopup">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>复核
          </k-btn>
        </div>
      </div>
      <k-grid ref="tableGrid" @data-row-select="selectRow" data-fixed="right" data-operate-width="150px" :data-autoload="false" data-action="ProdRegistFilingInfo.findProdRegistFilingInfos" >
        <k-grid-column data-align="left" data-header="新增日期" data-name="createDate" data-export="false" data-width="80" ></k-grid-column>
        <k-grid-column data-align="left" data-header="版本号" data-name="sysDataVersion" data-export="false" data-width="60" ></k-grid-column>
        <k-grid-column data-align="left" data-header="产品名称" data-name="prodName" data-width="280" ></k-grid-column>
        <k-grid-column data-align="left" data-header="行内标识码" data-name="identCode" data-width="140"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构代码" data-name="bankCode" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品审批人姓名" data-name="prodAprvNm" data-width="90"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品审批人身份证号" data-name="approverIdCode" data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品设计人姓名" data-name="prodDsnNm" data-width="90"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品设计人身份证号" data-name="designerIdCode" data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资经理姓名" data-name="invMngNm" data-width="90"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资经理身份证号" data-name="managerIdCode" data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="业务联络人姓名" data-name="contactName" data-width="90"></k-grid-column>
        <k-grid-column data-align="left" data-header="业务联络人座机" data-name="contactTelphone" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="业务联络人手机" data-name="contactMobile" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="业务联络人邮箱" data-name="contactEmail" data-width="180"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品收益类型" data-name="prodRetrunType" data-dict="subm_prod_revenue_type" data-width="130"></k-grid-column>
         <k-grid-column data-align="left" data-header="新老产品标记" data-name="newProd" data-dict="subm_newProd" data-width="100"></k-grid-column >
        <k-grid-column data-align="left" data-header="产品期限" data-name="prodTerm" data-dict="prod_term" data-width="120" data-dict-type="1"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否金融同业专属" data-name="fiancialExclusive" data-dict="subm_isTrue" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="资金投向地区" data-name="invertRegion" data-dict="subm_invest_region" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品投资国家或地区（境外）" data-name="invertCountry" data-dict="tr_iss_country" data-width="130"></k-grid-column>
        <k-grid-column data-align="left" data-header="理财业务服务模式" data-name="serviceMode" data-dict="subm_t8_srv_mode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品运作模式" data-name="operationMode" data-dict="subm_t8_product_operation_mode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品募集方式" data-name="typeCollect" data-dict="subm_t8_raise_type"  data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品资产配置方式" data-name="assetAcMethod"  data-dict="subm_t8_asset_maping" data-width="155"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品管理模式" data-name="prodManaMode" data-dict="subm_prod_manage_mode" data-width="140"></k-grid-column>
        <k-grid-column data-align="left" data-header="实际管理人名称" data-name="acManaName" data-width="160"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品定价方式" data-name="priceMethod" data-dict="subm_prod_price_way" data-width="140"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品投资性质" data-name="investType" data-dict="subm_prod_invest_nature" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="业绩比较基准%" data-name="prodBenchmark" dataDigits="5" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否设置最短持有期限" data-name="minHoldPeriod" data-dict="subm_isTrue" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="最短持有期限（天）" data-name="minHoldDay" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="最短持有期后是否自由赎回" data-name="optionRedemptPeriod" data-dict="subm_optionRedemptPeriod" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否现金管理类" data-name="cashManager" data-dict="subm_isTrue" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品销售区域" data-name="prodSalesRegion" data-dict="subm_prod_sale_area" data-width="200"></k-grid-column>
        <k-grid-column data-align="right" data-header="起点销售金额" data-name="investThreshold" dataDigits="2" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="募集币种" data-name="fundCur" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="兑付本金币种" data-name="principalCur" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="兑付收益币种" data-name="incomeCur" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="销售手续费率%" data-name="salesCommissionRate" dataDigits="5"  data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="托管费率%" data-name="cdFeeRate" dataDigits="5"  data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="募集起始日期（从)" data-name="startDateEarliest" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="募集起始日期（到)" data-name="startDateLatest" data-width="100"></k-grid-column>
        <k-grid-column data-align="right" data-header="计划募集金额（元）" data-name="planFundAmt" dataDigits="2" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="境内托管机构名称" data-name="dcCdName" data-dict="subm_domestic_custodian_name" data-width="200"></k-grid-column>
        <k-grid-column data-align="left" data-header="境内托管机构代码" data-name="dcCdIdentCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="境外托管机构国别" data-name="seasCdNation" data-dict="tr_iss_country" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="境外托管机构名称" data-name="seasCdName" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资者风险偏好" data-name="riskLevel" data-dict="subm_investor_risk_preference" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品风险等级" data-name="riskRate" data-dict="subm_prod_risk_level" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构提前终止权标识" data-name="earlyTnOption" data-dict="subm_prod_credit_logo" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="客户赎回权标识" data-name="investRdmOption" data-dict="subm_prod_credit_logo" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品品牌" data-name="prodBrand" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品期次" data-name="prodTermNo" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品特殊属性" data-name="prodEspPrpt"  data-dict="subm_prodEspPrpt" data-width="130"></k-grid-column>
        <k-grid-column data-align="right" data-header="投资管理费率%" data-name="manageFeeRate"  dataDigits="5" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="合作模式" data-name="cooperateMode" data-dict="subm_cooperation_mode" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="合作机构名称" data-name="cooperator" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资本金到账日" data-name="principalDueDate"  data-dict="subm_invest_income_arrive_date" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资收益到账日" data-name="incomeDueDate" data-dict="subm_invest_income_arrive_date" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品增信标识" data-name="prodCrtEnhance" data-dict="subm_prod_credit_logo" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品增信机构类型" data-name="crtInsType" data-dict="subm_prod_credit_type" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="产品增信形式" data-name="prodCrtMethod"  data-dict="subm_prod_credit_form" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资资产种类及比例" data-name="investTypeRatio" data-width="350"></k-grid-column>
        <k-grid-column data-align="left" data-header="备注" data-name="details" data-width="200"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记流水号" data-name="registerSerno" data-width="250" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记日期" data-name="registerDate"  data-width="100"  data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="报送状态" data-name="registerStatus" data-dict="report_status" data-export="false" data-width="100"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改产品申报登记管理" data-functype="POPUP" data-size="mini"
               v-show="scope.row.row.registerStatus != '5'"   data-target="editProdRegistFilingInfoPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="ProdRegistFilingInfo.deleteProdRegistFilingInfo" data-size="mini"
             v-show="scope.row.row.registerStatus != '5'"     data-type="danger" data-target="tableGrid" :data-confirm="true" data-descript="删除产品申报登记管理">
            删除
          </k-btn>
          <!--<k-btn data-functype="PAGE" data-size="mini" class="btn-custom-plain" :data-model="scope.row.row.id"
                 @click="popupEdit(scope.row.row)"  data-descript="产品申报登记管理错误详情">
            错误详情
          </k-btn>-->
        </template>
      </k-grid>
    </div>

    <!--    添加产品申报登记弹出框   -->
    <k-popup ref="addProdRegistFilingInfoPopup" data-title="新增" >
      <k-form ref="addProdRegistFilingInfoForm" :data-col="2" isFormBodyScreen data-label-width="200px">
        <k-form-item label="行内标识码">
          <k-field-select v-model="formData.identCode" data-action="ProdRegistFilingInfo.findProdInfos" data-value-field="identCode"
                          data-display-field="identCode" :data-allowblank="false" @data-on-change="codeChange" :data-max-length="100"/>
        </k-form-item>
        <k-form-item label="产品名称">
          <k-field-text v-model="formData.prodName" :data-allowblank="false" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="产品品牌">
          <k-field-text v-model="formData.prodBrand" :data-max-length="120"/>
        </k-form-item>
        <k-form-item label="产品特殊属性">
          <k-field-select v-model="formData.prodEspPrpt" data-dict="subm_prodEspPrpt" :data-max-length="8"/>
        </k-form-item>
        <k-form-item label="产品期次">
          <k-field-text v-model="formData.prodTermNo" :data-allowblank="false" :data-max-length="6"/>
        </k-form-item>
        <k-form-item label="发行机构代码">
          <k-field-text v-model="formData.bankCode" :data-allowblank="false"  :data-max-length="6"/>
        </k-form-item>
        <k-form-item label="产品审批人姓名">
          <k-field-text v-model="formData.prodAprvNm" :data-allowblank="false"   :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="产品审批人身份证号">
          <k-field-text v-model="formData.approverIdCode" :data-allowblank="false"   :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="产品审批人身份证号">
          <k-field-text v-model="formData.initApproverIdCode" :data-allowblank="false"   :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="产品设计人">
          <k-field-text v-model="formData.prodDsnNm" :data-allowblank="false" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="产品设计人身份证号">
          <k-field-text v-model="formData.designerIdCode" :data-allowblank="false" :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="产品设计人身份证号">
          <k-field-text v-model="formData.initDesignerIdCode" :data-allowblank="false" :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="投资经理姓名">
          <k-field-text v-model="formData.invMngNm" :data-allowblank="false" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="投资经理身份证号">
          <k-field-text v-model="formData.managerIdCode" :data-allowblank="false" :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="投资经理身份证号">
          <k-field-text v-model="formData.initManagerIdCode" :data-allowblank="false" :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="业务联络人姓名">
          <k-field-text v-model="formData.contactName" :data-allowblank="false" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="业务联络人座机">
          <k-field-text v-model="formData.contactTelphone" :data-allowblank="false" :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="业务联络人手机">
          <k-field-text v-model="formData.contactMobile" :data-allowblank="false" data-validate-type="telephone" :data-max-length="11"/>
        </k-form-item>
        <k-form-item label="业务联络人邮箱">
          <k-field-text v-model="formData.contactEmail" :data-allowblank="false" data-validate-type="email" :data-max-length="50"/>
        </k-form-item>
        <k-form-item label="产品募集方式">
          <k-field-select v-model="formData.typeCollect" :data-allowblank="false" data-dict="subm_t8_raise_type"/>
        </k-form-item>
        <k-form-item label="产品收益类型">
          <k-field-select v-model="formData.prodRetrunType" :data-allowblank="false" data-dict="subm_prod_revenue_type"/>
        </k-form-item>
        <k-form-item label="产品期限">
          <k-field-select v-model="formData.prodTerm" :data-allowblank="false" data-dict="prod_term" data-dict-type="1"/>
        </k-form-item>
        <k-form-item label="是否金融同业专属">
          <k-field-select v-model="formData.fiancialExclusive" :data-allowblank="false" data-dict="subm_isTrue"/>
        </k-form-item>
        <k-form-item label="资金投向地区">
          <k-field-select v-model="formData.invertRegion" :data-allowblank="false" data-dict="subm_invest_region"   @data-on-change="dataOnChange"/>
        </k-form-item>
        <k-form-item label="产品投资国家或地区（境外）">
          <k-field-select v-model="formData.invertCountry" :data-allowblank="formData.invertCountryAllowblank" :data-disabled="formData.invertCountryAllowblank" data-dict="tr_iss_country" />
        </k-form-item>
        <k-form-item label="理财业务服务模式">
          <k-field-select v-model="formData.serviceMode" :data-allowblank="formData.serviceModeAllowblank" data-dict="subm_t8_srv_mode"/>
        </k-form-item>
        <k-form-item label="产品运作模式">
          <k-field-select v-model="formData.operationMode" :data-allowblank="false" data-dict="subm_t8_product_operation_mode" @data-on-change="dataOnChange"/>
        </k-form-item>
        <k-form-item label="新老产品标记">
          <k-field-select v-model="formData.newProd" data-dict="newProd" data-dict-type="1"/>
        </k-form-item>
        <k-form-item label="是否设置最短持有期限">
          <k-field-select v-model="formData.minHoldPeriod" :data-allowblank="formData.minHoldPeriodAllowblank" :data-disabled="formData.rminHoldPeriodDisabled" data-dict="subm_isTrue"  @data-on-change="dataOnChange"/>
        </k-form-item>
        <k-form-item label="最短持有期限（天）">
          <k-field-text v-model="formData.minHoldDay" :data-allowblank="formData.minHoldDayAllowblank" :data-disabled="formData.minHoldDayDisabled"  :data-max-length="5"  data-validate-type="number" data-min-value="(0" />
        </k-form-item>
        <k-form-item label="最短持有期后是否自由赎回">
          <k-field-select v-model="formData.optionRedemptPeriod" data-dict="subm_optionRedemptPeriod" @data-on-change="dataOnChange"/>
        </k-form-item>
        <k-form-item label="是否现金管理类">
          <k-field-select v-model="formData.cashManager" data-dict="subm_isTrue"/>
        </k-form-item>
        <k-form-item label="产品资产配置方式">
          <k-field-select v-model="formData.assetAcMethod" :data-allowblank="false" data-dict="subm_t8_asset_maping"/>
        </k-form-item>
        <k-form-item label="产品管理模式">
          <k-field-select v-model="formData.prodManaMode" :data-allowblank="false" data-dict="subm_prod_manage_mode"/>
        </k-form-item>
        <k-form-item label="实际管理人名称">
          <k-field-text v-model="formData.acManaName" :data-max-length="120" />
        </k-form-item>
        <k-form-item label="产品定价方式">
          <k-field-select v-model="formData.priceMethod" :data-allowblank="false" data-dict="subm_prod_price_way"/>
        </k-form-item>
        <k-form-item label="产品投资性质">
          <k-field-select v-model="formData.investType" :data-allowblank="false" data-dict="subm_prod_invest_nature"/>
        </k-form-item>
        <k-form-item label="合作模式">
          <k-field-select v-model="formData.cooperateMode" :data-allowblank="false" data-dict="subm_cooperation_mode"/>
        </k-form-item>
        <k-form-item label="合作机构名称" v-show="this.formData.cooperateMode==1">
          <k-field-text v-model="formData.cooperator" :data-max-length="120" />
        </k-form-item>
        <k-form-item label="投资资产种类及比例">
          <k-field-text v-model="formData.investTypeRatio" :data-allowblank="false" :data-max-length="300" />
        </k-form-item>
        <k-form-item label="业绩比较基准%">
          <k-field-text v-model="formData.prodBenchmark" data-validate-type="money" data-type="money"  data-integer-length="1" data-digits="7" />
        </k-form-item>
        <k-form-item label="投资者风险偏好">
          <k-field-select v-model="formData.riskLevel" data-dict="subm_investor_risk_preference" :data-multiple="true" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="产品销售区域">
          <k-field-select v-model="formData.prodSalesRegion" data-dict="subm_prod_sale_area" :data-allowblank="false"  :data-multiple="true"  data-value-field="itemkey"   data-display-field="itemval"/>
        </k-form-item>
        <k-form-item label="募集币种">
          <k-field-select v-model="formData.fundCur" data-dict="subm_cur_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="兑付本金币种">
          <k-field-select v-model="formData.principalCur" data-dict="subm_cur_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="兑付收益币种">
          <k-field-select v-model="formData.incomeCur" data-dict="subm_cur_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="起点销售金额">
          <k-field-text v-model="formData.investThreshold" :data-allowblank="false" data-digits="2"  data-integer-length="13"
                        data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="计划募集金额（元）">
          <k-field-text v-model="formData.planFundAmt" :data-allowblank="false" data-digits="2"  data-integer-length="13"
                        data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="募集起始日期（从)">
          <k-field-date v-model="formData.startDateEarliest" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="募集起始日期（到)">
          <k-field-date v-model="formData.startDateLatest" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="投资本金到帐日">
          <k-field-select v-model="formData.principalDueDate" data-dict="invest_income_arrive_date" data-dict-type="1" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="投资收益到账日">
          <k-field-select v-model="formData.incomeDueDate" data-dict="invest_income_arrive_date" data-dict-type="1" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="销售手续费率%">
          <k-field-text v-model="formData.salesCommissionRate" :data-allowblank="false" data-digits="5" data-integer-length="1" data-min-value="0"
                        data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="投资管理费率%">
          <k-field-text v-model="formData.manageFeeRate" :data-allowblank="false" data-digits="5" data-integer-length="3" data-max-value="100" data-min-value="0"
                        data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="境内托管机构名称" v-show="this.formData.invertRegion != '02'">
          <k-field-select v-model="formData.dcCdName" :data-allowblank="false" data-dict="subm_domestic_custodian_name" @data-on-change="dataOnChange"/>
        </k-form-item>
        <k-form-item label="境内托管机构代码" v-show="this.formData.invertRegion != '02'">
          <k-field-text v-model="formData.dcCdIdentCode" data-validate-type="codeletter" :data-allowblank="true" :data-max-length="12"/>
        </k-form-item>
        <k-form-item label="境外托管机构国别" v-show="this.formData.invertRegion != '01'">
          <k-field-select v-model="formData.seasCdNation" :data-allowblank="false" data-dict="tr_iss_country"/>
        </k-form-item>
        <k-form-item label="境外托管机构名称" v-show="this.formData.invertRegion != '01'">
          <k-field-text v-model="formData.seasCdName" :data-allowblank="false" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="托管费率%">
          <k-field-text v-model="formData.cdFeeRate" :data-allowblank="false" data-digits="5" :data-integer-length="3" :data-max-value="100" :data-min-value="0"
                        data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="产品风险等级">
          <k-field-select v-model="formData.riskRate" :data-allowblank="false" data-dict="subm_prod_risk_level"/>
        </k-form-item>
        <k-form-item label="发行机构提前终止权标识">
          <k-field-select v-model="formData.earlyTnOption" :data-allowblank="false" data-dict="subm_prod_credit_logo"/>
        </k-form-item>
        <k-form-item label="客户赎回权标识">
          <k-field-select v-model="formData.investRdmOption" :data-allowblank="false" data-dict="subm_prod_credit_logo"/>
        </k-form-item>
        <k-form-item label="产品增信标识">
          <k-field-select v-model="formData.prodCrtEnhance" :data-allowblank="false" data-dict="subm_prod_credit_logo"/>
        </k-form-item>
        <k-form-item label="产品增信机构类型">
          <k-field-select v-model="formData.crtInsType" :data-allowblank="false" data-dict="subm_prod_credit_type"  :data-multiple="true" :data-disabled="creditDisabled" />
        </k-form-item>
        <k-form-item label="产品增信形式">
          <k-field-select v-model="formData.prodCrtMethod"  :data-allowblank="false" data-dict="subm_prod_credit_form" :data-disabled="creditDisabled" />
        </k-form-item>
        <k-form-item label="备注">
          <k-field-text v-model="formData.details" :data-allowblank="formData.detailsAllowblank" :data-disabled="formData.detailsDisabled"  :data-max-length="256"/>
        </k-form-item>

        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ProdRegistFilingInfo.addProdRegistFilingInfo" data-from="addProdRegistFilingInfoForm"
                 :data-model="formData" data-target="tableGrid">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

    <!--    修改产品申报登记弹出框   -->
    <k-popup ref="editProdRegistFilingInfoPopup" data-title="修改"  @data-opened="editOpened()">
      <k-form ref="editProdRegistFilingInfoForm" :data-col="2" isFormBodyScreen data-label-width="200px">
        <k-form-item label="行内标识码" :class="[handleItemDiff('identCode')]">
          <k-field-select v-model="formData.identCode" data-action="ProdRegistFilingInfo.findProdInfos" data-value-field="identCode"
                          data-display-field="identCode" :data-disabled="true" :data-allowblank="false" @data-on-change="codeChange"/>
        </k-form-item>
        <k-form-item label="产品名称" :class="[handleItemDiff('prodName')]">
          <k-field-text v-model="formData.prodName" :data-disabled="true" :data-allowblank="false" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="产品品牌" :class="[handleItemDiff('prodBrand')]">
          <k-field-text v-model="formData.prodBrand" :data-max-length="120" data-regx-text="产品品牌不能超过120个字，且必须填写白名单中的文字"/>
        </k-form-item>
        <k-form-item label="产品特殊属性" :class="[handleItemDiff('prodEspPrpt')]">
          <k-field-select v-model="formData.prodEspPrpt" data-dict="subm_prodEspPrpt"/>
        </k-form-item>
        <k-form-item label="产品期次" :class="[handleItemDiff('prodTermNo')]">
          <k-field-text v-model="formData.prodTermNo" :data-allowblank="false" :data-max-length="6"/>
        </k-form-item>
        <k-form-item label="发行机构代码" :class="[handleItemDiff('bankCode')]">
          <k-field-text v-model="formData.bankCode" :data-disabled="true" :data-allowblank="false"  :data-max-length="6"/>
        </k-form-item>
        <k-form-item label="产品审批人姓名" :class="[handleItemDiff('prodAprvNm')]">
          <k-field-text v-model="formData.prodAprvNm" :data-allowblank="false"   :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="产品审批人身份证号" :class="[handleItemDiff('approverIdCode')]">
          <k-field-text v-model="formData.approverIdCode" :data-allowblank="false"  :data-max-length="30" />
        </k-form-item>
        <k-form-item label="产品设计人" :class="[handleItemDiff('prodDsnNm')]">
          <k-field-text v-model="formData.prodDsnNm" :data-allowblank="false" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="产品设计人身份证号" :class="[handleItemDiff('designerIdCode')]">
          <k-field-text v-model="formData.designerIdCode" :data-allowblank="false" :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="投资经理姓名" :class="[handleItemDiff('invMngNm')]">
          <k-field-text v-model="formData.invMngNm" :data-allowblank="false" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="投资经理身份证号" :class="[handleItemDiff('managerIdCode')]">
          <k-field-text v-model="formData.managerIdCode" :data-allowblank="false" :data-max-length="30"/>
        </k-form-item>
        <k-form-item label="业务联络人姓名" :class="[handleItemDiff('contactName')]">
          <k-field-text v-model="formData.contactName" :data-allowblank="false" :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="业务联络人座机" :class="[handleItemDiff('contactTelphone')]">
          <k-field-text v-model="formData.contactTelphone" :data-allowblank="false" :data-max-length="40"/>
        </k-form-item>
        <k-form-item label="业务联络人手机" :class="[handleItemDiff('contactMobile')]">
          <k-field-text v-model="formData.contactMobile" :data-allowblank="false" data-validate-type="telephone" :data-max-length="11"/>
        </k-form-item>
        <k-form-item label="业务联络人邮箱" :class="[handleItemDiff('contactEmail')]">
          <k-field-text v-model="formData.contactEmail" :data-allowblank="false" data-validate-type="email" :data-max-length="50"/>
        </k-form-item>
        <k-form-item label="产品募集方式" :class="[handleItemDiff('typeCollect')]">
          <k-field-select v-model="formData.typeCollect" :data-allowblank="false" data-dict="subm_t8_raise_type"/>
        </k-form-item>
        <k-form-item label="产品收益类型" :class="[handleItemDiff('prodRetrunType')]">
          <k-field-select v-model="formData.prodRetrunType" :data-allowblank="false" data-dict="subm_prod_revenue_type"/>
        </k-form-item>
        <k-form-item label="产品期限" :class="[handleItemDiff('prodTerm')]">
          <k-field-select v-model="formData.prodTerm" :data-allowblank="false" data-dict="prod_term" data-dict-type="1"/>
        </k-form-item>
        <k-form-item label="是否金融同业专属" :class="[handleItemDiff('fiancialExclusive')]">
          <k-field-select v-model="formData.fiancialExclusive" :data-allowblank="false" data-dict="subm_isTrue"/>
        </k-form-item>
        <k-form-item label="资金投向地区" :class="[handleItemDiff('invertRegion')]">
          <k-field-select v-model="formData.invertRegion" :data-allowblank="false" data-dict="subm_invest_region"   @data-on-change="dataOnChange"/>
        </k-form-item>
        <k-form-item label="产品投资国家或地区（境外）" :class="[handleItemDiff('invertCountry')]">
          <k-field-select v-model="formData.invertCountry" :data-allowblank="formData.invertCountryAllowblank"  :data-disabled="formData.invertCountryAllowblank"  data-dict="tr_iss_country"  />
        </k-form-item>
        <k-form-item label="理财业务服务模式" :class="[handleItemDiff('serviceMode')]">
          <k-field-select v-model="formData.serviceMode" :data-allowblank="formData.serviceModeAllowblank" :data-disabled="formData.serviceModeAllowblank"  data-dict="subm_t8_srv_mode"/>
        </k-form-item>
        <k-form-item label="产品运作模式" :class="[handleItemDiff('operationMode')]">
          <k-field-select v-model="formData.operationMode" :data-allowblank="false" data-dict="subm_t8_product_operation_mode"  @data-on-change="dataOnChange"/>
        </k-form-item>
        <k-form-item label="新老产品标记" :class="[handleItemDiff('newProd')]">
          <k-field-select v-model="formData.newProd"  data-dict-type="1" data-dict="newProd"/>
        </k-form-item>
        <k-form-item label="是否设置最短持有期限" :class="[handleItemDiff('minHoldPeriod')]">
          <k-field-select v-model="formData.minHoldPeriod" :data-allowblank="formData.minHoldPeriodAllowblank" :data-disabled="formData.rminHoldPeriodDisabled" data-dict="subm_isTrue"  @data-on-change="dataOnChange"/>
        </k-form-item>
        <k-form-item label="最短持有期限（天）" :class="[handleItemDiff('minHoldDay')]">
          <k-field-text v-model="formData.minHoldDay" :data-allowblank="formData.minHoldDayAllowblank" :data-disabled="formData.minHoldDayDisabled"  :data-max-length="5"  data-validate-type="number" data-min-value="(0"/>
        </k-form-item>
        <k-form-item label="最短持有期后是否自由赎回" :class="[handleItemDiff('optionRedemptPeriod')]">
          <k-field-select v-model="formData.optionRedemptPeriod" :data-allowblank="formData.optionRedemptPeriodAllowblank" :data-disabled="formData.optionRedemptPeriodDisabled" data-dict="subm_optionRedemptPeriod" @data-on-change="dataOnChange"/>
        </k-form-item>
        <k-form-item label="是否现金管理类" :class="[handleItemDiff('cashManager')]">
          <k-field-select v-model="formData.cashManager" :data-disabled="formData.cashManagerDisabled" data-dict="subm_isTrue"/>
        </k-form-item>
        <k-form-item label="产品资产配置方式" :class="[handleItemDiff('assetAcMethod')]">
          <k-field-select v-model="formData.assetAcMethod" :data-allowblank="false" data-dict="subm_t8_asset_maping"/>
        </k-form-item>
        <k-form-item label="产品管理模式" :class="[handleItemDiff('prodManaMode')]">
          <k-field-select v-model="formData.prodManaMode" :data-allowblank="false" data-dict="subm_prod_manage_mode"/>
        </k-form-item>
        <k-form-item label="实际管理人名称" :class="[handleItemDiff('acManaName')]">
          <k-field-text v-model="formData.acManaName" :data-max-length="120" />
        </k-form-item>
        <k-form-item label="产品定价方式" :class="[handleItemDiff('priceMethod')]">
          <k-field-select v-model="formData.priceMethod" :data-allowblank="false" data-dict="subm_prod_price_way"/>
        </k-form-item>
        <k-form-item label="产品投资性质" :class="[handleItemDiff('investType')]">
          <k-field-select v-model="formData.investType" :data-allowblank="false" data-dict="subm_prod_invest_nature"/>
        </k-form-item>
        <k-form-item label="合作模式" :class="[handleItemDiff('cooperateMode')]">
          <k-field-select v-model="formData.cooperateMode" :data-allowblank="false" data-dict="subm_cooperation_mode"/>
        </k-form-item>
        <k-form-item label="合作机构名称" v-show="this.formData.cooperateMode==1" :class="[handleItemDiff('cooperator')]">
          <k-field-text v-model="formData.cooperator" :data-max-length="120" />
        </k-form-item>
        <k-form-item label="投资资产种类及比例" :class="[handleItemDiff('investTypeRatio')]">
          <k-field-text v-model="formData.investTypeRatio" data-placeholder="多个种类用英文分号分割" data-regx=""
                        data-regx-text="正确的格式:  数字%:文字 或者 数字%-数字%:文字，多个以英文分号分割"
                       :data-allowblank="false" :data-max-length="300" />
        </k-form-item>
        <k-form-item label="业绩比较基准%" :class="[handleItemDiff('prodBenchmark')]">
          <k-field-text v-model="formData.prodBenchmark"  data-validate-type="money" data-type="money" data-integer-length="1" data-digits="7" />
        </k-form-item>
        <k-form-item label="投资者风险偏好" :class="[handleItemDiff('riskLevel')]">
          <k-field-select v-model="formData.riskLevel" data-dict="subm_investor_risk_preference" :data-multiple="true" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="产品销售区域" style="width:810px" :class="[handleItemDiff('prodSalesRegion')]">
          <k-field-select style="width:100%" v-model="formData.prodSalesRegion" data-dict="subm_prod_sale_area" :data-allowblank="false"  :data-multiple="true" data-value-field="itemkey"   data-display-field="itemval"/>
        </k-form-item>
        <k-form-item label="募集币种" :class="[handleItemDiff('fundCur')]">
          <k-field-select v-model="formData.fundCur" data-dict="subm_cur_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="兑付本金币种" :class="[handleItemDiff('principalCur')]">
          <k-field-select v-model="formData.principalCur" data-dict="subm_cur_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="兑付收益币种" :class="[handleItemDiff('incomeCur')]">
          <k-field-select v-model="formData.incomeCur" data-dict="subm_cur_type" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="起点销售金额" :class="[handleItemDiff('investThreshold')]">
          <k-field-text v-model="formData.investThreshold" :data-allowblank="false" data-digits="2"  data-integer-length="13" data-min-value="0"
                        data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="计划募集金额（元）" :class="[handleItemDiff('planFundAmt')]">
          <k-field-text v-model="formData.planFundAmt" :data-allowblank="false" data-digits="2"  data-integer-length="13" data-min-value="0"
                        data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="募集起始日期（从)" :class="[handleItemDiff('startDateEarliest')]">
          <k-field-date v-model="formData.startDateEarliest" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="募集起始日期（到)" :class="[handleItemDiff('startDateLatest')]">
          <k-field-date v-model="formData.startDateLatest" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="投资本金到帐日" :class="[handleItemDiff('principalDueDate')]">
          <k-field-select v-model="formData.principalDueDate" data-dict="invest_income_arrive_date" data-dict-type="1" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="投资收益到账日" :class="[handleItemDiff('incomeDueDate')]">
          <k-field-select v-model="formData.incomeDueDate" data-dict="invest_income_arrive_date" data-dict-type="1" :data-allowblank="false"/>
        </k-form-item>
        <k-form-item label="销售手续费率%" :class="[handleItemDiff('salesCommissionRate')]">
          <k-field-text v-model="formData.salesCommissionRate" :data-allowblank="false" data-integer-length="3"  data-max-value="100" data-min-value="0" data-digits="5"
                        data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="投资管理费率%" :class="[handleItemDiff('manageFeeRate')]">
          <k-field-text v-model="formData.manageFeeRate" :data-allowblank="false" data-integer-length="3"  data-max-value="100" data-min-value="0" data-digits="5"
                       data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="境内托管机构名称" v-show="this.formData.invertRegion != '02'" :class="[handleItemDiff('dcCdName')]">
          <k-field-select v-model="formData.dcCdName" :data-allowblank="this.formData.invertRegion == '02'" data-dict="subm_domestic_custodian_name" />
        </k-form-item>
        <k-form-item label="境内托管机构代码" v-show="this.formData.invertRegion != '02'" :class="[handleItemDiff('dcCdIdentCode')]">
          <k-field-text v-model="formData.dcCdIdentCode"  :data-max-length="12"/>
        </k-form-item>
        <k-form-item label="境外托管机构国别" v-show="this.formData.invertRegion != '01'" :class="[handleItemDiff('seasCdNation')]">
          <k-field-select v-model="formData.seasCdNation" :data-allowblank="this.formData.invertRegion == '01'" data-dict="tr_iss_country"/>
        </k-form-item>
        <k-form-item label="境外托管机构名称" v-show="this.formData.invertRegion != '01'" :class="[handleItemDiff('seasCdName')]">
          <k-field-text v-model="formData.seasCdName" :data-allowblank="this.formData.invertRegion == '01'" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="托管费率%" :class="[handleItemDiff('cdFeeRate')]">
          <k-field-text v-model="formData.cdFeeRate" :data-allowblank="false" data-integer-length="3"  data-max-value="100" data-min-value="0" data-digits="5"  data-regx-text="数据范围在0-100，不超过100，且最多有5位小数"
                        data-validate-type="money" data-type="money"/>
        </k-form-item>
        <k-form-item label="产品风险等级" :class="[handleItemDiff('riskRate')]">
          <k-field-select v-model="formData.riskRate" :data-allowblank="false" data-dict="subm_prod_risk_level"/>
        </k-form-item>
        <k-form-item label="发行机构提前终止权标识" :class="[handleItemDiff('earlyTnOption')]">
          <k-field-select v-model="formData.earlyTnOption" :data-allowblank="false" data-dict="subm_prod_credit_logo"/>
        </k-form-item>
        <k-form-item label="客户赎回权标识" :class="[handleItemDiff('investRdmOption')]">
          <k-field-select v-model="formData.investRdmOption" :data-allowblank="false" data-dict="subm_prod_credit_logo"/>
        </k-form-item>
        <k-form-item label="产品增信标识" :class="[handleItemDiff('prodCrtEnhance')]">
          <k-field-select v-model="formData.prodCrtEnhance" :data-allowblank="false" data-dict="subm_prod_credit_logo" @data-on-change="changeEnhance"/>
        </k-form-item>
        <k-form-item label="产品增信机构类型" :class="[handleItemDiff('crtInsType')]">
          <k-field-select v-model="formData.crtInsType" data-dict="subm_prod_credit_type" :data-disabled="creditDisabled"/>
        </k-form-item>
        <k-form-item label="产品增信形式" :class="[handleItemDiff('prodCrtMethod')]">
          <k-field-select v-model="formData.prodCrtMethod" data-dict="subm_prod_credit_form" :data-disabled="creditDisabled"/>
        </k-form-item>
        <k-form-item label="备注" :class="[handleItemDiff('details')]">
          <k-field-text v-model="formData.details" :data-allowblank="formData.detailsAllowblank" :data-disabled="formData.detailsDisabled" :data-max-length="256"/>
        </k-form-item>

        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary"  ref="sumbitedit"   data-from="editProdRegistFilingInfoForm"
                 :data-model="formData" data-target="tableGrid" :data-handler="sumbit_edit" :handle-before="handleBeforeUpdate">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>


<k-popup ref="uploadProdRegistFilingInfoPopup" title="报送数据导入" @data-opened="uploadOpened()">
    <k-form ref="addForm" data-ui="element">
      <!-- <k-form-item label="募集起始日期（从）">
        <k-field-date v-model="formData.reportDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
      </k-form-item> -->
      <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
        <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
            data-accept=".xlsx,.xls"
            :data-error="onSubmitError" :data-success="onSubmitSuccess"
            :data-auto-upload="false"
            data-upload-url="upload/server/RptApp/reportManage/prodRegistFilingImport.json">
        </k-field-excel-upload>
      </k-form-item>
      <k-form-footer data-align="center">
        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="tableGrid" ref="submitBtn"
              :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">
             <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
        </k-btn>
        <k-btn class="btn-custom-plain" data-functype="CLOSE">
          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
      </k-form-footer>
    </k-form>
    </k-popup>
    <k-popup ref="auditInfoPopup">
      <k-form ref="updateAuditStatusForm" :data-col="2" isFormBodyScreen>
         <k-form-item label="报表类型">
           <k-field-text v-model="infoPop.tableName" :data-allowblank="false" data-disabled="true"/>
         </k-form-item>
         <k-form-item label="新增日期" data-label-width="100px">
           <k-field-date v-model="queryParamDateRange" data-type="daterange" data-date-format="yyyy-MM-dd"
                         data-value-format="yyyyMMdd" :data-allowblank="false"/>
         </k-form-item>
         <k-form-item label="复核状态">
           <k-field-select v-model="infoPop.auditStatus" data-dict="xp_disclosure_check_status" data-default-value="1" data-disabled="true"/>
         </k-form-item>
         <k-form-footer slot="footer" data-align="center">
           <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="updateAuditStatusForm"
                  @click="audit" :data-model="infoPop"><md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
           <k-btn class="btn-custom-plain" data-functype="CLOSE"><md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
         </k-form-footer>
      </k-form>
    </k-popup>
  </div>
</template>

<script>
import Tools from "@/utils/tools";
import ProdMixin from "@/pages/zz/manage/mixins/prodMixin.js"
export default {
  name: "ProdRegistFilingInfo",
  mixins: [ProdMixin],
  data() {
    return {
      formData: {
        prodName:'',
        prodBrand:'',
        prodTermNo:'',
        bankCode:'',
        approverIdCode:'',
        prodAprvNm:'',
        prodDsnNm:'',
        designerIdCode:'',
        invMngNm:'',
        reportDate: "",
        invertCountryAllowblank: true,
        prodCrtEnhance:''
      },
      formDataCopy: {},
      selectRowData: {},
      searchParam:{},
      infoPop: {},
      auditInfoPopupData: {
        tableId: 'app_prod_regist_filing_info',
        tableName: '产品申报登记信息管理'
      },
      queryParamDateRange: [],
      abnormalAction: "ProdRegistFilingInfo.getAbnormalData",
      updateStatusAction: "ProdRegistFilingInfo.updateProdRegistFilingInfoStatus",
      comfirmExportParam: {},
      saveLoading:false
    };
  },
  computed: {
    queryParam () {
      return {
          'beginDate': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
          'queryDate': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
          'prodName': this.searchParam.prodName,
          'identCode': this.searchParam.identCode,
          'registerStatus': this.searchParam.registerStatus,
          'registerSerno':this.searchParam.registerSerno,
      }
    },
    creditDisabled() {
			return this.formData.prodCrtEnhance == "02";
		}
  },
  watch: {
		//"formData.prodCrtEnhance": {
			//handler(v) {
			//	if (v == "02") {
			//	  console.log("prodCrtEnhance",v,"++++")
			//		this.$set(this.formData, "crtInsType", "");
			//		this.$set(this.formData, "prodCrtMethod", "");
			//	} else {
      //    console.log("prodCrtEnhance",v,"------")
			//	}
			// },
		//},
	},
  methods: {
    handleBeforeUpdate() {
      if (this.formNoChangeCb()) {
        this.$refs.editProdRegistFilingInfoPopup.close();
        return false
      }
      return true
    },
     sumbit_edit(){
              this.$refs.sumbitedit.setIconStyle(0,[]);
              if(this.$refs.editProdRegistFilingInfoForm.validate()){
                   this.httpUtil.query({
                           url: 'server/json/RptApp/audit/checkprodRegistFilingInfo.json',
                           params:  this.formData
                                    }).then(res => {
                                      if(res.success) {
                                       this.httpUtil.comnUpdate({
                                                action: 'ProdRegistFilingInfo.updateProdRegistFilingInfo',
                                                params:  this.formData
                                                 }).then(res => {
                                                  if(res.success) {
                                                  this.$refs.editProdRegistFilingInfoPopup.close();
                                              }else{
                                                this.$refs.sumbitedit.setIconStyle(1,[]);
                                              }
                                    })
                              }else{
                                  this.$refs.sumbitedit.setIconStyle(1,[]);
                              }
                   });
              }else{
                this.$refs.sumbitedit.setIconStyle(1,[]);
              }
            },
    audit() {
      let tableName = this.infoPop.tableName;
      let tableId = this.infoPop.tableId;
      let auditStatus = this.infoPop.auditStatus;
      let startDate = this.queryParamDateRange ? this.queryParamDateRange[0] : null;
      let endDate = this.queryParamDateRange ? this.queryParamDateRange[1] : null;
      this.httpUtil.ajax({
         url: 'server/json/RptApp/audit/indexstatus.json',
         params: {
           tableId: tableId,
           startDate: startDate,
           endDate: endDate,
           auditStatus: auditStatus
         }
       }).then(res => {
         if(res.success) {
           if(res.returnmsg=='存在九大附件缺失的产品'){
                Tools.alert("日期区间存在产品对应九大附件未获取到", "danger");
                this.$refs.tableGrid.load(this.queryParam);
                this.$refs.auditInfoPopup.close();
           }else if(res.returnmsg=='存在指标校验未通过数据'){
               this.$confirm("日期区间存在未校验或校验未通过的数据,确认复核吗？", "操作提示", {
               confirmButtonText: "确定",
               cancelButtonText: "取消",
               type: "warning"
               }).then(() => {
                    this.httpUtil.ajax({
                    url: 'server/json/RptApp/audit/status.json',
                    params: {
                      tableId: tableId,
                      startDate: startDate,
                      endDate: endDate,
                      auditStatus: auditStatus
                    }
                  }).then(res => {
                    if(res.success) {
                     Tools.alert(res.returnmsg, "success");
                     this.$refs.tableGrid.load(this.queryParam);
                     this.$refs.auditInfoPopup.close();
                    }
                  })
              }).catch(() => {});
           }else{
              this.httpUtil.ajax({
                url: 'server/json/RptApp/audit/status.json',
                params: {
                  tableId: tableId,
                  startDate: startDate,
                  endDate: endDate,
                  auditStatus: auditStatus
                }
              }).then(res => {
                if(res.success) {
                 Tools.alert(res.returnmsg, "success");
                 this.$refs.tableGrid.load(this.queryParam);
                 this.$refs.auditInfoPopup.close();
                }
              })
           }
         }
       })
    },
    changeEnhance() {
	    if (this.formData.prodCrtEnhance == "02") {
        this.$set(this.formData, "crtInsType", "");
        this.$set(this.formData, "prodCrtMethod", "");
      }
    },
    setConfirmExportParam() {
      this.comfirmExportParam = {
        beginDate: this.queryParamDateRange ? this.queryParamDateRange[0] : null,
        queryDate: this.queryParamDateRange ? this.queryParamDateRange[1] : null,
        prodName: this.searchParam.prodName,
        identCode: this.searchParam.identCode,
        registerStatus: this.searchParam.registerStatus,
      };
    },
    submitUploadParam() {
      //文件上传校验
      let validate = this.$refs.addForm.validate();
      if (validate) {
        let formData = {reportDate: this.formData.reportDate};
        let temp = document.getElementsByClassName('upload-demo');
        let lis = temp[0].childNodes[1].childNodes.length;
        if (lis > 0) {
          this.$refs.uploadBtnRef.setIconStyle(0);
          this.$refs.uploadRef.upload(formData);
          setTimeout(()=>{
            this.$refs.uploadProdRegistFilingInfoPopup.close();
          }, 300)
        } else {
          this.$message.error("上传文件不能为空!");
          return false;
        }
      }
      return false
    },
    onSubmitSuccess() {
      this.$refs.tableGrid.load(this.queryParam);
      this.$refs.uploadBtnRef.setIconStyle(1);

    },
    onSubmitError() {
      this.$refs.uploadBtnRef.setIconStyle(1);
    },
    popupEdit(row){
      let pathUrl = '/main/zz/errorInfo/ProdRgFlInfoErr';
      this.$router.push({
        path: pathUrl,
        query: {
          registerSerno: row.registerSerno,
        },
      });
    },
    selectRow(row, column, event) {
      this.selectRowData = Object.assign({}, row)
      this.formData = Object.assign({}, row)
      this.formDataCopy = Object.assign({}, row)
    },
    codeChange(){
      this.httpUtil.comnQuery({
        action: "ProdRegistFilingInfo.findProdRegistFilingInfosById",
        params: {identCode:this.formData.identCode}
      }).then(data => {
        if (data && data.rows.length > 0){
          this.formData = {}
          this.formData = data.rows[0]
        }
      }).catch({})
    },
    dataOnChange(){
        //开放式
        if(this.formData.operationMode == '03' || this.formData.operationMode == '04'){
            this.formData.minHoldPeriodAllowblank=false;
            this.formData.rminHoldPeriodDisabled = false;//是否设置最短持有期限
            this.formData.minHoldDayDisabled = false;//最短持有期限
            this.formData.optionRedemptPeriodDisabled = false;//最短持有期后是否自由赎回
            this.formData.cashManagerDisabled = false;//现金管理类
        }else{//封闭式
            this.formData.minHoldPeriodAllowblank=true;
            this.formData.rminHoldPeriodDisabled = true;
            this.formData.minHoldDayDisabled = true;
            this.formData.optionRedemptPeriodDisabled = true;
            this.formData.cashManagerDisabled = true;
            this.$set(this.formData, 'minHoldPeriod', '');
            this.$set(this.formData, 'minHoldDay', '');
            this.$set(this.formData, 'optionRedemptPeriod', '');
            this.$set(this.formData, 'cashManager', '');
        }
        //最短持有期限（天）
        if(this.formData.minHoldPeriod == '01'){
            this.formData.minHoldDayAllowblank=false;
            this.formData.minHoldDayDisabled = false;
            this.formData.optionRedemptPeriodAllowblank=false;
            this.formData.optionRedemptPeriodDisabled = false;
        }else{
            this.formData.minHoldDayAllowblank=true;
            this.formData.minHoldDayDisabled = true;
            this.formData.optionRedemptPeriodAllowblank=true;
            this.formData.optionRedemptPeriodDisabled = true;
            this.$set(this.formData, 'minHoldDay', '');
            this.$set(this.formData, 'optionRedemptPeriod', '');
        }
        //备注
         if(this.formData.optionRedemptPeriod == '99' || this.formData.dcCdName == '99'){
            this.formData.detailsAllowblank=false;
         }else{
            this.formData.detailsAllowblank=true;
         }
         //产品投资国家或地区（境外）
          if(this.formData.invertRegion == '02' ){
             this.formData.invertCountryAllowblank=false;
			       this.$set(this.formData, 'dcCdName', '');
			       this.$set(this.formData, 'dcCdIdentCode', '');
          }else if(this.formData.invertRegion == '01' ){
             this.formData.invertCountryAllowblank=true;
             this.$set(this.formData, 'invertCountry', '');
             this.$set(this.formData, 'seasCdNation', '');
             this.$set(this.formData, 'seasCdName', '');
          }
          //理财业务服务模式
           if(this.formData.invertRegion == '01' || this.formData.invertRegion == '03'){
               this.formData.serviceModeAllowblank=false;
           }else{
               this.formData.serviceModeAllowblank=true;
               this.$set(this.formData, 'serviceMode', '');
           }

    },
    uploadOpened() {
      this.formData.reportDate = ''
    },
    editOpened(){
      this.$set(this.formData, 'initApproverIdCode', this.formData.approverIdCode);
      this.$set(this.formData, 'initDesignerIdCode', this.formData.designerIdCode);
      this.$set(this.formData, 'initManagerIdCode', this.formData.managerIdCode);
      this.dataOnChange();
      this.formDataCopy = Object.assign({}, this.formData)
    },
    // 复核弹窗
    auditPopup() {
      this.infoPop = this.auditInfoPopupData;
      this.$refs.auditInfoPopup.popup();
    },
  }
};
</script>
