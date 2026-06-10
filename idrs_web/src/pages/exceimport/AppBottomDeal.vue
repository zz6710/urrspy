<template>
  <div class="py-page">
    <div>
      <k-form-search-customize data-target="appBottomDealGrid" data-model-name="AppBottomDeal" v-model="selectRowData">
        <k-form-item label="券商机构类型">
          <k-field-select v-model="selectRowData.securitiesfirmsType" data-action="ValReportTab.findValReportTabs"
                          data-display-field="reporttabName"  data-value-field="id" />
        </k-form-item>

        <k-form-item label="资产代码">
          <k-field-text v-model="selectRowData.ftoolCode" :data-max-length="120" />
        </k-form-item>
      </k-form-search-customize>
    </div>
    <div class="py-page-container">
      <div class="table-top-btns">
        <div class="left">
          <k-btn slot="button" class="btn-custom-primary" data-functype="POPUP" :data-handler="()=>this.formData={}" data-target="addAppBottomDealPopup">
            <md-icon md-src="/static/svg/add.svg" />新增</k-btn>
        </div>
      </div>
      <k-grid ref="appBottomDealGrid" @data-row-select="selectRow" data-action="AppBottomDeal.findAppBottomDeals"
              data-fixed="right">
        <k-grid-column data-header="ID" data-hidden="true" data-name="id"/>
        <k-grid-column data-header="券商机构类型" data-hidden="true"  data-name="securitiesfirmsType"/>
        <k-grid-column data-header="券商机构类型" data-name="reporttabName"/>
        <k-grid-column data-header="行权条件说明" data-hidden="true" data-name="remarkPar4"/>
        <k-grid-column data-header="融资人组织机构（社会信用）代码" data-hidden="true" data-name="financOrgCode"/>
        <k-grid-column data-header="投资类型" data-hidden="true" data-name="investType"/>
        <k-grid-column data-header="发行机构所属行业" data-hidden="true" data-name="publisherTrade"/>
        <k-grid-column data-header="管理人" data-hidden="true" data-name="issuOrgname"/>
        <k-grid-column data-header="资金运用方式" data-hidden="true" data-name="cooperMode"/>
        <k-grid-column data-header="基金公司名称" data-hidden="true" data-name="funComName"/>
        <k-grid-column data-header="投资阶段" data-hidden="true" data-name="investmentStage"/>
        <k-grid-column data-header="项目收益率(利率)%" data-hidden="true" data-name="projectYield"/>
        <k-grid-column data-header="付息频率（个月/次）" data-hidden="true" data-name="payFreq"/>
        <k-grid-column data-header="结构档次" data-hidden="true" data-name="structureGrade"/>
        <k-grid-column data-header="基础资产类型" data-hidden="true" data-name="baseAssetsType"/>
        <k-grid-column data-header="融资项目" data-hidden="true" data-name="remarkPar2"/>
        <k-grid-column data-header="项目是否属于重点监控行业和领域" data-hidden="true" data-name="isImport"/>
        <k-grid-column data-header="担保方式" data-hidden="true" data-name="vouch"/>
        <k-grid-column data-header="行权方式" data-hidden="true" data-name="lineRightWay"/>
        <k-grid-column data-header="选择权" data-hidden="true" data-name="remarkPar3"/>
        <k-grid-column data-header="融资总费率" data-hidden="true" data-name="feePar"/>
        <k-grid-column data-header="资产代码" data-name="ftoolCode"/>
        <k-grid-column data-header="是否为银行理财产品" data-hidden="true" data-name="isBankprod"/>
        <k-grid-column data-header="资管计划发起人机构编码" data-hidden="true" data-name="planOrgCode"/>
        <k-grid-column data-header="资金实际投向" data-hidden="true" data-name="capitalActualInvest"/>
        <k-grid-column data-header="管理费率" data-hidden="true" data-name="rateDescManage"/>
        <k-grid-column data-header="中介服务机构合计费率" data-hidden="true" data-name="rateDescService"/>
        <k-grid-column data-header="交易对手方名称" data-hidden="true" data-name="osideNames"/>
        <k-grid-column data-header="回购利率" data-hidden="true" data-name="yield"/>
        <k-grid-column data-header="投资企业类型（按技术领域划分）" data-hidden="true" data-name="inButypeTec"/>
        <k-grid-column data-header="基准利率种类" data-hidden="true" data-name="baseRateType"/>
        <k-grid-column data-header="浮动因子（%）" data-hidden="true" data-name="fdyz"/>
        <k-grid-column data-header="首次行权日期" data-hidden="true" data-name="firstExerciseDate"/>
        <k-grid-column data-header="永续条款类型" data-hidden="true" data-name="perpetuityClauseType"/>
        <k-grid-column data-header="递延利息是否计息" data-hidden="true" data-name="whetherDeferredInterest"/>
        <k-grid-column data-header="部分赎回标识" data-hidden="true" data-name="partialRedemptionMark"/>
        <k-grid-column data-header="融资人所属地区" data-hidden="true" data-name="countryCode"/>
        <k-grid-column data-header="融资项目所属行业" data-hidden="true" data-name="commIndCode"/>
        <k-grid-column data-header="交易流通场所" data-name="tradePlaces"  data-dict="tacdingPlace"/>
        <k-grid-column data-header="发行机构类型（按技术领域划分）" data-hidden="true" data-name="issTypeTec"/>
        <k-grid-column data-header="购买结构" data-hidden="true" data-name="typePar4"/>
        <k-grid-column data-header="其他合计费率" data-hidden="true" data-name="rateDescOther"/>
        <k-grid-column data-header="交易方向" data-hidden="true" data-name="direction"/>
        <k-grid-column data-header="融资人所属行业" data-hidden="true" data-name="comm2IndCode"/>
        <k-grid-column data-header="还本方式" data-hidden="true" data-name="debtWay"/>
        <k-grid-column data-header="超额收益分配比例（%）" data-hidden="true" data-name="excessIncomeRate"/>
        <k-grid-column data-header="融资人类型（按规模划分）" data-hidden="true" data-name="finaTypeScale"/>
        <k-grid-column data-header="担保人与融资人关系" data-hidden="true" data-name="relationBtwGaf"/>
        <k-grid-column data-header="融资人主体评级" data-hidden="true" data-name="scorePar2"/>
        <k-grid-column data-header="利息递延条款类型" data-hidden="true" data-name="deferredClauseType"/>
        <k-grid-column data-header="发行方式（公募、私募）" data-hidden="true" data-name="issMod"/>
        <k-grid-column data-header="中债一级分类" data-hidden="true" data-name="cbcType"/>
        <k-grid-column data-header="中债二级分类" data-hidden="true" data-name="cbcSubtype"/>
        <k-grid-column data-header="起息日" data-hidden="true" data-name="begindate"/>
        <k-grid-column data-header="登记备案机构" data-hidden="true" data-name="depot"/>
        <k-grid-column data-header="是否属于政府投资基金" data-hidden="true" data-name="isGovermentFundation"/>
        <k-grid-column data-header="基金管理机构名称" data-hidden="true" data-name="funManName"/>
        <k-grid-column data-header="计息类型" data-hidden="true" data-name="interestType"/>
        <k-grid-column data-header="担保情况说明" data-hidden="true" data-name="vouchDesc"/>
        <k-grid-column data-header="增信机构代码" data-hidden="true" data-name="creditOrgCode"/>
        <k-grid-column data-header="业务品种" data-name="t8SysAdtypeId" data-dict="t8_bottomn_type"/>
        <k-grid-column data-header="主体评级" data-hidden="true" data-name="subLevel"/>
        <k-grid-column data-header="资金运用行业" data-hidden="true" data-name="cooperTrade"/>
        <k-grid-column data-header="资管计划属性" data-hidden="true" data-name="typePar3"/>
        <k-grid-column data-header="托管费率" data-hidden="true" data-name="rateDescTrust"/>
        <k-grid-column data-header="交易相关合计费率" data-hidden="true" data-name="rateDescTrans"/>
        <k-grid-column data-header="计息基础" data-hidden="true" data-name="basedays"/>
        <k-grid-column data-header="是否属于买入返售" data-hidden="true" data-name="isBuyback"/>
        <k-grid-column data-header="利息分布方式" data-hidden="true" data-name="modeDistribute"/>
        <k-grid-column data-header="利差(BP)" data-hidden="true" data-name="ratepare"/>
        <k-grid-column data-header="含权类型" data-hidden="true" data-name="containRightType"/>
        <k-grid-column data-header="行权周期" data-hidden="true" data-name="vestingPeriod"/>
        <k-grid-column data-header="具体类别" data-hidden="true" data-name="concreteType"/>
        <k-grid-column data-header="发行机构类型（按规模划分）" data-hidden="true" data-name="issTypeScale"/>
        <k-grid-column data-header="资管计划登记编码" data-hidden="true" data-name="registPar"/>
        <k-grid-column data-header="存款方账号" data-hidden="true" data-name="accountCode"/>
        <k-grid-column data-header="行业" data-hidden="true" data-name="industry"/>
        <k-grid-column data-header="基金托管机构名称" data-hidden="true" data-name="funCusName"/>
        <k-grid-column data-header="份额面值" data-hidden="true" data-name="volValue"/>
        <k-grid-column data-header="法定到期日" data-hidden="true" data-name="legalMaturity"/>
        <k-grid-column data-header="规则付息标识" data-hidden="true" data-name="rulePayFlag"/>
        <k-grid-column data-header="担保性质" data-hidden="true" data-name="natureGuarantee"/>
        <k-grid-column data-header="资产内部评级" data-hidden="true" data-name="scorePar3"/>
        <k-grid-column data-header="资产外部评级" data-hidden="true" data-name="scorePar4"/>
        <k-grid-column data-header="首次重定价日期" data-hidden="true" data-name="firstRepricingDate"/>
        <k-grid-column data-header="增信机构名称" data-hidden="true" data-name="creditOrgName"/>
        <k-grid-column data-header="发行机构类型（按经济类型划分）" data-hidden="true" data-name="issTypeEco"/>
        <k-grid-column data-header="登记托管机构" data-hidden="true" data-name="trusteeAgency"/>
        <k-grid-column data-header="登记托管机构说明" data-hidden="true" data-name="trusteeAgencyRemark"/>
        <k-grid-column data-header="是否由金融资产投资公司发行" data-hidden="true" data-name="isJrcompany"/>
        <k-grid-column data-header="托管人" data-hidden="true" data-name="trustPeople"/>
        <k-grid-column data-header="金额" data-hidden="true" data-name="principal"/>
        <k-grid-column data-header="交易对手方类型" data-hidden="true" data-name="orgType"/>
        <k-grid-column data-header="投资基金类型" data-hidden="true" data-name="investFundType"/>
        <k-grid-column data-header="是否为固定收益类" data-hidden="true" data-name="investPropType"/>
        <k-grid-column data-header="投资企业类型（按规模划分）" data-hidden="true" data-name="inButypeSca"/>
        <k-grid-column data-header="投资企业类型（按经济类型划分）" data-hidden="true" data-name="inButypeEco"/>
        <k-grid-column data-header="还本付息情况说明" data-hidden="true" data-name="debtServiceDescription"/>
        <k-grid-column data-header="是否有浮动因子" data-hidden="true" data-name="isFlow"/>
        <k-grid-column data-header="分期还本条款标识" data-hidden="true" data-name="installmentsMarked"/>
        <k-grid-column data-header="融资人" data-hidden="true" data-name="osideName"/>
        <k-grid-column data-header="融资人内部信用评级" data-hidden="true" data-name="scorePar1"/>
        <k-grid-column data-header="融资人类型（按经济类型划分）" data-hidden="true" data-name="finaTypeEco"/>
        <k-grid-column data-header="重点监控行业和领域类别" data-hidden="true" data-name="spIndCode"/>
        <k-grid-column data-header="固定行权日" data-hidden="true" data-name="fixedExerciseDate"/>
        <k-grid-column data-header="行权价格" data-hidden="true" data-name="executivePrice"/>
        <k-grid-column data-header="重定价周期" data-hidden="true" data-name="repricingCycle"/>
        <k-grid-column data-header="部分赎回比例（%）" data-hidden="true" data-name="partialRedemptionRate"/>
        <k-grid-column data-header="产品登记编码" data-hidden="true" data-name="registrationCode"/>
        <k-grid-column data-header="管理方式" data-hidden="true" data-name="manageMethod"/>
        <k-grid-column data-header="到期日" data-hidden="true" data-name="enddate"/>
        <k-grid-column data-header="政府投资基金投向" data-hidden="true" data-name="govermentWays"/>
        <k-grid-column data-header="是否由金融资产投资公司发行" data-hidden="true" data-name="isJrcompanyFundation"/>
        <k-grid-column data-header="基金投资资产" data-hidden="true" data-name="funInvestAsset"/>
        <k-grid-column data-header="是否标准化票据" data-hidden="true" data-name="isBzpj"/>
        <k-grid-column data-header="否投向地方政府融资平台" data-hidden="true" data-name="isLgfplatform"/>
        <k-grid-column data-header="收/受益权类型" data-hidden="true" data-name="inBenType"/>
        <k-grid-column data-header="外部评级机构名称及对融资人评级结果" data-hidden="true" data-name="outPar"/>
        <k-grid-column data-header="融资人类型（按技术领域划分）" data-hidden="true" data-name="finaTypeTec"/>
        <k-grid-column data-header="重点监控行业和领域类别说明" data-hidden="true" data-name="spIndDesc"/>
        <k-grid-column data-header="抵质押物类型" data-hidden="true" data-name="collateralType"/>
        <k-grid-column data-header="抵质押物价值（元）" data-hidden="true" data-name="collateralValue"/>
        <k-grid-column data-header="其他类型非标资产类别" data-hidden="true" data-name="fbZcType"/>
        <k-grid-column data-header="非标行业归属" data-hidden="true" data-name="comm3IndCode"/>
        <k-grid-column data-header="报送资产编码" data-hidden="true" data-name="reportAssetCode"/>
        <k-grid-column data-header="非标资产类型" data-hidden="true" data-name="fbzcType"/>
        <k-grid-column data-header="股权退出安排" data-hidden="true" data-name="gqEdate"/>
        <k-grid-column data-header="是否为质押融资" data-hidden="true" data-name="isPledgeFinancing"/>
        <k-grid-column data-header="是否为债转股" data-hidden="true" data-name="isSwap"/>
        <k-grid-column data-header="标的类别" data-hidden="true" data-name="structprodSubject"/>
        <k-grid-column data-header="持有目的" data-hidden="true" data-name="holdingPurpose"/>
        <k-grid-column data-header="是否标准化金融工具特征" data-hidden="true" data-name="isFinancialInstruments"/>
        <k-grid-column data-header="所属国家或地区" data-hidden="true" data-name="issCountry"/>
        <k-grid-column data-header="期限（天）" data-hidden="true" data-name="termDays"/>
        <k-grid-column data-header="资产价值" data-hidden="true" data-name="assetValue"/>
        <k-grid-column data-header="是否为同业借款" data-hidden="true" data-name="isinterbankloan"/>

        <template slot="operate" slot-scope="scope">
          <k-btn class="btn-custom-text" data-descript="修改底层资产信息表" data-functype="POPUP" data-size="mini"
            data-target="editAppBottomDealPopup">
            修改
          </k-btn>
          <k-btn class="btn-custom-text" data-functype="SUBMIT" data-action="AppBottomDeal.deleteAppBottomDeal" data-size="mini"
                 data-type="danger" data-target="appBottomDealGrid" :data-confirm="true" data-descript="删除底层资产信息表">
            删除
          </k-btn>
        </template>
      </k-grid>
    </div>



	<!--    添加底层资产信息表弹出框   -->
	<k-popup ref="addAppBottomDealPopup" data-title="新增" :dataDialogDrag="true" :dataPopupAppendToBody="true">
    <AppBottomDealForm ref="addAppBottomDeal" :formData="formData"/>
    <k-form-footer data-align="center">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT" :data-handler="checkParamsForAdd" data-action="AppBottomDeal.addAppBottomDeal" data-from="appBottomDealForm"
             :data-model="formData" data-target="appBottomDealGrid">
        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
      </k-btn>
      <k-btn class="btn-custom-plain" data-functype="CLOSE">
        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
    </k-form-footer>
	</k-popup>

	<!--    修改底层资产信息表弹出框   -->
	<k-popup ref="editAppBottomDealPopup" data-title="修改" :dataDialogDrag="true" :dataPopupAppendToBody="true">
    <AppBottomDealForm ref="editAppBottomDeal" :formData="formData"/>
    <k-form-footer data-align="center">
      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="AppBottomDeal.updateAppBottomDeal" data-from="appBottomDealForm"
             :data-model="formData" data-target="appBottomDealGrid" :data-handler="checkParamsForEdit">
        <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
      </k-btn>
      <k-btn class="btn-custom-plain" data-functype="CLOSE">
        <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
    </k-form-footer>
	</k-popup>
  </div>
</template>

<script>
import AppBottomDealForm from "./AppBottomDealForm";
  export default {
    name:"AppBottomDeal",
    components: {
      AppBottomDealForm,
    },
    data() {
      return {
        formData: {},
        selectRowData: {},
      };
    },
    methods: {
      selectRow(row, column, event) {
        // this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      },
      checkParamsForAdd(){
        let flag = this.$refs.addAppBottomDeal.$refs.appBottomDealForm.validate();
        return flag;
      },
      checkParamsForEdit(){
        let flag = this.$refs.editAppBottomDeal.$refs.appBottomDealForm.validate();
        return flag;
      },
    }
  };
</script>
