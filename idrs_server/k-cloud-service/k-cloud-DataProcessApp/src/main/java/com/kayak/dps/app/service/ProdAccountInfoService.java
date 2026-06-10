package com.kayak.dps.app.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.app.dao.ProdAccountInfoDao;
import com.kayak.dps.app.model.ProdAccountInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 产品账户信息Service
 */
@APIDefine(desc = "产品账户信息Service",model = ProdAccountInfo.class)
@Service
public class ProdAccountInfoService {

    @Autowired
    private ProdAccountInfoDao prodAccountInfoDao;


    @API(desc = "查询账户信息", auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<ProdAccountInfo> findAccountInfo(SqlParam<ProdAccountInfo> params) throws Exception {
        return prodAccountInfoDao.findAccountInfo(params);
    }

    @API(desc = "修改时查询是否已经存在对应类型的账户", auth = APIAuth.NO,operation = APIOperation.SELECT)
    public String findProdAccountInfoTypeOnUpdate(SqlParam<ProdAccountInfo> params) throws Exception {
        String prodCode = params.getModel().getProdCode();
        StringBuilder msg = new StringBuilder();
        if (StringUtils.isNotEmpty(prodCode)){
            ProdAccountInfo model = params.getModel();
            model.setProdCode(prodCode);
            int count = prodAccountInfoDao.findProdAccountInfoCountUpdate(model);
            if (count > 0) {
                msg.append(prodCode).append(" ");
            }

        }
        return RequestSupport.updateReturnJson(true, msg.toString(), null).toString();

    }
    @API(desc = "修改账户信息", auth = APIAuth.YES,operation = APIOperation.UPDATE)
    public int updateAccountInfo(SqlParam<ProdAccountInfo> params) throws Exception {
        return prodAccountInfoDao.updateAccountInfo(params);
    }
    @API(desc = "新增账户信息", auth = APIAuth.YES,operation = APIOperation.INSTER)
    public String addAccountInfo(SqlParam<ProdAccountInfo> params) throws Exception {
        try {
            prodAccountInfoDao.addAccountInfo(params);
            return RequestSupport.updateReturnJson(true,"新增成功",null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,"新增失败！" + e.getMessage() ,null).toString();
        }
    }
    @API(desc = "删除账户信息", auth = APIAuth.YES,operation = APIOperation.DELETE)
    public String deleteAccountInfo(SqlParam<ProdAccountInfo> params) throws Exception {
        try {
            prodAccountInfoDao.deleteAccountInfo(params);
            return RequestSupport.updateReturnJson(true,"删除成功",null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,"删除失败！" + e.getMessage() ,null).toString();
        }
    }
    @API(desc = "查询产品名称与代码", auth = APIAuth.NO ,operation = APIOperation.SELECT)
    public SqlResult<ProdAccountInfo> findProdCdAndNm(SqlParam<ProdAccountInfo> params) throws Exception {
        return prodAccountInfoDao.findProdCdAndNm(params);
    }
    @API(desc = "查询销售商名称与代码", auth = APIAuth.NO ,operation = APIOperation.SELECT)
    public SqlResult<ProdAccountInfo> findSellerCdAndNm(SqlParam<ProdAccountInfo> params) throws Exception {
        return prodAccountInfoDao.findSellerCdAndNm(params);
    }
}
