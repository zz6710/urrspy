<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-model-name="AssetDebtRegist" data-label-width="130px" data-target="AssetDebtRegistGrid" v-model = "searchParam">
        <k-form-item label="操作日期">
          <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMMdd"/>
        </k-form-item>
        <k-form-item label="操作人员">
          <k-field-text v-model="searchParam.summitUser"/>
        </k-form-item>
        <k-form-item label="操作类型">
          <k-field-select v-model="searchParam.opType"  data-dict="op_type"/>
        </k-form-item>
        <k-form-item label="行内资产/负债编码">
          <k-field-text v-model="searchParam.assetCode"/>
        </k-form-item>
        <k-form-item label="资产/负债类别">
          <k-field-select v-model="searchParam.assDebtType" data-dict="cbndScdCtg"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <k-grid ref="AssetDebtRegistGrid" @data-row-select="selectRow" data-operate-column="false" data-action="AssetDebtRegist.findAssetDebtRegists" >
		<k-grid-column data-align="left" data-header="操作人员" data-name="summitUser"  data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作日期" data-name="createDate" data-width="100"></k-grid-column>
        <k-grid-column data-align="left" data-header="操作时间" data-name="createTime" data-type="time"  data-width="100" ></k-grid-column>
        <k-grid-column data-align="left" data-header="操作类型" data-name="opType" data-dict="op_type"  data-width="100"></k-grid-column>
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
      </k-grid>
    </div>

  </div>
</template>

<script>
  export default {
    name: "assetDebtRegist",
    data() {
      return {
        formData: {
        },
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
        console.log(this.BreathDay);
        this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
        this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
      },
    }
  };
</script>
