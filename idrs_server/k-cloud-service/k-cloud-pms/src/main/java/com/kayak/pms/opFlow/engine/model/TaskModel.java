package com.kayak.pms.opFlow.engine.model;

import com.kayak.helper.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 19/03/2017.
 */
public class TaskModel extends WorkModel {
    private static final long serialVersionUID = -7391424574896986711L;
    /**
     * 类型：普通任务
     */
    public static final String PERFORMTYPE_ANY = "ANY";
    /**
     * 类型：参与者fork任务
     */
    public static final String PERFORMTYPE_ALL = "ALL";


    @Override
    protected void exec(Execution execution) throws Exception {
        if (performType == null || performType.equalsIgnoreCase(PERFORMTYPE_ANY)) {
            /**
             * any方式，直接执行输出变迁
             */
            runOutTransition(execution);
        } else {
            /**
             * all方式，需要判断是否已全部合并
             * 由于all方式分配任务，是每人一个任务
             * 那么此时需要判断之前分配的所有任务都执行完成后，才可执行下一步，否则不处理
             */
            //fire(new MergeActorHandler(getName()), execution);
            if (execution.isMerged()) {
                runOutTransition(execution);
            }
        }
    }

    /**
     * 参与方式
     * any：任何一个参与者处理完即执行下一步
     * all：所有参与者都完成，才可执行下一步
     */
    private String performType = PERFORMTYPE_ANY;

    /**
     * 参与者sql。可以执行该sql得到对应的的参与者Id
     */
    private String actorSql;

    private String fields;//动态查询所需要的字段信息
    private String fieldsText;

    private String detailFields;//字段详情
    private String detailFieldsText;
    private String enableAttachment;

    private String refuseHandler;
    private String applyRefuseHandler;
    /**
     * 节点绑定的审批流
     */
    private String workflow;

    public boolean isPerformAny() {
        return PERFORMTYPE_ANY.equalsIgnoreCase(this.performType);
    }

    public boolean isPerformAll() {
        return PERFORMTYPE_ALL.equalsIgnoreCase(this.performType);
    }

    public String getPerformType() {
        return performType;
    }

    public void setPerformType(String performType) {
        this.performType = (StringHelper.isEmpty(performType) ? PERFORMTYPE_ANY : performType);
    }


    /**
     * 获取后续任务模型集合（方便预处理）
     *
     * @return 模型集合
     * @deprecated
     */
    public List<TaskModel> getNextTaskModels() {
        List<TaskModel> models = new ArrayList<TaskModel>();
        for (TransitionModel tm : this.getOutputs()) {
            addNextModels(models, tm, TaskModel.class);
        }
        return models;
    }

    public String getActorSql() {
        return actorSql;
    }

    public void setActorSql(String actorSql) {
        this.actorSql = actorSql;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getFieldsText() {
        return fieldsText;
    }

    public void setFieldsText(String fieldsText) {
        this.fieldsText = fieldsText;
    }

    public String getEnableAttachment() {
        return enableAttachment;
    }

    public void setEnableAttachment(String enableAttachment) {
        this.enableAttachment = enableAttachment;
    }

    public String getDetailFields() {
        return detailFields;
    }

    public void setDetailFields(String detailFields) {
        this.detailFields = detailFields;
    }

    public String getDetailFieldsText() {
        return detailFieldsText;
    }

    public void setDetailFieldsText(String detailFieldsText) {
        this.detailFieldsText = detailFieldsText;
    }

    public String getUseRole() {
        return useRole;
    }

    public void setUseRole(String useRole) {
        this.useRole = useRole;
    }

    public String getRefuseHandler() {
        return refuseHandler;
    }

    public void setRefuseHandler(String refuseHandler) {
        this.refuseHandler = refuseHandler;
    }

    public void setSpecifiedUser(String specifiedUser) {
        this.specifiedUser = specifiedUser;
    }

    public String getApplyRefuseHandler() {
        return applyRefuseHandler;
    }

    public void setApplyRefuseHandler(String applyRefuseHandler) {
        this.applyRefuseHandler = applyRefuseHandler;
    }

    public String getWorkflow() {
        return workflow;
    }

    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }
}
