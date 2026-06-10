package com.kayak.subject.dao;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.subject.model.DwsAstPrdItmBalSmr;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;
import java.util.Map;

@Repository
public class DwsAstPrdItmBalSmrDao extends ComnDao {

	public SqlResult<DwsAstPrdItmBalSmr> findDwsAstPrdItmBalSmrs(SqlParam<DwsAstPrdItmBalSmr> params) throws Exception {
	    String sql = "SELECT id,prod_cd,prod_intr_cd,ccy_cd,ctg_cd,amt_bal,act_dt,crt_date,crt_time,upd_date,upd_time" +
    				" FROM dws_ast_prd_itm_bal_smr" +
    				" where 1=1";
		if (Tools.isNotEmpty(params.getModel().getProdCd())) {
            sql += " and prod_cd like '%" + params.getModel().getProdCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getProdIntrCd())) {
            sql += " and prod_intr_cd like '%" + params.getModel().getProdIntrCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCcyCd())) {
            sql += " and ccy_cd like '%" + params.getModel().getCcyCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCtgCd())) {
            sql += " and ctg_cd like '%" + params.getModel().getCtgCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getAmtBal())) {
            sql += " and amt_bal like '%" + params.getModel().getAmtBal() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getActDt())) {
            sql += " and act_dt like '" + params.getModel().getActDt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCrtDate())) {
			sql += " and crt_date = '" + params.getModel().getCrtDate() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getCrtTime())) {
			sql += " and crt_time = '" + params.getModel().getCrtTime() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getUpdDate())) {
			sql += " and upd_date = '" + params.getModel().getUpdDate() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getUpdTime())) {
			sql += " and upd_time = '" + params.getModel().getUpdTime() + "'";
		}
		sql += " order by act_dt desc, crt_date desc";
        return super.findRows(sql, params);
	}

	public List<SqlRow> findDwsAstPrdItmBalSmrs(Map<String, Object> params) throws Exception {
		String sql = "SELECT id,prod_cd,prod_intr_cd,ccy_cd,ctg_cd,amt_bal,act_dt,crt_date,crt_time,upd_date,upd_time" +
				" FROM dws_ast_prd_itm_bal_smr where act_dt like '" + params.get("dealDate") + "%'";
		return super.findRows(sql);
	}

	public UpdateResult addDwsAstPrdItmBalSmr(SqlParam<DwsAstPrdItmBalSmr> params) throws Exception {
		return super.update("INSERT INTO dws_ast_prd_itm_bal_smr(prod_cd,prod_intr_cd,ccy_cd,ctg_cd,amt_bal,act_dt,crt_date,crt_time,upd_date,upd_time)" +
						" VALUES($S{prodCd},$S{prodIntrCd},$S{ccyCd},$S{ctgCd},$D{amtBal},$S{actDt},$D{crtDate},$S{crtTime},$D{updDate},$S{updTime})",
				params.getModel());
	}
	
	public UpdateResult updateDwsAstPrdItmBalSmr(SqlParam<DwsAstPrdItmBalSmr> params) throws Exception {
		return super.update("UPDATE dws_ast_prd_itm_bal_smr SET prod_cd=$S{prodCd} ,prod_intr_cd=$S{prodIntrCd} ,ccy_cd=$S{ccyCd} ,ctg_cd=$S{ctgCd} ,amt_bal=$D{amtBal} ,act_dt=$S{actDt} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,upd_date=$S{updDate} ,upd_time=$S{updTime} WHERE id=$I{id}",
				params.getModel());
	}
	
	public UpdateResult deleteDwsAstPrdItmBalSmr(SqlParam<DwsAstPrdItmBalSmr> params) throws Exception {
		return super.update("DELETE FROM dws_ast_prd_itm_bal_smr WHERE id=$I{id}",
				params.getModel());
	}

	public UpdateResult deleteDwsAstPrdItmBalSmr(DwsAstPrdItmBalSmr params) throws Exception {
		return super.update("DELETE FROM dws_ast_prd_itm_bal_smr WHERE act_dt = $S{actDt}",
				params);
	}

}
