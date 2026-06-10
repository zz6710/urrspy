<template>
	<div class="py-page">
		<k-form-search-customize data-target="prodInfoOdsGrid" v-model="SearchParam" data-label-width="100px">
			<k-form-item label="产品代码">
				<k-field-select
					v-model="SearchParam.prodCode"
					data-action="ProdInfoOds.findProdInfoOds"
					ref="prodCodeId"
					data-display-field="prodCode,prodName"
					data-value-field="prodCode"
				/>
			</k-form-item>
			<k-form-item label="产品名称">
				<k-field-text v-model="SearchParam.prodName" />
			</k-form-item>
			<k-form-item label="中债登记编码">
				<k-field-text v-model="SearchParam.checkInon" />
			</k-form-item>
			<k-form-item label="人行登记编码">
				<k-field-text v-model="SearchParam.pbcRegcode" />
			</k-form-item>
			<k-form-item label="产品状态">
				<k-field-select v-model="SearchParam.runStat" data-dict="prod_stat_new" />
			</k-form-item>
			<!--      <k-form-item label="是否母产品">-->
			<!--        <k-field-select v-model="SearchParam.motherFundFlag" data-dict="1yes0no"/>-->
			<!--      </k-form-item>-->
			<k-form-item label="申报状态">
				<k-field-select v-model="SearchParam.prodReportStatus" data-dict="prod_report_status" />
			</k-form-item>
			<!--      <k-form-item label="成立日">-->
			<!--        <k-field-date v-model="SearchParam.establishDate1" style="width:106px;font-size: 4px" :dataMaxValue="SearchParam.beginDate2"/>-->
			<!--        - -->
			<!--        <k-field-date v-model="SearchParam.establishDate2" style="width:106px;font-size: 4px" :dataMinValue="SearchParam.beginDate1"/>-->
			<!--      </k-form-item>-->
			<!--      <k-form-item label="到期日">-->
			<!--        <k-field-date v-model="SearchParam.realEndDate1" style="width:106px;font-size: 4px" :dataMaxValue="SearchParam.endDate2" />-->
			<!--        - -->
			<!--        <k-field-date v-model="SearchParam.realEndDate2" style="width:106px;font-size: 4px" :dataMinValue="SearchParam.endDate1" />-->
			<!--      </k-form-item>-->
		</k-form-search-customize>
		<div class="py-page-container">
			<!-- <div class="table-top-btns">
      <div class="left">
        <k-btn slot="button" class="btn-custom-plain" data-functype="POPUP"  data-target="noticePublishPopup" :data-handler="checkBatchPublishData"
          v-if="global.isShowAuthorityButton('ProdInfoOds.batchSendProdFile')">
          <md-icon md-src="/static/svg/confirm.svg"></md-icon>批量发送文件
        </k-btn>
      </div>
    </div> -->
			<k-grid
				ref="prodInfoOdsGrid"
				@data-row-select="selectRow"
				data-action="ProdInfoOds.findProdInfoOds"
				data-fixed="right"
				data-operate-width="300px"
				data-operate-column="true"
				@init="
					(grid) => {
						this.$kgrid = grid;
					}
				"
				:data-autoload="true"
				:data-checkbox="true"
				data-checkbox-id="id"
				data-tree-id="id"
				:data-reserve-selection="true"
			>
				<k-grid-column data-header="序号" data-name="id" data-hidden="true" />
				<k-grid-column data-header="产品系列ID" data-name="prodSeriesCd" data-hidden="true" />
				<k-grid-column data-header="子账户代码" data-name="childAccountCode" data-hidden="true" />
				<k-grid-column data-header="产品代码" data-name="prodCode" />
				<k-grid-column data-header="产品名称" data-name="prodName" />
				<k-grid-column data-header="产品模式" data-name="prodMod" data-dict="t8_prod_mod" />
				<k-grid-column data-header="产品形态" data-name="prodForm" data-dict="t8_prod_form" data-hidden="true"/>
				<k-grid-column data-header="托管行账户" data-name="accountCode" data-hidden="true"/>
				<k-grid-column data-header="分级产品标志" data-name="motherFundFlag" data-dict="mother_fund_flag" data-hidden="true"/>
        <k-grid-column data-header="中债登记编码" data-name="checkInon"/>
        <k-grid-column data-header="人行登记编码" data-name="pbcRegcode" />
				<k-grid-column data-header="母产品代码" data-name="motherFundCode" data-hidden="true" />
				<k-grid-column data-header="净值产品模式" data-name="netvalProdMod" data-hidden="true" />
				<k-grid-column
					data-header="产品投资性质"
					data-name="t8InvestPropType"
					data-hidden="true"
					data-dict="t8_prod_classify"
				/>
				<k-grid-column
					data-header="开放频率"
					data-name="openFreq"
					data-hidden="true"
					data-dict="t8_prod_open_freq"
				/>
				<k-grid-column
					data-header="产品风险等级"
					data-name="riskLev"
					data-hidden="true"
					data-dict="risk_rate"
				/>
				<k-grid-column
					data-header="投资者风险偏好"
					data-name="investorTrend"
					data-hidden="true"
					data-dict="t8_inv_rsk_prf"
				/>
				<k-grid-column
					data-header="投资者类型"
					data-name="investObject"
					data-hidden="true"
					data-dict="t8_prod_fxdx"
				/>
				<k-grid-column data-header="实际规模" data-name="actualScale" data-hidden="true" />
				<k-grid-column
					data-header="产品类型"
					data-name="prodType"
					data-hidden="true"
					data-dict="actual_invest_dir_fund"
				/>
				<k-grid-column
					data-header="是否代销"
					data-name="isConsignment"
					data-hidden="true"
					data-dict="1yes0no"
				/>
				<k-grid-column
					data-header="是否现金管理类"
					data-name="cashType"
					data-hidden="true"
					data-dict="1yes0no"
				/>
				<k-grid-column
					data-header="收益类型"
					data-name="incomeType"
					data-hidden="true"
					data-dict="prod_revenue_type"
				/>
				<k-grid-column data-header="预期规模" data-name="expeScale" data-hidden="true" />
				<k-grid-column data-header="发行规模下限" data-name="minSize" data-hidden="true" />
				<k-grid-column data-header="巨额赎回比例" data-name="bigRate" data-hidden="true" />
				<k-grid-column data-header="发行币种" data-name="issuCcy" data-hidden="true" />
				<k-grid-column data-header="收益币种" data-name="incomeCcy" data-hidden="true" />
				<k-grid-column data-header="投资币种" data-name="investCuy" data-hidden="true" />
				<k-grid-column data-header="本金返回币种" data-name="returnCcy" data-hidden="true" />
				<k-grid-column data-header="认购起始日" data-name="subsBdate" data-hidden="true" data-type="date" />
				<k-grid-column data-header="认购截止日" data-name="subsEdate" data-hidden="true" data-type="date" />
				<k-grid-column data-header="认购起始时间" data-name="subsBtime" data-hidden="true" />
				<k-grid-column data-header="认购结束时间" data-name="subsEtime" data-hidden="true" />
				<k-grid-column data-header="成立日期" data-name="establishDate" data-type="date" />
				<k-grid-column
					data-header="开放起始日期"
					data-name="openBeginDate"
					data-hidden="true"
					data-type="date"
				/>
				<k-grid-column data-header="开放起始时间" data-name="openBeginTime" data-hidden="true" />
				<k-grid-column data-header="开放结束日期" data-name="openEndDate" data-hidden="true" data-type="date" />
				<k-grid-column data-header="开放结束时间" data-name="openEndTime" data-hidden="true" />
				<k-grid-column data-header="收市时间" data-name="transCloseTime" data-hidden="true" />
				<k-grid-column
					data-header="首次开放日"
					data-name="firstDisparkDay"
					data-hidden="true"
					data-type="date"
				/>
				<k-grid-column
					data-header="首次开放确认日"
					data-name="firstOpenDay"
					data-hidden="true"
					data-type="date"
				/>
				<k-grid-column data-header="冷静期起始日" data-name="calmsBdate" data-hidden="true" data-type="date" />
				<k-grid-column
					data-header="是否周期分红"
					data-name="periodicDicvidendFlag"
					data-hidden="true"
					data-dict="1yes0no"
				/>
				<k-grid-column data-header="冷静期结束日" data-name="calmsEdate" data-hidden="true" data-type="date" />
				<k-grid-column data-header="到期日期" data-name="endDate" data-type="date" />
				<k-grid-column data-header="实际到期日" data-name="realEndDate" data-hidden="true" data-type="date" />
				<k-grid-column data-header="兑付日期" data-name="payDate" data-type="date" />
				<k-grid-column data-header="实际兑付日" data-name="realPayDate" data-hidden="true" data-type="date" />
				<k-grid-column data-header="产品天数" data-name="prodPeriod" data-hidden="true" />
				<k-grid-column data-header="开放规则" data-name="openRule" data-hidden="true" data-dict="t8_opn_rul" />
				<k-grid-column data-header="开放周期" data-name="opendays" data-hidden="true" />
				<k-grid-column
					data-header="开放期维度"
					data-name="openDimen"
					data-hidden="true"
					data-dict="t8_open_dimen"
				/>
				<k-grid-column data-header="开放期长度" data-name="openLength" data-hidden="true" />
				<k-grid-column
					data-header="投资周期维度"
					data-name="investCycleDimen"
					data-hidden="true"
					data-dict="t8_invest_cycle_dimen"
				/>
				<k-grid-column data-header="投资周期长度" data-name="investCycleLength" data-hidden="true" />
				<k-grid-column
					data-header="是否设置最短持有期限"
					data-name="isMinHoldTerm"
					data-hidden="true"
					data-dict="1yes0no"
				/>
				<k-grid-column data-header="最短持有期限(天)" data-name="minHoldTerm" data-hidden="true" />
				<k-grid-column
					data-header="最短持有期后是否自由赎回"
					data-name="redeemAfterHold"
					data-hidden="true"
					data-dict="1yes0no"
				/>
				<k-grid-column data-header="赎回资金到账N值" data-name="redeemToaccountDate" data-hidden="true" />
				<k-grid-column data-header="单笔认购起点金额(个人)" data-name="minSubsP" data-hidden="true" />
				<k-grid-column data-header="单笔认购递增金额(个人)" data-name="stepSubsP" data-hidden="true" />
				<k-grid-column data-header="单笔认购最高金额(个人)" data-name="maxSubsP" data-hidden="true" />
				<k-grid-column data-header="累计购买金额上限(个人)" data-name="maxBuyP" data-hidden="true" />
				<k-grid-column data-header="单笔申购起点金额(个人)" data-name="minPchsP" data-hidden="true" />
				<k-grid-column data-header="单笔申购递增金额(个人)" data-name="stepPchsP" data-hidden="true" />
				<k-grid-column data-header="单笔申购最高金额(个人)" data-name="maxPchsP" data-hidden="true" />
				<k-grid-column data-header="单笔赎回最低份额(个人)" data-name="minRedeemP" data-hidden="true" />
				<k-grid-column data-header="单笔赎回递增金额(元/个人)" data-name="stepRedeemP" data-hidden="true" />
				<k-grid-column
					data-header="单户单日快速赎回上限(个人)"
					data-name="dailyRedeemMaxP"
					data-hidden="true"
				/>
				<k-grid-column data-header="最低持有份额(个人)" data-name="minHoldP" data-hidden="true" />
				<k-grid-column data-header="单户最大持仓上限(个人)" data-name="maxHoldVolP" data-hidden="true" />
				<k-grid-column data-header="单笔认购起点金额(机构)" data-name="minSubsM" data-hidden="true" />
				<k-grid-column data-header="单笔认购递增金额(机构)" data-name="stepSubsM" data-hidden="true" />
				<k-grid-column data-header="单笔认购最高金额(机构)" data-name="maxSubsM" data-hidden="true" />
				<k-grid-column data-header="累计购买金额上限(机构)" data-name="maxBuyM" data-hidden="true" />
				<k-grid-column data-header="单笔申购起点金额(机构)" data-name="minPchsM" data-hidden="true" />
				<k-grid-column data-header="单笔申购递增金额(机构)" data-name="stepPchsM" data-hidden="true" />
				<k-grid-column data-header="单笔申购最高金额(机构)" data-name="maxPchsM" data-hidden="true" />
				<k-grid-column data-header="单笔赎回最低份额(机构)" data-name="minRedeemM" data-hidden="true" />
				<k-grid-column data-header="单笔赎回递增金额(元/机构)" data-name="stepRedeemM" data-hidden="true" />
				<k-grid-column
					data-header="单户单日快速赎回上限(机构)"
					data-name="dailyRedeemMaxM"
					data-hidden="true"
				/>
				<k-grid-column data-header="最低持有份额(机构)" data-name="minHoldM" data-hidden="true" />
				<k-grid-column data-header="单户最大持仓上限(机构)" data-name="maxHoldVolM" data-hidden="true" />
				<k-grid-column data-header="扣费方式" data-name="deductMod" data-hidden="true" />
				<k-grid-column data-header="份额赎回方式" data-name="redeemMod" data-hidden="true" />
				<k-grid-column data-header="收费方式" data-name="chargeMod" data-hidden="true" />
				<k-grid-column
					data-header="是否有认购费"
					data-name="isSubscribeFee"
					data-hidden="true"
					data-dict="1yes0no"
				/>
				<k-grid-column data-header="认购费率" data-name="subscribeRate" data-hidden="true" />
				<k-grid-column
					data-header="是否有申购费"
					data-name="isSubsRate"
					data-hidden="true"
					data-dict="1yes0no"
				/>
				<k-grid-column
					data-header="是否有赎回费"
					data-name="isRedeemRate"
					data-hidden="true"
					data-dict="1yes0no"
				/>
				<k-grid-column data-header="申购费率" data-name="subsRate" data-hidden="true" />
				<k-grid-column data-header="赎回费率" data-name="redeemRate" data-hidden="true" />
				<k-grid-column data-header="投资人数上限" data-name="investormax" data-hidden="true" />
				<k-grid-column data-header="产品状态" data-name="runStat" data-dict="prod_stat_new" />
				<k-grid-column data-header="发布日期" data-name="issuedate" data-hidden="true" data-type="date" />
				<k-grid-column data-header="处理日期" data-name="dealDate" data-hidden="true" data-type="date" />
				<k-grid-column data-header="申报状态" data-name="prodReportStatus" data-dict="prod_report_status" />
				<k-grid-column data-header="文件发送状态" data-name="fileStatus" data-dict="file_status" data-hidden="true" />
				<template slot="operate" slot-scope="scope">
					<k-btn
						class="btn-custom-text specialClass"
						data-descript="修改"
						data-functype="POPUP"
						data-size="mini"
						data-target="prodInfoOdsPopup"
						v-if="global.isShowAuthorityButton('ProdInfoOds.updateProdInfoOds')"
					>
						修改
					</k-btn>
					<k-btn
						class="btn-custom-text specialClass"
						data-descript="修改"
						data-functype="POPUP"
						data-size="mini"
						data-target="prodInfoOdsPopupReport"
						style="width: 110px"
						:data-disabled="scope.row.row.fileStatus === '1' || scope.row.row.motherFundFlag === '0'"
						v-if="global.isShowAuthorityButton('ProdInfoOds.updateProdInfoOds')"
					>
						申报状态
					</k-btn>
					<k-btn
						class="btn-custom-text specialClass"
						data-descript="详情"
						data-functype="POPUP"
						data-size="mini"
						data-target="prodInfoOdsPopupDetail"
					>
						详情
					</k-btn>
				</template>
			</k-grid>
		</div>

		<k-popup ref="prodInfoOdsPopup" data-title="产品信息" :dataDialogDrag="true">
			<k-form ref="prodInfoOdsForm" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px" isFormBodyScreen>
				<k-form-item label="序号" v-show="false">
					<k-field-text v-model="formData.id" />
				</k-form-item>
				<k-form-item label="产品代码">
					<k-field-text
						v-model="formData.prodCode"
						:data-disabled="true"
						:data-allowblank="false"
						:data-max-length="32"
					/>
				</k-form-item>
				<k-form-item label="产品名称">
					<k-field-text
						v-model="formData.prodName"
						:data-disabled="true"
						:data-allowblank="false"
						:data-max-length="128"
					/>
				</k-form-item>
				<k-form-item label="产品系列">
					<k-field-select
						v-model="formData.prodSeriesCd"
						:data-disabled="true"
						:data-allowblank="false"
						data-action="ProdInfoOds.findProdSeries"
						data-display-field="prodSeriesCd,prodSeriesName"
						data-value-field="prodSeriesCd"
					/>
				</k-form-item>
				<k-form-item label="产品模式">
					<k-field-select
						v-model="formData.prodMod"
						:data-disabled="true"
						:data-allowblank="false"
						data-dict="t8_prod_mod"
					/>
				</k-form-item>
				<k-form-item label="产品模式" v-show="false">
					<k-field-text v-model="formData.netvalProdMod" />
				</k-form-item>
				<k-form-item label="托管行账户">
					<k-field-text v-model="formData.accountCode" :data-allowblank="false" :data-max-length="50" />
				</k-form-item>
				<!-- <k-form-item label="子账户代码" v-show="false">
					<k-field-text v-model="formData.childAccountCode" :data-allowblank="false" :data-max-length="50" />
				</k-form-item> -->
				<!-- <k-form-item label="分级产品标志" v-show="false">
					<k-field-select
						v-model="formData.motherFundFlag"
						:data-allowblank="false"
						data-dict="mother_fund_flag"
					/>
				</k-form-item> -->
				<k-form-item label="母产品代码">
					<k-field-text
						v-model="formData.motherFundCode"
						:data-allowblank="formData.motherFundFlag !== '2'"
						:data-max-length="32"
					/>
				</k-form-item>
				<k-form-item label="中债登记编码">
					<k-field-text
						v-model="formData.checkInon"
						:data-max-length="32"
						:data-disabled="formData.prodReportStatus !== '3' || formData.fileStatus !== '0'"
					/>
				</k-form-item>
        <k-form-item label="人行登记编码">
          <k-field-text v-model="formData.pbcRegcode" :data-max-length="32"/>
        </k-form-item>
				<k-form-item label="产品投资性质">
					<k-field-select v-model="formData.t8InvestPropType" data-dict="t8_prod_classify" />
				</k-form-item>
				<k-form-item label="开放频率">
					<k-field-select v-model="formData.openFreq" data-dict="t8_prod_open_freq" />
				</k-form-item>
				<k-form-item label="产品风险等级">
					<k-field-select v-model="formData.riskLev" data-dict="risk_rate" />
				</k-form-item>
				<k-form-item label="投资者风险偏好">
					<k-field-select v-model="formData.investorTrend" data-dict="t8_inv_rsk_prf" />
				</k-form-item>
				<k-form-item label="投资者类型">
					<k-field-select v-model="formData.investObject" data-dict="t8_prod_fxdx" data-multiple="true" />
				</k-form-item>
				<k-form-item label="实际规模">
					<k-field-text
						v-model="formData.actualScale"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="产品类型" v-show="false">
					<k-field-select v-model="formData.prodType" data-dict="actual_invest_dir_fund" />
				</k-form-item>
				<k-form-item label="是否代销">
					<k-field-select v-model="formData.isConsignment" :data-allowblank="false" data-dict="1yes2no" />
				</k-form-item>
				<k-form-item label="是否现金管理类">
					<k-field-select v-model="formData.cashType" :data-allowblank="false" data-dict="1yes2no" />
				</k-form-item>
				<k-form-item label="收益类型">
					<k-field-select v-model="formData.incomeType" data-dict="prod_revenue_type" />
				</k-form-item>
				<k-form-item label="产品状态">
					<k-field-select
						v-model="formData.runStat"
						:data-disabled="true"
						:data-allowblank="false"
						data-dict="prod_stat_new"
					/>
				</k-form-item>
				<k-form-item label="产品形态">
					<k-field-select v-model="formData.prodForm" :data-allowblank="false" data-dict="t8_prod_form" />
				</k-form-item>
				<k-form-item label="预期规模">
					<k-field-text
						v-model="formData.expeScale"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="发行规模下限">
					<k-field-text
						v-model="formData.minSize"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="巨额赎回比例">
					<k-field-text
						v-model="formData.bigRate"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="5"
						data-digits="2"
					/>
				</k-form-item>
				<k-form-item label="发行币种">
					<k-field-select v-model="formData.issuCcy" data-dict="cur_type" />
				</k-form-item>
				<k-form-item label="收益币种">
					<k-field-select v-model="formData.incomeCcy" data-dict="cur_type" />
				</k-form-item>
				<k-form-item label="投资币种">
					<k-field-select v-model="formData.investCuy" data-dict="cur_type" />
				</k-form-item>
				<k-form-item label="本金返回币种">
					<k-field-select v-model="formData.returnCcy" data-dict="cur_type" />
				</k-form-item>
				<k-form-item label="认购起始日">
					<k-field-date v-model="formData.subsBdate" />
				</k-form-item>
				<k-form-item label="认购截止日">
					<k-field-date v-model="formData.subsEdate" />
				</k-form-item>
				<k-form-item label="认购起始时间">
					<k-field-time v-model="formData.subsBtime" />
				</k-form-item>
				<k-form-item label="认购结束时间">
					<k-field-time v-model="formData.subsEtime" />
				</k-form-item>
				<k-form-item label="发行成立日期">
					<k-field-date v-model="formData.establishDate" />
				</k-form-item>
				<k-form-item label="开放起始日期">
					<k-field-date v-model="formData.openBeginDate" />
				</k-form-item>
				<k-form-item label="开放起始时间">
					<k-field-time v-model="formData.openBeginTime" />
				</k-form-item>
				<k-form-item label="开放结束日期">
					<k-field-date v-model="formData.openEndDate" />
				</k-form-item>
				<k-form-item label="开放结束时间">
					<k-field-time v-model="formData.openEndTime" />
				</k-form-item>
				<k-form-item label="收市时间">
					<k-field-time v-model="formData.transCloseTime" />
				</k-form-item>
				<k-form-item label="首次开放日">
					<k-field-date v-model="formData.firstDisparkDay" />
				</k-form-item>
				<k-form-item label="首次开放确认日">
					<k-field-date v-model="formData.firstOpenDay" />
				</k-form-item>
				<k-form-item label="冷静期起始日">
					<k-field-date v-model="formData.calmsBdate" />
				</k-form-item>
				<k-form-item label="是否周期分红">
					<k-field-select v-model="formData.periodicDicvidendFlag" data-dict="1yes0no" />
				</k-form-item>
				<k-form-item label="冷静期结束日">
					<k-field-date v-model="formData.calmsEdate" />
				</k-form-item>
				<k-form-item label="到期日期">
					<k-field-date v-model="formData.endDate" />
				</k-form-item>
				<k-form-item label="实际到期日">
					<k-field-date v-model="formData.realEndDate" />
				</k-form-item>
				<k-form-item label="兑付日期">
					<k-field-date v-model="formData.payDate" />
				</k-form-item>
				<k-form-item label="实际兑付日">
					<k-field-date v-model="formData.realPayDate" />
				</k-form-item>
				<k-form-item label="产品天数">
					<k-field-text
						v-model="formData.prodPeriod"
						data-validate-type="number"
						data-type="number"
						data-min-value="(0"
						data-show-gbmoney="true"
						:data-max-length="8"
						data-digits="0"
					/>
				</k-form-item>
				<k-form-item label="开放规则">
					<k-field-select v-model="formData.openRule" data-dict="t8_opn_rul" />
				</k-form-item>
				<k-form-item label="开放周期">
					<k-field-text v-model="formData.opendays" :data-max-length="10" />
				</k-form-item>
				<k-form-item label="开放期维度">
					<k-field-select v-model="formData.openDimen" data-dict="t8_open_dimen" />
				</k-form-item>
				<k-form-item label="开放期长度">
					<k-field-text v-model="formData.openLength" :data-max-length="10" />
				</k-form-item>
				<k-form-item label="投资周期维度">
					<k-field-select v-model="formData.investCycleDimen" data-dict="t8_invest_cycle_dimen" />
				</k-form-item>
				<k-form-item label="投资周期长度">
					<k-field-text v-model="formData.investCycleLength" :data-max-length="5" />
				</k-form-item>
				<k-form-item label="是否设置最短持有期限">
					<k-field-select v-model="formData.isMinHoldTerm" data-dict="1yes2no" />
				</k-form-item>
				<k-form-item label="最短持有期限(天)">
					<k-field-text
						v-model="formData.minHoldTerm"
						data-validate-type="number"
						data-type="number"
						data-min-value="(0"
						data-show-gbmoney="true"
						:data-max-length="8"
						data-digits="0"
					/>
				</k-form-item>
				<k-form-item label="最短持有期后是否自由赎回">
					<k-field-select v-model="formData.redeemAfterHold" data-dict="1yes2no" />
				</k-form-item>
				<k-form-item label="赎回资金到账N值">
					<k-field-text v-model="formData.redeemToaccountDate" :data-max-length="10" />
				</k-form-item>
				<k-form-item label="单笔认购起点金额(个人)">
					<k-field-text
						v-model="formData.minSubsP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="单笔认购递增金额(个人)">
					<k-field-text
						v-model="formData.stepSubsP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="单笔认购最高金额(个人)">
					<k-field-text
						v-model="formData.maxSubsP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="累计购买金额上限(个人)">
					<k-field-text
						v-model="formData.maxBuyP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="单笔申购起点金额(个人)">
					<k-field-text
						v-model="formData.minPchsP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="单笔申购递增金额(个人)">
					<k-field-text
						v-model="formData.stepPchsP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="单笔申购最高金额(个人)">
					<k-field-text
						v-model="formData.maxPchsP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="单笔赎回最低份额(个人)">
					<k-field-text
						v-model="formData.minRedeemP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="单笔赎回递增金额(个人)">
					<k-field-text
						v-model="formData.stepRedeemP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="单户单日快赎上限(个人)">
					<k-field-text
						v-model="formData.dailyRedeemMaxP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="最低持有份额(个人)">
					<k-field-text
						v-model="formData.minHoldP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="单户最大持仓上限(个人)">
					<k-field-text
						v-model="formData.maxHoldVolP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="单笔认购起点金额(机构)">
					<k-field-text
						v-model="formData.minSubsM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="单笔认购递增金额(机构)">
					<k-field-text
						v-model="formData.stepSubsM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="单笔认购最高金额(机构)">
					<k-field-text
						v-model="formData.maxSubsM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="累计购买金额上限(机构)">
					<k-field-text
						v-model="formData.maxBuyM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="单笔申购起点金额(机构)">
					<k-field-text
						v-model="formData.minPchsM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="单笔申购递增金额(机构)">
					<k-field-text
						v-model="formData.stepPchsM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="单笔申购最高金额(机构)">
					<k-field-text
						v-model="formData.maxPchsM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="单笔赎回最低份额(机构)">
					<k-field-text
						v-model="formData.minRedeemM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="单笔赎回递增金额(机构)">
					<k-field-text
						v-model="formData.stepRedeemM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="单户单日快赎上限(机构)">
					<k-field-text
						v-model="formData.dailyRedeemMaxM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="最低持有份额(机构)">
					<k-field-text
						v-model="formData.minHoldM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="单户最大持仓上限(机构)">
					<k-field-text
						v-model="formData.maxHoldVolM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
					/>
				</k-form-item>
				<k-form-item label="扣费方式">
					<k-field-select v-model="formData.deductMod" data-dict="t8_dec_mth" />
				</k-form-item>
				<k-form-item label="份额赎回方式">
					<k-field-select v-model="formData.redeemMod" data-dict="t8_lot_rdm_mth" />
				</k-form-item>
				<k-form-item label="收费方式">
					<k-field-select v-model="formData.chargeMod" data-dict="t8_chrg_mth" />
				</k-form-item>
				<k-form-item label="是否有认购费">
					<k-field-select v-model="formData.isSubscribeFee" data-dict="1yes0no" />
				</k-form-item>
				<k-form-item label="认购费率">
					<k-field-text
						v-model="formData.subscribeRate"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-max-value="100)"
						data-show-gbmoney="true"
						data-integer-length="12"
						data-digits="8"
						data-placeholder="单位(%)"
					/>
				</k-form-item>
				<k-form-item label="是否有申购费">
					<k-field-select v-model="formData.isSubsRate" data-dict="1yes0no" />
				</k-form-item>
				<k-form-item label="是否有赎回费">
					<k-field-select v-model="formData.isRedeemRate" data-dict="1yes0no" />
				</k-form-item>
				<k-form-item label="申购费率">
					<k-field-text
						v-model="formData.subsRate"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-max-value="100)"
						data-show-gbmoney="true"
						data-integer-length="12"
						data-digits="8"
						data-placeholder="单位(%)"
					/>
				</k-form-item>
				<k-form-item label="赎回费率">
					<k-field-text
						v-model="formData.redeemRate"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-max-value="100)"
						data-show-gbmoney="true"
						data-integer-length="12"
						data-digits="8"
						data-placeholder="单位(%)"
					/>
				</k-form-item>
				<k-form-item label="投资人数上限">
					<k-field-text
						v-model="formData.investormax"
						data-validate-type="number"
						data-type="number"
						data-min-value="(0"
						data-show-gbmoney="true"
						:data-max-length="11"
						data-digits="0"
					/>
				</k-form-item>
				<k-form-item label="发布日期" v-show="false">
					<k-field-date v-model="formData.issuedate" />
				</k-form-item>
				<k-form-item label="处理日期" v-show="false">
					<k-field-date v-model="formData.dealDate" />
				</k-form-item>
				<k-form-item label="申报状态" v-show="false">
					<k-field-date v-model="formData.prodReportStatus" />
				</k-form-item>
				<k-form-item label="文件发送状态" v-show="false">
					<k-field-date v-model="formData.fileStatus" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-target="prodInfoOdsGrid"
						ref="submitBtn"
						data-from="prodInfoOdsForm"
						:data-model="formData"
						data-action="ProdInfoOds.updateProdInfoOds"
					>
						确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<k-popup ref="prodInfoOdsPopupDetail" data-title="产品信息" :dataDialogDrag="true">
			<k-form ref="prodInfoOdsForm" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px" isFormBodyScreen>
				<k-form-item label="序号" v-show="false">
					<k-field-text v-model="formData.id" />
				</k-form-item>
				<k-form-item label="产品代码">
					<k-field-text
						v-model="formData.prodCode"
						:data-disabled="true"
						:data-allowblank="false"
						:data-max-length="32"
					/>
				</k-form-item>
				<k-form-item label="产品名称">
					<k-field-text
						v-model="formData.prodName"
						:data-disabled="true"
						:data-allowblank="false"
						:data-max-length="128"
					/>
				</k-form-item>
				<k-form-item label="产品系列">
					<k-field-select
						v-model="formData.prodSeriesCd"
						:data-disabled="true"
						:data-allowblank="false"
						data-action="ProdInfoOds.findProdSeries"
						data-display-field="prodSeriesCd,prodSeriesName"
						data-value-field="prodSeriesCd"
					/>
				</k-form-item>
				<k-form-item label="产品模式">
					<k-field-select
						v-model="formData.prodMod"
						:data-disabled="true"
						:data-allowblank="false"
						data-dict="t8_prod_mod"
					/>
				</k-form-item>
				<k-form-item label="产品模式" v-show="false">
					<k-field-text v-model="formData.netvalProdMod" />
				</k-form-item>
				<k-form-item label="托管行账户">
					<k-field-text
						v-model="formData.accountCode"
						:data-allowblank="false"
						:data-max-length="50"
						:data-disabled="true"
					/>
				</k-form-item>
				<!-- <k-form-item label="子账户代码" v-show="false">
					<k-field-text
						v-model="formData.childAccountCode"
						:data-allowblank="false"
						:data-max-length="50"
						:data-disabled="true"
					/>
				</k-form-item> -->
				<!-- <k-form-item label="分级产品标志" v-show="false">
					<k-field-select
						v-model="formData.motherFundFlag"
						:data-allowblank="false"
						data-dict="mother_fund_flag"
						:data-disabled="true"
					/>
				</k-form-item> -->
				<k-form-item label="母产品代码">
					<k-field-text
						v-model="formData.motherFundCode"
						:data-allowblank="formData.motherFundFlag !== '2'"
						:data-max-length="32"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="中债登记编码">
					<k-field-text v-model="formData.checkInon" :data-max-length="32" :data-disabled="true" />
				</k-form-item>
        <k-form-item label="人行登记编码">
          <k-field-text v-model="formData.pbcRegcode" :data-max-length="32" :data-disabled="true"/>
        </k-form-item>
				<k-form-item label="产品投资性质">
					<k-field-select
						v-model="formData.t8InvestPropType"
						data-dict="t8_prod_classify"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="开放频率">
					<k-field-select v-model="formData.openFreq" data-dict="t8_prod_open_freq" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="产品风险等级">
					<k-field-select v-model="formData.riskLev" data-dict="risk_rate" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="投资者风险偏好">
					<k-field-select v-model="formData.investorTrend" data-dict="t8_inv_rsk_prf" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="投资者类型">
					<k-field-select
						v-model="formData.investObject"
						data-dict="t8_prod_fxdx"
						:data-disabled="true"
						data-multiple="true"
					/>
				</k-form-item>
				<k-form-item label="实际规模">
					<k-field-text
						v-model="formData.actualScale"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="产品类型" v-show="false">
					<k-field-select
						v-model="formData.prodType"
						data-dict="actual_invest_dir_fund"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="是否代销">
					<k-field-select
						v-model="formData.isConsignment"
						:data-allowblank="false"
						data-dict="1yes2no"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="是否现金管理类">
					<k-field-select
						v-model="formData.cashType"
						:data-allowblank="false"
						data-dict="1yes2no"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="收益类型">
					<k-field-select v-model="formData.incomeType" data-dict="prod_revenue_type" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="产品状态">
					<k-field-select
						v-model="formData.runStat"
						:data-disabled="true"
						:data-allowblank="false"
						data-dict="prod_stat_new"
					/>
				</k-form-item>
				<k-form-item label="产品形态">
					<k-field-select
						v-model="formData.prodForm"
						:data-allowblank="false"
						data-dict="t8_prod_form"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="预期规模">
					<k-field-text
						v-model="formData.expeScale"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="发行规模下限">
					<k-field-text
						v-model="formData.minSize"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="巨额赎回比例">
					<k-field-text
						v-model="formData.bigRate"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="5"
						data-digits="2"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="发行币种">
					<k-field-select v-model="formData.issuCcy" data-dict="cur_type" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="收益币种">
					<k-field-select v-model="formData.incomeCcy" data-dict="cur_type" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="投资币种">
					<k-field-select v-model="formData.investCuy" data-dict="cur_type" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="本金返回币种">
					<k-field-select v-model="formData.returnCcy" data-dict="cur_type" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="认购起始日">
					<k-field-date v-model="formData.subsBdate" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="认购截止日">
					<k-field-date v-model="formData.subsEdate" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="认购起始时间">
					<k-field-time v-model="formData.subsBtime" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="认购结束时间">
					<k-field-time v-model="formData.subsEtime" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="发行成立日期">
					<k-field-date v-model="formData.establishDate" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="开放起始日期">
					<k-field-date v-model="formData.openBeginDate" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="开放起始时间">
					<k-field-time v-model="formData.openBeginTime" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="开放结束日期">
					<k-field-date v-model="formData.openEndDate" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="开放结束时间">
					<k-field-time v-model="formData.openEndTime" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="收市时间">
					<k-field-time v-model="formData.transCloseTime" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="首次开放日">
					<k-field-date v-model="formData.firstDisparkDay" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="首次开放确认日">
					<k-field-date v-model="formData.firstOpenDay" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="冷静期起始日">
					<k-field-date v-model="formData.calmsBdate" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="是否周期分红">
					<k-field-select
						v-model="formData.periodicDicvidendFlag"
						data-dict="1yes0no"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="冷静期结束日">
					<k-field-date v-model="formData.calmsEdate" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="到期日期">
					<k-field-date v-model="formData.endDate" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="实际到期日">
					<k-field-date v-model="formData.realEndDate" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="兑付日期">
					<k-field-date v-model="formData.payDate" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="实际兑付日">
					<k-field-date v-model="formData.realPayDate" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="产品天数">
					<k-field-text
						v-model="formData.prodPeriod"
						data-validate-type="number"
						data-type="number"
						data-min-value="(0"
						data-show-gbmoney="true"
						:data-max-length="8"
						data-digits="0"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="开放规则">
					<k-field-select v-model="formData.openRule" data-dict="t8_opn_rul" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="开放周期">
					<k-field-text v-model="formData.opendays" :data-max-length="10" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="开放期维度">
					<k-field-select v-model="formData.openDimen" data-dict="t8_open_dimen" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="开放期长度">
					<k-field-text v-model="formData.openLength" :data-max-length="10" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="投资周期维度">
					<k-field-select
						v-model="formData.investCycleDimen"
						data-dict="t8_invest_cycle_dimen"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="投资周期长度">
					<k-field-text v-model="formData.investCycleLength" :data-max-length="5" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="是否设置最短持有期限">
					<k-field-select v-model="formData.isMinHoldTerm" data-dict="1yes0no" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="最短持有期限(天)">
					<k-field-text
						v-model="formData.minHoldTerm"
						data-validate-type="number"
						data-type="number"
						data-min-value="(0"
						data-show-gbmoney="true"
						:data-max-length="8"
						data-digits="0"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="最短持有期后是否自由赎回">
					<k-field-select v-model="formData.redeemAfterHold" data-dict="1yes0no" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="赎回资金到账N值">
					<k-field-text v-model="formData.redeemToaccountDate" :data-max-length="10" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="单笔认购起点金额(个人)">
					<k-field-text
						v-model="formData.minSubsP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="单笔认购递增金额(个人)">
					<k-field-text
						v-model="formData.stepSubsP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="单笔认购最高金额(个人)">
					<k-field-text
						v-model="formData.maxSubsP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="累计购买金额上限(个人)">
					<k-field-text
						v-model="formData.maxBuyP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="单笔申购起点金额(个人)">
					<k-field-text
						v-model="formData.minPchsP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="单笔申购递增金额(个人)">
					<k-field-text
						v-model="formData.stepPchsP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="单笔申购最高金额(个人)">
					<k-field-text
						v-model="formData.maxPchsP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="单笔赎回最低份额(个人)">
					<k-field-text
						v-model="formData.minRedeemP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="单笔赎回递增金额(个人)">
					<k-field-text
						v-model="formData.stepRedeemP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="单户单日快赎上限(个人)">
					<k-field-text
						v-model="formData.dailyRedeemMaxP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="最低持有份额(个人)">
					<k-field-text
						v-model="formData.minHoldP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="单户最大持仓上限(个人)">
					<k-field-text
						v-model="formData.maxHoldVolP"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="单笔认购起点金额(机构)">
					<k-field-text
						v-model="formData.minSubsM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="单笔认购递增金额(机构)">
					<k-field-text
						v-model="formData.stepSubsM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="单笔认购最高金额(机构)">
					<k-field-text
						v-model="formData.maxSubsM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="累计购买金额上限(机构)">
					<k-field-text
						v-model="formData.maxBuyM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="单笔申购起点金额(机构)">
					<k-field-text
						v-model="formData.minPchsM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="单笔申购递增金额(机构)">
					<k-field-text
						v-model="formData.stepPchsM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="单笔申购最高金额(机构)">
					<k-field-text
						v-model="formData.maxPchsM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="单笔赎回最低份额(机构)">
					<k-field-text
						v-model="formData.minRedeemM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="单笔赎回递增金额(机构)">
					<k-field-text
						v-model="formData.stepRedeemM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="单户单日快赎上限(机构)">
					<k-field-text
						v-model="formData.dailyRedeemMaxM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="最低持有份额(机构)">
					<k-field-text
						v-model="formData.minHoldM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="单户最大持仓上限(机构)">
					<k-field-text
						v-model="formData.maxHoldVolM"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-show-gbmoney="true"
						data-integer-length="16"
						data-digits="2"
						data-placeholder="单位(元)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="扣费方式">
					<k-field-select v-model="formData.deductMod" data-dict="t8_dec_mth" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="份额赎回方式">
					<k-field-select v-model="formData.redeemMod" data-dict="t8_lot_rdm_mth" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="收费方式">
					<k-field-select v-model="formData.chargeMod" data-dict="t8_chrg_mth" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="是否有认购费">
					<k-field-select v-model="formData.isSubscribeFee" data-dict="1yes0no" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="认购费率">
					<k-field-text
						v-model="formData.subscribeRate"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-max-value="100)"
						data-show-gbmoney="true"
						data-integer-length="12"
						data-digits="8"
						data-placeholder="单位(%)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="是否有申购费">
					<k-field-select v-model="formData.isSubsRate" data-dict="1yes0no" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="是否有赎回费">
					<k-field-select v-model="formData.isRedeemRate" data-dict="1yes0no" :data-disabled="true" />
				</k-form-item>
				<k-form-item label="申购费率">
					<k-field-text
						v-model="formData.subsRate"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-max-value="100)"
						data-show-gbmoney="true"
						data-integer-length="12"
						data-digits="8"
						data-placeholder="单位(%)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="赎回费率">
					<k-field-text
						v-model="formData.redeemRate"
						data-validate-type="money"
						data-type="money"
						data-min-value="(0"
						data-max-value="100)"
						data-show-gbmoney="true"
						data-integer-length="12"
						data-digits="8"
						data-placeholder="单位(%)"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="投资人数上限">
					<k-field-text
						v-model="formData.investormax"
						data-validate-type="number"
						data-type="number"
						data-min-value="(0"
						data-show-gbmoney="true"
						:data-max-length="11"
						data-digits="0"
						:data-disabled="true"
					/>
				</k-form-item>
				<k-form-item label="发布日期" v-show="false">
					<k-field-date v-model="formData.issuedate" />
				</k-form-item>
				<k-form-item label="处理日期" v-show="false">
					<k-field-date v-model="formData.dealDate" />
				</k-form-item>
				<k-form-item label="申报状态" v-show="false">
					<k-field-date v-model="formData.prodReportStatus" />
				</k-form-item>
				<k-form-item label="文件发送状态" v-show="false">
					<k-field-date v-model="formData.fileStatus" />
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn class="btn-custom-plain" data-functype="CLOSE">关闭</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
		<!-- 批量发送文件   -->
		<k-popup ref="noticePublishPopup" title="批量发送文件">
			<k-form ref="noticePublishForm" data-ui="element" dataLabelWidth="130px" dataInputWidth="220px">
				<k-form-item label="确定发送吗？" :data-col="2" data-input-width="80px"> </k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-target="prodInfoGrid"
						ref="submitPublishBtn"
						data-from="noticePublishForm"
						@click="batchPublishChannel"
					>
						<span v-show="showSubmitBtn">发送</span>
						<i v-show="!showSubmitBtn" class="el-icon-loading" />
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>

		<k-popup ref="prodInfoOdsPopupReport" data-title="登记申报结果" :dataDialogDrag="true">
			<k-form ref="prodInfoOdsForm" :data-col="2" dataLabelWidth="170px" dataInputWidth="300px" isFormBodyScreen>
				<k-form-item label="序号" v-show="false">
					<k-field-text v-model="formData.id" />
				</k-form-item>
				<k-form-item label="产品代码">
					<k-field-text v-model="formData.prodCode" :data-disabled="true" :data-max-length="32" />
				</k-form-item>
				<k-form-item label="产品名称">
					<k-field-text v-model="formData.prodName" :data-disabled="true" :data-max-length="128" />
				</k-form-item>
				<k-form-item label="申报结果">
					<k-field-select
						v-model="formData.prodReportStatus"
						data-dict="prod_report_status"
						@data-on-change="resetCheckInon"
					/>
				</k-form-item>
				<k-form-item label="中债登记编码">
					<k-field-text
						v-model="formData.checkInon"
						:data-max-length="32"
						:data-disabled="formData.prodReportStatus !== '3'"
						:data-allowblank="formData.prodReportStatus !== '3'"
					/>
				</k-form-item>
				<k-form-footer data-align="center">
					<k-btn
						class="btn-custom-primary"
						data-functype="SUBMIT"
						data-target="prodInfoOdsGrid"
						ref="submitBtn"
						data-from="prodInfoOdsForm"
						:data-model="formData"
						data-action="ProdInfoOds.updateProdInfoReport"
					>
						确定
					</k-btn>
					<k-btn class="btn-custom-plain" data-functype="CLOSE">取消</k-btn>
				</k-form-footer>
			</k-form>
		</k-popup>
	</div>
</template>

<script>
import Tools from "@/utils/tools";

export default {
	name: "ProdInfoOds",
	data() {
		return {
			formData: {},
			selectRowData: {},
			SearchParam: {}, //查询参数
			showSubmitBtn: true,
			prodReportStatus: "",
			fileStatus: "",
			motherFundFlag: "",
			checkInon: "",
		};
	},
	computed: {
		queryParam() {
			return {
				prodCode: this.SearchParam.prodCode,
				establishDate1: this.SearchParam.establishDate1,
				establishDate2: this.SearchParam.establishDate2,
				realEndDate1: this.SearchParam.realEndDate1,
				realEndDate2: this.SearchParam.realEndDate2,
			};
		},
	},

	methods: {
		selectRow(row, column, event) {
			this.selectRowData = Object.assign({}, row);
			this.formData = Object.assign({}, row);
		},
		//检查选中数据是否满足可发送状态与中债登记编码不为空的条件
		checkBatchPublishData() {
			let pass = true;
			let prodCodes = "";
			let prodCodea = "";
			const _this = this;
			const list = _this.$kgrid.getSelected();
			if (list.length === 0) {
				Tools.alertTime("请先勾选产品信息复选框！", "danger", 5000);
				return false;
			}
			//当没有选中时不会进入
			for (let i = 0; i < list.length; i++) {
				//报备失败和成功才可以发送
				if (list[i].prodReportStatus === "0") {
					pass = false;
					if (!pass) {
						Tools.alert("产品申报状态必须为[报备成功]或[报备失败]！", "danger");
						this.$refs.prodInfoOdsGrid.setSelected([]);
						return false;
					}
				}
				if (list[i].fileStatus === "1") {
					//已发送文件的产品
					pass = false;
					prodCodes = list[i].prodCode;
					if (!pass) {
						Tools.alert("产品 [" + prodCodes + "] 已发送文件，请勿重复操作", "danger");
						this.$refs.prodInfoOdsGrid.setSelected([]);
						return false;
					}
				}
				if (list[i].prodReportStatus === "3") {
					//报备成功的产品，中债登记编码要有值
					pass = true;
					if (list[i].checkInon === "") {
						pass = false;
					}
					prodCodea = list[i].prodCode;
					if (!pass) {
						Tools.alert("产品 [" + prodCodea + "] 的中债登记编码为空，请先维护", "danger");
						this.$refs.prodInfoOdsGrid.setSelected([]);
						return false;
					}
				}
			}
		},
		//批量发送文件
		batchPublishChannel() {
			const _this = this;
			const list = _this.$kgrid.getSelected();
			this.showSubmitBtn = false;
			this.httpUtil
				.comnUpdate({
					action: "ProdInfoOds.batchSendProdFile",
					params: { list: JSON.stringify(list) },
					successAlert: true,
				})
				.then((data) => {
					this.showSubmitBtn = true;
					this.$refs.noticePublishPopup.close();
					this.$refs.prodInfoOdsGrid.load(this.queryParam);
					this.$refs.prodInfoOdsGrid.setSelected([]);
				})
				.catch(() => {
					this.showSubmitBtn = true;
					this.$refs.noticePublishPopup.close();
					this.$refs.prodInfoOdsGrid.load(this.queryParam);
					this.$refs.prodInfoOdsGrid.setSelected([]);
				});
		},
		resetCheckInon() {
			this.$set(this.formData, "checkInon", "");
		},
	},
};
</script>
