package com.kayak.pms.T82.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t8DictService")
public class T8Dict {

    @GraphQLField
    private String id;
    @GraphQLField
    private String t8ProdInfoId;
    @GraphQLField( label = "销售商", sql = "distributor_code IN ($U{distributorCode})" ,field = "distributor_code",kkhtmlExt="{\"data-action\":\"T8Dict.findTaDistributorInfos\",\"data-display-field\":\"distributorCode,distributorName\",\"data-value-field\":\"distributorCode\"}")
    private String distributorCode;
    @GraphQLField( label = "销售商名称", sql = "distributor_name = $S{distributorName}" ,field = "distributor_name")
    private String distributorName;

    @GraphQLField(label = "产品代码", sql = "prod_code IN ($U{prodCode})" ,field = "prod_code", kkhtmlExt="{\"data-action\":\"T8Dict.findTaProdInfos\",\"data-display-field\":\"prodCode,prodName\",\"data-value-field\":\"prodCode\"}")
    private String prodCode;

    @GraphQLField(label = "产品名称", sql = "prod_name = $S{prodName}" ,field = "prod_name")
    private String prodName;

    @GraphQLField(label = "产品简称", sql = "prod_name_short = $S{prodNameShort}" ,field = "prod_name_short")
    private String prodNameShort;

    @GraphQLField(label = "产品类型", sql = "profit_type = $S{profitType}" ,field = "profit_type")
    private String profitType; // （0-净值型 1-收益型 2-货币型）

    @GraphQLField(label = "产品模型", sql = "period_type = $S{periodType}" ,field = "period_type")
    private String periodType;

    @GraphQLField(label = "产品状态", sql = "prod_status = $S{prodStatus}" ,field = "prod_status")
    private String prodStatus;

    @GraphQLField(label = "产品子状态", sql = "prod_son_status in ($U{prodSonStatus})", field = "prod_son_status")
    private String prodSonStatus;

    @GraphQLField(label = "", sql = "dict = $S{dict}", field = "dict")
    private String dict;
    @GraphQLField(label = "", sql = "itemkey = $S{itemkey}", field = "itemkey")
    private String itemkey;

    @GraphQLField(label = "", sql = "itemval = $S{itemval}", field = "itemval")
    private String itemval;
    @GraphQLField(label = "会议id")
    private String meetId;
    @GraphQLField(label = "决议类型", sql = "t8_decision_type = $S{t8DecisionType}" ,field = "t8_decision_type")
    private String t8DecisionType;
    @GraphQLField(label = "产品投资经理id")
    private String investManageIdcardNo;
    @GraphQLField(label = "产品投资经理名称")
    private String custName;
    @GraphQLField(label = "产品投资经理序号id")
    private String custNo;
    @GraphQLField(label = "信披渠道代码")
    private String channelCode;
    @GraphQLField(label = "信披渠道名称")
    private String channelName;
    @GraphQLField
    private String jobno;
    @GraphQLField
    private String userId;
    @GraphQLField
    private String userName;
    @GraphQLField
    private String prodMode;
    @GraphQLField(label = "系列代码", sql = "series_code = $S{seriesCode}" ,field = "series_code")
    private String seriesCode;
    @GraphQLField(label = "系列名称", sql = "series_name = $S{seriesName}" ,field = "series_name")
    private String seriesName;
    @GraphQLField
    private String seriesExplain;
    @GraphQLField
    private String t8ProdSeriesId;

    @GraphQLField(label = "接口名称",field = "port_name")
    private String portName;
    @GraphQLField(label = "接口代码",field = "port_code")
    private String portCode;
    
    /**
     * 运营机构
     */
    @GraphQLField
    private String operatingAgency;
    
}
