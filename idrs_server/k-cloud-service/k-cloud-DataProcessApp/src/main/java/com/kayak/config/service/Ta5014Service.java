package com.kayak.config.service;

import com.alibaba.fastjson.JSON;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.config.dao.Ta5014Dao;
import com.kayak.config.model.Ta5014;
import com.kayak.config.model.Ta5015GroupList;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: lfzh
 * @date: 2020-12-24 15:37
 */
@Service
@APIDefine(desc = "清算流程", model = Ta5014.class)
public class Ta5014Service {

    @Autowired
    private Ta5014Dao ta5014Dao;

    @API(desc = "查询指定工作日已注册的所有清算组", auth = APIAuth.YES, operation = APIOperation.SELECT)
    public String queryClearGroup(SqlParam<Ta5014> param) throws Exception {
        param.setMakeSql(false);
        param.setSqlNoLog(false);
        SqlResult<Ta5014> data = ta5014Dao.queryClearGroups(param);
        List<Ta5014>      rows = data.getRows();
        //将pre_task_group和last_task_group如果为null,则赋值为"";
        rows.forEach(res -> {
            if (res.getPreTaskGroup() == null) {
                res.setLastTaskGroup("");
                res.setPreTaskGroup("");
            }
        });
        rows.sort(new Ta5014());
        List<String>              list1        = rows.stream().map(row -> row.getTaskGroup()).collect(Collectors.toList());
        List<Map<String, Object>> firstDay     = new ArrayList<>();
        List<Map<String, Object>> secondDay    = new ArrayList<>();
        List<Ta5014>              notTimeGroup = new ArrayList<>();
        Map<String, List<Ta5014>> temp         = new HashMap<>();
        //获取所有的清算组合该清算组的上一个清算组
        SqlResult<Ta5015GroupList> res = ta5014Dao.getAllGroup();
        if (res != null) {
            Map<String, String> groups = new HashMap();
            for (Ta5015GroupList map : res.getRows()) {
                groups.put(map.getTaskGroup(), map.getLastTaskGroup());
            }
            for (Ta5014 row : rows) {
                //校验清算组集合中的上一个清算组
                if (!"".equals(row.getLastTaskGroup())) {
                    StringBuilder sb  = new StringBuilder();
                    //String[]      arr = row.getLastTaskGroup().split(",");
                    checkLastGroup(list1, groups, row.getLastTaskGroup(), sb);
                    row.setLastTaskGroup(sb.toString());
                }
            }
        }
        for (Ta5014 row : rows) {
            String shouldExecTime = "";
            if ("999999".equals(row.getShouldExecTime())) {
                notTimeGroup.add(row);
            } else {
                if (row.getShouldExecTime() != null) {
                    shouldExecTime = row.getShouldExecTime().substring(0, 2) + ":" + row.getShouldExecTime().substring(2, 4);
                }
                if ("1".equals(row.getRunningType())) {
                    List<Ta5014> list = new ArrayList<>();
                    if (temp.containsKey(shouldExecTime + "1")) {
                        temp.get(shouldExecTime + "1").add(row);
                    } else {
                        list.add(row);
                        temp.put(shouldExecTime + "1", list);
                        Map<String, Object> map = new HashMap<>();
                        map.put("time", shouldExecTime);
                        map.put("rows", list);
                        firstDay.add(map);
                    }
                } else if ("0".equals(row.getRunningType())) {
                    List<Ta5014> list = new ArrayList<>();
                    if (temp.containsKey(shouldExecTime + "0")) {
                        temp.get(shouldExecTime + "0").add(row);
                    } else {
                        list.add(row);
                        temp.put(shouldExecTime + "0", list);
                        Map<String, Object> map = new HashMap<>();
                        map.put("time", shouldExecTime);
                        map.put("rows", list);
                        secondDay.add(map);
                    }
                }
            }
        }
        this.sortByTime(firstDay);
        this.sortByTime(secondDay);
        Map<String, Object> map = new HashMap();
        map.put("success", true);
        map.put("firstDay", firstDay);
        map.put("secondDay", secondDay);
        map.put("notTimeGroup", notTimeGroup);
        return JSON.toJSONString(map);
    }


    private void checkLastGroup(List<String> rows, Map<String, String> groups, String lastGroup, StringBuilder sb) {
        if (lastGroup == null) {
            return;
        }
        String[] arr = lastGroup.split(",");
        for (String s : arr) {
            if (!rows.contains(s)) {
                this.checkLastGroup(rows, groups, groups.get(s), sb);
            } else {
                if (!"".equals(sb.toString())) {
                    sb.append(",");
                }
                sb.append(s);
            }
        }
    }

    public void sortByTime(List<Map<String, Object>> list) {
        list.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                int time1 = Integer.parseInt(((String) o1.get("time")).replace(":", ""));
                int time2 = Integer.parseInt(((String) o2.get("time")).replace(":", ""));
                return time1 - time2;
            }
        });
    }

    /**
     * 根据清算组ID查询所有失败任务的Task_Id
     * @param param
     * @return
     * @throws Exception
     */
    public SqlResult<String> queryErrorTaskByTaskGroup(SqlParam<Ta5014> param) throws Exception {
        param.setMakeSql(false);
        SqlResult<Ta5014> result  = ta5014Dao.queryErrorTaskByTaskGroup(param);
        List<Ta5014>      list    = result.getRows();
        List<String>      taskIds = list.stream().map(item -> item.getTaskId()).collect(Collectors.toList());
        SqlResult<String> build   = SqlResult.build(taskIds);
        return build;
    }

    /**
     * 根据清算组ID查询所有任务的Task_Id
     * @param param
     * @return
     * @throws Exception
     */
    public SqlResult<String> queryTaskByTaskGroup(SqlParam<Ta5014> param) throws Exception {
        param.setMakeSql(false);
        SqlResult<Ta5014> result  = ta5014Dao.queryTaskByTaskGroup(param);
        List<Ta5014>      list    = result.getRows();
        List<String>      taskExecids = list.stream().map(Ta5014::getTaskExecid).collect(Collectors.toList());
        return SqlResult.build(taskExecids);
    }
}
