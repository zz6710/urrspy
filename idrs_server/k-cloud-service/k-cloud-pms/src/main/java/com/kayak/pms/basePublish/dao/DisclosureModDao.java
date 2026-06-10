package com.kayak.pms.basePublish.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.basePublish.model.DisclosureMod;
import com.kayak.pms.basePublish.model.DisclosureModVersion;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DisclosureModDao extends ComnDao {
	private static final Logger logger = LoggerFactory.getLogger(DisclosureModDao.class);

	public SqlResult<DisclosureMod> findDisclosureMods(SqlParam<DisclosureMod> params) throws Exception {
		String sql = "select * from (select tdm.id,tdm.disclosure_type,tdm.disclosure_son_type,tdm.mod_name,tdm.doc_name,tdvm.status,tdm.crt_date,tdm.crt_time,tdvm.upd_date,tdvm.upd_time,tdm.remark,tdvm.version,tdm.crt_user_name,tdm.crt_user_id " +
				"from idb_disclosure_mod tdm left join idb_disclosure_mod_version tdvm on tdm.id = tdvm.disclosure_mod_id and version = (select max(version) from idb_disclosure_mod_version where disclosure_mod_id = tdm.id)) t where 1 =1 ";
		if(StringUtils.isNotBlank(params.getModel().getModName())){
			sql = sql + " and  t.mod_name like '%" + params.getModel().getModName() + "%'";
		}
		if(StringUtils.isNotBlank(params.getModel().getDisclosureType())){
			sql = sql + " and t.disclosure_type  =  '"+params.getModel().getDisclosureType()+"' ";
		}
		if(StringUtils.isNotBlank(params.getModel().getDisclosureSonType())){
			sql = sql + " and t.disclosure_son_type =  '"+params.getModel().getDisclosureSonType() +"' ";
		}
		if(StringUtils.isNotBlank(params.getModel().getStatus())){
			sql = sql + " and t.status = '"+params.getModel().getStatus()+"' ";
		}
		sql = sql + " order by t.crt_date desc,t.crt_time desc ";
		return super.findRows(sql,
				DataSourceProperty.IDB, params);
	}

	/**
	 * 功能：首页查询待复核定期报告模板信息 不包含自己创建的
	 * 作者：rennannan
	 * 日期：20210915
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<DisclosureMod> findNeedApprovalMods(SqlParam<DisclosureMod> params) throws Exception {
		String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
		params.getModel().setCrtUserId(userid);
		return super.findRows(" select dmod.id,dmod.disclosure_type,dmod.disclosure_son_type,dmod.mod_name,(select url operation_desc from idb_disclosure_operation_set where disclosure_type = '5' and operation_type = '13') url," +
						" '13' operation_type " +
						"  from idb_disclosure_mod dmod\n" +
						"  join idb_disclosure_mod_version version\n" +
						"\t  on dmod.id= version.disclosure_mod_id\n" +
						"  where version.`status`='0' and version.crt_user_id!=$S{crtUserId}\n" +
						"\t\tgroup by dmod.id ",
				DataSourceProperty.IDB, params);
	}

	public String addDisclosureMod(DisclosureMod disclosureMod) throws Exception {
		return super.update("INSERT INTO idb_disclosure_mod(id,disclosure_type,disclosure_son_type,mod_name,doc_name,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,remark) VALUES($AUTOIDS{id},$S{disclosureType},$S{disclosureSonType},$S{modName},$S{docName},$S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName},$S{updDate},$S{updTime},$S{updUserId},$S{updUserName},$S{remark})",
				DataSourceProperty.IDB,disclosureMod).getAutoId();
	}

	public UpdateResult updateDisclosureModName(SqlParam<DisclosureMod> params) throws Exception {
		return super.update("UPDATE idb_disclosure_mod SET  mod_name=$S{modName} ,remark = $S{remark},upd_date = $S{updDate},upd_time = $S{updTime},upd_user_id = $S{updUserId},upd_user_name = $S{updUserName} WHERE  id=$S{id} ",
				DataSourceProperty.IDB,params.getModel());
	}

	public UpdateResult updateDisclosureRemark(DisclosureModVersion disclosureModVersion) throws Exception {
		return super.update("UPDATE idb_disclosure_mod SET  remark = $S{remark} WHERE  id=$S{disclosureModId} ",
				DataSourceProperty.IDB,disclosureModVersion);
	}

	public UpdateResult deleteDisclosureMod(SqlParam<DisclosureMod> params) throws Exception {
		return super.update("DELETE FROM idb_disclosure_mod WHERE  id=$S{id} ",
				DataSourceProperty.IDB,params.getModel());
	}
	public UpdateResult deleteDisclosureMod(String disclosureModId) throws Exception {
		return super.update("DELETE FROM idb_disclosure_mod WHERE  id=$S{disclosureModId} ",
				DataSourceProperty.IDB,disclosureModId);
	}
	public UpdateResult deleteDisclosureModVersion(String t8DisclosureModVersion) throws Exception {
		return super.update("DELETE FROM idb_disclosure_mod_version WHERE  id=$S{t8DisclosureModVersion} ",
				DataSourceProperty.IDB,t8DisclosureModVersion);
	}
	public UpdateResult deleteDisclosureModVersionCol(String t8DisclosureModVersion) throws Exception {
		return super.update("DELETE FROM idb_disclosure_mod_column WHERE  disclosure_mod_version_id=$S{t8DisclosureModVersion} ",
				DataSourceProperty.IDB,t8DisclosureModVersion);
	}

	public Integer checkDisclosureMod(SqlParam<DisclosureMod> params) throws Exception {
		return super.findRow("select count(*) count from idb_disclosure_mod_version where 1=1 and status = '1' and disclosure_mod_id=$S{id}",
				DataSourceProperty.IDB,params.getModel()).getInteger("count");
	}
	public Integer checkDisclosureModInTask(SqlParam<DisclosureMod> params) throws Exception {
		return super.findRow("SELECT COUNT(*) `count` FROM (SELECT DISTINCT disclosure_mod_version_id FROM idb_disclosure_notice_version )notVer\n" +
						"LEFT JOIN idb_disclosure_mod_version `ver` ON notVer.disclosure_mod_version_id = ver.id\n" +
						"LEFT JOIN idb_disclosure_mod `mod`  ON ver.disclosure_mod_id=mod.id  WHERE 1=1  and mod.id=$S{id}",
				DataSourceProperty.IDB,params.getModel()).getInteger("count");
	}
	public Integer checkDisclosureModInRule(SqlParam<DisclosureMod> params) throws Exception {
		return super.findRow("SELECT COUNT(*) COUNT FROM idb_disclosure_prod_rule WHERE 1=1  AND disclosure_mod_id=$S{id}",
				DataSourceProperty.IDB,params.getModel()).getInteger("count");
	}


	public List<SqlRow> getXPType(SqlParam<DisclosureMod> params) throws Exception {
		return super.findRows("SELECT itemkey value, \n" +
						"itemval text \n" +
						"FROM sys_dict_item \n" +
						"WHERE dict = 'xp_doc_type'\n" +
						" and itemkey  in ('3', '5', '2', '10', '9')\n" +
						"ORDER BY itemkey+0",
				DataSourceProperty.IDB,params.getModel());
	}
	public List<SqlRow> queryNetBaseDate(SqlParam<DisclosureMod> params) throws Exception {
		return super.findRows("SELECT itemkey value, \n" +
						"itemval text \n" +
						"FROM sys_dict_item \n" +
						"WHERE dict = 'xp_disclosure_net_value_date'\n" +
						" and itemkey  in ('1','2','3')\n" +
						"ORDER BY itemkey+0",
				DataSourceProperty.IDB,params.getModel());
	}
	public List<SqlRow> getXPTypeInProd(SqlParam<DisclosureMod> params) throws Exception {
		return super.findRows("SELECT itemkey value, \n" +
						"itemval text \n" +
						"FROM sys_dict_item \n" +
						"WHERE dict = 'xp_doc_type'\n" +
						" and itemkey in ('3', '5', '2', '10', '9')\n" +
						"ORDER BY itemkey+0",
				DataSourceProperty.IDB,params.getModel());
	}

	public List<SqlRow> statusChangeList() throws Exception {
		return super.findRows("SELECT itemkey value, \n" +
				"itemval text \n" +
				"FROM sys_dict_item \n" +
				"WHERE dict = 'xp_disclosure_notice_status' AND itemkey  IN ('1','2') \n" +
				"ORDER BY itemkey+0",
				DataSourceProperty.IDB);
	}
	public List<SqlRow> getXPTypeByDocType(String doc_type) throws Exception {
		return super.findRows("SELECT itemkey value, \n" +
				"itemval text \n" +
				"FROM sys_dict_item \n" +
				"WHERE dict = (CASE $S{doc_type}\n" +
				"WHEN '1' THEN \n" +
				"'xp_son_stype' \n" +
				"WHEN '5' THEN \n" +
				"'xp_son_dtype' \n" +
				"WHEN '6' THEN \n" +
				"'xp_son_ztype'\n" +
				"WHEN '9' THEN \n" +
				"'xp_son_jtype'\n" +
				"END) AND itemkey NOT IN ('0504') \n" +
				"ORDER BY itemkey+0",
				DataSourceProperty.IDB,doc_type);
	}
	public Integer checkXPPrintTemp(DisclosureMod disclosureMod) throws Exception {
			StringBuilder sql = new StringBuilder("select count(t.id) cont " +
					"from idb_disclosure_mod t " +
					"where t.mod_name = $S{modName} ");

			Integer cont = super.findRow(sql.toString(),
					DataSourceProperty.IDB, disclosureMod).getInteger("cont");
		return cont;
	}

	/**
	 * 功能：检验版本名称是否重复
	 * 作者：ouyifan
	 * 日期：20220526
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Integer duplicateModName(SqlParam<DisclosureMod> params) throws Exception {
		SqlRow sqlRow = super.findRow("SELECT COUNT(*) count FROM idb_disclosure_mod WHERE mod_name = $S{modName} and id not in ($S{id})",
				DataSourceProperty.IDB,params.getModel());
		return sqlRow.getInteger("count");
	}

	public DisclosureMod getDisclosureModById(String modId) throws Exception {
		String sql = "select id,disclosure_type,disclosure_son_type,mod_name,doc_name,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,remark" +
				" from idb_disclosure_mod where id = $S{modId}";
		return super.findRow(DisclosureMod.class,sql,
				DataSourceProperty.IDB,modId);
	}

	public List<SqlRow> getXPTypeInProd2(SqlParam<DisclosureMod> params) throws Exception {
		return super.findRows("SELECT itemkey value, \n" +
						"itemval text \n" +
						"FROM sys_dict_item \n" +
						"WHERE dict = 'xp_doc_type'\n" +
						" and itemkey in ('3', '5', '2', '10', '9')\n" +
						"ORDER BY itemkey+0",
				DataSourceProperty.IDB,params.getModel());
	}
}
