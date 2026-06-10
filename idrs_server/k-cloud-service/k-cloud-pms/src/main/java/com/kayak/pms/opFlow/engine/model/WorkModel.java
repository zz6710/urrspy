package com.kayak.pms.opFlow.engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.engine.entity.SelectEntity;
import com.kayak.pms.opFlow.engine.handlers.RoleAssignmentHandler;
import com.kayak.pms.opFlow.engine.handlers.UserAssignmentHandler;
import com.kayak.pms.opFlow.engine.helper.AssertHelper;
import com.kayak.pms.opFlow.engine.helper.ClassHelper;

import java.util.List;

/**
 * Created by daniel on 19/03/2017.
 */
public abstract class WorkModel extends NodeModel {
    private static final long serialVersionUID = 2851899081002829555L;

    private String dynamicFormId;
    private String formUrl;
    private String formId;
    // 任务上下文
    private String envTask;
    // 更新上下文
    private String updateEnvTask;
    private String quartzExpr;
    //用于控制任务节点按钮的显示与隐藏
    private String btns;

    /**
     * 任务参与者，用逗号分隔,可以指定多个
     */
    protected String actorIds;

    /**
     * 任务参与者角色，用逗号分隔,可以指定多个
     */
    protected String roleIds;

    protected String useRole; //特殊字段, 在计算任务的参与者的时候，角色和参与者是否会同时存在。默认为 是同时计算的
    protected String specifiedUser;//是否指定用户

    /**
     * 分配参与者处理类型
     */
    @JsonIgnore
    protected String userAssignmentHandler;

    /**
     * 分配参与者角色处理类型
     */
    @JsonIgnore
    protected String roleAssignmentHandler;

    /**
     * 分配参与者处理对象
     */
    @JsonIgnore
    protected UserAssignmentHandler userAssignmentHandlerObject;

    /**
     * 分配参与者处理对象
     */
    @JsonIgnore
    protected RoleAssignmentHandler roleAssignmentHandlerObject;

    private List<SelectEntity> rejectTaskSelect;

    public String getFormUrl() {
        return formUrl;
    }

    public void setFormUrl(String formUrl) {
        this.formUrl = formUrl;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getBtns() {
        return btns;
    }

    public void setBtns(String btns) {
        this.btns = btns;
    }

    public List<SelectEntity> getRejectTaskSelect() {
        return rejectTaskSelect;
    }

    public void setRejectTaskSelect(List<SelectEntity> rejectTaskSelect) {
        this.rejectTaskSelect = rejectTaskSelect;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getEnvTask() {
        return envTask;
    }

    public void setEnvTask(String envTask) {
        this.envTask = envTask;
    }

    public String getQuartzExpr() {
        return quartzExpr;
    }

    public void setQuartzExpr(String quartzExpr) {
        this.quartzExpr = quartzExpr;
    }

    public String getUpdateEnvTask() {
        return updateEnvTask;
    }

    public void setUpdateEnvTask(String updateEnvTask) {
        this.updateEnvTask = updateEnvTask;
    }

    public String getDynamicFormId() {
        return dynamicFormId;
    }

    public void setDynamicFormId(String dynamicFormId) {
        this.dynamicFormId = dynamicFormId;
    }

    public String getActorIds() {
        return actorIds;
    }

    public void setActorIds(String actorIds) {
        this.actorIds = actorIds;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getUseRole() {
        return useRole;
    }

    public void setUseRole(String useRole) {
        this.useRole = useRole;
    }

    public String getSpecifiedUser() {
        return specifiedUser;
    }
    public void setSpecifiedUser(String specifiedUser) {
        this.specifiedUser = specifiedUser;
    }

    public UserAssignmentHandler getUserAssignmentHandlerObject() {
        return userAssignmentHandlerObject;
    }

    public RoleAssignmentHandler getRoleAssignmentHandlerObject() {
        return roleAssignmentHandlerObject;
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

    public String getUserAssignmentHandler() {
        return userAssignmentHandler;
    }

    public String getRoleAssignmentHandler() {
        return roleAssignmentHandler;
    }
    public void setAssignmentHandlerObject(UserAssignmentHandler assignmentHandlerObject) {
        this.userAssignmentHandlerObject = assignmentHandlerObject;
    }
}
