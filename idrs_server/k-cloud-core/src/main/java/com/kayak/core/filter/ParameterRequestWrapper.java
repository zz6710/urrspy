package com.kayak.core.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/9/11 17:40
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper {
    private static final Logger log = LoggerFactory.getLogger(ParameterRequestWrapper.class);
    /*存放请求参数*/
    private Map<String, String[]> params = new HashMap<>();
    /*存放请求头参数*/
    private Map<String, String> headerMap = new HashMap<>();

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request
     * @throws IllegalArgumentException if the request is null
     */
    public ParameterRequestWrapper(HttpServletRequest request) {
        super(request);
        //将参数表，赋予给当前的Map以便于持有request中的参数
        this.params.putAll(request.getParameterMap());
    }

    /**
     * 重载构造方法
     */

    public ParameterRequestWrapper(HttpServletRequest request, Map<String, Object> extendParams) {
        this(request);
        //这里将扩展参数写入参数表
        addAllParameters(extendParams);
    }

    /**********************************重写设置请求头方法*********************************************/

    /**
     * 在header中添加key-value
     */
    public void addHeader(String key, String value) {
        headerMap.put(key, value);
    }

    @Override
    public String getHeader(String name) {
        String headerValue = super.getHeader(name);
        if (headerMap.containsKey(name)) {
            headerValue = headerMap.get(name);
        }
        return headerValue;
    }

    /**
     * 需要重写的方法，即获取请求头Key时，加上自定义请求头内容的key
     * get the Header names
     */
    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());
        for (String name : headerMap.keySet()) {
            names.add(name);
        }
        return Collections.enumeration(names);
    }

    /**
     * 需要重写的方法，获取所有的header参数时，添加上自定义header的map
     * get the Header key-value
     */
    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> values = Collections.list(super.getHeaders(name));
        if (headerMap.containsKey(name)) {
            values = Arrays.asList(headerMap.get(name));
        }
        return Collections.enumeration(values);
    }

    /**********************************重写设置请求参数方法*********************************************/

    /**
     * 在获取所有的参数名,必须重写此方法，否则对象中参数值映射不上
     *
     * @return
     */
    @Override
    public Enumeration<String> getParameterNames() {
        return new Vector(params.keySet()).elements();
    }

    /**
     * 重写getParameter方法
     *
     * @param name 参数名
     * @return 返回参数值
     */
    @Override
    public String getParameter(String name) {
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values;
    }
    @Override
    public Map<String, String[]> getParameterMap(){
        return params;
    }
    /**
     * 增加多个参数
     *
     * @param otherParams 增加的多个参数
     */
    public void addAllParameters(Map<String, Object> otherParams) {
        for (Map.Entry<String, Object> entry : otherParams.entrySet()) {
            addParameter(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 增加参数
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void addParameter(String name, Object value) {
        if (value != null) {
            if (value instanceof String[]) {
                params.put(name, (String[]) value);
            } else if (value instanceof String) {
                params.put(name, new String[]{(String) value});
            } else {
                params.put(name, new String[]{String.valueOf(value)});
            }
        }
    }
}
