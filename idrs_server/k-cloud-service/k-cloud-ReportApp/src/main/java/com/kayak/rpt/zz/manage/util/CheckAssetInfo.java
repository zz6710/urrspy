package com.kayak.rpt.zz.manage.util;

import com.kayak.rpt.zz.manage.model.AssetDebtRegisterInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//bryan
public class CheckAssetInfo {
    private static final Logger log = LoggerFactory.getLogger(CheckAssetInfo.class);

    /**
     * 存款到非标
     * @param whiteregex
     * @param whitereForCode
     * @param assetDebtRegisterInfo
     * @param assetType
     * @return
     */
    public static String checkAsset_1(String whiteregex,String whitereForCode, AssetDebtRegisterInfo assetDebtRegisterInfo,String assetType) throws Exception {
        StringBuffer stringErr = new StringBuffer();
        //----本行/他行存款||大额存单---
        //资金存入银行
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getBbDepositBank(),"资金存入银行","1003,1005",assetType,"本行存款","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getBbDepositBank(),"资金存入银行",200,"0"));
        //存款账号
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getBbAccountNo(),"存款账号","1002,1003,1004,1005",assetType,"本行存款","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getBbAccountNo(),"存款账号",60,"0"));
        //存款金额
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getBbDepositAmt(),"存款金额","1002,1003,1004,1005",assetType,"本行存款","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getBbDepositAmt(), "存款金额", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "0", "1"));
        //起息日
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getBbValueDate(),"起息日（现金及银行存款）","1002,1003,1004,1005",assetType,"本行存款","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getBbValueDate(),"起息日（现金及银行存款）"));
        //到期日
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getBbMaturityDate(),"到期日（现金及银行存款）","1002,1003,1004,1005",assetType,"本行存款","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getBbMaturityDate(),"到期日（现金及银行存款）"));
        //年利率%（现金及银行存款）
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getBbAnnualRate(),"年利率%（现金及银行存款）","1002,1003,1004,1005",assetType,"本行存款","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getBbAnnualRate(), "年利率%（现金及银行存款）", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));
        //计息基础（现金及银行存款）
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getBbInterestBasis(),"计息基础（现金及银行存款）","1002,1003,1004,1005",assetType,"本行存款","1"));
        //存款类型（现金及银行存款）
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getBbDepositType(),"存款类型（现金及银行存款）","1003,1002",assetType,"本行存款","1"));
        //结构性存款标的类别
        String value = assetDebtRegisterInfo.getBbStructDepositType();
        if("1003,1002".contains(assetType) && assetDebtRegisterInfo.getBbDepositType().contains("06") && StringUtils.isBlank(value) ){
            stringErr.append("当资产/负债类别为本行存款、存款类型为结构性存款时，结构性存款挂钩标的类别要素不可为空。<br/>");
        }else if("1003,1002".contains(assetType) && !assetDebtRegisterInfo.getBbDepositType().contains("06") && StringUtils.isNotBlank(value)){
            stringErr.append("当资产/负债类别为本行存款、存款类型为定期存款时，结构性存款挂钩标的类别要素必须为空。<br/>");
        }else if(!"1003,1002".contains(assetType) && StringUtils.isNotBlank(value) ){
            stringErr.append("当资产/负债类别为国债时，结构性存款挂钩标的类别要素必须为空。<br/>");
        }

        value = assetDebtRegisterInfo.getBbStructDeposit();
        //结构性存款挂钩标的
        if("1003,1002".contains(assetType) && assetDebtRegisterInfo.getBbDepositType().contains("06") && StringUtils.isBlank(value) ){
            stringErr.append("当资产/负债类别为本行存款、存款类型为结构性存款时，结构性存款挂钩标的要素不可为空。<br/>");
        }else if("1003,1002".contains(assetType) && !assetDebtRegisterInfo.getBbDepositType().contains("06") && StringUtils.isNotBlank(value)){
            stringErr.append("当资产/负债类别为本行存款、存款类型为定期存款时，结构性存款挂钩标的要素必须为空。<br/>");
        }else if(!"1003,1002".contains(assetType) && StringUtils.isNotBlank(value) ){
            stringErr.append("当资产/负债类别为国债时，结构性存款挂钩标的要素必须为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getBbDepositType(),"结构性存款挂钩标的",256,"0"));
        if(StringUtils.isNotBlank(stringErr)){
            return stringErr.toString();
        }
        //----2 债券类资产/理财直接融资工具/同业存单---
        //代码
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getCcIdentCode(),"代码","1101,1110,1109,1102,1111,1112,1113,1114,1115,1116,1117,2101,2501,2502",assetType,"债券","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getCcIdentCode(),"代码",15,"0"));
        if(!"".equals(whitereForCode) && StringUtils.isNotBlank(assetDebtRegisterInfo.getCcIdentCode())){
            Pattern whiterpattern1 = Pattern.compile(whitereForCode);
            if(!whiterpattern1.matcher(assetDebtRegisterInfo.getCcIdentCode()).matches()){
                stringErr.append("代码只能含有白名单内的阿拉伯数字，英文字母，半角符号，全角符号。");
            }
        }
        //名称（债券、理财直融工具、同业存单）
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getCcName(),"名称（债券、理财直融工具、同业存单）","1101,1110,1109,1102,1111,1112,1113,1114,1115,1116,1117,2101,2501,2502",assetType,"债券","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getCcName(),"名称（债券、理财直融工具、同业存单）",200,"0"));

        //具体类别
        value = assetDebtRegisterInfo.getCcSpecificBondType();
        if(!"1101,1110,1109,1102,1111,1112,1113,1114,1115,1116,1117,2101,2501,2502".contains(assetType) && StringUtils.isNotBlank(value) ){
            stringErr.append("当资产/负债类别为本行存款时，具体类别要素必须为空。<br/>");
        }else if("1101,1110,1109,1102,1111,1112,1113,1114,1115,1116,1117,2101,2501,2502".contains(assetType) ){
            if("1112,1113,1114,1115,1116,1117".contains(assetType) && StringUtils.isBlank(value) ){
                stringErr.append("当资产/负债类别为商业性金融债券时，具体类别要素不可为空。<br/>");
            }else if("1112".contains(assetType) && !"01,02,03,014,05,06,07,08,09,10,99".contains(value)){
                stringErr.append("资产/负债类别选择商业性金融债券时，具体类别必须选择01-10、99中的一项。<br/>");
            }else if("1113".contains(assetType) && !"11,12,99".contains(value)){
                stringErr.append("资产/负债类别选择企业债券时，具体类别必须选择11、12、99中的一项。<br/>");
            }else if("1114".contains(assetType) && !"13,14,15,99".contains(value)){
                stringErr.append("资产/负债类别选择公司债券时，具体类别必须选择13-15、99中的一项。<br/>");
            }else if("1115".contains(assetType) && !"16,17,18,19,20,21,99".contains(value)){
                stringErr.append("资产/负债类别选择企业债务融资工具时，具体类别必须选择16-21、99中的一项。<br/>");
            }else if("1116".contains(assetType) && !"22,23,24,99".contains(value)){
                stringErr.append("资产/负债类别选择资产支持证券时，具体类别必须选择22-24、99中的一项。<br/>");
            }else if("1117".contains(assetType) && !"25,26,27,28,99".contains(value)){
                stringErr.append("资产/负债类别选择外国债券时，具体类别必须选择25-28、99中的一项。<br/>");
            }
        }

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getCcIssModeBond(),"发行方式","1101,1110,1109,1102,1111,1112,1113,1114,1115,1116,1117,2101,2501,2502",assetType,"债券","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getCcIssRatePart(),"主体评级","1101,1110,1109,1102,1111,1112,1113,1114,1115,1116,1117,2101,2501,2502",assetType,"债券","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getCcInstituteTypeScale(),"发行机构类型（按规模划分）","1101,1110,1109,1102,1111,1112,1113,1114,1115,1116,1117,2101,2501,2502",assetType,"债券","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getCcInstituteTypeTech(),"发行机构类型（按技术领域划分）","1101,1110,1109,1102,1111,1112,1113,1114,1115,1116,1117,2101,2501,2502",assetType,"债券","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getCcInstituteTypeEconomic(),"发行机构类型（按经济类型划分）","1101,1110,1109,1102,1111,1112,1113,1114,1115,1116,1117,2101,2501,2502",assetType,"债券","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getCcIndustryIssuer(),"发行机构所属行业（债券、理财直融工具、同业存单）","1101,1110,1109,1102,1111,1112,1113,1114,1115,1116,1117,2101,2501,2502",assetType,"债券","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getCcRegistDeposit(),"登记托管机构","1101,1110,1109,1102,1111,1112,1113,1114,1115,1116,1117,2101,2501,2502",assetType,"债券","1"));

        value = assetDebtRegisterInfo.getCcDetailsRegistDeposit();
        if(!"1101,1110,1109,1102,1111,1112,1113,1114,1115,1116,1117,2101,2501,2502".contains(assetType) && StringUtils.isNotBlank(value) ){
            stringErr.append("当资产/负债类别为本行存款时，登记托管机构说明必须为空。<br/>");
        }else if(StringUtils.isNotBlank(assetDebtRegisterInfo.getCcRegistDeposit()) && assetDebtRegisterInfo.getCcRegistDeposit().contains("99") && StringUtils.isBlank(value)  ){
            stringErr.append("当资产/负债类别为国债、登记托管机构为其他时，登记托管机构说明要素不可为空。<br/>");
        }else if(StringUtils.isNotBlank(assetDebtRegisterInfo.getCcRegistDeposit()) && !assetDebtRegisterInfo.getCcRegistDeposit().contains("99") && StringUtils.isNotBlank(value)  ){
            stringErr.append("当资产/负债类别为国债、登记托管机构为上海清算所时，登记托管机构说明要素必须为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getCcDetailsRegistDeposit(),"登记托管机构说明",256,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getDdValueDate(),"起息日（拆放同业及债券买入返售/同业拆入及卖出回购）","1901,1902,1107,1108",assetType,"拆放同业","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getDdValueDate(),"起息日（拆放同业及债券买入返售/同业拆入及卖出回购）"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getDdMaturityDate(),"到期日（拆放同业及债券买入返售/同业拆入及卖出回购）","1901,1902,1107,1108",assetType,"拆放同业","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getDdMaturityDate(),"到期日（拆放同业及债券买入返售/同业拆入及卖出回购）"));
        if(StringUtils.isNotBlank(assetDebtRegisterInfo.getDdMaturityDate())&&StringUtils.isNotBlank(assetDebtRegisterInfo.getDdValueDate())&&assetDebtRegisterInfo.getDdMaturityDate().compareTo(assetDebtRegisterInfo.getDdValueDate()) < 0){
            stringErr.append("到期日（拆放同业及债券买入返售/同业拆入及卖出回购）必须大于等于起息日（拆放同业及债券买入返售/同业拆入及卖出回购）。<br/>");
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getDdCounterparty(),"对手方（拆放同业及债券买入返售/同业拆入及卖出回购）","1901,1902,1107,1108",assetType,"拆放同业","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getDdCounterparty(),"对手方（拆放同业及债券买入返售/同业拆入及卖出回购）",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getDdCounterpartyType(),"对手方类型（拆放同业及债券买入返售/同业拆入及卖出回购）","1901,1902,1107,1108",assetType,"拆放同业","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getDdAnnalInterestRate(),"年利率%（拆放同业及债券买入返售/同业拆入及卖出回购）","1901,1902,1107,1108",assetType,"拆放同业","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getDdAnnalInterestRate(), "年利率%（拆放同业及债券买入返售/同业拆入及卖出回购）", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getDdInterestBasis(),"计息基础（拆放同业及债券买入返售/同业拆入及卖出回购）","1901,1902,1107,1108",assetType,"拆放同业","1"));
        //回购标的类别
        value = assetDebtRegisterInfo.getDdCollateralType();
        if(!"1108,1902".contains(assetType) && StringUtils.isNotBlank(value) ){
            stringErr.append("当资产/负债类别为本行存款时，回购标的类别要素必须为空。<br/>");
        }else if("1108,1902".contains(assetType)  && StringUtils.isBlank(value)  ){
            stringErr.append("当资产/负债类别为本行存款时，回购标的类别要素不可为空。<br/>");
        }else if("1108".equals(assetType) && !value.contains("01") ){
            stringErr.append("当资产/负债类别为债券买入返售时，回购标的类别只能填写债券。<br/>");
        }

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getDdCollateralValue(),"回购标的金额","1902,1108",assetType,"卖出回购","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getDdCollateralValue(), "回购标的金额", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeName(),"名称（非标准化债权类资产）","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getEeName(),"名称（非标准化债权类资产）",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeAssetCode(),"资产代码","2202",assetType,"信贷资产流转和收益权转让产品","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getEeAssetCode(),"资产代码",10,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeAmt(),"金额（非标准化债权类资产）","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getEeAmt(), "金额（非标准化债权类资产）", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeUnitParValue(),"份额面值","1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getEeUnitParValue(), "份额面值", "^(\\d{1,13}(\\.\\d{1,4})?)", "n（17,4）", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeOwnershipType(),"收/受权益类型","1205",assetType,"收/受益权","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeBuyback(),"是否属于买入返售（非标准化债权类资产）","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeValueDate(),"起息日（非标准化债权类资产）","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getEeValueDate(),"起息日（非标准化债权类资产）"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeMaturityDate(),"到期日","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getEeMaturityDate(),"到期日（非标准化债权类资产）"));
        if(StringUtils.isNotBlank(assetDebtRegisterInfo.getEeMaturityDate())&&StringUtils.isNotBlank(assetDebtRegisterInfo.getEeValueDate())&&assetDebtRegisterInfo.getEeMaturityDate().compareTo(assetDebtRegisterInfo.getEeValueDate()) <= 0){
            stringErr.append("到期日（非标准化债权类资产）应大于“起息日（非标准化债权类资产）”");
        }

        if(!"1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213".contains(assetType) && StringUtils.isNotBlank(assetDebtRegisterInfo.getEeProjectDt()) ){
            stringErr.append("当资产/负债类别为本行存款时，法定到期日要素必须为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getEeProjectDt(),"法定到期日"));

//		stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeStatutoryMaturityDate(),"项目期限","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeExpectedReturn(),"是否有预期收益率（非标准化债权类资产）","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));


       value =ObjectUtils.allNotNull(assetDebtRegisterInfo.getEeProjectAnnaulReturn())?assetDebtRegisterInfo.getEeProjectAnnaulReturn().toString():"";
        if("1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213".contains(assetType)  ){
            if(assetDebtRegisterInfo.getEeExpectedReturn().contains("01") && StringUtils.isBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、是否有预期收益率（非标准化债权类资产）为是时，项目收益率（利率）%（非标准化债权类资产）要素不可为空。<br/>");
            }else if(assetDebtRegisterInfo.getEeExpectedReturn().contains("02") && StringUtils.isNotBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、是否有预期收益率（非标准化债权类资产）为否时，项目收益率（利率）%（非标准化债权类资产）要素必须为空。<br/>");
            }
        }else if(!"1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213".contains(assetType) && StringUtils.isNotBlank(value)  ){
            stringErr.append("当资产/负债类别为本行存款时，项目收益率（利率）%（非标准化债权类资产）要素必须为空<br/>");
        }
        stringErr.append(CheckDataUtils.checkMoney(value, "项目收益率（利率）%（非标准化债权类资产）", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeCouponType(),"计息类型","1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeRegualrInterestPay(),"规则付息标识","1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeInterestPayFrequency(),"付息频率（个月/次）（非标准化债权类资产）","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getEeInterestPayFrequency(), "付息频率（个月/次）（非标准化债权类资产）", "^(\\d{1,2})", "n..2", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeCouponAllocationType(),"利息分布方式","1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeDetailPrincInterest(),"还本付息情况说明","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeInterestBasis(),"计息基础（非标准化债权类资产）","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));

        value = assetDebtRegisterInfo.getEeEnhanceInstituteCode();
        stringErr.append(CheckDataUtils.checkAssetEmpty(value,"增信机构代码","1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213",assetType,"信托贷款","1"));
        if(StringUtils.isNotBlank(value)){
            Pattern p9=Pattern.compile("^[A-Z0-9]{9}");
            Pattern p18=Pattern.compile("^[A-Z0-9]{18}");
            if(!(p9.matcher(value).matches() || p18.matcher(value).matches() || value.equals("无"))){
                stringErr.append("增信机构代码要素格式不对正确的格式：9或18位大写字母或数字，或者无。<br/>");
            }
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeEnhanceInstituteName(),"增信机构名称","1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getEeEnhanceInstituteName(),"增信机构名称",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeBenchRateType(),"基准利率种类","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeFloatFactor(),"是否有浮动因子","1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213",assetType,"信托贷款","1"));
        value =ObjectUtils.allNotNull(assetDebtRegisterInfo.getEeFloatRate())?assetDebtRegisterInfo.getEeFloatRate().toString():"";
        if("1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213".contains(assetType)  ){
            if(assetDebtRegisterInfo.getEeFloatFactor().contains("01") && StringUtils.isBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、是否有浮动因子为是时，浮动因子要素不可为空。<br/>");
            }else if(assetDebtRegisterInfo.getEeFloatFactor().contains("02") && StringUtils.isNotBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、是否有浮动因子为否时，浮动因子要素必须为空。<br/>");
            }
        }else if(!"1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213".contains(assetType) && StringUtils.isNotBlank(value)  ){
            stringErr.append("当资产/负债类别为本行存款时，浮动因子要素必须为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkMoney(value, "浮动因子（%）", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(ObjectUtils.allNotNull(assetDebtRegisterInfo.getEeYieldSpreadBp())?assetDebtRegisterInfo.getEeYieldSpreadBp().toString():"","利差(BP)","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","0"));
        stringErr.append(CheckDataUtils.checkMoney(ObjectUtils.allNotNull(assetDebtRegisterInfo.getEeYieldSpreadBp())?assetDebtRegisterInfo.getEeYieldSpreadBp().toString():"", "利差(BP)", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeStructGrade(),"结构档次","1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213",assetType,"信托贷款","0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEePrincPaymentType(),"还本方式","1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213",assetType,"信托贷款","0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeInstallRepayType(),"分期还本条款标识","1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeBaseAssetType(),"基础资产类型","1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213",assetType,"信托贷款","0"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getEeBaseAssetType(),"基础资产类型",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(ObjectUtils.allNotNull(assetDebtRegisterInfo.getEePercentExcInAllot())?assetDebtRegisterInfo.getEePercentExcInAllot().toString():"","超额收益分配比例（%）","1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213",assetType,"信托贷款","0"));
        stringErr.append(CheckDataUtils.checkMoney(ObjectUtils.allNotNull(assetDebtRegisterInfo.getEePercentExcInAllot())?assetDebtRegisterInfo.getEePercentExcInAllot().toString():"", "超额收益分配比例（%）", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeDebtor(),"融资人（非标准化债权类资产）","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getEeDebtor(),"融资人（非标准化债权类资产）",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeDeptorRate(),"融资人内部信用评级","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getEeDeptorRate(),"融资人内部信用评级",10,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeRateAgencyIss(),"外部评级机构名称及对融资人评级结果（非标准化债权类资产）","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","0"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getEeRateAgencyIss(),"外部评级机构名称及对融资人评级结果（非标准化债权类资产）",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeDebtorTypeScale(),"融资人类型（按规模划分）（非标准化债权类资产）","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeDebtorTypeTech(),"融资人类型（按技术领域划分）（非标准化债权类资产）（非标准化债权类资产）","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeDebtorTypeEconomic(),"融资人类型（按经济类型划分）（非标准化债权类资产）","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeProject(),"融资项目（非标准化债权类资产）","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getEeProject(),"融资项目（非标准化债权类资产）",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeIndustryDebtor(),"融资人所属行业（非标准化债权类资产）","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeIndustryProject(),"融资项目所属行业（非标准化债权类资产）","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeMonitorIndusType(),"项目是否属于重点监控行业和领域","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));

        value = assetDebtRegisterInfo.getEeMonitorIndustryType();
        if("1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213".contains(assetType)  ){
            if(assetDebtRegisterInfo.getEeMonitorIndusType().contains("01") && StringUtils.isBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、项目是否属于重点监控行业和领域为是时，重点监控行业和领域类别要素不可为空。<br/>");
            }else if(assetDebtRegisterInfo.getEeMonitorIndusType().contains("02") && StringUtils.isNotBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、项目是否属于重点监控行业和领域为否时，重点监控行业和领域类别要素必须为空。<br/>");
            }
        }else if(!"1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213".contains(assetType) && StringUtils.isNotBlank(value)  ){
            stringErr.append("当资产/负债类别为本行存款时，重点监控行业和领域类别要素必须为空。<br/>");
        }

        value = assetDebtRegisterInfo.getEeDetailsMonitoryType();
        if("1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213".contains(assetType)  ){
            if(assetDebtRegisterInfo.getEeMonitorIndusType().contains("01")
                    && assetDebtRegisterInfo.getEeMonitorIndustryType().contains("99") && StringUtils.isBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、项目是否属于重点监控行业和领域为是、重点监控行业和领域类别为其他时，重点监控行业和领域类别说明要素不可为空。<br/>");
            }else if(assetDebtRegisterInfo.getEeMonitorIndusType().contains("02") && StringUtils.isNotBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、项目是否属于重点监控行业和领域为否时，重点监控行业和领域类别说明要素必须为空。<br/>");
            }
        }else if(!"1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213".contains(assetType) && StringUtils.isNotBlank(value)  ){
            stringErr.append("当资产/负债类别为本行存款时，重点监控行业和领域类别说明要素必须为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkStringLength(value,"重点监控行业和领域类别说明",256,"0"));

        String eeGuaranteeMethod = assetDebtRegisterInfo.getEeGuaranteeMethod();
        stringErr.append(CheckDataUtils.checkAssetEmpty(eeGuaranteeMethod,"担保方式","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","0"));

        value = assetDebtRegisterInfo.getEeDetailGuaranteeStatus();
        if(StringUtils.isNotBlank(eeGuaranteeMethod) && StringUtils.isBlank(value)){
            stringErr.append("当资产/负债类别为信托贷款、担保方式不为空时，担保情况说明要素不可为空。<br/>");
        }else if(StringUtils.isBlank(eeGuaranteeMethod) && StringUtils.isNotBlank(value)){
            stringErr.append("当资产/负债类别为信托贷款、担保方式为空时，担保情况说明要素不可填写。<br/>");
        }
        stringErr.append(CheckDataUtils.checkStringLength(value,"担保情况说明",200,"0"));


        value = assetDebtRegisterInfo.getEePledgeType();
        if(!"1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213".contains(assetType) && StringUtils.isNotBlank(value)){
            stringErr.append("当资产/负债类别为本行存款时，抵质押物类型（非标准化债权类资产）要素必须为空。<br/>");
        }else if(StringUtils.isNotBlank(eeGuaranteeMethod) ){
            if("03,04".contains(eeGuaranteeMethod) && StringUtils.isBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、担保方式为抵押或质押时，抵质押物类型（非标准化债权类资产）要素不可为空。<br/>");
            }else if(!"03,04".contains(eeGuaranteeMethod) && StringUtils.isNotBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、担保方式为信用担保时，抵质押物类型（非标准化债权类资产）要素必须为空。<br/>");
            }
        }else if(StringUtils.isBlank(eeGuaranteeMethod) && StringUtils.isNotBlank(value)){
            stringErr.append("当担保方式为空时，抵质押物类型（非标准化债权类资产）必须为空。<br/>");
        }

        value = assetDebtRegisterInfo.getEePledgeValue();
        if(!"1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213".contains(assetType) && StringUtils.isNotBlank(value)){
            stringErr.append("资产/负债类别为本行存款时，抵质押物价值（非标准化债权类资产）要素必须为空。<br/>");
        }else if(StringUtils.isNotBlank(eeGuaranteeMethod) ){
            if("03,04".contains(eeGuaranteeMethod) && StringUtils.isBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、担保方式为抵押时，抵质押物价值（非标准化债权类资产）要素不可为空。<br/>");
            }else if(!"03,04".contains(eeGuaranteeMethod) && StringUtils.isNotBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、担保方式为信用担保时，抵质押物价值（非标准化债权类资产）要素必须为空。<br/>");
            }
        }

        value = assetDebtRegisterInfo.getEeGuaranteeType();
        if(!"1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213".contains(assetType) && StringUtils.isNotBlank(value)){
            stringErr.append("当资产/负债类别为本行存款时，担保性质（非标准化债权类资产）要素必须为空。<br/>");
        }else if(StringUtils.isNotBlank(eeGuaranteeMethod) ){
            if("01,02".contains(eeGuaranteeMethod) && StringUtils.isBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、担保方式为信用担保时，担保性质（非标准化债权类资产）要素不可为空。<br/>");
            }else if("03,04".contains(eeGuaranteeMethod) && StringUtils.isNotBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、担保方式为抵押时，担保性质（非标准化债权类资产）要素必须为空。<br/>");
            }
        }

        value = assetDebtRegisterInfo.getEeGuarantorType();
        if(!"1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213".contains(assetType) && StringUtils.isNotBlank(value)){
            stringErr.append("当资产/负债类别为本行存款时，担保人与融资人关系（非标准化债权类资产）要素必须为空。<br/>");
        }else if(StringUtils.isNotBlank(eeGuaranteeMethod) ){
            if("01,02".contains(eeGuaranteeMethod) && StringUtils.isBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、担保方式为信用担保时，担保人与融资人关系（非标准化债权类资产）要素不可为空。<br/>");
            }else if("03,04".contains(eeGuaranteeMethod) && StringUtils.isNotBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、担保方式为抵押时，担保人与融资人关系（非标准化债权类资产）要素必须为空。<br/>");
            }
        }


        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeDebtorRate(),"融资人主体评级","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeInterAssetRate(),"资产内部评级","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getEeInterAssetRate(),"资产内部评级",20,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeOutAssetRate(),"资产外部评级","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));

        String eeOptionType = assetDebtRegisterInfo.getEeOptionType();
        stringErr.append(CheckDataUtils.checkAssetEmpty(eeOptionType,"含权类型","1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213",assetType,"信托贷款","0"));
        if(StringUtils.isBlank(eeOptionType) && StringUtils.isNotBlank(assetDebtRegisterInfo.getEeExerciseDateType())){
            stringErr.append("当含权类型为空时，行权方式必须为空。<br/>");
        }

        String eeExerciseDateType = assetDebtRegisterInfo.getEeExerciseDateType();
        stringErr.append(CheckDataUtils.checkAssetEmpty(eeExerciseDateType,"行权方式","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","0"));

        value = assetDebtRegisterInfo.getEeFixedExerciseDate();
        if(!"1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213".contains(assetType) && StringUtils.isNotBlank(value)){
            stringErr.append("当资产/负债类别为本行存款时，固定行权日要素必须为空。<br/>");
        }else if(StringUtils.isNotBlank(eeExerciseDateType) ){
            if("02".contains(eeExerciseDateType) && StringUtils.isBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、行权方式为欧式期权时，固定行权日要素不可为空。<br/>");
            }else if(!"02".contains(eeExerciseDateType) && StringUtils.isNotBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、行权方式为美式期权时，固定行权日要素必须为空。<br/>");
            }
        }else if(StringUtils.isBlank(eeExerciseDateType) && StringUtils.isNotBlank(value)){
            stringErr.append("当行权方式为空时，固定行权日必须为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkDate(value,"固定行权日"));

        value = assetDebtRegisterInfo.getEeFirstExerciseDate();
        if(!"1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213".contains(assetType) && StringUtils.isNotBlank(value)){
            stringErr.append("当资产/负债类别为本行存款时，首次行权日期要素必须为空。<br/>");
        }else if(StringUtils.isNotBlank(eeExerciseDateType) ){
            if("03".contains(eeExerciseDateType) && StringUtils.isBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、行权方式为百慕大期权时，首次行权日期要素不可为空。<br/>");
            }else if(!"03".contains(eeExerciseDateType) && StringUtils.isNotBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、行权方式为美式期权时，首次行权日期要素必须为空。<br/>");
            }
        }else if(StringUtils.isBlank(eeExerciseDateType) && StringUtils.isNotBlank(value)){
            stringErr.append("当行权方式为空时，首次行权日期必须为空。<br/>");
        }


        value = assetDebtRegisterInfo.getEeExercisePeriod();
        if(!"1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213".contains(assetType) && StringUtils.isNotBlank(value)){
            stringErr.append("当资产/负债类别为本行存款时，行权周期要素必须为空。<br/>");
        }else if(StringUtils.isNotBlank(eeExerciseDateType) ){
            if("03".contains(eeExerciseDateType) && StringUtils.isBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、行权方式为百慕大期权时，行权周期要素不可为空。<br/>");
            }else if(!"03".contains(eeExerciseDateType) && StringUtils.isNotBlank(value)){
                stringErr.append("当资产/负债类别为信托贷款、行权方式为美式期权时，行权周期要素必须为空。<br/>");
            }
        }else if(StringUtils.isBlank(eeExerciseDateType) && StringUtils.isNotBlank(value)){
            stringErr.append("当行权方式为空时，行权周期必须为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkMoney(value, "行权周期", "^(\\d{1,5}", "n..5", "0", "1"));


        value = assetDebtRegisterInfo.getEeExercisePrice();
        if(!"1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213".contains(assetType) && StringUtils.isNotBlank(value)){
            stringErr.append("当资产/负债类别为本行存款时，行权价格要素必须为空。<br/>");
        }else if(StringUtils.isNotBlank(eeOptionType)&&"01,04".contains(eeOptionType) && StringUtils.isBlank(value)){
            stringErr.append("当资产/负债类别为信托贷款、含权类型为发行人赎回选择权时，行权价格要素不可为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkMoney(value, "行权价格", "^(\\d{1,13}(\\.\\d{1,4})?)", "n（17,4）", "0", "1"));

        String eePerpetualType = assetDebtRegisterInfo.getEePerpetualType();
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEePerpetualType(),"永续条款类型","1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213",assetType,"信托贷款","1"));
        String eeDeferreInterestType = assetDebtRegisterInfo.getEeDeferreInterestType();
        stringErr.append(CheckDataUtils.checkAssetEmpty(eeDeferreInterestType,"利息递延条款类型","1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213",assetType,"信托贷款","1"));

        value = assetDebtRegisterInfo.getEeInterestDeferred();
        if(!"1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213".contains(assetType) && StringUtils.isNotBlank(value)){
            stringErr.append("当资产/负债类别为本行存款时，递延利息是否计息要素必须为空。<br/>");
        }else if(StringUtils.isNotBlank(eeDeferreInterestType)&&"02,03".contains(eeDeferreInterestType) && StringUtils.isBlank(value)){
            stringErr.append("当资产/负债类别为信托贷款、利息递延条款类型为有利息递延条款时，递延利息是否计息要素不可为空。<br/>");
        }else if(StringUtils.isNotBlank(eeDeferreInterestType)&&!"02,03".contains(eeDeferreInterestType) && StringUtils.isNotBlank(value)){
            stringErr.append("当资产/负债类别为信托贷款、利息递延条款类型为无利息递延条款时，递延利息是否计息要素必须为空。<br/>");
        }

        value = assetDebtRegisterInfo.getEeFirstRepriceDate();
        if(!"1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213".contains(assetType) && StringUtils.isNotBlank(value)){
            stringErr.append("当资产/负债类别为本行存款时，首次重定价日期要素必须为空。<br/>");
        }else if(StringUtils.isNotBlank(eePerpetualType)&&"01,02".contains(eePerpetualType) && StringUtils.isBlank(value)){
            stringErr.append("当资产/负债类别为信托贷款、永续条款类型为可续期条款时，首次重定价日期要素不可为空。<br/>");
        }else if(StringUtils.isNotBlank(eePerpetualType)&&!"01,02".contains(eePerpetualType) && StringUtils.isNotBlank(value)){
            stringErr.append("当资产/负债类别为信托贷款、永续条款类型为无时，首次重定价日期要素必须为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkDate(value,"首次重定价日期"));

        value = assetDebtRegisterInfo.getEeRepricePeriod();
        if(!"1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213".contains(assetType) && StringUtils.isNotBlank(value)){
            stringErr.append("当资产/负债类别为本行存款时，重定价周期要素必须为空。<br/>");
        }else if(StringUtils.isNotBlank(eePerpetualType)&&"01,02".contains(eePerpetualType) && StringUtils.isBlank(value)){
            stringErr.append("当资产/负债类别为信托贷款、永续条款类型为可续期条款时，重定价周期要素不可为空。<br/>");
        }else if(StringUtils.isNotBlank(eePerpetualType)&&!"01,02".contains(eePerpetualType) && StringUtils.isNotBlank(value)){
            stringErr.append("当资产/负债类别为信托贷款、永续条款类型为无时，重定价周期要素必须为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkMoney(value, "重定价周期", "^(\\d{1,5}", "n..5", "0", "1"));


        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEePartialRedemption(),"部分赎回标识","1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(ObjectUtils.allNotNull(assetDebtRegisterInfo.getEePartialRedemptionRate())?assetDebtRegisterInfo.getEePartialRedemptionRate().toString():"","部分赎回比例","1202,1203,1204,1205,1206,1207,1208,1209,1211,1212,1213",assetType,"信托贷款","0"));
        stringErr.append(CheckDataUtils.checkMoney(ObjectUtils.allNotNull(assetDebtRegisterInfo.getEePartialRedemptionRate())?assetDebtRegisterInfo.getEePartialRedemptionRate().toString():"", "部分赎回比例", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeOptionRight(),"选择权","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getEeOptionRight(),"选择权",60,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeDetailsExerciseTerm(),"行权条件说明","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getEeDetailsExerciseTerm(),"行权条件说明",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getEeRegionDebtor(),"融资人所属地区","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(ObjectUtils.allNotNull(assetDebtRegisterInfo.getEeTotalFeeRate())?assetDebtRegisterInfo.getEeTotalFeeRate().toString():"","融资总费率%","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        stringErr.append(CheckDataUtils.checkMoney(ObjectUtils.allNotNull(assetDebtRegisterInfo.getEeTotalFeeRate())?assetDebtRegisterInfo.getEeTotalFeeRate().toString():"", "融资总费率%", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));

        value = assetDebtRegisterInfo.getEeOrganizationCode();
        stringErr.append(CheckDataUtils.checkAssetEmpty(value,"融资人组织机构（社会信用）代码（非标准化债权类资产）","1202,1203,1204,1205,1206,1207,1208,1209,2202,1211,1212,1213",assetType,"信托贷款","1"));
        if(StringUtils.isNotBlank(value)){
            Pattern p9=Pattern.compile("^[A-Z0-9]{9}");
            Pattern p18=Pattern.compile("^[A-Z0-9]{18}");
            if(!(p9.matcher(value).matches() || p18.matcher(value).matches())){
                stringErr.append("融资人组织机构（社会信用）代码（非标准化债权类资产）要素格式不对正确的格式：9或18位大写字母或数字。<br/>");
            }
        }

        //--非标准化债权类投资(票据/信用证资产)--
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getFfOwnership(),"是否为收/受益权","1201,1210",assetType,"票据类","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getFfBuyback(),"是否属于买入返售（非标准化债权类投资）","1201,1210",assetType,"票据类","1"));
        value = assetDebtRegisterInfo.getFfType();
        stringErr.append(CheckDataUtils.checkAssetEmpty(value,"类型","1201,1210",assetType,"票据类","1"));
        if(assetType.contains("1210") && !value.contains("05")){
            stringErr.append("当资产/负债类别为信用证时，类型（非标准化债权类投资）只能填写信用证。<br/>");
        }else if (assetType.contains("1210") && value.contains("05")){
            stringErr.append("当资产/负债类别为票据类时，类型（非标准化债权类投资）不能填写信用证。<br/>");
        }
        String getFfStandarBill = assetDebtRegisterInfo.getFfStandarBill();
        stringErr.append(CheckDataUtils.checkAssetEmpty(getFfStandarBill,"是否为标准化票据","1201",assetType,"票据类","1"));

        value = assetDebtRegisterInfo.getFfNoteCode();
        if(StringUtils.isNotBlank(assetType)&&!assetType.contains("1201") && StringUtils.isNotBlank(value)){
            stringErr.append("当资产/负债类别为信用证时，票据代码要素必须为空。<br/>");
        }else if(StringUtils.isNotBlank(getFfStandarBill)&&getFfStandarBill.contains("01") && StringUtils.isBlank(value)){
            stringErr.append("当是否为标准化票据为是时，票据代码要素不可为空。<br/>");
        }else if(StringUtils.isNotBlank(getFfStandarBill)&&getFfStandarBill.contains("02") && StringUtils.isNotBlank(value)){
            stringErr.append("当资产/负债类别为票据类，是否为标准化票据为否时，票据代码要素必须为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkStringLength(value,"票据代码",20,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getFfQuantity(),"数量（非标准化债权类投资）","1201,1210",assetType,"票据类","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getFfQuantity(), "数量（非标准化债权类投资）", "^(\\d{1,6}", "n..6", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getFfAggregateAmt(),"合计金额","1201,1210",assetType,"票据类","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getFfAggregateAmt(), "合计金额", "^(\\d{1,13}(\\.\\d{1,2})?)","n（15,2）", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getFfWeightRemainDay(),"加权剩余期限（天）","1201,1210",assetType,"票据类","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getFfWeightRemainDay(), "加权剩余期限（天）", "^(\\d{1,5}(\\.\\d{1,2})?)", "n（7,2）", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getFfMaxRemainDay(),"最长剩余期限","1201,1210",assetType,"票据类","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getFfMaxRemainDay(), "最长剩余期限", "^(\\d{1,5}", "n..5", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getFfMinRemainFay(),"最短剩余期限","1201,1210",assetType,"票据类","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getFfMinRemainFay(), "最短剩余期限", "^(\\d{1,5}", "n..5","0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getFfMaturityDate(),"到期日（非标准化债权类投资）","1201,1210",assetType,"票据类","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getFfMaturityDate(),"到期日（非标准化债权类投资）"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getFfValueDate(),"起息日（非标准化债权类投资）","1201,1210",assetType,"票据类","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getFfValueDate(),"起息日（非标准化债权类投资）"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getFfDiscountRate(),"贴现利率（%）","1201,1210",assetType,"票据类","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getFfDiscountRate(), "贴现利率（%）", "^(\\d{1,3}(\\.\\d{1,5})?)","n（8,5）", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getFfIndustry(),"行业（非标准化债权类投资）","1201,1210",assetType,"票据类","1"));

        return stringErr.toString();
    }

    /**
     * QDII
     * @param whiteregex
     * @param whitereForCode
     * @param assetDebtRegisterInfo
     * @param assetType
     * @return
     */
    public static String checkAsset_2(String whiteregex,String whitereForCode, AssetDebtRegisterInfo assetDebtRegisterInfo,String assetType) throws Exception {
        StringBuffer stringErr = new StringBuffer();
        String value = assetDebtRegisterInfo.getGgStockCode();
        if("1305,1302".contains(assetType) && StringUtils.isBlank(value) ){
            stringErr.append("当资产/负债类别为股票（二级市场）时，股票代码要素不可为空。<br/>");
        }else if(!("1305,1302,1301").contains(assetType) && StringUtils.isNotBlank(value) ){
            stringErr.append("当资产/负债类别为本行存款时，股票代码要素必须为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkStringLength(value,"股票代码",20,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getGgName(),"股票/企业名称","1305,1302,1301",assetType,"股权","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getGgName(),"股票/企业名称",200,"0"));

        value = assetDebtRegisterInfo.getGgStockType();
        if("1305,1302".contains(assetType) && StringUtils.isBlank(value) ){
            stringErr.append("当资产/负债类别为股票（二级市场）时，股票类型要素不可为空。<br/>");
        }else if (!("1305,1302,1301").contains(assetType) && StringUtils.isNotBlank(value) ){
            stringErr.append("当资产/负债类别为本行存款时，股票类型要素必须为空。<br/>");
        }

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getGgIndustry(),"行业（权益类资产）","1305,1302,1301",assetType,"股权","1"));

        value = assetDebtRegisterInfo.getGgInvestStage();
        if(("1301").contains(assetType) && StringUtils.isBlank(value) ){
            stringErr.append("当资产/负债类别为股权时，投资阶段（权益类资产）要素不可为空。<br/>");
        }else if (!("1305,1302,1301").contains(assetType) && StringUtils.isNotBlank(value) ){
            stringErr.append("当资产/负债类别为本行存款时，投资阶段（权益类资产）要素必须为空。<br/>");
        }

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getGgEquityOutDate(),"股权退出安排","1301",assetType,"股权","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getGgEquityOutDate(),"股权退出安排"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getGgEnterTypeScale(),"企业类型（按规模划分）","1305,1302,1301",assetType,"股权","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getGgEnterTypeTech(),"企业类型（按技术领域划分）","1305,1302,1301",assetType,"股权","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getGgEnterTypeEconomic(),"企业类型（按经济类型划分）","1305,1302,1301",assetType,"股权","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getGgPledgedFinace(),"是否质押融资","1305,1302,1301",assetType,"股权","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getGgDebtEquitySwap(),"是否为债转股（权益类资产）","1305,1302,1301",assetType,"股权","1"));

        //--金融衍生品---
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getHhName(),"名称（金融衍生品）","1401,1402,1403,1404,1405,1406,1499",assetType,"远期","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getHhName(),"名称（金融衍生品）",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getHhNominalPrincipal(),"名义本金","1401,1402,1403,1404,1405,1406,1499",assetType,"远期","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getHhNominalPrincipal(), "名义本金", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getHhUnderAssetType(),"标的类别","1401,1402,1403,1404,1405,1406,1499",assetType,"远期","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getHhHoldObjective(),"持有目的","1401,1402,1403,1404,1405,1406,1499",assetType,"远期","1"));

        //QDII(债券类资产)
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getIiCountyRegion(),"所属国家或地区（QDII债券类资产）","1501",assetType,"QDII债券","1"));
        if("1501".contains(assetType) && assetDebtRegisterInfo.getIiCountyRegion().contains("CHN") ){
            stringErr.append("当资产/负债类别为QDII债券时，所属国家或地区（QDII债券类资产）不能填写CHN 中国。<br/>");
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getIiBondName(),"债券名称","1501",assetType,"QDII债券","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getIiBondName(),"债券名称",200,"0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getIiBondIdentCode(),"债券代码","1501",assetType,"QDII债券","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getIiBondIdentCode(),"债券代码",15,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getIiIssuer(),"发行机构（QDII债券类资产）","1501",assetType,"QDII债券","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getIiIssuer(),"发行机构（QDII债券类资产）",200,"0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getIiIndustryIssuer(),"发行机构所属行业（QDII债券类资产）","1501",assetType,"QDII债券","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getIiValueDate(),"起息日（QDII债券类资产）","1501",assetType,"QDII债券","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getIiValueDate(),"起息日（QDII债券类资产）"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getIiMaturityDate(),"到期日（QDII债券类资产）","1501",assetType,"QDII债券","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getIiMaturityDate(),"到期日（QDII债券类资产）"));
        if(StringUtils.isNotBlank(assetDebtRegisterInfo.getIiValueDate()) && StringUtils.isNotBlank(assetDebtRegisterInfo.getIiMaturityDate()) &&
                assetDebtRegisterInfo.getIiMaturityDate().compareTo(assetDebtRegisterInfo.getIiValueDate()) < 0){
            stringErr.append("当资产/负债类别为QDII债券时，到期日（QDII债券类资产）必须大于等于起息日（QDII债券类资产）。<br/>");
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getIiTermMaturity(),"期限（月）","1501",assetType,"QDII债券","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getIiTermMaturity(), "期限（月）", "^(\\d{1,5})", "n（5）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getIiIssuerRateBond(),"发行机构主体信用评级","1501",assetType,"QDII债券","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getIiIssuerRateBond(),"发行机构主体信用评级",10,"0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getIiBondRate(),"债券信用评级","1501",assetType,"QDII债券","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getIiBondRate(),"债券信用评级",10,"0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getIiCoupRate(),"票面利率%（QDII债券类资产）","1501",assetType,"QDII债券","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getIiCoupRate(), "票面利率%（QDII债券类资产）", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getIiInterestPayQuency(),"付息频率（个月/次）（QDII债券类资产）","1501",assetType,"QDII债券","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getIiInterestPayQuency(), "付息频率（个月/次）（QDII债券类资产）", "^(\\d{1,2})", "n（2）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getIiDetailsAssureStatus(),"担保情况说明（QDII债券类资产）","1501",assetType,"QDII债券","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getIiDetailsAssureStatus(),"担保情况说明（QDII债券类资产）",200,"0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getIiDetailsSpecialTerms(),"是否含权等特殊条款情况说明","1501",assetType,"QDII债券","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getIiDetailsSpecialTerms(),"是否含权等特殊条款情况说明",200,"0"));

        //--QDII(拆出/逆回购)--
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getJjCountry(),"所属国家或地区（QDII拆出/逆回购）","1504,1506",assetType,"QDII拆出/逆回购","1"));
        if("1504,1506".contains(assetType) && assetDebtRegisterInfo.getJjCountry().contains("CHN") ){
            stringErr.append("当资产/负债类别为QDII拆出/逆回购时，所属国家或地区（QDII拆出/逆回购）不能填写CHN 中国。<br/>");
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getJjValueDate(),"起息日（QDII拆出/逆回购）","1504,1506",assetType,"QDII拆出/逆回购","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getJjValueDate(),"起息日（QDII拆出/逆回购）"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getJjMaturityDate(),"到期日（QDII拆出/逆回购）","1504,1506",assetType,"QDII拆出/逆回购","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getJjMaturityDate(),"到期日（QDII拆出/逆回购）"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getJjCounterparty(),"对手方（QDII拆出/逆回购）","1504,1506",assetType,"QDII拆出/逆回购","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getJjCounterparty(),"对手方（QDII拆出/逆回购）",200,"0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getJjInterestRate(),"年利率%（QDII拆出/逆回购）","1504,1506",assetType,"QDII拆出/逆回购","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getJjInterestRate(), "年利率%（QDII拆出/逆回购）", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getJjInterestBasis(),"计息基础（QDII拆出/逆回购）","1504,1506",assetType,"QDII拆出/逆回购","1"));

        //QDII基金股票
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getKkCountry(),"所属国家或地区（QDII基金股票）","1502,1505,1507,1508,1509",assetType,"QDII基金股票","1"));
        if("1502,1505,1507,1508,1509".contains(assetType) && assetDebtRegisterInfo.getKkCountry().contains("CHN") ){
            stringErr.append("当资产/负债类别为QDII基金股票时，所属国家或地区（QDII基金股票）不能填写CHN 中国。<br/>");
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getKkIdentCode(),"股票/基金代码","1502,1505,1507,1508,1509]",assetType,"QDII基金股票","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getKkIdentCode(),"股票/基金代码",20,"0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getKkName(),"股票/基金名称","1502,1505,1507,1508,1509]",assetType,"QDII基金股票","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getKkName(),"股票/基金名称",200,"0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getKkIssuer(),"发行机构（QDII基金股票）","1502,1505,1507,1508,1509]",assetType,"QDII基金股票","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getKkIssuer(),"发行机构（QDII基金股票）",200,"0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getKkIndustry(),"行业（QDII基金股票）","1502,1505,1507,1508,1509]",assetType,"QDII基金股票","1"));

        //--QDII结构性票据--
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getLlCountry(),"所属国家或地区（QDII结构性票据）","1503",assetType,"QDII结构性票据","1"));
        if("1503".contains(assetType) && assetDebtRegisterInfo.getLlCountry().contains("CHN") ){
            stringErr.append("当资产/负债类别为QDII债券时，所属国家或地区（QDII结构性票据）不能填写CHN 中国。<br/>");
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getLlContractName(),"合约名称","1503",assetType,"QDII结构性票据","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getLlContractName(),"合约名称",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getLlValueDate(),"起息日（QDII结构性票据）","1503",assetType,"QDII结构性票据","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getLlValueDate(),"起息日（QDII结构性票据）"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getLlMaturityDate(),"到期日（QDII结构性票据）","1503",assetType,"QDII结构性票据","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getLlMaturityDate(),"到期日（QDII结构性票据）"));
        if(StringUtils.isNotBlank(assetDebtRegisterInfo.getLlValueDate()) && StringUtils.isNotBlank(assetDebtRegisterInfo.getLlMaturityDate()) &&
                assetDebtRegisterInfo.getLlMaturityDate().compareTo(assetDebtRegisterInfo.getLlValueDate()) < 0){
            stringErr.append("当资产/负债类别为QDII结构性票据时，到期日（QDII结构性票据）必须大于等于起息日（QDII结构性票据）。<br/>");
        }

        value = assetDebtRegisterInfo.getLlCouponRate();
        if (!"1503".contains(assetType) && StringUtils.isNotBlank(value) ){
            stringErr.append("当资产/负债类别为本行存款时，票面利率%（QDII结构性票据）要素必须为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkMoney(value, "票面利率%（QDII结构性票据）", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));

        value = assetDebtRegisterInfo.getLlInterestFrequency();
        if (!"1503".contains(assetType) && StringUtils.isNotBlank(value) ){
            stringErr.append("当资产/负债类别为本行存款时，付息频率（个月/次）（QDII结构性票据）要素必须为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkMoney(value, "付息频率（个月/次）（QDII结构性票据）", "^(\\d{1,2})", "n..2", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getLlPercentFix(),"固定收益部分所占比例","1503",assetType,"QDII结构性票据","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getLlPercentFix(), "固定收益部分所占比例", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getLlPercentDerivate(),"衍生金融工具所占比例","1503",assetType,"QDII结构性票据","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getLlPercentDerivate(), "衍生金融工具所占比例", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getLlDerivateInvetType(),"衍生金融工具具体投资方式","1503",assetType,"QDII结构性票据","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getLlDerivateInvetType(),"衍生金融工具具体投资方式",300,"0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getLlUnderAsset(),"衍生金融工具挂钩标的资产","1503",assetType,"QDII结构性票据","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getLlUnderAsset(),"衍生金融工具挂钩标的资产",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getLlDetailsProceeds(),"利息结算方式","1503",assetType,"QDII结构性票据","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getLlDetailsProceeds(),"利息结算方式",200,"0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getLlDetailsOption(),"含权情况说明","1503",assetType,"QDII结构性票据","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getLlDetailsOption(),"含权情况说明",200,"0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getLlMaxNoteReturn(),"结构性票据最高收益率","1503",assetType,"QDII结构性票据","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getLlMaxNoteReturn(), "结构性票据最高收益率", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getLlMinNoteReturn(),"结构性票据最低收益率","1503",assetType,"QDII结构性票据","0"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getLlMinNoteReturn(), "结构性票据最低收益率", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getLlStrikeUnderAsset(),"挂钩标的资产基准价格","1503",assetType,"QDII结构性票据","0"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getLlStrikeUnderAsset(), "挂钩标的资产基准价格", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getLlUnderRgPrice(),"挂钩标的资产登记日价格","1503",assetType,"QDII结构性票据","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getLlUnderRgPrice(), "挂钩标的资产登记日价格", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "0", "1"));

        //--资产管理产品--
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getMmManagePlanName(),"资管计划名称","1701,1702,1703,1704,1705,1706",assetType,"信托产品","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getMmManagePlanName(),"资管计划名称",200,"0"));
        String getMmManageProduct = assetDebtRegisterInfo.getMmManageProduct();
        stringErr.append(CheckDataUtils.checkAssetEmpty(getMmManageProduct,"是否为银行理财产品","1705",assetType,"其他资产管理产品","1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getMmProductCode(),"理财产品登记编码","1701,1702,1703,1704,1706",assetType,"信托产品","0"));
        value = assetDebtRegisterInfo.getMmProductCode();
        if(!"1705".contains(assetType) && StringUtils.isNotBlank(value) ){
            stringErr.append("当资产/负债类别为本行存款时，产品登记编码要素必须为空。<br/>");
        }else if ( StringUtils.isNotBlank(getMmManageProduct)&&!getMmManageProduct.contains("01") && StringUtils.isNotBlank(value) ){
            stringErr.append("当是否为银行理财产品为否时，产品登记编码要素必须为空。<br/>");
        }else if (StringUtils.isNotBlank(getMmManageProduct)&&getMmManageProduct.contains("01") && StringUtils.isBlank(value) ){
            stringErr.append("当是否为银行理财产品为是时，产品登记编码要素不可为空。<br/>");
        }else if(StringUtils.isNotBlank(value)){
            Pattern p14=Pattern.compile("^[A-Z0-9]{14}");
            Pattern p15=Pattern.compile("^[A-Z0-9]{15}");
            if(!(p14.matcher(value).matches() || p15.matcher(value).matches()) ){
                stringErr.append("理财产品登记编码要素格式不对。正确的格式：14或15位英文或数字。<br/>");
            }
        }

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getMmIssuedAssetCompany(),"是否由金融资产投资公司发行（资产管理计划）","1705",assetType,"其他资产管理产品","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getMmPlanIssuerCode(),"资管计划发起人机构编码","1701,1702,1703,1704,1705,1706",assetType,"信托产品","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getMmAssetPlanRgCode(),"资管计划登记编码","1701,1702,1703,1704,1705,1706",assetType,"信托产品","0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getMmManager(),"管理人","1701,1702,1703,1704,1705,1706",assetType,"信托产品","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getMmManager(),"管理人",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getMmCustodian(),"托管人","1701,1702,1703,1704,1705,1706",assetType,"信托产品","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getMmCustodian(),"托管人",200,"0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getMmAmt(),"金额（资产管理计划）","1701,1702,1703,1704,1705,1706",assetType,"信托产品","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getMmAmt(), "金额（资产管理计划）", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getMmActualDirect(),"资金实际投向","1701,1702,1703,1704,1705,1706",assetType,"信托产品","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getMmDetailsInvest(),"资金运用方式","1701,1702,1703,1704,1705,1706",assetType,"信托产品","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getMmDetailsInvest(),"资金运用方式",300,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getMmIndustryInvest(),"资金运用行业","1701,1702,1703,1704,1705,1706",assetType,"信托产品","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getMmPlanStartDate(),"资管计划成立日期","1701,1702,1703,1704,1705,1706",assetType,"信托产品","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getMmPlanStartDate(),"资管计划成立日期"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getMmPlanMaturityDate(),"资管计划终止日期","1701,1702,1703,1704,1705,1706",assetType,"信托产品","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getMmPlanMaturityDate(),"资管计划终止日期"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getMmPlanType(),"资管计划属性","1701,1702,1703,1704,1705,1706",assetType,"信托产品","1"));
        String getMmExpectedReturn = assetDebtRegisterInfo.getMmExpectedReturn();
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getMmExpectedReturn(),"是否有预期收益率（资产管理计划）","1701,1702,1703,1704,1705,1706",assetType,"信托产品","1"));

        value = ObjectUtils.allNotNull(assetDebtRegisterInfo.getMmMaxExpectedReturn())?assetDebtRegisterInfo.getMmMaxExpectedReturn().toString():"";
        if(!"1701,1702,1703,1704,1705,1706".contains(assetType) && StringUtils.isNotBlank(value) ){
            stringErr.append("当资产/负债类别为本行存款时，预期最高收益率%要素必须为空。<br/>");
        }else if (StringUtils.isNotBlank(getMmExpectedReturn)&&getMmExpectedReturn.contains("02") && StringUtils.isNotBlank(value) ){
            stringErr.append("当资产/负债类别为信托产品、是否有预期收益率（资产管理计划）为否时，预期最高收益率%要素必须为空。<br/>");
        }else if (StringUtils.isNotBlank(getMmExpectedReturn)&&getMmExpectedReturn.contains("01") && StringUtils.isBlank(value) ){
            stringErr.append("当资产/负债类别为信托产品、是否有预期收益率（资产管理计划）为是时，预期最高收益率%要素不可为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkMoney(value, "预期最高收益率%", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));

        value = ObjectUtils.allNotNull(assetDebtRegisterInfo.getMmMinExpectedReturn())?assetDebtRegisterInfo.getMmMinExpectedReturn().toString():"";
        if(!"1701,1702,1703,1704,1705,1706".contains(assetType) && StringUtils.isNotBlank(value) ){
            stringErr.append("当资产/负债类别为本行存款时，预期最低收益率%要素必须为空。<br/>");
        }else if (StringUtils.isNotBlank(getMmExpectedReturn)&&getMmExpectedReturn.contains("02") && StringUtils.isNotBlank(value) ){
            stringErr.append("当资产/负债类别为信托产品、是否有预期最低收益率（资产管理计划）为否时，预期最低收益率%要素必须为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkMoney(value, "预期最低收益率%", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));
        if(StringUtils.isNotBlank(value) && StringUtils.isNotBlank(ObjectUtils.allNotNull(assetDebtRegisterInfo.getMmMaxExpectedReturn())?assetDebtRegisterInfo.getMmMaxExpectedReturn().toString():"") &&
                (ObjectUtils.allNotNull(assetDebtRegisterInfo.getMmMaxExpectedReturn())?assetDebtRegisterInfo.getMmMaxExpectedReturn().toString():"").compareTo(value) < 0){
            stringErr.append("预期最低收益率（资产管理计划）应小于或等于预期最高收益（资产管理计划）。<br/>");
        }

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getMmInvestStructure(),"购买结构","1701,1702,1703,1704,1705,1706",assetType,"信托产品","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getMmManagerType(),"管理方式","1701,1702,1703,1704,1705,1706",assetType,"信托产品","1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(ObjectUtils.allNotNull(assetDebtRegisterInfo.getMmManagerFeeRate())?assetDebtRegisterInfo.getMmManagerFeeRate().toString():"","管理费率%","1701,1702,1703,1704,1705,1706",assetType,"信托产品","1"));
        stringErr.append(CheckDataUtils.checkMoney(ObjectUtils.allNotNull(assetDebtRegisterInfo.getMmManagerFeeRate())?assetDebtRegisterInfo.getMmManagerFeeRate().toString():"", "管理费率%", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(ObjectUtils.allNotNull(assetDebtRegisterInfo.getMmCustodianFeeRate())?assetDebtRegisterInfo.getMmCustodianFeeRate().toString():"","托管费率%（资产管理计划）","1701,1702,1703,1704,1705,1706",assetType,"信托产品","1"));
        stringErr.append(CheckDataUtils.checkMoney(ObjectUtils.allNotNull(assetDebtRegisterInfo.getMmCustodianFeeRate())?assetDebtRegisterInfo.getMmCustodianFeeRate().toString():"", "托管费率%（资产管理计划）", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(ObjectUtils.allNotNull(assetDebtRegisterInfo.getMmTransCostRate())?assetDebtRegisterInfo.getMmTransCostRate().toString():"","交易相关合计费率%","1701,1702,1703,1704,1705,1706",assetType,"信托产品","1"));
        stringErr.append(CheckDataUtils.checkMoney(ObjectUtils.allNotNull(assetDebtRegisterInfo.getMmTransCostRate())?assetDebtRegisterInfo.getMmTransCostRate().toString():"", "交易相关合计费率%", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(ObjectUtils.allNotNull(assetDebtRegisterInfo.getMmInterFeeRate())?assetDebtRegisterInfo.getMmInterFeeRate().toString():"","中介服务机构合计费率","1701,1702,1703,1704,1705,1706",assetType,"信托产品","1"));
        stringErr.append(CheckDataUtils.checkMoney(ObjectUtils.allNotNull(assetDebtRegisterInfo.getMmInterFeeRate())?assetDebtRegisterInfo.getMmInterFeeRate().toString():"", "中介服务机构合计费率%", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(ObjectUtils.allNotNull(assetDebtRegisterInfo.getMmOtherExpenseRate())?assetDebtRegisterInfo.getMmOtherExpenseRate().toString():"","其他合计费率%","1701,1702,1703,1704,1705,1706",assetType,"信托产品","1"));
        stringErr.append(CheckDataUtils.checkMoney(ObjectUtils.allNotNull(assetDebtRegisterInfo.getMmOtherExpenseRate())?assetDebtRegisterInfo.getMmOtherExpenseRate().toString():"", "其他合计费率%", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));

        return stringErr.toString();
    }

    /**
     * 其他类型
     * @param whiteregex
     * @param whitereForCode
     * @param assetDebtRegisterInfo
     * @param assetType
     * @return
     */
    public static String checkAsset_3(String whiteregex,String whitereForCode, AssetDebtRegisterInfo assetDebtRegisterInfo,String assetType){
        StringBuffer stringErr = new StringBuffer();
        //--商品类资产--
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getNnCountry(),"所属国家或地区（商品类资产）","1601,1602,1699",assetType,"贵金属类","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getNnName(),"名称（商品类资产）","1601,1602,1699",assetType,"贵金属类","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getNnName(),"名称（商品类资产）",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getNnTermDays(),"期限（商品类资产）","1601,1602,1699",assetType,"贵金属类","0"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getNnTermDays(), "期限（商品类资产）", "^(\\d{1,5}", "n..5", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getNnAssetValue(),"资产价值（商品类资产）","1601,1602,1699",assetType,"贵金属类","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getNnAssetValue(), "资产价值（商品类资产）", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getNnAssetReturn(),"资产收益率%（商品类资产）","1601,1602,1699",assetType,"贵金属类","0"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getNnAssetReturn(), "资产收益率%（商品类资产）", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));

        //--另类资产--
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getOoCountry(),"所属国家或地区（另类资产）","2301,2302,2303,2304,2305,1603",assetType,"字画类艺术品","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getOoName(),"名称（另类资产）","2301,2302,2303,2304,2305,1603",assetType,"字画类艺术品","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getOoName(),"名称（另类资产）",200,"0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getOoValueDate(),"起息日（另类资产）","2301,2302,2303,2304,2305,1603",assetType,"字画类艺术品","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getOoValueDate(),"起息日（另类资产）"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getOoMaturityDate(),"到期日（另类资产）","2301,2302,2303,2304,2305,1603",assetType,"字画类艺术品","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getOoMaturityDate(),"到期日（另类资产）"));
        if(StringUtils.isNotBlank(assetDebtRegisterInfo.getOoValueDate()) && StringUtils.isNotBlank(assetDebtRegisterInfo.getOoMaturityDate()) &&
                String.valueOf(assetDebtRegisterInfo.getOoMaturityDate()).compareTo(assetDebtRegisterInfo.getOoValueDate()) < 0){
            stringErr.append("到期日（另类资产）必须大于等于起息日（另类资产）。<br/>");
        }

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getOoAssetValue(),"资产价值（另类资产）","2301,2302,2303,2304,2305,1603",assetType,"字画类艺术品","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getOoAssetValue(), "资产价值（另类资产）", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getOoAssetReturn(),"资产收益率%（另类资产）","2301,2302,2303,2304,2305,1603",assetType,"字画类艺术品","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getOoAssetReturn(), "资产收益率%（另类资产）", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));

        //--公募基金/私募基金--
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getPpFundCode(),"基金代码","1106,1303,2401,2402,2403,2499,2601,2602,2604,2605,2606,2607,2610,2611,2612,2613,2614,2616,2617,2618,2619,2620,2699",assetType,"债券基金","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getPpFundCode(),"基金代码",20,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getPpFundName(),"基金名称","1106,1303,2401,2402,2403,2499,2601,2602,2604,2605,2606,2607,2610,2611,2612,2613,2614,2616,2617,2618,2619,2620,2699",assetType,"债券基金","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getPpFundName(),"基金名称",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getPpIssuedAssetCompany(),"是否由金融资产投资公司发行（公募基金/私募基金）","2601,2602,2604,2605,2606,2607,2610,2611,2612,2613,2614,2616,2617,2618,2619,2620,2699",assetType,"权益类基金","1"));
//        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getPpIssuedAssetCompany(),"是否由金融资产投资公司发行（公募基金/私募基金）","1106,1303,2401,2402,2403,2499,2601,2602,2604,2605,2606,2607,2610,2611,2612,2613,2614,2616,2617,2618,2619,2620,2699",assetType,"债券基金","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getPpIndustry(),"行业（公募基金/私募基金）","1106,1303,2401,2402,2403,2499,2601,2602,2604,2605,2606,2607,2610,2611,2612,2613,2614,2616,2617,2618,2619,2620,2699",assetType,"债券基金","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getPpRegistAgency(),"登记备案机构","1106,1303,2401,2402,2403,2499,2601,2602,2604,2605,2606,2607,2610,2611,2612,2613,2614,2616,2617,2618,2619,2620,2699",assetType,"债券基金","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getPpFixedIncome(),"是否为固定收益类","1106,1303,2401,2402,2403,2499",assetType,"债券基金","1"));

        String getPpGovernInvestFund = assetDebtRegisterInfo.getPpGovernInvestFund();
        if("2607,2610,2611,2612,2613,2617".contains(assetType) && StringUtils.isBlank(getPpGovernInvestFund) ){
            stringErr.append("当资产/负债类别为上市公司定增基金时，是否属于政府投资基金要素不可为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(getPpGovernInvestFund,"是否属于政府投资基金","1106,1303,2401,2402,2403,2499,2601,2602,2604,2605,2606,2607,2610,2611,2612,2613,2614,2616,2617,2618,2619,2620,2699",assetType,"债券基金","0"));

        String getPpDirectGovernFund = assetDebtRegisterInfo.getPpDirectGovernFund();
        if(StringUtils.isNotBlank(getPpGovernInvestFund)&&getPpGovernInvestFund.contains("01") && StringUtils.isBlank(getPpDirectGovernFund)){
            stringErr.append("当资产/负债类别为上市公司定增基金、是否属于政府投资基金为是时，政府投资基金投向要素不可为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(getPpDirectGovernFund,"政府投资基金投向","1106,1303,2401,2402,2403,2499,2601,2602,2604,2605,2606,2607,2610,2611,2612,2613,2614,2616,2617,2618,2619,2620,2699",assetType,"债券基金","0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getPpTaName(),"基金公司名称","1106,1303,2401,2402,2403,2499,2601,2602,2604,2605,2606,2607,2610,2611,2612,2613,2614,2616,2617,2618,2619,2620,2699",assetType,"债券基金","0"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getPpTaName(),"基金公司名称",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getPpManagerFundName(),"基金管理机构名称","1106,1303,2401,2402,2403,2499,2601,2602,2604,2605,2606,2607,2610,2611,2612,2613,2614,2616,2617,2618,2619,2620,2699",assetType,"债券基金","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getPpManagerFundName(),"基金管理机构名称",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getPpCustodianFundName(),"基金托管机构名称","1106,1303,2401,2402,2403,2499,2601,2602,2604,2605,2606,2607,2610,2611,2612,2613,2614,2616,2617,2618,2619,2620,2699",assetType,"债券基金","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getPpCustodianFundName(),"基金托管机构名称",200,"0"));

        String getPpInvestStage = assetDebtRegisterInfo.getPpInvestStage();
        if("2610,2611,2612,2613".contains(assetType) && StringUtils.isBlank(getPpInvestStage) ){
            stringErr.append("当资产/负债类别为并购基金时，投资阶段（公募基金/私募基金）要素不可为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(getPpInvestStage,"投资阶段（公募基金/私募基金）","1106,1303,2401,2402,2403,2499,2601,2602,2604,2605,2606,2607,2610,2611,2612,2613,2614,2616,2617,2618,2619,2620,2699",assetType,"债券基金","0"));

        String getPpEnterTypeScale = assetDebtRegisterInfo.getPpEnterTypeScale();
        if("2610,2611,2612,2613".contains(assetType) && StringUtils.isBlank(getPpEnterTypeScale) ){
            stringErr.append("当资产/负债类别为并购基金时，投资企业类型（按规模划分）要素不可为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(getPpEnterTypeScale,"投资企业类型（按规模划分）","1106,1303,2401,2402,2403,2499,2601,2602,2604,2605,2606,2607,2610,2611,2612,2613,2614,2616,2617,2618,2619,2620,2699",assetType,"债券基金","0"));

        String getPpEnterTypeTech = assetDebtRegisterInfo.getPpEnterTypeTech();
        if("2610,2611,2612,2613".contains(assetType) && StringUtils.isBlank(getPpEnterTypeTech) ){
            stringErr.append("当资产/负债类别为并购基金时，投资企业类型（按技术领域划分）要素不可为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(getPpEnterTypeTech,"投资企业类型（按技术领域划分）","1106,1303,2401,2402,2403,2499,2601,2602,2604,2605,2606,2607,2610,2611,2612,2613,2614,2616,2617,2618,2619,2620,2699",assetType,"债券基金","0"));

        String getPpEnterTypeEconomic = assetDebtRegisterInfo.getPpEnterTypeEconomic();
        if("2610,2611,2612,2613".contains(assetType) && StringUtils.isBlank(getPpEnterTypeEconomic) ){
            stringErr.append("当资产/负债类别为并购基金时，投资企业类型（按经济类型划分）要素不可为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(getPpEnterTypeEconomic,"投资企业类型（按经济类型划分）","1106,1303,2401,2402,2403,2499,2601,2602,2604,2605,2606,2607,2610,2611,2612,2613,2614,2616,2617,2618,2619,2620,2699",assetType,"债券基金","0"));

        String getPpInvestAssets = assetDebtRegisterInfo.getPpInvestAssets();
        stringErr.append(CheckDataUtils.checkAssetEmpty(getPpInvestAssets,"基金投资资产","1106,1303,2401,2402,2403,2499,2601,2602,2604,2605,2606,2607,2610,2611,2612,2613,2614,2616,2617,2618,2619,2620,2699",assetType,"债券基金","1"));
        stringErr.append(CheckDataUtils.checkStringLength(getPpInvestAssets,"基金投资资产",400,"0"));

        if(StringUtils.isNotBlank(getPpInvestAssets)){
            String regex = "^(100(\\.00)?%:[^%:;\\-\\d]+(;100(\\.00)?%:[^%:;\\-\\d]+)*|" +
                    "(100(\\.00)?%|\\d{1,3}(?:\\.\\d{1,2})?%)(-(100(\\.00)?%|\\d{1,3}(?:\\.\\d{1,2})?%))?:" +
                    "[^%:;\\-\\d]+(;(100(\\.00)?%|\\d{1,3}(?:\\.\\d{1,2})?%)(-(100(\\.00)?%|\\d{1,3}(?:\\.\\d{1,2})?%))?:" +
                    "[^%:;\\-\\d]+)*;?)$";
            Pattern p = Pattern.compile(regex);
            Matcher m=p.matcher(getPpInvestAssets);
            if(!m.matches()){
                stringErr.append("基金投资资产要素格式不对正确的格式：数字%：文字；数字%-数字%：文字（其中，百分号、冒号、分号、连字号均应为英文标点，文字和数字部分均不得含有英文百分号、冒号、分号、连字号）其中，数字格式为n..（5，2），且需大于等于0、小于等于100。<br/>");
            }
        }

        //委外投资协议
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getQqOutAgreementName(),"委外投资协议名称","2801,2802,2803,2804,2805,2806,2899",assetType,"协议委外-信托公司","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getQqOutAgreementName(),"委外投资协议名称",200,"0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getQqOutAgreementCode(),"委外投资协议编号","2801,2802,2803,2804,2805,2806,2899",assetType,"协议委外-信托公司","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getQqOutAgreementCode(),"委外投资协议编号",200,"0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getQqTrustee(),"受托人","2801,2802,2803,2804,2805,2806,2899",assetType,"协议委外-信托公司","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getQqTrustee(),"受托人",200,"0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getQqActualManager(),"实际管理人","2801,2802,2803,2804,2805,2806,2899",assetType,"协议委外-信托公司","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getQqActualManager(),"实际管理人",200,"0"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getQqCustodian(),"托管人","2801,2802,2803,2804,2805,2806,2899",assetType,"协议委外-信托公司","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getQqCustodian(),"托管人",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getQqOutAmt(),"委托投资金额","2801,2802,2803,2804,2805,2806,2899",assetType,"协议委外-信托公司","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getQqOutAmt(), "委托投资金额", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getQqActualDirection(),"资金实际投向（委外投资协议方式）","2801,2802,2803,2804,2805,2806,2899",assetType,"协议委外-信托公司","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getQqDetailsInvest(),"资金运用方式（委外投资协议方式）","2801,2802,2803,2804,2805,2806,2899",assetType,"协议委外-信托公司","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getQqIndustryInvest(),"资金运用行业（委外投资协议方式）","2801,2802,2803,2804,2805,2806,2899",assetType,"协议委外-信托公司","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getQqValueDate(),"投资运作起始日期","2801,2802,2803,2804,2805,2806,2899",assetType,"协议委外-信托公司","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getQqValueDate(),"投资运作起始日期"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getQqMaturityDate(),"投资运作终止日期","2801,2802,2803,2804,2805,2806,2899",assetType,"协议委外-信托公司","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getQqMaturityDate(),"投资运作终止日期"));
        if(StringUtils.isNotBlank(assetDebtRegisterInfo.getQqValueDate()) && StringUtils.isNotBlank(assetDebtRegisterInfo.getQqMaturityDate().toString()) &&
                assetDebtRegisterInfo.getQqMaturityDate().toString().compareTo(assetDebtRegisterInfo.getQqValueDate()) < 0){
            stringErr.append("投资运作终止日期必须大于等于投资运作起始日期。<br/>");
        }

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getQqOutType(),"委外投资属性","2801,2802,2803,2804,2805,2806,2899",assetType,"协议委外-信托公司","1"));

        String getQqExpectedReturn = assetDebtRegisterInfo.getQqExpectedReturn();
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getQqExpectedReturn(),"是否有预期收益率（委外投资协议方式）","2801,2802,2803,2804,2805,2806,2899",assetType,"协议委外-信托公司","1"));




        String value = StringUtils.isNotBlank(assetDebtRegisterInfo.getQqMaxExpectedReturn())?assetDebtRegisterInfo.getQqMaxExpectedReturn():"";

        if(!"2801,2802,2803,2804,2805,2806,2899".contains(assetType) && StringUtils.isNotBlank(value) ){
            stringErr.append("当资产/负债类别为本行存款时，预期最高收益率（委外投资协议方式）要素必须为空。<br/>");
        }else if (StringUtils.isNotBlank(getQqExpectedReturn)&&getQqExpectedReturn.contains("02") && StringUtils.isNotBlank(value) ){
            stringErr.append("当资产/负债类别为协议委外-信托公司、是否有预期收益率（委外投资协议方式）为否时，预期最高收益率（委外投资协议方式）要素必须为空。<br/>");
        }else if (StringUtils.isNotBlank(getQqExpectedReturn)&&getQqExpectedReturn.contains("01") && StringUtils.isBlank(value) ){
            stringErr.append("当资产/负债类别为协议委外-信托公司、是否有预期收益率（委外投资协议方式）为是时，预期最高收益率（委外投资协议方式）要素不可为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkMoney(value, "预期最高收益率%", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));

        value =StringUtils.isNotBlank(assetDebtRegisterInfo.getQqMinExpectedReturn())?assetDebtRegisterInfo.getQqMinExpectedReturn():"";
        if(!"2801,2802,2803,2804,2805,2806,2899".contains(assetType) && StringUtils.isNotBlank(value) ){
            stringErr.append("当资产/负债类别为本行存款时，预期最低收益率（委外投资协议方式）要素必须为空。<br/>");
        }else if (StringUtils.isNotBlank(getQqExpectedReturn)&&getQqExpectedReturn.contains("02") && StringUtils.isNotBlank(value) ){
            stringErr.append("当资产/负债类别为协议委外-信托公司、是否有预期收益率（委外投资协议方式）为否时，预期最低收益率（委外投资协议方式）要素必须为空。<br/>");
        }else if (StringUtils.isNotBlank(getQqExpectedReturn)&&getQqExpectedReturn.contains("01") && StringUtils.isBlank(value) ){
            stringErr.append("当资产/负债类别为协议委外-信托公司、是否有预期收益率（委外投资协议方式）为是时，预期最低收益率（委外投资协议方式）要素不可为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkMoney(value, "预期最低收益率%", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));
        if(StringUtils.isNotBlank(value) && StringUtils.isNotBlank(assetDebtRegisterInfo.getQqMaxExpectedReturn()) &&
                String.valueOf(assetDebtRegisterInfo.getQqMaxExpectedReturn()).compareTo(value) < 0){
            stringErr.append("预期最低收益率（资产管理计划）应小于或等于预期最高收益（资产管理计划）。<br/>");
        }

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getQqManagerFeeRate(),"管理费率（委外投资协议方式）","2801,2802,2803,2804,2805,2806,2899",assetType,"协议委外-信托公司","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getQqManagerFeeRate(), "管理费率（委外投资协议方式）", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getQqCustodianFeeRate(),"托管费率（委外投资协议方式）","2801,2802,2803,2804,2805,2806,2899",assetType,"协议委外-信托公司","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getQqCustodianFeeRate(),"托管费率（委外投资协议方式）", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getQqTransCostRate(),"交易相关合计费率（委外投资协议方式）","2801,2802,2803,2804,2805,2806,2899",assetType,"协议委外-信托公司","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getQqTransCostRate(),"交易相关合计费率（委外投资协议方式）", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getQqInterFeeRate(),"中介服务机构合计费率（委外投资协议方式）","2801,2802,2803,2804,2805,2806,2899",assetType,"协议委外-信托公司","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getQqInterFeeRate(),"中介服务机构合计费率（委外投资协议方式）", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getQqOtherExpensesRate(),"其他合计费率（委外投资协议方式）","2801,2802,2803,2804,2805,2806,2899",assetType,"协议委外-信托公司","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getQqOtherExpensesRate(),"其他合计费率（委外投资协议方式）", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));

        //其他负债
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getRrCountry(),"所属国家或地区（其他负债）","2000",assetType,"其他（负债）","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getRrName(),"名称（其他负债）","2000",assetType,"其他（负债）","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getRrName(),"名称（其他负债）",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getRrTermMaturity(),"期限（其他负债）","2000",assetType,"其他（负债）","0"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getRrTermMaturity(), "期限（其他负债）", "^(\\d{1,5}", "n..5", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getRrLiabilityAmt(),"负债规模","2000",assetType,"其他（负债）","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getRrLiabilityAmt(), "负债规模", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getRrInterestRate(),"利率%","2000",assetType,"其他（负债）","0"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getRrInterestRate(), "利率%", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getRrCashBorrow(),"是否为同业借款","2000",assetType,"其他（负债）","1"));

        //其他非标准化债权类投资
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsName(),"名称（其他）","1299,1399,1599",assetType,"其他权益类投资","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getSsName(),"名称（其他）",200,"0"));
        String getSsAssetType = assetDebtRegisterInfo.getSsAssetType();
        stringErr.append(CheckDataUtils.checkAssetEmpty(getSsAssetType,"行内资产类别","1299,1399,1599",assetType,"其他权益类投资","0"));
        String getSsDetailsAssetType = assetDebtRegisterInfo.getSsDetailsAssetType();
        if( StringUtils.isNotBlank(getSsAssetType)){
            if(getSsAssetType.contains("99") && StringUtils.isBlank(getSsDetailsAssetType)){
                stringErr.append("当资产/负债类别为其他权益类投资、行内资产类别为其他时，行内资产类别说明要素不可为空。<br/>");
            }else if(StringUtils.isNotBlank(getSsDetailsAssetType)){
                stringErr.append("当资产/负债类别为其他权益类投资、行内资产类别为场内股票质押回购时，行内资产类别说明要素必须为空。<br/>");
            }
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(getSsDetailsAssetType,"行内资产类别说明","1299,1399,1599",assetType,"其他权益类投资","0"));
        stringErr.append(CheckDataUtils.checkStringLength(getSsDetailsAssetType,"行内资产类别说明",256,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsAmt(),"金额","1299,1399,1599",assetType,"其他权益类投资","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getSsAmt(), "金额", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsValueDate(),"起息日（其他）","1299,1399,1599",assetType,"其他权益类投资","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getSsValueDate(),"起息日（其他）"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsMaturityDate(),"到期日（其他）","1299,1399,1599",assetType,"其他权益类投资","1"));
        stringErr.append(CheckDataUtils.checkDate(assetDebtRegisterInfo.getSsMaturityDate(),"到期日（其他）"));
        if(StringUtils.isNotBlank(assetDebtRegisterInfo.getSsValueDate()) && StringUtils.isNotBlank(assetDebtRegisterInfo.getSsMaturityDate()) &&
                String.valueOf(assetDebtRegisterInfo.getSsMaturityDate()).compareTo(assetDebtRegisterInfo.getSsValueDate()) < 0){
            stringErr.append("到期日（其他）必须大于或等于起息日（其他）。<br/>");
        }

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsCountry(),"所属国家或地区（其他）","1299,1399,1599",assetType,"其他权益类投资","1"));
        if("1599".contains(assetType) && assetDebtRegisterInfo.getSsCountry().contains("CHN") ){
            stringErr.append("当资产/负债类别为其他代客境外理财投资QDII时，所属国家或地区（其他）不能填写CHN 中国。<br/>");
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsExpectedReturn(),"是否有预期收益率%（其他）","1299,1399,1599",assetType,"其他权益类投资","1"));

        String getSsAnnualReturn = ObjectUtils.allNotNull(assetDebtRegisterInfo.getSsAnnualReturn())?assetDebtRegisterInfo.getSsAnnualReturn().toString():"";
        if( StringUtils.isNotBlank(assetDebtRegisterInfo.getSsExpectedReturn())){
            if(assetDebtRegisterInfo.getSsExpectedReturn().contains("01") && StringUtils.isBlank(getSsAnnualReturn)){
                stringErr.append("当资产/负债类别为其他权益类投资、是否有预期收益率（其他）为是时，项目收益率（利率）%（其他）要素不可为空。<br/>");
            }else if(assetDebtRegisterInfo.getSsExpectedReturn().contains("02") && StringUtils.isNotBlank(getSsAnnualReturn)){
                stringErr.append("当资产/负债类别为其他权益类投资、是否有预期收益率（其他）为否时，项目收益率（利率）%（其他）要素必须为空。<br/>");
            }
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(getSsAnnualReturn,"项目收益率（利率）%（其他）","1299,1399,1599",assetType,"其他权益类投资","0"));
        stringErr.append(CheckDataUtils.checkMoney(getSsAnnualReturn, "项目收益率（利率）%（其他）", "^(\\d{1,3}(\\.\\d{1,5})?)", "n（8,5）", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsInterestFrequency(),"付息频率（其他）","1299,1399,1599",assetType,"其他权益类投资","1"));
        stringErr.append(CheckDataUtils.checkMoney(assetDebtRegisterInfo.getSsInterestFrequency(),"付息频率（其他）", "^(\\d{1,2})", "n..2", "0", "1"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsDebtor(),"融资人（其他）","1299,1399,1599",assetType,"其他权益类投资","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getSsDebtor(),"融资人（其他）",200,"0"));

        value = assetDebtRegisterInfo.getSsOrganCode();
        stringErr.append(CheckDataUtils.checkAssetEmpty(value,"融资人组织结构（社会信用）代码（其他）","1299,1399,1599",assetType,"其他权益类投资","1"));
        if(StringUtils.isNotBlank(value)){
            Pattern p9=Pattern.compile("^[A-Z0-9]{9}");
            Pattern p18=Pattern.compile("^[A-Z0-9]{18}");
            if(!(p9.matcher(value).matches() || p18.matcher(value).matches()) ){
                stringErr.append("融资人组织机构（社会信用）代码（其他）要素格式不对正确的格式：9或18位大写字母或数字。<br/>");
            }
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsRateAgencyIss(),"外部评级机构名称及对融资人评级结果（其他）","1299,1399,1599",assetType,"其他权益类投资","0"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getSsRateAgencyIss(),"外部评级机构名称及对融资人评级结果（其他）",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsDebtorTypeScale(),"融资人类型（按规模划分）（其他）","1299,1399,1599",assetType,"其他权益类投资","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsDebtorTypeTech(),"融资人类型（按技术领域划分）（其他）","1299,1399,1599",assetType,"其他权益类投资","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsDebtorTypeEconomic(),"融资人类型（按经济类型划分）（其他）","1299,1399,1599",assetType,"其他权益类投资","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsProject(),"融资项目（其他）","1299,1399,1599",assetType,"其他权益类投资","1"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getSsProject(),"融资项目（其他）",200,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsIndustryDebtor(),"融资人所属行业（其他）","1299,1399,1599",assetType,"其他权益类投资","1"));
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsIndustryProject(),"融资项目所属行业（其他）","1299,1399,1599",assetType,"其他权益类投资","1"));

        String getSsMonitoryIndustry = assetDebtRegisterInfo.getSsMonitoryIndustry();
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsMonitoryIndustry(),"项目是否属于重点监控行业和领域（其他）","1299,1399,1599",assetType,"其他权益类投资","1"));

        String getSsMonitoryIndustryType = assetDebtRegisterInfo.getSsMonitoryIndustryType();
        if( StringUtils.isNotBlank(getSsMonitoryIndustry)){
            if(getSsMonitoryIndustry.contains("01") && StringUtils.isBlank(getSsMonitoryIndustryType)){
                stringErr.append("当资产/负债类别为其他权益类投资、项目是否属于重点监控行业和领域（其他）为是时，重点监控行业和领域类别（其他）要素不可为空。<br/>");
            }else if(getSsMonitoryIndustry.contains("02") && StringUtils.isNotBlank(getSsMonitoryIndustryType)){
                stringErr.append("当资产/负债类别为其他权益类投资、项目是否属于重点监控行业和领域（其他）为否时，重点监控行业和领域类别（其他）要素必须为空。<br/>");
            }
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(getSsMonitoryIndustryType,"重点监控行业和领域类别（其他）","1299,1399,1599",assetType,"其他权益类投资","0"));


        String getSsDetailsMonitoryType = assetDebtRegisterInfo.getSsDetailsMonitoryType();
        if( StringUtils.isNotBlank(getSsMonitoryIndustry)){
            if(getSsMonitoryIndustry.contains("01") && getSsMonitoryIndustryType.contains("99")&& StringUtils.isBlank(getSsDetailsMonitoryType)){
                stringErr.append("当资产/负债类别为其他权益类投资、项目是否属于重点监控行业和领域（其他）为是、重点监控行业和领域类别（其他）为其他时，重点监控行业和领域类别说明（其他）要素不可为空。<br/>");
            }else if(getSsMonitoryIndustry.contains("02") && StringUtils.isNotBlank(getSsDetailsMonitoryType)){
                stringErr.append("当资产/负债类别为其他权益类投资、项目是否属于重点监控行业和领域（其他）为否时，重点监控行业和领域类别说明（其他）要素必须为空。<br/>");
            }
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(getSsDetailsMonitoryType,"重点监控行业和领域类别说明（其他）","1299,1399,1599",assetType,"其他权益类投资","0"));
        stringErr.append(CheckDataUtils.checkStringLength(getSsDetailsMonitoryType,"重点监控行业和领域类别说明（其他）",256,"0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsInternalAssetRate(),"对应资产外部评级","1299,1399,1599",assetType,"其他权益类投资","1"));

        String getSsGuaranteeMethod = assetDebtRegisterInfo.getSsGuaranteeMethod();
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsGuaranteeMethod(),"担保方式（其他）","1299",assetType,"其他权益类投资","0"));

        if(StringUtils.isNotBlank(getSsGuaranteeMethod)&&StringUtils.isBlank(assetDebtRegisterInfo.getSsDetailsGuarantee())){
            stringErr.append("当资产/负债类别为其他非标准化债权类投资、担保方式（其他）为信用担保时，担保情况说明（其他）要素不可为空。<br/>");
        }else if(StringUtils.isBlank(getSsGuaranteeMethod)&&StringUtils.isNotBlank(assetDebtRegisterInfo.getSsDetailsGuarantee())){
            stringErr.append("当担保方式（其他）为空时，担保情况说明（其他）必须为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsDetailsGuarantee(),"担保情况说明（其他）","1299",assetType,"其他权益类投资","0"));
        stringErr.append(CheckDataUtils.checkStringLength(assetDebtRegisterInfo.getSsDetailsGuarantee(),"担保情况说明（其他）",200,"0"));

        String getSsPledgeType = assetDebtRegisterInfo.getSsPledgeType();
        if(StringUtils.isNotBlank(getSsGuaranteeMethod) ){
            if("03,04".contains(getSsGuaranteeMethod) && StringUtils.isBlank(getSsPledgeType)){
                stringErr.append("当资产/负债类别为其他非标准化债权类投资、担保方式（其他）为抵押时，抵质押物类型（其他）要素不可为空。<br/>");
            }else if(!"03,04".contains(getSsGuaranteeMethod) && StringUtils.isNotBlank(getSsPledgeType)){
                stringErr.append("当资产/负债类别为其他非标准化债权类投资、担保方式（其他）为信用担保时，抵质押物类型（其他）要素必须为空。<br/>");
            }
        }else if(StringUtils.isNotBlank(getSsPledgeType)){
            stringErr.append("当担保方式（其他）为空时，抵质押物类型（其他）必须为空。<br/>");
        }

        stringErr.append(CheckDataUtils.checkAssetEmpty(getSsPledgeType,"抵质押物类型","1299",assetType,"其他权益类投资","0"));

        String getSsPledgeValue = assetDebtRegisterInfo.getSsPledgeValue();
        if(StringUtils.isNotBlank(getSsGuaranteeMethod) ){
            if("03,04".contains(getSsGuaranteeMethod) && StringUtils.isBlank(getSsPledgeValue)){
                stringErr.append("当资产/负债类别为其他非标准化债权类投资、担保方式（其他）为抵押时，抵质押物价值（其他）要素不可为空。<br/>");
            }else if(!"03,04".contains(getSsGuaranteeMethod) && StringUtils.isNotBlank(getSsPledgeValue)){
                stringErr.append("当资产/负债类别为其他非标准化债权类投资、担保方式（其他）为信用担保时，抵质押物价值（其他）要素必须为空。<br/>");
            }
        }else if(StringUtils.isNotBlank(getSsPledgeValue)){
            stringErr.append("当担保方式（其他）为空时，抵质押物价值（其他）必须为空。<br/>");
        }

        stringErr.append(CheckDataUtils.checkAssetEmpty(getSsPledgeValue,"抵质押物价值（其他）","1299",assetType,"其他权益类投资","0"));
        stringErr.append(CheckDataUtils.checkMoney(getSsPledgeValue, "抵质押物价值（其他）", "^(\\d{1,13}(\\.\\d{1,2})?)", "n（15,2）", "0", "1"));

        String getSsGuaranteeType = assetDebtRegisterInfo.getSsGuaranteeType();
        if(StringUtils.isNotBlank(getSsGuaranteeMethod) ){
            if("01,02".contains(getSsGuaranteeMethod) && StringUtils.isBlank(getSsGuaranteeType)){
                stringErr.append("当资产/负债类别为其他非标准化债权类投资、担保方式（其他）为抵押时，担保性质（其他）要素不可为空。<br/>");
            }else if(!"01,02".contains(getSsGuaranteeMethod) && StringUtils.isNotBlank(getSsGuaranteeType)){
                stringErr.append("当资产/负债类别为其他非标准化债权类投资、担保方式（其他）为信用担保时，担保性质（其他）要素必须为空。<br/>");
            }
        }else if(StringUtils.isNotBlank(getSsGuaranteeType)){
            stringErr.append("当担保方式（其他）为空时，担保性质（其他）必须为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(getSsGuaranteeType,"担保性质（其他）","1299",assetType,"其他权益类投资","0"));

        String getSsGuarantorType = assetDebtRegisterInfo.getSsGuarantorType();
        if(StringUtils.isNotBlank(getSsGuaranteeMethod) ){
            if("01,02".contains(getSsGuaranteeMethod) && StringUtils.isBlank(getSsGuarantorType)){
                stringErr.append("当资产/负债类别为其他非标准化债权类投资、担保方式（其他）为抵押时，担保人与融资人关系（其他）要素不可为空。<br/>");
            }else if(!"01,02".contains(getSsGuaranteeMethod) && StringUtils.isNotBlank(getSsGuarantorType)){
                stringErr.append("当资产/负债类别为其他非标准化债权类投资、担保方式（其他）为信用担保时，担保人与融资人关系（其他）要素必须为空。<br/>");
            }
        }else if(StringUtils.isNotBlank(getSsGuarantorType)){
            stringErr.append("当担保方式（其他）为空时，担保人与融资人关系（其他）必须为空。<br/>");
        }
        stringErr.append(CheckDataUtils.checkAssetEmpty(getSsGuarantorType,"担保人与融资人关系（其他）","1299",assetType,"其他权益类投资","0"));

        stringErr.append(CheckDataUtils.checkAssetEmpty(assetDebtRegisterInfo.getSsDebtEquitySwap(),"是否为债转股（其他）","1399",assetType,"其他权益类投资","1"));

        return stringErr.toString();
    }
}
