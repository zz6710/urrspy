package com.kayak.rpt.zz.manage.service;

import com.kayak.rpt.zz.manage.model.*;
import com.kayak.rpt.zz.manage.util.CheckAssetInfo;
import com.kayak.rpt.zz.manage.util.CheckDataParams;
import com.kayak.rpt.zz.manage.util.CheckDataUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CheckDataForInfoService {

	/**
	 * 合法性校验--申报登记
	 * @param whiteregex
	 * @param whitereForCode
	 * @param prodRegistFilingInfo
	 * @return
	 * @throws Exception
	 */
	public String prodRegistFilingInfoCheckInfo(String whiteregex,String whitereForCode, ProdRegistFilingInfo prodRegistFilingInfo) throws Exception{
		StringBuffer stringErr = new StringBuffer();
		stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getProdName(),"产品名称",200,"1"));
		stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getIdentCode(),"行内标识码",100,"1"));

		stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getProdAprvNm(),"产品审批人姓名",200,"1"));
		String getApproverIdCode = prodRegistFilingInfo.getApproverIdCode();
		stringErr.append(CheckDataUtils.checkStringLength(getApproverIdCode,"产品审批人身份证号",30,"1"));

		if(StringUtils.isNotBlank(getApproverIdCode)){
			Pattern p9=Pattern.compile("^\\d{6}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$");
			if(!p9.matcher(getApproverIdCode).matches() ){
				stringErr.append("产品审批人身份证号要素格式不对。<br/>");
			}
		}

		stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getProdDsnNm(),"产品设计人姓名",200,"1"));
		stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getDesignerIdCode(),"产品设计人身份证号",30,"1"));
		if(StringUtils.isNotBlank(prodRegistFilingInfo.getDesignerIdCode())){
			Pattern p9=Pattern.compile("^\\d{6}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$");
			if(!p9.matcher(prodRegistFilingInfo.getDesignerIdCode()).matches() ){
				stringErr.append("产品设计人身份证号要素格式不对。<br/>");
			}
		}
		stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getInvMngNm(),"投资经理姓名",200,"1"));
		stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getManagerIdCode(),"投资经理身份证号",30,"1"));
		if(StringUtils.isNotBlank(prodRegistFilingInfo.getManagerIdCode())){
			Pattern p9=Pattern.compile("^\\d{6}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$");
			if(!p9.matcher(prodRegistFilingInfo.getManagerIdCode()).matches() ){
				stringErr.append("投资经理身份证号要素格式不对。<br/>");
			}
		}
		stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getContactName(),"业务联络人姓名",32,"1"));

		String getContactTelphone = prodRegistFilingInfo.getContactTelphone();
		stringErr.append(CheckDataUtils.checkStringLength(getContactTelphone,"业务联络人座机",30,"1"));
		if(StringUtils.isNotBlank(getContactTelphone)){
			Pattern p9=Pattern.compile("^[\\-\\d]{1,30}");
			if(!p9.matcher(getContactTelphone).matches() ){
				stringErr.append("业务联络人座机要素格式不对。正确的格式：-或数字。<br/>");
			}
		}
		stringErr.append(CheckDataUtils.checkMoney(prodRegistFilingInfo.getContactMobile(),"业务联络人手机","^[0-9]{1,11}","n..11","0","0") );

		stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getContactEmail(),"业务联络人邮箱",50,"1"));
		if(StringUtils.isNotBlank(prodRegistFilingInfo.getContactEmail())){
			String regex = "^[(a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
			Pattern p = Pattern.compile(regex);
			Matcher m=p.matcher(prodRegistFilingInfo.getContactEmail());
			if(!m.matches()){
				stringErr.append("业务联络人邮箱格式错误,正确格式为：xx@xx.xx。<br/>");
			}
		}

		stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getAcManaName(),"实际管理人名称",120,"0"));
		stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getProdBrand(),"产品品牌",120,"0"));

		stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getDetails(),"备注",256,"0"));

		String getInvestTypeRatio = prodRegistFilingInfo.getInvestTypeRatio();
		stringErr.append(CheckDataUtils.checkStringLength(getInvestTypeRatio,"投资资产种类及比例",300,"1"));
		if(StringUtils.isNotBlank(getInvestTypeRatio)){
			String regex = "^(100(\\.00)?%:[^%:;\\-\\d]+(;100(\\.00)?%:[^%:;\\-\\d]+)*|" +
					"(100(\\.00)?%|\\d{1,3}(?:\\.\\d{1,2})?%)(-(100(\\.00)?%|\\d{1,3}(?:\\.\\d{1,2})?%))?:" +
					"[^%:;\\-\\d]+(;(100(\\.00)?%|\\d{1,3}(?:\\.\\d{1,2})?%)(-(100(\\.00)?%|\\d{1,3}(?:\\.\\d{1,2})?%))?:" +
					"[^%:;\\-\\d]+)*;?)$";
			Pattern p = Pattern.compile(regex);
			Matcher m=p.matcher(getInvestTypeRatio);
			if(!m.matches()){
				stringErr.append("投资资产种类及比例要素格式不对。正确的格式：数字%：文字；数字%-数字%：文字（其中，百分号、冒号、分号、连字号均应为英文标点，文字和数字部分均不得含有英文百分号、冒号、分号、连字号）。其中，数字格式为n..（5，2），且需大于等于0、小于等于100。<br/>");
			}
		}

		//1次返回，只判斷長度后
//		if(StringUtils.isNotBlank(stringErr)){
//			return stringErr.toString();
//		}
		//发行机构代码
		String bank_code = prodRegistFilingInfo.getBankCode();
		String bank_code_s = CheckDataParams.bankCode;

		if (StringUtils.isBlank(bank_code)){
			stringErr.append("发行机构代码要素不可为空。<br/>");
		}else {
			Pattern p=Pattern.compile("^([Z]{1}\\d{5})");
			Matcher m=p.matcher(bank_code.trim());
			boolean flagBankCode = m.matches();
			if (!bank_code_s.equals(bank_code)){
				stringErr.append("发行机构代码必须与银行代码相同。<br/>");
			}
			if(!flagBankCode){
				stringErr.append("发行机构代码要素格式不对。正确的格式：6位英文或数字。<br/>");
			}
		}


		//产品收益类型
		if(StringUtils.isBlank(prodRegistFilingInfo.getProdRetrunType())){
			stringErr.append("产品收益类型要素不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_prod_revenue_type", prodRegistFilingInfo.getProdRetrunType());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("产品收益类型："+errDesc);
			}
		}

		//产品期限
		if(StringUtils.isBlank(prodRegistFilingInfo.getProdTerm())){
			stringErr.append("产品期限要素不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_t8_prod_term", prodRegistFilingInfo.getProdTerm());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("产品期限："+errDesc);
			}
		}

		//是否金融同业专属
		if(StringUtils.isBlank(prodRegistFilingInfo.getFiancialExclusive())){
			stringErr.append("是否金融同业专属要素不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_isTrue", prodRegistFilingInfo.getFiancialExclusive());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("是否金融同业专属："+errDesc);
			}
		}

		//资金投向地区
		String invertRegion = prodRegistFilingInfo.getInvertRegion();
		if(StringUtils.isBlank(invertRegion)){
			stringErr.append("资金投向地区不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_invest_region", invertRegion);
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("资金投向地区："+errDesc);
			}
		}
		//产品投资国家或地区
		String invertCountry = prodRegistFilingInfo.getInvertCountry();
		if("02,03".contains(invertRegion) || "02 境外,03 境内和境外".contains(invertRegion) ){
			if(StringUtils.isBlank(invertCountry)){
				stringErr.append("当资金投向地区为境外时，产品投资国家或地区要素不可为空。<br/>");
			}else if(invertRegion.contains("02") && invertCountry.contains("CHN")) {
				stringErr.append("当资金投向地区为境外时，产品投资国家或地区不能填写中国。<br/>");
			}
		}else if("01".contains(invertRegion) || "01 境内".contains(invertRegion)){
			if(StringUtils.isNotBlank(invertCountry)){
				stringErr.append("当资金投向地区为境内时，产品投资国家或地区要素必须为空。<br/>");
			}
		}
		//理财业务服务模式
		String serviceMode = prodRegistFilingInfo.getServiceMode();
		if("01,03".contains(invertRegion) || "01 境内,03 境内和境外".contains(invertRegion) ){
			if(StringUtils.isBlank(serviceMode)){
				stringErr.append("当资金投向地区为境内和境外时，理财业务服务模式要素不可为空。<br/>");
			}else{
				String errDesc = CheckDataUtils.checkDictValue("subm_t8_srv_mode", serviceMode);
				if(StringUtils.isNotBlank(errDesc)){
					stringErr.append("理财业务服务模式："+errDesc);
				}
			}
		}else if("02".contains(invertRegion) || "02 境外".contains(invertRegion)){
			if(StringUtils.isNotBlank(serviceMode)){
				stringErr.append("当资金投向地区为境外时，理财业务服务模式要素必须为空。<br/>");
			}
		}

		//境内托管机构代码
		String dcCdIdentCode = prodRegistFilingInfo.getDcCdIdentCode();
		stringErr.append(CheckDataUtils.checkStringLength(dcCdIdentCode,"境内托管机构代码",12,"0"));
		if("02".contains(invertRegion) || "02 境外".contains(invertRegion)){
			if(StringUtils.isNotBlank(dcCdIdentCode)){
				stringErr.append("当资金投向地区为境外时，境内托管机构代码要素必须为空。<br/>");
			}
		}
		//境内托管机构名称
		String dcCdName = prodRegistFilingInfo.getDcCdName();
		stringErr.append(CheckDataUtils.checkStringLength(dcCdName,"境内托管机构名称",200,"0"));
		if("01,03".contains(invertRegion) || "01 境内,03 境内和境外".contains(invertRegion) ){
			if(StringUtils.isBlank(dcCdName)){
				stringErr.append("当资金投向地区为境内时，境内托管机构名称（公募私募）要素不可为空。<br/>");
			}
		}else if("02".contains(invertRegion) || "02 境外".contains(invertRegion)){
			if(StringUtils.isNotBlank(dcCdName)){
				stringErr.append("当资金投向地区为境外时，境内托管机构名称（公募私募）要素必须为空。<br/>");
			}
		}

		//境外托管机构国别
		String seasCdNation = prodRegistFilingInfo.getSeasCdNation();
		if("02,03".contains(invertRegion) || "02 境外,03 境内和境外".contains(invertRegion) ){
			if(StringUtils.isBlank(seasCdNation)){
				stringErr.append("当资金投向地区为境外时，境外托管机构国别要素不可为空。<br/>");
			}else if(invertRegion.contains("02") && seasCdNation.contains("CHN")) {
				stringErr.append("当资金投向地区为境外时，境外托管机构国别不能填写中国。<br/>");
			}
		}else if("01".contains(invertRegion) || "01 境内".contains(invertRegion)){
			if(StringUtils.isNotBlank(seasCdNation)){
				stringErr.append("当资金投向地区为境内时，境外托管机构国别要素必须为空。<br/>");
			}
		}
		//境外托管机构名称
		String seasCdName = prodRegistFilingInfo.getSeasCdName();
		if("02,03".contains(invertRegion) || "02 境外,03 境内和境外".contains(invertRegion) ){
			if(StringUtils.isBlank(seasCdName)){
				stringErr.append("当资金投向地区为境外时，境外托管机构名称要素不可为空。<br/>");
			}
		}else if("01".contains(invertRegion) || "01 境内".contains(invertRegion)){
			if(StringUtils.isNotBlank(seasCdName)){
				stringErr.append("当资金投向地区为境内时，境外托管机构名称要素必须为空。<br/>");
			}
		}


		//产品运作模式
		String operationMode = prodRegistFilingInfo.getOperationMode();
		if(StringUtils.isBlank(operationMode)){
			stringErr.append("产品运作模式要素不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_t8_product_operation_mode", operationMode);
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("产品运作模式："+errDesc);
			}
			if("03,04".contains(operationMode) || "03 开放式净值型,04 开放式非净值型".contains(operationMode) ){
				if(StringUtils.isBlank(prodRegistFilingInfo.getCashManager())){
					stringErr.append("当产品运作模式为开放式净值型时，是否现金管理类要素不可为空。<br/>");
				}else{
					errDesc = CheckDataUtils.checkDictValue("subm_isTrue", prodRegistFilingInfo.getCashManager());
					if(StringUtils.isNotBlank(errDesc)){
						stringErr.append("是否现金管理类："+errDesc);
					}
				}

				if(StringUtils.isBlank(prodRegistFilingInfo.getMinHoldPeriod())){
					stringErr.append("当产品运作模式为开放式净值型时，是否设置最短持有期限要素不可为空。<br/>");
				}else{
					errDesc = CheckDataUtils.checkDictValue("subm_isTrue", prodRegistFilingInfo.getMinHoldPeriod());
					if(StringUtils.isNotBlank(errDesc)){
						stringErr.append("是否设置最短持有期限："+errDesc);
					}
				}

			}else if("01,02".contains(operationMode) || "01 封闭式净值型,02 封闭式非净值型".contains(operationMode)){
				if(StringUtils.isNotBlank(prodRegistFilingInfo.getCashManager())){
					stringErr.append("当产品运作模式为封闭式净值型时，是否现金管理类要素必须为空。<br/>");
				}

				if(StringUtils.isNotBlank(prodRegistFilingInfo.getMinHoldPeriod())){
					stringErr.append("当产品运作模式为封闭式净值型时，是否设置最短持有期限必须为空。<br/>");
				}
			}

		}

		//产品募集方式
		if(StringUtils.isBlank(prodRegistFilingInfo.getTypeCollect())){
			stringErr.append("产品募集方式要素不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_t8_raise_type", prodRegistFilingInfo.getTypeCollect());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("产品募集方式："+errDesc);
			}
		}

		//产品资产配置方式
		if(StringUtils.isBlank(prodRegistFilingInfo.getAssetAcMethod())){
			stringErr.append("产品资产配置方式要素不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_t8_asset_maping", prodRegistFilingInfo.getAssetAcMethod());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("产品资产配置方式："+errDesc);
			}
		}
		//产品管理模式
		if(StringUtils.isBlank(prodRegistFilingInfo.getProdManaMode())){
			stringErr.append("产品管理模式要素不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_prod_manage_mode", prodRegistFilingInfo.getProdManaMode());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("产品管理模式："+errDesc);
			}
		}
		//产品定价方式
		if(StringUtils.isBlank(prodRegistFilingInfo.getPriceMethod())){
			stringErr.append("产品定价方式要素不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_prod_price_way", prodRegistFilingInfo.getPriceMethod());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("产品定价方式："+errDesc);
			}
		}
		//产品投资性质
		if(StringUtils.isBlank(prodRegistFilingInfo.getInvestType())){
			stringErr.append("产品投资性质要素不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_prod_invest_nature", prodRegistFilingInfo.getInvestType());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("产品投资性质："+errDesc);
			}
		}

		//起点销售金额

		stringErr.append(CheckDataUtils.checkMoney(prodRegistFilingInfo.getInvestThreshold(),"起点销售金额","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","1") );
		String am1 = prodRegistFilingInfo.getInvestThreshold().trim();
		if(StringUtils.isNotBlank(am1)&am1.compareTo("0") == 0){
			stringErr.append("起点销售金额必须大于0。<br/>");
		}

		//产品销售区域
		String prodSalesRegion = prodRegistFilingInfo.getProdSalesRegion();
		if(StringUtils.isBlank(prodSalesRegion)){
			stringErr.append("产品销售区域要素不可为空。<br/>");
		}else{
			String errDesc = CheckDataUtils.checkMultipleDict("subm_prod_sale_area", prodSalesRegion,1);
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("产品销售区域："+errDesc);
			}
		}

		//募集币种
		if(StringUtils.isBlank(prodRegistFilingInfo.getFundCur())){
			stringErr.append("募集币种要素不可为空。<br/>");
		}else{
			String errDesc = CheckDataUtils.checkMultipleDict("subm_cur_type", prodRegistFilingInfo.getFundCur(),0);
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("募集币种："+errDesc);
			}
		}
		//兑付本金币种
		if(StringUtils.isBlank(prodRegistFilingInfo.getPrincipalCur())){
			stringErr.append("兑付本金币种要素不可为空。<br/>");
		}else{
			String errDesc = CheckDataUtils.checkMultipleDict("subm_cur_type", prodRegistFilingInfo.getPrincipalCur(),0);
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("兑付本金币种："+errDesc);
			}
		}
		//兑付收益币种
		if(StringUtils.isBlank(prodRegistFilingInfo.getIncomeCur())){
			stringErr.append("兑付收益币种要素不可为空。<br/>");
		}else{
			String errDesc = CheckDataUtils.checkMultipleDict("subm_cur_type", prodRegistFilingInfo.getIncomeCur(),0);
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("兑付收益币种："+errDesc);
			}
		}
		//销售手续费率%
		stringErr.append(CheckDataUtils.checkMoney(prodRegistFilingInfo.getSalesCommissionRate(),"销售手续费率%","^(\\d{1,3}(\\.\\d{1,5})?)","n（8,5）","1","1") );

		//托管费率%
		stringErr.append(CheckDataUtils.checkMoney(prodRegistFilingInfo.getCdFeeRate(),"托管费率%","^(\\d{1,3}(\\.\\d{1,5})?)","n（8,5）","1","1") );

		//募集起始日期（从)
		if(StringUtils.isBlank(prodRegistFilingInfo.getStartDateEarliest())){
			stringErr.append("募集起始日期（从）要素不可为空。<br/>");
		}else {
			String am = prodRegistFilingInfo.getStartDateEarliest().trim();
			if(am.codePoints().count() == 8){
				Pattern p=Pattern.compile("^(\\d{8})");
				Matcher m=p.matcher(am);
				if(!m.matches()){
					stringErr.append("募集起始日期（从）必须为日期格式（YYYYMMDD）。<br/>");
				}else if(!CheckDataUtils.isLegalDate(8,am,"yyyyMMdd")){
					stringErr.append("募集起始日期（从）必须为正确日期。<br/>");
				}
			}else{
				Pattern p=Pattern.compile("^(\\d{4}\\-\\d{2}\\-\\d{2})");
				Matcher m=p.matcher(am);
				if(!m.matches()){
					stringErr.append("募集起始日期（从）必须为日期格式（YYYY-MM-DD）。<br/>");
				}else if(!CheckDataUtils.isLegalDate(10,am,"yyyy-MM-dd")){
					stringErr.append("募集起始日期（从）必须为正确日期。<br/>");
				}
			}
		}
		//募集起始日期（到)
		if(StringUtils.isBlank(prodRegistFilingInfo.getStartDateLatest())){
			stringErr.append("募集起始日期（到）要素不可为空。<br/>");
		}else {
			String am = prodRegistFilingInfo.getStartDateLatest().trim();
			if(am.codePoints().count() == 8){
				Pattern p=Pattern.compile("^(\\d{8})");
				Matcher m=p.matcher(am);
				if(!m.matches()){
					stringErr.append("募集起始日期（到）必须为日期格式（YYYYMMDD）。<br/>");
				}else if(!CheckDataUtils.isLegalDate(8,am,"yyyyMMdd")){
					stringErr.append("募集起始日期（到）必须为正确日期。<br/>");
				}
			}else{
				Pattern p=Pattern.compile("^(\\d{4}\\-\\d{2}\\-\\d{2})");
				Matcher m=p.matcher(am);
				if(!m.matches()){
					stringErr.append("募集起始日期（到）必须为日期格式（YYYY-MM-DD）。<br/>");
				}else if(!CheckDataUtils.isLegalDate(10,am,"yyyy-MM-dd")){
					stringErr.append("募集起始日期（到）必须为正确日期。<br/>");

				}
			}
			if(CheckDataUtils.compareTo(am,prodRegistFilingInfo.getStartDateEarliest()) < 0){
				stringErr.append("募集起始日期（到）必须大于等于募集起始日期（从）。<br/>");
			}
		}
		//计划募集金额（元）
		stringErr.append(CheckDataUtils.checkMoney(prodRegistFilingInfo.getPlanFundAmt(),"计划募集金额（元）","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","1") );
		String am0 = prodRegistFilingInfo.getPlanFundAmt().trim();
		if(StringUtils.isNotBlank(am0) && am0.compareTo("0") == 0){
			stringErr.append("计划募集金额（元）必须大于0。<br/>");
		}
		//投资者风险偏好
		if(StringUtils.isBlank(prodRegistFilingInfo.getRiskLevel())){
			stringErr.append("投资者风险偏好要素不可为空。<br/>");
		}
		/*else {
			String errDesc = CheckDataUtils.checkDictValue("subm_investor_risk_preference", prodRegistFilingInfo.getRiskLevel());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("投资者风险偏好："+errDesc);
			}
		}*/
		//产品风险等级
		if(StringUtils.isBlank(prodRegistFilingInfo.getRiskRate())){
			stringErr.append("产品风险等级要素不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_prod_risk_level", prodRegistFilingInfo.getRiskRate());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("产品风险等级："+errDesc);
			}
		}
		//发行机构提前终止权标识
		if(StringUtils.isBlank(prodRegistFilingInfo.getEarlyTnOption())){
			stringErr.append("发行机构提前终止权标识要素不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_prod_credit_logo", prodRegistFilingInfo.getEarlyTnOption());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("发行机构提前终止权标识："+errDesc);
			}
		}
		//客户赎回权标识
		if(StringUtils.isBlank(prodRegistFilingInfo.getInvestRdmOption())){
			stringErr.append("客户赎回权标识要素不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_prod_credit_logo", prodRegistFilingInfo.getInvestRdmOption());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("客户赎回权标识："+errDesc);
			}
		}
		//产品期次
		stringErr.append(CheckDataUtils.checkMoney(prodRegistFilingInfo.getProdTermNo(),"产品期次","^(\\d{1,6})","n..6","1","0") );

		//投资管理费率%
		stringErr.append(CheckDataUtils.checkMoney(prodRegistFilingInfo.getManageFeeRate(),"投资管理费率%","^(\\d{1,3}(\\.\\d{1,5})?)","n（8,5）","1","1") );

		//合作模式
		if(StringUtils.isBlank(prodRegistFilingInfo.getCooperateMode())){
			stringErr.append("合作模式要素不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_cooperation_mode", prodRegistFilingInfo.getCooperateMode());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("合作模式："+errDesc);
			}

			if(prodRegistFilingInfo.getCooperateMode().contains("01")){
				if(StringUtils.isNotBlank(prodRegistFilingInfo.getCooperator())){
					stringErr.append("当合作模式为独立运作时，合作机构名称要素必须为空。<br/>");
				}
			}else if (!prodRegistFilingInfo.getCooperateMode().contains("01")){
				if(StringUtils.isBlank(prodRegistFilingInfo.getCooperator())){
					stringErr.append("当合作模式不为独立运作时，合作机构名称要素不可为空。<br/>");
				}
			}
			stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getCooperator(),"合作机构名称",120,"0"));
		}

		//投资本金到账日
		if(StringUtils.isBlank(prodRegistFilingInfo.getPrincipalDueDate())){
			stringErr.append("投资本金到账日要素不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_invest_income_arrive_date", prodRegistFilingInfo.getPrincipalDueDate());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("投资本金到账日："+errDesc);
			}
		}
		//投资收益到账日
		if(StringUtils.isBlank(prodRegistFilingInfo.getIncomeDueDate())){
			stringErr.append("投资收益到账日要素不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_invest_income_arrive_date", prodRegistFilingInfo.getIncomeDueDate());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("投资收益到账日："+errDesc);
			}
		}
		//产品增信标识
		if(StringUtils.isBlank(prodRegistFilingInfo.getProdCrtEnhance())){
			stringErr.append("产品增信标识要素不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_prod_credit_logo", prodRegistFilingInfo.getProdCrtEnhance());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("产品增信标识："+errDesc);
			}
		}

		//产品增信机构类型
		if(prodRegistFilingInfo.getProdCrtEnhance().equals("01 有")){
			if(StringUtils.isBlank(prodRegistFilingInfo.getCrtInsType())){
				stringErr.append("当产品增信标识为有时，产品增信机构类型要素不可为空。<br/>");
			}
		}else{
			if(StringUtils.isNotBlank(prodRegistFilingInfo.getCrtInsType())){
				stringErr.append("当产品增信标识为无时，产品增信机构类型要素必须为空。<br/>");
			}
		}


		//产品增信形式
		if(prodRegistFilingInfo.getProdCrtEnhance().equals("01 有")){
			if(StringUtils.isBlank(prodRegistFilingInfo.getProdCrtMethod())){
				stringErr.append("当产品增信标识为有时，产品增信形式要素不可为空。<br/>");
			}else {
				String errDesc = CheckDataUtils.checkDictValue("subm_prod_credit_form", prodRegistFilingInfo.getProdCrtMethod());
				if(StringUtils.isNotBlank(errDesc)){
					stringErr.append("产品增信形式："+errDesc);
				}
			}
		}else{
			if(StringUtils.isNotBlank(prodRegistFilingInfo.getProdCrtMethod())){
				stringErr.append("当产品增信标识为无时，产品增信形式要素必须为空。<br/>");
			}
		}
		//2次返回
//		if(StringUtils.isNotBlank(stringErr)){
//			return stringErr.toString();
//		}

		//白名单字符
		Pattern whiterpattern = null;
		if(!"".equals(whiteregex)){
			whiterpattern = Pattern.compile(whiteregex);
		}
		if(!"".equals(whiteregex)){
			if(!whiterpattern.matcher(prodRegistFilingInfo.getProdName()).matches()){stringErr.append("产品名称必须填写白名单中的文字。<br/>");}
			if(!bank_code.equals("")&&!whiterpattern.matcher(bank_code).matches()) stringErr.append("登记银行代码必须填写白名单中的文字。<br/>");
			if(!whiterpattern.matcher(prodRegistFilingInfo.getApproverIdCode()).matches()){stringErr.append("产品审批人身份证号必须填写白名单中的文字。<br/>");}
			if(!whiterpattern.matcher(prodRegistFilingInfo.getDesignerIdCode()).matches()){stringErr.append("产品设计人身份证号必须填写白名单中的文字。<br/>");}
			if(!whiterpattern.matcher(prodRegistFilingInfo.getManagerIdCode()).matches()){stringErr.append("投资经理身份证号必须填写白名单中的文字。<br/>");}
			if(StringUtils.isNotBlank(prodRegistFilingInfo.getContactName()) && !whiterpattern.matcher(prodRegistFilingInfo.getContactName()).matches()){stringErr.append("业务联络人姓名必须填写白名单中的文字。<br/>");}
			if(StringUtils.isNotBlank(prodRegistFilingInfo.getAcManaName()) && !whiterpattern.matcher(prodRegistFilingInfo.getAcManaName()).matches()){stringErr.append("实际管理人名称必须填写白名单中的文字。<br/>");}
			if(StringUtils.isNotBlank(prodRegistFilingInfo.getDcCdIdentCode()) && !whiterpattern.matcher(prodRegistFilingInfo.getDcCdIdentCode()).matches()){stringErr.append("境内托管机构代码必须填写白名单中的文字。<br/>");}
			if(StringUtils.isNotBlank(prodRegistFilingInfo.getDcCdName()) && !whiterpattern.matcher(prodRegistFilingInfo.getDcCdName()).matches()){stringErr.append("境外托管机构名称必须填写白名单中的文字。<br/>");}
			if(StringUtils.isNotBlank(prodRegistFilingInfo.getProdBrand()) && !whiterpattern.matcher(prodRegistFilingInfo.getProdBrand()).matches()){stringErr.append("产品品牌必须填写白名单中的文字。<br/>");}
			if(StringUtils.isNotBlank(prodRegistFilingInfo.getCooperator()) && !whiterpattern.matcher(prodRegistFilingInfo.getCooperator()).matches()){stringErr.append("合作机构名称必须填写白名单中的文字。<br/>");}
			if(StringUtils.isNotBlank(prodRegistFilingInfo.getInvestTypeRatio()) && !whiterpattern.matcher(prodRegistFilingInfo.getInvestTypeRatio()).matches()){stringErr.append("投资资产种类及比例必须填写白名单中的文字。<br/>");}
			if(StringUtils.isNotBlank(prodRegistFilingInfo.getDetails()) && !whiterpattern.matcher(prodRegistFilingInfo.getDetails()).matches()){stringErr.append("备注必须填写白名单中的文字。<br/>");}
		}
		//行内标识码只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。
		Pattern whiterpattern1 = null;
		if(!"".equals(whitereForCode)){
			whiterpattern1 = Pattern.compile(whitereForCode);
			if(!whiterpattern1.matcher(prodRegistFilingInfo.getIdentCode()).matches()){stringErr.append("行内标识码只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。<br/>");}
		}

		return stringErr.toString();
	}

	/**
	 * 合法性校验--发行登记
	 * @param whiteregex
	 * @param whitereForCode
	 * @param prodIssuanceRegistInfo
	 * @return
	 * @throws Exception
	 */
	public String prodIssuanceRegistInfoCheckInfo(String whiteregex,String whitereForCode, ProdIssuanceRegistInfo prodIssuanceRegistInfo) throws Exception{
		StringBuffer stringErr = new StringBuffer();
		if(StringUtils.isBlank(prodIssuanceRegistInfo.getProdCode())){
			stringErr.append("产品登记编码要素不可为空。<br/>");
		}else if(prodIssuanceRegistInfo.getProdCode().length() > 15){
			stringErr.append("产品登记编码要素过长：" + prodIssuanceRegistInfo.getProdCode());
		}else{
			String regex = "^[A-Za-z0-9]{14,15}$";
			Pattern whiterpattern =Pattern.compile(regex);
			if(!whiterpattern.matcher(prodIssuanceRegistInfo.getProdCode()).matches()){stringErr.append("产品登记编码要素格式不对。正确的格式：14或15位英文或数字。<br/>");}
		}
		stringErr.append(CheckDataUtils.checkStringLength(prodIssuanceRegistInfo.getProdIdentCode(),"理财产品代码",100,"1"));
		stringErr.append(CheckDataUtils.checkStringLength(prodIssuanceRegistInfo.getDetailsPerRate(),"业绩比较基准说明",400,"0"));
		stringErr.append(CheckDataUtils.checkStringLength(prodIssuanceRegistInfo.getDisorderOpenPeriod(),"无规律开放说明",256,"0"));
		stringErr.append(CheckDataUtils.checkStringLength(prodIssuanceRegistInfo.getDetailsBusiOpPeriod(),"开放期业务说明",256,"0"));
		String AverageOpenNo =  CheckDataUtils.checkMoney(prodIssuanceRegistInfo.getAverageOpenNo(),"平均开放次数（年化）","^(\\d{1,3}(\\.\\d{1,2})?)","n（5,2）","0","1");
		if(StringUtils.isNotBlank(prodIssuanceRegistInfo.getAverageOpenNo())&&(prodIssuanceRegistInfo.getAverageOpenNo().compareTo("0") <= 0 || prodIssuanceRegistInfo.getAverageOpenNo().compareTo("366") > 0)){
			stringErr.append("平均开放次数（年化）必须大于0且不能超过366。<br/>");
		}else{
			stringErr.append(AverageOpenNo);
		}
		stringErr.append(CheckDataUtils.checkMoney(prodIssuanceRegistInfo.getOtherOpenPeriod(),"其他规律开放周期(天)","^(\\d{1,4})","n..4","0","1"));
		stringErr.append(CheckDataUtils.checkMoney(prodIssuanceRegistInfo.getRegularOpenPeriodDay(),"定期开放周期（天）","^(\\d{1,4})","n..4","0","1"));

		//发行机构代码
		String bank_code = prodIssuanceRegistInfo.getBankCode();
		String bank_code_s = CheckDataParams.bankCode;
		if (StringUtils.isBlank(bank_code)){
			stringErr.append("发行机构代码要素不可为空。<br/>");
		}else {
			Pattern p=Pattern.compile("^([Z]{1}\\d{5})");
			Matcher m=p.matcher(bank_code.trim());
			boolean flagBankCode = m.matches();
			if (!bank_code_s.equals(bank_code)){
				stringErr.append("发行机构代码必须与银行代码相同。<br/>");
			}else if(!flagBankCode){
				stringErr.append("发行机构代码要素格式不对。正确的格式：6位英文或数字。<br/>");
			}
		}


		//管理方式
		if(StringUtils.isBlank(prodIssuanceRegistInfo.getManagementMethod())){
			stringErr.append("管理方式要素不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_managementMethod", prodIssuanceRegistInfo.getManagementMethod());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("管理方式要素："+errDesc);
			}
		}
		//是否为结构化（分级）产品
		if(StringUtils.isBlank(prodIssuanceRegistInfo.getStructuredProd())){
			stringErr.append("是否为结构化（分级）产品不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_isTrue", prodIssuanceRegistInfo.getStructuredProd());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("是否为结构化（分级）产品："+errDesc);
			}
			Double clsfSto = prodIssuanceRegistInfo.getClsfSto();

			if(prodIssuanceRegistInfo.getStructuredProd().contains("01") && clsfSto == null ){
				stringErr.append("当是否为结构化（分级）产品为是时，分级比例要素不可为空。<br/>");
			}else if(prodIssuanceRegistInfo.getStructuredProd().contains("02") && clsfSto != null ){
				stringErr.append("当是否为结构化（分级）产品为否时，分级比例要素必须为空。<br/>");
			}
			if(prodIssuanceRegistInfo.getStructuredProd().contains("01")&& clsfSto != null){
				stringErr.append(CheckDataUtils.checkMoney(String.valueOf(clsfSto),"分级比例","^(\\d{1,3}(\\.\\d{1,5})?)","n（8,5）","0","0") );
			}

		}
//		//管理方式
//		if(StringUtils.isBlank(prodIssuanceRegistInfo.getManagementMethod())){
//			stringErr.append("管理方式要素不可为空。<br/>");
//		}else {
//			String errDesc = CheckDataUtils.checkDictValue("subm_managementMethod", prodIssuanceRegistInfo.getManagementMethod());
//			if(StringUtils.isNotBlank(errDesc)){
//				stringErr.append("管理方式要素："+errDesc);
//			}
//		}
		//开放模式
		if(!StringUtils.isBlank(prodIssuanceRegistInfo.getOpeningMode())){
			//01有规律开放
			if("01".equals(prodIssuanceRegistInfo.getOpeningMode())){
				if(StringUtils.isBlank(prodIssuanceRegistInfo.getRegularOpenPeriod())){
					stringErr.append("当“开放模式”选择 “01 有规律开放”时，规律开放周期不可为空。<br/>");
				}
				if(StringUtils.isNotBlank(prodIssuanceRegistInfo.getDisorderOpenPeriod())){
					stringErr.append("当“开放模式”选择 “01 有规律开放”时，无规律开放说明要素必须为空。<br/>");
				}
			}
			//02无规律开放
			if("02".equals(prodIssuanceRegistInfo.getOpeningMode())){
				if(!StringUtils.isBlank(prodIssuanceRegistInfo.getRegularOpenPeriod())){
					stringErr.append("当“开放模式”选择 “02 无规律开放”时，规律开放周期必须为空。<br/>");
				}
				if(StringUtils.isBlank(prodIssuanceRegistInfo.getDisorderOpenPeriod())){
					stringErr.append("当“开放模式”选择 “02 无规律开放”时，无规律开放说明要素不可为空。<br/>");
				}
			}

			String errDesc = CheckDataUtils.checkDictValue("subm_open_mod", prodIssuanceRegistInfo.getOpeningMode());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("开放模式："+errDesc);
			}
		}
		//规律开放周期
		String regularOpenPeriod = prodIssuanceRegistInfo.getRegularOpenPeriod();
		if(!StringUtils.isBlank(regularOpenPeriod)){
			String errDesc = CheckDataUtils.checkDictValue("subm_t8_open_calendar", regularOpenPeriod);
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("规律开放周期："+errDesc);
			}
			if(regularOpenPeriod.contains("99")){
				if(StringUtils.isBlank(prodIssuanceRegistInfo.getOtherOpenPeriod())){
					stringErr.append("当“开放模式”选择“01 有规律开放”、“规律开放周期”选择“99 其他规律开放周期”时，其他规律开放周期(天)要素不可为空。<br/>");
				}
			}else{
				if(StringUtils.isNotBlank(prodIssuanceRegistInfo.getOtherOpenPeriod())){
					stringErr.append("当“开放模式”选择“01 有规律开放”、“规律开放周期”选择“99 其他规律开放周期”以外的要素时，其他规律开放周期(天)要素必须为空。<br/>");
				}
			}
		}

		if(StringUtils.isNotBlank(prodIssuanceRegistInfo.getLowLimitPerRate()) && StringUtils.isNotBlank(prodIssuanceRegistInfo.getUpLimitPerRate()) &&
				prodIssuanceRegistInfo.getLowLimitPerRate().compareTo(prodIssuanceRegistInfo.getUpLimitPerRate()) > 0 ){
			stringErr.append("业绩比较基准上限必须大于等于业绩比较基准下限。<br/>");
		}


		//节假日是否开放
		if(!StringUtils.isBlank(prodIssuanceRegistInfo.getHolidayOpenType())){
			String errDesc = CheckDataUtils.checkDictValue("subm_isTrue", prodIssuanceRegistInfo.getHolidayOpenType());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("节假日是否开放："+errDesc);
			}
		}
		//开放期业务
		if(!StringUtils.isBlank(prodIssuanceRegistInfo.getBusiOpenPeriod())){
			String errDesc = CheckDataUtils.checkDictValue("subm_t8_open_control", prodIssuanceRegistInfo.getBusiOpenPeriod());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("开放期业务："+errDesc);
			}
		}

		//募集起始日期
		if(StringUtils.isBlank(prodIssuanceRegistInfo.getSubscriptionStartDate())){
			stringErr.append("募集起始日期要素不可为空。<br/>");
		}else {
			String am = prodIssuanceRegistInfo.getSubscriptionStartDate().trim();
			if(am.codePoints().count() == 8){
				Pattern p=Pattern.compile("^(\\d{8})");
				Matcher m=p.matcher(am);
				if(!m.matches()){
					stringErr.append("募集起始日期必须为日期格式（YYYYMMDD）。<br/>");
				}else if(!CheckDataUtils.isLegalDate(8,am,"yyyyMMdd")){
					stringErr.append("募集起始日期必须为正确日期。<br/>");
				}
			}else{
				Pattern p=Pattern.compile("^(\\d{4}\\-\\d{2}\\-\\d{2})");
				Matcher m=p.matcher(am);
				if(!m.matches()){
					stringErr.append("募集起始日期必须为日期格式（YYYY-MM-DD）。<br/>");
				}else if(!CheckDataUtils.isLegalDate(10,am,"yyyy-MM-dd")){
					stringErr.append("募集起始日期必须为正确日期。<br/>");
				}
			}
		}
		//募集结束日期
		if(StringUtils.isBlank(prodIssuanceRegistInfo.getSubscriptionEndDate())){
			stringErr.append("募集结束日期要素不可为空。<br/>");
		}else {
			String am = prodIssuanceRegistInfo.getSubscriptionEndDate().trim();
			if(CheckDataUtils.compareTo(am,prodIssuanceRegistInfo.getSubscriptionStartDate()) < 0){
				stringErr.append("募集结束日期必须大于等于募集起始日期。<br/>");
			}else if(am.codePoints().count() == 8){
				Pattern p=Pattern.compile("^(\\d{8})");
				Matcher m=p.matcher(am);
				if(!m.matches()){
					stringErr.append("募集结束日期必须为日期格式（YYYYMMDD）。<br/>");
				}else if(!CheckDataUtils.isLegalDate(8,am,"yyyyMMdd")){
					stringErr.append("募集结束日期必须为正确日期。<br/>");
				}
			}else{
				Pattern p=Pattern.compile("^(\\d{4}\\-\\d{2}\\-\\d{2})");
				Matcher m=p.matcher(am);
				if(!m.matches()){
					stringErr.append("募集结束日期必须为日期格式（YYYY-MM-DD）。<br/>");
				}else if(!CheckDataUtils.isLegalDate(10,am,"yyyy-MM-dd")){
					stringErr.append("募集结束日期必须为正确日期。<br/>");
				}
			}
		}
		//产品起始日期
		if(StringUtils.isBlank(prodIssuanceRegistInfo.getProdValueDate())){
			stringErr.append("产品起始日期要素不可为空。<br/>");
		}else {
			String am = prodIssuanceRegistInfo.getProdValueDate().trim();
			if(CheckDataUtils.compareTo(am,prodIssuanceRegistInfo.getSubscriptionEndDate()) < 0){
				stringErr.append("产品起始日期必须大于等于募集结束日期。<br/>");
			}else if(am.codePoints().count() == 8){
				Pattern p=Pattern.compile("^(\\d{8})");
				Matcher m=p.matcher(am);
				if(!m.matches()){
					stringErr.append("产品起始日期必须为日期格式（YYYYMMDD）。<br/>");
				}else if(!CheckDataUtils.isLegalDate(8,am,"yyyyMMdd")){
					stringErr.append("产品起始日期必须为正确日期。<br/>");
				}
			}else{
				Pattern p=Pattern.compile("^(\\d{4}\\-\\d{2}\\-\\d{2})");
				Matcher m=p.matcher(am);
				if(!m.matches()){
					stringErr.append("产品起始日期必须为日期格式（YYYY-MM-DD）。<br/>");
				}else if(!CheckDataUtils.isLegalDate(10,am,"yyyy-MM-dd")){
					stringErr.append("产品起始日期必须为正确日期。<br/>");
				}
			}
		}
		//产品终止日期
		if(StringUtils.isBlank(prodIssuanceRegistInfo.getProdMaturityDate())){
			stringErr.append("产品终止日期要素不可为空。<br/>");
		}else {
			String am = prodIssuanceRegistInfo.getProdMaturityDate().trim();
			if(CheckDataUtils.compareTo(am,prodIssuanceRegistInfo.getProdValueDate()) <= 0){
				stringErr.append("产品终止日期必须大于产品起始日期。<br/>");
			}else if(am.codePoints().count() == 8){
				Pattern p=Pattern.compile("^(\\d{8})");
				Matcher m=p.matcher(am);
				if(!m.matches()){
					stringErr.append("产品终止日期必须为日期格式（YYYYMMDD）。<br/>");
				}else if(!CheckDataUtils.isLegalDate(8,am,"yyyyMMdd")){
					stringErr.append("产品终止日期必须为正确日期。<br/>");
				}
			}else{
				Pattern p=Pattern.compile("^(\\d{4}\\-\\d{2}\\-\\d{2})");
				Matcher m=p.matcher(am);
				if(!m.matches()){
					stringErr.append("产品终止日期必须为日期格式（YYYY-MM-DD）。<br/>");
				}else if(!CheckDataUtils.isLegalDate(10,am,"yyyy-MM-dd")){
					stringErr.append("产品终止日期必须为正确日期。<br/>");
				}
			}
		}
		//首次开放周期起始日
		String firstOpenDay = prodIssuanceRegistInfo.getFirstOpenDay();
		if (StringUtils.isNotBlank(firstOpenDay)) {
			stringErr.append(CheckDataUtils.checkDate(firstOpenDay, "首次开放周期起始日"));
		}

		//白名单字符
		Pattern whiterpattern = null;
		if(!"".equals(whiteregex)){
			whiterpattern = Pattern.compile(whiteregex);
		}
		if(!"".equals(whiteregex)){
			if(!whiterpattern.matcher(prodIssuanceRegistInfo.getProdIdentCode()).matches()){stringErr.append("理财产品代码必须填写白名单中的文字。<br/>");}
			if(!bank_code.equals("")&&!whiterpattern.matcher(bank_code).matches()) stringErr.append("登记银行代码必须填写白名单中的文字。<br/>");
			if(StringUtils.isNotBlank(prodIssuanceRegistInfo.getDetailsPerRate()) && !whiterpattern.matcher(prodIssuanceRegistInfo.getDetailsPerRate()).matches()){stringErr.append("业绩比较基准说明必须填写白名单中的文字。<br/>");}
			if(StringUtils.isNotBlank(prodIssuanceRegistInfo.getDisorderOpenPeriod()) && !whiterpattern.matcher(prodIssuanceRegistInfo.getDisorderOpenPeriod()).matches()){stringErr.append("无规律开放说明必须填写白名单中的文字。<br/>");}
			if(StringUtils.isNotBlank(prodIssuanceRegistInfo.getDetailsBusiOpPeriod()) && !whiterpattern.matcher(prodIssuanceRegistInfo.getDetailsBusiOpPeriod()).matches()){stringErr.append("开放期业务说明必须填写白名单中的文字。<br/>");}
		}

		return stringErr.toString();
	}

	/**
	 * 合法性校验--募集期总量登记
	 * @param whiteregex
	 * @param whitereForCode
	 * @param initialSubRegistInfo
	 * @return
	 * @throws Exception
	 */
	public String initialSubRegistInfoCheckInfo(String whiteregex,String whitereForCode, InitialSubRegistInfo initialSubRegistInfo) throws Exception{
		StringBuffer stringErr = new StringBuffer();
		if(StringUtils.isBlank(initialSubRegistInfo.getProdCode())){
			stringErr.append("产品登记编码要素不可为空。<br/>");
		}else if(initialSubRegistInfo.getProdCode().length() > 15){
			stringErr.append("产品登记编码要素过长：" + initialSubRegistInfo.getProdCode().substring(0,14)+"<br/>");
		}else{
			String regex = "^[A-Za-z0-9]{14,15}$";
			Pattern whiterpattern =Pattern.compile(regex);
			if(!whiterpattern.matcher(initialSubRegistInfo.getProdCode()).matches()){stringErr.append("产品登记编码要素格式不对。正确的格式：14或15位英文或数字。<br/>");}
		}
		if(StringUtils.isBlank(initialSubRegistInfo.getFndTrstActNbr())){
			stringErr.append("资金托管账号要素不可为空。<br/>");
		}else if(initialSubRegistInfo.getFndTrstActNbr().codePoints().count() > 60){
			stringErr.append("资金托管账号过长：" + initialSubRegistInfo.getFndTrstActNbr()+"<br/>");
		}else{
			String regex = "^(?!-)(?!.*-$)[A-Za-z0-9-]{3,60}$";
			Pattern whiterpattern =Pattern.compile(regex);
			if(!whiterpattern.matcher(initialSubRegistInfo.getFndTrstActNbr()).matches()){stringErr.append("资金托管账号要素格式不对。正确的格式：大小写字母或数字、以及“-”。<br/>");}
		}
		if(StringUtils.isBlank(initialSubRegistInfo.getFndTrstAct())){
			stringErr.append("资金托管账户要素不可为空。<br/>");
		}else if(initialSubRegistInfo.getFndTrstAct().codePoints().count() > 200){
			stringErr.append("资金托管账户过长：" + initialSubRegistInfo.getFndTrstAct()+"<br/>");
		}

		//个人投资者总数
		stringErr.append(CheckDataUtils.checkMoney(initialSubRegistInfo.getNumberIndivInvest(),"个人投资者总数","^(\\d{1,9}(\\.0)?)","n..9","1","1") );
		//法人投资者总数
		stringErr.append(CheckDataUtils.checkMoney(initialSubRegistInfo.getNumberCorporInvest(),"法人投资者总数","^(\\d{1,9}(\\.0)?)","n..9","1","1") );
		//非法人投资者总数
		stringErr.append(CheckDataUtils.checkMoney(initialSubRegistInfo.getNumberUcorInvest(),"非法人投资者总数","^(\\d{1,9}(\\.0)?)","n..9","1","1") );
		//实际募集金额（元）
		stringErr.append(CheckDataUtils.checkMoney(initialSubRegistInfo.getActualSubscribedAmt(),"实际募集金额（元）","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","1") );

		//募集总份额
		stringErr.append(CheckDataUtils.checkMoney(initialSubRegistInfo.getSubscribedVol(),"募集总份额","^(\\d{1,13}(\\.\\d{1,5})?)","n（18,5）","1","1") );

		//发行机构代码
		String bank_code = initialSubRegistInfo.getBankCode();
		String bank_code_s = CheckDataParams.bankCode;
		if (StringUtils.isBlank(bank_code)){
			stringErr.append("发行机构代码要素不可为空。<br/>");
		}else {
			Pattern p=Pattern.compile("^([Z]{1}\\d{5})");
			Matcher m=p.matcher(bank_code.trim());
			boolean flagBankCode = m.matches();
			if (!bank_code_s.equals(bank_code)){
				stringErr.append("发行机构代码必须与银行代码相同。<br/>");
			}else if(!flagBankCode){
				stringErr.append("发行机构代码要素格式不对。正确的格式：6位英文或数字。<br/>");
			}
		}

		//是否有其他机构代销
		if(StringUtils.isBlank(initialSubRegistInfo.getOtherDistributAgents())){
			stringErr.append("是否有其他机构代销要素不可为空。<br/>");
		}else {
			String errDesc = CheckDataUtils.checkDictValue("subm_isTrue", initialSubRegistInfo.getOtherDistributAgents());
			if(StringUtils.isNotBlank(errDesc)){
				stringErr.append("是否有其他机构代销："+errDesc);
			}
		}

		//代销总金额
		if(StringUtils.isNotBlank(initialSubRegistInfo.getOtherDistributAgents())){
			if(initialSubRegistInfo.getOtherDistributAgents().equals("01 是")){
				if(StringUtils.isBlank(initialSubRegistInfo.getAmtOtherDbAgents())){
					stringErr.append("当是否有其他机构代销为是时，代销总金额要素不可为空。<br/>");
				}else{
					if(CheckDataUtils.compareTo(initialSubRegistInfo.getActualSubscribedAmt(),initialSubRegistInfo.getAmtOtherDbAgents()) < 0 ){
						stringErr.append("代销总金额必须小于等于实际募集金额。<br/>");
					}else{
						stringErr.append(CheckDataUtils.checkMoney(initialSubRegistInfo.getAmtOtherDbAgents(),"代销总金额","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","1") );
					}
				}
			}else if(initialSubRegistInfo.getOtherDistributAgents().equals("02 否")){
				if(StringUtils.isNotBlank(initialSubRegistInfo.getAmtOtherDbAgents())){
					stringErr.append("当是否有其他机构代销为否时，代销总金额要素必须为空。<br/>");
				}
			}
		}
		//产品销售区域及募集金额
		String zonClcAmt = initialSubRegistInfo.getZonClcAmt();
		if(StringUtils.isNotBlank(zonClcAmt)){
			List<String> valueList = Arrays.asList(zonClcAmt.split(";"));
			for(String value : valueList){
				List<String> list = Arrays.asList(value.split(","));
				if(list.size() != 2){
					stringErr.append("产品销售区域及募集金额必须组合报送，不可只报送一个字段。<br/>");
				}else{
					String errDesc = CheckDataUtils.checkDictValue("subm_prod_sale_area", list.get(0));
					if(StringUtils.isNotBlank(errDesc)){
						stringErr.append("产品销售区域："+errDesc);
					}
					stringErr.append(CheckDataUtils.checkMoney(list.get(1),"区域募集金额","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","1") );
				}
			}
		}

		//认购币种
		String prodCcy = initialSubRegistInfo.getProdCcy();
		if(StringUtils.isNotBlank(prodCcy)){
			List<String> valueList = Arrays.asList(prodCcy.split(";"));
			for(String value : valueList){
				List<String> list = Arrays.asList(value.split(","));
				if(list.size() != 3){
					stringErr.append("认购币种、认购金额、折算人民币金额必须组合报送，字段不可缺失。<br/>");
				}else{
					if(list.get(0).contains("CNY") && new BigDecimal(list.get(1)).compareTo(new BigDecimal(list.get(2))) != 0 ){
						stringErr.append("当认购币种选择“CNY （人民币）”时，折算人民币金额与认购金额必须相等。<br/>");
					}
					stringErr.append(CheckDataUtils.checkMoney(list.get(1),"认购金额","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","1") );
					stringErr.append(CheckDataUtils.checkMoney(list.get(2),"折算人民币金额","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","1") );
				}
			}
		}

		//白名单字符
		Pattern whiterpattern = null;
		if(!"".equals(whiteregex)){
			whiterpattern = Pattern.compile(whiteregex);
		}
		if(!"".equals(whiteregex)){
			if(StringUtils.isNotBlank(initialSubRegistInfo.getFndTrstAct()) && !whiterpattern.matcher(initialSubRegistInfo.getFndTrstAct()).matches()){stringErr.append("资金托管账户必须填写白名单中的文字。<br/>");}
			if(!bank_code.equals("")&&!whiterpattern.matcher(bank_code).matches()) stringErr.append("登记银行代码必须填写白名单中的文字。<br/>");
			if(StringUtils.isNotBlank(initialSubRegistInfo.getDetails()) && !whiterpattern.matcher(initialSubRegistInfo.getDetails()).matches()){stringErr.append("备注必须填写白名单中的文字。<br/>");}
		}
		return stringErr.toString();
	}

	/**
	 * 合法性校验--存续期总量登记
	 * @param whiteregex
	 * @param whitereForCode
	 * @param subseqSubscrRegistInfo
	 * @return
	 * @throws Exception
	 */
	public String subseqSubscrRegistInfoCheckInfo(String whiteregex,String whitereForCode, SubseqSubscrRegistInfo subseqSubscrRegistInfo) throws Exception{
		StringBuffer stringErr = new StringBuffer();
		//发行机构代码
		String bank_code = subseqSubscrRegistInfo.getBankCode();
		String bank_code_s = CheckDataParams.bankCode;
		if (StringUtils.isBlank(bank_code)){
			stringErr.append("发行机构代码要素不可为空。<br/>");
		}else {
			Pattern p=Pattern.compile("^([Z]{1}\\d{5})");
			Matcher m=p.matcher(bank_code.trim());
			boolean flagBankCode = m.matches();
			if (!bank_code_s.equals(bank_code)){
				stringErr.append("发行机构代码必须与银行代码相同。<br/>");
			}else if(!flagBankCode){
				stringErr.append("发行机构代码要素格式不对。正确的格式：6位英文或数字。<br/>");
			}
		}
		if(StringUtils.isBlank(subseqSubscrRegistInfo.getProdCode())){
			stringErr.append("产品登记编码要素不可为空。<br/>");
		}else if(subseqSubscrRegistInfo.getProdCode().length() > 15){
			stringErr.append("产品登记编码要素过长：" + subseqSubscrRegistInfo.getProdCode()+"<br/>");
		}else{
			String regex = "^[A-Za-z0-9]{14,15}$";
			Pattern whiterpattern =Pattern.compile(regex);
			if(!whiterpattern.matcher(subseqSubscrRegistInfo.getProdCode()).matches()){stringErr.append("产品登记编码要素格式不对。正确的格式：14或15位英文或数字。<br/>");}
		}


		if(Strings.isBlank(subseqSubscrRegistInfo.getCcyAndPchRdm())){
			stringErr.append("币种和申购兑付信息不可为空。<br/>");
		}else{
			String[] numinfo = subseqSubscrRegistInfo.getCcyAndPchRdm().split(",");
			if(numinfo.length!=4){
				stringErr.append("币种和申购兑付信息格式不正确。<br/>");
			}else{
				stringErr.append(CheckDataUtils.checkMoney(numinfo[1],"该币种累计申购金额（元）","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","1") );
				stringErr.append(CheckDataUtils.checkMoney(numinfo[2],"该币种累计兑付金额（元）","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","1") );
				stringErr.append(CheckDataUtils.checkMoney(numinfo[3],"该币种累计兑付收益金额（元）","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","0") );
			}
		}
		if (StringUtils.isBlank(subseqSubscrRegistInfo.getNavDt())) {
			stringErr.append("净值日期要素不可为空。<br/>");
		} else {
			stringErr.append(CheckDataUtils.checkDate(subseqSubscrRegistInfo.getNavDt(), "净值日期"));
		}
		if (StringUtils.isBlank(subseqSubscrRegistInfo.getBusinessStartDate())) {
			stringErr.append("业务起始日要素不可为空。<br/>");
		} else {
			stringErr.append(CheckDataUtils.checkDate(subseqSubscrRegistInfo.getBusinessStartDate(), "业务起始日"));
			stringErr.append(CheckDataUtils.checkDateAfer(subseqSubscrRegistInfo.getBusinessStartDate(),"业务起始日期"));
		}

		if (StringUtils.isBlank(subseqSubscrRegistInfo.getBusinessStartDate())) {
			stringErr.append("业务结束日要素不可为空。<br/>");
		} else {
			stringErr.append(CheckDataUtils.checkDate(subseqSubscrRegistInfo.getBusinessStartDate(), "业务结束日"));
		}

		//初始净值
		stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getInitialNav(),"初始净值","^(\\d{1,5}(\\.\\d{1,5})?)","n（10,5）","0","1") );

		//产品净值
		stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getNav(),"产品净值","^(\\d{1,5}(\\.\\d{1,5})?)","n（10,5）","0","1") );
		//累计净值
		stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getAggregateNav(),"累计净值","^(\\d{1,5}(\\.\\d{1,5})?)","n（10,5）","0","1") );

		//折算人民币初始净值
		stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getConvertInitialNav(),"折算人民币初始净值","^(\\d{1,5}(\\.\\d{1,5})?)","n（10,5）","0","1") );

		//折算人民币净值
		stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getConvertRmbNav(),"折算人民币净值","^(\\d{1,5}(\\.\\d{1,5})?)","n（10,5）","0","1") );

		//折算人民币累计净值
		stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getConvertRmbAggNav(),"折算人民币累计净值","^(\\d{1,5}(\\.\\d{1,5})?)","n（10,5）","0","1") );

		if(StringUtils.isNotBlank(subseqSubscrRegistInfo.getNavCur()) && subseqSubscrRegistInfo.getNavCur().equals("CNY")){
			if("".equals(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getInitialNav(),"初始净值","^(\\d{1,5}(\\.\\d{1,5})?)","n（10,5）","0","1"))){
				if("".equals(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getConvertInitialNav(),"折算人民币初始净值","^(\\d{1,5}(\\.\\d{1,5})?)","n（10,5）","0","1"))){
					if(new BigDecimal(subseqSubscrRegistInfo.getConvertInitialNav()).compareTo(new BigDecimal(subseqSubscrRegistInfo.getInitialNav())) != 0){
						stringErr.append("当初始净值的币种为人民币(CNY)时，折算人民币初始净值与初始净值必须相等。<br/>");
					}
				}
			}
		}

		if(StringUtils.isNotBlank(subseqSubscrRegistInfo.getNavCur()) && subseqSubscrRegistInfo.getNavCur().equals("CNY")){
			if("".equals(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getNav(),"产品净值","^(\\d{1,5}(\\.\\d{1,5})?)","n（10,5）","0","1"))){
				if("".equals(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getConvertRmbNav(),"折算人民币累计净值","^(\\d{1,5}(\\.\\d{1,5})?)","n（10,5）","0","1"))){
					if(new BigDecimal(subseqSubscrRegistInfo.getNav()).compareTo(new BigDecimal(subseqSubscrRegistInfo.getConvertRmbNav()))!=0){
						stringErr.append("当产品净值的币种为人民币(CNY)时，折算人民币净值与产品净值必须相等。<br/>");
					}
				}
			}
		}

		if(StringUtils.isNotBlank(subseqSubscrRegistInfo.getNavCur()) && subseqSubscrRegistInfo.getNavCur().equals("CNY")){
			if("".equals(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getAggregateNav(),"累计净值","^(\\d{1,5}(\\.\\d{1,5})?)","n（10,5）","0","1"))){
				if("".equals(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getConvertRmbAggNav(),"折算人民币净值","^(\\d{1,5}(\\.\\d{1,5})?)","n（10,5）","0","1"))){
					if(new BigDecimal(subseqSubscrRegistInfo.getAggregateNav()).compareTo(new BigDecimal(subseqSubscrRegistInfo.getConvertRmbAggNav()))!=0){
						stringErr.append("当累计净值的币种为人民币(CNY)时，折算人民币累计净值与累计净值必须相等。<br/>");
					}
				}
			}
		}
		//最新预期最新收益率
		stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getExpectedAnnualReturnStr(),"最新预期收益率%","^(\\d{1,3}(\\.\\d{1,5})?)","n（8,5）","0","0") );

		//实现收益率%
		stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getRealizedAnnualReturnStr(),"实现收益率%","^(\\d{1,3}(\\.\\d{1,5})?)","n（8,5）","0","0") );

		//银行实现收益（元）
		stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getProdAmt(),"银行实现收益（元）","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","0") );

		//累计申购份额
		stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getSubscribedLatestVol(),"累计申购份额","^(\\d{1,13}(\\.\\d{1,5})?)","n（18,5）","1","1") );

		//累计赎回份额
		stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getRedeemedLatestVol(),"累计赎回份额","^(\\d{1,13}(\\.\\d{1,5})?)","n（18,5）","1","1") );

		//每万份份额分红
		stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getUnitsBonus(),"每万份份额分红","^(\\d{1,13}(\\.\\d{1,5})?)","n（18,5）","0","1") );

		//每万份现金分红
		stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getCashBonus(),"每万份现金分红","^(\\d{1,13}(\\.\\d{1,5})?)","n（18,5）","0","1") );

		//产品余额
		stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getProdAmt(),"产品余额","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","0","1") );

		//产品份额
		stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getProdVol(),"产品份额","^(\\d{1,13}(\\.\\d{1,5})?)","n（18,5）","1","1") );

		return stringErr.toString();
	}


	/**
	 * 合法性校验--终止登记
	 * @param whiteregex
	 * @param whitereForCode
	 * @param trTerminationRegistInfo
	 * @return
	 * @throws Exception
	 */
	public String terminationRegistInfoCheckInfo(String whiteregex,String whitereForCode, TrTerminationRegistInfo trTerminationRegistInfo) throws Exception{
		StringBuffer stringErr = new StringBuffer();
		if(StringUtils.isBlank(trTerminationRegistInfo.getProdCode())){
			stringErr.append("产品登记编码要素不可为空。<br/>");
		}else if(trTerminationRegistInfo.getProdCode().length() > 15){
			stringErr.append("产品登记编码要素过长：" + trTerminationRegistInfo.getProdCode()+"<br/>");
		}else{
			String regex = "^[A-Za-z0-9]{14,15}$";
			Pattern whiterpattern =Pattern.compile(regex);
			if(!whiterpattern.matcher(trTerminationRegistInfo.getProdCode()).matches()){stringErr.append("产品登记编码要素格式不对。正确的格式：14或15位英文或数字。<br/>");}
		}
		//发行机构代码
		String bank_code = trTerminationRegistInfo.getBankCode();
		String bank_code_s = CheckDataParams.bankCode;
		if (StringUtils.isBlank(bank_code)){
			stringErr.append("发行机构代码要素不可为空。<br/>");
		}else {
			Pattern p=Pattern.compile("^([Z]{1}\\d{5})");
			Matcher m=p.matcher(bank_code.trim());
			boolean flagBankCode = m.matches();
			if (!bank_code_s.equals(bank_code)){
				stringErr.append("发行机构代码必须与银行代码相同。<br/>");
			}else if(!flagBankCode){
				stringErr.append("发行机构代码要素格式不对。正确的格式：6位英文或数字。<br/>");
			}
		}

		//理财产品实际终止日期
		if(StringUtils.isBlank(trTerminationRegistInfo.getActualProdTerDate())){
			stringErr.append("理财产品实际终止日期要素不可为空。<br/>");
		}else {
			String am = trTerminationRegistInfo.getActualProdTerDate().trim();
			if(am.codePoints().count() == 8){
				Pattern p=Pattern.compile("^(\\d{8})");
				Matcher m=p.matcher(am);
				if(!m.matches()){
					stringErr.append("理财产品实际终止日期必须为日期格式（YYYYMMDD）。<br/>");
				}else if(!CheckDataUtils.isLegalDate(8,am,"yyyyMMdd")){
					stringErr.append("理财产品实际终止日期必须为正确日期。<br/>");
				}
			}else{
				Pattern p=Pattern.compile("^(\\d{4}\\-\\d{2}\\-\\d{2})");
				Matcher m=p.matcher(am);
				if(!m.matches()){
					stringErr.append("理财产品实际终止日期必须为日期格式（YYYY-MM-DD）。<br/>");
				}else if(!CheckDataUtils.isLegalDate(10,am,"yyyy-MM-dd")){
					stringErr.append("理财产品实际终止日期必须为正确日期。<br/>");
				}
			}
		}
		//银行实际实现收入（元）
		stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getRealizedBankIncome(),"银行实际实现收入（元）","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","0") );
		//兑付客户收益
		stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getInterestPayment(),"兑付客户收益（元）","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","0") );
		//兑付客户总金额（元）
		stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getPayment(),"兑付客户总金额（元）","^(\\d{1,15}(\\.\\d{1,5})?)","n（20,5）","1","1") );
		//兑付总份额
		stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getDeliveredVol(),"兑付总份额","^(\\d{1,15}(\\.\\d{1,5})?)","n（20,5）","1","1") );
		//本机构托管费（元）
		stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getInCustodianFee(),"本机构托管费（元）","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","1") );
		//本机构管理费（元）
		stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getInManageFee(),"本机构管理费（元）","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","1"));
		//本机构销售手续费（元）
		stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getInSalesCommision(),"本机构销售手续费（元）","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","1"));
		//本机构其他产品费用（元）
		stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getInOtherProdFee(),"本机构其他产品费用（元）","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","0"));
		//其他机构托管费（元）
		stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getOtherCustodianFee(),"其他机构托管费（元）","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","1"));
		//其他机构管理费（元）
		stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getOtherManageFee(),"其他机构管理费（元）","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","1"));
		//其他机构销售手续费（元）
		stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getOtherSalesComm(),"其他机构销售手续费（元）","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","1"));
		//投资顾问费用（元）
		stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getConsultFee(),"投资顾问费用（元）","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","1"));
		//其他机构其他产品费用（元）
		stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getOtherProdFee(),"其他机构其他产品费用（元）","^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）","1","0"));
		//客户实际年化收益率%
		stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getAnnualReturnClientStr(),"客户实际年化收益率%","^(\\d{1,3}(\\.\\d{1,5})?)","n（8,5）","1","0"));
		//产品实际年化收益率%
		stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getAnnualReturnProdStr(),"产品实际年化收益率%","^(\\d{1,3}(\\.\\d{1,5})?)","n（8,5）","1","0"));

		return stringErr.toString();
	}

	/**
	 * 合法性校验--资产负债信息登记
	 * @param whiteregex
	 * @param whitereForCode
	 * @param assetDebtRegisterInfo
	 * @return
	 * @throws Exception
	 */
	public String assetDebtRegisterInfoCheckInfo (String whiteregex,String whitereForCode, AssetDebtRegisterInfo assetDebtRegisterInfo) throws Exception {
		StringBuffer stringErr = new StringBuffer();

		if (StringUtils.isBlank(assetDebtRegisterInfo.getAssetCode())) {
			stringErr.append("行内资产/负债编码要素不可为空。<br/>");
		} else if (assetDebtRegisterInfo.getAssetCode().length() > 40) {
			stringErr.append("行内资产/负债编码要素过长：" + assetDebtRegisterInfo.getAssetCode().substring(0,39) + "<br/>");
		} else {
			String regex = "^[\\u0030-\\u0039\\u0041-\\u005A\\u0061-\\u007A]+$";
			Pattern whiterpattern = Pattern.compile(regex);
			if (!whiterpattern.matcher(assetDebtRegisterInfo.getAssetCode()).matches()) {
				stringErr.append("行内资产/负债编码只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。<br/>");
			}
		}
		//发行机构代码
		String bank_code = assetDebtRegisterInfo.getBankCode();
		String bank_code_s = CheckDataParams.bankCode;
		if (StringUtils.isBlank(bank_code)) {
			stringErr.append("发行机构代码要素不可为空。<br/>");
		} else {
			Pattern p = Pattern.compile("^([Z]{1}\\d{5})");
			Matcher m = p.matcher(bank_code.trim());
			boolean flagBankCode = m.matches();
			if (!bank_code_s.equals(bank_code)) {
				stringErr.append("发行机构代码必须与银行代码相同。<br/>");
			} else if (!flagBankCode) {
				stringErr.append("发行机构代码要素格式不对。正确的格式：6位英文或数字。<br/>");
			}
		}

		//资产/负债类别
		String assetType = assetDebtRegisterInfo.getAssDebtType();
		stringErr.append(CheckDataUtils.isNotEmpty(assetType ,"资产/负债类别"));
		//交易流通场所
		stringErr.append(CheckDataUtils.isNotEmpty(assetDebtRegisterInfo.getTradeVenue(),"交易流通场所"));
		//币种
		stringErr.append(CheckDataUtils.isNotEmpty(assetDebtRegisterInfo.getCur(),"币种"));

		String err_1 = CheckAssetInfo.checkAsset_1(whiteregex,whitereForCode,assetDebtRegisterInfo,assetType);
		if(StringUtils.isNotBlank(err_1)){
			stringErr.append(err_1);
		}
		String err_2 = CheckAssetInfo.checkAsset_2(whiteregex,whitereForCode,assetDebtRegisterInfo,assetType);
		if(StringUtils.isNotBlank(err_2)) {
			stringErr.append(err_2);
		}
		String err_3 = CheckAssetInfo.checkAsset_3(whiteregex,whitereForCode,assetDebtRegisterInfo,assetType);
		if(StringUtils.isNotBlank(err_3)) {
			stringErr.append(err_3);
		}
		return stringErr.toString();
	}
}
