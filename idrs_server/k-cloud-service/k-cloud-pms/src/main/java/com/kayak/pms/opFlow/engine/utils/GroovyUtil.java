package com.kayak.pms.opFlow.engine.utils;

import groovy.lang.GroovyShell;

/**
 * @author ddai
 * @date 2019-04-26 16:25
 */
public class GroovyUtil {
    private static GroovyShell groovyShell = new GroovyShell();

    public static boolean eval(String expr) {
        Object evaluate = groovyShell.evaluate(expr);
        if (evaluate instanceof Boolean) {
            return (boolean) evaluate;
        } else {
            throw new RuntimeException("表达式: " + expr + " 计算结果不为 boolean 值");
        }
    }

}
