<template>
  <div>
    <!--动态表单-->
    <CodePreview v-if="formType==='dynamicFormId'" :source="code" :submit-data="submitData" :task-info="taskInfo"></CodePreview>
    <!-- 自定义表单组件 -->
    <component v-else-if="formType==='formComponentName'" :is="formKey" :task-info="taskInfo" :submit-data="submitData" />
    <!-- 界面配置表单 -->
    <k-form v-else-if="formType==='formFieldId'">
      <k-form-item v-for="(item, index) in formFieldList" :key="index" :label="item.displayName">
        <component :is="item.fieldType" v-model="item.value" v-bind="item.extendAttr" :data-allowblank="false" :data-dict="item['data-dict']" :data-disabled="true"></component>
      </k-form-item>
    </k-form>
    <!-- 表单提交字段 -->
    <k-form v-else>
      <k-form-item v-for="(item,index) in businessInfo" :key="index" :label="item.label">
        <k-field-display v-model="item.value" :data-dict="item.dict" />
      </k-form-item>
    </k-form>
  </div>
</template>

  <script>
import CodePreview from "@/pages/design/code-viewer/src/code-preview.vue";
import generateCode from "@/pages/design//utils/generateCode.js";

export default {
	name: "FlowCopyFormInfo",
	components: {
		CodePreview,
	},
	props: {
		taskInfo: {},
	},
	data() {
		return {
			businessInfo: [],
			formFieldList: null,
			code: "",
			submitData: {},
			formType: "",
			formKey: "",
			showOldData: false, //是否显示老数据
		};
	},
	watch: {},
	async created() {
		await this.getFormConf();
		await this.getFormData();
		this.loadForm();
	},
	methods: {
		//获取表单配置
		getFormConf() {
			return this.httpUtil
				.ajaxJson({
					url: "wf/copy/formConf/" + this.taskInfo.procDefId + "/" + this.taskInfo.taskDefKey + ".json",
				})
				.then((res) => {
					this.formType = res.data.formType;
					this.formKey = res.data.formKey;
				});
		},
		//获取表单数据
		getFormData() {
			return this.httpUtil
				.ajaxJson({
					url: "wf/process/formData/" + this.taskInfo.procInsId + ".json",
				})
				.then((res) => {
					this.submitData = res.data;
				});
		},
		//加载表单
		loadForm() {
			if (this.formType === "formComponentName") {
				// 表单组件
				console.log(" 使用表单组件");
			} else if (this.formType === "formFieldId") {
				// 表单
				console.log(" 使用表单配置");
				this.initFormFieldInfo();
			} else if (this.formType === "dynamicFormId") {
				// 动态表单
				console.log(" 使用动态表单");
				this.initDynamicFormInfo();
			} else {
				console.log(" 未设置表单");
				this.initFormLabelInfo();
			}
		},
		dataTypeFliter(item) {
			if (item.fieldType === "k-field-date") {
				return "date";
			} else if (item.fieldType === "k-field-time") {
				return "time";
			} else if (item.extendAttr["data-type"] == "money" || item.extendAttr["dataType"] == "money") {
				return "money";
			}
		},
		initFormLabelInfo() {
			const latestData = this.submitData;
			this.httpUtil
				.ajaxJson({
					url: "wf/process/formLabelInfo/" + this.taskInfo.procInsId + ".json",
				})
				.then((res) => {
					let labelInfoData = res.data;
					let businessInfo = [];
					for (let field in latestData) {
						if (labelInfoData) {
							let info = labelInfoData[field];
							if (!info || !info.label) {
								continue;
							}
							info.value = latestData[field];
							businessInfo.push(info);
						}
					}
					this.businessInfo = businessInfo;
				});
		},
		initFormFieldInfo() {
			let latestData = this.submitData;
			let oldData = {};
			if (latestData.oldData) {
				oldData = JSON.parse(latestData.oldData);
				this.showOldData = true;
			} else {
				this.showOldData = false;
			}
			if (!this.formKey) {
				return;
			}
			let formFieldList = [];
			// 设置了表单
			this.httpUtil
				.ajaxJson({
					url: "wf/formField/get/" + this.formKey + ".json",
				})
				.then((res) => {
					let data = res.data;
					if (data) {
						if (data.formType == "form") {
							data = JSON.parse(data.json);
							if (data.fieldsConf && data.fieldsConf.length > 0) {
								for (let index in data.fieldsConf) {
									let field = data.fieldsConf[index];
									field.value = latestData[field.name];
									field.oldValue = oldData[field.name];
									let attr = field.extendAttr ? field.extendAttr : "{}";
									field.extendAttr = JSON.parse(attr);
									formFieldList.push(field);
								}
								this.formFieldList = formFieldList;
							}
							console.log(" 使用表单配置-表单");
						} else if (data.formType == "mircoFormComponent") {
							//动态导入组件
							this.formType = "formComponentName";
							this.formKey = data.json;
							console.log(" 使用表单配置-表单组件");
						} else if (data.formType == "onlineForm") {
							this.formType = "dynamicFormId";
							this.code = data.json;
							console.log(" 使用表单配置-在线表单");
						}
					}
				});
		},
		initDynamicFormInfo() {
			this.httpUtil
				.comnQuery({
					action: "LowCodeConfig.findConfigById",
					params: { id: this.formKey },
				})
				.then((data) => {
					if (data.success) {
						this.code = generateCode(JSON.parse(data.returndata.json));
					}
				});
		},
	},
};
</script>
<style lang="scss" scoped>
.formItemStyle {
	display: flex;
	align-items: center;
	justify-content: space-around;
	flex: 1;
	.lineStyle {
		width: 1px;
		height: 70%;
		background: #e6e6e6;
		margin: 0 10px;
	}
	.displayRedStyle /deep/ .ds-span {
		min-height: 32px;
		line-height: 31px;
		color: red;
	}
}
</style>