package com.kayak.pms.basePublish.dao;

import java.util.List;

import com.kayak.base.dao.DataSourceProperty;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.pms.basePublish.model.DisclosureSource;

@Repository
public class DisclosureSourceDao extends ComnDao {
	/**
	 * @功能描述:查询字段配置
	 * @params:[params]
	 * @return:com.kayak.core.sql.SqlResult<com.kayak.pms.basePublish.model.DisclosureSource>
	 * @Athor:ouyifan
	 * @date:2022/6/20
	 */
	public SqlResult<DisclosureSource> findDisclosureSources(SqlParam<DisclosureSource> params) throws Exception {
		return super.findRows("SELECT id,column_label,column_key,column_value,dict,value_sql,sql_parameter,money_format,computed_expression,functype,confoption,status,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,remark,data_length,data_source FROM idb_disclosure_source order by id desc",
				DataSourceProperty.IDB, params);
	}
	/**
	 * @功能描述:添加字段配置
	 * @params:[params]
	 * @return:com.kayak.core.sql.UpdateResult
	 * @Athor:ouyifan
	 * @date:2022/6/20
	 */
	public UpdateResult addDisclosureSource(SqlParam<DisclosureSource> params) throws Exception {
		return super.update("INSERT INTO idb_disclosure_source(column_label,column_key,column_value,dict,functype,confoption,status,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,remark,money_format,computed_expression,data_length,sql_parameter,value_sql,data_source) VALUES($S{columnLabel},$S{columnKey},$S{columnValue},$S{dict},$S{functype},$S{confoption},'1',$S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName},$S{updDate},$S{updTime},$S{updUserId},$S{updUserName},$S{remark}, $S{moneyFormat}, $S{computedExpression}, $D{dataLength}, $S{sqlParameter}, $S{valueSql},$S{dataSource})",
				DataSourceProperty.IDB,params.getModel());
	}
	/**
	 * @功能描述:更改字段配置
	 * @params:[params]
	 * @return:com.kayak.core.sql.UpdateResult
	 * @Athor:ouyifan
	 * @date:2022/6/20
	 */
	public UpdateResult updateDisclosureSource(SqlParam<DisclosureSource> params) throws Exception {
		return super.update("UPDATE idb_disclosure_source SET column_label=$S{columnLabel} ,column_key=$S{columnKey} ,column_value=$S{columnValue} ,dict=$S{dict} ,value_sql=$S{valueSql} ,sql_parameter=$S{sqlParameter} ,money_format=$S{moneyFormat},computed_expression=$S{computedExpression} ,functype=$S{functype} ,data_length=$D{dataLength} ,status=$S{status} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,crt_user_id=$S{crtUserId} ,crt_user_name=$S{crtUserName} ,upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user_id=$S{updUserId} ,upd_user_name=$S{updUserName} ,remark=$S{remark},data_source = $S{dataSource}  WHERE  id=$S{id} ",
				DataSourceProperty.IDB,params.getModel());
	}
	/**
	 * @功能描述:删除字段配置
	 * @params:[params]
	 * @return:com.kayak.core.sql.UpdateResult
	 * @Athor:ouyifan
	 * @date:2022/6/20
	 */
	public UpdateResult deleteDisclosureSource(SqlParam<DisclosureSource> params) throws Exception {

		return super.update("DELETE FROM idb_disclosure_source WHERE  id=$S{id} ",
				DataSourceProperty.IDB,params.getModel());
	}
	/**
	 * @功能描述:模板读取查询字段配置
	 * @params:[keys]
	 * @return:java.util.List<com.kayak.core.sql.SqlRow>
	 * @Athor:ouyifan
	 * @date:2022/6/20
	 */
	public List<SqlRow> findDisclosureSourcesBykeys(String keys) throws Exception {
		return super.findRows("SELECT id,column_label,column_key,column_value,dict,value_sql,sql_parameter,money_format,computed_expression,functype,confoption,status,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,remark,data_source FROM idb_disclosure_source where FIND_IN_SET (column_key,$S{keys})",
				DataSourceProperty.IDB,Tools.makeParams().put("keys", keys).build());
	}
	/**
	 * @功能描述:校验重复字段配置
	 * @params:[params]
	 * @return:java.lang.Integer
	 * @Athor:ouyifan
	 * @date:2022/6/20
	 */
	public Integer findDisclosureSourcesDupKey(SqlParam<DisclosureSource> params) throws Exception {
		SqlRow sqlRow =super.findRow("SELECT count(*) count FROM idb_disclosure_source where FIND_IN_SET (column_key,$S{columnKey})",
				DataSourceProperty.IDB, params.getModel());
		return sqlRow.getInteger("count");
	}
}
