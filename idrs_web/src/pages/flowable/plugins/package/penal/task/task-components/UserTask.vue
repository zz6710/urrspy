<template>
  <div>
    <el-row>
      <h4><b>审批人设置</b></h4>
    </el-row>
    <el-row>
      <el-form-item v-if="showApplyTask">
        <template slot="label">
          <el-tooltip content="发起流程后由发起人自动审批" placement="top-start" @click.stop.prevent>
            <i class="header-icon el-icon-info"></i>
          </el-tooltip>
          <span>申请任务</span>
        </template>
        <el-switch v-model="isApplyTask" @change="changeIsApplyTask" />
      </el-form-item>
      <template v-if="!isApplyTask">
        <el-form-item label="用户">
          <el-select v-model="userIds" multiple size="mini" placeholder="请搜索 用户" filterable remote :loading="userOptionsLoading" :remote-method="userOptionsRemoteMethod" @change="changeSelectUsers">
            <el-option v-for="item in userOptionsCopy" :key="item.userid" :label="item.username" :value="item.userid">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="roleIds" multiple size="mini" placeholder="请选择 角色" @change="changeSelectRoles">
            <el-option v-for="item in roleOptions" :key="item.roleid" :label="item.rolename" :value="item.roleid" :disabled="item.status === 1">
            </el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item v-if="roleIds.length>0">
          <el-radio-group v-model="roleType">
            <el-radio @click.native.prevent="changeRoleType(item.value)" v-for="item in roleTypeRedios" :label="item.value" :key="item.value">{{item.label}}</el-radio>
          </el-radio-group>
        </el-form-item> -->
        <el-form-item label="动态审批人">
          <el-select v-model="candidateParam" size="mini" clearable placeholder="请选择 流程参数" @change="changeSelectParam">
            <el-option v-for="item in envItems" :key="item.envItemId" :label="item.itemKey" :value="item.envItemId">
            </el-option>
          </el-select>
        </el-form-item>
      </template>
    </el-row>
    <el-row v-if="!isApplyTask">
      <el-divider />
      <h4><b>会签</b></h4>
      <el-form-item label="会签类型">
        <el-select v-model="multiLoopType" size="mini" placeholder="请选择 类型" @change="changeMultiLoopType">
          <el-option v-for="item in multiLoopTypeOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <template v-if="multiLoopType!=='Null'">
        <el-form-item label="数字类型">
          <el-radio-group v-model="numType" @change="changeNumType">
            <el-radio v-for="item in numTypeRedios" :label="item.value" :key="item.value">{{item.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="完成条件">
          <el-input-number v-model="completionConditionNum" @change="changeCondition" :min="0"></el-input-number>
        </el-form-item>
      </template>
    </el-row>
  </div>

</template>

<script>
const userTaskForm = {
	candidateUsers: "",
	candidateGroups: "",
	text: "",
	candidateParam: "",
	roleType: "",
	assignee: "",
};
import { getEnvItems } from "../../../utils";
export default {
	name: "UserTask",
	props: {
		id: String,
		type: String,
	},
	inject: {
		prefix: "prefix",
		width: "width",
	},
	data() {
		return {
			userOptionsLoading: false,
			userOptions: [],
			userOptionsCopy: [],
			roleOptions: [],
			roleIds: [],
			userIds: [],
			multiLoopType: "Null",
			candidateParam: "",
			envItems: [],
			multiLoopTypeOptions: [
				{ label: "并行", value: "ParallelMultiInstance" },
				{ label: "串行", value: "SequentialMultiInstance" },
				{ label: "无", value: "Null" },
			],
			completionConditionNum: 0,
			numType: "number",
			numTypeRedios: [
				{ label: "数字", value: "number" },
				{ label: "百分比", value: "scale" },
			],
			roleType: null,
			roleTypeRedios: [
				{ label: "同级机构", value: "peerOrg" },
				{ label: "上级机构", value: "upperOrg" },
			],
			isApplyTask: false,
			showApplyTask: true,
		};
	},
	watch: {
		id: {
			immediate: true,
			handler() {
				this.bpmnElement = window.bpmnInstances.bpmnElement;
				this.$nextTick(() => {
					this.getEnvItemList();
					this.resetTaskForm();
				});
			},
		},
	},
	beforeDestroy() {
		this.bpmnElement = null;
	},
	created() {
		this.getRoleOptions();
	},
	methods: {
		changeIsApplyTask(val) {
			Object.keys(userTaskForm).forEach((key) => (userTaskForm[key] = null));
			if (val) {
				//清空其他数据
				this.clearOptionsData();
				this.multiLoopType = "Null";
				this.changeMultiLoopType("Null");
				//设置发起人
				userTaskForm.assignee = "${initiator}";
				userTaskForm.text = "流程发起人";
			}
			this.updateElementTask();
		},
		hasApplyTask() {
			let flowElements = window.bpmnInstances.modeler.getDefinitions().rootElements[0].flowElements;
			if (flowElements && flowElements.length > 0) {
				let count = flowElements.filter((t) => {
					return t.assignee == "${initiator}";
				}).length;
				if (count > 0) {
					this.showApplyTask = false;
				} else {
					this.showApplyTask = true;
				}
			}
		},
		getEnvItemList() {
			getEnvItems().then((rows) => {
				this.envItems = rows;
			});
		},
		userOptionsRemoteMethod(query) {
			if (query !== "") {
				this.userOptionsLoading = true;
				setTimeout(() => {
					this.userOptionsLoading = false;
					this.userOptionsCopy = this.userOptions.filter((item) => {
						if (item.username.toLowerCase().indexOf(query.toLowerCase()) > -1) {
							return true;
						} else if (this.userIds.indexOf(item.userid) >= 0) {
							return true;
						} else {
							return false;
						}
					});
				}, 200);
			}
		},
		resetTaskForm() {
			this.hasApplyTask();
			const bpmnElementObj = this.bpmnElement?.businessObject;
			if (!bpmnElementObj) {
				return;
			}
			this.clearOptionsData();
			//处理是否申请任务
			if (bpmnElementObj["assignee"] === "${initiator}") {
				this.isApplyTask = true;
				this.showApplyTask = true;
			} else {
				this.isApplyTask = false;
			}
			//处理用户
			Promise.all([this.getUserOptions()]).then((data) => {
				let userIdData = bpmnElementObj["candidateUsers"];
				if (userIdData && userIdData.length > 0) {
					this.userIds = userIdData?.toString().split(",");
					userTaskForm.candidateUsers = this.userIds;
				}
				this.userOptionsCopy = this.userOptions.filter((k) => this.userIds.indexOf(k.userid) >= 0);
			});
			//处理角色
			let roleIdData = bpmnElementObj["candidateGroups"];
			if (roleIdData && roleIdData.length > 0) {
				this.roleIds = roleIdData?.toString().split(",");
				userTaskForm.candidateGroups = this.roleIds;
			}
			//处理动态审批人
			this.candidateParam = bpmnElementObj["candidateParam"];
			userTaskForm.candidateParam = bpmnElementObj["candidateParam"];
			//处理角色类
			this.roleType = bpmnElementObj["roleType"];
			userTaskForm.roleType = bpmnElementObj["roleType"];
			//处理中文text
			userTaskForm.text = bpmnElementObj["text"];
			userTaskForm.assignee = bpmnElementObj["assignee"];
			this.getElementLoop(bpmnElementObj);
		},
		/**
		 * 清空选项数据
		 */
		clearOptionsData() {
			this.roleIds = [];
			this.userIds = [];
			this.candidateParam = "";
			this.roleType = "";
		},
		/**
		 * 跟新节点数据
		 */
		updateElementTask() {
			const taskAttr = Object.create(null);
			for (let key in userTaskForm) {
				taskAttr[key] = userTaskForm[key];
			}
			window.bpmnInstances.modeling.updateProperties(this.bpmnElement, taskAttr);
		},
		/**
		 * 查询角色
		 */
		getRoleOptions() {
			return this.httpUtil
				.ajaxJson({
					url: "wf/system/role/list.json",
				})
				.then((response) => {
					this.roleOptions = response.rows;
				});
		},
		/** 查询用户列表 */
		getUserOptions() {
			return this.httpUtil
				.ajaxJson({
					url: "wf/system/user/list.json",
					params: { start: 0, limit: 10000000 },
				})
				.then((response) => {
					this.userOptions = response.rows;
				});
		},
		changeSelectRoles(val) {
			userTaskForm.candidateGroups = val.join() || null;
			this.changeText();
			this.updateElementTask();
		},
		changeSelectUsers(val) {
			userTaskForm.candidateUsers = val.join() || null;
			this.changeText();
			this.updateElementTask();
		},

		changeText() {
			//处理用户角色的text
			let userTextArr = this.userOptions.filter((k) => this.userIds.indexOf(k.userid) >= 0);
			let roleTextArr = this.roleOptions.filter((k) => this.roleIds.indexOf(k.roleid) >= 0);
			let envItemTextArr = this.envItems.filter((k) => this.candidateParam == k.envItemId);
			let roleText = roleTextArr?.map((k) => k.rolename).join() || null;
			let userText = userTextArr?.map((k) => k.username).join() || null;
			let envItemText = envItemTextArr?.map((k) => k.itemKey).join() || null;

			let texts = [];
			texts.push(userText);
			texts.push(roleText);
			texts.push(envItemText);

			userTaskForm.text = texts.filter((t) => t != null).join(",");
		},
		changeSelectParam(val) {
			userTaskForm.candidateParam = val || null;
			this.changeText();
			this.updateElementTask();
		},
		changeRoleType(val) {
			//重复点击时，取消选中
			val === this.roleType ? (this.roleType = null) : (this.roleType = val);
			userTaskForm.roleType = this.roleType || null;
			this.updateElementTask();
		},
		getElementLoop(businessObject) {
			if (!businessObject.loopCharacteristics) {
				this.multiLoopType = "Null";
				return;
			}
			let isSequential = businessObject.loopCharacteristics.isSequential;
			if (isSequential) {
				this.multiLoopType = "SequentialMultiInstance";
			} else {
				this.multiLoopType = "ParallelMultiInstance";
			}
			this.multiLoopInstance = window.bpmnInstances.moddle.create("bpmn:MultiInstanceLoopCharacteristics", {
				isSequential: isSequential,
			});
			if (businessObject.loopCharacteristics.completionCondition?.body) {
				let conditionBody = businessObject.loopCharacteristics.completionCondition.body;
				//解析数字类型
				this.numTypeRedios.forEach((t) => {
					if (conditionBody.indexOf(t.value) != -1) {
						this.numType = t.value;
					}
				});
				//提取出数字
				this.completionConditionNum = conditionBody.match(/\d+(\.\d+)?/g)[0];
			}
		},
		//修改完成条件
		changeCondition() {
			this.changeMultiLoopType(this.multiLoopType);
		},
		//修改数字类型
		changeNumType() {
			this.changeMultiLoopType(this.multiLoopType);
		},
		//修改会签类型
		changeMultiLoopType(type) {
			// 取消多实例配置
			if (type === "Null" || !type) {
				userTaskForm.assignee = null;
				window.bpmnInstances.modeling.updateProperties(this.bpmnElement, { loopCharacteristics: null, assignee: null });
				return;
			}
			if (type === "SequentialMultiInstance") {
				this.multiLoopInstance = window.bpmnInstances.moddle.create("bpmn:MultiInstanceLoopCharacteristics", { isSequential: true });
			}
			if (type === "ParallelMultiInstance") {
				this.multiLoopInstance = window.bpmnInstances.moddle.create("bpmn:MultiInstanceLoopCharacteristics", { isSequential: false });
			}
			userTaskForm.assignee = "${assignee}";
			// 更新多实例配置
			window.bpmnInstances.modeling.updateProperties(this.bpmnElement, {
				loopCharacteristics: this.multiLoopInstance,
				assignee: "${assignee}",
			});
			// 完成条件
			let completionCondition = window.bpmnInstances.moddle.create("bpmn:FormalExpression", {
				body: "${multiInstanceHandler.isCompleted(execution,'" + this.numType + "'," + this.completionConditionNum + ")}",
			});
			// 更新模块属性信息
			window.bpmnInstances.modeling.updateModdleProperties(this.bpmnElement, this.multiLoopInstance, {
				collection: "${multiInstanceHandler.getUserIds(execution)}",
				elementVariable: "assignee",
				completionCondition,
			});
		},
	},
};
</script>

<style scoped lang="scss">
</style>
