package com.kayak.pms.basePublish.dao;
import cn.hutool.core.collection.CollectionUtil;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.basePublish.model.DisclosureModColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Repository
public class DisclosureModColumnDao extends ComnDao {

	public UpdateResult updateDisclosureModColumn(DisclosureModColumn disclosureModColumn) throws Exception {
		return super.update("UPDATE idb_disclosure_mod_column SET isdisplay=$S{isdisplay} ,roleids=$S{roleids} ,is_sysvalue=$S{isSysvalue} ,userid=$S{userid},upd_user_name = $S{updUserName},upd_user_id = $S{updUserId},upd_time = $S{updTime},upd_date = $S{updDate}  WHERE  id=$S{id} ",
				DataSourceProperty.IDB,disclosureModColumn);
	}

	public int addDisclosureModColumn(DisclosureModColumn disclosureModColumn) throws Exception {
		return super.update("INSERT INTO idb_disclosure_mod_column(id,disclosure_mod_version_id,t8_disclosure_source_id,column_label,column_key,column_value,isdisplay,roleids,is_sysvalue,sqls,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,remark,file_name,upload_path,view_url) VALUES($AUTOIDS{id},$S{disclosureModVersionId},$S{t8DisclosureSourceId},$S{columnLabel},$S{columnKey},$S{columnValue},$S{isdisplay},$S{roleids},$S{isSysvalue},$S{sqls},$S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName},$S{updDate},$S{updTime},$S{updUserId},$S{updUserName},$S{remark},$S{fileName},$S{uploadPath},$S{viewUrl})",
				DataSourceProperty.IDB,disclosureModColumn).getEffect();
	}

	public DisclosureModColumn getMaxXPVersionId(String disclosureModVersionId) throws Exception {
		String sql = "SELECT id,disclosure_mod_version_id,t8_disclosure_source_id,column_label,column_key,column_value,isdisplay,roleids,is_sysvalue,sqls,remark ,file_name,upload_path,view_url FROM idb_disclosure_mod_column " +
				"              where disclosure_mod_version_id = $S{disclosureModVersionId}";
		return super.findRow(DisclosureModColumn.class, sql,
				DataSourceProperty.IDB, disclosureModVersionId);
	}

	public List<DisclosureModColumn> geXPbyModId(String disclosureModVersionId) throws Exception {
		String sql = "SELECT row_number() over(order by convert(tds.column_label using gbk) asc) row_num,tdmc.userid,tdmc.id,tdmc.disclosure_mod_version_id,tds.source_type AS data_type,tds.column_label,tds.column_key,tds.column_value,tdmc.isdisplay,\n" +
				"tdmc.roleids,tdmc.is_sysvalue,tdmc.sqls,tdmc.remark,\n" +
				"tdmc.file_name,tdmc.upload_path,tdmc.view_url FROM idb_disclosure_mod_column tdmc LEFT JOIN idb_disclosure_source tds ON tdmc.column_key=tds.column_key WHERE disclosure_mod_version_id=$S{disclosureModVersionId} AND tds.column_label IS NOT NULL order by convert(tds.column_label using gbk) asc";
		return super.findRows(DisclosureModColumn.class, sql,
				DataSourceProperty.IDB, disclosureModVersionId);
	}

	public SqlResult<SqlRow> findSupplementaryRecord(SqlParam<DisclosureModColumn> params) throws Exception {
		List<SqlRow> rows = super.findRows("select a.column_label,a.column_key,a.isdisplay,a.roleids,b.dict,b.functype from idb_disclosure_mod_column a left join idb_disclosure_source b on a.t8_disclosure_source_id = b.id where a.disclosure_mod_version_id = '4'",
				DataSourceProperty.IDB);
		SqlResult<SqlRow> sqlResult = new SqlResult<>();
		sqlResult.setRows(rows);
		return sqlResult;
	}

	/**
	 * 查询公告详情对应模板维护字段信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<SqlRow> findSupplementaryRecordForDisclosureInfo(SqlParam<DisclosureModColumn> params) throws Exception {
		String sqlStr = "select dnv.id, dnv.t8_disclosure_notice_id, dnv.t8_disclosure_version_id, dnv.prod_code, dnv.data_date, dmc.column_label, dnv.column_key, dnv.column_value, dnv.isdisplay, " +
				"       dnv.roleids, dnv.is_sysvalue, dnv.dict, dnv.functype, dnv.data_type, dnv.confoption, dnv.source_type, " +
				"        CASE " +
				"      WHEN ( " +
				"        dnv.column_value IS NULL  " +
				"        OR dnv.column_value = '')  " +
				"        AND dnv.is_sysvalue = '2' " +
				"        " +
				"      THEN '0'  " +
				"      /*待补录*/ " +
				"      WHEN  " +
				"        dnv.column_value IS NOT NULL  " +
				"        AND dnv.column_value <> ''  " +
				"        AND dnv.is_sysvalue = '2' " +
				"        " +
				"      THEN '1'  " +
				"      /*已补录*/ " +
				"      ELSE '2'  " +
				"      /*不需要补录*/ " +
				"    END AS is_addition " +
				"  from idb_disclosure_notice_value dnv" +
				"  left join idb_disclosure_source dmc on dmc.column_key =dnv.column_key  " +
				" where dnv.t8_disclosure_version_id = $S{noticeVersionId} ";
		if (StringUtils.isNotEmpty(params.getModel().getIsSysvalue())) {
			sqlStr += " and dnv.is_sysvalue = $S{isSysvalue}";
		};
//		sqlStr += " order by dnv.seq_numbers+0 desc";
		sqlStr += " order by  dnv.column_key";

		List<SqlRow> rows = super.findRows(sqlStr, DataSourceProperty.IDB, params.getModel());
		SqlResult<SqlRow> sqlResult = new SqlResult<>();
		sqlResult.setRows(rows);
		return sqlResult;
	}

	public SqlResult<SqlRow> findNonStandardDesc(SqlParam<DisclosureModColumn> params) throws Exception {
		List<SqlRow> rows = super.findRows("select id, t8_disclosure_notice_id, t8_disclosure_version_id as disclosure_mod_version_id, prod_code, data_date, column_label, column_key, column_value, isdisplay, roleids, is_sysvalue, dict, functype,data_type, confoption, source_type from idb_disclosure_notice_value where t8_disclosure_notice_id=$S{t8DisclosureNoticeId} and column_key in ('is_formal','non_standard_desc','is_single','non_standard_term_desc') order by seq_numbers+0 desc",
				DataSourceProperty.IDB,params.getModel());
		SqlResult<SqlRow> sqlResult = new SqlResult<>();
		sqlResult.setRows(rows);
		return sqlResult;
	}


	public SqlResult<DisclosureModColumn> isEdit(SqlParam<DisclosureModColumn> params) throws Exception{
		//根据任务id以及用户名判断是产品经理还是估值经理
		List<SqlRow> sqlRows = new ArrayList<>();
		SqlRow row1 = super.findRow("select  case when locate((select jobno from sys_user where userid = $S{userId}),invest_manager_id) > 0 then '14' end role_id from t8_prod_bonus_rule where id = $S{id}",
				DataSourceProperty.IDB,params.getModel());
		if (row1.get("role_id") !=null)
			sqlRows.add(row1);
		SqlRow row2 = super.findRow("select  case when locate((select jobno from sys_user where userid = $S{userId}),valuation_manager_id) > 0 then '9' end role_id from t8_prod_bonus_rule where id = $S{id}",
				DataSourceProperty.IDB,params.getModel());
		if (row2.get("role_id") != null)
			sqlRows.add(row2);
		List<SqlRow> row3 = super.findRows("select role_id  from  idb_disclosure_notice_entry_process where notice_id = $S{id} and to_user_id = $S{userId} and input_status = '0'",
				DataSourceProperty.IDB,params.getModel());
		if (CollectionUtil.isNotEmpty(row3)) {
			sqlRows.addAll(row3);
		}
		SqlResult<DisclosureModColumn> result = new SqlResult<>();
		if (CollectionUtil.isNotEmpty(sqlRows)) {
			StringJoiner roles = new StringJoiner(",");
			for (SqlRow row : sqlRows) {
				roles.add("'"+row.getString("role_id")+"'");
			}
			String sql = "select tdmc.* from idb_disclosure_mod_column tdmc left join idb_disclosure_mod_version tdmv \n" +
					"\ton tdmc.disclosure_mod_version_id = tdmv.id\n" +
					"\tleft join idb_disclosure_mod tdm on tdmv.t8_disclosure_mod_id = tdm.id\n" +
					"\twhere disclosure_type = '7' and disclosure_son_type ='8' and tdmv.status = '1' and tdmc.roleids in ("+roles +")";
			result = super.findRows(sql,
					DataSourceProperty.IDB,params);
		} else {
			List<DisclosureModColumn> list = new ArrayList<>();
			result.setRows(list);
			result.setResults(list.size());
		}
		return result;
	}
}
