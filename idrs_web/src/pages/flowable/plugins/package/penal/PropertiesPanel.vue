<template>
  <div class="process-panel__container" :style="{ width: `${this.width}px` }">
    <el-collapse v-model="activeTab">
      <el-collapse-item name="base">
        <div slot="title" class="panel-tab__title"><i class="el-icon-info"></i>常规</div>
        <element-base-info :id-edit-disabled="idEditDisabled" :business-object="elementBusinessObject" :type="elementType" />
      </el-collapse-item>
      <!-- <el-collapse-item name="message" v-if="elementType === 'Process'" key="message">
        <div slot="title" class="panel-tab__title"><i class="el-icon-s-comment"></i>消息与信号</div>
        <signal-and-massage />
      </el-collapse-item> -->
      <el-collapse-item name="condition" v-if="conditionFormVisible" key="condition">
        <div slot="title" class="panel-tab__title"><i class="el-icon-s-promotion"></i>流转条件</div>
        <flow-condition :id="elementId" :business-object="elementBusinessObject" :type="elementType" />
      </el-collapse-item>
      <!-- <el-collapse-item name="form" v-if="elementType === 'UserTask' || elementType === 'StartEvent'" key="form">
        <div slot="title" class="panel-tab__title"><i class="el-icon-s-order"></i>表单</div>
        <element-form :id="elementId" :type="elementType" />
      </el-collapse-item> -->
      <el-collapse-item name="task" v-if="elementType === 'UserTask'||elementType === 'ServiceTask'||elementType === 'ScriptTask'" key="task">
        <div slot="title" class="panel-tab__title"><i class="el-icon-s-claim"></i>任务</div>
        <element-task :id="elementId" :type="elementType" />
      </el-collapse-item>
      <el-collapse-item name="btn" v-if="elementType === 'UserTask'" key="btn">
        <div slot="title" class="panel-tab__title"><i class="el-icon-s-tools"></i>按钮配置</div>
        <element-btn :id="elementId" :type="elementType" />
      </el-collapse-item>
      <el-collapse-item name="copy" v-if="elementType === 'UserTask'" key="copy">
        <div slot="title" class="panel-tab__title"><i class="el-icon-s-claim"></i>抄送</div>
        <element-copy :id="elementId" :type="elementType" />
      </el-collapse-item>
      <!-- <el-collapse-item name="multiInstance" v-if="elementType.indexOf('Task') !== -1 && elementType !== 'UserTask'" key="multiInstance">
        <div slot="title" class="panel-tab__title"><i class="el-icon-s-help"></i>多实例</div>
        <element-multi-instance :business-object="elementBusinessObject" :type="elementType" />
      </el-collapse-item> -->
      <el-collapse-item name="env" key="env" v-if="elementType === 'Process'">
        <div slot="title" class="panel-tab__title"><i class="el-icon-s-cooperation"></i>流程参数</div>
        <element-env :id="elementId" />
      </el-collapse-item>
      <el-collapse-item name="validate" key="validate" v-if="elementType === 'Process'">
        <div slot="title" class="panel-tab__title"><i class="el-icon-phone"></i>回调报文校验</div>
        <element-callback-validate :id="elementId" />
      </el-collapse-item>
      <el-collapse-item name="fileUpload" key="fileUpload" v-if="elementType === 'StartEvent'">
        <div slot="title" class="panel-tab__title"><i class="el-icon-upload"></i>附件上传</div>
        <element-file-upload :id="elementId" />
      </el-collapse-item>
      <el-collapse-item name="refuseCallback" key="refuseCallback" v-if="elementType === 'StartEvent'">
        <div slot="title" class="panel-tab__title"><i class="el-icon-error"></i>撤销、拒绝回调</div>
        <element-refuse-callback :id="elementId" />
      </el-collapse-item>
      <el-collapse-item name="repeatApproved" key="repeatApproved" v-if="elementType === 'StartEvent'">
        <div slot="title" class="panel-tab__title"><i class="el-icon-s-custom"></i>重复审批</div>
        <element-repeat-approved :id="elementId" />
      </el-collapse-item>
      <el-collapse-item name="listeners" v-if="elementType !== 'UserTask'" key="listeners">
        <div slot="title" class="panel-tab__title"><i class="el-icon-message-solid"></i>执行监听器</div>
        <element-listeners :id="elementId" :type="elementType" />
      </el-collapse-item>
      <el-collapse-item name="taskListeners" v-if="elementType === 'UserTask'" key="taskListeners">
        <div slot="title" class="panel-tab__title"><i class="el-icon-message-solid"></i>任务监听器</div>
        <user-task-listeners :id="elementId" :type="elementType" />
      </el-collapse-item>
      <el-collapse-item name="extensions" key="extensions">
        <div slot="title" class="panel-tab__title"><i class="el-icon-circle-plus"></i>扩展属性</div>
        <element-properties :id="elementId" :type="elementType" />
      </el-collapse-item>
      <el-collapse-item name="other" key="other">
        <div slot="title" class="panel-tab__title"><i class="el-icon-s-promotion"></i>其他</div>
        <element-other-config :id="elementId" />
      </el-collapse-item>
    </el-collapse>
  </div>
</template>
<script>
import ElementBaseInfo from "./base/ElementBaseInfo";
import ElementOtherConfig from "./other/ElementOtherConfig";
import ElementTask from "./task/ElementTask";
import ElementMultiInstance from "./multi-instance/ElementMultiInstance";
import FlowCondition from "./flow-condition/FlowCondition";
import SignalAndMassage from "./signal-message/SignalAndMessage";
import ElementListeners from "./listeners/ElementListeners";
import ElementProperties from "./properties/ElementProperties";
import ElementForm from "./form/ElementForm";
import UserTaskListeners from "./listeners/UserTaskListeners";
import ElementCallbackValidate from "./callbackValidate/ElementCallbackValidate.vue";
import ElementFileUpload from "./fileUpload/elementFileUpload.vue";
import ElementBtn from "./btn/elementBtn.vue";
import ElementRefuseCallback from "./refuseCallback/ElementRefuseCallback.vue";
import ElementRepeatApproved from "./repeatApproved/ElementRepeatApproved.vue";
import ElementEnv from "./env/ElementEnv.vue";
import ElementCopy from "./copy/ElementCopy.vue";

/**
 * 侧边栏
 * @Author MiyueFE
 * @Home https://github.com/miyuesc
 * @Date 2021年3月31日18:57:51
 */
export default {
	name: "BpmnPropertiesPanel",
	components: {
		UserTaskListeners,
		ElementForm,
		ElementProperties,
		ElementListeners,
		SignalAndMassage,
		FlowCondition,
		ElementMultiInstance,
		ElementTask,
		ElementOtherConfig,
		ElementBaseInfo,
		ElementCallbackValidate,
		ElementFileUpload,
		ElementBtn,
		ElementRefuseCallback,
		ElementRepeatApproved,
		ElementEnv,
		ElementCopy,
	},
	componentName: "BpmnPropertiesPanel",
	props: {
		bpmnModeler: Object,
		prefix: {
			type: String,
			default: "camunda",
		},
		width: {
			type: Number,
			default: 450,
		},
		idEditDisabled: {
			type: Boolean,
			default: false,
		},
	},
	provide() {
		return {
			prefix: this.prefix,
			width: this.width,
		};
	},
	data() {
		return {
			activeTab: ["base", "condition", "form", "task", "btn", "env", "validate", "fileUpload", "refuseCallback", "repeatApproved"],
			elementId: "",
			elementType: "",
			elementBusinessObject: {}, // 元素 businessObject 镜像，提供给需要做判断的组件使用
			conditionFormVisible: false, // 流转条件设置
		};
	},
	watch: {
		elementId: {
			handler() {},
		},
	},
	created() {
		this.initModels();
	},
	methods: {
		initModels() {
			// 初始化 modeler 以及其他 moddle
			if (!this.bpmnModeler) {
				// 避免加载时 流程图 并未加载完成
				this.timer = setTimeout(() => this.initModels(), 10);
				return;
			}
			if (this.timer) clearTimeout(this.timer);
			window.bpmnInstances = {
				modeler: this.bpmnModeler,
				modeling: this.bpmnModeler.get("modeling"),
				moddle: this.bpmnModeler.get("moddle"),
				eventBus: this.bpmnModeler.get("eventBus"),
				bpmnFactory: this.bpmnModeler.get("bpmnFactory"),
				elementFactory: this.bpmnModeler.get("elementFactory"),
				elementRegistry: this.bpmnModeler.get("elementRegistry"),
				replace: this.bpmnModeler.get("replace"),
				selection: this.bpmnModeler.get("selection"),
			};
			this.getActiveElement();
		},
		getActiveElement() {
			// 初始第一个选中元素 bpmn:Process
			this.initFormOnChanged(null);
			this.bpmnModeler.on("import.done", (e) => {
				this.initFormOnChanged(null);
			});
			// 监听选择事件，修改当前激活的元素以及表单
			this.bpmnModeler.on("selection.changed", ({ newSelection }) => {
				this.initFormOnChanged(newSelection[0] || null);
			});
			this.bpmnModeler.on("element.changed", ({ element }) => {
				// 保证 修改 "默认流转路径" 类似需要修改多个元素的事件发生的时候，更新表单的元素与原选中元素不一致。
				if (element && element.id === this.elementId) {
					this.initFormOnChanged(element);
				}
			});
		},
		// 初始化数据
		initFormOnChanged(element) {
			let activatedElement = element;
			if (!activatedElement) {
				let val1 = window.bpmnInstances.elementRegistry.find((el) => el.type === "bpmn:Process");
				let val2 = window.bpmnInstances.elementRegistry.find((el) => el.type === "bpmn:Collaboration");
				if (val1) {
					activatedElement = val1;
				} else {
					activatedElement = val2;
				}
				// activatedElement =
				// 	window.bpmnInstances.elementRegistry.find((el) => el.type === "bpmn:Process") ??
				// 	window.bpmnInstances.elementRegistry.find((el) => el.type === "bpmn:Collaboration");
			}
			if (!activatedElement) return;
			console.log(`
              ----------
              select element changed:
                id:  ${activatedElement.id}
              type:  ${activatedElement.businessObject.$type}
              ----------
              `);
			console.log("businessObject: ", activatedElement.businessObject);
			console.log("window.bpmnInstances: ", window.bpmnInstances);

			window.bpmnInstances.bpmnElement = activatedElement;
			this.bpmnElement = activatedElement;
			this.elementId = activatedElement.id;
			this.elementType = activatedElement.type.split(":")[1] || "";
			this.elementBusinessObject = JSON.parse(JSON.stringify(activatedElement.businessObject));
			this.conditionFormVisible = !!(
				this.elementType === "SequenceFlow" &&
				activatedElement.source &&
				activatedElement.source.type.indexOf("StartEvent") === -1
			);
		},
		beforeDestroy() {
			window.bpmnInstances = null;
		},
	},
};
</script>
