package com.kayak.pms.opFlow.engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.engine.entity.SelectEntity;
import com.kayak.pms.opFlow.engine.entity.Task;
import com.kayak.pms.opFlow.engine.exception.WorkflowException;
import com.kayak.pms.opFlow.engine.handlers.ForkHandler;
import com.kayak.pms.opFlow.engine.helper.AssertHelper;
import com.kayak.pms.opFlow.engine.helper.ClassHelper;
import com.kayak.pms.opFlow.engine.intercepter.TaskInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 19/03/2017.
 */
public abstract class NodeModel extends BaseModel implements Action {

    private static final long serialVersionUID = 5602491214181490627L;
    private static final Logger log = LoggerFactory.getLogger(NodeModel.class);

    /**
     * 局部前置拦截器
     */
    @JsonIgnore
    private String preInterceptors;
    /**
     * 局部后置拦截器
     */
    @JsonIgnore
    private String postInterceptors;

    /**
     * 输入变迁集合
     */
    @JsonIgnore
    private List<TransitionModel> inputs = new ArrayList<TransitionModel>();
    /**
     * 输出变迁集合
     */
    @JsonIgnore
    private List<TransitionModel> outputs = new ArrayList<TransitionModel>();


    /**
     * 前置局部拦截器实例集合
     */
    @JsonIgnore
    private List<TaskInterceptor> preInterceptorList = new ArrayList<TaskInterceptor>();

    @JsonIgnore
    private List<TaskInterceptor> postInterceptorList = new ArrayList<TaskInterceptor>();

    /**
     * 具体节点模型需要完成的执行逻辑
     *
     * @param execution 执行对象
     */
    protected abstract void exec(Execution execution) throws Exception;

    /**
     * 普通节点，调用该方法
     *
     * @param execution
     */
    @Override
    public void execute(Execution execution) throws Exception {
        exec(execution);
        //后置拦截
        //intercept(postInterceptorList, execution);
    }

    /**
     * 运行变迁继续执行
     *
     * @param execution 执行对象
     */
    protected void runOutTransition(Execution execution) throws Exception {
        if (getOutputs().size() == 0) {
            throw new WorkflowException("【" +execution.getTask().getName() +"】任务必须要配置连线");
        }
        for (TransitionModel tm : getOutputs()) {
            if (forkCanRun(execution, tm) && canRun(execution, tm)) {
                tm.setEnabled(true);
                tm.execute(execution);
            }
        }
    }

    /**
     * 判断 fork 是否可执行, 如果是 fork 节点，则需要进行计算
     * 默认可执行
     *
     * @param execution
     * @param tm
     * @return
     */
    private boolean forkCanRun(Execution execution, TransitionModel tm) {
        NodeModel source = tm.getSource();
        if (source instanceof ForkModel) {
            ForkModel model = (ForkModel) source;
            String forkHandler = model.getForkHandler();
            if (StringHelper.isNotEmpty(forkHandler)) {
                try {
                    Class<?> forkHandlerClass = Class.forName(forkHandler);
                    ForkHandler forkHandlerObj = (ForkHandler) forkHandlerClass.newInstance();
                    List<String> decides = forkHandlerObj.decide(execution);
                    if (decides!= null && decides.contains(tm.getTo())) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (ClassNotFoundException e) {
                    throw new WorkflowException(e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    throw new WorkflowException(e.getMessage(), e);
                } catch (InstantiationException e) {
                    throw new WorkflowException(e.getMessage(), e);
                }
            }
        }
        return true;
    }

    /**
     * 如果当前节点是任务节点，并且该节点配置了多个输出线，那么就需要排除配置的可驳回节点(任务节点本身只能有一条线)
     *
     * @param execution
     * @return
     */
    private boolean canRun(Execution execution, TransitionModel tm) {
        String to = tm.getTo();
        Task task = execution.getTask();
        if (execution.getStart()) {//如果该节点是申请节点，那么可执行
            return true;
        }
        if (task != null) {
            NodeModel node = execution.getProcess().getProcessModel().getNode(task.getName());
            if (node instanceof TaskModel) {
                TaskModel taskModel = (TaskModel) node;
                List<SelectEntity> rejectTaskSelects = taskModel.getRejectTaskSelect();
                if (rejectTaskSelects != null) {
                    for (SelectEntity selectEntity : rejectTaskSelects) {
                        if (to.equals(selectEntity.getValue())) {
                            return false;
                        }
                    }
                }
            }
            // TODO 如果当前任务是 fork 节点，判断是否有接口，有则计算 to 是否 满足条件
        }
        return true;
    }

    public <T> List<T> getNextModels(Class<T> clazz) {
        List<T> models = new ArrayList<T>();
        for (TransitionModel tm : this.getOutputs()) {
            addNextModels(models, tm, clazz);
        }
        return models;
    }

    protected <T> void addNextModels(List<T> models, TransitionModel tm, Class<T> clazz) {
        if (clazz.isInstance(tm.getTarget())) {
            models.add((T) tm.getTarget());
        } else {
            for (TransitionModel tm2 : tm.getTarget().getOutputs()) {
                addNextModels(models, tm2, clazz);
            }
        }
    }

    public List<TaskModel> getNextTaskModels() {
        List<TaskModel> models = new ArrayList<>();
        for (TransitionModel tm : this.getOutputs()) {
            addNextTaskModels(models, tm);
        }
        return models;
    }
    protected void addNextTaskModels(List<TaskModel> models, TransitionModel tm) {
        if (tm.getTarget() instanceof TaskModel) {
            models.add((TaskModel) tm.getTarget());
        } else if (tm.getTarget() instanceof JoinModel) {
            for (TransitionModel tm2 : tm.getTarget().getOutputs()) {
                addNextTaskModels(models, tm2);
            }
        }
    }

    public <T> List<T> getAllNextModels(Class<T> clazz) {
        List<T> models = new ArrayList<T>();
        for (TransitionModel tm : this.getOutputs()) {
            addAllNextModels(models, tm, clazz);
        }
        return models;
    }

    protected <T> void addAllNextModels(List<T> models, TransitionModel tm, Class<T> clazz) {
        if (clazz.isInstance(tm.getTarget())) {
            if (models.contains(tm.getTarget())) {
                models.remove(tm.getTarget());
            }
            models.add((T) tm.getTarget());
            for (TransitionModel output : tm.getTarget().getOutputs()) {
                addAllNextModels(models, output, clazz);
            }
        } else {
            for (TransitionModel tm2 : tm.getTarget().getOutputs()) {
                addAllNextModels(models, tm2, clazz);
            }
        }
    }

    public <T> List<T> getDirectNextModels(Class<T> clazz) {
        List<T> models = new ArrayList<T>();
        for (TransitionModel tm : this.getOutputs()) {
            if (clazz.isInstance(tm.getTarget())) {
                models.add((T) tm.getTarget());
            }
        }
        return models;
    }


    public <T> List<T> getPrevModels(Class<T> clazz) {
        List<T> models = new ArrayList<>();
        for (TransitionModel tm : this.getInputs()) {
            if (clazz.isInstance(tm.getSource())) {
                models.add((T) tm.getSource());
            }
        }
        return models;
    }

    public <T> T getJoinModel(Class<T> clazz) {
        List<TransitionModel> outputs = this.getOutputs();
        if (outputs.size() > 0) {
            TransitionModel transitionModel = outputs.get(0);
            while (transitionModel != null && transitionModel.getTarget().getInputs().size() == 1) {
                if (transitionModel.getTarget().getOutputs().size() > 0) {
                    transitionModel = transitionModel.getTarget().getOutputs().get(0);
                } else {
                    // 遇到输出为0，跳出循环
                    transitionModel = null;
                }
            }
            if (transitionModel != null) {
                // 如果当前节点是指定的类型，直接返回
                if (clazz.isInstance(transitionModel.getTarget())) {
                    return (T) transitionModel.getTarget();
                } else {
                    // 如果是网关节点，则继续获取后续节点
                    return transitionModel.getTarget().getNextModels(clazz).get(0);
                }
            }
        }
        return null;
    }

    /**
     * 根据父节点模型、当前节点模型判断是否可退回。可退回条件：
     * 1、满足中间无fork、join、subprocess模型
     * 2、满足父节点模型如果为任务模型时，参与类型为any
     *
     * @param parent 父节点模型
     * @return 是否可以退回
     */
    public static boolean canRejected(NodeModel current, NodeModel parent) {
        if (parent instanceof TaskModel && !((TaskModel) parent).isPerformAny()) {
            return false;
        }
        boolean result = false;
        for (TransitionModel tm : current.getInputs()) {
            NodeModel source = tm.getSource();
            if (source == parent) {
                return true;
            }
            if (source instanceof ForkModel
                    || source instanceof JoinModel
                    || source instanceof SubProcessModel
                    || source instanceof StartModel) {
                continue;
            }
            result = result || canRejected(source, parent);
        }
        return result;
    }

    private void intercept(List<TaskInterceptor> interceptors, Execution execution) {
        try {
            for (TaskInterceptor interceptor : interceptors) {
                interceptor.intercept(execution);
            }
        } catch (Exception e) {
            log.error("拦截器执行失败: {}", e);
        }
    }

    public boolean nextIs(NodeModel nodeModel) {
        for (TransitionModel output : this.getOutputs()) {
            if (output.getTarget().equals(nodeModel)) {
                return true;
            }
        }
        return false;
    }

    public List<TransitionModel> getInputs() {
        return inputs;
    }

    public void setInputs(List<TransitionModel> inputs) {
        this.inputs = inputs;
    }

    public List<TransitionModel> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TransitionModel> outputs) {
        this.outputs = outputs;
    }

    public void setPreInterceptors(String preInterceptors) {
        this.preInterceptors = preInterceptors;
        addInterceptorsToList(preInterceptors, preInterceptorList);
    }

    public void setPostInterceptors(String postInterceptors) {
        this.postInterceptors = postInterceptors;
        addInterceptorsToList(postInterceptors, postInterceptorList);
    }

    private void addInterceptorsToList(String interceptors, List<TaskInterceptor> interceptorList) {
        if (StringHelper.isNotEmpty(interceptors)) {
            for (String interceptor : interceptors.split(",")) {
                TaskInterceptor instance = (TaskInterceptor) ClassHelper.newInstance(interceptor);
                AssertHelper.notNull(instance, interceptor + ":任务拦截器实例化失败");
                interceptorList.add(instance);
            }
        }
    }

    public String getPreInterceptors() {
        return preInterceptors;
    }

    public String getPostInterceptors() {
        return postInterceptors;
    }

    public List<TaskInterceptor> getPreInterceptorList() {
        return preInterceptorList;
    }

    public List<TaskInterceptor> getPostInterceptorList() {
        return postInterceptorList;
    }
}
