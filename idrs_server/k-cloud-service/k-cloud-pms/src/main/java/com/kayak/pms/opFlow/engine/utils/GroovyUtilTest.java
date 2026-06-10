package com.kayak.pms.opFlow.engine.utils;

/**
 * @author ddai
 * @date 2019-04-26 16:04
 */
public class GroovyUtilTest {

    public static void main(String[] args) {
//        String envTransitionExpr = "$N{  一级审批条件  } == 1";
//        String key = envTransitionExpr.substring(envTransitionExpr.indexOf("$N{"), envTransitionExpr.indexOf("}") + 1);
//        String sql = envTransitionExpr.substring(envTransitionExpr.indexOf("$N{") + 3, envTransitionExpr.indexOf("}"));
        String envTransitionExpr = "$S{  一级审批条件  } == 'a'";

        String key = envTransitionExpr.substring(envTransitionExpr.indexOf("$S{"), envTransitionExpr.indexOf("}") + 1);
        String sql = envTransitionExpr.substring(envTransitionExpr.indexOf("$S{") + 3, envTransitionExpr.indexOf("}"));

        // 执行sql 返回值
//        Integer res = 1;
        String res = "a";

//        String exprWithVal = envTransitionExpr.replace(key, res.toString());
        String exprWithVal = envTransitionExpr.replace(key, "\"" + res +"\"");


        System.out.println(exprWithVal);
        System.out.println(GroovyUtil.eval(exprWithVal));
    }
}
