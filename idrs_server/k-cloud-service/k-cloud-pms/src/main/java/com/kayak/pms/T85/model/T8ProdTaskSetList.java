package com.kayak.pms.T85.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

import java.util.List;

/**
 * 文件名: TaProdTaskSetList.java
 * 描述:  产品清算任务配置表列表
 * 创建人: zengzt
 * 创建时间:2020年5月20日上午11:55:00
 */
@GraphQLModel(fetcher = "t8ProdTaskSetListService", table = "T8_PROD_TASK_SET")
public class T8ProdTaskSetList {

	@GraphQLField(label = "产品清算任务列表",field = "prodTaskList")
	private List<T8ProdTaskSet> prodTaskList;

	@GraphQLField(label = "产品形态",field = "prodMode" )
	private String prodMode;

	public List<T8ProdTaskSet> getProdTaskList() {
		return prodTaskList;
	}

	public void setProdTaskList(List<T8ProdTaskSet> prodTaskList) {
		this.prodTaskList = prodTaskList;
	}

	public String getProdMode() {
		return prodMode;
	}

	public void setProdMode(String prodMode) {
		this.prodMode = prodMode;
	}
	
}
