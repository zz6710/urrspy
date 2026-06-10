package com.kayak.bak.core.config;

import com.kayak.bak.model.dto.SourceConfigDTO;
import com.kayak.core.sql.SqlRow;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库外部依赖配置，其它项目使用需要在此修改数据库配置
 */
public class SourceConfig {

    public static int DPB=0;//公共信息数据库  使用的是在jdbc.yml中的序号
    public static int IDB=0;//信息披露数据库  共用公共库
    public static int SRB=0;//监管报送数据库  共用公共库
    public static int BAK=1;//归档备份数据库

    public static String DPB_NAME="dpb";//公共信息数据库
    public static String IDB_NAME="idb";//归档备份数据库
    public static String SRB_NAME="srb";//归档备份数据库
    public static String BAK_NAME="bak";//归档备份数据库

    public static int getDataSource(String data){
        int dataSource = 0;
        if(StringUtils.equals(data,"dpb"))
            dataSource = DPB;
        else if(StringUtils.equals(data,"idb"))
            dataSource = IDB;
        else if(StringUtils.equals(data,"srb"))
            dataSource = SRB;
        else if(StringUtils.equals(data,"bak"))
            dataSource = BAK;
        return dataSource;
    }

    /**
     * 数据库是否存在
     * @param data
     * @return
     */
    public static Boolean hasSource(String data) {
        Boolean ret = false;
        if(StringUtils.equals(data,"dpb"))
            ret = true;
        else if(StringUtils.equals(data,"idb"))
            ret = true;
        else if(StringUtils.equals(data,"srb"))
            ret = true;
        else if(StringUtils.equals(data,"bak"))
            ret = true;
        return ret;
    }

    public static List<SourceConfigDTO> getDbList() {
        List<SourceConfigDTO> dbList = new ArrayList<>();
        dbList.add(new SourceConfigDTO().setDbName(DPB_NAME));
        dbList.add(new SourceConfigDTO().setDbName(IDB_NAME));
        dbList.add(new SourceConfigDTO().setDbName(SRB_NAME));
        dbList.add(new SourceConfigDTO().setDbName(BAK_NAME));
        return dbList;
    }
}
