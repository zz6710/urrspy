package com.kayak.pms.netValue.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.netValue.model.T8ProdNetvalDays;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;

@Repository
public class T8ProdNetvalDaysDao extends ComnDao {

	public SqlResult<T8ProdNetvalDays> findT8ProdNetvalDayss(SqlParam<T8ProdNetvalDays> params) throws Exception {
		return super.findRows("SELECT id,t8_prod_info_id,netval_date,disclosure_date," +
				" crt_date,crt_time,crt_user_id,crt_user_name," +
				" upd_date,upd_time,upd_user_id,upd_user_name," +
				" status,remark,month,type,netval_date_type" +
				" FROM t8_prod_netval_days", params);
	}

	public UpdateResult addT8ProdNetvalDays(SqlParam<T8ProdNetvalDays> params) throws Exception {
		return super.update("INSERT INTO t8_prod_netval_days(id,t8_prod_info_id,netval_date,disclosure_date," +
						" crt_date,crt_time,crt_user_id,crt_user_name," +
						" upd_date,upd_time,upd_user_id,upd_user_name," +
						" status,remark,type,netval_date_type) " +
						" VALUES( $AUTOIDS{id},$S{t8ProdInfoId},$S{netvalDate},$S{disclosureDate}," +
						" $S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName}," +
						" $S{updDate},$S{updTime},$S{updUserId},$S{updUserName}," +
						" $S{status},$S{remark},$S{type},$S{netvalDateType})",
				params.getModel());
	}

	public void addProdNetvalDays(T8ProdNetvalDays params) throws Exception {
		String sql = "INSERT INTO t8_prod_netval_days(id,t8_prod_info_id,netval_date,disclosure_date," +
				" crt_date,crt_time,crt_user_id,crt_user_name," +
				" upd_date,upd_time,upd_user_id,upd_user_name," +
				" status,remark,month,type,netval_date_type) " +
				" VALUES( $AUTOIDS{id},$S{t8ProdInfoId},$S{netvalDate},$S{disclosureDate}," +
				" $S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName}," +
				" $S{updDate},$S{updTime},$S{updUserId},$S{updUserName}," +
				" $S{status},$S{remark},$S{month},$S{type},$S{netvalDateType})";
		super.update(sql, params);
	}

	public UpdateResult updateT8ProdNetvalDays(SqlParam<T8ProdNetvalDays> params) throws Exception {
		return super.update("  UPDATE t8_prod_netval_days " +
						" SET t8_prod_info_id=$S{t8ProdInfoId} ," +
						" netval_date=$S{netvalDate} ," +
						" disclosure_date=$S{disclosureDate} ," +
						" crt_date=$S{crtDate} ," +
						" crt_time=$S{crtTime} ," +
						" crt_user_id=$S{crtUserId} ," +
						" crt_user_name=$S{crtUserName} ," +
						" upd_date=$S{updDate} ," +
						" upd_time=$S{updTime} ," +
						" upd_user_id=$S{updUserId} ," +
						" upd_user_name=$S{updUserName} ," +
						" status=$S{status} ," +
						" remark=$S{remark}," +
						" month=$S{month}," +
						" type=$S{type}," +
						" netval_date_type= $S{netvalDateType} " +
						" WHERE  id=$S{id} ",
				params.getModel());
	}

	public UpdateResult deleteT8ProdNetvalDays(SqlParam<T8ProdNetvalDays> params) throws Exception {
		return super.update("DELETE FROM t8_prod_netval_days WHERE  id=$S{id} ",
				params.getModel());
	}

	/**
	 * 功能：根据所属月份与类型删除产品净值批量日期数据
	 * 作者：rennannan
	 * 日期：20210621
	 *
	 * @param
	 */
	public void deleteNetvalDaysByMonth(T8ProdNetvalDays param) throws Exception {
		String sql = "DELETE FROM t8_prod_netval_days WHERE  month=$S{month} and type=$S{type}";
		super.update(sql, param);
	}
}
