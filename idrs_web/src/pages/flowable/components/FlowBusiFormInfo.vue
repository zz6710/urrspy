<!--业务表单信息-->
<template>
  <div>
    <el-card class="box-card" shadow="never" :key="index" v-for="(t,index) in currentBusiFormInfo">
      <div slot="header" class="clearfix">
        <span>{{ t.taskName }}</span>
      </div>
      <el-col :span="20" :offset="2">
        <CodePreview v-if="t.busiFormType==='dynamicFormId'" :source="t.code" :submitData="t.variables"></CodePreview>
        <component v-else-if="t.busiFormType==='formComponentName'" :is="t.busiFormKey" :submitData="t.variables" />
      </el-col>
    </el-card>
  </div>
</template>
  
  <script>
import CodePreview from "@/pages/design/code-viewer/src/code-preview.vue";
import generateCode from "@/pages/design//utils/generateCode.js";
import { cloneDeep } from "lodash";

export default {
	name: "FlowBusiFormInfo",
	components: {
		CodePreview,
	},
	props: {
		taskInfo: {},
		busiFormInfo: [],
	},
	data() {
		return {
			currentBusiFormInfo: [],
		};
	},
	watch: {},
	beforeDestroy() {},
	created() {
		this.currentBusiFormInfo = cloneDeep(this.busiFormInfo);
		this.renderForm();
	},
	methods: {
		//渲染表单
		renderForm() {
			this.currentBusiFormInfo.forEach((t) => {
				if (t.busiFormType === "formComponentName") {
					// 表单组件
					console.log(t.taskName, " 使用表单组件");
				} else if (t.busiFormType === "dynamicFormId") {
					// 动态表单
					console.log(t.taskName, " 使用动态表单");
					this.initDynamicFormInfo(t);
				}
			});
		},
		initDynamicFormInfo(t) {
			this.httpUtil
				.comnQuery({
					action: "LowCodeConfig.findConfigById",
					params: { id: t.busiFormKey },
				})
				.then((data) => {
					if (data.success) {
						t.code = generateCode(JSON.parse(data.returndata.json));
					}
				});
		},
	},
};
</script>
<style lang="scss" scoped>
.box-card {
	width: 100%;
	margin-bottom: 20px;
}
</style>
