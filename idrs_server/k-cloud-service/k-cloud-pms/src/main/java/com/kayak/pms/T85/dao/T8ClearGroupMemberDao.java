package com.kayak.pms.T85.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.Sql;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;
import com.kayak.pms.T85.model.T8ClearGroupMember;
import com.kayak.pms.T85.model.T8ClearGroupMemberList;
import com.kayak.pms.global.constants.BatchTaskType;
import com.kayak.pms.T82.model.T82001;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文件名: TaClearGroupMemberOperDao.java 
 * 描述:   清算组产品销售商配置表操作DAO
 * 创建人: zengzt
 * 创建时间:2020年5月6日下午3:58:23
 */
@Repository
public class T8ClearGroupMemberDao extends ComnDao {

	/**
	 * 
	 * 方法描述:查询清算组产品销售商配置表
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8ClearGroupMember> queryTaClearGroupMembers(SqlParam<T8ClearGroupMember> params) throws Exception {
		
		String sql = "SELECT mem.task_group,mem.exec_task_type,mem.group_member,(CASE mem.exec_task_type WHEN '2' THEN prod.prod_name ELSE dis.distributor_name END) AS group_member_name   "
				+ "	FROM t8_clear_group_member mem LEFT JOIN t8_prod_info prod ON mem.group_member=prod.prod_code"
				+ " LEFT JOIN t8_distributor_info dis ON mem.group_member=dis.distributor_code ";
		
		return super.findRows(sql, params);
	}
	
	
	/**
	 * 
	 * 方法描述:校验销售商是否已经在其他销售商导出组
	 * @param params 组成员代码（销售商代码）列表
	 * @return 
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> checkDistributorExpMembers(List<String> params,String taskGroup) throws Exception {
		
		//拼接销售商代码
		String distribuitorCodes = String.join("','", params);
		
		String sql = "SELECT group_member FROM  t8_clear_group_member WHERE exec_task_type="+BatchTaskType.DISTRIBUTOR_FILE_EXP
					+" AND group_member IN ('"+distribuitorCodes+"') AND task_group<> '"+taskGroup+"'";
		
		return super.findRows(sql, params);
	}
	
	
	
	
	/**
	 * 
	 * 方法描述:查询产品代码，并标志哪些已经入组
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8ClearGroupMember> queryGroupMemberProds(SqlParam<T8ClearGroupMember> params) throws Exception {
		
		String sql = "SELECT "
				+"  prod.prod_code  AS group_member, "
				+"  prod.prod_name_short AS group_member_name, "
				+"  (CASE WHEN mem.exec_task_type IS NULL THEN $S{execTaskType} ELSE mem.exec_task_type END ) AS exec_task_type, "
				+"  (CASE WHEN mem.task_group IS NULL THEN $S{taskGroup} ELSE mem.task_group END ) AS task_group, "
				+"  (CASE WHEN mem.task_group IS NULL THEN '0' ELSE '1' END ) AS is_group_member"
				+" FROM t8_prod_info prod LEFT JOIN  t8_clear_group_member mem ON  mem.group_member=prod.prod_code WHERE  mem.task_group = $S{taskGroup} OR mem.task_group IS NULL";
		
		return super.findRows(sql, params);
	}
	/**
	 * 
	 * 方法描述:查询销售商代码，并标志哪些已经入组
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8ClearGroupMember> queryGroupMemberDists(SqlParam<T8ClearGroupMember> params) throws Exception {
		
		String sql = "SELECT "
				+"  dis.distributor_code  AS group_member, "
				+"  dis.distributor_name AS group_member_name, "
				+"  (CASE WHEN mem.exec_task_type IS NULL THEN $S{execTaskType} ELSE mem.exec_task_type END ) AS exec_task_type, "
				+"  (CASE WHEN mem.task_group IS NULL THEN $S{taskGroup} ELSE mem.task_group END ) AS task_group, "
				+"  (CASE WHEN mem.task_group IS NULL THEN '0' ELSE '1' END ) AS is_group_member"
				+" FROM t8_distributor_info dis LEFT JOIN  t8_clear_group_member mem ON  mem.group_member=dis.distributor_code AND  mem.task_group = $S{taskGroup} ";
		
		return super.findRows(sql, params);
	}

	/**
	 * 
	 * 方法描述:查询资管导入文件类型，并标志哪些已经入组
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8ClearGroupMember> queryGroupMemberZgImps(SqlParam<T8ClearGroupMember> params) throws Exception {
		
		String sql = "SELECT "
						+"  intf.file_type AS group_member, "
						+"  intf.FILE_DESC AS group_member_name, "
						+"  (CASE WHEN mem.exec_task_type IS NULL THEN $S{execTaskType} ELSE mem.exec_task_type END ) AS exec_task_type, "
						+"  (CASE WHEN mem.task_group IS NULL THEN $S{taskGroup} ELSE mem.task_group END ) AS task_group, "
						+"  (CASE WHEN mem.task_group IS NULL THEN '0' ELSE '1' END ) AS is_group_member "
						+"FROM  "
						+"  t8_intf_file_manage intf LEFT JOIN t8_clear_group_member mem ON intf.file_type=mem.group_member AND mem.task_group=$S{taskGroup} "
						+"WHERE  "
						+"  intf.system_serno = 'zg' "
						+"  AND intf.file_kind = '0' "
						+"  AND status='1' ";
						
		return super.findRows(sql, params);
	}

	/**
	 * 
	 * 方法描述:查询资管导出文件类型，并标志哪些已经入组
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8ClearGroupMember> queryGroupMemberZgExps(SqlParam<T8ClearGroupMember> params) throws Exception {
		
		String sql = "SELECT "
							+"  intf.file_type AS group_member, "
							+"  intf.FILE_DESC AS group_member_name, "
							+"  (CASE WHEN mem.exec_task_type IS NULL THEN $S{execTaskType} ELSE mem.exec_task_type END ) AS exec_task_type, "
							+"  (CASE WHEN mem.task_group IS NULL THEN $S{taskGroup} ELSE mem.task_group END ) AS task_group, "
							+"  (CASE WHEN mem.task_group IS NULL THEN '0' ELSE '1' END ) AS is_group_member "
							+"FROM  "
							+"  t8_intf_file_manage intf LEFT JOIN t8_clear_group_member mem ON intf.file_type=mem.group_member AND mem.task_group=$S{taskGroup} "
							+"WHERE  "
							+"  intf.system_serno = 'zg' "
							+"  AND intf.file_kind = '1' "
							+"  AND status='1' ";
		return super.findRows(sql, params);
	}


	/**
	 * 
	 * 方法描述:插入清算组产品销售商配置
	 * @param
	 * @return
	 * @throws Exception
	 */
	public int insertTaClearGroupMember(T8ClearGroupMember t8ClearGroupMember) throws Exception {

		String sqlAll = "INSERT INTO t8_clear_group_member(task_group,exec_task_type,group_member,crt_date,upt_date)"
				+ " VALUES($S{taskGroup},$S{execTaskType},$S{groupMember},current_timestamp,current_timestamp)";
		String sqlDb2 = "INSERT INTO t8_clear_group_member(task_group,exec_task_type,group_member,crt_date,upt_date)"
				+ " VALUES($S{taskGroup},$S{execTaskType},$S{groupMember},current timestamp,current timestamp)";
		Sql sql = Sql.build().mysqlSql(sqlAll).db2Sql(sqlDb2);

		return super.update(sql, t8ClearGroupMember).getEffect();
		
	}

	/**
	 *
	 * 方法描述:更新清算组产品销售商配置
	 * @param
	 * @return
	 * @throws Exception
	 */
	public int updateTaClearGroupMember(T8ClearGroupMember t8ClearGroupMember) throws Exception {

		String sql = "update t8_clear_group_member set task_group=$S{taskGroup}, group_member=$S{groupMember}, exec_task_type=$S{execTaskType} where group_member=$S{groupMember} and task_group=$S{taskGroup}";

		return super.update(sql, t8ClearGroupMember).getEffect();

	}

	/**
	 *
	 * 方法描述:查询清算组产品销售商配置
	 * @param
	 * @return
	 * @throws Exception
	 */
	public SqlResult<T8ClearGroupMember> queryTaClearGroupMember(SqlParam<T8ClearGroupMember> params) throws Exception {

		String sql = "select task_group, group_member, exec_task_type, upt_date, crt_date  from t8_clear_group_member mem where 1=1";

		return super.findRows(sql, params);

	}

	/**
	 *
	 * 方法描述:删除清算组产品销售商配置-单个删除
	 * @param
	 * @return
	 * @throws Exception
	 */
	public int deleteClearGroupMember(T8ClearGroupMember t8ClearGroupMember) throws Exception {

		String sql = "DELETE FROM t8_clear_group_member WHERE task_group = $S{oldTaskGroup} AND group_member= $S{groupMember}";

		return super.update(sql, t8ClearGroupMember).getEffect();

	}

	/**
	 *
	 * 方法描述:更新清算组产品销售商配置
	 * @param
	 * @return
	 * @throws Exception
	 */
	public int updateClearGroupMember(T8ClearGroupMember t8ClearGroupMember) throws Exception {

		String sql = "update t8_clear_group_member set task_group=$S{taskGroup}, group_member=$S{groupMember}, exec_task_type=$S{execTaskType} where group_member=$S{groupMember} and task_group=$S{oldTaskGroup}";

		return super.update(sql, t8ClearGroupMember).getEffect();

	}
	/**
	 *
	 * 方法描述:查询清算组产品销售商配置
	 * @param
	 * @return
	 * @throws Exception
	 */
	public SqlRow queryTaClearGroupMember(T8ClearGroupMember t8ClearGroupMember) throws Exception {

		String sql = "select task_group, group_member, exec_task_type, upt_date, crt_date  from t8_clear_group_member mem where 1=1";
		if (Tools.isNotEmpty(t8ClearGroupMember.getGroupMember())) {
			sql = sql + " and mem.group_member=$S{groupMember}";
		}
		if (Tools.isNotEmpty(t8ClearGroupMember.getExecTaskType())) {
			sql = sql + " and mem.exec_task_type=$S{execTaskType}";
		}
		if (Tools.isNotEmpty(t8ClearGroupMember.getTaskGroup())) {
			sql = sql + " and mem.task_group=$S{taskGroup}";
		}
		return super.findRow(sql, t8ClearGroupMember);

	}

	/**
		 *
		 * 方法描述:查询清算组产品销售商配置
		 * @param
		 * @return
		 * @throws Exception
		 */
		public SqlResult<T82001> queryTaClearGroupMemberMem(SqlParam<T82001> params) throws Exception {

			String sql = "select task_group, group_member, exec_task_type, upt_date, crt_date  from t8_clear_group_member mem where 1=1";

			return super.findRows(sql, params);

		}

	/**
	 * 
	 * 方法描述:删除清算组产品销售商配置-单个删除
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int deleteSingleTaClearGroupMember(SqlParam<T8ClearGroupMember> params) throws Exception {

		String sql = "DELETE FROM t8_clear_group_member WHERE task_group = $S{taskGroup} AND group_member= $S{groupMember}";
		
		return super.update(sql, params.getModel()).getEffect();
		
	}
	/**
		 *
		 * 方法描述:删除清算组产品销售商配置-单个删除
		 * @param
		 * @return
		 * @throws Exception
		 */
		public int deleteTaClearGroupMember(T8ClearGroupMember t8ClearGroupMember) throws Exception {

			String sql = "DELETE FROM t8_clear_group_member WHERE task_group = $S{taskGroup} AND group_member= $S{groupMember}";

			return super.update(sql, t8ClearGroupMember).getEffect();

		}

	/**
	 * 
	 * 方法描述:删除清算组产品销售商配置-按组删除
	 * @param
	 * @return
	 * @throws Exception
	 */
	public int deleteTaClearGroupMember(String taskGroup) throws Exception {

		String sql = "DELETE FROM t8_clear_group_member WHERE task_group = $S{taskGroup} ";
		
		return super.update(sql, taskGroup).getEffect();
		
	}
	
	
	public void addMemberInfos(T8ClearGroupMemberList t8ClearGroupMemberList) throws Exception{
		
		//先删除原组成员，再插入新的组成员信息
		doTrans(() -> {
			
			deleteTaClearGroupMember(t8ClearGroupMemberList.getTaskGroup());
			
			List<T8ClearGroupMember> memberList = t8ClearGroupMemberList.getMemberList();
			for (T8ClearGroupMember t8ClearGroupMember : memberList) {
				insertTaClearGroupMember(t8ClearGroupMember);
			}
			
		});
	}
	
	
	
	
}
