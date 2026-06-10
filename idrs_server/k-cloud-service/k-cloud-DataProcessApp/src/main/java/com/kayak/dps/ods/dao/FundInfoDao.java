package com.kayak.dps.ods.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.FundInfoModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class FundInfoDao extends ComnDao {

    @Resource(name = "comnDao")
    private ComnDao comnDao;

    /**
     * 新增债券信息
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult insertFundInfo( SqlParam<FundInfoModel> params) throws Exception {
        return comnDao.update("insert into ods_fnd_bas_inf (" +
                "CRT_DATE, " +
                "CRT_TIME, " +
                "CRT_USER, " +
                "DEAL_DATE, " +
                "FIX_ERN_F, " +
                "FND_CMP_NM, " +
                "FND_MNG_ORG_NM, " +
                "FND_TRST_ORG_NM, " +
                "IDT, " +
                "INV_ENTP_TYP_ECN, " +
                "INV_ENTP_TYP_SIZ, " +
                "INV_ENTP_TYP_TCHNO, " +
                "REG_RCD_ORG, " +
                "SCR_CD, " +
                "SCR_ID, " +
                "SCR_NM, " +
                "TRX_MKT, " +
                "CBND_FRS_CTG, " +
                "CBND_SCD_CTG, " +
                "INV_STG, " +
                "BLG_GOV_INV_FND_F, " +
                "GOV_INV_FND_DIR, " +
                "FIN_AST_INV_CMP_ISU_F, " +
                "ISU_LOT, " +
                "SET_UP_DT, " +
                "RUN_MTH, " +
                "FND_PROD_REG_ENC, " +
                "FND_ORG_ENC, " +
                "ASS_INF_CLASS, " +
                "FND_INV_AST, " +
                "GG_CBC_SUB_TYPE, " +
                "GG_CBC_TYPE, " +
                "PBNK_FRS_CTG, " +
                "PBNK_SCD_CTG, " +
                "PBNK_TRD_CTG, " +
                "PBNK_FUR_CTG, " +
                "CMT, " +
                "TRX_PLA,VERSION) " +
                "values ( " +
                "$S{crtDate}, " +
                "$S{crtTime}, " +
                "$S{crtUser}, " +
                "$S{dealDate}, " +
                "$S{fixErnF}, " +
                "$S{fndCmpNm}, " +
                "$S{fndMngOrgNm}, " +
                "$S{fndTrstOrgNm}, " +
                "$S{idt}, " +
                "$S{invEntpTypEcn}, " +
                "$S{invEntpTypSiz}, " +
                "$S{invEntpTypTchno}, " +
                "$S{regRcdOrg}, " +
                "$S{scrCd}, " +
                "$S{scrId}, " +
                "$S{scrNm}, " +
                "$S{trxMkt}, " +
                "$S{cbndFrsCtg}, " +
                "$S{cbndScdCtg}, " +
                "$S{invStg}, " +
                "$S{blgGovInvFndF}, " +
                "$S{govInvFndDir}, " +
                "$S{finAstInvCmpIsuF}, " +
                "nullif($S{isuLot},''), " +
                "$S{setUpDt}, " +
                "$S{runMth}, " +
                "$S{fndProdRegEnc}, " +
                "$S{fndOrgEnc}, " +
                "$S{assInfClass}, " +
                "$S{fndInvAst}, " +
                "$S{ggCbcSubType}, " +
                "$S{ggCbcType}, " +
                "$S{pbnkFrsCtg}, " +
                "$S{pbnkScdCtg}, " +
                "$S{pbnkTrdCtg}, " +
                "$S{pbnkFurCtg}, " +
                "$S{cmt}, " +
                "$S{trxPla},$S{version})",DataSourceProperty.PUB, params.getModel());
    }

    public SqlResult<FundInfoModel> findFundInfo(SqlParam<FundInfoModel> params) throws Exception {
        String sql = "select " +
                "t1.CBND_FRS_CTG, " +
                "t1.CBND_SCD_CTG, " +
                "t1.CRT_DATE, " +
                "t1.CRT_TIME, " +
                "t1.CRT_USER, " +
                "t1.DEAL_DATE, " +
                "t1.FIX_ERN_F, " +
                "t1.FND_CMP_NM, " +
                "t1.FND_MNG_ORG_NM, " +
                "t1.FND_TRST_ORG_NM, " +
                "t1.IDT, " +
                "t1.INV_ENTP_TYP_ECN, " +
                "t1.INV_ENTP_TYP_SIZ, " +
                "t1.INV_ENTP_TYP_TCHNO, " +
                "t1.REG_RCD_ORG, " +
                "t1.SCR_CD, " +
                "t1.SCR_ID, " +
                "t1.SCR_NM, " +
                "t1.TRX_MKT, " +
                "t1.TRX_PLA, " +
                "t1.UPD_DATE, " +
                "t1.UPD_TIME, " +
                "t1.UPD_USER, " +
                "t1.ASS_INF_CLASS, " +
                "t1.BLG_GOV_INV_FND_F, " +
                "t1.CMT, " +
                "t1.FIN_AST_INV_CMP_ISU_F, " +
                "t1.FND_INV_AST, " +
                "t1.FND_ORG_ENC, " +
                "t1.FND_PROD_REG_ENC, " +
                "t1.GG_CBC_SUB_TYPE, " +
                "t1.GG_CBC_TYPE, " +
                "t1.GOV_INV_FND_DIR, " +
                "t1.INV_STG, " +
                "t1.ISU_LOT, " +
                "t1.PBNK_FRS_CTG, " +
                "t1.PBNK_FUR_CTG, " +
                "t1.PBNK_SCD_CTG, " +
                "t1.PBNK_TRD_CTG, " +
                "t1.RUN_MTH, " +
                "t1.SET_UP_DT, " +
                "t1.VERSION " +
                "from ods_fnd_bas_inf t1  where 1=1 ";
        if (StringUtils.isNotBlank(params.getModel().getScrCd())){
            sql += "and t1.scr_cd = $S{scrCd} ";
        }
        if (StringUtils.isNotBlank(params.getModel().getCbndScdCtg())){
            sql += "and t1.CBND_SCD_CTG = $S{cbndScdCtg} ";
        }
        if (StringUtils.isNotBlank(params.getModel().getTrxMkt())){
            sql += "and t1.TRX_MKT = $S{trxMkt} ";
        }
        return super.findRows(sql,DataSourceProperty.PUB, params);
    }

    public UpdateResult updateFundInfo(SqlParam<FundInfoModel> params) throws Exception {
        return super.update("update ods_fnd_bas_inf set " +
                "FIX_ERN_F= $S{fixErnF}, " +
                "FND_CMP_NM= $S{fndCmpNm}, " +
                "FND_MNG_ORG_NM= $S{fndMngOrgNm}, " +
                "FND_TRST_ORG_NM= $S{fndTrstOrgNm}, " +
                "IDT= $S{idt}, " +
                "INV_ENTP_TYP_ECN= $S{invEntpTypEcn}, " +
                "INV_ENTP_TYP_SIZ= $S{invEntpTypSiz}, " +
                "INV_ENTP_TYP_TCHNO= $S{invEntpTypTchno}, " +
                "REG_RCD_ORG= $S{regRcdOrg}, " +
                "SCR_NM= $S{scrNm}, " +
                "TRX_MKT= $S{trxMkt}, " +
                "TRX_PLA= $S{trxPla}, " +
                "RUN_MTH= $S{runMth}, " +
                "ASS_INF_CLASS=$S{assInfClass}, " +
                "CBND_FRS_CTG= $S{cbndFrsCtg}, " +
                "CBND_SCD_CTG= $S{cbndScdCtg}, " +
                "BLG_GOV_INV_FND_F=$S{blgGovInvFndF}, " +
                "CMT=$S{cmt}, " +
                "FIN_AST_INV_CMP_ISU_F=$S{finAstInvCmpIsuF}, " +
                "FND_INV_AST=$S{fndInvAst}, " +
                "FND_ORG_ENC=$S{fndOrgEnc}, " +
                "SET_UP_DT=$S{setUpDt}, " +
                "GG_CBC_SUB_TYPE=$S{ggCbcSubType}, " +
                "GG_CBC_TYPE=$S{ggCbcType}, " +
                "GOV_INV_FND_DIR=$S{govInvFndDir}, " +
                "INV_STG=$S{invStg}, " +
                "ISU_LOT=nullif($S{isuLot},''), " +
                "PBNK_FRS_CTG=$S{pbnkFrsCtg}, " +
                "PBNK_FUR_CTG=$S{pbnkFurCtg}, " +
                "PBNK_SCD_CTG=$S{pbnkScdCtg}, " +
                "PBNK_TRD_CTG=$S{pbnkTrdCtg}, " +
                "UPD_DATE= $S{updDate}, " +
                "UPD_TIME= $S{updTime}, " +
                "UPD_USER= $S{updUser} " +
                "where SCR_ID= $S{scrId}", DataSourceProperty.PUB, params.getModel());
    }
    public UpdateResult updateFundInfoBl(SqlParam<FundInfoModel> params) throws Exception {
        return super.update("update ods_fnd_bas_inf set " +
                "FND_PROD_REG_ENC=$S{fndProdRegEnc}, " +
                "UPD_DATE=$S{updDate}, " +
                "UPD_TIME=$S{updTime}, " +
                "UPD_USER=$S{updUser}, " +
                "VERSION=$S{version} " +
                "where SCR_ID=$S{scrId}", DataSourceProperty.PUB, params.getModel());
    }

    public UpdateResult deleteFundInfo(SqlParam<FundInfoModel> params) throws Exception {
        return super.update("delete from ods_fnd_bas_inf where SCR_ID=$S{scrId}",DataSourceProperty.PUB, params.getModel());
    }

    public SqlRow existSameFund(SqlParam<FundInfoModel> params) throws Exception {
        return super.findRow("select count(1) con from ods_ast_fnd_bas_inf where SCR_ID=$S{scrId}",DataSourceProperty.PUB, params.getModel());
    }

    public List<SqlRow> getXPTypeByDocType(String doc_type) throws Exception {
        return super.findRows("SELECT itemkey VALUE,  " +
                "itemval TEXT  " +
                "FROM sys_dict_item  " +
                "WHERE dict = 'tr_fund_frs_type' " +
                "AND itemkey LIKE '$U{doc_type}%' " +
                "ORDER BY itemkey+0",DataSourceProperty.PUB,doc_type);
    }


    public  List<SqlRow> findFundTypeAndNmById(String scrId) throws Exception {
        String sql = "SELECT DISTINCT WD_FRS_CTG,SCR_ID, SCR_CD,SCR_NM FROM dwd_ast_fnd_bas_inf where SCR_ID=$S{scrId} ";
        return super.findRows(sql,DataSourceProperty.PUB, scrId);
    }

    public SqlResult<FundInfoModel> findFundInfoCdAndNm(SqlParam<FundInfoModel> params) throws Exception {
        String sql = "SELECT DISTINCT SCR_ID,SCR_CD,SCR_NM FROM ods_fnd_bas_inf where 1=1 AND SCR_CD like '%$U{scrCd}%' or SCR_NM like '%$U{scrCd}%'";
        return super.findRows(sql,DataSourceProperty.PUB, params);
    }

    public SqlResult<FundInfoModel> findFundInfoCdAndNmByScrCd(SqlParam<FundInfoModel> params) throws Exception {
        String sql = "SELECT DISTINCT WD_FRS_CTG,SCR_ID,SCR_CD,SCR_NM FROM ods_ast_fnd_bas_inf where 1=1 ";
        if (Strings.isNotBlank(params.getModel().getScrCd())) {
            sql += " and SCR_CD  =  $S{scrCd} ";
        }
        return super.findRows(sql,DataSourceProperty.PUB, params);
    }

    public SqlResult<FundInfoModel> findFondInfoModelsCdAndNmByTrxMkt(SqlParam<FundInfoModel> params) throws Exception {
        String sql = "SELECT DISTINCT WD_FRS_CTG,SCR_ID,SCR_CD,SCR_NM FROM ods_ast_fnd_bas_inf where 1=1 and TRX_MKT=$S{trxMkt} ";
        return super.findRows(sql,DataSourceProperty.PUB, params);
    }

    public  List<SqlRow>  getDictByVal(Object params) throws Exception {
        List<SqlRow> s = super.findRows("SELECT itemkey VALUE,  "  +
                "itemval TEXT  "  +
                "FROM sys_dict_item  "  +
                "WHERE dict = $S{dict} " +
                "AND itemorder LIKE $S{key} ORDER BY itemkey+0  ",DataSourceProperty.PUB,params);
        return s;
    }

    public  List<SqlRow>  getUPDTypeByDoc(Object params) throws Exception {
        List<SqlRow> s = super.findRows("SELECT itemkey VALUE,  "  +
                "itemval TEXT  "  +
                "FROM sys_dict_item  "  +
                "WHERE dict = 'cbndScdCtg' " +
                "AND itemorder = '$U{cbndFrsCtg}' " +
                "ORDER BY itemkey+0",DataSourceProperty.PUB,params);
        return s;
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
