package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.pms.disclosureControl.model.ScheduleProdRule;
import com.kayak.pms.global.constants.DisclosureSonType;
import com.kayak.pms.global.constants.DisclosureStatus;
import com.kayak.pms.global.constants.TaskStart;
import com.kayak.pms.global.constants.XpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Repository
public class ScheduleProdRuleDao extends ComnDao {


	/**
	 * 功能：查询产品信披规则  仅查询状态产品状态为6、7 子状态为14、15、16的
	 * 作者：rennannan
	 * 日期：20210607
	 *
	 * @param scheduleProdRule
	 * @return
	 * @throws Exception
	 */
	public List<ScheduleProdRule> findProdRules(ScheduleProdRule scheduleProdRule) throws Exception {
		String sql = "SELECT  prodRule.id,prod.prod_cd prod_code,prodRule.t8_disclosure_rule_id, " +
				" prodRule.remark,prodRule.source, prod.prod_nm prod_name, " +
				"  prodRule.disclosure_type,prodRule.disclosure_son_type,prodRule.disclosure_mod_id,prodRule.disclosure_mod_version_id," +
				"  prodRule.base_date,prodRule.exp_create_rule,prodRule.exp_create_days, " +
				"  prodRule.notice_title,prod.PROD_BRND," +
				"  prod.FOUND_DT ESTABLISH_DATE,prod.MTU_DT end_date ,prod.SSCR_BGN_DT START_RAISE ,prod.MTU_DT REAL_END_DATE \n" +
				" FROM (select tdpr.id,tdpr.prod_code,tdpr.t8_disclosure_rule_id,tdpr.remark,tdpr.source,tdpr.disclosure_type," +
				" tdpr.disclosure_son_type,tdpr.base_date,tdpr.exp_create_rule,tdpr.exp_create_days," +
				" tdpr.notice_title,tdpr.disclosure_mod_id,tdpr.disclosure_mod_version_id" +
				" from idb_disclosure_prod_rule tdpr left join idb_disclosure_rule tdr  " +
				" on tdr.id = tdpr.t8_disclosure_rule_id " +
				" where 1=1  ";
		if (StringUtils.isNotEmpty(scheduleProdRule.getDisclosureType())) {//信披类型
			sql += " and tdpr.disclosure_type=$S{disclosureType} ";
		}
		if (StringUtils.isNotEmpty(scheduleProdRule.getDisclosureSonType())) {
			sql += " and tdpr.disclosure_son_type=$S{disclosureSonType} ";
		}
		if (StringUtils.isNotEmpty(scheduleProdRule.getId())) {
			sql += " and tdpr.id=$S{id} ";
		}
		if (StringUtils.isNotEmpty(scheduleProdRule.getStartRule())&&TaskStart.manual.getItemKey().equals(scheduleProdRule.getStartRule())) {
			sql += " and tdpr.start_rule in ('1','2')";
		}else if(StringUtils.isNotEmpty(scheduleProdRule.getStartRule())&&TaskStart.auto.getItemKey().equals(scheduleProdRule.getStartRule())){
			sql += " and tdpr.start_rule in ($S{startRule})";
		}
		sql += ")prodRule" +
				" left join APP_PRD_BAS_INF prod " +
				"   on prodRule.prod_code = prod.PROD_CD and prod.PROD_STS <> '3' /*过滤发行失败的产品*/ ";
		return super.findRows(ScheduleProdRule.class, sql, DataSourceProperty.IDB, scheduleProdRule);
	}
	public List<ScheduleProdRule> findProdRulesZT(ScheduleProdRule scheduleProdRule) throws Exception {
		String sql = "select tdr.id t8_disclosure_rule_id,tdr.remark,tdr.disclosure_type,tdr.net_value_date," +
				" tdr.disclosure_son_type,tdr.base_date,tdr.exp_create_rule,tdr.exp_create_days," +
				" tdr.notice_title,tdr.disclosure_mod_id,tdr.disclosure_mod_version_id,tdr.PROD_CLC_MTH,tdr.PROD_SER_CD,tdr.PROD_OBJ,tdr.prod_inv_typ,tdr.INV_PRD_LEN,tdr.INV_PRD_DIME,tdr.PROD_FORM" +
				" from  idb_disclosure_rule tdr  " +
				" where 1=1 AND status ='"+XpStatus.start.getItemKey()+"'\n" ;
		if (StringUtils.isNotEmpty(scheduleProdRule.getDisclosureType())) {//信披类型
			sql += " and tdr.disclosure_type=$S{disclosureType} ";
		}
		if (StringUtils.isNotEmpty(scheduleProdRule.getDisclosureSonType())) {
			sql += " and tdr.disclosure_son_type=$S{disclosureSonType} ";
		}
		if (StringUtils.isNotEmpty(scheduleProdRule.getId())) {
			sql += " and tdr.id=$S{id} ";
		}
		if (StringUtils.isNotEmpty(scheduleProdRule.getStartRule())&&TaskStart.manual.getItemKey().equals(scheduleProdRule.getStartRule())) {
			sql += " and tdr.start_rule in ('1','2')";
		}else if(StringUtils.isNotEmpty(scheduleProdRule.getStartRule())&&TaskStart.auto.getItemKey().equals(scheduleProdRule.getStartRule())){
			sql += " and tdr.start_rule in ($S{startRule})";
		}
		return super.findRows(ScheduleProdRule.class, sql, DataSourceProperty.IDB, scheduleProdRule);
	}

	/**
	 * 功能：根据信披类型查询产品信披规则
	 * 作者：rennannan
	 * 日期：20211122
	 *
	 * @return
	 */
	public List<ScheduleProdRule> findRuleExistsByType(ScheduleProdRule scheduleProdRule) throws Exception {
		StringBuilder stringBuilder = new StringBuilder(" select id from idb_disclosure_prod_rule where disclosure_type=$S{disclosureType} ");
		if (StringUtils.isNotEmpty(scheduleProdRule.getDisclosureSonType())) {
			stringBuilder.append(" and disclosure_son_type=$S{disclosureSonType}");
		}
		return super.findRows(ScheduleProdRule.class, stringBuilder.toString(),
				DataSourceProperty.IDB, scheduleProdRule);
	}
	public List<ScheduleProdRule> findRuleExistsByNet(ScheduleProdRule scheduleProdRule) throws Exception {
		StringBuilder stringBuilder = new StringBuilder(" select id from idb_disclosure_prod_rule where disclosure_type=$S{disclosureType}\n");
		if (StringUtils.isNotEmpty(scheduleProdRule.getDisclosureSonType())) {
			stringBuilder.append(" and disclosure_son_type=$S{disclosureSonType}\n");
		}
		stringBuilder.append(" union\n select id from idb_disclosure_rule where disclosure_type=$S{disclosureType} and status in ('"+XpStatus.start.getItemKey()+"')\n");
		if (StringUtils.isNotEmpty(scheduleProdRule.getDisclosureSonType())) {
			stringBuilder.append(" and disclosure_son_type=$S{disclosureSonType}\n");
		}
		return super.findRows(ScheduleProdRule.class, stringBuilder.toString(),
				DataSourceProperty.IDB, scheduleProdRule);
	}
	public List<SqlRow> findExistsNetDate(ScheduleProdRule scheduleProdRule) throws Exception {
		StringBuilder stringBuilder = new StringBuilder(" select prod_base_date from idb_disclosure_prod_task where disclosure_type=$S{disclosureType}\n");
		if (StringUtils.isNotEmpty(scheduleProdRule.getDisclosureSonType())) {
			stringBuilder.append(" and disclosure_son_type=$S{disclosureSonType}\n");
		}
		if (Strings.isNotBlank(scheduleProdRule.getProdForm())) {//产品形态
			stringBuilder.append(" AND PROD_FORM =$S{prodForm}\n");
		}
		if (Strings.isNotBlank(scheduleProdRule.getInvPrdDime())) {//周期为度
			stringBuilder.append( " AND INV_PRD_DIME =$S{invPrdDime}\n");
		}
		if (Strings.isNotBlank(scheduleProdRule.getInvPrdLen())) {
			stringBuilder.append(" AND INV_PRD_LEN =$S{invPrdLen}\n");
		}
		if (Strings.isNotBlank(scheduleProdRule.getProdInvTyp())) {
			stringBuilder.append(" AND prod_inv_typ =$S{prodInvTyp}\n");
		}
		if (Strings.isNotBlank(scheduleProdRule.getProdObj())) {
			stringBuilder.append(" AND PROD_OBJ =$S{prodObj}\n");
		}
		if (Strings.isNotBlank(scheduleProdRule.getProdClcMth())) {
			stringBuilder.append(" AND PROD_CLC_MTH =$S{prodClcMth}\n");
		}
		if (Strings.isNotBlank(scheduleProdRule.getProdSerCd())) {
			stringBuilder.append(" AND PROD_SER_CD =$S{prodSerCd}\n");
		}
		return super.findRows( stringBuilder.toString(),
				DataSourceProperty.IDB, scheduleProdRule);
	}
	public List<ScheduleProdRule> findRuleExists(ScheduleProdRule scheduleProdRule) throws Exception {
		StringBuilder stringBuilder = new StringBuilder(" select id from idb_disclosure_rule where disclosure_type=$S{disclosureType} AND status ='"+XpStatus.start.getItemKey()+"'\n");
		if (StringUtils.isNotEmpty(scheduleProdRule.getDisclosureSonType())) {
			stringBuilder.append(" and disclosure_son_type=$S{disclosureSonType}");
		}
		return super.findRows(ScheduleProdRule.class, stringBuilder.toString(),
				DataSourceProperty.IDB, scheduleProdRule);
	}
	/**
	 * 功能：根据信披类型关联这天的分红接口数据查询产品信披规则
	 * 作者：rennannan
	 * 日期：20211122
	 *
	 * @return
	 */
	public List<ScheduleProdRule> findRuleExistsByBonusIssueDate(ScheduleProdRule scheduleProdRule) throws Exception {
		return super.findRows(ScheduleProdRule.class, " select t.id from idb_disclosure_prod_rule t   where disclosure_type=$S{disclosureType}",
				DataSourceProperty.IDB, scheduleProdRule);
	}
	/**
	 * 功能：根据信披类型查询产品信披规则
	 * 作者：rennannan
	 * 日期：20211122
	 *
	 * @return
	 */
	public List<ScheduleProdRule> findRuleExistsByTypeBefSale(ScheduleProdRule scheduleProdRule) throws Exception {
		StringBuilder stringBuilder = new StringBuilder(" select distinct disclosure_son_type from idb_disclosure_prod_rule where disclosure_type=$S{disclosureType} ");
		if (StringUtils.isNotEmpty(scheduleProdRule.getDisclosureSonType())) {
			stringBuilder.append(" and disclosure_son_type=$S{disclosureSonType}");
		}
		return super.findRows(ScheduleProdRule.class, stringBuilder.toString(),
				DataSourceProperty.IDB, scheduleProdRule);
	}

	/**
	 * 根据信披类型为发行公告、发起方式为时间规则发起、成立日为传入日期的产品信披规则
	 * 产品状态不为 5上会未通过、9申报失败、13发行失败、17已到期 、18已作废
	 * 募集方式为公募的
	 *
	 * @return
	 */
	public List<ScheduleProdRule> findProdByTypeAndRule(Map<String, Object> params ) throws Exception {
		String sql = "SELECT t1.id,t1.prod_code,t1.t8_disclosure_rule_id,\n" +
				"t1.rule_name,t1.disclosure_type,t1.disclosure_son_type,t1.notice_title,\n" +
				"t1.start_rule,t1.disclosure_mod_id,t1.disclosure_mod_version_id,\n" +
				"t1.base_date,t1.exp_create_rule,t1.exp_create_days,\n" +
				"t2.PROD_CD prod_code,t2.PROD_NM prod_name,t2.FOUND_DT establish_date\n" +
				"   FROM idb_disclosure_prod_rule AS t1\n" +
				" right JOIN APP_PRD_BAS_INF AS t2 ON t1.PROD_CODE = t2.PROD_CD and t2.PROD_STS <> '3' /*过滤发行失败的产品*/ \n" +
				" WHERE 1=1 AND t1.disclosure_type='2'\n";
		if (StringUtils.isNotEmpty(params.get("establishDate").toString())) {//发行成立日期
			sql += " AND t2.FOUND_DT=$S{establishDate} ";
		}
		if (StringUtils.isNotEmpty(params.get("id").toString())) {
			sql += " AND t1.id=$S{id} ";
		}
		if (StringUtils.isNotEmpty(params.get("taskStart").toString())&& TaskStart.manual.getItemKey().equals(params.get("taskStart").toString())) {
			sql += " and t1.start_rule in ('1','2')";
		}else if(StringUtils.isNotEmpty(params.get("taskStart").toString())&& TaskStart.auto.getItemKey().equals(params.get("taskStart").toString())){
			sql += " AND t1.start_rule = $S{taskStart}";
		}
		return super.findRows(ScheduleProdRule.class, sql,
				DataSourceProperty.IDB, params);
	}
	public List<ScheduleProdRule> findProdByTypeAndRuleSale(Map<String, Object> params ) throws Exception {
		String sql = "SELECT t1.id,t1.prod_code,t1.t8_disclosure_rule_id,\n" +
				"t1.rule_name,t1.disclosure_type,t1.disclosure_son_type,t1.notice_title,\n" +
				"t1.start_rule,t1.disclosure_mod_id,t1.disclosure_mod_version_id,\n" +
				"t1.base_date,t1.exp_create_rule,t1.exp_create_days,\n" +
				"t2.PROD_CD prod_code,t2.PROD_NM prod_name,t2.ISU_DT apply_start_date,t2.ISU_DT START_RAISE\n" +
				"   FROM idb_disclosure_prod_rule AS t1\n" +
				" right JOIN  APP_PRD_BAS_INF AS t2 ON t1.PROD_CODE = t2.PROD_CD and t2.PROD_STS <> '3' /*过滤发行失败的产品*/ \n" +
				" WHERE 1=1 AND t1.disclosure_type='12'\n";
		if (StringUtils.isNotEmpty(params.get("applyStartDate").toString())) {//信披类型
			sql += " AND t2.ISU_DT=$S{applyStartDate}";
		}
		if (StringUtils.isNotEmpty(params.get("id").toString())) {
			sql += " AND t1.id=$S{id}";
		}
		if (StringUtils.isNotEmpty(params.get("taskStart").toString())&&TaskStart.manual.getItemKey().equals(params.get("taskStart").toString())) {
			sql += " and t1.start_rule in ('1','2')";
		}else if(StringUtils.isNotEmpty(params.get("taskStart").toString())&&TaskStart.auto.getItemKey().equals(params.get("taskStart").toString())){
			sql += " AND t1.start_rule = $S{taskStart}";
		}
		return super.findRows(ScheduleProdRule.class, sql,
				DataSourceProperty.IDB, params);
	}
	public List<ScheduleProdRule> findProdByTypeAndRuleBeforeSale(Map<String, Object> params ) throws Exception {
		String sql = "SELECT t1.id,t1.prod_code,t1.t8_disclosure_rule_id,\n" +
				"t1.rule_name,t1.disclosure_type,t1.disclosure_son_type,t1.notice_title,\n" +
				"t1.start_rule,t1.disclosure_mod_id,t1.disclosure_mod_version_id,\n" +
				"t1.base_date,t1.exp_create_rule,t1.exp_create_days,\n" +
				"t2.PROD_CD prod_code,t2.PROD_NM prod_name,t2.SSCR_BGN_DT apply_start_date,t2.SSCR_BGN_DT START_RAISE\n" +
				"   FROM idb_disclosure_prod_rule AS t1\n" +
				" right JOIN  APP_PRD_BAS_INF AS t2 ON t1.PROD_CODE = t2.PROD_CD and t2.PROD_STS <> '3' /*过滤发行失败的产品*/ \n" +
				" WHERE 1=1 AND t1.disclosure_type='1'\n";
		if (StringUtils.isNotEmpty(params.get("applyStartDate").toString())) {
			sql += " AND t2.SSCR_BGN_DT=$S{applyStartDate}";
		}
		if (StringUtils.isNotEmpty(params.get("disclosureSonType").toString())) {//信披子类型
			sql += " AND t1.disclosure_son_type=$S{disclosureSonType}";
		}
		if (StringUtils.isNotEmpty(params.get("id").toString())) {//信披子类型
			sql += " AND t1.id=$S{id}";
		}
		if (StringUtils.isNotEmpty(params.get("taskStart").toString())&&TaskStart.manual.getItemKey().equals(params.get("taskStart").toString())) {
			sql += " and t1.start_rule in ('1','2')";
		}else if(StringUtils.isNotEmpty(params.get("taskStart").toString())&&TaskStart.auto.getItemKey().equals(params.get("taskStart").toString())){
			sql += " AND t1.start_rule = $S{taskStart}";
		}
		return super.findRows(ScheduleProdRule.class, sql,
				DataSourceProperty.IDB, params);
	}
	public List<ScheduleProdRule> findProdByTypeAndRuleNet(Map<String, Object> params ) throws Exception {
		String sql = "SELECT t1.id,t1.prod_code,t1.t8_disclosure_rule_id,t1.net_value_date,\n" +
				"t1.rule_name,t1.disclosure_type,t1.disclosure_son_type,t1.notice_title,\n" +
				"t1.start_rule,t1.disclosure_mod_id,t1.disclosure_mod_version_id,\n" +
				"t1.base_date,t1.exp_create_rule,t1.exp_create_days,\n" +
				"t2.PROD_CD prod_code,t2.PROD_NM prod_name,t2.MTU_DT END_DATE" +
				"   FROM idb_disclosure_prod_rule AS t1\n" +
				" right JOIN  APP_PRD_BAS_INF AS t2 ON t1.PROD_CODE = t2.PROD_CD and t2.PROD_STS <> '3' /*过滤发行失败的产品*/ \n" +
				" WHERE 1=1 AND t1.disclosure_type='9' \n" +
				" AND t1.disclosure_son_type NOT IN ('"+ DisclosureSonType.netValueEntity.getItemKey() +"')\n";
		if (StringUtils.isNotEmpty(params.get("taskStart").toString())&&TaskStart.manual.getItemKey().equals(params.get("taskStart").toString())) {
			sql += " and t1.start_rule in ('1','2')";
		}else if(StringUtils.isNotEmpty(params.get("taskStart").toString())&&TaskStart.auto.getItemKey().equals(params.get("taskStart").toString())){
			sql += " AND t1.start_rule = $S{taskStart}";
		}
		if (StringUtils.isNotEmpty(params.get("disclosureSonType").toString())) {//信披子类型
			sql += " AND t1.disclosure_son_type=$S{disclosureSonType}";
		}
		if (StringUtils.isNotEmpty(params.get("id").toString())) {
			sql += " and t1.id in ($S{id})";
		}
		return super.findRows(ScheduleProdRule.class, sql,
				DataSourceProperty.IDB, params);
	}

	/**
	 * 判断是不是发布日（根据净值发布日期=基准日期）
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> findNetValueForIsuDate(Map<String, Object> params ) throws Exception {
		String sql = "select prod_cd prod_code,nav_dt from app_prd_nav_inf where 1=1 and ISU_DT = $S{netDate}\n" +
				" and (IS_BUT_CNF_DT = '0' /*净值日非申购确认日*/\n " +
				" and IS_RDM_CNF_DT = '0' /*净值日非赎回确认日*/\n )";
		if (StringUtils.isNotEmpty(params.get("prodCode").toString())) {
			sql += " and prod_cd = $S{prodCode}\n";
		}
		return super.findRows( sql,DataSourceProperty.PUB, params);
	}

	/**
	 * 判断是否到期日（根据净值日期=基准日期）
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> findNetValueForEndDate(Map<String, Object> params ) throws Exception {
		String sql = "select prod_cd prod_code,nav_dt from app_prd_nav_inf where 1=1 and NAV_DT = $S{netDate}\n" +
				" and (IS_BUT_CNF_DT = '0' /*净值日非申购确认日*/\n " +
				" and IS_RDM_CNF_DT = '0' /*净值日非赎回确认日*/\n )";
		if (StringUtils.isNotEmpty(params.get("prodCode").toString())) {
			sql += " and prod_cd = $S{prodCode}\n";
		}
		return super.findRows( sql,DataSourceProperty.PUB, params);
	}

	/**
	 * 判断是否申赎确认日（根据净值日期=基准日期）
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> findNetValueForCnfDate(Map<String, Object> params ) throws Exception {
		String sql = "select prod_cd prod_code,nav_dt from app_prd_nav_inf where 1=1 and NAV_DT = $S{netDate}\n" +
				" and (IS_BUT_CNF_DT = '1' /*净值日为申购确认日*/\n " +
				" or IS_RDM_CNF_DT = '1' /*净值日为赎回确认日*/\n )";
		if (StringUtils.isNotEmpty(params.get("prodCode").toString())) {
			sql += " and prod_cd = $S{prodCode}\n";
		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}
	public Integer findNetValueZT(Map<String, Object> params ) throws Exception {
		//TODO sql调整
		String sql = "SELECT count(*) `count` FROM app_prd_nav_inf net\n" +
				"LEFT JOIN app_prd_bas_inf prod ON prod.prod_cd = net.PROD_CD\n" +
				"WHERE 1=1 AND net.ISU_DT = $S{netDate} AND prod.MTU_DT<>$S{netDate} AND prod.FOUND_DT<>$S{netDate}\n";
		if (Strings.isNotBlank((String) params.get("prodForm"))) {//产品形态
			sql += " AND prod.PROD_FORM in ($U{prodForm})";
		}
        if (Strings.isNotBlank((String)params.get("invPrdDime"))) {//周期为度
            sql += " AND prod.INV_PRD_DIME in ($U{invPrdDime})";
        }
        if (Strings.isNotBlank((String)params.get("invPrdLen"))) {
            sql += " AND prod.INV_PRD_LEN in ($U{invPrdLen})";
        }
		if (Strings.isNotBlank((String)params.get("prodInvTyp"))) {
			sql += " AND prod.prod_inv_typ in ($U{prodInvTyp})";
		}
		if (Strings.isNotBlank((String)params.get("prodObj"))) {
			sql += " AND prod.PROD_OBJ in ($U{prodObj})";
		}
		if (Strings.isNotBlank((String)params.get("prodClcMth"))) {
			sql += " AND prod.PROD_CLC_MTH in ($U{prodClcMth})";
		}
		if (Strings.isNotBlank((String)params.get("prodSerCd"))) {
			sql += " AND prod.PROD_SER_CD in ($U{prodSerCd})";
		}
		return super.findRow( sql,
				DataSourceProperty.IDB, params).getInteger("count");
	}
	public Integer findNetValueZTEnd(Map<String, Object> params ) throws Exception {
		//TODO sql调整
		String sql = "SELECT count(*) `count` FROM app_prd_nav_inf net\n" +
				"LEFT JOIN app_prd_bas_inf prod ON prod.prod_cd = net.PROD_CD\n" +
				"WHERE 1=1 AND prod.MTU_DT=$S{netDate} \n";
		if (Strings.isNotBlank((String)params.get("prodForm"))) {//产品形态
			sql += " AND prod.PROD_FORM in ($U{prodForm})";
		}
        if (Strings.isNotBlank((String)params.get("invPrdDime"))) {//周期为度
            sql += " AND prod.INV_PRD_DIME in ($U{invPrdDime})";
        }
        if (Strings.isNotBlank((String)params.get("invPrdLen"))) {
            sql += " AND prod.INV_PRD_LEN in ($U{invPrdLen})";
        }
		if (Strings.isNotBlank((String)params.get("prodInvTyp"))) {
			sql += " AND prod.prod_inv_typ in ($U{prodInvTyp})";
		}
		if (Strings.isNotBlank((String)params.get("prodObj"))) {
			sql += " AND prod.PROD_OBJ in ($U{prodObj})";
		}
		if (Strings.isNotBlank((String)params.get("prodClcMth"))) {
			sql += " AND prod.PROD_CLC_MTH in ($U{prodClcMth})";
		}
		if (Strings.isNotBlank((String)params.get("prodSerCd"))) {
			sql += " AND prod.PROD_SER_CD in ($U{prodSerCd})";
		}

		return super.findRow( sql,
				DataSourceProperty.IDB, params).getInteger("count");
	}
	public Integer findNetValueZTqr(Map<String, Object> params ) throws Exception {
		//TODO sql调整
		String sql = "SELECT count(*) `count` FROM app_prd_nav_inf net\n" +
				"LEFT JOIN app_prd_bas_inf prod ON prod.prod_cd = net.PROD_CD\n" +
				"WHERE 1=1  AND prod.FOUND_DT=$S{netDate}\n";
		if (Strings.isNotBlank((String)params.get("prodForm"))) {//产品形态
			sql += " AND prod.PROD_FORM in ($U{prodForm})";
		}
        if (Strings.isNotBlank((String)params.get("invPrdDime"))) {//周期为度
            sql += " AND prod.INV_PRD_DIME in ($U{invPrdDime})";
        }
        if (Strings.isNotBlank((String)params.get("invPrdLen"))) {
            sql += " AND prod.INV_PRD_LEN in ($U{invPrdLen})";
        }
		if (Strings.isNotBlank((String)params.get("prodInvTyp"))) {
			sql += " AND prod.prod_inv_typ in ($U{prodInvTyp})";
		}
		if (Strings.isNotBlank((String)params.get("prodObj"))) {
			sql += " AND prod.PROD_OBJ in ($U{prodObj})";
		}
		if (Strings.isNotBlank((String)params.get("prodClcMth"))) {
			sql += " AND prod.PROD_CLC_MTH in ($U{prodClcMth})";
		}
		if (Strings.isNotBlank((String)params.get("prodSerCd"))) {
			sql += " AND prod.PROD_SER_CD in ($U{prodSerCd})";
		}

		return super.findRow( sql,
				DataSourceProperty.IDB, params).getInteger("count");
	}
	public String findProdByNetDay(String netDay) throws Exception {
		String sql = "SELECT IFNULL(GROUP_CONCAT(PROD_CD),'') prodCode FROM app_prd_nav_inf WHERE ISU_DT =$S{netDay}";
		return super.findRow( sql,
				DataSourceProperty.IDB, netDay).getString("prodCodes");
	}

	/**
	 * 功能：根据信披类型为到期公告、发起方式为时间规则发起,实际终止日期为传入日期的产品信披规则
	 * 产品状态不为 5上会未通过、9申报失败、13发行失败 、18已作废
	 * 募集方式为公募的
	 * 到期公告标题字典 ： maturity
	 * 作者：rennannan
	 * 日期：20211101
	 *
	 * @return
	 */
	public List<ScheduleProdRule> findEndProdRule(Map<String, Object> params) throws Exception {
		String sql = "SELECT t1.id,t1.prod_code,t1.t8_disclosure_rule_id," +
				"t1.rule_name,t1.disclosure_type,t1.disclosure_son_type,t1.notice_title," +
				"t1.start_rule,t1.disclosure_mod_id,t1.disclosure_mod_version_id," +
				"t1.base_date,t1.exp_create_rule,t1.exp_create_days," +
				"t2.PROD_CD prod_code,t2.PROD_NM prod_name,t2.MTU_DT END_DATE" +
				"   FROM idb_disclosure_prod_rule AS t1" +
				" right JOIN APP_PRD_BAS_INF AS t2 ON t1.prod_code = t2.PROD_CD and t2.PROD_STS <> '3' /*过滤发行失败的产品*/ " +
				" WHERE 1=1 AND t1.disclosure_type='3' ";
		if (StringUtils.isNotEmpty(params.get("realEndDate").toString())) {//信披类型
			sql += " and t2.MTU_DT=$S{realEndDate}";
		}
		if (StringUtils.isNotEmpty(params.get("id").toString())) {//信披类型
			sql += " and t1.id=$S{id}";
		}
		if (StringUtils.isNotEmpty(params.get("taskStart").toString())&&TaskStart.manual.getItemKey().equals(params.get("taskStart").toString())) {
			sql += " and t1.start_rule in ('1','2')";
		}else if(StringUtils.isNotEmpty(params.get("taskStart").toString())&&TaskStart.manual.getItemKey().equals(params.get("taskStart").toString())){
			sql += " AND t1.start_rule = $S{taskStart}";
		}
		return super.findRows(ScheduleProdRule.class, sql,
				DataSourceProperty.IDB, params);
	}


	/**
	 * 功能：根据信披类型和发起方式等查询产品信息规则
	 * 作者：rennannan
	 * 日期：20210621
	 *
	 * @param scheduleProdRule
	 * @return
	 * @throws Exception
	 */
	public List<ScheduleProdRule> findProdRulesByType(ScheduleProdRule scheduleProdRule) throws Exception {
		String sql = " select prodRule.id,prodRule.base_date," +
				" prodRule.disclosure_type,prodRule.disclosure_son_type," +
				" prodRule.net_value_date,prodRule.net_value_date_rule," +
				" prodRule.rule_name,prodRule.notice_title,prodRule.start_rule," +
				" prod.PROD_CD prod_code,expiration.prod_real_close_date,cal.establish_date  " +
				" from ( select id,base_date, " +
				" disclosure_type,disclosure_son_type, " +
				" net_value_date,net_value_date_rule, " +
				" rule_name,notice_title,start_rule" +
				" from idb_disclosure_prod_rule " +
				"  where 1=1 ";
		if (StringUtils.isNotEmpty(scheduleProdRule.getDisclosureType())) {//信披类型
			sql += " and disclosure_type=$S{disclosureType} ";
		}
		if (StringUtils.isNotEmpty(scheduleProdRule.getStartRule())) {
			sql += " and start_rule=$S{startRule} ";
		}
		sql += " ) prodRule" +
				" left join APP_PRD_BAS_INF prod " +
				"        on prodRule.PROD_CODE = prod.PROD_CD ";
		return super.findRows(ScheduleProdRule.class, sql, DataSourceProperty.PUB, scheduleProdRule);
	}

	/**
	 * 功能：根据产品信披规则id查询规则详情
	 * 作者：rennannan
	 * 修改日期：20210607
	 *
	 * @param ruleId
	 * @return
	 * @throws Exception
	 */
	public ScheduleProdRule findRuleById(String ruleId) throws Exception {

		return super.findRow(ScheduleProdRule.class,
				"SELECT rule.disclosure_mod_version_id,rule.channel_ids,rule.if_condition," +
						" CASE " +
						" IFNULL(dmct.count,'0') " +
						" WHEN '0' " +
						" THEN '0' " +
						" ELSE '1' " +
						" END if_clearing," +
						" rule.exp_supplement_rule,rule.exp_supplement_days,rule.exp_approval_rule,rule.exp_approval_days,rule.exp_publish_rule,rule.exp_publish_days,rule.notice_roleid FROM idb_disclosure_prod_rule rule " +
						" left join idb_disclosure_mod_version dmv on rule.disclosure_mod_version_id = dmv.id " +
						" LEFT JOIN " +
						" (SELECT DISTINCT disclosure_mod_version_id,COUNT(*) `count` FROM idb_disclosure_mod_column  " +
						" WHERE is_sysvalue = '2'  GROUP BY disclosure_mod_version_id)dmct " +
						" ON dmv.id = dmct.disclosure_mod_version_id " +
						" where id=$S{ruleId}",
				DataSourceProperty.IDB, ruleId);

	}
	public List<ScheduleProdRule> findProdByBonusIssueDateAndRule(Map<String, Object> params ) throws Exception {
		String sql = "SELECT t1.id,t1.prod_code,t1.t8_disclosure_rule_id,\n" +
				"t1.rule_name,t1.disclosure_type,t1.disclosure_son_type,t1.notice_title,\n" +
				"t1.start_rule,t1.disclosure_mod_id,t1.disclosure_mod_version_id,\n" +
				"t1.base_date,t1.exp_create_rule,t1.exp_create_days,\n" +
				"t2.PROD_CD prod_code,t2.PROD_NM prod_name,t2.FOUND_DT establish_date\n" +
				"   FROM idb_disclosure_prod_rule AS t1\n" +
				" right JOIN APP_PRD_BAS_INF AS t2 ON t1.PROD_CODE = t2.PROD_CD and t2.PROD_STS <> '3' /*过滤发行失败的产品*/ \n" +
				" left join app_prd_bonus_confirm_inf t3 on t2.PROD_CD = t3.PROD_CD "+
				" WHERE 1=1 AND t1.disclosure_type='10'\n";
		if (StringUtils.isNotEmpty(params.get("bonusIssueDate").toString())) {//红利发放日
			sql += " AND t3.BONUS_ISSUE_DATE=$S{bonusIssueDate} ";
		}
		if (StringUtils.isNotEmpty(params.get("id").toString())) {
			sql += " AND t1.id=$S{id} ";
		}
		if (StringUtils.isNotEmpty(params.get("taskStart").toString())&& TaskStart.manual.getItemKey().equals(params.get("taskStart").toString())) {
			sql += " and t1.start_rule in ('1','2')";
		}else if(StringUtils.isNotEmpty(params.get("taskStart").toString())&& TaskStart.auto.getItemKey().equals(params.get("taskStart").toString())){
			sql += " AND t1.start_rule = $S{taskStart}";
		}
		return super.findRows(ScheduleProdRule.class, sql,
				DataSourceProperty.IDB, params);
	}
	/**
	 * @功能描述:查询满足该条信披规则中设置的产品参数
	 * @params:[prodCode]
	 * @return:java.util.List<com.kayak.core.sql.SqlRow>
	 * @Athor:ouyifan
	 * @date:2022/6/20
	 */
	public List<SqlRow> ProdParamsByCode(String prodCode, String disclosureType ,String disclosureSonType) throws Exception {
		StringBuilder sql = new StringBuilder("select PROD_MOD,ru.PROD_FULL_NAME," +
				"PROD_NM," +
				"PROD_CD," +
				"PROD_CCY," +
				"PROD_BRND\n" +
				"from APP_PRD_BAS_INF ap left join idb_disclosure_prod_rule ru on ap.PROD_CD = ru.prod_code where PROD_CD = $S{prodCode} and ru.disclosure_type = '"+disclosureType+"' and ru.disclosure_son_type = '"+disclosureSonType+"' ");
		return super.findRows(sql.toString(),
				DataSourceProperty.IDB,prodCode);
	}

	/**
	 * 计算两个指定格式的字符串类型的日期相差的工作日天数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public int computeTwoWorkDays(String date1, String date2) throws Exception {
		String sql = "select count(1) as count_num from sys_workday_set s where s.workday >= '" + date1 + "' and s.workday <= '" + date2 + "'";
		int betweenDate = super.findRow(sql, DataSourceProperty.PUB, null).getInteger("count_num");
		return betweenDate;
	}
}
