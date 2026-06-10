package com.kayak.auth.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.auth.dao.ServerDao;

@Service
public class ServerService {

	@Autowired
	private ServerDao serverDao;

	public void addServer(List<Map<String, Object>> params, String app_name) throws Exception {
		serverDao.addServer(params, app_name);
	}

	public void addGraphQLModel(String appName, List<Map<String, Object>> params) throws Exception {
		serverDao.addGraphQLModel(appName, params);
	}

}
