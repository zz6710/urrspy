package com.kayak.pms.opFlow.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

import java.util.List;

@Data
@GraphQLModel(fetcher = "opBusiInfoService",table = "op_busi_info")
public class OpBusiInfo {

    @GraphQLField(label = "功能id", sql = "busi_id = $S{busiId}", field = "busi_id")
    private String busiId;

    @GraphQLField(label = "功能名称", sql = "busi_name like '%$U{busiName}%'", field = "busi_name", kkhtml = "KFieldText")
    private String busiName;

    @GraphQLField(label = "上级功能", sql = "upper_id = $S{upperId}", field = "upper_id", kkhtml = "KFieldSelect", kkhtmlExt = "{\"data-action\":\"OpBusiInfo.findAll\",\"data-display-field\":\"busiName\",\"data-value-field\":\"busiId\"}")
    private String upperId;

    @GraphQLField(label = "icon图标", field = "icon_class")
    private String iconClass;

    private List<OpBusiInfo> children;
}
