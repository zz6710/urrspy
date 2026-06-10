package com.kayak.clear.utils;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysBeans;
import com.kayak.core.util.DateUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskUtil {

    public static Boolean isSysWorkDay(String day) throws Exception {
        ComnDao dao = SysBeans.getBean("comnDao");
            String str = "select 1 from sys_workday_set where pgmno = '001' and workday = $S{day}";
            Map map=new HashMap<String, Object>();
            map.put("day",day);
            List<SqlRow> sqlRows = dao.findRows( str, map);
            if (sqlRows != null && sqlRows.size() > 0){
                return true;
            } else  return false;

    }

    /**
     * 是否跳过任务 true跳过
     * @param day
     * @param taskId
     * @return
     * @throws Exception
     */
    public static Boolean isSkipTask(String day,String taskId) throws Exception {
        ComnDao dao = SysBeans.getBean("comnDao");
        String str = "select * from kbatch_task_info kti where kti.TASK_ID =$S{taskId} ";
        Map map=new HashMap<String, Object>();
        map.put("taskId",taskId);
        List<SqlRow> sqlRows = dao.findRows( str, map);
        String is_skip_in_holiday="1";//节假日是否跳过 默认 1跳过
        if (sqlRows != null && sqlRows.size() > 0){
            is_skip_in_holiday=sqlRows.get(0).getString("is_skip_in_holiday");
        }
        //节日日跳过为是 且 不是工作日
        if("1".equals(is_skip_in_holiday) && !isSysWorkDay(day)) return true;
        else return false;
    }
}
