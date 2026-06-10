package com.kayak.config.service;

import com.alibaba.fastjson.JSON;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.config.dao.Ta5015Dao;
import com.kayak.config.model.Ta5015;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: lfzh
 * @date: 2020-12-24 15:37
 */
@Service
@APIDefine(desc = "清算编辑", model = Ta5015.class)
public class Ta5015Service {

    @Autowired
    private Ta5015Dao ta5015Dao;

    @API(desc = "查询清算组配置信息", auth = APIAuth.YES,operation = APIOperation.SELECT)
    public String queryTa5015(SqlParam<Ta5015> param) throws Exception {
        param.setMakeSql(false);
        SqlResult<Ta5015> data = ta5015Dao.queryClearGroups(param);
        List<Ta5015>      rows = data.getRows();
        rows.forEach(res -> {
            if (res.getPreTaskGroup() == null){
                res.setLastTaskGroup("");
                res.setPreTaskGroup("");
            }
        });
        rows.sort(new Ta5015());
        List<Map<String, Object>> firstDay   = new ArrayList<>();
        List<Map<String, Object>> secondDay  = new ArrayList<>();
        List<Ta5015>              noTimeList = new ArrayList<>();
        Map<String, List<Ta5015>> temp       = new HashMap<>();

        for (Ta5015 row : rows) {
            if (row.getLastTaskGroup() == null) {
                row.setLastTaskGroup("");
            }
            if (row.getPreTaskGroup() == null) {
                row.setPreTaskGroup("");
            }
            if ("999999".equals(row.getShouldExecTime())) {
                noTimeList.add(row);
            } else {
                String shouldExecTime = row.getShouldExecTime().substring(0, 2) + ":" + row.getShouldExecTime().substring(2, 4);
                if ("1".equals(row.getRunningType())) {
                    List<Ta5015> list = new ArrayList<>();
                    if (temp.containsKey(shouldExecTime + "1")) {
                        temp.get(shouldExecTime + "1").add(row);
                    } else {
                        list.add(row);
                        temp.put(shouldExecTime + "1", list);
                        Map<String, Object> map = new HashMap<>();
                        map.put("time", shouldExecTime);
                        map.put("id", UUID.randomUUID().toString());
                        map.put("rows", list);
                        firstDay.add(map);
                    }
                } else if ("0".equals(row.getRunningType())) {
                    List<Ta5015> list = new ArrayList<>();
                    if (temp.containsKey(shouldExecTime + "0")) {
                        temp.get(shouldExecTime + "0").add(row);
                    } else {
                        list.add(row);
                        temp.put(shouldExecTime + "0", list);
                        Map<String, Object> map = new HashMap<>();
                        map.put("time", shouldExecTime);
                        map.put("id", UUID.randomUUID().toString());
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
        map.put("noTimeList", noTimeList);
        return JSON.toJSONString(map);
    }

    //添加清算组时需要获取清算组id
    @API(desc = "添加清算组时需要获取清算组id", auth = APIAuth.YES)
    public String getSequence(SqlParam<Ta5015> param) throws Exception {
        param.setMakeSql(false);
        List<SqlRow>        sequence = ta5015Dao.getSequence(param);
        Map<String, Object> result   = new HashMap<>();
        List                temp     = sequence.stream().map(res -> res.get("sequence")).collect(Collectors.toList());
        result.put("success", "true");
        result.put("rows", temp);
        return JSON.toJSONString(result);
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

    //检查清算组名是否重复
//    @API(desc = "检查清算组名是否重复", auth = APIAuth.NO)
    public String getAllTaskGloupName(SqlParam<Ta5015> param) throws Exception {
        param.setMakeSql(false);
        List<SqlRow>        rows   = ta5015Dao.getAllTaskGloupName(param);
        Map<String, Object> result = new HashMap<>();
        List                temp   = rows.stream().map(res -> res.get("name")).collect(Collectors.toList());
        result.put("success", "true");
        result.put("rows", temp);
        return JSON.toJSONString(result);
    }

}
