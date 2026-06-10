package com.kayak.dps.valtabimp.repository;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.valtabimp.model.OdsReadAssetsReport;
import com.kayak.dps.valtabimp.model.ValReadassetsMerge;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;

import java.util.Map;

@Repository
public class ValReadassetsMergeDao extends ComnDao {

	public SqlResult<ValReadassetsMerge> findAssetCode (SqlParam<ValReadassetsMerge> params) throws Exception {
		String sql = " SELECT DISTINCT asset_code FROM ods_fa_readassets_prodreportrel "+
		"where 1 = 1 ";
		ValReadassetsMerge o = params.getModel();
		if (StringUtils.isNotBlank(o.getT8ValReporttabId())){
			sql += " and t8_val_reporttab_id = $S{t8ValReporttabId}";
		}
		return super.findRows(sql, params);
	}

	public SqlResult<ValReadassetsMerge> findValReadassetsMerges(SqlParam<ValReadassetsMerge> params) throws Exception {
		ValReadassetsMerge v = params.getModel();

		String sql = "SELECT t1.id,t1.asset_code,t1.change_date,t1.ftool_code,t1.ftool_name,t1.t8_sys_adtype_id,t1.market,t1.account_type," +
				"t1.positionbln,t1.principalbln,t1.interestbln,t1.accruedincomebln,t1.npamountbln,t1.feepaybln,t1.fairvaluebln,t1.taxfeebln,t1.pay_taxbln," +
				"t1.accruedpaybln,t1.securitiesliquidationbln,t1.jrjs_value,t1.inputuser,t1.crt_date,t1.crt_time,t1.isprodorasset,t1.prod_name,t1.balance, " +
				"t2.ad_name,t4.reporttab_name " +
				"FROM ods_fa_readassets_merge t1 " +
				"left join base_sys_adtype t2 on t1.t8_sys_adtype_id = t2.id " +
				"left join ods_fa_readassets_prodreportrel t3 on t1.asset_code = t3.asset_code " +
				"left join base_fa_reporttab t4 on t3.t8_val_reporttab_id = t4.id ";
		return super.findRows(sql, params);
	}

	public UpdateResult addValReadassetsMerge(SqlParam<ValReadassetsMerge> params) throws Exception {
		return super.update("INSERT INTO ods_fa_readassets_merge(id,t8_prod_base_id,change_date,ftool_code,ftool_name,t8_sys_adtype_id,market,account_type,positionbln,principalbln,interestbln,accruedincomebln,npamountbln,feepaybln,fairvaluebln,taxfeebln,pay_taxbln,accruedpaybln,securitiesliquidationbln,jrjs_value,inputuser,crt_date,crt_time,isprodorasset,prod_name,balance) VALUES($AUTOIDI{id},$I{t8ProdBaseId},$S{changeDate},$S{ftoolCode},$S{ftoolName},$I{t8SysAdtypeId},$S{market},$S{accountType},$S{positionbln},$S{principalbln},$S{interestbln},$S{accruedincomebln},$S{npamountbln},$S{feepaybln},$S{fairvaluebln},$S{taxfeebln},$S{payTaxbln},$S{accruedpaybln},$S{securitiesliquidationbln},$S{jrjsValue},$S{inputuser},$S{crtDate},$S{crtTime},$S{isprodorasset},$S{prodName},$S{balance})",
				params.getModel());
	}
	
	public UpdateResult updateValReadassetsMerge(SqlParam<ValReadassetsMerge> params) throws Exception {
		return super.update("UPDATE ods_fa_readassets_merge SET t8_prod_base_id=$I{t8ProdBaseId} ,change_date=$S{changeDate} ,ftool_code=$S{ftoolCode} ,ftool_name=$S{ftoolName} ,t8_sys_adtype_id=$I{t8SysAdtypeId} ,market=$S{market} ,account_type=$S{accountType} ,positionbln=$S{positionbln} ,principalbln=$S{principalbln} ,interestbln=$S{interestbln} ,accruedincomebln=$S{accruedincomebln} ,npamountbln=$S{npamountbln} ,feepaybln=$S{feepaybln} ,fairvaluebln=$S{fairvaluebln} ,taxfeebln=$S{taxfeebln} ,pay_taxbln=$S{payTaxbln} ,accruedpaybln=$S{accruedpaybln} ,securitiesliquidationbln=$S{securitiesliquidationbln} ,jrjs_value=$S{jrjsValue} ,inputuser=$S{inputuser} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,isprodorasset=$S{isprodorasset} ,prod_name=$S{prodName} ,balance=$S{balance}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteValReadassetsMerge(SqlParam<ValReadassetsMerge> params) throws Exception {
		return super.update("DELETE FROM ods_fa_readassets_merge WHERE  id=$I{id} ",
				params.getModel());
	}

    public UpdateResult batchDeleteValReadassetsMerge(Map<String, Object> params) throws Exception {
		return super.update("DELETE FROM ods_fa_readassets_merge WHERE  id=$I{id} ",params);
    }

	public SqlResult<ValReadassetsMerge> findisprodorassetList (SqlParam<ValReadassetsMerge> params) throws Exception {
		String sql = " SELECT itemkey isprodorasset,itemval  isprodorassetname FROM sys_dict_item t1";
		ValReadassetsMerge o = params.getModel();
		if (StringUtils.isNotBlank(o.getT8ValReporttabId())){
			sql += " inner join ods_fa_readassets_prodreportrel t2 on t1.itemkey = t2.isprodorasset and t2.t8_val_reporttab_id = $S{t8ValReporttabId}";
		}
		sql+=" where t1.dict ='base_isprodorasset'";
		return super.findRows(sql, params);
	}
}
