package com.kayak.pms.T81.Action;

import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.pms.T81.service.T8ProdInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/1/14 17:53
 */
@RestController
public class T8ProdInfoAction extends BaseController {
    @Autowired
    private T8ProdInfoService t8ProdInfoService;

    @RequestMapping(value = "/prodInfo/findProdInfoByLike.json",produces = { "application/json;charset=UTF-8"})
    public SqlResult<Map<String, Object>> findProdInfoByLike(){
        try {
            Map<String, Object> params = RequestSupport.getParameters();
            return t8ProdInfoService.findProdInfoByLike(params);
        } catch (Exception e) {
            e.printStackTrace();
            return new SqlResult<Map<String, Object>>();
        }
    }

}
