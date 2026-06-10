package com.kayak.index.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.dao.DaoService;
import com.kayak.core.exception.SqlException;
import com.kayak.core.util.Tools;

@Service
public class IndexService {

	private static Logger log = LoggerFactory.getLogger(IndexService.class);

	@Autowired
	private DaoService daoService;
	@Autowired
	private ComnDao comnDao;

	public Map<String, String> getTableIndex(String table) throws Exception {
		if (Tools.isSqlInjection(table)) {
			log.error("检测到sql注入[{}]!!!", table);
			throw new SqlException("检测到sql注入!!!");
		}

		String dbType = daoService.getDbType(0);
		if (dbType.contains("mysql")) {
			return getMysqlTableIndex(table);
		} else if (dbType.contains("oracle")) {
			return getOracleTableIndex(table);
		} else if (dbType.contains("db2")) {
			return getDb2TableIndex(table);
		}
		return null;
	}

	private Map<String, String> getMysqlTableIndex(String table) throws Exception {
		Map<String, String> indexMap = new HashMap<String, String>();
		daoService.doTrans(() -> {
			Connection connection = daoService.getConnection();

			PreparedStatement ps = connection.prepareStatement("SHOW INDEX FROM " + table);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String column = rs.getString(5);
				String keyName = rs.getString(3);
				indexMap.put(column, keyName);
			}

		});

		return indexMap;
	}

	private Map<String, String> getOracleTableIndex(String table) throws Exception {
		Map<String, String> indexMap = new HashMap<String, String>();
		daoService.doTrans(() -> {
			Connection connection = daoService.getConnection();

			PreparedStatement ps = connection.prepareStatement("SELECT ui.index_name,ui.table_name,ui.uniqueness," +
					"uic.column_name FROM user_indexes ui,user_ind_columns uic WHERE ui.index_name = uic.index_name AND" +
					" ui.table_name = '" + table.toUpperCase() + "'");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String column = rs.getString(4);
				String keyName = rs.getString(1);
				indexMap.put(column, keyName);
			}

		});

		return indexMap;
	}

	private Map<String, String> getDb2TableIndex(String table) throws Exception {
		Map<String, String> indexMap = new HashMap<String, String>();
		daoService.doTrans(() -> {
			Connection connection = daoService.getConnection();

			PreparedStatement ps = connection.prepareStatement("SELECT idx.name indexname,idx.tbname,idc.colname " +
					"FROM sysibm.sysindexes idx,sysibm.sysindexcoluse idc WHERE idx.name =  idc.indname AND idx.tbname" +
					" = '"+ table.toUpperCase() + "'");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String column = rs.getString(3);
				String keyName = rs.getString(1);
				indexMap.put(column, keyName);
			}

		});

		return indexMap;
	}

	public void addIndex(String table, String dbFeild,String indexName) throws Exception {
		if (Tools.isSqlInjection(table)) {
			log.error("检测到sql注入[{}]!!!", table);
			throw new SqlException("检测到sql注入!!!");
		}

		if (Tools.isSqlInjection(dbFeild)) {
			log.error("检测到sql注入[{}]!!!", dbFeild);
			throw new SqlException("检测到sql注入!!!");
		}
		comnDao.update("CREATE INDEX " + indexName + " ON " + table + "(" + dbFeild + ")");
	}

	public void deleteIndex(String table, String indexName) throws Exception {
		if (Tools.isSqlInjection(table)) {
			log.error("检测到sql注入[{}]!!!", table);
			throw new SqlException("检测到sql注入!!!");
		}

		if (Tools.isSqlInjection(indexName)) {
			log.error("检测到sql注入[{}]!!!", indexName);
			throw new SqlException("检测到sql注入!!!");
		}

		String dbType = daoService.getDbType(0);
		if (dbType.contains("mysql")) {
			comnDao.update("DROP INDEX " + indexName + " ON " + table);
		} else if (dbType.contains("oracle")) {
			comnDao.update("DROP INDEX " + indexName);
		} else if (dbType.contains("db2")) {
			comnDao.update("DROP INDEX " + indexName);
		}
	}
}
