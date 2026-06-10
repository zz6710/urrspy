package com.kayak.pms.basePublish.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class DisclosureErrorHandlerDao extends ComnDao {

    /**
     * 插入处理报错信息
     * @param params
     */
    public void insertErrorMessage(Map<String, Object> params) throws Exception {
        String sqlStr = "insert into base_error_message (error_code, error_info, error_group, create_date, create_time) " +
                        "values ($S{error_code}, $S{error_info}, $S{error_group}, DATE_FORMAT(NOW(), '%Y%m%d'), DATE_FORMAT(NOW(), '%H%i%s'))";
        try{
            super.update(sqlStr, DataSourceProperty.PUB, params);
        }catch (Exception e){
            throw new Exception("插入报错信息语句执行异常: " + e.getMessage());
        }
    }
    

}
