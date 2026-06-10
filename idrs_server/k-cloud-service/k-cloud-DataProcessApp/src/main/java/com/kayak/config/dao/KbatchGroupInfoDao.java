package com.kayak.config.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.config.model.KbatchGroupInfo;
import com.kayak.core.sql.Sql;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 文件名: TaClearGroupOperDao.java
 * 描述:   TA清算组信息操作
 * 创建人: zengzt
 * 创建时间:2020年4月29日下午2:31:57
 */
@Repository
public class KbatchGroupInfoDao extends ComnDao{

	/**
	 * 
	 * 方法描述:查询清算任务组信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<KbatchGroupInfo> queryKbatchGroupInfos(SqlParam<KbatchGroupInfo> params) throws Exception {
		String sql = "SELECT task_group,task_group_name,exec_task_type,pre_task_group,display_order,running_type,should_exec_time FROM kbatch_group_info ORDER BY display_order";
		return super.findRows(sql, params);
	}
	

	/**
	 * 
	 * 方法描述:查询清算任务组信息 （不包含某个组）
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<KbatchGroupInfo> findKbatchGroupInfosExceptGroup(SqlParam<KbatchGroupInfo> params) throws Exception {
		String sql = "SELECT task_group,task_group_name,exec_task_type,pre_task_group,display_order,running_type,should_exec_time FROM kbatch_group_info ";
		if(params.getModel().getTaskGroup()!=null && !"".equals(params.getModel().getTaskGroup())){
			sql = sql + " WHERE task_group <> $S{taskGroup}";
		}
		sql = sql + " ORDER BY display_order";
		return super.findRows(sql, params);
	}
	
	
	/**
	 * 
	 * 方法描述:插入清算组信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertKbatchGroupInfo(SqlParam<KbatchGroupInfo> params) throws Exception {
        String random = UUID.randomUUID().toString().replace("-", "").substring(0,8);
		String sqlAll = "INSERT INTO kbatch_group_info(task_group,task_group_name,exec_task_type,pre_task_group,crt_date,upt_date,display_order,running_type,should_exec_time,alarm_time)"
				+ "VALUES(lpad(seq_task_group.nextVal,12,'0'),$S{taskGroupName},$S{execTaskType},$S{preTaskGroup},current_timestamp,current_timestamp,$S{displayOrder},$S{runningType},$S{shouldExecTime},$S{alarmTime})";
		String sqlDb2 = "INSERT INTO kbatch_group_info(task_group,task_group_name,exec_task_type,pre_task_group,crt_date,upt_date,display_order,running_type,should_exec_time,alarm_time)"
				+ "VALUES(lpad(seq_task_group.nextVal,12,'0'),$S{taskGroupName},$S{execTaskType},$S{preTaskGroup},current timestamp,current timestamp,$S{displayOrder},$S{runningType},$S{shouldExecTime},$S{alarmTime})";
        String sqlMysql = "INSERT INTO kbatch_group_info(task_group,task_group_name,exec_task_type,pre_task_group,crt_date,upt_date,display_order,running_type,should_exec_time,alarm_time)"
                + "VALUES('"+random+"',$S{taskGroupName},$S{execTaskType},$S{preTaskGroup},current_timestamp,current_timestamp,$S{displayOrder},$S{runningType},$S{shouldExecTime},$S{alarmTime})";
		Sql sql = Sql.build().oracleSql(sqlAll).db2Sql(sqlDb2).mysqlSql(sqlMysql);
		return super.update(sql, params.getModel()).getEffect();
	}
	
	/**
	 * 
	 * 方法描述:修改清算组信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateKbatchGroupInfo(SqlParam<KbatchGroupInfo> params) throws Exception {
		String sqlAll = "UPDATE kbatch_group_info"
				+ " SET task_group_name = $S{taskGroupName},"
				+ "		pre_task_group = $S{preTaskGroup}, "
				+ "		upt_date = current_timestamp, "
				+ "		display_order = $S{displayOrder} ,"
				+ "     running_type = $S{runningType},"
				+ "     should_exec_time = $S{shouldExecTime},"
				+ "     alarm_time = $S{alarmTime}"
				+ " WHERE task_group = $S{taskGroup} ";
		String sqlDb2 = "UPDATE kbatch_group_info"
				+ " SET task_group_name = $S{taskGroupName},"
				+ "		pre_task_group = $S{preTaskGroup}, "
				+ "		upt_date = current timestamp, "
				+ "		display_order = $S{displayOrder}, "
				+ "     running_type = $S{runningType},"
				+ "     should_exec_time = $S{shouldExecTime},"
				+ "     alarm_time = $S{alarmTime}"
				+ " WHERE task_group = $S{taskGroup} ";
		Sql sql = Sql.build().oracleSql(sqlAll).db2Sql(sqlDb2);
		return super.update(sql, params.getModel()).getEffect();
	}


    /**
     * 方法描述: 查询所有产品清算组信息,根据task_exec_type和should_exec_time排序
     */
    public SqlResult<KbatchGroupInfo> findProdClearGroupInfoOrderByTime(SqlParam<KbatchGroupInfo> param)  throws Exception{
        param.setMakeSql(false);
        String sqlAll = "SELECT t.task_group,t.last_task_group,t.task_group_name,t.running_type,t.should_exec_time,t.pre_task_group " +
                " FROM kbatch_group_info t WHERE t.exec_task_type = '2'";
        return super.findRows(sqlAll,param);
    }
	
	/**
	 * 
	 * 方法描述:删除清算组信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public void deleteKbatchGroupInfo(SqlParam<KbatchGroupInfo> params) throws Exception {
		doTrans(() ->{
			super.update("DELETE FROM kbatch_group_info WHERE task_group = $S{taskGroup} ", params.getModel()).getEffect();
			super.update("DELETE FROM kbatch_task_set WHERE task_group = $S{taskGroup} ", params.getModel()).getEffect();
		});
	}


	/**
	 * 
	 * 方法描述:查询是否有以本组未前置的清算组
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<KbatchGroupInfo> queryIsPreTaClearGroup(SqlParam<KbatchGroupInfo> params) throws Exception {
		
		String sql = "SELECT task_group,task_group_name,exec_task_type,pre_task_group,running_type,should_exec_time FROM kbatch_group_info where instr(pre_task_group,$S{taskGroup}) > 0";
		
		return super.findRows(sql, params);
	}

//	public void updateDistributorInfo(KbatchGroupInfo KbatchGroupInfo,String sql)throws Exception{
//		if(BatchTaskType.PRODUCT.equals(KbatchGroupInfo.getExecTaskType())){
//			SqlResult<TaProdInfo> chengeRow = taProdInfoDao.findTaProdInfosByTaskGroup(new FetcherData<>(new HashMap<>(),TaProdInfo.class),"where task_group  like '%"+KbatchGroupInfo.getTaskGroup() +"%'" + sql)  ;
//			for(TaProdInfo item : chengeRow.getRows()){
//				SqlParam<TaProdInfo> params = new FetcherData<>(ObjectToMapUtils.entityToMap(item),TaProdInfo.class);
//				params.getModel().setTaskGroup(taskGroup(params.getModel().getTaskGroup(),KbatchGroupInfo.getTaskGroup()));
//				taProdInfoDao.updateTaProdInfo(params);
//			}
//		}else if(BatchTaskType.DISTRIBUTOR_FILE_IMP.equals(KbatchGroupInfo.getExecTaskType())){
//			SqlResult<Ta2001> chengeRow = taDistributorInfoDao.findTaDistributorInfosByTaskGroup(new FetcherData<>(new HashMap<>(),Ta2001.class),"where imp_task_group  like '%"+KbatchGroupInfo.getTaskGroup() +"%'"+sql)  ;
//			for(Ta2001 item : chengeRow.getRows()){
//				SqlParam<Ta2001> params = new FetcherData<>(ObjectToMapUtils.entityToMap(item),Ta2001.class);
//				params.getModel().setImpTaskGroup(taskGroup(params.getModel().getImpTaskGroup(),KbatchGroupInfo.getTaskGroup()));
//				taDistributorInfoDao.updateDis(params);
//			}
//		}else if(BatchTaskType.DISTRIBUTOR_FILE_EXP.equals(KbatchGroupInfo.getExecTaskType())){
//			SqlResult<Ta2001> chengeRow = taDistributorInfoDao.findTaDistributorInfosByTaskGroup(new FetcherData<>(new HashMap<>(),Ta2001.class),"where exp_task_group  like '%"+KbatchGroupInfo.getTaskGroup() +"%'"+sql);
//			for(Ta2001 item : chengeRow.getRows()){
//				SqlParam<Ta2001> params = new FetcherData<>(ObjectToMapUtils.entityToMap(item),Ta2001.class);
//				params.getModel().setExpTaskGroup(taskGroup(params.getModel().getExpTaskGroup(),KbatchGroupInfo.getTaskGroup()));
//				taDistributorInfoDao.updateDis(params);
//			}
//		}else if(StringUtils.isEmpty(KbatchGroupInfo.getExecTaskType())){
//			throw  new Exception("未传回类型");
//		}
//	}

	private String taskGroup(String taskGroup,String empty){
		if(StringUtils.isNotEmpty(taskGroup)){
			taskGroup = String.join(",",Arrays.asList(taskGroup.split(",")).stream().filter(item->!item.equals(empty)).collect(Collectors.toList()));
		}
		return taskGroup;
	}
}
