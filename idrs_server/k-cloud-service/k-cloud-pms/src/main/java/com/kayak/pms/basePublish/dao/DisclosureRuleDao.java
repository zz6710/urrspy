package com.kayak.pms.basePublish.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.pms.basePublish.model.DisclosureChannel;
import com.kayak.pms.basePublish.model.DisclosureChannelRule;
import com.kayak.pms.basePublish.model.DisclosureRule;
import com.kayak.pms.disclosureControl.model.DisclosureProdRule;
import com.kayak.pms.global.constants.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class DisclosureRuleDao extends ComnDao {

    public SqlResult<DisclosureRule> findDisclosureRules(SqlParam<DisclosureRule> params) throws Exception {
        String sql = "SELECT  " +
                "  dr.id, " +
                "  dr.rule_name, " +
                "  dr.disclosure_type, " +
                "  dr.disclosure_son_type, " +
                "  dr.disclosure_mod_version_id, " +
                "  dr.notice_title, " +
                "  dr.`status`, " +
                "  dr.notice_roleid, " +
                "  dr.start_rule, " +
                "  IF(dr.base_date<>'' AND dr.base_date IS NOT NULL,dr.base_date , IFNULL(dr.net_value_date,'')) base_date, " +
                "  dr.exp_create_rule, " +
                "  dr.exp_create_days, " +
                "  dr.exp_create_attribute, " +
                "  dr.exp_supplement_rule, " +
                "  dr.exp_supplement_days, " +
                "  dr.exp_supplement_attribute, " +
                "  dr.exp_approval_rule, " +
                "  dr.exp_approval_days, " +
                "  dr.exp_approval_attribute, " +
                "  dr.exp_publish_rule, " +
                "  dr.exp_publish_days, " +
                "  dr.exp_publish_attribute, " +
                "  dr.crt_date, " +
                "  dr.crt_time, " +
                "  dr.crt_user_id, " +
                "  dr.crt_user_name, " +
                "  dr.upd_date, " +
                "  dr.upd_time, " +
                "  dr.upd_user_id, " +
                "  dr.upd_user_name, " +
                "  dr.remark, " +
                "  dr.net_value_date_rule, " +
                "  dr.net_value_date, " +
                "  dmv.doc_name, " +
                "  dmv.version version_number, " +
                "  dm.mod_name t8_disclosure_mod_name, " +
                "  dr.PROD_CLC_MTH, " +
                "  dr.PROD_FORM, " +
                "  dr.INV_PRD_DIME, " +
                "  dr.INV_PRD_LEN, " +
                "  dr.PROD_OBJ, " +
                "  dr.PROD_SER_CD, " +
                "  sr.PROD_SER_NM, " +
                "  CASE " +
                "  IFNULL(dmct.count,'0') " +
                "  WHEN '0'  " +
                "  THEN '0'  " +
                "  ELSE '1'  " +
                "  END if_clearing, " +
                "  dr.if_condition, " +
                "  dr.prod_inv_typ, " +
                "  dr.disclosure_mod_id, " +
                "  dr.mother_fund_flag " +
                " FROM " +
                "  idb_disclosure_rule dr  " +
                "  LEFT JOIN idb_disclosure_mod_version dmv  " +
                "    ON dr.disclosure_mod_version_id = dmv.id  " +
                " LEFT JOIN idb_disclosure_mod dm " +
                " ON dm.id = dmv.disclosure_mod_id " +
                "LEFT JOIN  " +
                "(SELECT DISTINCT disclosure_mod_version_id,COUNT(*) `count` FROM idb_disclosure_mod_column  " +
                "WHERE is_sysvalue = '2'  GROUP BY disclosure_mod_version_id)dmct " +
                "ON dmv.id = dmct.disclosure_mod_version_id  " +
                " LEFT JOIN (SELECT DISTINCT k.PROD_SER_CD,k.PROD_SER_NM FROM APP_PRD_BAS_INF k) sr ON dr.PROD_SER_CD = sr.PROD_SER_CD" +
                " where 1=1 ";

        if(org.apache.commons.lang3.StringUtils.isNotBlank(params.getModel().getId())){
            sql = sql + " and dr.id  =  '"+params.getModel().getId()+"' ";
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(params.getModel().getRuleName())){
            sql = sql + " and dr.rule_name  like '"+"%"+params.getModel().getRuleName()+"%"+"' ";
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(params.getModel().getStartRule())){
            sql = sql + " and dr.start_rule  =  '"+params.getModel().getStartRule()+"' ";
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(params.getModel().getStatus())){
            sql = sql + " and dr.status  =  '"+params.getModel().getStatus()+"' ";
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(params.getModel().getDisclosureType())){
            sql = sql + " and dr.disclosure_type  =  '"+params.getModel().getDisclosureType()+"' ";
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(params.getModel().getDisclosureSonType())){
            sql = sql + " and dr.disclosure_son_type  =  '"+params.getModel().getDisclosureSonType()+"' ";
        }
        sql = sql+"ORDER BY crt_date DESC, crt_time DESC ";
        SqlResult<DisclosureRule> rows = super.findRows(sql,
                DataSourceProperty.IDB,params);
        if (CollectionUtil.isNotEmpty(rows.getRows())) {
            rows.getRows().forEach(disclosureRule -> {
                if (Tools.isNotEmpty(disclosureRule.getDocName())) {
                    //在这里处理下后缀   以最后一个.截取
                    String substring = disclosureRule.getDocName().substring(0, disclosureRule.getDocName().lastIndexOf("."));
                    disclosureRule.setDocName(substring);
                }
            });
        }
        return rows;
    }
    public List<DisclosureRule> findDisclosureRules2(DisclosureRule disclosureRule) throws Exception {
        String sql = "SELECT  " +
                "  dr.id, " +
                "  dr.rule_name, " +
                "  dr.disclosure_type, " +
                "  dr.disclosure_son_type, " +
                "  dr.disclosure_mod_version_id, " +
                "  dr.notice_title, " +
                "  dr.`status`, " +
                "  dr.notice_roleid, " +
                "  dr.start_rule, " +
                "  dr.base_date, " +
                "  dr.exp_create_rule, " +
                "  dr.exp_create_days, " +
                "  dr.exp_create_attribute, " +
                "  dr.exp_supplement_rule, " +
                "  dr.exp_supplement_days, " +
                "  dr.exp_supplement_attribute, " +
                "  dr.exp_approval_rule, " +
                "  dr.exp_approval_days, " +
                "  dr.exp_approval_attribute, " +
                "  dr.exp_publish_rule, " +
                "  dr.exp_publish_days, " +
                "  dr.exp_publish_attribute, " +
                "  dr.crt_date, " +
                "  dr.crt_time, " +
                "  dr.crt_user_id, " +
                "  dr.crt_user_name, " +
                "  dr.upd_date, " +
                "  dr.upd_time, " +
                "  dr.upd_user_id, " +
                "  dr.upd_user_name, " +
                "  dr.remark, " +
                "  dr.net_value_date_rule, " +
                "  dr.net_value_date, " +
                "  dmv.doc_name, " +
                "  dmv.version version_number, " +
                "  dr.PROD_CLC_MTH, " +
                "  dr.PROD_FORM, " +
                "  dr.INV_PRD_DIME, " +
                "  dr.INV_PRD_LEN, " +
                "  dr.PROD_OBJ, " +
                "  dr.PROD_SER_CD, " +
                "  CASE " +
                "   IFNULL(dmct.count,'0') " +
                "   WHEN '0'  " +
                "   THEN '0'  " +
                "   ELSE '1'  " +
                "   END if_clearing, " +
                "  dr.if_condition, " +
                "  dr.prod_inv_typ, " +
                "  dr.disclosure_mod_id " +
                "FROM " +
                "  idb_disclosure_rule dr  " +
                "  LEFT JOIN idb_disclosure_mod_version dmv  " +
                "    ON dr.disclosure_mod_version_id = dmv.id  " +
                "  LEFT JOIN  " +
                "   (SELECT DISTINCT disclosure_mod_version_id,COUNT(*) `count` FROM idb_disclosure_mod_column  " +
                "   WHERE is_sysvalue = '2'  GROUP BY disclosure_mod_version_id)dmct " +
                "   ON dmv.id = dmct.disclosure_mod_version_id " +
                "    where 1=1  and dr.id  = $S{id}";
        return super.findRows(DisclosureRule.class, sql,
                DataSourceProperty.IDB, disclosureRule);
    }
    /**
     * 功能：查询信披规则中对应信披类型模板
     * 作者：ouyifan
     * 日期：20220604
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<DisclosureRule> findDisclosureModsWithRule(SqlParam<DisclosureRule> params) throws Exception {
        String sql = "SELECT  " +
                "*  " +
                "FROM " +
                "(SELECT  " +
                "tdm.id disclosure_mod_id, " +
                "tdm.mod_name t8_disclosure_mod_name, " +
                "tdm.disclosure_type, " +
                "tdm.disclosure_son_type, " +
                "tdm.crt_date, " +
                "tdm.crt_time, " +
                "tdvm.upd_date, " +
                "tdvm.upd_time, " +
                "tdvm.version version_number  " +
                "FROM " +
                "idb_disclosure_mod tdm  " +
                "LEFT JOIN idb_disclosure_mod_version tdvm  " +
                "ON tdm.id = tdvm.disclosure_mod_id  " +
                "AND VERSION=  " +
                "(SELECT  " +
                "MAX(VERSION)  " +
                "FROM " +
                "idb_disclosure_mod_version  " +
                "WHERE disclosure_mod_id = tdm.id)) t  " +
                "WHERE 1 = 1 ";
        if(org.apache.commons.lang3.StringUtils.isNotBlank(params.getModel().getDisclosureType())){
            sql = sql + " and t.disclosure_type  =  '"+params.getModel().getDisclosureType()+"' ";
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(params.getModel().getDisclosureSonType())&&!"5".equals(params.getModel().getDisclosureType())){
            sql = sql + " and t.disclosure_son_type =  '"+params.getModel().getDisclosureSonType() +"' ";
        }
        sql = sql + " order by t.upd_date,t.upd_time";
        return super.findRows(sql,
                DataSourceProperty.IDB,params);
    }
    /**
     * 功能：查询信披规则中对应信披类型模板版本号
     * 作者：ouyifan
     * 日期：20220604
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<DisclosureRule> findDisclosureModsVWithRule(SqlParam<DisclosureRule> params) throws Exception {
        String sql = "SELECT  " +
                "*  " +
                "FROM " +
                "(SELECT  " +
                "tdm.id disclosure_mod_id, " +
                "tdvm.id disclosure_mod_version_id, " +
                "tdvm.doc_name t8_disclosure_version_name, " +
                "tdvm.status mod_V_Status, " +
                "tdvm.crt_date, " +
                "tdvm.crt_time, " +
                "tdvm.upd_date, " +
                "tdvm.upd_time, " +
                "tdvm.version version_number  " +
                "FROM " +
                "idb_disclosure_mod_version tdvm  " +
                "LEFT JOIN idb_disclosure_mod tdm  " +
                "ON tdm.id = tdvm.disclosure_mod_id  " +
                "WHERE tdvm.`status` = 1) t  " +
                "WHERE 1 = 1  ";
        if(org.apache.commons.lang3.StringUtils.isNotBlank(params.getModel().getDisclosureModId())){
            sql = sql + " and t.disclosure_mod_id = '"+params.getModel().getDisclosureModId()+"' ";
        }
        sql = sql + " order by t.upd_date,t.upd_time";
        return super.findRows(sql,
                DataSourceProperty.IDB,params);
    }
    /**
     * 功能：查询信披规则中对应信披类型模板版本是否补录
     * 作者：ouyifan
     * 日期：20220604
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<DisclosureRule> clearingOrNot(SqlParam<DisclosureRule> params) throws Exception {
        String sql = "SELECT  " +
                "CASE " +
                "t.`count`  " +
                "WHEN '0'  " +
                "THEN '0'  " +
                "ELSE '1'  " +
                "END if_clearing  " +
                "FROM " +
                "(SELECT  " +
                "COUNT(*) `count`  " +
                "FROM " +
                "idb_disclosure_mod_column dmc  " +
                "WHERE 1 = 1  " +
                "AND dmc.is_sysvalue = '"+ IsSysvalue.hand.getItemKey()+"'  ";
        if(org.apache.commons.lang3.StringUtils.isNotBlank(params.getModel().getDisclosureModVersionId())){
            sql = sql + " and dmc.disclosure_mod_version_id = '"+params.getModel().getDisclosureModVersionId()+"' ";
        }
        sql = sql + ") t";
        return super.findRows(sql,
                DataSourceProperty.IDB,params);
    }

    public DisclosureRule findGGDisclosureRules(String id) throws Exception {
        String sql = "SELECT tdmv.id,tdpr.disclosure_son_type as type " +
                " FROM idb_disclosure_notice tdn " +
                " left join idb_disclosure_prod_rule tdpr " +
                " on tdn.t8_disclosure_rule_id=tdpr.id " +
                " left join idb_disclosure_mod_version tdmv " +
                " on tdpr.disclosure_mod_version_id=tdmv.id" +
                " where tdn.id='" + id + "'";
        SqlRow sqlRow = super.findRow(sql,
                DataSourceProperty.IDB,id);
        DisclosureRule disclosureRule = new DisclosureRule();
        disclosureRule.setId(sqlRow.getString("id"));
        disclosureRule.setDisclosureSonType(sqlRow.getString("type"));
        return disclosureRule;
    }

    public UpdateResult addDisclosureRule(SqlParam<DisclosureRule> params) throws Exception {
        return super.update("INSERT INTO idb_disclosure_rule(id,rule_name,disclosure_type,disclosure_son_type," +
                        "disclosure_mod_version_id,notice_title," +
                        "status,notice_roleid," +
                        "start_rule," +
                        "base_date,exp_create_rule,exp_create_days,exp_create_attribute," +
                        "exp_supplement_rule,exp_supplement_days,exp_supplement_attribute," +
                        "exp_approval_rule,exp_approval_days,exp_approval_attribute,exp_publish_rule," +
                        "exp_publish_days,exp_publish_attribute," +
                        "crt_date,crt_time,crt_user_id,crt_user_name," +
                        "upd_date,upd_time,upd_user_id,upd_user_name," +
                        "remark,net_value_date_rule,net_value_date," +
                        "PROD_CLC_MTH," +
                        "if_condition,PROD_FORM,INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_SER_CD,disclosure_mod_id,prod_inv_typ,mother_fund_flag) " +
                        " VALUES( $AUTOIDS{id},$S{ruleName},$S{disclosureType},$S{disclosureSonType}," +
                        " $S{disclosureModVersionId},$S{noticeTitle}," +
                        " $S{status},$S{noticeRoleid}," +
                        " $S{startRule}," +
                        " $S{baseDate},$S{expCreateRule},$S{expCreateDays},$S{expCreateAttribute}," +
                        " $S{expSupplementRule},$S{expSupplementDays},$S{expSupplementAttribute}," +
                        " $S{expApprovalRule},$S{expApprovalDays},$S{expApprovalAttribute},$S{expPublishRule}," +
                        " $S{expPublishDays},$S{expPublishAttribute}," +
                        " $S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName}," +
                        " $S{updDate},$S{updTime},$S{updUserId},$S{updUserName}," +
                        " $S{remark},$S{netValueDateRule},$S{netValueDate}," +
                        " $S{prodClcMth}," +
                        "$S{ifCondition},$S{prodForm},$S{invPrdDime}, " +
                        "$S{invPrdLen},$S{prodObj},$S{prodSerCd},$S{disclosureModId},$S{prodInvTyp},$S{motherFundFlag})",
                DataSourceProperty.IDB, params.getModel());
    }

    public UpdateResult updateDisclosureRule(SqlParam<DisclosureRule> params) throws Exception {
        return super.update("UPDATE idb_disclosure_rule " +
                        "   SET rule_name=$S{ruleName} ,disclosure_type=$S{disclosureType} ," +
                        "   disclosure_son_type=$S{disclosureSonType} ," +
                        "   disclosure_mod_version_id=$S{disclosureModVersionId} ,notice_title=$S{noticeTitle} ," +
                        "   status=$S{status} ,notice_roleid=$S{noticeRoleid} ," +
                        "   start_rule=$S{startRule} ," +
                        "   base_date=$S{baseDate} ," +
                        "   exp_create_rule=$S{expCreateRule} ,exp_create_days=$S{expCreateDays} ,exp_create_attribute=$S{expCreateAttribute} ," +
                        "   exp_supplement_rule=$S{expSupplementRule} ,exp_supplement_days=$S{expSupplementDays} ,exp_supplement_attribute=$S{expSupplementAttribute} ," +
                        "   exp_approval_rule=$S{expApprovalRule} ,exp_approval_days=$S{expApprovalDays} ,exp_approval_attribute=$S{expApprovalAttribute} ," +
                        "   exp_publish_rule=$S{expPublishRule} ,exp_publish_days=$S{expPublishDays} ,exp_publish_attribute=$S{expPublishAttribute} ," +
                        "   upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user_id=$S{updUserId} ,upd_user_name=$S{updUserName} ," +
                        "   remark=$S{remark}," +
                        "   net_value_date_rule= $S{netValueDateRule},net_value_date=$S{netValueDate}," +
                        "   PROD_CLC_MTH=$S{prodClcMth}," +
                        "   if_condition=$S{ifCondition}," +
                        "   PROD_FORM=$S{prodForm}," +
                        "   INV_PRD_DIME=$S{invPrdDime}," +
                        "   INV_PRD_LEN=$S{invPrdLen}," +
                        "   PROD_OBJ=$S{prodObj}," +
                        "   PROD_SER_CD=$S{prodSerCd}," +
                        "   disclosure_mod_id = $S{disclosureModId},prod_inv_typ = $S{prodInvTyp},mother_fund_flag = $S{motherFundFlag}" +
                        " WHERE  id=$S{id} ",
                DataSourceProperty.IDB, params.getModel());
    }

    public UpdateResult deleteDisclosureRule(SqlParam<DisclosureRule> params) throws Exception {
        return super.update("DELETE FROM idb_disclosure_rule WHERE  id=$S{id} ",
                DataSourceProperty.IDB, params.getModel());
    }

    /**
     * 功能：根据信披类型数据查询信披子类型字典数据
     * 作者：rennannan
     * 日期：20210513
     *
     * @return
     */
    public List<SqlRow> getDisclosureSonType(String disclosure_type) throws Exception {
        String sql = "select itemkey value,itemval label " +
                " from sys_dict_item " +
                " where dict = (CASE $S{doc_type} " +
                " WHEN '5' THEN 'xp_son_dtype'" +
                " WHEN '6' THEN 'xp_son_ztype'" +
                " END)" +
                " order by itemkey+0";
        return super.findRows(sql,
                DataSourceProperty.IDB,disclosure_type);
    }

    /**
     * 功能：修改信披规则状态
     * 作者：rennannan
     * 日期：20210515
     *
     * @param
     * @return
     * @throws Exception
     */
    public int updateDisRuleStatus(DisclosureRule rule) throws Exception {
        String sql = "update idb_disclosure_rule " +
                "        set status=$S{status}" +
                "      where id=$S{id}";
        return super.update(sql,
                DataSourceProperty.IDB,rule).getEffect();
    }

    /**
    * @功能描述:校验规则是否存在
    * @params:[params]
    * @return:java.lang.Integer
    * @Athor:ouyifan
    * @date:2022/6/20
    */

    public Integer checkIsExists(SqlParam<DisclosureRule> params) throws Exception {
        StringBuilder sql = new StringBuilder("select count(*) count from idb_disclosure_rule where  disclosure_type = $S{disclosureType}" +
                "\tand PROD_FORM = $S{prodForm} " +
                "\tand PROD_OBJ = $S{prodObj} " +
                "\tand PROD_CLC_MTH=$S{prodClcMth} " +
                "\tAND INV_PRD_DIME  = $S{invPrdDime} " +
                "\tAND INV_PRD_LEN  = $S{invPrdLen} " +
                "\tand prod_inv_typ = $S{prodInvTyp} " +
                "\tand PROD_SER_CD=$S{prodSerCd} and mother_fund_flag = $S{motherFundFlag}" );
        //信披子类型
        if (!StringUtils.isEmpty(params.getModel().getDisclosureSonType())) {
            sql.append(" and disclosure_son_type = $S{disclosureSonType} ");
        }
        //规则id
        if (!StringUtils.isEmpty(params.getModel().getId())) {
            sql.append(" and id != $S{id} ");//修改时不验证当前数据
        }
        return super.findRow(sql.toString(),
                DataSourceProperty.IDB,params.getModel()).getInteger("count");
    }
    /**
    * @功能描述:校验规则名称是否存在
    * @params:[params]
    * @return:com.kayak.core.sql.SqlResult<com.kayak.pms.basePublish.model.DisclosureRule>
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public SqlResult<DisclosureRule> checkNameIsExists(SqlParam<DisclosureRule> params) throws Exception {
        return super.findRows("select id  from idb_disclosure_rule where rule_name = $S{ruleName} and id != $S{id}",
                DataSourceProperty.IDB,params);
    }

    /**
     * 功能：更新信披规则的产品ids
     * 作者：rennannan
     * 日期：20211116
     *
     * @param rule
     * @throws Exception
     */
    public void updateRuleInfoIds(DisclosureRule rule) throws Exception {
        String sql = " ";
        super.update(sql,
                DataSourceProperty.IDB,rule);
    }
    /**
    * @功能描述:查询满足该条信披规则中设置的产品参数的产品id
    * @params:[params]
    * @return:java.util.List<com.kayak.core.sql.SqlRow>
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public List<SqlRow> prodIDByProdParamsConfig(SqlParam<DisclosureRule> params) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT prod.PROD_CD prod_code FROM APP_PRD_BAS_INF prod WHERE 1=1 ");
        return super.findRows(sql.toString(),
                DataSourceProperty.PUB,params.getModel());
    }
    /**
    * @功能描述:查询所有生成规则里启动状态规则的信披类型(除整体、净值整体公告类型)
    * @params:[params]
    * @return:java.util.List<com.kayak.core.sql.SqlRow>
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public List<SqlRow> prodDisclosureType(SqlParam<DisclosureRule> params) throws Exception {
        StringBuilder sql = new StringBuilder("select DISTINCT rule.disclosure_type,rule.disclosure_son_type from idb_disclosure_rule rule where rule.status = '"+ XpStatus.start.getItemKey() +"' " +
                " and rule.disclosure_type not in ('"+ DisclosureType.ensemble.getItemKey() +"') and rule.disclosure_son_type not in ('"+ DisclosureSonType.netValueEntity.getItemKey() +"')");
        return super.findRows(sql.toString(),
                DataSourceProperty.IDB,params.getModel());
    }
    /**
    * @功能描述:生成规则里所有产品
    * @params:[params]
    * @return:java.util.List<com.kayak.core.sql.SqlRow>
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public List<SqlRow> disclosureProd(SqlParam<DisclosureRule> params) throws Exception {
        StringBuilder sql = new StringBuilder("select DISTINCT rule.prod_code from idb_disclosure_prod_rule rule where 1=1");
        return super.findRows(sql.toString(),
                DataSourceProperty.IDB,params.getModel());
    }
    /**
    * @功能描述:查询满足该条信披规则中设置的产品参数的渠道id
    * @params:[disclosureRule]
    * @return:java.util.List<com.kayak.core.sql.SqlRow>
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public List<SqlRow> ChannelIDByProdParamsConfig(DisclosureRule disclosureRule) throws Exception {
        StringBuilder sql = new StringBuilder(
                "SELECT group_concat(channelRule.channel_ids) channel_ids " +
                        "FROM (SELECT 1 flag," +
                        "$S{invPrdDime} INV_PRD_DIME,  " +
                        "$S{invPrdLen} INV_PRD_LEN,  " +
                        "$S{prodObj} PROD_OBJ,  " +
                        "$S{prodClcMth} PROD_CLC_MTH,  " +
                        "$S{prodForm} PROD_FORM,  " +
                        "$S{prodSerCd} PROD_SER_CD,   " +
                        "$S{prodInvTyp}  prod_inv_typ   " +
                        "FROM DUAL ) prodParams " +
                        "LEFT JOIN idb_disclosure_channel_rule channelRule " +
                        "ON 1=1  " +
                        " AND (find_in_set(channelRule.INV_PRD_DIME,prodParams.INV_PRD_DIME) OR channelRule.INV_PRD_DIME IS NULL OR channelRule.INV_PRD_DIME ='') " +
                        " AND (find_in_set(channelRule.INV_PRD_LEN,prodParams.INV_PRD_LEN) OR channelRule.INV_PRD_LEN IS NULL OR channelRule.INV_PRD_LEN ='') " +
                        " AND (find_in_set(channelRule.PROD_OBJ,prodParams.PROD_OBJ) OR channelRule.PROD_OBJ IS NULL OR channelRule.PROD_OBJ ='') " +
                        " AND (find_in_set(channelRule.PROD_CLC_MTH,prodParams.PROD_CLC_MTH) OR channelRule.PROD_CLC_MTH IS NULL OR channelRule.PROD_CLC_MTH ='') " +
                        " AND (find_in_set(channelRule.PROD_FORM,prodParams.PROD_FORM) OR channelRule.PROD_FORM IS NULL OR channelRule.PROD_FORM ='') " +
                        " AND (find_in_set(channelRule.PROD_SER_CD,prodParams.PROD_SER_CD) OR channelRule.PROD_SER_CD IS NULL OR channelRule.PROD_SER_CD ='') " +
                        " AND (find_in_set(channelRule.prod_inv_typ,prodParams.prod_inv_typ) OR channelRule.prod_inv_typ IS NULL OR channelRule.prod_inv_typ ='')" +
                        " WHERE 1=1 AND channelRule.disclosure_type =$S{disclosureType} AND channelRule.status = '"+XpStatus.start.getItemKey()+"' ");
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(disclosureRule.getDisclosureSonType())) {//信披子类型
            sql.append(" AND channelRule.disclosure_son_type =$S{disclosureSonType} ");
        }
        sql.append(" group by prodParams.flag");
        return super.findRows(sql.toString(),
                DataSourceProperty.IDB,disclosureRule);
    }
    /**
    * @功能描述:查询满足该条信披规则中设置的产品参数的信披生成规则
    * @params:[disclosureRule]
    * @return:java.util.List<com.kayak.core.sql.SqlRow>
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public List<SqlRow> disclosureIDByProdParamsConfig(DisclosureRule disclosureRule) throws Exception {
        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT  " +
                        "disRule.disclosure_type, " +
                        "disRule.disclosure_son_type  " +
                        "FROM " +
                        "idb_disclosure_rule disRule  " +
                        "WHERE disRule.status = '1' AND disRule.start_rule = '1' ");
        return super.findRows(sql.toString(),
                DataSourceProperty.IDB,disclosureRule);
    }
    /**
    * @功能描述:查询满足该条信披规则中设置的产品参数的信披生成规则
    * @params:[disclosureRule]
    * @return:java.util.List<com.kayak.core.sql.SqlRow>
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public List<SqlRow> disclosureIDByDisclosureType(DisclosureRule disclosureRule) throws Exception {
        StringBuilder sql = new StringBuilder(
                "SELECT disRule.id " +
                        "FROM (SELECT " +
                        "$S{invPrdDime} INV_PRD_DIME," +
                        "$S{invPrdLen} INV_PRD_LEN," +
                        "$S{prodObj} PROD_OBJ," +
                        "$S{prodClcMth} PROD_CLC_MTH," +
                        "$S{prodForm} PROD_FORM," +
                        "$S{prodSerCd} PROD_SER_CD, " +
                        "$S{prodInvTyp}  prod_inv_typ, " +
                        "$S{motherFundFlag}  mother_fund_flag " +
                        "FROM DUAL ) prodParams " +
                        "LEFT JOIN idb_disclosure_rule disRule " +
                        "ON 1=1  "+
                        " AND (find_in_set(disRule.INV_PRD_DIME,prodParams.INV_PRD_DIME) OR disRule.INV_PRD_DIME is null OR disRule.INV_PRD_DIME ='') "+
                        " AND (find_in_set(disRule.INV_PRD_LEN,prodParams.INV_PRD_LEN) OR disRule.INV_PRD_LEN is null OR disRule.INV_PRD_LEN='') "+
                        " AND (find_in_set(disRule.PROD_OBJ,prodParams.PROD_OBJ) OR disRule.PROD_OBJ is null OR disRule.PROD_OBJ='') "+
                        " AND (find_in_set(disRule.PROD_CLC_MTH,prodParams.PROD_CLC_MTH) OR disRule.PROD_CLC_MTH is null OR disRule.PROD_CLC_MTH='') "+
                        " AND (find_in_set(disRule.PROD_FORM,prodParams.PROD_FORM) OR disRule.PROD_FORM is null OR disRule.PROD_FORM='') "+
                        " AND (find_in_set(disRule.PROD_SER_CD,prodParams.PROD_SER_CD) OR disRule.PROD_SER_CD is null OR disRule.PROD_SER_CD='') "+
                        " AND (find_in_set(disRule.prod_inv_typ,prodParams.prod_inv_typ) OR disRule.prod_inv_typ is null OR disRule.prod_inv_typ='')" +
                        " AND (find_in_set(disRule.mother_fund_flag,prodParams.mother_fund_flag) OR disRule.mother_fund_flag is null OR disRule.mother_fund_flag='')" +
                        " WHERE 1=1 AND disRule.disclosure_type =$S{disclosureType} AND disRule.status = '"+XpStatus.start.getItemKey()+"'  " +
                        " AND disRule.start_rule = '"+ TaskStart.auto.getItemKey()+"' ");
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(disclosureRule.getDisclosureSonType())) {//信披子类型
            sql.append(" AND disRule.disclosure_son_type =$S{disclosureSonType} ");
        }
        return super.findRows(sql.toString(),
                DataSourceProperty.IDB,disclosureRule);
    }
    /**
    * @功能描述:规则信息匹配产品参数属性数量
    * @params:[disclosureRule]
    * @return:java.lang.Integer
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public Integer cumCount(DisclosureRule disclosureRule) throws Exception {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) `count` FROM ( " +
                        "SELECT disRule.INV_PRD_DIME AS gruop FROM (SELECT INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_CLC_MTH,PROD_FORM,PROD_SER_CD,prod_inv_typ FROM idb_disclosure_rule WHERE id = $S{id}) disRule " +
                        "UNION  " +
                        "SELECT disRule.INV_PRD_LEN AS gruop FROM (SELECT INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_CLC_MTH,PROD_FORM,PROD_SER_CD,prod_inv_typ FROM idb_disclosure_rule WHERE id = $S{id}) disRule " +
                        "UNION " +
                        "SELECT disRule.PROD_OBJ AS gruop  FROM (SELECT INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_CLC_MTH,PROD_FORM,PROD_SER_CD,prod_inv_typ FROM idb_disclosure_rule WHERE id = $S{id}) disRule " +
                        "UNION " +
                        "SELECT disRule.PROD_CLC_MTH AS gruop  FROM (SELECT INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_CLC_MTH,PROD_FORM,PROD_SER_CD,prod_inv_typ FROM idb_disclosure_rule WHERE id = $S{id}) disRule " +
                        "UNION " +
                        "SELECT disRule.PROD_FORM AS gruop  FROM (SELECT INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_CLC_MTH,PROD_FORM,PROD_SER_CD,prod_inv_typ FROM idb_disclosure_rule WHERE id = $S{id}) disRule " +
                        "UNION " +
                        "SELECT disRule.PROD_SER_CD AS gruop FROM (SELECT INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_CLC_MTH,PROD_FORM,PROD_SER_CD,prod_inv_typ FROM idb_disclosure_rule WHERE id = $S{id}) disRule " +
                        "UNION " +
                        "SELECT disRule.prod_inv_typ AS gruop FROM (SELECT INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_CLC_MTH,PROD_FORM,PROD_SER_CD,prod_inv_typ FROM idb_disclosure_rule WHERE id = $S{id}) disRule " +
                        "UNION " +
                        "SELECT disRule.mother_fund_flag AS gruop FROM (SELECT INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_CLC_MTH,PROD_FORM,PROD_SER_CD,prod_inv_typ,mother_fund_flag FROM idb_disclosure_rule WHERE id = $S{id}) disRule) " +
                        "disRule WHERE disRule.gruop  IS NOT NULL AND  disRule.gruop <> ''");
        return super.findRow(sql.toString(),
                DataSourceProperty.IDB,disclosureRule).getInteger("count");
    }
    public List<DisclosureRule> findRuleParams(String id) throws Exception {
        StringBuilder sql = new StringBuilder(
                "SELECT INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_CLC_MTH,PROD_FORM,PROD_SER_CD,prod_inv_typ,mother_fund_flag FROM idb_disclosure_rule WHERE id = $S{id}");
        return super.findRows(DisclosureRule.class,sql.toString(),
                DataSourceProperty.IDB,id);
    }
    public List<DisclosureChannelRule> findChannelParams(String id) throws Exception {
        StringBuilder sql = new StringBuilder(
                "SELECT INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_CLC_MTH,PROD_FORM,PROD_SER_CD,PROD_INV_TYP FROM idb_disclosure_channel_rule WHERE id = $S{id}");
        return super.findRows(DisclosureChannelRule.class,sql.toString(),
                DataSourceProperty.IDB,id);
    }
    /**
    * @功能描述:渠道信息匹配产品参数属性数量
    * @params:[disclosureRule]
    * @return:java.lang.Integer
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public Integer channelCount(DisclosureRule disclosureRule) throws Exception {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) `count` FROM ( " +
                        "SELECT channelRule.INV_PRD_DIME AS gruop FROM (SELECT INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_CLC_MTH,PROD_FORM,PROD_SER_CD,prod_inv_typ FROM idb_disclosure_channel_rule WHERE id = $S{channelRuleId}) channelRule " +
                        "UNION  " +
                        "SELECT channelRule.INV_PRD_LEN AS gruop FROM (SELECT INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_CLC_MTH,PROD_FORM,PROD_SER_CD,prod_inv_typ FROM idb_disclosure_channel_rule WHERE id = $S{channelRuleId}) channelRule " +
                        "UNION " +
                        "SELECT channelRule.PROD_OBJ AS gruop  FROM (SELECT INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_CLC_MTH,PROD_FORM,PROD_SER_CD,prod_inv_typ FROM idb_disclosure_channel_rule WHERE id = $S{channelRuleId}) channelRule " +
                        "UNION " +
                        "SELECT channelRule.PROD_CLC_MTH AS gruop  FROM (SELECT INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_CLC_MTH,PROD_FORM,PROD_SER_CD,prod_inv_typ FROM idb_disclosure_channel_rule WHERE id = $S{channelRuleId}) channelRule " +
                        "UNION " +
                        "SELECT channelRule.PROD_FORM AS gruop  FROM (SELECT INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_CLC_MTH,PROD_FORM,PROD_SER_CD,prod_inv_typ FROM idb_disclosure_channel_rule WHERE id = $S{channelRuleId}) channelRule " +
                        "UNION " +
                        "SELECT channelRule.PROD_SER_CD AS gruop FROM (SELECT INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_CLC_MTH,PROD_FORM,PROD_SER_CD,prod_inv_typ FROM idb_disclosure_channel_rule WHERE id = $S{channelRuleId}) channelRule " +
                        "UNION " +
                        "SELECT channelRule.prod_inv_typ AS gruop FROM (SELECT INV_PRD_DIME,INV_PRD_LEN,PROD_OBJ,PROD_CLC_MTH,PROD_FORM,PROD_SER_CD,prod_inv_typ FROM idb_disclosure_channel_rule WHERE id = $S{channelRuleId}) channelRule) " +
                        "channelRule WHERE channelRule.gruop  IS NOT NULL AND  channelRule.gruop <> ''");
        return super.findRow(sql.toString(),
                DataSourceProperty.IDB,disclosureRule).getInteger("count");
    }
    /**
    * @功能描述:查询当前存在产品信披表里的产品
    * @params:[params]
    * @return:java.util.List<com.kayak.core.sql.SqlRow>
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public List<SqlRow> prodIDByProdRule(SqlParam<DisclosureRule> params) throws Exception {
        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT  " +
                        "prod.PROD_CD prod_code " +
                        "FROM " +
                        "idb_disclosure_prod_rule  prodRule " +
                        "LEFT JOIN  APP_PRD_BAS_INF prod  " +
                        "ON prodRule.prod_code = prod.PROD_CD " +
                        "WHERE 1 = 1  "
        );
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(params.getModel().getDisclosureType())) {//信披类型
            sql.append(" AND prodRule.disclosure_type =$S{disclosureType} ");
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(params.getModel().getDisclosureSonType())) {//信披子类型
            sql.append(" AND prodRule.disclosure_son_type =$S{disclosureSonType} ");
        }

        return super.findRows(sql.toString(),
                DataSourceProperty.IDB,params.getModel());
    }
    

}
