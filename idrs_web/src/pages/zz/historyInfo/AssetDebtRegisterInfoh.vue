<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="AssetDebtRegisterInfoh" data-label-width="130px" data-target="assetDebtRegisterInfohGrid"  v-model = "searchParam">
        <k-form-item label="登记日期" data-label-width="80px">
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="行内资产/负债编码">
          <k-field-text v-model="searchParam.assetCode"/>
        </k-form-item>
        <k-form-item label="资产/负债类别">
          <k-field-select v-model="searchParam.assDebtType"  data-dict="subm_cbndScdCtg"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <k-grid ref="assetDebtRegisterInfohGrid" @data-row-select="selectRow"  data-operate-column="false" data-action="AssetDebtRegisterInfoh.findAssetDebtRegisterInfohs" >
        <k-grid-column data-align="left" data-header="id" data-name="id" data-hidden="true" data-export="false"></k-grid-column>
        <k-grid-column data-align="left" data-header="报送状态" data-name="registerStatus" data-dict="subm_report_status" data-export="false" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="行内资产/负债编码" data-name="assetCode"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构代码" data-name="bankCode" data-width="120"></k-grid-column>
        <k-grid-column data-align="left" data-header="资产/负债类别" data-name="assDebtType" data-dict="subm_cbndScdCtg" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易流通场所" data-name="tradeVenue" data-dict="subm_tacdingPlace" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="币种" data-name="cur" data-dict="tr_cur" data-width="120"></k-grid-column>
        <!--1 本行/他行存款||大额存单-->
        <k-grid-column data-align="left" data-header="资金存入银行" data-name="bbDepositBank" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="存款账号" data-name="bbAccountNo" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="存款金额" data-name="bbDepositAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="起息日" data-name="bbValueDate"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="到期日" data-name="bbMaturityDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="年利率%" data-name="bbAnnualRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="计息基础" data-name="bbInterestBasis" data-width="150"  data-dict="subm_intr_base"></k-grid-column>
        <k-grid-column data-align="left" data-header="存款类型" data-name="bbDepositType" data-width="150" data-dict="subm_deposit_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="结构性存款挂钩标的类别" data-name="bbStructDepositType" data-width="150" data-dict="subm_stru_deposit_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="结构性存款挂钩标的" data-name="bbStructDeposit" data-width="150"></k-grid-column>
        <!--2 债券类资产/理财直接融资工具/同业存单-->
        <k-grid-column data-align="left" data-header="代码" data-name="ccIdentCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="名称" data-name="ccName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="具体类别" data-name="ccSpecificBondType" data-dict="subm_spcType" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行方式" data-name="ccIssModeBond" data-dict="subm_iss_mode_bond" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="主体评级" data-name="ccIssRatePart" data-dict="subm_mainRating" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构类型（按规模划分）" data-name="ccInstituteTypeScale" data-dict="subm_instituteTypeTech" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构类型（按技术领域划分）" data-name="ccInstituteTypeTech" data-dict="subm_isuOrgTypTchno" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构类型（按经济类型划分）" data-name="ccInstituteTypeEconomic" data-dict="subm_isuOrgTypEcn" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构所属行业" data-name="ccIndustryIssuer" data-dict="subm_isuOrgBlgIdt" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记托管机构" data-name="ccRegistDeposit" data-dict="subm_regTrstOrg" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记托管机构说明" data-name="ccDetailsRegistDeposit" data-width="150"></k-grid-column>
        <!--3 拆放同业及买入返售/同业拆入及卖出回购-->
        <k-grid-column data-align="left" data-header="起息日" data-name="ddValueDate"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="到期日" data-name="ddMaturityDate"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="对手方" data-name="ddCounterparty" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="对手方类型" data-name="ddCounterpartyType" data-dict="subm_counterparty_type" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="年利率%" data-name="ddAnnalInterestRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="计息基础" data-name="ddInterestBasis" data-width="150" data-dict="subm_intr_base"></k-grid-column>
        <k-grid-column data-align="left" data-header="回购标的类别" data-name="ddCollateralType" data-dict="subm_interest_basis" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="回购标的金额" data-name="ddCollateralValue" data-width="150"></k-grid-column>
        <!--4 非标准化债权类资产/新增可投资资产-->
        <k-grid-column data-align="left" data-header="收/受益权类型" data-name="eeOwnershipType" data-dict="subm_incBenRitType" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否属于买入返售" data-name="eeBuyback" data-width="150"  data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="名称" data-name="eeName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="资产代码" data-name="eeAssetCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="金额" data-name="eeAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="份额面值" data-name="eeUnitParValue" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="起息日" data-name="eeValueDate"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="到期日" data-name="eeMaturityDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="法定到期日" data-name="eeStatutoryMaturityDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否有预期收益率" data-name="eeExpectedReturn" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="right" data-header="项目收益率(利率)%" data-name="eeProjectAnnaulReturn" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="计息类型" data-name="eeCouponType" data-width="150"  data-dict="subm_interest_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="规则付息标识" data-name="eeRegualrInterestPay" data-dict="subm_isTrue" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="付息频率（个月/次）" data-name="eeInterestPayFrequency" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="利息分布方式" data-name="eeCouponAllocationType" data-dict="subm_intrAlcMth" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="还本付息情况说明" data-name="eeDetailPrincInterest" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="计息基础" data-name="eeInterestBasis" data-width="150" data-dict="subm_intr_base"></k-grid-column>
        <k-grid-column data-align="left" data-header="基准利率种类" data-name="eeBenchRateType" data-dict="subm_bchmRatTyp" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否有浮动因子" data-name="eeFloatFactor" data-dict="subm_isTrue" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="浮动因子（%）" data-name="eeFloatRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="利差(BP)" data-name="eeYieldSpreadBp" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="结构档次" data-name="eeStructGrade" data-width="150" data-dict="subm_strcGrd"></k-grid-column>
        <k-grid-column data-align="left" data-header="还本方式" data-name="eePrincPaymentType" data-width="150" data-dict="subm_payPrcpMth"></k-grid-column>
        <k-grid-column data-align="left" data-header="分期还本条款标识" data-name="eeInstallRepayType" data-width="150" data-dict="subm_insPayPrcpF"></k-grid-column>
        <k-grid-column data-align="left" data-header="基础资产类型" data-name="eeBaseAssetType" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="超额收益分配比例（%）" data-name="eePercentExcInAllot" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资人" data-name="eeDebtor" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资人内部信用评级" data-name="eeDeptorRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="外部评级机构名称及对融资人评级结果" data-name="eeRateAgencyIss" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资人类型（按规模划分）" data-name="eeDebtorTypeScale" data-width="150" data-dict="subm_instituteTypeTech"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资人类型（按技术领域划分）" data-name="eeDebtorTypeTech" data-width="150" data-dict="subm_isuOrgTypTchno"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资人类型（按经济类型划分）" data-name="eeDebtorTypeEconomic" data-width="150" data-dict="subm_isuOrgTypEcn"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资项目" data-name="eeProject" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资人所属行业" data-name="eeIndustryDebtor" data-width="150" data-dict="subm_isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-align="left" data-header="项目是否属于重点监控行业和领域" data-name="eeMonitorIndusType" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="重点监控行业和领域类别" data-name="eeMonitorIndustryType" data-width="150" data-dict="subm_keyMntIdtTyp"></k-grid-column>
        <k-grid-column data-align="left" data-header="重点监控行业和领域类别说明" data-name="eeDetailsMonitoryType" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="担保方式" data-name="eeGuaranteeMethod" data-width="150"  data-dict="subm_grntWay"></k-grid-column>
        <k-grid-column data-align="left" data-header="抵质押物类型" data-name="eePledgeType" data-width="150" data-dict="subm_plgTyp"></k-grid-column>
        <k-grid-column data-align="right" data-header="抵质押物价值（元）" data-name="eePledgeValue" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="担保性质" data-name="eeGuaranteeType" data-width="150" data-dict="subm_grntChr"></k-grid-column>
        <k-grid-column data-align="left" data-header="担保人与融资人关系" data-name="eeGuarantorType" data-width="150" data-dict="subm_grntLvrgRel"></k-grid-column>
        <k-grid-column data-align="left" data-header="担保情况说明" data-name="eeDetailGuaranteeStatus" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资人主体评级" data-name="eeDebtorRate" data-width="150" data-dict="subm_mainRating"></k-grid-column>
        <k-grid-column data-align="left" data-header="资产内部评级" data-name="eeInterAssetRate" data-width="150" data-dict="subm_mainRating"></k-grid-column>
        <k-grid-column data-align="left" data-header="资产外部评级" data-name="eeOutAssetRate" data-width="150" data-dict="subm_mainRating"></k-grid-column>
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
        <k-grid-column data-align="left" data-header="选择权" data-name="eeOptionRight" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="行权条件说明" data-name="eeDetailsExerciseTerm" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资人所属地区" data-name="eeRegionDebtor" data-dict="subm_prod_sale_area" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资人组织机构（社会信用）代码" data-name="eeOrganizationCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="增信机构代码" data-name="eeEnhanceInstituteCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="增信机构名称" data-name="eeEnhanceInstituteName" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="融资总费率%" data-name="eeTotalFeeRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资项目所属行业" data-name="eeIndustryProject"  data-dict="subm_isuOrgBlgIdt" data-width="150"></k-grid-column>
        <!--5 票据类/信用证类资产-->
        <k-grid-column data-align="left" data-header="是否为收/受益权" data-name="ffOwnership" data-width="150" data-dict="tr_is_belong"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否属于买入返售" data-name="ffBuyback" data-width="150"  data-dict="tr_is_belong"></k-grid-column>
        <k-grid-column data-align="left" data-header="类型" data-name="ffType" data-width="150" data-dict="subm_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否为标准化票据" data-name="ffStandarBill" data-width="150" data-dict="tr_is_belong"></k-grid-column>
        <k-grid-column data-align="left" data-header="票据代码" data-name="ffNoteCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="数量" data-name="ffQuantity" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="合计金额" data-name="ffAggregateAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="加权剩余期限（天）" data-name="ffWeightRemainDay" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="最长剩余期限（天）" data-name="ffMaxRemainDay" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="最短剩余期限（天）" data-name="ffMinRemainFay" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="起息日" data-name="ffValueDate"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="到期日" data-name="ffMaturityDate"  data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="行业" data-name="ffIndustry" data-width="150" data-dict="subm_isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-align="right" data-header="贴现利率（%）" data-name="ffDiscountRate" data-width="150"></k-grid-column>
        <!--6 权益类资产-->
        <k-grid-column data-align="left" data-header="股票代码" data-name="ggStockCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="股票/企业名称" data-name="ggName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="股票类型" data-name="ggStockType" data-width="150"  data-dict="subm_stock_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="行业" data-name="ggIndustry" data-width="150" data-dict="subm_isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资阶段" data-name="ggInvestStage" data-width="150" data-dict="subm_invest_stage"></k-grid-column>
        <k-grid-column data-align="left" data-header="股权退出安排" data-name="ggEquityOutDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="企业类型（按规模划分）" data-name="ggEnterTypeScale" data-width="150" data-dict="subm_instituteTypeTech"></k-grid-column>
        <k-grid-column data-align="left" data-header="企业类型（按技术领域划分）" data-name="ggEnterTypeTech" data-width="150" data-dict="subm_isuOrgTypTchno"></k-grid-column>
        <k-grid-column data-align="left" data-header="企业类型（按经济类型划分）" data-name="ggEnterTypeEconomic" data-width="150" data-dict="subm_isuOrgTypEcn"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否为质押融资" data-name="ggPledgedFinace" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否为债转股" data-name="ggDebtEquitySwap" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <!--7 金融衍生品-->
        <k-grid-column data-align="left" data-header="名称" data-name="hhName" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="名义本金" data-name="hhNominalPrincipal" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="标的类别" data-name="hhUnderAssetType" data-width="150"  data-dict="stru_deposit_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="持有目的" data-name="hhHoldObjective" data-width="150" data-dict="subm_holding_obj"></k-grid-column>
        <!--8 QDII债券资产-->
        <k-grid-column data-align="left" data-header="所属国家或地区" data-name="iiCountyRegion" data-width="150" data-dict="tr_iss_country"></k-grid-column>
        <k-grid-column data-align="left" data-header="债券名称" data-name="iiBondName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="债券代码" data-name="iiBondIdentCode" data-width="150" ></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构" data-name="iiIssuer" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构所属行业" data-name="iiIndustryIssuer" data-width="150" data-dict="subm_isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-align="left" data-header="起息日" data-name="iiValueDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="到期日" data-name="iiMaturityDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="期限（月）" data-name="iiTermMaturity" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构主体信用评级" data-name="iiIssuerRateBond" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="债券信用评级" data-name="iiBondRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="票面利率%" data-name="iiCoupRate" data-width="150" ></k-grid-column>
        <k-grid-column data-align="right" data-header="付息频率（个月/次）" data-name="iiInterestPayQuency" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="担保情况说明" data-name="iiDetailsAssureStatus" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否含权等特殊条款情况说明" data-name="iiDetailsSpecialTerms" data-width="150"></k-grid-column>
        <!--9 QDII拆出/逆回购资产-->
        <k-grid-column data-align="left" data-header="所属国家或地区" data-name="jjCountry" data-width="150" data-dict="tr_iss_country"></k-grid-column>
        <k-grid-column data-align="left" data-header="起息日" data-name="jjValueDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="到期日" data-name="jjMaturityDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="对手方" data-name="jjCounterparty" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="年利率%" data-name="jjInterestRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="计息基础" data-name="jjInterestBasis" data-width="150" data-dict="subm_intr_base"></k-grid-column>
        <!--10 QDII股票/基金类资产-->
        <k-grid-column data-align="left" data-header="所属国家或地区" data-name="kkCountry" data-width="150" data-dict="tr_iss_country"></k-grid-column>
        <k-grid-column data-align="left" data-header="股票/基金代码" data-name="kkIdentCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="股票/基金名称" data-name="kkName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="发行机构" data-name="kkIssuer" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="行业" data-name="kkIndustry" data-width="150" data-dict="subm_isuOrgBlgIdt"></k-grid-column>
        <!--11 QDII结构性票据类资产-->
        <k-grid-column data-align="left" data-header="所属国家或地区" data-name="llCountry" data-width="150" data-dict="tr_iss_country"></k-grid-column>
        <k-grid-column data-align="left" data-header="合约名称" data-name="llContractName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="起息日" data-name="llValueDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="到期日" data-name="llMaturityDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="票面利率%" data-name="llCouponRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="付息频率（个月/次）" data-name="llInterestFrequency" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="固定收益部分所占比例(%)" data-name="llPercentFix" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="衍生金融工具所占比例(%)" data-name="llPercentDerivate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="衍生金融工具具体投资方式" data-name="llDerivateInvetType" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="衍生金融工具挂钩的标的资产" data-name="llUnderAsset" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="利息结算方式" data-name="llDetailsProceeds" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="含权情况说明" data-name="llDetailsOption" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="结构性票据最高收益率%" data-name="llMaxNoteReturn" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="结构性票据最低收益率%" data-name="llMinNoteReturn" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="挂钩标的资产基准价格" data-name="llStrikeUnderAsset" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="挂钩标的资产登记日价格" data-name="llUnderRgPrice" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="交易费" data-name="llTransCosts" data-width="150"></k-grid-column>
        <!--12 资产管理产品-->
        <k-grid-column data-align="left" data-header="资管计划名称" data-name="mmManagePlanName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否为银行理财产品" data-name="mmManageProduct" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="理财产品登记编码" data-name="mmProductCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否由金融资产投资公司发行" data-name="mmIssuedAssetCompany" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="资管计划发起人机构编码" data-name="mmPlanIssuerCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="资管计划登记编码" data-name="mmAssetPlanRgCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="管理人" data-name="mmManager" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="托管人" data-name="mmCustodian" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="金额" data-name="mmAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="资金实际投向" data-name="mmActualDirect" data-width="150" data-dict="subm_actual_invest_dir_fund"></k-grid-column>
        <k-grid-column data-align="left" data-header="资金运用方式" data-name="mmDetailsInvest" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="资金运用行业" data-name="mmIndustryInvest" data-width="150"  data-dict="subm_isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-align="left" data-header="资管计划成立日期" data-name="mmPlanStartDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="资管计划终止日期" data-name="mmPlanMaturityDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="资管计划属性" data-name="mmPlanType" data-width="150" data-dict="subm_astMngPlanPrpt"></k-grid-column>
        <k-grid-column data-align="right" data-header="是否有预期收益率" data-name="mmExpectedReturn" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="right" data-header="预期最高收益率%" data-name="mmMaxExpectedReturn" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="预期最低收益率%" data-name="mmMinExpectedReturn" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="购买结构" data-name="mmInvestStructure" data-width="150" data-dict="subm_buyStrc"></k-grid-column>
        <k-grid-column data-align="left" data-header="管理方式" data-name="mmManagerType" data-width="150"  data-dict="subm_mngMth"></k-grid-column>
        <k-grid-column data-align="right" data-header="管理费率%" data-name="mmManagerFeeRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="托管费率%" data-name="mmCustodianFeeRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="交易相关合计费率%" data-name="mmTransCostRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="中介服务机构合计费率%" data-name="mmInterFeeRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="其他合计费率%" data-name="mmOtherExpenseRate" data-width="150"></k-grid-column>
        <!--13 贵金属/商品-->
        <k-grid-column data-align="right" data-header="所属国家或地区" data-name="nnCountry" data-width="150" data-dict="tr_iss_country"></k-grid-column>
        <k-grid-column data-align="right" data-header="名称" data-name="nnName" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="期限（天）" data-name="nnTermDays" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="资产价值" data-name="nnAssetValue" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="资产收益率%" data-name="nnAssetReturn" data-width="150"></k-grid-column>
        <!--14 其他非标准化债权类/权益类/代客境外理财投资QDII/新增可投资资产-->
        <k-grid-column data-align="left" data-header="名称" data-name="ssName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="行内资产类别" data-name="ssAssetType" data-width="150" data-dict="subm_asset_type"></k-grid-column>
        <k-grid-column data-align="left" data-header="行内资产类别说明" data-name="ssDetailsAssetType" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="金额" data-name="ssAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="起息日" data-name="ssValueDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="到期日" data-name="ssMaturityDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="所属国家或地区" data-name="ssCountry" data-width="150"  data-dict="tr_iss_country"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否有预期收益率" data-name="ssExpectedReturn" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="right" data-header="项目收益率（利率）%" data-name="ssAnnualReturn" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="付息频率（个月/次）" data-name="ssInterestFrequency" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资人" data-name="ssDebtor" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资人组织机构（社会信用）代码" data-name="ssOrganCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="外部评级机构名称及对融资人评级结果" data-name="ssRateAgencyIss" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资人类型（按规模划分）" data-name="ssDebtorTypeScale" data-width="150" data-dict="subm_instituteTypeTech"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资人类型（按技术领域划分）" data-name="ssDebtorTypeTech" data-width="150" data-dict="subm_isuOrgTypTchno"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资人类型（按经济类型划分）" data-name="ssDebtorTypeEconomic" data-width="150" data-dict="subm_isuOrgTypEcn"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资项目" data-name="ssProject" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资人所属行业" data-name="ssIndustryDebtor" data-width="150" data-dict="subm_isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-align="left" data-header="融资项目所属行业" data-name="ssIndustryProject" data-width="150" data-dict="subm_isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-align="left" data-header="项目是否属于重点监控行业和领域" data-name="ssMonitoryIndustry" data-width="150" data-dict="subm_isTrue"></k-grid-column>
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
        <!--15 另类资产-->
        <k-grid-column data-align="left" data-header="所属国家或地区" data-name="ooCountry" data-width="150" data-dict="tr_iss_country"></k-grid-column>
        <k-grid-column data-align="left" data-header="名称" data-name="ooName" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="起息日" data-name="ooValueDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="到期日" data-name="ooMaturityDate" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="资产价值" data-name="ooAssetValue" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="资产收益率%" data-name="ooAssetReturn" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <!--16 公募基金/私募基金-->
        <k-grid-column data-align="left" data-header="基金代码" data-name="ppFundCode" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="基金名称" data-name="ppFundName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="行业" data-name="ppIndustry" data-width="150" data-dict="subm_isuOrgBlgIdt"></k-grid-column>
        <k-grid-column data-align="left" data-header="登记备案机构" data-name="ppRegistAgency" data-width="150" data-dict="subm_regTrstOrg"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否为固定收益类" data-name="ppFixedIncome" data-width="150"  data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否属于政府投资基金" data-name="ppGovernInvestFund" data-width="150"  data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="政府投资基金投向" data-name="ppDirectGovernFund" data-width="150" data-dict="subm_actual_invest_dir_gov_fund"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否由金融资产投资公司发行" data-name="ppIssuedAssetCompany" data-width="150" data-dict="subm_isTrue"></k-grid-column>
        <k-grid-column data-align="left" data-header="基金公司名称" data-name="ppTaName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="基金管理机构名称" data-name="ppManagerFundName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="基金托管机构名称" data-name="ppCustodianFundName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资阶段" data-name="ppInvestStage" data-width="150"  data-dict="subm_invest_stage"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资企业类型（按规模划分）" data-name="ppEnterTypeScale" data-width="150" data-dict="subm_instituteTypeTech"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资企业类型（按技术领域划分）" data-name="ppEnterTypeTech" data-width="150" data-dict="subm_isuOrgTypTchno"></k-grid-column>
        <k-grid-column data-align="left" data-header="投资企业类型（按经济类型划分）" data-name="ppEnterTypeEconomic" data-width="150" data-dict="subm_isuOrgTypEcn"></k-grid-column>
        <k-grid-column data-align="left" data-header="基金投资资产" data-name="ppInvestAssets" data-width="150"></k-grid-column>
        <!--17 委外投资——协议方式-->
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
        <!--18 其他（负债）类资产-->
        <k-grid-column data-align="left" data-header="所属国家或地区" data-name="rrCountry"  data-dict="tr_iss_country" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="名称" data-name="rrName" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="期限（天）" data-name="rrTermMaturity" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="负债规模" data-name="rrLiabilityAmt" data-width="150"></k-grid-column>
        <k-grid-column data-align="right" data-header="利率%" data-name="rrInterestRate" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="是否为同业借款" data-name="rrCashBorrow" data-dict="subm_isTrue" data-width="150"></k-grid-column>
        <k-grid-column data-align="left" data-header="备注" data-name="details" data-width="150"></k-grid-column>
        <k-grid-column data-header="登记流水号" data-name="registerSerno" data-export="false" data-width="250"></k-grid-column>
        <k-grid-column data-header="登记日期" data-name="registerDate" data-export="false"  data-width="100"></k-grid-column>
        <k-grid-column data-header="新增日期" data-name="createDate"  data-export="false"  data-width="100"></k-grid-column>
      </k-grid>
    </div>

	<!--    添加资产负债登记历史信息弹出框   -->
	<k-popup ref="addAssetDebtRegisterInfohPopup" data-title="新增">
    	<k-form ref="addAssetDebtRegisterInfohForm" :data-col="2">
			<k-form-item label="登记流水号">
	        	<k-field-text v-model="formData.registerSerno"/>
	     	</k-form-item>
			<k-form-item label="导入日期">
	        	<k-field-text v-model="formData.impDate"/>
	     	</k-form-item>
			<k-form-item label="投资者登记日期">
	        	<k-field-text v-model="formData.registerDate"/>
	     	</k-form-item>
			<k-form-item label="登记状态（0">
	        	<k-field-text v-model="formData.registerStatus"/>
	     	</k-form-item>
			<k-form-item label="发行机构代码">
	        	<k-field-text v-model="formData.bankCode"/>
	     	</k-form-item>
			<k-form-item label="行内资产/负债编码">
	        	<k-field-text v-model="formData.assetCode"/>
	     	</k-form-item>
			<k-form-item label="资产/负债类别">
	        	<k-field-text v-model="formData.assDebtType"/>
	     	</k-form-item>
			<k-form-item label="币种">
	        	<k-field-text v-model="formData.cur"/>
	     	</k-form-item>
			<k-form-item label="交易流通场所">
	        	<k-field-text v-model="formData.tradeVenue"/>
	     	</k-form-item>
			<k-form-item label="备注">
	        	<k-field-text v-model="formData.details"/>
	     	</k-form-item>
			<k-form-item label="资金存入银行">
	        	<k-field-text v-model="formData.bbDepositBank"/>
	     	</k-form-item>
			<k-form-item label="存款金额">
	        	<k-field-text v-model="formData.bbDepositAmt"/>
	     	</k-form-item>
			<k-form-item label="起息日">
	        	<k-field-text v-model="formData.bbValueDate"/>
	     	</k-form-item>
			<k-form-item label="到期日">
	        	<k-field-text v-model="formData.bbMaturityDate"/>
	     	</k-form-item>
			<k-form-item label="年利率%">
	        	<k-field-text v-model="formData.bbAnnualRate"/>
	     	</k-form-item>
			<k-form-item label="计息基础">
	        	<k-field-text v-model="formData.bbInterestBasis"/>
	     	</k-form-item>
			<k-form-item label="存款类型">
	        	<k-field-text v-model="formData.bbDepositType"/>
	     	</k-form-item>
			<k-form-item label="结构性存款标的类别">
	        	<k-field-text v-model="formData.bbStructDepositType"/>
	     	</k-form-item>
			<k-form-item label="结构性存款挂钩标的">
	        	<k-field-text v-model="formData.bbStructDeposit"/>
	     	</k-form-item>
			<k-form-item label="代码">
	        	<k-field-text v-model="formData.ccIdentCode"/>
	     	</k-form-item>
			<k-form-item label="名称">
	        	<k-field-text v-model="formData.ccName"/>
	     	</k-form-item>
			<k-form-item label="具体类别">
	        	<k-field-text v-model="formData.ccSpecificBondType"/>
	     	</k-form-item>
			<k-form-item label="发行方式">
	        	<k-field-text v-model="formData.ccIssModeBond"/>
	     	</k-form-item>
			<k-form-item label="主体评级">
	        	<k-field-text v-model="formData.ccIssRatePart"/>
	     	</k-form-item>
			<k-form-item label="发行机构类型">
	        	<k-field-text v-model="formData.ccInstituteTypeScale"/>
	     	</k-form-item>
			<k-form-item label="发行机构类型">
	        	<k-field-text v-model="formData.ccInstituteTypeTech"/>
	     	</k-form-item>
			<k-form-item label="发行机构类型">
	        	<k-field-text v-model="formData.ccInstituteTypeEconomic"/>
	     	</k-form-item>
			<k-form-item label="发行机构所属行业">
	        	<k-field-text v-model="formData.ccIndustryIssuer"/>
	     	</k-form-item>
			<k-form-item label="登记托管机构">
	        	<k-field-text v-model="formData.ccRegistDeposit"/>
	     	</k-form-item>
			<k-form-item label="登记托管机构说明">
	        	<k-field-text v-model="formData.ccDetailsRegistDeposit"/>
	     	</k-form-item>
			<k-form-item label="起息日">
	        	<k-field-text v-model="formData.ddValueDate"/>
	     	</k-form-item>
			<k-form-item label="到期日">
	        	<k-field-text v-model="formData.ddMaturityDate"/>
	     	</k-form-item>
			<k-form-item label="对手方">
	        	<k-field-text v-model="formData.ddCounterparty"/>
	     	</k-form-item>
			<k-form-item label="对手方类型">
	        	<k-field-text v-model="formData.ddCounterpartyType"/>
	     	</k-form-item>
			<k-form-item label="年利率%">
	        	<k-field-text v-model="formData.ddAnnalInterestRate"/>
	     	</k-form-item>
			<k-form-item label="计息基础">
	        	<k-field-text v-model="formData.ddInterestBasis"/>
	     	</k-form-item>
			<k-form-item label="回购标的类别">
	        	<k-field-text v-model="formData.ddCollateralType"/>
	     	</k-form-item>
			<k-form-item label="回购标的金额">
	        	<k-field-text v-model="formData.ddCollateralValue"/>
	     	</k-form-item>
			<k-form-item label="名称">
	        	<k-field-text v-model="formData.eeName"/>
	     	</k-form-item>
			<k-form-item label="金额">
	        	<k-field-text v-model="formData.eeAmt"/>
	     	</k-form-item>
			<k-form-item label="份额面值">
	        	<k-field-text v-model="formData.eeUnitParValue"/>
	     	</k-form-item>
			<k-form-item label="收/受权益类型">
	        	<k-field-text v-model="formData.eeOwnershipType"/>
	     	</k-form-item>
			<k-form-item label="是否属于买入反售">
	        	<k-field-text v-model="formData.eeBuyback"/>
	     	</k-form-item>
			<k-form-item label="起息日">
	        	<k-field-text v-model="formData.eeValueDate"/>
	     	</k-form-item>
			<k-form-item label="到期日">
	        	<k-field-text v-model="formData.eeMaturityDate"/>
	     	</k-form-item>
			<k-form-item label="法定到期日">
	        	<k-field-text v-model="formData.eeStatutoryMaturityDate"/>
	     	</k-form-item>
			<k-form-item label="是否有预期收益率">
	        	<k-field-text v-model="formData.eeExpectedReturn"/>
	     	</k-form-item>
			<k-form-item label="项目收益率">
	        	<k-field-text v-model="formData.eeProjectAnnaulReturn"/>
	     	</k-form-item>
			<k-form-item label="计息类型">
	        	<k-field-text v-model="formData.eeCouponType"/>
	     	</k-form-item>
			<k-form-item label="规则付息标识">
	        	<k-field-text v-model="formData.eeRegualrInterestPay"/>
	     	</k-form-item>
			<k-form-item label="付息频率">
	        	<k-field-text v-model="formData.eeInterestPayFrequency"/>
	     	</k-form-item>
			<k-form-item label="利息分布方式">
	        	<k-field-text v-model="formData.eeCouponAllocationType"/>
	     	</k-form-item>
			<k-form-item label="还本付息情况说明">
	        	<k-field-text v-model="formData.eeDetailPrincInterest"/>
	     	</k-form-item>
			<k-form-item label="计息基础">
	        	<k-field-text v-model="formData.eeInterestBasis"/>
	     	</k-form-item>
			<k-form-item label="基准利率种类">
	        	<k-field-text v-model="formData.eeBenchRateType"/>
	     	</k-form-item>
			<k-form-item label="是否有浮动因子">
	        	<k-field-text v-model="formData.eeFloatFactor"/>
	     	</k-form-item>
			<k-form-item label="浮动因子%">
	        	<k-field-text v-model="formData.eeFloatRate"/>
	     	</k-form-item>
			<k-form-item label="利差">
	        	<k-field-text v-model="formData.eeYieldSpreadBp"/>
	     	</k-form-item>
			<k-form-item label="结构档次">
	        	<k-field-text v-model="formData.eeStructGrade"/>
	     	</k-form-item>
			<k-form-item label="还本方式">
	        	<k-field-text v-model="formData.eePrincPaymentType"/>
	     	</k-form-item>
			<k-form-item label="分期还本条款标识">
	        	<k-field-text v-model="formData.eeInstallRepayType"/>
	     	</k-form-item>
			<k-form-item label="基础资产类型">
	        	<k-field-text v-model="formData.eeBaseAssetType"/>
	     	</k-form-item>
			<k-form-item label="超额收益分配比例%">
	        	<k-field-text v-model="formData.eePercentExcInAllot"/>
	     	</k-form-item>
			<k-form-item label="融资人">
	        	<k-field-text v-model="formData.eeDebtor"/>
	     	</k-form-item>
			<k-form-item label="融资人内部信用评级">
	        	<k-field-text v-model="formData.eeDeptorRate"/>
	     	</k-form-item>
			<k-form-item label="外部评级机构名称及对融资人评级结果">
	        	<k-field-text v-model="formData.eeRateAgencyIss"/>
	     	</k-form-item>
			<k-form-item label="融资人类型">
	        	<k-field-text v-model="formData.eeDebtorTypeScale"/>
	     	</k-form-item>
			<k-form-item label="融资人类型">
	        	<k-field-text v-model="formData.eeDebtorTypeTech"/>
	     	</k-form-item>
			<k-form-item label="融资人类型">
	        	<k-field-text v-model="formData.eeDebtorTypeEconomic"/>
	     	</k-form-item>
			<k-form-item label="融资项目">
	        	<k-field-text v-model="formData.eeProject"/>
	     	</k-form-item>
			<k-form-item label="融资人所属行业">
	        	<k-field-text v-model="formData.eeIndustryDebtor"/>
	     	</k-form-item>
			<k-form-item label="融资项目所属行业">
	        	<k-field-text v-model="formData.eeIndustryProject"/>
	     	</k-form-item>
			<k-form-item label="项目是否属于重点监控行业和领域">
	        	<k-field-text v-model="formData.eeMonitorIndusType"/>
	     	</k-form-item>
			<k-form-item label="重点监控行业和领域类别">
	        	<k-field-text v-model="formData.eeMonitorIndustryType"/>
	     	</k-form-item>
			<k-form-item label="重点监控行业和领域类别说明">
	        	<k-field-text v-model="formData.eeDetailsMonitoryType"/>
	     	</k-form-item>
			<k-form-item label="担保方式">
	        	<k-field-text v-model="formData.eeGuaranteeMethod"/>
	     	</k-form-item>
			<k-form-item label="担保情况说明">
	        	<k-field-text v-model="formData.eeDetailGuaranteeStatus"/>
	     	</k-form-item>
			<k-form-item label="抵质押物类型">
	        	<k-field-text v-model="formData.eePledgeType"/>
	     	</k-form-item>
			<k-form-item label="抵质押物价值">
	        	<k-field-text v-model="formData.eePledgeValue"/>
	     	</k-form-item>
			<k-form-item label="担保性质">
	        	<k-field-text v-model="formData.eeGuaranteeType"/>
	     	</k-form-item>
			<k-form-item label="担保人与融资人关系">
	        	<k-field-text v-model="formData.eeGuarantorType"/>
	     	</k-form-item>
			<k-form-item label="融资人主体评级">
	        	<k-field-text v-model="formData.eeDebtorRate"/>
	     	</k-form-item>
			<k-form-item label="资产内部评级">
	        	<k-field-text v-model="formData.eeInterAssetRate"/>
	     	</k-form-item>
			<k-form-item label="资产外部评级">
	        	<k-field-text v-model="formData.eeOutAssetRate"/>
	     	</k-form-item>
			<k-form-item label="含权类型">
	        	<k-field-text v-model="formData.eeOptionType"/>
	     	</k-form-item>
			<k-form-item label="行权方式">
	        	<k-field-text v-model="formData.eeExerciseDateType"/>
	     	</k-form-item>
			<k-form-item label="固定行权日">
	        	<k-field-text v-model="formData.eeFixedExerciseDate"/>
	     	</k-form-item>
			<k-form-item label="首次行权日期">
	        	<k-field-text v-model="formData.eeFirstExerciseDate"/>
	     	</k-form-item>
			<k-form-item label="行权周期">
	        	<k-field-text v-model="formData.eeExercisePeriod"/>
	     	</k-form-item>
			<k-form-item label="行权价格">
	        	<k-field-text v-model="formData.eeExercisePrice"/>
	     	</k-form-item>
			<k-form-item label="永续条款类型">
	        	<k-field-text v-model="formData.eePerpetualType"/>
	     	</k-form-item>
			<k-form-item label="利息递延条款类型">
	        	<k-field-text v-model="formData.eeDeferreInterestType"/>
	     	</k-form-item>
			<k-form-item label="递延利息是否计息">
	        	<k-field-text v-model="formData.eeInterestDeferred"/>
	     	</k-form-item>
			<k-form-item label="首次重定价日期">
	        	<k-field-text v-model="formData.eeFirstRepriceDate"/>
	     	</k-form-item>
			<k-form-item label="重定价周期">
	        	<k-field-text v-model="formData.eeRepricePeriod"/>
	     	</k-form-item>
			<k-form-item label="部分赎回标识">
	        	<k-field-text v-model="formData.eePartialRedemption"/>
	     	</k-form-item>
			<k-form-item label="部分赎回比例%">
	        	<k-field-text v-model="formData.eePartialRedemptionRate"/>
	     	</k-form-item>
			<k-form-item label="选择权">
	        	<k-field-text v-model="formData.eeOptionRight"/>
	     	</k-form-item>
			<k-form-item label="行权条件说明">
	        	<k-field-text v-model="formData.eeDetailsExerciseTerm"/>
	     	</k-form-item>
			<k-form-item label="融资人所属地区">
	        	<k-field-text v-model="formData.eeRegionDebtor"/>
	     	</k-form-item>
			<k-form-item label="增信机构代码">
	        	<k-field-text v-model="formData.eeEnhanceInstituteCode"/>
	     	</k-form-item>
			<k-form-item label="增信机构名称">
	        	<k-field-text v-model="formData.eeEnhanceInstituteName"/>
	     	</k-form-item>
			<k-form-item label="融资总费率">
	        	<k-field-text v-model="formData.eeTotalFeeRate"/>
	     	</k-form-item>
			<k-form-item label="融资人组织机构">
	        	<k-field-text v-model="formData.eeOrganizationCode"/>
	     	</k-form-item>
			<k-form-item label="是否收/受益权">
	        	<k-field-text v-model="formData.ffOwnership"/>
	     	</k-form-item>
			<k-form-item label="是否买入反售">
	        	<k-field-text v-model="formData.ffBuyback"/>
	     	</k-form-item>
			<k-form-item label="类型">
	        	<k-field-text v-model="formData.ffType"/>
	     	</k-form-item>
			<k-form-item label="数量">
	        	<k-field-text v-model="formData.ffQuantity"/>
	     	</k-form-item>
			<k-form-item label="合计金额">
	        	<k-field-text v-model="formData.ffAggregateAmt"/>
	     	</k-form-item>
			<k-form-item label="加权剩余期限">
	        	<k-field-text v-model="formData.ffWeightRemainDay"/>
	     	</k-form-item>
			<k-form-item label="最长剩余期限">
	        	<k-field-text v-model="formData.ffMaxRemainDay"/>
	     	</k-form-item>
			<k-form-item label="最短剩余期限">
	        	<k-field-text v-model="formData.ffMinRemainFay"/>
	     	</k-form-item>
			<k-form-item label="到期日">
	        	<k-field-text v-model="formData.ffMaturityDate"/>
	     	</k-form-item>
			<k-form-item label="起息日">
	        	<k-field-text v-model="formData.ffValueDate"/>
	     	</k-form-item>
			<k-form-item label="贴现利率%">
	        	<k-field-text v-model="formData.ffDiscountRate"/>
	     	</k-form-item>
			<k-form-item label="行业">
	        	<k-field-text v-model="formData.ffIndustry"/>
	     	</k-form-item>
			<k-form-item label="股票代码">
	        	<k-field-text v-model="formData.ggStockCode"/>
	     	</k-form-item>
			<k-form-item label="股票/企业名称">
	        	<k-field-text v-model="formData.ggName"/>
	     	</k-form-item>
			<k-form-item label="股票类型">
	        	<k-field-text v-model="formData.ggStockType"/>
	     	</k-form-item>
			<k-form-item label="行业">
	        	<k-field-text v-model="formData.ggIndustry"/>
	     	</k-form-item>
			<k-form-item label="投资阶段">
	        	<k-field-text v-model="formData.ggInvestStage"/>
	     	</k-form-item>
			<k-form-item label="股权退出安排">
	        	<k-field-text v-model="formData.ggEquityOutDate"/>
	     	</k-form-item>
			<k-form-item label="企业类型">
	        	<k-field-text v-model="formData.ggEnterTypeScale"/>
	     	</k-form-item>
			<k-form-item label="企业类型">
	        	<k-field-text v-model="formData.ggEnterTypeTech"/>
	     	</k-form-item>
			<k-form-item label="企业类型">
	        	<k-field-text v-model="formData.ggEnterTypeEconomic"/>
	     	</k-form-item>
			<k-form-item label="是否质押融资">
	        	<k-field-text v-model="formData.ggPledgedFinace"/>
	     	</k-form-item>
			<k-form-item label="是否为债转股">
	        	<k-field-text v-model="formData.ggDebtEquitySwap"/>
	     	</k-form-item>
			<k-form-item label="名称">
	        	<k-field-text v-model="formData.hhName"/>
	     	</k-form-item>
			<k-form-item label="名义本金">
	        	<k-field-text v-model="formData.hhNominalPrincipal"/>
	     	</k-form-item>
			<k-form-item label="标的类别">
	        	<k-field-text v-model="formData.hhUnderAssetType"/>
	     	</k-form-item>
			<k-form-item label="持有目的">
	        	<k-field-text v-model="formData.hhHoldObjective"/>
	     	</k-form-item>
			<k-form-item label="所属国家或地区">
	        	<k-field-text v-model="formData.iiCountyRegion"/>
	     	</k-form-item>
			<k-form-item label="债券名称">
	        	<k-field-text v-model="formData.iiBondName"/>
	     	</k-form-item>
			<k-form-item label="债券代码">
	        	<k-field-text v-model="formData.iiBondIdentCode"/>
	     	</k-form-item>
			<k-form-item label="发行机构">
	        	<k-field-text v-model="formData.iiIssuer"/>
	     	</k-form-item>
			<k-form-item label="发行机构所属行业">
	        	<k-field-text v-model="formData.iiIndustryIssuer"/>
	     	</k-form-item>
			<k-form-item label="起息日">
	        	<k-field-text v-model="formData.iiValueDate"/>
	     	</k-form-item>
			<k-form-item label="到期日">
	        	<k-field-text v-model="formData.iiMaturityDate"/>
	     	</k-form-item>
			<k-form-item label="期限">
	        	<k-field-text v-model="formData.iiTermMaturity"/>
	     	</k-form-item>
			<k-form-item label="发行机构主体信用评级">
	        	<k-field-text v-model="formData.iiIssuerRateBond"/>
	     	</k-form-item>
			<k-form-item label="债券信用评级">
	        	<k-field-text v-model="formData.iiBondRate"/>
	     	</k-form-item>
			<k-form-item label="票面利率%">
	        	<k-field-text v-model="formData.iiCoupRate"/>
	     	</k-form-item>
			<k-form-item label="付息频率">
	        	<k-field-text v-model="formData.iiInterestPayQuency"/>
	     	</k-form-item>
			<k-form-item label="担保情况说明">
	        	<k-field-text v-model="formData.iiDetailsAssureStatus"/>
	     	</k-form-item>
			<k-form-item label="是否含权等特殊条款情况说明">
	        	<k-field-text v-model="formData.iiDetailsSpecialTerms"/>
	     	</k-form-item>
			<k-form-item label="所属国家或地区">
	        	<k-field-text v-model="formData.jjCountry"/>
	     	</k-form-item>
			<k-form-item label="起息日">
	        	<k-field-text v-model="formData.jjValueDate"/>
	     	</k-form-item>
			<k-form-item label="到期日">
	        	<k-field-text v-model="formData.jjMaturityDate"/>
	     	</k-form-item>
			<k-form-item label="对手方">
	        	<k-field-text v-model="formData.jjCounterparty"/>
	     	</k-form-item>
			<k-form-item label="年利率%">
	        	<k-field-text v-model="formData.jjInterestRate"/>
	     	</k-form-item>
			<k-form-item label="计息基础">
	        	<k-field-text v-model="formData.jjInterestBasis"/>
	     	</k-form-item>
			<k-form-item label="所属国家或地区">
	        	<k-field-text v-model="formData.kkCountry"/>
	     	</k-form-item>
			<k-form-item label="股票/基金代码">
	        	<k-field-text v-model="formData.kkIdentCode"/>
	     	</k-form-item>
			<k-form-item label="股票/基金名称">
	        	<k-field-text v-model="formData.kkName"/>
	     	</k-form-item>
			<k-form-item label="发行机构">
	        	<k-field-text v-model="formData.kkIssuer"/>
	     	</k-form-item>
			<k-form-item label="行业">
	        	<k-field-text v-model="formData.kkIndustry"/>
	     	</k-form-item>
			<k-form-item label="所属国家或地区">
	        	<k-field-text v-model="formData.llCountry"/>
	     	</k-form-item>
			<k-form-item label="合约名称">
	        	<k-field-text v-model="formData.llContractName"/>
	     	</k-form-item>
			<k-form-item label="起息日">
	        	<k-field-text v-model="formData.llValueDate"/>
	     	</k-form-item>
			<k-form-item label="到期日">
	        	<k-field-text v-model="formData.llMaturityDate"/>
	     	</k-form-item>
			<k-form-item label="票面利率%">
	        	<k-field-text v-model="formData.llCouponRate"/>
	     	</k-form-item>
			<k-form-item label="付息频率">
	        	<k-field-text v-model="formData.llInterestFrequency"/>
	     	</k-form-item>
			<k-form-item label="固定收益部分所占比例%">
	        	<k-field-text v-model="formData.llPercentFix"/>
	     	</k-form-item>
			<k-form-item label="衍生金融工具所占比例%">
	        	<k-field-text v-model="formData.llPercentDerivate"/>
	     	</k-form-item>
			<k-form-item label="衍生金融工具具体投资方式">
	        	<k-field-text v-model="formData.llDerivateInvetType"/>
	     	</k-form-item>
			<k-form-item label="衍生金融工具挂钩标的资产">
	        	<k-field-text v-model="formData.llUnderAsset"/>
	     	</k-form-item>
			<k-form-item label="利息结算方式">
	        	<k-field-text v-model="formData.llDetailsProceeds"/>
	     	</k-form-item>
			<k-form-item label="含权情况说明">
	        	<k-field-text v-model="formData.llDetailsOption"/>
	     	</k-form-item>
			<k-form-item label="结构性票据最高收益率%">
	        	<k-field-text v-model="formData.llMaxNoteReturn"/>
	     	</k-form-item>
			<k-form-item label="机构性票据最低收益率%">
	        	<k-field-text v-model="formData.llMinNoteReturn"/>
	     	</k-form-item>
			<k-form-item label="挂钩标的资产基准价格">
	        	<k-field-text v-model="formData.llStrikeUnderAsset"/>
	     	</k-form-item>
			<k-form-item label="挂钩标的资产登记日价格">
	        	<k-field-text v-model="formData.llUnderRgPrice"/>
	     	</k-form-item>
			<k-form-item label="交易费">
	        	<k-field-text v-model="formData.llTransCosts"/>
	     	</k-form-item>
			<k-form-item label="资管计划名称">
	        	<k-field-text v-model="formData.mmManagePlanName"/>
	     	</k-form-item>
			<k-form-item label="是否由金融资产投资公司发行">
	        	<k-field-text v-model="formData.mmIssuedAssetCompany"/>
	     	</k-form-item>
			<k-form-item label="资管计划发起人机构编码">
	        	<k-field-text v-model="formData.mmPlanIssuerCode"/>
	     	</k-form-item>
			<k-form-item label="资管计划登记编码">
	        	<k-field-text v-model="formData.mmAssetPlanRgCode"/>
	     	</k-form-item>
			<k-form-item label="管理人">
	        	<k-field-text v-model="formData.mmManager"/>
	     	</k-form-item>
			<k-form-item label="托管人">
	        	<k-field-text v-model="formData.mmCustodian"/>
	     	</k-form-item>
			<k-form-item label="金额">
	        	<k-field-text v-model="formData.mmAmt"/>
	     	</k-form-item>
			<k-form-item label="资金实际投向">
	        	<k-field-text v-model="formData.mmActualDirect"/>
	     	</k-form-item>
			<k-form-item label="资金运用方式">
	        	<k-field-text v-model="formData.mmDetailsInvest"/>
	     	</k-form-item>
			<k-form-item label="资金运用行业">
	        	<k-field-text v-model="formData.mmIndustryInvest"/>
	     	</k-form-item>
			<k-form-item label="资管计划成立日期">
	        	<k-field-text v-model="formData.mmPlanStartDate"/>
	     	</k-form-item>
			<k-form-item label="资管计划终止日期">
	        	<k-field-text v-model="formData.mmPlanMaturityDate"/>
	     	</k-form-item>
			<k-form-item label="资管计划属性">
	        	<k-field-text v-model="formData.mmPlanType"/>
	     	</k-form-item>
			<k-form-item label="是否有预期收益率">
	        	<k-field-text v-model="formData.mmExpectedReturn"/>
	     	</k-form-item>
			<k-form-item label="预期最高收益率%">
	        	<k-field-text v-model="formData.mmMaxExpectedReturn"/>
	     	</k-form-item>
			<k-form-item label="预期最低收益率%">
	        	<k-field-text v-model="formData.mmMinExpectedReturn"/>
	     	</k-form-item>
			<k-form-item label="购买结构">
	        	<k-field-text v-model="formData.mmInvestStructure"/>
	     	</k-form-item>
			<k-form-item label="管理方式">
	        	<k-field-text v-model="formData.mmManagerType"/>
	     	</k-form-item>
			<k-form-item label="管理费率%">
	        	<k-field-text v-model="formData.mmManagerFeeRate"/>
	     	</k-form-item>
			<k-form-item label="托管费率%">
	        	<k-field-text v-model="formData.mmCustodianFeeRate"/>
	     	</k-form-item>
			<k-form-item label="交易相关合计费率%">
	        	<k-field-text v-model="formData.mmTransCostRate"/>
	     	</k-form-item>
			<k-form-item label="中介服务机构合计费率%">
	        	<k-field-text v-model="formData.mmInterFeeRate"/>
	     	</k-form-item>
			<k-form-item label="其他合计费率%">
	        	<k-field-text v-model="formData.mmOtherExpenseRate"/>
	     	</k-form-item>
			<k-form-item label="所属国家或地区">
	        	<k-field-text v-model="formData.nnCountry"/>
	     	</k-form-item>
			<k-form-item label="名称">
	        	<k-field-text v-model="formData.nnName"/>
	     	</k-form-item>
			<k-form-item label="期限">
	        	<k-field-text v-model="formData.nnTermDays"/>
	     	</k-form-item>
			<k-form-item label="资产价值">
	        	<k-field-text v-model="formData.nnAssetValue"/>
	     	</k-form-item>
			<k-form-item label="资产收益率%">
	        	<k-field-text v-model="formData.nnAssetReturn"/>
	     	</k-form-item>
			<k-form-item label="所属国家或地区">
	        	<k-field-text v-model="formData.ooCountry"/>
	     	</k-form-item>
			<k-form-item label="名称">
	        	<k-field-text v-model="formData.ooName"/>
	     	</k-form-item>
			<k-form-item label="起息日">
	        	<k-field-text v-model="formData.ooValueDate"/>
	     	</k-form-item>
			<k-form-item label="到期日">
	        	<k-field-text v-model="formData.ooMaturityDate"/>
	     	</k-form-item>
			<k-form-item label="资产价值">
	        	<k-field-text v-model="formData.ooAssetValue"/>
	     	</k-form-item>
			<k-form-item label="资产收益率%">
	        	<k-field-text v-model="formData.ooAssetReturn"/>
	     	</k-form-item>
			<k-form-item label="基金代码">
	        	<k-field-text v-model="formData.ppFundCode"/>
	     	</k-form-item>
			<k-form-item label="基金名称">
	        	<k-field-text v-model="formData.ppFundName"/>
	     	</k-form-item>
			<k-form-item label="是否由金融资产投资公司发行">
	        	<k-field-text v-model="formData.ppIssuedAssetCompany"/>
	     	</k-form-item>
			<k-form-item label="行业">
	        	<k-field-text v-model="formData.ppIndustry"/>
	     	</k-form-item>
			<k-form-item label="登记备案机构">
	        	<k-field-text v-model="formData.ppRegistAgency"/>
	     	</k-form-item>
			<k-form-item label="是否属于政府投资基金">
	        	<k-field-text v-model="formData.ppGovernInvestFund"/>
	     	</k-form-item>
			<k-form-item label="政府投资基金投向">
	        	<k-field-text v-model="formData.ppDirectGovernFund"/>
	     	</k-form-item>
			<k-form-item label="基金公司名称">
	        	<k-field-text v-model="formData.ppTaName"/>
	     	</k-form-item>
			<k-form-item label="基金管理机构名称">
	        	<k-field-text v-model="formData.ppManagerFundName"/>
	     	</k-form-item>
			<k-form-item label="基金托管机构名称">
	        	<k-field-text v-model="formData.ppCustodianFundName"/>
	     	</k-form-item>
			<k-form-item label="投资阶段">
	        	<k-field-text v-model="formData.ppInvestStage"/>
	     	</k-form-item>
			<k-form-item label="投资企业类型">
	        	<k-field-text v-model="formData.ppEnterTypeScale"/>
	     	</k-form-item>
			<k-form-item label="投资企业类型">
	        	<k-field-text v-model="formData.ppEnterTypeTech"/>
	     	</k-form-item>
			<k-form-item label="投资企业类型">
	        	<k-field-text v-model="formData.ppEnterTypeEconomic"/>
	     	</k-form-item>
			<k-form-item label="基金投资资产">
	        	<k-field-text v-model="formData.ppInvestAssets"/>
	     	</k-form-item>
			<k-form-item label="委外投资协议名称">
	        	<k-field-text v-model="formData.qqOutAgreementName"/>
	     	</k-form-item>
			<k-form-item label="委外投资协议编号">
	        	<k-field-text v-model="formData.qqOutAgreementCode"/>
	     	</k-form-item>
			<k-form-item label="受托人">
	        	<k-field-text v-model="formData.qqTrustee"/>
	     	</k-form-item>
			<k-form-item label="实际管理人">
	        	<k-field-text v-model="formData.qqActualManager"/>
	     	</k-form-item>
			<k-form-item label="托管人">
	        	<k-field-text v-model="formData.qqCustodian"/>
	     	</k-form-item>
			<k-form-item label="委托投资金额">
	        	<k-field-text v-model="formData.qqOutAmt"/>
	     	</k-form-item>
			<k-form-item label="资金实际投向">
	        	<k-field-text v-model="formData.qqActualDirection"/>
	     	</k-form-item>
			<k-form-item label="资金运用方式">
	        	<k-field-text v-model="formData.qqDetailsInvest"/>
	     	</k-form-item>
			<k-form-item label="资金运用行业">
	        	<k-field-text v-model="formData.qqIndustryInvest"/>
	     	</k-form-item>
			<k-form-item label="投资运作起始日期">
	        	<k-field-text v-model="formData.qqValueDate"/>
	     	</k-form-item>
			<k-form-item label="投资运作终止日期">
	        	<k-field-text v-model="formData.qqMaturityDate"/>
	     	</k-form-item>
			<k-form-item label="委外投资属性">
	        	<k-field-text v-model="formData.qqOutType"/>
	     	</k-form-item>
			<k-form-item label="是否有预期收益率">
	        	<k-field-text v-model="formData.qqExpectedReturn"/>
	     	</k-form-item>
			<k-form-item label="预期最高收益率%">
	        	<k-field-text v-model="formData.qqMaxExpectedReturn"/>
	     	</k-form-item>
			<k-form-item label="预期最低收益率%">
	        	<k-field-text v-model="formData.qqMinExpectedReturn"/>
	     	</k-form-item>
			<k-form-item label="管理费率%">
	        	<k-field-text v-model="formData.qqManagerFeeRate"/>
	     	</k-form-item>
			<k-form-item label="托管费率%">
	        	<k-field-text v-model="formData.qqCustodianFeeRate"/>
	     	</k-form-item>
			<k-form-item label="交易相关合计费率%">
	        	<k-field-text v-model="formData.qqTransCostRate"/>
	     	</k-form-item>
			<k-form-item label="中介服务机构合计费率%">
	        	<k-field-text v-model="formData.qqInterFeeRate"/>
	     	</k-form-item>
			<k-form-item label="其他合计费率%">
	        	<k-field-text v-model="formData.qqOtherExpensesRate"/>
	     	</k-form-item>
			<k-form-item label="所属国家或地区">
	        	<k-field-text v-model="formData.rrCountry"/>
	     	</k-form-item>
			<k-form-item label="名称">
	        	<k-field-text v-model="formData.rrName"/>
	     	</k-form-item>
			<k-form-item label="期限">
	        	<k-field-text v-model="formData.rrTermMaturity"/>
	     	</k-form-item>
			<k-form-item label="负债规模">
	        	<k-field-text v-model="formData.rrLiabilityAmt"/>
	     	</k-form-item>
			<k-form-item label="利率%">
	        	<k-field-text v-model="formData.rrInterestRate"/>
	     	</k-form-item>
			<k-form-item label="名称">
	        	<k-field-text v-model="formData.ssName"/>
	     	</k-form-item>
			<k-form-item label="行内资产类别">
	        	<k-field-text v-model="formData.ssAssetType"/>
	     	</k-form-item>
			<k-form-item label="行内资产类别说明">
	        	<k-field-text v-model="formData.ssDetailsAssetType"/>
	     	</k-form-item>
			<k-form-item label="金额">
	        	<k-field-text v-model="formData.ssAmt"/>
	     	</k-form-item>
			<k-form-item label="起息日">
	        	<k-field-text v-model="formData.ssValueDate"/>
	     	</k-form-item>
			<k-form-item label="到期日">
	        	<k-field-text v-model="formData.ssMaturityDate"/>
	     	</k-form-item>
			<k-form-item label="所属国家或地区">
	        	<k-field-text v-model="formData.ssCountry"/>
	     	</k-form-item>
			<k-form-item label="是否有预期收益率">
	        	<k-field-text v-model="formData.ssExpectedReturn"/>
	     	</k-form-item>
			<k-form-item label="项目预期收益率">
	        	<k-field-text v-model="formData.ssAnnualReturn"/>
	     	</k-form-item>
			<k-form-item label="付息频率">
	        	<k-field-text v-model="formData.ssInterestFrequency"/>
	     	</k-form-item>
			<k-form-item label="融资人">
	        	<k-field-text v-model="formData.ssDebtor"/>
	     	</k-form-item>
			<k-form-item label="融资人组织结构">
	        	<k-field-text v-model="formData.ssOrganCode"/>
	     	</k-form-item>
			<k-form-item label="外部评级机构名称及对融资人评级结果">
	        	<k-field-text v-model="formData.ssRateAgencyIss"/>
	     	</k-form-item>
			<k-form-item label="融资人类型">
	        	<k-field-text v-model="formData.ssDebtorTypeScale"/>
	     	</k-form-item>
			<k-form-item label="融资人类型">
	        	<k-field-text v-model="formData.ssDebtorTypeTech"/>
	     	</k-form-item>
			<k-form-item label="融资人类型">
	        	<k-field-text v-model="formData.ssDebtorTypeEconomic"/>
	     	</k-form-item>
			<k-form-item label="融资项目">
	        	<k-field-text v-model="formData.ssProject"/>
	     	</k-form-item>
			<k-form-item label="融资人所属行业">
	        	<k-field-text v-model="formData.ssIndustryDebtor"/>
	     	</k-form-item>
			<k-form-item label="融资项目所属行业">
	        	<k-field-text v-model="formData.ssIndustryProject"/>
	     	</k-form-item>
			<k-form-item label="项目是否属于重点监控行业和领域">
	        	<k-field-text v-model="formData.ssMonitoryIndustry"/>
	     	</k-form-item>
			<k-form-item label="重点监控行业和领域类别">
	        	<k-field-text v-model="formData.ssMonitoryIndustryType"/>
	     	</k-form-item>
			<k-form-item label="重点监控行业和领域类别说明">
	        	<k-field-text v-model="formData.ssDetailsMonitoryType"/>
	     	</k-form-item>
			<k-form-item label="对应资产外部评级">
	        	<k-field-text v-model="formData.ssInternalAssetRate"/>
	     	</k-form-item>
			<k-form-item label="担保方式">
	        	<k-field-text v-model="formData.ssGuaranteeMethod"/>
	     	</k-form-item>
			<k-form-item label="担保情况说明">
	        	<k-field-text v-model="formData.ssDetailsGuarantee"/>
	     	</k-form-item>
			<k-form-item label="抵质押物类型">
	        	<k-field-text v-model="formData.ssPledgeType"/>
	     	</k-form-item>
			<k-form-item label="抵质押物价值">
	        	<k-field-text v-model="formData.ssPledgeValue"/>
	     	</k-form-item>
			<k-form-item label="担保性质">
	        	<k-field-text v-model="formData.ssGuaranteeType"/>
	     	</k-form-item>
			<k-form-item label="担保人与融资人关系">
	        	<k-field-text v-model="formData.ssGuarantorType"/>
	     	</k-form-item>
			<k-form-item label="是否为债转股">
	        	<k-field-text v-model="formData.ssDebtEquitySwap"/>
	     	</k-form-item>
			<k-form-item label="id">
	        	<k-field-text v-model="formData.id"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AssetDebtRegisterInfoh.addAssetDebtRegisterInfoh" data-from="addAssetDebtRegisterInfohForm"
		               :data-model="formData" data-target="assetDebtRegisterInfohGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改资产负债登记历史信息弹出框   -->
	<k-popup ref="editAssetDebtRegisterInfohPopup" data-title="修改">
	  <k-form ref="editAssetDebtRegisterInfohForm" :data-col="2">
		<k-form-item label="登记流水号">
        	<k-field-text v-model="formData.registerSerno"/>
     	</k-form-item>
		<k-form-item label="导入日期">
        	<k-field-text v-model="formData.impDate"/>
     	</k-form-item>
		<k-form-item label="投资者登记日期">
        	<k-field-text v-model="formData.registerDate"/>
     	</k-form-item>
		<k-form-item label="登记状态（0">
        	<k-field-text v-model="formData.registerStatus"/>
     	</k-form-item>
		<k-form-item label="发行机构代码">
        	<k-field-text v-model="formData.bankCode"/>
     	</k-form-item>
		<k-form-item label="行内资产/负债编码">
        	<k-field-text v-model="formData.assetCode"/>
     	</k-form-item>
		<k-form-item label="资产/负债类别">
        	<k-field-text v-model="formData.assDebtType"/>
     	</k-form-item>
		<k-form-item label="币种">
        	<k-field-text v-model="formData.cur"/>
     	</k-form-item>
		<k-form-item label="交易流通场所">
        	<k-field-text v-model="formData.tradeVenue"/>
     	</k-form-item>
		<k-form-item label="备注">
        	<k-field-text v-model="formData.details"/>
     	</k-form-item>
		<k-form-item label="资金存入银行">
        	<k-field-text v-model="formData.bbDepositBank"/>
     	</k-form-item>
		<k-form-item label="存款金额">
        	<k-field-text v-model="formData.bbDepositAmt"/>
     	</k-form-item>
		<k-form-item label="起息日">
        	<k-field-text v-model="formData.bbValueDate"/>
     	</k-form-item>
		<k-form-item label="到期日">
        	<k-field-text v-model="formData.bbMaturityDate"/>
     	</k-form-item>
		<k-form-item label="年利率%">
        	<k-field-text v-model="formData.bbAnnualRate"/>
     	</k-form-item>
		<k-form-item label="计息基础">
        	<k-field-text v-model="formData.bbInterestBasis"/>
     	</k-form-item>
		<k-form-item label="存款类型">
        	<k-field-text v-model="formData.bbDepositType"/>
     	</k-form-item>
		<k-form-item label="结构性存款标的类别">
        	<k-field-text v-model="formData.bbStructDepositType"/>
     	</k-form-item>
		<k-form-item label="结构性存款挂钩标的">
        	<k-field-text v-model="formData.bbStructDeposit"/>
     	</k-form-item>
		<k-form-item label="代码">
        	<k-field-text v-model="formData.ccIdentCode"/>
     	</k-form-item>
		<k-form-item label="名称">
        	<k-field-text v-model="formData.ccName"/>
     	</k-form-item>
		<k-form-item label="具体类别">
        	<k-field-text v-model="formData.ccSpecificBondType"/>
     	</k-form-item>
		<k-form-item label="发行方式">
        	<k-field-text v-model="formData.ccIssModeBond"/>
     	</k-form-item>
		<k-form-item label="主体评级">
        	<k-field-text v-model="formData.ccIssRatePart"/>
     	</k-form-item>
		<k-form-item label="发行机构类型">
        	<k-field-text v-model="formData.ccInstituteTypeScale"/>
     	</k-form-item>
		<k-form-item label="发行机构类型">
        	<k-field-text v-model="formData.ccInstituteTypeTech"/>
     	</k-form-item>
		<k-form-item label="发行机构类型">
        	<k-field-text v-model="formData.ccInstituteTypeEconomic"/>
     	</k-form-item>
		<k-form-item label="发行机构所属行业">
        	<k-field-text v-model="formData.ccIndustryIssuer"/>
     	</k-form-item>
		<k-form-item label="登记托管机构">
        	<k-field-text v-model="formData.ccRegistDeposit"/>
     	</k-form-item>
		<k-form-item label="登记托管机构说明">
        	<k-field-text v-model="formData.ccDetailsRegistDeposit"/>
     	</k-form-item>
		<k-form-item label="起息日">
        	<k-field-text v-model="formData.ddValueDate"/>
     	</k-form-item>
		<k-form-item label="到期日">
        	<k-field-text v-model="formData.ddMaturityDate"/>
     	</k-form-item>
		<k-form-item label="对手方">
        	<k-field-text v-model="formData.ddCounterparty"/>
     	</k-form-item>
		<k-form-item label="对手方类型">
        	<k-field-text v-model="formData.ddCounterpartyType"/>
     	</k-form-item>
		<k-form-item label="年利率%">
        	<k-field-text v-model="formData.ddAnnalInterestRate"/>
     	</k-form-item>
		<k-form-item label="计息基础">
        	<k-field-text v-model="formData.ddInterestBasis"/>
     	</k-form-item>
		<k-form-item label="回购标的类别">
        	<k-field-text v-model="formData.ddCollateralType"/>
     	</k-form-item>
		<k-form-item label="回购标的金额">
        	<k-field-text v-model="formData.ddCollateralValue"/>
     	</k-form-item>
		<k-form-item label="名称">
        	<k-field-text v-model="formData.eeName"/>
     	</k-form-item>
		<k-form-item label="金额">
        	<k-field-text v-model="formData.eeAmt"/>
     	</k-form-item>
		<k-form-item label="份额面值">
        	<k-field-text v-model="formData.eeUnitParValue"/>
     	</k-form-item>
		<k-form-item label="收/受权益类型">
        	<k-field-text v-model="formData.eeOwnershipType"/>
     	</k-form-item>
		<k-form-item label="是否属于买入反售">
        	<k-field-text v-model="formData.eeBuyback"/>
     	</k-form-item>
		<k-form-item label="起息日">
        	<k-field-text v-model="formData.eeValueDate"/>
     	</k-form-item>
		<k-form-item label="到期日">
        	<k-field-text v-model="formData.eeMaturityDate"/>
     	</k-form-item>
		<k-form-item label="法定到期日">
        	<k-field-text v-model="formData.eeStatutoryMaturityDate"/>
     	</k-form-item>
		<k-form-item label="是否有预期收益率">
        	<k-field-text v-model="formData.eeExpectedReturn"/>
     	</k-form-item>
		<k-form-item label="项目收益率">
        	<k-field-text v-model="formData.eeProjectAnnaulReturn"/>
     	</k-form-item>
		<k-form-item label="计息类型">
        	<k-field-text v-model="formData.eeCouponType"/>
     	</k-form-item>
		<k-form-item label="规则付息标识">
        	<k-field-text v-model="formData.eeRegualrInterestPay"/>
     	</k-form-item>
		<k-form-item label="付息频率">
        	<k-field-text v-model="formData.eeInterestPayFrequency"/>
     	</k-form-item>
		<k-form-item label="利息分布方式">
        	<k-field-text v-model="formData.eeCouponAllocationType"/>
     	</k-form-item>
		<k-form-item label="还本付息情况说明">
        	<k-field-text v-model="formData.eeDetailPrincInterest"/>
     	</k-form-item>
		<k-form-item label="计息基础">
        	<k-field-text v-model="formData.eeInterestBasis"/>
     	</k-form-item>
		<k-form-item label="基准利率种类">
        	<k-field-text v-model="formData.eeBenchRateType"/>
     	</k-form-item>
		<k-form-item label="是否有浮动因子">
        	<k-field-text v-model="formData.eeFloatFactor"/>
     	</k-form-item>
		<k-form-item label="浮动因子%">
        	<k-field-text v-model="formData.eeFloatRate"/>
     	</k-form-item>
		<k-form-item label="利差">
        	<k-field-text v-model="formData.eeYieldSpreadBp"/>
     	</k-form-item>
		<k-form-item label="结构档次">
        	<k-field-text v-model="formData.eeStructGrade"/>
     	</k-form-item>
		<k-form-item label="还本方式">
        	<k-field-text v-model="formData.eePrincPaymentType"/>
     	</k-form-item>
		<k-form-item label="分期还本条款标识">
        	<k-field-text v-model="formData.eeInstallRepayType"/>
     	</k-form-item>
		<k-form-item label="基础资产类型">
        	<k-field-text v-model="formData.eeBaseAssetType"/>
     	</k-form-item>
		<k-form-item label="超额收益分配比例%">
        	<k-field-text v-model="formData.eePercentExcInAllot"/>
     	</k-form-item>
		<k-form-item label="融资人">
        	<k-field-text v-model="formData.eeDebtor"/>
     	</k-form-item>
		<k-form-item label="融资人内部信用评级">
        	<k-field-text v-model="formData.eeDeptorRate"/>
     	</k-form-item>
		<k-form-item label="外部评级机构名称及对融资人评级结果">
        	<k-field-text v-model="formData.eeRateAgencyIss"/>
     	</k-form-item>
		<k-form-item label="融资人类型">
        	<k-field-text v-model="formData.eeDebtorTypeScale"/>
     	</k-form-item>
		<k-form-item label="融资人类型">
        	<k-field-text v-model="formData.eeDebtorTypeTech"/>
     	</k-form-item>
		<k-form-item label="融资人类型">
        	<k-field-text v-model="formData.eeDebtorTypeEconomic"/>
     	</k-form-item>
		<k-form-item label="融资项目">
        	<k-field-text v-model="formData.eeProject"/>
     	</k-form-item>
		<k-form-item label="融资人所属行业">
        	<k-field-text v-model="formData.eeIndustryDebtor"/>
     	</k-form-item>
		<k-form-item label="融资项目所属行业">
        	<k-field-text v-model="formData.eeIndustryProject"/>
     	</k-form-item>
		<k-form-item label="项目是否属于重点监控行业和领域">
        	<k-field-text v-model="formData.eeMonitorIndusType"/>
     	</k-form-item>
		<k-form-item label="重点监控行业和领域类别">
        	<k-field-text v-model="formData.eeMonitorIndustryType"/>
     	</k-form-item>
		<k-form-item label="重点监控行业和领域类别说明">
        	<k-field-text v-model="formData.eeDetailsMonitoryType"/>
     	</k-form-item>
		<k-form-item label="担保方式">
        	<k-field-text v-model="formData.eeGuaranteeMethod"/>
     	</k-form-item>
		<k-form-item label="担保情况说明">
        	<k-field-text v-model="formData.eeDetailGuaranteeStatus"/>
     	</k-form-item>
		<k-form-item label="抵质押物类型">
        	<k-field-text v-model="formData.eePledgeType"/>
     	</k-form-item>
		<k-form-item label="抵质押物价值">
        	<k-field-text v-model="formData.eePledgeValue"/>
     	</k-form-item>
		<k-form-item label="担保性质">
        	<k-field-text v-model="formData.eeGuaranteeType"/>
     	</k-form-item>
		<k-form-item label="担保人与融资人关系">
        	<k-field-text v-model="formData.eeGuarantorType"/>
     	</k-form-item>
		<k-form-item label="融资人主体评级">
        	<k-field-text v-model="formData.eeDebtorRate"/>
     	</k-form-item>
		<k-form-item label="资产内部评级">
        	<k-field-text v-model="formData.eeInterAssetRate"/>
     	</k-form-item>
		<k-form-item label="资产外部评级">
        	<k-field-text v-model="formData.eeOutAssetRate"/>
     	</k-form-item>
		<k-form-item label="含权类型">
        	<k-field-text v-model="formData.eeOptionType"/>
     	</k-form-item>
		<k-form-item label="行权方式">
        	<k-field-text v-model="formData.eeExerciseDateType"/>
     	</k-form-item>
		<k-form-item label="固定行权日">
        	<k-field-text v-model="formData.eeFixedExerciseDate"/>
     	</k-form-item>
		<k-form-item label="首次行权日期">
        	<k-field-text v-model="formData.eeFirstExerciseDate"/>
     	</k-form-item>
		<k-form-item label="行权周期">
        	<k-field-text v-model="formData.eeExercisePeriod"/>
     	</k-form-item>
		<k-form-item label="行权价格">
        	<k-field-text v-model="formData.eeExercisePrice"/>
     	</k-form-item>
		<k-form-item label="永续条款类型">
        	<k-field-text v-model="formData.eePerpetualType"/>
     	</k-form-item>
		<k-form-item label="利息递延条款类型">
        	<k-field-text v-model="formData.eeDeferreInterestType"/>
     	</k-form-item>
		<k-form-item label="递延利息是否计息">
        	<k-field-text v-model="formData.eeInterestDeferred"/>
     	</k-form-item>
		<k-form-item label="首次重定价日期">
        	<k-field-text v-model="formData.eeFirstRepriceDate"/>
     	</k-form-item>
		<k-form-item label="重定价周期">
        	<k-field-text v-model="formData.eeRepricePeriod"/>
     	</k-form-item>
		<k-form-item label="部分赎回标识">
        	<k-field-text v-model="formData.eePartialRedemption"/>
     	</k-form-item>
		<k-form-item label="部分赎回比例%">
        	<k-field-text v-model="formData.eePartialRedemptionRate"/>
     	</k-form-item>
		<k-form-item label="选择权">
        	<k-field-text v-model="formData.eeOptionRight"/>
     	</k-form-item>
		<k-form-item label="行权条件说明">
        	<k-field-text v-model="formData.eeDetailsExerciseTerm"/>
     	</k-form-item>
		<k-form-item label="融资人所属地区">
        	<k-field-text v-model="formData.eeRegionDebtor"/>
     	</k-form-item>
		<k-form-item label="增信机构代码">
        	<k-field-text v-model="formData.eeEnhanceInstituteCode"/>
     	</k-form-item>
		<k-form-item label="增信机构名称">
        	<k-field-text v-model="formData.eeEnhanceInstituteName"/>
     	</k-form-item>
		<k-form-item label="融资总费率">
        	<k-field-text v-model="formData.eeTotalFeeRate"/>
     	</k-form-item>
		<k-form-item label="融资人组织机构">
        	<k-field-text v-model="formData.eeOrganizationCode"/>
     	</k-form-item>
		<k-form-item label="是否收/受益权">
        	<k-field-text v-model="formData.ffOwnership"/>
     	</k-form-item>
		<k-form-item label="是否买入反售">
        	<k-field-text v-model="formData.ffBuyback"/>
     	</k-form-item>
		<k-form-item label="类型">
        	<k-field-text v-model="formData.ffType"/>
     	</k-form-item>
		<k-form-item label="数量">
        	<k-field-text v-model="formData.ffQuantity"/>
     	</k-form-item>
		<k-form-item label="合计金额">
        	<k-field-text v-model="formData.ffAggregateAmt"/>
     	</k-form-item>
		<k-form-item label="加权剩余期限">
        	<k-field-text v-model="formData.ffWeightRemainDay"/>
     	</k-form-item>
		<k-form-item label="最长剩余期限">
        	<k-field-text v-model="formData.ffMaxRemainDay"/>
     	</k-form-item>
		<k-form-item label="最短剩余期限">
        	<k-field-text v-model="formData.ffMinRemainFay"/>
     	</k-form-item>
		<k-form-item label="到期日">
        	<k-field-text v-model="formData.ffMaturityDate"/>
     	</k-form-item>
		<k-form-item label="起息日">
        	<k-field-text v-model="formData.ffValueDate"/>
     	</k-form-item>
		<k-form-item label="贴现利率%">
        	<k-field-text v-model="formData.ffDiscountRate"/>
     	</k-form-item>
		<k-form-item label="行业">
        	<k-field-text v-model="formData.ffIndustry"/>
     	</k-form-item>
		<k-form-item label="股票代码">
        	<k-field-text v-model="formData.ggStockCode"/>
     	</k-form-item>
		<k-form-item label="股票/企业名称">
        	<k-field-text v-model="formData.ggName"/>
     	</k-form-item>
		<k-form-item label="股票类型">
        	<k-field-text v-model="formData.ggStockType"/>
     	</k-form-item>
		<k-form-item label="行业">
        	<k-field-text v-model="formData.ggIndustry"/>
     	</k-form-item>
		<k-form-item label="投资阶段">
        	<k-field-text v-model="formData.ggInvestStage"/>
     	</k-form-item>
		<k-form-item label="股权退出安排">
        	<k-field-text v-model="formData.ggEquityOutDate"/>
     	</k-form-item>
		<k-form-item label="企业类型">
        	<k-field-text v-model="formData.ggEnterTypeScale"/>
     	</k-form-item>
		<k-form-item label="企业类型">
        	<k-field-text v-model="formData.ggEnterTypeTech"/>
     	</k-form-item>
		<k-form-item label="企业类型">
        	<k-field-text v-model="formData.ggEnterTypeEconomic"/>
     	</k-form-item>
		<k-form-item label="是否质押融资">
        	<k-field-text v-model="formData.ggPledgedFinace"/>
     	</k-form-item>
		<k-form-item label="是否为债转股">
        	<k-field-text v-model="formData.ggDebtEquitySwap"/>
     	</k-form-item>
		<k-form-item label="名称">
        	<k-field-text v-model="formData.hhName"/>
     	</k-form-item>
		<k-form-item label="名义本金">
        	<k-field-text v-model="formData.hhNominalPrincipal"/>
     	</k-form-item>
		<k-form-item label="标的类别">
        	<k-field-text v-model="formData.hhUnderAssetType"/>
     	</k-form-item>
		<k-form-item label="持有目的">
        	<k-field-text v-model="formData.hhHoldObjective"/>
     	</k-form-item>
		<k-form-item label="所属国家或地区">
        	<k-field-text v-model="formData.iiCountyRegion"/>
     	</k-form-item>
		<k-form-item label="债券名称">
        	<k-field-text v-model="formData.iiBondName"/>
     	</k-form-item>
		<k-form-item label="债券代码">
        	<k-field-text v-model="formData.iiBondIdentCode"/>
     	</k-form-item>
		<k-form-item label="发行机构">
        	<k-field-text v-model="formData.iiIssuer"/>
     	</k-form-item>
		<k-form-item label="发行机构所属行业">
        	<k-field-text v-model="formData.iiIndustryIssuer"/>
     	</k-form-item>
		<k-form-item label="起息日">
        	<k-field-text v-model="formData.iiValueDate"/>
     	</k-form-item>
		<k-form-item label="到期日">
        	<k-field-text v-model="formData.iiMaturityDate"/>
     	</k-form-item>
		<k-form-item label="期限">
        	<k-field-text v-model="formData.iiTermMaturity"/>
     	</k-form-item>
		<k-form-item label="发行机构主体信用评级">
        	<k-field-text v-model="formData.iiIssuerRateBond"/>
     	</k-form-item>
		<k-form-item label="债券信用评级">
        	<k-field-text v-model="formData.iiBondRate"/>
     	</k-form-item>
		<k-form-item label="票面利率%">
        	<k-field-text v-model="formData.iiCoupRate"/>
     	</k-form-item>
		<k-form-item label="付息频率">
        	<k-field-text v-model="formData.iiInterestPayQuency"/>
     	</k-form-item>
		<k-form-item label="担保情况说明">
        	<k-field-text v-model="formData.iiDetailsAssureStatus"/>
     	</k-form-item>
		<k-form-item label="是否含权等特殊条款情况说明">
        	<k-field-text v-model="formData.iiDetailsSpecialTerms"/>
     	</k-form-item>
		<k-form-item label="所属国家或地区">
        	<k-field-text v-model="formData.jjCountry"/>
     	</k-form-item>
		<k-form-item label="起息日">
        	<k-field-text v-model="formData.jjValueDate"/>
     	</k-form-item>
		<k-form-item label="到期日">
        	<k-field-text v-model="formData.jjMaturityDate"/>
     	</k-form-item>
		<k-form-item label="对手方">
        	<k-field-text v-model="formData.jjCounterparty"/>
     	</k-form-item>
		<k-form-item label="年利率%">
        	<k-field-text v-model="formData.jjInterestRate"/>
     	</k-form-item>
		<k-form-item label="计息基础">
        	<k-field-text v-model="formData.jjInterestBasis"/>
     	</k-form-item>
		<k-form-item label="所属国家或地区">
        	<k-field-text v-model="formData.kkCountry"/>
     	</k-form-item>
		<k-form-item label="股票/基金代码">
        	<k-field-text v-model="formData.kkIdentCode"/>
     	</k-form-item>
		<k-form-item label="股票/基金名称">
        	<k-field-text v-model="formData.kkName"/>
     	</k-form-item>
		<k-form-item label="发行机构">
        	<k-field-text v-model="formData.kkIssuer"/>
     	</k-form-item>
		<k-form-item label="行业">
        	<k-field-text v-model="formData.kkIndustry"/>
     	</k-form-item>
		<k-form-item label="所属国家或地区">
        	<k-field-text v-model="formData.llCountry"/>
     	</k-form-item>
		<k-form-item label="合约名称">
        	<k-field-text v-model="formData.llContractName"/>
     	</k-form-item>
		<k-form-item label="起息日">
        	<k-field-text v-model="formData.llValueDate"/>
     	</k-form-item>
		<k-form-item label="到期日">
        	<k-field-text v-model="formData.llMaturityDate"/>
     	</k-form-item>
		<k-form-item label="票面利率%">
        	<k-field-text v-model="formData.llCouponRate"/>
     	</k-form-item>
		<k-form-item label="付息频率">
        	<k-field-text v-model="formData.llInterestFrequency"/>
     	</k-form-item>
		<k-form-item label="固定收益部分所占比例%">
        	<k-field-text v-model="formData.llPercentFix"/>
     	</k-form-item>
		<k-form-item label="衍生金融工具所占比例%">
        	<k-field-text v-model="formData.llPercentDerivate"/>
     	</k-form-item>
		<k-form-item label="衍生金融工具具体投资方式">
        	<k-field-text v-model="formData.llDerivateInvetType"/>
     	</k-form-item>
		<k-form-item label="衍生金融工具挂钩标的资产">
        	<k-field-text v-model="formData.llUnderAsset"/>
     	</k-form-item>
		<k-form-item label="利息结算方式">
        	<k-field-text v-model="formData.llDetailsProceeds"/>
     	</k-form-item>
		<k-form-item label="含权情况说明">
        	<k-field-text v-model="formData.llDetailsOption"/>
     	</k-form-item>
		<k-form-item label="结构性票据最高收益率%">
        	<k-field-text v-model="formData.llMaxNoteReturn"/>
     	</k-form-item>
		<k-form-item label="机构性票据最低收益率%">
        	<k-field-text v-model="formData.llMinNoteReturn"/>
     	</k-form-item>
		<k-form-item label="挂钩标的资产基准价格">
        	<k-field-text v-model="formData.llStrikeUnderAsset"/>
     	</k-form-item>
		<k-form-item label="挂钩标的资产登记日价格">
        	<k-field-text v-model="formData.llUnderRgPrice"/>
     	</k-form-item>
		<k-form-item label="交易费">
        	<k-field-text v-model="formData.llTransCosts"/>
     	</k-form-item>
		<k-form-item label="资管计划名称">
        	<k-field-text v-model="formData.mmManagePlanName"/>
     	</k-form-item>
		<k-form-item label="是否由金融资产投资公司发行">
        	<k-field-text v-model="formData.mmIssuedAssetCompany"/>
     	</k-form-item>
		<k-form-item label="资管计划发起人机构编码">
        	<k-field-text v-model="formData.mmPlanIssuerCode"/>
     	</k-form-item>
		<k-form-item label="资管计划登记编码">
        	<k-field-text v-model="formData.mmAssetPlanRgCode"/>
     	</k-form-item>
		<k-form-item label="管理人">
        	<k-field-text v-model="formData.mmManager"/>
     	</k-form-item>
		<k-form-item label="托管人">
        	<k-field-text v-model="formData.mmCustodian"/>
     	</k-form-item>
		<k-form-item label="金额">
        	<k-field-text v-model="formData.mmAmt"/>
     	</k-form-item>
		<k-form-item label="资金实际投向">
        	<k-field-text v-model="formData.mmActualDirect"/>
     	</k-form-item>
		<k-form-item label="资金运用方式">
        	<k-field-text v-model="formData.mmDetailsInvest"/>
     	</k-form-item>
		<k-form-item label="资金运用行业">
        	<k-field-text v-model="formData.mmIndustryInvest"/>
     	</k-form-item>
		<k-form-item label="资管计划成立日期">
        	<k-field-text v-model="formData.mmPlanStartDate"/>
     	</k-form-item>
		<k-form-item label="资管计划终止日期">
        	<k-field-text v-model="formData.mmPlanMaturityDate"/>
     	</k-form-item>
		<k-form-item label="资管计划属性">
        	<k-field-text v-model="formData.mmPlanType"/>
     	</k-form-item>
		<k-form-item label="是否有预期收益率">
        	<k-field-text v-model="formData.mmExpectedReturn"/>
     	</k-form-item>
		<k-form-item label="预期最高收益率%">
        	<k-field-text v-model="formData.mmMaxExpectedReturn"/>
     	</k-form-item>
		<k-form-item label="预期最低收益率%">
        	<k-field-text v-model="formData.mmMinExpectedReturn"/>
     	</k-form-item>
		<k-form-item label="购买结构">
        	<k-field-text v-model="formData.mmInvestStructure"/>
     	</k-form-item>
		<k-form-item label="管理方式">
        	<k-field-text v-model="formData.mmManagerType"/>
     	</k-form-item>
		<k-form-item label="管理费率%">
        	<k-field-text v-model="formData.mmManagerFeeRate"/>
     	</k-form-item>
		<k-form-item label="托管费率%">
        	<k-field-text v-model="formData.mmCustodianFeeRate"/>
     	</k-form-item>
		<k-form-item label="交易相关合计费率%">
        	<k-field-text v-model="formData.mmTransCostRate"/>
     	</k-form-item>
		<k-form-item label="中介服务机构合计费率%">
        	<k-field-text v-model="formData.mmInterFeeRate"/>
     	</k-form-item>
		<k-form-item label="其他合计费率%">
        	<k-field-text v-model="formData.mmOtherExpenseRate"/>
     	</k-form-item>
		<k-form-item label="所属国家或地区">
        	<k-field-text v-model="formData.nnCountry"/>
     	</k-form-item>
		<k-form-item label="名称">
        	<k-field-text v-model="formData.nnName"/>
     	</k-form-item>
		<k-form-item label="期限">
        	<k-field-text v-model="formData.nnTermDays"/>
     	</k-form-item>
		<k-form-item label="资产价值">
        	<k-field-text v-model="formData.nnAssetValue"/>
     	</k-form-item>
		<k-form-item label="资产收益率%">
        	<k-field-text v-model="formData.nnAssetReturn"/>
     	</k-form-item>
		<k-form-item label="所属国家或地区">
        	<k-field-text v-model="formData.ooCountry"/>
     	</k-form-item>
		<k-form-item label="名称">
        	<k-field-text v-model="formData.ooName"/>
     	</k-form-item>
		<k-form-item label="起息日">
        	<k-field-text v-model="formData.ooValueDate"/>
     	</k-form-item>
		<k-form-item label="到期日">
        	<k-field-text v-model="formData.ooMaturityDate"/>
     	</k-form-item>
		<k-form-item label="资产价值">
        	<k-field-text v-model="formData.ooAssetValue"/>
     	</k-form-item>
		<k-form-item label="资产收益率%">
        	<k-field-text v-model="formData.ooAssetReturn"/>
     	</k-form-item>
		<k-form-item label="基金代码">
        	<k-field-text v-model="formData.ppFundCode"/>
     	</k-form-item>
		<k-form-item label="基金名称">
        	<k-field-text v-model="formData.ppFundName"/>
     	</k-form-item>
		<k-form-item label="是否由金融资产投资公司发行">
        	<k-field-text v-model="formData.ppIssuedAssetCompany"/>
     	</k-form-item>
		<k-form-item label="行业">
        	<k-field-text v-model="formData.ppIndustry"/>
     	</k-form-item>
		<k-form-item label="登记备案机构">
        	<k-field-text v-model="formData.ppRegistAgency"/>
     	</k-form-item>
		<k-form-item label="是否属于政府投资基金">
        	<k-field-text v-model="formData.ppGovernInvestFund"/>
     	</k-form-item>
		<k-form-item label="政府投资基金投向">
        	<k-field-text v-model="formData.ppDirectGovernFund"/>
     	</k-form-item>
		<k-form-item label="基金公司名称">
        	<k-field-text v-model="formData.ppTaName"/>
     	</k-form-item>
		<k-form-item label="基金管理机构名称">
        	<k-field-text v-model="formData.ppManagerFundName"/>
     	</k-form-item>
		<k-form-item label="基金托管机构名称">
        	<k-field-text v-model="formData.ppCustodianFundName"/>
     	</k-form-item>
		<k-form-item label="投资阶段">
        	<k-field-text v-model="formData.ppInvestStage"/>
     	</k-form-item>
		<k-form-item label="投资企业类型">
        	<k-field-text v-model="formData.ppEnterTypeScale"/>
     	</k-form-item>
		<k-form-item label="投资企业类型">
        	<k-field-text v-model="formData.ppEnterTypeTech"/>
     	</k-form-item>
		<k-form-item label="投资企业类型">
        	<k-field-text v-model="formData.ppEnterTypeEconomic"/>
     	</k-form-item>
		<k-form-item label="基金投资资产">
        	<k-field-text v-model="formData.ppInvestAssets"/>
     	</k-form-item>
		<k-form-item label="委外投资协议名称">
        	<k-field-text v-model="formData.qqOutAgreementName"/>
     	</k-form-item>
		<k-form-item label="委外投资协议编号">
        	<k-field-text v-model="formData.qqOutAgreementCode"/>
     	</k-form-item>
		<k-form-item label="受托人">
        	<k-field-text v-model="formData.qqTrustee"/>
     	</k-form-item>
		<k-form-item label="实际管理人">
        	<k-field-text v-model="formData.qqActualManager"/>
     	</k-form-item>
		<k-form-item label="托管人">
        	<k-field-text v-model="formData.qqCustodian"/>
     	</k-form-item>
		<k-form-item label="委托投资金额">
        	<k-field-text v-model="formData.qqOutAmt"/>
     	</k-form-item>
		<k-form-item label="资金实际投向">
        	<k-field-text v-model="formData.qqActualDirection"/>
     	</k-form-item>
		<k-form-item label="资金运用方式">
        	<k-field-text v-model="formData.qqDetailsInvest"/>
     	</k-form-item>
		<k-form-item label="资金运用行业">
        	<k-field-text v-model="formData.qqIndustryInvest"/>
     	</k-form-item>
		<k-form-item label="投资运作起始日期">
        	<k-field-text v-model="formData.qqValueDate"/>
     	</k-form-item>
		<k-form-item label="投资运作终止日期">
        	<k-field-text v-model="formData.qqMaturityDate"/>
     	</k-form-item>
		<k-form-item label="委外投资属性">
        	<k-field-text v-model="formData.qqOutType"/>
     	</k-form-item>
		<k-form-item label="是否有预期收益率">
        	<k-field-text v-model="formData.qqExpectedReturn"/>
     	</k-form-item>
		<k-form-item label="预期最高收益率%">
        	<k-field-text v-model="formData.qqMaxExpectedReturn"/>
     	</k-form-item>
		<k-form-item label="预期最低收益率%">
        	<k-field-text v-model="formData.qqMinExpectedReturn"/>
     	</k-form-item>
		<k-form-item label="管理费率%">
        	<k-field-text v-model="formData.qqManagerFeeRate"/>
     	</k-form-item>
		<k-form-item label="托管费率%">
        	<k-field-text v-model="formData.qqCustodianFeeRate"/>
     	</k-form-item>
		<k-form-item label="交易相关合计费率%">
        	<k-field-text v-model="formData.qqTransCostRate"/>
     	</k-form-item>
		<k-form-item label="中介服务机构合计费率%">
        	<k-field-text v-model="formData.qqInterFeeRate"/>
     	</k-form-item>
		<k-form-item label="其他合计费率%">
        	<k-field-text v-model="formData.qqOtherExpensesRate"/>
     	</k-form-item>
		<k-form-item label="所属国家或地区">
        	<k-field-text v-model="formData.rrCountry"/>
     	</k-form-item>
		<k-form-item label="名称">
        	<k-field-text v-model="formData.rrName"/>
     	</k-form-item>
		<k-form-item label="期限">
        	<k-field-text v-model="formData.rrTermMaturity"/>
     	</k-form-item>
		<k-form-item label="负债规模">
        	<k-field-text v-model="formData.rrLiabilityAmt"/>
     	</k-form-item>
		<k-form-item label="利率%">
        	<k-field-text v-model="formData.rrInterestRate"/>
     	</k-form-item>
		<k-form-item label="名称">
        	<k-field-text v-model="formData.ssName"/>
     	</k-form-item>
		<k-form-item label="行内资产类别">
        	<k-field-text v-model="formData.ssAssetType"/>
     	</k-form-item>
		<k-form-item label="行内资产类别说明">
        	<k-field-text v-model="formData.ssDetailsAssetType"/>
     	</k-form-item>
		<k-form-item label="金额">
        	<k-field-text v-model="formData.ssAmt"/>
     	</k-form-item>
		<k-form-item label="起息日">
        	<k-field-text v-model="formData.ssValueDate"/>
     	</k-form-item>
		<k-form-item label="到期日">
        	<k-field-text v-model="formData.ssMaturityDate"/>
     	</k-form-item>
		<k-form-item label="所属国家或地区">
        	<k-field-text v-model="formData.ssCountry"/>
     	</k-form-item>
		<k-form-item label="是否有预期收益率">
        	<k-field-text v-model="formData.ssExpectedReturn"/>
     	</k-form-item>
		<k-form-item label="项目预期收益率">
        	<k-field-text v-model="formData.ssAnnualReturn"/>
     	</k-form-item>
		<k-form-item label="付息频率">
        	<k-field-text v-model="formData.ssInterestFrequency"/>
     	</k-form-item>
		<k-form-item label="融资人">
        	<k-field-text v-model="formData.ssDebtor"/>
     	</k-form-item>
		<k-form-item label="融资人组织结构">
        	<k-field-text v-model="formData.ssOrganCode"/>
     	</k-form-item>
		<k-form-item label="外部评级机构名称及对融资人评级结果">
        	<k-field-text v-model="formData.ssRateAgencyIss"/>
     	</k-form-item>
		<k-form-item label="融资人类型">
        	<k-field-text v-model="formData.ssDebtorTypeScale"/>
     	</k-form-item>
		<k-form-item label="融资人类型">
        	<k-field-text v-model="formData.ssDebtorTypeTech"/>
     	</k-form-item>
		<k-form-item label="融资人类型">
        	<k-field-text v-model="formData.ssDebtorTypeEconomic"/>
     	</k-form-item>
		<k-form-item label="融资项目">
        	<k-field-text v-model="formData.ssProject"/>
     	</k-form-item>
		<k-form-item label="融资人所属行业">
        	<k-field-text v-model="formData.ssIndustryDebtor"/>
     	</k-form-item>
		<k-form-item label="融资项目所属行业">
        	<k-field-text v-model="formData.ssIndustryProject"/>
     	</k-form-item>
		<k-form-item label="项目是否属于重点监控行业和领域">
        	<k-field-text v-model="formData.ssMonitoryIndustry"/>
     	</k-form-item>
		<k-form-item label="重点监控行业和领域类别">
        	<k-field-text v-model="formData.ssMonitoryIndustryType"/>
     	</k-form-item>
		<k-form-item label="重点监控行业和领域类别说明">
        	<k-field-text v-model="formData.ssDetailsMonitoryType"/>
     	</k-form-item>
		<k-form-item label="对应资产外部评级">
        	<k-field-text v-model="formData.ssInternalAssetRate"/>
     	</k-form-item>
		<k-form-item label="担保方式">
        	<k-field-text v-model="formData.ssGuaranteeMethod"/>
     	</k-form-item>
		<k-form-item label="担保情况说明">
        	<k-field-text v-model="formData.ssDetailsGuarantee"/>
     	</k-form-item>
		<k-form-item label="抵质押物类型">
        	<k-field-text v-model="formData.ssPledgeType"/>
     	</k-form-item>
		<k-form-item label="抵质押物价值">
        	<k-field-text v-model="formData.ssPledgeValue"/>
     	</k-form-item>
		<k-form-item label="担保性质">
        	<k-field-text v-model="formData.ssGuaranteeType"/>
     	</k-form-item>
		<k-form-item label="担保人与融资人关系">
        	<k-field-text v-model="formData.ssGuarantorType"/>
     	</k-form-item>
		<k-form-item label="是否为债转股">
        	<k-field-text v-model="formData.ssDebtEquitySwap"/>
     	</k-form-item>
		<k-form-item label="id">
        	<k-field-text v-model="formData.id"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AssetDebtRegisterInfoh.updateAssetDebtRegisterInfoh" data-from="editAssetDebtRegisterInfohForm"
	        :data-model="formData" data-target="assetDebtRegisterInfohGrid">
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
    name: "AssetDebtRegisterInfoh",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{},
         BreathDay:[]
      };
    },

    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
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
