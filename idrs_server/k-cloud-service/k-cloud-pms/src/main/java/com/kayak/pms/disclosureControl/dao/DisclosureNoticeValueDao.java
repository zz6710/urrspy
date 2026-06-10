package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.pms.basePublish.model.DisclosureWordDate;
import com.kayak.pms.disclosureControl.model.*;
import com.kayak.pms.global.constants.DisclosureSonType;
import com.kayak.pms.global.constants.DisclosureType;
import com.kayak.pms.global.constants.IsSysvalue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DisclosureNoticeValueDao extends ComnDao {

	public SqlResult<DisclosureNoticeValue> findDisclosureNoticeValues(SqlParam<DisclosureNoticeValue> params) throws Exception {
		return super.findRows("SELECT id,t8_disclosure_notice_id,t8_disclosure_version_id,prod_code,data_date,column_label,column_key,column_value,isdisplay,roleids,is_sysvalue,dict,functype,confoption,source_type,seq_numbers,file_name,upload_path,view_url,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,remark FROM idb_disclosure_notice_value", params);
	}

	public List<SqlRow> findDisclosureNoticeValuesByKey(DisclosureNoticeValue params) throws Exception {
		return super.findRows("SELECT max(id),t8_disclosure_notice_id,column_label,column_key,column_value FROM idb_disclosure_notice_value WHERE t8_disclosure_notice_id='"+params.getT8DisclosureNoticeId()+"' and column_key='"+params.getColumnKey()+"' and column_value is not null and column_value!=''", params);
	}


	public List<DisclosureNoticeValue> findDisclosureNoticeValues(Map<String,Object> params) throws Exception {
		return super.findRows(DisclosureNoticeValue.class, "select v.id,t8_disclosure_notice_id,t8_disclosure_version_id,v.prod_code,data_date,column_label,column_key,column_value,isdisplay,roleids,is_sysvalue,dict,functype,confoption,source_type,seq_numbers,file_name,upload_path,view_url from idb_disclosure_notice_value v LEFT JOIN idb_disclosure_notice n on v.t8_disclosure_notice_id = n.id where "
				+ "n.prod_base_date =$S{prod_base_date} and n.disclosure_son_type = $S{disclosure_son_type} and n.prod_code = $S{prod_code} and v.roleids='14' and column_key !='product_operation_analysis' and column_key !='product_investment_strategy'", 0, params);
	}

	public UpdateResult addDisclosureNoticeValue(DisclosureNoticeValue params) throws Exception {
		return super.update("INSERT INTO idb_disclosure_notice_value(id,t8_disclosure_notice_id,t8_disclosure_version_id,prod_code," +
						"data_date,column_label,column_key,column_value,isdisplay,roleids,is_sysvalue,dict,functype,data_type,confoption,source_type,seq_numbers," +
						"file_name,upload_path,view_url,crt_date,crt_time,crt_user_id,crt_user_name,remark) " +
						"VALUES($AUTOIDS{id},$S{t8DisclosureNoticeId},$S{t8DisclosureVersionId},$S{prodCode},$S{dataDate},$S{columnLabel},$S{columnKey}," +
						"$S{columnValue},$S{isdisplay},$S{roleids},$S{isSysvalue},$S{dict},$S{functype},$S{dataType},$S{confoption},$S{sourceType},$S{seqNumbers},$S{fileName}," +
						"$S{uploadPath},$S{viewUrl},$S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName},$S{remark})",
				DataSourceProperty.IDB,params);
	}

	public UpdateResult addNoticeGridConfigValue(DisclosureGridConfig params) throws Exception {
		return super.update("INSERT INTO idb_notice_grid_config(id,disclosure_mod_version_id,replace_str,column_name,row_order,column_order,value_table_name,value_column_code,exeid,merge_row_num,merge_column_num) " +
						"VALUES($AUTOIDI{id},$S{disclosureModVersionId},$S{replaceStr},$S{columnName},$I{rowOrder},$I{columnOrder},$S{valueTableName},$S{valueColumnCode},$S{exeid},$I{mergeRowNum},$I{mergeColumnNum})",
				DataSourceProperty.IDB,params);
	}

	public UpdateResult addNoticeGridNetValue(DisclosureGridValue params) throws Exception {
		return super.update("INSERT INTO app_grid_netprice_source_data(id,notice_version_id,prod_nm,asset_value,net_price) " +
						"VALUES($AUTOIDI{id},$S{noticeVersionId},$S{prodNm},$S{assetValue},$S{netPrice})",
				DataSourceProperty.IDB,params);
	}

	public UpdateResult addNoticeGridAssetConfigVal(DisclosureGridValue params) throws Exception {
		return super.update("INSERT INTO app_grid_asset_config_analysis_data(id,notice_version_id,invest_way,invest_type,balance_amt,ratio) " +
						"VALUES($AUTOIDI{id},$S{noticeVersionId},$S{investWay},$S{investType},$S{balanceAmt},$S{ratio})",
				DataSourceProperty.PUB,params);
	}

	public UpdateResult addNoticeGridFBassetHoldingVal(DisclosureGridValue params) throws Exception {
		return super.update("INSERT INTO app_grid_fbasset_holding_analysis_data(id,notice_version_id,row_num,finance_customer,project_name,left_days,income_allocate,deal_structure,risk_conditions) " +
						"VALUES($AUTOIDI{id},$S{noticeVersionId},$S{rowNum},$S{financeCustomer},$S{projectName},$S{leftDays},$S{incomeAllocate},$S{dealStructure},$S{riskConditions})",
				DataSourceProperty.PUB,params);
	}

	public UpdateResult addNoticeGridAffiliateVal(DisclosureGridValue params) throws Exception {
		return super.update("INSERT INTO app_grid_netprice_source_data(id,notice_version_id,prod_nm,asset_value,net_price) " +
						"VALUES($AUTOIDI{id},$S{noticeVersionId},$S{prodNm},$S{assetValue},$S{netPrice})",
				DataSourceProperty.PUB,params);
	}

	public UpdateResult updateDisclosureNoticeValue(SqlParam<DisclosureNoticeValue> params) throws Exception {
		return super.update("UPDATE idb_disclosure_notice_value SET t8_disclosure_notice_id=$S{t8DisclosureNoticeId} ,t8_disclosure_version_id=$S{t8DisclosureVersionId} ,prod_code=$S{prodCode} ,data_date=$S{dataDate} ,column_label=$S{columnLabel} ,column_key=$S{columnKey} ,column_value=$S{columnValue} ,isdisplay=$S{isdisplay} ,roleids=$S{roleids} ,is_sysvalue=$S{isSysvalue} ,dict=$S{dict} ,functype=$S{functype} ,confoption=$S{confoption} ,source_type=$S{sourceType} ,seq_numbers=$S{seqNumbers}, file_name=$S{fileName} ,upload_path=$S{uploadPath} ,view_url=$S{viewUrl} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,crt_user_id=$S{crtUserId} ,crt_user_name=$S{crtUserName} ,upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user_id=$S{updUserId} ,upd_user_name=$S{updUserName} ,remark=$S{remark}  WHERE  id=$S{id} ",
				params.getModel());
	}

	public UpdateResult updateDisclosureNoticeImagePath(DisclosureNoticeValue params) throws Exception {
		return super.update("UPDATE idb_disclosure_notice_value SET column_value=$S{columnValue}  WHERE  t8_disclosure_notice_id=$S{t8DisclosureNoticeId} and  column_key=$S{columnKey}",
				params);
	}

	public UpdateResult updateDisclosureNoticeRate(String columnValue,String t8DisclosureNoticeId,String columnKey ) throws Exception {

		return super.update("UPDATE idb_disclosure_notice_value SET column_value='"+columnValue+"'  WHERE  t8_disclosure_notice_id='"+t8DisclosureNoticeId+"' and  column_key='"+columnKey+"'",
				null);
	}

	public UpdateResult deleteDisclosureNoticeValue(SqlParam<DisclosureNoticeValue> params) throws Exception {
		return super.update("DELETE FROM idb_disclosure_notice_value WHERE  id=$S{id} ",
				params.getModel());
	}
	/**
	* @功能描述:查询对应的净值任务当日所需发布的产品
	* @params:[disclosureGridValue]
	* @return:java.util.List<com.kayak.pms.disclosureControl.model.DisclosureGridValue>
	* @Athor:ouyifan
	* @date:2022/9/8
	*/
	public List<DisclosureGridValue> findNetProdValue(DisclosureGridValue disclosureGridValue) throws Exception {
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
		return super.findRows(DisclosureGridValue.class, sql,
				DataSourceProperty.IDB, disclosureGridValue);
	}
	/**
	* @功能描述:查找NoticeValue的初始化字段数据
	* @params:[params]
	* @return:java.util.List<com.kayak.pms.disclosureControl.model.DisclosureNoticeValue>
	* @Athor:ouyifan
	* @date:2022/9/14
	*/
	public List<DisclosureNoticeValue> findDisNoticeValuesNew(DisclosureNoticeValue params) throws Exception {
		String sql = "SELECT notice.id t8_disclosure_notice_id,notice.prod_code,notice.prod_base_date data_date,  " +
				"column1.column_key,column1.isdisplay,column1.roleids,column1.is_sysvalue,notVer.id, " +
				"ifnull(source.column_value,'') column_value,source.column_label,source.dict, source.functype,source.data_type,source.money_format,source.computed_expression, " +
				"source.data_length,source.sql_parameter,source.value_sql,source.data_source " +
				"FROM idb_disclosure_notice notice " +
				"LEFT JOIN idb_disclosure_notice_version notVer ON notVer.t8_disclosure_notice_id = notice.id " +
				"LEFT JOIN idb_disclosure_mod_column column1 ON notVer.disclosure_mod_version_id = column1.disclosure_mod_version_id " +
				"LEFT JOIN idb_disclosure_source source ON column1.column_key = source.column_key " +
				"WHERE notice.id=$S{id} AND notVer.id = $S{t8DisclosureVersionId} ";
		return super.findRows(DisclosureNoticeValue.class, sql,
				DataSourceProperty.IDB, params);
	}
	public List<DisclosureNoticeValue> findAllDisNoticeValues(String versionId) throws Exception {
		String sql = "SELECT * FROM idb_disclosure_notice_value WHERE t8_disclosure_version_id = '"+versionId+"' ";
		return super.findRows(DisclosureNoticeValue.class, sql,
				DataSourceProperty.IDB, versionId);
	}

	/**
	* @功能描述:查询净值整体公告表格配置信息
	* @params:[disclosureGridConfig]
	* @return:java.util.List<com.kayak.pms.disclosureControl.model.DisclosureGridConfig>
	* @Athor:ouyifan
	* @date:2022/9/14
	*/
	public List<IdbNoticeGridConfigSource> findDisNoticeGridValuesNew(DisclosureGridConfig disclosureGridConfig) throws Exception {
		String sql = "select * from idb_notice_grid_config_source where disclosure_type = $S{disclosureType} and disclosure_son_type = $S{disclosureSonType} ";
		return super.findRows(IdbNoticeGridConfigSource.class, sql,
				DataSourceProperty.IDB, disclosureGridConfig);
	}
	/**
	* @功能描述:查询资产配置情况
	* @params:[disclosureGridValue]
	* @return:java.util.List<com.kayak.pms.disclosureControl.model.DisclosureGridValue>
	* @Athor:ouyifan
	* @date:2022/9/14
	*/
	public List<DisclosureGridValue> findGridAssetConfigVal(DisclosureGridValue disclosureGridValue) throws Exception {
		String sql = "select base.*,prod.prod_nm from app_grid_asset_config_analysis_base base" +
				" left join app_prd_bas_inf prod on prod.prod_cd = base.prod_cd " +
				" where base.prod_cd in($S{prodCd}) and base.pos_dt >= $S{posStartDt} and base.pos_dt <= $S{posEndDt}";
		return super.findRows(DisclosureGridValue.class, sql,
				DataSourceProperty.PUB, disclosureGridValue);
	}
	/**
	* @功能描述:查询非标资产持仓情况
	* @params:[disclosureGridValue]
	* @return:java.util.List<com.kayak.pms.disclosureControl.model.DisclosureGridValue>
	* @Athor:ouyifan
	* @date:2022/9/14
	*/
	public List<DisclosureGridValue> findGridFBassetHoldingVal(DisclosureGridValue disclosureGridValue) throws Exception {
		String sql = "select base.*,prod.prod_nm from app_grid_fbasset_holding_analysis_base base" +
				" left join app_prd_bas_inf prod on prod.prod_cd = base.prod_cd " +
				" where base.prod_cd in($S{prodCd}) and base.pos_dt >= $S{posStartDt} and base.pos_dt <= $S{posEndDt}";
		return super.findRows(DisclosureGridValue.class, sql,
				DataSourceProperty.PUB, disclosureGridValue);
	}
	/**
	* @功能描述:查询投资关联方情况
	* @params:[disclosureGridValue]
	* @return:java.util.List<com.kayak.pms.disclosureControl.model.DisclosureGridValue>
	* @Athor:ouyifan
	* @date:2022/9/14
	*/
	public List<DisclosureGridValue> findGridAffiliateVal(DisclosureGridValue disclosureGridValue) throws Exception {
		String sql = "select base.*,prod.prod_nm from app_grid_affiliate_analysis_base base" +
				" left join app_prd_bas_inf prod on prod.prod_cd = base.prod_cd " +
				" where base.prod_cd in($S{prodCd}) and base.pos_dt >= $S{posStartDt} and base.pos_dt <= $S{posEndDt}";
		return super.findRows(DisclosureGridValue.class, sql,
				DataSourceProperty.PUB, disclosureGridValue);
	}
	/**
	 *	查找NoticeValue的上版本字段数据
	 */
	public List<DisclosureNoticeValue> findDisNoticeValuesHis(DisclosureNoticeValue params) throws Exception {
		String sql = "SELECT  " +
				"notVal.column_key, " +
				"notVal.column_value, " +
				"notVal.is_sysvalue " +
				"FROM idb_disclosure_notice_value notVal " +
				"WHERE notVal.t8_disclosure_notice_id=$S{id} AND notVal.t8_disclosure_version_id = $S{t8DisclosureVersionId} ";
		return super.findRows(DisclosureNoticeValue.class, sql,
				DataSourceProperty.IDB, params);
	}
	/**
	 *	查找NoticeValue的上一公告版本的版本号
	 */
	public SqlRow  findDisNoticeHisVersion(String noticeId) throws Exception {
		String sql = "SELECT notVer.id notVersionId,notVer.notice_version FROM idb_disclosure_notice_version notVer WHERE notVer.t8_disclosure_notice_id='"+noticeId+"' ORDER BY notVer.id+0 desc LIMIT 1,1";
		return super.findRow( sql,
				DataSourceProperty.IDB, noticeId);
	}
	/**
	 *	查找当前公告版本的版本号
	 */
	public SqlRow  findDisNoticeNowVersion(String noticeId) throws Exception {
		String sql = "SELECT IFNULL(notVer.notice_version,'') notice_version,notVer.id FROM idb_disclosure_notice_version notVer WHERE notVer.t8_disclosure_notice_id = '"+noticeId+"' ORDER BY notVer.id + 0 DESC LIMIT 1";
		return super.findRow( sql,
				DataSourceProperty.IDB, noticeId);
	}

	public List<DisclosureNoticeVersion>  findDisNoticeNowVersionAll(SqlParam<DisclosureWordDate> params) throws Exception {
		String sql = "SELECT version.* FROM idb_disclosure_notice_version version WHERE version.t8_disclosure_notice_id= $S{t8DisclosureNoticeId} AND version.id= $S{t8DisclosureVersionId} ";
		return super.findRows(DisclosureNoticeVersion.class, sql,
				DataSourceProperty.IDB, params.getModel());
	}
	/**
	 *	计算净值增长率(非份额)
	 */
	public Map<String,String> getRate(ScheduleNotice params) throws Exception {
		Map<String,String> map = new HashMap<>();
		String term = this.getTerm(params.getDisclosureSonType());
		// 净值增长率(非份额)
		// 期末累计净值取值逻辑:取报告日期当天数据,如果没有,则取报告期内日期最大一天的数据
		String end_nav_sql =
				"SELECT IFNULL((SELECT TOTAL_NAV  FROM t8_prod_nav WHERE PROD_CODE=$S{prodCode} AND nav_date=$S{reportEndDate}),(SELECT TOTAL_NAV  FROM t8_prod_nav WHERE PROD_CODE=$S{prodCode} AND nav_date=(SELECT MAX(nav_date) FROM t8_prod_nav WHERE nav_date <= $S{reportEndDate} AND  "
						+ "nav_date >=(SELECT REPLACE (LAST_DAY(DATE_SUB(STR_TO_DATE($S{reportEndDate},'%Y%m%d'),INTERVAL "+term+" MONTH)),'-','') FROM DUAL)  AND prod_code=$S{prodCode})))  "
						+ "AS end_nav FROM DUAL";
		SqlRow sqlRowEnd = super.findRow(end_nav_sql,params);
		// 净值增长率(非份额)
		// 期初累计净值取值逻辑:取上季度末累计净值,如果没有,则取成立日当天的净值,则报告期内净值增长率=成立以来净值增长率
		String begin_nav_sql =
				"SELECT  "
						+ "IFNULL((IFNULL((SELECT TOTAL_NAV FROM t8_prod_nav WHERE PROD_CODE=$S{prodCode} AND nav_date=REPLACE ((SELECT NAV_DATE FROM T8_PROD_NAV WHERE PROD_CODE=$S{prodCode} AND  "
						+ "nav_date=(SELECT REPLACE (LAST_DAY(DATE_SUB(STR_TO_DATE($S{reportEndDate},'%Y%m%d'),INTERVAL "
						+ term
						+ " MONTH)),'-','') FROM DUAL)),'-','')), "
						+ "(SELECT TOTAL_NAV FROM t8_prod_nav WHERE PROD_CODE=$S{prodCode} AND nav_date= "
						+ "REPLACE ((SELECT NAV_DATE FROM T8_PROD_NAV WHERE PROD_CODE=$S{prodCode} AND nav_date=(SELECT establish_date FROM t8_prod_calendar WHERE prod_code=$S{prodCode})),'-','')))), "
						+ "(SELECT TOTAL_NAV FROM t8_prod_nav WHERE PROD_CODE=$S{prodCode} AND nav_date= "
						+ "(SELECT MIN(NAV_DATE) FROM t8_prod_nav WHERE NAV_DATE>=(SELECT REPLACE (LAST_DAY(DATE_SUB(STR_TO_DATE($S{reportEndDate},'%Y%m%d'),INTERVAL "+term+" MONTH)),'-','') FROM DUAL) "
						+ "AND NAV_DATE<=$S{reportEndDate} AND PROD_CODE=$S{prodCode}) "
						+ ")) AS begin_nav FROM DUAL";
		SqlRow sqlRowBegin = super.findRow(begin_nav_sql,params);
		// 查询期初单位净值
		String nav_sql =
				"SELECT IFNULL((IFNULL((SELECT NAV FROM t8_prod_nav WHERE PROD_CODE=$S{prodCode} AND nav_date=REPLACE ((SELECT NAV_DATE FROM T8_PROD_NAV WHERE PROD_CODE=$S{prodCode} AND  "
						+ "nav_date=(SELECT REPLACE (LAST_DAY(DATE_SUB(STR_TO_DATE($S{reportEndDate},'%Y%m%d'),INTERVAL "
						+ term
						+ " MONTH)),'-','') FROM DUAL)),'-','')), "
						+ "(SELECT NAV FROM t8_prod_nav WHERE PROD_CODE=$S{prodCode} AND nav_date=REPLACE ((SELECT NAV_DATE FROM T8_PROD_NAV WHERE PROD_CODE=$S{prodCode} AND  "
						+ "nav_date=(SELECT establish_date FROM t8_prod_calendar WHERE prod_code=$S{prodCode})),'-','')))), "
						+ "(SELECT NAV FROM t8_prod_nav WHERE PROD_CODE=$S{prodCode} AND nav_date= "
						+ "(SELECT MIN(NAV_DATE) FROM t8_prod_nav WHERE NAV_DATE>=(SELECT REPLACE (LAST_DAY(DATE_SUB(STR_TO_DATE($S{reportEndDate},'%Y%m%d'),INTERVAL "+term+" MONTH)),'-','') FROM DUAL) "
						+ "AND NAV_DATE<=$S{reportEndDate} AND PROD_CODE=$S{prodCode}) "
						+ ")) AS nav FROM DUAL";
		SqlRow sqlRowNav = super.findRow(nav_sql,params);
		//计算成立以来净值增长率
		//成立以来净值增长率=((期末累计净值-1)/1)*100
		double subsist_rate = 0.00;
		if(!Tools.isEmpty(sqlRowEnd.getString("end_nav"))&&!("0".equals(sqlRowEnd.getString("end_nav")))){
			subsist_rate = ((Double.parseDouble(sqlRowEnd.getString("end_nav"))-1)/1)*100;
		}
		BigDecimal subsist = new BigDecimal(subsist_rate);
		subsist = subsist.setScale(2, BigDecimal.ROUND_HALF_UP);
		map.put("subsist_rate",subsist.toString());
		this.updateDisclosureNoticeRate(subsist.toString(),params.getId(),"subsist_netval_rate");
		// 计算报告期内净值增长率
		// 报告期内净值增长率=(期末累计净值-期初累计净值)/期初累计净值
		double report_rate = 0.00;
		if (!Tools.isEmpty(sqlRowBegin.getString("begin_nav"))&&!("0".equals(sqlRowBegin.getString("begin_nav")))) {
			report_rate = ((Double.parseDouble(sqlRowEnd.getString("end_nav"))-Double.parseDouble(sqlRowBegin.getString("begin_nav")))/(Double.parseDouble(sqlRowNav.getString("nav"))))*100;
		}else{
			report_rate = subsist_rate;
		}
		BigDecimal report = new BigDecimal(report_rate);
		report = report.setScale(2, BigDecimal.ROUND_HALF_UP);
		//map.put("report_rate",report.toString());
		this.updateDisclosureNoticeRate(report.toString(),params.getId(),"report_netval_rate");
		return map;
	}

	public SqlRow queryDate (String baseDate)  throws Exception {

		return super.findRow("	SELECT REPLACE (LAST_DAY(DATE_SUB(STR_TO_DATE('"+baseDate+"','%Y%m%d'),INTERVAL 3 MONTH)),'-','') prod_base_date FROM DUAL",
				DataSourceProperty.IDB,null);
	}

	public String getTerm(String disclosureSonType) throws Exception {
		String term = "";
		if (StringUtils.isNotEmpty(disclosureSonType)) {
			if (disclosureSonType.equals("1")) {//季报
				term = "3";
			} else if (disclosureSonType.equals("2")) {//半年报
				term = "6";
			} else if (disclosureSonType.equals("3")) {//年报
				term = "12";
			} else if (disclosureSonType.equals("4")) {//月报
				term = "1";
			}
		}
		return term;

	}

}
