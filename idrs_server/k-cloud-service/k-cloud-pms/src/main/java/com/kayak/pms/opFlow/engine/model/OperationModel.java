package com.kayak.pms.opFlow.engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.engine.handlers.RoleAssignmentHandler;
import com.kayak.pms.opFlow.engine.handlers.UserAssignmentHandler;
import com.kayak.pms.opFlow.engine.helper.AssertHelper;
import com.kayak.pms.opFlow.engine.helper.ClassHelper;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 19/03/2017.
 */
@Data
public class OperationModel extends WorkModel {
    private static final long serialVersionUID = -7391424574896986711L;
    /**
     * 类型：普通任务
     */
    public static final String PERFORMTYPE_ANY = "ANY";
    /**
     * 类型：参与者fork任务
     */
    public static final String PERFORMTYPE_ALL = "ALL";

    /**
     * 参与方式
     * any：任何一个参与者处理完即执行下一步
     * all：所有参与者都完成，才可执行下一步
     */
    private String performType = PERFORMTYPE_ANY;

    /**
     * 分配参与者处理类型
     */
    @JsonIgnore
    private String userAssignmentHandler;

    /**
     * 分配参与者角色处理类型
     */
    @JsonIgnore
    private String roleAssignmentHandler;

    /**
     * 分配参与者处理对象
     */
    @JsonIgnore
    private UserAssignmentHandler userAssignmentHandlerObject;

    /**
     * 分配参与者处理对象
     */
    @JsonIgnore
    private RoleAssignmentHandler roleAssignmentHandlerObject;

    /**
     * 任务参与者，用逗号分隔,可以指定多个
     */
    private String actorIds;

    /**
     * 任务参与者角色，用逗号分隔,可以指定多个
     */
    private String roleIds;

    /**
     * 参与者sql。可以执行该sql得到对应的的参与者Id
     */
    private String actorSql;
    private String fields;//动态查询所需要的字段信息
    private String fieldsText;

    private String detailFields;//字段详情
    private String detailFieldsText;
    private String enableAttachment;

    private String useRole; //特殊字段, 在计算任务的参与者的时候，角色和参与者是否会同时存在。默认为 是同时计算的
    private String specifiedUser;//是否指定用户

    private String refuseHandler;
    private String applyRefuseHandler;
    /**
     * 节点绑定的功能
     */
    private String busiId;
    private String id;
    private String processId;
    private String processInstanceId;

    public boolean isPerformAny() {
        return PERFORMTYPE_ANY.equalsIgnoreCase(this.performType);
    }

    public boolean isPerformAll() {
        return PERFORMTYPE_ALL.equalsIgnoreCase(this.performType);
    }

    public void setPerformType(String performType) {
        this.performType = (StringHelper.isEmpty(performType) ? PERFORMTYPE_ANY : performType);
    }

    public void setUserAssignmentHandler(String userAssignmentHandler) {
        if (StringHelper.isNotEmpty(userAssignmentHandler)) {
            this.userAssignmentHandler = userAssignmentHandler;
            userAssignmentHandlerObject = (UserAssignmentHandler) ClassHelper.newInstance(userAssignmentHandler);
            AssertHelper.notNull(userAssignmentHandlerObject, "分配参与者处理类实例化失败");
        }
    }

    public void setRoleAssignmentHandler(String roleAssignmentHandler) {
        if (StringHelper.isNotEmpty(roleAssignmentHandler)) {
            this.roleAssignmentHandler = roleAssignmentHandler;
            roleAssignmentHandlerObject = (RoleAssignmentHandler) ClassHelper.newInstance(roleAssignmentHandler);
            AssertHelper.notNull(roleAssignmentHandlerObject, "分配参与者角色处理类实例化失败");
        }
    }

    /**
     * 获取后续任务模型集合（方便预处理）
     *
     * @return 模型集合
     * @deprecated
     */
    public List<OperationModel> getNextOperationModels() {
        List<OperationModel> models = new ArrayList<>();
        for (TransitionModel tm : this.getOutputs()) {
            addNextModels(models, tm, OperationModel.class);
        }
        return models;
    }


    @Override
    protected void exec(Execution execution) throws Exception {
        if (performType == null || performType.equalsIgnoreCase(PERFORMTYPE_ANY)) {
            // any方式，直接执行输出变迁
            runOutTransition(execution);
        } else {
            // all方式，需要判断是否已全部合并
            // 由于all方式分配任务，是每人一个任务
            // 那么此时需要判断之前分配的所有任务都执行完成后，才可执行下一步，否则不处理
            if (execution.isMerged()) {
                runOutTransition(execution);
            }
        }
    }
}
