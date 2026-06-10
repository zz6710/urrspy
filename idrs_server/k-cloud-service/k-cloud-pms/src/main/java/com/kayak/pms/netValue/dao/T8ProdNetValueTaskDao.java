package com.kayak.pms.netValue.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.disclosureControl.model.DisclosureGridValue;
import com.kayak.pms.global.constants.DisclosureSonType;
import com.kayak.pms.global.constants.DisclosureType;
import com.kayak.pms.global.constants.TaskStatus;
import com.kayak.pms.netValue.model.T8ProdNetValueTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;

@Repository
public class T8ProdNetValueTaskDao extends ComnDao {

	public SqlResult<T8ProdNetValueTask> findT8ProdNetValueTasks(SqlParam<T8ProdNetValueTask> params) throws Exception {
		String sql = "  SELECT * FROM (SELECT \n" +
				"task.id,\n" +
				"noticeInfo.notice_id,\n" +
				"task.disclosure_type,\n" +
				"task.disclosure_son_type,\n" +
				"IFNULL(prodInfo.count,'0') `count`,\n" +
				"prodInfo.prod_codes,\n" +
				"prodInfo.prod_names,\n" +
				"task.prod_base_date,\n" +
				"task.status,\n" +
				"task.data_source,\n" +
				"noticeInfo.ver_max_id,\n" +
				"noticeInfo.disclosure_mod_version_id,\n" +
				"noticeInfo.notice_doc_name,\n" +
				"noticeInfo.notice_version,\n" +
				"noticeInfo.mod_doc_name,\n" +
				"noticeInfo.mod_version_number,\n" +
				"noticeInfo.disclosure_status,\n" +
				"noticeInfo.is_notice_pub,\n" +
				"task.crt_date,\n" +
				"task.crt_user_name,\n" +
				"task.PROD_CLC_MTH,\n" +
				"task.PROD_FORM,\n" +
				"task.INV_PRD_DIME,\n" +
				"task.INV_PRD_LEN,\n" +
				"task.PROD_OBJ,\n" +
				"task.PROD_SER_CD,\n" +
				"task.prod_inv_typ,\n" +
				"task.t8_disclosure_prod_rule_id,\n" +
				"task.t8_disclosure_rule_id,\n" +
				"task.notice_title,\n" +
				"task.crt_task_date,\n" +
				"task.sys_crt_date,\n" +
				"task.task_month,\n" +
				"task.remark\n" +
				"FROM\n" +
				"/*--任务及规则配置基本信息start--*/\n" +
				"(SELECT \n" +
				"ta.id,\n" +
				"ta.PROD_CLC_MTH,\n" +
				"ta.PROD_FORM,\n" +
				"ta.PROD_OBJ,\n" +
				"ta.PROD_SER_CD,\n" +
				"ta.prod_inv_typ,\n" +
				"ta.INV_PRD_DIME,\n" +
				"ta.INV_PRD_LEN,\n" +
				"ta.prod_base_date,\n" +
				"ru.net_value_date ,\n" +
				"ta.disclosure_type,\n" +
				"ta.disclosure_son_type,\n" +
				"ta.status,\n" +
				"ta.data_source,\n" +
				"ta.crt_date,\n" +
				"ta.crt_user_name,\n" +
				"ta.t8_disclosure_prod_rule_id,\n" +
				"ta.t8_disclosure_rule_id,\n" +
				"ta.crt_task_date,\n" +
				"ta.sys_crt_date,\n" +
				"ta.task_month,\n" +
				"ta.notice_title,\n" +
				"ta.remark\n" +
				"FROM idb_disclosure_prod_task ta\n" +
				"LEFT JOIN idb_disclosure_rule ru\n" +
				"ON IFNULL(ta.PROD_CLC_MTH,'') = IFNULL(ru.PROD_CLC_MTH,'')\n" +
				"AND IFNULL(ta.PROD_OBJ,'') = IFNULL(ru.PROD_OBJ,'')\n" +
				"AND IFNULL(ta.PROD_FORM,'') = IFNULL(ru.PROD_FORM,'')\n" +
				"AND IFNULL(ta.PROD_SER_CD,'') = IFNULL(ru.PROD_SER_CD,'')\n" +
				"AND IFNULL(ta.prod_inv_typ,'') = IFNULL(ru.prod_inv_typ,'')\n" +
				"AND IFNULL(ta.INV_PRD_DIME,'') = IFNULL(ru.INV_PRD_DIME,'')\n" +
				"AND IFNULL(ta.INV_PRD_LEN,'') = IFNULL(ru.INV_PRD_LEN,'')\n" +
				"AND ru.disclosure_type = '9'\n" +
				"AND ru.disclosure_son_type = '0903'\n" +
				"WHERE ta.disclosure_type = '9' AND ta.disclosure_son_type = '0903')task\n" +
				"/*--任务及规则配置基本信息end--*/\n" +
				"LEFT JOIN \n" +
				"/*--符合任务产品参数的产品基本信息start--*/\n" +
				"(SELECT COUNT(tp.prod_code) `count`,\n" +
				"GROUP_CONCAT(tp.prod_code) prod_codes,\n" +
				"GROUP_CONCAT(tp.prod_name) prod_names,\n" +
				"tp.prod_base_date,\n" +
				"tp.task_id,\n" +
				"tp.PROD_CLC_MTH,\n" +
				"tp.PROD_FORM,\n" +
				"tp.INV_PRD_DIME,\n" +
				"tp.INV_PRD_LEN,\n" +
				"tp.PROD_OBJ,\n" +
				"tp.PROD_SER_CD,\n" +
				"tp.prod_inv_typ,\n" +
				"tp.net_value_date,\n" +
				"tp.status,\n" +
				"tp.crt_date,\n" +
				"tp.crt_user_name,\n" +
				"tp.data_source,\n" +
				"tp.disclosure_type,\n" +
				"tp.disclosure_son_type,\n" +
				"tp.t8_disclosure_prod_rule_id,\n" +
				"tp.t8_disclosure_rule_id,\n" +
				"tp.crt_task_date,\n" +
				"tp.sys_crt_date,\n" +
				"tp.task_month,\n" +
				"tp.remark\n" +
				" FROM (SELECT * FROM (\n" +
				"SELECT np.prod_base_date,\n" +
				"np.public_day,\n" +
				"np.prod_code,\n" +
				"np.prod_name,\n" +
				"np.start_date,\n" +
				"np.end_date,\n" +
				"task.id task_id,\n" +
				"task.PROD_CLC_MTH,\n" +
				"task.PROD_FORM,\n" +
				"task.INV_PRD_DIME,\n" +
				"task.INV_PRD_LEN,\n" +
				"task.PROD_OBJ,\n" +
				"task.PROD_SER_CD,\n" +
				"task.prod_inv_typ,\n" +
				"task.net_value_date,\n" +
				"task.status,\n" +
				"task.crt_date,\n" +
				"task.crt_user_name,\n" +
				"task.data_source,\n" +
				"task.disclosure_type,\n" +
				"task.disclosure_son_type,\n" +
				"task.t8_disclosure_prod_rule_id,\n" +
				"task.t8_disclosure_rule_id,\n" +
				"task.crt_task_date,\n" +
				"task.sys_crt_date,\n" +
				"task.task_month,\n" +
				"task.remark,\n" +
				"np.flag /*生成规则配置的公告发布日标识 1-净值发布日，2-到期日，3-产品开放日*/\n" +
				"FROM (SELECT ta.*,ru.net_value_date FROM idb_disclosure_prod_task ta\n" +
				"LEFT JOIN idb_disclosure_rule ru\n" +
				"ON IFNULL(ta.PROD_CLC_MTH,'') = IFNULL(ru.PROD_CLC_MTH,'')\n" +
				"AND IFNULL(ta.PROD_OBJ,'') = IFNULL(ru.PROD_OBJ,'')\n" +
				"AND IFNULL(ta.PROD_FORM,'') = IFNULL(ru.PROD_FORM,'')\n" +
				"AND IFNULL(ta.PROD_SER_CD,'') = IFNULL(ru.PROD_SER_CD,'')\n" +
				"AND IFNULL(ta.prod_inv_typ,'') = IFNULL(ru.prod_inv_typ,'')\n" +
				"AND IFNULL(ta.INV_PRD_DIME,'') = IFNULL(ru.INV_PRD_DIME,'')\n" +
				"AND IFNULL(ta.INV_PRD_LEN,'') = IFNULL(ru.INV_PRD_LEN,'')\n" +
				"AND ru.disclosure_type = '9'\n" +
				"AND ru.disclosure_son_type = '0903'\n" +
				" WHERE ta.disclosure_type = '9' AND ta.disclosure_son_type = '0903')task\n" +
				"LEFT JOIN \n" +
				"(\n" +
				"SELECT net.ISU_DT prod_base_date,\n" +
				"net.ISU_DT public_day,\n" +
				"net.prod_cd prod_code,\n" +
				"net.IS_BUT_CNF_DT,/*0-不是产品申购开放日，1-是*/\n" +
				"net.IS_RDM_CNF_DT,/*0-不是产品赎回开放日，1-是*/\n" +
				"CASE \n" +
				"WHEN net.IS_BUT_CNF_DT='0' AND  net.IS_RDM_CNF_DT='0' AND net.ISU_DT<>prod.MTU_DT/*不是产品申赎开放日且不是到期日的净值发布日*/\n" +
				"THEN '1'\n" +
				"WHEN net.IS_BUT_CNF_DT='0' AND  net.IS_RDM_CNF_DT='0' AND net.ISU_DT=prod.MTU_DT/*不是产品申赎开放日但为到期日的净值发布日*/\n" +
				"THEN '2'\n" +
				"WHEN net.IS_BUT_CNF_DT='1' AND  net.IS_RDM_CNF_DT='1' AND net.ISU_DT<>prod.MTU_DT/*是产品申赎开放日但不是到期日的净值发布日*/\n" +
				"THEN '3'\n" +
				"END flag,/*生成规则配置的公告发布日标识 1-净值发布日，2-到期日，3-产品开放日*/\n" +
				"prod.prod_nm prod_name,\n" +
				"prod.FOUND_DT start_date,\n" +
				"prod.MTU_DT end_date,\n" +
				"prod.PROD_CLC_MTH,\n" +
				"prod.PROD_FORM,\n" +
				"prod.INV_PRD_DIME,\n" +
				"prod.INV_PRD_LEN,\n" +
				"prod.PROD_OBJ,\n" +
				"prod.PROD_SER_CD,\n" +
				"prod.prod_inv_typ\n" +
				"FROM app_prd_nav_inf net\n" +
				"LEFT JOIN app_prd_bas_inf prod \n" +
				"ON prod.prod_cd = net.prod_cd\n" +
				"WHERE 1=1 AND (net.prod_cd <>'' OR net.prod_cd IS NOT NULL)) np \n" +
				"\n" +
				"ON (FIND_IN_SET(np.PROD_CLC_MTH,task.PROD_CLC_MTH) OR task.PROD_CLC_MTH IS NULL OR task.PROD_CLC_MTH ='')\n" +
				"AND (FIND_IN_SET(np.PROD_FORM ,task.PROD_FORM) OR task.PROD_FORM IS NULL OR task.PROD_FORM ='')\n" +
				"AND (FIND_IN_SET(np.PROD_OBJ,task.PROD_OBJ) OR task.PROD_OBJ IS NULL OR task.PROD_OBJ ='')\n" +
				"AND (FIND_IN_SET(np.PROD_SER_CD,task.PROD_SER_CD) OR task.PROD_SER_CD IS NULL OR task.PROD_SER_CD ='')\n" +
				"AND (FIND_IN_SET(np.prod_inv_typ,task.prod_inv_typ) OR task.prod_inv_typ IS NULL OR task.prod_inv_typ ='') \n" +
				"AND (FIND_IN_SET(np.INV_PRD_DIME,task.INV_PRD_DIME) OR task.INV_PRD_DIME IS NULL OR task.INV_PRD_DIME ='') \n" +
				"AND (FIND_IN_SET(np.INV_PRD_LEN,task.INV_PRD_LEN) OR task.INV_PRD_LEN IS NULL OR task.INV_PRD_LEN ='') \n" +
				"AND task.prod_base_date = np.prod_base_date ) tr\n" +
				"WHERE  FIND_IN_SET(tr.flag,tr.net_value_date))tp GROUP BY tp.task_id)prodInfo \n" +
				"/*--符合任务产品参数的产品基本信息end--*/\n" +
				"\n" +
				"ON IFNULL(prodInfo.PROD_CLC_MTH,'') = IFNULL(task.PROD_CLC_MTH,'')\n" +
				"AND IFNULL(prodInfo.PROD_OBJ,'') = IFNULL(task.PROD_OBJ,'')\n" +
				"AND IFNULL(prodInfo.PROD_FORM,'') = IFNULL(task.PROD_FORM,'')\n" +
				"AND IFNULL(prodInfo.PROD_SER_CD,'') = IFNULL(task.PROD_SER_CD,'')\n" +
				"AND IFNULL(prodInfo.prod_inv_typ,'') = IFNULL(task.prod_inv_typ,'')\n" +
				"AND IFNULL(prodInfo.INV_PRD_DIME,'') = IFNULL(task.INV_PRD_DIME,'')\n" +
				"AND IFNULL(prodInfo.INV_PRD_LEN,'') = IFNULL(task.INV_PRD_LEN,'')\n" +
				"AND prodInfo.prod_base_date = task.prod_base_date\n" +
				"\n" +
				"LEFT JOIN \n" +
				"(SELECT \n" +
				"t.id ver_max_id,\n" +
				"notVer.t8_disclosure_notice_id,\n" +
				"notVer.disclosure_mod_version_id,\n" +
				"notVer.notice_version,\n" +
				"notVer.file_name notice_doc_name,\n" +
				"modVer.doc_name mod_doc_name,\n" +
				"modVer.version mod_version_number,\n" +
				"notice.id notice_id,\n" +
				"notice.disclosure_type,\n" +
				"notice.disclosure_son_type,\n" +
				"notice.prod_base_date,\n" +
				"notice.PROD_CLC_MTH,\n" +
				"notice.PROD_FORM,\n" +
				"notice.PROD_OBJ,\n" +
				"notice.PROD_SER_CD,\n" +
				"notice.prod_inv_typ,\n" +
				"notice.INV_PRD_DIME,\n" +
				"notice.INV_PRD_LEN,\n" +
				"notice.disclosure_status,\n" +
				"notVer.is_notice_pub,\n" +
				"'2' `status`\n" +
				"FROM \n" +
				"(SELECT MAX(CONVERT(t.id,SIGNED)) id \n" +
				"FROM \n" +
				"idb_disclosure_notice_version t \n" +
				"GROUP BY t.t8_disclosure_notice_id ) t\n" +
				"LEFT JOIN idb_disclosure_notice_version notVer \n" +
				"ON t.id = notVer.id\n" +
				"LEFT JOIN idb_disclosure_mod_version modVer\n" +
				"ON modVer.id = notVer.disclosure_mod_version_id\n" +
				"LEFT JOIN idb_disclosure_notice notice \n" +
				"ON notice.id = notVer.t8_disclosure_notice_id \n" +
				"WHERE notice.disclosure_type = '9' \n" +
				"AND notice.disclosure_son_type = '0903' \n" +
				") noticeInfo\n" +
				"\n" +
				"ON IFNULL(noticeInfo.PROD_CLC_MTH,'') = IFNULL(task.PROD_CLC_MTH,'')\n" +
				"AND IFNULL(noticeInfo.PROD_OBJ,'') = IFNULL(task.PROD_OBJ,'')\n" +
				"AND IFNULL(noticeInfo.PROD_FORM,'') = IFNULL(task.PROD_FORM,'')\n" +
				"AND IFNULL(noticeInfo.PROD_SER_CD,'') = IFNULL(task.PROD_SER_CD,'')\n" +
				"AND IFNULL(noticeInfo.prod_inv_typ,'') = IFNULL(task.prod_inv_typ,'')\n" +
				"AND IFNULL(noticeInfo.INV_PRD_DIME,'') = IFNULL(task.INV_PRD_DIME,'')\n" +
				"AND IFNULL(noticeInfo.INV_PRD_LEN,'') = IFNULL(task.INV_PRD_LEN,'')\n" +
				"AND noticeInfo.prod_base_date= IF(task.status='2',task.prod_base_date,'')\n" +
				"AND task.status = noticeInfo.status) allInfo WHERE 1=1\n";

		if (StringUtils.isNotEmpty(params.getModel().getStartDate())) {
			sql += " and allInfo.prod_base_date >=$S{startDate}\n";
		}
		if (StringUtils.isNotEmpty(params.getModel().getEndDate())) {
			sql += " and allInfo.prod_base_date <=$S{endDate}\n";
		}
		if (StringUtils.isNotEmpty(params.getModel().getStatus())) {
			sql += " and allInfo.status =$S{status}\n";
		}
		sql+="ORDER BY allInfo.prod_base_date DESC";
		return super.findRows(sql,
				DataSourceProperty.IDB,params);
	}

	/**
	 * 功能：查询净值披露任务list
	 * 作者：rennannan
	 * 日期：20210626
	 *
	 * @param param
	 * @return
	 */
	public List<T8ProdNetValueTask> findT8ProdNetValueTasksList(T8ProdNetValueTask param) throws Exception {
		String sql = "SELECT id,task_date,task_name,task_status,task_desc," +
				" crt_date,crt_time," +
				" confirm_user_id,confirm_user_name,confirm_date,confirm_time " +
				" FROM t8_prod_net_value_task where 1=1 ";
		if (StringUtils.isNotEmpty(param.getTaskDate())) {
			sql += " and task_date = $S{taskDate}";
		}
		return super.findRows(T8ProdNetValueTask.class, sql, 0, param);
	}


	public SqlResult<T8ProdNetValueTask> findNetValueProdTasksList(SqlParam<T8ProdNetValueTask> params) throws Exception {
		String sql ="SELECT tr.prod_code prod_cd,  " +
				" tr.prod_name prod_nm,  " +
				"tr.asset_value,  " +
				"tr.net_price,  " +
				"tr.nav_dt,  " +
				"tr.pos_dt   " +
				" FROM (  " +
				"SELECT np.prod_base_date,  " +
				"np.prod_code,  " +
				"np.prod_name,  " +
				"task.id task_id,  " +
				"task.net_value_date,  " +
				"np.flag, /*生成规则配置的公告发布日标识 1-净值发布日，2-到期日，3-产品开放日*/  " +
				"np.asset_value,  " +
				"np.net_price,  " +
				"np.nav_dt,  " +
				"np.pos_dt  " +
				"FROM (SELECT ta.*,ru.net_value_date FROM idb_disclosure_prod_task ta  " +
				"LEFT JOIN idb_disclosure_rule ru  " +
				"ON IFNULL(ta.PROD_CLC_MTH,'') = IFNULL(ru.PROD_CLC_MTH,'')  " +
				"AND IFNULL(ta.PROD_OBJ,'') = IFNULL(ru.PROD_OBJ,'')  " +
				"AND IFNULL(ta.PROD_FORM,'') = IFNULL(ru.PROD_FORM,'')  " +
				"AND IFNULL(ta.PROD_SER_CD,'') = IFNULL(ru.PROD_SER_CD,'')  " +
				"AND IFNULL(ta.prod_inv_typ,'') = IFNULL(ru.prod_inv_typ,'')  " +
				"AND IFNULL(ta.INV_PRD_DIME,'') = IFNULL(ru.INV_PRD_DIME,'')  " +
				"AND IFNULL(ta.INV_PRD_LEN,'') = IFNULL(ru.INV_PRD_LEN,'')  " +
				"AND ru.disclosure_type = '9'  " +
				"AND ru.disclosure_son_type = '0903'  " +
				" WHERE ta.disclosure_type = '9' AND ta.disclosure_son_type = '0903')task  " +
				"LEFT JOIN   " +
				"(  " +
				"SELECT net.ISU_DT prod_base_date,  " +
				"net.ISU_DT public_day,  " +
				"net.prod_cd prod_code,  " +
				"ROUND(IFNULL(net.TOT_NAV,0),2) AS asset_value,  " +
				"ROUND(IFNULL(net.UNT_NAV, 0),4) AS net_price,  " +
				"DATE_FORMAT(net.NAV_DT , '%Y%m%d') AS nav_dt,   " +
				"net.ISU_DT AS pos_dt,/*净值日第二天*/   " +
				"CASE   " +
				"WHEN net.IS_BUT_CNF_DT='0' AND  net.IS_RDM_CNF_DT='0' AND net.ISU_DT<>prod.MTU_DT/*不是产品申赎开放日且不是到期日的净值发布日*/  " +
				"THEN '1'  " +
				"WHEN net.IS_BUT_CNF_DT='0' AND  net.IS_RDM_CNF_DT='0' AND net.ISU_DT=prod.MTU_DT/*不是产品申赎开放日但为到期日的净值发布日*/  " +
				"THEN '2'  " +
				"WHEN net.IS_BUT_CNF_DT='1' AND  net.IS_RDM_CNF_DT='1' AND net.ISU_DT<>prod.MTU_DT/*是产品申赎开放日但不是到期日的净值发布日*/  " +
				"THEN '3'  " +
				"END flag,/*生成规则配置的公告发布日标识 1-净值发布日，2-到期日，3-产品开放日*/  " +
				"prod.prod_nm prod_name,  " +
				"prod.FOUND_DT start_date,  " +
				"prod.MTU_DT end_date,  " +
				"prod.PROD_CLC_MTH,  " +
				"prod.PROD_FORM,  " +
				"prod.INV_PRD_DIME,  " +
				"prod.INV_PRD_LEN,  " +
				"prod.PROD_OBJ,  " +
				"prod.PROD_SER_CD,  " +
				"prod.prod_inv_typ  " +
				"FROM app_prd_nav_inf net  " +
				"LEFT JOIN app_prd_bas_inf prod   " +
				"ON prod.prod_cd = net.prod_cd  " +
				"WHERE 1=1 AND (net.prod_cd <>'' OR net.prod_cd IS NOT NULL)) np   " +
				"ON (FIND_IN_SET(np.PROD_CLC_MTH,task.PROD_CLC_MTH) OR task.PROD_CLC_MTH IS NULL OR task.PROD_CLC_MTH ='')  " +
				"AND (FIND_IN_SET(np.PROD_FORM ,task.PROD_FORM) OR task.PROD_FORM IS NULL OR task.PROD_FORM ='')  " +
				"AND (FIND_IN_SET(np.PROD_OBJ,task.PROD_OBJ) OR task.PROD_OBJ IS NULL OR task.PROD_OBJ ='')  " +
				"AND (FIND_IN_SET(np.PROD_SER_CD,task.PROD_SER_CD) OR task.PROD_SER_CD IS NULL OR task.PROD_SER_CD ='')  " +
				"AND (FIND_IN_SET(np.prod_inv_typ,task.prod_inv_typ) OR task.prod_inv_typ IS NULL OR task.prod_inv_typ ='')   " +
				"AND (FIND_IN_SET(np.INV_PRD_DIME,task.INV_PRD_DIME) OR task.INV_PRD_DIME IS NULL OR task.INV_PRD_DIME ='')   " +
				"AND (FIND_IN_SET(np.INV_PRD_LEN,task.INV_PRD_LEN) OR task.INV_PRD_LEN IS NULL OR task.INV_PRD_LEN ='')   " +
				"AND task.prod_base_date = np.prod_base_date ) tr  " +
				"WHERE  FIND_IN_SET(tr.flag,tr.net_value_date) AND tr.task_id =$S{taskId} AND tr.prod_base_date = $S{posDt}";
		return super.findRows(sql,
				DataSourceProperty.IDB, params);
	}

	public UpdateResult addT8ProdNetValueTask(SqlParam<T8ProdNetValueTask> params) throws Exception {
		return super.update("INSERT INTO t8_prod_net_value_task(id,task_date,task_name,task_status,task_desc,crt_date,crt_time,confirm_user_id,confirm_user_name,confirm_date,confirm_time) VALUES($AUTOIDS{id},$S{taskDate},$S{taskName},$S{taskStatus},$S{taskDesc},$S{crtDate},$S{crtTime},$S{confirmUserId},$S{confirmUserName},$S{confirmDate},$S{confirmTime})",
				params.getModel());
	}

	public UpdateResult updateT8ProdNetValueTask(SqlParam<T8ProdNetValueTask> params) throws Exception {
		return super.update("UPDATE t8_prod_net_value_task SET task_date=$S{taskDate} ,task_name=$S{taskName} ,task_status=$S{taskStatus} ,task_desc=$S{taskDesc} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,confirm_user_id=$S{confirmUserId} ,confirm_user_name=$S{confirmUserName} ,confirm_date=$S{confirmDate} ,confirm_time=$S{confirmTime}  WHERE  id=$S{id} ",
				params.getModel());
	}

	/**
	 * 功能：确认披露任务
	 * 作者：rennannan
	 * 日期：20210629
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int confirmNetValueTask(SqlParam<T8ProdNetValueTask> params) throws Exception {
		String sql = " UPDATE t8_prod_net_value_task" +
				" set task_status=$S{taskStatus}," +
				"  confirm_user_id=$S{confirmUserId} ," +
				" confirm_user_name=$S{confirmUserName} ," +
				" confirm_date=$S{confirmDate} ," +
				" confirm_time=$S{confirmTime}" +
				" where id=$S{id}";
		return super.update(sql, params.getModel()).getEffect();
	}

	public UpdateResult deleteT8ProdNetValueTask(SqlParam<T8ProdNetValueTask> params) throws Exception {
		return super.update("DELETE FROM t8_prod_net_value_task WHERE  id=$S{id} ",
				params.getModel());
	}

	/**
	 * 功能：插入净值披露任务 传入参数T8ProdNetValueTask
	 * 作者：rennannan
	 * 日期：20210626
	 *
	 * @param task
	 * @return
	 * @throws Exception
	 */
	public String insertProdNetValueTask(T8ProdNetValueTask task) throws Exception {
		String sql = "INSERT INTO t8_prod_net_value_task(id,task_date,task_name,task_status,task_desc," +
				" crt_date,crt_time) " +
				" VALUES($AUTOIDS{id},$S{taskDate},$S{taskName},$S{taskStatus},$S{taskDesc}," +
				" $S{crtDate},$S{crtTime})";
		return super.update(sql, task).getAutoId();
	}

	public UpdateResult updateT8ProdNetValueTaskForDes(String des,String taskId) throws Exception {
		return super.update("UPDATE t8_prod_net_value_task SET task_desc='"+des+"' WHERE  id='"+taskId+"' ",
				des);
	}

	public UpdateResult updateNetValueTaskForDes(String taskId) throws Exception {
		List<SqlRow> row = super.findRows("SELECT notice.id,notice.t8_disclosure_task_id,notice.prod_code,notice.netval_date," +
				" notice.disclosure_date,notice.total_net,notice.total_vol,notice.nav,notice.nav_profit," +
				" notice.ten_thousand_income_amt,notice.seven_days_income_rate,notice.total_nav," +
				" notice.crt_date,notice.crt_time,notice.crt_user_id,notice.crt_user_name," +
				" notice.upd_date,notice.upd_time,notice.upd_user_id,notice.upd_user_name," +
				" notice.remark,notice.status," +
				" prod.prod_name " +
				" FROM t8_prod_net_value_notice notice" +
				" left join t8_prod_info prod " +
				"        on notice.prod_code = prod.prod_code where t8_disclosure_task_id='"+taskId+"'", taskId);
		Integer count = 0;
		if(row!=null&&row.size()>0){
			count = row.size();
		}
		String des = "当前有"+count+"只产品需要披露";
		return super.update("UPDATE t8_prod_net_value_task SET task_desc='"+des+"' WHERE  id='"+taskId+"' ",
				des);
	}
}
