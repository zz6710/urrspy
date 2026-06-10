package com.kayak.pms.home.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.home.dao.HomeWorkFlowDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * com.kayak.pms.home.service
 * user:rennannan
 * date:2021/3/9 20:32
 * function:
 */
@Service
public class HomeWorkFlowService {
    @Autowired
    private HomeWorkFlowDao homeWorkFlowDao;

    /**
     * 功能：查询当前用户待办工作流
     * 作者：rennannan
     * 日期：20210309
     *
     * @param
     * @return
     * @throws Exception
     */
    @API(desc = "查询当前用户", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<HashMap> findHomeWorkFlowInfos() throws Exception {
        //add by zhangchangsi 移动审批添加节点名称查询条件
        Map<String, Object> parameters = RequestSupport.getParameters();
        Map<String, Object> params = new HashMap<>();
        String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
        params.put("userId", userid);
        params.put("nodeName", parameters.get("nodeName"));
        List<HashMap> list = this.homeWorkFlowDao.findHomeWorkFlowInfos(params);
        SqlResult<HashMap> result = new SqlResult<>();
        result.setResults(list.size());
        result.setRows(list);
        return result;
    }
}
