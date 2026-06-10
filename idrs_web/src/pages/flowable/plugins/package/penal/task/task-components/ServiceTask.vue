<template>
  <div style="margin-top: 16px">
    <el-form size="mini" :model="serviceTaskForm" label-width="96px" ref="serviceTaskFormRef" @submit.native.prevent>
      <el-form-item label="服务类型" prop="serviceType" :rules="{ required: true, trigger: ['blur', 'change'] }">
        <el-select v-model="serviceTaskForm.serviceType" @change="updateElementTask()">
          <el-option v-for="i in Object.keys(listenerTypeObject)" :key="i" :label="listenerTypeObject[i]" :value="i" />
        </el-select>
      </el-form-item>
      <el-form-item v-if="serviceTaskForm.serviceType === 'paramListener' || serviceTaskForm.serviceType === 'paramListener'" label="流程参数" prop="expression" key="listener-param" :rules="{ required: true, trigger: ['blur', 'change'] }">
        <el-select v-model="serviceTaskForm.expression" size="mini" clearable placeholder="请选择 流程参数 " @change="updateElementTask()">
          <el-option v-for="item in envItems" :key="item.itemKey" :label="item.itemKey" :value="item.itemValue" clearable>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item v-if="serviceTaskForm.serviceType === 'classListener'" label="Java类" prop="class" key="listener-class" :rules="{ required: true, trigger: ['blur', 'change'] }">
        <el-input v-model="serviceTaskForm.class" clearable @change="updateElementTask()" />
      </el-form-item>
      <el-form-item v-if="serviceTaskForm.serviceType === 'expressionListener'" label="表达式" prop="expression" key="listener-expression" :rules="{ required: true, trigger: ['blur', 'change'] }">
        <el-input v-model="serviceTaskForm.expression" clearable @change="updateElementTask()" />
      </el-form-item>
      <el-form-item v-if="serviceTaskForm.serviceType === 'delegateExpressionListener'" label="代理表达式" prop="delegateExpression" key="listener-delegate" :rules="{ required: true, trigger: ['blur', 'change'] }">
        <el-input v-model="serviceTaskForm.delegateExpression" clearable @change="updateElementTask()" />
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { getEnvItems } from "../../../utils";
import wfStatus from "@/pages/flowable/enum/enum.js";
export default {
	name: "ServiceTask",
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
			listenerTypeObject: {
				classListener: "Java 类",
				expressionListener: "表达式",
				delegateExpressionListener: "代理表达式",
				paramListener: "流程参数",
			},
			serviceTaskForm: {},
			envItems: [],
			initServiceTask: {
				class: null,
				expression: null,
				delegateExpression: null,
			},
		};
	},
	watch: {
		id: {
			immediate: true,
			handler() {
				this.bpmnElement = window.bpmnInstances.bpmnElement;
				this.getEnvItemList();
				this.$nextTick(() => this.resetTaskForm());
			},
		},
	},
	created() {},
	methods: {
		getEnvItemList() {
			getEnvItems().then((rows) => {
				this.envItems = rows
					.filter((t) => t.itemType == wfStatus.param.url.value || t.itemType == wfStatus.param.sql.value)
					.map((t) => {
						//拼接表达式
						t.itemValue = "${parseWfParamListener.parseExecutionParam('" + t.envItemId + "',execution)}";
						return t;
					});
			});
		},
		resetTaskForm() {
			if (this.bpmnElement?.businessObject?.class) {
				this.$set(this.serviceTaskForm, "serviceType", "classListener");
				this.$set(this.serviceTaskForm, "class", this.bpmnElement?.businessObject?.class);
			}
			if (this.bpmnElement?.businessObject?.delegateExpression) {
				this.$set(this.serviceTaskForm, "serviceType", "delegateExpressionListener");
				this.$set(this.serviceTaskForm, "delegateExpression", this.bpmnElement?.businessObject?.delegateExpression);
			}
			if (this.bpmnElement?.businessObject?.expression) {
				if (this.bpmnElement?.businessObject?.expression.indexOf("parseWfParamListener") > -1) {
					this.$set(this.serviceTaskForm, "serviceType", "paramListener");
				} else {
					this.$set(this.serviceTaskForm, "serviceType", "expressionListener");
				}
				this.$set(this.serviceTaskForm, "expression", this.bpmnElement?.businessObject?.expression);
			}
		},
		updateElementTask() {
			const taskAttr = Object.assign({}, this.initServiceTask);
			if (this.serviceTaskForm.serviceType == "classListener") {
				taskAttr.class = this.serviceTaskForm.class;
			} else if (this.serviceTaskForm.serviceType == "delegateExpressionListener") {
				taskAttr.delegateExpression = this.serviceTaskForm.delegateExpression;
			} else if (this.serviceTaskForm.serviceType == "paramListener" || this.serviceTaskForm.serviceType == "expressionListener") {
				taskAttr.expression = this.serviceTaskForm.expression;
			}
			window.bpmnInstances.modeling.updateProperties(this.bpmnElement, taskAttr);
		},
	},
	beforeDestroy() {
		this.bpmnElement = null;
	},
};
</script>
  