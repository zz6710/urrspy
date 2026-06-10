<template>
  <div class="panel-tab__content">
    <el-form size="mini" label-width="100px" @submit.native.prevent>
      <el-form-item label="ID">
        <el-input v-model="elementBaseInfo.id" :disabled="true" clearable @change="updateBaseInfo('id')" />
      </el-form-item>
      <el-form-item label="名称">
        <el-input v-model="elementBaseInfo.name" clearable @change="updateBaseInfo('name')" />
      </el-form-item>

      <template v-if="type === 'UserTask' || type === 'StartEvent'">
        <el-form-item :label="type=='StartEvent'?'全局表单类型':'表单类型'" prop="formType">
          <el-select v-model="elementBaseInfo.formType" placeholder="请选择表单类型" @change="updateBaseInfo('formType')" clearable filterable style="width:100%">
            <el-option v-for="item in formTypes" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="elementBaseInfo.formType" :label="type=='StartEvent'?'全局表单':'表单'">
          <el-select v-if="elementBaseInfo.formType=='formFieldId'" v-model="elementBaseInfo.formKey" placeholder="请选择表单" @change="updateBaseInfo('formKey')" clearable filterable style="width:100%">
            <el-option v-for="item in formOptions" :key="item.formFieldId" :label="item.displayName" :value="item.formFieldId" />
          </el-select>
          <el-select v-else-if="elementBaseInfo.formType=='dynamicFormId'" v-model="elementBaseInfo.formKey" placeholder="请选择表单" @change="updateBaseInfo('formKey')" clearable filterable>
            <el-option v-for="item in dynamicForms" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
          <el-input v-else-if="elementBaseInfo.formType=='formComponentName'" v-model="elementBaseInfo.formKey" placeholder="请输入内容" @change="updateBaseInfo('formKey')" clearable />
        </el-form-item>
      </template>
    </el-form>
  </div>
</template>
<script>
import Tools from '@/utils/tools.js'
export default {
	name: "ElementBaseInfo",
	props: {
		businessObject: Object,
		type: String,
		idEditDisabled: {
			type: Boolean,
			default: true,
		},
	},
	data() {
		return {
			elementBaseInfo: {},
			formOptions: [],
			formTypes: [
				{
					id: "formFieldId",
					name: "表单",
				},
				{
					id: "dynamicFormId",
					name: "动态表单",
				},
			],
		};
	},
	watch: {
		businessObject: {
			immediate: false,
			handler: function (val) {
				if (val) {
					this.$nextTick(() => this.resetBaseInfo());
				}
			},
		},
	},
	created() {
		this.getFormList();
		//this.getDynamicForms();
	},
	methods: {
		resetBaseInfo() {
			this.bpmnElement = window?.bpmnInstances?.bpmnElement;
			this.elementBaseInfo = JSON.parse(JSON.stringify(this.bpmnElement.businessObject));
		},
		/** 查询表单列表 */
		getFormList() {
			this.httpUtil
				.ajaxJson({
					url: "wf/formField/list.json",
				})
				.then((res) => {
					this.formOptions = res.rows;
				});
		},
		updateBaseInfo(key) {
			const attrObj = Object.create(null);
			attrObj[key] = this.elementBaseInfo[key];
			if (key === "id") {
				window.bpmnInstances.modeling.updateProperties(this.bpmnElement, {
					id: this.elementBaseInfo[key],
					di: { id: `${this.elementBaseInfo[key]}_di` },
				});
			} else if (key === "formType") {
				window.bpmnInstances.modeling.updateProperties(this.bpmnElement, {
					formType: this.elementBaseInfo[key],
					formKey: null,
				});
			} else {
				window.bpmnInstances.modeling.updateProperties(this.bpmnElement, attrObj);
			}
		},
		getDynamicForms() {
			this.httpUtil
				.comnQuery({
					action: "LowCodeSysVersion.getCurrentVersion",
					errCallback: (reData) => {
						Tools.alert("低代码服务调用出错，无法使用动态表单", "warning");
					},
				})
				.then((res) => {
					if (res.success) {
						let version = res.returndata.version;
						this.httpUtil
							.comnQuery({
								action: "LowCodeConfig.page",
								params: {
									sysVersion: version,
								},
							})
							.then((res) => {
								this.dynamicForms = res.rows;
							});
					}
				});
		},
	},
	beforeDestroy() {
		this.bpmnElement = null;
	},
};
</script>
