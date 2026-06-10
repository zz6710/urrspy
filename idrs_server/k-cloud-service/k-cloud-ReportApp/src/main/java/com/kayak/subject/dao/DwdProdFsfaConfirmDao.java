package com.kayak.subject.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.subject.model.DwdProdFsfaConfirm;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class DwdProdFsfaConfirmDao extends ComnDao {

	public SqlResult<DwdProdFsfaConfirm> findDwdProdFsfaConfirms(SqlParam<DwdProdFsfaConfirm> params) throws Exception {
		return super.findRows("SELECT id,deal_date,prdc_cd,prdc_nm,dt_dt,gzb_stt,gnrt_tm,opt_naem FROM dwd_prod_fsfa_confirm", params);
	}

	public UpdateResult addDwdProdFsfaConfirm(SqlParam<DwdProdFsfaConfirm> params) throws Exception {
		return super.update("INSERT INTO dwd_prod_fsfa_confirm(id,deal_date,prdc_cd,prdc_nm,dt_dt,gzb_stt,gnrt_tm,opt_naem) VALUES($AUTOIDI{id},$S{dealDate},$S{prdcCd},$S{prdcNm},$S{dtDt},$S{gzbStt},$S{gnrtTm},$S{optNaem})",
				params.getModel());
	}
	
	public UpdateResult updateDwdProdFsfaConfirm(SqlParam<DwdProdFsfaConfirm> params) throws Exception {
		return super.update("UPDATE dwd_prod_fsfa_confirm SET deal_date=$S{dealDate} ,prdc_cd=$S{prdcCd} ,prdc_nm=$S{prdcNm} ,dt_dt=$S{dtDt} ,gzb_stt=$S{gzbStt} ,gnrt_tm=$S{gnrtTm} ,opt_naem=$S{optNaem}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteDwdProdFsfaConfirm(SqlParam<DwdProdFsfaConfirm> params) throws Exception {
		return super.update("DELETE FROM dwd_prod_fsfa_confirm WHERE  id=$I{id} ",
				params.getModel());
	}

}
