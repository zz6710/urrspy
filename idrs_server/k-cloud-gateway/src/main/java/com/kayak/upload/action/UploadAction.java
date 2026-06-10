package com.kayak.upload.action;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.kayak.base.dao.ComnDao;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.constants.ExcelDownloadConstants;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.core.util.NetworkUtil;
import com.kayak.core.util.Tools;
import com.kayak.graphql.autoconfigure.GraphQLAnnotationImpl;
import com.kayak.graphql.service.GraphqlService;
import com.kayak.upload.util.FileTransferHelpler;
import com.kayak.upload.util.UploadFile;
import com.kayak.upload.util.UploadUtil;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.kayak.core.util.DateUtil.getLastDayOfYearMonth;

@RestController
public class UploadAction extends BaseController {

	@Autowired
	private GraphQLAnnotationImpl graphQLAnnotationImpl;

	@Autowired
	private GraphqlService graphqlService;

	@Autowired
	private ComnDao comnDao;

	@Value("${upload.path}")
	private String uploadPath;

	@Value("${excel.maxlen:1000000}")
	private int maxLen;

	@Value("${excel.tem.path:null}")
	private String temPath;

	@RequestMapping(value = "excel/download.json")
	public void serverDownload(HttpServletRequest httpRequest, HttpServletResponse response) {
		try {
			/*前端vue表格handleExport导出方法入口*/
			Map<String, Object> params = RequestSupport.getParameters();
			if (!RequestSupport.getCanCodeMore(params)) {
				log.error("检测到sql注入异常");
				return;
			}
			int type = Tools.obj2Int(params.get("type"));

			if (ObjectUtils.isEmpty(params.get("dataExcelTemplate"))) {
				String action = Tools.obj2Str(params.get("action"));

				String[] actions = action.split("[.]");

				String modelName = actions[0];
				action = actions[1] + ExcelDownloadConstants.SUFFIX;

				SqlRow serverModel = graphQLAnnotationImpl.modelMap.get(modelName);

				String appName = serverModel.getString("app_name");
				String modelFullName = serverModel.getString("model_full_name");

				params.put("action", action);
				params.put("modelClassName", modelFullName);
				graphqlService.requestDownload(appName, "/excel/download.json", params, response);
			} else if (ObjectUtils.isEmpty(params.get("dataExportType"))) {
				try {
					String headers = Tools.obj2Str(params.get("headers"));
					String action = Tools.obj2Str(params.get("action"));
					String[] actions = action.split("[.]");
					String modelName = actions[0];
					SqlRow serverModel = graphQLAnnotationImpl.modelMap.get(modelName);
					String appName = serverModel.getString("app_name");
					String modelFullName = serverModel.getString("model_full_name");
					String serverName = serverModel.getString("server_name");
					HashMap<String, Object> paramsMap = JSON.parseObject(Tools.obj2Str(params.get("action_params")),
							HashMap.class);
					params.putAll(paramsMap);
					params.put("action", actions[1]);
					String ip= NetworkUtil.getIpAddress(RequestSupport.getLocalRequest());
					Object body = graphqlService.commQuery(appName, serverName, modelFullName, params,ip);
					HashMap<String, Object> b = (HashMap<String, Object>) body;
					List<?> list = (List<?>) b.get("rows");

					// 解析请求头配置
					String[] headerConfigs = headers.split(",");

					// 头部数据
					List<List<String>> excelHeaders = new ArrayList<List<String>>();
					List<String> headKeys = new ArrayList<>();
					Map<String, Map<String, Object>> colMaps = new HashMap<>();
					for (String headerConfig : headerConfigs) {
						String[] _headerConfigs = headerConfig.split(":");
						List<String> head = new ArrayList<String>();
						Map<String, Object> keyMap = new HashMap<>();
						head.add(_headerConfigs[0]);
						excelHeaders.add(head);
						headKeys.add(_headerConfigs[1]);
						keyMap.put("key", _headerConfigs[1]);
						if (_headerConfigs.length == 3 && Tools.isNotBlank(_headerConfigs[2])) {
							keyMap.put("type", _headerConfigs[2]);
						}
						if (_headerConfigs.length == 4 && Tools.isNotBlank(_headerConfigs[3])) {
							keyMap.put("dict", _headerConfigs[3]);
						}
						colMaps.put(_headerConfigs[1], keyMap);
					}
					downFile(list, excelHeaders, headKeys, colMaps, response, params.get("unToDict").toString(), params);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else{
				try {
					HashMap<String, Object> paramsMap = JSON.parseObject(Tools.obj2Str(params.get("action_params")), HashMap.class);
					params.putAll(paramsMap);
					downFile1D2D(response,params); /*兼容1维，2维*/
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void downFile(List<?> datas, List<List<String>> excelHeaders, List<String> headKeys,
						  Map<String, Map<String, Object>> colMaps, HttpServletResponse response, String unToDict,
						  Map<String, Object> params) throws Exception {
		log.info("采用页面的表头制作excel并输出的方法。");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Expose-Headers", "filename");

		if (datas.size() > maxLen) {// 数据长度过长，需要拆分成多个Excel打包导出
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-disposition", "attachment;filename=download.zip");
			response.setHeader("filename", "download.zip");

			File dir = new File(temPath);
			if (!dir.exists()) {// 判断目录是否存在
				dir.mkdirs();
			}

			String temZip = temPath + "/" + UUID.randomUUID().toString() + ".zip";

			File temZipFile = new File(temZip);
			temZipFile.createNewFile();

			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(temZipFile)); // 创建ZipOutputStream类对象

			int start = 0;
			int end = maxLen;

			while (end <= datas.size()) {
				File temFile = makeExcel(datas, excelHeaders, headKeys, colMaps, start, end, unToDict, params);

				out.putNextEntry(new ZipEntry("download（" + start + "~" + end + "）.xlsx")); // 创建新的进入点
				// 创建FileInputStream对象
				FileInputStream in = new FileInputStream(temFile);
				int b; // 定义int型变量
				while ((b = in.read()) != -1) { // 如果没有到达流的尾部
					out.write(b); // 将字节写入当前ZIP条目
				}
				in.close(); // 关闭流

				temFile.delete();

				if (end == datas.size()) {
					break;
				}

				start = end;
				end += maxLen;
				if (end > datas.size()) {
					end = datas.size();
				}
			}

			out.close();
			OutputStream os = response.getOutputStream();
			try {
				os.write(FileUtils.readFileToByteArray(temZipFile));
				temZipFile.delete();
			}catch (Exception e){
				throw e;
			}finally {
				if(os != null ){
					os.flush();
//					os.close();
				}
			}
		} else {// 单个Excel文件导出
			exportExcelNew(response,datas, excelHeaders, headKeys, colMaps,0, datas.size(), unToDict, params);
		}
	}

	/**
	 * 下载一维，二维数据文件
	 * 已同时支撑一维，二维到导出excel  程晓鹏 2025.03.11 modify
	 * @param response
	 * @param params
	 * @throws Exception
	 */
	private void downFile1D2D(HttpServletResponse response, Map<String, Object> params) throws Exception {
		String fileType = ".xlsx";
		String templateFileName = String.valueOf(params.get("templateFileName"));

		if (StringUtils.isNotEmpty(templateFileName)) {
			if (templateFileName.toLowerCase().endsWith("xls")) {
				fileType = ".xls";
			} else if (templateFileName.toLowerCase().endsWith("xlsx")) {
				fileType = ".xlsx";
			}
		}

		String dataExcelTemplate = String.valueOf(params.get("dataExcelTemplate"));
		params.put("dataExcelTemplate", StringUtils.replace(dataExcelTemplate, ".xlsx", fileType));

		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Expose-Headers", "filename");
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=download"+fileType);
		response.setHeader("filename", "download"+fileType);
		OutputStream os = response.getOutputStream();
		File temFile = makeExcel1D2D(params, fileType);
		os.write(FileUtils.readFileToByteArray(temFile));
		temFile.delete();
	}

	/**
	 * 生成一维二维Excel数据文件
	 * 已同时支撑一维，二维到导出excel  程晓鹏 2025.03.11 modify
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private File makeExcel1D2D(Map<String, Object> params, String fileType) throws Exception {
		File dir = new File(temPath);
		if (!dir.exists()) {// 判断目录是否存在
			dir.mkdirs();
		}
		// 生成临时文件
		String temExcel = temPath + "/" + UUID.randomUUID().toString() + fileType;
		File temExcelFile = new File(temExcel);

		if (!temExcelFile.exists()) {
			temExcelFile.createNewFile();
		}
		String dataExportType="";
		if(params.get("dataExportType")!=null){
			dataExportType = params.get("dataExportType").toString();
		}
		FileOutputStream out = new FileOutputStream(temExcelFile);
		// 特殊增加五篇大文章导出逻辑
		if ("app_subank_five_article".equals(params.get("reportTable"))) {
			subankFiveArticle(out, params);
		} else if("3".equals(dataExportType)) {
			customWriteByPoi1D1D(out, params);
		} else {
			customWriteByPoi1D2D(out, params);
		}
		return temExcelFile;
	}

	private File makeExcel(List<?> datas, List<List<String>> excelHeaders, List<String> headKeys,
						   Map<String, Map<String, Object>> colMaps, int start, int end, String unToDict,
						   Map<String, Object> params) throws Exception {
		File dir = new File(temPath);
		if (!dir.exists()) {// 判断目录是否存在
			dir.mkdirs();
		}
		// 生成临时文件
		String temExcel = temPath + "/" + UUID.randomUUID().toString() + ".xlsx";
		File temExcelFile = new File(temExcel);

		if (!temExcelFile.exists()) {
			temExcelFile.createNewFile();
		}

		FileOutputStream out = new FileOutputStream(temExcelFile);
		try {
			// 设置文本格式
			WriteCellStyle textCellStyle = new WriteCellStyle();
			textCellStyle.setDataFormat((short) 49);
			HorizontalCellStyleStrategy styleStrategy = new HorizontalCellStyleStrategy(textCellStyle, textCellStyle);

			ExcelWriterSheetBuilder excelBuilder = EasyExcel.write(out)
					.registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).registerWriteHandler(styleStrategy).sheet("数据");

			// 添加头部信息
			excelBuilder.head(excelHeaders);

			// 写入数据
			List<List<String>> excelDatas = new ArrayList<List<String>>();

			for (int i = start; i < end; i++) {
				Object data = datas.get(i);

				List<String> _datas = new ArrayList<String>();

				String value = null;
				for (String headKey : headKeys) {
					if (data instanceof SqlRow) {// SqlRow对象
						SqlRow sqlRowData = (SqlRow) data;
						value = sqlRowData.getString(headKey);
					} else if (data instanceof LinkedHashMap) {
						value = Tools.obj2Str(((LinkedHashMap) data).get(headKey));
					} else {// model对象
						Object valueObj = getFieldValueByName(headKey, data);
						value = Tools.obj2Str(valueObj);
					}

					if (Tools.isNotEmpty(value)) {
						// 普通类型转换
						if (colMaps.get(headKey).containsKey("type")) {
							value = typeConvert(colMaps, value, headKey);
						}
						// 数字字典转换
						if (colMaps.get(headKey).containsKey("dict") && (unToDict.indexOf(headKey) == -1)) {
							value = dictConvert(colMaps, value, headKey);
						}
					}

					_datas.add(value);
				}
				excelDatas.add(_datas);
			}

			/**
			 * 是否是根据模板生成excel，如果是，用原生poi
			 */
			if (!ObjectUtils.isEmpty(params.get("dataExcelTemplate"))) {
				customWriteByPoi(out, params, excelDatas);
				return temExcelFile;
			}

			// 添加头部信息
			excelBuilder.head(excelHeaders);

			excelBuilder.doWrite(excelDatas);

		}catch (Exception e){
			throw e;
		}finally {
			if(out != null ){
				out.flush();
//				out.close();
			}
		}
		return temExcelFile;
	}

	private String dictConvert(Map<String, Map<String, Object>> colMaps, String value, String headKey) {
		String dict = (String) colMaps.get(headKey).get("dict");
		String key = value;
		value = CacheUtil.getDictItem(dict, value);
		if(Objects.nonNull(value) && !value.contains(" ")) {
			value = key + " " + value;
		}
		return value;
	}

	/**
	 * 根据字典代码和字典key值转换数据字典
	 * @param dict_code
	 * @param column_key
	 * @return
	 */
	private String valueDictConvert (String dict_code, String column_key) {
		String column_value = CacheUtil.getDictItem(dict_code, column_key);
		if(Objects.nonNull(column_value) && !column_value.contains(" ")) {
			column_value = column_key + " " + column_value;
		}
		return column_value;
	}

	private String typeConvert(Map<String, Map<String, Object>> colMaps, String value, String headKey) {
		String type = (String) colMaps.get(headKey).get("type");
		if (type.equals("date")) {
			if (value.split("").length == 6) {
				value = value.substring(0, 4) + "-" + value.substring(4, 6);
			}
			if (value.split("").length == 8) {
				value = value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(6, 8);
			}
		}
		if (type.equals("time")) {
			if (value.split("").length == 6) {
				value = value.substring(0, 2) + ":" + value.substring(2, 4) + ":" + value.substring(4, 6);
			}
		}
		return value;
	}

	private Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			return value;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 通用的上传文件接口
	 *
	 * @param request
	 * @param response
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/base/comn-upload.json")
	public String upload(HttpServletRequest request, HttpServletResponse response,
						 @RequestParam(value = "file") MultipartFile file) {
		response.setContentType("text/html;chartset=UTF-8");

		if (file != null) {
			Map<String, Object> params = RequestSupport.getParameters(true);
			if (!RequestSupport.getCanCodeMore(RequestSupport.getParameters())) {
				return "检测到sql注入异常";
			}
			// 自定义的上传根目录
			String uploadDir = (String) params.get("upload_dir");

			Map<String, Object> returnData = new HashMap<>();
			String fileName = file.getOriginalFilename().toLowerCase();
			if (fileName.endsWith(".sh") || fileName.endsWith(".class") || fileName.endsWith(".jsp")
					|| fileName.endsWith(".html")) {
				return updateFailure("禁止上传脚本");
			}
			try {
				UploadFile uploadFile = UploadUtil.uploadCode(file, uploadPath, uploadDir);
				returnData.put("upload_path", uploadFile.getFilePath().substring(uploadPath.length()));
				returnData.put("upload_name", fileName);
				returnData.put("upload_code", uploadFile.getFileCode());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return updateFailure("文件上传失败");
			}
			return updateSuccess("上传成功", returnData);
		}

		return updateFailure("文件上传失败");

	}

	/**
	 * 通用的下载文件接口
	 *
	 * @param request
	 * @param response
	 * @param path
	 * @throws IOException
	 */
	@RequestMapping(value = "/base/comn-download.json")
	public void download(HttpServletRequest request, HttpServletResponse response,
						 @RequestParam(value = "path") String path) throws IOException {
		if (path == null || path.length() == 0) {
			log.error("下载文件异常，缺少path参数.");
			return;
		}
		if (!RequestSupport.getCanCodeMore(RequestSupport.getParameters())) {
			log.error("检测到sql注入异常");
			return;
		}
		File file = new File(uploadPath + path);
		if (file.exists()) {
			FileInputStream input = new FileInputStream(file);
			OutputStream outputStream = response.getOutputStream();
			try {
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
				response.setContentLength((int) file.length());
				IOUtils.copy(input, outputStream);
			} finally {
				input.close();
				outputStream.close();
			}
		} else {
			throw new RuntimeException("file not exits.");
		}
	}

	@RequestMapping(value = "/base/common-download.json")
	public void commonDownload(HttpServletRequest request, HttpServletResponse response,
							   @RequestParam(value = "path") String path) throws IOException {
		if (path == null || path.length() == 0) {
			log.error("下载文件异常，缺少path参数.");
			return;
		}
		if (!RequestSupport.getCanCodeMore(RequestSupport.getParameters())) {
			log.error("检测到sql注入异常");
			return;
		}
		File file = new File(path);
		if (file.exists()) {
			try(BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
				OutputStream outputStream = response.getOutputStream()) {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/octet-stream;charset=utf-8");
				response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
				response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
				response.setContentLength((int) file.length());
				IOUtils.copy(input, outputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("file not exits.");
		}
	}

	/**
	 * 通用的下载文件接口
	 *
	 * @param response
	 * @param files
	 * @throws IOException
	 */
	@RequestMapping(value = "/base/comn-batch-download.json")
	public void batchDownload(HttpServletResponse response, @RequestParam(value = "paths") String files)
			throws IOException {
		if (files == null || files.length() == 0) {
			log.error("下载文件异常，缺少paths参数.");
			return;
		}
		if (!RequestSupport.getCanCodeMore(RequestSupport.getParameters())) {
			log.error("检测到sql注入异常");
			return;
		}
		String[] filePathArray = files.split(",");
		if (filePathArray.length == 0) {
			log.warn("下载文件异常,path:{}", files);
			return;
		}
		ServletOutputStream out = response.getOutputStream();
		ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(out));
		response.setContentType("application/zip");
		String downloadAttachmentFilename = "全部附件" + ".zip";
		try {
			downloadAttachmentFilename = URLEncoder.encode(downloadAttachmentFilename, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
		response.setHeader("Content-Disposition", "attachment; filename=" + downloadAttachmentFilename + "");

		byte[] buffer = new byte[1024];
		for (int i = 0; i < filePathArray.length; i++) {
			String path = filePathArray[i];
			String name = filePathArray[++i];
			if (Tools.isBlank(path) || Tools.isBlank(name)) {
				continue;
			}
			File file = new File(uploadPath + path);
			if (file.exists()) {
				zos.putNextEntry(new ZipEntry(name));
				FileInputStream fis = new FileInputStream(file);

				BufferedInputStream fif = new BufferedInputStream(fis);
				int len;
				// 读入需要下载的文件的内容，打包到zip文件
				while ((len = fif.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
				fif.close();
				zos.closeEntry();
			} else {
				zos.write(("文件未找到 " + file.getName()).getBytes());
				zos.closeEntry();
			}
		}
		zos.close();
	}

	@PostMapping(value = "/upload/server/{serverName}/**")
	public @ResponseBody
	Object serverUpload(HttpServletRequest httpRequest, HttpServletResponse response,
						@PathVariable String serverName, @RequestParam("file") MultipartFile file) {
		try {
			String currentURL = httpRequest.getRequestURI();
			String prefix = "server/" + serverName;
			String url = currentURL.substring(currentURL.indexOf(prefix) + prefix.length());

			return graphqlService.requestUpload(serverName, url, RequestSupport.getParameters(), file);

		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return updateFailure(e.getMessage());
		}
	}

	@PostMapping(value = "/uploadDownload/server/{serverName}/**")
	public @ResponseBody
	void serverUploadDownload(HttpServletRequest httpRequest, HttpServletResponse response,
						@PathVariable String serverName, @RequestParam("file") MultipartFile file) {
		try {
			String currentURL = httpRequest.getRequestURI();
			String prefix = "server/" + serverName;
			String url = currentURL.substring(currentURL.indexOf(prefix) + prefix.length());

			graphqlService.requestUploadDownLoad(serverName, url, RequestSupport.getParameters(), file, response);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}

	@PostMapping(value = "/upload-files/server/{serverName}/**")
	public @ResponseBody
	Object serverUploadFiles(HttpServletRequest httpRequest, HttpServletResponse response,
							 @PathVariable String serverName, @RequestParam("files") MultipartFile[] files) {
		try {
			String currentURL = httpRequest.getRequestURI();
			String prefix = "server/" + serverName;
			String url = currentURL.substring(currentURL.indexOf(prefix) + prefix.length());

			return graphqlService.requestUploadFiles(serverName, url, RequestSupport.getParameters(), files);

		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return updateFailure(e.getMessage());
		}
	}

	/**
	 * 数据流请求，用于大数据量时请求，因为当用普通的json数据，post请求出现异常，因此采用将json数据，以blob文件的方式传递
	 * 后端解析文件，提取出json数据，再往后端去处理 【程晓鹏 2025.01.07 add】
	 * @param httpRequest
	 * @param response
	 * @param serverName
	 * @param files
	 * @return
	 */
	@PostMapping(value = "/dataStream/server/{serverName}/**")
	public @ResponseBody
	Object serverDataStream(HttpServletRequest httpRequest, HttpServletResponse response,
							 @PathVariable String serverName, @RequestParam("files") MultipartFile[] files) {
		try {
			String currentURL = httpRequest.getRequestURI();
			String prefix = "server/" + serverName;
			String url = currentURL.substring(currentURL.indexOf(prefix) + prefix.length());

			return graphqlService.requestDataStream(serverName, url, RequestSupport.getParameters(), files);

		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return updateFailure(e.getMessage());
		}
	}

	@PostMapping(value = "/transmit/upload/{targetUrl}/**")
	public @ResponseBody Object transmitUpload(HttpServletRequest httpRequest, @PathVariable String targetUrl, @RequestParam("file") MultipartFile file) {
		try {
			String currentURL = httpRequest.getRequestURI();
			String prefix = "/transmit/upload/" + targetUrl;
			String url=targetUrl.substring(1)+currentURL.substring(currentURL.indexOf(prefix)+prefix.length());
			if(!url.startsWith("http")){
				url= "http://" + url;
			}
			return graphqlService.requestUploadTransmit(url,RequestSupport.getParameters(),file,RequestSupport.getHeaders(),null);

		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return updateFailure(e.getMessage());
		}
	}

	@PostMapping(value = "/transmit/uploadfiles/{targetUrl}/**")
	public  Object transmitUploadFiles(HttpServletRequest httpRequest, @PathVariable(name = "targetUrl",value = "targetUrl") String targetUrl, @RequestParam("files") MultipartFile[] files) {
		try {
			String currentURL = httpRequest.getRequestURI();
			String prefix = "/transmit/uploadfiles/" + targetUrl;
			String url=targetUrl.substring(1)+currentURL.substring(currentURL.indexOf(prefix)+prefix.length());
			if(!url.startsWith("http")){
				url= "http://" + url;
			}
			return graphqlService.requestUploadTransmitFiles(url,RequestSupport.getParameters(),files,RequestSupport.getHeaders(),null);

		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return updateFailure(e.getMessage());
		}
	}

	/**
	 * 自定义模板、行、列 写excel数据
	 *
	 * @param out
	 * @param params
	 * @param excelData
	 * @throws Exception
	 */
	private void customWriteByPoi(FileOutputStream out, Map<String, Object> params, List<List<String>> excelData) throws Exception {
		Instant startTime = Instant.now();
		try {
			// 初始行数和列数
			int rowNum = 1;
			int cellNum = 0;
			if (!ObjectUtils.isEmpty(params.get("dataExcelStartLine"))) {
				rowNum = Integer.parseInt(String.valueOf(params.get("dataExcelStartLine")));
			}
			if (!ObjectUtils.isEmpty(params.get("dataExcelStartCol"))) {
				cellNum = Integer.parseInt(String.valueOf(params.get("dataExcelStartCol")));
			}
			// 模板
			String template = String.valueOf(params.get("dataExcelTemplate"));
			String sql = "SELECT id, system_table_name, template_name, template_file_name, template_file_path, import_type, version, template_status, imp_usr, imp_date, imp_time, oss_file_path\n" +
					"FROM import_template_manage  where  1=1 and template_name ='"+params.get("dataTemplateName")+"'  order by imp_date desc limit 1";
			List<SqlRow> rows = comnDao.findRows(sql);

			String sql_excel = "SELECT row_num,cell_num,cell_key from base_excel_special_config where  1=1 and template_name ='"+params.get("dataTemplateName")+"' ";
			List<SqlRow> rows_excel = comnDao.findRows(sql_excel);
			String specialRowNum [] = new String[0];
			String specialCellNum [] = new String[0];
			String specialCellKey [] = new String[0];
			if (rows_excel != null && rows_excel.size() > 0) {
				specialRowNum = rows_excel.get(0).getString("row_num").split(",");
				specialCellNum = rows_excel.get(0).getString("cell_num").split(",");
				specialCellKey = rows_excel.get(0).getString("cell_key").split(",");
			}
			String pathFile ="";
			String remoteFile ="";
			if (rows != null && rows.size() > 0) {
				pathFile = rows.get(0).getString("template_file_path")+ rows.get(0).getString("template_file_name");
				remoteFile = rows.get(0).getString("oss_file_path");
			}
			File file = new File(pathFile);
			if (!file.exists() && Tools.isNotEmpty(remoteFile)) {
				if(!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				FileTransfer transfer = FileTransferHelpler.getTransfer();
				transfer.downloadFileAndDisconnect(remoteFile, pathFile);
			}
			InputStream inputStream = new FileInputStream(pathFile);
			Workbook workbook;
			if (template.endsWith(".xls")) {
				workbook = new HSSFWorkbook(inputStream);
			} else {
				workbook = new XSSFWorkbook(inputStream);
			}

			Sheet sheet0 = workbook.getSheetAt(0);
			CellStyle style = workbook.createCellStyle();
			style.setDataFormat((short) 49);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderRight(BorderStyle.THIN);
			/*处理表头特殊值*/
			if(specialRowNum.length>0){
				for (String s : specialRowNum) {
					Row sp_row = sheet0.getRow(Integer.parseInt(s));
					for (String value : specialCellNum) {
						for (String item : specialCellKey) {
							// TODO: 2024/9/26 根据cell_key 处理特殊字段的格式
							if(params.get(item)!=null)
								sp_row.getCell(Integer.parseInt(value)).setCellValue(getLastDayOfYearMonth(params.get(item).toString()));
						}
					}
				}
			}
			/*获取表头*/
			SXSSFWorkbook workbookSx = new SXSSFWorkbook((XSSFWorkbook) workbook);
			SXSSFSheet sheet0Sx = workbookSx.getSheetAt(0);
			int tempCellNum = cellNum;
			for (List<String> excelDatum : excelData) {

				Row row = sheet0Sx.getRow(rowNum);
				if (ObjectUtils.isEmpty(row)) {
					row = sheet0Sx.createRow(rowNum);
				}
				for (String s : excelDatum) {
					if (!ObjectUtils.isEmpty(row.getCell(tempCellNum))) {
						row.getCell(tempCellNum).setCellValue(s);
					} else {
						Cell cell = row.createCell(tempCellNum);
						cell.setCellValue(s);
						cell.setCellStyle(style);
					}
					tempCellNum++;
				}

				tempCellNum = cellNum;
				rowNum++;
			}
			// 获取当前系统时间点
			Instant endTime = Instant.now();
			// 计算时间间隔
			Duration duration = Duration.between(startTime, endTime);
			long seconds = duration.getSeconds();
			log.info("本次生成excel用时{}秒",seconds);
			workbookSx.write(out);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 自定义模板、行、列 写excel数据
	 * 已同时支撑一维，二维到导出excel  程晓鹏 2025.03.11 modify
	 * @param out
	 * @param params
	 * @throws Exception
	 */
	private void customWriteByPoi1D2D(FileOutputStream out, Map<String, Object> params) throws Exception {
		Instant startTime = Instant.now();
		try {
			// 初始行数和列数,sheet
			int rowNum = 1;
			int cellNum = 0;
			int sheetNum = 0;
			// 模板 需要环境有配置UPLOADACTION001 对应SQL
			List<SqlRow> rows = comnDao.findRows(ExeQuery.queryExeId("UPLOADACTION001"), params);
//			String template = String.valueOf(params.get("dataExcelTemplate"));
//			String sql = "SELECT t.row_start,t.column_start,t.skip_column,t1.system_table_name, t.template_name,t1.export_table_id,t1.id,t1.start_sheet,template_file_name, template_file_path, import_type, version, template_status, imp_usr, imp_date, imp_time, oss_file_path\n" +
//					"FROM import_template_manage  t  inner join app_table_info t1 on t.system_table_name = t1.id where template_status = '1' and template_name ='"+params.get("dataTemplateName")+"' order by imp_date desc limit 1";
//			List<SqlRow> rows = comnDao.findRows(sql);
			String pathFile ="";
			String remoteFile ="";
			String[] specialRowNum = new String[0];
			String[] specialCellNum = new String[0];
			String[] specialCellKey = new String[0];
			String[] exportTableId = new String[0];
			String[] skipCellNum = new String[0];
			if (rows != null && rows.size() > 0) {
				pathFile = rows.get(0).getString("template_file_path")+ rows.get(0).getString("template_file_name");
				remoteFile = rows.get(0).getString("oss_file_path");
				sheetNum = Integer.parseInt(rows.get(0).getString("start_sheet"));
				//根据export_table_id 判断是否有多sheet合并导出
				if(!"".equals(rows.get(0).getString("export_table_id"))){
					exportTableId = rows.get(0).getString("export_table_id").split(",");
				}else{
					exportTableId = rows.get(0).getString("id").split(",");
				}
			}
			File file = new File(pathFile);
			if (!file.exists() && Tools.isNotEmpty(remoteFile)) {
				if(!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				FileTransfer transfer = FileTransferHelpler.getTransfer();
				transfer.downloadFileAndDisconnect(remoteFile, pathFile);
			}
			InputStream inputStream = new FileInputStream(pathFile);
			Workbook workbook = WorkbookFactory.create(inputStream);
			for (String tableId : exportTableId) {
//				params.put("tableId", tableId);
//				List<SqlRow> rows_table = comnDao.findRows(ExeQuery.queryExeId("UPLOADACTION002"), params);
				String sql_table = "SELECT t.row_start,t.column_start,t.skip_column,t1.system_table_name, t.template_name,t1.export_table_id,t1.id, template_file_name, template_file_path, import_type, version, template_status, imp_usr, imp_date, imp_time, oss_file_path\n" +
						"FROM import_template_manage  t  inner join app_table_info t1 on t.system_table_name = t1.id where 1=1 and t1.id ='"+tableId+"'  order by imp_date desc limit 1";
				if(params.get("dataTemplateName").toString().contains("G06a")){
					 sql_table = "SELECT t.row_start,t.column_start,t.skip_column,t1.system_table_name, t.template_name,t1.export_table_id,t1.id, template_file_name, template_file_path, import_type, version, template_status, imp_usr, imp_date, imp_time, oss_file_path\n" +
							"FROM import_template_manage  t  inner join app_table_info t1 on t.system_table_name = t1.id where t.template_name ='"+params.get("dataTemplateName")+"' and t1.id ='"+tableId+"'  order by imp_date desc limit 1";
				}
				List<SqlRow> rows_table = comnDao.findRows(sql_table);
				String importType = ""; //导入类型 01 横表 02 纵表
				if (rows_table != null && rows_table.size() > 0) {
					params.put("dataTemplateName",rows_table.get(0).getString("template_name"));
					params.put("reportTable",rows_table.get(0).getString("system_table_name"));
					importType = rows.get(0).getString("import_type");
					skipCellNum = rows_table.get(0).getString("skip_column").split(",");
					rowNum = Integer.parseInt(rows_table.get(0).getString("row_start"));
					cellNum = Integer.parseInt(rows_table.get(0).getString("column_start"));
					params.put("row_start",rowNum); //设置开始行
					params.put("column_start",cellNum); //设置开始列
				}
				String report_table = StringUtils.lowerCase(String.valueOf(params.get("reportTable")));

				String sql_excel = "SELECT row_num,cell_num,cell_key from base_excel_special_config where  1=1 and template_name ='"+params.get("dataTemplateName")+"' ";
				List<SqlRow> rows_excel = comnDao.findRows(sql_excel);
				if (rows_excel != null && rows_excel.size() > 0) {
					specialRowNum = rows_excel.get(0).getString("row_num").split(",");
					specialCellNum = rows_excel.get(0).getString("cell_num").split(",");
					specialCellKey = rows_excel.get(0).getString("cell_key").split(",");
				} else {
					specialRowNum = new String[0];
					specialCellNum = new String[0];
					specialCellKey = new String[0];
				}
				Sheet sheet0 = workbook.getSheetAt(sheetNum);
				CellStyle style = workbook.createCellStyle();
				style.setLocked(false);
				style.setDataFormat((short) 49);
				//追加导入类型的判断，支撑01 横表,02 纵表的导出  程晓鹏 2025.03.11 modify
				if("02".equals(importType)){ //当为02 纵表时
					int rowNum02 = rowNum - 1;
					int cellNum02 = cellNum - 1;

					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					Date date = sdf.parse(params.get("reportDate").toString());

					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					String date1 = "报表日期：" + calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月";
					String date2 = DateUtil.dateFormate(date, "yyyyMMdd");

					/*处理表头特殊值*/
					if(specialRowNum.length>0){
						for (String s : specialRowNum) {
							Row sp_row = sheet0.getRow(Integer.parseInt(s));
							for (String value : specialCellNum) {
								for (String item : specialCellKey) {
									if(params.get(item)!=null) {
										if (report_table.contains("app_pbc_report_")) {
											sp_row.getCell(Integer.parseInt(value)).setCellValue(date);
										} else if (report_table.contains("app_rpt_g06_") || "app_zy_s51_01".equals(report_table)) {
											sp_row.getCell(Integer.parseInt(value)).setCellValue(date1);
										} else if ("app_zz_transform_01".equals(report_table) || "app_zy_sts_prd_cdt".equals(report_table)) {
											sp_row.getCell(Integer.parseInt(value)).setCellValue(date2);
										} else if ("app_prod_month_count".equals(report_table) || "app_zy_sts_asset_cdt".equals(report_table)) {
											// 不处理
										} else {
											sp_row.getCell(Integer.parseInt(value)).setCellValue(date);
										}
									}
								}
							}
						}
					}
					//处理数据单元格
					String reportTable = params.get("reportTable").toString();
					String sql_excel_data = "select row_id, column_id, data_value from "+reportTable+" where  1=1 and report_date ='"+params.get("reportDate")+"' and sys_data_status ='1' ";
					if ("app_prod_month_count".equals(reportTable)) {
						// 投资资产情况表
						sql_excel_data = ExeQuery.queryExeId("ASSETEQ001");
					}
					if ("app_zy_sts_asset_cdt".equals(reportTable)) {
						// 非保本理财资产负债表
						sql_excel_data = ExeQuery.queryExeId("ASSETEQ002");
					}
					if ("app_zy_s51_01".equals(reportTable)) {
						// S51 资管机构跨境投资资产统计表
						sql_excel_data = ExeQuery.queryExeId("ASSETEQ003");
					}
					List<SqlRow> rows_data = comnDao.findRows(sql_excel_data, params);
					if(rows_data.size()==0){
						sheetNum++;
						continue;
					}
					int maxColumns = sheet0.getRow(rowNum02).getLastCellNum();
					int maxRows = sheet0.getLastRowNum();
					int row_id = 0;
					String data_value="";
					if ("app_rpt_g06_01".equals(report_table)) {
						maxRows = 78;
					} else if ("app_zy_sts_prd_cdt".equals(report_table)) {
						maxRows = 83;
					}
					for(int i=rowNum02; i < maxRows; i++){
						int skip_time =0;
						int column_id =0;
						row_id++;
						Row data_row = sheet0.getRow(i);
						for(int j =cellNum02; j<maxColumns; j++){
							boolean skip_flag = false;
							column_id++;
							if(skipCellNum.length>0){
								for (String s : skipCellNum) {
									if (s.equals(String.valueOf(j))) {
										skip_flag = true;
										skip_time++;
										break;
									}
								}
							}
							if (skip_flag){
								continue;
							}
							data_value = getDateValue(rows_data, row_id, column_id-skip_time);
							if(!"".equals(data_value) && data_row != null && data_row.getCell(j) != null){
								if (report_table.contains("app_pbc_report_")) {
									if (new BigDecimal(String.valueOf(data_value)).compareTo(new BigDecimal(0)) != 0) {
										data_row.getCell(j).setCellValue(Double.parseDouble(data_value));
									}
								} else if ("app_zz_transform_01".equals(reportTable) || "app_prod_month_count".equals(reportTable) ||
										"app_zy_sts_prd_cdt".equals(reportTable) || "app_zy_sts_asset_cdt".equals(reportTable)) {
									data_row.getCell(j).setCellValue(data_value);
								} else {
									data_row.getCell(j).setCellValue(Double.parseDouble(data_value));
								}
							}
						}
					}
				}else if("01".equals(importType)){ //01 横表的处理逻辑【1维报表 程晓鹏 2025.03.11 modify】
					String reportDate = params.get("reportDate").toString(); //报表日期
					/*处理表头特殊值*/
					//原本这段代码可以复用，原因为2维的设置单元格进行了报送日期的特殊处理，为不影响原有逻辑重新加上，这样就不会对二维（02 纵表）有影响, 程晓鹏 2025.03.11 modify
					if(specialRowNum.length>0){
						for (String s : specialRowNum) {
							Row sp_row = sheet0.getRow(Integer.parseInt(s));
							for (String value : specialCellNum) {
								for (String item : specialCellKey) {
									if(params.get(item)!=null)
										sp_row.getCell(Integer.parseInt(value)).setCellValue(reportDate);
								}
							}
						}
					}
					String strSql = "select database_column_code,column_dict from import_template_manage_field_01 where system_table_name = '"+tableId+"' order by cast(template_column_serial as unsigned)";
					List<SqlRow> row_1columns = comnDao.findRows(strSql); //查询一维表配置的列字段
					List<String> columns1 = new ArrayList<>(); //一维的列名
					List<String> columnDict = new ArrayList<>(); //一维的列名
					if (row_1columns != null && row_1columns.size() > 0) {
						StringBuilder strQueryTableSql = new StringBuilder();
						strQueryTableSql.append("select ");
						for(int i=0; i<row_1columns.size(); i++){
							String columnName =  row_1columns.get(i).getString("database_column_code");
							String dict =  row_1columns.get(i).getString("column_dict");
							strQueryTableSql.append(columnName);
							columns1.add(columnName); //插入列
							columnDict.add(dict); //插入字典序列
							if(i+1 != row_1columns.size()){
								strQueryTableSql.append(", ");
							}
						}
						strQueryTableSql.append(" from ");
						// 特殊判断资产持仓登记
						String tableName = params.get("reportTable").toString().toLowerCase();
						if ("app_asset_regist_info".equals(tableName)) {
							String lastCycleDay = DateUtil.getLastCycleDay(reportDate,0, 1);
							if (StringUtils.isNotEmpty(reportDate) && reportDate.equals(lastCycleDay)) {
								strQueryTableSql.append(tableName);
							} else {
								strQueryTableSql.append(tableName + "_day");
							}
						} else {
							strQueryTableSql.append(tableName);
						}
						strQueryTableSql.append(" where report_date ='"+params.get("reportDate")+"' and sys_data_status = '1'");
						log.info("查询一维报表字段sql为：" + strQueryTableSql.toString());
						List<SqlRow> list1Data = comnDao.findRows(strQueryTableSql.toString());
						if(list1Data != null && list1Data.size() > 0){
							int startRow = Integer.valueOf(params.get("row_start").toString()).intValue();
							int startColumn = Integer.valueOf(params.get("column_start").toString()).intValue();
							for(SqlRow row: list1Data){ //循环将数据写入到excel文件中
								Row excelrow = sheet0.createRow(startRow++); //创建行
								int dataColumn = startColumn; //数据列索引，
								for(int n=0; n<columns1.size(); n++){
									Object obj = row.get(columns1.get(n));
									String cellValue = "";
									if(obj != null){
										cellValue = obj.toString();
									}
									// 数字字典转换
									if (StringUtils.isNotBlank(columnDict.get(n))) {
										cellValue = valueDictConvert(columnDict.get(n), cellValue);
									}

									Cell cell = excelrow.createCell(dataColumn++); //创建单元格
									cell.setCellValue(cellValue);  //设置1维表格的单元格数据
									cell.setCellStyle(style); //设置1维表格的单元格格式
								}
							}
						}
					}
				}else{
					;
				}
				sheetNum++;
			}
			// 获取当前系统时间点
			Instant endTime = Instant.now();
			// 计算时间间隔
			Duration duration = Duration.between(startTime, endTime);
			long seconds = duration.getSeconds();
			log.info("本次生成excel用时{}秒",seconds);
			workbook.write(out);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 五篇大文章导出 写excel数据
	 *
	 * @param out
	 * @param params
	 * @throws Exception
	 */
	private void subankFiveArticle(FileOutputStream out, Map<String, Object> params) throws Exception {
        String reportDate = String.valueOf(params.get("reportDate"));
		String filePath = String.valueOf(params.get("templateFilePath"));
		String fileName = String.valueOf(params.get("templateFileName"));
		String remoteFile = String.valueOf(params.get("ossFilePath"));
		String pathFile = filePath + fileName;

		File file = new File(pathFile);
		if (!file.exists() && Tools.isNotEmpty(remoteFile)) {
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			FileTransfer transfer = FileTransferHelpler.getTransfer();
			transfer.downloadFileAndDisconnect(remoteFile, pathFile);
		}

		try (InputStream inputStream = new FileInputStream(pathFile);
			 Workbook workbook = WorkbookFactory.create(inputStream)) {
			Font font = workbook.createFont();
			font.setFontName("等线");
			font.setFontHeightInPoints((short) 11);

			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFont(font);
			cellStyle.setBorderTop(BorderStyle.THIN);
			cellStyle.setBorderBottom(BorderStyle.THIN);
			cellStyle.setBorderLeft(BorderStyle.THIN);
			cellStyle.setBorderRight(BorderStyle.THIN);

            //  第一部分：封面、数字金融-数字化转型两张表【统计日期】填充
			CellStyle dateStyle = workbook.createCellStyle();
			dateStyle.setFont(font);
			dateStyle.setBorderTop(BorderStyle.THIN);
			dateStyle.setBorderBottom(BorderStyle.THIN);
			dateStyle.setBorderLeft(BorderStyle.THIN);
			dateStyle.setBorderRight(BorderStyle.THIN);
			dateStyle.setAlignment(HorizontalAlignment.CENTER);
			dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            int[] sheets = new int[]{0, 21};

            for (int index : sheets) {
                Sheet sheet = workbook.getSheetAt(index);
                SqlRow fiveArticleInfo = ExeQuery.querySqlRowId("FIVEARTICLE"+index);
                int cellnum = fiveArticleInfo.getInteger("sqlid");
                for (int i = 0; i < 10; i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;
                    // 日期列
                    Cell dateCell = row.getCell(cellnum + 1);
                    String dateValue = StringUtils.trim(row.getCell(cellnum).getStringCellValue());
                    if ("统计日期".equals(dateValue) || "统计日期：".equals(dateValue)) {
                        dateCell.setCellStyle(dateStyle);
                        dateCell.setCellValue(reportDate);
                    }
                }
            }

			// 第二部分：10张产品表
			CellStyle cellStyle1 = workbook.createCellStyle();
			cellStyle1.setFont(font);
			DataFormat format = workbook.createDataFormat();
			cellStyle1.setDataFormat(format.getFormat("0.00"));
			cellStyle1.setBorderTop(BorderStyle.THIN);
			cellStyle1.setBorderBottom(BorderStyle.THIN);
			cellStyle1.setBorderLeft(BorderStyle.THIN);
			cellStyle1.setBorderRight(BorderStyle.THIN);

			CellStyle cellStyle2 = workbook.createCellStyle();
			cellStyle2.setFont(font);
			cellStyle2.setDataFormat(format.getFormat("0.00000"));
			cellStyle2.setBorderTop(BorderStyle.THIN);
			cellStyle2.setBorderBottom(BorderStyle.THIN);
			cellStyle2.setBorderLeft(BorderStyle.THIN);
			cellStyle2.setBorderRight(BorderStyle.THIN);

			int[] prodSheets = new int[]{1, 4, 5, 8, 9, 12, 13, 16, 17, 20};

			for (int index : prodSheets) {
				int rowNum = 2; // 第3行，索引从0开始
				Sheet sheet = workbook.getSheetAt(index);
                SqlRow fiveArticleInfo = ExeQuery.querySqlRowId("FIVEARTICLE"+index);
				List<SqlRow> sqlRowList = comnDao.findRows(fiveArticleInfo.getString("sqlstr"), params);
				List<SqlRow> columnList = comnDao.findRows("SELECT data_type, numeric_scale FROM information_schema.columns " +
                        "WHERE table_schema = DATABASE() AND table_name = '"+fiveArticleInfo.getString("sqlid")+"' AND LOWER(column_name) <> 'id' ORDER BY ordinal_position", params);
				for (SqlRow sqlRow : sqlRowList) {
					Row row = sheet.getRow(rowNum);
					if (row == null) row = sheet.createRow(rowNum);
					for (int i = 0;i < sqlRow.size();i++) {
						Cell cell = row.createCell(i); // 第一列，索引从0开始
						if ("decimal".equals(columnList.get(i).getString("DATA_TYPE"))) {
                            if ("2".equals(columnList.get(i).getString("NUMERIC_SCALE"))) {
                                cell.setCellStyle(cellStyle1);
                            } else if ("6".equals(columnList.get(i).getString("NUMERIC_SCALE"))) {
                                cell.setCellStyle(cellStyle2);
                            }
							if (!ObjectUtils.isEmpty(sqlRow.get("c" + i))) {
								cell.setCellValue(sqlRow.getDouble("c" + i));
							}
						} else {
							cell.setCellStyle(cellStyle);
							cell.setCellValue(sqlRow.getString("c" + i));
						}
					}
					rowNum++;
				}
			}

			// 第三部分：10张资产表
            CellStyle cellStyle3 = workbook.createCellStyle();
            cellStyle3.setFont(font);
            cellStyle3.setDataFormat(format.getFormat("0.00000"));
            cellStyle3.setBorderTop(BorderStyle.THIN);
            cellStyle3.setBorderBottom(BorderStyle.THIN);
            cellStyle3.setBorderLeft(BorderStyle.THIN);
            cellStyle3.setBorderRight(BorderStyle.THIN);

            CellStyle cellStyle4 = workbook.createCellStyle();
            cellStyle4.setFont(font);
            cellStyle4.setDataFormat(format.getFormat("0.000000000"));
            cellStyle4.setBorderTop(BorderStyle.THIN);
            cellStyle4.setBorderBottom(BorderStyle.THIN);
            cellStyle4.setBorderLeft(BorderStyle.THIN);
            cellStyle4.setBorderRight(BorderStyle.THIN);

			int[] assetSheets = new int[]{2, 3, 6, 7, 10, 11, 14, 15, 18, 19};

            for (int index : assetSheets) {
                Sheet sheet = workbook.getSheetAt(index);
                SqlRow fiveArticleInfo = ExeQuery.querySqlRowId("FIVEARTICLE"+index);
                List<SqlRow> sqlRowList = comnDao.findRows(fiveArticleInfo.getString("sqlstr"), params);
                String cellnum = fiveArticleInfo.getString("sqlid");
				String[] cellnums = cellnum.split(",");
                Map<String, Double> amounts = new HashMap<>();
                Map<String, Double> zy_amounts = new HashMap<>();
                for (SqlRow sqlRow : sqlRowList) {
                    if (!ObjectUtils.isEmpty(sqlRow.get("amount"))) {
                        amounts.put(sqlRow.getString("src_typ"), sqlRow.getDouble("amount"));
                    }
					if (!ObjectUtils.isEmpty(sqlRow.get("zy_amount"))) {
						zy_amounts.put(sqlRow.getString("src_typ"), sqlRow.getDouble("zy_amount"));
					}
                }
                for (int i = 0; i < 100; i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;
                    // 日期列
					int dateInx = Integer.parseInt(cellnums[0]);
                    Cell dateCell = row.getCell(dateInx);
                    String dateValue = StringUtils.trim(row.getCell(dateInx - 1).getStringCellValue());
                    if ("统计日期：".equals(dateValue)) {
                        dateCell.setCellStyle(dateStyle);
                        dateCell.setCellValue(reportDate);
                    }
                    // 产品资金投资
					int cpzjInx = Integer.parseInt(cellnums[1]);
                    Cell cell = row.getCell(cpzjInx);
                    String cellValue = StringUtils.trim(row.getCell(cpzjInx - 1).getStringCellValue());
					boolean skipColumn = "1.1.12 其他债券".equals(cellValue) || "2.资产期限结构余额".equals(cellValue) || "3.资产违约情况".equals(cellValue) || "4.募资主体注册地余额情况".equals(cellValue) || "5.项目所属地余额情况".equals(cellValue);
                    if ("3.2资产违约率".equals(cellValue)) {
                        cell.setCellStyle(cellStyle3);
                    } else if (skipColumn) {
						// 不设置样式
					} else {
                        cell.setCellStyle(cellStyle4);
                    }
                    if (!ObjectUtils.isEmpty(amounts.get(cellValue)) && !skipColumn) {
                        cell.setCellValue(amounts.get(cellValue));
                    }
					// 1.7 合计
					String cellValue1 = StringUtils.trim(row.getCell((cpzjInx - 2) < 0 ? 0 : cpzjInx - 2).getStringCellValue());
					if ("1.7 合计".equals(cellValue1) && !ObjectUtils.isEmpty(amounts.get(cellValue1))) {
						cell.setCellStyle(cellStyle4);
						cell.setCellValue(amounts.get(cellValue1));
					}

					// 自有资金投资
					int zyzjInx = Integer.parseInt(cellnums[3]);
					Cell cell3 = row.getCell(zyzjInx);
					if ("3.2资产违约率".equals(cellValue)) {
						cell3.setCellStyle(cellStyle3);
					} else if (skipColumn) {
						// 不设置样式
					} else {
						cell3.setCellStyle(cellStyle4);
					}
					if (!ObjectUtils.isEmpty(amounts.get(cellValue)) && !skipColumn) {
						cell3.setCellValue(zy_amounts.get(cellValue));
					}
					// 1.7 合计
					if ("1.7 合计".equals(cellValue1) && !ObjectUtils.isEmpty(zy_amounts.get(cellValue1))) {
						cell3.setCellStyle(cellStyle4);
						cell3.setCellValue(zy_amounts.get(cellValue1));
					}
                }
            }

			workbook.write(out);
		} catch (Exception e) {
            throw e;
        } finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 自定义模板、行、列 写excel数据
	 * 已同时支撑一维到导出excel
	 * @param out
	 * @param params
	 * @throws Exception
	 */
	private void customWriteByPoi1D1D(FileOutputStream out, Map<String, Object> params) throws Exception {
		Instant startTime = Instant.now();
		try {
			// 初始行数和列数,sheet
			int rowNum = 1;
			int cellNum = 0;
			int sheetNum = 0;

			String sql = "SELECT t.row_start,t.column_start,t.skip_column,t1.system_table_name, t.template_name,t1.export_table_id,t1.id,t1.start_sheet,template_file_name, template_file_path, import_type, version, template_status, imp_usr, imp_date, imp_time, oss_file_path\n" +
					"FROM import_template_manage  t  inner join app_table_info t1 on t.system_table_name = t1.id where template_status = '1' and template_name ='"+params.get("dataTemplateName")+"' order by imp_date desc limit 1";
			List<SqlRow> rows = comnDao.findRows(sql);
			String pathFile ="";
			String remoteFile ="";
			String[] exportTableId = new String[0];
			String[] skipCellNum = new String[0];
			if (rows != null && rows.size() > 0) {
				pathFile = rows.get(0).getString("template_file_path")+ rows.get(0).getString("template_file_name");
				remoteFile = rows.get(0).getString("oss_file_path");
				sheetNum = Integer.parseInt(rows.get(0).getString("start_sheet"));
				//根据export_table_id 判断是否有多sheet合并导出
				if(!"".equals(rows.get(0).getString("export_table_id"))){
					exportTableId = rows.get(0).getString("export_table_id").split(",");
				}else{
					exportTableId = rows.get(0).getString("id").split(",");
				}
			}
			File file = new File(pathFile);
			if (!file.exists() && Tools.isNotEmpty(remoteFile)) {
				if(!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				FileTransfer transfer = FileTransferHelpler.getTransfer();
				transfer.downloadFileAndDisconnect(remoteFile, pathFile);
			}
			InputStream inputStream = new FileInputStream(pathFile);
			XSSFWorkbook tempFile=new XSSFWorkbook(inputStream);
			SXSSFWorkbook workbook = new SXSSFWorkbook(tempFile);
			for (String tableId : exportTableId) {
				String sql_table = "SELECT t.row_start,t.column_start,t.skip_column,t1.system_table_name, t.template_name,t1.export_table_id,t1.id, template_file_name, template_file_path, import_type, version, template_status, imp_usr, imp_date, imp_time, oss_file_path\n" +
						"FROM import_template_manage  t  inner join app_table_info t1 on t.system_table_name = t1.id where  1=1 and  t1.id ='"+tableId+"'  order by imp_date desc limit 1";
				List<SqlRow> rows_table = comnDao.findRows(sql_table);
				String importType = ""; //导入类型 01 横表 02 纵表
				if (rows_table != null && rows_table.size() > 0) {
					params.put("dataTemplateName",rows_table.get(0).getString("template_name"));
					params.put("reportTable",rows_table.get(0).getString("system_table_name"));
					importType = rows.get(0).getString("import_type");
					skipCellNum = rows_table.get(0).getString("skip_column").split(",");
					rowNum = Integer.parseInt(rows_table.get(0).getString("row_start"));
					if("".equals(rows_table.get(0).getString("column_start"))){
						cellNum=0;
					}else{
						cellNum = Integer.parseInt(rows_table.get(0).getString("column_start"));
					}
					params.put("row_start",rowNum); //设置开始行
					params.put("column_start",cellNum); //设置开始列
				}
				Sheet sheet0 = workbook.getSheetAt(sheetNum);
				CellStyle style = workbook.createCellStyle();
				style.setDataFormat((short) 49);
				if("01".equals(importType)){ //01 横表的处理逻辑【1维报表 程晓鹏 2025.03.11 modify】
					String reportDate = "";
					if(params.get("reportDate")!=null){
						reportDate = params.get("reportDate").toString();
					}else if(params.get("startDate")!=null){
						reportDate = params.get("startDate").toString();
					}
					String strSql = "select database_column_code,column_dict from import_template_manage_field_01 where system_table_name = '"+tableId+"' order by cast(template_column_serial as unsigned)";
					List<SqlRow> row_1columns = comnDao.findRows(strSql); //查询一维表配置的列字段
					List<String> columns1 = new ArrayList<>(); //一维的列名
					List<String> columnDict = new ArrayList<>(); //一维的列名
					if (row_1columns != null && row_1columns.size() > 0) {
						StringBuilder strQueryTableSql = new StringBuilder();
						strQueryTableSql.append("select ");
						for(int i=0; i<row_1columns.size(); i++){
							String columnName =  row_1columns.get(i).getString("database_column_code");
							String dict =  row_1columns.get(i).getString("column_dict");
							strQueryTableSql.append(columnName);
							columns1.add(columnName); //插入列
							columnDict.add(dict); //插入字典序列
							strQueryTableSql.append(", ");
						}
						strQueryTableSql.append(" id from ");
						// 特殊判断资产持仓登记
						String tableName = params.get("reportTable").toString().toLowerCase();
						if ("app_asset_regist_info".equals(tableName)) {
							String lastCycleDay = DateUtil.getLastCycleDay(reportDate,0, 1);
							if (StringUtils.isNotEmpty(reportDate) && reportDate.equals(lastCycleDay)) {
								strQueryTableSql.append(tableName);
							} else {
								strQueryTableSql.append(tableName + "_day");
							}
						} else {
							strQueryTableSql.append(tableName);
						}
						strQueryTableSql.append(" where report_date ='"+reportDate+"' and sys_data_status = '1' ");
						int num = 0;
						int rowsid=0;
						int startRow = Integer.valueOf(params.get("row_start").toString());
						while(num<50){
							num = num+1;
							String querysql = strQueryTableSql.toString()+"and id>"+rowsid+" order by id limit 50000";
							log.info("查询一维报表字段sql为：" + querysql);
							List<SqlRow> list1Data = comnDao.findRows(querysql);
							if(list1Data != null && list1Data.size() > 0){
								int m=0;
								int startColumn = Integer.valueOf(params.get("column_start").toString()).intValue();
								for(SqlRow row: list1Data){ //循环将数据写入到excel文件中
									Row excelrow = sheet0.createRow(startRow++); //创建行
									int dataColumn = startColumn; //数据列索引，
									for(int n=0; n<columns1.size(); n++){
										Object obj = row.get(columns1.get(n));
										String cellValue = "";
										if(obj != null){
											cellValue = obj.toString();
										}
										// 数字字典转换
										if (StringUtils.isNotBlank(columnDict.get(n))) {
											cellValue = valueDictConvert(columnDict.get(n), cellValue);
										}
										Cell cell = excelrow.createCell(dataColumn++); //创建单元格
										cell.setCellValue(cellValue);  //设置1维表格的单元格数据
										cell.setCellStyle(style); //设置1维表格的单元格格式
									}
									m=m+1;
									if(m==list1Data.size()){
										rowsid = Integer.parseInt(row.get("id").toString());
									}
									if(startRow%1000==0){
										((SXSSFSheet)sheet0).flushRows(100);
									}
								}
							}else{
								break;
							}
						}
					}
				}
				sheetNum++;
			}
			// 获取当前系统时间点
			Instant endTime = Instant.now();
			// 计算时间间隔
			Duration duration = Duration.between(startTime, endTime);
			long seconds = duration.getSeconds();
			log.info("本次生成excel用时{}秒",seconds);
			workbook.write(out);
			workbook.dispose();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	public String getDateValue(List<SqlRow> rows_data,int row_id, int column_id){
		String date_value="";
		if (rows_data != null && rows_data.size() > 0){
			for(SqlRow row :rows_data) {
				if((row_id==row.getInteger("row_id"))&&(column_id==row.getInteger("column_id"))){
					date_value = row.getString("data_value");
				}
			}
		}
		return date_value;
	}

	/**
	 * 重构后的Excel生成方法：支持普通导出和模板导出两种模式，兼容大数据量
	 * @param response 响应体
	 * @param datas 原始数据列表
	 * @param excelHeaders Excel表头
	 * @param headKeys 表头对应的字段key
	 * @param colMaps 字段转换配置（类型/字典）
	 * @param start 起始条数
	 * @param end 截止条数
	 * @param unToDict 不需要字典转换的字段
	 * @param params 额外参数
	 * @throws Exception 异常
	 */
	public void exportExcelNew(
			HttpServletResponse response,
			List<?> datas,
			List<List<String>> excelHeaders,
			List<String> headKeys,
			Map<String, Map<String, Object>> colMaps,
			int start,
			int end,
			String unToDict,
			Map<String, Object> params) throws Exception {

		File tempFile = null;
		Instant startTime = Instant.now();
		long totalRecords = end - start;
		boolean isTemplateExport = false;
		try {
			// 1. 创建临时文件
			tempFile = createTempExcelFile();
			log.info("创建临时文件: {}，导出数据范围: {}~{}条",
					tempFile.getAbsolutePath(), start, end);

			// 2. 检查导出模式
			isTemplateExport = !ObjectUtils.isEmpty(params.get("dataExcelTemplate"));

			if (isTemplateExport) {
				// 模板导出模式 - 使用重构后版本
				log.info("使用重构后的模板导出模式，模板文件: {}", params.get("dataExcelTemplate"));
				templateExportOptimized(tempFile, datas, headKeys, colMaps, start, end, unToDict, params);
			} else {
				// 普通导出模式 - 使用EasyExcel（原逻辑）
				log.info("使用普通导出模式");
				normalExport(tempFile, datas, excelHeaders, headKeys,colMaps, start, end, unToDict, params);
			}

			// 3. 设置响应头
			setupResponseHeaders(response, tempFile, isTemplateExport,params);

			// 4. 将临时文件内容写入响应
			writeTempFileToResponse(tempFile, response);

			// 5. 记录日志
			Instant endTime = Instant.now();
			Duration duration = Duration.between(startTime, endTime);
			log.info("Excel导出完成，总记录数：{}条，导出模式：{}，文件大小：{}字节，耗时：{}秒",
					totalRecords,
					(isTemplateExport ? "模板导出" : "普通导出"),
					tempFile.length(),
					duration.getSeconds());

		} catch (ClientAbortException e) {
			log.warn("客户端中断下载连接，可能是用户取消下载", e);
		} catch (Exception e) {
			log.error("Excel导出异常，数据范围: {}~{}，导出模式: {}",
					start, end, isTemplateExport ? "模板导出" : "普通导出", e);
			throw e;
		} finally {
			// 6. 删除临时文件
			deleteTempFile(tempFile);
		}
	}

	/**
	 * 普通导出模式 - 使用EasyExcel
	 */
	private void normalExport(
			File tempFile,
			List<?> datas,
			List<List<String>> excelHeaders,
			List<String> headKeys,
			Map<String, Map<String, Object>> colMaps,
			int start,
			int end,
			String unToDict,
			Map<String, Object> params) throws Exception {

		long totalWrite = 0;
		ExcelWriter excelWriter = null;

		try (FileOutputStream fos = new FileOutputStream(tempFile)) {
			// 1. 创建ExcelWriter
			excelWriter = EasyExcel.write(fos)
					.registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
					.build();

			// 2. 创建Sheet
			WriteSheet writeSheet = EasyExcel.writerSheet("数据")
					.head(excelHeaders)
					.build();

			// 3. 分批处理数据
			int BATCH_SIZE = 10000;
			if (end - start < BATCH_SIZE) {
				BATCH_SIZE = end - start;
			}

			int currentIndex = start;
			int batchNumber = 0;

			while (currentIndex < end) {
				batchNumber++;
				int batchEnd = Math.min(currentIndex + BATCH_SIZE, end);
				List<List<String>> batchData = new ArrayList<>(BATCH_SIZE);

				// 处理当前批次数据
				for (int i = currentIndex; i < batchEnd; i++) {
					Object data = datas.get(i);
					List<String> rowData = new ArrayList<>(headKeys.size());

					for (String headKey : headKeys) {
						String value = extractValueFromData(data, headKey);

						// 类型/字典转换逻辑
						if (Tools.isNotEmpty(value)) {
							if (colMaps.get(headKey) != null && colMaps.get(headKey).containsKey("type")) {
								value = typeConvert(colMaps, value, headKey);
							}
							if (colMaps.get(headKey) != null && colMaps.get(headKey).containsKey("dict")
									&& unToDict.indexOf(headKey) == -1) {
								value = dictConvert(colMaps, value, headKey);
							}
						}
						rowData.add(value);
					}
					batchData.add(rowData);
				}

				// 写入当前批次
				excelWriter.write(batchData, writeSheet);

				// 统计和日志
				totalWrite += batchData.size();
				log.warn("写入批次数据：{}~{}条，累计写入：{}条", currentIndex, batchEnd, totalWrite);

				// 清空批次数据，释放内存
				batchData.clear();
				currentIndex = batchEnd;

				// 每写入5万条刷新一次文件缓冲区
				if (totalWrite % 50000 == 0) {
					fos.flush();
					log.info("临时缓存文件已写入 {} 条数据", totalWrite);
				}else if(totalWrite == (end - start)){
					log.info("临时缓存文件已写入 {} 条数据", totalWrite);
				}
			}

			// 4. 完成写入
			if (excelWriter != null) {
				try {
					excelWriter.finish();
				} catch (Exception e) {
					log.warn("关闭ExcelWriter时发生异常", e);
				}
			}

			// 5. 最终刷新
			fos.flush();

			// 6. 验证写入条数
			if (totalWrite != (end - start)) {
				log.warn("数据写入条数不匹配！请求导出{}条，实际写入{}条", (end - start), totalWrite);
			}

		}
	}

	/**
	 * 从数据对象中提取值
	 */
	private String extractValueFromData(Object data, String headKey) {
		if (data instanceof SqlRow) {
			return ((SqlRow) data).getString(headKey);
		} else if (data instanceof LinkedHashMap) {
			return Tools.obj2Str(((LinkedHashMap) data).get(headKey));
		} else {
			Object valueObj = getFieldValueByName(headKey, data);
			return Tools.obj2Str(valueObj);
		}
	}

	/**
	 * 设置响应头
	 */
	private void setupResponseHeaders(HttpServletResponse response, File tempFile,
									  boolean isTemplateExport ,Map<String,Object> params) throws UnsupportedEncodingException {

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setCharacterEncoding("UTF-8");

		// 设置文件名
		String fileName;
		if (isTemplateExport) {
			// 模板导出使用自定义文件名
			String templateFileName = String.valueOf(params.get("dataExcelTemplate"));
			if (StringUtils.isNotEmpty(templateFileName)) {
				// 保持原文件扩展名
				fileName = URLEncoder.encode(templateFileName, "UTF-8");
			} else {
				fileName = URLEncoder.encode("template_export.xlsx", "UTF-8");
			}
		} else {
			// 普通导出使用默认文件名
			fileName = URLEncoder.encode("download.xlsx", "UTF-8");
		}

		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		response.setHeader("filename", fileName);

		// 设置文件大小（有助于浏览器显示下载进度）
		response.setContentLengthLong(tempFile.length());

		// 添加缓存控制头
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		response.setHeader("Access-Control-Expose-Headers", "filename");
	}

	/**
	 * 将临时文件内容写入响应
	 */
	private void writeTempFileToResponse(File tempFile, HttpServletResponse response)
			throws IOException {

		byte[] buffer = new byte[8192]; // 8KB缓冲区
		int bytesRead;

		try (FileInputStream fis = new FileInputStream(tempFile);
			 ServletOutputStream sos = response.getOutputStream()) {

			while ((bytesRead = fis.read(buffer)) != -1) {
				sos.write(buffer, 0, bytesRead);
			}
			sos.flush();

			log.warn("临时文件内容已成功写入响应，文件大小：{}字节", tempFile.length());
		}
	}

	/**
	 * 创建临时Excel文件
	 */
	private File createTempExcelFile() throws IOException {
		// 使用配置的临时目录
		File dir = new File(temPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// 生成临时文件名
		String tempFileName = "export_" + System.currentTimeMillis() + "_" +
				UUID.randomUUID().toString() + ".xlsx";
		File tempFile = new File(dir, tempFileName);

		// 确保文件存在
		if (!tempFile.exists()) {
			tempFile.createNewFile();
		}

		return tempFile;
	}

	/**
	 * 删除临时文件
	 */
	private void deleteTempFile(File tempFile) {
		if (tempFile != null && tempFile.exists()) {
			try {
				boolean deleted = tempFile.delete();
				if (deleted) {
					log.warn("临时文件已删除: {}", tempFile.getAbsolutePath());
				} else {
					log.warn("临时文件删除失败，将尝试稍后删除: {}", tempFile.getAbsolutePath());
					// 标记为删除，JVM退出时删除
					tempFile.deleteOnExit();
				}
			} catch (Exception e) {
				log.warn("删除临时文件时发生异常: {}", tempFile.getAbsolutePath(), e);
				// 标记为删除，JVM退出时删除
				tempFile.deleteOnExit();
			}
		}
	}

	/**
	 * 模板导出方法 - 基于原customWriteByPoi优化
	 */
	public void templateExportOptimized(
			File tempFile,
			List<?> datas,
			List<String> headKeys,
			Map<String, Map<String, Object>> colMaps,
			int start,
			int end,
			String unToDict,
			Map<String, Object> params) throws Exception {

		Instant startTime = Instant.now();
		SXSSFWorkbook workbook = null;
		File templateFile = null;

		try {
			// 1. 获取模板信息（复用原逻辑）
			String template = String.valueOf(params.get("dataExcelTemplate"));
			String templateName = String.valueOf(params.get("dataTemplateName"));

			// 查询模板配置
			// 查询模板配置
			String sql = "SELECT id, system_table_name, template_name, template_file_name, " +
					"template_file_path, import_type, version, template_status, imp_usr, " +
					"imp_date, imp_time, oss_file_path " +
					"FROM import_template_manage WHERE template_name = '"+templateName+"' ORDER BY imp_date DESC LIMIT 1";

			List<SqlRow> rows = comnDao.findRows(sql);

			if (rows == null || rows.isEmpty()) {
				throw new RuntimeException("未找到模板配置: " + templateName);
			}

			SqlRow templateConfig = rows.get(0);
			String pathFile = templateConfig.getString("template_file_path") +
					templateConfig.getString("template_file_name");
			String remoteFile = templateConfig.getString("oss_file_path");

			// 2. 准备模板文件（并行下载）
			templateFile = prepareTemplateFile(pathFile, remoteFile);

			// 3. 批量数据转换（并行处理）
			List<List<String>> excelDatas = new ArrayList<>(end - start);
			int batchSize = 1000; // 每批处理1000条

			for (int batchStart = start; batchStart < end; batchStart += batchSize) {
				int batchEnd = Math.min(batchStart + batchSize, end);
				List<List<String>> batch = convertDataBatch(datas, headKeys, colMaps,
						unToDict, batchStart, batchEnd);
				excelDatas.addAll(batch);
			}

			log.info("数据转换完成，共{}条数据", excelDatas.size());

			// 4. 使用SXSSFWorkbook（流式API）写入
			try (InputStream inputStream = new FileInputStream(templateFile)) {
				Workbook templateWorkbook;
				if (template.toLowerCase().endsWith(".xls")) {
					templateWorkbook = new HSSFWorkbook(inputStream);
				} else {
					templateWorkbook = new XSSFWorkbook(inputStream);
				}

				// 转换为SXSSFWorkbook，启用流式写入
				workbook = new SXSSFWorkbook((XSSFWorkbook) templateWorkbook, 100); // 内存中保留100行
				workbook.setCompressTempFiles(true); // 压缩临时文件

				// 5. 获取Sheet并设置缓存
				Sheet sheet = workbook.getSheetAt(0);
				((SXSSFSheet) sheet).setRandomAccessWindowSize(100); // 设置随机访问窗口

				// 6. 预创建样式缓存（避免重复创建）
				Map<Integer, CellStyle> styleCache = preCreateStyles(workbook);

				// 7. 并行处理特殊单元格（如果需要）
				processSpecialCells(sheet, params, templateName);

				// 8. 批量写入数据（优化写入性能）
				long writeStart = System.currentTimeMillis();
				writeDataToSheet(sheet, excelDatas, params, styleCache);
				long writeEnd = System.currentTimeMillis();
				log.info("数据写入完成，耗时: {}ms", writeEnd - writeStart);

				// 9. 写入输出文件
				try (FileOutputStream out = new FileOutputStream(tempFile)) {
					workbook.write(out);
					out.flush();
				}

				// 10. 清理临时文件
				workbook.dispose();
			}

			// 记录性能日志
			Instant endTime = Instant.now();
			Duration duration = Duration.between(startTime, endTime);
			log.info("模板导出优化版完成，数据条数：{}条，总耗时：{}秒",
					excelDatas.size(), duration.getSeconds());

		} catch (Exception e) {
			log.error("模板导出异常", e);
			throw e;
		} finally {
			// 清理资源
			if (workbook != null) {
				try {
					workbook.close();
				} catch (Exception e) {
					log.warn("关闭工作簿异常", e);
				}
			}
			// 删除模板临时文件
			if (templateFile != null && templateFile.exists()) {
				templateFile.delete();
			}
		}
	}

	/**
	 * 准备模板文件（支持并行下载）
	 */
	public File prepareTemplateFile(String localPath, String remotePath) throws Exception {
		File file = new File(localPath);

		if (!file.exists()) {
			if (StringUtils.isNotEmpty(remotePath)) {
				// 创建目录
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}

				// 使用并行流下载（如果远程文件支持）
				log.info("开始下载模板文件: {}", remotePath);
				FileTransfer transfer = FileTransferHelpler.getTransfer();
				transfer.downloadFileAndDisconnect(remotePath, localPath);
				log.info("模板文件下载完成: {}", localPath);
			} else {
				throw new FileNotFoundException("模板文件不存在: " + localPath);
			}
		}

		return file;
	}

	/**
	 * 批量转换数据（并行处理优化）
	 */
	public List<List<String>> convertDataBatch(List<?> datas,
											   List<String> headKeys,
											   Map<String, Map<String, Object>> colMaps,
											   String unToDict,
											   int start,
											   int end) {

		List<List<String>> batchData = new ArrayList<>(end - start);

		// 使用并行流处理数据转换（对于CPU密集型操作）
		if (end - start > 1000) { // 数据量大时启用并行
			batchData = IntStream.range(start, end)
					.parallel()
					.mapToObj(i -> convertSingleRow(datas.get(i), headKeys, colMaps, unToDict))
					.collect(Collectors.toList());
		} else {
			// 数据量小时使用串行处理
			for (int i = start; i < end; i++) {
				batchData.add(convertSingleRow(datas.get(i), headKeys, colMaps, unToDict));
			}
		}

		return batchData;
	}

	/**
	 * 转换单行数据（优化性能）
	 */
	public List<String> convertSingleRow(Object data,
										 List<String> headKeys,
										 Map<String, Map<String, Object>> colMaps,
										 String unToDict) {

		List<String> rowData = new ArrayList<>(headKeys.size());

		// 预计算非字典转换的字段
		Set<String> skipDictFields = new HashSet<>();
		if (StringUtils.isNotEmpty(unToDict)) {
			skipDictFields.addAll(Arrays.asList(unToDict.split(",")));
		}

		for (String headKey : headKeys) {
			String value = extractValueFast(data, headKey);

			if (StringUtils.isNotEmpty(value)) {
				// 类型转换
				Map<String, Object> colConfig = colMaps.get(headKey);
				if (colConfig != null) {
					// 普通类型转换
					if (colMaps.get(headKey).containsKey("type")) {
						value = typeConvert(colMaps, value, headKey);
					}
					// 数字字典转换
					if (colMaps.get(headKey).containsKey("dict") && (unToDict.indexOf(headKey) == -1)) {
						value = dictConvert(colMaps, value, headKey);
					}
				}
			}

			rowData.add(value != null ? value : "");
		}

		return rowData;
	}

	/**
	 * 快速值提取（优化反射调用）
	 */
	public String extractValueFast(Object data, String headKey) {
		if (data instanceof SqlRow) {
			return ((SqlRow) data).getString(headKey);
		} else if (data instanceof Map) {
			Object value = ((Map<?, ?>) data).get(headKey);
			return value != null ? value.toString() : null;
		} else {
			// 缓存反射方法，避免重复获取
			return getFieldValueByReflection(data, headKey);
		}
	}

	/**
	 * 缓存反射方法提高性能
	 */
	private static final Map<Class<?>, Map<String, Method>> METHOD_CACHE = new ConcurrentHashMap<>();

	public String getFieldValueByReflection(Object obj, String fieldName) {
		if (obj == null) return null;

		try {
			Class<?> clazz = obj.getClass();
			Map<String, Method> classMethods = METHOD_CACHE.computeIfAbsent(clazz, k -> new HashMap<>());

			Method method = classMethods.get(fieldName);
			if (method == null) {
				String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) +
						fieldName.substring(1);
				method = clazz.getMethod(getterName);
				method.setAccessible(true);
				classMethods.put(fieldName, method);
			}

			Object value = method.invoke(obj);
			return value != null ? value.toString() : null;

		} catch (Exception e) {
			log.debug("反射获取字段值失败: {} -> {}", obj.getClass().getName(), fieldName);
			return null;
		}
	}
	/**
	 * 预创建样式缓存
	 */
	public Map<Integer, CellStyle> preCreateStyles(SXSSFWorkbook workbook) {
		Map<Integer, CellStyle> styleCache = new HashMap<>();

		// 创建文本格式样式
		CellStyle textStyle = workbook.createCellStyle();
		textStyle.setDataFormat((short) 49); // 文本格式
		textStyle.setBorderTop(BorderStyle.THIN);
		textStyle.setBorderBottom(BorderStyle.THIN);
		textStyle.setBorderLeft(BorderStyle.THIN);
		textStyle.setBorderRight(BorderStyle.THIN);
		styleCache.put(0, textStyle);

		// 可以添加更多预定义样式
		return styleCache;
	}

	/**
	 * 处理特殊单元格（模板中的占位符）
	 */
	public void processSpecialCells(Sheet sheet, Map<String, Object> params, String templateName) {
		try {
			// 查询特殊单元格配置
			List<SqlRow> specialCells = comnDao.findRows(
					"SELECT row_num, cell_num, cell_key FROM base_excel_special_config WHERE template_name = '"+templateName+"'" );

			if (specialCells == null || specialCells.isEmpty()) {
				return;
			}

			for (SqlRow cellConfig : specialCells) {
				String rowNums = cellConfig.getString("row_num");
				String cellNums = cellConfig.getString("cell_num");
				String cellKeys = cellConfig.getString("cell_key");

				if (StringUtils.isAnyBlank(rowNums, cellNums, cellKeys)) {
					continue;
				}

				String[] rows = rowNums.split(",");
				String[] cells = cellNums.split(",");
				String[] keys = cellKeys.split(",");

				for (String rowStr : rows) {
					int rowNum = Integer.parseInt(rowStr.trim());
					Row row = sheet.getRow(rowNum);
					if (row == null) continue;

					for (String cellStr : cells) {
						int cellNum = Integer.parseInt(cellStr.trim());
						Cell cell = row.getCell(cellNum);
						if (cell == null) continue;

						for (String key : keys) {
							if (params.get(key) != null) {
								cell.setCellValue(com.kayak.core.util.DateUtil.getLastDayOfYearMonth(params.get(key).toString()));
								break; // 找到一个匹配的key就设置值
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.warn("处理特殊单元格异常", e);
		}
	}

	/**
	 * 批量写入数据到Sheet（性能优化版）
	 */
	public void writeDataToSheet(Sheet sheet,
								 List<List<String>> excelDatas,
								 Map<String, Object> params,
								 Map<Integer, CellStyle> styleCache) {

		// 获取起始位置
		int startRow = 1;
		int startCol = 0;

		if (!ObjectUtils.isEmpty(params.get("dataExcelStartLine"))) {
			startRow = Integer.parseInt(params.get("dataExcelStartLine").toString());
		}
		if (!ObjectUtils.isEmpty(params.get("dataExcelStartCol"))) {
			startCol = Integer.parseInt(params.get("dataExcelStartCol").toString());
		}

		int totalRows = excelDatas.size();
		int batchSize = 20000; // 每批处理20000行

		log.info("开始写入数据，总行数: {}, 起始行: {}, 起始列: {}",
				totalRows, startRow, startCol);

		// 分批次写入，避免内存占用过高
		for (int batchStart = 0; batchStart < totalRows; batchStart += batchSize) {
			int batchEnd = Math.min(batchStart + batchSize, totalRows);

			// 处理当前批次
			for (int i = batchStart; i < batchEnd; i++) {
				int rowIndex = startRow + i;
				Row row = sheet.getRow(rowIndex);
				if (row == null) {
					row = sheet.createRow(rowIndex);
				}

				List<String> rowData = excelDatas.get(i);
				int colIndex = startCol;

				for (String cellValue : rowData) {
					Cell cell = row.getCell(colIndex);
					if (cell == null) {
						cell = row.createCell(colIndex);
						// 使用缓存的样式
						cell.setCellStyle(styleCache.get(0));
					}
					cell.setCellValue(cellValue != null ? cellValue : "");
					colIndex++;
				}
			}

			try {
				// 刷新行到磁盘（对于SXSSF）
				if (sheet instanceof SXSSFSheet) {
					((SXSSFSheet) sheet).flushRows(batchSize);
				}
			} catch (IOException e) {
				// 处理异常，例如记录日志并决定是否继续执行或抛出运行时异常
				log.error("刷新行到磁盘时发生IO异常", e);
				// 根据实际情况，可以选择抛出运行时异常或采取其他措施
				throw new RuntimeException(e);
			}

			// 记录进度
			if (batchEnd % 100000 == 0) {
				log.info("已写入 {} 行数据", batchEnd);
			}
		}
		log.info("数据写入完成，总计 {} 行", totalRows);
	}

}

