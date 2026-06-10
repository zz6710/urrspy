/* Copyright 2013-2015 www.snakerflow.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kayak.pms.opFlow.engine.handlers.impl;

import com.kayak.pms.opFlow.engine.entity.Task;
import com.kayak.pms.opFlow.engine.handlers.IHandler;
import com.kayak.pms.opFlow.engine.model.Execution;

import java.util.Arrays;
import java.util.List;

/**
 * 合并处理的抽象处理器
 * 需要子类提供查询无法合并的task集合的参数map
 *
 * @author daniel
 */
public abstract class AbstractMergeHandler implements IHandler {

    public void handle(Execution execution) throws Exception {
        /**
         * 查询当前流程实例的无法参与合并的node列表
         * 若所有中间node都完成，则设置为已合并状态，告诉model可继续执行join的输出变迁
         */
        String[] activeNodes = findActiveNodes();
        List<String> activeNodeList = Arrays.asList(activeNodes);
        boolean isTaskMerged = true;

        //查询某个流程实例是否还有相关的任务没有审批
        List<Task> tasks = execution.getEngine().task().listActiveTasksByParentProcessInstanceId(execution.getProcessInstance().getParentProcessId());
        for (Task task : tasks) {
            if (activeNodeList.contains(task.getName())) {
                isTaskMerged = false;
            }
        }
        execution.setMerged(isTaskMerged);
    }

    /**
     * 子类需要提供如何查询未合并任务的参数map
     *
     * @return
     */
    protected abstract String[] findActiveNodes();

}