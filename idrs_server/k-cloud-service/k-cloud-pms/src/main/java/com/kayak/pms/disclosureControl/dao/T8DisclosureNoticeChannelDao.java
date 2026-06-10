package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.basePublish.model.DisclosureChannel;
import com.kayak.pms.disclosureControl.model.DisclosureProdTask;
import com.kayak.pms.disclosureControl.model.ScheduleNotice;
import com.kayak.pms.disclosureControl.model.T8DisclosureNoticeChannel;
import com.kayak.pms.global.constants.XpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class T8DisclosureNoticeChannelDao extends ComnDao {

	public SqlResult<T8DisclosureNoticeChannel> findT8DisclosureNoticeChannels(SqlParam<T8DisclosureNoticeChannel> params) throws Exception {
		String sql =
				"SELECT\n"
						+ "\ttdnc.id,\n"
						+ "\ttdnc.disclosure_notice_id,\n"
						+ "\ttdn.prod_code,\n"
						+ "\ttdn.prod_name,\n"
						+ "\ttdn.notice_title,\n"
						+ "\ttdnc.version doc_version,\n"
						+ "\ttdci.channel_name,\n"
						+ "\ttdn.prod_base_date,\n"
						+ "\ttdn.disclosure_type,\n"
						+ "\ttdn.disclosure_son_type,\n"
						+ "\ttdnc.disclosure_notice_channel_id,\n"
						+ "\ttdnc.notice_channel_public_status,\n"
						+ "\ttdnc.channel_public_date,\n"
						+ "\ttdnc.create_date,\n"
						+ "\ttdnc.create_time,\n"
						+ "\ttdnc.update_date,\n"
						+ "\ttdnc.update_time,\n"
						+ "\ttdnc.create_user_id,\n"
						+ "\ttdnc.update_user_id,\n"
						+ "\ttdnc.create_user_name \n"
						+ "FROM\n"
						+ "\tidb_disclosure_notice_channel tdnc\n"
						+ "\tLEFT JOIN idb_disclosure_notice tdn ON tdn.id = tdnc.disclosure_notice_id\n"
						+ "\tLEFT JOIN idb_disclosure_channel_info tdci ON tdnc.disclosure_notice_channel_id = tdci.id \n"
						+ "WHERE notice_channel_public_status not in('0','1') and \n"
						+ "\ttdn.prod_code != '' \n"
						+ "\tAND tdn.prod_code IS NOT NULL ";
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql = sql+" and tdn.prod_code=$S{prodCode} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getDisclosureNoticeChannelId())) {
			sql = sql+" and tdnc.disclosure_notice_channel_id=$S{disclosureNoticeChannelId} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getDisclosureType())) {
			sql = sql+" and tdn.disclosure_type=$S{disclosureType} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getDisclosureSonType())) {
			sql = sql+" and tdn.disclosure_son_type=$S{disclosureSonType} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdBaseDate())) {
			sql = sql+" and tdn.prod_base_date=$S{prodBaseDate} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getNoticeChannelPublicStatus())) {
			sql = sql+" and tdnc.notice_channel_public_status=$S{noticeChannelPublicStatus} ";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdName())) {
			sql += " and tdn.prod_name like '%" + params.getModel().getProdName() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getPublicDateStart()) && StringUtils.isNotBlank(params.getModel().getPublicDateEnd())) {
			sql = sql+" and tdnc.channel_public_date between '"+params.getModel().getPublicDateStart()+"' and '"+params.getModel().getPublicDateEnd()+"' ";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addT8DisclosureNoticeChannel(SqlParam<T8DisclosureNoticeChannel> params) throws Exception {
		return super.update("INSERT INTO idb_disclosure_notice_channel(id,disclosure_notice_id,disclosure_notice_channel_id,notice_channel_public_status,channel_public_date,create_date,create_time,update_date,update_time,create_user_id,update_user_id,create_user_name) VALUES($AUTOIDS{id},$S{disclosureNoticeId},$S{disclosureNoticeChannelId},$S{noticeChannelPublicStatus},$S{channelPublicDate},$S{createDate},$S{createTime},$S{updateDate},$S{updateTime},$S{createUserId},$S{updateUserId},$S{createUserName})",
				params.getModel());
	}

	public String addT8DisclosureNoticeChannel(T8DisclosureNoticeChannel params) throws Exception {
		return super.update("INSERT INTO idb_disclosure_notice_channel\n" +
						"(id,disclosure_notice_id,disclosure_notice_version_id,disclosure_notice_channel_id,upload_file_name,notice_channel_public_status,channel_public_date,create_date,create_time,create_user_id,create_user_name) VALUES\n" +
						"($AUTOIDS{id},$S{disclosureNoticeId},$S{disclosureNoticeVersionId},$S{disclosureNoticeChannelId},$S{uploadFileName},$S{noticeChannelPublicStatus},$S{channelPublicDate},$S{createDate},$S{createTime},$S{createUserId},$S{createUserName})",
				DataSourceProperty.IDB,params).getAutoId();
	}
	public SqlRow findDisInfo(DisclosureProdTask prodTask) throws Exception {
		return super.findRow("SELECT rule.notice_title,dmv.version,dmv.doc_name,rule.id ruleId," +
						" rule.if_condition," +
						" CASE " +
						" IFNULL(dmct.count,'0') " +
						" WHEN '0' " +
						" THEN '0' " +
						" ELSE '1' " +
						" END if_clearing," +
						"rule.channel_ids,rule.disclosure_mod_version_id,rule.disclosure_mod_id,rule.notice_roleid," +
						"task.sys_crt_date," +
						"rule.exp_create_rule,rule.exp_create_days," +
						"rule.exp_supplement_rule,rule.exp_supplement_days,rule.exp_approval_rule,rule.exp_approval_days,rule.exp_publish_rule,rule.exp_publish_days " +
						"FROM (SELECT * FROM idb_disclosure_prod_task WHERE 1=1 " +
						"AND disclosure_type =$S{disclosureType} " +
						"AND disclosure_son_type =$S{disclosureSonType} " +
						"AND prod_code =$S{prodCode}" +
						"AND prod_base_date =$S{prodBaseDate}) task  " +
						"LEFT JOIN idb_disclosure_prod_rule rule  " +
						"ON rule.disclosure_type =task.disclosure_type " +
						"AND rule.disclosure_son_type =task.disclosure_son_type " +
						"AND rule.prod_code =task.prod_code " +
						" LEFT JOIN idb_disclosure_mod_version dmv ON rule.disclosure_mod_version_id = dmv.id  " +
						" LEFT JOIN  " +
						" (SELECT DISTINCT disclosure_mod_version_id,COUNT(*) `count` FROM idb_disclosure_mod_column   " +
						" WHERE is_sysvalue = '2'  GROUP BY disclosure_mod_version_id)dmct  " +
						" ON dmv.id = dmct.disclosure_mod_version_id ",
				DataSourceProperty.IDB,prodTask);
	}
	public SqlRow findDisInfoZT(DisclosureProdTask Task) throws Exception {
		//TODO sql调整
		return super.findRow("SELECT rule.notice_title,dmv.version,dmv.doc_name,rule.id ruleId, " +
						"rule.if_condition," +
						" CASE  " +
						" IFNULL(dmct.count,'0') " +
						" WHEN '0' " +
						" THEN '0' " +
						" ELSE '1' " +
						" END if_clearing," +
						"rule.disclosure_mod_version_id,rule.disclosure_mod_id,rule.notice_roleid, " +
						"task.sys_crt_date,rule.exp_create_rule,rule.exp_create_days, " +
						"rule.exp_supplement_rule,rule.exp_supplement_days,rule.exp_approval_rule,rule.exp_approval_days,rule.exp_publish_rule,rule.exp_publish_days " +
						"FROM (SELECT * FROM idb_disclosure_prod_task  WHERE 1=1 " +
						"AND disclosure_type =$S{disclosureType} " +
						"AND disclosure_son_type =$S{disclosureSonType} " +
						"AND PROD_CLC_MTH =$S{prodClcMth} " +
						"AND PROD_FORM =$S{prodForm} " +
						"AND INV_PRD_DIME =$S{invPrdDime} " +
						"AND INV_PRD_LEN =$S{invPrdLen} " +
						"AND PROD_OBJ =$S{prodObj} " +
						"AND PROD_SER_CD =$S{prodSerCd} " +
						"AND prod_inv_typ =$S{prodInvTyp} " +
						"AND prod_base_date = $S{prodBaseDate}) task " +
						"LEFT JOIN idb_disclosure_rule rule  " +
						"ON rule.disclosure_type =task.disclosure_type " +
						"AND rule.disclosure_son_type =task.disclosure_son_type " +
						"AND rule.PROD_CLC_MTH =task.PROD_CLC_MTH " +
						"AND rule.PROD_FORM =task.PROD_FORM " +
						"AND rule.INV_PRD_DIME =task.INV_PRD_DIME " +
						"AND rule.INV_PRD_LEN =task.INV_PRD_LEN " +
						"AND rule.PROD_OBJ =task.PROD_OBJ " +
						"AND rule.PROD_SER_CD =task.PROD_SER_CD " +
						"AND rule.prod_inv_typ =task.prod_inv_typ " +
						" LEFT JOIN idb_disclosure_mod_version dmv ON rule.disclosure_mod_version_id = dmv.id  " +
						" LEFT JOIN  " +
						" (SELECT DISTINCT disclosure_mod_version_id,COUNT(*) `count` FROM idb_disclosure_mod_column   " +
						" WHERE is_sysvalue = '2'  GROUP BY disclosure_mod_version_id)dmct  " +
						" ON dmv.id = dmct.disclosure_mod_version_id ",
				DataSourceProperty.IDB,Task);
	}
	public String findChannelInfoZT(DisclosureProdTask Task) throws Exception {
		String sql = "SELECT GROUP_CONCAT(channel_ids) channel_ids FROM idb_disclosure_channel_rule WHERE 1=1 AND disclosure_type = $S{disclosureType} AND status = '"+ XpStatus.start.getItemKey() +"' ";

		if (StringUtils.isNotBlank(Task.getDisclosureSonType())) {
			sql = sql+" AND disclosure_son_type = $S{disclosureSonType} ";
		}
		if (StringUtils.isNotBlank(Task.getProdClcMth())) {
			sql = sql+"AND PROD_CLC_MTH =$S{prodClcMth}\n";
		}
//		if (StringUtils.isNotBlank(Task.getProdForm())) {
//			sql = sql+"AND PROD_FORM =$S{prodForm}\n";
//		}
		if (StringUtils.isNotBlank(Task.getInvPrdDime())) {
			sql = sql+"AND INV_PRD_DIME =$S{invPrdDime}\n";
		}
		if (StringUtils.isNotBlank(Task.getInvPrdLen())) {
			sql = sql+"AND INV_PRD_LEN =$S{invPrdLen}\n";
		}
		if (StringUtils.isNotBlank(Task.getProdObj())) {
			sql = sql+"AND PROD_OBJ =$S{prodObj}\n";
		}
		if (StringUtils.isNotBlank(Task.getProdSerCd())) {
			sql = sql+"AND PROD_SER_CD =$S{prodSerCd}\n";
		}
		if (StringUtils.isNotBlank(Task.getProdInvTyp())) {
			sql = sql+"AND PROD_INV_TYP =$S{prodInvTyp}\n";
		}
		return super.findRow(sql,
				DataSourceProperty.IDB,Task).getString("channel_ids");
	}
	public SqlRow checkExistNoticeId(ScheduleNotice Notice) throws Exception {
		String sql = "SELECT id noticeId from idb_disclosure_notice where 1=1\n";
		if (StringUtils.isNotBlank(Notice.getDisclosureType())) {
			sql = sql+" AND disclosure_type = $S{disclosureType}\n";
		}
		if (StringUtils.isNotBlank(Notice.getDisclosureSonType())) {
			sql = sql+"AND disclosure_son_type =$S{disclosureSonType}\n";
		}
		if (StringUtils.isNotBlank(Notice.getProdBaseDate())) {
			sql = sql+"AND prod_base_date =$S{prodBaseDate}\n";
		}
		if (StringUtils.isNotBlank(Notice.getProdCode())) {
			sql = sql+"AND prod_code =$S{prodCode}\n";
		}
		if (StringUtils.isNotBlank(Notice.getProdClcMth())) {
			sql = sql+"AND PROD_CLC_MTH =$S{prodClcMth}\n";
		}
		if (StringUtils.isNotBlank(Notice.getProdForm())) {
			sql = sql+"AND PROD_FORM =$S{prodForm}\n";
		}
		if (StringUtils.isNotBlank(Notice.getInvPrdDime())) {
			sql = sql+"AND INV_PRD_DIME =$S{invPrdDime}\n";
		}
		if (StringUtils.isNotBlank(Notice.getInvPrdLen())) {
			sql = sql+"AND INV_PRD_LEN =$S{invPrdLen}\n";
		}
		if (StringUtils.isNotBlank(Notice.getProdObj())) {
			sql = sql+"AND PROD_OBJ =$S{prodObj}\n";
		}
		if (StringUtils.isNotBlank(Notice.getProdSerCd())) {
			sql = sql+"AND PROD_SER_CD =$S{prodSerCd}\n";
		}
		if (StringUtils.isNotBlank(Notice.getProdInvTyp())) {
			sql = sql+"AND PROD_INV_TYP =$S{prodInvTyp}\n";
		}
		sql = sql + " limit 1 ";
		return super.findRow(sql,
				DataSourceProperty.IDB,Notice);
	}
	public Integer checkTaskStatus(String taskId) throws Exception {
		Integer count =super.findRows("SELECT count(*) count  from idb_disclosure_prod_task WHERE id =$S{taskId} and status in ('1')",
				DataSourceProperty.IDB,taskId).get(0).getInteger("count");
		return count;
	}
	public UpdateResult updateT8DisclosureNoticeChannel(SqlParam<T8DisclosureNoticeChannel> params) throws Exception {
		return super.update("UPDATE idb_disclosure_notice_channel SET disclosure_notice_id=$S{disclosureNoticeId} ,disclosure_notice_channel_id=$S{disclosureNoticeChannelId} ,notice_channel_public_status=$S{noticeChannelPublicStatus} ,channel_public_date=$S{channelPublicDate} ,create_date=$S{createDate} ,create_time=$S{createTime} ,update_date=$S{updateDate} ,update_time=$S{updateTime} ,create_user_id=$S{createUserId} ,update_user_id=$S{updateUserId} ,create_user_name=$S{createUserName}  WHERE  id=$S{id} ",
				params.getModel());
	}

	public List<T8DisclosureNoticeChannel> findAllNoticeChannel(String versionId) throws Exception {
		String sql = " SELECT * FROM idb_disclosure_notice_channel WHERE disclosure_notice_version_id = '"+versionId+"' ";
		return super.findRows(T8DisclosureNoticeChannel.class,sql,
				DataSourceProperty.IDB, versionId);
	}

	public UpdateResult deleteT8DisclosureNoticeChannel(SqlParam<T8DisclosureNoticeChannel> params) throws Exception {
		return super.update("DELETE FROM idb_disclosure_notice_channel WHERE  id=$S{id} ",
				params.getModel());
	}

}
