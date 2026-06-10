package com.kayak.config.service;

import com.kayak.aspect.annotations.APIDefine;
import com.kayak.config.constants.GlobalConstants;
import com.kayak.config.dao.KbatchTaskSetDao;
import com.kayak.config.model.KbatchTaskSet;
import com.kayak.config.model.KbatchTaskSetList;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.system.RequestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 文件名: TaClearTaskSetListService.java
 * 描述:  清算任务配置操作服务
 * 创建人: zengzt
 * 创建时间:2020年5月13日下午3:15:13
 */
@Service
@APIDefine(desc = "清算任务配置操作服务", model = KbatchTaskSetList.class)
public class KbatchTaskSetListService {

	@Autowired
	private KbatchTaskSetDao taClearTaskSetDao;

//	@API(desc = "新增清算组任务配置", auth = APIAuth.YES)
	public String addTaClearTaskSets(SqlParam<KbatchTaskSetList> params) throws Exception{
		
		List<KbatchTaskSet> taskSetList = params.getModel().getTaskSetList();
		String preTaskIdStr = "";
		
		//需要对配置表部分字段进行赋值
		for (int index = 0; index < taskSetList.size(); index++) {

            KbatchTaskSet taskSet = taskSetList.get(index);
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
		taClearTaskSetDao.addKbatchTaskSets(params.getModel());
		return RequestSupport.updateReturnJson(true, "任务设置成功", null).toString();
	}

    public String addTaClearTaskSets(SqlParam<KbatchTaskSetList> params, String preTaskGroup) throws Exception {
        KbatchTaskSetList   taClearTaskSetList = params.getModel();
        List<KbatchTaskSet> taskSetList        = taClearTaskSetList.getTaskSetList();
        String               preTaskIdStr       = "";

        //需要对配置表部分字段进行赋值
        for (int index = 0; index < taskSetList.size(); index++) {

            KbatchTaskSet taskSet = taskSetList.get(index);
            taskSet.setTaskGroup(taClearTaskSetList.getTaskGroup());

            //展示顺序
            taskSet.setDisplayOrder(index + "");

            //前置任务
            String preTaskId = "";
            if (null != preTaskGroup && !"".equals(preTaskGroup)) {
                preTaskId = preTaskGroup + ("".equals(preTaskIdStr) ? "" : GlobalConstants.GROUP_TASK_REGEX);
                preTaskId += preTaskIdStr;
            } else {
                preTaskId = preTaskIdStr;
            }
            taskSet.setPreTaskId(preTaskId);

            //前置任务， #表示当前任务组，同一组内的任务依次依赖，不允许出现并行执行，后一个任务的前置任务包含排在它前面的所有任务id
            if (index == 0) {
                preTaskIdStr = preTaskIdStr + GlobalConstants.CURRENT_GROUP_PRE_FLAG + taskSet.getTaskId();
            } else {
                preTaskIdStr = preTaskIdStr + GlobalConstants.GROUP_TASK_REGEX + GlobalConstants.CURRENT_GROUP_PRE_FLAG + taskSet.getTaskId();
            }
        }

        //插入数据
        taClearTaskSetDao.addKbatchTaskSets(taClearTaskSetList);
        return RequestSupport.updateReturnJson(true, "任务设置成功", null).toString();
    }

	public void deleteAllTaClearTaskSet(Map<String,Object> tparam) throws Exception {
		taClearTaskSetDao.deleteAllKbatchTaskSet(tparam);
	}
	
}
