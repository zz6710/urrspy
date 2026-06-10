package com.kayak.dps.valtabimp.repository;

import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.valtabimp.model.ValReadassets;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class ValReadassetsDao extends ComnDao {

	public SqlResult<ValReadassets> findValReadassetss(SqlParam<ValReadassets> params) throws Exception {
		return super.findRows("SELECT id,t8_prod_base_id,change_date,ftool_code,ftool_name,t8_sys_adtype_id,market,account_type,positionbln,principalbln,interestbln,accruedincomebln,npamountbln,feepaybln,fairvaluebln,taxfeebln,pay_taxbln,accruedpaybln,securitiesliquidationbln,jrjs_value,inputuser,crt_date,crt_time,isprodorasset,prod_name,balance FROM ods_fa_readassets", params);
	}

	public UpdateResult addValReadassets(SqlParam<ValReadassets> params) throws Exception {
		return super.update("INSERT INTO ods_fa_readassets(id,t8_prod_base_id,change_date,ftool_code,ftool_name,t8_sys_adtype_id,market,account_type,positionbln,principalbln,interestbln,accruedincomebln,npamountbln,feepaybln,fairvaluebln,taxfeebln,pay_taxbln,accruedpaybln,securitiesliquidationbln,jrjs_value,inputuser,crt_date,crt_time,isprodorasset,prod_name,balance) VALUES($AUTOIDI{id},$I{t8ProdBaseId},$S{changeDate},$S{ftoolCode},$S{ftoolName},$I{t8SysAdtypeId},$S{market},$S{accountType},$S{positionbln},$S{principalbln},$S{interestbln},$S{accruedincomebln},$S{npamountbln},$S{feepaybln},$S{fairvaluebln},$S{taxfeebln},$S{payTaxbln},$S{accruedpaybln},$S{securitiesliquidationbln},$S{jrjsValue},$S{inputuser},$S{crtDate},$S{crtTime},$S{isprodorasset},$S{prodName},$S{balance})",
				params.getModel());
	}
	
	public UpdateResult updateValReadassets(SqlParam<ValReadassets> params) throws Exception {
		return super.update("UPDATE ods_fa_readassets SET t8_prod_base_id=$I{t8ProdBaseId} ,change_date=$S{changeDate} ,ftool_code=$S{ftoolCode} ,ftool_name=$S{ftoolName} ,t8_sys_adtype_id=$I{t8SysAdtypeId} ,market=$S{market} ,account_type=$S{accountType} ,positionbln=$S{positionbln} ,principalbln=$S{principalbln} ,interestbln=$S{interestbln} ,accruedincomebln=$S{accruedincomebln} ,npamountbln=$S{npamountbln} ,feepaybln=$S{feepaybln} ,fairvaluebln=$S{fairvaluebln} ,taxfeebln=$S{taxfeebln} ,pay_taxbln=$S{payTaxbln} ,accruedpaybln=$S{accruedpaybln} ,securitiesliquidationbln=$S{securitiesliquidationbln} ,jrjs_value=$S{jrjsValue} ,inputuser=$S{inputuser} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,isprodorasset=$S{isprodorasset} ,prod_name=$S{prodName} ,balance=$S{balance}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteValReadassets(SqlParam<ValReadassets> params) throws Exception {
		return super.update("DELETE FROM ods_fa_readassets WHERE  id=$I{id} ",
				params.getModel());
	}

}
