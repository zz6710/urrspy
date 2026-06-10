package com.kayak.dps.operation.model;


import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * @description: 运营情况统计分析
 * @author: axin
 * @create: 2023-06-01 19:20
 */
@Data
@GraphQLModel(fetcher = "operationSituationService", table = "app_operation_xxx")
public class OperationSituation {

    @GraphQLField(field = "ID")
    private String id;

    /** app_operation_asset_positions -- 资产占比 **/
    @GraphQLField(field = "资产代码")
    private String assetCode;
    @GraphQLField(field = "资产名称")
    private String assetName;
    @GraphQLField(field = "产品代码")
    private String prodCode;
    @GraphQLField(field = "产品名称")
    private String prodName;
    @GraphQLField(field = "业务日期")
    private String tradeDate;
    @GraphQLField(field = "持仓份额")
    private String positionVol;
    @GraphQLField(field = "持仓金额")
    private String positionAmt;
    @GraphQLField(field = "当日占比")
    private String proportion;
    @GraphQLField(field = "数据日期")
    private String crtDate;
    /** 页面参数 **/
    @GraphQLField(field = "value")
    private String value;
    @GraphQLField(field = "name")
    private String name;
    @GraphQLField(field = "label")
    private String label;
    @GraphQLField(field = "amount")
    private String amount;

    /** app_operation_prod_scale -- 产品规模 **/
    @GraphQLField(field = "子产品代码")
    private String prodCodeSub;
    @GraphQLField(field = "现有规模")
    private String existingScale;
    @GraphQLField(field = "上月规模")
    private String historyScale;
    /** 页面参数 **/
    @GraphQLField(field = "title")
    private String title;
    @GraphQLField(field = "unit")
    private String unit;
    @GraphQLField(field = "status")
    private String status;
    @GraphQLField(field = "percent")
    private String percent;
    @GraphQLField(field = "tip")
    private String tip;

    /** app_operation_org_hold -- 机构持有量 **/
    @GraphQLField(field = "机构简称")
    private String orgName;
    @GraphQLField(field = "机构代码")
    private String orgCode;
    @GraphQLField(field = "持有数量")
    private String holdScale;
    @GraphQLField(field = "数据集")
    private List<SeriesData> seriesData;
    @GraphQLField(field = "标题栏")
    private List<String> axisData;
    @GraphQLField(field = "颜色")
    private List<String> color;

    /** app_operation_asset_distribute 债券分布情况**/
    @GraphQLField(field = "评级")
    private String grade;
    @GraphQLField(field = "行业")
    private String industry;
    @GraphQLField(field = "债券类型")
    private String assetType;
    @GraphQLField(field = "穿透前持有")
    private String pierceBefore;
    @GraphQLField(field = "穿透后持有")
    private String pierceLater;

    @Data
    public static class SeriesData {
        private String name;
        private String type;
        private List<String> data;
        private String unit;
    }

/*
    title: '产品现有总规模',
    unit: '亿元',
    value: "20,015",
    status: 'up',
    percent: '8.5',
    tip: '同比上月末'
*/







}
