package com.kayak.pms.T85.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.pms.T85.dao.T8ClearTaskInfoDao;
import com.kayak.pms.T85.model.T8ClearTaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文件名: TaClearTaskInfoService.java
 * 描述: 清算任务信息操作服务
 * 创建人: zengzt
 * 创建时间:2020年5月9日下午5:05:11
 */
@Service
@APIDefine(desc = "清算任务信息操作服务", model = T8ClearTaskInfo.class)
public class T8ClearTaskInfoService {

	@Autowired
	private T8ClearTaskInfoDao t8ClearTaskInfoDao;
	
	@API(desc = "查询清算任务信息", auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<T8ClearTaskInfo> findTaClearTaskInfos(SqlParam<T8ClearTaskInfo> params) throws Exception {

		params.setMakeSql(true);
		
		return t8ClearTaskInfoDao.queryTaClearTaskInfos(params);
	}

	@API(desc = "关联查询清算任务信息、清算任务配置", auth = APIAuth.NO)
	public SqlResult<T8ClearTaskInfo> findTaClearTaskInfoWithFlag(SqlParam<T8ClearTaskInfo> params) throws Exception {

		params.setMakeSql(false);
		return t8ClearTaskInfoDao.queryTaClearTaskInfoWithFlag(params);
	}

	@API(desc = "关联查询清算任务信息、产品清算任务配置", auth = APIAuth.NO)
	public SqlResult<T8ClearTaskInfo> findProdClearTaskInfoWithFlag(SqlParam<T8ClearTaskInfo> params) throws Exception {

		params.setMakeSql(false);
		return t8ClearTaskInfoDao.queryProdClearTaskInfoWithFlag(params);
	}

	@API(desc = "新增清算任务", auth = APIAuth.NO)
	public String insertTaClearTaskInfo(SqlParam<T8ClearTaskInfo> params) throws Exception {

		//先校验组件ID是否已经存在
		List<SqlRow> queryResult = t8ClearTaskInfoDao.queryTaClearTaskInfoById(params.getModel().getTaskId());
		if(queryResult.size()>0){
			throw new PromptException("任务ID已经存在，新增清算组件失败");
		}
		
		params.setMakeSql(false);
		t8ClearTaskInfoDao.insertTaClearTaskInfo(params);
		return RequestSupport.updateReturnJson(true, "新增组件成功", null).toString();
	}

	@API(desc = "修改清算任务", auth = APIAuth.NO)
	public String updateTaClearTaskInfo(SqlParam<T8ClearTaskInfo> params) throws Exception {

		params.setMakeSql(false);
		t8ClearTaskInfoDao.updateTaClearTaskInfo(params);
		return RequestSupport.updateReturnJson(true, "修改组件成功", null).toString();
	}

	@API(desc = "删除清算任务", auth = APIAuth.NO)
	public String deleteTaClearTaskInfo(SqlParam<T8ClearTaskInfo> params) throws Exception {

		//删除组件前，先判断是否有任务使用了该组件
		List<SqlRow>  result = t8ClearTaskInfoDao.checkTaskIdIsUsed(params.getModel().getTaskId());
		if(result.get(0).getInteger("setNum")>0){
			throw new PromptException("已有清算配置使用了该组件，不允许删除");
		}
		
		params.setMakeSql(false);
		t8ClearTaskInfoDao.deleteTaClearTaskInfo(params);
		
		return RequestSupport.updateReturnJson(true, "删除组件成功", null).toString();
	}
	
}
