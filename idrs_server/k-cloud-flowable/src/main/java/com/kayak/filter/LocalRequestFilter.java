package com.kayak.filter;

import com.kayak.utils.RequestSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding("UTF-8");
        }
        RequestSupport.setLocalRequest((HttpServletRequest) request);
        RequestSupport.clearUserParams();
        String contentType = request.getContentType();
        // 加入filter链继续向下执行
        chain.doFilter(request, response);// 调用FilterChain对象的doFilter方法。Filter接口的doFilter方法取一个FilterChain对象作为它的一个参数。在调用此对象的doFilter方法时，激活下一个相关的过滤器。如果没有另一个过滤器与servlet或JSP页面关联，则servlet或JSP页面被激活。
    }

}
