package com.kayak.dps.app.service;


import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.dps.app.dao.NetReportRulesDao;
import com.kayak.dps.app.model.NetReportRules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@APIDefine(model = NetReportRules.class)
public class NetReportRulesService {
    
    @Autowired
    private NetReportRulesDao netReportRulesDao;

    private static Logger log = LogManager.getLogger(NetReportRulesService.class);
    
    @API(desc = "查询", auth = APIAuth.NO)
    public SqlResult<NetReportRules> findNetReportRules(SqlParam<NetReportRules> params) throws Exception {
        params.setMakeSql(false);
        return netReportRulesDao.findNetReportRules(params);
    }

    @API(desc = "添加", auth = APIAuth.YES, operation = APIOperation.INSTER)
    public String addNetReportRules(SqlParam<NetReportRules> params) throws Exception {

        try {
            //查询数据是否存在
            List<NetReportRules> l = netReportRulesDao.findNetReportRules(params).getRows();
            if (l.size() > 0)
                throw new Exception("该运作模式已存在！！！");

            NetReportRules m = params.getModel();
            String date = DateUtil.getNowDate();
            String time = DateUtil.getNowTime();
            String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));

            m.setCrtDate(date);
            m.setCrtTime(time);
            m.setCrtUser(userid);
            netReportRulesDao.addNetReportRules(m);
            return RequestSupport.updateReturnJson(true,"新增成功",null).toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return RequestSupport.updateReturnJson(false,"新增失败！" + e.getMessage() ,null).toString();
        }
    }

    @API(desc = "修改", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String updateNetReportRules(SqlParam<NetReportRules> params) throws Exception {
        NetReportRules m = params.getModel();
        String date = DateUtil.getNowDate();
        String time = DateUtil.getNowTime();
        String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
        try {
            m.setUpdDate(date);
            m.setUpdTime(time);
            m.setUpdUser(userid);
            netReportRulesDao.updateNetReportRules(m);
            return RequestSupport.updateReturnJson(true,"修改成功",null).toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return RequestSupport.updateReturnJson(false,"修改失败！" + e.getMessage() ,null).toString();
        }
    }

    @API(desc = "删除", auth = APIAuth.YES, operation = APIOperation.DELETE)
    public String deleteNetReportRules(SqlParam<NetReportRules> params) throws Exception {
        try {
            netReportRulesDao.deleteNetReportRules(params.getModel());
            return RequestSupport.updateReturnJson(true,"删除成功",null).toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return RequestSupport.updateReturnJson(false,"删除失败！" + e.getMessage() ,null).toString();
        }
    }
}
