package com.kayak.dps.ods.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.dps.app.model.BondInfoModel;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class BondInfoDao extends ComnDao {

    @Resource(name = "comnDao")
    private ComnDao comnDao;


    /**
     * 债券基本信息查询
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<BondInfoModel> findBondInfo(SqlParam<BondInfoModel> params) throws Exception {
        String sql = "SELECT t1.SCR_ID,t1.SCR_CD,t1.SCR_SHT_NM,t1.SCR_NM,t1.TRX_MKT,t1.TRX_PLA,t1.ISU_PRC,t1.ACTL_ISU_TOT_AMT, " +
                "       t1.ISU_MTH,t1.GRNT_MTH,t1.IS_CREDIT,t1.EMB_OPT_F,t1.IS_REPAID,t1.VAL_DT,t1.MTU_DT,t1.INTR_MTH, " +
                "       t1.PAY_INTR_FRQ,t1.INTR_BAS,t1.COUPON_TYPE,t1.PAR_RAT,t1.SPRD,t1.ISU,t1.ORIGN_INTEREST_OBJECT, " +
                "       t1.CC_INDUSTRY_ISSUER,t1.ISU_ORG_TYP_TCHNO,t1.ISU_ORG_TYP_ECN,t1.ISU_ORG_TYP_SCALE_SIZ, " +
                "       t1.REG_TRST_ORG,t1.GRNT,t1.ISU_BND_RAT,t1.BOND_FRS_RAT,t1.MAIN_RAT,t1.GRNT_RAT, " +
                "       t1.ASS_INF_CLASS,t1.DEBT_EQUITY_CLASS,t1.IS_SUSTAIN,t1.CRT_DATE,t1.CRT_TIME, " +
                "       t1.CRT_USER,t1.UPD_DATE,t1.UPD_TIME,t1.UPD_USER,t1.DEAL_DATE,t1.CBND_FRS_CTG, " +
                "       t1.CBND_SCD_CTG,t1.SPC_TYPE,t1.GG_CBC_SUB_TYPE,t1.GG_CBC_TYPE,t1.PBNK_FRS_CTG, " +
                "       t1.PBNK_SCD_CTG,t1.PBNK_TRD_CTG,t1.PBNK_FUR_CTG,t1.PBNK_INDUSTRY_ISSUER, " +
                "       t1.ISU_ORG_TYP_SIZ,t1.VERSION,t1.CMT" +
                " FROM ODS_BOND_BAS_INF t1 WHERE 1=1 ";
        if (Strings.isNotBlank(params.getModel().getScrCd())) {
            sql += " AND t1.SCR_CD = $S{scrCd} ";
        }
        if (Strings.isNotBlank(params.getModel().getScrShtNm())) {
            sql += " AND t1.SCR_SHT_NM like '%$U{scrShtNm}%' ";
        }
        if (Strings.isNotBlank(params.getModel().getTrxMkt())) {
            sql += " AND t1.TRX_MKT = $S{trxMkt} ";
        }
        if (Strings.isNotBlank(params.getModel().getCbndScdCtg())) {
            sql += " AND t1.CBND_SCD_CTG = $S{cbndScdCtg} ";
        }
        return super.findRows(sql,DataSourceProperty.PUB, params);
    }

    /**
     * 新增债券基本信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult insertBondInfo( SqlParam<BondInfoModel> params) throws Exception {
        UpdateResult update = comnDao.update("INSERT INTO ODS_BOND_BAS_INF (SCR_ID,SCR_CD,SCR_SHT_NM,SCR_NM," +
                "TRX_MKT,TRX_PLA,ISU_PRC,ACTL_ISU_TOT_AMT,ISU_MTH,GRNT_MTH,IS_CREDIT,EMB_OPT_F,IS_REPAID," +
                "VAL_DT,MTU_DT,INTR_MTH,PAY_INTR_FRQ,INTR_BAS,COUPON_TYPE,PAR_RAT,SPRD,ISU,ORIGN_INTEREST_OBJECT," +
                "CC_INDUSTRY_ISSUER,ISU_ORG_TYP_TCHNO,ISU_ORG_TYP_ECN,ISU_ORG_TYP_SCALE_SIZ,REG_TRST_ORG,GRNT," +
                "ISU_BND_RAT,BOND_FRS_RAT,MAIN_RAT,GRNT_RAT,ASS_INF_CLASS,DEBT_EQUITY_CLASS,IS_SUSTAIN," +
                "CBND_FRS_CTG,CBND_SCD_CTG," +
                "SPC_TYPE,GG_CBC_SUB_TYPE,GG_CBC_TYPE, " +
                "PBNK_FRS_CTG,PBNK_SCD_CTG,PBNK_TRD_CTG,PBNK_FUR_CTG,PBNK_INDUSTRY_ISSUER, " +
                "ISU_ORG_TYP_SIZ,VERSION,CMT," +
                "CRT_DATE,CRT_TIME,CRT_USER,DEAL_DATE) VALUES ($S{scrId},$S{scrCd},$S{scrShtNm}," +
                "$S{scrNm},$S{trxMkt},$S{trxPla},nullif($S{isuPrc},''),nullif($S{actlIsuTotAmt},''),$S{isuMth},$S{grntMth},$S{isCredit}," +
                "$S{embOptF},$S{isRepaid},$S{valDt},$S{mtuDt},$S{intrMth},$S{payIntrFrq},$S{intrBas},$S{couponType}," +
                "nullif($S{parRat},''),nullif($S{sprd},''),$S{isu},$S{orignInterestObject},$S{ccIndustryIssuer},$S{isuOrgTypTchno}," +
                "$S{isuOrgTypEcn},$S{isuOrgTypScaleSiz},$S{regTrstOrg},$S{grnt},$S{isuBndRat},$S{bondFrsRat}," +
                "$S{mainRat},$S{grntRat},$S{assInfClass},$S{debtEquityClass},$S{isSustain},$S{cbndFrsCtg}," +
                "$S{cbndScdCtg},$S{spcType},$S{ggCbcSubType},$S{ggCbcType},$S{pbnkFrsCtg},$S{pbnkScdCtg}," +
                "$S{pbnkTrdCtg},$S{pbnkFurCtg},$S{pbnkIndustryIssuer},$S{isuOrgTypSiz},$S{version},$S{cmt}," +
                "$S{crtDate},$S{crtTime},$S{crtUser},$S{dealDate})",DataSourceProperty.PUB, params.getModel());
        return update;
    }

    /**
     * 查浮息
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<BondInfoModel> findBondFX(SqlParam<BondInfoModel> params) throws Exception {

        String sql = "SELECT SCR_ID,FL_BEGIN_DATE,BASE_RATE,FL_END_DATE,CRT_DATE,CRT_TIME,CRT_USER,UPD_DATE,UPD_TIME," +
                "UPD_USER,DEAL_DATE FROM ODS_ASS_FLOAT_RATE";

        return super.findRows( sql, params);
    }

    /**
     * 查浮息
     * @param map
     * @return
     * @throws Exception
     */
    public List<BondInfoModel> findBondFX(Map<String,Object> map) throws Exception {

        String sql = "SELECT SCR_ID,FL_BEGIN_DATE,BASE_RATE,FL_END_DATE,CRT_DATE,CRT_TIME,CRT_USER,UPD_DATE,UPD_TIME," +
                "UPD_USER,DEAL_DATE FROM ODS_ASS_FLOAT_RATE where SCR_ID=$S{scrId}";

        return super.findRows(BondInfoModel.class,sql,0, map);
    }

    /**
     * 查行权
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<BondInfoModel> findBondXQ(SqlParam<BondInfoModel> params) throws Exception {

        String sql = "SELECT SCR_ID,EXERCISE_DATE,EX_COUPON_RATE,CRT_DATE,CRT_TIME,CRT_USER,UPD_DATE,UPD_TIME,UPD_USER," +
                "DEAL_DATE FROM ODS_ASS_FUSION_EXERCISE";

        return super.findRows( sql, params);
    }
    /**
     * 查行权
     * @param map
     * @return
     * @throws Exception
     */
    public List<BondInfoModel> findBondXQ(Map<String,Object> map) throws Exception {

        String sql = "SELECT SCR_ID,EXERCISE_DATE,EX_COUPON_RATE,CRT_DATE,CRT_TIME,CRT_USER,UPD_DATE,UPD_TIME,UPD_USER," +
                "DEAL_DATE FROM ODS_ASS_FUSION_EXERCISE WHERE  SCR_ID=$S{scrId}";

        return super.findRows(BondInfoModel.class,sql,0, map);
    }
    /**
     * 查还本
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<BondInfoModel> findBondHB(SqlParam<BondInfoModel> params) throws Exception {

        String sql = "SELECT SCR_ID,REPAY_DATE,UNIT_PRINCIPAL,CRT_DATE,CRT_TIME,CRT_USER,UPD_DATE,UPD_TIME,UPD_USER," +
                "DEAL_DATE FROM ODS_ASS_ADVANCE_REPAY ";

        return super.findRows( sql, params);
    }
    /**
     * 查还本
     * @param map
     * @return
     * @throws Exception
     */
    public List<BondInfoModel> findBondHB(Map<String,Object> map) throws Exception {

        String sql = "SELECT SCR_ID,REPAY_DATE,UNIT_PRINCIPAL,CRT_DATE,CRT_TIME,CRT_USER,UPD_DATE,UPD_TIME,UPD_USER," +
                "DEAL_DATE FROM ODS_ASS_ADVANCE_REPAY where SCR_ID=$S{scrId}";

        return super.findRows(BondInfoModel.class,sql,0, map);
    }
    /**
     * 保存浮息信息
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult insertBondFX(BondInfoModel params) throws Exception {
        String sql = "INSERT INTO ODS_ASS_FLOAT_RATE (SCR_ID,FL_BEGIN_DATE,BASE_RATE,FL_END_DATE,CRT_DATE,CRT_TIME," +
                "CRT_USER,UPD_DATE,UPD_TIME,UPD_USER,DEAL_DATE) VALUES ($S{scrId},$S{flBeginDate},nullif($S{baseRate},'')," +
                "$S{flEndDate},$S{crtDate},$S{crtTime},$S{crtUser},$S{updDate},$S{updTime},$S{updUser},$S{dealDate}) ";
        return super.update(sql,DataSourceProperty.PUB, params);
    }

    /**
     * 保存行权信息
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult insertBondXQ(BondInfoModel params) throws Exception {
        String sql = "INSERT INTO ODS_ASS_FUSION_EXERCISE (SCR_ID,EXERCISE_DATE,EX_COUPON_RATE,CRT_DATE,CRT_TIME,CRT_USER,UPD_DATE,UPD_TIME," +
                "UPD_USER,DEAL_DATE)VALUES($S{scrId},$S{exerciseDate},nullif($S{exCouponRate},''),$S{crtDate},$S{crtTime}," +
                "$S{crtUser},$S{updDate},$S{updTime},$S{updUser},$S{dealDate}) ";
        return super.update(sql,DataSourceProperty.PUB, params);
    }

    /**
     * 保存还本信息
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult insertBondHB(BondInfoModel params) throws Exception {
        String sql = "INSERT INTO ODS_ASS_ADVANCE_REPAY(SCR_ID,REPAY_DATE,UNIT_PRINCIPAL,CRT_DATE,CRT_TIME,CRT_USER," +
                "UPD_DATE,UPD_TIME,UPD_USER,DEAL_DATE)VALUES($S{scrId},$S{repayDate},nullif($S{unitPrincipal},''),$S{crtDate}," +
                "$S{crtTime},$S{crtUser},$S{updDate},$S{updTime},$S{updUser},$S{dealDate})";
        return super.update(sql,DataSourceProperty.PUB, params);
    }

    /**
     * 改债券基本信息
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult updateBondInfo(SqlParam<BondInfoModel> params) throws Exception {
        return super.update("UPDATE ODS_BOND_BAS_INF SET  " +
                "SCR_CD = $S{scrCd}, " +
                "SCR_SHT_NM = $S{scrShtNm}, " +
                "SCR_NM = $S{scrNm}, " +
                "TRX_MKT = $S{trxMkt}, " +
                "TRX_PLA = $S{trxPla}, " +
                "ISU_PRC = nullif($S{isuPrc},''), " +
                "ACTL_ISU_TOT_AMT = nullif($S{actlIsuTotAmt},''), " +
                "GRNT_MTH = $S{grntMth}, " +
                "IS_CREDIT = $S{isCredit}, " +
                "EMB_OPT_F = $S{embOptF}, " +
                "IS_REPAID = $S{isRepaid}, " +
                "VAL_DT = $S{valDt}, " +
                "MTU_DT = $S{mtuDt}, " +
                "INTR_MTH = $S{intrMth}, " +
                "PAY_INTR_FRQ = $S{payIntrFrq}, " +
                "INTR_BAS = $S{intrBas}, " +
                "COUPON_TYPE = $S{couponType}, " +
                "PAR_RAT = nullif($S{parRat},''), " +
                "SPRD = nullif($S{sprd},''), " +
                "ISU = $S{isu}, " +
                "ORIGN_INTEREST_OBJECT = $S{orignInterestObject}, " +
                "CC_INDUSTRY_ISSUER = $S{ccIndustryIssuer}, " +
                "ISU_ORG_TYP_TCHNO = $S{isuOrgTypTchno}, " +
                "ISU_ORG_TYP_ECN = $S{isuOrgTypEcn}, " +
                "ISU_ORG_TYP_SCALE_SIZ = $S{isuOrgTypScaleSiz}, " +
                "REG_TRST_ORG = $S{regTrstOrg}, " +
                "GRNT = $S{grnt}, " +
                "ISU_BND_RAT = $S{isuBndRat}, " +
                "BOND_FRS_RAT = $S{bondFrsRat}, " +
                "MAIN_RAT = $S{mainRat}, " +
                "GRNT_RAT = $S{grntRat}, " +
                "ASS_INF_CLASS = $S{assInfClass}, " +
                "DEBT_EQUITY_CLASS = $S{debtEquityClass}, " +
                "IS_SUSTAIN = $S{isSustain}, " +
                "CBND_FRS_CTG = $S{cbndFrsCtg}, " +
                "CBND_SCD_CTG = $S{cbndScdCtg}, " +
                "SPC_TYPE = $S{spcType}, " +
                "GG_CBC_SUB_TYPE = $S{ggCbcSubType}, " +
                "GG_CBC_TYPE = $S{ggCbcType}, " +
                "PBNK_FRS_CTG = $S{pbnkFrsCtg}, " +
                "PBNK_SCD_CTG = $S{pbnkScdCtg}, " +
                "PBNK_TRD_CTG = $S{pbnkTrdCtg}, " +
                "PBNK_FUR_CTG = $S{pbnkFurCtg}, " +
                "PBNK_INDUSTRY_ISSUER = $S{pbnkIndustryIssuer}, " +
                "ISU_ORG_TYP_SIZ = $S{isuOrgTypSiz}, " +
                "CMT = $S{cmt}, " +
                "UPD_DATE = $S{updDate}, " +
                "UPD_TIME = $S{updTime}, " +
                "UPD_USER = $S{updUser}, " +
                "DEAL_DATE = $S{dealDate} " +
                "WHERE SCR_ID = $S{scrId}", DataSourceProperty.PUB, params.getModel());
    }

    /**
     * 改债券补录信息
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult updateBondInfoBl(SqlParam<BondInfoModel> params) throws Exception {
        return super.update("UPDATE ODS_BOND_BAS_INF SET  " +
                "ISU_MTH = $S{isuMth}, " +
                "VERSION = $S{version}, " +
                "UPD_DATE = $S{updDate}, " +
                "UPD_TIME = $S{updTime}, " +
                "UPD_USER = $S{updUser}, " +
                "DEAL_DATE = $S{dealDate} " +
                "WHERE SCR_ID = $S{scrId}", DataSourceProperty.PUB, params.getModel());
    }

    /**
     * 联动更改默认值
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult updateBondInfoBlField(SqlParam<BondInfoModel> params) throws Exception {
        return super.update("UPDATE ODS_BOND_BAS_INF SET  " +
                "GG_CBC_SUB_TYPE = $S{ggCbcSubType} " +
                "WHERE SCR_ID = $S{scrId}", DataSourceProperty.PUB, params.getModel());
    }

    /**
     * 删债券基本信息
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult deleteBondInfo(SqlParam<BondInfoModel> params) throws Exception {
        return super.update("DELETE FROM ODS_BOND_BAS_INF WHERE SCR_ID = $S{scrId}",DataSourceProperty.PUB, params.getModel());
    }
    /**
     * 删债券浮息信息
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult deleteBondFX(BondInfoModel params) throws Exception {
        return super.update("DELETE FROM ODS_ASS_FLOAT_RATE WHERE SCR_ID = $S{scrId}",DataSourceProperty.PUB, params);
    }

    /**
     * 删债券行权信息
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult deleteBondXQ(BondInfoModel params) throws Exception {
        return super.update("DELETE FROM ODS_ASS_ADVANCE_REPAY WHERE SCR_ID = $S{scrId}",DataSourceProperty.PUB, params);
    }

    /**
     * 删债券还本信息
     * @param params
     * @return
     * @throws Exception
     */
    public UpdateResult deleteBondHB(BondInfoModel params) throws Exception {
        return super.update("DELETE FROM ODS_ASS_FUSION_EXERCISE WHERE SCR_ID = $S{scrId}",DataSourceProperty.PUB, params);
    }

    /**
     * 查是否有相同债券
     * @param params
     * @return
     * @throws Exception
     */
    public SqlRow existSameBond(SqlParam<BondInfoModel> params) throws Exception {
        return super.findRow("SELECT COUNT(1) CON FROM ODS_BOND_BAS_INF WHERE SCR_CD = $S{scrCd} AND TRX_MKT = $S{trxMkt}",DataSourceProperty.PUB, params.getModel());
    }



    public  List<SqlRow>  getUPDTypeByDocType(Object params) throws Exception {
        List<SqlRow> s = super.findRows("SELECT itemkey VALUE,  "  +
                "itemval TEXT  "  +
                "FROM sys_dict_item  "  +
                "WHERE dict = 'windScd' " +
                "AND itemkey LIKE '$U{bndFrsCtg}%' " +
                "ORDER BY itemkey+0",DataSourceProperty.PUB,params);
        return s;
    }

    public SqlResult<BondInfoModel> findBondInfoCdAndNm(SqlParam<BondInfoModel> params) throws Exception {
        String sql = "SELECT DISTINCT SCR_CD,SCR_SHT_NM FROM ods_bond_bas_inf where SCR_CD like '%$U{scrCd}%' or SCR_SHT_NM like '%$U{scrCd}%'";
        return super.findRows(sql,DataSourceProperty.PUB, params);

    }

    public SqlResult<BondInfoModel> findBondInfoCdAndNmByTrxMkt(SqlParam<BondInfoModel> params) throws Exception {
        String sql = "SELECT DISTINCT SCR_CD,SCR_SHT_NM FROM ods_bond_bas_inf where trx_mkt = $S{trxMkt}";
        return super.findRows(sql,DataSourceProperty.PUB, params);

    }

    public SqlResult<BondInfoModel> findBondInfoName(SqlParam<BondInfoModel> params) throws Exception {
        String sql = "SELECT DISTINCT SCR_SHT_NM  FROM ods_bond_bas_inf where SCR_CD= $S{scrCd}";
        return super.findRows(sql,DataSourceProperty.PUB, params);
    }


    public List<SqlRow> findProd() throws Exception {
        String sql = "SELECT prod_cd as prodCode,prod_nm as prodName FROM dwd_prd_prd_bas_inf where call_dt >= '" + SysUtil.getSystemParamsByParaid("10004") + "' order by found_dt";
        return super.findRows(sql,DataSourceProperty.PUB);
    }


    public  List<SqlRow>  getUPDTypeByDoc(Object params) throws Exception {
        List<SqlRow> s = super.findRows("SELECT itemkey VALUE,  "  +
                "itemval TEXT  "  +
                "FROM sys_dict_item  "  +
                "WHERE dict = 'cbndScdCtg' " +
                "AND itemorder LIKE '$U{cbndFrsCtg}%' " +
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

    public List<SqlRow> findIsuOrgBlgIdtDict(Map<String, Object> params) throws Exception {
        List<SqlRow> s = super.findRows("select itemkey VALUE,itemval TEXT    FROM sys_dict_item   WHERE dict = 'isuOrgBlgIdt' and substr(itemkey,1,1) = $S{ecoFrsTyp}  ORDER BY itemkey+0 ",DataSourceProperty.PUB,params);
        return s;
    }

    public List<SqlRow> findDict(Map<String, Object> params) throws Exception {
        String sql = "SELECT itemkey VALUE,itemval TEXT FROM sys_dict_item WHERE dict = $S{dict} AND itemkey IN ("+params.get("itemkey")+") ORDER BY itemkey+0";
        List<SqlRow> s = super.findRows(sql ,DataSourceProperty.PUB,params);
        return s;
    }

    public List<SqlRow> findDictLike(Map<String, Object> params) throws Exception {
        String sql = "SELECT itemkey VALUE,itemval TEXT FROM sys_dict_item WHERE dict = $S{dict} AND itemkey like '$U{itemkey}%' ORDER BY itemkey+0";
        List<SqlRow> s = super.findRows(sql ,DataSourceProperty.PUB,params);
        return s;
    }

    public SqlResult<BondInfoModel> isOnlyOne(SqlParam<BondInfoModel> params) throws Exception {
        return super.findRows("SELECT SCR_CD FROM ODS_BOND_BAS_INF WHERE SCR_CD = $S{scrCd} AND TRX_MKT = $S{trxMkt}",DataSourceProperty.PUB, params);
    }
}
