package com.kayak.pms.T85.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.system.RequestSupport;
import com.kayak.pms.T85.dao.T8ClearTaskSetDao;
import com.kayak.pms.T85.model.T8ClearTaskSet;
import com.kayak.pms.T85.model.T8ClearTaskSetList;
import com.kayak.pms.global.constants.GlobalConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文件名: TaClearTaskSetListService.java
 * 描述:  清算任务配置操作服务
 * 创建人: zengzt
 * 创建时间:2020年5月13日下午3:15:13
 */
@Service
@APIDefine(desc = "清算任务配置操作服务", model = T8ClearTaskSetList.class)
public class T8ClearTaskSetListService {

	@Autowired
	private T8ClearTaskSetDao t8ClearTaskSetDao;

	@API(desc = "新增清算组任务配置", auth = APIAuth.YES)
	public String addTaClearTaskSets(SqlParam<T8ClearTaskSetList> params) throws Exception{
		
		List<T8ClearTaskSet> taskSetList = params.getModel().getTaskSetList();
		String preTaskIdStr = "";
		
		//需要对配置表部分字段进行赋值
		for (int index = 0; index < taskSetList.size(); index++) {
			
			T8ClearTaskSet taskSet = taskSetList.get(index);
			taskSet.setTaskGroup(params.getModel().getTaskGroup());
			
			//展示顺序
			taskSet.setDisplayOrder(index+"");

			//前置任务
			taskSet.setPreTaskId(preTaskIdStr);
			
			//前置任务， #表示当前任务组，同一组内的任务依次依赖，不允许出现并行执行，后一个任务的前置任务包含排在它前面的所有任务id
			if(index==0){
				preTaskIdStr = preTaskIdStr + GlobalConstants.CURRENT_GROUP_PRE_FLAG + taskSet.getTaskId();
			}else{
				preTaskIdStr = preTaskIdStr + GlobalConstants.GROUP_TASK_REGEX + GlobalConstants.CURRENT_GROUP_PRE_FLAG + taskSet.getTaskId();
			}
		}
		
		//插入数据
		t8ClearTaskSetDao.addTaClearTaskSets(params.getModel());
		
		return RequestSupport.updateReturnJson(true, "任务设置成功", null).toString();
	}
	
}
