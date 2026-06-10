package com.kayak.dps.valtabimp.service;

import com.kayak.dps.valtabimp.model.ValReadassets;
import com.kayak.dps.valtabimp.repository.ValReadassetsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;


@Service
@APIDefine(desc = "ValReadassetsDesc服务", model = ValReadassets.class)
public class ValReadassetsService {

	@Autowired
	private ValReadassetsDao valReadassetsDao;

	@API(desc = "查询ValReadassetsDesc信息", auth = APIAuth.YES)
	public SqlResult<ValReadassets> findValReadassetss(SqlParam<ValReadassets> params) throws Exception {
		params.setMakeSql(true);
		return valReadassetsDao.findValReadassetss(params);
	}

	@API(desc = "添加ValReadassetsDesc", params = "id,t8_prod_base_id,change_date,ftool_code,ftool_name,t8_sys_adtype_id,market,account_type,positionbln,principalbln,interestbln,accruedincomebln,npamountbln,feepaybln,fairvaluebln,taxfeebln,pay_taxbln,accruedpaybln,securitiesliquidationbln,jrjs_value,inputuser,crt_date,crt_time,isprodorasset,prod_name,balance", auth = APIAuth.NO)
	public int addValReadassets(SqlParam<ValReadassets> params) throws Exception {
		return valReadassetsDao.addValReadassets(params).getEffect();
	}
	
	@API(desc = "修改ValReadassetsDesc", params = "id,t8_prod_base_id,change_date,ftool_code,ftool_name,t8_sys_adtype_id,market,account_type,positionbln,principalbln,interestbln,accruedincomebln,npamountbln,feepaybln,fairvaluebln,taxfeebln,pay_taxbln,accruedpaybln,securitiesliquidationbln,jrjs_value,inputuser,crt_date,crt_time,isprodorasset,prod_name,balance", auth = APIAuth.NO)
	public int updateValReadassets(SqlParam<ValReadassets> params) throws Exception {
		return valReadassetsDao.updateValReadassets(params).getEffect();
	}
	
	@API(desc = "删除ValReadassetsDesc", params = "id,t8_prod_base_id,change_date,ftool_code,ftool_name,t8_sys_adtype_id,market,account_type,positionbln,principalbln,interestbln,accruedincomebln,npamountbln,feepaybln,fairvaluebln,taxfeebln,pay_taxbln,accruedpaybln,securitiesliquidationbln,jrjs_value,inputuser,crt_date,crt_time,isprodorasset,prod_name,balance", auth = APIAuth.NO)
	public int deleteValReadassets(SqlParam<ValReadassets> params) throws Exception {
		return valReadassetsDao.deleteValReadassets(params).getEffect();
	}

}
