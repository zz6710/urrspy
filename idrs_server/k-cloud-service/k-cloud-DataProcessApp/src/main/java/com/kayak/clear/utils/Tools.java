package com.kayak.clear.utils;

import com.kayak.clear.constants.GlobalContents;
import com.kayakwise.kcloud.batch.exception.TransException;
import com.kayakwise.kcloud.db.SqlRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
    private final static Logger log = LoggerFactory.getLogger(Tools.class);

    public static final String[] emptyArrayString = new String[]{};

    public static final Integer[] emptyArrayInteger = new Integer[]{};

    public static final byte[] emptyByteArray = new byte[]{};

    /**
     * 值为0的Short实例
     */
    public static final Short zeroShort = 0;

    public static final Date emptyDate = new Date(-28800000);

    /**
     * 取得new Date(0l)对象 在系统中,将把该对象作为空白日期对象处理
     */
    public static Date getEmptyDate() {
        return emptyDate;
    }

    private static SimpleDateFormat DATE_FORMATTER_DATE = new SimpleDateFormat("yyyyMMdd");

    private static SimpleDateFormat DATE_FORMATTER_TIME = new SimpleDateFormat("HHmmss");

    private static SimpleDateFormat DATE_FORMATTER_CURRENT_TIME = new SimpleDateFormat("yyyyMMddHHmmss");

    private static SimpleDateFormat DATE_FORMATER_CURRENT_HOUR_MIN = new SimpleDateFormat("HHmm");

    public static final String IS_NUM_REGEX = "[-+]?\\d+\\.\\d*|[-+]?\\d*\\.\\d+";

    /**
     * 通过key值，从Map中获取对应的值，并返回字符串 若Map非空，未找到key的对应值时，正常返回空字符串
     *
     * @param map
     * @param key
     * @return
     */
    public static String getObjectValue(Map<String, Object> map, String key) {
        if (map == null || map.isEmpty()) {
            throw new TransException("999999", "输入Map值为空");
        }

        Object object = map.get(key);
        if (object == null) {
            return "";
        } else {
            return object.toString();
        }
    }

    /**
     * @param currday
     * @param brithday
     * @return
     */
    public static Integer getCustomAgeByBrithday(Date currday, Date brithday) {
        Integer  age  = 0;
        Calendar born = Calendar.getInstance();
        Calendar now  = Calendar.getInstance();
        if (currday == null || brithday == null) {
            throw new TransException("PROD66", "日期输入不完整，无法获取客户年龄");
        }
        if (DateUtils.getDateFormat(brithday) != null) {
            born.setTime(brithday);
            now.setTime(currday);
            if (born.after(now)) {
                throw new TransException("PROD67", "客户输入出生日期错误，不能大于当前日期");
            }

            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            if (now.get(Calendar.DAY_OF_YEAR) < born.get(Calendar.DAY_OF_YEAR)) {
                age -= 1;
            }
        }
        return age;
    }

    /**
     * @param brithday
     * @return
     */
    public static Integer getCustomAgeByBrithday(String brithday) {
        return getCustomAgeByBrithday(new Date(), DateUtils.getDateFormat(brithday));
    }

    /**
     * 如果str为空(null), 或者将str去掉首尾空格后,等于空字符串(""), 则返回假
     */
    public static boolean isNotBlank(String str) {
        if (str == null || "".equals(str.trim()))
            return false;
        else
            return true;
    }

    /**
     * 如果str为空(null), 或者将str去掉首尾空格后,等于空字符串(""), 则返回真
     */
    public static boolean isBlank(String str) {
        return !isNotBlank(str);
    }

    public static boolean isObjBlank(Object obj) {
        if (obj == null || "".equals(obj.toString().trim()))
            return true;
        else
            return false;
    }

    public static boolean isObjNotBlank(Object obj) {
        return !isObjBlank(obj);
    }

    /**
     * 通过18位身份证号码获取15位身份证号码 18为身份证号码变15位身份证号码，去掉出生年份的前2位和最后一位校验码，
     * 如：18位身份证号码652422 19371015 132 9 降为15位身份证号码652422 371015 132
     */
    public static String getIdCode15(String idCode) {
        if (isBlank(idCode) || idCode.length() != 18) {
            throw new TransException("OTHR03", "身份证号码长度应该为18位");
        }
        return idCode.substring(0, 6) + idCode.substring(8, 17);
    }

    /**
     * 比较两个对象值的大小， 若: firstValue >= secondValue = true 若: firstValue <
     * secondValue = false;
     *
     * @param firstValue
     * @param secondValue
     * @return
     */
    public static boolean compare2Values(Long firstValue, Long secondValue) {
        return Long.compare(firstValue, secondValue) >= 0 ? true : false;
    }

    /**
     * 比较两个对象值的大小，若obj1 >= obj2 = true 若obj1 < obj2 = false;
     *
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean compare2Values(Object obj1, Object obj2) {
        if (obj1 == null || obj2 == null) {
            throw new TransException("OTHR04", "对象为空");
        }
        return compare2Values(Long.valueOf(obj1.toString()), Long.valueOf(obj2.toString()));
    }

    /**
     * Date类型转换成yyyyMMdd格式的字符串
     */
    public static String dt2date(Date date) {
        if (date == null)
            return "";
        return DATE_FORMATTER_DATE.format(date);
    }

    /**
     * Date类型转换成HHmmss格式的字符串
     */
    public static String dt2time(Date date) {
        if (date == null)
            return "";
        return DATE_FORMATTER_TIME.format(date);
    }

    /**
     * 时间格式化
     *
     * @param date
     * @return HHmm
     */
    public static String dt2currHourMin(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_FORMATER_CURRENT_HOUR_MIN.format(date);
    }

    /**
     * 匹配当前时间
     *
     * @param date
     * @return yyyyMMddHHmmss
     */
    public static String dt2currtime(Date date) {
        if (date == null)
            return "";
        return DATE_FORMATTER_CURRENT_TIME.format(date);
    }

    public static int str2int(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 字符串转换成Double类型
     */
    public static Double str2Double(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * 字符串转换成BigDecimal类型
     */
    public static BigDecimal str2BigDecimal(String str) {
        try {
            return new BigDecimal(str);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 字符串转换成BigDecimal类型
     */
    public static BigDecimal str2BigDecimal2(String str) {

        try {
            BigDecimal big = new BigDecimal(str);
            if (big.compareTo(BigDecimal.ZERO) > 0) {
                return big;
            }
            return new BigDecimal(GlobalContents.UNLIMITED);
        } catch (Exception e) {
            return new BigDecimal(GlobalContents.UNLIMITED);
        }

    }

    /**
     * 字符串转换成Long类型
     */
    public static Long str2Long(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return 0l;
        }
    }

    public static final BigDecimal ZERO_E_BIGDECIMAL = new BigDecimal("0E-10");

    /**
     * 转换为BigDecimal对象返回，如果obj为null，则返回0
     */
    public static BigDecimal obj2BigDecimal(Object obj) {
        if (obj == null || obj.equals(ZERO_E_BIGDECIMAL))
            return new BigDecimal(0);

        try {
            return (BigDecimal) obj;
        } catch (ClassCastException e) {
            return BigDecimal.valueOf(Double.parseDouble(obj.toString()));
        }
    }


    /**
     * 把从用sql语句里取得的对象，转换为Long对象返回，如果obj为null，则返回0
     */
    public static Long obj2Long(Object obj) {
        return obj2Long(obj, true);
    }

    /**
     * 把从用sql语句里取得的对象，转换为Long对象返回，如果obj为null，则根据 isDefault 决定是否返回 0
     * <br>isDefault = true  -> null -> 0</br>
     * <br>isDefault = false -> null</br>
     */
    public static Long obj2Long(Object obj, boolean isDefault) {
        if (obj == null) {
            if (isDefault) {
                return 0l;
            } else {
                return null;
            }
        }

        try {
            String str = obj.toString();

            if (str.contains("E")) {// 判断是否是科学技术法，如果是，则进行转换
                DecimalFormat df = new DecimalFormat("#.#");
                str = df.format(obj);
            }

            int idx = str.indexOf(".");
            if (idx > -1)
                str = str.substring(0, str.indexOf("."));
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            throw e;
        }
    }

    /**
     * 转换为String对象返回，如果obj为null，则返回空字符串“”
     */
    public static String obj2Str(Object obj) {
        if (objIsEmpty(obj)) {
            return "";
        }

        return obj.toString();
    }

    /**
     * 如果str为空(null), 或者将str去掉首尾空格后,等于空字符串(""), 则返回真
     * <pre>
     *  Tools.strIsEmpty(null)      = true
     *  Tools.strIsEmpty("")        = true
     *  Tools.strIsEmpty(" ")       = true
     *  Tools.strIsEmpty("bob")     = false
     *  Tools.strIsEmpty("  bob  ") = false
     * </pre>
     */
    public static boolean strIsEmpty(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查 str 不为空(null), 或者将str去掉首尾空格后,不等于空字符串("")
     * <pre>
     *  Tools.strIsEmpty(null)      = false
     *  Tools.strIsEmpty("")        = false
     *  Tools.strIsEmpty(" ")       = flase
     *  Tools.strIsEmpty("bob")     = true
     *  Tools.strIsEmpty("  bob  ") = true
     * </pre>
     */
    public static boolean strIsNotEmpty(String str) {
        return !Tools.strIsEmpty(str);
    }

    /**
     * 当str等于"true"(不区分大小写)返回true，否则返回false
     */
    public static boolean str2boolean(String str) {
        return "true".equalsIgnoreCase(str);
    }

    /**
     * 如果string!=null则返回trim后的string值，如果string==null则返回""<br />
     * 此方法不返回null
     */
    public static String trimString(String string) {
        if (string == null)
            return "";
        else
            return string.trim();
    }

    /**
     * 如果string!=null则返回trim后的string值，如果string==null则返回""<br />
     * 此方法不返回null
     */
    public static String trimString(Object obj) {
        if (obj == null)
            return "";
        else
            return obj.toString().trim();
    }

    /**
     * "yyyyMMdd"格式的字段串转换成Date类型
     */
    public static Date str2Dt1(String date) {
        Date             dt;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        try {
            dt = fmt.parse(date);
        } catch (Exception e) {
            dt = getEmptyDate();
        }
        return dt;
    }

    /**
     * 在date日期上加上(或减去)amount天后返回一个新的Date对象
     *
     * @param amount 正数则加，负数则减
     */
    public static Date dateAdd(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, amount);
        return cal.getTime();
    }

    /**
     * 在date日期上加上(或减去)amount天后返回一个新的Date对象
     *
     * @param dateStr yyyyMMdd 格式字符串
     * @param amount  调整天数
     * @return 天数调整后字符串
     */
    public static String dateAdd(String dateStr, int amount) {
        Date date = Tools.str2Dt1(dateStr);
        date = Tools.dateAdd(date, amount);
        return Tools.dt2date(date);
    }

    /**
     * 在date日期上加上(或减去)amount个月后返回一个新的Date对象
     *
     * @param amount 正数则加，负数则减
     */
    public static Date dateAddMonth(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, amount);
        return cal.getTime();
    }

    /**
     * 判断是否为空
     *
     * @param object
     */
    public static boolean objIsEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object.toString().equals("{}")) {
            return true;
        }
        if (object instanceof String) {
            if (object.toString().equals("null") || object.toString().equals("")) {
                return true;
            }
        }
        return false;
    }

    public static boolean objIsNotEmpty(Object object) {
        return !objIsEmpty(object);
    }

    /**
     * 根据日历的规则，为给定的日历字段添加或减去指定的时间量 例如，要从当前日历时间减去 5
     * 天，可以通过调用以下方法做到这一点：add(Calendar.DAY_OF_MONTH, -5)。
     *
     * @param date
     * @param field  日历字段
     * @param amount 为字段添加的日期或时间量
     * @return
     */
    public static Date getAddTime(Date date, int field, int amount) {
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        gregorianCalendar.add(field, amount);
        return gregorianCalendar.getTime();
    }

    /**
     * 将obj的值set到targetObj中
     *
     * @param targetObj 目标对象
     * @param obj       源对象
     * @param coverflag 是否覆盖原值
     * @return
     * @throws Exception
     */
    public static Object applyObject(Object targetObj, Object obj, Boolean coverflag) throws Exception {

        Method[]            targetObjmethods = targetObj.getClass().getMethods();
        Map<String, Object> targetValues     = new HashMap<>();
        Method[]            objmethods       = obj.getClass().getMethods();
        Map<String, Object> values           = new HashMap<>();
        for (Method method : objmethods) {
            String methodName = method.getName();
            if (methodName.startsWith("get") && !methodName.equals("getClass")) {
                // KLog.debug("obj methodName="+methodName);
                values.put(methodName.substring(4), method.invoke(obj));
            }
        }
        for (Method method : targetObjmethods) {
            String methodName = method.getName();
            if (methodName.startsWith("get") && !methodName.equals("getClass")) {
                // KLog.debug("targetObj methodName="+methodName);
                targetValues.put(methodName.substring(4), method.invoke(targetObj));
            }
        }
        for (Method method : targetObjmethods) {

            String methodName = method.getName();
            if (methodName.startsWith("set") && !methodName.equals("getClass")) {
                // KLog.debug("targetObj methodName="+methodName);
                if (values.get(methodName.substring(4)) != null) {
                    if (targetValues.get(methodName.substring(4)) != null) {
                        // 覆盖原值
                        if (coverflag) {
                            method.invoke(targetObj, values.get(methodName.substring(4)));
                        }
                    } else {
                        method.invoke(targetObj, values.get(methodName.substring(4)));
                    }

                }
            }
        }
        return targetObj;
    }

    /**
     * 根据成员属性的名称，通过JAVA反射，检查o实例里是否有field_name指定成员的getter方法， 有的话就返回这个getter方法
     */
    private static Method findGetterMethod(Object o, String field_name) {
        Method[] methods = o.getClass().getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (!methodName.startsWith("get") || methodName.equals("getClass")) {
                continue;
            }
            String fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
            if (fieldName.equals(field_name) && method.getParameterCount() == 0) {
                return method;
            }
        }
        return null;
    }

    private static Method findGetterMethod(Object o, Class<?> field_class) {
        Method[] methods = o.getClass().getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (!methodName.startsWith("get") || methodName.equals("getClass")) {
                continue;
            }
            if (method.getParameterCount() == 0 && method.getReturnType() == field_class) {
                return method;
            }
        }
        return null;
    }

    /**
     * 根据成员属性的名称，通过JAVA反射，检查o实例里是否有field_name指定成员的getter方法，<br>
     * 有的话就通过这个getter返回o实例里的field_name成员属性值，<br>
     * 如果没有getter方法，尝试直接获取成员值，如果取不到，则返回null
     */
    @SuppressWarnings("unchecked")
    public static <T> T invokeFieldValue(Object o, String field_name) {
        Method method = findGetterMethod(o, field_name);
        if (method != null) {
            try {
                return (T) method.invoke(o);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | ClassCastException e) {
                return null;
            }
        }

        try {
            Field field = o.getClass().getDeclaredField(field_name);
            if (field == null) {
                return null;
            }
            Field.setAccessible(new Field[]{field}, true);
            return (T) field.get(o);
        } catch (NoSuchFieldException | SecurityException | ClassCastException | IllegalArgumentException
                | IllegalAccessException e) {
            log.warn("Warn Msg:[{}]", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据实例的成员类型获取成员属性值
     */
    @SuppressWarnings("unchecked")
    public static <T> T invokeFieldValue(Object o, Class<T> field_class) {
        Method method = findGetterMethod(o, field_class);
        if (method != null) {
            try {
                return (T) method.invoke(o);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                return null;
            }
        }

        try {
            Field[] fields = o.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getType() == field_class) {
                    Field.setAccessible(new Field[]{field}, true);
                    return (T) field.get(o);
                }
            }
        } catch (SecurityException | ClassCastException | IllegalArgumentException | IllegalAccessException e) {
            log.warn("Warn Msg:[{}]", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将pojo对象转换成hashMap
     *
     * @param object
     * @return
     */
    public static void pojoToHashMap(Map<String, String> map, Object object) {

        try {
            Method[] methods = object.getClass().getMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                if (methodName.startsWith("get") && !methodName.equals("getClass")) {

                    try {
                        String fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
                        Object value     = method.invoke(object);
                        map.put(fieldName, value + "");
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        log.error("ErrorMsg:[{}]", e.getMessage(), e);
                    }
                }
            }
        } catch (Throwable e) {
            log.warn("ErrorMsg:[{}]", e.getMessage(), e);
        }

    }

    /**
     * 数字左补0
     *
     * @param num
     * @param digit 位数
     * @return
     */
    public static String padLeft(long num, int digit) {
        if (String.valueOf(num).length() >= digit) {
            return String.valueOf(num);
        }
        return String.format("%0" + digit + "d", num);
    }

    /**
     * 字符串右补空格
     *
     * @param str
     * @param digit
     * @return
     */
    public static String padRight(String str, int digit) {
        if (str.length() >= digit) {
            return str;
        }
        return String.format("%-" + digit + "s", str);
    }

    /**
     * 生成文件流水号
     *
     * @return 返回文件流水号
     */
    public static String genFileSerno() {
        return String.format("%024d", System.currentTimeMillis());
    }

    /**
     * 按照【份额的精度处理方式】处理份额精度
     *
     * @param appAmt
     * @param precisionProcess
     * @return
     */
    public static BigDecimal changeVol(BigDecimal appAmt, String precisionProcess) {
        if (Tools.strIsEmpty(precisionProcess)) {
            throw new TransException("M5Tools01", "份额的精度处理方式为空，不能创建份额明细");
        }
        BigDecimal changeAppAmt = BigDecimal.ZERO;
        //0:四舍五入  1:截位   2:进位
        if (BigDecimal.valueOf(appAmt.intValue()).compareTo(appAmt) == 0) {
            return appAmt;
        } else {
            //获取小数点后的数据
            String dian = appAmt.toString().substring(appAmt.toString().indexOf(".") + 1, appAmt.toString().length());
            if ("0".equals(precisionProcess)) {
                changeAppAmt = appAmt.divide(BigDecimal.ONE, 2, BigDecimal.ROUND_HALF_UP);
            } else if ("1".equals(precisionProcess)) {
                changeAppAmt = new BigDecimal(appAmt.intValue() + "." + (dian.length() > 2 ? dian.substring(0, 2) : dian));
            } else if ("2".equals(precisionProcess)) {
                if (dian.length() <= 2) {
                    changeAppAmt = appAmt;
                } else {
                    if ("0".equals("" + dian.charAt(2))) {
                        changeAppAmt = new BigDecimal(appAmt.intValue() + "." + dian.substring(0, 2));
                    } else {
                        changeAppAmt = new BigDecimal(appAmt.intValue() + "." + dian.substring(0, 2)).add(BigDecimal.valueOf(0.01));
                    }
                }
            } else {
                throw new TransException("M5Tools02", "份额的精度处理方式异常，不能创建份额明细");
            }
        }
        return changeAppAmt;
    }

    /**
     * 按照给定字段进行数据合并
     *
     * @param AllSubsDataList 分库汇总数据集合
     * @param keyList         对比字段：分库数据合并的Key
     * @param addKeyList      数据统计字段：分库数据合并时需要汇总统计的字段
     * @param otherKeyList    其他字段
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> List<T> mergeData(List<SqlRow> AllSubsDataList, List<String> keyList, List<String> addKeyList,
                                        List<String> otherKeyList, Class<T> clazz) throws Exception {

        if (AllSubsDataList == null || AllSubsDataList.size() == 0) {
            return new ArrayList<>();
        }

        if (keyList == null || keyList.size() == 0) {
            throw new Exception("没有汇总统计的key");
        }

        if (addKeyList == null || addKeyList.size() == 0) {
            throw new Exception("没有需要汇总统计的字段");
        }
        //拿到该类的所有方法
        Method[] methods = clazz.getMethods();

        //用于存放最终返回的数据
        List<T> mergeList = new ArrayList<T>();

        //用于处理集合中数据
        Map<String, T> map = new HashMap<String, T>();

        //循环数据集合列表
        for (SqlRow row : AllSubsDataList) {

            //拼装map中真正的key
            StringBuilder key = new StringBuilder();

            for (String k : keyList) {
                key.append(row.getString(k));
            }

            if (map.containsKey(key.toString())) {
                //map中包含此key
                //取出map该key值对应的对象
                T o = map.get(key.toString());
                //只需要处理addKeyList中的字段即可
                for (String addKey : addKeyList) {
                    for (Method method : methods) {
                        if (method.getName().startsWith("get") && method.getName().substring(3).toLowerCase().equals(addKey)) {
                            BigDecimal value = ((BigDecimal) (method.invoke(o))).add(row.getBigDecimal(addKey));
                            for (Method m : methods) {
                                if (m.getName().startsWith("set") && m.getName().substring(3).toLowerCase().equals(addKey)) {
                                    m.invoke(o, value);
                                }
                            }
                        }
                    }
                }
                map.put(key.toString(), o);
            } else {
                //map中不包含此key
                //创建对象
                T o = clazz.newInstance();
                for (Method method : methods) {
                    //遍历所有set方法
                    if (method.getName().startsWith("set")) {

                        //向对象中添加需要汇总统计的字段的值
                        for (String addKey : addKeyList) {
                            if (method.getName().substring(3).toLowerCase().equals(addKey)) {
                                method.invoke(o, row.getBigDecimal(addKey));
                            }
                        }

                        //向对象中添加key所有对应的字段的值
                        for (String k : keyList) {
                            if (method.getName().substring(3).toLowerCase().equals(k)) {
                                method.invoke(o, row.get(k));
                            }
                        }
                        //向对象中添加其他字段的值
                        for (String otherKey : otherKeyList) {
                            if (method.getName().substring(3).toLowerCase().equals(otherKey)) {
                                method.invoke(o, row.get(otherKey));
                            }
                        }
                    }
                }
                map.put(key.toString(), o);
            }
        }

        //遍历map，将map中的值移入mergeList中
        for (String key : map.keySet()) {
            mergeList.add(map.get(key));
        }
        return mergeList;
    }

    /**
     * 校验字符串是否为数字
     *
     * <pre>
     * Tools.strIsNumber(null)     = false
     * Tools.strIsNumber("")       = false
     * Tools.strIsNumber("  ")     = false
     * Tools.strIsNumber("12 3")   = false
     * Tools.strIsNumber("ab2c")   = false
     * Tools.strIsNumber("12-3")   = false
     * Tools.strIsNumber("123")    = true
     * Tools.strIsNumber("12.3")   = true
     * Tools.strIsNumber("-12.3")  = true
     * Tools.strIsNumber("+12.3")  = true
     * Tools.strIsNumber("-012.3") = true
     * Tools.strIsNumber("+012.3") = true
     * Tools.strIsNumber("1.")     = true
     * Tools.strIsNumber(".1")     = true
     * Tools.strIsNumber("-1.")    = true
     * Tools.strIsNumber("+.1")    = true
     * </pre>
     *
     * @param str 校验值
     * @return
     */
    public static boolean strIsNumber(String str) {
        return isMatch(IS_NUM_REGEX, str);
    }

    /**
     * 正则校验
     * <p>orginal 为 空 或 Null 返回 Falses</>
     *
     * @param regex   正则表达式
     * @param orginal 校验值
     * @return 正则结果
     */
    public static boolean isMatch(String regex, String orginal) {
        if (orginal == null || Tools.strIsEmpty(orginal)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum   = pattern.matcher(orginal);
        return isNum.matches();
    }

    /**
     * 数值按长度补九
     *
     * @param length 长度
     * @return 补9长度
     */
    public static String numToString(int num, Integer length) {
        String str = "";
        for (int i = 0; i < length; i++) {
            str += num + "";
        }
        return str;
    }

    /**
     * 获取时间
     *
     * @return YYYYmmddHHmmss
     */
    public static String getYYYYmmddHHmmss() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar   calender   = Calendar.getInstance();
        return dateFormat.format(calender.getTime());
    }

    /**
     * 获取时间
     *
     * @return YYYYmmdd
     */
    public static String getYYYYmmdd() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar   calender   = Calendar.getInstance();
        return dateFormat.format(calender.getTime());
    }

    /**
     * 获取时间
     *
     * @return HHmmss
     */
    public static String getHHmmss() {
        DateFormat dateFormat = new SimpleDateFormat("HHmmss");
        Calendar   calender   = Calendar.getInstance();
        return dateFormat.format(calender.getTime());
    }

    //上送值不为空且大于等于0则返回true 否则返回false
    public static boolean chkUnLimited(Object obj) {
        if (!objIsEmpty(obj) && BigDecimal.ZERO.compareTo(new BigDecimal(obj.toString())) <= 0) {
            return true;
        }
        return false;
    }

    //chkUnLimited校验通过返回object 否则返回 bigDecimal
    public static BigDecimal nvlBigDecimal(Object object, BigDecimal bigDecimal) {
        if (chkUnLimited(object)) {
            return new BigDecimal(object.toString());
        } else {
            return bigDecimal;
        }
    }

    //取余函数
    public static BigDecimal dbRemainder(BigDecimal divisor, BigDecimal stepApply) {

        return divisor.divideAndRemainder(stepApply)[1];
    }

    /**
     * 左补字符串
     * 中文字符按字节计算
     *
     * @param str    字符串
     * @param size   长度
     * @param padStr 补位字符串
     * @return
     */
    public static String alignRight(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }

        if ((padStr == null) || (padStr.length() == 0)) {
            padStr = " ";
        }

        int padLen = padStr.length();
        int strLen = str.length();
        int pads   = size - strLen;

        if (pads <= 0) {
            return str;
        }

        if (pads == padLen) {
            return padStr.concat(str);
        } else if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        } else {
            char[] padding  = new char[pads];
            char[] padChars = padStr.toCharArray();

            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }

            return new String(padding).concat(str);
        }
    }
    public static String getExceptionInfo(Exception e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        return baos.toString();
    }
}
