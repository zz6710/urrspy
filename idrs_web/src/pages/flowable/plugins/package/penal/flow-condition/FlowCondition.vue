<template>
  <div class="panel-tab__content">
    <el-form :model="flowConditionForm" label-width="90px" size="mini" @submit.native.prevent>
      <el-form-item label="流转类型">
        <el-select v-model="flowConditionForm.type" @change="updateFlowType">
          <el-option label="普通流转路径" value="normal" />
          <el-option label="默认流转路径" value="default" />
          <el-option label="条件流转路径" value="condition" />
        </el-select>
      </el-form-item>
      <template v-if="flowConditionForm.type=='condition'">
        <el-form-item label="条件类型">
          <el-radio-group v-model="conditionType" @input="conditionTypeChange">
            <el-radio label="custom">自定义条件</el-radio>
            <el-radio label="config">配置条件</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item key="express" v-if="conditionType==='custom'">
          <template slot="label">
            <el-tooltip placement="top-start" @click.stop.prevent>
              <template slot="content">
                示例：<br>
                &nbsp;数字：${num >= 1}<br>
                &nbsp;字符串：${name == '张三'}<br>
                &nbsp;字符串长度：${var:length(name) == 4}<br>
                &nbsp;解析流程参数：${param.流程参数名 == '张三'}<br>
                &nbsp;解析表单参数：${form.name == '张三'} 或 ${form.user.name == '张三'}
              </template>
              <i class="header-icon el-icon-info"></i>
            </el-tooltip>
            表达式
          </template>
          <el-tooltip placement="top-start" @click.stop.prevent :disabled="conditionTooltipDisabled">
            <template slot="content">
              <span v-html='envItemStr'></span>
            </template>
            <el-input v-model="flowConditionForm.body" clearable @change="updateFlowCondition" type="textarea" autosize />
          </el-tooltip>
        </el-form-item>
        <ConditionGroup @updateConditionExpression="updateConditionExpression" v-else />
      </template>
    </el-form>
  </div>
</template>

<script>
import ConditionGroup from "./ConditionGroup.vue";
import { getEnvItems } from "../../utils";

export default {
	name: "FlowCondition",
	components: { ConditionGroup },
	props: {
		businessObject: Object,
		type: String,
		id: String,
	},
	inject: {
		prefix: "prefix",
		width: "width",
	},
	data() {
		return {
			flowConditionForm: {},
			envItemStr: "",
			conditionTooltipDisabled: true,
			conditionType: "custom",
		};
	},
	watch: {
		id: {
			immediate: true,
			handler() {
				this.$nextTick(() => {
					this.getEnvItemList();
					this.resetFlowCondition();
				});
			},
		},
	},
	methods: {
		conditionTypeChange(value) {
			window.bpmnInstances.modeling.updateProperties(this.bpmnElement, { conditionType: value });
		},
		updateConditionExpression(conditionExpression) {
			this.flowConditionForm.body = conditionExpression;
		},
		getEnvItemList() {
			this.conditionTooltipDisabled = true;
			getEnvItems().then((rows) => {
				this.envItemStr = "流程参数：<br>";
				if (rows && rows.length > 0) {
					for (let row of rows) {
						this.envItemStr = this.envItemStr + "&nbsp;" + row.itemKey + "<br>";
					}
					this.conditionTooltipDisabled = false;
				} else {
					this.conditionTooltipDisabled = true;
				}
			});
		},
		resetFlowCondition() {
			this.bpmnElement = window.bpmnInstances.bpmnElement;
			this.bpmnElementSource = this.bpmnElement.source;
			this.bpmnElementSourceRef = this.bpmnElement.businessObject.sourceRef;
			this.conditionType = this.bpmnElement.businessObject.conditionType ?? this.conditionType;
			if (this.bpmnElementSourceRef && this.bpmnElementSourceRef.default && this.bpmnElementSourceRef.default.id === this.bpmnElement.id) {
				// 默认
				this.flowConditionForm = { type: "default" };
			} else if (!this.bpmnElement.businessObject.conditionExpression) {
				// 普通
				this.flowConditionForm = { type: "normal" };
			} else {
				// 带条件
				const conditionExpression = this.bpmnElement.businessObject.conditionExpression;
				this.flowConditionForm = { ...conditionExpression, type: "condition" };
				// resource 可直接标识 是否是外部资源脚本
				if (this.flowConditionForm.resource) {
					this.$set(this.flowConditionForm, "conditionType", "script");
					this.$set(this.flowConditionForm, "scriptType", "externalScript");
					return;
				}
				if (conditionExpression.language) {
					this.$set(this.flowConditionForm, "conditionType", "script");
					this.$set(this.flowConditionForm, "scriptType", "inlineScript");
					return;
				}
				this.$set(this.flowConditionForm, "conditionType", "expression");
			}
		},
		updateFlowType(flowType) {
			// 正常条件类
			if (flowType === "condition") {
				this.flowConditionRef = window.bpmnInstances.moddle.create("bpmn:FormalExpression");
				window.bpmnInstances.modeling.updateProperties(this.bpmnElement, {
					conditionExpression: this.flowConditionRef,
				});
				return;
			}
			// 默认路径
			if (flowType === "default") {
				window.bpmnInstances.modeling.updateProperties(this.bpmnElement, {
					conditionExpression: null,
				});
				window.bpmnInstances.modeling.updateProperties(this.bpmnElementSource, {
					default: this.bpmnElement,
				});
				return;
			}
			// 正常路径，如果来源节点的默认路径是当前连线时，清除父元素的默认路径配置
			if (this.bpmnElementSourceRef.default && this.bpmnElementSourceRef.default.id === this.bpmnElement.id) {
				window.bpmnInstances.modeling.updateProperties(this.bpmnElementSource, {
					default: null,
				});
			}
			window.bpmnInstances.modeling.updateProperties(this.bpmnElement, {
				conditionExpression: null,
			});
		},
		updateFlowCondition() {
			let { conditionType, scriptType, body, resource, language } = this.flowConditionForm;
			let condition;
			if (conditionType === "expression") {
				condition = window.bpmnInstances.moddle.create("bpmn:FormalExpression", { body });
			} else {
				if (scriptType === "inlineScript") {
					condition = window.bpmnInstances.moddle.create("bpmn:FormalExpression", { body, language });
					this.$set(this.flowConditionForm, "resource", "");
				} else {
					this.$set(this.flowConditionForm, "body", "");
					condition = window.bpmnInstances.moddle.create("bpmn:FormalExpression", { resource, language });
				}
			}
			window.bpmnInstances.modeling.updateProperties(this.bpmnElement, { conditionExpression: condition });
		},
	},
	beforeDestroy() {
		this.bpmnElement = null;
		this.bpmnElementSource = null;
		this.bpmnElementSourceRef = null;
	},
};
</script>
