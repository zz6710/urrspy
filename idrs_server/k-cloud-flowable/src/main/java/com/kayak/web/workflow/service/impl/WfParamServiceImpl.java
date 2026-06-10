package com.kayak.web.workflow.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kayak.common.constant.FieldConstants;
import com.kayak.common.enums.ParamTypeEnum;
import com.kayak.common.exception.WorkflowException;
import com.kayak.utils.JsonUtils;
import com.kayak.utils.RegexUtil;
import com.kayak.utils.RemoteInvokeUtil;
import com.kayak.utils.StringUtils;
import com.kayak.utils.flow.ModelUtils;
import com.kayak.web.workflow.domain.WfBusiInfo;
import com.kayak.web.workflow.domain.WfEnvItem;
import com.kayak.web.workflow.domain.dto.WfUrlDto;
import com.kayak.web.workflow.mapper.CommonMapper;
import com.kayak.web.workflow.mapper.WfBusiInfoMapper;
import com.kayak.web.workflow.mapper.WfEnvItemMapper;
import com.kayak.web.workflow.service.IWfParamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.HistoryService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.history.HistoricProcessInstance;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程参数Service业务层处理
 *
 * @author yuanjinqiao
 * @date 2022-10-11
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class WfParamServiceImpl implements IWfParamService {

    private final CommonMapper commonMapper;

    private final HistoryService historyService;

    private final WfEnvItemMapper wfEnvItemMapper;

    private final WfBusiInfoMapper wfBusiInfoMapper;

    /**
     * 解析流程参数
     *
     * @param paramName          流程参数名字
     * @param variables
     * @param currentFlowElement
     * @param processInstanceId
     * @param fieldExtensionMap
     * @return
     */
    @Override
    public Object parseWfParam(String paramName, Map<String, Object> variables,
                               FlowElement currentFlowElement, String processInstanceId,
                               Map<String, String> fieldExtensionMap) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        //扩展属性
        Map<String, Object> extensionPropertyMap = ModelUtils.getExtensionProperties(currentFlowElement);

        //构成传输对象
        Map<String, Object> retMap = new HashMap<>();
        retMap.putAll(variables);
        retMap.put(FieldConstants.PROCESS_KEY, historicProcessInstance.getProcessDefinitionKey());
        retMap.put(FieldConstants.PROCESS_INSTANCE_ID, historicProcessInstance.getId());
        retMap.put(FieldConstants.PROCESS_DEFINITION_ID, historicProcessInstance.getProcessDefinitionId());
        retMap.put(FieldConstants.PROCESS_STATUS, historicProcessInstance.getBusinessStatus());
        retMap.putAll(extensionPropertyMap);
        retMap.putAll(fieldExtensionMap == null ? new HashMap<>() : fieldExtensionMap);

        return parseWfParam(paramName, retMap, historicProcessInstance.getId());
    }

    @Override
    public Object parseWfParam(String paramName, DelegateExecution execution) {
        return parseWfParam(paramName, execution.getVariables(), execution.getCurrentFlowElement(), execution.getProcessInstanceId(), null);
    }

    @Override
    public Object parseWfParam(String envItemId, Map<String, Object> varMap, String processInstanceId) {
        //获取流程参数
        WfEnvItem wfEnvItem = wfEnvItemMapper.selectById(envItemId);
        if (wfEnvItem == null) {
            throw new WorkflowException("找不到流程参数【" + envItemId + "】");
        }
        Map<String, Object> allParamMap = new HashMap<>();
        allParamMap.putAll(varMap);
        //获取表单提交参数
        if (StringUtils.isNotEmpty(processInstanceId)) {
            LambdaQueryWrapper<WfBusiInfo> query = Wrappers.lambdaQuery();
            query.eq(WfBusiInfo::getProcessInstanceId, processInstanceId);
            WfBusiInfo busiInfo = wfBusiInfoMapper.selectOne(query);
            if (busiInfo != null && StringUtils.isNotEmpty(busiInfo.getSubmitData())) {
                String submitData = busiInfo.getSubmitData();
                Map submitDataMap = JSONUtil.toBean(submitData, Map.class);
                allParamMap.putAll(submitDataMap);
            }
        }
        String paramName = wfEnvItem.getItemKey();
        String paramType = wfEnvItem.getItemType();
        String paramValue = wfEnvItem.getItemValue();
        if (ParamTypeEnum.URL.getType().equals(paramType)) {
            List<WfUrlDto> wfUrlDtos = JSONUtil.toList(paramValue, WfUrlDto.class);
            for (WfUrlDto wfUrlDto : wfUrlDtos) {
                Object o = null;
                if (HttpMethod.POST.toString().equals(wfUrlDto.getRequestType())) {
                    int i = wfUrlDto.getUrlValue().indexOf("?");
                    if (i > 0) {
                        String param = wfUrlDto.getUrlValue().substring(i + 1);
                        String resolveParam = RegexUtil.getSql(param, allParamMap);
                        Dict paramMap = JsonUtils.parseMap(resolveParam);
                        o = RemoteInvokeUtil.restPostInvoke(wfUrlDto.getUrlValue().substring(0, i), paramMap);
                    } else {
                        //执行url
                        o = RemoteInvokeUtil.restPostInvoke(wfUrlDto.getUrlValue(), allParamMap);
                    }
                    log.info("url参数:{}", allParamMap);
                    log.info("流程参数【{}】对应的url为【{}】,执行结果【{}】", paramName, paramValue, o);
                } else if (HttpMethod.GET.toString().equals(wfUrlDto.getRequestType())) {
                    //解析url
                    String url = RegexUtil.getSql(wfUrlDto.getUrlValue(), allParamMap);
                    //执行url
                    o = RemoteInvokeUtil.restGetInvoke(url);
                    log.info("url参数:{}", allParamMap);
                    log.info("流程参数【{}】对应的url为【{}】,执行结果【{}】", paramName, url, o);
                }
                //保存结果
                allParamMap.put(wfUrlDto.getUrlKey(), o);
            }
            //最终返回的结果为url的最后一个。
            Object o = allParamMap.get(wfUrlDtos.get(wfUrlDtos.size() - 1).getUrlKey());
            log.info("流程参数【{}】最终的执行结果【{}】", paramName, o);
            return o;
        } else if (ParamTypeEnum.SQL.getType().equals(paramType)) {
            //执行sql
            //sql构建sql参数
            Object o;
            String sql = RegexUtil.getSql(paramValue, allParamMap);
            try {
                o = commonMapper.getBySqlRetObject(sql);
            } catch (MyBatisSystemException e) {
                o = commonMapper.getBySqlRetList(sql);
            }
            log.info("sql参数:{}", allParamMap);
            log.info("流程参数【{}】对应的sql为【{}】,执行结果【{}】", paramName, sql, o);
            return o;
        } else if (ParamTypeEnum.FORM_FIELD.getType().equals(paramType)) {
            String value = (String) StringUtils.getJsonValue(allParamMap, paramValue);
            log.info("流程参数【{}】对应的表单字段为【{}】", paramName, value);
            return value;
        }
        return null;
    }

}
