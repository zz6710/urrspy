package com.kayak.pms.T85.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;


/**
 * 文件名: SysParam.java
 * 描述: 系统参数表
 * 创建人: zengzt
 * 创建时间:2020年5月23日下午5:47:43
 */
@GraphQLModel(fetcher = "sysParamService",table = "SYS_PARAM")
public class SysParam {

	@GraphQLField(label = "模块ID", field = "moduleid")
	private String moduleid;
	@GraphQLField(label = "参数ID", field = "paraid")
	private String paraid;
	@GraphQLField(label = "参数值", field = "paravalue")
	private String paravalue;
	@GraphQLField(label = "参数名称", field = "paraname")
	private String paraname;
	@GraphQLField(label = "分组ID", field = "groupparaid")
	private String groupparaid;
	@GraphQLField(label = "转换的数据字典", field = "dict")
	private String dict;
	@GraphQLField(label = "functype", field = "functype")
	private String functype;
	@GraphQLField(label = "控件配置", field = "isdisplay")
	private String isdisplay;
	@GraphQLField(label = "是否显示1-显示  其他-隐藏", field = "confoption")
	private String confoption;
	
	
	public String getModuleid() {
		return moduleid;
	}
	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}
	public String getParaid() {
		return paraid;
	}
	public void setParaid(String paraid) {
		this.paraid = paraid;
	}
	public String getParavalue() {
		return paravalue;
	}
	public void setParavalue(String paravalue) {
		this.paravalue = paravalue;
	}
	public String getParaname() {
		return paraname;
	}
	public void setParaname(String paraname) {
		this.paraname = paraname;
	}
	public String getGroupparaid() {
		return groupparaid;
	}
	public void setGroupparaid(String groupparaid) {
		this.groupparaid = groupparaid;
	}
	public String getDict() {
		return dict;
	}
	public void setDict(String dict) {
		this.dict = dict;
	}
	public String getFunctype() {
		return functype;
	}
	public void setFunctype(String functype) {
		this.functype = functype;
	}
	public String getIsdisplay() {
		return isdisplay;
	}
	public void setIsdisplay(String isdisplay) {
		this.isdisplay = isdisplay;
	}
	public String getConfoption() {
		return confoption;
	}
	public void setConfoption(String confoption) {
		this.confoption = confoption;
	}
	
}
