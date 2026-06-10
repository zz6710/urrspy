package com.kayak.pms.T85.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.system.RequestSupport;
import com.kayak.pms.T85.dao.T8ProdTaskSetDao;
import com.kayak.pms.T85.model.T8ProdTaskSet;
import com.kayak.pms.T85.model.T8ProdTaskSetList;
import com.kayak.pms.global.constants.GlobalConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文件名: TaProdTaskSetListService.java
 * 描述:  产品清算任务配置操作服务
 * 创建人: zengzt
 * 创建时间:2020年5月20日下午1:54:10
 */
@Service
@APIDefine(desc = "产品清算任务配置操作服务", model = T8ProdTaskSetList.class)
public class T8ProdTaskSetListService {
	
	@Autowired
	private T8ProdTaskSetDao t8ProdTaskSetDao;

	@API(desc = "新增产品清算任务配置", auth = APIAuth.YES)
	public String addTaProdTaskSets(SqlParam<T8ProdTaskSetList> params) throws Exception{

		//不需要自动拼接sql
		params.setMakeSql(false);
		List<T8ProdTaskSet> prodTaskList = params.getModel().getProdTaskList();
		String preTaskIdStr = "";
		T8ProdTaskSet t8ProdTaskSet;
		
		//需要对一些字段进行特殊处理
		for (int index = 0; index < prodTaskList.size(); index++) {

			t8ProdTaskSet = prodTaskList.get(index);
			t8ProdTaskSet.setProdMode(params.getModel().getProdMode());
			
			//展示顺序
			t8ProdTaskSet.setDisplayOrder(index+"");
			//前置任务
			t8ProdTaskSet.setPreTaskId(preTaskIdStr);
			
			//前置任务， #表示当前任务组，同一组内的任务依次依赖，不允许出现并行执行，后一个任务的前置任务包含排在它前面的所有任务id
			if(index==0){
				preTaskIdStr = preTaskIdStr + GlobalConstants.CURRENT_GROUP_PRE_FLAG + t8ProdTaskSet.getTaskId();
			}else{
				preTaskIdStr = preTaskIdStr + GlobalConstants.GROUP_TASK_REGEX + GlobalConstants.CURRENT_GROUP_PRE_FLAG + t8ProdTaskSet.getTaskId();
			}
		}
		
		//插入数据
		t8ProdTaskSetDao.addTaProdTaskSets(params);

		return RequestSupport.updateReturnJson(true, "任务设置成功", null).toString();
	}

}
