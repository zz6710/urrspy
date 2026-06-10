<template>
  <div>
    <div>
      <k-form-search-customize data-model-name="AssetDebtRegisterInfoErr" data-target="assetDebtRegisterInfoErrGrid" v-model="searchParam">
        <k-form-item label="导入日期">
          <k-field-text v-model="searchParam.impDate"/>
        </k-form-item>
        <k-form-item label="业务登记日期">
          <k-field-date v-model="RegisterDate" data-type="daterange" data-date-format="yyyyMMdd"  data-value-format="yyyyMMdd"/>
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div>
      <k-grid ref="assetDebtRegisterInfoErrGrid" @data-row-select="selectRow"  data-operate-column="false" data-action="AssetDebtRegisterInfoErr.findAssetDebtRegisterInfoErrs" >
		<k-grid-column data-header="登记流水号" data-name="registerSerno"></k-grid-column>
		<k-grid-column data-header="id" data-name="id" data-hidden="true"></k-grid-column>
		<k-grid-column data-header="导入日期" data-name="impDate"></k-grid-column>
		<k-grid-column data-header="投资者登记日期" data-name="registerDate"></k-grid-column>
		<k-grid-column data-header="报送状态" data-name="registerStatus"></k-grid-column>
		<k-grid-column data-header="发行机构代码" data-name="bankCode"></k-grid-column>
		<k-grid-column data-header="行内资产/负债编码" data-name="assetCode"></k-grid-column>
		<k-grid-column data-header="资产/负债类别" data-name="assDebtType"></k-grid-column>
		<k-grid-column data-header="币种" data-name="cur"></k-grid-column>
		<k-grid-column data-header="交易流通场所" data-name="tradeVenue"></k-grid-column>
		<k-grid-column data-header="备注" data-name="details"></k-grid-column>
		<k-grid-column data-header="资金存入银行" data-name="bbDepositBank"></k-grid-column>
		<k-grid-column data-header="存款金额" data-name="bbDepositAmt"></k-grid-column>
		<k-grid-column data-header="起息日" data-name="bbValueDate"></k-grid-column>
		<k-grid-column data-header="到期日" data-name="bbMaturityDate"></k-grid-column>
		<k-grid-column data-header="年利率%" data-name="bbAnnualRate"></k-grid-column>
		<k-grid-column data-header="计息基础" data-name="bbInterestBasis"></k-grid-column>
		<k-grid-column data-header="存款类型" data-name="bbDepositType"></k-grid-column>
		<k-grid-column data-header="结构性存款标的类别" data-name="bbStructDepositType"></k-grid-column>
		<k-grid-column data-header="结构性存款挂钩标的" data-name="bbStructDeposit"></k-grid-column>
		<k-grid-column data-header="代码" data-name="ccIdentCode"></k-grid-column>
		<k-grid-column data-header="名称" data-name="ccName"></k-grid-column>
		<k-grid-column data-header="具体类别" data-name="ccSpecificBondType"></k-grid-column>
		<k-grid-column data-header="发行方式" data-name="ccIssModeBond"></k-grid-column>
		<k-grid-column data-header="主体评级" data-name="ccIssRatePart"></k-grid-column>
		<k-grid-column data-header="发行机构类型" data-name="ccInstituteTypeScale"></k-grid-column>
		<k-grid-column data-header="发行机构类型" data-name="ccInstituteTypeTech"></k-grid-column>
		<k-grid-column data-header="发行机构类型" data-name="ccInstituteTypeEconomic"></k-grid-column>
		<k-grid-column data-header="发行机构所属行业" data-name="ccIndustryIssuer"></k-grid-column>
		<k-grid-column data-header="登记托管机构" data-name="ccRegistDeposit"></k-grid-column>
		<k-grid-column data-header="登记托管机构说明" data-name="ccDetailsRegistDeposit"></k-grid-column>
		<k-grid-column data-header="起息日" data-name="ddValueDate"></k-grid-column>
		<k-grid-column data-header="到期日" data-name="ddMaturityDate"></k-grid-column>
		<k-grid-column data-header="对手方" data-name="ddCounterparty"></k-grid-column>
		<k-grid-column data-header="对手方类型" data-name="ddCounterpartyType"></k-grid-column>
		<k-grid-column data-header="年利率%" data-name="ddAnnalInterestRate"></k-grid-column>
		<k-grid-column data-header="计息基础" data-name="ddInterestBasis"></k-grid-column>
		<k-grid-column data-header="回购标的类别" data-name="ddCollateralType"></k-grid-column>
		<k-grid-column data-header="回购标的金额" data-name="ddCollateralValue"></k-grid-column>
		<k-grid-column data-header="名称" data-name="eeName"></k-grid-column>
		<k-grid-column data-header="金额" data-name="eeAmt"></k-grid-column>
		<k-grid-column data-header="份额面值" data-name="eeUnitParValue"></k-grid-column>
		<k-grid-column data-header="收/受权益类型" data-name="eeOwnershipType"></k-grid-column>
		<k-grid-column data-header="是否属于买入反售" data-name="eeBuyback"></k-grid-column>
		<k-grid-column data-header="起息日" data-name="eeValueDate"></k-grid-column>
		<k-grid-column data-header="到期日" data-name="eeMaturityDate"></k-grid-column>
		<k-grid-column data-header="法定到期日" data-name="eeStatutoryMaturityDate"></k-grid-column>
		<k-grid-column data-header="是否有预期收益率" data-name="eeExpectedReturn"></k-grid-column>
		<k-grid-column data-header="项目收益率" data-name="eeProjectAnnaulReturn"></k-grid-column>
		<k-grid-column data-header="计息类型" data-name="eeCouponType"></k-grid-column>
		<k-grid-column data-header="规则付息标识" data-name="eeRegualrInterestPay"></k-grid-column>
		<k-grid-column data-header="付息频率" data-name="eeInterestPayFrequency"></k-grid-column>
		<k-grid-column data-header="利息分布方式" data-name="eeCouponAllocationType"></k-grid-column>
		<k-grid-column data-header="还本付息情况说明" data-name="eeDetailPrincInterest"></k-grid-column>
		<k-grid-column data-header="计息基础" data-name="eeInterestBasis"></k-grid-column>
		<k-grid-column data-header="基准利率种类" data-name="eeBenchRateType"></k-grid-column>
		<k-grid-column data-header="是否有浮动因子" data-name="eeFloatFactor"></k-grid-column>
		<k-grid-column data-header="浮动因子%" data-name="eeFloatRate"></k-grid-column>
		<k-grid-column data-header="利差" data-name="eeYieldSpreadBp"></k-grid-column>
		<k-grid-column data-header="结构档次" data-name="eeStructGrade"></k-grid-column>
		<k-grid-column data-header="还本方式" data-name="eePrincPaymentType"></k-grid-column>
		<k-grid-column data-header="分期还本条款标识" data-name="eeInstallRepayType"></k-grid-column>
		<k-grid-column data-header="基础资产类型" data-name="eeBaseAssetType"></k-grid-column>
		<k-grid-column data-header="超额收益分配比例%" data-name="eePercentExcInAllot"></k-grid-column>
		<k-grid-column data-header="融资人" data-name="eeDebtor"></k-grid-column>
		<k-grid-column data-header="融资人内部信用评级" data-name="eeDeptorRate"></k-grid-column>
		<k-grid-column data-header="外部评级机构名称及对融资人评级结果" data-name="eeRateAgencyIss"></k-grid-column>
		<k-grid-column data-header="融资人类型" data-name="eeDebtorTypeScale"></k-grid-column>
		<k-grid-column data-header="融资人类型" data-name="eeDebtorTypeTech"></k-grid-column>
		<k-grid-column data-header="融资人类型" data-name="eeDebtorTypeEconomic"></k-grid-column>
		<k-grid-column data-header="融资项目" data-name="eeProject"></k-grid-column>
		<k-grid-column data-header="融资人所属行业" data-name="eeIndustryDebtor"></k-grid-column>
		<k-grid-column data-header="融资项目所属行业" data-name="eeIndustryProject"></k-grid-column>
		<k-grid-column data-header="项目是否属于重点监控行业和领域" data-name="eeMonitorIndusType"></k-grid-column>
		<k-grid-column data-header="重点监控行业和领域类别" data-name="eeMonitorIndustryType"></k-grid-column>
		<k-grid-column data-header="重点监控行业和领域类别说明" data-name="eeDetailsMonitoryType"></k-grid-column>
		<k-grid-column data-header="担保方式" data-name="eeGuaranteeMethod"></k-grid-column>
		<k-grid-column data-header="担保情况说明" data-name="eeDetailGuaranteeStatus"></k-grid-column>
		<k-grid-column data-header="抵质押物类型" data-name="eePledgeType"></k-grid-column>
		<k-grid-column data-header="抵质押物价值" data-name="eePledgeValue"></k-grid-column>
		<k-grid-column data-header="担保性质" data-name="eeGuaranteeType"></k-grid-column>
		<k-grid-column data-header="担保人与融资人关系" data-name="eeGuarantorType"></k-grid-column>
		<k-grid-column data-header="融资人主体评级" data-name="eeDebtorRate"></k-grid-column>
		<k-grid-column data-header="资产内部评级" data-name="eeInterAssetRate"></k-grid-column>
		<k-grid-column data-header="资产外部评级" data-name="eeOutAssetRate"></k-grid-column>
		<k-grid-column data-header="含权类型" data-name="eeOptionType"></k-grid-column>
		<k-grid-column data-header="行权方式" data-name="eeExerciseDateType"></k-grid-column>
		<k-grid-column data-header="固定行权日" data-name="eeFixedExerciseDate"></k-grid-column>
		<k-grid-column data-header="首次行权日期" data-name="eeFirstExerciseDate"></k-grid-column>
		<k-grid-column data-header="行权周期" data-name="eeExercisePeriod"></k-grid-column>
		<k-grid-column data-header="行权价格" data-name="eeExercisePrice"></k-grid-column>
		<k-grid-column data-header="永续条款类型" data-name="eePerpetualType"></k-grid-column>
		<k-grid-column data-header="利息递延条款类型" data-name="eeDeferreInterestType"></k-grid-column>
		<k-grid-column data-header="递延利息是否计息" data-name="eeInterestDeferred"></k-grid-column>
		<k-grid-column data-header="首次重定价日期" data-name="eeFirstRepriceDate"></k-grid-column>
		<k-grid-column data-header="重定价周期" data-name="eeRepricePeriod"></k-grid-column>
		<k-grid-column data-header="部分赎回标识" data-name="eePartialRedemption"></k-grid-column>
		<k-grid-column data-header="部分赎回比例%" data-name="eePartialRedemptionRate"></k-grid-column>
		<k-grid-column data-header="选择权" data-name="eeOptionRight"></k-grid-column>
		<k-grid-column data-header="行权条件说明" data-name="eeDetailsExerciseTerm"></k-grid-column>
		<k-grid-column data-header="融资人所属地区" data-name="eeRegionDebtor"></k-grid-column>
		<k-grid-column data-header="增信机构代码" data-name="eeEnhanceInstituteCode"></k-grid-column>
		<k-grid-column data-header="增信机构名称" data-name="eeEnhanceInstituteName"></k-grid-column>
		<k-grid-column data-header="融资总费率" data-name="eeTotalFeeRate"></k-grid-column>
		<k-grid-column data-header="融资人组织机构" data-name="eeOrganizationCode"></k-grid-column>
		<k-grid-column data-header="是否收/受益权" data-name="ffOwnership"></k-grid-column>
		<k-grid-column data-header="是否买入反售" data-name="ffBuyback"></k-grid-column>
		<k-grid-column data-header="类型" data-name="ffType"></k-grid-column>
		<k-grid-column data-header="数量" data-name="ffQuantity"></k-grid-column>
		<k-grid-column data-header="合计金额" data-name="ffAggregateAmt"></k-grid-column>
		<k-grid-column data-header="加权剩余期限" data-name="ffWeightRemainDay"></k-grid-column>
		<k-grid-column data-header="最长剩余期限" data-name="ffMaxRemainDay"></k-grid-column>
		<k-grid-column data-header="最短剩余期限" data-name="ffMinRemainFay"></k-grid-column>
		<k-grid-column data-header="到期日" data-name="ffMaturityDate"></k-grid-column>
		<k-grid-column data-header="起息日" data-name="ffValueDate"></k-grid-column>
		<k-grid-column data-header="贴现利率%" data-name="ffDiscountRate"></k-grid-column>
		<k-grid-column data-header="行业" data-name="ffIndustry"></k-grid-column>
		<k-grid-column data-header="股票代码" data-name="ggStockCode"></k-grid-column>
		<k-grid-column data-header="股票/企业名称" data-name="ggName"></k-grid-column>
		<k-grid-column data-header="股票类型" data-name="ggStockType"></k-grid-column>
		<k-grid-column data-header="行业" data-name="ggIndustry"></k-grid-column>
		<k-grid-column data-header="投资阶段" data-name="ggInvestStage"></k-grid-column>
		<k-grid-column data-header="股权退出安排" data-name="ggEquityOutDate"></k-grid-column>
		<k-grid-column data-header="企业类型" data-name="ggEnterTypeScale"></k-grid-column>
		<k-grid-column data-header="企业类型" data-name="ggEnterTypeTech"></k-grid-column>
		<k-grid-column data-header="企业类型" data-name="ggEnterTypeEconomic"></k-grid-column>
		<k-grid-column data-header="是否质押融资" data-name="ggPledgedFinace"></k-grid-column>
		<k-grid-column data-header="是否为债转股" data-name="ggDebtEquitySwap"></k-grid-column>
		<k-grid-column data-header="名称" data-name="hhName"></k-grid-column>
		<k-grid-column data-header="名义本金" data-name="hhNominalPrincipal"></k-grid-column>
		<k-grid-column data-header="标的类别" data-name="hhUnderAssetType"></k-grid-column>
		<k-grid-column data-header="持有目的" data-name="hhHoldObjective"></k-grid-column>
		<k-grid-column data-header="所属国家或地区" data-name="iiCountyRegion"></k-grid-column>
		<k-grid-column data-header="债券名称" data-name="iiBondName"></k-grid-column>
		<k-grid-column data-header="债券代码" data-name="iiBondIdentCode"></k-grid-column>
		<k-grid-column data-header="发行机构" data-name="iiIssuer"></k-grid-column>
		<k-grid-column data-header="发行机构所属行业" data-name="iiIndustryIssuer"></k-grid-column>
		<k-grid-column data-header="起息日" data-name="iiValueDate"></k-grid-column>
		<k-grid-column data-header="到期日" data-name="iiMaturityDate"></k-grid-column>
		<k-grid-column data-header="期限" data-name="iiTermMaturity"></k-grid-column>
		<k-grid-column data-header="发行机构主体信用评级" data-name="iiIssuerRateBond"></k-grid-column>
		<k-grid-column data-header="债券信用评级" data-name="iiBondRate"></k-grid-column>
		<k-grid-column data-header="票面利率%" data-name="iiCoupRate"></k-grid-column>
		<k-grid-column data-header="付息频率" data-name="iiInterestPayQuency"></k-grid-column>
		<k-grid-column data-header="担保情况说明" data-name="iiDetailsAssureStatus"></k-grid-column>
		<k-grid-column data-header="是否含权等特殊条款情况说明" data-name="iiDetailsSpecialTerms"></k-grid-column>
		<k-grid-column data-header="所属国家或地区" data-name="jjCountry"></k-grid-column>
		<k-grid-column data-header="起息日" data-name="jjValueDate"></k-grid-column>
		<k-grid-column data-header="到期日" data-name="jjMaturityDate"></k-grid-column>
		<k-grid-column data-header="对手方" data-name="jjCounterparty"></k-grid-column>
		<k-grid-column data-header="年利率%" data-name="jjInterestRate"></k-grid-column>
		<k-grid-column data-header="计息基础" data-name="jjInterestBasis"></k-grid-column>
		<k-grid-column data-header="所属国家或地区" data-name="kkCountry"></k-grid-column>
		<k-grid-column data-header="股票/基金代码" data-name="kkIdentCode"></k-grid-column>
		<k-grid-column data-header="股票/基金名称" data-name="kkName"></k-grid-column>
		<k-grid-column data-header="发行机构" data-name="kkIssuer"></k-grid-column>
		<k-grid-column data-header="行业" data-name="kkIndustry"></k-grid-column>
		<k-grid-column data-header="所属国家或地区" data-name="llCountry"></k-grid-column>
		<k-grid-column data-header="合约名称" data-name="llContractName"></k-grid-column>
		<k-grid-column data-header="起息日" data-name="llValueDate"></k-grid-column>
		<k-grid-column data-header="到期日" data-name="llMaturityDate"></k-grid-column>
		<k-grid-column data-header="票面利率%" data-name="llCouponRate"></k-grid-column>
		<k-grid-column data-header="付息频率" data-name="llInterestFrequency"></k-grid-column>
		<k-grid-column data-header="固定收益部分所占比例%" data-name="llPercentFix"></k-grid-column>
		<k-grid-column data-header="衍生金融工具所占比例%" data-name="llPercentDerivate"></k-grid-column>
		<k-grid-column data-header="衍生金融工具具体投资方式" data-name="llDerivateInvetType"></k-grid-column>
		<k-grid-column data-header="衍生金融工具挂钩标的资产" data-name="llUnderAsset"></k-grid-column>
		<k-grid-column data-header="利息结算方式" data-name="llDetailsProceeds"></k-grid-column>
		<k-grid-column data-header="含权情况说明" data-name="llDetailsOption"></k-grid-column>
		<k-grid-column data-header="结构性票据最高收益率%" data-name="llMaxNoteReturn"></k-grid-column>
		<k-grid-column data-header="机构性票据最低收益率%" data-name="llMinNoteReturn"></k-grid-column>
		<k-grid-column data-header="挂钩标的资产基准价格" data-name="llStrikeUnderAsset"></k-grid-column>
		<k-grid-column data-header="挂钩标的资产登记日价格" data-name="llUnderRgPrice"></k-grid-column>
		<k-grid-column data-header="交易费" data-name="llTransCosts"></k-grid-column>
		<k-grid-column data-header="资管计划名称" data-name="mmManagePlanName"></k-grid-column>
		<k-grid-column data-header="是否由金融资产投资公司发行" data-name="mmIssuedAssetCompany"></k-grid-column>
		<k-grid-column data-header="资管计划发起人机构编码" data-name="mmPlanIssuerCode"></k-grid-column>
		<k-grid-column data-header="资管计划登记编码" data-name="mmAssetPlanRgCode"></k-grid-column>
		<k-grid-column data-header="管理人" data-name="mmManager"></k-grid-column>
		<k-grid-column data-header="托管人" data-name="mmCustodian"></k-grid-column>
		<k-grid-column data-header="金额" data-name="mmAmt"></k-grid-column>
		<k-grid-column data-header="资金实际投向" data-name="mmActualDirect"></k-grid-column>
		<k-grid-column data-header="资金运用方式" data-name="mmDetailsInvest"></k-grid-column>
		<k-grid-column data-header="资金运用行业" data-name="mmIndustryInvest"></k-grid-column>
		<k-grid-column data-header="资管计划成立日期" data-name="mmPlanStartDate"></k-grid-column>
		<k-grid-column data-header="资管计划终止日期" data-name="mmPlanMaturityDate"></k-grid-column>
		<k-grid-column data-header="资管计划属性" data-name="mmPlanType"></k-grid-column>
		<k-grid-column data-header="是否有预期收益率" data-name="mmExpectedReturn"></k-grid-column>
		<k-grid-column data-header="预期最高收益率%" data-name="mmMaxExpectedReturn"></k-grid-column>
		<k-grid-column data-header="预期最低收益率%" data-name="mmMinExpectedReturn"></k-grid-column>
		<k-grid-column data-header="购买结构" data-name="mmInvestStructure"></k-grid-column>
		<k-grid-column data-header="管理方式" data-name="mmManagerType"></k-grid-column>
		<k-grid-column data-header="管理费率%" data-name="mmManagerFeeRate"></k-grid-column>
		<k-grid-column data-header="托管费率%" data-name="mmCustodianFeeRate"></k-grid-column>
		<k-grid-column data-header="交易相关合计费率%" data-name="mmTransCostRate"></k-grid-column>
		<k-grid-column data-header="中介服务机构合计费率%" data-name="mmInterFeeRate"></k-grid-column>
		<k-grid-column data-header="其他合计费率%" data-name="mmOtherExpenseRate"></k-grid-column>
		<k-grid-column data-header="所属国家或地区" data-name="nnCountry"></k-grid-column>
		<k-grid-column data-header="名称" data-name="nnName"></k-grid-column>
		<k-grid-column data-header="期限" data-name="nnTermDays"></k-grid-column>
		<k-grid-column data-header="资产价值" data-name="nnAssetValue"></k-grid-column>
		<k-grid-column data-header="资产收益率%" data-name="nnAssetReturn"></k-grid-column>
		<k-grid-column data-header="所属国家或地区" data-name="ooCountry"></k-grid-column>
		<k-grid-column data-header="名称" data-name="ooName"></k-grid-column>
		<k-grid-column data-header="起息日" data-name="ooValueDate"></k-grid-column>
		<k-grid-column data-header="到期日" data-name="ooMaturityDate"></k-grid-column>
		<k-grid-column data-header="资产价值" data-name="ooAssetValue"></k-grid-column>
		<k-grid-column data-header="资产收益率%" data-name="ooAssetReturn"></k-grid-column>
		<k-grid-column data-header="基金代码" data-name="ppFundCode"></k-grid-column>
		<k-grid-column data-header="基金名称" data-name="ppFundName"></k-grid-column>
		<k-grid-column data-header="是否由金融资产投资公司发行" data-name="ppIssuedAssetCompany"></k-grid-column>
		<k-grid-column data-header="行业" data-name="ppIndustry"></k-grid-column>
		<k-grid-column data-header="登记备案机构" data-name="ppRegistAgency"></k-grid-column>
		<k-grid-column data-header="是否属于政府投资基金" data-name="ppGovernInvestFund"></k-grid-column>
		<k-grid-column data-header="政府投资基金投向" data-name="ppDirectGovernFund"></k-grid-column>
		<k-grid-column data-header="基金公司名称" data-name="ppTaName"></k-grid-column>
		<k-grid-column data-header="基金管理机构名称" data-name="ppManagerFundName"></k-grid-column>
		<k-grid-column data-header="基金托管机构名称" data-name="ppCustodianFundName"></k-grid-column>
		<k-grid-column data-header="投资阶段" data-name="ppInvestStage"></k-grid-column>
		<k-grid-column data-header="投资企业类型" data-name="ppEnterTypeScale"></k-grid-column>
		<k-grid-column data-header="投资企业类型" data-name="ppEnterTypeTech"></k-grid-column>
		<k-grid-column data-header="投资企业类型" data-name="ppEnterTypeEconomic"></k-grid-column>
		<k-grid-column data-header="基金投资资产" data-name="ppInvestAssets"></k-grid-column>
		<k-grid-column data-header="委外投资协议名称" data-name="qqOutAgreementName"></k-grid-column>
		<k-grid-column data-header="委外投资协议编号" data-name="qqOutAgreementCode"></k-grid-column>
		<k-grid-column data-header="受托人" data-name="qqTrustee"></k-grid-column>
		<k-grid-column data-header="实际管理人" data-name="qqActualManager"></k-grid-column>
		<k-grid-column data-header="托管人" data-name="qqCustodian"></k-grid-column>
		<k-grid-column data-header="委托投资金额" data-name="qqOutAmt"></k-grid-column>
		<k-grid-column data-header="资金实际投向" data-name="qqActualDirection"></k-grid-column>
		<k-grid-column data-header="资金运用方式" data-name="qqDetailsInvest"></k-grid-column>
		<k-grid-column data-header="资金运用行业" data-name="qqIndustryInvest"></k-grid-column>
		<k-grid-column data-header="投资运作起始日期" data-name="qqValueDate"></k-grid-column>
		<k-grid-column data-header="投资运作终止日期" data-name="qqMaturityDate"></k-grid-column>
		<k-grid-column data-header="委外投资属性" data-name="qqOutType"></k-grid-column>
		<k-grid-column data-header="是否有预期收益率" data-name="qqExpectedReturn"></k-grid-column>
		<k-grid-column data-header="预期最高收益率%" data-name="qqMaxExpectedReturn"></k-grid-column>
		<k-grid-column data-header="预期最低收益率%" data-name="qqMinExpectedReturn"></k-grid-column>
		<k-grid-column data-header="管理费率%" data-name="qqManagerFeeRate"></k-grid-column>
		<k-grid-column data-header="托管费率%" data-name="qqCustodianFeeRate"></k-grid-column>
		<k-grid-column data-header="交易相关合计费率%" data-name="qqTransCostRate"></k-grid-column>
		<k-grid-column data-header="中介服务机构合计费率%" data-name="qqInterFeeRate"></k-grid-column>
		<k-grid-column data-header="其他合计费率%" data-name="qqOtherExpensesRate"></k-grid-column>
		<k-grid-column data-header="所属国家或地区" data-name="rrCountry"></k-grid-column>
		<k-grid-column data-header="名称" data-name="rrName"></k-grid-column>
		<k-grid-column data-header="期限" data-name="rrTermMaturity"></k-grid-column>
		<k-grid-column data-header="负债规模" data-name="rrLiabilityAmt"></k-grid-column>
		<k-grid-column data-header="利率%" data-name="rrInterestRate"></k-grid-column>
		<k-grid-column data-header="名称" data-name="ssName"></k-grid-column>
		<k-grid-column data-header="行内资产类别" data-name="ssAssetType"></k-grid-column>
		<k-grid-column data-header="行内资产类别说明" data-name="ssDetailsAssetType"></k-grid-column>
		<k-grid-column data-header="金额" data-name="ssAmt"></k-grid-column>
		<k-grid-column data-header="起息日" data-name="ssValueDate"></k-grid-column>
		<k-grid-column data-header="到期日" data-name="ssMaturityDate"></k-grid-column>
		<k-grid-column data-header="所属国家或地区" data-name="ssCountry"></k-grid-column>
		<k-grid-column data-header="是否有预期收益率" data-name="ssExpectedReturn"></k-grid-column>
		<k-grid-column data-header="项目预期收益率" data-name="ssAnnualReturn"></k-grid-column>
		<k-grid-column data-header="付息频率" data-name="ssInterestFrequency"></k-grid-column>
		<k-grid-column data-header="融资人" data-name="ssDebtor"></k-grid-column>
		<k-grid-column data-header="融资人组织结构" data-name="ssOrganCode"></k-grid-column>
		<k-grid-column data-header="外部评级机构名称及对融资人评级结果" data-name="ssRateAgencyIss"></k-grid-column>
		<k-grid-column data-header="融资人类型" data-name="ssDebtorTypeScale"></k-grid-column>
		<k-grid-column data-header="融资人类型" data-name="ssDebtorTypeTech"></k-grid-column>
		<k-grid-column data-header="融资人类型" data-name="ssDebtorTypeEconomic"></k-grid-column>
		<k-grid-column data-header="融资项目" data-name="ssProject"></k-grid-column>
		<k-grid-column data-header="融资人所属行业" data-name="ssIndustryDebtor"></k-grid-column>
		<k-grid-column data-header="融资项目所属行业" data-name="ssIndustryProject"></k-grid-column>
		<k-grid-column data-header="项目是否属于重点监控行业和领域" data-name="ssMonitoryIndustry"></k-grid-column>
		<k-grid-column data-header="重点监控行业和领域类别" data-name="ssMonitoryIndustryType"></k-grid-column>
		<k-grid-column data-header="重点监控行业和领域类别说明" data-name="ssDetailsMonitoryType"></k-grid-column>
		<k-grid-column data-header="对应资产外部评级" data-name="ssInternalAssetRate"></k-grid-column>
		<k-grid-column data-header="担保方式" data-name="ssGuaranteeMethod"></k-grid-column>
		<k-grid-column data-header="担保情况说明" data-name="ssDetailsGuarantee"></k-grid-column>
		<k-grid-column data-header="抵质押物类型" data-name="ssPledgeType"></k-grid-column>
		<k-grid-column data-header="抵质押物价值" data-name="ssPledgeValue"></k-grid-column>
		<k-grid-column data-header="担保性质" data-name="ssGuaranteeType"></k-grid-column>
		<k-grid-column data-header="担保人与融资人关系" data-name="ssGuarantorType"></k-grid-column>
		<k-grid-column data-header="是否为债转股" data-name="ssDebtEquitySwap"></k-grid-column>
		<k-grid-column data-header="id" data-name="id"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改资产负债登记错误信息" data-functype="POPUP" data-size="mini"
            data-target="editAssetDebtRegisterInfoErrPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="AssetDebtRegisterInfoErr.deleteAssetDebtRegisterInfoErr" data-size="mini"
               data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true" data-descript="删除资产负债登记错误信息">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加资产负债登记错误信息弹出框   -->
	<k-popup ref="addAssetDebtRegisterInfoErrPopup" data-title="新增">
    	<k-form ref="addAssetDebtRegisterInfoErrForm" :data-col="2">
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
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AssetDebtRegisterInfoErr.addAssetDebtRegisterInfoErr" data-from="addAssetDebtRegisterInfoErrForm"
		               :data-model="formData" data-target="assetDebtRegisterInfoErrGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改资产负债登记错误信息弹出框   -->
	<k-popup ref="editAssetDebtRegisterInfoErrPopup" data-title="修改">
	  <k-form ref="editAssetDebtRegisterInfoErrForm" :data-col="2">
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
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AssetDebtRegisterInfoErr.updateAssetDebtRegisterInfoErr" data-from="editAssetDebtRegisterInfoErrForm"
	        :data-model="formData" data-target="assetDebtRegisterInfoErrGrid">
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
    name: "AssetDebtRegisterInfoErr",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{},
		RegisterDate:[]
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
	  RegisterDate() {
		this.$set(this.searchParam, 'startDate', this.RegisterDate == null ? '' : this.RegisterDate[0]);
		this.$set(this.searchParam, 'endDate', this.RegisterDate == null ? '' : this.RegisterDate[1]);
	  },
	}
  };
</script>
