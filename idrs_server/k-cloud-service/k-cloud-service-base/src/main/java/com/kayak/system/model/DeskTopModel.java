package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "deskTopService", table = "")
@Data
public class DeskTopModel {
    @GraphQLField(key = true , label = "id" ,field = "id")
    private String id;
    @GraphQLField(label = "接口名称" ,field = "port_name")
    private String portName;
    @GraphQLField(label = "接口类型" ,field = "port_type")
    private String portType;
    @GraphQLField(label = "处理日期" ,field = "deal_date")
    private String dealDate;
    @GraphQLField(label = "处理状态" ,field = "file_state")
    private String fileState;
    @GraphQLField(label = "总笔数" ,field = "total_num")
    private String totalNum;
    @GraphQLField( label = "信披类型" ,field = "disclosure_type")
    private String disclosureType;
    @GraphQLField(label = "信披子类型" ,field = "disclosure_son_type")
    private String disclosureSonType;
    @GraphQLField(label = "计划发布日期" ,field = "plan_fb_date")
    private String planFbDate;
    @GraphQLField(label = "信披状态" ,field = "disclosure_status")
    private String disclosureStatus;
    @GraphQLField(label = "披露情况标识",field = "disclosure_flag")
    private String disclosureFlag;
    @GraphQLField(label = "披露情况统计",field = "disclosure_count")
    private String disclosureCount;
    @GraphQLField(label = "补录页面",field = "page")
    private String page;
    @GraphQLField(label = "持仓日期",field = "holding_date")
    private String holdingDate;
    @GraphQLField(label = "代码",field = "scr_cd")
    private String scrCd;
    @GraphQLField(label = "名称",field = "scr_nm")
    private String scrNm;
    @GraphQLField
    private String reportType;
    @GraphQLField
    private String reportTable;
    @GraphQLField
    private String reportTableName;
    @GraphQLField
    private String theoryReportStartDate;
    @GraphQLField
    private String theoryReportEndDate;
    @GraphQLField
    private String registerDate;
    @GraphQLField
    private String total;
    @GraphQLField
    private String reportSuccessNumber;
    @GraphQLField
    private String status;
    @GraphQLField
    private String registerStatus;
    @GraphQLField
    private String createDate;
    @GraphQLField
    private String createTime;
    @GraphQLField
    private String updateDate;
    @GraphQLField
    private String updateTime;
    @GraphQLField
    private String workDay;
    @GraphQLField
    private String needTotal;
    @GraphQLField
    private String checkType;
    @GraphQLField
    private String reportCatgory;
    @GraphQLField
    private String tableName;
    @GraphQLField
    private String theoryStartDate;
    @GraphQLField
    private String theoryEndDate;
    @GraphQLField
    private String realStartDate;
    @GraphQLField
    private String realEndDate;
    @GraphQLField
    private String resultOrder;
    @GraphQLField
    private String indexCode;
    @GraphQLField
    private String indexName;
    @GraphQLField
    private String validateTable;
    @GraphQLField
    private String columnCode;
    @GraphQLField
    private String validateType;
    @GraphQLField
    private String validateResult;
    @GraphQLField
    private String reason;
    @GraphQLField
    private String validateColumn;
    @GraphQLField
    private String validateRow;
    @GraphQLField
    private String dataNum;




    /*原资产代码*/
    @GraphQLField
    private String oldScrCd;

    /*市场*/
    @GraphQLField
    private String trxMkt;

    /*资产分类*/
    @GraphQLField
    private String assetType;

    /*生效状态*/
    @GraphQLField
    private String dataSource;

    /*生效日期*/
    @GraphQLField
    private String effectiveDate;

    /*生效时间*/
    @GraphQLField
    private String effectiveTime;

    /*失效日期*/
    @GraphQLField
    private String expirationDate;

    /*失效时间*/
    @GraphQLField
    private String expirationTime;

    /*更新日期*/
    @GraphQLField
    private String updDate;

    /*更新时间*/
    @GraphQLField
    private String updTime;

    /*修改人*/
    @GraphQLField
    private String updUser;

    /*信息*/
    @GraphQLField
    private String msg;

    /*办结日期*/
    @GraphQLField
    private String endDate;

    /*是否已办结   默认查询未办结*/
    @GraphQLField
    private String topInfoStatus = "0";

    /*来源表*/
    @GraphQLField
    private String sourceTable;

    /*日期*/
    @GraphQLField
    private String crtDate;

    /*时间*/
    @GraphQLField
    private String crtTime;


    /*数据键*/
    @GraphQLField
    private String keyword;

}
