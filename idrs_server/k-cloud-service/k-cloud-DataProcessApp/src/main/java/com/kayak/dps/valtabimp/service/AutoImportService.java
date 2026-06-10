package com.kayak.dps.valtabimp.service;

import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.valtabimp.biz.ValTabImpDataBiz;
import com.kayak.dps.valtabimp.util.ZipUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * 自动导入估值表
 * @author Lenovo
 *
 */
@Service(value = "autoImportService")
public class AutoImportService {
	
    @Resource(name = "valTabImpDataBiz")
    private ValTabImpDataBiz valTabImpDataBiz;

	
	private static Logger log = LogManager.getLogger(AutoImportService.class);
	/**
	 * 导入主函数
	 * @return
	 * @throws Exception 
	 */
	public synchronized String autoImportService() throws Exception {

		String pathstr="";
		//系统工作日
		String sys_date = DateUtil.getSysWordDay();

//		FileUploadAction fileuploadaction=SysBeans.getBean("fileUploadAction");//获取文件上传
//		String rootpath=fileuploadaction.getSysUploadRootPath();
		// 获取文件上传根路径
		String rootpath = SysUtil.getSystemParamsByParaid("80000080002");
		pathstr=rootpath+"prodvaltab/"+sys_date;//拼接上传地址自动解压


		
		List<File> files= ZipUtil.getFiles(pathstr);
		for(File file:files) {
			log.info("-----"+file.getName());
			if(file.getName().toLowerCase().endsWith(".xls")||file.getName().toLowerCase().endsWith(".xlsx")) {//如果直接是文件
				String fileName = file.getName();
				String tghType =getTghType(fileName);
				log.info("-----"+fileName+"---"+tghType+"----"+new FileInputStream(file));
				valTabImpDataBiz.parseAnalysisGzDataByAuto(fileName, tghType,new FileInputStream(file));
				
			}else if(file.getName().toLowerCase().endsWith(".zip")) {
				
				// 获得zip信息
				ZipFile zipFile = new ZipFile(file,Charset.forName("GBK"));
				@SuppressWarnings("unchecked")
				Enumeration<ZipEntry> enu = (Enumeration<ZipEntry>) zipFile.entries();
				while (enu.hasMoreElements()) {//一个个读取zip文件
					ZipEntry zipElement = (ZipEntry) enu.nextElement();
					InputStream read = zipFile.getInputStream(zipElement);
					String fileName = zipElement.getName();
					String tghType =getTghType(fileName);
					if (fileName.toLowerCase().endsWith(".xls")||fileName.toLowerCase().endsWith(".xlsx")) {//是否为xls文件
//						System.out.println("-----"+fileName+"---"+tghType+"----"+read);
						log.info("调用解析函数具体参数-----"+fileName+"---"+tghType+"----"+read);
						valTabImpDataBiz.parseAnalysisGzDataByAuto(fileName, tghType,read);
					}else {
						
					}
				}
			}
		}

		return "";
	}
	
	/**
	 * 获取托管行类型
	 * @param filename 托管行估值表
	 * @return 文件类型
	 */
    private String getTghType(String filename){
    	String tghType = "";

        	if(filename.endsWith(".xls") || filename.endsWith(".XLS")){
        		// 兴业银行
        		tghType = "XLS";
        	}else{
        		tghType = "XLSX";
        	}


    	return tghType;
    }


	/**
	 * 查询文件夹有存在文件
	 * @return
	 */
	public List<Map>  queryfile() throws Exception {
		//系统工作日
		String sys_date = DateUtil.getSysWordDay();

//		FileUploadAction fileuploadaction=SysBeans.getBean("fileUploadAction");//获取文件上传
//		String rootpath=fileuploadaction.getSysUploadRootPath();
		String rootpath = SysUtil.getSystemParamsByParaid("80000080002");
		String pathstr="";
		pathstr=rootpath+"prodvaltab/"+sys_date;//拼接上传地址自动解压


		List<Map> filelist=new ArrayList<>();
		List<File> files=ZipUtil.getFiles(pathstr);
		int i=1;
		for(File obj:files){
			Map file=new HashMap();
			file.put("id",i);
			file.put("name",obj.getName());//文件名
			file.put("path",obj.getAbsolutePath());//结对路径
			i++;
			filelist.add(file);
		}
    	return filelist;
	}


	/**
	 * 删除文件
	 * @return
	 */
	public String  deletefile(String path,String filename) throws Exception {

		String message="";
		File uploadFile = new File(path);
		if(uploadFile.exists()){
			boolean isDeleted = uploadFile.delete();
			if(!isDeleted){
				log.error("文件【" + filename + "】删除失败！");
				message="删除成功！";
			}else{
				log.info("文件【" + filename + "】删除成功！");
				message="删除成功！";
			}
		}else{
			log.error("文件地址【" + path + "】不存在！");
			message="文件不存在！";
		}

		return message;
	}
	
	
	 public static void main(String args[]) {

			try {
				new AutoImportService().autoImportService();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}



}
