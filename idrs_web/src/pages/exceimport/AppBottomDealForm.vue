<template>
  <k-form ref="appBottomDealForm" :data-col="2" isFormBodyScreen>
    <k-form-item label="ID" v-show="false">
      <k-field-text v-model="formData.id" :data-disabled="true"/>
    </k-form-item>
    <k-form-item label="券商机构类型">
      <k-field-select v-model="formData.securitiesfirmsType" data-action="ValReportTab.findValReportTabs"
                      data-display-field="reporttabName"  data-value-field="id" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="业务品种">
      <k-field-select v-model="formData.t8SysAdtypeId" data-dict="t8_bottomn_type"
                      @data-on-change="bottonTypeChange" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="资产代码">
      <k-field-text v-model="formData.ftoolCode" :data-max-length="120" :data-allowblank="false"/>
    </k-form-item>
    <k-form-item label="报送资产编码" v-show="dataType.param5">
      <k-field-text v-model="formData.reportAssetCode" :data-max-length="120"/>
    </k-form-item>


    <k-form-item label="交易流通场所">
      <k-field-select v-model="formData.tradePlaces" data-dict="tacdingPlace" />
    </k-form-item>
    <k-form-item label="交易对手方名称/融资人" v-show="dataType.param1 || dataType.param2 || dataType.param3 || dataType.param5">
      <k-field-text v-model="formData.osideNames"/>
    </k-form-item>
<!--
    <k-form-item label="交易对手方类型" v-show="dataType.param3>
      <k-field-text v-model="formData.orgType"/>
    </k-form-item>
    -->
    <k-form-item label="交易方向" v-show="dataType.param3">
      <k-field-select v-model="formData.direction" data-dict="t8_hg_direction"/>
    </k-form-item>
    <k-form-item label="存款方账号" v-show="dataType.param1">
      <k-field-text v-model="formData.accountCode" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="起息日" v-show="dataType.param1 || dataType.param2 || dataType.param3 || dataType.param5 || dataType.param6 || dataType.param10">
      <k-field-date v-model="formData.begindate" />
    </k-form-item>
    <k-form-item label="到期日"  v-show="dataType.param1 || dataType.param2 || dataType.param3 || dataType.param4 || dataType.param5 || dataType.param6 || dataType.param10">
      <k-field-date v-model="formData.enddate"  />
    </k-form-item>
    <k-form-item label="回购利率" v-show="dataType.param1 || dataType.param3 || dataType.param9 || dataType.param10 || dataType.param11">
      <k-field-text v-model="formData.yield" data-validate-type="number" data-digits="4"
                    name="yield" data-max-value="100" data-min-value="(0"/>
    </k-form-item>
    <k-form-item label="项目收益率(利率)%" v-show="dataType.param5">
      <k-field-text v-model="formData.projectYield" data-validate-type="number" data-digits="4"
                    name="yield" data-max-value="100" data-min-value="(0"/>
    </k-form-item>
    <k-form-item label="付息频率（个月/次）" v-show="dataType.param2 || dataType.param5">
      <k-field-text v-model="formData.payFreq" data-validate-type="number" data-max-value="100" data-min-value="(0"/>
    </k-form-item>
    <k-form-item label="计息基础" v-show="dataType.param1 || dataType.param2 || dataType.param3 || dataType.param5">
      <k-field-select v-model="formData.basedays" data-dict="intr_base"/>
    </k-form-item>
    <k-form-item label="投资基金类型" v-show="dataType.param4">
      <k-field-select v-model="formData.investFundType" data-dict="t8_invest_fundtype"/>
    </k-form-item>
    <k-form-item label="行业" v-show="dataType.param4 || dataType.param7">
      <k-field-select v-model="formData.industry" data-dict="isuOrgBlgIdt"/>
    </k-form-item>
    <k-form-item label="登记备案机构" v-show="dataType.param4">
      <k-field-select v-model="formData.depot" data-dict="regRcdOrg"/>
    </k-form-item>
    <k-form-item label="是否为固定收益类" v-show="dataType.param4">
      <k-field-select v-model="formData.investPropType" data-dict="1yes0no"/>
    </k-form-item>
    <k-form-item label="是否属于政府投资基金" v-show="dataType.param4">
      <k-field-select v-model="formData.isGovermentFundation" data-dict="1yes0no"/>
    </k-form-item>
    <k-form-item label="政府投资基金投向" v-show="dataType.param4">
      <k-field-select v-model="formData.govermentWays" data-dict="actual_invest_dir_gov_fund"/>
    </k-form-item>
    <k-form-item label="是否由金融资产投资公司发行" v-show="dataType.param4">
      <k-field-select v-model="formData.isJrcompanyFundation" data-dict="1yes0no"/>
    </k-form-item>
    <k-form-item label="是否由金融资产投资公司发行" v-show="dataType.param6">
      <k-field-select v-model="formData.isJrcompany" data-dict="1yes0no"/>
    </k-form-item>
    <k-form-item label="基金公司名称" v-show="dataType.param4">
      <k-field-text v-model="formData.funComName" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="基金管理机构名称" v-show="dataType.param4">
      <k-field-text v-model="formData.funManName" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="基金托管机构名称" v-show="dataType.param4">
      <k-field-text v-model="formData.funCusName" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="投资阶段" v-show="dataType.param4 || dataType.param7">
      <k-field-select v-model="formData.investmentStage" data-dict="invest_stage"/>
    </k-form-item>
    <k-form-item label="投资企业类型（按规模划分）" v-show="dataType.param4 || dataType.param7">
      <k-field-select v-model="formData.inButypeSca" data-dict="isuOrgTypSiz"/>
    </k-form-item>
    <k-form-item label="投资企业类型（按技术领域划分）" v-show="dataType.param4 || dataType.param7">
      <k-field-select v-model="formData.inButypeTec" data-dict="isuOrgTypTchno"/>
    </k-form-item>
    <k-form-item label="投资企业类型（按经济类型划分）" v-show="dataType.param4 || dataType.param7">
      <k-field-select v-model="formData.inButypeEco" data-dict="isuOrgTypEcn"/>
    </k-form-item>
    <k-form-item label="基金投资资产" v-show="dataType.param4">
      <k-field-text v-model="formData.funInvestAsset" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="资产类别" v-show="dataType.param2 || dataType.param5 || dataType.param12">
      <k-field-select v-model="formData.fbZcType"  data-dict="stru_deposit_type"/>
    </k-form-item>
    <k-form-item label="投资类型" v-show="dataType.param5 || dataType.param12">
      <k-field-select v-model="formData.investType" data-dict="t8_fb_invest_type"/>
    </k-form-item>
    <k-form-item label="中债一级分类" v-show="dataType.param4 || dataType.param5 || dataType.param6 || dataType.param8 || dataType.param9 || dataType.param10">
      <k-field-select v-model="formData.cbcType" data-dict="cbndFrsCtg"/>
    </k-form-item>
    <k-form-item label="中债二级分类" v-show="dataType.param4 || dataType.param5 || dataType.param6 || dataType.param8 || dataType.param9 || dataType.param10">
      <k-field-select v-model="formData.cbcSubtype" data-dict="cbndScdCtg"/>
    </k-form-item>
    <k-form-item label="是否标准化票据" v-show="dataType.param5 ">
      <k-field-select v-model="formData.isBzpj" data-dict="1yes0no"/>
    </k-form-item>
    <k-form-item label="是否投向地方政府融资平台" v-show="false">
      <k-field-select v-model="formData.isLgfplatform" data-dict="1yes0no"/>
    </k-form-item>
    <k-form-item label="行业归属" v-show="dataType.param5">
      <k-field-select v-model="formData.comm3IndCode" data-dict="isuOrgBlgIdt"/>
    </k-form-item>
    <k-form-item label="收/受益权类型" v-show="dataType.param5">
      <k-field-select v-model="formData.inBenType" data-dict="t8_in_ben_type"/>
    </k-form-item>
    <k-form-item label="是否属于买入返售" v-show="dataType.param5">
      <k-field-select v-model="formData.isBuyback" data-dict="1yes0no"/>
    </k-form-item>
    <k-form-item label="金额" v-show="dataType.param2 ||dataType.param5 || dataType.param6 || dataType.param8">
      <k-field-text v-model="formData.principal" data-max-value="999999999999" data-unit-value="10000"
                    :data-allowblank="!(dataType.param2 ||dataType.param5 || dataType.param6 || dataType.param8)" data-validate-type="money"
                    name="principal" :data-max-length="15" data-min-value="0" data-digits="2"/>
    </k-form-item>
    <k-form-item label="份额面值" v-show="dataType.param5">
      <k-field-text v-model="formData.volValue" data-max-value="999999999999" data-unit-value="10000"
                    :data-allowblank="!(dataType.param5)" data-validate-type="money" :data-max-length="15" data-min-value="0" data-digits="2"/>
    </k-form-item>
    <k-form-item label="法定到期日" v-show="dataType.param5">
      <k-field-date v-model="formData.legalMaturity"/>
    </k-form-item>
    <k-form-item label="计息类型" v-show="dataType.param5">
      <k-field-select v-model="formData.interestType" data-dict="interType"/>
    </k-form-item>
    <k-form-item label="规则付息标识" v-show="dataType.param5">
      <k-field-select v-model="formData.rulePayFlag" data-dict="isTrue"/>
    </k-form-item>
    <k-form-item label="利息分布方式" v-show="dataType.param5">
      <k-field-select v-model="formData.modeDistribute" data-dict="intrAlcMth"/>
    </k-form-item>
    <k-form-item label="还本付息情况说明" v-show="dataType.param5">
      <k-field-text v-model="formData.debtServiceDescription" :data-max-length="128"/>
    </k-form-item>
    <k-form-item label="基准利率种类" v-show="dataType.param5">
      <k-field-select v-model="formData.baseRateType" data-dict="bchmRatTyp"/>
    </k-form-item>
    <k-form-item label="是否有浮动因子" v-show="dataType.param5">
      <k-field-select v-model="formData.isFlow" data-dict="1yes0no" />
    </k-form-item>
    <k-form-item label="浮动因子（%）" v-show="formData.isFlow === '1'">
      <k-field-text v-model="formData.fdyz" data-validate-type="number" data-digits="4"
                    data-max-value="100" data-min-value="0"/>
    </k-form-item>
    <k-form-item label="利差(BP)" v-show="dataType.param5">
      <k-field-text v-model="formData.ratepare" data-validate-type="number" data-digits="4"
                    data-max-value="100" data-min-value="0"/>
    </k-form-item>
    <k-form-item label="结构档次" v-show="dataType.param5">
      <k-field-select v-model="formData.structureGrade" data-dict="strcGrd"/>
    </k-form-item>
    <k-form-item label="还本方式" v-show="dataType.param5">
      <k-field-select v-model="formData.debtWay" data-dict="payPrcpMth"/>
    </k-form-item>
    <k-form-item label="分期还本条款标识" v-show="dataType.param5">
      <k-field-select v-model="formData.installmentsMarked" data-dict="insPayPrcpF"/>
    </k-form-item>
    <k-form-item label="基础资产类型" v-show="dataType.param5">
      <k-field-text v-model="formData.baseAssetsType" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="超额收益分配比例（%）" v-show="dataType.param5">
      <k-field-text v-model="formData.excessIncomeRate"  data-max-value="100" data-min-value="0"
                    data-validate-type="number" data-digits="4"/>
    </k-form-item>
    <k-form-item label="融资人内部信用评级" v-show="dataType.param5">
      <k-field-select v-model="formData.scorePar1"  data-dict="mainRating"/>
    </k-form-item>
    <k-form-item label="外部评级机构名称及对融资人评级结果" v-show="dataType.param5">
      <k-field-text v-model="formData.outPar" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="融资人主体评级" v-show="dataType.param5">
      <k-field-select v-model="formData.scorePar2"  data-dict="mainRating"/>
    </k-form-item>
    <k-form-item label="资产内部评级" v-show="dataType.param5">
      <k-field-select v-model="formData.scorePar3" data-dict="mainRating"/>
    </k-form-item>
    <k-form-item label="资产外部评级" v-show="dataType.param5">
      <k-field-select v-model="formData.scorePar4" data-dict="mainRating"/>
    </k-form-item>
    <k-form-item label="融资人类型（按规模划分）" v-show="dataType.param5">
      <k-field-select v-model="formData.finaTypeScale" data-dict="isuOrgTypSiz"/>
    </k-form-item>
    <k-form-item label="融资人类型（按技术领域划分）" v-show="dataType.param5">
      <k-field-select v-model="formData.finaTypeTec"  data-dict="isuOrgTypTchno"/>
    </k-form-item>
    <k-form-item label="融资人类型（按经济类型划分）" v-show="dataType.param5">
      <k-field-select v-model="formData.finaTypeEco" data-dict="isuOrgTypEcn"/>
    </k-form-item>
    <k-form-item label="融资项目" v-show="dataType.param5">
      <k-field-text v-model="formData.remarkPar2" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="融资人所属行业" v-show="dataType.param5">
      <k-field-select v-model="formData.comm2IndCode" data-dict="isuOrgBlgIdt"/>
    </k-form-item>
    <k-form-item label="融资项目所属行业" v-show="dataType.param5">
      <k-field-select v-model="formData.commIndCode" data-dict="isuOrgBlgIdt"/>
    </k-form-item>
    <k-form-item label="项目是否属于重点监控行业和领域" v-show="dataType.param5">
      <k-field-select v-model="formData.isImport"  data-dict="1yes0no"/>
    </k-form-item>
    <k-form-item label="重点监控行业和领域类别" v-show="dataType.param5">
      <k-field-text v-model="formData.spIndCode" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="重点监控行业和领域类别说明" v-show="dataType.param5">
      <k-field-text v-model="formData.spIndDesc" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="担保方式" v-show="dataType.param5">
      <k-field-select v-model="formData.vouch" data-dict="eeGuaranteeMethod"/>
    </k-form-item>
    <k-form-item label="抵质押物类型" v-show="dataType.param5">
      <k-field-select v-model="formData.collateralType" data-dict="grntWay"/>
    </k-form-item>
    <k-form-item label="抵质押物价值（元）" v-show="dataType.param5">
      <k-field-text v-model="formData.collateralValue"  data-max-value="999999999999" data-unit-value="10000"
                    :data-allowblank="true" data-validate-type="money" :data-max-length="15"
                    data-min-value="0" data-digits="2"/>
    </k-form-item>
    <k-form-item label="担保性质" v-show="dataType.param5">
      <k-field-select v-model="formData.natureGuarantee" data-dict="plgTyp"/>
    </k-form-item>
    <k-form-item label="担保人与融资人关系" v-show="dataType.param5">
      <k-field-select v-model="formData.relationBtwGaf" data-dict="grntLvrgRel"/>
    </k-form-item>
    <k-form-item label="担保情况说明" v-show="dataType.param5">
      <k-field-text v-model="formData.vouchDesc"  :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="含权类型" v-show="dataType.param5">
      <k-field-select v-model="formData.containRightType" data-dict="embOptTyp"/>
    </k-form-item>
    <k-form-item label="行权方式" v-show="dataType.param5">
      <k-field-select v-model="formData.lineRightWay" data-dict="xcsRitMth"/>
    </k-form-item>
    <k-form-item label="固定行权日" v-show="dataType.param5">
      <k-field-date v-model="formData.fixedExerciseDate"/>
    </k-form-item>
    <k-form-item label="首次行权日期" v-show="dataType.param5">
      <k-field-date v-model="formData.firstExerciseDate"/>
    </k-form-item>
    <k-form-item label="行权周期" v-show="dataType.param5">
      <k-field-text v-model="formData.vestingPeriod" data-max-value="99999999999999" data-validate-type="int" data-min-value="0"/>
    </k-form-item>
    <k-form-item label="行权价格" v-show="dataType.param5">
      <k-field-text v-model="formData.executivePrice" data-max-value="999999999999" data-unit-value="10000"
                    :data-allowblank="true" data-validate-type="money" :data-max-length="15" data-min-value="0" data-digits="2"/>
    </k-form-item>
    <k-form-item label="永续条款类型" v-show="dataType.param5">
      <k-field-select v-model="formData.perpetuityClauseType" data-dict="perpTyp"/>
    </k-form-item>
    <k-form-item label="利息递延条款类型" v-show="dataType.param5">
      <k-field-select v-model="formData.deferredClauseType" data-dict="intrPpnTyp"/>
    </k-form-item>
    <k-form-item label="递延利息是否计息" v-show="dataType.param5">
      <k-field-select v-model="formData.whetherDeferredInterest" data-dict="isTrue"/>
    </k-form-item>
    <k-form-item label="首次重定价日期" v-show="dataType.param5">
      <k-field-date v-model="formData.firstRepricingDate"/>
    </k-form-item>
    <k-form-item label="重定价周期" v-show="dataType.param5">
      <k-field-text v-model="formData.repricingCycle" data-max-value="99999999999999"
                    data-validate-type="int" data-min-value="0"/>
    </k-form-item>
    <k-form-item label="部分赎回标识" v-show="dataType.param5">
      <k-field-select v-model="formData.partialRedemptionMark" data-dict="isTrue"/>
    </k-form-item>
    <k-form-item label="部分赎回比例（%）" v-show="dataType.param5">
      <k-field-text v-model="formData.partialRedemptionRate" :data-max-length="10"
                    :data-allowblank="true" data-validate-type="number" data-digits="4" data-max-value="100" data-min-value="0"/>
    </k-form-item>
    <k-form-item label="选择权" v-show="dataType.param5">
      <k-field-text v-model="formData.remarkPar3" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="行权条件说明" v-show="dataType.param5">
      <k-field-text v-model="formData.remarkPar4" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="融资人所属地区" v-show="dataType.param5">
      <k-field-select v-model="formData.countryCode" data-dict="prod_sale_area"/>
    </k-form-item>
    <k-form-item label="融资人组织机构（社会信用）代码" v-show="dataType.param5">
      <k-field-text v-model="formData.financOrgCode" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="增信机构代码" v-show="dataType.param5">
      <k-field-text v-model="formData.creditOrgCode" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="增信机构名称" v-show="dataType.param5">
      <k-field-text v-model="formData.creditOrgName" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="融资总费率" v-show="dataType.param5">
      <k-field-text v-model="formData.feePar" :data-max-length="10" :data-allowblank="!(dataType.param5)"
                    data-validate-type="number" data-digits="4"  data-max-value="100" data-min-value="0"/>
    </k-form-item>
    <k-form-item label="是否为银行理财产品" v-show="dataType.param6">
      <k-field-select v-model="formData.isBankprod"  data-dict="1yes0no"/>
    </k-form-item>
    <k-form-item label="产品登记编码" v-show="dataType.param6">
      <k-field-text v-model="formData.registrationCode" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="资管计划发起人机构编码" v-show="dataType.param6">
      <k-field-text v-model="formData.planOrgCode" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="资管计划登记编码" v-show="dataType.param6">
      <k-field-text v-model="formData.registPar" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="管理人" v-show="dataType.param6">
      <k-field-text v-model="formData.issuOrgname" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="托管人" v-show="dataType.param6">
      <k-field-text v-model="formData.trustPeople" :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="资金实际投向" v-show="dataType.param6">
      <k-field-select v-model="formData.capitalActualInvest" data-dict="actual_invest_dir_fund"/>
    </k-form-item>
    <k-form-item label="资金运用方式" v-show="dataType.param6">
      <k-field-text v-model="formData.cooperMode"  :data-max-length="120"/>
    </k-form-item>
    <k-form-item label="资金运用行业" v-show="dataType.param6">
      <k-field-select v-model="formData.cooperTrade" data-dict="isuOrgBlgIdt"/>
    </k-form-item>
    <k-form-item label="资管计划属性" v-show="dataType.param6">
      <k-field-select v-model="formData.typePar3" data-dict="astMngPlanPrpt"/>
    </k-form-item>
    <k-form-item label="购买结构" v-show="dataType.param6">
      <k-field-select v-model="formData.typePar4" data-dict="buyStrc"/>
    </k-form-item>
    <k-form-item label="管理方式" v-show="dataType.param6">
      <k-field-select v-model="formData.manageMethod" data-dict="mngMth"/>
    </k-form-item>
    <k-form-item label="管理费率" v-show="dataType.param6">
      <k-field-text v-model="formData.rateDescManage" :data-max-length="10"
                    :data-allowblank="!(dataType.param6)" data-validate-type="number" data-digits="4"
                    data-max-value="100" data-min-value="(0"/>
    </k-form-item>
    <k-form-item label="托管费率" v-show="dataType.param6">
      <k-field-text v-model="formData.rateDescTrust" :data-max-length="10"
                    :data-allowblank="!(dataType.param6)" data-validate-type="number" data-digits="4"
                    data-max-value="100" data-min-value="(0"/>
    </k-form-item>
    <k-form-item label="交易相关合计费率" v-show="dataType.param6">
      <k-field-text v-model="formData.rateDescTrans" :data-max-length="10"
                    :data-allowblank="!(dataType.param6)" data-validate-type="number" data-digits="4"
                    data-max-value="100" data-min-value="(0"/>
    </k-form-item>
    <k-form-item label="中介服务机构合计费率" v-show="dataType.param6">
      <k-field-text v-model="formData.rateDescService" :data-max-length="10"
                    :data-allowblank="!(dataType.param6)" data-validate-type="number" data-digits="4"
                    data-max-value="100" data-min-value="(0"/>
    </k-form-item>
    <k-form-item label="其他合计费率" v-show="dataType.param6" >
      <k-field-text v-model="formData.rateDescOther" :data-max-length="10"
                    :data-allowblank="!(dataType.param6)" data-validate-type="number" data-digits="4"
                    data-max-value="100" data-min-value="(0"/>
    </k-form-item>
    <k-form-item label="股权退出安排" v-show="dataType.param7">
      <k-field-date v-model="formData.gqEdate"/>
    </k-form-item>
    <k-form-item label="是否为质押融资" v-show="dataType.param7">
      <k-field-select v-model="formData.isPledgeFinancing"   data-dict="1yes0no"/>
    </k-form-item>
    <k-form-item label="是否为债转股" v-show="dataType.param7">
      <k-field-select v-model="formData.isSwap" data-dict="1yes0no"/>
    </k-form-item>
    <k-form-item label="标的类别" v-show="dataType.param8">
      <k-field-select v-model="formData.structprodSubject" data-dict="stru_deposit_type"/>
    </k-form-item>
    <k-form-item label="持有目的" v-show="dataType.param8">
      <k-field-select v-model="formData.holdingPurpose" data-dict="holding_obj"/>
    </k-form-item>
    <k-form-item label="是否标准化金融工具特征" v-show="dataType.param8">
      <k-field-select v-model="formData.isFinancialInstruments" data-dict="1yes0no"/>
    </k-form-item>
    <k-form-item label="所属国家或地区" v-show="dataType.param9 || dataType.param10 || dataType.param11">
      <k-field-select v-model="formData.issCountry" data-dict="tr_iss_country"/>
    </k-form-item>
    <k-form-item label="期限（天）" v-show="dataType.param9 || dataType.param11">
      <k-field-text v-model="formData.termDays" :data-max-length="10"
                    :data-allowblank="true" data-validate-type="number" data-digits="0" data-min-value="0"/>
    </k-form-item>
    <k-form-item label="资产价值" v-show="dataType.param9 || dataType.param10">
      <k-field-text v-model="formData.assetValue" data-max-value="999999999999" data-unit-value="10000"
                    :data-allowblank="!(dataType.param9 || dataType.param10)" data-validate-type="money" :data-max-length="15" data-min-value="(0" data-digits="2"/>
    </k-form-item>
    <k-form-item label="是否为同业借款" v-show="dataType.param11" >
      <k-field-select v-model="formData.isinterbankloan" data-dict="1yes0no"/>
    </k-form-item>
    <k-form-item label="具体类别" v-show="dataType.param12" >
      <k-field-select v-model="formData.concreteType"data-dict="spcType"/>
    </k-form-item>
    <k-form-item label="发行方式" v-show="dataType.param12" >
      <k-field-select v-model="formData.issMod" data-dict="iss_mode_bond"/>
    </k-form-item>
    <k-form-item label="主体评级" v-show="dataType.param12" >
      <k-field-select v-model="formData.subLevel" data-dict="mainRating"/>
    </k-form-item>
    <k-form-item label="发行机构类型（按规模划分）" v-show="dataType.param12" >
      <k-field-select v-model="formData.issTypeScale" data-dict="instituteTypeTech"/>
    </k-form-item>
    <k-form-item label="发行机构类型（按技术领域划分）" v-show="dataType.param12" >
      <k-field-select v-model="formData.issTypeTec" data-dict="isuOrgTypTchno"/>
    </k-form-item>
    <k-form-item label="发行机构类型（按经济类型划分）" v-show="dataType.param12" >
      <k-field-select v-model="formData.issTypeEco" data-dict="isuOrgTypEcn"/>
    </k-form-item>
    <k-form-item label="发行机构所属行业" v-show="dataType.param12" >
      <k-field-select v-model="formData.publisherTrade" data-dict="isuOrgBlgIdt"/>
    </k-form-item>
    <k-form-item label="登记托管机构" v-show="dataType.param12" >
      <k-field-select v-model="formData.trusteeAgency" data-dict="regTrstOrg"/>
    </k-form-item>
    <k-form-item label="登记托管机构说明" v-show="dataType.param12" >
      <k-field-text v-model="formData.trusteeAgencyRemark" :data-max-length="120"/>
    </k-form-item>
  </k-form>
</template>

<script>
export default {
  name: "AppBottomDealForm",
  props: {
    formData : {},

  },
  data() {
    return {
      dataType:{
        param1 : false, //5
        param2 : false,  //29
        param3 : false,  //2
        param4 : false,  //19
        param5 : false,  //10000
        param6 : false,  //36
        param7 : false,  //200
        param8 : false,  //300
        param9 : false,  //400
        param10 : false,  //500
        param11 : false,  //600
        param12 : false,  //qita
      }
    };
  },
  methods: {
    bottonTypeChange(param){
      this.dataType = {
        param1 : false, //5
        param2 : false,  //29
        param3 : false,  //2
        param4 : false,  //19
        param5 : false,  //10000
        param6 : false,  //36
        param7 : false,  //200
        param8 : false,  //300
        param9 : false,  //400
        param10 : false,  //500
        param11 : false,  //600
        param12 : false,  //qita
      };

      if(param==='5'){//存放同业字段展示
        this.dataType.param1 = true;
      }else if(param==='29'){//同业借款
        this.dataType.param2 = true;
      }else if(param==='2'){//买入返售/卖出回购
        this.dataType.param3 = true;
      }else if(param==='19'){//私募基金
        this.dataType.param4 = true;
      }else if(param==='10000'){//非标准化债权类资产
        this.dataType.param5 = true;
      }else if(param==='36'){//资产管理产品
        this.dataType.param6 = true;
      }else if(param==='100'){//股权
        this.dataType.param7 = true;
      }else if(param==='101'){ //金融衍生品（权证）
        this.dataType.param8 = true;
      }else if(param==='400'){//商品类资产（贵金属）
        this.dataType.param9 = true;
      }else if(param==='500'){//另类资产
        this.dataType.param10 = true;
      }else if(param==='600'){//其他（负债）类资产
        this.dataType.param11 = true;
      }else{//其他类型非标
        this.dataType.param12 = true;
      }


    },

  },

  created() {
    let t8SysAdtypeId = this.formData.t8SysAdtypeId;
    if (t8SysAdtypeId){
      this.bottonTypeChange(t8SysAdtypeId);
    }

  },
}
</script>

<style scoped>

</style>
