package com.kayak.system.action;

import com.alibaba.fastjson.JSON;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;
import com.kayak.system.service.SqlInfoService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class SqlInfoAction {

    @Autowired
    private SqlInfoService sqlInfoService;

    /**
     * 获取所有的数据字典,转换成json返回到前台
     * @throws Exception
     */
    @PostMapping(value = "/base/sqlInfo/sqlInfo.json",produces = { "application/json;charset=UTF-8"})
    public String sqlInfo(@RequestParam String id) throws Exception {

        List<SqlRow> sqlRows = sqlInfoService.sqlInfoQuery(id);
        return JSON.toJSONString(sqlRows);
    }
}
