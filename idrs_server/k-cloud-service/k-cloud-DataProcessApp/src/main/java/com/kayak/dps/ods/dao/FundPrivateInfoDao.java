package com.kayak.dps.ods.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.FundPrivateInfoModel;
import com.kayak.dps.app.model.T8OrgSheet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class FundPrivateInfoDao extends ComnDao {

    @Resource(name = "comnDao")
    private ComnDao comnDao;
    public SqlResult<FundPrivateInfoModel> findFundPrivateInfo(SqlParam<FundPrivateInfoModel> params) throws Exception {
        String sql = "select " +
                "t1.SCR_ID,"+
                "t1.SCR_CD,"+
                "t1.SCR_NM,"+
                "t1.FIS_CD,"+
                "t1.IS_ISSUE_FAIC,"+
                "t1.INVESTMENT_INDUSTRY,"+
                "t1.FND_RFA,"+
                "t1.IS_FIC,"+
                "t1.IS_GIFND,"+
                "t1.FND_IDG,"+
                "t1.FND_MI,"+
                "t2.ORG_FULL_NAME ORG_FULL_NAME2,"+
                "t1.FND_MI_NAME,"+
                "t3.ORG_FULL_NAME ORG_FULL_NAME3,"+
                "t1.FND_CI_NAME,"+
                "t4.ORG_FULL_NAME ORG_FULL_NAME4,"+
                "t1.LVRG_TYP_SIZ,"+
                "t1.LVRG_TYP_TCHNO,"+
                "t1.LVRG_TYP_ECN,"+
                "t1.FND_IS,"+
                "t1.IS_INV_PEIF,"+
                "t1.FND_TYPE,"+
                "t1.CBND_FRS_CTG,"+
                "t1.CBND_SCD_CTG,"+
                "t1.PBNK_FRS_CTG,"+
                "t1.PBNK_SCD_CTG,"+
                "t1.PBNK_TRD_CTG,"+
                "t1.PBNK_FOU_CTG,"+
                "t1.G06_FRS_CTG,"+
                "t1.G06_SCD_CTG,"+
                "t1.G06_TRD_CTG,"+
                "t1.INV_ASSET,"+
                "t1.CMT,"+
                "t1.CRT_DATE,"+
                "t1.CRT_TIME,"+
                "t1.CRT_USER,"+
                "t1.UPD_DATE,"+
                "t1.UPD_TIME,"+
                "t1.UPD_USER,t1.VERSION "+
                "from ods_amng_fund_ntpinfo t1 " +
                "left join ods_org_info t2 on t1.FND_MI = t2.CSLD_SOC_CRD_CD " +
                "left join ods_org_info t3 on t1.FND_MI_NAME = t3.CSLD_SOC_CRD_CD " +
                "left join ods_org_info t4 on t1.FND_CI_NAME = t4.CSLD_SOC_CRD_CD " +
                " where 1=1 ";
        if (StringUtils.isNotBlank(params.getModel().getScrCd())){
            sql += "and t1.scr_cd = $S{scrCd} ";
        }
        if (StringUtils.isNotBlank(params.getModel().getInvestmentIndustry())){
            sql += "and t1.INVESTMENT_INDUSTRY = $S{investmentIndustry} ";
        }
        return super.findRows(sql,DataSourceProperty.PUB, params);
    }

    /**
     * 新增私募基金信息
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult insertFundPrivateInfo( SqlParam<FundPrivateInfoModel> params) throws Exception {
        return comnDao.update("INSERT INTO ODS_AMNG_FUND_NTPINFO( " +
                "SCR_ID,"+
                "SCR_CD,"+
                "SCR_NM,"+
                "FIS_CD,"+
                "IS_ISSUE_FAIC,"+
                "INVESTMENT_INDUSTRY,"+
                "FND_RFA,"+
                "IS_FIC,"+
                "IS_GIFND,"+
                "FND_IDG,"+
                "FND_MI,"+
                "FND_MI_NAME,"+
                "FND_CI_NAME,"+
                "LVRG_TYP_SIZ,"+
                "LVRG_TYP_TCHNO,"+
                "LVRG_TYP_ECN,"+
                "FND_IS,"+
                "IS_INV_PEIF,"+
                "FND_TYPE,"+
                "CBND_FRS_CTG,"+
                "CBND_SCD_CTG,"+
                "PBNK_FRS_CTG,"+
                "PBNK_SCD_CTG,"+
                "PBNK_TRD_CTG,"+
                "PBNK_FOU_CTG,"+
                "G06_FRS_CTG,"+
                "G06_SCD_CTG,"+
                "G06_TRD_CTG,"+
                "INV_ASSET,"+
                "CMT,"+
                "CRT_DATE,"+
                "CRT_TIME,"+
                "CRT_USER,"+
                "UPD_DATE,"+
                "UPD_TIME,"+
                "UPD_USER)VALUES( " +
                "$S{scrId},"+
                "$S{scrCd},"+
                "$S{scrNm},"+
                "$S{fisCd},"+
                "$S{isIssueFaic},"+
                "$S{investmentIndustry},"+
                "$S{fndRfa},"+
                "$S{isFic},"+
                "$S{isGifnd},"+
                "$S{fndIdg},"+
                "$S{fndMi},"+
                "$S{fndMiName},"+
                "$S{fndCiName},"+
                "$S{lvrgTypSiz},"+
                "$S{lvrgTypTchno},"+
                "$S{lvrgTypEcn},"+
                "$S{fndIs},"+
                "$S{isInvPeif},"+
                "$S{fndType},"+
                "$S{cbndFrsCtg},"+
                "$S{cbndScdCtg},"+
                "$S{pbnkFrsCtg},"+
                "$S{pbnkScdCtg},"+
                "$S{pbnkTrdCtg},"+
                "$S{pbnkFouCtg},"+
                "$S{g06FrsCtg},"+
                "$S{g06ScdCtg},"+
                "$S{g06TrdCtg},"+
                "$S{invAsset},"+
                "$S{cmt},"+
                "$S{crtDate},"+
                "$S{crtTime},"+
                "$S{crtUser},"+
                "$S{updDate},"+
                "$S{updTime},"+
                "$S{updUser})",DataSourceProperty.PUB, params.getModel());
    }

    public UpdateResult updateFundPrivateInfo(SqlParam<FundPrivateInfoModel> params) throws Exception {
        return super.update("update ods_amng_fund_ntpinfo set " +
                "SCR_NM = $S{scrNm},"+
                "FIS_CD = $S{fisCd},"+
                "IS_ISSUE_FAIC = $S{isIssueFaic},"+
                "INVESTMENT_INDUSTRY = $S{investmentIndustry},"+
                "FND_RFA = $S{fndRfa},"+
                "IS_FIC = $S{isFic},"+
                "IS_GIFND = $S{isGifnd},"+
                "FND_IDG = $S{fndIdg},"+
                "FND_MI = $S{fndMi},"+
                "FND_MI_NAME = $S{fndMiName},"+
                "FND_CI_NAME = $S{fndCiName},"+
                "LVRG_TYP_SIZ = $S{lvrgTypSiz},"+
                "LVRG_TYP_TCHNO = $S{lvrgTypTchno},"+
                "LVRG_TYP_ECN = $S{lvrgTypEcn},"+
                "FND_IS = $S{fndIs},"+
                "IS_INV_PEIF = $S{isInvPeif},"+
                "FND_TYPE = $S{fndType},"+
                "CBND_FRS_CTG = $S{cbndFrsCtg},"+
                "CBND_SCD_CTG = $S{cbndScdCtg},"+
                "PBNK_FRS_CTG = $S{pbnkFrsCtg},"+
                "PBNK_SCD_CTG = $S{pbnkScdCtg},"+
                "PBNK_TRD_CTG = $S{pbnkTrdCtg},"+
                "PBNK_FOU_CTG = $S{pbnkFouCtg},"+
                "G06_FRS_CTG = $S{g06FrsCtg},"+
                "G06_SCD_CTG = $S{g06ScdCtg},"+
                "G06_TRD_CTG = $S{g06TrdCtg},"+
                "INV_ASSET = $S{invAsset},"+
                "CMT = $S{cmt},"+
                "UPD_DATE= $S{updDate}, " +
                "UPD_TIME= $S{updTime}, " +
                "UPD_USER= $S{updUser} " +
                "where SCR_ID= $S{scrId}", DataSourceProperty.PUB, params.getModel());
    }

    public UpdateResult updatePrivateInfoBl(SqlParam<FundPrivateInfoModel> params) throws Exception {
        return super.update("update ods_amng_fund_ntpinfo set " +
                "IS_GIFND = $S{isGifnd},"+
                "FND_IDG = $S{fndIdg},"+
                "IS_ISSUE_FAIC = $S{isIssueFaic},"+
                "FND_IS = $S{fndIs},"+
                "INV_ASSET = $S{invAsset},"+
                "CMT = $S{cmt},"+
                "VERSION = $S{version}, " +
                "UPD_DATE= $S{updDate}, " +
                "UPD_TIME= $S{updTime}, " +
                "UPD_USER= $S{updUser} " +
                "where SCR_ID= $S{scrId}", DataSourceProperty.PUB, params.getModel());
    }

    public UpdateResult deleteFundPrivateInfo(SqlParam<FundPrivateInfoModel> params) throws Exception {
        return super.update("delete from ods_amng_fund_ntpinfo where SCR_ID=$S{scrId}",DataSourceProperty.PUB, params.getModel());
    }

    public SqlRow existSameFund(SqlParam<FundPrivateInfoModel> params) throws Exception {
        return super.findRow("select count(1) con from ods_amng_fund_ntpinfo where SCR_CD=$S{scrCd}",DataSourceProperty.PUB, params.getModel());
    }

    public SqlResult<FundPrivateInfoModel> findFundPrivateInfoCdAndNm(SqlParam<FundPrivateInfoModel> params) throws Exception {
        String sql = "SELECT DISTINCT SCR_ID,SCR_CD,SCR_NM FROM ods_amng_fund_ntpinfo where 1=1 AND SCR_CD like '%$U{scrCd}%' or SCR_NM like '%$U{scrCd}%'";
        return super.findRows(sql,DataSourceProperty.PUB, params);
    }

    public SqlResult<FundPrivateInfoModel> findOrgNmAll(SqlParam<FundPrivateInfoModel> params) throws Exception {
        String sql = "SELECT t1.ORG_NBR_EXT  ,t1.ORG_FULL_NAME  FROM ODS_ORG_INFO t1 WHERE 1=1";
        return super.findRows(sql, params);
    }

    public  List<SqlRow>  getUPDTypeByDoc(Object params) throws Exception {
        return super.findRows("SELECT itemkey VALUE,  "  +
                "itemval TEXT  "  +
                "FROM sys_dict_item  "  +
                "WHERE dict = 'cbndScdCtg' " +
                "AND itemorder = '$U{cbndFrsCtg}' " +
                "ORDER BY itemkey+0",DataSourceProperty.PUB,params);
    }

    public  List<SqlRow>  getPbnkScdByPbnkFrs(Object params) throws Exception {
        List<SqlRow> s = super.findRows("select itemkey VALUE,itemval TEXT   FROM sys_dict_item   WHERE dict = 'pbnkScdCtg'  and substr(itemkey,1,1) = $S{pbnkFrsCtg}   ORDER BY itemkey+0 ",DataSourceProperty.PUB,params);
        return s;
    }

    public  List<SqlRow>  getPbnkTrdByPbnkScd(Object params) throws Exception {
        List<SqlRow> s = super.findRows("select itemkey VALUE,itemval TEXT   FROM sys_dict_item   WHERE dict = 'pbnkTrdCtg'  and substr(itemkey,1,2) = $S{pbnkScdCtg}  ORDER BY itemkey+0 ",DataSourceProperty.PUB,params);
        return s;
    }

    public int existFund(String fullScrNm) throws Exception {
        String sql = "select count(1) as count_num from ods_ast_fnd_bas_inf s where s.FIS_CD = '" + fullScrNm + "'";
        int cont = super.findRow(sql, DataSourceProperty.PUB, null).getInteger("count_num");
        return cont;
    }
}
