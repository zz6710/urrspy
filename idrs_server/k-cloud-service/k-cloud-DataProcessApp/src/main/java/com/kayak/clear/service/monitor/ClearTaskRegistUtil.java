package com.kayak.clear.service.monitor;

import com.kayakwise.kcloud.batch.model.entity.KbatchTaskExec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Set;

/**
 * 文件名: ClearTaskRegistUtil.java
 * 描述:
 * 创建人: zengzt
 * 创建时间:2020年4月23日上午11:26:38
 */
public class ClearTaskRegistUtil {
    private final static Logger log = LoggerFactory.getLogger(ClearTaskRegistUtil.class);

    //存储清算任务MAP的key分隔符
    private static final String SEPARATE_CHARACTER = "_";

    /**
     * 方法描述:已经注册的系统清算执行任务数据存入的MAP的key
     *
     * @param taskGroup
     * @param taskId
     * @return
     */
    public static String getSysExecKey(String taskGroup, String taskId) {
        return taskGroup + SEPARATE_CHARACTER + taskId;
    }

    /**
     * 方法描述:根据清算类型拼接任务key，放入到taskSet中
     *
     * @param taskSet
     */
    public static Set<String> initTaskSet(KbatchTaskExec taskExec, Set<String> taskSet) {

        //组代码
        String taskGroup = taskExec.getTaskGroup();
        //任务id
        String taskId = taskExec.getTaskId();

        taskSet.add(getSysExecKey(taskGroup, taskId));
        return taskSet;
    }

    /**
     * 方法描述:根据任务类型返回对应缓存任务信息、预展示信息等map的key
     * 产品任务按  task_group、task_id 、prod_code
     * 非产品任务都按task_group,task_id
     *
     * @param taskType   任务类型
     * @param taskGroup  任务组
     * @param taskId     任务ID
     * @param prodCode   产品代码
     * @param targetCode 目标代码
     * @return 拼接结果Key
     */
    public static String getMapKey(String taskType, String taskGroup, String taskId, String prodCode, String targetCode) {
        String mapKey = null;

        //这个拿来做前置任务校验，处理产品是按产品来校验前置任务，其他都是按整个任务，销售商任务也是得等组内所有销售商完成这个任务，前置校验才算通过
//        if (BatchTaskType.isProduct(taskType)) {
//            // 拼接: 组ID_任务ID_产品代码
//            mapKey = getProdExecKey(taskGroup, taskId, prodCode);
//        } else if (BatchTaskType.isDistributor(taskType)) {
//            // 销售商任务也和产品任务一样，要按销售商级别来控制
//            // 拼接: 组ID_任务ID_目标代码
//            mapKey = getDisExecKey(taskGroup, taskId, targetCode);
//        } else {
            // 拼接: 组ID_任务ID
            mapKey = getSysExecKey(taskGroup, taskId);
//        }

        return mapKey;
    }

}
