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
package com.kayak.pms.opFlow.engine.handlers;


import com.kayak.pms.opFlow.engine.model.Execution;
import com.kayak.pms.opFlow.engine.model.WorkModel;

import java.util.List;

/**
 * 用户参与者处理器
 */
public interface UserAssignmentHandler {

	/**
	 *
	 * @param taskModel 当前正在执行的模型
	 * @param execution 当前正在执行的上下文
	 * @return Object的类型
	 * 		返回类型	List<String>  其中list集合中的数据为人员ID，说明该任务可以由该人员审核
	 */
	List<String> assign(WorkModel taskModel, Execution execution);
}
