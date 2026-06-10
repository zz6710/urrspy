package com.kayak.utils.flow;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kayak.common.constant.ProcessConstants;
import com.kayak.common.constant.TaskConstants;
import com.kayak.common.enums.RoleTypeEnum;
import com.kayak.common.exception.WorkflowException;
import com.kayak.factory.SystemServiceFactory;
import com.kayak.utils.StringUtils;
import com.kayak.web.system.domain.SysOrg;
import com.kayak.web.system.domain.SysUser;
import com.kayak.web.workflow.service.IWfParamService;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.impl.util.io.StringStreamSource;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yuanjinqiao
 * @createTime 2022/3/26 19:04
 */
@Component
public class ModelUtils {
    @Autowired
    private SystemServiceFactory systemServiceFactory;

    @Autowired
    private IWfParamService wfParamService;

    private static ModelUtils modelUtils;

    private static final BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();

    @PostConstruct
    public void init() {
        modelUtils = this;
    }

    /**
     * xml转bpmnModel对象
     *
     * @param xml xml
     * @return bpmnModel对象
     */
    public static BpmnModel getBpmnModel(String xml) {
        return bpmnXMLConverter.convertToBpmnModel(new StringStreamSource(xml), false, false);
    }

    /**
     * bpmnModel转xml对象
     *
     * @param bpmnModel bpmnModel对象
     * @return xml
     */
    public static byte[] getBpmnXml(BpmnModel bpmnModel) {
        return bpmnXMLConverter.convertToXML(bpmnModel);
    }

    /**
     * 获取开始节点
     *
     * @param model bpmnModel对象
     * @return 开始节点（未找到开始节点，返回null）
     */
    public static StartEvent getStartEvent(BpmnModel model) {
        Process process = model.getMainProcess();
        FlowElement startElement = process.getInitialFlowElement();
        if (startElement instanceof StartEvent) {
            return (StartEvent) startElement;
        }
        return getStartEvent(process.getFlowElements());
    }

    /**
     * 获取开始节点
     *
     * @param flowElements 流程元素集合
     * @return 开始节点（未找到开始节点，返回null）
     */
    public static StartEvent getStartEvent(Collection<FlowElement> flowElements) {
        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof StartEvent) {
                return (StartEvent) flowElement;
            }
        }
        return null;
    }

    /**
     * 获取所有用户任务节点
     *
     * @param model bpmnModel对象
     * @return 用户任务节点列表
     */
    public static Collection<UserTask> getAllUserTaskEvent(BpmnModel model) {
        Process process = model.getMainProcess();
        Collection<FlowElement> flowElements = process.getFlowElements();
        return getAllUserTaskEvent(flowElements, null);
    }

    /**
     * 获取所有用户任务节点
     *
     * @param flowElements 流程元素集合
     * @param allElements  所有流程元素集合
     * @return 用户任务节点列表
     */
    public static Collection<UserTask> getAllUserTaskEvent(Collection<FlowElement> flowElements, Collection<UserTask> allElements) {
        allElements = allElements == null ? new ArrayList<>() : allElements;
        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof UserTask) {
                allElements.add((UserTask) flowElement);
            }
            if (flowElement instanceof SubProcess) {
                // 继续深入子流程，进一步获取子流程
                allElements = getAllUserTaskEvent(((SubProcess) flowElement).getFlowElements(), allElements);
            }
        }
        return allElements;
    }

    public static String getExtensionElementText(BaseElement baseElement, String processConstants) {
        Map<String, List<ExtensionElement>> extensionElementsMap = baseElement.getExtensionElements();
        if (extensionElementsMap == null) {
            return "";
        }
        List<ExtensionElement> extensionElements = extensionElementsMap.get(processConstants);
        if (extensionElements == null || extensionElements.size() == 0) {
            return "";
        }
        String env = extensionElements.get(0).getElementText();
        if (ObjectUtil.isEmpty(env)) {
            return "";
        }
        return env;
    }

    /**
     * 获取扩展属性
     *
     * @param baseElement
     * @return
     */
    public static Map<String, Object> getExtensionProperties(BaseElement baseElement) {
        if (baseElement == null) {
            return Collections.EMPTY_MAP;
        }
        Map<String, List<ExtensionElement>> extensionElements = baseElement.getExtensionElements();
        if (CollectionUtil.isEmpty(extensionElements)) {
            return Collections.EMPTY_MAP;
        }
        List<ExtensionElement> properties = extensionElements.get("properties");
        if (CollectionUtil.isEmpty(properties)) {
            return Collections.EMPTY_MAP;
        }
        Map<String, List<ExtensionElement>> childElements = properties.get(0).getChildElements();
        if (CollectionUtil.isEmpty(childElements)) {
            return Collections.EMPTY_MAP;
        }
        HashMap<String, Object> extensionPropertyMap = new HashMap<>();
        List<ExtensionElement> property = childElements.get("property");
        for (ExtensionElement extensionElement : property) {
            Map<String, List<ExtensionAttribute>> attributes = extensionElement.getAttributes();
            if (CollectionUtil.isEmpty(attributes)) {
                continue;
            }
            List<ExtensionAttribute> name = attributes.get("name");
            List<ExtensionAttribute> value = attributes.get("value");
            if (CollectionUtil.isEmpty(name) || CollectionUtil.isEmpty(value)) {
                continue;
            }
            String mapKey = "";
            String mapValue = "";
            for (ExtensionAttribute extensionAttribute : name) {
                mapKey = extensionAttribute.getValue();
            }
            for (ExtensionAttribute extensionAttribute : value) {
                mapValue = extensionAttribute.getValue();
            }
            extensionPropertyMap.put(mapKey, mapValue);
        }
        return extensionPropertyMap;
    }

    /**
     * 查找流程申请任务节点(第一个)
     *
     * @param bpmnModel
     * @return
     */
    public static UserTask getApplyTask(BpmnModel bpmnModel) {
        for (FlowElement flowElement : bpmnModel.getMainProcess().getFlowElements()) {
            if (flowElement instanceof StartEvent) {
                return (UserTask) bpmnModel.getFlowElement(((StartEvent) flowElement).getOutgoingFlows().get(0).getTargetRef());
            }
        }
        throw new WorkflowException("当前流程未设置申请节点");
    }

    /**
     * 获取用户人的候选用户
     *
     * @param userTask
     * @param execution
     * @param variables
     * @param processInstanceId
     * @return
     */
    public static Set<String> getCandidateUserIds(UserTask userTask, DelegateExecution execution, Map<String, Object> variables, String processInstanceId) {
        if (CollectionUtil.isEmpty(variables)) {
            variables = execution.getVariables();
        }

        HashSet<String> candidateUserIds = new LinkedHashSet<>();
        //处理用户
        if (CollUtil.isNotEmpty(userTask.getCandidateUsers())) {
            candidateUserIds.addAll(userTask.getCandidateUsers());
        }
        //处理角色
        if (CollUtil.isNotEmpty(userTask.getCandidateGroups())) {
            //角色类型
            String roleType = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.ROLE_TYPE);
            //流程发起人信息
            String startUserId = (String) variables.get(TaskConstants.PROCESS_INITIATOR);
            SysUser startUserInfo = modelUtils.systemServiceFactory.createService().getUserInfo(startUserId);
            //任务的候选角色
            List<String> roleIdList = userTask.getCandidateGroups();
            //根据角色查询用户
            List<SysUser> users = modelUtils.systemServiceFactory.createService().getUserByRoleIds(roleIdList);
            for (SysUser user : users) {
                //获取当前用户的下级机构
                List<SysOrg> lowerOrgs = modelUtils.systemServiceFactory.createService().getLowerOrgs(user.getOrgno());
                List<String> lowerOrgNo = lowerOrgs.stream().map(t -> t.getOrgno()).collect(Collectors.toList());
                //需要考虑上级机构、同级机构
                if (RoleTypeEnum.UPPER_ORG.getType().equals(roleType)) {
                    //判断申请人的的机构是否为当前用户的下级机构
                    if (lowerOrgNo.contains(startUserInfo.getOrgno())) {
                        candidateUserIds.add(user.getUserid());
                    }
                } else if (RoleTypeEnum.PEER_ORG.getType().equals(roleType)) {
                    //判断申请人的的机构是否为当前用户的下级机构
                    if (user.getOrgno().equals(startUserInfo.getOrgno())) {
                        candidateUserIds.add(user.getUserid());
                    }
                } else {
                    candidateUserIds.add(user.getUserid());
                }
            }
        }
        //处理流程参数
        String candidateParam = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.CANDIDATE_PARAM);
        if (StringUtils.isNotEmpty(candidateParam)) {
            Object userIds;
            if (execution != null) {
                userIds = modelUtils.wfParamService.parseWfParam(candidateParam, execution);
            } else {
                userIds = modelUtils.wfParamService.parseWfParam(candidateParam, variables, userTask, processInstanceId, null);
            }
            if (userIds instanceof List) {
                candidateUserIds.addAll((List<String>) userIds);
            } else if (userIds instanceof String) {
                candidateUserIds.add((String) userIds);
            } else {
                throw new WorkflowException("流程参数[" + candidateParam + "]用于动态审批人时,返回值应该为List<String>或者String");
            }
        }
        return candidateUserIds;
    }

}
