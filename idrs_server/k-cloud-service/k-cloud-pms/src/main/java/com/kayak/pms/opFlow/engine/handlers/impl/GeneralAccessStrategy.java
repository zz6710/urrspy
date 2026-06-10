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
import com.kayak.pms.opFlow.engine.entity.User;
import com.kayak.pms.opFlow.engine.handlers.TaskAccessStrategy;

/**
 * 基于用户或组（角色、部门等）的访问策略类
 */
public class GeneralAccessStrategy implements TaskAccessStrategy {


    /**
     * 如果操作人id所属的组只要有一项存在于参与者集合中，则表示可访问
     */
    //TODO 默认全部返回, 用于测试
    public boolean isAllowed(String operator, User user, Task task) {
        //获取某人具有的部门与角色id
        return true;
    }
}
