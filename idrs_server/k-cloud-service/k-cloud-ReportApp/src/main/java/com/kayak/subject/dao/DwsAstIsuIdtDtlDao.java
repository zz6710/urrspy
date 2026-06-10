package com.kayak.subject.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.subject.model.DwsAstIsuIdtDtl;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.Map;

@Repository
public class DwsAstIsuIdtDtlDao extends ComnDao {

	public SqlResult<DwsAstIsuIdtDtl> findDwsAstIsuIdtDtls(SqlParam<DwsAstIsuIdtDtl> params) throws Exception {
		return super.findRows("SELECT id,report_date,prod_cd,prod_nm,scr_cd,scr_nm,asst_thr_knd,isu_org_nm,isu_org_idt,isu_org_vol,mkt_vol FROM dws_ast_isu_idt_dtl", params);
	}

	public UpdateResult addDwsAstIsuIdtDtl(SqlParam<DwsAstIsuIdtDtl> params) throws Exception {
		return super.update("INSERT INTO dws_ast_isu_idt_dtl(id,report_date,prod_cd,prod_nm,scr_cd,scr_nm,asst_thr_knd,isu_org_nm,isu_org_idt,isu_org_vol,mkt_vol) VALUES($AUTOIDI{id},$S{reportDate},$S{prodCd},$S{prodNm},$S{scrCd},$S{scrNm},$S{asstThrKnd},$S{isuOrgNm},$S{isuOrgIdt},$S{isuOrgVol},$D{mktVol})",
				params.getModel());
	}
	
	public UpdateResult updateDwsAstIsuIdtDtl(SqlParam<DwsAstIsuIdtDtl> params) throws Exception {
		return super.update("UPDATE dws_ast_isu_idt_dtl SET report_date=$S{reportDate} ,prod_cd=$S{prodCd} ,prod_nm=$S{prodNm} ,scr_cd=$S{scrCd} ,scr_nm=$S{scrNm} ,asst_thr_knd=$S{asstThrKnd} ,isu_org_nm=$S{isuOrgNm} ,isu_org_idt=$S{isuOrgIdt} ,isu_org_vol=$S{isuOrgVol} ,mkt_vol=$D{mktVol}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteDwsAstIsuIdtDtl(SqlParam<DwsAstIsuIdtDtl> params) throws Exception {
		return super.update("DELETE FROM dws_ast_isu_idt_dtl WHERE  id=$I{id} ",
				params.getModel());
	}

	/**
	 * 根据处理日期删除数据
	 * @param dealDate 处理日期
	 * @return
	 * @throws Exception
	 */
	public UpdateResult deleteDwsAstIsuIdtDtl(String dealDate) throws Exception {
		return super.update("DELETE FROM dws_ast_isu_idt_dtl WHERE  REPORT_DATE = $S{dealDate}", dealDate);
	}

}
