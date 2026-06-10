package com.kayak.pms.T85.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

/**
 * 文件名: SysDictItem.java
 * 描述:   数据字典数据表
 * 创建人: zengzt
 * 创建时间:2020年5月16日下午2:36:37
 */
@GraphQLModel(fetcher = "sysDictItemService", table = "SYS_DICT_ITEM")
public class SysDictItem {

	@GraphQLField(label = "字典标识",sql = " dict = $S{dict}",field = "dict")
	private String dict;
	@GraphQLField(label = "数据键",sql = " itemkey = $S{itemkey}",field = "itemkey")
	private String itemkey;
	@GraphQLField(label = "数据值",field = "itemval")
	private String itemval;
	@GraphQLField(label = "排序",field = "itemorder")
	private String itemorder;
	@GraphQLField(label = "是否启用（0-否，1-是）",field = "ifUsing")
	private String ifUsing;
	@GraphQLField(label = "渲染样式",field = "itemrender")
	private String itemrender;
	
	public String getDict() {
		return dict;
	}
	public void setDict(String dict) {
		this.dict = dict;
	}
	public String getItemkey() {
		return itemkey;
	}
	public void setItemkey(String itemkey) {
		this.itemkey = itemkey;
	}
	public String getItemval() {
		return itemval;
	}
	public void setItemval(String itemval) {
		this.itemval = itemval;
	}
	public String getItemorder() {
		return itemorder;
	}
	public void setItemorder(String itemorder) {
		this.itemorder = itemorder;
	}
	public String getIfUsing() {
		return ifUsing;
	}
	public void setIfUsing(String ifUsing) {
		this.ifUsing = ifUsing;
	}
	public String getItemrender() {
		return itemrender;
	}
	public void setItemrender(String itemrender) {
		this.itemrender = itemrender;
	}
	
}
