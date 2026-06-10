package com.kayak.upload.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kayak.core.sql.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.graphql.service.GraphqlService;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DownloadAction extends BaseController {

	@Autowired
	private GraphqlService graphqlService;
    /*modify by zzhangchangsi 20210831 将postMapping改为RequestMapping 为了使get请求也能下载文件*/
	@RequestMapping(value = "/download/server/{serverName}/**")
	public void serverDownload(HttpServletRequest httpRequest, HttpServletResponse response,
			@PathVariable String serverName) {
		try {
			String currentURL = httpRequest.getRequestURI();
			String prefix = "server/" + serverName;
			if (!RequestSupport.getCanCodeMore(RequestSupport.getParameters())) {
				log.error("检测到sql注入异常");
				return;
			}
			String url = currentURL.substring(currentURL.indexOf(prefix) + prefix.length());
				graphqlService.requestDownload(serverName, url, RequestSupport.getParameters(), response);
//			 return updateSuccess("下载成功!");

		} catch (Exception e) {
			e.printStackTrace();
//			String a=updateFailure(e.getMessage());
//		  return a;
		}
	}

	@PostMapping(value = "/download/json/server/{serverName}/**")
	public void serverDownloadJson(HttpServletRequest httpRequest, HttpServletResponse response,
			@PathVariable String serverName) {
		try {
			String currentURL = httpRequest.getRequestURI();
			String prefix = "server/" + serverName;
			String url = currentURL.substring(currentURL.indexOf(prefix) + prefix.length());

			// TODO 财富后台接口，目前均采用"/Txxx"模式，所以需要将".json"去掉，如果其他系统有用.json结尾，则去掉改行代码
			url = url.replace(".json", "");

			graphqlService.requestDownloadJson(serverName, url, RequestSupport.getParameters(), response);
//			 return updateSuccess("下载成功!");

		} catch (Exception e) {
//			String a=updateFailure(e.getMessage());
//		  return a;
		}
	}

}
