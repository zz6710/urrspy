<template>
  <div class="py-page">
      <div>
          <k-form-search-customize data-model-name="ProdRgFlInfoErr" data-target="prodRgFlInfoErrGrid" data-label-width="80px" v-model = "searchParam">
            <k-form-item label="导入日期">
              <k-field-date v-model="BreathDay" data-type="daterange" data-date-format="yyyyMM"
                            data-value-format="yyyyMMdd"/>
            </k-form-item>
            <k-form-item label="登记流水号">
              <k-field-text v-model="searchParam.registerSerno"/>
            </k-form-item>
          </k-form-search-customize>
        </div>
    <div class="py-page-container">
      <k-grid ref="prodRgFlInfoErrGrid" @data-row-select="selectRow" data-operate-column="false" data-action="ProdRgFlInfoErr.findProdRgFlInfoErrs" >
		<k-grid-column data-header="产品名称错误描述" data-name="prodNameDesc"></k-grid-column>
		<k-grid-column data-header="行内标识码错误描述" data-name="identCodeDesc"></k-grid-column>
		<k-grid-column data-header="产品品牌错误描述" data-name="prodBrandDesc"></k-grid-column>
		<k-grid-column data-header="产品期次错误描述" data-name="prodTermNoDesc"></k-grid-column>
		<k-grid-column data-header="发行机构代码错误描述" data-name="bankCodeDesc"></k-grid-column>
		<k-grid-column data-header="产品审批人身份证号错误描述" data-name="approverIdCodeDesc"></k-grid-column>
		<k-grid-column data-header="产品设计人身份证号错误描述" data-name="designerIdCodeDesc"></k-grid-column>
		<k-grid-column data-header="投资经理身份证号错误描述" data-name="managerIdCodeDesc"></k-grid-column>
		<k-grid-column data-header="业务联络人姓名错误描述" data-name="contactNameDesc"></k-grid-column>
		<k-grid-column data-header="业务联络人座机错误描述" data-name="contactTelphoneDesc"></k-grid-column>
		<k-grid-column data-header="业务联络人手机错误描述" data-name="contactMobileDesc"></k-grid-column>
		<k-grid-column data-header="业务联络人邮箱错误描述" data-name="contactEmailDesc"></k-grid-column>
		<k-grid-column data-header="产品募集方式错误描述" data-name="typeCollectDesc"></k-grid-column>
		<k-grid-column data-header="产品收益类型错误描述" data-name="prodRetrunTypeDesc"></k-grid-column>
		<k-grid-column data-header="产品期限错误描述" data-name="prodTermDesc"></k-grid-column>
		<k-grid-column data-header="是否金融同业专属错误描述" data-name="fiancialExclusiveDesc"></k-grid-column>
		<k-grid-column data-header="资金投向地区错误描述" data-name="invertRegionDesc"></k-grid-column>
		<k-grid-column data-header="产品投资国家或地区错误描述" data-name="invertCountryDesc"></k-grid-column>
		<k-grid-column data-header="理财业务服务模式错误描述" data-name="serviceModeDesc"></k-grid-column>
		<k-grid-column data-header="产品运作模式错误描述" data-name="operationModeDesc"></k-grid-column>
		<k-grid-column data-header="是否设置最短持有期限错误描述" data-name="minHoldPeriodDesc"></k-grid-column>
		<k-grid-column data-header="最短持有期限错误描述" data-name="minHoldDayDesc"></k-grid-column>
		<k-grid-column data-header="最短持有期后是否自由赎回错误描述" data-name="optionRedemptPeriodDesc"></k-grid-column>
		<k-grid-column data-header="是否现金管理类错误描述" data-name="cashManagerDesc"></k-grid-column>
		<k-grid-column data-header="产品资产配置方式错误描述" data-name="assetAcMethodDesc"></k-grid-column>
		<k-grid-column data-header="产品管理模式错误描述" data-name="prodManaModeDesc"></k-grid-column>
		<k-grid-column data-header="实际管理人名称错误描述" data-name="acManaNameDesc"></k-grid-column>
		<k-grid-column data-header="产品定价方式错误描述" data-name="priceMethodDesc"></k-grid-column>
		<k-grid-column data-header="产品投资性质错误描述" data-name="investTypeDesc"></k-grid-column>
		<k-grid-column data-header="合作模式错误描述" data-name="cooperateModeDesc"></k-grid-column>
		<k-grid-column data-header="合作机构名称错误描述" data-name="cooperatorDesc"></k-grid-column>
		<k-grid-column data-header="投资资产种类及比例错误描述" data-name="investTypeRatioDesc"></k-grid-column>
		<k-grid-column data-header="业绩比较基准错误描述" data-name="prodBenchmarkDesc"></k-grid-column>
		<k-grid-column data-header="产品销售区域错误描述" data-name="prodSalesRegionDesc"></k-grid-column>
		<k-grid-column data-header="募集币种错误描述" data-name="fundCurDesc"></k-grid-column>
		<k-grid-column data-header="兑付本金币种错误描述" data-name="principalCurDesc"></k-grid-column>
		<k-grid-column data-header="兑付收益币种错误描述" data-name="incomeCurDesc"></k-grid-column>
		<k-grid-column data-header="起点销售金额错误描述" data-name="investThresholdDesc"></k-grid-column>
		<k-grid-column data-header="计划募集金额" data-name="planFundAmtDesc"></k-grid-column>
		<k-grid-column data-header="募集起始日期" data-name="startDateEarliestDesc"></k-grid-column>
		<k-grid-column data-header="募集起始日期" data-name="startDateLatestDesc"></k-grid-column>
		<k-grid-column data-header="投资本金到帐日错误描述" data-name="principalDueDateDesc"></k-grid-column>
		<k-grid-column data-header="投资收益到账日错误描述" data-name="incomeDueDateDesc"></k-grid-column>
		<k-grid-column data-header="销售手续费率" data-name="salesCommissionRateDesc"></k-grid-column>
		<k-grid-column data-header="投资管理费率" data-name="manageFeeRateDesc"></k-grid-column>
		<k-grid-column data-header="境内托管机构名称错误描述" data-name="dcCdNameDesc"></k-grid-column>
		<k-grid-column data-header="境内托管机构代码错误描述" data-name="dcCdIdentCodeDesc"></k-grid-column>
		<k-grid-column data-header="境外托管机构国别错误描述" data-name="seasCdNationDesc"></k-grid-column>
		<k-grid-column data-header="境外托管机构名称错误描述" data-name="seasCdNameDesc"></k-grid-column>
		<k-grid-column data-header="托管费率" data-name="cdFeeRateDesc"></k-grid-column>
		<k-grid-column data-header="产品风险等级错误描述" data-name="riskLevelDesc"></k-grid-column>
		<k-grid-column data-header="发行机构提前终止权标识错误描述" data-name="earlyTnOptionDesc"></k-grid-column>
		<k-grid-column data-header="客户赎回权标识错误描述" data-name="investRdmOptionDesc"></k-grid-column>
		<k-grid-column data-header="产品增信标识错误描述" data-name="prodCrtEnhanceDesc"></k-grid-column>
		<k-grid-column data-header="产品增信机构类型错误描述" data-name="crtInsTypeDesc"></k-grid-column>
		<k-grid-column data-header="产品增信形式错误描述" data-name="prodCrtMethodDesc"></k-grid-column>
		<k-grid-column data-header="备注错误描述" data-name="detailsDesc"></k-grid-column>
		<k-grid-column data-header="登记流水号" data-name="registerSerno"></k-grid-column>
		<k-grid-column data-header="导入日期" data-name="impDate"></k-grid-column>
		<k-grid-column data-header="报告主文件错误描述" data-name="mainDocDesc"></k-grid-column>
		<k-grid-column data-header="理财产品可信性评估报告错误描述" data-name="feasyAssReportDesc"></k-grid-column>
		<k-grid-column data-header="内部审核文件错误描述" data-name="interAuditDocDesc"></k-grid-column>
		<k-grid-column data-header="对理财产品投资管理人,托管人,投资顾问等相关方的尽职调查错误描述" data-name="dueDiligencrDocDesc"></k-grid-column>
		<k-grid-column data-header="对理财产品投资管理人,托管人,投资顾问等相关方签署的法律文件错误描述" data-name="legalDocSifnedDesc"></k-grid-column>
		<k-grid-column data-header="理财产品销售文件错误描述" data-name="prodSaleDocDesc"></k-grid-column>
		<k-grid-column data-header="理财产品说明书错误描述" data-name="prodSpecifiDesc"></k-grid-column>
		<k-grid-column data-header="理财产品宣传材料错误描述" data-name="prodMarkDocDesc"></k-grid-column>
		<k-grid-column data-header="其他材料错误描述" data-name="otherDocDesc"></k-grid-column>
		<k-grid-column data-header="产品风险等级错误描述" data-name="riskRateDesc"></k-grid-column>
		<k-grid-column data-header="新老产品标识" data-name="newProdDesc"></k-grid-column>
        <template slot="operate" slot-scope="scope">
          <k-btn class="md-info md-just-icon md-simple" data-descript="修改产品报告登记错误信息" data-functype="POPUP" data-size="mini"
            data-target="editProdRgFlInfoErrPopup">
            <md-icon>edit</md-icon>
          </k-btn>
          <k-btn class="md-danger md-just-icon md-simple" data-functype="SUBMIT" data-action="ProdRgFlInfoErr.deleteProdRgFlInfoErr" data-size="mini"
               data-type="danger" data-target="{lowHeadModel}Grid" :data-confirm="true" data-descript="删除产品报告登记错误信息">
          	<md-icon>close</md-icon>
    	  </k-btn>
        </template>
      </k-grid>
    </div>

	<!--    添加产品报告登记错误信息弹出框   -->
	<k-popup ref="addProdRgFlInfoErrPopup" data-title="新增">
    	<k-form ref="addProdRgFlInfoErrForm" :data-col="2">
			<k-form-item label="产品名称错误描述">
	        	<k-field-text v-model="formData.prodNameDesc"/>
	     	</k-form-item>
			<k-form-item label="行内标识码错误描述">
	        	<k-field-text v-model="formData.identCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="产品品牌错误描述">
	        	<k-field-text v-model="formData.prodBrandDesc"/>
	     	</k-form-item>
			<k-form-item label="产品期次错误描述">
	        	<k-field-text v-model="formData.prodTermNoDesc"/>
	     	</k-form-item>
			<k-form-item label="发行机构代码错误描述">
	        	<k-field-text v-model="formData.bankCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="产品审批人身份证号错误描述">
	        	<k-field-text v-model="formData.approverIdCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="产品设计人身份证号错误描述">
	        	<k-field-text v-model="formData.designerIdCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="投资经理身份证号错误描述">
	        	<k-field-text v-model="formData.managerIdCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="业务联络人姓名错误描述">
	        	<k-field-text v-model="formData.contactNameDesc"/>
	     	</k-form-item>
			<k-form-item label="业务联络人座机错误描述">
	        	<k-field-text v-model="formData.contactTelphoneDesc"/>
	     	</k-form-item>
			<k-form-item label="业务联络人手机错误描述">
	        	<k-field-text v-model="formData.contactMobileDesc"/>
	     	</k-form-item>
			<k-form-item label="业务联络人邮箱错误描述">
	        	<k-field-text v-model="formData.contactEmailDesc"/>
	     	</k-form-item>
			<k-form-item label="产品募集方式错误描述">
	        	<k-field-text v-model="formData.typeCollectDesc"/>
	     	</k-form-item>
			<k-form-item label="产品收益类型错误描述">
	        	<k-field-text v-model="formData.prodRetrunTypeDesc"/>
	     	</k-form-item>
			<k-form-item label="产品期限错误描述">
	        	<k-field-text v-model="formData.prodTermDesc"/>
	     	</k-form-item>
			<k-form-item label="是否金融同业专属错误描述">
	        	<k-field-text v-model="formData.fiancialExclusiveDesc"/>
	     	</k-form-item>
			<k-form-item label="资金投向地区错误描述">
	        	<k-field-text v-model="formData.invertRegionDesc"/>
	     	</k-form-item>
			<k-form-item label="产品投资国家或地区错误描述">
	        	<k-field-text v-model="formData.invertCountryDesc"/>
	     	</k-form-item>
			<k-form-item label="理财业务服务模式错误描述">
	        	<k-field-text v-model="formData.serviceModeDesc"/>
	     	</k-form-item>
			<k-form-item label="产品运作模式错误描述">
	        	<k-field-text v-model="formData.operationModeDesc"/>
	     	</k-form-item>
			<k-form-item label="是否设置最短持有期限错误描述">
	        	<k-field-text v-model="formData.minHoldPeriodDesc"/>
	     	</k-form-item>
			<k-form-item label="最短持有期限错误描述">
	        	<k-field-text v-model="formData.minHoldDayDesc"/>
	     	</k-form-item>
			<k-form-item label="最短持有期后是否自由赎回错误描述">
	        	<k-field-text v-model="formData.optionRedemptPeriodDesc"/>
	     	</k-form-item>
			<k-form-item label="是否现金管理类错误描述">
	        	<k-field-text v-model="formData.cashManagerDesc"/>
	     	</k-form-item>
			<k-form-item label="产品资产配置方式错误描述">
	        	<k-field-text v-model="formData.assetAcMethodDesc"/>
	     	</k-form-item>
			<k-form-item label="产品管理模式错误描述">
	        	<k-field-text v-model="formData.prodManaModeDesc"/>
	     	</k-form-item>
			<k-form-item label="实际管理人名称错误描述">
	        	<k-field-text v-model="formData.acManaNameDesc"/>
	     	</k-form-item>
			<k-form-item label="产品定价方式错误描述">
	        	<k-field-text v-model="formData.priceMethodDesc"/>
	     	</k-form-item>
			<k-form-item label="产品投资性质错误描述">
	        	<k-field-text v-model="formData.investTypeDesc"/>
	     	</k-form-item>
			<k-form-item label="合作模式错误描述">
	        	<k-field-text v-model="formData.cooperateModeDesc"/>
	     	</k-form-item>
			<k-form-item label="合作机构名称错误描述">
	        	<k-field-text v-model="formData.cooperatorDesc"/>
	     	</k-form-item>
			<k-form-item label="投资资产种类及比例错误描述">
	        	<k-field-text v-model="formData.investTypeRatioDesc"/>
	     	</k-form-item>
			<k-form-item label="业绩比较基准错误描述">
	        	<k-field-text v-model="formData.prodBenchmarkDesc"/>
	     	</k-form-item>
			<k-form-item label="产品销售区域错误描述">
	        	<k-field-text v-model="formData.prodSalesRegionDesc"/>
	     	</k-form-item>
			<k-form-item label="募集币种错误描述">
	        	<k-field-text v-model="formData.fundCurDesc"/>
	     	</k-form-item>
			<k-form-item label="兑付本金币种错误描述">
	        	<k-field-text v-model="formData.principalCurDesc"/>
	     	</k-form-item>
			<k-form-item label="兑付收益币种错误描述">
	        	<k-field-text v-model="formData.incomeCurDesc"/>
	     	</k-form-item>
			<k-form-item label="起点销售金额错误描述">
	        	<k-field-text v-model="formData.investThresholdDesc"/>
	     	</k-form-item>
			<k-form-item label="计划募集金额">
	        	<k-field-text v-model="formData.planFundAmtDesc"/>
	     	</k-form-item>
			<k-form-item label="募集起始日期">
	        	<k-field-text v-model="formData.startDateEarliestDesc"/>
	     	</k-form-item>
			<k-form-item label="募集起始日期">
	        	<k-field-text v-model="formData.startDateLatestDesc"/>
	     	</k-form-item>
			<k-form-item label="投资本金到帐日错误描述">
	        	<k-field-text v-model="formData.principalDueDateDesc"/>
	     	</k-form-item>
			<k-form-item label="投资收益到账日错误描述">
	        	<k-field-text v-model="formData.incomeDueDateDesc"/>
	     	</k-form-item>
			<k-form-item label="销售手续费率">
	        	<k-field-text v-model="formData.salesCommissionRateDesc"/>
	     	</k-form-item>
			<k-form-item label="投资管理费率">
	        	<k-field-text v-model="formData.manageFeeRateDesc"/>
	     	</k-form-item>
			<k-form-item label="境内托管机构名称错误描述">
	        	<k-field-text v-model="formData.dcCdNameDesc"/>
	     	</k-form-item>
			<k-form-item label="境内托管机构代码错误描述">
	        	<k-field-text v-model="formData.dcCdIdentCodeDesc"/>
	     	</k-form-item>
			<k-form-item label="境外托管机构国别错误描述">
	        	<k-field-text v-model="formData.seasCdNationDesc"/>
	     	</k-form-item>
			<k-form-item label="境外托管机构名称错误描述">
	        	<k-field-text v-model="formData.seasCdNameDesc"/>
	     	</k-form-item>
			<k-form-item label="托管费率">
	        	<k-field-text v-model="formData.cdFeeRateDesc"/>
	     	</k-form-item>
			<k-form-item label="产品风险等级错误描述">
	        	<k-field-text v-model="formData.riskLevelDesc"/>
	     	</k-form-item>
			<k-form-item label="发行机构提前终止权标识错误描述">
	        	<k-field-text v-model="formData.earlyTnOptionDesc"/>
	     	</k-form-item>
			<k-form-item label="客户赎回权标识错误描述">
	        	<k-field-text v-model="formData.investRdmOptionDesc"/>
	     	</k-form-item>
			<k-form-item label="产品增信标识错误描述">
	        	<k-field-text v-model="formData.prodCrtEnhanceDesc"/>
	     	</k-form-item>
			<k-form-item label="产品增信机构类型错误描述">
	        	<k-field-text v-model="formData.crtInsTypeDesc"/>
	     	</k-form-item>
			<k-form-item label="产品增信形式错误描述">
	        	<k-field-text v-model="formData.prodCrtMethodDesc"/>
	     	</k-form-item>
			<k-form-item label="备注错误描述">
	        	<k-field-text v-model="formData.detailsDesc"/>
	     	</k-form-item>
			<k-form-item label="登记流水号">
	        	<k-field-text v-model="formData.registerSerno"/>
	     	</k-form-item>
			<k-form-item label="导入日期">
	        	<k-field-text v-model="formData.impDate"/>
	     	</k-form-item>
			<k-form-item label="报告主文件错误描述">
	        	<k-field-text v-model="formData.mainDocDesc"/>
	     	</k-form-item>
			<k-form-item label="理财产品可信性评估报告错误描述">
	        	<k-field-text v-model="formData.feasyAssReportDesc"/>
	     	</k-form-item>
			<k-form-item label="内部审核文件错误描述">
	        	<k-field-text v-model="formData.interAuditDocDesc"/>
	     	</k-form-item>
			<k-form-item label="对理财产品投资管理人,托管人,投资顾问等相关方的尽职调查错误描述">
	        	<k-field-text v-model="formData.dueDiligencrDocDesc"/>
	     	</k-form-item>
			<k-form-item label="对理财产品投资管理人,托管人,投资顾问等相关方签署的法律文件错误描述">
	        	<k-field-text v-model="formData.legalDocSifnedDesc"/>
	     	</k-form-item>
			<k-form-item label="理财产品销售文件错误描述">
	        	<k-field-text v-model="formData.prodSaleDocDesc"/>
	     	</k-form-item>
			<k-form-item label="理财产品说明书错误描述">
	        	<k-field-text v-model="formData.prodSpecifiDesc"/>
	     	</k-form-item>
			<k-form-item label="理财产品宣传材料错误描述">
	        	<k-field-text v-model="formData.prodMarkDocDesc"/>
	     	</k-form-item>
			<k-form-item label="其他材料错误描述">
	        	<k-field-text v-model="formData.otherDocDesc"/>
	     	</k-form-item>
			<k-form-item label="产品风险等级错误描述">
	        	<k-field-text v-model="formData.riskRateDesc"/>
	     	</k-form-item>
			<k-form-item label="新老产品标识">
	        	<k-field-text v-model="formData.newProdDesc"/>
	     	</k-form-item>

	      	<k-form-footer data-align="center">
		        <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ProdRgFlInfoErr.addProdRgFlInfoErr" data-from="addProdRgFlInfoErrForm"
		               :data-model="formData" data-target="prodRgFlInfoErrGrid">
		          <md-icon md-src="/static/svg/confirm.svg"></md-icon>确定
		        </k-btn>
		        <k-btn class="btn-custom-plain" data-functype="CLOSE">
		          <md-icon md-src="/static/svg/cancel.svg"></md-icon>取消</k-btn>
	      	</k-form-footer>
    	</k-form>
	</k-popup>

	<!--    修改产品报告登记错误信息弹出框   -->
	<k-popup ref="editProdRgFlInfoErrPopup" data-title="修改">
	  <k-form ref="editProdRgFlInfoErrForm" :data-col="2">
		<k-form-item label="产品名称错误描述">
        	<k-field-text v-model="formData.prodNameDesc"/>
     	</k-form-item>
		<k-form-item label="行内标识码错误描述">
        	<k-field-text v-model="formData.identCodeDesc"/>
     	</k-form-item>
		<k-form-item label="产品品牌错误描述">
        	<k-field-text v-model="formData.prodBrandDesc"/>
     	</k-form-item>
		<k-form-item label="产品期次错误描述">
        	<k-field-text v-model="formData.prodTermNoDesc"/>
     	</k-form-item>
		<k-form-item label="发行机构代码错误描述">
        	<k-field-text v-model="formData.bankCodeDesc"/>
     	</k-form-item>
		<k-form-item label="产品审批人身份证号错误描述">
        	<k-field-text v-model="formData.approverIdCodeDesc"/>
     	</k-form-item>
		<k-form-item label="产品设计人身份证号错误描述">
        	<k-field-text v-model="formData.designerIdCodeDesc"/>
     	</k-form-item>
		<k-form-item label="投资经理身份证号错误描述">
        	<k-field-text v-model="formData.managerIdCodeDesc"/>
     	</k-form-item>
		<k-form-item label="业务联络人姓名错误描述">
        	<k-field-text v-model="formData.contactNameDesc"/>
     	</k-form-item>
		<k-form-item label="业务联络人座机错误描述">
        	<k-field-text v-model="formData.contactTelphoneDesc"/>
     	</k-form-item>
		<k-form-item label="业务联络人手机错误描述">
        	<k-field-text v-model="formData.contactMobileDesc"/>
     	</k-form-item>
		<k-form-item label="业务联络人邮箱错误描述">
        	<k-field-text v-model="formData.contactEmailDesc"/>
     	</k-form-item>
		<k-form-item label="产品募集方式错误描述">
        	<k-field-text v-model="formData.typeCollectDesc"/>
     	</k-form-item>
		<k-form-item label="产品收益类型错误描述">
        	<k-field-text v-model="formData.prodRetrunTypeDesc"/>
     	</k-form-item>
		<k-form-item label="产品期限错误描述">
        	<k-field-text v-model="formData.prodTermDesc"/>
     	</k-form-item>
		<k-form-item label="是否金融同业专属错误描述">
        	<k-field-text v-model="formData.fiancialExclusiveDesc"/>
     	</k-form-item>
		<k-form-item label="资金投向地区错误描述">
        	<k-field-text v-model="formData.invertRegionDesc"/>
     	</k-form-item>
		<k-form-item label="产品投资国家或地区错误描述">
        	<k-field-text v-model="formData.invertCountryDesc"/>
     	</k-form-item>
		<k-form-item label="理财业务服务模式错误描述">
        	<k-field-text v-model="formData.serviceModeDesc"/>
     	</k-form-item>
		<k-form-item label="产品运作模式错误描述">
        	<k-field-text v-model="formData.operationModeDesc"/>
     	</k-form-item>
		<k-form-item label="是否设置最短持有期限错误描述">
        	<k-field-text v-model="formData.minHoldPeriodDesc"/>
     	</k-form-item>
		<k-form-item label="最短持有期限错误描述">
        	<k-field-text v-model="formData.minHoldDayDesc"/>
     	</k-form-item>
		<k-form-item label="最短持有期后是否自由赎回错误描述">
        	<k-field-text v-model="formData.optionRedemptPeriodDesc"/>
     	</k-form-item>
		<k-form-item label="是否现金管理类错误描述">
        	<k-field-text v-model="formData.cashManagerDesc"/>
     	</k-form-item>
		<k-form-item label="产品资产配置方式错误描述">
        	<k-field-text v-model="formData.assetAcMethodDesc"/>
     	</k-form-item>
		<k-form-item label="产品管理模式错误描述">
        	<k-field-text v-model="formData.prodManaModeDesc"/>
     	</k-form-item>
		<k-form-item label="实际管理人名称错误描述">
        	<k-field-text v-model="formData.acManaNameDesc"/>
     	</k-form-item>
		<k-form-item label="产品定价方式错误描述">
        	<k-field-text v-model="formData.priceMethodDesc"/>
     	</k-form-item>
		<k-form-item label="产品投资性质错误描述">
        	<k-field-text v-model="formData.investTypeDesc"/>
     	</k-form-item>
		<k-form-item label="合作模式错误描述">
        	<k-field-text v-model="formData.cooperateModeDesc"/>
     	</k-form-item>
		<k-form-item label="合作机构名称错误描述">
        	<k-field-text v-model="formData.cooperatorDesc"/>
     	</k-form-item>
		<k-form-item label="投资资产种类及比例错误描述">
        	<k-field-text v-model="formData.investTypeRatioDesc"/>
     	</k-form-item>
		<k-form-item label="业绩比较基准错误描述">
        	<k-field-text v-model="formData.prodBenchmarkDesc"/>
     	</k-form-item>
		<k-form-item label="产品销售区域错误描述">
        	<k-field-text v-model="formData.prodSalesRegionDesc"/>
     	</k-form-item>
		<k-form-item label="募集币种错误描述">
        	<k-field-text v-model="formData.fundCurDesc"/>
     	</k-form-item>
		<k-form-item label="兑付本金币种错误描述">
        	<k-field-text v-model="formData.principalCurDesc"/>
     	</k-form-item>
		<k-form-item label="兑付收益币种错误描述">
        	<k-field-text v-model="formData.incomeCurDesc"/>
     	</k-form-item>
		<k-form-item label="起点销售金额错误描述">
        	<k-field-text v-model="formData.investThresholdDesc"/>
     	</k-form-item>
		<k-form-item label="计划募集金额">
        	<k-field-text v-model="formData.planFundAmtDesc"/>
     	</k-form-item>
		<k-form-item label="募集起始日期">
        	<k-field-text v-model="formData.startDateEarliestDesc"/>
     	</k-form-item>
		<k-form-item label="募集起始日期">
        	<k-field-text v-model="formData.startDateLatestDesc"/>
     	</k-form-item>
		<k-form-item label="投资本金到帐日错误描述">
        	<k-field-text v-model="formData.principalDueDateDesc"/>
     	</k-form-item>
		<k-form-item label="投资收益到账日错误描述">
        	<k-field-text v-model="formData.incomeDueDateDesc"/>
     	</k-form-item>
		<k-form-item label="销售手续费率">
        	<k-field-text v-model="formData.salesCommissionRateDesc"/>
     	</k-form-item>
		<k-form-item label="投资管理费率">
        	<k-field-text v-model="formData.manageFeeRateDesc"/>
     	</k-form-item>
		<k-form-item label="境内托管机构名称错误描述">
        	<k-field-text v-model="formData.dcCdNameDesc"/>
     	</k-form-item>
		<k-form-item label="境内托管机构代码错误描述">
        	<k-field-text v-model="formData.dcCdIdentCodeDesc"/>
     	</k-form-item>
		<k-form-item label="境外托管机构国别错误描述">
        	<k-field-text v-model="formData.seasCdNationDesc"/>
     	</k-form-item>
		<k-form-item label="境外托管机构名称错误描述">
        	<k-field-text v-model="formData.seasCdNameDesc"/>
     	</k-form-item>
		<k-form-item label="托管费率">
        	<k-field-text v-model="formData.cdFeeRateDesc"/>
     	</k-form-item>
		<k-form-item label="产品风险等级错误描述">
        	<k-field-text v-model="formData.riskLevelDesc"/>
     	</k-form-item>
		<k-form-item label="发行机构提前终止权标识错误描述">
        	<k-field-text v-model="formData.earlyTnOptionDesc"/>
     	</k-form-item>
		<k-form-item label="客户赎回权标识错误描述">
        	<k-field-text v-model="formData.investRdmOptionDesc"/>
     	</k-form-item>
		<k-form-item label="产品增信标识错误描述">
        	<k-field-text v-model="formData.prodCrtEnhanceDesc"/>
     	</k-form-item>
		<k-form-item label="产品增信机构类型错误描述">
        	<k-field-text v-model="formData.crtInsTypeDesc"/>
     	</k-form-item>
		<k-form-item label="产品增信形式错误描述">
        	<k-field-text v-model="formData.prodCrtMethodDesc"/>
     	</k-form-item>
		<k-form-item label="备注错误描述">
        	<k-field-text v-model="formData.detailsDesc"/>
     	</k-form-item>
		<k-form-item label="登记流水号">
        	<k-field-text v-model="formData.registerSerno"/>
     	</k-form-item>
		<k-form-item label="导入日期">
        	<k-field-text v-model="formData.impDate"/>
     	</k-form-item>
		<k-form-item label="报告主文件错误描述">
        	<k-field-text v-model="formData.mainDocDesc"/>
     	</k-form-item>
		<k-form-item label="理财产品可信性评估报告错误描述">
        	<k-field-text v-model="formData.feasyAssReportDesc"/>
     	</k-form-item>
		<k-form-item label="内部审核文件错误描述">
        	<k-field-text v-model="formData.interAuditDocDesc"/>
     	</k-form-item>
		<k-form-item label="对理财产品投资管理人,托管人,投资顾问等相关方的尽职调查错误描述">
        	<k-field-text v-model="formData.dueDiligencrDocDesc"/>
     	</k-form-item>
		<k-form-item label="对理财产品投资管理人,托管人,投资顾问等相关方签署的法律文件错误描述">
        	<k-field-text v-model="formData.legalDocSifnedDesc"/>
     	</k-form-item>
		<k-form-item label="理财产品销售文件错误描述">
        	<k-field-text v-model="formData.prodSaleDocDesc"/>
     	</k-form-item>
		<k-form-item label="理财产品说明书错误描述">
        	<k-field-text v-model="formData.prodSpecifiDesc"/>
     	</k-form-item>
		<k-form-item label="理财产品宣传材料错误描述">
        	<k-field-text v-model="formData.prodMarkDocDesc"/>
     	</k-form-item>
		<k-form-item label="其他材料错误描述">
        	<k-field-text v-model="formData.otherDocDesc"/>
     	</k-form-item>
		<k-form-item label="产品风险等级错误描述">
        	<k-field-text v-model="formData.riskRateDesc"/>
     	</k-form-item>
		<k-form-item label="新老产品标识">
        	<k-field-text v-model="formData.newProdDesc"/>
     	</k-form-item>
	    <k-form-footer data-align="center">
	      <k-btn class="btn-custom-primary" data-functype="SUBMIT" data-action="ProdRgFlInfoErr.updateProdRgFlInfoErr" data-from="editProdRgFlInfoErrForm"
	        :data-model="formData" data-target="prodRgFlInfoErrGrid">
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
    name: "ProdRgFlInfoErr",
    data() {
      return {
        formData: {},
        selectRowData: {},
        searchParam:{}, //查询条件
        BreathDay:[],
      };
    },
    methods: {
      selectRow(row, column, event) {
        this.selectRowData = Object.assign({}, row)
        this.formData = Object.assign({}, row)
      }
    },
    watch: {
      // 查询导入日期
      BreathDay() {
        console.log(this.BreathDay);
        this.$set(this.searchParam, 'startDate', this.BreathDay == null ? '' : this.BreathDay[0]);
        this.$set(this.searchParam, 'endDate', this.BreathDay == null ? '' : this.BreathDay[1]);
      },
    }
  };
</script>
