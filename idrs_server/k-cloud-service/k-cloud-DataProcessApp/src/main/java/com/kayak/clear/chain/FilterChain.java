package com.kayak.clear.chain;

import java.util.ArrayList;
import java.util.List;

/**
 * 过滤责任链
 * @param <REQ>
 * @param <RESP>
 */
public class FilterChain <REQ, RESP> implements Filter<REQ,RESP>{

    private List<Filter<REQ,RESP>> filters = new ArrayList<>();
    private int index = 0 ;

    /**
     * 添加过滤器
     * @param filter 过滤器
     * @return
     */
    public FilterChain<REQ,RESP> addFilter(Filter<REQ,RESP> filter){
        filters.add(filter);
        return this;
    }

    @Override
    public void doFilter(REQ request, RESP response, FilterChain<REQ, RESP> chain) throws Exception{
        while(index < this.size()) {
            Filter<REQ, RESP> filter = filters.get(index++);
            filter.doFilter(request, response, chain);
        }
    }

    @Override
    public void reset() {
        for(Filter<REQ,RESP> item: filters){
            item.reset();
        }
        index = 0;
    }

    /**
     * 过滤器数量
     * @return
     */
    public int size(){
        return filters.size();
    }
}
