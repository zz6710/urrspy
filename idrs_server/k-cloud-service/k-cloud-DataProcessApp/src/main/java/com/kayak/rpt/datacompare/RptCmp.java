package com.kayak.rpt.datacompare;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "rptCmpService", table = "base_rpt_cmp_result")
public class RptCmp {

	@GraphQLField(key = true, label = "ID", sql = "id = $S{id}", field = "id")
	private String id;
	@GraphQLField(label = "数据日期")
	private String dealDate;
	@GraphQLField(label = "报送日期", sql = "a.report_date = $S{reportDate}", field = "reportDate")
	private String reportDate;
	@GraphQLField(label = "表名", sql = "a.table_name = $S{tableName}", field = "tableName")
	private String tableName;
	@GraphQLField(label = "表名",  field = "systemTableName")
	private String systemTableName;
	@GraphQLField(label = "左边业务主键", field = "rt_pk")
	private String lfPk;
	@GraphQLField(label = "右边业务主键",  field = "rt_pk")
	private String rtPk;

	@GraphQLField(label = "左边行代码", field = "lf_row_code")
	private String lfRowCode;
	@GraphQLField(label = "右边行代码",  field = "rt_row_code")
	private String rtRowCode;

	@GraphQLField(label = "左边行名称", field = "lf_row_name")
	private String lfRowName;
	@GraphQLField(label = "右边行名称",  field = "rt_row_name")
	private String rtRowName;

	@GraphQLField(label = "左边列代码", field = "lf_column_code")
	private String lfColumnCode;
	@GraphQLField(label = "右边列代码",  field = "rt_column_code")
	private String rtColumnCode;

	@GraphQLField(label = "左边行名称", field = "lf_column_name")
	private String lfColumnName;
	@GraphQLField(label = "右边行名称",  field = "rt_column_name")
	private String rtColumnName;


	@GraphQLField(label = "左边字段", field = "lf_data")
	private String lfData;
	@GraphQLField(label = "右边字段",  field = "rt_data")
	private String rtData;
	@GraphQLField(label = "上传记录数",  field = "upload_count")
	private String uploadCount;
	@GraphQLField(label = "不同记录数",  field = "diff_count")
	private String diffCount;
	@GraphQLField(label = "相同记录数",  field = "same_count")
	private String sameCount;
	@GraphQLField(label = "主键匹配记录数",  field = "pk_match_count")
	private String pkMatchCount;
	@GraphQLField(label = "全部匹配记录数",  field = "all_match_count")
	private String allMatchCount;
	@GraphQLField(label = "系统记录数",  field = "system_count")
	private String systemCount;
	@GraphQLField(label = "不匹配记录数",  field = "not_match_count")
	private String notMatchCount;


}
