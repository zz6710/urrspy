package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "prodInfoOdsService", table = "t8_prod_creative_project")
public class ProdInfoOds {
    @GraphQLField
    private String id;
    @GraphQLField
    private String motherFundCode;
    @GraphQLField
    private String prodName;
    @GraphQLField
    private String prodCode;
    @GraphQLField
    private String prodNmFu;
    @GraphQLField
    private String checkInon;
    @GraphQLField
    private String pbcRegcode;
    @GraphQLField
    private String t8InvestPropType;
    @GraphQLField
    private String prodMod;
    @GraphQLField
    private String collMod;
    @GraphQLField
    private String operMod;
    @GraphQLField
    private String prodCycle;
    @GraphQLField
    private String prodBrand;
    @GraphQLField
    private String prodTimes;
    @GraphQLField
    private String issuCcy;
    @GraphQLField
    private String returnCcy;
    @GraphQLField
    private String incomeCcy;
    @GraphQLField
    private String subsBdate;
    @GraphQLField
    private String subsEdate;
    @GraphQLField
    private String termFlag;
    @GraphQLField
    private String redeemFlag;
    @GraphQLField
    private String prodCreditFlag;
    @GraphQLField
    private String prodCreditOrg;
    @GraphQLField
    private String prodCreditMod;
    @GraphQLField
    private String bordTrustiCode;
    @GraphQLField
    private String bordTrustiCodeP;
    @GraphQLField
    private String oversTrustiNation;
    @GraphQLField
    private String bordTrustiName;
    @GraphQLField
    private String oversTrustiName;
    @GraphQLField
    private String establishDate;
    @GraphQLField
    private String realEndDate;
    @GraphQLField
    private String endDate;
    @GraphQLField
    private String isStructprod;
    @GraphQLField
    private String cashType;
    @GraphQLField
    private String cashTypeZ;
    @GraphQLField
    private String rglrPrdOpnCyc;
    @GraphQLField
    private String othRegulOpenCyc;
    @GraphQLField
    private String prodStatus;
    @GraphQLField
    private String incomeType;
    @GraphQLField
    private String newOldProdF;
    @GraphQLField
    private String blgFinSamBusF;
    @GraphQLField
    private String salePlace;
    @GraphQLField
    private String speciCountryRegion;
    @GraphQLField
    private String srvMode;
    @GraphQLField
    private String assetMaping;
    @GraphQLField
    private String manageMode;
    @GraphQLField
    private String adminName;
    @GraphQLField
    private String pricingType;
    @GraphQLField
    private String perfmBenchmRate;
    @GraphQLField
    private String isMinHoldTerm;
    @GraphQLField
    private String minHoldTerm;
    @GraphQLField
    private String redeemAfterHold;
    @GraphQLField
    private String prodSalZon;
    @GraphQLField
    private String investThresh;
    @GraphQLField
    private String saleCommisRate;
    @GraphQLField
    private String custodyFeeRate;
    @GraphQLField
    private String subscrSdEarliest;
    @GraphQLField
    private String subscrEdLatest;
    @GraphQLField
    private String planFundAmount;
    @GraphQLField
    private String investorTrend;
    @GraphQLField
    private String riskLev;
    @GraphQLField
    private String prodEspPrpt;
    @GraphQLField
    private String investMngFeeRate;
    @GraphQLField
    private String cooperationMode;
    @GraphQLField
    private String cooperationOrgName;
    @GraphQLField
    private String returnCost;
    @GraphQLField
    private String returnIncome;
    @GraphQLField
    private String prodPrecent;
    @GraphQLField
    private String authorName;
    @GraphQLField
    private String authorIdentif;
    @GraphQLField
    private String designName;
    @GraphQLField
    private String designIdentif;
    @GraphQLField
    private String manageName;
    @GraphQLField
    private String manageIdentif;
    @GraphQLField
    private String salemanName;
    @GraphQLField
    private String salemanPhoneno;
    @GraphQLField
    private String salemanTelno;
    @GraphQLField
    private String salemanEmail;
    @GraphQLField
    private String clsfSto;
    @GraphQLField
    private String perfmBenchmUpper;
    @GraphQLField
    private String perfmBenchmLower;
    @GraphQLField
    private String yjbjjzsmPj;
    @GraphQLField
    private String openMod;
    @GraphQLField
    private String regularOpenCycle;
    @GraphQLField
    private String irregularOpenDesc;
    @GraphQLField
    private String firstOpenDate;
    @GraphQLField
    private String isOpenInHoliday;
    @GraphQLField
    private String openTimes;
    @GraphQLField
    private String openPeriodBusiness;
    @GraphQLField
    private String openPeriBusiDesc;
    @GraphQLField
    private String accountCode;
    @GraphQLField
    private String accountName;
    @GraphQLField
    private String msgType;
    @GraphQLField
    private String redeemFlagPb;
    @GraphQLField
    private String prodChangeDate;
    @GraphQLField
    private String custodyOrgMngDuty;
    @GraphQLField
    private String incomeTransProdMark;
    @GraphQLField
    private String crossBorderWealth;
    @GraphQLField
    private String baseInfoOpenMark;
    @GraphQLField
    private String changeReason;
    @GraphQLField
    private String prodRenewalMark;
    @GraphQLField
    private String liquidateMark;
    @GraphQLField
    private String minPrdOpnCyc;
    @GraphQLField
    private String crtDt;
    @GraphQLField
    private String crtTime;
    @GraphQLField
    private String updDt;
    @GraphQLField
    private String updTime;
    @GraphQLField
    private String dataFlag;
    @GraphQLField
    private String subsBdate1;
    @GraphQLField
    private String subsBdate2;
    @GraphQLField
    private String establishDate1;
    @GraphQLField
    private String establishDate2;
    @GraphQLField
    private String realEndDate1;
    @GraphQLField
    private String realEndDate2;
}
