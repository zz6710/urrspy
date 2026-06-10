package com.kayak.dps.expresssion.enums;

/**
 * 条件表达式函数枚举
 */
public enum ExpressionEnum {

    SYSTEM_HOLD_SPLIT("SYSTEM_HOLD_SPLIT", "系统保留分割用关键字"),

    // 一维报表函数， 以下函数均为‘表名.字段’读取第一个数据，此数据为map中的自定义名称，非真实的表名。部分函数值作X100处理，是为了避免计算精度问题
    DICT("DICT", "DICT(表.字段名, 字典码制)校验是否为数据字典"),
    DICT_X("DICT_X", "DICT_X(表.字段名, 字典码制)多选情况下校验是否参数都在数据字典内,可多选不可重复"),
    DICT_S("DICT_S", "DICT_S(表.字段名, 字典码制)多选情况下校验是否参数都在数据字典内,可多选可重复"),
    NULL_N("NULL_N", "NULL(表.字段名) 非空时返回true，为空返回false"),
    NULL("NULL", "NULL(表.字段名) 非空时返回false，为空返回true"),
    COND("COND", "COND(表名.字段)返回字段,作乘以100的处理,同VALUE_P"),
    VALUE_A("VALUE_A", "VALUE_A(表名.字段)返回表中某字段数据"),
    VALUE_P("VALUE_P", "VALUE_P(表名.字段)返回表中某字段数据,作乘以100的处理"),
    KEY_D("KEY_D", "KEY_D(表名.字段)返回数据字典码值"),
    ABS("ABS", "ABS(表名.字段)返回绝对值"),
    CONT("CONT", "CONT(表名.字段)返回true即为包含,false为不包含"),
    CONT_N("CONT_N", "CONT_N(表名.字段,目标值)字段中是否包含目标值,返回true即为不包含,false为包含"),
    DATE_R("DATE_R", "DATE_R(表名.字段,起始日期,结束日期)判断校验日期是否在日期范围内,符合返回true,不符合返回false"),
    DATE("DATE", "DATE()校验日期格式,满足返回true,否则false"),
    DATE_S("DATE_S", "DATE_S(表名.字段,日期格式)校验日期格式,满足返回true,否则false"),
    DATE_LAST_DAY("DATE_LAST_DAY", "DATE_LAST_DAY(表名.字段,日期格式)校验日期是否为当月最后一个自然日,满足返回true,否则false"),
    REGREX("REGREX", "REGREX(表名.字段,ExpressionRegrexEnum枚举标识)校验字段是否符合规定编码格式"),
    VALUE_DM("VALUE_DM", "VALUE_DM(表名.字段)获取YYYY-mm-dd中日期mm部分"),
    MINUS_D("MINUS_D", "MINUS_D(表名.字段)返回特定日期类型中相差天数,日期类型:自然日/工作日"),
    DIF("DIF", "DIF(表名1.字段名1,表名1.字段名2)返回n个字段1是否唯一"),
    DIF_S("DIF_S", "DIF_S(表名.字段名)返回字段在系统表中是否唯一，不同于DIF函数"),
    CHAR("CHAR", "CHAR(表名.字段,长度)返回字符串是否匹配函数"),
    SUBSTR("SUBSTR", "SUBSTR(表名.字段,长度,截取位数)字段截取"),
    LENGTH_X("LENGTH_X", "LENGTH_X(表名.字段)返回字段的长度"),
    NUMBER_X("NUMBER_X", "NUMBER_X(表名.字段,总数位长度,小数位长度) 判断小于等于总位数长度和.0等于小数位长度"),
    NUMBER_D("NUMBER_D", "NUMBER_D(表名.字段,数位长度) 判断字段是否小于等于数位长度"),
    REPEAT_X("REPEAT_X", "REPEAT_X(表名.字段,分隔符) 判断多选字段值中不能重复"),
    REPEAT_NOT_N("REPEAT_NOT_N", "REPEAT_NOT_N(表名.字段,分隔符) 逗号间的多选字段值不能有空"),
    VALUE_REPEAT_NUM("VALUE_REPEAT_NUM", "VALUE_REPEAT_NUM(表名.字段,分隔符) 返回分隔符多选字段数量"),
    CHECK_EN("CHECK_EN", "CHECK_EN(表名.字段)校验字段值为英文或数字"),
    CHECK_N("CHECK_N", "CHECK_N(表名.字段) 校验字段值为数字"),
    CHECK_INT("CHECK_INT", "CHECK_INT(表名.字段) 校验字段值为整数"),
    CHECK_C("CHECK_C", "CHECK_C(表名.字段) 校验字段值包含中文"),

    // 二维报表函数
    VALUE_P_NM("VALUE_P_NM", "VALUE_P_NM(表名.字段)返回表中某字段数据,作乘以100000的处理,一维、二维表间校验专属，仅支持单条数据校验"),
    COOR("COOR", "COOR(表名.x, 表名.y) x、y为横纵坐标,通过坐标去字段值, null值自动计算为0"),


    // 特殊函数定制化校验（设计方案不满足此此类，暂时写死处理），示例：430000 湖南省,64672402.0000;440000 广东省,10000000.0000
    CPXS_MJ("CPXS_MJ", "CPXS_MJ(表名.字段) 产品募集总量登记.产品销售区域及募集金额 定制函数"),
    CPXS_BZ("CPXS_BZ", "CPXS_BZ(表名.字段) 币种和申购兑付信息 定制函数"),
    //募集总量登记币种定制函数
    CPMJ_BZ("CPMJ_BZ", "CPMJ_BZ(表名.字段) 币种和申购兑付 定制函数"),
    //校验持仓登记-中间层行内资产/负债编码是否匹配数据库中已有的记录
    CCDJ_AS_CD("CCDJ_AS_CD", "CCDJ_AS_CD(表名.字段) 持仓登记 中间层行内资产/负债编码 定制函数"),
    //统一分号多选，逗号多字段配置函数 示例：430000 湖南省,64672402.0000;440000 广东省,10000000.0000
    MULTI_SPECIAL_VAL("MULTI_SPECIAL_VAL", "MULTI_SPECIAL_VAL(表名.字段)"),


    //校验机构代码定制函数
    BANK_CODE_CHECK("BANK_CODE_CHECK", "BANK_CODE_CHECK(表名.字段)校验机构代码定制函数"),
    //用于多选字段金额统计值
    RE_VAL_CONT("RE_VAL_CONT", "RE_VAL_CONT(表名.字段,取数位数) 取数位数为金额字段所处顺序"),
    //验证如投资资产种类及比例字段，符合格式及数据源要求例： 10%股票(二级市场);10%-20%公司债券;70%-80%:信贷资产
    INVEST_TYPE_AND_RATIO("INVEST_TYPE_AND_RATIO", "TYPE_AND_RATIO(表名.字段名)验证字符为特殊格式"),


    //----------------白名单合集--------------------
    //老白名单，后续请使用下方的白名单独立校验（逐步弃用中）
    WHITE_CHAR_UNICODE("WHITE_CHAR", "WHITE_CHAR(表名.字段名)验证字符串包含字段仅为白名单的英文、数字、符号"),
    //新白名单
    //校验全部白名单（一、二期）
    WHITE_ALL("WHITE_ALL", "WHITE_ALL(表名.字段)校验字符串中的每个字符均属于白名单内"),
    //校验选择的枚举类型白名单（一、二期）
    WHITE_X("WHITE_X", "WHITE_ALL(表名.字段,ExpressionWhiteEnum枚举类型) 校验字符串中的每个字符均属于枚举类型的白名单内"),
    //校验全部白名单（三期）
    WHITE_TH_ALL("WHITE_TH_ALL", "WHITE_TH_ALL(表名.字段)校验字符串中的每个字符均属于白名单内"),
    //校验选择的枚举类型白名单（三期）
    WHITE_TH_X("WHITE_TH_X", "WHITE_TH_ALL(表名.字段,ExpressionWhiteEnum枚举类型) 校验字符串中的每个字符均属于枚举类型的白名单内");

    ExpressionEnum(String val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    private String val;
    private String desc;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
