package com.kayak.dps.expresssion.util;

import com.kayak.dps.check.util.PrimaryDataCheckUtil;
import com.kayak.dps.expresssion.exception.CalculationFormulaExecuteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Slf4j
public class DataCalculationUtil {

    /**
     * 二维报表校验表达式是否成立
     * @param express 计算表达式
     * @param allow_deviation
     * @param express_symbol 表达式符号
     * @param report_date 报送日期
     * @param coordinate_type 报表类型
     * @return 布尔值,true则表达式成立，false则不成立
     * @throws Exception
     */
    public static boolean transferExpressToResultC2 (String express, String allow_deviation, String express_symbol, String report_date, String coordinate_type, Map<String, Object> params2) throws CalculationFormulaExecuteException{
        log.info("校验二维报表表达式是否成立,计算表达式: " + express);
        boolean result = false;
        String infix_express = express;
        String value_str = infix_express.replace("+", "|").replace("-", "|").replace("*", "|").
                replace("/", "|").replace("=", "|").replace("<=", "|").replace(">=", "|").
                replace("<", "|").replace(">", "|").replace("≤", "|").replace("≥", "|");
        String[] value_fields = value_str.split("\\|");

        try {
            for (String value_field : value_fields) {
                Pattern pattern = Pattern.compile("^value\\(.*\\,\\d{0,3}\\,\\d{0,3}\\)");
                Matcher match = pattern.matcher(value_field);
                while (match.find()) {
                    String contents = match.group().substring(match.group().indexOf("(")+1, match.group().length() -1);//获取value方法入参参数
                    String[] content_field = contents.split(",");
                    //Object value_obj = PrimaryDataCheckUtil.valueC2(content_field[0], content_field[1], content_field[2], report_date);//二维报表获取数值
                    String value_o =
                            (((Map<String, Object>) params2.get(content_field[0])).get(content_field[1]+"_"+content_field[2])==null || "".equalsIgnoreCase(((Map<String, Object>) params2.get(content_field[0])).get(content_field[1]+"_"+content_field[2]).toString()))?
                                    "0":((Map<String, Object>) params2.get(content_field[0])).get(content_field[1]+"_"+content_field[2]).toString();
                    //将原表达式两端需要计算的value表达式替换成计算结果
                    infix_express = infix_express.trim().replace(match.group(), value_o).replace("≤", "<=").replace("≥", ">=");
                }
            }
            String[] expressStr = infix_express.split(express_symbol);//分隔表达式两端分别计算进行比较
            result = FormulaUtil.judgeCalculationResults(express_symbol, BigDecimal.valueOf(expressCalculate(expressStr[0])), BigDecimal.valueOf(expressCalculate(expressStr[1])), allow_deviation);
            if(!result){
            }
        } catch (Exception e) {
            throw new CalculationFormulaExecuteException("报送业务二维报表数据计算/表间校验表达式计算异常: " + e.getMessage());
        }

        return result;
    }

    /**
     * 二维报表校验表达式是否成立
     * @param express 计算表达式
     * @param allow_deviation
     * @param express_symbol 表达式符号
     * @param report_table 报送报表
     * @param coordinate_type 报表类型
     * @return 布尔值,true则表达式成立，false则不成立
     * @throws CalculationFormulaExecuteException
     */
    public static boolean transferExpressToResultC1 (String express, String allow_deviation, String express_symbol, String report_table, String coordinate_type, Map<String, Object> params1) throws CalculationFormulaExecuteException{
        log.info("校验表达式是否成立,计算表达式: " + express);
        boolean result = true;
        String infix_express = express;
        String value_str = infix_express.replace("+", "|").replace("-", "|").replace("*", "|").
                replace("/", "|").replace("=", "|").replace("<=", "|").replace(">=", "|").
                replace("<", "|").replace(">", "|").replace("≤", "|").replace("≥", "|");
        String[] value_fields = value_str.split("\\|");

        try {
            for(String value_field : value_fields){
                Pattern pattern = Pattern.compile("^value\\(.*\\,.*\\)");
                Matcher match = pattern.matcher(value_field);
                while (match.find()){
                    String contents = match.group().substring(match.group().indexOf("(")+1, match.group().length() -1);//获取value方法入参参数
                    String[] content_field = contents.split(",");
                    Object value_obj = params1.get(content_field[1]);
                    //将原表达式两端需要计算的value表达式替换成计算结果
                    infix_express = infix_express.trim().replace(match.group(), String.valueOf(value_obj)).replace("≤", "<=").replace("≥", ">=");
                }
            }
            String[] expressStr = infix_express.split(express_symbol);//分隔表达式两端分别计算进行比较
            boolean res = FormulaUtil.judgeCalculationResults(express_symbol, BigDecimal.valueOf(expressCalculate(expressStr[0])), BigDecimal.valueOf(expressCalculate(expressStr[1])), allow_deviation);
            if (!res) {
                result = false;//若所有行数据中存在一行不满足条件，则校验失败
            }
        } catch (Exception e) {
            throw new CalculationFormulaExecuteException("报送业务报表数据计算/表间校验表达式计算异常: " + e.getMessage());
        }
        return result;
    }

    /**
     *
     * @param infix_express 后缀表达式
     * @return
     */
    public static double expressCalculate(String infix_express){
        log.info("后缀表达式为: + infix_express");
        double result = 0.0;
        try{
            ArrayList<String> suffix = infixToSuffix(infix_express);
            result = calculate(suffix);
            log.info("中缀表达式为: " + suffix.toString() + "。 计算结果:" + result);
        }catch (Exception e){
            log.info("表达式计算异常:" + e.getMessage());
        }
        return result;
    }

    /**
     * 将后缀表达式转化为中缀表达式
     * @param infix
     * @return
     * @throws Exception
     */
    public static ArrayList<String> infixToSuffix(String infix) throws Exception{
        if(StringUtils.isEmpty(infix.trim())){
            return null;
        }
        ArrayList<String> suffix = new ArrayList<>();//后缀表达式
        Stack<String> operator = new Stack<>();
        //匹配数字、符号正则匹配
        Pattern pattern = Pattern.compile("(?<!\\d)-?\\d+(\\.\\d+)?|[+\\-*/\\(\\)]");
        Matcher match = pattern.matcher(infix);
        while (match.find()){
            String matchStr = match.group();
            if (matchStr.matches("[+\\-*/\\(\\)]")){
                if("(".equals(matchStr)){
                    operator.push(matchStr);//读到(入栈
                } else if(")".equals(matchStr)){
                    String topOperator = null;
                    while (!(topOperator = operator.pop()).equals("(")){
                        suffix.add(topOperator);
                    }
                } else {//读到运算符号
                    while (!operator.isEmpty() && operatorPriority(matchStr) <= operatorPriority(operator.peek())){
                        suffix.add(operator.pop());
                    }
                    operator.push(matchStr);
                }
            } else {//遇到数值，直接输出
                suffix.add(matchStr);
            }
        }

        if (!operator.isEmpty()) {
            while (!operator.isEmpty()){
                suffix.add(operator.pop());
            }
        }

        return suffix;
    }

    /**
     * 设置运算符号的优先级(数值越大优先级越高)
     * @param matchStr
     * @return
     * @throws Exception
     */
    public static int operatorPriority(String matchStr) throws Exception {
        if (matchStr == null) {
            return 0;
        }

        switch (matchStr) {
            case "(" :
                return 1;
            case "+" :
            case "-" :
                return 2;
            case "*" :
            case "/" :
                return 3;
            default : break;
        }

        throw new Exception("报送数据校验计算表达式中运算符不符合规范(+-*/)!");
    }

    /**
     * 后缀表达式计算结果值
     * @param suffixExpress
     * @return
     * @throws Exception
     */
    public static double calculate(ArrayList<String> suffixExpress) throws Exception{
        double result;
        //当数组只有一位时则不进行计算直接返回值
        result = (suffixExpress.size()==1)?Double.parseDouble(suffixExpress.get(0)):0.0;
        Stack<Double> values = new Stack<>();
        for (String str : suffixExpress) {
            //遇到符号取出栈中前两个数值进行计算
            if (str.matches("[+\\-*/]")) {
                double second = values.pop();
                double first = values.pop();
                result = calculateTwoValue(first, second, str);
                values.push(result);
            } else {
                values.push(Double.parseDouble(str));
            }
        }
        return result;
    }

    /**
     * 根据运算符号计算两值的结果值
     * @param first
     * @param second
     * @param operator
     * @return
     * @throws Exception
     */
    public static double calculateTwoValue(double first, double second, String operator) throws Exception {
        switch (operator) {
            case "+" :
                return first + second;
            case "-" :
                return first - second;
            case "*" :
                return first * second;
            case "/" :
                return first / second;
            default : break;
        }
        throw new Exception("报送数据校验计算表达式中运算符不符合规范(+-*/)!");
    }

    //public static boolean

}
