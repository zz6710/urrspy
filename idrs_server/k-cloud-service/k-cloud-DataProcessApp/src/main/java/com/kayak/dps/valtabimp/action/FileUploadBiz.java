package com.kayak.dps.valtabimp.action;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.ods.constants.Constants;
import com.kayak.dps.ods.util.zz.SFtpHelper;
import com.kayak.dps.valtabimp.dbfUtils.DbfFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author xuzy
 * @date 20181219
 *
 */

@Scope("prototype")
@Slf4j
@Controller
public class FileUploadBiz extends BaseController {

	@Autowired
	private ComnDao comnDao;

	@RequestMapping(value = "/base/file-upload.json")
	public void upload(HttpServletResponse response,
			@RequestParam(value = "file", required = false) MultipartFile file, String path,
						 JSONArray returndatas) {
		
		response.setContentType("application/json;chartset=UTF-8");
		
		String fileUploadRootPath = "";
		String fileUploadPath = "";
		try {
			fileUploadRootPath = getSysUploadRootPath();
			fileUploadRootPath = fileUploadRootPath.endsWith("/") ? fileUploadRootPath : fileUploadRootPath + "/";
			if(StringUtils.isNotBlank(path)){
				fileUploadPath = fileUploadRootPath + path;
			}else{
				fileUploadPath = fileUploadRootPath;
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		if (file != null) {
			String fileName = file.getOriginalFilename();
			if(!fileUploadPath.endsWith("/")){
				fileUploadPath = fileUploadPath + "/";
			}
			File fileDirectory = new File(fileUploadPath);
            if(!fileDirectory.exists()){
            	fileDirectory.mkdirs();
            }
			String uploadFileName = fileUploadPath + fileName;
			File uploadFile = new File(uploadFileName);
			
            try {
	            if(uploadFile.exists()){
	            	boolean isDeleted = uploadFile.delete();
	            	if(!isDeleted){
	            		log.error("文件【" + uploadFileName + "】删除失败！");
	            	}else{
	            		log.info("文件【" + uploadFileName + "】删除成功！");
	            	}
	            }
            	file.transferTo(uploadFile);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				try {
					response.getWriter().write(updateFailure("文件上传失败"));
				} catch (IOException e1) {
					log.error(e1.getMessage(), e1);
				}
			}
			try {
				Map<String, Object> returndata = new HashMap<>();
				returndata.put("uploadFileName",uploadFileName);//20220610 放入上传文件的全路径，返回前端处理
				returndata.put("path",path);//20220610 相对路径下载时候需要用到
				returndatas.put(returndata);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	@RequestMapping(value = "/base/file-download.action")
	public void download(HttpServletRequest request, HttpServletResponse res) throws Exception {
		Map<String, Object> params = RequestSupport.getParameters();
		String fileName = (String) params.get("fileName");
		String filePath = (String) params.get("path");
	
		String fileUploadRootPath = "";
		String fileUploadPath = "";
		try {
			fileUploadRootPath = getSysUploadRootPath();
			fileUploadRootPath = fileUploadRootPath.endsWith("/") ? fileUploadRootPath : fileUploadRootPath + "/";
			if(StringUtils.isNotBlank(filePath)){
				fileUploadPath = fileUploadRootPath + filePath;
			}else{
				fileUploadPath = fileUploadRootPath;
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		if(!fileUploadPath.endsWith("/")){
			fileUploadPath = fileUploadPath + "/";
		}
		File fileDirectory = new File(fileUploadPath);
        if(!fileDirectory.exists()){
        	throw new Exception(fileUploadPath + "文件目录不存在");
        }
		
		File file = new File(fileUploadPath + fileName);
		OutputStream os = null;
		FileInputStream fos = null;
		try {
			fileName = URLEncoder.encode(fileName, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			fos = new FileInputStream(file);
			os = res.getOutputStream();
			res.reset();
			res.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"),"ISO-8859-1"));
			res.setContentType("application/octet-stream; charset=utf-8");
		    byte[] b =new byte[100];
		    int len;
	        while((len = fos.read(b)) > 0){
	        	os.write(b, 0, len);
	        }
			os.flush();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}
	
	/**
	 * 获取系统文件上传根路径
	 * @return
	 * @throws Exception
	 */
	public String getSysUploadRootPath() throws Exception {

		List<SqlRow> rst;
		String path = "";
		String exeid = "";
		String os = System.getProperty("os.name");
		if(os.toLowerCase().startsWith("win")){
			exeid = "M817EQ013";
		}else {
			exeid = "M817EQ014";
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		try {
			rst = comnDao.findRows("select paravalue from sys_param where moduleid = '9' and paraid = '80000080002'", paraMap);
			for (SqlRow rs : rst)
			path = rs.getString("paravalue");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		if(!path.substring(path.length()-1).equals("/")){
			path = path + "/";
		}
		return path;
	}

	/**
	 * 上传委外估值文件至估值回传sftp目录
	 * 先获取文件流生成本地文件再上传指定服务器目录下
	 * @param response
	 * @throws Exception
	 */
	public void uploadExternalGZFile(HttpServletResponse response,@RequestParam(value = "file", required = false) MultipartFile file, Map<String, String> gzParams) throws Exception {
		//获取指定接口文件名称
		String settle_date = gzParams.get("settle_date");//估值文件日期
		String sftp_ip = getExternalGzFileUploadParams(Constants.CONFIG_TYPE_JYHC, "SFTP_IP");
		String username = getExternalGzFileUploadParams(Constants.CONFIG_TYPE_JYHC, "USERNAME");
		String password = getExternalGzFileUploadParams(Constants.CONFIG_TYPE_JYHC, "PASSWORD");
		String remote_path = getExternalGzFileUploadParams(Constants.CONFIG_TYPE_JYHC, "REMOTE_PATH_UPLD").replace("[deal_date]", settle_date).trim();//上传路径
		String local_path = "";//本地路径
		String file_name = file.getOriginalFilename();
		String system = System.getProperty("os.name");
		try {
			if (system.toLowerCase().startsWith("win")) {
				local_path = getExternalGzFileUploadParams(Constants.CONFIG_TYPE_GZHC, "LOCAL_PATH_WIN");
			} else {
				local_path = getExternalGzFileUploadParams(Constants.CONFIG_TYPE_GZHC, "LOCAL_PATH_LINUX");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		local_path = local_path.replace("[deal_date]", settle_date).trim();

		try{
			//生成本地文件
			File localFile = new File(local_path + file_name);

			if(!localFile.getParentFile().exists()){//目录不存在时创建目录
				localFile.getParentFile().mkdirs();
			}
			if(localFile.exists()){//文件存在则先删后生成，避免估值表错误重新导入上传仍为错误估值表
				localFile.delete();
				localFile.createNewFile();
			}
			file.transferTo(localFile);

			//远程服务器上传文件至本地目录
			SFtpHelper.putFile(sftp_ip, username, password, remote_path, local_path, file_name,"1");
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(updateFailure("委外估值文件上传失败:" + e.getMessage()));
		}

	}

	/**
	 * 获取委外估值文件回传sftp相关参数
	 * @param gZConstant
	 * @return
	 * @throws Exception
	 */
	public String getExternalGzFileUploadParams (String gZConstant, String key) throws Exception {
		String sqlStr = "SELECT config_code value FROM base_port_config_info WHERE config_name = '" + key + "' AND config_type = '" + gZConstant + "'";
		return comnDao.findRow(sqlStr, DataSourceProperty.PUB).getString("value").trim();
	}
	
}
