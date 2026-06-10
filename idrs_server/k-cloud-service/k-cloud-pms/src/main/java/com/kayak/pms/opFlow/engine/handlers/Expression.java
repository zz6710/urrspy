package com.kayak.pms.opFlow.engine.handlers;

import java.util.Map;

/**
 * 表达式解析接口
 * Created by daniel on 20/03/2017.
 */
public interface Expression {
    <T> T eval(Class<T> T, String expr, Map<String, Object> args);
}
