package com.kayak.config.service;

import com.alibaba.fastjson.JSON;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.config.dao.Ta5015Dao;
import com.kayak.config.model.*;
import com.kayak.config.utils.ClearTaskUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.system.RequestSupport;
import com.kayak.graphql.model.FetcherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: lfzh
 * @date: 2021-01-26 19:03
 */
@Service
@APIDefine(desc = "清算提交", model = String.class)
public class Ta5015CombinService {
    protected static final Logger log = LoggerFactory.getLogger(Ta5015CombinService.class);

    @Autowired
    private KbatchTaskSetListService taskSetListService;

    @Autowired
    private Ta5015Dao ta5015Dao;

    @API(desc = "修改清算组、成员及任务配置", auth = APIAuth.NO)
    public String submit(String jsonData) throws Exception {
        Map<String,Object> tparam= new HashMap<>();

        List<KbatchGroupInfo> taClearGroupInfos = new ArrayList<>();
        if(jsonData != null && jsonData.length() > 0){
            List<Ta5015> ta5015List = JSON.parseArray(jsonData, Ta5015.class);
            for (Ta5015 ta5015 : ta5015List) {
                KbatchGroupInfo taClearGroupInfo = new KbatchGroupInfo();
                taClearGroupInfo.setPreTaskGroup(ta5015.getPreTaskGroup());
                taClearGroupInfo.setTaskGroup(ta5015.getTaskGroup());
                taClearGroupInfo.setExecTaskType(ta5015.getExecTaskType());
                taClearGroupInfo.setLastTaskGroup(ta5015.getLastTaskGroup());
                tparam.put("execTaskType",ta5015.getExecTaskType());
                taClearGroupInfos.add(taClearGroupInfo);
            }
            Map<String, String> preTaskGroupMap = ClearTaskUtil.getPreTaskGroup(taClearGroupInfos);

            DaoUtil.doTrans(() -> {
                //清空所有的任务
                taskSetListService.deleteAllTaClearTaskSet(tparam);
                //删除所有ta_clear_group_info中数据
                ta5015Dao.DeleteAllData(tparam);
                //先删除所有的成员
//            memberService.deleteAllTaClearGroupMember();
                //清空所有的任务
                taskSetListService.deleteAllTaClearTaskSet(tparam);
                //改变产品清算组的任务pre_task_id的前置清算组数据
//            updateProdTaskPreTaskId(preTaskGroupMap);
                //开始插入数据
                for (Ta5015 res : ta5015List) {
                    //添加数据到ta_clear_group_info中
                    Map<String, Object> dataMap = new HashMap<>();
                    Field[] declaredFields = res.getClass().getDeclaredFields();
                    for (Field field : declaredFields) {
                        if ("member".equals(field.getName()) || "existTaskInfos".equals(field.getName())) {
                            continue;
                        }
                        field.setAccessible(true);
                        dataMap.put(field.getName(), field.get(res));
                    }
                    FetcherData<Ta5015> data = new FetcherData<Ta5015>(dataMap, Ta5015.class);
                    ta5015Dao.insertTaClearGroupInfo(data);
                    //处理任务设置
                    String                str;
                    List<KbatchTaskInfo> taskList = res.getExistTaskInfos();
                    if (taskList != null) {
                        str = JSON.toJSONString(taskList);
                        Map<String, Object> taskSet = new HashMap<>();
                        taskSet.put("taskGroup", res.getTaskGroup());
                        taskSet.put("taskSetList", str);

                        SqlParam<KbatchTaskSetList> taskSqlParam = new FetcherData<>(taskSet, KbatchTaskSetList.class);
                        taskSetListService.addTaClearTaskSets(taskSqlParam, preTaskGroupMap.get(res.getTaskGroup()));
                    }
                }
            });
            return RequestSupport.updateReturnJson(true, "提交修改成功", null).toString();
        }else{
            return RequestSupport.updateReturnJson(true, "提交修改失败，数据文件丢失，详情查阅服务器日志。", null).toString();
        }
    }
}
