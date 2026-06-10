package com.kayak.pms.indexInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @program: k-cloud  
 * @description: 指数基本信息表
 * @author:  WangZhenXin
 * @create: 2021-01-13 17:29:51 
 * @memo 备注信息
 */

@Data
@GraphQLModel(fetcher="t8IndexInfoService",table="t8_index_info")
public class T8IndexInfo {


	/**
	 * id
	 */
   	@GraphQLField(label="id", sql="id=$S{id}",field="id",key = true)
	private String id;

	/**
	 * 指数代码
	 */
   	@GraphQLField(label="指数代码",kkhtml = "KFieldText", kkhtmlDefault = true, sql="index_code like '%$U{indexCode}%'",field="index_code")
	private String indexCode;

	/**
	 * 指数名称
	 */
   	@GraphQLField(label="指数名称",kkhtml = "KFieldText", kkhtmlDefault = true, sql="index_name like '%$U{indexName}%'",field="index_name")
	private String indexName;

	/**
	 * 基准日期
	 */
   	@GraphQLField(label="基准日期", sql="base_date=$S{baseDate}",field="base_date")
	private String baseDate;

	/**
	 * 基准点数
	 */
   	@GraphQLField(label="基准点数", sql="base_points=$S{basePoints}",field="base_points")
	private Double basePoints;

	/**
	 * 成分股数量
	 */
   	@GraphQLField(label="成分股数量", sql="constituent_stocks=$S{constituentStocks}",field="constituent_stocks")
	private String constituentStocks;

	/**
	 * 创建日期
	 */
   	@GraphQLField(label="创建日期", sql="create_date=$S{createDate}",field="create_date")
	private String createDate;

	/**
	 * 创建时间
	 */
   	@GraphQLField(label="创建时间", sql="create_time=$S{createTime}",field="create_time")
	private String createTime;

	/**
	 * 更新日期
	 */
   	@GraphQLField(label="更新日期", sql="update_date=$S{updateDate}",field="update_date")
	private String updateDate;

	/**
	 * 更新时间
	 */
   	@GraphQLField(label="更新时间", sql="update_time=$S{updateTime}",field="update_time")
	private String updateTime;

	/**
	 * 创建人
	 */
   	@GraphQLField(label="创建人", sql="create_user_id=$S{createUserId}",field="create_user_id")
	private String createUserId;

	/**
	 * 创建人名称
	 */
   	@GraphQLField(label="创建人名称", sql="create_user_name=$S{createUserName}",field="create_user_name")
	private String createUserName;

}
