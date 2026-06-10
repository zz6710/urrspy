package com.kayak.config.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.config.dao.KbatchGroupInfoDao;
import com.kayak.config.model.KbatchGroupInfo;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件名: KbatchGroupInfoService.java
 * 描述:   清算组信息操作
 * 创建人: zengzt
 * 创建时间:2020年4月29日下午2:55:20
 */

@Service
@APIDefine(desc = "清算组信息操作服务", model = KbatchGroupInfo.class)
public class KbatchGroupInfoService {

	//前台m-select、checkbox多数据连接的分隔符
	public static final String webCheckBoxSpe = ",";

	@Autowired
	private KbatchGroupInfoDao batchGroupOperDao;
	
	@API(desc = "查询清算任务组信息", auth = APIAuth.NO)
	public SqlResult<KbatchGroupInfo> findKbatchGroupInfos(SqlParam<KbatchGroupInfo> params) throws Exception {

		params.setMakeSql(true);
		return batchGroupOperDao.queryKbatchGroupInfos(params);
	}

	@API(desc = "查询清算任务组信息，加上一个'-1'的值代表所有", auth = APIAuth.NO)
	public SqlResult<KbatchGroupInfo> findTaClearGroupAndAll(SqlParam<KbatchGroupInfo> params) throws Exception {
		SqlResult<KbatchGroupInfo> sqlResult = batchGroupOperDao.queryKbatchGroupInfos(params);
		List<KbatchGroupInfo> list = new ArrayList<KbatchGroupInfo>();
		KbatchGroupInfo groupInfo = new KbatchGroupInfo();
		groupInfo.setTaskGroup("-1");
		groupInfo.setTaskGroupName("所有任务组");
		list.add(groupInfo);
		for (int i = 0; i < sqlResult.getRows().size(); i++) {
			KbatchGroupInfo KbatchGroupInfo = new KbatchGroupInfo();
			KbatchGroupInfo.setTaskGroup(sqlResult.getRows().get(i).getTaskGroup());
			KbatchGroupInfo.setTaskGroupName(sqlResult.getRows().get(i).getTaskGroupName());
			list.add(KbatchGroupInfo);
		}
		sqlResult.setRows(list);
		return sqlResult;
	}
	
	@API(desc = "查询清算任务组信息（不包含某个组）", auth = APIAuth.NO)
	public SqlResult<KbatchGroupInfo> findKbatchGroupInfosExceptGroup(SqlParam<KbatchGroupInfo> params) throws Exception {
		return batchGroupOperDao.findKbatchGroupInfosExceptGroup(params);
	}
	

	@API(desc = "新增清算任务组信息", auth = APIAuth.NO)
	public String addKbatchGroupInfo(SqlParam<KbatchGroupInfo> params) throws Exception {
		//插入前先递归查找前置批次组，判断是否有出现组收尾相接死循环，并登记所有前置批次信息到库里面
		params.setMakeSql(false);
		List<KbatchGroupInfo> KbatchGroupInfoList  = batchGroupOperDao.queryKbatchGroupInfos(params).getRows();
		//记录每个批组的前置批组信息
		Map<String, String> preGroupInfos = KbatchGroupInfoList.stream().collect(Collectors.toMap(m -> m.getTaskGroup(), m -> m.getPreTaskGroup()==null?"":m.getPreTaskGroup()));

		Set<String> existsgroupSet = new HashSet<>();
		//递归查找前置任务批次组，将任务组添加到Set中
		addCheckPreTaskGroup(null,params.getModel().getPreTaskGroup(), existsgroupSet, preGroupInfos);

		//将返回得到的全量前置批次组Set转成字符串重新设置到清算任务信息组中
		String newPreTaskGroup = existsgroupSet.toString().replaceAll("\\[|\\]| ", "");
		params.getModel().setPreTaskGroup(newPreTaskGroup);

		//插入清算组信息
		batchGroupOperDao.insertKbatchGroupInfo(params);

		return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
	}

	@API(desc = "修改清算任务组信息", auth = APIAuth.NO)
	public String modifyKbatchGroupInfo(SqlParam<KbatchGroupInfo> params) throws Exception {
		params.setMakeSql(false);
		List<KbatchGroupInfo> KbatchGroupInfoList  = batchGroupOperDao.queryKbatchGroupInfos(params).getRows();
		//记录每个批组的前置批组信息
		Map<String, String> preGroupInfos = KbatchGroupInfoList.stream().collect(Collectors.toMap(m -> m.getTaskGroup(), m -> m.getPreTaskGroup()==null?"":m.getPreTaskGroup()));

		Set<String> existsgroupSet = new HashSet<>();
		modifyCheckPreTaskGroup(params.getModel().getTaskGroup(),null,params.getModel().getPreTaskGroup(), existsgroupSet, preGroupInfos);

		//将返回得到的全量前置批次组Set转成字符串重新设置到清算任务信息组中
		String newPreTaskGroup = existsgroupSet.toString().replaceAll("\\[|\\]| ", "");
		params.getModel().setPreTaskGroup(newPreTaskGroup);

		//修改清算组信息
		batchGroupOperDao.updateKbatchGroupInfo(params);

		return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
	}
	

	/**
	 * 递归查找前置任务批次组，将任务组添加到Set中
	 * @param preTaskGroupStr
	 * @param existsgroupSet
	 */
	private void addCheckPreTaskGroup(String currTaskGroup,String preTaskGroupStr, Set<String> existsgroupSet,Map<String, String> preGroupInfos) throws PromptException {
		if(!Tools.strIsEmpty(currTaskGroup)){
			existsgroupSet.add(currTaskGroup);
		}
		if(!Tools.strIsEmpty(preTaskGroupStr)){
			//直接存的前台传过来的前置组数据，是以逗号分隔
			String[] split = preTaskGroupStr.split(webCheckBoxSpe);
			for (int i = 0; i < split.length; i++) {
				addCheckPreTaskGroup(split[i],preGroupInfos.get(split[i]), existsgroupSet, preGroupInfos);
			}
		}
	}


	/**
	 * 递归查找前置任务批次组，将任务组添加到Set中，并校验是否有前置任务组又依赖了本任务组构成批次死循环
	 * @param preTaskGroupStr
	 * @param existsgroupSet
	 */
	private void modifyCheckPreTaskGroup(String oriTaskGroup,String currTaskGroup,String preTaskGroupStr, Set<String> existsgroupSet,Map<String, String> preGroupInfos) throws PromptException {
		if(!Tools.strIsEmpty(currTaskGroup)){
			existsgroupSet.add(currTaskGroup);
		}
		if(!Tools.strIsEmpty(oriTaskGroup) && !Tools.strIsEmpty(currTaskGroup) && currTaskGroup.equals(oriTaskGroup)){
			//前置组又依赖了本任务组，就出现了闭环，不允许
			throw new PromptException("依赖关系出现首尾衔接，保存失败");
		}
		//还有前置组，递归处理
		if(!Tools.strIsEmpty(preTaskGroupStr)){
			//直接存的前台传过来的前置组数据，是以逗号分隔
			String[] split = preTaskGroupStr.split(webCheckBoxSpe);
			for (int i = 0; i < split.length; i++) {
				modifyCheckPreTaskGroup(oriTaskGroup,split[i],preGroupInfos.get(split[i]), existsgroupSet, preGroupInfos);
			}
		}
	}

}
