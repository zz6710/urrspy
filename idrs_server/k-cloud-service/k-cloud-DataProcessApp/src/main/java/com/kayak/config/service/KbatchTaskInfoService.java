package com.kayak.config.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.config.dao.KbatchGroupInfoDao;
import com.kayak.config.dao.KbatchTaskInfoDao;
import com.kayak.config.model.KbatchTaskInfo;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件名: TaClearTaskInfoService.java
 * 描述: 清算任务信息操作服务
 * 创建人: zengzt
 * 创建时间:2020年5月9日下午5:05:11
 */
@Service
@APIDefine(desc = "清算任务信息操作服务", model = KbatchTaskInfo.class)
public class KbatchTaskInfoService {

	@Autowired
	private KbatchTaskInfoDao batchTaskInfoDao;
	@Autowired
    private KbatchGroupInfoDao batchGroupDao;
	//axin
	@API(desc = "查询最大id", auth = APIAuth.NO)
	public SqlResult<KbatchTaskInfo> findTaskId(SqlParam<KbatchTaskInfo> params) throws Exception {
		params.setMakeSql(false);
		SqlResult<KbatchTaskInfo> sqlResult = batchTaskInfoDao.queryTaskId(params);
		List<String> taskId = sqlResult.getRows().stream().map(KbatchTaskInfo::getTaskId).collect(Collectors.toList());
		int z = taskId.size() + 1;
		for(int i = 0 ; i < taskId.size() ; i++) {
			String a = taskId.get(i).substring(1);
			int j = Integer.parseInt(a);
			if (i + 1 != j) {
				z = i + 1;
				break;
			}
		}

		String maxTaskId = "P" + taskIdLength(z + "");
		KbatchTaskInfo kbatchTaskInfo = new KbatchTaskInfo();
		kbatchTaskInfo.setTaskId(maxTaskId);

		List<KbatchTaskInfo> list = new ArrayList<>();
		list.add(kbatchTaskInfo);

		sqlResult.setRows(list);
		return sqlResult;
	}

	public String taskIdLength(String a){
		int i = 3;
		a = "0" + a ;
		if (a.length() < i)
			return taskIdLength(a);
		return a;
	}



	@API(desc = "查询清算任务信息", auth = APIAuth.YES,operation = APIOperation.SELECT)
	public SqlResult<KbatchTaskInfo> findTaskInfos(SqlParam<KbatchTaskInfo> params) throws Exception {
		params.setMakeSql(true);
		return batchTaskInfoDao.queryTaskInfos(params);
	}

	@API(desc = "关联查询清算任务信息、清算任务配置", auth = APIAuth.YES,operation = APIOperation.SELECT)
	public SqlResult<KbatchTaskInfo> findKbatchTaskInfoWithFlag(SqlParam<KbatchTaskInfo> params) throws Exception {
		params.setMakeSql(false);
		return batchTaskInfoDao.queryKbatchTaskInfoWithFlag(params);
	}

	@API(desc = "新增清算任务", auth = APIAuth.YES)
	public String insertKbatchTaskInfo(SqlParam<KbatchTaskInfo> params) throws Exception {
		//先校验组件ID是否已经存在
		List<SqlRow> queryResult = batchTaskInfoDao.queryKbatchTaskInfoById(params.getModel().getTaskId());
		if(queryResult.size()>0){
			throw new PromptException("任务ID已经存在，新增清算组件失败");
		}

		params.setMakeSql(false);
		batchTaskInfoDao.insertKbatchTaskInfo(params);
		return RequestSupport.updateReturnJson(true, "新增组件成功", null).toString();
	}

	@API(desc = "修改清算任务", auth = APIAuth.YES)
	public String updateKbatchTaskInfo(SqlParam<KbatchTaskInfo> params) throws Exception {
		params.setMakeSql(false);
		batchTaskInfoDao.updateKbatchTaskInfo(params);
		return RequestSupport.updateReturnJson(true, "修改组件成功", null).toString();
	}

	@API(desc = "删除清算任务", auth = APIAuth.YES)
	public String deleteKbatchTaskInfo(SqlParam<KbatchTaskInfo> params) throws Exception {
		//删除组件前，先判断是否有任务使用了该组件
		List<SqlRow>  result = batchTaskInfoDao.checkTaskIdIsUsed(params.getModel().getTaskId());
		if(result.get(0).getInteger("setNum")>0){
			throw new PromptException("已有清算配置使用了该组件，不允许删除");
		}

		params.setMakeSql(false);
		batchTaskInfoDao.deleteKbatchTaskInfo(params);

		return RequestSupport.updateReturnJson(true, "删除组件成功", null).toString();
	}


}
