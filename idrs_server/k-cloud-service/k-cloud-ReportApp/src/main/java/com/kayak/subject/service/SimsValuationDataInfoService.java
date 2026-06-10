package com.kayak.subject.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.FileUtil;
import com.kayak.graphql.model.FetcherData;
import com.kayak.subject.dao.BaseReportReloadLogDao;
import com.kayak.subject.dao.SimsValuationDataInfoDao;
import com.kayak.subject.model.BaseReportReloadLog;
import com.kayak.subject.model.DwsProdTTRDBef;
import com.kayak.subject.model.SimsValuationDataBInfo;
import com.kayak.subject.model.SimsValuationDataInfo;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.config.FileTransferConfig;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@APIDefine(desc = "SIMS底层估值明细表（调整前）", model = SimsValuationDataInfo.class)
public class SimsValuationDataInfoService {

	@Autowired
	private SimsValuationDataInfoDao simsValuationDataInfoDao;

	@Autowired
	private BaseReportReloadLogDao baseReportReloadLoDao;

	@Autowired
	private JCConfigService jcConfigService;

	@Autowired
	private RptBusinessBaseTaskService rptBusinessBaseTaskService;

	@Autowired
	private DwsProdTTRDBefService dwsProdTTRDBefService;

	@Autowired
	private SimsValuationDataBInfoService simsValuationDataBInfoService;

	@API(desc = "查询SIMS底层估值明细表（调整前）", auth = APIAuth.YES)
	public SqlResult<SimsValuationDataInfo> findSimsValuationDataInfos(SqlParam<SimsValuationDataInfo> params) throws Exception {
		return simsValuationDataInfoDao.findSimsValuationDataInfos(params);
	}

	@API(desc = "导入委外专户估值表解析数据", auth = APIAuth.YES)
	public String impSimsValuationDataInfos(SqlParam<SimsValuationDataInfo> params) throws Exception {
		String returnStr = "";
		Map<String, Object> map = new HashMap<>();
		map.put("paravalue","1");
		map.put("oldParavalue","0");
		map.put("paraid","90000051803");

		String maxId = "";
		BaseReportReloadLog baseReportReloadLog = new BaseReportReloadLog();
		baseReportReloadLog.setMenuId(params.getModel().getMenuId());
		baseReportReloadLog.setReportDate(params.getModel().getDealDate());
		baseReportReloadLog.setStartDate(DateUtil.getNowDate());
		baseReportReloadLog.setStartTime(DateUtil.getNowTime());
		baseReportReloadLog.setResultStatus("正在"+params.getModel().getButtonName()+"中");
		baseReportReloadLog.setUserName(SysUtil.getLoginUserid());

		if ("1".equals(SysUtil.getSystemParamsByParaid("90000061000"))) {
			return RequestSupport.updateReturnJson(false, "系统清算流程正在执行中，请稍后重试！", null).toString();
		}
		if (rptBusinessBaseTaskService.upTaskStatus(map) > 0) {
			map.put("paravalue","0");
			map.put("oldParavalue","1");
		} else {
			return RequestSupport.updateReturnJson(false, "正在"+params.getModel().getButtonName()+"，请稍后重试", null).toString();
		}

		try {
			baseReportReloadLoDao.addBaseReportReloadLog(baseReportReloadLog);
			List<SqlRow> sqlRows = baseReportReloadLoDao.findIdBaseReportReloadLogs(baseReportReloadLog);
			if (CollectionUtil.isNotEmpty(sqlRows)) {
				maxId = sqlRows.get(0).getString("id");
			}

			// 从S3现在获取当前日期文件夹下面的所有文件，并且逐个解析
			Map<String, Object> configMap = jcConfigService.getConfigInfo("WWFA");

			String resv = "S3";
			String value = "";
			Object resvObj = configMap.get("RESV");
			if (ObjectUtil.isNotEmpty(resvObj)) {
				resv = String.valueOf(resvObj);
				value = "S3".equals(resv) ? "" : "_" + resv;
			}

			FileTransferConfig config = new FileTransferConfig();
			config.setProtocol(resv);
			config.setAmazonEndpointUrl((String) configMap.get("ENDPOINT_URL" + value));
			config.setAmazonAwsAccessKey((String) configMap.get("ACCESS_KEY" + value));
			config.setAmazonAwsSecretKey((String) configMap.get("SECRET_KEY" + value));
			config.setAmazonAwsBucketName((String) configMap.get("BUCKET_NAME" + value));

			FileTransfer transfer = FileTransferHelpler.getTransfer(config);

			String deal_date = params.getModel().getDealDate();
			String remotePath = (String) configMap.get("REMOTE_PATH" + value);
			String localPath = (String) configMap.get("LOCAL_PATH" + value);

			if (StringUtils.isNotEmpty(remotePath) && StringUtils.isNotEmpty(localPath)) {
				remotePath = remotePath.replace("[deal_date]", deal_date);
				localPath = localPath.replace("[deal_date]", deal_date);
			}
			// 现在到本地路径，并且逐个解析
			transfer.downFolderFiles(remotePath, localPath);
			File dir = new File(localPath);

			if (dir.isDirectory()) {
				File[] files = dir.listFiles();
				List<SimsValuationDataInfo> simsValuationDataInfoList = new ArrayList<>();

				if (files == null || files.length == 0) {
					rptBusinessBaseTaskService.upTaskStatus(map);

					baseReportReloadLog.setId(maxId);
					baseReportReloadLog.setEndDate(DateUtil.getNowDate());
					baseReportReloadLog.setEndTime(DateUtil.getNowTime());
					baseReportReloadLog.setResultStatus(params.getModel().getButtonName()+"失败");
					baseReportReloadLog.setResultInfo("未找到委外专户估值表文件");
					baseReportReloadLoDao.updateBaseReportReloadLog(baseReportReloadLog);

					return RequestSupport.updateReturnJson(false, params.getModel().getButtonName()+"失败，未找到委外专户估值表文件", null).toString();
				}
				log.info("委外估值文件数量：{}"+files.length);
				for (File file : files) {
					if (!file.getAbsolutePath().contains("ZZBS.csv"))
						continue;
					try (BufferedReader reader = new BufferedReader(new FileReader(file));
						 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
						log.info("解析委外估值文件：{}"+file.getName());
						List<CSVRecord> csvRecords = csvParser.getRecords();
						if (CollectionUtil.isNotEmpty(csvRecords)) {
							for (int i = 0; i < csvRecords.size(); i++) {
								CSVRecord csvRecord = csvRecords.get(i);
								String cell = csvRecord.get(0);// 始终获取第一列
								if (StringUtils.isNotEmpty(cell)) {
									String[] values = cell.split("\\!\\^");
									if (values.length != 13)
										continue;
									SimsValuationDataInfo simsValuationDataInfo = new SimsValuationDataInfo();
									simsValuationDataInfo.setComcode(values[0]);
									simsValuationDataInfo.setBottomCode(values[1]);
									simsValuationDataInfo.setAssetType(values[2]);
									simsValuationDataInfo.setAmount(values[3]);
									simsValuationDataInfo.setCost(values[4]);
									simsValuationDataInfo.setCurrency(values[5]);
									simsValuationDataInfo.setInputDate(values[6]);
									simsValuationDataInfo.setItemId(values[7]);
									simsValuationDataInfo.setItemName(values[8]);
									simsValuationDataInfo.setImportDate(values[9]);
									simsValuationDataInfo.setTradePlace(values[10]);
									simsValuationDataInfo.setIsPublic(values[11]);
									simsValuationDataInfo.setNetValue(values[12]);
									simsValuationDataInfoList.add(simsValuationDataInfo);
								}
							}
						}
					} catch (Exception e) {
						log.error("委外估值文件解析失败：{}", e.getMessage());
						continue;
					} finally {
						FileUtil.delFile(file); // 读取完删除文件
					}
				}
				// 批量插入
				if (CollectionUtil.isNotEmpty(simsValuationDataInfoList)) {
					simsValuationDataInfoDao.addBatchSimsValuationDataInfos(deal_date, simsValuationDataInfoList);
				}

				// 触发后台清算任务处理逻辑
				Map<String, Object> mapData = new HashMap<>();
				mapData.put("menuId", params.getModel().getMenuId());
				mapData.put("buttonName", params.getModel().getButtonName());
				mapData.put("reportDate", params.getModel().getDealDate());
				FetcherData<DwsProdTTRDBef> mapParams = new FetcherData<>(mapData, DwsProdTTRDBef.class);
				returnStr = dwsProdTTRDBefService.updateTaskAppQuery(mapParams);

				// 根据解析出来的估值日期循环调用M086
				List<SqlRow> sqlRowList = simsValuationDataInfoDao.findWwfaFilesInputDate(deal_date);
				for (SqlRow sqlRow : sqlRowList) {
					Map<String, Object> mapData1 = new HashMap<>();
					mapData1.put("inputDate", sqlRow.get("input_date"));
					FetcherData<SimsValuationDataBInfo> mapParams1 = new FetcherData<>(mapData1, SimsValuationDataBInfo.class);
					simsValuationDataBInfoService.reloadTask(mapParams1);
				}

				// 根据调用清算任务类的返回信息进行日志写入
				Map<String, Object> returnMap = JSONUtil.toBean(returnStr, Map.class);
				boolean success = (boolean) returnMap.get("success");
				String returnmsg = (String) returnMap.get("returnmsg");

				baseReportReloadLog.setId(maxId);
				baseReportReloadLog.setEndDate(DateUtil.getNowDate());
				baseReportReloadLog.setEndTime(DateUtil.getNowTime());
				baseReportReloadLog.setResultStatus(params.getModel().getButtonName()+(success ? "成功" : "失败"));
				baseReportReloadLog.setResultInfo(returnmsg);
				baseReportReloadLoDao.updateBaseReportReloadLog(baseReportReloadLog);
			} else {
				rptBusinessBaseTaskService.upTaskStatus(map);

				baseReportReloadLog.setId(maxId);
				baseReportReloadLog.setEndDate(DateUtil.getNowDate());
				baseReportReloadLog.setEndTime(DateUtil.getNowTime());
				baseReportReloadLog.setResultStatus(params.getModel().getButtonName()+"失败");
				baseReportReloadLog.setResultInfo("未找到委外专户估值表文件");
				baseReportReloadLoDao.updateBaseReportReloadLog(baseReportReloadLog);

				return RequestSupport.updateReturnJson(false, params.getModel().getButtonName()+"失败，未找到委外专户估值表文件", null).toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			rptBusinessBaseTaskService.upTaskStatus(map);

			baseReportReloadLog.setId(maxId);
			baseReportReloadLog.setEndDate(DateUtil.getNowDate());
			baseReportReloadLog.setEndTime(DateUtil.getNowTime());
			baseReportReloadLog.setResultStatus(params.getModel().getButtonName()+"失败");
			baseReportReloadLog.setResultInfo(e.getMessage());
			baseReportReloadLoDao.updateBaseReportReloadLog(baseReportReloadLog);

			return RequestSupport.updateReturnJson(false, params.getModel().getButtonName()+"失败", null).toString();
		}
		rptBusinessBaseTaskService.upTaskStatus(map);
		return returnStr;
	}
	
}
