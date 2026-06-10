package com.kayak.login.filter;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysBeans;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.login.dao.LoginDao;
import io.jsonwebtoken.Claims;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JwtFilter implements Filter {

	private static Logger log = LoggerFactory.getLogger(JwtFilter.class);

	private String unfilterPage;

	public static String[] unfilters;

	public static Map<String, Map> userMap = new ConcurrentHashMap<>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		unfilterPage = filterConfig.getInitParameter("unfilter-page");
		unfilters = unfilterPage.split("[,]");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// 如果处理HTTP请求，并且需要访问诸如getHeader或getCookies等在ServletRequest中无法得到的方法，就要把此request对象构造成HttpServletRequest
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		
		// 取得根目录所对应的绝对路径
		String currentURL = httpRequest.getRequestURI();
		// 截取到当前文件名用于比较
		String targetURL =
				currentURL.substring(currentURL.indexOf("/", 1) == -1 ? 1 : currentURL.indexOf("/", 1));

		boolean dofilter = true;// 是否做过滤

		for (String page : unfilters) {
			String p = page.trim();
			if (p.equals(targetURL) || p.equals(currentURL)) {
				dofilter = false;
				break;
			}
		}
		
		if (!dofilter) {
			chain.doFilter(request, response);
			return;
		}

		// jwt验证
		String token = httpRequest.getHeader("Authorization");
		if (Tools.strIsEmpty(token)) {// 没有token，则尝试从url参数获取
			Map<String, Object> params = RequestSupport.getParameters();
			if (params.containsKey("Authorization")) {
				token = Tools.obj2Str(params.get("Authorization"));
			}
		}
		if (Tools.strIsEmpty(token)) {// 没有token，不进行解析
			// 重新登录
			JSONObject _json = new JSONObject();
			_json.put("success", false);
			_json.put("login", true);
			response.getWriter().write(_json.toString());
			response.getWriter().flush();
			return;
		}

		// 解析token
		try {
			String json = null;
			try {
				Claims claims = JwtServer.parshTokenBody(token);
				json = claims.getSubject();
				if((claims.getExpiration().getTime() - System.currentTimeMillis()) < 0){
					JSONObject _json = new JSONObject();
					_json.put("success", false);
					_json.put("login", true);
					HttpServletResponse response1= (HttpServletResponse) response;
					response1.setHeader("Access-Control-Expose-Headers", "login");
					response1.setHeader("login", "true");
					response.getWriter().write(_json.toString());
					response.getWriter().flush();
					return;
				}
				int autoLogoutTime=Integer.parseInt(SysUtil.getSystemParams().get("10008").getString("paravalue"));
				if ((claims.getExpiration().getTime() - System.currentTimeMillis()) < 0.95 *autoLogoutTime* 60 * 1000) {
					log.info("刷新token时效");
					String _token = JwtServer.makeToken(new Date(System.currentTimeMillis() + autoLogoutTime * 60 * 1000),
							json);
					// 更新token

					JSONObject _json = new JSONObject();
					_json.put("success", false);
					_json.put("token", _token);
					_json.put("token_freshen", true);
					_json.put("zcs_test", true);
					HttpServletResponse response1= (HttpServletResponse) response;
					response1.setHeader("Access-Control-Expose-Headers", "token_freshen");
					response1.setHeader("token_freshen", _token);
					response1.getWriter().write(_json.toString());
					response1.getWriter().flush();
					return;
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				// 重新登录
				JSONObject _json = new JSONObject();
				_json.put("success", false);
				_json.put("login", true);
				response.getWriter().write(_json.toString());
				response.getWriter().flush();
				return;
			}
			HttpSession session = httpRequest.getSession();

			String userid = new JSONObject(json).getString("userid");

			if (!userMap.containsKey(userid)) {
				synchronized (JwtFilter.class) {// 防止服务器重启导致用户会话数据丢失，重新查询缓存
					if (!userMap.containsKey(userid)) {
						LoginDao loginDao = SysBeans.getBean("loginDao");
						SqlRow user = loginDao.findUserByUserid(userid);
						// 查询服务权限
						List<SqlRow> servers = loginDao.findRoleServers(userid);
						Map<String, String> serverMap = new HashMap<>();
						servers.forEach(server -> {
							serverMap.put(server.getString("server"), server.getString("server"));
						});
						user.put("servers", serverMap);
						// 查询角色
						List<SqlRow> sqlRows = loginDao.findUserRoles(userid);
						String roleids = "";
						for (SqlRow sqlRow : sqlRows) {
							roleids += "," + sqlRow.getString("roleid");
						}
						if (roleids.length() > 0) {
							user.put("roleids", roleids.substring(1));
						}

						userMap.put(userid, user);
					}
				}
			}

			session.setAttribute(SysUtil.SYS_USER_PARAMS_SESSION_KEY, userMap.get(userid));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// 重新登录
			JSONObject json = new JSONObject();
			json.put("success", false);
			json.put("login", true);
			response.getWriter().write(json.toString());
			response.getWriter().flush();
			return;
		}

		chain.doFilter(request, response);
		return;

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
