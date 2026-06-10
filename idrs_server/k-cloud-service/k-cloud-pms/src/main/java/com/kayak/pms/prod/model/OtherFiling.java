package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * com.kayak.pms.prod.model
 * user:rennannan
 * date:2021/3/17 11:04
 * function:其他报备材料实体  用于解决权限问题新建
 */
@Data
@GraphQLModel(fetcher = "otherFilingService")
public class OtherFiling {
    @GraphQLField(label="文档id")
    private String id;
    @GraphQLField(label="产品代码")
    private String prodCode;
    @GraphQLField(label="文档类型")
    private String documentType;

    @GraphQLField(label="产品名称")
    private String prodName;
    @GraphQLField(label="是否代码回收")
    private String isRecycleCode;

}
