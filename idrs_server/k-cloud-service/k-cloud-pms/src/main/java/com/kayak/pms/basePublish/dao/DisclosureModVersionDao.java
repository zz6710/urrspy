package com.kayak.pms.basePublish.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.basePublish.model.DisclosureModVersion;
import com.kayak.pms.global.constants.XpStatus;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;

@Repository
public class DisclosureModVersionDao extends ComnDao {

	public SqlResult<DisclosureModVersion> findDisclosureModVersions(SqlParam<DisclosureModVersion> params) throws Exception {
		return super.findRows("SELECT mv.id,mv.id disclosure_mod_version_id,mv.disclosure_mod_id,md.mod_name,mv.version,mv.doc_name,mv.doc_html," +
				" mv.crt_date,mv.crt_time,mv.crt_user_id,mv.crt_user_name," +
				" mv.upd_date,mv.upd_time,mv.upd_user_id,mv.upd_user_name," +
				" mv.status,mv.remark," +
				" md.disclosure_type,md.disclosure_son_type " +
				" FROM idb_disclosure_mod_version mv" +
				" left join idb_disclosure_mod md" +
				" on mv.disclosure_mod_id = md.id where 1=1 and disclosure_mod_id = $S{disclosureModId} order by mv.id asc",
				DataSourceProperty.IDB,params);
	}
	/**
	* @功能描述:启用信披模板版本
	* @params:[params]
	* @return:com.kayak.core.sql.UpdateResult
	* @Athor:ouyifan
	* @date:2022/6/20
	*/
	public UpdateResult recoverStatus(SqlParam<DisclosureModVersion> params) throws Exception {
		return super.update("update idb_disclosure_mod_version set status = '"+XpStatus.start.getItemKey()+"' where id = $S{id}",
				DataSourceProperty.IDB,params.getModel());
	}
	/**
	* @功能描述:停用信披模板版本
	* @params:[params]
	* @return:com.kayak.core.sql.UpdateResult
	* @Athor:ouyifan
	* @date:2022/6/20
	*/
	public UpdateResult stopStatus(SqlParam<DisclosureModVersion> params) throws Exception {
		return super.update("update idb_disclosure_mod_version set status = '"+ XpStatus.stop.getItemKey()+"' where id = $S{id}",
				DataSourceProperty.IDB,params.getModel());
	}
	/**
	* @功能描述:check版本被绑定的数量
	* @params:[params]
	* @return:java.lang.Integer
	* @Athor:ouyifan
	* @date:2022/6/20
	*/
	public Integer findCountOfBandVersion(SqlParam<DisclosureModVersion> params) throws Exception {
		SqlRow sqlRow = super.findRow("SELECT COUNT(*) count FROM idb_disclosure_rule WHERE disclosure_mod_version_id = $S{id} AND STATUS = '1'",
				DataSourceProperty.IDB,params.getModel());
		return sqlRow.getInteger("count");
	}
	public String addDisclosureModVersion(DisclosureModVersion disclosureModVersion) throws Exception {
		return super.update("INSERT INTO idb_disclosure_mod_version(id,disclosure_mod_id,version,doc_name,doc_html,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,status,remark,upload_path) VALUES($AUTOIDS{id},$S{disclosureModId},$S{version},$S{docName},$S{docHtml},$S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName},$S{updDate},$S{updTime},$S{updUserId},$S{updUserName},$S{status},$S{remark},$S{uploadPath})",
				DataSourceProperty.IDB,disclosureModVersion).getAutoId();
	}
	public void updDisclosureVersionPath(DisclosureModVersion disclosureModVersion) throws Exception {
		super.update("update idb_disclosure_mod_version set upload_path = $S{uploadPath} where id = $S{disclosureModVersionId}",
				DataSourceProperty.IDB,disclosureModVersion);
	}
	public DisclosureModVersion getMaxXPVersion(String disclosureModId) throws Exception {
		String sql = "SELECT IF(MAX(VERSION)<>'' or MAX(VERSION) is not null,MAX(VERSION),'V1.0') version FROM idb_disclosure_mod_version WHERE disclosure_mod_id =$S{disclosureModId}";
		return super.findRow(DisclosureModVersion.class, sql,
				DataSourceProperty.IDB, disclosureModId);
	}
	public UpdateResult deleteDisclosureModVersion(SqlParam<DisclosureModVersion> params) throws Exception {
		return super.update("DELETE FROM idb_disclosure_mod_version WHERE  id=$S{id} ",
				DataSourceProperty.IDB, params.getModel());
	}
	public Integer checkDisclosureMod(SqlParam<DisclosureModVersion> params) throws Exception {
		return super.findRow("select count(*) count from idb_disclosure_rule where 1=1  and disclosure_mod_version_id=$S{id}",
				DataSourceProperty.IDB, params.getModel()).getInteger("count");
	}
	public Integer checkDisclosureModInRule(SqlParam<DisclosureModVersion> params) throws Exception {
		return super.findRow("select count(*) count from idb_disclosure_prod_rule where 1=1  and disclosure_mod_version_id=$S{id}",
				DataSourceProperty.IDB, params.getModel()).getInteger("count");
	}
	public Integer checkDisclosureModInTask(SqlParam<DisclosureModVersion> params) throws Exception {
		return super.findRow("SELECT COUNT(*) `count` FROM (SELECT DISTINCT disclosure_mod_version_id FROM idb_disclosure_notice_version )notVer\n" +
						"LEFT JOIN idb_disclosure_mod_version `ver` ON notVer.disclosure_mod_version_id = ver.id\n" +
						"where ver.id=$S{id}\n",
				DataSourceProperty.IDB, params.getModel()).getInteger("count");
	}
	public UpdateResult deleteDisclosureMod(SqlParam<DisclosureModVersion> params) throws Exception {
		return super.update("DELETE FROM idb_disclosure_mod_version WHERE  disclosure_mod_id=$S{disclosureModId} ",
				DataSourceProperty.IDB,params.getModel());
	}
	public UpdateResult deleteDisclosureModCol(String disclosureModVersionId) throws Exception {
		return super.update("DELETE FROM idb_disclosure_mod_column WHERE disclosure_mod_version_id=$S{disclosureModId} ",
				DataSourceProperty.IDB,disclosureModVersionId);
	}
	public List<SqlRow> getNewestPrintTempVersion(String disclosureModId) throws Exception {
		return super.findRows("select t.version,t.doc_name from idb_disclosure_mod_version t where t.id in (select max(CONVERT(p.id,SIGNED))  id from idb_disclosure_mod_version p where p.disclosure_mod_id = $S{disclosureModId})",
				DataSourceProperty.IDB,disclosureModId);
	}
	public List<SqlRow> getVersionId(String disclosureModId) throws Exception {
		return super.findRows("SELECT id versionId FROM idb_disclosure_mod_version WHERE  disclosure_mod_id = $S{disclosureModId}",
				DataSourceProperty.IDB,disclosureModId);
	}
	public DisclosureModVersion getPrintXPVersionById(String id) throws Exception{
		String sql = "SELECT id,disclosure_mod_id,version,doc_name,doc_html,remark FROM idb_disclosure_mod_version " +
				"  where id = $S{id} ";
		SqlRow sqlRow = super.findRow(sql,
				DataSourceProperty.IDB,id);
		DisclosureModVersion modVersion = new DisclosureModVersion();
		modVersion.setId(sqlRow.getString("id"));
		modVersion.setDisclosureModId(sqlRow.getString("disclosure_mod_id"));
		modVersion.setVersion(sqlRow.getString("version"));
		modVersion.setDocName(sqlRow.getString("doc_name"));
		modVersion.setDocHtml(sqlRow.getString("doc_html"));
		modVersion.setRemark(sqlRow.getString("remark"));
		return modVersion;
	}
	public DisclosureModVersion findDisclosureModVersionsById(String id) throws Exception {
		SqlParam<DisclosureModVersion> param=new FetcherData<>(new HashMap<>(),DisclosureModVersion.class);
		return super.findRow(DisclosureModVersion.class,"SELECT id,disclosure_mod_id,version,doc_name,doc_html,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,status,remark FROM idb_disclosure_mod_version where id='"+id+"'",
				DataSourceProperty.IDB,param);
	}
}
