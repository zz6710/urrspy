package com.kayak.utils;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.exception.TaException;
import com.kayak.pms.T82.dao.SystemParamDao;
import com.kayak.pms.T82.model.T82009;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * 描述：
 *
 * @author grt
 * @date 2020-05-22
 */
@Component
public class T8SysUtils {


    @Autowired
    private SystemParamDao systemParamDao;

    /**
     * 查询paramValue
     * @param paraId
     * @return
     * @throws Exception
     */
    public String findParamValueByParaId(String paraId) throws Exception {
        if(StringUtils.isBlank(paraId)){
            throw new TaException("参数id为空");
        }
        SqlParam<T82009> params = new FetcherData<>(new HashMap<>() , T82009.class);
        params.getModel().setParaid(paraId);
        SqlResult<T82009> result =  systemParamDao.findParamValueByParaId(params);
        if(result.getRows().size() > 1 ){
             throw new TaException("查询结果含多条数据");
        }
        if(!StringUtils.isBlank(result.getRows().get(0).getParavalue())){
            return result.getRows().get(0).getParavalue();
        }
        throw new TaException("结果为空");

    }


}
