package com.kayak.pms.channelInterface.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.pms.channelInterface.dao.*;
import com.kayak.pms.channelInterface.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "渠道任务接口关系配置服务", model = ChannelInterfaceTask.class)
public class ChannelInterfaceTaskService {

	private Logger logger = LoggerFactory.getLogger(ChannelInterfaceTaskService.class);

	@Autowired
	private ChannelInterfaceTaskDao channelInterfaceTaskDao;
	@Autowired
	private ChannelInterfaceManagerDao channelInterfaceManagerDao;
	@Autowired
	private ChannelProdTaskDao channelProdTaskDao;
	@Autowired
	private ChannelProdInterfaceFileDao channelProdInterfaceFileDao;
	@Autowired
	private ChannelInterfaceTaskInfoDao channelInterfaceTaskInfoDao;

	@API(desc = "查询渠道任务接口关系配置信息", auth = APIAuth.NO)
	public SqlResult<ChannelInterfaceTask> findChannelInterfaceTasks(SqlParam<ChannelInterfaceTask> params) throws Exception {
		params.setMakeSql(true);
		return channelInterfaceTaskDao.findChannelInterfaceTasks(params);
	}

	@API(desc = "添加渠道任务接口关系配置", auth = APIAuth.YES)
	public String  addChannelInterfaceTask(SqlParam<ChannelInterfaceTask> params) throws Exception {
		ChannelInterfaceTask param = params.getModel();
		params.getModel().setCrtUser((String) SysUtil.getSysUserParamValue("sys_user_username"));
		params.getModel().setCrtTime(DateUtil.getTimestamp19());
		List<ChannelInterfaceTask> taskList = channelInterfaceTaskDao.findChannelInterfaceTaskByTaskCode(param.getTaskCode());
		if (CollectionUtil.isNotEmpty(taskList))
			return RequestSupport.updateReturnJson(false,"该任务编码已存在",null).toString();
		JSONArray jsonArray = JSONArray.parseArray(param.getChannelNoList());
		List<ChannelInterfaceTaskInfo> addList = new ArrayList<>();
		List<String> channelNos = new ArrayList<>();
		for (int i =0;i<jsonArray.size();i++) {
			JSONObject object = jsonArray.getJSONObject(i);
			if (channelNos.contains(object.getString("channelNo")))
				return RequestSupport.updateReturnJson(false,object.getString("channelNo")+"渠道编码已存在",null).toString();
			channelNos.add(object.getString("channelNo"));
			if (StringUtils.isNotBlank(object.getString("interfaceNo"))) {
				String [] interfaceNoArr = object.getString("interfaceNo").split(",");
				for (String interfaceNo : interfaceNoArr) {
					ChannelInterfaceTaskInfo taskInfo = new ChannelInterfaceTaskInfo();
					taskInfo.setChannelNo(object.getString("channelNo"));
					taskInfo.setChannelName(object.getString("channelName"));
					taskInfo.setInterfaceNo(interfaceNo);
					taskInfo.setTaskCode(param.getTaskCode());
					taskInfo.setCrtTime(DateUtil.getTimestamp19());
					taskInfo.setCrtUser((String) SysUtil.getSysUserParamValue("sys_user_username"));
					addList.add(taskInfo);
				}
			}

		}

		if (CollectionUtil.isNotEmpty(addList)) {
			DaoUtil.doTrans(() -> {
				channelInterfaceTaskDao.addChannelInterfaceTask(params);
				for (ChannelInterfaceTaskInfo t : addList) {
					channelInterfaceTaskInfoDao.addChannelInterfaceTaskInfo(t);
				}
			});
		}

		return RequestSupport.updateReturnJson(true,"操作成功",null).toString();

	}
	
	@API(desc = "修改渠道任务接口关系配置", auth = APIAuth.YES)
	public String updateChannelInterfaceTask(SqlParam<ChannelInterfaceTask> params) throws Exception {
		ChannelInterfaceTask param = params.getModel();
		params.getModel().setCrtUser((String) SysUtil.getSysUserParamValue("sys_user_username"));
		params.getModel().setCrtTime(DateUtil.getTimestamp19());
		JSONArray jsonArray = JSONArray.parseArray(param.getChannelNoList());
		List<ChannelInterfaceTaskInfo> addList = new ArrayList<>();
		List<String> channelNos = new ArrayList<>();
		for (int i =0;i<jsonArray.size();i++) {
			JSONObject object = jsonArray.getJSONObject(i);
			if (channelNos.contains(object.getString("channelNo")))
				return RequestSupport.updateReturnJson(false,object.getString("channelNo")+"渠道编码已存在",null).toString();
			channelNos.add(object.getString("channelNo"));
			if (StringUtils.isNotBlank(object.getString("interfaceNo"))) {
				String [] interfaceNoArr = object.getString("interfaceNo").split(",");
				for (String interfaceNo : interfaceNoArr) {
					ChannelInterfaceTaskInfo taskInfo = new ChannelInterfaceTaskInfo();
					taskInfo.setChannelNo(object.getString("channelNo"));
					taskInfo.setChannelName(object.getString("channelName"));
					taskInfo.setInterfaceNo(interfaceNo);
					taskInfo.setTaskCode(param.getTaskCode());
					taskInfo.setCrtTime(DateUtil.getTimestamp19());
					taskInfo.setCrtUser((String) SysUtil.getSysUserParamValue("sys_user_username"));
					addList.add(taskInfo);
				}
			}

		}

		if (CollectionUtil.isNotEmpty(addList)) {
			DaoUtil.doTrans(() -> {
				//修改任务
				channelInterfaceTaskDao.updateChannelInterfaceTask(params.getModel());
				//删除详情 重新插入
				channelInterfaceTaskInfoDao.deleteChannelInterfaceTaskInfo(param.getTaskCode());
				for (ChannelInterfaceTaskInfo t : addList) {
					channelInterfaceTaskInfoDao.addChannelInterfaceTaskInfo(t);
				}
			});
		}

		return RequestSupport.updateReturnJson(true,"操作成功",null).toString();
	}
	
	@API(desc = "删除渠道任务接口关系配置", auth = APIAuth.YES)
	public int deleteChannelInterfaceTask(SqlParam<ChannelInterfaceTask> params) throws Exception {
		return channelInterfaceTaskDao.deleteChannelInterfaceTask(params.getModel().getTaskCode()).getEffect();
	}


	@API(desc = "根据任务代码查询渠道接口任务详情", auth = APIAuth.NO)
	public SqlResult<Map<String,Object>> findChannelInterfaceTaskByCode(SqlParam<ChannelInterfaceTask> params) throws Exception {
		List<Map<String,Object>> list = new ArrayList<>();
		List<ChannelInterfaceTaskInfo>  taskInfoList = channelInterfaceTaskInfoDao.findChannelInterfaceTaskInfoByTaskCode(params.getModel().getTaskCode());

		//构造前端需要的数据
		for (ChannelInterfaceTaskInfo task : taskInfoList) {
			Map<String,Object> map = new HashMap<>();
			map.put("channelNo",task.getChannelNo());
			map.put("channelName",task.getChannelName());
			map.put("interfaceNo",task.getInterfaceNo());
			List<SqlRow> managerList= channelInterfaceManagerDao.findChannelInterfaceManagerByChannelNo(task.getChannelNo());
			map.put("interfaceList",managerList);
			list.add(map);
		}
		SqlResult<Map<String,Object>> result = new SqlResult<>();
		result.setRows(list);
		result.setResults(list.size());
		result.setDesensitized(false);
		return result;
	}

	@API(desc = "根据任务代码查询销售商接口配置", auth = APIAuth.NO)
	public SqlResult<Map<String,Object>> findSaleInterfaceTaskByCode(SqlParam<ChannelInterfaceTask> params) throws Exception {
		List<Map<String,Object>> list = new ArrayList<>();
		List<ChannelInterfaceTaskInfo>  taskInfoList = channelInterfaceTaskInfoDao.findChannelInterfaceTaskInfoByTaskCode(params.getModel().getTaskCode());

		//构造前端需要的数据
		for (ChannelInterfaceTaskInfo task : taskInfoList) {
			Map<String,Object> map = new HashMap<>();
			map.put("channelNo",task.getChannelNo());
			map.put("channelName",task.getChannelName());
			map.put("interfaceNo",task.getInterfaceNo());
			List<SqlRow> managerList= channelInterfaceManagerDao.findChannelInterfaceManagerByChannelNo(task.getChannelNo(),task.getTaskCode());
			map.put("interfaceList",managerList);
			list.add(map);
		}
		SqlResult<Map<String,Object>> result = new SqlResult<>();
		result.setRows(list);
		result.setResults(list.size());
		result.setDesensitized(false);
		return result;
	}


		/**
         * @description: 参数下发通用接口
         * @author: wangchenglin
         * @date: 2023/2/17 14:32
         * @param: [params]
         * @return: java.lang.String
         **/
	public String paramIssue(Map<String,Object> params) throws Exception {

		/*
		taskCode:任务代码;
		issueType:下发方式：1-手动下发   2-自动下发;
		prodCode:产品代码
		prodName:产品名称
		prodFlag:产品标识  1-普通产品;2-母产品;3-子产品
		paramCode:参数代码  （产品发行登记和信息调整需要传）
		interfaceNo:接口代码  可不传
		 */

		/*
		 * msg:  true-生成成功  false-生成失败
		 **/

		String msg = "true";
//		String taskCode = (String) params.get("taskCode");
//		List<ChannelInterfaceTask> taskList = channelInterfaceTaskDao.findChannelInterfaceTaskByTaskCode(taskCode);
//		if (CollectionUtil.isNotEmpty(taskList)) {
//			try {
//				ChannelInterfaceTask task = taskList.get(0);
//				//首先判断业务操作时自动下发的还是手动下发 （由调用方传过来）
//				String issueType = (String) params.get("issueType");
//				if ("1".equals(issueType)) { //手动下发  直接生成记录
//					msg = this.addChannelFiles(task, params);
//				} else {  //自动下发 需要看一下配置里是什么下发方式
//					if ("1".equals(task.getIssueType())) {  //配置里手动下发 直接返回false
//						msg = "false";
//					} else {  //配置里自动触发生成记录
//						msg = this.addChannelFiles(task, params);
//					}
//				}
//			} catch (Exception e) {
//				msg = "false";
//				logger.error("生成产品下发任务失败，原因是：{}",e.getMessage());
//			}
//		} else {
//			msg = "false";
//			logger.error(taskCode +"无该渠道接口任务配置");
//		}
		return msg;
	}


	/**
	 * @description: 根据渠道接口配置生成文件（多个）
	 * @author: wangchenglin
	 * @date: 2023/2/17 15:03
	 * @param: [task, param]
	 * @return: java.lang.String
	 **/
	public String addChannelFiles(ChannelInterfaceTask task,Map<String,Object> param) throws Exception {
		String result = "true";
		//根据任务查询所有的接口信息
		List<ChannelInterfaceTaskInfo> taskInfoList = channelInterfaceTaskInfoDao.findChannelInterfaceTaskInfoByTaskCode1(task.getTaskCode());
		if (CollectionUtil.isEmpty(taskInfoList)) {
			result = "false";
			return result;
		}

		DaoUtil.doTrans(() -> {
			//生成一条产品渠道下发记录
			ChannelProdTask prodTask = new ChannelProdTask();
			prodTask.setTaskCode(task.getTaskCode());
			prodTask.setProdCode((String) param.get("prodCode"));
			prodTask.setProdName((String) param.get("prodName"));
			prodTask.setTaskStatus("1");  //默认待下发
			prodTask.setProdFlag((String) param.get("prodFlag"));
			prodTask.setParamCode((String) param.get("paramCode"));
			prodTask.setCrtTime(DateUtil.getTimestamp19());
			prodTask.setCrtUser((String)SysUtil.getSysUserParamValue("sys_user_username"));
			prodTask.setCrtUserId((String)SysUtil.getSysUserParamValue("sys_user_userid"));
			String s = JSONObject.toJSONString(param);
			prodTask.setParams(s);
			UpdateResult r =  channelProdTaskDao.addChannelProdTask(prodTask);
			String taskId = r.getAutoId();
			for (ChannelInterfaceTaskInfo interfaceTaskInfo : taskInfoList) {
				//根据渠道code和接口code 渠道接口配置
				List<ChannelInterfaceManager> channelInterfaceManagerList = channelInterfaceManagerDao.findChannelInterfaceManagerByNo(interfaceTaskInfo.getChannelNo(),interfaceTaskInfo.getInterfaceNo());
				if (CollectionUtil.isNotEmpty(channelInterfaceManagerList)) {
					ChannelInterfaceManager manager = channelInterfaceManagerList.get(0);
					ChannelProdInterfaceFile file = new ChannelProdInterfaceFile();
					file.setTaskProdId(taskId);
					file.setInterfaceNo(interfaceTaskInfo.getInterfaceNo());
					file.setStatus("0"); //待生成
					file.setCrtUser((String)SysUtil.getSysUserParamValue("sys_user_username"));
					file.setCrtTime(DateUtil.getTimestamp19());
					channelProdInterfaceFileDao.addChannelProdInterfaceFile(file);
				} else {
					logger.info("渠道"+interfaceTaskInfo.getChannelNo()+"接口"+interfaceTaskInfo.getInterfaceNo()+"不存在！！！");
				}
			}
		});
		return result;
	}

}
