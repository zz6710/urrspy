package com.kayak.pms.T85.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.pms.T85.dao.SysParamDao;
import com.kayak.pms.T85.dao.T85002Dao;
import com.kayak.pms.T85.model.T85002;
import com.kayak.pms.T85.model.T85002FlowInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件名: Ta5002Service.java
 * 描述:  清算流程
 * 创建人: zengzt
 * 创建时间:2020年5月23日下午5:37:02
 */
@Service
@APIDefine(desc = "清算流程", model = T85002.class)
public class T85002Service {

	@Autowired
	private T85002Dao t85002Dao;
	
	@Autowired
	private SysParamDao sysParamDao;

	@API(desc = "查询清算流程信息", auth = APIAuth.YES)
	public SqlResult<T85002> findTa5002s(SqlParam<T85002FlowInfo> params) throws Exception {

		/*
		 * 判断当前系统工作日是否已经全部执行完任务：
		 * 	全部完成：查询下一系统工作日的任务
		 * 未全部完成：查询当前系统工作日的任务
		 */
		String workdate;
		//查询当前系统工作日
		List<SqlRow> sysParam = sysParamDao.queryParam("a0000002");
		if(sysParam.size()<1){
			throw new PromptException("未初始化系统工作日参数");
		}
		workdate = sysParam.get(0).getString("paravalue");
		
		//判断当前工作日的任务是否全部执行完成，是则查询下一工作日
		List<SqlRow> checkClearExecResult = t85002Dao.checkClearExec(workdate);
		Integer unFinishCount = checkClearExecResult.get(0).getInteger("count");
		if(unFinishCount<=0){
			//如果有未完成的任务，则展示当前系统工作日清算流程，否则展示下一系统工作日清算流程
			workdate = sysParamDao.queryParam("a0000003").get(0).getString("paravalue");
		}
		
		//是否自动追加参数
		params.setMakeSql(false);
		params.getModel().setTaskDate(workdate);;
		//查询流程数据
		SqlResult<T85002FlowInfo> queryClearFlowInfo = t85002Dao.queryClearFlowInfo(params);
		
		//构造返回结果
		List<T85002FlowInfo> flowInfos = queryClearFlowInfo.getRows();
		
		T85002 result = new T85002();
		result.setFlowInfos(flowInfos);
		result.setWorkdate(workdate);	//设置需要返回的系统工作日
		
		List<T85002> resultList = new ArrayList<T85002>();
		resultList.add(result);
		
		SqlResult<T85002> build = SqlResult.build(resultList);
		
		return build;
	}

	@API(desc = "查询清算流程对应产品代码", auth = APIAuth.YES)
	public SqlResult<String> findClearExecProd(SqlParam<T85002FlowInfo> params) throws Exception {
		
		List<SqlRow> queryResult = t85002Dao.queryClearExecProd(params.getModel());
		
		List<String> collect = queryResult.stream().map( row -> row.getString("prod_code")).collect(Collectors.toList());
		
		return  SqlResult.build(collect);
		
	}

	@API(desc = "查询清算流程对应目标代码", auth = APIAuth.YES)
	public SqlResult<String> findClearExecTarget(SqlParam<T85002FlowInfo> params) throws Exception {
		
		List<SqlRow> queryResult = t85002Dao.queryClearExecTarget(params.getModel());
		
		List<String> collect = queryResult.stream().map( row -> row.getString("target_code")).collect(Collectors.toList());
		
		return  SqlResult.build(collect);
		
	}
	
}
