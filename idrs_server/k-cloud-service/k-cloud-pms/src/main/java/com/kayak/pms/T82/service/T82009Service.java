package com.kayak.pms.T82.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.constants.SystemParamConstants;
import com.kayak.core.util.Tools;
import com.kayak.pms.T82.dao.SystemParamDao;
import com.kayak.pms.T82.model.T82009;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@APIDefine(desc = "系统参数服务", model = T82009.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class T82009Service {

    private final SystemParamDao systemParamDao;

    @API(desc = "查询系统参数列表", auth = APIAuth.NO)
    public SqlResult<T82009> find(SqlParam<T82009> params) throws Exception {
    	params.setMakeSql(true);
        T82009 model = params.getModel();
        model.setIsdisplay(SystemParamConstants.SHOW);
        model.setModuleid("a"); // TA系统子系统号
        return systemParamDao.find(params);
    }

    @API(desc = "保存系统参数列表")
    public String update(SqlParam<T82009> param) throws Exception {
        T82009 model = param.getModel();
        String paraid = model.getParaid();
        String paravalue = model.getParavalue();

        if (Tools.isBlank(paraid)) {
            return RequestSupport.updateReturnJson(true, "保存成功", null).toString();
        }
        // 等号分隔转换为list
        List<T82009> params = new ArrayList<>();
        String[] paraidArr = paraid.split("=");
        String[] paravalueArr = paravalue.split("=");
        for (int i = 0; i < paraidArr.length; i++) {
            T82009 p = new T82009();
            p.setParaid(paraidArr[i]);
            p.setParavalue(paravalueArr[i]);
            params.add(p);
        }

        systemParamDao.update(params);
        return RequestSupport.updateReturnJson(true, "保存成功", null).toString();
    }

   

    @API(desc = "查询系统参数表信息", auth = APIAuth.YES)
    public SqlResult<T82009> findSysParams(SqlParam<T82009> params) throws Exception {
        params.setMakeSql(true);
        T82009 model = params.getModel();
        model.setIsdisplay(SystemParamConstants.SHOW);
        model.setAction(null);
        model.setModuleid("a"); // TA系统子系统号
        return systemParamDao.findSysParams(params);
    }

    @API(desc = "添加系统参数表", params = "moduleid,paraid,paravalue,paraname,groupparaid,dict,functype,confoption,isdisplay,fieldtype")
    public int addSysParam(SqlParam<T82009> params) throws Exception {
        return systemParamDao.addSysParam(params).getEffect();
    }

    @API(desc = "修改系统参数表", params = "moduleid,paraid,paravalue,paraname,groupparaid,dict,functype,confoption,isdisplay,fieldtype")
    public int updateSysParam(SqlParam<T82009> params) throws Exception {
        return systemParamDao.updateSysParam(params).getEffect();
    }

    @API(desc = "删除系统参数表", params = "moduleid,paraid,paravalue,paraname,groupparaid,dict,functype,confoption,isdisplay,fieldtype")
    public int deleteSysParam(SqlParam<T82009> params) throws Exception {
        return systemParamDao.deleteSysParam(params).getEffect();
    }
    
    
}
