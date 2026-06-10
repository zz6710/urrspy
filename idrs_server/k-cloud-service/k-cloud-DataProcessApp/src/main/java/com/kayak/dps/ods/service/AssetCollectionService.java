package com.kayak.dps.ods.service;

import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.dps.app.model.AssetCollection;
import com.kayak.dps.ods.dao.AssetCollectionDao;
import org.bouncycastle.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@APIDefine(desc = "资产补录配置服务", model = AssetCollection.class)
public class AssetCollectionService {

	@Autowired
	private AssetCollectionDao assetCollectionDao;

	@API(desc = "查询资产补录配置", auth = APIAuth.YES)
	public SqlResult<AssetCollection> findAssetCollections(SqlParam<AssetCollection> params) throws Exception {
		params.setMakeSql(true);
		SqlResult<AssetCollection> result =  assetCollectionDao.findAssetCollections(params);
		List<AssetCollection> rows = result.getRows();
		Map<String,Object> map = new HashMap();
		// 遍历角色查询角色名称
		for (AssetCollection row : rows) {
			map.put("roleid",row.getRoles());
			List<SqlRow> rolesName = assetCollectionDao.findRolesName(map);
			if (rolesName.isEmpty()) {
				row.setRolename("");
			} else {
				row.setRolename(rolesName.get(0).getString("rolename"));
			}

		}
		return result;
	}

	@API(desc = "新增资产补录配置", params = "page,roles,remark,page_field", auth = APIAuth.YES)
	public String addAssetCollection(SqlParam<AssetCollection> params) throws Exception {
		int n=assetCollectionDao.findCount(params);
		if(n>0){
			return RequestSupport.updateReturnJson(false, "已存在有相同的配置，新增失败", null).toString();
		}
		DaoUtil.doTrans(() -> {
			assetCollectionDao.addAssetCollection(params);
			String oriType = params.getModel().getFieldType();
			if (!oriType.equals(FileType.getV("本系统映射"))){
				params.getModel().setFieldType(oriType.equals(FileType.getV("本系统补录")) ?
						FileType.getV("本系统修改") :
						FileType.getV("本系统补录"));
				SqlResult<AssetCollection> result = assetCollectionDao.findTableSupplyUpdateColumns(params);
				if (!result.getRows().isEmpty()) {
					//原补录字段
					String strOri = result.getRows().get(0).getPageField();
					//需要从原补录字段中去掉的字段
					String stNew = params.getModel().getPageField();
					List<String> listOri = Arrays.stream(strOri.split(",")).collect(Collectors.toList());
					List<String> listNew = Arrays.stream(stNew.split(",")).collect(Collectors.toList());
					listOri.removeIf(listNew::contains);
					String newPageField = String.join(",", listOri);
					params.getModel().setPageField(newPageField);
					params.getModel().setPage(result.getRows().get(0).getPage());
					params.getModel().setRoles(result.getRows().get(0).getRoles());
					params.getModel().setFieldType(result.getRows().get(0).getFieldType());
					assetCollectionDao.updateAssetCollectionforChange(params);
				}
				params.getModel().setFieldType(oriType);
			}
		});

		return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
	}
	
	@API(desc = "修改资产补录配置", params = "page,roles,remark,page_field", auth = APIAuth.YES)
	public int updateAssetCollection(SqlParam<AssetCollection> params) throws Exception {
		AtomicInteger res = new AtomicInteger();
		DaoUtil.doTrans(() -> {
			res.set(assetCollectionDao.updateAssetCollection(params).getEffect());
			String oriType = params.getModel().getFieldType();
			if (!oriType.equals(FileType.getV("本系统映射"))) {
				params.getModel().setFieldType(oriType.equals(FileType.getV("本系统补录")) ?
						FileType.getV("本系统修改") :
						FileType.getV("本系统补录"));
				//查原补录字段信息
				SqlResult<AssetCollection> result = assetCollectionDao.findTableSupplyUpdateColumns(params);
				if (!result.getRows().isEmpty()) {
					//原补录字段
					String strOri = result.getRows().get(0).getPageField();
					//需要从原补录字段中去掉的字段
					String stNew = params.getModel().getPageField();
					List<String> listOri = Arrays.stream(strOri.split(",")).collect(Collectors.toList());
					List<String> listNew = Arrays.stream(stNew.split(",")).collect(Collectors.toList());
					listOri.removeIf(listNew::contains);
					String newPageField = String.join(",", listOri);
					params.getModel().setPageField(newPageField);
					params.getModel().setPage(result.getRows().get(0).getPage());
					params.getModel().setRoles(result.getRows().get(0).getRoles());
					params.getModel().setFieldType(result.getRows().get(0).getFieldType());
					res.set(assetCollectionDao.updateAssetCollectionforChange(params).getEffect());
				}
				params.getModel().setFieldType(oriType);
			}
		});
		return res.get();
	}
	
	@API(desc = "删除资产补录配置", params = "page,roles,remark,page_field", auth = APIAuth.YES)
	public int deleteAssetCollection(SqlParam<AssetCollection> params) throws Exception {
		return assetCollectionDao.deleteAssetCollection(params).getEffect();
	}

	/**
	 * 根据SCR_ID查资产
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int isOnlyOne(Map<String,Object> params) throws Exception {
		List<SqlRow> res = assetCollectionDao.isOnlyOne(params);
		return res.size();
	}

	/**
	 * 根据机构代码查唯一
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int isOnlyForOne(Map<String,Object> params) throws Exception {
		List<SqlRow> res = assetCollectionDao.isOnlyForOne(params);
		return res.size();
	}


	@API(desc = "查询系统维护的角色", auth = APIAuth.NO)
	public SqlResult<SqlRow> findSysRoles(SqlParam<AssetCollection> params) throws Exception {
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		List<SqlRow> tempRoles = assetCollectionDao.findSysRoles(params);
		sqlRowSqlResult.setResults(tempRoles.size());
		sqlRowSqlResult.setRows(tempRoles);
		sqlRowSqlResult.setDesensitized(false);;
		return sqlRowSqlResult;
	}

	@API(desc = "查询表字段", auth = APIAuth.NO)
	public SqlResult<AssetCollection> findTableColumns(SqlParam<AssetCollection> params) throws Exception {
		Map<String, Object> param = new HashMap<>();
		param.put("page", params.getModel().getPage());
		List<SqlRow> sqlRows = assetCollectionDao.getTableName(param);
		if (!sqlRows.isEmpty()) {
			params.getModel().setTableName(sqlRows.get(0).getString("tableName"));
		}
		//区分补录与修改时所查询的字段
		SqlResult<AssetCollection> result = assetCollectionDao.findTableColumns(params);
		List<AssetCollection> assetCollections = result.getRows();
		assetCollections.removeIf(a -> a.getValue().equalsIgnoreCase("SCR_ID")
				|| a.getValue().equalsIgnoreCase("SCR_CD")
				|| a.getValue().equalsIgnoreCase("CRT_DATE")
				|| a.getValue().equalsIgnoreCase("CRT_TIME")
				|| a.getValue().equalsIgnoreCase("UPD_DATE")
				|| a.getValue().equalsIgnoreCase("UPD_TIME")
				|| a.getValue().equalsIgnoreCase("CRT_USER")
				|| a.getValue().equalsIgnoreCase("UPD_USER")
				|| a.getValue().equalsIgnoreCase("VERSION")
				|| a.getValue().equalsIgnoreCase("DEAL_DATE")
				|| a.getValue().equalsIgnoreCase("ID"));
		result.setRows(assetCollections);
		return result;
	}

	@API(desc = "查询已配置的表字段", auth = APIAuth.NO,operation = APIOperation.SELECT)
	public String findTableSetColumns(SqlParam<AssetCollection> params) throws Exception {
		Map<String, Object> param = new HashMap<>();
		param.put("page", params.getModel().getPage());
		List<SqlRow> sqlRows = assetCollectionDao.getTableName(param);
		if (!sqlRows.isEmpty()) {
			params.getModel().setTableName(sqlRows.get(0).getString("tableName"));
		}
		String stNew = params.getModel().getPageField();
		List<String> listNew = Arrays.stream(stNew.split(",")).collect(Collectors.toList());
		String oriType = params.getModel().getFieldType();
		params.getModel().setRoleid(params.getModel().getRoles());
		params.getModel().setFieldType(oriType.equals(FileType.getV("本系统补录")) ?
				FileType.getV("本系统修改") :
				FileType.getV("本系统补录"));
		List<SqlRow> rowsOri = assetCollectionDao.findColumns(params);
		params.getModel().setFieldType(oriType);
		if (!rowsOri.isEmpty()){
			List<String> listOri = Arrays.stream(rowsOri.get(0).getString("page_field").split(",")).collect(Collectors.toList());
			String resultPageField = listNew.stream()
					.filter(listOri::contains)
					.collect(Collectors.joining(","));
			List<SqlRow> resultLabel = assetCollectionDao.findColumnsLabel(params,resultPageField);
			param.put("label",resultLabel.get(0).getString("label"));
		}else{
			param.put("label","");
		}
		return RequestSupport.updateReturnJson(true, "操作成功", param).toString();
	}

	@API(desc = "查询资产补录配置字段", auth = APIAuth.NO)
	public SqlResult<SqlRow> findColumns(SqlParam<AssetCollection> params) throws Exception {
		String userid = SysUtil.getLoginUserid();
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		List<SqlRow> sqlRows = assetCollectionDao.getRoleIdFromUserId(userid);
		List<SqlRow> result = new ArrayList<>();
		params.getModel().setRoleid(sqlRows.get(0).getString("roleid"));
		List<SqlRow> rows = assetCollectionDao.findColumns(params);
		result.addAll(rows);
		if (result.size() == 0) {
			sqlRowSqlResult.setResults(result.size());
			sqlRowSqlResult.setRows(result);
			sqlRowSqlResult.setDesensitized(false);;
			return sqlRowSqlResult;
		}
		String[] columns = Strings.split(result.get(0).getString("page_field"), ',');
		//去重
		String[] uniqueColumns = Stream.of(columns).distinct().toArray(String[]::new);
		StringBuilder s = new StringBuilder();
		for (String column : uniqueColumns) {
			s.append(camelName(column)).append(",");
		}
		result.get(0).put("label",s.length() > 0 ? s.deleteCharAt(s.length()-1).toString() : "");
		sqlRowSqlResult.setResults(result.size());
		sqlRowSqlResult.setRows(result);
		sqlRowSqlResult.setDesensitized(false);;
		return sqlRowSqlResult;
	}

	// 将查询出来的字段处理成驼峰形式
	private static String camelName(String name){
		StringBuilder result = new StringBuilder();
		if (name == null || name.isEmpty()) {
			return "";
		} else if (!name.contains("_")) {
			return name.toLowerCase(Locale.CHINESE);
		}
		// 用下划线将原始字符串分割
		String[] camels = name.split("_");
		for (String camel : camels) {
			// 跳过原始字符串中开头、结尾的下划线或双重下划线
			if (camel.isEmpty()) {
				continue;
			}
			// 处理真正的驼峰片段
			if (result.length() == 0) {
				// 第一个驼峰片段，全部字母都小写
				result.append(camel.toLowerCase(Locale.CHINESE));
			} else {
				// 其他驼峰片段，首字母大写
				result.append(camel.substring(0, 1).toUpperCase(Locale.CHINESE));
				result.append(camel.substring(1).toLowerCase(Locale.CHINESE));
			}
		}
		return result.toString();
	}

	public enum FileType {
		/**
		 * 文件类型
		 */
		DOCX("本系统补录", "01"),
		DOC("本系统修改", "02"),
		PDF("本系统映射", "03");

		private final String key;
		private final String value;

		private static final Map<String, FileType> keyMap = new HashMap<>();

		static {
			for (FileType fileType : FileType.values()) {
				keyMap.put(fileType.key, fileType);
			}
		}

		FileType(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		public static String getV(String key) {
			FileType fileType = keyMap.get(key);
			if (fileType != null) {
				return fileType.value;
			}
			throw new IllegalArgumentException("未知的 fileType: " + key);
		}
	}
}
