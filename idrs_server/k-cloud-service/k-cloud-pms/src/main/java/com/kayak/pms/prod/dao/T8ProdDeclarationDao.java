package com.kayak.pms.prod.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.prod.model.T8ProdDeclaration;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/1/14 20:53
 */
@Repository
public class T8ProdDeclarationDao extends ComnDao {
    public SqlResult<T8ProdDeclaration> getT8ProdDeclarationInfoByprodCode(SqlParam<T8ProdDeclaration> params) throws Exception {
        return super.findRows("select a.t8_prod_info_id,a.prod_code,a.prod_name,a.income_characteristic,a.prod_days,a.raise_type, a.perf_method_max,\n" +
                "       a.perf_method_min,a.perf_method_explain,a.prod_cur,a.prod_cur redeem_cash_cur,a.prod_cur redeem_income_cur,\n" +
                "       a.expe_scale,a.prod_risk_level,b.bank_prodsid,b.approval_name,b.approval_identif,b.design_name,b.design_identif,\n" +
                "       b.invest_manage_name,b.invest_manage_identif,b.liaison,b.liaison_tel,b.liaison_phone,b.liaison_email,\n" +
                "       b.sale_place,b.out_place_desc, b.srv_mode,b.asset_maping,b.manage_mode,b.admin_name,b.pricing_type,b.is_hold_period,\n" +
                "       b.minholdday,b.holdredeem,b.sales_palce,b.bord_trusti_code,b.overs_trusti_name,b.overs_trusti_nation,b.preendmark,\n" +
                "       b.redeem_mark,b.prod_brand,b.prod_times,b.cooperat_mode,b.cooperat_orgname,b.returncost,b.returnincome,b.prodcreditflag,\n" +
                "       b.prodcreditorg,b.prodcreditmod,b.prod_precent,b.remark,c.raise_start_date,c.raise_end_date\n" +
                "from t8_prod_info a  left join ods_amng_prod_cbrcdat b on a.prod_code=b.prod_code left  join t8_prod_days c on a.prod_code=c.prod_code\n" +
                "where a.prod_code =$S{prodCode}", params);
    }

    public int addprodDeclarationInfo(T8ProdDeclaration t8ProdDeclaration) throws Exception {
        return super.update("INSERT INTO t8_prod_declaration(id,t8_prod_info_id,prod_code, prod_name, income_characteristic, prod_days," +
                "                                raise_type, perf_method_max, perf_method_min, perf_method_explain, prod_cur," +
                "                                redeem_cash_cur, redeem_income_cur, expe_scale, prod_risk_level, bank_prodsid," +
                "                                approval_name, approval_identif, design_name, design_identif, invest_manage_name," +
                "                                invest_manage_identif, liaison, liaison_tel, liaison_phone, liaison_email, sale_place," +
                "                                out_place_desc, srv_mode, asset_maping, manage_mode, admin_name, pricing_type, is_hold_period," +
                "                                minholdday, holdredeem, sales_palce, bord_trusti_code, overs_trusti_name, overs_trusti_nation," +
                "                                preendmark, redeem_mark, prod_brand, prod_times, cooperat_mode, cooperat_orgname, returncost," +
                "                                returnincome, prodcreditflag, prodcreditorg, prodcreditmod, prod_precent, remark," +
                "                                raise_start_date, raise_end_date,status,inputuser,crt_date,crt_time) " +
                "        VALUES ($AUTOIDS{prodDeclarationId},$S{t8ProdInfoId},$S{prodCode}, $S{prodName}, $S{incomeCharacteristic}, 0," +
                "                $S{raiseType}, $S{perfMethodMax}, $S{perfMethodMin}, $S{perfMethodExplain}," +
                "                $S{prodCur}, $S{redeemCashCur}, $S{redeemIncomeCur}, 0, $S{prodRiskLevel}," +
                "                $S{bankProdsid}, $S{approvalName}, $S{approvalIdentif}, $S{designName}, $S{designIdentif}, $S{investManageName}," +
                "                $S{investManageIdentif}, $S{liaison}, $S{liaisonTel}, $S{liaisonPhone}, $S{liaisonEmail}, $S{salePlace}," +
                "                $S{outPlaceDesc}, $S{srvMode}, $S{assetMaping}, $S{manageMode}, $S{adminName}, $S{pricingType}, $S{isHoldPeriod}," +
                "                $S{minholdday}, $S{holdredeem}, $S{salesPalce}, $S{bordTrustiCode}, $S{oversTrustiName}, $S{oversTrustiNation}," +
                "                $S{preendmark}, $S{redeemMark}, $S{prodBrand}, $S{prodTimes}, $S{cooperatMode}, $S{cooperatOrgname}, $S{returncost}, $S{returnincome}," +
                "                $S{prodcreditflag}, $S{prodcreditorg}, $S{prodcreditmod}, $S{prodPrecent}, $S{remark}, $S{raiseStartDate}, $S{raiseEndDate},'0'," +
                "                $S{inputuser},$S{crtDate},$S{crtTime})", t8ProdDeclaration).getEffect();
    }

    public SqlResult<T8ProdDeclaration> getDeclarationInfoByProdCode(SqlParam<T8ProdDeclaration> params) throws Exception {
        return super.findRows("select * from t8_prod_declaration where prod_code=$S{prodCode}", params);
    }

    public int updateStatus(SqlParam<T8ProdDeclaration> params) throws Exception {
        return super.update("update t8_prod_declaration set status='1' where id=$S{id}", params.getModel()).getEffect();
    }
}
