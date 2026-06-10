<template>
  <div>
    <CodePreview ref="form" v-if="busiFormType==='dynamicFormId'" :source="code"></CodePreview>
    <component ref="form" v-else-if="busiFormType==='formComponentName'" :is="busiFormKey" :task-info="taskInfo" />
  </div>
</template>
  
  <script>
import CodePreview from "@/pages/design/code-viewer/src/code-preview.vue";
import generateCode from "@/pages/design//utils/generateCode.js";
import eventBus from "@/utils/eventBus";

export default {
	name: "FlowFillBusiFormInfo",
	components: {
		CodePreview,
	},
	props: {
		taskInfo: {},
		busiFormType: "",
		busiFormKey: "",
	},
	data() {
		return {
			code: "",
		};
	},
	watch: {},
	beforeDestroy() {
		eventBus.$off("getBusiFormData");
	},
	created() {
		this.renderForm();
		eventBus.$on("getBusiFormData", (data, callback) => {
			this.getBusiFormData(data, callback);
		});
	},
	methods: {
		//渲染表单
		renderForm() {
			if (this.busiFormType === "formComponentName") {
				// 表单组件
				console.log(" 使用表单组件");
			} else if (this.busiFormType === "dynamicFormId") {
				// 动态表单
				console.log(" 使用动态表单");
				this.initDynamicFormInfo();
			}
		},
		initDynamicFormInfo() {
			this.httpUtil
				.comnQuery({
					action: "LowCodeConfig.findConfigById",
					params: { id: this.busiFormKey },
				})
				.then((data) => {
					if (data.success) {
						this.code = generateCode(JSON.parse(data.returndata.json));
					}
				});
		},
		//获取表单数据
		getBusiFormData(data, callback) {
			callback(this.$refs.form._props.submitData);
		},
	},
};
</script>
  