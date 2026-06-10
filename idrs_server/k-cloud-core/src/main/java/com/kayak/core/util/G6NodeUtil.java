package com.kayak.core.util;

import com.alibaba.fastjson.JSONObject;
import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysBeans;
import com.kayak.core.system.SysUtil;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

public class G6NodeUtil {
    private static String type="dd";
    public static List<String> findNodeIdList(String templateItemId , String nodeId) throws Exception {
        List<String> taskIdList=new ArrayList<>();
        DaoService daoService = SysBeans.getBean("daoService");
        SqlRow sqlRows = daoService.query(SqlRow.class, "SELECT * FROM wf_flow_template_item where id=$S{id}",templateItemId);
        JSONObject jsonObject=JSONObject.parseObject(sqlRows.getString("json"));
        jsonObject.getJSONObject("orignal").getJSONArray("edges").stream().forEach(item->{
            JSONObject edge=(JSONObject)item;
            if(edge.getString("sourceId").equals(nodeId)){
                taskIdList.add(edge.getString("targetId"));
            }
        });
        return taskIdList;
    }

    public static List<String> findNodeIdList2(String templateItemId ,String nodeId) throws Exception {
        MultiValueMap<String, JSONObject> gg = new LinkedMultiValueMap<>();
        List<String> taskIdList=new ArrayList<>();
        DaoService daoService = SysBeans.getBean("daoService");
        SqlRow sqlRows = daoService.query(SqlRow.class, "SELECT * FROM wf_flow_template_item where id=$S{id}",templateItemId);
        JSONObject jsonObject=JSONObject.parseObject(sqlRows.getString("json"));
        jsonObject.getJSONObject("orignal").getJSONArray("edges").stream().forEach(item->{
            JSONObject edge=(JSONObject)item;
            jsonObject.getJSONObject("orignal").getJSONArray("nodes").forEach(item2->{
                JSONObject node=(JSONObject)item2;

                if(node.getString("id").equals(nodeId)){
                    type=node.getString("shape");
                }

                if(edge.getString("targetId").equals(node.getString("id"))){
                    gg.add(edge.getString("sourceId"),node);
                }
            });
        });

        return dg(type,nodeId,gg,taskIdList);
    }

    public static List<String> dg(String nodeType, String nodeId, MultiValueMap<String,JSONObject> map, List<String> idList){
        if(null==map.get(nodeId)){
            return new ArrayList<String>();
        }else{
            map.get(nodeId).forEach(item->{
                if(item.getJSONObject("attrs").getString("type").equals("endevent-none")){
                    if(!nodeType.equals("k-Rect")){
                        idList.add("end");
                    }
                }else{
                    if(item.getString("shape").equals("dd")){
                        idList.add(item.getString("id"));
                    }else{
                        if(nodeType.equals("k-Rect")&&item.getString("shape").equals("k-Rect")){

                        }else{
                            dg(item.getString("shape"),item.getString("id"),map,idList);
                        }
                    }
                }

            });
        }

        return idList;
    }
}
