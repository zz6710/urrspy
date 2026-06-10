package com.kayak.pms.T85.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * 文件名: T8ProdModeInfo.java
 * 描述: 产品模型信息
 * 创建人: zengzt
 * 创建时间:2020年6月4日下午1:44:14
 */
@Data
@GraphQLModel(fetcher = "t8ProdModeInfoService", table = "T8_PROD_MODE_INFO")
public class T8ProdModeInfo {

	@GraphQLField(label = "产品模型",field = "prodMode")
	private String prodMode;
	
	@GraphQLField(label = "产品模型名称",field = "prodModeName",kkhtmlDefault = true)
	private String prodModeName;

	@GraphQLField(label = "产品创建URL",field = "createUrl")
	private String createUrl;
	
	@GraphQLField(label = "说明",field = "remark",kkhtmlDefault = true)
	private String remark;

	private String prodCode;

	
}
