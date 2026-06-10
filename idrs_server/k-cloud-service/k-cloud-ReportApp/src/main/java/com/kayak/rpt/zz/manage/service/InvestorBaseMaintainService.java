package com.kayak.rpt.zz.manage.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.rpt.dataMerge.model.CustomerDataMergeModel;
import com.kayak.rpt.zz.manage.dao.InvestorBaseMaintainDao;
import com.kayak.rpt.zz.manage.model.InvestorBaseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "全量投资者基本信息处理", model = InvestorBaseInfo.class)
public class InvestorBaseMaintainService {

    @Autowired
    private InvestorBaseMaintainDao investorBaseMaintainDao;

    @API(desc = "查询全量投资者基础信息", auth = APIAuth.YES)
    public SqlResult<InvestorBaseInfo> queryInvestorBaseInfo(SqlParam<InvestorBaseInfo> params) throws Exception {
        return investorBaseMaintainDao.queryInvestorBaseInfoByCond(params);
    }

    @API(desc = "新增投资者基础信息", auth = APIAuth.YES)
    public String putInvestorBaseInfo(SqlParam<InvestorBaseInfo> params) throws Exception {
        /**先校验客户识别标识是否存在,存在则返回提示信息*/
        boolean is_exists = investorBaseMaintainDao.judgeInverstorExists(params);
        if(is_exists){
            return RequestSupport.updateReturnJson(false, "新增投资者识别标识已存在!", null).toString();
        }
        investorBaseMaintainDao.putInvestorBaseInfo(params);
        return RequestSupport.updateReturnJson(true, "新增投资者信息成功!", null).toString();
    }

    @API(desc = "更新投资者基础信息", auth = APIAuth.YES)
    public String updateInvestorBaseInfo(SqlParam<InvestorBaseInfo> params) throws Exception {
        investorBaseMaintainDao.updateInvestorBaseInfo(params);
        return RequestSupport.updateReturnJson(true, "更新投资者信息成功!", null).toString();
    }

    @API(desc = "删除投资者基础信息", auth = APIAuth.YES)
    public String removeInvestorBaseInfo(SqlParam<CustomerDataMergeModel> params) throws Exception {
        investorBaseMaintainDao.deleteInvestorBaseInfo(params);
        return RequestSupport.updateReturnJson(true, "已删除投资者信息!", null).toString();
    }

}
