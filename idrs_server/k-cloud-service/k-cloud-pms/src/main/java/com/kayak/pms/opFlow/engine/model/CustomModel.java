package com.kayak.pms.opFlow.engine.model;

import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.engine.exception.WorkflowException;
import com.kayak.pms.opFlow.engine.handlers.IHandler;
import com.kayak.pms.opFlow.engine.helper.ClassHelper;
import com.kayak.pms.opFlow.engine.helper.ReflectHelper;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by daniel on 20/03/2017.
 */
public class CustomModel extends WorkModel {
    private static final long serialVersionUID = -4833936010768473911L;

    /**
     * 需要执行的class类路径
     */
    private String clazz;
    /**
     * 需要执行的class对象的方法名称
     */
    private String methodName;
    /**
     * 执行方法时传递的参数表达式变量名称
     */
    private String args;
    /**
     * 执行的返回值变量
     */
    private String var;

    /**
     * 加载模型时初始化的对象实例
     */
    private Object invokeObject;

    @Override
    protected void exec(Execution execution) throws Exception {
        if(invokeObject == null) {
            invokeObject = ClassHelper.newInstance(clazz);
        }
        if(invokeObject == null) {
            throw new WorkflowException("自定义模型[class=" + clazz + "]实例化对象失败");
        }

        if(invokeObject instanceof IHandler) {
            IHandler handler = (IHandler)invokeObject;
            handler.handle(execution);
        } else {
            Method method = ReflectHelper.findMethod(invokeObject.getClass(), methodName);
            if(method == null) {
                throw new WorkflowException("自定义模型[class=" + clazz + "]无法找到方法名称:" + methodName);
            }
        }
        runOutTransition(execution);
    }

    /**
     * 根据传递的执行参数、模型的参数列表返回实际的参数对象数组
     * @param execArgs 运行时传递的参数数据
     * @param args 自定义节点需要的参数
     * @return 调用自定义节点类方法的参数数组
     */
    private Object[] getArgs(Map<String, Object> execArgs, String args) {
        Object[] objects = null;
        if(StringHelper.isNotEmpty(args)) {
            String[] argArray = args.split(",");
            objects = new Object[argArray.length];
            for(int i = 0; i < argArray.length; i++) {
                objects[i] = execArgs.get(argArray[i]);
            }
        }
        return objects;
    }
}
