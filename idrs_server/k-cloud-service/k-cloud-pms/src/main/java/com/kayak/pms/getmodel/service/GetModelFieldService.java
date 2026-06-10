package com.kayak.pms.getmodel.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.pms.getmodel.dao.ServerMethodDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * com.kayak.pms.home.service
 * user:zhanghao
 * date:2022/3/21 20:32
 * function:
 */
@Service
public class GetModelFieldService {
    @Autowired
    private ServerMethodDao serverMethodDao;

    /**
     * 功能：查询当前用户待办工作流
     * 作者：zhanghao
     * 日期：20220322
     *
     * @param
     * @return
     * @throws Exception
     */
    @API(desc = "根据服务菜单查询对应实体", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlRow getModelNameByServer() throws Exception {
        Map<String, Object> parameters = RequestSupport.getParameters();
        Map<String, Object> params = new HashMap<>();
        params.put("server", parameters.get("server"));
        SqlRow SqlRow = this.serverMethodDao.getModelNameByserver(params);
        return SqlRow;
    }
}
