package com.kayak.jwt.filter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;

public class JwtFilter implements Filter {

	private static Logger log = LoggerFactory.getLogger(JwtFilter.class);

	public final static Map<String, SqlRow> userMap = new ConcurrentHashMap<>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		String userid = httpRequest.getHeader("Authorization");
		try {
			if (!Tools.strIsEmpty(userid)) {
				HttpSession session = httpRequest.getSession();

				if (!userMap.containsKey(userid)) {
					SqlRow sqlRow = new SqlRow();

					sqlRow.put("userid", userid);
					userMap.put(userid, sqlRow);
				}
				session.setAttribute(SysUtil.SYS_USER_PARAMS_SESSION_KEY, userMap.get(userid));
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);

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
