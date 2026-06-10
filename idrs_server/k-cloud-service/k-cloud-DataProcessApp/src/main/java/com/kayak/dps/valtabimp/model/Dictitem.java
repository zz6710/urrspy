package com.kayak.dps.valtabimp.model;

/**
 * 数据字典表实体
 * @author zzl
 *
 */
public class Dictitem {

	private String dict;//
	private String titemkey;
	private String titemval;
	public String getDict() {
		return dict;
	}
	public void setDict(String dict) {
		this.dict = dict;
	}
	public String getTitemkey() {
		return titemkey;
	}
	public void setTitemkey(String titemkey) {
		this.titemkey = titemkey;
	}
	public String getTitemval() {
		return titemval;
	}
	public void setTitemval(String titemval) {
		this.titemval = titemval;
	}
	public Dictitem(String dict, String titemkey, String titemval) {
		super();
		this.dict = dict;
		this.titemkey = titemkey;
		this.titemval = titemval;
	}
	
	public Dictitem(){};
}
