package com.kayak.subject.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.subject.model.DwsAstAllocationDtl;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class DwsAstAllocationDtlDao extends ComnDao {

	public SqlResult<DwsAstAllocationDtl> findDwsAstAllocationDtls(SqlParam<DwsAstAllocationDtl> params) throws Exception {
		return super.findRows("SELECT id,report_date,prod_cd,prod_nm,scr_cd,scr_nm,fin_deb_sec,non_fin_deb_sec,rat,non_fin_lab,bank_cap_sup,mkt_vol FROM dws_ast_allocation_dtl", params);
	}

	public UpdateResult addDwsAstAllocationDtl(SqlParam<DwsAstAllocationDtl> params) throws Exception {
		return super.update("INSERT INTO dws_ast_allocation_dtl(id,report_date,prod_cd,prod_nm,scr_cd,scr_nm,fin_deb_sec,non_fin_deb_sec,rat,non_fin_lab,bank_cap_sup,mkt_vol) VALUES($AUTOIDI{id},$S{reportDate},$S{prodCd},$S{prodNm},$S{scrCd},$S{scrNm},$S{finDebSec},$S{nonFinDebSec},$S{rat},$S{nonFinLab},$S{bankCapSup},$D{mktVol})",
				params.getModel());
	}
	
	public UpdateResult updateDwsAstAllocationDtl(SqlParam<DwsAstAllocationDtl> params) throws Exception {
		return super.update("UPDATE dws_ast_allocation_dtl SET report_date=$S{reportDate} ,prod_cd=$S{prodCd} ,prod_nm=$S{prodNm} ,scr_cd=$S{scrCd} ,scr_nm=$S{scrNm} ,fin_deb_sec=$S{finDebSec} ,non_fin_deb_sec=$S{nonFinDebSec} ,rat=$S{rat} ,non_fin_lab=$S{nonFinLab} ,bank_cap_sup=$S{bankCapSup} ,mkt_vol=$D{mktVol}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteDwsAstAllocationDtl(SqlParam<DwsAstAllocationDtl> params) throws Exception {
		return super.update("DELETE FROM dws_ast_allocation_dtl WHERE  id=$I{id} ",
				params.getModel());
	}

	public UpdateResult deleteDwsAstAllocationDtl(String dealDate) throws Exception{
		return super.update("DELETE FROM dws_ast_allocation_dtl WHERE  report_date = $S{dealDate} ", dealDate);
	}
}
