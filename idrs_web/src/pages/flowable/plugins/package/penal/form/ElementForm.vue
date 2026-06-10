<template>
  <div class="panel-tab__content">
    <el-form size="mini" label-width="125px" @submit.native.prevent>
      <el-form-item :label="type=='StartEvent'?'全局审批表单类型':'审批表单类型'" prop="formType">
        <el-select v-model="formType" placeholder="请选择表单类型" @change="updatFormType" clearable filterable>
          <el-option v-for="item in formTypes" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item v-if="formType" :label="type=='StartEvent'?'全局审批表单':'审批表单'">
        <el-select v-if="formType=='formFieldId'" v-model="formKey" placeholder="请选择表单" @change="updateFormKey" clearable filterable>
          <el-option v-for="item in formOptions" :key="item.formFieldId" :label="item.displayName" :value="item.formFieldId" />
        </el-select>
        <el-select v-else-if="formType=='dynamicFormId'" v-model="formKey" placeholder="请选择表单" @change="updateFormKey" clearable filterable>
          <el-option v-for="item in dynamicForms" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
        <el-input v-else-if="formType=='formComponentName'" v-model="formKey" placeholder="请输入内容" @change="updateFormKey" clearable />
      </el-form-item>
      <!-- <template v-if="type=='UserTask'">
        <el-form-item :label="type=='StartEvent'?'全局业务表单类型':'业务表单类型'" prop="busiFormType">
          <el-select v-model="busiFormType" placeholder="请选择表单类型" @change="updatBusiFormType" clearable filterable>
            <el-option v-for="item in formTypes" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item :label="type=='StartEvent'?'全局业务表单':'业务表单'">
          <el-select v-if="busiFormType=='formFieldId'" v-model="busiFormKey" placeholder="请选择表单" @change="updateBusiFormKey" filterable>
            <el-option v-for="item in formOptions" :key="item.formFieldId" :label="item.displayName" :value="item.formFieldId" />
          </el-select>
          <el-select v-else-if="busiFormType=='dynamicFormId'" v-model="busiFormKey" placeholder="请选择表单" @change="updateBusiFormKey" clearable filterable>
            <el-option v-for="item in dynamicForms" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
          <el-input v-else-if="busiFormType=='formComponentName'" v-model="busiFormKey" placeholder="请输入内容" @change="updateBusiFormKey" clearable />
        </el-form-item>
      </template> -->
    </el-form>
  </div>
</template>

<script>
import Tools from "@/utils/tools.js";
export default {
	name: "ElementForm",
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
			formOptions: [],
			dynamicForms: [],
			formKey: "",
			busiFormKey: "",
			formType: "",
			busiFormType: "",
			formTypes: [
				{
					id: "formFieldId",
					name: "表单",
				},
				{
					id: "dynamicFormId",
					name: "动态表单",
				},
				{
					id: "formComponentName",
					name: "表单组件",
				},
			],
		};
	},
	watch: {
		id: {
			immediate: true,
			handler(val) {
				val &&
					val.length &&
					this.$nextTick(() => {
						this.reset();
					});
			},
		},
	},
	created() {
		this.getFormList();
		//this.getDynamicForms();
	},
	methods: {
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

		reset() {
			this.bpmnELement = window.bpmnInstances.bpmnElement;

			this.formKey = this.bpmnELement.businessObject.formKey;
			this.busiFormKey = this.bpmnELement.businessObject.busiFormKey;

			this.formType = this.bpmnELement.businessObject.formType;
			this.busiFormType = this.bpmnELement.businessObject.busiFormType;
		},
		updatFormType() {
			window.bpmnInstances.modeling.updateProperties(this.bpmnELement, { formType: this.formType });
			this.formKey = null;
			this.updateFormKey();
		},
		updatBusiFormType() {
			window.bpmnInstances.modeling.updateProperties(this.bpmnELement, { busiFormType: this.busiFormType });
			this.busiFormKey = null;
			this.updateBusiFormKey();
		},
		updateFormKey() {
			window.bpmnInstances.modeling.updateProperties(this.bpmnELement, { formKey: this.formKey });
		},
		updateBusiFormKey() {
			window.bpmnInstances.modeling.updateProperties(this.bpmnELement, { busiFormKey: this.busiFormKey });
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
};
</script>
