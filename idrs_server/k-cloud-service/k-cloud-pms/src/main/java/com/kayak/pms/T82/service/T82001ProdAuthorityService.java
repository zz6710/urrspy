package com.kayak.pms.T82.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.dao.DaoService;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.global.constants.GlobalContents;
import com.kayak.pms.T82.dao.T8ProdAuthorityDao;
import com.kayak.pms.T82.model.T82001ProdAuthority;
import com.kayak.utils.ObjectToMapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "产品权限服务", model = T82001ProdAuthority.class)
public class T82001ProdAuthorityService {

    @Autowired
    private T8ProdAuthorityDao t8ProdAuthorityDao;

    @Autowired
    protected DaoService daoService;

    @API(desc = "查询产品与销售商关系信息", auth = APIAuth.YES)
    public SqlResult<T82001ProdAuthority> findTaDistributorInfos(SqlParam<T82001ProdAuthority> params) throws Exception {
        params.setMakeSql(true);
        return t8ProdAuthorityDao.findProdAuthority(params);
    }

    @API(desc = "添加产品与销售商关系表", params = "prod_code, distributor_code, handler_mode, interest_code, status")
    public String addTaProdAuthorityRelation(SqlParam<T82001ProdAuthority> params) throws Exception {
        T82001ProdAuthority t82001 = params.getModel();
        String[] prodCodes = t82001.getProdCode().split(GlobalContents.MSELECT_SPLIT);
        if (prodCodes.length <= 0){
            throw new PromptException("产品代码参数错误");
        }
        daoService.doTrans(() -> {
            for (String prodCode : prodCodes) {
                T82001ProdAuthority fort82001 = t82001.clone();
                fort82001.setProdCode(prodCode);
                SqlParam<T82001ProdAuthority> T82005SqlParam =new FetcherData<T82001ProdAuthority>(ObjectToMapUtils.entityToMap(fort82001), T82001ProdAuthority.class);

                if (t8ProdAuthorityDao.isprodCodeAndDistributorCode(T82005SqlParam).getRows().size() >0){
                    throw new PromptException("产品利率方案已存在！");
                };
                t8ProdAuthorityDao.addTaProdAuthority(T82005SqlParam).getEffect();
            }

        });

        return RequestSupport.updateReturnJson(true, "添加成功", null).toString();
    }

    @API(desc = "修改产品与销售商关系表", params = "prod_code, distributor_code, handler_mode, interest_code, status")
    public String updateTaProdAuthorityRelation(SqlParam<T82001ProdAuthority> params) throws Exception {
        t8ProdAuthorityDao.updateTaProdAuthorityRelation(params);
        return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
    }

    @API(desc = "删除销售商信息表", params = "prod_code, distributor_code")
    public int deleteTaDistributorInfo(SqlParam<T82001ProdAuthority> params) throws Exception {
        return t8ProdAuthorityDao.delTaByProdCodeAndDistributorCode(params);
    }
}
