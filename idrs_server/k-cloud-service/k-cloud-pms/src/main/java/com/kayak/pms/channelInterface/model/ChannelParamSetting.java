package com.kayak.pms.channelInterface.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "channelParamSettingService",table = "t8_channel_param_setting")
@Data
public class ChannelParamSetting {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "渠道编码", sql = "channel_no = $S{channelNo}" ,field = "channel_no")
   private String channelNo;
   @GraphQLField(kkhtml = "KFieldText", label = "接口编码", sql = "interface_no = $S{interfaceNo}" ,field = "interface_no")
   private String interfaceNo;
   @GraphQLField(kkhtml = "KFieldText", label = "字段名称", sql = "field_name = $S{fieldName}" ,field = "field_name")
   private String fieldName;
   @GraphQLField(kkhtml = "KFieldText", label = "数据字典", sql = "field_dict = $S{fieldDict}" ,field = "field_dict")
   private String fieldDict;
   @GraphQLField(kkhtml = "KFieldText", label = "对接方数据字典", sql = "other_field = $S{otherField}" ,field = "other_field")
   private String otherField;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}" ,field = "remark")
   private String remark;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user = $S{crtUser}" ,field = "crt_user")
   private String crtUser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
    @GraphQLField(kkhtml = "KFieldText", label = "字段", sql = "field = $S{field}" ,field = "field")
    private String field;
}