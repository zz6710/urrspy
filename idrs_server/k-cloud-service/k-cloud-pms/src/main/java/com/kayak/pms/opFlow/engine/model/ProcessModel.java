package com.kayak.pms.opFlow.engine.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 19/03/2017.
 */
public class ProcessModel extends BaseModel {

    private static final long serialVersionUID = 3058092871801214322L;

    private static final Logger log = LoggerFactory.getLogger(ProcessModel.class);

    /**
     * 节点元素集合
     */
    private List<NodeModel> nodes = new ArrayList<>();
    private List<TaskModel> taskModels = new ArrayList<>();
    private List<OperationModel> operationModels = null;

    /**
     * 返回当前流程定义的所有工作任务节点模型
     *
     * @return
     */
    public List<WorkModel> getWorkModels() {
        List<WorkModel> models = new ArrayList<WorkModel>();
        for (NodeModel nodeModel : nodes) {
            if (nodeModel instanceof WorkModel) {
                models.add((WorkModel) nodeModel);
            }
        }
        return models;
    }

    /**
     * 获取所有的有序任务模型集合
     *
     * @return
     */
    public List<TaskModel> getTaskModels() {
        if (taskModels.isEmpty()) {
            try {
                buildModels(taskModels, getStart().getNextModels(TaskModel.class), TaskModel.class);
            } catch (Exception e) {
                log.error("获取有序任务模型集合失败！", e);
            }
        }
        return taskModels;
    }

    public List<OperationModel> getOperationModels() {
        if (operationModels == null) {
            operationModels = new ArrayList<>();
        }
        if (operationModels.isEmpty()) {
            try {
                buildModels(operationModels, getStart().getNextModels(OperationModel.class), OperationModel.class);
            } catch (Exception e) {
                log.error("获取功能任务模型集合失败！", e);
            }
        }
        return operationModels;
    }

    public <T> List<T> getModels(Class<T> clazz) {
        List<T> models = new ArrayList<T>();
        try {
            buildModels(models, getStart().getNextModels(clazz), clazz);
        } catch (Exception e) {
            log.error("获取模型集合失败！", e);
        }
        return models;
    }

    /***
     *  获取process定义的指定节点名称的节点模型
     * @param nodeName
     * @return
     */
    public NodeModel getNode(String nodeName){
        for(NodeModel node : nodes) {
            if(node.getName().equals(nodeName)) {
                return node;
            }
        }
        return null;
    }

    public TaskModel getTaskNode(String nodeName){
        for(TaskModel node : taskModels) {
            if(node.getName().equals(nodeName)) {
                return node;
            }
        }
        return null;
    }

    public OperationModel getOperationNode(String nodeName){
        if (operationModels == null) {
            this.getOperationModels();
        }
        for(OperationModel node : operationModels) {
            if(node.getName().equals(nodeName)) {
                return node;
            }
        }
        return null;
    }

    private <T> void buildModels(List<T> models, List<T> nextModels, Class<T> clazz) {
        for (T nextModel : nextModels) {
            if (!models.contains(nextModel)) {
                models.add(nextModel);
                buildModels(models, ((NodeModel) nextModel).getNextModels(clazz), clazz);
            }
        }

    }

    /*
        获取process定义的start节点模型
     */
    public StartModel getStart() {
        for (NodeModel nodeModel : nodes) {
            if (nodeModel instanceof StartModel) {
                return (StartModel) nodeModel;
            }
        }
        return null;
    }

    public List<NodeModel> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeModel> nodes) {
        this.nodes = nodes;
    }

    public void setTaskModels(List<TaskModel> taskModels) {
        this.taskModels = taskModels;
    }

    public void setOperationModels(List<OperationModel> operationModels) {
        this.operationModels = operationModels;
    }
}
