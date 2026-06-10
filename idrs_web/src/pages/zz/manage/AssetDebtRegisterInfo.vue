<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="AssetDebtRegisterInfo" data-target="assetDebtRegisterInfoGrid" data-label-width="130px" v-model="queryParam" ref="searchFormRef">

        <k-form-item label="行内资产/负债编码">
          <k-field-text v-model="searchParam.assetCode"/>
        </k-form-item>
        <k-form-item label="数据日期">
          <k-field-date v-model="dataDate" data-type="daterange"  :data-allowblank="false" data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd"/>
        </k-form-item>
        <!--
        <k-form-item label="业务登记日期">
          <k-field-date v-model="RegisterDate" data-type="daterange" data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd"/>
        </k-form-item>
        -->
        <k-form-item label="资产/负债类别">
          <k-field-select v-model="searchParam.assDebtType" data-dict="subm_cbndScdCtg" />
        </k-form-item>
        <k-form-item label="报送状态">
          <k-field-select v-model="searchParam.registerStatus" data-dict="report_status"/>
        </k-form-item>
        <k-form-item label="数据变更类型">
          <k-field-select v-model="searchParam.dataChangeType" data-dict="data_change_type"/>
        </k-form-item>
        <!-- <k-form-item label="理论报送起始日期">
          <k-field-date v-model="searchParam.theoryReportStartDate"   data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd"/>
        </k-form-item> -->
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
<!--           <k-btn class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addAssetDebtRegisterInfoPopup" slot="button">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn> -->
          <k-btn slot="button"  data-functype="POPUP" class="btn-custom-plain" data-target="uploadAssetDebtRegisterInfoPopup">
            <md-icon>cloud_upload</md-icon>导入
          </k-btn>
          <!-- <k-btn slot="button"  class="btn-custom-plain" ref="exportRef"  @downSuccess="downSuccess" :data-handler="handleExport"  data-functype="EXPORT" data-target="assetDebtRegisterInfoGrid" data-export-dict="true"   data-template-name="资产负债要素导出"  data-export-form="searchFormRef"
                  :data-export-name="'资产要素登记管理'">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn> -->
          <k-btn slot="button"  class="btn-custom-plain"  ref="exportRef"  @downSuccess="downSuccess" :handleBefore="handleBefore" :data-handler="handleExport" data-functype="EXPORT" data-target="assetDebtRegisterInfoGrid" data-export-dict="true" data-excel-template ="资产要素登记管理.xlsx"  data-excel-start-line ="2" data-template-name="资产负债要素导出" data-export-form="searchFormRef"
                  data-export-name="资产负债要素登记管理">
            <md-icon>cloud_download</md-icon>
            导出
          </k-btn>
           <k-btn slot="button" class="btn-custom-plain" :handleBefore="handleBefore"  data-excel-template ="资产要素登记管理.xlsx" data-excel-start-line ="2" data-export-dict="true" :data-handler="handleConfirmExport">
            <md-icon>cloud_download</md-icon>确认并导出
          </k-btn>
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="auditPopup">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>复核
          </k-btn>
        </div>
      </div>
      <k-grid ref="assetDebtRegisterInfoGrid" @data-row-select="selectRow" :data-autoload="false"  data-fixed="right" data-operate-width="250px" data-action="AssetDebtRegisterInfo.findAssetDebtRegisterInfos"  >
        <k-grid-column data-align="left" data-header="id" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="数据日期" data-name="theoryReportStartDate" data-export="false" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="报送状态" data-name="registerStatus" data-dict="subm_report_status" data-export="false" data-width="100"></k-grid-column>
				<k-grid-column data-header="公共信息（必填）" data-align="center">
        <k-grid-column data-align="left" data-header="*行内资产/负债编码" data-name="assetCode"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*发行机构代码" data-name="bankCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="*资产/负债类别" data-name="assDebtType" data-dict="subm_cbndScdCtg" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*交易流通场所" data-name="tradeVenue" data-dict="subm_tacdingPlace" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*币种" data-name="cur" data-width="120"></k-grid-column>
				</k-grid-column>
        <!--1 本行/他行存款||大额存单-->
        <k-grid-column data-header="本行/他行存款||大额存单" data-align="center">
        <k-grid-column data-align="left" data-header="资金存入银行" data-name="bbDepositBank" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*存款账号" data-name="bbAccountNo" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*存款金额" data-name="bbDepositAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*起息日" data-name="bbValueDate"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*到期日" data-name="bbMaturityDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*年利率%" data-name="bbAnnualRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*计息基础" data-name="bbInterestBasis" data-width="150"  data-dict="subm_intr_base"></k-grid-column>
        <k-grid-column data-align="left" data-header="存款类型" data-name="bbDepositType" data-width="150" data-dict="subm_deposit_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="结构性存款挂钩标的类别" data-name="bbStructDepositType" data-width="150" data-dict="subm_stru_deposit_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="结构性存款挂钩标的" data-name="bbStructDeposit" data-width="150"></k-grid-column>
				</k-grid-column>
        <!--2 债券类资产/理财直接融资工具/同业存单-->
        <k-grid-column data-header="债券类资产/理财直接融资工具/同业存单" data-align="center">
        <k-grid-column data-align="left" data-header="*代码" data-name="ccIdentCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*名称" data-name="ccName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="具体类别" data-name="ccSpecificBondType" data-dict="subm_spcType" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*发行方式" data-name="ccIssModeBond" data-dict="subm_iss_mode_bond" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*主体评级" data-name="ccIssRatePart" data-dict="subm_mainRating" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*发行机构类型（按规模划分）" data-name="ccInstituteTypeScale" data-dict="subm_instituteTypeTech" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*发行机构类型（按技术领域划分）" data-name="ccInstituteTypeTech" data-dict="subm_isuOrgTypTchno" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*发行机构类型（按经济类型划分）" data-name="ccInstituteTypeEconomic" data-dict="subm_isuOrgTypEcn" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*发行机构所属行业" data-name="ccIndustryIssuer" data-dict="subm_isuOrgBlgIdt" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*登记托管机构" data-name="ccRegistDeposit" data-dict="subm_regTrstOrg" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记托管机构说明" data-name="ccDetailsRegistDeposit" data-width="150"></k-grid-column>
				</k-grid-column>
        <!--3 拆放同业及买入返售/同业拆入及卖出回购-->
        <k-grid-column data-header="拆放同业及买入返售/同业拆入及卖出回购" data-align="center">
        <k-grid-column data-align="left" data-header="*起息日" data-name="ddValueDate"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*到期日" data-name="ddMaturityDate"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*对手方" data-name="ddCounterparty" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*对手方类型" data-name="ddCounterpartyType" data-dict="subm_counterparty_type" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*年利率%" data-name="ddAnnalInterestRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*计息基础" data-name="ddInterestBasis" data-width="150" data-dict="subm_intr_base"></k-grid-column>
        <k-grid-column data-align="left" data-header="回购标的类别" data-name="ddCollateralType" data-dict="subm_interest_basis" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="回购标的金额" data-name="ddCollateralValue" data-width="150"></k-grid-column>
				</k-grid-column>
        <!--4 非标准化债权类资产/新增可投资资产-->
        <k-grid-column data-header="非标准化债权类资产/新增可投资资产" data-align="center">
        <k-grid-column data-align="left" data-header="收/受益权类型" data-name="eeOwnershipType" data-dict="subm_incBenRitType" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*是否属于买入返售" data-name="eeBuyback" data-width="150"  data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="*名称" data-name="eeName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="资产代码" data-name="eeAssetCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*金额" data-name="eeAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="份额面值" data-name="eeUnitParValue" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*起息日" data-name="eeValueDate"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*到期日" data-name="eeMaturityDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="法定到期日" data-name="eeStatutoryMaturityDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*是否有预期收益率" data-name="eeExpectedReturn" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="right" data-header="项目收益率(利率)%" data-name="eeProjectAnnaulReturn" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="计息类型" data-name="eeCouponType" data-width="150"  data-dict="subm_interest_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="规则付息标识" data-name="eeRegualrInterestPay" data-dict="subm_isTrue" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*付息频率（个月/次）" data-name="eeInterestPayFrequency" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="利息分布方式" data-name="eeCouponAllocationType" data-dict="subm_intrAlcMth" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*还本付息情况说明" data-name="eeDetailPrincInterest" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*计息基础" data-name="eeInterestBasis" data-width="150" data-dict="subm_intr_base"></k-grid-column>
        <k-grid-column data-align="left" data-header="基准利率种类" data-name="eeBenchRateType" data-dict="subm_bchmRatTyp" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否有浮动因子" data-name="eeFloatFactor" data-dict="subm_isTrue" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="浮动因子（%）" data-name="eeFloatRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="利差（BP）%" data-name="eeYieldSpreadBp" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="结构档次" data-name="eeStructGrade" data-width="150" data-dict="subm_strcGrd"></k-grid-column>
        <k-grid-column data-align="left" data-header="还本方式" data-name="eePrincPaymentType" data-width="150" data-dict="subm_payPrcpMth"></k-grid-column>
        <k-grid-column data-align="left" data-header="*分期还本条款标识" data-name="eeInstallRepayType" data-width="150" data-dict="subm_insPayPrcpF"></k-grid-column>
        <k-grid-column data-align="left" data-header="基础资产类型" data-name="eeBaseAssetType" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="超额收益分配比例（%）" data-name="eePercentExcInAllot" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*融资人" data-name="eeDebtor" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*融资人内部信用评级" data-name="eeDeptorRate" data-width="150"  ></k-grid-column>
        <k-grid-column data-align="left" data-header="外部评级机构名称及对融资人评级结果" data-name="eeRateAgencyIss" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*融资人类型（按规模划分）" data-name="eeDebtorTypeScale" data-width="150" data-dict="subm_instituteTypeTech"></k-grid-column>
        <k-grid-column data-align="left" data-header="*融资人类型（按技术领域划分）" data-name="eeDebtorTypeTech" data-width="150" data-dict="subm_isuOrgTypTchno"></k-grid-column>
        <k-grid-column data-align="left" data-header="*融资人类型（按经济类型划分）" data-name="eeDebtorTypeEconomic" data-width="150" data-dict="subm_isuOrgTypEcn"></k-grid-column>
        <k-grid-column data-align="left" data-header="*融资项目" data-name="eeProject" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*融资人所属行业" data-name="eeIndustryDebtor" data-width="150" data-dict="subm_isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-align="left" data-header="*项目是否属于重点监控行业和领域" data-name="eeMonitorIndusType" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="重点监控行业和领域类别" data-name="eeMonitorIndustryType" data-width="150" data-dict="subm_keyMntIdtTyp"></k-grid-column>
        <k-grid-column data-align="left" data-header="重点监控行业和领域类别说明" data-name="eeDetailsMonitoryType" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="担保方式" data-name="eeGuaranteeMethod" data-width="150"  data-dict="subm_grntWay"></k-grid-column>
        <k-grid-column data-align="left" data-header="抵质押物类型" data-name="eePledgeType" data-width="150" data-dict="subm_plgTyp"></k-grid-column>
        <k-grid-column data-align="right" data-header="抵质押物价值（元）" data-name="eePledgeValue" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="担保性质" data-name="eeGuaranteeType" data-width="150" data-dict="subm_grntChr"></k-grid-column>
        <k-grid-column data-align="left" data-header="担保人与融资人关系" data-name="eeGuarantorType" data-width="150" data-dict="subm_grntLvrgRel"></k-grid-column>
        <k-grid-column data-align="left" data-header="担保情况说明" data-name="eeDetailGuaranteeStatus" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*融资人主体评级" data-name="eeDebtorRate" data-width="150" data-dict="subm_mainRating"></k-grid-column>
        <k-grid-column data-align="left" data-header="*资产内部评级" data-name="eeInterAssetRate" data-width="150" data-dict="subm_mainRating"></k-grid-column>
        <k-grid-column data-align="left" data-header="*资产外部评级" data-name="eeOutAssetRate" data-width="150" data-dict="subm_mainRating"></k-grid-column>
        <k-grid-column data-align="left" data-header="含权类型" data-name="eeOptionType" data-width="150" data-dict="subm_embOptTyp"></k-grid-column>
        <k-grid-column data-align="left" data-header="行权方式" data-name="eeExerciseDateType" data-width="150" data-dict="subm_xcsRitMth"></k-grid-column>
        <k-grid-column data-align="left" data-header="固定行权日" data-name="eeFixedExerciseDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="首次行权日期" data-name="eeFirstExerciseDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="行权周期" data-name="eeExercisePeriod" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="行权价格" data-name="eeExercisePrice" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="永续条款类型" data-name="eePerpetualType" data-width="150" data-dict="subm_perpTyp"></k-grid-column>
        <k-grid-column data-align="left" data-header="利息递延条款类型" data-name="eeDeferreInterestType" data-width="150" data-dict="subm_intrPpnTyp"></k-grid-column>
        <k-grid-column data-align="left" data-header="递延利息是否计息" data-name="eeInterestDeferred" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="首次重定价日期" data-name="eeFirstRepriceDate"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="重定价周期" data-name="eeRepricePeriod" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="部分赎回标识" data-name="eePartialRedemption" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="right" data-header="部分赎回比例（%）" data-name="eePartialRedemptionRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*选择权" data-name="eeOptionRight" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*行权条件说明" data-name="eeDetailsExerciseTerm" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*融资人所属地区" data-name="eeRegionDebtor" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*融资人组织机构（社会信用）代码" data-name="eeOrganizationCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="增信机构代码" data-name="eeEnhanceInstituteCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="增信机构名称" data-name="eeEnhanceInstituteName" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*融资总费率%" data-name="eeTotalFeeRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*融资项目所属行业" data-name="eeIndustryProject"  data-dict="subm_isuOrgBlgIdt" data-width="150"></k-grid-column>
				</k-grid-column>
        <!--5 票据类/信用证类资产-->
        <k-grid-column data-header="票据类/信用证类资产" data-align="center">
        <k-grid-column data-align="left" data-header="*是否为收/受益权" data-name="ffOwnership" data-width="150" data-dict="tr_is_belong"></k-grid-column>
        <k-grid-column data-align="left" data-header="*是否属于买入返售" data-name="ffBuyback" data-width="150"  data-dict="tr_is_belong"></k-grid-column>
        <k-grid-column data-align="left" data-header="*类型" data-name="ffType" data-width="150" data-dict="subm_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否为标准化票据" data-name="ffStandarBill" data-width="150" data-dict="tr_is_belong"></k-grid-column>
        <k-grid-column data-align="left" data-header="票据代码" data-name="ffNoteCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*数量" data-name="ffQuantity" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*合计金额" data-name="ffAggregateAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*加权剩余期限（天）" data-name="ffWeightRemainDay" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*最长剩余期限（天）" data-name="ffMaxRemainDay" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*最短剩余期限（天）" data-name="ffMinRemainFay" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*起息日" data-name="ffValueDate"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*到期日" data-name="ffMaturityDate"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*行业" data-name="ffIndustry" data-width="150" data-dict="subm_isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-align="right" data-header="*贴现利率（%）" data-name="ffDiscountRate" data-width="150"></k-grid-column>
				</k-grid-column>
        <!--6 权益类资产-->
        <k-grid-column data-header="权益类资产" data-align="center">
        <k-grid-column data-align="left" data-header="股票代码" data-name="ggStockCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*股票/企业名称" data-name="ggName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="股票类型" data-name="ggStockType" data-width="150"  data-dict="subm_stock_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="*行业" data-name="ggIndustry" data-width="150" data-dict="subm_isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资阶段" data-name="ggInvestStage" data-width="150" data-dict="subm_invest_stage"></k-grid-column>
        <k-grid-column data-align="left" data-header="股权退出安排" data-name="ggEquityOutDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*企业类型（按规模划分）" data-name="ggEnterTypeScale" data-width="150" data-dict="subm_instituteTypeTech"></k-grid-column>
        <k-grid-column data-align="left" data-header="*企业类型（按技术领域划分）" data-name="ggEnterTypeTech" data-width="150" data-dict="subm_isuOrgTypTchno"></k-grid-column>
        <k-grid-column data-align="left" data-header="*企业类型（按经济类型划分）" data-name="ggEnterTypeEconomic" data-width="150" data-dict="subm_isuOrgTypEcn"></k-grid-column>
        <k-grid-column data-align="left" data-header="*是否为质押融资" data-name="ggPledgedFinace" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="*是否为债转股" data-name="ggDebtEquitySwap" data-width="150" data-dict="subm_isTrue"></k-grid-column>
				</k-grid-column>
        <!--7 金融衍生品-->
        <k-grid-column data-header="金融衍生品" data-align="center">
        <k-grid-column data-align="left" data-header="*名称" data-name="hhName" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*名义本金" data-name="hhNominalPrincipal" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*标的类别" data-name="hhUnderAssetType" data-width="150" data-dict="subm_stru_deposit_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="*持有目的" data-name="hhHoldObjective" data-width="150" data-dict="subm_holding_obj"></k-grid-column>
				</k-grid-column>
        <!--8 QDII债券资产-->
        <k-grid-column data-header="QDII债券资产" data-align="center">
        <k-grid-column data-align="left" data-header="*所属国家或地区" data-name="iiCountyRegion" data-width="150" data-dict="tr_iss_country"></k-grid-column>
        <k-grid-column data-align="left" data-header="*债券名称" data-name="iiBondName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*债券代码" data-name="iiBondIdentCode" data-width="150" ></k-grid-column>
        <k-grid-column data-align="left" data-header="*发行机构" data-name="iiIssuer" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*发行机构所属行业" data-name="iiIndustryIssuer" data-width="150" data-dict="subm_isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-align="left" data-header="*起息日" data-name="iiValueDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*到期日" data-name="iiMaturityDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*期限（月）" data-name="iiTermMaturity" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*发行机构主体信用评级" data-name="iiIssuerRateBond" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*债券信用评级" data-name="iiBondRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*票面利率%" data-name="iiCoupRate" data-width="150" ></k-grid-column>
        <k-grid-column data-align="right" data-header="*付息频率（个月/次）" data-name="iiInterestPayQuency" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*担保情况说明" data-name="iiDetailsAssureStatus" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*是否含权等特殊条款情况说明" data-name="iiDetailsSpecialTerms" data-width="150"></k-grid-column>
				</k-grid-column>
        <!--9 QDII拆出/逆回购资产-->
        <k-grid-column data-header="QDII拆出/逆回购资产" data-align="center">
        <k-grid-column data-align="left" data-header="*所属国家或地区" data-name="jjCountry" data-width="150" data-dict="tr_iss_country"></k-grid-column>
        <k-grid-column data-align="left" data-header="*起息日" data-name="jjValueDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*到期日" data-name="jjMaturityDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*对手方" data-name="jjCounterparty" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*年利率%" data-name="jjInterestRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*计息基础" data-name="jjInterestBasis" data-width="150" data-dict="subm_intr_base"></k-grid-column>
				</k-grid-column>
        <!--10 QDII股票/基金类资产-->
        <k-grid-column data-header="QDII股票/基金类资产" data-align="center">
        <k-grid-column data-align="left" data-header="*所属国家或地区" data-name="kkCountry" data-width="150" data-dict="tr_iss_country"></k-grid-column>
        <k-grid-column data-align="left" data-header="*股票/基金代码" data-name="kkIdentCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*股票/基金名称" data-name="kkName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*发行机构" data-name="kkIssuer" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*行业" data-name="kkIndustry" data-width="150" data-dict="subm_isuOrgBlgIdt"></k-grid-column>
				</k-grid-column>
        <!--11 QDII结构性票据类资产-->
        <k-grid-column data-header="QDII结构性票据类资产" data-align="center">
        <k-grid-column data-align="left" data-header="*所属国家或地区" data-name="llCountry" data-width="150" data-dict="tr_iss_country"></k-grid-column>
        <k-grid-column data-align="left" data-header="*合约名称" data-name="llContractName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*起息日" data-name="llValueDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*到期日" data-name="llMaturityDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="票面利率%" data-name="llCouponRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="付息频率（个月/次）" data-name="llInterestFrequency" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*固定收益部分所占比例(%)" data-name="llPercentFix" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*衍生金融工具所占比例(%)" data-name="llPercentDerivate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*衍生金融工具具体投资方式" data-name="llDerivateInvetType" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*衍生金融工具挂钩的标的资产" data-name="llUnderAsset" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*利息结算方式" data-name="llDetailsProceeds" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*含权情况说明" data-name="llDetailsOption" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*结构性票据最高收益率%" data-name="llMaxNoteReturn" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="结构性票据最低收益率%" data-name="llMinNoteReturn" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="挂钩标的资产基准价格" data-name="llStrikeUnderAsset" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*挂钩标的资产登记日价格" data-name="llUnderRgPrice" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*交易费" data-name="llTransCosts" data-width="150"></k-grid-column>
        </k-grid-column>
        <!--12 资产管理产品-->
        <k-grid-column data-header="资产管理产品" data-align="center">
        <k-grid-column data-align="left" data-header="*资管计划名称" data-name="mmManagePlanName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否为银行理财产品" data-name="mmManageProduct" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="理财产品登记编码" data-name="mmProductCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否由金融资产投资公司发行" data-name="mmIssuedAssetCompany" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="*资管计划发起人机构编码" data-name="mmPlanIssuerCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="资管计划登记编码" data-name="mmAssetPlanRgCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*管理人" data-name="mmManager" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*托管人" data-name="mmCustodian" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*金额" data-name="mmAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*资金实际投向" data-name="mmActualDirect" data-width="150" data-dict="subm_actual_invest_dir_fund"></k-grid-column>
        <k-grid-column data-align="left" data-header="*资金运用方式" data-name="mmDetailsInvest" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*资金运用行业" data-name="mmIndustryInvest" data-width="150"  data-dict="subm_isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-align="left" data-header="*资管计划成立日期" data-name="mmPlanStartDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*资管计划终止日期" data-name="mmPlanMaturityDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*资管计划属性" data-name="mmPlanType" data-width="150" data-dict="subm_astMngPlanPrpt"></k-grid-column>
        <k-grid-column data-align="right" data-header="*是否有预期收益率" data-name="mmExpectedReturn" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="right" data-header="预期最高收益率%" data-name="mmMaxExpectedReturn" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="预期最低收益率%" data-name="mmMinExpectedReturn" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*购买结构" data-name="mmInvestStructure" data-width="150" data-dict="subm_buyStrc"></k-grid-column>
        <k-grid-column data-align="left" data-header="*管理方式" data-name="mmManagerType" data-width="150"  data-dict="subm_mngMth"></k-grid-column>
        <k-grid-column data-align="right" data-header="*管理费率%" data-name="mmManagerFeeRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*托管费率%" data-name="mmCustodianFeeRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*交易相关合计费率%" data-name="mmTransCostRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*中介服务机构合计费率%" data-name="mmInterFeeRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*其他合计费率%" data-name="mmOtherExpenseRate" data-width="150"></k-grid-column>
        </k-grid-column>
        <!--13 贵金属/商品-->
        <k-grid-column data-header="贵金属/商品" data-align="center">
        <k-grid-column data-align="right" data-header="*所属国家或地区" data-name="nnCountry" data-width="150" data-dict="tr_iss_country"></k-grid-column>
        <k-grid-column data-align="right" data-header="*名称" data-name="nnName" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="期限（天）" data-name="nnTermDays" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*资产价值" data-name="nnAssetValue" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="资产收益率%" data-name="nnAssetReturn" data-width="150"></k-grid-column>
        </k-grid-column>
        <!--14 其他非标准化债权类/权益类/代客境外理财投资QDII/新增可投资资产-->
        <k-grid-column data-header="其他非标准化债权类/权益类/代客境外理财投资QDII/新增可投资资产" data-align="center">
        <k-grid-column data-align="left" data-header="*名称" data-name="ssName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*行内资产类别" data-name="ssAssetType" data-width="150" data-dict="subm_asset_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="行内资产类别说明" data-name="ssDetailsAssetType" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*金额" data-name="ssAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*起息日" data-name="ssValueDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*到期日" data-name="ssMaturityDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*所属国家或地区" data-name="ssCountry" data-width="150"  data-dict="tr_iss_country"></k-grid-column>
        <k-grid-column data-align="left" data-header="*是否有预期收益率" data-name="ssExpectedReturn" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="right" data-header="项目收益率（利率）%" data-name="ssAnnualReturn" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*付息频率（个月/次）" data-name="ssInterestFrequency" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*融资人" data-name="ssDebtor" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*融资人组织机构（社会信用）代码" data-name="ssOrganCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="外部评级机构名称及对融资人评级结果" data-name="ssRateAgencyIss" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*融资人类型（按规模划分）" data-name="ssDebtorTypeScale" data-width="150" data-dict="subm_instituteTypeTech"></k-grid-column>
        <k-grid-column data-align="left" data-header="*融资人类型（按技术领域划分）" data-name="ssDebtorTypeTech" data-width="150" data-dict="subm_isuOrgTypTchno"></k-grid-column>
        <k-grid-column data-align="left" data-header="*融资人类型（按经济类型划分）" data-name="ssDebtorTypeEconomic" data-width="150" data-dict="subm_isuOrgTypEcn"></k-grid-column>
        <k-grid-column data-align="left" data-header="*融资项目" data-name="ssProject" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*融资人所属行业" data-name="ssIndustryDebtor" data-width="150" data-dict="subm_isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-align="left" data-header="*融资项目所属行业" data-name="ssIndustryProject" data-width="150" data-dict="subm_isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-align="left" data-header="*项目是否属于重点监控行业和领域" data-name="ssMonitoryIndustry" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="重点监控行业和领域类别" data-name="ssMonitoryIndustryType" data-width="150" data-dict="subm_keyMntIdtTyp"></k-grid-column>
        <k-grid-column data-align="left" data-header="重点监控行业和领域类别说明" data-name="ssDetailsMonitoryType" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="对应资产外部评级" data-name="ssInternalAssetRate" data-width="150" data-dict="subm_mainRating"></k-grid-column>
        <k-grid-column data-align="left" data-header="担保方式" data-name="ssGuaranteeMethod" data-width="150" data-dict="subm_grntWay"></k-grid-column>
        <k-grid-column data-align="left" data-header="抵质押物类型" data-name="ssPledgeType" data-width="150" data-dict="subm_plgTyp"></k-grid-column>
        <k-grid-column data-align="right" data-header="抵质押物价值（元）" data-name="ssPledgeValue" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="担保性质" data-name="ssGuaranteeType" data-width="150" data-dict="subm_grntChr"></k-grid-column>
        <k-grid-column data-align="left" data-header="担保人与融资人关系" data-name="ssGuarantorType" data-width="150" data-dict="subm_grntLvrgRel"></k-grid-column>
        <k-grid-column data-align="left" data-header="担保情况说明" data-name="ssDetailsGuarantee" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否为债转股" data-name="ssDebtEquitySwap" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        </k-grid-column>
        <!--15 另类资产-->
        <k-grid-column data-header="另类资产" data-align="center">
        <k-grid-column data-align="left" data-header="*所属国家或地区" data-name="ooCountry" data-width="150" data-dict="tr_iss_country"></k-grid-column>
        <k-grid-column data-align="left" data-header="*名称" data-name="ooName" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="*起息日" data-name="ooValueDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*到期日" data-name="ooMaturityDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*资产价值" data-name="ooAssetValue" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*资产收益率%" data-name="ooAssetReturn" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        </k-grid-column>
        <!--16 公募基金/私募基金-->
        <k-grid-column data-header="公募基金/私募基金" data-align="center">
        <k-grid-column data-align="left" data-header="*基金代码" data-name="ppFundCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*基金名称" data-name="ppFundName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*行业" data-name="ppIndustry" data-width="150" data-dict="subm_isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-align="left" data-header="*登记备案机构" data-name="ppRegistAgency" data-width="150" data-dict="subm_regTrstOrg"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否为固定收益类" data-name="ppFixedIncome" data-width="150"  data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否属于政府投资基金" data-name="ppGovernInvestFund" data-width="150"  data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="政府投资基金投向" data-name="ppDirectGovernFund" data-width="150" data-dict="subm_actual_invest_dir_gov_fund"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否由金融资产投资公司发行" data-name="ppIssuedAssetCompany" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="基金公司名称" data-name="ppTaName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*基金管理机构名称" data-name="ppManagerFundName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*基金托管机构名称" data-name="ppCustodianFundName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资阶段" data-name="ppInvestStage" data-width="150"  data-dict="subm_invest_stage"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资企业类型（按规模划分）" data-name="ppEnterTypeScale" data-width="150" data-dict="subm_instituteTypeTech"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资企业类型（按技术领域划分）" data-name="ppEnterTypeTech" data-width="150" data-dict="subm_isuOrgTypTchno"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资企业类型（按经济类型划分）" data-name="ppEnterTypeEconomic" data-width="150" data-dict="subm_isuOrgTypEcn"></k-grid-column>
        <k-grid-column data-align="left" data-header="*基金投资资产" data-name="ppInvestAssets" data-width="150"></k-grid-column>
        </k-grid-column>
        <!--17 委外投资——协议方式-->
        <k-grid-column data-header="委外投资——协议方式" data-align="center">
        <k-grid-column data-align="left" data-header="委外投资协议名称" data-name="qqOutAgreementName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="委外投资协议编号" data-name="qqOutAgreementCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="受托人" data-name="qqTrustee" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="实际管理人" data-name="qqActualManager" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="托管人" data-name="qqCustodian" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="委托投资金额" data-name="qqOutAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="资金实际投向" data-name="qqActualDirection" data-width="150" data-dict="subm_actual_invest_dir_fund"></k-grid-column>
        <k-grid-column data-align="left" data-header="资金运用方式" data-name="qqDetailsInvest" data-width="150" ></k-grid-column>
        <k-grid-column data-align="left" data-header="资金运用行业" data-name="qqIndustryInvest" data-width="150" data-dict="subm_isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资运作起始日期" data-name="qqValueDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资运作终止日期" data-name="qqMaturityDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="委外投资属性" data-name="qqOutType" data-width="150"  data-dict="subm_outsourc_invest"></k-grid-column>
        <k-grid-column data-align="right" data-header="是否有预期收益率" data-name="qqExpectedReturn" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="right" data-header="预期最高收益率" data-name="qqMaxExpectedReturn" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="预期最低收益率" data-name="qqMinExpectedReturn" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="管理费率%" data-name="qqManagerFeeRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="托管费率%" data-name="qqCustodianFeeRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="交易相关合计费率%" data-name="qqTransCostRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="中介服务机构合计费率%" data-name="qqInterFeeRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="其他合计费率%" data-name="qqOtherExpensesRate" data-width="150"></k-grid-column>
        </k-grid-column>
        <!--18 其他（负债）类资产-->
        <k-grid-column data-header="其他（负债）类资产" data-align="center">
        <k-grid-column data-align="left" data-header="*所属国家或地区" data-name="rrCountry"  data-dict="tr_iss_country" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*名称" data-name="rrName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="期限（天）" data-name="rrTermMaturity" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="*负债规模" data-name="rrLiabilityAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="利率%" data-name="rrInterestRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="*是否为同业借款" data-name="rrCashBorrow" data-dict="subm_isTrue" data-width="150"></k-grid-column>
        </k-grid-column>
        <k-grid-column data-header="登记日期" data-name="registerDate"  data-export="false" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="备注" data-name="details" data-width="150"></k-grid-column>
        <k-grid-column data-header="数据变更类型" data-name="dataChangeType" data-export="false" data-dict="data_change_type" data-width="150"></k-grid-column>
        <k-grid-column data-header="登记流水号" data-name="registerSerno" data-export="false" data-width="250"></k-grid-column>
        <!-- <k-grid-column data-header="理论报送起始日期" data-name="theoryReportStartDate"   data-export="false" data-width="100"></k-grid-column>
        <k-grid-column data-header="理论报送截止日期" data-name="theoryReportEndDate"   data-export="false" data-width="100"></k-grid-column> -->
        <k-grid-column data-header="新增日期" data-name="createDate"  data-export="false"  data-width="100"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改资产要素登记管理" data-functype="POPUP" data-size="mini"
             v-show="scope.row.row.registerStatus != '5'"     data-target="editAssetDebtRegisterInfoPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="AssetDebtRegisterInfo.deleteAssetDebtRegisterInfo" data-size="mini"
             v-show="scope.row.row.registerStatus != '5'"     data-type="danger" data-target="assetDebtRegisterInfoGrid" :data-confirm="true" data-descript="删除资产要素登记管理">
            删除
          </k-btn>
          <!--<k-btn data-functype="PAGE" data-size="mini" class="btn-custom-plain" :data-model="scope.row.row.id"
                 @click="popupEdit(scope.row.row)"  data-descript="资产要素登记管理错误详情">
            错误详情
          </k-btn>-->
        </template>
      </k-grid>
    </div>

    <!--    添加资产要素登记管理弹出框   -->
    <k-popup ref="addAssetDebtRegisterInfoPopup" data-title="新增">
      <k-form ref="addAssetDebtRegisterInfoForm" :data-col="2" isFormBodyScreen>
       <k-form-item label="资产要素分类" :data-col="2" >
         <k-field-select v-model="formData.AssetDebtRegisterType" :data-allowblank="false" data-dict="AssetDebtRegisterType" @data-on-change="dataOnChange"/>
       </k-form-item>
        <!-- 公共信息-->
        <k-form-item label="行内资产/负债编码"  v-show="this.formData.AssetDebtRegisterType == '1' || this.formData.AssetDebtRegisterType == '2' || this.formData.AssetDebtRegisterType == '3' || this.formData.AssetDebtRegisterType == '4' || this.formData.AssetDebtRegisterType == '6' || this.formData.AssetDebtRegisterType == '12' || this.formData.AssetDebtRegisterType == '14' || this.formData.AssetDebtRegisterType == '16' ">
          <k-field-text v-model="formData.assetCode" :data-allowblank="false" :data-max-length="40"/>
        </k-form-item>
        <k-form-item label="发行机构代码"  v-show="this.formData.AssetDebtRegisterType == '1' || this.formData.AssetDebtRegisterType == '2' || this.formData.AssetDebtRegisterType == '3' || this.formData.AssetDebtRegisterType == '4' || this.formData.AssetDebtRegisterType == '6' || this.formData.AssetDebtRegisterType == '12' || this.formData.AssetDebtRegisterType == '14' || this.formData.AssetDebtRegisterType == '16' ">
          <k-field-text v-model="formData.bankCode" :data-allowblank="false" :data-max-length="6"/>
        </k-form-item>
        <k-form-item label="交易流通场所"  v-show="this.formData.AssetDebtRegisterType == '1' || this.formData.AssetDebtRegisterType == '2' || this.formData.AssetDebtRegisterType == '3' || this.formData.AssetDebtRegisterType == '4' || this.formData.AssetDebtRegisterType == '6' || this.formData.AssetDebtRegisterType == '12' || this.formData.AssetDebtRegisterType == '14' || this.formData.AssetDebtRegisterType == '16' ">
          <k-field-select v-model="formData.tradeVenue" :data-allowblank="false" data-dict="subm_tacdingPlace"/>
        </k-form-item>
        <k-form-item label="币种"  v-show="this.formData.AssetDebtRegisterType == '1' || this.formData.AssetDebtRegisterType == '2' || this.formData.AssetDebtRegisterType == '3' || this.formData.AssetDebtRegisterType == '4' || this.formData.AssetDebtRegisterType == '6' || this.formData.AssetDebtRegisterType == '12' || this.formData.AssetDebtRegisterType == '14' || this.formData.AssetDebtRegisterType == '16' ">
          <k-field-select v-model="formData.cur" :data-allowblank="false" data-dict="tr_cur" :data-max-length="3"/>
        </k-form-item>
        <k-form-item label="资产/负债类别"  v-show="this.formData.AssetDebtRegisterType == '1' || this.formData.AssetDebtRegisterType == '2' || this.formData.AssetDebtRegisterType == '3' || this.formData.AssetDebtRegisterType == '4' || this.formData.AssetDebtRegisterType == '6' || this.formData.AssetDebtRegisterType == '12' || this.formData.AssetDebtRegisterType == '14' || this.formData.AssetDebtRegisterType == '16' ">
          <k-field-select v-model="formData.assDebtType" :data-allowblank="false" :data-data="formData.assDebtTypeDict"  data-value-field="VALUE" data-display-field="TEXT"/>
        </k-form-item>
        <k-form-item label="备注"  v-show="this.formData.AssetDebtRegisterType == '1' || this.formData.AssetDebtRegisterType == '2' || this.formData.AssetDebtRegisterType == '3' || this.formData.AssetDebtRegisterType == '4' || this.formData.AssetDebtRegisterType == '6' || this.formData.AssetDebtRegisterType == '12' || this.formData.AssetDebtRegisterType == '14' || this.formData.AssetDebtRegisterType == '16' ">
          <k-field-text v-model="formData.details" :data-max-length="256"/>
        </k-form-item>

        <!--1 本行/他行存款||大额存单-->
        <k-form-item label="资金存入银行"  v-show="this.formData.AssetDebtRegisterType == 1">
          <k-field-text v-model="formData.bbDepositBank" :data-max-length="200"/>
        </k-form-item>
         <k-form-item label="存款账号"  v-show="this.formData.AssetDebtRegisterType == 1">
          <k-field-text v-model="formData.bbAccountNo" :data-max-length="60"/>
        </k-form-item>
        <k-form-item label="存款金额"  v-show="this.formData.AssetDebtRegisterType == 1">
          <k-field-text v-model="formData.bbDepositAmt" :data-allowblank="formData.bbDepositAmtAllowblank" data-validate-type="money" data-type="money" data-integer-length="13" data-digits="2"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>
        <k-form-item label="起息日"  v-show="this.formData.AssetDebtRegisterType == 1">
          <k-field-date v-model="formData.bbValueDate" :data-allowblank="formData.bbValueDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="到期日"  v-show="this.formData.AssetDebtRegisterType == 1">
          <k-field-date v-model="formData.bbMaturityDate" :data-allowblank="formData.bbMaturityDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="年利率%"  v-show="this.formData.AssetDebtRegisterType == 1">
          <k-field-text v-model="formData.bbAnnualRate" :data-allowblank="formData.bbAnnualRateAllowblank" data-validate-type="number" data-type="number" data-integer-length="1" data-digits="7" data-min-value="0" />
        </k-form-item>
        <k-form-item label="计息基础"  v-show="this.formData.AssetDebtRegisterType == 1">
          <k-field-select v-model="formData.bbInterestBasis" :data-allowblank="formData.bbInterestBasisAllowblank" data-dict="intr_base"/>
        </k-form-item>
        <k-form-item label="存款类型"  v-show="this.formData.AssetDebtRegisterType == 1">
          <k-field-select v-model="formData.bbDepositType" data-dict="deposit_type"/>
        </k-form-item>
        <k-form-item label="结构性存款挂钩标的类别"  v-show="this.formData.AssetDebtRegisterType == 1">
          <k-field-select v-model="formData.bbStructDepositType" data-dict="stru_deposit_type"/>
        </k-form-item>
        <k-form-item label="结构性存款挂钩标的"  v-show="this.formData.AssetDebtRegisterType == 1">
          <k-field-text v-model="formData.bbStructDeposit" :data-max-length="256"/>
        </k-form-item>

        <!--2 债券类资产/理财直接融资工具/同业存单-->
        <k-form-item label="代码"  v-show="this.formData.AssetDebtRegisterType == 2">
          <k-field-text v-model="formData.ccIdentCode" :data-allowblank="formData.ccIdentCodeAllowblank" :data-max-length="15"/>
        </k-form-item>
        <k-form-item label="名称"  v-show="this.formData.AssetDebtRegisterType == 2">
          <k-field-text v-model="formData.ccName" :data-allowblank="formData.ccNameAllowblank" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="具体类别"  v-show="this.formData.AssetDebtRegisterType == 2">
          <k-field-select v-model="formData.ccSpecificBondType"  data-dict="spcType"/>
        </k-form-item>
        <k-form-item label="发行方式"  v-show="this.formData.AssetDebtRegisterType == 2">
          <k-field-select v-model="formData.ccIssModeBond" :data-allowblank="formData.ccIssModeBondAllowblank" data-dict="iss_mode_bond"/>
        </k-form-item>
        <k-form-item label="主体评级"  v-show="this.formData.AssetDebtRegisterType == 2">
          <k-field-select v-model="formData.ccIssRatePart" :data-allowblank="formData.ccIssRatePartAllowblank" data-dict="mainRating"/>
        </k-form-item>
        <k-form-item label="发行机构类型(按规模划分)"  v-show="this.formData.AssetDebtRegisterType == 2">
          <k-field-select v-model="formData.ccInstituteTypeScale" :data-allowblank="formData.ccInstituteTypeScaleAllowblank" data-dict="instituteTypeTech"/>
        </k-form-item>
        <k-form-item label="发行机构类型(按技术领域划分)"  v-show="this.formData.AssetDebtRegisterType == 2">
          <k-field-select v-model="formData.ccInstituteTypeTech" :data-allowblank="formData.ccInstituteTypeTechAllowblank" data-dict="isuOrgTypTchno"/>
        </k-form-item>
        <k-form-item label="发行机构类型(按经济类型划分)"  v-show="this.formData.AssetDebtRegisterType == 2">
          <k-field-select v-model="formData.ccInstituteTypeEconomic" :data-allowblank="formData.ccInstituteTypeEconomicAllowblank" data-dict="isuOrgTypEcn"/>
        </k-form-item>
        <k-form-item label="发行机构所属行业"  v-show="this.formData.AssetDebtRegisterType == 2">
          <k-field-select v-model="formData.ccIndustryIssuer" :data-allowblank="formData.ccIndustryIssuerAllowblank"  data-dict="isuOrgBlgIdt"/>
        </k-form-item>
        <k-form-item label="登记托管机构"  v-show="this.formData.AssetDebtRegisterType == 2">
          <k-field-select v-model="formData.ccRegistDeposit" :data-allowblank="formData.ccRegistDepositAllowblank" data-dict="regTrstOrg"/>
        </k-form-item>
        <k-form-item label="登记托管机构说明"  v-show="this.formData.AssetDebtRegisterType == 2">
          <k-field-text v-model="formData.ccDetailsRegistDeposit" :data-max-length="256"/>
        </k-form-item>

        <!--3 拆放同业及买入返售/同业拆入及卖出回购-->
        <k-form-item label="起息日"  v-show="this.formData.AssetDebtRegisterType == 3">
          <k-field-date v-model="formData.ddValueDate" :data-allowblank="formData.ddValueDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="到期日"  v-show="this.formData.AssetDebtRegisterType == 3">
          <k-field-date v-model="formData.ddMaturityDate" :data-allowblank="formData.ddMaturityDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="对手方"  v-show="this.formData.AssetDebtRegisterType == 3">
          <k-field-text v-model="formData.ddCounterparty" :data-allowblank="formData.ddCounterpartyAllowblank" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="对手方类型"  v-show="this.formData.AssetDebtRegisterType == 3">
          <k-field-select v-model="formData.ddCounterpartyType" :data-allowblank="formData.ddCounterpartyTypeAllowblank" data-dict="counterparty_type"/>
        </k-form-item>
        <k-form-item label="年利率%"  v-show="this.formData.AssetDebtRegisterType == 3">
          <k-field-text v-model="formData.ddAnnalInterestRate" :data-allowblank="formData.ddAnnalInterestRateAllowblank" data-validate-type="money" data-type="money" data-integer-length="1" data-digits="7" data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>
        <k-form-item label="计息基础"  v-show="this.formData.AssetDebtRegisterType == 3">
          <k-field-select v-model="formData.ddInterestBasis" :data-allowblank="formData.ddInterestBasisAllowblank" data-dict="intr_base"/>
        </k-form-item>
        <k-form-item label="回购标的类别"  v-show="this.formData.AssetDebtRegisterType == 3">
          <k-field-select v-model="formData.ddCollateralType" :data-allowblank="formData.ddCollateralTypeAllowblank" data-dict="interest_basis"/>
        </k-form-item>
        <k-form-item label="回购标的金额"  v-show="this.formData.AssetDebtRegisterType == 3">
          <k-field-text v-model="formData.ddCollateralValue" data-validate-type="money" data-type="money"  data-integer-length="13" data-digits="2" data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>

        <!--4 非标准化债权类资产/新增可投资资产-->
        <k-form-item label="收/受权益类型"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeOwnershipType" data-dict="incBenRitType"/>
        </k-form-item>
        <k-form-item label="是否属于买入反售"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeBuyback"  :data-allowblank="formData.eeBuybackAllowblank" data-dict="isTrue"/>
        </k-form-item>
        <k-form-item label="名称"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeName" :data-allowblank="formData.eeNameAllowblank" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="资产代码"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeAssetCode" :data-allowblank="formData.eeAssetCodeAllowblank" :data-max-length="10"/>
        </k-form-item>
        <k-form-item label="金额"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeAmt" :data-allowblank="formData.eeAmtAllowblank" data-validate-type="money" data-type="money" data-integer-length="13" data-digits="2" data-min-value="0"   data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>
        <k-form-item label="份额面值"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeUnitParValue" :data-allowblank="formData.eeUnitParValueAllowblank" data-validate-type="money" data-type="money" data-integer-length="13" data-digits="4" data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>
        <k-form-item label="起息日"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-date v-model="formData.eeValueDate" :data-allowblank="formData.eeValueDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="到期日"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-date v-model="formData.eeMaturityDate" :data-allowblank="formData.eeMaturityDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="法定到期日"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-date v-model="formData.eeStatutoryMaturityDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="是否有预期收益率"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeExpectedReturn" :data-allowblank="formData.eeExpectedReturnAllowblank" data-dict="isTrue"/>
        </k-form-item>
        <k-form-item label="项目收益率(利率)%"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeProjectAnnaulReturn" data-validate-type="number" data-integer-length="3" data-digits="5" data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>
        <k-form-item label="计息类型"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeCouponType" data-dict="interest_type"/>
        </k-form-item>
        <k-form-item label="规则付息标识"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeRegualrInterestPay" data-dict="isTrue"/>
        </k-form-item>
        <k-form-item label="付息频率(个月/次)"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeInterestPayFrequency" :data-allowblank="formData.eeInterestPayFrequencyAllowblank" data-validate-type="number"  data-integer-length="2" data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>
        <k-form-item label="利息分布方式"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeCouponAllocationType" data-dict="intrAlcMth"/>
        </k-form-item>
        <k-form-item label="还本付息情况说明"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeDetailPrincInterest" :data-allowblank="formData.eeDetailPrincInterestAllowblank" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="计息基础"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeInterestBasis" :data-allowblank="formData.eeInterestBasisAllowblank" data-dict="intr_base"/>
        </k-form-item>
        <k-form-item label="基准利率种类"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeBenchRateType" data-dict="bchmRatTyp"/>
        </k-form-item>
        <k-form-item label="是否有浮动因子"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeFloatFactor" data-dict="isTrue"/>
        </k-form-item>
        <k-form-item label="浮动因子%"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeFloatRate" data-validate-type="number" data-integer-length="3" data-digits="5" />
        </k-form-item>
        <k-form-item label="利差（BP）%"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeYieldSpreadBp" data-validate-type="number"  data-integer-length="3" data-digits="5" />
        </k-form-item>
        <k-form-item label="结构档次"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeStructGrade" data-dict="strcGrd"/>
        </k-form-item>
        <k-form-item label="还本方式"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eePrincPaymentType" data-dict="payPrcpMth"/>
        </k-form-item>
        <k-form-item label="分期还本条款标识"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeInstallRepayType" data-dict="insPayPrcpF"/>
        </k-form-item>
        <k-form-item label="基础资产类型"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeBaseAssetType"  :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="超额收益分配比例（%）"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eePercentExcInAllot" data-validate-type="number"  data-type="number"  data-integer-length="3" data-digits="5" />
        </k-form-item>
        <k-form-item label="融资人"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeDebtor" :data-allowblank="formData.eeDebtorAllowblank" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="融资人内部信用评级"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeDeptorRate" :data-allowblank="formData.eeDeptorRateAllowblank" data-dict="subm_mainRating"/>
        </k-form-item>
        <k-form-item label="外部评级机构名称及对融资人评级结果"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeRateAgencyIss" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="融资人类型(按规模划分)"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeDebtorTypeScale" :data-allowblank="formData.eeDebtorTypeScaleAllowblank" data-dict="instituteTypeTech"/>
        </k-form-item>
        <k-form-item label="融资人类型(按技术领域划分)"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeDebtorTypeTech" :data-allowblank="formData.eeDebtorTypeTechAllowblank" data-dict="isuOrgTypTchno"/>
        </k-form-item>
        <k-form-item label="融资人类型(按经济类型划分)"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeDebtorTypeEconomic" :data-allowblank="formData.eeDebtorTypeEconomicAllowblank" data-dict="isuOrgTypEcn"/>
        </k-form-item>
        <k-form-item label="融资项目"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeProject" :data-allowblank="formData.eeProjectAllowblank" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="融资人所属行业"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeIndustryDebtor" :data-allowblank="formData.eeIndustryDebtorAllowblank" data-dict="isuOrgBlgIdt"/>
        </k-form-item>
        <k-form-item label="项目是否属于重点监控行业和领域"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeMonitorIndusType" :data-allowblank="formData.eeMonitorIndusTypeAllowblank" data-dict="isTrue"/>
        </k-form-item>
        <k-form-item label="重点监控行业和领域类别"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeMonitorIndustryType" data-dict="keyMntIdtTyp"/>
        </k-form-item>
        <k-form-item label="重点监控行业和领域类别说明"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeDetailsMonitoryType" :data-max-length="256"/>
        </k-form-item>
        <k-form-item label="担保方式"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeGuaranteeMethod" data-dict="grntWay"/>
        </k-form-item>
        <k-form-item label="抵质押物类型"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eePledgeType" data-dict="plgTyp"/>
        </k-form-item>
        <k-form-item label="抵质押物价值（元）"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eePledgeValue" data-validate-type="money"  data-type="money" data-integer-length="13" data-digits="2" />
        </k-form-item>
        <k-form-item label="担保性质"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeGuaranteeType" data-dict="grntChr"/>
        </k-form-item>
        <k-form-item label="担保人与融资人关系"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeGuarantorType" data-dict="grntLvrgRel"/>
        </k-form-item>
        <k-form-item label="担保情况说明"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeDetailGuaranteeStatus" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="融资人主体评级"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeDebtorRate" :data-allowblank="formData.eeDebtorRateAllowblank" data-dict="mainRating"/>
        </k-form-item>
        <k-form-item label="资产内部评级"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeInterAssetRate" :data-allowblank="formData.eeInterAssetRateAllowblank" data-dict="mainRating"/>
        </k-form-item>
        <k-form-item label="资产外部评级"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeOutAssetRate" :data-allowblank="formData.eeOutAssetRateAllowblank" data-dict="mainRating"/>
        </k-form-item>
        <k-form-item label="含权类型"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeOptionType"  data-dict="embOptTyp"/>
        </k-form-item>
        <k-form-item label="行权方式"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeExerciseDateType" data-dict="xcsRitMth"/>
        </k-form-item>
        <k-form-item label="固定行权日"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-date v-model="formData.eeFixedExerciseDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="首次行权日期"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-date v-model="formData.eeFirstExerciseDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="行权周期"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeExercisePeriod" data-validate-type="number"  data-integer-length="5" data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>
        <k-form-item label="行权价格"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeExercisePrice"  data-validate-type="money"  data-type="money"  data-integer-length="13" data-digits="4"/>
        </k-form-item>
        <k-form-item label="永续条款类型"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eePerpetualType" data-dict="perpTyp"/>
        </k-form-item>
        <k-form-item label="利息递延条款类型"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeDeferreInterestType" data-dict="intrPpnTyp"/>
        </k-form-item>
        <k-form-item label="递延利息是否计息"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeInterestDeferred" data-dict="isTrue"/>
        </k-form-item>
        <k-form-item label="首次重定价日期"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-date v-model="formData.eeFirstRepriceDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="重定价周期"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeRepricePeriod"  :data-max-length="5" data-validate-type="number"  data-type="number" data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>
        <k-form-item label="部分赎回标识"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eePartialRedemption" data-dict="isTrue"/>
        </k-form-item>
        <k-form-item label="部分赎回比例%"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eePartialRedemptionRate"  data-validate-type="number"  data-integer-length="3" data-digits="5"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>
        <k-form-item label="选择权"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeOptionRight" :data-allowblank="formData.eeOptionRightAllowblank"  :data-max-length="60"/>
        </k-form-item>
        <k-form-item label="行权条件说明"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeDetailsExerciseTerm" :data-allowblank="formData.eeDetailsExerciseTermAllowblank"  :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="融资人所属地区"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeRegionDebtor" :data-allowblank="formData.eeRegionDebtorAllowblank" data-dict="asset_sale_area" data-dict-type="1"/>
        </k-form-item>
         <k-form-item label="融资人组织机构（社会信用）代码"  v-show="this.formData.AssetDebtRegisterType == 4">
            <k-field-text v-model="formData.eeOrganizationCode" :data-allowblank="formData.eeOrganizationCodeAllowblank" :data-max-length="18"/>
          </k-form-item>
        <k-form-item label="增信机构代码"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeEnhanceInstituteCode" :data-max-length="18"/>
        </k-form-item>
        <k-form-item label="增信机构名称"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeEnhanceInstituteName" :data-max-length="18"/>
        </k-form-item>
        <k-form-item label="融资总费率"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-text v-model="formData.eeTotalFeeRate" :data-allowblank="formData.eeTotalFeeRateAllowblank"  data-validate-type="number"  data-integer-length="3" data-digits="5"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>
        <k-form-item label="融资项目所属行业"  v-show="this.formData.AssetDebtRegisterType == 4">
          <k-field-select v-model="formData.eeIndustryProject" :data-allowblank="formData.eeIndustryProjectAllowblank" data-dict="isuOrgBlgIdt"/>
        </k-form-item>

        <!--6 权益类资产-->
        <k-form-item label="股票代码"  v-show="this.formData.AssetDebtRegisterType == 6">
          <k-field-text v-model="formData.ggStockCode" :data-max-length="20" />
        </k-form-item>
        <k-form-item label="股票/企业名称"  v-show="this.formData.AssetDebtRegisterType == 6">
          <k-field-text v-model="formData.ggName" :data-allowblank="formData.ggNameAllowblank" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="股票类型"  v-show="this.formData.AssetDebtRegisterType == 6">
          <k-field-select v-model="formData.ggStockType" data-dict="subm_stock_type" />
        </k-form-item>
        <k-form-item label="行业"  v-show="this.formData.AssetDebtRegisterType == 6">
          <k-field-select v-model="formData.ggIndustry" :data-allowblank="formData.ggIndustryAllowblank" data-dict="hang_ye"/>
        </k-form-item>
        <k-form-item label="投资阶段"  v-show="this.formData.AssetDebtRegisterType == 6">
          <k-field-select v-model="formData.ggInvestStage" data-dict="subm_invest_stage" :data-allowblank="formData.ggInvestStageAllowblank"/>
        </k-form-item>
        <k-form-item label="股权退出安排"  v-show="this.formData.AssetDebtRegisterType == 6">
          <k-field-date v-model="formData.ggEquityOutDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="formData.ggEquityOutDateAllowblank"/>
        </k-form-item>
        <k-form-item label="企业类型(按规模划分)"  v-show="this.formData.AssetDebtRegisterType == 6">
          <k-field-select v-model="formData.ggEnterTypeScale" :data-allowblank="formData.ggEnterTypeScaleAllowblank" data-dict="subm_instituteTypeTech"/>
        </k-form-item>
        <k-form-item label="企业类型(按技术领域划分)"  v-show="this.formData.AssetDebtRegisterType == 6">
          <k-field-select v-model="formData.ggEnterTypeTech" :data-allowblank="formData.ggEnterTypeTechAllowblank" data-dict="subm_isuOrgTypTchno"/>
        </k-form-item>
        <k-form-item label="企业类型(按经济类型划分)"  v-show="this.formData.AssetDebtRegisterType == 6">
          <k-field-select v-model="formData.ggEnterTypeEconomic" :data-allowblank="formData.ggEnterTypeEconomicAllowblank" data-dict="subm_isuOrgTypEcn"/>
        </k-form-item>
        <k-form-item label="是否质押融资"  v-show="this.formData.AssetDebtRegisterType == 6">
          <k-field-select v-model="formData.ggPledgedFinace" :data-allowblank="formData.ggPledgedFinaceAllowblank" data-dict="subm_isTrue"/>
        </k-form-item>
        <k-form-item label="是否为债转股"  v-show="this.formData.AssetDebtRegisterType == 6">
          <k-field-select v-model="formData.ggDebtEquitySwap" data-dict="subm_isTrue"/>
        </k-form-item>

        <!--12 资产管理产品-->
        <k-form-item label="资管计划名称"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-text v-model="formData.mmManagePlanName" :data-allowblank="formData.mmManagePlanNameAllowblank" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="是否为银行理财产品"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-select v-model="formData.mmManageProduct" :data-allowblank="formData.mmManageProductAllowblank"  data-dict="isTrue"/>
        </k-form-item>
        <k-form-item label="理财产品登记编码"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-text v-model="formData.mmProductCode" :data-allowblank="formData.mmProductCodeAllowblank"  :data-max-length="15"/>
        </k-form-item>
        <k-form-item label="资管计划发起人机构编码"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-text v-model="formData.mmPlanIssuerCode" :data-allowblank="formData.mmPlanIssuerCodeAllowblank"  :data-max-length="32"/>
        </k-form-item>
        <k-form-item label="是否由金融资产投资公司发行"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-select v-model="formData.mmIssuedAssetCompany"  data-dict="isTrue"/>
        </k-form-item>
        <k-form-item label="资管计划登记编码"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-text v-model="formData.mmAssetPlanRgCode" :data-allowblank="formData.mmAssetPlanRgCodeAllowblank" :data-max-length="256"/>
        </k-form-item>
        <k-form-item label="管理人"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-text v-model="formData.mmManager" :data-allowblank="formData.mmManagerAllowblank" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="托管人"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-text v-model="formData.mmCustodian" :data-allowblank="formData.mmCustodianAllowblank" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="金额"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-text v-model="formData.mmAmt" :data-allowblank="formData.mmAmtAllowblank"   data-validate-type="money" data-type="money"  data-min-value="0" data-integer-length="13" data-digits="2"/>
        </k-form-item>
        <k-form-item label="资金实际投向"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-select v-model="formData.mmActualDirect" :data-allowblank="formData.mmActualDirectAllowblank" data-dict="actual_invest_dir_fund"/>
        </k-form-item>
        <k-form-item label="资金运用方式"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-text v-model="formData.mmDetailsInvest" :data-allowblank="formData.mmDetailsInvestAllowblank" :data-max-length="300"/>
        </k-form-item>
        <k-form-item label="资金运用行业"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-select v-model="formData.mmIndustryInvest" :data-allowblank="formData.mmIndustryInvestAllowblank" data-dict="isuOrgBlgIdt"/>
        </k-form-item>
        <k-form-item label="资管计划成立日期"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-date v-model="formData.mmPlanStartDate" :data-allowblank="formData.mmPlanStartDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="资管计划终止日期"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-date v-model="formData.mmPlanMaturityDate" :data-allowblank="formData.mmPlanMaturityDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="资管计划属性"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-select v-model="formData.mmPlanType" :data-allowblank="formData.mmPlanTypeAllowblank" data-dict="astMngPlanPrpt"/>
        </k-form-item>
        <k-form-item label="是否有预期收益率"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-select v-model="formData.mmExpectedReturn" :data-allowblank="formData.mmExpectedReturnAllowblank" data-dict="isTrue"/>
        </k-form-item>
        <k-form-item label="预期最高收益率%"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-text v-model="formData.mmMaxExpectedReturn"  data-validate-type="number" data-type="number"  data-integer-length="3" data-digits="5"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>
        <k-form-item label="预期最低收益率%"   v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-text v-model="formData.mmMinExpectedReturn"  data-validate-type="number" data-type="number"   data-integer-length="3" data-digits="5"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>
        <k-form-item label="购买结构"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-select v-model="formData.mmInvestStructure" :data-allowblank="formData.mmInvestStructureAllowblank" data-dict="buyStrc"/>
        </k-form-item>
        <k-form-item label="管理方式"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-select v-model="formData.mmManagerType" :data-allowblank="formData.mmManagerTypeAllowblank" data-dict="mngMth"/>
        </k-form-item>
        <k-form-item label="管理费率%"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-text v-model="formData.mmManagerFeeRate" :data-allowblank="formData.mmManagerFeeRateAllowblank" data-validate-type="number"  data-type="number"  data-integer-length="3" data-digits="5"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>
        <k-form-item label="托管费率%"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-text v-model="formData.mmCustodianFeeRate" :data-allowblank="formData.mmCustodianFeeRateAllowblank" data-validate-type="number" data-type="number"   data-integer-length="3" data-digits="5"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>
        <k-form-item label="交易相关合计费率%"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-text v-model="formData.mmTransCostRate" :data-allowblank="formData.mmTransCostRateAllowblank"   data-validate-type="number" data-type="number"   data-integer-length="3" data-digits="5"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>
        <k-form-item label="中介服务机构合计费率%"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-text v-model="formData.mmInterFeeRate" :data-allowblank="formData.mmInterFeeRateAllowblank"   data-validate-type="number" data-type="number"   data-integer-length="3" data-digits="5"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>
        <k-form-item label="其他合计费率%"  v-show="this.formData.AssetDebtRegisterType == 12">
          <k-field-text v-model="formData.mmOtherExpenseRate" :data-allowblank="formData.mmOtherExpenseRateAllowblank"   data-validate-type="number" data-type="number"   data-integer-length="3" data-digits="5"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>

        <!--14 其他非标准化债权类/权益类/代客境外理财投资QDII/新增可投资资产-->
        <k-form-item label="名称"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-text v-model="formData.ssName" :data-allowblank="formData.ssNameAllowblank"  :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="行内资产类别"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-select v-model="formData.ssAssetType" :data-allowblank="formData.ssAssetTypeAllowblank" data-dict="asset_type"/>
        </k-form-item>
        <k-form-item label="行内资产类别说明"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-text v-model="formData.ssDetailsAssetType" :data-max-length="256"/>
        </k-form-item>
        <k-form-item label="金额"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-text v-model="formData.ssAmt"  :data-allowblank="formData.ssAmtAllowblank" data-validate-type="money"   data-integer-length="13" data-digits="2"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>
        <k-form-item label="起息日"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-date v-model="formData.ssValueDate" :data-allowblank="formData.ssValueDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="到期日"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-date v-model="formData.ssMaturityDate" :data-allowblank="formData.ssMaturityDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="所属国家或地区"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-select v-model="formData.ssCountry" :data-allowblank="formData.ssCountryAllowblank" data-dict="tr_iss_country"/>
        </k-form-item>
        <k-form-item label="是否有预期收益率"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-select v-model="formData.ssExpectedReturn" :data-allowblank="formData.ssExpectedReturnAllowblank" data-dict="isTrue"/>
        </k-form-item>
        <k-form-item label="项目收益率(利率)%"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-text v-model="formData.ssAnnualReturn" data-validate-type="number" data-type="number"   data-integer-length="3" data-digits="5"/>
        </k-form-item>
        <k-form-item label="付息频率(个月/次)"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-text v-model="formData.ssInterestFrequency" :data-allowblank="formData.ssInterestFrequencyAllowblank" data-validate-type="number"  data-type="number" :data-max-length="2" data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
        </k-form-item>
        <k-form-item label="融资人"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-text v-model="formData.ssDebtor" :data-allowblank="formData.ssDebtorAllowblank" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="融资人组织机构(社会信用)代码"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-text v-model="formData.ssOrganCode"  :data-allowblank="formData.ssOrganCodeAllowblank" :data-max-length="18"/>
        </k-form-item>
        <k-form-item label="外部评级机构名称及对融资人评级结果"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-text v-model="formData.ssRateAgencyIss" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="融资人类型(按规模划分)"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-select v-model="formData.ssDebtorTypeScale"  :data-allowblank="formData.ssDebtorTypeScaleAllowblank" data-dict="instituteTypeTech"/>
        </k-form-item>
        <k-form-item label="融资人类型(按技术领域划分)"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-select v-model="formData.ssDebtorTypeTech"  :data-allowblank="formData.ssDebtorTypeTechAllowblank" data-dict="isuOrgTypTchno"/>
        </k-form-item>
        <k-form-item label="融资人类型(按经济类型划分)"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-select v-model="formData.ssDebtorTypeEconomic"  :data-allowblank="formData.ssDebtorTypeEconomicAllowblank" data-dict="isuOrgTypEcn"/>
        </k-form-item>
        <k-form-item label="融资项目"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-text v-model="formData.ssProject"  :data-allowblank="formData.ssProjectAllowblank" :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="融资人所属行业"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-select v-model="formData.ssIndustryDebtor" :data-allowblank="formData.ssIndustryDebtorAllowblank" data-dict="isuOrgBlgIdt"/>
        </k-form-item>
        <k-form-item label="融资项目所属行业"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-select v-model="formData.ssIndustryProject" :data-allowblank="formData.ssIndustryProjectAllowblank" data-dict="isuOrgBlgIdt" />
        </k-form-item>
        <k-form-item label="项目是否属于重点监控行业和领域"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-select v-model="formData.ssMonitoryIndustry" :data-allowblank="formData.ssMonitoryIndustryAllowblank" data-dict="isTrue"/>
        </k-form-item>
        <k-form-item label="重点监控行业和领域类别"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-select v-model="formData.ssMonitoryIndustryType" data-dict="keyMntIdtTyp"/>
        </k-form-item>
        <k-form-item label="重点监控行业和领域类别说明"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-text v-model="formData.ssDetailsMonitoryType" :data-max-length="256"/>
        </k-form-item>
        <k-form-item label="对应资产外部评级"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-select v-model="formData.ssInternalAssetRate" :data-allowblank="formData.ssInternalAssetRateAllowblank" data-dict="mainRating"/>
        </k-form-item>
        <k-form-item label="担保方式"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-select v-model="formData.ssGuaranteeMethod" data-dict="grntWay"/>
        </k-form-item>
        <k-form-item label="抵质押物类型"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-select v-model="formData.ssPledgeType" data-dict="plgTyp"/>
        </k-form-item>
        <k-form-item label="抵质押物价值（元）"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-text v-model="formData.ssPledgeValue" data-validate-type="money"  data-type="money" data-integer-length="13" data-digits="2" />
        </k-form-item>
        <k-form-item label="担保性质"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-select v-model="formData.ssGuaranteeType" data-dict="grntChr"/>
        </k-form-item>
        <k-form-item label="担保人与融资人关系"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-select v-model="formData.ssGuarantorType" data-dict="grntLvrgRel"/>
        </k-form-item>
        <k-form-item label="担保情况说明"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-text v-model="formData.ssDetailsGuarantee" :data-allowblank="formData.ssDetailsGuaranteeAllowblank"  :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="是否为债转股"  v-show="this.formData.AssetDebtRegisterType == 14">
          <k-field-select v-model="formData.ssDebtEquitySwap" data-dict="subm_isTrue"/>
        </k-form-item>

        <!--16 公募基金/私募基金-->
        <k-form-item label="基金代码"  v-show="this.formData.AssetDebtRegisterType == 16">
          <k-field-text v-model="formData.ppFundCode" :data-allowblank="formData.ppFundCodeAllowblank" :data-max-length="20"/>
        </k-form-item>
        <k-form-item label="基金名称"  v-show="this.formData.AssetDebtRegisterType == 16">
          <k-field-text v-model="formData.ppFundName" :data-allowblank="formData.ppFundNameAllowblank"  :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="行业"  v-show="this.formData.AssetDebtRegisterType == 16">
          <k-field-select v-model="formData.ppIndustry" :data-allowblank="formData.ppIndustryAllowblank" data-dict="hang_ye"/>
        </k-form-item>
        <k-form-item label="登记备案机构"  v-show="this.formData.AssetDebtRegisterType == 16">
          <k-field-select v-model="formData.ppRegistAgency" :data-allowblank="formData.ppRegistAgencyAllowblank" data-dict="regTrstOrg"/>
        </k-form-item>
        <k-form-item label="是否为固定收益类"  v-show="this.formData.AssetDebtRegisterType == 16">
          <k-field-select v-model="formData.ppFixedIncome"  data-dict="istrue"/>
        </k-form-item>
        <k-form-item label="是否属于政府投资基金"  v-show="this.formData.AssetDebtRegisterType == 16">
          <k-field-select v-model="formData.ppGovernInvestFund"  data-dict="isTrue"/>
        </k-form-item>
        <k-form-item label="政府投资基金投向"  v-show="this.formData.AssetDebtRegisterType == 16">
          <k-field-select v-model="formData.ppDirectGovernFund" :data-allowblank="formData.ppDirectGovernFundAllowblank" data-dict="actual_invest_dir_gov_fund"/>
        </k-form-item>
        <k-form-item label="是否由金融资产投资公司发行"  v-show="this.formData.AssetDebtRegisterType == 16">
          <k-field-select v-model="formData.ppIssuedAssetCompany" :data-allowblank="formData.ppIssuedAssetCompanyAllowblank" data-dict="isTrue"/>
        </k-form-item>
        <k-form-item label="基金公司名称"  v-show="this.formData.AssetDebtRegisterType == 16">
          <k-field-text v-model="formData.ppTaName"  :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="基金管理机构名称"  v-show="this.formData.AssetDebtRegisterType == 16">
          <k-field-text v-model="formData.ppManagerFundName" :data-allowblank="formData.ppManagerFundNameAllowblank"  :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="基金托管机构名称"  v-show="this.formData.AssetDebtRegisterType == 16">
          <k-field-text v-model="formData.ppCustodianFundName" :data-allowblank="formData.ppCustodianFundNameAllowblank"  :data-max-length="200"/>
        </k-form-item>
        <k-form-item label="投资阶段"  v-show="this.formData.AssetDebtRegisterType == 16">
          <k-field-select v-model="formData.ppInvestStage" data-dict="subm_invest_stage"/>
        </k-form-item>
        <k-form-item label="投资企业类型(按规模划分)"  v-show="this.formData.AssetDebtRegisterType == 16">
          <k-field-select v-model="formData.ppEnterTypeScale" data-dict="instituteTypeTech"/>
        </k-form-item>
        <k-form-item label="投资企业类型(按技术领域划分)"  v-show="this.formData.AssetDebtRegisterType == 16">
          <k-field-select v-model="formData.ppEnterTypeTech" data-dict="isuOrgTypTchno"/>
        </k-form-item>
        <k-form-item label="投资企业类型(按经济类型划分)"  v-show="this.formData.AssetDebtRegisterType == 16">
          <k-field-select v-model="formData.ppEnterTypeEconomic"  data-dict="isuOrgTypEcn"/>
        </k-form-item>
        <k-form-item label="基金投资资产"  v-show="this.formData.AssetDebtRegisterType == 16">
          <k-field-text v-model="formData.ppInvestAssets" :data-allowblank="formData.ppInvestAssetsAllowblank" :data-max-length="400"/>
        </k-form-item>
         <k-form-item label="理论报送起始日期"  v-show="this.formData.AssetDebtRegisterType == '1' || this.formData.AssetDebtRegisterType == '2' || this.formData.AssetDebtRegisterType == '3' || this.formData.AssetDebtRegisterType == '4' || this.formData.AssetDebtRegisterType == '6' || this.formData.AssetDebtRegisterType == '12' || this.formData.AssetDebtRegisterType == '14' || this.formData.AssetDebtRegisterType == '16' ">
          <k-field-date v-model="formData.theoryReportStartDate" />
        </k-form-item>
        <k-form-footer slot="footer" data-align="center">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AssetDebtRegisterInfo.addAssetDebtRegisterInfo" data-from="addAssetDebtRegisterInfoForm"
                       :data-model="formData" data-target="assetDebtRegisterInfoGrid" :data-handler="submitCheck">
                  <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
                </k-btn>
                <k-btn class="btn-custom-plain" data-functype="CLOSE">
                  <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
              </k-form-footer>
            </k-form>
    </k-popup>

    <!--    修改资产要素登记管理弹出框   -->
    <k-popup ref="editAssetDebtRegisterInfoPopup" data-title="修改"  @data-opened="editOpened()">
      <k-form ref="editAssetDebtRegisterInfoForm" :data-col="2" isFormBodyScreen>
                  <k-form-item label="资产要素分类"   :class="[handleItemDiff('AssetDebtRegisterType')]">
                    <k-field-select v-model="formData.AssetDebtRegisterType" :data-allowblank="false" data-dict="AssetDebtRegisterType" data-disabled="true" data-dict-type="1" @data-on-change="dataOnChange"/>
                  </k-form-item>
                   <!-- 公共信息-->
                   <k-form-item label="行内资产/负债编码" :class="[handleItemDiff('assetCode')]"  v-show="this.formData.AssetDebtRegisterType == '1' || this.formData.AssetDebtRegisterType == '2' || this.formData.AssetDebtRegisterType == '3' || this.formData.AssetDebtRegisterType == '4' || this.formData.AssetDebtRegisterType == '6' || this.formData.AssetDebtRegisterType == '12' || this.formData.AssetDebtRegisterType == '14' || this.formData.AssetDebtRegisterType == '16' || this.formData.AssetDebtRegisterType == '7' || this.formData.AssetDebtRegisterType == '10' || this.formData.AssetDebtRegisterType == '11' ">
                     <k-field-text v-model="formData.assetCode" :data-disabled="true" :data-allowblank="false" :data-max-length="40"/>
                   </k-form-item>
                   <k-form-item label="发行机构代码" :class="[handleItemDiff('bankCode')]"  v-show="this.formData.AssetDebtRegisterType == '1' || this.formData.AssetDebtRegisterType == '2' || this.formData.AssetDebtRegisterType == '3' || this.formData.AssetDebtRegisterType == '4' || this.formData.AssetDebtRegisterType == '6' || this.formData.AssetDebtRegisterType == '12' || this.formData.AssetDebtRegisterType == '14' || this.formData.AssetDebtRegisterType == '16' || this.formData.AssetDebtRegisterType == '7' || this.formData.AssetDebtRegisterType == '10' || this.formData.AssetDebtRegisterType == '11'">
                     <k-field-text v-model="formData.bankCode" :data-disabled="true" :data-allowblank="false" :data-max-length="6"/>
                   </k-form-item>
                   <k-form-item label="交易流通场所" :class="[handleItemDiff('tradeVenue')]"  v-show="this.formData.AssetDebtRegisterType == '1' || this.formData.AssetDebtRegisterType == '2' || this.formData.AssetDebtRegisterType == '3' || this.formData.AssetDebtRegisterType == '4' || this.formData.AssetDebtRegisterType == '6' || this.formData.AssetDebtRegisterType == '12' || this.formData.AssetDebtRegisterType == '14' || this.formData.AssetDebtRegisterType == '16' || this.formData.AssetDebtRegisterType == '7' || this.formData.AssetDebtRegisterType == '10' || this.formData.AssetDebtRegisterType == '11'">
                     <k-field-select v-model="formData.tradeVenue"  :data-disabled="true" :data-allowblank="false" data-dict="subm_tacdingPlace"/>
                   </k-form-item>
                   <k-form-item label="币种" :class="[handleItemDiff('cur')]"  v-show="this.formData.AssetDebtRegisterType == '1' || this.formData.AssetDebtRegisterType == '2' || this.formData.AssetDebtRegisterType == '3' || this.formData.AssetDebtRegisterType == '4' || this.formData.AssetDebtRegisterType == '6' || this.formData.AssetDebtRegisterType == '12' || this.formData.AssetDebtRegisterType == '14' || this.formData.AssetDebtRegisterType == '16' || this.formData.AssetDebtRegisterType == '7' || this.formData.AssetDebtRegisterType == '10' || this.formData.AssetDebtRegisterType == '11'">
                     <k-field-select v-model="formData.cur" :data-allowblank="false" data-dict="tr_cur" :data-max-length="3"/>
                   </k-form-item>
                   <k-form-item label="资产/负债类别" :class="[handleItemDiff('assDebtType')]" v-show="this.formData.AssetDebtRegisterType == '1' || this.formData.AssetDebtRegisterType == '2' || this.formData.AssetDebtRegisterType == '3' || this.formData.AssetDebtRegisterType == '4' || this.formData.AssetDebtRegisterType == '6' || this.formData.AssetDebtRegisterType == '12' || this.formData.AssetDebtRegisterType == '14' || this.formData.AssetDebtRegisterType == '16' || this.formData.AssetDebtRegisterType == '7' || this.formData.AssetDebtRegisterType == '10' || this.formData.AssetDebtRegisterType == '11'">
                     <k-field-select v-model="formData.assDebtType" :data-disabled="true" :data-allowblank="false" :data-data="formData.assDebtTypeDict"  data-value-field="VALUE" data-display-field="TEXT"/>
                   </k-form-item>
                   <k-form-item label="备注" :class="[handleItemDiff('details')]" v-show="this.formData.AssetDebtRegisterType == '1' || this.formData.AssetDebtRegisterType == '2' || this.formData.AssetDebtRegisterType == '3' || this.formData.AssetDebtRegisterType == '4' || this.formData.AssetDebtRegisterType == '6' || this.formData.AssetDebtRegisterType == '12' || this.formData.AssetDebtRegisterType == '14' || this.formData.AssetDebtRegisterType == '16' || this.formData.AssetDebtRegisterType == '7' || this.formData.AssetDebtRegisterType == '10' || this.formData.AssetDebtRegisterType == '11'">
                     <k-field-text v-model="formData.details"  :data-max-length="256" :data-allowblank="formData.detailsAllowblank"/>
                   </k-form-item>

                   <!--1 本行/他行存款||大额存单-->
                   <k-form-item label="资金存入银行" :class="[handleItemDiff('bbDepositBank')]" v-show="this.formData.AssetDebtRegisterType == 1">
                     <k-field-text v-model="formData.bbDepositBank" :data-max-length="200"/>
                   </k-form-item>
                    <k-form-item label="存款账号" :class="[handleItemDiff('bbAccountNo')]" v-show="this.formData.AssetDebtRegisterType == 1">
                     <k-field-text v-model="formData.bbAccountNo" :data-max-length="60"/>
                   </k-form-item>
                   <k-form-item label="存款金额" :class="[handleItemDiff('bbDepositAmt')]" v-show="this.formData.AssetDebtRegisterType == 1">
                     <k-field-text v-model="formData.bbDepositAmt" :data-allowblank="formData.bbDepositAmtAllowblank" data-validate-type="money" data-type="money" data-integer-length="13" data-digits="2"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
                   <k-form-item label="起息日" :class="[handleItemDiff('bbValueDate')]" v-show="this.formData.AssetDebtRegisterType == 1">
                     <k-field-date v-model="formData.bbValueDate" :data-allowblank="formData.bbValueDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
                   </k-form-item>
                   <k-form-item label="到期日" :class="[handleItemDiff('bbMaturityDate')]" v-show="this.formData.AssetDebtRegisterType == 1">
                     <k-field-date v-model="formData.bbMaturityDate" :data-allowblank="formData.bbMaturityDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
                   </k-form-item>
                   <k-form-item label="年利率%" :class="[handleItemDiff('bbAnnualRate')]" v-show="this.formData.AssetDebtRegisterType == 1">
                     <k-field-text v-model="formData.bbAnnualRate" :data-allowblank="formData.bbAnnualRateAllowblank" data-validate-type="number" data-type="number" data-integer-length="3" data-digits="5" data-min-value="0" />
                   </k-form-item>
                   <k-form-item label="计息基础" :class="[handleItemDiff('bbInterestBasis')]" v-show="this.formData.AssetDebtRegisterType == 1">
                     <k-field-select v-model="formData.bbInterestBasis" :data-allowblank="formData.bbInterestBasisAllowblank" data-dict="intr_base" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="存款类型" :class="[handleItemDiff('bbDepositType')]" v-show="this.formData.AssetDebtRegisterType == 1">
                     <k-field-select v-model="formData.bbDepositType" data-dict="deposit_type" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="结构性存款挂钩标的类别" :class="[handleItemDiff('bbStructDepositType')]" v-show="this.formData.AssetDebtRegisterType == 1">
                     <k-field-select v-model="formData.bbStructDepositType" data-dict="stru_deposit_type" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="结构性存款挂钩标的" :class="[handleItemDiff('bbStructDeposit')]" v-show="this.formData.AssetDebtRegisterType == 1">
                     <k-field-text v-model="formData.bbStructDeposit" :data-max-length="256"/>
                   </k-form-item>

                   <!--2 债券类资产/理财直接融资工具/同业存单-->
                   <k-form-item label="代码" :class="[handleItemDiff('ccIdentCode')]" v-show="this.formData.AssetDebtRegisterType == 2">
                     <k-field-text v-model="formData.ccIdentCode" :data-allowblank="formData.ccIdentCodeAllowblank" :data-max-length="15"/>
                   </k-form-item>
                   <k-form-item label="名称" :class="[handleItemDiff('ccName')]" v-show="this.formData.AssetDebtRegisterType == 2">
                     <k-field-text v-model="formData.ccName" :data-allowblank="formData.ccNameAllowblank" :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="具体类别" :class="[handleItemDiff('ccSpecificBondType')]" v-show="this.formData.AssetDebtRegisterType == 2">
                     <k-field-select v-model="formData.ccSpecificBondType"  data-dict="spcType" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="发行方式" :class="[handleItemDiff('ccIssModeBond')]" v-show="this.formData.AssetDebtRegisterType == 2">
                     <k-field-select v-model="formData.ccIssModeBond" :data-allowblank="formData.ccIssModeBondAllowblank" data-dict="iss_mode_bond" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="主体评级" :class="[handleItemDiff('ccIssRatePart')]" v-show="this.formData.AssetDebtRegisterType == 2">
                     <k-field-select v-model="formData.ccIssRatePart" :data-allowblank="formData.ccIssRatePartAllowblank" data-dict="mainRating" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="发行机构类型(按规模划分)" :class="[handleItemDiff('ccInstituteTypeScale')]" v-show="this.formData.AssetDebtRegisterType == 2">
                     <k-field-select v-model="formData.ccInstituteTypeScale" :data-allowblank="formData.ccInstituteTypeScaleAllowblank" data-dict="instituteTypeTech" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="发行机构类型(按技术领域划分)" :class="[handleItemDiff('ccInstituteTypeTech')]" v-show="this.formData.AssetDebtRegisterType == 2">
                     <k-field-select v-model="formData.ccInstituteTypeTech" :data-allowblank="formData.ccInstituteTypeTechAllowblank" data-dict="isuOrgTypTchno" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="发行机构类型(按经济类型划分)" :class="[handleItemDiff('ccInstituteTypeEconomic')]" v-show="this.formData.AssetDebtRegisterType == 2">
                     <k-field-select v-model="formData.ccInstituteTypeEconomic" :data-allowblank="formData.ccInstituteTypeEconomicAllowblank" data-dict="isuOrgTypEcn" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="发行机构所属行业" :class="[handleItemDiff('ccIndustryIssuer')]" v-show="this.formData.AssetDebtRegisterType == 2">
                     <k-field-select v-model="formData.ccIndustryIssuer" :data-allowblank="formData.ccIndustryIssuerAllowblank"  data-dict="isuOrgBlgIdt" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="登记托管机构" :class="[handleItemDiff('ccRegistDeposit')]" v-show="this.formData.AssetDebtRegisterType == 2">
                     <k-field-select v-model="formData.ccRegistDeposit" :data-allowblank="formData.ccRegistDepositAllowblank" data-dict="regTrstOrg" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="登记托管机构说明" :class="[handleItemDiff('ccDetailsRegistDeposit')]" v-show="this.formData.AssetDebtRegisterType == 2">
                     <k-field-text v-model="formData.ccDetailsRegistDeposit" :data-max-length="256"/>
                   </k-form-item>

                   <!--3 拆放同业及买入返售/同业拆入及卖出回购-->
                   <k-form-item label="起息日" :class="[handleItemDiff('ddValueDate')]" v-show="this.formData.AssetDebtRegisterType == 3">
                     <k-field-date v-model="formData.ddValueDate" :data-allowblank="formData.ddValueDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
                   </k-form-item>
                   <k-form-item label="到期日" :class="[handleItemDiff('ddMaturityDate')]" v-show="this.formData.AssetDebtRegisterType == 3">
                     <k-field-date v-model="formData.ddMaturityDate" :data-allowblank="formData.ddMaturityDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
                   </k-form-item>
                   <k-form-item label="对手方" :class="[handleItemDiff('ddCounterparty')]" v-show="this.formData.AssetDebtRegisterType == 3">
                     <k-field-text v-model="formData.ddCounterparty" :data-allowblank="formData.ddCounterpartyAllowblank" :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="对手方类型" :class="[handleItemDiff('ddCounterpartyType')]" v-show="this.formData.AssetDebtRegisterType == 3">
                     <k-field-select v-model="formData.ddCounterpartyType" :data-allowblank="formData.ddCounterpartyTypeAllowblank" data-dict="counterparty_type" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="年利率%" :class="[handleItemDiff('ddAnnalInterestRate')]" v-show="this.formData.AssetDebtRegisterType == 3">
                     <k-field-text v-model="formData.ddAnnalInterestRate" :data-allowblank="formData.ddAnnalInterestRateAllowblank" data-validate-type="number" data-type="number" data-integer-length="3" data-digits="5" data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
                   <k-form-item label="计息基础" :class="[handleItemDiff('ddInterestBasis')]" v-show="this.formData.AssetDebtRegisterType == 3">
                     <k-field-select v-model="formData.ddInterestBasis" :data-allowblank="formData.ddInterestBasisAllowblank" data-dict="intr_base" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="回购标的类别" :class="[handleItemDiff('ddCollateralType')]" v-show="this.formData.AssetDebtRegisterType == 3">
                     <k-field-select v-model="formData.ddCollateralType" :data-allowblank="formData.ddCollateralTypeAllowblank" data-dict="interest_basis" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="回购标的金额" :class="[handleItemDiff('ddCollateralValue')]" v-show="this.formData.AssetDebtRegisterType == 3">
                     <k-field-text v-model="formData.ddCollateralValue" data-validate-type="money" data-type="money"  data-integer-length="13" data-digits="2" data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>

                   <!--4 非标准化债权类资产/新增可投资资产-->
                   <k-form-item label="收/受权益类型" :class="[handleItemDiff('eeOwnershipType')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeOwnershipType" data-dict="incBenRitType" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="是否属于买入反售" :class="[handleItemDiff('eeBuyback')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeBuyback"  :data-allowblank="formData.eeBuybackAllowblank" data-dict="isTrue" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="名称" :class="[handleItemDiff('eeName')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeName" :data-allowblank="formData.eeNameAllowblank" :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="资产代码" :class="[handleItemDiff('eeAssetCode')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeAssetCode" :data-allowblank="formData.eeAssetCodeAllowblank" :data-max-length="10"/>
                   </k-form-item>
                   <k-form-item label="金额" :class="[handleItemDiff('eeAmt')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeAmt" :data-allowblank="formData.eeAmtAllowblank" data-validate-type="money" data-type="money" data-integer-length="13" data-digits="2" data-min-value="0"   data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
                   <k-form-item label="份额面值" :class="[handleItemDiff('eeUnitParValue')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeUnitParValue" :data-allowblank="formData.eeUnitParValueAllowblank" data-validate-type="money" data-type="money" data-integer-length="13" data-digits="4" data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
                   <k-form-item label="起息日" :class="[handleItemDiff('eeValueDate')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-date v-model="formData.eeValueDate" :data-allowblank="formData.eeValueDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
                   </k-form-item>
                   <k-form-item label="到期日" :class="[handleItemDiff('eeMaturityDate')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-date v-model="formData.eeMaturityDate" :data-allowblank="formData.eeMaturityDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
                   </k-form-item>
                   <k-form-item label="法定到期日" :class="[handleItemDiff('eeStatutoryMaturityDate')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-date v-model="formData.eeStatutoryMaturityDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
                   </k-form-item>
                   <k-form-item label="是否有预期收益率" :class="[handleItemDiff('eeExpectedReturn')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeExpectedReturn" :data-allowblank="formData.eeExpectedReturnAllowblank" data-dict="isTrue" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="项目收益率(利率)%" :class="[handleItemDiff('eeProjectAnnaulReturn')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeProjectAnnaulReturn" data-validate-type="money" data-type="money" data-integer-length="3" data-digits="5" data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
                   <k-form-item label="计息类型" :class="[handleItemDiff('eeCouponType')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeCouponType" data-dict="interest_type" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="规则付息标识" :class="[handleItemDiff('eeRegualrInterestPay')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeRegualrInterestPay" data-dict="isTrue" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="付息频率(个月/次)" :class="[handleItemDiff('eeInterestPayFrequency')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeInterestPayFrequency" :data-allowblank="formData.eeInterestPayFrequencyAllowblank" data-validate-type="number"  data-integer-length="2" data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
                   <k-form-item label="利息分布方式" :class="[handleItemDiff('eeCouponAllocationType')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeCouponAllocationType" data-dict="intrAlcMth" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="还本付息情况说明" :class="[handleItemDiff('eeDetailPrincInterest')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeDetailPrincInterest" :data-allowblank="formData.eeDetailPrincInterestAllowblank" :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="计息基础" :class="[handleItemDiff('eeInterestBasis')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeInterestBasis" :data-allowblank="formData.eeInterestBasisAllowblank" data-dict="intr_base" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="基准利率种类" :class="[handleItemDiff('eeBenchRateType')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeBenchRateType" data-dict="bchmRatTyp" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="是否有浮动因子" :class="[handleItemDiff('eeFloatFactor')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeFloatFactor" data-dict="isTrue" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="浮动因子%" :class="[handleItemDiff('eeFloatRate')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeFloatRate" data-validate-type="money" data-type="money" data-integer-length="3" data-digits="5" />
                   </k-form-item>
                   <k-form-item label="利差（BP）%" :class="[handleItemDiff('eeYieldSpreadBp')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeYieldSpreadBp" data-validate-type="money" data-type="money"  data-integer-length="3" data-digits="5" />
                   </k-form-item>
                   <k-form-item label="结构档次" :class="[handleItemDiff('eeStructGrade')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeStructGrade" data-dict="strcGrd" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="还本方式" :class="[handleItemDiff('eePrincPaymentType')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eePrincPaymentType" data-dict="payPrcpMth" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="分期还本条款标识" :class="[handleItemDiff('eeInstallRepayType')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeInstallRepayType" data-dict="insPayPrcpF" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="基础资产类型" :class="[handleItemDiff('eeBaseAssetType')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeBaseAssetType"  :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="超额收益分配比例（%）" :class="[handleItemDiff('eePercentExcInAllot')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eePercentExcInAllot" data-validate-type="number"  data-type="number"  data-integer-length="3" data-digits="5" />
                   </k-form-item>
                   <k-form-item label="融资人" :class="[handleItemDiff('eeDebtor')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeDebtor" :data-allowblank="formData.eeDebtorAllowblank" :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="融资人内部信用评级" :class="[handleItemDiff('eeDeptorRate')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeDeptorRate" :data-allowblank="formData.eeDeptorRateAllowblank" data-dict="subm_mainRating"/>
                   </k-form-item>
                   <k-form-item label="外部评级机构名称及对融资人评级结果" :class="[handleItemDiff('eeRateAgencyIss')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeRateAgencyIss" :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="融资人类型(按规模划分)" :class="[handleItemDiff('eeDebtorTypeScale')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeDebtorTypeScale" :data-allowblank="formData.eeDebtorTypeScaleAllowblank" data-dict="instituteTypeTech" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="融资人类型(按技术领域划分)" :class="[handleItemDiff('eeDebtorTypeTech')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeDebtorTypeTech" :data-allowblank="formData.eeDebtorTypeTechAllowblank" data-dict="isuOrgTypTchno" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="融资人类型(按经济类型划分)" :class="[handleItemDiff('eeDebtorTypeEconomic')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeDebtorTypeEconomic" :data-allowblank="formData.eeDebtorTypeEconomicAllowblank" data-dict="isuOrgTypEcn" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="融资项目" :class="[handleItemDiff('eeProject')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeProject" :data-allowblank="formData.eeProjectAllowblank" :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="融资人所属行业" :class="[handleItemDiff('eeIndustryDebtor')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeIndustryDebtor" :data-allowblank="formData.eeIndustryDebtorAllowblank" data-dict="isuOrgBlgIdt" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="项目是否属于重点监控行业和领域" :class="[handleItemDiff('eeMonitorIndusType')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeMonitorIndusType" :data-allowblank="formData.eeMonitorIndusTypeAllowblank" data-dict="isTrue" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="重点监控行业和领域类别" :class="[handleItemDiff('eeMonitorIndustryType')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeMonitorIndustryType" data-dict="keyMntIdtTyp" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="重点监控行业和领域类别说明" :class="[handleItemDiff('eeDetailsMonitoryType')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeDetailsMonitoryType" :data-max-length="256"/>
                   </k-form-item>
                   <k-form-item label="担保方式" :class="[handleItemDiff('eeGuaranteeMethod')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeGuaranteeMethod" data-dict="grntWay" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="抵质押物类型" :class="[handleItemDiff('eePledgeType')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eePledgeType" data-dict="plgTyp" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="抵质押物价值（元）" :class="[handleItemDiff('eePledgeValue')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eePledgeValue" data-validate-type="money"  data-type="money" data-integer-length="13" data-digits="2" />
                   </k-form-item>
                   <k-form-item label="担保性质" :class="[handleItemDiff('eeGuaranteeType')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeGuaranteeType" data-dict="grntChr" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="担保人与融资人关系" :class="[handleItemDiff('eeGuarantorType')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeGuarantorType" data-dict="grntLvrgRel" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="担保情况说明" :class="[handleItemDiff('eeDetailGuaranteeStatus')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeDetailGuaranteeStatus" :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="融资人主体评级" :class="[handleItemDiff('eeDebtorRate')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeDebtorRate" :data-allowblank="formData.eeDebtorRateAllowblank" data-dict="mainRating" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="资产内部评级" :class="[handleItemDiff('eeInterAssetRate')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeInterAssetRate" :data-allowblank="formData.eeInterAssetRateAllowblank" data-dict="mainRating" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="资产外部评级" :class="[handleItemDiff('eeOutAssetRate')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeOutAssetRate" :data-allowblank="formData.eeOutAssetRateAllowblank" data-dict="mainRating" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="含权类型" :class="[handleItemDiff('eeOptionType')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeOptionType"  data-dict="embOptTyp" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="行权方式" :class="[handleItemDiff('eeExerciseDateType')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeExerciseDateType" data-dict="xcsRitMth" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="固定行权日" :class="[handleItemDiff('eeFixedExerciseDate')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-date v-model="formData.eeFixedExerciseDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
                   </k-form-item>
                   <k-form-item label="首次行权日期" :class="[handleItemDiff('eeFirstExerciseDate')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-date v-model="formData.eeFirstExerciseDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
                   </k-form-item>
                   <k-form-item label="行权周期" :class="[handleItemDiff('eeExercisePeriod')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeExercisePeriod" data-validate-type="number"  data-integer-length="5" data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
                   <k-form-item label="行权价格" :class="[handleItemDiff('eeExercisePrice')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeExercisePrice"  data-validate-type="money"  data-type="money"  data-integer-length="13" data-digits="4"/>
                   </k-form-item>
                   <k-form-item label="永续条款类型" :class="[handleItemDiff('eePerpetualType')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eePerpetualType" data-dict="perpTyp" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="利息递延条款类型" :class="[handleItemDiff('eeDeferreInterestType')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeDeferreInterestType" data-dict="intrPpnTyp" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="递延利息是否计息" :class="[handleItemDiff('eeInterestDeferred')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeInterestDeferred" data-dict="isTrue" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="首次重定价日期" :class="[handleItemDiff('eeFirstRepriceDate')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-date v-model="formData.eeFirstRepriceDate" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
                   </k-form-item>
                   <k-form-item label="重定价周期" :class="[handleItemDiff('eeRepricePeriod')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeRepricePeriod"  :data-max-length="5" data-validate-type="number"  data-type="number" data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
                   <k-form-item label="部分赎回标识" :class="[handleItemDiff('eePartialRedemption')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eePartialRedemption" data-dict="isTrue" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="部分赎回比例%" :class="[handleItemDiff('eePartialRedemptionRate')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eePartialRedemptionRate"  data-validate-type="number"  data-integer-length="3" data-digits="5"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
                   <k-form-item label="选择权" :class="[handleItemDiff('eeOptionRight')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeOptionRight" :data-allowblank="formData.eeOptionRightAllowblank"  :data-max-length="60"/>
                   </k-form-item>
                   <k-form-item label="行权条件说明" :class="[handleItemDiff('eeDetailsExerciseTerm')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeDetailsExerciseTerm" :data-allowblank="formData.eeDetailsExerciseTermAllowblank"  :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="融资人所属地区" :class="[handleItemDiff('eeRegionDebtor')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeRegionDebtor" :data-allowblank="formData.eeRegionDebtorAllowblank" data-dict="asset_sale_area" data-dict-type="1"/>
                   </k-form-item>
                    <k-form-item label="融资人组织机构（社会信用）代码" :class="[handleItemDiff('eeOrganizationCode')]" v-show="this.formData.AssetDebtRegisterType == 4">
                       <k-field-text v-model="formData.eeOrganizationCode" :data-allowblank="formData.eeOrganizationCodeAllowblank" :data-max-length="18"/>
                     </k-form-item>
                   <k-form-item label="增信机构代码" :class="[handleItemDiff('eeEnhanceInstituteCode')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeEnhanceInstituteCode" :data-max-length="18"/>
                   </k-form-item>
                   <k-form-item label="增信机构名称" :class="[handleItemDiff('eeEnhanceInstituteName')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeEnhanceInstituteName" :data-max-length="18"/>
                   </k-form-item>
                   <k-form-item label="融资总费率" :class="[handleItemDiff('eeTotalFeeRate')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-text v-model="formData.eeTotalFeeRate" :data-allowblank="formData.eeTotalFeeRateAllowblank"  data-validate-type="number"  data-integer-length="3" data-digits="5"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
                   <k-form-item label="融资项目所属行业" :class="[handleItemDiff('eeIndustryProject')]" v-show="this.formData.AssetDebtRegisterType == 4">
                     <k-field-select v-model="formData.eeIndustryProject" :data-allowblank="formData.eeIndustryProjectAllowblank" data-dict="isuOrgBlgIdt" data-dict-type="1"/>
                   </k-form-item>

                   <!--6 权益类资产-->
                   <k-form-item label="股票代码" :class="[handleItemDiff('ggStockCode')]" v-show="this.formData.AssetDebtRegisterType == 6">
                     <k-field-text v-model="formData.ggStockCode" :data-max-length="20" :data-allowblank="formData.ggStockCodeAllowblank"/>
                   </k-form-item>
                   <k-form-item label="股票/企业名称" :class="[handleItemDiff('ggName')]" v-show="this.formData.AssetDebtRegisterType == 6">
                     <k-field-text v-model="formData.ggName" :data-allowblank="formData.ggNameAllowblank" :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="股票类型" :class="[handleItemDiff('ggStockType')]" v-show="this.formData.AssetDebtRegisterType == 6">
                     <k-field-select v-model="formData.ggStockType" data-dict="subm_stock_type" :data-allowblank="formData.ggStockTypeAllowblank"/>
                   </k-form-item>
                   <k-form-item label="行业" :class="[handleItemDiff('ggIndustry')]" v-show="this.formData.AssetDebtRegisterType == 6">
                     <k-field-select v-model="formData.ggIndustry" :data-allowblank="formData.ggIndustryAllowblank" data-dict="hang_ye"/>
                   </k-form-item>
                   <k-form-item label="投资阶段" :class="[handleItemDiff('ggInvestStage')]" v-show="this.formData.AssetDebtRegisterType == 6">
                     <k-field-select v-model="formData.ggInvestStage" data-dict="subm_invest_stage" :data-allowblank="formData.ggInvestStageAllowblank"/>
                   </k-form-item>
                   <k-form-item label="股权退出安排" :class="[handleItemDiff('ggEquityOutDate')]" v-show="this.formData.AssetDebtRegisterType == 6">
                     <k-field-date v-model="formData.ggEquityOutDate" data-date-format="yyyyMMdd" :data-allowblank="formData.ggEquityOutDateAllowblank" data-value-format="yyyyMMdd"/>
                   </k-form-item>
                   <k-form-item label="企业类型(按规模划分)" :class="[handleItemDiff('ggEnterTypeScale')]" v-show="this.formData.AssetDebtRegisterType == 6">
                     <k-field-select v-model="formData.ggEnterTypeScale" :data-allowblank="formData.ggEnterTypeScaleAllowblank" data-dict="subm_instituteTypeTech"/>
                   </k-form-item>
                   <k-form-item label="企业类型(按技术领域划分)" :class="[handleItemDiff('ggEnterTypeTech')]" v-show="this.formData.AssetDebtRegisterType == 6">
                     <k-field-select v-model="formData.ggEnterTypeTech" :data-allowblank="formData.ggEnterTypeTechAllowblank" data-dict="subm_isuOrgTypTchno"/>
                   </k-form-item>
                   <k-form-item label="企业类型(按经济类型划分)" :class="[handleItemDiff('ggEnterTypeEconomic')]" v-show="this.formData.AssetDebtRegisterType == 6">
                     <k-field-select v-model="formData.ggEnterTypeEconomic" :data-allowblank="formData.ggEnterTypeEconomicAllowblank" data-dict="subm_isuOrgTypEcn"/>
                   </k-form-item>
                   <k-form-item label="是否质押融资" :class="[handleItemDiff('ggPledgedFinace')]" v-show="this.formData.AssetDebtRegisterType == 6">
                     <k-field-select v-model="formData.ggPledgedFinace" :data-allowblank="formData.ggPledgedFinaceAllowblank" data-dict="subm_isTrue"/>
                   </k-form-item>
                   <k-form-item label="是否为债转股" :class="[handleItemDiff('ggDebtEquitySwap')]" v-show="this.formData.AssetDebtRegisterType == 6">
                     <k-field-select v-model="formData.ggDebtEquitySwap" data-dict="subm_isTrue"/>
                   </k-form-item>

                   <!--12 资产管理产品-->
                   <k-form-item label="资管计划名称" :class="[handleItemDiff('mmManagePlanName')]"  v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-text v-model="formData.mmManagePlanName" :data-allowblank="formData.mmManagePlanNameAllowblank" :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="是否为银行理财产品" :class="[handleItemDiff('mmManageProduct')]"  v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-select v-model="formData.mmManageProduct" :data-allowblank="formData.mmManageProductAllowblank"  data-dict="isTrue" data-dict-type="1"/>
                   </k-form-item>
                     <k-form-item label="理财产品登记编码" :class="[handleItemDiff('mmProductCode')]" v-show="this.formData.AssetDebtRegisterType == 12">
                       <k-field-text v-model="formData.mmProductCode" :data-allowblank="formData.mmProductCodeAllowblank"  :data-max-length="15"/>
                     </k-form-item>
                   <k-form-item label="资管计划发起人机构编码" :class="[handleItemDiff('mmPlanIssuerCode')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-text v-model="formData.mmPlanIssuerCode" :data-allowblank="formData.mmPlanIssuerCodeAllowblank"  :data-max-length="32"/>
                   </k-form-item>
                   <k-form-item label="是否由金融资产投资公司发行" :class="[handleItemDiff('mmIssuedAssetCompany')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-select v-model="formData.mmIssuedAssetCompany"  data-dict="isTrue" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="资管计划登记编码" :class="[handleItemDiff('mmAssetPlanRgCode')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-text v-model="formData.mmAssetPlanRgCode" :data-allowblank="formData.mmAssetPlanRgCodeAllowblank" :data-max-length="256"/>
                   </k-form-item>
                   <k-form-item label="管理人" :class="[handleItemDiff('mmManager')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-text v-model="formData.mmManager" :data-allowblank="formData.mmManagerAllowblank" :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="托管人" :class="[handleItemDiff('mmCustodian')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-text v-model="formData.mmCustodian" :data-allowblank="formData.mmCustodianAllowblank" :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="金额" :class="[handleItemDiff('mmAmt')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-text v-model="formData.mmAmt" :data-allowblank="formData.mmAmtAllowblank"   data-validate-type="money" data-type="money"  data-min-value="0" data-integer-length="13" data-digits="2"/>
                   </k-form-item>
                   <k-form-item label="资金实际投向" :class="[handleItemDiff('mmActualDirect')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-select v-model="formData.mmActualDirect" :data-allowblank="formData.mmActualDirectAllowblank" data-dict="actual_invest_dir_fund" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="资金运用方式" :class="[handleItemDiff('mmDetailsInvest')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-text v-model="formData.mmDetailsInvest" :data-allowblank="formData.mmDetailsInvestAllowblank" :data-max-length="300"/>
                   </k-form-item>
                   <k-form-item label="资金运用行业" :class="[handleItemDiff('mmIndustryInvest')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-select v-model="formData.mmIndustryInvest" :data-allowblank="formData.mmIndustryInvestAllowblank" data-dict="isuOrgBlgIdt" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="资管计划成立日期" :class="[handleItemDiff('mmPlanStartDate')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-date v-model="formData.mmPlanStartDate" :data-allowblank="formData.mmPlanStartDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
                   </k-form-item>
                   <k-form-item label="资管计划终止日期" :class="[handleItemDiff('mmPlanMaturityDate')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-date v-model="formData.mmPlanMaturityDate" :data-allowblank="formData.mmPlanMaturityDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
                   </k-form-item>
                   <k-form-item label="资管计划属性" :class="[handleItemDiff('mmPlanType')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-select v-model="formData.mmPlanType" :data-allowblank="formData.mmPlanTypeAllowblank" data-dict="astMngPlanPrpt" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="是否有预期收益率" :class="[handleItemDiff('mmExpectedReturn')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-select v-model="formData.mmExpectedReturn" :data-allowblank="formData.mmExpectedReturnAllowblank" data-dict="isTrue" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="预期最高收益率%" :class="[handleItemDiff('mmMaxExpectedReturn')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-text v-model="formData.mmMaxExpectedReturn"  data-validate-type="number" data-type="number"  data-integer-length="3" data-digits="5"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
                   <k-form-item label="预期最低收益率%" :class="[handleItemDiff('mmMinExpectedReturn')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-text v-model="formData.mmMinExpectedReturn"  data-validate-type="number" data-type="number"   data-integer-length="3" data-digits="5"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
                   <k-form-item label="购买结构" :class="[handleItemDiff('mmInvestStructure')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-select v-model="formData.mmInvestStructure" :data-allowblank="formData.mmInvestStructureAllowblank" data-dict="buyStrc" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="管理方式" :class="[handleItemDiff('mmManagerType')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-select v-model="formData.mmManagerType" :data-allowblank="formData.mmManagerTypeAllowblank" data-dict="mngMth" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="管理费率%" :class="[handleItemDiff('mmManagerFeeRate')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-text v-model="formData.mmManagerFeeRate" :data-allowblank="formData.mmManagerFeeRateAllowblank" data-validate-type="number"  data-type="number"  data-integer-length="3" data-digits="5"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
                   <k-form-item label="托管费率%" :class="[handleItemDiff('mmCustodianFeeRate')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-text v-model="formData.mmCustodianFeeRate" :data-allowblank="formData.mmCustodianFeeRateAllowblank" data-validate-type="number" data-type="number"   data-integer-length="3" data-digits="5"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
                   <k-form-item label="交易相关合计费率%" :class="[handleItemDiff('mmTransCostRate')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-text v-model="formData.mmTransCostRate" :data-allowblank="formData.mmTransCostRateAllowblank"   data-validate-type="number" data-type="number"   data-integer-length="3" data-digits="5"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
                   <k-form-item label="中介服务机构合计费率%" :class="[handleItemDiff('mmInterFeeRate')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-text v-model="formData.mmInterFeeRate" :data-allowblank="formData.mmInterFeeRateAllowblank"   data-validate-type="number" data-type="number"   data-integer-length="3" data-digits="5"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
                   <k-form-item label="其他合计费率%" :class="[handleItemDiff('mmOtherExpenseRate')]" v-show="this.formData.AssetDebtRegisterType == 12">
                     <k-field-text v-model="formData.mmOtherExpenseRate" :data-allowblank="formData.mmOtherExpenseRateAllowblank"   data-validate-type="number" data-type="number"   data-integer-length="3" data-digits="5"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>

                   <!--14 其他非标准化债权类/权益类/代客境外理财投资QDII/新增可投资资产-->
                   <k-form-item label="名称" :class="[handleItemDiff('ssName')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-text v-model="formData.ssName" :data-allowblank="formData.ssNameAllowblank"  :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="行内资产类别" :class="[handleItemDiff('ssAssetType')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-select v-model="formData.ssAssetType" :data-allowblank="formData.ssAssetTypeAllowblank" data-dict="asset_type" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="行内资产类别说明" :class="[handleItemDiff('ssDetailsAssetType')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-text v-model="formData.ssDetailsAssetType" :data-max-length="256"/>
                   </k-form-item>
                   <k-form-item label="金额" :class="[handleItemDiff('ssAmt')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-text v-model="formData.ssAmt"  :data-allowblank="formData.ssAmtAllowblank" data-validate-type="money"   data-integer-length="13" data-digits="2"  data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
                   <k-form-item label="起息日" :class="[handleItemDiff('ssValueDate')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-date v-model="formData.ssValueDate" :data-allowblank="formData.ssValueDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
                   </k-form-item>
                   <k-form-item label="到期日" :class="[handleItemDiff('ssMaturityDate')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-date v-model="formData.ssMaturityDate" :data-allowblank="formData.ssMaturityDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
                   </k-form-item>
                   <k-form-item label="所属国家或地区" :class="[handleItemDiff('ssCountry')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-select v-model="formData.ssCountry" :data-allowblank="formData.ssCountryAllowblank" data-dict="tr_iss_country" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="是否有预期收益率" :class="[handleItemDiff('ssExpectedReturn')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-select v-model="formData.ssExpectedReturn" :data-allowblank="formData.ssExpectedReturnAllowblank" data-dict="isTrue" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="项目收益率(利率)%" :class="[handleItemDiff('ssAnnualReturn')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-text v-model="formData.ssAnnualReturn" data-validate-type="number" data-type="number"   data-integer-length="3" data-digits="5"/>
                   </k-form-item>
                   <k-form-item label="付息频率(个月/次)" :class="[handleItemDiff('ssInterestFrequency')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-text v-model="formData.ssInterestFrequency" :data-allowblank="formData.ssInterestFrequencyAllowblank" data-validate-type="number"  data-type="number" :data-max-length="2" data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
                   <k-form-item label="融资人" :class="[handleItemDiff('ssDebtor')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-text v-model="formData.ssDebtor" :data-allowblank="formData.ssDebtorAllowblank" :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="融资人组织机构(社会信用)代码" :class="[handleItemDiff('ssOrganCode')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-text v-model="formData.ssOrganCode"  :data-allowblank="formData.ssOrganCodeAllowblank" :data-max-length="18"/>
                   </k-form-item>
                   <k-form-item label="外部评级机构名称及对融资人评级结果" :class="[handleItemDiff('ssRateAgencyIss')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-text v-model="formData.ssRateAgencyIss" :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="融资人类型(按规模划分)" :class="[handleItemDiff('ssDebtorTypeScale')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-select v-model="formData.ssDebtorTypeScale"  :data-allowblank="formData.ssDebtorTypeScaleAllowblank" data-dict="instituteTypeTech" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="融资人类型(按技术领域划分)" :class="[handleItemDiff('ssDebtorTypeTech')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-select v-model="formData.ssDebtorTypeTech"  :data-allowblank="formData.ssDebtorTypeTechAllowblank" data-dict="isuOrgTypTchno" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="融资人类型(按经济类型划分)" :class="[handleItemDiff('ssDebtorTypeEconomic')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-select v-model="formData.ssDebtorTypeEconomic"  :data-allowblank="formData.ssDebtorTypeEconomicAllowblank" data-dict="isuOrgTypEcn" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="融资项目" :class="[handleItemDiff('ssProject')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-text v-model="formData.ssProject"  :data-allowblank="formData.ssProjectAllowblank" :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="融资人所属行业" :class="[handleItemDiff('ssIndustryDebtor')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-select v-model="formData.ssIndustryDebtor" :data-allowblank="formData.ssIndustryDebtorAllowblank" data-dict="isuOrgBlgIdt" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="融资项目所属行业" :class="[handleItemDiff('ssIndustryProject')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-select v-model="formData.ssIndustryProject" :data-allowblank="formData.ssIndustryProjectAllowblank" data-dict="isuOrgBlgIdt" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="项目是否属于重点监控行业和领域" :class="[handleItemDiff('ssMonitoryIndustry')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-select v-model="formData.ssMonitoryIndustry" :data-allowblank="formData.ssMonitoryIndustryAllowblank" data-dict="isTrue" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="重点监控行业和领域类别" :class="[handleItemDiff('ssMonitoryIndustryType')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-select v-model="formData.ssMonitoryIndustryType" data-dict="keyMntIdtTyp" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="重点监控行业和领域类别说明" :class="[handleItemDiff('ssDetailsMonitoryType')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-text v-model="formData.ssDetailsMonitoryType" :data-max-length="256"/>
                   </k-form-item>
                   <k-form-item label="对应资产外部评级" :class="[handleItemDiff('ssInternalAssetRate')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-select v-model="formData.ssInternalAssetRate" :data-allowblank="formData.ssInternalAssetRateAllowblank" data-dict="mainRating" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="担保方式" :class="[handleItemDiff('ssGuaranteeMethod')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-select v-model="formData.ssGuaranteeMethod" data-dict="grntWay" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="抵质押物类型" :class="[handleItemDiff('ssPledgeType')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-select v-model="formData.ssPledgeType" data-dict="plgTyp" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="抵质押物价值（元）" :class="[handleItemDiff('ssPledgeValue')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-text v-model="formData.ssPledgeValue" data-validate-type="money"  data-type="money" data-integer-length="13" data-digits="2" />
                   </k-form-item>
                   <k-form-item label="担保性质" :class="[handleItemDiff('ssGuaranteeType')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-select v-model="formData.ssGuaranteeType" data-dict="grntChr" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="担保人与融资人关系" :class="[handleItemDiff('ssGuarantorType')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-select v-model="formData.ssGuarantorType" data-dict="grntLvrgRel" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="担保情况说明" :class="[handleItemDiff('ssDetailsGuarantee')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-text v-model="formData.ssDetailsGuarantee" :data-allowblank="formData.ssDetailsGuaranteeAllowblank"  :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="是否为债转股" :class="[handleItemDiff('ssDebtEquitySwap')]" v-show="this.formData.AssetDebtRegisterType == 14">
                     <k-field-select v-model="formData.ssDebtEquitySwap" data-dict="subm_isTrue"/>
                   </k-form-item>

                   <!--16 公募基金/私募基金-->
                   <k-form-item label="基金代码" :class="[handleItemDiff('ppFundCode')]" v-show="this.formData.AssetDebtRegisterType == 16">
                     <k-field-text v-model="formData.ppFundCode" :data-allowblank="formData.ppFundCodeAllowblank" :data-max-length="20"/>
                   </k-form-item>
                   <k-form-item label="基金名称" :class="[handleItemDiff('ppFundName')]" v-show="this.formData.AssetDebtRegisterType == 16">
                     <k-field-text v-model="formData.ppFundName" :data-allowblank="formData.ppFundNameAllowblank"  :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="行业" :class="[handleItemDiff('ppIndustry')]" v-show="this.formData.AssetDebtRegisterType == 16">
                     <k-field-select v-model="formData.ppIndustry" :data-allowblank="formData.ppIndustryAllowblank" data-dict="hang_ye"/>
                   </k-form-item>
                   <k-form-item label="登记备案机构" :class="[handleItemDiff('ppRegistAgency')]" v-show="this.formData.AssetDebtRegisterType == 16">
                     <k-field-select v-model="formData.ppRegistAgency" :data-allowblank="formData.ppRegistAgencyAllowblank" data-dict="regTrstOrg" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="是否为固定收益类" :class="[handleItemDiff('ppFixedIncome')]" v-show="this.formData.AssetDebtRegisterType == 16">
                     <k-field-select v-model="formData.ppFixedIncome"  data-dict="istrue" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="是否属于政府投资基金" :class="[handleItemDiff('ppGovernInvestFund')]" v-show="this.formData.AssetDebtRegisterType == 16">
                     <k-field-select v-model="formData.ppGovernInvestFund"  data-dict="isTrue" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="政府投资基金投向" :class="[handleItemDiff('ppDirectGovernFund')]" v-show="this.formData.AssetDebtRegisterType == 16">
                     <k-field-select v-model="formData.ppDirectGovernFund" :data-allowblank="formData.ppDirectGovernFundAllowblank" data-dict="actual_invest_dir_gov_fund" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="是否由金融资产投资公司发行" :class="[handleItemDiff('ppIssuedAssetCompany')]" v-show="this.formData.AssetDebtRegisterType == 16">
                     <k-field-select v-model="formData.ppIssuedAssetCompany" :data-allowblank="formData.ppIssuedAssetCompanyAllowblank" data-dict="isTrue" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="基金公司名称" :class="[handleItemDiff('ppTaName')]" v-show="this.formData.AssetDebtRegisterType == 16">
                     <k-field-text v-model="formData.ppTaName"  :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="基金管理机构名称" :class="[handleItemDiff('ppManagerFundName')]" v-show="this.formData.AssetDebtRegisterType == 16">
                     <k-field-text v-model="formData.ppManagerFundName" :data-allowblank="formData.ppManagerFundNameAllowblank"  :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="基金托管机构名称" :class="[handleItemDiff('ppCustodianFundName')]" v-show="this.formData.AssetDebtRegisterType == 16">
                     <k-field-text v-model="formData.ppCustodianFundName" :data-allowblank="formData.ppCustodianFundNameAllowblank"  :data-max-length="200"/>
                   </k-form-item>
                   <k-form-item label="投资阶段" :class="[handleItemDiff('ppInvestStage')]" v-show="this.formData.AssetDebtRegisterType == 16">
                     <k-field-select v-model="formData.ppInvestStage" data-dict="subm_invest_stage"/>
                   </k-form-item>
                   <k-form-item label="投资企业类型(按规模划分)" :class="[handleItemDiff('ppEnterTypeScale')]" v-show="this.formData.AssetDebtRegisterType == 16">
                     <k-field-select v-model="formData.ppEnterTypeScale" data-dict="instituteTypeTech" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="投资企业类型(按技术领域划分)" :class="[handleItemDiff('ppEnterTypeTech')]" v-show="this.formData.AssetDebtRegisterType == 16">
                     <k-field-select v-model="formData.ppEnterTypeTech" data-dict="isuOrgTypTchno" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="投资企业类型(按经济类型划分)" :class="[handleItemDiff('ppEnterTypeEconomic')]" v-show="this.formData.AssetDebtRegisterType == 16">
                     <k-field-select v-model="formData.ppEnterTypeEconomic"  data-dict="isuOrgTypEcn" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="基金投资资产" :class="[handleItemDiff('ppInvestAssets')]" v-show="this.formData.AssetDebtRegisterType == 16">
                     <k-field-text v-model="formData.ppInvestAssets" :data-allowblank="formData.ppInvestAssetsAllowblank" :data-max-length="400"/>
                   </k-form-item>

                   <k-form-item label="名称" :class="[handleItemDiff('hhName')]" v-show="this.formData.AssetDebtRegisterType == 7">
                     <k-field-text v-model="formData.hhName" :data-allowblank="formData.hhNameAllowblank" :data-max-length="400"/>
                   </k-form-item>
                   <k-form-item label="名义本金" :class="[handleItemDiff('hhNominalPrincipal')]" v-show="this.formData.AssetDebtRegisterType == 7">
                     <k-field-text v-model="formData.hhNominalPrincipal" :data-allowblank="formData.hhNominalPrincipalAllowblank" data-validate-type="number"  data-type="number" data-integer-length="13" data-digits="2"/>
                   </k-form-item>
                   <k-form-item label="标的类别" :class="[handleItemDiff('hhUnderAssetType')]" v-show="this.formData.AssetDebtRegisterType == 7" >
                     <k-field-select v-model="formData.hhUnderAssetType" :data-allowblank="formData.hhUnderAssetTypeAllowblank" :data-max-length="400" data-dict="stru_deposit_type" data-dict-type="1"/>
                   </k-form-item>
                   <k-form-item label="持有目的" :class="[handleItemDiff('hhHoldObjective')]" v-show="this.formData.AssetDebtRegisterType == 7">
                     <k-field-select v-model="formData.hhHoldObjective" :data-allowblank="formData.hhHoldObjectiveAllowblank" :data-max-length="400" data-dict="subm_holding_obj"/>
                   </k-form-item>

                    <k-form-item label="所属国家或地区" :class="[handleItemDiff('kkCountry')]" v-show="this.formData.AssetDebtRegisterType == 10">
                     <k-field-select v-model="formData.kkCountry" :data-allowblank="formData.kkCountryAllowblank"  data-dict="tr_iss_country"  :data-max-length="400"/>
                   </k-form-item>
                   <k-form-item label="股票/基金代码" :class="[handleItemDiff('kkIdentCode')]" v-show="this.formData.AssetDebtRegisterType == 10">
                     <k-field-text v-model="formData.kkIdentCode" :data-allowblank="formData.kkIdentCodeAllowblank" :data-max-length="20"/>
                   </k-form-item>
                   <k-form-item label="股票/基金名称" :class="[handleItemDiff('kkName')]" v-show="this.formData.AssetDebtRegisterType == 10">
                     <k-field-text v-model="formData.kkName" :data-allowblank="formData.kkNameAllowblank" :data-max-length="200" />
                   </k-form-item>
                   <k-form-item label="发行机构" :class="[handleItemDiff('kkIssuer')]" v-show="this.formData.AssetDebtRegisterType == 10">
                     <k-field-text v-model="formData.kkIssuer" :data-allowblank="formData.kkIssuerAllowblank" :data-max-length="200" />
                   </k-form-item>
                   <k-form-item label="行业" :class="[handleItemDiff('kkIndustry')]" v-show="this.formData.AssetDebtRegisterType == 10">
                     <k-field-select v-model="formData.kkIndustry" :data-allowblank="formData.kkIndustryAllowblank" :data-max-length="400" data-dict="subm_isuOrgBlgIdt" />
                   </k-form-item>

                  <k-form-item label="所属国家或地区" :class="[handleItemDiff('llCountry')]" v-show="this.formData.AssetDebtRegisterType == 11">
                     <k-field-select v-model="formData.llCountry" :data-allowblank="formData.llCountryAllowblank"  data-dict="tr_iss_country"  :data-max-length="400"/>
                   </k-form-item>
                   <k-form-item label="合约名称" :class="[handleItemDiff('llContractName')]" v-show="this.formData.AssetDebtRegisterType == 11">
                     <k-field-text v-model="formData.llContractName" :data-allowblank="formData.llContractNameAllowblank" :data-max-length="400"/>
                   </k-form-item>
                   <k-form-item label="起息日" :class="[handleItemDiff('llValueDate')]" v-show="this.formData.AssetDebtRegisterType == 11">
                     <k-field-date v-model="formData.llValueDate" :data-allowblank="formData.llValueDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
                   </k-form-item>
                   <k-form-item label="到期日" :class="[handleItemDiff('llMaturityDate')]" v-show="this.formData.AssetDebtRegisterType == 11">
                     <k-field-date v-model="formData.llMaturityDate" :data-allowblank="formData.llMaturityDateAllowblank" data-date-format="yyyyMMdd" data-value-format="yyyyMMdd"/>
                   </k-form-item>
				          <k-form-item label="票面利率%" :class="[handleItemDiff('llCouponRate')]" v-show="this.formData.AssetDebtRegisterType == 11">
                     <k-field-text v-model="formData.llCouponRate" data-validate-type="number" data-type="number"   data-integer-length="3" data-digits="5"/>
                   </k-form-item>
                   <k-form-item label="付息频率(个月/次)" :class="[handleItemDiff('llInterestFrequency')]" v-show="this.formData.AssetDebtRegisterType == 11">
                     <k-field-text v-model="formData.llInterestFrequency" :data-allowblank="formData.llInterestFrequencyAllowblank" data-validate-type="number"  data-type="number" :data-max-length="2" data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
				          <k-form-item label="固定收益部分所占比例%" :class="[handleItemDiff('llPercentFix')]" v-show="this.formData.AssetDebtRegisterType == 11">
                     <k-field-text v-model="formData.llPercentFix" data-validate-type="number" data-type="number"   data-integer-length="3" data-digits="5"/>
                   </k-form-item>
				          <k-form-item label="衍生金融工具所占比例%" :class="[handleItemDiff('llPercentDerivate')]" v-show="this.formData.AssetDebtRegisterType == 11">
                     <k-field-text v-model="formData.llPercentDerivate" data-validate-type="number" data-type="number"   data-integer-length="3" data-digits="5"/>
                   </k-form-item>
                   <k-form-item label="衍生金融工具具体投资方式" :class="[handleItemDiff('llDerivateInvetType')]" v-show="this.formData.AssetDebtRegisterType == 11">
                     <k-field-text v-model="formData.llDerivateInvetType" :data-allowblank="formData.llDerivateInvetTypeAllowblank" :data-max-length="400" />
                   </k-form-item>
                   <k-form-item label="衍生金融工具挂钩的标的资产" :class="[handleItemDiff('llUnderAsset')]" v-show="this.formData.AssetDebtRegisterType == 11">
                     <k-field-text v-model="formData.llUnderAsset" :data-allowblank="formData.llUnderAssetAllowblank" :data-max-length="400" />
                   </k-form-item>
                   <k-form-item label="利息结算方式" :class="[handleItemDiff('llDetailsProceeds')]" v-show="this.formData.AssetDebtRegisterType == 11">
                     <k-field-text v-model="formData.llDetailsProceeds" :data-allowblank="formData.llDetailsProceedsAllowblank" :data-max-length="400" />
                   </k-form-item>
                   <k-form-item label="含权情况说明" :class="[handleItemDiff('llDetailsOption')]" v-show="this.formData.AssetDebtRegisterType == 11">
                     <k-field-text v-model="formData.llDetailsOption" :data-allowblank="formData.llDetailsOptionAllowblank" :data-max-length="400" />
                   </k-form-item>
				            <k-form-item label="结构性票据最高收益率%" :class="[handleItemDiff('llMaxNoteReturn')]" v-show="this.formData.AssetDebtRegisterType == 11">
                     <k-field-text v-model="formData.llMaxNoteReturn" data-validate-type="number" data-type="number"   data-integer-length="3" data-digits="5"/>
                   </k-form-item>
				           <k-form-item label="结构性票据最低收益率%" :class="[handleItemDiff('llMinNoteReturn')]" v-show="this.formData.AssetDebtRegisterType == 11">
                     <k-field-text v-model="formData.llMinNoteReturn" data-validate-type="number" data-type="number"   data-integer-length="3" data-digits="5"/>
                   </k-form-item>
				           <k-form-item label="挂钩标的资产基准价格" :class="[handleItemDiff('llStrikeUnderAsset')]" v-show="this.formData.AssetDebtRegisterType == 11">
                     <k-field-text v-model="formData.llStrikeUnderAsset" data-validate-type="number" data-type="number"   data-integer-length="15" data-digits="2" data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
				           <k-form-item label="挂钩标的资产登记日价格" :class="[handleItemDiff('llUnderRgPrice')]" v-show="this.formData.AssetDebtRegisterType == 11">
                     <k-field-text v-model="formData.llUnderRgPrice" data-validate-type="number" data-type="number"   data-integer-length="15" data-digits="2" data-min-value="0"  data-regx-text="请输入大于或等于0的数字"/>
                   </k-form-item>
                   <k-form-item label="交易费" :class="[handleItemDiff('llTransCosts')]" v-show="this.formData.AssetDebtRegisterType == 11">
                     <k-field-text v-model="formData.llTransCosts" :data-allowblank="formData.llTransCostsAllowblank" :data-max-length="400" />
                   </k-form-item>

                    <!--<k-form-item label="理论报送起始日期"  v-show="this.formData.AssetDebtRegisterType == '1' || this.formData.AssetDebtRegisterType == '2' || this.formData.AssetDebtRegisterType == '3' || this.formData.AssetDebtRegisterType == '4' || this.formData.AssetDebtRegisterType == '6' || this.formData.AssetDebtRegisterType == '12' || this.formData.AssetDebtRegisterType == '14' || this.formData.AssetDebtRegisterType == '16' || this.formData.AssetDebtRegisterType == '17' ">
                     <k-field-date v-model="formData.theoryReportStartDate" />
                   </k-form-item>-->
        <k-form-footer slot="footer" data-align="center">
          <k-btn class="btn-custom-primary" ref="sumbitedit"   data-from="editAssetDebtRegisterInfoForm"
                 :data-model="formData" data-target="assetDebtRegisterInfoGrid" :data-handler="sumbit_edit" :handle-before="handleBeforeUpdate">
            <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
          </k-btn>
          <k-btn class="btn-custom-plain" data-functype="CLOSE">
            <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
        </k-form-footer>
      </k-form>
    </k-popup>

       <k-popup ref="uploadAssetDebtRegisterInfoPopup" title="报送数据导入" @data-opened="uploadOpened()">
            <k-form ref="addForm" data-ui="element">
              <k-form-item label="数据日期">
                <k-field-date v-model="uploadBeginDate"   data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd" :data-allowblank="false"/>
              </k-form-item>
              <!--<k-form-item label="至">
                <k-field-date v-model="uploadQueryDate"   data-date-format="yyyyMMdd" data-value-format="yyyyMMdd" :data-allowblank="false"/>
              </k-form-item>-->
              <k-form-item label="报送数据导入" data-ui="element" data-input-width="500px">
                <k-field-excel-upload data-type="file" ref="uploadRef" :data-multiple="false" :data-limit='1'
                    data-accept=".xlsx,.xls"
                    :data-error="onSubmitError" :data-success="onSubmitSuccess"
                    :data-auto-upload="false"
                    data-upload-url="upload/server/RptApp/reportManage/assetDebtRegistImport.json">
                </k-field-excel-upload>
              </k-form-item>
              <k-form-footer data-align="center">
                <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-target="prodRegistFilingInfoGrid" ref="submitBtn"
                      :data-auto-upload="false" data-from="addForm" :data-handler="submitUploadParam">
                  <i v-show="this.showSubmitBtn=='0'" class="icon-confirm"/>确定
                </k-btn>
                <k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
              </k-form-footer>
            </k-form>
            </k-popup>
            <k-popup ref="auditInfoPopup">
              <k-form ref="updateAuditStatusForm" :data-col="2" isFormBodyScreen>
                 <k-form-item label="报表类型">
                   <k-field-text v-model="infoPop.tableName" :data-allowblank="false" data-disabled="true"/>
                 </k-form-item>
                 <k-form-item label="数据日期" data-label-width="100px">
                   <k-field-date v-model="queryParamDateRange" data-type="daterange" data-date-format="yyyy-MM-dd"
                                 data-value-format="yyyyMMdd" :data-allowblank="false"/>
                 </k-form-item>
                 <k-form-item label="复核状态">
                   <k-field-select v-model="infoPop.auditStatus" data-dict="xp_disclosure_check_status" data-default-value="1" data-disabled="true"/>
                 </k-form-item>
                 <k-form-footer slot="footer" data-align="center">
                   <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-from="updateAuditStatusForm" data-target="prodIssuanceRegistInfoGrid"
                          @click="audit" :data-model="infoPop"><md-icon md-src="/static/svg/confirm.svg"></md-icon>确定</k-btn>
                   <k-btn class="btn-custom-plain" data-functype="CLOSE"><md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
                 </k-form-footer>
              </k-form>
            </k-popup>
  </div>
</template>

<script>
import KFieldExcelUpload from "@/components/k-element/k-field-excel-upload/k-field-excel-upload";
// import KFieldCheckboxParam from "@/pages/design/components/param/KFieldCheckboxParam.vue";
import Tools from "@/utils/tools";
import ProdMixin from "@/pages/zz/manage/mixins/prodMixin.js";

export default {
  name: "AssetDebtRegisterInfo",
  components: {KFieldExcelUpload},
  mixins: [ProdMixin],
  data() {
    return {
      formData: {assDebtTypeDict:''},
      formDataCopy: {},
      selectRowData: {},
      searchParam:{},
      RegisterDate:[],
      dataDate: [],
      queryParamDateRange: [],
      uploadBeginDate: '',
      uploadQueryDate: '',

      infoPop: {},
      auditInfoPopupData: {
        tableId: 'app_asset_debt_register_info',
        tableName: '资产要素登记管理'
      },
      showSubmitBtn: 0,
      abnormalAction: "AssetDebtRegisterInfo.getAbnormalData",
      updateStatusAction: "AssetDebtRegisterInfo.updateAssetDebtRegisterInfoStatus",
      comfirmExportParam:{}
    };
  },
  created() {
     this.$set(this.formData, 'assDebtTypeDict', '');
  },
   computed: {
      queryParam() {
        return {
          'queryStartDate': this.dataDate ? this.dataDate[0] : null,
          'queryEndDate': this.dataDate ? this.dataDate[1] : null,
          // 'queryStartDate': this.queryParamDateRange ? this.queryParamDateRange[0] : null,
          // 'queryEndDate': this.queryParamDateRange ? this.queryParamDateRange[1] : null,
          'assetCode': this.searchParam.assetCode,
          'assDebtType': this.searchParam.assDebtType,
          'registerStatus': this.searchParam.registerStatus,
          'dataChangeType': this.searchParam.dataChangeType,
          'theoryReportStartDate': this.searchParam.theoryReportStartDate
        }
      }
    },
  methods: {
   setConfirmExportParam() {
            this.comfirmExportParam = {
               'queryStartDate': this.dataDate ? this.dataDate[0] : null,
               'queryEndDate': this.dataDate ? this.dataDate[1] : null,
              //  'queryStartDate': this.searchParam.queryStartDate,
              //  'queryEndDate': this.searchParam.queryEndDate,
               'assetCode': this.searchParam.assetCode,
               'assDebtType': this.searchParam.assDebtType,
               'registerStatus': this.searchParam.registerStatus,
               'dataChangeType': this.searchParam.dataChangeType,
               'theoryReportStartDate': this.searchParam.theoryReportStartDate
            };
    },
    handleBeforeUpdate() {
      if (this.formNoChangeCb()) {
        this.$refs.editAssetDebtRegisterInfoPopup.close();
        return false
      }
      return true
    },
    sumbit_edit(){
          if(this.formData.AssetDebtRegisterType=='5' || this.formData.AssetDebtRegisterType=='8'|| this.formData.AssetDebtRegisterType=='9' || this.formData.AssetDebtRegisterType=='13' || this.formData.AssetDebtRegisterType=='15' || this.formData.AssetDebtRegisterType=='17' || this.formData.AssetDebtRegisterType=='18'){
                        Tools.alert("该业务不支持数据录入！","danger");
                        return false;
          }
          this.$refs.sumbitedit.setIconStyle(0,[]);
          if(this.$refs.editAssetDebtRegisterInfoForm.validate()){
               this.httpUtil.query({
                       url: 'server/json/RptApp/audit/checkassetDebtRegisterInfo.json',
                       params:  this.formData
                                }).then(res => {
                                  if(res.success) {
                                   this.httpUtil.comnUpdate({
                                            action: 'AssetDebtRegisterInfo.updateAssetDebtRegisterInfo',
                                            params:  this.formData
                                             }).then(res => {
                                              if(res.success) {
                                              this.$refs.editAssetDebtRegisterInfoPopup.close();
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
           if(res.returnmsg=='存在指标校验未通过数据'){
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
                     this.$refs.assetDebtRegisterInfoGrid.load(this.searchParam);
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
                 this.$refs.assetDebtRegisterInfoGrid.load(this.searchParam);
                 this.$refs.auditInfoPopup.close();
                }
              })
           }
         }
       })
    },
  submitUploadParam() {
        //文件上传校验
        let validate = this.$refs.addForm.validate();
        if (validate) {
          let formData = { beginDate: this.uploadBeginDate, queryDate: this.uploadBeginDate};
          let temp = document.getElementsByClassName('upload-demo');
          let lis = temp[0].childNodes[1].childNodes.length;
          if (lis > 0) {
            this.$refs.uploadRef.upload(formData);
          } else {
            this.$message.error("上传文件不能为空!");
            return false;
          }
        }
      },
    popupEdit(row){
      let pathUrl = '/main/zz/errorInfo/AssetDebtRegisterInfoErr';
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
    onSubmitError() {
      this.$refs.uploadRef.doReset();
       this.showSubmitBtn = '0'
     },
     onSubmitSuccess() {
          this.$refs.uploadRef.doReset();
          this.$refs.addForm.reset();
          this.$refs.uploadAssetDebtRegisterInfoPopup.close();
          this.$refs.assetDebtRegisterInfoGrid.load(this.searchParam);
        },
     submitCheck() {
         if(this.formData.AssetDebtRegisterType=='5' || this.formData.AssetDebtRegisterType=='8'|| this.formData.AssetDebtRegisterType=='9' || this.formData.AssetDebtRegisterType=='13' || this.formData.AssetDebtRegisterType=='15' || this.formData.AssetDebtRegisterType=='17' || this.formData.AssetDebtRegisterType=='18'){
            Tools.alert("该业务不支持数据录入！","danger");
            return false;
         }else{
            return true;
         }

     },
     uploadOpened() {
         this.uploadBeginDate = ''
         this.uploadQueryDate = ''
       },
    // 复核弹窗
    auditPopup() {
      this.infoPop = this.auditInfoPopupData;
      this.httpUtil.sysDate().then(res=>{
        if (res) {
          this.queryParamDateRange = [res,res];
        }
      });
      this.$refs.auditInfoPopup.popup();
    },
     editOpened(){
        if(this.formData.assDebtType=='1002' || this.formData.assDebtType=='1003' || this.formData.assDebtType=='1004' || this.formData.assDebtType=='1005' ){
             this.$set(this.formData, 'AssetDebtRegisterType', '1');
        }else if(this.formData.assDebtType=='1101' || this.formData.assDebtType=='1110' || this.formData.assDebtType=='1109' || this.formData.assDebtType=='1102' || this.formData.assDebtType=='1111'
              || this.formData.assDebtType=='1112' || this.formData.assDebtType=='1113' || this.formData.assDebtType=='1114' || this.formData.assDebtType=='1115' || this.formData.assDebtType=='1116'
              || this.formData.assDebtType=='1117' || this.formData.assDebtType=='2101' || this.formData.assDebtType=='2501' || this.formData.assDebtType=='2502') {
              this.$set(this.formData, 'AssetDebtRegisterType', '2');
         }else if(this.formData.assDebtType=='1107' || this.formData.assDebtType=='1108' || this.formData.assDebtType=='1901' || this.formData.assDebtType=='1902') {
               this.$set(this.formData, 'AssetDebtRegisterType', '3');
         }else if(this.formData.assDebtType=='1202' || this.formData.assDebtType=='1203' || this.formData.assDebtType=='1204' || this.formData.assDebtType=='1205' || this.formData.assDebtType=='1206'
               || this.formData.assDebtType=='1207' || this.formData.assDebtType=='1208' || this.formData.assDebtType=='1209' || this.formData.assDebtType=='1211' || this.formData.assDebtType=='1212'
               || this.formData.assDebtType=='1213' || this.formData.assDebtType=='2202') {
               this.$set(this.formData, 'AssetDebtRegisterType', '4');
         }else if(this.formData.assDebtType=='1301' || this.formData.assDebtType=='1302' || this.formData.assDebtType=='1305') {
               this.$set(this.formData, 'AssetDebtRegisterType', '6');
         }else if(this.formData.assDebtType=='1701' || this.formData.assDebtType=='1702' || this.formData.assDebtType=='1703' || this.formData.assDebtType=='1704' || this.formData.assDebtType=='1705'
              || this.formData.assDebtType=='1706') {
              this.$set(this.formData, 'AssetDebtRegisterType', '12');
         }else if(this.formData.assDebtType=='1299' || this.formData.assDebtType=='1399' || this.formData.assDebtType=='1599' || this.formData.assDebtType=='2299') {
              this.$set(this.formData, 'AssetDebtRegisterType', '14');
         }else if(this.formData.assDebtType=='1106' || this.formData.assDebtType=='2401' || this.formData.assDebtType=='1303' || this.formData.assDebtType=='2403' || this.formData.assDebtType=='2402'
              || this.formData.assDebtType=='2499' || this.formData.assDebtType=='2601' || this.formData.assDebtType=='2602' || this.formData.assDebtType=='2604' || this.formData.assDebtType=='2605'
              || this.formData.assDebtType=='2606' || this.formData.assDebtType=='2607' || this.formData.assDebtType=='2610' || this.formData.assDebtType=='2611' || this.formData.assDebtType=='2612'
              || this.formData.assDebtType=='2617' || this.formData.assDebtType=='2613' || this.formData.assDebtType=='2614' || this.formData.assDebtType=='2616' || this.formData.assDebtType=='2618'
              || this.formData.assDebtType=='2619' || this.formData.assDebtType=='2620' || this.formData.assDebtType=='2699') {
              this.$set(this.formData, 'AssetDebtRegisterType', '16');
         }else if(this.formData.assDebtType=='1401' || this.formData.assDebtType=='1402'|| this.formData.assDebtType=='1403'
         || this.formData.assDebtType=='1404'|| this.formData.assDebtType=='1405'|| this.formData.assDebtType=='1406'|| this.formData.assDebtType=='1499') {
              this.$set(this.formData, 'AssetDebtRegisterType', '7');
         }else if(this.formData.assDebtType=='1505' ) {
              this.$set(this.formData, 'AssetDebtRegisterType', '10');
         }else if(this.formData.assDebtType=='1503' ) {
              this.$set(this.formData, 'AssetDebtRegisterType', '11');
         }else if(this.formData.assDebtType=='1507' ) {
              this.$set(this.formData, 'AssetDebtRegisterType', '10');
         }
        this.findAssDebtTypeDict();
        this.getAllowblankValue();
        this.formDataCopy = Object.assign({}, this.formData)
        this.formData.oldData=Tools.json2str(this.formData);
     },
      findAssDebtTypeDict() {
           this.httpUtil.comnQuery({
             action: "AssetDebtRegisterInfo.findAssDebtTypeDict",
             params: {assetDebtRegisterType: this.formData.AssetDebtRegisterType}
           }).then(data => {
             this.$set(this.formData, 'assDebtTypeDict', '');
             this.formData.assDebtTypeDict = data.rows;
             this.formDataCopy.assDebtTypeDict = data.rows;
           }).catch({})
         },
      dataOnChange(){
        this.findAssDebtTypeDict();
        this.getAllowblankValue();
      },

      getAllowblankValue(){
        this.$set(this.formData, 'detailsAllowblank', true);
        this.$set(this.formData, 'bbDepositAmtAllowblank', true);
        this.$set(this.formData, 'bbValueDateAllowblank', true);
        this.$set(this.formData, 'bbMaturityDateAllowblank', true);
        this.$set(this.formData, 'bbAnnualRateAllowblank', true);
        this.$set(this.formData, 'bbInterestBasisAllowblank', true);
        //2
         this.$set(this.formData, 'ccIdentCodeAllowblank', true);
         this.$set(this.formData, 'ccNameAllowblank', true);
         this.$set(this.formData, 'ccIssModeBondAllowblank', true);
         this.$set(this.formData, 'ccIssRatePartAllowblank', true);
         this.$set(this.formData, 'ccInstituteTypeScaleAllowblank', true);
         this.$set(this.formData, 'ccInstituteTypeTechAllowblank', true);
         this.$set(this.formData, 'ccInstituteTypeEconomicAllowblank', true);
         this.$set(this.formData, 'ccIndustryIssuerAllowblank', true);
         this.$set(this.formData, 'ccRegistDepositAllowblank', true);
         //3
         this.$set(this.formData, 'ddValueDateAllowblank', true);
         this.$set(this.formData, 'ddMaturityDateAllowblank', true);
         this.$set(this.formData, 'ddCounterpartyAllowblank', true);
         this.$set(this.formData, 'ddCounterpartyTypeAllowblank', true);
         this.$set(this.formData, 'ddAnnalInterestRateAllowblank', true);
         this.$set(this.formData, 'ddInterestBasisAllowblank', true);
         this.$set(this.formData, 'ddCollateralTypeAllowblank', true);
         //4
         this.$set(this.formData, 'eeBuybackAllowblank', true);
         this.$set(this.formData, 'eeNameAllowblank', true);
         this.$set(this.formData, 'eeAssetCodeAllowblank', true);
         this.$set(this.formData, 'eeAmtAllowblank', true);
         this.$set(this.formData, 'eeUnitParValueAllowblank', true);
         this.$set(this.formData, 'eeValueDateAllowblank', true);
         this.$set(this.formData, 'eeMaturityDateAllowblank', true);
         this.$set(this.formData, 'eeExpectedReturnAllowblank', true);
         this.$set(this.formData, 'eeInterestPayFrequencyAllowblank', true);
         this.$set(this.formData, 'eeDetailPrincInterestAllowblank', true);
         this.$set(this.formData, 'eeInterestBasisAllowblank', true);
         this.$set(this.formData, 'eeDebtorAllowblank', true);
         this.$set(this.formData, 'eeDeptorRateAllowblank', true);
         this.$set(this.formData, 'eeDebtorTypeScaleAllowblank', true);
         this.$set(this.formData, 'eeDebtorTypeTechAllowblank', true);
         this.$set(this.formData, 'eeDebtorTypeEconomicAllowblank', true);
         this.$set(this.formData, 'eeProjectAllowblank', true);
         this.$set(this.formData, 'eeIndustryDebtorAllowblank', true);
         this.$set(this.formData, 'eeMonitorIndusTypeAllowblank', true);
         this.$set(this.formData, 'eeDebtorRateAllowblank', true);
         this.$set(this.formData, 'eeInterAssetRateAllowblank', true);
         this.$set(this.formData, 'eeOutAssetRateAllowblank', true);
         this.$set(this.formData, 'eeOptionRightAllowblank', true);
         this.$set(this.formData, 'eeDetailsExerciseTermAllowblank', true);
         this.$set(this.formData, 'eeRegionDebtorAllowblank', true);
         this.$set(this.formData, 'eeOrganizationCodeAllowblank', true);
         this.$set(this.formData, 'eeTotalFeeRateAllowblank', true);
         this.$set(this.formData, 'eeIndustryProjectAllowblank', true);
         //6
          this.$set(this.formData, 'ggNameAllowblank', true);
          this.$set(this.formData, 'ggIndustryAllowblank', true);
          this.$set(this.formData, 'ggEnterTypeScaleAllowblank', true);
          this.$set(this.formData, 'ggEnterTypeTechAllowblank', true);
          this.$set(this.formData, 'ggEnterTypeEconomicAllowblank', true);
          this.$set(this.formData, 'ggPledgedFinaceAllowblank', true);
          //12
           this.$set(this.formData, 'mmManagePlanNameAllowblank', true);
           this.$set(this.formData, 'mmManageProductAllowblank', true);
           this.$set(this.formData, 'mmProductCodeAllowblank', true);
           this.$set(this.formData, 'mmPlanIssuerCodeAllowblank', true);
           this.$set(this.formData, 'mmAssetPlanRgCodeAllowblank', true);
           this.$set(this.formData, 'mmManagerAllowblank', true);
           this.$set(this.formData, 'mmCustodianAllowblank', true);
           this.$set(this.formData, 'mmAmtAllowblank', true);
           this.$set(this.formData, 'mmActualDirectAllowblank', true);
           this.$set(this.formData, 'mmDetailsInvestAllowblank', true);
           this.$set(this.formData, 'mmIndustryInvestAllowblank', true);
           this.$set(this.formData, 'mmPlanStartDateAllowblank', true);
           this.$set(this.formData, 'mmPlanMaturityDateAllowblank', true);
           this.$set(this.formData, 'mmPlanTypeAllowblank', true);
           this.$set(this.formData, 'mmExpectedReturnAllowblank', true);
           this.$set(this.formData, 'mmInvestStructureAllowblank', true);
           this.$set(this.formData, 'mmManagerTypeAllowblank', true);
           this.$set(this.formData, 'mmManagerFeeRateAllowblank', true);
           this.$set(this.formData, 'mmCustodianFeeRateAllowblank', true);
           this.$set(this.formData, 'mmTransCostRateAllowblank', true);
           this.$set(this.formData, 'mmInterFeeRateAllowblank', true);
           this.$set(this.formData, 'mmOtherExpenseRateAllowblank', true);
           //14
           this.$set(this.formData, 'ssNameAllowblank', true);
           this.$set(this.formData, 'ssAssetTypeAllowblank', true);
           this.$set(this.formData, 'ssAmtAllowblank', true);
           this.$set(this.formData, 'ssValueDateAllowblank', true);
           this.$set(this.formData, 'ssMaturityDateAllowblank', true);
           this.$set(this.formData, 'ssCountryAllowblank', true);
           this.$set(this.formData, 'ssExpectedReturnAllowblank', true);
           this.$set(this.formData, 'ssInterestFrequencyAllowblank', true);
           this.$set(this.formData, 'ssDebtorAllowblank', true);
           this.$set(this.formData, 'ssOrganCodeAllowblank', true);
           this.$set(this.formData, 'ssDebtorTypeScaleAllowblank', true);
           this.$set(this.formData, 'ssDebtorTypeTechAllowblank', true);
           this.$set(this.formData, 'ssDebtorTypeEconomicAllowblank', true);
           this.$set(this.formData, 'ssProjectAllowblank', true);
           this.$set(this.formData, 'ssIndustryDebtorAllowblank', true);
           this.$set(this.formData, 'ssIndustryProjectAllowblank', true);
           this.$set(this.formData, 'ssMonitoryIndustryAllowblank', true);
           this.$set(this.formData, 'ssInternalAssetRateAllowblank', true);
           this.$set(this.formData, 'ssDetailsGuaranteeAllowblank', true);
           //16
           this.$set(this.formData, 'ppFundCodeAllowblank', true);
           this.$set(this.formData, 'ppFundNameAllowblank', true);
           this.$set(this.formData, 'ppIndustryAllowblank', true);
           this.$set(this.formData, 'ppRegistAgencyAllowblank', true);
           this.$set(this.formData, 'ppDirectGovernFundAllowblank', true);
           this.$set(this.formData, 'ppIssuedAssetCompanyAllowblank', true);
           this.$set(this.formData, 'ppManagerFundNameAllowblank', true);
           this.$set(this.formData, 'ppCustodianFundNameAllowblank', true);
           this.$set(this.formData, 'ppInvestAssetsAllowblank', true);

        if(this.formData.AssetDebtRegisterType == '1'){
            this.$set(this.formData, 'bbDepositAmtAllowblank', false);
            this.$set(this.formData, 'bbValueDateAllowblank', false);
            this.$set(this.formData, 'bbMaturityDateAllowblank', false);
            this.$set(this.formData, 'bbAnnualRateAllowblank', false);
            this.$set(this.formData, 'bbInterestBasisAllowblank', false);
       }else if(this.formData.AssetDebtRegisterType == '2'){
            this.$set(this.formData, 'ccIdentCodeAllowblank', false);
            this.$set(this.formData, 'ccNameAllowblank', false);
           this.$set(this.formData, 'ccIssModeBondAllowblank', false);
           this.$set(this.formData, 'ccIssRatePartAllowblank', false);
           this.$set(this.formData, 'ccInstituteTypeScaleAllowblank', false);
           this.$set(this.formData, 'ccInstituteTypeTechAllowblank', false);
           this.$set(this.formData, 'ccInstituteTypeEconomicAllowblank', false);
           this.$set(this.formData, 'ccIndustryIssuerAllowblank', false);
           this.$set(this.formData, 'ccRegistDepositAllowblank', false);
       }else if(this.formData.AssetDebtRegisterType == '3'){
         this.$set(this.formData, 'ddValueDateAllowblank', false);
         this.$set(this.formData, 'ddMaturityDateAllowblank', false);
         this.$set(this.formData, 'ddCounterpartyAllowblank', false);
         this.$set(this.formData, 'ddCounterpartyTypeAllowblank', false);
         this.$set(this.formData, 'ddAnnalInterestRateAllowblank', false);
         this.$set(this.formData, 'ddInterestBasisAllowblank', false);
         this.$set(this.formData, 'ddCollateralTypeAllowblank', false);
       }else if(this.formData.AssetDebtRegisterType == '4'){
         this.$set(this.formData, 'eeBuybackAllowblank', false);
         this.$set(this.formData, 'eeNameAllowblank', false);
         this.$set(this.formData, 'eeAssetCodeAllowblank', false);
         this.$set(this.formData, 'eeAmtAllowblank', false);
         this.$set(this.formData, 'eeUnitParValueAllowblank', false);
         this.$set(this.formData, 'eeValueDateAllowblank', false);
         this.$set(this.formData, 'eeMaturityDateAllowblank', false);
         this.$set(this.formData, 'eeExpectedReturnAllowblank', false);
         this.$set(this.formData, 'eeInterestPayFrequencyAllowblank', false);
         this.$set(this.formData, 'eeDetailPrincInterestAllowblank', false);
         this.$set(this.formData, 'eeInterestBasisAllowblank', false);
         this.$set(this.formData, 'eeDebtorAllowblank', false);
         this.$set(this.formData, 'eeDeptorRateAllowblank', false);
         this.$set(this.formData, 'eeDebtorTypeScaleAllowblank', false);
         this.$set(this.formData, 'eeDebtorTypeTechAllowblank', false);
         this.$set(this.formData, 'eeDebtorTypeEconomicAllowblank', false);
         this.$set(this.formData, 'eeProjectAllowblank', false);
         this.$set(this.formData, 'eeIndustryDebtorAllowblank', false);
         this.$set(this.formData, 'eeMonitorIndusTypeAllowblank', false);
         this.$set(this.formData, 'eeDebtorRateAllowblank', false);
         this.$set(this.formData, 'eeInterAssetRateAllowblank', false);
         this.$set(this.formData, 'eeOutAssetRateAllowblank', false);
         this.$set(this.formData, 'eeOptionRightAllowblank', false);
         this.$set(this.formData, 'eeDetailsExerciseTermAllowblank', false);
         this.$set(this.formData, 'eeRegionDebtorAllowblank', false);
         this.$set(this.formData, 'eeOrganizationCodeAllowblank', false);
         this.$set(this.formData, 'eeTotalFeeRateAllowblank', false);
         this.$set(this.formData, 'eeIndustryProjectAllowblank', false);
         if(this.formData.assDebtType=='2202'){
                this.$set(this.formData, 'eeAssetCodeAllowblank', false);
            }else{
                this.$set(this.formData, 'eeAssetCodeAllowblank', true);
                this.$set(this.formData, 'eeAssetCode', "");
         }
       }else if(this.formData.AssetDebtRegisterType == '6'){
          this.$set(this.formData, 'ggNameAllowblank', false);
          this.$set(this.formData, 'ggIndustryAllowblank', false);
          this.$set(this.formData, 'ggEnterTypeScaleAllowblank', false);
          this.$set(this.formData, 'ggEnterTypeTechAllowblank', false);
          this.$set(this.formData, 'ggEnterTypeEconomicAllowblank', false);
          this.$set(this.formData, 'ggPledgedFinaceAllowblank', false);

          this.$set(this.formData, 'ggEquityOutDateAllowblank', false);
          this.$set(this.formData, 'ggInvestStageAllowblank', false);

          this.$set(this.formData, 'ggStockCodeAllowblank', true);
          if(this.formData.assDebtType=='1302'||this.formData.assDebtType=='1305'){
                this.$set(this.formData, 'ggStockTypeAllowblank', true);
                this.$set(this.formData, 'ggEquityOutDateAllowblank', true);
                this.$set(this.formData, 'ggStockCodeAllowblank', false);
                this.$set(this.formData, 'ggEquityOutDate', '');
             }else{
               this.$set(this.formData, 'ggStockTypeAllowblank', true);
               this.$set(this.formData, 'ggEquityOutDateAllowblank', false);
                this.$set(this.formData, 'ggStockCodeAllowblank', true);
             }
       }else if(this.formData.AssetDebtRegisterType == '12'){
           this.$set(this.formData, 'mmManagePlanNameAllowblank', false);
           this.$set(this.formData, 'mmManageProductAllowblank', false);
           this.$set(this.formData, 'mmPlanIssuerCodeAllowblank', false);
           this.$set(this.formData, 'mmAssetPlanRgCodeAllowblank', false);
           this.$set(this.formData, 'mmManagerAllowblank', false);
           this.$set(this.formData, 'mmCustodianAllowblank', false);
           this.$set(this.formData, 'mmAmtAllowblank', false);
           this.$set(this.formData, 'mmActualDirectAllowblank', false);
           this.$set(this.formData, 'mmDetailsInvestAllowblank', false);
           this.$set(this.formData, 'mmIndustryInvestAllowblank', false);
           this.$set(this.formData, 'mmPlanStartDateAllowblank', false);
           this.$set(this.formData, 'mmPlanMaturityDateAllowblank', false);
           this.$set(this.formData, 'mmPlanTypeAllowblank', false);
           this.$set(this.formData, 'mmExpectedReturnAllowblank', false);
           this.$set(this.formData, 'mmInvestStructureAllowblank', false);
           this.$set(this.formData, 'mmManagerTypeAllowblank', false);
           this.$set(this.formData, 'mmManagerFeeRateAllowblank', false);
           this.$set(this.formData, 'mmCustodianFeeRateAllowblank', false);
           this.$set(this.formData, 'mmTransCostRateAllowblank', false);
           this.$set(this.formData, 'mmInterFeeRateAllowblank', false);
           this.$set(this.formData, 'mmOtherExpenseRateAllowblank', false);
           if(this.formData.mmManageProduct=='01'){
                 this.$set(this.formData, 'mmProductCodeAllowblank', false);
           }else{
                this.$set(this.formData, 'mmProductCodeAllowblank', true);
           }
           if(this.formData.assDebtType=='1705'){
                this.$set(this.formData, 'mmManageProductAllowblank', false);
           }else{
                this.$set(this.formData, 'mmManageProductAllowblank', true);
                this.$set(this.formData, 'mmManageProduct', "");
           }
       }else if(this.formData.AssetDebtRegisterType == '14'){
           this.$set(this.formData, 'ssNameAllowblank', false);
           this.$set(this.formData, 'ssAssetTypeAllowblank', false);
           this.$set(this.formData, 'ssAmtAllowblank', false);
           this.$set(this.formData, 'ssValueDateAllowblank', false);
           this.$set(this.formData, 'ssMaturityDateAllowblank', false);
           this.$set(this.formData, 'ssCountryAllowblank', false);
           this.$set(this.formData, 'ssExpectedReturnAllowblank', false);
           this.$set(this.formData, 'ssInterestFrequencyAllowblank', false);
           this.$set(this.formData, 'ssDebtorAllowblank', false);
           this.$set(this.formData, 'ssOrganCodeAllowblank', false);
           this.$set(this.formData, 'ssDebtorTypeScaleAllowblank', false);
           this.$set(this.formData, 'ssDebtorTypeTechAllowblank', false);
           this.$set(this.formData, 'ssDebtorTypeEconomicAllowblank', false);
           this.$set(this.formData, 'ssProjectAllowblank', false);
           this.$set(this.formData, 'ssIndustryDebtorAllowblank', false);
           this.$set(this.formData, 'ssIndustryProjectAllowblank', false);
           this.$set(this.formData, 'ssMonitoryIndustryAllowblank', false);
           this.$set(this.formData, 'ssInternalAssetRateAllowblank', false);
           this.$set(this.formData, 'ssDetailsGuaranteeAllowblank', false);
       }else if(this.formData.AssetDebtRegisterType == '16'){
           this.$set(this.formData, 'ppFundCodeAllowblank', false);
           this.$set(this.formData, 'ppFundNameAllowblank', false);
           this.$set(this.formData, 'ppIndustryAllowblank', false);
           this.$set(this.formData, 'ppRegistAgencyAllowblank', false);
           this.$set(this.formData, 'ppDirectGovernFundAllowblank', false);
           this.$set(this.formData, 'ppIssuedAssetCompanyAllowblank', false);
           this.$set(this.formData, 'ppManagerFundNameAllowblank', false);
           this.$set(this.formData, 'ppCustodianFundNameAllowblank', false);
           this.$set(this.formData, 'ppInvestAssetsAllowblank', false);
       }else if(this.formData.AssetDebtRegisterType == '10'){
           this.$set(this.formData, 'kkCountryAllowblank', false);
           this.$set(this.formData, 'kkIdentCodeAllowblank', false);
           this.$set(this.formData, 'kkNameAllowblank', false);
           this.$set(this.formData, 'kkIssuerAllowblank', false);
           this.$set(this.formData, 'kkIndustryAllowblank', false);
       }else if(this.formData.AssetDebtRegisterType == '7'){
        this.$set(this.formData, 'hhNameAllowblank', false);
        this.$set(this.formData, 'hhUnderAssetTypeAllowblank', false);
        this.$set(this.formData, 'hhNominalPrincipalAllowblank', false);
        this.$set(this.formData, 'hhHoldObjectiveAllowblank', false);
          if(this.formData.hhUnderAssetType=='99'||this.formData.hhHoldObjective=='99'||this.formData.tradeVenue=='99'){
                  this.$set(this.formData, 'detailsAllowblank', false);
          }else{
                this.$set(this.formData, 'detailsAllowblank', true);
         }
        }

      },

  },
  mounted(){
    this.$set(this.searchParam,'dataChangeType','0')
  }
  ,
  watch: {
    //查询起息日
    RegisterDate() {
      this.$set(this.searchParam, 'startDate', this.RegisterDate == null ? '' : this.RegisterDate[0]);
      this.$set(this.searchParam, 'endDate', this.RegisterDate == null ? '' : this.RegisterDate[1]);
    },
    dataDate() {
      this.$set(this.searchParam, 'queryStartDate', this.dataDate ? this.dataDate[0] : '');
      this.$set(this.searchParam,'queryEndDate',this.dataDate ? this.dataDate[1] : '');
      this.$set(this.queryParam, 'queryStartDate', this.dataDate ? this.dataDate[0] : '');
      this.$set(this.queryParam,'queryEndDate',this.dataDate ? this.dataDate[1] : '');
    },
    'formData.hhHoldObjective': function(value){
    if(this.formData.AssetDebtRegisterType == '7'){
      if(value=='99'){
        this.$set(this.formData, 'detailsAllowblank', false);
      }else{
      this.$set(this.formData, 'detailsAllowblank', true);
        }
       }
     },
     'formData.hhUnderAssetType': function(value){
     if(this.formData.AssetDebtRegisterType == '7'){
      if(value=='99'||this.formData.hhHoldObjective=='99'||this.formData.tradeVenue=='99'){
        this.$set(this.formData, 'detailsAllowblank', false);
      }else{
      this.$set(this.formData, 'detailsAllowblank', true);
        }
        }
     },
     'formData.tradeVenue': function(value){
     if(this.formData.AssetDebtRegisterType == '7'){
      if(value=='99'){
        this.$set(this.formData, 'detailsAllowblank', false);
      }else{
        this.$set(this.formData, 'detailsAllowblank', true);
         }
        }
      },
     'formData.assDebtType': function(value){
     if(this.formData.AssetDebtRegisterType == '6'){
     if(value=='1302'||value=='1305'){
           this.$set(this.formData, 'ggStockTypeAllowblank', true);
           this.$set(this.formData, 'ggEquityOutDateAllowblank', true);
            this.$set(this.formData, 'ggStockCodeAllowblank', false);
           this.$set(this.formData, 'ggEquityOutDate', '');
        }else{
          this.$set(this.formData, 'ggStockTypeAllowblank', true);
          this.$set(this.formData, 'ggEquityOutDateAllowblank', false);
           this.$set(this.formData, 'ggStockCodeAllowblank', true);
        }
     }
     if(this.formData.AssetDebtRegisterType == '4'){
        if(value=='2202'){
            this.$set(this.formData, 'eeAssetCodeAllowblank', false);
          }else{
            this.$set(this.formData, 'eeAssetCodeAllowblank', true);
            this.$set(this.formData, 'eeAssetCode', "");
          }
     }
     if(this.formData.AssetDebtRegisterType == '12'){
              if(value=='1705'){
                  this.$set(this.formData, 'mmManageProductAllowblank', false);
                }else{
                  this.$set(this.formData, 'mmManageProductAllowblank', true);
                  this.$set(this.formData, 'mmManageProduct', "");
                }
           }
      },
     'formData.mmManageProduct': function(value){
     if(this.formData.AssetDebtRegisterType == '12'){
     if(value=='01'){
           this.$set(this.formData, 'mmProductCodeAllowblank', false);
        }else{
          this.$set(this.formData, 'mmProductCodeAllowblank', true);
        }
     }
  }
  }
};
</script>
