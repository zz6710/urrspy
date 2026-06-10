package com.kayak.config.constants;

public class DataStatus {
    //数据状态(A-新增，E-生效，P-审核中, U-更改)
    //数据状态(A-新增，E-生效，P-审核中, U-更改,T-暂存)
    public static final String ADD = "A";

    public static final String EFFECTED = "E";

    public static final String APPROVE = "P";

    public static final String UPDATE = "U";

    public static final String DELETED = "D";

    public static final String TEMP = "T";

    public static final String CLEAR_DELSQL = "'D'";

    public static final String CLEAR_EFTSQL = "'E','A','D','P','U'";

    public static final String CLEAR_SQL = "'A','D','P','U'";
}
