/*
package com.kayak.jimureport.controller;


import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.SysUtil;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component
public class JimuReportTokenService  implements JmReportTokenServiceI {

    @Resource
    private ComnDao comnDao;

    @Override
    public String getToken(HttpServletRequest request) {
        // 获取当前用户登录juid
        Cookie[] cookies = request.getCookies();
        String UUID = null;
        if(cookies != null){
            for(Cookie cookie : cookies){
                if("JUUID".equals(cookie.getName())){
                    UUID = cookie.getValue() == null ? "" : cookie.getValue();
                    break;
                }
            }
        }

        return UUID;
    }


    @Override
    public String getUsername(String s){

        return SysUtil.getSysUserParamValue("sys_user_username").toString();
    }

    @Override
    public Boolean verifyToken(String s) {
        return true;
    }

    @Override
    public Map<String, Object> getUserInfo(String token){
        Map map = new HashMap();
*/
/*       Map param = new HashMap();
        SqlResult sqlResult = null;
        String sysDate = "";
        //动态获取设置默认值(sql查询)
        try {
            //动态获取设置默认值（固定值）
            String getDictSql1 = "select  di.ITEM_VALUE key,di.DESCRIPTION text  from jimu_dict_item di left join JIMU_DICT d on d.ID=di.DICT_ID where d.DICT_NAME='查询框固定设置默认值'";
            sqlResult = comnDao.sqlQuery(getDictSql1,"dsSys");
            while (sqlResult.next()) {
                map.put(sqlResult.getString("key"),sqlResult.getString("text"));

            }

        }catch (Exception e){

        }*//*

        //缓存系统工作日
        return map;
    }

    @Override
    public HttpHeaders customApiHeader() {
        HttpHeaders header = new HttpHeaders();
        header.add("custom-header1", "Please set a custom value 1");
        header.add("token", "token value 2");
        return header;
    }
}
*/
