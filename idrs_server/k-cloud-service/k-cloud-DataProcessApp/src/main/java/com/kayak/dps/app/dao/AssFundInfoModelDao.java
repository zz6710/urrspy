package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.dps.app.model.AssBondInfoModel;
import com.kayak.dps.app.model.AssFundInfoModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AssFundInfoModelDao extends ComnDao {

    public void deleteAssFundInfoModel(SqlParam<AssFundInfoModel> params) throws Exception {
        String sql = " delete from ods_supply_fund_bas_inf where scr_id = $S{scrId}";
        super.update(sql, DataSourceProperty.PUB, params.getModel());
    }


    public void addAssFundInfoModel(SqlParam<AssFundInfoModel> params) throws Exception {
        String sql = "update ods_supply_fund_bas_inf t\n" +
                "set \n" +
                "t.cbnd_frs_ctg = $S{cbndFrsCtg},/**中债一级分类*/\n" +
                "t.cbnd_scd_ctg = $S{cbndScdCtg},/**中债二级分类*/\n" +
                "t.pbnk_frs_ctg = $S{pbnkFrsCtg},/**人行一级分类*/\n" +
                "t.pbnk_scd_ctg = $S{pbnkScdCtg},/**人行二级分类*/\n" +
                "t.pbnk_trd_ctg = $S{pbnkTrdCtg},/**人行三级分类*/\n" +
                "t.fin_ast_inv_cmp_isu_f = $S{finAstInvCmpIsuF},/**金融资产投资公司发行标识*/\n" +
                "t.idt = $S{idt},/**发行机构所属行业（二级分类）*/\n" +
                "t.reg_rcd_org = $S{regRcdOrg},/**登记备案机构*/\n" +
                "t.BLG_GOV_INV_FND_F = $S{blgGovInvFndF} ,/**归属政府投资基金标识*/\n" +
                "t.GOV_INV_FND_DIR = $S{govInvFndDir},/**政府投资基金投向*/\n" +
                "t.INV_STG = $S{invStg} ,/**投资阶段*/\n" +
                "t.INV_ENTP_TYP_SIZ = $S{invEntpTypSiz} ,/**投资企业类型(按规模划分)*/\n" +
                "t.INV_ENTP_TYP_TCHNO = $S{invEntpTypTchno},/**投资企业类型(按技术领域划分)*/\n" +
                "t.INV_ENTP_TYP_ECN = $S{invEntpTypEcn} ,/**投资企业类型(按经济类型划分)*/\n" +
                "t.FND_INV_AST = $S{fndInvAst},/**基金投资资产*/\n" +
                "t.SPV_ORG_ENC = $S{spvOrgEnc},/**SPV机构编码*/\n" +
                "t.SPV_PROD_REG_ENC = $S{spvProdRegEnc},/**SPV产品登记编码*/\n" +
                "t.UPD_DT = date_format(CURDATE(), '%Y%m%d') ,\n" +
                "t.VERSION = t.VERSION + 1,\n" +
                "t.TRX_PLA = $S{trxPla},\n" +
                "t.CMT = $S{cmt}\n" +
                "where \n" +
                "t.SCR_ID = $S{scrId}";
        super.update(sql, DataSourceProperty.PUB,params.getModel());
    }

    public void importAssFundInfo(List<AssFundInfoModel> assFundInfoModelList) throws Exception {
        for (AssFundInfoModel abim:assFundInfoModelList) {
            String scrId = abim.getScrId();;//基金代码
            String trxPla = abim.getTrxPla();//交易流通场所
            if(StringUtils.isNotBlank(scrId)) {
                if (scrId.contains(".")) {
                    String tempStr1 = scrId.substring(0, scrId.indexOf("."));
                    String tempStr2 = scrId.substring(scrId.indexOf(".") + 1);
                    if (StringUtils.equals(tempStr2, "OF")) {
                        abim.setScrId(tempStr1 + ".6.8");
                    } else if (StringUtils.equals(tempStr2, "SZ")) {
                        abim.setScrId(tempStr1 + ".2.8");
                    }
                    if (StringUtils.equals(tempStr2, "SH")) {
                        abim.setScrId(tempStr1 + ".1.8");
                    }
                    super.update("update ods_supply_fund_bas_inf ab  set SPV_ORG_ENC = $S{spvOrgEnc},SPV_PROD_REG_ENC = $S{spvProdRegEnc},version=(version+1) where SCR_ID=$S{scrId}", DataSourceProperty.PUB, abim);
                } else {
                    if (StringUtils.equals(trxPla, "15")) {
                        abim.setScrId(scrId + ".6.8");
                    } else if (StringUtils.equals(trxPla, "03")) {
                        abim.setScrId(scrId + ".1.8");
                    }
                    if (StringUtils.equals(trxPla, "04")) {
                        abim.setScrId(scrId + ".2.8");
                    }
                    super.update("update ods_supply_fund_bas_inf ab  set cbnd_scd_ctg = $S{cbndScdCtg},trx_pla = $S{trxPla},idt = $S{idt},REG_RCD_ORG = $S{regRcdOrg},fnd_inv_ast = $S{fndInvAst},version=(version+1) where SCR_ID=$S{scrId}", DataSourceProperty.PUB, abim);
                }
            }
        }
    }
}
