package com.kayak.privilege.action;

import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.privilege.dao.PrivilegeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * 描述：Privilege控制
 * 
 * @author zhaojie
 * @version 1.0
 * @date 2021/5/15 09:22
 */

@RestController
public class PrivilegeAction extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(PrivilegeAction.class);


	@RequestMapping(value = "/privilege/download.json", produces = { "application/json;charset=UTF-8" })
	public void downloadXPGGPrintVersion(HttpServletResponse response) {
		byte[] buffer = new byte[1024];
		FileInputStream fileInputStream = null;
		BufferedInputStream bufferedInputStream = null;
		BufferedOutputStream outputStream = null;
		Map<String, Object> params = RequestSupport.getParameters();
		String filepath = (String) params.get("filepath");
		String filename = (String) params.get("filename");
		if (!"null".equals(filepath) && !"".equals(filepath)) {

			File file = new File(filepath);

			log.info("文件路径【{}】", filepath);
			try {

				fileInputStream = new FileInputStream(file);
				bufferedInputStream = new BufferedInputStream(fileInputStream);
				response.setContentType("application/msword;charset=UTF-8");
				response.addHeader("Content-Disposition",
						"attachment;filename=" + new String(
								filename.getBytes("GB2312"),
								"ISO8859-1"));
				outputStream = new BufferedOutputStream(response.getOutputStream());
				int i = bufferedInputStream.read(buffer);
				while (i != -1) {
					outputStream.write(buffer, 0, buffer.length);
					outputStream.flush();
					i = bufferedInputStream.read(buffer);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
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
}
