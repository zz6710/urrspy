package com.kayak.core.sql;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlRow extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = -6949629281253429688L;

	/**
	 * 是否叶子节点
	 */
	private boolean isLeaf;

	/**
	 * 是否第一条记录
	 */
	private boolean isFirst;

	/**
	 * 是否最后一条记录
	 */
	private boolean isLast;

	/**
	 * 用于树状结果集，保存子集
	 */
	private List<SqlRow> childrens;

	public Object get(Object key) {
		if (key == null)
			return null;

		return super.get(key.toString());
	}

	public Object put(String key, Object value) {
		if (key == null)
			return null;
		return super.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		for (Iterator<? extends Map.Entry<? extends String, ? extends Object>> i = m.entrySet().iterator(); i
				.hasNext();) {
			Map.Entry<? extends String, ? extends Object> e = i.next();
			put(e.getKey(), e.getValue());
		}
		// super.putAll(m);
	}

	/**
	 * 取得String字段值
	 */
	public String getString(String key) {
		return SqlUtil.obj2String(this.get(key));
	}

	/**
	 * 取得Integer字段值
	 */
	public Integer getInteger(String key) {
		return SqlUtil.obj2Integer(this.get(key));
	}

	/**
	 * 取得Short字段值
	 */
	public Short getShort(String key) {
		return SqlUtil.obj2Short(this.get(key));
	}

	/**
	 * 取得Long字段值
	 */
	public Long getLong(String key) {
		return SqlUtil.obj2Long(this.get(key));
	}

	/**
	 * 取得Double字段值
	 */
	public Double getDouble(String key) {
		return SqlUtil.obj2Double(this.get(key));
	}

	/**
	 * 取得BigDecimal字段值
	 */
	public BigDecimal getBigDecimal(String key) {
		return SqlUtil.obj2BigDecimal(this.get(key));
	}

	/**
	 * 取得Blob字段值
	 */
	public byte[] getByteArray(String key) {
		return SqlUtil.obj2ByteArray(this.get(key));
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	/**
	 * @return the isLast
	 */
	public boolean isLast() {
		return isLast;
	}

	/**
	 * @param isLast the isLast to set
	 */
	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public List<SqlRow> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<SqlRow> childrens) {
		this.childrens = childrens;
	}

	public void addChild(SqlRow row) {
		if (this.childrens == null) {
			this.childrens = new ArrayList<SqlRow>();
		}
		this.childrens.add(row);
	}

}
