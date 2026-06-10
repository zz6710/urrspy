package com.kayak.pms.channelInterface.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.ResponseResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateFormatEnum;
import com.kayak.core.util.DateUtil;
import com.kayak.pms.channelInterface.dao.ChannelParamSettingDao;
import com.kayak.pms.channelInterface.dao.ChannelProdFileDetailDao;
import com.kayak.pms.channelInterface.dao.ChannelProdInterfaceFileDao;
import com.kayak.pms.channelInterface.dao.ChannelProdTaskDao;
import com.kayak.pms.channelInterface.model.ChannelFileInfo;
import com.kayak.pms.channelInterface.model.ChannelProdFileDetail;
import com.kayak.pms.channelInterface.model.ChannelProdInterfaceFile;
import com.kayak.pms.channelInterface.model.ChannelProdTask;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.*;
import java.util.*;

@Service
@APIDefine(desc = "产品下发任务服务", model = ChannelProdTask.class)
public class ChannelProdTaskService {

	private Logger logger = LoggerFactory.getLogger(ChannelProdTaskService.class);

	@Autowired
	private ChannelProdTaskDao channelProdTaskDao;

	@Autowired
	private ChannelParamSettingDao channelParamSettingDao;

	@Autowired
	private ChannelProdFileDetailDao channelProdFileDetailDao;

	@Autowired
	private ChannelProdInterfaceFileDao channelProdInterfaceFileDao;

	@API(desc = "查询产品下发任务信息", auth = APIAuth.NO)
	public SqlResult<ChannelProdTask> findChannelProdTasks(SqlParam<ChannelProdTask> params) throws Exception {
		//params.setMakeSql(true);
		return channelProdTaskDao.findChannelProdTasks(params);
	}

	@API(desc = "添加产品下发任务", auth = APIAuth.YES,operation = APIOperation.INSTER,StatusChangeFlow="StatusChangeFlow")
	public int addChannelProdTask(SqlParam<ChannelProdTask> params) throws Exception {
		params.getModel().setCrtUser((String) SysUtil.getSysUserParamValue("sys_user_username"));
		params.getModel().setCrtTime(DateUtil.getTimestamp19());
		params.getModel().setTaskStatus("1"); //待下发
		return channelProdTaskDao.addChannelProdTask(params).getEffect();
	}

	public ResponseResult StatusChangeFlow(@RequestBody Object o) throws Exception{
		return new ResponseResult();
	}
	
	@API(desc = "修改产品下发任务", auth = APIAuth.NO)
	public int updateChannelProdTask(SqlParam<ChannelProdTask> params) throws Exception {
		return channelProdTaskDao.updateChannelProdTask(params).getEffect();
	}
	
	@API(desc = "删除产品下发任务", auth = APIAuth.NO)
	public int deleteChannelProdTask(SqlParam<ChannelProdTask> params) throws Exception {
		return channelProdTaskDao.deleteChannelProdTask(params).getEffect();
	}

	@API(desc = "定时任务生成参数下发对接文件", auth = APIAuth.NO)
	public String generateParamFiles(SqlParam<ChannelProdTask> params) throws Exception {
		List<String> taskIdList = new ArrayList<>();  //记录产品下发任务id
		//1.首先查询所有待下发的任务数据
		List<ChannelFileInfo> fileInfoList = channelProdTaskDao.findAllChannelFiles();
		if (CollectionUtil.isNotEmpty(fileInfoList)) {
			for (ChannelFileInfo fileInfo : fileInfoList) {
				//将任务id存到list  方便后续更新状态
				taskIdList.add(fileInfo.getTaskProdId());
				//格式化参数
				JSONObject jsonObject =  JSON.parseObject(fileInfo.getParams()); ;
				//2.执行全量下发的sql  + 全量下发条件
				String fullSql = fileInfo.getSelectSql() + " " + fileInfo.getFullQueryCondition();
				List<SqlRow> fullSqlRow = channelProdTaskDao.findRows(fullSql,jsonObject);
				if (CollectionUtil.isEmpty(fullSqlRow))
					continue;
				List<SqlRow> incrementalSqlRow = new ArrayList<>();
				//增量查询条件
				if (StringUtils.isNotBlank(fileInfo.getIncrementalQueryCondition())) {
					String incrementalSql = fileInfo.getSelectSql() + " " + fileInfo.getIncrementalQueryCondition();
					incrementalSqlRow =  channelProdTaskDao.findRows(incrementalSql,jsonObject);
				}
				//3.获取比对结果List
				List<Map<String,String>> resultList = this.compareFullAndIncremental(fullSqlRow,incrementalSqlRow,fileInfo.getInterfaceNo());
				//4.根据结果生成文件
				boolean fullFlag = true;  //全量标识
				if (CollectionUtil.isNotEmpty(incrementalSqlRow))
					fullFlag = false;
				String result = this.saveChannelFile(fileInfo,resultList,fullFlag);
			}

			//5.执行完后查一下任务所属文件是否都生成了   如果都生成了  那么更新成已下发完成
			for (String taskId : taskIdList) {
				//查询待下发的任务
				List<ChannelProdTask> taskList = channelProdTaskDao.findChannelProdTaskById(taskId,"1");
				if (CollectionUtil.isEmpty(taskList)) {
					channelProdTaskDao.updateTaskStatus(taskId,"5");
				}
			}
		}
		return RequestSupport.updateReturnJson(true,"生成参数下发对接文件成功！",null).toString();
	}

	/**
	 * @description: 比较全量增量数据 并返回需要输出到文件的数据
	 * @author: wangchenglin
	 * @date: 2023/3/7 15:17
	 * @param: [fullSqlRow, incrementalSqlRow, interfaceNo]  全量数据   增量数据   接口编码
	 * @return: java.util.List<java.util.Map<java.lang.String,java.lang.String>>
	 **/
	private List<Map<String,String>> compareFullAndIncremental(List<SqlRow> fullSqlRow, List<SqlRow> incrementalSqlRow, String interfaceNo) throws Exception {
		List<Map<String,String>> resultList = new ArrayList<>();
		boolean fullFlag = true;
		if (CollectionUtil.isNotEmpty(incrementalSqlRow))
			fullFlag = false;

		SqlRow fullRow;
		SqlRow incrementalRow;
		for (int i = 0; i < fullSqlRow.size() ; i++) {
			Map<String,String> map = new HashMap<>();
			fullRow = fullSqlRow.get(i);
			if (fullFlag) {
				//循环处理一下value值统一成String
				Iterator<Map.Entry<String, Object>> t = fullRow.entrySet().iterator();
				while (t.hasNext()){
					Map.Entry<String, Object> next = t.next();
					String k = next.getKey();
					Object v = next.getValue();
					if (v != null) {
						map.put(k,v.toString());
					} else {
						map.put(k,"");
					}
				}
			} else {
				incrementalRow = incrementalSqlRow.get(i);
				//循环两个row进行数据比较
				Iterator<Map.Entry<String, Object>> iterator = fullRow.entrySet().iterator();
				while (iterator.hasNext()){
					Map.Entry<String, Object> next = iterator.next();
					String key = next.getKey();
					String value = "";
					if (next.getValue() != null) {
						value =  next.getValue().toString();
					}
					//比较旧的值与新的值
					if (!value.equals(incrementalRow.getString(key))) {
						System.err.println("key++++" + key);
						System.err.println("新的值>>>>" + value);
						System.err.println("旧的值----" + incrementalRow.getString(key));
						System.err.println("-------------------------------------------------");
						map.put(key,value);
					}
				}
			}

			//得到要生成数据的Map，循环此map并处理key值转化以及数据字典转换
			Map<String,String> resultMap = new HashMap<>();
			Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
			while (it.hasNext()){
				Map.Entry<String, String> next = it.next();
				//查询字段配置表
				SqlRow row = channelParamSettingDao.findChannelParamSettingByInterfaceNo(interfaceNo,next.getKey());
				if (row == null) {
					logger.info(next.getKey()+">>>>>key值无字段配置");
				} else {
					//判断是否需要字典值转换
					if (StringUtils.isNotBlank(row.getString("fieldDict"))) {
						SqlRow r = channelParamSettingDao.getChanelDict(row.getString("fieldDict"),next.getValue());
						if (r == null) {
							logger.info(next.getKey()+"-----key值数据字典无数据");
						} else {
							resultMap.put(row.getString("otherField"),r.getString("other_item_key"));
						}
					} else {
						resultMap.put(row.getString("otherField"),next.getValue());
					}
				}
			}
			resultList.add(resultMap);
		}
		return resultList;
	}

	/**
	 * @description: 生成接口文件
	 * @author: wangchenglin
	 * @date: 2023/3/7 15:17
	 * @param: [fileInfo, resultList, fullFlag] 文件记录  文件数据 全量标识
	 * @return: java.lang.String
	 **/
	private String saveChannelFile(ChannelFileInfo fileInfo,List<Map<String,String>> resultList,boolean fullFlag) {
		//调用获取文件名字的sql
		String result = "1";
		BufferedWriter out = null;
		String fileName = "";
		String filePath = "";
		List<ChannelProdFileDetail> detailList = new ArrayList<>();
		try {
			//每次生成调用一下渠道接口文件id自增序列的方法  用于自增
			this.autoAddSeq(fileInfo);
			JSONObject jsonObject =  JSON.parseObject(fileInfo.getParams()); ;
			SqlRow nameRow = channelProdTaskDao.findRow(fileInfo.getFileNameSql(),jsonObject);
			if (nameRow == null) {
				logger.info("获取文件名字失败");
				result = "2";
			}
			fileName = nameRow.getString("val");
			filePath = fileInfo.getFilePath() + File.separator + fileName;
			File file = new File(filePath);
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			if (!file.exists())
				file.createNewFile();
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filePath), "GBK"));
			JSONObject object = new JSONObject();
			object.put("begFlag","APPARADAT");
			out.write(object.toJSONString() + "\r\n");
			JSONObject o = new JSONObject();
			o.put("dataRowNums",resultList.size());
			o.put("time",DateUtil.formatDate(new Date(), DateFormatEnum.TIME_FORMAT));
			o.put("date",DateUtil.formatDate(new Date(), DateFormatEnum.DATE_FORMAT));
			o.put("version","3000");
			object = new JSONObject();
			object.put("header",o);
			out.write(object.toJSONString() + "\r\n");

			//循环list插入
			int dataRowNum = channelProdFileDetailDao.findMaxDataRowNum();
			for (Map<String,String> r : resultList) {
				ChannelProdFileDetail detail = new ChannelProdFileDetail();
				detail.setDataRowNum(dataRowNum+"");
				detail.setChannelInterfaceFileId(fileInfo.getId());
				detailList.add(detail);

				object = new JSONObject();
				r.put("dataRowNum",dataRowNum+"");
				object.put("data",r);
				out.write(object.toJSONString() + "\r\n");
				dataRowNum++;
			}
			object = new JSONObject();
			object.put("endFlag","APPARAEND");
			out.write(object.toJSONString() + "\r\n");

			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("生成文件失败");
			result = "2";
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		String finalFileName = fileName;
		String finalFilePath = filePath;
		//生成文件成功后   再插入明细表
		if ("1".equals(result)) {
			try {
				DaoUtil.doTrans(() -> {
					channelProdFileDetailDao.addChannelProdFileDetail(detailList);
					//更新附件表的路径和附件名称
					ChannelProdInterfaceFile file = new ChannelProdInterfaceFile();
					file.setId(fileInfo.getId());
					file.setFileName(finalFileName);
					file.setFilePath(finalFilePath);
					file.setStatus("3");   //已下发
					if (fullFlag) //全量
						file.setTaskFlag("1");
					else
						file.setTaskFlag("2");  //增量
					channelProdInterfaceFileDao.updateFileInfoById(file);

					//每个任务下的接口文件生成一次   都把任务重置一下为 部分已下发
					channelProdTaskDao.updateTaskStatus(fileInfo.getTaskProdId(),"4");
				});
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("插入渠道对接文件明细表失败，原因是：{}",e.getMessage());
			}
		}
		return result;
	}

	/**
	 * @description: 获取文件的自增序列，在文件命名sql中使用
	 * @author: wangchenglin
	 * @date: 2023/3/7 15:18
	 * @param: [fileInfo] 接口文件数据
	 * @return: void
	 **/
	private void autoAddSeq(ChannelFileInfo fileInfo) throws Exception{
		Map<String,Object> map = new HashMap<>();
		map.put("channelCode",fileInfo.getInterfaceNo());
		map.put("crtDate",DateUtil.formatDate(new Date(),DateFormatEnum.DATE_FORMAT));
		map.put("crtTime",DateUtil.formatDate(new Date(),DateFormatEnum.TIME_FORMAT));
		map.put("dataDate",DateUtil.formatDate(new Date(),DateFormatEnum.DATE_FORMAT));
		SqlRow row = channelProdTaskDao.getSeqByInterfaceNo(fileInfo.getInterfaceNo());
		if ("1".equals(fileInfo.getChannelSeqType())) {  //毫秒数自增  不做任何处理

		}  else if ("2".equals(fileInfo.getChannelSeqType())) { //1自增形式
			if (row == null || StringUtils.isBlank(row.getString("seqNo"))) {
				map.put("seqNo","1");
				channelProdTaskDao.addInterfaceNoSeq(map);
			} else {
				String seqNo = row.getString("seqNo");
				int temp = Integer.parseInt(seqNo);
				seqNo = String.valueOf(temp + 1);
				map.put("seqNo",seqNo);
				channelProdTaskDao.updateInterfaceNoSeq(map);
			}
		}  else if ("3".equals(fileInfo.getChannelSeqType())) { //001自增形式
			if (row == null || StringUtils.isBlank(row.getString("seqNo"))) {
				map.put("seqNo","001");
				channelProdTaskDao.addInterfaceNoSeq(map);
			} else {
				String seqNo = row.getString("seqNo");
				int temp = Integer.parseInt(seqNo);
				seqNo = String.format("%03d", temp + 1);
				map.put("seqNo",seqNo);
				channelProdTaskDao.updateInterfaceNoSeq(map);
			}
		}
	}


//	@API(desc = "获取参数下发的数据", auth = APIAuth.NO)
//	public SqlResult<Map<String,Object>> getParamDelivery(SqlParam<ChannelProdTask> params) throws Exception {
//		ChannelProdTask task = params.getModel();
//		//首先根据渠道接口获取的详细的信息
//		List<Map<String,Object>> resultList = new ArrayList<>();
//		String [] interfaceNoArr = task.getInterfaceNo().split(",");
//		for (String interfaceNo : interfaceNoArr) {
//			List<Map<String,Object>> mapList = new ArrayList<>();
//			List<ChannelInterfaceManager> managerList = channelInterfaceManagerDao.findChannelInterfaceManagerByNo(task.getChannelNo(),interfaceNo);
//				ChannelInterfaceManager manager = managerList.get(0);
//				//执行配置的sql获取数据源
//				SqlRow infoRow = channelProdTaskDao.findRow(manager.getSelectSql(),task);
//				//查询该接口配置的所有字段
//				ChannelParamSetting setting = new ChannelParamSetting();
//				setting.setChannelNo(task.getChannelNo());
//				setting.setInterfaceNo(interfaceNo);
//				List<ChannelParamSetting> settingList = channelParamSettingDao.findChannelParamSettingsByCondition(setting);
//				if (CollectionUtil.isNotEmpty(settingList)) {
//					for (ChannelParamSetting set : settingList) {
//						Map<String,Object> map = new HashMap<>();
//						map.put("columnLabel",set.getFieldName());
//						map.put("columnKey", set.getOtherField());
//						map.put("functype","k-field-text");
//						map.put("id",set.getId());
//						String value = infoRow.getString(set.getField());
//						//有数据字典则转换值为数据字典
//						if (StringUtils.isNotBlank(set.getFieldDict())) {
//							SqlRow dictRow = channelParamSettingDao.getChanelDict(set.getFieldDict(),value);
//							if (dictRow != null) {
//								map.put("columnValue",dictRow.getString("item_val"));
//								map.put("realValue",dictRow.getString("other_item_key"));
//							} else {
//								map.put("columnValue",value);
//								map.put("realValue",value);
//							}
//						} else {
//							map.put("columnValue",value);
//							map.put("realValue",value);
//						}
//						//放到结果集中
//						mapList.add(map);
//					}
//				}
//
//			Map<String,Object> resultMap = new HashMap<>();
//			resultMap.put("displayName",manager.getInterfaceName());
//			resultMap.put("prodCode",task.getProdCode());
//			resultMap.put("prodName",task.getProdName());
//			resultMap.put("channelName",task.getChannelName());
//			resultMap.put("data",mapList);
//			resultList.add(resultMap);
//		}
//
//		SqlResult<Map<String,Object>> result = new SqlResult<>();
//		result.setRows(resultList);
//		result.setDesensitized(false);
//		result.setResults(resultList.size());
//		return result;
//	}
}
