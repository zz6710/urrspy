package com.kayak.flowwork.service;

import com.google.common.collect.Lists;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.constants.RoleAuthorityTypeEnum;
import com.kayak.core.system.constants.UserConstants;
import com.kayak.core.util.Tools;
import com.kayak.flowwork.dao.WfBusinessConfigDao;
import com.kayak.flowwork.model.WfBusinessConfig;
import com.kayak.system.dao.MenuDao;
import com.kayak.system.dao.ServerMethodDao;
import com.kayak.system.model.Menu;
import com.kayak.system.model.RoleAuthority;
import com.kayak.system.model.ServerMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@APIDefine(desc = "工作流业务配置", model = WfBusinessConfig.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class BusinessConfigurationService {

	private final WfBusinessConfigDao wfBusinessConfigDao;

	private final MenuDao menuDao;

	private final ServerMethodDao serverMethodDao;
	

	@API(desc = "查询工作流业务配置",auth = APIAuth.YES)
	public SqlResult<WfBusinessConfig> find1(SqlParam<WfBusinessConfig> param) throws Exception {
		return find(param);
	}

	@API(desc = "查询工作流业务配置",auth = APIAuth.NO)
	public SqlResult<WfBusinessConfig> find(SqlParam<WfBusinessConfig> param) throws Exception {
		param.setMakeSql(true);
		return wfBusinessConfigDao.find(param);
	}

	@API(desc = "查询可选业务列表",auth = APIAuth.NO)
	public SqlResult<RoleAuthority> findServerMethodTree(SqlParam param) throws Exception {
		String roleId = UserConstants.SUPER_ROLE_ID;

		Map<String, List<ServerMethod>> serverMethodMap = serverMethodDao.findByRoleId(roleId).stream()
//				.filter(serverMethod -> !APIOperation.SELECT.getOperation().equals(serverMethod.getOperation()))
				.collect(Collectors.toMap(ServerMethod::getModelName, s -> Lists.newArrayList(s),
						(List<ServerMethod> newValueList, List<ServerMethod> oldValueList) -> {
							oldValueList.addAll(newValueList);
							return oldValueList;
						}));

		// 避免多个菜单配置相同的model，导致界面的tree的key相同
		Set<String> alreadyExistsModelSet = new HashSet<>();

		List<RoleAuthority> roleAuthorities = new ArrayList<>();
		List<Menu> menus = menuDao.findByRoleId(roleId);
		for (Menu menu : menus) {
			String model = menu.getModel();
			if (Tools.isBlank(model)) {
				continue;
			}
			int beforeRoleAuthoritieSize = roleAuthorities.size();

			String[] modelArr = model.split(",");
			for (String m : modelArr) {
				if (Tools.isBlank(m)) {
					continue;
				}
				if (alreadyExistsModelSet.contains(m)) {
					continue;
				} else {
					alreadyExistsModelSet.add(m);
				}
				List<ServerMethod> serverMethods = serverMethodMap.get(m);
				if (CollectionUtils.isEmpty(serverMethods)) {
					continue;
				}
				for (ServerMethod serverMethod : serverMethods) {
					roleAuthorities.add(new RoleAuthority(serverMethod.getServer(), menu.getMenuid(),
							RoleAuthorityTypeEnum.SERVER.getValue(), serverMethod.getServerDesc()));
				}

			}

			int afterRoleAuthorities = roleAuthorities.size();
			boolean menuHaveServerMethod = afterRoleAuthorities > beforeRoleAuthoritieSize;
			if (menuHaveServerMethod) {
				roleAuthorities.add(new RoleAuthority(menu.getMenuid(), menu.getUpperid(),
						RoleAuthorityTypeEnum.MENU.getValue(), menu.getMenuname()));
			}
		}

		return SqlResult.build(roleAuthorities, roleAuthorities.size());
	}

	@API(desc = "新增工作流业务配置",auth = APIAuth.YES)
	public String add(SqlParam<WfBusinessConfig> param) throws Exception {
		try {
			if (wfBusinessConfigDao.add(param) < 1) {
				throw new PromptException("添加失败");
			}
			// 刷新缓存
			//refresh();
			CacheUtil.freshenFlow();
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new PromptException("数据已存在");
		}
		return RequestSupport.updateReturnJson(true, "添加成功", null).toString();
	}
	
	
	

	@API(desc = "编辑工作流业务配置",auth = APIAuth.YES)
	public String edit(SqlParam<WfBusinessConfig> param) throws Exception {
		if (wfBusinessConfigDao.edit(param) < 1) {
			throw new PromptException("编辑失败");
		}
		// 刷新缓存
		CacheUtil.freshenFlow();
		return RequestSupport.updateReturnJson(true, "编辑成功", null).toString();
	}

	@API(desc = "删除工作流业务配置",auth = APIAuth.YES)
	public String delete(SqlParam<WfBusinessConfig> param) throws Exception {
		wfBusinessConfigDao.delete(param);
		// 刷新缓存
		//refresh();
		CacheUtil.freshenFlow();
		return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
	}
	
	@API(desc = "启用工作流业务配置",auth = APIAuth.YES)
	public String turnOn(SqlParam<WfBusinessConfig> param) throws Exception {
		wfBusinessConfigDao.turnOn(param);
		// 刷新缓存
		//refresh();
		CacheUtil.freshenFlow();
		return RequestSupport.updateReturnJson(true, "开启成功", null).toString();
	}
	@API(desc = "停用工作流业务配置",auth = APIAuth.YES)
	public String turnDown(SqlParam<WfBusinessConfig> param) throws Exception {
		wfBusinessConfigDao.turnDown(param);
		//refresh();
		CacheUtil.freshenFlow();
		return RequestSupport.updateReturnJson(true, "停用成功", null).toString();
	}
	
	/*public void refresh() {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);	
		requestHeaders.add("Authorization",RequestSupport.getLocalRequest().getHeader("Authorization") );
		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
		// HttpEntity
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, requestHeaders);
		
	    restTemplate.postForEntity("http://PmsApp/flow/reload.json", requestEntity, Object.class).getBody();
	}*/

}
