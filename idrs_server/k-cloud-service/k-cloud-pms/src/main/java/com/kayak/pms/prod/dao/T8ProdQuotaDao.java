package com.kayak.pms.prod.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.prod.model.T8ProdQuota;
import com.kayak.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class T8ProdQuotaDao extends ComnDao {
	/**
	 * 功能：查询总额度信息
	 * 作者：rennannan
	 * 日期：20210222
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public SqlResult<Map<String, Object>> findT8ProdQuotas(Map<String, Object> parameters) throws Exception {
		String sql = "select quota.id,quota.prod_code,prod.prod_name,quota.quota_date,prod.id t8_prod_info_id," +
				" (quota.total_sale_quota/100000000) total_sale_quota,quota.crt_user,quota.upd_user,quota.crt_date,quota.upd_date," +
				" quota.crt_time,quota.upd_time,quota.confirm_status," +
				" quota.decision_type,quota.meeting_id,users.username crt_user_name,stat.itemval confirm_status_name,meet.meeting_name " +
				" from t8_prod_quota quota " +
				"left join t8_prod_info prod" +
				"  on quota.prod_code = prod.prod_code " +
				"left join sys_user users " +
				" on quota.crt_user = users.userid " +
				"left join sys_dict_item stat" +
				"   on quota.confirm_status = stat.itemkey " +
				"and stat.dict = 'confirm_status' " +
				"left join t8_quota_meeting meet" +
				" on quota.meeting_id = meet.id where 1 = 1 ";
		if (StringUtils.isNotBlank((String) parameters.get("prodName"))) {
		    sql = sql + " and prod.prod_name like '%" + parameters.get("prodName") + "%'";
		}
		if(StringUtils.isNotBlank((String) parameters.get("prodCode"))){
			sql = sql + " and quota.prod_code = $S{prodCode}";
		}
		if(StringUtils.isNotBlank((String) parameters.get("startEstablishDate"))){
			sql = sql + " and quota.quota_date>=$S{startEstablishDate}";
		}
		if(StringUtils.isNotBlank((String) parameters.get("endEstablishDate"))){
			sql = sql + " and quota.quota_date<=$S{endEstablishDate}";
		}

		return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, parameters, this);
	}

	/**
	 * 功能：根据产品代码、日期、状态查询总额度信息
	 * 作者：rennannan
	 * 日期：20210422
	 *
	 * @param quota
	 * @return
	 * @throws Exception
	 */
	public List<T8ProdQuota> findQuotaList(T8ProdQuota quota) throws Exception {
		String sql = "select id,prod_code,quota_date,(total_sale_quota/100000000) total_sale_quota,crt_user," +
				" upd_user,crt_date,upd_date,crt_time,upd_time,confirm_status,decision_type,meeting_id" +
				" from t8_prod_quota" +
				" where prod_code=$S{prodCode} " +
				"      and quota_date=$S{quotaDate}" +
				"      and confirm_status=$S{confirmStatus}";
		return super.findRows(T8ProdQuota.class, sql, 0, quota);
	}

	/**
	 * 功能：根据产品代码、日期、状态查询总额度信息
	 * 作者：rennannan
	 * 日期：20210422
	 *
	 * @param quota
	 * @return
	 * @throws Exception
	 */
	public List<T8ProdQuota> findQuotaByCodeDate(T8ProdQuota quota) throws Exception {
		String sql = "select id,prod_code,quota_date,(total_sale_quota/100000000) total_sale_quota,crt_user," +
				" upd_user,crt_date,upd_date,crt_time,upd_time,confirm_status,decision_type,meeting_id" +
				" from t8_prod_quota" +
				" where prod_code=$S{prodCode} " +
				"      and quota_date=$S{quotaDate}";
		return super.findRows(T8ProdQuota.class, sql, 0, quota);
	}

	/**
	 * 功能：根据id查询产品当前剩余额度
	 * 作者：rennannan
	 * 日期：20210326
	 *
	 * @return
	 */
	public SqlResult<T8ProdQuota> findProdRemainQuotas(SqlParam<T8ProdQuota> params) throws Exception {
		String sql = "select (total_sale_quota -(select sum(quota) from t8_distributor_quota_manage where total_quota_id = $S{id}))/100000000 as remain_quota\n" +
				"  from t8_prod_quota \n" +
				" where id  = $S{id}";
		return super.findRows(sql, params);
	}

	/**
	 * 功能：插入总额度信息    传入map类型参数
	 * 作者：rennannan
	 * 日期：20210316
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	public int addT8ProdQuotaMap(T8ProdQuota t8ProdQuota) throws Exception {
		return super.update("INSERT INTO t8_prod_quota(id,prod_code,quota_date,total_sale_quota," +
						" crt_user,crt_date,crt_time,confirm_status,decision_type,meeting_id,date_type) " +
						" VALUES($AUTOIDS{id},$S{prodCode},$S{quotaDate},$D{totalSaleQuota}*100000000," +
						" $S{crtUser},$S{crtDate},$S{crtTime},$S{confirmStatus},$S{decisionType},$S{meetingId},$S{dateType})",
				t8ProdQuota).getEffect();
	}

	//检查新增是否重复
	public boolean checkAddT8ProdQuota(T8ProdQuota t8ProdQuota) throws Exception {
		SqlRow row = super.findRow("select count(1) c from t8_prod_quota q where q.prod_code=$S{prodCode} and q.quota_date=$S{quotaDate}", t8ProdQuota);
		if (row.getInteger("c") == 0) {
			return true;
		}
		return false;
	}

	//检查修改是否重复 rennannan 20210425
	public boolean checkUpdateExists(T8ProdQuota t8ProdQuota) throws Exception {
		SqlRow row = super.findRow("select count(1) c from t8_prod_quota q where q.prod_code=$S{prodCode} and q.quota_date=$S{quotaDate} and id !=$S{id}", t8ProdQuota);
		if (row.getInteger("c") == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 功能：修改总额度需求信息  传入参数类型为map
	 * 作者：rennannan
	 * 日期：20210316
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	public int updateT8ProdQuotaMap(Map<String, Object> map) throws Exception {
		return super.update("update t8_prod_quota set prod_code=$S{prodCode} ," +
						" quota_date=$S{quotaDate} ," +
						" total_sale_quota=$D{totalSaleQuota}*100000000 ," +
						" upd_user=$S{updUser} ," +
						" upd_date=$S{updDate}," +
						" upd_time=$S{updTime}," +
						" confirm_status=$S{confirmStatus}," +
						" decision_type= $S{decisionType}," +
						" meeting_id= $S{meetingId} " +
						" where id=$S{id}",
				map).getEffect();
	}

	/**
	 * 功能：删除产品总额度  传入参数为map
	 * 作者：rennannan
	 * 日期：20210316
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public UpdateResult deleteT8ProdQuotaMap(Map<String, Object> map) throws Exception {
		return super.update("delete from t8_prod_quota where id=$S{id}",
				map);
	}

	/**
	 * 功能：根据id改变总额度状态
	 * 作者：rennannan
	 * 日期：20210422
	 *
	 * @param t8ProdQuota
	 * @return
	 * @throws Exception
	 */
	public int updateStatusById(T8ProdQuota t8ProdQuota) throws Exception {
		return super.update("update t8_prod_quota " +
				" set confirm_status=$S{confirmStatus} " +
				" where id=$S{id}",t8ProdQuota).getEffect();
	}
}
