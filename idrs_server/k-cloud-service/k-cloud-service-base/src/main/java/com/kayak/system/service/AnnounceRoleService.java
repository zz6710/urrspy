package com.kayak.system.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.system.dao.AnnounceRoleDao;
import com.kayak.system.model.AnnounceRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "公告角色服务", model = AnnounceRole.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class AnnounceRoleService {

	private final AnnounceRoleDao announceRoleDao;

	@API(desc = "查询公告角色关系列表")
	public SqlResult<AnnounceRole> find(SqlParam<AnnounceRole> param) throws Exception {
		return announceRoleDao.find(param);
	}

}
