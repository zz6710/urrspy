package com.kayak.dps.sqlflow.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.dps.sqlflow.model.TableLineage;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.HashMap;
import java.util.List;

@Repository
public class TableLineageDao extends ComnDao {

	public SqlResult<TableLineage> findTableLineages(SqlParam<TableLineage> params) throws Exception {
		String sql = "SELECT id,exeid,to_table_info_id,from_table_info_id,manual_flag FROM rms_table_lineage where 1=1";
		if (Tools.isNotEmpty(params.getModel().getToTableInfoId())) {
			sql += " and to_table_info_id like concat('%', $S{toTableInfoId}, '%')";
		}
		if (Tools.isNotEmpty(params.getModel().getFromTableInfoId())) {
			sql += " and from_table_info_id like concat('%', $S{fromTableInfoId}, '%')";
		}
		return super.findRows(sql, params);
	}


	public UpdateResult addTableLineage(SqlParam<TableLineage> params) throws Exception {
		return super.update("INSERT INTO rms_table_lineage(id,exeid,to_table_info_id,from_table_info_id,manual_flag) VALUES((SELECT MAX(a.id)+1  as id from rms_table_lineage a),$S{exeid},$S{toTableInfoId},$S{fromTableInfoId},$S{manualFlag})",
				params.getModel());
	}

	public UpdateResult updateTableLineage(SqlParam<TableLineage> params) throws Exception {
		return super.update("UPDATE rms_table_lineage SET exeid=$S{exeid} ,to_table_info_id=$S{toTableInfoId} ,from_table_info_id=$S{fromTableInfoId} ,manual_flag=$S{manualFlag}  WHERE  id=$I{id} ",
				params.getModel());
	}

	public UpdateResult deleteTableLineage(SqlParam<TableLineage> params) throws Exception {
		return super.update("DELETE FROM rms_table_lineage WHERE  id=$I{id} ",
				params.getModel());
	}

	/**
	 * 新增
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public UpdateResult addTableLineage(TableLineage params) throws Exception {
		return super.update("INSERT INTO rms_table_lineage(exeid,to_table_info_id,from_table_info_id,manual_flag) VALUES($S{exeid},$S{toTableInfoId},$S{fromTableInfoId},$S{manualFlag})",
				params);
	}

	/**
	 * 删除非手工维护的数据
	 * @return
	 * @throws Exception
	 */
	public void deleteTableLineage() throws Exception {
		super.update("DELETE FROM rms_table_lineage WHERE manual_flag = '0'");
	}

	/**
	 * 查询手工维护的数据
	 * @return
	 * @throws Exception
	 */
	public List<String> findManualTableLineages() throws Exception {
		String sql = "select concat(exeid, '-', to_table_info_id, '-', from_table_info_id) as value" +
				" from rms_table_lineage" +
				" where manual_flag = '1'";
		return super.findRows(String.class, sql, DataSourceProperty.PUB, new HashMap<>());
	}

}
