package com.kayak.graphql.service;

import com.kayak.cache.util.CacheUtil;
import com.kayak.config.ConfigUitl;
import com.kayak.core.sql.SqlRow;
import com.kayak.graphql.dao.SearchFieldDao;
import org.apache.commons.collections4.map.CompositeMap;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SearchFieldService implements SmartInitializingSingleton {

    @Autowired
    private SearchFieldDao searchFieldDao;

    @Autowired
    private JedisPool jedisPool;

    @Value("${cache.detection}")
    private String detection;

    @Override
    public void afterSingletonsInstantiated() {
        try {
            if(detection.equals("nacos")){
                ConfigUitl.addNacosConfigListener("CustomSearchField",data->{
                    clearSearchFieldMap();
                });
            } else if (Objects.equals(detection, "redis")) {
                new Thread(() -> jedisPool.getResource().subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        clearSearchFieldMap();
                    }
                }, "CustomSearchField")).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearSearchFieldMap(){
        CacheUtil.customSearchFieldMap.clear();
        CacheUtil.customDefaultSearchFieldMap.clear();
    }

    public List<Map<String, Object>> getCustomSearchField(String modelName){
        checkMap(modelName);
        return CacheUtil.customSearchFieldMap.get(modelName);
    }

    public List<Map<String, Object>> getDefaultCustomSearchField(String modelName){
        checkMap(modelName);
        return CacheUtil.customDefaultSearchFieldMap.get(modelName);
    }

    private void checkMap(String modelName){
        if(!CacheUtil.customSearchFieldMap.containsKey(modelName)){
            synchronized (this){
                if(!CacheUtil.customSearchFieldMap.containsKey(modelName)){
                    try {
                        List<SqlRow> list=  searchFieldDao.findCustomSearchField(modelName);
                        List<Map<String, Object>> searchFields = new ArrayList<Map<String, Object>>();
                        List<Map<String, Object>> defaultSearchFields = new ArrayList<Map<String, Object>>();

                        if (list != null && list.size() > 0) {
                            for (SqlRow sqlRow : list) {
                                boolean isDefault = "1".equals(sqlRow.getString("is_default"));

                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("label", sqlRow.getString("model_field_label"));
                                map.put("isDefault", isDefault);
                                map.put("inputHtml", sqlRow.getString("input_html"));
                                map.put("inputConfig", sqlRow.getString("input_config"));
                                map.put("field", sqlRow.getString("model_field"));
                                searchFields.add(map);

                                if(isDefault){
                                    defaultSearchFields.add(map);
                                }
                            }
                        }

                        CacheUtil.customSearchFieldMap.put(modelName,searchFields);
                        CacheUtil.customDefaultSearchFieldMap.put(modelName,defaultSearchFields);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
