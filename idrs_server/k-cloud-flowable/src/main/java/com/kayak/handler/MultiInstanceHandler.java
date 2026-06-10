package com.kayak.handler;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.common.constant.TaskConstants;
import com.kayak.common.enums.MultiInstanceNumberTypeEnum;
import com.kayak.utils.flow.ModelUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 多实例处理类
 *
 * @author yuanjinqiao
 */
@AllArgsConstructor
@Slf4j
@Component("multiInstanceHandler")
public class MultiInstanceHandler {

    /**
     * 获取用户id
     *
     * @param execution
     * @return
     */
    public Set<String> getUserIds(DelegateExecution execution) {
        FlowElement flowElement = execution.getCurrentFlowElement();
        Set<String> candidateUserIds = new LinkedHashSet<>();
        if (ObjectUtil.isNotEmpty(flowElement) && flowElement instanceof UserTask) {
            UserTask userTask = (UserTask) flowElement;
            candidateUserIds = ModelUtils.getCandidateUserIds(userTask, execution, null, null);
        }
        return candidateUserIds;
    }

    /**
     * 会签是否完成
     *
     * @param execution
     * @param numType   数字类,关联 {@link com.kayak.common.enums.MultiInstanceNumberTypeEnum}
     * @param num       数字
     * @return
     */
    public Boolean isCompleted(DelegateExecution execution, String numType, String num) {
        Map<String, Object> variables = execution.getVariables();
        //拒绝数
        Object variable = variables.get(TaskConstants.MULTI_REFUSE_COUNT);
        Integer refuseNum = variable == null ? 0 : (Integer) variable;

        //会签任务中活动的实例的数量，即还没有完成的实例数量
        Integer nrOfActiveInstances = (Integer) variables.get("nrOfActiveInstances");
        //会签任务中已经完成的实例的数量
        Integer nrOfCompletedInstances = (Integer) variables.get("nrOfCompletedInstances");
        return compute(numType, num, variables, refuseNum, nrOfActiveInstances, nrOfCompletedInstances);
    }

    /**
     * @param numType                数字类,关联 {@link com.kayak.common.enums.MultiInstanceNumberTypeEnum}
     * @param num                    数字
     * @param variables              流程参数
     * @param refuseNum              拒绝人数
     * @param nrOfActiveInstances    未完成的实例数量
     * @param nrOfCompletedInstances 已经完成的实例数
     * @return
     */
    public Boolean compute(String numType, String num, Map<String, Object> variables,
                           Integer refuseNum, Integer nrOfActiveInstances, Integer nrOfCompletedInstances) {
        //会签任务中总实例数
        Integer nrOfInstances = (Integer) variables.get("nrOfInstances");

        //通过数
        Object variable1 = variables.get(TaskConstants.MULTI_PASS_COUNT);
        Integer passNum = variable1 == null ? 0 : (Integer) variable1;
        log.info("会签通过需要人数或比例：{}", num);
        log.info("会签总人数：{}", nrOfInstances);
        log.info("会签已完成的人数：{}", nrOfCompletedInstances);
        log.info("会签未完成的人数：{}", nrOfActiveInstances);
        log.info("会签通过的人数：{}", passNum);
        log.info("会签拒绝的人数：{}", refuseNum);

        if (MultiInstanceNumberTypeEnum.NUMBER.getType().equals(numType)) {
            Integer numInt = Integer.valueOf(num);
            if (passNum >= numInt) {
                //会签通过
                return true;
            }
            //如果未完成的数量，小于还需要多少才能通过的人数，则会签任务完不成了。
            if (nrOfActiveInstances < numInt - passNum) {
                //会签无法通过
                return true;
            }
            return false;
        } else if (MultiInstanceNumberTypeEnum.SCALE.getType().equals(numType)) {
            BigDecimal numBigDecimal = new BigDecimal(num);
            BigDecimal passBigDecimal = new BigDecimal(passNum);
            BigDecimal nrOfInstancesBigDecimal = new BigDecimal(nrOfInstances);
            BigDecimal refuseBigDecimal = new BigDecimal(refuseNum);

            //通过比例=通过数/会签总人数
            BigDecimal passScale = passBigDecimal.divide(nrOfInstancesBigDecimal, 2, BigDecimal.ROUND_HALF_UP);
            //通过比例>=会签通过需要的比例
            if (passScale.compareTo(numBigDecimal) > -1) {
                //会签通过
                return true;
            }
            //未完成的比例
            BigDecimal nrOfActiveInstanceseScaleBigDecimal = new BigDecimal(nrOfActiveInstances).divide(nrOfInstancesBigDecimal, 2, BigDecimal.ROUND_HALF_UP);
            //还需要多少比例才能通过
            BigDecimal subtract = numBigDecimal.subtract(passScale);
            //如果未完成的比例<还需要多少比例才能通过
            if (nrOfActiveInstanceseScaleBigDecimal.compareTo(subtract) == -1) {
                //会签无法通过
                return true;
            }
            return false;
        } else {
            return false;
        }
    }
}
