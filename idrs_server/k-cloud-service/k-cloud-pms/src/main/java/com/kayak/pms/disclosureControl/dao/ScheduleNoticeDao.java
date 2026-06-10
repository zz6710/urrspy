package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeVersion;
import com.kayak.pms.disclosureControl.model.DisclosureProdTask;
import com.kayak.pms.disclosureControl.model.ScheduleNotice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ScheduleNoticeDao extends ComnDao {

	public SqlResult<ScheduleNotice> findT8DisclosureNotices(SqlParam<ScheduleNotice> params) throws Exception {
		return super.findRows("SELECT notice.id,notice.t8_prod_info_id,notice.prod_code,notice.prod_name,notice.t8_disclosure_rule_id,notice.notice_title,notice.prod_base_date," +
				" notice.publish_status,notice.approval_status,notice.eba_status,notice.review_status," +
				" notice.crt_date,notice.crt_time,notice.crt_user_id,notice.crt_user_name," +
				" notice.upd_date,notice.upd_time,notice.upd_user_id,notice.upd_user_name," +
				" notice.task_id " +
				" FROM idb_disclosure_notice notice" +
				" ", params);
	}

	/**
	 * 功能：根据查询条件查询公告数据 返回list
	 * 作者：rennannan
	 * 日期：20210609
	 *
	 * @param notice
	 * @return
	 * @throws Exception
	 */
	public List<ScheduleNotice> findNoticeList(ScheduleNotice notice) throws Exception {
		String sql = "SELECT notice.id,notice.t8_prod_info_id,notice.prod_code,notice.prod_name,notice.t8_disclosure_rule_id,notice.notice_title,notice.prod_base_date," +
				" notice.publish_status,notice.approval_status,notice.eba_status,notice.review_status," +
				" notice.crt_date,notice.crt_time,notice.crt_user_id,notice.crt_user_name," +
				" notice.upd_date,notice.upd_time,notice.upd_user_id,notice.upd_user_name," +
				" notice.task_id " +
				" FROM idb_disclosure_notice notice where 1=1";
		if (StringUtils.isNotEmpty(notice.getTaskId())) {
			sql += " and notice.task_id=$S{taskId}";
		}
		return super.findRows(ScheduleNotice.class, sql +
				" ", 0, notice);
	}

	public int addT8DisclosureNotice(SqlParam<ScheduleNotice> params) throws Exception {
		return super.update("INSERT INTO idb_disclosure_notice(id,t8_prod_info_id,prod_code,prod_name,t8_disclosure_rule_id," +
						" notice_title,prod_base_date,publish_status,approval_status,eba_status,review_status," +
						" crt_date,crt_time,crt_user_id,crt_user_name," +
						" upd_date,upd_time,upd_user_id,upd_user_name,current_stage_status,task_id) " +
						" VALUES($AUTOIDS{id},$S{prodCode},$S{prodName},$S{t8DisclosureRuleId}," +
						" $S{noticeTitle},$S{prodBaseDate},$S{publishStatus},$S{approvalStatus},$S{ebaStatus},$S{reviewStatus}," +
						" $S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName}," +
						" $S{updDate},$S{updTime},$S{updUserId},$S{updUserName},$S{currentStageStatus},$S{taskId})",
				params.getModel()).getEffect();
	}

	public int updateT8DisclosureNotice(SqlParam<ScheduleNotice> params) throws Exception {
		return super.update(" UPDATE idb_disclosure_notice " +
						"  SET " +
						" prod_code=$S{prodCode} ," +
						" prod_name=$S{prodName} ," +
						" t8_disclosure_rule_id=$S{t8DisclosureRuleId} ," +
						" notice_title=$S{noticeTitle} ," +
						" prod_base_date=$S{prodBaseDate} ," +
						" publish_status=$S{publishStatus} ," +
						" approval_status=$S{approvalStatus} ," +
						" eba_status=$S{ebaStatus} ," +
						" review_status=$S{reviewStatus} ," +
						" upd_date=$S{updDate} ," +
						" upd_time=$S{updTime} ," +
						" upd_user_id=$S{updUserId} ," +
						" upd_user_name=$S{updUserName}," +
						" current_stage_status=$S{currentStageStatus}," +
						" task_id=$S{taskId} " +
						" WHERE  id=$S{id} ",
				params.getModel()).getEffect();
	}

	public int deleteT8DisclosureNotice(SqlParam<ScheduleNotice> params) throws Exception {
		return super.update("DELETE FROM idb_disclosure_notice WHERE  id=$S{id} ",
				params.getModel()).getEffect();
	}

	/**
	 * 功能：根据id删除公告
	 * 作者：rennannan
	 * 日期：20210609
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int deleteNoticeById(String id) throws Exception {
		String sql = " delete from idb_disclosure_notice where id = $S{id}";
		return super.update(sql, id).getEffect();
	}

	/**
	 * 功能：插入公告数据 返回autoid
	 *
	 * @param notice
	 * @return
	 * @throws Exception
	 */
	public String addT8DisclosureNotice(ScheduleNotice notice) throws Exception {
		return super.update("INSERT INTO idb_disclosure_notice(id, prod_code,prod_name,disclosure_prod_rule_id,disclosure_rule_id, " +
				"notice_title, task_id, plan_fb_date, plan_sp_date, plan_bl_date, real_bl_date, " +
				"real_sp_date, real_fb_date, prod_base_date, disclosure_status, review_status, " +
				"report_start_date, report_end_date, disclosure_type, disclosure_son_type, " +
				"effect_status, file_path, file_name,is_manual_notice, crt_path, crt_date, PROD_CLC_MTH,PROD_FORM,INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_SER_CD,PROD_INV_TYP," +
				"crt_time, crt_user_id, crt_user_name) " +
				"  VALUES($AUTOIDS{id},$S{prodCode},$S{prodName},$S{disclosureProdRuleId},$S{disclosureRuleId}," +
				" $S{noticeTitle},$S{taskId},$S{planFbDate},$S{planSpDate},$S{planBlDate}," +
				" $S{realBlDate},$S{realSpDate},$S{realFbDate},$S{prodBaseDate},$S{disclosureStatus},$S{reviewStatus},$S{reportStartDate},$S{reportEndDate}," +
				" $S{disclosureType},$S{disclosureSonType},$S{effectStatus},$S{filePath},$S{fileName},$S{isManualNotice},$S{crtPath}," +
				" $S{crtDate},$S{prodClcMth},$S{prodForm},$S{invPrdDime},$S{invPrdLen},$S{prodObj},$S{prodSerCd},$S{prodInvTyp}," +
				" $S{crtTime},$S{crtUserId},$S{crtUserName})",
				DataSourceProperty.IDB,notice).getAutoId();
	}
	public UpdateResult updT8DisclosureNotice(ScheduleNotice notice) throws Exception {
		return super.update(" UPDATE idb_disclosure_notice SET notice_title=$S{noticeTitle},plan_fb_date=$S{planFbDate},plan_sp_date=$S{planSpDate},plan_bl_date=$S{planBlDate},\n" +
						" disclosure_status=$S{disclosureStatus},disclosure_prod_rule_id = $S{disclosureProdRuleId},disclosure_rule_id = $S{disclosureRuleId},report_start_date=$S{reportStartDate},report_end_date=$S{reportEndDate},\n" +
						" effect_status=$S{effectStatus},file_path=$S{filePath},file_name=$S{fileName},crt_path=$S{crtPath},upd_date=$S{updDate},\n" +
						" upd_time=$S{updTime},upd_user_id=$S{updUserId},upd_user_name=$S{updUserName},task_id = $S{taskId} WHERE id = $S{id}",
				DataSourceProperty.IDB,notice);
	}
	/**
	 * 功能：插入公告数据 返回autoid
	 *
	 * @param notice
	 * @return
	 * @throws Exception
	 */
	public UpdateResult delT8DisclosureNotice(ScheduleNotice notice) throws Exception {
		return super.update("delete from idb_disclosure_notice where id =$S{id}",
				DataSourceProperty.IDB,notice);
	}
	/**
	 * 功能：插入公告数据 返回autoid
	 *
	 * @param notice
	 * @return
	 * @throws Exception
	 */
	public UpdateResult delT8DisclosureNoticeChannel(ScheduleNotice notice) throws Exception {
		return super.update("delete from idb_disclosure_notice_channel where id in ($S{noticeChannelId})",
				DataSourceProperty.IDB,notice);
	}
	public UpdateResult delT8DisclosureNoticeVersion(ScheduleNotice notice) throws Exception {
		return super.update("delete from idb_disclosure_notice_version where id = $S{noticeVersionId} ",
				DataSourceProperty.IDB,notice);
	}
	/**
	 * 功能：插入公告数据 返回autoid
	 *
	 * @param notice
	 * @return
	 * @throws Exception
	 */
	public String find(ScheduleNotice notice) throws Exception {
		return super.update("INSERT INTO idb_disclosure_notice(id, prod_code,prod_name, disclosure_prod_rule_id, " +
				"notice_title, task_id, plan_fb_date, plan_sp_date, plan_bl_date, real_bl_date, " +
				"real_sp_date, real_fb_date, prod_base_date, disclosure_status, review_status, " +
				"report_start_date, report_end_date, disclosure_type, disclosure_son_type, " +
				"effect_status, file_path, file_name, crt_path, crt_date, " +
				"crt_time, crt_user_id, crt_user_name) " +
				"  VALUES($AUTOIDS{id},$S{prodCode},$S{prodName},$S{disclosureProdRuleId}," +
				" $S{noticeTitle},$S{taskId},$S{planFbDate},$S{planSpDate},$S{planBlDate}," +
				" $S{realBlDate},$S{realSpDate},$S{realFbDate},$S{prodBaseDate},$S{disclosureStatus},$S{reviewStatus},$S{reportStartDate},$S{reportEndDate}," +
				" $S{disclosureType},$S{disclosureSonType},$S{effectStatus},$S{filePath},$S{fileName},$S{crtPath}," +
				" $S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName})",
				DataSourceProperty.IDB,notice).getAutoId();
	}

	/**
	 * 功能：根据生成日期、信披类型删除公告数据
	 * 作者：rennannan
	 * 日期：20210602
	 *
	 * @return
	 */
	public int deleteByCrtDate(ScheduleNotice notice) throws Exception {
		StringBuilder sql = new StringBuilder(" delete from idb_disclosure_notice where crt_date=$S{crtDate} " +
				" and disclosure_type=$S{disclosureType}");
		if (StringUtils.isNotEmpty(notice.getNotInNoticeIds())) {
			sql.append(" and id not in $U{notInNoticeIds}");
		}
		return super.update(sql.toString(), notice).getEffect();
	}

	/**
	 * 功能：根据基准日期、信披类型删除公告数据
	 * 作者：rennannan
	 * 日期：20211108
	 *
	 * @return
	 */
	public int deleteByBaseDate(ScheduleNotice notice) throws Exception {
		StringBuilder sql = new StringBuilder(" delete from idb_disclosure_notice where prod_base_date=$S{prodBaseDate} " +
				" and disclosure_type=$S{disclosureType}");
		if (StringUtils.isNotEmpty(notice.getNotInNoticeIds())) {
			sql.append(" and id not in $U{notInNoticeIds}");
		}
		return super.update(sql.toString(), notice).getEffect();
	}

	/**
	 * 功能：根据信披任务id删除公告
	 * 作者：rennannan
	 * 日期：20211108
	 *
	 * @param notice
	 * @throws Exception
	 */
	public void deleteNoticeByTaskId(ScheduleNotice notice) throws Exception {
		String sql = " delete from idb_disclosure_notice where task_id = $S{taskId}";
		super.update(sql, notice);
	}

	/**
	 * 功能：根据信披规则id查询对应模板设置的字段角色
	 * 作者：rennannan
	 * 日期：20210604
	 *
	 * @return
	 */
	public List<ScheduleNotice> findRoleIdsByRuleId(String ruleId) throws Exception {
		String sql = "select roleids role_ids  from idb_disclosure_mod_column\n" +
				"     where t8_disclosure_version_id=(select t8_disclosure_version_id from idb_disclosure_prod_rule where id = $S{ruleId})\n" +
				"\t\t   and LENGTH(trim(roleids))>0\n" +
				"\t\t\t and IFNULL(roleids,'') <> ''\n" +
				"  group by roleids";
		return super.findRows(ScheduleNotice.class, sql, 0, ruleId);
	}

	/**
	 * 功能：根据产品id和角色编号查询对应的用户id
	 * 作者：rennannan
	 * 日期：20210604
	 *
	 * @param notice
	 * @return
	 */
	public List<ScheduleNotice> findUserIds(ScheduleNotice notice) throws Exception {
		String sql = "select userid_a user_id from t8_prod_user where t8_prod_info_id=$S{t8ProdInfoId} and role_id = $S{roleId}";
		return super.findRows(ScheduleNotice.class, sql, 0, notice);

	}

	/**
	 * 功能：根据信披类型和公告生成日期删除公告渠道表
	 * 作者：rennannan
	 * 日期：20211025
	 *
	 * @param notice
	 * @throws Exception
	 */
	public void deleteChannelByTypeAndDate(ScheduleNotice notice) throws Exception {
		super.update("delete from idb_disclosure_notice_channel where disclosure_notice_id in(select id from idb_disclosure_notice where disclosure_type=$S{disclosureType} and CRT_DATE=$S{crtDate})", notice);
	}

	/**
	 * 功能：根据信披类型和公告生成日期删除公告渠道表
	 * 作者：rennannan
	 * 日期：20211108
	 *
	 * @param notice
	 * @throws Exception
	 */
	public void deleteChannelByNoticeInfo(ScheduleNotice notice) throws Exception {
		super.update("delete from idb_disclosure_notice_channel where disclosure_notice_id in(select id from idb_disclosure_notice where disclosure_type=$S{disclosureType} and prod_base_date=$S{crtDate})", notice);
	}

	/**
	 * 功能：根据信披类型id删除公告渠道表
	 * 作者：rennannan
	 * 日期：20211108
	 *
	 * @param notice
	 * @throws Exception
	 */
	public void deleteChannelByTaskId(ScheduleNotice notice) throws Exception {
		super.update("delete from idb_disclosure_notice_channel where disclosure_notice_id in(select id from idb_disclosure_notice where task_id = $S{taskId})", notice);
	}
	
	public List<SqlRow> findNoticeByTaskId(String taskId) throws Exception{
		return super.findRows("select id from idb_disclosure_notice where task_id = '"+taskId+"'");
	}
	
	public SqlRow queryNotice(DisclosureProdTask task) throws Exception {
		
		return super.findRow(" select id from idb_disclosure_notice where prod_base_date = $S{prodBaseDate} and prod_code = $S{prodCode} and disclosure_son_type = $S{disclosureSonType} and disclosure_type = $S{disclosureType}",
				DataSourceProperty.IDB, task);
	}

	public List<SqlRow> findShareSortProdCode(String prodCode) throws Exception{
		String sql = "select tpss.sales_code as prodCode,tpss.sales_name as shareName from t8_prod_share_sort tpss left join t8_prod_info tpi on tpss.t8_prod_info_id = tpi.id\n" +
				"\twhere tpi.prod_code = '"+prodCode+"'";
		return super.findRows(sql);
	}
}
