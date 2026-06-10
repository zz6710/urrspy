package com.kayak.pms.indexInfo.service;

import cn.hutool.core.map.MapUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.indexInfo.dao.DesktopIndexDao;
import com.kayak.pms.indexInfo.model.DesktopIndex;
import com.kayak.utils.CamelCaseMapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@APIDefine(desc = "首页", model = DesktopIndex.class)
@Service
public class DesktopIndexService {
    @Autowired
    private DesktopIndexDao desktopIndexDao;


    //axin
    @API(desc = "查询提醒信息",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<Map<String,Object>> findDesktopIndexList(SqlParam<DesktopIndex> param) throws Exception {
        SqlResult<Map<String,Object>> sqlResult = CamelCaseMapUtils.CamelCaseSqlRow(desktopIndexDao.findDesktopIndexList());
        List<Map<String, Object>> mapList = new ArrayList<>();
        sqlResult.getRows().forEach( homeTask->{
            try {
                SqlResult<Map<String,Object>> taskLists =  CamelCaseMapUtils.CamelCaseSqlRow(desktopIndexDao.findHomeTaskList((String)homeTask.get("sql_str")));
                if(homeTask.get("id") .equals("10001")){
                    String str = homeTask.get("content").toString().replace("${count}",taskLists.getResults()+"");
                    if(taskLists.getResults() > 0) {

                        StringBuffer prodCods = new StringBuffer("");
                        for(int i=0;i<taskLists.getRows().size();i++){
                            prodCods.append("'").append(taskLists.getRows().get(i).get("prodCode")).append("',");
                        }
                        String Str = prodCods.substring(1,prodCods.length()-2);
                        homeTask.put("prodCode",Str);

                        homeTask.put("content",str);
                        mapList.add(homeTask);
                    }
                }else{
                    taskLists.getRows().forEach(mapRow -> {
                        Map<String, Object> newMap = new HashMap<>();
                        newMap.putAll(homeTask);
                        newMap.putAll(mapRow);
                        newMap.put("content",getContent((String)homeTask.get("content"),mapRow));
                        mapList.add(newMap);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        });
        SqlResult<Map<String,Object>> returnResult = new SqlResult<>();
        returnResult.setResults(mapList.size());
        returnResult.setRows(mapList);
        return returnResult;
    }



    public String getContent (String str , Map<String,Object> mapRow){
        str = str.replace("${t8_prod_info_id}",(String)mapRow.get("t8_prod_info_id"));
        str = str.replace("${prod_name}",(String)mapRow.get("prod_name"));
        str = str.replace("${prod_code}",(String)mapRow.get("prod_code"));
        str = str.replace("${change_date}",(String)mapRow.get("change_date"));
        return  str;
    }

    public SqlResult<DesktopIndex> findCommonlyUsedMenu(SqlParam<DesktopIndex> param) throws Exception {
        return desktopIndexDao.findCommonlyUsedMenu(param);
    }
    public SqlResult<DesktopIndex> findCommonlyUsedMenuByuser(SqlParam<DesktopIndex> param) throws Exception {
        return desktopIndexDao.findCommonlyUsedMenuByuser(param);
    }
    public String saveCommonlyUsedMenu(SqlParam<DesktopIndex> param) throws Exception {
        desktopIndexDao.saveCommonlyUsedMenu(param);
        return RequestSupport.updateReturnJson(true,"保存成功",null).toString();
    }
    public String delCommonlyUsedMenu(SqlParam<DesktopIndex> param) throws Exception {
        desktopIndexDao.delCommonlyUsedMenu(param);
        return RequestSupport.updateReturnJson(true,"删除成功",null).toString();
    }


}
