package com.kayak.dps.app.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@GraphQLModel(fetcher = "linkedTransMappingService",table = "dwd_linked_trans_mapping_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DwdLinkedTransMapping {

    @ExcelIgnore
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;

    @GraphQLField(kkhtml = "KFieldText", label = "交易对手名称", sql = "counter_party_name = $S{counterPartyName}" ,field = "counter_party_name")
    private String counterPartyName;

    @GraphQLField(kkhtml = "KFieldText", label = "关联交易情况", sql = "counter_type = $S{counterType}" ,field = "counter_type")
    private String counterType;

    @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "reg_code = $S{regCode}" ,field = "reg_code")
    private String regCode;

    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}" ,field = "remark")
    private String remark;

    @GraphQLField(kkhtml = "KFieldText", label = "托管行名称", sql = "care_name = $S{careName}" ,field = "care_name")
    private String careName;

    @GraphQLField(kkhtml = "KFieldText", label = "数据来源", sql = "data_from = $S{dataFrom}" ,field = "data_from")
    private String dataFrom;

    @GraphQLField(kkhtml = "KFieldText", label = "更新人", sql = "user_id = $S{userId}" ,field = "user_id")
    private String userId;

    @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "update_time = $S{updateTime}" ,field = "update_time")
    private String updateTime;
}
