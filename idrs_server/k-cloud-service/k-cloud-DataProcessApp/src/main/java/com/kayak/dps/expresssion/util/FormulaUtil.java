package com.kayak.dps.expresssion.util;

import com.kayak.core.system.SysBeans;
import com.kayak.dps.check.dao.DataValidateDao;
import com.kayak.dps.check.exception.DataValidateExecuteException;
import com.kayak.dps.check.exception.ReportDataValidateExecuteException;
import com.kayak.dps.check.util.PrimaryDataCheckUtil;
import com.kayak.dps.expresssion.exception.CalculationFormulaExecuteException;
import com.kayak.dps.expresssion.exception.CalculationFormulaFormatException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kayak.dps.expresssion.util.DataCalculationUtil.expressCalculate;

public class FormulaUtil {

    private static Logger log = LogManager.getLogger(FormulaUtil.class);

    public static String IDENTITY_CHECK_REGEX = "(^\\d{18}$)|(^\\d{15}$)";//身份证正则

    public static String EMAIL_CHECK_REGEX = "^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$]";//邮箱正则表达式

    public static String NUMERICAL_CHECK_REGEX_01 = "^[-\\+]?[\\d]*$";//整数正则

    public static String NUMERICAL_CHECK_REGEX_02 = "^\\d*(\\.\\d{1,20})?$";//小数位校验,最多20位

    public static String DATA_TYPE_RMB = "^\\d*(\\.\\d{2})?$";//人民币金额,2位小数

    public static String DATA_TYPE_NUM_WORD = "^[a-zA-Z0-9]*$";//英文和数字


    private static DataValidateDao dataValidateDao = SysBeans.getBean("dataValidateDao");

    /**
     * 01-非空校验(值不能为空)
     * @param check_val 校验对象
     * @return 返回结果为判断校验值域预期值是否符合
     */
    public static boolean isnull (Object check_val){
        if (check_val == null || "null".equalsIgnoreCase(String.valueOf(check_val)) || "".equals(String.valueOf(check_val))) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 非空校验(值必须为空)
     * @param check_val 校验对象
     * @return 返回结果为判断校验值域预期值是否符合
     */
    public static boolean nullCheck (Object check_val){
        if (check_val == null || "null".equalsIgnoreCase(String.valueOf(check_val)) || "".equals(String.valueOf(check_val))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 校验字符长度是否满足条件
     * @param check_val 校验对象
     * @param express 校验表达式
     * @return
     * * @throws CalculationFormulaExecuteException
     */
    public static boolean charLengthCheck(Object check_val, String express) throws CalculationFormulaFormatException {
        if ("null".equalsIgnoreCase(String.valueOf(check_val))) {
            return true;
        }

        int len = String.valueOf(check_val).length();
        int ex_len = 0;//定义初始长度为0
        try {
            ex_len = Integer.parseInt(express.substring(express.indexOf("(")+1, express.indexOf(")")));
        } catch (Exception e) {
            throw new CalculationFormulaFormatException("表达式校验公式数据转换异常: 定义方法名 length " + e.getMessage());
        }
        return (len <= ex_len);
    }

    /**
     * 校验字符及数字固定长度是否满足条件
     * @param check_val 校验对象
     * @param express 校验表达式
     * @return
     * * @throws CalculationFormulaExecuteException
     */
    public static boolean fixedLengthCheck(Object check_val, String express) throws CalculationFormulaFormatException {
        int len = String.valueOf(check_val).length();
        int fix_len = 0;//定义初始长度为0
        try {
            fix_len = Integer.parseInt(express.substring(express.indexOf("(")+1, express.indexOf(")")));
        } catch (Exception e) {
            throw new CalculationFormulaFormatException("表达式校验公式数据转换异常: 定义方法名 length " + e.getMessage());
        }
        return (len <= fix_len);
    }

    /**
     * 校验数字长度是否满足条件
     * @param check_val 校验对象
     * @param express 校验表达式
     * @return
     * * @throws CalculationFormulaExecuteException
     */
    public static boolean numberLengthCheck(Object check_val, String express) throws CalculationFormulaFormatException {
        String num_exp = express.substring(express.indexOf("(")+1, express.indexOf(")"));;//定义初始长度为0

        try{
            //首先校验传入对象是否为数值类型
            boolean isNumber = dataNumericalValidateCheck(String.valueOf(check_val));
            if (isNumber && !num_exp.contains(",") && !String.valueOf(check_val).contains(".")) {//若为整数类型，则校验长度
                return (String.valueOf(check_val).length() == Integer.parseInt(num_exp));
            } else if (isNumber && express.contains(",")) {//存在小数位数校验
                String[] numStr = num_exp.split(",");
                try{
                    if(numStr[1].equals("0") && !String.valueOf(check_val).contains(".")){//期望值存在小数位数为0,默认为整数类型
                        return Integer.parseInt(numStr[0]) == String.valueOf(check_val).length();
                    }else if(numStr[1].equals("0") && String.valueOf(check_val).contains(".")){//小数位数为0单校验对象包含小数返回false
                        return false;
                    }else if(Integer.parseInt(numStr[1]) > 0 && String.valueOf(check_val).contains(".")){//期望值存在小数位数情况,切校验对象含小数
                        String[] check_val_str = String.valueOf(check_val).split("\\.");
                        //校验整数位和小数位的长度符合期望值则返回true,其他返回false
                        return ((Integer.parseInt(numStr[0]) - Integer.parseInt(numStr[1])) >= check_val_str[0].length() &&
                                Integer.parseInt(numStr[1]) >= check_val_str[1].length());
                    }else{
                        return false;
                    }
                }catch (Exception e){
                    throw new CalculationFormulaFormatException("表达式校验公式数值转换异常: 定义方法名 valueFormat " + e.getMessage());
                }
            }else{
                throw new CalculationFormulaFormatException("表达式校验公式数据格式不正确: 定义方法名 valueFormat 传入校验对象非数值类型");
            }
        }catch (Exception e){
            throw new CalculationFormulaFormatException("表达式校验公式数据转换异常: 定义方法名 length " + e.getMessage());
        }
    }

    /**
     * 校验对象是否满足日期格式
     * @param check_val
     * @param express 校验表达式
     * @return
     * @throws CalculationFormulaExecuteException
     */
    public static boolean dateFormat(Object check_val, String express) throws CalculationFormulaFormatException {
        String date = "";
        String fMat = express.substring(express.indexOf("(")+1, express.indexOf(")"));
        SimpleDateFormat df = new SimpleDateFormat(fMat);
        try{
            date = df.format(df.parse(String.valueOf(check_val)));
        }catch (Exception e){
            throw new CalculationFormulaFormatException("表达式校验公式日期格式转换异常: 定义方法名 dateFormat " +e.getMessage());
        }
        return String.valueOf(check_val).equalsIgnoreCase(date);
    }

    /**
     * 校验对象是否满足邮箱格式
     * @param check_val 校验值
     * @return
     * @throws CalculationFormulaExecuteException
     */
    public static boolean emailFormat(String check_val) throws CalculationFormulaFormatException {
        return Pattern.matches(EMAIL_CHECK_REGEX, check_val);
    }

    /**
     * 数据类型校验
     * @param check_val 校验表达式
     * @return
     * @throws Exception
     */
    public static boolean dataTypeCheck(String check_val, String express) throws Exception {
        String type = express.substring(express.indexOf("(")+1, express.indexOf(")"));

        boolean check_result = false;
        switch (type) {
            case "RMB":
                check_result = Pattern.matches(DATA_TYPE_RMB, check_val);
                break;
            case "NUM|WORD":
                check_result = Pattern.matches(DATA_TYPE_NUM_WORD, check_val);
                break;
            case "-":
                check_result = Pattern.matches(NUMERICAL_CHECK_REGEX_01, check_val);
                break;
            default:
                break;
        }

        return check_result;
    }

    /**
     * 校验字符是否含有中文
     * @param check_val 校验字符
     * @return
     * @throws CalculationFormulaExecuteException
     */
    public static boolean ChineseCheck(Object check_val) throws CalculationFormulaFormatException {
        String checkStr = String.valueOf(check_val);
        try{
            Pattern pattern = Pattern.compile("^[\u4E00-\u9FA5]+$");//正则匹配字符中所有[]的内容
            Matcher matcher = pattern.matcher(checkStr);
            if (matcher.find()) {//若匹配上了,则说明含有中文,返回false
                return false;
            } else {
                return true;
            }
        }catch (Exception e){
            throw new CalculationFormulaFormatException("表达式校验校验字符是否含有中文异常: 定义方法名 dateFormat " +e.getMessage());
        }
    }

    /**
     * 03-字段格式及长度校验:C()-字符 / N()-数值
     * 校验对象是否满足传入字符格式
     * @param check_val
     * @param expected_val
     * @return
     * @throws CalculationFormulaFormatException
     */
    public static boolean valueFormat(Object check_val, String expected_val) throws CalculationFormulaFormatException {
        int expected_length = 0;
        String ex_check_str;//期望校验字符
        try{
            ex_check_str = (expected_val.substring(expected_val.indexOf("(")+1, expected_val.indexOf(")")));
            expected_length = Integer.parseInt(ex_check_str);
        }catch (Exception e){
            throw new CalculationFormulaFormatException("表达式校验公式数值转换异常: 定义方法名 valueFormat " + e.getMessage());
        }

        //校验传入对象是否满足格式要求
        if ("C".equalsIgnoreCase(expected_val.substring(0, 1)) && !ex_check_str.contains(",")) {//截取校验公式的第一个字符判断校验对象的类型C-字符(不含分隔符,) N-数值
            return (String.valueOf(check_val).length() == expected_length);
        } else if ("N".equalsIgnoreCase(expected_val.substring(0, 1)) && dataNumericalValidateCheck(String.valueOf(check_val))) {//数值类型长度及小数位数校验
            //首先校验传入对象是否为数值类型
            boolean isNumber = dataNumericalValidateCheck(String.valueOf(check_val));
            if (isNumber && !ex_check_str.contains(",") && !String.valueOf(check_val).contains(".")) {//若为整数类型，则校验长度
                return (String.valueOf(check_val).length() == expected_length);
            } else if(isNumber && ex_check_str.contains(",")) {//存在小数位数校验
                String[] numStr = ex_check_str.split(",");
                try {
                    if (numStr[1].equals("0") && !String.valueOf(check_val).contains(".")) {//期望值存在小数位数为0,,默认为整数类型
                        return Integer.parseInt(numStr[0]) == String.valueOf(check_val).length();
                    } else if(numStr[1].equals("0") && String.valueOf(check_val).contains(".")) {//小数位数为0单校验对象包含小数返回false
                        return false;
                    } else if(Integer.parseInt(numStr[1]) > 0 && String.valueOf(check_val).contains(".")) {//期望值存在小数位数情况,切校验对象含小数
                        String[] check_val_str = String.valueOf(check_val).split(".");
                        //校验整数位和小数位的长度符合期望值则返回true,其他返回false
                        return ((Integer.parseInt(numStr[0]) - Integer.parseInt(numStr[1])) >= check_val_str[0].length() && Integer.parseInt(numStr[1]) >= check_val_str[1].length());
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    throw new CalculationFormulaFormatException("表达式校验公式数值转换异常: 定义方法名 valueFormat " + e.getMessage());
                }
            }else{
                throw new CalculationFormulaFormatException("表达式校验公式数据格式不正确: 定义方法名 valueFormat 传入校验对象非数值类型");
            }
        }else{
            throw new CalculationFormulaFormatException("表达式校验公式数据格式不正确: 定义方法名 valueFormat");
        }
    }

    /**
     * 06-数值类型正则校验(调整前暂时未用)
     * @param check_val 需要校验的字符
     * @return
     * @throws Exception
     */
    public static boolean dataNumericalValidateCheck(String check_val) {
        String num_val;
        try{
            num_val = new BigDecimal(check_val).toString();
        }catch (Exception e){
            //校验不通过时返回不符合校验
            return false;
        }
        //校验数值(整数型或小数型)是否符合正则
        if (Pattern.matches(NUMERICAL_CHECK_REGEX_01, num_val) || Pattern.matches(NUMERICAL_CHECK_REGEX_02, num_val)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 06-数字校验(数值范围限制校验)
     * @param check_exp 校验表达式
     * @param check_val 校验指标值
     * @return
     * @throws Exception
     */
    public static boolean numberRangeCheck(Object check_val, String check_exp) {
        String express_type = check_exp.substring(0,1);//截取公式首位字符判断校验类型:R-数字值域判断
        if("R".equalsIgnoreCase(express_type) ){
            String left_symbol = check_exp.substring(1, 2);
            String left_val = check_exp.substring(check_exp.indexOf(left_symbol)+1, check_exp.indexOf(","));
            String right_symbol = check_exp.substring(check_exp.length()-1);
            String right_val = check_exp.substring(check_exp.indexOf(",")+1, check_exp.indexOf(right_symbol));

            return (SymbolTransform(left_symbol, String.valueOf(check_val), left_val) && SymbolTransform(right_symbol, String.valueOf(check_val), right_val));
        } else {
            throw new CalculationFormulaFormatException("报送数据数字校验表达式不符合规范:" + check_exp);
        }
    }

    /**
     * 重复性校验:(仅一维报表存在该场景)
     * 查询校验字段总数与distinct数量是否相同
     * @param express 校验表达式
     * @param params1 报送数据集合 Map<table, List<Map<column, value>>>
     * @param report_date 报送日期
     * @return
     */
    public static boolean repeatCheck(String express, Map<String, Object> params1, String report_date) throws Exception {
        String[] expStr = new String[10];
        String expressStr = "";
        boolean flag = true;
        String type = "";

        if (express.contains(" and ") || express.contains(" AND ")) {//用于校验多个字段都不能重复
            expStr = express.replace(" and ", "|").replace(" AND ", "|").split("\\|");
            type = "&";
            flag = true;
        } else if (express.contains(" or ") || express.contains(" OR ")) {//用于校验字段不能同时重复
            expStr = express.replace(" or ", "|").replace(" OR ", "|").split("\\|");
            type = "|";
            flag = false;
        } else {
            expStr[0] = express;
        }

        if ("&".equalsIgnoreCase(type)) {
            /** 用于校验同一条数据多个不同字段间不能重复 */
            String report_table = "";//报表名称
            String[] columnStr = new String[expStr.length];
            for (int i=0; i<expStr.length-1; i++) {//将每个需校验字段组装入columnStr
                expressStr = expStr[i].substring(expStr[i].indexOf("(")+1, expStr[i].indexOf(")"));
                String[] paramsStr = expressStr.split(",");
                if (paramsStr.length > 1) {//若不存在,则不符合公式格式
                    report_table = paramsStr[0];
                    columnStr[i] = paramsStr[1];
                } else {
                    throw new DataValidateExecuteException("报送数据重复性校验表达式不符合规范");
                }
            }

            List<Map<String, Object>> mapList = (List<Map<String, Object>>) params1.get(report_table);
            for (Map<String, Object> maps : mapList) {
                for (int i=0; i<expStr.length-1; i++) {
                    if(maps.get(columnStr[i]).equals(maps.get(columnStr[i+1]))) {
                        flag = false;
                    }
                }
            }
        } else if ("|".equalsIgnoreCase(type)) {
            /** 用于校验字段不能同时重复 */
            flag = false;//默认false,|条件下若有一项为true，则返回true
            String concat_column = "";
            String report_table = "";
            for (int i=0; i<expStr.length; i++) {
                expressStr = expStr[i].substring(expStr[i].indexOf("(")+1, expStr[i].indexOf(")"));
                String[] paramsStr = expressStr.split(",");
                if (paramsStr.length > 1) {//若不存在,则不符合公式格式
                    concat_column = concat_column + paramsStr[1] + ",";
                    report_table = paramsStr[0];
                } else {
                    throw new DataValidateExecuteException("报送数据重复性校验表达式不符合规范");
                }
            }
            concat_column = concat_column.substring(0, concat_column.length()-1);
            if(dataValidateDao.getCheckColumnTotalNum(report_table, concat_column, report_date)) {
                flag = true;//默认false,|条件下结果仅有一项，返回值即为重复性校验结果,true即通过，否则为false
            }
        } else {
            expressStr = expStr[0].substring(expStr[0].indexOf("(")+1, expStr[0].indexOf(")"));
            String[] paramsStr = expressStr.split(",");
            if (paramsStr.length > 1) {//若不存在,则不符合公式格式
                if(!dataValidateDao.getCheckColumnTotalNum(paramsStr[0], paramsStr[1], report_date)) {
                    flag = false;//默认true,&条件下若有一项为false，则返回false
                }
            } else {
                throw new DataValidateExecuteException("报送数据重复性校验表达式不符合规范");
            }
        }
        return flag;
    }

    /**
     * 02-值域校验(二维报表)
     * @param check_val
     * @param expected_val
     * @return
     */
    public static boolean dictRange(Object check_val, String expected_val) throws Exception {
        String express_type = expected_val.substring(0,1);//截取公式首位字符判断校验类型:D-字典取值判断
        if("D".equalsIgnoreCase(express_type)){
            return dataValidateDao.dataDictRangeCheck(String.valueOf(check_val), expected_val);
        }else{
            throw new CalculationFormulaFormatException("报送数据值域校验表达式不符合规范:");
        }
    }

    /**
     *
     * @param symbol 比较符号
     * @param validate_value 校验对象值
     * @param check_value 字典对象值
     * @return
     */
    public static boolean SymbolTransform(String symbol, String validate_value, String check_value) throws CalculationFormulaFormatException{
        if (("+N".equals(check_value) && ")".equals(symbol)) || ("-N".equals(check_value) && "(".equals(symbol))) {
            return true;/** 当 */
        }

        try {
            if("(".equalsIgnoreCase(symbol)){//左括号:对象是否比比较值大
                return (Float.parseFloat(validate_value) > Float.parseFloat(check_value));
            }else if("[".equalsIgnoreCase(symbol)){
                return (Float.parseFloat(validate_value) >= Float.parseFloat(check_value));
            }else if(")".equalsIgnoreCase(symbol)){
                return (Float.parseFloat(check_value) > Float.parseFloat(validate_value));
            }else if("]".equalsIgnoreCase(symbol)){
                return (Float.parseFloat(check_value) >= Float.parseFloat(validate_value));
            }else{
                throw new CalculationFormulaFormatException("表达式校验公式比较符号暂未识别:" + symbol);
            }
        } catch (Exception e){
            throw new CalculationFormulaFormatException("表达式校验公式数值转换异常: String->Float 转换值:" + check_value);
        }
    }

    /**
     * 校验身份证信息是否符合输入规则
     * @param validate_value
     * @return
     */
    public static boolean identityRegexValidate(String validate_value){
        return Pattern.matches(IDENTITY_CHECK_REGEX, validate_value);
    }

    /**
     * 数据校验-计算公式校验(待补充:暂无设计思路,如何调用公式)
     * @param express 计算公式
     * @param allow_deviation 允许差值
     * @param express_symbol 公式符号
     * @param report_date 报送数据校验日期
     * @param params 报送数据校验日期
     * @return
     */
    public static boolean dataFormulaValidateCheck(String express, String allow_deviation, String express_symbol, String report_date, String report_table,
                                                   String coordinate_type, Map<String, Object> params) throws Exception {
        if ("1".equals(coordinate_type)) {
            return DataCalculationUtil.transferExpressToResultC1(express, allow_deviation, express_symbol, report_table, coordinate_type, params);
        } else if ("2".equals(coordinate_type)) {
            return DataCalculationUtil.transferExpressToResultC2(express, allow_deviation, express_symbol, report_date, coordinate_type, params);
        } else {
            return true;
        }
    }

    /**
     * 判断表达式是否满足判断结果及差值是否小于等于允许误差
     * @param express_symbol 公式运算符号
     * @param left 公式左侧值
     * @param right 公式右侧值
     * @return
     */
    public static boolean judgeCalculationResults (String express_symbol, BigDecimal left, BigDecimal right, String allow_deviation) {
        if(left.subtract(right).abs().compareTo(new BigDecimal(allow_deviation)) == 1){
            //判断允许误差值，暂时舍弃
            //return false;
        }

        if ("=".equals(express_symbol)){
            return (left.compareTo(right) == 0);
        } else if (">".equals(express_symbol)) {
            return (left.compareTo(right) == 1);
        } else if ("<".equals(express_symbol)) {
            return (left.compareTo(right) == -1);
        } else if (">=".equals(express_symbol) || "≥".equals(express_symbol)) {
            return (left.compareTo(right) >= 0);
        } else if ("<=".equals(express_symbol) || "≤".equals(express_symbol)) {
            return (left.compareTo(right) <= 0);
        }
        return false;
    }


    /**
     * 判断字段联动校验表达式校验是否满足
     * @param express
     * @param report_table
     * @param params1
     * @return
     * @throws ReportDataValidateExecuteException
     */
    public static boolean linkedNullCheck(String express, String report_table, Map<String, Object> params1) throws ReportDataValidateExecuteException {
        String[] conditionStr = new String[3];//每一个元素都是一个校验表达式
        String[] splitStr = express.split("\\?");//左边为条件,右边校验结果
        String condition = splitStr[0];//验证条件
        String result = splitStr[1];//校验结果
        String syb = "";
        boolean check_result = true;
        boolean final_flag = false;

        //对条件进行拆解，若含有and/or时需要满足多个条件
        if (condition.contains(" and ") || condition.contains(" AND ")) {
            conditionStr = condition.replace(" and ", "|").replace(" AND ", "|").split("\\|");//每一个字符都是一个表达公式
            check_result = true;
            syb = "&";
        } else if (condition.contains(" or ") || condition.contains(" OR ")) {
            conditionStr = condition.replace(" or ", "|").replace(" OR ", "|").split("\\|");
            check_result = false;
            syb = "|";
        } else {
            conditionStr[0] = condition;
        }

        //此处校验条件是否满足,若条件满足则校验结果，否则不校验，直接跳出返回true;
        for (int i=0; i<conditionStr.length; i++) {
            if(conditionStr[i] == null) {
                continue;//防止为空
            }
            String symbol = PrimaryDataCheckUtil.getUsualSymbolByExpress(conditionStr[i]);//获取每一个条件表达式的符号:</>/<=/>=
            String[] expStr = conditionStr[i].split(symbol);//每一个表达式左右两侧切分
            String leftStr = expStr[0].replace("\"", "");//表达式左侧(去除双引号)
            String right_value = expStr[1].replace("\"", "");//表达式右侧(去除双引号)

            String[] elementStr = leftStr.substring(leftStr.indexOf("(")+1, leftStr.indexOf(")")).split("\\,");//表达式左侧元素
            String left_value = String.valueOf(params1.get(elementStr[1]));//表达式左侧值

            if ("=".equals(symbol)) {//等式
                if (!right_value.equals(left_value) && "&".equals(syb)) {
                    check_result = false;//左右表达式相同
                } else if (right_value.equals(left_value) && "|".equals(syb)) {
                    check_result = true;
                } else if (right_value.equals(left_value) && "".equals(syb)) {
                    check_result = true;
                }
            } else {//不等式
                boolean flag = false;
                if (">".equals(symbol)) {
                    flag = (left_value.compareTo(right_value) == 1);
                } else if ("<".equals(symbol)) {
                    flag = (left_value.compareTo(right_value) == -1);
                } else if (">=".equals(symbol)) {
                    flag = (left_value.compareTo(right_value) >= 0);
                } else if ("<=".equals(symbol)) {
                    flag = (left_value.compareTo(right_value) <= 0);
                } else if ("<>".equals(symbol)) {
                    flag = (!left_value.equalsIgnoreCase(right_value));//不等于
                }

                if ("&".equals(syb) && !flag) {
                    check_result = false;
                } else if ("|".equals(syb) && flag) {
                    check_result = true;
                }else if ("".equals(syb)) {
                    check_result = flag;
                }
            }
        }

        if (!check_result) {
            /** 若字段联动校验条件表达式满足，则校验结果表达式是否满足；若字段联动校验条件表达式不满足,则返回true且不进行结果表达式校验 */
            return true;
        } else {
            String[] resultElementStr = result.substring(result.indexOf("(") + 1, result.indexOf(")")).split("\\,");//结果表达式元素
            String right_value = String.valueOf(params1.get(resultElementStr[1]));//结果表达式的取值

            /** 若条件表达式满足，则校验结果表达式 */
            if (express.contains("LK_NULL(")) {
                final_flag = nullCheck(right_value);//结果表达式必为空
            } else if (express.contains("LK_NESS(")) {
                final_flag = isnull(right_value);//结果表达式必填
            } else if (express.contains("LK_COND(")) {
                final_flag = expressCheck(result, params1);//结果表达式成立(含不等式、等式)
            } else if (express.contains("LK_CHAR(")) {
                final_flag = charExpressCheck(result, params1);//结果表达式成立(字符)
            }
        }
        return final_flag;
    }

    /**
     * 校验表达式是否成立(一维报表报送数据)
     * @param express
     * @param dataMap 每行校验数据的map集合
     * @return
     * @throws ReportDataValidateExecuteException
     */
    public static boolean expressCheck(String express, Map<String, Object> dataMap) throws ReportDataValidateExecuteException {
        String symbol = PrimaryDataCheckUtil.getUsualSymbolByExpress(express);//获取每一个条件表达式的符号:</>/<=/>=
        log.info("一维报送数据校验表达式为:" + express);
        boolean result = true;
        String infix_express = express;
        String value_str = infix_express.replace("+", "|").replace("-", "|").replace("*", "|").
                replace("/", "|").replace("=", "|");
        String[] value_fields = value_str.split("\\|");

        try {
            for (String value_field : value_fields) {
                Pattern pattern = Pattern.compile("^LK_COND\\(.*\\,.*\\)");
                Matcher match = pattern.matcher(value_field);
                while (match.find()) {
                    String contents = match.group().substring(match.group().indexOf("(")+1, match.group().length() -1);//获取value方法入参参数
                    String[] content_field = contents.split(",");
                    Object value_obj = dataMap.get(content_field[1]);//获取数据
                    infix_express = infix_express.trim().replace(match.group(), String.valueOf(value_obj));//将原表达式两端需要计算的value表达式替换成计算结果
                }
            }
            String[] expressStr = infix_express.split(symbol);//分隔表达式两端分别计算进行比较
            result = FormulaUtil.judgeCalculationResults(symbol, BigDecimal.valueOf(expressCalculate(expressStr[0])), BigDecimal.valueOf(expressCalculate(expressStr[1])), "0");
        } catch (Exception e) {
            throw new CalculationFormulaExecuteException("一维报表报送数据字段联动校验异常: " + e.getMessage());
        }
        return result;
    }

    /**
     * 判断一维报表报送数据字段联动校验表达式校验
     * @param express
     * @param dataMap
     * @return
     * @throws ReportDataValidateExecuteException
     */
    public static boolean charExpressCheck(String express, Map<String, Object> dataMap) throws ReportDataValidateExecuteException {
        String symbol = PrimaryDataCheckUtil.getCharSymbolByExpress(express);//获取每一个条件表达式的符号:<>/=
        String[] charStr = express.split(symbol);
        String leftStr = charStr[0]; String rightStr = charStr[1];
        String[] leftElement = leftStr.substring(leftStr.indexOf("(")+1, leftStr.indexOf(")")).split("\\,");
        String leftValue = String.valueOf(dataMap.get(leftElement[1]));
        if (rightStr.contains("LK_CHAR(") && rightStr.split(",").length > 1) {
            //表达式右边也为计算公式
            String[] rightElement = rightStr.substring(rightStr.indexOf("(")+1, rightStr.indexOf(")")).split("\\,");
            String rightValue = String.valueOf(dataMap.get(rightElement[1]));
            if (("=".equals(symbol) && leftValue.equalsIgnoreCase(rightValue)) || ("<>".equals(symbol) && !leftValue.equalsIgnoreCase(rightValue))) {
                return true;
            } else {
                return false;
            }
        } else {
            //表达式右边为字符值
            if (("=".equals(symbol) && leftValue.equalsIgnoreCase(rightStr)) || ("<>".equals(symbol) && !leftValue.equalsIgnoreCase(rightStr))) {
                return true;
            } else {
                return false;
            }
        }
    }
}
