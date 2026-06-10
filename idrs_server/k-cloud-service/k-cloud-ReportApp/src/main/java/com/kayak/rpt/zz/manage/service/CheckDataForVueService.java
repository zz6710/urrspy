package com.kayak.rpt.zz.manage.service;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysBeans;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.zz.manage.model.*;
import com.kayak.rpt.zz.manage.util.CheckAssetInfo;
import com.kayak.rpt.zz.manage.util.CheckDataParams;
import com.kayak.rpt.zz.manage.util.CheckDataUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CheckDataForVueService {
    @Autowired
    public ComnDao comnDao = SysBeans.getBean("comnDao");

    /**
     * 合法性校验--申报登记
     *
     * @param whiteregex
     * @param whitereForCode
     * @param prodRegistFilingInfo
     * @return
     * @throws Exception
     */
    public String prodRegistFilingInfoCheckForVue(String whiteregex, String whitereForCode, ProdRegistFilingInfo prodRegistFilingInfo) throws Exception {
        StringBuffer stringErr = new StringBuffer();
        stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getProdName(), "产品名称", 200, "1"));
        stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getIdentCode(), "行内标识码", 100, "1"));

        stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getProdAprvNm(), "产品审批人姓名", 200, "1"));
        String getApproverIdCode = prodRegistFilingInfo.getApproverIdCode();
        stringErr.append(CheckDataUtils.checkStringLength(getApproverIdCode, "产品审批人身份证号", 30, "1"));

        if (StringUtils.isNotBlank(getApproverIdCode)) {
            Pattern p9 = Pattern.compile("^\\d{6}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$");
            if (!p9.matcher(getApproverIdCode).matches()) {
                stringErr.append("产品审批人身份证号要素格式不对。<br/>");
            }
        }

        stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getProdDsnNm(), "产品设计人姓名", 200, "1"));
        stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getDesignerIdCode(), "产品设计人身份证号", 30, "1"));
        if (StringUtils.isNotBlank(prodRegistFilingInfo.getDesignerIdCode())) {
            Pattern p9 = Pattern.compile("^\\d{6}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$");
            if (!p9.matcher(prodRegistFilingInfo.getDesignerIdCode()).matches()) {
                stringErr.append("产品设计人身份证号要素格式不对。<br/>");
            }
        }
        stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getInvMngNm(), "投资经理姓名", 200, "1"));
        stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getManagerIdCode(), "投资经理身份证号", 30, "1"));
        if (StringUtils.isNotBlank(prodRegistFilingInfo.getManagerIdCode())) {
            Pattern p9 = Pattern.compile("^\\d{6}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$");
            if (!p9.matcher(prodRegistFilingInfo.getManagerIdCode()).matches()) {
                stringErr.append("投资经理身份证号要素格式不对。<br/>");
            }
        }

        stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getContactName(), "业务联络人姓名", 32, "1"));

        String getContactTelphone = prodRegistFilingInfo.getContactTelphone();
        stringErr.append(CheckDataUtils.checkStringLength(getContactTelphone, "业务联络人座机", 30, "1"));
        if (StringUtils.isNotBlank(getContactTelphone)) {
            Pattern p9 = Pattern.compile("^[\\-\\d]{1,30}");
            if (!p9.matcher(getContactTelphone).matches()) {
                stringErr.append("业务联络人座机要素格式不对。正确的格式：-或数字。<br/>");
            }
        }
        stringErr.append(CheckDataUtils.checkMoney(prodRegistFilingInfo.getContactMobile(), "业务联络人手机", "^[0-9]{1,11}", "n..11", "0", "0"));


        stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getContactEmail(), "业务联络人邮箱", 50, "1"));

        stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getAcManaName(), "实际管理人名称", 120, "0"));

        String getDcCdIdentCode = prodRegistFilingInfo.getDcCdIdentCode();
        stringErr.append(CheckDataUtils.checkStringLength(getDcCdIdentCode, "境内托管机构代码", 12, "0"));
        if (StringUtils.isNotBlank(getDcCdIdentCode)) {
            Pattern p9 = Pattern.compile("^[A-Za-z0-9]{1,12}");
            if (!p9.matcher(getDcCdIdentCode).matches()) {
                stringErr.append("境内托管机构代码要素格式不对。正确的格式：英文或数字。<br/>");
            }
        }

        stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getDcCdName(), "境外托管机构名称", 200, "0"));
        stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getProdBrand(), "产品品牌", 120, "0"));
        stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getCooperator(), "合作机构名称", 120, "0"));

        stringErr.append(CheckDataUtils.checkStringLength(prodRegistFilingInfo.getDetails(), "备注", 256, "0"));

        String getInvestTypeRatio = prodRegistFilingInfo.getInvestTypeRatio();
        stringErr.append(CheckDataUtils.checkStringLength(getInvestTypeRatio, "投资资产种类及比例", 300, "1"));
        if (StringUtils.isNotBlank(getInvestTypeRatio)) {
            String regex = "^(100(\\.00)?%:[^%:;\\-\\d]+(;100(\\.00)?%:[^%:;\\-\\d]+)*|" +
                    "(100(\\.00)?%|\\d{1,3}(?:\\.\\d{1,2})?%)(-(100(\\.00)?%|\\d{1,3}(?:\\.\\d{1,2})?%))?:" +
                    "[^%:;\\-\\d]+(;(100(\\.00)?%|\\d{1,3}(?:\\.\\d{1,2})?%)(-(100(\\.00)?%|\\d{1,3}(?:\\.\\d{1,2})?%))?:" +
                    "[^%:;\\-\\d]+)*;?)$";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(getInvestTypeRatio);
            if (!m.matches()) {
                stringErr.append("投资资产种类及比例要素格式不对。正确的格式：数字%：文字；数字%-数字%：文字（其中，百分号、冒号、分号、连字号均应为英文标点，文字和数字部分均不得含有英文百分号、冒号、分号、连字号）。其中，数字格式为n..（5，2），且需大于等于0、小于等于100。<br/>");
            }
        }
        //发行机构代码
        String bank_code = prodRegistFilingInfo.getBankCode();
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

        //与导入之间的区间是，不需要校验字段值域
        //产品收益类型
        if (StringUtils.isBlank(prodRegistFilingInfo.getProdRetrunType())) {
            stringErr.append("产品收益类型要素不可为空。<br/>");
        }
        //产品期限
        if (StringUtils.isBlank(prodRegistFilingInfo.getProdTerm())) {
            stringErr.append("产品期限要素不可为空。<br/>");
        }

        //是否金融同业专属
        if (StringUtils.isBlank(prodRegistFilingInfo.getFiancialExclusive())) {
            stringErr.append("是否金融同业专属要素不可为空。<br/>");
        }
        //资金投向地区
        if (StringUtils.isBlank(prodRegistFilingInfo.getInvertRegion())) {
            stringErr.append("资金投向地区不可为空。<br/>");
        }

        //产品运作模式
        if (StringUtils.isBlank(prodRegistFilingInfo.getOperationMode())) {
            stringErr.append("产品运作模式要素不可为空。<br/>");
        }

        //产品募集方式
        if (StringUtils.isBlank(prodRegistFilingInfo.getTypeCollect())) {
            stringErr.append("产品募集方式要素不可为空。<br/>");
        }

        //产品资产配置方式
        if (StringUtils.isBlank(prodRegistFilingInfo.getAssetAcMethod())) {
            stringErr.append("产品资产配置方式要素不可为空。<br/>");
        }
        //产品资产配置方式
        if (StringUtils.isBlank(prodRegistFilingInfo.getAssetAcMethod())) {
            stringErr.append("产品资产配置方式要素不可为空。<br/>");
        }
        //产品管理模式
        if (StringUtils.isBlank(prodRegistFilingInfo.getProdManaMode())) {
            stringErr.append("产品管理模式要素不可为空。<br/>");
        }
        //产品定价方式
        if (StringUtils.isBlank(prodRegistFilingInfo.getPriceMethod())) {
            stringErr.append("产品定价方式要素不可为空。<br/>");
        }
        //产品投资性质
        if (StringUtils.isBlank(prodRegistFilingInfo.getInvestType())) {
            stringErr.append("产品投资性质要素不可为空。<br/>");
        }
        //起点销售金额

        stringErr.append(CheckDataUtils.checkMoney(prodRegistFilingInfo.getInvestThreshold(), "起点销售金额", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "1", "1"));
        String am1 = prodRegistFilingInfo.getInvestThreshold().trim();
        if (StringUtils.isNotBlank(am1) & am1.compareTo("0") == 0) {
            stringErr.append("起点销售金额必须大于0。<br/>");
        }

        //募集币种
        if (StringUtils.isBlank(prodRegistFilingInfo.getFundCur())) {
            stringErr.append("募集币种要素不可为空。<br/>");
        }
        //兑付本金币种
        if (StringUtils.isBlank(prodRegistFilingInfo.getPrincipalCur())) {
            stringErr.append("兑付本金币种要素不可为空。<br/>");
        }
        //兑付收益币种
        if (StringUtils.isBlank(prodRegistFilingInfo.getIncomeCur())) {
            stringErr.append("兑付收益币种要素不可为空。<br/>");
        }
        //销售手续费率%
        stringErr.append(CheckDataUtils.checkMoney(prodRegistFilingInfo.getSalesCommissionRate(), "销售手续费率%", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "1", "1"));

        //托管费率%
        stringErr.append(CheckDataUtils.checkMoney(prodRegistFilingInfo.getCdFeeRate(), "托管费率%", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "1", "1"));

        //募集起始日期（从)
        if (StringUtils.isBlank(prodRegistFilingInfo.getStartDateEarliest())) {
            stringErr.append("募集起始日期（从）要素不可为空。<br/>");
        } else {
            String am = prodRegistFilingInfo.getStartDateEarliest().trim();
            if (am.codePoints().count() == 8) {
                Pattern p = Pattern.compile("^(\\d{8})");
                Matcher m = p.matcher(am);
                if (!m.matches()) {
                    stringErr.append("募集起始日期（从）必须为日期格式（YYYYMMDD）。<br/>");
                } else if (!CheckDataUtils.isLegalDate(8, am, "yyyyMMdd")) {
                    stringErr.append("募集起始日期（从）必须为正确日期。<br/>");
                }
            } else {
                Pattern p = Pattern.compile("^(\\d{4}\\-\\d{2}\\-\\d{2})");
                Matcher m = p.matcher(am);
                if (!m.matches()) {
                    stringErr.append("募集起始日期（从）必须为日期格式（YYYY-MM-DD）。<br/>");
                } else if (!CheckDataUtils.isLegalDate(10, am, "yyyy-MM-dd")) {
                    stringErr.append("募集起始日期（从）必须为正确日期。<br/>");
                }
            }
        }
        //募集起始日期（到)
        if (StringUtils.isBlank(prodRegistFilingInfo.getStartDateLatest())) {
            stringErr.append("募集起始日期（到）要素不可为空。<br/>");
        } else {
            String am = prodRegistFilingInfo.getStartDateLatest().trim();
            if (am.codePoints().count() == 8) {
                Pattern p = Pattern.compile("^(\\d{8})");
                Matcher m = p.matcher(am);
                if (!m.matches()) {
                    stringErr.append("募集起始日期（到）必须为日期格式（YYYYMMDD）。<br/>");
                } else if (!CheckDataUtils.isLegalDate(8, am, "yyyyMMdd")) {
                    stringErr.append("募集起始日期（到）必须为正确日期。<br/>");
                }
            } else {
                Pattern p = Pattern.compile("^(\\d{4}\\-\\d{2}\\-\\d{2})");
                Matcher m = p.matcher(am);
                if (!m.matches()) {
                    stringErr.append("募集起始日期（到）必须为日期格式（YYYY-MM-DD）。<br/>");
                } else if (!CheckDataUtils.isLegalDate(10, am, "yyyy-MM-dd")) {
                    stringErr.append("募集起始日期（到）必须为正确日期。<br/>");

                }
            }
            if (CheckDataUtils.compareTo(am, prodRegistFilingInfo.getStartDateEarliest()) < 0) {
                stringErr.append("募集起始日期（到）必须大于等于募集起始日期（从）。<br/>");
            }
        }
        //计划募集金额（元）
        stringErr.append(CheckDataUtils.checkMoney(prodRegistFilingInfo.getPlanFundAmt(), "计划募集金额（元）", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "1", "1"));
        String am0 = prodRegistFilingInfo.getPlanFundAmt().trim();
        if (StringUtils.isNotBlank(am0) && am0.compareTo("0") == 0) {
            stringErr.append("计划募集金额（元）必须大于0。<br/>");
        }
        //投资者风险偏好
        if (StringUtils.isBlank(prodRegistFilingInfo.getRiskLevel())) {
            stringErr.append("投资者风险偏好要素不可为空。<br/>");
        }
        //产品风险等级
        if (StringUtils.isBlank(prodRegistFilingInfo.getRiskRate())) {
            stringErr.append("产品风险等级要素不可为空。<br/>");
        }
        //发行机构提前终止权标识
        if (StringUtils.isBlank(prodRegistFilingInfo.getEarlyTnOption())) {
            stringErr.append("发行机构提前终止权标识要素不可为空。<br/>");
        }
        //客户赎回权标识
        if (StringUtils.isBlank(prodRegistFilingInfo.getInvestRdmOption())) {
            stringErr.append("客户赎回权标识要素不可为空。<br/>");
        }
        //产品期次
        stringErr.append(CheckDataUtils.checkMoney(prodRegistFilingInfo.getProdTermNo(), "产品期次", "^(\\d{1,6})", "n..6", "1", "0"));

        //投资管理费率%
        stringErr.append(CheckDataUtils.checkMoney(prodRegistFilingInfo.getManageFeeRate(), "投资管理费率%", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "1", "1"));

        //合作模式
        if (StringUtils.isBlank(prodRegistFilingInfo.getCooperateMode())) {
            stringErr.append("合作模式要素不可为空。<br/>");
        }

        //投资本金到账日
        if (StringUtils.isBlank(prodRegistFilingInfo.getPrincipalDueDate())) {
            stringErr.append("投资本金到账日要素不可为空。<br/>");
        }
        //投资收益到账日
        if (StringUtils.isBlank(prodRegistFilingInfo.getIncomeDueDate())) {
            stringErr.append("投资收益到账日要素不可为空。<br/>");
        }
        //产品增信标识
        if (StringUtils.isBlank(prodRegistFilingInfo.getProdCrtEnhance())) {
            stringErr.append("产品增信标识要素不可为空。<br/>");
        }
        //产品增信机构类型
        if (prodRegistFilingInfo.getProdCrtEnhance().equals("01")) {
            if (StringUtils.isBlank(prodRegistFilingInfo.getCrtInsType())) {
                stringErr.append("当产品增信标识为有时，产品增信机构类型要素不可为空。<br/>");
            }
        } else {
            if (StringUtils.isNotBlank(prodRegistFilingInfo.getCrtInsType())) {
                stringErr.append("当产品增信标识为无时，产品增信机构类型要素必须为空。<br/>");
            }
        }


        //产品增信形式
        if (prodRegistFilingInfo.getProdCrtEnhance().equals("01")) {
            if (StringUtils.isBlank(prodRegistFilingInfo.getProdCrtMethod())) {
                stringErr.append("当产品增信标识为有时，产品增信形式要素不可为空。<br/>");
            }
        } else {
            if (StringUtils.isNotBlank(prodRegistFilingInfo.getProdCrtMethod())) {
                stringErr.append("当产品增信标识为无时，产品增信形式要素必须为空。<br/>");
            }
        }

        //白名单字符
        Pattern whiterpattern = null;
        if (!"".equals(whiteregex)) {
            whiterpattern = Pattern.compile(whiteregex);
        }
        if (!"".equals(whiteregex)) {
            if (!whiterpattern.matcher(prodRegistFilingInfo.getProdName()).matches()) {
                stringErr.append("产品名称必须填写白名单中的文字。<br/>");
            }
            if (!bank_code.equals("") && !whiterpattern.matcher(bank_code).matches())
                stringErr.append("登记银行代码必须填写白名单中的文字");
            if (!whiterpattern.matcher(prodRegistFilingInfo.getApproverIdCode()).matches()) {
                stringErr.append("产品审批人身份证号必须填写白名单中的文字。<br/>");
            }
            if (!whiterpattern.matcher(prodRegistFilingInfo.getDesignerIdCode()).matches()) {
                stringErr.append("产品设计人身份证号必须填写白名单中的文字。<br/>");
            }
            if (!whiterpattern.matcher(prodRegistFilingInfo.getManagerIdCode()).matches()) {
                stringErr.append("投资经理身份证号必须填写白名单中的文字。<br/>");
            }
            if (StringUtils.isNotBlank(prodRegistFilingInfo.getContactName()) && !whiterpattern.matcher(prodRegistFilingInfo.getContactName()).matches()) {
                stringErr.append("业务联络人姓名必须填写白名单中的文字。<br/>");
            }
            if (StringUtils.isNotBlank(prodRegistFilingInfo.getAcManaName()) && !whiterpattern.matcher(prodRegistFilingInfo.getAcManaName()).matches()) {
                stringErr.append("实际管理人名称必须填写白名单中的文字。<br/>");
            }
            if (StringUtils.isNotBlank(prodRegistFilingInfo.getDcCdIdentCode()) && !whiterpattern.matcher(prodRegistFilingInfo.getDcCdIdentCode()).matches()) {
                stringErr.append("境内托管机构代码必须填写白名单中的文字。<br/>");
            }
            if (StringUtils.isNotBlank(prodRegistFilingInfo.getDcCdName()) && !whiterpattern.matcher(prodRegistFilingInfo.getDcCdName()).matches()) {
                stringErr.append("境外托管机构名称必须填写白名单中的文字。<br/>");
            }
            if (StringUtils.isNotBlank(prodRegistFilingInfo.getProdBrand()) && !whiterpattern.matcher(prodRegistFilingInfo.getProdBrand()).matches()) {
                stringErr.append("产品品牌必须填写白名单中的文字。<br/>");
            }
            if (StringUtils.isNotBlank(prodRegistFilingInfo.getCooperator()) && !whiterpattern.matcher(prodRegistFilingInfo.getCooperator()).matches()) {
                stringErr.append("合作机构名称必须填写白名单中的文字。<br/>");
            }
            if (StringUtils.isNotBlank(prodRegistFilingInfo.getInvestTypeRatio()) && !whiterpattern.matcher(prodRegistFilingInfo.getInvestTypeRatio()).matches()) {
                stringErr.append("投资资产种类及比例必须填写白名单中的文字。<br/>");
            }
            if (StringUtils.isNotBlank(prodRegistFilingInfo.getDetails()) && !whiterpattern.matcher(prodRegistFilingInfo.getDetails()).matches()) {
                stringErr.append("备注必须填写白名单中的文字。<br/>");
            }
        }
        //行内标识码只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。
        Pattern whiterpattern1 = null;
        if (!"".equals(whitereForCode)) {
            whiterpattern1 = Pattern.compile(whitereForCode);
            if (!whiterpattern1.matcher(prodRegistFilingInfo.getIdentCode()).matches()) {
                stringErr.append("行内标识码只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。<br/>");
            }
        }

        return stringErr.toString();
    }

    /**
     * 合法性校验--发行登记
     *
     * @param whiteregex
     * @param whitereForCode
     * @param prodIssuanceRegistInfo
     * @return
     * @throws Exception
     */
    public String prodIssuanceRegistInfoCheckForVue(String whiteregex, String whitereForCode, ProdIssuanceRegistInfo prodIssuanceRegistInfo) throws Exception {
        StringBuffer stringErr = new StringBuffer();
        if (StringUtils.isBlank(prodIssuanceRegistInfo.getProdCode())) {
            stringErr.append("产品登记编码要素不可为空。<br/>");
        } else if (prodIssuanceRegistInfo.getProdCode().length() > 15) {
            stringErr.append("产品登记编码要素过长：" + prodIssuanceRegistInfo.getProdCode() + "。<br/>");
        } else {
            String regex = "^[A-Za-z0-9]{14,15}$";
            Pattern whiterpattern = Pattern.compile(regex);
            if (!whiterpattern.matcher(prodIssuanceRegistInfo.getProdCode()).matches()) {
                stringErr.append("产品登记编码要素格式不对。正确的格式：14或15位英文或数字。<br/>");
            }
        }
        stringErr.append(CheckDataUtils.checkStringLength(prodIssuanceRegistInfo.getProdIdentCode(), "理财产品代码", 100, "1"));
        stringErr.append(CheckDataUtils.checkStringLength(prodIssuanceRegistInfo.getDetailsPerRate(), "业绩比较基准说明", 400, "0"));
        stringErr.append(CheckDataUtils.checkStringLength(prodIssuanceRegistInfo.getDisorderOpenPeriod(), "无规律开放说明", 256, "0"));
        stringErr.append(CheckDataUtils.checkStringLength(prodIssuanceRegistInfo.getDetailsBusiOpPeriod(), "开放期业务说明", 256, "0"));
        String AverageOpenNo = CheckDataUtils.checkMoney(prodIssuanceRegistInfo.getAverageOpenNo(), "平均开放次数（年化）", "^(\\d{1,3}(\\.\\d{1,2})?)", "n（5,2）", "0", "1");
        if (StringUtils.isNotBlank(prodIssuanceRegistInfo.getAverageOpenNo()) &&
                (CheckDataUtils.compareTo(prodIssuanceRegistInfo.getAverageOpenNo(), "0") <= 0 || CheckDataUtils.compareTo(prodIssuanceRegistInfo.getAverageOpenNo(), "366") > 0)) {
            stringErr.append("平均开放次数（年化）必须大于0且不能超过366。<br/>");
        } else {
            stringErr.append(AverageOpenNo);
        }
        stringErr.append(CheckDataUtils.checkMoney(prodIssuanceRegistInfo.getOtherOpenPeriod(), "其他规律开放周期(天)", "^(\\d{1,4})", "n..4", "0", "1"));
        stringErr.append(CheckDataUtils.checkMoney(prodIssuanceRegistInfo.getRegularOpenPeriodDay(), "定期开放周期（天）", "^(\\d{1,4})", "n..4", "0", "1"));

        //发行机构代码
        String bank_code = prodIssuanceRegistInfo.getBankCode();
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


        //管理方式
        if (StringUtils.isBlank(prodIssuanceRegistInfo.getManagementMethod())) {
            stringErr.append("管理方式要素不可为空。<br/>");
        }
        //是否为结构化（分级）产品
        if (StringUtils.isBlank(prodIssuanceRegistInfo.getStructuredProd())) {
            stringErr.append("是否为结构化（分级）产品不可为空。<br/>");
        }
        //管理方式
//		if(StringUtils.isBlank(prodIssuanceRegistInfo.getManagementMethod())){
//			stringErr.append("管理方式要素不可为空。<br/>");
//		}
        //开放模式
        if (!StringUtils.isBlank(prodIssuanceRegistInfo.getOpeningMode())) {
            //01有规律开放
            if ("01".equals(prodIssuanceRegistInfo.getOpeningMode())) {
                if (StringUtils.isBlank(prodIssuanceRegistInfo.getRegularOpenPeriod())) {
                    stringErr.append("当“开放模式”选择 “01 有规律开放”时，规律开放周期不可为空。<br/>");
                }
            }
            //02无规律开放
            if ("02".equals(prodIssuanceRegistInfo.getOpeningMode())) {
                if (!StringUtils.isBlank(prodIssuanceRegistInfo.getRegularOpenPeriod())) {
                    stringErr.append("当“开放模式”选择 “02无规律开放”时，规律开放周期必须为空。<br/>");
                }
            }
        }
//		//规律开放周期
//		if(StringUtils.isBlank(prodIssuanceRegistInfo.getRegularOpenPeriod())){
//			stringErr.append("规律开放周期不可为空。<br/>");
//		}
//		//节假日是否开放
//		if(StringUtils.isBlank(prodIssuanceRegistInfo.getHolidayOpenType())){
//			stringErr.append("节假日是否开放不可为空。<br/>");
//		}
//		//开放期业务
//		if(StringUtils.isBlank(prodIssuanceRegistInfo.getBusiOpenPeriod())){
//			stringErr.append("开放期业务不可为空。<br/>");
//		}

        //募集起始日期
        if (StringUtils.isBlank(prodIssuanceRegistInfo.getSubscriptionStartDate())) {
            stringErr.append("募集起始日期要素不可为空。<br/>");
        } else {
            String am = prodIssuanceRegistInfo.getSubscriptionStartDate().trim();
            if (am.codePoints().count() == 8) {
                Pattern p = Pattern.compile("^(\\d{8})");
                Matcher m = p.matcher(am);
                if (!m.matches()) {
                    stringErr.append("募集起始日期必须为日期格式（YYYYMMDD）。<br/>");
                } else if (!CheckDataUtils.isLegalDate(8, am, "yyyyMMdd")) {
                    stringErr.append("募集起始日期必须为正确日期。<br/>");
                }
            } else {
                Pattern p = Pattern.compile("^(\\d{4}\\-\\d{2}\\-\\d{2})");
                Matcher m = p.matcher(am);
                if (!m.matches()) {
                    stringErr.append("募集起始日期必须为日期格式（YYYY-MM-DD）。<br/>");
                } else if (!CheckDataUtils.isLegalDate(10, am, "yyyy-MM-dd")) {
                    stringErr.append("募集起始日期必须为正确日期。<br/>");
                }
            }
        }
        //募集结束日期
        if (StringUtils.isBlank(prodIssuanceRegistInfo.getSubscriptionEndDate())) {
            stringErr.append("募集结束日期要素不可为空。<br/>");
        } else {
            String am = prodIssuanceRegistInfo.getSubscriptionEndDate().trim();
            if (CheckDataUtils.compareTo(am, prodIssuanceRegistInfo.getSubscriptionStartDate()) < 0) {
                stringErr.append("募集结束日期必须大于等于募集起始日期。<br/>");
            } else if (am.codePoints().count() == 8) {
                Pattern p = Pattern.compile("^(\\d{8})");
                Matcher m = p.matcher(am);
                if (!m.matches()) {
                    stringErr.append("募集结束日期必须为日期格式（YYYYMMDD）。<br/>");
                } else if (!CheckDataUtils.isLegalDate(8, am, "yyyyMMdd")) {
                    stringErr.append("募集结束日期必须为正确日期。<br/>");
                }
            } else {
                Pattern p = Pattern.compile("^(\\d{4}\\-\\d{2}\\-\\d{2})");
                Matcher m = p.matcher(am);
                if (!m.matches()) {
                    stringErr.append("募集结束日期必须为日期格式（YYYY-MM-DD）。<br/>");
                } else if (!CheckDataUtils.isLegalDate(10, am, "yyyy-MM-dd")) {
                    stringErr.append("募集结束日期必须为正确日期。<br/>");
                }
            }
        }
        //产品起始日期
        if (StringUtils.isBlank(prodIssuanceRegistInfo.getProdValueDate())) {
            stringErr.append("产品起始日期要素不可为空。<br/>");
        } else {
            String am = prodIssuanceRegistInfo.getProdValueDate().trim();
            if (CheckDataUtils.compareTo(am, prodIssuanceRegistInfo.getSubscriptionEndDate()) < 0) {
                stringErr.append("产品起始日期必须大于等于募集结束日期。<br/>");
            } else if (am.codePoints().count() == 8) {
                Pattern p = Pattern.compile("^(\\d{8})");
                Matcher m = p.matcher(am);
                if (!m.matches()) {
                    stringErr.append("产品起始日期必须为日期格式（YYYYMMDD）。<br/>");
                } else if (!CheckDataUtils.isLegalDate(8, am, "yyyyMMdd")) {
                    stringErr.append("产品起始日期必须为正确日期。<br/>");
                }
            } else {
                Pattern p = Pattern.compile("^(\\d{4}\\-\\d{2}\\-\\d{2})");
                Matcher m = p.matcher(am);
                if (!m.matches()) {
                    stringErr.append("产品起始日期必须为日期格式（YYYY-MM-DD）。<br/>");
                } else if (!CheckDataUtils.isLegalDate(10, am, "yyyy-MM-dd")) {
                    stringErr.append("产品起始日期必须为正确日期。<br/>");
                }
            }
        }
        //产品终止日期
        if (StringUtils.isBlank(prodIssuanceRegistInfo.getProdMaturityDate())) {
            stringErr.append("产品终止日期要素不可为空。<br/>");
        } else {
            String am = prodIssuanceRegistInfo.getProdMaturityDate().trim();
            if (CheckDataUtils.compareTo(am, prodIssuanceRegistInfo.getProdValueDate()) <= 0) {
                stringErr.append("产品终止日期必须大于产品起始日期。<br/>");
            } else if (am.codePoints().count() == 8) {
                Pattern p = Pattern.compile("^(\\d{8})");
                Matcher m = p.matcher(am);
                if (!m.matches()) {
                    stringErr.append("产品终止日期必须为日期格式（YYYYMMDD）。<br/>");
                } else if (!CheckDataUtils.isLegalDate(8, am, "yyyyMMdd")) {
                    stringErr.append("产品终止日期必须为正确日期。<br/>");
                }
            } else {
                Pattern p = Pattern.compile("^(\\d{4}\\-\\d{2}\\-\\d{2})");
                Matcher m = p.matcher(am);
                if (!m.matches()) {
                    stringErr.append("产品终止日期必须为日期格式（YYYY-MM-DD）。<br/>");
                } else if (!CheckDataUtils.isLegalDate(10, am, "yyyy-MM-dd")) {
                    stringErr.append("产品终止日期必须为正确日期。<br/>");
                }
            }
        }


        //白名单字符
        Pattern whiterpattern = null;
        if (!"".equals(whiteregex)) {
            whiterpattern = Pattern.compile(whiteregex);
        }
        if (!"".equals(whiteregex)) {
            if (!whiterpattern.matcher(prodIssuanceRegistInfo.getProdIdentCode()).matches()) {
                stringErr.append("理财产品代码必须填写白名单中的文字。<br/>");
            }
            if (!bank_code.equals("") && !whiterpattern.matcher(bank_code).matches())
                stringErr.append("登记银行代码必须填写白名单中的文字。<br/>");
            if (StringUtils.isNotBlank(prodIssuanceRegistInfo.getDetailsPerRate()) && !whiterpattern.matcher(prodIssuanceRegistInfo.getDetailsPerRate()).matches()) {
                stringErr.append("业绩比较基准说明必须填写白名单中的文字。<br/>");
            }
            if (StringUtils.isNotBlank(prodIssuanceRegistInfo.getDisorderOpenPeriod()) && !whiterpattern.matcher(prodIssuanceRegistInfo.getDisorderOpenPeriod()).matches()) {
                stringErr.append("无规律开放说明必须填写白名单中的文字。<br/>");
            }
            if (StringUtils.isNotBlank(prodIssuanceRegistInfo.getDetailsBusiOpPeriod()) && !whiterpattern.matcher(prodIssuanceRegistInfo.getDetailsBusiOpPeriod()).matches()) {
                stringErr.append("开放期业务说明必须填写白名单中的文字。<br/>");
            }
        }

        return stringErr.toString();
    }

    /**
     * 合法性校验--募集期总量登记
     *
     * @param whiteregex
     * @param whitereForCode
     * @param initialSubRegistInfo
     * @return
     * @throws Exception
     */
    public String initialSubRegistInfoCheckForVue(String whiteregex, String whitereForCode, InitialSubRegistInfo initialSubRegistInfo) throws Exception {
        StringBuffer stringErr = new StringBuffer();
        if (StringUtils.isBlank(initialSubRegistInfo.getProdCode())) {
            stringErr.append("产品登记编码要素不可为空。<br/>");
        } else if (initialSubRegistInfo.getProdCode().length() > 15) {
            stringErr.append("产品登记编码要素过长：" + initialSubRegistInfo.getProdCode() + "。<br/>");
        } else {
            String regex = "^[A-Za-z0-9]{14,15}$";
            Pattern whiterpattern = Pattern.compile(regex);
            if (!whiterpattern.matcher(initialSubRegistInfo.getProdCode()).matches()) {
                stringErr.append("产品登记编码要素格式不对。正确的格式：14或15位英文或数字。<br/>");
            }
        }
        if (StringUtils.isBlank(initialSubRegistInfo.getFndTrstActNbr())) {
            stringErr.append("资金托管账号要素不可为空。<br/>");
        } else if (initialSubRegistInfo.getFndTrstActNbr().codePoints().count() > 60) {
            stringErr.append("资金托管账号过长：" + initialSubRegistInfo.getFndTrstActNbr() + "。<br/>");
        } else {
            String regex = "^[A-Za-z0-9\\-]+$";
            Pattern whiterpattern = Pattern.compile(regex);
            if (!whiterpattern.matcher(initialSubRegistInfo.getFndTrstActNbr()).matches()) {
                stringErr.append("资金托管账号要素格式不对。正确的格式：大小写字母或数字、以及“-”。<br/>");
            }
        }
        if (StringUtils.isBlank(initialSubRegistInfo.getFndTrstAct())) {
            stringErr.append("资金托管账户要素不可为空。<br/>");
        } else if (initialSubRegistInfo.getFndTrstAct().codePoints().count() > 200) {
            stringErr.append("资金托管账户过长：" + initialSubRegistInfo.getFndTrstAct() + "。<br/>");
        }

        //个人投资者总数
        stringErr.append(CheckDataUtils.checkMoney(initialSubRegistInfo.getNumberIndivInvest(), "个人投资者总数", "^(\\d{1,9})", "n..9", "1", "1"));
        //法人投资者总数
        stringErr.append(CheckDataUtils.checkMoney(initialSubRegistInfo.getNumberCorporInvest(), "法人投资者总数", "^(\\d{1,9})", "n..9", "1", "1"));
        //非法人投资者总数
        stringErr.append(CheckDataUtils.checkMoney(initialSubRegistInfo.getNumberUcorInvest(), "非法人投资者总数", "^(\\d{1,9})", "n..9", "1", "1"));
        //实际募集金额（元）
        stringErr.append(CheckDataUtils.checkMoney(initialSubRegistInfo.getActualSubscribedAmt(), "实际募集金额（元）", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "1", "1"));

        //募集总份额
        stringErr.append(CheckDataUtils.checkMoney(initialSubRegistInfo.getSubscribedVol(), "募集总份额", "^(\\d{1,13}(\\.\\d{1,5})?)", "n（18,5）", "1", "1"));

        //发行机构代码
        String bank_code = initialSubRegistInfo.getBankCode();
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

        //是否有其他机构代销
        if (StringUtils.isBlank(initialSubRegistInfo.getOtherDistributAgents())) {
            stringErr.append("是否有其他机构代销要素不可为空。<br/>");
        }

        //代销总金额
        if (StringUtils.isNotBlank(initialSubRegistInfo.getOtherDistributAgents())) {
            if (initialSubRegistInfo.getOtherDistributAgents().equals("01")) {
                if (StringUtils.isBlank(initialSubRegistInfo.getAmtOtherDbAgents())) {
                    stringErr.append("当是否有其他机构代销为是时，代销总金额要素不可为空。<br/>");
                } else {
                    if (CheckDataUtils.compareTo(initialSubRegistInfo.getActualSubscribedAmt(), initialSubRegistInfo.getAmtOtherDbAgents()) < 0) {
                        stringErr.append("代销总金额必须小于等于实际募集金额。<br/>");
                    } else {
                        stringErr.append(CheckDataUtils.checkMoney(initialSubRegistInfo.getAmtOtherDbAgents(), "代销总金额", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "1", "1"));
                    }
                }
            } else if (initialSubRegistInfo.getOtherDistributAgents().equals("02")) {
                if (StringUtils.isNotBlank(initialSubRegistInfo.getAmtOtherDbAgents())) {
                    stringErr.append("当是否有其他机构代销为否时，代销总金额要素必须为空。<br/>");
                }
            }
        }


        //白名单字符
        Pattern whiterpattern = null;
        if (!"".equals(whiteregex)) {
            whiterpattern = Pattern.compile(whiteregex);
        }
        if (!"".equals(whiteregex)) {
            if (StringUtils.isNotBlank(initialSubRegistInfo.getFndTrstAct()) && !whiterpattern.matcher(initialSubRegistInfo.getFndTrstAct()).matches()) {
                stringErr.append("资金托管账户必须填写白名单中的文字。<br/>");
            }
            if (!bank_code.equals("") && !whiterpattern.matcher(bank_code).matches())
                stringErr.append("登记银行代码必须填写白名单中的文字。<br/>");
            if (StringUtils.isNotBlank(initialSubRegistInfo.getDetails()) && !whiterpattern.matcher(initialSubRegistInfo.getDetails()).matches()) {
                stringErr.append("备注必须填写白名单中的文字。<br/>");
            }
        }
        return stringErr.toString();
    }

    /**
     * 合法性校验--存续期总量登记
     *
     * @param whiteregex
     * @param whitereForCode
     * @param subseqSubscrRegistInfo
     * @return
     * @throws Exception
     */
    public String subseqSubscrRegistInfoCheckForVue(String whiteregex, String whitereForCode, SubseqSubscrRegistInfo subseqSubscrRegistInfo) throws Exception {
        StringBuffer stringErr = new StringBuffer();
        //发行机构代码
        String bank_code = subseqSubscrRegistInfo.getBankCode();
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
        if (StringUtils.isBlank(subseqSubscrRegistInfo.getProdCode())) {
            stringErr.append("产品登记编码要素不可为空。<br/>");
        } else if (subseqSubscrRegistInfo.getProdCode().length() > 15) {
            stringErr.append("产品登记编码要素过长：" + subseqSubscrRegistInfo.getProdCode() + "。<br/>");
        } else {
            String regex = "^[A-Za-z0-9]{14,15}$";
            Pattern whiterpattern = Pattern.compile(regex);
            if (!whiterpattern.matcher(subseqSubscrRegistInfo.getProdCode()).matches()) {
                stringErr.append("产品登记编码要素格式不对。正确的格式：14或15位英文或数字。<br/>");
            }
        }

        //初始净值
        stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getInitialNav(), "初始净值", "^(\\d{1,5}(\\.\\d{1,5})?)", "n（10,5）", "0", "1"));

        //产品净值
        stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getNav(), "产品净值", "^(\\d{1,5}(\\.\\d{1,5})?)", "n（10,5）", "0", "1"));
        //累计净值
        stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getAggregateNav(), "累计净值", "^(\\d{1,5}(\\.\\d{1,5})?)", "n（10,5）", "0", "1"));

        //折算人民币初始净值
        stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getConvertInitialNav(), "折算人民币初始净值", "^(\\d{1,5}(\\.\\d{1,5})?)", "n（10,5）", "0", "1"));

        //折算人民币净值
        stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getConvertRmbNav(), "折算人民币净值", "^(\\d{1,5}(\\.\\d{1,5})?)", "n（10,5）", "0", "1"));

        //折算人民币累计净值
        stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getConvertRmbAggNav(), "折算人民币累计净值", "^(\\d{1,5}(\\.\\d{1,5})?)", "n（10,5）", "0", "1"));

        //累计申购份额
        stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getSubscribedLatestVol(), "累计申购份额", "^(\\d{1,13}(\\.\\d{1,5})?)", "n（18,5）", "1", "1"));

        //累计赎回份额
        stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getRedeemedLatestVol(), "累计赎回份额", "^(\\d{1,13}(\\.\\d{1,5})?)", "n（18,5）", "1", "1"));

        //每万份份额分红
        stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getUnitsBonus(), "每万份份额分红", "^(\\d{1,13}(\\.\\d{1,5})?)", "n（18,5）", "0", "1"));

        //每万份现金分红
        stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getCashBonus(), "每万份现金分红", "^(\\d{1,13}(\\.\\d{1,5})?)", "n（18,5）", "0", "1"));

        //产品余额
        stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getProdAmt(), "产品余额", "^(\\d{1,13}(\\.\\d{1,5})?)", "n（18,5）", "0", "1"));

        //产品份额
        stringErr.append(CheckDataUtils.checkMoney(subseqSubscrRegistInfo.getProdVol(), "产品份额", "^(\\d{1,13}(\\.\\d{1,5})?)", "n（18,5）", "1", "1"));

        return stringErr.toString();
    }


    /**
     * 合法性校验--终止登记
     *
     * @param whiteregex
     * @param whitereForCode
     * @param trTerminationRegistInfo
     * @return
     * @throws Exception
     */
    public String terminationRegistInfoCheckForVue(String whiteregex, String whitereForCode, TrTerminationRegistInfo trTerminationRegistInfo) throws Exception {
        StringBuffer stringErr = new StringBuffer();
        if (StringUtils.isBlank(trTerminationRegistInfo.getProdCode())) {
            stringErr.append("产品登记编码要素不可为空。<br/>");
        } else if (trTerminationRegistInfo.getProdCode().length() > 15) {
            stringErr.append("产品登记编码要素过长：" + trTerminationRegistInfo.getProdCode() + "。<br/>");
        } else {
            String regex = "^[A-Za-z0-9]{14,15}$";
            Pattern whiterpattern = Pattern.compile(regex);
            if (!whiterpattern.matcher(trTerminationRegistInfo.getProdCode()).matches()) {
                stringErr.append("产品登记编码要素格式不对。正确的格式：14或15位英文或数字。<br/>");
            }
        }
        //发行机构代码
        String bank_code = trTerminationRegistInfo.getBankCode();
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

        //理财产品实际终止日期
        if (StringUtils.isBlank(trTerminationRegistInfo.getActualProdTerDate())) {
            stringErr.append("理财产品实际终止日期要素不可为空。<br/>");
        } else {
            String am = trTerminationRegistInfo.getActualProdTerDate().trim();
            if (am.codePoints().count() == 8) {
                Pattern p = Pattern.compile("^(\\d{8})");
                Matcher m = p.matcher(am);
                if (!m.matches()) {
                    stringErr.append("理财产品实际终止日期必须为日期格式（YYYYMMDD）。<br/>");
                } else if (!CheckDataUtils.isLegalDate(8, am, "yyyyMMdd")) {
                    stringErr.append("理财产品实际终止日期必须为正确日期。<br/>");
                }
            } else {
                Pattern p = Pattern.compile("^(\\d{4}\\-\\d{2}\\-\\d{2})");
                Matcher m = p.matcher(am);
                if (!m.matches()) {
                    stringErr.append("理财产品实际终止日期必须为日期格式（YYYY-MM-DD）。<br/>");
                } else if (!CheckDataUtils.isLegalDate(10, am, "yyyy-MM-dd")) {
                    stringErr.append("理财产品实际终止日期必须为正确日期。<br/>");
                }
            }
        }
        //银行实际实现收入（元）
        stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getRealizedBankIncome(), "银行实际实现收入（元）", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "1", "1"));
        //兑付客户收益
        stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getInterestPayment(), "兑付客户收益（元）", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "1", "1"));
        //兑付客户总金额（元）
        stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getPayment(), "兑付客户总金额（元）", "^(\\d{1,15}(\\.\\d{1,5})?)", "n（20,5）", "1", "1"));
        //兑付总份额
        stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getDeliveredVol(), "兑付总份额", "^(\\d{1,15}(\\.\\d{1,5})?)", "n（20,5）", "1", "1"));
        //本机构托管费（元）
        stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getInCustodianFee(), "本机构托管费（元）", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "1", "1"));
        //本机构管理费（元）
        stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getInManageFee(), "本机构管理费（元）", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "1", "1"));
        //本机构销售手续费（元）
        stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getInSalesCommision(), "本机构销售手续费（元）", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "1", "1"));
        //本机构其他产品费用（元）
        stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getInOtherProdFee(), "本机构其他产品费用（元）", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "1", "0"));
        //其他机构托管费（元）
        stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getOtherCustodianFee(), "其他机构托管费（元）", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "1", "1"));
        //其他机构管理费（元）
        stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getOtherManageFee(), "其他机构管理费（元）", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "1", "1"));
        //其他机构销售手续费（元）
        stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getOtherSalesComm(), "其他机构销售手续费（元）", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "1", "1"));
        //投资顾问费用（元）
        stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getConsultFee(), "投资顾问费用（元）", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "1", "1"));
        //其他机构其他产品费用（元）
        stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getOtherProdFee(), "其他机构其他产品费用（元）", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "1", "0"));
        //客户实际年化收益率%
        stringErr.append(CheckDataUtils.checkMoney(trTerminationRegistInfo.getAnnualReturnClient().toString(), "客户实际年化收益率%", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "1", "0"));

        return stringErr.toString();
    }

    /**
     * 净值信息登记
     *
     * @param whiteregex
     * @param whitereForCode
     * @param appNavInfoReg
     * @return
     * @throws Exception
     */
    public String appNavInfoRegCheckForVue(String whiteregex, String whitereForCode, AppNavInfoReg appNavInfoReg) throws Exception {
        StringBuffer stringErr = new StringBuffer();

        String prodRegEnc = appNavInfoReg.getProdRegEnc();
        if (StringUtils.isBlank(prodRegEnc)) {
            stringErr.append("产品登记编码要素不可为空。<br/>");
        } else if (appNavInfoReg.getProdRegEnc().length() > 15) {
            stringErr.append("产品登记编码要素过长：" + prodRegEnc + "。<br/>");
        } else {
            String regex = "^[A-Za-z0-9]{14,15}$";
            Pattern whiterpattern = Pattern.compile(regex);
            if (!whiterpattern.matcher(prodRegEnc).matches()) {
                stringErr.append("产品登记编码要素格式不对。正确的格式：14或15位英文或数字。<br/>");
            }
        }
        //发行机构代码
        String bank_code = appNavInfoReg.getBankCode();
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
        String NavRegType = appNavInfoReg.getNavRegType();
        if (StringUtils.isBlank(NavRegType)) {
            stringErr.append("净值登记类型不可为空。<br/>");
        }

        String sonShareCode = appNavInfoReg.getSonShareCode();
        if (StringUtils.isNotBlank(NavRegType)) {
            if (NavRegType.contains("01") && StringUtils.isNotBlank(sonShareCode)) {
                stringErr.append("当“净值登记类型”选择01母产品时，本要素不能填写。<br/>");
            } else if (NavRegType.contains("02") && StringUtils.isBlank(sonShareCode)) {
                stringErr.append("当“净值登记类型”选择02子份额时，本要素必填。<br/>");
            }
        }
        if (StringUtils.isBlank(appNavInfoReg.getCny())) {
            stringErr.append("币种不可为空。<br/>");
        }else{
            String ccyinfo="AED,AFN,ALL,AMD,ANG,AOA,ARS,AUD,AWG,AZN,BAM,BBD,BDT,BGN,BHD,BIF,BMD,BND,BOB,BOV,BRL,BSD,BTN,BWP,BYR,BZD,CAD,CDF,CHE,CHF,CHW,CLF,CLP,CNY,COP,COU,CRC,CUC,CUP,CVE,CZK,DJF,DKK,DOP,DZD,EGP,ERN,ETB,EUR,FJD,FKP,GBP,GEL,GHS,GIP,GMD,GNF,GTQ,GYD,HKD,HNL,HRK,HTG,HUF,IDR,ILS,INR,IQD,IRR,ISK,JMD,JOD,JPY,KES,KGS,KHR,KMF,KPW,KRW,KWD,KYD,KZT,LAK,LBP,LKR,LRD,LSL,LYD,MAD,MDL,MGA,MKD,MMK,MNT,MOP,MRO,MUR,MVR,MWK,MXN,MXV,MYR,MZN,NAD,NGN,NIO,NOK,NPR,NZD,OMR,PAB,PEN,PGK,PHP,PKR,PLN,PYG,QAR,RON,RSD,RUB,RWF,SAR,SBD,SCR,SDG,SEK,SGD,SHP,SLL,SOS,SRD,SSP,STD,SVC,SYP,SZL,THB,TJS,TMT,TND,TOP,TRY,TTD,TWD,TZS,UAH,UGX,USD,USN,UYI,UYU,UZS,VEF,VND,VUV,WST,XAF,XAG,XAU,XBA,XBB,XBC,XBD,XCD,XDR,XOF,XPD,XPF,XPT,XSU,XTS,XUA,XXX,YER,ZAR,ZMW,ZWL";
            if(!ccyinfo.contains(appNavInfoReg.getCny())){
                stringErr.append("币种不在值域范围内。<br/>");
            }
        }

        stringErr.append(CheckDataUtils.checkMoney(appNavInfoReg.getNav(), "净值", "^(\\d{1,5}(\\.\\d{1,8})?)", "n（13,8）", "1", "1"));
        stringErr.append(CheckDataUtils.checkMoney(appNavInfoReg.getRmbNav(), "折算人民币净值", "^(\\d{1,5}(\\.\\d{1,8})?)", "n（13,8）", "1", "1"));
        stringErr.append(CheckDataUtils.checkMoney(appNavInfoReg.getDjNav(), "达基净值", "^(\\d{1,5}(\\.\\d{1,8})?)", "n（13,8）", "0", "1"));
        stringErr.append(CheckDataUtils.checkMoney(appNavInfoReg.getTotalNav(), "累计净值", "^(\\d{1,5}(\\.\\d{1,8})?)", "n（13,8）", "1", "1"));
        stringErr.append(CheckDataUtils.checkMoney(appNavInfoReg.getRmbTotalNav(), "折算人民币累计净值", "^(\\d{1,5}(\\.\\d{1,8})?)", "n（13,8）", "1", "1"));
        stringErr.append(CheckDataUtils.checkMoney(appNavInfoReg.getFqNav(), "复权净值", "^(\\d{1,5}(\\.\\d{1,8})?)", "n（13,8）", "1", "1"));
        stringErr.append(CheckDataUtils.checkMoney(appNavInfoReg.getRmbFqNav(), "折算人民币复权净值", "^(\\d{1,5}(\\.\\d{1,8})?)", "n（13,8）", "1", "1"));

        if (StringUtils.isBlank(appNavInfoReg.getNavCalType())) {
            stringErr.append("估值依据不可为空。<br/>");
        }
        if (StringUtils.isBlank(appNavInfoReg.getNavDate())) {
            stringErr.append("净值日期要素不可为空。<br/>");
        } else {
            stringErr.append(CheckDataUtils.checkDate(appNavInfoReg.getNavDate(), "净值日期"));
        }

        stringErr.append(CheckDataUtils.checkDate(appNavInfoReg.getDisclosureDate(), "披露日期"));


        stringErr.append(CheckDataUtils.checkMoney(appNavInfoReg.getShare(), "份额", "^(\\d{1,15}(\\.\\d{1,5})?)", "n（20,5）", "1", "1"));
        stringErr.append(CheckDataUtils.checkMoney(appNavInfoReg.getRemainBal(), "存续余额（元）", "^(\\d{1,15}(\\.\\d{1,5})?)", "n（20,5）", "1", "1"));
        stringErr.append(CheckDataUtils.checkMoney(appNavInfoReg.getRemainBal(), "折算人民币存续余额（元）", "^(\\d{1,15}(\\.\\d{1,5})?)", "n（20,5）", "1", "1"));

        stringErr.append(CheckDataUtils.checkStringLength(appNavInfoReg.getDetails(), "备注", 256, "0"));


        return stringErr.toString();
    }

    /**
     * 净值信息登记
     *
     * @param appNavInfoReg
     * @return
     * @throws Exception
     */
    public String appNavInfoRegCheckForImport(AppNavInfoReg appNavInfoReg) throws Exception {
        StringBuffer stringErr = new StringBuffer();
        if(StringUtils.isNotBlank(appNavInfoReg.getNavCalType())){
            String errDesc = CheckDataUtils.checkDictValue("subm_navCalType", appNavInfoReg.getNavCalType());
            if(StringUtils.isNotBlank(errDesc)){
                stringErr.append("估值依据："+errDesc);
            }
        }
        return stringErr.toString();
    }


    /**
     * 产品状态登记
     *
     * @param whiteregex
     * @param whitereForCode
     * @param prodStateRegistInfo
     * @return
     * @throws Exception
     */
    public String prodStateRegistInfoCheckForVue(String whiteregex, String whitereForCode, ProdStateRegistInfo prodStateRegistInfo) throws Exception {
        StringBuffer stringErr = new StringBuffer();
        //行内标识码只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。
        Pattern whiterpattern1 = null;
        whiterpattern1 = Pattern.compile(whitereForCode);
        String prodRegEnc = prodStateRegistInfo.getProdRegEnc();
        if (StringUtils.isBlank(prodRegEnc)) {
            stringErr.append("产品登记编码要素不可为空。<br/>");
        } else if (prodRegEnc.length() > 15) {
            stringErr.append("产品登记编码要素过长：" + prodRegEnc + "。<br/>");
        } else {
            String regex = "^[A-Za-z0-9]{14,15}$";
            Pattern whiterpattern = Pattern.compile(regex);
            if (!whiterpattern.matcher(prodRegEnc).matches()) {
                stringErr.append("产品登记编码要素格式不对。正确的格式：14或15位英文或数字。<br/>");
            }
        }
        //发行机构代码
        String bank_code = prodStateRegistInfo.getBankCode();
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
        stringErr.append(CheckDataUtils.checkMoney(prodStateRegistInfo.getTotAssets(), "理财产品总资产金额(元)", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "1", "0"));

        stringErr.append(CheckDataUtils.checkMoney(prodStateRegistInfo.getRate(), "理财产品杠杆率(%)", "^(-?\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "1", "0"));
        stringErr.append(CheckDataUtils.checkDate(prodStateRegistInfo.getValdate(), "产品状态统计日"));
        stringErr.append(CheckDataUtils.checkStringLength(prodStateRegistInfo.getDetails(), "备注", 256, "0"));

        return stringErr.toString();
    }

    /**
     * 合法性校验--资产负债信息登记
     *
     * @param assetDebtRegisterInfo
     * @return
     * @throws Exception
     */
    public String assetDebtRegisterInfoCheckForImport(AssetDebtRegisterInfo assetDebtRegisterInfo) throws Exception {
        StringBuffer stringErr = new StringBuffer();
        if(StringUtils.isNotBlank(assetDebtRegisterInfo.getCcSpecificBondType())){
            String errDesc = CheckDataUtils.checkDictValue("subm_spcType", assetDebtRegisterInfo.getCcSpecificBondType().split(" ")[0], assetDebtRegisterInfo.getCcSpecificBondType());
            if(StringUtils.isNotBlank(errDesc)){
                stringErr.append("具体类别："+errDesc);
            }
        }

        if(StringUtils.isNotBlank(assetDebtRegisterInfo.getCcIssRatePart())){
            String errDesc = CheckDataUtils.checkDictValue("subm_mainRating", assetDebtRegisterInfo.getCcIssRatePart().split(" ")[0], assetDebtRegisterInfo.getCcIssRatePart());
            if(StringUtils.isNotBlank(errDesc)){
                stringErr.append("主体评级："+errDesc);
            }
        }

        if(StringUtils.isNotBlank(assetDebtRegisterInfo.getCcIssModeBond())){
            String errDesc = CheckDataUtils.checkDictValue("subm_iss_mode_bond", assetDebtRegisterInfo.getCcIssModeBond().split(" ")[0], assetDebtRegisterInfo.getCcIssModeBond());
            if(StringUtils.isNotBlank(errDesc)){
                stringErr.append("发行方式："+errDesc);
            }
        }

        if(StringUtils.isNotBlank(assetDebtRegisterInfo.getCcIndustryIssuer())){
            String errDesc = CheckDataUtils.checkDictValue("subm_isuOrgBlgIdt", assetDebtRegisterInfo.getCcIndustryIssuer().split(" ")[0], assetDebtRegisterInfo.getCcIndustryIssuer());
            if(StringUtils.isNotBlank(errDesc)){
                stringErr.append("发行机构所属行业："+errDesc);
            }
        }

        if(StringUtils.isNotBlank(assetDebtRegisterInfo.getMmIndustryInvest())){
            String errDesc = CheckDataUtils.checkDictValue("subm_isuOrgBlgIdt",assetDebtRegisterInfo.getMmIndustryInvest().split(" ")[0],assetDebtRegisterInfo.getMmIndustryInvest());
            if(StringUtils.isNotBlank(errDesc)){
                stringErr.append("资金运用行业："+errDesc);
            }
        }

        if(StringUtils.isNotBlank(assetDebtRegisterInfo.getMmManagerType())){
            String errDesc = CheckDataUtils.checkDictValue("subm_mngMth",assetDebtRegisterInfo.getMmManagerType().split(" ")[0],assetDebtRegisterInfo.getMmManagerType());
            if(StringUtils.isNotBlank(errDesc)){
                stringErr.append("管理方式："+errDesc);
            }
        }

        return stringErr.toString();
    }

    /**
     * 合法性校验--资产负债信息登记
     *
     * @param whiteregex
     * @param whitereForCode
     * @param assetDebtRegisterInfo
     * @return
     * @throws Exception
     */
    public String assetDebtRegisterInfoCheckForVue(String whiteregex, String whitereForCode, AssetDebtRegisterInfo assetDebtRegisterInfo) throws Exception {
        StringBuffer stringErr = new StringBuffer();
        //行内标识码只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。
        Pattern whiterpattern1 = null;
        whiterpattern1 = Pattern.compile(whitereForCode);
        if (StringUtils.isBlank(assetDebtRegisterInfo.getAssetCode())) {
            stringErr.append("行内资产/负债编码要素不可为空。<br/>");
        } else if (assetDebtRegisterInfo.getAssetCode().length() > 40) {
            stringErr.append("行内资产/负债编码要素过长。<br/>");
        } else {
            if (!"".equals(whitereForCode)) {
                if (!whiterpattern1.matcher(assetDebtRegisterInfo.getAssetCode()).matches()) {
                    stringErr.append("行内资产/负债编码只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。<br/>");
                }
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
        stringErr.append(CheckDataUtils.isNotEmpty(assetType, "资产/负债类别"));
        //交易流通场所
        stringErr.append(CheckDataUtils.isNotEmpty(assetDebtRegisterInfo.getTradeVenue(), "交易流通场所"));
        //币种
        stringErr.append(CheckDataUtils.isNotEmpty(assetDebtRegisterInfo.getCur(), "币种"));

        String err_1 = CheckAssetInfo.checkAsset_1(whiteregex, whitereForCode, assetDebtRegisterInfo, assetType);
        if (StringUtils.isNotBlank(err_1)) {
            stringErr.append(err_1);
        }
        String err_2 = CheckAssetInfo.checkAsset_2(whiteregex, whitereForCode, assetDebtRegisterInfo, assetType);
        if (StringUtils.isNotBlank(err_2)) {
            stringErr.append(err_2);
        }
        String err_3 = CheckAssetInfo.checkAsset_3(whiteregex, whitereForCode, assetDebtRegisterInfo, assetType);
        if (StringUtils.isNotBlank(err_3)) {
            stringErr.append(err_3);
        }
        return stringErr.toString();
    }

    /**
     * 合法性校验--底层资产持仓
     *
     * @param whiteregex
     * @param whitereForCode
     * @param underAssetRegistInfo
     * @return
     * @throws Exception
     */
    public String underAssetRegistInfoCheckForVue(String whiteregex, String whitereForCode, UnderAssetRegistInfo underAssetRegistInfo) throws Exception {
        StringBuffer stringErr = new StringBuffer();
        Pattern whiterpattern1 = null;
        whiterpattern1 = Pattern.compile(whitereForCode);
        String value = "";

        value = StringUtils.isNotBlank(underAssetRegistInfo.getBankCode()) ? underAssetRegistInfo.getBankCode().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("发行机构代码要素不可为空。<br/>");
        } else if (value.length() > 6) {
            stringErr.append("发行机构代码要素过长" + value + "。<br/>");
        } else {
            String regex = "^[\\u0030-\\u0039\\u0041-\\u005A\\u0061-\\u007A]+$";
            Pattern whiterpattern = Pattern.compile(regex);
            if (!whiterpattern.matcher(value).matches()) {
                stringErr.append("发行机构代码要素格式不对正确的格式：6位英文或数字。<br/>");
            }
        }

        value = StringUtils.isNotBlank(underAssetRegistInfo.getAssetManagerCode()) ? underAssetRegistInfo.getAssetManagerCode().trim() : "";
        String under_code = StringUtils.isNotBlank(underAssetRegistInfo.getUnderAssetCode()) ? underAssetRegistInfo.getUnderAssetCode().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("对应资管及委外资产行内资产/负债编码要素不可为空。<br/>");
        } else if (value.length() > 40) {
            stringErr.append("对应资管及委外资产行内资产/负债编码过长。<br/>");
        } else if (under_code.equals(value)) {
            stringErr.append("对应资管及委外资产行内资产/负债编码和底层资产行内资产/负债编码不能相同。<br/>");
        } else {
            if (!whiterpattern1.matcher(value).matches()) {
                stringErr.append("对应资管及委外资产行内资产/负债编码只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。 <br/>");
            }
        }

        value = StringUtils.isNotBlank(underAssetRegistInfo.getAssetSumNumber()) ? underAssetRegistInfo.getAssetSumNumber().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("资管及委外资产当前总数量要素不可为空。<br/>");
        } else if (value.compareTo("0") == 0) {
            stringErr.append("资管及委外资产当前总数量要素必须大于0。<br/>");
        } else {
            stringErr.append(CheckDataUtils.checkMoney(value, "资管及委外资产当前总数量", "^(\\d{1,13}(\\.\\d{1,5})?)", "必须为n（18,5）格式", "1", "1"));
        }

        value = StringUtils.isNotBlank(underAssetRegistInfo.getConvertSumAmt()) ? underAssetRegistInfo.getConvertSumAmt().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("资管及委外资产当前总折算人民币金额（元）要素不可为空。<br/>");
        } else {
            stringErr.append(CheckDataUtils.checkMoney(value, "资管及委外资产当前总折算人民币金额（元）", "^(\\d{1,13}(\\.\\d{1,2})?)", "必须为n（15,2）格式", "1", "1"));
        }

        value = StringUtils.isNotBlank(underAssetRegistInfo.getNonInvestedAmt()) ? underAssetRegistInfo.getNonInvestedAmt().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("资管及委外资产未投资头寸（元）要素不可为空。<br/>");
        } else {
            stringErr.append(CheckDataUtils.checkMoney(value, "资管及委外资产未投资头寸（元）", "^(\\d{1,13}(\\.\\d{1,2})?)", "必须为n（15,2）格式", "1", "1"));
        }

        value = StringUtils.isNotBlank(underAssetRegistInfo.getUnderAssetCode()) ? underAssetRegistInfo.getUnderAssetCode().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("底层资产行内资产/负债编码要素不可为空。<br/>");
        } else if (value.length() > 40) {
            stringErr.append("底层资产行内资产/负债编码要素过长：" + underAssetRegistInfo.getUnderAssetCode() + "。<br/>");
        } else {
            if (!whiterpattern1.matcher(value).matches()) {
                stringErr.append("底层资产行内资产/负债编码只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。 <br/>");
            }
        }

        value = StringUtils.isNotBlank(underAssetRegistInfo.getUnderAssetSum()) ? underAssetRegistInfo.getUnderAssetSum().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("底层资产持仓数量要素不可为空。<br/>");
        } else {
            stringErr.append(CheckDataUtils.checkMoney(value, "底层资产持仓数量要素", "^(\\d{1,13}(\\.\\d{1,5})?)", "必须为n（18,5）格式", "1", "1"));
        }

        value = StringUtils.isNotBlank(underAssetRegistInfo.getUnderConvertSumAmt()) ? underAssetRegistInfo.getUnderConvertSumAmt().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("底层资产折算人民币市值(元)要素不可为空。<br/>");
        } else {
            stringErr.append(CheckDataUtils.checkMoney(value, "底层资产折算人民币市值(元)", "^(\\d{1,13}(\\.\\d{1,2})?)", "必须为n（15,2）格式", "1", "0"));
        }

        value = StringUtils.isNotBlank(underAssetRegistInfo.getReportDate()) ? underAssetRegistInfo.getReportDate().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("持仓日期要素不可为空。<br/>");
        } else {
            stringErr.append(CheckDataUtils.checkDate(value,"持仓日期"));
            if("".equals(CheckDataUtils.checkDate(value,"持仓日期"))){
                DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyyMMdd");
                LocalDate localDate = LocalDate.parse(value,dateTimeFormatter);
                if(!(localDate.getDayOfWeek()== DayOfWeek.SUNDAY) && !(localDate.getDayOfMonth()== localDate.lengthOfMonth())){
                    stringErr.append("持仓日期仅允许填写每周周日或每月最后一个自然日。<br/>");
                }

            }
//            Pattern p = Pattern.compile("^(\\d{8})");
//            Matcher m = p.matcher(value);
//            if (!m.matches()) {
//                stringErr.append("持仓日期要素必须为日期格式（YYYYMMDD）。<br/>");
//            } else if (!CheckDataUtils.isLegalDate(8, value, "yyyyMMdd")) {
//                stringErr.append("持仓日期要素必须为正确日期。<br/>");
//            }

        }

//		value = StringUtils.isNotBlank(underAssetRegistInfo.getRegisterSerno())?underAssetRegistInfo.getRegisterSerno().trim():"";
//		if(StringUtils.isBlank(value)){
//			stringErr.append("登记流水号要素不可为空。<br/>");
//		}else if (value.length()>32){
//			stringErr.append("登记流水号要素过长。<br/>");
//		}else{
//			whiterpattern1 = Pattern.compile(whitereForCode);
//			if(!whiterpattern1.matcher(value).matches()){
//				stringErr.append("登记流水号要素只能含有白名单内的阿拉伯数字，英文字母，半角符号。 ");}
//
//		}
        return stringErr.toString();
    }

    /**
     * 合法性校验--资产持仓
     *
     * @param whiteregex
     * @param whitereForCode
     * @param assetRegistInfo
     * @return
     * @throws Exception
     */
    public String assetRegistInfoCheckForVue(String whiteregex, String whitereForCode, AssetRegistInfo assetRegistInfo) throws Exception {
        StringBuffer stringErr = new StringBuffer();
        Pattern whiterpattern1 = null;
        whiterpattern1 = Pattern.compile(whitereForCode);
        String value = "";
        if (StringUtils.isBlank(assetRegistInfo.getBankCode())) {
            stringErr.append("发行机构代码要素不可为空。<br/>");
        } else if (StringUtils.isNotBlank(assetRegistInfo.getBankCode()) && assetRegistInfo.getBankCode().length() > 6) {
            stringErr.append("发行机构代码要素过长：" + assetRegistInfo.getBankCode() + "。<br/>");
        } else {
            String regex = "^[A-Za-z0-9]{6}$";
            Pattern whiterpattern = Pattern.compile(regex);
            if (StringUtils.isNotBlank(assetRegistInfo.getBankCode()) && !whiterpattern.matcher(assetRegistInfo.getBankCode()).matches()) {
                stringErr.append("发行机构代码要素格式不对正确的格式：6位英文或数字。<br/>");
            }
        }

        if (StringUtils.isBlank(assetRegistInfo.getProdRegEnc())) {
            stringErr.append("产品登记编码要素不可为空。<br/>");
        }else if (StringUtils.isNotBlank(assetRegistInfo.getProdRegEnc()) && CheckDataUtils.checkStringLengthOver(assetRegistInfo.getProdRegEnc(),15)) {
            stringErr.append("产品登记编码要素过长：" + assetRegistInfo.getProdRegEnc() + "。<br/>");
        } else {
            String regex = "^[A-Za-z0-9]{14,15}$";;
            Pattern whiterpattern = Pattern.compile(regex);
            if (!whiterpattern.matcher(assetRegistInfo.getProdRegEnc()).matches()) {
                stringErr.append("产品登记编码要素格式不对。正确的格式：14或15位英文或数字。<br/>");
            }
        }
        value = StringUtils.isNotBlank(assetRegistInfo.getAssetCode()) ? assetRegistInfo.getAssetCode().trim() : "";
        if (value.length() > 40) {
            stringErr.append("行内资产/负债编码要素过长：" + value + "。<br/>");
        } else if (StringUtils.isNotBlank(assetRegistInfo.getHoldingType()) && ("02,03").contains(assetRegistInfo.getHoldingType()) && StringUtils.isBlank(value)) {
            stringErr.append("当持仓类别为“02 登记系统资产”或“03 登记系统负债”时，行内资产/负债编码要素不可为空。<br/>");
        } else if (StringUtils.isNotBlank(assetRegistInfo.getHoldingType()) && ("01,04,05").contains(assetRegistInfo.getHoldingType()) && StringUtils.isNotBlank(value)) {
            stringErr.append("当“持仓类别”选择“01 现金及活期存款”、“04 其他资产”或“05 其他负债”时，行内资产/负债编码要素必须为空。<br/>");

        } else {
            if (!"".equals(whitereForCode)) {
                whiterpattern1 = Pattern.compile(whitereForCode);
                if (StringUtils.isNotBlank(value) && !whiterpattern1.matcher(value).matches()) {
                    stringErr.append("行内资产/负债编码只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。<br/>");
                }
            }
        }
        value = StringUtils.isNotBlank(assetRegistInfo.getInvestedAsset()) ? assetRegistInfo.getInvestedAsset().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("资产穿透情况要素不可为空。<br/>");
        } else if (!("01,02,03").contains(value)) {
            stringErr.append("资产穿透情况要素不在值域范围内。<br/>");
        }

        value = StringUtils.isNotBlank(assetRegistInfo.getMezzanineNumber()) ? assetRegistInfo.getMezzanineNumber().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("中间层数要素不可为空。<br/>");
        } else {
            String regex = "^\\d{0,5}$";
            Pattern whiterpattern = Pattern.compile(regex);
            if (!whiterpattern.matcher(value).matches()) {
                stringErr.append("中间层数必须大于等于0，且为n..5格式。<br/>");
            }
        }

        value = StringUtils.isNotBlank(assetRegistInfo.getMezzanineAssetCode()) ? assetRegistInfo.getMezzanineAssetCode() : "";
        String mezzanineNumber = StringUtils.isNotBlank(assetRegistInfo.getMezzanineNumber()) ? assetRegistInfo.getMezzanineNumber() : "";
        if (StringUtils.isNotBlank(value) && "0".equals(mezzanineNumber)) {
            stringErr.append("中间层数为0时，中间层行内资产/负债编码不可填写。<br/>");
        } else if (value.length() > 200) {
            stringErr.append("中间层行内资产/负债编码过长。<br/>");
        } else if (StringUtils.isBlank(value) && !"0".equals(mezzanineNumber)) {
            stringErr.append("中间层数不为0时，中间层行内资产/负债编码必填。<br/>");
        } else if (!"0".equals(mezzanineNumber) && StringUtils.isNotBlank(value)) {
            String[] codes = value.split(",");
            Set set = new HashSet<>();
            for (int i = 0; i < codes.length; i++) {
                if("".equals(codes[i])){
                    stringErr.append("中间层行内资产/负债编码内分隔符间的行内资产/负债编码不能为空。<br/>");
                }
                set.add(codes[i]);
            }
            if (new BigDecimal(mezzanineNumber).compareTo(new BigDecimal(codes.length))!=0) {
                stringErr.append("中间层行内资产/负债编码数量应与中间层数相等。<br/>");
            } else if (codes.length != set.size()) {
                stringErr.append("中间层行内资产/负债编码内分隔符间的行内资产/负债编码不可重复。<br/>");
            }
        } else {
            if (!"".equals(whitereForCode) && StringUtils.isNotBlank(value)) {
                whiterpattern1 = Pattern.compile(whitereForCode);
                if (!whiterpattern1.matcher(value).matches()) {
                    stringErr.append("中间层行内资产/负债编码只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。<br/>");
                }
            }
        }

        Pattern whiterpattern = null;
        if (!"".equals(whiteregex)) {
            whiterpattern = Pattern.compile(whiteregex);
        }
        value = StringUtils.isNotBlank(assetRegistInfo.getAccountCode()) ? assetRegistInfo.getAccountCode().trim() : "";
        String holdingType = StringUtils.isNotBlank(assetRegistInfo.getHoldingType()) ? assetRegistInfo.getHoldingType() : "";
        if (StringUtils.isNotBlank(value) && CheckDataUtils.checkStringLengthOver(value,200)) {
            stringErr.append("会计科目名称要素过长：" + value + "。<br/>");
        } else if (("01,04,05").contains(holdingType) && StringUtils.isBlank(value)) {
            stringErr.append("当持仓类别为“01 现金及活期存款”、“04 其他资产”或“05 其他负债”时，会计科目名称要素不可为空。<br/>");
        } else {
            if (!whiterpattern.matcher(value).matches() && StringUtils.isNotBlank(value)) {
                stringErr.append("会计科目名称必须填写白名单中的文字。<br/>");
            }
        }

        value = StringUtils.isNotBlank(assetRegistInfo.getInvestedAmount()) ? assetRegistInfo.getInvestedAmount() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("金额要素不可为空。<br/>");
        } else {
            stringErr.append(CheckDataUtils.checkMoney(value, "金额", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "1", "1"));
        }
        value = StringUtils.isNotBlank(assetRegistInfo.getInvestedAmountCny()) ? assetRegistInfo.getInvestedAmountCny() : "";
        String cny = StringUtils.isNotBlank(assetRegistInfo.getCny()) ? assetRegistInfo.getCny().trim() : "";
        String amount = StringUtils.isNotBlank(assetRegistInfo.getInvestedAmount()) ? assetRegistInfo.getInvestedAmount() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("折算人民币金额要素不可为空。<br/>");
        }else if (cny.equals("CNY") && StringUtils.isNotBlank(amount)&&!amount.equals(value)) {
            stringErr.append("当币种为人民币(CNY)时，折算人民币金额与金额必须相等。<br/>");
        } else {
            stringErr.append(CheckDataUtils.checkMoney(value, "折算人民币金额", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "1", "1"));
        }
        value = StringUtils.isNotBlank(assetRegistInfo.getFairValue()) ? assetRegistInfo.getFairValue().trim() : "";
        stringErr.append(CheckDataUtils.checkMoney(value, "公允价值", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "0", "1"));

        value = StringUtils.isNotBlank(assetRegistInfo.getFairValueCny()) ? assetRegistInfo.getFairValueCny().trim() : "";
        stringErr.append(CheckDataUtils.checkMoney(value, "折算人民币公允价值", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "0", "1"));

        value = StringUtils.isNotBlank(assetRegistInfo.getNetValuation()) ? assetRegistInfo.getNetValuation().trim() : "";

        if (("02,03").contains(holdingType)) {
            stringErr.append(CheckDataUtils.checkMoney(value, "单位估值(净价)", "^(\\d{1,13}(\\.\\d{1,4})?)", "必须为n（17,4）格式", "1", "1"));
        }

        value = StringUtils.isNotBlank(assetRegistInfo.getFlValuation()) ? assetRegistInfo.getFlValuation().trim() : "";
        String netValuation = StringUtils.isNotBlank(assetRegistInfo.getNetValuation()) ? assetRegistInfo.getNetValuation() : "";
        if (("02,03").contains(holdingType)) {
            stringErr.append(CheckDataUtils.checkMoney(value, "单位估值(全价)", "^(\\d{1,13}(\\.\\d{1,4})?)", "必须为n（17,4）格式", "1", "1"));
        } else if (StringUtils.isNotBlank(value) && StringUtils.isNotBlank(netValuation)) {
            BigDecimal big_value = new BigDecimal(value);
            BigDecimal big_netValuation = new BigDecimal(netValuation);
            if (big_value.compareTo(big_netValuation) < 0) {
                stringErr.append("单位估值（全价）必须大于等于单位估值（净价）。<br/>");
            }
        }
        value = StringUtils.isNotBlank(assetRegistInfo.getQuantity()) ? assetRegistInfo.getQuantity().trim() : "";
        if (("02,03").contains(holdingType)) {
            stringErr.append(CheckDataUtils.checkMoney(value, "数量", "^(\\d{1,13}(\\.\\d{1,5})?)", "必须为n（18,5）格式", "1", "1"));
        } else if (StringUtils.isNotBlank(value)) {
            if (value.trim().compareTo("0") == 0) {
                stringErr.append("数量必须大于等于0。<br/>");
            }
        }
        value = StringUtils.isNotBlank(assetRegistInfo.getHoldingDate()) ? assetRegistInfo.getHoldingDate().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("持仓日期要素不可为空。<br/>");
        } else {
            stringErr.append(CheckDataUtils.checkDate(value,"持仓日期"));
//            Pattern p = Pattern.compile("^(\\d{8})");
//            Matcher m = p.matcher(value);
//            if (!m.matches()) {
//                stringErr.append("持仓日期要素必须为日期格式（YYYYMMDD）。<br/>");
//            } else if (!CheckDataUtils.isLegalDate(8, value, "yyyyMMdd")) {
//                stringErr.append("持仓日期要素必须为正确日期。<br/>");
//            }
        }
        value = StringUtils.isNotBlank(assetRegistInfo.getDetails()) ? assetRegistInfo.getDetails() : "";
        if (CheckDataUtils.checkStringLengthOver(value,256)) {
            stringErr.append("备注过长。<br/>");
        } else if (StringUtils.isNotBlank(value)) {
            if (!whiterpattern.matcher(value).matches() && StringUtils.isNotBlank(value)) {
                stringErr.append("备注必须填写白名单中的文字。<br/>");
            }
        }
//		value = StringUtils.isNotBlank(assetRegistInfo.getRegisterSerno())?assetRegistInfo.getRegisterSerno().trim():"";
//		if(StringUtils.isBlank(value)){
//			stringErr.append("登记流水号要素不可为空。<br/>");
//		}else if (value.length()>32){
//			stringErr.append("登记流水号要素过长。<br/>");
//		}else{
//			whiterpattern1 = Pattern.compile(whitereForCode);
//			if(!whiterpattern1.matcher(value).matches()){
//				stringErr.append("登记流水号要素只能含有白名单内的阿拉伯数字，英文字母，半角符号。 ");}
//
//		}
        return stringErr.toString();

    }


    /**
     * 合法性校验--交易信息登记
     *
     * @param whiteregex
     * @param whitereForCode
     * @param prodTransRegistInfo
     * @return
     * @throws Exception
     */
    public String prodTransRegistInfoCheckForVue(String whiteregex, String whitereForCode, ProdTransRegistInfo prodTransRegistInfo) throws Exception {
        StringBuffer stringErr = new StringBuffer();
        Pattern whiterpattern1 = null;
        whiterpattern1 = Pattern.compile(whitereForCode);

        Pattern whiterpattern = null;
        if (!"".equals(whiteregex)) {
            whiterpattern = Pattern.compile(whiteregex);
        }
        String value = "";
        value = StringUtils.isNotBlank(prodTransRegistInfo.getBankCode()) ? prodTransRegistInfo.getBankCode() : "";

        if (StringUtils.isBlank(value)) {
            stringErr.append("发行机构代码要素不可为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && value.length() > 6) {
            stringErr.append("发行机构代码要素过长：" + value + "<br/>");
        } else {
            String regex = "^[\\u0030-\\u0039\\u0041-\\u005A\\u0061-\\u007A]+$";
            Pattern whiterpattern2 = Pattern.compile(regex);
            if (StringUtils.isNotBlank(value) && !whiterpattern.matcher(value).matches()) {
                stringErr.append("发行机构代码要素格式不对正确的格式：6位英文或数字。<br/>");
            }
        }
        value = StringUtils.isNotBlank(prodTransRegistInfo.getProdCode()) ? prodTransRegistInfo.getProdCode().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("产品登记编码要素不可为空。<br/>");
        } else if (value.length() > 15) {
            stringErr.append("产品登记编码要素过长。<br/>");
        } else {
            String regex = "^[A-Za-z0-9]{14,15}$";
            Pattern whiterpattern3 = Pattern.compile(regex);
            if (!whiterpattern.matcher(value).matches()) {
                stringErr.append("产品登记编码要素格式不对。正确的格式：14或15位英文或数字。<br/>");
            }
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getTransCode()) ? prodTransRegistInfo.getTransCode().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("行内交易编码要素不可为空。<br/>");
        } else if (value.length() > 32) {
            stringErr.append("行内交易编码要素过长。<br/>");
        } else {
            whiterpattern1 = Pattern.compile(whitereForCode);
            if (!whiterpattern1.matcher(value).matches()) {
                stringErr.append("行内交易编码要素只能含有白名单内的阿拉伯数字，英文字母，半角符号。 <br/>");
            }
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getAssetCode()) ? prodTransRegistInfo.getAssetCode().trim() : "";
        if (StringUtils.isNotBlank(prodTransRegistInfo.getCashType()) && ("01,02,03,04,05,06,07,08,09,10").contains(prodTransRegistInfo.getCashType()) && StringUtils.isBlank(value)) {
            stringErr.append("当资金流动类型为资产买入时，行内资产/负债编码要素不可为空。<br/>");
        } else if (value.length() > 40) {
            stringErr.append("行内资产/负债编码要素过长：" + value + "。<br/>");
        } else {
            if (StringUtils.isNotBlank(value) && !whiterpattern1.matcher(value).matches()) {
                stringErr.append("行内资产/负债编码只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。<br/>");
            }
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getAmt()) ? prodTransRegistInfo.getAmt().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("发生金额要素不可为空。<br/>");
        } else {
            stringErr.append(CheckDataUtils.checkMoney(value, "发生金额", "^(\\d{1,13}(\\.\\d{1,2})?)", "必须为n（15,2）格式", "1", "1"));
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getConvertRmb()) ? prodTransRegistInfo.getConvertRmb().trim() : "";
        String amt = StringUtils.isNotBlank(prodTransRegistInfo.getAmt()) ? prodTransRegistInfo.getAmt().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("折算人民币金额（元）要素不可为空。<br/>");
        } else if (StringUtils.isNotBlank(prodTransRegistInfo.getCur()) && prodTransRegistInfo.getCur().equals("CNY") && !value.equals(amt)) {
            stringErr.append("当发生金额的币种为人民币(CNY)时，折算人民币金额（元）与发生金额必须相等。<br/>");
        } else {
            stringErr.append(CheckDataUtils.checkMoney(value, "折算人民币金额（元）", "^(\\d{1,13}(\\.\\d{1,2})?)", "必须为n（15,2）格式", "1", "1"));
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getQuantity()) ? prodTransRegistInfo.getQuantity().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("数量要素不可为空。<br/>");
        } else {
            stringErr.append(CheckDataUtils.checkMoney(value, "数量", "^(\\d{1,13}(\\.\\d{1,5})?)", "必须为n（18,5）格式", "1", "1"));
        }
        value = StringUtils.isNotBlank(prodTransRegistInfo.getMethodAssetMeasure()) ? prodTransRegistInfo.getMethodAssetMeasure().trim() : "";
        String cash_type = StringUtils.isNotBlank(prodTransRegistInfo.getCashType()) ? prodTransRegistInfo.getCashType().trim() : "";
        if (("01,02,03,04,05,06,07,08").contains(cash_type) && StringUtils.isBlank(value)) {
            stringErr.append("当资金流动类型为资产买入时，资产计量方式要素不可为空。<br/>");
        } else if (!("01,02,03,04,05,06,07,08").contains(cash_type) && StringUtils.isNotBlank(value)) {
            stringErr.append("当资金流动类型为投资资产所得收益时，资产计量方式要素必须为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && !("01,02").contains(value)) {
            stringErr.append("资产计量方式不在值域范围内。<br/>");
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getCashType()) ? prodTransRegistInfo.getCashType().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("资金流动类型要素不可为空。<br/>");
        } else if (!("01,02,03,04,05,06,07,08,09,10,11,12").contains(value)) {
            stringErr.append("资金流动类型要素不在值域范围内。<br/>");
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getDetailCashType()) ? prodTransRegistInfo.getDetailCashType().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("资金流动类型说明要素不可为空。<br/>");
        } else if (CheckDataUtils.checkStringLengthOver(value,60)) {
            stringErr.append("资金流动类型说明过长。<br/>");
        } else {
            if (!whiterpattern.matcher(value).matches()) {
                stringErr.append("资金流动类型说明必须填写白名单中的文字。<br/>");
            }
        }
        value = StringUtils.isNotBlank(prodTransRegistInfo.getTradeDate()) ? prodTransRegistInfo.getTradeDate().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("交易日要素不可为空。<br/>");
        } else {
            stringErr.append(CheckDataUtils.checkDate(value,"交易日要素"));
            if("".equals(CheckDataUtils.checkDate(value,"交易日要素"))){
                stringErr.append(CheckDataUtils.checkDateAfer(value,"交易日要素"));
            }
//            Pattern p = Pattern.compile("^(\\d{8})");
//            Matcher m = p.matcher(value);
//            if (!m.matches()) {
//                stringErr.append("交易日要素必须为日期格式（YYYYMMDD）。<br/>");
//            } else if (!CheckDataUtils.isLegalDate(8, value, "yyyyMMdd")) {
//                stringErr.append("交易日要素必须为正确日期。<br/>");
//            }
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getTrxTm()) ? prodTransRegistInfo.getTrxTm().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("交易发起时间要素不可为空。<br/>");
        } else {
            Pattern p = Pattern.compile("^(\\d{14})");
            Matcher m = p.matcher(value);
            if (!m.matches()) {
                stringErr.append("交易发起时间要素必须为日期格式（YYYYMMDDHHMMSS）。<br/>");
            } else{
                p = Pattern.compile("^(([0-9]{4})((0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])([01][0-9]|2[0-3])([0-5][0-9]){2}))");
                m = p.matcher(value);
                if(!m.matches()){
                    stringErr.append("交易发起时间要素必须为正确时间。<br/>");
                }

            }
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getTradeCounter()) ? prodTransRegistInfo.getTradeCounter().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("交易对手方要素不可为空。<br/>");
        } else if (value.length() > 200) {
            stringErr.append("交易对手方要素过长。<br/>");
        } else {
            if (!whiterpattern.matcher(value).matches()) {
                stringErr.append("交易对手方要素必须填写白名单中的文字。<br/>");
            }
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getRelatedPartyTrans()) ? prodTransRegistInfo.getRelatedPartyTrans().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("关联交易情况要素不可为空。<br/>");
        } else if (!("01,02,03,04,05,99").contains(value)) {
            stringErr.append("关联交易情况要素不在值域范围内。<br/>");
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getCounterType()) ? prodTransRegistInfo.getCounterType().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("交易对手方类型要素不可为空。<br/>");
        } else if (!("01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,99").contains(value)) {
            stringErr.append("交易对手方类型要素不在值域范围内。<br/>");
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getUnitPriceFull()) ? prodTransRegistInfo.getUnitPriceFull().trim() : "";
        String net_price = StringUtils.isNotBlank(prodTransRegistInfo.getUnitPriceNet()) ? prodTransRegistInfo.getUnitPriceNet().trim() : "";
        if (StringUtils.isBlank(value) && ("01,02").contains(cash_type)) {
            stringErr.append("当资金流动类型为资产买入时，单位成交价格（全价）要素不可为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && StringUtils.isNotBlank(net_price) && new BigDecimal(value).compareTo(new BigDecimal(net_price)) < 0) {
            stringErr.append("单位成交价格（全价）必须大于等于单位成交价格（净价）。<br/>");
        } else {
            stringErr.append(CheckDataUtils.checkMoney(value, "单位成交价格（全价）", "^(\\d{1,13}(\\.\\d{1,4})?)", "必须为n（17,4）格式", "0", "1"));
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getUnitPriceNet()) ? prodTransRegistInfo.getUnitPriceNet().trim() : "";
        if (StringUtils.isBlank(value) && ("01,02").contains(cash_type)) {
            stringErr.append("当资金流动类型为资产买入时，单位成交价格（净价）要素不可为空。<br/>");
        } else {
            stringErr.append(CheckDataUtils.checkMoney(value, "单位成交价格（净价）", "^(\\d{1,13}(\\.\\d{1,4})?)", "必须为n（17,4）格式", "0", "1"));
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getRateAnnualReturnStr()) ? prodTransRegistInfo.getRateAnnualReturnStr().trim() : "";
        if (StringUtils.isNotBlank(value)) {
            stringErr.append(CheckDataUtils.checkMoney(value, "到期收益率", "^(\\d{1}(\\.\\d{1,7})?)", "必须为n（8,5）格式", "0", "1"));
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getTransIdentCode()) ? prodTransRegistInfo.getTransIdentCode().trim() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("成交编号/合同号要素不可为空。<br/>");
        } else if (value.length() > 100) {
            stringErr.append("成交编号/合同号要素过长。<br/>");
        } else {
            if (!whiterpattern.matcher(value).matches()) {
                stringErr.append("成交编号/合同号要素必须填写白名单中的文字。<br/>");
            }
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getTransApproveId()) ? prodTransRegistInfo.getTransApproveId().trim() : "";
        if (StringUtils.isBlank(value) && ("01,02,03,04,05,06").contains(cash_type)) {
            stringErr.append("交易审批人身份证号要素不可为空。<br/>");
        } else if (value.length() > 30) {
            stringErr.append("交易审批人身份证号要素过长。<br/>");
        } else if (StringUtils.isNotBlank(value)) {
            if(!whiterpattern.matcher(value).matches()){
                stringErr.append("交易审批人身份证号要素必须填写白名单中的文字。<br/>");
            }
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getTransApproveName()) ? prodTransRegistInfo.getTransApproveName().trim() : "";
        if (StringUtils.isBlank(value) && ("01,02,03,04,05,06").contains(cash_type)) {
            stringErr.append("交易审批人姓名要素不可为空。<br/>");
        } else if (CheckDataUtils.checkStringLengthOver(value,200)) {
            stringErr.append("交易审批人姓名要素过长。<br/>");
        } else if (StringUtils.isNotBlank(value)) {
            if (!whiterpattern.matcher(value).matches()) {
                stringErr.append("交易审批人姓名要素必须填写白名单中的文字。<br/>");
            }
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getTraderId()) ? prodTransRegistInfo.getTraderId().trim() : "";
        if (StringUtils.isBlank(value) && ("01,02,03,04,05,06").contains(cash_type)) {
            stringErr.append("交易员身份证号要素不可为空。<br/>");
        } else if (value.length() > 30) {
            stringErr.append("交易员身份证号要素过长。<br/>");
        } else if (StringUtils.isNotBlank(value)) {
            if (!whiterpattern.matcher(value).matches()) {
                stringErr.append("交易员身份证号要素必须填写白名单中的文字。<br/>");
            }
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getTraderName()) ? prodTransRegistInfo.getTraderName().trim() : "";
        if (StringUtils.isBlank(value) && ("01,02,03,04,05,06").contains(cash_type)) {
            stringErr.append("交易员姓名要素不可为空。<br/>");
        } else if (CheckDataUtils.checkStringLengthOver(value,200)) {
            stringErr.append("交易员姓名要素过长。<br/>");
        } else if (StringUtils.isNotBlank(value)) {
            if (!whiterpattern.matcher(value).matches()) {
                stringErr.append("交易员姓名要素必须填写白名单中的文字。<br/>");
            }
        }

        value = StringUtils.isNotBlank(prodTransRegistInfo.getDetails()) ? prodTransRegistInfo.getDetails() : "";
        String counter_type = StringUtils.isNotBlank(prodTransRegistInfo.getCounterType()) ? prodTransRegistInfo.getCounterType() : "";
        String relation_type = StringUtils.isNotBlank(prodTransRegistInfo.getRelatedPartyTrans()) ? prodTransRegistInfo.getRelatedPartyTrans().trim() : "";
        if (StringUtils.isBlank(value) && ("99").equals(relation_type)) {
            stringErr.append("当关联交易情况为其他关联交易情况时，备注要素不可为空。<br/>");
        }
        if (StringUtils.isBlank(value) && ("99").equals(counter_type)) {
            stringErr.append("当交易对手方类型为其他时，备注要素不可为空。<br/>");
        } else if (CheckDataUtils.checkStringLengthOver(value,256)) {
            stringErr.append("备注过长。<br/>");
        } else if (StringUtils.isNotBlank(value)) {
            if (!whiterpattern.matcher(value).matches() && StringUtils.isNotBlank(value)) {
                stringErr.append("备注必须填写白名单中的文字。<br/>");
            }
        }
//		value = StringUtils.isNotBlank(prodTransRegistInfo.getRegisterSerno())?prodTransRegistInfo.getRegisterSerno().trim():"";
//		if(StringUtils.isBlank(value)){
//			stringErr.append("登记流水号要素不可为空。<br/>");
//		}else if (value.length()>32){
//			stringErr.append("登记流水号要素过长。<br/>");
//		}else{
//			whiterpattern1 = Pattern.compile(whitereForCode);
//			if(!whiterpattern1.matcher(value).matches()){
//				stringErr.append("登记流水号要素只能含有白名单内的阿拉伯数字，英文字母，半角符号。 ");}
//
//		}
        return stringErr.toString();

    }

    /**
     * 合法性校验--交易信息登记
     *
     * @param prodTransRegistInfo
     * @return
     * @throws Exception
     */
    public String prodTransRegistInfoCheckForImport(ProdTransRegistInfo prodTransRegistInfo) throws Exception {
        StringBuffer stringErr = new StringBuffer();
        if(StringUtils.isNotBlank(prodTransRegistInfo.getCounterType())){
            String errDesc = CheckDataUtils.checkDictValue("subm_counterparty_type",prodTransRegistInfo.getCounterType().split(" ")[0],prodTransRegistInfo.getCounterType());
            if(StringUtils.isNotBlank(errDesc)){
                stringErr.append("交易对手类型："+errDesc);
            }
        }

        return stringErr.toString();

    }


    /**
     * 合法性校验--交易信息登记
     *
     * @param whiteregex
     * @param whitereForCode
     * @param trCustRegisterInfo
     * @return
     * @throws Exception
     */
    public String prodTrCustRegisterInfoCheckForVue(String whiteregex, String whitereForCode, TrCustRegisterInfo trCustRegisterInfo) throws Exception {
        StringBuffer stringErr = new StringBuffer();
        Pattern whiterpattern1 = null;
        whiterpattern1 = Pattern.compile(whitereForCode);

        Pattern whiterpattern = null;
        if (!"".equals(whiteregex)) {
            whiterpattern = Pattern.compile(whiteregex);
        }
        String value = "";
        //发行机构代码
        value = StringUtils.isNotBlank(trCustRegisterInfo.getBankCode()) ? trCustRegisterInfo.getBankCode() : "";

        String bank_code_s = CheckDataParams.bankCode;

        if (StringUtils.isBlank(value)) {
            stringErr.append("发行机构代码要素不可为空。<br/>");
        } else {
            Pattern p = Pattern.compile("^([Z]{1}\\d{5})");
            Matcher m = p.matcher(value.trim());
            boolean flagBankCode = m.matches();
            if (!bank_code_s.equals(value)) {
                stringErr.append("发行机构代码必须与银行代码相同。<br/>");
            } else if (!flagBankCode) {
                stringErr.append("发行机构代码要素格式不对。正确的格式：6位英文或数字。<br/>");
            }
        }

        //该投资者是否属于本机构
        value = StringUtils.isNotBlank(trCustRegisterInfo.getIsBelong()) ? trCustRegisterInfo.getIsBelong() : "";
        String data_type = StringUtils.isNotBlank(trCustRegisterInfo.getDataType()) ? trCustRegisterInfo.getDataType() : "";
        if (StringUtils.isNotBlank(value) && ("02").equals(data_type)) {
            stringErr.append("当数据类型为识别标识变更时，该投资者是否属于本机构要素必须为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("04").equals(data_type)) {
            stringErr.append("当数据类型为其他信息变更时，该投资者是否属于本机构要素必须为空。<br/>");
        }
        //投资者所属银行名称
        value = StringUtils.isNotBlank(trCustRegisterInfo.getIssBankName()) ? trCustRegisterInfo.getIssBankName() : "";
        if (StringUtils.isNotBlank(value) && ("02").equals(data_type)) {
            stringErr.append("当数据类型为识别标识变更时，该投资者所属银行名称要素必须为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("04").equals(data_type)) {
            stringErr.append("当数据类型为其他信息变更时，该投资者所属银行名称要素必须为空。<br/>");
        } else if (CheckDataUtils.checkStringLengthOver(value,60)) {
            stringErr.append("投资者所属银行名称过长。<br/>");
        } else if (StringUtils.isNotBlank(value)) {
            if (!whiterpattern.matcher(value).matches()) {
                stringErr.append("投资者所属银行名称要素必须填写白名单中的文字。<br/>");
            }
        }

        //投资者所属银行代码
        value = StringUtils.isNotBlank(trCustRegisterInfo.getIssBankCode()) ? trCustRegisterInfo.getIssBankCode() : "";
        if (StringUtils.isNotBlank(value) && ("02").equals(data_type)) {
            stringErr.append("当数据类型为识别标识变更时，该投资者所属银行代码要素必须为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("04").equals(data_type)) {
            stringErr.append("当数据类型为其他信息变更时，该投资者所属银行代码要素必须为空。<br/>");
        } else if (value.length() > 60) {
            stringErr.append("投资者所属银行代码过长。<br/>");
        } else if (StringUtils.isNotBlank(value)) {
            if (!whiterpattern.matcher(value).matches()) {
                stringErr.append("投资者所属银行代码要素必须填写白名单中的文字。<br/>");
            }
        }

        //投资者境内外标识
        value = StringUtils.isNotBlank(trCustRegisterInfo.getInOutSign()) ? trCustRegisterInfo.getInOutSign() : "";
        if (StringUtils.isBlank(value) && ("01").equals(data_type)) {
            stringErr.append("当数据类型为新增时，投资者境内外标识要素不可为空。<br/>");
        } else if (StringUtils.isBlank(value) && ("03").equals(data_type)) {
            stringErr.append("当数据类型为其他信息变更时，投资者所属银行代码要素不可为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("02").equals(data_type)) {
            stringErr.append("当数据类型为识别标识变更时，投资者境内外标识要素必须为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("04").equals(data_type)) {
            stringErr.append("当数据类型为其他信息变更时，投资者境内外标识要素必须为空。<br/>");
        }
        //投资者所属国家或地区
        value = StringUtils.isNotBlank(trCustRegisterInfo.getIssCountry()) ? trCustRegisterInfo.getIssCountry() : "";
        if (StringUtils.isBlank(value) && ("01").equals(data_type)) {
            stringErr.append("当数据类型为新增时，投资者所属国家或地区要素不可为空。<br/>");
        } else if (StringUtils.isBlank(value) && ("03").equals(data_type)) {
            stringErr.append("当数据类型为其他信息变更时，投资者所属国家或地区要素不可为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("02").equals(data_type)) {
            stringErr.append("当数据类型为识别标识变更时，投资者所属国家或地区要素必须为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("04").equals(data_type)) {
            stringErr.append("当数据类型为其他信息变更时，投资者所属国家或地区要素必须为空。<br/>");
        }

        //数据类型
        value = StringUtils.isNotBlank(trCustRegisterInfo.getDataType()) ? trCustRegisterInfo.getDataType() : "";
        if (StringUtils.isBlank(value)) {
            stringErr.append("数据类型要素不可为空。<br/>");
        }

        //原识别标识
        value = StringUtils.isNotBlank(trCustRegisterInfo.getOriCustNo()) ? trCustRegisterInfo.getOriCustNo() : "";

        if (StringUtils.isBlank(value) && ("02").equals(data_type)) {
            stringErr.append("当数据类型为识别标识变更时，原识别标识要素不可为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("01").equals(data_type)) {
            stringErr.append("当数据类型为新增时，原识别标识要素必须为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("03").equals(data_type)) {
            stringErr.append("当数据类型为重要信息变更时，原识别标识要素必须为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("04").equals(data_type)) {
            stringErr.append("当数据类型为其他信息变更时，原识别标识要素必须为空。<br/>");
        } else if (value.length() > 30) {
            stringErr.append("原识别标识过长。<br/>");
        } else if (StringUtils.isNotBlank(value)) {
            if (!whiterpattern1.matcher(value).matches()) {
                stringErr.append("原识别标识只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。<br/>");
            }
        }
        //识别标识
        value = StringUtils.isNotBlank(trCustRegisterInfo.getCustNo()) ? trCustRegisterInfo.getCustNo() : "";
        String ori_cust_no = StringUtils.isNotBlank(trCustRegisterInfo.getOriCustNo()) ? trCustRegisterInfo.getOriCustNo() : "";

        if (StringUtils.isBlank(value)) {
            stringErr.append("数据类型要素不可为空。<br/>");
        } else if ((value).equals(ori_cust_no) && ("02").equals(data_type)) {
            stringErr.append("原识别标识和识别标识不能相同。<br/>");
        } else if (value.length() > 30) {
            stringErr.append("识别标识过长。<br/>");
        } else {
            if (!whiterpattern1.matcher(value).matches()) {
                stringErr.append("原识别标识只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。<br/>");
            }
        }

        //投资者类别
        value = StringUtils.isNotBlank(trCustRegisterInfo.getCustType()) ? trCustRegisterInfo.getCustType() : "";
        if (StringUtils.isBlank(value) && ("01").equals(data_type)) {
            stringErr.append("当数据类型为新增时，投资者类别要素不可为空。<br/>");
        } else if (StringUtils.isBlank(value) && ("03").equals(data_type)) {
            stringErr.append("当数据类型为重要信息变更时，投资者类别要素不可为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("02").equals(data_type)) {
            stringErr.append("当数据类型为识别标识变更时，投资者类别要素必须为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("04").equals(data_type)) {
            stringErr.append("当数据类型为其他信息变更时，投资者类别要素必须为空。<br/>");
        } else {
            if (!whiterpattern1.matcher(value).matches()) {
                stringErr.append("原识别标识只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。<br/>");
            }
        }

        //机构证件类别
        String cust_type = StringUtils.isNotBlank(trCustRegisterInfo.getCustType()) ? trCustRegisterInfo.getCustType() : "";

        value = StringUtils.isNotBlank(trCustRegisterInfo.getOrganizationIdType()) ? trCustRegisterInfo.getOrganizationIdType() : "";
        if (StringUtils.isBlank(value) && ("01").equals(data_type) && ("04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27").contains(cust_type)) {
            stringErr.append("当数据类型为新增时，投资者类别要素不可为空。<br/>");
        } else if (StringUtils.isBlank(value) && ("03").equals(data_type) && ("04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27").contains(cust_type)) {
            stringErr.append("当数据类型为重要信息变更、投资者类别为银行时，机构证件类别要素不可为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("01").equals(data_type) && ("01,02,03").contains(cust_type)) {
            stringErr.append("当数据类型为新增、投资者类别为普通个人时，机构证件类别要素必须为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("03").equals(data_type) && ("01,02,03").contains(cust_type)) {
            stringErr.append("当数据类型为重要信息变更、投资者类别为普通个人时，机构证件类别要素必须为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("02").equals(data_type)) {
            stringErr.append("当数据类型为识别标识变更时，机构证件类别要素必须为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("01").equals(data_type) && ("17,18,19,20,21,22,23,27").contains(cust_type) && !("34".equals(value))) {
            stringErr.append("当数据类型为新增、投资者类别为信托产品时，机构证件类别只能填写SPV登记编码。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("03").equals(data_type) && ("17,18,19,20,21,22,23,27").contains(cust_type) && !("34".equals(value))) {
            stringErr.append("当数据类型为重要信息变更、投资者类别为信托产品时，机构证件类别只能填写SPV登记编码。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("01").equals(data_type) && ("04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26").contains(cust_type) && ("34".equals(value))) {
            stringErr.append("当数据类型为新增、投资者类别为银行时，机构证件类别不能填写SPV登记编码。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("03").equals(data_type) && ("04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26").contains(cust_type) && ("34".equals(value))) {
            stringErr.append("当数据类型为重要信息变更、投资者类别为银行时，机构证件类别不能填写SPV登记编码。<br/>");
        }
        //个人证件类别
        String in_out_sign = StringUtils.isNotBlank(trCustRegisterInfo.getInOutSign()) ? trCustRegisterInfo.getInOutSign() : "";
        value = StringUtils.isNotBlank(trCustRegisterInfo.getPersonalIdType()) ? trCustRegisterInfo.getPersonalIdType() : "";
        if (StringUtils.isBlank(value) && ("01").equals(data_type) && ("01，02，03").contains(cust_type)) {
            stringErr.append("当数据类型为新增、投资者类别为普通个人时，个人证件类别要素不可为空。<br/>");
        } else if (StringUtils.isBlank(value) && ("03").equals(data_type) && ("01，02，03").contains(cust_type)) {
            stringErr.append("当数据类型为重要信息变更、投资者类别为普通个人时，个人证件类别要素不可为空。<br/>");
        }
        if (StringUtils.isNotBlank(value) && ("01").equals(data_type) && ("04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27").contains(cust_type)) {
            stringErr.append("当数据类型为新增、投资者类别为银行时，个人证件类别要素必须为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("03").equals(data_type) && ("04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27").contains(cust_type)) {
            stringErr.append("当数据类型为重要信息变更、投资者类别为银行时，个人证件类别要素必须为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("02").equals(data_type)) {
            stringErr.append("当数据类型为识别标识变更时，个人证件类别要素必须为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("04").equals(data_type)) {
            stringErr.append("当数据类型为其他信息变更时，个人证件类别要素必须为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("02").equals(in_out_sign) && ("01,").equals(data_type) && ("01,02,03").contains(cust_type)) {
            stringErr.append("当数据类型为新增、投资者类别为普通个人时，机构证件类别要素必须为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("03").equals(data_type) && ("01,02,03").contains(cust_type)) {
            stringErr.append("当数据类型为重要信息变更、投资者类别为普通个人时，机构证件类别要素必须为空。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("01").equals(data_type) && ("17,18,19,20,21,22,23,27").contains(cust_type) && !("34".equals(value))) {
            stringErr.append("当数据类型为新增、投资者类别为信托产品时，机构证件类别只能填写SPV登记编码。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("03").equals(data_type) && ("17,18,19,20,21,22,23,27").contains(cust_type) && !("34".equals(value))) {
            stringErr.append("当数据类型为重要信息变更、投资者类别为信托产品时，机构证件类别只能填写SPV登记编码。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("01").equals(data_type) && ("04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26").contains(cust_type) && ("34".equals(value))) {
            stringErr.append("当数据类型为新增、投资者类别为银行时，机构证件类别不能填写SPV登记编码。<br/>");
        } else if (StringUtils.isNotBlank(value) && ("03").equals(data_type) && ("04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26").contains(cust_type) && ("34".equals(value))) {
            stringErr.append("当数据类型为重要信息变更、投资者类别为银行时，机构证件类别不能填写SPV登记编码。<br/>");
        }

        return stringErr.toString();

    }

    /**
     * 合法性校验-- 202 投资者持有信息登记
     *
     * @param whiteregex
     * @param whitereForCode
     * @param trCustVolRegisterInfo
     * @return
     * @throws Exception
     */
    public String trCustVolRegisterInfoCheckForVue(String whiteregex, String whitereForCode, TrCustVolRegisterInfo trCustVolRegisterInfo) throws Exception {
        StringBuffer stringErr = new StringBuffer();
        Pattern whiterpattern = null;
        if (!"".equals(whiteregex)) {
            whiterpattern = Pattern.compile(whiteregex);
        }
        Map<String, Object> param = new HashMap<>();
        //登记银行代码
        String bank_code = StringUtils.isNotBlank(trCustVolRegisterInfo.getBankCode()) ? trCustVolRegisterInfo.getBankCode() : "";
        String bank_code_desc = "";
        String bank_code_desc2 = "";
        String bank_code_desc3 = "";
        String bank_code_s = CheckDataParams.bankCode;
//				Pattern p=Pattern.compile("^([Z]{1}\\d{5})");
//				Matcher m=p.matcher(bank_code);
//				boolean flagBankCode = m.matches();
        Pattern p = Pattern.compile("^([a-zA-Z0-9]{6}$)");
        Matcher m = p.matcher(bank_code.trim());
        boolean flagBankCode = m.matches();
        if ("".equals(bank_code)) {
            bank_code_desc = "登记银行代码要素不可为空。<br/>";
        } else if (!flagBankCode) {
            bank_code_desc = "登记银行代码要素格式不对。正确的格式：6位英文或数字。<br/>";
        }
        if (!bank_code_s.equals(bank_code)) {
            bank_code_desc2 = "登记银行代码必须与银行代码相同。<br/>";
        }

        //产品登记编码
        String prod_code = StringUtils.isNotBlank(trCustVolRegisterInfo.getProdCode()) ? trCustVolRegisterInfo.getProdCode() : "";
        String prod_code_desc = "";
        String prod_code_desc2 = "";
        String prod_code_desc3 = "";
        String prod_code_desc4 = "";
        Pattern pProdCode = Pattern.compile("^[A-Za-z0-9]{14,15}$");
        Matcher mProdCode = pProdCode.matcher(prod_code);
        boolean flagProdCode = mProdCode.matches();
        if ("".equals(prod_code)) {
            prod_code_desc = "产品登记编码要素不可为空。<br/>";
        } else if (!flagProdCode) {
            prod_code_desc = "产品登记编码要素格式不对。正确的格式：14或15位英文或数字。<br/>";
        } else if (prod_code.length() > 6 && !bank_code_s.equals(prod_code.substring(0, 6))) {
            prod_code_desc = "产品登记编码为" + prod_code + "的产品与登记银行不匹配。<br/>";
        }
        //必须与登记银行匹配

        // 识别标识
        String cust_no = StringUtils.isNotBlank(trCustVolRegisterInfo.getCustNo()) ? trCustVolRegisterInfo.getCustNo() : "";
        String cust_no_desc = "";
        String cust_no_desc2 = "";
        Pattern pcustNo = Pattern.compile("^[^\\u4e00-\\u9fa5]+$");
        Matcher mcustNo = pcustNo.matcher(cust_no);
        boolean flagpcustNo = mcustNo.matches();
        if ("".equals(cust_no)) {
            cust_no_desc = "识别标识要素不可为空。<br/>";
        } else if (cust_no.length() > 30) {
            cust_no_desc = "识别标识过长。<br/>";
        }
        if (!"".equals(cust_no) && !flagpcustNo) {
            cust_no_desc2 = "识别标识只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。<br/>";
        }

        // 持有日期
        String nowDate = DateUtil.getNowDate();
        String hold_date = StringUtils.isNotBlank(trCustVolRegisterInfo.getHoldDate()) ? trCustVolRegisterInfo.getHoldDate().trim() : "";
        String hold_date_desc = "";
        String hold_date_desc2 = "";
        String hold_date_desc3 = "";
        if ("".equals(hold_date)) {
            hold_date_desc = "持有日期要素不可为空。<br/>";
        } else if (hold_date.length() == 8) {
            Pattern pdate = Pattern.compile("^(\\d{8})");
            Matcher pmatcher = pdate.matcher(hold_date);
            if (!pmatcher.matches()) {
                hold_date_desc = "若为8位日期，持有日期必须为日期格式（YYYYMMDD）。<br/>";
            } else {
                DateFormat df = new SimpleDateFormat("yyyyMMdd");
                df.setLenient(false);
                try {
                    df.parse(hold_date);
                } catch (ParseException e) {
                    hold_date_desc = "请输入正确日期。<br/>";
                }
            }
//            hold_date_desc = "持有日期必须为日期格式（YYYY-MM-DD）";
            //TODO　生产要放开
        } else {
            Pattern pdate = Pattern.compile("^(\\d{4}\\-\\d{2}\\-\\d{2})");
            Matcher pmatcher = pdate.matcher(hold_date);
            if (!pmatcher.matches()) {
                hold_date_desc = "若为10位日期，持有日期必须为日期格式（YYYY-MM-DD）。<br/>";
            } else {
                try {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    format.setLenient(false);
                    format.parse(hold_date);
                } catch (ParseException e) {
                    hold_date_desc = "请输入正确日期。<br/>";
                }

            }
        }
        if (!"".equals(hold_date) && Integer.parseInt(hold_date.replace("-", "")) > Integer.parseInt(nowDate)) {
            hold_date_desc2 = "持有日期必须小于等于系统处理当天日期。<br/>";
        }

        // 币种
        String cur = StringUtils.isNotBlank(trCustVolRegisterInfo.getCur()) ? trCustVolRegisterInfo.getCur() : "";
        String cur_desc = "";
        String cur_desc2 = "";
        boolean flagCur = cur.matches("^[A-Z]{3}$");
        if ("".equals(cur)) {
            cur_desc = "币种不可为空。<br/>";
        } else if (cur.length() != 3 || !flagCur) {
            cur_desc = "币种不在值域范围内。<br/>";
        }
        //持有份额
        String vol_tmp = StringUtils.isNotBlank(trCustVolRegisterInfo.getHoldVol()) ? trCustVolRegisterInfo.getHoldVol() : "";
        String hold_vol_desc = "";
        String hold_vol_desc2 = "";
        String hold_vol_desc3 = "";
        if ("".equals(vol_tmp)) {
            hold_vol_desc = "持有份额要素不可为空。<br/>";
        } else {
            try {
                double vol = Double.parseDouble(vol_tmp);
                hold_vol_desc = "";
                Pattern pHoldVol = Pattern.compile("^^(([1-9]{1}\\d{0,12})|([0]{1}))(\\.(\\d){1,5})?$");
                Matcher mHoldVol = pHoldVol.matcher(vol_tmp);
                boolean flagVol = mHoldVol.matches();
                if (vol <= 0) {
                    hold_vol_desc = "持有份额必须大于0。<br/>";
                }
                if (!flagVol) {
                    hold_vol_desc2 = "持有份额必须为n（18,5）格式。<br/>";
                }
            } catch (Exception e) {
                hold_vol_desc2 = "持有份额必须为n（18,5）格式。<br/>";
            }
        }

        //持有金额
        String amt_tmp = StringUtils.isNotBlank(trCustVolRegisterInfo.getHoldAmt()) ? trCustVolRegisterInfo.getHoldAmt() : "";
        double amt = 0.0;
        String hold_amt_desc = "";
        String hold_amt_desc2 = "";
        String hold_amt_desc3 = "";
        if ("".equals(amt_tmp)) {
            hold_amt_desc = "持有金额要素不可为空。<br/>";
        } else {
            try {
                amt = Double.parseDouble(amt_tmp);
                hold_amt_desc = "";
                Pattern pHoldAmt = Pattern.compile("^^(([1-9]{1}\\d{0,12})|([0]{1}))(\\.(\\d){1,5})?$");
                Matcher mHoldAmt = pHoldAmt.matcher(amt_tmp);
                boolean flagAmt = mHoldAmt.matches();
                if (amt <= 0) {
                    hold_amt_desc = "持有金额必须大于0。<br/>";
                }
                if (!flagAmt) {
                    hold_amt_desc2 = "持有金额必须为n（18,5）格式。<br/>";
                }
            } catch (Exception e) {
                hold_amt_desc2 = "持有金额必须为n（18,5）格式。<br/>";
            }
        }

        // 折算人民币金额（元）
        String convert_rmb_tmp = StringUtils.isNotBlank(trCustVolRegisterInfo.getConvertRmb()) ? trCustVolRegisterInfo.getConvertRmb() : "";
        /*modify qink 20210426*/
        double convert_rmb = 0.0;
        String convert_rmb_desc = "";
        String convert_rmb_desc2 = "";
        String convert_rmb_desc3 = "";
        String convert_rmb_desc4 = "";
        if ("".equals(convert_rmb_tmp)) {
            convert_rmb_desc = "折算人民币金额（元）要素不可为空。<br/>";
        } else {
            try {
                convert_rmb = Double.parseDouble(convert_rmb_tmp);
                convert_rmb_desc = "";
                Pattern pConvertAmt = Pattern.compile("^^(([1-9]{1}\\d{0,12})|([0]{1}))(\\.(\\d){1,5})?$");
                Matcher mConvertAmt = pConvertAmt.matcher(convert_rmb_tmp);
                boolean flagRmb = mConvertAmt.matches();
                if (convert_rmb <= 0) {
                    convert_rmb_desc = "折算人民币金额（元）必须大于0。<br/>";
                }
                if (StringUtils.isNotBlank(cur) && ("CNY").equals(cur) && Double.compare(amt, convert_rmb) != 0) {
                    convert_rmb_desc2 = "当持有金额的币种为人民币(CNY)时，折算人民币金额（元）与持有金额必须相等。<br/>";
                }
                if (!flagRmb) {
                    convert_rmb_desc3 = "折算人民币金额（元）必须为n（18,5）格式。<br/>";
                }
            } catch (Exception e) {
                convert_rmb_desc3 = "折算人民币金额（元）必须为n（18,5）格式。<br/>";
            }
        }

        //登记流水号，系统内部生成
        //重复性校验放到指标中

        if (!"".equals(whiteregex)) {
            if (!bank_code.equals("") && !whiterpattern.matcher(bank_code).matches())
                bank_code_desc3 = "登记银行代码必须填写白名单中的文字。<br/>";
            if (!prod_code.equals("") && !whiterpattern.matcher(prod_code).matches())
                prod_code_desc4 = "产品登记编码必须填写白名单中的文字。<br/>";
            if (!cust_no.equals("") && !whiterpattern.matcher(cust_no).matches())
                cust_no_desc2 = "识别标识必须填写白名单中的文字。<br/>";
            if (!hold_date.equals("") && !whiterpattern.matcher(hold_date).matches())
                hold_date_desc3 = "持有日期必须填写白名单中的文字。<br/>";
            if (!cur.equals("") && !whiterpattern.matcher(cur).matches())
                cur_desc2 = "币种必须填写白名单中的文字。<br/>";
            if (!vol_tmp.equals("") && !whiterpattern.matcher(vol_tmp).matches())
                hold_vol_desc3 = "持有份额必须填写白名单中的文字。<br/>";
            if (!amt_tmp.equals("") && !whiterpattern.matcher(amt_tmp).matches())
                hold_amt_desc3 = "持有金额必须填写白名单中的文字。<br/>";
            if (!String.valueOf(convert_rmb).equals("") && !whiterpattern.matcher(String.valueOf(convert_rmb)).matches())
                convert_rmb_desc4 = "折算人民币金额必须填写白名单中的文字。<br/>";
        }
//        String desc_flag = "1";
//        desc_flag = ("".equals(bank_code_desc)) ? desc_flag : "1";
//        desc_flag = ("".equals(prod_code_desc)) ? desc_flag : "1";
//        desc_flag = ("".equals(hold_date_desc)) ? desc_flag : "1";
//        desc_flag = ("".equals(cust_no_desc)) ? desc_flag : "1";
//        desc_flag = ("".equals(cur_desc)) ? desc_flag : "1";
//        desc_flag = ("".equals(hold_amt_desc)) ? desc_flag : "1";
//        desc_flag = ("".equals(convert_rmb_desc)) ? desc_flag : "1";
//        desc_flag = ("".equals(hold_vol_desc)) ? desc_flag : "1";

//        if (desc_flag.equals("1")) {//有错误描述,插入差错描述表
        stringErr.append(bank_code_desc);
        stringErr.append(bank_code_desc2);
        stringErr.append(bank_code_desc3);
        stringErr.append(prod_code_desc);
        stringErr.append(prod_code_desc2);
        stringErr.append(prod_code_desc3);
        stringErr.append(prod_code_desc4);
        stringErr.append(cust_no_desc);
        stringErr.append(cust_no_desc2);
        stringErr.append(hold_date_desc);
        stringErr.append(hold_date_desc2);
        stringErr.append(hold_date_desc3);
        stringErr.append(hold_vol_desc);
        stringErr.append(hold_vol_desc2);
        stringErr.append(hold_vol_desc3);
        stringErr.append(cur_desc);
        stringErr.append(cur_desc2);
        stringErr.append(hold_amt_desc);
        stringErr.append(hold_amt_desc2);
        stringErr.append(hold_amt_desc3);
        stringErr.append(convert_rmb_desc);
        stringErr.append(convert_rmb_desc2);
        stringErr.append(convert_rmb_desc3);
        stringErr.append(convert_rmb_desc4);
//        }
        if (StringUtils.isNotBlank(stringErr.toString().replace(" ", "").replace("\n", ""))) {
            return stringErr.toString();
        } else {
            return "";
        }
    }

    /**
     * 投资者明细信息  导入时 先做字典（此处会校验 “01 柜面”整个字符串）校验，避免影响新增和修改操作
     * @param trCustTransInfo
     * @return
     * @throws Exception
     */
    public String trCustTransInfoCheckDictForImport(TrCustTransInfo trCustTransInfo) throws Exception {
        StringBuffer stringErr = new StringBuffer();
        //渠道
        if(StringUtils.isNotBlank(trCustTransInfo.getChannelFlag())){
            String errDesc = CheckDataUtils.checkDictValue("subm_tr_spe_channel_flag_z", trCustTransInfo.getChannelFlag().split(" ")[0], trCustTransInfo.getChannelFlag());
            if(StringUtils.isNotBlank(errDesc)){
                stringErr.append("渠道："+errDesc);
            }
        }
        return stringErr.toString();
    }
    /**
     * 合法性校验  203--投资者明细信息登记
     *
     * @throws Exception
     */
    public String trCustTransInfoCheckForVue(String whiteregex, String whitereForCode, TrCustTransInfo trCustTransInfo) throws Exception {

        //发行机构代码
        String bank_code = StringUtils.isNotBlank(trCustTransInfo.getBankCode()) ? trCustTransInfo.getBankCode() : "";
        String bank_code_desc = "";
        String bank_code_desc2 = "";
        String bank_code_desc3 = "";
        String bank_code_s = CheckDataParams.bankCode;
//        boolean flagBankCode = bank_code.matches("^[a-zA-Z]{1}\\d{5}$");
        Pattern pbankCode = Pattern.compile("^[A-Za-z0-9]{6}$");
        Matcher mbankCode = pbankCode.matcher(bank_code);
        boolean flagBankCode = mbankCode.matches();
        if ("".equals(bank_code)) {
            bank_code_desc = "发行机构代码要素不可为空。<br/>";
        }
        if (!bank_code_s.equals(bank_code)) {
            bank_code_desc2 = "发行机构代码必须与银行代码相同。<br/>";
        }
        if (!flagBankCode && !"".equals(bank_code)) {
            bank_code_desc3 = "发行机构代码要素格式不对。正确的格式：6位英文或数字。<br/>";
        }

        //销售合同号
        String contract_no = StringUtils.isNotBlank(trCustTransInfo.getContractNo()) ? trCustTransInfo.getContractNo() : "";
        String contract_no_desc = "";
        String contract_no_desc2 = "";
        if ("".equals(contract_no)) {
            contract_no_desc = "销售合同号要素不可为空。<br/>";
        } else if (contract_no.length() > 100) {
            contract_no_desc = "销售合同号过长。<br/>";
        }

        //核心交易流水号
        String trans_serno = StringUtils.isNotBlank(trCustTransInfo.getTransSerno()) ? trCustTransInfo.getTransSerno() : "";
        String trans_serno_desc = "";
        String trans_serno_desc2 = "";
        if ("".equals(trans_serno)) {
            trans_serno_desc = "核心交易流水号要素不可为空。<br/>";
        } else if (trans_serno.length() > 60) {
            trans_serno_desc = "核心交易流水号过长。<br/>";
        }

        //理财账号
        String fnc_trans_acct_no = StringUtils.isNotBlank(trCustTransInfo.getFncTransAcctNo()) ? trCustTransInfo.getFncTransAcctNo() : "";
        String fnc_trans_acct_no_desc = "";
        String fnc_trans_acct_no_desc2 = "";
        if ("".equals(fnc_trans_acct_no)) {
            fnc_trans_acct_no_desc = "理财账号要素不可为空。<br/>";
        } else if (fnc_trans_acct_no.length() > 60) {
            fnc_trans_acct_no_desc = "理财账号过长。<br/>";
        }

        //客户统一编号
        String host_cust_no = StringUtils.isNotBlank(trCustTransInfo.getHostCustNo()) ? trCustTransInfo.getHostCustNo() : "";
        String host_cust_no_desc = "";
        String host_cust_no_desc2 = "";
        if ("".equals(host_cust_no)) {
            host_cust_no_desc = "客户统一编号要素不可为空。<br/>";
        } else if (host_cust_no.length() > 60) {
            host_cust_no_desc = "客户统一编号过长。<br/>";
        }

        // 识别标识
        String cust_no = StringUtils.isNotBlank(trCustTransInfo.getCustNo()) ? trCustTransInfo.getCustNo() : "";
        String cust_no_desc = "";
        String cust_no_desc2 = "";
        Pattern pCustNo = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher mCustNo = pCustNo.matcher(cust_no);
        boolean flagCustNo = mCustNo.find();
        if ("".equals(cust_no)) {
            cust_no_desc = "识别标识要素不可为空。<br/>";
        } else if (cust_no.length() > 30) {
            cust_no_desc2 = "识别标识过长。<br/>";
        }

        //客户姓名
//        String cust_name = StringUtils.isNotBlank(trCustTransInfo.getCustName()) ? trCustTransInfo.getCustName() : "";
//        String cust_name_desc = "";
		/*if ("".equals(cust_name)) {
			cust_name_desc = "客户姓名要素不可为空。<br/>";
		} else if (cust_name.getBytes("GBK").length > 100) {
			cust_name_desc = "客户姓名过长。<br/>";
		} else if (cust_name.matches("^[\u4e00-\u9fa5]+$") && (cust_name.length() == 2 || cust_name.length() == 3)) {
			cust_name_desc = "客户姓名不符合变形处理要求。<br/>";
		}*/

        //交易序列号
        String deal_no = StringUtils.isNotBlank(trCustTransInfo.getDealNo()) ? trCustTransInfo.getDealNo() : "";
        String deal_no_desc = "";
        String deal_no_desc2 = "";
        if ("".equals(deal_no)) {
            deal_no_desc = "交易序列号要素不可为空。<br/>";
        } else if (deal_no.length() > 30) {
            deal_no_desc = "交易序列号过长。<br/>";
        }

        //关联活期存款账号
        String acct_no = StringUtils.isNotBlank(trCustTransInfo.getAcctNo()) ? trCustTransInfo.getAcctNo() : "";
        String acct_no_desc = "";
        String acct_no_desc2 = "";
        if ("".equals(acct_no)) {
            acct_no_desc = "关联活期存款账号要素不可为空。<br/>";
        } else if (acct_no.length() > 60) {
            acct_no_desc = "关联活期存款账号过长。<br/>";
        }
        //关联活期存款账号开户行代码
        String acct_bank_no = StringUtils.isNotBlank(trCustTransInfo.getAcctBankNo()) ? trCustTransInfo.getAcctBankNo() : "";
        String acct_bank_no_desc = "";
        String acct_bank_no_desc2 = "";
        if ("".equals(acct_bank_no)) {
            acct_bank_no_desc = "关联活期存款账号开户行代码要素不可为空。<br/>";
        } else if (acct_bank_no.length() > 30) {
            acct_bank_no_desc = "关联活期存款账号开户行代码过长。<br/>";
        }

        //关联活期存款账号开户行名称
        String acct_bank_name = StringUtils.isNotBlank(trCustTransInfo.getAcctBankName()) ? trCustTransInfo.getAcctBankName() : "";
        String acct_bank_name_desc = "";
        String acct_bank_name_desc2 = "";
        if ("".equals(acct_bank_name)) {
            acct_bank_name_desc = "关联活期存款账号开户行名称要素不可为空。<br/>";
        } else if (CheckDataUtils.checkStringLengthOver(acct_bank_name,200)) {
            acct_bank_name_desc = "关联活期存款账号开户行名称过长。<br/>";
        }
        //关联账号开户所在地
        String code = "110000,310000,120000,370000,370200,230000,220000,210000,210200,320000,330000,330200,360000,340000,350000,350200,140000,410000,130000,430000,420000,440000,440300,450000,460000,500000,510000,530000,520000,610000,620000,630000,150000,640000,650000,540000,810000,820000,710000,900000";
        String acct_loc_code = StringUtils.isNotBlank(trCustTransInfo.getAcctLocCode()) ? trCustTransInfo.getAcctLocCode() : "";
        String acct_loc_code_desc = "";
        if ("".equals(acct_loc_code)) {
            acct_loc_code_desc = "关联账号开户所在地要素不可为空。<br/>";
        } else if (!code.contains(acct_loc_code)) {
            acct_loc_code_desc = "关联账号开户所在地不在值域范围内。<br/>";
        }

        //是否有其他机构代销
        code = "01,02";
        String is_agent = StringUtils.isNotBlank(trCustTransInfo.getIsAgent()) ? trCustTransInfo.getIsAgent() : "";
        String is_agent_desc = "";
        if ("".equals(is_agent)) {
            is_agent_desc = "是否代销要素不可为空。<br/>";
        } else if (!"".equals(acct_loc_code) && !code.contains(is_agent)) {
            is_agent_desc = "是否代销不在值域范围内。<br/>";
        }

        //销售机构代码
        String agent_bank_code = StringUtils.isNotBlank(trCustTransInfo.getAgentBankCode()) ? trCustTransInfo.getAgentBankCode() : "";
        String agent_bank_code_desc = "";
        String agent_bank_code_desc1 = "";
        String agent_bank_code_desc2 = "";
        String agent_bank_code_desc3 = "";
        if("".equals(agent_bank_code)){
            agent_bank_code_desc = "销售机构代码要素不可为空。<br/>";
        }
        if ("02".equals(is_agent) && !agent_bank_code.equals(bank_code)) {
            agent_bank_code_desc1 = "当是否代销为否时，销售机构代码必须与登记机构代码保持一致。<br/>";
        }
        if (agent_bank_code.length() > 30) {
            agent_bank_code_desc2 = "销售机构代码过长。<br/>";
        }

        //销售机构名称
        String agent_bank_name = StringUtils.isNotBlank(trCustTransInfo.getAgentBankName()) ? trCustTransInfo.getAgentBankName() : "";
        String agent_bank_name_desc = "";
        String agent_bank_name_desc2 = "";
        String agent_bank_name_desc3 = "";
        if("".equals(agent_bank_name)){
            agent_bank_name_desc = "销售机构名称要素不可为空。<br/>";
        }
//        if ("02".equals(is_agent) && StringUtils.isNotBlank(agent_bank_name)) {
//            agent_bank_name_desc = "当是否代销为否时，销售机构名称要素。<br/>";
//        }
//        if (agent_bank_name.getBytes("GBK").length > 200) {
        if (CheckDataUtils.checkStringLengthOver(agent_bank_name,200)) {
            agent_bank_name_desc2 = "销售机构名称过长。<br/>";
        }
        //销售机构所属监管机构
        code = "01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37";
        String agent_regu_code = StringUtils.isNotBlank(trCustTransInfo.getAgentReguCode()) ? trCustTransInfo.getAgentReguCode() : "";
        String agent_regu_code_desc = "";
        String agent_regu_code_desc2 = "";
        if ("01".equals(is_agent) && "".equals(agent_regu_code)) {
            agent_regu_code_desc = "当是否代销为是时，销售机构所属监管机构要素不可为空。<br/>";
        } else if ("02".equals(is_agent) && !"".equals(agent_regu_code)) {
            agent_regu_code_desc = "当是否代销为否时，销售机构所属监管机构要素必须为空。<br/>";
        }
        if (!"".equals(agent_regu_code) && !code.contains(agent_regu_code)) {
            agent_regu_code_desc2 = "销售机构所属监管机构不在值域范围内。<br/>";
        }
        //产品登记编码
        String prod_code = StringUtils.isNotBlank(trCustTransInfo.getProdCode()) ? trCustTransInfo.getProdCode() : "";
        //System.out.println("woshizp1"+prod_code);
        String prod_code_desc = "";
        boolean flagProdCode = prod_code.matches("^[a-zA-Z0-9]{14,15}$");
        if ("".equals(prod_code)) {
            prod_code_desc = "产品登记编码要素不可为空。<br/>";
        } else if (prod_code.length() > 6 && !bank_code_s.equals(prod_code.substring(0, 6))) {
            prod_code_desc = "产品登记编码为" + prod_code + "的产品与登记银行不匹配。<br/>";
        } else if (!flagProdCode) {
            prod_code_desc = "产品登记编码要素格式不对。正确的格式：14或15位英文或数字。<br/>";
        }

        //System.out.println("woshizp"+prod_code_desc);
        //业务种类
        code = "01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16";
        String busi_code = StringUtils.isNotBlank(trCustTransInfo.getBusiCode()) ? trCustTransInfo.getBusiCode() : "";
        String busi_code_desc = "";
        String busi_code_desc2 = "";
        if ("".equals(busi_code)) {
            busi_code_desc = "业务种类要素不可为空。<br/>";
        }
        if (!"".equals(busi_code) && !code.contains(busi_code)) {
            busi_code_desc2 = "业务种类不在值域范围内。<br/>";
        }
        //业务发生地所属监管
        code = "02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37";
        String busi_regu_code = StringUtils.isNotBlank(trCustTransInfo.getBusiReguCode()) ? trCustTransInfo.getBusiReguCode() : "";
        String busi_regu_code_desc = "";
        if ("".equals(busi_regu_code)) {
            busi_regu_code_desc = "业务发生地所属监管要素不可为空。<br/>";
        } else if (!"".equals(busi_regu_code) && !code.contains(busi_regu_code)) {
            busi_regu_code_desc = "业务发生地所属监管不在值域范围内。<br/>";
        }

        //业务确认日期

        String ack_date = StringUtils.isNotBlank(trCustTransInfo.getAckDate()) ? trCustTransInfo.getAckDate() : "";
        String sysDate = DateUtil.getNowDate();
        String ack_date_desc = "";
//        String ack_date_desc2 = "";
        if ("".equals(ack_date)) {
            ack_date_desc = "业务确认日期要素不可为空。<br/>";
        } else if (ack_date.length() == 10) {
            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                format.setLenient(false);
                format.parse(ack_date);
            } catch (ParseException e) {
                ack_date_desc = "请输入正确日期，日期为10位时，业务确认日期必须为日期格式（YYYY-MM-DD）。<br/>";
            }
        } else {
            /* Pattern pdate = Pattern.compile("^(\\d{4}\\-\\d{2}\\-\\d{2})");
            Matcher pmatcher = pdate.matcher(ack_date);
            if(!pmatcher.matches()){
                ack_date_desc = "若为10位日期，持有日期必须为日期格式（YYYY-MM-DD）";
            }
            ack_date_desc = "业务确认日期必须为日期格式（YYYY-MM-DD）。<br/>";*/
            if (!ack_date.matches("[0-9]{4}[0-9]{2}[0-9]{2}")) {
                ack_date_desc = "日期为8位时，业务确认日期必须为日期格式（YYYYMMDD）。<br/>";
            }else{
                DateFormat df = new SimpleDateFormat("yyyyMMdd");
                df.setLenient(false);
                try {
                    df.parse(ack_date);
                } catch (ParseException e) {
                    ack_date_desc = "请输入正确日期。<br/>";
                }
            }
        }
//        if (!"".equals(ack_date) && Integer.parseInt(ack_date.replace("-", "")) > Integer.parseInt(sysDate)) {
//            ack_date_desc2 = "业务确认日期为" + ack_date + "的登记要素不可大于明细登记日期。<br/>";
//        }

        //业务确认时间
        SimpleDateFormat format1 = new SimpleDateFormat("HHmmss");
        format1.setLenient(false);
        String ack_time = StringUtils.isNotBlank(trCustTransInfo.getAckTime()) ? trCustTransInfo.getAckTime() : "";
        String ack_time_desc = "";
        String ack_time_desc2 = "";
        String ack_time_desc3 = "";
        String matche = "([0-9][0-9])([0-9][0-9])([0-9][0-9])$";
        if ("".equals(ack_time)) {
            ack_time_desc = "业务确认时间要素不可为空。<br/>";
        } else if (!ack_time.matches(matche)) {
            ack_time_desc2 = "业务确认时间要素格式不对。正确的格式：HHMMSS。<br/>";
        } else {
            try {
                format1.parse(ack_time);
            } catch (ParseException e) {
                ack_time_desc3 = "业务确认时间必须为正确时间。<br/>";
            }
        }

        //币种
        String cur_desc = "";
        String cur_desc2 = "";
        String cur = StringUtils.isNotBlank(trCustTransInfo.getCur()) ? trCustTransInfo.getCur() : "";
        boolean flagCur = cur.matches("^[A-Z]{3}$");
        if ("".equals(cur)) {
            cur_desc = "币种不可为空。<br/>";
        } else if (cur.length() != 3 || !flagCur) {
            cur_desc = "币种不在值域范围内。<br/>";
        }
        //币种 枚举值范围内单选

        //金额
        String ack_amt = StringUtils.isNotBlank(trCustTransInfo.getAckAmt()) ? trCustTransInfo.getAckAmt() : "";
        String ack_amt_desc = "";
        String ack_amt_desc2 = "";
        if ("".equals(ack_amt)) {
            ack_amt_desc = "金额要素不可为空。<br/>";
        } else {
            try {
                if (Double.parseDouble(ack_amt) < 0) {
                    ack_amt_desc = "金额必须大于等于0。<br/>";
                }
                if (!ack_amt.matches("^^(([1-9]{1}\\d{0,12})|([0]{1}))(\\.(\\d){1,5})?$")) {
                    ack_amt_desc2 = "金额必须为n（18,5）格式。<br/>";
                }
            } catch (Exception e) {
                ack_amt_desc2 = "金额必须为n（18,5）格式。<br/>";
            }
        }

        //折算人民币金额
        //System.out.println("12345"+DirectUtils.getCorrectValue(correctRow, "convert_rmb", sqlRow.get("convert_rmb")).getClass().getName());
        String convert_rmb = StringUtils.isNotBlank(trCustTransInfo.getConvertRmb()) ? trCustTransInfo.getConvertRmb() : "";
        String convert_rmb_desc = "";
        String convert_rmb_desc2 = "";
        String convert_rmb_desc3 = "";
        if ("".equals(convert_rmb)) {
            convert_rmb_desc = "折算人民币金额（元）要素不可为空。<br/>";
        } else {
            try {
                if (Double.parseDouble(convert_rmb) < 0) {
                    convert_rmb_desc = "折算人民币金额（元）必须大于等于0。<br/>";
                }
                if ("CNY".equals(cur) && Double.compare(Double.parseDouble(ack_amt), Double.parseDouble(convert_rmb)) != 0) {
                    convert_rmb_desc2 = "当币种为人民币(CNY)时，折算人民币金额与金额必须相等。<br/>";
                }
                if (!convert_rmb.matches("^^(([1-9]{1}\\d{0,12})|([0]{1}))(\\.(\\d){1,5})?$")) {
                    convert_rmb_desc3 = "折算人民币金额（元）必须为n（18,5）格式。<br/>";
                }
            } catch (Exception e) {
                convert_rmb_desc3 = "折算人民币金额（元）必须为n（18,5）格式。<br/>";
            }
        }

        //确认净值
        String Nav = StringUtils.isNotBlank(trCustTransInfo.getNav()) ? trCustTransInfo.getNav() : "";
        //String proc_mode = sqlRow.getString("proc_mode");
        String nav_desc = "";
        String nav_desc2 = "";
        String nav_desc3 = "";
        try {
            //净值产品和非净值产品净值都不能为空
            if ("".equals(Nav)) {
                nav_desc = "对于开放式和封闭式净值型产品,确认净值必填。<br/>";
            }
            if (Double.parseDouble(Nav) < 0) {
                nav_desc2 = "确认净值必须大于等于0。<br/>";
            }
            if (!Nav.matches("^^(([1-9]{1}\\d{0,4})|([0]{1}))(\\.(\\d){1,5})?$")) {
                nav_desc3 = "确认净值必须为n（10,5）格式。<br/>";
            }
        } catch (Exception e) {
            nav_desc3 = "确认净值必须为n（10,5）格式。<br/>";
        }

        //份额
        String ack_vol = StringUtils.isNotBlank(trCustTransInfo.getAckVol()) ? trCustTransInfo.getAckVol() : "";
        String ack_vol_desc = "";
        String ack_vol_desc2 = "";
        String ack_vol_desc3 = "";
        try {
            if ("".equals(ack_vol)) {
                ack_vol_desc = "份额要素不可为空。<br/>";
            }
            if (Double.parseDouble(ack_vol) < 0) {
                ack_vol_desc2 = "份额必须大于等于0。<br/>";
            }
            if (!ack_vol.matches("^^(([1-9]{1}\\d{0,12})|([0]{1}))(\\.(\\d){1,5})?$")) {
                ack_vol_desc3 = "份额必须为n（18,5）格式。<br/>";
            }
        } catch (Exception e) {
            ack_vol_desc3 = "份额必须为n（18,5）格式。<br/>";
        }

        //费用
        String fee_amt = StringUtils.isNotBlank(trCustTransInfo.getFeeAmt()) ? trCustTransInfo.getFeeAmt() : "";
        String fee_amt_desc = "";
        String fee_amt_desc2 = "";
        String fee_amt_desc3 = "";
        try {
            if ("".equals(fee_amt)) {
                fee_amt_desc = "费用要素不可为空。<br/>";
            }
            if (Double.parseDouble(fee_amt) < 0) {
                fee_amt_desc2 = "费用必须大于等于0。<br/>";
            }
            if (!fee_amt.matches("^^(([1-9]{1}\\d{0,12})|([0]{1}))(\\.(\\d){1,2})?$")) {
                fee_amt_desc3 = "费用必须为n（15,2）格式。<br/>";
            }
        } catch (Exception e) {
            fee_amt_desc3 = "费用必须为n（15,2）格式。<br/>";
        }

        //渠道
        String channel_flag_desc = "";
        String channel_flag = StringUtils.isNotBlank(trCustTransInfo.getChannelFlag()) ? trCustTransInfo.getChannelFlag() : "";
        code = "01,02,03,04,05,06,99";
        if ("".equals(channel_flag)) {
            channel_flag_desc = "渠道要素不可为空。<br/>";
        }
//        else if (!"".equals(channel_flag) && !code.contains(channel_flag)) {
//            channel_flag_desc = "渠道不在值域范围内。<br/>";
//        }

        //交易柜员号
        String inputuser_desc = "";
        String inputuser_desc2 = "";
        String inputuser_desc3 = "";
        String inputuser = StringUtils.isNotBlank(trCustTransInfo.getInputuser()) ? trCustTransInfo.getInputuser() : "";
        if ("".equals(inputuser)) {
            inputuser_desc = "交易柜员号要素不可为空。<br/>";
        }
//        if (inputuser.getBytes("GBK").length > 30) {
//            inputuser_desc2 = "交易柜员号过长。<br/>";
//        }
        if (inputuser.length() > 30) {
            inputuser_desc2 = "交易柜员号过长。<br/>";
        }

        //备注
        String remark_desc = "";
        String remark_desc2 = "";
        String remark = StringUtils.isNotBlank(trCustTransInfo.getRemark()) ? trCustTransInfo.getRemark() : "";
//        if (!"".equals(remark) && remark.getBytes("GBK").length > 256) {
//            remark_desc = "备注过长。<br/>";
//        }
        if (!"".equals(remark) && CheckDataUtils.checkStringLengthOver(remark,256)) {
            remark_desc = "备注过长。<br/>";
        }

        //登记流水号
		/*;
		String register_serno_desc = "";
		boolean flagmpregister_serno = register_serno.matches("[\\x00-\\xff]+");
		if ("".equals(register_serno)) {
			register_serno_desc = "登记流水号要素不可为空。<br/>";
		} else if (register_serno.getBytes("GBK").length > 32) {
			register_serno_desc = "登记流水号过长。<br/>";
		}*/
        Pattern whiterpattern = null;
        if (!"".equals(whiteregex)) {
            whiterpattern = Pattern.compile(whiteregex);
        }

        if (!"".equals(whiteregex)) {
            if (StringUtils.isNotBlank(contract_no) && !whiterpattern.matcher(contract_no).matches())
                contract_no_desc2 = "销售合同号必须填写白名单中的文字。<br/>";
            if (StringUtils.isNotBlank(trans_serno) && !whiterpattern.matcher(trans_serno).matches())
                trans_serno_desc2 = "核心交易流水号必须填写白名单中的文字。<br/>";
            if (StringUtils.isNotBlank(fnc_trans_acct_no) && !whiterpattern.matcher(fnc_trans_acct_no).matches())
                fnc_trans_acct_no_desc2 = "理财账号必须填写白名单中的文字。<br/>";
            if (StringUtils.isNotBlank(host_cust_no) && !whiterpattern.matcher(host_cust_no).matches())
                host_cust_no_desc2 = "客户统一编号必须填写白名单中的文字。<br/>";
            if (StringUtils.isNotBlank(cust_no) && (!whiterpattern.matcher(cust_no).matches() || flagCustNo))
                cust_no_desc2 = "识别标识只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。<br/>";
//            if (StringUtils.isNotBlank(cust_name) && !whiterpattern.matcher(cust_name).matches())
//                cust_name_desc = "客户姓名必须填写白名单中的文字。<br/>";
            if (StringUtils.isNotBlank(deal_no) && !whiterpattern.matcher(deal_no).matches())
                deal_no_desc2 = "交易序列号必须填写白名单中的文字。<br/>";
            if (StringUtils.isNotBlank(acct_no) && !whiterpattern.matcher(acct_no).matches())
                acct_no_desc2 = "关联活期存款账号必须填写白名单中的文字。<br/>";
            if (StringUtils.isNotBlank(acct_bank_no) && !whiterpattern.matcher(acct_bank_no).matches())
                acct_bank_no_desc2 = "关联活期存款账号开户行代码必须填写白名单中的文字。<br/>";
            if (StringUtils.isNotBlank(acct_bank_name) && !whiterpattern.matcher(acct_bank_name).matches())
                acct_bank_name_desc2 = "关联活期存款账号开户行名称必须填写白名单中的文字。<br/>";
            if (StringUtils.isNotBlank(agent_bank_code) && !whiterpattern.matcher(agent_bank_code).matches())
                agent_bank_code_desc3 = "代销机构代码必须填写白名单中的文字。<br/>";
            if (StringUtils.isNotBlank(agent_bank_name) && !whiterpattern.matcher(agent_bank_name).matches())
                agent_bank_name_desc3 = "代销机构名称必须填写白名单中的文字。<br/>";
            if (StringUtils.isNotBlank(inputuser) && !whiterpattern.matcher(inputuser).matches())
                inputuser_desc3 = "交易柜员号必须填写白名单中的文字。<br/>";
            if (StringUtils.isNotBlank(remark) && !whiterpattern.matcher(remark).matches())
                remark_desc2 = "备注必须填写白名单中的文字。<br/>";
			/*if (!register_serno.equals("") && (!whiterpattern.matcher(register_serno).matches() || !flagmpregister_serno))
				register_serno_desc = register_serno_desc + "登记流水号只能含有白名单内的阿拉伯数字，英文字母，半角符号。<br/>";*/
        }
        StringBuffer stringErr = new StringBuffer();
        stringErr.append(bank_code_desc);
        stringErr.append(bank_code_desc2);
        stringErr.append(bank_code_desc3);
        stringErr.append(trans_serno_desc);
        stringErr.append(trans_serno_desc2);
        ;
        stringErr.append(contract_no_desc);
        stringErr.append(contract_no_desc2);
        ;
        stringErr.append(fnc_trans_acct_no_desc);
        stringErr.append(fnc_trans_acct_no_desc2);
        stringErr.append(host_cust_no_desc);
        stringErr.append(host_cust_no_desc2);
        stringErr.append(cust_no_desc);
        stringErr.append(cust_no_desc2);
//        stringErr.append(cust_name_desc);
        stringErr.append(deal_no_desc);
        stringErr.append(deal_no_desc2);
        stringErr.append(acct_no_desc);
        stringErr.append(acct_no_desc2);
        stringErr.append(acct_bank_no_desc);
        stringErr.append(acct_bank_no_desc2);
        stringErr.append(acct_bank_name_desc);
        stringErr.append(acct_bank_name_desc2);
        stringErr.append(acct_loc_code_desc);
        stringErr.append(is_agent_desc);
        stringErr.append(agent_bank_code_desc);
        stringErr.append(agent_bank_code_desc1);
        stringErr.append(agent_bank_code_desc2);
        stringErr.append(agent_bank_code_desc3);
        stringErr.append(agent_bank_name_desc);
        stringErr.append(agent_bank_name_desc2);
        stringErr.append(agent_bank_name_desc3);
        stringErr.append(agent_regu_code_desc);
        stringErr.append(agent_regu_code_desc2);
        stringErr.append(prod_code_desc);
        stringErr.append(busi_code_desc);
        stringErr.append(busi_code_desc2);
        stringErr.append(busi_regu_code_desc);
//        stringErr.append(ack_date_desc);
        stringErr.append(ack_date_desc);
        stringErr.append(ack_time_desc);
        stringErr.append(ack_time_desc2);
        stringErr.append(ack_time_desc3);
        stringErr.append(cur_desc);
        stringErr.append(ack_amt_desc);
        stringErr.append(ack_amt_desc2);
        stringErr.append(convert_rmb_desc);
        stringErr.append(convert_rmb_desc2);
        stringErr.append(convert_rmb_desc3);
        stringErr.append(nav_desc);
        stringErr.append(nav_desc2);
        stringErr.append(nav_desc3);
        stringErr.append(ack_vol_desc);
        stringErr.append(ack_vol_desc2);
        stringErr.append(ack_vol_desc3);
        stringErr.append(fee_amt_desc);
        stringErr.append(fee_amt_desc2);
        stringErr.append(fee_amt_desc3);
        stringErr.append(channel_flag_desc);
        stringErr.append(inputuser_desc);
        stringErr.append(inputuser_desc2);
        stringErr.append(inputuser_desc3);
        stringErr.append(remark_desc);
        stringErr.append(remark_desc2);
        /*stringErr.append(("register_serno_desc", register_serno_desc);*/
        if (StringUtils.isNotBlank(stringErr.toString().replace(" ", "").replace("\n", ""))) {
            return stringErr.toString();
        } else {
            return "";
        }
    }

    /**
     * 投资者身份信息  导入时 先做字典（此处会校验 “01 普通个人”整个字符串） 投资者类别 校验，避免影响新增和修改操作
     * @param trCustRegisterInfo
     * @return
     * @throws Exception
     */
    public String trCustRegisterInfoDictCheckForImport(TrCustRegisterInfo trCustRegisterInfo) throws Exception {
        StringBuffer stringErr = new StringBuffer();
        if(StringUtils.isNotBlank(trCustRegisterInfo.getCustType())){
            String errDesc = CheckDataUtils.checkDictValue("tr_cust_type", trCustRegisterInfo.getCustType().split(" ")[0], trCustRegisterInfo.getCustType());
            if(StringUtils.isNotBlank(errDesc)){
                stringErr.append("投资者类别："+errDesc);
            }
        }
        return stringErr.toString();
    }
    /**
     * 合法性校验 201--投资者身份信息登记，
     *
     * @throws Exception
     */
    public String trCustRegisterInfoCheckForVue(String whiteregex, String whitereForCode, TrCustRegisterInfo trCustRegisterInfo) throws Exception {
        //初始化数据字典值
        Pattern whiterpattern = null;
        if (!"".equals(whiteregex)) {
            whiterpattern = Pattern.compile(whiteregex);
        }

        //白名单字符

        String cust_no = StringUtils.isNotBlank(trCustRegisterInfo.getCustNo()) ? trCustRegisterInfo.getCustNo() : "";   //识别标识
        String cust_no_desc = "";
        String cust_no_desc2 = "";
        String cust_no_desc3 = "";
        //登记银行代码
        String bank_code = StringUtils.isNotBlank(trCustRegisterInfo.getBankCode()) ? trCustRegisterInfo.getBankCode() : "";
        String bank_code_desc = "";
        String bank_code_desc2 = "";
        String bank_code_desc3 = "";
        String bank_code_desc4 = "";
        String bank_code_s = CheckDataParams.bankCode;
//				Pattern p=Pattern.compile("^([Z]{1}\\d{5})");
        Pattern p = Pattern.compile("^([a-zA-Z0-9]{6}$)");
        Matcher m = p.matcher(bank_code.trim());
        boolean flagBankCode = m.matches();
        if ("".equals(bank_code)) {
            bank_code_desc = "登记银行代码要素不可为空。<br/>";
        } else if (!flagBankCode) {
            bank_code_desc3 = "登记银行代码要素格式不对。正确的格式：6位英文或数字。<br/>";
        }
        if (!bank_code_s.equals(bank_code)) {
            bank_code_desc2 = "登记银行代码必须与银行代码相同。<br/>";
        }
        //数据类型
        String data_type = StringUtils.isNotBlank(trCustRegisterInfo.getDataType()) ? trCustRegisterInfo.getDataType() : "";
        String data_type_desc = "";
        String data_type_desc2 = "";
        if ("".equals(data_type)) {
            data_type_desc = "数据类型要素不可为空。<br/>";
        } else if (!("01,02,03,04").contains(data_type)) {
            data_type_desc = "数据类型不在值域范围内。<br/>";
        }

        //该投资者是否属于本机构
        String is_belong = StringUtils.isNotBlank(trCustRegisterInfo.getIsBelong()) ? trCustRegisterInfo.getIsBelong() : "";
        String is_belong_desc = "";
        String is_belong_desc2 = "";
        String is_belong_desc3 = "";
        /*modify qink 20210426 验证识别标识是否存在*/

        if (StringUtils.isNotBlank(data_type) && ("02,04").contains(data_type) && !"".equals(is_belong)) {
            is_belong_desc = "当数据类型为识别标识变更或其他信息变更时，该投资者是否属于本机构要素必须为空。<br/>";
        }
        if (!("01,02").contains(is_belong) && !"".equals(is_belong)) {
            is_belong_desc2 = "该投资者是否属于本机构不在值域范围内。<br/>";
        }
        //投资者所属银行名称
        String iss_bank_name = StringUtils.isNotBlank(trCustRegisterInfo.getIssBankName()) ? trCustRegisterInfo.getIssBankName() : "";
        String iss_bank_name_desc = "";
        String iss_bank_name_desc2 = "";
        String iss_bank_name_desc3 = "";
        String iss_bank_name_desc4 = "";
        if (StringUtils.isNotBlank(data_type) && ("02,04").contains(data_type) && !"".equals(iss_bank_name)) {
            iss_bank_name_desc = "当数据类型为识别标识变更或其他信息变更时，投资者所属银行名称要素必须为空。<br/>";
        }
        if (CheckDataUtils.checkStringLengthOver(iss_bank_name,60)) {
            iss_bank_name_desc2 = "投资者所属银行名称过长。<br/>";
        }
//        if (("01").equals(is_belong) && !iss_bank_name.equals(bank_name)) { //TODO 缺少登记机构名称字段做比对，需确认来源
//            iss_bank_name_desc3 = "当“该投资者是否属于本机构”选择“是”时，该代码必须与登记机构名称一致。<br/>";
//        }
        //投资者所属银行代码
        String iss_bank_code = StringUtils.isNotBlank(trCustRegisterInfo.getIssBankCode()) ? trCustRegisterInfo.getIssBankCode() : "";
        String iss_bank_code_desc = "";
        String iss_bank_code_desc2 = "";
        String iss_bank_code_desc3 = "";
        String iss_bank_code_desc4 = "";
//        Pattern pissBankCode = Pattern.compile("^([CZ]{1}\\d{5})");
        Pattern pissBankCode = Pattern.compile("^([a-zA-Z0-9]{6}$)");
        Matcher missBankCode = pissBankCode.matcher(iss_bank_code.trim());
        boolean flagissBankCode = missBankCode.matches();
        if (this.conUtil(data_type, "02,04") > -1 && !"".equals(iss_bank_code)) {
            iss_bank_code_desc = "当数据类型为识别标识变更或其他信息变更时，投资者所属银行代码要素必须为空。<br/>";
        }
        if (!flagissBankCode && !"".equalsIgnoreCase(iss_bank_code)) {
//            iss_bank_code_desc2 = "投资者所属银行代码要素格式不对,正确的格式：C/ZXXXXX（XXXXX为数字）。<br/>";
            iss_bank_code_desc2 = "投资者所属银行代码要素格式不对,正确的格式：6位英文或数字。<br/>";
        }
        //contains的判断首先需要判定非空，再判定包含
        if (("01").equals(is_belong) && !iss_bank_code.equals(bank_code)) {
            iss_bank_name_desc3 = "当“该投资者是否属于本机构”选择“是”时，投资者所属银行代码必须与登记机构代码一致。<br/>";
        }
        //投资者境内外标识
        String in_out_sign = StringUtils.isNotBlank(trCustRegisterInfo.getInOutSign()) ? trCustRegisterInfo.getInOutSign() : "";
        String in_out_sign_desc = "";
        String in_out_sign_desc2 = "";
        String in_out_sign_desc3 = "";
        if (this.conUtil(data_type, "01,03") > -1 && "".equals(in_out_sign)) {
            in_out_sign_desc = "当数据类型为新增或重要信息变更时, 投资者境内外标识要素不可为空。<br/>";
        } else if (this.conUtil(data_type, "02,04") > -1 && !"".equals(in_out_sign)) {
            in_out_sign_desc = "当数据类型为识别标识变更或其他信息变更时, 投资者境内外标识要素必须为空。<br/>";
        }
        if (this.conUtil(in_out_sign, "01,02") <= -1 && !"".equals(in_out_sign)) {
            in_out_sign_desc2 = "投资者境内外标识不在值域范围内。<br/>";
        }
//        System.err.println(in_out_sign + " : " + in_out_sign_desc + " == " + this.conUtil(in_out_sign, "01,02"));
        //投资者所属国家或地区
        String iss_country = StringUtils.isNotBlank(trCustRegisterInfo.getIssCountry()) ? trCustRegisterInfo.getIssCountry() : "";
        String iss_country_desc = "";
        String iss_country_desc2 = "";
        String iss_country_desc3 = "";
        if (this.conUtil(data_type, "01,03") > -1 && this.conUtil(in_out_sign, "01") > -1 && !"".equals(iss_country)) {
            iss_country_desc = "当数据类型为新增或重要信息变更时, 投资者境内外标识为境内时，投资者所属国家或地区要素必须为空。<br/>";
        }
        if (this.conUtil(data_type, "02,04") > -1 && !"".equals(iss_country)) {
            iss_country_desc = "当数据类型为识别标识变更或其他信息变更时,投资者所属国家或地区要素必须为空。<br/>";
        }
        if (!"".equals(iss_country) && iss_country.length() > 3) {
            in_out_sign_desc2 = "投资者所属国家或地区不在值域范围内。<br/>";
        }
//        if (this.conUtil(iss_country, "TODO dict") <= -1 && !"".equals(iss_country)) { // 国家或地区需要联表查询比对
//            in_out_sign_desc2 = "投资者所属国家或地区不在值域范围内。<br/>";
//        }

        //原识别标识   识别标识
        String ori_cust_no = StringUtils.isNotBlank(trCustRegisterInfo.getOriCustNo()) ? trCustRegisterInfo.getOriCustNo() : "";
        String ori_cust_no_desc = "";
        String ori_cust_no_desc2 = "";
        String ori_cust_no_desc3 = "";
        Pattern pcustNo = Pattern.compile("^[^\\u4e00-\\u9fa5]+$");
        Matcher mCustNo = pcustNo.matcher(cust_no);
        Matcher orimCustNo = pcustNo.matcher(ori_cust_no);
        boolean flagCustNo = mCustNo.matches();
        boolean flagoriCustNo = orimCustNo.matches();
        if ("".equals(cust_no)) {
            cust_no_desc = "识别标识要素不可为空。<br/>";
        } else if (cust_no.length() > 30) {
            cust_no_desc = "识别标识过长。<br/>";
        } else if (!flagCustNo) {
            cust_no_desc3 = "不得含有汉字，识别标识只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。<br/>";
        }
        if (this.conUtil(data_type, "02") > -1 && cust_no.equals(ori_cust_no)) {
            cust_no_desc2 = "当数据类型为识别标识变更时，原识别标识和识别标识不能相同。<br/>";
        }

        if (this.conUtil(data_type, "01") > -1 && !"".equals(ori_cust_no)) {
            ori_cust_no_desc = "当数据类型为新增时，原识别标识要素必须为空。<br/>";
        } else if (this.conUtil(data_type, "02") > -1 && "".equals(ori_cust_no)) {
            ori_cust_no_desc = "当数据类型为识别标识变更时，原识别标识要素不可为空。<br/>";
        } else if (this.conUtil(data_type, "03,04") > -1 && !"".equals(ori_cust_no)) {
            ori_cust_no_desc = "当数据类型为重要信息变更或其他信息变更时，原识别标识要素必须为空。<br/>";
        }
        if (ori_cust_no.length() > 30) {
            ori_cust_no_desc2 = "原识别标识过长。<br/>";
        }
        if (!"".equals(ori_cust_no) && !flagoriCustNo) {
            ori_cust_no_desc2 = "原识别标识只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。<br/>";
        }
        //投资者类别
        boolean isCustType = true;
        String cust_type = StringUtils.isNotBlank(trCustRegisterInfo.getCustType()) ? trCustRegisterInfo.getCustType() : "";
        String cust_type_desc = "";
        String cust_type_desc2 = "";
        String cust_type_desc3 = "";
//						DirectUtils.checkDict("tr_cust_type", cust_type);
        if (this.conUtil(data_type, "01,03") > -1 && "".equals(cust_type)) {
            cust_type_desc = "当数据类型为新增或重要信息变更时，投资者类别要素不可为空。<br/>";
        } else if (this.conUtil(data_type, "02,04") > -1 && !"".equals(cust_type)) {
            cust_type_desc = "当数据类型为识别标识变更或其他信息变更时，投资者类别要素必须为空。<br/>";
        }
        /*if (!"".equals(cust_type) && this.conUtil(cust_type, "01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27") <= -1) {
            cust_type_desc2 = "投资者类别不在值域范围内。<br/>";
            isCustType = false;
        }*/
        String sql = "select dict,itemkey k,itemval v from sys_dict_item where dict='tr_cust_type' order by dict";
        List<SqlRow> sqlRows = comnDao.findRows(sql);
        CheckDataParams.dict_name = this.paramToMap(sqlRows);
        //个人证件类别
        String personal_id_type = StringUtils.isNotBlank(trCustRegisterInfo.getPersonalIdType()) ? trCustRegisterInfo.getPersonalIdType() : "";
        String personal_id_type_desc = "";
        String personal_id_type_desc2 = "";
        String personal_id_type_desc3 = "";
        String personal_id_type_desc4 = "";
        if (this.conUtil(data_type, "01") > -1 && isCustType) {//新增
            if (this.conUtil(cust_type, "01,02,03") > -1 && "".equals(personal_id_type)) {
                personal_id_type_desc = "当数据类型为新增、重要信息变更，且投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，个人证件类别要素不可为空。<br/>";
            } else if (this.conUtil(cust_type, "04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27") > -1
                    && !"".equals(personal_id_type)) {
                personal_id_type_desc = "当数据类型为新增、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，个人证件类别要素必须为空。<br/>";
            }
            if (this.conUtil(in_out_sign, "02") > -1 && this.conUtil(cust_type, "01,02,03") > -1 && this.conUtil(personal_id_type, "21,22,23,24,99") <= -1) {
                personal_id_type_desc2 = "当数据类型为新增、投资者境内外标识为境外、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，个人证件类别只能填写外国护照、港澳往来内地通行证、台湾往来内地通行证、外国人永久居留证、其他。<br/>";
            } else if (this.conUtil(in_out_sign, "01") > -1 && this.conUtil(cust_type, "01,02,03") > -1 && this.conUtil(personal_id_type, "00,01,02,10,11,12,13,14,15,16,17,20,99") <= -1) {
                personal_id_type_desc2 = "当数据类型为新增、投资者境内外标识为境内、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，个人证件类别只能填写居民身份证、临时居民身份证、户口簿、军官证、警官证、文职干部证、士兵证、军事院校学员证、离休干部荣誉证、军官退休证、文职干部退休证、中华人民共和国护照、其他。<br/>";
            }
        }
        if (this.conUtil(data_type, "02") > -1) {//识别标识变更
            if (!"".equals(personal_id_type)) {
                personal_id_type_desc = "当数据类型为识别标识变更时，个人证件类别要素必须为空。<br/>";
            }
        }
        if (this.conUtil(data_type, "03") > -1 && isCustType) {//重要信息变更
            if (this.conUtil(cust_type, "01,02,03") > -1 && "".equals(personal_id_type)) {
                personal_id_type_desc = "当数据类型为重要信息变更、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，个人证件类别要素不可为空。<br/>";
            } else if (this.conUtil(cust_type, "04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27") > -1
                    && !"".equals(personal_id_type)) {
                personal_id_type_desc = "当数据类型为重要信息变更、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，个人证件类别要素必须为空。<br/>";
            }
            if (this.conUtil(in_out_sign, "02") > -1 && this.conUtil(cust_type, "01,02,03") > -1 && this.conUtil(personal_id_type, "21,22,23,24,99") <= -1) {
                personal_id_type_desc2 = "当数据类型为重要信息变更、投资者境内外标识为境外、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，个人证件类别只能填写外国护照、港澳往来内地通行证、台湾往来内地通行证、外国人永久居留证、其他。<br/>";
            } else if (this.conUtil(in_out_sign, "01") > -1 && this.conUtil(cust_type, "01,02,03") > -1 && this.conUtil(personal_id_type, "00,01,02,10,11,12,13,14,15,16,17,20,99") <= -1) {
                personal_id_type_desc2 = "当数据类型为重要信息变更、投资者境内外标识为境内、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，个人证件类别只能填写居民身份证、临时居民身份证、户口簿、军官证、警官证、文职干部证、士兵证、军事院校学员证、离休干部荣誉证、军官退休证、文职干部退休证、中华人民共和国护照、其他。<br/>";
            }
        }
        if (this.conUtil(data_type, "04") > -1) {//其他信息变更
            if (!"".equals(personal_id_type)) {
                personal_id_type_desc = "当数据类型为其他信息变更时，个人证件类别要素必须为空。<br/>";
            }
        }
        if (!"".equals(personal_id_type) && this.conUtil(personal_id_type, "00,01,02,10,11,12,13,14,15,16,17,20,21,22,23,24,99") <= -1) {
            personal_id_type_desc3 = "个人证件类别不在值域范围内。<br/>";
        }

        //机构证件类别
        String organization_id_type = StringUtils.isNotBlank(trCustRegisterInfo.getOrganizationIdType()) ? trCustRegisterInfo.getOrganizationIdType() : "";
        String organization_id_type_desc = "";
        String organization_id_type_desc2 = "";
        String organization_id_type_desc3 = "";
        if (this.conUtil(data_type, "01") > -1 && isCustType) {//新增
            if (this.conUtil(cust_type, "01,02,03") > -1 && !"".equals(organization_id_type)) {
                organization_id_type_desc = "当数据类型为新增、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，机构证件类别要素必须为空。<br/>";
            } else if (this.conUtil(cust_type, "04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26,27") > -1 && "".equals(organization_id_type)) {
                organization_id_type_desc = "当数据类型为新增、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，机构证件类别要素不可为空。<br/>";
            } else if (this.conUtil(cust_type, "17,18,19,20,21,22,23,27") > -1 && this.conUtil(organization_id_type, "34") < 0) {
                organization_id_type_desc = "当数据类型为新增、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，机构证件类别只能填写SPV登记编码。<br/>";
            } else if (this.conUtil(cust_type, "04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26") > -1 && this.conUtil(organization_id_type, "34") > -1) {
                organization_id_type_desc = "当数据类型为新增、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，机构证件类别不能填写SPV登记编码。<br/>";
            }
        } else if (this.conUtil(data_type, "02") > -1) {//识别标识变更
            if (!"".equals(organization_id_type)) {
                organization_id_type_desc = "当数据类型为识别标识变更时，机构证件类别要素必须为空。<br/>";
            }
        } else if (this.conUtil(data_type, "03") > -1 && isCustType) {//重要信息变更
            if (this.conUtil(cust_type, "01,02,03") > -1 && !"".equals(organization_id_type)) {
                organization_id_type_desc = "当数据类型为重要信息变更、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，机构证件类别要素必须为空。<br/>";
            } else if (this.conUtil(cust_type, "04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26,27") > -1 && "".equals(organization_id_type)) {
                organization_id_type_desc = "当数据类型为重要信息变更、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，机构证件类别要素不可为空。<br/>";
            } else if (this.conUtil(cust_type, "17,18,19,20,21,22,23,27") > -1 && this.conUtil(organization_id_type, "34") < 0) {
                organization_id_type_desc = "当数据类型为重要信息变更、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，机构证件类别只能填写SPV登记编码。<br/>";
            } else if (this.conUtil(cust_type, "04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26") > -1 && this.conUtil(organization_id_type, "34") > -1) {
                organization_id_type_desc = "当数据类型为重要信息变更、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，机构证件类别不能填写SPV登记编码。<br/>";
            }
        } else if (this.conUtil(data_type, "04") > -1) {//其他信息变更
            if (!"".equals(organization_id_type)) {
                organization_id_type_desc = "当数据类型为其他信息变更时，机构证件类别要素必须为空。<br/>";
            }
        }
        if (!"".equals(organization_id_type) && this.conUtil(organization_id_type, "01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,99") <= -1) {
            organization_id_type_desc2 = "机构证件类别不在值域范围内。<br/>";
        }
        //其他证件名称
        String other_id_name = StringUtils.isNotBlank(trCustRegisterInfo.getOtherIdName()) ? trCustRegisterInfo.getOtherIdName() : "";
        String other_id_name_desc = "";
        String other_id_name_desc2 = "";
        String other_id_name_desc3 = "";
        if (this.conUtil(data_type, "01") > -1 && isCustType) {//新增
            if (this.conUtil(cust_type, "01,02,03") > -1 && "99".equals(personal_id_type) && "".equals(other_id_name)) {
                other_id_name_desc = "当数据类型为新增、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时、个人证件类别为其他时，其他证件名称要素不可为空。<br/>";
            } else if (this.conUtil(cust_type, "04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26") > -1 && "99".equals(organization_id_type) && "".equals(other_id_name)) {
                other_id_name_desc = "当数据类型为新增、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时、机构证件类别为其他时，其他证件名称要素不可为空。<br/>";
            } else if (this.conUtil(cust_type, "01,02,03") > -1 && !"99".equals(personal_id_type) && !"".equals(other_id_name)) {
                other_id_name_desc = "当数据类型为新增、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，个人证件类别为其他以外的选项时，其他证件名称要素必须为空。<br/>";
            } else if (this.conUtil(cust_type, "04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26") > -1 && !"99".equals(organization_id_type) && !"".equals(other_id_name)) {
                other_id_name_desc = "当数据类型为新增、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，机构证件类别为其他以外的选项时，其他证件名称要素必须为空。<br/>";
            } else if (this.conUtil(cust_type, "17,18,19,20,21,22,23") > -1 && !"".equals(other_id_name)) {
                other_id_name_desc = "当数据类型为新增、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，其他证件名称要素必须为空。<br/>";
            }
        } else if (this.conUtil(data_type, "02") > -1) {//识别标识变更
            if (!"".equals(other_id_name)) {
                other_id_name_desc = "当数据类型为识别标识变更时，其他证件名称要素必须为空。<br/>";
            }
        } else if (this.conUtil(data_type, "03") > -1 && isCustType) {//重要信息变更
            if (this.conUtil(cust_type, "01,02,03") > -1 && "99".equals(personal_id_type) && "".equals(other_id_name)) {
                other_id_name_desc = "当数据类型为重要信息变更、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时、个人证件类别为其他时，其他证件名称要素不可为空。<br/>";
            } else if (this.conUtil(cust_type, "04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26") > -1 && "99".equals(organization_id_type) && "".equals(other_id_name)) {
                other_id_name_desc = "当数据类型为重要信息变更、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时、机构证件类别为其他时，其他证件名称要素不可为空。<br/>";
            } else if (this.conUtil(cust_type, "01,02,03") > -1 && !"99".equals(personal_id_type) && !"".equals(other_id_name)) {
                other_id_name_desc = "当数据类型为重要信息变更、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，个人证件类别为其他以外的选项时，其他证件名称要素必须为空。<br/>";
            } else if (this.conUtil(cust_type, "04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26") > -1 && !"99".equals(organization_id_type) && !"".equals(other_id_name)) {
                other_id_name_desc = "当数据类型为重要信息变更、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，机构证件类别为其他以外的选项时，其他证件名称要素必须为空。<br/>";
            } else if (this.conUtil(cust_type, "17,18,19,20,21,22,23,27") > -1 && !"".equals(other_id_name)) {
                other_id_name_desc = "当数据类型为重要信息变更、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，其他证件名称要素必须为空。<br/>";
            }
        } else if (this.conUtil(data_type, "04") > -1) {//其他信息变更
            if (!"".equals(other_id_name)) {
                other_id_name_desc = "当数据类型为其他信息变更时，其他证件名称要素必须为空。<br/>";
            }
        }
        if (CheckDataUtils.checkStringLengthOver(other_id_name,60)) {
            other_id_name_desc2 = "其他证件名称过长。<br/>";
        }
        //证件号码
        String id_code = StringUtils.isNotBlank(trCustRegisterInfo.getIdCode()) ? trCustRegisterInfo.getIdCode() : "";
        String id_code_desc = "";
        String id_code_desc2 = "";
        String id_code_desc3 = "";
        if (this.conUtil(data_type, "01") > -1) {//新增
            if ("".equals(id_code)) {
                id_code_desc = "当数据类型为新增时，证件号码要素不可为空。<br/>";
            } else if (this.conUtil(cust_type, "04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26") > -1 && "01".equals(organization_id_type) && id_code.length() != 18) {
                id_code_desc = "当数据类型为新增、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "、机构证件类别为法人和其他组织统一社会信用代码时，证件号码必须为固定长18位英文数字。<br/>";
            } else if (this.conUtil(cust_type, "04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26") > -1 && "02".equals(organization_id_type) && id_code.length() != 9) {
                id_code_desc = "当数据类型为新增、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "、机构证件类别为全国组织机构代码时，证件号码必须为固定长9位英文数字。<br/>";
            } else if (this.conUtil(cust_type, "01,02,03") > -1 && "01".equals(personal_id_type) && (id_code.length() != 15 && id_code.length() != 18)) {
                id_code_desc = "当数据类型为新增、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "、个人证件类别为临时居民身份证时，证件号码必须为固定长15位或者18位英文数字。<br/>";
            } else if (this.conUtil(cust_type, "01,02,03") > -1 && "00".equals(personal_id_type) && (id_code.length() != 15 && id_code.length() != 18)) {
                id_code_desc = "当数据类型为新增、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "、个人证件类别为居民身份证时，证件号码必须为固定长15位或者18位英文数字。<br/>";
            }
        } else if (this.conUtil(data_type, "02") > -1) {//识别标识变更
            if (!"".equals(id_code)) {
                id_code_desc = "当数据类型为识别标识变更时，证件号码要素必须为空。<br/>";
            }
        } else if (this.conUtil(data_type, "03") > -1) {//重要信息变更
            if ("".equals(id_code)) {
                id_code_desc = "当数据类型为重要信息变更时，证件号码要素不可为空。<br/>";
            } else if (this.conUtil(cust_type, "04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26") > -1 && this.conUtil(organization_id_type, "01") > -1 && id_code.length() != 18) {
                id_code_desc = "当数据类型为重要信息变更、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "、机构证件类别为法人和其他组织统一社会信用代码时，证件号码必须为固定长18位英文数字。<br/>";
            } else if (this.conUtil(cust_type, "04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26") > -1 && this.conUtil(organization_id_type, "02") > -1 && id_code.length() != 9) {
                id_code_desc = "当数据类型为重要信息变更、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "、机构证件类别为全国组织机构代码时，证件号码必须为固定长9位英文数字。<br/>";
            } else if (this.conUtil(cust_type, "01,02,03") > -1 && this.conUtil(personal_id_type, "00") > -1 && (id_code.length() != 15 && id_code.length() != 18)) {
                id_code_desc = "当数据类型为重要信息变更、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "、个人证件类别为居民身份证时，证件号码必须为固定长15位或者18位英文数字。<br/>";
            } else if (this.conUtil(cust_type, "01,02,03") > -1 && this.conUtil(personal_id_type, "01") > -1 && ((id_code.length() != 15 && id_code.length() != 18))) {
                id_code_desc = "当数据类型为重要信息变更、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "、个人证件类别为临时居民身份证时，证件号码必须为固定长15位或者18位英文数字。<br/>";
            }
        } else if (this.conUtil(data_type, "04") > -1) {//其他信息变更
            if (!"".equals(id_code)) {
                id_code_desc = "当数据类型为其他信息变更时，证件号码要素必须为空。<br/>";
            }
        }
        if (id_code.length() > 30) {
            id_code_desc2 = "证件号码过长。<br/>";
        }

        //SPV资金托管账户开户行
        String spv_open_bank = StringUtils.isNotBlank(trCustRegisterInfo.getSpvOpenBank()) ? trCustRegisterInfo.getSpvOpenBank() : "";
        String spv_open_bank_desc = "";
        String spv_open_bank_desc2 = "";
        String spv_open_bank_desc3 = "";
        if (this.conUtil(data_type, "01") > -1 && this.conUtil(cust_type, "01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26,27") > -1 && !"".equals(spv_open_bank)) {//新增
            spv_open_bank_desc = "当数据类型为新增、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，SPV资金托管账户开户行要素必须为空。<br/>";
        } else if (this.conUtil(data_type, "02") > -1) {//识别标识变更
            if (!"".equals(spv_open_bank)) {
                spv_open_bank_desc = "当数据类型为识别标识变更时，SPV资金托管账户开户行要素必须为空。<br/>";
            }
        } else if (this.conUtil(data_type, "03") > -1 && this.conUtil(cust_type, "01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,24,25,26,27") > -1 && !"".equals(spv_open_bank)) {//重要信息变更
            spv_open_bank_desc = "当数据类型为重要信息变更、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，SPV资金托管账户开户行要素必须为空。<br/>";
        } else if (this.conUtil(data_type, "04") > -1) {//其他信息变更
            if (!"".equals(spv_open_bank)) {
                spv_open_bank_desc = "当数据类型为其他信息变更时，SPV资金托管账户开户行要素必须为空。<br/>";
            }
        }
        if (!"".equals(spv_open_bank)) {
            if (spv_open_bank.length() > 6) {
                spv_open_bank_desc2 = "SPV资金托管账户开户行要素过长，只能为6位英文或数字组合。<br/>";
            }
            Pattern spvPt = Pattern.compile("^([a-zA-Z0-9]{6}$)");
            Matcher mspv = spvPt.matcher(spv_open_bank);
            boolean flagmspv = mspv.matches();
            if (!flagmspv) {
                spv_open_bank_desc2 = "SPV资金托管账户开户行要素只能为6位英文或数字组合。<br/>";
            }
        }

        //其他资金托管账户开户行
        String other_open_bank = StringUtils.isNotBlank(trCustRegisterInfo.getOtherOpenBank()) ? trCustRegisterInfo.getOtherOpenBank() : "";
        String other_open_bank_desc = "";
        String other_open_bank_desc2 = "";
        String other_open_bank_desc3 = "";
        String other_open_bank_desc4 = "";
        if (this.conUtil(data_type, "01") > -1) {//新增
            if ("01".equals(cust_type) && !"".equals(other_open_bank)) {
//                other_open_bank_desc = "当数据类型为新增、投资者类别为"+ this.getDictName("tr_cust_type", cust_type) + "时，其他资金托管账户开户行要素必须为空。<br/>";
                other_open_bank_desc = "当数据类型为新增、投资者类别为普通个人时，其他资金托管账户开户行要素必须为空。<br/>";
            } else if ("17".equals(cust_type) && "C10102".equals(spv_open_bank) && !"".equals(other_open_bank)) {
                other_open_bank_desc = "当数据类型为新增、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "、SPV资金托管账户开户行为中国工商银行股份有限公司时，其他资金托管账户开户行要素必须为空。<br/>";
//                other_open_bank_desc = "当数据类型为新增、投资者类别为信托产品、SPV资金托管账户开户行为中国工商银行股份有限公司时，其他资金托管账户开户行要素必须为空。<br/>";
            }
        } else if (this.conUtil(data_type, "02") > -1) {//识别标识变更
            if (!"".equals(other_open_bank)) {
                other_open_bank_desc = "当数据类型为识别标识变更时，其他资金托管账户开户行要素必须为空。<br/>";
            }
        } else if (this.conUtil(data_type, "03") > -1) {//重要信息变更
            if ("01".equals(cust_type) && !"".equals(other_open_bank)) {
                other_open_bank_desc = "当数据类型为重要信息变更、投资者类别为普通个人时，其他资金托管账户开户行要素必须为空。<br/>";
            } else if ("17".equals(cust_type) && "C10102".equals(spv_open_bank) && !"".equals(other_open_bank)) {
                other_open_bank_desc = "当数据类型为重要信息变更、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "、SPV资金托管账户开户行为中国工商银行股份有限公司时，其他资金托管账户开户行要素必须为空。<br/>";
//                other_open_bank_desc = "当数据类型为重要信息变更、投资者类别为信托产品、SPV资金托管账户开户行为中国工商银行股份有限公司时，其他资金托管账户开户行要素必须为空。<br/>";
            }
        } else if (this.conUtil(data_type, "04") > -1) {//其他信息变更
            if (!"".equals(other_open_bank)) {
                other_open_bank_desc = "当数据类型为其他信息变更时，其他资金托管账户开户行要素必须为空。<br/>";
            }
        }
        if (!(this.conUtil(data_type, "01,03") > -1
                && this.conUtil(cust_type, "17,18,19,20,21,22,23,27") > -1
                && "999999".equals(spv_open_bank))
                && !"".equals(other_open_bank)) {
            // 当数据类型为’新增‘、’重要信息变更‘，且投资者类别为普通个人时，其他资金托管账户开户行要素必须为空。
            other_open_bank_desc2 = "当数据类型为'新增'或'重要信息变更'、投资者类别为普通个人时，其他资金托管账户开户行要素必须为空。<br/>";
        }
        if (CheckDataUtils.checkStringLengthOver(other_open_bank,60)) {
            other_open_bank_desc3 = "其他资金托管账户开户行过长。<br/>";
        }
        //投资者名称
        String cust_name = StringUtils.isNotBlank(trCustRegisterInfo.getCustName()) ? trCustRegisterInfo.getCustName() : "";
        String cust_name_desc = "";
        String cust_name_desc2 = "";
        String cust_name_desc3 = "";
        if (this.conUtil(data_type, "01,03") > -1) {
            if ("".equals(cust_name)) {
                cust_name_desc = "当数据类型为新增或重要信息变更时，投资者名称要素不可为空。<br/>";
            } else {
                if (this.conUtil(in_out_sign, "01") > -1 && cust_name.indexOf(" ") > -1) { //修复境外投资者名称含有空格的bug 程晓鹏 2018.08.20 moidfy
                    cust_name_desc = "境内投资者名称中不得含有空格 <br/>";
                }
            }
        } else if (this.conUtil(data_type, "02,04") > -1 && !"".equals(cust_name)) {
            cust_name_desc = "当数据类型为识别标识变更或其他信息变更时，投资者名称要素必须为空。<br/>";
        }
        if (CheckDataUtils.checkStringLengthOver(cust_name,200)) {
            cust_name_desc2 = "投资者名称过长。<br/>";
        }

        //性别
        String sex = StringUtils.isNotBlank(trCustRegisterInfo.getSex()) ? trCustRegisterInfo.getSex() : "";
        String sex_desc = "";
        String sex_desc2 = "";
        String sex_desc3 = "";
        if (this.conUtil(data_type, "01,03") > -1 && this.conUtil(cust_type, "01,02,03") > -1 && "".equals(sex)) {
            sex_desc = "当数据类型为新增或重要信息变更、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，性别要素不可为空。<br/>";
        } else if (this.conUtil(data_type, "01,03") > -1 &&
                this.conUtil(cust_type, "04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27") > -1 && !"".equals(sex)) {
            sex_desc = "当数据类型为新增或重要信息变更、投资者类别为" + this.getDictName("tr_cust_type", cust_type) + "时，性别要素必须为空。<br/>";
        } else if (this.conUtil(data_type, "02,04") > -1 && !"".equals(sex)) {
            sex_desc = "当数据类型为识别标识变更或其他信息变更时，性别要素必须为空。<br/>";
        }
        if (!"".equals(sex) && this.conUtil(sex, "01,02") <= -1) {
            sex_desc2 = "性别不在值域范围内。<br/>";
        }

        //风险偏好
        String risk_level = StringUtils.isNotBlank(trCustRegisterInfo.getRiskLevel()) ? trCustRegisterInfo.getRiskLevel() : "";
        String risk_level_desc = "";
        String risk_level_desc2 = "";
        String risk_level_desc3 = "";
        if (this.conUtil(data_type, "02") > -1 && !"".equals(risk_level)) {
            risk_level_desc = "当数据类型为识别标识变更时，风险偏好要素必须为空。<br/>";
        }
        if (!"".equals(risk_level) && this.conUtil(risk_level, "01,02,03,04,05") <= -1) {
            risk_level_desc2 = "风险偏好不在值域范围内。<br/>";
        }

        //手机号码
        String moble = StringUtils.isNotBlank(trCustRegisterInfo.getMobleDisplay()) ? trCustRegisterInfo.getMobleDisplay() : StringUtils.isNotBlank(trCustRegisterInfo.getMoble()) ? trCustRegisterInfo.getMoble() : "";
        String moble_desc = "";
        String moble_desc2 = "";
        String moble_desc3 = "";
        boolean flagMobile = true;
        if (StringUtils.isNotBlank(moble)) {
            Pattern pMobile = Pattern.compile("^[\u0030-\u0039]{11}$");
            Matcher mMobile = pMobile.matcher(moble + "");
            flagMobile = mMobile.find();
        }

        if (this.conUtil(data_type, "01,03") > -1 && "".equals(moble)) {
            moble_desc = "当数据类型为新增或重要信息变更时，手机号码要素不可为空。<br/>";
        } else if (this.conUtil(data_type, "02") > -1 && !"".equals(moble)) {
            moble_desc = "当数据类型为识别标识变更时，手机号码要素必须为空。<br/>";
        }
        if (!flagMobile && !"".equals(moble)) {
            moble_desc2 = "手机号码不合法,合法的模式：13488996666（11位）<br/>";
        }

        //固定电话
        String tel_phone = StringUtils.isNotBlank(trCustRegisterInfo.getTelPhoneDisplay()) ? trCustRegisterInfo.getTelPhoneDisplay() : StringUtils.isNotBlank(trCustRegisterInfo.getTelPhone()) ? trCustRegisterInfo.getTelPhone() : "";
        String tel_phone_desc = "";
        String tel_phone_desc2 = "";
        String tel_phone_desc3 = "";
        String tel_phone_desc4 = "";
        String reg = "(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([0-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|"
                + "(?:(86-?)?(0[0-9]{2,3}\\-?)?([0-9][0-9]{6,7})+(\\-[0-9]{1,8})?)";

        boolean flagTel = Pattern.matches(reg, tel_phone);
        if (this.conUtil(data_type, "01,03") > -1 && "".equals(tel_phone)) {
            tel_phone_desc = "当数据类型为新增或重要信息变更时，固定电话要素不可为空。<br/>";
        } else if (this.conUtil(data_type, "02") > -1 && !"".equals(tel_phone)) {
            tel_phone_desc = "当数据类型为识别标识变更时，固定电话要素必须为空。<br/>";
        }
        if (tel_phone.length() > 30) {
            tel_phone_desc2 = "固定电话过长。<br/>";
        }
        if (!flagTel && !"".equals(tel_phone)) {
            tel_phone_desc3 = "固定电话格式有误。<br/>";
        }

        //电子邮箱
        String email = StringUtils.isNotBlank(trCustRegisterInfo.getEmailDisplay()) ? trCustRegisterInfo.getEmailDisplay() : StringUtils.isNotBlank(trCustRegisterInfo.getEmail()) ? trCustRegisterInfo.getEmail() : "";
        String email_desc = "";
        String email_desc2 = "";
        String email_desc3 = "";
        String email_desc4 = "";
        String email_desc5 = "";
        boolean flagEmail = true;
        if (StringUtils.isNotBlank(email)) {
            Pattern pEamil = Pattern.compile("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
            Matcher mEmail = pEamil.matcher(email + "");
            flagEmail = mEmail.find();
        }
        if (this.conUtil(data_type, "01,03") > -1 && "".equals(email)) {
            email_desc = "当数据类型为新增或重要信息变更时，电子邮箱要素不可为空。<br/>";
        } else if (this.conUtil(data_type, "02") > -1 && !"".equals(email)) {
            email_desc = "当数据类型为识别标识变更时，电子邮箱要素必须为空。<br/>";
        }
        if (email.length() > 50) {
            email_desc2 = "电子邮箱过长。<br/>";
        }
        if (!flagEmail && !"".equals(email)) {
            email_desc3 = "电子邮箱格式有误。<br/>";
        }

        //同空校验
        if (this.conUtil(data_type, "04") > -1) {
            if ("".equals(moble) && "".equals(tel_phone) && "".equals(email) && "".equals(risk_level)) {
                email_desc4 = "当数据类型为其他信息变更时，风险偏好、手机号码、固定电话、电子邮箱不可同时为空。<br/>";
            }
        }

        //备注
        String remark = StringUtils.isNotBlank(trCustRegisterInfo.getRemark()) ? trCustRegisterInfo.getRemark() : "";
        String remark_desc = "";
        String remark_desc2 = "";
        String remark_desc3 = "";
        if (this.conUtil(data_type, "02,04") > -1 && !"".equals(remark)) {
            remark_desc = "当数据类型为'识别标识变更'或'其他信息变更'时，投资者信息备注要素必须为空。<br/>";
        }
        if (!"".equals(remark) && CheckDataUtils.checkStringLengthOver(remark,256)) {
            remark_desc2 = "投资者信息备注过长。<br/>";
        }
        if (!"".equals(whiteregex)) {
            if (!bank_code.equals("") && !whiterpattern.matcher(bank_code).matches())
//                bank_code_desc = bank_code_desc + "登记银行代码必须填写白名单中的文字。<br/>";
                bank_code_desc4 = "登记银行代码必须填写白名单中的文字。<br/>";
            if (!is_belong.equals("") && !whiterpattern.matcher(is_belong).matches())
                is_belong_desc3 = "投资者是否属于本行必须填写白名单中的文字。<br/>";
            if (!iss_bank_name.equals("") && !whiterpattern.matcher(iss_bank_name).matches())
                iss_bank_name_desc4 = "投资者所属银行名称必须填写白名单中的文字。<br/>";
            if (!iss_bank_code.equals("") && !whiterpattern.matcher(iss_bank_code).matches())
                iss_bank_code_desc4 = "投资者所属银行代码必须填写白名单中的文字。<br/>";
            if (!in_out_sign.equals("") && !whiterpattern.matcher(in_out_sign).matches())
                in_out_sign_desc3 = "投资者境内外标识必须填写白名单中的文字。<br/>";
            if (!iss_country.equals("") && !whiterpattern.matcher(iss_country).matches())
                iss_country_desc3 = "投资者所属国家或地区必须填写白名单中的文字。<br/>";
            if (!data_type.equals("") && !whiterpattern.matcher(data_type).matches())
//                data_type_desc = data_type_desc + "数据类型必须填写白名单中的文字。<br/>";
                data_type_desc2 = "数据类型必须填写白名单中的文字。<br/>";
            if (!ori_cust_no.equals("") && !whiterpattern.matcher(ori_cust_no).matches())
                ori_cust_no_desc3 = "原识别标识必须填写白名单中的文字。<br/>";
            if (!cust_no.equals("") && !whiterpattern.matcher(cust_no).matches())
                cust_no_desc3 = "不得含有汉字，识别标识只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。<br/>";
            if (!cust_type.equals("") && !whiterpattern.matcher(cust_type).matches())
                cust_type_desc3 = "投资者类别必须填写白名单中的文字。<br/>";
            if (!personal_id_type.equals("") && !whiterpattern.matcher(personal_id_type).matches())
                personal_id_type_desc4 = "个人证件类别必须填写白名单中的文字。<br/>";
            if (!organization_id_type.equals("") && !whiterpattern.matcher(organization_id_type).matches())
                organization_id_type_desc3 = "机构证件类别必须填写白名单中的文字。<br/>";
            if (!other_id_name.equals("") && !whiterpattern.matcher(other_id_name).matches())
                other_id_name_desc3 = "其他证件名称必须填写白名单中的文字。<br/>";
            if (!id_code.equals("") && !whiterpattern.matcher(id_code).matches())
                id_code_desc3 = "证件号码必须填写白名单中的文字。<br/>";
            if (!spv_open_bank.equals("") && !whiterpattern.matcher(spv_open_bank).matches())
                spv_open_bank_desc3 = "SPV资金托管账户开户行必须填写白名单中的文字。<br/>";
            if (!other_open_bank.equals("") && !whiterpattern.matcher(other_open_bank).matches())
                other_open_bank_desc4 = "其他资金托管账户开户行必须填写白名单中的文字。<br/>";
            if (!cust_name.equals("") && !whiterpattern.matcher(cust_name).matches())
                cust_name_desc3 = "投资者名称必须填写白名单中的文字。<br/>";
            if (!sex.equals("") && !whiterpattern.matcher(sex).matches())
                sex_desc3 = "性别必须填写白名单中的文字。<br/>";
            if (!risk_level.equals("") && !whiterpattern.matcher(risk_level).matches())
                risk_level_desc3 = "风险偏好必须填写白名单中的文字。<br/>";
            if (!moble.equals("") && !whiterpattern.matcher(moble).matches())
                moble_desc3 = "手机号码必须填写白名单中的文字。<br/>";
            if (!tel_phone.equals("") && !whiterpattern.matcher(tel_phone).matches())
                tel_phone_desc4 = "固定电话必须填写白名单中的文字。<br/>";
            if (!email.equals("") && !whiterpattern.matcher(email).matches())
                email_desc5 = "电子邮箱必须填写白名单中的文字。<br/>";
            if (!remark.equals("") && !whiterpattern.matcher(remark).matches())
                remark_desc3 = "备注必须填写白名单中的文字。<br/>";
        }
        StringBuffer stringErr = new StringBuffer();

        stringErr.append(bank_code_desc);
        stringErr.append(bank_code_desc2);
        stringErr.append(bank_code_desc3);
        stringErr.append(bank_code_desc4);
        stringErr.append(is_belong_desc);
        stringErr.append(is_belong_desc2);
        stringErr.append(is_belong_desc3);
        stringErr.append(iss_bank_name_desc);
        stringErr.append(iss_bank_name_desc2);
        stringErr.append(iss_bank_name_desc3);
        stringErr.append(iss_bank_name_desc4);
        stringErr.append(iss_bank_code_desc);
        stringErr.append(iss_bank_code_desc2);
        stringErr.append(iss_bank_code_desc3);
        stringErr.append(iss_bank_code_desc4);
        stringErr.append(in_out_sign_desc);
        stringErr.append(in_out_sign_desc2);
        stringErr.append(in_out_sign_desc3);
        stringErr.append(iss_country_desc);
        stringErr.append(iss_country_desc2);
        stringErr.append(iss_country_desc3);
        stringErr.append(data_type_desc);
        stringErr.append(data_type_desc2);
        stringErr.append(cust_no_desc);
        stringErr.append(cust_no_desc2);
        stringErr.append(cust_no_desc3);
        stringErr.append(ori_cust_no_desc);
        stringErr.append(ori_cust_no_desc2);
        stringErr.append(ori_cust_no_desc3);
        stringErr.append(cust_type_desc);
        stringErr.append(cust_type_desc2);
        stringErr.append(cust_type_desc3);
        stringErr.append(personal_id_type_desc);
        stringErr.append(personal_id_type_desc2);
        stringErr.append(personal_id_type_desc3);
        stringErr.append(personal_id_type_desc4);
        stringErr.append(organization_id_type_desc);
        stringErr.append(organization_id_type_desc2);
        stringErr.append(organization_id_type_desc3);
        stringErr.append(other_id_name_desc);
        stringErr.append(other_id_name_desc2);
        stringErr.append(other_id_name_desc3);
        stringErr.append(id_code_desc);
        stringErr.append(id_code_desc2);
        stringErr.append(id_code_desc3);
        stringErr.append(spv_open_bank_desc);
        stringErr.append(spv_open_bank_desc2);
        stringErr.append(spv_open_bank_desc3);
        stringErr.append(other_open_bank_desc);
        stringErr.append(other_open_bank_desc2);
        stringErr.append(other_open_bank_desc3);
        stringErr.append(other_open_bank_desc4);
        stringErr.append(cust_name_desc);
        stringErr.append(cust_name_desc2);
        stringErr.append(cust_name_desc3);
        stringErr.append(sex_desc);
        stringErr.append(sex_desc2);
        stringErr.append(sex_desc3);
        stringErr.append(risk_level_desc);
        stringErr.append(risk_level_desc2);
        stringErr.append(risk_level_desc3);
        stringErr.append(moble_desc);
        stringErr.append(moble_desc2);
        stringErr.append(moble_desc3);
        stringErr.append(tel_phone_desc);
        stringErr.append(tel_phone_desc2);
        stringErr.append(tel_phone_desc3);
        stringErr.append(tel_phone_desc4);
        stringErr.append(email_desc);
        stringErr.append(email_desc2);
        stringErr.append(email_desc3);
        stringErr.append(email_desc4);
        stringErr.append(email_desc5);
        stringErr.append(remark_desc);
        stringErr.append(remark_desc2);
        stringErr.append(remark_desc3);

        if (StringUtils.isNotBlank(stringErr.toString().replace(" ", "").replace("\n", ""))) {
            return stringErr.toString();
        } else {
            return "";
        }
    }

    /**
     * 校验dataname是否包含regex中字符集
     *
     * @param dataname dataname.contains(regexs[i]  并不能完全判定值域包含 例如 001 新增  {00,01,02}  将返回true
     * @param regex
     * @return
     */
    public static int conUtil(String dataname, String regex) {
        int ret = -1;
        if (dataname.contains(" ")) {//如果存在空格  01 新增
            dataname = dataname.substring(0, dataname.indexOf(" ")); //01
        }
        String[] split = regex.split(",");//得到字符串数组
        List<String> strings = Arrays.asList(split);//将数组转化为List
        return strings.contains(dataname) ? 0 : -1;
    }


    /**
     * 获取字典名称
     *
     * @param dict
     * @param key
     * @return
     * @throws Exception
     */
    public static String getDictName(String dict, String key) throws Exception {
        String out_value = "";

        if ("".equals(key) || key == null) {
            return out_value;
        }
        if (key.contains(" ")) {
            key = key.split(" ")[0];
        }

        if (CheckDataParams.dict_name.get(dict) == null) {
            throw new Exception("数据字典base_ex_map：" + dict + "不存在");
        }
        out_value = CheckDataParams.dict_name.get(dict).get(key);
        out_value = out_value == null ? "" : out_value;
        return out_value;
    }

    public Map<String, Map<String, String>> paramToMap(List<SqlRow> sqlRow) {
        Map<String, Map<String, String>> listToMap = new HashMap<>();

        List<String> str = sqlRow.stream().map(map -> map.getString("dict")).distinct().collect(Collectors.toList());
        for (String dict : str) {
            Map<String, String> m = new HashMap<>();
            for (SqlRow sr : sqlRow) {
                String dict1 = sr.getString("dict");
                if (dict.equals(dict1)) {
                    m.put(sr.getString("k"), sr.getString("v"));
                }
            }
            listToMap.put(dict, m);
        }
        return listToMap;
    }
}