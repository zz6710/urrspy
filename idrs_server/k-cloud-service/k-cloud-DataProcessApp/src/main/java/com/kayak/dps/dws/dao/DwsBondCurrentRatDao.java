package com.kayak.dps.dws.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.dps.check.constants.ErrorCollectionConstants;
import com.kayak.dps.check.util.PrimaryDataCheckUtil;
import org.springframework.boot.SpringBootVersion;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.core.SpringVersion;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class DwsBondCurrentRatDao extends ComnDao {

    @Resource(name = "comnDao")
    private ComnDao comnDao;

    /**
     * 获取当前处理日期有持仓的债券的评级的债券标识
     * @return
     * @throws Exception
     */
    public List<String> getBondScrIdList(String deal_date) throws Exception {
        List<String> bondScrIdList = new ArrayList<>();

        String sqlStr = "select distinct pd.SCR_ID from dwd_ast_prd_ast_lbl_pos_dtl pd where pd.BRED_CD = '4' and pd.POS_DT = '" + deal_date + "'";
        List<SqlRow> rowResList = comnDao.findRows(sqlStr, DataSourceProperty.PUB);
        for (SqlRow bondList : rowResList) {
            bondScrIdList.add(bondList.getString("SCR_ID"));
        }
        return bondScrIdList;
    }

    /**
     * 根据债券标识获取债券最新债券评级
     * 债项评级为空时，取主体评级
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Map<String, Object> getBondCurrentBondRat (Map<String, Object> params) throws Exception {
        String sqlStr = "select ifnull(sdi.itemkey,'') as BND_CRD_RAT/*债券评级*/, ifnull(cr.rat_org_cd, '') as BND_RAT_ORG_CD/*债券评级机构代码*/, " +
                "       ifnull(cr.rat_typ,'') as BND_RAT_TYP/*债券评级类型*/, ifnull(cr.rat_dt, '') as BND_RAT_DT/*债项评级日期*/ " +
                "  from DWD_AST_BND_CRD_RAT cr " +
                "  left join sys_dict_item sdi on sdi.itemkey = cr.CRD_RAT and sdi.dict = 'mainRating' " +
                " where RAT_ORG_CD in ('2','4','5','6','7','13','14','19') " +
                "   and scr_id = $S{scr_id} " +
                "   and sdi.itemkey is not null " +
                "   and RAT_DT = (select max(RAT_DT) from DWD_AST_BND_CRD_RAT where scr_id = $S{scr_id} and RAT_ORG_CD in ('2','4','5','6','7','13','14','19')) " +
                " order by sdi.itemkey desc " +
                "  limit 1";
        SqlRow sqlRow = comnDao.findRow(sqlStr, DataSourceProperty.PUB, params);
        if (sqlRow == null ){
            //债项评级为空时，取主体评级
            params.put("BND_CRD_RAT", params.get("ISU_CRD_RAT"));
            params.put("BND_RAT_ORG_CD", params.get("ISU_RAT_ORG_CD"));
            params.put("BND_RAT_TYP", "1");
            params.put("BND_RAT_DT", params.get("ISU_RAT_DT"));
        } else {
            params.put("BND_CRD_RAT", sqlRow.getString("BND_CRD_RAT"));
            params.put("BND_RAT_ORG_CD", sqlRow.getString("BND_RAT_ORG_CD"));
            params.put("BND_RAT_TYP", sqlRow.getString("BND_RAT_TYP"));
            params.put("BND_RAT_DT", sqlRow.getString("BND_RAT_DT"));
        }
        return params;
    }

    /**
     * 根据债券标识获取债券最新的主体评级
     * @param params
     * @return
     * @throws Exception
     */
    public Map<String, Object> getBondCurrentIssuerRat (Map<String, Object> params) throws Exception {
        String sqlStr = "select ifnull(b.itemval, '') as ISU_CRD_RAT/*主体评级*/, ifnull(bm.rat_dt, '') as ISU_RAT_DT/*主体评级日期*/, " +
                "       ifnull(bm.rat_typ, '') as ISU_RAT_TYP/*主体评级类型*/, ifnull(bm.rat_org_cd, '') as ISU_RAT_ORG_CD/*主体评级机构代码*/ " +
                "  from DWD_AST_BND_BAS_INF bb /*债券信息*/ " +
                "  left join DWD_AST_BND_MAIN_RAT bm/*债券主体评级*/ on bm.CMP_NM = bb.ISU " +
                "  left join sys_dict_item b on b.dict = 't8_wind_bondlevel' and b.itemval = bm.CRD_RAT  " +
                " where bm.RAT_DT = (select max(RAT_DT) from DWD_AST_BND_MAIN_RAT where rat_org_cd in ('2','4','5','6','7','13','14','19') and CMP_NM = (select ISU from DWD_AST_BND_BAS_INF where SCR_ID = $S{scr_id})) " +
                "   and bb.SCR_ID = $S{scr_id} " +
                "   and b.itemkey is not null " +
                " limit 1" ;
        SqlRow sqlRow = comnDao.findRow(sqlStr, DataSourceProperty.PUB, params);
        if (sqlRow == null) {
            params.put("ISU_CRD_RAT", "");
            params.put("ISU_RAT_DT", "");
            params.put("ISU_RAT_TYP", "");
            params.put("ISU_RAT_ORG_CD", "");
        } else {
            params.put("ISU_CRD_RAT", sqlRow.getString("ISU_CRD_RAT"));
            params.put("ISU_RAT_DT", sqlRow.getString("ISU_RAT_DT"));
            params.put("ISU_RAT_TYP", sqlRow.getString("ISU_RAT_TYP"));
            params.put("ISU_RAT_ORG_CD", sqlRow.getString("ISU_RAT_ORG_CD"));
        }
        return params;
    }

    /**
     * 插入债券最新评级信息
     * @param params
     * @throws Exception
     */
    public void genBondLatestRatInfo(Map<String, Object> params) throws Exception {
        String delSql = "delete from DWS_EVT_BND_CUR_RAT where SCR_ID = $S{scr_id} and SETTLE_DATE = $S{deal_date} ";
        String insert_sqlStr = "insert into DWS_EVT_BND_CUR_RAT (SCR_ID, BND_RAT_DT, BND_RAT_TYP, BND_CRD_RAT, ISU_RAT_DT, ISU_RAT_TYP, ISU_CRD_RAT, " +
                "BND_RAT_ORG_CD, ISU_RAT_ORG_CD, CRT_DT, SETTLE_DATE) values " +
                "($S{scr_id}, $S{BND_RAT_DT}, $S{BND_RAT_TYP}, $S{BND_CRD_RAT}, $S{ISU_RAT_DT}, $S{ISU_RAT_TYP}, $S{ISU_CRD_RAT}," +
                " $S{BND_RAT_ORG_CD}, $S{ISU_RAT_ORG_CD}, date_format(now(),'%Y%m%d'), $S{deal_date})";
        try {
            comnDao.update(delSql, DataSourceProperty.PUB, params);
            comnDao.update(insert_sqlStr, DataSourceProperty.PUB, params);
        } catch (Exception e) {
            PrimaryDataCheckUtil.ErrorInfoRecordHandle(ErrorCollectionConstants.ERROR_DATA_VALIDATE_REPORT, e.getMessage());//base_error_message记录异常报错报错信息
            throw new Exception(e.getMessage());
        }
    }

}
