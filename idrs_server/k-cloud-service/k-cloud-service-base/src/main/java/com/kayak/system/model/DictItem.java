package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "dictItemService", table = "sys_dict_item")
public class DictItem {

    /**
     * 字典标识
     */
    @GraphQLField(key = true, kkhtml = "defaultText", label = "字典标识", sql = "dict = $S{dict}", field = "dict")
    private String dict;

    /**
     * 数据键
     */
    @GraphQLField(key = true, kkhtml = "defaultText", label = "数据键", sql = "itemkey = $S{itemkey}", field = "itemkey")
    private String itemkey;

    /**
     * 数据值
     */
    @GraphQLField(kkhtml = "defaultText", label = "数据键", sql = "itemval = $S{itemval}", field = "itemval")
    private String itemval;

    /**
     * 渲染样式
     */
    @GraphQLField(kkhtml = "defaultText", label = "数据键", sql = "itemrender = $S{itemrender}", field = "itemrender")
    private String itemrender;

    /**
     * 排序
     */
    @GraphQLField(kkhtml = "defaultText", label = "数据键", sql = "itemorder = $S{itemorder}", field = "itemorder")
    private String itemorder;

}
