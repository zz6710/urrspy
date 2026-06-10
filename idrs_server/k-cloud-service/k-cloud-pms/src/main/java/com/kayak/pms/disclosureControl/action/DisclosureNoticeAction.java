package com.kayak.pms.disclosureControl.action;

import com.alibaba.fastjson.JSONObject;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.ResponseResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.disclosureControl.dao.DisclosureNoticeDao;
import com.kayak.pms.disclosureControl.model.DisclosureNotice;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeVersion;
import com.kayak.pms.disclosureControl.model.DisclosureTruteeApproval;
import com.kayak.pms.disclosureControl.service.DisclosureNoticeDocService;
import com.kayak.pms.disclosureControl.service.DisclosureNoticeService;
import com.kayak.pms.disclosureControl.service.DisclosureNoticeVersionService;

import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DisclosureNoticeAction {
	
	@Autowired
    private DisclosureNoticeDao disclosureNoticeDao;
    @Autowired
    private DisclosureNoticeService disclosureNoticeService;
    
    @Autowired
    private DisclosureNoticeVersionService disclosureNoticeVersionService;

    @Autowired
    private DisclosureNoticeDocService disclosureNoticeDocService;
    
    @Value("${path.word}")
    private String basePath;

	/**
	 * 信披公告审批拒绝回调
	 * @param o 审批参数
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/flowReject.json",produces = { "application/json;charset=UTF-8"})
    public ResponseResult flowReject(@RequestBody Object o) throws Exception {
        String data = JSONObject.toJSONString(o);
        JSONObject json = JSONObject.parseObject(data);
        String str = JSONObject.toJSONString(json.get("latestSubmitParams"));
		String str3 = JSONObject.parseObject(str).toJSONString();
		DisclosureNotice dis = JSONObject.parseObject(str3, DisclosureNotice.class);

        return disclosureNoticeService.DisclosureNoticeRejectFlow(dis);
    }

	/**
	 * 信披公告审批拒绝回调
	 * @param o 审批参数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/startFlowDisclosure.json",produces = { "application/json;charset=UTF-8"})
	public ResponseResult startFlowDisclosure(@RequestBody Object o) throws Exception {
		String data = JSONObject.toJSONString(o);
		JSONObject json = JSONObject.parseObject(data);
		String str = JSONObject.toJSONString(json.get("latestSubmitParams"));
		String str3 = JSONObject.parseObject(str).toJSONString();
		DisclosureNotice dis = JSONObject.parseObject(str3, DisclosureNotice.class);

		return disclosureNoticeService.startFlowDisclosure(dis);
	}

/*    @RequestMapping(value = "/flowAgree.json")
    public ResponseResult flowAgree(@RequestBody Object o) throws Exception {
        String data = JSONObject.toJSONString(o);
        JSONObject json = JSONObject.parseObject(data);
        String str = JSONObject.toJSONString(json.get("latestSubmitParams"));
        JSONObject json2 = JSONObject.parseObject(str);
        String str2 = JSONObject.toJSONString(json2.get("disclosureNotice"));
        String str3 = JSON.parse(str2).toString();
        DisclosureNotice dis=JSONObject.parseObject(str3, DisclosureNotice.class);

        return disclosureNoticeService.DisclosureNoticeAgreeFlow(dis);
    }*/

	/**
	 * 发送托管行审批被拒绝回调
	 * @param o
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/trusteeReject.json",produces = { "application/json;charset=UTF-8"})
    public ResponseResult trusteeReject(@RequestBody Object o) throws Exception {
        String data = JSONObject.toJSONString(o);
        JSONObject json = JSONObject.parseObject(data);
        String str = JSONObject.toJSONString(json.get("latestSubmitParams"));
        DisclosureTruteeApproval trustee = JSONObject.parseObject(str, DisclosureTruteeApproval.class);
        //被拒绝之后把老数据也存一份 做回显用
        //disclosureTruteeApprovalDao.updateTruteeById(trustee);
        return disclosureNoticeService.DisclosureNoticeTrusteeRejectFlow(trustee);
    }

    @RequestMapping(value = "/trusteeAgree.json",produces = { "application/json;charset=UTF-8"})
    public ResponseResult trusteeAgree(@RequestBody Object o) throws Exception {
        String data = JSONObject.toJSONString(o);
        JSONObject json = JSONObject.parseObject(data);
        String str = JSONObject.toJSONString(json.get("latestSubmitParams"));
        //String str2 = JSONObject.toJSONString(json);

        DisclosureTruteeApproval trustee = JSONObject.parseObject(str, DisclosureTruteeApproval.class);

        return disclosureNoticeService.DisclosureNoticeTrusteeAgreeFlow(trustee);
    }

	/**
	 * 信披公告版本批量下载
	 * @param response
	 * @throws Exception
	 */
    @RequestMapping(value = "/notice/batchDownLoad.json",produces = { "application/json;charset=UTF-8"})
    public void BatchDownload( HttpServletResponse response) throws Exception{
		String fileName = DateUtil.getNowDate() + ".zip";
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Expose-Headers", "filename");
		response.setContentType("application/octet-stream; charset=utf-8");
		response.setHeader("Content-disposition", "attachment;filename=" + fileName + "");
		response.setHeader("filename", fileName);

		Map<String, Object> paramsDirect = RequestSupport.getParameters();
		//用户选择的公告
		List<DisclosureNoticeVersion> noticeList = JSONObject.parseArray(String.valueOf(paramsDirect.get("list")), DisclosureNoticeVersion.class);
		//根据前端查询条件查询公告
		Map<String,Object> queryInfo = JSONObject.parseObject(String.valueOf(paramsDirect.get("prodSearchParam")), Map.class) ;
		if(MapUtil.isNotEmpty(queryInfo)&&CollectionUtils.isEmpty(noticeList)) {
			SqlParam<DisclosureNoticeVersion> params = new FetcherData<DisclosureNoticeVersion>(queryInfo,DisclosureNoticeVersion.class);
			SqlResult<DisclosureNoticeVersion> data = disclosureNoticeVersionService.findDisclosureNoticeVersion(params);
			noticeList= data.getRows();
		}
		String os = System.getProperty("os.name");
		String path = "";
		if(os.toLowerCase().startsWith("win")) {
			String basePaths = SysUtil.getSystemParamsByParaid("70000010002");
			path = basePaths + "notice/zip/";
		}else {
			String basePaths = SysUtil.getSystemParamsByParaid("70000010009");
			path = basePaths + "notice" + File.separator + "zip" + File.separator;
		}


		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		String temZip= path+fileName;
		log.info("临时文件路径{}", temZip);
		FileOutputStream fileOutputStream = null;
		ZipOutputStream zipOutputStream = null;
		File temZipFile = new File(temZip);
		try {
			temZipFile.createNewFile();
			fileOutputStream = new FileOutputStream(temZipFile);
			zipOutputStream = new ZipOutputStream(fileOutputStream);
			//循环下载文件
			for (DisclosureNoticeVersion noticeVersion : noticeList) {

				/** 在文件发布上传路径检索最新版本的公告文件是否存在，若不存在则调用生成方法并返回上传文件路径 */
				Map<String, Object> pubFile_params = disclosureNoticeDocService.judgeNoticeDocIsExistAndGenerateDoc(noticeVersion.getT8DisclosureNoticeId(), noticeVersion.getId(),noticeVersion.getFileName(),true);

				/**该文件后缀为公告生成后替换的公告文件后缀，应与模板一致，若无，则 .docx */
				String noticeFilePath = (String) pubFile_params.get("filePath") +pubFile_params.get("suffix");//模板后缀
				ZipEntry zipEntry = null;
				File file =  new File(noticeFilePath);
				if(!file.isFile()){
					continue;
				}
				if(!file.exists()){
					file.createNewFile();
				}

				FileInputStream fileInputStream = new FileInputStream(file);
				zipEntry = new ZipEntry(file.getName());
				zipOutputStream.putNextEntry(zipEntry);

				int len;
				byte[] buffer = new byte[1024];
				while ((len = fileInputStream.read(buffer)) > 0) {
					zipOutputStream.write(buffer, 0, len);
				}
				fileInputStream.close();
				/*if(file.exists()) {
					file.delete();
				}*/
			}
		} catch (Exception e) {
			log.error("报告批量下载异常{}", e.getMessage());
		} finally {
			try {
				if (zipOutputStream != null) {
					zipOutputStream.closeEntry();
					zipOutputStream.close();
				}
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
				OutputStream ops = response.getOutputStream();
				ops.write(FileUtils.readFileToByteArray(temZipFile));
				temZipFile.delete();
			} catch (Exception e2) {
				log.error("io 关闭异常{}", e2.getMessage());
			}
		}
    }

    
    
}