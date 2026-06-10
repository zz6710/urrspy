package com.kayak.dps.ods.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.dps.app.model.AssetRightModel;
import com.kayak.dps.ods.dao.AssetRightDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

@Service
@APIDefine(desc = "股权信息服务", model = AssetRightModel.class)
public class AssetRightService {
	protected static final Logger log = LoggerFactory.getLogger(AssetRightService.class);

	@Autowired
	private AssetRightDao assetRightDao;

	@API(desc = "查询股权信息", auth = APIAuth.YES,operation = APIOperation.SELECT)
	public SqlResult<AssetRightModel> findAssetRight(SqlParam<AssetRightModel> params) throws Exception {
		return assetRightDao.findAssetRight(params);
	}

	@API(desc = "新增股权信息",operation = APIOperation.INSTER, auth = APIAuth.YES)
	public String addAssetRight(SqlParam<AssetRightModel> params) throws Exception {
		try {
			params.getModel().setCrtDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setCrtUser((String) SysUtil.getSysUserParamValue("sys_user_userid"));
			int n = assetRightDao.findAssetCount(params);
			if (n > 0) {
				return RequestSupport.updateReturnJson(false, "存在有相同的股权信息，新增失败", null).toString();
			}
			assetRightDao.addAssetRight(params).getAutoId();
			return RequestSupport.updateReturnJson(true, "新增成功！", null).toString();
		}catch(Exception e){
			return RequestSupport.updateReturnJson(false,"新增失败！",null).toString();
		}
	}
	
	@API(desc = "修改股权信息",operation = APIOperation.UPDATE,  auth = APIAuth.YES)
	public String updateAssetRight(SqlParam<AssetRightModel> params) throws Exception {
		try {
			params.getModel().setUpdDate(SysUtil.getSystemParamsByParaid("10004"));
			params.getModel().setUpdUser((String) SysUtil.getSysUserParamValue("sys_user_userid"));
			assetRightDao.updateAssetRight(params).getEffect();
			return RequestSupport.updateReturnJson(true, "修改成功！", null).toString();
		}catch(Exception e){
			return RequestSupport.updateReturnJson(false,"修改失败！",null).toString();
		}
	}
	
	@API(desc = "删除股权信息",operation = APIOperation.DELETE, auth = APIAuth.YES)
	public int deleteAssetRight(SqlParam<AssetRightModel> params) throws Exception {
		return assetRightDao.deleteAssetRight(params).getEffect();
	}

	@API(desc = "导入股权信息", auth = APIAuth.YES,operation = APIOperation.UPDATE)
	public void rightUploadAction(SqlParam<AssetRightModel> params) throws Exception {}
	@API(desc = "模板下载", auth = APIAuth.NO)
	public void exportFile(HttpServletResponse response, String fileName) {
		//  Map<String, Object> params = RequestSupport.getParameters();
		byte[] buffer = new byte[1024];
		FileInputStream fileInputStream = null;
		BufferedInputStream bufferedInputStream = null;
		BufferedOutputStream outputStream = null;
		try{
			if (fileName != null) {
				response.setContentType("application/x-octetstream;charset=utf-8");
				response.setCharacterEncoding("utf-8");
				response.addHeader("Access-Control-Expose-Headers","*");
				response.setHeader("filename",fileName);
				response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
//				response.setHeader("content-disposition", "attachment;filename=" +
//						new String(fileName.getBytes("GB2312"), "ISO8859-1"));


				/**指定下载模板文件的文件服务器的ip、用户名、用户密码等*/
				//远端服务器ip地址
				String remoteIp = SysUtil.getSystemParamsByParaid("70000010004");
				//远端服务器用户名
				String remoteUserName = SysUtil.getSystemParamsByParaid("70000010005");
				//远端服务器密码
				String remoteUserPassword = SysUtil.getSystemParamsByParaid("70000010006");
				//远端服务器文档模板sftp存储路径
				String remotePath = SysUtil.getSystemParamsByParaid("90000010000");
				String dealRemotePath = remotePath;
				//本机或本地服务器模板文档存储路径
				//本机或本地服务器模板文档存储路径
				String temPath =  System.getProperty("os.name").toLowerCase().startsWith("win")
						?SysUtil.getSystemParamsByParaid("90000010001")//本机存放根路径
						:SysUtil.getSystemParamsByParaid("90000010002");//服务器存放根路径
				File dir = new File(temPath);
				if (!dir.exists()) {
					dir.mkdir();
				}
				//文件名
				/**操作远端下载文件存储本地路径*/

//                try {
//                    SftpUtils.getFile(remoteIp, remoteUserName, remoteUserPassword, dealRemotePath, temPath, fileName);
//                } catch (Exception e) {
//                    log.error("获取远端服务器文件失败{}",e);
//                }
				//从本地路径输出前端head中响应IO
				String pathFile = temPath + fileName;
				File file = new File(pathFile);
				fileInputStream = new FileInputStream(file);
				bufferedInputStream = new BufferedInputStream(fileInputStream);
				outputStream = new BufferedOutputStream(response.getOutputStream());
				int i = bufferedInputStream.read(buffer);
				while (i != -1) {
					outputStream.write(buffer, 0, buffer.length);
					outputStream.flush();
					i = bufferedInputStream.read(buffer);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
				if (bufferedInputStream != null) {
					bufferedInputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e2) {
				log.error("io关闭异常[{}]", e2);
			}
		}
	}
}
