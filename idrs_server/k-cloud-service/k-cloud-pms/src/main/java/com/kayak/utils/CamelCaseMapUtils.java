package com.kayak.utils;

import cn.hutool.core.map.MapUtil;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: k-cloud
 * @description: 查询结果驼峰转换工具类
 * @author: WangZhenXin
 * @create: 2021-01-06 10:37
 * @memo 备注信息
 */
public class CamelCaseMapUtils {

    /**
     * 将查询sql结果转换为驼峰命名
     * @param sqlRowList 数据库查询结果
     * @return 驼峰命名Map
     */
    public static SqlResult<Map<String,Object>> CamelCaseSqlRow(List<SqlRow> sqlRowList){
        SqlResult<Map<String,Object>> sqlResult = new SqlResult<>();
        List<Map<String, Object>> mapList = sqlRowList.stream().map(MapUtil::toCamelCaseMap).collect(Collectors.toList());
        sqlResult.setResults(mapList.size());
        sqlResult.setRows(mapList);
        sqlResult.setDesensitized(false);
        return sqlResult;
    }

    /**
     * 将查询sql结果转换为驼峰命名,并将分页结果返回
     * @param sqlRowList 数据库查询结果
     * @param size 数据库查询总记录
     * @return 驼峰命名Map
     */
    public static SqlResult<Map<String,Object>> CamelCaseSqlRow(List<SqlRow> sqlRowList, int size){
        SqlResult<Map<String,Object>> sqlResult = new SqlResult<>();
        List<Map<String, Object>> mapList = sqlRowList.stream().map(MapUtil::toCamelCaseMap).collect(Collectors.toList());
        sqlResult.setResults(size);
        sqlResult.setRows(mapList);
        sqlResult.setDesensitized(false);
        return sqlResult;
    }

    /**
     * 重构加入脱敏参数
     * @param sqlRowList 数据库查询结果
     * @param desensitized 是否脱敏
     * @return 驼峰命名Map
     */
    public static SqlResult<Map<String,Object>> CamelCaseSqlRow(List<SqlRow> sqlRowList,boolean desensitized){
        SqlResult<Map<String,Object>> sqlResult = new SqlResult<>();
        List<Map<String, Object>> mapList = sqlRowList.stream().map(MapUtil::toCamelCaseMap).collect(Collectors.toList());
        sqlResult.setResults(mapList.size());
        sqlResult.setRows(mapList);
        sqlResult.setDesensitized(false);
        return sqlResult;
    }
}
