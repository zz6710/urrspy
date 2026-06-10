package com.kayak.pms.T8ProdDeal.service;

import cn.hutool.core.map.MapUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.pms.T8ProdDeal.dao.T8ProdDealDao;
import com.kayak.pms.T8ProdDeal.model.T8ProdDeal;
import com.kayak.utils.CamelCaseMapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: k-cloud
 * @description: 产品流水Service
 * @author: WangZhenXin
 * @create: 2021-01-06 09:21
 * @memo 备注信息
 */
@Service
@APIDefine(desc = "产品流水Service", model = T8ProdDeal.class)
public class T8ProdDealService {
    private static final Logger logger = LoggerFactory.getLogger(T8ProdDealService.class);

    @Autowired
    private T8ProdDealDao t8ProdDealDao;

    @API(desc = "查询产品销售流水",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<Map<String, Object>> findProdDealByProdCodeOrProdName(SqlParam<T8ProdDeal> params) throws Exception {
        List<SqlRow> prodDealByProdCodeOrProdName = t8ProdDealDao.findProdDealByProdCodeOrProdName(params);
        return CamelCaseMapUtils.CamelCaseSqlRow(prodDealByProdCodeOrProdName);
    }


}
