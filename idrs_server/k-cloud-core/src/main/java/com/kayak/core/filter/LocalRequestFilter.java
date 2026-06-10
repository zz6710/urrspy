package com.kayak.core.filter;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.kayak.core.system.RequestSupport;
import org.apache.catalina.connector.RequestFacade;

public class LocalRequestFilter implements Filter {
    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        String userFrom = ((RequestFacade) request).getHeader("User-from");
        String queryString = ((RequestFacade) request).getQueryString();
        /*对光大家发送过来的请求做一下特殊处理*/
        if ("pmsGdApp".equals(userFrom) || (queryString != null && queryString.contains("isBankAppFileDownload") && queryString.contains("needDecode"))) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userFrom", userFrom);
            /*对需要进行解码的url做处理*/
            if ((queryString != null && queryString.contains("isBankAppFileDownload") && queryString.contains("needDecode"))) {
                String[] split = queryString.split("&");
                HashMap<String, String> paramHandler = new HashMap<>();
                for (String s: split) {
                    String[] split1 = s.split("=");
                    if (split1.length > 1) {
                        paramHandler.put(split1[0], URLDecoder.decode(split1[1], "UTF-8"));
                    } else {
                        paramHandler.put(split1[0], "");
                    }
                }
                /*将解码后的参数从新设置到map集合中*/
                map.putAll(paramHandler);
            }
            ParameterRequestWrapper parameterRequestWrapper = new ParameterRequestWrapper((HttpServletRequest) request, map);
            RequestSupport.setLocalRequest(parameterRequestWrapper);
            RequestSupport.clearUserParams();
            // 加入filter链继续向下执行
            chain.doFilter(parameterRequestWrapper, response);
        } else {
            RequestSupport.setLocalRequest((HttpServletRequest) request);
            RequestSupport.clearUserParams();
            // 加入filter链继续向下执行
            chain.doFilter(request, response);// 调用FilterChain对象的doFilter方法。Filter接口的doFilter方法取一个FilterChain对象作为它的一个参数。在调用此对象的doFilter方法时，激活下一个相关的过滤器。如果没有另一个过滤器与servlet或JSP页面关联，则servlet或JSP页面被激活。
        }
    }

}
