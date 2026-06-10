package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.basePublish.model.DisclosureRule;
import com.kayak.pms.disclosureControl.model.DisclosureProdRule;
import com.kayak.pms.global.constants.TaskStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DisclosureProdRuleDao extends ComnDao {
    /**
    * @功能描述:产品规则所有信息
    * @params:[params]
    * @return:com.kayak.core.sql.SqlResult<com.kayak.pms.disclosureControl.model.DisclosureProdRule>
    * @Athor:ouyifan
    * @date:2022/6/20
    */

    public SqlResult<DisclosureProdRule> findDisclosureProdRules(SqlParam<DisclosureProdRule> params) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT prodRule.id,prodRule.prod_code,prodRule.t8_disclosure_rule_id," +
                " prodRule.rule_name,prodRule.disclosure_type,prodRule.disclosure_son_type,prodRule.if_condition," +
                " CASE " +
                " IFNULL(dmct.count,'0') " +
                " WHEN '0' " +
                " THEN '0' " +
                " ELSE '1' " +
                " END if_clearing," +
                " prodRule.disclosure_mod_version_id," +
                " prodRule.notice_title,prodRule.status,prodRule.notice_roleid," +
                " prodRule.start_rule," +
                " IF(prodRule.base_date<>'' AND prodRule.base_date IS NOT NULL,prodRule.base_date , IFNULL(prodRule.net_value_date,'')) base_date," +
                " prodRule.exp_create_rule,prodRule.exp_create_days,prodRule.exp_create_attribute," +
                " prodRule.exp_supplement_rule,prodRule.exp_supplement_days,prodRule.exp_supplement_attribute," +
                " prodRule.exp_approval_rule,prodRule.exp_approval_days,prodRule.exp_approval_attribute," +
                " prodRule.exp_publish_rule,prodRule.exp_publish_days,prodRule.exp_publish_attribute," +
                " prodRule.crt_date,prodRule.crt_time,prodRule.crt_user_id,prodRule.crt_user_name," +
                " prodRule.upd_date,prodRule.upd_time,prodRule.upd_user_id,prodRule.upd_user_name," +
                " prodRule.remark,prodRule.source, prod.PROD_NM prod_name,prodRule.net_value_date_rule,prodRule.net_value_date,prodRule.prod_full_name," +
                " tdm.mod_name t8_disclosure_mod_name," +
                " tdmv.version version_number,prodRule.channel_ids,prodRule.disclosure_mod_id" +
                " FROM idb_disclosure_prod_rule prodRule" +
                " left join APP_PRD_BAS_INF prod " +
                " on prodRule.prod_code = prod.PROD_CD " +
                " left join idb_disclosure_rule rule" +
                " on prodRule.t8_disclosure_rule_id = rule.id " +
                " left join idb_disclosure_mod tdm on " +
                " tdm.id = prodRule.disclosure_mod_id " +
                " left join idb_disclosure_mod_version tdmv on " +
                " prodRule.disclosure_mod_version_id = tdmv.id " +
                " LEFT JOIN " +
                " (SELECT DISTINCT disclosure_mod_version_id,COUNT(*) `count` FROM idb_disclosure_mod_column " +
                " WHERE is_sysvalue = '2'  GROUP BY disclosure_mod_version_id)dmct " +
                " ON tdmv.id = dmct.disclosure_mod_version_id" +
                " where 1=1");

        if (StringUtils.isNotBlank(params.getModel().getProdName())) {
            sql.append(" and prod.PROD_NM like '%" + params.getModel().getProdName() + "%' ");
        }
        if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
            sql.append("  and prodRule.prod_code = $S{prodCode}  ");
        }
        if (StringUtils.isNotBlank(params.getModel().getT8DisclosureRuleId())) {
            sql.append("  and prodRule.t8_disclosure_rule_id = $S{t8DisclosureRuleId}  ");
        }
        if (StringUtils.isNotBlank(params.getModel().getDisclosureType())) {
            sql.append("  and prodRule.disclosure_type = $S{disclosureType}  ");
        }
        if (StringUtils.isNotBlank(params.getModel().getDisclosureSonType())) {
            sql.append("  and prodRule.disclosure_son_type = $S{disclosureSonType}  ");
        }

        if (StringUtils.isNotBlank(params.getModel().getSource())) {
            sql.append("  and prodRule.source = $S{source}  ");
        }

        if (StringUtils.isNotBlank(params.getModel().getT8DisclosureModName())) {
            sql.append("  and tdm.mod_name like '%"+params.getModel().getT8DisclosureModName()+"%'");
        }

        sql.append("   order by prodRule.crt_date desc,prodRule.crt_time desc  ");

        return super.findRows(sql.toString(),
                DataSourceProperty.IDB, params);
    }

    /**
     * 功能：根据产品id查询该产品所有的信披规则
     * 作者：rennannan
     * 日期：20210518
     *
     * @param prodCode
     * @return
     */
    public List<DisclosureProdRule> findDisclosureProdRuleList(String prodCode) throws Exception {
        String sql = "\tselect * \n" +
                "    from idb_disclosure_prod_rule \n" +
                "\t where prod_code=$S{prodCode};";
        return super.findRows(DisclosureProdRule.class, sql,
                DataSourceProperty.IDB,prodCode);
    }

    /**
     * 功能：查询已经维护过产品信披规则的产品作为复制功能下拉框
     * 作者：rennannan
     * 日期：20210518
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<DisclosureProdRule> findDisclosureProdDict(SqlParam<DisclosureProdRule> params) throws Exception {
        String sql = "select prod.prod_nm prod_name,prod.prod_cd prod_code\n" +
                "\t\t from(select DISTINCT prodRule.prod_code from idb_disclosure_prod_rule prodRule\n" +
                " WHERE prodRule.prod_code IS NOT NULL AND prodRule.prod_code<>'') prodRule\n" +
                "left join\t  APP_PRD_BAS_INF prod\n" +
                "       on prodRule.prod_code = prod.prod_cd" +
                " where prod.prod_cd is not null and prod.prod_cd <> ''";
        return super.findRows(sql,
                DataSourceProperty.IDB,params);
    }

    /**
     * 功能：查询不存在产品信息规则表中的产品作为下拉框
     * 作者：rennannan
     * 日期：20210518
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<DisclosureProdRule> findNeedCopyProds(SqlParam<DisclosureProdRule> params) throws Exception {
        String sql = " SELECT prod.prod_nm prod_name,prod.prod_cd prod_code\n" +
                "FROM APP_PRD_BAS_INF prod\n" +
                "WHERE 1=1 and prod.prod_cd is not null and prod.prod_cd <> ''\n" +
                "AND prod.prod_cd NOT IN (\n" +
                "$S{prodCode})\n" ;
//                "AND prod.prod_cd NOT IN (SELECT DISTINCT prodRule.prod_code FROM idb_disclosure_prod_rule prodRule WHERE prodRule.prod_code IS NOT NULL AND prodRule.prod_code<>'')";
        return super.findRows(sql,
                DataSourceProperty.IDB,params);
    }
    /**
    * @功能描述:添加产品信披规则
    * @params:[rule]
    * @return:com.kayak.core.sql.UpdateResult
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public UpdateResult addDisclosureProdRule(DisclosureProdRule rule) throws Exception {
        return super.update("INSERT INTO idb_disclosure_prod_rule(id,prod_code,t8_disclosure_rule_id," +
                        " rule_name,disclosure_type,disclosure_son_type," +
                        " disclosure_mod_version_id,disclosure_mod_id,channel_ids,notice_title," +
                        " status,notice_roleid," +
                        " start_rule,base_date," +
                        " exp_create_rule,exp_create_days,exp_create_attribute," +
                        " exp_supplement_rule,exp_supplement_days,exp_supplement_attribute," +
                        " exp_approval_rule,exp_approval_days,exp_approval_attribute," +
                        " exp_publish_rule,exp_publish_days,exp_publish_attribute," +
                        " crt_date,crt_time,crt_user_id,crt_user_name," +
                        " upd_date,upd_time,upd_user_id,upd_user_name," +
                        " remark,source,net_value_date_rule,net_value_date," +
                        " if_condition,prod_full_name) " +
                        "  VALUES($AUTOIDS{id},$S{prodCode},$S{t8DisclosureRuleId}," +
                        " $S{ruleName},$S{disclosureType},$S{disclosureSonType}," +
                        " $S{disclosureModVersionId},$S{disclosureModId},$S{channelIds},$S{noticeTitle}," +
                        " $S{status},$S{noticeRoleid}," +
                        " $S{startRule},$S{baseDate}," +
                        " $S{expCreateRule},$S{expCreateDays},$S{expCreateAttribute}," +
                        " $S{expSupplementRule},$S{expSupplementDays},$S{expSupplementAttribute}," +
                        " $S{expApprovalRule},$S{expApprovalDays},$S{expApprovalAttribute}," +
                        " $S{expPublishRule},$S{expPublishDays},$S{expPublishAttribute}," +
                        " $S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName}," +
                        " $S{updDate},$S{updTime},$S{updUserId},$S{updUserName}," +
                        " $S{remark},$S{source},$S{netValueDateRule},$S{netValueDate}," +
                        " $S{ifCondition},$S{prodFullName})",
                DataSourceProperty.IDB,rule);
    }

    /**
    * @功能描述:新增产品信披规则
    * @params:[params]
    * @return:com.kayak.core.sql.UpdateResult
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public UpdateResult addDisclosureProdRuleOnEnable(DisclosureProdRule params) throws Exception {
        return super.update("INSERT INTO idb_disclosure_prod_rule(id,prod_code," +
                        " t8_disclosure_rule_id,rule_name,disclosure_type,disclosure_son_type," +
                        " disclosure_mod_id,disclosure_mod_version_id,channel_ids,notice_title,status," +
                        " notice_roleid," +
                        " start_rule," +
                        " base_date,exp_create_rule,exp_create_days," +
                        " exp_create_attribute,exp_supplement_rule," +
                        " exp_supplement_days,exp_supplement_attribute," +
                        " exp_approval_rule,exp_approval_days," +
                        " exp_approval_attribute,exp_publish_rule," +
                        " exp_publish_days,exp_publish_attribute," +
                        " crt_date,crt_time,crt_user_id,crt_user_name," +
                        " upd_date,upd_time,upd_user_id,upd_user_name," +
                        " remark,source,net_value_date_rule,net_value_date," +
                        " if_condition,prod_full_name) " +
                        " VALUES($AUTOIDS{id},$S{prodCode}," +
                        " $S{t8DisclosureRuleId},$S{ruleName},$S{disclosureType},$S{disclosureSonType}," +
                        " $S{disclosureModId},$S{disclosureModVersionId},$S{channelIds},$S{noticeTitle},$S{status}," +
                        " $S{noticeRoleid}," +
                        " $S{startRule}," +
                        " $S{baseDate},$S{expCreateRule},$S{expCreateDays}," +
                        " $S{expCreateAttribute},$S{expSupplementRule}," +
                        " $S{expSupplementDays},$S{expSupplementAttribute}," +
                        " $S{expApprovalRule},$S{expApprovalDays}," +
                        " $S{expApprovalAttribute},$S{expPublishRule}," +
                        " $S{expPublishDays},$S{expPublishAttribute}," +
                        " $S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName}," +
                        " $S{updDate},$S{updTime},$S{updUserId},$S{updUserName}," +
                        " $S{remark},$S{source},$S{netValueDateRule},$S{netValueDate}," +
                        " $S{ifCondition},$S{prodFullName})",
                DataSourceProperty.IDB, params);
    }
    /**
    * @功能描述:校验该产品是否存在该信披类型
    * @params:[params]
    * @return:java.lang.Integer
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public Integer findRulesByDisclosureForExist(SqlParam<DisclosureProdRule> params) throws Exception {
        String sql = "SELECT COUNT(*) `count` FROM idb_disclosure_prod_rule WHERE 1=1 \n";
        if(org.apache.commons.lang3.StringUtils.isNotBlank(params.getModel().getDisclosureSonType())){
            sql = sql + " AND disclosure_son_type ='"+params.getModel().getDisclosureSonType()+"' ";
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(params.getModel().getDisclosureType())){
            sql = sql + " AND disclosure_type ='"+params.getModel().getDisclosureType()+"' ";
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(params.getModel().getProdCode())){
            sql = sql + " AND prod_code ='"+params.getModel().getProdCode()+"' ";
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(params.getModel().getId())){
            sql = sql + " AND id <>'"+params.getModel().getId()+"' ";
        }
        return super.findRow(sql,
                DataSourceProperty.IDB,params.getModel()).getInteger("count");
    }
    public Integer findTaskByDisclosureForExist(SqlParam<DisclosureProdRule> params) throws Exception {
        String sql = "SELECT count(*) count FROM idb_disclosure_prod_task WHERE  disclosure_type=$S{disclosureType} " +
                "and prod_base_date=$S{baseDate}  and status NOT IN ('"+TaskStatus.alreadyGenerate.getItemKey()+"')" ;
        if (StringUtils.isNotEmpty(params.getModel().getDisclosureSonType())) {
            sql += " and disclosure_son_type=$S{disclosureSonType}";
        }
        if (StringUtils.isNotEmpty(params.getModel().getProdCode())) {
            sql += " and prod_code=$S{prodCode}";
        }
        return super.findRow(sql,
                DataSourceProperty.IDB,params.getModel()).getInteger("count");
    }
    public Integer findTaskByDisForExist(SqlParam<DisclosureRule> params) throws Exception {
        String sql = "SELECT count(*) count FROM idb_disclosure_prod_task WHERE  disclosure_type=$S{disclosureType} " +
                "and prod_base_date=$S{baseDate}  and status NOT IN ('"+TaskStatus.alreadyGenerate.getItemKey()+"')" ;
        if (StringUtils.isNotEmpty(params.getModel().getDisclosureSonType())) {
            sql += " and disclosure_son_type=$S{disclosureSonType}";
        }
        return super.findRow(sql,
                DataSourceProperty.IDB,params.getModel()).getInteger("count");
    }
    /**
    * @功能描述:更新产品信披规则
    * @params:[params]
    * @return:com.kayak.core.sql.UpdateResult
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public UpdateResult updateDisclosureProdRule(SqlParam<DisclosureProdRule> params) throws Exception {
        return super.update("UPDATE idb_disclosure_prod_rule " +
                        " SET prod_code=$S{prodCode} ,t8_disclosure_rule_id=$S{t8DisclosureRuleId} ," +
                        " rule_name=$S{ruleName} ,disclosure_type=$S{disclosureType} ," +
                        " disclosure_son_type=$S{disclosureSonType} ," +
                        " disclosure_mod_version_id=$S{disclosureModVersionId}," +
                        " channel_ids = $S{channelIds},notice_title=$S{noticeTitle} ," +
                        " disclosure_mod_id = $S{disclosureModId}," +
                        " status=$S{status} ,notice_roleid=$S{noticeRoleid} ," +
                        " start_rule=$S{startRule} ," +
                        " base_date=$S{baseDate} ," +
                        " exp_create_rule=$S{expCreateRule} ,exp_create_days=$S{expCreateDays} ,exp_create_attribute=$S{expCreateAttribute} ," +
                        " exp_supplement_rule=$S{expSupplementRule} ,exp_supplement_days=$S{expSupplementDays} ,exp_supplement_attribute=$S{expSupplementAttribute} ," +
                        " exp_approval_rule=$S{expApprovalRule} ,exp_approval_days=$S{expApprovalDays} ,exp_approval_attribute=$S{expApprovalAttribute} ," +
                        " exp_publish_rule=$S{expPublishRule} ,exp_publish_days=$S{expPublishDays} ,exp_publish_attribute=$S{expPublishAttribute} ," +
                        " upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user_id=$S{updUserId} ,upd_user_name=$S{updUserName} ," +
                        " remark=$S{remark} ,source=$S{source} ,net_value_date_rule=$S{netValueDateRule},net_value_date=$S{netValueDate}," +
                        " if_condition = $S{ifCondition},prod_full_name = $S{prodFullName} " +
                        " WHERE  id=$S{id} ",
                DataSourceProperty.IDB, params.getModel());
    }
    /**
    * @功能描述:删除产品信披规则
    * @params:[params]
    * @return:com.kayak.core.sql.UpdateResult
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public UpdateResult deleteDisclosureProdRule(SqlParam<DisclosureProdRule> params) throws Exception {
        return super.update("DELETE FROM idb_disclosure_prod_rule WHERE  id=$S{id} ",
                DataSourceProperty.IDB, params.getModel());
    }
    /**
    * @功能描述:根据信披类型删除产品信披规则
    * @params:[params]
    * @return:com.kayak.core.sql.UpdateResult
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public UpdateResult deleteProdRuleByType(DisclosureProdRule rule) throws Exception {

        String sql = "DELETE FROM idb_disclosure_prod_rule WHERE 1=1 AND disclosure_type =$S{disclosureType} \n" +
                " AND prod_code = $S{prodCode} \n" ;
        if (StringUtils.isNotEmpty(rule.getDisclosureSonType())) {
            sql += " AND disclosure_son_type=$S{disclosureSonType}\n";
        }
        return super.update(sql,
                DataSourceProperty.IDB, rule);
    }
    /**
    * @功能描述:查询满足该条信披规则中设置的产品参数
    * @params:[prodCode]
    * @return:java.util.List<com.kayak.core.sql.SqlRow>
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public List<SqlRow> ProdParamsConfigByCode(String prodCode) throws Exception {
        StringBuilder sql = new StringBuilder("select INV_PRD_DIME,PROD_NM," +
                "INV_PRD_LEN," +
                "PROD_OBJ," +
                "PROD_CLC_MTH," +
                "PROD_FORM," +
                "PROD_SER_CD,\n" +
                "PROD_INV_TYP,\n" +
                "MOTHER_FUND_FLAG\n" +
                "from APP_PRD_BAS_INF where PROD_CD = $S{prodCode}");
        return super.findRows(sql.toString(),
                DataSourceProperty.PUB,prodCode);
    }

    public SqlRow findDisclosureRules(SqlParam<DisclosureProdRule> params) throws Exception {
        String sql = "SELECT \n" +
                "  dr.id t8DisclosureRuleId,\n" +
                "  dr.disclosure_mod_version_id disclosureModVersionId,\n" +
                "  dr.notice_title noticeTitle,\n" +
                "  dr.`status`,\n" +
                "  dr.notice_roleid noticeRoleid,\n" +
                "  dr.start_rule startRule,\n" +
                "  dr.base_date baseDate,\n" +
                "  dr.exp_create_rule expCreateRule,\n" +
                "  dr.exp_create_days expCreateDays ,\n" +
                "  dr.exp_create_attribute expCreateAttribute,\n" +
                "  dr.exp_supplement_rule expSupplementRule,\n" +
                "  dr.exp_supplement_days  expSupplementDays,\n" +
                "  dr.exp_supplement_attribute expSupplementAttribute,\n" +
                "  dr.exp_approval_rule expApprovalRule,\n" +
                "  dr.exp_approval_days expApprovalDays,\n" +
                "  dr.exp_approval_attribute expApprovalAttribute,\n" +
                "  dr.exp_publish_rule expPublishRule,\n" +
                "  dr.exp_publish_days expPublishDays,\n" +
                "  dr.exp_publish_attribute expPublishAttribute,\n" +
                "  dr.remark,\n" +
                "  CASE \n" +
                "  IFNULL(dmct.count,'0')\n" +
                "  WHEN '0' \n" +
                "  THEN '0' \n" +
                "  ELSE '1' \n" +
                "  END ifClearing,\n" +
                "  dr.if_condition ifCondition,\n" +
                "  dr.disclosure_mod_id disclosureModId\n" +
                "  FROM\n" +
                "  idb_disclosure_rule dr \n" +
                "  LEFT JOIN idb_disclosure_mod_version dmv \n" +
                "  ON dr.disclosure_mod_version_id = dmv.id \n" +
                " LEFT JOIN \n" +
                " (SELECT DISTINCT disclosure_mod_version_id,COUNT(*) `count` FROM idb_disclosure_mod_column \n" +
                " WHERE is_sysvalue = '2'  GROUP BY disclosure_mod_version_id)dmct\n" +
                " ON dmv.id = dmct.disclosure_mod_version_id\n" +
                " where 1=1  and dr.id  = $S{id}";
        SqlRow row =super.findRow(sql,
                DataSourceProperty.IDB, params.getModel());
        return row;
    }

    public SqlRow findBassInfoByProdCode(SqlParam<DisclosureProdRule> params) throws Exception {
        String sql = " select PROD_FORM as prodForm,INV_PRD_DIME as invProDime,INV_PRD_LEN as invPrdLed,PROD_OBJ as prodObj,PROD_CLC_MTH as prodClcMth,PROD_INV_TYP as prodInvTyp,PROD_SER_NM as prodSerNm,PROD_CD as prodCode from APP_PRD_BAS_INF where PROD_CD = $S{prodCode}";
        SqlRow row =super.findRow(sql,DataSourceProperty.IDB, params.getModel());
        return row;
    }
}
