package com.kayak.login.action;

import com.kayak.cache.util.CacheUtil;
import com.kayak.core.action.BaseController;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.IPCheckUtils;
import com.kayak.core.util.IpMatcher;
import com.kayak.core.util.Tools;
import com.kayak.graphql.autoconfigure.GraphQLAnnotationImpl;
import com.kayak.login.dao.LoginDao;
import com.kayak.login.filter.JwtFilter;
import com.kayak.login.filter.JwtServer;
import com.kayak.login.service.LoginService;
import com.kayak.login.service.SsoLoginService;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RefreshScope
@Controller
public class LoginAction extends BaseController {

	@Autowired
	private LoginDao loginDao;

	@Autowired
	private LoginService loginService;
	@Autowired
	private SsoLoginService ssoLoginService;
	//统一认证登录地址
	@Value("${uias.uiasUrl}")
	private String uiasUrl;
	//统一认证跳转本系统登录uri
	@Value("${uias.targetUri:/jwt/login.json}")
	private String ssoTargetURI;

	@Value("${uias.sourceUri:#/main/desktop}")
	private String sourceUri;

	/**
	 * 统一认证登录
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/jwt/loginAuth.json")
	public void  jwtloginAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> params = RequestSupport.getParameters();
		String regChkflg = getRegCheckFlag(request);
		String localObject =  request.getRequestURL().toString();
		String contextPath = request.getContextPath();
		if ((localObject != null) && (localObject.indexOf(contextPath) > -1)) {
			StringBuilder sb = new StringBuilder();
			//重定向地址
			sb.append(uiasUrl);
			//前端IP端口
			String sourceIp = request.getHeader("referer");
			//网关IP端口
			String hostIp = request.getHeader("host");
			log.info("redirectToLoginPage sourceIp：{}", sourceIp);
			sb.append("?ssotarget=").append("http://").append(hostIp).append(ssoTargetURI);
//			sb.append("?sourceUrl=").append(sourceIp);
			sb.append("?sourceUrl=").append(sourceIp).append("-regChkflg="+regChkflg);
			log.info("regxChekFlag:"+sb.toString());
			//重定向到统一认证登录系统
			response.sendRedirect(sb.toString());
		}
	}
	@RequestMapping(value = "/jwt/login.json")
	public @ResponseBody String jwtLogin(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> params = RequestSupport.getParameters();

			try {
				//统一登陆处理
				Boolean sso_login_flag= ssoLoginService.ssoLoginProcess(request,params);
				// 系统原登录流程
				String loginUser = (String) params.get("username");
				Boolean pass = Boolean.valueOf(String.valueOf(params.get("pass")));

				String password = (String) params.get("password");
				
				if (Tools.strIsEmpty(loginUser)) {
					return updateFailure("用户名不能为空");
				}

				SqlRow user = loginService.findUser(loginUser);
				if (user == null) {
					return updateFailure("用户名或密码错误");
				}

				if (!user.get("userstatus").equals("N")) {
					return updateFailure("用户已被禁用");
				}

				int numberOfPasswords=Integer.valueOf(SysUtil.getSystemParams().get("10010").getString("paravalue"));

				int passwordExpiration=Integer.valueOf(SysUtil.getSystemParams().get("10012").getString("paravalue"));

				int waithours=Integer.valueOf(SysUtil.getSystemParams().get("10011").getString("paravalue"));

				int waitTime=waithours*60*60*1000;
				List<String> uncheckUsers = loginService.getUncheckUser();
				String isInitialPwd = user.getString("is_initial_pwd");
				Date pwdsetdate=Tools.getDateFromString("yyyyMMdd",user.getString("pwdsetdate"));
				Date currentdate=Tools.getDateFromString("yyyyMMdd",Tools.getCurrentDate());
				if(passwordExpiration>0){
					if(null==pwdsetdate||Tools.getMonthDiff(currentdate,pwdsetdate)>passwordExpiration) {
						Map<String,Object> returData = new HashMap<String,Object>();
						returData.put("reset", true);
						return updateFailure("用户密码已失效，请重置密码！",returData);
					}
					
				}

				int pwderrtimes = user.getInteger("pwderrtimes");
				String err=waitTime==0?"您的密码已经错误超过"+numberOfPasswords+"次，不可登录":"您的密码已经错误超过"+numberOfPasswords+"次，请"+waithours+"小时再试尝试";
				if (numberOfPasswords>0&&pwderrtimes>=numberOfPasswords) {

					if(waitTime==0){
						return updateFailure(err);
					}

					Date errDate = Tools.getDateFromString("yyyyMMdd HHmmss", user.getString("pwderrlockdt"));
					long nowDate = System.currentTimeMillis();
					if (waitTime>0&&null!=errDate&&(errDate.getTime() + waitTime) >= nowDate) {
						return updateFailure(err);
					} else {// 清除错误次数
						pwderrtimes = 0;
						loginService.resetUserLock(pwderrtimes, loginUser);
					}
				}
				boolean re=true;

				if(sso_login_flag){
					//统一认证不校验密码
					pass=true;
				}else{
					// 账号认证
					re= loginService.checkPassword(user, password);
				}

				
				if (re) {// 认证成功，生成token
					if (!pass) {
						Map<String,Object> returData = new HashMap<>();
						returData.put("reset", true);
						return updateFailure("密码为弱口令，请重置为强密码！",returData);
					}
					
					Map<String,Object> returData = new HashMap<String,Object>();
					returData.put("reset", true);
					if(INITIALTYPENUM.IS_INITIAL_PWD.getVal().equals(isInitialPwd)&&!uncheckUsers.contains(loginUser)) {
						 return updateFailure("当前用户为新用户,请修改初始密码！",returData);
					}
					//重置密码错误次数
					pwderrtimes = 0;
					loginService.resetUserLock(pwderrtimes, loginUser);

					// 查询用户权限
					String userid = user.getString("userid");
					List<SqlRow> servers = loginDao.findRoleServers(userid);

					StringBuilder serverBuffer = new StringBuilder();
					boolean flag = false;
					for (SqlRow server : servers) {
						String serverStr = server.getString("server");
						String modelStr = server.getString("model_name");

						if (!flag) {
							if (StringUtils.isNotEmpty(serverStr) && serverStr.contains("-")) {
								serverBuffer.append(modelStr);
								serverBuffer.append(".");
								serverBuffer.append(serverStr.split("-")[1]);
								flag = true;
							}
						} else {
							if (StringUtils.isNotEmpty(serverStr) && serverStr.contains("-")) {
								serverBuffer.append(",");
								serverBuffer.append(modelStr);
								serverBuffer.append(".");
								serverBuffer.append(serverStr.split("-")[1]);
							}

						}
					}
					List<SqlRow> roleMenu = loginDao.findRoleMenu(userid);

					// 查询服务权限
					Map<String, String> serverMap = new HashMap<String, String>();
					servers.forEach(server -> {
						serverMap.put(server.getString("server"), server.getString("server"));
					});
					user.put("servers", serverMap);

					for (SqlRow menu : roleMenu) {
						String authServer = menu.getString("auth_server");
						if (Tools.isBlank(authServer)) {
							continue;
						}
						String[] serverArr = authServer.split(",");
						for (String server : serverArr) {
							SqlRow sqlRow = GraphQLAnnotationImpl.authMap.get(server);
							if (sqlRow == null) {
								// sys_server_method配置不需要验证，就不用添加到缓存
								continue;
							}
							if (!flag) {
								if(StringUtils.isNotEmpty(server) && server.contains("-")){
									serverBuffer.append(sqlRow.getString("model_name"));
									serverBuffer.append(".");
									serverBuffer.append(server.split("-")[1]);
									flag = true;
								}
							} else {
								if(StringUtils.isNotEmpty(server) && server.contains("-")){
									serverBuffer.append(",");
									serverBuffer.append(sqlRow.getString("model_name"));
									serverBuffer.append(".");
									serverBuffer.append(server.split("-")[1]);
								}
							}
							serverMap.put(server, server);
						}

					}
					serverBuffer.append(",UserInfo.resetPwdCheckOldPwd,UserInfo.updateUserInfo");
					
					// 查询角色
					List<SqlRow> sqlRows = loginDao.findUserRoles(userid);
					String roleids = "";
					for (SqlRow sqlRow : sqlRows) {
						roleids += "," + sqlRow.getString("roleid");
					}
					if (roleids.length() > 0) {
						user.put("roleids", roleids.substring(1));
					}

					JwtFilter.userMap.put(user.getString("employee_no"), user);

					// 生成token
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("userid", userid);
					String data = jsonObject.toString();
					int autoLogoutTime=Integer.valueOf(SysUtil.getSystemParams().get("10008").getString("paravalue"));
					String token = JwtServer.makeToken(new Date(System.currentTimeMillis() + autoLogoutTime*60*1000), data);
					Map<String, Object> reparams = new HashMap<String, Object>();
					reparams.put("token", token);
					reparams.put("username", user.getString("username"));
					reparams.put("userid", userid);
					reparams.put("servers", serverBuffer.toString());
                    reparams.put("gridMaxHeight", SysUtil.getSystemParamsByParaid("10007"));
					reparams.put("roleids", roleids);//用户角色
					JwtFilter.userMap.remove(user.getString("employee_no"));

					addLog(userid);
					//统一认证 需要跳转到主页
					if(sso_login_flag){
						String oriSourceUrl = "";
						String authRegChkflg = "1";
						try{
							oriSourceUrl = params.get("sourceUrl").toString().split("-regChkflg=")[0].trim();
							authRegChkflg = params.get("sourceUrl").toString().split("-regChkflg=")[1].trim();
						}catch (Exception e){
							authRegChkflg = "1";
							log.info("sourceUrl:"+params.get("sourceUrl").toString()+"; "+e.getMessage());
						}
						StringBuffer url=new StringBuffer(oriSourceUrl);
						url.append(sourceUri);
						url.append("?").append("username=").append(user.getString("username"));
						url.append("&").append("userid=").append(userid);
						url.append("&").append("regChkflg=").append(authRegChkflg);
						url.append("&").append("roleids=").append(roleids);
						url.append("&").append("token=").append(token);
						HttpSession session = request.getSession();
						session.setAttribute("userid", userid);
						response.setCharacterEncoding("UTF-8");
						response.sendRedirect(url.toString());
					}
					//非统一认证登录时
					String regChkflg= getRegCheckFlag(request);
					reparams.put("regChkflg",regChkflg);

					return updateSuccess("登录成功", reparams);

				} else {// 认证失败
					if (numberOfPasswords>0&&pwderrtimes >= (numberOfPasswords-1)) {
						loginService.updateUserLock(loginUser, numberOfPasswords, Tools.getStringFromDate("yyyyMMdd HHmmss", new Date()));
						return updateFailure(err);
					} else {
						if(numberOfPasswords>0){
							loginService.updateUserLockTime(loginUser);
						}else{

						}
						return updateFailure("用户名或密码错误");
					}
				}

			} catch (PromptException e) {
				return updateFailure(e.getMessage());
			} catch (Exception e1) {
				log.error(e1.getMessage(), e1);
				return updateFailure("服务繁忙，请稍后尝试");
			}

	}

	
	@RequestMapping(value = "/jwt/resetPwd.json")
	public @ResponseBody String jwtResetPwd() {
			Map<String, Object> params = RequestSupport.getParameters();

			try {
				String loginUser = (String) params.get("username");
				String password = (String) params.get("password");
				String newPassword = (String) params.get("newPassword");
				if (Tools.strIsEmpty(loginUser)) {
					return updateFailure("用户名不能为空");
				}

				SqlRow user = loginService.findUser(loginUser);
				if (user == null) {
					return updateFailure("账号不存在");
				}

				if (!user.get("userstatus").equals("N")) {
					return updateFailure("用户已被禁用");
				}
				int numberOfPasswords=Integer.valueOf(SysUtil.getSystemParams().get("10010").getString("paravalue"));
				int waithours=Integer.valueOf(SysUtil.getSystemParams().get("10011").getString("paravalue"));
				int waitTime=waithours*60*60*1000;
				int pwderrtimes = user.getInteger("pwderrtimes");
				String err=waitTime==0?"您的密码已经错误超过"+numberOfPasswords+"次，不可登录":"您的密码已经错误超过"+numberOfPasswords+"次，请"+waithours+"小时再试尝试";
				if (numberOfPasswords>0&&pwderrtimes>=numberOfPasswords) {

					if(waitTime==0){
						return updateFailure(err);
					}
					Date errDate = Tools.getDateFromString("yyyyMMdd HHmmss", user.getString("pwderrlockdt"));
					long nowDate = System.currentTimeMillis();
					if (waitTime>0&&null!=errDate&&(errDate.getTime() + waitTime) >= nowDate) {
						return updateFailure(err);
					} else {// 清除错误次数
						pwderrtimes = 0;
						loginService.resetUserLock(pwderrtimes, loginUser);
					}
				}

				// 账号认证
				boolean re = loginService.checkPassword(user, password);
				
				if (re) {
					loginService.resetPwd(loginUser,newPassword);		
					return updateSuccess("修改密码成功", null);

				} else {// 认证失败
					if (numberOfPasswords>0&&pwderrtimes >= (numberOfPasswords-1)) {
						loginService.updateUserLock(loginUser, numberOfPasswords, Tools.getStringFromDate("yyyyMMdd HHmmss", new Date()));
						return updateFailure(err);
					} else {
						if(numberOfPasswords>0){
							loginService.updateUserLockTime(loginUser);
						}else{

						}
						return updateFailure("密码错误");
					}
				}

			} catch (PromptException e) {
				return updateFailure(e.getMessage());
			} catch (Exception e1) {
				log.error(e1.getMessage(), e1);
				return updateFailure("服务繁忙，请稍后尝试");
			}
		

	}
	
	@RequestMapping(value = "/getLoginUser.json")
	public @ResponseBody String getLoginUser() {
		try {
			Map<String, Object> sysUserParams = SysUtil.getSysUserParams();
			// 密码脱敏
			sysUserParams.put("passwd", "");
			return updateSuccess(sysUserParams);
		} catch (Exception e) {// 获取返回提示的错误
			return updateFailure(e.getMessage());
		}
	}

	/**
	 * 用户注销
	 */
	@RequestMapping(value = "/logout.json")
	public String logout() {
		// 获取员工编号
		HttpSession session = RequestSupport.getLocalRequest().getSession();
		session.removeAttribute(SysUtil.SYS_USER_PARAMS_SESSION_KEY);
		return "login";
	}

	@RequestMapping(value = "/getLoginConfig.json")
	public @ResponseBody String getLoginConfig() {
		try {
			JSONObject json = new JSONObject();
			json.put("success", true);
			json.put("systemName", CacheUtil.getSystemParam("10000")==null?"产品管理系统":CacheUtil.getSystemParam("10000"));
			json.put("companyName", CacheUtil.getSystemParam("10001"));
			json.put("companyLogo", CacheUtil.getSystemParam("10002"));
			json.put("loginColor", CacheUtil.getSystemParam("10003"));
			return json.toString();
		} catch (Exception e) {// 获取返回提示的错误
			return updateFailure(e.getMessage());
		}
	}
	
	/**
	 *  本系统登录业务逻辑
	 * @param loginUser
	 * @return
	 */
	public String doLoginBusiness(String loginUser) {
		// 查询用户权限
		try {
			SqlRow user = loginService.findUser(loginUser);
			if (user == null) {
				return updateFailure("账号不存在");
			}

			if (!"N".equals(user.getString("userstatus"))) {
				return updateFailure("用户已停用");
			}


			String userid = user.getString("userid");
			List<SqlRow> servers = loginDao.findRoleServers(userid);

			StringBuilder serverBuffer = new StringBuilder();
			boolean flag = false;
			for (SqlRow server : servers) {
				String serverStr = server.getString("server");
				String modelStr = server.getString("model_name");

				if (!flag) {
					if(StringUtils.isNotEmpty(serverStr) && serverStr.contains("-")){
						serverBuffer.append(modelStr);
						serverBuffer.append(".");
						serverBuffer.append(serverStr.split("-")[1]);
						flag = true;
					}
				} else {
					if (StringUtils.isNotEmpty(serverStr) && serverStr.contains("-")) {
						serverBuffer.append(",");
						serverBuffer.append(modelStr);
						serverBuffer.append(".");
						serverBuffer.append(serverStr.split("-")[1]);
					}
				}
			}
			List<SqlRow> roleMenu = loginDao.findRoleMenu(userid);

			// 查询服务权限
			Map<String, String> serverMap = new HashMap<String, String>();
			servers.forEach(server -> {
				serverMap.put(server.getString("server"), server.getString("server"));
			});
			user.put("servers", serverMap);

			for (SqlRow menu : roleMenu) {
				String authServer = menu.getString("auth_server");
				if (Tools.isBlank(authServer)) {
					continue;
				}
				String[] serverArr = authServer.split(",");
				for (String server : serverArr) {
					SqlRow sqlRow = GraphQLAnnotationImpl.authMap.get(server);
					if (sqlRow == null) {
						// sys_server_method配置不需要验证，就不用添加到缓存
						continue;
					}
					if (!flag) {
						if (StringUtils.isNotEmpty(server) && server.contains("-")) {
							serverBuffer.append(sqlRow.getString("model_name"));
							serverBuffer.append(".");
							serverBuffer.append(server.split("-")[1]);
							flag = true;
						}
					} else {
						if (StringUtils.isNotEmpty(server) && server.contains("-")) {
							serverBuffer.append(",");
							serverBuffer.append(sqlRow.getString("model_name"));
							serverBuffer.append(".");
							serverBuffer.append(server.split("-")[1]);
						}
					}
					serverMap.put(server, server);
				}

			}
			serverBuffer.append(",UserInfo.resetPwdCheckOldPwd,UserInfo.updateUserInfo");
			// 查询角色
			List<SqlRow> sqlRows = loginDao.findUserRoles(userid);
			String roleids = "";
			for (SqlRow sqlRow : sqlRows) {
				roleids += "," + sqlRow.getString("roleid");
			}
			if (roleids.length() > 0) {
				user.put("roleids", roleids.substring(1));
			}

			JwtFilter.userMap.put(user.getString("employee_no"), user);

			// 生成token
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userid", userid);
			String data = jsonObject.toString();
			String token = JwtServer.makeToken(new Date(System.currentTimeMillis() + 3 * 60 * 60 * 1000), data);// 3小时过期
			Map<String, Object> reparams = new HashMap<String, Object>();
			reparams.put("token", token);
			reparams.put("username", user.getString("username"));
			reparams.put("userid", userid);
			reparams.put("servers", serverBuffer.toString());
			reparams.put("roleids", roleids);//用户角色
			JwtFilter.userMap.remove(user.getString("employee_no"));
			addLog(userid);
			return updateSuccess("登录成功", reparams);

		}catch (Exception e) {
			log.error("数据库查询异常【{}】", e);
			return updateFailure("服务繁忙，请稍后尝试");

		}
	}
	
	
	
	/**
	 *  紧急登录
	 * @param loginUser
	 * @return
	 */
	public String doLoginUrgent(String loginUser) {
		// 查询用户权限
		try {
			SqlRow user = loginService.findUser(loginUser);
			if (user == null) {
				
				return updateFailure("账号不存在");
			}

			if (!user.get("userstatus").equals("N")) {
				return updateFailure("帐号已经注销");
			}
			String userid = user.getString("userid");
			List<SqlRow> servers = loginDao.findRoleServers(userid);

			StringBuilder serverBuffer = new StringBuilder();
			boolean flag = false;
			for (SqlRow server : servers) {
				String serverStr = server.getString("server");
				String modelStr = server.getString("model_name");

				if (!flag) {
				  if (StringUtils.isNotEmpty(serverStr) && serverStr.contains("-")) {
					serverBuffer.append(modelStr);
					serverBuffer.append(".");
					serverBuffer.append(serverStr.split("-")[1]);
					flag = true;
				  }
				} else {
					if (StringUtils.isNotEmpty(serverStr) && serverStr.contains("-")) {
						serverBuffer.append(",");
						serverBuffer.append(modelStr);
						serverBuffer.append(".");
						serverBuffer.append(serverStr.split("-")[1]);
					}
				}
			}
			List<SqlRow> roleMenu = loginDao.findRoleMenu(userid);

			// 查询服务权限
			Map<String, String> serverMap = new HashMap<String, String>();
			servers.forEach(server -> {
				serverMap.put(server.getString("server"), server.getString("server"));
			});
			user.put("servers", serverMap);

			for (SqlRow menu : roleMenu) {
				String authServer = menu.getString("auth_server");
				if (Tools.isBlank(authServer)) {
					continue;
				}
				String[] serverArr = authServer.split(",");
				for (String server : serverArr) {
					SqlRow sqlRow = GraphQLAnnotationImpl.authMap.get(server);
					if (sqlRow == null) {
						// sys_server_method配置不需要验证，就不用添加到缓存
						continue;
					}
					if (!flag) {
						if (StringUtils.isNotEmpty(server) && server.contains("-")) {
							serverBuffer.append(sqlRow.getString("model_name"));
							serverBuffer.append(".");
							serverBuffer.append(server.split("-")[1]);
							flag = true;
						}
					} else {
						if (StringUtils.isNotEmpty(server) && server.contains("-")) {
						  serverBuffer.append(",");
						  serverBuffer.append(sqlRow.getString("model_name"));
						  serverBuffer.append(".");
						  serverBuffer.append(server.split("-")[1]);
						}
					}
					serverMap.put(server, server);
				}

			}

			// 查询角色
			List<SqlRow> sqlRows = loginDao.findUserRoles(userid);
			String roleids = "";
			for (SqlRow sqlRow : sqlRows) {
				roleids += "," + sqlRow.getString("roleid");
			}
			if (roleids.length() > 0) {
				user.put("roleids", roleids.substring(1));
			}

			JwtFilter.userMap.put(user.getString("employee_no"), user);

			// 生成token
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userid", userid);
			String data = jsonObject.toString();
			String token = JwtServer.makeToken(new Date(System.currentTimeMillis() + 3 * 60 * 60 * 1000), data);// 3小时过期
			Map<String, Object> reparams = new HashMap<String, Object>();
			reparams.put("token", token);
			reparams.put("username", user.getString("username"));
			reparams.put("userid", userid);
			reparams.put("servers", serverBuffer.toString());

			JwtFilter.userMap.remove(user.getString("employee_no"));
			addLog(userid);
			return updateSuccess("登录成功", reparams);
			
		}catch (Exception e) {
			log.error("数据库查询异常【{}】", e);
			return updateFailure("服务繁忙，请稍后尝试");
			
		}
	}
	

	
	public void addLog(String userId) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("method_desc", "登录");
		param.put("operation_date", DateUtil.getNowDate());
		param.put("operation_time", DateUtil.getNowTime());
		param.put("userid", userId);
		param.put("result", "成功");
		try {
			loginService.addLog(param);
		} catch (Exception e) {
			log.error("日志保存异常【{}】",e);
		}
	}

	@RequestMapping(value = "/getAuthServers.json")
	public @ResponseBody String getAuthServers(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> params = RequestSupport.getParameters();

		try {

				// 查询用户权限
				String userid = params.get("userid").toString();
				List<SqlRow> servers = loginDao.findRoleServers(userid);

				StringBuilder serverBuffer = new StringBuilder();
				boolean flag = false;
				for (SqlRow server : servers) {
					String serverStr = server.getString("server");
					String modelStr = server.getString("model_name");

					if (!flag) {
						if (StringUtils.isNotEmpty(serverStr) && serverStr.contains("-")) {
							serverBuffer.append(modelStr);
							serverBuffer.append(".");
							serverBuffer.append(serverStr.split("-")[1]);
							flag = true;
						}
					} else {
						if (StringUtils.isNotEmpty(serverStr) && serverStr.contains("-")) {
							serverBuffer.append(",");
							serverBuffer.append(modelStr);
							serverBuffer.append(".");
							serverBuffer.append(serverStr.split("-")[1]);
						}

					}
				}
				List<SqlRow> roleMenu = loginDao.findRoleMenu(userid);

				// 查询服务权限
//				Map<String, String> serverMap = new HashMap<String, String>();
//				servers.forEach(server -> {
//					serverMap.put(server.getString("server"), server.getString("server"));
//				});

				for (SqlRow menu : roleMenu) {
					String authServer = menu.getString("auth_server");
					if (Tools.isBlank(authServer)) {
						continue;
					}
					String[] serverArr = authServer.split(",");
					for (String server : serverArr) {
						SqlRow sqlRow = GraphQLAnnotationImpl.authMap.get(server);
						if (sqlRow == null) {
							// sys_server_method配置不需要验证，就不用添加到缓存
							continue;
						}
						if (!flag) {
							if(StringUtils.isNotEmpty(server) && server.contains("-")){
								serverBuffer.append(sqlRow.getString("model_name"));
								serverBuffer.append(".");
								serverBuffer.append(server.split("-")[1]);
								flag = true;
							}
						} else {
							if(StringUtils.isNotEmpty(server) && server.contains("-")){
								serverBuffer.append(",");
								serverBuffer.append(sqlRow.getString("model_name"));
								serverBuffer.append(".");
								serverBuffer.append(server.split("-")[1]);
							}
						}
						//serverMap.put(server, server);
					}

				}
				serverBuffer.append(",UserInfo.resetPwdCheckOldPwd,UserInfo.updateUserInfo");

				Map reparams =new HashMap();
				reparams.put("servers", serverBuffer.toString());

				return updateSuccess("获取权限成功", reparams);

			}  catch (Exception ex) {
			log.error(ex.getMessage(),ex);
			return updateSuccess("获取权限失败"+ex.getMessage());
		}

	}

	private String getRegCheckFlag(HttpServletRequest request){
		String regChkflg="1";
		try{
			String ipImportCheck = SysUtil.getSystemParamsByParaid("ip_imp_chk");
			//配置的ip黑名单 以逗号分割
			String ipRegionStrs = SysUtil.getSystemParamsByParaid("ip_region_strs");
			//0屏蔽; 1 不屏蔽
			//reparams.put("regChkflg", IPCheckUtils.checkInValidIp(IPCheckUtils.getClientIp(request),ipRegionStrs));
			if(ipImportCheck==null ||"".equals(ipImportCheck)){
				regChkflg="1";
			}else if("1".equals(ipImportCheck)){ //ip校验，ipRegionStrs需配置ip 例：10.204.51.0,10.204.51.1
				regChkflg = String.valueOf(IPCheckUtils.checkInValidIp(IPCheckUtils.getClientIp(request),ipRegionStrs));
			}else if("2".equals(ipImportCheck)){//网段校验，ip非null，且 ipRegionStrs需配置网段 例：10.204.51.0/24,10.204.52.0/24
				regChkflg = String.valueOf(IpMatcher.matchAnyCidr(IPCheckUtils.getClientIp(request),ipRegionStrs));
			}else if("3".equals(ipImportCheck)){//扩展网段校验，ip非null，且 ipRegionStrs需配置扩展网段 例：10.204.51-53.0/24,10.204.67-70.0/24
				regChkflg = String.valueOf(IpMatcher.matchAnyCidrEx(IPCheckUtils.getClientIp(request),ipRegionStrs));
			}else{
				regChkflg="1";
			}
			log.info("ipRegionStrs:" +ipRegionStrs +"; IP2:"+ IPCheckUtils.getClientIp(request)+ "; regChkflg:"+regChkflg);
		}catch (Exception e){
			regChkflg="1";
		}
		return regChkflg;
	}
}
