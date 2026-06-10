package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.dps.app.model.AssNetValSPVInfoModel;
import org.springframework.stereotype.Repository;

@Repository
public class AssNetValSPVInfoModelDao extends ComnDao {

    public void deleteAssNetValSPVInfoModel(SqlParam<AssNetValSPVInfoModel> params) throws Exception {
        String sql = " delete from ods_supply_ast_mng_plan_inf where scr_id = $S{scrId}";
        super.update(sql, DataSourceProperty.PUB, params.getModel());
    }

    public void addAssNetValSPVInfoModel(SqlParam<AssNetValSPVInfoModel> params) throws Exception {
       String sql = "update ods_supply_ast_mng_plan_inf t \n" +
               "set \n" +
               "t.SET_UP_DT = $S{setUpDt},/**成立日*/\n" +
               "t.MTU_DT = $S{mtuDt},/**到期日*/\n" +
               "t.CBND_FRS_CTG = $S{cbndFrsCtg},/**中债一级分类*/\n" +
               "t.CBND_SCD_CTG = $S{cbndScdCtg},/**中债二级分类*/\n" +
               "t.PBNK_FRS_CTG = $S{pbnkFrsCtg},/**人行一级分类*/\n" +
               "t.PBNK_SCD_CTG = $S{pbnkScdCtg},/**人行二级分类*/\n" +
               "t.PBNK_TRD_CTG = $S{pbnkTrdCtg},/**人行三级分类*/\n" +
               "t.BNK_INV_PROD_F = $S{bnkInvProdF},/**银行理财产品标识*/\n" +
               "t.PROD_REG_ENC = $S{prodRegEnc},/**产品登记编码*/\n" +
               "t.FIN_AST_INV_CMP_ISU_F = $S{finAstInvCmpIsuF},/**金融资产投资公司发行标识*/\n" +
               "t.ISU_ORG_ENC  = $S{isuOrgEnc},/**发起人机构编码*/\n" +
               "t.AST_MNG_PLAN_REG_ENC  = $S{astMngPlanRegEnc},/**资管计划登记编码*/\n" +
               "t.MNG =  $S{mng},/**管理人*/\n" +
               "t.CSTD  = $S{cstd},/**托管人*/\n" +
               "t.AMT = if($S{amt}='',null,$S{amt}),/**金额*/\n" +
               "t.FND_ACTL_DIR = $S{fndActlDir},/**资金实际投向*/\n" +
               "t.FND_CRRY_MTH = $S{fndCrryMth},/**资金运用方式*/\n" +
               "t.FND_CRRY_IDT = $S{fndCrryIdt},/**资金运用行业*/\n" +
               "t.AST_MNG_PLAN_PRPT = $S{astMngPlanPrpt},/**资管计划属性*/\n" +
               "t.EXPE_RAT_F = $S{expeRatF},/**预期收益率标识*/\n" +
               "t.EXPE_MAX_RAT = ROUND($S{expeMaxRat} / 100, 7),/**预期最高收益率*/\n" +
               "t.EXPE_MIN_RAT = ROUND($S{expeMinRat} / 100, 7),/**预期最低收益率*/\n" +
               "t.BUY_STRC = $S{buyStrc},/**购买结构*/\n" +
               "t.MNG_MTH  = $S{mngMth},/**管理方式*/\n" +
               "t.MNG_FEE_TAT  = ROUND($S{mngFeeTat} / 100, 7),/**管理费率*/\n" +
               "t.TRST_FEE_TAT = ROUND($S{trstFeeTat} / 100, 7),/**托管费率*/\n" +
               "t.TRX_REL_SMR_FEE_RAT  =  ROUND($S{trxRelSmrFeeRat} / 100, 7),/**交易相关合计费率*/\n" +
               "t.MED_AGN_SRV_ORG_SMR_FEE_RAT = ROUND($S{medAgnSrvOrgSmrFeeRat} / 100, 7),/**中介服务机构合计费率*/\n" +
               "t.OTH_SMR_FEE_RAT  = ROUND($S{othSmrFeeRat} / 100, 7),/**其他合计费率*/\n" +
               "t.CMT = $S{cmt},/**备注*/\n"+
               "t.UPD_DT = date_format(CURDATE(),'%Y%m%d'),\n" +
               "t.VERSION = (t.VERSION + 1),\n" +
               "t.SPV_ORG_ENC = $S{spvOrgEnc},/**特定目的载体发起人机构编码*/\n" +
               "t.SPV_PROD_REG_ENC  = $S{spvProdRegEnc}/**特定目的载体产品登记编码*/\n" +
               "where  scr_id = $S{scrId}";
        super.update(sql, DataSourceProperty.PUB,params.getModel());
    }



}
