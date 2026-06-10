package com.kayak.pms.prod.dao;

import com.kayak.core.sql.SqlRow;
import com.kayak.pms.printTemp.model.PrintTemp;
import com.kayak.pms.prod.model.T8ProdQuota;
import com.kayak.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.prod.model.T8DistributorQuotaManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class T8DistributorQuotaManageDao extends ComnDao {

	public SqlResult<T8DistributorQuotaManage> findT8DistributorQuotaManages(SqlParam<T8DistributorQuotaManage> params) throws Exception {
		return super.findRows("select quota.id,quota.prod_code,quota.total_quota_id,quota.distributor_code,quota.prod_sale_custom," +
				" (quota.quota/100000000) quota," +
				" quota.crt_user,quota.crt_date,quota.crt_time,quota.upd_date,quota.upd_time,quota.upd_user," +
				" quota.confirm_user,quota.confirm_date,quota.confirm_time, dis.distributor_type, dis.distributor_name," +
				"                         quota.manager_dept  " +
				" from t8_distributor_quota_manage quota " +
				"left join t8_distributor_info dis  " +
				"        on quota.distributor_code = dis.DISTRIBUTOR_CODE ", params);
	}

	/**
	 * 功能：根据总额度id、状态查询总额度信息
	 * 作者：rennannan
	 * 日期：20210422
	 *
	 * @param quota
	 * @return
	 * @throws Exception
	 */
	public List<T8DistributorQuotaManage> findQuotaList(T8DistributorQuotaManage quota) throws Exception {
		String sql = "select id,prod_code,total_quota_id,distributor_code,manager_dept,prod_sale_custom," +
				" quota,crt_user,crt_date,crt_time,upd_date,upd_time,upd_user,confirm_user,confirm_date," +
				" confirm_time,confirm_status" +
				" from t8_distributor_quota_manage" +
				" where total_quota_id=$S{totalQuotaId} " +
				"      and confirm_status=$S{confirmStatus}";
		return super.findRows(T8DistributorQuotaManage.class, sql, 0, quota);
	}

	/**
	 * 功能:根据总额度id与部门编号查询额度信息
	 * 作者:rennannan
	 * 日期:20210224
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8DistributorQuotaManage> findT8DisQuotaByTotalIdAndDeptNo(SqlParam<T8DistributorQuotaManage> params) throws Exception {
		return super.findRows("select quota.id,quota.prod_code,quota.total_quota_id,quota.distributor_code,quota.prod_sale_custom," +
				" (quota.quota/100000000) quota," +
				" quota.crt_user,quota.crt_date,quota.crt_time,quota.upd_date,quota.upd_time,quota.upd_user," +
				" quota.confirm_user,quota.confirm_date,quota.confirm_time, dis.distributor_type, dis.distributor_name," +
				" quota.confirm_status,quota.manager_dept  " +
				" from t8_distributor_quota_manage quota " +
				"left join t8_distributor_info dis  " +
				"        on quota.distributor_code = dis.DISTRIBUTOR_CODE " +
				" where quota.total_quota_id=$S{totalQuotaId} " +
				"   and quota.MANAGER_DEPT = $S{managerDept}" +
				" order by quota.crt_date,crt_time", params);
	}

	/**
	 * 功能：根据部门id与总额度id查询部门总额度
	 * 作者：rennannan
	 * 日期：20210326
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8DistributorQuotaManage> findDeptTotalQuota(SqlParam<T8DistributorQuotaManage> params) throws Exception {
		String sql = "select sum(quota)/100000000 dept_total_quota\n" +
				"      from t8_distributor_quota_manage\n" +
				"\t   where total_quota_id = $S{totalQuotaId} and MANAGER_DEPT = $S{managerDept}";
		return super.findRows(sql, params);
	}
	/**
	 * 功能：根据部门编号查询与总额度id查询销售商额度需求列表
	 * 作者：rennannan
	 * 日期：20210223
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8DistributorQuotaManage> findQuotaListByDeptNo(SqlParam<T8DistributorQuotaManage> params) throws Exception {
		StringBuffer sql = new StringBuffer("select quota.distributor_code,quota.prod_sale_custom,(quota.quota/100000000) quota ," +
				"  	       quota.crt_user,quota.crt_date,quota.crt_time,users.username crt_user_name, distri.distributor_name," +
				"          quota.confirm_status,quota.confirm_date,quota.confirm_time,quota.manager_dept " +
				"     from t8_distributor_quota_manage quota " +
				"left join t8_distributor_info distri" +
				"       on quota.distributor_code = distri.DISTRIBUTOR_CODE" +
				" left join sys_user users" +
				"       on quota.crt_user = users.userid " +
				"    where quota.total_quota_id = $S{totalQuotaId}" +
				"      and quota.MANAGER_DEPT=$S{managerDept}");
		if (StringUtils.isNotEmpty(params.getModel().getStatus())) {
			sql.append(" and quota.confirm_status >= $S{status}");
		}
		sql.append("  order by quota.crt_date,crt_time");
		return super.findRows(sql.toString(), params);
	}

	/**
	 * 功能：根据部门编号查询与总额度id、状态查询销售商额度需求列表
	 * 作者：rennannan
	 * 日期：20210422
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8DistributorQuotaManage> findQuotaByTotalDeptId(SqlParam<T8DistributorQuotaManage> params) throws Exception {
		String sql = "select quota.distributor_code,quota.prod_sale_custom,(quota.quota/100000000) quota ," +
				"  	       quota.crt_user,quota.crt_date,quota.crt_time,users.username crt_user_name, distri.distributor_name," +
				"          quota.confirm_status,quota.confirm_date,quota.confirm_time,quota.manager_dept " +
				"     from t8_distributor_quota_manage quota " +
				"left join t8_distributor_info distri" +
				"       on quota.distributor_code = distri.DISTRIBUTOR_CODE" +
				" left join sys_user users" +
				"       on quota.crt_user = users.userid " +
				"    where quota.total_quota_id = $S{totalQuotaId}" +
				"      and quota.MANAGER_DEPT=$S{managerDept}" +
				"      and quota.confirm_status=$S{confirmStatus}" +
				"  order by quota.crt_date,crt_time";
		return super.findRows(sql, params);
	}

	/**
	 * 功能:统计查询各部门额度信息
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8DistributorQuotaManage> findTotalDeptQuota(SqlParam<T8DistributorQuotaManage> params) throws Exception {
		StringBuffer sql1 = new StringBuffer(" select disQuato.MANAGER_DEPT,(sum(disQuato.quota)/100000000) total_Dept_Quota,disQuato.total_quota_id," +
				"ti.id t8_prod_info_id" +
				"	   from t8_distributor_quota_manage disQuato" +
				" left join t8_prod_info ti on ti.prod_code = disQuato.prod_code" +
				"     where disQuato.total_quota_id=$S{totalQuotaId}");
		if (StringUtils.isNotEmpty(params.getModel().getStatus())) {
			sql1.append(" and disQuato.confirm_status >= $S{status}");
		}
		sql1.append("group by disQuato.MANAGER_DEPT,disQuato.total_quota_id,ti.id");
		;


		return super.findRows(sql1.toString(), params);
	}

	/**
	 * 功能:根据总额度id与状态统计查询各部门额度信息
	 * 作者：rennannan
	 * 日期：20210422
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8DistributorQuotaManage> findConfirmTotalDeptQuota(SqlParam<T8DistributorQuotaManage> params) throws Exception {
		String sql = " select disQuato.MANAGER_DEPT,(sum(disQuato.quota)/100000000) total_Dept_Quota,disQuato.total_quota_id," +
				"ti.id t8_prod_info_id" +
				"	   from t8_distributor_quota_manage disQuato" +
				" left join t8_prod_info ti on ti.prod_code = disQuato.prod_code" +
				"     where disQuato.total_quota_id=$S{totalQuotaId} and disQuato.confirm_status=$S{confirmStatus}" +
				"  group by disQuato.MANAGER_DEPT,disQuato.total_quota_id,ti.id";
		return super.findRows(sql, params);
	}

	/**
	 * 功能:插入销售商额度需求信息
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public UpdateResult addT8DistributorQuotaManage(T8DistributorQuotaManage params) throws Exception {
		return super.update("insert into t8_distributor_quota_manage(id,prod_code,total_quota_id,distributor_code,prod_sale_custom,quota," +
						" crt_user,crt_date,crt_time,confirm_status,manager_dept) " +
						" values($AUTOIDS{id},$S{prodCode},$S{totalQuotaId},$S{distributorCode},$S{prodSaleCustom}, $D{quota}*100000000," +
						" $S{crtUser},$S{crtDate},$S{crtTime},$S{confirmStatus},$S{managerDept})",
				params);
	}

	/**
	 * 功能:修改销售商额度需求
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateT8DistributorQuotaManage(T8DistributorQuotaManage params) throws Exception {
		return super.update("update t8_distributor_quota_manage " +
						" set prod_code=$S{prodCode}," +
						" total_quota_id=$S{totalQuotaId}," +
						" distributor_code=$S{distributorCode} ," +
						" quota=$D{quota}*100000000 ," +
						" prod_sale_custom=$S{prodSaleCustom} ," +
						" upd_date=$S{updDate} ," +
						" upd_time=$S{updTime}," +
						" upd_user=$S{updUser}," +
						" confirm_status=$S{confirmStatus}," +
						"               manager_dept=$S{managerDept} " +
						" where  id=$S{id} ",
				params).getEffect();
	}

	/**
	 * 功能:修改销售商额度需求状态
	 * 作者：rennannan
	 * 日期：20210423
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateT8DisQuotaStatus(T8DistributorQuotaManage params) throws Exception {
		return super.update("update t8_distributor_quota_manage " +
						" set confirm_status=$S{confirmStatus}" +
						" where  id=$S{id} ",
				params).getEffect();
	}

	/**
	 * 功能：根据id删除销售商额度需求信息
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public UpdateResult deleteT8DistributorQuotaManage(SqlParam<T8DistributorQuotaManage> params) throws Exception {
		return super.update("delete from t8_distributor_quota_manage where  id=$S{id} ",
				params.getModel());
	}
	/**
	 * 功能：根据总额度id删除销售商额度需求信息
	 * @param
	 * @return
	 * @throws Exception
	 */
	public int deleteQuotaManageByTotalId(String totalQuotaId) throws Exception {
		return super.update("delete from t8_distributor_quota_manage where  total_quota_id=$S{totalQuotaId} and confirm_status='0'",
				totalQuotaId).getEffect();
	}

	/**
	 * 功能：根据总额度id与部门删除销售商额度需求信息
	 * 作者：rennannan
	 * 日期：20210225
	 */
	public int deleteQuotaByTotalIdAndDept(T8DistributorQuotaManage quota) throws Exception {
		return super.update(" delete from t8_distributor_quota_manage " +
				"    where total_quota_id = $S{totalQuotaId}" +
				"      and MANAGER_DEPT = $S{managerDept} and confirm_status != '3'", quota).getEffect();
	}

	/**
	 * 功能：根据销售商代码查询所属部门 作为页面下拉框展示值
	 * 作者：rennannan
	 * 日期：20210415
	 *
	 * @return
	 */
	public SqlResult<T8DistributorQuotaManage> findDeptByDisCode(SqlParam<T8DistributorQuotaManage> params) throws Exception {
		String sql = "\tselect dept.MANAGER_DEPT,dict.itemval MANAGER_DEPT_NAME\n" +
				"\t\t\t  from\n" +
				"\t\t\t  (\n" +
				"\t\t\t\t\tselect MANAGER_DEPT from t8_distributor_info where DISTRIBUTOR_CODE = $S{distributorCode}\n" +
				"\t\t\t\t\tunion \n" +
				"\t\t\t\t\tselect ORG_MANAGE_DEPT from t8_distributor_info where DISTRIBUTOR_CODE = $S{distributorCode}\n" +
				"\t\t\t\t\tunion \n" +
				"\t\t\t\t\tselect INTER_MANAGE_DEPT from t8_distributor_info where DISTRIBUTOR_CODE = $S{distributorCode}\n" +
				"\t\t\t   ) dept \n" +
				"\t\tleft join sys_dict_item dict\n" +
				"\t\t\t     on dept.MANAGER_DEPT = dict.itemkey\n" +
				"\t\t\t\t\tand dict.dict='manager_dept'" +
				"       where dept.MANAGER_DEPT is not null";
		return super.findRows(sql, params);
	}

	//根据销售商代码查询数据
	public List<SqlRow> findByDistributorCode(String distributorCode) throws Exception {

		Map<String, Object> params = new HashMap<>(1);
		params.put("distributorCode", distributorCode);
		return super.findRows("select t.id,t.distributor_code from t8_distributor_quota_manage t where t.distributor_code=$S{distributorCode}", params);
	}

	public Integer updateDistributorCode(T8DistributorQuotaManage t8DistributorQuotaManage) throws Exception {

		String sql = " UPDATE t8_distributor_quota_manage SET distributor_code = $S{distributorCode} WHERE id = $S{id}";
		return super.update(sql, t8DistributorQuotaManage).getEffect();
	}
}
