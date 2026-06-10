package com.kayak.pms.basePublish.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.pms.basePublish.model.DisclosureChannelRule;
import com.kayak.pms.global.constants.IsDocking;
import com.kayak.pms.global.constants.XpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

/**
 * com.kayak.pms.basePublish.dao
 * user:rennannan
 * date:2021/5/11 15:55
 * function:
 */
@Repository
public class DisclosureChannelRuleDao extends ComnDao {


    /**
     * 查询文件后缀名
     * @param param
     * @return
     * @throws Exception
     */
    public SqlResult<DisclosureChannelRule> findSuffixFileName(SqlParam<DisclosureChannelRule> param) throws Exception {
        String sql = "SELECT suffix_file_name as id,suffix_file_name FROM idb_disclosure_channel_rule WHERE id=$S{id}";

        return super.findRows(sql,
                DataSourceProperty.IDB, param);
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
    public SqlResult<DisclosureChannelRule> findChannelRule(SqlParam<DisclosureChannelRule> param) throws Exception {
        String sql =
                "SELECT\n" +
                        "\t\tr.id,\n" +
                        "\t\tr.channel_rule_name,\n" +
                        "\t\tr.channel_rule_desc,\n" +
                        "\t\tr.channel_ids,\n" +
                        "\t\tchannel.channel_name,\n" +
                        "\t\tr.crt_date,\n" +
                        " r.disclosure_type, r.disclosure_son_type, \t\tr.crt_time,\n" +
                        "\t\tr.crt_user_id,\n" +
                        "\t\tr.crt_user_name,\n" +
                        "\t\tr.upd_date,\n" +
                        "\t\tr.upd_time,\n" +
                        "\t\tr.upd_user_id,\n" +
                        "\t\tr.upd_user_name,\n" +
                        "\t\tr.remark,\n" +
                        "\t\tr.STATUS,\n" +
                        "\t\tr.PROD_CLC_MTH,\n" +
                        "\t\tr.PROD_SER_CD, \n" +
                        "\t\tprod.PROD_SER_NM PROD_SER_NM, \n" +
                        "\t\tr.PROD_FORM, \n" +
                        "\t\tr.INV_PRD_DIME, \n" +
                        "\t\tr.INV_PRD_LEN, \n" +
                        "\t\tr.PROD_OBJ, \n" +
                        "\t\tr.prod_inv_typ, \n" +
                        "\t\tr.upload_file_type, \n" +
                        "\t\tr.upload_file_name_type, \n" +
                        "\t\tr.suffix_file_name \n" +
                        "\tFROM\n" +
                        "\tidb_disclosure_channel_rule r  LEFT JOIN (SELECT DISTINCT k.PROD_SER_CD,k.PROD_SER_NM FROM APP_PRD_BAS_INF k) prod ON r.PROD_SER_CD = prod.PROD_SER_CD\n" +
                        "\tleft join  idb_disclosure_channel channel on channel.id = r.channel_ids WHERE 1=1  ";

        if (StringUtils.isNotEmpty(param.getModel().getChannelRuleName())) {//渠道规则名称
            sql += " AND r.channel_rule_name like '%$U{channelRuleName}%' ";
        }
        if (StringUtils.isNotEmpty(param.getModel().getChannelName())) {//渠道名称
            sql += " AND channel.channel_name like '%$U{channelName}%' ";
        }
        if (StringUtils.isNotEmpty(param.getModel().getStatus())) {//状态
            sql += " AND r.status=$S{status} ";
        }
        if (StringUtils.isNotEmpty(param.getModel().getDisclosureType())) {//信披类型
            sql += " AND r.disclosure_type=$S{disclosureType}  ";
        }
        if (StringUtils.isNotEmpty(param.getModel().getDisclosureSonType())) {//信披子类型
            sql += " AND r.disclosure_son_type=$S{disclosureSonType} ";
        }
        sql+= " ORDER BY r.id DESC ";
        return super.findRows(sql,
                DataSourceProperty.IDB, param);
    }
    /**
    * @功能描述:查询是否已经存在同名渠道数据
    * @params:[param]
    * @return:java.lang.Integer
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public Integer findExistsByRuleName(SqlParam<DisclosureChannelRule> param) throws Exception {
        String sql = "select count(*) count from idb_disclosure_channel_rule where  1=1 ";
        if (StringUtils.isNotEmpty(param.getModel().getChannelRuleName())) {//渠道名称
            sql += " and channel_rule_name =$S{channelRuleName}";
        }
        return super.findRow(sql,
                DataSourceProperty.IDB,param.getModel()).getInteger("count");
    }
    /**
    * @功能描述:更新时排除当前渠道配置，查询是否已经存在同名渠道数据
    * @params:[param]
    * @return:java.lang.Integer
    * @Athor:ouyifan
    * @date:2022/6/20
    */
    public Integer findExistsByRuleNameForUpdate(SqlParam<DisclosureChannelRule> param) throws Exception {
        String sql = "select count(*) count from idb_disclosure_channel_rule where  1=1 and id not in ($S{id})";
        if (StringUtils.isNotEmpty(param.getModel().getChannelRuleName())) {//渠道名称
            sql += " and channel_rule_name =$S{channelRuleName}";
        }
        return super.findRow(sql,
                DataSourceProperty.IDB,param.getModel()).getInteger("count");
    }

    /**
     * 功能：新增信披渠道规则信息
     * 作者：rennannan
     * 日期：20210511
     *
     * @param channelRule
     * @return
     */
    public int insertChannelRule(DisclosureChannelRule channelRule) throws Exception {
        String sql = "insert into idb_disclosure_channel_rule(id,channel_rule_name,channel_rule_desc," +
                "channel_ids,crt_date,crt_time,crt_user_id,crt_user_name," +
                "remark,status,PROD_CLC_MTH,disclosure_type,disclosure_son_type,PROD_SER_CD,PROD_FORM,INV_PRD_DIME," +
                "INV_PRD_LEN,PROD_OBJ,PROD_INV_TYP,upload_file_type,upload_file_name_type,suffix_file_name)" +
                "values($AUTOIDS{idb_disclosure_channel_rule},$S{channelRuleName},$S{channelRuleDesc}," +
                "$S{channelIds},$S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName}," +
                "$S{remark},$S{status},$S{prodClcMth},$S{disclosureType},$S{disclosureSonType},$S{prodSerCd},$S{prodForm},$S{invPrdDime}," +
                "$S{invPrdLen},$S{prodObj},$S{prodInvTyp},$S{uploadFileType},$S{uploadFileNameType},$S{suffixFileName})";
        return super.update(sql,
                DataSourceProperty.IDB,channelRule).getEffect();
    }

    /**
     * 功能：根据id修改信披渠道规则信息
     * 作者：rennannan
     * 日期：20210511
     *
     * @param channelRule
     * @return
     * @throws Exception
     */
    public int updateChannelRule(DisclosureChannelRule channelRule) throws Exception {
        String sql = "update idb_disclosure_channel_rule\n" +
                "\tset channel_rule_name=$S{channelRuleName}," +
                "channel_ids=$S{channelIds}," +
                "disclosure_type=$S{disclosureType}," +
                "disclosure_son_type=$S{disclosureSonType}," +
                "remark=$S{remark}," +
                "status=$S{status},"+
                "PROD_CLC_MTH=$S{prodClcMth},"+
                "upd_date=$S{updDate}," +
                "upd_time=$S{updTime}," +
                "upd_user_id=$S{updUserId}," +
                "upd_user_name=$S{updUserName}," +
                "PROD_SER_CD=$S{prodSerCd}," +
                "PROD_FORM=$S{prodForm}," +
                "INV_PRD_DIME=$S{invPrdDime}," +
                "INV_PRD_LEN=$S{invPrdLen}," +
                "PROD_OBJ=$S{prodObj},prod_inv_typ=$S{prodInvTyp},\n" +
                "upload_file_name_type=$S{uploadFileNameType}," +
                "upload_file_type=$S{uploadFileType},\n" +
                "suffix_file_name=$S{suffixFileName}\n" +
                "\twhere id=$S{id}";
        return super.update(sql,
                DataSourceProperty.IDB,channelRule).getEffect();
    }

    /**
     * 功能：修改渠道规则状态
     * 作者：rennannan
     * 日期：20210512
     *
     * @param channelRule
     * @return
     * @throws Exception
     */
    public int updateChannelStatus(DisclosureChannelRule channelRule) throws Exception {
        String sql = "update idb_disclosure_channel_rule\n" +
                "\tset status=$S{status}\n" +
                "\twhere id=$S{id}";
        return super.update(sql,
                DataSourceProperty.IDB,channelRule).getEffect();
    }

    /**
     * 功能：删除信披渠道规则信息
     * 作者：rennannan
     * 日期：20210511
     *
     * @param channelRule
     * @return
     * @throws Exception
     */
    public int deleteChannelRule(DisclosureChannelRule channelRule) throws Exception {
        String sql = "delete from idb_disclosure_channel_rule where id=$S{id}";
        return super.update(sql,
                DataSourceProperty.IDB,channelRule).getEffect();
    }

    public SqlRow findDisclosureRule(DisclosureChannelRule channelRule) throws Exception {

        StringBuffer sql = new StringBuffer(" SELECT channel_ids from idb_disclosure_channel_rule where 1=1" +
                " and disclosure_type =  $S{disclosureType} and disclosure_son_type = $S{disclosureSonType} ");
        if(StringUtils.isNotBlank(channelRule.getProdClcMth())) {
            sql.append(" and PROD_CLC_MTH = $S{prodClcMth}");
        }

        return super.findRow(sql.toString(),
                DataSourceProperty.IDB,channelRule);
    }

    public SqlRow selectDisclosureTypeHasChannels(DisclosureChannelRule model) throws Exception {
        String sql = "SELECT COUNT(*) con FROM (SELECT * FROM idb_disclosure_channel_rule WHERE 1=1 AND disclosure_type = $S{disclosureType}\n" +
                "AND PROD_FORM = $S{prodForm}\n" +
                "AND prod_inv_typ = $S{prodInvTyp}\n" +
                "AND INV_PRD_DIME = $S{invPrdDime}\n" +
                "AND INV_PRD_LEN = $S{invPrdLen}\n" +
                "AND PROD_OBJ = $S{prodObj}\n" +
                "AND PROD_CLC_MTH = $S{prodClcMth}\n" +
                "AND PROD_SER_CD = $S{prodSerCd}\n";
        if (Strings.isNotBlank(model.getDisclosureSonType())) {
            sql += " AND disclosure_son_type = $S{disclosureSonType}\n";
        }
        if (Strings.isNotBlank(model.getId())) {
            sql += " AND id not in ($S{id})\n";
        }
        sql +=") rule WHERE 1=1 AND FIND_IN_SET($S{channelIds},rule.channel_ids) ";
        return super.findRow(sql,
                DataSourceProperty.IDB,model);
    }

    public Integer findIsStoping(DisclosureChannelRule channelIds) throws Exception {
        String sql = "select count(*) count from idb_disclosure_channel_rule  where channel_ids in ("+channelIds.getChannelIds()+") and status ='"+ XpStatus.start.getItemKey() +"' AND disclosure_type = $S{disclosureType}";
        if (Strings.isNotBlank(channelIds.getDisclosureSonType())) {
            sql += " AND disclosure_son_type = $S{disclosureSonType}";
        }
        return super.findRow( sql,
                DataSourceProperty.IDB, channelIds).getInteger("count");
    }
}
