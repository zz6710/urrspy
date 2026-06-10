package com.kayak.subject.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.subject.model.DwsAstDebPbnkDtl;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class DwsAstDebPbnkDtlDao extends ComnDao {

	public SqlResult<DwsAstDebPbnkDtl> findDwsAstDebPbnkDtls(SqlParam<DwsAstDebPbnkDtl> params) throws Exception {
		return super.findRows("SELECT id,report_date,prod_cd,prod_nm,scr_cd,scr_nm,asst_thr_knd,asst_type,asst_clss,mtu_dt,prod_trm_pbnk,mkt_vol FROM dws_ast_deb_pbnk_dtl", params);
	}

	public UpdateResult addDwsAstDebPbnkDtl(SqlParam<DwsAstDebPbnkDtl> params) throws Exception {
		return super.update("INSERT INTO dws_ast_deb_pbnk_dtl(id,report_date,prod_cd,prod_nm,scr_cd,scr_nm,asst_thr_knd,asst_type,asst_clss,mtu_dt,prod_trm_pbnk,mkt_vol) VALUES($AUTOIDI{id},$S{reportDate},$S{prodCd},$S{prodNm},$S{scrCd},$S{scrNm},$S{asstThrKnd},$S{asstType},$S{asstClss},$S{mtuDt},$S{prodTrmPbnk},$D{mktVol})",
				params.getModel());
	}
	
	public UpdateResult updateDwsAstDebPbnkDtl(SqlParam<DwsAstDebPbnkDtl> params) throws Exception {
		return super.update("UPDATE dws_ast_deb_pbnk_dtl SET report_date=$S{reportDate} ,prod_cd=$S{prodCd} ,prod_nm=$S{prodNm} ,scr_cd=$S{scrCd} ,scr_nm=$S{scrNm} ,asst_thr_knd=$S{asstThrKnd} ,asst_type=$S{asstType} ,asst_clss=$S{asstClss} ,mtu_dt=$S{mtuDt} ,prod_trm_pbnk=$S{prodTrmPbnk} ,mkt_vol=$D{mktVol}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteDwsAstDebPbnkDtl(SqlParam<DwsAstDebPbnkDtl> params) throws Exception {
		return super.update("DELETE FROM dws_ast_deb_pbnk_dtl WHERE  id=$I{id} ",
				params.getModel());
	}

	public UpdateResult deleteDwsAstDebPbnkDtl(String dealDate) throws Exception{
		return super.update("DELETE FROM dws_ast_deb_pbnk_dtl WHERE report_date =$S{dealDate}", dealDate);
	}
}
