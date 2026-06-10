package com.kayak.pms.prod.dao;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.prod.model.ProdShareSortDetail;
import com.kayak.utils.CamelCaseMapUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;
import java.util.Map;

@Repository
public class ProdShareSortDetailDao extends ComnDao {

	public SqlResult<Map<String, Object>> findProdShareSortDetails(SqlParam<ProdShareSortDetail> params) throws Exception {
		String sql = "SELECT\n" +
				"\ta.id,\n" +
				"\ta.t8_prod_share_sort_info_id,\n" +
				"\ta.distributor_code,\n" +
				"\ta.occur_event,\n" +
				"\ta.occur_date,\n" +
				"\ta.subs_vol,\n" +
				"\ta.subs_amt,\n" +
				"\ta.redeem_vol,\n" +
				"\ta.redeem_amt,\n" +
				"\ta.fee_type,\n" +
				"\ta.fee_money,\n" +
				"\ta.cur,\n" +
				"\ta.remark,\n" +
				"\tc.prod_code,\n" +
				"\td.share_name," +
				" sNames.itemval real_share_name" +
				" FROM t8_prod_share_sort_detail a " +
				" join t8_prod_share_sort_info b on a.t8_prod_share_sort_info_id = b.id\n" +
				" join t8_prod_info c on b.t8_prod_info_id = c.id\n" +
				" join t8_prod_share_sort d on b.t8_prod_share_sort_id = d.id " +
				" left join sys_dict_item sNames" +
				"   on d.share_name=sNames.itemkey" +
				"  and sNames.dict='t8_share_name'" +
				" where a.t8_prod_share_sort_info_id = $S{t8ProdShareSortInfoId}";
		List<SqlRow> rows = super.findRows(sql, params.getModel());
		return CamelCaseMapUtils.CamelCaseSqlRow(rows);
	}

	public UpdateResult addProdShareSortDetail(SqlParam<ProdShareSortDetail> params) throws Exception {
		return super.update("INSERT INTO t8_prod_share_sort_detail(id,cur,t8_prod_share_sort_info_id,distributor_code,occur_event,occur_date,subs_vol,subs_amt,redeem_vol,redeem_amt,fee_type,fee_money,remark,inputuser,crt_date,crt_time,upt_date,upt_time) VALUES($AUTOIDS{id},$S{cur},$S{t8ProdShareSortInfoId},$S{distributorCode},$S{occurEvent},$S{occurDate},$D{subsVol},$D{subsAmt},$D{redeemVol},$D{redeemAmt},$S{feeType},$D{feeMoney},$S{remark},$S{inputuser},$S{crtDate},$S{crtTime},$S{uptDate},$S{uptTime})",
				params.getModel());
	}
	
	public UpdateResult updateProdShareSortDetail(SqlParam<ProdShareSortDetail> params) throws Exception {
		return super.update("UPDATE t8_prod_share_sort_detail SET t8_prod_share_sort_info_id=$S{t8ProdShareSortInfoId} ,distributor_code=$S{distributorCode} ,occur_event=$S{occurEvent} ,cur=$S{cur},occur_date=$S{occurDate} ,subs_vol=$D{subsVol} ,subs_amt=$D{subsAmt} ,redeem_vol=$D{redeemVol} ,redeem_amt=$D{redeemAmt} ,fee_type=$S{feeType} ,fee_money=$D{feeMoney} ,remark=$S{remark} ,upt_date=$S{uptDate} ,upt_time=$S{uptTime}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteProdShareSortDetail(SqlParam<ProdShareSortDetail> params) throws Exception {
		return super.update("DELETE FROM t8_prod_share_sort_detail WHERE  id=$S{id} ",
				params.getModel());
	}

}
