package com.kayak.dps.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;
import com.kayak.clear.utils.Tools;
import com.kayak.config.utils.DbopChange;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.FileUtil;
import com.kayak.dps.app.dao.*;
import com.kayak.dps.app.model.ImportTemplateDataLog;
import com.kayak.dps.app.model.ImportTemplateManage;
import com.kayak.dps.app.model.ImportTemplateManageField01;
import com.kayak.dps.app.model.ImportTemplateManageField02;
import com.kayak.dps.app.utils.ExcelUtils;
import com.kayak.dps.pub.ICallback;
import com.kayak.dps.py.excel.ExcelImportListener;
import com.kayak.graphql.model.FetcherData;
import com.kayak.report.dao.BaseReportFileManageDao;
import com.kayak.report.model.BaseReportFileManage;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import com.kayakwise.kcloud.db.util.ParamMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service

@APIDefine(desc = "导入模板管理表", model = ImportTemplateManage.class)
@Slf4j
@RefreshScope
public class ImportTemplateManageService {
	@Value("${database.schemas}")
	private String databases;
	@Value("${oss.charset.mac}")
	private String charsetType;//单接口最大发送次数
	@Autowired
	private ImportTemplateManageDao importTemplateManageDao;
	@Autowired
	private ImportTemplateDataLogDao importTemplateDataLogDao;
	@Autowired
	private ImportTemplateManageField01Dao importTemplateManageField01Dao;
	@Autowired
	private ImportTemplateManageField02Dao importTemplateManageField02Dao;
	@Autowired
	private ProdInfoOdsDao prodInfoOdsDao;
	@Autowired
	private ComnDao comnDao;
	@Autowired
	private DbopChange dbopChange;
	@Autowired
	private BaseReportFileManageDao baseReportFileManageDao;

	@API(desc = "更新起始行",  auth = APIAuth.YES)
	public String updTemplateInfoRowStart(SqlParam<ImportTemplateManage> params) throws Exception {
		try {
			importTemplateManageDao.updTemplateInfoRowStart(params.getModel());
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		} catch (Exception e){
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "修改失败！", null).toString();
		}
	}

	@API(desc = "查询模板管理列表", auth = APIAuth.YES)
	public SqlResult<ImportTemplateManage> findTemplateList(SqlParam<ImportTemplateManage> params) throws Exception {
		params.setMakeSql(false);
		return importTemplateManageDao.findTemplateList(params);
	}

	@API(desc = "查询模板管理列表-历史", auth = APIAuth.YES)
	public SqlResult<ImportTemplateManage> findTemplateListHis(SqlParam<ImportTemplateManage> params) throws Exception {
		params.setMakeSql(false);
		return importTemplateManageDao.findTemplateListHis(params);
	}

	@API(desc = "上传模板", auth = APIAuth.YES)
	public void importTemplate(SqlParam<ImportTemplateManageField01> params){
		//此方法只作用于权限控制
	}

	@API(desc = "查询报送数据列表", auth = APIAuth.YES)
	public SqlResult<ImportTemplateManage> findTemplateDataList(SqlParam<ImportTemplateManage> params) throws Exception {
		params.setMakeSql(false);

		// TODO  OK
		List<ImportTemplateManage> tableList =	importTemplateManageDao.findTableNameList(params.getModel());
		StringBuffer sql = new StringBuffer();

		sql.append(" select *  from  ( ");
		StringBuffer forSql = new StringBuffer();
		for (int i = 0; i < tableList.size() ; i++) {

			String tableName = tableList.get(i).getTableName();
			forSql.append("select  e.template_file_name ,e.id,e.template_name, d.*  from  (select '");
			forSql.append(tableName).append("' as system_table_name, ");
			// TODO
//			forSql.append("( select itemkey  from  sys_dict_item where itemval  = '");
			forSql.append("( select   id  as  itemkey  from  app_table_info  where  system_table_name  = '");
			forSql.append(tableName).append("') as itemkey,");
			forSql.append("( select   CONCAT(system_table_name_cn,'-',system_table_name)  as  itemkey  from  app_table_info  where  system_table_name  = '");
			forSql.append(tableName).append("') as table_name,");
			forSql.append("a.sys_data_status ,max( a.sys_data_version) as sys_data_version ,a.imp_date from ");
			forSql.append(tableName).append(" a  group by  a.sys_data_status  , a.imp_date ");
			forSql.append(" ) d left  join   (select *  from  import_template_manage  where template_status = '1' ) e  on d.itemkey = e.system_table_name ");
			if(i!=tableList.size()-1 ){
				forSql.append(" union all  ");
			}
		}
		sql.append(forSql);
		sql.append(" )x where x.id is not  null");
		return importTemplateManageDao.findTemplateDataList(sql.toString(),params);
	}


	@API(desc = "添加模板管理表",  auth = APIAuth.YES)
	public String addTemplateInfo(SqlParam<ImportTemplateManage> params) throws Exception {
		try {
			params.getModel().setTemplateStatus("1");
			params.getModel().setImpDate(DateUtil.getNowDate());
			params.getModel().setImpTime(DateUtil.getNowTime());


			SqlResult<ImportTemplateManage> list = importTemplateManageDao.findTemplateInfo(params);
			if(list.getRows().size()<=0){
				// 数据库中没有对应的模板
				params.getModel().setVersion("1.0");
			}else {

				// 更新旧的模板状态 为停用
				ImportTemplateManage saveModel = new ImportTemplateManage();
				saveModel.setId(list.getRows().get(0).getId());
				saveModel.setTemplateStatus("0");
				importTemplateManageDao.updTemplateInfo(saveModel);

				// 增加新的模板版本号
				BigDecimal version =  new BigDecimal(list.getRows().get(0).getVersion());
				version = version.add(new BigDecimal("0.1"));
				params.getModel().setVersion(version.toString());

			}

			importTemplateManageDao.addTemplateInfo(params);
			return RequestSupport.updateReturnJson(true,  "添加成功！", null).toString();
		} catch (Exception e){
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "添加失败！", null).toString();
		}
	}


	public ImportTemplateManage getTemplateInfoByUpload(ImportTemplateManage params) throws Exception {
			params.setTemplateStatus("1");
			List <ImportTemplateManage> list = importTemplateManageDao.findTemplateInfoByModel(params);
			if(list.size()<=0){
				// 数据库中没有对应的模板
				return null;
			}else {
				return list.get(0);
			}
	}

	/**
	 * 获取最新的模板信息
	 * @param tableName 表名称
	 * @return
	 * @throws Exception
	 */
	public ImportTemplateManage getTemplateInfoByTableName(String tableName) throws Exception {
		List <ImportTemplateManage> list = importTemplateManageDao.findTemplateInfoByTableName(tableName);
		if(list.size()<=0){
			// 数据库中没有对应的模板
			return null;
		}else {
			return list.get(0);
		}
	}

     /**
     *@param @param params
     *@return ImportTemplateManage
     *@date 2023/8/16  22:31
     *@description 下载模板
     *
     */
	public ImportTemplateManage getTemplateInfoById(ImportTemplateManage params) throws Exception {

		List <ImportTemplateManage> list = importTemplateManageDao.findTemplateInfoById(params);
		if(list.size()<=0){
			// 数据库中没有对应的模板
			return null;
		}else {
			return list.get(0);
		}
	}


	/**
	 *@param @param params
	 *@return ImportTemplateManage
	 *@date 2023/8/16  22:31
	 *@description 下载模板
	 *
	 */
	public  SqlResult<ImportTemplateManage> findTemplateInfoOrderById(SqlParam<ImportTemplateManage> params) throws Exception {

		return importTemplateManageDao.findTemplateInfoOrderById(params);
	}



	public String addTemplateInfoByUpload(ImportTemplateManage params,ImportTemplateManage oldParams) throws Exception {
		try {
			params.setTemplateStatus("1");
			params.setImpDate(DateUtil.getNowDate());
			params.setImpTime(DateUtil.getNowTime());

			if(null != oldParams){
				// 数据库中有对应的模板, 旧模板停用
				oldParams.setTemplateStatus("0");
				importTemplateManageDao.updTemplateInfo(oldParams);
			}
			importTemplateManageDao.addTemplateInfoByModel(params);
			return RequestSupport.updateReturnJson(true,  "添加成功！", null).toString();
		} catch (Exception e){
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "添加失败！", null).toString();
		}
	}

	/**
	 * 模版上传字段自动配置
	 * @param importTemplateInfo
	 * @param  localFile
	 * @return
	 * @throws Exception
	 */
	public void fieldAutoGenerate(ImportTemplateManage importTemplateInfo, File localFile) throws Exception {
		// 获取数据库名称
		String database = null;
		if (!databases.isEmpty()){
			database = databases.split(",")[0];
		}
		// 数字类型
		String[] numTypes = {"TINYINT","SMALLINT","MEDIUMINT","INT","BIGINT","FLOAT","DOUBLE","DECIMAL"};
		ArrayList<String> numTypeList = new ArrayList<>(Arrays.asList(numTypes));
		// 获取表英文名
		String tableName = importTemplateManageDao.findTableName(importTemplateInfo);
		// 删除该表字段配置
		importTemplateManageField01Dao.deleteTableField(importTemplateInfo);
		// 查询字段信息参数
		Map<String, Object> params = new HashMap<>();
		params.put("tableName", tableName);
		params.put("database", "wmurrs");
		String fileName = localFile.getName();
		//文件后缀
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
		List<Object[]> list = null;
		if ("xlsx".equalsIgnoreCase(extension)) {
			list = ExcelUtils.readXSSFExcel(localFile, null);
		} else if ("xls".equalsIgnoreCase(extension) || "et".equalsIgnoreCase(extension)) {
			list = ExcelUtils.readHSSFExcel(localFile, null);
		}
		// 获取表头
		if (list != null) {
			// TODO: 2023/10/13 改成List<Map> 值为index
			int rowStart = Tools.strIsEmpty(importTemplateInfo.getRowStart()) ? 1 : Integer.parseInt(importTemplateInfo.getRowStart());
			AtomicInteger index = new AtomicInteger(1);
			Map<String, Integer> columnComments = Stream.of(list.get(rowStart-1)).map(Object::toString).collect(Collectors.toMap(
					(key) -> key,
					(value) -> index.getAndIncrement(),
					Math::max
			));
			// 字段顺序
			Map<String, Object> map = new HashMap<>();
			// lizs 优化：把所有字段信息一次查出，减少数据库连接
			List<SqlRow> tableFieldInfo = null;
			try {
				tableFieldInfo = importTemplateManageDao.findTableFieldInfo(params);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (tableFieldInfo == null || tableFieldInfo.isEmpty()) {
				throw new Exception("无法查询到字段信息");
			}
			tableFieldInfo.forEach(tableField -> {
				String columnComment = tableField.getString("COLUMN_COMMENT");
				String columnName = tableField.getString("COLUMN_NAME");
				String dataType = tableField.getString("DATA_TYPE");
				Integer indexNum = columnComments.get(columnComment);
				if (indexNum != null) {
					// 获取字段的字段英文名，字段类型
					map.put("systemTableName", importTemplateInfo.getSystemTableName());
					map.put("columnName", columnName);
					map.put("columnComment", columnComment);
					// todo 需要转换成字典
					// 判断数据类型
					String isDict = null;
					try {
						List<SqlRow> tableFieldDictInfo = importTemplateManageDao.findTableFieldDictInfo(map);
						if (!tableFieldDictInfo.isEmpty()){
							isDict = tableFieldDictInfo.get(0).getString("field_name");
						}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					if (null != isDict) {
						// 字典类型
						map.put("dataType", "03");
					} else if (numTypeList.contains(dataType.toUpperCase())) {
						// 数字类型
						map.put("dataType", "02");
					}else{
						// 字符串
						map.put("dataType", "01");
					}
					map.put("index", String.valueOf(indexNum));
					// 插入 import_template_manage_field_01 表
					try {
						importTemplateManageField01Dao.updateTableField(map);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
		}
	}


	@API(desc = "更新模板状态",  auth = APIAuth.YES)
	public String updateTemplateInfoStatusStop(SqlParam<ImportTemplateManage> params) throws Exception {
		try {
				params.getModel().setTemplateStatus("0");
				importTemplateManageDao.updTemplateInfo(params.getModel());
			return RequestSupport.updateReturnJson(true,  "成功！", null).toString();
		} catch (Exception e){
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "失败！", null).toString();
		}
	}

	@API(desc = "更新模板状态",  auth = APIAuth.YES)
	public String updateTemplateInfoStatusOpen(SqlParam<ImportTemplateManage> params) throws Exception {
		try {
			params.getModel().setTemplateStatus("1");
			importTemplateManageDao.updTemplateInfo(params.getModel());
			return RequestSupport.updateReturnJson(true,  "成功！", null).toString();
		} catch (Exception e){
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "失败！", null).toString();
		}
	}

	/**
	 * 导入EXCEL文件
	 * @param importTemplateInfo
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public void implExcelData(int importFlag,ImportTemplateManage importTemplateInfo, File file, String sheetName) throws Exception {
		// 获取模板完整信息
		List<ImportTemplateManage> tempDataList = importTemplateManageDao.findTemplateInfoByModel(importTemplateInfo);
		if (tempDataList == null || tempDataList.isEmpty()) {
			throw new Exception("请配置模板");
		}
		ImportTemplateManage importTemplateManage = tempDataList.get(0);
		importTemplateManage.setSysDataDate(importTemplateInfo.getSysDataDate());
		// 验证重复报送
	   String isRepeatSubmission =SysUtil.getSystemParamsByParaid("is_repeat_submission");
	   if(!StringUtils.isEmpty(isRepeatSubmission) &&  "0".equals(isRepeatSubmission)){
		   List<ImportTemplateManage> list = importTemplateManageDao.findRepeatSubmission(importTemplateManage);
			if(list.size()>0){
				throw new Exception("报送记录已存在，不允许重复报送");
			}
   		}
	    // 数据版本号获取 根据日期
		String version = getDateVersionByDate(importTemplateManage);
		if(!StringUtils.isEmpty(importTemplateInfo.getSysDataVersion())){
			version = importTemplateInfo.getSysDataVersion();
		}
		importTemplateManage.setSysDataVersion(version);

		importTemplateManage.setSysDataStatus(importTemplateInfo.getSysDataStatus());

		if (file != null) {
			if(2==importFlag){//标志为2时删除
				deleteDataByVersion(importTemplateManage);
			}

			//解析 excel
			if(file.getName().endsWith(".zip")) {
				log.info("开始解析excle入库");
				/**如果传入文件是zip压缩文件,则解压后再进行解析*/
				List<File> fileList = extractZipToFileList(file, file.getParent());
				log.info("压缩包中dat/exceld的文件个数为："+fileList.size());
				AtomicInteger total=new AtomicInteger();//总数量
				Instant startTime = Instant.now();
				// 初始化线程池
				String newMaximumPoolSize = SysUtil.getSystemParamsByParaid("90000030001");
				String newCorePoolSize = SysUtil.getSystemParamsByParaid("90000030002");
				int size = Integer.parseInt(newMaximumPoolSize);
				int coreSize = Integer.parseInt(newCorePoolSize);
				ExecutorService executorService = new ThreadPoolExecutor(coreSize, size, 0L, TimeUnit.MILLISECONDS,
						new LinkedBlockingQueue<>());
				List<Future> futureList = new ArrayList<>();
				if(fileList.size() >1){
					//大于1个文件时，采用原线程池方法
					for(File singleFile : fileList){
						Runnable runnable = () ->  {
							try {
								total.getAndAdd(1);
								processExcelFileData(importTemplateManage, singleFile.getName(), singleFile, sheetName);
								singleFile.delete();//删除解压后文件
							} catch (Exception e) {
								log.error(e.getMessage(),e);
								throw new RuntimeException(e);
							}
						};
						Future<?> future = executorService.submit(runnable);
						futureList.add(future);

					}
					for (Future future : futureList) {
						try {
							future.get();
						} catch (Exception e) {
							log.error(e.getMessage(),e);
							throw new PromptException(e.getMessage());
						}
					}
					executorService.shutdown();

				}else if(fileList.size() == 1){
					File singleFile = fileList.get(0);
					try {
						total.getAndAdd(1);
						processExcelFileData(importTemplateManage, singleFile.getName(), singleFile, sheetName);
						singleFile.delete();//删除解压后文件
					} catch (Exception e) {
						log.error(e.getMessage(),e);
						throw new RuntimeException(e);
					}
				}
				// 获取当前系统时间点
				Instant endTime = Instant.now();
				// 计算时间间隔
				Duration duration = Duration.between(startTime, endTime);
				long seconds = duration.getSeconds();
				log.info("解析excle入库用时{}秒", seconds);
			} else {
				/**单文件导入解析Excel入库*/
				processExcelFileData(importTemplateManage, file.getName(), file, sheetName);
			}
			if(1==importFlag){//标志为1时更新旧版本数据
				updateOldDataByVersion(importTemplateManage);
			}

			// 新增报送数据导入日志
			ImportTemplateDataLog importTemplateDataLog = new ImportTemplateDataLog();
			importTemplateDataLog.setImportTemplateManageId(importTemplateManage.getId());
			importTemplateDataLog.setReportDate(importTemplateManage.getSysDataDate());
			importTemplateDataLog.setImpDate(DateUtil.getTimestamp19());
			importTemplateDataLog.setSysDataVersion(importTemplateManage.getSysDataVersion());
			Map<String, Object> map = BeanUtil.beanToMap(importTemplateDataLog);
			FetcherData<ImportTemplateDataLog> fetcherData = new FetcherData<>(map, ImportTemplateDataLog.class);
			importTemplateDataLogDao.addImportTemplateDataLog(fetcherData);

		}else {
			throw new PromptException("上传文件为空");
		}
	}

	/**
	 * 导入文件管理数据
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public void importBaseReportFileMange(File file) throws Exception {
		try {
			// ZIP文件
			String remotePath = SysUtil.getSystemParamsByParaid("90000051312");
			if (file.getName().endsWith(".zip")) {
				// 如果传入文件是zip压缩文件,则解压后再进行解析
				List<File> fileList = extractAllZipToFileList(file, file.getParent());

				if (CollectionUtil.isEmpty(fileList)) {
					throw new Exception("上传文件为空");
				}

				for (File singleFile : fileList) {
					addBaseReportFileMange(singleFile, file, remotePath);
				}
			} else {
				addBaseReportFileMange(file, file, remotePath);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 导入文件管理数据
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public String importProdReportFileMange(File file,String importmodel) throws Exception {
		String prod_msg="";
		try {
			// ZIP文件
			String remotePath = SysUtil.getSystemParamsByParaid("90000051312");
			if ("02".equals(importmodel)) {
				// 如果传入文件是zip压缩文件,则解压后再进行解析
				List<File> fileList = extractAllZipToFileList(file, file.getParent());
				if (CollectionUtil.isEmpty(fileList)) {
					throw new Exception("上传文件为空");
				}
				int zipNum = 0,errNum = 0;
				for (File singleFile : fileList) {
					if(singleFile.getName().toLowerCase().endsWith(".zip")){
						zipNum ++;
						prod_msg=prod_msg+addProdReportFileMange(singleFile, file, remotePath);
						if(prod_msg.length()>0){
							errNum ++;
							prod_msg=prod_msg+";";
						}
					}
				}
				if(StringUtils.isNotBlank(prod_msg) && zipNum != errNum){
					prod_msg ="部分导入失败！错误信息为【<br/>" +prod_msg+"】";
				}else if(StringUtils.isNotBlank(prod_msg) && zipNum == errNum){
					prod_msg ="全部导入失败！错误信息为【<br/>" +prod_msg+"】";
				}
			} else {
				prod_msg=addProdReportFileMange(file, file, remotePath);
				if(prod_msg.length()>0){
					prod_msg ="文件导入失败！错误信息为【<br/>" +prod_msg+"】";
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return prod_msg;
	}

	private void addBaseReportFileMange (File singleFile, File file, String remotePath) throws Exception {
		try {
			String split = SysUtil.getSystemParamsByParaid("90000051311");
			String jobno = String.valueOf(SysUtil.getSysUserParams().get("JOBNO"));
			String username = String.valueOf(SysUtil.getSysUserParams().get("username"));

			if (StringUtils.isEmpty(split)) {
				throw new PromptException("请在数据字典中配置产品名称分隔符");
			}

			String prodctName = "";
			String[] splits = split.split(",");
			String fileName = singleFile.getName();
			List<BaseReportFileManage> baseReportFileManageList = new ArrayList<>();

			for (String str : splits) {
				if (StringUtils.contains(fileName, str)) {
					prodctName = fileName.split(str)[0];
				}

				//根据文件名称模糊查询除所有的产品
				BaseReportFileManage param = new BaseReportFileManage();
				param.setProdNmFu(prodctName);
				baseReportFileManageList = baseReportFileManageDao.findDwdPrdPrdBasInfs(param);

				if (CollectionUtils.isNotEmpty(baseReportFileManageList)) {
					break;
				}
			}

			// 存入oss
			String remoteFile;
			String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
			String newFileName = fileName.substring(0,fileName.lastIndexOf(".")) +"-"+DateUtil.getTimestamp17()+"."+extension;

			if (!remotePath.endsWith("\\") && !remotePath.endsWith("/")) {
				remoteFile = remotePath + "/" +  DateUtil.getNowDate() + "/" +newFileName;
			} else {
				remoteFile = remotePath + DateUtil.getNowDate() + "/" +newFileName;
			}

			try {
				FileTransfer transfer =  FileTransferHelpler.getTransfer();
				transfer.uploadFileAndDisconnect(singleFile.getAbsolutePath(), remoteFile);
			} catch (Exception e) {
				log.error("文件上传oss失败", e);
				throw new PromptException("文件上传oss失败");
			}

			if (CollectionUtils.isEmpty(baseReportFileManageList)) {
				BaseReportFileManage baseReportFileManage = new BaseReportFileManage();
				baseReportFileManage.setFileName(fileName);// 文件名称
				baseReportFileManage.setFileType("1");// 文件类型
				baseReportFileManage.setZipfilename(file.getName());// 压缩文件名称

				baseReportFileManage.setOperaterno(jobno);// 操作员编号
				baseReportFileManage.setOperatername(username);// 操作员名称

				baseReportFileManage.setCrtDate(DateUtil.getNowDate());// 上传日期
				baseReportFileManage.setCrtTime(DateUtil.getNowTime());// 上传时间

				baseReportFileManage.setUpdDate(DateUtil.getNowDate());// 更新日期
				baseReportFileManage.setUpdTime(DateUtil.getNowTime());// 更新时间

				baseReportFileManage.setRemoteFile(remoteFile);// oss文件路径
				baseReportFileManageDao.addBaseReportFileManage(baseReportFileManage);
			} else {
				for (BaseReportFileManage baseReportFileManage : baseReportFileManageList) {
					baseReportFileManage.setFileName(fileName);// 文件名称
					baseReportFileManage.setFileType("1");// 文件类型
					baseReportFileManage.setZipfilename(file.getName());// 压缩文件名称

					baseReportFileManage.setOperaterno(jobno);// 操作员编号
					baseReportFileManage.setOperatername(username);// 操作员名称

					baseReportFileManage.setCrtDate(DateUtil.getNowDate());// 上传日期
					baseReportFileManage.setCrtTime(DateUtil.getNowTime());// 上传时间

					baseReportFileManage.setUpdDate(DateUtil.getNowDate());// 更新日期
					baseReportFileManage.setUpdTime(DateUtil.getNowTime());// 更新时间

					baseReportFileManage.setRemoteFile(remoteFile);// oss文件路径
					baseReportFileManageDao.addBaseReportFileManage(baseReportFileManage);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			FileUtil.delFile(singleFile);
		}
	}

	/**
	 * 
	 * @param singleFile 单个产品的压缩文件
	 * @param file       多产品或单产品压缩文件
	 * @param remotePath
	 * @return
	 * @throws Exception
	 */
	private String addProdReportFileMange (File singleFile, File file, String remotePath) throws Exception {
		StringBuffer returnStr = new StringBuffer();
		try {
			String jobno = String.valueOf(SysUtil.getSysUserParams().get("JOBNO"));
			String username = String.valueOf(SysUtil.getSysUserParams().get("username"));
			String prodCd = "";
			String fileName = singleFile.getName();
			if (StringUtils.contains(fileName, ".")) {
				prodCd = fileName.split("\\.")[0];
			}
			List<BaseReportFileManage> baseReportFileManageList = new ArrayList<>();
			BaseReportFileManage baseReportFileManage = new BaseReportFileManage();
			baseReportFileManage.setProdCd(prodCd);

			//判断代码是否存在，根据产品基本信息的ods表判断，因为导入的申报登记，会直接写入ods表
			baseReportFileManageList = baseReportFileManageDao.findOdsPrdPrdBasInfsBy(baseReportFileManage);
			if(baseReportFileManageList.size()>0){
				baseReportFileManage.setProdNmFu(baseReportFileManageList.get(0).getProdNmFu());
				baseReportFileManage.setOperationMode(baseReportFileManageList.get(0).getOperationMode());
			}else{
				returnStr.append(prodCd+",不存在，跳过执行;<br/>");
			}
			baseReportFileManage.setFileName(fileName);// 文件名称
			baseReportFileManage.setFileType("2");// 文件类型
			baseReportFileManage.setZipfilename(file.getName());// 压缩文件名称

			List<SqlRow> fileManages =  baseReportFileManageDao.findFileManagesByBody(baseReportFileManage);
			if(fileManages.size() > 0){
				returnStr.append(prodCd+"已存在九大附件记录，若文件有误请删除记录后重新上传！"+fileName+"已跳过执行;<br/>");
			}
			if(StringUtils.isNotBlank(returnStr)){
				return returnStr.toString();
			}
			// 如果传入文件是zip压缩文件,则解压后再进行解析
			List<File> fileList = extractAllZipToFileList(singleFile, singleFile.getPath() );
			if (CollectionUtil.isEmpty(fileList)) {
				returnStr.append(fileName+"中为空文件，请检查后再上传！;<br/>");
			}else{
				String parentNameAll = "";
				for (File file01 : fileList) {
					String parentName =  file01.getParent();
					if (StringUtils.contains(parentName, "/")) {
						parentName = parentName.substring(parentName.lastIndexOf("/"));
					}else if (StringUtils.contains(parentName, "\\")) {
						parentName = parentName.substring(parentName.lastIndexOf("\\"));
					}
					parentNameAll  = parentNameAll+","+parentName;
					//检查文件是否符合规范
					returnStr.append(checkFile(fileName,file01,"01","报告主文件"));
					returnStr.append(checkFile(fileName,file01,"02","理财产品可行性评估报告"));
					returnStr.append(checkFile(fileName,file01,"03","内部审核文件"));
					returnStr.append(checkFile(fileName,file01,"04","对相关方的尽职调查文件 "));
					returnStr.append(checkFile(fileName,file01,"05","与相关方签署的法律文件"));
					returnStr.append(checkFile(fileName,file01,"06","理财产品销售文件"));
					returnStr.append(checkFile(fileName,file01,"07","理财产品说明书"));
					returnStr.append(checkFile(fileName,file01,"08","理财产品宣传材料"));
					returnStr.append(checkFile(fileName,file01,"09","流动性风险评估文件"));
				}
				if(StringUtils.isNotBlank(parentNameAll)){
					if(!parentNameAll.contains("02")){ returnStr.append(fileName+"中不存在02-理财产品可行性评估报告，请检查后重新上传！<br/>"); }
					if(!parentNameAll.contains("03")){ returnStr.append(fileName+"中不存在03-内部审核文件，请检查后重新上传！<br/>"); }
					if(!parentNameAll.contains("04")){ returnStr.append(fileName+"中不存在04-对相关方的尽职调查文件，请检查后重新上传！<br/>"); }
					if(!parentNameAll.contains("05")){ returnStr.append(fileName+"中不存在05-与相关方签署的法律文件，请检查后重新上传！<br/>"); }
					if(!parentNameAll.contains("06")){ returnStr.append(fileName+"中不存在06-理财产品销售文件，请检查后重新上传！<br/>"); }
					if(!parentNameAll.contains("07")){ returnStr.append(fileName+"中不存在07-理财产品说明书，请检查后重新上传！<br/>"); }
					if(!parentNameAll.contains("08")){ returnStr.append(fileName+"中不存在08-理财产品宣传材料，请检查后重新上传！<br/>"); }
					if(StringUtils.isNotBlank(baseReportFileManage.getOperationMode()) ){
						if("03,04".contains(baseReportFileManageList.get(0).getOperationMode()) ){
							if(!parentNameAll.contains("09")){ returnStr.append(fileName+"中不存在09-流动性风险评估文件，请检查后重新上传！<br/>"); }
						}
					}
				}
				String parentPath = singleFile.getPath();
				if(parentPath.contains(".")){
					parentPath = parentPath.substring(0,parentPath.lastIndexOf("."))+ File.separator ;
				}
				//校验全部走完之后，删除包里面的临时文件
				deleteFolder(parentPath);
//				for (File file01 : fileList) {
//					FileUtil.delFile(file01);
//				}
			}
			if(StringUtils.isNotBlank(returnStr)){
				return returnStr.toString();
			}

			baseReportFileManage.setOperaterno(jobno);// 操作员编号
			baseReportFileManage.setOperatername(username);// 操作员名称
			baseReportFileManage.setCrtDate(DateUtil.getNowDate());// 上传日期
			baseReportFileManage.setCrtTime(DateUtil.getNowTime());// 上传时间
			baseReportFileManage.setUpdDate(DateUtil.getNowDate());// 更新日期
			baseReportFileManage.setUpdTime(DateUtil.getNowTime());// 更新时间
			// 存入oss
			String remoteFile;
			if (!remotePath.endsWith("\\") && !remotePath.endsWith("/")) {
				remoteFile = remotePath + "/zzfile/NineAttachments/" +fileName;
			} else {
				remoteFile = remotePath + "/zzfile/NineAttachments/" +fileName;
			}

			try {
				FileTransfer transfer =  FileTransferHelpler.getTransfer();
				transfer.uploadFileAndDisconnect(singleFile.getAbsolutePath(), remoteFile);
			} catch (Exception e) {
				log.error("文件上传oss失败", e);
				throw new PromptException("文件上传oss失败");
			}
			baseReportFileManage.setRemoteFile(remoteFile);// oss文件路径
			baseReportFileManageDao.addBaseReportFileManage(baseReportFileManage);
		} catch (Exception e) {
			throw new Exception(e.getMessage().replaceAll("java.lang.Exception: ",""));
		} finally {
			FileUtil.delFile(singleFile);
		}
		return returnStr.toString();
	}

	private void processExcelFileData (ImportTemplateManage importTemplateManage, String fileName, File tempFile, String sheetName) throws Exception {
		//文件后缀
		if ("01".equals(importTemplateManage.getImportType()) &&  !fileName.endsWith(".dat")  &&  !fileName.endsWith(".csv") ) {//横表入库
			int rowNum=1;
			if(StringUtils.isNotEmpty(importTemplateManage.getRowStart())){
				rowNum=Integer.parseInt(importTemplateManage.getRowStart());
			}
			if(fileName.toLowerCase().endsWith(".xls")){
				EasyExcel.read(new FileInputStream(tempFile)).headRowNumber(rowNum).registerReadListener(new ExcelImportListener<Map>((ICallback<List<Map>>) cacheList -> {
					saveExcelH(importTemplateManage, cacheList);
				})).excelType(ExcelTypeEnum.XLS).sheet().doRead();
			}else if(fileName.toLowerCase().endsWith(".xlsx")){
				EasyExcel.read(new FileInputStream(tempFile)).headRowNumber(rowNum).registerReadListener(new ExcelImportListener<Map>((ICallback<List<Map>>) cacheList -> {
					saveExcelH(importTemplateManage, cacheList);
				})).excelType(ExcelTypeEnum.XLSX).sheet().doRead();
			}
		}else if ("02".equals(importTemplateManage.getImportType())) {//纵表入库
			String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
			List<Object[]> list=null;
			if("xlsx".equalsIgnoreCase(extension)){
				list = ExcelUtils.readXSSFExcel(tempFile, sheetName);
			}else if("xls".equalsIgnoreCase(extension)||"et".equalsIgnoreCase(extension)){
				list = ExcelUtils.readHSSFExcel(tempFile, sheetName);
			}
			saveExcelZ(importTemplateManage, list);
		}else if ("01".equals(importTemplateManage.getImportType()) && importTemplateManage.getTableName().contains("report_zg")  &&  fileName.endsWith(".dat") ) {//zip里面是dat文件的情况
			String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

			if("dat".equalsIgnoreCase(extension)){
				//删除sys_data_status = 0 的数据
				String deleteSql = "delete from "+ importTemplateManage.getTableName()  + " where sys_data_status = '0' ";
				comnDao.update(deleteSql);

				ImportTemplateManageField01 importTemplateManageField01 = new ImportTemplateManageField01();
				importTemplateManageField01.setSystemTableName(importTemplateManage.getSystemTableName());
				List<ImportTemplateManageField01> fieldList = importTemplateManageField01Dao.findTemplateFieldListByModel(importTemplateManageField01);
				// 新增日期处理
				String containSql = "select  *  from information_schema.`COLUMNS` c where TABLE_NAME = '"+fieldList.get(0).getTableName()+"' and COLUMN_NAME ='create_date' ";
				List<SqlRow> rows_excel = comnDao.findRows(containSql);

				if (fieldList == null || fieldList.isEmpty()) {
					throw new Exception("请配置模板字段列表");
				}else{
					String  files01 = com.kayak.dps.utils.FileUtil.readFile(tempFile,"utf-8");
					String[] list = files01.split("\n");
					List<String> lis03 = new ArrayList<>();
					for (String s:list){
						lis03.add(s);
					}
					if(lis03.size() > 5000){
						log.info(">>>>>dat文件条数为：<<<<<<"+list.length);
						List<List<String>> splits = CollectionUtil.split(lis03, 5000);
						int a = splits.size();
						// 创建固定大小的线程池
						ExecutorService executorService = Executors.newFixedThreadPool(a);
						// 提交任务到线程池执行
						for (int i = 0; i < a; i++) {
							List<String> list02 = splits.get(i);
							log.info(">>>>>第"+i+"次循环：list02长度为<<<<<<"+list02.size());
							final int taskId = i;
							executorService.submit(() -> {
								log.info("Executing task " + taskId);
								try {
									saveBydat(importTemplateManage,list02,fieldList,rows_excel);
								} catch (InterruptedException e) {
									Thread.currentThread().interrupt();
								} catch (Exception e) {
									e.printStackTrace();
								}
							});
						}
						// 关闭线程池，等待所有任务完成
						try {
							executorService.shutdown();
							executorService.awaitTermination(10000, TimeUnit.SECONDS);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						} finally {
							if (!executorService.isTerminated()) {
								executorService.shutdownNow();
							}
						}
					}else{
						log.info(">>>>>dat文件条数为：<<<<<<"+list.length);
						saveBydat(importTemplateManage,lis03,fieldList,rows_excel);
					}
					log.info(">>>>>执行dat文件结束<<<<<<");
				}
			}
		}else if ("01".equals(importTemplateManage.getImportType()) &&  fileName.toLowerCase().endsWith(".csv") ) {//CSV文件
			List<Map> list = new ArrayList<>();

			String  files01 = com.kayak.dps.utils.FileUtil.readFile(tempFile,"utf-8");
			if(StringUtils.isNotBlank(files01)){
				String[] rows01 = files01.split("\n");
				List<String> lis03 = new ArrayList<>();
				int startRow = 0;
				if (importTemplateManage.getRowStart().compareTo("0") > 0 ){
					startRow = Integer.valueOf(importTemplateManage.getRowStart());
				}
				for (int a  = startRow;a<rows01.length;a++){
					String row = rows01[a];
					String[] values01 = row.split(",");
					HashMap map = new HashMap();
					for(int i  = 0;i<values01.length;i++){
						map.put(i,values01[i]);
					}
					if(map != null ){
						list.add(map);
					}
				}
				if(list != null && list.size() >0){
					List<List<Map>> splits = CollectionUtil.split(list, 5000);
					log.info("=========数据总条数："+list.size()+"=======");
					int cunt = 0;
					for(List<Map> splitsList : splits){
						saveExcelH(importTemplateManage, splitsList);
						cunt = cunt + splitsList.size();
						log.info("=====数据总条数:"+list.size()+";已写入完成条数："+cunt);
					}
				}
			}
		}
	}


	public XSSFWorkbook exportExcelData(ImportTemplateManage importTemplateInfo, File file) throws Exception {

		// 获取模板完整信息
		List<ImportTemplateManage> tempDataList = importTemplateManageDao.findTemplateInfoByModel(importTemplateInfo);
		ImportTemplateManage importTemplateManage = tempDataList.get(0);
		importTemplateManage.setSysDataDate(importTemplateInfo.getSysDataDate());
		importTemplateManage.setSysDataVersion(importTemplateInfo.getSysDataVersion());

		List<SqlRow> sqlRowList = importTemplateManageDao.findTemplateData(importTemplateManage);


			//解析 excel
			List<Object[]> list = ExcelUtils.readXSSFExcel(file,null);

			List<Object[]>  reportList =  new ArrayList<>();
			if ("01".equals(importTemplateManage.getImportType())) {
				//横表查询
				  reportList =	reportExcelH (importTemplateManage, list, sqlRowList);
			}else if ("02".equals(importTemplateManage.getImportType())) {
				//纵表查询
				  reportList =	reportExcelZ(list, sqlRowList);
			}

		return ExcelUtils.createXSSFExcel(reportList);
	}

	/**
	 * dat数据对比
	 * @param importTemplateManage
	 * @param list
	 * @throws Exception
	 */
	private void saveBydat(ImportTemplateManage importTemplateManage, List<String> list,List<ImportTemplateManageField01> fieldList,List<SqlRow> rows_excel) throws Exception {

		//2.sql拼接
		StringBuffer sqlStr = new StringBuffer("INSERT INTO ");
		// 表名拼接
		sqlStr.append(fieldList.get(0).getTableName()).append(" ( ");
		// 字段拼接
		int k=7;
		for (int i = 0; i < fieldList.size(); i++) {
			sqlStr.append(fieldList.get(i).getDatabaseColumnCode()).append(" , ");
		}
		sqlStr.append("sys_data_status,");
		sqlStr.append("sys_data_source,");
		sqlStr.append("sys_data_version,");
		sqlStr.append("register_status,");
		sqlStr.append("register_serno,");
		sqlStr.append("report_date,");
		sqlStr.append("imp_date");
		if(rows_excel.size()>0){
			sqlStr.append(",create_date");
			k=8;
		}
		sqlStr.append(" )  VALUES ( ");
		// 占位符拼接
		for (int i = 0; i < fieldList.size() + k; i++) {
			// 数据类型判断
			sqlStr.append("?,");
		}
		sqlStr.setLength(sqlStr.length() - 1);
		sqlStr.append(")");

		comnDao.doTrans(() -> {
			Connection connection = comnDao.getConnection();
			PreparedStatement ps = connection.prepareStatement(sqlStr.toString());
			try {

				for (int ii = 0; ii < list.size(); ii ++) {
					String s = list.get(ii);
					String[] contents = s.split("\\|",-1);//1条数据多个字段

					String value;
					// 数据结构转换
					for (int i = 0; i < contents.length; i++) {
						String objValue = contents[i];
						ImportTemplateManageField01 field = fieldList.get(i);
						if (objValue == null || StringUtils.isEmpty(objValue)) {//为空置null
							value = null;
						} else {
							if (field.getColumnType().equals("03")) {
								// 字典类型   使用 " "切割 并默认取首个值
								value = objValue.trim().split(" ")[0];
							} else if (field.getColumnType().equals("02") || field.getColumnType().equals("01")) {
								// 类型为数字、字符串的需要特殊处理
								value = objValue.trim();
								String columnUnit = field.getColumnUnit();
								if (NumberUtils.isNumber(value) && NumberUtils.isNumber(columnUnit)) {
									BigDecimal bigDataValue = new BigDecimal(value);
									BigDecimal bigColumnUnit = new BigDecimal(columnUnit);
									BigDecimal bigDataValueUnit = bigDataValue.multiply(bigColumnUnit);
									value = bigDataValueUnit.toString();
								}
							} else {
								value = objValue.trim();
							}
							// 字符串以及其他类型  不做特殊处理
						}
						ps.setString(i + 1, value);
					}
					ps.setString(contents.length + 1, importTemplateManage.getSysDataStatus());//sys_data_status 1
					ps.setString(contents.length + 2, "2");//sys_data_source 2
					ps.setString(contents.length + 3, importTemplateManage.getSysDataVersion());//sys_data_version 2
					ps.setString(contents.length + 4, "0");//register_status 2
					ps.setString(contents.length + 5, UUID.randomUUID().toString());//register_serno 2
					ps.setString(contents.length + 6, importTemplateManage.getSysDataDate());// report_date
					ps.setString(contents.length + 7, DateUtil.getNowDate());// imp_date
					if(rows_excel.size()>0){
						ps.setString(contents.length + 8, DateUtil.getNowDate());// create_date
					}
					ps.addBatch();
				}
				log.info(">>>>>sqlStr:>>>"+sqlStr);
				ps.executeBatch();
			} catch (Exception e) {
				log.error("ZIP文件导入异常!", e);
				throw new PromptException(e.getMessage());
			} finally {
				ps.close();
			}
		});

	}

	/**
	 *@param @param
	 *@return void
	 *@date 2023/8/16  16:02
	 *@description  横表解析方法
	 *
	 */
	private void saveExcelH(ImportTemplateManage importTemplateManage, List<Map> list) throws Exception {
		ImportTemplateManageField01 importTemplateManageField01 = new ImportTemplateManageField01();
		importTemplateManageField01.setSystemTableName(importTemplateManage.getSystemTableName());

		//1.查询横表字段表
		List<ImportTemplateManageField01> fieldList = importTemplateManageField01Dao.findTemplateFieldListByModel(importTemplateManageField01);
		if (fieldList == null || fieldList.isEmpty()) {
			throw new Exception("请配置模板字段列表");
		}
		//2.sql拼接
		StringBuffer sqlStr = new StringBuffer("INSERT INTO ");

		// 表名拼接
		sqlStr.append(fieldList.get(0).getTableName()).append(" ( ");
		String tableName = fieldList.get(0).getTableName();
		// 新增日期处理
		String containSql = "select  *  from information_schema.`COLUMNS` c where TABLE_NAME = '"+fieldList.get(0).getTableName()+"' and COLUMN_NAME ='create_date' ";
		List<SqlRow> rows_excel = comnDao.findRows(containSql);
		// 字段拼接
		int k=7;
		for (int i = 0; i < fieldList.size(); i++) {
			sqlStr.append(fieldList.get(i).getDatabaseColumnCode()).append(" , ");
		}
		sqlStr.append("sys_data_status,");
		sqlStr.append("sys_data_source,");
		sqlStr.append("sys_data_version,");
		sqlStr.append("register_status,");
		sqlStr.append("register_serno,");
		sqlStr.append("report_date,");
		sqlStr.append("imp_date");
		if(rows_excel.size()>0){
			sqlStr.append(",create_date");
			k=8;
		}
		if ("app_asset_debt_register_info".equals(tableName)) {
			sqlStr.append(",theory_report_start_date");
		}
		sqlStr.append(" )  VALUES ( ");
		// 占位符拼接
		for (int i = 0; i < fieldList.size() + k; i++) {
			// 数据类型判断
			sqlStr.append("?,");
		}
		if ("app_asset_debt_register_info".equals(tableName)) {
			sqlStr.append("?,");
		}
		sqlStr.setLength(sqlStr.length() - 1);
		sqlStr.append(")");
		String theoryReportStartDate = getTheoryReportStartDate(importTemplateManage.getSysDataDate());
		comnDao.doTrans(() -> {
			Connection connection = comnDao.getConnection();
			PreparedStatement ps = connection.prepareStatement(sqlStr.toString());
			try {
				String value;
				// 数据结构转换
				for (int i = 0; i < list.size(); i++) {
					Map rowMap = list.get(i);
					//由于可能存在字典值如key+空格+value情况,对值做特殊处理,用空格split后去[0]
					int j = 0;//字段下标
					for (; j < fieldList.size(); j++) {
						ImportTemplateManageField01 field = fieldList.get(j);
						//纵坐标
						int columnSerial = Integer.parseInt(field.getTemplateColumnSerial());
						Object objValue = rowMap.get(columnSerial - 1);

						if (objValue == null || StringUtils.isEmpty((String)objValue)) {//为空置null
							value = null;
						}
						else {
							if(tableName.equals("app_prod_trans_regist_info")){//交易信息登记，判断交易日的问题
								if(field.getDatabaseColumnCode().equals("TRADE_DATE")&& objValue != null  && ((String)objValue).contains("-")){
									objValue = ((String)objValue).replace("-","");
								}
							}

							if (field.getColumnType().equals("03")) {
								// 字典类型   使用 " "切割 并默认取首个值
								value = objValue.toString().trim().split(" ")[0];
							} else if (field.getColumnType().equals("02") || field.getColumnType().equals("01")) {
								// 类型为数字、字符串的需要特殊处理
								value = objValue.toString().trim();
								String columnUnit = field.getColumnUnit();
								if (NumberUtils.isNumber(value) && NumberUtils.isNumber(columnUnit)) {
									BigDecimal bigDataValue = new BigDecimal(value);
									BigDecimal bigColumnUnit = new BigDecimal(columnUnit);
									BigDecimal bigDataValueUnit = bigDataValue.multiply(bigColumnUnit);
									value = bigDataValueUnit.toString();
								}
							} else {
								value = objValue.toString().trim();
							}
							// 字符串以及其他类型  不做特殊处理
						}
						ps.setString(j + 1, value);
					}
					ps.setString(j + 1, importTemplateManage.getSysDataStatus());//sys_data_status 1
					ps.setString(j + 2, "2");//sys_data_source 2
					ps.setString(j + 3, importTemplateManage.getSysDataVersion());//sys_data_version 2
					ps.setString(j + 4, "0");//register_status 2
					ps.setString(j + 5, UUID.randomUUID().toString());//register_serno 2
					ps.setString(j + 6, importTemplateManage.getSysDataDate());// report_date
					ps.setString(j + 7, DateUtil.getNowDate());// imp_date
					if(rows_excel.size()>0){
						ps.setString(j + 8, DateUtil.getNowDate());// create_date
					}
					if ("app_asset_debt_register_info".equals(tableName)) {
						ps.setString(j + 9,theoryReportStartDate);
						sqlStr.append("?,");
					}
					ps.addBatch();
					log.info(ps.toString());
				}
				ps.executeBatch();
			} catch (Exception e) {
				log.error("报表导入异常!", e);
				throw new PromptException(e.getMessage());
			} finally {
				ps.close();
			}
		});

	}

	/**
	 * @methodName getTheoryReportStartDate
	 * @description 根据报送日期获取理论报送开始日期
	 * @param reportDate 报告日期
	 * @return java.lang.String
	 */
	private String getTheoryReportStartDate(String reportDate) throws Exception {
		String sql = "select workday from sys_workday_set where workday < $S{reportDate} order by workday desc limit 1";
		HashMap<String, Object> params = new HashMap<>();
		params.put("reportDate",reportDate);
		List<SqlRow> rows = comnDao.findRows(sql, params);
		if (CollectionUtils.isNotEmpty(rows)) {
			return rows.get(0).getString("workday");
		}
		return reportDate;
	}

	/**
	 *@param @param
	 *@return void
	 *@date 2023/8/16  16:02
	 *@description  纵表解析方法
	 *
	 */
	private void saveExcelZ(ImportTemplateManage importTemplateManage,List<Object[]> list) throws Exception {
		try {
			ImportTemplateManageField02 importTemplateManageField02 = new ImportTemplateManageField02();
			importTemplateManageField02.setSystemTableName(importTemplateManage.getSystemTableName());
			//1.查询纵表字段表
			List<ImportTemplateManageField02> fieldList = importTemplateManageField02Dao.findTemplateFieldListByModel(importTemplateManageField02);
			if (fieldList.isEmpty()) {
				throw new Exception("请配置纵表字段");
			}
			//2.sql拼接
			StringBuilder sqlStr =  new StringBuilder("INSERT INTO ");
			// 表名拼接
			sqlStr.append(fieldList.get(0).getTableName()).append(" ( report_date,row_id,column_id,data_value,sys_data_status,sys_data_version,register_status ,imp_date )  VALUES ( :reportDate,:rowId,:columnId,:dataValue,:sysDataStatus,:sysDataVersion,'3',:sysDataDate )");
			// 数据结构转换
			int rowStart = Integer.parseInt(importTemplateManage.getRowStart());
			int columnStart = Integer.parseInt(importTemplateManage.getColumnStart());
			List<ParamMap> listParamsMaps = new ArrayList<>();
			for(ImportTemplateManageField02 field :fieldList){
				//行坐标
				String [] rowInfo = parseValue(field.getValueRow());
				//列坐标
				String [] columnInfo = parseValue(field.getValueColumn());
				String columnType = field.getColumnType();
				String columnUnit = field.getColumnUnit();
				int rowOffset = 0;

				// 循环读取横纵坐标对应的值
				for(String row : rowInfo) {
					String excelRow = row; //表格中的行坐标
					if (row.contains("+")) {
						String[] strings = row.split("\\+");
						if (strings.length == 2) {
							excelRow = strings[0];
							row = String.valueOf(Integer.parseInt(excelRow) - rowOffset);
							rowOffset += Integer.parseInt(strings[1]);
						} else {
							throw new PromptException("表达式配置错误");
						}
					} else {
						row = String.valueOf(Integer.parseInt(excelRow) - rowOffset);
					}
					//定位行
					Object[] str = list.get(Integer.parseInt(excelRow)-1);
					int columnOffset = 0;
					for(String column : columnInfo) {
						String excelColumn = column;
						if (column.contains("+")) {
							String[] strings = column.split("\\+");
							if (strings.length == 2) {
								excelColumn = strings[0];
								column = String.valueOf(Integer.parseInt(excelColumn) - columnOffset);
								columnOffset += Integer.parseInt(strings[1]);
							} else {
								throw new PromptException("表达式配置错误");
							}
						} else {
							column = String.valueOf(Integer.parseInt(excelColumn) - columnOffset);
						}
						// 定位列
						Object objectValue = null;
						int columnSerial =Integer.parseInt(excelColumn);
						if(str.length>=columnSerial){
							objectValue = str[columnSerial-1];
						}
						ParamMap paramMap = new ParamMap();
						paramMap.put("reportDate",importTemplateManage.getSysDataDate());
						paramMap.put("rowId",Integer.parseInt(row) - rowStart + 1);
						paramMap.put("columnId",Integer.parseInt(column) - columnStart + 1);

						if(objectValue ==null ||StringUtils.isBlank((String)objectValue)){
							paramMap.put("dataValue", null);
						}else{
							objectValue = String.valueOf(objectValue);
							if(columnType.equals("03")){//类型为枚举的需要特殊处理
								paramMap.put("dataValue",objectValue.toString().trim().split(" ")[0]);
							} else if (columnType.equals("02") || columnType.equals("01")) {//类型为数字、字符串的需要特殊处理
								String dataValue = ((String) objectValue).trim();
								if (NumberUtils.isNumber(dataValue) && NumberUtils.isNumber(columnUnit)) {
									BigDecimal bigDataValue = new BigDecimal(dataValue);
									BigDecimal bigColumnUnit = new BigDecimal(columnUnit);
									BigDecimal bigDataValueUnit = bigDataValue.multiply(bigColumnUnit);
									paramMap.put("dataValue",bigDataValueUnit.toString());
								} else if (dataValue.matches("[+-]?\\d+(\\.\\d+)?[eE][+-]?\\d+")){//科学计数法特殊处理
									if (null == columnUnit) columnUnit = "1.00";
									BigDecimal bigColumnUnit = new BigDecimal(columnUnit);
									paramMap.put("dataValue", new BigDecimal(dataValue).multiply(bigColumnUnit).toPlainString());
								} else {
									paramMap.put("dataValue",dataValue);
								}
							} else if ("——".equals(((String) objectValue).trim())) {
								paramMap.put("dataValue", "--");
							} else {
								paramMap.put("dataValue",((String) objectValue).trim());
							}
						}

						paramMap.put("sysDataSource","2");
						paramMap.put("sysDataStatus",importTemplateManage.getSysDataStatus());
						paramMap.put("sysDataVersion",importTemplateManage.getSysDataVersion());
						paramMap.put("sysDataDate", DateUtil.getNowDate());
						listParamsMaps.add(paramMap);
					}
				}
			}
			dbopChange.updateBatchSqlChange(sqlStr.toString(), listParamsMaps.toArray(new ParamMap[0]));
		} catch (Exception ex) {
			log.error(ex.getMessage(),ex);
			throw new PromptException("解析文件失败");
		}
	}

	// 解析1,3-5为1+1,3,4,5
	private static String[] parseValue(String value) throws Exception {
		List<String> list =  new ArrayList<>();
		try {
			String[] array = value.split(",");
			for(int i=0; i<array.length; i++) {
				String element = array[i];
				if(element.indexOf("-") > 0) {
					String[] arr = element.split("-");
					if(arr.length == 2) {
						int start = Integer.parseInt(arr[0].trim());
						int end = Integer.parseInt(arr[1].trim());
						for(int j=start; j<=end; j++) {
							list.add(String.valueOf(j));
						}
					}
				} else {
					list.add(element);
				}
			}
		} catch (Exception e) {
			throw new PromptException("表达式配置错误");
		}
		return list.toArray(new String[list.size()]);
	}


	/**
	 *@param @param
	 *@return void
	 *@date 2023/8/16  16:02
	 *@description  纵表导出方法
	 *
	 */
	private  List<Object[]> reportExcelZ (List<Object[]> list,List<SqlRow> dataList) throws Exception {

			List<Object[]> resList   =  new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				Object[] str =  list.get(i);
				for(Map<String,Object> map : dataList){
					//行
					int row =  Integer.parseInt(map.get("row_id").toString());
					if(row-1 == i){
						//列
						int column =  Integer.parseInt(map.get("column_id").toString());
						Object value =  map.get("data_value").toString();
						//数字长度是否足够 不够的扩充
						if(str.length <column ){
							str = Arrays.copyOf(str, column);
						}
						//赋值
						str[column-1] =value;
					}
				}
				resList.add(str);
			}
		return resList;
	}



	/**
	 *@param @param
	 *@return void
	 *@date 2023/8/16  16:02
	 *@description  纵表导出方法
	 *
	 */
	private  List<Object[]> reportExcelH (ImportTemplateManage importTemplateManage,List<Object[]> list,List<SqlRow> dataList) throws Exception {


		ImportTemplateManageField01 importTemplateManageField01 = new ImportTemplateManageField01();
		importTemplateManageField01.setSystemTableName(importTemplateManage.getSystemTableName());

		List<Object[]> resList   =  new ArrayList<>();

		resList.add(list.get(0));

		//1.查询横表字段表
		try {
			List<ImportTemplateManageField01> fieldList = importTemplateManageField01Dao.findTemplateFieldListByModel(importTemplateManageField01);
			for (int i = 0; i < dataList.size(); i++) {
				Map<String,Object> map = dataList.get(i);
				Object[] str= new Object[resList.get(0).length];
				for ( ImportTemplateManageField01 field : fieldList  ){
					int column = Integer.parseInt(field.getTemplateColumnSerial());
					//扩充字段
					if(str.length <column ){
						str = Arrays.copyOf(str, column);
					}
					str[column-1] = map.get(field.getDatabaseColumnCode());
				}
				resList.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

		return resList;
	}

	/**
	 *@param @param
	 *@return void
	 *@date 2023/8/16  16:02
	 *@description  根据日期获取数据最新版本号
	 *
	 */
	private  String  getDateVersionByDate (ImportTemplateManage importTemplateManage) {
		String version = "1.0";
		StringBuilder sql =  new StringBuilder(" SELECT  max(sys_data_version) as  sys_data_version from   ");
		sql.append(importTemplateManage.getTableName()).append(" where report_date =  $S{sysDataDate}  and  sys_data_version >= '1.0' ");
		try {
			List<ImportTemplateManage> list =  importTemplateManageDao.getDateVersionByDate(sql.toString(),importTemplateManage);
			if(list.isEmpty()  || Tools.strIsEmpty(list.get(0).getSysDataVersion())){
				return version;
			}
			// 增加新的模板版本号
			BigDecimal existVersion =  new BigDecimal(list.get(0).getSysDataVersion());
			existVersion = existVersion.add(new BigDecimal("0.1"));
			return existVersion.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return version;
		}
	}


	/**
	 *@param @param
	 *@return void
	 *@date 2023/8/16  16:02
	 *@description  根据日期和版本号将旧数据置为失效
	 *
	 */
	private  void  updateOldDataByVersion (ImportTemplateManage importTemplateManage) {
		String version = importTemplateManage.getSysDataVersion();
		if(!"1.0".equals(version)){
			ImportTemplateManage saveData = new ImportTemplateManage();
			String oldVersion = new BigDecimal(version).subtract(new BigDecimal("0.1")).toString();
			saveData.setSysDataVersion(oldVersion);
			saveData.setSysDataDate(importTemplateManage.getSysDataDate());
			StringBuffer sql =  new StringBuffer(" update    ").append(importTemplateManage.getTableName());
			sql.append(" set sys_data_status = '0' where  sys_data_version = $S{sysDataVersion} and report_date =  $S{sysDataDate}");
			try {
				importTemplateManageDao.updDateStatusByDate(sql.toString(),saveData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private  void  deleteDataByVersion (ImportTemplateManage importTemplateManage) throws PromptException {
			ImportTemplateManage saveData = new ImportTemplateManage();
			saveData.setSysDataVersion("0");
			saveData.setSysDataDate(importTemplateManage.getSysDataDate());
			StringBuffer sql =  new StringBuffer("delete from  ").append(importTemplateManage.getTableName());
			sql.append(" where  sys_data_version = $S{sysDataVersion} and report_date =  $S{sysDataDate}");
			try {
				importTemplateManageDao.updDateStatusByDate(sql.toString(),saveData);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				throw new PromptException("删除数据报错");
			}
	}

	/**
	 * 将多个xlsx文件的zip压缩文件解析到list中
	 * @param zipFile
	 * @return
	 * @throws Exception
	 */
	public List<File> extractZipToFileList(File zipFile, String tempPath) throws Exception {
		List<File> fileList = new ArrayList<>();
		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile.getAbsolutePath()), Charset.forName(charsetType))){
			byte[] buffer = new byte[1024];
			ZipEntry entry ;
			while((entry = zis.getNextEntry()) != null){
				if(!entry.isDirectory() && (entry.getName().toLowerCase().endsWith(".xlsx") || entry.getName().toLowerCase().endsWith(".xls") || entry.getName().toLowerCase().endsWith(".dat") || entry.getName().toLowerCase().endsWith(".csv")  )) {
					if(entry.getName().codePoints().count() >200){
						throw new Exception("文件上传失败："+entry.getName()+"文件名超过200字符，无法解压文件，请检查修改后重试。");
					}
					File outFile = new File(tempPath, entry.getName());
					try (FileOutputStream fos = new FileOutputStream(outFile)) {
						int length ;
						while ((length = zis.read(buffer)) > 0) {
							fos.write(buffer, 0, length);
						}
					}
					fileList.add(outFile);
				}
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return fileList;
	}

	/**
	 * 将多个文件的zip压缩文件解析到list中
	 * @param  zipFile
	 * @param  tempPath
	 * @return List<File>
	 * @throws Exception
	 */
	public List<File> extractAllZipToFileList(File zipFile, String tempPath) throws Exception {
		if(tempPath.contains(".")){
			tempPath = tempPath.substring(0,tempPath.lastIndexOf("."))+ File.separator ;
		}
		List<File> fileList = new ArrayList<>();
		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile.getAbsolutePath()), Charset.forName(charsetType))){
			byte[] buffer = new byte[1024];
			ZipEntry entry ;
			while((entry = zis.getNextEntry()) != null){
				if(!entry.isDirectory()) {
					if(entry.getName().codePoints().count() >200){
						throw new Exception("文件上传失败："+entry.getName()+"文件名超过200字符，无法解压文件，请检查修改后重试。");
					}
					File outFile = new File(tempPath, entry.getName());
					File _parent = outFile.getParentFile() ;
					if( !_parent.exists() ){
						_parent.mkdirs() ;
					}
					try (FileOutputStream fos = new FileOutputStream(outFile)) {
						int length ;
						while ((length = zis.read(buffer)) > 0) {
							fos.write(buffer, 0, length);
						}
					}
					fileList.add(outFile);
				}
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return fileList;
	}

//	public String exportExcelData(ImportTemplateManage importTemplateInfo, MultipartFile file) throws Exception {
//
//
//		// 获取模板完整信息
//		List<ImportTemplateManage> tempDataList = importTemplateManageDao.findTemplateInfoByModel(importTemplateInfo);
//		ImportTemplateManage importTemplateManage = tempDataList.get(0);
//		importTemplateManage.setSysDataDate(importTemplateInfo.getSysDataDate());
//
//
//		if (file != null) {
//
//			File tempFile = File.createTempFile("prefix", ".suffix");
//			file.transferTo(tempFile);
//			//解析 excel
//			List<Object[]> list = ExcelUtils.readXSSFExcel(tempFile);
//
//			if ("01".equals(importTemplateManage.getImportType())) {
//				//横表入库
//				saveExcelH(importTemplateManage,list);
//			}else if ("02".equals(importTemplateManage.getImportType())) {
//				//纵表入库
//				saveExcelZ(importTemplateManage,list);
//			}
//
//			// 删除临时文件
//			tempFile.delete();
//
//		}else {
//			return RequestSupport.updateReturnJson(false, "上传文件为空", null).toString();
//		}
//		return  RequestSupport.updateReturnJson(true, "导入完成！", null).toString();
//	}

	/**
	 * 对文件做校验
	 * @param zipFileName
	 * @param file
	 * @param fileNo
	 * @param fileDesc
	 * @return
	 */
	public String checkFile(String zipFileName,File file,String fileNo,String fileDesc){
		StringBuffer returnStr = new StringBuffer();
		String parent = file.getParent();
		String fileName = file.getName();
		log.info("FileName:>>>"+file.getName());
		if(parent.endsWith(fileNo) && file.exists()){
			long sizeInBytes = file.length();
			if(sizeInBytes == 0){
				returnStr.append(zipFileName+"中上传的"+fileNo+"-"+fileDesc+" 为0字节，请检查后重新上传！<br/>");
			}else if (fileName.codePoints().count() > 200){
				returnStr.append(zipFileName+"中上传的"+fileNo+"-"+fileDesc+" 文件名称（包含扩展名）长度超过200！<br/>");
			}else{
				BigDecimal sizeMB = new BigDecimal(sizeInBytes / (1024 * 1024)).divide(BigDecimal.ONE,8,BigDecimal.ROUND_HALF_UP);
				if(sizeMB.compareTo(new BigDecimal(20)) >0){
					returnStr.append(zipFileName+"中上传的"+fileNo+"-"+fileDesc+" 大小超过20M，请检查后重新上传！<br/>");
				}
				if(!isValid(fileName)){
					returnStr.append(zipFileName+"中上传的"+fileNo+"-"+fileDesc+" 扩展名不在范围内，仅支持（doc,docx,xls,xlsx,zip,pdf,jpg,jpeg,bmp,png,gif,gd,ppt,pptx）！<br/>");
				}
			}
		}
		return returnStr.toString();
	}
	public static boolean isValid(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			return false;
		}

		int lastDotIndex = fileName.lastIndexOf('.');
		if (lastDotIndex <= 0 || lastDotIndex == fileName.length() - 1) {
			return false; // 无扩展名或扩展名为空
		}

		String extension = fileName.substring(lastDotIndex + 1).toLowerCase();
		boolean isValid = extension.matches("(doc|docx|xls|xlsx|zip|pdf|jpg|jpeg|bmp|png|gif|gd|ppt|pptx)");
		// 调试输出
//		System.out.println("文件名: " + fileName);
//		System.out.println("扩展名: " + extension);
//		System.out.println("是否合法: " + isValid);
		return isValid;
	}

	/**
	 * 删除文件夹及其所有内容
	 * @param folderPath 要删除的文件夹路径
	 * @return 是否删除成功
	 */
	public static boolean deleteFolder(String folderPath) {
		File folder = new File(folderPath);
		if (!folder.exists()) {
			System.out.println("文件夹不存在: " + folderPath);
			return false;
		}
		if (!folder.isDirectory()) {
			System.out.println("路径不是文件夹: " + folderPath);
			return false;
		}
		// 递归删除文件夹内容
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					deleteFolder(file.getAbsolutePath()); // 递归删除子文件夹
				} else {
					if (!file.delete()) {
						System.out.println("删除文件失败: " + file.getAbsolutePath());
						return false;
					}
				}
			}
		}
		// 删除空文件夹
		if (!folder.delete()) {
			System.out.println("删除文件夹失败: " + folderPath);
			return false;
		}
		System.out.println("删除成功: " + folderPath);
		return true;
	}

}
