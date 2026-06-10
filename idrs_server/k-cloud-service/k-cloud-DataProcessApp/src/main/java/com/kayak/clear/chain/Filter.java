package com.kayak.clear.chain;

/**
 * 过滤责任链接口
 * @param <REQ>
 * @param <RESP>
 */
public interface Filter<REQ,RESP> {

    /**
     * 进行过滤
     * @param request 请求对象
     * @param response 响应对象
     * @param chain 责任链
     */
    void doFilter(REQ request, RESP response, FilterChain<REQ,RESP> chain) throws Exception;

    /**
     * 进行重置
     */
    void reset();
}
