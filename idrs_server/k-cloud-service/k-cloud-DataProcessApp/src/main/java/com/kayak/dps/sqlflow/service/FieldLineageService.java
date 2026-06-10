package com.kayak.dps.sqlflow.service;

import com.kayak.DpsApp;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;
import com.kayak.clear.utils.Tools;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.sqlflow.dao.FieldLineageDao;
import com.kayak.dps.sqlflow.dao.RmsFieldInfoDao;
import com.kayak.dps.sqlflow.dao.RmsTableInfoDao;
import com.kayak.dps.sqlflow.dao.TableLineageDao;
import com.kayak.dps.sqlflow.model.FieldLineage;
import com.kayak.dps.sqlflow.model.RmsFieldInfo;
import com.kayak.dps.sqlflow.model.RmsTableInfo;
import com.kayak.dps.sqlflow.model.TableLineage;
import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.dlineage.DataFlowAnalyzer;
import gudusoft.gsqlparser.dlineage.dataflow.model.json.Dataflow;
import gudusoft.gsqlparser.dlineage.dataflow.model.json.Relationship;
import gudusoft.gsqlparser.dlineage.dataflow.model.json.RelationshipElement;
import gudusoft.gsqlparser.dlineage.dataflow.model.xml.dataflow;
import gudusoft.gsqlparser.dlineage.util.RemoveDataflowFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.sql.ResultSetMetaData;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 表字段血缘关系
 * @author lizs
 */
@Service
@APIDefine(desc = "表字段血缘关系服务", model = FieldLineage.class)
@Slf4j
@EnableAsync
public class FieldLineageService {

	@Autowired
	private RmsTableInfoService rmsTableInfoService;
	@Autowired
	private RmsFieldInfoService rmsFieldInfoService;
	@Autowired
	private TableLineageDao tableLineageDao;
	@Autowired
	private FieldLineageDao fieldLineageDao;
	@Autowired
	private RmsFieldInfoDao rmsFieldInfoDao;
	@Autowired
	private RmsTableInfoDao rmsTableInfoDao;
	@Autowired
	private ComnDao comnDao;
	private ResultSetMetaData metaData;

	@API(desc = "查询表字段血缘关系信息", auth = APIAuth.YES)
	public SqlResult<FieldLineage> findFieldLineages(SqlParam<FieldLineage> params) throws Exception {
		params.setMakeSql(true);
		return fieldLineageDao.findFieldLineages(params);
	}

	@API(desc = "添加表字段血缘关系", params = "to_table_info_id,to_table_field_id,from_table_info_id,from_table_field_id,all_dependency,all_superior,manual_flag,sequence", auth = APIAuth.NO)
	public int addFieldLineage(SqlParam<FieldLineage> params) throws Exception {
		return fieldLineageDao.addFieldLineage(params).getEffect();
	}

	@API(desc = "修改表字段血缘关系", params = "to_table_info_id,to_table_field_id,from_table_info_id,from_table_field_id,all_dependency,all_superior,manual_flag,sequence", auth = APIAuth.NO)
	public int updateFieldLineage(SqlParam<FieldLineage> params) throws Exception {
		return fieldLineageDao.updateFieldLineage(params).getEffect();
	}

	@API(desc = "删除表字段血缘关系", params = "to_table_info_id,to_table_field_id,from_table_info_id,from_table_field_id,all_dependency,all_superior,manual_flag,sequence", auth = APIAuth.NO)
	public int deleteFieldLineage(SqlParam<FieldLineage> params) throws Exception {
		return fieldLineageDao.deleteFieldLineage(params).getEffect();
	}

	@API(desc = "字段下游血缘关系", auth = APIAuth.NO)
	public String findDownStreamFieldLineage(SqlParam<FieldLineage> params) throws Exception {
		String tableFieldId = (String) params.getParamsDirect().get("tableFieldId"); //字段id
		if (Tools.strIsEmpty(tableFieldId)) {
			throw new PromptException("字段名不能为空");
		}
		List<FieldLineage> fieldLineageList = fieldLineageDao.findDownStreamFieldLineage(tableFieldId);
		String highLightTableName = tableFieldId.split("\\.")[1];
		Map<String, Object> data = getEdgesAndNodes(fieldLineageList, highLightTableName, null);
		return RequestSupport.updateReturnJson(true, "操作成功", data).toString();
	}

	@API(desc = "字段上游血缘关系", auth = APIAuth.NO)
	public String findUpStreamFieldLineage(SqlParam<FieldLineage> params) throws Exception {
		String tableFieldId = (String) params.getParamsDirect().get("tableFieldId"); //字段id
		if (Tools.strIsEmpty(tableFieldId)) {
			throw new PromptException("字段名不能为空");
		}
		List<FieldLineage> fieldLineageList = fieldLineageDao.findUpStreamFieldLineage(tableFieldId);
		String highLightTableName = tableFieldId.split("\\.")[1];
		Map<String, Object> data = getEdgesAndNodes(fieldLineageList, highLightTableName, null);
		return RequestSupport.updateReturnJson(true, "操作成功", data).toString();
	}

	@API(desc = "字段全血缘关系", auth = APIAuth.NO)
	public String findFieldLineage(SqlParam<FieldLineage> params) throws Exception {
		String tableFieldId = (String) params.getParamsDirect().get("tableFieldId"); //字段id
		if (Tools.strIsEmpty(tableFieldId)) {
			throw new PromptException("字段名不能为空");
		}
		Set<FieldLineage> set = new HashSet<>();
		List<FieldLineage> upStreamList = fieldLineageDao.findUpStreamFieldLineage(tableFieldId);
		List<FieldLineage> downStreamList = fieldLineageDao.findDownStreamFieldLineage(tableFieldId);
		set.addAll(upStreamList);
		set.addAll(downStreamList);
		List<FieldLineage> fieldLineageList = new ArrayList<>(set);
		String highLightTableName = tableFieldId.split("\\.")[1];
		Map<String, Object> data = getEdgesAndNodes(fieldLineageList, highLightTableName, null);
		return RequestSupport.updateReturnJson(true, "操作成功", data).toString();
	}

	/**
	 * 生成edges和nodes
	 * @param fieldLineageList 血缘关系列表
	 * @param highLightTableName 高亮表名
	 * @param mainFieldList 主表字段信息
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getEdgesAndNodes(List<FieldLineage> fieldLineageList, String highLightTableName, List<RmsFieldInfo> mainFieldList) throws Exception {
		// 生成edges
		List<Map<String, Map<String, String>>> edgeList = new ArrayList<>();
		fieldLineageList.forEach(fieldLineage -> {
			Map<String, String> fromMap = new HashMap<>(4);
			String[] fromArray = fieldLineage.getFromTableFieldId().split("\\.");
			String fromTable = fromArray[1];
			String fromField = fromArray[2];
			fromMap.put("name", fromTable);
			fromMap.put("field", fromField);
			Map<String, String> toMap = new HashMap<>(4);
			String[] toArray = fieldLineage.getToTableFieldId().split("\\.");
			String toTable = toArray[1];
			String toField = toArray[2];
			toMap.put("name", toTable);
			toMap.put("field", toField);
			Map<String, Map<String, String>> edge = new HashMap<>();
			edge.put("from", fromMap);
			edge.put("to", toMap);
			edgeList.add(edge);
		});
		// 生成nodes
		List<Map<String, Object>> nodeList = new ArrayList<>();
		Map<String, String> fieldIdMap = new LinkedHashMap<>();
		fieldLineageList.forEach(fieldLineage -> {
			fieldIdMap.put(fieldLineage.getToTableFieldId(), fieldLineage.getToFieldComment());
			fieldIdMap.put(fieldLineage.getFromTableFieldId(), fieldLineage.getFromFieldComment());
		});
		Map<String, List<Map<String, String>>> map = fieldIdMap.entrySet().stream()
				.collect(Collectors.groupingBy(entry -> entry.getKey().split("\\.")[1],
						Collectors.mapping(entry -> {
							Map<String, String> field = new HashMap<>();
							field.put("name", entry.getKey().split("\\.")[2]);
							field.put("comment", entry.getValue());
							return field;
						}, Collectors.toList())));
		map.forEach((tableName, fieldList) -> {
			Map<String, Object> node = new HashMap<>();
			node.put("name", tableName);
			if (tableName.equals(highLightTableName)) {
				node.put("type", "HighLight");
				if (mainFieldList != null) {
					// 主表显示所有字段
					fieldList = mainFieldList.stream().map(field -> {
						Map<String, String> fieldMap = new HashMap<>();
						fieldMap.put("name", field.getFieldName());
						fieldMap.put("comment", field.getFieldComment());
						return fieldMap;
					}).collect(Collectors.toList());
				}
			} else {
				node.put("type", "Origin");
			}
			node.put("fields", fieldList);
			nodeList.add(node);
		});
		addTableName(nodeList);

        addPosition(edgeList, nodeList, highLightTableName);

        Map<String, Object> data = new HashMap<>(2);
		data.put("edges", edgeList);
		data.put("nodes", nodeList);
		return data;
	}

	/**
	 * 计算top和left给前端
	 * @param edgeList
	 * @param nodeList
	 * @param highLightTableName
	 */
	private void addPosition(List<Map<String, Map<String, String>>> edgeList, List<Map<String, Object>> nodeList, String highLightTableName) {
		if (nodeList.isEmpty()) {
			return ;
		}
		Map<String, Integer> fieldCount = new HashMap<>();//表字段数量
		Map<String, Set<String>> leftNodes = new HashMap<>();//字段的左节点列表
		Map<String, Set<String>> rightNodes = new HashMap<>();//字段的右节点列表
		Map<String, List<String>> tableSort = new HashMap<>();
		Set<String> allTableSet = new HashSet<>();
		int start = 0;
		nodeList.forEach(node -> {
			List<Map<String, String>> fields = (List) node.get("fields");
			fieldCount.put((String) node.get("name"), fields.size());
		});
		edgeList.forEach(edge -> {
			String fromName = edge.get("from").get("name");
			String toName = edge.get("to").get("name");
			Set<String> toNameSet = rightNodes.containsKey(fromName) ? rightNodes.get(fromName) : new HashSet<>();
			toNameSet.add(toName);
			rightNodes.put(fromName, toNameSet);
			Set<String> fromNameSet = leftNodes.containsKey(toName) ? leftNodes.get(toName) : new HashSet<>();
			fromNameSet.add(fromName);
			leftNodes.put(toName, fromNameSet);
		});
		// 所有表从左到右排序
		List<String> tableList = new ArrayList<>();
		tableList.add(highLightTableName);
		tableSort.put(Integer.toString(start), tableList);
		int min = start;
		for (List<String> current = tableSort.get(Integer.toString(min)); !current.isEmpty(); min--, current = tableSort.get(Integer.toString(min))) {
			int index = min - 1;
			List<String> tables = tableSort.containsKey(Integer.toString(index)) ? tableSort.get(Integer.toString(index)) : new ArrayList<>();
			current.forEach(name -> {
				Set<String> leftArr = leftNodes.get(name);
				if (leftArr != null) {
					leftArr.forEach(tableName -> {
						if (!allTableSet.contains(tableName)) {
							allTableSet.add(tableName);
							tables.add(tableName);
						}
					});
				}
			});
			tableSort.put(Integer.toString(index), tables);
		}
		int max = start;
		for (List<String> nameArr = tableSort.get(Integer.toString(max)); !nameArr.isEmpty(); max++, nameArr = tableSort.get(Integer.toString(max))) {
			int index = max + 1;
			List<String> tables = tableSort.containsKey(Integer.toString(index)) ? tableSort.get(Integer.toString(index)) : new ArrayList<>();
			nameArr.forEach(name -> {
				Set<String> rightArr = rightNodes.get(name);
				if (rightArr != null) {
					for (String tableName : rightArr) {
						if (!allTableSet.contains(tableName)) {
							allTableSet.add(tableName);
							tables.add(tableName);
						}
					}
				}
			});
			tableSort.put(Integer.toString(index), tables);
		}
		// 根据 tableSort 计算top和left
		int offsetTop = 0; //全局top偏移
		int offsetLeft = 0; //全局left偏移
		int columnSpacing = 300; //表左右间距
		int rowSpacing = 50; //表上下间距
		int fieldHeight = 30; //字段高度
		Map<String, Integer> nameTop = new HashMap<>(); //存表的top值
		Map<String, Integer> nameLeft = new HashMap<>(); //存表的left值
		for (int i = min + 1, columnNo = 1; i <= max - 1; i++, columnNo++) {
			List<String> nameArr = tableSort.get(Integer.toString(i));
			int rowTopOffset = 0;
			for (String name : nameArr) {
				if (nameTop.containsKey(name)) {//一张表多次出现则跳过后面出现的
					continue;
				}
				nameTop.put(name, rowTopOffset + offsetTop);
				nameLeft.put(name, (columnNo - 1) * columnSpacing + offsetLeft);
				rowTopOffset += (fieldCount.get(name) + 2) * fieldHeight + rowSpacing;
			}
		}
		// 更新nodeList
		nodeList.forEach(node -> {
			node.put("top", nameTop.get(node.get("name")));
			node.put("left", nameLeft.get(node.get("name")));
		});
	}

	private void addTableName(List<Map<String, Object>> nodeList) throws Exception {
		String tablesString = nodeList.stream().map(node -> (String) node.get("name")).collect(Collectors.joining(","));
		List<RmsTableInfo> tableInfoList = rmsTableInfoDao.findRmsTableInfoByTableName(tablesString);
		nodeList.forEach(node -> tableInfoList.forEach(tableInfo -> {
			if (node.get("name").equals(tableInfo.getTableName())) {
				node.put("comment", tableInfo.getComment());
			}
		}));
	}

	@Value("${database.schemas}")
	private String databases;
	private static final String REGEX = "\\$([A-Za-z0-9_]+)\\{([A-Za-z0-9_]+)}";
	private static final String REPLACE = "''";

	/**
	 * 解析sql血缘关系
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Async
	public void parseSql(SqlParam<FieldLineage> params) throws Exception {
		fieldLineageDao.setSystemParamsByParaid("90000051111", "1"); //设置执行状态
		long startTime = System.currentTimeMillis();
		List<String> errorExeIdList = new ArrayList<>();
		// 获取数据库名称
		if (Tools.strIsEmpty(databases)) {
			throw new PromptException("database.schemas未配置");
		}
		// 删除所有血缘关系
		rmsTableInfoDao.truncateRmsTableInfo();
		rmsFieldInfoDao.truncateRmsFieldInfo();

		//String database = databases.split(",")[0];
		for (String database : databases.split(",")) {
			List<SqlRow> sqlResult = rmsTableInfoDao.getDatabaseName(database);
			if (sqlResult.isEmpty()) {
				throw new PromptException("数据库不存在！");
			}
			// database = sqlResult.get(0).getString("TABLE_SCHEMA");
			// 更新表和字段信息
			rmsTableInfoDao.insertRmsTableInfoFromSchema(database);
			rmsFieldInfoDao.insertRmsFieldInfoFromSchema(database);

		}
		try {
			String database = databases.split(",")[0];// 二维报表只处理主库
			rmsFieldInfoDao.deleteTwoDimensionalFiledInfo(database); //删除二维报表字段信息
			rmsFieldInfoDao.insertTwoDimensionalFiledInfo(database); //插入二维报表字段信息
			// 删除非手工维护的血缘关系
			tableLineageDao.deleteTableLineage();
			fieldLineageDao.deleteFieldLineage();
			// 查询需要解析的sql
			List<SqlRow> rows = fieldLineageDao.findAllSql();
			Pattern pattern = Pattern.compile(REGEX);
			Set<String> tableLineageSet = initTableLineageSet(20480); //表血缘关系去重set
			Set<String> fieldLineageSet = initFieldLineageSet(40960); //字段血缘关系去重set
			for (SqlRow row : rows) {
				String sql = row.getString("sqlstr");
				String exeId = row.getString("exeid");
				String coordinateType = row.getString("coordinate_type");
				if (Tools.strIsNotEmpty(sql)) {
					try{
						// 替换sql中的$S{}占位符
						Matcher matcher = pattern.matcher(sql);
						sql = matcher.replaceAll(REPLACE);
						// 二维报表sql需要预处理
						if ("2".equals(coordinateType)) {
							sql = initTwoDimensionalSql(row, sql);
						}
						// gsqlparser解析sql
						Dataflow flow = getSqlDataflow(sql);
						if (flow == null || flow.getRelationships().length == 0) {
							errorExeIdList.add(exeId+ "[" + sql + "]");
							continue;
						}
						// 保存血缘关系
						saveTableFieldLineage(flow, exeId, sql, tableLineageSet, fieldLineageSet);

					} catch (Exception e){
						errorExeIdList.add(exeId+ "[" + sql + "]");
						e.printStackTrace();
					}
				}
			}

			// 更新表任务上下游关系
			for(RmsTableInfo rmsTableInfo : rmsTableInfoDao.findAllRmsTableInfos()){
				rmsTableInfoDao.updateTableInfoTasks(rmsTableInfo.getTableInfoId(),
						rmsTableInfoDao.findUpStreamTasks(rmsTableInfo.getDatabaseName(), rmsTableInfo.getTableInfoId()).get(0).getUpTasks(),
						rmsTableInfoDao.findDownStreamTasks(rmsTableInfo.getDatabaseName(), rmsTableInfo.getTableInfoId()).get(0).getDownTasks());
			}

		} catch (Exception e) {
			fieldLineageDao.setSystemParamsByParaid("90000051111", "0");
			e.printStackTrace();
			throw new PromptException(e.getMessage());
		}
		fieldLineageDao.setSystemParamsByParaid("90000051111", "0");
		log.info("##### 生成血缘关系执行耗时: {} ms, 无法解析的exeid： {}", System.currentTimeMillis() - startTime, errorExeIdList);
	}

	/**
	 * 保存血缘关系
	 * @param dataflow
	 * @param exeId
	 * @param sql
	 * @param tableLineageSet
	 * @param fieldLineageSet
	 * @throws Exception
	 */
	private void saveTableFieldLineage(Dataflow dataflow, String exeId, String sql, Set<String> tableLineageSet, Set<String> fieldLineageSet) throws Exception {
		//默认第一个数据库为当主数据库，其他数据库在sql中需要带数据库名
		String database = databases.split(",")[0];
		String toTableInfoId = null; //目标表id
		String toTableFieldId = null; //目标字段id
		Set<String> fromTableInfoIdSet = new HashSet<>(); //源表set

		for (Relationship relationship : dataflow.getRelationships()) {
			String toTableName = formatValue(relationship.getTarget().getParentName());
			String toFieldName = formatValue(relationship.getTarget().getColumn());
			if(toTableName.indexOf(".") > 0){//表名带库名时使用语句中的库名
				toTableInfoId = toTableName; //目标表id
				toTableFieldId = getFullName(toTableName, toFieldName); //目标字段id
			}else{
				toTableInfoId = getFullName(database, toTableName); //目标表id
				toTableFieldId = getFullName(database, toTableName, toFieldName); //目标字段id
			}
			// 一个目标字段可由多个源字段加工而成
			for (RelationshipElement source : relationship.getSources()) {
				String fromTableInfoId = null; //源表id
				String fromTableFieldId = null; //源字段id
				String fromTableName = formatValue(source.getParentName());
				// count(1)特殊处理
				String fromColumnName = "countFunction".equals(source.getColumn()) ? "count(1)" : formatValue(source.getColumn());

				if(fromTableName.indexOf(".") > 0){//表名带库名时使用语句中的库名
					rmsFieldInfoDao.addFiledInfo(fromTableName.substring(0,fromTableName.indexOf(".")), fromTableName.substring(fromTableName.indexOf(".")+1), "count(1)", "合计");
					fromTableInfoId = fromTableName;//源表id
					fromTableFieldId = getFullName(fromTableName, fromColumnName);//源字段id
				}else{
					rmsFieldInfoDao.addFiledInfo(database, fromTableName, "count(1)", "合计");
					fromTableInfoId = getFullName(database, fromTableName);//源表id
					fromTableFieldId = getFullName(database, fromTableName, fromColumnName);//源字段id
				}

				fromTableInfoIdSet.add(fromTableInfoId);

				// 处理字段血缘关系
				saveFieldLineage(toTableInfoId, toTableFieldId, fromTableInfoId,fromTableFieldId,fieldLineageSet);

				// 特殊处理，只要表字段存在关系就打标记
				/*String fromTableFieldId2 = getFullName(fromTableInfoId, "@!ControlOrOther");//源字段id
				String toTableFieldId2 = getFullName(toTableInfoId, "@!ControlOrOther"); //目标字段id
				rmsFieldInfoDao.addFiledInfo(fromTableInfoId.substring(0,fromTableInfoId.indexOf(".")), fromTableInfoId.substring(fromTableInfoId.indexOf(".")+1), "@!ControlOrOther", "@!条件控制或其他关系");
				rmsFieldInfoDao.addFiledInfo(toTableInfoId.substring(0,toTableInfoId.indexOf(".")), toTableInfoId.substring(toTableInfoId.indexOf(".")+1), "@!ControlOrOther", "@!条件控制或其他关系");
				saveFieldLineage(toTableInfoId, toTableFieldId2, fromTableInfoId, fromTableFieldId2, fieldLineageSet);*/

			}
		}


		// 保存表血缘关系
		TableLineage tableLineage = new TableLineage();
		tableLineage.setManualFlag("0");
		for (String fromTableInfoId : fromTableInfoIdSet) {
			String uniqueKey = exeId + "-" + toTableInfoId + "-" + fromTableInfoId;
			if (tableLineageSet.contains(uniqueKey)) {
				continue;
			}
			tableLineage.setExeid(exeId);
			tableLineage.setToTableInfoId(toTableInfoId);
			tableLineage.setFromTableInfoId(fromTableInfoId);
			tableLineageDao.addTableLineage(tableLineage);
			tableLineageSet.add(uniqueKey);
		}

		// 补充血缘关系，数据加工逻辑中涉及到的表（没有直接的数据关系，但使用到条件控制等关系的表）
		/*List<RmsTableInfo> tableInfoList = rmsTableInfoDao.findRmsTableInfoIdByExeSql(database, sql);
		for (RmsTableInfo tableInfo : tableInfoList) {
			String fromTableInfoId = null; //源表id
			String fromTableFieldId = null; //源字段id
			String fromFieldName = "@!ControlOrOther";
			fromTableInfoId = tableInfo.getTableInfoId();
			fromTableFieldId = getFullName(fromTableInfoId, fromFieldName);//源字段id

			String toFieldName = "@!ControlOrOther";
			toTableFieldId = getFullName(toTableInfoId, toFieldName); //目标字段id

			String uniqueKey = exeId + "-" + toTableInfoId + "-" + fromTableInfoId;
			if (!tableLineageSet.contains(uniqueKey) && !toTableInfoId.equals(fromTableInfoId)) {
				rmsFieldInfoDao.addFiledInfo(fromTableInfoId.substring(0,fromTableInfoId.indexOf(".")), fromTableInfoId.substring(fromTableInfoId.indexOf(".")+1), fromFieldName, "@!条件控制或其他关系");
				rmsFieldInfoDao.addFiledInfo(toTableInfoId.substring(0,toTableInfoId.indexOf(".")), toTableInfoId.substring(toTableInfoId.indexOf(".")+1), toFieldName, "@!条件控制或其他关系");
				// 处理字段血缘关系
				saveFieldLineage(toTableInfoId, toTableFieldId, fromTableInfoId, fromTableFieldId, fieldLineageSet);

				tableLineage.setExeid(exeId);
				tableLineage.setToTableInfoId(toTableInfoId);
				tableLineage.setFromTableInfoId(fromTableInfoId);
				tableLineageDao.addTableLineage(tableLineage);
				tableLineageSet.add(uniqueKey);
			}
		}*/
	}

	/**
	 *
	 * @param toTableInfoId 目标表
	 * @param toTableFieldId 目标字段
	 * @param fromTableInfoId 源表
	 * @param fromTableFieldId 源字段
	 * @param fieldLineageSet 字段血缘关系去重set
	 */
	private void saveFieldLineage(String toTableInfoId,String toTableFieldId,String fromTableInfoId,String fromTableFieldId,Set<String> fieldLineageSet) throws Exception{
		FieldLineage fieldLineage = new FieldLineage();
		fieldLineage.setManualFlag("0"); //0系统1手工

		// 字段血缘关系set去重，减少查库
		String key = toTableFieldId + "-" + fromTableFieldId;
		if (fieldLineageSet.contains(key)) {
			return;
		}

		// 保存字段血缘关系
		fieldLineage.setToTableInfoId(toTableInfoId);
		fieldLineage.setToTableFieldId(toTableFieldId);
		fieldLineage.setFromTableInfoId(fromTableInfoId);
		fieldLineage.setFromTableFieldId(fromTableFieldId);
		String childAllDependency = fieldLineageDao.findChildAllDependency(fromTableFieldId); // 查询源字段对应的all_dependency
		String allDependency = (childAllDependency == null) ? format(toTableFieldId) + format(fromTableFieldId)
				: format(toTableFieldId) + childAllDependency;
		fieldLineage.setAllDependency(removeDuplicates(allDependency));
		String childAllSuperior = fieldLineageDao.findChildAllSuperior(toTableFieldId); // 查询目标字段对应的all_superior
		String allSuperior = (childAllSuperior == null) ? format(fromTableFieldId) + format(toTableFieldId)
				: format(fromTableFieldId) + childAllSuperior;
		fieldLineage.setAllSuperior(removeDuplicates(allSuperior));
		fieldLineage.setSequence(String.valueOf(fieldLineageSet.size() + 1)); //生成的次序，便于排查
		fieldLineageDao.addFieldLineage(fieldLineage);

		// 加入字段血缘关系set
		fieldLineageSet.add(key);

		// 更新字段血缘关系表中的all_dependency
		List<FieldLineage> updateList = fieldLineageDao.findUpdateAllDependencyList(toTableFieldId);
		for (FieldLineage updateFieldLineage : updateList) {
			String oldAllDependency = updateFieldLineage.getAllDependency();
			String newAllDependency = (childAllDependency == null) ? oldAllDependency + format(fromTableFieldId)
					: oldAllDependency + childAllDependency;
			updateFieldLineage.setAllDependency(removeDuplicates(newAllDependency));
			fieldLineageDao.updateAllDependency(updateFieldLineage);
		}

		// 更新字段血缘关系表中的all_superior
		List<FieldLineage> list = fieldLineageDao.findUpdateAllSuperiorList(fromTableFieldId);
		for (FieldLineage updateFieldLineage : list) {
			String oldAllSuperior = updateFieldLineage.getAllSuperior();
			String newAllSuperior = (childAllSuperior == null) ? oldAllSuperior + format(toTableFieldId)
					: oldAllSuperior + childAllSuperior;
			updateFieldLineage.setAllSuperior(removeDuplicates(newAllSuperior));
			fieldLineageDao.updateAllSuperior(updateFieldLineage);
		}
	}

	// 使用mysql语法解析获取Dataflow
	private Dataflow getSqlDataflow(String sql) {
		DataFlowAnalyzer dlineage = new DataFlowAnalyzer(sql, EDbVendor.dbvmysql, true);
		dlineage.setSqlEnv(null);
		dlineage.setShowJoin(true);
		dlineage.setIgnoreRecordSet(true);
		dlineage.setLinkOrphanColumnToFirstTable(false);
		dlineage.setTextFormat(false);
		dlineage.setTransform(true);// 设置解析关系
		dlineage.setTransformCoordinate(false);// 设置忽略关系坐标
		dlineage.setShowCallRelation(true); // 设置调用关系
		dlineage.setIgnoreCoordinate(true);// 设置忽略坐标
		dlineage.setShowImplicitSchema(true);
		dlineage.generateDataFlow(); // 关系初始化
		dlineage.setShowConstantTable(true);
		dataflow fieldDataFlow = dlineage.getDataFlow();// 获取字段级别关系对象
		//dataflow dataflow = ProcessUtility.generateTableLevelLineage(dlineage, fieldDataFlow);// 获取表级别关系对象
		dataflow dataflow = new RemoveDataflowFunction().removeFunction(fieldDataFlow, EDbVendor.dbvmysql);// 使用mysql语法解析
		return DataFlowAnalyzer.getSqlflowJSONModel(dataflow, EDbVendor.dbvmysql);
	}

	public static void main(String[] args) throws Exception{

		String sql = "insert into app_rpt_g06_02 (lie1) select c1 from (select c1,row_index from (\n" +
				"with dwd_ast_prd_ast_lbl_pos_dtl_w as (\n" +
				"select (ifnull(PRCP_BAL,0) + ifnull(FAIR_VAL,0) + ifnull(AMRZ_CST_BAL,0) + ifnull(ACR_INTR_BAL,0))/10000 as AMT_WEIGH, SCR_ID, POS_DT, BRED_CD\n" +
				"from dwd_ast_prd_ast_lbl_pos_dtl where POS_DT='' and BRED_CD in ('2','4','5','8','9','10')/*债券基金等*/\n" +
				"union all\n" +
				"select (ifnull(PRCP_BAL,0) + ifnull(ACR_INTR_BAL,0))/10000 as AMT_WEIGH, SCR_ID, POS_DT, BRED_CD from dwd_ast_prd_ast_lbl_pos_dtl where POS_DT='' and  BRED_CD not in ('2','4','5','8','9','10')/*其他*/\n" +
				"),\n" +
				"fb as (\n" +
				"select w.AMT_WEIGH,w.BRED_CD,b.CBND_SCD_CTG\n" +
				"  from dwd_ast_prd_ast_lbl_pos_dtl_w w\n" +
				"  join DWD_AST_NSTD_AST_INF b on w.SCR_ID=b.SCR_ID\n" +
				"where w.POS_DT='' and  w.BRED_CD=5/*非标*/ and (b.CHN_SCR_ID is null or CHN_SCR_ID='')/*非标去掉通道*/),\n" +
				"zgjh as (\n" +
				"select w.AMT_WEIGH,w.BRED_CD,b.CBND_SCD_CTG,b.MNG_MTH from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_ast_mng_plan_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=10/*资产管理产品*/\n" +
				"union all\n" +
				"select  w.AMT_WEIGH,w.BRED_CD,b.CBND_SCD_CTG,b.MNG_MTH from  dwd_ast_prd_ast_lbl_pos_dtl_w w\n" +
				"\tjoin DWD_AST_NSTD_AST_INF a on w.SCR_ID=a.SCR_ID /*非标*/\n" +
				"\tjoin dwd_ast_ast_mng_plan_inf b on a.CHN_SCR_ID=b.SCR_ID /*通道*/  where w.POS_DT='' and w.BRED_CD=5\n" +
				")\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 1 row_index  from  dwd_ast_prd_ast_lbl_pos_dtl_w w where w.POS_DT='' and  w.BRED_CD=1/*现金*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 6 row_index/*合计*/  from  dwd_ast_prd_ast_lbl_pos_dtl_w w where w.POS_DT='' and  w.BRED_CD=1/*现金*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 7 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_bnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=4/*债券*/ and  b.G06_TYPE='121'/*本行发行的同业存单*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 8 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_bnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=4/*债券*/ and  b.G06_TYPE='122'/*他行发行的同业存单*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 9 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_bnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=4/*债券*/ and  b.G06_TYPE in ('121','122')/*同业存单合计*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 11 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w  where w.POS_DT='' and  w.BRED_CD=3/*买入返售*/ /*债券买入返售*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 12 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w where w.POS_DT='' and  w.BRED_CD=3/*买入返售*/ /*合计*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 13 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_bnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=4/*债券*/ and  b.G06_TYPE='141'/*1.4.1 国债*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 14 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_bnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=4/*债券*/ and  b.G06_TYPE='142'/*1.4.2 地方政府债券*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 16 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_bnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=4/*债券*/ and  b.G06_TYPE='143'/*1.4.3 中央银行票据*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 17 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_bnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=4/*债券*/ and  b.G06_TYPE='144'/*1.4.4 政府机构债券*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 18 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_bnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=4/*债券*/ and  b.G06_TYPE='145'/*1.4.5 政策性金融债券*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 19 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_bnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=4/*债券*/ and  b.G06_TYPE='146'/*1.4.6 商业性金融债券*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 20 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_bnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=4/*债券*/ and  b.G06_TYPE='147'/*1.4.7 企业债券*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 21 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_bnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=4/*债券*/ and  b.G06_TYPE='148'/*1.4.8 公司债券*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 22 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_bnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=4/*债券*/ and  b.G06_TYPE='149'/*1.4.9 企业债务融资工具*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 23 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_bnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=4/*债券*/ and  b.G06_TYPE='1410'/*1.4.10 资产支持证券*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 24 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_bnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=4/*债券*/ and  b.G06_TYPE='1411'/*1.4.11外国债券（不含QDII债券）*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 26 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_bnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=4/*债券*/ and  b.G06_TYPE not in ('121','122')/*债券合计*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 27 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w\n" +
				"\t\t\tleft join dwd_ast_bnd_bas_inf b on w.SCR_ID=b.SCR_ID\n" +
				"\t\t\t\tleft join DWS_EVT_BND_CUR_RAT r on b.SCR_ID=r.SCR_ID and r.SETTLE_DATE = '' where w.POS_DT='' and  w.BRED_CD=4/*债券*/ and  b.G06_TYPE not in ('121','122','141','142','143','145')/*信用债券*/ and r.BND_CRD_RAT in ('01','02','03','04','10') /*AA+（含）以上信用债券*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 28 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w\n" +
				"\t\t\tleft join dwd_ast_bnd_bas_inf b on w.SCR_ID=b.SCR_ID\n" +
				"\t\t\t\tleft join DWS_EVT_BND_CUR_RAT r on b.SCR_ID=r.SCR_ID and r.SETTLE_DATE = '' where w.POS_DT='' and  w.BRED_CD=4/*债券*/ and  b.G06_TYPE not in ('121','122','141','142','143','145')/*信用债券*/ and r.BND_CRD_RAT>'04' and r.BND_CRD_RAT <> '10' and r.BND_CRD_RAT<>'99'  /*AA+以下信用债券*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 29 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w\n" +
				"\t\t\tleft join dwd_ast_bnd_bas_inf b on w.SCR_ID=b.SCR_ID\n" +
				"\t\t\t\tleft join DWS_EVT_BND_CUR_RAT r on b.SCR_ID=r.SCR_ID and r.SETTLE_DATE = '' where w.POS_DT='' and  w.BRED_CD=4/*债券*/ and  b.G06_TYPE not in ('121','122','141','142','143','145')/*信用债券*/ and r.BND_CRD_RAT='99' /*无评级信用债券*/\n" +
				"union all\n" +
				"select ifnull(sum(fb.AMT_WEIGH),0) c1, 30 row_index from  fb where fb.CBND_SCD_CTG='1201'/*1.5.1 票据类*/\n" +
				"union all\n" +
				"select ifnull(sum(fb.AMT_WEIGH),0) c1, 32 row_index from  fb where  fb.CBND_SCD_CTG='1210'/*1.5.2 信用证*/\n" +
				"union all\n" +
				"select ifnull(sum(fb.AMT_WEIGH),0) c1, 33 row_index from  fb where  fb.CBND_SCD_CTG='1202'/*1.5.3 信托贷款*/\n" +
				"union all\n" +
				"select ifnull(sum(fb.AMT_WEIGH),0) c1, 34 row_index from  fb where  fb.CBND_SCD_CTG='1203'/*1.5.4 委托贷款*/\n" +
				"union all\n" +
				"select ifnull(sum(fb.AMT_WEIGH),0) c1, 35 row_index from  fb where  fb.CBND_SCD_CTG='1204'/*1.5.5 信贷资产转让*/\n" +
				"union all\n" +
				"select ifnull(sum(fb.AMT_WEIGH),0) c1, 36 row_index from  fb where  fb.CBND_SCD_CTG='1205'/*1.5.6 收/受益权*/\n" +
				"union all\n" +
				"select ifnull(sum(fb.AMT_WEIGH),0) c1, 37 row_index from  fb where  fb.CBND_SCD_CTG='1206'/*1.5.7 委托债权*/\n" +
				"union all\n" +
				"select ifnull(sum(fb.AMT_WEIGH),0) c1, 38 row_index from  fb where  fb.CBND_SCD_CTG='1207'/*1.5.8 应收账款*/\n" +
				"union all\n" +
				"select ifnull(sum(fb.AMT_WEIGH),0) c1, 39 row_index from  fb where  fb.CBND_SCD_CTG='1208'/*1.5.9 带回购条款的股权性融资*/\n" +
				"union all\n" +
				"select ifnull(sum(fb.AMT_WEIGH),0) c1, 40 row_index from  fb where  fb.CBND_SCD_CTG='1209'/*1.5.10 债权融资类产品*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 41 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w  where w.POS_DT='' and  w.BRED_CD=2/*同业借款*/ /*1.5.11 同业借款*/\n" +
				"union all\n" +
				"select ifnull(sum(fb.AMT_WEIGH),0) c1, 42 row_index from  fb where  fb.CBND_SCD_CTG='1212'/*1.5.12 收益凭证*/\n" +
				"union all\n" +
				"select ifnull(sum(fb.AMT_WEIGH),0) c1, 43 row_index from  fb where  fb.CBND_SCD_CTG='1213'/*1.5.13 债权投资计划和资产支持计划*/\n" +
				"union all\n" +
				"select ifnull(sum(fb.AMT_WEIGH),0) c1, 44 row_index from  fb where  fb.CBND_SCD_CTG='2101'/*1.5.14 理财直接融资工具*/\n" +
				"union all\n" +
				"select ifnull(sum(fb.AMT_WEIGH),0) c1, 45 row_index from  fb where  fb.CBND_SCD_CTG='2202'/*1.5.15 信贷资产流转和收益权转让产品*/\n" +
				"union all\n" +
				"select ifnull(sum(fb.AMT_WEIGH),0) c1, 46 row_index from  fb where  fb.CBND_SCD_CTG='1299'/*1.5.16 其他非标准化债权类投资*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 49 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join DWD_AST_NSTD_AST_INF b on w.SCR_ID=b.SCR_ID  where w.POS_DT='' and  (w.BRED_CD=5\n" +
				"and (b.CHN_SCR_ID is null or CHN_SCR_ID='')/*非标去掉通道*/ or w.BRED_CD=2/*同业借款*/) /*非标合计*/\n" +
				"\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 86 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_fnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=8/*公募基金*/ and  b.CBND_SCD_CTG='1106'/*债券基金*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 87 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_fnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=8/*公募基金*/ and  b.CBND_SCD_CTG='2401'/*货币市场基金*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 88 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_fnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=8/*公募基金*/ and  b.CBND_SCD_CTG='1303'/*股票基金*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 89 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_fnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=8/*公募基金*/ and  b.CBND_SCD_CTG='2403'/*基金中基金*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 90 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w join dwd_ast_fnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=8/*公募基金*/ and  b.CBND_SCD_CTG='2402'/*混合基金*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 91 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w left join dwd_ast_fnd_bas_inf b on w.SCR_ID=b.SCR_ID where w.POS_DT='' and  w.BRED_CD=8/*公募基金*/ and  (b.CBND_SCD_CTG is null or b.CBND_SCD_CTG not in ('1106','2401','1303','2403','2402'))/*其他公募基金*/\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 92 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w where w.POS_DT='' and  w.BRED_CD=8/*公募基金*/ /*公募基金合计*/\n" +
				"\n" +
				"union all\n" +
				"select ifnull(sum(zgjh.AMT_WEIGH),0) c1, 113 row_index from  zgjh  where zgjh.CBND_SCD_CTG='1701'/*信托产品*/\n" +
				"union all\n" +
				"select ifnull(sum(zgjh.AMT_WEIGH),0) c1, 114 row_index from  zgjh  where zgjh.CBND_SCD_CTG='1701'/*信托产品*/ and (zgjh.MNG_MTH='01' or zgjh.MNG_MTH ='' or zgjh.MNG_MTH is null) /*主动(自主管理)*/\n" +
				"union all\n" +
				"select ifnull(sum(zgjh.AMT_WEIGH),0) c1, 115 row_index from  zgjh  where zgjh.CBND_SCD_CTG='1701'/*信托产品*/ and zgjh.MNG_MTH='02' /*被动(委托管理) */\n" +
				"\n" +
				"union all\n" +
				"select ifnull(sum(zgjh.AMT_WEIGH),0) c1, 116 row_index from  zgjh  where zgjh.CBND_SCD_CTG='1703'/*券商资产管理产品*/\n" +
				"union all\n" +
				"select ifnull(sum(zgjh.AMT_WEIGH),0) c1, 117 row_index from  zgjh  where zgjh.CBND_SCD_CTG='1703'/*券商资产管理产品*/ and (zgjh.MNG_MTH='01' or zgjh.MNG_MTH ='' or zgjh.MNG_MTH is null) /*主动(自主管理)*/\n" +
				"union all\n" +
				"select ifnull(sum(zgjh.AMT_WEIGH),0) c1, 118 row_index from  zgjh  where zgjh.CBND_SCD_CTG='1703'/*券商资产管理产品*/ and zgjh.MNG_MTH='02' /*被动(委托管理) */\n" +
				"\n" +
				"union all\n" +
				"select ifnull(sum(zgjh.AMT_WEIGH),0) c1, 119 row_index from  zgjh  where zgjh.CBND_SCD_CTG='1704'/*基金资产管理产品*/\n" +
				"union all\n" +
				"select ifnull(sum(zgjh.AMT_WEIGH),0) c1, 120 row_index from  zgjh  where zgjh.CBND_SCD_CTG='1704'/*基金资产管理产品*/ and (zgjh.MNG_MTH='01' or zgjh.MNG_MTH ='' or zgjh.MNG_MTH is null) /*主动(自主管理)*/\n" +
				"union all\n" +
				"select ifnull(sum(zgjh.AMT_WEIGH),0) c1, 121 row_index from  zgjh  where zgjh.CBND_SCD_CTG='1704'/*基金资产管理产品*/ and zgjh.MNG_MTH='02' /*被动(委托管理) */\n" +
				"\n" +
				"union all\n" +
				"select ifnull(sum(zgjh.AMT_WEIGH),0) c1, 122 row_index from  zgjh  where zgjh.CBND_SCD_CTG='1706'/*期货资产管理产品*/\n" +
				"union all\n" +
				"select ifnull(sum(zgjh.AMT_WEIGH),0) c1, 123 row_index from  zgjh  where zgjh.CBND_SCD_CTG='1706'/*期货资产管理产品*/ and (zgjh.MNG_MTH='01' or zgjh.MNG_MTH ='' or zgjh.MNG_MTH is null) /*主动(自主管理)*/\n" +
				"union all\n" +
				"select ifnull(sum(zgjh.AMT_WEIGH),0) c1, 124 row_index from  zgjh  where zgjh.CBND_SCD_CTG='1706'/*期货资产管理产品*/ and zgjh.MNG_MTH='02' /*被动(委托管理) */\n" +
				"\n" +
				"union all\n" +
				"select ifnull(sum(zgjh.AMT_WEIGH),0) c1, 125 row_index from  zgjh  where zgjh.CBND_SCD_CTG='1702'/*保险资产管理产品*/\n" +
				"union all\n" +
				"select ifnull(sum(zgjh.AMT_WEIGH),0) c1, 126 row_index from  zgjh  where zgjh.CBND_SCD_CTG='1702'/*保险资产管理产品*/ and (zgjh.MNG_MTH='01' or zgjh.MNG_MTH ='' or zgjh.MNG_MTH is null) /*主动(自主管理)*/\n" +
				"union all\n" +
				"select ifnull(sum(zgjh.AMT_WEIGH),0) c1, 127 row_index from  zgjh  where zgjh.CBND_SCD_CTG='1702'/*保险资产管理产品*/ and zgjh.MNG_MTH='02' /*被动(委托管理) */\n" +
				"\n" +
				"union all\n" +
				"select ifnull(sum(zgjh.AMT_WEIGH),0) c1, 128 row_index from  zgjh  where (zgjh.CBND_SCD_CTG is null or zgjh.CBND_SCD_CTG not in('1701','1703','1704','1706','1702'))/*其他资产管理产品*/\n" +
				"union all\n" +
				"select ifnull(sum(zgjh.AMT_WEIGH),0) c1, 129 row_index from  zgjh  where (zgjh.CBND_SCD_CTG is null or zgjh.CBND_SCD_CTG not in('1701','1703','1704','1706','1702'))/*其他资产管理产品*/ and (zgjh.MNG_MTH='01' or zgjh.MNG_MTH ='' or zgjh.MNG_MTH is null) /*主动(自主管理)*/\n" +
				"union all\n" +
				"select ifnull(sum(zgjh.AMT_WEIGH),0) c1, 130 row_index from  zgjh  where (zgjh.CBND_SCD_CTG is null or zgjh.CBND_SCD_CTG not in('1701','1703','1704','1706','1702'))/*其他资产管理产品*/ and zgjh.MNG_MTH='02' /*被动(委托管理) */\n" +
				"union all\n" +
				"select ifnull(sum(zgjh.AMT_WEIGH),0) c1, 131 row_index from  zgjh  /*资产管理产品合计*/\n" +
				"\n" +
				"union all\n" +
				"select sum(tt.AMT_WEIGH) as c1,141 as row_index from (\n" +
				"\tselect ifnull(sum(w.AMT_WEIGH),0) as AMT_WEIGH from dwd_ast_prd_ast_lbl_pos_dtl_w w where w.POS_DT='' and (w.BRED_CD in ('1','3','4','8'))\n" +
				"\tunion all\n" +
				"\tselect ifnull(sum(w.AMT_WEIGH),0) as AMT_WEIGH from  dwd_ast_prd_ast_lbl_pos_dtl_w w join DWD_AST_NSTD_AST_INF b on w.SCR_ID=b.SCR_ID  where w.POS_DT='' and  (w.BRED_CD=5\n" +
				"\tand (b.CHN_SCR_ID is null or CHN_SCR_ID='')/*非标去掉通道*/ or w.BRED_CD=2/*同业借款*/) /*非标合计*/\n" +
				"\tunion all\n" +
				"\tselect ifnull(sum(zgjh.AMT_WEIGH),0) as AMT_WEIGH from  zgjh  /*资产管理产品合计*/\n" +
				") tt\n" +
				"\n" +
				"union all\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 147 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w where w.POS_DT='' and  w.BRED_CD=12/*卖出回购*/\n" +
				"union all\n" +
				"\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 148 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w  where w.POS_DT='' and  w.BRED_CD=12/*回购合计*/\n" +
				"union all\n" +
				"\n" +
				"select ifnull(sum(w.AMT_WEIGH),0) c1, 151 row_index from  dwd_ast_prd_ast_lbl_pos_dtl_w w  where w.POS_DT='' and  w.BRED_CD=12/*负债合计*/\n" +
				")temp\n" +
				") t";
		//Dataflow flow = new FieldLineageService().getSqlDataflow(sql);
        //System.out.println(flow);

	}

	/**
	 * 初始化表血缘关系集合
	 * @param size
	 * @return
	 * @throws Exception
	 */
	private Set<String> initTableLineageSet(int size) throws Exception {
		Set<String> set = new HashSet<>(size);
		List<String> list = tableLineageDao.findManualTableLineages();
		set.addAll(list);
		return set;
	}

	Pattern cPattern = Pattern.compile(C_REGEX);
	private static final String C_REGEX = "count\\(.*?\\)";

	/**
	 * 初始化二维报表sql
	 * @param row
	 * @return
	 */
	public String initTwoDimensionalSql(SqlRow row, String sql) throws Exception {
		String reportTable = row.getString("report_table");
		List<String> columnList = new ArrayList<>();
		ResultSetMetaData metaData = fieldLineageDao.getMetaData(sql);
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			columnList.add(metaData.getColumnName(i));
		}
		List<SqlRow> fieldList = fieldLineageDao.queryTwoDimensionalTableField(reportTable, columnList);
		if (fieldList.isEmpty()) {
			log.error("base_report_column_info配置异常，表名：{}", reportTable);
			return sql;
		}
		Matcher matcher = cPattern.matcher(sql);
		if (matcher.find()) {
			sql = matcher.replaceAll("countFunction");
		}
		String insertColumnSql = fieldList.stream().map(map -> map.getString("reflect_column")).collect(Collectors.joining(", "));
		String columnSql = fieldList.stream().map(map -> map.getString("field_name")).collect(Collectors.joining(", "));
		return "insert into " + reportTable + " ( " + insertColumnSql + " ) select " + columnSql + " from ( " + sql + ") t";
	}

	/**
	 * 初始化字段血缘关系集合
	 * @param size
	 * @return
	 * @throws Exception
	 */
	private Set<String> initFieldLineageSet(int size) throws Exception {
		Set<String> set = new HashSet<>(size);
		List<String> list = fieldLineageDao.findManualFieldLineages();
		set.addAll(list);
		return set;
	}

	/**
	 * 格式化表名、字段名
	 * @param value 表名、字段名
	 * @return
	 */
	private String formatValue(String value) {
		return value.toLowerCase(Locale.ROOT)
				.trim()
				.replace("`", "")
				.replace("'","")
				.replace("\"","");
	}

	/**
	 * 拼接为全限定名
	 * @param value
	 * @return
	 */
	private String getFullName(String... value) {
		return Arrays.stream(value).filter(item-> !item.isEmpty()).collect(Collectors.joining("."));
	}

	/**
	 * 为tableFieldId首尾拼接'|'，防止模糊查询时匹配异常
	 * 否则查%prod_code%会查出prod_code_name
	 * @param tableFieldId
	 * @return
	 */
	private String format(String tableFieldId) {
		return "|" + tableFieldId + "|";
	}

	/**
	 * 关系字符去重
	 * @param dependency
	 */
	private String removeDuplicates(String dependency) {

		Set<String> set = new HashSet<>();
		StringBuilder sb = new StringBuilder();

		// 遍历字符串中关系字符，按“||”分隔
		String[] strs = dependency.split("\\|\\|");
		for (String str : strs){
			// 去除所有分隔符
			str = str.replace("|", "");
			// 如果set集合中不包含该字符串，则添加至set集合和结果字符串中
			if(!set.contains(str)){
				set.add(str);
				sb.append("|" + str + "|");
			}
		}

		return sb.toString();
	}


}
