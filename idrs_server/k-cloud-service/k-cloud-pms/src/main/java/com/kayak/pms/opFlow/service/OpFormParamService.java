package com.kayak.pms.opFlow.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.opFlow.dao.OpFormParamDao;
import com.kayak.pms.opFlow.dao.OpFormParamRelationDao;
import com.kayak.pms.opFlow.model.OpFormParam;
import com.kayak.pms.opFlow.model.OpFormParamRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@APIDefine(desc = "功能表单参数业务", model = OpFormParam.class)
public class OpFormParamService {

    @Autowired
    private OpFormParamDao opFormParamDao;
    @Autowired
    private OpFormParamRelationDao opFormParamRelationDao;

    @API(desc = "查询表单参数", auth = APIAuth.NO)
    public SqlResult<OpFormParam> findOpFormParam(SqlParam<OpFormParam> params) throws Exception {
        // 查询联动关系
        SqlResult<OpFormParamRelation> opFormParamRelations = opFormParamRelationDao.findOpFormParamRelations(new FetcherData<>(params.getParams(), OpFormParamRelation.class));
        // 按参数分组
        Map<String, List<OpFormParamRelation>> map = opFormParamRelations.getRows().stream().collect(Collectors.groupingBy(OpFormParamRelation::getParamCode));
        // 查询表单参数
        SqlResult<OpFormParam> opFormParamResult = opFormParamDao.findOpFormParamById(params);
        // 给表单参数塞入配置的联动关系
        opFormParamResult.getRows().forEach(item -> item.setRelations(map.getOrDefault(item.getParamCode(), Collections.emptyList())));
        // 返回给前端
        return opFormParamResult;
    }

    @API(desc = "保存表单参数", auth = APIAuth.NO)
    public String save(SqlParam<OpFormParam> params) throws Exception {
        opFormParamDao.save(params);
        return RequestSupport.updateReturnJson(true, "保存成功", null).toString();
    }

    @API(desc = "查询表单信息", auth = APIAuth.NO)
    public SqlResult<OpFormParam> findOpFormDataInfo(SqlParam<OpFormParam> params) throws Exception {
        String processInstanceId = params.getModel().getProcessInstanceId().substring(0, 32);
        params.getModel().setProcessInstanceId(processInstanceId);
        return opFormParamDao.findOpFormDataById(params);
    }
}
