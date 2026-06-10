package com.kayak.core.sql;

import com.kayak.core.exception.SystemException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SqlQueryTree {

	private static final Logger log = LoggerFactory.getLogger(SqlQueryTree.class);

	private String idcolumn;
	private String textcolumn;
	private String iconClscolumn;
	private String diffway;
	private String diffcondition;
	private String initexpand;
	private Boolean isasync;
	private String iconcolumn;
	private List<SqlRow> rows;

	public SqlQueryTree(String diffcondition, List<SqlRow> rows) {
		this.diffcondition = diffcondition;
		this.diffway = "upper";
		this.iconcolumn = "";
		this.isasync = false;
		this.rows = rows;
	}

	public SqlQueryTree(String diffcondition, Collection<Map<String, Object>> maps) {
		this.diffcondition = diffcondition;
		this.diffway = "upper";
		this.iconcolumn = "";
		this.isasync = false;

		rows = new ArrayList<>();// 结果记录集对象

		if (maps == null)
			return;

		// 字段信息
		for (Map<String, Object> map : maps) {
			SqlRow row = new SqlRow();
			Set<Map.Entry<String, Object>> set = map.entrySet();
			for (Map.Entry<String, Object> e : set) {
				String columnName = e.getKey();
				Object value = e.getValue();
				// 添加row记录
				row.put(columnName, value);
			}
			rows.add(row);
		}
	}

	/**
	 * 处理分级方式为'U'的构造
	 * 
	 * @param baseColumnName  基准字段名称
	 * @param upperColumnName 上级字段名称
	 * @throws KSystemException
	 */
	private void dealUpper(String baseColumnName, String upperColumnName) throws SystemException {
		List<SqlRow> removeRows = new ArrayList<SqlRow>();
		for (int line = 0; line < rows.size(); line++) {
			SqlRow currRow = rows.get(line);

			if (!currRow.containsKey(upperColumnName)) {
				throw new SystemException("构造树结果集时，diffCondition指定的上级字段名称 " + upperColumnName + " 不存在");
			}

			String upperValue = currRow.getString(upperColumnName);
			for (int i = 0; i < rows.size(); i++) {// 从遍历查找上级
				SqlRow row = rows.get(i);

				if (!currRow.containsKey(baseColumnName)) {
					throw new SystemException("构造树结果集时，diffCondition指定的基准字段名称 " + baseColumnName + " 不存在");
				}

				String baseValue = row.getString(baseColumnName);
				if (baseValue.equals(upperValue)) {// 找到上级
					row.addChild(currRow);// 将该行添加到上级的childrens中
					removeRows.add(currRow);// 保存要从sqlResult中移去的行，将在后面统一移去
					break;
				}
			}
		}
		updateSqlResult(removeRows);
	}

	private void updateSqlResult(List<SqlRow> removeRows) {
		for (int i = 0; i < rows.size(); i++) {
			SqlRow row = rows.get(i);
			if (removeRows.contains(row)) {// 从sqlResult移走需要移去的行
				rows.remove(row);
				i--;
			}
		}
	}

	private JSONArray getTreeJson(List<SqlRow> sqlRows) {
		if (sqlRows == null) {
			log.info("查询结果集sResult=null");
			return null;
		}

		if (sqlRows.size() == 0) {// 查无记录，返回空结果
			return new JSONArray();
		}

		JSONArray jsonArr = new JSONArray();

		for (SqlRow row : sqlRows) {
			JSONObject jo = new JSONObject(row);
			jsonArr.put(jo);
			// 把记录行的所有字段添加到节点的rowData属性中
//			jo.put("rowData", row);

			if (row.getChildrens() != null) {
				jo.put("children", getTreeJson(row.getChildrens()));
			}

		}

		return jsonArr;
	}

	public JSONArray getTreeJson() throws SystemException {
		// 要返回树结构结果返回，先要把结果集构造成树结构
		makeTreeResult();

		return this.getTreeJson(this.rows);
	}

	/**
	 * 把查询结果集构造成树结构结果集
	 * 
	 * @throws KSystemException
	 */
	public void makeTreeResult() throws SystemException {
		String[] diffs = this.diffcondition.split("[,]");
		if (diffs.length != 2) {
			throw new SystemException("构造树结果集时，diffCondition设置的条件内容不正确");
		}
		dealUpper(diffs[0].trim(), diffs[1].trim());
	}

	public String getIdcolumn() {
		return idcolumn;
	}

	public void setIdcolumn(String idcolumn) {
		this.idcolumn = idcolumn;
	}

	public String getTextcolumn() {
		return textcolumn;
	}

	public void setTextcolumn(String textcolumn) {
		this.textcolumn = textcolumn;
	}

	public String getIconClscolumn() {
		return iconClscolumn;
	}

	public void setIconClscolumn(String iconClscolumn) {
		this.iconClscolumn = iconClscolumn;
	}

	public String getDiffway() {
		return diffway;
	}

	public void setDiffway(String diffway) {
		this.diffway = diffway;
	}

	public String getDiffcondition() {
		return diffcondition;
	}

	public void setDiffcondition(String diffcondition) {
		this.diffcondition = diffcondition;
	}

	public String getInitexpand() {
		return initexpand;
	}

	public void setInitexpand(String initexpand) {
		this.initexpand = initexpand;
	}

	public Boolean getIsasync() {
		return isasync;
	}

	public void setIsasync(Boolean isasync) {
		this.isasync = isasync;
	}

	public String getIconcolumn() {
		return iconcolumn;
	}

	public void setIconcolumn(String iconcolumn) {
		this.iconcolumn = iconcolumn;
	}

}
