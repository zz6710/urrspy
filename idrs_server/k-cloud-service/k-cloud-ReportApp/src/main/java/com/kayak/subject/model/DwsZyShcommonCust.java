package com.kayak.subject.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwsZyShcommonCustService",table = "dws_zy_shcommon_cust")
@Data
public class DwsZyShcommonCust {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "客户名称", sql = "cust_name = $S{custName}" ,field = "cust_name")
   private String custName;
   @GraphQLField(kkhtml = "KFieldText", label = "统一社会信用代码", sql = "registernumber = $S{registernumber}" ,field = "registernumber")
   private String registernumber;
   @GraphQLField(kkhtml = "KFieldText", label = "组织机构代码", sql = "s_info_org_code = $S{sInfoOrgCode}" ,field = "s_info_org_code")
   private String sInfoOrgCode;
   @GraphQLField(kkhtml = "KFieldText", label = "其他代码", sql = "s_info_oth_code = $S{sInfoOthCode}" ,field = "s_info_oth_code")
   private String sInfoOthCode;
   @GraphQLField(kkhtml = "KFieldText", label = "其他代码类型", sql = "s_info_oth_type = $S{sInfoOthType}" ,field = "s_info_oth_type")
   private String sInfoOthType;
   @GraphQLField(kkhtml = "KFieldText", label = "行内客户号", sql = "cust_number = $S{custNumber}" ,field = "cust_number")
   private String custNumber;
   @GraphQLField(kkhtml = "KFieldText", label = "国民经济行业分类", sql = "ne_ind_code = $S{neIndCode}" ,field = "ne_ind_code")
   private String neIndCode;
   @GraphQLField(kkhtml = "KFieldText", label = "经济成分", sql = "ne_ind_type = $S{neIndType}" ,field = "ne_ind_type")
   private String neIndType;
   @GraphQLField(kkhtml = "KFieldText", label = "是否为关联方", sql = "s_relevance = $S{sRelevance}" ,field = "s_relevance")
   private String sRelevance;
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "act_dt = $S{actDt}" ,field = "act_dt")
   private String actDt;
   @GraphQLField(kkhtml = "KFieldText", label = "处理日期", sql = "deal_date = $S{dealDate}" ,field = "deal_date")
   private String dealDate;

}