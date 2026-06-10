package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.disclosureControl.disclousreEnum.DisclosureTypeEnum;
import com.kayak.pms.disclosureControl.disclousreEnum.OperationTypeEnum;
import com.kayak.pms.disclosureControl.model.DisclosureNotice;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeProcess;
import com.kayak.pms.disclosureControl.model.DisclosureOperation;
import com.kayak.utils.DateHelper;
import com.kayak.pms.global.constants.ApprovalStatus;
import com.kayak.pms.global.constants.DisclosureStatus;
import com.spire.ms.System.Collections.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class DisclosureNoticeDao extends ComnDao {

	@Autowired
	private DisclosureOperationDao disclosureOperationDao;

	public SqlResult<DisclosureNotice>  findDisclosureNotices(SqlParam<DisclosureNotice> params) throws Exception {
		StringBuilder sql =
				new StringBuilder(
						"SELECT dn.id, dnv.id notice_version_id, dn.disclosure_type, dn.disclosure_son_type, po.prod_nm prod_name, dn.prod_code prod_code, " +
								"  dr.t8_disclosure_rule_id, dm.id AS t8_disclosure_mod_id, dmv.id AS t8_disclosure_mod_version_id, " +
								"  dn.report_type, dn.notice_title, " +
								"  dn.PROD_CLC_MTH, " +
								"  dn.PROD_FORM, " +
								"  dn.INV_PRD_DIME, " +
								"  dn.INV_PRD_LEN, " +
								"  dn.PROD_OBJ, " +
								"  dn.PROD_SER_CD, " +
								"  sr.PROD_SER_NM, " +
								"  dn.task_id, dn.plan_fb_date, dn.plan_sp_date, dn.plan_bl_date, dn.real_bl_date, dn.real_sp_date, dn.real_fb_date, " +
								"  dn.prod_base_date, dn.disclosure_status, dn.review_status, dn.report_start_date, dn.report_end_date, dn.is_send_email, " +
								"  dn.effect_status, dnv.file_name, dnv.file_path, dnv.crt_path, dn.crt_date, dn.crt_time, dn.crt_user_id, dn.crt_user_name, " +
								"  dn.upd_date, dn.upd_time, dn.upd_user_id, dn.upd_user_name, " +
								"  SUBSTR(dmv.doc_name,LOCATE('.',dmv.doc_name)) suffix," +
								"  dm.mod_name, dmv.version mod_version, dnv.notice_version disclosure_version,po.dms_trst_org_nm trustee_name " +
								"  FROM idb_disclosure_notice dn " +
								"  LEFT JOIN APP_PRD_BAS_INF po ON po.prod_cd = dn.prod_code " +
								"  LEFT JOIN idb_disclosure_notice_version dnv ON dnv.t8_disclosure_notice_id = dn.id " +
								"  LEFT JOIN idb_disclosure_mod_version dmv ON dmv.id = dnv.disclosure_mod_version_id " +
								"  LEFT JOIN idb_disclosure_mod dm ON dm.id = dmv.disclosure_mod_id " +
								"  LEFT JOIN idb_disclosure_prod_rule dr ON dr.id = dn.disclosure_prod_rule_id " +
								"  LEFT JOIN (SELECT DISTINCT k.PROD_SER_CD,k.PROD_SER_NM FROM APP_PRD_BAS_INF k) sr ON dn.PROD_SER_CD = sr.PROD_SER_CD" +
								"  WHERE 1 = 1 and (is_manual_notice!='1' or is_manual_notice is null) and dnv.id in (select max(CONVERT ( dnv.id, SIGNED )) notice_version_id  from idb_disclosure_notice_version dnv group by dnv.t8_disclosure_notice_id) " );
		if (StringUtils.isNotBlank(params.getModel().getDisclosureType())) {
			sql.append(" and dn.disclosure_type = '" + params.getModel().getDisclosureType() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getDisclosureSonType())) {
			sql.append(" and dn.disclosure_son_type = '" + params.getModel().getDisclosureSonType() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdName())) {
			sql.append(" and po.prod_nm like '%" + params.getModel().getProdName() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql.append(" and po.prod_cd like '%" + params.getModel().getProdCode() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdBaseDate())) {
			sql.append(" and dn.prod_base_date = '" + params.getModel().getProdBaseDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getPlanFbDate())) {
			sql.append(" and dn.plan_fb_date = '" + params.getModel().getPlanFbDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getRealFbDate())) {
			sql.append(" and dn.real_fb_date = '" + params.getModel().getRealFbDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getDisclosureStatus())) {
			sql.append(" and dn.disclosure_status = '" + params.getModel().getDisclosureStatus() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getEffectStatus())) {
			sql.append(" and dn.effect_status = '" + params.getModel().getEffectStatus() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getReviewStatus())) {
			sql.append(" and dn.review_status = '" + params.getModel().getReviewStatus() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getId())) {
			sql.append(" and dn.id = '" + params.getModel().getId() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getTrusteeName())) {
			sql.append(" and po.dms_trst_org_nm like '%" + params.getModel().getTrusteeName() + "%'");
		}
		sql.append(" order by dn.crt_date desc,dn.crt_time desc");
		return super.findRows(sql.toString(), DataSourceProperty.IDB, params);
	}

	public SqlResult<DisclosureNotice> findTrusteeName(SqlParam<DisclosureNotice> params) throws Exception {
		return super.findRows("SELECT distinct trustee_name FROM ods_amng_trustee_acco_info ", params);
	}
	public SqlResult<DisclosureNotice>  findDisclosureNDetails(SqlParam<DisclosureNotice> params,String nowPlanDate,String nextPlanDate) throws Exception {
		// 0 今日 1 延期  2 明日
		String disclosureFlag=params.getModel().getDisclosureFlag();
		StringBuilder sql = new StringBuilder("");
		 if("1".equals(disclosureFlag)){
			sql.append("select d.disclosure_type,d.plan_fb_date,d.disclosure_son_type,d.disclosure_status,count(*) disclosure_count from idb_disclosure_notice d where 1=1 and d.plan_fb_date <'").append(nowPlanDate).append("'");
		}else if("2".equals(disclosureFlag)){
			sql.append("select d.disclosure_type,d.plan_fb_date,d.disclosure_son_type,d.disclosure_status,count(*) disclosure_count from idb_disclosure_notice d where 1=1 and d.plan_fb_date ='").append(nextPlanDate).append("' ");
		}else{
			sql.append("select d.disclosure_type,d.plan_fb_date,d.disclosure_son_type,d.disclosure_status,count(*) disclosure_count from idb_disclosure_notice d where 1=1 and d.plan_fb_date ='").append(nowPlanDate).append("' ");
		}
		sql.append("group by d.disclosure_status,d.disclosure_type,d.disclosure_son_type,d.plan_fb_date");
		return super.findRows(sql.toString(), DataSourceProperty.IDB, params);
	}

	//获取当期日期的下一个工作日
	public SqlRow getNextWorkday(String currentDay,String pgmno) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("pgmno",pgmno);
		params.put("currentDay",currentDay);
		String sql = "SELECT min(workday) workday FROM sys_workday_set WHERE pgmno = $S{pgmno} and workday > $S{currentDay} order by workday";
		return super.findRow(sql, params);
	}

	//获取当期日期的下一个工作日
	public SqlRow getPreviousWorkday(String currentDay,String pgmno) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("pgmno",pgmno);
		params.put("currentDay",currentDay);
		String sql = "SELECT  max(workday) workday FROM sys_workday_set WHERE pgmno = $S{pgmno} and workday < $S{currentDay} order by workday";
		return super.findRow(sql, params);
	}
	public UpdateResult addDisclosureNotice(SqlParam<DisclosureNotice> params) throws Exception {
		params.getModel().setCrtUserId((String) SysUtil.getSysUserParamValue("sys_user_userid"));
		params.getModel().setCrtUserName((String)SysUtil.getSysUserParamValue("sys_user_username"));
		return super.update("INSERT INTO idb_disclosure_notice(id,t8_prod_info_id,prod_code,prod_name,t8_disclosure_rule_id,report_type,notice_title,plan_fb_date,plan_sp_date,plan_bl_date,real_bl_date,real_sp_date,real_fb_date,stage,prod_base_date,publish_status,approval_status,eba_status,review_status,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name) VALUES($AUTOIDS{id},$S{t8ProdInfoId},$S{prodCode},$S{prodName},$S{t8DisclosureRuleId},$S{reportType},$S{noticeTitle},$S{planFbDate},$S{planSpDate},$S{planBlDate},$S{realBlDate},$S{realSpDate},$S{realFbDate},$S{stage},$S{prodBaseDate},$S{publishStatus},$S{approvalStatus},$S{ebaStatus},$S{reviewStatus},$S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName},$S{updDate},$S{updTime},$S{updUserId},$S{updUserName})",
				DataSourceProperty.IDB, params.getModel());
	}

	public UpdateResult updateDisclosureNotice(SqlParam<DisclosureNotice> params) throws Exception {
		return super.update("UPDATE idb_disclosure_notice SET t8_prod_info_id=$S{t8ProdInfoId} ,prod_code=$S{prodCode} ,prod_name=$S{prodName} ,t8_disclosure_rule_id=$S{t8DisclosureRuleId} ,report_type=$S{reportType},notice_title=$S{noticeTitle} ,plan_fb_date=$S{planFbDate} ,plan_sp_date=$S{planSpDate} ,plan_bl_date=$S{planBlDate} ,real_bl_date=$S{realBlDate} ,real_sp_date=$S{realSpDate} ,real_fb_date=$S{realFbDate} ,stage=$S{stage} ,prod_base_date=$S{prodBaseDate} ,publish_status=$S{publishStatus} ,approval_status=$S{approvalStatus} ,eba_status=$S{ebaStatus} ,review_status=$S{reviewStatus} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,crt_user_id=$S{crtUserId} ,crt_user_name=$S{crtUserName} ,upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user_id=$S{updUserId} ,upd_user_name=$S{updUserName}  WHERE  id=$S{id} ",
				DataSourceProperty.IDB, params.getModel());
	}

	public UpdateResult updateDisclosureNoticeForDelete(SqlParam<DisclosureNotice> params) throws Exception {
		return super.update("UPDATE idb_disclosure_notice SET delete_flag='1'  WHERE  id=$S{id} ",
				DataSourceProperty.IDB, params.getModel());
	}

	public UpdateResult updateDefaultManger(DisclosureNotice params) throws Exception {
		return super.update("UPDATE idb_disclosure_notice SET default_manger_id=$S{defaultMangerId} ,default_manger_name=$S{defaultMangerName} WHERE  id=$S{id} ", DataSourceProperty.IDB, params);
	}

	public UpdateResult updateDisclosureNoticeChannel(SqlParam<DisclosureNotice> params) throws Exception {

		super.update("update idb_disclosure_notice set current_stage_status='1',publish_status='2' where id=$S{id}", DataSourceProperty.IDB, params.getModel());

		//TODO 修改每个渠道的状态

		return null;
	}

	public UpdateResult updateNoticeStatus(DisclosureNotice notice) throws Exception {
		String updUserId = (String) SysUtil.getSysUserParamValue("sys_user_userid");
		String updUserName = (String) SysUtil.getSysUserParamValue("sys_user_username");
		String date = DateUtil.getNowDate();
		String time = DateUtil.getNowTime();
		return super.update("update idb_disclosure_notice set current_stage_status=$S{currentStageStatus},stage=$S{stage} ,upd_date = '"+date+"', upd_time = '"+time+"', upd_user_id = '"+updUserId+"', upd_user_name = '"+updUserName+"'  where id=$S{id}", DataSourceProperty.IDB, notice);
	}

	public UpdateResult updateNoticeStageStatus(DisclosureNotice notice) throws Exception {
		String updUserId = (String) SysUtil.getSysUserParamValue("sys_user_userid");
		String updUserName = (String) SysUtil.getSysUserParamValue("sys_user_username");
		String date = DateUtil.getNowDate();
		String time = DateUtil.getNowTime();
		return super.update("update idb_disclosure_notice set current_stage_status=$S{currentStageStatus},stage=$S{stage} ,upd_date = '"+date+"', upd_time = '"+time+"', upd_user_id = '"+updUserId+"', upd_user_name = '"+updUserName+"' where id=$S{id}", DataSourceProperty.IDB, notice);
	}

	public Integer updateDisNoticeProcessByRoleIds1(DisclosureNoticeProcess params) throws Exception {
		return super.update("UPDATE idb_disclosure_notice_process SET input_status=$S{inputStatus} ,upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user_id=$S{updUserId} WHERE  t8_disclosure_notice_id=$S{t8DisclosureNoticeId} and role_id = $U{roleId} ",
				DataSourceProperty.IDB, params).getEffect();
	}

	public SqlRow queryNoticeBystatus(DisclosureNotice params) throws Exception {
		return super.findRow("select id from  idb_disclosure_notice where stage='3' and current_stage_status= '11'  and id=$S{id} ",
				DataSourceProperty.IDB, params);
	}


	public UpdateResult updateFinishStatus(DisclosureNotice params) throws Exception {
		return super.update("UPDATE idb_disclosure_notice SET stage='4' ,current_stage_status= '15',approval_status='2' WHERE id=$S{id} ",
				DataSourceProperty.IDB, params);
	}

	/**
	 * @param params t8_disclosure_notice表的id
	 * @return
	 * @throws Exception 变更当前阶段为公告发布，发布状态变更为待发布
	 */
	public UpdateResult updateStatus(DisclosureNotice params) throws Exception {
		return super.update("UPDATE idb_disclosure_notice SET stage='4' ,publish_status='1' ,upd_date = $S{updDate}, upd_time = $S{updTime}, upd_user_id = $S{updUserId}, upd_user_name = $S{updUserName} WHERE id=$S{id} ",
				DataSourceProperty.IDB, params);
	}

	public UpdateResult updateDisclosureNoticeStatus(SqlParam<DisclosureNotice> params) throws Exception {

		super.update("update idb_disclosure_notice set stage='2',current_stage_status='0',approval_status='1' where id=$S{id}", DataSourceProperty.IDB, params.getModel());
		List<SqlRow> list2 = super.findRows("SELECT tdr.notice_roleid,tpu.userid_a,tdn.id FROM idb_disclosure_notice tdn " +
				"LEFT JOIN idb_disclosure_prod_rule tdpr ON tdn.t8_disclosure_rule_id=tdpr.id " +
				"LEFT JOIN idb_disclosure_rule tdr ON tdpr.t8_disclosure_rule_id=tdr.id " +
				"WHERE tdn.id='" + params.getModel().getId() + "' AND tdn.prod_code='" + params.getModel().getProdCode() + "'", DataSourceProperty.IDB);
		String userId = SysUtil.getSysUserParamValue("sys_user_userid").toString();
		String date = DateHelper.getCurrentDate();
		String time = DateHelper.getCurrentTime();
		if (list2 != null && list2.size() > 0) {
			for (int i = 0; i < list2.size(); i++) {
				String userId2 = list2.get(i).getString("userid_a");
				if (userId.equals(userId2)) {
					DisclosureOperation operation = new DisclosureOperation();
					operation.setDealId(list2.get(i).getString("id"));
					operation.setOperationType(OperationTypeEnum.SEVEN.getVal());
					operation.setDisclosureType(DisclosureTypeEnum.FIVE.getVal());
					operation.setUserid(userId2);
					operation.setRoleid(list2.get(i).getString("notice_roleid"));
					operation.setEndDate(date);
					operation.setEndTime(time);
					operation.setStatus("1");
					operation.setDealTable("idb_disclosure_notice");
					//修改首页待办状态
					disclosureOperationDao.updateDisclosureOperation(operation);
				}
			}
		}
		return null;
	}

	public UpdateResult updateDisclosureNoticeStatus2(SqlParam<DisclosureNotice> params) throws Exception {

		super.update("update idb_disclosure_notice set stage='2',current_stage_status='0',approval_status='2' where id=$S{id}", DataSourceProperty.IDB, params.getModel());
		List<SqlRow> list2 = super.findRows("SELECT tdr.notice_roleid,tpu.userid_a,tdn.id FROM idb_disclosure_notice tdn " +
				"LEFT JOIN idb_disclosure_prod_rule tdpr ON tdn.t8_disclosure_rule_id=tdpr.id " +
				"LEFT JOIN idb_disclosure_rule tdr ON tdpr.t8_disclosure_rule_id=tdr.id " +
				"WHERE tdn.id='" + params.getModel().getId() + "' AND tdn.prod_cpde='" + params.getModel().getProdCode() + "'", DataSourceProperty.IDB);
		String userId = SysUtil.getSysUserParamValue("sys_user_userid").toString();
		String date = DateHelper.getCurrentDate();
		String time = DateHelper.getCurrentTime();
		if (list2 != null && list2.size() > 0) {
			for (int i = 0; i < list2.size(); i++) {
				String userId2 = list2.get(i).getString("userid_a");
				if (userId.equals(userId2)) {
					DisclosureOperation operation = new DisclosureOperation();
					operation.setDealId(list2.get(i).getString("id"));
					operation.setOperationType(OperationTypeEnum.SEVEN.getVal());
					operation.setDisclosureType(DisclosureTypeEnum.FIVE.getVal());
					operation.setUserid(userId2);
					operation.setRoleid(list2.get(i).getString("notice_roleid"));
					operation.setEndDate(date);
					operation.setEndTime(time);
					operation.setStatus("1");
					operation.setDealTable("idb_disclosure_notice");
					//修改首页待办状态
					disclosureOperationDao.updateDisclosureOperation(operation);
				}
			}
		}
		return null;
	}

	public UpdateResult updateDisclosureNoticeStatusAgree(DisclosureNotice params) throws Exception {
		//审批通过后修改公告状态,stage='4'--将当前阶段改为公告发布,current_stage_status='15'当前阶段状态改为待发布,approval_status='2'状态改为审批通过,并变更默认经理id以及姓名
		//UpdateResult update = super.update("update idb_disclosure_notice set current_stage_status='15',stage='4',approval_status='2',default_manger_id=$S{defaultMangerId},default_manger_name=$S{defaultMangerName} where id=$S{id} ", params);

		String updUserId = (String) SysUtil.getSysUserParamValue("sys_user_userid");
		String updUserName = (String) SysUtil.getSysUserParamValue("sys_user_username");
		String date = DateHelper.getCurrentDate();
		String time = DateHelper.getCurrentTime();
		UpdateResult update = super.update("update idb_disclosure_notice set current_stage_status='15',stage='4',approval_status='2',upd_date = '"+date+"', upd_time = '"+time+"', upd_user_id = '"+updUserId+"', upd_user_name = '"+updUserName+"' where id=$S{id} ", params);
		if (update.getEffect() > 0) {
			List<SqlRow> list2 = super.findRows("SELECT tdr.notice_roleid,tpu.userid_a,tdn.id FROM idb_disclosure_notice tdn " +
					"LEFT JOIN idb_disclosure_prod_rule tdpr ON tdn.t8_disclosure_rule_id=tdpr.id " +
					"LEFT JOIN idb_disclosure_rule tdr ON tdpr.t8_disclosure_rule_id=tdr.id " +
					"WHERE tdn.id='" + params.getId() + "' AND tdn.prod_code='" + params.getProdCode() + "'", DataSourceProperty.IDB);
			/*将首页待办设置为已办*/
			if (list2 != null && list2.size() > 0) {

				for (SqlRow sqlRow : list2) {
					String userId2 = sqlRow.getString("userid_a");
					DisclosureOperation operation = new DisclosureOperation();
					operation.setDealId(params.getId());
					operation.setOperationType(OperationTypeEnum.SEVEN.getVal());
					operation.setDisclosureType(DisclosureTypeEnum.FIVE.getVal());
					operation.setUserid(userId2);
					operation.setRoleid(sqlRow.getString("notice_roleid"));
					operation.setCrtDate(date);
					operation.setCrtTime(time);
					operation.setStatus("1");
					//修改首页待办状态
					disclosureOperationDao.updateDisclosureOperation(operation);
				}
			}
		}
		return update;
	}

	public UpdateResult updateDisclosureNoticeStatusReject(DisclosureNotice params) throws Exception {
		//修改当前阶段状态为已拒绝,审批状态为审批拒绝
		super.update("update idb_disclosure_notice set current_stage_status='4',approval_status='3'  where id=$S{id}", params);
		List<SqlRow> list2 = super.findRows("SELECT tdr.notice_roleid,tpu.userid_a,tdn.id FROM idb_disclosure_notice tdn " +
				"LEFT JOIN idb_disclosure_prod_rule tdpr ON tdn.t8_disclosure_rule_id=tdpr.id " +
				"LEFT JOIN idb_disclosure_rule tdr ON tdpr.t8_disclosure_rule_id=tdr.id " +
				"WHERE tdn.id='" + params.getId() + "' AND tdn.prod_code='" + params.getProdCode() + "'", DataSourceProperty.IDB);

		String date = DateHelper.getCurrentDate();
		String time = DateHelper.getCurrentTime();
		if (list2 != null && list2.size() > 0) {
			for (SqlRow sqlRow : list2) {
				String userId2 = sqlRow.getString("userid_a");
				DisclosureOperation operation = new DisclosureOperation();
				operation.setDealId(sqlRow.getString("id"));
				operation.setProdCode(params.getProdCode());
				operation.setOperationType(OperationTypeEnum.SEVEN.getVal());
				operation.setDisclosureType(DisclosureTypeEnum.FIVE.getVal());
				operation.setUserid(userId2);
				operation.setRoleid(sqlRow.getString("notice_roleid"));
				operation.setEndDate(date);
				operation.setEndTime(time);
				operation.setStatus("1");
				operation.setDealTable("idb_disclosure_notice");
				//将发起审批的待办设置为已办
				disclosureOperationDao.updateDisclosureOperation(operation);
				operation.setStatus("0");
				operation.setOperationType(OperationTypeEnum.THREE.getVal());
				//插入补录分发的待办
				disclosureOperationDao.insertDisOperation(operation);

			}
		}
		return null;
	}

	public UpdateResult updateDisclosureNoticeStatusStart(DisclosureNotice params) throws Exception {
		//审批发起后,当前阶段应为公告审核，当前状态应为审核中，审批状态应为已发起
		return super.update("update idb_disclosure_notice set current_stage_status='2',stage='2',approval_status='1'  where id=$S{id}", DataSourceProperty.IDB, params);
	}

	public UpdateResult updateDisclosureNoticeTrusteeStatusReject(DisclosureNotice params) throws Exception {
		List<SqlRow> list2 = super.findRows("SELECT tdr.notice_roleid,tpu.userid_a,tdn.id FROM idb_disclosure_notice tdn " +
				"LEFT JOIN idb_disclosure_prod_rule tdpr ON tdn.t8_disclosure_rule_id=tdpr.id " +
				"LEFT JOIN idb_disclosure_rule tdr ON tdpr.t8_disclosure_rule_id=tdr.id " +
				"WHERE tdn.id='" + params.getId() + "' AND tdn.prodCode='" + params.getProdCode() + "'", DataSourceProperty.IDB);

		String date = DateHelper.getCurrentDate();
		String time = DateHelper.getCurrentTime();
		if (list2 != null && list2.size() > 0) {
			for (SqlRow sqlRow : list2) {
				String userId2 = sqlRow.getString("userid_a");
				DisclosureOperation operation = new DisclosureOperation();
				operation.setDealId(sqlRow.getString("id"));
				operation.setProdCode(params.getProdCode());
				operation.setOperationType(OperationTypeEnum.EIGHT.getVal());
				operation.setDisclosureType(DisclosureTypeEnum.FIVE.getVal());
				operation.setUserid(userId2);
				operation.setRoleid(sqlRow.getString("notice_roleid"));
				operation.setEndDate(date);
				operation.setEndTime(time);
				operation.setStatus("1");
				operation.setDealTable("idb_disclosure_notice");
				//将发送托管行的待办设置为已办
				disclosureOperationDao.updateDisclosureOperation(operation);
				operation.setStatus("0");
				operation.setOperationType(OperationTypeEnum.SIX.getVal());
				//插入补录分发的待办
				disclosureOperationDao.insertDisOperation(operation);

			}
		}
		return null;
	}

	public UpdateResult updateDisclosureNoticeTrusteeStatusAgree(DisclosureNotice params) throws Exception {
		super.update("update idb_disclosure_notice set review_status='1' where id=$S{id}", params);
		super.update("update idb_disclosure_trutee_approval set recheck_status='1' where id=$S{id}", params);
		List<SqlRow> list2 = super.findRows("SELECT tdr.notice_roleid,tpu.userid_a,tdn.id FROM idb_disclosure_notice tdn " +
				"LEFT JOIN idb_disclosure_prod_rule tdpr ON tdn.t8_disclosure_rule_id=tdpr.id " +
				"LEFT JOIN idb_disclosure_rule tdr ON tdpr.t8_disclosure_rule_id=tdr.id " +
				"WHERE tdn.id='" + params.getId() + "' AND tdn.prodCode='" + params.getProdCode() + "'",  DataSourceProperty.IDB);
		return null;
	}

	public UpdateResult deleteDisclosureNotice(SqlParam<DisclosureNotice> params) throws Exception {
		return super.update("DELETE FROM idb_disclosure_notice WHERE  id=$S{id} ", DataSourceProperty.IDB, params.getModel());
	}

	/**
	 * 功能：查询信披渠道规则列表
	 * 作者：rennannan
	 * 日期：20210511
	 *
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public SqlResult<DisclosureNotice> findChannelRuleList(SqlParam<DisclosureNotice> param) throws Exception {
		String sql = "SELECT id,channel_name,channel_code,emails FROM idb_disclosure_channel_info tdci WHERE FIND_IN_SET(tdci.id,(\n" +
				"SELECT temp.channel_ids FROM (\n" +
				"SELECT GROUP_CONCAT(channel_ids) AS channel_ids,STATUS FROM idb_disclosure_channel_rule tdcr WHERE FIND_IN_SET($S{prodCode},t8_prod_info_ids) AND STATUS='1' OR tdcr.channel_apply_type='2' AND STATUS='1' GROUP BY STATUS) temp))";
		return super.findRows(sql, DataSourceProperty.IDB, param);
	}

	/**
	 * 功能：查询信披渠道规则列表
	 * 作者：rennannan
	 * 日期：20210511
	 *
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> findDetailList(String param) throws Exception {

		List<SqlRow> list1 = null;
		String sql = "select (@i:=@i+1) as list_id,temp.* from\n" +
				"(select tdn.prod_code,tdpra.assets_type as list_assets_type,tdpra.amount as list_amount,tdpra.asset_ratio as list_asset_ratio  from idb_disclosure_notice tdn left join idb_disclosure_regular_asset tdpra on tdn.id=tdpra.t8_disclosure_notice_id where tdn.prod_code=$S{prodCode} and tdpra.assets_type!='总资产' order by row_numbers+0 asc) temp ,(select @i:=0) as t ";
		list1 = super.findRows(sql, DataSourceProperty.IDB, param);
		String sql2 = "SELECT tdped.private_prod_code,tdped.private_prod_name,tped.private_total FROM idb_disclosure_asset_bottom_change tdped LEFT JOIN idb_disclosure_asset_bottom tped ON tped.private_prod_code=tdped.private_prod_code AND tped.data_date=tdped.data_date WHERE tdped.prod_code=$S{prodCode} GROUP BY tdped.private_prod_code;";
		List<SqlRow> list2 = super.findRows(sql2, DataSourceProperty.IDB, param);
		List<SqlRow> total = super.findRows("SELECT tdpra.amount AS invest_amount FROM idb_disclosure_notice tdn LEFT JOIN idb_disclosure_regular_asset tdpra ON tdpra.t8_disclosure_notice_id=tdn.id WHERE tdpra.assets_type='总资产' AND tdn.prod_code='" + param + "'", DataSourceProperty.IDB, param);
		String totalAmount = "";
		if (total != null && total.size() > 0) {
			totalAmount = total.get(0).getString("invest_amount");
		}
		if (list2 != null && list2.size() > 0) {
			for (int i = 0; i < list2.size(); i++) {
				SqlRow row = new SqlRow();
				row.put("list_assets_type", "私募资管产品" + (i + 1));
				String privateTotal = list2.get(i).getString("private_total");
				String ratio = "";
				if (!Tools.isEmpty(totalAmount)) {
					ratio = (Double.parseDouble(privateTotal) / Double.parseDouble(totalAmount)) + "";
				}

				row.put("list_amount", privateTotal);
				row.put("list_asset_ratio", ratio);
				row.put("list_id", 6 + "." + (i + 1));
				list1.add(row);
				String prodCode = list2.get(i).getString("private_prod_code");
				String sql3 = "select remark as list_id,private_prod_code,private_prod_name,invest_type as list_assets_type,invest_amount as list_amount,tdped.asset_ratio as list_asset_ratio \n" +
						"from idb_disclosure_asset_bottom_change tdped\n" +
						"where private_prod_code='" + prodCode + "' and tdped.prod_code='" + param + "' order by row_numbers asc;";
				List<SqlRow> list3 = super.findRows(sql3, DataSourceProperty.IDB, param);
				if (list3.size() > 0) {
					list1.addAll(list3);
				}
			}
		}

		String sql4 = "select '合计' as list_id,tdn.prod_code,tdpra.assets_type as list_assets_type,tdpra.amount as list_amount,tdpra.asset_ratio as list_asset_ratio " +
				"  from idb_disclosure_notice tdn left join idb_disclosure_regular_asset tdpra on tdn.id=tdpra.t8_disclosure_notice_id and tdpra.assets_type='总资产',(select @i:=0) as t " +
				" where tdn.id='31'  order by tdpra.id asc;";
		List<SqlRow> list4 = super.findRows(sql4, DataSourceProperty.IDB, param);
		list1.addAll(list4);
		return list1;
	}


	public String findDetail(String noticeId) throws Exception {
		List<SqlRow> list1 = null;
		String sql = "select (@i:=@i+1) as list_id,temp.* from\n" +
				"(select tdn.prod_code,tdpra.assets_type as list_assets_type,format(tdpra.amount,2) AS list_amount,format(tdpra.asset_ratio,2) as list_asset_ratio  " +
				"   from idb_disclosure_notice tdn left join idb_disclosure_regular_asset tdpra on tdn.id=tdpra.t8_disclosure_notice_id " +
				"  where  tdpra.assets_type!='总资产' and tdn.id = '" + noticeId + "' order by row_numbers+0 asc) temp ,(select @i:=0) as t ";
		list1 = super.findRows(sql, DataSourceProperty.IDB, noticeId);

		if (Tools.isNotEmpty(list1.get(list1.size() - 1).getString("list_amount"))) {
			if ("0.00".equals(list1.get(list1.size() - 1).getString("list_amount"))) {
				return "failed";
			} else {
				return "success";
			}
		} else {
			return "failed";
		}
	}

	public List<SqlRow> findDetailListForNotice(String prodCode, String noticeId, String baseDate) throws Exception {

		List<SqlRow> list1 = null;
		String sql = "select (@i:=@i+1) as list_id,temp.* from\n" +
				"   (select tdn.prod_code,tdpra.assets_type as list_assets_type,format(tdpra.amount,2) AS list_amount,format(tdpra.asset_ratio,2) as list_asset_ratio  " +
				"      from idb_disclosure_notice tdn left join idb_disclosure_regular_asset tdpra on tdn.id=tdpra.t8_disclosure_notice_id where tdn.prod_code=$S{prodCode} and tdpra.assets_type!='总资产' and tdn.id = '" + noticeId + "' order by row_numbers+0 asc) temp ,(select @i:=0) as t ";
		list1 = super.findRows(sql, DataSourceProperty.IDB, prodCode);
		String sql2 = "SELECT tdped.private_prod_code,tdped.private_prod_name,tped.private_total " +
				"   FROM idb_disclosure_asset_bottom_change tdped LEFT JOIN idb_disclosure_asset_bottom tped ON tped.private_prod_code=tdped.private_prod_code AND tped.data_date=tdped.data_date " +
				"  WHERE tdped.prod_code=$S{prodCode} AND tdped.data_date='" + baseDate + "'  GROUP BY tdped.private_prod_code;";
		List<SqlRow> list2 = super.findRows(sql2, DataSourceProperty.IDB, prodCode);
		List<SqlRow> total = super.findRows("SELECT tdpra.amount AS invest_amount " +
				"  FROM idb_disclosure_notice tdn LEFT JOIN idb_disclosure_regular_asset tdpra ON tdpra.t8_disclosure_notice_id=tdn.id " +
				" WHERE tdpra.assets_type='总资产' AND tdn.prod_code='" + prodCode + "' AND tdn.id='" + noticeId + "'", DataSourceProperty.IDB, prodCode);
		String totalAmount = "";
		if (total != null && total.size() > 0) {
			totalAmount = total.get(0).getString("invest_amount");
		}
		if (list2 != null && list2.size() > 0) {
			for (int i = 0; i < list2.size(); i++) {
				SqlRow row = new SqlRow();
				row.put("list_assets_type", list2.get(i).getString("private_prod_name"));
				String totalPrivate = list2.get(i).getString("private_total");
				String privateTotal = "";
				if (Tools.isNotEmpty(totalPrivate)) {
					Double f = Double.parseDouble(totalPrivate);
					//privateTotal = String.format("%.2f",f).toString();
					DecimalFormat df = new DecimalFormat("#,###.00");
					privateTotal = df.format(f);
				}
				String ratio = "";
				if (!Tools.isEmpty(totalAmount)) {
					Double f2 = (Double.parseDouble(privateTotal.replaceAll(",", "")) / Double.parseDouble(totalAmount)) * 100;
					//privateTotal = String.format("%.2f",f2).toString();
					BigDecimal b = new BigDecimal(f2);
					ratio = b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
					//DecimalFormat df = new DecimalFormat("#,###.00");
					//ratio = df.format(f2);
					//ratio = String.format("%.2f",f2);
				}

				row.put("list_amount", privateTotal);
				row.put("list_asset_ratio", ratio);
				row.put("list_id", 6 + "." + (i + 1));
				list1.add(row);
				String prodCode2 = list2.get(i).getString("private_prod_code");
				String sql3 = " select remark as list_id,private_prod_code,private_prod_name,invest_type as list_assets_type,format(invest_amount,2) AS list_amount,format(tdped.asset_ratio,2) AS list_asset_ratio \\n\" +\n" +
						"\t\t\t\t\t\t\"from idb_disclosure_asset_bottom_change tdped\\n\" +\n" +
						"\t\t\t\t\t\t\"where private_prod_code='\" + prodCode2 + \"' and tdped.prod_code='\" + prodCode + \"' AND tdped.data_date='\" + baseDate + \"' order by row_numbers asc; ";
				List<SqlRow> list3 = super.findRows(sql3, DataSourceProperty.IDB, prodCode);
				List<SqlRow> newList = new ArrayList();
				Integer num = list3.size() / 5;
				if (num != 1) {
					for (int k = 0; k < num; k++) {
						for (int n = k; n < list3.size(); n = n + num) {
							newList.add(list3.get(n));
						}
					}
					list1.addAll(newList);
				} else {
					if (list3.size() > 0) {
						list1.addAll(list3);
					}
				}
			}
		}

		String sql4 = "select '合计' as list_id,tdn.prod_code,tdpra.assets_type as list_assets_type,tdpra.amount as list_amount,tdpra.asset_ratio as list_asset_ratio " +
				"  from idb_disclosure_notice tdn left join idb_disclosure_regular_asset tdpra on tdn.id=tdpra.t8_disclosure_notice_id and tdpra.assets_type='总资产',(select @i:=0) as t " +
				" where tdn.id='" + noticeId + "'  order by tdpra.id asc;";
		List<SqlRow> list4 = super.findRows(sql4, DataSourceProperty.IDB, noticeId);
		list1.addAll(list4);
		return list1;
	}

	public List<SqlRow> findDetailListForNotice1(String prodCode, String noticeId, String baseDate) throws Exception {
		List<SqlRow> list1 = null;
		//资产配置情况
		String sql = "select (@i:=@i+1) as list_id,temp.* from\n" +
				"  (select tdn.prod_code,tdpra.assets_type as list_assets_type,format(tdpra.amount,2) AS list_amount,format(tdpra.asset_ratio,2) as list_asset_ratio  " +
				"     from idb_disclosure_notice tdn left join idb_disclosure_regular_asset tdpra on tdn.id=tdpra.t8_disclosure_notice_id " +
				"    where tdn.prod_code=$S{prodCode} and tdpra.assets_type!='总资产' and tdpra.data_type = '1' and tdn.id = '" + noticeId + "' order by row_numbers+0 asc) temp ,(select @i:=0) as t ";
		list1 = super.findRows(sql, DataSourceProperty.IDB, prodCode);
		String sql3 = "select '' as list_id,temp.* from\n" +
				"  (select tdn.prod_code,tdpra.assets_type as list_assets_type,row_numbers,format(tdpra.amount,2) AS list_amount,format(tdpra.asset_ratio,2) as list_asset_ratio " +
				"     from idb_disclosure_notice tdn left join idb_disclosure_regular_asset tdpra on tdn.id=tdpra.t8_disclosure_notice_id " +
				"    where tdn.prod_code=$S{prodCode} and tdpra.data_type = '2' and tdn.id = '" + noticeId + "' order by row_numbers+0 asc) temp ,(select @i:=0) as t ";
		List<SqlRow> list3 = super.findRows(sql3, DataSourceProperty.IDB, prodCode);

		//刷选私募明细中公募数据
		if (CollectionUtils.isEmpty(list3)) {
			//查询数据并判断是否为入库数据  为空或者为0 没有入库
			List<SqlRow> assetList = super.findRows("select remark as list_id,data_date,invest_type as list_assets_type,row_numbers,format(invest_amount,2) as list_amount,format(asset_ratio,2) as list_asset_ratio " +
					"  from idb_disclosure_asset_bottom_change " +
					" where prod_code = '" +prodCode +"' and data_date = '" + baseDate +"'", DataSourceProperty.IDB, prodCode);
			if (assetList !=null) {
				for (SqlRow row : assetList) {
					if (row.get("list_asset_ratio") != null && !"".equals(row.get("list_asset_ratio").toString()) && new BigDecimal(row.get("list_asset_ratio").toString()).compareTo(BigDecimal.ZERO) >0) {
						list3 = assetList;
					}
				}
			}
		}
		//如果公募资管产品金额为零则不显示
		boolean flag = false;
		boolean flag5 = false;
		//list3
		BigDecimal total = new BigDecimal(0);
		for (SqlRow row : list3) {
			if ("7".equals(row.getString("row_numbers")) || "8".equals(row.getString("row_numbers")) || "9".equals(row.getString("row_numbers")) ||
					"10".equals(row.getString("row_numbers"))) {
				String amount = row.getString("list_amount");
				total = total.add(new BigDecimal(amount.replace(",","")));
			}
			if ("11".equals(row.getString("row_numbers")) && ("0.00".equals(row.getString("list_amount")) || "".equals(row.getString("list_amount")))) {
				flag5 = true;
			}
		}

		if (total.compareTo(new BigDecimal(0)) == 0)
			flag=true;

		if(flag && flag5) { //5条都为空
			list3.remove(4);
			list3.remove(3);
			list3.remove(2);
			list3.remove(1);
			list3.remove(0);
		} else if (!flag && flag5) {
			list3.remove(4);
		}

//	   	for(int i=0;i<list3.size();i++){
//			if("公募资管产品".equals(list3.get(i).get("list_assets_type")) && "0.00".equals(list3.get(i).get("list_amount").toString())) {
//				list3.remove(i);
//				break;
//			}
//		}
		list1.addAll(list3);
		//总资产
		String sql4 = "select '合计' as list_id,tdn.prod_code,tdpra.assets_type as list_assets_type,FORMAT(tdpra.amount,2) as list_amount,tdpra.asset_ratio as list_asset_ratio from idb_disclosure_notice tdn left join idb_disclosure_regular_asset tdpra on tdn.id=tdpra.t8_disclosure_notice_id and tdpra.assets_type='总资产',(select @i:=0) as t where tdn.id='" + noticeId + "'  order by tdpra.id asc;";
		List<SqlRow> list4 = super.findRows(sql4, DataSourceProperty.IDB, noticeId);
		list1.addAll(list4);
		return list1;
	}


	/**
	 * 功能：查询十大资产明细信息
	 * 作者：rennannan
	 * 日期：20210511
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> findTenDetailList(String noticeId) throws Exception {

		String sql4 = "select (@i:=@i+1) as ten_list_id,temp.* from\n" +
				"  (select tdai.assets_code as ten_assets_code,tdai.assets_name as ten_assets_name,FORMAT(IFNULL(tdai.assets_scale,0.00),2) as ten_assets_scale,tdai.asset_ratio as ten_asset_ratio " +
				"     from idb_disclosure_notice tdpr left join idb_disclosure_regular_major_asset tdai on tdai.t8_disclosure_notice_id=tdpr.id " +
				"    where tdpr.id=$S{noticeId} order by assets_scale+0 desc)temp,(select @i:=0) as t order by temp.ten_asset_ratio + 0 desc";
		List<SqlRow> list4 = super.findRows(sql4, DataSourceProperty.IDB, noticeId);

		return list4;
	}

	public SqlRow findDataInfoByType(Map<String, Object> params) throws Exception {
		SqlRow mapSqlResult = null;
		String isComplete = "";
		if (params.get("isComplete") != null) {
			isComplete = params.get("isComplete").toString();
		}
		String dateSql = "SELECT report_start_date,report_end_date FROM idb_disclosure_notice WHERE id='" + params.get("t8DisclosureNoticeId") + "'";
		SqlRow dateSqlRow = super.findRow(dateSql, DataSourceProperty.IDB, params);
		String startDateReport = dateSqlRow.getString("report_start_date");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		format.setLenient(false);
		Date navStartDate = format.parse(startDateReport);
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(navStartDate);
		rightNow.add(Calendar.DAY_OF_MONTH, -1);
		Date dt1 = rightNow.getTime();
		String reportStartDate = format.format(dt1);
		String reportEndDate = dateSqlRow.getString("report_end_date");
		/*String navSql = "SELECT NAV FROM t8_prod_nav where PROD_CODE='"+params.get("prodCode")+"' ";
		SqlRow navSqlRow = super.findRow(navSql, DataSourceProperty.IDB, params);
		String nav = navSqlRow.getString("NAV");*/
		String totalNavSql = "SELECT TOTAL_NAV,NAV from t8_prod_nav where PROD_CODE='" + params.get("prodCode") + "' and NAV_DATE='" + reportStartDate + "'";
		SqlRow totalNavSqlRow = super.findRow(totalNavSql, DataSourceProperty.IDB, params);
		String totalNavBegin = "";
		String nav = "";
		if (totalNavSqlRow != null) {
			totalNavBegin = totalNavSqlRow.getString("TOTAL_NAV");
			nav = totalNavSqlRow.getString("NAV");
		}

		String totalNavSql2 = "SELECT TOTAL_NAV from t8_prod_nav where PROD_CODE='" + params.get("prodCode") + "' and NAV_DATE='" + reportEndDate + "'";
		SqlRow totalNavSqlRow2 = super.findRow(totalNavSql2, DataSourceProperty.IDB, params);
		String totalNavEnd = "";
		if (totalNavSqlRow2 != null) {
			totalNavEnd = totalNavSqlRow2.getString("TOTAL_NAV");
		}
		if ("1".equals(isComplete)) {
			String sql = "SELECT * FROM t8_print_temp_data tptd WHERE is_xp_data='1' AND xp_doc_type=(\n" +
					"SELECT tdpr.disclosure_type FROM idb_disclosure_notice tdn LEFT JOIN idb_disclosure_prod_rule tdpr ON tdn.t8_disclosure_rule_id=tdpr.id WHERE tdn.id='" + params.get("t8DisclosureNoticeId") + "')";
			SqlRow sqlRow = super.findRow(sql, DataSourceProperty.IDB, params);
			String sqlInfo = sqlRow.getString("sql_info");

			mapSqlResult = super.findRow(sqlInfo, DataSourceProperty.IDB, params);
		} else {
			String sql = "SELECT * FROM t8_print_temp_data tptd WHERE is_xp_data='2' AND xp_doc_type=(\n" +
					"SELECT tdpr.disclosure_type FROM idb_disclosure_notice tdn LEFT JOIN idb_disclosure_prod_rule tdpr ON tdn.t8_disclosure_rule_id=tdpr.id WHERE tdn.id='" + params.get("t8DisclosureNoticeId") + "')";
			SqlRow sqlRow = super.findRow(sql, DataSourceProperty.IDB, params);
			if (sqlRow != null) {
				String sqlInfo = sqlRow.getString("sql_info");
				mapSqlResult = super.findRow(sqlInfo, DataSourceProperty.IDB, params);
			}
		}
		if (Tools.isNotEmpty(totalNavBegin) && Tools.isNotEmpty(totalNavEnd)) {
			// subsist_netval_rate  report_netval_rate
			Double sub = ((Double.parseDouble(totalNavEnd) - 1) / 1) * 100;
			Double report = ((Double.parseDouble(totalNavEnd) - Double.parseDouble(totalNavBegin)) / Double.parseDouble(nav)) * 100;
			//privateTotal = String.format("%.2f",f).toString();
			DecimalFormat df = new DecimalFormat("##.##");
			String subsistNetvalRate = df.format(sub);
			String reportNetvalRate = df.format(report);
		} else {
			//mapSqlResult.put("subsist_netval_rate", "");
			//mapSqlResult.put("report_netval_rate", "");
		}
		return mapSqlResult;
	}


	public SqlResult<SqlRow> findUserInfo(SqlParam<DisclosureNotice> param) throws Exception {
		SqlResult<SqlRow> res = new SqlResult<>();
		List<SqlRow> rows = super.findRows("SELECT count(id) count FROM idb_disclosure_notice_process WHERE t8_disclosure_notice_id = $S{id}\n" +
				"\tAND (user_id = $S{userId} or to_user_id = $S{userId})", DataSourceProperty.IDB, param.getModel());
		res.setRows(rows);
		return res;
	}

	public Map<String, Object> findAssetsInfo(SqlParam<DisclosureNotice> param) throws Exception {
		String sql = "SELECT sum(private_total) as total FROM idb_disclosure_asset_bottom WHERE prod_code='" + param.getModel().getProdCode() + "' AND data_date='" + param.getModel().getProdBaseDate() + "'";
		List<SqlRow> mapSqlResult = super.findRows(sql, DataSourceProperty.IDB);
		String sql2 = "SELECT amount as total FROM idb_disclosure_regular_asset WHERE t8_disclosure_notice_id='" + param.getModel().getId() + "' AND assets_type='私募资管产品'";
		List<SqlRow> mapSqlResult2 = super.findRows(sql2, DataSourceProperty.IDB);
		Map<String, Object> map = new HashMap<>();
		//0 相等 / 1 不相等
		if (Tools.isNotEmpty(mapSqlResult.get(0).getString("total"))) {
			if (Tools.isNotEmpty(mapSqlResult2.get(0).getString("total"))) {
				Float f1 = Float.parseFloat(mapSqlResult.get(0).getString("total"));
				Float f2 = Float.parseFloat(mapSqlResult2.get(0).getString("total"));

				if (Math.abs(f1 - f2) == 0) {
					map.put("result", "0");
				} else {
					map.put("result", "1");
				}
			} else {
				map.put("result", "1");
			}
		} else {
			if (Tools.isNotEmpty(mapSqlResult2.get(0).getString("total"))) {
				map.put("result", "1");
			} else {
				map.put("result", "0");
			}
		}

		return map;
	}

	public SqlResult<SqlRow> findUserInfoInGroup(SqlParam<DisclosureNotice> param) throws Exception {
		String sql = "SELECT tpu.userid_a,tpu.role_id FROM sys_user su LEFT JOIN t8_prod_user tpu ON su.userid=tpu.userid_a " +
				"WHERE tpu.prod_code='" + param.getModel().getProdCode() + "' AND tpu.userid_a='" + param.getModel().getCrtUserId() + "'";
		List<SqlRow> mapSqlResult = super.findRows(sql, DataSourceProperty.PUB);
		SqlResult<SqlRow> res = new SqlResult<>();
		res.setRows(mapSqlResult);
		return res;
	}

	/**
	 * 功能：根据信披规则id查询对应模板设置的字段角色
	 * 作者：rennannan
	 * 日期：20210605
	 *
	 * @return
	 */
	public List<DisclosureNotice> findRoleIdsByRuleId(String ruleId) throws Exception {
		String sql = "select roleids role_ids  from idb_disclosure_mod_column\n" +
				"     where t8_disclosure_version_id=(select t8_disclosure_version_id from idb_disclosure_rule where id = $S{ruleId})\n" +
				"\t\t   and LENGTH(trim(roleids))>0\n" +
				"\t\t\t and IFNULL(roleids,'') <> ''\n" +
				"  group by roleids";
		return super.findRows(DisclosureNotice.class, sql, DataSourceProperty.IDB, ruleId);
	}

	/**
	 * 功能：根据产品id和角色编号查询对应的用户id
	 * 作者：rennannan
	 * 日期：20210605
	 *
	 * @param notice
	 * @return
	 */
	public List<DisclosureNotice> findUserIds(DisclosureNotice notice) throws Exception {
		String sql = "select userid_a user_id from t8_prod_user where t8_prod_info_id=$S{t8ProdInfoId} and role_id = $S{roleId}";
		return super.findRows(DisclosureNotice.class, sql, DataSourceProperty.PUB, notice);

	}

	/**
	 * 功能：根据生成日期删除公告数据
	 * 作者：rennannan
	 * 日期：20210602
	 *
	 * @return
	 */
	public int deleteByCrtDate(String date) throws Exception {
		String sql = " delete from idb_disclosure_notice where crt_date=$S{crtDate}";
		return super.update(sql, DataSourceProperty.IDB, date).getEffect();
	}

	/**
	 * 查询已发布的信披公告数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<DisclosureNotice> findCountsByCode(String id) throws Exception {
		String sql = "select prod_code from idb_disclosure_notice where id =$S{id} AND disclosure_status = '8' ";
		return super.findRows(DisclosureNotice.class, sql, DataSourceProperty.IDB, id);

	}

	public List<SqlRow> findOperatorForDivided(SqlParam<DisclosureNotice> param) throws Exception {
		String sql = "select * from idb_disclosure_operation where deal_id=$S{id} and disclosure_type='5' and userid=$S{userId} and status='0' and (operation_type='3' or operation_type='5')";
		return super.findRows(sql, DataSourceProperty.IDB, param.getModel());

	}

	public List<DisclosureNotice> findCountsStatus(String prodCode) throws Exception {
		String sql = "select prod_code from idb_disclosure_notice where prod_code =$S{prodCode} and current_stage_status ='9' ";
		return super.findRows(DisclosureNotice.class, sql, DataSourceProperty.IDB, prodCode);

	}

	public List<DisclosureNotice> findCountsStatusByInfoId(String disclosureNoticeId) throws Exception {
		String sql = "select prod_code,id from idb_disclosure_notice where id =$S{disclosureNoticeId} and stage ='1' and current_stage_status ='9'";
		return super.findRows(DisclosureNotice.class, sql, DataSourceProperty.IDB, disclosureNoticeId);

	}

	public List<DisclosureNotice> findCountsStatusById(String prodId) throws Exception {
		String sql = "select prod_code from idb_disclosure_notice where id =$S{id} and stage ='4' and current_stage_status = '15'";
		return super.findRows(DisclosureNotice.class, sql, DataSourceProperty.IDB, prodId);

	}

	/**
	 * 根据信披公告主键id修改当前阶段
	 *
	 * @param id 信披公告主键id
	 * @return
	 * @throws Exception
	 */
	public int updateStageStatus(String id, String stage) throws Exception {
		//新逻辑,更新公告状态,同时更新公告子状态为托管复核中
		String updUserId = (String) SysUtil.getSysUserParamValue("sys_user_userid");
		String updUserName = (String) SysUtil.getSysUserParamValue("sys_user_username");
		String date = DateUtil.getNowDate();
		String time = DateUtil.getNowTime();

		String sql = " update  idb_disclosure_notice set disclosure_status = '-2',current_stage_status ='10',upd_date = '"+date+"', upd_time = '"+time+"', upd_user_id = '"+updUserId+"', upd_user_name = '"+updUserName+"' where id = '" + id + "' ";
		return super.update(sql, DataSourceProperty.IDB).getEffect();
	}

	/**
	 * @param params id
	 * @return
	 * @throws Exception 公告发布完成，发布状态变更为已发布
	 */
	public int updateNoticeStatus(SqlParam<DisclosureNotice> params) throws Exception {
		String sql = " update idb_disclosure_notice set publish_status = '2' where id =$S{id} ";
		return super.update(sql, DataSourceProperty.IDB, params.getModel()).getEffect();
	}

	/**
	 * 功能：修改公告当前阶段与当前阶段状态
	 * 作者：rennannan
	 * 日期：20211115
	 *
	 * @param id
	 * @param status
	 */
	public int updateNoticeStageStatus(String id, String status) throws Exception {
		String sql = " update  idb_disclosure_notice set effect_status = '" + status + "' where id = '" + id + "' ";
		return super.update(sql, DataSourceProperty.IDB,null).getEffect();
	}

	/**
	 * @param params id
	 * @return
	 * @throws Exception 公告发布完成，发布状态变更为已发布
	 */
	public int updateNoticePublishStatus(DisclosureNotice params) throws Exception {
		String sql = " update  idb_disclosure_notice set publish_status = '2',current_stage_status='13' where id =$S{id} ";
		return super.update(sql, DataSourceProperty.IDB, params).getEffect();
	}

	public SqlRow findNoticeStatus(String t8DisclosureNoticeId) throws Exception {
		return super.findRow("select stage,current_stage_status,approval_status," +
				"eba_status from idb_disclosure_notice where id = $S{t8DisclosureNoticeId}", DataSourceProperty.IDB, t8DisclosureNoticeId);
	}

	/**
	 * @param t8DisclosureNoticeId
	 * @return
	 * @throws Exception 查询托管行邮箱地址
	 */
	public List<SqlRow> findEamilById(String t8DisclosureNoticeId) throws Exception {
		return super.findRows("SELECT tti.email AS trutee_email FROM idb_disclosure_notice tdn LEFT JOIN" +
				" t8_prod_trutee_bank tptb ON tdn.prod_code = tptb.prod_code LEFT JOIN" +
				" t8_trutee_info tti ON tptb.t8_trutee_info_id = tti.id WHERE tdn.id=$S{disclosureNoticeId}", DataSourceProperty.IDB, t8DisclosureNoticeId);
	}

	public void updateIssuedStatus(SqlParam<DisclosureNotice> param) throws Exception {
		String sql = "UPDATE idb_disclosure_notice SET stage = $S{stage}, current_stage_status = $S{currentStageStatus}, publish_status = $S{publishStatus}, approval_status = $S{approvalStatus} WHERE id = $S{id}";
		super.update(sql, DataSourceProperty.IDB, param.getModel());
	}

	public List<SqlRow> findIssuedInfo(Map<String, Object> params) throws Exception {
		//从数据源查询大sql
		String sql = super.findRow(String.class, "SELECT sql_info FROM t8_print_temp_data WHERE xp_doc_type=2 ", 0, params);
		return super.findRows(sql, DataSourceProperty.PUB, params);
	}

	public String findVersionId(Map<String, Object> params) throws Exception {
		String sql = "SELECT\n" +
				"\tt3.t8_disclosure_version_id id \n" +
				"FROM\n" +
				"\tidb_disclosure_notice t1\n" +
				"\tLEFT JOIN idb_disclosure_prod_rule t2 ON t1.t8_disclosure_rule_id = t2.id\n" +
				"\tLEFT JOIN idb_disclosure_rule t3 ON t3.id = t2.t8_disclosure_rule_id \n" +
				"WHERE\n" +
				"\tt1.id = '" + params.get("id") + "'";
		return super.findRow(String.class, sql, DataSourceProperty.IDB, params);
	}

	public String insertNotice(SqlRow sqlRow) throws Exception {
		String sql = "INSERT INTO idb_disclosure_notice (`id`, `t8_prod_info_id`, `prod_code`, `prod_name`, `t8_disclosure_rule_id`, `notice_title`, `task_id`, `plan_sp_date`, `plan_bl_date`, `plan_fb_date`, `stage`, `current_stage_status`, `prod_base_date`, `publish_status`, `approval_status`, `crt_date`, `crt_time`, `crt_user_id`, `crt_user_name`) " +
				"VALUES ($AUTOIDS{id}, '" + sqlRow.get("t8_prod_info_id") + "', '" + sqlRow.get("prod_code") + "', '" + sqlRow.get("prod_name") + "', '" + sqlRow.get("id") + "', '" + sqlRow.get("notice_title") + "', '" + sqlRow.get("taskid") + "', '" + sqlRow.get("plan_sp_date") + "', '" + sqlRow.get("plan_bl_date") + "', '" + sqlRow.get("plan_fb_date") + "', '1', '0', '" + sqlRow.get("prod_base_date") + "', '0', '0', '" + sqlRow.get("crt_date") + "', '" + sqlRow.get("crt_time") + "', '" + sqlRow.get("crt_user_id") + "', '" + sqlRow.get("crt_user_name") + "')";
		return super.update(sql, DataSourceProperty.IDB).getAutoId();
	}

	public DisclosureNotice queryNoticeById(String id ) throws Exception {
		return super.findRow(DisclosureNotice.class, "select n.*,p.is_share_sort from  idb_disclosure_notice n left join t8_prod_info p on n.prod_code = p.prod_code  where n.id = "+id+"", DataSourceProperty.IDB, null);
	}

	public SqlRow queryModByNoticeId(String id ) throws Exception {
		return super.findRow( "SELECT r.t8_disclosure_version_id  id  from idb_disclosure_notice n  LEFT JOIN   idb_disclosure_prod_rule r on n.t8_disclosure_rule_id=  r.id  where n.id = "+id+"", DataSourceProperty.IDB, null);
	}

	public List<SqlRow> queryShareImage(String noticeId ,String type) throws Exception {
		String sql1 = "select report_start_date,report_end_date from idb_disclosure_notice where id = '"+noticeId+"'";
		SqlRow sqlRow = super.findRow(sql1,noticeId);
		String sql = "select t.* from (select \n" +
				"tdsi.share_name,\n" +
				"tdsi.share_image_path, \n" +
				"tpss.sales_code \n" +
				"from idb_disclosure_share_image tdsi left join t8_prod_share_sort tpss on tdsi.share_name = tpss.sales_name " +
				" left join t8_prod_nav nav on nav.prod_code = tpss.sales_code " +
				" left join idb_disclosure_regular_share_sort tdrss on tpss.sales_code = tdrss.sales_code " +
				" where tdsi.t8_disclosure_notice_id = '"+noticeId+"' " +
				" and area_section = '"+type+"' " +
				" and (nav.nav_date = '"+sqlRow.getString("report_start_date")+"' or nav.nav_date = '"+sqlRow.getString("report_end_date")+"')" +
				" and tdrss.is_deleted = '0' group by tdsi.area_section,nav.prod_code) t" +
				" left join idb_disclosure_regular_share_sort  tdrss\n" +
				" on t.sales_code = tdrss.sales_code where tdrss.is_deleted = '0' and tdrss.notice_id = '"+noticeId+"'  order by t.share_name asc";

		return super.findRows(sql, DataSourceProperty.IDB,"0");//需要创建视图
		//return super.findRows("select share_name ,share_image_path from  idb_disclosure_share_image where t8_disclosure_notice_id= "+"'"+noticeId+"' and area_section = "+"'"+type+"' order by share_name asc", 0);
	}


	/**
	 * 通过公告id查询公告状态
	 * @param param 参数对象
	 * @return
	 */
	public SqlResult<DisclosureNotice> findDisclosureNoticeStatus(SqlParam<DisclosureNotice> param) throws Exception {
		return super.findRows("select id,prod_code,effect_status,review_status,disclosure_status,crt_user_id from idb_disclosure_notice where id = $S{id}", DataSourceProperty.IDB, param);
	}

	/**
	 * 更新信披公告复核状态
	 * @return
	 */
	public UpdateResult updateDisclosureStatus(Map<String, Object> params) throws Exception {
		String updStr = "update idb_disclosure_notice set review_status = '"+ ApprovalStatus.down.getItemKey() +"' where id = $S{id}";
		return super.update(updStr, DataSourceProperty.IDB, params);
	}

	/**
	 * 变更信披公告状态
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public void updateDisclosureNoticeStatus(Map<String,Object> params) throws Exception{
		String sqlStr = "update idb_disclosure_notice set disclosure_status = $S{disclosureStatusAfter} where id = $S{t8DisclosureNoticeId}";
		super.update(sqlStr, DataSourceProperty.IDB, params);
	}
	public void updateDisclosureVersionStatus(Map<String,Object> params) throws Exception{
		String sqlStr = "update idb_disclosure_notice_version set is_notice_pub = $S{disclosureStatusAfter} where id = $S{noticeVersionId} and t8_disclosure_notice_id = $S{t8DisclosureNoticeId}";
		super.update(sqlStr, DataSourceProperty.IDB, params);
	}
	public void updateDisclosureChannelStatus(Map<String,Object> params) throws Exception{
		String sqlStr = "update idb_disclosure_notice_channel set notice_channel_public_status = $S{disclosureStatusAfter} where disclosure_notice_id = $S{t8DisclosureNoticeId} AND disclosure_notice_version_id = $S{noticeVersionId}";
		super.update(sqlStr, DataSourceProperty.IDB, params);
	}

	/**
	 * 插入信披公告状态变更记录
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public void insertDisclosureNoticeRecord(Map<String,Object> params) throws Exception{
		String sqlStr = "insert into idb_disclosure_notice_status_record (id, t8_disclosure_notice_id, disclosure_status_ahead, disclosure_status_after, change_reason, crt_date, crt_time, crt_user_id, crt_user_name,notice_version_id) " +
				"values ($AUTOIDS{idb_disclosure_notice_status_record}, $S{t8DisclosureNoticeId}, $S{disclosureStatusAhead}, $S{disclosureStatusAfter}, $S{changeReason}, $S{crtDate}, $S{crtTime}, $S{crtUserId}, $S{crtUserName},$S{noticeVersionId})";
		super.update(sqlStr, DataSourceProperty.IDB, params);
	}

	/**
	 * @Description 通过母产品code查询份额分类产品信息
	 * @Date 2021/11/29 11:00
	 * @Param []
	 * @Return java.util.List<com.kayak.core.sql.SqlRow>
	 */
	public List<SqlRow> findDisclosureRegularShareSort(String prodCode,String navDate,String noticeId) throws Exception{
		String sql1 = "select report_start_date,report_end_date from idb_disclosure_notice where id = '"+noticeId+"'";
		SqlRow sqlRow = super.findRow(sql1,noticeId);
		String sql = "select sort.id,sort.parent_prod_code,sort.sales_code,sort.sales_name,nav.prod_code,\n" +
				"\tFORMAT(IFNULL(sort.share_value,0.00),4) as share_value,\n" +
				"\tFORMAT(IFNULL(sort.share_total_value,0.00),4) as share_total_value,\n" +
				"\tFORMAT(IFNULL(sort.asset_value,0.00),2) as asset_value,\n" +
				"\tFORMAT(IFNULL(sort.dur_net_growth,0.00),2) as dur_net_growth,\n" +
				"\tFORMAT(IFNULL(sort.sur_net_growth,0.00),2) as sur_net_growth,\n" +
				"\tFORMAT(IFNULL(sort.end_share_value,0.00),4) as end_share_value,\n" +
				"\tFORMAT(IFNULL(sort.end_total_value,0.00),4) as end_total_value,\n" +
				"\tFORMAT(IFNULL(sort.end_prod_share,0.00),2) as end_prod_share,\n" +
				"\tFORMAT(IFNULL(sort.end_asset_value,0.00),2) as end_asset_value,\n" +
				"\tFORMAT(IFNULL(sort.begin_total_share,0.00),2) as begin_total_share,\n" +
				"\tFORMAT(IFNULL(sort.dur_purch_share,0.00),2) as dur_purch_share,\n" +
				"\tFORMAT(IFNULL(sort.dur_redem_share,0.00),2) as dur_redem_share,\n" +
				"\tFORMAT(IFNULL(sort.end_total_share,0.00),2) as end_total_share,\n" +
				"\tsort.earnings,sort.current_profit\n" +
				"\tfrom idb_disclosure_regular_share_sort sort left join t8_prod_nav nav on nav.prod_code = sort.sales_code\n" +
				"\twhere sort.parent_prod_code = '"+prodCode+"' " +
				" and (nav.nav_date = '"+sqlRow.getString("report_start_date") +"' or nav.nav_date = '" + sqlRow.getString("report_end_date") +"')" +

				" and sort.notice_id = '" +noticeId +"' and sort.is_deleted = '0' group by nav.prod_code order by sort.sales_code asc" ;
		return super.findRows(sql ,DataSourceProperty.IDB);
	}

	/**
	 * @Description 判断该产品是否为份额分类产品
	 * @Date 2021/11/29 16:53
	 * @Param []
	 * @Return java.lang.String
	 */
	public String isShareSort(String prodCode) throws Exception {
		String sql = "select is_share_sort from t8_prod_info where prod_code = '"+ prodCode + "'";
		SqlRow sqlRow = super.findRow(sql, DataSourceProperty.PUB,null);
		return sqlRow.get("is_share_sort").toString();
	}

	public SqlResult<DisclosureNotice>  findDisclosureNoticesByCode(FetcherData<DisclosureNotice> param) throws Exception {
		return super.findRows("select id,t8_prod_info_id,prod_code,t8_disclosure_rule_id,current_stage_status from idb_disclosure_notice where prod_code = $S{prodCode}", DataSourceProperty.IDB, param);
	}

	/**
	 *查询所有需要修改的公告进程id
	 * @param disclosureEvaluateEmp 估值导入对象信息
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> findAllProcessId(HashMap<String, Object> disclosureEvaluateEmp) throws Exception {
		return super.findRows(" select b.id\n" +
				"from idb_disclosure_notice a\n" +
				"         join idb_disclosure_notice_process b on a.id = b.t8_disclosure_notice_id\n" +
				"         join sys_user c on b.user_id = c.userid\n" +
				"where a.prod_code = $S{prodCode}\n" +
				"  and a.disclosure_type = '5' and b.role_id = '9' and c.jobno = $S{empNo}", DataSourceProperty.IDB, disclosureEvaluateEmp);//需要创建视图
	}

	public SqlRow noticeFilter(DisclosureNotice notice ) throws Exception{
		return super.findRow("select id from idb_disclosure_notice where stage = '1' and current_stage_status = '9' and id = '"+notice.getId()+"'", DataSourceProperty.IDB, null);
	}

	//判断是否有私募资管明细
	public List<SqlRow> findPrivateDetail(String noticeId) throws Exception {
		String sql = "select id from idb_disclosure_regular_asset where t8_disclosure_notice_id = '" +noticeId +"' " +
				" and  row_numbers in ('7','8','9','10','11')";
		return super.findRows(sql, DataSourceProperty.IDB);

	}

	/**
	 * 查询公告对应符合条件未发布的渠道信息,将需要发布该渠道的所有公告id储存起来
	 * @param noticeIds
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> findChannels(String noticeIds,String noticeVersionId) throws Exception {
		//添加筛选条件,已发布的渠道不用再次发布
		return super.findRows(" SELECT DISTINCT c.disclosure_notice_channel_id, c.channel_public_date, i.channel_name, i.is_docking, i.status, " +
				"                 i.host_ip, i.port_code, i.user_name, i.password, i.file_path, GROUP_CONCAT(DISTINCT c.disclosure_notice_id) disclosure_notice_id " +
				"   FROM idb_disclosure_notice_channel c " +
				"   LEFT JOIN idb_disclosure_channel i on c.disclosure_notice_channel_id = i.id " +
				"  WHERE c.disclosure_notice_id in ("+noticeIds+") " +
				"  AND c.disclosure_notice_version_id = '"+noticeVersionId+"' " +
				"  and i.is_docking = '1' and i.status = '1' " +
				"  and ( c.notice_channel_public_status ='"+DisclosureStatus.waitPub.getItemKey()+"'/*待发布*/  " +
				"  or c.notice_channel_public_status ='"+DisclosureStatus.failure.getItemKey()+"'/*或发布失败*/ ) " +
				"  GROUP BY c.disclosure_notice_channel_id order by c.disclosure_notice_channel_id desc",
				DataSourceProperty.IDB);
	}
	public List<SqlRow> findChannels(String noticeIds) throws Exception {
		//添加筛选条件,已发布的渠道不用再次发布
		return super.findRows(" SELECT DISTINCT c.disclosure_notice_channel_id, (case when i.id = '41' then '1' else '0' end) as is_lcxs_channel, c.channel_public_date, i.channel_name, i.is_docking, i.status, " +
				"                 i.host_ip, i.port_code, i.user_name, i.password, i.file_path, GROUP_CONCAT(DISTINCT c.disclosure_notice_id) disclosure_notice_id " +
				"   FROM idb_disclosure_notice_channel c " +
				"   LEFT JOIN idb_disclosure_channel i on c.disclosure_notice_channel_id = i.id " +
				"  WHERE c.disclosure_notice_id in ("+noticeIds+") " +
				"  and c.notice_channel_public_status in ('2','-1') /*仅仅查询未发布和发布失败的的*/ and i.is_docking = '1' and i.status = '1' " +
				"  GROUP BY c.disclosure_notice_channel_id order by c.disclosure_notice_channel_id desc",
				DataSourceProperty.IDB);
	}

	/**
	 * 获取信批批次号
	 * @param pub_date
	 * @return
	 * @throws Exception
	 */
	public String getNoticeFileSubmitBatchNo (String pub_date) throws Exception {
		return super.findRow("select ifnull(max(pc.batch_no),0)+1 as batch_no from idb_pubfile_channel_record pc " +
				"where pc.pub_date = '" + pub_date + "'", DataSourceProperty.IDB, null).getString("batch_no");
	}
	public Integer findChannelsForNoticeId(String noticeId,String noticeLatestVersionId) throws Exception {
		//添加筛选条件,已发布的渠道不用再次发布
		return super.findRow(" SELECT count(*) count " +
				"   FROM idb_disclosure_notice_channel c " +
				"  WHERE c.disclosure_notice_id = '"+noticeId+"' and c.disclosure_notice_version_id ='"+noticeLatestVersionId+"'" +
				"  AND c.notice_channel_public_status <> '" +DisclosureStatus.overSend.getItemKey() + "'",
				DataSourceProperty.IDB,null).getInteger("count");
	}
	/**
	* @功能描述:公告发布查询当天所需发布公告id
	* @params:[noticeIds]
	* @return:java.util.List<com.kayak.core.sql.SqlRow>
	* @Athor:ouyifan
	* @date:2022/8/29
	*/
	public List<SqlRow> findNoticeIdForPub(String nowDate, String disclosureType) throws Exception {
		return super.findRows("SELECT notice.id FROM idb_disclosure_notice notice WHERE notice.plan_fb_date = '"+nowDate+"' AND notice.disclosure_status = '"+ DisclosureStatus.waitPub.getItemKey()+"' and disclosure_type = '"+disclosureType+"'",
				DataSourceProperty.IDB);
	}



	public SqlRow queryPublishCount(String noticeIds) throws Exception {

		return super.findRow("select count(1) count from idb_disclosure_notice where id in ("+noticeIds+") and stage = '4' and current_stage_status='15' and publish_status='1'", DataSourceProperty.IDB, null);
	}

	public SqlResult<DisclosureNotice>  findDisclosureOtherNoticesAuth(SqlParam<DisclosureNotice> params) throws Exception {
		StringBuilder sql =
				new StringBuilder(
						"SELECT dn.id, dnv.id notice_version_id, dn.disclosure_type, dn.disclosure_son_type, po.prod_nm prod_name, dn.prod_code prod_code, " +
								"  dr.t8_disclosure_rule_id, dm.id AS t8_disclosure_mod_id, dmv.id AS t8_disclosure_mod_version_id, " +
								"  dn.report_type, dn.notice_title, " +
								"  dn.PROD_CLC_MTH, " +
								"  dn.PROD_FORM, " +
								"  dn.INV_PRD_DIME, " +
								"  dn.INV_PRD_LEN, " +
								"  dn.PROD_OBJ, " +
								"  dn.PROD_SER_CD, " +
								"  sr.PROD_SER_NM, " +
								"  dn.task_id, dn.plan_fb_date, dn.plan_sp_date, dn.plan_bl_date, dn.real_bl_date, dn.real_sp_date, dn.real_fb_date, " +
								"  dn.prod_base_date, dn.disclosure_status, dn.review_status, dn.report_start_date, dn.report_end_date, dn.is_send_email, " +
								"  dn.effect_status, dnv.file_name, dnv.file_path, dnv.crt_path, dn.crt_date, dn.crt_time, dn.crt_user_id, dn.crt_user_name, " +
								"  dn.upd_date, dn.upd_time, dn.upd_user_id, dn.upd_user_name, " +
								"  SUBSTR(dmv.doc_name,LOCATE('.',dmv.doc_name)) suffix," +
								"  dm.mod_name, dmv.version mod_version, dnv.notice_version disclosure_version" +
								"  FROM idb_disclosure_notice dn " +
								"  LEFT JOIN APP_PRD_BAS_INF po ON po.prod_cd = dn.prod_code " +
								"  LEFT JOIN idb_disclosure_notice_version dnv ON dnv.t8_disclosure_notice_id = dn.id " +
								"  LEFT JOIN idb_disclosure_mod_version dmv ON dmv.id = dnv.disclosure_mod_version_id " +
								"  LEFT JOIN idb_disclosure_mod dm ON dm.id = dmv.disclosure_mod_id " +
								"  LEFT JOIN idb_disclosure_prod_rule dr ON dr.id = dn.disclosure_prod_rule_id " +
								"  LEFT JOIN (SELECT DISTINCT k.PROD_SER_CD,k.PROD_SER_NM FROM APP_PRD_BAS_INF k) sr ON dn.PROD_SER_CD = sr.PROD_SER_CD" +
								"  WHERE 1 = 1  and is_manual_notice='1' and (delete_flag is null or delete_flag='0') and dnv.id in (select max(CONVERT ( dnv.id, SIGNED )) notice_version_id  from idb_disclosure_notice_version dnv group by dnv.t8_disclosure_notice_id) " );
		if (StringUtils.isNotBlank(params.getModel().getDisclosureType())) {
			sql.append(" and dn.disclosure_type = '" + params.getModel().getDisclosureType() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getDisclosureSonType())) {
			sql.append(" and dn.disclosure_son_type = '" + params.getModel().getDisclosureSonType() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdName())) {
			sql.append(" and po.prod_nm like '%" + params.getModel().getProdName() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql.append(" and po.prod_cd like '%" + params.getModel().getProdCode() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdBaseDate())) {
			sql.append(" and dn.prod_base_date like '%" + params.getModel().getProdBaseDate() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getDisclosureStatus())) {
			sql.append(" and dn.disclosure_status = '" + params.getModel().getDisclosureStatus() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getEffectStatus())) {
			sql.append(" and dn.effect_status = '" + params.getModel().getEffectStatus() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getId())) {
			sql.append(" and dn.id = '" + params.getModel().getId() + "'");
		}
		sql.append(" order by dn.crt_date desc,dn.crt_time desc");
		return super.findRows(sql.toString(), DataSourceProperty.IDB, params);
	}

	public SqlResult<DisclosureNotice>  findDisclosureManualNoticesAuth(SqlParam<DisclosureNotice> params) throws Exception {
		StringBuilder sql =
				new StringBuilder(
						"SELECT dn.id, dnv.id notice_version_id, dn.disclosure_type, dn.disclosure_son_type, po.prod_nm prod_name, dn.prod_code prod_code, " +
								"  dn.report_type, dn.notice_title, " +
								"  dn.PROD_CLC_MTH, " +
								"  dn.PROD_FORM, " +
								"  dn.INV_PRD_DIME, " +
								"  dn.INV_PRD_LEN, " +
								"  dn.PROD_OBJ, " +
								"  dn.PROD_SER_CD, " +
								"  dn.task_id, dn.plan_fb_date, dn.plan_sp_date, dn.plan_bl_date, dn.real_bl_date, dn.real_sp_date, dn.real_fb_date, " +
								"  dn.prod_base_date, dn.disclosure_status, dn.review_status, dn.report_start_date, dn.report_end_date, dn.is_send_email, " +
								"  dn.effect_status, dnv.file_name, dnv.file_path, dnv.crt_path, dn.crt_date, dn.crt_time, dn.crt_user_id, dn.crt_user_name, " +
								"  dn.upd_date, dn.upd_time, dn.upd_user_id, dn.upd_user_name, " +
								"  dnv.notice_version disclosure_version" +
								"  FROM idb_disclosure_notice dn " +
								"  LEFT JOIN APP_PRD_BAS_INF po ON po.prod_cd = dn.prod_code " +
								"  LEFT JOIN idb_disclosure_notice_version dnv ON dnv.t8_disclosure_notice_id = dn.id " +
								"  LEFT JOIN (SELECT DISTINCT k.PROD_SER_CD,k.PROD_SER_NM FROM APP_PRD_BAS_INF k) sr ON dn.PROD_SER_CD = sr.PROD_SER_CD" +
								"  WHERE 1 = 1  and is_manual_notice='1' and (delete_flag is null or delete_flag='0') and dnv.id in (select max(CONVERT ( dnv.id, SIGNED )) notice_version_id  from idb_disclosure_notice_version dnv group by dnv.t8_disclosure_notice_id) " );
		if (StringUtils.isNotBlank(params.getModel().getDisclosureType())) {
			sql.append(" and dn.disclosure_type = '" + params.getModel().getDisclosureType() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getDisclosureSonType())) {
			sql.append(" and dn.disclosure_son_type = '" + params.getModel().getDisclosureSonType() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdName())) {
			sql.append(" and po.prod_nm like '%" + params.getModel().getProdName() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql.append(" and po.prod_cd like '%" + params.getModel().getProdCode() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdBaseDate())) {
			sql.append(" and dn.prod_base_date like '%" + params.getModel().getProdBaseDate() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getDisclosureStatus())) {
			sql.append(" and dn.disclosure_status = '" + params.getModel().getDisclosureStatus() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getEffectStatus())) {
			sql.append(" and dn.effect_status = '" + params.getModel().getEffectStatus() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getId())) {
			sql.append(" and dn.id = '" + params.getModel().getId() + "'");
		}
		sql.append("  order by dn.crt_date desc,dn.crt_time desc");
		return super.findRows(sql.toString(), DataSourceProperty.IDB, params);
	}

}
