package com.kayak.core.system;

import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class SysUtil {
	/**
	 * 数组参数值组成字符串的分隔符
	 */
	public static final String PARAVALUE_ARRAY_SEPARATOR = "|,|";
	public static final String USERID = "userid";

	public static final String USERNAME = "realname";

	/**
	 * 数组参数值组成字符串的分隔符应用到java.lang.String.split方法的正则串
	 */
	public static final String PARAVALUE_ARRAY_SPLIT_SEPARATOR = PARAVALUE_ARRAY_SEPARATOR.replaceAll("\\|", "\\\\\\|");

	public static Boolean buttonAuth;

	/**
	 * 逗号中每个字符串都加下单引号
	 *
	 * @param str 字符串
	 * @return String
	 */
	public static String inStr(String str) {
		if (StringUtils.isEmpty(str)) {
			return str;
		}

		StringBuilder sb = new StringBuilder();
		String[] strArr = str.split(",");

		for (String string : strArr) {
			if (StringUtils.isEmpty(sb)) {
				sb.append("'" + string + "'");
			} else {
				sb.append(",'" + string + "'");
			}
		}

		return String.valueOf(sb);
	}

	/**
	 * 返回date的时间是否在timeRange指定的时间范围内
	 *
	 * @param date      Date
	 * @param timeRange String 格式：hhmm-hhmm
	 */
	public static boolean isInTimeRange(Date date, String timeRange) {
		if (date == null || timeRange == null)
			return false;

		String timeStr = Tools.dt2Time1(date);
		int time = Tools.str2Int(timeStr.substring(0, 4));

		String[] range = timeRange.split("[-]");
		if (range.length != 2)
			return false;

		int startTime = Tools.str2Int(range[0].trim());
		int endTime = Tools.str2Int(range[1].trim());

		return time >= startTime && time <= endTime;
	}

	/**
	 * 系统参数变量名前缀
	 */
	public static final String SYS_PARAM_PREFIX = "sys_param_";
	/**
	 * 用户信息变量名前缀
	 */
	public static final String SYS_USER_PREFIX = "sys_user_";

	/**
	 * 把当前用户登录信息参数集保存在SESSION中的key
	 */
	public static final String SYS_USER_PARAMS_SESSION_KEY = "session.sys.user.params";

	/***
	 * 获取系统参数
	 *
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, SqlRow> getSystemParams() throws Exception {
		DaoService daoService = SysBeans.getBean("daoService");
		try (AutoCloseable ca = daoService.selectDataSource(0)) {
			List<SqlRow> sqlRows = daoService.list(SqlRow.class, "SELECT * FROM sys_param", null);
			HashMap<String, SqlRow> systemParamsMap = new HashMap<>();
			for (SqlRow sqlRow : sqlRows) {
				systemParamsMap.put(sqlRow.getString("paraid"), sqlRow);
			}
			return systemParamsMap;
		}

	}

	public static String getSystemParamsByParaid(String paraid) throws Exception {
		HashMap<String, SqlRow> systemParams = SysUtil.getSystemParams();
		SqlRow sqlRow = (SqlRow) systemParams.get(paraid);
		return sqlRow.getString("paravalue");
	}

	/**
	 * 取得当前用户登录信息参数集
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getSysUserParams() {
		// 用户登录信息参数，先尝试从SESSION中取得
		if (RequestSupport.getLocalRequest() == null) {
			return null;
		}
		Map<String, Object> userParams = (Map<String, Object>) RequestSupport.getLocalRequest().getSession()
				.getAttribute(SYS_USER_PARAMS_SESSION_KEY);

		DaoService daoService = SysBeans.getBean("daoService");

		try (AutoCloseable ca = daoService.selectDataSource(0)) {
			String userid = Tools.obj2Str(userParams.get("userid"));
			SqlRow user = daoService.query(SqlRow.class, "SELECT * FROM sys_user WHERE userid = $S{userid}", userid);

			// 查询服务权限
			List<SqlRow> servers = daoService.list(SqlRow.class,
					"SELECT t1.server FROM sys_server_method t1, sys_role_server t2, "
							+ "sys_user_role t3 WHERE t1.server = t2.server AND t2.roleid = t3.roleid AND t3.userid = $S{userid}",
					userid);
			Map<String, String> serverMap = new HashMap<>();
			servers.forEach(server -> {
				serverMap.put(server.getString("server"), server.getString("server"));
			});
			// 查询菜单配置的接口权限
			serverMap.putAll(getUserMenuServerMap(userid));

			user.put("servers", serverMap);
			// 查询角色
			List<SqlRow> sqlRows = daoService.list(SqlRow.class,
					"SELECT roleid FROM sys_user_role WHERE userid = $S{userid}", userid);
			String roleids = "";
			for (SqlRow sqlRow : sqlRows) {
				roleids += "," + sqlRow.getString("roleid");
			}
			user.put("roleids", roleids.length() > 0 ? roleids.substring(1) : "");
			String[] roleidsArr = roleids.split(",");
			String str = "";
			for (String roleid : roleidsArr) {
				List<SqlRow> sqlRowList = daoService.list(SqlRow.class,
						"SELECT * FROM sys_role WHERE roleid = $S{roleid}", roleid);
				if(sqlRowList.size() >0 && sqlRowList.get(0).containsKey("busiss_code")){
					List<SqlRow> list = daoService.list(SqlRow.class,
							"SELECT busiss_code FROM sys_role WHERE roleid = $S{roleid}", roleid);
					for (SqlRow sqlRow : list) {
						str += "," + sqlRow.getString("busiss_code");
					}
				}
			}
			if(!str.isEmpty() && str.length()>0){
				Set<String> set = new LinkedHashSet<String>();
				String[] busissCodes = str.split(",");
				StringBuffer busissCode = new StringBuffer();
				for (int i = 0; i < busissCodes.length; i++) {
					if (!set.contains(busissCodes[i]) && !busissCodes[i].isEmpty()) {
						set.add(busissCodes[i]);
						busissCode.append("," + busissCodes[i]);
					}
				}
				user.put("busissCode",busissCode.length() > 0 ? busissCode.substring(1) : "");
			}
			return user;
		} catch (Exception e) {
			log.error("获取用户登陆信息异常", e);
		}

		return userParams;
	}

	/**
	 * 取得当前用户登录信息参数集,给理财代销用
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getSysUserParamsByWmp() {
		// 用户登录信息参数，先尝试从SESSION中取得
		if (RequestSupport.getLocalRequest() == null) {
			return null;
		}
		Map<String, Object> userParams = (Map<String, Object>) RequestSupport.getLocalRequest().getSession()
				.getAttribute(SYS_USER_PARAMS_SESSION_KEY);

		DaoService daoService = SysBeans.getBean("daoService");

		try (AutoCloseable ca = daoService.selectDataSource(0)) {
			String userid = Tools.obj2Str(userParams.get("userid"));
			SqlRow user = daoService.query(SqlRow.class, "SELECT * FROM sys_user WHERE userid = $S{userid}", userid);

			// 查询服务权限
			List<SqlRow> servers = daoService.list(SqlRow.class,
					"SELECT t1.server FROM sys_server_method t1, sys_role_server t2, "
							+ "sys_user_role t3 WHERE t1.server = t2.server AND t2.roleid = t3.roleid AND t3.userid = $S{userid}",
					userid);
			Map<String, String> serverMap = new HashMap<>();
			servers.forEach(server -> {
				serverMap.put(server.getString("server"), server.getString("server"));
			});
			// 查询菜单配置的接口权限
			serverMap.putAll(getUserMenuServerMap(userid));

			user.put("servers", serverMap);
			// 查询角色
			List<SqlRow> sqlRows = daoService.list(SqlRow.class,
					"SELECT roleid FROM sys_user_role WHERE userid = $S{userid}", userid);
			String roleids = "";
			for (SqlRow sqlRow : sqlRows) {
				roleids += "," + sqlRow.getString("roleid");
			}
			user.put("roleids", roleids.length() > 0 ? roleids.substring(1) : "");

			return user;
		} catch (Exception e) {
			log.error("获取用户登陆信息异常", e);
		}

		return userParams;
	}

	public static Map<String, String> getUserMenuServerMap(String userid) throws Exception {
		DaoService daoService = SysBeans.getBean("daoService");

		Map<String, String> serverMap = new HashMap<>();
		try (AutoCloseable ca = daoService.selectDataSource(0)) {
			List<SqlRow> roleMenu = daoService.list(SqlRow.class,
					"SELECT m.* FROM sys_menu m " + " JOIN sys_role_menu rm ON m.menuid = rm.menuid "
							+ " JOIN sys_user_role ur ON ur.roleid = rm.roleid " + " WHERE ur.userid = $S{userid}",
					userid);
			for (SqlRow menu : roleMenu) {
				String authServer = menu.getString("auth_server");
				if (Tools.isBlank(authServer)) {
					continue;
				}
				String[] serverArr = authServer.split(",");
				for (String server : serverArr) {
					serverMap.put(server, server);
				}
			}
		}

		return serverMap;
	}
	public static Map<String, String> getUserMenuMap(String userid) throws Exception {
		DaoService daoService = SysBeans.getBean("daoService");

		Map<String, String> menuMap = new HashMap<>();
		try (AutoCloseable ca = daoService.selectDataSource(0)) {
			List<SqlRow> roleMenu = daoService.list(SqlRow.class,
					"SELECT m.* FROM sys_menu m " + " JOIN sys_role_menu rm ON m.menuid = rm.menuid "
							+ " JOIN sys_user_role ur ON ur.roleid = rm.roleid " + " WHERE ur.userid = $S{userid}",
					userid);
			for (SqlRow menu : roleMenu) {
				String menuid = menu.getString("menuid");
				if (Tools.isBlank(menuid)) {
					continue;
				}
					menuMap.put(menuid, menuid);
				}
		}

		return menuMap;
	}
	public static Map<String, Object> getSysUserParams(Map<String, Object> params) {
		// 用户登录信息参数，先尝试从SESSION中取得
		Map<String, Object> userParams = (Map<String, Object>) params.get(SYS_USER_PARAMS_SESSION_KEY);
		return userParams;
	}

	public static String getLoginUserid() {
		// 用户登录信息参数，先尝试从SESSION中取得
		HttpServletRequest request = RequestSupport.getLocalRequest();
		if(request==null||request.getSession()==null)
			return null;
		SqlRow userParams = (SqlRow) request.getSession().getAttribute(SYS_USER_PARAMS_SESSION_KEY);
		if(userParams==null)
			return null;
		
		return userParams.getString("userid");
	}

	/**
	 * 取得当前用户登录信息参数值
	 *
	 * @throws
	 */
	public static Object getSysUserParamValue(String pname) {
		if (!pname.startsWith(SYS_USER_PREFIX)) {
			return null;
		}
		Map<String, Object> userParams = getSysUserParams();
		if (userParams == null)
			return null;

		pname = pname.replaceFirst(SysUtil.SYS_USER_PREFIX, "");
		return userParams.get(pname);
	}

	public static String getCurrentEmployeeNo() {
		return (String) SysUtil.getSysUserParamValue("sys_user_employee_no");
	}

	/**
	 * 取得当前用户登录信息参数值
	 *
	 * @throws
	 */
	public static Object getSysUserParamValue(Map<String, Object> params, String pname) {
		if (!pname.startsWith(SYS_USER_PREFIX)) {
			return null;
		}
		Map<String, Object> userParams = getSysUserParams(params);
		if (userParams == null)
			return null;

		pname = pname.replaceFirst(SysUtil.SYS_USER_PREFIX, "");
		return userParams.get(pname);
	}

	/**
	 * 解释数据库中定义为char型，用于表示是/否的值，翻译成boolean对象返回
	 */
	public static boolean char2Boolean(String str) {
		return "Y".equalsIgnoreCase(str);
	}

	/**
	 * 将boolean值解释成数据库中定义为char型，用于表示是/否的值
	 */
	public static String boolean2Char(Boolean bool) {
		return (bool != null && bool ? "Y" : "N");
	}

	/**
	 * 将数组所有元素以PARAVALUE_ARRAY_SEPARATOR定义的分隔符串连起来组成一个字符串返回<br />
	 * （数组中的元素将被直接转成字符串）
	 *
	 * @param objs
	 * @return
	 */
	public static String paraArray2String(Object[] objs) {
		return Tools.arrayJoin(objs, PARAVALUE_ARRAY_SEPARATOR);
	}

	public static void main(String[] args) {
		String str = "20120314181212";
		System.out.println(str.substring(0, 8));
		System.out.println(str.substring(8));
	}

	/**
	 * 取得系统动态参数的实时值
	 *
	 * <pre>
	 * SYSDATE - String, 系统当前日期
	 * SYSTIME - String, 系统当前时间
	 * SYSYESTODAY - String，系统昨天的日期
	 * SYSYFIRSTDAY - String, 今年的第一天
	 * SYSMFIRSTDAY - String, 本月的第一天
	 * SYSMLASTDAY - String, 本月的最后一天
	 * </pre>
	 *
	 * @param pname
	 * @return
	 */
	public static Object getSysDynamicParamValue(String pname) {
		if ("SYSDATE".equals(pname)) {
			return Tools.dt2Date1(new Date());
		} else if ("SYSTIME".equals(pname)) {
			return Tools.dt2Time1(new Date());
		} else if ("SYSYFIRSTDAY".equals(pname)) {
			return Tools.dt2Date1(Tools.firstDayOfCurrYear());
		} else if ("SYSMFIRSTDAY".equals(pname)) {
			return Tools.dt2Date1(Tools.firstDayOfMonth(new Date()));
		} else if ("SYSMLASTDAY".equals(pname)) {
			return Tools.dt2Date1(Tools.lastDayOfMonth(new Date()));
		} else if ("SYSYESTODAY".equals(pname)) {
			return Tools.dt2Date1(Tools.yeatoday());
		}
		// 没有值返回null
		return null;
	}
	public static Map<String, Object> getUserInfo() {
		Map<String, Object> result = new HashMap<>();
		HttpServletRequest request = RequestSupport.getLocalRequest();
		final String userid = request.getHeader("Authorization");
		result.put(USERID, userid);
		final String username;
		try {
			DaoService daoService = SysBeans.getBean("daoService");
			final List<SqlRow> rows = daoService.list(SqlRow.class,"SELECT * FROM sys_user WHERE userid = $S{userid}", result);
			username = rows.get(0).getString("username");
			result.put(USERNAME, username);
			result.put("jobno", rows.get(0).getString("jobno"));
		} catch (Exception e) {
			log.error("查询用户信息异常【{}】",e);

		}
		return result;

	}
	public static String getLoginUserRoleIds() {
		HttpServletRequest request = RequestSupport.getLocalRequest();
		if (request == null || request.getSession() == null)
			return null;
		SqlRow userParams = (SqlRow) request.getSession().getAttribute(SYS_USER_PARAMS_SESSION_KEY);
		if (userParams == null)
			return null;
//		String userid = userParams.getString("userid");
		String userid = SysUtil.getLoginUserid();
		log.info("操作流获取当前登录用户id为{}",userid);
		return String.join(",",getRoleIdsByUser(userid));
	}
	public static List<String> getRoleIdsByUser(String userid){
		DaoService daoService = SysBeans.getBean("daoService");
		try {
			log.info("用户信息{}",userid);
			List<String> list = daoService.list(String.class, "SELECT roleid FROM sys_user_role WHERE userid = '" + userid + "'", userid);
			log.info("获取的用户角色信息{}",list);
			return list;
		}catch (Exception e){
			log.error("获取用户角色信息异常",e);
		}
		return Collections.emptyList();
	}

}
