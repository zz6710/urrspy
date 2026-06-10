package com.kayak.graphql.service;

import com.google.common.collect.Sets;
import com.kayak.auth.dao.ErrLogDao;
import com.kayak.auth.dao.WorkflowDao;
import com.kayak.auth.dao.model.WfBusiExtend;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.system.constants.SystemAuthLogicTypeEnum;
import com.kayak.core.system.constants.SystemAuthOpjoinTypeEnum;
import com.kayak.core.system.constants.UserConstants;
import com.kayak.core.util.FileUtil;
import com.kayak.core.util.HttpUtil;
import com.kayak.core.util.NetworkUtil;
import com.kayak.core.util.Tools;
import com.kayak.graphql.autoconfigure.GraphQLAnnotationImpl;
import com.kayak.login.dao.LoginDao;
import com.kayak.login.service.LoginService;
import graphql.schema.DataFetcher;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class GraphqlService {

	private static final Logger log = LoggerFactory.getLogger(GraphqlService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private WorkflowDao workflowDao;

	@Autowired
	private LoginDao loginDao;

    @Autowired
    private LoginService loginService;

    @Autowired
    private ErrLogDao errLogDao;

    public static final String PROCESS_INSTANCE_ID = "_wfProcessInstanceId";

    public DataFetcher<Object> queryFetcher(String appName, String serverName, String modelFullName) {
        return dataFetchingEnvironment -> {
            Map<String, Object> params = dataFetchingEnvironment.getArguments();
            return commQuery(appName, serverName, modelFullName, params,
                    NetworkUtil.getIpAddress(RequestSupport.getLocalRequest()));
        };
    }

    public DataFetcher<Object> mutationFetcher(String appName, String serverName, String modelFullName) {
        return dataFetchingEnvironment -> {
            Map<String, Object> params = dataFetchingEnvironment.getArguments();
            return commUpdate(appName, serverName, modelFullName, params,
                    NetworkUtil.getIpAddress(RequestSupport.getLocalRequest()));
        };
    }

        public Object commQuery(String appName, String serverName, String modelFullName, Map<String, Object> oldParams,
                            String ip) throws Exception {
        // 获取操作对象实例
        Map<String, Object> params = new HashMap<>(oldParams);
        String action = Tools.obj2Str(params.get("action"));
        params.put("modelClassName", modelFullName);
            if("findXmlInfo".equals(action) ||"findHtmlInfo".equals(action) ||"findXmlSqlInfo".equals(action)){//报表特殊处理
                checkServerForReport(serverName, action, (String) params.get("forTable"));
            }else{
                checkServer(serverName, action, ip);
            }




		// headers
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		requestHeaders.add("Authorization", SysUtil.getSysUserParamValue("sys_user_userid").toString());
		// HttpEntity
		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();

		if (params != null && !params.isEmpty()) {
			Set<String> keys = params.keySet();
			for (String key : keys) {
				postParameters.add(key, params.get(key));
			}
		}

		// HttpEntity
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, requestHeaders);

		Object body = restTemplate
				.postForEntity("http://" + appName + "/graphql/commQuery.json", requestEntity, Object.class).getBody();

		return body;
	}

    public Object commUpdate(String appName, String serverName, String modelFullName, Map<String, Object> params,
                             String ip) throws Exception {

		String action = Tools.obj2Str(params.get("action"));
		params.put("modelClassName", modelFullName);

        checkServer(serverName, action, ip);
        try {
            checkAuth(serverName, action);
        } catch (Exception e) {
            JSONObject _json = new JSONObject();
            _json.put("success", false);
            _json.put("authOpcheck", true);
            _json.put("returnmsg",e.getMessage());
            return Tools.json2map(_json);
        }

        // headers
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        requestHeaders.add("Authorization", SysUtil.getSysUserParamValue("sys_user_userid").toString());
        // HttpEntity
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        if (params != null && !params.isEmpty()) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                postParameters.add(key, params.get(key));
            }
        }

        // HttpEntity
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, requestHeaders);
        String body = restTemplate
                .postForEntity("http://" + appName + "/graphql/commUpdate.json", requestEntity, String.class).getBody();

		JSONObject jsonObject = new JSONObject(body);
		return Tools.json2map(jsonObject);
	}

	private boolean isSuperRole(String roleIds) {
		if (Tools.isBlank(roleIds)) {
			return false;
		}

		String[] roleIdArr = roleIds.split(",");
		return ArrayUtils.contains(roleIdArr, UserConstants.SUPER_ROLE_ID);
	}
    public void checkServerForReport(String serverName, String action ,String menuid) throws Exception {

        String roleIds = (String) SysUtil.getSysUserParamValue("sys_user_roleids");
        String userid = (String) SysUtil.getSysUserParamValue("sys_user_userid");

        String server = serverName + "-" + action;

        // 需要进行权限控制
        if (!isSuperRole(roleIds) && GraphQLAnnotationImpl.authMap.containsKey(server)) {
            Map<String, String> menuMap =  SysUtil.getUserMenuMap(userid);
            if (menuMap == null || !menuMap.containsKey(menuid)) {
                SqlRow serverData = GraphQLAnnotationImpl.authMap.get(server);
                SqlRow apiDefine = GraphQLAnnotationImpl.authMap.get(serverName);
                throw new PromptException("权限不足，禁止访问，请求服务：" + apiDefine.getString("server_desc") + "，接口："
                        + serverData.getString("server_desc")+"菜单："+menuid);
            }
        }

    }

    public void checkServer(String serverName, String action, String ip) throws Exception {
        checkServer(serverName, action, null, ip);
    }

    /**
     * 校验权限
     *
     * @param serverName        service的名称
     * @param action            service的方法名
     * @param processInstanceId 流程实例id
     * @param ip                ip
     * @throws Exception
     */
    public void checkServer(String serverName, String action, String processInstanceId, String ip) throws Exception {
        String roleIds = (String) SysUtil.getSysUserParamValue("sys_user_roleids");

		String server = serverName + "-" + action;

		// 需要进行权限控制
		if (!isSuperRole(roleIds) && GraphQLAnnotationImpl.authMap.containsKey(server)) {
			Map<String, String> servers = (Map<String, String>) SysUtil.getSysUserParamValue("sys_user_servers");
			if (servers == null || !servers.containsKey(server)) {

				if (processInstanceId != null) {
					WfBusiExtend wfBusiExtend = workflowDao.findWfBusiExtend(processInstanceId);
					String userid = wfBusiExtend.getUserid();

					if (isSuperuserUserId(userid)) {
						// 如果流程发起人有该权限，则可以访问
						return;
					}

					Set<String> userServerSet = getUserServerSet(userid);

					if (userServerSet.contains(server)) {
						// 如果流程发起人有该权限，则可以访问
						return;
					}
				}

                SqlRow serverData = GraphQLAnnotationImpl.authMap.get(server);
                SqlRow apiDefine = GraphQLAnnotationImpl.authMap.get(serverName);
                Map<String, Object> errLogMap = new HashMap<>();
                errLogMap.put("userid", SysUtil.getSysUserParams().get("userid"));
                errLogMap.put("ip", ip);
                errLogMap.put("operation", serverData.getString("server_desc"));
                errLogMap.put("operation_date", Tools.getCurrentDate());
                errLogMap.put("operation_time", Tools.getStringFromDate("HHmmss", new Date()));
                errLogDao.addErrLog(errLogMap);
                throw new PromptException("权限不足，禁止访问，请求服务：" + apiDefine.getString("server_desc") + "，接口："
                        + serverData.getString("server_desc"));
            }
        }

    }

    public void checkServer(String url)throws Exception{
        String roleIds = (String) SysUtil.getSysUserParamValue("sys_user_roleids");
        url="zf-"+url;
        // 需要进行权限控制
        if (!isSuperRole(roleIds) && (GraphQLAnnotationImpl.authMap.containsKey(url))) {
            Map<String, String> servers = (Map<String, String>) SysUtil.getSysUserParamValue("sys_user_servers");
            if (servers == null || !servers.containsKey(url)) {
                SqlRow serverData = GraphQLAnnotationImpl.authMap.get(url);
                SqlRow apiDefine = GraphQLAnnotationImpl.authMap.get(url);
                Map<String,Object> errLogMap=new HashMap<>();
                errLogMap.put("userid",SysUtil.getSysUserParams().get("userid"));
                errLogMap.put("ip",NetworkUtil.getIpAddress(RequestSupport.getLocalRequest()));
                errLogMap.put("operation",serverData.getString("server_desc"));
                errLogMap.put("operation_date",Tools.getCurrentDate());
                errLogMap.put("operation_time",Tools.getStringFromDate("HHmmss",new Date()));
                errLogDao.addErrLog(errLogMap);
                throw new PromptException("权限不足，禁止访问，请求服务：" + apiDefine.getString("server_desc") + "，接口："
                        + serverData.getString("server_desc"));
            }
        }
    }

    /**
     * 校验操作授权
     *
     * @param serverName service的名称
     * @param action     service的方法名
     */
    public void checkAuth(String serverName, String action) throws Exception {

        String server = serverName + "-" + action;
        Map<String, Object> params = RequestSupport.getParameters();

        if (!params.containsKey("btnop")) {
            return;
        }
        if (!GraphQLAnnotationImpl.authOpCheckMap.containsKey(server)) {
            return;
        }

        List<SqlRow> sqlRows = GraphQLAnnotationImpl.authOpCheckMap.get(server);
        List<SqlRow> andRows = new LinkedList<>();
        List<SqlRow> orRows = new LinkedList<>();

        for (SqlRow item : sqlRows) {
            if (item.getString("opjoin").equals(SystemAuthOpjoinTypeEnum.OR.getValue())) {
                orRows.add(item);
                continue;
            }
            if (item.getString("opjoin").equals(SystemAuthOpjoinTypeEnum.AND.getValue())) {
                andRows.add(item);
                continue;
            }
        }

        boolean needAuth = false;
        int num = 0;//num代表and条件满足的数量，只有num==andRows.size（）才满足
        for (SqlRow item : andRows) {
            String field = item.getString("field");
            String value = item.getString("value");
            if (item.getString("logic").equals(SystemAuthLogicTypeEnum.EQUALS.getValue())) {
                if (params.containsKey(field) && null != params.get(field) && value.equals(params.get(field).toString())) {
                    num++;
                }
                continue;
            }

            if (item.getString("logic").equals(SystemAuthLogicTypeEnum.UNEQUALS.getValue())) {
                if (!params.containsKey(field) || null == params.get(field) || !value.equals(params.get(field).toString())) {
                    num++;
                }
                continue;
            }

            if (item.getString("logic").equals(SystemAuthLogicTypeEnum.DAYU.getValue())) {
                  if(!params.containsKey(field)||null == params.get(field)){
                     continue;
                  }
                  if(Integer.valueOf(params.get(field).toString())>Integer.valueOf(value)){
                      num++;
                  }
                continue;
            }

            if (item.getString("logic").equals(SystemAuthLogicTypeEnum.XIAOYU.getValue())) {
                if(!params.containsKey(field)||null == params.get(field)){
                    continue;
                }
                if(Integer.valueOf(params.get(field).toString())<Integer.valueOf(value)){
                    num++;
                }
                continue;
            }

        }

        if (num == andRows.size()&&num!=0) {
            needAuth = true;
        }

        for (SqlRow item : orRows) {
            String field = item.getString("field");
            String value = item.getString("value");
            if (item.getString("logic").equals(SystemAuthLogicTypeEnum.EQUALS.getValue())) {
                if (params.containsKey(field) && null != params.get(field) && value.equals(params.get(field).toString())) {
                    needAuth = true;
                    break;
                }
            }
            if (item.getString("logic").equals(SystemAuthLogicTypeEnum.UNEQUALS.getValue())) {
                if (!params.containsKey(field) || null == params.get(field) || !value.equals(params.get(field).toString())) {
                    needAuth = true;
                    break;
                }
            }
            if (item.getString("logic").equals(SystemAuthLogicTypeEnum.DAYU.getValue())) {
                if(!params.containsKey(field)||null == params.get(field)){
                    continue;
                }
                if(Integer.valueOf(params.get(field).toString())>Integer.valueOf(value)){
                    needAuth = true;
                    break;
                }
                continue;
            }

            if (item.getString("logic").equals(SystemAuthLogicTypeEnum.XIAOYU.getValue())) {
                if(!params.containsKey(field)||null == params.get(field)){
                    continue;
                }
                if(Integer.valueOf(params.get(field).toString())<Integer.valueOf(value)){
                    needAuth = true;
                    break;
                }
                continue;
            }
        }

        if (!needAuth) {
            return;
        }

        String roleids[] = SysUtil.getSysUserParams().get("roleids").toString().split(",");
        needAuth=checkNeedAuth(roleids,server,params);
        if(!needAuth){
            return;
        }


        //授权人员输入账号密码后，重新发起请求，需要校验该用户是否有审批的权限
        if (!Tools.isEmptyObjOrString(params.get("auth-role-check-loginname")) && !Tools.isEmptyObjOrString(params.get("auth-role-check-passwd"))) {
            SqlRow user = loginService.findUser(params.get("auth-role-check-loginname").toString());
            if (user == null) {
                throw new Exception("账号不存在");
            }

            if (!user.get("userstatus").equals("N")) {
                throw new Exception("用户已禁用");
            }
            boolean re = loginService.checkPassword(user, params.get("auth-role-check-passwd").toString());
            if(!re){
                throw new Exception("账号密码错误");
            }

            List<SqlRow> userRoles=loginDao.findUserRoles(user.getString("userid"));
            List<String> roleidList=new LinkedList<>();
            for(SqlRow temp:userRoles){
                 roleidList.add(temp.getString("roleid"));
            }
            needAuth=checkNeedAuth(Tools.listJoin(roleidList,",").split(","),server,params);
            if(needAuth){
                throw new Exception("权限不足");
            }else{
                return;
            }
        }

        if (needAuth) {
            throw new Exception("需要角色授权");
        }

    }

    private boolean checkNeedAuth(String[] roleids,String server,Map<String,Object> params){

        int num=0;
        List<SqlRow> andRows = new LinkedList<>();
        List<SqlRow> orRows = new LinkedList<>();

        boolean needAuth=true;

        for (String roleid : roleids) {
            List<SqlRow> authRoleChecks = GraphQLAnnotationImpl.authRoleCheckMap.get(roleid + "-" + server);
            if (null == authRoleChecks || authRoleChecks.size() == 0) {
                continue;
            }
            andRows.clear();
            orRows.clear();
            for (SqlRow item : authRoleChecks) {
                if (item.getString("opjoin").equals(SystemAuthOpjoinTypeEnum.OR.getValue())) {
                    orRows.add(item);
                    continue;
                }
                if (item.getString("opjoin").equals(SystemAuthOpjoinTypeEnum.AND.getValue())) {
                    andRows.add(item);
                    continue;
                }
            }

            num = 0;

            for (SqlRow item : andRows) {
                String field = item.getString("field");
                String value = item.getString("value");
                if (item.getString("logic").equals(SystemAuthLogicTypeEnum.EQUALS.getValue())) {
                    if (params.containsKey(field) && null != params.get(field) && value.equals(params.get(field).toString())) {
                        num++;
                    }
                    continue;
                }
                if (item.getString("logic").equals(SystemAuthLogicTypeEnum.UNEQUALS.getValue())) {
                    if (!params.containsKey(field) || null == params.get(field) || !value.equals(params.get(field).toString())) {
                        num++;
                    }
                    continue;
                }

                if (item.getString("logic").equals(SystemAuthLogicTypeEnum.DAYU.getValue())) {
                    if(!params.containsKey(field)||null == params.get(field)){
                        continue;
                    }
                    if(Integer.valueOf(params.get(field).toString())>Integer.valueOf(value)){
                        num++;
                    }
                    continue;
                }

                if (item.getString("logic").equals(SystemAuthLogicTypeEnum.XIAOYU.getValue())) {
                    if(!params.containsKey(field)||null == params.get(field)){
                        continue;
                    }
                    if(Integer.valueOf(params.get(field).toString())<Integer.valueOf(value)){
                        num++;
                    }
                    continue;
                }
            }

            if (num == andRows.size()&&num!=0) {
                needAuth = false;
                break;
            }

            for (SqlRow item : orRows) {
                String field = item.getString("field");
                String value = item.getString("value");
                if (item.getString("logic").equals(SystemAuthLogicTypeEnum.EQUALS.getValue())) {
                    if (params.containsKey(field) && null != params.get(field) && value.equals(params.get(field).toString())) {
                        needAuth = false;
                        break;
                    }
                }
                if (item.getString("logic").equals(SystemAuthLogicTypeEnum.UNEQUALS.getValue())) {
                    if (!params.containsKey(field) || null == params.get(field) || !value.equals(params.get(field).toString())) {
                        needAuth = false;
                        break;
                    }
                }
                if (item.getString("logic").equals(SystemAuthLogicTypeEnum.DAYU.getValue())) {
                    if(!params.containsKey(field)||null == params.get(field)){
                        continue;
                    }
                    if(Integer.valueOf(params.get(field).toString())>Integer.valueOf(value)){
                        needAuth = true;
                        break;
                    }
                    continue;
                }

                if (item.getString("logic").equals(SystemAuthLogicTypeEnum.XIAOYU.getValue())) {
                    if(!params.containsKey(field)||null == params.get(field)){
                        continue;
                    }
                    if(Integer.valueOf(params.get(field).toString())<Integer.valueOf(value)){
                        needAuth = true;
                        break;
                    }
                    continue;
                }
            }

        }
        return needAuth;
    }

	private boolean isSuperuserUserId(String userId) throws Exception {
		List<SqlRow> userRoles = loginDao.findUserRoles(userId);
		if (userRoles == null || userRoles.size() == 0) {
			return false;
		}

		for (SqlRow userRole : userRoles) {
			String roleId = userRole.getString("roleid");
			if (UserConstants.SUPER_ROLE_ID.equals(roleId)) {
				return true;
			}
		}

		return false;
	}

	private Set<String> getUserServerSet(String userId) throws Exception {
		List<SqlRow> servers = loginDao.findRoleServers(userId);
		if (servers == null || servers.size() == 0) {
			return Collections.EMPTY_SET;
		}

		Set<String> userServerSet = Sets.newHashSetWithExpectedSize(servers.size());
		for (SqlRow server : servers) {
			userServerSet.add(server.getString("server"));
		}

		return userServerSet;
	}

    public Object requestPostJson(String appName, String url, Map<String, Object> params) {
        // headers
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        requestHeaders.add("Authorization", Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
        // HttpEntity
        HttpEntity<Map> requestEntity = new HttpEntity<Map>(params, requestHeaders);

        Object body = restTemplate.postForEntity("http://" + appName + url, requestEntity, Object.class).getBody();

		return body;
	}


	public Object requestPostJson(String appName, String url, String params) {
        // headers
        HttpHeaders requestHeaders = new HttpHeaders();
        // headers
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        requestHeaders.add("Authorization", Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
        // HttpEntity
        HttpEntity<String> requestEntity = new HttpEntity<String>(params, requestHeaders);
        Object body = null;
        try {
            //ResponseEntity entity = restTemplate.postForEntity("http://" + appName + url, requestEntity, Object.class);
            //body = entity.getBody();
            body = restTemplate.postForEntity("http://" + appName + url, requestEntity, Object.class).getBody();
        }catch(Exception e){
            if(e instanceof HttpStatusCodeException){
                log.error("请求异常，异常原因为{}",((HttpStatusCodeException) e).getResponseBodyAsString());
            }
            e.printStackTrace();
            StackTraceElement stackTraceElement= e.getStackTrace()[0];
            log.error("系统出错，错误信息:"+e.toString()+" at "+stackTraceElement.getClassName()+"."+stackTraceElement.getMethodName()+":"+stackTraceElement.getLineNumber());
        }

        return body;
    }

    public Object requestPostForm(String appName, String url, Map<String, Object> params) {
        // headers
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        requestHeaders.add("Authorization", Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));

		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();

		if (params != null && !params.isEmpty()) {
			Set<String> keys = params.keySet();
			for (String key : keys) {
				postParameters.add(key, params.get(key));
			}
		}

        // HttpEntity
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, requestHeaders);

        Object body = restTemplate.postForEntity("http://" + appName + url, requestEntity, Object.class).getBody();

		return body;
	}

    public Object requestGetTransmit(String url,String server, Map<String, Object> params) throws Exception {
        checkServer(server);
        return HttpUtil.sendGet(url,params);
    }

    public Object requestPostTransmitForm(String url,String server, Map<String, Object> params) throws Exception {
        checkServer(server);
        return HttpUtil.sendPost(url,params);
    }
    public Object requestPostTransmitJson(String url,String server, String jsonParams) throws Exception {
        checkServer(server);
        return HttpUtil.jsonPost(url,jsonParams);
    }

    public void requestDownloadOld(String appName, String url, Map<String, Object> params, HttpServletResponse response)
            throws Exception {
        // headers
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        requestHeaders.add("Authorization", SysUtil.getSysUserParamValue("sys_user_userid").toString());

        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                postParameters.add(key, params.get(key));
            }
        }
        // HttpEntity
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, requestHeaders);
        String downloadUrl = "http://" + appName + url;
        log.info("通用导出，文件下载请求URL:" + downloadUrl);
        // ========== 2. 核心：用execute方法，手动处理请求/响应（绕过转换器限制） ==========
        restTemplate.execute(
            downloadUrl,
            HttpMethod.POST,
            // RequestCallback：复用原始requestEntity，Spring自动处理请求体（和postForEntity一致）
            request -> {
                // 复制原始请求头（和postForEntity逻辑完全一致）
                HttpHeaders headers = request.getHeaders();
                headers.putAll(requestEntity.getHeaders());
                // Spring自动将MultiValueMap转换为FORM_URLENCODED请求体（无需手动处理）
                FormHttpMessageConverter formConverter = new FormHttpMessageConverter();
                formConverter.setCharset(requestHeaders.getContentType().getCharset() != null ?
                        requestHeaders.getContentType().getCharset():StandardCharsets.UTF_8); // 适配UTF-8编码
                formConverter.write(requestEntity.getBody(), MediaType.APPLICATION_FORM_URLENCODED,request);
            },
            // ResponseExtractor：手动获取原生流，避免转换器问题
            responseExtractor -> {
                // 校验远程响应状态
                if (!responseExtractor.getStatusCode().is2xxSuccessful()) {
                    log.error("远程服务返回异常状态码：{}", responseExtractor.getStatusCode());
                    throw new RuntimeException("文件下载失败：远程服务返回" + responseExtractor.getStatusCode());
                }

                HttpHeaders remoteHeaders = responseExtractor.getHeaders();
                response.setCharacterEncoding("UTF-8");
                // 适配Excel下载的MIME类型和文件名编码
                if (url.equals("/excel/download.json")) {
                    String fileName = URLEncoder.encode("download.xlsx", StandardCharsets.UTF_8.name());
                    response.setHeader("Access-Control-Expose-Headers", "filename,Content-Disposition");
                    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\";filename*=UTF-8''" + fileName);
                    response.setHeader("filename", fileName);
                } else {
                    for (String key : remoteHeaders.keySet()) {
                        if (key.equals("err")) {
                            response.setHeader("Access-Control-Expose-Headers", "err");
                            response.setHeader("err", Tools.obj2Str(remoteHeaders.get(key).get(0)));
                        }
                        if (!key.equals("Content-Length")) {
                            response.setHeader(key, Tools.obj2Str(remoteHeaders.get(key).get(0)));
                        }
                    }
                }
                // ========== 4. 适配大文件+办公域网络的响应配置 ==========
                // 关键：延长连接超时、启用分块传输、关闭无效缓存
                response.setContentType(Optional.ofNullable(remoteHeaders.getContentType()).
                        map(MediaType::toString).orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE));
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Connection", "keep-alive");
                response.setHeader("Keep-Alive", "timeout=60, max=100"); // 延长连接存活时间
                response.setHeader("Transfer-Encoding", "chunked"); // 大文件分块传输
                response.setBufferSize(16 * 1024); // 优化缓冲区大小（16KB）

                // ========== 5. 流式传输核心（直接拿原生InputStream，无转换器） ==========客户端断开异常捕获
                long totalBytes = 0;
                int bufferSize = 16 * 1024;
                try (
                    // 修复点1：将responseExtractor.getBody()强制转换为InputStream（RestTemplate返回的是InputStream）
                    InputStream inputStream = new BufferedInputStream((InputStream) responseExtractor.getBody());
                    BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream(), bufferSize)
                ) {
                    byte[] buffer = new byte[bufferSize];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        if (bytesRead > 0) {
                            try {
                                outputStream.write(buffer, 0, bytesRead);
                                totalBytes += bytesRead;
                                if (totalBytes % (8 * bufferSize) == 0) {
                                    outputStream.flush();
                                }
                            } catch (IOException e) {
                                if (e.getMessage().contains("Connection reset by peer") ||
                                        e.getMessage().contains("Broken pipe")) {
                                    log.warn("客户端已断开下载连接，已传：{}KB", totalBytes / 1024);
                                } else {
                                    log.error("写入输出流失败", e);
                                }
                                break; // 退出循环
                            }
                        }
                    }

                    if (totalBytes > 0) {
                        log.info("文件传输完成，总字节数：{}MB", new BigDecimal(totalBytes).divide(new BigDecimal(1024 * 1024),4,BigDecimal.ROUND_HALF_UP) );
                        outputStream.flush();
                        log.info("文件传输完成，输出流刷新完成。");
                    }

                    if (totalBytes == 0) {
                        log.warn("远程服务返回空文件，URL: {}", downloadUrl);
                        response.setHeader("err", "下载失败：文件内容为空");
                    }

                } catch (ClientAbortException e) {
                    // 客户端断开属于预期异常，仅记录WARN
                    log.warn("客户端提前断开下载连接（用户取消/超时），已传：{}KB，异常：{}", totalBytes / 1024, e.getMessage());
                } catch (IOException e) {
                    if (e.getMessage().contains("Connection reset by peer") ||
                            e.getMessage().contains("Broken pipe")) {
                        log.warn("文件传输时客户端连接重置，已传：{}KB，异常：{}", totalBytes / 1024, e.getMessage());
                    } else {
                        log.error("文件流式传输失败（非客户端断开）", e);
                        throw new RuntimeException("文件下载失败：" + e.getMessage(), e);
                    }
                } catch (Exception e) {
                    log.error("文件下载过程中发生未知异常", e);
                    throw new RuntimeException("文件下载失败：" + e.getMessage(), e);
                }
                return null;
            }
        );
    }

    /**
     * 修复的 requestDownload 方法，增强流式传输的健壮性
     */
    public void requestDownload(String appName, String url, Map<String, Object> params, HttpServletResponse response)
            throws Exception {
        // 1. 构建请求头和参数
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        requestHeaders.add("Authorization", SysUtil.getSysUserParamValue("sys_user_userid").toString());

        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                postParameters.add(key, params.get(key));
            }
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, requestHeaders);
        String downloadUrl = "http://" + appName + url;
        log.info("通用导出，文件下载请求URL: {}", downloadUrl);

        AtomicBoolean isClientDisconnected = new AtomicBoolean(false);
        AtomicLong totalBytesTransferred = new AtomicLong();

        try {
            // 2. 使用 execute 方法进行流式传输
            restTemplate.execute(
                    downloadUrl,
                    HttpMethod.POST,
                    request -> {
                        // 复制请求头
                        HttpHeaders headers = request.getHeaders();
                        headers.putAll(requestEntity.getHeaders());

                        // 写入请求体
                        FormHttpMessageConverter formConverter = new FormHttpMessageConverter();
                        formConverter.setCharset(StandardCharsets.UTF_8);
                        formConverter.write(requestEntity.getBody(), MediaType.APPLICATION_FORM_URLENCODED, request);
                    },
                    responseExtractor -> {
                        // 检查远程服务响应状态
                        if (!responseExtractor.getStatusCode().is2xxSuccessful()) {
                            log.error("远程服务返回异常状态码：{}", responseExtractor.getStatusCode());
                            throw new RuntimeException("文件下载失败：远程服务返回" + responseExtractor.getStatusCode());
                        }

                        HttpHeaders remoteHeaders = responseExtractor.getHeaders();
                        response.setCharacterEncoding("UTF-8");

                        // 【重要修改】移除 Transfer-Encoding: chunked 头，让Servlet容器处理
                        // 不设置分块传输编码，避免Nginx解析问题

                        // 设置响应头
                        for (String key : remoteHeaders.keySet()) {
                            if (!key.equalsIgnoreCase("Transfer-Encoding") && !key.equalsIgnoreCase("Content-Length")) {
                                response.setHeader(key, Tools.obj2Str(remoteHeaders.get(key).get(0)));
                            }
                        }

                        // 设置Excel下载相关头
                        if (url.equals("/excel/download.json")) {
                            String fileName = URLEncoder.encode("download.xlsx", StandardCharsets.UTF_8.name());
                            response.setHeader("Access-Control-Expose-Headers", "filename,Content-Disposition");
                            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                            response.setHeader("Content-Disposition",
                                    "attachment;filename=\"" + fileName + "\";filename*=UTF-8''" + fileName);
                            response.setHeader("filename", fileName);

                            // 【新增】显式设置 Content-Type，避免Nginx猜测
                            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                        } else {
                            for (String key : remoteHeaders.keySet()) {
                                if (key.equals("err")) {
                                    response.setHeader("Access-Control-Expose-Headers", "err");
                                    response.setHeader("err", Tools.obj2Str(remoteHeaders.get(key).get(0)));
                                }
                                if (!key.equals("Content-Length")) {
                                    response.setHeader(key, Tools.obj2Str(remoteHeaders.get(key).get(0)));
                                }
                            }
                        }

                        // 【重要修改】设置合理的缓冲区大小
                        response.setBufferSize(64 * 1024); // 64KB

                        // 【新增】禁用响应压缩，避免分块问题
                        response.setHeader("Content-Encoding", "identity");
                        int bufferSize = 64 * 1024;
                        // 4. 流式传输
                        try (
                                InputStream inputStream = new BufferedInputStream((InputStream) responseExtractor.getBody());
                                BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream(), 64 * 1024)
                        ) {
                            byte[] buffer = new byte[bufferSize];
                            int bytesRead;
                            long lastFlushTime = System.currentTimeMillis();

                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                // 【新增】定期检查客户端连接状态
                                if (isClientDisconnected(response)) {
                                    log.warn("检测到客户端连接断开，停止传输");
                                    isClientDisconnected.set(true);
                                    break;
                                }

                                if (bytesRead > 0) {
                                    try {
                                        outputStream.write(buffer, 0, bytesRead);
                                        totalBytesTransferred.addAndGet(bytesRead);

                                        // 定期刷新，但不要太频繁
                                        long currentTime = System.currentTimeMillis();
                                        if (currentTime - lastFlushTime > 1000) { // 每1秒刷新一次
                                            outputStream.flush();
                                            lastFlushTime = currentTime;
                                        }

                                        // 【新增】每10MB记录一次进度
                                        if (totalBytesTransferred.get() % (10 * 1024 * 1024) == 0) {
                                            log.info("已传输: {} MB", totalBytesTransferred.get() / (1024 * 1024));
                                        }
                                    } catch (IOException e) {
                                        // 检查是否是连接断开
                                        if (e.getMessage().contains("Broken pipe") ||
                                                e.getMessage().contains("Connection reset") ||
                                                e instanceof ClientAbortException) {
                                            log.warn("写入时客户端连接断开: {}", e.getMessage());
                                            isClientDisconnected.set(true);
                                            break;
                                        }
                                        throw e;
                                    }
                                }
                            }

                            // 最终刷新
                            if (!isClientDisconnected.get() && totalBytesTransferred.get() > 0) {
                                outputStream.flush();
                                log.info("文件传输完成，总字节数: {} MB", new BigDecimal(totalBytesTransferred.get()).divide(new BigDecimal(1024 * 1024),4,BigDecimal.ROUND_HALF_UP) );
                            }

                        } catch (ClientAbortException e) {
                            log.warn("客户端主动断开连接，已传输: {} MB", new BigDecimal(totalBytesTransferred.get()).divide(new BigDecimal(1024 * 1024),4,BigDecimal.ROUND_HALF_UP) );
                            isClientDisconnected.set(true);
                        } catch (IOException e) {
                            if (e.getMessage().contains("Broken pipe") || e.getMessage().contains("Connection reset")) {
                                log.warn("连接重置，已传输: {} MB", new BigDecimal(totalBytesTransferred.get()).divide(new BigDecimal(1024 * 1024),4,BigDecimal.ROUND_HALF_UP) );
                                isClientDisconnected.set(true);
                            } else {
                                log.error("文件流式传输失败", e);
                                throw new RuntimeException("文件下载失败: " + e.getMessage(), e);
                            }
                        }

                        return null;
                    }
            );
        } catch (Exception e) {
            if (!isClientDisconnected.get()) {
                log.error("请求下载异常", e);
                throw e;
            } else {
                log.warn("客户端已断开连接，忽略异常: {}", e.getMessage());
            }
        } finally {
            // 【新增】记录最终传输状态
            if (isClientDisconnected.get()) {
                log.warn("下载被客户端中断，总传输: {} MB", totalBytesTransferred.get() / (1024 * 1024));
            } else if (totalBytesTransferred.get() == 0) {
                log.warn("未传输任何数据，可能请求失败");
            }
        }
    }

    /**
     * 检查客户端连接状态
     */
    private boolean isClientDisconnected(HttpServletResponse response) {
        try {
            // 尝试检查响应状态
            if (response.isCommitted()) {
                // 响应已提交，但可能客户端已断开
                // 尝试写入一个空字节测试连接
                try {
                    response.getOutputStream().write(new byte[0]);
                    response.getOutputStream().flush();
                    return false;
                } catch (IOException e) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public void requestDownloadJson(String appName, String url, Map<String, Object> params,
                                    HttpServletResponse response) throws Exception {
        // headers
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.add("Authorization", SysUtil.getSysUserParamValue("sys_user_userid").toString());

        HttpEntity<Map> requestEntity = new HttpEntity<Map>(params, requestHeaders);

        String downloadUrl = "http://" + appName + url;

        log.info("导出url:" + downloadUrl);

        ResponseEntity<byte[]> responseEntity = restTemplate.postForEntity(downloadUrl, requestEntity, byte[].class);

        try {
            log.info("导出字节长度:" + responseEntity.getBody().length);
        } catch (Exception e) {

        }

        HttpHeaders httpHeaders = responseEntity.getHeaders();
        Set<String> keys = httpHeaders.keySet();
        for (String key : keys) {
            if (key.equals("err")) {
                response.setHeader("Access-Control-Expose-Headers", "err");
                response.setHeader("err", Tools.obj2Str(httpHeaders.get(key).get(0)));
            }
            response.setHeader(key, Tools.obj2Str(httpHeaders.get(key).get(0)));
        }

		OutputStream out = null;
		try {
			out = response.getOutputStream();
			out.write(responseEntity.getBody());
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

	public Object requestUpload(String appName, String url, Map<String, Object> params, MultipartFile multipartFile)
			throws Exception {
		// headers
		HttpHeaders requestHeaders = new HttpHeaders();
		try {
			requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
		}catch (Exception e){
			log.info("设置内容类型失败!");
		}
		try{
			requestHeaders.add("Authorization", SysUtil.getSysUserParamValue("sys_user_userid").toString());
		}catch (Exception e){
			log.info("获取用户唯一标识失败!");
		}


		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();

		if (params != null && !params.isEmpty()) {
			Set<String> keys = params.keySet();
			for (String key : keys) {
				postParameters.add(key, params.get(key));
			}
		}

		ByteArrayResource is = new ByteArrayResource(multipartFile.getBytes()) {
			@Override
			public String getFilename() {
				return multipartFile.getOriginalFilename();
			}
		}; // 此处从multipartFile获取byte[],如果是上传本地文件可以使用io获取byte[]
		postParameters.add("file", is);

		// HttpEntity
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, requestHeaders);

		Object body = restTemplate.postForEntity("http://" + appName + url, requestEntity, Object.class).getBody();

		return body;
	}

    public void requestUploadDownLoad(String appName, String url, Map<String, Object> params, MultipartFile multipartFile, HttpServletResponse response)
            throws Exception {
        // headers
        HttpHeaders requestHeaders = new HttpHeaders();
        try {
            requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        }catch (Exception e){
            log.info("设置内容类型失败!");
        }
        try{
            requestHeaders.add("Authorization", SysUtil.getSysUserParamValue("sys_user_userid").toString());
        }catch (Exception e){
            log.info("获取用户唯一标识失败!");
        }


        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();

        if (params != null && !params.isEmpty()) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                postParameters.add(key, params.get(key));
            }
        }
        log.info("下载请求参数: {}", postParameters);
        ByteArrayResource is = new ByteArrayResource(multipartFile.getBytes()) {
            @Override
            public String getFilename() {
                return multipartFile.getOriginalFilename();
            }
        }; // 此处从multipartFile获取byte[],如果是上传本地文件可以使用io获取byte[]
        postParameters.add("file", is);

        // HttpEntity
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, requestHeaders);
        ResponseEntity<byte[]> responseEntity = restTemplate.postForEntity("http://" + appName + url, requestEntity, byte[].class);

        try{
            log.info("导出字节长度:" + responseEntity.getBody().length);
        }catch (Exception e){

        }

        HttpHeaders httpHeaders = responseEntity.getHeaders();
        Set<String> keys = httpHeaders.keySet();
        for (String key : keys) {
            if (key.equals("err")) {
                response.setHeader("Access-Control-Expose-Headers", "err");
                response.setHeader("err", Tools.obj2Str(httpHeaders.get(key).get(0)));
            }
            response.setHeader(key, Tools.obj2Str(httpHeaders.get(key).get(0)));
        }

        OutputStream out = null;
        try {
            out = response.getOutputStream();
            out.write(responseEntity.getBody());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    public Object requestUploadFiles(String appName, String url, Map<String, Object> params,
                                     MultipartFile[] multipartFiles) throws Exception {
        // headers
        HttpHeaders requestHeaders = new HttpHeaders();
        try {
            requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        } catch (Exception e) {
            log.info("设置内容类型失败!");
        }
        try {
            requestHeaders.add("Authorization", SysUtil.getSysUserParamValue("sys_user_userid").toString());
        } catch (Exception e) {
            log.info("获取用户唯一标识失败!");
        }

		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();

		if (params != null && !params.isEmpty()) {
			Set<String> keys = params.keySet();
			for (String key : keys) {
				postParameters.add(key, params.get(key));
			}
		}
		List<ByteArrayResource> byteArrayResourceList=new ArrayList<>();
		for(MultipartFile m:multipartFiles){
			ByteArrayResource is = new ByteArrayResource(m.getBytes()) {
				@Override
				public String getFilename() {
					return m.getOriginalFilename();
				}
			}; // 此处从multipartFile获取byte[],如果是上传本地文件可以使用io获取byte[]
			postParameters.add("file", is);
		}



		// HttpEntity
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, requestHeaders);

		Object body = restTemplate.postForEntity("http://" + appName + url, requestEntity, Object.class).getBody();

        return body;
    }

    /**
     * 数据流请求（当数据量大的时候，前端将请求的json数据封装为一个blob类型，以文件形式上传）
     * 读取文件流后，将json字符串解析后，再传递到后端服务进行进一步的处理
     * @param appName
     * @param url
     * @param params
     * @param multipartFiles
     * @return
     * @throws Exception
     */
    public Object requestDataStream(String appName, String url, Map<String, Object> params,
                                     MultipartFile[] multipartFiles) throws Exception {
        // headers
        HttpHeaders requestHeaders = new HttpHeaders();
        try {
            requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        } catch (Exception e) {
            log.info("设置内容类型失败!");
        }
        try {
            requestHeaders.add("Authorization", SysUtil.getSysUserParamValue("sys_user_userid").toString());
        } catch (Exception e) {
            log.info("获取用户唯一标识失败!");
        }

        String jsonData = readDataStream(multipartFiles[0]); //读取json数据

//        Object body = restTemplate.postForEntity("http://" + appName + url, requestEntity, Object.class).getBody();

        return null; //测试
    }

    /**
     *  读取文件中的数据流（前端传递的blob类型文件）
     * @param multipartFile
     * @return
     * @throws Exception
     */
    private String readDataStream(@RequestParam(value = "file") MultipartFile multipartFile)  throws Exception{
        File file = null;
        StringBuilder dataBuilder = new StringBuilder();
        try {
            if (multipartFile == null) {
                throw new Exception("上传文件为空");
            }
            // 转换File
            File tmpFile = FileUtil.multipartFileToFile(multipartFile);
            file = new File(tmpFile.getAbsolutePath());
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String strLine;
            while((strLine = reader.readLine()) != null){
                dataBuilder.append(strLine);
            }
            reader.close();
        } catch (Exception ex) {
            throw ex;
        } finally {
            FileUtil.delFile(file);
        }

        String jsonData = dataBuilder.toString();
        return jsonData.substring(jsonData.indexOf("["),jsonData.lastIndexOf("]")+1);
    }

    public Object requestUploadTransmit(String url, Map<String, Object> params, MultipartFile file , Map<String,Object> headers, String encode)
            throws Exception {
        return HttpUtil.sendPostFormMultipart(url,params,file,headers,encode);
    }

    public Object requestUploadTransmitFiles(String url, Map<String, Object> params, MultipartFile[] files , Map<String,Object> headers, String encode)
            throws Exception {
        return HttpUtil.sendPostFormMultipart(url,params,files,headers,encode);
    }
}
