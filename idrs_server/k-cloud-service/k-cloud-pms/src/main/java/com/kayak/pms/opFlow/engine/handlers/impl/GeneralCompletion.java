/*
 *  Copyright 2013-2015 www.snakerflow.com.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.kayak.pms.opFlow.engine.handlers.impl;

import com.kayak.core.system.SysUtil;
import com.kayak.pms.opFlow.engine.entity.ProcessInstance;
import com.kayak.pms.opFlow.engine.entity.Task;
import com.kayak.pms.opFlow.engine.handlers.Completion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认的任务、实例完成时触发的动作
 */
public class GeneralCompletion implements Completion {
    private static final Logger log = LoggerFactory.getLogger(GeneralCompletion.class);

    public void complete(Task task) {
//        log.info("[{}] 完成了任务[taskId={},taskName={}] ", SysUtil.getUserInfo(), task.getId(), task.getDisplayName());
    }

    public void complete(ProcessInstance processInstance) {
        log.info("[{}]完成了实例[id={}]", SysUtil.getLoginUserid(), processInstance.getProcessInstanceId());
    }
}
