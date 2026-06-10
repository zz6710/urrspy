package com.kayak.base.dao;

import org.apache.commons.lang.StringUtils;

public class DataSourceProperty {
    public static int PUB=0;//公共信息数据库
    public static int IDB=0;//信息披露数据库
    public static int SRB=0;//监管报送数据库
    public static int BAK=1;//归档备份数据库

    public static int getDataSource(String dataS){
        int dataSource = 0;
        if(StringUtils.equals(dataS,"PUB"))
            dataSource = PUB;
        else if(StringUtils.equals(dataS,"IDB"))
            dataSource = IDB;
        else if(StringUtils.equals(dataS,"SRB"))
            dataSource = SRB;
        else if(StringUtils.equals(dataS,"BAK"))
            dataSource = BAK;
        return dataSource;
    }
}
