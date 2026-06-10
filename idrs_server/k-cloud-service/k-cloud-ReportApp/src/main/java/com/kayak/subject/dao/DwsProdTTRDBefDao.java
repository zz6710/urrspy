package com.kayak.subject.dao;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.subject.model.DwsProdTTRDBef;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;
import java.util.Map;

@Repository
public class DwsProdTTRDBefDao extends ComnDao {

	public SqlResult<DwsProdTTRDBef> findDwsProdTTRDBefs(SqlParam<DwsProdTTRDBef> params) throws Exception {
		return super.findRows("SELECT id,product_code,i_code as icode,asset_third_type,i_name as iname,amount,changerate,investedamountcny,org_classific,orderfreemanage,new_classific,isoverdue,cashtodomain,vcintfund,govintfund,isnostandard,bondrating,specialbond,inmarketornot,cashtogovernment,cashtopublic,cashtorelateproduct,moneyofoverdueasset,moneyofproperty,secondlevelcaptialbond,continuebondforever,seniorbond,convertbond,otherbanksupplementtools,assettype,deal_date,report_date,csh_mng_f,hldn_qntt,pen_inv_f,per_pen_inv_f,hk_inv,qdii_inv,recvbl_prnc FROM dws_prod_ttrd_bef_g06a2", params);
	}

	public UpdateResult addDwsProdTTRDBef(SqlParam<DwsProdTTRDBef> params) throws Exception {
		return super.update("INSERT INTO dws_prod_ttrd_bef_g06a2(id,product_code,i_code,asset_third_type,i_name,amount,changerate,investedamountcny,org_classific,orderfreemanage,new_classific,isoverdue,cashtodomain,vcintfund,govintfund,isnostandard,bondrating,specialbond,inmarketornot,cashtogovernment,cashtopublic,cashtorelateproduct,moneyofoverdueasset,moneyofproperty,secondlevelcaptialbond,continuebondforever,seniorbond,convertbond,otherbanksupplementtools,assettype,deal_date,report_date,csh_mng_f,pen_inv_f,per_pen_inv_f) VALUES($AUTOIDI{id},$S{productCode},$S{icode},$S{assetThirdType},$S{iname},$D{amount},$D{changerate},$D{investedamountcny},$S{orgClassific},$S{orderfreemanage},$S{newClassific},$S{isoverdue},$S{cashtodomain},$S{vcintfund},$S{govintfund},$S{isnostandard},$S{bondrating},$S{specialbond},$S{inmarketornot},$S{cashtogovernment},$D{cashtopublic},$D{cashtorelateproduct},$D{moneyofoverdueasset},$D{moneyofproperty},$S{secondlevelcaptialbond},$S{continuebondforever},$S{seniorbond},$S{convertbond},$S{otherbanksupplementtools},$S{assettype},$S{dealDate},$S{reportDate},$S{cshMngF},$S{penInvF},$S{perPenInvF})",
				params.getModel());
	}
	
	public UpdateResult updateDwsProdTTRDBef(SqlParam<DwsProdTTRDBef> params) throws Exception {
		return super.update("UPDATE dws_prod_ttrd_bef_g06a2 SET product_code=$S{productCode} ,i_code=$S{icode} ,asset_third_type=$S{assetThirdType} ,i_name=$S{iname} ,amount=$D{amount} ,changerate=$D{changerate} ,investedamountcny=$D{investedamountcny} ,org_classific=$S{orgClassific} ,orderfreemanage=$S{orderfreemanage} ,new_classific=$S{newClassific} ,isoverdue=$S{isoverdue} ,cashtodomain=$S{cashtodomain} ,vcintfund=$S{vcintfund} ,govintfund=$S{govintfund} ,isnostandard=$S{isnostandard} ,bondrating=$S{bondrating} ,specialbond=$S{specialbond} ,inmarketornot=$S{inmarketornot} ,cashtogovernment=$S{cashtogovernment} ,cashtopublic=$D{cashtopublic} ,cashtorelateproduct=$D{cashtorelateproduct} ,moneyofoverdueasset=$D{moneyofoverdueasset} ,moneyofproperty=$D{moneyofproperty} ,secondlevelcaptialbond=$S{secondlevelcaptialbond} ,continuebondforever=$S{continuebondforever} ,seniorbond=$S{seniorbond} ,convertbond=$S{convertbond} ,otherbanksupplementtools=$S{otherbanksupplementtools} ,assettype=$S{assettype} ,deal_date=$S{dealDate} ,report_date=$S{reportDate} ,csh_mng_f=$S{cshMngF},pen_inv_f=$S{penInvF} ,per_pen_inv_f=$S{perPenInvF}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteDwsProdTTRDBef(SqlParam<DwsProdTTRDBef> params) throws Exception {
		return super.update("DELETE FROM dws_prod_ttrd_bef_g06a2 WHERE  id=$I{id} ",
				params.getModel());
	}

	public UpdateResult deleteDwsProdTTRDBef(DwsProdTTRDBef params) throws Exception {
		return super.update("DELETE FROM dws_prod_ttrd_bef_g06a2 WHERE report_date = $S{reportDate}",
				params);
	}

	public List<SqlRow> findT8SqlParamInfos(Map<String, String> params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT id,code,sqlstr,data_type,remark,status FROM base_port_sql_param_info where 1=1");
		if (Tools.isNotEmpty(params.get("code"))) {
			sql.append(" and code like '%").append(params.get("code")).append("%'");
		}
		if (Tools.isNotEmpty(params.get("status"))) {
			sql.append(" and status = '").append(params.get("status")).append("'");
		}
		return super.findRows(sql.toString(), params);
	}

}
