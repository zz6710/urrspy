package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.dps.app.model.AssNonStandInfoModel;
import org.springframework.stereotype.Repository;

@Repository
public class AssNonStandInfoModelDao extends ComnDao {

    public void deleteAssNonStandInfoModel(SqlParam<AssNonStandInfoModel> params) throws Exception {
        String sql = " delete from ods_supply_nstd_ast_inf where scr_id = $S{scrId}";
        super.update(sql, DataSourceProperty.PUB, params.getModel());
    }

    public void addAssNonStandInfoModel(SqlParam<AssNonStandInfoModel> params) throws Exception {
        String sql = "update ods_supply_nstd_ast_inf t \n" +
                "set \n" +
                "t.PAY_INTR_FRQ = $S{payIntrFrq} ,/**付息频率*/\n" +
                "t.CBND_FRS_CTG = $S{cbndFrsCtg} ,/**中债一级分类*/\n" +
                "t.CBND_SCD_CTG = $S{cbndScdCtg} ,/**中债二级分类*/\n" +
                "t.PBNK_FRS_CTG = $S{pbnkFrsCtg} ,/**人行一级分类*/\n" +
                "t.PBNK_SCD_CTG = $S{pbnkScdCtg} ,/**人行二级分类*/\n" +
                "t.PBNK_TRD_CTG = $S{pbnkTrdCtg} ,/**人行三级分类*/\n" +
                "t.LOT_PAR_VAL  = (case when $S{lotParVal} ='' then null else $S{lotParVal} end),/**份额面值*/\n" +
                "t.INC_BEN_RIT_TYP = $S{incBenRitTyp} ,/**收/受益权类型*/\n" +
                "t.BUY_BACK_F = $S{buyBackF} ,/**买入返售标识*/\n" +
                "t.PRJ_TRM = (case when $S{prjTrm} = '' then null else $S{prjTrm} end ),/**项目期限*/\n" +
                "t.INTR_TYP  = $S{intrTyp},/**计息类型*/\n" +
                "t.INTR_ALC_MTH = $S{intrAlcMth} ,/**利息分布方式*/\n" +
                "t.PAY_PRCP_INTR_STS_CMT = $S{payPrcpIntrStsCmt},/**还本付息情况说明*/\n" +
                "t.BCHM_RAT_TYP = $S{bchmRatTyp} ,/**基准利率种类*/\n" +
                "t.FLT_FCT_F = $S{fltFctF} ,/**浮动因子标识*/\n" +
                "t.FLT_FCT =  (case when $S{fltFct} = '' then null else format($S{fltFct} / 100,7) end ) ,/**浮动因子*/\n" +
                "t.SPRD =  (case when $S{sprd} = '' then null else format($S{sprd}/100,7) end )  ,/**利差*/\n" +
                "t.STRC_GRD  = $S{strcGrd} ,/**结构档次*/\n" +
                "t.INS_PAY_PRCP_F  = $S{insPayPrcpF} ,/**分期还本标识*/\n" +
                "t.BAS_AST_TYP  = $S{basAstTyp} ,/**基础资产类型*/\n" +
                "t.EXS_ERN_ALC_RTO = (case when $S{exsErnAlcRto} = '' then null else format($S{exsErnAlcRto} / 100,7) end )  ,/**超额收益分配比例*/\n" +
                "t.LVRG = $S{lvrg} ,/**融资人*/\n" +
                "t.LVRG_IN_CRD_RAT = $S{lvrgInCrdRat} ,/**融资人内部信用评级*/\n" +
                "t.OUT_RAT_ORG_AND_LVRG_RAT = $S{outRatOrgAndLvrgRat} ,/**外部评级机构及融资人评级*/\n" +
                "t.LVRG_TYP_SIZ = $S{lvrgTypSiz} ,/**融资人类型（按规模划分）*/\n" +
                "t.LVRG_TYP_TCHNO  = $S{lvrgTypTchno} ,/**融资人类型（按技术领域划分）*/\n" +
                "t.LVRG_TYP_ECN = $S{lvrgTypEcn} ,/**融资人类型（按经济类型划分）*/\n" +
                "t.LVRG_PRJ  = $S{lvrgPrj} ,/**融资项目*/\n" +
                "t.LVRG_BLG_IDT = $S{lvrgBlgIdt} ,/**融资人归属行业*/\n" +
                "t.LVRG_PRJ_BLG_IDT = $S{lvrgPrjBlgIdt} ,/**融资项目归属行业*/\n" +
                "t.PRJ_BLG_KEY_MNT_IDT = $S{prjBlgKeyMntIdt} ,/**项目归属重点监控行业和领域标识*/\n" +
                "t.KEY_MNT_IDT_TYP = $S{keyMntIdtTyp} ,/**重点监控行业和领域类别*/\n" +
                "t.KEY_MNT_IDT_TYP_CMT = $S{keyMntIdtTypCmt} ,/**重点监控行业和领域类别说明*/\n" +
                "t.GRNT_MTH  = $S{grntMth} ,/**担保方式*/\n" +
                "t.GRNT_STS_CMT = $S{grntStsCmt} ,/**担保情况说明*/\n" +
                "t.PLG_TYP = $S{plgTyp},/**抵质押物类型*/\n" +
                "t.PLG_VAL = (case when $S{plgVal} = '' then null else $S{plgVal} end ) ,/**抵质押物价值*/\n" +
                "t.GRNT_CHR  = $S{grntChr} ,/**担保性质*/\n" +
                "t.GRNT_LVRG_REL = $S{grntLvrgRel} ,/**担保人与融资人关系*/\n" +
                "t.GRNT_MAIN_RAT = $S{grntMainRat},/**融资人主体评级*/\n" +
                "t.AST_IN_RAT = $S{astInRat} ,/**资产内部评级*/\n" +
                "t.AST_OUT_RAT  = $S{astOutRat} ,/**资产外部评级*/\n" +
                "t.EMB_OPT_TYP  = $S{embOptTyp},/**含权类型*/\n" +
                "t.XCS_RIT_MTH  = $S{xcsRitMth} ,/**行权方式*/\n" +
                "t.FIX_XCS_RIT_DT  = $S{fixXcsRitDt} ,/**固定行权日*/\n" +
                "t.FRS_FIX_XCS_DT  = $S{frsFixXcsDt} ,/**首次行权日期*/\n" +
                "t.XCS_RIT_PRD  = (case when $S{xcsRitPrd} = '' then null else $S{xcsRitPrd} end ),/**行权周期*/\n" +
                "t.XCS_RIT_PRC  = (case when $S{xcsRitPrc} = '' then null else $S{xcsRitPrc} end ) ,/**行权价格*/\n" +
                "t.PERP_TYP  = $S{perpTyp} ,/**永续条款类型*/\n" +
                "t.INTR_PPN_TYP = $S{intrPpnTyp} ,/**利息递延条款类型*/\n" +
                "t.PPN_INTR_INTR_F = $S{ppnIntrIntrF} ,/**递延利息计息标识*/\n" +
                "t.FRS_RPRC_DT  = $S{frsRprcDt} ,/**首次重定价日期*/\n" +
                "t.RPRC_PRD  = (case when $S{rprcPrd} = '' then null else $S{rprcPrd} end ) ,/**重定价周期*/\n" +
                "t.PART_RDM_F = $S{partRdmF} ,/**部分赎回标识*/\n" +
                "t.PART_RDM_RTO = (case when $S{partRdmRto} = '' then null else format($S{partRdmRto} / 100,7) end ),/**部分赎回比例*/\n" +
                "t.CHC_RIT = $S{chcRit} ,/**选择权*/\n" +
                "t.XCS_RIT_COND_CMT = $S{xcsRitCondCmt} ,/**行权条件说明*/\n" +
                "t.LVRG_BLG_ZON = $S{lvrgBlgZon} ,/**融资人归属地区*/\n" +
                "t.INC_CRD_ORG_CD  = $S{incCrdOrgCd} ,/**增信机构代码*/\n" +
                "t.INC_CRD_ORG_NM  = $S{incCrdOrgNm} ,/**增信机构名称*/\n" +
                "t.LVRG_TOT_FEE = (case when $S{lvrgTotFee} = '' then null else format($S{lvrgTotFee} / 100,7) end ) ,/**融资总费率*/\n" +
                "t.LVRG_ORG_ORG_CD = $S{lvrgOrgOrgCd} ,/**融资人组织机构（社会信用）代码*/\n" +
                "t.CMT  = $S{cmt},/**备注*/\n" +
                "t.UPD_DT  = date_format(CURDATE(), '%Y%m%d'),\n" +
                "t.VERSION = t.VERSION + 1,\n" +
                "t.TRX_PLA = $S{trxPla}  ,/**交易流通场所*/\n" +
                "t.RUL_PAY_INTR_F = $S{rulPayIntrF}, /**规则付息标识*/\n" +
                "t.INVEST_TYPE = $S{investType} /**表内外城商行投资分类*/\n" +
                "where SCR_ID = $S{scrId}";
        super.update(sql, DataSourceProperty.PUB,params.getModel());
    }
}
