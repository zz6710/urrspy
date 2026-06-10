package com.kayak.scheduled;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kayak.common.constant.BusinessStatus;
import com.kayak.common.constant.FieldConstants;
import com.kayak.common.enums.ProcessInstanceStatusEnum;
import com.kayak.common.exception.WorkflowException;
import com.kayak.utils.JsonUtils;
import com.kayak.utils.RemoteInvokeUtil;
import com.kayak.utils.SysUtil;
import com.kayak.web.workflow.domain.WfBusiInfo;
import com.kayak.web.workflow.domain.WfValidateConfig;
import com.kayak.web.workflow.mapper.WfBusiInfoMapper;
import com.kayak.web.workflow.mapper.WfValidateConfigMapper;
import com.kayak.web.workflow.service.IWfProcessService;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 业务回调服务
 *
 * @author yuanjinqiao
 * @date 2021-2-3
 */
@Slf4j
@Service
public class WorkFlowCallbackService {

    @Autowired
    private WfBusiInfoMapper busiInfoDao;

    @Autowired
    private WfValidateConfigMapper validateConfigDao;

    @Autowired
    private IWfProcessService wfProcessService;

    /**
     * 业务回调入口
     *
     * @param busiInfo
     */
    public void execute(WfBusiInfo busiInfo) {
        String userId = SysUtil.getCurrentUserId();
        //业务状态更新为处理中
        LambdaUpdateWrapper<WfBusiInfo> w = new LambdaUpdateWrapper<WfBusiInfo>()
                .eq(WfBusiInfo::getProcessInstanceId, busiInfo.getProcessInstanceId())
                .in(WfBusiInfo::getBusStatus, Arrays.asList(BusinessStatus.READY, BusinessStatus.ERROR))
                .set(WfBusiInfo::getBusStatus, BusinessStatus.PROCESSING)
                .set(WfBusiInfo::getUpdateTime, new Date())
                .set(WfBusiInfo::getUpdateBy, userId);
        int ret = busiInfoDao.update(null, w);
        if (ret < 1) {
            log.info(" 业务[{}]已在处理中", busiInfo.getBusiId());
            return;
        }
        try {
            // 获取表单参数
            //Map<String, Object> busiData = JsonUtils.parseMap(busiInfo.getSubmitData());
            //获取流程变量，流程变量里的表单参数是在审批中动态添加的。
            Map<String, Object> variables = wfProcessService.getProcessVariables(busiInfo.getProcessInstanceId());
            // 回调业务接口
            String returnMsg = this.doExecute(busiInfo, variables);
            // 更新业务为完成状态
            busiInfoDao.updateBusStatus(BusinessStatus.FINISH, returnMsg, userId, busiInfo.getProcessInstanceId(),new Date());

        } catch (Exception e) {
            // 更新业务状态为错误
            busiInfoDao.updateBusStatus(BusinessStatus.ERROR, e.getMessage(), userId, busiInfo.getProcessInstanceId(), new Date());
            log.error("工作流-业务回调失败, 业务信息：{}", busiInfo, e);
            throw new WorkflowException(e.getMessage());
        }
    }

    @Async
    public void execute(String processInstanceId) {
        LambdaQueryWrapper<WfBusiInfo> query = Wrappers.lambdaQuery();
        query.eq(WfBusiInfo::getProcessInstanceId, processInstanceId);
        WfBusiInfo busiInfo = busiInfoDao.selectOne(query);
        this.execute(busiInfo);
    }

    /**
     * 发起回调
     *
     * @param businessData
     */
    private String doExecute(WfBusiInfo busiInfo, Map<String, Object> businessData) {
        businessData.put(FieldConstants.BUSI_CALL_BACK, true);
        businessData.put(FieldConstants.PROCESS_KEY,busiInfo.getProcessKey());
        businessData.put(FieldConstants.PROCESS_INSTANCE_ID,busiInfo.getProcessInstanceId());
        businessData.put(FieldConstants.PROCESS_DEFINITION_ID,busiInfo.getProcessDefinitionId());
        businessData.put(FieldConstants.PROCESS_STATUS, ProcessInstanceStatusEnum.FINISH.getType());
        Map<String, Object> result;
        if (StringUtils.contains(busiInfo.getContentType(),MediaType.APPLICATION_JSON_VALUE)) {
            result = (Map<String, Object>) RemoteInvokeUtil.requestPostJson(busiInfo.getServer(), busiInfo.getUrl(), busiInfo.getCreateBy(), businessData);
        } else {
            result = (Map<String, Object>) RemoteInvokeUtil.requestPostForm(busiInfo.getServer(), busiInfo.getUrl(), busiInfo.getCreateBy(), businessData);
        }
        log.info(" ### 远程交易回调结果: {}", result);
        if (!this.isSuccess(result, busiInfo.getValidateId())) {
            log.error("工作流-执行业务失败，result:{}", result);
            throw new WorkflowException(result.toString());
        }
        return JSONUtil.toJsonStr(result);
    }

    /**
     * 判断业务回调处理是否成功
     *
     * @param result
     * @return
     */
    private boolean isSuccess(Map<String, Object> result, String validateId) {
        if (result == null) {
            return false;
        }
        WfValidateConfig wfValidateConfig = validateConfigDao.selectById(validateId);
        if (wfValidateConfig == null || StringUtils.isBlank(wfValidateConfig.getRule())) {
            throw new WorkflowException("返回报文校验规则不存在，请检查配置");
        }

        Binding bind = new Binding();
        bind.setVariable("result", result);
        GroovyShell groovyShell = new GroovyShell(bind);
        return (Boolean) groovyShell.evaluate(wfValidateConfig.getRule());
    }

}
